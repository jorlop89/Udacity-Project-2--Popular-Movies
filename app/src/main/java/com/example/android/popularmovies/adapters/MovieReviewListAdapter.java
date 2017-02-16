package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.MovieReview;
import com.example.android.popularmovies.R;

import java.util.ArrayList;


public class MovieReviewListAdapter extends RecyclerView.Adapter<MovieReviewViewHolder> {


    public static ArrayList<MovieReview> mReviewsData = new ArrayList<MovieReview>();
    private LayoutInflater inflater;

    public MovieReviewListAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_review_item,parent,false);
        MovieReviewViewHolder holder = new MovieReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {


        MovieReview movieReview = mReviewsData.get(position);
        holder.reviewAuthor.setText(movieReview.getAuthor());
        holder.reviewContent.setText(movieReview.getContent());

    }

    @Override
    public int getItemCount() {

        return mReviewsData.size();
    }
}
