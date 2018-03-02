package ru.ejevikaapp.gm_android.Dealer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Activity_add_client;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

public class Activity_client extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", user_id;
    View view;

    Button btn_search, btn_add_client;
    EditText c_search;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        btn_search = (Button) findViewById(R.id.btn_search);
        btn_add_client = (Button) findViewById(R.id.btn_add_client);
        c_search = (EditText) findViewById(R.id.c_search);

        btn_search.setOnClickListener(this);
        btn_add_client.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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

        list_clients = (ListView) findViewById(R.id.list_client);
        clients("");

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_search:
                clients(c_search.getText().toString());

                break;

            case R.id.btn_add_client:
                Intent intent = new Intent(Activity_client.this, Activity_add_client.class);
                startActivity(intent);
                break;
        }
    }

    void clients(String client_name) {

        client_mas.clear();

        SP = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client = new ArrayList();

        if (client_name.equals("")) {
            String sqlQuewy = "SELECT _id, client_name, created "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? " +
                    "group by client_name";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        sqlQuewy = "SELECT project_info, project_status "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where client_id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                        if (cc != null) {
                            if (cc.moveToLast()) {

                                    String status = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                    sqlQuewy = "SELECT title "
                                            + "FROM rgzbn_gm_ceiling_status " +
                                            "where _id = ? ";
                                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{status});
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            do {
                                                status = c1.getString(c1.getColumnIndex(c1.getColumnName(0)));
                                            } while (c1.moveToNext());
                                        }
                                    }
                                    c1.close();

                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            c.getString(c.getColumnIndex(c.getColumnName(1))),
                                            cc.getString(cc.getColumnIndex(cc.getColumnName(0))),
                                            c.getString(c.getColumnIndex(c.getColumnName(0))),
                                            status,
                                            c.getString(c.getColumnIndex(c.getColumnName(2))));
                                    client_mas.add(fc);

                            } else {
                                Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                        c.getString(c.getColumnIndex(c.getColumnName(1))),
                                        "-",
                                        c.getString(c.getColumnIndex(c.getColumnName(0))),
                                        "-",
                                        c.getString(c.getColumnIndex(c.getColumnName(2))));
                                client_mas.add(fc);
                            }
                            cc.close();
                        }

                    } while (c.moveToNext());
                }
            }
            c.close();
        } else {
            String sqlQuewy = "SELECT _id, client_name, created "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? and client_name LIKE '%" + client_name + "%' " +
                    "group by client_name ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        sqlQuewy = "SELECT project_info, project_status "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where client_id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                        if (cc != null) {
                            if (cc.moveToLast()) {

                                String status = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_status " +
                                        "where _id = ? ";
                                Cursor c1 = db.rawQuery(sqlQuewy, new String[]{status});
                                if (c1 != null) {
                                    if (c1.moveToFirst()) {
                                        do {
                                            status = c1.getString(c1.getColumnIndex(c1.getColumnName(0)));
                                        } while (c1.moveToNext());
                                    }
                                }
                                c1.close();

                                Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                        c.getString(c.getColumnIndex(c.getColumnName(1))),
                                        cc.getString(cc.getColumnIndex(cc.getColumnName(0))),
                                        c.getString(c.getColumnIndex(c.getColumnName(0))),
                                        status,
                                        c.getString(c.getColumnIndex(c.getColumnName(2))));
                                client_mas.add(fc);

                            } else {
                                Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                        c.getString(c.getColumnIndex(c.getColumnName(1))),
                                        "-",
                                        c.getString(c.getColumnIndex(c.getColumnName(0))),
                                        "-",
                                        c.getString(c.getColumnIndex(c.getColumnName(2))));
                                client_mas.add(fc);
                            }
                            cc.close();
                        }

                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getCreate();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });
        FunDapter adapter = new FunDapter(this, client_mas, R.layout.clients_item44, dict);
        list_clients.setAdapter(adapter);
        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId_client();

                SP = getSharedPreferences("activity_client", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(p_id));
                ed.commit();

                Intent intent = new Intent(Activity_client.this, Activity_for_spisok.class);
                startActivity(intent);
            }
        });
    }
}