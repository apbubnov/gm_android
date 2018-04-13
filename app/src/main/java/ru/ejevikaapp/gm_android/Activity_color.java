package ru.ejevikaapp.gm_android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Activity_color extends Activity{

    LinearLayout mainL;
    LinearLayout.LayoutParams titleViewParams;
    DBHelper dbHelper;
    Button btn;
    int i=0;
    private ArrayList <Button> BtnList = new ArrayList<Button>();
    ArrayList<Integer> color_array = new ArrayList<Integer>();
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        mainL = (LinearLayout) findViewById(R.id.linear_lay);

        titleViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleViewParams.setMargins(0,15,0,0);

        String text_title="";
        String color = getIntent().getStringExtra("texture_id");
        String component_id = getIntent().getStringExtra("component_id");

        if (color != null){

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sqlQuewy = "SELECT color_id "
                    + "FROM rgzbn_gm_ceiling_canvases" +
                    " WHERE texture_id = ? and count > 0";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{color});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String color_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        sqlQuewy = "SELECT title, hex, _id "
                                + "FROM rgzbn_gm_ceiling_colors" +
                                " where _id = ?";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{color_id});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    String title = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    String hex = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));
                                    String id = cc.getString(cc.getColumnIndex(cc.getColumnName(2)));
                                    btn(id, title, hex);
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();


                    } while (c.moveToNext());
                }
            }
            c.close();
        }

        if (component_id != null){

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sqlQuewy = "SELECT title "
                    + "FROM rgzbn_gm_ceiling_components_option" +
                    " WHERE component_id = ? ";
            Cursor c = db.rawQuery(sqlQuewy, new String[]{component_id});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String color_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                        try {
                            Integer.parseInt(color_id);

                            sqlQuewy = "SELECT title, hex "
                                    + "FROM rgzbn_gm_ceiling_colors" +
                                    " WHERE title = ? ";
                            Cursor cc = db.rawQuery(sqlQuewy, new String[]{color_id});
                            if (cc != null) {
                                if (cc.moveToFirst()) {
                                    Log.d("mLog color2", color_id);
                                    String title = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                    String hex = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));
                                    String id = cc.getString(cc.getColumnIndex(cc.getColumnName(2)));
                                    btn_vs(title, hex);
                                }
                            }
                            cc.close();

                        } catch (Exception e) {
                        }


                    } while (c.moveToNext());
                }
            }
            c.close();

        }

    }

    View.OnClickListener getColor = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();

            Button btnn = BtnList.get(editId);

            SharedPreferences SP = getSharedPreferences("color_title", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", btnn.getText().toString());
            ed.commit();

            SP = getSharedPreferences("color_title_id", MODE_PRIVATE);
            ed = SP.edit();
            ed.putString("", String.valueOf(color_array.get(editId)));
            ed.commit();

             finish();
        }
    };

    void btn(String id, String title, String hex) {
        btn = new Button(this);
        BtnList.add(i, btn);
        btn.setId(i++);
        color_array.add(Integer.valueOf(id));
        btn.setLayoutParams(titleViewParams);
        btn.setBackgroundColor(Color.parseColor("#"+hex));
        btn.setText(title);
        btn.setOnClickListener(getColor);
        mainL.addView(btn);
    }

    View.OnClickListener getColor_vs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int editId = v.getId();
            Button btnn = BtnList.get(editId);
            SharedPreferences SP = getSharedPreferences("color_title_vs", MODE_PRIVATE);
            SharedPreferences.Editor ed = SP.edit();
            ed.putString("", btnn.getText().toString());
            ed.commit();
            finish();
        }
    };

    void btn_vs(String title, String hex) {
        btn = new Button(this);
        BtnList.add(i, btn);
        btn.setId(i++);
        btn.setLayoutParams(titleViewParams);
        btn.setBackgroundColor(Color.parseColor("#"+hex));
        btn.setText(title);
        btn.setOnClickListener(getColor_vs);
        mainL.addView(btn);
    }
}