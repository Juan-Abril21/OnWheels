package com.example.onwheels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder> {
    private List<ReservationData> cardList;
    private Context context;

    public ReservationsAdapter(List<ReservationData> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public ReservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_card, parent, false);
        return new ReservationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationsViewHolder holder, int position) {
        try {
            ReservationData data = cardList.get(position);
            holder.userText.setText(data.getUser());
            holder.horaText.setText(data.getHora());
            holder.placaText.setText(data.getPlaca());
            holder.rutaText.setText(data.getRuta());
            holder.fechaText.setText(data.getFecha());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, ReservationInfo.class);

                        intent.putExtra("placa", data.getPlaca().replace("Placa: ", ""));
                        intent.putExtra("fecha", data.getFecha().replace("Fecha: ", ""));
                        intent.putExtra("hora", data.getHora().replace("Hora: ", ""));

                        String[] rutaParts = data.getRuta().split("\n+");
                        String inicio = rutaParts[0].replace("Desde: ", "");
                        String fin = rutaParts[1].replace("Hasta: ", "");

                        intent.putExtra("inicio", inicio);
                        intent.putExtra("fin", fin);

                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class ReservationsViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView userText, horaText, placaText, rutaText, fechaText, cuposText;

        public ReservationsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            userText = itemView.findViewById(R.id.userText);
            horaText = itemView.findViewById(R.id.horaText);
            placaText = itemView.findViewById(R.id.placaText);
            rutaText = itemView.findViewById(R.id.rutaText);
            fechaText = itemView.findViewById(R.id.fechaText);
        }
    }
}

