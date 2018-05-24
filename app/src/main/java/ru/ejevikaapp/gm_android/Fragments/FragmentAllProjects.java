package ru.ejevikaapp.gm_android.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_zamer;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.Dealer_office;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAllProjects extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", user_id;
    View view;

    Button btn_add_zamer;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    Context ctx;

    public FragmentAllProjects() {

        ctx = this.getContext();
    }

    public static FragmentAllProjects newInstance() {
        return new FragmentAllProjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_projects, container, false);

        btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
        btn_add_zamer.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorBlue);

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
            Toast.makeText(getActivity().getApplicationContext(),
                    "проверьте подключение к интернету", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            client_mas.clear();
            list_clients = (ListView) view.findViewById(R.id.list_client);
            clients();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SP = getActivity().getSharedPreferences("activity_client", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();
    }

    void clients() {

        SP = this.getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        SP = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(getActivity());
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client_project = new ArrayList();

        String sqlQuewy = "SELECT _id, client_id "
                + "FROM rgzbn_gm_ceiling_projects " +
                "order by change_time desc";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String client_id = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    sqlQuewy = "SELECT * "
                            + "FROM rgzbn_gm_ceiling_clients " +
                            "where _id = ? and dealer_id = ?";
                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{client_id, dealer_id});
                    if (c1 != null) {
                        if (c1.moveToFirst()) {

                            client_project.add(id);
                        }
                    }
                    c1.close();

                } while (c.moveToNext());
            }
        }
        c.close();

        try {
            for (int g = 0; g < client_project.size(); g++) {

                String p_info = "";
                String change_time = "";
                String id_client = "";
                String fio = "";
                String project_status = "";

                sqlQuewy = "SELECT project_info, client_id, change_time, project_status "
                        + "FROM rgzbn_gm_ceiling_projects " +
                        "where _id = ? ";

                Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client_project.get(g))});

                if (cursor_1 != null) {
                    if (cursor_1.moveToFirst()) {
                        do {
                            p_info = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                            id_client = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(1)));
                            change_time = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2)));
                            project_status = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(3)));

                        } while (cursor_1.moveToNext());
                    }
                }
                cursor_1.close();

                SimpleDateFormat out_format = null;
                SimpleDateFormat out_format_time = null;
                SimpleDateFormat out_format_minute;
                Date change_max = null;
                int hours = 0;

                String tempId = String.valueOf(client_project.get(g));

                String tempIdClient = id_client;

                if (p_info.equals("null")){
                    p_info = "";
                }
                /* Обработка адреса */
                p_info = p_info.replace("Воронеж, ", "");

                if (project_status.equals("1")) {
                    project_status = "Просчёт";
                } else {
                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_status " +
                            "where _id = ? ";

                    cursor_1 = db.rawQuery(sqlQuewy, new String[]{project_status});

                    if (cursor_1 != null) {
                        if (cursor_1.moveToFirst()) {
                            do {
                                project_status = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));

                            } while (cursor_1.moveToNext());
                        }
                    }
                    cursor_1.close();
                }

                Frag_client_schedule_class fc = new Frag_client_schedule_class(
                        tempId, change_time, p_info, tempIdClient, project_status, null);
                client_mas.add(fc);
            }
        } catch (Exception e) {
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

                        String sqlQuewy = "SELECT _id "
                                + "FROM rgzbn_gm_ceiling_calculations " +
                                "where project_id = ? ";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(cId)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    int calcId = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, "calculation_id = ?", new String[]{String.valueOf(calcId)});
                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, "calculation_id = ?", new String[]{String.valueOf(calcId)});

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, "project_id = ?", new String[]{String.valueOf(cId)});
                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, "_id = ?", new String[]{String.valueOf(cId)});

                        loadFragment(FragmentAllProjects.newInstance());
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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
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
