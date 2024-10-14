package com.example.movie_scout;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class GenreFragment extends Fragment {

    private RecyclerView recyclerViewMovies;
    private MovieActionAdapter movieAdapter;

    // Declare TextView variables
    private TextView textAction, textDrama, textHorror, textComedy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genre, container, false);

        // Find TextViews
        textAction = view.findViewById(R.id.btn_action);
        textDrama = view.findViewById(R.id.btn_drama);
        textHorror = view.findViewById(R.id.btn_horror);
        textComedy = view.findViewById(R.id.btn_comedy);

        // Set click listeners to replace fragments and update text color
        textAction.setOnClickListener(v -> {
            replaceFragment(new ActionFragment());
            updateTextViewColors(textAction);  // Update text color when Action is selected
        });

        textDrama.setOnClickListener(v -> {
            replaceFragment(new DramaFragment());
            updateTextViewColors(textDrama);  // Update text color when Drama is selected
        });

        textHorror.setOnClickListener(v -> {
            replaceFragment(new HorrorFragment());
            updateTextViewColors(textHorror);  // Update text color when Horror is selected
        });

        textComedy.setOnClickListener(v -> {
            replaceFragment(new ComedyFragment());
            updateTextViewColors(textComedy);  // Update text color when Comedy is selected
        });

        // Initialize RecyclerView
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMovies.setHasFixedSize(true);

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

    private void updateTextViewColors(TextView selectedTextView) {
        // Reset all text colors to default
        textAction.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        textDrama.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        textHorror.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        textComedy.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        // Set the selected TextView's text color to white
        selectedTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void fetchMovies() {
        // Create instance of the MovieApiService
        MovieApiService  apiService = MovieApiClient.getRetrofitInstance().create(MovieApiService.class);
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
                Toast.makeText(getActivity(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MoviesAPI", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        // Set adapter for RecyclerView
        movieAdapter = new MovieActionAdapter(movies);
        recyclerViewMovies.setAdapter(movieAdapter);
    }
}
