package com.developersolution.viewwindow.viewwindow;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.PowerManager;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutsitting;

    WebView window;
    EditText ip;
    Button save, hide;
    Spinner spinner1;

    ImageButton sitting,reload;
    String ip_server , type_window;
    protected PowerManager.WakeLock mWakeLock;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,"");
        this.mWakeLock.acquire();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sitting= findViewById(R.id.settingbtn);
        reload = findViewById(R.id.reloadbtn);
        window = findViewById(R.id.viewwindow);
        spinner1 = findViewById(R.id.spinner);
        ip = findViewById(R.id.ip);
        save = findViewById(R.id.savebtn);
        hide = findViewById(R.id.hidebtn);
        layoutsitting = findViewById(R.id.layoutsitting);
        SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        type_window = sh.getString("type", "");
        ip_server = sh.getString("ip", "");

        window.getSettings().getBuiltInZoomControls();
        window.getSettings().getAllowFileAccess();
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
        window.loadUrl("http://" + ip_server +"/"+ type_window);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.reload();
            }
        });
        sitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutsitting.setVisibility(View.VISIBLE);
            }
        });


        ArrayAdapter<CharSequence> marry = ArrayAdapter.createFromResource(this, R.array.spinardata, android.R.layout.simple_list_item_1);
        marry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(marry);
        String ipprf = sh.getString("ip", "");;
        ip.setText(ipprf);

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutsitting.setVisibility(View.GONE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor useredit = sh.edit();

                // useredit.putString("company",company.getText().toString());
                useredit.putString("ip", ip.getText().toString());
                useredit.putString("type", spinner1.getSelectedItem().toString());
                useredit.apply();

                Toast.makeText(getBaseContext(), "Thanks Your Changes is Saved",
                        Toast.LENGTH_LONG).show();


            }
        });
    }
    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }



}
