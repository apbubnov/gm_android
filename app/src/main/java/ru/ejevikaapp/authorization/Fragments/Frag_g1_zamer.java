package ru.ejevikaapp.authorization.Fragments;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ejevikaapp.authorization.Activity_add_svetiln;
import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

import static android.content.Context.MODE_PRIVATE;
import static ru.ejevikaapp.authorization.R.id.currentDateTime;

public class Frag_g1_zamer extends Fragment implements View.OnClickListener {

    TextView DateTime;
    Calendar dateAndTime = new GregorianCalendar();
    Calendar date_cr = new GregorianCalendar();

    Button btn_date, btn_add_zamer, btn_search;
    DBHelper dbHelper;
    EditText c_fio, c_phone, c_address, c_note, c_search;
    Spinner sp_date;

    String date_zam, date_created, item, jsonClient = "", jsonClient_Contacts = "", client_id = "", client_new_id,
            gager_id = "", global_results = "", time_h ="", dealer_id="";
    Integer gager_id_int = 0, max_id = 0;

    String[] time_zam = {"- Выберите время замера -", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00",
            "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"};

    Map<String, String> parameters = new HashMap<String, String>();
    RequestQueue requestQueue;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time = new SimpleDateFormat("h:mm");
    String date = df.format(date_cr.getTime());
    String date_zamera = df.format(dateAndTime.getTime());

    JSONObject jsonObjectClient = new JSONObject();
    JSONObject jsonObjectClient_Contacts = new JSONObject();
    JSONObject jsonObjectClient_Contacts_id = new JSONObject();

    int id_cl;

    public Frag_g1_zamer() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

            SharedPreferences SP = this.getActivity().getSharedPreferences("gager_id", MODE_PRIVATE);
            gager_id = SP.getString("", "");
            gager_id_int = Integer.parseInt(gager_id);

        SP = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SP.getString("", "");

        DateTime = (TextView) view.findViewById(R.id.currentDateTime);
        setInitialDateTime();

        dbHelper = new DBHelper(getActivity());

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        c_fio = (EditText) getActivity().findViewById(R.id.c_fio);
        c_phone = (EditText) getActivity().findViewById(R.id.c_phone);
        c_address = (EditText) getActivity().findViewById(R.id.c_address);
        c_note = (EditText) getActivity().findViewById(R.id.c_note);
        c_search = (EditText) getActivity().findViewById(R.id.c_search);
        sp_date = (Spinner) getActivity().findViewById(R.id.sp_date);

        btn_date = (Button) getActivity().findViewById(R.id.dateButton);
        btn_search = (Button) getActivity().findViewById(R.id.btn_search);
        btn_add_zamer = (Button) getActivity().findViewById(R.id.btn_add_zamer);

