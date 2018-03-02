package ru.ejevikaapp.gm_android.Dealer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_mounting extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mounting);

        Button btn_mounting_1 = (Button) findViewById(R.id.btn_mounting_1);
        Button btn_mounting_2 = (Button) findViewById(R.id.btn_mounting_2);
        Button btn_brigade = (Button) findViewById(R.id.btn_brigade);

        btn_mounting_1.setOnClickListener(this);
        btn_mounting_2.setOnClickListener(this);
        btn_brigade.setOnClickListener(this);

        TextView txt_count_btn1 = (TextView) findViewById(R.id.count_btn1);
        TextView txt_count_btn2 = (TextView) findViewById(R.id.count_btn2);

        int count_1 = 0;
        int count_2 = 0;

        SharedPreferences SP_end = getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_clients " +
                "where dealer_id = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    sqlQuewy = "SELECT project_info, project_status "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where client_id = ? and (project_status = 10 or project_status = 5)";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                count_1 ++;
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_1>0){
            txt_count_btn1.setVisibility(View.VISIBLE);
            txt_count_btn1.setText(String.valueOf(count_1));
        }

        sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_clients " +
                "where dealer_id = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    sqlQuewy = "SELECT project_info, project_status "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where client_id = ? and project_status = 4";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                count_2 ++;
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_2>0){
            txt_count_btn2.setVisibility(View.VISIBLE);
            txt_count_btn2.setText(String.valueOf(count_2));
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            case R.id.btn_mounting_1:

                SharedPreferences SP = getSharedPreferences("activity_mounting_1", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", "true");
                ed.commit();
                Intent intent = new Intent(this, Activity_empty_mounting.class);
                startActivity(intent);
                break;

            case R.id.btn_mounting_2:
                SP = getSharedPreferences("activity_mounting_2", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "true");
                ed.commit();
                intent = new Intent(this, Activity_empty_mounting.class);
                startActivity(intent);
                break;

            case R.id.btn_brigade:
                intent = new Intent(this, Activity_spisok_brigade.class);
                startActivity(intent);
                break;
        }
    }
}
