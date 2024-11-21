package com.example.onwheels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<CardData> cardList;
    private Context context;

    public CardAdapter(List<CardData> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        try {
            CardData data = cardList.get(position);
            holder.userText.setText(data.getUser());
            holder.horaText.setText(data.getHora());
            holder.placaText.setText(data.getPlaca());
            holder.rutaText.setText(data.getRuta());
            holder.fechaText.setText(data.getFecha());
            holder.cuposText.setText(String.format("Cupos disponibles: %d", data.getCupos()));

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, InfoGetWheelsActivity.class);

                        // Pasar el ID del documento
                        intent.putExtra("documentId", data.getId());
                        intent.putExtra("placa", data.getPlaca().replace("Placa: ", ""));
                        intent.putExtra("fecha", data.getFecha().replace("Fecha: ", ""));
                        intent.putExtra("hora", data.getHora().replace("Hora: ", ""));

                        String[] rutaParts = data.getRuta().split("\n+");
                        String inicio = rutaParts[0].replace("Desde: ", "");
                        String fin = rutaParts[1].replace("Hasta: ", "");

                        intent.putExtra("inicio", inicio);
                        intent.putExtra("fin", fin);
                        intent.putExtra("cupos", data.getCupos());

                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error al abrir los detalles: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Error al cargar la tarjeta: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView userText, horaText, placaText, rutaText, fechaText, cuposText;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            userText = itemView.findViewById(R.id.userText);
            horaText = itemView.findViewById(R.id.horaText);
            placaText = itemView.findViewById(R.id.placaText);
            rutaText = itemView.findViewById(R.id.rutaText);
            fechaText = itemView.findViewById(R.id.fechaText);
            cuposText = itemView.findViewById(R.id.cuposText);
        }
    }
}