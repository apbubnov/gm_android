package ru.ejevikaapp.authorization.Dealer;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ru.ejevikaapp.authorization.Fragments.Activity_calcul;
import ru.ejevikaapp.authorization.Fragments.Frag_g3_zapusch;
import ru.ejevikaapp.authorization.Fragments.Fragment_calculation;
import ru.ejevikaapp.authorization.MainActivity;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;
import ru.ejevikaapp.authorization.Service_Sync_Import;

public class Dealer_office extends AppCompatActivity{
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

                Intent intent = new Intent(Dealer_office.this, MainActivity.class);
                startActivity(intent);
                finish();
            return true;
        } else if (id == R.id.margin) {
            Intent intent = new Intent(Dealer_office.this, Activity_margin.class);
            startActivity(intent);
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
    }

}