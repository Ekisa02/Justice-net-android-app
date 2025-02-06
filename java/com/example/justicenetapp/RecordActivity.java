package com.example.justicenetapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

import firebase.com.protolitewrapper.BuildConfig;

public class RecordActivity extends AppCompatActivity {

    private Button recordAudioButton, stopAudioButton, playPauseAudioButton;
    private TextView recordingStatusTextView, txtshare;
    private SeekBar recordingWaveSeekBar, audioSeekBar;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPlaying = false;
    private boolean isRecording = false;
    private View linearlyout;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Request permissions
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        recordAudioButton = findViewById(R.id.record_audio_button);
        recordingWaveSeekBar = findViewById(R.id.recording_wave_seekbar);
        stopAudioButton = findViewById(R.id.stop_audio_button);
        playPauseAudioButton = findViewById(R.id.play_pause_audio_button);
        recordingStatusTextView = findViewById(R.id.recording_status_textview);
        audioSeekBar = findViewById(R.id.audio_seekbar);
        linearlyout = findViewById(R.id.linearlayout1);
        txtshare = findViewById(R.id.txtshare);

        // Set up sharing functionality
        txtshare.setOnClickListener(view -> shareAudioFile());

        recordAudioButton.setOnClickListener(view -> {
            startAudioRecording();
            recordingWaveSeekBar.setVisibility(View.VISIBLE);
            startRecordingWaveAnimation(); // Start wave animation based on amplitude
            recordAudioButton.setEnabled(false);
            stopAudioButton.setVisibility(View.VISIBLE);
            recordingStatusTextView.setText("Status: Recording...");
        });

        stopAudioButton.setOnClickListener(view -> {
            stopAudioRecording();
            stopRecordingWaveAnimation();
            stopAudioButton.setVisibility(View.GONE);
            recordAudioButton.setEnabled(true);
            linearlyout.setVisibility(View.VISIBLE);
            playPauseAudioButton.setVisibility(View.VISIBLE);
            recordingStatusTextView.setText("Status: Recording stopped");
        });

        playPauseAudioButton.setOnClickListener(view -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                playAudio();
            }
        });

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void shareAudioFile() {
        Log.d("RecordActivity", "Sharing audio file: " + audioFilePath);

        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            Log.e("RecordActivity", "Audio file does not exist");
            return; // Exit if the file does not exist
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*"); // Set the MIME type for audio files
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Recorded Audio");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attached audio recording.");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                "info@kenyaforestservice.org",
                "admin@khrc.or.ke",
                "info@landcommission.go.ke",
                "report@integrity.go.ke"
        });

        Uri uri = FileProvider.getUriForFile(this, "com.example.justicenetapp.fileprovider", audioFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to access the URI

        try {
            startActivity(Intent.createChooser(shareIntent, "Share Audio Recording"));
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("RecordActivity", "No email client installed.");
        }
    }

    private void startAudioRecording() {
        File audioFile = new File(getExternalFilesDir(null), "recorded_audio.3gp");
        audioFilePath = audioFile.getAbsolutePath();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }

    private void playAudio() {
        if (audioFilePath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPauseAudioButton.setText("Pause");
                isPlaying = true;

                audioSeekBar.setVisibility(View.VISIBLE);
                audioSeekBar.setMax(mediaPlayer.getDuration());

                // Update SeekBar during playback
                handler.postDelayed(updateSeekBar, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnCompletionListener(mp -> {
                playPauseAudioButton.setText("Play");
                isPlaying = false;
                handler.removeCallbacks(updateSeekBar);
            });
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseAudioButton.setText("Play");
            isPlaying = false;
            handler.removeCallbacks(updateSeekBar);
        }
    }

    private void startRecordingWaveAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecording && mediaRecorder != null) {
                    int maxAmplitude = mediaRecorder.getMaxAmplitude(); // Get current amplitude level
                    int waveLevel = scaleAmplitudeToSeekBar(maxAmplitude); // Scale amplitude to SeekBar range (0 to 100)
                    recordingWaveSeekBar.setProgress(waveLevel);
                    handler.postDelayed(this, 100); // Update every 100ms
                }
            }
        }, 100);
    }

    private void stopRecordingWaveAnimation() {
        recordingWaveSeekBar.setProgress(0); // Reset progress
        recordingWaveSeekBar.setVisibility(View.GONE); // Hide SeekBar after recording stops
        handler.removeCallbacksAndMessages(null); // Stop animation
    }

    // Scale amplitude (0-32767) to SeekBar range (0-100)
    private int scaleAmplitudeToSeekBar(int amplitude) {
        return (int) ((amplitude / 32767.0) * 100);
    }

    // Update SeekBar to reflect audio playback progress
    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPlaying) {
                audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 100); // Update every 100ms
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }
}
