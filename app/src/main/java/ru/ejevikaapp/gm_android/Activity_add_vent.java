package ru.ejevikaapp.gm_android;

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

import ru.ejevikaapp.gm_android.Class.Ventil_class;

public class Activity_add_vent extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    EditText kol_vo_vent;

    Button btn_add_vent;
    ListView list_vent;

    ArrayList s_t = new ArrayList();
    ArrayList s_r = new ArrayList();

    String select_type, select_razmer, id_calc;

    Integer type, size;

    Cursor c;

    String gager_id="";
    Integer gager_id_int = 0;

    final ArrayList<Ventil_class> vent_mas = new ArrayList<>();

    String s_type, s_razmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vent);

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        SP = this.getSharedPreferences("id_calc", MODE_PRIVATE);
        id_calc = SP.getString("", "");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = null;

        s_t.clear();

        c = db.query("rgzbn_gm_ceiling_type", null, "_id between 5 and 8", null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    }
                    s_t.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_type);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_t);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String c_id = "";

                select_type = spinner1.getSelectedItem().toString();

                switch (spinner1.getSelectedItemPosition()){
                    case (0): c_id = " 21";
                        type = 5;
                        break;
                    case (2): c_id = " 21";
                        type = 7;
                        break;
                    case (1): c_id = " 12";
                        type = 6;
                        break;
                    case (3): c_id = " 12";
                        type = 8;
                        break;
                }

                s_r.clear();
                String item_content2 = null;
                SQLiteDatabase db = dbHelper.getWritableDatabase();

             //   String sqlQuewy = "SELECT _id "
             //           + "FROM rgzbn_gm_ceiling_components_option" +
             //           " WHERE component_id = ?";
//
             //   c = db.rawQuery(sqlQuewy, new String[]{c_id});
//
             //   if (c != null) {
             //       if (c.moveToFirst()) {
             //           do {
             //               for (String cn : c.getColumnNames()) {
             //                   type = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
//
             //               }
             //           } while (c.moveToNext());
             //       }
             //   }
             //   c.close();

                String sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE component_id = ?";

                c = db.rawQuery(sqlQuewy, new String[]{c_id});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                item_content2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                Log.d("title",item_content2);
                            }

                            s_r.add(item_content2);
                        } while (c.moveToNext());
                    }
                }
                c.close();


                final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_razmer);
                SpinnerAdapter adapter2 = new ArrayAdapter<String>(Activity_add_vent.this, android.R.layout.simple_spinner_item, s_r);
                ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {

                        select_razmer = spinner2.getSelectedItem().toString();

                        String sqlQuewy = "SELECT _id "
                                + "FROM rgzbn_gm_ceiling_components_option" +
                                " WHERE title = ?";

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        c = db.rawQuery(sqlQuewy, new String[]{select_razmer});

                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    for (String cn : c.getColumnNames()) {
                                        size = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    }
                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        kol_vo_vent = (EditText)findViewById(R.id.kol_vo_vent);
        btn_add_vent= (Button)findViewById(R.id.btn_add_vent);

        btn_add_vent.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null){
            id_calc = id_cl;
        }
        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, "calculation_id =?", new String[]{id_calc}, null, null, null);
//
        if (cursor.moveToFirst()) {
            int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
            int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N22_COUNT);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_N22_TYPE);
            int razmerIndex = cursor.getColumnIndex(DBHelper.KEY_N22_SIZE);
            do {

                String type_c = cursor.getString(typeIndex);
                String size_c = cursor.getString(razmerIndex);
                String type="";
                String size="";

              //  Log.d("diam = ", String.valueOf(colorIndex) + " " + cursor.getString(lampaIndex));

                String sqlQuewy = "SELECT title "
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

                c = db.rawQuery(sqlQuewy, new String[]{size_c});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            size = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        } while (c.moveToNext());
                    }
                }
                c.close();

                Ventil_class c = new Ventil_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                        cursor.getString(kol_voIndex),
                        type, size);
                vent_mas.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();

        BindDictionary<Ventil_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Ventil_class>() {
            @Override
            public String getStringValue(Ventil_class nc, int position) {
                return nc.getKol_vo();
            }
        });
        dict.addStringField(R.id.tv_vid, new StringExtractor<Ventil_class>() {
            @Override
            public String getStringValue(Ventil_class nc, int position) {
                return nc.getType();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Ventil_class>() {
            @Override
            public String getStringValue(Ventil_class nc, int position) {
                return nc.getRazmer();
            }
        });

        FunDapter adapter = new FunDapter(this, vent_mas, R.layout.svet_list,dict);

        list_vent = (ListView)findViewById(R.id.list_vent);
        list_vent.setAdapter(adapter);

        list_vent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Ventil_class selectedid = vent_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_vent.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbHelper = new DBHelper(Activity_add_vent.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                          Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, null, null, null, null, null);
//
                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, "_id = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                            new String[]{String.valueOf(s_id),"rgzbn_gm_ceiling_hoods"});
                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        Intent intent = new Intent(Activity_add_vent.this, Activity_add_vent.class);
                                        intent.putExtra("id_calc", id_calc);
                                        finish();
                                        startActivity(intent);
                                          cursor.close();

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Вентиляция удалена ", Toast.LENGTH_SHORT);
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

        setTitle("Вентиляция");
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
            case R.id.btn_add_vent:
                Toast toast;
                String kol_vo = kol_vo_vent.getText().toString().trim();
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
                                + "FROM rgzbn_gm_ceiling_hoods " +
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
                    values.put(DBHelper.KEY_N22_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N22_TYPE, type);
                    values.put(DBHelper.KEY_N22_SIZE, size);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_hoods");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getApplicationContext(),
                            "Вентиляция добавленa ", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(this, Activity_add_vent.class);
                    intent.putExtra("id_calc", id_calc);
                    finish();
                    this.startActivity(intent);
                } else {toast = Toast.makeText(getApplicationContext(),
                        "Введите нужное количество вентиляции", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}