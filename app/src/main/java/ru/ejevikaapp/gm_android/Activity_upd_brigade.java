package ru.ejevikaapp.gm_android;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_upd_brigade extends AppCompatActivity implements View.OnClickListener{

    String id;
    DBHelper dbHelper;
    EditText name, phone, fio;
    Button save, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upd_brigade);

        id = getIntent().getExtras().getString("ID_Client");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        fio = (EditText) findViewById(R.id.fio);
        phone = (EditText) findViewById(R.id.phone);

    //    Cursor cursor = db.query(DBHelper.TABLE_NAME7, null, null, null, null, null, null);
//
    //    if (cursor.moveToFirst()) {
    //        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
    //        int fioIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
    //        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
    //        int phoneIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
    //        do {
    //            if (id.equals(cursor.getString(idIndex))){
    //                name.setText(cursor.getString(nameIndex));
    //                phone.setText(cursor.getString(phoneIndex));
    //                fio.setText(cursor.getString(fioIndex));
    //            }
    //        } while (cursor.moveToNext());
    //    }
    //    cursor.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:

                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String s_name = name.getText().toString().trim();
                String s_phone = phone.getText().toString().trim();
                String s_fio = fio.getText().toString().trim();

            //    values.put(DBHelper.KEY_FIO, s_fio);
            //    values.put(DBHelper.KEY_PHONE, s_phone);
            //    values.put(DBHelper.KEY_NAME, s_name);
            //    db.update(DBHelper.TABLE_NAME7, values, DBHelper.KEY_ID + "=" + id, null);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Бригада изменена", Toast.LENGTH_SHORT);
                toast.show();

                finish();

                break;
            case R.id.back:
                finish();
                break;

        }
    }
}
