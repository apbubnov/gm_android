package ru.ejevikaapp.authorization;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.format.Time;
import android.widget.Toast;

import ru.ejevikaapp.authorization.Fragments.Frag_g3_buhgalt;
import ru.ejevikaapp.authorization.Fragments.Frag_g3_client;


public class Activity_add_client extends AppCompatActivity implements View.OnClickListener{

    EditText name, phone;
    Button btn_back, btn_save;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        btn_back = (Button)findViewById(R.id.back);
        btn_save = (Button)findViewById(R.id.save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.save:

                if (name.getText().length() > 0)
                {

                    SQLiteDatabase db;
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    String fio = name.getText().toString().trim();
                    String number = phone.getText().toString().trim();

                    Time time = new Time(Time.getCurrentTimezone());
                    time.setToNow();
                    int mon = time.month+1;

                    String date = String.valueOf(time.monthDay + "." + mon + "." + time.year);

                 //  values.put(DBHelper.KEY_CLIENT_NAME, fio);
                 //  values.put(DBHelper.KEY_CLIENT_DATA_ID, );
                 //  values.put(DBHelper.KEY_TYPE_ID, );
                 //  values.put(DBHelper.KEY_DEALER_ID, );
                 //  values.put(DBHelper.KEY_MANAGER_ID, );
                 //  values.put(DBHelper.KEY_CREATED, );
                 //  db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Клиент добавлен", Toast.LENGTH_SHORT);
                    toast.show();

                    finish();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "У Вас что-то неправильно заполнено", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
