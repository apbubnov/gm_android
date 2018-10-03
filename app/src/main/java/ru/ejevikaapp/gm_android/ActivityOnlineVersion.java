package ru.ejevikaapp.gm_android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Dealer.Activity_analytics;
import ru.ejevikaapp.gm_android.Dealer.Activity_margin;
import ru.ejevikaapp.gm_android.Dealer.Dealer_office;

public class ActivityOnlineVersion extends AppCompatActivity {

    WebView webView;
    ProgressDialog dialog;

    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_version);

        if(HelperClass.isOnline(this)){
        } else {
            Toast.makeText(getApplicationContext(), "Нет соединения с интернетом, перейдите в offline версию",
                    Toast.LENGTH_LONG).show();
        }

        SharedPreferences SP = getSharedPreferences("version", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "online");
        ed.commit();

        SP = getSharedPreferences("link", MODE_PRIVATE);
        String domen = SP.getString("", "");

        if(domen.equals("")){
            domen = "calc";
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Загрузка ... ");
        dialog.show();

        CookieSyncManager.createInstance(webView.getContext());
        CookieSyncManager.getInstance().sync();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.loadUrl("http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&view=mainpage&type=dealermainpage");

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_online_version, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажмите ещё раз, для того чтобы выйти",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.offline) {

            SharedPreferences SP = getSharedPreferences("version", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "offline");
            ed.commit();

            Intent intent = new Intent(ActivityOnlineVersion.this, MainActivity.class);
            intent.putExtra("offline", 1);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading
                (WebView view, String url) {
            return(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
