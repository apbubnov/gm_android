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

import ru.ejevikaapp.authorization.Class.Kupit_Svetlin_class;
import ru.ejevikaapp.authorization.Class.Truby_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_add_truby extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    EditText kol_vo_truby;
    Spinner spinner_diametr;
    Button btn_add_truby;
    ListView list_svetilnik;

    ArrayList s_t = new ArrayList();

    String select_truby, id_calc;

    Integer count, size;

    final ArrayList<Truby_class> truby_mas = new ArrayList<>();

    String diametr;

    String gager_id = "";
    Integer gager_id_int = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truby);

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        SP = this.getSharedPreferences("id_calc", MODE_PRIVATE);
        id_calc = SP.getString("", "");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = null;

        Cursor c;

        s_t.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 24' and count > ' 0'";

        c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_t.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_diametr);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_t);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_truby = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ? and component_id =' 24'";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor c = db.rawQuery(sqlQuewy, new String[]{select_truby});

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

        kol_vo_truby = (EditText) findViewById(R.id.kol_vo_truby);
        btn_add_truby = (Button) findViewById(R.id.btn_add_truby);

        btn_add_truby.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null) {
            id_calc = id_cl;
        }

        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, "calculation_id =?", new String[]{id_calc},
                null, null, null);

        if (cursor.moveToFirst()) {

            int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
            int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N14_COUNT);
            int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_N14_SIZE);

            do {
                String pipes_c = cursor.getString(sizeIndex);
                String pipes = "";

                sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE _id = ? ";

                Log.d("pipes = ", pipes + " | " + pipes_c);

                c = db.rawQuery(sqlQuewy, new String[]{pipes_c});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                Truby_class t = new Truby_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                        cursor.getString(kol_voIndex), pipes);
                truby_mas.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();

        BindDictionary<Truby_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_count, new StringExtractor<Truby_class>() {
            @Override
            public String getStringValue(Truby_class nc, int position) {
                return nc.getKol_vo();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Truby_class>() {
            @Override
            public String getStringValue(Truby_class nc, int position) {
                return nc.getDiametr();
            }
        });

        FunDapter adapter = new FunDapter(this, truby_mas, R.layout.list_2column, dict);

        list_svetilnik = (ListView) findViewById(R.id.list_truby);
        list_svetilnik.setAdapter(adapter);

        list_svetilnik.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Truby_class selectedid = truby_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_truby.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbHelper = new DBHelper(Activity_add_truby.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, null, null, null, null, null);

                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, "_id = ?", new String[]{String.valueOf(s_id)});
                                                    db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                            new String[]{String.valueOf(s_id),"rgzbn_gm_ceiling_pipes"});
                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        Intent intent = new Intent(Activity_add_truby.this, Activity_add_truby.class);
                                        intent.putExtra("id_calc", id_calc);
                                        finish();
                                        startActivity(intent);
                                        cursor.close();

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Трубы удалены ", Toast.LENGTH_SHORT);
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

        setTitle("Трубы");
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
            case R.id.btn_add_truby:
                Toast toast;
                String kol_vo = kol_vo_truby.getText().toString().trim();
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
                                + "FROM rgzbn_gm_ceiling_pipes " +
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
                    values.put(DBHelper.KEY_N14_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N14_SIZE, size);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_pipes");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getApplicationContext(),
                            "Трубы добавлены ", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(this, Activity_add_truby.class);
                    intent.putExtra("id_calc", id_calc);
                    finish();
                    this.startActivity(intent);
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Введите нужное количество труб", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
