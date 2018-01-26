package ru.ejevikaapp.authorization.Fragments;


import android.content.Context;
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
import android.widget.Button;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.Activity_zamer;
import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_spisok extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID="", gager_id;
    View view;

    Button btn_add_zamer;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    Context ctx;

    public Frag_spisok() {

        ctx = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_spisok, container, false);

        btn_add_zamer = (Button)view.findViewById(R.id.btn_add_zamer);
        btn_add_zamer.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view ;
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

        client_mas.clear();
        list_clients = (ListView)view.findViewById(R.id.list_client);
        clients();
    }

    void clients (){

        SP = this.getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
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
                    " WHERE project_status = ? and client_id = ? " +
                    "order by project_calculation_date";

            c = db.rawQuery(sqlQuewy, new String[]{"1", String.valueOf(client.get(g))});

            int i = 0;
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
                                i++;

                                sqlQuewy = "SELECT project_info, client_id, project_calculation_date "
                                        + "FROM rgzbn_gm_ceiling_projects " +
                                        "where _id = ?";

                                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{k.getString(kdIndex)});

                                if (cursor_1 != null) {
                                    if (cursor_1.moveToFirst()) {
                                        do {
                                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                                            id_client = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
                                            project_calculation_date = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2)));

                                            Log.d("count_client", p_info + " " + id_client);

                                        } while (cursor_1.moveToNext());
                                    }
                                }
                                cursor_1.close();

                                Log.d("spisok", id_client + " " + k.getString(kdIndex));

                                SimpleDateFormat out_format = null;
                                SimpleDateFormat out_format_time = null;
                                SimpleDateFormat out_format_minute = null;
                                Date change_max = null;
                                Date minute = null;

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

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(k.getString(kdIndex),
                                        String.valueOf(out_format.format(change_max) + "\n" + out_format_time.format(change_max))
                                                + " - " + hours + ":00",
                                        p_info, String.valueOf(client.get(g)), null);
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
        dict.addStringField(R.id.c_fio, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item, dict);
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

                Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_zamer:

                Intent intent = new Intent(getActivity(), Activity_zamer.class);
                startActivity(intent);
                break;

        }
        }
}
