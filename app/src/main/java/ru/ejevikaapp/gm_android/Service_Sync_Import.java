package ru.ejevikaapp.gm_android;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.ejevikaapp.gm_android.Dealer.Activity_empty;

public class Service_Sync_Import extends Service {
    private static final String TAG = "responce_import";
    static DBHelper dbHelper;

    static String sync_import = "", user_id = "", change_time_global, version_send = "";

    static org.json.simple.JSONObject jsonSync_Import = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonVersion = new org.json.simple.JSONObject();

    static RequestQueue requestQueue;

    static Context ctx;

    static String domen;

    static int count_project1 = 0;

    public Service_Sync_Import() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Service started!");

        SharedPreferences SP = this.getSharedPreferences("link", MODE_PRIVATE);
        domen = SP.getString("", "");

        int count = 0;

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "SELECT * "
                + "FROM history_send_to_server";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    count++;
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count == 0) {
            ctx = this.getApplicationContext();
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());

            SP = this.getSharedPreferences("dealer_id", MODE_PRIVATE);
            String dealer_id = SP.getString("", "");

            SharedPreferences SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
            user_id = SP_end.getString("", "");

            String change_time = "";

            sqlQuewy = "SELECT change_time "
                    + "FROM history_import_to_server" +
                    " WHERE user_id = ?";

            c = db.rawQuery(sqlQuewy, new String[]{user_id});

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        change_time_global = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    } while (c.moveToNext());
                }
            }
            c.close();

            String usergroup = "";
            sqlQuewy = "SELECT group_id "
                    + "FROM rgzbn_user_usergroup_map" +
                    " WHERE user_id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        usergroup = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    } while (c.moveToNext());
                }
            }
            c.close();

            if (usergroup.equals("14")) {
                jsonSync_Import.put("change_time", change_time_global);
                jsonSync_Import.put("dealer_id", user_id);
                sync_import = String.valueOf(jsonSync_Import);

                if (user_id.equals("")) {
                } else {
                    new ImportDate().execute();
                }
            } else if (usergroup.equals("22") || usergroup.equals("21")) {
                jsonSync_Import.put("change_time", change_time_global);
                jsonSync_Import.put("project_calculator", user_id);
                sync_import = String.valueOf(jsonSync_Import);

                if (user_id.equals("")) {
                } else {
                    new ImportDate().execute();
                }
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service_Sync_Import.class.getName().equals(service.service.getClassName())) {
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
        String sqlQuewy;
        Cursor cursor;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Alarm received: " + intent.getAction());

            SharedPreferences SP = context.getSharedPreferences("link", MODE_PRIVATE);
            domen = SP.getString("", "");

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

            int count = 0;

            dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            try {
                String sqlQuewy = "SELECT * "
                        + "FROM history_send_to_server";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            count++;
                        } while (c.moveToNext());
                    }
                }
                c.close();
            } catch (Exception e) {
                count = 0;
            }

            if (!isRunning(context)) {
            } else if (count == 0) {
                Log.v(TAG, "don't start service: already running...");

                SP = context.getSharedPreferences("dealer_id", MODE_PRIVATE);
                String dealer_id = SP.getString("", "");

                SharedPreferences SP_end = context.getSharedPreferences("user_id", MODE_PRIVATE);
                user_id = SP_end.getString("", "");

                sqlQuewy = "SELECT change_time "
                        + "FROM history_import_to_server" +
                        " WHERE user_id = ?";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            change_time_global = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                String usergroup = "";
                sqlQuewy = "SELECT group_id "
                        + "FROM rgzbn_user_usergroup_map" +
                        " WHERE user_id = ?";
                c = db.rawQuery(sqlQuewy, new String[]{user_id});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            usergroup = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                if (usergroup.equals("14")) {
                    jsonSync_Import.put("change_time", change_time_global);
                    jsonSync_Import.put("dealer_id", user_id);
                    sync_import = String.valueOf(jsonSync_Import);

                    if (user_id.equals("")) {
                    } else {
                        new ImportDate().execute();
                    }
                } else if (usergroup.equals("22") || usergroup.equals("21")) {
                    jsonSync_Import.put("change_time", change_time_global);
                    jsonSync_Import.put("project_calculator", user_id);
                    sync_import = String.valueOf(jsonSync_Import);

                    if (user_id.equals("")) {
                    } else {
                        new ImportDate().execute();
                    }
                }
            }
        }

        public static void setAlarm(Context context) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 * ALARM_INTERVAL_SEC, pi);
        }

        public static void cancelAlarm(Context context) {
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(ALARM_EVENT), 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

    static class ImportDate extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://" + domen + ".gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.sendDataToAndroid";
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

                    Log.d(TAG, res);

                    SQLiteDatabase db;
                    db = dbHelper.getReadableDatabase();

                    if (res.equals("null")) {

                    } else {

                        int count = 0;

                        try {

                            ContentValues values;

                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date change_max = ft.parse(change_time_global);

                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray rgzbn_gm_ceiling_clients = jsonObject.getJSONArray("rgzbn_gm_ceiling_clients");

                            for (int i = 0; i < rgzbn_gm_ceiling_clients.length(); i++) {

                                values = new ContentValues();
                                org.json.JSONObject cleint = rgzbn_gm_ceiling_clients.getJSONObject(i);

                                String id = cleint.getString("id");
                                count = 0;

                                String client_name = cleint.getString("client_name");
                                String client_data_id = cleint.getString("client_data_id");
                                String type_id = cleint.getString("type_id");
                                String manager_id = cleint.getString("manager_id");
                                String dealer_id = cleint.getString("dealer_id");
                                String created = cleint.getString("created");
                                String sex = cleint.getString("sex");
                                String deleted_by_user = cleint.getString("deleted_by_user");
                                String change_time = cleint.getString("change_time");

                                values.put(DBHelper.KEY_CLIENT_NAME, client_name);
                                values.put(DBHelper.KEY_CLIENT_DATA_ID, client_data_id);
                                values.put(DBHelper.KEY_TYPE_ID, type_id);
                                values.put(DBHelper.KEY_MANAGER_ID, manager_id);
                                values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                                values.put(DBHelper.KEY_CREATED, created);
                                values.put(DBHelper.KEY_SEX, sex);
                                values.put(DBHelper.KEY_DELETED_BY_USER, deleted_by_user);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_clients" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }

                                c.close();

                                if (count == 0) {
                                    try {
                                        values.put(DBHelper.KEY_ID, id);
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                                        Date change = ft.parse(change_time);
                                        if (change_max.getTime() < change.getTime()) {
                                            change_max = change;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_clients_contacts = jsonObject.getJSONArray("rgzbn_gm_ceiling_clients_contacts");
                            for (int i = 0; i < rgzbn_gm_ceiling_clients_contacts.length(); i++) {

                                values = new ContentValues();
                                org.json.JSONObject client_contact = rgzbn_gm_ceiling_clients_contacts.getJSONObject(i);

                                count = 0;
                                String id = client_contact.getString("id");

                                String client_id = client_contact.getString("client_id");
                                String phone = client_contact.getString("phone");
                                String change_time = client_contact.getString("change_time");

                                values.put(DBHelper.KEY_ID, id);
                                values.put(DBHelper.KEY_CLIENT_ID, client_id);
                                values.put(DBHelper.KEY_PHONE, phone);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_clients_contacts" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    try {
                                        values.put(DBHelper.KEY_ID, id);
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);
                                        Date change = ft.parse(change_time);
                                        if (change_max.getTime() < change.getTime()) {
                                            change_max = change;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_callback = jsonObject.getJSONArray("rgzbn_gm_ceiling_callback");
                            for (int i = 0; i < rgzbn_gm_ceiling_callback.length(); i++) {

                                values = new ContentValues();
                                org.json.JSONObject callback = rgzbn_gm_ceiling_callback.getJSONObject(i);

                                Log.d(TAG, "callback " + String.valueOf(callback));

                                count = 0;
                                String id = callback.getString("id");
                                String client_id = callback.getString("client_id");
                                String date_time = callback.getString("date_time");
                                String comment = callback.getString("comment");
                                String manager_id = callback.getString("manager_id");
                                String notify = callback.getString("notify");
                                String change_time = callback.getString("change_time");

                                values.put(DBHelper.KEY_CLIENT_ID, client_id);
                                values.put(DBHelper.KEY_DATE_TIME, date_time);
                                values.put(DBHelper.KEY_COMMENT, comment);
                                values.put(DBHelper.KEY_MANAGER_ID, manager_id);
                                values.put(DBHelper.KEY_NOTIFY, notify);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_callback" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, values,
                                                    "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    try {
                                        values.put(DBHelper.KEY_ID, id);
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, null, values);
                                        Date change = ft.parse(change_time);
                                        if (change_max.getTime() < change.getTime()) {
                                            change_max = change;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_client_history = jsonObject.getJSONArray("rgzbn_gm_ceiling_client_history");
                            for (int i = 0; i < rgzbn_gm_ceiling_client_history.length(); i++) {

                                values = new ContentValues();
                                org.json.JSONObject client_history = rgzbn_gm_ceiling_client_history.getJSONObject(i);

                                Log.d(TAG, "client_history " + String.valueOf(client_history));

                                count = 0;
                                String id = client_history.getString("id");
                                String client_id = client_history.getString("client_id");
                                String date_time = client_history.getString("date_time");
                                String text = client_history.getString("text");
                                String change_time = client_history.getString("change_time");

                                values.put(DBHelper.KEY_CLIENT_ID, client_id);
                                values.put(DBHelper.KEY_DATE_TIME, date_time);
                                values.put(DBHelper.KEY_TEXT, text);
                                //values.put(DBHelper.KEY_CHANGE_TIME, change_time);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_client_history" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, values,
                                                    "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    try {
                                        values.put(DBHelper.KEY_ID, id);
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, null, values);
                                        Date change = ft.parse(change_time);
                                        if (change_max.getTime() < change.getTime()) {
                                            change_max = change;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_projects = jsonObject.getJSONArray("rgzbn_gm_ceiling_projects");

                            for (int i = 0; i < rgzbn_gm_ceiling_projects.length(); i++) {

                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_projects.getJSONObject(i);
                                Log.d(TAG, "project " + String.valueOf(porject_tmp));
                                count = 0;

                                try {
                                    String id = porject_tmp.getString("id");
                                    String ordering = porject_tmp.getString("ordering");
                                    String state = porject_tmp.getString("state");
                                    String checked_out = porject_tmp.getString("checked_out");
                                    String checked_out_time = porject_tmp.getString("checked_out_time");
                                    String created_by = porject_tmp.getString("created_by");
                                    String modified_by = porject_tmp.getString("modified_by");
                                    String client_id = porject_tmp.getString("client_id");
                                    String project_info = porject_tmp.getString("project_info");
                                    String project_status = porject_tmp.getString("project_status");
                                    String project_note = porject_tmp.getString("project_note");
                                    String gm_calculator_note = porject_tmp.getString("gm_calculator_note");
                                    String dealer_calculator_note = porject_tmp.getString("dealer_calculator_note");
                                    String gm_manager_note = porject_tmp.getString("gm_manager_note");
                                    String gm_chief_note = porject_tmp.getString("gm_chief_note");
                                    String dealer_chief_note = porject_tmp.getString("dealer_chief_note");
                                    String dealer_manager_note = porject_tmp.getString("dealer_manager_note");
                                    String buh_note = porject_tmp.getString("buh_note");
                                    String project_calculation_date = porject_tmp.getString("project_calculation_date");
                                    String project_calculator = porject_tmp.getString("project_calculator");
                                    String who_calculate = porject_tmp.getString("who_calculate");
                                    String project_verdict = porject_tmp.getString("project_verdict");
                                    String project_discount = porject_tmp.getString("project_discount");
                                    String created = porject_tmp.getString("created");
                                    String closed = porject_tmp.getString("closed");
                                    String project_check = porject_tmp.getString("project_check");
                                    String sum_check = porject_tmp.getString("sum_check");
                                    String cost_check = porject_tmp.getString("cost_check");
                                    String spend_check = porject_tmp.getString("spend_check");
                                    String mounting_check = porject_tmp.getString("mounting_check");
                                    String new_project_sum = porject_tmp.getString("new_project_sum");
                                    String new_project_spend = porject_tmp.getString("new_project_spend");
                                    String new_project_mounting = porject_tmp.getString("new_project_mounting");
                                    String new_extra_spend = porject_tmp.getString("new_extra_spend");
                                    String gm_canvases_margin = porject_tmp.getString("gm_canvases_margin");
                                    String gm_components_margin = porject_tmp.getString("gm_components_margin");
                                    String gm_mounting_margin = porject_tmp.getString("gm_mounting_margin");
                                    String dealer_canvases_margin = porject_tmp.getString("dealer_canvases_margin");
                                    String dealer_components_margin = porject_tmp.getString("dealer_components_margin");
                                    String dealer_mounting_margin = porject_tmp.getString("dealer_mounting_margin");
                                    String project_sum = porject_tmp.getString("project_sum");
                                    String salary_sum = porject_tmp.getString("salary_sum");
                                    String extra_spend = porject_tmp.getString("extra_spend");
                                    String penalty = porject_tmp.getString("penalty");
                                    String bonus = porject_tmp.getString("bonus");
                                    String calculated_by = porject_tmp.getString("calculated_by");
                                    String approved_by = porject_tmp.getString("approved_by");
                                    String checked_by = porject_tmp.getString("checked_by");
                                    String read_by_manager = porject_tmp.getString("read_by_manager");
                                    String api_phone_id = porject_tmp.getString("api_phone_id");
                                    String read_by_mounter = porject_tmp.getString("read_by_mounter");
                                    String new_mount_sum = porject_tmp.getString("new_mount_sum");
                                    String new_material_sum = porject_tmp.getString("new_material_sum");
                                    String transport = porject_tmp.getString("transport");
                                    String distance = porject_tmp.getString("distance");
                                    String distance_col = porject_tmp.getString("distance_col");
                                    String deleted_by_user = porject_tmp.getString("deleted_by_user");
                                    String change_time = porject_tmp.getString("change_time");

                                    values.put(DBHelper.KEY_ORDERING, ordering);
                                    values.put(DBHelper.KEY_STATE, state);
                                    values.put(DBHelper.KEY_CHECKED_OUT, checked_out);
                                    values.put(DBHelper.KEY_CHECKED_OUT_TIME, checked_out_time);
                                    values.put(DBHelper.KEY_CREATED_BY, created_by);
                                    values.put(DBHelper.KEY_MODIFIED_BY, modified_by);
                                    values.put(DBHelper.KEY_CLIENT_ID, client_id);
                                    values.put(DBHelper.KEY_PROJECT_INFO, project_info);
                                    values.put(DBHelper.KEY_PROJECT_STATUS, project_status);
                                    values.put(DBHelper.KEY_PROJECT_NOTE, project_note);
                                    values.put(DBHelper.KEY_GM_CALCULATOR_NOTE, gm_calculator_note);
                                    values.put(DBHelper.KEY_DEALER_CALCULATOR_NOTE, dealer_calculator_note);
                                    values.put(DBHelper.KEY_GM_MANAGER_NOTE, gm_manager_note);
                                    values.put(DBHelper.KEY_GM_CHIEF_NOTE, gm_chief_note);
                                    values.put(DBHelper.KEY_DEALER_CHIEF_NOTE, dealer_chief_note);
                                    values.put(DBHelper.KEY_DEALER_MANAGER_NOTE, dealer_manager_note);
                                    values.put(DBHelper.KEY_BUH_NOTE, buh_note);
                                    values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, project_calculation_date);
                                    values.put(DBHelper.KEY_PROJECT_CALCULATOR, project_calculator);
                                    values.put(DBHelper.KEY_WHO_CALCULATE, who_calculate);
                                    values.put(DBHelper.KEY_PROJECT_VERDICT, project_verdict);
                                    values.put(DBHelper.KEY_PROJECT_DISCOUNT, project_discount);
                                    values.put(DBHelper.KEY_CREATED, created);
                                    values.put(DBHelper.KEY_CLOSED, closed);
                                    values.put(DBHelper.KEY_PROJECT_CHECK, project_check);
                                    values.put(DBHelper.KEY_SUM_CHECK, sum_check);
                                    values.put(DBHelper.KEY_COST_CHECK, cost_check);
                                    values.put(DBHelper.KEY_SPEND_CHECK, spend_check);
                                    values.put(DBHelper.KEY_MOUNTING_CHECK, mounting_check);
                                    values.put(DBHelper.KEY_NEW_PROJECT_SUM, new_project_sum);
                                    values.put(DBHelper.KEY_NEW_PROJECT_SPEND, new_project_spend);
                                    values.put(DBHelper.KEY_NEW_PROJECT_MOUNTING, new_project_mounting);
                                    values.put(DBHelper.KEY_NEW_EXTRA_SPEND, new_extra_spend);
                                    values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                                    values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                                    values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                                    values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                                    values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                                    values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                                    values.put(DBHelper.KEY_PROJECT_SUM, project_sum);
                                    values.put(DBHelper.KEY_SALARY_SUM, salary_sum);
                                    values.put(DBHelper.KEY_EXTRA_SPEND, extra_spend);
                                    values.put(DBHelper.KEY_PENALTY, penalty);
                                    values.put(DBHelper.KEY_BONUS, bonus);
                                    values.put(DBHelper.KEY_CALCULATED_BY, calculated_by);
                                    values.put(DBHelper.KEY_APPROVED_BY, approved_by);
                                    values.put(DBHelper.KEY_CHECKED_BY, checked_by);
                                    values.put(DBHelper.KEY_READ_BY_MANAGER, read_by_manager);
                                    values.put(DBHelper.KEY_API_PHONE_ID, api_phone_id);
                                    values.put(DBHelper.KEY_READ_BY_MOUNTER, read_by_mounter);
                                    values.put(DBHelper.KEY_NEW_MOUNT_SUM, new_mount_sum);
                                    values.put(DBHelper.KEY_NEW_MATERIAL_SUM, new_material_sum);
                                    values.put(DBHelper.KEY_TRANSPORT, transport);
                                    values.put(DBHelper.KEY_DISTANCE, distance);
                                    values.put(DBHelper.KEY_DISTANCE_COL, distance_col);
                                    values.put(DBHelper.KEY_DELETED_BY_USER, deleted_by_user);
                                    values.put(DBHelper.KEY_CHANGE_TIME, change_time);

                                    String sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_projects" +
                                            " WHERE _id = ?";
                                    Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id});
                                                count++;
                                                Date change = ft.parse(change_time);
                                                if (change_max.getTime() < change.getTime()) {
                                                    change_max = change;
                                                }
                                            } while (c.moveToNext());
                                        }
                                    }
                                    c.close();

                                    if (count == 0) {

                                        if (project_status.equals("0") || project_status.equals("1")) {

                                            count_project1++;

                                            Intent resultIntent = new Intent(ctx, Activity_empty.class);
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
                                                        resultIntent = new Intent(ctx, MainActivity.class);
                                                        break;

                                                    } else if (group_id.get(g).equals("21") || group_id.get(g).equals("22")) {
                                                        resultIntent = new Intent(ctx, MainActivity.class);
                                                        break;
                                                    } else if (group_id.get(g).equals("14")) {
                                                        resultIntent = new Intent(ctx, Activity_empty.class);
                                                        break;
                                                    }
                                                }

                                            PendingIntent resultPendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent,
                                                    PendingIntent.FLAG_UPDATE_CURRENT);
                                            NotificationCompat.Builder builder =
                                                    new NotificationCompat.Builder(ctx)
                                                            .setAutoCancel(true)
                                                            .setTicker("У Вас новый замер")
                                                            .setWhen(System.currentTimeMillis())
                                                            .setDefaults(Notification.DEFAULT_ALL)
                                                            .setSmallIcon(R.raw.itc_icon)
                                                            .setContentIntent(resultPendingIntent)
                                                            .setContentTitle("IT-Ceiling")
                                                            .setContentText("У Вас новый замер (" + count_project1 + ")");
                                            Notification notification = builder.build();

                                            NotificationManager notificationManager = (NotificationManager) ctx
                                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1, notification);

                                            values.put(DBHelper.KEY_ID, id);
                                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }

                                        } else {
                                            values.put(DBHelper.KEY_ID, id);
                                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG,"Error " + e);
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_projects_mounts = jsonObject.getJSONArray("rgzbn_gm_ceiling_projects_mounts");

                            for (int i = 0; i < rgzbn_gm_ceiling_projects_mounts.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_projects_mounts.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String project_id = porject_tmp.getString("project_id");
                                String mounter_id = porject_tmp.getString("mounter_id");
                                String date_time = porject_tmp.getString("date_time");
                                String type = porject_tmp.getString("type");
                                String mount_start = porject_tmp.getString("mount_start");
                                String mount_end = porject_tmp.getString("mount_end");
                                String change_time = porject_tmp.getString("change_time");

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_cornice" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {

                                            values.put(DBHelper.KEY_PROJECT_ID, project_id);
                                            values.put(DBHelper.KEY_MOUNTER_ID, mounter_id);
                                            values.put(DBHelper.KEY_DATE_TIME, date_time);
                                            values.put(DBHelper.KEY_TYPE, type);
                                            values.put(DBHelper.KEY_MOUNT_START, mount_start);
                                            values.put(DBHelper.KEY_MOUNT_END, mount_end);
                                            values.put(DBHelper.KEY_CHANGE_TIME, change_time);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_MOUNTS, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    values.put(DBHelper.KEY_PROJECT_ID, project_id);
                                    values.put(DBHelper.KEY_MOUNTER_ID, mounter_id);
                                    values.put(DBHelper.KEY_DATE_TIME, date_time);
                                    values.put(DBHelper.KEY_TYPE, type);
                                    values.put(DBHelper.KEY_MOUNT_START, mount_start);
                                    values.put(DBHelper.KEY_MOUNT_END, mount_end);
                                    values.put(DBHelper.KEY_CHANGE_TIME, change_time);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_MOUNTS, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_calculations = jsonObject.getJSONArray("rgzbn_gm_ceiling_calculations");
                            for (int i = 0; i < rgzbn_gm_ceiling_calculations.length(); i++) {
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_calculations.getJSONObject(i);

                                count = 0;
                                try {
                                    String id = porject_tmp.getString("id");
                                    String ordering = porject_tmp.getString("ordering");
                                    String state = porject_tmp.getString("state");
                                    String checked_out = porject_tmp.getString("checked_out");
                                    String checked_out_time = porject_tmp.getString("checked_out_time");
                                    String created_by = porject_tmp.getString("created_by");
                                    String modified_by = porject_tmp.getString("modified_by");
                                    String calculation_title = porject_tmp.getString("calculation_title");
                                    String project_id = porject_tmp.getString("project_id");
                                    String n3 = porject_tmp.getString("n3");
                                    String n4 = porject_tmp.getString("n4");
                                    String n5 = porject_tmp.getString("n5");
                                    String n6 = porject_tmp.getString("n6");
                                    String n7 = porject_tmp.getString("n7");
                                    String n8 = porject_tmp.getString("n8");
                                    String n9 = porject_tmp.getString("n9");
                                    String n10 = porject_tmp.getString("n10");
                                    String n11 = porject_tmp.getString("n11");
                                    String n12 = porject_tmp.getString("n12");
                                    String n16 = porject_tmp.getString("n16");
                                    String n17 = porject_tmp.getString("n17");
                                    String n18 = porject_tmp.getString("n18");
                                    String n19 = porject_tmp.getString("n19");
                                    String n20 = porject_tmp.getString("n20");
                                    String n21 = porject_tmp.getString("n21");
                                    String n24 = porject_tmp.getString("n24");
                                    String n25 = porject_tmp.getString("n25");
                                    String n27 = porject_tmp.getString("n27");
                                    String n28 = porject_tmp.getString("n28");
                                    String n30 = porject_tmp.getString("n30");
                                    String n31 = porject_tmp.getString("n31");
                                    String n32 = porject_tmp.getString("n32");
                                    String height = porject_tmp.getString("height");
                                    String components_sum = porject_tmp.getString("components_sum");
                                    String canvases_sum = porject_tmp.getString("canvases_sum");
                                    String mounting_sum = porject_tmp.getString("mounting_sum");
                                    String dop_krepezh = porject_tmp.getString("dop_krepezh");
                                    String extra_components = porject_tmp.getString("extra_components");
                                    String extra_mounting = porject_tmp.getString("extra_mounting");
                                    String color = porject_tmp.getString("color");
                                    String details = porject_tmp.getString("details");
                                    String image = porject_tmp.getString("image");
                                    String calc_data = porject_tmp.getString("calc_data");
                                    String cut_data = porject_tmp.getString("cut_data");
                                    String offcut_square = porject_tmp.getString("offcut_square");
                                    String original_sketch = porject_tmp.getString("original_sketch");
                                    String discount = porject_tmp.getString("discount");
                                    String change_time = porject_tmp.getString("change_time");

                                    values.put(DBHelper.KEY_ORDERING, ordering);
                                    values.put(DBHelper.KEY_STATE, state);
                                    values.put(DBHelper.KEY_CHECKED_OUT, checked_out);
                                    values.put(DBHelper.KEY_CHECKED_OUT_TIME, checked_out_time);
                                    values.put(DBHelper.KEY_CREATED_BY, created_by);
                                    values.put(DBHelper.KEY_MODIFIED_BY, modified_by);
                                    values.put(DBHelper.KEY_CALCULATION_TITLE, calculation_title);
                                    values.put(DBHelper.KEY_PROJECT_ID, project_id);
                                    values.put(DBHelper.KEY_N3, n3);
                                    values.put(DBHelper.KEY_N4, n4);
                                    values.put(DBHelper.KEY_N5, n5);
                                    values.put(DBHelper.KEY_N6, n6);
                                    values.put(DBHelper.KEY_N7, n7);
                                    values.put(DBHelper.KEY_N8, n8);
                                    values.put(DBHelper.KEY_N9, n9);
                                    values.put(DBHelper.KEY_N10, n10);
                                    values.put(DBHelper.KEY_N11, n11);
                                    values.put(DBHelper.KEY_N12, n12);
                                    values.put(DBHelper.KEY_N16, n16);
                                    values.put(DBHelper.KEY_N17, n17);
                                    values.put(DBHelper.KEY_N18, n18);
                                    values.put(DBHelper.KEY_N19, n19);
                                    values.put(DBHelper.KEY_N20, n20);
                                    values.put(DBHelper.KEY_N21, n21);
                                    values.put(DBHelper.KEY_N24, n24);
                                    values.put(DBHelper.KEY_N25, n25);
                                    values.put(DBHelper.KEY_N27, n27);
                                    values.put(DBHelper.KEY_N28, n28);
                                    values.put(DBHelper.KEY_N30, n30);
                                    values.put(DBHelper.KEY_N31, n31);
                                    values.put(DBHelper.KEY_N32, n32);
                                    values.put(DBHelper.KEY_HEIGHT, height);
                                    values.put(DBHelper.KEY_COMPONENTS_SUM, components_sum);
                                    values.put(DBHelper.KEY_CANVASES_SUM, canvases_sum);
                                    values.put(DBHelper.KEY_MOUNTING_SUM, mounting_sum);
                                    values.put(DBHelper.KEY_DOP_KREPEZH, dop_krepezh);
                                    values.put(DBHelper.KEY_EXTRA_COMPONENTS, extra_components);
                                    values.put(DBHelper.KEY_EXTRA_MOUNTING, extra_mounting);
                                    values.put(DBHelper.KEY_COLOR, color);
                                    values.put(DBHelper.KEY_DETAILS, details);
                                    values.put(DBHelper.KEY_CALC_IMAGE, image);
                                    values.put(DBHelper.KEY_CALC_DATA, calc_data);
                                    values.put(DBHelper.KEY_CUT_DATA, cut_data);
                                    values.put(DBHelper.KEY_OFFCUT_SQUARE, offcut_square);
                                    values.put(DBHelper.KEY_ORIGINAL_SKETCH, original_sketch);
                                    values.put(DBHelper.KEY_DISCOUNT, discount);

                                    String sqlQuewy = "SELECT * "
                                            + "FROM rgzbn_gm_ceiling_calculations" +
                                            " WHERE _id = ?";
                                    Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{id});
                                                count++;
                                                Date change = ft.parse(change_time);
                                                if (change_max.getTime() < change.getTime()) {
                                                    change_max = change;
                                                }
                                            } while (c.moveToNext());
                                        }
                                    }
                                    c.close();

                                    if (count == 0) {
                                        values.put(DBHelper.KEY_ID, id);
                                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);
                                        Date change = ft.parse(change_time);
                                        if (change_max.getTime() < change.getTime()) {
                                            change_max = change;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_cornice = jsonObject.getJSONArray("rgzbn_gm_ceiling_cornice");

                            for (int i = 0; i < rgzbn_gm_ceiling_cornice.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_cornice.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n15_count = porject_tmp.getString("n15_count");
                                String n15_type = porject_tmp.getString("n15_type");
                                String n15_size = porject_tmp.getString("n15_size");
                                String change_time = porject_tmp.getString("change_time");

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_cornice" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {

                                            values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                            values.put(DBHelper.KEY_N15_COUNT, n15_count);
                                            values.put(DBHelper.KEY_N15_TYPE, n15_type);
                                            values.put(DBHelper.KEY_N15_SIZE, n15_size);
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);

                                    values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                    values.put(DBHelper.KEY_N15_COUNT, n15_count);
                                    values.put(DBHelper.KEY_N15_TYPE, n15_type);
                                    values.put(DBHelper.KEY_N15_SIZE, n15_size);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_diffusers = jsonObject.getJSONArray("rgzbn_gm_ceiling_diffusers");

                            for (int i = 0; i < rgzbn_gm_ceiling_diffusers.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_diffusers.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n23_count = porject_tmp.getString("n23_count");
                                String n23_size = porject_tmp.getString("n23_size");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N23_COUNT, n23_count);
                                values.put(DBHelper.KEY_N23_SIZE, n23_size);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_diffusers" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }

                            }

                            JSONArray rgzbn_gm_ceiling_ecola = jsonObject.getJSONArray("rgzbn_gm_ceiling_ecola");

                            for (int i = 0; i < rgzbn_gm_ceiling_ecola.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_ecola.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n26_count = porject_tmp.getString("n26_count");
                                String n26_illuminator = porject_tmp.getString("n26_illuminator");
                                String n26_lamp = porject_tmp.getString("n26_lamp");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N26_COUNT, n26_count);
                                values.put(DBHelper.KEY_N26_ILLUMINATOR, n26_illuminator);
                                values.put(DBHelper.KEY_N26_LAMP, n26_lamp);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_ecola" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_fixtures = jsonObject.getJSONArray("rgzbn_gm_ceiling_fixtures");

                            for (int i = 0; i < rgzbn_gm_ceiling_fixtures.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_fixtures.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n13_count = porject_tmp.getString("n13_count");
                                String n13_type = porject_tmp.getString("n13_type");
                                String n13_size = porject_tmp.getString("n13_size");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N13_COUNT, n13_count);
                                values.put(DBHelper.KEY_N13_TYPE, n13_type);
                                values.put(DBHelper.KEY_N13_SIZE, n13_size);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_fixtures" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_hoods = jsonObject.getJSONArray("rgzbn_gm_ceiling_hoods");

                            for (int i = 0; i < rgzbn_gm_ceiling_hoods.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_hoods.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n22_count = porject_tmp.getString("n22_count");
                                String n22_type = porject_tmp.getString("n22_type");
                                String n22_size = porject_tmp.getString("n22_size");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N22_COUNT, n22_count);
                                values.put(DBHelper.KEY_N22_TYPE, n22_type);
                                values.put(DBHelper.KEY_N22_SIZE, n22_size);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_hoods" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_pipes = jsonObject.getJSONArray("rgzbn_gm_ceiling_pipes");

                            for (int i = 0; i < rgzbn_gm_ceiling_pipes.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_pipes.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n14_count = porject_tmp.getString("n14_count");
                                String n14_size = porject_tmp.getString("n14_size");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N14_COUNT, n14_count);
                                values.put(DBHelper.KEY_N14_SIZE, n14_size);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_pipes" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {
                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            JSONArray rgzbn_gm_ceiling_profil = null;
                            rgzbn_gm_ceiling_profil = jsonObject.getJSONArray("rgzbn_gm_ceiling_profil");

                            for (int i = 0; i < rgzbn_gm_ceiling_profil.length(); i++) {

                                count = 0;
                                values = new ContentValues();
                                org.json.JSONObject porject_tmp = rgzbn_gm_ceiling_profil.getJSONObject(i);

                                String id = porject_tmp.getString("id");
                                String calculation_id = porject_tmp.getString("calculation_id");
                                String n29_count = porject_tmp.getString("n29_count");
                                String n29_type = porject_tmp.getString("n29_type");
                                String n29_profil = porject_tmp.getString("n29_profil");
                                String change_time = porject_tmp.getString("change_time");

                                values.put(DBHelper.KEY_CALCULATION_ID, calculation_id);
                                values.put(DBHelper.KEY_N29_COUNT, n29_count);
                                values.put(DBHelper.KEY_N29_TYPE, n29_type);
                                values.put(DBHelper.KEY_N29_PROFILE, n29_profil);

                                String sqlQuewy = "SELECT * "
                                        + "FROM rgzbn_gm_ceiling_profil" +
                                        " WHERE _id = ?";
                                Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {

                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, values, "_id = ?", new String[]{id});
                                            count++;
                                            Date change = ft.parse(change_time);
                                            if (change_max.getTime() < change.getTime()) {
                                                change_max = change;
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();

                                if (count == 0) {

                                    values.put(DBHelper.KEY_ID, id);
                                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, values);
                                    Date change = ft.parse(change_time);
                                    if (change_max.getTime() < change.getTime()) {
                                        change_max = change;
                                    }
                                }
                            }

                            SimpleDateFormat out_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, String.valueOf(out_format.format(change_max)));
                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});


                        } catch (Exception e) {
                        }
                        int i = 0;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("synchronization", sync_import);
                    Log.d(TAG, String.valueOf(parameters));
                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }

    }
}