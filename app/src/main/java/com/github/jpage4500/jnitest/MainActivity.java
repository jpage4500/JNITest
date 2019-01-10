package com.github.jpage4500.jnitest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.jpage4500.jnitest.adapters.MovieAdapter;
import com.github.jpage4500.jnitest.models.Movie;
import com.github.jpage4500.jnitest.models.MovieDetail;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    // ------------------------
    // JNI
    // ------------------------
    static {
        System.loadLibrary("MovieController");
    }

    public native long createMovieController();

    public native Movie[] getMovies(long nativePointer);

    public native MovieDetail getMovieDetail(long nativePointer, String name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.movies);

        recyclerView = findViewById(R.id.movieRecyclerView);
        adapter = new MovieAdapter(this);
        adapter.setClickHandler(new MovieAdapter.MovieClickHandler() {
            @Override
            public void handleMovieClicked(Movie movie) {
                // open movie detail page
                Log.d(TAG, "handleMovieClicked: " + movie.getName());
                Intent intent = MovieDetailActivity.getIntent(MainActivity.this, movie);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        loadMovies();
    }

    private void loadMovies() {
        // load movies using JNI in background thread to avoid freezing the UI (only necessary if this call isn't super fast..)
        new AsyncTask<Void, Void, List<Movie>>() {

            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List movieList = new ArrayList<>();

                // load movies using JNI
                // TODO: depending on how many movies we're dealing with we may want to delay loading movie details until the movie is selected
                long movieControllerPtr = createMovieController();
                Movie[] movies = getMovies(movieControllerPtr);
                for (Movie movie : movies) {
                    MovieDetail movieDetail = getMovieDetail(movieControllerPtr, movie.getName());
                    movie.setDetail(movieDetail);
                    movieList.add(movie);
                }

                Log.d(TAG, "loadMovies: loaded " + movieList.size() + " movies");

                return movieList;
            }

            @Override
            protected void onPostExecute(List<Movie> movieList) {
                if (isFinishing()) return; // activity has been destroyed

                adapter.setMovieList(movieList);
            }
        }.execute();
    }


}
