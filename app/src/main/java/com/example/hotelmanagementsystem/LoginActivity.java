package com.example.hotelmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // =====================================================
    // LOGIN CONTROLS
    // =====================================================

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin;

    private CheckBox cbRememberMe;


    // =====================================================
    // DATABASE
    // =====================================================

    private DatabaseHelper databaseHelper;


    // =====================================================
    // REMEMBER LOGIN
    // =====================================================

    private SharedPreferences loginPreferences;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Connect Java with XML
        setContentView(R.layout.activity_login);


        // =================================================
        // SYSTEM BAR
        // =================================================

        SystemBarHelper.setup(this);


        // =================================================
        // CONNECT XML CONTROLS
        // =================================================

        etUsername =
                findViewById(R.id.etUsername);

        etPassword =
                findViewById(R.id.etPassword);

        btnLogin =
                findViewById(R.id.btnLogin);

        cbRememberMe =
                findViewById(R.id.cbRememberMe);


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =================================================
        // SHARED PREFERENCES
        // =================================================

        loginPreferences =
                getSharedPreferences(
                        "HotelLogin",
                        MODE_PRIVATE
                );


        // =================================================
        // LOAD SAVED LOGIN
        // =================================================

        loadSavedLogin();


        // =================================================
        // LOGIN BUTTON
        // =================================================

        btnLogin.setOnClickListener(v -> {

            loginUser();

        });
    }


    // =====================================================
    // LOAD SAVED LOGIN
    // =====================================================

    private void loadSavedLogin() {

        String savedUsername =
                loginPreferences.getString(
                        "username",
                        ""
                );


        String savedPassword =
                loginPreferences.getString(
                        "password",
                        ""
                );


        boolean rememberLogin =
                loginPreferences.getBoolean(
                        "remember",
                        false
                );


        if (rememberLogin &&
                !savedUsername.isEmpty() &&
                !savedPassword.isEmpty()) {


            etUsername.setText(
                    savedUsername
            );


            etPassword.setText(
                    savedPassword
            );


            cbRememberMe.setChecked(
                    true
            );
        }
    }


    // =====================================================
    // LOGIN USER
    // =====================================================

    private void loginUser() {

        // Get entered values

        String username =
                etUsername
                        .getText()
                        .toString()
                        .trim();


        String password =
                etPassword
                        .getText()
                        .toString()
                        .trim();


        // =================================================
        // USERNAME VALIDATION
        // =================================================

        if (username.isEmpty()) {

            etUsername.setError(
                    "Enter username"
            );

            etUsername.requestFocus();

            return;
        }


        // =================================================
        // PASSWORD VALIDATION
        // =================================================

        if (password.isEmpty()) {

            etPassword.setError(
                    "Enter password"
            );

            etPassword.requestFocus();

            return;
        }


        // =================================================
        // CHECK DATABASE LOGIN
        // =================================================

        boolean loginSuccessful =
                databaseHelper.checkLogin(
                        username,
                        password
                );


        // =================================================
        // LOGIN SUCCESS
        // =================================================

        if (loginSuccessful) {


            // ---------------------------------------------
            // REMEMBER LOGIN
            // ---------------------------------------------

            if (cbRememberMe.isChecked()) {

                loginPreferences
                        .edit()
                        .putString(
                                "username",
                                username
                        )
                        .putString(
                                "password",
                                password
                        )
                        .putBoolean(
                                "remember",
                                true
                        )
                        .apply();

            } else {

                // -----------------------------------------
                // REMOVE SAVED LOGIN
                // -----------------------------------------

                loginPreferences
                        .edit()
                        .clear()
                        .apply();
            }


            // ---------------------------------------------
            // SUCCESS MESSAGE
            // ---------------------------------------------

            Toast.makeText(
                    LoginActivity.this,
                    "Login Successful",
                    Toast.LENGTH_SHORT
            ).show();


            // ---------------------------------------------
            // OPEN DASHBOARD
            // ---------------------------------------------

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            DashboardActivity.class
                    );


            startActivity(intent);


            // Prevent going back to login
            finish();

        } else {


            // =================================================
            // LOGIN FAILED
            // =================================================

            Toast.makeText(
                    LoginActivity.this,
                    "Invalid username or password",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}