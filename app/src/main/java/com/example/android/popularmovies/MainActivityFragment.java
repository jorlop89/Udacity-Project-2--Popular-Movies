package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.adapters.MoviePosterAdapter;
import com.example.android.popularmovies.asynctasks.FetchMovieTask;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static ArrayList<Movie> moviesList;
    public static ArrayList<String> images;
    public static ArrayList<String> imagesBackground;
    public static MoviePosterAdapter posterAdapter;
    public static String lastSortOrder;
    public static GridView gridView;

    /*New*/
    public static int pos;
    public boolean mTwoPane;
    /*New*/

    public MainActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        DisplayMetrics dMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        int width = Math.round(dMetrics.widthPixels / density);
        int height = Math.round(dMetrics.heightPixels / density);

        /* Single mode*/
        if(height > width || width < 720){
            mTwoPane = false;
        }



        /*Two pane mode*/
        else{
            mTwoPane = true;
        }

        if(savedInstanceState != null && savedInstanceState.containsKey("movies")) {
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            images = savedInstanceState.getStringArrayList("images");
            pos = savedInstanceState.getInt("position");
        }

        else {
            moviesList = new ArrayList<Movie>();
            images = new ArrayList<String>();
            imagesBackground = new ArrayList<String>();
            posterAdapter = new MoviePosterAdapter(getActivity());
            updateMovies();
        }

        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.content_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(posterAdapter);
        gridView.setOnItemClickListener(this);
        return rootView;
    }


    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingCriteria = sharedPreferences.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default_value));

        if(lastSortOrder != null && !sortingCriteria.equals(lastSortOrder)){
            moviesList = new ArrayList<Movie>();
            images = new ArrayList<String>();
            imagesBackground = new ArrayList<String>();
            updateMovies();
        }
        lastSortOrder = sortingCriteria;
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        if(mTwoPane){
            OnMovieSelectionListener listener = (OnMovieSelectionListener) getActivity();
            listener.onMovieSelected(position);
        }

        else{
            Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
            intent.putExtra("movie_id", id);
            intent.putExtra("movie_position", position);
            pos = intent.getIntExtra("movie_position", -1);
            startActivity(intent);
        }

    }

    /* Method to update the movies that we choose according the sort criteria in the setting screen */

    public void updateMovies() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingCriteria = sharedPreferences.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default_value));
        new FetchMovieTask(getContext()).execute(sortingCriteria, null);



    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
