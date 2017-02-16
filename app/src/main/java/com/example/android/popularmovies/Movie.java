package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

//Change

public class Movie implements Parcelable{

    long id;
    String title;
    String image;
    String imageBackground;
    String plot;
    String ratingAvg;
    String date;
    String runtime;
    String genres;
    String movieReviews;
    String movieTrailers;


    public Movie (long vId, String vTitle, String vImage, String vImageBackground, String vPlot,String vRatingAvg, String vDate, String vRuntime, String vGenres){
        id = vId;
        title = vTitle;
        image = vImage;
        imageBackground = vImageBackground;
        plot = vPlot;
        ratingAvg = vRatingAvg;
        date = vDate;
        runtime = vRuntime;
        genres = vGenres;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBackground(){
        return this.imageBackground;
    }

    public void setImageBackground(String imageBackground){
        this.imageBackground = imageBackground;
    }

    public String getPlot() {
        return this.plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRatingAvg() {
        return this.ratingAvg;
    }

    public void setRatingAvg(String ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenres() {
        return this.genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRuntime() {
        return this.runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReviews() {
        return movieReviews;
    }

    public void setReviews(String reviews) {
        movieReviews = reviews;
    }

    public String getMovieTrailers() {
        return movieTrailers;
    }

    public void setMovieTrailers(String trailers) {
        movieTrailers = trailers;
    }


    public Movie(){

    }

    private Movie(Parcel in) {
        id= in.readLong();
        title = in.readString();
        image = in.readString();
        imageBackground = in.readString();
        plot = in.readString();
        ratingAvg = in.readString();
        date = in.readString();
        runtime = in.readString();
        genres = in.readString();
        movieReviews = in.readString();
        movieTrailers = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(imageBackground);
        parcel.writeString(plot);
        parcel.writeString(ratingAvg);
        parcel.writeString(date);
        parcel.writeString(runtime);
        parcel.writeString(genres);
        parcel.writeString(movieReviews);
        parcel.writeString(movieTrailers);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel){
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {

            return new Movie[i];
        }
    };
}
