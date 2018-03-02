package ru.ejevikaapp.gm_android;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

import ru.ejevikaapp.gm_android.Class.Other_comp_and_work_class;

public class Activity_add_other_comp extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    EditText name_work, price_work;
    Button btn_add_work;
    ListView list_diff;

    ArrayList s_d = new ArrayList();

    Integer size;

    final ArrayList<Other_comp_and_work_class> diff_mas = new ArrayList<>();

    String diffuzor, id_calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_comp);

        SharedPreferences SP = this.getSharedPreferences("id_calc", MODE_PRIVATE);
        id_calc = SP.getString("", "");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sqlQuewy = "";

        Cursor c;

        name_work = (EditText)findViewById(R.id.name_work);
        price_work = (EditText)findViewById(R.id.price_work);

        btn_add_work = (Button)findViewById(R.id.btn_add_work);
        btn_add_work.setOnClickListener(this);

        String id_cl = getIntent().getStringExtra("id_calc");
        if (id_cl != null){
            id_calc = id_cl;
        }
        Cursor cursor = db.query(DBHelper.TABLE_OTHER_COMP, null, "calculation_id =?", new String[]{id_calc}, null, null, null);

        if (cursor.moveToFirst()) {
            int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            do {

                Other_comp_and_work_class d = new Other_comp_and_work_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                        cursor.getString(nameIndex), cursor.getString(priceIndex));
                diff_mas.add(d);
            } while (cursor.moveToNext());
        }
        cursor.close();

        BindDictionary<Other_comp_and_work_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_id, new StringExtractor<Other_comp_and_work_class>() {
            @Override
            public String getStringValue(Other_comp_and_work_class nc, int position) {
                return nc.getId();
            }
        });

        dict.addStringField(R.id.tv_name, new StringExtractor<Other_comp_and_work_class>() {
            @Override
            public String getStringValue(Other_comp_and_work_class nc, int position) {
                return nc.getName();
            }
        });
        dict.addStringField(R.id.tv_price, new StringExtractor<Other_comp_and_work_class>() {
            @Override
            public String getStringValue(Other_comp_and_work_class nc, int position) {
                return nc.getPrice();
            }
        });

        FunDapter adapter = new FunDapter(this, diff_mas, R.layout.other_product,dict);

        list_diff = (ListView)findViewById(R.id.list_diff);
        list_diff.setAdapter(adapter);

        list_diff.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Other_comp_and_work_class selectedid = diff_mas.get(position);
                final String s_id = selectedid.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_add_other_comp.this);
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbHelper = new DBHelper(Activity_add_other_comp.this);
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        Cursor cursor = db.query(DBHelper.TABLE_OTHER_COMP, null, null, null, null, null, null);
//
                                        if (cursor.moveToFirst()) {
                                            int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                            do {
                                                if (s_id.equals(cursor.getString(kd_Index))) {
                                                    db.delete(DBHelper.TABLE_OTHER_COMP, "_id = ?", new String[]{String.valueOf(s_id)});
                                                }
                                            }
                                            while (cursor.moveToNext());
                                        }
                                        Intent intent = new Intent(Activity_add_other_comp.this, Activity_add_other_comp.class);
                                        intent.putExtra("id_calc", id_calc);
                                        finish();
                                        startActivity(intent);
                                        //  cursor.close();

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Комплектующая удалёна ", Toast.LENGTH_SHORT);
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_work:
                Toast toast;
                String name = name_work.getText().toString().trim();
                String price = price_work.getText().toString().trim();
                int count=0;
                if (name_work.length() == 0 && price_work.length() == 0 ) {
                    count = 0;
                } else if (name_work.length() >0 && price_work.length() >0) {
                    count = 1;
                }
                if (count>0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();

                    values.put(DBHelper.KEY_CALCULATION_ID, id_calc);
                    values.put(DBHelper.KEY_NAME, name);
                    values.put(DBHelper.KEY_PRICE, price);
                    db.insert(DBHelper.TABLE_OTHER_COMP, null, values);

                    toast = Toast.makeText(getApplicationContext(),
                            "Комплектующая добавлена ", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(this, Activity_add_other_comp.class);
                    intent.putExtra("id_calc", id_calc);
                    finish();
                    this.startActivity(intent);
                } else {toast = Toast.makeText(getApplicationContext(),
                        "Заполните все поля", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
