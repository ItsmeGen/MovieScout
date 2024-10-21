package com.example.movie_scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> filteredList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMovies.setHasFixedSize(true);

        // Enable options menu to show SearchView in the toolbar
        setHasOptionsMenu(true);

        // Fetch and display movies
        fetchMovies();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu containing the SearchView
        inflater.inflate(R.menu.menu_main, menu);

        // Initialize SearchView from the menu
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Access the EditText inside the SearchView to customize text color
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // Change the search text color to black
        searchEditText.setTextColor(getResources().getColor(R.color.black));

        // Change the hint text color
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

        // Set the background color of the SearchView to white initially
        searchView.setBackgroundColor(getResources().getColor(R.color.white));

        // Add a listener to change the background color when the user starts typing (focus gained)
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // When the search is focused, set background to white and text color to black
                    searchView.setBackgroundColor(getResources().getColor(R.color.white));
                    searchEditText.setTextColor(getResources().getColor(R.color.black));  // Black text when typing
                } else {
                    // Optional: Change back to another color when the user is not typing
                    searchView.setBackgroundColor(getResources().getColor(R.color.white)); // Keep the background white
                    searchEditText.setTextColor(getResources().getColor(R.color.black));  // Keep the text color black
                }
            }
        });

        // Set up SearchView to filter movie list based on query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optional: Handle query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the movie list when query text changes
                filterMovies(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    // Function to filter the movie list based on the query
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

        // Update RecyclerView with the filtered list
        movieAdapter.updateMovieList(filteredList);
    }

    private void fetchMovies() {
        // Fetch movies from the API (assuming the function is set up correctly)
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
                // Handle failure
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieAdapter(movies);
        recyclerViewMovies.setAdapter(movieAdapter);
    }
}
