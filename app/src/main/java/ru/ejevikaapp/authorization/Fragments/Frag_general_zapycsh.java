package ru.ejevikaapp.authorization.Fragments;

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
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

import static android.content.Context.MODE_PRIVATE;

public class Frag_general_zapycsh extends Fragment implements View.OnClickListener {

    TextView id_proj, dealer_cl, components_sum_total, mounting_sum, total_sum, final_amount, final_amount_disc, final_transport, final_transport_sum, data_mounting;
    Calendar dateAndTime = Calendar.getInstance();
    TextView name_cl, contact_cl, address_cl, notes_cl, ed_discount, edit_transport_0, edit_transport_1, edit_transport_21, edit_transport_22;

    String id_cl, id_project, phone, fio, pro_info, mount_date="", dealer_id, item, S, P, transport = "", distance_col = "", distance = "", calc_date= "";

    String time_h ="";

    Button btn_transport_ok;

    DBHelper dbHelper;
    View view;

    TextView DateTime, S_and_P;

    String[] time_zam = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00",
            "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"};

    int discount = 0, count_calc = 0, bt_i = 0, ch_i = 0;

    ArrayList id_calcul = new ArrayList();

    Spinner sp_date;

    double total = 0, sum_transport = 0.0;

    boolean flag;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date_zam = df.format(dateAndTime.getTime());
    LinearLayout.LayoutParams titleViewParams, titleViewParams2;

    Button btn;
    private List<Button> BtnList = new ArrayList<Button>();
    private List<TextView> CheckBoxList = new ArrayList<TextView>();
    int i = 0;

    RadioButton rb_transport_0, rb_transport_1, rb_transport_2;

    LinearLayout.LayoutParams lin_calc;
    LinearLayout mainL, mainC, mainL2;

    public Frag_general_zapycsh() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_general_zapycsh, container, false);


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
        data_mounting = (TextView) view.findViewById(R.id.data_mounting);

        btn_transport_ok = (Button) view.findViewById(R.id.btn_transport_ok);
        btn_transport_ok.setOnClickListener(this);

        name_cl = (TextView) view.findViewById(R.id.name_cl);
        contact_cl = (TextView) view.findViewById(R.id.contact_cl);
        address_cl = (TextView) view.findViewById(R.id.address_cl);
        ed_discount = (TextView) view.findViewById(R.id.ed_discount);
        notes_cl = (TextView) view.findViewById(R.id.notes_cl);

        edit_transport_1 = (EditText) view.findViewById(R.id.edit_transport_1);
        edit_transport_1.setVisibility(View.INVISIBLE);

        edit_transport_21 = (EditText) view.findViewById(R.id.edit_transport_21);
        edit_transport_21.setVisibility(View.INVISIBLE);

        edit_transport_22 = (EditText) view.findViewById(R.id.edit_transport_22);
        edit_transport_22.setVisibility(View.INVISIBLE);

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

        rb_transport_0 = (RadioButton) view.findViewById(R.id.rb_transport_0);
        rb_transport_1 = (RadioButton) view.findViewById(R.id.rb_transport_1);
        rb_transport_2 = (RadioButton) view.findViewById(R.id.rb_transport_2);

        if (distance.equals("0")){
            distance = "1";
        }
        if (distance_col.equals("0")){
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
        dealer_cl = (TextView) view.findViewById(R.id.dealer_cl);
        S_and_P = (TextView) view.findViewById(R.id.S_and_P);
        components_sum_total = (TextView) view.findViewById(R.id.components_sum_total);
        mounting_sum = (TextView) view.findViewById(R.id.mounting_sum);
        total_sum = (TextView) view.findViewById(R.id.total_sum);
        final_amount = (TextView) view.findViewById(R.id.final_amount);
        final_amount_disc = (TextView) view.findViewById(R.id.final_amount_disc);

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

        discount = 0;
        sqlQuewy = "SELECT project_info, project_mounting_date, project_note, project_calculation_date "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_project});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    pro_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    SimpleDateFormat out_format = null;
                    SimpleDateFormat out_format_time = null;
                    SimpleDateFormat out_format_minute = null;
                    Date change_max = null;
                    Date minute = null;

                    int hours = 0;

                    try {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        change_max = ft.parse(pro_info);

                        out_format = new SimpleDateFormat("dd.MM.yyyy");
                        out_format_minute = new SimpleDateFormat("HH");

                        hours = Integer.parseInt(out_format_minute.format(change_max))+1;

                        out_format_time = new SimpleDateFormat("HH:mm");

                        address_cl.setText(String.valueOf(out_format.format(change_max) + "\n" + out_format_time.format(change_max))
                                + " - " + hours + ":00");
                    }catch (Exception e){
                    }

                    mount_date = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    out_format = null;
                    out_format_time = null;
                    out_format_minute = null;
                    change_max = null;
                    minute = null;

                    hours = 0;

                    try {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        change_max = ft.parse(mount_date);

                        out_format = new SimpleDateFormat("dd.MM.yyyy");
                        out_format_minute = new SimpleDateFormat("HH");

                        hours = Integer.parseInt(out_format_minute.format(change_max))+1;

                        out_format_time = new SimpleDateFormat("HH:mm");

                        data_mounting.setText(String.valueOf(out_format.format(change_max) + "\n" + out_format_time.format(change_max))
                                + " - " + hours + ":00");
                    }catch (Exception e){
                    }

                    calc_date = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    DateTime.setText(calc_date);

                } while (c.moveToNext());
            }
        }
        c.close();

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

        return view;
    }

    void calc(ArrayList id_calcul) {

        for (int i = 0; i < id_calcul.size(); i++) {
            Log.d("mLog", String.valueOf(id_calcul.get(i)));
        }

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double s = 0.0;
        double p = 0.0;
        double tmp = 0;     // компоненты
        double tmp2 = 0;    // канвас
        double tmp3 = 0;    // монтаж

        double tmp_d = 0;     // компоненты
        double tmp2_d = 0;    // канвас
        double tmp3_d = 0;    // монтаж
        double total_d = 0;

        double dis = 0;

        String sqlQuewy;
        Cursor c;

        int min_sum =0;

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

                        if (c.getString(c.getColumnIndex(c.getColumnName(5))).equals("null")){
                            dis = 0.0;
                        } else {
                            dis = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(5))));
                        }

                        tmp_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                        Double avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                        avg = avg * ((100 - dis)/100);
                        tmp += avg;

                        tmp2_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                        avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                        avg = avg * ((100 - dis)/100);
                        tmp2 += avg;

                        tmp3_d += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                        avg = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                        avg = avg * ((100 - dis)/100);
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
            sum_transport=0;
            Log.d("mLog", transport);
        } else if (transport.equals("1")) {
            Log.d("mLog", transport);
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

            sum_transport = dist*dist_col*22;

            if (sum_transport < 624) {
                sum_transport = 625;
            }
        }

        final_transport_sum.setText(String.valueOf(sum_transport));

        discount =0;
        for (int i = 0; id_calcul.size() > i; i++) {
            sqlQuewy = "SELECT discount "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE project_id = ? and _id=?";
            c = db.rawQuery(sqlQuewy, new String[]{id_project, String.valueOf(id_calcul.get(i))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        if (c.getString(c.getColumnIndex(c.getColumnName(0))).equals("null")){
                            discount=0;
                        } else {
                            discount += Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        }
                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        total += sum_transport;
        total_d += sum_transport;

        Log.d("mLog", "discount " + discount);

        if (discount > 0) {
            final_amount_disc.setText("Итого/ \n"  + "  - %");
            final_amount.setText(String.valueOf((Math.round(total_d) * 100.0) / 100) + "/ \n" + String.valueOf((Math.round(total) * 100.0) / 100));
        } else {
            final_amount.setText(String.valueOf(total_d));
        }

        if (total_d == 0){
            final_amount.setText(String.valueOf(total_d));
        } else if (total_d < min_sum && count_calc>0) {
            total_d = min_sum;
            final_amount.setText(String.valueOf(total_d) + " * минимальная сумма заказа "+min_sum+" р.");
        }

        components_sum_total.setText(String.valueOf((Math.round(tmp + tmp2) * 100.0) / 100));
        mounting_sum.setText(String.valueOf((Math.round(tmp3) * 100.0) / 100));
        total_sum.setText(String.valueOf((Math.round(tmp + tmp2 + tmp3) * 100.0) / 100));

        S_and_P.setText(Math.round(s * 100.0) / 100.0 + " м2 / \n" + Math.round(p * 100.0) / 100.0 + " м");
    }

    void id_calc(){

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        count_calc =0;
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

        calc(id_calcul);
    }

    private void setInitialDateTime() {
        DateTime.setText(DateUtils.formatDateTime(this.getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        // date = DateUtils.formatDateTime(getActivity(),                  //дата замера
        //         dateAndTime.getTimeInMillis(),
        //         DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        flag = true;
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    View.OnClickListener getPhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();

            Button btnn = BtnList.get(editId);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + btnn.getText().toString()));
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

                                            Log.d("mLog", String.valueOf(id_phone));
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

    void btn(String title) {
        btn = new Button(getActivity());
        BtnList.add(bt_i, btn);
        btn.setId(bt_i++);
        btn.setLayoutParams(titleViewParams);
        //btn.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
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
        TextView rb = new TextView(getActivity());
        CheckBoxList.add(ch_i, rb);
        rb.setId(Integer.parseInt(id));
        rb.setText(calc_title);
        rb.setLayoutParams(lin_calc);
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
                    Log.d("mLog1", String.valueOf(tmp));
                    Log.d("mLog2", String.valueOf(tmp2));
                    Log.d("mLog3", String.valueOf(tmp3));
                } while (c.moveToNext());
            }
        }
        c.close();

        double totall = 0.0;
        Log.d("mLog", "dealer_id 2 " + dealer_id);
        sqlQuewy = "SELECT dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, " +
                "gm_canvases_margin, gm_components_margin, gm_mounting_margin "
                + "FROM rgzbn_gm_ceiling_dealer_info" +
                " WHERE dealer_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(dealer_id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    Log.d("mLog", String.valueOf(totall));

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

        try {
            if (Integer.valueOf(dis) == null) {
                dis = "0";
            }
        }catch (Exception e){
            dis="0";
        }

        if (Integer.valueOf(dis)>0){

            String tot1 = "Итого/ \n" + dis + "%";
            String tot2 = Math.round(totall) * 100.0 / 100 + "/ \n" + String.valueOf((Math.round(totall - (totall / 100 * Integer.valueOf(dis))) * 100.0) / 100);
            tx1.setText(tot1 + "     \n" + tot2);
        }else {
            tx1.setText("Итого     " + totall);
        }

        tx1.setLayoutParams(lin_calc);
        tx1.setTextColor(Color.parseColor("#414099"));
        mainC.addView(tx1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_transport_ok:
                try {
                    Log.d("mLog", transport);
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
        }
    }

}