package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MovieReviewListAdapter;
import com.example.android.popularmovies.adapters.MovieTrailerListAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.popularmovies.MainActivityFragment.moviesList;
import static com.example.android.popularmovies.adapters.MovieReviewListAdapter.mReviewsData;
import static com.example.android.popularmovies.adapters.MovieTrailerListAdapter.mTrailersData;

public class DetailPlaceHolderFragment extends Fragment implements MovieTrailerListAdapter.ListItemClickListener{

    public static Movie movie;
    public static Intent intent;
    public static TextView movieTitle;
    public static TextView movieYear;
    public static TextView movieRate;
    public static TextView moviePlot;
    public static ImageView moviePoster;
    private RecyclerView movieReviews;
    private RecyclerView movieTrailers;

    public TextView textViewMovieReviews;
    public TextView textViewMovieTrailers;


    private ImageButton buttonFavourite;
    //New
    public static ImageView movieBackground;
    public static TextView movieGenres;
    public static TextView movieRuntime;

    //New

    View rootView;
    int id;

    public MovieReviewListAdapter movieReviewListAdapter;
    public MovieTrailerListAdapter movieTrailerListAdapter;

    public DetailPlaceHolderFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intent = getActivity().getIntent();
        id = intent.getIntExtra("movie_position", - 1);
        loadMovie(id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_detail,container,false);
        setHasOptionsMenu(true);
        return rootView;

    }



    /* Method to initialize the views of layout content_detail */

    public void initMovieView(View view, int id){
        movie = new Movie();
        try{
            movie = moviesList.get(id);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            movieYear = (TextView) view.findViewById(R.id.movie_year);
            movieRate = (TextView) view.findViewById(R.id.movie_rate);
            moviePoster = (ImageView) view.findViewById(R.id.movie_detail_image);
            moviePlot = (TextView) view.findViewById(R.id.movie_plot);

            textViewMovieReviews = (TextView) view.findViewById(R.id.text_view_reviews);
            textViewMovieTrailers = (TextView) view.findViewById(R.id.text_view_trailers);

            movieBackground = (ImageView) view.findViewById(R.id.movie_background_image);
            movieRuntime = (TextView) view.findViewById(R.id.movie_runtime);
            movieGenres = (TextView) view.findViewById(R.id.movie_genres);


        }
        catch (IndexOutOfBoundsException e){
             e.printStackTrace();
        }

    }

    /* Method to load the values of the views of the layout content_detail */

    public void loadMovieValues(View view){

        try {
            movieTitle.setText(movie.getTitle());
            movieYear.setText(movie.getDate().substring(0, 4));
            movieRate.setText(movie.getRatingAvg() + "/10");
            moviePlot.setText(movie.getPlot());

            if(movie.getRuntime() != null){

                int minutes = Integer.valueOf(movie.getRuntime());
                int hours = 0;
                while(minutes > 59){
                    hours = hours + 1;
                    minutes = minutes - 60;
                }
                String s = String.valueOf(hours) + " h " + String.valueOf(minutes) + " min";
                movieRuntime.setText(s);
            }

            else{
                movieRuntime.setText(getResources().getString(R.string.no_runtime_available));
            }

            if(movie.getGenres() != null){
                movieGenres.setText(movie.getGenres());
            }

            else{
                movieGenres.setText(getResources().getString(R.string.no_genres_available));
            }




            String movie_image_background = NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_500 + movie.getImageBackground();
            Picasso.with(view.getContext()).load(movie_image_background).into(movieBackground);

            String movie_image_poster = NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_IMAGE_SIZE_185 + "/" + movie.getImage();
            Picasso.with(view.getContext()).load(movie_image_poster).into(moviePoster);
        }

        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /*Method to load the Reviews in the arrayList of the MovieReviewListAdapter clas*/
    private void loadReviews(){

        mReviewsData.clear();

        if(movie.getReviews() == null) {
            return;
        }

        try {
            JSONObject moviesReviewsObject = new JSONObject(movie.getReviews());
            JSONArray moviesReviewsArray = moviesReviewsObject.getJSONArray("results");

            //int moviesReviewsResults = moviesReviewsObject.getInt("total_results");
            int length = moviesReviewsArray.length();

            if(length == 0){
                movieReviews.setVisibility(View.GONE);
                textViewMovieReviews.setVisibility(View.VISIBLE);
                textViewMovieReviews.setText(getResources().getString(R.string.no_reviews_available));
                return;
            }

            for (int i = 0; i < length; i++) {

                JSONObject review = moviesReviewsArray.getJSONObject(i);

                MovieReview movieReview = new MovieReview();
                movieReview.setAuthor(review.getString("author"));
                movieReview.setContent(review.getString("content"));

                if (mReviewsData != null) {
                    mReviewsData.add(movieReview);
                }
            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void loadTrailers(){
        mTrailersData.clear();

        if(movie.getMovieTrailers() == null){

            return;
        }

        try {
            JSONObject moviesTrailersObject = new JSONObject(movie.getMovieTrailers());
            JSONArray moviesTrailersYTArray = moviesTrailersObject.getJSONArray("youtube");
            int length = moviesTrailersYTArray.length();

            if(length == 0){
                movieTrailers.setVisibility(View.GONE);
                textViewMovieTrailers.setVisibility(View.VISIBLE);
                textViewMovieTrailers.setText(getResources().getString(R.string.no_trailers_available));
                return;
            }

            for (int i = 0; i < length; i++) {

                JSONObject trailer = moviesTrailersYTArray.getJSONObject(i);

                MovieTrailer movieTrailer = new MovieTrailer();
                movieTrailer.setTitle(trailer.getString("name"));
                movieTrailer.setSource(trailer.getString("source"));

                if (mTrailersData != null) {
                    mTrailersData.add(movieTrailer);
                }
            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void toogleFavouriteMovie(){
        boolean inFav = checkFavouriteMovie();
        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.button_favourite);
        if(inFav){
            imageButton.setImageResource(R.drawable.favorite_added);
        }
        else{
            imageButton.setImageResource(R.drawable.favorite_removed);
        }

    }

    private boolean checkFavouriteMovie(){
        Uri uri = MovieContract.MovieEntry.buildMovieUri(movie.getId());
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = null;

        try{
            cursor = resolver.query(uri,null,null,null,null);
            if(cursor.moveToFirst())
                return true;
        }
        finally {
            if(cursor != null)
                cursor.close();

        }
        return false;

    }

    public void addFavouriteMovie(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.MOVIE_IMAGE, movie.getImage());
        values.put(MovieContract.MovieEntry.MOVIE_IMAGE_BACKGROUND, movie.getImageBackground());
        values.put(MovieContract.MovieEntry.MOVIE_PLOT, movie.getPlot());
        values.put(MovieContract.MovieEntry.MOVIE_RATING_AVG, movie.getRatingAvg());
        values.put(MovieContract.MovieEntry.MOVIE_DATE, movie.getDate());
        values.put(MovieContract.MovieEntry.MOVIE_RUNTIME, movie.getRuntime());
        values.put(MovieContract.MovieEntry.MOVIE_GENRES, movie.getGenres());
        values.put(MovieContract.MovieEntry.MOVIE_REVIEWS, movie.getReviews());
        values.put(MovieContract.MovieEntry.MOVIE_TRAILERS, movie.getMovieTrailers());
        resolver.insert(uri,values);
    }

    public void deleteFavouriteMovie(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getActivity().getContentResolver();
        resolver.delete(uri,MovieContract.MovieEntry.MOVIE_ID + "= ? ", new String[]{Long.toString(movie.getId())});
    }


    public void loadMovie(final int id){
        this.id = id;

        View v = getView();
        initMovieView(v, id);
        buttonFavourite = (ImageButton) v.findViewById(R.id.button_favourite);
        buttonFavourite.setOnClickListener(null);
        toogleFavouriteMovie();
        loadMovieValues(v);
        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean clickFavouriteButton = checkFavouriteMovie();
                if(clickFavouriteButton){
                    deleteFavouriteMovie();
                    Toast.makeText(getContext(),"Favourite movie deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    addFavouriteMovie();
                    Toast.makeText(getContext(),"Favourite movie added", Toast.LENGTH_SHORT).show();

                }

                toogleFavouriteMovie();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //Create the RecyclerView of reviews
        movieReviews = (RecyclerView) v.findViewById(R.id.recycler_view_reviews);
        movieReviews.setLayoutManager(layoutManager);
        movieReviewListAdapter = new MovieReviewListAdapter(getContext());
        movieReviews.setAdapter(movieReviewListAdapter);

        //Create the RecyclerView of trailers

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());

        movieTrailers = (RecyclerView) v.findViewById(R.id.recycler_view_movies);
        movieTrailers.setLayoutManager(layoutManager1);
        movieTrailerListAdapter = new MovieTrailerListAdapter(getContext(), this);
        movieTrailers.setAdapter(movieTrailerListAdapter);

        loadReviews();
        loadTrailers();

    }



    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
