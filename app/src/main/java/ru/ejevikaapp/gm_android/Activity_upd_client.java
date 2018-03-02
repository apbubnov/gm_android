package ru.ejevikaapp.gm_android;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_upd_client extends AppCompatActivity implements View.OnClickListener{

    String id;
    DBHelper dbHelper;
    EditText name, phone, address;
    Button save, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upd_client);

        id = getIntent().getExtras().getString("ID_Client");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);

    //   Cursor cursor = db.query(DBHelper.TABLE_NAME6, null, null, null, null, null, null);

    //   if (cursor.moveToFirst()) {
    //       int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
    //       int fioIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
    //       int phoneIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
    //       int addressIndex = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
    //       do {
    //           if (id.equals(cursor.getString(idIndex))){
    //               name.setText(cursor.getString(fioIndex));
    //               phone.setText(cursor.getString(phoneIndex));
    //               address.setText(cursor.getString(addressIndex));
    //           }
    //       } while (cursor.moveToNext());
    //   }
    //   cursor.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:

                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String fio = name.getText().toString().trim();
                String number = phone.getText().toString().trim();
                String s_address = address.getText().toString().trim();

            //    values.put(DBHelper.KEY_FIO, fio);
            //    values.put(DBHelper.KEY_PHONE, number);
            //    values.put(DBHelper.KEY_ADDRESS, s_address);
            //    db.update(DBHelper.TABLE_NAME6, values, DBHelper.KEY_ID + "=" + id, null);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Клиент изменен", Toast.LENGTH_SHORT);
                toast.show();

                finish();

                break;
            case R.id.back:
                finish();
                break;

        }
    }
}
