package com.example.justicenetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ReportedissueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportedissue);

        // Buttons for the provided links
        Button buttonLandCommission = findViewById(R.id.btnlandcommisin);
        Button buttonEacc = findViewById(R.id.btneacc);
        Button buttonKhrc = findViewById(R.id.btnkhrc);
        Button buttonKfs = findViewById(R.id.btn_kfs);
        Button buttonNema = findViewById(R.id.btnnema);
        TextView txtback = findViewById(R.id.txtarrowbackicon);


        // Handle clicks for each button to open the URL in WebViewActivity
        txtback.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this,ReportActivity.class)));
        buttonNema.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this, NemaActivity.class)));
        buttonLandCommission.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this, LandcommissionActivity.class)));
        buttonEacc.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this, EACCActivity.class)));
        buttonKfs.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this, KFSActivity.class)));
        buttonKhrc.setOnClickListener(v -> startActivity(new Intent(ReportedissueActivity.this, KHRCActivity.class)));

       // buttonLandCommission.setOnClickListener(v -> openUrlInWebView("https://www.landcommission.go.ke/"));
        //buttonEacc.setOnClickListener(v -> openUrlInWebView("https://www.eacc.go.ke/"));
       // buttonKhrc.setOnClickListener(v -> openUrlInWebView("https://www.khrc.or.ke/"));
       // buttonKfs.setOnClickListener(v -> openUrlInWebView("http://www.kenyaforestservice.org/"));
       // buttonNema.setOnClickListener(v -> openUrlInWebView("https://www.nema.go.ke/"));
    }

    private void openUrlInWebView(String url) {
        Intent intent = new Intent(ReportedissueActivity.this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
