package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.android.popularmovies.adapters.MovieTrailerListAdapter.mTrailersData;

public class DetailMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().add(R.id.fragmentdetail, new DetailPlaceHolderFragment());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsMovie.class));
            return true;
        }


        if(id == R.id.action_share){
            Intent sendIntent = new Intent();
            String movieSource = "";
            if(!mTrailersData.isEmpty()){
                movieSource = mTrailersData.get(0).getSource();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I recommend to see this movie trailer: http://www.youtube.com/watch?v=" + movieSource);
            }
            else{
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This movie doesn't have any trailer" + movieSource);
            }


            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", MainActivityFragment.moviesList);
        outState.putStringArrayList("images", MainActivityFragment.images);
        super.onSaveInstanceState(outState);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
