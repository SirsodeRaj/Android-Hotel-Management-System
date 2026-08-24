package com.example.hotelmanagementsystem;

import android.app.AlertDialog;
import android.database.Cursor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillHistoryActivity
        extends AppCompatActivity {

    RecyclerView recyclerBillHistory;

    TextView tvNoBills;

    DatabaseHelper databaseHelper;

    ArrayList<Bill> billList;

    BillAdapter billAdapter;


    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_bill_history
        );


        // =====================================================
        // CONNECT VIEWS
        // =====================================================

        recyclerBillHistory =
                findViewById(
                        R.id.recyclerBillHistory
                );


        tvNoBills =
                findViewById(
                        R.id.tvNoBills
                );


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // BILL LIST
        // =====================================================

        billList =
                new ArrayList<>();


        // =====================================================
        // RECYCLER VIEW
        // =====================================================

        recyclerBillHistory.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // =====================================================
        // ADAPTER
        // =====================================================

        billAdapter =
                new BillAdapter(

                        billList,


                        // -------------------------------------------------
                        // VIEW BILL
                        // -------------------------------------------------

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


                        // -------------------------------------------------
                        // DELETE BILL
                        // -------------------------------------------------

                        bill -> {

                            showDeleteConfirmation(
                                    bill
                            );
                        }
                );


        recyclerBillHistory.setAdapter(
                billAdapter
        );


        // =====================================================
        // LOAD BILLS
        // =====================================================

        loadBills();
    }


    // =========================================================
    // LOAD SAVED BILLS
    // =========================================================

    private void loadBills() {

        billList.clear();


        Cursor cursor =
                databaseHelper.getAllBills();


        while (cursor.moveToNext()) {

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


        cursor.close();


        // Refresh list
        billAdapter.notifyDataSetChanged();


        // =====================================================
        // SHOW / HIDE EMPTY MESSAGE
        // =====================================================

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


    // =========================================================
    // DELETE CONFIRMATION
    // =========================================================

    private void showDeleteConfirmation(
            Bill bill) {


        new AlertDialog.Builder(this)

                // Dialog title
                .setTitle(
                        "Delete Bill?"
                )


                // Dialog message
                .setMessage(
                        "Are you sure you want to " +
                                "delete Bill No. " +
                                bill.getBillNo() +
                                "?\n\n" +
                                "This action cannot be undone."
                )


                // CANCEL
                .setNegativeButton(
                        "CANCEL",
                        (dialog, which) -> {

                            // Simply close dialog
                            dialog.dismiss();
                        }
                )


                // DELETE
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


    // =========================================================
    // DELETE BILL
    // =========================================================

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


            // Reload list
            loadBills();

        } else {

            Toast.makeText(
                    this,
                    "Unable to delete bill",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // RELOAD WHEN RETURNING
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();


        if (billAdapter != null) {

            loadBills();
        }
    }
}