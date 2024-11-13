package com.example.spotfind;



import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotfind.ChatAdapter;
import com.example.spotfind.ChatMessage;
import com.example.spotfind.R;
import com.google.ai.client.generativeai.GenerativeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private EditText etMessageInput;
    private Button btnSendMessage;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private GeminiContentGenerator geminiContentGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_bot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        geminiContentGenerator = new GeminiContentGenerator();
        String prompt = "Write a story about a magic backpack.";
        geminiContentGenerator.generateStoryContent(prompt);

        GenerativeModel gm = new GenerativeModel(
                /* modelName */ "gemini-1.5-flash",
                /* apiKey */ BuildConfig.GEMINI_API_KEY
        );
        Log.d("Gemini API Key", BuildConfig.GEMINI_API_KEY);
        // Initialize the chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setAdapter(chatAdapter);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = etMessageInput.getText().toString();
                if (!userMessage.isEmpty()) {
                    sendMessageToChatBot(userMessage);
                    etMessageInput.setText("");
                }
            }
        });

    }
    private void sendMessageToChatBot(String userMessage) {
        // Display user's message in chat
        chatMessages.add(new ChatMessage(userMessage, true));
        chatAdapter.notifyDataSetChanged();

        Log.d("ChatBotActivity", "User message: " + userMessage);

        // Use Retrofit to call Gemini Chatbot API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ChatResponse> call = apiService.getChatResponse(new ChatRequest(userMessage));
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String botResponse = response.body().getMessage();
                    chatMessages.add(new ChatMessage(botResponse, false));
                    chatAdapter.notifyDataSetChanged();
                    recyclerViewChat.smoothScrollToPosition(chatMessages.size() - 1);
                } else {
                    Toast.makeText(ChatBotActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                }
                Log.d("ChatBotActivity", "Response: " + response.body());

                Log.e("ChatBotActivity", "Error: " + response.code() + " " + response.message());

            }
            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(ChatBotActivity.this, "Failed to connect to chatbot", Toast.LENGTH_SHORT).show();
            }
        });
    }
}