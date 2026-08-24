package com.example.hotelmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Login screen controls
    EditText etUsername, etPassword;
    Button btnLogin;

    // Database object
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java file with login XML
        setContentView(R.layout.activity_login);

        // Connect XML controls with Java
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Create database object
        databaseHelper = new DatabaseHelper(this);

        // Login button click
        btnLogin.setOnClickListener(v -> {

            // Get entered values
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Check username
            if (username.isEmpty()) {
                etUsername.setError("Enter username");
                etUsername.requestFocus();
                return;
            }

            // Check password
            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                etPassword.requestFocus();
                return;
            }

            // Check login details in local database
            boolean loginSuccessful =
                    databaseHelper.checkLogin(username, password);

            if (loginSuccessful) {

                Toast.makeText(
                        LoginActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                ).show();

                // Open Dashboard
                Intent intent = new Intent(
                        LoginActivity.this,
                        DashboardActivity.class
                );

                startActivity(intent);

                // Prevent returning to Login using back button
                finish();

            } else {

                // Login failed
                Toast.makeText(
                        LoginActivity.this,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}