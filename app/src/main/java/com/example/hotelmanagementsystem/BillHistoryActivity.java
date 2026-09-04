package com.example.hotelmanagementsystem;

import android.app.AlertDialog;
import android.database.Cursor;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillHistoryActivity extends AppCompatActivity {

    // =====================================================
    // VIEWS
    // =====================================================

    private RecyclerView recyclerBillHistory;

    private TextView tvNoBills;

    private EditText etBillSearch;

    private ImageButton btnBack;


    // =====================================================
    // DATABASE
    // =====================================================

    private DatabaseHelper databaseHelper;


    // =====================================================
    // BILL LIST
    // =====================================================

    private ArrayList<Bill> billList;


    // =====================================================
    // ADAPTER
    // =====================================================

    private BillAdapter billAdapter;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_bill_history
        );


        // =================================================
        // SYSTEM BAR
        // =================================================

        SystemBarHelper.setup(this);


        // =================================================
        // CONNECT VIEWS
        // =================================================

        recyclerBillHistory =
                findViewById(
                        R.id.recyclerBillHistory
                );

        tvNoBills =
                findViewById(
                        R.id.tvNoBills
                );

        etBillSearch =
                findViewById(
                        R.id.etBillSearch
                );

        btnBack =
                findViewById(
                        R.id.btnBack
                );


        // =================================================
        // BACK BUTTON
        // =================================================

        btnBack.setOnClickListener(v -> {

            finish();

        });


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =================================================
        // BILL LIST
        // =================================================

        billList =
                new ArrayList<>();


        // =================================================
        // RECYCLER VIEW
        // =================================================

        recyclerBillHistory.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // =================================================
        // BILL ADAPTER
        // =================================================

        billAdapter =
                new BillAdapter(

                        billList,

                        // ---------------------------------
                        // VIEW BILL
                        // ---------------------------------

                        bill -> {

                            Intent intent =
                                    new Intent(
                                            BillHistoryActivity.this,
                                            BillDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "bill_no",
                                    bill.getBillNo()
                            );

                            startActivity(intent);
                        },


                        // ---------------------------------
                        // DELETE BILL
                        // ---------------------------------

                        bill -> {

                            showDeleteConfirmation(
                                    bill
                            );
                        }
                );


        recyclerBillHistory.setAdapter(
                billAdapter
        );


        // =================================================
        // LOAD BILLS
        // =================================================

        loadBills();


        // =================================================
        // SEARCH
        // =================================================

        etBillSearch.addTextChangedListener(

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

                        String searchText =
                                s.toString().trim();


                        if (searchText.isEmpty()) {

                            loadBills();

                        } else {

                            searchBills(
                                    searchText
                            );
                        }
                    }


                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );
    }


    // =====================================================
    // LOAD ALL BILLS
    // =====================================================

    private void loadBills() {

        billList.clear();


        Cursor cursor =
                databaseHelper.getAllBills();


        while (cursor.moveToNext()) {

            addBillFromCursor(cursor);
        }


        cursor.close();


        billAdapter.notifyDataSetChanged();


        updateEmptyMessage();
    }


    // =====================================================
    // SEARCH BILLS
    // =====================================================

    private void searchBills(
            String searchText) {

        billList.clear();


        Cursor cursor =
                databaseHelper.searchBills(
                        searchText
                );


        while (cursor.moveToNext()) {

            addBillFromCursor(cursor);
        }


        cursor.close();


        billAdapter.notifyDataSetChanged();


        updateEmptyMessage();
    }


    // =====================================================
    // CREATE BILL OBJECT
    // =====================================================

    private void addBillFromCursor(
            Cursor cursor) {

        int billNo =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                "bill_no"
                        )
                );


        String billDate =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "bill_date"
                        )
                );


        String customerName =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "customer_name"
                        )
                );


        String mobileNo =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "mobile_no"
                        )
                );


        double totalBill =
                cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                "total_bill"
                        )
                );


        billList.add(
                new Bill(
                        billNo,
                        billDate,
                        customerName,
                        mobileNo,
                        totalBill
                )
        );
    }


    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void updateEmptyMessage() {

        if (billList.isEmpty()) {

            recyclerBillHistory.setVisibility(
                    View.GONE
            );

            tvNoBills.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerBillHistory.setVisibility(
                    View.VISIBLE
            );

            tvNoBills.setVisibility(
                    View.GONE
            );
        }
    }


    // =====================================================
    // DELETE CONFIRMATION
    // =====================================================

    private void showDeleteConfirmation(
            Bill bill) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Bill?"
                )

                .setMessage(
                        "Are you sure you want to " +
                                "delete Bill No. " +
                                bill.getBillNo() +
                                "?\n\n" +
                                "This action cannot be undone."
                )

                .setNegativeButton(
                        "CANCEL",
                        (dialog, which) -> {
                            dialog.dismiss();
                        }
                )

                .setPositiveButton(
                        "DELETE",
                        (dialog, which) -> {

                            deleteBill(
                                    bill
                            );
                        }
                )

                .show();
    }


    // =====================================================
    // DELETE BILL
    // =====================================================

    private void deleteBill(
            Bill bill) {

        boolean deleted =
                databaseHelper.deleteBill(
                        bill.getBillNo()
                );


        if (deleted) {

            Toast.makeText(
                    this,
                    "Bill No. " +
                            bill.getBillNo() +
                            " deleted successfully",
                    Toast.LENGTH_SHORT
            ).show();


            String currentSearch =
                    etBillSearch
                            .getText()
                            .toString()
                            .trim();


            if (currentSearch.isEmpty()) {

                loadBills();

            } else {

                searchBills(
                        currentSearch
                );
            }

        } else {

            Toast.makeText(
                    this,
                    "Unable to delete bill",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =====================================================
    // RELOAD
    // =====================================================

    @Override
    protected void onResume() {

        super.onResume();


        if (billAdapter != null) {

            String currentSearch =
                    etBillSearch
                            .getText()
                            .toString()
                            .trim();


            if (currentSearch.isEmpty()) {

                loadBills();

            } else {

                searchBills(
                        currentSearch
                );
            }
        }
    }
}