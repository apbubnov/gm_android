package ru.ejevikaapp.gm_android;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.ejevikaapp.gm_android.Class.HelperClass;

public class ActivityEstimate extends AppCompatActivity {

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
        String canvases= "", texture= "", rb_vstavka= "", n1= "", n3= "", offcut_square= "", width_final= "", final_mount= "", final_comp= "";
        Integer n2 = 0;
        double S = 0, P = 0, n7 = 0, n8 = 0, n9 = 0, n11 = 0, n12 = 0, n16 = 0, n17 = 0, n18 = 0, n19 = 0, n20 = 0, n21 = 0,
                n24 = 0, n27 = 0, n28 = 0, n30 = 0, n31 = 0, n32 = 0, dop_krepezh = 0, height = 0;

        String sqlQuewy = "SELECT calculation_title, n1, n2, n3, n4, n5, n6, n7, n8, n9," +
                " n10, n11, n12, n16, n17, n18, n19, n20, n21, n24," +
                " n25, dop_krepezh, calc_image, n27, color, offcut_square, discount, n28, n30, original_sketch, n31, n32, height, " +
                " extra_components, extra_mounting "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        Cursor k = db.rawQuery(sqlQuewy, new String[]{id_calculation});

        if (k != null) {
            if (k.moveToFirst()) {
                String calculation_title = k.getString(k.getColumnIndex(k.getColumnName(0)));
                n1 = k.getString(k.getColumnIndex(k.getColumnName(1)));
                n2 = Integer.parseInt(k.getString(k.getColumnIndex(k.getColumnName(2))));
                n3 = k.getString(k.getColumnIndex(k.getColumnName(3)));
                S = k.getDouble(k.getColumnIndex(k.getColumnName(4)));
                P = k.getDouble(k.getColumnIndex(k.getColumnName(5)));
                n6 = k.getInt(k.getColumnIndex(k.getColumnName(6)));
                n7 = k.getDouble(k.getColumnIndex(k.getColumnName(7)));
                n8 = k.getDouble(k.getColumnIndex(k.getColumnName(8)));
                n9 = k.getDouble(k.getColumnIndex(k.getColumnName(9)));
                n11 = k.getDouble(k.getColumnIndex(k.getColumnName(11)));
                n12 = k.getDouble(k.getColumnIndex(k.getColumnName(12)));
                n16 = k.getDouble(k.getColumnIndex(k.getColumnName(13)));
                n17 = k.getDouble(k.getColumnIndex(k.getColumnName(14)));
                n18 = k.getDouble(k.getColumnIndex(k.getColumnName(15)));
                n19 = k.getDouble(k.getColumnIndex(k.getColumnName(16)));
                n20 = k.getDouble(k.getColumnIndex(k.getColumnName(17)));
                n21 = k.getDouble(k.getColumnIndex(k.getColumnName(18)));
                n24 = k.getDouble(k.getColumnIndex(k.getColumnName(19)));
                dop_krepezh = k.getDouble(k.getColumnIndex(k.getColumnName(21)));
                //calc_image = k.getString(k.getColumnIndex(k.getColumnName(22)));
                n27 = k.getDouble(k.getColumnIndex(k.getColumnName(23)));
                //color = k.getString(k.getColumnIndex(k.getColumnName(24)));
                offcut_square = k.getString(k.getColumnIndex(k.getColumnName(25)));
                //discount = k.getString(k.getColumnIndex(k.getColumnName(26)));
                n28 = k.getDouble(k.getColumnIndex(k.getColumnName(27)));
                n30 = k.getDouble(k.getColumnIndex(k.getColumnName(28)));
                //original_sketch = k.getString(k.getColumnIndex(k.getColumnName(29)));
                n31 = k.getDouble(k.getColumnIndex(k.getColumnName(30)));
                n32 = k.getDouble(k.getColumnIndex(k.getColumnName(31)));
                height = k.getDouble(k.getColumnIndex(k.getColumnName(32)));
                final_comp = k.getString(k.getColumnIndex(k.getColumnName(33)));
                final_mount = k.getString(k.getColumnIndex(k.getColumnName(34)));
            }
        }

        //sqlQuewy = "select hex "
        //        + "FROM rgzbn_gm_ceiling_canvases " +
        //        "where _id = ? ";
        //Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n3)});
        //if (c != null) {
        //    if (c.moveToFirst()) {
        //        do {
        //            String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
        //            btn_color_canvases.setBackgroundColor(Color.parseColor("#" + hex));

        //        } while (c.moveToNext());
        //    }
        //}
        //c.close();

        JSONObject result = HelperClass.calculation(this, dealer_id_str, colorIndex, id_calculation, canvases, texture, rb_vstavka,
                n1, n2, n3, S, P, n6, n7, n8, n9,
                n11, n12, n16, n17, n18, n19,
                n20, n21, n24, n27, n28,
                n30, n31, n32, dop_krepezh, height, offcut_square, width_final,
                final_comp, final_mount);

        TextView text = (TextView) findViewById(R.id.text);

        try {
            JSONObject project = result.getJSONObject("estimate");
            String components = project.getString("components");

            text.setText(String.valueOf(components));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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