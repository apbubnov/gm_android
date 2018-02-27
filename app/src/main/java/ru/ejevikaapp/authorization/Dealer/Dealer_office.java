package ru.ejevikaapp.authorization.Dealer;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.Fragments.Frag_g3_zapusch;
import ru.ejevikaapp.authorization.Fragments.Fragment_calculation;
import ru.ejevikaapp.authorization.MainActivity;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;
import ru.ejevikaapp.authorization.Service_Sync_Import;

public class Dealer_office extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(Fragment_Home.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    SharedPreferences SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", "true");
                    ed.commit();
                    loadFragment(Fragment_calculation.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(Frag_g3_zapusch.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dealer_office, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exit) {
            stopService(new Intent(Dealer_office.this, Service_Sync.class));
            stopService(new Intent(Dealer_office.this, Service_Sync_Import.class));
            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "");
            ed.commit();

            SP = getSharedPreferences("avatar_user", MODE_PRIVATE);
            ed = SP.edit();
            ed.putString("", "");
            ed.commit();

            SP = getSharedPreferences("first_entry", MODE_PRIVATE);
            ed = SP.edit();
            ed.putString("", "");
            ed.commit();

            Intent intent = new Intent(Dealer_office.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.min_sum) {

            final Context context = this;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_min_sum, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
            mDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.ed_min_sum);

            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            final String user_id = SP.getString("", "");

            String min_sum = "";
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();
            String sqlQuewy = "SELECT min_sum "
                    + "FROM rgzbn_gm_ceiling_mount" +
                    " WHERE user_id = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        min_sum = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            userInput.setText(min_sum);

            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DBHelper dbHelper = new DBHelper(Dealer_office.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_MIN_SUM, userInput.getText().toString());
                                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNT, values, "user_id = ?",
                                            new String[]{user_id});

                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();

            ContentValues values = new ContentValues();
            values = new ContentValues();
            values.put(DBHelper.KEY_ID_OLD, user_id);
            values.put(DBHelper.KEY_ID_NEW, "0");
            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mount");
            values.put(DBHelper.KEY_SYNC, "0");
            values.put(DBHelper.KEY_TYPE, "send");
            values.put(DBHelper.KEY_STATUS, "1");
            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

            startService(new Intent(this, Service_Sync.class));

            return true;

        } else if (id == R.id.margin) {
            Intent intent = new Intent(Dealer_office.this, Activity_margin.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {

            final Context context = this;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_profile_dealer, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
            mDialogBuilder.setView(promptsView);
            //final EditText pass = (EditText) promptsView.findViewById(R.id.ed_password);
            final EditText email = (EditText) promptsView.findViewById(R.id.ed_email);

            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            final String user_id = SP.getString("", "");

            String str_email = "";
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();

            String sqlQuewy = "SELECT email "
                    + "FROM rgzbn_users" +
                    " WHERE _id = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        str_email = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            Log.d("mLog", user_id + "  email =  " + str_email);

            email.setText(str_email);

            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DBHelper dbHelper = new DBHelper(Dealer_office.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    //values.put(DBHelper.KEY_MIN_SUM, userInput.getText().toString());  тут будет пароль
                                    values.put(DBHelper.KEY_EMAIL, email.getText().toString());
                                    db.update(DBHelper.TABLE_USERS, values, "user_id = ?",
                                            new String[]{user_id});

                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();

            ContentValues values = new ContentValues();
            values = new ContentValues();
            values.put(DBHelper.KEY_ID_OLD, user_id);
            values.put(DBHelper.KEY_ID_NEW, "0");
            values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
            values.put(DBHelper.KEY_SYNC, "0");
            values.put(DBHelper.KEY_TYPE, "send");
            values.put(DBHelper.KEY_STATUS, "1");
            db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

            startService(new Intent(this, Service_Sync.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_office);
        loadFragment(Fragment_Home.newInstance());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        SharedPreferences SP = getSharedPreferences("first_entry", MODE_PRIVATE);
        final String first_entry = SP.getString("", "");

        if (first_entry.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Dealer_office.this);
            builder.setTitle("Подсказка")
                    .setMessage("В правом верхнем углу есть кнопка, если на неё нажать, то вы увидите скрытое меню")
                    .setCancelable(false)
                    .setNegativeButton("Спасибо",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            SP = getSharedPreferences("first_entry", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "1");
            ed.commit();

        }
    }
}