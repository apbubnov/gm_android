package ru.ejevikaapp.gm_android;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.Class.phone_edit;
import ru.ejevikaapp.gm_android.Crew.Activity_calendar;
import ru.ejevikaapp.gm_android.Crew.Activity_mounting_day;

public class Activity_zamer extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    View promptsView;
    TextView DateTime;
    Calendar dateAndTime = new GregorianCalendar();
    Calendar date_cr = new GregorianCalendar();

    Button btn_date, btn_add_zamer, btn_search, btn_selection, btn_advertisement;
    DBHelper dbHelper;
    EditText c_fio, c_phone, c_note, c_search, c_address, c_house, с_body, c_porch, c_floor, c_code, c_room;
    Spinner sp_date;

    TextView t_fio, t_phone, text;

    String date_zam, date_created, jsonClient = "", user_id = "", time_h = "", dealer_id = "", id_z, advertisement_id;

    Integer user_id_int = 0, max_id = 0;

    int day_week, year, day, dday, month, max_day;
    TextView calendar_month, advertisement;
    TableLayout tableLayout;
    private List<Button> BtnList = new ArrayList<Button>();
    ImageButton calendar_minus, calendar_plus;
    ListView list_work;

    String[] time_zam = {"- Выберите время замера -", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00",
            "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"};

    ArrayList<Select_work> sel_work = new ArrayList<>();
    ArrayList<String> name_zamer_id = new ArrayList<String>();

    ArrayList<String> name_clients = new ArrayList<String>();
    ArrayList<String> name_clients_id = new ArrayList<String>();

    private List<Spinner> SpinnerList = new ArrayList<Spinner>();

    AutoCompleteTextView addressText;
    List<String> addressList = new ArrayList<String>();

    RequestQueue requestQueue;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time = new SimpleDateFormat("h:mm");
    String date = df.format(date_cr.getTime());
    String date_zamera = df.format(dateAndTime.getTime());

    int id_cl, sp_i = 0, ch_i = 0;

    boolean bool = false;

    String name_cl = "";
    int in = 0;
    LinearLayout.LayoutParams lin_calc, layoutParams;
    LinearLayout mainL, linearLayout;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zamer);

        setTitle("Добавить замеры");

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        SharedPreferences SP = this.getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");
        user_id_int = Integer.parseInt(user_id);

        SP = this.getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SP.getString("", "");

        text = (TextView) findViewById(R.id.text);

        advertisement = (TextView) findViewById(R.id.advertisement);

        btn_advertisement = (Button) findViewById(R.id.btn_advertisement);
        btn_advertisement.setOnClickListener(this);

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

        btn_search.setOnClickListener(this);
        btn_add_zamer.setOnClickListener(this);
        btn_selection.setOnClickListener(this);

        c_search.setVisibility(View.GONE);
        btn_search.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Calendar cl = Calendar.getInstance();
        day_week = cl.get(Calendar.DAY_OF_WEEK);
        year = cl.get(Calendar.YEAR);
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH);
        calendar_minus = (ImageButton) findViewById(R.id.calendar_minus);
        calendar_minus.setOnClickListener(this);
        calendar_plus = (ImageButton) findViewById(R.id.calendar_plus);
        calendar_plus.setOnClickListener(this);
        calendar_month = (TextView) findViewById(R.id.calendar_month);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        cal_preview(0);

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

    void date() {

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
                            "where user_id = ? and (group_id = 22 or group_id = 21)";
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
                        "where project_calculation_date = '" + date_zamera1 + "' and project_calculator = ?";
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
    }

    void cal_preview(int btn_id) {

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        dday = 0;
        max_day = 0;
        String month_str = "";

        if (month == 0) {
            max_day = 31;
            calendar_month.setText("Январь");
        } else if (month == 1) {
            if ((year % 4) == 0) {
                max_day = 29;
            } else {
                max_day = 28;
            }
            calendar_month.setText("Февраль");
        } else if (month == 2) {
            max_day = 31;
            calendar_month.setText("Март");
        } else if (month == 3) {
            max_day = 30;
            calendar_month.setText("Апрель");
        } else if (month == 4) {
            max_day = 31;
            calendar_month.setText("Май");
        } else if (month == 5) {
            max_day = 30;
            calendar_month.setText("Июнь");
        } else if (month == 6) {
            max_day = 31;
            calendar_month.setText("Июль");
        } else if (month == 7) {
            max_day = 31;
            calendar_month.setText("Август");
        } else if (month == 8) {
            max_day = 30;
            calendar_month.setText("Сентябрь");
        } else if (month == 9) {
            max_day = 31;
            calendar_month.setText("Октябрь");
        } else if (month == 10) {
            max_day = 30;
            calendar_month.setText("Ноябрь");
        } else if (month == 11) {
            max_day = 31;
            calendar_month.setText("Декабрь");
        }

        calendar_month.setText(calendar_month.getText().toString() + " " + year);

        JodaTimeAndroid.init(this);
        org.joda.time.DateTime dt = new DateTime(year, month + 1, 1, 0, 0, 0, 0);
        String first_day = dt.toString("E");

        int first_day_int = 0;
        if (first_day.equals("пн")) {
            first_day_int = 0;
        } else if (first_day.equals("вт")) {
            first_day_int = 1;
        } else if (first_day.equals("ср")) {
            first_day_int = 2;
        } else if (first_day.equals("чт")) {
            first_day_int = 3;
        } else if (first_day.equals("пт")) {
            first_day_int = 4;
        } else if (first_day.equals("сб")) {
            first_day_int = 5;
        } else if (first_day.equals("вс")) {
            first_day_int = 6;
        }

        int ROWS = 6;
        int COLUMNS = 7;
        boolean flag = false;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for (int i = 0; i < ROWS; i++) {

            int count = 0;
            TableRow tableRow = new TableRow(this);

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 4f);

            for (int j = 0; j < COLUMNS; j++) {
                if ((j == first_day_int || flag) && dday < max_day) {

                    Button btn = new Button(this);
                    dday++;
                    String mount_day;

                    if (dday < 10 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-0" + dday;
                    } else if (dday < 10 && month > 9) {
                        mount_day = year + "-" + (month + 1) + "-0" + dday;
                    } else if (dday > 9 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-" + dday;
                    } else {
                        mount_day = year + "-" + (month + 1) + "-" + dday;
                    }

                    ArrayList<String> client = new ArrayList<>();

                    String sqlQuewy = "SELECT _id "
                            + "FROM rgzbn_gm_ceiling_clients " +
                            "where dealer_id = ?";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                client.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    if (dday == btn_id && btn_id != 0) {
                        flag = true;
                        btn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                        btn.setTextColor(Color.BLACK);
                        count++;
                        BtnList.add(btn);
                        btn.setId(dday - 1);
                        btn.setText(String.valueOf(dday));
                        btn.setLayoutParams(tableParams);
                        btn.setOnClickListener(getPhone);
                        tableRow.addView(btn, j);
                    } else if (today.equals(mount_day)) {
                        btn.setBackgroundResource(R.drawable.calendar_today);
                        btn.setTextColor(Color.BLACK);
                        count++;
                        flag = true;
                        BtnList.add(btn);
                        btn.setId(dday - 1);
                        btn.setText(String.valueOf(dday));
                        btn.setLayoutParams(tableParams);
                        btn.setOnClickListener(getPhone);
                        tableRow.addView(btn, j);

                    } else {
                        for (int g = 0; client.size() > g; g++) {
                            sqlQuewy = "select * "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where project_calculation_date > ? and project_calculation_date < ? and project_calculator <> ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{mount_day + " 08:00:00", mount_day + " 22:00:00", ""});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    do {
                                        btn.setBackgroundResource(R.drawable.calendar_btn_blue);
                                        btn.setTextColor(Color.BLACK);
                                    } while (cc.moveToNext());
                                } else {
                                    btn.setBackgroundResource(R.drawable.calendar_btn);
                                    btn.setTextColor(R.style.text_style_spisok);
                                }
                            }
                            cc.close();
                        }

                        count++;
                        flag = true;
                        BtnList.add(btn);
                        btn.setId(dday - 1);
                        btn.setText(String.valueOf(dday));
                        btn.setLayoutParams(tableParams);
                        btn.setOnClickListener(getPhone);
                        tableRow.addView(btn, j);
                    }
                } else {

                    Button btn = new Button(this);
                    btn.setText("");
                    btn.setBackgroundResource(R.drawable.calendar_other_month);
                    btn.setLayoutParams(tableParams);
                    tableRow.addView(btn, j);
                }
            }
            tableLayout.addView(tableRow, i);
        }
    }

    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^[7]{1}[0-9]{10}$", Pattern.CASE_INSENSITIVE);

    public static boolean validatePhone(String phoneStr) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneStr);
        return matcher.find();
    }

    View.OnClickListener getPhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            final Button btnn = BtnList.get(editId);
            int day = Integer.parseInt(btnn.getText().toString());
            String mount_day;

            if (day < 10 && month < 10) {
                mount_day = year + "-0" + (month + 1) + "-0" + day;
            } else if (day < 10 && month > 9) {
                mount_day = year + "-" + (month + 1) + "-0" + day;
            } else if (day > 9 && month < 10) {
                mount_day = year + "-0" + (month + 1) + "-" + day;
            } else {
                mount_day = year + "-" + (month + 1) + "-" + day;
            }

            LayoutInflater li = LayoutInflater.from(Activity_zamer.this);
            promptsView = li.inflate(R.layout.dialog_list, null);
            final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Activity_zamer.this);
            mDialogBuilder.setView(promptsView)
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            list_work = (ListView) promptsView.findViewById(R.id.list_work);
            setListViewHeightBasedOnChildren(list_work);
            TextView day_zamer = (TextView) promptsView.findViewById(R.id.day_zamer);
            day_zamer.setText(mount_day);
            date_zamera = mount_day;

            date();

            final AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();

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

            final FunDapter adapter = new FunDapter(Activity_zamer.this, sel_work, R.layout.select_work_l, dict);
            list_work.setAdapter(adapter);

            list_work.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Select_work selectedid = sel_work.get(position);
                    id_z = selectedid.getId();
                    if (selectedid.getAddres().equals("")) {
                        String time = selectedid.getTime();
                        view.setBackgroundColor(Color.LTGRAY);
                        time_h = time.substring(0, time.length() - 8);

                        Toast toast = Toast.makeText(Activity_zamer.this,
                                "Замер выбран на " + time, Toast.LENGTH_SHORT);
                        toast.show();

                        tableLayout.removeAllViews();
                        @SuppressLint("ResourceType") int getid = btnn.getId() + 1;
                        cal_preview(getid);

                        btnn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                        alertDialog.dismiss();
                    } else {
                        Toast toast = Toast.makeText(Activity_zamer.this,
                                "Этот замерщик занят, выберите другого замерщика или другое время", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

        }
    };

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

            if (listItem != null) {
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

                if (body.equals("")) {
                } else {
                    full_address += ", корпус: " + body;
                }

                if (porch.equals("")) {
                } else {
                    full_address += ", подъезд: " + porch;
                }

                if (floor.equals("")) {
                } else {
                    full_address += ", этаж: " + floor;
                }

                if (room.equals("")) {
                } else {
                    full_address += ", квартира: " + room;
                }

                if (code.equals("")) {
                } else {
                    full_address += ", код: " + code;
                }

                if (!bool) {
                    if (validatePhone(phone)) {
                        if (fio.length() > 0 && address.length() > 0 && house.length() > 0 && time_h.length() > 0) {
                            ContentValues values = new ContentValues();
                            try {
                                String sqlQuewy = "";
                                Cursor c;
                                max_id = 0;
                                try {
                                    sqlQuewy = "select MAX(_id) "
                                            + "FROM rgzbn_gm_ceiling_clients " +
                                            "where _id>? and _id<?";
                                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 999999)});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                max_id++;
                                            } while (c.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    max_id = user_id_int * 100000 + 1;
                                }

                                values.put(DBHelper.KEY_ID, max_id);
                                values.put(DBHelper.KEY_CLIENT_NAME, fio);
                                values.put(DBHelper.KEY_CLIENT_DATA_ID, "");
                                values.put(DBHelper.KEY_TYPE_ID, "1");
                                values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                                values.put(DBHelper.KEY_MANAGER_ID, "");
                                values.put(DBHelper.KEY_CREATED, date);
                                values.put(DBHelper.KEY_DELETED_BY_USER, "0");
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
                                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 999999)});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                max_id_contac++;
                                            } while (c.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    max_id_contac = user_id_int * 100000 + 1;
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
                                try {
                                    sqlQuewy = "select MAX(_id) "
                                            + "FROM rgzbn_gm_ceiling_projects " +
                                            "where _id>? and _id<?";
                                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 99999)});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                max_id_proj++;
                                            } while (c.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    max_id_proj = user_id_int * 100000 + 1;
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
                                String change_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                values.put(DBHelper.KEY_CHANGE_TIME, change_time);
                                values.put(DBHelper.KEY_API_PHONE_ID, advertisement_id);
                                values.put(DBHelper.KEY_DELETED_BY_USER, "0");
                                db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, max_id_proj);
                                values.put(DBHelper.KEY_ID_NEW, 0);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "send");
                                values.put(DBHelper.KEY_STATUS, "1");
                                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                int max_id_proj_history = 0;
                                try {
                                    sqlQuewy = "select MAX(_id) "
                                            + "FROM rgzbn_gm_ceiling_projects_history " +
                                            "where _id>? and _id<?";
                                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 99999)});
                                    if (c != null) {
                                        if (c.moveToFirst()) {
                                            do {
                                                max_id_proj_history = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                max_id_proj_history++;
                                            } while (c.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    max_id_proj_history = user_id_int * 100000 + 1;
                                }

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, max_id_proj_history);
                                values.put(DBHelper.KEY_PROJECT_ID, max_id_proj);
                                values.put(DBHelper.KEY_NEW_STATUS, "1");
                                values.put(DBHelper.KEY_DATE_OF_CHANGE, date);
                                db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_HISTORY, null, values);

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, max_id_proj_history);
                                values.put(DBHelper.KEY_ID_NEW, 0);
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects_history");
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
                        Toast toast = Toast.makeText(this,
                                "Проверьте ваш номер ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    if (search.length() > 0 && address.length() > 0 && time_h.length() > 0) {
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
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 99999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id_proj++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id_proj = user_id_int * 100000 + 1;
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

                            if (usergroup.equals("21") || usergroup.equals("22")) { // замерщик
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
                            values.put(DBHelper.KEY_API_PHONE_ID, advertisement_id);
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id_proj);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            int max_id_proj_history = 0;
                            try {
                                sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_projects_history " +
                                        "where _id>? and _id<?";
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 99999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id_proj_history = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id_proj_history++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id_proj_history = user_id_int * 100000 + 1;
                            }

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id_proj_history);
                            values.put(DBHelper.KEY_PROJECT_ID, max_id_proj);
                            values.put(DBHelper.KEY_NEW_STATUS, "1");
                            values.put(DBHelper.KEY_DATE_OF_CHANGE, date);
                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS_HISTORY, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id_proj_history);
                            values.put(DBHelper.KEY_ID_NEW, 0);
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects_history");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            startService(new Intent(this, Service_Sync.class));

                            Toast toast = Toast.makeText(this,
                                    "Замер добавлен", Toast.LENGTH_SHORT);
                            toast.show();

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

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

            case R.id.calendar_minus:
                month--;
                if (month < 0) {
                    month = 11;
                    year--;
                }
                tableLayout.removeAllViews();
                cal_preview(0);

                break;

            case R.id.calendar_plus:
                month++;
                if (month == 12) {
                    month = 0;
                    year++;
                }
                tableLayout.removeAllViews();
                cal_preview(0);

                break;

            case R.id.btn_advertisement:

                DBHelper dbHelper = new DBHelper(this);
                db = dbHelper.getWritableDatabase();
                final ArrayList<Select_work> sel_work = new ArrayList<>();
                final ArrayList<Integer> id_api_phones = new ArrayList<>();

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.add_api_phones, null);
                android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(this);
                mDialogBuilder.setView(promptsView);
                final EditText ed_api_phones = (EditText) promptsView.findViewById(R.id.ed_api_phones);
                Button btn_api_phones = (Button) promptsView.findViewById(R.id.btn_add_api_phones);
                final ListView list_api_phones = (ListView) promptsView.findViewById(R.id.list_api_phones);

                String sqlQuewy = "select _id, name "
                        + "FROM rgzbn_gm_ceiling_api_phones " +
                        "where dealer_id = ?";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{dealer_id});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {

                            String idd = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            String name = c.getString(c.getColumnIndex(c.getColumnName(1)));

                            sel_work.add(new Select_work(idd, null, dealer_id, name, null));

                        } while (c.moveToNext());
                    }
                    c.close();
                }

                BindDictionary<Select_work> dict = new BindDictionary<>();
                dict.addStringField(R.id.name_column, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getName();
                    }
                });

                final FunDapter adapter_f = new FunDapter(this, sel_work, R.layout.list_1column, dict);
                list_api_phones.setAdapter(adapter_f);

                final SQLiteDatabase finalDb2 = db;
                btn_api_phones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ed_api_phones.getText().toString().length() > 0) {

                            sel_work.clear();

                            int max_id = 0;
                            try {
                                String sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_api_phones " +
                                        "where _id>? and _id<?";
                                Cursor c = finalDb2.rawQuery(sqlQuewy, new String[]{String.valueOf(Integer.parseInt(dealer_id) * 100000),
                                        String.valueOf(Integer.parseInt(dealer_id) * 100000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id = Integer.parseInt(dealer_id) * 100000 + 1;
                            }

                            id_api_phones.add(max_id);
                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id);
                            values.put(DBHelper.KEY_NAME, ed_api_phones.getText().toString());
                            values.put(DBHelper.KEY_NUMBER, "");
                            values.put(DBHelper.KEY_DESCRIPTION, "");
                            values.put(DBHelper.KEY_SITE, "");
                            values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                            finalDb2.insert(DBHelper.TABLE_RGZBN_GM_CEILING_API_PHONES, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id);
                            values.put(DBHelper.KEY_ID_NEW, "0");
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_api_phones");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "0");
                            finalDb2.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            String sqlQuewy = "select _id, name "
                                    + "FROM rgzbn_gm_ceiling_api_phones " +
                                    "where dealer_id = ?";
                            Cursor c = finalDb2.rawQuery(sqlQuewy, new String[]{dealer_id});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {

                                        String idd = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                        String name = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                        sel_work.add(new Select_work(idd, null, dealer_id, name, null));

                                    } while (c.moveToNext());
                                }
                                c.close();
                            }

                            BindDictionary<Select_work> dict = new BindDictionary<>();
                            dict.addStringField(R.id.name_column, new StringExtractor<Select_work>() {
                                @Override
                                public String getStringValue(Select_work nc, int position) {
                                    return nc.getName();
                                }
                            });

                            final FunDapter adapter_f = new FunDapter(Activity_zamer.this,
                                    sel_work, R.layout.list_1column, dict);
                            list_api_phones.setAdapter(adapter_f);
                            ed_api_phones.setText("");

                        } else {
                            Toast.makeText(getApplicationContext(), "Введите название рекламы",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                final android.app.AlertDialog Alertdialog = new android.app.AlertDialog.Builder(this)
                        .setView(promptsView)
                        .setTitle("Добавьте или выберите рекламу")
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setNeutralButton("Убрать", null)
                        .setCancelable(false)
                        .create();

                final SQLiteDatabase finalDb = db;
                final SQLiteDatabase finalDb1 = db;
                Alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((android.app.AlertDialog) Alertdialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                for (int i = 0; id_api_phones.size() > i; i++) {
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    finalDb.update(DBHelper.HISTORY_SEND_TO_SERVER, values,
                                            "id_old = ? and name_table = ? and sync = ? and status = ?",
                                            new String[]{String.valueOf(id_api_phones.get(i)), "rgzbn_gm_ceiling_api_phones", "0", "0"});
                                }
                                Alertdialog.dismiss();
                            }
                        });

                        Button button_negative = ((android.app.AlertDialog) Alertdialog).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                        button_negative.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                // запонимать id которые добавлял, и удалить их из всех таблиц

                                for (int i = 0; id_api_phones.size() > i; i++) {
                                    finalDb1.delete(DBHelper.TABLE_RGZBN_GM_CEILING_API_PHONES,
                                            "_id = ?", new String[]{String.valueOf(id_api_phones.get(i))});

                                    finalDb1.delete(DBHelper.HISTORY_SEND_TO_SERVER,
                                            "id_old = ? and name_table = ? and sync = ? and status = ?",
                                            new String[]{String.valueOf(id_api_phones.get(i)), "rgzbn_gm_ceiling_api_phones", "0", "0"});
                                }

                                Alertdialog.dismiss();
                            }
                        });

                        Button button_neutral = ((android.app.AlertDialog) Alertdialog).getButton(android.app.AlertDialog.BUTTON_NEUTRAL);
                        button_neutral.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                for (int i = 0; id_api_phones.size() > i; i++) {
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    finalDb.update(DBHelper.HISTORY_SEND_TO_SERVER, values,
                                            "id_old = ? and name_table = ? and sync = ? and status = ?",
                                            new String[]{String.valueOf(id_api_phones.get(i)), "rgzbn_gm_ceiling_api_phones", "0", "0"});
                                }

                                advertisement.setText("");
                                advertisement_id = null;

                                Alertdialog.dismiss();
                            }
                        });
                    }
                });

                list_api_phones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        Select_work selectedid = sel_work.get(position);
                        advertisement_id = selectedid.getId();
                        String s_name = selectedid.getName();

                        for (int i = 0; id_api_phones.size() > i; i++) {
                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_STATUS, "1");
                            finalDb.update(DBHelper.HISTORY_SEND_TO_SERVER, values,
                                    "id_old = ? and name_table = ? and sync = ? and status = ?",
                                    new String[]{String.valueOf(id_api_phones.get(i)), "rgzbn_gm_ceiling_api_phones", "0", "0"});
                        }

                        Log.d("mLog", "advertisement_id " + advertisement_id);
                        advertisement.setText(s_name);
                        Alertdialog.dismiss();

                    }
                });

                Alertdialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                Alertdialog.show();
                break;
        }
    }

}