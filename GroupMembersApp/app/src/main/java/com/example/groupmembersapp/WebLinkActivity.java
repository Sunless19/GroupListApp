package com.example.groupmembersapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebLinkActivity extends AppCompatActivity {

    private TextView textMemberName, textUrl;
    private Button buttonBack, buttonRefresh;
    private WebView webView;
    private String url;
    private String memberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Web Content");
        }

        // Initialize UI components
        textMemberName = findViewById(R.id.textMemberName);
        textUrl = findViewById(R.id.textUrl);
        webView = findViewById(R.id.webView);
        buttonBack = findViewById(R.id.buttonBack);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        // Configure WebView
        setupWebView();

        // Get data from intent
        if (getIntent().hasExtra("url") && getIntent().hasExtra("member_name")) {
            url = getIntent().getStringExtra("url");
            memberName = getIntent().getStringExtra("member_name");

            textMemberName.setText(memberName);
            textUrl.setText(url);

            // Load the URL
            loadUrl(url);
        } else {
            Toast.makeText(this, "Error: Missing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set click listeners
        buttonRefresh.setOnClickListener(v -> webView.reload());
        buttonBack.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        });
    }

    private void setupWebView() {
        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webSettings.setDomStorageEnabled(true); // Enable DOM storage
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Set WebViewClient to handle page loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // You could show a loading indicator here
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide loading indicator if you added one
                textUrl.setText(url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(WebLinkActivity.this, "Error loading page", Toast.LENGTH_SHORT).show();
            }
        });

        // Set WebChromeClient to handle JavaScript dialogs, favicons, etc.
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // You could update a progress bar here if you added one
            }
        });
    }

    private void loadUrl(String url) {
        if (url != null && !url.isEmpty()) {
            // Ensure URL has proper format
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            webView.loadUrl(url);
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}