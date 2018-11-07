package com.developersolution.viewwindow.viewwindow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  WebView window;

    ImageButton sitting,reload;
    String ip_server , type_window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sitting = (ImageButton) findViewById(R.id.settingbtn);
        reload =(ImageButton) findViewById(R.id.reloadbtn);
        window =(WebView) findViewById(R.id.viewwindow);

        SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        type_window = sh.getString("type","");
        ip_server= sh.getString("ip","");

        window.getSettings().getBuiltInZoomControls();
        window.getSettings().getAllowFileAccess();
        window.getSettings().getAllowFileAccessFromFileURLs();
        window.getSettings().setAllowContentAccess(true);
        window.getSettings().getDomStorageEnabled();
        window.getSettings().setJavaScriptEnabled(true);
        window.getSettings().setDomStorageEnabled(true);
        window.getSettings().setAppCacheEnabled(true);
        window.getSettings().setLoadWithOverviewMode(true);
        window.getSettings().setUseWideViewPort(true);
        window.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        window.getSettings().setDatabaseEnabled(true);
        window.setWebChromeClient(new WebChromeClient());
        window.setWebViewClient(
                new SSLTolerentWebViewClient()
        );
        window.loadUrl("http://"+ip_server+type_window);
        sitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(MainActivity.this, Pop.class)});
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.reload();
            }
        });

    }
    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }



}
