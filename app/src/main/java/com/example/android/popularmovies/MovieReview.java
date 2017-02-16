package com.example.android.popularmovies;

public class MovieReview {

    private String mAuthor;
    private String mContent;

    public MovieReview(){

    }

    public MovieReview(String author, String content){
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public void setAuthor(String author){
        mAuthor = author;
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }
}
