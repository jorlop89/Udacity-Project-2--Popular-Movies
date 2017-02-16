package com.example.android.popularmovies;

public class MovieTrailer {
    private String mTitle;
    private String mSource;

    public MovieTrailer(){

    }

    public MovieTrailer(String title, String source){
        mTitle = title;
        mSource = source;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getSource(){
        return mSource;
    }

    public void setSource(String source){
        mSource = source;
    }



}
