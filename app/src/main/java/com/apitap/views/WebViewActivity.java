package com.apitap.views;

/**
 * Created by Shami on 5/4/2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apitap.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class WebViewActivity extends AppCompatActivity  {
    WebView webView;
    Context context;
    CircularProgressView mPocketBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        context = this;
        init();

    }

    private void init() {
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mPocketBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mPocketBar.setVisibility(View.VISIBLE);

            }
        });
        webView.loadUrl("https://www.google.co.in/");

    }


}
