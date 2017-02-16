package com.example.android.popularmovies.asynctasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.example.android.popularmovies.MainActivityFragment;
import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.android.popularmovies.MainActivityFragment.images;
import static com.example.android.popularmovies.MainActivityFragment.imagesBackground;
import static com.example.android.popularmovies.MainActivityFragment.moviesList;
import static com.example.android.popularmovies.utilities.NetworkUtils.MOVIES_BASE_URL;
import static com.example.android.popularmovies.utilities.NetworkUtils.api_key;
import static com.example.android.popularmovies.utilities.NetworkUtils.popular;
import static com.example.android.popularmovies.utilities.NetworkUtils.toprated;

public class FetchMovieTask extends AsyncTask<String,Void, String>{

    Context mContext;

    public FetchMovieTask(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String sortingCriteria = params[0];
        String response = null;
        Uri builtUri = null;

        if(sortingCriteria.contentEquals("Popularity")){
            builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(popular)
                    .appendQueryParameter(api_key,NetworkUtils.APP_ID)
                    .build();
        }

        else if(sortingCriteria.contentEquals("Vote Average")){
            builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(toprated)
                    .appendQueryParameter(api_key,NetworkUtils.APP_ID)
                    .build();
        }

        else if(sortingCriteria.contentEquals("Favourite Movies")){
            return null;
        }



        // Put the URI of Content Provider when we push the Favorite Movies in the Settings Screen.

        try{
            response = getJSON(builtUri);
            return response;
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }


    @Override
    protected void onPostExecute(String response) {

        if(response != null){

            showGridView();
            loadInfo(response);

        }
        else{
            showGridView();
            getFavouriteMovies();
        }
    }

    private void showGridView() {
        MainActivityFragment.gridView.setVisibility(View.VISIBLE);

    }


    public static void loadInfo(String jsonString){

        images.clear();
        moviesList.clear();

        try{
            if(jsonString != null){

                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");


                for(int i= 0; i < moviesArray.length(); i++) {

                    JSONObject movie = moviesArray.getJSONObject(i);
                    Movie movieItem = new Movie();
                    movieItem.setTitle(movie.getString("title"));
                    movieItem.setId(movie.getLong("id"));

                    movieItem.setImage(movie.getString("poster_path"));
                    MainActivityFragment.images.add(NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_185 + movie.getString("poster_path"));


                    movieItem.setImageBackground(movie.getString("backdrop_path"));
                    MainActivityFragment.imagesBackground.add(NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_500 + movie.getString("backdrop_path"));


                    if (movie.getString("overview") == "null") {
                        movieItem.setPlot("Plot Overview is not available");
                    } else {
                        movieItem.setPlot(movie.getString("overview"));
                    }

                    if (movie.getString("release_date") == "null") {
                        movieItem.setDate("Unknown Release date");
                    } else {
                        movieItem.setDate(movie.getString("release_date"));
                    }
                    movieItem.setRatingAvg(movie.getString("vote_average"));

                    FetchMovieComponentsTask fetchReviews = new FetchMovieComponentsTask(movieItem, NetworkUtils.reviews);
                    fetchReviews.execute(NetworkUtils.reviews);

                    FetchMovieComponentsTask fetchTrailers = new FetchMovieComponentsTask(movieItem,NetworkUtils.trailers);
                    fetchTrailers.execute(NetworkUtils.trailers);

                    FetchMovieComponentsTask fetchMoreInfo = new FetchMovieComponentsTask(movieItem,"");
                    fetchMoreInfo.execute("");

                    moviesList.add(movieItem);
                    MainActivityFragment.posterAdapter.notifyDataSetChanged();
                }

            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static String getJSON (Uri builtUri){
        InputStream inputStream;
        StringBuffer buffer;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;

        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJson = buffer.toString();

        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }
        return moviesJson;
    }


    private void getFavouriteMovies(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = null;
        moviesList.clear();
        images.clear();
        imagesBackground.clear();

        try{
            cursor = resolver.query(uri,null,null,null,null);

            if(cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                    images.add(NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_185 + cursor.getString(3));
                    imagesBackground.add(NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_500 + cursor.getString(4));
                    movie.setReviews(cursor.getString(10));
                    movie.setMovieTrailers(cursor.getString(11));
                    moviesList.add(movie);

                }
                while (cursor.moveToNext());

            }
        }

        finally {
            if(cursor != null)
                cursor.close();

        }
        MainActivityFragment.posterAdapter.notifyDataSetChanged();

    }


}
