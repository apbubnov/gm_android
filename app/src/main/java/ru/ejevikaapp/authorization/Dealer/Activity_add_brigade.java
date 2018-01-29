package ru.ejevikaapp.authorization.Dealer;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.IntegerRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_add_brigade extends AppCompatActivity implements View.OnClickListener {

    ListView list_mount;

    ArrayList<Frag_client_schedule_class> mount_mas = new ArrayList<>();

    int count = 0;

    EditText mail, phone, name, phone_mount, fio_mount;
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

       //list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener(){
       //    @Override
       //    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

       //        SQLiteDatabase db = dbHelper.getReadableDatabase();
       //        Frag_client_schedule_class selectedid = client_mas.get(position);
       //        String p_id = selectedid.getId();

       //        SP = getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
       //        SharedPreferences.Editor ed = SP.edit();
       //        ed.putString("", String.valueOf(p_id));
       //        ed.commit();

       //        String c_id = "";

       //        String sqlQuewy = "SELECT client_id "
       //                + "FROM rgzbn_gm_ceiling_projects" +
       //                " WHERE _id = ?";

       //        Cursor cursor = db.rawQuery(sqlQuewy, new String[]{p_id});
       //        if (cursor != null) {
       //            if (cursor.moveToFirst()) {
       //                do {
       //                    c_id = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
       //                } while (cursor.moveToNext());
       //            }
       //        }
       //        cursor.close();

       //        SP = getActivity().getSharedPreferences("id_client_spisok", MODE_PRIVATE);
       //        ed = SP.edit();
       //        ed.putString("", String.valueOf(c_id));
       //        ed.commit();

       //    }
       //});
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

                    Frag_client_schedule_class fc = new Frag_client_schedule_class(String.valueOf(count),
                            fio_mount.getText().toString(),
                            null, null, phone_mount.getText().toString());
                    mount_mas.add(fc);

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
                            return nc.getPhone();
                        }
                    });

                    FunDapter adapter = new FunDapter(this, mount_mas, R.layout.clients_item4, dict);

                    list_mount = (ListView) findViewById(R.id.list_mount);
                    list_mount.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(list_mount);

                    fio_mount.setText("");
                    phone_mount.setText("");

                } else {

                    Toast toast = Toast.makeText(this,
                            "Вы что-то не ввели", Toast.LENGTH_SHORT);
                    toast.show();

                }

                break;
            case R.id.btn_add_brigade:
                if (mail.length() > 0 || phone.length() > 0 || name.length() > 0) {

                    SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
                    String user_id = SP.getString("", "");
                    int user_id_int = Integer.parseInt(user_id) * 1000000;


                    SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
                    String dealer_id = SP.getString("", "");

                    DBHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    int max_id_brigade = 0;
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

                    Log.d("mLog", String.valueOf(max_id_brigade));

                    //ContentValues values = new ContentValues();
                    //values.put(DBHelper.KEY_ID, max_id_brigade);
                    //values.put(DBHelper.KEY_NAME, name.getText().toString());
                    //values.put(DBHelper.KEY_USERNAME, phone.getText().toString());
                    //values.put(DBHelper.KEY_EMAIL, mail.getText().toString());
                    //values.put(DBHelper.KEY_DEALER_ID, dealer_id);
                    //db.insert(DBHelper.TABLE_USERS, null, values);

                    //values = new ContentValues();
                    //values.put(DBHelper.KEY_ID_OLD, max_id_brigade);
                    //values.put(DBHelper.KEY_ID_NEW, "0");
                    //values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                    //values.put(DBHelper.KEY_SYNC, "0");
                    //values.put(DBHelper.KEY_TYPE, "send");
                    //values.put(DBHelper.KEY_STATUS, "1");
                    //db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    int max_id_mounter = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_mounters " +
                                "where _id>? and _id<?";
                        ;
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

                    for (int m = 0; mount_mas.size() > m; m++) {

                        Frag_client_schedule_class selectedid = mount_mas.get(m);
                        String fio = selectedid.getFio();
                        String phone = selectedid.getPhone();

                        Log.d("mLog", fio + " " + phone);

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_ID, max_id_mounter + m);
                        values.put(DBHelper.KEY_NAME, fio);
                        values.put(DBHelper.KEY_PHONE, phone);
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNTERS, null, values);

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID_OLD, max_id_mounter + m);
                        values.put(DBHelper.KEY_ID_NEW, "0");
                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mounters");
                        values.put(DBHelper.KEY_SYNC, "0");
                        values.put(DBHelper.KEY_TYPE, "send");
                        values.put(DBHelper.KEY_STATUS, "1");
                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                    }

                    finish();

                }

                break;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}