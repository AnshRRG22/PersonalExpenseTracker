package com.example.personalexpensetracker.database;

public class MonthlyTotal {
    private String month;
    private double total;

    public MonthlyTotal(String month, double total) {
        this.month = month;
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public double getTotal() {
        return total;
    }
}