package ru.ejevikaapp.gm_android;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

import ru.ejevikaapp.gm_android.Class.HelperClass;

public class Service_Sync extends Service {
    private static final String TAG = "responce_send";
    static DBHelper dbHelper;

    static String SAVED_ID = "[", id_cl, id_project, phone, fio, pro_info, calc_date, dealer_id, item, S, P, table_name, table_name_title, components_diffusers = "[",
            components_fixtures = "[", components_corn = "[", components_ecola = "[", components_hoods = "[", components_pipes = "[", check_project = "[", check_calculation = "[",
            components_profil = "[", check_components_diffusers = "[", check_components_fixtures = "[", check_components_corn = "[", check_components_ecola = "[",
            check_components_hoods = "[", check_components_pipes = "[", check_components_profil = "[", check_History = "[", check_ApiPhones = "[", checkClientHistory = "[",
            checkCallback = "[", checkRecoil = "[";

    static String jsonProjects = "[", jsonCalc = "[", jsonImage = "[", jsonClient = "[", jsonClient_Contacts = "[", jsonFixtures = "[", jsonEcola = "[", jsonCornice = "[",
            jsonPipes = "[", jsonHoods = "[", jsonDiffusers = "[", global_results = "[", check_client = "[", check_clients_contacts = "[", check_users = "[", jsonUsers = "[",
            jsonUserGroup = "[", jsonMounters = "[", check_mounters = "[", mounters_map = "[", jsonDealer_info = "[", jsonMount = "[", jsonHistory = "[", jsonApiPhones = "[",
            jsonClientHistory = "[", jsonCallback = "[", jsonDelete = "[", jsonDeleteTable = "[", jsonRecoil = "[";

    static org.json.simple.JSONObject jsonObjectClient = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonObjectClient_Contacts = new org.json.simple.JSONObject();
    static JSONObject jsonObjectProject = new JSONObject();
    static org.json.simple.JSONObject jsonObjectCalculation = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonObjectComponents = new org.json.simple.JSONObject();
    static JSONObject jsonObjectUsers = new JSONObject();
    static JSONObject jsonObjectUserGroup = new JSONObject();
    static JSONObject jsonObjectMounters = new JSONObject();
    static JSONObject jsonObjectMounters_map = new JSONObject();
    static JSONObject jsonObjectDealer_info = new JSONObject();
    static JSONObject jsonObjectMount = new JSONObject();
    static JSONObject jsonObjectClientHistory = new JSONObject();
    static JSONObject jsonObjectCallback = new JSONObject();

    static RequestQueue requestQueue;

    static String domen;

    static Context ctx;

    static int gager_id_int = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Service started!");

        ctx = Service_Sync.this;
        SharedPreferences SP = getSharedPreferences("link", MODE_PRIVATE);
        domen = SP.getString("", "");

        SP = getSharedPreferences("sync", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "0");
        ed.commit();

