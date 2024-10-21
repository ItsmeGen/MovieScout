package com.example.movie_scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>(); // Full movie list
    private List<Movie> filteredList = new ArrayList<>(); // Filtered list for search

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMovies.setHasFixedSize(true);

        // Set up SearchView
        SearchView searchView = view.findViewById(R.id.searchView);

        // Customize the SearchView
        customizeSearchView(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optional: Handle the submit action
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the movie list based on the query
                filterMovies(newText);
                return false;
            }
        });

        // Fetch and display the movies
        fetchMovies();

        return view;
    }

    // Function to filter the movie list
    private void filterMovies(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(movieList); // If search query is empty, show all movies
        } else {
            for (Movie movie : movieList) {
                if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(movie); // Add movie to filtered list if it matches the query
                }
            }
        }

        // Update the RecyclerView with the filtered list
        movieAdapter.updateMovieList(filteredList);
    }

    private void fetchMovies() {
        // Fetch movies from the API (assuming you already have this function working)
        // Example API call
        MovieApiService apiService = MovieApiClient.getRetrofitInstance().create(MovieApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    if (movies != null) {
                        movieList = movies;
                        filteredList.addAll(movieList); // Initialize filtered list
                        displayMovies(movieList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle error
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieAdapter(movies);
        recyclerViewMovies.setAdapter(movieAdapter);
    }

    // Function to customize SearchView appearance
    private void customizeSearchView(SearchView searchView) {
        // Change the search icon color to red
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(getResources().getColor(R.color.my_primary)); // Set the search icon color to red

        // Change the close button (clear button) color
        ImageView closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setColorFilter(getResources().getColor(R.color.my_primary)); // Set the close button color to red

        // Change the search text color and hint text color when the user is typing
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black)); // Set the search text color to black
        searchEditText.setHintTextColor(getResources().getColor(R.color.black)); // Set the hint text color

        // Set background color to black initially
        searchView.setBackgroundColor(getResources().getColor(R.color.white));

        // Add a listener to change background color to white when user starts typing
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    searchView.setBackgroundColor(getResources().getColor(R.color.white)); // White background when typing
                    searchEditText.setTextColor(getResources().getColor(R.color.black));  // Black text when typing
                } else {
                    searchView.setBackgroundColor(getResources().getColor(R.color.white)); // Return to black background
                    searchEditText.setTextColor(getResources().getColor(R.color.black));  // White text initially
                }
            }
        });
    }
}
