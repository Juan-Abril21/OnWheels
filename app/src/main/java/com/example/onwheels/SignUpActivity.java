package com.example.onwheels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signUpEmail, signUpPassword;
    private CircularProgressButton signUpButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String user = signUpEmail.getText().toString().trim();
                        String pass = signUpPassword.getText().toString().trim();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        if (user.isEmpty()) {
                            signUpEmail.setError("Email is required");
                        } else if (!user.endsWith("@unisabana.edu.co")) {
                            signUpEmail.setError("Email must be a @unisabana.edu.co address");
                        } else if (pass.isEmpty()) {
                            signUpPassword.setError("Password is required");
                        } else {
                            signUpButton.startAnimation();
                            auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    } else {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                recreate();
                                            }
                                        }, 1);
                                        Toast.makeText(SignUpActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }, 1);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}

