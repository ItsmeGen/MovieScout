package com.example.movie_scout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MovieComedyAdapter extends RecyclerView.Adapter<MovieComedyAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieComedyAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_drama2 , parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.descriptionTextView.setText(movie.getDescription());
        holder.genreTextView.setText(movie.getGenre());
        holder.yearTextView.setText(String.valueOf(movie.getYear_released()));
        holder.directorTextView.setText(movie.getDirector());

        // Log the image URL for debugging
        Log.d("MovieAdapter", "Loading image from: " + movie.getImage_url());

        Glide.with(holder.itemView.getContext())
                .load(movie.getImage_url())
                .into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, genreTextView, yearTextView, directorTextView;
        ImageView movieImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            directorTextView = itemView.findViewById(R.id.directorTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            movieImageView = itemView.findViewById(R.id.movieImageView);
        }
    }
}
