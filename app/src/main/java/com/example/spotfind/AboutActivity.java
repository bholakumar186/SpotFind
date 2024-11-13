package com.example.spotfind;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setTitle("About SpotFind");

        TextView textViewAppName = findViewById(R.id.textView_app_name);
        TextView textViewAppDescription = findViewById(R.id.textView_app_description);
        TextView textViewDeveloperHeader = findViewById(R.id.textView_developer_header);
        TextView textViewDeveloperName = findViewById(R.id.textView_developer_name);
        TextView textViewDeveloperEmail = findViewById(R.id.textView_developer_email);
        TextView textViewDeveloperLinkedIn = findViewById(R.id.textView_developer_linkedin);

        textViewAppName.setText("SpotFind");
        textViewAppDescription.setText("SpotFind is a comprehensive solution for urban parking management. Leveraging mobile technology and geolocation, SpotFind allows users to find and reserve parking spaces with ease. It streamlines the parking experience by providing real-time availability and location-based search, making urban commuting more convenient and efficient.");

        textViewDeveloperHeader.setText("Developer Details");
        textViewDeveloperName.setText("Name: Alina Ali");
        textViewDeveloperEmail.setText("Email: alinaali0119@gmail.com");
        textViewDeveloperLinkedIn.setText("LinkedIn: https://www.linkedin.com/in/alina-ali-7930aa296/");

    }
}