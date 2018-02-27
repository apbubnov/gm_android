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

    ProgressDialog mProgressDialog;

    Map<String, String> parameters = new HashMap<String, String>();
    StringRequest request = null;
    org.json.simple.JSONObject jsonObjectAuth = new org.json.simple.JSONObject();
    String jsonAuth = "";

    String domen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        domen = getResources().getString(R.string.link);

        upgrade();

        dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();

        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        ArrayList group_id = new ArrayList();

        String sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});

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

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Service_Sync.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync.class));

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Intent intent = new Intent(MainActivity.this, Activity_crew.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;

                } else if (group_id.get(g).equals("21") || group_id.get(g).equals("22")) {

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Service_Sync.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync.class));

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

                    Intent intent = new Intent(MainActivity.this, Gager_office.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;
                }
                else if (group_id.get(g).equals("14")) {

                    Service_Sync_Import.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Service_Sync_Import.class));

                    Send_All.Alarm.setAlarm(MainActivity.this);
                    startService(new Intent(MainActivity.this, Send_All.class));

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

    void upgrade(){

        String versionName = BuildConfig.VERSION_NAME;
        Log.d("mLog", "vers = " + versionName);

        dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();

        String version = "";
        try {
            String sqlQuewy = "SELECT change_time "
                    + "FROM history_import_to_server" +
                    " WHERE title = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{"version"});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        version = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();
        }catch (Exception e){
        }

        if (version.equals("")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, versionName);
            values.put(DBHelper.KEY_TITLE, "version");
            db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
        } else if (version.equals(versionName)){
            //ничего не делаем
        } else {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_CHANGE_TIME, versionName);
            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"version"});
        }

        startService(new Intent(MainActivity.this, Service_Sync_Import.class));
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

                        ContentValues values = new ContentValues();
                        String sqlQuewy = "SELECT change_time "
                                + "FROM history_import_to_server" +
                                " WHERE title = ?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{"material"});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                    db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"material"});
                                } while (c.moveToNext());
                            }
                        }{
                            values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_TITLE, "material");
                            db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                        }
                        c.close();

                        values = new ContentValues();
                        sqlQuewy = "SELECT change_time "
                                + "FROM history_import_to_server" +
                                " WHERE title = ?";
                        c = db.rawQuery(sqlQuewy, new String[]{"mount"});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                    db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"mount"});
                                } while (c.moveToNext());
                            }
                        }{
                            values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_TITLE, "mount");
                            db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                        }
                        c.close();

                        values = new ContentValues();
                        sqlQuewy = "SELECT change_time "
                                + "FROM history_import_to_server" +
                                " WHERE title = ?";
                        c = db.rawQuery(sqlQuewy, new String[]{"dealer"});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                    db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "title = ?", new String[]{"dealer"});
                                } while (c.moveToNext());
                            }
                        }{
                            values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_TITLE, "dealer");
                            db.insert(DBHelper.HISTORY_IMPORT_TO_SERVER, null, values);
                        }
                        c.close();

                        sqlQuewy = "SELECT group_id "
                                + "FROM rgzbn_user_usergroup_map" +
                                " WHERE user_id = ?";

                        c = db.rawQuery(sqlQuewy, new String[]{user_id});

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
                        if (group_id.get(g).equals("11")) { // монтажная бригада

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

                                        ContentValues values = new ContentValues();
                                        values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                        db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});

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
                                            "это происходит из-за проектов, полотен, производителей и т.д., которые скачиваются...", Toast.LENGTH_LONG);
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
                        } else if (group_id.get(g).equals("22") || group_id.get(g).equals("21")) { // замерщик
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

                                            ContentValues values = new ContentValues();
                                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
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
                        else if (group_id.get(g).equals("14")) {    // дилер
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

                                            ContentValues values = new ContentValues();
                                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});
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