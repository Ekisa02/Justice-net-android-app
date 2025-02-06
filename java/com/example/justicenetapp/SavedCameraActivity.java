package com.example.justicenetapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.justicenetapp.ImageListAdapter;

import java.io.File;
import java.util.ArrayList;

public class SavedCameraActivity extends AppCompatActivity {

    private ListView listViewPhotos;
    private ArrayList<String> imagePaths;
    private ImageListAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        listViewPhotos = findViewById(R.id.listViewPhotos);

        imagePaths = getIntent().getStringArrayListExtra("imagePaths");

        if (imagePaths != null && !imagePaths.isEmpty()) {
            imageListAdapter = new ImageListAdapter(this, imagePaths);
            listViewPhotos.setAdapter(imageListAdapter);
        }
    }
}
