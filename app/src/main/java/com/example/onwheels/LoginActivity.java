package com.example.onwheels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signUpRedirectText;
    private CircularProgressButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String email = loginEmail.getText().toString().trim();
                        String pass = loginPassword.getText().toString().trim();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            String usuario = email.substring(0, email.indexOf('@'));
                            if (!pass.isEmpty()) {
                                loginButton.startAnimation();
                                auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        SessionManager.getInstance(LoginActivity.this).setUsername(usuario);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                recreate();
                                            }
                                        }, 1);
                                        Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                loginPassword.setError("Password is required");
                            }
                        } else if (email.isEmpty()) {
                            loginEmail.setError("Email is required");
                        } else {
                            loginEmail.setError("Enter a valid email");
                        }
                    }
                }, 1);
            }
        });

        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
