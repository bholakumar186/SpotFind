package com.example.spotfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SupportActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFAQ;
    private FAQAdapter faqAdapter;
    private List<FAQItem> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewFAQ = findViewById(R.id.recyclerView_faq);
        recyclerViewFAQ.setLayoutManager(new LinearLayoutManager(this));

        // Initialize FAQ list and adapter
        faqList = new ArrayList<>();
        populateFAQList();
        faqAdapter = new FAQAdapter(faqList);
        recyclerViewFAQ.setAdapter(faqAdapter);

        // Add a button for the chatbot later
        Button buttonChatbot = findViewById(R.id.button_chatbot);
        buttonChatbot.setOnClickListener(view -> {
            Intent intent = new Intent(SupportActivity.this, ChatBotActivity.class);
            startActivity(intent);// Implement chatbot functionality later
        });
    }

    private void populateFAQList() {
        // Sample FAQ items
        faqList.add(new FAQItem("What is SpotFind?", "SpotFind is an app to help you find parking spaces."));
        faqList.add(new FAQItem("How do I book a parking space?", "You can book a parking space through the app by selecting a location."));
        faqList.add(new FAQItem("Is there a fee for booking?", "Yes, there might be a nominal fee for booking a parking space."));
        faqList.add(new FAQItem("Can I cancel my booking?", "Yes, you can cancel your booking before the time of reservation."));
        faqList.add(new FAQItem("How do I contact support?", "You can contact support through the app's support section."));
        faqList.add(new FAQItem("Is my data secure?", "Yes, we ensure your data is stored securely."));
        faqList.add(new FAQItem("What if I forget my password?", "You can reset your password through the login screen."));
        faqList.add(new FAQItem("Can I edit my profile?", "Yes, you can edit your profile in the settings section."));
        faqList.add(new FAQItem("Do I need to create an account?", "Yes, you need to create an account to use the app."));
        faqList.add(new FAQItem("How do I provide feedback?", "You can provide feedback through the feedback option in the app."));
    }
}
