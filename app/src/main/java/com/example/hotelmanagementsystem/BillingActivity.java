package com.example.hotelmanagementsystem;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BillingActivity extends AppCompatActivity {

    // Customer details
    EditText etCustomerName;
    EditText etMobileNo;

    // Dish search and quantity
    EditText etDishSearch;
    EditText etQuantity;

    // Display fields
    TextView tvBillNo;
    TextView tvBillDate;
    TextView tvSelectedDish;
    TextView tvSelectedPrice;
    TextView tvAmount;
    TextView tvTotal;

    // Buttons
    Button btnAddToCart;
    Button btnDeleteItem;
    Button btnSaveBill;

    // RecyclerViews
    RecyclerView recyclerDishSearch;
    RecyclerView recyclerCart;

    // Database
    DatabaseHelper databaseHelper;

    // Search results
    ArrayList<Dish> searchDishList;
    DishAdapter searchAdapter;

    // Selected dish
    Dish selectedDish = null;

    // Cart
    ArrayList<CartItem> cartList;
    CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java with XML
        setContentView(R.layout.activity_billing);


        // =====================================================
        // CUSTOMER FIELDS
        // =====================================================

        etCustomerName =
                findViewById(R.id.etCustomerName);

        etMobileNo =
                findViewById(R.id.etMobileNo);


        // =====================================================
        // DISH FIELDS
        // =====================================================

        etDishSearch =
                findViewById(R.id.etDishSearch);

        etQuantity =
                findViewById(R.id.etQuantity);


        // =====================================================
        // DISPLAY FIELDS
        // =====================================================

        tvBillNo =
                findViewById(R.id.tvBillNo);

        tvBillDate =
                findViewById(R.id.tvBillDate);

        tvSelectedDish =
                findViewById(R.id.tvSelectedDish);

        tvSelectedPrice =
                findViewById(R.id.tvSelectedPrice);

        tvAmount =
                findViewById(R.id.tvAmount);

        tvTotal =
                findViewById(R.id.tvTotal);


        // =====================================================
        // BUTTONS
        // =====================================================

        btnAddToCart =
                findViewById(R.id.btnAddToCart);

        btnDeleteItem =
                findViewById(R.id.btnDeleteItem);

        btnSaveBill =
                findViewById(R.id.btnSaveBill);


        // =====================================================
        // RECYCLERVIEWS
        // =====================================================

        recyclerDishSearch =
                findViewById(R.id.recyclerDishSearch);

        recyclerCart =
                findViewById(R.id.recyclerCart);


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // BILL NUMBER
        // =====================================================

        int nextBillNo =
                databaseHelper.getNextBillNumber();

        tvBillNo.setText(
                "Bill No.: " + nextBillNo
        );


        // =====================================================
        // DATE AND TIME
        // =====================================================

        updateDate();


        // =====================================================
        // DISH SEARCH RECYCLERVIEW
        // =====================================================

        recyclerDishSearch.setLayoutManager(
                new LinearLayoutManager(this)
        );

        searchDishList =
                new ArrayList<>();


        // =====================================================
        // DISH SEARCH ADAPTER
        // =====================================================

        searchAdapter = new DishAdapter(
                searchDishList,

                dish -> {

                    // Save selected dish
                    selectedDish = dish;

                    // Display dish name
                    tvSelectedDish.setText(
                            "Selected Dish: "
                                    + dish.getDishName()
                    );

                    // Display price
                    tvSelectedPrice.setText(
                            "Price: ₹"
                                    + String.format(
                                    "%.2f",
                                    dish.getDishPrice()
                            )
                    );

                    // Clear search field
                    etDishSearch.setText("");

                    // Hide search results
                    recyclerDishSearch.setVisibility(
                            View.GONE
                    );

                    // Calculate amount
                    calculateAmount();
                }
        );

        recyclerDishSearch.setAdapter(
                searchAdapter
        );


        // =====================================================
        // SEARCH WHILE TYPING
        // =====================================================

        etDishSearch.addTextChangedListener(
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

                        searchDishes(
                                s.toString().trim()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );


        // =====================================================
        // CART RECYCLERVIEW
        // =====================================================

        recyclerCart.setLayoutManager(
                new LinearLayoutManager(this)
        );

        cartList =
                new ArrayList<>();

        cartAdapter =
                new CartAdapter(cartList);

        recyclerCart.setAdapter(
                cartAdapter
        );


        // =====================================================
        // QUANTITY CHANGE
        // =====================================================

        etQuantity.addTextChangedListener(
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

                        calculateAmount();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );


        // =====================================================
        // ADD TO CART
        // =====================================================

        btnAddToCart.setOnClickListener(
                v -> addToCart()
        );


        // =====================================================
        // DELETE CART ITEM
        // =====================================================

        btnDeleteItem.setOnClickListener(
                v -> deleteCartItem()
        );


        // =====================================================
        // SAVE BILL
        // =====================================================

        btnSaveBill.setOnClickListener(
                v -> saveBill()
        );
    }


    // =========================================================
    // SEARCH DISHES
    // =========================================================

    private void searchDishes(String searchText) {

        searchDishList.clear();

        // Empty search
        if (searchText.isEmpty()) {

            searchAdapter.notifyDataSetChanged();

            recyclerDishSearch.setVisibility(
                    View.GONE
            );

            return;
        }


        Cursor cursor =
                databaseHelper.searchDishes(
                        searchText
                );


        // Read database results
        while (cursor.moveToNext()) {

            int dishNo =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "dish_no"
                            )
                    );

            String dishName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "dish_name"
                            )
                    );

            double dishPrice =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "dish_price"
                            )
                    );


            searchDishList.add(
                    new Dish(
                            dishNo,
                            dishName,
                            dishPrice
                    )
            );
        }


        cursor.close();


        // Refresh results
        searchAdapter.notifyDataSetChanged();


        if (searchDishList.size() > 0) {

            recyclerDishSearch.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerDishSearch.setVisibility(
                    View.GONE
            );
        }
    }


    // =========================================================
    // CALCULATE ITEM AMOUNT
    // =========================================================

    private void calculateAmount() {

        // No dish selected
        if (selectedDish == null) {

            tvAmount.setText(
                    "Amount: ₹0.00"
            );

            return;
        }


        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();


        // Quantity empty
        if (quantityText.isEmpty()) {

            tvAmount.setText(
                    "Amount: ₹0.00"
            );

            return;
        }


        try {

            int quantity =
                    Integer.parseInt(quantityText);


            if (quantity <= 0) {

                tvAmount.setText(
                        "Amount: ₹0.00"
                );

                return;
            }


            double amount =
                    selectedDish.getDishPrice()
                            * quantity;


            tvAmount.setText(
                    "Amount: ₹"
                            + String.format(
                            "%.2f",
                            amount
                    )
            );

        } catch (NumberFormatException e) {

            tvAmount.setText(
                    "Amount: ₹0.00"
            );
        }
    }


    // =========================================================
    // ADD TO CART
    // =========================================================

    private void addToCart() {

        // Check dish
        if (selectedDish == null) {

            Toast.makeText(
                    this,
                    "Please select a dish first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Get quantity
        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();


        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Enter quantity"
            );

            etQuantity.requestFocus();

            return;
        }


        int quantity;


        try {

            quantity =
                    Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Invalid quantity"
            );

            return;
        }


        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than zero"
            );

            return;
        }


        // =====================================================
        // DUPLICATE DISH CHECK
        // =====================================================

        for (CartItem item : cartList) {

            if (item.getDishNo()
                    == selectedDish.getDishNo()) {

                Toast.makeText(
                        this,
                        "Dish is already in cart",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
        }


        // Create cart item
        CartItem cartItem =
                new CartItem(
                        selectedDish.getDishNo(),
                        selectedDish.getDishName(),
                        selectedDish.getDishPrice(),
                        quantity
                );


        // Add to list
        cartList.add(cartItem);


        // Refresh cart
        cartAdapter.notifyDataSetChanged();


        // Calculate total
        calculateTotal();


        // Clear selection
        selectedDish = null;

        tvSelectedDish.setText(
                "Selected Dish: None"
        );

        tvSelectedPrice.setText(
                "Price: ₹0.00"
        );

        etQuantity.setText("");

        tvAmount.setText(
                "Amount: ₹0.00"
        );


        Toast.makeText(
                this,
                "Dish added to cart",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // CALCULATE TOTAL
    // =========================================================

    private void calculateTotal() {

        double total = 0;


        for (CartItem item : cartList) {

            total += item.getAmount();
        }


        tvTotal.setText(
                "Total Bill: ₹"
                        + String.format(
                        "%.2f",
                        total
                )
        );
    }


    // =========================================================
    // DELETE CART ITEM
    // =========================================================

    private void deleteCartItem() {

        int selectedPosition =
                cartAdapter.getSelectedPosition();


        // Nothing selected
        if (selectedPosition == -1) {

            Toast.makeText(
                    this,
                    "Select a cart item first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Remove item
        cartList.remove(selectedPosition);


        // Clear adapter selection
        cartAdapter.clearSelection();


        // Refresh RecyclerView
        cartAdapter.notifyDataSetChanged();


        // Recalculate total
        calculateTotal();


        Toast.makeText(
                this,
                "Item deleted from cart",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // SAVE BILL
    // =========================================================

    private void saveBill() {

        // Customer name
        String customerName =
                etCustomerName.getText()
                        .toString()
                        .trim();


        if (customerName.isEmpty()) {

            etCustomerName.setError(
                    "Enter customer name"
            );

            etCustomerName.requestFocus();

            return;
        }


        // Mobile number
        String mobileNo =
                etMobileNo.getText()
                        .toString()
                        .trim();


        if (mobileNo.isEmpty()) {

            etMobileNo.setError(
                    "Enter mobile number"
            );

            etMobileNo.requestFocus();

            return;
        }


        // Cart must contain items
        if (cartList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Add at least one item to the cart",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Get bill number
        int billNo =
                databaseHelper.getNextBillNumber();


        // Get date
        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm"
                );

        String billDate =
                format.format(new Date());


        // Calculate final total
        double totalBill = 0;

        for (CartItem item : cartList) {

            totalBill += item.getAmount();
        }


        // Save to database
        boolean result =
                databaseHelper.saveBill(
                        billNo,
                        billDate,
                        customerName,
                        mobileNo,
                        totalBill,
                        cartList
                );


        // =====================================================
        // SAVE SUCCESS
        // =====================================================

        if (result) {

            Toast.makeText(
                    this,
                    "Bill saved successfully",
                    Toast.LENGTH_LONG
            ).show();


            // Clear cart
            cartList.clear();

            cartAdapter.clearSelection();

            cartAdapter.notifyDataSetChanged();


            // Reset total
            tvTotal.setText(
                    "Total Bill: ₹0.00"
            );


            // Clear customer details
            etCustomerName.setText("");
            etMobileNo.setText("");


            // Clear selected dish
            selectedDish = null;

            tvSelectedDish.setText(
                    "Selected Dish: None"
            );

            tvSelectedPrice.setText(
                    "Price: ₹0.00"
            );

            etQuantity.setText("");

            tvAmount.setText(
                    "Amount: ₹0.00"
            );


            // Get next bill number
            int nextBillNo =
                    databaseHelper.getNextBillNumber();

            tvBillNo.setText(
                    "Bill No.: " + nextBillNo
            );


            // Update date
            updateDate();

        } else {

            Toast.makeText(
                    this,
                    "Failed to save bill",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =========================================================
    // UPDATE DATE
    // =========================================================

    private void updateDate() {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm"
                );

        tvBillDate.setText(
                format.format(new Date())
        );
    }
}