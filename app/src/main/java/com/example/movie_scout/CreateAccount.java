package com.example.movie_scout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {

    private EditText usernameEditText;  // This will now be used for email input
    private EditText passwordEditText;
    private Button registerButton;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize views
        usernameEditText = findViewById(R.id.username_id); // This is your email EditText now
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
        String email = usernameEditText.getText().toString();  // Email input is retrieved here
        String password = passwordEditText.getText().toString();
        User newUser = new User(email, password);

        // Check for empty fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
            return; // Stop if fields are empty
        }

        // Check for valid Gmail address using regex
        if (!isValidGmail(email)) {
            Toast.makeText(this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
            return; // Stop if the email is not valid
        }

        // Check for email length (min 10, max 20 characters)
        if (email.length() < 10 || email.length() > 20) {
            Toast.makeText(this, "Email must be between 10 and 20 characters", Toast.LENGTH_SHORT).show();
            return; // Stop if email is not within the range
        }

        // Check for password length (min 10, max 15)
        if (password.length() < 10 || password.length() > 20) {
            Toast.makeText(this, "Password must be between 10 and 20 characters", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccount.this, "Email already exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateAccount.this, "Registration Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to validate Gmail addresses with regex
    private boolean isValidGmail(String email) {
        // Regex pattern for a valid Gmail address
        String gmailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(gmailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

