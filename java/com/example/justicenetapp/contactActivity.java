package com.example.justicenetapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class contactActivity extends AppCompatActivity {
    CardView  facebook,whatsapp;
    TextView  arrowback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

       whatsapp= findViewById(R.id.whatsapp);
       facebook = findViewById(R.id.Facebook);
        arrowback= findViewById(R.id.txtarrowbackicon);

        arrowback.setOnClickListener(v ->startActivity(new Intent(contactActivity.this, FirstpageActivity.class)));

        facebook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse("https://www.facebook.com/foresters"));
               startActivity(intent);

           }
       });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://chat.whatsapp.com/H2FcDm3zkoq9iCgIRde1yh"));
                startActivity(intent);

            }
        });

    }



}