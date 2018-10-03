package ru.ejevikaapp.gm_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.ejevikaapp.gm_android.Fragments.Frag_g3_zapusch;
import ru.ejevikaapp.gm_android.Fragments.Frag_spisok;

public class Gager_office extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String user_id;
    AlarmImportData alarmImportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gager_office);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences SP_end = this.getSharedPreferences("name_user", MODE_PRIVATE);
        String user_name = SP_end.getString("", "");

        SP_end = this.getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP_end.getString("", "");

        View navHeader = navigationView.getHeaderView(0);
        TextView twNavBarName = (TextView) navHeader.findViewById(R.id.nav_header1);
        twNavBarName.setText(user_name);

        Frag_spisok Frag_c = new Frag_spisok();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_fragment_blank2,
                Frag_c,
                Frag_c.getTag()
        ).commit();

        alarmImportData = new AlarmImportData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gager_office, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.repeat_project) {

            new AlertDialog.Builder(this)
                    .setTitle("Перезагрузить проекты?")
                    .setIcon(R.raw.alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Идёт перезагрузка проектов... ", Toast.LENGTH_SHORT);
                            toast.show();

                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            SQLiteDatabase db = dbHelper.getReadableDatabase();

                            ContentValues values = new ContentValues();
                            values.put(DBHelper.KEY_CHANGE_TIME, "0000-00-00 00:00:00");
                            db.update(DBHelper.HISTORY_IMPORT_TO_SERVER, values, "user_id = ?", new String[]{user_id});

                            db.delete(DBHelper.HISTORY_SEND_TO_SERVER, null, null);

                            Intent intent = new Intent(Gager_office.this, AlarmImportData.class);
                            alarmImportData.onReceive(Gager_office.this,intent);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.g1_spisok) {
            Frag_spisok Frag_g1_z = new Frag_spisok();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment_blank2,
                    Frag_g1_z,
                    Frag_g1_z.getTag()
            ).commit();
        } else if (id == R.id.g1_zapusch) {
            Frag_g3_zapusch Frag_z = new Frag_g3_zapusch();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment_blank2,
                    Frag_z,
                    Frag_z.getTag()
            ).commit();
        } else if (id == R.id.exit) {

            stopService(new Intent(Gager_office.this, Service_Sync.class));
            alarmImportData.CancelAlarm(this);
            SharedPreferences SP = getSharedPreferences("user_id", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "");
            ed.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
