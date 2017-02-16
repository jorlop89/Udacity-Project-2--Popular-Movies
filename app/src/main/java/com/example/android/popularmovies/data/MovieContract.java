package com.example.android.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


//Change
public class MovieContract {

    /*Add content provider constants to the Contract*/

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract

    // This is the path for the "movies" directory
    public static final String PATH_MOVIES = "movies";

    /* MovieEntry is an inner class that defines the contents of the task table */

    public static final class MovieEntry implements BaseColumns{

        //MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Movie table and columns names
        public static final String TABLE_NAME = "movies";

        // Since MovieEntry implements the interface "BaseColumns", it has an automatically "_ID" column in addition to the entries below.

        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_IMAGE = "image";
        public static final String MOVIE_IMAGE_BACKGROUND = "image_background";
        public static final String MOVIE_PLOT = "plot";
        public static final String MOVIE_RATING_AVG = "rating_avg";
        public static final String MOVIE_DATE = "date";
        public static final String MOVIE_RUNTIME = "runtime";
        public static final String MOVIE_GENRES = "genres";
        public static final String MOVIE_REVIEWS = "reviews";
        public static final String MOVIE_TRAILERS = "trailers";

        public static final String[] PROJECTION = {MOVIE_ID, MOVIE_TITLE, MOVIE_IMAGE, MOVIE_IMAGE_BACKGROUND, MOVIE_PLOT, MOVIE_RATING_AVG, MOVIE_DATE, MOVIE_RUNTIME, MOVIE_GENRES, MOVIE_REVIEWS, MOVIE_TRAILERS};

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }



}
