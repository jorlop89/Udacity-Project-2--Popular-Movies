package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.MovieTrailer;
import com.example.android.popularmovies.R;

import java.util.ArrayList;

public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerViewHolder> {

    public static ArrayList<MovieTrailer> mTrailersData = new ArrayList<MovieTrailer>() ;
    private LayoutInflater inflater;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public MovieTrailerListAdapter(Context context, ListItemClickListener listener){
        mOnClickListener = listener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = inflater.inflate(R.layout.movie_trailer_item,parent,false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        MovieTrailer movieTrailer = mTrailersData.get(position);
        holder.titleTrailer.setText(movieTrailer.getTitle());
    }

    @Override
    public int getItemCount() {
        return mTrailersData.size();
    }
}
