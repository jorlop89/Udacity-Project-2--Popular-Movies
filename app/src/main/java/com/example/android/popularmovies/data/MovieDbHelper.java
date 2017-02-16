package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Change

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_IMAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_IMAGE_BACKGROUND + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_PLOT + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_RATING_AVG + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_RUNTIME + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_GENRES + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_REVIEWS + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_TRAILERS + " TEXT NOT NULL, " +
                "UNIQUE ("+ MovieContract.MovieEntry.MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
