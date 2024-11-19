package com.example.onwheels;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoCreationActivity extends AppCompatActivity {
    private Button accept_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_creation);
        accept_button = findViewById(R.id.accept_button);
        accept_button.setOnClickListener(view -> {
            startActivity(new Intent(InfoCreationActivity.this, HomeActivity.class));
        });

        Intent intent = getIntent();
        String placa = intent.getStringExtra("placa");
        String fecha = intent.getStringExtra("fecha");
        String hora = intent.getStringExtra("hora");
        String puntoInicio = intent.getStringExtra("puntoInicio");
        String puntoLlegada = intent.getStringExtra("puntoLlegada");

        TextView placaTextView = findViewById(R.id.placa_text_view);
        TextView fechaTextView = findViewById(R.id.fecha_text_view);
        TextView horaTextView = findViewById(R.id.hora_text_view);
        TextView puntoInicioTextView = findViewById(R.id.punto_inicio_text_view);
        TextView puntoLlegadaTextView = findViewById(R.id.punto_llegada_text_view);

        placaTextView.setText(placa);
        fechaTextView.setText(fecha);
        horaTextView.setText(hora);
        puntoInicioTextView.setText(puntoInicio);
        puntoLlegadaTextView.setText(puntoLlegada);
    }
}
