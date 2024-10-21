package com.example.movie_scout;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieComedyApiClient {
    private static final String BASE_URL = "http://192.168.9.240:3006/"; // For Android Emulator
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
