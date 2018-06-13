package ru.ejevikaapp.gm_android.Dealer;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Crew.Activity_calendar;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;

public class ListOfMeasurers extends AppCompatActivity implements View.OnClickListener {


    DBHelper dbHelper;
    ListView list_measurer;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID = "", user_id;
    View view;

    Button btn_add_measurer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_measurers);

        btn_add_measurer = (Button) findViewById(R.id.btn_add_measurer);
        btn_add_measurer.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        client_mas.clear();
        list_measurer = (ListView) findViewById(R.id.list_measurer);
        clients();
    }

    void clients() {

        SP = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id = SP.getString("", "");

        dbHelper = new DBHelper(this);

        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList brigade = new ArrayList();

        int count = 0;
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
                            "where user_id = ? and (group_id = 22 or group_id = 21)";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{br});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            count++;
                            brigade.add(br);
                        }
                    }
                    cc.close();

                } while (c.moveToNext());
            }
        }
        c.close();

        if (count == 0) {
            brigade.add(user_id);
        }

        for (int g = 0; g < brigade.size(); g++) {
            String name_brigade = "";
            String name_brigade_full = "";

            sqlQuewy = "SELECT name "
                    + "FROM rgzbn_users " +
                    "where _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(brigade.get(g))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        name_brigade_full = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            Frag_client_schedule_class fc = new Frag_client_schedule_class(String.valueOf(brigade.get(g)),
                    name_brigade_full, null, null, null, null);
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
        list_measurer.setAdapter(adapter);

        list_measurer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                Intent intent = new Intent(ListOfMeasurers.this, Activity_calendar.class);
                intent.putExtra("id_measurer", p_id);
                startActivity(intent);
            }
        });

        list_measurer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {        // УДАЛЕНИЕ

                final SQLiteDatabase db = dbHelper.getReadableDatabase();
                Frag_client_schedule_class selectedid = client_mas.get(position);
                final String p_id = selectedid.getId();
                final String p_name = selectedid.getFio();

                AlertDialog.Builder builder = new AlertDialog.Builder(ListOfMeasurers.this);
                builder.setTitle("Удалить замерщика " + p_name + " ?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // Доделать удаление

                                        ContentValues values = new ContentValues();
                                        values.put(DBHelper.KEY_ID_OLD, p_id);
                                        values.put(DBHelper.KEY_ID_NEW, "0");
                                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                                        values.put(DBHelper.KEY_SYNC, "0");
                                        values.put(DBHelper.KEY_TYPE, "delete");
                                        values.put(DBHelper.KEY_STATUS, "1");
                                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                        db.delete(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, "user_id = ?", new String[]{String.valueOf(p_id)});
                                        db.delete(DBHelper.TABLE_USERS, "_id = ?", new String[]{String.valueOf(p_id)});

                                        onResume();
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

                return false;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_measurer:

                SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
                final String user_id = SP.getString("", "");
                final int user_id_int = Integer.parseInt(user_id) * 100000;

                final Context context = this;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.layout_add_gager, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                final EditText nameGager = (EditText) promptsView.findViewById(R.id.nameGager);
                final EditText phoneGager = (EditText) promptsView.findViewById(R.id.phoneGager);
                final EditText mailGager = (EditText) promptsView.findViewById(R.id.mailGager);

                Button btnAddGager = (Button) promptsView.findViewById(R.id.btnAddGager);
                btnAddGager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_nameGager = nameGager.getText().toString();
                        String str_phoneGager = phoneGager.getText().toString();
                        String str_mailGager = mailGager.getText().toString();

                        if (HelperClass.validateMail(str_mailGager)) {
                            if (str_phoneGager.length() == 11) {
                                if (str_nameGager.length() > 0) {

                                    DBHelper dbHelper = new DBHelper(ListOfMeasurers.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    int max_id_gager = 0;
                                    try {
                                        String sqlQuewy = "select MAX(_id) "
                                                + "FROM rgzbn_users " +
                                                "where _id>? and _id<?";

                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int),
                                                String.valueOf(user_id_int + 999999)});
                                        if (c != null) {
                                            if (c.moveToFirst()) {
                                                do {
                                                    max_id_gager = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                    max_id_gager++;

                                                } while (c.moveToNext());
                                            }
                                        }
                                    } catch (Exception e) {
                                        max_id_gager = user_id_int + 1;
                                    }

                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_ID, max_id_gager);
                                    values.put(DBHelper.KEY_NAME, str_nameGager);
                                    values.put(DBHelper.KEY_DEALER_ID, user_id);
                                    values.put(DBHelper.KEY_EMAIL, str_mailGager);
                                    values.put(DBHelper.KEY_USERNAME, str_phoneGager);
                                    db.insert(DBHelper.TABLE_USERS, null, values);

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID, max_id_gager);
                                    values.put(DBHelper.KEY_USER_ID, max_id_gager);
                                    values.put(DBHelper.KEY_GROUP_ID, "21");
                                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, max_id_gager);
                                    values.put(DBHelper.KEY_ID_NEW, 0);
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, max_id_gager);
                                    values.put(DBHelper.KEY_ID_NEW, 0);
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_user_usergroup_map");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                    startService(new Intent(ListOfMeasurers.this, Service_Sync.class));

                                    Toast.makeText(getApplicationContext(), "Замерщик добавлен", Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();

                                    onResume();

                                } else {
                                    Toast.makeText(getApplicationContext(), "проверьте введенное имя", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "проверьте введенный телефон", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "проверьте введенную почту", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }
}