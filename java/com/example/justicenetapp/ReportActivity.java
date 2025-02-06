package com.example.justicenetapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends AppCompatActivity {

    TextView btnreport, txtbackarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        btnreport = findViewById(R.id.btnreport);
        txtbackarrow = findViewById(R.id.txtarrowbackicon);
        btnreport.setOnClickListener(v -> startActivity(new Intent(ReportActivity.this, ReportedissueActivity.class)));
        txtbackarrow.setOnClickListener(v -> startActivity(new Intent(ReportActivity.this, FirstpageActivity.class)));


    }
}