package ru.ejevikaapp.gm_android.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

public class Frag_g3_zapusch extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID="", user_id="";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static Frag_g3_zapusch newInstance() {
        return new Frag_g3_zapusch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View View = inflater.inflate(R.layout.frag_g3_zapusch, container, false);

        list_clients = (ListView)View.findViewById(R.id.list_client);

        mSwipeRefreshLayout = (SwipeRefreshLayout) View.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorBlue);

        return View;
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

        try {
            SP = this.getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
            user_id = SP.getString("", "");

            dbHelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

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

            if (usergroup.equals("14")) {
                clients_dealer();
            } else if (usergroup.equals("21") || usergroup.equals("22")) {
                clients_gager();
            }

        }catch (Exception e){
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SP = this.getActivity().getSharedPreferences("activity_mounting_1", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = this.getActivity().getSharedPreferences("activity_mounting_2", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

    }

    void clients_gager (){

        client_mas.clear();

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Integer> client = new ArrayList();

        String sqlQuewy = "SELECT client_id "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where project_calculator = ? and project_status <> 1 " +
                " order by _id desc";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    boolean bool = false;

                    int clin = c.getInt(c.getColumnIndex(c.getColumnName(0)));

                    for(int g = 0; client.size()>g; g++) {
                        if (clin == client.get(g)){
                            bool = true;
                        }
                    }

                    if (!bool) {
                        client.add(clin);
                    }

                } while (c.moveToNext());
            }
        }
        c.close();

        for (int g = 0; g<client.size(); g++) {

            sqlQuewy = "SELECT _id "
                    + "FROM rgzbn_gm_ceiling_projects" +
                    " WHERE project_status <> ? and project_status <> ? and client_id = ?" +
                    " order by _id desc";
            c = db.rawQuery(sqlQuewy, new String[]{"1", "0", String.valueOf(client.get(g))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_projects" +
                                " WHERE _id = ? " +
                                " order by _id desc";
                        Cursor k = db.rawQuery(sqlQuewy, new String[]{id});
                        if (k.moveToFirst()) {
                            int kdIndex = k.getColumnIndex(DBHelper.KEY_ID);
                            do {
                                String p_info = "";
                                String phone = "";
                                String created = "";
                                String fio = "";
                                String project_status = "";
                                String project_status_title = "";

                                sqlQuewy = "SELECT project_info, project_calculation_date, project_status "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where _id = ?";

                                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{k.getString(kdIndex)});

                                if (cursor_1 != null) {
                                    if (cursor_1.moveToFirst()) {
                                        do {
                                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                                            created = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
                                            project_status = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2)));

                                        } while (cursor_1.moveToNext());
                                    }
                                }

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_status " +
                                        "where _id = ?";

                                Cursor cursor_2 = db.rawQuery(sqlQuewy, new String[]{project_status});

                                if (cursor_2 != null) {
                                    if (cursor_2.moveToFirst()) {
                                        do {
                                            project_status_title = cursor_2.getString(cursor_2.getColumnIndex(cursor_2.getColumnName(0)));

                                        } while (cursor_2.moveToNext());
                                    }
                                }

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(k.getString(kdIndex), fio,
                                        p_info, created, project_status_title, null);
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
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId_client();
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

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId();

                SP = getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
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

                Intent intent = new Intent(getActivity(), Activity_inform_zapysch.class);
                startActivity(intent);
            }
        });
    }

    void clients_dealer (){

        client_mas.clear();

        SP = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        final SharedPreferences[] SP = {this.getActivity().getSharedPreferences("activity_mounting_1", MODE_PRIVATE)}; // если зашёл после выбора клиента (Дилер)
        String activity_mounting_1 = SP[0].getString("", "");

        SP[0] = this.getActivity().getSharedPreferences("activity_mounting_2", MODE_PRIVATE); // если зашёл после выбора клиента (Дилер)
        String activity_mounting_2 = SP[0].getString("", "");

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList client = new ArrayList();

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

        for (int g = 0; g<client.size(); g++) {
            if (activity_mounting_1.equals("true")){

                sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE client_id = ? and (project_status = 10 or project_status = 5)" +
                        " order by _id desc";
            } else if (activity_mounting_2.equals("true")) {

                sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE client_id = ? and project_status = 4" +
                        " order by _id desc";
            } else {
                sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_projects" +
                        " WHERE client_id = ? and project_status = 5" +
                        " order by _id desc";
            }
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client.get(g))});
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
                                String phone = "";
                                String project_calculation_date = "";
                                String fio = "";
                                String project_status = "";
                                String project_status_title = "";

                                sqlQuewy = "SELECT project_info, project_calculation_date, project_status "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where _id = ?";

                                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{k.getString(kdIndex)});

                                if (cursor_1 != null) {
                                    if (cursor_1.moveToFirst()) {
                                        do {
                                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                                            project_calculation_date = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
                                            project_status = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2)));

                                        } while (cursor_1.moveToNext());
                                    }
                                }

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_status " +
                                        "where _id = ?";

                                Cursor cursor_2 = db.rawQuery(sqlQuewy, new String[]{project_status});

                                if (cursor_2 != null) {
                                    if (cursor_2.moveToFirst()) {
                                        do {
                                            project_status_title = cursor_2.getString(cursor_2.getColumnIndex(cursor_2.getColumnName(0)));

                                        } while (cursor_2.moveToNext());
                                    }
                                }

                                SimpleDateFormat out_format = null;
                                SimpleDateFormat out_format_time = null;
                                SimpleDateFormat out_format_minute = null;
                                Date change_max = null;
                                int hours = 0;
                                try {
                                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    change_max = ft.parse(project_calculation_date);

                                    out_format = new SimpleDateFormat("dd.MM.yyyy");
                                    out_format_minute = new SimpleDateFormat("HH");

                                    hours = Integer.parseInt(out_format_minute.format(change_max))+1;

                                    out_format_time = new SimpleDateFormat("HH:mm");

                                }catch (Exception e){
                                }

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(k.getString(kdIndex), fio,
                                        p_info,
                                        String.valueOf(out_format.format(change_max) +" "+ out_format_time.format(change_max))
                                        + " - " + hours + ":00",
                                        project_status_title, null);
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
                return nc.getId_client();
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

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId();

                SP[0] = getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP[0].edit();
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

                SP[0] = getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
                ed = SP[0].edit();
                ed.putString("", String.valueOf(c_id));
                ed.commit();

                Intent intent = new Intent(getActivity(), Activity_inform_zapysch.class);
                startActivity(intent);
            }
        });
    }

}
