package com.example.hotelmanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // =====================================================
    // DATABASE INFORMATION
    // =====================================================

    private static final String DATABASE_NAME =
            "HotelManagement.db";

    // Version 4 = billing tables added
    private static final int DATABASE_VERSION = 4;


    // =====================================================
    // TABLE NAMES
    // =====================================================

    private static final String TABLE_USERS =
            "users";

    private static final String TABLE_DISHES =
            "dishes";

    private static final String TABLE_BILLS =
            "bills";

    private static final String TABLE_BILL_DETAILS =
            "bill_details";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =====================================================
    // CREATE DATABASE
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // -------------------------------------------------
        // USERS TABLE
        // -------------------------------------------------

        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "email TEXT, " +
                        "password TEXT NOT NULL" +
                        ")";

        db.execSQL(createUsersTable);


        // -------------------------------------------------
        // DISHES TABLE
        // -------------------------------------------------

        String createDishesTable =
                "CREATE TABLE " + TABLE_DISHES + " (" +
                        "dish_no INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "dish_name TEXT NOT NULL, " +
                        "dish_price REAL NOT NULL" +
                        ")";

        db.execSQL(createDishesTable);


        // -------------------------------------------------
        // BILLS TABLE
        // -------------------------------------------------

        String createBillsTable =
                "CREATE TABLE " + TABLE_BILLS + " (" +
                        "bill_no INTEGER PRIMARY KEY, " +
                        "bill_date TEXT NOT NULL, " +
                        "customer_name TEXT NOT NULL, " +
                        "mobile_no TEXT NOT NULL, " +
                        "total_bill REAL NOT NULL" +
                        ")";

        db.execSQL(createBillsTable);


        // -------------------------------------------------
        // BILL DETAILS TABLE
        // -------------------------------------------------

        String createBillDetailsTable =
                "CREATE TABLE " +
                        TABLE_BILL_DETAILS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "bill_no INTEGER NOT NULL, " +
                        "dish_name TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "quantity INTEGER NOT NULL, " +
                        "amount REAL NOT NULL" +
                        ")";

        db.execSQL(createBillDetailsTable);


        // -------------------------------------------------
        // DEFAULT ADMIN ACCOUNT
        // -------------------------------------------------

        ContentValues values =
                new ContentValues();

        values.put(
                "username",
                "admin"
        );

        values.put(
                "email",
                "admin@hotel.com"
        );

        values.put(
                "password",
                "123"
        );

        db.insert(
                TABLE_USERS,
                null,
                values
        );
    }


    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        // -------------------------------------------------
        // VERSION 2
        // Add dishes table
        // -------------------------------------------------

        if (oldVersion < 2) {

            String createDishesTable =
                    "CREATE TABLE IF NOT EXISTS dishes (" +
                            "dish_no INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "dish_name TEXT NOT NULL, " +
                            "dish_price REAL NOT NULL" +
                            ")";

            db.execSQL(createDishesTable);
        }


        // -------------------------------------------------
        // VERSION 3
        // Change admin password
        // admin123 -> 123
        // -------------------------------------------------

        if (oldVersion < 3) {

            ContentValues values =
                    new ContentValues();

            values.put(
                    "password",
                    "123"
            );

            db.update(
                    TABLE_USERS,
                    values,
                    "username = ?",
                    new String[]{
                            "admin"
                    }
            );
        }


        // -------------------------------------------------
        // VERSION 4
        // Add billing tables
        // -------------------------------------------------

        if (oldVersion < 4) {

            // Bills table
            String createBillsTable =
                    "CREATE TABLE IF NOT EXISTS bills (" +
                            "bill_no INTEGER PRIMARY KEY, " +
                            "bill_date TEXT NOT NULL, " +
                            "customer_name TEXT NOT NULL, " +
                            "mobile_no TEXT NOT NULL, " +
                            "total_bill REAL NOT NULL" +
                            ")";

            db.execSQL(createBillsTable);


            // Bill details table
            String createBillDetailsTable =
                    "CREATE TABLE IF NOT EXISTS bill_details (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "bill_no INTEGER NOT NULL, " +
                            "dish_name TEXT NOT NULL, " +
                            "price REAL NOT NULL, " +
                            "quantity INTEGER NOT NULL, " +
                            "amount REAL NOT NULL" +
                            ")";

            db.execSQL(createBillDetailsTable);
        }
    }


    // =====================================================
    // LOGIN
    // =====================================================

    public boolean checkLogin(
            String username,
            String password) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_USERS +
                                " WHERE username = ? " +
                                "AND password = ?",

                        new String[]{
                                username,
                                password
                        }
                );

        boolean result =
                cursor.getCount() > 0;

        cursor.close();

        return result;
    }


    // =====================================================
    // ADD DISH
    // Returns TRUE if successful
    // =====================================================

    public boolean addDish(
            String dishName,
            double dishPrice) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "dish_name",
                dishName
        );

        values.put(
                "dish_price",
                dishPrice
        );

        long result =
                db.insert(
                        TABLE_DISHES,
                        null,
                        values
                );

        // -1 means insertion failed
        return result != -1;
    }


    // =====================================================
    // UPDATE DISH
    // Returns TRUE if successful
    // =====================================================

    public boolean updateDish(
            int dishNo,
            String dishName,
            double dishPrice) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "dish_name",
                dishName
        );

        values.put(
                "dish_price",
                dishPrice
        );

        int result =
                db.update(
                        TABLE_DISHES,
                        values,
                        "dish_no = ?",
                        new String[]{
                                String.valueOf(dishNo)
                        }
                );

        // More than 0 rows updated = success
        return result > 0;
    }


    // =====================================================
    // DELETE DISH
    // Returns TRUE if successful
    // =====================================================

    public boolean deleteDish(
            int dishNo) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_DISHES,
                        "dish_no = ?",
                        new String[]{
                                String.valueOf(dishNo)
                        }
                );

        // More than 0 rows deleted = success
        return result > 0;
    }


    // =====================================================
    // GET ALL DISHES
    // =====================================================

    public Cursor getAllDishes() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " +
                        TABLE_DISHES +
                        " ORDER BY dish_no DESC",
                null
        );
    }


    // =====================================================
    // SEARCH DISHES
    // =====================================================

    public Cursor searchDishes(
            String searchText) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " +
                        TABLE_DISHES +
                        " WHERE dish_name LIKE ? " +
                        "ORDER BY dish_name",

                new String[]{
                        "%" + searchText + "%"
                }
        );
    }


    // =====================================================
    // GET NEXT BILL NUMBER
    // =====================================================

    public int getNextBillNumber() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT MAX(bill_no) " +
                                "FROM " +
                                TABLE_BILLS,
                        null
                );

        int nextBillNumber = 1;

        if (cursor.moveToFirst()) {

            // Check if there is already a bill
            if (!cursor.isNull(0)) {

                nextBillNumber =
                        cursor.getInt(0) + 1;
            }
        }

        cursor.close();

        return nextBillNumber;
    }


    // =====================================================
    // SAVE COMPLETE BILL
    // =====================================================

    public boolean saveBill(
            int billNo,
            String billDate,
            String customerName,
            String mobileNo,
            double totalBill,
            ArrayList<CartItem> cartList) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        // Start transaction
        db.beginTransaction();

        try {

            // -------------------------------------------------
            // SAVE MAIN BILL
            // -------------------------------------------------

            ContentValues billValues =
                    new ContentValues();

            billValues.put(
                    "bill_no",
                    billNo
            );

            billValues.put(
                    "bill_date",
                    billDate
            );

            billValues.put(
                    "customer_name",
                    customerName
            );

            billValues.put(
                    "mobile_no",
                    mobileNo
            );

            billValues.put(
                    "total_bill",
                    totalBill
            );

            long billResult =
                    db.insert(
                            TABLE_BILLS,
                            null,
                            billValues
                    );


            // Main bill failed
            if (billResult == -1) {

                return false;
            }


            // -------------------------------------------------
            // SAVE EACH CART ITEM
            // -------------------------------------------------

            for (CartItem item : cartList) {

                ContentValues detailValues =
                        new ContentValues();

                detailValues.put(
                        "bill_no",
                        billNo
                );

                detailValues.put(
                        "dish_name",
                        item.getDishName()
                );

                detailValues.put(
                        "price",
                        item.getPrice()
                );

                detailValues.put(
                        "quantity",
                        item.getQuantity()
                );

                detailValues.put(
                        "amount",
                        item.getAmount()
                );


                long detailResult =
                        db.insert(
                                TABLE_BILL_DETAILS,
                                null,
                                detailValues
                        );


                // Item failed
                if (detailResult == -1) {

                    return false;
                }
            }


            // Everything was saved
            db.setTransactionSuccessful();

            return true;

        } finally {

            // Finish transaction
            db.endTransaction();
        }
    }

    // =====================================================
    // GET ALL SAVED BILLS
    // =====================================================

    public Cursor getAllBills() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM bills " +
                        "ORDER BY bill_no DESC",
                null
        );
    }

    // =====================================================
    // GET DETAILS OF ONE BILL
    // =====================================================

    public Cursor getBillDetails(int billNo) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM bill_details " +
                        "WHERE bill_no = ? " +
                        "ORDER BY id ASC",

                new String[]{
                        String.valueOf(billNo)
                }
        );
    }

    // =====================================================
// DELETE BILL
// Deletes bill and all its items
// =====================================================

    public boolean deleteBill(int billNo) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        // Start transaction so both deletes happen together
        db.beginTransaction();

        try {

            // Delete bill items first
            db.delete(
                    "bill_details",
                    "bill_no = ?",
                    new String[]{
                            String.valueOf(billNo)
                    }
            );

            // Delete main bill
            int deletedRows =
                    db.delete(
                            "bills",
                            "bill_no = ?",
                            new String[]{
                                    String.valueOf(billNo)
                            }
                    );

            // Everything succeeded
            db.setTransactionSuccessful();

            return deletedRows > 0;

        } finally {

            // Finish transaction
            db.endTransaction();
        }
    }
}