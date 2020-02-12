package com.cliquet.gautier.go4lunch.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.cliquet.gautier.go4lunch.R;

public class WebsiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        String url = getIntent().getStringExtra("url");

        WebView webview;
        webview = findViewById(R.id.activity_website_webview);

        webview.loadUrl(url);
    }
}
