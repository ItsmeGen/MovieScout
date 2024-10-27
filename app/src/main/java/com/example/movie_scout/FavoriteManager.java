package com.example.movie_scout;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class FavoriteManager {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "favorites";
    private static final String KEY_FAVORITES = "favorite_movies";

    public FavoriteManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addFavorite(String movieJson) {
        Set<String> favorites = sharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<>());
        favorites.add(movieJson);
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public Set<String> getFavorites() {
        return sharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<>());
    }

    public void updateFavorites(Set<String> favorites) {
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }
}

