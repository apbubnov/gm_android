package ru.ejevikaapp.gm_android.Dealer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class ActivitySumUsers extends AppCompatActivity {

    DBHelper dbHelper;
    ListView list_clients;
    Button show, show_all;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String user_id = "", first_date = "2000-01-01 00:00:00", second_date = "2050-01-01 23:00:59";

    Calendar dateAndTime = Calendar.getInstance();

    TextView first_calendar;
    TextView second_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_users);

        SP = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        list_clients = (ListView) findViewById(R.id.list_client);

        show_all = (Button) findViewById(R.id.show_all);
        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_date = "2000-01-01 00:00:00";
                second_date = "2050-01-01 23:00:59";
                recoil_map_project();
            }
        });

        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoil_map_project();
            }
        });

        first_calendar = (TextView) findViewById(R.id.first_calendar);
        second_calendar = (TextView) findViewById(R.id.second_calendar);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        float sum = 0;

        String sqlQuewy = "SELECT sum "
                + "FROM rgzbn_gm_ceiling_recoil_map_project" +
                " WHERE recoil_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    float project_sum = c.getFloat(c.getColumnIndex(c.getColumnName(0)));
                    sum += project_sum;
                } while (c.moveToNext());
            }
        }
        c.close();

        TextView sumTitle = (TextView) findViewById(R.id.sumTitle);
        sumTitle.setText("Детализация счета\n" + sum);

        recoil_map_project();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void recoil_map_project() {
        client_mas.clear();

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Log.d("mLog", "first_date " + first_date);
        Log.d("mLog", "second_date " + second_date);

        float sum = 0;
        String sqlQuewy = "SELECT project_id, sum, date_time, comment "
                + "FROM rgzbn_gm_ceiling_recoil_map_project" +
                " WHERE recoil_id = ? and date_time between '" + first_date + "' AND '" + second_date + "' " +
                "order by date_time desc";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String project_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    float project_sum = c.getFloat(c.getColumnIndex(c.getColumnName(1)));
                    String date_time = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    String comment = c.getString(c.getColumnIndex(c.getColumnName(3)));

                    sum += project_sum;

                    Frag_client_schedule_class fc = new Frag_client_schedule_class(project_id, String.valueOf(project_sum),
                            date_time, comment, null, null);
                    client_mas.add(fc);

                } while (c.moveToNext());
            }
        }
        c.close();

        Frag_client_schedule_class fc = new Frag_client_schedule_class("", "Итого: " + String.valueOf(sum),
                null, null, null, null);
        client_mas.add(fc);

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
                return nc.getId_client();
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


                Intent intent = new Intent(ActivitySumUsers.this, Activity_inform_zapysch.class);
                startActivity(intent);
            }
        });
    }

    // отображаем диалоговое окно для выбора даты
    public void firstSetDate(View v) {
        new DatePickerDialog(ActivitySumUsers.this, first,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener first = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            first_calendar.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
            String day = String.valueOf(dayOfMonth);
            String month = String.valueOf(monthOfYear + 1);

            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            if (monthOfYear + 1 < 10) {
                month = "0" + month;
            }
            first_date = year + "-" + month + "-" + day + " 00:00:00";
        }
    };

    private void setInitialDateTimeFirst() {
        first_calendar.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void SecondSetDate(View v) {
        new DatePickerDialog(ActivitySumUsers.this, second,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener second = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            second_calendar.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
            String day = String.valueOf(dayOfMonth);
            String month = String.valueOf(monthOfYear + 1);

            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            if (monthOfYear + 1 < 10) {
                month = "0" + month;
            }
            second_date = year + "-" + month + "-" + day + " 23:00:59";

        }
    };

    private void setInitialDateTimeSecond() {
        second_calendar.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
