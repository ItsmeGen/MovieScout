package com.example.movie_scout;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieApiClient {
    private static final String BASE_URL = "http://192.168.42.240:3002/"; // For Android Emulator
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
