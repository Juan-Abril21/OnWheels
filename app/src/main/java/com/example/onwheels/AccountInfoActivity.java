package com.example.onwheels;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountInfoActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView username_text_view, email_text_view;
    private RecyclerView reservationsRecyclerView;
    private List<ReservationData> reservationList;
    private ReservationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        Button previous_button = findViewById(R.id.previous_button);
        username_text_view = findViewById(R.id.username_text_view);
        email_text_view = findViewById(R.id.email_text_view);
        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        setupRecyclerView();
        previous_button.setOnClickListener(view -> finish());
        loadUserData();
    }

    private void setupRecyclerView() {
        reservationList = new ArrayList<>();
        adapter = new ReservationsAdapter(reservationList);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationsRecyclerView.setAdapter(adapter);
    }

    private void loadUserData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            String email = currentUser.getEmail();

                            if (username != null) {
                                username_text_view.setText(username);
                            }
                            if (email != null) {
                                email_text_view.setText(email);
                            }
                            List<String> reservations = (List<String>) documentSnapshot.get("reservations");
                            if (reservations != null && !reservations.isEmpty()) {
                                loadReservationsData(reservations);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AccountInfoActivity.this,
                                "Error al cargar datos: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No se encontró usuario activo", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadReservationsData(List<String> reservationIds) {
        for (String wheelId : reservationIds) {
            db.collection("wheels").document(wheelId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            ReservationData cardData = new ReservationData(
                                    documentSnapshot.getId(),
                                    documentSnapshot.getString("usuario"),
                                    documentSnapshot.getString("hora"),
                                    documentSnapshot.getString("placa"),
                                    String.format("Desde: %s\nHasta: %s",
                                            documentSnapshot.getString("inicio"),
                                            documentSnapshot.getString("fin")),
                                    documentSnapshot.getString("fecha")
                            );
                            reservationList.add(cardData);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AccountInfoActivity.this,
                                "Error al cargar reservación: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }
}