package ru.ejevikaapp.gm_android.Dealer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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

public class Activity_price_canvases extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    private List<String> GroupsList = new ArrayList<String>();
    private List<String> ChildList = new ArrayList<String>();
    ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
    ArrayList<Select_work> sel_work = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_canvases);
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expListView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT _id, texture_title "
                + "FROM rgzbn_gm_ceiling_textures";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                    String title = c.getString(c.getColumnIndex(c.getColumnName(1)));

                    if (id == 28) {
                    } else {
                        GroupsList.add(title);

                        sqlQuewy = "SELECT manufacturer_id "
                                + "FROM rgzbn_gm_ceiling_canvases " +
                                "where texture_id = ? " +
                                "group by manufacturer_id";
                        Cursor c1 = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                        if (c1 != null) {
                            if (c1.moveToFirst()) {
                                do {
                                    int idd = c1.getInt(c1.getColumnIndex(c1.getColumnName(0)));

                                    sqlQuewy = "SELECT name "
                                            + "FROM rgzbn_gm_ceiling_canvases_manufacturers " +
                                            "where _id = ?";
                                    Cursor c2 = db.rawQuery(sqlQuewy, new String[]{String.valueOf(idd)});
                                    if (c2 != null) {
                                        if (c2.moveToFirst()) {
                                            String name = c2.getString(c2.getColumnIndex(c2.getColumnName(0)));
                                            ChildList.add(name);
                                        }
                                    }
                                    c2.close();

                                } while (c1.moveToNext());
                            }
                        }
                        c1.close();
                    }

                    child();
                    ChildList.clear();
                } while (c.moveToNext());
            }
        }
        c.close();

        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        for (String group : GroupsList) {
            map = new HashMap<>();
            map.put("groupName", group);
            groupDataList.add(map);
        }

        String groupFrom[] = new String[]{"groupName"};
        int groupTo[] = new int[]{android.R.id.text1};

        String childFrom[] = new String[]{"childName"};
        int childTo[] = new int[]{android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expListView);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                SharedPreferences SP_end = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id = SP_end.getString("", "");

                sel_work.clear();

                int group = groupPosition + 1;
                int child = childPosition;

                String sqlQuewy = "select manufacturer_id "
                        + "FROM rgzbn_gm_ceiling_canvases " +
                        "where texture_id = ? " +
                        "group by manufacturer_id";
                Cursor cc = db.rawQuery(sqlQuewy, new String[]{String.valueOf(group)});
                if (cc != null) {
                    if (cc.moveToPosition(child)) {
                        String manufacturer_id = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));

                        sqlQuewy = "select width, color_id, price, _id "
                                + "FROM rgzbn_gm_ceiling_canvases " +
                                "where texture_id = ? and manufacturer_id = ?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(group), manufacturer_id});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    String width = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    String color_id = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                    Double price = c.getDouble(c.getColumnIndex(c.getColumnName(2)));
                                    Integer id_n3 = c.getInt(c.getColumnIndex(c.getColumnName(3)));

                                    String title = "";

                                    double dealer_canvases_margin = 0.0;

                                    sqlQuewy = "select title, hex "
                                            + "FROM rgzbn_gm_ceiling_colors " +
                                            "where _id = ?";
                                    Cursor c1 = db.rawQuery(sqlQuewy, new String[]{String.valueOf(color_id)});
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            do {
                                                title = c1.getString(c1.getColumnIndex(c1.getColumnName(0)));
                                                String hex = c1.getString(c1.getColumnIndex(c1.getColumnName(1)));

                                            } while (c1.moveToNext());
                                        }
                                    }
                                    c1.close();

                                    boolean canvases_price = false;
                                    boolean boolean_canvases = false;

                                    sqlQuewy = "select * "
                                            + "FROM rgzbn_gm_ceiling_canvases_dealer_price " +
                                            "where user_id = ? ";
                                    c1 = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            canvases_price = true;
                                            boolean_canvases = true;
                                        }
                                    }
                                    c1.close();

                                    Log.d("mLog", user_id + " " + id_n3 +  " " + price);

                                    if (boolean_canvases) {
                                        if (canvases_price) {
                                            price = HelperClass.new_price("canvases", user_id, id_n3, price, Activity_price_canvases.this);
                                        } else {
                                            price = HelperClass.new_price("canvases", "1", id_n3, price, Activity_price_canvases.this);
                                        }
                                    }

                                    sqlQuewy = "select dealer_canvases_margin "
                                            + "FROM rgzbn_gm_ceiling_dealer_info " +
                                            "where dealer_id = ? ";
                                    c1 = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
                                    if (c1 != null) {
                                        if (c1.moveToFirst()) {
                                            dealer_canvases_margin = c1.getDouble(c1.getColumnIndex(c1.getColumnName(0)));
                                        }
                                    }
                                    c1.close();

                                    dealer_canvases_margin = price * 100 / (100 - dealer_canvases_margin);

                                    sel_work.add(new Select_work(null, String.valueOf(price), title, width, String.valueOf(dealer_canvases_margin)));

                                } while (c.moveToNext());
                            }
                        }
                        c.close();
                    }
                }
                cc.close();

                BindDictionary<Select_work> dict = new BindDictionary<>();
                dict.addStringField(R.id.c_number, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getName();
                    }
                });
                dict.addStringField(R.id.c_address, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getAddres();
                    }
                });
                dict.addStringField(R.id.c_price, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getTime();
                    }
                });
                dict.addStringField(R.id.c_income, new StringExtractor<Select_work>() {
                    @Override
                    public String getStringValue(Select_work nc, int position) {
                        return nc.getN5();
                    }
                });

                LayoutInflater li = LayoutInflater.from(Activity_price_canvases.this);
                View promptsView = li.inflate(R.layout.activity_mounting_day, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Activity_price_canvases.this);
                mDialogBuilder.setView(promptsView);
                final TextView textView1 = (TextView) promptsView.findViewById(R.id.c_number);
                textView1.setText("Hаименованиe");
                final TextView textView2 = (TextView) promptsView.findViewById(R.id.c_address);
                textView2.setText("Цвет");
                final TextView textView3 = (TextView) promptsView.findViewById(R.id.c_price);
                textView3.setText("Себестоимость");
                final TextView textView4 = (TextView) promptsView.findViewById(R.id.c_income);
                textView4.setText("Цена для клиента");
                final ListView listView = (ListView) promptsView.findViewById(R.id.List_mounting);

                final FunDapter adapter = new FunDapter(Activity_price_canvases.this, sel_work, R.layout.clients_item44, dict);
                listView.setAdapter(adapter);

                //mDialogBuilder
                //        .setCancelable(false)
                //        .setPositiveButton("OK",
                //                new DialogInterface.OnClickListener() {
                //                    public void onClick(DialogInterface dialog, int id) {

                //                    }
                //                });

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void child() {
        Map<String, String> map;

        ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
        for (String month : ChildList) {
            map = new HashMap<>();
            map.put("childName", month); // название месяца
            сhildDataItemList.add(map);
        }
        // добавляем в коллекцию коллекций
        сhildDataList.add(сhildDataItemList);
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