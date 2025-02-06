package com.example.justicenetapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private ImageView imageViewCamera;
    private Button btnTakePicture;
    private Button btnSharePicture;
    private String currentPhotoPath;
    private final ArrayList<String> imagePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageViewCamera = findViewById(R.id.imageViewcamera);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSharePicture = findViewById(R.id.btnSharePicture); // Button for sharing

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        btnSharePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

        // Initially hide the share button
        btnSharePicture.setVisibility(View.GONE);
    }

    private void checkPermissions() {
        Log.d("CameraActivity", "Checking camera permissions");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Log.d("CameraActivity", "Permissions granted. Dispatching camera intent.");
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call to super method
        Log.d("CameraActivity", "onRequestPermissionsResult called");
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CameraActivity", "Camera permission granted. Dispatching camera intent.");
                dispatchTakePictureIntent();
            } else {
                Log.e("CameraActivity", "Camera permission denied");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Log.d("CameraActivity", "dispatchTakePictureIntent called");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("CameraActivity", "Photo file created: " + photoFile.getAbsolutePath());
            } catch (IOException ex) {
                Log.e("CameraActivity", "Error creating image file: ", ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.justicenetapp.fileprovider", // Ensure this matches your manifest
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.e("CameraActivity", "Photo file is null.");
            }
        } else {
            Log.e("CameraActivity", "No application available to take pictures.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CameraActivity", "onActivityResult called");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("CameraActivity", "Image capture successful");
            File file = new File(currentPhotoPath);
            imageViewCamera.setImageURI(Uri.fromFile(file));
            imagePaths.add(currentPhotoPath);
            savePhotoToGallery();

            // Show the share button after capturing the image
            btnSharePicture.setVisibility(View.VISIBLE);
        } else {
            Log.e("CameraActivity", "Image capture failed or cancelled");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d("CameraActivity", "Image file created at: " + currentPhotoPath);
        return image;
    }

    private void savePhotoToGallery() {
        Log.d("CameraActivity", "Saving photo to gallery");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void shareImage() {
        Log.d("CameraActivity", "Sharing image: " + currentPhotoPath);
        File imageFile = new File(currentPhotoPath);
        Uri imageUri = FileProvider.getUriForFile(this,
                "com.example.justicenetapp.fileprovider", // Ensure this matches your manifest
                imageFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                "info@kenyaforestservice.org",
                "admin@khrc.or.ke",
                "info@landcommission.go.ke",
                "report@integrity.go.ke"
        });
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Important to grant read permission
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
