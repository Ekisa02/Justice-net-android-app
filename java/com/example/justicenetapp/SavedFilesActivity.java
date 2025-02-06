package com.example.justicenetapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.content.FileProvider;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedFilesActivity extends AppCompatActivity {
    private ListView listViewFiles;
    private List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_files);

        listViewFiles = findViewById(R.id.listViewFiles);
        displayUploadedFiles();

        // Set an item click listener for ListView items
        listViewFiles.setOnItemClickListener((parent, view, position, id) -> {
            File selectedFile = fileList.get(position);
            showFileOptionsDialog(selectedFile);
        });
    }

    // Method to display uploaded files in the ListView
    private void displayUploadedFiles() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();

        if (files != null && files.length > 0) {
            fileList = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            for (File file : files) {
                fileList.add(file);
                fileNames.add(file.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
            listViewFiles.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No uploaded files found.", Toast.LENGTH_SHORT).show();
        }
    }

    // Show dialog with file operations (Open, Share, Delete)
    private void showFileOptionsDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(file.getName());
        String[] options = {"Open", "Share", "Delete"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openFile(file);
                        break;
                    case 1:
                        shareFile(file);
                        break;
                    case 2:
                        deleteFile(file);
                        break;
                }
            }
        });

        builder.show();
    }

    // Method to open the file with an appropriate app
    private void openFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getMimeType(file.getPath()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to share the file using an Intent
    private void shareFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getMimeType(file.getPath()));
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share File"));
    }

    // Method to delete the file
    private void deleteFile(File file) {
        boolean deleted = file.delete();
        if (deleted) {
            Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show();
            displayUploadedFiles(); // Refresh the list after deletion
        } else {
            Toast.makeText(this, "Failed to delete the file", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the MIME type of a file
    private String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        String mimeType = null;
        if (extension != null) {
            if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (extension.equalsIgnoreCase(".png")) {
                mimeType = "image/png";
            } else if (extension.equalsIgnoreCase(".mp4")) {
                mimeType = "video/mp4";
            } else if (extension.equalsIgnoreCase(".mp3")) {
                mimeType = "audio/mp3";
            } else {
                mimeType = "*/*"; // For unknown types
            }
        }
        return mimeType;
    }
}
