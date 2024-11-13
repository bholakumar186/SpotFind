package com.example.spotfind;

// Necessary imports for handling Gemini API and Futures
import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.FutureCallback;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiContentGenerator {

    private static final String MODEL_NAME = "gemini-1.5-flash"; // Gemini model name
    private final Executor executor;
    private final GenerativeModelFutures model;


    public GeminiContentGenerator() {
        // Initialize the Gemini model with the model name and API key
        GenerativeModel gm = new GenerativeModel(MODEL_NAME, BuildConfig.GEMINI_API_KEY);
        this.model = GenerativeModelFutures.from(gm);
        this.executor = Executors.newSingleThreadExecutor(); // Single-thread executor
    }

    public void generateStoryContent(String promptText) {
        // Create content with the input text prompt
        Content content = new Content.Builder().addText(promptText).build();

        // Send request to the model and get a ListenableFuture response
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        // Handle the response asynchronously
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // Get the result text from the response
                String resultText = result.getText();
                if (resultText != null) {
                    System.out.println("Response from Gemini: " + resultText);
                } else {
                    System.out.println("Received empty response from Gemini.");
                }
                // Optionally, you could update the UI with this result
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println("Error in Gemini API response:");
                t.printStackTrace();
                // Optionally, handle the error in the UI
            }
        }, executor);
    }
}

