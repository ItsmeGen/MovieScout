package com.example.movie_scout;

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

public class genre extends AppCompatActivity {

    private RecyclerView recyclerActionMovies;
    private MovieActionAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        TextView btn_drama =findViewById(R.id.btn_drama);
        ImageView home_btn =findViewById(R.id.btn_home);
        ImageView fav_btn =findViewById(R.id.btn_genre);
        ImageView menu_btn =findViewById(R.id.btn_menu);


        btn_drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(genre.this,movie_drama.class));
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(genre.this, homepage.class));
            }
        });
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(genre.this, favorite_page.class));
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(genre.this, menu.class));
            }
        });




        // Initialize RecyclerView
        recyclerActionMovies = findViewById(R.id.recyclerActionMovies);
        recyclerActionMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerActionMovies.setHasFixedSize(true);

        // Fetch and display the movies
        fetchMovies();
    }

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieActionApiService  apiService = MovieActionApiClient.getRetrofitInstance().create(MovieActionApiService.class);
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
                Toast.makeText(genre.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MoviesAPI", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieActionAdapter(movies);
        recyclerActionMovies.setAdapter(movieAdapter);
    }
}
