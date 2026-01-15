package com.myuang.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlagAdapter extends RecyclerView.Adapter<FlagAdapter.FlagViewHolder> {

    private int[] flagDrawables;

    public FlagAdapter(int[] flagDrawables) {
        this.flagDrawables = flagDrawables;
    }

    @NonNull
    @Override
    public FlagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flag_item, parent, false);
        return new FlagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
        holder.flagImage.setImageResource(flagDrawables[position % flagDrawables.length]);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class FlagViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImage;

        public FlagViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.flag_image);
        }
    }
}
