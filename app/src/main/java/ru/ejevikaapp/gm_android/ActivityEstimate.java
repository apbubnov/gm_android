package ru.ejevikaapp.gm_android;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.pixplicity.sharp.Sharp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Class.ClassEstimate;

public class ActivityEstimate extends AppCompatActivity {

    int circle_count, square_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String id_calculation = getIntent().getStringExtra("id_calculation");  // id_calculation

        SharedPreferences SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        String dealer_id_str = SP.getString("", "");

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int colorIndex = 0, n6 = 0;
        String canvases = "", texture = "", rb_vstavka = "", n1 = "", n3 = "", offcut_square = "", width_final = "", final_mount = "", final_comp = "", calc_image = "",
                calcData = "", calculation_title = "";
        Integer n2 = 0;
        double S = 0, P = 0, n7 = 0, n8 = 0, n9 = 0, n11 = 0, n12 = 0, n16 = 0, n17 = 0, n18 = 0, n19 = 0, n20 = 0, n21 = 0,
                n24 = 0, n27 = 0, n28 = 0, n30 = 0, n31 = 0, n32 = 0, dop_krepezh = 0, height = 0;

        String sqlQuewy = "SELECT calculation_title, n3, n4, n5, n6, n7, n8, n9," +
                " n10, n11, n12, n16, n17, n18, n19, n20, n21, n24," +
                " n25, dop_krepezh, calc_image, n27, color, offcut_square, discount, n28, n30, original_sketch, n31, n32, height, " +
                " extra_components, extra_mounting, calc_data "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        Cursor k = db.rawQuery(sqlQuewy, new String[]{id_calculation});

        if (k != null) {
            if (k.moveToFirst()) {
                calculation_title = k.getString(k.getColumnIndex(k.getColumnName(0)));
                n3 = k.getString(k.getColumnIndex(k.getColumnName(1)));
                S = k.getDouble(k.getColumnIndex(k.getColumnName(2)));
                P = k.getDouble(k.getColumnIndex(k.getColumnName(3)));
                n6 = k.getInt(k.getColumnIndex(k.getColumnName(4)));
                n7 = k.getDouble(k.getColumnIndex(k.getColumnName(5)));
                n8 = k.getDouble(k.getColumnIndex(k.getColumnName(6)));
                n9 = k.getDouble(k.getColumnIndex(k.getColumnName(7)));
                n11 = k.getDouble(k.getColumnIndex(k.getColumnName(9)));
                n12 = k.getDouble(k.getColumnIndex(k.getColumnName(10)));
                n16 = k.getDouble(k.getColumnIndex(k.getColumnName(1)));
                n17 = k.getDouble(k.getColumnIndex(k.getColumnName(12)));
                n18 = k.getDouble(k.getColumnIndex(k.getColumnName(13)));
                n19 = k.getDouble(k.getColumnIndex(k.getColumnName(14)));
                n20 = k.getDouble(k.getColumnIndex(k.getColumnName(15)));
                n21 = k.getDouble(k.getColumnIndex(k.getColumnName(16)));
                n24 = k.getDouble(k.getColumnIndex(k.getColumnName(17)));
                dop_krepezh = k.getDouble(k.getColumnIndex(k.getColumnName(19)));
                calc_image = k.getString(k.getColumnIndex(k.getColumnName(20)));
                n27 = k.getDouble(k.getColumnIndex(k.getColumnName(21)));
                //color = k.getString(k.getColumnIndex(k.getColumnName(24)));
                offcut_square = k.getString(k.getColumnIndex(k.getColumnName(23)));
                //discount = k.getString(k.getColumnIndex(k.getColumnName(26)));
                n28 = k.getDouble(k.getColumnIndex(k.getColumnName(25)));
                n30 = k.getDouble(k.getColumnIndex(k.getColumnName(26)));
                //original_sketch = k.getString(k.getColumnIndex(k.getColumnName(29)));
                n31 = k.getDouble(k.getColumnIndex(k.getColumnName(28)));
                n32 = k.getDouble(k.getColumnIndex(k.getColumnName(29)));
                height = k.getDouble(k.getColumnIndex(k.getColumnName(30)));
                final_comp = k.getString(k.getColumnIndex(k.getColumnName(31)));
                final_mount = k.getString(k.getColumnIndex(k.getColumnName(32)));
                calcData = k.getString(k.getColumnIndex(k.getColumnName(33)));
            }
        }

