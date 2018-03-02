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

import ru.ejevikaapp.gm_android.Class.Svetiln_class;

public class Activity_add_svetiln extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    EditText kol_vo_svetiln;
    Button btn_add_svetilnik;
    ListView list_svetilnik;

    String select_vid, select_diam, c_id = "", gager_id="", id_calc;

    Integer type_id, comp_opt;

    ArrayList s_v = new ArrayList();
    ArrayList s_d = new ArrayList();
    ArrayAdapter<String> adapter;

    Cursor c = null;

    final ArrayList<Svetiln_class> svet_mas = new ArrayList<>();

    Integer gager_id_int = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_add_svetiln);

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        dbHelper = new DBHelper(this);

        setTitle("Светильники");

        String groupBy1 = null;
        String item_content1 = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        s_v.clear();

        c = db.query("rgzbn_gm_ceiling_type", null, "_id between 2 and 3", null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    }
                    s_v.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_vid);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_v);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_vid = spinner1.getSelectedItem().toString();

                switch (spinner1.getSelectedItemPosition()){
                    case (0): c_id = " 21";
                                type_id = 2;
                        break;
                    case (1): c_id = " 12";
                                type_id = 3;
                        break;
                }

        s_d.clear();
        String item_content2 = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{c_id});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }

                    s_d.add(item_content2);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_diametr);
        SpinnerAdapter adapter2 = new ArrayAdapter<String>(Activity_add_svetiln.this, android.R.layout.simple_spinner_item, s_d);
        ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {

                        select_diam = spinner2.getSelectedItem().toString();

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        String sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_components_option" +
                                " WHERE component_id = ? and title = ?";

                        c = db.rawQuery(sqlQuewy, new String[]{c_id,select_diam});

                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                        comp_opt = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));

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

        kol_vo_svetiln = (EditText)findViewById(R.id.kol_vo_svetiln);
        btn_add_svetilnik = (Button)findViewById(R.id.btn_add_svetilnik);

        btn_add_svetilnik.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null){
            id_calc = id_cl;
            Log.d("mLog svet", id_calc);
        }

        String sqlQuewy = "SELECT * "
                + "FROM rgzbn_gm_ceiling_fixtures" +
                " WHERE calculation_id = ?";

        Cursor cursor = db.rawQuery(sqlQuewy, new String[]{id_calc});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int kdIndex = cursor.getColumnIndex(cursor.getColumnName(0));
                int kidIndex = cursor.getColumnIndex(cursor.getColumnName(1));
                int kol_voIndex = cursor.getColumnIndex(cursor.getColumnName(2));
                int vidIndex = cursor.getColumnIndex(cursor.getColumnName(3));
                int diametrIndex = cursor.getColumnIndex(cursor.getColumnName(4));

                do {
                    String vid_c = cursor.getString(vidIndex);
                    String diam_c = cursor.getString(diametrIndex);
                    String vid = "";
                    String diam = "";

                    sqlQuewy = "SELECT * "
                            + "FROM rgzbn_gm_ceiling_type" +
                            " WHERE _id = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{vid_c});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                vid = c.getString(c.getColumnIndex(c.getColumnName(2)));

                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{diam_c});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                diam = c.getString(c.getColumnIndex(c.getColumnName(0)));

                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    Svetiln_class c = new Svetiln_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), vid, diam);
                    svet_mas.add(c);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        BindDictionary<Svetiln_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Svetiln_class>() {
            @Override
            public String getStringValue(Svetiln_class nc, int position) {
                return nc.getKol_vo();
            }
        });
        dict.addStringField(R.id.tv_vid, new StringExtractor<Svetiln_class>() {
            @Override
            public String getStringValue(Svetiln_class nc, int position) {
                return nc.getVid();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Svetiln_class>() {
            @Override
            public String getStringValue(Svetiln_class nc, int position) {
                return nc.getDiametr();
            }
        });

        FunDapter adapter = new FunDapter(this, svet_mas, R.layout.svet_list,dict);

        list_svetilnik = (ListView)findViewById(R.id.list_svetilnik);
        list_svetilnik.setAdapter(adapter);

        list_svetilnik.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {        // УДАЛЕНИЕ

                Svetiln_class selectedid = svet_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_svetiln.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dbHelper = new DBHelper(Activity_add_svetiln.this);
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                               Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, null, null, null, null, null);

                               if (cursor.moveToFirst()) {
                                   int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                   do {
                                       if (s_id.equals(cursor.getString(kd_Index))) {
                                           db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, "_id = ?", new String[]{String.valueOf(s_id)});
                                           db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                   new String[]{String.valueOf(s_id),"rgzbn_gm_ceiling_fixtures"});
                                       }
                                   }
                                   while (cursor.moveToNext());
                               }
                                Intent intent = new Intent(Activity_add_svetiln.this, Activity_add_svetiln.class);
                                intent.putExtra("id_calc", id_calc);
                                finish();
                                startActivity(intent);
                                cursor.close();

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Светильник удалён ", Toast.LENGTH_SHORT);
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
            case R.id.btn_add_svetilnik:
                Toast toast;
                String kol_vo = kol_vo_svetiln.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }
                    if (count > 0) {

                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        int max_id_contac=0;
                        try {
                            String sqlQuewy = "select MAX(_id) "
                                    + "FROM rgzbn_gm_ceiling_fixtures " +
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
                        values.put(DBHelper.KEY_N13_COUNT, kol_vo);
                        values.put(DBHelper.KEY_N13_TYPE, type_id);
                        values.put(DBHelper.KEY_N13_SIZE, comp_opt);
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, values);

                        values = new ContentValues();
                        values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                        values.put(DBHelper.KEY_ID_NEW, "0");
                        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_fixtures");
                        values.put(DBHelper.KEY_SYNC, "0");
                        values.put(DBHelper.KEY_TYPE, "send");
                        values.put(DBHelper.KEY_STATUS, "0");
                        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                        toast = Toast.makeText(getApplicationContext(),
                                "Светильник добавлен ", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(this, Activity_add_svetiln.class);
                        intent.putExtra("id_calc", id_calc);
                        finish();
                        this.startActivity(intent);
                    } else {
                        toast = Toast.makeText(getApplicationContext(),
                                "Введите нужное количество светильников", Toast.LENGTH_SHORT);
                        toast.show();
                    }
            break;
        }
    }
}