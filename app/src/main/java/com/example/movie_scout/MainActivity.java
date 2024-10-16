package com.example.movie_scout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Declare your views (EditTexts, Buttons)
    private EditText usernameEditText;
    private EditText passwordEditText;
    private boolean isPasswordVisible = false; // Flag to track password visibility
    private Button loginButton;

    // Loading dialog
    private AlertDialog loadingDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_btn);
        TextView sign_up_btn = findViewById(R.id.sign_up);

        // Set up the password visibility toggle
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is on the drawable end (eye icon)
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
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

        showLoadingDialog(); // Show loading dialog when login starts

        ApiService apiService = LoginApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> loginCall = apiService.loginUser(loginUser);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissLoadingDialog(); // Hide the dialog once response is received

                if (response.isSuccessful()) {
                    // Login successful
                    startActivity(new Intent(MainActivity.this, BottomNavMenu.class));
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                } else {
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
                dismissLoadingDialog(); // Hide the dialog on failure
                Toast.makeText(MainActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_icon_close, 0); // Set close eye icon
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_icon_open, 0); // Set open eye icon
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end
    }

    // Method to show loading dialog
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);

        // Set up the progress bar and text view
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        TextView loadingMessage = dialogView.findViewById(R.id.loadingMessage);

        // Set custom colors
        loadingMessage.setTextColor(Color.WHITE); // Change this to your desired color
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.RED)); // Change to your desired color

        builder.setView(dialogView);
        builder.setCancelable(false); // Prevent dismiss by tapping outside
        loadingDialog = builder.create();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Make background transparent
        loadingDialog.show();
    }

    // Method to dismiss loading dialog
    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
