package com.myuang.demo;

public class Card {
    private String number;
    private String holderName;
    private String expiryDate;
    private int backgroundResId; // To handle different card backgrounds if needed

    public Card(String number, String holderName, String expiryDate) {
        this.number = number;
        this.holderName = holderName;
        this.expiryDate = expiryDate;
    }

    public String getNumber() {
        return number;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
