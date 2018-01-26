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
import android.view.MenuItem;
import android.widget.TextView;

import ru.ejevikaapp.authorization.Fragments.Activity_calcul;
import ru.ejevikaapp.authorization.Fragments.Frag_g3_zapusch;
import ru.ejevikaapp.authorization.Fragments.Fragment_calculation;
import ru.ejevikaapp.authorization.R;

public class Dealer_office extends AppCompatActivity{

    Boolean dealer = false;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_office);

        loadFragment(Fragment_Home.newInstance());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}
