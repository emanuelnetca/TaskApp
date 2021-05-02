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

public class MainActivity extends AppCompatActivity {

    private TextView signupTextMainActivity;
    private EditText emailLoginMainActivity;
    private EditText passwordLoginMainActivity;
    private Button loginButtonMainActivity;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_layout);

        signupTextMainActivity = findViewById(R.id.signupTextMainActivity);
        emailLoginMainActivity = findViewById(R.id.emailLoginMainActivity);
        passwordLoginMainActivity = findViewById(R.id.passwordLoginMainActivity);
        loginButtonMainActivity = findViewById(R.id.loginButtonMainActivity);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        loginButtonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLoginMainActivity.getText().toString().trim();
                String password = passwordLoginMainActivity.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailLoginMainActivity.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordLoginMainActivity.setError("Required field");
                    return;
                }

                progressDialog.setMessage("Processing...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            progressDialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login unsuccessful!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        });

        signupTextMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
}