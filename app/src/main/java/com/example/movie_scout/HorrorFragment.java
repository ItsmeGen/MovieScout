package com.example.movie_scout;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HorrorFragment extends Fragment {
    private RecyclerView recyclerHorrorMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> filteredMovies = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horror, container, false);

        // Setup the Toolbar and set it as the ActionBar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);  // Set Toolbar as the ActionBar
            activity.getSupportActionBar().setTitle("Horror Movies");  // Set Toolbar title
        }
        setUpRecyclerView(view); // Set up the RecyclerView
        setHasOptionsMenu(true); // Enable the options menu
        fetchMovies(); // Fetch movies from API
        return view;
    }

    private void setUpRecyclerView(View view) {
        recyclerHorrorMovies = view.findViewById(R.id.recyclerHorrorMovies);
        recyclerHorrorMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerHorrorMovies.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(filteredMovies); // Initialize adapter once
        recyclerHorrorMovies.setAdapter(movieAdapter); // Set the adapter for the RecyclerView
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu); // Inflate the menu

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        if (searchEditText != null) {
            // Set the query hint color to white
            searchEditText.setHintTextColor(Color.BLACK);

            // Set the typed text color to white
            searchEditText.setTextColor(Color.BLACK);

            // Optionally, set the text size if needed
            searchEditText.setTextSize(16); // Adjust this size as needed
        }
        searchView.setBackgroundColor(Color.WHITE);

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Return false to not consume the event
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                   resetMovieList();
                } else {
                    // Filter the list based on the search query
                    filterMovies(newText);
                }
                return true;
            }
        });
    }

    private void filterMovies(String query) {
        List<Movie> filteredList = new ArrayList<>(); // Create a new filtered list
        if (query.isEmpty()) {
            filteredList.addAll(movieList); // Show all movies if the query is empty
        } else {
            for (Movie movie : movieList) {
                if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(movie); // Add movie to filtered list if it matches the query
                }
            }
        }
        movieAdapter.updateMovieList(filteredList); // Update the adapter with the filtered list
    }

    private void resetMovieList() {
        // Reset the filteredMovies list with the full movieList
        filteredMovies.clear();
        filteredMovies.addAll(movieList);
        movieAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    private void fetchMovies() {
        MovieHorrorApiService apiService = MovieHorrorApiClient.getRetrofitInstance().create(MovieHorrorApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        // Make the API call
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear(); // Clear the existing list
                    movieList.addAll(response.body()); // Store the movie list
                    resetMovieList();
                } else {
                    // Handle the case where the response is not successful
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle API failure (e.g., show a Toast or log the error)
            }
        });

    }
}
