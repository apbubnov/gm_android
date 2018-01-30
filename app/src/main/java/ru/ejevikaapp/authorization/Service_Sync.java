package ru.ejevikaapp.authorization;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ru.ejevikaapp.authorization.Fragments.Frag_g1_zamer;
import ru.ejevikaapp.authorization.Fragments.Fragment_general_infor;

public class Service_Sync extends Service {
    private static final String TAG = "responce_send";
    static DBHelper dbHelper;

    static String SAVED_ID = "", id_cl, id_project, phone, fio, pro_info, calc_date, dealer_id, item, S, P, table_name, table_name_title, components_diffusers = "",
            components_fixtures = "", components_corn = "", components_ecola = "", components_hoods = "", components_pipes = "", check_project="", check_calculation="",
            components_profil = "";

    static String jsonProjects = "", jsonCalc = "", jsonImage = "", jsonClient = "", jsonClient_Contacts = "", jsonFixtures = "", jsonEcola = "", jsonCornice = "",
            jsonPipes = "", jsonHoods = "", jsonDiffusers = "", global_results = "", check_client="", check_clients_contacts="", check_users = "", jsonUsers = "",
            jsonMounters = "";

    static org.json.simple.JSONObject jsonObjectClient = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonObjectClient_Contacts = new org.json.simple.JSONObject();
    static JSONObject jsonObjectProject = new JSONObject();
    static org.json.simple.JSONObject jsonObjectCalculation = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonObjectComponents = new org.json.simple.JSONObject();
    static JSONObject jsonObjectUsers = new JSONObject();
    static JSONObject jsonObjectMounters = new JSONObject();

    static RequestQueue requestQueue;

    static int time_mills = 20000;

    static String domen = "test1";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Service started!");

        Service_Sync.Alarm.setAlarm(this);

        //startService(new Intent(this, Service_Sync.class));

       //SharedPreferences SP = getSharedPreferences("sync", MODE_PRIVATE);
       //SharedPreferences.Editor ed = SP.edit();
       //ed.putString("", "0");
       //ed.commit();

       //int count_line = 0;
       //dbHelper = new DBHelper(this);
       //final SQLiteDatabase db = dbHelper.getReadableDatabase();
       //String sqlQuewy = "SELECT _id "
       //        + "FROM history_send_to_server ";
       //Cursor cursor = db.rawQuery(sqlQuewy,
       //        new String[]{});
       //if (cursor != null) {
       //    if (cursor.moveToFirst()) {
       //        do {
       //            count_line++;
       //        } while (cursor.moveToNext());
       //    }
       //}

       //if (!isRunning(this)) {
       //    this.startService(new Intent(this, Service_Sync.class));
       //} else if (count_line > 0) {

       //    int count_line_sync = 0;
       //    sqlQuewy = "SELECT _id "
       //            + "FROM history_send_to_server " +
       //            "where sync=?";
       //    cursor = db.rawQuery(sqlQuewy,
       //            new String[]{"1"});
       //    if (cursor != null) {
       //        if (cursor.moveToFirst()) {
       //            do {
       //                count_line_sync++;
       //            } while (cursor.moveToNext());
       //        }
       //    }

       //    if (count_line_sync == count_line) {
       //        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, null, null);
       //    }

       //    SP = this.getSharedPreferences("gager_id", MODE_PRIVATE);
       //    String gager_id = SP.getString("", "");
       //    final int gager_id_int = Integer.parseInt(gager_id) * 1000000;

       //    requestQueue = Volley.newRequestQueue(this.getApplicationContext());

       //    Log.v(TAG, "don't start service: already running...");

       //    Log.d(TAG, "--------------------------КЛИЕНТ------------------------");
       //    //клиент send
       //    jsonClient = "[";
       //    sqlQuewy = "SELECT id_old "
       //            + "FROM history_send_to_server " +
       //            "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //    cursor = db.rawQuery(sqlQuewy,
       //            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                    "send", "0", "rgzbn_gm_ceiling_clients", "1"});
       //    if (cursor != null) {
       //        if (cursor.moveToFirst()) {
       //            do {
       //                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                try {
       //                    sqlQuewy = "SELECT * "
       //                            + "FROM rgzbn_gm_ceiling_clients " +
       //                            "where _id = ?";
       //                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                    if (cursor != null) {
       //                        if (cursor.moveToFirst()) {
       //                            do {
       //                                JSONObject jsonObjectClient = new JSONObject();
       //                                for (int j = 0; j < 7; j++) {
       //                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
       //                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
       //                                    if (j == 0) {
       //                                        status = "android_id";
       //                                    }
       //                                    if (status1.equals("") || (status1.equals("null"))) {
       //                                    } else {
       //                                        jsonObjectClient.put(status, status1);
       //                                    }
       //                                }
       //                                jsonClient += String.valueOf(jsonObjectClient) + ",";
       //                            } while (cursor.moveToNext());
       //                        }
       //                    }
       //                    cursor.close();
       //                } catch (Exception e) {
       //                }

       //            } while (cursor.moveToNext());
       //        }
       //    }
       //    jsonClient = jsonClient.substring(0, jsonClient.length() - 1) + "]";
       //    if (jsonClient.equals("]")) {
       //    } else {

       //        new SendClientData().execute();

       //    }
       //    cursor.close();

       //    //клиент check

       //    check_client = "[";
       //    sqlQuewy = "SELECT id_new "
       //            + "FROM history_send_to_server " +
       //            "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //    cursor = db.rawQuery(sqlQuewy,
       //            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                    "check", "0", "rgzbn_gm_ceiling_clients"});
       //    if (cursor != null) {
       //        if (cursor.moveToFirst()) {
       //            do {
       //                jsonObjectClient = new org.json.simple.JSONObject();
       //                String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                jsonObjectClient.put("id", id_new);
       //                check_client += String.valueOf(jsonObjectClient) + ",";
       //            } while (cursor.moveToNext());
       //        }
       //    }
       //    check_client = check_client.substring(0, check_client.length() - 1) + "]";
       //    if (check_client.equals("]")) {
       //    } else {
       //        new CheckClientsData().execute();
       //    }
       //    cursor.close();

