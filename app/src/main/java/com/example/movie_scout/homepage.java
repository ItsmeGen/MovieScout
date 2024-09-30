package com.example.movie_scout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

        public class homepage extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                EdgeToEdge.enable(this);
                setContentView(R.layout.activity_homepage);

                ImageView home_btn =findViewById(R.id.btn_home);
                ImageView fav_btn =findViewById(R.id.btn_fav);
                ImageView genre_btn =findViewById(R.id.btn_genre);
                ImageView menu_btn =findViewById(R.id.btn_menu);

                home_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(homepage.this, homepage.class));
                    }
                });

                fav_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(homepage.this, favorite_page.class));
                    }
                });

                genre_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(homepage.this, genre.class));
                    }
                });
                menu_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(homepage.this, menu.class));
                    }
                });

            }
        }
