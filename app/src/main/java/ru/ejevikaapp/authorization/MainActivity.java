package ru.ejevikaapp.authorization;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.ejevikaapp.authorization.Crew.Activity_crew;
import ru.ejevikaapp.authorization.Dealer.Dealer_office;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    EditText login, password;
    Button btn_vhod;

    DBHelper dbHelper;
    SQLiteDatabase db;

    SharedPreferences SP_end;
    final String SAVED_end = "";

    ProgressDialog mProgressDialog, pd;

    String material;
    Map<String, String> parameters = new HashMap<String, String>();
    StringRequest request = null;
    org.json.simple.JSONObject jsonObjectAuth = new org.json.simple.JSONObject();
    org.json.simple.JSONObject jsonObjectProject = new org.json.simple.JSONObject();
    String jsonAuth = "", change_time_project = "";

    static org.json.simple.JSONObject jsonMaterial = new org.json.simple.JSONObject();

    static String domen = "test1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();

        String change = "";
        String sqlQuewy = "SELECT change_time "
                + "FROM history_import_to_server" +
                " WHERE title = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{"material"});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    change = c.getString(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            } else {
                change = "0000-00-00 00:00:00";
            }
        }
        c.close();

        jsonMaterial.put("change_time", change);
        material = String.valueOf(jsonMaterial);

        String bol = "";
        SP_end = this.getSharedPreferences("SAVED_end", MODE_PRIVATE);
        bol = SP_end.getString(SAVED_end, "");
        if (bol.equals("")) {

            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Идёт загрузка актуальных данных...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

            new Send_Material().execute();
        }

        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        ArrayList group_id = new ArrayList();

        sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{user_id});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    group_id.add(c.getString(c.getColumnIndex(c.getColumnName(0))));

                } while (c.moveToNext());
            }
        }
        c.close();

        if (user_id.equals("")) {
        } else
            for (int g = 0; group_id.size() > g; g++) {
                if (group_id.get(g).equals("11")) {

                    Service_Sync.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync.class));

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Intent intent = new Intent(MainActivity.this, Activity_crew.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                    break;

                } else if (group_id.get(g).equals("21") || group_id.get(g).equals("22")) {

                    Service_Sync.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync.class));

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Intent intent = new Intent(MainActivity.this, Gager_office.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                    break;
                }
                else if (group_id.get(g).equals("14")) {

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Service_Sync.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync.class));

                    Intent intent = new Intent(MainActivity.this, Dealer_office.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                    break;
                }
            }

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        btn_vhod = (Button) findViewById(R.id.btn_vhod);
        btn_vhod.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
    }

    class Send_Material extends AsyncTask<Integer, String, String> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.sendMaterialToAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final Integer... integers) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d("send_all__", "Send_Material " + res);

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values;
                    String new_id = "";
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);

                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_canvases");
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject canv = id_array.getJSONObject(i);

                            String id = canv.getString("id");
                            String texture_id = canv.getString("texture_id");
                            String color_id = canv.getString("color_id");
                            String name = canv.getString("name");
                            String country = canv.getString("country");
                            String width = canv.getString("width");
                            String price = canv.getString("price");
                            String count = canv.getString("count");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TEXTURE_ID, texture_id);
                            values.put(DBHelper.KEY_COLOR_ID, color_id);
                            values.put(DBHelper.KEY_NAME, name);
                            values.put(DBHelper.KEY_COUNTRY, country);
                            values.put(DBHelper.KEY_WIDTH, width);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_COUNT, count);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CANVASES, null, values);

                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_colors");
                        Log.d("send_all__", "rgzbn_gm_ceiling_colors " + id_array);

                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject color = id_array.getJSONObject(i);

                            String id = color.getString("id");
                            String title = color.getString("title");
                            String hex = color.getString("hex");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_HEX, hex);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COLORS, null, values);
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_components");
                        Log.d("send_all__", "rgzbn_gm_ceiling_components " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject comp = id_array.getJSONObject(i);

                            String id = comp.getString("id");
                            String title = comp.getString("title");
                            String unit = comp.getString("unit");
                            String code = comp.getString("code");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_UNIT, unit);
                            values.put(DBHelper.KEY_CODE, code);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS, null, values);
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_components_option");
                        Log.d("send_all__", "rgzbn_gm_ceiling_components_option " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject comp_op = id_array.getJSONObject(i);

                            String id = comp_op.getString("id");
                            String component_id = comp_op.getString("component_id");
                            String title = comp_op.getString("title");
                            String price = comp_op.getString("price");
                            String count = comp_op.getString("count");
                            String count_sale = comp_op.getString("count_sale");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_COMPONENT_ID, component_id);
                            values.put(DBHelper.KEY_TITLE, title);
                            values.put(DBHelper.KEY_PRICE, price);
                            values.put(DBHelper.KEY_COUNT, count);
                            values.put(DBHelper.KEY_COUNT_SALE, count_sale);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_COMPONENTS_OPTION, null, values);
                        }


                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_type");
                        Log.d("send_all__", "rgzbn_gm_ceiling_type " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject type = id_array.getJSONObject(i);

                            String id = type.getString("id");
                            String parent = type.getString("parent");
                            String title = type.getString("title");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_PARENT, parent);
                            values.put(DBHelper.KEY_TITLE, title);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE, null, values);
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_type_option");
                        Log.d("send_all__", "rgzbn_gm_ceiling_type_option " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject type_op = id_array.getJSONObject(i);

                            String id = type_op.getString("id");
                            String type_id = type_op.getString("type_id");
                            String component_id = type_op.getString("component_id");
                            String default_comp_option_id = type_op.getString("default_comp_option_id");
                            String count = type_op.getString("count");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TYPE_ID, type_id);
                            values.put(DBHelper.KEY_COMPONENT_ID, component_id);
                            values.put(DBHelper.KEY_DEFAULT_COMP_OPTION_ID, default_comp_option_id);
                            values.put(DBHelper.KEY_COUNT, count);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TYPE_OPTION, null, values);
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_textures");
                        Log.d("send_all__", "rgzbn_gm_ceiling_textures " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject text = id_array.getJSONObject(i);

                            String id = text.getString("id");
                            String texture_title = text.getString("texture_title");
                            String texture_colored = text.getString("texture_colored");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TEXTURE_TITLE, texture_title);
                            values.put(DBHelper.KEY_TEXTURE_COLORED, texture_colored);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, null, values);
                        }

                        id_array = dat.getJSONArray("rgzbn_gm_ceiling_status");
                        Log.d("send_all__", "rgzbn_gm_ceiling_status " + id_array);
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject status = id_array.getJSONObject(i);

                            String id = status.getString("id");
                            String title = status.getString("title");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, id);
                            values.put(DBHelper.KEY_TITLE, title);

                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_STATUS, null, values);

                        }

                        pd.dismiss();

                        SP_end = getSharedPreferences("SAVED_end", MODE_PRIVATE);
                        SharedPreferences.Editor ed = SP_end.edit();
                        ed.putString(SAVED_end, "1");
                        ed.commit();

                        values = new ContentValues();
                        Time time = new Time(Time.getCurrentTimezone());
                        time.setToNow();
                        String t = time.format("%Y-%m-%d %H:%M:00");
                        values.put(DBHelper.KEY_CHANGE_TIME, t);
                        values.put(DBHelper.KEY_TITLE, "material");
                        db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);

                        values = new ContentValues();
                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                        values.put(DBHelper.KEY_TITLE, "mount");
                        db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);

                        values = new ContentValues();
                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                        values.put(DBHelper.KEY_TITLE, "dealer");
                        db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);


                    } catch (Exception e) {
                        Log.d("send_all__", "send error " + String.valueOf(e));
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    final Toast toast = Toast.makeText(getApplicationContext(),
                            "Проверьте соединение с интернетом", Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 10000);
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("sync_data", material);
                    Log.d("send_all__", String.valueOf(parameters));
                    return parameters;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);

            return null;
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_vhod:

                if (login.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите данные", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    jsonObjectAuth.put("username", login.getText().toString());
                    jsonObjectAuth.put("password", password.getText().toString());
                    jsonAuth = String.valueOf(jsonObjectAuth);
                    Log.d("responce", jsonAuth);

                   //jsonObjectProject = new org.json.simple.JSONObject();
                   //jsonObjectProject.put("change_time", "2000-01-01");
                   //change_time_project = String.valueOf(jsonObjectProject);

                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setMessage("Проверяем...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.show();

                    new SendAuthorization().execute();
                }

                break;
        }
    }

    class SendAuthorization extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.Authorization_FromAndroid";
        RequestQueue requestQueue;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //try {
            request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d("responce", res);
                    String user_id = "";
                    ArrayList group_id = new ArrayList();

                    res = "{\"user\":" + res + "}";
                    Log.d("responce", res);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        user_id = jsonObject.getJSONObject("user").getString("id");

                        String ob = jsonObject.getJSONObject("user").getString("groups");

                        Log.d("mLog", ob);

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"material"});

                        values = new ContentValues();
                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"mount"});

                        values = new ContentValues();
                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"dealer"});

                        String sqlQuewy = "SELECT group_id "
                                + "FROM rgzbn_user_usergroup_map" +
                                " WHERE user_id = ?";

                        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    db.delete(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, "user_id = ?", new String[]{String.valueOf(user_id)});
                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        int i = 0;
                        for (String retval : ob.split(",")) {
                            i++;
                            int indexJava = retval.indexOf(":");
                            if (indexJava == -1) {
                            } else {
                                for (String retval1 : retval.split(":")) {

                                    retval1 = retval1.replaceAll("[^0-9]", "");

                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_user_usergroup_map" +
                                            " WHERE user_id = ? and group_id = ? ";

                                    c = db.rawQuery(sqlQuewy, new String[]{user_id, retval1});

                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                // ничего не делаем
                                            } while (c.moveToNext());
                                        } else {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_USER_ID, user_id);
                                            values.put(DBHelper.KEY_GROUP_ID, retval1);
                                            db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);
                                        }
                                    }
                                    c.close();

                                    //continue;
                                }

                                continue;
                            }
                        }

                        sqlQuewy = "SELECT group_id "
                                + "FROM rgzbn_user_usergroup_map" +
                                " WHERE user_id = ?";

                        c = db.rawQuery(sqlQuewy, new String[]{user_id});

                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    group_id.add(c.getString(c.getColumnIndex(c.getColumnName(0))));

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                    } catch (Exception e) {
                        Log.d("mLog", "error " + String.valueOf(e));
                        user_id = "";
                    }

                    boolean bool = true;
                    for (int g = 0; group_id.size() > g; g++) {
                        if (group_id.get(g).equals("11")) {

                            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
                            SharedPreferences.Editor ed = SP.edit();
                            ed.putString("", user_id);
                            ed.commit();

                            String dealer_id = "";
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(res);
                                dealer_id = jsonObject.getJSONObject("user").getString("dealer_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                            ed = SP.edit();
                            ed.putString("", dealer_id);
                            ed.commit();

                            String time = "";

                            String sqlQuewy = "SELECT change_time "
                                    + "FROM history_import_to_server" +
                                    " WHERE user_id = ?";

                            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                    } while (c.moveToNext());
                                }
                            }
                            c.close();

                            if (time.equals("")) {
                                time = "2017-01-01 00:00:00";
                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_USER_ID, user_id);
                                values.put(DBHelper.KEY_CHANGE_TIME, time);
                                db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                            }

                            Send_All.Alarm.setAlarm(MainActivity.this);
                            startService(new Intent(MainActivity.this, Send_All.class));

                            Service_Sync.Alarm.setAlarm(MainActivity.this);
                            startService(new Intent(MainActivity.this, Service_Sync.class));

                            Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                            startService(new Intent(MainActivity.this, Service_Sync_Import.class));


                            mProgressDialog.dismiss();

                            final Toast toast = Toast.makeText(getApplicationContext(),
                                    "При первом запуске приложения возможны торможения или зависания, " +
                                            "это происходит из-за большого количества проектов, которые скачиваются...", Toast.LENGTH_LONG);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 10000);

                            Intent intent = new Intent(MainActivity.this, Activity_crew.class);
                            startActivity(intent);
                            finish();

                            bool = false;
                        } else if (group_id.get(g).equals("22") || group_id.get(g).equals("21")) {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                user_id = jsonObject.getJSONObject("user").getString("id");

                                SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
                                SharedPreferences.Editor ed = SP.edit();
                                ed.putString("", user_id);
                                ed.commit();

                                SP = getSharedPreferences("user_id", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", user_id);
                                ed.commit();

                                String name = jsonObject.getJSONObject("user").getString("name");

                                SP = getSharedPreferences("name_user", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", name);
                                ed.commit();

                                String dealer_id = jsonObject.getJSONObject("user").getString("dealer_id");

                                SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", dealer_id);
                                ed.commit();

                                String time = "";

                                String sqlQuewy = "SELECT change_time "
                                        + "FROM history_import_to_server" +
                                        " WHERE user_id = ?";

                                Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (time.equals("")) {
                                    time = "2017-01-01 00:00:00";
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_USER_ID, user_id);
                                    values.put(DBHelper.KEY_CHANGE_TIME, time);
                                    db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                                }

                                Log.d("responce", time);

                                Send_All.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Send_All.class));

                                Service_Sync.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Service_Sync.class));

                                Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                                mProgressDialog.dismiss();

                                final Toast toast = Toast.makeText(getApplicationContext(),
                                        "При первом запуске приложения возможны торможения или зависания, " +
                                                "это происходит из-за большого количества проектов, которые скачиваются...", Toast.LENGTH_LONG);
                                toast.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 10000);

                                Intent intent = new Intent(MainActivity.this, Gager_office.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                            }

                            bool = false;
                        }
                        else if (group_id.get(g).equals("14")) {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                user_id = jsonObject.getJSONObject("user").getString("id");

                                SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
                                SharedPreferences.Editor ed = SP.edit();
                                ed.putString("", user_id);
                                ed.commit();

                                SP = getSharedPreferences("user_id", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", user_id);
                                ed.commit();

                                String name = jsonObject.getJSONObject("user").getString("name");

                                SP = getSharedPreferences("name_user", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", name);
                                ed.commit();

                                String dealer_id = jsonObject.getJSONObject("user").getString("dealer_id");

                                SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                                ed = SP.edit();
                                ed.putString("", dealer_id);
                                ed.commit();

                                String time = "";

                                String sqlQuewy = "SELECT change_time "
                                        + "FROM history_import_to_server" +
                                        " WHERE user_id = ?";

                                Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (time.equals("")) {
                                    time = "2017-01-01 00:00:00";
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_USER_ID, user_id);
                                    values.put(DBHelper.KEY_CHANGE_TIME, time);
                                    db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                                }

                                Log.d("responce", time);

                                Send_All.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Send_All.class));

                                Service_Sync.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Service_Sync.class));

                                Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                                startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                                mProgressDialog.dismiss();

                                final Toast toast = Toast.makeText(getApplicationContext(),
                                        "При первом запуске приложения возможны торможения или зависания, " +
                                                "это происходит из-за большого количества проектов, которые скачиваются...", Toast.LENGTH_LONG);
                                toast.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 10000);

                                Intent intent = new Intent(MainActivity.this, Dealer_office.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                            }

                            bool = false;
                        }
                    }
                    if (bool) {
                        mProgressDialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Неверные данные", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    mProgressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Проверьте подключение к интернету, или возможны работы на сервере", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("authorizations", jsonAuth);
                    //parameters.put("rgzbn_gm_ceiling_projects", change_time_project);
                    Log.d("responce", String.valueOf(parameters));
                    return parameters;
                }
            };

            request.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);

            return null;
        }
    }
}