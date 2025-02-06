package com.example.justicenetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AboutusActivity extends AppCompatActivity {
    TextView txtarrowbackicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aboutus);

        txtarrowbackicon = findViewById(R.id.txtarrowbackicon);
        txtarrowbackicon.setOnClickListener(v ->startActivity(new Intent(AboutusActivity.this, FirstpageActivity.class)));

    }
}