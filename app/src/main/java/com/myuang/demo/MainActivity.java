package com.myuang.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Cards RecyclerView ---
        RecyclerView cardsRecyclerView = findViewById(R.id.cards_recycler_view);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("123 123 123 123 123", "Zoo Kow We", "06/26"));
        cards.add(new Card("987 654 321 098 765", "Zoo Kow We", "12/28"));
        
        CardAdapter cardAdapter = new CardAdapter(cards);
        cardsRecyclerView.setAdapter(cardAdapter);

        // --- Transactions RecyclerView ---
        RecyclerView transactionsRecyclerView = findViewById(R.id.transactions_recycler_view);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        List<Transaction> transactions = new ArrayList<>();
        // Using existing drawables as placeholders where specific ones are missing
        transactions.add(new Transaction("Food", "Nasi Goreng", "- Rp 15,000", "Oktober 15, 2025", R.drawable.ic_nav_wallet, R.color.expense_red));
        transactions.add(new Transaction("Bank Transfer", "Salary for March", "+ Rp 15,000,000", "Oktober 15, 2025", R.drawable.ic_bank, R.color.income_green));
        transactions.add(new Transaction("Coffeee Shop", "Coffee with Sarah", "- Rp 30,000", "Oktober 11, 2025", R.drawable.ic_nav_home, R.color.expense_red));
        
        TransactionAdapter transactionAdapter = new TransactionAdapter(transactions);
        transactionsRecyclerView.setAdapter(transactionAdapter);

        // --- FAB Click Listener ---
        ImageView fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }
        });
    }
}
