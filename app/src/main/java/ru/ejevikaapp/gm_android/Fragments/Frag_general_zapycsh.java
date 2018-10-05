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
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.gms.vision.text.Line;
import com.pixplicity.sharp.Sharp;

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

import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
import ru.ejevikaapp.gm_android.Class.ForAdapterClass;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Class.NonScrollListView;
import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.Activity_for_spisok;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;

import static android.content.Context.MODE_PRIVATE;

public class Frag_general_zapycsh extends Fragment implements View.OnClickListener {

    TextView id_proj, dealer_cl, components_sum_total, mounting_sum, total_sum, final_amount, final_amount_disc, final_transport, final_transport_sum, data_mounting;
    Calendar dateAndTime = Calendar.getInstance();
    TextView name_cl, contact_cl, address_cl, notes_cl, ed_discount, data_cl, project_calculator, project_mounter, advertisement;

    String id_cl, id_project, phone, fio, pro_info, mount_date = "", dealer_id, item, S, P, transport = "", distance_col = "", distance = "", calc_date = "";

    DBHelper dbHelper;
    View view;

    AutoCompleteTextView addressEdit;
    List<String> addressList = new ArrayList<String>();

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

    NonScrollListView listMounts;

    Button btn, completedProject;
    private List<Button> BtnList = new ArrayList<Button>();
    private List<TextView> CheckBoxList = new ArrayList<TextView>();
    int i = 0;

    LinearLayout.LayoutParams lin_calc;
    LinearLayout mainL, mainC, mainC2, mainL2;

    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    int day_week, year, day, dday, month, max_day;
    String id_z, id_b, time_brig, mount_day;
    ListView list_work;
    View promptsView2;
    TableLayout tableLayout, tableLayoutSelectMount;
    final ArrayList<ForAdapterClass> mount_mas = new ArrayList<>();

    TextView calendar_month;

    private List<Button> BtnList_mount_zamer = new ArrayList<Button>();
    private List<Button> BtnList_mount_brigade = new ArrayList<Button>();
    ArrayList<Select_work> sel_work = new ArrayList<>();
    ArrayList<String> name_zamer_id = new ArrayList<String>();
    ArrayList<String> name_brigade = new ArrayList<String>();
    ArrayList<String> id_brigade = new ArrayList<String>();
    ArrayList<String> time_free = new ArrayList<String>();
    HashMap<String, String> checkTypeMount = new HashMap<>();
    AlertDialog alertDialogMountsTime;

    String date_zamera = df.format(dateAndTime.getTime());
    String date_mount = df.format(dateAndTime.getTime());

    String TAG = "mLog";

