package ru.ejevikaapp.gm_android.Dealer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Fragments.Frag_g3_zapusch;
import ru.ejevikaapp.gm_android.Fragments.FragmentClient;
import ru.ejevikaapp.gm_android.Fragments.Fragment_calculation;
import ru.ejevikaapp.gm_android.MainActivity;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;
import ru.ejevikaapp.gm_android.Service_Sync_Import;

public class Dealer_office extends AppCompatActivity {

    View promptsView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.start_home:

                    SharedPreferences SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", "");
                    ed.commit();
                    loadFragment(Fragment_Home.newInstance());

                    return true;
                case R.id.start_calculation:

                    SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", "true");
                    ed.commit();
                    loadFragment(Fragment_calculation.newInstance());

                    return true;
                case R.id.start_projects:

                    SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", "");
                    ed.commit();
                    loadFragment(FragmentClient.newInstance());

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

    @SuppressLint("ResourceType")
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

            SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
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

                                    if (userInput.getText().toString().equals("")){
                                        userInput.setText("0");
                                    }

                                    DBHelper dbHelper = new DBHelper(Dealer_office.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_MIN_SUM, userInput.getText().toString());
                                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_MOUNT, values, "user_id = ?",
                                            new String[]{user_id});

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, user_id);
                                    values.put(DBHelper.KEY_ID_NEW, "0");
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_mount");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                    startService(new Intent(Dealer_office.this, Service_Sync.class));


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

            return true;

        } else if (id == R.id.initial_cost) {
            Intent intent = new Intent(Dealer_office.this, Activity_margin.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {

            final Context context = this;
            LayoutInflater li = LayoutInflater.from(context);
            promptsView = li.inflate(R.layout.layout_profile_dealer, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
            mDialogBuilder.setView(promptsView);
            //final EditText pass = (EditText) promptsView.findViewById(R.id.ed_password);
            final EditText email = (EditText) promptsView.findViewById(R.id.ed_email);
            final EditText name = (EditText) promptsView.findViewById(R.id.ed_name);
            final ImageView ava = (ImageView) promptsView.findViewById(R.id.ed_ava);

            String avatar_user = "";
            try {
                SharedPreferences SP_end = getSharedPreferences("avatar_user", MODE_PRIVATE);
                avatar_user = SP_end.getString("", "");
            } catch (Exception e) {
            }

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(avatar_user));
                ava.setImageBitmap(bitmap);
            } catch (IOException e) {
               // ava.setBackgroundResource(R.drawable.it_c);
            }

            ava.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                }
            });

            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            final String user_id = SP.getString("", "");

            String str_email = "";
            String str_name = "";
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();

            String sqlQuewy = "SELECT email, name "
                    + "FROM rgzbn_users" +
                    " WHERE _id = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        str_email = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        str_name = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            email.setText(str_email);
            name.setText(str_name);

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
                                    values.put(DBHelper.KEY_NAME, name.getText().toString());
                                    db.update(DBHelper.TABLE_USERS, values, "_id = ?",
                                            new String[]{user_id});

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, user_id);
                                    values.put(DBHelper.KEY_ID_NEW, "0");
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                    startService(new Intent(Dealer_office.this, Service_Sync.class));

                                    loadFragment(Fragment_Home.newInstance());

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

            return true;
        }  else if (id == R.id.margin) {

            final Context context = this;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_margin, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
            mDialogBuilder.setView(promptsView);
            final EditText canvases_margin = (EditText) promptsView.findViewById(R.id.canvases_margin);
            final EditText components_margin = (EditText) promptsView.findViewById(R.id.components_margin);
            final EditText mounting_margin = (EditText) promptsView.findViewById(R.id.mounting_margin);

            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            final String user_id = SP.getString("", "");

            String min_sum = "";
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();

            String str_canvases_margin = "";
            String str_components_margin = "";
            String str_mounting_margin = "";
            String id_dealer_info = "";
            String sqlQuewy = "SELECT dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, _id "
                    + "FROM rgzbn_gm_ceiling_dealer_info" +
                    " WHERE dealer_id = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        str_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        str_components_margin = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        str_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(2)));
                        id_dealer_info = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            canvases_margin.setText(str_canvases_margin);
            components_margin.setText(str_components_margin);
            mounting_margin.setText(str_mounting_margin);

            final String id_dealer = id_dealer_info;
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (canvases_margin.getText().toString().equals("")){
                                        canvases_margin.setText("0");
                                    }
                                    if (components_margin.getText().toString().equals("")){
                                        components_margin.setText("0");
                                    }
                                    if (mounting_margin.getText().toString().equals("")){
                                        mounting_margin.setText("0");
                                    }
                                    DBHelper dbHelper = new DBHelper(Dealer_office.this);
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, canvases_margin.getText().toString());
                                    values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, components_margin.getText().toString());
                                    values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, mounting_margin.getText().toString());
                                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_DEALER_INFO, values, "dealer_id = ?", new String[]{user_id});

                                    values = new ContentValues();
                                    values.put(DBHelper.KEY_ID_OLD, id_dealer);
                                    values.put(DBHelper.KEY_ID_NEW, "0");
                                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_dealer_info");
                                    values.put(DBHelper.KEY_SYNC, "0");
                                    values.put(DBHelper.KEY_TYPE, "send");
                                    values.put(DBHelper.KEY_STATUS, "1");
                                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                                    startService(new Intent(Dealer_office.this, Service_Sync.class));

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

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_office);
        loadFragment(Fragment_calculation.newInstance());

        SharedPreferences SP = getSharedPreferences("dealer_calc", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "true");
        ed.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SP = getSharedPreferences("first_entry", MODE_PRIVATE);
        final String first_entry = SP.getString("", "");

        if (first_entry.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Dealer_office.this);
            builder.setTitle("Подсказка")
                    .setMessage("В правом верхнем углу есть кнопка, если на неё нажать, то вы увидите скрытое меню")
                    .setCancelable(false)
                    .setNegativeButton("Спасибо",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    loadFragment(Fragment_calculation.newInstance());
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            SP = getSharedPreferences("first_entry", MODE_PRIVATE);
            ed = SP.edit();
            ed.putString("", "1");
            ed.commit();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;

        ImageView ava = (ImageView) promptsView.findViewById(R.id.ed_ava);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ava.setImageBitmap(bitmap);

                    SharedPreferences SP = getSharedPreferences("avatar_user", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", String.valueOf(selectedImage));
                    ed.commit();

                }
        }
    }
}