package ru.ejevikaapp.gm_android.Crew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_mounting_day extends AppCompatActivity {


    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", gager_id = "", id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mounting_day);

        list_clients = (ListView) findViewById(R.id.List_mounting);
        clients();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    void clients() {

        String day_mount = getIntent().getStringExtra("day_mount");
        String user_id = getIntent().getStringExtra("user_id");

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, day_mount + " 00:00:00", day_mount + " 23:00:00"});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String project_mounting_date = "";
                    String project_info = "";
                    String project_status = "";
                    String read_by_mounter = "";
                    Double n5 = 0.0;

                    Log.d("mLog", id);

                    sqlQuewy = "SELECT project_mounting_date, project_info, project_status, read_by_mounter "
                            + "FROM rgzbn_gm_ceiling_projects" +
                            " WHERE _id = ?";
                    Cursor k = db.rawQuery(sqlQuewy, new String[]{id});
                    if (k != null) {
                        if (k.moveToFirst()) {
                            do {
                                project_mounting_date = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                project_info = k.getString(k.getColumnIndex(k.getColumnName(1)));
                                project_status = k.getString(k.getColumnIndex(k.getColumnName(2)));
                                read_by_mounter = k.getString(k.getColumnIndex(k.getColumnName(3)));
                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "select n5 "
                            + "FROM rgzbn_gm_ceiling_calculations " +
                            "where project_id = ?";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                String n5_str = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                n5 += Double.parseDouble(n5_str);
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();

                    sqlQuewy = "select title "
                            + "FROM rgzbn_gm_ceiling_status " +
                            "where _id = ?";
                    cc = db.rawQuery(sqlQuewy, new String[]{project_status});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                project_status = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));

                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();

                    Log.d("mLog", id);

                    if (read_by_mounter.equals("0")) {
                        project_status += "/Не прочитан";
                    }

                    Frag_client_schedule_class fc = new Frag_client_schedule_class(id, project_mounting_date,
                            project_info, String.valueOf(n5), project_status,null);
                    client_mas.add(fc);


                } while (c.moveToNext());
            }
            c.close();
        }


        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
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

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.clients_item3, dict);
        list_clients.setAdapter(adapter);

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId();

                SP = getSharedPreferences("id_project_spisok", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(p_id));
                ed.commit();

                String project_status = "";
                String client = "";

                String sqlQuewy = "select project_status, client_id "
                        + "FROM rgzbn_gm_ceiling_projects " +
                        "where _id = ?";
                Cursor cc = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                if (cc != null) {
                    if (cc.moveToFirst()) {
                        do {
                            project_status = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                            client = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                        } while (cc.moveToNext());
                    }
                }
                cc.close();


                SP = getSharedPreferences("id_client_spisok", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", String.valueOf(client));
                ed.commit();

                if (project_status.equals("1")) {
                    Intent intent = new Intent(Activity_mounting_day.this, Activity_inform_proj.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Activity_mounting_day.this, Activity_inform_zapysch.class);
                    startActivity(intent);
                }
            }
        });
    }
}
