package com.example.onwheels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReservationInfo extends AppCompatActivity {
    private CircularProgressButton aceptar_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

        String placa = getIntent().getStringExtra("placa");
        String fecha = getIntent().getStringExtra("fecha");
        String hora = getIntent().getStringExtra("hora");
        String inicio = getIntent().getStringExtra("inicio");
        String fin = getIntent().getStringExtra("fin");

        TextView placa_text_view = findViewById(R.id.placa_text_view);
        TextView fecha_text_view = findViewById(R.id.fecha_text_view);
        TextView hora_text_view = findViewById(R.id.hora_text_view);
        TextView punto_inicio_text_view = findViewById(R.id.punto_inicio_text_view);
        TextView punto_llegada_text_view = findViewById(R.id.punto_llegada_text_view);
        aceptar_button = findViewById(R.id.aceptar_button);

        placa_text_view.setText(placa);
        fecha_text_view.setText(fecha);
        hora_text_view.setText(hora);
        punto_inicio_text_view.setText(inicio);
        punto_llegada_text_view.setText(fin);
        Button backButton = findViewById(R.id.previous_button);
        backButton.setOnClickListener(v -> finish());

        aceptar_button.setOnClickListener(view -> finish());
    }

}
