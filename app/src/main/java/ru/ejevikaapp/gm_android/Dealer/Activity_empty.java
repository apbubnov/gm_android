package ru.ejevikaapp.gm_android.Dealer;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spisok, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.add_gager:{


                SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
                final String user_id = SP.getString("", "");
                final int user_id_int = Integer.parseInt(user_id) * 1000000;

                final Context context = this;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.layout_add_gager, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);

                final AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
                alertDialog.show();

                final EditText nameGager = (EditText) promptsView.findViewById(R.id.nameGager);
                final EditText phoneGager = (EditText) promptsView.findViewById(R.id.phoneGager);
                final EditText mailGager = (EditText) promptsView.findViewById(R.id.mailGager);

                Button btnAddGager = (Button) promptsView.findViewById(R.id.btnAddGager);
                btnAddGager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str_nameGager  = nameGager.getText().toString();
                        String str_phoneGager = phoneGager.getText().toString();
                        String str_mailGager  = mailGager.getText().toString();

                        if (validateMail(str_mailGager)){
                            if (str_phoneGager.length() == 11){
                                if (str_nameGager.length()>0){

                                    DBHelper dbHelper = new DBHelper(Activity_empty.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    int max_id_gager = 0;
                                    try {
                                        String sqlQuewy = "select MAX(_id) "
                                                + "FROM rgzbn_gm_ceiling_mounters " +
                                                "where _id>? and _id<?";

                                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int),
                                                String.valueOf(user_id_int + 999999)});
                                        if (c != null) {
                                            if (c.moveToFirst()) {
                                                do {
                                                    max_id_gager = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                                    max_id_gager++;

                                                } while (c.moveToNext());
                                            }
                                        }
                                    } catch (Exception e) {
                                        max_id_gager = user_id_int + 1;
                                    }

                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_ID, max_id_gager);
                                    values.put(DBHelper.KEY_NAME, str_nameGager);
                                    values.put(DBHelper.KEY_DEALER_ID, user_id);
                                    values.put(DBHelper.KEY_EMAIL, str_mailGager);
                                    values.put(DBHelper.KEY_USERNAME, str_phoneGager);
                                    db.insert(DBHelper.TABLE_USERS, null, values);

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_USER_ID, max_id_gager);
                                    values.put(DBHelper.KEY_GROUP_ID, "21");
                                    db.insert(DBHelper.TABLE_RGZBN_USER_USERGROUP_MAP, null, values);

                                    Toast.makeText(getApplicationContext(), "Замерщик добавлен", Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();

                                } else{
                                    Toast.makeText(getApplicationContext(), "проверьте введенное имя", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "проверьте введенный телефон", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "проверьте введенную почту", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
