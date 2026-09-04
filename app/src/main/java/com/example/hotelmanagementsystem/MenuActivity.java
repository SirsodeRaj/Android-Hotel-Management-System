package com.example.hotelmanagementsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    EditText etSearch;
    EditText etDishName;
    EditText etDishPrice;

    Button btnAdd;
    Button btnUpdate;
    Button btnDelete;
    Button btnClear;
    private ImageButton btnBack;
    RecyclerView recyclerDishes;

    DatabaseHelper databaseHelper;

    ArrayList<Dish> dishList;
    DishAdapter dishAdapter;

    // Stores selected dish number
    int selectedDishNo = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java with XML
        setContentView(R.layout.activity_menu);

        SystemBarHelper.setup(this);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Connect views
        etSearch = findViewById(R.id.etSearch);
        etDishName = findViewById(R.id.etDishName);
        etDishPrice = findViewById(R.id.etDishPrice);

        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);

        recyclerDishes = findViewById(R.id.recyclerDishes);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Prepare RecyclerView
        recyclerDishes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        dishList = new ArrayList<>();

        // Adapter
        dishAdapter = new DishAdapter(
                dishList,
                dish -> {

                    // Store selected dish number
                    selectedDishNo = dish.getDishNo();

                    // Show selected dish details
                    etDishName.setText(dish.getDishName());

                    etDishPrice.setText(
                            String.valueOf(dish.getDishPrice())
                    );
                }
        );

        recyclerDishes.setAdapter(dishAdapter);

        // Load all dishes
        loadDishes("");

        // Search as user types
        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        loadDishes(s.toString().trim());
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        // Add dish
        btnAdd.setOnClickListener(v -> addDish());

        // Update dish
        btnUpdate.setOnClickListener(v -> updateDish());

        // Delete dish
        btnDelete.setOnClickListener(v -> deleteDish());

        // Clear fields
        btnClear.setOnClickListener(v -> clearFields());
    }

    // Load dishes from database
    private void loadDishes(String searchText) {

        dishList.clear();

        Cursor cursor;

        if (searchText.isEmpty()) {

            cursor = databaseHelper.getAllDishes();

        } else {

            cursor = databaseHelper.searchDishes(searchText);
        }

        while (cursor.moveToNext()) {

            int dishNo =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("dish_no")
                    );

            String dishName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("dish_name")
                    );

            double dishPrice =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("dish_price")
                    );

            dishList.add(
                    new Dish(
                            dishNo,
                            dishName,
                            dishPrice
                    )
            );
        }

        cursor.close();

        dishAdapter.notifyDataSetChanged();
    }

    // Add new dish
    private void addDish() {

        String name =
                etDishName.getText().toString().trim();

        String priceText =
                etDishPrice.getText().toString().trim();

        if (name.isEmpty()) {

            etDishName.setError("Enter dish name");
            etDishName.requestFocus();
            return;
        }

        if (priceText.isEmpty()) {

            etDishPrice.setError("Enter dish price");
            etDishPrice.requestFocus();
            return;
        }

        double price;

        try {

            price = Double.parseDouble(priceText);

        } catch (NumberFormatException e) {

            etDishPrice.setError("Invalid price");
            return;
        }

        if (price <= 0) {

            etDishPrice.setError(
                    "Price must be greater than zero"
            );

            return;
        }

        boolean result =
                databaseHelper.addDish(name, price);

        if (result) {

            Toast.makeText(
                    this,
                    "Dish added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();
            loadDishes(etSearch.getText().toString().trim());

        } else {

            Toast.makeText(
                    this,
                    "Dish could not be added",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Update selected dish
    private void updateDish() {

        if (selectedDishNo == -1) {

            Toast.makeText(
                    this,
                    "Select a dish first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String name =
                etDishName.getText().toString().trim();

        String priceText =
                etDishPrice.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter all dish details",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double price;

        try {

            price = Double.parseDouble(priceText);

        } catch (NumberFormatException e) {

            etDishPrice.setError("Invalid price");
            return;
        }

        if (price <= 0) {

            etDishPrice.setError(
                    "Price must be greater than zero"
            );

            return;
        }

        boolean result =
                databaseHelper.updateDish(
                        selectedDishNo,
                        name,
                        price
                );

        if (result) {

            Toast.makeText(
                    this,
                    "Dish updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();
            loadDishes(etSearch.getText().toString().trim());

        } else {

            Toast.makeText(
                    this,
                    "Dish could not be updated",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Delete selected dish
    private void deleteDish() {

        if (selectedDishNo == -1) {

            Toast.makeText(
                    this,
                    "Select a dish first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean result =
                databaseHelper.deleteDish(selectedDishNo);

        if (result) {

            Toast.makeText(
                    this,
                    "Dish deleted successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();
            loadDishes(etSearch.getText().toString().trim());

        } else {

            Toast.makeText(
                    this,
                    "Dish could not be deleted",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Clear form
    private void clearFields() {

        etDishName.setText("");
        etDishPrice.setText("");

        selectedDishNo = -1;
    }
}