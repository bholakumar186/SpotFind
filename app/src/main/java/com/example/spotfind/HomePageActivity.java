package com.example.spotfind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    ImageButton btnProfile, btnOrders, btnSupport, btnAbout;
    Button btnChoose, btnSearch;
    TextView tvLocation;
    private double chosenLatitude;
    private double chosenLongitude;
    private boolean isLocationSelected = false;

    private static final int MAPS_REQUEST_CODE = 1001;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        tvLocation= findViewById(R.id.tvLocation);
        btnProfile= findViewById(R.id.btnProfile);
        btnOrders= findViewById(R.id.btnOrders);
        btnSupport= findViewById(R.id.btnSupport);
        btnAbout= findViewById(R.id.btnAbout);
        btnChoose= findViewById(R.id.btnChoose);
        btnSearch= findViewById(R.id.btnSearch);
       // btnPark= findViewById(R.id.btnPark);



        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(HomePageActivity.this, MapsActivity.class);
                startActivityForResult(mapIntent, MAPS_REQUEST_CODE);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationSelected) {
                    LatLng chosenLocation = new LatLng(chosenLatitude, chosenLongitude);
                    double radiusInMeters = 500; // 0.5 KM radius
                    ParkingSpaceRepository repository = new ParkingSpaceRepository();
                    repository.getNearbyParkingSpaces(chosenLocation, radiusInMeters, new ParkingSpaceRepository.NearbyParkingCallback() {
                        @Override
                        public void onNearbyParkingFound(List<ParkingSpaceDatabase> nearbyParkingSpaces) {
                            Intent resultsIntent = new Intent(HomePageActivity.this, SearchResultsActivity.class);
                            resultsIntent.putParcelableArrayListExtra("parking_spaces", new ArrayList<>(nearbyParkingSpaces));
                            startActivity(resultsIntent);
                        }

                        @Override
                        public void onNoNearbyParkingFound() {
                            Toast.makeText(HomePageActivity.this, "No parking spaces found within 0.5 km radius", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(HomePageActivity.this, "Error retrieving parking spaces: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //searchAvailableParkingSpaces();
                } else {
                    Toast.makeText(HomePageActivity.this, "No location selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HomePageActivity.this, ProfileActivity.class);
                startActivity(i);
                //finish();
            }
        });

      btnOrders.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent i= new Intent(HomePageActivity.this, OrderActivity.class);
              startActivity(i);
              //finish();
          }
      });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HomePageActivity.this, SupportActivity.class);
                startActivity(i);
               // finish();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HomePageActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Retrieve latitude and longitude from the Intent
                double latitude = data.getDoubleExtra("selected_latitude", Double.NaN);
                double longitude = data.getDoubleExtra("selected_longitude", Double.NaN);

                if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
                    chosenLatitude = latitude;
                    chosenLongitude = longitude;
                    isLocationSelected = true;
                    tvLocation.setText(String.format("Lat: %s, Lng: %s", chosenLatitude, chosenLongitude));
                } else {
                    tvLocation.setText("No location selected");
                    isLocationSelected = false;
                }
            }
        }
    }

    private void searchAvailableParkingSpaces() {
        DatabaseReference parkingSpacesRef = FirebaseDatabase.getInstance().getReference("ParkingSpaces");
        parkingSpacesRef.orderByChild("isAvailable").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ParkingSpaceDatabase> availableSpaces = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ParkingSpaceDatabase space = snapshot.getValue(ParkingSpaceDatabase.class);
                    availableSpaces.add(space);
                }
                // Pass the availableSpaces to a new activity or display them on the HomePage UI
                // Example: Show in a new activity
                Intent intent = new Intent(HomePageActivity.this, SearchResultsActivity.class);
                intent.putParcelableArrayListExtra("available_spaces", new ArrayList<>(availableSpaces));
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomePageActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }




}