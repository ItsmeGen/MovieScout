package com.example.movie_scout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    // Declare your views (EditTexts, Buttons, etc.)
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your views
        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_btn);
        TextView sign_up_btn = findViewById(R.id.sign_up);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,createAccount.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(); // Call the login method
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User loginUser = new User(username, password);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill username and password", Toast.LENGTH_SHORT).show();
            return; // Stop the execution if fields are empty
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> loginCall = apiService.loginUser(loginUser);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Login successful
                    startActivity(new Intent(MainActivity.this, homepage.class));
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        // Check for specific error messages
                        String errorMessage = response.errorBody().string();
                        if (errorMessage.contains("User not found")) {
                            Toast.makeText(getApplicationContext(), "Invalid email.", Toast.LENGTH_SHORT).show();
                        } else if (errorMessage.contains("Incorrect password")) {
                            Toast.makeText(getApplicationContext(), "Incorrect password.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error
                Toast.makeText(MainActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}





