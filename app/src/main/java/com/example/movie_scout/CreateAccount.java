package com.example.movie_scout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends AppCompatActivity {

    // Declare your views (EditTexts, Buttons, etc.)
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private boolean isPasswordVisible = false; // Flag to track password visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize views
        usernameEditText = findViewById(R.id.username_id);
        passwordEditText = findViewById(R.id.password_id);
        registerButton = findViewById(R.id.btn_create);

        // Set up the password visibility toggle
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(); // Call the register method
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User newUser = new User(username, password);

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both username and password", Toast.LENGTH_SHORT).show();
            return; // Stop if fields are empty
        }

        // Check for username length (min 10, max 15)
        if (username.length() < 15 || username.length() > 20) {
            Toast.makeText(this, "Username must be between 10 and 15 characters", Toast.LENGTH_SHORT).show();
            return; // Stop if username is not within the range
        }

        // Check for password length (min 10, max 15)
        if (password.length() < 10 || password.length() > 15) {
            Toast.makeText(this, "Password must be between 10 and 15 characters", Toast.LENGTH_SHORT).show();
            return; // Stop if password is not within the range
        }

        ApiService apiService = LoginApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(newUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Registration successful
                    startActivity(new Intent(CreateAccount.this, MainActivity.class));
                    Log.d("Registration", "Success: " + response.body().toString());
                    Toast.makeText(CreateAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Username and password already exist.", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Registration", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("Registration", "Error reading error response", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error
                Toast.makeText(CreateAccount.this, "Registration Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Registration", "Error: " + t.getMessage());
            }
        });
    }

    // Method to toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_icon_close, 0); // Set closed eye icon
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_icon_open, 0); // Set open eye icon
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end
    }
}
