package com.example.spotfind;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView bookingRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;

    private FirebaseAuth auth;
    private DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize RecyclerView and other views
        bookingRecyclerView = findViewById(R.id.bookingRecyclerView);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList);
        bookingRecyclerView.setAdapter(bookingAdapter);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String currentUserId = user.getUid();
            Log.d("OrderActivity", "Current User ID: " + currentUserId); // Log current user ID for debugging

            // Get reference to Bookings node in Firebase
            bookingRef = FirebaseDatabase.getInstance().getReference("Bookings");

            // Query the database to get bookings for the current user
            bookingRef.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    bookingList.clear(); // Clear the list before adding new data

                    if (snapshot.exists()) {
                        Log.d("OrderActivity", "Bookings found for this user: " + snapshot.getChildrenCount());
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Booking booking = dataSnapshot.getValue(Booking.class);
                            Log.d("OrderActivity", "Booking: " + booking.getUserName() + ", Location: " + booking.getLocation());
                            if (booking != null) {
                                bookingList.add(booking); // Add each booking to the list
                            }
                        }
                        bookingAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    } else {
                        Log.e("OrderActivity", "No bookings found for this user.");
                        Toast.makeText(OrderActivity.this, "No bookings found for this user.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("OrderActivity", "Database Error: " + error.getMessage());
                    Toast.makeText(OrderActivity.this, "Failed to retrieve bookings.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("OrderActivity", "User not authenticated.");
            Toast.makeText(this, "Please log in to view your bookings.", Toast.LENGTH_SHORT).show();
        }
    }
}
