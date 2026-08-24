package com.example.hotelmanagementsystem;

public class CartItem {

    private int dishNo;
    private String dishName;
    private double price;
    private int quantity;
    private double amount;


    public CartItem(
            int dishNo,
            String dishName,
            double price,
            int quantity) {

        this.dishNo = dishNo;
        this.dishName = dishName;
        this.price = price;
        this.quantity = quantity;

        // Calculate amount
        this.amount =
                price * quantity;
    }


    public int getDishNo() {
        return dishNo;
    }


    public String getDishName() {
        return dishName;
    }


    public double getPrice() {
        return price;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getAmount() {
        return amount;
    }
}