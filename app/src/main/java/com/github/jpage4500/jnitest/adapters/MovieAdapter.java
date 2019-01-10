package com.github.jpage4500.jnitest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jpage4500.jnitest.R;
import com.github.jpage4500.jnitest.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private MovieClickHandler clickHandler;

    public interface MovieClickHandler {
        void handleMovieClicked(Movie movie);
    }

    public MovieAdapter(Context context) {
        this.context = context;
        movieList = new ArrayList<>();
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void setClickHandler(MovieClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final Movie movie = movieList.get(position);

        holder.numberView.setText(Integer.toString(position + 1));
        holder.movieNameView.setText(movie.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickHandler != null) {
                    clickHandler.handleMovieClicked(movie);
                }
            }
        });
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final TextView numberView;
        final TextView movieNameView;

        MovieViewHolder(View view) {
            super(view);
            numberView = view.findViewById(R.id.movieNumber);
            movieNameView = view.findViewById(R.id.movieName);
        }
    }
}