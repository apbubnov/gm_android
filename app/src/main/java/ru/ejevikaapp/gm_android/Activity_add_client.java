package ru.ejevikaapp.gm_android;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.ejevikaapp.gm_android.Class.HelperClass;


public class Activity_add_client extends AppCompatActivity implements View.OnClickListener{

    EditText name, phone;
    Button btn_back, btn_save;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        btn_back = (Button)findViewById(R.id.back);
        btn_save = (Button)findViewById(R.id.save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        dbHelper = new DBHelper(this);

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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.save:

                if (name.getText().length() > 0 && phone.length() > 0)
                {
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    String fio = name.getText().toString().trim();
                    String number = phone.getText().toString().trim();

                    Calendar date_cr = new GregorianCalendar();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(date_cr.getTime());

                    SharedPreferences SP = this.getSharedPreferences("user_id", MODE_PRIVATE);
                    String user_id = SP.getString("", "");
                    int user_id_int = Integer.parseInt(user_id);

                    int max_id = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_clients " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e) {
                        max_id = user_id_int * 100000 + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id);
                    values.put(DBHelper.KEY_CLIENT_NAME, fio);
                    values.put(DBHelper.KEY_CLIENT_DATA_ID, "");
                    values.put(DBHelper.KEY_TYPE_ID, "1");
                    values.put(DBHelper.KEY_DEALER_ID, user_id);
                    values.put(DBHelper.KEY_MANAGER_ID, "");
                    values.put(DBHelper.KEY_CREATED, date);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_clients_contacts " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int * 100000), String.valueOf(user_id_int * 100000 + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e) {
                        max_id_contac = user_id_int * 100000 + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CLIENT_ID, max_id);
                    try {
                        values.put(DBHelper.KEY_PHONE, HelperClass.phone_edit(number));
                    } catch (Exception e) {
                    }
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS_CONTACTS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients_contacts");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Клиент добавлен", Toast.LENGTH_SHORT);
                    toast.show();

                    startService(new Intent(this, Service_Sync.class));

                    finish();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "У Вас что-то неправильно заполнено", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
