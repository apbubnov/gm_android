package ru.ejevikaapp.gm_android.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_zamer;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.Activity_for_spisok;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_spisok extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Calendar dateAndTime = new GregorianCalendar();

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    String SAVED_ID = "", user_id;
    View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    Context ctx;

    Button btn_add_zamer, btn_history;
    ImageButton btn_client_call;

    TextView last_history, last_history_time, add_client_call;

    EditText add_client_call_note;

    String activity_client;

    public Frag_spisok() {

        ctx = this.getContext();
    }

    public static Frag_spisok newInstance() {
        return new Frag_spisok();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_spisok, container, false);

        btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
        btn_add_zamer.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorBlue);

        getActivity().startService(new Intent(getActivity(), Service_Sync.class));
        client_mas.clear();
        list_clients = (ListView) view.findViewById(R.id.list_client);
        clients();

        return view;
    }

    @Override
    public void onRefresh() {
        if (HelperClass.isOnline(getActivity())) {
            getActivity().startService(new Intent(getActivity(), Service_Sync_Import.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                }
            }, 3000);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity().getApplicationContext(), "проверьте подключение к интернету", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        client_mas.clear();
        clients();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences SP = getActivity().getSharedPreferences("activity_client", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();
    }

    void clients() {

        SharedPreferences SP = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        SP = getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        // если зашёл после выбора клиента (Дилер)
        SP = getActivity().getSharedPreferences("activity_client", MODE_PRIVATE);
        activity_client = SP.getString("", "");

        LinearLayout layout_history = (LinearLayout) view.findViewById(R.id.layout_history);

        if (activity_client.isEmpty()) {
            layout_history.setVisibility(View.GONE);
        } else {

            btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
            btn_add_zamer.setOnClickListener(this);
            btn_history = (Button) view.findViewById(R.id.btn_history);
            btn_history.setOnClickListener(this);
            btn_client_call = (ImageButton) view.findViewById(R.id.btn_client_call);
            btn_client_call.setOnClickListener(this);

            last_history = (TextView) view.findViewById(R.id.last_history);
            last_history_time = (TextView) view.findViewById(R.id.last_history_time);

            add_client_call = (TextView) view.findViewById(R.id.add_client_call);
            add_client_call.setOnClickListener(this);

            add_client_call_note = (EditText) view.findViewById(R.id.add_client_call_note);

            history();
        }

        dbHelper = new DBHelper(getActivity());
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client = new ArrayList();
        ArrayList client_project = new ArrayList();
        String usergroup = "";
        String sqlQuewy = "SELECT group_id "
                + "FROM rgzbn_user_usergroup_map" +
                " WHERE user_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    usergroup = c.getString(c.getColumnIndex(c.getColumnName(0)));

                } while (c.moveToNext());
            }
        }
        c.close();

        if (usergroup.equals("21") || usergroup.equals("22")) { // замерщик

            sqlQuewy = "SELECT client_id "
                    + "FROM rgzbn_gm_ceiling_projects " +
                    "where project_calculator = ? and project_status = 1 " +
                    "group by client_id ";
            c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String client_id = (c.getString(c.getColumnIndex(c.getColumnName(0))));
                        if (HelperClass.associated_client(getActivity(), user_id, client_id)) {
                        } else {
                            client.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        }

                    } while (c.moveToNext());
                }
            }
            c.close();

        } else if (usergroup.equals("14")) {    // дилер


            if (activity_client.equals("")) {
                sqlQuewy = "SELECT client_id "
                        + "FROM rgzbn_gm_ceiling_projects " +
                        "where deleted_by_user = ? and project_status = ? " +
                        "group by client_id " +
                        "order by _id desc";
                c = db.rawQuery(sqlQuewy, new String[]{"0", "1"});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            client_project.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                for (int i = 0; client_project.size() > i; i++) {
                    sqlQuewy = "SELECT _id "
                            + "FROM rgzbn_gm_ceiling_clients " +
                            "where dealer_id = ? and _id = ? and deleted_by_user = ?";
                    c = db.rawQuery(sqlQuewy, new String[]{user_id, String.valueOf(client_project.get(i)), "0"});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                String client_id = (c.getString(c.getColumnIndex(c.getColumnName(0))));

                                boolean bool = HelperClass.associated_client(getActivity(), user_id, client_id);

                                Log.d("mLog", "bool " + bool);

                                if (bool) {
                                } else {
                                    client.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                }

                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                }
            } else {
                client.add(activity_client);
            }
        }

        for (int g = 0; g < client.size(); g++) {
            if (activity_client.equals("")) {
                sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE project_status = ? and client_id = ? ";
                c = db.rawQuery(sqlQuewy, new String[]{"1", String.valueOf(client.get(g))});
            } else {
                sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE client_id = ? ";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client.get(g))});
            }
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_projects" +
                                " WHERE _id = ?";
                        Cursor k = db.rawQuery(sqlQuewy, new String[]{id});
                        if (k.moveToFirst()) {
                            int kdIndex = k.getColumnIndex(DBHelper.KEY_ID);
                            do {
                                String p_info = "";
                                String project_calculation_date = "";
                                String id_client = "";
                                String fio = "";
                                String project_note = "-";

                                sqlQuewy = "SELECT project_info, client_id, project_calculation_date, gm_manager_note "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where _id = ? ";
                                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{k.getString(kdIndex)});
                                if (cursor_1 != null) {
                                    if (cursor_1.moveToFirst()) {
                                        do {
                                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                                            id_client = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
                                            project_calculation_date = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2)));
                                            project_note = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(3)));

                                        } while (cursor_1.moveToNext());
                                    }
                                }
                                cursor_1.close();

                                SimpleDateFormat out_format = null;
                                SimpleDateFormat out_format_time = null;
                                SimpleDateFormat out_format_minute;
                                Date change_max = null;
                                int hours = 0;

                                try {
                                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    change_max = ft.parse(project_calculation_date);

                                    out_format = new SimpleDateFormat("dd.MM.yyyy");
                                    out_format_minute = new SimpleDateFormat("HH");

                                    hours = Integer.parseInt(out_format_minute.format(change_max)) + 1;

                                    out_format_time = new SimpleDateFormat("HH:mm");

                                } catch (Exception e) {
                                }

                                String tempId = k.getString(kdIndex);

                                String tempDate =
                                        String.valueOf(out_format.format(change_max)
                                                + "\n" + out_format_time.format(change_max))
                                                + " - " + hours + ":00";
                                String tempIdClient = String.valueOf(client.get(g));

                                /* Обработка адреса */
                                p_info = p_info.replace("Воронеж, ", "");

                                if (project_note == null || project_note.equals("null") || project_note.equals("")) {
                                    project_note = "-";
                                }

                                if (p_info.equals("null")) {
                                    p_info = "-";
                                }

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(
                                        tempId, tempDate, p_info, tempIdClient, project_note, null);
                                client_mas.add(fc);

                            } while (k.moveToNext());
                        }
                        k.close();

                    } while (c.moveToNext());
                }
                c.close();
            }
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();
        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item3, dict);
        list_clients.setAdapter(adapter);

        mSwipeRefreshLayout.setRefreshing(false);

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId();

                SharedPreferences SP = getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(p_id));
                ed.commit();

                String c_id = "";

                String sqlQuewy = "SELECT client_id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE _id = ?";

                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{p_id});
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            c_id = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();

                SP = getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", String.valueOf(c_id));
                ed.commit();

                Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                startActivity(intent);
            }
        });

        list_clients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Frag_client_schedule_class selectedid = client_mas.get(pos);
                final String cId = selectedid.getId();

                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("Удалить проект № " + cId + "?"); // сообщение
                ad.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_DELETED_BY_USER, "1");
                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?",
                                new String[]{cId});

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID_OLD, cId);
                        values.put(DBHelper.KEY_ID_NEW, "0");
                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                        values.put(DBHelper.KEY_SYNC, "0");
                        values.put(DBHelper.KEY_TYPE, "send");
                        values.put(DBHelper.KEY_STATUS, "1");
                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        onResume();

                        //Intent intent = new Intent(getActivity(), Activity_for_spisok.class);
                        //startActivity(intent);
                        //getActivity().finish();
                    }
                });
                ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setCancelable(true);
                ad.show();
                return true;
            }
        });

    }

    void history() {

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select date_time, text "
                + "FROM rgzbn_gm_ceiling_client_history " +
                "where client_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{activity_client});
        if (c != null) {
            if (c.moveToLast()) {
                String date_time = c.getString(c.getColumnIndex(c.getColumnName(0)));
                String text = c.getString(c.getColumnIndex(c.getColumnName(1)));

                last_history.setText(text);
                last_history_time.setText(date_time);
            }
        }
        c.close();
    }

    @Override
    public void onClick(View view) {

        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        ContentValues values;
        final Context context = getActivity();
        switch (view.getId()) {
            case R.id.btn_add_zamer:
                Intent intent = new Intent(getActivity(), Activity_zamer.class);
                startActivity(intent);
                break;
            case R.id.btn_history:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.layout_history_client, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                final ListView list_history = (ListView) promptsView.findViewById(R.id.list_history);
                final EditText ed_history = (EditText) promptsView.findViewById(R.id.ed_history);
                final ImageButton btn_history = (ImageButton) promptsView.findViewById(R.id.btn_history);

                final SQLiteDatabase finalDb3 = db;
                btn_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_history = ed_history.getText().toString();
                        if (str_history.length() > 0) {

                            int max_id = 0;
                            try {
                                String sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_client_history " +
                                        "where _id>? and _id<?";
                                Cursor c = finalDb3.rawQuery(sqlQuewy, new String[]{String.valueOf(Integer.parseInt(user_id) * 100000),
                                        String.valueOf(Integer.parseInt(user_id) * 100000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id = Integer.parseInt(user_id) * 100000 + 1;
                            }

                            String date = HelperClass.now_date(getActivity());

                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id);
                            values.put(DBHelper.KEY_CLIENT_ID, activity_client);
                            values.put(DBHelper.KEY_DATE_TIME, date);
                            values.put(DBHelper.KEY_TEXT, str_history);
                            finalDb3.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id);
                            values.put(DBHelper.KEY_ID_NEW, "0");
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_client_history");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            finalDb3.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                            HashMap<String, String> map;

                            String sqlQuewy = "select  date_time, text "
                                    + "FROM rgzbn_gm_ceiling_client_history " +
                                    "where client_id = ?";
                            Cursor c = finalDb3.rawQuery(sqlQuewy, new String[]{String.valueOf(activity_client)});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        String date_time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                                        String text = c.getString(c.getColumnIndex(c.getColumnName(1)));

                                        map = new HashMap<>();
                                        map.put("time", date_time);
                                        map.put("title", text);
                                        arrayList.add(map);

                                    } while (c.moveToNext());
                                }
                            }

                            SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2,
                                    new String[]{"title", "time"},
                                    new int[]{android.R.id.text1, android.R.id.text2});

                            list_history.setAdapter(adapter);

                            ed_history.setText("");

                            history();
                        }
                    }
                });

                ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                HashMap<String, String> map;

                String sqlQuewy = "select  date_time, text "
                        + "FROM rgzbn_gm_ceiling_client_history " +
                        "where client_id = ?";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(activity_client)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            String date_time = c.getString(c.getColumnIndex(c.getColumnName(0)));

                            String text = c.getString(c.getColumnIndex(c.getColumnName(1)));

                            map = new HashMap<>();
                            map.put("time", date_time);
                            map.put("title", text);
                            arrayList.add(map);

                        } while (c.moveToNext());
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2,
                        new String[]{"title", "time"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                list_history.setAdapter(adapter);

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();
                break;

            case R.id.add_client_call:
                setTime(add_client_call);
                setDate(add_client_call);

                final SQLiteDatabase finalDb4 = db;
                btn_client_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (add_client_call.getText().toString().length() > 0) {

                            int max_id = 0;
                            try {
                                String sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_client_history " +
                                        "where _id>? and _id<?";
                                Cursor c = finalDb4.rawQuery(sqlQuewy, new String[]{String.valueOf(Integer.parseInt(user_id) * 100000),
                                        String.valueOf(Integer.parseInt(user_id) * 100000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id = Integer.parseInt(user_id) * 100000 + 1;
                            }

                            String date = HelperClass.now_date(getActivity());

                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_ID, max_id);
                            values.put(DBHelper.KEY_CLIENT_ID, activity_client);
                            values.put(DBHelper.KEY_DATE_TIME, date);
                            values.put(DBHelper.KEY_TEXT, "Добавлен звонок на " + add_client_call.getText().toString());
                            finalDb4.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENT_HISTORY, null, values);

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id);
                            values.put(DBHelper.KEY_ID_NEW, "0");
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_client_history");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            finalDb4.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            max_id = 0;
                            try {
                                String sqlQuewy = "select MAX(_id) "
                                        + "FROM rgzbn_gm_ceiling_callback " +
                                        "where _id>? and _id<?";
                                Cursor c = finalDb4.rawQuery(sqlQuewy, new String[]{String.valueOf(Integer.parseInt(user_id) * 100000),
                                        String.valueOf(Integer.parseInt(user_id) * 100000 + 999999)});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                            max_id++;
                                        } while (c.moveToNext());
                                    }
                                }
                            } catch (Exception e) {
                                max_id = Integer.parseInt(user_id) * 100000 + 1;
                            }

                            date = HelperClass.now_date(getActivity());

                            try {
                                values = new ContentValues();
                                values.put(DBHelper.KEY_ID, max_id);
                                values.put(DBHelper.KEY_CLIENT_ID, activity_client);
                                values.put(DBHelper.KEY_DATE_TIME, date);
                                values.put(DBHelper.KEY_COMMENT, add_client_call_note.getText().toString());
                                values.put(DBHelper.KEY_MANAGER_ID, "");
                                values.put(DBHelper.KEY_NOTIFY, "");
                                finalDb4.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALLBACK, null, values);
                            } catch (Exception e) {
                                Log.d("mLog", "errror = " + e);
                            }

                            values = new ContentValues();
                            values.put(DBHelper.KEY_ID_OLD, max_id);
                            values.put(DBHelper.KEY_ID_NEW, "0");
                            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_callback");
                            values.put(DBHelper.KEY_SYNC, "0");
                            values.put(DBHelper.KEY_TYPE, "send");
                            values.put(DBHelper.KEY_STATUS, "1");
                            finalDb4.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                            add_client_call.setText("");
                            add_client_call_note.setText("");

                            history();
                        }
                    }
                });
                break;
        }
    }

    private void setInitialDateTimeCall() {
        add_client_call.setText(add_client_call.getText().toString() + " " +
                DateUtils.formatDateTime(getActivity(),
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME));
    }

    public void setDate(View v) {

        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 9) {
                            String editTextDateParam = dayOfMonth + ".0" + (monthOfYear + 1) + "." + year;
                            add_client_call.setText(editTextDateParam);
                        } else {
                            String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                            add_client_call.setText(editTextDateParam);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void setTime(View v) {
        new TimePickerDialog(getActivity(), call_time,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    DatePickerDialog.OnDateSetListener call_date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTimeCall();
        }
    };

    TimePickerDialog.OnTimeSetListener call_time = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTimeCall();
        }
    };
}