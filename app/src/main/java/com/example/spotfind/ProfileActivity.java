package com.example.spotfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText tietName, tietEmail, tietPhone, tietAddress, tietPin;
    private TextView tvLocation;
    private Button btnMap, btnSave;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private double selectedLatitude, selectedLongitude;

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tietName = findViewById(R.id.tietName);
        tietEmail = findViewById(R.id.tietEmail);
        tietPhone = findViewById(R.id.tietPhone);
        tietAddress = findViewById(R.id.tietAddress);
        tietPin = findViewById(R.id.tietPin);
        tvLocation = findViewById(R.id.tvLocation);
        btnMap = findViewById(R.id.btnMap);
        btnSave = findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the user is not logged in
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        loadUserProfile();

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to open map activity or dialog to select location
                // Replace with your own implementation of map location selection
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class); // Example MapActivity
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });

    }
    private void loadUserProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tietName.setText(user.name);
                    tietEmail.setText(user.email);
                    tietPhone.setText(user.phone);
                    tietAddress.setText(user.address);
                    tietPin.setText(user.pin);
                    // Assume location is part of the address or a separate field if needed
                    if (user.latitude != 0 && user.longitude != 0) {
                        tvLocation.setText("Location: " + user.latitude + ", " + user.longitude);
                    } else {
                        tvLocation.setText("Location not set");
                    }// Display address or other location info
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = tietName.getText().toString();
        String email = tietEmail.getText().toString();
        String phone = tietPhone.getText().toString();
        String address = tietAddress.getText().toString();
        String pin = tietPin.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email, phone, address, pin);
        user.latitude = selectedLatitude;
        user.longitude = selectedLongitude;

        databaseReference.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK && data!= null) {
            // Retrieve location data from the map activity
            selectedLatitude = data.getDoubleExtra("selected_latitude", 0);
            selectedLongitude = data.getDoubleExtra("selected_longitude", 0);// Example, ensure "location" is set correctly in MapActivity
            if (selectedLatitude != 0 && selectedLongitude != 0) {
                tvLocation.setText("Selected Location: " + selectedLatitude + ", " + selectedLongitude);
            } else {
                Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

}