       //    Handler handler = new Handler();
       //    handler.postDelayed(new Runnable() {
       //        public void run() {
       //            Log.d(TAG, "--------------------------контакты------------------------");
       //            //контакты send
       //            SQLiteDatabase db = dbHelper.getReadableDatabase();
       //            jsonClient_Contacts = "[";
       //            String sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            Cursor cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_clients_contacts", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        //try {
       //                        sqlQuewy = "SELECT * "
       //                                + "FROM rgzbn_gm_ceiling_clients_contacts " +
       //                                "where _id = ?";
       //                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                        if (cursor != null) {
       //                            if (cursor.moveToFirst()) {
       //                                do {
       //                                    jsonObjectClient_Contacts = new org.json.simple.JSONObject();
       //                                    String status = "android_id";
       //                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                                    jsonObjectClient_Contacts.put(status, status1);
       //                                    status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(1)));
       //                                    status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
       //                                    jsonObjectClient_Contacts.put(status, status1);
       //                                    status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(2)));
       //                                    status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
       //                                    jsonObjectClient_Contacts.put(status, status1);

       //                                    jsonClient_Contacts += String.valueOf(jsonObjectClient_Contacts) + ",";

       //                                } while (cursor.moveToNext());
       //                            }
       //                        }
       //                        cursor.close();
       //                        //} catch (Exception e) {
       //                        //}
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            jsonClient_Contacts = jsonClient_Contacts.substring(0, jsonClient_Contacts.length() - 1) + "]";
       //            if (jsonClient_Contacts.equals("]")) {
       //            } else {
       //                new SendClientsContactsData().execute();
       //            }
       //            cursor.close();

       //            //контакты check
       //            check_clients_contacts = "[";
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_clients_contacts"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectClient_Contacts = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectClient_Contacts.put("id", id_new);
       //                        check_clients_contacts += String.valueOf(jsonObjectClient_Contacts) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            check_clients_contacts = check_clients_contacts.substring(0, check_clients_contacts.length() - 1) + "]";
       //            if (check_clients_contacts.equals("]")) {
       //            } else {
       //                new CheckClientsContactsData().execute();
       //            }
       //            cursor.close();
       //        }
       //    }, 5000);

       //    handler = new Handler();
       //    handler.postDelayed(new Runnable() {
       //        public void run() {
       //            Log.d(TAG, "--------------------------проект------------------------");
       //            //проект send
       //            jsonProjects = "[";
       //            String sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or id_old<?) and type=? and sync=? and name_table=? and status=?";
       //            Cursor cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_projects", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        Log.d(TAG, " project = " + id_old);

       //                        //try {
       //                        sqlQuewy = "SELECT * "
       //                                + "FROM rgzbn_gm_ceiling_projects " +
       //                                "where _id = ?";
       //                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                        if (c != null) {
       //                            if (c.moveToFirst()) {
       //                                do {
       //                                    JSONObject jsonObjectClient = new JSONObject();
       //                                    for (int j = 0; j < 62; j++) {
       //                                        String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
       //                                        String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
       //                                        if (j == 0) {
       //                                            status = "android_id";
       //                                        }
       //                                        if (status1 == null || status1.equals("") || status1.equals("null")) {
       //                                        } else {
       //                                            try {
       //                                                jsonObjectClient.put(status, status1);
       //                                            } catch (JSONException e) {
       //                                                e.printStackTrace();
       //                                            }
       //                                        }
       //                                    }
       //                                    jsonProjects += String.valueOf(jsonObjectClient) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                        }
       //                        c.close();
       //                        //} catch (Exception e) {
       //                        //}

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            jsonProjects = jsonProjects.substring(0, jsonProjects.length() - 1) + "]";
       //            if (jsonProjects.equals("]")) {
       //            } else {
       //                new SendProjectsData().execute();
       //            }
       //            cursor.close();

       //            //проект check
       //            check_project = "[";
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_projects"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectClient = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectClient.put("id", id_new);
       //                        check_project += String.valueOf(jsonObjectClient) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            check_project = check_project.substring(0, check_project.length() - 1) + "]";
       //            if (check_project.equals("]")) {
       //            } else {
       //                new CheckProjectsData().execute();
       //            }
       //            cursor.close();
       //        }
       //    }, 5000);

       //    handler = new Handler();
       //    handler.postDelayed(new Runnable() {
       //        public void run() {
       //            Log.d(TAG, "--------------------------калькулятор------------------------");
       //            //калькулятор send
       //            jsonCalc = "[";
       //            String sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            Cursor cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_calculations", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        sqlQuewy = "SELECT * "
       //                                + "FROM rgzbn_gm_ceiling_calculations " +
       //                                "where _id = ?";
       //                        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_old});
       //                        if (c != null) {
       //                            if (c.moveToFirst()) {
       //                                do {
       //                                    for (int j = 0; j < 51; j++) {
       //                                        try {
       //                                            String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
       //                                            String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
       //                                            if (status.equals("_id")) {
       //                                                status = "android_id";
       //                                                jsonObjectCalculation.put(status, status1);
       //                                            } else if (status.equals("calc_image")) {
       //                                            } else if (status.equals("cut_image")) {
       //                                            } else if (status1.equals("")) {
       //                                            } else {
       //                                                jsonObjectCalculation.put(status, status1);
       //                                            }
       //                                        } catch (Exception e) {
       //                                        }
       //                                    }
       //                                    jsonCalc += String.valueOf(jsonObjectCalculation) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            jsonCalc = jsonCalc.substring(0, jsonCalc.length() - 1) + "]";
       //            if (jsonCalc.equals("]")) {
       //            } else {
       //                new SendCalculationsData().execute();
       //            }
       //            cursor.close();

       //            //калькулятор check
       //            check_calculation = "[";
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_calculations"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {

       //                        jsonObjectClient = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectClient.put("id", id_new);
       //                        check_calculation += String.valueOf(jsonObjectClient) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            check_calculation = check_calculation.substring(0, check_calculation.length() - 1) + "]";
       //            if (check_calculation.equals("]")) {
       //            } else {
       //                new CheckCalculationData().execute();
       //            }
       //            cursor.close();
       //        }
       //    }, 5000);

       //    handler = new Handler();
       //    handler.postDelayed(new Runnable() {
       //        public void run() {
       //            Log.d(TAG, "--------------------------картинки------------------------");
       //            //картинки send
       //            try {
       //                org.json.simple.JSONObject jsonObjectCalculation_image = new org.json.simple.JSONObject();
       //                String sqlQuewy = "SELECT id_new "
       //                        + "FROM history_send_to_server " +
       //                        "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //                Cursor cursor = db.rawQuery(sqlQuewy,
       //                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                                "send", "0", "rgzbn_gm_ceiling_calculations_cal", "1"});
       //                if (cursor != null) {
       //                    if (cursor.moveToFirst()) {
       //                        do {
       //                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                            sqlQuewy = "SELECT _id, calc_image, cut_image "
       //                                    + "FROM rgzbn_gm_ceiling_calculations " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    jsonObjectCalculation_image = new org.json.simple.JSONObject();
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
       //                                    Log.d(TAG, status1);
       //                                    jsonObjectCalculation_image.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(c.getColumnName(1)));
       //                                    status1 = c.getString(c.getColumnIndex(c.getColumnName(1)));
       //                                    jsonObjectCalculation_image.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(c.getColumnName(2)));
       //                                    status1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
       //                                    jsonObjectCalculation_image.put(status, status1);

