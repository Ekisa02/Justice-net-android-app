package com.example.justicenetapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PartnersActivity extends AppCompatActivity {
CardView cardnema,cardkws,cardwra,cardkfs;
TextView txtback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_partners);
        cardkfs = findViewById(R.id.cardkws);
        txtback = findViewById(R.id.txtarrowbackicon);
        cardkws = findViewById(R.id.cardkfs);
        cardnema = findViewById(R.id.cardnema);
        cardwra = findViewById(R.id.cardwra);

        txtback.setOnClickListener(v -> startActivity(new Intent(PartnersActivity.this, FirstpageActivity.class)));
        cardnema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.nema.go.ke"));
                startActivity(intent);

            }
        });

        cardwra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.wra.go.ke"));
                startActivity(intent);

            }
        });
        cardkws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.kws.go.ke"));
                startActivity(intent);

            }
        });
        cardkfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.kenyaforestservice.org/"));
                startActivity(intent);

            }
        });


    }
}