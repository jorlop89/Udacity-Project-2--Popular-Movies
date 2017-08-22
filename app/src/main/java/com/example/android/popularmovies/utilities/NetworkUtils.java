package com.example.android.popularmovies.utilities;

public final class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public static final String MOVIE_IMAGE_SIZE_185 = "w185";

    public static final String MOVIE_IMAGE_SIZE_500= "w500";


    // TODO: Here we need to add the APP ID of themoviedb.org API. You need to register in the website to get the ID.
    public static final String APP_ID = "9877b90db1c28d428ad5e10edbac0215";

    /*The order of the movies, in this case most popular movies in this moment, we want our API to return*/
    public static final String popular = "popular";

    /*The order of the movies, in this case the top rated movies, we want our API to return*/
    public static final String toprated = "top_rated";

    /*Section of the API where we saw the reviews of the movie*/
    public static final String reviews = "reviews";

    /*Section of the API where we saw the trailers of the movie */
    public static final String trailers = "trailers";

    /*String that we add when we are going to put the APP_ID in the URL*/
    public static final String api_key = "api_key";

}
