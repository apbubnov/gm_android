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
import android.widget.EditText;
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
import ru.ejevikaapp.authorization.Fragments.Activity_calcul;
import ru.ejevikaapp.authorization.Fragments.Frag_spisok;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync_Import;

import static android.content.Context.MODE_PRIVATE;

public class Activity_client extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", gager_id;
    View view;

    Button btn_search;
    EditText c_search;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        btn_search = (Button) findViewById(R.id.btn_search);
        c_search = (EditText) findViewById(R.id.c_search);

        btn_search.setOnClickListener(this);

        list_clients = (ListView) findViewById(R.id.list_client);
        clients("");

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
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_search:
                clients(c_search.getText().toString());

                break;
        }
    }

    void clients(String client_name) {

        client_mas.clear();

        SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client = new ArrayList();

        if (client_name.equals("")) {
            String sqlQuewy = "SELECT _id, client_name "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? " +
                    "group by client_name";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{dealer_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                c.getString(c.getColumnIndex(c.getColumnName(1))),
                                null, c.getString(c.getColumnIndex(c.getColumnName(0))), null);
                        client_mas.add(fc);

                        //client.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } while (c.moveToNext());
                }
            }
            c.close();
        } else {
            String sqlQuewy = "SELECT _id, client_name "
                    + "FROM rgzbn_gm_ceiling_clients " +
                    "where dealer_id = ? and client_name LIKE '%" + client_name + "%' " +
                    "group by client_name ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{dealer_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                c.getString(c.getColumnIndex(c.getColumnName(1))),
                                null, c.getString(c.getColumnIndex(c.getColumnName(0))), null);
                        client_mas.add(fc);

                        //client.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.name_brigade, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.brigade_list, dict);
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