package ru.ejevikaapp.gm_android.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import ru.ejevikaapp.gm_android.ActivityEstimate;
import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.Activity_for_spisok;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;

import static android.content.Context.MODE_PRIVATE;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pixplicity.sharp.Sharp;

import net.danlew.android.joda.JodaTimeAndroid;

public class Fragment_general_infor extends Fragment implements View.OnClickListener {

    TextView id_proj, dealer_cl, canvases_sum, components_sum_total, mounting_sum, total_sum, final_amount,
            final_amount_disc, final_transport, final_transport_sum, text2, text3, text;
    Calendar dateAndTime = new GregorianCalendar();
    Calendar dateAndTime2 = Calendar.getInstance();
    TextView name_cl, contact_cl, notes_cl, notes_gm_chief, notes_gm_calc, ed_discount, edit_transport_1, edit_transport_21,
            edit_transport_22, c_address, advertisement;

    ScrollView view_general_inform;
    String id_cl;
    String id_project;
    String phone;
    String fio;
    String pro_info;
    String calc_date;
    String dealer_id;
    String S;
    String P;
    String transport = "";
    String distance_col = "";
    String distance = "";
    String time_h = "", time_brig, id_b, id_z;

    Button new_calc, open_notes, contract, leave, btn_transport_ok, btn_discount_ok, save_proj;
    static DBHelper dbHelper;
    View view;
    TextView DateTime, S_and_P, DateTime_mount, currentDateTime;
    int discount = 0, count_calc = 0, bt_i = 0, ch_i = 0;
    ArrayList id_calcul = new ArrayList();
    double total = 0, sum_transport = 0.0;
    static RequestQueue requestQueue;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date_zamera = df.format(dateAndTime.getTime());
    String date_mount = df.format(dateAndTime.getTime());
    LinearLayout.LayoutParams titleViewParams, titleViewParams2;

    Button btn;
    private List<Button> BtnList = new ArrayList<Button>();
    private List<Button> BtnList_mount_zamer = new ArrayList<Button>();
    private List<CheckBox> CheckBoxList = new ArrayList<CheckBox>();
    private List<Button> BtnListEstimate = new ArrayList<Button>();

    AutoCompleteTextView addressEdit;
    List<String> addressList = new ArrayList<String>();

    int i = 0;

    RadioButton rb_transport_0, rb_transport_1, rb_transport_2;

    TableLayout table_l;

    LinearLayout.LayoutParams lin_calc;
    LinearLayout mainL, mainL2, mainC, mainC2, mainC3, visible_potolok, notes;

    ArrayList<String> time_free = new ArrayList<String>();
    ArrayList<Select_work> sel_work = new ArrayList<>();
    ArrayList<String> name_zamer_id = new ArrayList<String>();
    ArrayList<Integer> estimate = new ArrayList<Integer>();

    ListView list_work;

    TextView addressText;

    int day_week, year, day, dday, month, max_day;
    TextView calendar_month;
    TableLayout tableLayout;
    View promptsView2;
    ImageButton calendar_minus, calendar_plus;

    ArrayList<String> name_brigade = new ArrayList<String>();
    ArrayList<String> id_brigade = new ArrayList<String>();

