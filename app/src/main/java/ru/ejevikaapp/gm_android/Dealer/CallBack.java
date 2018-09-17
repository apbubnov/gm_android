package ru.ejevikaapp.gm_android.Dealer;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
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

import javax.security.auth.callback.Callback;

import ru.ejevikaapp.gm_android.Activity_inform_zapysch;
import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class CallBack extends AppCompatActivity {

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
        setContentView(R.layout.activity_call_back);

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

        recoil_map_project();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void recoil_map_project() {
        client_mas.clear();

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList client = new ArrayList();
        String sqlQuewy = "SELECT client_id, date_time "
                + "FROM rgzbn_gm_ceiling_callback " +
                "order by date_time DESC";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String client_id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    sqlQuewy = "SELECT _id "
                            + "FROM rgzbn_gm_ceiling_clients" +
                            " WHERE dealer_id = ? and _id = ?";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{user_id, client_id});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            client.add(client_id);
                        }
                    }
                    cc.close();

                    String date_time = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    String now_date = HelperClass.now_date(CallBack.this);
                    now_date = now_date.substring(0, now_date.length());

                    Log.d("mLog", "one " + date_time);
                    Log.d("mLog", "two " + now_date);

                    Date one = null;
                    Date two = null;

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    try {
                        one = format.parse(date_time);
                        two = format.parse(now_date);
                    } catch (Exception e) {
                    }

                    Log.d("mLog", "one " + one);
                    Log.d("mLog", "two " + two);

                    long difference = one.getMinutes() - two.getMinutes();

                    if (difference == 7){

                        Intent resultIntent = new Intent(CallBack.this, CallBack.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(CallBack.this, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(CallBack.this)
                                        .setAutoCancel(true)
                                        .setTicker("CALL")
                                        .setWhen(System.currentTimeMillis())
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setSmallIcon(R.raw.gm_ico2)
                                        .setContentIntent(resultPendingIntent)
                                        .setContentTitle("ГМ");
                        Notification notification = builder.build();

                        NotificationManager notificationManager = (NotificationManager) CallBack.this
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, notification);
                    }

                    Log.d("mLog", String.valueOf(difference));

                } while (c.moveToNext());
            }
        }
        c.close();

        for (int i = 0; client.size() > i; i++) {
            sqlQuewy = "SELECT client_id, date_time, comment "
                    + "FROM rgzbn_gm_ceiling_callback" +
                    " WHERE client_id = ? and date_time between '" + first_date + "' AND '" + second_date + "' ";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(client.get(i))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String client_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        String date_time = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        String comment = c.getString(c.getColumnIndex(c.getColumnName(2)));

                        String client_name = "";
                        sqlQuewy = "SELECT client_name "
                                + "FROM rgzbn_gm_ceiling_clients" +
                                " WHERE _id = ? ";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{client_id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    client_name = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();

                        Frag_client_schedule_class fc = new Frag_client_schedule_class(client_id, client_name,
                                date_time, comment, null, null);
                        client_mas.add(fc);
                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();
        dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.tv_vid, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId_client();
            }
        });

        FunDapter adapter = new FunDapter(this, client_mas, R.layout.svet_list, dict);
        list_clients.setAdapter(adapter);

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                String p_id = selectedid.getId();

                SP = getSharedPreferences("activity_client", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(p_id));
                ed.commit();

                Intent intent = new Intent(CallBack.this, Activity_for_spisok.class);
                startActivity(intent);
            }
        });
    }

    // отображаем диалоговое окно для выбора даты
    public void firstSetDate(View v) {
        new DatePickerDialog(CallBack.this, first,
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
        new DatePickerDialog(CallBack.this, second,
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
