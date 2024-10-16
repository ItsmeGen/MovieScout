package com.example.movie_scout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DramaFragment extends Fragment {
    private RecyclerView recyclerDramaMovies;
    private MovieAdapter movieAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drama, container, false);

        // Find TextView for Drama
        TextView textAction = view.findViewById(R.id.btn_action);
        TextView textHorror = view.findViewById(R.id.btn_horror);
        TextView textComedy = view.findViewById(R.id.btn_comedy);

        // Set click listener to replace with DramaFragment
        textAction.setOnClickListener(v -> replaceFragment(new ActionFragment()));
        textHorror.setOnClickListener(v -> replaceFragment(new HorrorFragment()));
        textComedy.setOnClickListener(v -> replaceFragment(new ComedyFragment()));

        recyclerDramaMovies = view.findViewById(R.id.recyclerDramaMovies); // use view.findViewById for fragments
        recyclerDramaMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerDramaMovies.setHasFixedSize(true);

        // Fetch and display the movies
        fetchMovies();
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);  // Replace with the ID of your FrameLayout container
        transaction.addToBackStack(null);  // Add to back stack so you can navigate back
        transaction.commit();
    }
    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieDramaApiService apiService = MovieDramaApiClient.getRetrofitInstance().create(MovieDramaApiService.class);
        Call<List<Movie>> call = apiService.getAllMovies();

        // Make the API call
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    if (movies != null) {
                        displayMovies(movies);  // Display the movies
                    } else {
                        Toast.makeText(getActivity(), "No movies available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch movies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(getActivity(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieAdapter(movies);
        recyclerDramaMovies.setAdapter(movieAdapter);
    }

}
