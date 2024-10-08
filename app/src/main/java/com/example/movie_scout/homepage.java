package com.example.movie_scout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homepage extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize RecyclerView
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMovies.setHasFixedSize(true);

        // Fetch and display the movies
        fetchMovies();
    }

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieApiService apiService = MovieApiClient.getRetrofitInstance().create(MovieApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        // Make the API call
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    if (movies != null) {
                        displayMovies(movies);  // Display the movies
                        Log.d("MoviesAPI", "Fetched movies: " + movies.toString());
                    } else {
                        Log.d("MoviesAPI", "Response successful but no data.");
                    }
                } else {
                    Log.e("MoviesAPI", "Response error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(homepage.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MoviesAPI", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieAdapter(movies);
        recyclerViewMovies.setAdapter(movieAdapter);
    }
}
