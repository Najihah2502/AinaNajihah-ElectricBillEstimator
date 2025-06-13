package com.example.najihah_electricbillestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About Developer");

        TextView textLink = findViewById(R.id.textLink);
        textLink.setOnClickListener(v -> {
            // Replace this with your actual GitHub repo URL
            String url = "https://github.com/Najihah2502/AinaNajihah-ElectricBillEstimator";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
}
