package ru.ejevikaapp.authorization.Dealer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.Class.phone_edit;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_add_brigade extends AppCompatActivity implements View.OnClickListener {

    ListView list_mount;

    ArrayList<Frag_client_schedule_class> mount_mas = new ArrayList<>();

    int count = 0;

    EditText mail, phone, name, phone_mount, fio_mount;

    String user_id = "";

    int user_id_int = 0, max_id_brigade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brigade2);

        mail = (EditText) findViewById(R.id.mail);
        phone = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        phone_mount = (EditText) findViewById(R.id.phone_mount);
        fio_mount = (EditText) findViewById(R.id.fio_mount);

        Button btn_add_mount = (Button) findViewById(R.id.btn_add_mount);
        Button btn_add_brigade = (Button) findViewById(R.id.btn_add_brigade);

        btn_add_mount.setOnClickListener(this);
        btn_add_brigade.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");
        user_id_int = Integer.parseInt(user_id) * 1000000;

        list_mount = (ListView) findViewById(R.id.list_mount);

        list_mount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Frag_client_schedule_class selectedid = mount_mas.get(position);
                final String s_id = selectedid.getId_client();

                Log.d("mLog", s_id);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_brigade.this);
                builder.setTitle("Удалить выбранного монтажника?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        DBHelper dbHelper = new DBHelper(Activity_add_brigade.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, null, null, null, null, null, null);

                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, "_id = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, "id_mounter = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table = ?",
                                                            new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_mounters"});

                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        cursor.close();

                                        list();

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        max_id_brigade = 0;
        try {
            String sqlQuewy = "select MAX(_id) "
                    + "FROM rgzbn_users " +
                    "where _id>? and _id<?";
            ;
            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        max_id_brigade = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        max_id_brigade++;

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            max_id_brigade = user_id_int + 1;
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();
        FunDapter adapter = new FunDapter(this, mount_mas, R.layout.clients_item4, dict);
        list_mount.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_mount);

    }

    void list() {

        mount_mas.clear();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int max_id_mounter = 0;
        try {
            String sqlQuewy = "select id_mounter "
                    + "FROM rgzbn_gm_ceiling_mounters_map " +
                    "where id_brigade = ? ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(max_id_brigade)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        max_id_mounter = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));

                        sqlQuewy = "select * "
                                + "FROM rgzbn_gm_ceiling_mounters " +
                                "where _id = ? ";
                        Cursor cursor = db.rawQuery(sqlQuewy, new String[]{String.valueOf(max_id_mounter)});
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    String id = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                    String name = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                                    String phone = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));

                                    Log.d("mLog", "123  " + id + " " + name + " " + phone);
                                    Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                                            name, null, id, phone, null);
                                    mount_mas.add(fc);

                                } while (cursor.moveToNext());
                            }
                        }

                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            max_id_mounter = user_id_int + 1;

        }

        Log.d("mLog", String.valueOf(max_id_mounter));

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_date, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });

        dict.addStringField(R.id.c_fio, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });

        dict.addStringField(R.id.c_phone, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });

        FunDapter adapter = new FunDapter(this, mount_mas, R.layout.clients_item4, dict);

        list_mount.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_mount);

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
        switch (v.getId()) {
            case R.id.btn_add_mount:

                if (fio_mount.length() > 1 && phone_mount.length() > 1) {
                    count++;

                    DBHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    int max_id_mounter = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_mounters " +
                                "where _id>? and _id<?";

                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_mounter = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_mounter++;

                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e) {
                        max_id_mounter = user_id_int + 1;
                    }

                    Log.d("mLog", String.valueOf(max_id_mounter));

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_ID, max_id_mounter);
                    values.put(DBHelper.KEY_NAME, fio_mount.getText().toString());
                    values.put(DBHelper.KEY_PHONE, phone_mount.getText().toString());
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_MOUNTER, max_id_mounter);
                    values.put(DBHelper.KEY_ID_BRIGADE, max_id_brigade);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_mounter);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mounters");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    list();

                    fio_mount.setText("");
                    phone_mount.setText("");

                } else {

                    Toast toast = Toast.makeText(this,
                            "Вы что-то не ввели", Toast.LENGTH_SHORT);
                    toast.show();

                }

                break;

            case R.id.btn_add_brigade:

                if (mail.length() > 0 && phone.length() > 0 && name.length() > 0) {

                    SharedPreferences SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                    String dealer_id = SP.getString("", "");

                    DBHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    Log.d("mLog", String.valueOf(max_id_brigade));

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_ID, max_id_brigade);
                    values.put(DBHelper.KEY_NAME, name.getText().toString());
                    try {
                        phone_edit pe = new phone_edit();
                        values.put(DBHelper.KEY_USERNAME, pe.edit(phone.getText().toString()));
                    } catch (Exception e){
                    }
                    values.put(DBHelper.KEY_EMAIL, mail.getText().toString());
                    values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                    db.insert(DBHelper.TABLE_USERS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_USER_ID, max_id_brigade);
                    values.put(DBHelper.KEY_GROUP_ID, "11");
                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_brigade);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    Toast toast = Toast.makeText(this,
                            "Бригада создана", Toast.LENGTH_SHORT);
                    toast.show();

                    finish();
                } else {

                    Toast toast = Toast.makeText(this,
                            "Вы что-то не ввели", Toast.LENGTH_SHORT);
                    toast.show();

                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sqlQuewy = "select * "
                + "FROM rgzbn_users " +
                "where _id = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(max_id_brigade)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                } while (c.moveToNext());
            } else {

                sqlQuewy = "select id_mounter "
                        + "FROM rgzbn_gm_ceiling_mounters_map " +
                        "where id_brigade = ? ";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(max_id_brigade)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            int id_m = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));

                            db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS_MAP, "id_mounter = ?", new String[]{String.valueOf(id_m)});
                            db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, "_id = ?", new String[]{String.valueOf(id_m)});

                        } while (c.moveToNext());
                    }
                }

            }
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}