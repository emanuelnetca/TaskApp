package com.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailRegistrationActivity;
    private EditText passwordRegistrationActivity;
    private Button registrationButtonRegistrationActivity;
    private TextView loginTextRegistrationActivity;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_activity_layout);

        emailRegistrationActivity = findViewById(R.id.emailRegistrationActivity);
        passwordRegistrationActivity = findViewById(R.id.passwordRegistrationActivity);
        registrationButtonRegistrationActivity = findViewById(R.id.registrationButtonRegistrationActivity);
        loginTextRegistrationActivity = findViewById(R.id.loginTextRegistrationActivity);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        loginTextRegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        registrationButtonRegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRegistrationActivity.getText().toString().trim();
                String password = passwordRegistrationActivity.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailRegistrationActivity.setError("Required field!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordRegistrationActivity.setError("Required field!");
                    return;
                }

                progressDialog.setMessage("Processing...");

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unsuccesful",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        });
    }
}