package com.example.movie_scout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn_log = findViewById(R.id.login_btn);
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, homepage.class));

            }
        });


        TextView text_btn = findViewById(R.id.sign_up);
        text_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Intent new_page = new Intent(MainActivity.this,createAccount.class);
                startActivity(new_page);


            }
        });
    }
}