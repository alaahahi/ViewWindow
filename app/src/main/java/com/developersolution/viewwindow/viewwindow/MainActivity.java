package com.developersolution.viewwindow.viewwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        window.getSettings().setLoadWithOverviewMode(true);
        window.getSettings().setUseWideViewPort(true);
        window.getSettings().setDatabaseEnabled(true);
        window.setWebViewClient(new SSLTolerentWebViewClient());
        window.loadUrl("http://" + ip_server+"/"+ type_window);
        window.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getBaseContext(), "Page Reload",
                        Toast.LENGTH_LONG).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
                timerRunnable.run() ;
                view.loadUrl("about:blank");
            }
        });
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
        String ipprf = sh.getString("ip", "");
        ip.setText(ipprf);
        window.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getBaseContext(), "Page Reload",
                        Toast.LENGTH_LONG).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
                timerRunnable.run() ;
                window.loadUrl("http://" + ip_server+"/"+ type_window);
            }
        });
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
                window.loadUrl("http://" + ip.getText().toString()+"/"+ spinner1.getSelectedItem().toString()) ;
                finish();
                startActivity(getIntent());
                window.reload();
                Toast.makeText(getBaseContext(), "Thanks Your Changes is Saved",
                Toast.LENGTH_LONG).show();
                if(!isNetworkConnected()){
                    window.reload();
                    Toast.makeText(getBaseContext(), "Page Reload",
                            Toast.LENGTH_LONG).show();
                    timerRunnable.run() ;
                };


            }
        });
        if(isNetworkConnected()) window.reload();
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
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(isNetworkConnected()){
                timerHandler.removeCallbacks(timerRunnable);
            }
            Toast.makeText(getBaseContext(),"Reload",
                    Toast.LENGTH_LONG).show();
            window.loadUrl("http://" + ip.getText().toString()+"/"+ spinner1.getSelectedItem().toString()) ;
            timerHandler.postDelayed(this, 10000);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

}
