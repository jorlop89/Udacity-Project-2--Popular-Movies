package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.android.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;


public class MovieProvider extends ContentProvider{

    // Define final integer constants for the directory of movies and a single movie item.
    // It's a convention to use 100, 200, 300, etc for directories.
    // and related ints (101, 102, ..) for items in that directory.

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    // Declare a static variable for Uri matcher that we construct.

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a MovieDbHelper that's initialized in the onCreate() method.
    private MovieDbHelper movieDbHelper;

    @Override
    public boolean onCreate() {
        //Initialize a MovieDbHelper on startup.
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case MOVIES:
                retCursor = db.query(TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case MOVIE_WITH_ID:
                retCursor = db.query(TABLE_NAME, projection, MovieContract.MovieEntry.MOVIE_ID + " = " + uri.getLastPathSegment(), selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //getContext().getContentResolver().notifyChange(uri, null);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }


    @Override
    public String getType(Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match){
            //directory
            case MOVIES:
                return "vnd.android.cursor.dir" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
            //item
            case MOVIE_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }

    }



    // Implement insert to handle requests to insert a single new row of data.

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;


    }

    // Implement delete to delete a single row of data

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted;

        switch (match){
            case MOVIES:
                moviesDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if(moviesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int movieUpdated;

        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                movieUpdated = db.update(TABLE_NAME,contentValues,mSelection,mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        if (movieUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return movieUpdated;
    }
}
