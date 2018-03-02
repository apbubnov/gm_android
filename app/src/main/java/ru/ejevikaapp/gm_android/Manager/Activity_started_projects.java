package ru.ejevikaapp.gm_android.Manager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.Fragments.Fragment_inform_zapysch;
import ru.ejevikaapp.gm_android.R;

public class Activity_started_projects extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    String id_cl;

    int i = 0;

    String SAVED_ID="";
    Double S,P;

    DBHelper dbHelper;

    ArrayList id_calc = new ArrayList();
    ArrayList title_calc = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_production_tab);

        mViewPager = (ViewPager) findViewById(R.id.container);
        // setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onResume(){
        super.onResume();
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Frag_general_started_projects(), "Общее");
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
        SharedPreferences SPI = getSharedPreferences("id_cl", MODE_PRIVATE);
        id_cl = SPI.getString(SAVED_ID,"");

        // Toast toast = Toast.makeText(getApplicationContext(),
        //         id_cl, Toast.LENGTH_SHORT);
        // toast.show();

        i=0;
        id_calc.add("");
        title_calc.add("");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT _id, calculation_title, n4, n5 "
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
        Log.d("mLog onStop ac" , end);

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