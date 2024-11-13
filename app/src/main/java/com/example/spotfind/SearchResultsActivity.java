package com.example.spotfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView rvParkingSpaces;
    private ParkingSpaceAdapter adapter;
    private TextView tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        // Adjust padding for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvParkingSpaces = findViewById(R.id.rvParkingSpaces);
        tvNoResults = findViewById(R.id.tvNoResults);
        rvParkingSpaces.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the list of parking spaces from the intent
        ArrayList<ParkingSpaceDatabase> parkingSpaces = getIntent().getParcelableArrayListExtra("parking_spaces");

        // Set up the RecyclerView with the adapter
        if (parkingSpaces != null && !parkingSpaces.isEmpty()) {
            // Initialize adapter with item click listener to handle navigation
            adapter = new ParkingSpaceAdapter(parkingSpaces, parkingSpace -> {
                // Navigate to BookingActivity on item click
                Intent intent = new Intent(SearchResultsActivity.this, BookingActivity.class);
                intent.putExtra("selected_parking_space", parkingSpace);
                startActivity(intent);
            });
            rvParkingSpaces.setAdapter(adapter);
            showResults(true);  // Display the results
        } else {
            showResults(false);  // No results to display
        }
    }

    private void showResults(boolean hasResults) {
        if (hasResults) {

            tvNoResults.setVisibility(View.GONE);
            rvParkingSpaces.setVisibility(View.VISIBLE);
        } else {
            tvNoResults.setVisibility(View.VISIBLE);
            rvParkingSpaces.setVisibility(View.GONE);
        }
    }
}
