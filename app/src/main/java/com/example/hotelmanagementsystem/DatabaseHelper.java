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

        // =================================================
        // USERS TABLE
        // =================================================

        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "email TEXT, " +
                        "password TEXT NOT NULL" +
                        ")";

        db.execSQL(createUsersTable);


        // =================================================
        // DISHES TABLE
        // =================================================

        String createDishesTable =
                "CREATE TABLE " + TABLE_DISHES + " (" +
                        "dish_no INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "dish_name TEXT NOT NULL, " +
                        "dish_price REAL NOT NULL" +
                        ")";

        db.execSQL(createDishesTable);


        // =================================================
        // BILLS TABLE
        // =================================================

        String createBillsTable =
                "CREATE TABLE " + TABLE_BILLS + " (" +
                        "bill_no INTEGER PRIMARY KEY, " +
                        "bill_date TEXT NOT NULL, " +
                        "customer_name TEXT NOT NULL, " +
                        "mobile_no TEXT NOT NULL, " +
                        "total_bill REAL NOT NULL" +
                        ")";

        db.execSQL(createBillsTable);


        // =================================================
        // BILL DETAILS TABLE
        // =================================================

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


        // =================================================
        // DEFAULT ADMIN ACCOUNT
        // =================================================

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

        // =================================================
        // VERSION 2
        // Add dishes table
        // =================================================

        if (oldVersion < 2) {

            String createDishesTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_DISHES + " (" +
                            "dish_no INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "dish_name TEXT NOT NULL, " +
                            "dish_price REAL NOT NULL" +
                            ")";

            db.execSQL(createDishesTable);
        }


        // =================================================
        // VERSION 3
        // Change admin password
        // admin123 -> 123
        // =================================================

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


        // =================================================
        // VERSION 4
        // Add billing tables
        // =================================================

        if (oldVersion < 4) {

            // -------------------------------------------------
            // Bills table
            // -------------------------------------------------

            String createBillsTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_BILLS + " (" +
                            "bill_no INTEGER PRIMARY KEY, " +
                            "bill_date TEXT NOT NULL, " +
                            "customer_name TEXT NOT NULL, " +
                            "mobile_no TEXT NOT NULL, " +
                            "total_bill REAL NOT NULL" +
                            ")";

            db.execSQL(createBillsTable);


            // -------------------------------------------------
            // Bill details table
            // -------------------------------------------------

            String createBillDetailsTable =
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_BILL_DETAILS + " (" +
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
    // REGISTER USER
    // =====================================================

    public boolean registerUser(
            String username,
            String email,
            String password) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "username",
                username
        );

        values.put(
                "email",
                email
        );

        values.put(
                "password",
                password
        );

        long result =
                db.insert(
                        TABLE_USERS,
                        null,
                        values
                );

        return result != -1;
    }


    // =====================================================
    // CHECK USERNAME
    // =====================================================

    public boolean checkUsernameExists(
            String username) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id FROM " +
                                TABLE_USERS +
                                " WHERE username = ?",

                        new String[]{
                                username
                        }
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        return exists;
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

        db.beginTransaction();

        try {

            // =================================================
            // SAVE MAIN BILL
            // =================================================

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

            if (billResult == -1) {
                return false;
            }


            // =================================================
            // SAVE CART ITEMS
            // =================================================

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

                if (detailResult == -1) {
                    return false;
                }
            }


            // =================================================
            // COMPLETE
            // =================================================

            db.setTransactionSuccessful();

            return true;

        } finally {

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
                "SELECT * FROM " +
                        TABLE_BILLS +
                        " ORDER BY bill_no DESC",
                null
        );
    }


    // =====================================================
    // SEARCH BILLS
    //
    // Searches:
    // 1. Customer name
    // 2. Bill number
    // 3. Mobile number
    // 4. Bill date
    // 5. Total amount
    // 6. Dish name inside bill
    // =====================================================

    public Cursor searchBills(
            String searchText) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT DISTINCT b.* " +
                        "FROM " +
                        TABLE_BILLS + " b " +

                        "LEFT JOIN " +
                        TABLE_BILL_DETAILS + " bd " +

                        "ON b.bill_no = bd.bill_no " +

                        "WHERE " +

                        "CAST(b.bill_no AS TEXT) LIKE ? " +

                        "OR b.customer_name LIKE ? " +

                        "OR b.mobile_no LIKE ? " +

                        "OR b.bill_date LIKE ? " +

                        "OR CAST(b.total_bill AS TEXT) LIKE ? " +

                        "OR bd.dish_name LIKE ? " +

                        "ORDER BY b.bill_no DESC",

                new String[]{
                        "%" + searchText + "%",
                        "%" + searchText + "%",
                        "%" + searchText + "%",
                        "%" + searchText + "%",
                        "%" + searchText + "%",
                        "%" + searchText + "%"
                }
        );
    }


    // =====================================================
    // GET DETAILS OF ONE BILL
    // =====================================================

    public Cursor getBillDetails(
            int billNo) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM " +
                        TABLE_BILL_DETAILS +

                        " WHERE bill_no = ? " +

                        "ORDER BY id ASC",

                new String[]{
                        String.valueOf(billNo)
                }
        );
    }


    // =====================================================
    // DELETE BILL
    //
    // Deletes:
    // 1. All bill items
    // 2. Main bill
    //
    // Both operations happen inside
    // one transaction.
    // =====================================================

    public boolean deleteBill(
            int billNo) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.beginTransaction();

        try {

            // -------------------------------------------------
            // Delete bill items
            // -------------------------------------------------

            db.delete(
                    TABLE_BILL_DETAILS,
                    "bill_no = ?",
                    new String[]{
                            String.valueOf(billNo)
                    }
            );


            // -------------------------------------------------
            // Delete main bill
            // -------------------------------------------------

            int deletedRows =
                    db.delete(
                            TABLE_BILLS,
                            "bill_no = ?",
                            new String[]{
                                    String.valueOf(billNo)
                            }
                    );


            // -------------------------------------------------
            // Success
            // -------------------------------------------------

            db.setTransactionSuccessful();

            return deletedRows > 0;

        } finally {

            db.endTransaction();
        }
    }
}