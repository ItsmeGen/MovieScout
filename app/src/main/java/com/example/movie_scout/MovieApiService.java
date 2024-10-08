package com.example.movie_scout;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface MovieApiService {
    @GET("movies")
    Call<List<Movie>> getAllMovies();
}
