package com.example.android.popularmovies.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmovies.Movie;
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

import static com.example.android.popularmovies.utilities.NetworkUtils.MOVIES_BASE_URL;


public class FetchMovieComponentsTask extends AsyncTask<String,Void,String>{

    public Movie mMovie;
    public String mMovieComponent;

    public FetchMovieComponentsTask(Movie movie, String movieComponent){
        mMovie = movie;
        mMovieComponent = movieComponent;

    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }



        Uri builtUri = null;
        mMovieComponent = params[0];
        if(mMovieComponent != null) {

            if(mMovieComponent == ""){
                builtUri = Uri.parse(MOVIES_BASE_URL + "/" + Long.toString(mMovie.getId())).buildUpon()
                        .appendQueryParameter("api_key", NetworkUtils.APP_ID)
                        .build();
            }

            else{
                builtUri = Uri.parse(MOVIES_BASE_URL + "/" + Long.toString(mMovie.getId()) + "/" + mMovieComponent).buildUpon()
                        .appendQueryParameter("api_key", NetworkUtils.APP_ID)
                        .build();
            }

        }

        String response = null;
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
    protected void onPostExecute(String result) {

        if(result != null) {

            if (mMovieComponent.equals("reviews")) {
                mMovie.setReviews(result);

            }

            else if (mMovieComponent.equals("trailers")){
                mMovie.setMovieTrailers(result);
            }

            else if(mMovieComponent.equals("")){
                loadMoreInfo(result);
            }

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

    public void loadMoreInfo(String jsonString){

        //moviesList.clear();

        try{
            if(jsonString != null){
                Movie movie = mMovie;
                if(movie != null){
                    JSONObject moviesObject = new JSONObject(jsonString);
                    movie.setRuntime(moviesObject.getString("runtime"));
                    JSONArray moviesArrayGenres = moviesObject.getJSONArray("genres");
                    String categories = "";
                    for(int i= 0; i < moviesArrayGenres.length(); i++) {
                        JSONObject movieCategories = moviesArrayGenres.getJSONObject(i);
                        categories += movieCategories.getString("name") + ". ";
                    }
                    movie.setGenres(categories);

                }
                //MainActivityFragment.posterAdapter.notifyDataSetChanged();

            }

        }

        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        catch (JSONException e){
            e.printStackTrace();
        }



    }

}
