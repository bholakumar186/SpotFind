package com.example.spotfind;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText tietName, tietEmail, tietPhone, tietPassword, tietConfirm, tietAddress, tietPin;
    Button btnSignUp, btnLogin;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Bind XML elements
        tietName = findViewById(R.id.tietName);
        tietEmail = findViewById(R.id.tietEmail);
        tietPhone = findViewById(R.id.tietPhone);
        tietPassword = findViewById(R.id.tietPassword);
        tietConfirm = findViewById(R.id.tietConfirm);
        tietAddress = findViewById(R.id.tietAddress);
        tietPin = findViewById(R.id.tietPin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLP);

        // Sign Up button click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Go to Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Register user method moved outside of onCreate
    private void registerUser() {
        String name = tietName.getText().toString().trim();
        String email = tietEmail.getText().toString().trim();
        String phone = tietPhone.getText().toString().trim();
        String password = tietPassword.getText().toString().trim();
        String confirmPassword = tietConfirm.getText().toString().trim();
        String address = tietAddress.getText().toString().trim();
        String pin = tietPin.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name)) {
            tietName.setError("Name is required.");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            tietEmail.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            tietPhone.setError("Phone number is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tietPassword.setError("Password is required.");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            tietConfirm.setError("Passwords do not match.");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            tietAddress.setError("Address is required.");
            return;
        }
        if (TextUtils.isEmpty(pin)) {
            tietPin.setError("Pin is required.");
            return;
        }

        // Firebase Authentication for email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Save user data to Firebase Realtime Database
                            User newUser = new User(name, email, phone, address, pin);
                            mDatabase.child(user.getUid()).setValue(newUser)
                                    .addOnCompleteListener(databaseTask -> {
                                        if (databaseTask.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Database Error: " + databaseTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        Toast.makeText(SignUpActivity.this, "Error: " + errorCode, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
