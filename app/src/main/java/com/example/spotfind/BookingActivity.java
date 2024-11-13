package com.example.spotfind;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private TextView tvOwnerName, tvLocation, tvSize, tvTime, tvCamera, tvGuard;
    private EditText etUserName, etUserContact; // New input fields for user details
    private Button btnBook;
    private ParkingSpaceDatabase selectedParkingSpace;

    // Initialize Firebase Auth
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvLocation = findViewById(R.id.tvLocation);
        tvSize = findViewById(R.id.tvSize);
        tvTime = findViewById(R.id.tvTime);
        tvCamera = findViewById(R.id.tvCamera);
        tvGuard = findViewById(R.id.tvGuard);
        btnBook = findViewById(R.id.btnBook);

        // Initialize the input fields
        etUserName = findViewById(R.id.etUserName); // Ensure this EditText is in your XML
        etUserContact = findViewById(R.id.etUserContact); // Ensure this EditText is in your XML

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Create the notification channel
        NotificationHelper.createNotificationChannel(this);

        // Retrieve the selected parking space data from the Intent
        selectedParkingSpace = getIntent().getParcelableExtra("selected_parking_space");

        if (selectedParkingSpace != null) {
            tvOwnerName.setText("Owner: " + selectedParkingSpace.getOwnerName());
            tvLocation.setText("Location: " + selectedParkingSpace.getLocation());
            tvSize.setText("Size: " + selectedParkingSpace.getParkingSize());
            tvTime.setText("Available: " + (selectedParkingSpace.isisAvailable()? "Yes" : "No"));
            tvCamera.setText("Camera Availability: " + (selectedParkingSpace.isCameraAvailability() ? "Yes" : "No"));
            tvGuard.setText("Guard Availability: " + (selectedParkingSpace.isGuardAvailability() ? "Yes" : "No"));
        }

        btnBook.setOnClickListener(v -> {
            String userName = etUserName.getText().toString().trim();
            String userContact = etUserContact.getText().toString().trim();

            if (userName.isEmpty() || userContact.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                bookParkingSpace(userName, userContact);
                //updateParkingSpaceAvailability(userName, selectedParkingSpace.isisAvailable());
            }
        });

    }

    private void bookParkingSpace(String userName, String userContact) {
        // Check if the parking space is available before proceeding
        if (!selectedParkingSpace.isisAvailable()) {
            // If the space is not available, show a failure message
            Toast.makeText(this, "Parking space is already reserved.", Toast.LENGTH_SHORT).show();
            return; // Exit early to prevent booking
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");
        String bookingId = databaseReference.push().getKey();

        // Get the current user ID
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;

        // Create a map to store booking details
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("bookingId", bookingId);
        bookingDetails.put("ownerName", selectedParkingSpace.getOwnerName());
        bookingDetails.put("ownerContact", selectedParkingSpace.getPhoneNumber());
        bookingDetails.put("userId", userId); // Add userId to booking details
        bookingDetails.put("userName", userName);
        bookingDetails.put("userContact", userContact);
        bookingDetails.put("parkingLocation", selectedParkingSpace.getLocation());
        bookingDetails.put("parkingSize", selectedParkingSpace.getParkingSize());
        bookingDetails.put("availabilityTime", selectedParkingSpace.isisAvailable());
        // Get the current time")

        // Save booking information to Firebase
        databaseReference.child(bookingId).setValue(bookingDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Booking Successful", Toast.LENGTH_SHORT).show();
                       String parkingId = selectedParkingSpace.getParkingId();
                        updateParkingSpaceAvailability(parkingId, false);
                        String bookingMessage = "Your booking was successful!";
                        showLocalNotification(bookingMessage);
                        // Navigate to Home Activity
                        Intent intent = new Intent(BookingActivity.this, HomePageActivity.class);
                        // Optionally, you can clear the activity stack so the user can't return to the booking screen
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//    private void updateParkingSpaceAvailability(String ownerName, boolean isAvailable) {
//        DatabaseReference parkingSpaceReference = FirebaseDatabase.getInstance().getReference("ParkingSpaces");
//
//        Query query = parkingSpaceReference.orderByChild("ownerName").equalTo(ownerName);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String ownerName = snapshot.getKey(); // Get parking space ID
//                        // Now you can update the 'isAvailable' for this specific parking space
//                        snapshot.getRef().child("isAvailable").setValue(isAvailable);
//                        String message = isAvailable ? "Parking space is now available." : "Parking space is now reserved.";
//                        Toast.makeText(BookingActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(BookingActivity.this, "No parking space found in this location.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(BookingActivity.this, "Failed to query parking spaces.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
private void updateParkingSpaceAvailability(String parkingId, boolean isAvailable) {
    DatabaseReference parkingSpaceReference = FirebaseDatabase.getInstance().getReference("ParkingSpaces");

    // Query the parking space based on the parkingId
    Query query = parkingSpaceReference.orderByChild("parkingId").equalTo(parkingId);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Access the parking space data
                    snapshot.getRef().child("isAvailable").setValue(isAvailable); // Update the 'isAvailable' field
                    Log.d(TAG, "Parking space availability updated successfully.");

                    // Optionally, show a toast or notification here to confirm the update
                    Toast.makeText(BookingActivity.this, "Parking space is now reserved.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BookingActivity.this, "No parking space found with that ID.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(BookingActivity.this, "Failed to query parking spaces.", Toast.LENGTH_SHORT).show();
        }
    });
}

    private void showLocalNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "default_channel";
        NotificationChannel channel = new NotificationChannel(channelId, "Booking Notifications", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle("Booking Confirmation")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);
    }

}
