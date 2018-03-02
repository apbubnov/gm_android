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

import ru.ejevikaapp.gm_android.Class.Diffuzor_class;

public class Activity_add_diffuzor extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    EditText kol_vo_diff;
    Spinner spinner_diff;
    Button btn_add_diff;
    ListView list_diff;

    ArrayList s_d = new ArrayList();

    String select_diffuzor, id_calc;

    Integer size;

    final ArrayList<Diffuzor_class> diff_mas = new ArrayList<>();

    String diffuzor ="";
    String gager_id="";
    Integer gager_id_int = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diffuzor);

        // SharedPreferences SP = this.getSharedPreferences("id_cl", MODE_PRIVATE);
        // id_calc = SP.getString("", "");

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = null;

        Cursor c;

        s_d.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 22' and count > ' 0'";

        c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_d.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_diff);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_d);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_diffuzor = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor c = db.rawQuery(sqlQuewy, new String[]{select_diffuzor});

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

        kol_vo_diff = (EditText) findViewById(R.id.kol_vo_diff);

        btn_add_diff = (Button) findViewById(R.id.btn_add_diff);
        btn_add_diff.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null) {
            id_calc = id_cl;
            Log.d("mLog dif1", id_calc);
        }
        Log.d("mLog dif2", id_calc);

        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, "calculation_id =?", new String[]{id_calc}, null, null, null);
//
        if (cursor.moveToFirst()) {
            int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
            int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N23_COUNT);
            int razmerIndex = cursor.getColumnIndex(DBHelper.KEY_N23_SIZE);
            do {

                String diff_c = cursor.getString(razmerIndex);
                String diff = "";

                sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE _id = ?";

                Log.d("pipes = ", diff + " " + diff_c);

                c = db.rawQuery(sqlQuewy, new String[]{diff_c});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            diff = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        } while (c.moveToNext());
                    }
                }
                c.close();
                Diffuzor_class d = new Diffuzor_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                        cursor.getString(kol_voIndex), diff);
                diff_mas.add(d);
            } while (cursor.moveToNext());
        }
        cursor.close();

        BindDictionary<Diffuzor_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_count, new StringExtractor<Diffuzor_class>() {
            @Override
            public String getStringValue(Diffuzor_class nc, int position) {
                return nc.getKol_vo();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Diffuzor_class>() {
            @Override
            public String getStringValue(Diffuzor_class nc, int position) {
                return nc.getRazmer();
            }
        });

        FunDapter adapter = new FunDapter(this, diff_mas, R.layout.list_2column, dict);

        list_diff = (ListView) findViewById(R.id.list_diff);
        list_diff.setAdapter(adapter);

        list_diff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Diffuzor_class selectedid = diff_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_diffuzor.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbHelper = new DBHelper(Activity_add_diffuzor.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, null, null, null, null, null);
//
                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);

                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, "_id = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                            new String[]{String.valueOf(s_id),"rgzbn_gm_ceiling_diffusers"});
                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        Intent intent = new Intent(Activity_add_diffuzor.this, Activity_add_diffuzor.class);
                                        intent.putExtra("id_calc", id_calc);
                                        finish();
                                        startActivity(intent);
                                        //  cursor.close();

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Диффузор удалён ", Toast.LENGTH_SHORT);
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

        setTitle("Диффузор");
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
            case R.id.btn_add_diff:
                Toast toast;
                String kol_vo = kol_vo_diff.getText().toString().trim();
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();

                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_diffusers " +
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
                    values.put(DBHelper.KEY_N23_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N23_SIZE, size);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_diffusers");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getApplicationContext(),
                            "Диффузор добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(this, Activity_add_diffuzor.class);
                    intent.putExtra("id_calc", id_calc);
                    finish();
                    this.startActivity(intent);
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Введите нужное количество диффузоров", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
