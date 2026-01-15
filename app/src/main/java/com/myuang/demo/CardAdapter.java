package com.myuang.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;

    public CardAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.cardNumber.setText(card.getNumber());
        holder.cardHolderName.setText(card.getHolderName());
        holder.cardExpiry.setText(card.getExpiryDate());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber, cardHolderName, cardExpiry;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardHolderName = itemView.findViewById(R.id.card_holder_name);
            cardExpiry = itemView.findViewById(R.id.card_expiry_date);
        }
    }
}
