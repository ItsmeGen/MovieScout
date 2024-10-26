package com.example.movie_scout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_adapter, parent, false);
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

        // Load movie image using Glide
        Glide.with(holder.itemView.getContext())
                .load(movie.getImage_url())
                .into(holder.movieImageView);

        // Convert the movie object to JSON for favorite checking
        Gson gson = new Gson();
        String movieJson = gson.toJson(movie);

        FavoriteManager favoriteManager = new FavoriteManager(holder.itemView.getContext());
        Set<String> favorites = favoriteManager.getFavorites();
        boolean isFavorite = favorites.contains(movieJson);

        // Set the favorite icon based on whether the movie is in favorites
        holder.favoriteIcon.setImageResource(isFavorite ? R.drawable.filled_heart : R.drawable.favorite_icon);

        // Set up the favorite icon click listener
        holder.favoriteIcon.setOnClickListener(v -> {
            if (isFavorite) {
                // Remove from favorites
                favorites.remove(movieJson);
                holder.favoriteIcon.setImageResource(R.drawable.favorite_icon); // Change icon to unfilled
                Toast.makeText(v.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                // Add to favorites
                favorites.add(movieJson);
                holder.favoriteIcon.setImageResource(R.drawable.filled_heart); // Change icon to filled
                Toast.makeText(v.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            // Update the favorites in FavoriteManager
            favoriteManager.updateFavorites(favorites); // Create this method in FavoriteManager to save changes
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateMovieList(List<Movie> newMovieList) {
        this.movieList.clear(); // Clear the old list
        this.movieList.addAll(newMovieList); // Add new items
        notifyDataSetChanged(); // Notify adapter about the dataset change
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, genreTextView, yearTextView, directorTextView;
        ImageView movieImageView, favoriteIcon; // Add favoriteIcon here

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            directorTextView = itemView.findViewById(R.id.directorTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            movieImageView = itemView.findViewById(R.id.movieImageView);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon); // Initialize favoriteIcon
        }
    }
}
