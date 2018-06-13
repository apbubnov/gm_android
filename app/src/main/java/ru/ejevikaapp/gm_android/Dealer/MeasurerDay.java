package ru.ejevikaapp.gm_android.Dealer;

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
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class MeasurerDay extends AppCompatActivity {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", gager_id = "", id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurer_day);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        list_clients = (ListView) findViewById(R.id.List_mounting);
        clients();
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

    void clients() {

        String day_mount = getIntent().getStringExtra("day_mount");
        String user_id = getIntent().getStringExtra("user_id");

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "select project_calculation_date, project_info "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where project_calculator = ? and project_calculation_date > ? and project_calculation_date < ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, day_mount + " 00:00:00", day_mount + " 23:00:00"});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String project_calculation_date = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String project_info = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null, project_calculation_date,
                            project_info, null, null,null);
                    client_mas.add(fc);


                } while (c.moveToNext());
            }
            c.close();
        }


        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_count, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.list_2column, dict);
        list_clients.setAdapter(adapter);

    }
}
