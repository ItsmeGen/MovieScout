package com.example.movie_scout;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActionFragment extends Fragment {

    private RecyclerView recyclerActionMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action, container, false);

        // Setup the Toolbar and set it as the ActionBar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);  // Set Toolbar as the ActionBar
            activity.getSupportActionBar().setTitle("Action Movies");  // Set Toolbar title
        }
        setUpRecyclerView(view); // Set up the RecyclerView
        setHasOptionsMenu(true); // Enable the options menu
        fetchMovies(); // Fetch movies from API
        return view;
    }

    private void setUpRecyclerView(View view) {
        recyclerActionMovies = view.findViewById(R.id.recyclerActionMovies);
        recyclerActionMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerActionMovies.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(movieList); // Initialize adapter once
        recyclerActionMovies.setAdapter(movieAdapter); // Set the adapter for the RecyclerView
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu); // Inflate the menu

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Here");

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Return false to not consume the event
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // When the search is cleared, update the adapter with the full list
                    movieAdapter.updateMovieList(new ArrayList<>(movieList)); // Use a new list to avoid issues
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

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieActionApiService apiService = MovieActionApiClient.getRetrofitInstance().create(MovieActionApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        // Make the API call
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear(); // Clear the existing list
                    movieList.addAll(response.body()); // Store the movie list
                    movieAdapter.notifyDataSetChanged(); // Notify the adapter about data changes
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