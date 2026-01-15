package com.myuang.demo;

public class Transaction {
    private String title;
    private String subtitle;
    private String amount;
    private String date;
    private int iconResId;
    private int amountColorResId;

    public Transaction(String title, String subtitle, String amount, String date, int iconResId, int amountColorResId) {
        this.title = title;
        this.subtitle = subtitle;
        this.amount = amount;
        this.date = date;
        this.iconResId = iconResId;
        this.amountColorResId = amountColorResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getAmountColorResId() {
        return amountColorResId;
    }
}
