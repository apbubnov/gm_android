package ru.ejevikaapp.gm_android.Crew;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Dealer.MeasurerDay;
import ru.ejevikaapp.gm_android.R;

public class Activity_calendar extends AppCompatActivity implements View.OnClickListener {

    private List<Button> BtnList = new ArrayList<Button>();
    ImageButton calendar_minus, calendar_plus;

    int day_week, year, day, dday, month, max_day;

    TextView calendar_month;
    TableLayout tableLayout;
    String user_id;
    DBHelper dbHelper;
    ArrayList<Frag_client_schedule_class> installers_mas = new ArrayList<>();

    ListView list_installers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar_minus = (ImageButton) findViewById(R.id.calendar_minus);
        calendar_minus.setOnClickListener(this);
        calendar_plus = (ImageButton) findViewById(R.id.calendar_plus);
        calendar_plus.setOnClickListener(this);

        calendar_month = (TextView) findViewById(R.id.calendar_month);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        list_installers = (ListView) findViewById(R.id.list_installers);

        Calendar cl = Calendar.getInstance();
        day_week = cl.get(Calendar.DAY_OF_WEEK);
        year = cl.get(Calendar.YEAR);
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        installers_mas.clear();
        tableLayout.removeAllViews();

        if (getIntent().getStringExtra("id_brigade") == null) {
            user_id = getIntent().getStringExtra("id_measurer");
            TextView title = (TextView) findViewById(R.id.title);
            title.setVisibility(View.GONE);
            calendarMeasurer();

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mountLayout);
            linearLayout.setVisibility(View.GONE);
        } else {
            user_id = getIntent().getStringExtra("id_brigade");
            calendarMounter();

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.measurerLayout);
            linearLayout.setVisibility(View.GONE);
        }

        info();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void info() {
        TextView name_brigade = (TextView) findViewById(R.id.name_brigade);
        TextView number_brigade = (TextView) findViewById(R.id.number_brigade);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuewy = "select name, username, email "
                + "FROM rgzbn_users " +
                "where _id = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                name_brigade.setText(c.getString(c.getColumnIndex(c.getColumnName(0))));
                number_brigade.setText("Телефон: " + c.getString(c.getColumnIndex(c.getColumnName(1))) + "\n E-mail: "
                        + c.getString(c.getColumnIndex(c.getColumnName(2))));
            }
        }
        c.close();

        int count = 0;

        sqlQuewy = "select id_mounter "
                + "FROM rgzbn_gm_ceiling_mounters_map " +
                "where id_brigade = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id_mounter = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    sqlQuewy = "select name, phone "
                            + "FROM rgzbn_gm_ceiling_mounters " +
                            "where _id = ? ";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_mounter});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            count++;
                            String number = "Монтажник " + count + ":";
                            String name = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                            String phone = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));

                            Frag_client_schedule_class fc = new Frag_client_schedule_class(id_mounter,
                                    name, null, number, phone, null);
                            installers_mas.add(fc);
                        }
                    }
                    cc.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_time, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId_client();
            }
        });

        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        dict.addStringField(R.id.c_name, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });

        FunDapter adapter = new FunDapter(this, installers_mas, R.layout.select_work_l, dict);

        list_installers.setAdapter(adapter);

    }

    void calendarMounter() {

        dday = 0;
        max_day = 0;
        String month_str = "";

        if (month == 0) {
            max_day = 31;
            calendar_month.setText("Январь");
        } else if (month == 1) {
            if ((year % 4) == 0) {
                max_day = 29;
            } else {
                max_day = 28;
            }
            calendar_month.setText("Февраль");
        } else if (month == 2) {
            max_day = 31;
            calendar_month.setText("Март");
        } else if (month == 3) {
            max_day = 30;
            calendar_month.setText("Апрель");
        } else if (month == 4) {
            max_day = 31;
            calendar_month.setText("Май");
        } else if (month == 5) {
            max_day = 30;
            calendar_month.setText("Июнь");
        } else if (month == 6) {
            max_day = 31;
            calendar_month.setText("Июль");
        } else if (month == 7) {
            max_day = 31;
            calendar_month.setText("Август");
        } else if (month == 8) {
            max_day = 30;
            calendar_month.setText("Сентябрь");
        } else if (month == 9) {
            max_day = 31;
            calendar_month.setText("Октябрь");
        } else if (month == 10) {
            max_day = 30;
            calendar_month.setText("Ноябрь");
        } else if (month == 11) {
            max_day = 31;
            calendar_month.setText("Декабрь");
        }

        calendar_month.setText(calendar_month.getText().toString() + " " + year);

        JodaTimeAndroid.init(this);
        DateTime dt = new DateTime(year, month + 1, 1, 0, 0, 0, 0);
        String first_day = dt.toString("E");

        int first_day_int = 0;
        if (first_day.equals("пн")) {
            first_day_int = 0;
        } else if (first_day.equals("вт")) {
            first_day_int = 1;
        } else if (first_day.equals("ср")) {
            first_day_int = 2;
        } else if (first_day.equals("чт")) {
            first_day_int = 3;
        } else if (first_day.equals("пт")) {
            first_day_int = 4;
        } else if (first_day.equals("сб")) {
            first_day_int = 5;
        } else if (first_day.equals("вс")) {
            first_day_int = 6;
        }

        int ROWS = 6;
        int COLUMNS = 7;
        boolean flag = false;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for (int i = 0; i < ROWS; i++) {

            int count = 0;
            TableRow tableRow = new TableRow(this);

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 4f);

            for (int j = 0; j < COLUMNS; j++) {
                if ((j == first_day_int || flag) && dday < max_day) {

                    Button btn = new Button(this);
                    dday++;
                    String mount_day;

                    if (dday < 10 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-0" + dday;
                    } else if (dday < 10 && month > 9) {
                        mount_day = year + "-" + (month + 1) + "-0" + dday;
                    } else if (dday > 9 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-" + dday;
                    } else {
                        mount_day = year + "-" + (month + 1) + "-" + dday;
                    }

                    String sqlQuewy = "select _id, read_by_mounter "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? and (read_by_mounter=? or read_by_mounter=?)";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00", "0", "null"});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                btn.setBackgroundResource(R.drawable.calendar_btn_red);
                                btn.setTextColor(Color.WHITE);
                            } while (cc.moveToNext());
                        } else {
                            sqlQuewy = "select read_by_mounter "
                                    + "FROM rgzbn_gm_ceiling_projects " +
                                    "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? and read_by_mounter=? and project_status = ? ";
                            Cursor c1 = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00", "1", "16"});
                            if (c1 != null) {
                                if (c1.moveToFirst()) {
                                    do {
                                        btn.setBackgroundResource(R.drawable.calendar_btn_blue);
                                        btn.setTextColor(Color.WHITE);
                                    } while (c1.moveToNext());
                                } else {
                                    sqlQuewy = "select read_by_mounter "
                                            + "FROM rgzbn_gm_ceiling_projects " +
                                            "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? and read_by_mounter=? and project_status = ? ";
                                    Cursor c2 = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00", "1", "11"});
                                    if (c2 != null) {
                                        if (c2.moveToFirst()) {
                                            do {
                                                btn.setBackgroundResource(R.drawable.calendar_btn_green);
                                                btn.setTextColor(Color.BLACK);
                                            } while (c2.moveToNext());
                                        } else {
                                            sqlQuewy = "select read_by_mounter "
                                                    + "FROM rgzbn_gm_ceiling_projects " +
                                                    "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? and read_by_mounter=? and project_status = ? ";
                                            Cursor c3 = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00", "1", "17"});
                                            if (c3 != null) {
                                                if (c3.moveToFirst()) {
                                                    do {
                                                        btn.setBackgroundResource(R.drawable.calendar_btn_brown);
                                                        btn.setTextColor(Color.WHITE);
                                                    } while (c3.moveToNext());
                                                } else {
                                                    sqlQuewy = "select read_by_mounter "
                                                            + "FROM rgzbn_gm_ceiling_projects " +
                                                            "where project_mounter = ? and project_mounting_date > ? and project_mounting_date < ? " +
                                                            "and read_by_mounter=? and project_status > ? and project_status < ?";
                                                    Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00", "1", "4", "11"});
                                                    if (c != null) {
                                                        if (c.moveToFirst()) {
                                                            do {
                                                                btn.setBackgroundResource(R.drawable.calendar_btn_yellow);
                                                                btn.setTextColor(Color.BLACK);
                                                            } while (c.moveToNext());
                                                        } else {
                                                            btn.setBackgroundResource(R.drawable.calendar_btn);
                                                            btn.setTextColor(Color.BLACK);
                                                        }
                                                    }
                                                    c.close();
                                                }
                                            }
                                            c3.close();
                                        }
                                    }
                                    c2.close();
                                }
                            }
                            c1.close();
                        }
                    }
                    cc.close();

                    count++;
                    flag = true;
                    BtnList.add(btn);
                    btn.setId(dday - 1);
                    btn.setText(String.valueOf(dday));
                    btn.setLayoutParams(tableParams);
                    btn.setOnClickListener(getPhone);
                    tableRow.addView(btn, j);
                } else {
                    Button btn = new Button(this);
                    btn.setText("");
                    btn.setBackgroundResource(R.drawable.calendar_other_month);
                    btn.setLayoutParams(tableParams);
                    tableRow.addView(btn, j);
                }

            }
            tableLayout.addView(tableRow, i);

        }
    }

    void calendarMeasurer() {

        dday = 0;
        max_day = 0;
        String month_str = "";

        if (month == 0) {
            max_day = 31;
            calendar_month.setText("Январь");
        } else if (month == 1) {
            if ((year % 4) == 0) {
                max_day = 29;
            } else {
                max_day = 28;
            }
            calendar_month.setText("Февраль");
        } else if (month == 2) {
            max_day = 31;
            calendar_month.setText("Март");
        } else if (month == 3) {
            max_day = 30;
            calendar_month.setText("Апрель");
        } else if (month == 4) {
            max_day = 31;
            calendar_month.setText("Май");
        } else if (month == 5) {
            max_day = 30;
            calendar_month.setText("Июнь");
        } else if (month == 6) {
            max_day = 31;
            calendar_month.setText("Июль");
        } else if (month == 7) {
            max_day = 31;
            calendar_month.setText("Август");
        } else if (month == 8) {
            max_day = 30;
            calendar_month.setText("Сентябрь");
        } else if (month == 9) {
            max_day = 31;
            calendar_month.setText("Октябрь");
        } else if (month == 10) {
            max_day = 30;
            calendar_month.setText("Ноябрь");
        } else if (month == 11) {
            max_day = 31;
            calendar_month.setText("Декабрь");
        }

        calendar_month.setText(calendar_month.getText().toString() + " " + year);

        JodaTimeAndroid.init(this);
        DateTime dt = new DateTime(year, month + 1, 1, 0, 0, 0, 0);
        String first_day = dt.toString("E");

        int first_day_int = 0;
        if (first_day.equals("пн")) {
            first_day_int = 0;
        } else if (first_day.equals("вт")) {
            first_day_int = 1;
        } else if (first_day.equals("ср")) {
            first_day_int = 2;
        } else if (first_day.equals("чт")) {
            first_day_int = 3;
        } else if (first_day.equals("пт")) {
            first_day_int = 4;
        } else if (first_day.equals("сб")) {
            first_day_int = 5;
        } else if (first_day.equals("вс")) {
            first_day_int = 6;
        }

        int ROWS = 6;
        int COLUMNS = 7;
        boolean flag = false;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for (int i = 0; i < ROWS; i++) {

            int count = 0;
            TableRow tableRow = new TableRow(this);

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 4f);

            for (int j = 0; j < COLUMNS; j++) {
                if ((j == first_day_int || flag) && dday < max_day) {

                    Button btn = new Button(this);
                    dday++;
                    String mount_day;

                    if (dday < 10 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-0" + dday;
                    } else if (dday < 10 && month > 9) {
                        mount_day = year + "-" + (month + 1) + "-0" + dday;
                    } else if (dday > 9 && month < 10) {
                        mount_day = year + "-0" + (month + 1) + "-" + dday;
                    } else {
                        mount_day = year + "-" + (month + 1) + "-" + dday;
                    }

                    String sqlQuewy = "select _id, read_by_mounter "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where project_calculator = ? and project_calculation_date > ? and project_calculation_date < ?";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00"});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                btn.setBackgroundResource(R.drawable.calendar_btn_blue);
                                btn.setTextColor(Color.BLACK);
                            } while (cc.moveToNext());
                        } else {
                            btn.setBackgroundResource(R.drawable.calendar_btn);
                            btn.setTextColor(Color.BLACK);
                        }
                    }
                    cc.close();

                    count++;
                    flag = true;
                    BtnList.add(btn);
                    btn.setId(dday - 1);
                    btn.setText(String.valueOf(dday));
                    btn.setLayoutParams(tableParams);
                    btn.setOnClickListener(getPhone);
                    tableRow.addView(btn, j);
                } else {
                    Button btn = new Button(this);
                    btn.setText("");
                    btn.setBackgroundResource(R.drawable.calendar_other_month);
                    btn.setLayoutParams(tableParams);
                    tableRow.addView(btn, j);
                }

            }
            tableLayout.addView(tableRow, i);

        }
    }

    View.OnClickListener getPhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            Button btnn = BtnList.get(editId);
            int day = Integer.parseInt(btnn.getText().toString());
            String mount_day;

            if (day < 10 && month < 10) {
                mount_day = year + "-0" + (month + 1) + "-0" + day;
            } else if (day < 10 && month > 9) {
                mount_day = year + "-" + (month + 1) + "-0" + day;
            } else if (day > 9 && month < 10) {
                mount_day = year + "-0" + (month + 1) + "-" + day;
            } else {
                mount_day = year + "-" + (month + 1) + "-" + day;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sqlQuewy = "select * "
                    + "FROM rgzbn_gm_ceiling_projects " +
                    "where project_calculator = ? and project_calculation_date > ? and project_calculation_date < ? ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, mount_day + " 08:00:00", mount_day + " 22:00:00"});
            if (c != null) {
                if (c.moveToFirst()) {
                    Intent intent = new Intent(Activity_calendar.this, MeasurerDay.class);
                    intent.putExtra("day_mount", mount_day);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "В данный момент на этот день замеров нет", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            c.close();

        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.calendar_minus:
                month--;
                if (month < 0) {
                    month = 11;
                    year--;
                }
                tableLayout.removeAllViews();
                onResume();
                break;

            case R.id.calendar_plus:
                month++;
                if (month == 12) {
                    month = 0;
                    year++;
                }
                tableLayout.removeAllViews();
                onResume();
                break;
        }
    }
}
