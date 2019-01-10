package com.github.jpage4500.jnitest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.jpage4500.jnitest.adapters.ActorAdapter;
import com.github.jpage4500.jnitest.models.Movie;
import com.github.jpage4500.jnitest.models.MovieDetail;
import com.github.jpage4500.jnitest.utils.GsonHelper;

import java.util.Arrays;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    private Movie movie;

    private ActorAdapter adapter;

    private TextView movieNameText;
    private TextView movieDescriptionText;
    private TextView movieScoreText;
    private RecyclerView actorRecyclerView;

    public static Intent getIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, GsonHelper.getInstance().toJson(movie));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setTitle(getString(R.string.movie_details));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        movieNameText = findViewById(R.id.movieNameText);
        movieDescriptionText = findViewById(R.id.movieDescriptionText);
        movieScoreText = findViewById(R.id.movieScoreText);
        actorRecyclerView = findViewById(R.id.actorRecyclerView);

        adapter = new ActorAdapter(this);
        actorRecyclerView.setAdapter(adapter);

        String movieStr = getIntent().getStringExtra(EXTRA_MOVIE);
        if (movieStr != null) {
            movie = GsonHelper.getInstance().fromJson(movieStr, Movie.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movie == null) {
            finish();
        }
        loadMovieDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieDetails() {
        movieNameText.setText(movie.getName());

        MovieDetail detail = movie.getDetail();
        movieScoreText.setText(Float.toString(detail.getScore()));
        movieDescriptionText.setText(detail.getDescription());
        adapter.setActorList(Arrays.asList(detail.getActors()));
    }

}
