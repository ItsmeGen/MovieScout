package com.example.movie_scout;

import static com.example.movie_scout.R.id.recyclerDramaMovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class movie_drama extends AppCompatActivity {

    private RecyclerView recyclerDramaMovies;
    private MovieDramaAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_drama);

        TextView action_btn = findViewById(R.id.btn_action);
        ImageView home_btn =findViewById(R.id.btn_home);
        ImageView fav_btn =findViewById(R.id.btn_fav);
        ImageView menu_btn =findViewById(R.id.btn_menu);
        ImageView genre_btn =findViewById(R.id.btn_genre);





        action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(movie_drama.this, genre.class));
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(movie_drama.this, homepage.class));
            }
        });

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(movie_drama.this, favorite_page.class));
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(movie_drama.this, menu.class));
            }
        });

        genre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(movie_drama.this, genre.class));
            }
        });




        // Initialize RecyclerView
        recyclerDramaMovies = findViewById(R.id.recyclerDramaMovies);
        recyclerDramaMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerDramaMovies.setHasFixedSize(true);

        // Fetch and display the movies
        fetchMovies();
    }

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieDramaApiService  apiService = MovieDramaApiClient.getRetrofitInstance().create(MovieDramaApiService.class);
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
                Toast.makeText(movie_drama.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MoviesAPI", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieDramaAdapter(movies);
        recyclerDramaMovies.setAdapter(movieAdapter);
    }
}
