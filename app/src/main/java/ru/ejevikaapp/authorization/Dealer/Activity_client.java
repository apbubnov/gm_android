package ru.ejevikaapp.authorization.Dealer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class Activity_client extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", gager_id;
    View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRefresh() {

        startService(new Intent(this, Service_Sync_Import.class));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        client_mas.clear();
        list_clients = (ListView) findViewById(R.id.list_client);
        clients();
    }

    void clients() {

        SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(this);

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

        for (int g = 0; g < client.size(); g++) {

            String id_proj = "";
            String project_calculation_date = "";
            String id_client = "";
            Double project_sum = 0.0;
            Double transport = 0.0;
            Double distance = 0.0;
            Double distance_col = 0.0;
            String fio = "";

            sqlQuewy = "SELECT _id, client_id, project_sum, transport, distance, distance_col "
                    + "FROM rgzbn_gm_ceiling_projects " +
                    "where client_id = ?";

            Cursor cursor_1 = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client.get(g))});

            if (cursor_1 != null) {
                if (cursor_1.moveToFirst()) {
                    do {
                        id_proj = cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(0)));
                        try {
                            project_sum += Double.parseDouble(cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(2))));
                        } catch (Exception e) {
                        }
                        try {
                            transport += Double.parseDouble(cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(3))));
                        } catch (Exception e) {
                        }
                        try {
                            distance += Double.parseDouble(cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(4))));
                        } catch (Exception e) {
                        }
                        try {
                            distance_col += Double.parseDouble(cursor_1.getString(cursor_1.getColumnIndex(cursor_1.getColumnName(5))));
                        } catch (Exception e) {
                        }

                        double components_sum = 0.0;
                        double canvases_sum = 0.0;
                        double mounting_sum = 0.0;

                        sqlQuewy = "SELECT components_sum, canvases_sum, mounting_sum "
                                + "FROM rgzbn_gm_ceiling_calculations " +
                                "where project_id = ?";
                        Cursor cursor_2 = db.rawQuery(sqlQuewy, new String[]{id_proj});
                        if (cursor_2 != null) {
                            if (cursor_2.moveToFirst()) {
                                do {

                                    Log.d("mLog", id_proj);

                                    try {
                                        components_sum += Double.parseDouble(cursor_2.getString(cursor_2.getColumnIndex(cursor_2.getColumnName(0))));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        canvases_sum += Double.parseDouble(cursor_2.getString(cursor_2.getColumnIndex(cursor_2.getColumnName(1))));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        mounting_sum += Double.parseDouble(cursor_2.getString(cursor_2.getColumnIndex(cursor_2.getColumnName(2))));
                                    } catch (Exception e) {
                                    }

                                    Log.d("mLog", id_proj + " " + components_sum + " " + canvases_sum + " " + mounting_sum);

                                } while (cursor_2.moveToNext());
                            }
                        }
                        cursor_2.close();

                        project_sum = Double.valueOf(Math.round((project_sum - components_sum - canvases_sum - mounting_sum) * 100) / 100);

                    } while (cursor_1.moveToNext());
                }
            }
            cursor_1.close();

            sqlQuewy = "SELECT client_name "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client.get(g))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        fio = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            Frag_client_schedule_class fc = new Frag_client_schedule_class(String.valueOf(id_proj), fio,
                    null, String.valueOf(project_sum), null);
            client_mas.add(fc);

        }


        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_fio, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId_client();
            }
        });

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.clients_item, dict);
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

                SP = getSharedPreferences("id_client_spisok", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", String.valueOf(c_id));
                ed.commit();

                Intent intent = new Intent(Activity_client.this, Activity_inform_proj.class);
                startActivity(intent);
            }
        });
    }
}