package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.MainActivityFragment;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;


public class MoviePosterAdapter extends BaseAdapter{


    private Context mContext;

    public MoviePosterAdapter(Context c) {

        mContext = c;
    }

    @Override
    public int getCount() {
        return MainActivityFragment.images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        ImageView posterImage;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            posterImage = (ImageView) inflater.inflate(R.layout.movie_item, parent,false);
        }
        else{
            posterImage = (ImageView) convertView;
        }

        Picasso.with(mContext).load(MainActivityFragment.images.get(position)).into(posterImage);

        return posterImage;
    }
}