        btn_date.setOnClickListener(this);
        btn_add_zamer.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_zam);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_date.setAdapter(adapter);

        //RadioGroup radGrp = (RadioGroup)getActivity().findViewById(R.id.radios);
        //radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(RadioGroup arg0, int id) {
        //        switch(id) {
        //            case R.id.zamer_gm:

        //                break;
        //            case R.id.zamer_dealer:

        //                break;
        //        }
        //    }});


        date_created = DateUtils.formatDateTime(getActivity(),                  //дата создания
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_YEAR);

        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                item = (String) parent.getItemAtPosition(selectedItemPosition);
                time_h = item.substring(0,5);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setInitialDateTime() {
        DateTime.setText(DateUtils.formatDateTime(Frag_g1_zamer.this.getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        date_zam = DateUtils.formatDateTime(getActivity(),                  //дата замера
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);

        date_zamera = df.format(dateAndTime.getTime());
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_g1_zamer, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:


                break;

            case R.id.dateButton:
                new DatePickerDialog(Frag_g1_zamer.this.getActivity(), d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.btn_add_zamer:

                String fio = c_fio.getText().toString().trim();
                String phone = c_phone.getText().toString().trim();
                String address = c_address.getText().toString().trim();
                if (fio.length() > 0 && phone.length() > 0 && address.length() > 0) {
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        String sqlQuewy = "";
                        Cursor c;
                        max_id = 0;
                        try {
                            sqlQuewy = "select MAX(_id) "
                                    + "FROM rgzbn_gm_ceiling_clients " +
                                    "where _id>? and _id<?";
                            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int * 1000000), String.valueOf(gager_id_int * 1000000 + 999999)});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                        max_id++;
                                        Log.d("gager_id", "client 11 " + String.valueOf(max_id));
                                    } while (c.moveToNext());
                                }
                            }
                        } catch (Exception e) {
                            max_id = gager_id_int * 1000000 + 1;
                            Log.d("gager_id", "client 12 " + String.valueOf(max_id));
                        }

                        Log.d("gager_id", "client 1_1 " + gager_id + " " + gager_id_int);

                        Log.d("responce", "max_id " + max_id);

                        values.put(DBHelper.KEY_ID, max_id);
                        values.put(DBHelper.KEY_CLIENT_NAME, fio);
                        values.put(DBHelper.KEY_CLIENT_DATA_ID, "");
                        values.put(DBHelper.KEY_TYPE_ID, "1");
                        values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                        values.put(DBHelper.KEY_MANAGER_ID, "");
                        values.put(DBHelper.KEY_CREATED, date);
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                        //values = new ContentValues();
                        //values.put(DBHelper.KEY_ID_OLD, max_id);
                        //values.put(DBHelper.KEY_ID_NEW, 0);
                        //values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                        //values.put(DBHelper.KEY_SYNC, "0");
                        //values.put(DBHelper.KEY_TYPE, "send");
                        //values.put(DBHelper.KEY_STATUS, "0");
                        //db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, null, null,
                                null, null, null);
                        cursor.moveToLast();
                        int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                        id_cl = cursor.getInt(kdIndex);
                        cursor.close();

                        values = new ContentValues();
                        int max_id_contac = 0;
                        try {
                            sqlQuewy = "select MAX(_id) "
                                    + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                    "where _id>? and _id<?";
                            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int * 1000000), String.valueOf(gager_id_int * 1000000 + 999999)});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                        max_id_contac++;
                                    } while (c.moveToNext());
                                }
                            }
                        } catch (Exception e) {
                            max_id_contac = gager_id_int * 1000000 + 1;
                        }

                        Log.d("responce", "max_id_contac " + max_id);
                        values.put(DBHelper.KEY_ID, max_id_contac);
                        values.put(DBHelper.KEY_CLIENT_ID, max_id);
                        values.put(DBHelper.KEY_PHONE, phone);
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);

                        //values = new ContentValues();
                        //values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                        //values.put(DBHelper.KEY_ID_NEW, 0);
                        //values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                        //values.put(DBHelper.KEY_SYNC, "0");
                        //values.put(DBHelper.KEY_TYPE, "send");
                        //values.put(DBHelper.KEY_STATUS, "0");
                        //db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        String dealer_canvases_margin = "";
                        String dealer_components_margin = "";
                        String dealer_mounting_margin = "";
                        String gm_canvases_margin = "";
                        String gm_components_margin = "";
                        String gm_mounting_margin = "";

                        sqlQuewy = "select dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, gm_canvases_margin, " +
                                "gm_components_margin, gm_mounting_margin "
                                + "FROM rgzbn_gm_ceiling_dealer_info " +
                                "where dealer_id = ?";
                        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    dealer_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    dealer_components_margin = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                    dealer_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                    gm_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(3)));
                                    gm_components_margin = c.getString(c.getColumnIndex(c.getColumnName(4)));
                                    gm_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(5)));
                                } while (c.moveToNext());
                            }
                        }

                        int max_id_proj = 0;
                        try {
                            sqlQuewy = "select MAX(_id) "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where _id>? and _id<?";
                            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int * 1000000), String.valueOf(gager_id_int * 1000000 + 99999)});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                        max_id_proj++;
                                        Log.d("gager_id", "client 31 " + String.valueOf(max_id_proj));
                                    } while (c.moveToNext());
                                }
                            }
                        } catch (Exception e) {
                            max_id_proj = gager_id_int * 1000000 + 1;
                        }

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID, max_id_proj);
                        values.put(DBHelper.KEY_CLIENT_ID, id_cl);
                        values.put(DBHelper.KEY_PROJECT_INFO, address);
                        values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zamera + " " + time_h + ":00");
                        values.put(DBHelper.KEY_CREATED, date);
                        values.put(DBHelper.KEY_CREATED_BY, gager_id);
                        values.put(DBHelper.KEY_MODIFIED_BY, gager_id);
                        values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                        values.put(DBHelper.KEY_PROJECT_STATUS, "1");
                        values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                        values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                        values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                        values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                        values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                        values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                        values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(c_note.getText()));
                        values.put(DBHelper.KEY_TRANSPORT, "0");
                        values.put(DBHelper.KEY_DISTANCE, "0");
                        values.put(DBHelper.KEY_DISTANCE_COL, "0");
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                        //values = new ContentValues();
                        //values.put(DBHelper.KEY_ID_OLD, max_id_proj);
                        //values.put(DBHelper.KEY_ID_NEW, 0);
                        //values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                        //values.put(DBHelper.KEY_SYNC, "0");
                        //values.put(DBHelper.KEY_TYPE, "send");
                        //values.put(DBHelper.KEY_STATUS, "0");
                        //db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        JSONObject jsonObjectClient = new JSONObject();
                        sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_clients " +
                                "where _id = ?";
                        cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_cl)});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    for (int j = 0; j < 7; j++) {
                                        String status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(j)));
                                        String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                                        if (j == 0) {
                                            status = "android_id";
                                        }
                                        if (status1.equals("")) {
                                        } else {
                                            jsonObjectClient.put(status, status1);
                                        }
                                    }
                                } while (cursor.moveToNext());
                            }
                        }
                        jsonClient = String.valueOf(jsonObjectClient);
                        cursor.close();

                        Log.d("responce", "послал client " + jsonClient);

                        //new SendClientData().execute();

                        Toast toast = Toast.makeText(getActivity(),
                                "Замер добавлен", Toast.LENGTH_SHORT);
                        toast.show();

                        c_fio.setText("");
                        c_phone.setText("");
                        c_address.setText("");
                        c_note.setText("");
                        c_search.setText("");

                    } catch (NullPointerException e) {
                        Toast toast = Toast.makeText(getActivity(),
                                "Произошла какая-та ошибка....", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "У Вас что-то неправильно заполнено ", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

    class SendClientData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://test1.gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.addDataFromAndroid";
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

                    Log.d("responce","SendClientData "+ res);
                    if (res == "") {
                        Log.d("responce", "SendClientData пусто");
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
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_clients"});
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

                                        Log.d("responce", "перезапись контактов " + new_id + " " + old_id);

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

                                        Log.d("responce", "перезапись проектов " + new_id + " " + old_id);

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_CLIENT_ID, new_id);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "client_id = ?", new String[]{old_id});

                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                            }

                        } catch (Exception e) {
                            Log.d("responce", "send error " + String.valueOf(e));
                        }

                        JSONObject jsonObjectClient_Contacts = new JSONObject();

                        String sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                "where client_id = ?";
                        Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(new_id)});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    String status = "android_id";
                                    jsonObjectClient_Contacts.put(status, status1);

                                    status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                                    status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(1)));
                                    jsonObjectClient_Contacts.put(status, status1);

                                    status1 = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
                                    status = cursor.getColumnName(cursor.getColumnIndex(cursor.getColumnName(2)));
                                    jsonObjectClient_Contacts.put(status, status1);

                                } while (cursor.moveToNext());
                            }
                        }

                        jsonClient_Contacts = String.valueOf(jsonObjectClient_Contacts);

                        global_results = String.valueOf(jsonObjectClient);
                    }
                    new CheckClientsData().execute();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Проверьте подключение к интернету, или возможны работы на сервере", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    parameters.put("rgzbn_gm_ceiling_clients", "[" + jsonClient + "]");

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    class CheckClientsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://test1.gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.CheckDataFromAndroid";
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

                    Log.d("responce", "CheckClientsData " + res);

                    if (res.equals("")) {
                        Log.d("responce", "CheckClientsData пусто");
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

                    //Log.d("responce", "send_client_contacts " + jsonObjectClient_Contacts);
                    //jsonClient_Contacts = String.valueOf(jsonObjectClient_Contacts);

                    Log.d("responce", "-------------------------------------------------------");
                    new SendClientsContactsData().execute();
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Log.d("responce",jsonClient);
                    parameters.put("rgzbn_gm_ceiling_clients", "[" + global_results + "]");

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }

    class SendClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://test1.gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.addDataFromAndroid";
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

                    Log.d("responce", "SendClientsContactsData " + res);
                    if (res == "") {
                        Log.d("responce", "SendClientsContactsData пусто ");
                    } else {
                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            org.json.JSONObject dat = new org.json.JSONObject(res);
                            JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients_contacts");
                            for (int i = 0; i < id_array.length(); i++) {
                                Log.d("responce", String.valueOf(id_array.length()));

                                org.json.JSONObject client_contact = id_array.getJSONObject(i);
                                String old_id = client_contact.getString("old_id");
                                String new_id = client_contact.getString("new_id");

                                Log.d("responce", old_id + " " + new_id);

                                String sqlQuewy = "SELECT * "
                                        + "FROM history_send_to_server " +
                                        "where id_old = ? and type=? and sync = ? and name_table=?";
                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_id), "send", "0", "rgzbn_gm_ceiling_clients_contacts"});
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {

                                            Log.d("responce", old_id + " " + new_id);
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
                            Log.d("responce", "Exception sendclient_contacts " + String.valueOf(e));
                        }
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("responce", "error sendclient_contacts " + error);
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("responce", "contacts 1 " + jsonClient_Contacts);
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", "[" + jsonClient_Contacts + "]");
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }

    }

    class CheckClientsContactsData extends AsyncTask<Void, Void, Void> {

        String insertUrl = "http://test1.gm-vrn.ru/index.php?option=com_gm_ceiling&task=api.CheckDataFromAndroid";
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

                    Log.d("responce", "CheckClientsContactsData " + res);
                    if(res.equals("")) {
                        Log.d("responce", "CheckClientsContactsData пусто ");
                    }
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        org.json.JSONObject dat = new org.json.JSONObject(res);
                        JSONArray id_array = dat.getJSONArray("rgzbn_gm_ceiling_clients_contacts");
                        for (int i = 0; i < dat.length(); i++) {

                            Log.d("responce", String.valueOf(id_array));

                            org.json.JSONObject client_contact = id_array.getJSONObject(i);
                            String new_id = client_contact.getString("new_android_id");

                            Log.d("responce", new_id);

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
                    //Log.d("responce",jsonClient);
                    parameters.put("rgzbn_gm_ceiling_clients_contacts", "[" + global_results + "]");

                    return parameters;
                }
            };

            requestQueue.add(request);

            return null;
        }
    }
}