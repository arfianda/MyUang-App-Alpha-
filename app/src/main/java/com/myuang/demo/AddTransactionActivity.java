package com.myuang.demo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView tvAmount;
    private StringBuilder currentAmount = new StringBuilder("0");
    private boolean isExpense = false; // Default to Income to match design

    private TextView btnIncome;
    private TextView btnExpense;
    private TextView tvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        tvAmount = findViewById(R.id.tv_amount);
        btnIncome = findViewById(R.id.btn_income);
        btnExpense = findViewById(R.id.btn_expense);
        tvCategory = findViewById(R.id.tv_category);
        
        ImageView btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> finish());

        // Toggle Buttons
        btnIncome.setOnClickListener(v -> updateTransactionType(false));
        btnExpense.setOnClickListener(v -> updateTransactionType(true));
        
        // Initialize UI
        updateTransactionType(false);
    }

    private void updateTransactionType(boolean isExpenseObj) {
        this.isExpense = isExpenseObj;
        int activeColor = Color.parseColor("#448AFF"); // Blue
        int inactiveColor = Color.parseColor("#E3F2FD"); // Light Blue
        int white = Color.WHITE;

        if (!isExpense) {
            // Income Selected
            btnIncome.setBackgroundColor(activeColor);
            btnIncome.setTextColor(white);
            
            btnExpense.setBackgroundColor(inactiveColor);
            btnExpense.setTextColor(activeColor);
            
            tvCategory.setBackgroundTintList(ColorStateList.valueOf(activeColor));
        } else {
            // Expense Selected
            btnIncome.setBackgroundColor(inactiveColor);
            btnIncome.setTextColor(activeColor);
            
            btnExpense.setBackgroundColor(activeColor);
            btnExpense.setTextColor(white);
            
            tvCategory.setBackgroundTintList(ColorStateList.valueOf(activeColor)); // Keeping blue for now, change to red if desired
        }
    }
    
    public void onKeypadClick(View view) {
        if (view instanceof TextView) {
            String value = ((TextView) view).getText().toString();
            if (value.equals(".")) {
                if (!currentAmount.toString().contains(".")) {
                    currentAmount.append(".");
                }
            } else {
                if (currentAmount.toString().equals("0")) {
                    currentAmount = new StringBuilder(value);
                } else {
                    currentAmount.append(value);
                }
            }
        } else if (view instanceof ImageView) {
             // Backspace
             if (currentAmount.length() > 0) {
                 currentAmount.deleteCharAt(currentAmount.length() - 1);
                 if (currentAmount.length() == 0) {
                     currentAmount.append("0");
                 }
             }
        }
        updateAmountDisplay();
    }

    private void updateAmountDisplay() {
        tvAmount.setText(currentAmount.toString());
    }
}
