package com.example.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.R;

public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

    TextView reviewAuthor;
    TextView reviewContent;

    public MovieReviewViewHolder(View itemView) {
        super(itemView);
        reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
        reviewContent = (TextView) itemView.findViewById(R.id.review_content);


    }
}
