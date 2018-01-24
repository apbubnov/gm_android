package ru.ejevikaapp.authorization.Fragments;


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

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.Activity_inform_zapysch;
import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

public class Frag_g3_zapusch extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID="", gager_id="";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View View = inflater.inflate(R.layout.frag_g3_zapusch, container, false);

        list_clients = (ListView)View.findViewById(R.id.list_client);

        mSwipeRefreshLayout = (SwipeRefreshLayout) View.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return View;
    }

    @Override
    public void onRefresh() {

        getActivity().startService(new Intent(getActivity(), Service_Sync_Import.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Отменяем анимацию обновления
                mSwipeRefreshLayout.setRefreshing(false);
                onResume();
            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        clients();
    }

    void clients (){

        SP = this.getActivity().getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");

        SP = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

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
            sqlQuewy = "SELECT _id "
                    + "FROM rgzbn_gm_ceiling_projects" +
                    " WHERE project_status <> ? and project_status <> ? and client_id = ? " +
                    "order by _id desc";
            c = db.rawQuery(sqlQuewy, new String[]{"1", "0", String.valueOf(client.get(g))});
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
                                String id_client = "";
                                String fio = "";
                                String project_status = "";
                                String project_status_title = "";

                                sqlQuewy = "SELECT project_info, client_id, project_status "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where _id = ?";

                                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{k.getString(kdIndex)});

                                if (cursor_1 != null) {
                                    if (cursor_1.moveToFirst()) {
                                        do {
                                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                                            id_client = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
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

                                sqlQuewy = "SELECT phone "
                                        + "FROM rgzbn_gm_ceiling_clients_contacts" +
                                        " WHERE client_id = ?";

                                Cursor cursor = db.rawQuery(sqlQuewy, new String[]{id_client});

                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            phone = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();

                                sqlQuewy = "SELECT client_name "
                                        + "FROM rgzbn_gm_ceiling_clients" +
                                        " WHERE _id = ?";

                                cursor = db.rawQuery(sqlQuewy, new String[]{id_client});

                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            fio = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                        } while (cursor.moveToNext());
                                    }
                                }
                                cursor.close();

                                Log.d("spisok", id_client + " " + k.getString(kdIndex));

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(k.getString(kdIndex), fio,
                                        p_info, id_client, project_status_title);
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
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getPhone();
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

}
