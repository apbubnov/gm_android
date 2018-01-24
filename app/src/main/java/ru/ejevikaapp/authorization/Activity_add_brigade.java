package ru.ejevikaapp.authorization;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_add_brigade extends AppCompatActivity implements View.OnClickListener{

    EditText name, fio, phone;
    Button back, save;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brigade);

        name = (EditText)findViewById(R.id.name);
        fio = (EditText)findViewById(R.id.fio);
        phone = (EditText)findViewById(R.id.phone);

        save = (Button)findViewById(R.id.save);
        back = (Button)findViewById(R.id.back);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                if (fio.getText().length() > 0)
                {

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    String s_name = name.getText().toString().trim();
                    String s_phone = phone.getText().toString().trim();
                    String s_fio = fio.getText().toString().trim();

                //    values.put(DBHelper.KEY_NAME, s_name);
                //    values.put(DBHelper.KEY_PHONE, s_phone);
                //    values.put(DBHelper.KEY_FIO, s_fio);
                //    db.insert(DBHelper.TABLE_NAME7, null, values);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Бригада добавлена", Toast.LENGTH_SHORT);
                    toast.show();

                    finish();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "У Вас не заполнена фамилия", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.back:
                    finish();
                break;
        }
    }
}
