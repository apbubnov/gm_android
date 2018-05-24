package ru.ejevikaapp.gm_android.Dealer;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_price_components extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    ArrayList<Select_work> components = new ArrayList<>();
    ArrayList<String> strComponents = new ArrayList<>();
    ArrayList<Select_work> components_option = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_components);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT _id, title "
                + "FROM rgzbn_gm_ceiling_components " +
                "order by title";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String title = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    sqlQuewy = "SELECT * "
                            + "FROM rgzbn_gm_ceiling_components_option " +
                            "where component_id = ? and count > 0";
                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{id});
                    if (c1 != null) {
                        if (c1.moveToFirst()) {

                            Log.d("mLog", id);
                            components.add(new Select_work(id, null, null, title, null));
                            strComponents.add(title);

                        }
                    }
                    c1.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        //String[] stockArr = new String[components.size()];
        //stockArr = components.toArray(stockArr);

        String[] countries = strComponents.toArray(new String[strComponents.size()]);
        final ListView listView = (ListView) findViewById(R.id.ListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, countries);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                components_option.clear();

                SharedPreferences SP_end = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id = SP_end.getString("", "");

                String select = String.valueOf(((TextView) itemClicked).getText());
                int id_select;

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components " +
                        "where title LIKE '" + select + "'";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            id_select = c.getInt(c.getColumnIndex(c.getColumnName(0)));

                            sqlQuewy = "select _id, title, price "
                                    + "FROM rgzbn_gm_ceiling_components_option " +
                                    "where component_id = ? ";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_select)});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    do {
                                        Integer id_comp = cc.getInt(cc.getColumnIndex(cc.getColumnName(0)));
                                        String title = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));
                                        Double price = cc.getDouble(cc.getColumnIndex(cc.getColumnName(2)));

                                        boolean canvases_price = false;
                                        boolean boolean_canvases = false;

                                        sqlQuewy = "select * "
                                                + "FROM rgzbn_gm_ceiling_canvases_dealer_price " +
                                                "where user_id = ? ";
                                        Cursor c1 = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
                                        if (c1 != null) {
                                            if (c1.moveToFirst()) {
                                                canvases_price = true;
                                                boolean_canvases = true;
                                            }
                                        }
                                        c1.close();

                                        if (boolean_canvases) {
                                            if (canvases_price) {
                                                price = HelperClass.new_price("components", user_id, id_comp, price, Activity_price_components.this);
                                            } else {
                                                price = HelperClass.new_price("canvases", "1", id_comp, price, Activity_price_components.this);
                                            }
                                        }

                                        double dealer_components_margin = 0;
                                        sqlQuewy = "select dealer_components_margin "
                                                + "FROM rgzbn_gm_ceiling_dealer_info " +
                                                "where dealer_id = ? ";
                                        c1 = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
                                        if (c1 != null) {
                                            if (c1.moveToFirst()) {
                                                dealer_components_margin = c1.getDouble(c1.getColumnIndex(c1.getColumnName(0)));
                                            }
                                        }
                                        c1.close();

                                        dealer_components_margin = price * 100 / (100 - dealer_components_margin);

                                        components_option.add(new Select_work(null, String.valueOf(price), null,
                                                select + " " + title, String.valueOf(dealer_components_margin)));
                                    } while (cc.moveToNext());
                                }
                            }
                            cc.close();

                        } while (c.moveToNext());
                    }
                }
                c.close();

                BindDictionary<Select_work> dict = new BindDictionary<>();
                dict.addStringField(R.id.title, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getName();
                    }
                });
                dict.addStringField(R.id.price, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getTime();
                    }
                });
                dict.addStringField(R.id.price_cl, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getN5();
                    }
                });

                LayoutInflater li = LayoutInflater.from(Activity_price_components.this);
                View promptsView = li.inflate(R.layout.view_components_option, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Activity_price_components.this);
                mDialogBuilder.setView(promptsView);
                final ListView listView = (ListView) promptsView.findViewById(R.id.List_mounting);

                final FunDapter adapter = new FunDapter(Activity_price_components.this, components_option, R.layout.view_components_option_item, dict);
                listView.setAdapter(adapter);

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();

            }
        });


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
}