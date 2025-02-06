package com.example.justicenetapp;

import static java.lang.System.*;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    FirebaseFirestore db;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Get URL passed from the previous activity
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
            saveUrlToFirestore(url); // Save URL and timestamp to Firebase
        }
    }

    private void saveUrlToFirestore(String url) {
        Map<String, Object> urlData = new HashMap<>();
        urlData.put("url", url);
        urlData.put("timestamp", Timestamp.now());

        db.collection("AccessedLinks")
                .add(urlData)
                .addOnSuccessListener(documentReference -> {
                    out.println("URL saved with timestamp.");
                })
                .addOnFailureListener(e -> {
                    err.println("Error saving URL: " + e.getMessage());
                });
    }
}
