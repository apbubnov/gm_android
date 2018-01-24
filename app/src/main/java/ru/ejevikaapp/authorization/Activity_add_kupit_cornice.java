package ru.ejevikaapp.authorization;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Kupit_cornice;

/**
 * Created by Дмитрий on 12.10.2017.
 */

public class Activity_add_kupit_cornice extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;
    EditText kol_vo_cornice;
    Button btn_add_cornice;
    ListView list_cornice;

    String select_color, select_lampa, id_calc;

    final ArrayList<Kupit_cornice> svet_mas = new ArrayList<>();

    ArrayList s_c = new ArrayList();
    ArrayList s_l = new ArrayList();

    Integer type, length;

    Cursor c=null;

    String gager_id="";
    Integer gager_id_int = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kupit_cornice);

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        SP = this.getSharedPreferences("id_calc", MODE_PRIVATE);
        id_calc = SP.getString("", "");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = null;

        s_c.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_type" +
                " WHERE _id = ' 10'";

        c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_c.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_vid);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_c);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_color= spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_type" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c = db.rawQuery(sqlQuewy, new String[]{select_color});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                type = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s_l.clear();
        String item_content2 = null;
        db = dbHelper.getWritableDatabase();

        sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 51'";

        c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        Log.d("title",item_content2);
                    }

                    s_l.add(item_content2);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_diametr);
        SpinnerAdapter adapter2 = new ArrayAdapter<String>(Activity_add_kupit_cornice.this, android.R.layout.simple_spinner_item, s_l);
        ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_lampa = spinner2.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c = db.rawQuery(sqlQuewy, new String[]{select_lampa});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                length = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        kol_vo_cornice = (EditText)findViewById(R.id.kol_vo_cornice);
        btn_add_cornice = (Button)findViewById(R.id.btn_add_cornice);

        btn_add_cornice.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null){
            id_calc = id_cl;
        }

        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null,
                "calculation_id =?", new String[]{id_calc}, null, null, null);

        if (cursor.moveToFirst()) {

            int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
            int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N15_COUNT);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_N15_TYPE);
            int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_N15_SIZE);

            do {
                String type_c = cursor.getString(typeIndex);
                String length_c = cursor.getString(sizeIndex);
                String type="";
                String length="";

                Log.d("diam = ", String.valueOf(typeIndex) + " " + cursor.getString(sizeIndex));

                sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_type" +
                        " WHERE _id = ?";

                c = db.rawQuery(sqlQuewy, new String[]{type_c});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            type = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        } while (c.moveToNext());
                    }
                }
                c.close();

                sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE _id = ?";

                c = db.rawQuery(sqlQuewy, new String[]{length_c});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            length = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        } while (c.moveToNext());
                    }
                }
                c.close();

                Kupit_cornice kc = new Kupit_cornice(cursor.getString(kdIndex), cursor.getString(kidIndex),
                        cursor.getString(kol_voIndex), type, length);
                svet_mas.add(kc);
            } while (cursor.moveToNext());
        }
        cursor.close();

        BindDictionary<Kupit_cornice> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Kupit_cornice>() {
            @Override
            public String getStringValue(Kupit_cornice nc, int position) {
                return nc.getKol_vo();
            }
        });
        dict.addStringField(R.id.tv_vid, new StringExtractor<Kupit_cornice>() {
            @Override
            public String getStringValue(Kupit_cornice nc, int position) {
                return nc.getType();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Kupit_cornice>() {
            @Override
            public String getStringValue(Kupit_cornice nc, int position) {
                return nc.getLength();
            }
        });

        FunDapter adapter = new FunDapter(this, svet_mas, R.layout.svet_list,dict);

        list_cornice = (ListView)findViewById(R.id.list_cornice);
        list_cornice.setAdapter(adapter);

        list_cornice.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Kupit_cornice selectedid = svet_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_kupit_cornice.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbHelper = new DBHelper(Activity_add_kupit_cornice.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, null, null, null, null, null);
//
                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, "_id = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                            new String[]{String.valueOf(s_id),"rgzbn_gm_ceiling_cornice"});
                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        Intent intent = new Intent(Activity_add_kupit_cornice.this, Activity_add_kupit_cornice.class);
                                        intent.putExtra("id_calc", id_calc);
                                        finish();
                                        startActivity(intent);
                                        cursor.close();

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Карниз удалён ", Toast.LENGTH_SHORT);
                                        toast.show();
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

        setTitle("Карнизы");
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

        switch (v.getId()) {
            case R.id.btn_add_cornice:
                Toast toast;
                String kol_vo = kol_vo_cornice.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count>0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_cornice " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int+999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = gager_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calc);
                    values.put(DBHelper.KEY_N15_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N15_TYPE, type);
                    values.put(DBHelper.KEY_N15_SIZE, length);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_cornice");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getApplicationContext(),
                            "Карниз добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(this, Activity_add_kupit_cornice.class);
                    intent.putExtra("id_calc", id_calc);
                    finish();
                    this.startActivity(intent);
                } else {toast = Toast.makeText(getApplicationContext(),
                        "Введите нужное количество карнизов", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}