       //                                    jsonImage = String.valueOf(jsonObjectCalculation_image);
       //                                    new SendCalculation_ImageData().execute();

       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } while (cursor.moveToNext());

       //                    }
       //                    cursor.close();
       //                }

       //            } catch (Exception e) {
       //            }
       //        }
       //    }, 5000);

       //    handler = new Handler();
       //    handler.postDelayed(new Runnable() {
       //        public void run() {

       //            components_diffusers = "[";
       //            components_ecola = "[";
       //            components_fixtures = "[";
       //            components_hoods = "[";
       //            components_pipes = "[";
       //            components_corn = "[";
       //            components_profil = "[";

       //            Log.d(TAG, "--------------------------компоненты_fix------------------------");
       //            //компоненты_fix send
       //            String sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            Cursor cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_fixtures", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_fixtures " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_TYPE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_TYPE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_SIZE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_SIZE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_fixtures += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            Log.d(TAG, "--------------------------компоненты_ecola------------------------");
       //            //компоненты_ecola send
       //            sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_ecola", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_ecola " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_LAMP));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_LAMP));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_ecola += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            Log.d(TAG, "--------------------------компоненты_diff------------------------");
       //            //компоненты_diff send
       //            sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
       //                            "send", "0", "rgzbn_gm_ceiling_diffusers", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_diffusers " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N23_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N23_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N23_SIZE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N23_SIZE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_diffusers += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            Log.d(TAG, "--------------------------компоненты_hoods------------------------");
       //            //компоненты_hoods send
       //            sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
       //                            "send", "0", "rgzbn_gm_ceiling_hoods", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_hoods " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_TYPE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_TYPE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_SIZE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_SIZE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_hoods += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            Log.d(TAG, "--------------------------компоненты_pipes------------------------");
       //            //компоненты_pipes send
       //            sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_pipes", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_pipes " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N14_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N14_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N14_SIZE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N14_SIZE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_pipes += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            Log.d(TAG, "--------------------------компоненты_cornice------------------------");
       //            //компоненты_cornice send
       //            sqlQuewy = "SELECT id_old "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "send", "0", "rgzbn_gm_ceiling_cornice", "1"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        try {
       //                            sqlQuewy = "SELECT * "
       //                                    + "FROM rgzbn_gm_ceiling_cornice " +
       //                                    "where _id = ? ";
       //                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
       //                            if (c != null) {
       //                                c.moveToFirst();
       //                                do {
       //                                    String status = "android_id";
       //                                    String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N15_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N15_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N15_TYPE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N15_TYPE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N29_COUNT));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N29_COUNT));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N29_TYPE));
       //                                    status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N29_TYPE));
       //                                    jsonObjectComponents.put(status, status1);
       //                                    components_profil += String.valueOf(jsonObjectComponents) + ",";
       //                                } while (c.moveToNext());
       //                            }
       //                            c.close();
       //                        } catch (Exception e) {
       //                        }

       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            if (components_diffusers.length() < 2) {
       //                components_diffusers += "]";
       //            } else {
       //                components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
       //            }
       //            if (components_ecola.length() < 2) {
       //                components_ecola += "]";
       //            } else {
       //                components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
       //            }
       //            if (components_fixtures.length() < 2) {
       //                components_fixtures += "]";
       //            } else {
       //                components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
       //            }
       //            if (components_hoods.length() < 2) {
       //                components_hoods += "]";
       //            } else {
       //                components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
       //            }
       //            if (components_pipes.length() < 2) {
       //                components_pipes += "]";
       //            } else {
       //                components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
       //            }
       //            if (components_corn.length() < 2) {
       //                components_corn += "]";
       //            } else {
       //                components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
       //            }
       //            if (components_profil.length() < 2) {
       //                components_profil += "]";
       //            } else {
       //                components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";
       //            }

       //            new SendComponents().execute();

       //            //компоненты_fix check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_fixtures"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_fixtures += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_diff check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_diffusers"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_diffusers += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_pipes check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_pipes"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_pipes += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_hoods check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_hoods"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_hoods += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_ecola check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_ecola"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_ecola += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_cornice check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_cornice"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_corn += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            //компоненты_cornice check
       //            sqlQuewy = "SELECT id_new "
       //                    + "FROM history_send_to_server " +
       //                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
       //            cursor = db.rawQuery(sqlQuewy,
       //                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
       //                            "check", "0", "rgzbn_gm_ceiling_profil"});
       //            if (cursor != null) {
       //                if (cursor.moveToFirst()) {
       //                    do {
       //                        jsonObjectComponents = new org.json.simple.JSONObject();
       //                        String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                        jsonObjectComponents.put("id", id_new);
       //                        components_profil += String.valueOf(jsonObjectComponents) + ",";
       //                    } while (cursor.moveToNext());
       //                }
       //            }
       //            cursor.close();

       //            components_diffusers = "[" + components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
       //            components_ecola = "[" + components_ecola.substring(0, components_ecola.length() - 1) + "]";
       //            components_fixtures = "[" + components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
       //            components_hoods = "[" + components_hoods.substring(0, components_hoods.length() - 1) + "]";
       //            components_pipes = "[" + components_pipes.substring(0, components_pipes.length() - 1) + "]";
       //            components_corn = "[" + components_corn.substring(0, components_corn.length() - 1) + "]";
       //            components_profil = "[" + components_corn.substring(0, components_corn.length() - 1) + "]";

       //            if (components_diffusers.equals("]") && components_ecola.equals("]") && components_fixtures.equals("]") && components_hoods.equals("]") &&
       //                    components_pipes.equals("]") && components_corn.equals("]") && components_profil.equals("]")) {
       //            } else {
       //                new CheckComponents().execute();
       //            }

       //        }
       //    }, 1000);

       //} else {
       //}

        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service_Sync.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Service stopped!");
    }

    public static class Alarm extends BroadcastReceiver {

        public static final String ALARM_EVENT = "net.multipi.ALARM";
        public static final int ALARM_INTERVAL_SEC = 1;

        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.v(TAG, "Alarm received: " + intent.getAction());

            SharedPreferences SP = context.getSharedPreferences("sync", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "0");
            ed.commit();

            int count_line = 0;
            dbHelper = new DBHelper(context);
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sqlQuewy = "SELECT _id "
                    + "FROM history_send_to_server ";
            Cursor cursor = db.rawQuery(sqlQuewy,
                    new String[]{});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        count_line++;
                    } while (cursor.moveToNext());
                }
            }

            if (!isRunning(context)) {
                context.startService(new Intent(context, Service_Sync.class));
            } else if (count_line>0){

                int count_line_sync=0;
                sqlQuewy = "SELECT _id "
                        + "FROM history_send_to_server " +
                        "where sync=?";
                cursor = db.rawQuery(sqlQuewy,
                        new String[]{"1"});
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            count_line_sync++;
                        } while (cursor.moveToNext());
                    }
                }

                if (count_line_sync == count_line){
                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, null, null);
                }

                SP = context.getSharedPreferences("gager_id", MODE_PRIVATE);
                String gager_id = SP.getString("", "");
                final int gager_id_int = Integer.parseInt(gager_id) * 1000000;

                requestQueue = Volley.newRequestQueue(context.getApplicationContext());

                Log.v(TAG, "don't start service: already running...");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                Log.d(TAG,"--------------------------КЛИЕНТ------------------------");
                //клиент send
                jsonClient= "[";
                String sqlQuewy = "SELECT id_old "
                        + "FROM history_send_to_server " +
                        "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                Cursor cursor = db.rawQuery(sqlQuewy,
                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                "send", "0", "rgzbn_gm_ceiling_clients","1"});
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                            try {
                                sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_clients " +
                                        "where _id = ?";
                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            JSONObject jsonObjectClient = new JSONObject();
                                            for (int j = 0; j < 7; j++) {
                                                String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                if (j == 0) {
                                                    status = "android_id";
                                                }
                                                if (status1.equals("")||(status1.equals("null"))) {
                                                } else {
                                                    jsonObjectClient.put(status, status1);
                                                }
                                            }
                                            jsonClient += String.valueOf(jsonObjectClient) + ",";
                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();
                            } catch (Exception e) {
                            }

                        } while (cursor.moveToNext());
                    }
                }
                jsonClient = jsonClient.substring(0, jsonClient.length() - 1) + "]";
                if (jsonClient.equals("]")){
                }else {

                    new SendClientData().execute();
                }
                cursor.close();

                //клиент check

                check_client = "[";
                sqlQuewy = "SELECT id_new "
                        + "FROM history_send_to_server " +
                        "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                cursor = db.rawQuery(sqlQuewy,
                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                "check", "0", "rgzbn_gm_ceiling_clients"});
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjectClient = new org.json.simple.JSONObject();
                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                            jsonObjectClient.put("id", id_new);
                            check_client += String.valueOf(jsonObjectClient) + ",";
                        } while (cursor.moveToNext());
                    }
                }
                check_client = check_client.substring(0, check_client.length() - 1) + "]";
                if (check_client.equals("]")){
                }else {
                    new CheckClientsData().execute();
                }
                cursor.close();
                    }
                }, 5000);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG,"--------------------------контакты------------------------");
                        //контакты send
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        jsonClient_Contacts="[";
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_clients_contacts","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    //try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                jsonObjectClient_Contacts = new org.json.simple.JSONObject();
                                                String status = "android_id";
                                                String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                jsonObjectClient_Contacts.put(status, status1);
                                                status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(1)));
                                                status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                                                jsonObjectClient_Contacts.put(status, status1);
                                                status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(2)));
                                                status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
                                                jsonObjectClient_Contacts.put(status, status1);

                                                jsonClient_Contacts += String.valueOf(jsonObjectClient_Contacts) + ",";

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();
                                    //} catch (Exception e) {
                                    //}
                                } while (cursor.moveToNext());
                            }
                        }
                        jsonClient_Contacts = jsonClient_Contacts.substring(0, jsonClient_Contacts.length() - 1) + "]";
                        if (jsonClient_Contacts.equals("]")){
                        }else {
                            new SendClientsContactsData().execute();
                        }
                        cursor.close();

                        //контакты check
                        check_clients_contacts = "[";
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_clients_contacts"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectClient_Contacts = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectClient_Contacts.put("id", id_new);
                                    check_clients_contacts += String.valueOf(jsonObjectClient_Contacts) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        check_clients_contacts = check_clients_contacts.substring(0, check_clients_contacts.length() - 1) + "]";
                        if (check_clients_contacts.equals("]")){
                        }else {
                            new CheckClientsContactsData().execute();
                        }
                        cursor.close();
                    }
                }, 5000);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG,"--------------------------проект------------------------");
                        //проект send
                        jsonProjects ="[";
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or id_old<?)  and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_projects","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    //try {
                                   sqlQuewy = "SELECT * "
                                           + "FROM rgzbn_gm_ceiling_projects " +
                                           "where _id = ?";
                                   Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                   if (c != null) {
                                       if (c.moveToFirst()) {
                                           do {
                                               JSONObject jsonObjectClient = new JSONObject();
                                               for (int j = 0; j < 61; j++) {
                                                   String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                   String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                   if (j == 0) {
                                                       status = "android_id";
                                                   }
                                                   if (status1 == null || status1.equals("") || status1.equals("null")) {
                                                   } else {
                                                       try {
                                                           jsonObjectClient.put(status, status1);
                                                       } catch (JSONException e) {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                               }
                                               jsonProjects += String.valueOf(jsonObjectClient) + ",";
                                           } while (c.moveToNext());
                                       }
                                   }
                                   c.close();
                                    //} catch (Exception e) {
                                    //}

                                } while (cursor.moveToNext());
                            }
                        }
                        jsonProjects = jsonProjects.substring(0, jsonProjects.length() - 1) + "]";
                        if (jsonProjects.equals("]")){
                        }else {
                            new SendProjectsData().execute();
                        }
                        cursor.close();

                        //проект check
                        check_project = "[";
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_projects"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectClient = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectClient.put("id", id_new);
                                    check_project += String.valueOf(jsonObjectClient) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        check_project = check_project.substring(0, check_project.length() - 1) + "]";
                        if (check_project.equals("]")){
                        }else {
                            new CheckProjectsData().execute();
                        }
                        cursor.close();
                    }
                }, 5000);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {Log.d(TAG,"--------------------------калькулятор------------------------");
                        //калькулятор send
                        jsonCalc = "[";
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_calculations","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_calculations " +
                                            "where _id = ?";
                                    Cursor c = db.rawQuery(sqlQuewy, new String[]{id_old});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                for (int j = 0; j < 51; j++) {
                                                    try {
                                                        String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                        String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                        if (status.equals("_id")) {
                                                            status = "android_id";
                                                            jsonObjectCalculation.put(status, status1);
                                                        } else if (status.equals("calc_image")) {
                                                        } else if (status.equals("cut_image")) {
                                                        } else if (status1.equals("")) {
                                                        } else {
                                                            jsonObjectCalculation.put(status, status1);
                                                        }
                                                    }catch (Exception e){
                                                    }
                                                }
                                                jsonCalc += String.valueOf(jsonObjectCalculation) + ",";
                                            } while (c.moveToNext());
                                        }
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        jsonCalc = jsonCalc.substring(0, jsonCalc.length() - 1) + "]";
                        if (jsonCalc.equals("]")){
                        }else {
                            new SendCalculationsData().execute();
                        }
                        cursor.close();

                        //калькулятор check
                        check_calculation="[";
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_calculations"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {

                                    jsonObjectClient = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectClient.put("id", id_new);
                                    check_calculation += String.valueOf(jsonObjectClient) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        check_calculation = check_calculation.substring(0, check_calculation.length() - 1) + "]";
                        if (check_calculation.equals("]")){
                        }else {
                            new CheckCalculationData().execute();
                        }
                        cursor.close();
                    }
                }, 5000);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG, "--------------------------картинки------------------------");
                        //картинки send
                        try {
                            org.json.simple.JSONObject jsonObjectCalculation_image = new org.json.simple.JSONObject();
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_calculations_cal", "1"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                        sqlQuewy = "SELECT _id, calc_image, cut_image "
                                                + "FROM rgzbn_gm_ceiling_calculations " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                jsonObjectCalculation_image = new org.json.simple.JSONObject();
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                                Log.d(TAG, status1);
                                                jsonObjectCalculation_image.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(c.getColumnName(1)));
                                                status1 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                                jsonObjectCalculation_image.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(c.getColumnName(2)));
                                                status1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                                jsonObjectCalculation_image.put(status, status1);

                                                jsonImage = String.valueOf(jsonObjectCalculation_image);
                                                new SendCalculation_ImageData().execute();

                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                            }

                        } catch (Exception e) {
                        }

                    }

                }, 5000);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        components_diffusers = "[";
                        components_ecola = "[";
                        components_fixtures = "[";
                        components_hoods = "[";
                        components_pipes = "[";
                        components_corn = "[";
                        components_profil = "[";


                        Log.d(TAG,"--------------------------компоненты_fix------------------------");
                        //компоненты_fix send
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_fixtures","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_fixtures " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_TYPE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_TYPE));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N13_SIZE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N13_SIZE));
                                                jsonObjectComponents.put(status, status1);
                                                components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_ecola------------------------");
                        //компоненты_ecola send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_ecola","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_ecola " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N26_LAMP));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N26_LAMP));
                                                jsonObjectComponents.put(status, status1);
                                                components_ecola += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_diff------------------------");
                        //компоненты_diff send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
                                        "send", "0", "rgzbn_gm_ceiling_diffusers", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_diffusers " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N23_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N23_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N23_SIZE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N23_SIZE));
                                                jsonObjectComponents.put(status, status1);
                                                components_diffusers += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_hoods------------------------");
                        //компоненты_hoods send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
                                        "send", "0", "rgzbn_gm_ceiling_hoods","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_hoods " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_TYPE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_TYPE));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N22_SIZE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N22_SIZE));
                                                jsonObjectComponents.put(status, status1);
                                                components_hoods += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_pipes------------------------");
                        //компоненты_pipes send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_pipes","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_pipes " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N14_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N14_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N14_SIZE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N14_SIZE));
                                                jsonObjectComponents.put(status, status1);
                                                components_pipes += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_cornice------------------------");
                        //компоненты_cornice send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_cornice","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_cornice " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N15_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N15_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N15_TYPE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N15_TYPE));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N15_SIZE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N15_SIZE));
                                                jsonObjectComponents.put(status, status1);
                                                components_corn += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        Log.d(TAG,"--------------------------компоненты_profil------------------------");
                        //компоненты_cornice send
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_profil","1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_profil " +
                                                "where _id = ? ";
                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (c != null) {
                                            c.moveToFirst();
                                            do {
                                                String status = "android_id";
                                                String status1 = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N29_COUNT));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N29_COUNT));
                                                jsonObjectComponents.put(status, status1);
                                                status = c.getColumnName(c.getColumnIndex(DBHelper.KEY_N29_TYPE));
                                                status1 = c.getString(c.getColumnIndex(DBHelper.KEY_N29_TYPE));
                                                jsonObjectComponents.put(status, status1);
                                                components_profil += String.valueOf(jsonObjectComponents) + ",";
                                            } while (c.moveToNext());
                                        }
                                        c.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        if (components_diffusers.length()<2){
                            components_diffusers += "]";
                        } else {
                            components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
                        }
                        if (components_ecola.length()<2){
                            components_ecola += "]";
                        } else {
                            components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
                        }
                        if (components_fixtures.length()<2){
                            components_fixtures += "]";
                        } else {
                            components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
                        }
                        if (components_hoods.length()<2){
                            components_hoods += "]";
                        } else {
                            components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
                        }
                        if (components_pipes.length()<2){
                            components_pipes += "]";
                        } else {
                            components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
                        }
                        if (components_corn.length()<2){
                            components_corn += "]";
                        } else {
                            components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
                        }
                        if (components_profil.length()<2){
                            components_profil += "]";
                        } else {
                            components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";
                        }

                        new SendComponents().execute();

                        components_diffusers = "[";
                        components_ecola = "[";
                        components_fixtures = "[";
                        components_hoods = "[";
                        components_pipes = "[";
                        components_corn = "[";
                        components_profil = "[";

                        //компоненты_fix check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_fixtures"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_diff check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_diffusers"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_diffusers += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_pipes check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_pipes"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_pipes += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_hoods check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_hoods"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_hoods += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_ecola check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_ecola"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_ecola += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_cornice check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_cornice"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_corn += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_cornice check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_profil"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    components_profil += String.valueOf(jsonObjectComponents) + ",";
                                    new CheckCalculationData().execute();
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
                        components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
                        components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
                        components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
                        components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
                        components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
                        components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";

                        Log.d(TAG, "components_diffusers " + components_diffusers);
                        Log.d(TAG, "components_ecola " + components_ecola);
                        Log.d(TAG, "components_fixtures " + components_fixtures);
                        Log.d(TAG, "components_hoods " + components_hoods);
                        Log.d(TAG, "components_pipes " + components_pipes);
                        Log.d(TAG, "components_corn " + components_corn);
                        Log.d(TAG, "components_profil " + components_profil);



                        if (components_diffusers.equals("]") && components_ecola.equals("]") && components_fixtures.equals("]") && components_hoods.equals("]") &&
                                components_pipes.equals("]") && components_corn.equals("]") && components_profil.equals("]")){
                        }else {
                            new CheckComponents().execute();
                        }

                    }
                }, 1000);


                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG,"--------------------------ЮЗЕРЫ------------------------");
                        //клиент send
                        jsonUsers = "[";
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_users", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_users " +
                                                "where _id = ?";
                                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectUsers = new JSONObject();
                                                    for (int j = 0; j < 19; j++) {
                                                        String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        if (j == 0) {
                                                            status = "android_id";
                                                        }

                                                        try {
                                                            if (status1.equals("") || (status1 == null)) {
                                                            } else {
                                                                jsonObjectUsers.put(status, status1);
                                                            }
                                                        }catch (Exception e){
                                                        }

                                                        Log.d(TAG, j + " " + String.valueOf(jsonObjectUsers));
                                                    }
                                                    Log.d(TAG, " end " + String.valueOf(jsonObjectUsers));
                                                    jsonUsers += String.valueOf(jsonObjectUsers) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();
                                    } catch (Exception e) {
                                        Log.d(TAG, String.valueOf(e));
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        jsonUsers = jsonUsers.substring(0, jsonUsers.length() - 1) + "]";

                        if (jsonUsers.equals("]")){
                        }else {
                            new SendUsersData().execute();
                        }

                        cursor.close();

                        //клиент check

                       check_users = "[";
                       sqlQuewy = "SELECT id_new "
                               + "FROM history_send_to_server " +
                               "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                       cursor = db.rawQuery(sqlQuewy,
                               new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                       "check", "0", "rgzbn_users"});
                       if (cursor != null) {
                           if (cursor.moveToFirst()) {
                               do {
                                   try {
                                       jsonObjectUsers = new JSONObject();
                                       String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                       jsonObjectUsers.put("id", id_new);
                                       check_users += String.valueOf(jsonObjectUsers) + ",";
                                   } catch (Exception e){
                                   }
                               } while (cursor.moveToNext());
                           }
                       }
                       check_users = check_users.substring(0, check_users.length() - 1) + "]";
                       if (check_users.equals("]")){
                       }else {
                           new CheckUsersData().execute();
                       }
                       cursor.close();
                    }
                }, 5000);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG,"--------------------------МОНТАЖНИКИ------------------------");
                        //клиент send
                        jsonMounters = "[";
                        String sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=? and status=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_mounters", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_mounters " +
                                                "where _id = ?";
                                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectMounters = new JSONObject();
                                                    for (int j = 0; j < 4; j++) {
                                                        String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        if (j == 0) {
                                                            status = "android_id";
                                                        }

                                                        try {
                                                            if (status1.equals("") || (status1 == null)) {
                                                            } else {
                                                                jsonObjectMounters.put(status, status1);
                                                            }
                                                        }catch (Exception e){
                                                        }

                                                        Log.d(TAG, j + " " + String.valueOf(jsonObjectMounters));
                                                    }
                                                    Log.d(TAG, " end " + String.valueOf(jsonObjectMounters));
                                                    jsonMounters += String.valueOf(jsonObjectMounters) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();
                                    } catch (Exception e) {
                                        Log.d(TAG, String.valueOf(e));
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        jsonMounters = jsonMounters.substring(0, jsonMounters.length() - 1) + "]";

                        if (jsonMounters.equals("]")){
                        }else {
                            new SendMountersData().execute();
                        }

                        cursor.close();

                        //клиент check

                        //check_users = "[";
                        //sqlQuewy = "SELECT id_new "
                        //        + "FROM history_send_to_server " +
                        //        "where ((id_old>? and id_old<?) or (id_old<?)) and type=? and sync=? and name_table=?";
                        //cursor = db.rawQuery(sqlQuewy,
                        //        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                        //                "check", "0", "rgzbn_users"});
                        //if (cursor != null) {
                        //    if (cursor.moveToFirst()) {
                        //        do {
                        //            jsonObjectUsers = new org.json.simple.JSONObject();
                        //            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                        //            jsonObjectUsers.put("id", id_new);
                        //            check_users += String.valueOf(jsonObjectUsers) + ",";
                        //        } while (cursor.moveToNext());
                        //    }
                        //}
                        //check_users = check_users.substring(0, check_users.length() - 1) + "]";
                        //if (check_users.equals("]")){
                        //}else {
                        //    new CheckUsersData().execute();
                        //}
                        //cursor.close();
                    }
                }, 5000);

            } else {

            }

        }

        public static void setAlarm(Context context) {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 * ALARM_INTERVAL_SEC, pi);
        }

        public static void cancelAlarm(Context context) {
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

    }

    static class SendClientData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\"\u041e\u0448\u0438\u0431\u043a\u0430!\"")) {
                        Log.d("sync_app", "SendClientData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id="";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients");
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                String sqlQuewy = "SELECT * "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync=? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id),
                                        "send", "0", "rgzbn_gm_ceiling_clients"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?", new String[]{old_id});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_SYNC, "1");
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ?",
                                                    new String[]{old_id, "rgzbn_gm_ceiling_clients", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_OLD, old_id);
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                                            values.put(DBHelper.KEY_SYNC, "0");
                                            values.put(DBHelper.KEY_TYPE, "check");
                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                            jsonObjectClient.put("id", new_id);

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();

                                sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                        "where client_id = ? ";
                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id)});
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, values, "client_id = ?", new String[]{old_id});

                                    } while (cursor.moveToNext());
                                }
                                cursor.close();

                                sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where client_id = ? ";
                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id)});
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "client_id = ?", new String[]{old_id});

                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                            }

                        } catch (Exception e) {
                        }

                        check_client = "[" + String.valueOf(jsonObjectClient) + "]";
                    }
                    new CheckClientsData().execute();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients", jsonClient );
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckClientsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("sync_app", "CheckClientsData пусто");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check","0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and sync=?",
                                                new String[]{new_id,"0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }

                    } catch (Exception e) {
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients", check_client);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendUsersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "SendUsersData " + res);

                    if (res.equals("")){
                    } else {

                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_users");
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                Log.d(TAG, new_id + " " + old_id);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, "rgzbn_users","0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_BRIGADE, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, values, "id_brigade = ?",
                                        new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_USER_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, values, "user_id = ?",
                                        new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update("rgzbn_users", values, "_id = ?",
                                        new String[]{old_id});

                            }
                        } catch (Exception e) {
                        }


                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_users", jsonUsers );
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckUsersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "check " + res);
                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("sync_app", "CheckClientsData пусто");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_users");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check","0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and sync=? and name_table = ?",
                                                new String[]{new_id, "0", "rgzbn_users"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }

                    } catch (Exception e) {
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_users", check_users);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendMountersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "SendMountersData " + res);

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_mounters", jsonMounters );
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class SendClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("sync_app", "SendClientsContactsData пусто ");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients_contacts");
                            for (int i = 0; i < id_array.length(); i++) {

                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                String sqlQuewy = "SELECT * "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync = ? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_clients_contacts"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, values,
                                                    "_id = ?", new String[]{old_id});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_SYNC, "1");
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ?",
                                                    new String[]{old_id,"rgzbn_gm_ceiling_clients_contacts", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_OLD, old_id);
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                                            values.put(DBHelper.KEY_SYNC, "0");
                                            values.put(DBHelper.KEY_TYPE, "check");
                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                            jsonObjectClient_Contacts.put("id", new_id);

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();
                            }
                            global_results = String.valueOf(jsonObjectClient_Contacts);

                            new CheckClientsContactsData().execute();
                        } catch (Exception e) {
                        }
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", jsonClient_Contacts );
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }

    }

    static class CheckClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if(res.equals("")) {
                        Log.d(TAG, "CheckClientsContactsData пусто ");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients_contacts");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_clients_contacts","0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id,"rgzbn_gm_ceiling_clients_contacts","0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }

                    } catch (Exception e) {
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", check_clients_contacts );

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendProjectsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "SendProjectData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_projects");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                jsonObjectProject.put("id", new_id);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{old_id});

                                String sqlQuewy = "SELECT * "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync=? and name_table=? and id_new=?";
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_projects", "0"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_SYNC, "1");
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? and id_new=?",
                                                    new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_projects", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_OLD, old_id);
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                            values.put(DBHelper.KEY_SYNC, "0");
                                            values.put(DBHelper.KEY_TYPE, "check");
                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                            // global_results = String.valueOf(jsonObjectProject);

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();

                                global_results = String.valueOf(jsonObjectProject);

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_calculations " +
                                            "where project_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_PROJECT_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "project_id = ?", new String[]{old_id});

                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //org.json.simple.JSONObject jsonObjectCalc = new org.json.simple.JSONObject();
                               //jsonCalc = "[";
                               //sqlQuewy = "SELECT * "
                               //        + "FROM rgzbn_gm_ceiling_calculations " +
                               //        "where project_id = ?";
                               //cursor = db.rawQuery(sqlQuewy, new String[]{new_id});
                               //if (cursor != null) {
                               //    if (cursor.moveToFirst()) {
                               //        do {
                               //            String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(0)));
                               //            String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                               //            //jsonObjectCalculation_id.put(status, status1);
                               //            //calculation_id.add(status1);
                               //            for (int j = 0; j < 50; j++) {
                               //                status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                               //                status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                               //                //Log.d("responce", "status " + status + " " + status1);
                               //                if (status.equals("_id")) {
                               //                    status = "android_id";
                               //                    jsonObjectCalculation.put(status, status1);
                               //                } else if (status.equals("calc_image")) {
                               //                } else if (status.equals("cut_image")) {
                               //                } else if (status1.equals("")) {
                               //                } else {
                               //                    jsonObjectCalculation.put(status, status1);
                               //                }
                               //            }
                               //            jsonCalc += String.valueOf(jsonObjectCalculation) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //}
                               //jsonCalc = jsonCalc.substring(0, jsonCalc.length() - 1) + "]";
                               //Log.d("responce", "jsonCalc " + jsonCalc);
                            }

                        } catch (Exception e) {
                        }
                    }
                    new CheckProjectsData().execute();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_projects", jsonProjects );
                    Log.d(TAG, String.valueOf(parameters));

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckProjectsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "CheckProjectData пусто");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_projects");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_projects", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_projects", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }

                    } catch (Exception e) {
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_projects", check_project);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendCalculationsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();
        org.json.simple.JSONObject jsonObjectCalculation = new org.json.simple.JSONObject();
        org.json.simple.JSONObject jsonObjectCalculation_image = new org.json.simple.JSONObject();
        org.json.simple.JSONObject jsonObjectComponents = new org.json.simple.JSONObject();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "SendCalculationData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        String old_id = "";
                        global_results = "[";
                        //components_diffusers = "[";
                        //components_ecola = "[";
                        //components_fixtures = "[";
                        //components_hoods = "[";
                        //components_pipes = "[";
                        //components_corn = "[";
                        //components_profil = "[";

                        new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_calculations");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                jsonObjectCalculation.put("id", new_id);
                                global_results += String.valueOf(jsonObjectCalculation) + ",";

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{old_id});

                                String sqlQuewy = "SELECT * "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync=? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_calculations"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_SYNC, "1");
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and id_new=?",
                                                    new String[]{old_id, "rgzbn_gm_ceiling_calculations", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and id_new=?",
                                                    new String[]{old_id, "rgzbn_gm_ceiling_calculations_cal", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and id_new=?",
                                                    new String[]{old_id, "rgzbn_gm_ceiling_calculations_cut", "0"});

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_OLD, old_id);
                                            values.put(DBHelper.KEY_ID_NEW, new_id);
                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations");
                                            values.put(DBHelper.KEY_SYNC, "0");
                                            values.put(DBHelper.KEY_TYPE, "check");
                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();

                                sqlQuewy = "SELECT _id, calc_image, cut_image "
                                        + "FROM rgzbn_gm_ceiling_calculations " +
                                        "where _id = ? ";
                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    do {
                                        String status = "android_id";
                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                        jsonObjectCalculation_image.put(status, status1);
                                        status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(1)));
                                        status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                                        jsonObjectCalculation_image.put(status, status1);
                                        status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(2)));
                                        status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
                                        jsonObjectCalculation_image.put(status, status1);

                                        jsonImage = String.valueOf(jsonObjectCalculation_image);
                                        new SendCalculation_ImageData().execute();

                                    } while (cursor.moveToNext());
                                }
                                cursor.close();

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_diffusers " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_diffusers " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {
                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N23_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N23_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N23_SIZE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N23_SIZE));
                               //            jsonObjectComponents.put(status, status1);
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_ecola " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_ecola " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {
                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N26_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N26_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N26_LAMP));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N26_LAMP));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_ecola += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_fixtures " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_fixtures " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {
                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N13_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N13_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N13_TYPE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N13_TYPE));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N13_SIZE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N13_SIZE));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_hoods " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_hoods " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {
                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N22_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N22_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N22_TYPE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N22_TYPE));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N22_SIZE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N22_SIZE));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_hoods += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_pipes " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();

                                    components_pipes = String.valueOf(jsonObjectComponents);
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_pipes " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {
                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N14_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N14_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N14_SIZE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N14_SIZE));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_pipes += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_cornice " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_cornice " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {

                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N15_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N15_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N15_TYPE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N15_TYPE));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N15_SIZE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N15_SIZE));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_corn += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_profil " +
                                            "where calculation_id = ? ";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        do {
                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_CALCULATION_ID, new_id);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, values, "calculation_id = ?", new String[]{old_id});
                                        } while (cursor.moveToNext());
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                               //jsonObjectComponents = new org.json.simple.JSONObject();
                               //try {
                               //    sqlQuewy = "SELECT * "
                               //            + "FROM rgzbn_gm_ceiling_profil " +
                               //            "where calculation_id = ? ";
                               //    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                               //    if (cursor != null) {
                               //        cursor.moveToFirst();
                               //        do {

                               //            String status = "android_id";
                               //            String status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N29_COUNT));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N29_COUNT));
                               //            jsonObjectComponents.put(status, status1);
                               //            status = cursor.getColumnName(cursor.getColumnIndex(DBHelper.KEY_N29_TYPE));
                               //            status1 = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_N29_TYPE));
                               //            jsonObjectComponents.put(status, status1);
                               //            components_profil += String.valueOf(jsonObjectComponents) + ",";
                               //        } while (cursor.moveToNext());
                               //    }
                               //    cursor.close();
                               //} catch (Exception e) {
                               //}
                            }


                           //global_results = global_results.substring(0, global_results.length() - 1) + "]";
                           //components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
                           //components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
                           //components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
                           //components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
                           //components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
                           //components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
                           //components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";

                           //Log.d("responce", global_results);
                           //Log.d("responce", components_diffusers);
                           //Log.d("responce", components_fixtures);
                           //Log.d("responce", components_corn);
                           //Log.d("responce", components_ecola);
                           //Log.d("responce", components_hoods);
                           //Log.d("responce", components_pipes);
                           //Log.d("responce", "SENDCALCULATIONS GOOD");
                            //
                            //new SendComponents().execute();
                            new CheckCalculationData().execute();

                        } catch (Exception e) {
                        }
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_calculations", jsonCalc);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckCalculationData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "CheckCalculationData пусто");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_calculations");
                        for (int i = 0; i < id_array.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_calculations", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_calculations", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }
                    } catch (Exception e) {
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_calculations", check_calculation );

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendCalculation_ImageData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.addImagesFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG,"res " + res);
                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "SendCalculationData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            res = res.substring(1, res.length() - 1);

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and sync = ? and (name_table = ? or name_table = ?)";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "0",
                                    "rgzbn_gm_ceiling_calculations_cal","rgzbn_gm_ceiling_calculations_cut"});
                            if (cursor != null) {
                                cursor.moveToFirst();
                                do {

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_SYNC, "1");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=?",
                                            new String[]{res, "rgzbn_gm_ceiling_calculations_cal"});

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_SYNC, "1");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=?",
                                            new String[]{res, "rgzbn_gm_ceiling_calculations_cut"});

                                } while (cursor.moveToNext());
                            }
                            cursor.close();

                        } catch (Exception e) {
                        }
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "error " + error);

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("calculation_images", jsonImage);

                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendComponents extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();
        org.json.simple.JSONObject jsonObjectComponents = new org.json.simple.JSONObject();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "SendComponents пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            components_diffusers = "[";
                            table_name = "rgzbn_gm_ceiling_diffusers";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_diffusers += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";

                        try {
                            components_pipes = "[";
                            table_name = "rgzbn_gm_ceiling_pipes";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_PIPES;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_pipes += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";

                        try {
                            components_hoods = "[";
                            table_name = "rgzbn_gm_ceiling_hoods";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_HOODS;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_hoods += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";

                        try {
                            components_ecola = "[";
                            table_name = "rgzbn_gm_ceiling_ecola";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_ecola += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";

                        try {
                            components_corn = "[";
                            table_name = "rgzbn_gm_ceiling_cornice";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_corn += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";

                        try {
                            components_fixtures = "[";
                            table_name = "rgzbn_gm_ceiling_fixtures";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";

                        try {
                            components_corn = "[";
                            table_name = "rgzbn_gm_ceiling_profil";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_profil += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";

                        try {
                            components_profil = "[";
                            table_name = "rgzbn_gm_ceiling_profil";
                            table_name_title = DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL;
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray(table_name);
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, table_name,"0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, table_name);
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(table_name_title, values, "_id = ?",
                                        new String[]{old_id});

                                jsonObjectComponents.put("id", new_id);
                                components_profil += String.valueOf(jsonObjectComponents) + ",";
                            }
                        } catch (Exception e) {
                        }
                        components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";

                        new CheckComponents().execute();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_diffusers", components_diffusers);
                    parameters.put("rgzbn_gm_ceiling_fixtures", components_fixtures);
                    parameters.put("rgzbn_gm_ceiling_cornice", components_corn);
                    parameters.put("rgzbn_gm_ceiling_ecola", components_ecola);
                    parameters.put("rgzbn_gm_ceiling_hoods", components_hoods);
                    parameters.put("rgzbn_gm_ceiling_pipes", components_pipes);
                    parameters.put("rgzbn_gm_ceiling_profil", components_profil);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckComponents extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://"+domen+".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
        Map<String, String> parameters = new HashMap<String, String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // try {

            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    Log.d(TAG, "comp " + res);
                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "CheckComponents пусто");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        table_name = "rgzbn_gm_ceiling_diffusers";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_fixtures";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_cornice";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_ecola";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_hoods";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_pipes";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        table_name = "rgzbn_gm_ceiling_profil";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                    try {
                        table_name = "rgzbn_gm_ceiling_profil";
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray(table_name);
                        for (int i = 0; i < id_array.length(); i++) {
                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");
                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync = ? ";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", table_name, "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and type=? and sync=?",
                                                new String[]{new_id, table_name, "check", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_diffusers", components_diffusers);
                    parameters.put("rgzbn_gm_ceiling_fixtures", components_fixtures);
                    parameters.put("rgzbn_gm_ceiling_cornice", components_corn);
                    parameters.put("rgzbn_gm_ceiling_ecola", components_ecola);
                    parameters.put("rgzbn_gm_ceiling_hoods", components_hoods);
                    parameters.put("rgzbn_gm_ceiling_pipes", components_pipes);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

}
