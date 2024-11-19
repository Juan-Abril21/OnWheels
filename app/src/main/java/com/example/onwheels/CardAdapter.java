package com.example.onwheels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardData> cardList;

    public CardAdapter(List<CardData> cardList) {
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
        CardData data = cardList.get(position);
        holder.userText.setText(data.getUser());
        holder.horaText.setText(data.getHora());
        holder.placaText.setText(data.getPlaca());
        holder.rutaText.setText(data.getRuta());
        holder.fechaText.setText(data.getFecha());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView userText, horaText, placaText, rutaText, fechaText;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.userText);
            horaText = itemView.findViewById(R.id.horaText);
            placaText = itemView.findViewById(R.id.placaText);
            rutaText = itemView.findViewById(R.id.rutaText);
            fechaText = itemView.findViewById(R.id.fechaText);
        }
    }
}

