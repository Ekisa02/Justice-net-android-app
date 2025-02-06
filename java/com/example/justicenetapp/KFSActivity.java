package com.example.justicenetapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class KFSActivity extends AppCompatActivity {
    CardView cardviewPhoneCall, cardviewEmail, cardViewUpload, cardviewEmergencyNo, cardViewcamera, cardviewback;
    TextView txtnemaactivity;
    String[] counties = {"Nairobi"};
    String[] phoneNumbers = {"+254204054904"};
    String[] emergencyServices = {"Firefighters", "Police", "Ambulance", "Kenya Red Cross"};
    String[] emergencyNumbers = {"999", "999", "999", "1199"};
    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private final long maxFileSize = 15 * 1024 * 1024;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    // 15 MB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kfsactivity);
        txtnemaactivity = findViewById(R.id.txtnemaacivity);
        cardviewPhoneCall = findViewById(R.id.cardviewphonecall);
        cardviewEmail = findViewById(R.id.cardviewemail);
        cardviewEmergencyNo = findViewById(R.id.cardviewemergency);
        cardViewUpload = findViewById(R.id.cardviewupload);
        cardViewcamera = findViewById(R.id.cardviewcamera);
        cardviewback = findViewById(R.id.cardviewback);


        txtnemaactivity.setOnClickListener(v -> openUrlInWebView("http://www.kenyaforestservice.org/"));


        cardviewback.setOnClickListener(v -> startActivity(new Intent(KFSActivity.this,RecordActivity.class)));
        cardViewcamera.setOnClickListener(v -> startActivity(new Intent(KFSActivity.this,CameraActivity.class)));
        cardViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        // Set the click listener for the Emergency Number CardView
        cardviewEmergencyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmergencyServiceDialog();
            }
        });

        // Set the click listener for the Phone Call CardView
        cardviewPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountyDialog();
            }
        });

        // Set the click listener for the Email CardView
        cardviewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    // Open the file picker to select files like images, videos, or audio
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allows selecting any file type
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*", "audio/*"}); // Limit to specific file types
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData(); // Get the file URI
            try {
                // Check file size
                if (isFileSizeValid(fileUri)) {
                    // Show the dialog for upload or cancel
                    showUploadDialog();
                } else {
                    Toast.makeText(this, "File size exceeds 15 MB", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to process the file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Check if the file size is under 15 MB
    private boolean isFileSizeValid(Uri fileUri) throws Exception {
        Cursor returnCursor = getContentResolver().query(fileUri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long fileSize = returnCursor.getLong(sizeIndex);
        returnCursor.close();
        return fileSize <= maxFileSize;
    }

    // Show dialog to confirm upload or cancel
    private void showUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload File");
        builder.setMessage("Do you want to upload this file?");

        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFileLocally(fileUri);
                Toast.makeText(KFSActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                // Navigate to the file container page
                openFileContainer();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the dialog without uploading
            }
        });

        builder.show();
    }

    // Save the file locally (for demonstration purposes, it's saved in app-specific storage)
    private void saveFileLocally(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            File outputFile = new File(getFilesDir(), "uploaded_file_" + System.currentTimeMillis());
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigate to the page that contains the uploaded files (for display purposes)
    private void openFileContainer() {
        Intent intent = new Intent(this, SavedFilesActivity.class);
        startActivity(intent);
    }

    // Method to show a dialog with emergency services
    private void showEmergencyServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(KFSActivity.this);
        builder.setTitle("Select Emergency Service");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(KFSActivity.this, android.R.layout.simple_list_item_1, emergencyServices);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedService = emergencyServices[which];
                String emergencyNumber = emergencyNumbers[which];
                showCallDialog(selectedService, emergencyNumber);
            }
        });

        builder.show();
    }

    // Method to show a dialog with the option to call or cancel
    private void showCallDialog(String service, String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(KFSActivity.this);
        builder.setTitle(service + " Emergency Number");
        builder.setMessage("Would you like to call " + service + " at " + number + "?");

        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Method to show a dropdown list of counties
    private void showCountyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(KFSActivity.this);
        builder.setTitle("Select your County");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(KFSActivity.this, android.R.layout.simple_list_item_1, counties);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCounty = counties[which];
                String phoneNumber = phoneNumbers[which];
                showPhoneNumberDialog(selectedCounty, phoneNumber);
            }
        });

        builder.show();
    }

    // Method to show the phone number dialog
    private void showPhoneNumberDialog(String county, String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(KFSActivity.this);
        builder.setTitle(county + " Phone Number");
        builder.setMessage(phoneNumber);

        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Method to send an email
    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:info@kenyaforestservice.org"));
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(KFSActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void openUrlInWebView(String url) {
        Intent intent = new Intent(KFSActivity.this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
