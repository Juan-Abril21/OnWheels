package com.example.onwheels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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

public class GetWheelsActivity extends AppCompatActivity {
    private Button previous_button;
    private CircularProgressButton btnUpdate;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_wheels);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        previous_button = findViewById(R.id.previous_button);
        btnUpdate = findViewById(R.id.btnUpdate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        previous_button.setOnClickListener(view -> {
            startActivity(new Intent(GetWheelsActivity.this, HomeActivity.class));
        });

        ArrayList<CardData> cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);

        loadData(cardList);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnUpdate.startAnimation();
                        loadData(cardList);
                    }
                }, 1);
            }
        });
    }

    private void loadData(ArrayList<CardData> cardList) {
        db.collection("wheels").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    cardList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String placa = doc.getString("placa");
                        String fecha = doc.getString("fecha");
                        String hora = doc.getString("hora");
                        String inicio = doc.getString("inicio");
                        String fin = doc.getString("fin");
                        String usuario = doc.getString("usuario");

                        CardData card = new CardData("Ruta de " + usuario, "Hora: " + hora,
                                "Placa: " + placa, "Desde: " + inicio + "\nHasta: " + fin, "Fecha: " + fecha);

                        cardList.add(card);
                    }
                    adapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnUpdate.revertAnimation();
                        }
                    }, 1000);
                }
            }
        });
    }
}