    public Fragment_general_infor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_general_infor, container, false);

        mainL = (LinearLayout) view.findViewById(R.id.phone_lay1);
        titleViewParams = new LinearLayout.LayoutParams(80, 80, 1);
        titleViewParams.setMargins(0, 0, 0, 20);

        mainL2 = (LinearLayout) view.findViewById(R.id.phone_lay2);
        titleViewParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        titleViewParams2.setMargins(0, 0, 0, 20);

        visible_potolok = (LinearLayout) view.findViewById(R.id.visible_potolok);

        mainC = (LinearLayout) view.findViewById(R.id.linear_calc);
        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.setMargins(0, 2, 0, 0);

        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.setMargins(0, 2, 0, 0);

        mainC2 = (LinearLayout) view.findViewById(R.id.linear_calc2);

        table_l = (TableLayout) view.findViewById(R.id.table_la);

        list_work = (ListView) view.findViewById(R.id.list_work);

        text = (TextView) view.findViewById(R.id.text);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //SharedPreferences SPI = this.getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
        //id_cl = SPI.getString("", "");

        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
        id_project = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SPI.getString("", "");

        id_proj = (TextView) view.findViewById(R.id.id_proj);
        id_proj.setText("Информация по проекту № " + id_project);

        final_transport = (TextView) view.findViewById(R.id.final_transport);
        final_transport_sum = (TextView) view.findViewById(R.id.final_transport_sum);
        currentDateTime = (TextView) view.findViewById(R.id.currentDateTime);

        name_cl = (TextView) view.findViewById(R.id.name_cl);
        name_cl.setOnClickListener(this);
        contact_cl = (TextView) view.findViewById(R.id.contact_cl);
        ed_discount = (TextView) view.findViewById(R.id.ed_discount);
        notes_cl = (TextView) view.findViewById(R.id.notes_cl);
        notes_gm_chief = (TextView) view.findViewById(R.id.notes_gm_chief);
        notes_gm_calc = (TextView) view.findViewById(R.id.notes_gm_calc);

        c_address = (TextView) view.findViewById(R.id.c_address);
        c_address.setOnClickListener(this);

        advertisement = (TextView) view.findViewById(R.id.advertisement);
        advertisement.setOnClickListener(this);

        edit_transport_1 = (EditText) view.findViewById(R.id.edit_transport_1);
        edit_transport_1.setVisibility(View.GONE);

        edit_transport_21 = (EditText) view.findViewById(R.id.edit_transport_21);
        edit_transport_21.setVisibility(View.GONE);

        edit_transport_22 = (EditText) view.findViewById(R.id.edit_transport_22);
        edit_transport_22.setVisibility(View.GONE);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select transport, distance, distance_col, client_id "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where _id=?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    transport = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    distance = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    distance_col = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    id_cl = c.getString(c.getColumnIndex(c.getColumnName(3)));
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
        DateTime.setOnClickListener(this);

        dealer_cl = (TextView) view.findViewById(R.id.dealer_cl);
        S_and_P = (TextView) view.findViewById(R.id.S_and_P);
        canvases_sum = (TextView) view.findViewById(R.id.canvases_sum);
        components_sum_total = (TextView) view.findViewById(R.id.components_sum_total);
        mounting_sum = (TextView) view.findViewById(R.id.mounting_sum);
        total_sum = (TextView) view.findViewById(R.id.total_sum);
        final_amount = (TextView) view.findViewById(R.id.final_amount);
        final_amount_disc = (TextView) view.findViewById(R.id.final_amount_disc);
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);

        new_calc = (Button) view.findViewById(R.id.new_calc);
        new_calc.setOnClickListener(this);
        contract = (Button) view.findViewById(R.id.contract);
        contract.setOnClickListener(this);
        leave = (Button) view.findViewById(R.id.leave);
        leave.setOnClickListener(this);
        btn_transport_ok = (Button) view.findViewById(R.id.btn_transport_ok);
        btn_transport_ok.setOnClickListener(this);
        btn_discount_ok = (Button) view.findViewById(R.id.btn_discount_ok);
        btn_discount_ok.setOnClickListener(this);
        save_proj = (Button) view.findViewById(R.id.save_proj);
        save_proj.setOnClickListener(this);
        open_notes = (Button) view.findViewById(R.id.open_notes);
        open_notes.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();

        sqlQuewy = "SELECT client_name "
                + "FROM rgzbn_gm_ceiling_clients" +
                " WHERE _id = ?";

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
        sqlQuewy = "SELECT project_info, project_calculation_date, project_note, api_phone_id "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_project});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    pro_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    if (pro_info.equals("") || pro_info.equals("null")) {
                        c_address.setText("(редактировать)");
                    } else {
                        c_address.setText(pro_info);
                    }

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

                        hours = Integer.parseInt(out_format_minute.format(change_max)) + 1;

                        out_format_time = new SimpleDateFormat("HH:mm");

                        DateTime.setText(String.valueOf(out_format.format(change_max) + " " + out_format_time.format(change_max))
                                + " - " + hours + ":00");

                    } catch (Exception e) {
                    }

                    String note = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    if (note.equals("null")) {
                        note = "";
                    }

                    notes_cl.setText(note);

                    String advt = c.getString(c.getColumnIndex(c.getColumnName(3)));

                    if (advt == null || advt.equals("null") || advt.equals("")) {
                        advertisement.setText("(изменить)");
                    } else {
                        sqlQuewy = "SELECT name "
                                + "FROM rgzbn_gm_ceiling_api_phones" +
                                " WHERE _id = ?";
                        c = db.rawQuery(sqlQuewy, new String[]{advt});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    String name = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    advertisement.setText(name);
                                } while (c.moveToNext());
                            }
                        }
                        c.close();
                    }

                } while (c.moveToNext());
            }
        }
        c.close();

        id_calcul.clear();
        i = 0;

        id_calc();

        transport();
        calc(id_calcul);

        for (int j = 0; count_calc > j; j++) {
            final CheckBox chb = CheckBoxList.get(j);

            Log.d("mLog", "cc = " + CheckBoxList.size());
            chb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("mLog", " тут ");
                    if (chb.isChecked()) {
                        id_calcul.add(chb.getId());
                        count_calc++;
                        Log.d("mLog", "count_calc2 ");
                        calc(id_calcul);
                    } else {
                        for (int in = 0; count_calc > in; in++) {
                            String str1 = String.valueOf(chb.getId());
                            String str2 = String.valueOf(id_calcul.get(in));
                            int str3 = str1.compareTo(str2);
                            if (str3 == 0) {
                                id_calcul.remove(in);
                                Log.d("mLog", "count_calc3 ");
                                calc(id_calcul);
                                count_calc--;
                                break;
                            }
                        }
                    }
                }
            });
        }

        addressText = (TextView) view.findViewById(R.id.c_address);

        Calendar cl = Calendar.getInstance();
        day_week = cl.get(Calendar.DAY_OF_WEEK);
        year = cl.get(Calendar.YEAR);
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH);
        calendar_month = (TextView) view.findViewById(R.id.calendar_month);
        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);

        calendar_minus = (ImageButton) view.findViewById(R.id.calendar_minus);
        calendar_minus.setOnClickListener(this);
        calendar_plus = (ImageButton) view.findViewById(R.id.calendar_plus);
        calendar_plus.setOnClickListener(this);

        ImageButton edit_name = (ImageButton) view.findViewById(R.id.edit_name);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] array = new String[]{"Изменить", "Открыть"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Выберите действие")
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub

                        switch (item) {
                            case 0:

                                final Context context = getActivity();
                                View promptsView;
                                LayoutInflater li = LayoutInflater.from(context);
                                promptsView = li.inflate(R.layout.layout_profile_dealer, null);
                                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                                mDialogBuilder.setView(promptsView);
                                final EditText pass = (EditText) promptsView.findViewById(R.id.ed_oldPassword);
                                final EditText pass1 = (EditText) promptsView.findViewById(R.id.ed_newPassword1);
                                final EditText pass2 = (EditText) promptsView.findViewById(R.id.ed_newPassword2);
                                final EditText email = (EditText) promptsView.findViewById(R.id.ed_email);
                                final EditText name = (EditText) promptsView.findViewById(R.id.ed_name);
                                final TextView ed_name_text = (TextView) promptsView.findViewById(R.id.ed_name_text);
                                final TextView ed_password_text = (TextView) promptsView.findViewById(R.id.ed_password_text);
                                final TextView ed_name_text3 = (TextView) promptsView.findViewById(R.id.ed_name_text3);
                                final TextView ed_name_text2 = (TextView) promptsView.findViewById(R.id.ed_name_text2);
                                final ImageView ava = (ImageView) promptsView.findViewById(R.id.ed_ava);

                                pass.setVisibility(View.GONE);
                                pass1.setVisibility(View.GONE);
                                pass2.setVisibility(View.GONE);
                                email.setVisibility(View.GONE);
                                ava.setVisibility(View.GONE);
                                ed_name_text.setVisibility(View.GONE);
                                ed_password_text.setVisibility(View.GONE);
                                ed_name_text3.setVisibility(View.GONE);
                                ed_name_text2.setVisibility(View.GONE);

                                name.setText(name_cl.getText().toString());

                                mDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        DBHelper dbHelper = new DBHelper(getActivity());
                                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                        ContentValues values = new ContentValues();
                                                        values.put(DBHelper.KEY_CLIENT_NAME, name.getText().toString());
                                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?",
                                                                new String[]{id_cl});

                                                        values = new ContentValues();
                                                        values.put(DBHelper.KEY_ID_OLD, id_cl);
                                                        values.put(DBHelper.KEY_ID_NEW, "0");
                                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                                                        values.put(DBHelper.KEY_SYNC, "0");
                                                        values.put(DBHelper.KEY_TYPE, "send");
                                                        values.put(DBHelper.KEY_STATUS, "1");
                                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                                        getActivity().finish();
                                                        Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                                        startActivity(intent);

                                                    }
                                                })
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                AlertDialog alertDialog = mDialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                                alertDialog.show();

                                break;
                            case 1:

                                SharedPreferences SP = getActivity().getSharedPreferences("activity_client", MODE_PRIVATE);
                                SharedPreferences.Editor ed = SP.edit();
                                ed.putString("", String.valueOf(id_cl));
                                ed.commit();

                                Intent intent = new Intent(getActivity(), Activity_for_spisok.class);
                                startActivity(intent);

                                break;
                        }


                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();

            }
        });

        ImageButton edit_address = (ImageButton) view.findViewById(R.id.edit_address);
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] array = {"Изменить", "Построить маршрут"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Выберите действие")
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub

                        switch (item) {
                            case 0:

                                View promptsView;
                                LayoutInflater li = LayoutInflater.from(getActivity());
                                promptsView = li.inflate(R.layout.layout_edit_address, null);
                                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
                                mDialogBuilder.setView(promptsView);

                                addressEdit = (AutoCompleteTextView) promptsView.findViewById(R.id.c_address);
                                addressEdit.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String input = addressEdit.getText().toString();
                                        try {
                                            if (input.length() > 1)
                                                loadAddress();
                                        } catch (IOException ex) {
                                            Log.d("loadAddress", "Error in!");
                                        }
                                    }
                                });

                                addressEdit.setAdapter(new ArrayAdapter<>(getActivity(),
                                        android.R.layout.simple_dropdown_item_1line, addressList));

                                final EditText c_house = (EditText) promptsView.findViewById(R.id.c_house);
                                final EditText с_body = (EditText) promptsView.findViewById(R.id.с_body);
                                final EditText c_porch = (EditText) promptsView.findViewById(R.id.c_porch);
                                final EditText c_floor = (EditText) promptsView.findViewById(R.id.c_floor);
                                final EditText c_room = (EditText) promptsView.findViewById(R.id.c_room);
                                final EditText c_code = (EditText) promptsView.findViewById(R.id.c_code);

                                String str = "";

                                for (String retval : pro_info.split(",")) {

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
                                if (str.equals("null")){
                                    addressEdit.setText("");
                                } else {
                                    addressEdit.setText(str);
                                }

                                mDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        String address = addressEdit.getText().toString().trim();
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

                                                        DBHelper dbHelper = new DBHelper(getActivity());
                                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                        ContentValues values = new ContentValues();
                                                        values.put(DBHelper.KEY_PROJECT_INFO, full_address);
                                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                                                new String[]{id_project});

                                                        values = new ContentValues();
                                                        values.put(DBHelper.KEY_ID_OLD, id_project);
                                                        values.put(DBHelper.KEY_ID_NEW, 0);
                                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                                        values.put(DBHelper.KEY_SYNC, "0");
                                                        values.put(DBHelper.KEY_TYPE, "send");
                                                        values.put(DBHelper.KEY_STATUS, "1");
                                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                                        getActivity().finish();
                                                        Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                                        startActivity(intent);

                                                    }
                                                })
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                AlertDialog alertDialog = mDialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                                alertDialog.show();

                                break;
                            case 1:
                                String uri = "geo:0,0?q=" + pro_info;
                                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(mapIntent);
                                break;
                        }


                    }
                });

                builder.setCancelable(false);
                builder.create();
                builder.show();

            }
        });

        ImageButton edit_date = (ImageButton) view.findViewById(R.id.edit_date);

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getActivity();
                View promptsView;
                LayoutInflater li = LayoutInflater.from(context);
                promptsView = li.inflate(R.layout.layout_edit_date_zamer, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                Calendar cl = Calendar.getInstance();
                day_week = cl.get(Calendar.DAY_OF_WEEK);
                year = cl.get(Calendar.YEAR);
                day = cl.get(Calendar.DAY_OF_MONTH);
                month = cl.get(Calendar.MONTH);
                calendar_month = (TextView) promptsView.findViewById(R.id.calendar_month);
                tableLayout = (TableLayout) promptsView.findViewById(R.id.tableLayout);

                ImageButton calendar_minus = (ImageButton) promptsView.findViewById(R.id.calendar_minus);
                calendar_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month--;
                        if (month < 0) {
                            month = 11;
                            year--;
                        }
                        tableLayout.removeAllViews();
                        cal_preview(0);
                    }
                });

                ImageButton calendar_plus = (ImageButton) promptsView.findViewById(R.id.calendar_plus);
                calendar_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month++;
                        if (month == 12) {
                            month = 0;
                            year++;
                        }
                        tableLayout.removeAllViews();
                        cal_preview(0);
                    }
                });

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        DBHelper dbHelper = new DBHelper(getActivity());
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        Log.d("mLog", "vybor = " + id_z);
                                        values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zamera + " " + time_h + ":00");
                                        values.put(DBHelper.KEY_PROJECT_CALCULATOR, id_z);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                                new String[]{id_project});

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_ID_OLD, id_project);
                                        values.put(DBHelper.KEY_ID_NEW, "0");
                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                        values.put(DBHelper.KEY_SYNC, "0");
                                        values.put(DBHelper.KEY_TYPE, "send");
                                        values.put(DBHelper.KEY_STATUS, "1");
                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                        startActivity(intent);

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                cal_preview(0);
            }
        });

        ImageButton add_phone = (ImageButton) view.findViewById(R.id.add_phone);
        add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getActivity();
                View promptsView;
                LayoutInflater li = LayoutInflater.from(context);
                promptsView = li.inflate(R.layout.layout_dialog_number_phone, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                final EditText phone = (EditText) promptsView.findViewById(R.id.phone);

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (phone.getText().toString().length() == 11) {
                                            DBHelper dbHelper = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            int max_id_contac = 0;
                                            int user_id_int = Integer.parseInt(dealer_id);

                                            try {
                                                String sqlQuewy = "select MAX(_id) "
                                                        + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                                        "where _id>? and _id<?";
                                                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000),
                                                        String.valueOf(user_id_int * 100000 + 999999)});
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
                                            values.put(DBHelper.KEY_CLIENT_ID, id_cl);
                                            values.put(DBHelper.KEY_PHONE, phone.getText().toString());
                                            db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);

                                            values = new ContentValues();
                                            values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                                            values.put(DBHelper.KEY_ID_NEW, 0);
                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                                            values.put(DBHelper.KEY_SYNC, "0");
                                            values.put(DBHelper.KEY_TYPE, "send");
                                            values.put(DBHelper.KEY_STATUS, "1");
                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                            getActivity().finish();
                                            Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                            startActivity(intent);

                                        }
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences SP = getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();

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
                        if (S.equals("")) {
                            s += 0;
                        } else {
                            s += Double.parseDouble(S);
                        }
                        if (P.equals("")) {
                            p += 0;
                        } else {
                            p += Double.parseDouble(P);
                        }

                        try {
                            dis = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(5))));
                        } catch (Exception e) {
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

        int trans = 0;
        if (transport.equals("0")) {
            sum_transport = 0;
        } else if (transport.equals("1")) {
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
                        } catch (Exception e) {
                        }
                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        if (discount > 0) {
            sum_transport = sum_transport / 100 * (100 - discount);
        }

        final_transport_sum.setText(String.valueOf((Math.round(sum_transport) * 100.0) / 100));

        total += sum_transport;
        total_d += sum_transport;

        if (discount > 0) {
            final_amount_disc.setText("Итого/ \n" + "  - %");
            final_amount.setText(String.valueOf((Math.round(total_d) * 100.0) / 100) + "/ \n" + String.valueOf((Math.round(total) * 100.0) / 100));
        } else {
            final_amount.setText(String.valueOf(Math.round(total_d * 100) / 100));
        }

        if (total_d == 0) {
            final_amount.setText(String.valueOf(total_d));
        } else if (total_d < min_sum && count_calc > 0) {
            total_d = min_sum;
            final_amount.setText(String.valueOf(total_d) + " * минимальная сумма заказа " + min_sum + " р.");
        }

        canvases_sum.setText("П " + String.valueOf((Math.round(tmp2_d) * 100.0) / 100));
        components_sum_total.setText("К " + String.valueOf((Math.round(tmp_d) * 100.0) / 100));
        mounting_sum.setText("М " + String.valueOf((Math.round(tmp3_d + trans) * 100.0) / 100));
        total_sum.setText(String.valueOf((Math.round(tmp_d + tmp2_d + tmp3_d) * 100.0) / 100 + trans));

        S_and_P.setText(Math.round(s * 100.0) / 100.0 + " м2 / \n" + Math.round(p * 100.0) / 100.0 + " м");
    }

    void id_calc() {

        CheckBoxList.clear();
        BtnListEstimate.clear();
        ch_i = 0;
        mainC.removeAllViews();
        mainC2.removeAllViews();

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        count_calc = 0;
        id_calcul.clear();

        String sqlQuewy = "SELECT n4, n5, calculation_title, _id, calc_image "
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
                    String imag = c.getString(c.getColumnIndex(c.getColumnName(4)));

                    radiob(calc_title, n4, n5, id, imag);

                    count_calc++;
                    id_calcul.add(id);

                } while (c.moveToNext());
            }
        }
        c.close();

        if (id_calcul.size() == 0) {
            visible_potolok.setVisibility(View.GONE);
        }
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

        calc(id_calcul);
    }

    private void setInitialDateTime2() {
        DateTime_mount.setText(DateUtils.formatDateTime(Fragment_general_infor.this.getActivity(),
                dateAndTime2.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

    }

    DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime2.set(Calendar.YEAR, year);
            dateAndTime2.set(Calendar.MONTH, monthOfYear);
            dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime2();

            DateTime_mount.setVisibility(View.VISIBLE);

            list_work.setVisibility(View.VISIBLE);
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
        }
    };

    void brigade() {

        sel_work.clear();
        time_free.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Log.d("mLog", date_mount + " " + id_b);
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
                } else {

                    time_free.add(" " + i + ":00");

                }
                c.close();
            }
        }

        final Spinner sp_brigade_free = (Spinner) promptsView2.findViewById(R.id.sp_brigade_free);
        ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_free);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_brigade_free.setAdapter(sp_adapter);

        sp_brigade_free.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                time_brig = sp_brigade_free.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (count == 0) {
            sel_work.add(new Select_work(null, null,
                    null, null, null));
        }

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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view_general_inform.scrollTo(0, 6400);

            }
        }, 1);

    }

    View.OnClickListener getPhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();

            final Button btnn = BtnList.get(editId);

            String[] array = new String[]{"Изменить", "Позвонить", "Удалить"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Выберите действие")
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            builder.setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    // TODO Auto-generated method stub

                    switch (item) {
                        case 0:

                            final Context context = getActivity();
                            View promptsView;
                            LayoutInflater li = LayoutInflater.from(context);
                            promptsView = li.inflate(R.layout.layout_dialog_number_phone, null);
                            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                            mDialogBuilder.setView(promptsView);

                            final EditText phone = (EditText) promptsView.findViewById(R.id.phone);
                            phone.setText(btnn.getText().toString());
                            final String old_number = btnn.getText().toString();
                            String number_id = "";

                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            String sqlQuewy = "select _id "
                                    + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                    "where phone = ?";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{btnn.getText().toString()});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    do {
                                        number_id = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    } while (cc.moveToNext());
                                }
                            }
                            cc.close();

                            final String finalNumber_id = number_id;
                            mDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    if (phone.getText().toString().length() == 11) {

                                                        if (old_number.equals(phone.getText().toString())) {

                                                        } else {
                                                            DBHelper dbHelper = new DBHelper(getActivity());
                                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                            ContentValues values = new ContentValues();
                                                            values.put(DBHelper.KEY_PHONE, phone.getText().toString());
                                                            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, values, "_id = ?",
                                                                    new String[]{finalNumber_id});

                                                            values = new ContentValues();
                                                            values.put(DBHelper.KEY_ID_OLD, finalNumber_id);
                                                            values.put(DBHelper.KEY_ID_NEW, "0");
                                                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                                                            values.put(DBHelper.KEY_SYNC, "0");
                                                            values.put(DBHelper.KEY_TYPE, "send");
                                                            values.put(DBHelper.KEY_STATUS, "1");
                                                            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                                            getActivity().finish();
                                                            Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                                            startActivity(intent);
                                                        }
                                                    } else {

                                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                                                "Номер изменён ", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }

                                                }
                                            })
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            AlertDialog alertDialog = mDialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                            alertDialog.show();

                            break;
                        case 1:
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:+" + btnn.getText().toString()));
                            startActivity(intent);
                            break;
                        case 2:
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

                                                    getActivity().finish();
                                                    Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
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
                            break;
                    }
                }
            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
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

                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
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
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        ContentValues values;
        final Context context = getActivity();

        switch (v.getId()) {
            case R.id.btn_transport_ok:
                try {
                    values = new ContentValues();
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
                Intent intent = new Intent(getActivity(), Activity_calcul.class);
                intent.putExtra("id_project", id_project);
                startActivity(intent);
                break;

            case R.id.advertisement:

                DBHelper dbHelper = new DBHelper(getActivity());
                 db = dbHelper.getWritableDatabase();
                final ArrayList<Select_work> sel_work = new ArrayList<>();
                final ArrayList<Integer> id_api_phones = new ArrayList<>();

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.add_api_phones, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
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

                final FunDapter adapter_f = new FunDapter(getActivity(), sel_work, R.layout.list_1column, dict);
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

                            final FunDapter adapter_f = new FunDapter(getActivity(),
                                    sel_work, R.layout.list_1column, dict);
                            list_api_phones.setAdapter(adapter_f);
                            ed_api_phones.setText("");

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Введите название рекламы",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                final AlertDialog Alertdialog = new AlertDialog.Builder(context)
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

                        Button button = ((AlertDialog) Alertdialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                // изменить в send_to_history колонки на status = 1

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

                        Button button_negative = ((AlertDialog) Alertdialog).getButton(AlertDialog.BUTTON_NEGATIVE);
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

                        Button button_neutral = ((AlertDialog) Alertdialog).getButton(AlertDialog.BUTTON_NEUTRAL);
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

                                Integer nul = null;
                                ContentValues values = new ContentValues();
                                values.put(DBHelper.KEY_API_PHONE_ID, nul);
                                finalDb.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values,
                                        "_id = ?",
                                        new String[]{id_project});

                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID_OLD, id_project);
                                values.put(DBHelper.KEY_ID_NEW, "0");
                                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                values.put(DBHelper.KEY_SYNC, "0");
                                values.put(DBHelper.KEY_TYPE, "send");
                                values.put(DBHelper.KEY_STATUS, "1");
                                finalDb2.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                advertisement.setText("(изменить)");

                                Alertdialog.dismiss();
                            }
                        });
                    }
                });

                list_api_phones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        Select_work selectedid = sel_work.get(position);
                        String s_id = selectedid.getId();
                        String s_name = selectedid.getName();

                        for (int i = 0; id_api_phones.size() > i; i++) {
                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_STATUS, "1");
                            finalDb.update(DBHelper.HISTORY_SEND_TO_SERVER, values,
                                    "id_old = ? and name_table = ? and sync = ? and status = ?",
                                    new String[]{String.valueOf(id_api_phones.get(i)), "rgzbn_gm_ceiling_api_phones", "0", "0"});
                        }

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_API_PHONE_ID, s_id);
                        finalDb.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values,
                                "_id = ?",
                                new String[]{id_project});

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID_OLD, id_project);
                        values.put(DBHelper.KEY_ID_NEW, "0");
                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                        values.put(DBHelper.KEY_SYNC, "0");
                        values.put(DBHelper.KEY_TYPE, "send");
                        values.put(DBHelper.KEY_STATUS, "1");
                        finalDb2.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        advertisement.setText(s_name);

                        Alertdialog.dismiss();

                    }
                });

                Alertdialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                Alertdialog.show();
                break;

            case R.id.c_address:
                li = LayoutInflater.from(context);
                promptsView = li.inflate(R.layout.layout_edit_address, null);
                mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                addressEdit = (AutoCompleteTextView) promptsView.findViewById(R.id.c_address);
                addressEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = addressEdit.getText().toString();
                        try {
                            if (input.length() > 1)
                                loadAddress();
                        } catch (IOException ex) {
                            Log.d("loadAddress", "Error in!");
                        }
                    }
                });

                addressEdit.setAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, addressList));

                final EditText c_house = (EditText) promptsView.findViewById(R.id.c_house);
                final EditText с_body = (EditText) promptsView.findViewById(R.id.с_body);
                final EditText c_porch = (EditText) promptsView.findViewById(R.id.c_porch);
                final EditText c_floor = (EditText) promptsView.findViewById(R.id.c_floor);
                final EditText c_room = (EditText) promptsView.findViewById(R.id.c_room);
                final EditText c_code = (EditText) promptsView.findViewById(R.id.c_code);

                String str = "";

                for (String retval : pro_info.split(",")) {

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
                if (str.equals("null")){
                    addressEdit.setText("");
                } else {
                    addressEdit.setText(str);
                }

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        String address = addressEdit.getText().toString().trim();
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

                                        DBHelper dbHelper = new DBHelper(getActivity());
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(DBHelper.KEY_PROJECT_INFO, full_address);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                                new String[]{id_project});

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_ID_OLD, id_project);
                                        values.put(DBHelper.KEY_ID_NEW, 0);
                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                        values.put(DBHelper.KEY_SYNC, "0");
                                        values.put(DBHelper.KEY_TYPE, "send");
                                        values.put(DBHelper.KEY_STATUS, "1");
                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        c_address.setText(full_address);
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                break;

            case R.id.btn_discount_ok:

                for (int i = 0; i < id_calcul.size(); i++) {
                    values = new ContentValues();
                    values.put(DBHelper.KEY_DISCOUNT, String.valueOf(ed_discount.getText()));
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{String.valueOf(id_calcul.get(i))});

                    Toast toast = Toast.makeText(getActivity(),
                            "Скидка изменена", Toast.LENGTH_SHORT);
                    toast.show();
                }

                mainC2.removeAllViews();
                mainC.removeAllViews();
                id_calc();
                calc(id_calcul);

                break;
            case R.id.contract:
                LinearLayout lin_m3 = (LinearLayout) view.findViewById(R.id.lin_m3);
                lin_m3.setVisibility(View.VISIBLE);

                alert();

                break;
            case R.id.save_proj:
                dog();

                break;
            case R.id.leave:
                getActivity().finish();
                break;
            case R.id.name_cl:

                li = LayoutInflater.from(context);
                promptsView = li.inflate(R.layout.layout_profile_dealer, null);
                mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);
                final EditText pass = (EditText) promptsView.findViewById(R.id.ed_oldPassword);
                final EditText pass1 = (EditText) promptsView.findViewById(R.id.ed_newPassword1);
                final EditText pass2 = (EditText) promptsView.findViewById(R.id.ed_newPassword2);
                final EditText email = (EditText) promptsView.findViewById(R.id.ed_email);
                final EditText name = (EditText) promptsView.findViewById(R.id.ed_name);
                final TextView ed_name_text = (TextView) promptsView.findViewById(R.id.ed_name_text);
                final TextView ed_password_text = (TextView) promptsView.findViewById(R.id.ed_password_text);
                final TextView ed_name_text3 = (TextView) promptsView.findViewById(R.id.ed_name_text3);
                final TextView ed_name_text2 = (TextView) promptsView.findViewById(R.id.ed_name_text2);
                final ImageView ava = (ImageView) promptsView.findViewById(R.id.ed_ava);

                pass.setVisibility(View.GONE);
                pass1.setVisibility(View.GONE);
                pass2.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                ava.setVisibility(View.GONE);
                ed_name_text.setVisibility(View.GONE);
                ed_password_text.setVisibility(View.GONE);
                ed_name_text3.setVisibility(View.GONE);
                ed_name_text2.setVisibility(View.GONE);

                name.setText(name_cl.getText().toString());

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        DBHelper dbHelper = new DBHelper(getActivity());
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(DBHelper.KEY_CLIENT_NAME, name.getText().toString());
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, values, "_id = ?",
                                                new String[]{id_cl});

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_ID_OLD, id_cl);
                                        values.put(DBHelper.KEY_ID_NEW, "0");
                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                                        values.put(DBHelper.KEY_SYNC, "0");
                                        values.put(DBHelper.KEY_TYPE, "send");
                                        values.put(DBHelper.KEY_STATUS, "1");
                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                        startActivity(intent);

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                break;

            case R.id.data_cl:

                li = LayoutInflater.from(context);
                promptsView = li.inflate(R.layout.layout_edit_date_zamer, null);
                mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                Calendar cl = Calendar.getInstance();
                day_week = cl.get(Calendar.DAY_OF_WEEK);
                year = cl.get(Calendar.YEAR);
                day = cl.get(Calendar.DAY_OF_MONTH);
                month = cl.get(Calendar.MONTH);
                calendar_month = (TextView) promptsView.findViewById(R.id.calendar_month);
                tableLayout = (TableLayout) promptsView.findViewById(R.id.tableLayout);

                ImageButton calendar_minus = (ImageButton) promptsView.findViewById(R.id.calendar_minus);
                calendar_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month--;
                        if (month < 0) {
                            month = 11;
                            year--;
                        }
                        tableLayout.removeAllViews();
                        cal_preview(0);
                    }
                });

                ImageButton calendar_plus = (ImageButton) promptsView.findViewById(R.id.calendar_plus);
                calendar_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month++;
                        if (month == 12) {
                            month = 0;
                            year++;
                        }
                        tableLayout.removeAllViews();
                        cal_preview(0);
                    }
                });

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        DBHelper dbHelper = new DBHelper(getActivity());
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date_zamera + " " + time_h + ":00");
                                        values.put(DBHelper.KEY_PROJECT_CALCULATOR, id_z);
                                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                                new String[]{id_project});

                                        values = new ContentValues();
                                        values.put(DBHelper.KEY_ID_OLD, id_project);
                                        values.put(DBHelper.KEY_ID_NEW, "0");
                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                                        values.put(DBHelper.KEY_SYNC, "0");
                                        values.put(DBHelper.KEY_TYPE, "send");
                                        values.put(DBHelper.KEY_STATUS, "1");
                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                                        startActivity(intent);

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                cal_preview(0);

                break;
            case R.id.calendar_minus:
                month--;
                if (month < 0) {
                    month = 11;
                    year--;
                }
                tableLayout.removeAllViews();
                cal_preview_mount(0);

                break;

            case R.id.calendar_plus:
                month++;
                if (month == 12) {
                    month = 0;
                    year++;
                }
                tableLayout.removeAllViews();
                cal_preview_mount(0);

                break;

            case R.id.open_notes:
                notes = (LinearLayout) view.findViewById(R.id.notes);
                if (notes.getVisibility() == View.GONE) {
                    open_notes.setText("Закрыть примечания");
                    notes.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            view_general_inform.scrollTo(0, 10000);

                        }
                    }, 1);
                } else {
                    open_notes.setText("Открыть примечания");
                    notes.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void loadAddress() throws IOException {
        String input = addressEdit.getText().toString();
        Log.i("loadAddress", input);

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
        url += "?input=" + input;
        url += "&location=51.661535,39.200287";
        url += "&radius=200&address&types=geocode&language=ru&key=AIzaSyBXhCzmFicI1Xs3pOmfnpr0wlK6hV125_4";
        Log.d("loadAddress", url);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        addressEdit.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, addressList));
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

        JodaTimeAndroid.init(getActivity());
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
        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for (int i = 0; i < ROWS; i++) {

            int count = 0;
            TableRow tableRow = new TableRow(getActivity());

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 4f);

            for (int j = 0; j < COLUMNS; j++) {
                if ((j == first_day_int || flag) && dday < max_day) {

                    Button btn = new Button(getActivity());
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
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{dealer_id});
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
                        btn.setOnClickListener(getDate);
                        tableRow.addView(btn, j);
                    } else if (today.equals(mount_day)) {
                        btn.setBackgroundResource(R.drawable.calendar_today);
                        btn.setTextColor(Color.BLACK);
                        count++;
                        flag = true;
                        BtnList_mount_zamer.add(btn);
                        btn.setId(dday - 1);
                        btn.setText(String.valueOf(dday));
                        btn.setLayoutParams(tableParams);
                        btn.setOnClickListener(getDate);
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
                        BtnList_mount_zamer.add(btn);
                        btn.setId(dday - 1);
                        btn.setText(String.valueOf(dday));
                        btn.setLayoutParams(tableParams);
                        btn.setOnClickListener(getDate);
                        tableRow.addView(btn, j);

                    }

                } else {
                    Button btn = new Button(getActivity());
                    btn.setText("");
                    btn.setBackgroundResource(R.drawable.calendar_other_month);
                    btn.setLayoutParams(tableParams);
                    tableRow.addView(btn, j);
                }
            }
            tableLayout.addView(tableRow, i);
        }
    }

    View.OnClickListener getDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            final Button btnn = BtnList_mount_zamer.get(editId);
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

            LayoutInflater li = LayoutInflater.from(getActivity());
            promptsView2 = li.inflate(R.layout.dialog_list, null);
            final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
            mDialogBuilder.setView(promptsView2)
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            list_work = (ListView) promptsView2.findViewById(R.id.list_work);
            setListViewHeightBasedOnChildren(list_work);
            TextView day_zamer = (TextView) promptsView2.findViewById(R.id.day_zamer);
            day_zamer.setText(mount_day);
            date_zamera = mount_day;

            date();

            final AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
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

            final FunDapter adapter = new FunDapter(getActivity(), sel_work, R.layout.select_work_l, dict);
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

                        Toast toast = Toast.makeText(getActivity(),
                                "Замер выбран на " + time, Toast.LENGTH_SHORT);
                        toast.show();

                        tableLayout.removeAllViews();

                        @SuppressLint("ResourceType") int getid = btnn.getId() + 1;
                        cal_preview(getid);
                        btnn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                        alertDialog.dismiss();
                    } else {
                        Toast toast = Toast.makeText(getActivity(),
                                "Этот замерщик занят, выберите другого замерщика или другое время", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    };

    View.OnClickListener getEstimate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            int id_calc = estimate.get(id);

            Log.d("mLog", "getEst = " + id + " " + id_calc);
            Intent intent;
            intent = new Intent(getActivity(), ActivityEstimate.class);
            intent.putExtra("id_calculation", String.valueOf(id_calc));
            startActivity(intent);

        }
    };

    View.OnClickListener getDateMount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            final Button btnn = BtnList_mount_zamer.get(editId);
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

            LayoutInflater li = LayoutInflater.from(getActivity());
            promptsView2 = li.inflate(R.layout.layout_edit_mount, null);
            final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
            mDialogBuilder.setView(promptsView2)
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    tableLayout.removeAllViews();
                                    @SuppressLint("ResourceType") int getid = btnn.getId() + 1;
                                    cal_preview_mount(getid);
                                    btnn.setBackgroundResource(R.drawable.calendar_btn_yellow);

                                }
                            });

            list_work = (ListView) promptsView2.findViewById(R.id.list_work);
            setListViewHeightBasedOnChildren(list_work);
            Spinner sp_brigade = (Spinner) promptsView2.findViewById(R.id.sp_brigade);
            TextView data_mount = (TextView) promptsView2.findViewById(R.id.data_mount);
            data_mount.setText(mount_day);
            date_mount = mount_day;

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

            final AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
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

            final FunDapter adapter = new FunDapter(getActivity(), sel_work, R.layout.select_work_l, dict);
            list_work.setAdapter(adapter);

        }
    };

    void date() {

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
            name_zamer_id.add(dealer_id);
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

    void dog() {
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();

        ContentValues values;
        if (id_b == null) {
            values = new ContentValues();
            values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(notes_cl.getText()));
            values.put(DBHelper.KEY_GM_CALCULATOR_NOTE, String.valueOf(notes_gm_calc.getText()));
            values.put(DBHelper.KEY_GM_CHIEF_NOTE, String.valueOf(notes_gm_chief.getText()));
            values.put(DBHelper.KEY_PROJECT_VERDICT, "1");
            values.put(DBHelper.KEY_READ_BY_MOUNTER, "0");
            values.put(DBHelper.KEY_PROJECT_SUM, (Math.round(total) * 100.0) / 100);
            values.put(DBHelper.KEY_PROJECT_STATUS, "4");
            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});
        } else {
            values = new ContentValues();
            values.put(DBHelper.KEY_PROJECT_MOUNTING_DATE, date_mount + time_brig + ":00");
            values.put(DBHelper.KEY_PROJECT_MOUNTER, id_b);
            values.put(DBHelper.KEY_PROJECT_NOTE, String.valueOf(notes_cl.getText()));
            values.put(DBHelper.KEY_GM_CALCULATOR_NOTE, String.valueOf(notes_gm_calc.getText()));
            values.put(DBHelper.KEY_GM_CHIEF_NOTE, String.valueOf(notes_gm_chief.getText()));
            values.put(DBHelper.KEY_PROJECT_VERDICT, "1");
            values.put(DBHelper.KEY_READ_BY_MOUNTER, "0");
            values.put(DBHelper.KEY_PROJECT_SUM, (Math.round(total) * 100.0) / 100);
            values.put(DBHelper.KEY_PROJECT_STATUS, "5");
            db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_project});
        }

        values = new ContentValues();
        values.put(DBHelper.KEY_ID_OLD, id_project);
        values.put(DBHelper.KEY_ID_NEW, 0);
        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
        values.put(DBHelper.KEY_SYNC, "0");
        values.put(DBHelper.KEY_TYPE, "send");
        values.put(DBHelper.KEY_STATUS, "1");
        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

        int max_id_proj_history = 0;
        try {
            String sqlQuewy = "select MAX(_id) "
                    + "FROM rgzbn_gm_ceiling_projects_history " +
                    "where _id>? and _id<?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(Integer.parseInt(dealer_id) * 100000),
                    String.valueOf(Integer.parseInt(dealer_id) * 100000 + 99999)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        max_id_proj_history = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        max_id_proj_history++;
                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            max_id_proj_history = Integer.parseInt(dealer_id) * 100000 + 1;
        }

        Calendar date_cr = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(date_cr.getTime());

        values = new ContentValues();
        values.put(DBHelper.KEY_ID, max_id_proj_history);
        values.put(DBHelper.KEY_PROJECT_ID, id_project);
        values.put(DBHelper.KEY_NEW_STATUS, "5");
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

        Toast toast = Toast.makeText(getActivity(), "Проект направлен в запущенные", Toast.LENGTH_SHORT);
        toast.show();

        getActivity().startService(new Intent(getActivity(), Service_Sync.class));
        getActivity().finish();

        SharedPreferences SP = getActivity().getSharedPreferences("finishInform", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "1");
        ed.commit();

    }

    void alert() {

        cal_preview_mount(0);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view_general_inform.scrollTo(0, 4100);

            }
        }, 1);
    }

    void cal_preview_mount(int btn_id) {

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

        JodaTimeAndroid.init(getActivity());
        DateTime dt = new DateTime(year, month + 1, 1, 0, 0, 0, 0);
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

        int count = 0;
        name_brigade = new ArrayList<String>();
        id_brigade = new ArrayList<String>();

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
            SharedPreferences SPI = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
            String user_id = SPI.getString("", "");

            SharedPreferences SP_end = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
            String user_name = SP_end.getString("", "");

            name_brigade.add(user_name);
            id_brigade.add(user_id);
        }

        int ROWS = 6;
        int COLUMNS = 7;
        boolean flag = false;
        dbHelper = new DBHelper(getActivity());

        for (int i = 0; i < ROWS; i++) {

            count = 0;
            TableRow tableRow = new TableRow(getActivity());

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 4f);

            for (int j = 0; j < COLUMNS; j++) {
                if ((j == first_day_int || flag) && dday < max_day) {

                    for (int id_b = 0; id_brigade.size() > id_b; id_b++) {
                        Button btn = new Button(getActivity());
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

                        if (dday == btn_id && btn_id != 0) {
                            flag = true;
                            btn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                            btn.setTextColor(Color.BLACK);
                            count++;
                            BtnList.add(btn);
                            btn.setId(dday - 1);
                            btn.setText(String.valueOf(dday));
                            btn.setLayoutParams(tableParams);
                            btn.setOnClickListener(getDateMount);
                            tableRow.addView(btn, j);
                        } else if (today.equals(mount_day)) {
                            count++;
                            flag = true;
                            btn.setBackgroundResource(R.drawable.calendar_today);
                            btn.setTextColor(Color.BLACK);
                            BtnList_mount_zamer.add(btn);
                            btn.setId(dday - 1);
                            btn.setText(String.valueOf(dday));
                            btn.setLayoutParams(tableParams);
                            btn.setOnClickListener(getDateMount);
                            tableRow.addView(btn, j);
                        } else {
                            sqlQuewy = "select _id, read_by_mounter "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? and (read_by_mounter=? or read_by_mounter=?)";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_brigade.get(id_b), mount_day + " 08:00:00", mount_day + " 22:00:00", "0", "null"});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    do {
                                        btn.setBackgroundResource(R.drawable.calendar_btn_blue);
                                        btn.setTextColor(Color.BLACK);
                                    } while (cc.moveToNext());
                                } else {
                                    btn.setBackgroundResource(R.drawable.calendar_btn);
                                    btn.setTextColor(Color.BLACK);
                                }
                            }
                            cc.close();

                            count++;
                            flag = true;
                            BtnList_mount_zamer.add(btn);
                            btn.setId(dday - 1);
                            btn.setText(String.valueOf(dday));
                            btn.setLayoutParams(tableParams);
                            btn.setOnClickListener(getDateMount);
                            tableRow.addView(btn, j);
                        }
                    }
                } else {
                    Button btn = new Button(getActivity());
                    btn.setText("");
                    btn.setBackgroundResource(R.drawable.calendar_other_month);
                    btn.setLayoutParams(tableParams);
                    tableRow.addView(btn, j);
                }


            }
            tableLayout.addView(tableRow, i);
        }
    }

    public static void setListViewHeightBasedOnChildren(final ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewTreeObserver vto = listView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = listView.getMeasuredHeight();
                int width = listView.getMeasuredWidth();

            }
        });


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @SuppressLint("ResourceType")
    void btn(String title) {

        mainL = (LinearLayout) view.findViewById(R.id.phone_lay1);
        titleViewParams = new LinearLayout.LayoutParams(80, 80, 1);
        titleViewParams.setMargins(0, 0, 0, 20);

        int txt_i = bt_i;
        btn = new Button(getActivity());
        BtnList.add(bt_i, btn);
        btn.setId(bt_i++);
        btn.setLayoutParams(titleViewParams);
        btn.setBackgroundResource(R.drawable.white_btn_blue_text);
        btn.setTextSize(1);
        btn.setText(title);
        btn.setTextColor(Color.argb(0, 0, 0, 0));
        //btn.setOnLongClickListener(longGetPhone);
        btn.setOnClickListener(getPhone);
        btn.setBackgroundResource(R.drawable.edit);
        mainL.addView(btn);

        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.weight = 1;
        lin_calc.setMargins(0, 2, 0, 20);

        TextView txt = new TextView(getActivity());
        txt.setLayoutParams(lin_calc);
        txt.setTextSize(14);
        txt.setText(title);
        txt.setId(txt_i);
        //txt.setOnLongClickListener(longGetPhone);
        txt.setOnClickListener(getPhone);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setTextColor(Color.parseColor("#414099"));
        mainL2.addView(txt);

    }

    void radiob(String calc_title, String n4, String n5, String id, String imag) {

        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.weight = 1;
        lin_calc.setMargins(0, 2, 0, 0);

        CheckBox rb = new CheckBox(getActivity());
        CheckBoxList.add(ch_i, rb);
        rb.setId(Integer.parseInt(id));
        rb.setText(calc_title);
        rb.setLayoutParams(lin_calc);
        rb.setChecked(true);
        rb.setTextColor(Color.parseColor("#414099"));
        rb.setTypeface(null, Typeface.BOLD);
        mainC.addView(rb);

        if (n4.equals("") || n4.equals("0.0")) {
        } else {
            TextView tx = new TextView(getActivity());
            tx.setText("S/P = " + n4 + " м2 / " + n5 + " м");
            tx.setLayoutParams(lin_calc);
            tx.setTextColor(Color.parseColor("#414099"));
            mainC.addView(tx);
        }

        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        String sqlQuewy = "SELECT n4, n5, components_sum, canvases_sum, mounting_sum, discount "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{id});
        double tmp = 0;     // компоненты
        double tmp2 = 0;    // канвас
        double tmp3 = 0;    // монтаж
        double dis = 0.0;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    S = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    P = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    tmp += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    tmp2 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    tmp3 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));

                    if (c.getString(c.getColumnIndex(c.getColumnName(5))).equals("")) {
                        dis = 0.0;
                    } else {
                        dis = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(5))));
                    }
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

        if (dis > 0.0) {
            String tot1 = "Итого/ " + dis + "%";
            String tot2 = Math.round(totall) * 100.0 / 100 + "/ " + String.valueOf((Math.round(totall - (totall / 100 * dis)) * 100.0) / 100);
            tx1.setText(tot1);

            tx1.setLayoutParams(lin_calc);
            tx1.setTextColor(Color.parseColor("#414099"));
            mainC.addView(tx1);

            TextView tx2 = new TextView(getActivity());
            tx2.setText(tot2);
            tx2.setLayoutParams(lin_calc);
            tx2.setTextColor(Color.parseColor("#414099"));
            mainC.addView(tx2);

        } else {
            tx1.setText("Итого = " + Math.round(totall) * 100.0 / 100);

            tx1.setLayoutParams(lin_calc);
            tx1.setTextColor(Color.parseColor("#414099"));
            mainC.addView(tx1);
        }

        LinearLayout.LayoutParams lin_btn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        lin_btn.weight = 1;
        lin_btn.setMargins(0, 2, 0, 25);

        Button btnEstimate = new Button(getActivity());
        btnEstimate.setText("Смета");
        btnEstimate.setBackgroundColor(Color.parseColor("#f8f8ff"));
        btnEstimate.setLayoutParams(lin_btn);
        btnEstimate.setId(ch_i);
        btnEstimate.setOnClickListener(getEstimate);
        btnEstimate.setBackgroundResource(R.drawable.rounded_button);
        btnEstimate.setTextColor(Color.parseColor("#ffffff"));
        mainC.addView(btnEstimate);
        estimate.add(Integer.valueOf(id));

        ch_i++;

        LinearLayout.LayoutParams ViewParams = new LinearLayout.LayoutParams(200, 200, 1);

        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(ViewParams);

        if (imag.length() > 10) {
            try {
                Sharp.loadString(imag)
                        .into(image);
                mainC2.addView(image);
            } catch (Exception e) {
            }
        } else {
            View view = new View(getActivity());
            view.setLayoutParams(ViewParams);
            mainC2.addView(view);

        }

    }
}