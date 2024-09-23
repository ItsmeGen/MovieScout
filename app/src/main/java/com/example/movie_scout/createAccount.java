package com.example.movie_scout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class createAccount extends AppCompatActivity {

    // Declare your views (EditTexts, Buttons, etc.)
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize your views
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

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(newUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Registration successful
                    Toast.makeText(createAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error
                Toast.makeText(createAccount.this, "Registration Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