        int count_line = 0;
        dbHelper = new DBHelper(this);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String sqlQuewy = "SELECT _id, id_old, name_table "
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
        } catch (Exception e) {
        }

        if (!isRunning(this)) {
        } else if (count_line > 0) {
            delete();
            SP = getSharedPreferences("gager_id", MODE_PRIVATE);
            String gager_id = SP.getString("", "");
            if (gager_id.length() < 5) {
                gager_id_int = Integer.parseInt(gager_id) * 100000;
            } else {
                gager_id_int = Integer.parseInt(gager_id);
            }
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
            } catch (Exception e) {
            }

            Log.d(TAG, "--------------------------КЛИЕНТ------------------------");
            //клиент send
            jsonClient = "[";
            String sqlQuewy = "SELECT id_old "
                    + "FROM history_send_to_server " +
                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
            Cursor cursor = db.rawQuery(sqlQuewy,
                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                            "send", "0", "rgzbn_gm_ceiling_clients", "1"});
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
                                        for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_clients"); j++) {
                                            String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                            String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                            if (j == 0) {
                                                status = "android_id";
                                            }
                                            if (status1 == null || status1.equals("") || (status1.equals("null"))) {
                                            } else {
                                                jsonObjectClient.put(status, status1);
                                            }
                                        }
                                        jsonClient += String.valueOf(jsonObjectClient) + ",";
                                    } while (cursor.moveToNext());
                                } else {
                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_clients"});
                                }
                            }
                            cursor.close();
                        } catch (Exception e) {
                        }
                    } while (cursor.moveToNext());
                } else {
                    //клиент check
                    check_client = "[";
                    sqlQuewy = "SELECT id_new "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
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
                    if (check_client.equals("]")) {
                    } else {
                        new CheckClientsData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------контакты------------------------");
                    //контакты send
                    jsonClient_Contacts = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_clients_contacts", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
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
                                    } else {
                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_clients_contacts"});
                                        check_clients_contacts = "[";
                                    }
                                }
                                cursor.close();
                            } while (cursor.moveToNext());
                        } else {
                            sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
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
                            if (check_clients_contacts.equals("]")) {
                            } else {
                                new CheckClientsContactsData().execute();
                            }
                            cursor.close();
                        }
                    }
                    jsonClient_Contacts = jsonClient_Contacts.substring(0, jsonClient_Contacts.length() - 1) + "]";
                    if (jsonClient_Contacts.equals("]")) {
                    } else {
                        new SendClientsContactsData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------проект------------------------");
                    //проект send
                    jsonProjects = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>? and id_old<?) or id_old<?)  and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_projects", "1"});
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
                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_projects"); j++) {
                                                String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                if (j == 0) {
                                                    status = "android_id";
                                                }
                                                if (status.equals("change_time")) {
                                                } else {
                                                    if (status1 == null) {
                                                        try {
                                                            jsonObjectClient.put(status, status1);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        if (status1.equals("") || status1.equals("null")) {
                                                        } else {
                                                            try {
                                                                jsonObjectClient.put(status, status1);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            jsonProjects += String.valueOf(jsonObjectClient) + ",";
                                        } while (c.moveToNext());
                                    } else {
                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_projects"});
                                    }
                                }
                                c.close();
                            } while (cursor.moveToNext());
                        } else {
                            //проект check
                            check_project = "[";
                            sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
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
                            if (check_project.equals("]")) {
                            } else {
                                new CheckProjectsData().execute();
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------калькулятор------------------------");
                            //калькулятор send
                            jsonCalc = "[";
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_calculations", "1"});
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
                                                    jsonObjectCalculation = new org.json.simple.JSONObject();
                                                    for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_calculations"); j++) {
                                                        try {
                                                            String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                            String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                            if (status.equals("_id")) {
                                                                status = "android_id";
                                                                jsonObjectCalculation.put(status, status1);
                                                            } else if (status.equals("calc_image")) {
                                                                status = "image";
                                                                status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                                jsonObjectCalculation.put(status, status1);
                                                            } else if (status.equals("cut_image")) {
                                                                status = "cut_image";
                                                                status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                                jsonObjectCalculation.put(status, status1);
                                                            } else if (status1.equals("")) {
                                                            } else if (status1.equals("null") || status1.equals(null) || status1 == null) {
                                                            } else {
                                                                jsonObjectCalculation.put(status, status1);
                                                            }
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                    jsonCalc += String.valueOf(jsonObjectCalculation) + ",";
                                                } while (c.moveToNext());
                                            } else {
                                                db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                        "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                        new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_calculations"});
                                            }
                                        }

                                    } while (cursor.moveToNext());
                                } else {
                                    //калькулятор check
                                    check_calculation = "[";
                                    sqlQuewy = "SELECT id_new "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
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
                                    if (check_calculation.equals("]")) {
                                    } else {
                                        new CheckCalculationData().execute();
                                    }
                                    cursor.close();

                                    components_diffusers = "[";
                                    components_ecola = "[";
                                    components_fixtures = "[";
                                    components_hoods = "[";
                                    components_pipes = "[";
                                    components_corn = "[";
                                    components_profil = "[";

                                    Log.d(TAG, "--------------------------компоненты_fix------------------------");
                                    //компоненты_fix send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_fixtures", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_fixtures"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_ecola------------------------");
                                    //компоненты_ecola send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_ecola", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_ecola"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_diff------------------------");
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_diffusers"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_hoods------------------------");
                                    //компоненты_hoods send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
                                                    "send", "0", "rgzbn_gm_ceiling_hoods", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_hoods"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_pipes------------------------");
                                    //компоненты_pipes send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_pipes", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_pipes"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_cornice------------------------");
                                    //компоненты_cornice send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_cornice", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_cornice"});
                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    Log.d(TAG, "--------------------------компоненты_profil------------------------");
                                    //компоненты_cornice send
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_profil", "1"});
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
                                                        if (c.moveToFirst()) {
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
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_profil"});

                                                        }
                                                    }
                                                    c.close();
                                                } catch (Exception e) {
                                                }

                                            } while (cursor.moveToNext());
                                        }
                                    }
                                    cursor.close();

                                    if (components_diffusers.length() < 3) {
                                        components_diffusers += "]";
                                    } else {
                                        components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
                                    }
                                    if (components_ecola.length() < 3) {
                                        components_ecola += "]";
                                    } else {
                                        components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
                                    }
                                    if (components_fixtures.length() < 3) {
                                        components_fixtures += "]";
                                    } else {
                                        components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
                                    }
                                    if (components_hoods.length() < 3) {
                                        components_hoods += "]";
                                    } else {
                                        components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
                                    }
                                    if (components_pipes.length() < 3) {
                                        components_pipes += "]";
                                    } else {
                                        components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
                                    }
                                    if (components_corn.length() < 3) {
                                        components_corn += "]";
                                    } else {
                                        components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
                                    }
                                    if (components_profil.length() < 3) {
                                        components_profil += "]";
                                    } else {
                                        components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";
                                    }

                                    if ((components_diffusers.equals("]") && components_ecola.equals("]") && components_fixtures.equals("]") && components_hoods.equals("]") &&
                                            components_pipes.equals("]") && components_corn.equals("]") && components_profil.equals("]")) ||
                                            (components_diffusers.equals("[]") && components_ecola.equals("[]") && components_fixtures.equals("[]") && components_hoods.equals("[]") &&
                                                    components_pipes.equals("[]") && components_corn.equals("[]") && components_profil.equals("[]"))) {
                                        //компоненты_fix check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_fixtures"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_diff check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_diffusers"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_diffusers += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_pipes check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_pipes"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_pipes += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_hoods check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_hoods"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_hoods += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_ecola check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_ecola"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_ecola += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_cornice check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_cornice"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_corn += String.valueOf(jsonObjectComponents) + ",";
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        //компоненты_cornice check
                                        sqlQuewy = "SELECT id_new "
                                                + "FROM history_send_to_server " +
                                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                        cursor = db.rawQuery(sqlQuewy,
                                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                        "check", "0", "rgzbn_gm_ceiling_profil"});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectComponents.put("id", id_new);
                                                    check_components_profil += String.valueOf(jsonObjectComponents) + ",";
                                                    new CheckCalculationData().execute();
                                                } while (cursor.moveToNext());
                                            }
                                        }
                                        cursor.close();

                                        check_components_diffusers = check_components_diffusers.substring(0, check_components_diffusers.length() - 1) + "]";
                                        check_components_ecola = check_components_ecola.substring(0, check_components_ecola.length() - 1) + "]";
                                        check_components_fixtures = check_components_fixtures.substring(0, check_components_fixtures.length() - 1) + "]";
                                        check_components_hoods = check_components_hoods.substring(0, check_components_hoods.length() - 1) + "]";
                                        check_components_pipes = check_components_pipes.substring(0, check_components_pipes.length() - 1) + "]";
                                        check_components_corn = check_components_corn.substring(0, check_components_corn.length() - 1) + "]";
                                        check_components_profil = check_components_profil.substring(0, check_components_profil.length() - 1) + "]";

                                        if ((check_components_diffusers.equals("]") && check_components_ecola.equals("]") && check_components_fixtures.equals("]")
                                                && check_components_hoods.equals("]") && check_components_pipes.equals("]") && check_components_corn.equals("]")
                                                && check_components_profil.equals("]"))
                                                || (check_components_diffusers.equals("[]") && check_components_ecola.equals("[]") && check_components_fixtures.equals("[]")
                                                && check_components_hoods.equals("[]") && check_components_pipes.equals("[]") && check_components_corn.equals("[]")
                                                && check_components_profil.equals("[]"))) {
                                        } else {
                                            new CheckComponents().execute();
                                        }

                                    } else {
                                        new SendComponents().execute();
                                    }

                                    check_components_diffusers = "[";
                                    check_components_ecola = "[";
                                    check_components_fixtures = "[";
                                    check_components_hoods = "[";
                                    check_components_pipes = "[";
                                    check_components_corn = "[";
                                    check_components_profil = "[";
                                }
                            }
                            jsonCalc = jsonCalc.substring(0, jsonCalc.length() - 1) + "]";
                            if (jsonCalc.equals("]")) {
                            } else {
                                new SendCalculationsData().execute();
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------ИСТОРИЯ------------------------");
                            //клиент send
                            jsonHistory = "[";
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_projects_history", "1"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                        try {
                                            sqlQuewy = "SELECT * "
                                                    + "FROM rgzbn_gm_ceiling_projects_history " +
                                                    "where _id = ?";
                                            cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                            if (cursor != null) {
                                                if (cursor.moveToFirst()) {
                                                    do {
                                                        JSONObject jsonObjectClient = new JSONObject();
                                                        for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_projects_history"); j++) {
                                                            String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                            String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                            if (j == 0) {
                                                                status = "android_id";
                                                            }
                                                            if (status1 == null || status1.equals("") || (status1.equals("null"))) {
                                                            } else {
                                                                jsonObjectClient.put(status, status1);
                                                            }
                                                        }
                                                        jsonHistory += String.valueOf(jsonObjectClient) + ",";
                                                    } while (cursor.moveToNext());
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_projects_history"});
                                                }
                                            }
                                            cursor.close();

                                        } catch (Exception e) {
                                            Log.d(TAG, String.valueOf(e));
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                            jsonHistory = jsonHistory.substring(0, jsonHistory.length() - 1) + "]";
                            Log.d(TAG, "jsonHistory = " + jsonHistory);
                            if (jsonHistory.equals("]")) {
                            } else {
                                new SendProjectsHistory().execute();
                            }

                            Log.d(TAG, "--------------------------RECOIL_MAP_PROJECT------------------------");
                            //клиент send
                            jsonRecoil = "[";
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_recoil_map_project", "1"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                        try {
                                            sqlQuewy = "SELECT * "
                                                    + "FROM rgzbn_gm_ceiling_recoil_map_project " +
                                                    "where _id = ?";
                                            cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                            if (cursor != null) {
                                                if (cursor.moveToFirst()) {
                                                    do {
                                                        JSONObject jsonObjectClient = new JSONObject();
                                                        for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_recoil_map_project"); j++) {
                                                            String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                            String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                            if (j == 0) {
                                                                status = "android_id";
                                                            }
                                                            if (status1 == null || (status1.equals("null"))) {
                                                            } else {
                                                                jsonObjectClient.put(status, status1);
                                                            }
                                                        }
                                                        jsonRecoil += String.valueOf(jsonObjectClient) + ",";
                                                    } while (cursor.moveToNext());
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_recoil_map_project"});
                                                }
                                            }
                                            cursor.close();
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                } else {

                                    checkRecoil = "[";
                                    sqlQuewy = "SELECT id_new "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "check", "0", "rgzbn_gm_ceiling_recoil_map_project"});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                try {
                                                    JSONObject jsonObjectClient = new JSONObject();
                                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                    jsonObjectClient.put("id", id_new);
                                                    checkRecoil += String.valueOf(jsonObjectClient) + ",";
                                                } catch (Exception e) {
                                                }
                                            } while (cursor.moveToNext());
                                        }
                                    }

                                    checkRecoil = checkRecoil.substring(0, checkRecoil.length() - 1) + "]";
                                    if (checkRecoil.equals("]")) {
                                    } else {
                                        new CheckRecoilMapProject().execute();
                                    }
                                    cursor.close();
                                }
                            }
                            cursor.close();
                            jsonRecoil = jsonRecoil.substring(0, jsonRecoil.length() - 1) + "]";
                            if (jsonRecoil.equals("]")) {
                            } else {
                                new SendRecoilMapProject().execute();
                            }
                        }
                    }
                    jsonProjects = jsonProjects.substring(0, jsonProjects.length() - 1) + "]";
                    if (jsonProjects.equals("]")) {
                    } else {
                        new SendProjectsData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------ИСТОРИЯ КЛИЕНТА------------------------");
                    //клиент send
                    jsonClientHistory = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_client_history", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_client_history " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                JSONObject jsonObjectClient = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_client_history"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }
                                                    if (status1 == null || (status1.equals("null"))) {
                                                    } else {
                                                        jsonObjectClient.put(status, status1);
                                                    }
                                                }
                                                jsonClientHistory += String.valueOf(jsonObjectClient) + ",";
                                            } while (cursor.moveToNext());
                                        } else {
                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send'",
                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_client_history"});
                                        }
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }
                            } while (cursor.moveToNext());
                        } else {
                            checkClientHistory = "[";
                            sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_client_history"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            checkClientHistory += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            checkClientHistory = checkClientHistory.substring(0, checkClientHistory.length() - 1) + "]";
                            if (checkClientHistory.equals("]")) {
                            } else {
                                new CheckClientHistory().execute();
                            }
                            cursor.close();
                        }
                    }
                    cursor.close();
                    jsonClientHistory = jsonClientHistory.substring(0, jsonClientHistory.length() - 1) + "]";
                    Log.d(TAG, "jsonClientHistory " + jsonClientHistory);
                    if (jsonClientHistory.equals("]")) {
                    } else {
                        new SendClientHistory().execute();
                    }

                    Log.d(TAG, "--------------------------CALLBACK------------------------");
                    //клиент send
                    jsonCallback = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_callback", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_callback " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                JSONObject jsonObjectClient = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_callback"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }
                                                    if (status1 == null || (status1.equals("null"))) {
                                                    } else {
                                                        jsonObjectClient.put(status, status1);
                                                    }
                                                }
                                                jsonCallback += String.valueOf(jsonObjectClient) + ",";
                                            } while (cursor.moveToNext());
                                        } else {
                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_callback"});
                                        }
                                    }
                                    cursor.close();
                                } catch (Exception e) {
                                }

                            } while (cursor.moveToNext());
                        } else {
                            checkCallback = "[";
                            sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_callback"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            checkCallback += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            checkCallback = checkCallback.substring(0, checkCallback.length() - 1) + "]";
                            Log.d(TAG, "checkCallback " + checkCallback);
                            if (checkCallback.equals("]")) {
                            } else {
                                new CheckCallback().execute();
                            }
                            cursor.close();
                        }
                    }
                    cursor.close();
                    jsonCallback = jsonCallback.substring(0, jsonCallback.length() - 1) + "]";
                    Log.d(TAG, "jsonCallback " + jsonCallback);
                    if (jsonCallback.equals("]")) {
                    } else {
                        new SendCallback().execute();
                    }

                }
            }
            jsonClient = jsonClient.substring(0, jsonClient.length() - 1) + "]";
            if (jsonClient.equals("]")) {
            } else {
                new SendClientData().execute();
            }
            cursor.close();

            Log.d(TAG, "--------------------------ЮЗЕРЫ------------------------");
            //клиент send
            jsonUsers = "[";
            sqlQuewy = "SELECT id_old "
                    + "FROM history_send_to_server " +
                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
            cursor = db.rawQuery(sqlQuewy,
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
                                        for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_users"); j++) {
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
                                            } catch (Exception e) {
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
                } else {
                    //клиент check
                    check_users = "[";
                    sqlQuewy = "SELECT id_new "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
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
                                } catch (Exception e) {
                                }
                            } while (cursor.moveToNext());
                        }
                    }
                    check_users = check_users.substring(0, check_users.length() - 1) + "]";
                    if (check_users.equals("]")) {
                    } else {
                        new CheckUsersData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------UserGroup------------------------");
                    //клиент send
                    jsonUserGroup = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_user_usergroup_map", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_user_usergroup_map " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                jsonObjectUserGroup = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_user_usergroup_map"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }
                                                    try {
                                                        if (status1.equals("") || (status1 == null)) {
                                                        } else {
                                                            jsonObjectUserGroup.put(status, status1);
                                                        }
                                                    } catch (Exception e) {
                                                    }

                                                    Log.d(TAG, j + " " + String.valueOf(jsonObjectUserGroup));
                                                }
                                                Log.d(TAG, " end " + String.valueOf(jsonObjectUserGroup));
                                                jsonUserGroup += String.valueOf(jsonObjectUserGroup) + ",";
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
                    jsonUserGroup = jsonUserGroup.substring(0, jsonUserGroup.length() - 1) + "]";

                    if (jsonUserGroup.equals("]") || jsonUserGroup.equals("[]") || jsonUserGroup.equals("[")) {
                    } else {
                        new CheckUserGroupData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------МОНТАЖНИКИ------------------------");
                    //монтажники send
                    jsonMounters = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
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
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_mounters"); j++) {
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
                                                    } catch (Exception e) {
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
                        } else {
                            //монтажники check
                            check_mounters = "[";
                            sqlQuewy = "SELECT id_new "
                             + "FROM history_send_to_server " +
                             "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_mounters"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectMounters = new org.json.JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectMounters.put("id", id_new);
                                            check_mounters += String.valueOf(jsonObjectMounters) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            check_mounters = check_mounters.substring(0, check_mounters.length() - 1) + "]";
                            if (check_mounters.equals("]")) {
                            } else {
                                new CheckMountersData().execute();
                            }
                            cursor.close();

                            //
                            mounters_map = "[";
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_mounters_map", "1"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                            sqlQuewy = "SELECT _id, id_mounter, id_brigade "
                                                    + "FROM rgzbn_gm_ceiling_mounters_map " +
                                                    "where id_mounter = ?";
                                            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                            if (c != null) {
                                                if (c.moveToFirst()) {
                                                    do {
                                                        jsonObjectMounters_map = new JSONObject();

                                                        String status = "";
                                                        String status1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                                        jsonObjectMounters_map.put("android_id", status1);

                                                        status = c.getColumnName(c.getColumnIndex(c.getColumnName(1)));
                                                        status1 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                                        jsonObjectMounters_map.put(status, status1);

                                                        status = c.getColumnName(c.getColumnIndex(c.getColumnName(2)));
                                                        status1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                                        jsonObjectMounters_map.put(status, status1);

                                                        mounters_map += String.valueOf(jsonObjectMounters_map) + ",";
                                                    } while (c.moveToNext());
                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            mounters_map = mounters_map.substring(0, mounters_map.length() - 1) + "]";
                            if (mounters_map.equals("]")) {
                            } else {
                                new SendMounters_mapData().execute();
                            }
                            cursor.close();
                        }
                    }
                    jsonMounters = jsonMounters.substring(0, jsonMounters.length() - 1) + "]";
                    if (jsonMounters.equals("]")) {
                    } else {
                        new SendMountersData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------DEALER_INFO------------------------");
                    //клиент send
                    jsonDealer_info = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_dealer_info", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_dealer_info " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                jsonObjectDealer_info = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_dealer_info"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }

                                                    try {
                                                        if (status1.equals("") || (status1 == null)) {
                                                        } else {
                                                            jsonObjectDealer_info.put(status, status1);
                                                        }
                                                    } catch (Exception e) {
                                                    }

                                                    Log.d(TAG, j + " " + String.valueOf(jsonObjectDealer_info));
                                                }
                                                Log.d(TAG, " end " + String.valueOf(jsonObjectDealer_info));
                                                jsonDealer_info += String.valueOf(jsonObjectDealer_info) + ",";
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
                    jsonDealer_info = jsonDealer_info.substring(0, jsonDealer_info.length() - 1) + "]";

                    if (jsonDealer_info.equals("]")) {
                    } else {
                        new SendDealerData().execute();
                    }

                    cursor.close();

                    Log.d(TAG, "--------------------------MOUNT------------------------");
                    //клиент send
                    jsonMount = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_mount", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_mount " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                jsonObjectMount = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_mount"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }

                                                    try {
                                                        if (status1.equals("") || (status1 == null)) {
                                                        } else {
                                                            jsonObjectMount.put(status, status1);
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                }
                                                jsonMount += String.valueOf(jsonObjectMount) + ",";
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
                    jsonMount = jsonMount.substring(0, jsonMount.length() - 1) + "]";

                    if (jsonMount.equals("]")) {
                    } else {
                        new SendMountData().execute();
                    }

                    cursor.close();
                }
            }
            cursor.close();
            jsonUsers = jsonUsers.substring(0, jsonUsers.length() - 1) + "]";
            if (jsonUsers.equals("]") || jsonUsers.equals("[]") || jsonUsers.equals("[")) {
            } else {
                new SendUsersData().execute();
            }

            Log.d(TAG, "--------------------------РЕКЛАМА------------------------");
            //клиент send
            jsonApiPhones = "[";
            sqlQuewy = "SELECT id_old "
                    + "FROM history_send_to_server " +
                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
            cursor = db.rawQuery(sqlQuewy,
                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                            "send", "0", "rgzbn_gm_ceiling_api_phones", "1"});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                        try {
                            sqlQuewy = "SELECT * "
                                    + "FROM rgzbn_gm_ceiling_api_phones " +
                                    "where _id = ?";
                            cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        JSONObject jsonObjectClient = new JSONObject();
                                        for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_api_phones"); j++) {
                                            String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                            String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                            if (j == 0) {
                                                status = "android_id";
                                            }
                                            if (status1 == null || (status1.equals("null"))) {
                                            } else {
                                                jsonObjectClient.put(status, status1);
                                            }
                                        }
                                        jsonApiPhones += String.valueOf(jsonObjectClient) + ",";
                                    } while (cursor.moveToNext());
                                } else {
                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_api_phones"});
                                }
                            }
                            cursor.close();
                        } catch (Exception e) {
                            Log.d(TAG, String.valueOf(e));
                        }
                    } while (cursor.moveToNext());
                } else {
                    check_ApiPhones = "[";
                    sqlQuewy = "SELECT id_new "
                     + "FROM history_send_to_server " +
                     "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "check", "0", "rgzbn_gm_ceiling_api_phones"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                try {
                                    jsonObjectUsers = new JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectUsers.put("id", id_new);
                                    check_ApiPhones += String.valueOf(jsonObjectUsers) + ",";
                                } catch (Exception e) {
                                }
                            } while (cursor.moveToNext());
                        }
                    }
                    check_ApiPhones = check_ApiPhones.substring(0, check_ApiPhones.length() - 1) + "]";
                    if (check_ApiPhones.equals("]")) {
                    } else {
                        new CheckApiPhones().execute();
                    }
                    cursor.close();
                }
            }

            cursor.close();
            jsonApiPhones = jsonApiPhones.substring(0, jsonApiPhones.length() - 1) + "]";
            if (jsonApiPhones.equals("]")) {
            } else {
                new SendApiPhones().execute();
            }

            Log.d(TAG, "--------------------------DELETE------------------------");
            //клиент send
            jsonDelete = "[";
            sqlQuewy = "SELECT id_old, name_table "
                    + "FROM history_send_to_server " +
                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and status=?";
            cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int),
                    String.valueOf(gager_id_int + 999999), String.valueOf(999999), "delete", "0", "1"});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                    String name_table = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                    jsonDeleteTable = name_table;
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", id_old);
                        jsonDelete += String.valueOf(jsonObject);
                    } catch (Exception e) {
                    }
                }
            }
            cursor.close();

            jsonDelete = jsonDelete.substring(0, jsonDelete.length()) + "]";
            if (jsonDelete.equals("[]")) {
            } else {
                new SendDeleteTable().execute();
            }

        } else {
        }

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

    static void delete() {

        try {
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();

            int count_line = 0;
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

            int count_line_sync = 0;
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

            if (count_line == count_line_sync) {
                db.delete(DBHelper.HISTORY_SEND_TO_SERVER, null, null);
            }
        } catch (Exception e) {
        }
    }

    static class SendClientData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("") || res.equals("\"\u041e\u0448\u0438\u0431\u043a\u0430!\"")) {
                        Log.d("sync_app", "SendClientData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values;
                        String new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients");
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, values, "client_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, values, "client_id = ?", new String[]{old_id});

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

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, values, "client_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "client_id = ?", new String[]{old_id});
                            }

                        } catch (Exception e) {
                        }

                        //клиент check
                        check_client = "[";
                        String sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
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
                        if (check_client.equals("]")) {
                        } else {
                            new CheckClientsData().execute();
                        }
                        cursor.close();

                        Log.d(TAG, "--------------------------контакты------------------------");
                        //контакты send
                        jsonClient_Contacts = "[";
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_clients_contacts", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
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
                                        } else {
                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_clients_contacts"});
                                        }
                                    }
                                    cursor.close();
                                } while (cursor.moveToNext());
                            }
                        }
                        jsonClient_Contacts = jsonClient_Contacts.substring(0, jsonClient_Contacts.length() - 1) + "]";
                        if (jsonClient_Contacts.equals("]")) {
                        } else {
                            new SendClientsContactsData().execute();
                        }
                        cursor.close();

                        Log.d(TAG, "--------------------------проект------------------------");
                        //проект send
                        jsonProjects = "[";
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>? and id_old<?) or id_old<?)  and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_projects", "1"});
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
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_projects"); j++) {
                                                    String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                    String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }
                                                    if (status.equals("change_time")) {
                                                    } else {
                                                        if (status1 == null) {
                                                            try {
                                                                jsonObjectClient.put(status, status1);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            if (status1.equals("") || status1.equals("null")) {
                                                            } else {
                                                                try {
                                                                    jsonObjectClient.put(status, status1);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                jsonProjects += String.valueOf(jsonObjectClient) + ",";
                                            } while (c.moveToNext());
                                        } else {
                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_projects"});
                                        }
                                    }
                                    c.close();
                                } while (cursor.moveToNext());
                            }
                        }
                        jsonProjects = jsonProjects.substring(0, jsonProjects.length() - 1) + "]";
                        if (jsonProjects.equals("]")) {
                        } else {
                            new SendProjectsData().execute();
                        }
                        cursor.close();
                        Log.d(TAG, "--------------------------ИСТОРИЯ КЛИЕНТА------------------------");
                        //клиент send
                        jsonClientHistory = "[";
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_client_history", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_client_history " +
                                                "where _id = ?";
                                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    JSONObject jsonObjectClient = new JSONObject();
                                                    for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_client_history"); j++) {
                                                        String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                        if (j == 0) {
                                                            status = "android_id";
                                                        }
                                                        if (status1 == null || (status1.equals("null"))) {
                                                        } else {
                                                            jsonObjectClient.put(status, status1);
                                                        }
                                                    }
                                                    jsonClientHistory += String.valueOf(jsonObjectClient) + ",";
                                                } while (cursor.moveToNext());
                                            } else {
                                                db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                        "id_old = ? and name_table = ? and sync = 0 and type = 'send'",
                                                        new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_client_history"});
                                            }
                                        }
                                        cursor.close();
                                    } catch (Exception e) {
                                    }
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();
                        jsonClientHistory = jsonClientHistory.substring(0, jsonClientHistory.length() - 1) + "]";
                        Log.d(TAG, "jsonClientHistory " + jsonClientHistory);
                        if (jsonClientHistory.equals("]")) {
                        } else {
                            new SendClientHistory().execute();
                        }

                        Log.d(TAG, "--------------------------CALLBACK------------------------");
                        //клиент send
                        jsonCallback = "[";
                        sqlQuewy = "SELECT id_old "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "send", "0", "rgzbn_gm_ceiling_callback", "1"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    try {
                                        sqlQuewy = "SELECT * "
                                                + "FROM rgzbn_gm_ceiling_callback " +
                                                "where _id = ?";
                                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                        if (cursor != null) {
                                            if (cursor.moveToFirst()) {
                                                do {
                                                    JSONObject jsonObjectClient = new JSONObject();
                                                    for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_callback"); j++) {
                                                        String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                        if (j == 0) {
                                                            status = "android_id";
                                                        }
                                                        if (status1 == null || (status1.equals("null"))) {
                                                        } else {
                                                            jsonObjectClient.put(status, status1);
                                                        }
                                                    }
                                                    jsonCallback += String.valueOf(jsonObjectClient) + ",";
                                                } while (cursor.moveToNext());
                                            } else {
                                                db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                        "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                        new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_callback"});
                                            }
                                        }
                                        cursor.close();
                                    } catch (Exception e) {
                                    }

                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();
                        jsonCallback = jsonCallback.substring(0, jsonCallback.length() - 1) + "]";
                        Log.d(TAG, "jsonCallback " + jsonCallback);
                        if (jsonCallback.equals("]")) {
                        } else {
                            new SendCallback().execute();
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
                    parameters.put("rgzbn_gm_ceiling_clients", jsonClient);
                    return parameters;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }

    static class CheckClientsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and sync=?",
                                                new String[]{new_id, "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }

                    } catch (Exception e) {
                    }

                    delete();
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


    static class SendClientHistory extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    Log.d(TAG, "SendClientHistory " + res);

                    if (res.equals("")) {
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_client_history");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=?",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_client_history"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_client_history");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                            }


                            checkClientHistory = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_client_history"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            checkClientHistory += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            checkClientHistory = checkClientHistory.substring(0, checkClientHistory.length() - 1) + "]";
                            if (checkClientHistory.equals("]")) {
                            } else {
                                new CheckClientHistory().execute();
                            }
                            cursor.close();


                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_client_history", jsonClientHistory);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

    static class CheckClientHistory extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    Log.d(TAG, "rgzbn_gm_ceiling_client_history = " + res);

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_client_history");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_client_history", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_client_history", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_client_history", checkClientHistory);
                    Log.d(TAG, "rgzbn_gm_ceiling_client_history " + parameters);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }


    static class SendCallback extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("")) {

                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_callback");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? and id_new=?",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_callback", "0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, values, "_id = ?",
                                        new String[]{String.valueOf(old_id)});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_callback");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                            }

                            checkCallback = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_callback"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            checkCallback += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            checkCallback = checkCallback.substring(0, checkCallback.length() - 1) + "]";
                            Log.d(TAG, "checkCallback " + checkCallback);
                            if (checkCallback.equals("]")) {
                            } else {
                                new CheckCallback().execute();
                            }
                            cursor.close();

                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_callback", jsonCallback);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

    static class CheckCallback extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_callback");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_callback", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_callback", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_callback", checkCallback);
                    Log.d(TAG, "checkCallback " + parameters);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }


    static class SendUsersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("")) {
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
                                        new String[]{old_id, "rgzbn_users", "0"});

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
                                db.update(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, values, "user_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update("rgzbn_users", values, "_id = ?",
                                        new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_DEALER_ID, new_id);
                                db.update("rgzbn_gm_ceiling_dealer_info", values, "dealer_id = ?",
                                        new String[]{old_id});

                                //клиент check
                                check_users = "[";
                                String sqlQuewy = "SELECT id_new "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy,
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
                                            } catch (Exception e) {
                                            }
                                        } while (cursor.moveToNext());
                                    }
                                }
                                check_users = check_users.substring(0, check_users.length() - 1) + "]";
                                if (check_users.equals("]")) {
                                } else {
                                    new CheckUsersData().execute();
                                }
                                cursor.close();

                                Log.d(TAG, "--------------------------UserGroup------------------------");
                                //клиент send
                                jsonUserGroup = "[";
                                sqlQuewy = "SELECT id_old "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                cursor = db.rawQuery(sqlQuewy,
                                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                "send", "0", "rgzbn_user_usergroup_map", "1"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            try {
                                                sqlQuewy = "SELECT * "
                                                        + "FROM rgzbn_user_usergroup_map " +
                                                        "where _id = ?";
                                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                                if (cursor != null) {
                                                    if (cursor.moveToFirst()) {
                                                        do {
                                                            jsonObjectUserGroup = new JSONObject();
                                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_user_usergroup_map"); j++) {
                                                                String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                if (j == 0) {
                                                                    status = "android_id";
                                                                }
                                                                try {
                                                                    if (status1.equals("") || (status1 == null)) {
                                                                    } else {
                                                                        jsonObjectUserGroup.put(status, status1);
                                                                    }
                                                                } catch (Exception e) {
                                                                }

                                                                Log.d(TAG, j + " " + String.valueOf(jsonObjectUserGroup));
                                                            }
                                                            Log.d(TAG, " end " + String.valueOf(jsonObjectUserGroup));
                                                            jsonUserGroup += String.valueOf(jsonObjectUserGroup) + ",";
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
                                jsonUserGroup = jsonUserGroup.substring(0, jsonUserGroup.length() - 1) + "]";

                                if (jsonUserGroup.equals("]") || jsonUserGroup.equals("[]") || jsonUserGroup.equals("[")) {
                                } else {
                                    new CheckUserGroupData().execute();
                                }

                                cursor.close();

                                Log.d(TAG, "--------------------------МОНТАЖНИКИ------------------------");
                                //монтажники send
                                jsonMounters = "[";
                                sqlQuewy = "SELECT id_old "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                cursor = db.rawQuery(sqlQuewy,
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
                                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_mounters"); j++) {
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
                                                                } catch (Exception e) {
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
                                if (jsonMounters.equals("]")) {
                                } else {
                                    new SendMountersData().execute();
                                }
                                cursor.close();

                                Log.d(TAG, "--------------------------DEALER_INFO------------------------");
                                //клиент send
                                jsonDealer_info = "[";
                                sqlQuewy = "SELECT id_old "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                cursor = db.rawQuery(sqlQuewy,
                                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                "send", "0", "rgzbn_gm_ceiling_dealer_info", "1"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                            try {
                                                sqlQuewy = "SELECT * "
                                                        + "FROM rgzbn_gm_ceiling_dealer_info " +
                                                        "where _id = ?";
                                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                                if (cursor != null) {
                                                    if (cursor.moveToFirst()) {
                                                        do {
                                                            jsonObjectDealer_info = new JSONObject();
                                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_dealer_info"); j++) {
                                                                String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                if (j == 0) {
                                                                    status = "android_id";
                                                                }

                                                                try {
                                                                    if (status1.equals("") || (status1 == null)) {
                                                                    } else {
                                                                        jsonObjectDealer_info.put(status, status1);
                                                                    }
                                                                } catch (Exception e) {
                                                                }

                                                                Log.d(TAG, j + " " + String.valueOf(jsonObjectDealer_info));
                                                            }
                                                            Log.d(TAG, " end " + String.valueOf(jsonObjectDealer_info));
                                                            jsonDealer_info += String.valueOf(jsonObjectDealer_info) + ",";
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
                                jsonDealer_info = jsonDealer_info.substring(0, jsonDealer_info.length() - 1) + "]";

                                if (jsonDealer_info.equals("]")) {
                                } else {
                                    new SendDealerData().execute();
                                }

                                cursor.close();

                                Log.d(TAG, "--------------------------MOUNT------------------------");
                                //клиент send
                                jsonMount = "[";
                                sqlQuewy = "SELECT id_old "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                cursor = db.rawQuery(sqlQuewy,
                                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                "send", "0", "rgzbn_gm_ceiling_mount", "1"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                            try {
                                                sqlQuewy = "SELECT * "
                                                        + "FROM rgzbn_gm_ceiling_mount " +
                                                        "where _id = ?";
                                                cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                                if (cursor != null) {
                                                    if (cursor.moveToFirst()) {
                                                        do {
                                                            jsonObjectMount = new JSONObject();
                                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_mount"); j++) {
                                                                String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                if (j == 0) {
                                                                    status = "android_id";
                                                                }

                                                                try {
                                                                    if (status1.equals("") || (status1 == null)) {
                                                                    } else {
                                                                        jsonObjectMount.put(status, status1);
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                            jsonMount += String.valueOf(jsonObjectMount) + ",";
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
                                jsonMount = jsonMount.substring(0, jsonMount.length() - 1) + "]";

                                if (jsonMount.equals("]")) {
                                } else {
                                    new SendMountData().execute();
                                }

                                cursor.close();

                            }
                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_users", jsonUsers);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckUsersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "0"});
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

                    delete();
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


    static class CheckUserGroupData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    Log.d(TAG, "SendUserGroupData " + res);

                    if (res.equals("")) {
                    } else {

                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_user_usergroup_map");
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, "rgzbn_user_usergroup_map", "0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, values, "_id = ?",
                                        new String[]{old_id});
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "error " + e);
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
                    parameters.put("rgzbn_user_usergroup_map", jsonUserGroup);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendMountersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("")) {
                    } else {

                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_mounters");
                            for (int i = 0; i < dat.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                new_id = client_contact.getString("new_id");

                                Log.d(TAG, new_id + " " + old_id);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=? and sync = ? ",
                                        new String[]{old_id, "rgzbn_gm_ceiling_mounters", "0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mounters");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, values, "_id = ?",
                                        new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_MOUNTER, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, values, "id_mounter = ?",
                                        new String[]{old_id});

                                //монтажники check
                                check_mounters = "[";
                                String sqlQuewy = "SELECT id_new "
                                        + "FROM history_send_to_server " +
                                        "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy,
                                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                "check", "0", "rgzbn_gm_ceiling_mounters"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            try {
                                                jsonObjectMounters = new org.json.JSONObject();
                                                String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                                jsonObjectMounters.put("id", id_new);
                                                check_mounters += String.valueOf(jsonObjectMounters) + ",";
                                            } catch (Exception e) {
                                            }
                                        } while (cursor.moveToNext());
                                    }
                                }
                                check_mounters = check_mounters.substring(0, check_mounters.length() - 1) + "]";
                                if (check_mounters.equals("]")) {
                                } else {
                                    new CheckMountersData().execute();
                                }
                                cursor.close();

                                //
                                mounters_map = "[";
                                sqlQuewy = "SELECT id_old "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync=? and name_table=? and status=?";
                                cursor = db.rawQuery(sqlQuewy,
                                        new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                "send", "0", "rgzbn_gm_ceiling_mounters_map", "1"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            try {
                                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                                sqlQuewy = "SELECT _id, id_mounter, id_brigade "
                                                        + "FROM rgzbn_gm_ceiling_mounters_map " +
                                                        "where id_mounter = ?";
                                                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                                if (c != null) {
                                                    if (c.moveToFirst()) {
                                                        do {
                                                            jsonObjectMounters_map = new JSONObject();

                                                            String status = "";
                                                            String status1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                                            jsonObjectMounters_map.put("android_id", status1);

                                                            status = c.getColumnName(c.getColumnIndex(c.getColumnName(1)));
                                                            status1 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                                            jsonObjectMounters_map.put(status, status1);

                                                            status = c.getColumnName(c.getColumnIndex(c.getColumnName(2)));
                                                            status1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                                            jsonObjectMounters_map.put(status, status1);

                                                            mounters_map += String.valueOf(jsonObjectMounters_map) + ",";
                                                        } while (c.moveToNext());
                                                    }
                                                }
                                                c.close();
                                            } catch (Exception e) {
                                            }
                                        } while (cursor.moveToNext());
                                    }
                                }
                                mounters_map = mounters_map.substring(0, mounters_map.length() - 1) + "]";
                                if (mounters_map.equals("]")) {
                                } else {
                                    new SendMounters_mapData().execute();
                                }
                                cursor.close();

                            }
                        } catch (Exception e) {
                        }
                        ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_mounters", jsonMounters);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckMountersData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_mounters");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and sync=? and name_table = ?",
                                                new String[]{new_id, "0", "rgzbn_gm_ceiling_mounters"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, new_id);
                            values.put(DBHelper.KEY_ID_NEW, "0");
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mounters_map");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        }

                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_mounters", check_mounters);
                    return parameters;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }


    static class SendMounters_mapData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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
                    } else {

                        Log.d(TAG, res);
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values = new ContentValues();
                        values.put(DBHelper.KEY_SYNC, "1");
                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "sync=? and name_table = ?",
                                new String[]{"0", "rgzbn_gm_ceiling_mounters_map"});

                        //ctx.startService(new Intent(ctx, Service_Sync.class));

                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_mounters_map", mounters_map);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }


    static class SendMountData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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
                    } else {

                        Log.d(TAG, res);
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values = new ContentValues();
                        values.put(DBHelper.KEY_SYNC, "1");
                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "sync=? and name_table = ?",
                                new String[]{"0", "rgzbn_gm_ceiling_mount"});

                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_mount", jsonMount);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }

    static class SendDealerData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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
                    } else {

                        Log.d(TAG, res);
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values = new ContentValues();
                        values.put(DBHelper.KEY_SYNC, "1");
                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "sync=? and name_table = ?",
                                new String[]{"0", "rgzbn_gm_ceiling_dealer_info"});

                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_dealer_info", jsonDealer_info);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);
            return null;
        }
    }


    static class SendClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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
                                                    new String[]{old_id, "rgzbn_gm_ceiling_clients_contacts", "0"});

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

                            //контакты check
                            check_clients_contacts = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
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
                            if (check_clients_contacts.equals("]")) {
                            } else {
                                new CheckClientsContactsData().execute();
                            }
                            cursor.close();

                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", jsonClient_Contacts);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }

    }

    static class CheckClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    if (res.equals("")) {
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
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_clients_contacts", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_clients_contacts", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }

                    } catch (Exception e) {
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", check_clients_contacts);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendProjectsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    Log.d(TAG, "SendProjectsData " + res);

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                    } else {
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

                                values = new ContentValues();
                                values.put(DBHelper.KEY_PROJECT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_HISTORY, values, "project_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_PROJECT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_RECOIL_MAP_PROJECT, values, "project_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? ",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_projects"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                global_results = String.valueOf(jsonObjectProject);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_PROJECT_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "project_id = ?", new String[]{old_id});

                            }

                            //ctx.startService(new Intent(ctx, Service_Sync.class));
                        } catch (Exception e) {
                            //ctx.startService(new Intent(ctx, Service_Sync.class));
                        }
                    }

                    //проект check
                    check_project = "[";
                    String sqlQuewy = "SELECT id_new "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                    Cursor cursor = db.rawQuery(sqlQuewy,
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
                    if (check_project.equals("]")) {
                    } else {
                        new CheckProjectsData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------калькулятор------------------------");
                    //калькулятор send
                    jsonCalc = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_calculations", "1"});
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
                                            jsonObjectCalculation = new org.json.simple.JSONObject();
                                            for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_calculations"); j++) {
                                                try {
                                                    String status = c.getColumnName(c.getColumnIndex(c.getColumnName(j)));
                                                    String status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                    if (status.equals("_id")) {
                                                        status = "android_id";
                                                        jsonObjectCalculation.put(status, status1);
                                                    } else if (status.equals("calc_image")) {
                                                        status = "image";
                                                        status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                        jsonObjectCalculation.put(status, status1);
                                                    } else if (status.equals("cut_image")) {
                                                        status = "cut_image";
                                                        status1 = c.getString(c.getColumnIndex(c.getColumnName(j)));
                                                        jsonObjectCalculation.put(status, status1);
                                                    } else if (status1.equals("")) {
                                                    } else if (status1.equals("null") || status1.equals(null) || status1 == null) {
                                                    } else {
                                                        jsonObjectCalculation.put(status, status1);
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }
                                            jsonCalc += String.valueOf(jsonObjectCalculation) + ",";
                                        } while (c.moveToNext());
                                    } else {
                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_calculations"});
                                    }
                                }

                            } while (cursor.moveToNext());
                        }
                    }
                    jsonCalc = jsonCalc.substring(0, jsonCalc.length() - 1) + "]";
                    if (jsonCalc.equals("]")) {
                    } else {
                        new SendCalculationsData().execute();
                    }
                    cursor.close();

                    Log.d(TAG, "--------------------------ИСТОРИЯ------------------------");
                    //клиент send
                    jsonHistory = "[";
                    sqlQuewy = "SELECT id_old "
                            + "FROM history_send_to_server " +
                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                    cursor = db.rawQuery(sqlQuewy,
                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                    "send", "0", "rgzbn_gm_ceiling_projects_history", "1"});
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                String id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                try {
                                    sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_projects_history " +
                                            "where _id = ?";
                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                JSONObject jsonObjectClient = new JSONObject();
                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_projects_history"); j++) {
                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                    Log.d(TAG, status + " " + status1);
                                                    if (j == 0) {
                                                        status = "android_id";
                                                    }
                                                    if (status1 == null || status1.equals("") || (status1.equals("null"))) {
                                                    } else {
                                                        jsonObjectClient.put(status, status1);
                                                    }
                                                    Log.d(TAG, status + " " + status1);
                                                }
                                                jsonHistory += String.valueOf(jsonObjectClient) + ",";
                                            } while (cursor.moveToNext());
                                        } else {
                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_projects_history"});
                                        }
                                    }
                                    cursor.close();
                                    jsonRecoil = jsonRecoil.substring(0, jsonRecoil.length() - 1) + "]";
                                    if (jsonRecoil.equals("]")) {
                                    } else {
                                        new SendRecoilMapProject().execute();
                                    }

                                    Log.d(TAG, "--------------------------RECOIL_MAP_PROJECT------------------------");
                                    //клиент send
                                    jsonRecoil = "[";
                                    sqlQuewy = "SELECT id_old "
                                            + "FROM history_send_to_server " +
                                            "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                                    cursor = db.rawQuery(sqlQuewy,
                                            new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                                    "send", "0", "rgzbn_gm_ceiling_recoil_map_project", "1"});
                                    if (cursor != null) {
                                        if (cursor.moveToFirst()) {
                                            do {
                                                id_old = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                                try {
                                                    sqlQuewy = "SELECT * "
                                                            + "FROM rgzbn_gm_ceiling_recoil_map_project " +
                                                            "where _id = ?";
                                                    cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_old)});
                                                    if (cursor != null) {
                                                        if (cursor.moveToFirst()) {
                                                            do {
                                                                JSONObject jsonObjectClient = new JSONObject();
                                                                for (int j = 0; j < HelperClass.countColumns(ctx, "rgzbn_gm_ceiling_recoil_map_project"); j++) {
                                                                    String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));

                                                                    if (j == 0) {
                                                                        status = "android_id";
                                                                    }
                                                                    if (status1 == null || (status1.equals("null"))) {
                                                                    } else {
                                                                        jsonObjectClient.put(status, status1);
                                                                    }
                                                                }
                                                                jsonRecoil += String.valueOf(jsonObjectClient) + ",";
                                                            } while (cursor.moveToNext());
                                                        } else {
                                                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                                    "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                                    new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_recoil_map_project"});
                                                        }
                                                    }
                                                    cursor.close();
                                                } catch (Exception e) {
                                                }

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
                    cursor.close();
                    jsonHistory = jsonHistory.substring(0, jsonHistory.length() - 1) + "]";
                    Log.d(TAG, "jsonHistory = " + jsonHistory);
                    if (jsonHistory.equals("]")) {
                    } else {
                        new SendProjectsHistory().execute();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_projects", jsonProjects);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckProjectsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    delete();
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


    static class SendProjectsHistory extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    Log.d(TAG, "SendProjectsHistory " + res);

                    if (res.equals("")) {

                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_projects_history");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_HISTORY, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? and id_new=?",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_projects_history", "0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects_history");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                            }

                            check_History = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_projects_history"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            check_History += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            check_History = check_History.substring(0, check_History.length() - 1) + "]";
                            Log.d(TAG, "check_History = " + check_History);
                            if (check_History.equals("]")) {
                            } else {
                                new CheckProjectsHistory().execute();
                            }
                            cursor.close();

                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));

                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_projects_history", jsonHistory);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

    static class CheckProjectsHistory extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    Log.d(TAG, "CheckProjectsHistory " + res);

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_projects_history");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_projects_history", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_projects_history", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                        }

                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_projects_history", check_History);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendApiPhones extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("")) {

                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_api_phones");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_API_PHONES, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? and id_new=?",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_api_phones", "0"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_API_PHONE_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "api_phone_id = ?",
                                        new String[]{String.valueOf(old_id)});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_api_phones");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                            }

                            check_ApiPhones = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_api_phones"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            jsonObjectUsers = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectUsers.put("id", id_new);
                                            check_ApiPhones += String.valueOf(jsonObjectUsers) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            check_ApiPhones = check_ApiPhones.substring(0, check_ApiPhones.length() - 1) + "]";
                            if (check_ApiPhones.equals("]")) {
                            } else {
                                new CheckApiPhones().execute();
                            }
                            cursor.close();

                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_api_phones", jsonApiPhones);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

    static class CheckApiPhones extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    Log.d(TAG, "rgzbn_gm_ceiling_api_phones = " + res);

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_api_phones");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_api_phones", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_api_phones", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_api_phones", check_ApiPhones);

                    Log.d(TAG, "rgzbn_gm_ceiling_api_phones " + parameters);

                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }


    static class SendRecoilMapProject extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    if (res.equals("")) {

                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_recoil_map_project");
                            for (int i = 0; i < id_array.length(); i++) {
                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_ID, new_id);
                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_RECOIL_MAP_PROJECT, values, "_id = ?", new String[]{old_id});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_SYNC, "1");
                                db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=?",
                                        new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_recoil_map_project"});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, old_id);
                                values.put(DBHelper.KEY_ID_NEW, new_id);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_recoil_map_project");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "check");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                            }

                            checkRecoil = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "check", "0", "rgzbn_gm_ceiling_recoil_map_project"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        try {
                                            JSONObject jsonObjectClient = new JSONObject();
                                            String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                            jsonObjectClient.put("id", id_new);
                                            checkRecoil += String.valueOf(jsonObjectClient) + ",";
                                        } catch (Exception e) {
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }

                            checkRecoil = checkRecoil.substring(0, checkRecoil.length() - 1) + "]";
                            if (checkRecoil.equals("]")) {
                            } else {
                                new CheckRecoilMapProject().execute();
                            }
                            cursor.close();


                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_recoil_map_project", jsonRecoil);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

    static class CheckRecoilMapProject extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    Log.d(TAG, "rgzbn_gm_ceiling_recoil_map_project = " + res);

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_recoil_map_project");
                        for (int i = 0; i < dat.length(); i++) {

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            String sqlQuewy = "SELECT * "
                                    + "FROM history_send_to_server " +
                                    "where id_new = ? and type=? and name_table=? and sync=?";
                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id), "check", "rgzbn_gm_ceiling_recoil_map_project", "0"});
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_SYNC, "1");
                                        db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_new = ? and name_table=? and sync=?",
                                                new String[]{new_id, "rgzbn_gm_ceiling_recoil_map_project", "0"});

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();
                        }
                    } catch (Exception e) {
                    }

                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_recoil_map_project", checkRecoil);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }


    static class SendCalculationsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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

                    Log.d(TAG, "CALCULATOR пришло " + res);

                    if (res.equals("") || res.equals("\u041e\u0448\u0438\u0431\u043a\u0430!")) {
                        Log.d("responce", "SendCalculationData пусто");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String new_id = "";
                        String old_id = "";
                        global_results = "[";

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

                            }

                            //калькулятор check
                            check_calculation = "[";
                            String sqlQuewy = "SELECT id_new "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                            Cursor cursor = db.rawQuery(sqlQuewy,
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
                            if (check_calculation.equals("]")) {
                            } else {
                                new CheckCalculationData().execute();
                            }
                            cursor.close();

                            components_diffusers = "[";
                            components_ecola = "[";
                            components_fixtures = "[";
                            components_hoods = "[";
                            components_pipes = "[";
                            components_corn = "[";
                            components_profil = "[";

                            Log.d(TAG, "--------------------------компоненты_fix------------------------");
                            //компоненты_fix send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_fixtures", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_fixtures"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_ecola------------------------");
                            //компоненты_ecola send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_ecola", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_ecola"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_diff------------------------");
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_diffusers"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_hoods------------------------");
                            //компоненты_hoods send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where (id_old>? and id_old<?) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999),
                                            "send", "0", "rgzbn_gm_ceiling_hoods", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ?  and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_hoods"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_pipes------------------------");
                            //компоненты_pipes send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_pipes", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_pipes"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_cornice------------------------");
                            //компоненты_cornice send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_cornice", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_cornice"});
                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Log.d(TAG, "--------------------------компоненты_profil------------------------");
                            //компоненты_cornice send
                            sqlQuewy = "SELECT id_old "
                                    + "FROM history_send_to_server " +
                                    "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=? and status=?";
                            cursor = db.rawQuery(sqlQuewy,
                                    new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                            "send", "0", "rgzbn_gm_ceiling_profil", "1"});
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
                                                if (c.moveToFirst()) {
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
                                                } else {
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                                            "id_old = ? and name_table = ? and sync = 0 and type = 'send' ",
                                                            new String[]{String.valueOf(id_old), "rgzbn_gm_ceiling_profil"});

                                                }
                                            }
                                            c.close();
                                        } catch (Exception e) {
                                        }

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            if (components_diffusers.length() < 3) {
                                components_diffusers += "]";
                            } else {
                                components_diffusers = components_diffusers.substring(0, components_diffusers.length() - 1) + "]";
                            }
                            if (components_ecola.length() < 3) {
                                components_ecola += "]";
                            } else {
                                components_ecola = components_ecola.substring(0, components_ecola.length() - 1) + "]";
                            }
                            if (components_fixtures.length() < 3) {
                                components_fixtures += "]";
                            } else {
                                components_fixtures = components_fixtures.substring(0, components_fixtures.length() - 1) + "]";
                            }
                            if (components_hoods.length() < 3) {
                                components_hoods += "]";
                            } else {
                                components_hoods = components_hoods.substring(0, components_hoods.length() - 1) + "]";
                            }
                            if (components_pipes.length() < 3) {
                                components_pipes += "]";
                            } else {
                                components_pipes = components_pipes.substring(0, components_pipes.length() - 1) + "]";
                            }
                            if (components_corn.length() < 3) {
                                components_corn += "]";
                            } else {
                                components_corn = components_corn.substring(0, components_corn.length() - 1) + "]";
                            }
                            if (components_profil.length() < 3) {
                                components_profil += "]";
                            } else {
                                components_profil = components_profil.substring(0, components_profil.length() - 1) + "]";
                            }

                            if ((components_diffusers.equals("]") && components_ecola.equals("]") && components_fixtures.equals("]") && components_hoods.equals("]") &&
                                    components_pipes.equals("]") && components_corn.equals("]") && components_profil.equals("]")) ||
                                    (components_diffusers.equals("[]") && components_ecola.equals("[]") && components_fixtures.equals("[]") && components_hoods.equals("[]") &&
                                            components_pipes.equals("[]") && components_corn.equals("[]") && components_profil.equals("[]"))) {
                            } else {
                                new SendComponents().execute();
                            }

                            check_components_diffusers = "[";
                            check_components_ecola = "[";
                            check_components_fixtures = "[";
                            check_components_hoods = "[";
                            check_components_pipes = "[";
                            check_components_corn = "[";
                            check_components_profil = "[";

                        } catch (Exception e) {
                        }
                        //ctx.startService(new Intent(ctx, Service_Sync.class));
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

                    Log.d(TAG, "CALCULATOR отправил " + jsonCalc);
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckCalculationData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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
                    delete();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_calculations", check_calculation);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendComponents extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.addDataFromAndroid";
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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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
                                        new String[]{old_id, table_name, "0"});

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

                        //ctx.startService(new Intent(ctx, Service_Sync.class));

                        //компоненты_fix check
                        String sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        Cursor cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_fixtures"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_fixtures += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_diff check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_diffusers"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_diffusers += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_pipes check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_pipes"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_pipes += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_hoods check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_hoods"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_hoods += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_ecola check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_ecola"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_ecola += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_cornice check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_cornice"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_corn += String.valueOf(jsonObjectComponents) + ",";
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        //компоненты_cornice check
                        sqlQuewy = "SELECT id_new "
                                + "FROM history_send_to_server " +
                                "where ((id_old>=? and id_old<=?) or (id_old<=?)) and type=? and sync=? and name_table=?";
                        cursor = db.rawQuery(sqlQuewy,
                                new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999), String.valueOf(999999),
                                        "check", "0", "rgzbn_gm_ceiling_profil"});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    jsonObjectComponents = new org.json.simple.JSONObject();
                                    String id_new = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    jsonObjectComponents.put("id", id_new);
                                    check_components_profil += String.valueOf(jsonObjectComponents) + ",";
                                    new CheckCalculationData().execute();
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();

                        check_components_diffusers = check_components_diffusers.substring(0, check_components_diffusers.length() - 1) + "]";
                        check_components_ecola = check_components_ecola.substring(0, check_components_ecola.length() - 1) + "]";
                        check_components_fixtures = check_components_fixtures.substring(0, check_components_fixtures.length() - 1) + "]";
                        check_components_hoods = check_components_hoods.substring(0, check_components_hoods.length() - 1) + "]";
                        check_components_pipes = check_components_pipes.substring(0, check_components_pipes.length() - 1) + "]";
                        check_components_corn = check_components_corn.substring(0, check_components_corn.length() - 1) + "]";
                        check_components_profil = check_components_profil.substring(0, check_components_profil.length() - 1) + "]";

                        if ((check_components_diffusers.equals("]") && check_components_ecola.equals("]") && check_components_fixtures.equals("]")
                                && check_components_hoods.equals("]") && check_components_pipes.equals("]") && check_components_corn.equals("]")
                                && check_components_profil.equals("]"))
                                || (check_components_diffusers.equals("[]") && check_components_ecola.equals("[]") && check_components_fixtures.equals("[]")
                                && check_components_hoods.equals("[]") && check_components_pipes.equals("[]") && check_components_corn.equals("[]")
                                && check_components_profil.equals("[]"))) {
                        } else {
                            new CheckComponents().execute();
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
                    parameters.put("rgzbn_gm_ceiling_diffusers", components_diffusers);
                    parameters.put("rgzbn_gm_ceiling_fixtures", components_fixtures);
                    parameters.put("rgzbn_gm_ceiling_cornice", components_corn);
                    parameters.put("rgzbn_gm_ceiling_ecola", components_ecola);
                    parameters.put("rgzbn_gm_ceiling_hoods", components_hoods);
                    parameters.put("rgzbn_gm_ceiling_pipes", components_pipes);
                    parameters.put("rgzbn_gm_ceiling_profil", components_profil);

                    Log.d(TAG, "comp send = " + parameters);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    static class CheckComponents extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.CheckDataFromAndroid";
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

                    delete();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_diffusers", check_components_diffusers);
                    parameters.put("rgzbn_gm_ceiling_fixtures", check_components_fixtures);
                    parameters.put("rgzbn_gm_ceiling_cornice", check_components_corn);
                    parameters.put("rgzbn_gm_ceiling_ecola", check_components_ecola);
                    parameters.put("rgzbn_gm_ceiling_hoods", check_components_hoods);
                    parameters.put("rgzbn_gm_ceiling_pipes", check_components_pipes);
                    parameters.put("rgzbn_gm_ceiling_profil", check_components_profil);

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }


    static class SendDeleteTable extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&amp;task=api.deleteDataFromAndroid";
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

                    if (res.equals("")) {

                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();

                        res = res.substring(1, res.length() - 1);
                        Log.d(TAG, res);

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            String delete_id = jsonObject.getString("ids");
                            String table = jsonObject.getString("table");

                            Log.d(TAG, delete_id + " " + table);

                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_SYNC, "1");
                            db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and type=? and sync=? and name_table=? and id_new=?",
                                    new String[]{String.valueOf(delete_id), "delete", "0", table, "0"});

                        } catch (Exception e) {
                            Log.d(TAG, String.valueOf(e));
                        }
                        ctx.startService(new Intent(ctx, Service_Sync.class));
                    }
                    delete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put(jsonDeleteTable, jsonDelete);
                    Log.d(TAG, "delete" + parameters);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }

}