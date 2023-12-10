package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.utils.GlobalProgressDialog;

public class OpenBioLink extends AppCompatActivity {

    private WebView webView;
    private GlobalProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_bio_link);

        webView = findViewById(R.id.obl_webView);
        this.progress = new GlobalProgressDialog(OpenBioLink.this);



        progress.showProgressBar();

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >=21) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("msg", "Processing webview url click...");
                view.loadUrl(url);
                progress.hideProgressBar();
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                progress.hideProgressBar();
                Log.i("msg", "Finished loading URL: " + url);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("msg", "Error: " + description);
                progress.hideProgressBar();
                Toast.makeText(OpenBioLink.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();

            }
        });

        webView.loadUrl(getIntent().getStringExtra("link"));

    }
}