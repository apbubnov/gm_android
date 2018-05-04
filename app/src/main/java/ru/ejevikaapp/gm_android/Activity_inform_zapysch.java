package ru.ejevikaapp.gm_android;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.ejevikaapp.gm_android.Fragments.Frag_general_zapycsh;
import ru.ejevikaapp.gm_android.Fragments.Fragment_inform_zapysch;

public class Activity_inform_zapysch extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    String id_cl;

    int i = 0;

    String SAVED_ID="";

    DBHelper dbHelper;

    ArrayList id_calc = new ArrayList();
    ArrayList title_calc = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_zapysch);

        mViewPager = (ViewPager) findViewById(R.id.container);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        id_calc.add("");
        title_calc.add("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Frag_general_zapycsh(), "Общее");
        count();
        for (int j = 1; j < i + 1 ; j++) {

            adapter.addFragment(new Fragment_inform_zapysch(), String.valueOf(title_calc.get(j)));

            SharedPreferences SP = getSharedPreferences("id_calc", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString(SAVED_ID, String.valueOf(id_calc.get(j)));
            ed.commit();
        }

        viewPager.setAdapter(adapter);
    }

    void count(){
        SharedPreferences SPI = getSharedPreferences("id_project_spisok", MODE_PRIVATE);
        id_cl = SPI.getString(SAVED_ID,"");

        i=0;

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT _id, calculation_title, project_id, n5 "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ? ";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    id_calc.add(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    title_calc.add(c.getString(c.getColumnIndex(c.getColumnName(1))));

                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences SP = getSharedPreferences("end_activity_inform_proj", MODE_PRIVATE);
        String end = SP.getString("", "");

        if (end.equals("1")){
            SP = getSharedPreferences("end_activity_inform_proj", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", "0");
            ed.commit();
            finish();
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {

            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            String i = String.valueOf(id_calc.get(position));
            if (position != 0){

                return(Fragment_inform_zapysch.newInstance(Integer.parseInt(i)));
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}