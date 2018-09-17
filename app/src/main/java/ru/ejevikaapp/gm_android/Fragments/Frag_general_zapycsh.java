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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pixplicity.sharp.Sharp;

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

import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
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

    String time_h = "";

    Button btn_transport_ok;

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

    Button btn, completedProject;
    private List<Button> BtnList = new ArrayList<Button>();
    private List<TextView> CheckBoxList = new ArrayList<TextView>();
    int i = 0;

    RadioButton rb_transport_0, rb_transport_1, rb_transport_2;

    LinearLayout.LayoutParams lin_calc;
    LinearLayout mainL, mainC, mainC2, mainL2;

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

        id_proj = (TextView) view.findViewById(R.id.id_proj);
        id_proj.setText("Проект №" + id_project);

        final_transport = (TextView) view.findViewById(R.id.final_transport);
        final_transport_sum = (TextView) view.findViewById(R.id.final_transport_sum);
        data_mounting = (TextView) view.findViewById(R.id.data_mounting);
        project_mounter = (TextView) view.findViewById(R.id.project_mounter);
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

                try {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    change_max = ft.parse(mount_date);
                    out_format = new SimpleDateFormat("dd.MM.yyyy");
                    out_format_minute = new SimpleDateFormat("HH");
                    hours = Integer.parseInt(out_format_minute.format(change_max)) + 1;
                    out_format_time = new SimpleDateFormat("HH:mm");
                    data_mounting.setText(String.valueOf(out_format.format(change_max) + " " + out_format_time.format(change_max))
                            + " - " + hours + ":00");
                } catch (Exception e) {
                }

                String mount_id = c.getString(c.getColumnIndex(c.getColumnName(1)));

                sqlQuewy = "SELECT name "
                        + "FROM rgzbn_users" +
                        " WHERE _id = ?";
                Cursor c2 = db.rawQuery(sqlQuewy, new String[]{mount_id});
                if (c2 != null) {
                    if (c2.moveToFirst()) {
                        fio = c2.getString(c2.getColumnIndex(c2.getColumnName(0)));
                        project_mounter.setText(fio);
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

        id_calc();

        transport();
        calc(id_calcul);

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

                    if(c.getString(c.getColumnIndex(c.getColumnName(2))) == null){
                    } else {
                        tmp += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    }

                    if(c.getString(c.getColumnIndex(c.getColumnName(3))) == null){
                    } else {
                        tmp2 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    }

                    if(c.getString(c.getColumnIndex(c.getColumnName(4))) == null){
                    } else {
                        tmp3 += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    }

                    if(c.getString(c.getColumnIndex(c.getColumnName(5))) == null){
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
        }catch (Exception e){
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
        }
    }
}