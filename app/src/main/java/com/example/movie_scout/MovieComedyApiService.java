package com.example.movie_scout;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;


public interface MovieComedyApiService {
    @GET("comedy_movies")
    Call<List<Movie>> getAllMovies();
}
