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

public class MovieHorror extends AppCompatActivity {

    private RecyclerView recyclerHorrorMovies;
    private MovieHorrorAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_horror);

        TextView btn_drama =findViewById(R.id.btn_drama);
        TextView btn_horror =findViewById(R.id.btn_horror);//palitan ng comedy mamaya
        TextView btn_action = findViewById(R.id.btn_action);
        //Divider
        ImageView home_btn =findViewById(R.id.btn_home);
        ImageView fav_btn =findViewById(R.id.btn_fav);
        ImageView genre_btn =findViewById(R.id.btn_genre);
        ImageView menu_btn =findViewById(R.id.btn_menu);

        // Constraint button(Top)
        btn_drama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, MovieDrama.class));
            }
        });

        btn_horror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, MovieDrama.class));
            }
        });

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, GenrePage.class));
            }
        });



        // Constraint button(bottom)
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, homepage.class));
            }
        });
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, FavoritePage.class));
            }
        });


        genre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, GenrePage.class));
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieHorror.this, MenuPage.class));
            }
        });




        // Initialize RecyclerView
        recyclerHorrorMovies = findViewById(R.id.recyclerHorrorMovies);
        recyclerHorrorMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerHorrorMovies.setHasFixedSize(true);

        // Fetch and display the movies
        fetchMovies();
    }

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieHorrorApiService  apiService = MovieHorrorApiClient.getRetrofitInstance().create(MovieHorrorApiService.class);
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
                Toast.makeText(MovieHorror.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MoviesAPI", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieHorrorAdapter(movies);
        recyclerHorrorMovies.setAdapter(movieAdapter);
    }
}
