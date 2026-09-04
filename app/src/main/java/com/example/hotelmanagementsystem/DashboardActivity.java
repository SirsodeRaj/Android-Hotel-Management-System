package com.example.hotelmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnMenu;
    Button btnBilling;
    Button btnLogout;
    Button btnBillHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java with dashboard XML
        setContentView(R.layout.activity_dashboard);

        SystemBarHelper.setup(this);

        // Connect buttons
        btnMenu = findViewById(R.id.btnMenu);
        btnBilling = findViewById(R.id.btnBilling);
        btnLogout = findViewById(R.id.btnLogout);
        btnBillHistory =
                findViewById(
                        R.id.btnBillHistory
                );

        // Open Menu Management
        btnMenu.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MenuActivity.class
            );

            startActivity(intent);
        });

        // Open Billing screen
        btnBilling.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            BillingActivity.class
                    );

            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {

            // Return to Login screen
            Intent intent = new Intent(
                    DashboardActivity.this,
                    LoginActivity.class
            );

            // Remove Dashboard from back stack
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });

        btnBillHistory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            BillHistoryActivity.class
                    );

            startActivity(intent);
        });

    }
}