        TextView nameCeiling = (TextView) findViewById(R.id.nameCeiling);
        nameCeiling.setText("Потолок: " + calculation_title);

        countingComponents(id_calculation);

        try {
            int id_m = 0;
            int id_t = 0;
            sqlQuewy = "select manufacturer_id, texture_id "
                    + "FROM rgzbn_gm_ceiling_canvases " +
                    "where _id = ?";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{n3});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        id_m = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                        id_t = c.getInt(c.getColumnIndex(c.getColumnName(1)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sqlQuewy = "select name, country "
                    + "FROM rgzbn_gm_ceiling_canvases_manufacturers " +
                    "where _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_m)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        canvases = c.getString(c.getColumnIndex(c.getColumnName(0))) + " "
                                + c.getString(c.getColumnIndex(c.getColumnName(1)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            sqlQuewy = "select texture_title "
                    + "FROM rgzbn_gm_ceiling_textures " +
                    "where _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_t)});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        texture = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            if (id_t < 29) {
                n1 = "28";
            } else {
                n1 = "29";
            }

        } catch (Exception e) {
        }

        if (n6 > 0){
            rb_vstavka = "1";
        } else {
            rb_vstavka = "0";
        }

        JSONObject result = HelperClass.calculation(this, dealer_id_str, colorIndex, id_calculation, canvases, texture, rb_vstavka,
                n1, n2, n3, S, P, n6, n7, n8, n9,
                n11, n12, n16, n17, n18, n19,
                n20, n21, n24, n27, n28,
                n30, n31, n32, dop_krepezh, height, offcut_square, width_final,
                final_comp, final_mount, circle_count, square_count);

        ImageView id_image = (ImageView) findViewById(R.id.imageView);
        TextView textCalcData = (TextView) findViewById(R.id.calcData);

        if (calc_image.length() < 10) {
            id_image.setVisibility(View.GONE);
            textCalcData.setVisibility(View.GONE);
        } else {
            try {
                Sharp.loadString(calc_image).into(id_image);
                textCalcData.setText(calcData);
            } catch (Exception e) {
            }
        }

        String components = "";
        try {
            JSONObject project = result.getJSONObject("estimate");
            components = project.getString("components");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] subStr;
        FunDapter Fun_adapter;
        BindDictionary<ClassEstimate> dict = new BindDictionary<>();

        if (components.equals("")) {
            LinearLayout linearComp = (LinearLayout) findViewById(R.id.linearComp);
            linearComp.setVisibility(View.GONE);
        } else {

            ListView listComponents = (ListView) findViewById(R.id.listComponents);

            final ArrayList<ClassEstimate> estimate_mas = new ArrayList<>();

            subStr = components.split(" \\| ");

            for (int j = 0; j < subStr.length; j++) {

                String[] subRow;
                subRow = subStr[j].split(";");

                String title1 = "";
                String title2 = "";
                String title3 = "";
                String title4 = "";
                for (int in = 0; in < 4; in++) {

                    if (in == 0) {
                        title1 = subRow[in].substring(15, subRow[in].length());
                    }
                    if (in == 1) {
                        title2 = subRow[in].substring(14, subRow[in].length());
                    }
                    if (in == 2) {
                        title3 = subRow[in].substring(13, subRow[in].length());
                    }
                    if (in == 3) {
                        title4 = subRow[in].substring(18, subRow[in].length());
                    }
                }

                ClassEstimate fix_class = new ClassEstimate(title1, title2, title3, title4);
                estimate_mas.add(fix_class);
            }

            dict = new BindDictionary<>();

            dict.addStringField(R.id.name, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getName();
                }
            });
            dict.addStringField(R.id.price, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getPrice();
                }
            });
            dict.addStringField(R.id.count, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getCount();
                }
            });
            dict.addStringField(R.id.totalPrice, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getTotalPrice();
                }
            });

            Fun_adapter = new FunDapter(this, estimate_mas, R.layout.activity_estimate2, dict);
            listComponents.setAdapter(Fun_adapter);

            TextView totalTextComp = (TextView) findViewById(R.id.totalComp);

            String totalComp = "";
            try {
                JSONObject project = result.getJSONObject("project");
                totalComp = project.getString("dealer_components_sum");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            totalTextComp.setText(String.valueOf(Math.round(Double.valueOf(totalComp) * 100d) / 100d));

        }
        String mounters = "";
        try {
            JSONObject project = result.getJSONObject("estimate");
            mounters = project.getString("mounting");
            Log.d("mLog", String.valueOf(mounters));
            //text.setText(String.valueOf(components));
        } catch (JSONException e) {
            Log.d("mLog", String.valueOf(e));
        }

        ListView listMounters = (ListView) findViewById(R.id.listMounters);

        if (mounters.equals("")) {
            LinearLayout linearMount = (LinearLayout) findViewById(R.id.linearMount);
            linearMount.setVisibility(View.GONE);
        } else {
            final ArrayList<ClassEstimate> estimate_mount = new ArrayList<>();
            subStr = mounters.split(" \\| ");
            for (int j = 0; j < subStr.length; j++) {

                String[] subRow;
                subRow = subStr[j].split(";");

                String title1 = "";
                String title2 = "";
                String title3 = "";
                String title4 = "";
                for (int in = 0; in < 4; in++) {

                    if (in == 0) {
                        title1 = subRow[in].substring(15, subRow[in].length());
                    }
                    if (in == 1) {
                        title2 = subRow[in].substring(14, subRow[in].length());
                    }
                    if (in == 2) {
                        title3 = subRow[in].substring(13, subRow[in].length());
                    }
                    if (in == 3) {
                        title4 = subRow[in].substring(18, subRow[in].length());
                    }
                }

                ClassEstimate fix_class = new ClassEstimate(title1, title2, title3, title4);
                estimate_mount.add(fix_class);
            }

            dict.addStringField(R.id.name, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getName();
                }
            });
            dict.addStringField(R.id.price, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getPrice();
                }
            });
            dict.addStringField(R.id.count, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getCount();
                }
            });
            dict.addStringField(R.id.totalPrice, new StringExtractor<ClassEstimate>() {
                @Override
                public String getStringValue(ClassEstimate nc, int position) {
                    return nc.getTotalPrice();
                }
            });

            Fun_adapter = new FunDapter(this, estimate_mount, R.layout.activity_estimate2, dict);
            listMounters.setAdapter(Fun_adapter);

            TextView totalTextMount = (TextView) findViewById(R.id.totalMount);


            String totalMount = "";
            try {
                JSONObject project = result.getJSONObject("project");
                totalMount = project.getString("total_with_dealer_margin");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            totalTextMount.setText(String.valueOf(Math.round(Double.valueOf(totalMount) * 100d) / 100d));

        }

    }

    void countingComponents(String id_calculation) {

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "SELECT * "
                + "FROM rgzbn_gm_ceiling_fixtures" +
                " WHERE calculation_id = ?";
        Cursor cursor = db.rawQuery(sqlQuewy, new String[]{id_calculation});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int vidIndex = cursor.getColumnIndex(cursor.getColumnName(3));
                int kol_voIndex = cursor.getColumnIndex(cursor.getColumnName(2));

                do {
                    String vid_c = cursor.getString(vidIndex);
                    if (vid_c.equals("2")) {
                        circle_count += cursor.getInt(kol_voIndex);
                    } else if (vid_c.equals("3")) {
                        square_count += cursor.getInt(kol_voIndex);
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

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