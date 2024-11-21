package com.example.onwheels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GetWheelsActivity extends AppCompatActivity {
    private Button previous_button;
    private CircularProgressButton btnUpdate;
    private FirebaseFirestore db;
    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_wheels);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        initializeViews();

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar botones
        setupButtons();

        // Cargar datos iniciales
        loadData();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        previous_button = findViewById(R.id.previous_button);
        btnUpdate = findViewById(R.id.btnUpdate);

        recyclerView.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        previous_button.setOnClickListener(view -> {
            startActivity(new Intent(GetWheelsActivity.this, HomeActivity.class));
            finish();
        });

        btnUpdate.setOnClickListener(view -> {
            btnUpdate.startAnimation();
            loadData();
        });
    }

    private void loadData() {
        db.collection("wheels")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            ArrayList<CardData> cardList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String documentId = doc.getId(); 
                                String placa = doc.getString("placa");
                                String fecha = doc.getString("fecha");
                                String hora = doc.getString("hora");
                                String inicio = doc.getString("inicio");
                                String fin = doc.getString("fin");
                                String usuario = doc.getString("usuario");

                                int cupos = 0;
                                try {
                                    Object cuposObj = doc.get("cupos");
                                    if (cuposObj != null) {
                                        cupos = Integer.parseInt(cuposObj.toString());
                                    }
                                } catch (NumberFormatException e) {
                                    Toast.makeText(this, "Error al parsear cupos para " + placa,
                                            Toast.LENGTH_SHORT).show();
                                }

                                CardData card = new CardData(
                                        documentId, // Pasar el ID
                                        "Ruta de " + usuario,
                                        "Hora: " + hora,
                                        "Placa: " + placa,
                                        "Desde: " + inicio + "\n\nHasta: " + fin,
                                        "Fecha: " + fecha,
                                        cupos
                                );
                                cardList.add(card);
                            }

                            adapter = new CardAdapter(cardList);
                            recyclerView.setAdapter(adapter);
                            updateUIVisibility(true);

                        } catch (Exception e) {
                            Toast.makeText(this, "Error al procesar los datos: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUIVisibility(false);
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar los datos: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        updateUIVisibility(false);
                    }

                    new Handler().postDelayed(() -> {
                        if (btnUpdate != null) {
                            btnUpdate.revertAnimation();
                        }
                    }, 1000);
                });
    }

    private void updateUIVisibility(boolean success) {
        if (success) {
            recyclerView.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar referencias
        adapter = null;
        db = null;
    }
}