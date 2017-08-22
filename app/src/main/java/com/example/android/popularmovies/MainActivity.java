package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.asynctasks.FetchMovieTask;

public class MainActivity extends AppCompatActivity implements OnMovieSelectionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.fragment) != null){
            if(savedInstanceState != null){
                return;
            }

            MainActivityFragment mainFragment = new MainActivityFragment();
            mainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, mainFragment);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsMovie.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_refresh){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String sortingCriteria = sharedPreferences.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default_value));
            new FetchMovieTask(getApplicationContext()).execute(sortingCriteria, null);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", MainActivityFragment.moviesList);
        outState.putStringArrayList("images", MainActivityFragment.images);
        outState.putStringArrayList("imagesBackground", MainActivityFragment.imagesBackground);
        outState.putLong("position", MainActivityFragment.pos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieSelected(int moviePosition) {
        DetailPlaceHolderFragment detailFragment = (DetailPlaceHolderFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentdetail);
        if(detailFragment != null){
            detailFragment.loadMovie(moviePosition);
        }
        else{
            DetailPlaceHolderFragment newDetailFragment = new DetailPlaceHolderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("movie_position", moviePosition);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentdetail,newDetailFragment);
            fragmentTransaction.addToBackStack(null);
        }
    }
}
