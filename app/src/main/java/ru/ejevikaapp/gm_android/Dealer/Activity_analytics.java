package ru.ejevikaapp.gm_android.Dealer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.R;

import static android.content.Context.MODE_PRIVATE;

public class Activity_analytics extends AppCompatActivity {

    WebView webView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        SharedPreferences SP = this.getSharedPreferences("dealer_id", MODE_PRIVATE);
        String id = SP.getString("", "");

        SP = this.getSharedPreferences("link", MODE_PRIVATE);
        String domen = SP.getString("", "");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Загрузка ... ");
        dialog.show();

        webView.loadUrl("http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&view=analiticdealers&api=1&user_id=" + id);

        Log.d("mLog", webView.getUrl());

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100) {
                    dialog.dismiss();
                }
            }
        });

        webView.setWebViewClient(new MyWebViewClient());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading
                (WebView view, String url) {
            return(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}