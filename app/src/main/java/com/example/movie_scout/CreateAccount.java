package com.example.movie_scout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends AppCompatActivity {

    // Declare your views (EditTexts, Buttons, etc.)
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize views
        usernameEditText = findViewById(R.id.username_id);
        passwordEditText = findViewById(R.id.password_id);
        registerButton = findViewById(R.id.btn_create);

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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both username and password", Toast.LENGTH_SHORT).show();
                return; // Stop if fields are empty
            }

            // Check for username length (min 10, max 15)
            if (username.length() < 15 || username.length() > 20) {
                Toast.makeText(this, "Username must be between 15 and 20 characters", Toast.LENGTH_SHORT).show();
                return; // Stop if username is not within the range
            }

            // Check for password length (min 10, max 15)
            if (password.length() < 10 || password.length() > 15) {
                Toast.makeText(this, "Password must be between 10 and 15 characters", Toast.LENGTH_SHORT).show();
                return; // Stop if password is not within the range
            }

            if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill username and password", Toast.LENGTH_SHORT).show();
            return; // Stop the execution if fields are empty
            }


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(newUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Registration successful
                    startActivity(new Intent(CreateAccount.this,MainActivity.class));
                    Log.d("Registration", "Success: " + response.body().toString());
                    Toast.makeText(CreateAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "username and password are already Exist.", Toast.LENGTH_SHORT).show();
                    Log.e("Registration", "Failed: " + response.errorBody().toString());
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
}
