package com.example.movie_scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
    private List<Movie> movieList = new ArrayList<>(); // This is the full original movie list
    private List<Movie> filteredMovies = new ArrayList<>(); // This is the filtered list

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup the Toolbar and set it as the ActionBar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);  // Set Toolbar as the ActionBar
            activity.getSupportActionBar().setTitle("Trends and Popular");  // Set Toolbar title
        }

        setUpRecyclerView(view); // Set up the RecyclerView
        setHasOptionsMenu(true); // Enable the options menu
        fetchMovies(); // Fetch movies from API
        return view;
    }

    private void setUpRecyclerView(View view) {
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMovies.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(filteredMovies); // Initialize adapter with filteredMovies list
        recyclerViewMovies.setAdapter(movieAdapter); // Set the adapter for the RecyclerView
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
                    // When the search is cleared, reset the adapter with the full list
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
        filteredMovies.clear(); // Clear the filtered list
        for (Movie movie : movieList) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.add(movie); // Add movie to filtered list if it matches the query
            }
        }
        movieAdapter.notifyDataSetChanged(); // Update the adapter with the filtered list
    }

    private void resetMovieList() {
        // Reset the filteredMovies list with the full movieList
        filteredMovies.clear();
        filteredMovies.addAll(movieList);
        movieAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    private void fetchMovies() {
        MovieApiService apiService = MovieApiClient.getRetrofitInstance().create(MovieApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear(); // Clear the existing movieList
                    movieList.addAll(response.body()); // Store the movie list
                    resetMovieList(); // Reset the adapter with the full list
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
