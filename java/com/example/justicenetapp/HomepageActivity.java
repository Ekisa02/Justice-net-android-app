package com.example.justicenetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class HomepageActivity extends AppCompatActivity {
Button buttonhpcontinue;
TextView txtarrowbackicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        buttonhpcontinue = findViewById(R.id.btnhpcontinue);
        txtarrowbackicon = findViewById(R.id.txtarrowbackicon);

        buttonhpcontinue.setOnClickListener(v ->startActivity(new Intent(HomepageActivity.this, FirstpageActivity.class)));
        txtarrowbackicon.setOnClickListener(v ->startActivity(new Intent(HomepageActivity.this, loginActivity.class)));



    }
}