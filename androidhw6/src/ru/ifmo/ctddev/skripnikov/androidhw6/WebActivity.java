package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, getIntent().getStringExtra("description"), "text/html", "en_US", null);
    }
}
