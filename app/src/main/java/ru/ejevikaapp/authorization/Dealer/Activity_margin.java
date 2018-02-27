package ru.ejevikaapp.authorization.Dealer;

import android.content.ContentValues;
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

import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_margin extends AppCompatActivity implements View.OnClickListener {

    EditText distance, transport, mp43, mp22, mp23, mp24, mp25, mp26, mp27, mp33, mp30, mp34, mp36, mp37, mp38, mp40, mp41, mp42,
            canvases_margin, components_margin, mounting_margin, mp1, mp31, mp32, mp2, mp4, mp5, mp6, mp7, mp3, mp8, mp9, mp10, mp11,
            mp12, mp13, mp14, mp15, mp16, mp17, mp18, mp19;

    String str_distance, str_transport, str_mp43, str_mp22, str_mp23, str_mp24, str_mp25, str_mp26, str_mp27, str_mp33, str_mp30, str_mp34,
            str_mp36, str_mp37, str_mp38, str_mp40,str_mp41, str_mp42, str_canvases_margin, str_components_margin, str_mounting_margin,
            str_mp1, str_mp31, str_mp32, str_mp2, str_mp4, str_mp5, str_mp6, str_mp7, str_mp3, str_mp8, str_mp9, str_mp10, str_mp11, str_mp12,
            str_mp13, str_mp14, str_mp15, str_mp16, str_mp17, str_mp18, str_mp19;

    Button save_margin;

    String user_id, id_dealer_info, id_mount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_margin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");

        canvases_margin = (EditText) findViewById(R.id.canvases_margin);
        components_margin = (EditText) findViewById(R.id.components_margin);
        mounting_margin = (EditText) findViewById(R.id.mounting_margin);
        mp1 = (EditText) findViewById(R.id.mp1);
        mp2 = (EditText) findViewById(R.id.mp2);
        mp3 = (EditText) findViewById(R.id.mp3);
        mp4 = (EditText) findViewById(R.id.mp4);
        mp5 = (EditText) findViewById(R.id.mp5);
        mp6 = (EditText) findViewById(R.id.mp6);
        mp7 = (EditText) findViewById(R.id.mp7);
        mp8 = (EditText) findViewById(R.id.mp8);
        mp9 = (EditText) findViewById(R.id.mp9);
        mp10 = (EditText) findViewById(R.id.mp10);
        mp11 = (EditText) findViewById(R.id.mp11);
        mp12 = (EditText) findViewById(R.id.mp12);
        mp13 = (EditText) findViewById(R.id.mp13);
        mp14 = (EditText) findViewById(R.id.mp14);
        mp15 = (EditText) findViewById(R.id.mp15);
        mp16 = (EditText) findViewById(R.id.mp16);
        mp17 = (EditText) findViewById(R.id.mp17);
        mp18 = (EditText) findViewById(R.id.mp18);
        mp19 = (EditText) findViewById(R.id.mp19);
        mp22 = (EditText) findViewById(R.id.mp22);
        mp23 = (EditText) findViewById(R.id.mp23);
        mp24 = (EditText) findViewById(R.id.mp24);
        mp25 = (EditText) findViewById(R.id.mp25);
        mp26 = (EditText) findViewById(R.id.mp26);
        mp27 = (EditText) findViewById(R.id.mp27);
        mp30 = (EditText) findViewById(R.id.mp30);
        mp31 = (EditText) findViewById(R.id.mp31);
        mp32 = (EditText) findViewById(R.id.mp32);
        mp33 = (EditText) findViewById(R.id.mp33);
        mp34 = (EditText) findViewById(R.id.mp34);
        mp36 = (EditText) findViewById(R.id.mp36);
        mp37 = (EditText) findViewById(R.id.mp37);
        mp38 = (EditText) findViewById(R.id.mp38);
        mp40 = (EditText) findViewById(R.id.mp40);
        mp41 = (EditText) findViewById(R.id.mp41);
        mp42 = (EditText) findViewById(R.id.mp42);
        mp43 = (EditText) findViewById(R.id.mp43);
        transport = (EditText) findViewById(R.id.transport);
        distance = (EditText) findViewById(R.id.distance);

        save_margin = (Button) findViewById(R.id.save_margin);
        save_margin.setOnClickListener(this);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        String sqlQuewy = "SELECT mp1, mp2, mp3, mp4, mp5, mp6, mp7, mp8, mp9, mp10, mp11, mp12, mp13, mp14, mp15, mp16, mp17, " +
                "mp18, mp19, mp22, mp23, mp24, mp25, mp26, mp27, mp30, mp31, mp32, mp33, mp34, mp36, mp37, mp38, mp40, mp41, mp42, mp43, transport, distance, _id "
                + "FROM rgzbn_gm_ceiling_mount" +
                " WHERE user_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    str_mp1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    str_mp2 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    str_mp3 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    str_mp4 = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    str_mp5 = c.getString(c.getColumnIndex(c.getColumnName(4)));
                    str_mp6 = c.getString(c.getColumnIndex(c.getColumnName(5)));
                    str_mp7 = c.getString(c.getColumnIndex(c.getColumnName(6)));
                    str_mp8 = c.getString(c.getColumnIndex(c.getColumnName(7)));
                    str_mp9 = c.getString(c.getColumnIndex(c.getColumnName(8)));
                    str_mp10 = c.getString(c.getColumnIndex(c.getColumnName(9)));
                    str_mp11 = c.getString(c.getColumnIndex(c.getColumnName(10)));
                    str_mp12 = c.getString(c.getColumnIndex(c.getColumnName(11)));
                    str_mp13 = c.getString(c.getColumnIndex(c.getColumnName(12)));
                    str_mp14 = c.getString(c.getColumnIndex(c.getColumnName(13)));
                    str_mp15 = c.getString(c.getColumnIndex(c.getColumnName(14)));
                    str_mp16 = c.getString(c.getColumnIndex(c.getColumnName(15)));
                    str_mp17 = c.getString(c.getColumnIndex(c.getColumnName(16)));
                    str_mp18 = c.getString(c.getColumnIndex(c.getColumnName(17)));
                    str_mp19 = c.getString(c.getColumnIndex(c.getColumnName(18)));
                    str_mp22 = c.getString(c.getColumnIndex(c.getColumnName(19)));
                    str_mp23 = c.getString(c.getColumnIndex(c.getColumnName(20)));
                    str_mp24 = c.getString(c.getColumnIndex(c.getColumnName(21)));
                    str_mp25 = c.getString(c.getColumnIndex(c.getColumnName(22)));
                    str_mp26 = c.getString(c.getColumnIndex(c.getColumnName(23)));
                    str_mp27 = c.getString(c.getColumnIndex(c.getColumnName(24)));
                    str_mp30 = c.getString(c.getColumnIndex(c.getColumnName(25)));
                    str_mp31 = c.getString(c.getColumnIndex(c.getColumnName(26)));
                    str_mp32 = c.getString(c.getColumnIndex(c.getColumnName(27)));
                    str_mp33 = c.getString(c.getColumnIndex(c.getColumnName(28)));
                    str_mp34 = c.getString(c.getColumnIndex(c.getColumnName(29)));
                    str_mp36 = c.getString(c.getColumnIndex(c.getColumnName(30)));
                    str_mp37 = c.getString(c.getColumnIndex(c.getColumnName(31)));
                    str_mp38 = c.getString(c.getColumnIndex(c.getColumnName(32)));
                    str_mp40 = c.getString(c.getColumnIndex(c.getColumnName(33)));
                    str_mp41 = c.getString(c.getColumnIndex(c.getColumnName(34)));
                    str_mp42 = c.getString(c.getColumnIndex(c.getColumnName(35)));
                    str_mp43 = c.getString(c.getColumnIndex(c.getColumnName(36)));
                    str_transport = c.getString(c.getColumnIndex(c.getColumnName(37)));
                    str_distance = c.getString(c.getColumnIndex(c.getColumnName(38)));
                    id_mount = c.getString(c.getColumnIndex(c.getColumnName(39)));
                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, _id "
                + "FROM rgzbn_gm_ceiling_dealer_info" +
                " WHERE dealer_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    str_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    str_components_margin = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    str_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    id_dealer_info = c.getString(c.getColumnIndex(c.getColumnName(3)));
                } while (c.moveToNext());
            }
        }
        c.close();

        canvases_margin.setText(str_canvases_margin);
        components_margin.setText(str_components_margin);
        mounting_margin.setText(str_mounting_margin);
        mp1.setText(str_mp1);
        mp2.setText(str_mp2);
        mp3.setText(str_mp3);
        mp4.setText(str_mp4);
        mp5.setText(str_mp5);
        mp6.setText(str_mp6);
        mp7.setText(str_mp7);
        mp8.setText(str_mp8);
        mp9.setText(str_mp9);
        mp10.setText(str_mp10);
        mp11.setText(str_mp11);
        mp12.setText(str_mp12);
        mp13.setText(str_mp13);
        mp14.setText(str_mp14);
        mp15.setText(str_mp15);
        mp16.setText(str_mp16);
        mp17.setText(str_mp17);
        mp18.setText(str_mp18);
        mp19.setText(str_mp19);
        mp22.setText(str_mp22);
        mp23.setText(str_mp23);
        mp24.setText(str_mp24);
        mp25.setText(str_mp25);
        mp26.setText(str_mp26);
        mp27.setText(str_mp27);
        mp30.setText(str_mp30);
        mp31.setText(str_mp31);
        mp32.setText(str_mp32);
        mp33.setText(str_mp33);
        mp34.setText(str_mp34);
        mp36.setText(str_mp36);
        mp37.setText(str_mp37);
        mp38.setText(str_mp38);
        mp40.setText(str_mp40);
        mp41.setText(str_mp41);
        mp42.setText(str_mp42);
        mp43.setText(str_mp43);
        transport.setText(str_transport);
        distance.setText(str_distance);

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
            case R.id.save_margin:
                if (canvases_margin.getText().toString().equals("") && components_margin.getText().toString().equals("") && mounting_margin.getText().toString().equals("") &&
                mp1.getText().toString().equals("") && mp2.getText().toString().equals("") && mp3.getText().toString().equals("") && mp4.getText().toString().equals("") &&
                mp5.getText().toString().equals("") && mp6.getText().toString().equals("") && mp7.getText().toString().equals("") && mp8.getText().toString().equals("") &&
                mp9.getText().toString().equals("") && mp10.getText().toString().equals("") && mp11.getText().toString().equals("") && mp12.getText().toString().equals("") &&
                mp13.getText().toString().equals("") && mp14.getText().toString().equals("") && mp15.getText().toString().equals("") && mp16.getText().toString().equals("") &&
                mp17.getText().toString().equals("") && mp18.getText().toString().equals("") && mp19.getText().toString().equals("") && mp22.getText().toString().equals("") &&
                mp23.getText().toString().equals("") && mp24.getText().toString().equals("") && mp25.getText().toString().equals("") && mp26.getText().toString().equals("") &&
                mp27.getText().toString().equals("") && mp30.getText().toString().equals("") && mp31.getText().toString().equals("") && mp32.getText().toString().equals("") &&
                mp33.getText().toString().equals("") && mp34.getText().toString().equals("") && mp36.getText().toString().equals("") && mp37.getText().toString().equals("") &&
                mp38.getText().toString().equals("") && mp40.getText().toString().equals("") && mp41.getText().toString().equals("") && mp42.getText().toString().equals("") &&
                mp43.getText().toString().equals("") && transport.getText().toString().equals("") && distance.getText().toString().equals("")){

                    Toast toast = Toast.makeText(this,
                            "Вы что-то пропустили ", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    DBHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_MP1, str_mp1);
                    values.put(DBHelper.KEY_MP2, str_mp2);
                    values.put(DBHelper.KEY_MP3, str_mp3);
                    values.put(DBHelper.KEY_MP4, str_mp4);
                    values.put(DBHelper.KEY_MP5, str_mp5);
                    values.put(DBHelper.KEY_MP6, str_mp6);
                    values.put(DBHelper.KEY_MP7, str_mp7);
                    values.put(DBHelper.KEY_MP8, str_mp8);
                    values.put(DBHelper.KEY_MP9, str_mp9);
                    values.put(DBHelper.KEY_MP10, str_mp10);
                    values.put(DBHelper.KEY_MP11, str_mp11);
                    values.put(DBHelper.KEY_MP12, str_mp12);
                    values.put(DBHelper.KEY_MP13, str_mp13);
                    values.put(DBHelper.KEY_MP14, str_mp14);
                    values.put(DBHelper.KEY_MP15, str_mp15);
                    values.put(DBHelper.KEY_MP16, str_mp16);
                    values.put(DBHelper.KEY_MP17, str_mp17);
                    values.put(DBHelper.KEY_MP18, str_mp18);
                    values.put(DBHelper.KEY_MP19, str_mp19);
                    values.put(DBHelper.KEY_MP22, str_mp22);
                    values.put(DBHelper.KEY_MP23, str_mp23);
                    values.put(DBHelper.KEY_MP24, str_mp24);
                    values.put(DBHelper.KEY_MP25, str_mp25);
                    values.put(DBHelper.KEY_MP26, str_mp26);
                    values.put(DBHelper.KEY_MP27, str_mp27);
                    values.put(DBHelper.KEY_MP30, str_mp30);
                    values.put(DBHelper.KEY_MP31, str_mp31);
                    values.put(DBHelper.KEY_MP32, str_mp32);
                    values.put(DBHelper.KEY_MP33, str_mp33);
                    values.put(DBHelper.KEY_MP34, str_mp34);
                    values.put(DBHelper.KEY_MP36, str_mp36);
                    values.put(DBHelper.KEY_MP37, str_mp37);
                    values.put(DBHelper.KEY_MP38, str_mp38);
                    values.put(DBHelper.KEY_MP40, str_mp40);
                    values.put(DBHelper.KEY_MP41, str_mp41);
                    values.put(DBHelper.KEY_MP42, str_mp42);
                    values.put(DBHelper.KEY_MP43, str_mp43);
                    values.put(DBHelper.KEY_TRANSPORT, str_transport);
                    values.put(DBHelper.KEY_DISTANCE, str_distance);
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNT, values, "user_id = ?", new String[]{user_id});

                    values = new ContentValues();
                    values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, str_canvases_margin);
                    values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, str_components_margin);
                    values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, str_mounting_margin);
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_DEALER_INFO, values, "dealer_id = ?", new String[]{user_id});

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, id_dealer_info);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_dealer_info");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, id_mount);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mount");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                }
                finish();
                break;
        }
    }

}
