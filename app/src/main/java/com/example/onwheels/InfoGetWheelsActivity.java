package com.example.onwheels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoGetWheelsActivity extends AppCompatActivity {
    private CircularProgressButton reservar_button;
    private FirebaseFirestore db;
    private ReservationManager reservationManager;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_get_wheels);
        db = FirebaseFirestore.getInstance();

        String placa = getIntent().getStringExtra("placa");
        String fecha = getIntent().getStringExtra("fecha");
        String hora = getIntent().getStringExtra("hora");
        String inicio = getIntent().getStringExtra("inicio");
        String fin = getIntent().getStringExtra("fin");
        int cupos = getIntent().getIntExtra("cupos", 0);
        String documentId = getIntent().getStringExtra("documentId");

        TextView placa_text_view = findViewById(R.id.placa_text_view);
        TextView fecha_text_view = findViewById(R.id.fecha_text_view);
        TextView hora_text_view = findViewById(R.id.hora_text_view);
        TextView punto_inicio_text_view = findViewById(R.id.punto_inicio_text_view);
        TextView punto_llegada_text_view = findViewById(R.id.punto_llegada_text_view);
        TextView cupos_text_view = findViewById(R.id.cupos_text_view);
        reservar_button = findViewById(R.id.reservar_button);

        placa_text_view.setText(placa);
        fecha_text_view.setText(fecha);
        hora_text_view.setText(hora);
        punto_inicio_text_view.setText(inicio);
        punto_llegada_text_view.setText(fin);
        cupos_text_view.setText(String.valueOf(cupos));
        Button backButton = findViewById(R.id.previous_button);
        backButton.setOnClickListener(v -> finish());

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        reservationManager = new ReservationManager(userId);

        reservar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reservar_button.startAnimation();
                        if (documentId != null) {
                            db.collection("wheels").document(documentId).update("cupos", cupos - 1)
                                    .addOnSuccessListener(aVoid -> {
                                        db.collection("wheels").document(documentId).get()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            long updatedCupos = document.getLong("cupos");
                                                            if (updatedCupos == 0) {
                                                                db.collection("wheels").document(documentId).delete()
                                                                        .addOnSuccessListener(aVoid1 -> {
                                                                            // Agrega reserva al perfil del usuario
                                                                            reserveWheel(documentId);
                                                                            Toast.makeText(InfoGetWheelsActivity.this, "Reserva exitosa y registro eliminado", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(InfoGetWheelsActivity.this, HomeActivity.class));
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Toast.makeText(InfoGetWheelsActivity.this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show();
                                                                            reservar_button.revertAnimation();
                                                                        });
                                                            } else {
                                                                reserveWheel(documentId);
                                                                Toast.makeText(InfoGetWheelsActivity.this, "Reserva exitosa", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(InfoGetWheelsActivity.this, HomeActivity.class));
                                                            }
                                                        }
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(InfoGetWheelsActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                                        reservar_button.revertAnimation();
                                    });
                        } else {
                            Toast.makeText(InfoGetWheelsActivity.this, "Error al reservar, ID no disponible", Toast.LENGTH_SHORT).show();
                            reservar_button.revertAnimation();
                        }
                    }
                }, 1);
            }
        });

    }

    private void reserveWheel(String wheelId) {
        reservationManager.addReservation(
                wheelId,
                aVoid -> {
                    Toast.makeText(this, "Reserva exitosa", Toast.LENGTH_SHORT).show();
                    // Actualizar UI o navegar a otra pantalla
                },
                e -> Toast.makeText(this, "Error al reservar: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}