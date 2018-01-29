package ru.ejevikaapp.authorization.Dealer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.Crew.Activity_calendar;
import ru.ejevikaapp.authorization.Crew.Activity_crew;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_spisok_brigade extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    ListView list_brigade;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID="", gager_id;
    View view;

    Button btn_add_brigade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spisok_brigade);

        btn_add_brigade = (Button) findViewById(R.id.btn_add_brigade);
        btn_add_brigade.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        client_mas.clear();
        list_brigade = (ListView)findViewById(R.id.list_brigade);
        clients();
    }

    void clients (){

        SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList brigade = new ArrayList();
        ArrayList mounters = new ArrayList();

        String sqlQuewy = "SELECT _id "
                + "FROM rgzbn_users " +
                "where dealer_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{dealer_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String br = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    sqlQuewy = "SELECT * "
                            + "FROM rgzbn_user_usergroup_map " +
                            "where user_id = ? and group_id = 11";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{br});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                brigade.add(br);
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();

                } while (c.moveToNext());
            }
        }
        c.close();

        for (int g = 0; g<brigade.size(); g++) {

            String name_br = "";

            sqlQuewy = "SELECT name "
                    + "FROM rgzbn_users " +
                    "where _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(brigade.get(g))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        name_br = c.getString(c.getColumnIndex(c.getColumnName(0))) + " :";
                    } while (c.moveToNext());
                }
            }
            c.close();

            sqlQuewy = "SELECT id_mounter "
                    + "FROM rgzbn_gm_ceiling_mounters_map" +
                    " WHERE id_brigade = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(brigade.get(g))});
            int i = 0;
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        sqlQuewy = "SELECT name "
                                + "FROM rgzbn_gm_ceiling_mounters " +
                                "where _id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    name_br +="\n     " + cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();

                    } while (c.moveToNext());
                }
                c.close();
            }   else {
                Log.d("mLog", "2");
            }

            Frag_client_schedule_class fc = new Frag_client_schedule_class(String.valueOf(brigade.get(g)),
                    name_br, null, null, null);
            client_mas.add(fc);
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.name_brigade, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.brigade_list, dict);
        list_brigade.setAdapter(adapter);

        list_brigade.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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

                Intent intent = new Intent(Activity_spisok_brigade.this, Activity_calendar.class);
                intent.putExtra("id_brigade", p_id);
                startActivity(intent);
            }
        });
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_brigade:
                Intent intent;
                intent = new Intent(this, Activity_add_brigade.class);
                startActivity(intent);
                break;
        }
    }
}
