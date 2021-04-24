package com.rek.gplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rek.gplay.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    private final static String TAG = "WebActivityTAG";

    ActivityWebBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.tb);
        getSupportActionBar().setTitle("加载中......");

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        binding.nwv.setWebChromeClient(new MyWebViewClient());

        WebSettings settings = binding.nwv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);

        binding.nwv.loadUrl(url);
    }

    class MyWebViewClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > 0 && binding.progress.getVisibility() == View.GONE) {
                binding.progress.setVisibility(View.VISIBLE);
            }
            binding.progress.setProgress(newProgress);
            if (newProgress > 99 && binding.progress.getVisibility() == View.VISIBLE) {
                binding.progress.setVisibility(View.GONE);
                getSupportActionBar().setTitle(binding.nwv.getTitle());
            }
        }
    }
}
