package ru.ejevikaapp.gm_android;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class Activity_draft extends AppCompatActivity {

    private WebView mWebView;

    static String canvases, textures, diags_points, walls_points, pt_points, auto, code, alfavit;

    SharedPreferences sPref, SP4, SP5, SP9, SPI, SPSO, SPW;

    final String SAVED_TEXT = "saved_text", SAVED_N4 = "", SAVED_N5 = "", SAVED_N9 = "", SAVED_I = "", SAVED_SO = "",
            SAVED_WIDTH = "";

    public class myJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        myJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void func_back(int var1) {
            if (var1 == 1) {
                SPW = getSharedPreferences("end_draft", MODE_PRIVATE);
                SharedPreferences.Editor ed = SPW.edit();
                ed.putString("", "1");
                ed.commit();
                finish();
            }
        }

        @JavascriptInterface
        public void func_elem_jform_n4(String n4) {
            SP4 = getSharedPreferences("SAVED_N4", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP4.edit();
            ed.putString(SAVED_N4, n4);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_n5(String n5) {
            SP5 = getSharedPreferences("SAVED_N5", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP5.edit();
            ed.putString(SAVED_N5, n5);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_n9(String n9) {
            SP9 = getSharedPreferences("SAVED_N9", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP9.edit();
            ed.putString(SAVED_N9, n9);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_image(String im) {
            Log.d("mLog", im);
            SPI = getSharedPreferences("SAVED_I", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPI.edit();
            ed.putString(SAVED_I, im);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_image_cut(String im) {
            SPI = getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPI.edit();
            ed.putString(SAVED_I, im);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_square_obrezkov(String so) {
            SPSO = getSharedPreferences("SAVED_SO", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPSO.edit();
            ed.putString("", so);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_width(String sw) {
            SPW = getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", sw);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_seam(String seam) {
            SPW = getSharedPreferences("seam", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", seam);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_ll(String ll) {
            SPW = getSharedPreferences("SAVED_LL", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", ll);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_koordinats_poloten(String koordinats_poloten) {
            SPW = getSharedPreferences("SAVED_KP", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", koordinats_poloten);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_walls_points(String walls_points) {
            SPW = getSharedPreferences("draft_walls_points", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", walls_points);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_diags_points(String diags_points) {
            SPW = getSharedPreferences("draft_diags_points", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", diags_points);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_pt_points(String pt_points) {
            SPW = getSharedPreferences("draft_pt_points", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", pt_points);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_code(String code) {
            SPW = getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", code);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_p_usadki_final(String p_usadki_final) {
            SPW = getSharedPreferences("p_usadki_final", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", p_usadki_final);
            ed.commit();
        }

        @JavascriptInterface
        public void func_elem_jform_alfavit(String alfavit) {
            SPW = getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", alfavit);
            ed.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    private static class MyJavaInterface {
        @android.webkit.JavascriptInterface
        public String getGreeting() {

            return canvases;
        }

        @android.webkit.JavascriptInterface
        public String getTre() {

            return textures;
        }

        @android.webkit.JavascriptInterface
        public String get_diags_points() {

            return diags_points;
        }

        @android.webkit.JavascriptInterface
        public String get_walls_points() {

            return walls_points;
        }

        @android.webkit.JavascriptInterface
        public String get_pt_points() {

            return pt_points;
        }

        @android.webkit.JavascriptInterface
        public String get_code() {

            return code;
        }

        @android.webkit.JavascriptInterface
        public String get_alfavit() {

            return alfavit;
        }

        @android.webkit.JavascriptInterface
        public String get_auto() {

            return auto;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);

        sPref = this.getSharedPreferences("canvases", MODE_PRIVATE);
        canvases = sPref.getString("", "");

        sPref = this.getSharedPreferences("textures_draft", MODE_PRIVATE);
        textures = sPref.getString("", "");

        sPref = this.getSharedPreferences("draft_diags_points", MODE_PRIVATE);
        diags_points = sPref.getString("", "");

        sPref = this.getSharedPreferences("draft_walls_points", MODE_PRIVATE);
        walls_points = sPref.getString("", "");

        sPref = this.getSharedPreferences("draft_pt_points", MODE_PRIVATE);
        pt_points = sPref.getString("", "");

        sPref = this.getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
        code = sPref.getString("", "");

        sPref = this.getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
        alfavit = sPref.getString("", "");

        sPref = this.getSharedPreferences("draft_auto", MODE_PRIVATE);
        auto = sPref.getString("", "");

        Log.d("mLog", "________________");
        Log.d("mLog", "canvases " + canvases);
        Log.d("mLog", "textures " + textures);
        Log.d("mLog", "diags_points " + diags_points);
        Log.d("mLog", "walls_points  " + walls_points);
        Log.d("mLog", "pt_points " + pt_points);
        Log.d("mLog", "auto " + auto);
        Log.d("mLog", "________________");

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }

        String redactor = getIntent().getStringExtra("redactor");
        if (redactor.equals("0")) {
            mWebView.loadUrl("file:///android_asset/index.html");
        } else if (redactor.equals("1")) {
            mWebView.loadUrl("file:///android_asset/indexRedactor.html");
            SPW = getSharedPreferences("seam", MODE_PRIVATE);
            SharedPreferences.Editor ed = SPW.edit();
            ed.putString("", "0");
            ed.commit();
        }

        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.addJavascriptInterface(new myJavaScriptInterface(this),
                "AndroidFunction");

        mWebView.addJavascriptInterface(new MyJavaInterface(),
                "fun_canv"); // отдаёт в js
        mWebView.addJavascriptInterface(new MyJavaInterface(),
                "fun_tre"); // отдаёт в js

        SP4 = PreferenceManager.getDefaultSharedPreferences(this);
        SP5 = PreferenceManager.getDefaultSharedPreferences(this);
        SP9 = PreferenceManager.getDefaultSharedPreferences(this);
    }
}