package com.example.onwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    private Button previous_button;
    private MaterialButton create_wheels_button, get_wheels_button;
    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        previous_button = findViewById(R.id.previous_button);
        get_wheels_button = findViewById(R.id.get_wheels_button);
        create_wheels_button = findViewById(R.id.create_wheels_button);

        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(view -> {
            String usuario = SessionManager.getInstance(HomeActivity.this).getUsername();
            startActivity(new Intent(HomeActivity.this, AccountInfoActivity.class));
        });
        create_wheels_button.setOnClickListener(view -> {
            String usuario = SessionManager.getInstance(HomeActivity.this).getUsername();
            startActivity(new Intent(HomeActivity.this, CreateWheelsActivity.class));
        });
        get_wheels_button.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, GetWheelsActivity.class));
        });
        previous_button.setOnClickListener(view -> showLogoutConfirmationDialog());

    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesion")
                .setMessage("¿Estas seguro que quieres cerrar sesion?")
                .setPositiveButton("Si", (dialog, which) -> {
                    SessionManager.getInstance(HomeActivity.this).clearSession();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}