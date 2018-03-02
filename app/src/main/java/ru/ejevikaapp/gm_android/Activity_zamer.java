package ru.ejevikaapp.gm_android;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.BaseKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.Class.phone_edit;

public class Activity_zamer extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    TextView DateTime;
    Calendar dateAndTime = new GregorianCalendar();
    Calendar date_cr = new GregorianCalendar();

    Button btn_date, btn_add_zamer, btn_search, btn_selection;
    DBHelper dbHelper;
    EditText c_fio, c_phone, c_note, c_search, c_address, c_house, с_body, c_porch, c_floor, c_code, c_room ;
    Spinner sp_date;

    TextView t_fio, t_phone, text;

    String date_zam, date_created,jsonClient = "", user_id = "",time_h = "", dealer_id = "", id_z;

    Integer user_id_int = 0, max_id = 0;

    String[] time_zam = {"- Выберите время замера -", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00",
            "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"};

    ArrayList<Select_work> sel_work = new ArrayList<>();
    ArrayList<String> name_zamer_id = new ArrayList<String>();

    ArrayList<String> name_clients = new ArrayList<String>();
    ArrayList<String> name_clients_id = new ArrayList<String>();

    private List<Spinner> SpinnerList = new ArrayList<Spinner>();

    AutoCompleteTextView addressText;
    List<String> addressList = new ArrayList<String>();

    Map<String, String> parameters = new HashMap<String, String>();
    RequestQueue requestQueue;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time = new SimpleDateFormat("h:mm");
    String date = df.format(date_cr.getTime());
    String date_zamera = df.format(dateAndTime.getTime());

    int id_cl, sp_i = 0, ch_i=0;

    boolean bool = false;

    String name_cl = "";
    int in = 0;
    LinearLayout.LayoutParams lin_calc, layoutParams;
    LinearLayout mainL, linearLayout;

    SQLiteDatabase db;

    ListView list_work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zamer);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        SharedPreferences SP = this.getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");
        user_id_int = Integer.parseInt(user_id);

        SP = this.getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SP.getString("", "");

        list_work = (ListView)findViewById(R.id.list_work);

        text = (TextView) findViewById(R.id.text);

        DateTime = (TextView) findViewById(R.id.currentDateTime);
        setInitialDateTime();

        linearLayout = (LinearLayout) findViewById(R.id.linear_spinner);
        lin_calc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lin_calc.height = 150;

        dbHelper = new DBHelper(this);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        c_fio = (EditText) findViewById(R.id.c_fio);
        c_phone = (EditText) findViewById(R.id.c_phone);
        c_note = (EditText) findViewById(R.id.c_note);
        c_search = (EditText) findViewById(R.id.c_search);

        c_address = (EditText) findViewById(R.id.c_address);
        c_house = (EditText) findViewById(R.id.c_house);
        с_body = (EditText) findViewById(R.id.с_body);
        c_porch = (EditText) findViewById(R.id.c_porch);
        c_floor = (EditText) findViewById(R.id.c_floor);
        c_room = (EditText) findViewById(R.id.c_room);
        c_code = (EditText) findViewById(R.id.c_code);

        t_fio = (TextView) findViewById(R.id.t_fio);
        t_phone = (TextView) findViewById(R.id.t_phone);

        btn_date = (Button) findViewById(R.id.dateButton);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_add_zamer = (Button) findViewById(R.id.btn_add_zamer);
        btn_selection = (Button) findViewById(R.id.btn_selection);

        btn_date.setOnClickListener(this);
        btn_add_zamer.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_selection.setOnClickListener(this);

        c_search.setVisibility(View.GONE);
        btn_search.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        date_created = DateUtils.formatDateTime(this,                  //дата создания
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_YEAR);

        addressText = (AutoCompleteTextView) findViewById(R.id.c_address);

        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = addressText.getText().toString();
                try {
                    if (input.length() > 1)
                        loadAddress();
                } catch (IOException ex) {
                    Log.d("loadAddress", "Error in!");
                }
            }
        });

        addressText.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addressList));
    }

    private void loadAddress() throws IOException {
        String input = addressText.getText().toString();
        Log.i("loadAddress", input);

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
        url += "?input=" + input;
        url += "&location=51.661535,39.200287";
        url += "&radius=200&address&types=geocode&language=ru&key=AIzaSyBXhCzmFicI1Xs3pOmfnpr0wlK6hV125_4";
        Log.d("loadAddress", url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addressList.clear();
                JSONObject dataJsonObj = null;
                try {
                    dataJsonObj = new JSONObject(response);
                    JSONArray predictions = dataJsonObj.getJSONArray("predictions");

                    for (int i = 0; i < predictions.length(); i++) {
                        JSONObject prediction = predictions.getJSONObject(i);
                        JSONObject structured_formatting = prediction.getJSONObject("structured_formatting");
                        String address = structured_formatting.getString("main_text");
                        addressList.add(address);
                        Log.d("loadAddress", address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateAdapterAddress();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("loadAddress", error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void updateAdapterAddress() {
        addressText.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, addressList));
    }

    private void setInitialDateTime() {
        DateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        date_zam = DateUtils.formatDateTime(this,                  //дата замера
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

            DateTime.setVisibility(View.VISIBLE);

            int count = 0;

            sel_work.clear();

            String sqlQuewy = "select _id "
                    + "FROM rgzbn_users ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        Log.d("mLog", "id = " + id);
                        sqlQuewy = "select * "
                                + "FROM rgzbn_user_usergroup_map " +
                                "where user_id = ? and group_id = 22 ";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                count++;
                                name_zamer_id.add(id);
                            }
                        }
                        cc.close();
                    } while (c.moveToNext());
                }
            }
            c.close();

            if (count == 0) {
                count++;
                name_zamer_id.add(user_id);
            }

            for (int i = 9; i < 21; i++) {
                for (int j = 0; count > j; j++) {
                    String date_zamera1 = date_zamera + " " + i + ":00:00";
                    sqlQuewy = "select _id, project_info, project_calculation_date, project_calculator "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where project_calculation_date = '" + date_zamera1 + "' and project_calculator =?";
                    c = db.rawQuery(sqlQuewy, new String[]{name_zamer_id.get(j)});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                String idd = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                String project_info = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                String project_calculation_date = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                String project_calculator = c.getString(c.getColumnIndex(c.getColumnName(3)));

                                Log.d("mLog", idd + " " + project_calculation_date + " " + project_calculator);
                                sqlQuewy = "select name, _id "
                                        + "FROM rgzbn_users " +
                                        "where _id = ?";
                                Cursor cc = db.rawQuery(sqlQuewy, new String[]{project_calculator});
                                if (cc != null) {
                                    if (cc.moveToFirst()) {
                                        do {
                                            String name = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                            String id = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                            sel_work.add(new Select_work(idd, i + ":00 - " + (i + 1) + ":00",
                                                    project_info, name, null));

                                        } while (cc.moveToNext());
                                    }
                                }
                                cc.close();

                            } while (c.moveToNext());
                        } else {

                            String name = "";
                            String id = "";
                            sqlQuewy = "select name, _id "
                                    + "FROM rgzbn_users " +
                                    "where _id = ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{name_zamer_id.get(j)});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    do {
                                        name = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                        id = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                    } while (cc.moveToNext());
                                }
                            }
                            cc.close();

                            sel_work.add(new Select_work(id, i + ":00 - " + (i + 1) + ":00",
                                    "", name, null));
                        }
                    }
                    c.close();
                }
            }

            alert();
        }
    };

    void alert() {

        TableLayout table_l = (TableLayout) findViewById(R.id.table_l);
        table_l.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);

        BindDictionary<Select_work> dict = new BindDictionary<>();
        dict.addStringField(R.id.c_time, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getTime();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getAddres();
            }
        });
        dict.addStringField(R.id.c_name, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getName();
            }
        });

        final FunDapter adapter = new FunDapter(this, sel_work, R.layout.select_work_l, dict);
        list_work.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_work);

        final View[] v = new View[1];
        list_work.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                try {
                    v[0].setBackgroundColor(Color.WHITE);
                }catch (Exception e){
                }

                Select_work selectedid = sel_work.get(position);
                id_z = selectedid.getId();
                if (selectedid.getAddres().equals("")){
                    String time = selectedid.getTime();
                    view.setBackgroundColor(Color.LTGRAY);
                    time_h = time.substring(0,time.length()-8);

                    v[0] = view;
                    Toast toast = Toast.makeText(Activity_zamer.this,
                            "Замер выбран на " + time, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(Activity_zamer.this,
                            "Этот замерщик занят, выберите другого замерщика или другое время", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
        String dealer_calc = SP.getString("", "");
        if (dealer_calc.equals("true")){
            int max_id_proj = Integer.parseInt(getIntent().getStringExtra("project"));

            String sqlQuewy = "SELECT _id "
                    + "FROM rgzbn_gm_ceiling_projects " +
                    "WHERE _id = ? ";
            Cursor cc = db.rawQuery(sqlQuewy, new String[]{String.valueOf(max_id_proj)});
            if (cc != null) {
                if (cc.moveToFirst()) {
                    do {
                        // если создался проект
                    } while (cc.moveToNext());
                } else {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, "project_id = ?",
                            new String[]{String.valueOf(max_id_proj)});
                }
            }
            cc.close();

        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void spinnerL() {
        final Spinner sp = new Spinner(this);
        SpinnerList.add(sp_i, sp);
        sp.setId(sp_i++);
        sp.setLayoutParams(lin_calc);

        linearLayout.addView(sp);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name_clients);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                name_cl = sp.getSelectedItem().toString();
                in = sp.getSelectedItemPosition();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        max_id = Integer.valueOf(name_clients_id.get(in));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:

                linearLayout.removeAllViews();
                name_clients.clear();

                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                String name_c = c_search.getText().toString();

                if (name_c.equals("")) {
                    String sqlQuewy = "SELECT client_name, _id "
                            + "FROM rgzbn_gm_ceiling_clients";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                name_clients.add(cc.getString(cc.getColumnIndex(cc.getColumnName(0))));
                                name_clients_id.add(cc.getString(cc.getColumnIndex(cc.getColumnName(1))));
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                } else {
                    String sqlQuewy = "SELECT client_name, _id "
                            + "FROM rgzbn_gm_ceiling_clients" +
                            " WHERE client_name LIKE('%" + name_c + "%') and dealer_id = ?";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{dealer_id});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                name_clients.add(cc.getString(cc.getColumnIndex(cc.getColumnName(0))));
                                name_clients_id.add(cc.getString(cc.getColumnIndex(cc.getColumnName(1))));
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                }

                if (name_clients.size() > 0) {
                    spinnerL();
                }

                break;

            case R.id.btn_selection:

                if (btn_selection.getText().toString().equals("    Выбрать существующего клиента    ")) {

                    c_fio.setVisibility(View.GONE);
                    c_phone.setVisibility(View.GONE);
                    t_fio.setVisibility(View.GONE);
                    t_phone.setVisibility(View.GONE);
                    c_search.setVisibility(View.VISIBLE);
                    btn_search.setVisibility(View.VISIBLE);
                    btn_selection.setText("    Добавить нового клиента    ");
                    bool = true;

                } else if (btn_selection.getText().toString().equals("    Добавить нового клиента    ")) {

                    c_fio.setVisibility(View.VISIBLE);
                    c_phone.setVisibility(View.VISIBLE);
                    t_fio.setVisibility(View.VISIBLE);
                    t_phone.setVisibility(View.VISIBLE);
                    c_search.setVisibility(View.GONE);
                    btn_search.setVisibility(View.GONE);
                    btn_selection.setText("    Выбрать существующего клиента    ");
                    bool = false;

                    linearLayout.removeAllViews();
                    name_clients.clear();
                }

                break;

            case R.id.dateButton:

                new DatePickerDialog(this, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();

                break;

            case R.id.btn_add_zamer:

                db = dbHelper.getWritableDatabase();
                String fio = c_fio.getText().toString().trim();
                String phone = c_phone.getText().toString().trim();
                String search = c_search.getText().toString();
                String address = c_address.getText().toString().trim();
                String house = c_house.getText().toString().trim();
                String body = с_body.getText().toString().trim();
                String porch = c_porch.getText().toString().trim();
                String floor = c_floor.getText().toString().trim();
                String room = c_room.getText().toString().trim();
                String code = c_code.getText().toString().trim();
                String full_address = address + ", дом: " + house;

                if (body.equals("")){
                } else {
                    full_address += ", корпус: " + body;
                }

                if (porch.equals("")){
                } else {
                    full_address += ", подъезд: " + porch;
                }

                if (floor.equals("")){
                } else {
                    full_address += ", этаж: " + floor;
                }

                if (room.equals("")){
                } else {
                    full_address += ", квартира: " + room;
                }

                if (code.equals("")){
                } else {
                    full_address += ", код: " + code;
                }

                if (!bool) {
                    if (fio.length() > 0 && phone.length() > 0 && address.length() > 0 && house.length()>0 && time_h.length()>0) {
                        ContentValues values = new ContentValues();
                        try {
                            String sqlQuewy = "";
                            Cursor c;
                            max_id = 0;
                            try {
                                sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_clients " +
                                        "where _id>? and _id<?";
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 1000000), String.valueOf(user_id_int * 1000000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id = user_id_int * 1000000 + 1;
                            }

                            values.put(DBHelper.KEY_ID, max_id);
                            values.put(DBHelper.KEY_CLIENT_NAME, fio);
                            values.put(DBHelper.KEY_CLIENT_DATA_ID, "");
                            values.put(DBHelper.KEY_TYPE_ID, "1");
                            values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                            values.put(DBHelper.KEY_MANAGER_ID, "");
                            values.put(DBHelper.KEY_CREATED, date);
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

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
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 1000000), String.valueOf(user_id_int * 1000000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id_contac++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id_contac = user_id_int * 1000000 + 1;
                            }

                            values.put(DBHelper.KEY_ID, max_id_contac);
                            values.put(DBHelper.KEY_CLIENT_ID, max_id);
                            try {
                                phone_edit pe = new phone_edit();
                                values.put(DBHelper.KEY_PHONE, pe.edit(phone));
                            } catch (Exception e) {
                            }
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

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
                            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id)});
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
                            SharedPreferences SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
                            String dealer_calc = SP.getString("", "");
                            if (dealer_calc.equals("true")){

                                max_id_proj = Integer.parseInt(getIntent().getStringExtra("project"));
                                int id_calculation = Integer.parseInt(getIntent().getStringExtra("calculation"));

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, id_calculation);
                                values.put(DBHelper.KEY_ID_NEW, 0);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "send");
                                values.put(DBHelper.KEY_STATUS, "1");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            } else {
                                try {
                                    sqlQuewy = "select MAX(_id) "
                                            + "FROM rgzbn_gm_ceiling_projects " +
                                            "where _id>? and _id<?";
                                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 1000000), String.valueOf(user_id_int * 1000000 + 99999)});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                max_id_proj++;
                                            } while (c.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    max_id_proj = user_id_int * 1000000 + 1;
                                }
                            }

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id_proj);
                            values.put(DBHelper.KEY_STATE, "1");
                            values.put(DBHelper.KEY_CLIENT_ID, id_cl);
                            values.put(DBHelper.KEY_PROJECT_INFO, full_address);
                            values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zamera + " " + time_h + ":00");
                            values.put(DBHelper.KEY_PROJECT_CALCULATOR, id_z);
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_DATE, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_START, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_END, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTER, "null");
                            values.put(DBHelper.KEY_CREATED, date);
                            values.put(DBHelper.KEY_CREATED_BY, user_id_int);
                            values.put(DBHelper.KEY_MODIFIED_BY, user_id_int);
                            values.put(DBHelper.KEY_PROJECT_STATUS, "1");
                            values.put(DBHelper.KEY_WHO_CALCULATE, "1");
                            values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                            values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                            values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                            values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                            values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                            values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                            values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(c_note.getText()));
                            values.put(DBHelper.KEY_TRANSPORT, "1");
                            values.put(DBHelper.KEY_DISTANCE, "0");
                            values.put(DBHelper.KEY_DISTANCE_COL, "1");
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id_proj);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            Toast toast = Toast.makeText(this,
                                    "Замер добавлен", Toast.LENGTH_SHORT);
                            toast.show();

                            startService(new Intent(this, Service_Sync.class));

                            finish();

                        } catch (NullPointerException e) {
                            Toast toast = Toast.makeText(this,
                                    "Произошла какая-та ошибка....", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    } else {
                        Toast toast = Toast.makeText(this,
                                "У Вас что-то незаполнено ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    if (search.length() > 0 && address.length() > 0 && time_h.length()>0) {
                        ContentValues values = new ContentValues();
                        try {
                            String sqlQuewy = "";
                            Cursor c;

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
                            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id)});
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
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 1000000), String.valueOf(user_id_int * 1000000 + 99999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id_proj++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id_proj = user_id_int * 1000000 + 1;
                            }

                            String usergroup = "";
                            String who_calc = "";
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

                            if (usergroup.equals("21") || usergroup.equals("22")){ // замерщик
                                who_calc = "1";
                            } else if (usergroup.equals("14")) {    // дилер
                                who_calc = "0";
                            }

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id_proj);
                            values.put(DBHelper.KEY_STATE, "1");
                            values.put(DBHelper.KEY_CLIENT_ID, max_id);
                            values.put(DBHelper.KEY_PROJECT_INFO, full_address);
                            values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zamera + " " + time_h + ":00");
                            values.put(DBHelper.KEY_PROJECT_CALCULATOR, id_z);
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_DATE, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_START, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTING_END, "0000-00-00 00:00:00");
                            values.put(DBHelper.KEY_PROJECT_MOUNTER, "null");
                            values.put(DBHelper.KEY_CREATED, date);
                            values.put(DBHelper.KEY_CREATED_BY, user_id_int);
                            values.put(DBHelper.KEY_MODIFIED_BY, user_id_int);
                            values.put(DBHelper.KEY_PROJECT_STATUS, "1");
                            values.put(DBHelper.KEY_WHO_CALCULATE, who_calc);
                            values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                            values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                            values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                            values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                            values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                            values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                            values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(c_note.getText()));
                            values.put(DBHelper.KEY_TRANSPORT, "1");
                            values.put(DBHelper.KEY_DISTANCE, "0");
                            values.put(DBHelper.KEY_DISTANCE_COL, "1");
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id_proj);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            startService(new Intent(this, Service_Sync.class));

                            Toast toast = Toast.makeText(this,
                                    "Замер добавлен", Toast.LENGTH_SHORT);
                            toast.show();

                            finish();

                        } catch (NullPointerException e) {
                            Toast toast = Toast.makeText(this,
                                    "Произошла какая-та ошибка....", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    } else {
                        Toast toast = Toast.makeText(this,
                                "У Вас что-то неправильно заполнено ", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
                break;
        }
    }
}