package com.example.onwheels;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservationManager {
    private FirebaseFirestore db;
    private String userId;

    public ReservationManager(String userId) {
        this.db = FirebaseFirestore.getInstance();
        this.userId = userId;
    }

    public void addReservation(String wheelId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        // Actualizar el array de reservas en el documento del usuario
        db.collection("users")
                .document(userId)
                .update("reservations", FieldValue.arrayUnion(wheelId))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void removeReservation(String wheelId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        // Remover el wheelId del array de reservas
        db.collection("users")
                .document(userId)
                .update("reservations", FieldValue.arrayRemove(wheelId))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}