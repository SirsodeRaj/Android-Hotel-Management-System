package com.example.hotelmanagementsystem;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class BillDetailsActivity
        extends AppCompatActivity {

    // =====================================================
    // VIEWS
    // =====================================================

    TextView tvBillNo;
    TextView tvBillDate;
    TextView tvCustomerName;
    TextView tvMobileNo;
    TextView tvBillItems;
    TextView tvBillTotal;

    Button btnPrintBill;

    private ImageButton btnBack;


    // =====================================================
    // DATABASE
    // =====================================================

    DatabaseHelper databaseHelper;


    // =====================================================
    // CURRENT BILL
    // =====================================================

    int currentBillNo;

    String billDate = "";
    String customerName = "";
    String mobileNo = "";

    double totalBill = 0;


    // =====================================================
    // BILL TEXT FOR PRINTING
    // =====================================================

    String printableBill = "";


    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_bill_details
        );

        SystemBarHelper.setup(this);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });


        // =====================================================
        // CONNECT VIEWS
        // =====================================================

        tvBillNo =
                findViewById(
                        R.id.tvBillNo
                );

        tvBillDate =
                findViewById(
                        R.id.tvBillDate
                );

        tvCustomerName =
                findViewById(
                        R.id.tvCustomerName
                );

        tvMobileNo =
                findViewById(
                        R.id.tvMobileNo
                );

        tvBillItems =
                findViewById(
                        R.id.tvBillItems
                );

        tvBillTotal =
                findViewById(
                        R.id.tvBillTotal
                );

        btnPrintBill =
                findViewById(
                        R.id.btnPrintBill
                );


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // GET BILL NUMBER
        // =====================================================

        currentBillNo =
                getIntent().getIntExtra(
                        "bill_no",
                        -1
                );


        // Invalid bill
        if (currentBillNo == -1) {

            Toast.makeText(
                    this,
                    "Invalid bill",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }


        // =====================================================
        // LOAD BILL
        // =====================================================

        loadBill(currentBillNo);


        // =====================================================
        // PRINT BILL
        // =====================================================

        btnPrintBill.setOnClickListener(v -> {

            printBill();
        });
    }


    // =========================================================
    // LOAD BILL
    // =========================================================

    private void loadBill(int billNo) {

        // -----------------------------------------------------
        // MAIN BILL
        // -----------------------------------------------------

        Cursor billCursor =
                databaseHelper
                        .getReadableDatabase()
                        .rawQuery(
                                "SELECT * FROM bills " +
                                        "WHERE bill_no = ?",

                                new String[]{
                                        String.valueOf(billNo)
                                }
                        );


        if (billCursor.moveToFirst()) {

            billDate =
                    billCursor.getString(
                            billCursor.getColumnIndexOrThrow(
                                    "bill_date"
                            )
                    );


            customerName =
                    billCursor.getString(
                            billCursor.getColumnIndexOrThrow(
                                    "customer_name"
                            )
                    );


            mobileNo =
                    billCursor.getString(
                            billCursor.getColumnIndexOrThrow(
                                    "mobile_no"
                            )
                    );


            totalBill =
                    billCursor.getDouble(
                            billCursor.getColumnIndexOrThrow(
                                    "total_bill"
                            )
                    );
        }


        billCursor.close();


        // -----------------------------------------------------
        // DISPLAY BILL INFORMATION
        // -----------------------------------------------------

        tvBillNo.setText(
                "Bill No.: " + billNo
        );


        tvBillDate.setText(
                "Date: " + billDate
        );


        tvCustomerName.setText(
                "Customer: " + customerName
        );


        tvMobileNo.setText(
                "Mobile: " + mobileNo
        );


        tvBillTotal.setText(
                "TOTAL: ₹" +
                        String.format(
                                "%.2f",
                                totalBill
                        )
        );


        // -----------------------------------------------------
        // BILL ITEMS
        // -----------------------------------------------------

        Cursor detailCursor =
                databaseHelper.getBillDetails(
                        billNo
                );


        StringBuilder items =
                new StringBuilder();


        while (detailCursor.moveToNext()) {

            String dishName =
                    detailCursor.getString(
                            detailCursor.getColumnIndexOrThrow(
                                    "dish_name"
                            )
                    );


            double price =
                    detailCursor.getDouble(
                            detailCursor.getColumnIndexOrThrow(
                                    "price"
                            )
                    );


            int quantity =
                    detailCursor.getInt(
                            detailCursor.getColumnIndexOrThrow(
                                    "quantity"
                            )
                    );


            double amount =
                    detailCursor.getDouble(
                            detailCursor.getColumnIndexOrThrow(
                                    "amount"
                            )
                    );


            items.append(dishName);

            items.append("\n");

            items.append("  ₹");

            items.append(
                    String.format(
                            "%.2f",
                            price
                    )
            );

            items.append(" × ");

            items.append(quantity);

            items.append(" = ₹");

            items.append(
                    String.format(
                            "%.2f",
                            amount
                    )
            );

            items.append("\n\n");
        }


        detailCursor.close();


        // -----------------------------------------------------
        // DISPLAY ITEMS
        // -----------------------------------------------------

        if (items.length() == 0) {

            tvBillItems.setText(
                    "No items found."
            );

        } else {

            tvBillItems.setText(
                    items.toString()
            );
        }


        // -----------------------------------------------------
        // CREATE PRINTABLE BILL
        // -----------------------------------------------------

        createPrintableBill(
                billNo,
                items.toString()
        );
    }


    // =========================================================
    // CREATE PRINTABLE BILL TEXT
    // =========================================================

    private void createPrintableBill(
            int billNo,
            String items) {

        StringBuilder bill =
                new StringBuilder();


        bill.append(
                "================================\n"
        );

        bill.append(
                "       HOTEL MANAGEMENT\n"
        );

        bill.append(
                "             SYSTEM\n"
        );

        bill.append(
                "================================\n\n"
        );


        bill.append(
                "Bill No. : "
        );

        bill.append(
                billNo
        );

        bill.append("\n");


        bill.append(
                "Date     : "
        );

        bill.append(
                billDate
        );

        bill.append("\n\n");


        bill.append(
                "Customer : "
        );

        bill.append(
                customerName
        );

        bill.append("\n");


        bill.append(
                "Mobile   : "
        );

        bill.append(
                mobileNo
        );

        bill.append("\n");


        bill.append(
                "--------------------------------\n"
        );


        bill.append(
                "ITEMS\n"
        );


        bill.append(
                "--------------------------------\n"
        );


        bill.append(
                items
        );


        bill.append(
                "--------------------------------\n"
        );


        bill.append(
                "TOTAL    : ₹"
        );


        bill.append(
                String.format(
                        "%.2f",
                        totalBill
                )
        );


        bill.append("\n");


        bill.append(
                "================================\n"
        );


        bill.append(
                "          THANK YOU!\n"
        );


        bill.append(
                "         VISIT AGAIN\n"
        );


        bill.append(
                "================================\n"
        );


        printableBill =
                bill.toString();
    }


    // =========================================================
    // PRINT BILL
    // =========================================================

    private void printBill() {

        // Make sure bill exists
        if (printableBill == null ||
                printableBill.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Bill data is not available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================================
        // GET ANDROID PRINT MANAGER
        // =====================================================

        PrintManager printManager =
                (PrintManager)
                        getSystemService(
                                Context.PRINT_SERVICE
                        );


        // =====================================================
        // PRINT JOB NAME
        // =====================================================

        String jobName =
                "Hotel Bill " +
                        currentBillNo;


        // =====================================================
        // PRINT ATTRIBUTES
        // =====================================================

        PrintAttributes attributes =
                new PrintAttributes.Builder()
                        .setMediaSize(
                                PrintAttributes.MediaSize.ISO_A4
                        )
                        .setMinMargins(
                                PrintAttributes.Margins.NO_MARGINS
                        )
                        .build();


        // =====================================================
        // START PRINTING
        // =====================================================

        printManager.print(
                jobName,

                new BillPrintAdapter(
                        this,
                        printableBill
                ),

                attributes
        );
    }
}