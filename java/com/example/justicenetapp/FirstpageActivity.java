package com.example.justicenetapp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FirstpageActivity extends AppCompatActivity {

    CardView hometab,contactus,community,cardviewmain,cardviewaboutus,cardviewexit,cardviewsettings;
    TextView pictorial1Descriptiontxtwater,pictorial2Descriptiontxtecofriendly,pictorial3Descriptiontxteplore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firstpage);
  hometab = findViewById(R.id.hometab);
  contactus = findViewById(R.id.contactus);
  community = findViewById(R.id.community);
  cardviewmain =findViewById(R.id.cardviewmain);
  cardviewsettings =findViewById(R.id.cardviewsettings);
  cardviewexit = findViewById(R.id.cardviewexitapp);
  cardviewaboutus = findViewById(R.id.cardviewaboutus);
  pictorial1Descriptiontxtwater = findViewById(R.id.pictorial1Descriptiontxtwater);
  pictorial2Descriptiontxtecofriendly = findViewById(R.id.pictorial2Descriptiontxtecofriendly);
  pictorial3Descriptiontxteplore = findViewById(R.id.pictorial3Descriptiontxteplore);

        hometab.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, HomepageActivity.class)));
        contactus.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, contactActivity.class)));
        cardviewmain.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, ReportActivity.class)));
        cardviewaboutus.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, AboutusActivity.class)));
        cardviewexit.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, ExitActivity.class)));
        cardviewsettings.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this, SettingsActivity.class)));
        community.setOnClickListener(v -> startActivity(new Intent(FirstpageActivity.this,PartnersActivity .class)));

        pictorial1Descriptiontxtwater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wra.go.ke/"));
                startActivity(intent);

            }
        });
        pictorial3Descriptiontxteplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://bettergoods.org/how-to-be-eco-friendly/"));
                startActivity(intent);

            }
        });
        pictorial2Descriptiontxtecofriendly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.basf.com/us/en/who-we-are/change-for-climate?at_medium=display&at_campaign=COM_BAW_US_EN_Change-Image_TRA_Microsoft-Ads-Generic-Phrase-2024&at_creation=Search_Microsoft-Corporation_EN_Text-Ad_FY24-Generic-Bing-Change-Image&at_channel=Microsoft-Corporation&at_format=Text-Ad&at_variant=Change-Image_1x1_EN-Generic-Bing-Change-Image&utm_source=bing&utm_medium=cpc&utm_campaign=COM_BAW_US_EN_Change-Image_TRA_Microsoft-Ads_Generic-Phrase-2024&utm_term=Sustainable%20energy&utm_content=Search_Microsoft_Corp_EN_Text-Ad_FY24-Generic-Bing-Change-Image&gclid=CJD9h8jz9ogDFclYHQkdrbU0qA&gclsrc=ds"));
                startActivity(intent);

            }
        });
    }


}