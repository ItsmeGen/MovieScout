package com.example.movie_scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavFragment extends Fragment {
    private TextView noFavoritesMessage;
    private RecyclerView favoritesRecyclerView;
    private MovieAdapter favoritesAdapter;
    private List<Movie> favoriteMovies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        noFavoritesMessage = view.findViewById(R.id.noFavoritesMessage);
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoriteMovies = new ArrayList<>();

        loadFavorites(); // Load favorite movies

        favoritesAdapter = new MovieAdapter(favoriteMovies);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        return view;
    }

    private void loadFavorites() {
        FavoriteManager favoriteManager = new FavoriteManager(getContext());
        Set<String> favoriteSet = favoriteManager.getFavorites();

        if (favoriteSet.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.GONE);
            noFavoritesMessage.setVisibility(View.VISIBLE); //
        } else {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            noFavoritesMessage.setVisibility(View.GONE);
            Gson gson = new Gson();
            for (String movieJson : favoriteSet) {
                Movie movie = gson.fromJson(movieJson, Movie.class);
                favoriteMovies.add(movie);
            }
        }

    }
}
