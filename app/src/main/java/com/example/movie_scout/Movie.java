package com.example.movie_scout;

public class Movie {
    private int movie_id;
    private String title;
    private String description;
    private String genre;
    private String director;
    private int year_released;
    private String image_url;


    // Constructor
    public Movie(int movie_id, String title, String description, String genre, int year_released, String director,String image_url ) {
        this.movie_id = movie_id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.director = director;
        this.year_released = year_released;
        this.image_url = image_url;
    }

    // Getters
    public int getMovie_id() { return movie_id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public int getYear_released() { return year_released; }
    public String getImage_url() { return image_url; }
}

