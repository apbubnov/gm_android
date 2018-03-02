package ru.ejevikaapp.gm_android;

import android.content.Intent;
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

import ru.ejevikaapp.gm_android.Manager.Frag_in_production;
import ru.ejevikaapp.gm_android.Manager.Frag_started_projects;

public class Manager_office extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_office);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_office, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       // if (id == R.id.action_settings) {
       //     return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
      // Frag_g3_client Frag_g3_c = new Frag_g3_client();
      // FragmentManager manager = getSupportFragmentManager();
      // manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
      //         Frag_g3_c,
      //         Frag_g3_c.getTag()
      // ).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.in_production) {
            Frag_in_production Frag_c = new Frag_in_production();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
                    Frag_c,
                    Frag_c.getTag()
            ).commit();
        }
        else if (id == R.id.started_projects) {
            Frag_started_projects Frag_g1_z = new Frag_started_projects();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
                    Frag_g1_z,
                    Frag_g1_z.getTag()
            ).commit();
        }
       // else if (id == R.id.customer_orders) {
       //     Frag_g2_price_poloten Frag_g2_p = new Frag_g2_price_poloten();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g2_p,
       //             Frag_g2_p.getTag()
       //     ).commit();
       // }
       // else if (id == R.id.application_site) {
       //     Frag_g2_price_komplect Frag_g2_k = new Frag_g2_price_komplect();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g2_k,
       //             Frag_g2_k.getTag()
       //     ).commit();
       // }
       // else if (id == R.id.calls) {
       //     Frag_g3_client Frag_g3_c = new Frag_g3_client();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g3_c,
       //             Frag_g3_c.getTag()
       //     ).commit();
       // }
       // else if (id == R.id.entry_for_metering) {
       //     Frag_g3_otkazy Frag_g3_o = new Frag_g3_otkazy();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g3_o,
       //             Frag_g3_o.getTag()
       //     ).commit();
       // }
       // else if (id == R.id.prices) {
       //     Frag_g3_otkazy Frag_g3_o = new Frag_g3_otkazy();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g3_o,
       //             Frag_g3_o.getTag()
       //     ).commit();
       // }
       // else if (id == R.id.colors_of_canvases) {
       //     Frag_g3_otkazy Frag_g3_o = new Frag_g3_otkazy();
       //     FragmentManager manager = getSupportFragmentManager();
       //     manager.beginTransaction().replace(R.id.relativelayout_fragment_blank3,
       //             Frag_g3_o,
       //             Frag_g3_o.getTag()
       //     ).commit();
       // }
        else if (id == R.id.exit) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
