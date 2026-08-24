package com.example.hotelmanagementsystem;

public class Dish {

    private int dishNo;
    private String dishName;
    private double dishPrice;


    public Dish(
            int dishNo,
            String dishName,
            double dishPrice) {

        this.dishNo = dishNo;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
    }


    public int getDishNo() {
        return dishNo;
    }


    public String getDishName() {
        return dishName;
    }


    public double getDishPrice() {
        return dishPrice;
    }
}