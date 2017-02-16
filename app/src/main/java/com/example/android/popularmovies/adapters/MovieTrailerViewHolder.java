package com.example.android.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import static com.example.android.popularmovies.adapters.MovieTrailerListAdapter.mTrailersData;


public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView titleTrailer;

    public MovieTrailerViewHolder(View itemView) {
        super(itemView);
        titleTrailer = (TextView) itemView.findViewById(R.id.trailer_title);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mTrailersData.get(clickedPosition).getSource()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" +  mTrailersData.get(clickedPosition).getSource()));
        try {
            view.getContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            view.getContext().startActivity(webIntent);
        }
    }
}
