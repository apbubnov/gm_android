package ru.ejevikaapp.authorization.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ejevikaapp.authorization.Activity_add_svetiln;
import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.Class.Select_work;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;
import ru.ejevikaapp.authorization.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_general_infor extends Fragment implements View.OnClickListener {

    TextView id_proj, dealer_cl, components_sum_total, mounting_sum, total_sum, final_amount, final_amount_disc, final_transport, final_transport_sum, text2, text3;
    Calendar dateAndTime = Calendar.getInstance();
    Calendar dateAndTime2 = Calendar.getInstance();
    EditText name_cl, contact_cl, address_cl, notes_cl, notes_gm_chief, notes_gm_calc, ed_discount, edit_transport_0, edit_transport_1, edit_transport_21,
            edit_transport_22, c_address, c_house, с_body, c_porch, c_floor, c_room, c_code;

    ScrollView view_general_inform;
    String id_cl;
    String id_project;
    String phone;
    String fio;
    String pro_info;
    String calc_date;
    String dealer_id;
    String item;
    String S;
    String P;
    String table_name;
    String table_name_title;
    static String components_diffusers = "";
    static String components_fixtures = "";
    static String components_corn = "";
    static String components_ecola = "";
    static String components_hoods = "";
    static String components_pipes = "";
    String transport = "";
    String distance_col = "";
    String distance = "";
    static String check_client = "";
    static String check_clients_contacts = "";

    String jsonProjects = "";
    static String jsonCalc = "";
    static String jsonImage = "";
    static String jsonClient = "";
    static String jsonClient_Contacts = "";
    String jsonFixtures = "";
    String jsonEcola = "";
    String jsonCornice = "";
    String jsonPipes = "";
    String jsonHoods = "";
    String jsonDiffusers = "";
    static String global_results = "";
    String time_h = "", time_brig, id_b;

    Button new_calc, btn_date, contract, failure, add_contact, btn_transport_ok, btn_discount_ok, btn_date_mount, save_proj;

    static DBHelper dbHelper;
    View view;

    TextView DateTime, S_and_P, DateTime_mount;

    String[] time_zam = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00",
            "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"};

    int discount = 0, count_calc = 0, bt_i = 0, ch_i = 0;

    ArrayList id_calcul = new ArrayList();

    Spinner sp_date, sp_brigade, sp_brigade_free;

    double total = 0, sum_transport = 0.0;

    boolean flag;

    HashMap postData = new HashMap();

    List nameValuePairs = new ArrayList(56);
    JSONObject postDataParams = new JSONObject();

    JSONObject jsonObjectCalculation_id = new JSONObject();
    static JSONObject jsonObjectCalculation = new JSONObject();
    static JSONObject jsonObjectProject = new JSONObject();

    static RequestQueue requestQueue;

    String project_bd;

    Map<String, String> parameters = new HashMap<String, String>();

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date_zam = df.format(dateAndTime.getTime());
    String date_mount = df.format(dateAndTime.getTime());
    LinearLayout.LayoutParams titleViewParams, titleViewParams2;

    Button btn;
    private List<Button> BtnList = new ArrayList<Button>();
    private List<CheckBox> CheckBoxList = new ArrayList<CheckBox>();
    int i = 0;

    RadioButton rb_transport_0, rb_transport_1, rb_transport_2;

    TableLayout table_l;
    LinearLayout.LayoutParams lin_calc;
    LinearLayout mainL, mainC, mainL2;

    static org.json.simple.JSONObject jsonObjectClient = new org.json.simple.JSONObject();
    static org.json.simple.JSONObject jsonObjectClient_Contacts = new org.json.simple.JSONObject();

    ArrayList<String> time_free = new ArrayList<String>();
    ArrayList<Select_work> sel_work = new ArrayList<>();
    ArrayList<String> name_zamer_id = new ArrayList<String>();

    ListView list_work;

    AutoCompleteTextView addressText;
    List<String> addressList = new ArrayList<String>();

    public Fragment_general_infor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_general_infor, container, false);

        mainL = (LinearLayout) view.findViewById(R.id.phone_lay1);
        titleViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleViewParams.height = 150;
        titleViewParams.width = 150;
        titleViewParams.setMargins(40, 2, 0, 20);

        mainL2 = (LinearLayout) view.findViewById(R.id.phone_lay2);
        titleViewParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        titleViewParams2.weight = 1;
        titleViewParams2.setMargins(45, 0, 0, 0);

        mainC = (LinearLayout) view.findViewById(R.id.linear_calc);
        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.setMargins(0, 2, 0, 0);

        table_l = (TableLayout) view.findViewById(R.id.table_la);
        list_work = (ListView) view.findViewById(R.id.list_work);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
        id_cl = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
        id_project = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SPI.getString("", "");

        id_proj = (TextView) view.findViewById(R.id.id_proj);
        id_proj.setText("Информация по проекту № " + id_project);

        final_transport = (TextView) view.findViewById(R.id.final_transport);
        final_transport_sum = (TextView) view.findViewById(R.id.final_transport_sum);

        name_cl = (EditText) view.findViewById(R.id.name_cl);
        contact_cl = (EditText) view.findViewById(R.id.contact_cl);
        ed_discount = (EditText) view.findViewById(R.id.ed_discount);
        notes_cl = (EditText) view.findViewById(R.id.notes_cl);
        notes_gm_chief = (EditText) view.findViewById(R.id.notes_gm_chief);
        notes_gm_calc = (EditText) view.findViewById(R.id.notes_gm_calc);

        c_address = (EditText) view.findViewById(R.id.c_address);
        c_house = (EditText) view.findViewById(R.id.c_house);
        с_body = (EditText) view.findViewById(R.id.с_body);
        c_porch = (EditText) view.findViewById(R.id.c_porch);
        c_floor = (EditText) view.findViewById(R.id.c_floor);
        c_room = (EditText) view.findViewById(R.id.c_room);
        c_code = (EditText) view.findViewById(R.id.c_code);

        edit_transport_1 = (EditText) view.findViewById(R.id.edit_transport_1);
        edit_transport_1.setVisibility(View.GONE);

        edit_transport_21 = (EditText) view.findViewById(R.id.edit_transport_21);
        edit_transport_21.setVisibility(View.GONE);

        edit_transport_22 = (EditText) view.findViewById(R.id.edit_transport_22);
        edit_transport_22.setVisibility(View.GONE);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select transport, distance, distance_col "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where _id=?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    transport = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    distance = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    distance_col = c.getString(c.getColumnIndex(c.getColumnName(2)));
                } while (c.moveToNext());
            }
        }
        c.close();

        view_general_inform = (ScrollView) view.findViewById(R.id.view_general_inform);
        rb_transport_0 = (RadioButton) view.findViewById(R.id.rb_transport_0);
        rb_transport_1 = (RadioButton) view.findViewById(R.id.rb_transport_1);
        rb_transport_2 = (RadioButton) view.findViewById(R.id.rb_transport_2);

        if (distance.equals("0")) {
            distance = "1";
        }
        if (distance_col.equals("0")) {
            distance_col = "1";
        }

        if (transport.equals("0")) {
            rb_transport_0.setChecked(true);
            edit_transport_1.setVisibility(View.GONE);
            edit_transport_21.setVisibility(View.GONE);
            edit_transport_22.setVisibility(View.GONE);
        } else if (transport.equals("1")) {
            rb_transport_1.setChecked(true);
            edit_transport_1.setVisibility(View.VISIBLE);
            edit_transport_1.setText(distance_col);
            edit_transport_21.setVisibility(View.GONE);
            edit_transport_22.setVisibility(View.GONE);
        } else if (transport.equals("2")) {
            rb_transport_2.setChecked(true);
            edit_transport_1.setVisibility(View.GONE);
            edit_transport_21.setVisibility(View.VISIBLE);
            edit_transport_21.setText(distance);
            edit_transport_22.setVisibility(View.VISIBLE);
            edit_transport_22.setText(distance_col);
        }

        RadioGroup radGrp = (RadioGroup) view.findViewById(R.id.radios_transport);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_transport_0:
                        edit_transport_1.setVisibility(View.GONE);
                        edit_transport_21.setVisibility(View.GONE);
                        edit_transport_22.setVisibility(View.GONE);
                        transport = "0";
                        break;

                    case R.id.rb_transport_1:
                        edit_transport_1.setVisibility(View.VISIBLE);
                        edit_transport_1.setText(distance_col);
                        edit_transport_21.setVisibility(View.GONE);
                        edit_transport_22.setVisibility(View.GONE);
                        transport = "1";
                        break;

                    case R.id.rb_transport_2:
                        edit_transport_1.setVisibility(View.GONE);
                        edit_transport_21.setVisibility(View.VISIBLE);
                        edit_transport_21.setText(distance);
                        edit_transport_22.setVisibility(View.VISIBLE);
                        edit_transport_22.setText(distance_col);
                        transport = "2";
                        break;
                }
            }
        });

        DateTime = (TextView) view.findViewById(R.id.data_cl);
        DateTime_mount = (TextView) view.findViewById(R.id.data_mount);
        DateTime_mount.setText(df.format(dateAndTime.getTime()));

        dealer_cl = (TextView) view.findViewById(R.id.dealer_cl);
        S_and_P = (TextView) view.findViewById(R.id.S_and_P);
        components_sum_total = (TextView) view.findViewById(R.id.components_sum_total);
        mounting_sum = (TextView) view.findViewById(R.id.mounting_sum);
        total_sum = (TextView) view.findViewById(R.id.total_sum);
        final_amount = (TextView) view.findViewById(R.id.final_amount);
        final_amount_disc = (TextView) view.findViewById(R.id.final_amount_disc);
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);

        new_calc = (Button) view.findViewById(R.id.new_calc);
        new_calc.setOnClickListener(this);
        btn_date = (Button) view.findViewById(R.id.dateButton);
        btn_date.setOnClickListener(this);
        btn_date_mount = (Button) view.findViewById(R.id.dateButton_mount);
        btn_date_mount.setOnClickListener(this);
        contract = (Button) view.findViewById(R.id.contract);
        contract.setOnClickListener(this);
        failure = (Button) view.findViewById(R.id.failure);
        failure.setOnClickListener(this);
        add_contact = (Button) view.findViewById(R.id.add_contact);
        add_contact.setOnClickListener(this);
        btn_transport_ok = (Button) view.findViewById(R.id.btn_transport_ok);
        btn_transport_ok.setOnClickListener(this);
        btn_discount_ok = (Button) view.findViewById(R.id.btn_discount_ok);
        btn_discount_ok.setOnClickListener(this);
        save_proj = (Button) view.findViewById(R.id.save_proj);
        save_proj.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        sqlQuewy = "SELECT client_name "
                + "FROM rgzbn_gm_ceiling_clients" +
                " WHERE _id = ?";

        sp_date = (Spinner) view.findViewById(R.id.sp_date);
        sp_brigade = (Spinner) view.findViewById(R.id.sp_brigade);
        sp_brigade_free = (Spinner) view.findViewById(R.id.sp_brigade_free);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_zam);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_date.setAdapter(adapter);

        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                item = (String) parent.getItemAtPosition(selectedItemPosition);
                time_h = item.substring(0, 5);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    fio = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    name_cl.setText(fio);
                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT phone "
                + "FROM rgzbn_gm_ceiling_clients_contacts" +
                " WHERE client_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    phone = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    btn(phone);

                } while (c.moveToNext());
            }
        }
        c.close();

        String full_ad = "";
        discount = 0;
        sqlQuewy = "SELECT project_info, project_calculation_date, project_note "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_project});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    pro_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    full_ad = pro_info;

                    calc_date = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    SimpleDateFormat out_format = null;
                    SimpleDateFormat out_format_time = null;
                    SimpleDateFormat out_format_minute = null;
                    Date change_max = null;
                    Date minute = null;

                    int hours = 0;

                    try {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        change_max = ft.parse(calc_date);

                        out_format = new SimpleDateFormat("dd.MM.yyyy");
                        out_format_minute = new SimpleDateFormat("HH");

                        hours = Integer.parseInt(out_format_minute.format(change_max))+1;

                        out_format_time = new SimpleDateFormat("HH:mm");

                        DateTime.setText(String.valueOf(out_format.format(change_max) + "\n" + out_format_time.format(change_max))
                                + " - " + hours + ":00");

                    }catch (Exception e){
                    }

                    String note = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    if (note.equals("null")){
                        note = "";
                    }

                    notes_cl.setText(note);

                } while (c.moveToNext());
            }
        }
        c.close();

        String str = "";

        for (String retval : full_ad.split(",")) {

            int indexJava = retval.indexOf("дом:");
            if (indexJava == -1) {
            } else {
                c_house.setText(retval.substring(6));
                continue;
            }

            indexJava = retval.indexOf("корпус:");
            if (indexJava == -1) {
            } else {
                с_body.setText(retval.substring(9));
                continue;
            }

            indexJava = retval.indexOf("квартира:");
            if (indexJava == -1) {
            } else {
                c_room.setText(retval.substring(11));
                continue;
            }

            indexJava = retval.indexOf("подъезд:");
            if (indexJava == -1) {
            } else {
                c_porch.setText(retval.substring(10));
                continue;
            }

            indexJava = retval.indexOf("этаж:");
            if (indexJava == -1) {
            } else {
                c_floor.setText(retval.substring(7));
                continue;
            }

            indexJava = retval.indexOf("код:");
            if (indexJava == -1) {
            } else {
                c_code.setText(retval.substring(6));
                continue;
            }
            str += retval + ",";
        }
        str = str.substring(0, str.length() - 1);

        c_address.setText(str);

        id_calcul.clear();
        i = 0;

        id_calc();

        transport();
        calc(id_calcul);

        sqlQuewy = "SELECT name "
                + "FROM rgzbn_users" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String dealer_name = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    dealer_cl.setText(dealer_name);

                } while (c.moveToNext());
            }
        }
        c.close();

        for (int j = 0; count_calc > j; j++) {
            final CheckBox chb = CheckBoxList.get(j);

            Log.d("mLog","cc = " + CheckBoxList.size());
            chb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("mLog"," тут ");
                    if (chb.isChecked()) {
                        id_calcul.add(chb.getId());
                        count_calc++;
                        Log.d("mLog","count_calc2 " );
                        calc(id_calcul);
                    } else {
                        for (int in = 0; count_calc > in; in++) {
                            String str1 = String.valueOf(chb.getId());
                            String str2 = String.valueOf(id_calcul.get(in));
                            int str3 = str1.compareTo(str2);
                            if (str3 == 0) {
                                id_calcul.remove(in);
                                Log.d("mLog","count_calc3 " );
                                calc(id_calcul);
                                count_calc--;
                                break;
                            }
                        }
                    }
                }
            });
        }

        addressText = (AutoCompleteTextView) view.findViewById(R.id.c_address);

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

        addressText.setAdapter(new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, addressList));

        return view;
    }

    private void loadAddress() throws IOException {
        String input = addressText.getText().toString();
        Log.i("loadAddress", input);

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
        url += "?input=" + input;
        url += "&location=51.661535,39.200287";
        url += "&radius=200&address&types=geocode&language=ru&key=AIzaSyBXhCzmFicI1Xs3pOmfnpr0wlK6hV125_4";
        Log.d("loadAddress", url);

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addressList.clear();
                org.json.JSONObject dataJsonObj = null;
                try {
                    dataJsonObj = new org.json.JSONObject(response);
                    JSONArray predictions = dataJsonObj.getJSONArray("predictions");

                    for (int i = 0; i < predictions.length(); i++) {
                        org.json.JSONObject prediction = predictions.getJSONObject(i);
                        org.json.JSONObject structured_formatting = prediction.getJSONObject("structured_formatting");
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
        addressText.setAdapter(new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, addressList));
    }

    void calc(ArrayList id_calcul) {

        for (int i = 0; i < id_calcul.size(); i++) {
        }

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double s = 0.0;
        double p = 0.0;
        double tmp = 0;     // компоненты со скидкой
        double tmp2 = 0;    // канвас со скидкой
        double tmp3 = 0;    // монтаж со скидкой

        double tmp_d = 0;     // компоненты
        double tmp2_d = 0;    // канвас
        double tmp3_d = 0;    // монтаж
        double total_d = 0;

        double dis = 0;

        String sqlQuewy;
        Cursor c;

        int min_sum = 0;

        for (int i = 0; id_calcul.size() > i; i++) {
            sqlQuewy = "SELECT n4, n5, components_sum, canvases_sum, mounting_sum, discount "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE project_id = ? and _id=?";
            c = db.rawQuery(sqlQuewy, new String[]{id_project, String.valueOf(id_calcul.get(i))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        S = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        P = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        s += Double.parseDouble(S);
                        p += Double.parseDouble(P);

                        try {
                            dis = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(5))));
                        }catch (Exception e){
                            dis = 0;
                        }

                        tmp_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                        Double avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                        avg = avg * ((100 - dis) / 100);
                        tmp += avg;

                        tmp2_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                        avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                        avg = avg * ((100 - dis) / 100);
                        tmp2 += avg;

                        tmp3_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                        avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                        avg = avg * ((100 - dis) / 100);
                        tmp3 += avg;

                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        sqlQuewy = "SELECT dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, " +
                "gm_canvases_margin, gm_components_margin, gm_mounting_margin "
                + "FROM rgzbn_gm_ceiling_dealer_info" +
                " WHERE dealer_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int can = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    int mar = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    int moun = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    total = tmp * 100 / (100 - can);
                    total += tmp2 * 100 / (100 - mar);
                    total += tmp3 * 100 / (100 - moun);

                    total_d = tmp_d * 100 / (100 - can);
                    total_d += tmp2_d * 100 / (100 - mar);
                    total_d += tmp3_d * 100 / (100 - moun);
                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT min_sum "
                + "FROM rgzbn_gm_ceiling_mount" +
                " WHERE user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    min_sum = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                } while (c.moveToNext());
            }
        }
        c.close();

        if (transport.equals("0")) {
            sum_transport = 0;
        } else if (transport.equals("1")) {
            int trans = 0;
            int dist_col = 0;
            int dmm = 0;
            sqlQuewy = "SELECT transport "
                    + "FROM rgzbn_gm_ceiling_mount" +
                    " WHERE user_id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        trans = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sqlQuewy = "SELECT distance_col "
                    + "FROM rgzbn_gm_ceiling_projects" +
                    " WHERE _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_project)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        dist_col = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sqlQuewy = "SELECT dealer_mounting_margin "
                    + "FROM rgzbn_gm_ceiling_dealer_info" +
                    " WHERE dealer_id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        dmm = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sum_transport = Math.round((trans * dist_col * 100 / (100 - dmm) * 100.0)) / 100;

        } else if (transport.equals("2")) {

            int dist = 0;
            int dist_col = 0;
            int dmm = 0;
            sqlQuewy = "SELECT distance, distance_col "
                    + "FROM rgzbn_gm_ceiling_projects" +
                    " WHERE _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_project)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        dist = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        dist_col = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sum_transport = dist * dist_col * 11 + 250;

            if (sum_transport < 625) {
                sum_transport = 625;
            }
        }

        final_transport_sum.setText(String.valueOf(sum_transport));

        discount = 0;
        for (int i = 0; id_calcul.size() > i; i++) {
            sqlQuewy = "SELECT discount "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE project_id = ? and _id=?";
            c = db.rawQuery(sqlQuewy, new String[]{id_project, String.valueOf(id_calcul.get(i))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        try {
                            discount += Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        }catch (Exception e){
                        }
                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        total += sum_transport;
        total_d += sum_transport;

        if (discount > 0) {
            final_amount_disc.setText("Итого/ \n" + "  - %");
            final_amount.setText(String.valueOf((Math.round(total_d) * 100.0) / 100) + "/ \n" + String.valueOf((Math.round(total) * 100.0) / 100));
        } else {
            final_amount.setText(String.valueOf(Math.round(total_d * 100)/100));
        }

        Log.d("mLog", total_d + " " + min_sum);

        if (total_d == 0) {
            final_amount.setText(String.valueOf(total_d));
        } else if (total_d < min_sum && count_calc > 0) {
            total_d = min_sum;
            final_amount.setText(String.valueOf(total_d) + " * минимальная сумма заказа "+ min_sum +" р.");
        }

        components_sum_total.setText(String.valueOf((Math.round(tmp_d + tmp2_d) * 100.0) / 100));
        mounting_sum.setText(String.valueOf((Math.round(tmp3_d) * 100.0) / 100));
        total_sum.setText(String.valueOf((Math.round(tmp_d + tmp2_d + tmp3_d) * 100.0) / 100));

        S_and_P.setText(Math.round(s * 100.0) / 100.0 + " м2 / \n" + Math.round(p * 100.0) / 100.0 + " м");
    }

    void id_calc() {

        CheckBoxList.clear();
        ch_i = 0;
        mainC.removeAllViews();

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        count_calc = 0;
        id_calcul.clear();

        String sqlQuewy = "SELECT n4, n5, calculation_title, _id "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String n4 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String n5 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    String calc_title = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    String id = c.getString(c.getColumnIndex(c.getColumnName(3)));

                    radiob(calc_title, n4, n5, id);

                    count_calc++;
                    id_calcul.add(id);

                } while (c.moveToNext());
            }
        }
        c.close();
    }

    void transport() {
        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select transport, distance, distance_col "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where _id=?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    transport = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    distance = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    distance_col = c.getString(c.getColumnIndex(c.getColumnName(2)));
                } while (c.moveToNext());
            }
        }
        c.close();

        Log.d("mLog","count_calc4 " );
        calc(id_calcul);
    }

    private void setInitialDateTime() {
        DateTime.setText(DateUtils.formatDateTime(Fragment_general_infor.this.getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        flag = true;
    }

    private void setInitialDateTime2() {
        DateTime_mount.setText(DateUtils.formatDateTime(Fragment_general_infor.this.getActivity(),
                dateAndTime2.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime2.set(Calendar.YEAR, year);
            dateAndTime2.set(Calendar.MONTH, monthOfYear);
            dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime2();

            DateTime_mount.setVisibility(View.VISIBLE);

            list_work.setVisibility(View.VISIBLE);
            sp_brigade_free.setVisibility(View.VISIBLE);
            save_proj.setVisibility(View.VISIBLE);

            date_mount = df.format(dateAndTime2.getTime());

            int count = 0;
            ArrayList<String> name_brigade = new ArrayList<String>();
            final ArrayList<String> id_brigade = new ArrayList<String>();
            ArrayList<String> id_monter = new ArrayList<String>();

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            sel_work.clear();

            String sqlQuewy = "select _id, name "
                    + "FROM rgzbn_users ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        String name = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        sqlQuewy = "select group_id "
                                + "FROM rgzbn_user_usergroup_map " +
                                "where user_id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    String group_id = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    if (group_id.equals("11")) {
                                        count++;
                                        name_brigade.add(name);
                                        id_brigade.add(id);
                                    }
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();
                    } while (c.moveToNext());
                }
            }
            c.close();

            if (count == 0) {
                count = 1;

                SharedPreferences SPI = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id = SPI.getString("", "");

                SharedPreferences SP_end = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
                String user_name = SP_end.getString("", "");

                name_brigade.add(user_name);
                id_brigade.add(user_id);
            }

            text2.setVisibility(View.VISIBLE);
            text3.setVisibility(View.VISIBLE);

            sp_brigade.setVisibility(View.VISIBLE);
            ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, name_brigade);
            sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_brigade.setAdapter(sp_adapter);

            sp_brigade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    String pos = String.valueOf(selectedItemPosition);
                    id_b = id_brigade.get(Integer.valueOf(pos));

                    brigade();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }
    };

    void brigade(){

        sel_work.clear();
        time_free.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int count = 0;
        for (int i = 9; i < 21; i++) {
            String date_mount1 = "";
            if (i == 9) {
                date_mount1 = date_mount + " 0" + i + ":00:00";
            } else {
                date_mount1 = date_mount + " " + i + ":00:00";
            }
            String sqlQuewy = "select _id, project_info, project_mounting_date, project_mounter "
                    + "FROM rgzbn_gm_ceiling_projects " +
                    "where project_mounting_date = '" + date_mount1 + "' and project_mounter =?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{id_b});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String idd = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        String project_info = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        String project_mounting_date = c.getString(c.getColumnIndex(c.getColumnName(2)));
                        String project_mounter = c.getString(c.getColumnIndex(c.getColumnName(3)));

                        double n5 = 0;
                        sqlQuewy = "select n5 "
                                + "FROM rgzbn_gm_ceiling_calculations " +
                                "where project_id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{idd});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    String n5_str = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    n5 += Double.parseDouble(n5_str);

                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();

                        sqlQuewy = "select name "
                                + "FROM rgzbn_users " +
                                "where _id = ?";
                        cc = db.rawQuery(sqlQuewy, new String[]{project_mounter});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    String name = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    sel_work.add(new Select_work(idd, i + ":00 - " + (i + 1) + ":00",
                                            project_info, name, String.valueOf(n5)));
                                    count++;
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();

                    } while (c.moveToNext());
                }  else {

                    time_free.add("  " + i + ":00");

                }
                c.close();
            }
        }

        if (count==0){
            sel_work.add(new Select_work(null, null,
                    null, null, null));
        }

        table_l.setVisibility(View.VISIBLE);

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
                return nc.getN5();
            }
        });

        final FunDapter adapter_f = new FunDapter(getActivity(), sel_work, R.layout.select_work_l, dict);

        list_work.setAdapter(adapter_f);
        setListViewHeightBasedOnChildren(list_work);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_free);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_brigade_free.setAdapter(adapter);

        sp_brigade_free.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                time_brig = sp_brigade_free.getSelectedItem().toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view_general_inform.scrollTo(0,6400);

            }
        }, 1);

    }

    View.OnClickListener getPhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();

            Button btnn = BtnList.get(editId);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+" + btnn.getText().toString()));
            startActivity(intent);
        }
    };

    View.OnLongClickListener longGetPhone = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int editId = v.getId();

            final Button btnn = BtnList.get(editId);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Удалить номер " + btnn.getText().toString() + " ?")
                    .setMessage(null)
                    .setIcon(null)
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dbHelper = new DBHelper(getActivity());
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    String id_phone = "";
                                    String sqlQuewy = "SELECT _id "
                                            + "FROM rgzbn_gm_ceiling_clients_contacts" +
                                            " WHERE phone = ? ";
                                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{btnn.getText().toString()});
                                    if (cc != null) {
                                        if (cc.moveToFirst()) {
                                            id_phone = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));

                                        }
                                    }
                                    cc.close();

                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, "_id = ?", new String[]{id_phone});

                                    Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                    getActivity().finish();
                                    startActivity(intent);

                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            "Номер удалён ", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_transport_ok:
                try {
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_TRANSPORT, transport);
                    if (transport.equals("0")) {
                        values.put(DBHelper.KEY_DISTANCE, "0");
                        values.put(DBHelper.KEY_DISTANCE_COL, "0");
                    } else if (transport.equals("1")) {
                        values.put(DBHelper.KEY_DISTANCE, "0");
                        if (edit_transport_1.getText().toString().equals("")) {
                            values.put(DBHelper.KEY_DISTANCE_COL, "1");
                        } else {
                            values.put(DBHelper.KEY_DISTANCE_COL, edit_transport_1.getText().toString());
                        }
                    } else if (transport.equals("2")) {
                        if (edit_transport_21.getText().toString().equals("")) {
                            values.put(DBHelper.KEY_DISTANCE, "1");
                        } else {
                            values.put(DBHelper.KEY_DISTANCE, edit_transport_21.getText().toString());
                        }
                        if (edit_transport_22.getText().toString().equals("")) {
                            values.put(DBHelper.KEY_DISTANCE_COL, "1");
                        } else {
                            values.put(DBHelper.KEY_DISTANCE_COL, edit_transport_22.getText().toString());
                        }
                    }
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});

                    Toast toast = Toast.makeText(getActivity(),
                            "Транспорт изменён", Toast.LENGTH_SHORT);
                    toast.show();

                    transport();

                } catch (Exception e) {
                }
                break;
            case R.id.new_calc:
                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_CLIENT_NAME, String.valueOf(name_cl.getText()));
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?", new String[]{id_cl});

                if (contact_cl.getText().length() > 0) {
                    values = new ContentValues();
                    values.put(DBHelper.KEY_PHONE, String.valueOf(contact_cl.getText()));
                    values.put(DBHelper.KEY_CLIENT_ID, id_cl);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);
                    contact_cl.setText("");
                }

                String address = c_address.getText().toString().trim();
                String str_house = c_house.getText().toString().trim();
                String body = с_body.getText().toString().trim();
                String porch = c_porch.getText().toString().trim();
                String floor = c_floor.getText().toString().trim();
                String str_room = c_room.getText().toString().trim();
                String code = c_code.getText().toString().trim();
                String full_address = address + ", дом: " + str_house;

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

                if (str_room.equals("")) {
                } else {
                    full_address += ", квартира: " + str_room;
                }

                if (code.equals("")) {
                } else {
                    full_address += ", код: " + code;
                }

                values = new ContentValues();
                values.put(DBHelper.KEY_PROJECT_INFO, full_address);
                if (flag) {
                    values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zam + " \n" + item);
                }
                values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(notes_cl.getText()));
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});

                db = dbHelper.getWritableDatabase();
                values = new ContentValues();
                values.put(DBHelper.KEY_PROJECT_DISCOUNT, String.valueOf(ed_discount.getText()));
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});

                Intent intent = new Intent(getActivity(), Activity_calcul.class);
                intent.putExtra("id_project", id_project);
                startActivity(intent);
                break;

            case R.id.dateButton:
                new DatePickerDialog(Fragment_general_infor.this.getActivity(), d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.dateButton_mount:

                LinearLayout lin_m4 = (LinearLayout) view.findViewById(R.id.lin_m4);
                lin_m4.setVisibility(View.VISIBLE);

                new DatePickerDialog(Fragment_general_infor.this.getActivity(), d2,
                        dateAndTime2.get(Calendar.YEAR),
                        dateAndTime2.get(Calendar.MONTH),
                        dateAndTime2.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.btn_discount_ok:

                for (int i = 0; i < id_calcul.size(); i++) {
                    db = dbHelper.getWritableDatabase();
                    values = new ContentValues();
                    values.put(DBHelper.KEY_DISCOUNT, String.valueOf(ed_discount.getText()));
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{String.valueOf(id_calcul.get(i))});

                    Toast toast = Toast.makeText(getActivity(),
                            "Скидка изменена", Toast.LENGTH_SHORT);
                    toast.show();
                }

                mainC.removeAllViews();
                id_calc();
                Log.d("mLog","count_calc5 " );
                calc(id_calcul);

                break;

            case R.id.add_contact:

                SharedPreferences SP = this.getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
                String gager_id = SP.getString("", "");
                int gager_id_int = Integer.parseInt(gager_id);

                if (contact_cl.getText().length() > 0) {
                    db = dbHelper.getWritableDatabase();

                    values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int * 1000000), String.valueOf(gager_id_int * 1000000 + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CLIENT_ID, id_cl);
                    values.put(DBHelper.KEY_PHONE, String.valueOf(contact_cl.getText()));
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, 0);
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    contact_cl.setText("");
                    intent = new Intent(getActivity(), Activity_inform_proj.class);
                    getActivity().finish();
                    startActivity(intent);
                }

                break;
            case R.id.contract:

                LinearLayout lin_m3 = (LinearLayout) view.findViewById(R.id.lin_m3);
                lin_m3.setVisibility(View.VISIBLE);

                db = dbHelper.getWritableDatabase();
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                text1.setVisibility(View.VISIBLE);
                notes_gm_calc.setVisibility(View.VISIBLE);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                alert();

                break;
            case R.id.save_proj:
                dog();

                break;
            case R.id.failure:
                db = dbHelper.getWritableDatabase();
                values = new ContentValues();
                values.put(DBHelper.KEY_PROJECT_STATUS, "3");
                values.put(DBHelper.KEY_PROJECT_VERDICT, "0");
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_cl});
                getActivity().finish();

                break;
        }
    }

    void dog(){
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        //for (int k = 0; id_calcul.size() > k; k++) {
        //    String id = String.valueOf(id_calcul.get(k));
        //    ContentValues values = new ContentValues();
        //    values.put(DBHelper.KEY_ID_OLD, id);
        //    values.put(DBHelper.KEY_ID_NEW, "0");
        //    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations");
        //    values.put(DBHelper.KEY_SYNC, "0");
        //    values.put(DBHelper.KEY_TYPE, "send");
        //    values.put(DBHelper.KEY_STATUS, "1");
        //    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

        //    values = new ContentValues();
        //    values.put(DBHelper.KEY_ID_OLD, id);
        //    values.put(DBHelper.KEY_ID_NEW, "0");
        //    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations_cal");
        //    values.put(DBHelper.KEY_SYNC, "0");
        //    values.put(DBHelper.KEY_TYPE, "send");
        //    values.put(DBHelper.KEY_STATUS, "1");
        //    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

        //    values = new ContentValues();
        //    values.put(DBHelper.KEY_ID_OLD, id);
        //    values.put(DBHelper.KEY_ID_NEW, "0");
        //    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations_cut");
        //    values.put(DBHelper.KEY_SYNC, "0");
        //    values.put(DBHelper.KEY_TYPE, "send");
        //    values.put(DBHelper.KEY_STATUS, "1");
        //    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
        //}

         ContentValues values = new ContentValues();
         values.put(DBHelper.KEY_ID_OLD, id_cl);
         values.put(DBHelper.KEY_ID_NEW, "0");
         values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
         values.put(DBHelper.KEY_SYNC, "0");
         values.put(DBHelper.KEY_TYPE, "send");
         values.put(DBHelper.KEY_STATUS, "1");
         db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
         String sqlQuewy = "select _id, phone " + "FROM rgzbn_gm_ceiling_clients_contacts " + "where client_id = ?";
         Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_cl)});
         if (c != null) {
             if (c.moveToFirst()) {
                 do {
                     String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                     values = new ContentValues();
                     values.put(DBHelper.KEY_ID_OLD, id);
                     values.put(DBHelper.KEY_ID_NEW, "0");
                     values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                     values.put(DBHelper.KEY_SYNC, "0");
                     values.put(DBHelper.KEY_TYPE, "send");
                     values.put(DBHelper.KEY_STATUS, "1");
                     db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                 } while (c.moveToNext());
             }
         }

         values = new ContentValues();
         values.put(DBHelper.KEY_ID_OLD, id_project);
         values.put(DBHelper.KEY_ID_NEW, 0);
         values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
         values.put(DBHelper.KEY_SYNC, "0");
         values.put(DBHelper.KEY_TYPE, "send");
         values.put(DBHelper.KEY_STATUS, "1");
         db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

         sqlQuewy = "select _id " + "FROM rgzbn_gm_ceiling_calculations " + "where project_id = ?";
         c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_project)});
         if (c != null) {
             if (c.moveToFirst()) {
                 do {
                     String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                     values = new ContentValues();

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_cornice " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String cornice = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{cornice, "rgzbn_gm_ceiling_cornice"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_diffusers " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String dif = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{dif, "rgzbn_gm_ceiling_diffusers"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_profil " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String prof = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{prof, "rgzbn_gm_ceiling_profil"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_ecola " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String ecola = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{ecola, "rgzbn_gm_ceiling_ecola"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_fixtures " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String fixtures = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{fixtures, "rgzbn_gm_ceiling_fixtures"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_hoods " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String hoods = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{hoods, "rgzbn_gm_ceiling_hoods"});
                             } while (c.moveToNext());
                         }
                     }

                     sqlQuewy = "select _id "
                             + "FROM rgzbn_gm_ceiling_pipes " +
                             "where calculation_id = ?";
                     c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                     if (c != null) {
                         if (c.moveToFirst()) {
                             do {
                                 String pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                 values = new ContentValues();
                                 values.put(DBHelper.KEY_STATUS, "1");
                                 db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                                         new String[]{pipes, "rgzbn_gm_ceiling_pipes"});
                             } while (c.moveToNext());
                         }
                     }
                 } while (c.moveToNext());
             }
         }

        values = new ContentValues();
        values.put(DBHelper.KEY_CLIENT_NAME, String.valueOf(name_cl.getText()));
        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?", new String[]{id_cl});
        if (contact_cl.getText().length() > 0) {
            values = new ContentValues();
            values.put(DBHelper.KEY_PHONE, String.valueOf(contact_cl.getText()));
            values.put(DBHelper.KEY_CLIENT_ID, id_cl);
            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);
            contact_cl.setText("");
        }

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


        values = new ContentValues();
         values.put(DBHelper.KEY_PROJECT_INFO, full_address);
        if (flag) {
            values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zam);
        } else {
            values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, String.valueOf(DateTime.getText()));
        }

        values = new ContentValues();
        values.put(DBHelper.KEY_PROJECT_MOUNTING_DATE, date_mount + time_brig + ":00");
        values.put(DBHelper.KEY_PROJECT_MOUNTER, id_b);
        values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(notes_cl.getText()));
        values.put(DBHelper.KEY_GM_CALCULATOR_NOTE, String.valueOf(notes_gm_calc.getText()));
        values.put(DBHelper.KEY_GM_CHIEF_NOTE, String.valueOf(notes_gm_chief.getText()));
        values.put(DBHelper.KEY_PROJECT_DISCOUNT, String.valueOf(ed_discount.getText()));
        values.put(DBHelper.KEY_PROJECT_VERDICT, "1");
        values.put(DBHelper.KEY_READ_BY_MOUNTER, "0");
        values.put(DBHelper.KEY_PROJECT_SUM, (Math.round(total) * 100.0) / 100);
        values.put(DBHelper.KEY_PROJECT_STATUS, "5");
        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});

        Toast toast = Toast.makeText(getActivity(), "Проект направлен в запущенные", Toast.LENGTH_SHORT);
        toast.show();

        getActivity().startService(new Intent(getActivity(), Service_Sync.class));

        getActivity().finish();
    }

    void alert() {

        LinearLayout lin_m1 = (LinearLayout) view.findViewById(R.id.lin_m1);
        lin_m1.setVisibility(View.VISIBLE);

        LinearLayout lin_m2 = (LinearLayout) view.findViewById(R.id.lin_m2);
        lin_m2.setVisibility(View.VISIBLE);

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        text1.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
        view_general_inform.scrollTo(0,6400);

            }
        }, 1);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    void btn(String title) {
        btn = new Button(getActivity());
        BtnList.add(bt_i, btn);
        btn.setId(bt_i++);
        btn.setLayoutParams(titleViewParams);
        btn.setBackgroundResource(R.drawable.rounded_button_green);
        btn.setTextSize(1);
        btn.setText(title);
        btn.setTextColor(Color.argb(0,0,0,0));
        btn.setOnLongClickListener(longGetPhone);
        btn.setOnClickListener(getPhone);
        btn.setBackgroundResource(R.raw.phone2);
        mainL.addView(btn);

        TextView txt = new TextView(getActivity());
        txt.setLayoutParams(titleViewParams2);
        txt.setTextSize(14);
        txt.setText(title);
        txt.setGravity(Gravity.LEFT);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setTextColor(Color.parseColor("#000000"));
        mainL2.addView(txt);

    }

    void radiob(String calc_title, String n4, String n5, String id) {
        CheckBox rb = new CheckBox(getActivity());
        CheckBoxList.add(ch_i, rb);
        rb.setId(Integer.parseInt(id));
        rb.setText(calc_title);
        rb.setLayoutParams(lin_calc);
        rb.setChecked(true);
        rb.setTextColor(Color.parseColor("#414099"));
        rb.setTypeface(null, Typeface.BOLD);
        mainC.addView(rb);
        ch_i++;

        TextView tx = new TextView(getActivity());
        tx.setText("S/P     " + n4 + " м2 / " + n5 + " м");
        tx.setLayoutParams(lin_calc);
        tx.setTextColor(Color.parseColor("#414099"));
        mainC.addView(tx);

        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        String sqlQuewy = "SELECT n4, n5, components_sum, canvases_sum, mounting_sum, discount "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{id});

        double s = 0.0;
        double p = 0.0;
        double tmp = 0;     // компоненты
        double tmp2 = 0;    // канвас
        double tmp3 = 0;    // монтаж
        String dis = "";

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    S = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    P = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    s += Double.parseDouble(S);
                    p += Double.parseDouble(P);

                    tmp += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    tmp2 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    tmp3 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    dis = c.getString(c.getColumnIndex(c.getColumnName(5)));
                } while (c.moveToNext());
            }
        }
        c.close();

        double totall = 0.0;
        sqlQuewy = "SELECT dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, " +
                "gm_canvases_margin, gm_components_margin, gm_mounting_margin "
                + "FROM rgzbn_gm_ceiling_dealer_info" +
                " WHERE dealer_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    int can = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    int mar = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    int moun = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    totall = tmp * 100 / (100 - can);
                    totall += tmp2 * 100 / (100 - mar);
                    totall += tmp3 * 100 / (100 - moun);
                } while (c.moveToNext());
            }
        }
        c.close();

        TextView tx1 = new TextView(getActivity());

        if (dis.equals("")) {
            dis = "0";
        }

        if (Integer.valueOf(dis) > 0) {
            String tot1 = "Итого/ \n" + dis + "%";
            String tot2 = Math.round(totall) * 100.0 / 100 + "/ \n" + String.valueOf((Math.round(totall - (totall / 100 * Integer.valueOf(dis))) * 100.0) / 100);
            tx1.setText(tot1 + "     \n" + tot2);
        } else {
            tx1.setText("Итого     " + Math.round(totall) * 100.0 / 100);
        }

        tx1.setLayoutParams(lin_calc);
        tx1.setTextColor(Color.parseColor("#414099"));
        mainC.addView(tx1);

    }

}