    public Frag_general_zapycsh() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_general_zapycsh, container, false);

        mainC = (LinearLayout) view.findViewById(R.id.linear_calc);
        lin_calc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lin_calc.setMargins(0, 2, 0, 0);

        mainL = (LinearLayout) view.findViewById(R.id.phone_lay1);
        titleViewParams = new LinearLayout.LayoutParams(80, 80, 1);
        titleViewParams.setMargins(0, 0, 0, 20);

        mainL2 = (LinearLayout) view.findViewById(R.id.phone_lay2);
        titleViewParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        titleViewParams2.setMargins(0, 0, 0, 20);

        mainC2 = (LinearLayout) view.findViewById(R.id.linear_calc2);

        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
        id_cl = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
        id_project = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SPI.getString("", "");

        list_work = (NonScrollListView) view.findViewById(R.id.list_work);

        id_proj = (TextView) view.findViewById(R.id.id_proj);
        id_proj.setText("Проект №" + id_project);

        final_transport = (TextView) view.findViewById(R.id.final_transport);
        final_transport_sum = (TextView) view.findViewById(R.id.final_transport_sum);
        //data_mounting = (TextView) view.findViewById(R.id.data_mounting);
        //project_mounter = (TextView) view.findViewById(R.id.project_mounter);
        project_calculator = (TextView) view.findViewById(R.id.project_calculator);
        advertisement = (TextView) view.findViewById(R.id.advertisement);

        name_cl = (TextView) view.findViewById(R.id.name_cl);
        name_cl.setOnClickListener(this);
        contact_cl = (TextView) view.findViewById(R.id.contact_cl);
        address_cl = (TextView) view.findViewById(R.id.address_cl);
        address_cl.setOnClickListener(this);
        ed_discount = (TextView) view.findViewById(R.id.ed_discount);
        notes_cl = (TextView) view.findViewById(R.id.notes_cl);

        completedProject = (Button) view.findViewById(R.id.completedProject);
        completedProject.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select transport, distance, distance_col, api_phone_id "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where _id=?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    transport = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    distance = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    distance_col = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    String advt = c.getString(c.getColumnIndex(c.getColumnName(3)));

                    if (advt == null || advt.equals("null") || advt.equals("")) {
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
        sqlQuewy = "SELECT project_info, project_calculation_date, project_calculator "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_project});

        if (c != null) {
            if (c.moveToFirst()) {
                pro_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
                address_cl.setText(pro_info);

                SimpleDateFormat out_format = null;
                SimpleDateFormat out_format_time = null;
                SimpleDateFormat out_format_minute = null;
                Date change_max = null;
                Date minute = null;

                int hours = 0;

                mount_date = c.getString(c.getColumnIndex(c.getColumnName(1)));

                if (mount_date.equals("0000-00-00 00:00:00")) {

                } else {
                    try {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        change_max = ft.parse(mount_date);

                        out_format = new SimpleDateFormat("dd.MM.yyyy");
                        out_format_minute = new SimpleDateFormat("HH");

                        hours = Integer.parseInt(out_format_minute.format(change_max)) + 1;

                        out_format_time = new SimpleDateFormat("HH:mm");

                        DateTime.setText(String.valueOf(out_format.format(change_max) + " " + out_format_time.format(change_max))
                                + " - " + hours + ":00");
                    } catch (Exception e) {
                    }
                }

                String calculator_id = c.getString(c.getColumnIndex(c.getColumnName(2)));

                sqlQuewy = "SELECT name "
                        + "FROM rgzbn_users" +
                        " WHERE _id = ?";

                Cursor c2 = db.rawQuery(sqlQuewy, new String[]{calculator_id});

                if (c2 != null) {
                    if (c2.moveToFirst()) {
                        fio = c2.getString(c2.getColumnIndex(c2.getColumnName(0)));
                        project_calculator.setText(fio);
                    }
                }
                c2.close();

            }
        }
        c.close();

        sqlQuewy = "SELECT date_time, mounter_id "
                + "FROM rgzbn_gm_ceiling_projects_mounts " +
                " WHERE project_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {

                SimpleDateFormat out_format = null;
                SimpleDateFormat out_format_time = null;
                SimpleDateFormat out_format_minute = null;
                Date change_max = null;
                Date minute = null;

                int hours = 0;

                mount_date = c.getString(c.getColumnIndex(c.getColumnName(0)));

                if (mount_date.equals("0000-00-00 00:00:00")) {

                } else {
                    try {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        change_max = ft.parse(mount_date);
                        out_format = new SimpleDateFormat("dd.MM.yyyy");
                        out_format_minute = new SimpleDateFormat("HH");
                        hours = Integer.parseInt(out_format_minute.format(change_max)) + 1;
                        out_format_time = new SimpleDateFormat("HH:mm");
                        //data_mounting.setText(String.valueOf(out_format.format(change_max) + " " + out_format_time.format(change_max))
                        // + " - " + hours + ":00");
                    } catch (Exception e) {
                    }
                }

                String mount_id = c.getString(c.getColumnIndex(c.getColumnName(1)));

                sqlQuewy = "SELECT name "
                        + "FROM rgzbn_users" +
                        " WHERE _id = ?";
                Cursor c2 = db.rawQuery(sqlQuewy, new String[]{mount_id});
                if (c2 != null) {
                    if (c2.moveToFirst()) {
                        fio = c2.getString(c2.getColumnIndex(c2.getColumnName(0)));
                        //project_mounter.setText(fio);
                    }
                }
                c2.close();

            }
        }
        c.close();

        id_calcul.clear();
        i = 0;

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
                                addressEdit.setText(str);

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

        listMounts = (NonScrollListView) view.findViewById(R.id.listMounts);

        client_mas.clear();
        String mounter_name = "";
        String date_time = "";
        String type = "";

        sqlQuewy = "SELECT mounter_id, date_time, type "
                + "FROM rgzbn_gm_ceiling_projects_mounts " +
                "where project_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_project});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String mounter_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    date_time = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    String type_id = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    sqlQuewy = "SELECT name "
                            + "FROM rgzbn_users " +
                            "where _id = ?";
                    Cursor c_2 = db.rawQuery(sqlQuewy, new String[]{mounter_id});
                    if (c_2 != null) {
                        if (c_2.moveToFirst()) {
                            do {
                                mounter_name = c_2.getString(c_2.getColumnIndex(c_2.getColumnName(0)));
                            } while (c_2.moveToNext());
                        }
                    }
                    c_2.close();

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_mounts_types " +
                            "where _id = ?";
                    c_2 = db.rawQuery(sqlQuewy, new String[]{type_id});
                    if (c_2 != null) {
                        if (c_2.moveToFirst()) {
                            do {
                                type = c_2.getString(c_2.getColumnIndex(c_2.getColumnName(0)));
                            } while (c_2.moveToNext());
                        }
                    }
                    c_2.close();

                    Frag_client_schedule_class fc = new Frag_client_schedule_class(date_time, type,
                            mounter_name, null, null, null);
                    client_mas.add(fc);

                } while (c.moveToNext());
            }
        }
        c.close();

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.firstColumn, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId();
            }
        });
        dict.addStringField(R.id.secondColumn, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.thirdColumn, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.list3columns, dict);
        listMounts.setAdapter(adapter);

        id_calc();

        transport();
        calc(id_calcul);


        Calendar cl = Calendar.getInstance();
        day_week = cl.get(Calendar.DAY_OF_WEEK);
        year = cl.get(Calendar.YEAR);
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH);
        calendar_month = (TextView) view.findViewById(R.id.calendar_month);
        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);

        ImageButton calendar_minus = (ImageButton) view.findViewById(R.id.calendar_minus);
        calendar_minus.setOnClickListener(this);
        ImageButton calendar_plus = (ImageButton) view.findViewById(R.id.calendar_plus);
        calendar_plus.setOnClickListener(this);

        calendar_month = (TextView) view.findViewById(R.id.calendar_month);
        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        cal_preview_mount(0);

        return view;
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
                        if (S == null || S.equals("")) {
                            S = "0";
                        }

                        P = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        if (P == null || P.equals("")) {
                            P = "0";
                        }

                        s += Double.parseDouble(S);
                        p += Double.parseDouble(P);

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

        total += sum_transport;
        total_d += sum_transport;

        if (discount > 0) {
            final_amount_disc.setText("Итого/ \n" + "  - %");
            final_amount.setText(String.valueOf((Math.round(total_d) * 100.0) / 100) + "/ \n" + String.valueOf((Math.round(total) * 100.0) / 100));
        } else {
            final_amount.setText(String.valueOf(total_d));
        }

        if (total_d == 0) {
            final_amount.setText(String.valueOf(total_d));
        } else if (total_d < min_sum && count_calc > 0) {
            total_d = min_sum;
            final_amount.setText(String.valueOf(total_d) + " * минимальная сумма заказа " + min_sum + " р.");
        }

        components_sum_total.setText(String.valueOf((Math.round(tmp_d + tmp2_d) * 100.0) / 100));
        mounting_sum.setText(String.valueOf((Math.round(tmp3_d) * 100.0) / 100));
        total_sum.setText(String.valueOf((Math.round(tmp_d + tmp2_d + tmp3_d) * 100.0) / 100 + transport));

        S_and_P.setText(Math.round(s * 100.0) / 100.0 + " м2 / \n" + Math.round(p * 100.0) / 100.0 + " м");
    }

    void id_calc() {

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        count_calc = 0;
        mainC.removeAllViews();
        id_calcul.clear();
        mainC2.removeAllViews();

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
                    if (S == null || S.equals("")) {
                        S = "0";
                    }

                    P = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    if (P == null || P.equals("")) {
                        P = "0";
                    }

                    s += Double.parseDouble(S);
                    p += Double.parseDouble(P);

                    if (c.getString(c.getColumnIndex(c.getColumnName(2))) == null) {
                    } else {
                        tmp += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    }

                    if (c.getString(c.getColumnIndex(c.getColumnName(3))) == null) {
                    } else {
                        tmp2 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    }

                    if (c.getString(c.getColumnIndex(c.getColumnName(4))) == null) {
                    } else {
                        tmp3 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    }

                    if (c.getString(c.getColumnIndex(c.getColumnName(5))) == null) {
                    } else {
                        dis = c.getString(c.getColumnIndex(c.getColumnName(5)));
                    }

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
        } catch (Exception e) {
            dis = "0";
        }

        if (Integer.valueOf(dis) > 0) {

            String tot1 = "Итого/ " + dis + "%";
            String tot2 = Math.round(totall) * 100.0 / 100 + "/ " + String.valueOf((Math.round(totall - (totall / 100 * Integer.valueOf(dis))) * 100.0) / 100);
            //tx1.setText(tot1 + "     \n" + tot2);
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


        LinearLayout.LayoutParams ViewParams = new LinearLayout.LayoutParams(200, 200, 1);

        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(ViewParams);

        try {
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
        } catch (Exception e) {
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.address_cl:
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
                addressEdit.setText(str);

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

            case R.id.name_cl:

                final Context context = getActivity();
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

                                        getActivity().startService(new Intent(getActivity(), Service_Sync.class));

                                        Intent intent = new Intent(getActivity(), Activity_inform_zapysch.class);
                                        startActivity(intent);
                                        getActivity().finish();

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

            case R.id.completedProject:

                DBHelper dbHelper = new DBHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_PROJECT_STATUS, "12");
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
                values.put(DBHelper.KEY_NEW_STATUS, "12");
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

                getActivity().finish();
                getActivity().startService(new Intent(getActivity(), Service_Sync.class));

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
        }
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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        sel_work.clear();

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

                    //for (int id_b = 0; id_brigade.size() > id_b; id_b++) {
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
                        String sqlQuewy = "select * "
                                + "FROM rgzbn_gm_ceiling_projects_mounts " +
                                "where  date_time > ? and date_time < ? ";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{mount_day + " 08:00:00", mount_day + " 22:00:00"});
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

    View.OnClickListener getDateMount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            final Button btnn = BtnList_mount_zamer.get(editId);
            int day = Integer.parseInt(btnn.getText().toString());
            name_brigade = new ArrayList<String>();
            id_brigade = new ArrayList<String>();
            int count = 0;
            int count_time = 0;
            final ArrayList<ForAdapterClass> arrayList = new ArrayList<>();

            for (int i = 0; i < mount_mas.size(); i++) {
                arrayList.add(mount_mas.get(i));
            }

            mount_mas.clear();

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            if (day < 10 && month < 9) {
                mount_day = year + "-0" + (month + 1) + "-0" + day;
            } else if (day < 10 && month > 8) {
                mount_day = year + "-" + (month + 1) + "-0" + day;
            } else if (day > 9 && month < 9) {
                mount_day = year + "-0" + (month + 1) + "-" + day;
            } else {
                mount_day = year + "-" + (month + 1) + "-" + day;
            }


            LayoutInflater li = LayoutInflater.from(getActivity());
            promptsView2 = li.inflate(R.layout.layout_select_mount, null);
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

            alertDialogMountsTime = mDialogBuilder.create();
            alertDialogMountsTime.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
            alertDialogMountsTime.show();

            tableLayoutSelectMount = (TableLayout) promptsView2.findViewById(R.id.tableLayoutSelectMount);

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
                                String group_id = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                if (group_id.equals("11")) {
                                    count++;
                                    name_brigade.add(name);
                                    id_brigade.add(id);
                                }
                            }
                        }
                        cc.close();
                    } while (c.moveToNext());
                }
            }
            c.close();

            for (int i = 0; i < 13; i++) {
                TableRow tableRow = new TableRow(getActivity());
                TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 4f);

                for (int j = 0; j < count; j++) {
                    if (i == 0) {
                        Button btn = new Button(getActivity());
                        btn.setBackgroundResource(R.drawable.calendar_btn);
                        btn.setTextColor(Color.BLACK);
                        btn.setText(name_brigade.get(j));
                        btn.setLayoutParams(tableParams);
                        tableRow.addView(btn, j);
                    } else {
                        if (arrayList.size() > 0 && arrayList.get(count_time).getTdType() != null) {
                            Log.d(TAG, "onClick: " + i + 8 + ":00");
                            Button btn = new Button(getActivity());
                            btn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                            btn.setTextColor(Color.BLACK);
                            btn.setId(count_time);
                            BtnList_mount_brigade.add(btn);
                            btn.setText(i + 8 + ":00");
                            btn.setLayoutParams(tableParams);
                            btn.setOnClickListener(getDateMountBrigade);
                            tableRow.addView(btn, j);

                            ForAdapterClass fix_class = new ForAdapterClass(String.valueOf(count_time),
                                    null,
                                    null,
                                    mount_day + " " + (i + 8) + ":00",
                                    id_brigade.get(j),
                                    arrayList.get(count_time).getTdType());
                            mount_mas.add(fix_class);
                        } else {
                            Button btn = new Button(getActivity());
                            btn.setBackgroundResource(R.drawable.calendar_btn);
                            btn.setTextColor(Color.BLACK);
                            btn.setId(count_time);
                            BtnList_mount_brigade.add(btn);
                            btn.setText(i + 8 + ":00");
                            btn.setLayoutParams(tableParams);
                            btn.setOnClickListener(getDateMountBrigade);
                            tableRow.addView(btn, j);

                            ForAdapterClass fix_class = new ForAdapterClass(String.valueOf(count_time),
                                    null,
                                    null,
                                    mount_day + " " + (i + 8) + ":00",
                                    id_brigade.get(j),
                                    null);
                            mount_mas.add(fix_class);
                        }

                        count_time++;
                    }
                }
                tableLayoutSelectMount.addView(tableRow, i);
            }
        }
    };

    View.OnClickListener getDateMountBrigade = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int editId = v.getId();
            final Button btnn = BtnList_mount_brigade.get(editId);
            final int btnId = btnn.getId();
            final String tmp1 = mount_mas.get(btnId).getId();
            final String tmp2 = mount_mas.get(btnId).getFtType();
            final String tmp3 = mount_mas.get(btnId).getSdType();
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
                                    tableLayoutSelectMount.removeAllViews();

                                    getDateMount.onClick(BtnList_mount_zamer.get(dday - 1));

                                }
                            });

            final AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
            alertDialog.show();

            LinearLayout formMounter = (LinearLayout) promptsView2.findViewById(R.id.formMounter);
            formMounter.setVisibility(View.GONE);

            final CheckBox checkBoxType2 = (CheckBox) promptsView2.findViewById(R.id.checkBoxType2);
            final CheckBox checkBoxType3 = (CheckBox) promptsView2.findViewById(R.id.checkBoxType3);
            final CheckBox checkBoxType4 = (CheckBox) promptsView2.findViewById(R.id.checkBoxType4);

            final LinearLayout linearCheckBox = (LinearLayout) promptsView2.findViewById(R.id.linearCheckBox);

            RadioGroup radios_b = (RadioGroup) promptsView2.findViewById(R.id.radios_b);

            RadioButton radioButton_mount = (RadioButton)view.findViewById(R.id.radioButton_mount);
            RadioButton radioButton_mount2 = (RadioButton)view.findViewById(R.id.radioButton_mount2);

            for (int i = 0; i < mount_mas.size(); i++) {
                if (mount_mas.get(i).getTdType() != null) {
                    switch (mount_mas.get(i).getTdType()) {
                        case "1":
                            radios_b.check(R.id.radioButton_mount);
                            Log.d(TAG, "RADIOBTN: 1"  );
                            //radioButton_mount2.setEnabled(false);
                            break;
                        case "2":
                            radios_b.check(R.id.radioButton_mount2);
                            radioButton_mount.setEnabled(false);
                            checkBoxType2.setChecked(true);
                            break;
                        case "3":
                            radios_b.check(R.id.radioButton_mount2);
                            radioButton_mount.setEnabled(false);
                            checkBoxType3.setChecked(true);
                            break;
                        case "4":
                            radios_b.check(R.id.radioButton_mount2);
                            radioButton_mount.setEnabled(false);
                            checkBoxType4.setChecked(true);
                            break;
                    }
                }
            }

            radios_b.clearCheck();
            radios_b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    switch (id) {
                        case R.id.radioButton_mount:
                            linearCheckBox.setVisibility(View.GONE);
                            checkTypeMount.clear();
                            checkBoxType2.setChecked(false);
                            checkBoxType3.setChecked(false);
                            checkBoxType4.setChecked(false);

                            ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                    null,
                                    null,
                                    tmp2,
                                    tmp3,
                                    "1");
                            mount_mas.set(btnId, fix_class);
                            break;
                        case R.id.radioButton_mount2:
                            linearCheckBox.setVisibility(View.VISIBLE);
                            checkTypeMount.clear();
                            fix_class = new ForAdapterClass(tmp1,
                                    null,
                                    null,
                                    tmp2,
                                    tmp3,
                                    null);
                            mount_mas.set(btnId, fix_class);
                            break;
                    }
                }
            });

            checkBoxType2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                "2");
                        mount_mas.set(btnId, fix_class);
                    } else if (!isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                null);
                        mount_mas.set(btnId, fix_class);
                    }
                }
            });

            checkBoxType3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                "3");
                        mount_mas.set(btnId, fix_class);
                    } else if (!isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                null);
                        mount_mas.set(btnId, fix_class);
                    }
                }
            });

            checkBoxType4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                "4");
                        mount_mas.set(btnId, fix_class);
                    } else if (!isChecked) {
                        ForAdapterClass fix_class = new ForAdapterClass(tmp1,
                                null,
                                null,
                                tmp2,
                                tmp3,
                                null);
                        mount_mas.set(btnId, fix_class);
                    }
                }
            });

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

    void brigade() {

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
            String sqlQuewy = "select project_id, date_time, mounter_id "
                    + "FROM rgzbn_gm_ceiling_projects_mounts " +
                    "where date_time = '" + date_mount1 + "' and mounter_id =?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{id_b});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String project_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        String project_mounting_date = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        String project_mounter = c.getString(c.getColumnIndex(c.getColumnName(2)));

                        double n5 = 0;
                        sqlQuewy = "select n5 "
                                + "FROM rgzbn_gm_ceiling_calculations " +
                                "where project_id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{project_id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    String n5_str = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    n5 += Double.parseDouble(n5_str);

                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();

                        String project_info = "";
                        sqlQuewy = "select project_info "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where _id = ?";
                        cc = db.rawQuery(sqlQuewy, new String[]{project_id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    project_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
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
                                    sel_work.add(new Select_work(project_id, i + ":00 - " + (i + 1) + ":00",
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

    }

}