package com.example.hotelmanagementsystem;

public class Bill {

    private int billNo;
    private String billDate;
    private String customerName;
    private String mobileNo;
    private double totalBill;

    public Bill(
            int billNo,
            String billDate,
            String customerName,
            String mobileNo,
            double totalBill) {

        this.billNo = billNo;
        this.billDate = billDate;
        this.customerName = customerName;
        this.mobileNo = mobileNo;
        this.totalBill = totalBill;
    }

    public int getBillNo() {
        return billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public double getTotalBill() {
        return totalBill;
    }
}