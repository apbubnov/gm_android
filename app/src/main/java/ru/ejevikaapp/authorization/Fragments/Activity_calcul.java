package ru.ejevikaapp.authorization.Fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Activity_add_diffuzor;
import ru.ejevikaapp.authorization.Activity_add_kupit_cornice;
import ru.ejevikaapp.authorization.Activity_add_kupit_svetiln;
import ru.ejevikaapp.authorization.Activity_add_other_comp;
import ru.ejevikaapp.authorization.Activity_add_other_work;
import ru.ejevikaapp.authorization.Activity_add_profile;
import ru.ejevikaapp.authorization.Activity_add_svetiln;
import ru.ejevikaapp.authorization.Activity_add_truby;
import ru.ejevikaapp.authorization.Activity_add_vent;
import ru.ejevikaapp.authorization.Activity_color;
import ru.ejevikaapp.authorization.Activity_draft;
import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Activity_calcul extends AppCompatActivity implements View.OnClickListener {


    Button chertezh, btn_texture, btn_width, btn_vstavka, btn_light, btn_lustr, btn_add_svetiln,
            btn_add_kupit_svetiln, btn_karniz, btn_rb_v;
    Button btn_add_truby, btn_fire, btn_add_vent, btn_add_diff, btn_color_canvases, btn_save, btn_add_cornice,
            btn_add_comp, btn_add_other_work, btn_cancel, btn_calculate, btn_add_profile,
            btn_wall, btn_fasteners, btn_in_cut, btn_diff_acc, btn_separator, btn_soaring_ceiling, btn_mount_wall, btn_mount_granite, btn_cabling, btn_bond_beam;

    TextView area, perimetr, corners, text_calculate;

    ArrayAdapter<String> adapter;

    View view;

    public int colorIndex = 0;

    DBHelper dbHelper;

    ArrayList s_c = new ArrayList();
    ArrayList s_t = new ArrayList();

    String texture, canvases, texture_id, s_setMessage, s_setMessage1, s_setTitle, s_setdrawable, s_setdrawable1, square_obr, s_sp5 = "", s_spa = "", id_cl, calc;
    String width_final = "", imag_cut = "", imag = "", rb_vstavka = "0", n2, n3, lines_length, gager_id = "", dealer_id_str = "",
            rb_baget = "", original_sketch = "";

    SharedPreferences sPref, SP4, SP5, SP9, SPI, SPSO, SP, SPW;

    final String SAVED_TEXT = "saved_text";
    final String SAVED_N4 = "";
    final String SAVED_N5 = "";
    final String SAVED_N9 = "";
    final String SAVED_I = "";
    final String SAVED_SO = "";
    final String SAVED_W = "";
    final String n1 = "28";

    String SAVED_KP = "";
    String SAVED_WP = "";
    String SAVED_DP = "";
    String SAVED_PT_P = "";
    String SAVED_CODE = "";
    String SAVED_ALFAVIT = "";

    Integer gm_can_marg, gm_comp_marg, gm_mount_marg, dealer_can_marg, dealer_comp_marg, dealer_mount_marg, count_svet,
            count_vent, count_electr, count_diffus, count_pipes, gager_id_int, id_n3;

    Double S = 0.0, P = 0.0, Angle = 0.0;

    ImageView image;

    CheckBox RB_karniz;

    EditText lustr, ed_fire, karniz, name_project, ed_wall, ed_cabling, ed_separator, ed_fasteners, ed_curved, ed_in_cut, mount_wall,
            mount_granite, ed_diff_acc, ed_discount, distance, distance_kol, bond_beam, soaring_ceiling;

    boolean rb_v = false, rb_k = false, calculat = false, btn_color_canvases_visible = false, mounting = true;

    ArrayList<Double> component_count = new ArrayList();    // component_option
    ArrayList canvases_data = new ArrayList();
    ArrayList mounting_data = new ArrayList();
    ArrayList<Integer> results = new ArrayList();
    ArrayList component_item = new ArrayList();

    int items_9, items_5, items_11, items_vstavka_bel, items_vstavka, items_10, items_16, items_556, items_4, items_58, items_3,
            items_2, items_1, items_8, items_6, items_14, items_430, items_35, items_360, int_rb_v, int_rb_k, items_236,
            items_239, items_559, items_38, items_495, items_233, items_659, items_660;

    int[] n13_count;
    String[] n13_type;
    int[] n13_size_id;
    String[] n13_size;

    int[] n22_count;
    String[] n22_type;
    int[] n22_size_id;
    String[] n22_size;

    int[] n14_count;
    String[] n14_type;
    int[] n14_size_id;
    String[] n14_size;

    int[] n23_count;
    String[] n23_type;
    int[] n23_size_id;
    String[] n23_size;

    int[] n15_count;
    String[] n15_type;
    int[] n15_size_id;
    String[] n15_size;

    int[] n26_count;
    int[] n26_type_id;
    int[] n26_size_id;

    double[] ar1_size;
    double[] ar2_size;
    double[] ar2_size2;

    int[] n29_count;
    int[] n29_type;

    RadioButton rb_v_white, rb_v_color, rb_v_no, rb_m_yes, rb_m_no, rb_b_no, rb_b_potol, rb_b_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul);

        SharedPreferences SP = getSharedPreferences("gager_id", MODE_PRIVATE);
        gager_id = SP.getString("", "");
        gager_id_int = Integer.parseInt(gager_id) * 1000000;

        SP = getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id_str = SP.getString("", "");

        chertezh = (Button) findViewById(R.id.chertezh);
        btn_texture = (Button) findViewById(R.id.btn_texture);
        btn_width = (Button) findViewById(R.id.btn_width);
        btn_vstavka = (Button) findViewById(R.id.btn_vstavka);
        btn_light = (Button) findViewById(R.id.btn_light);
        btn_lustr = (Button) findViewById(R.id.btn_lustr);
        btn_add_svetiln = (Button) findViewById(R.id.btn_add_svetiln);
        btn_add_kupit_svetiln = (Button) findViewById(R.id.btn_add_kupit_svetiln);
        btn_karniz = (Button) findViewById(R.id.btn_karniz);
        btn_add_truby = (Button) findViewById(R.id.btn_add_truby);
        btn_fire = (Button) findViewById(R.id.btn_fire);
        btn_add_vent = (Button) findViewById(R.id.btn_add_vent);
        btn_add_diff = (Button) findViewById(R.id.btn_add_diff);
        btn_color_canvases = (Button) findViewById(R.id.color_canvases);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_add_cornice = (Button) findViewById(R.id.btn_add_cornice);
        //btn_add_comp = (Button) findViewById(R.id.btn_add_other_comp);
        //btn_add_other_work = (Button) findViewById(R.id.btn_add_other_work);
        //btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_rb_v = (Button) findViewById(R.id.btn_rb_v);
        btn_calculate = (Button) findViewById(R.id.btn_calculate);
        btn_add_profile = (Button) findViewById(R.id.btn_add_profile);
        btn_wall = (Button) findViewById(R.id.btn_wall);
        btn_fasteners = (Button) findViewById(R.id.btn_fasteners);
        btn_in_cut = (Button) findViewById(R.id.btn_in_cut);
        btn_diff_acc = (Button) findViewById(R.id.btn_diff_acc);
        btn_separator = (Button) findViewById(R.id.btn_separator);
        btn_soaring_ceiling = (Button) findViewById(R.id.btn_soaring_ceiling);
        btn_mount_wall = (Button) findViewById(R.id.btn_mount_wall);
        btn_mount_granite = (Button) findViewById(R.id.btn_mount_granite);
        btn_cabling = (Button) findViewById(R.id.btn_cabling);
        btn_bond_beam = (Button) findViewById(R.id.btn_bond_beam);

        chertezh.setOnClickListener(this);
        btn_texture.setOnClickListener(this);
        btn_width.setOnClickListener(this);
        btn_vstavka.setOnClickListener(this);
        btn_light.setOnClickListener(this);
        btn_lustr.setOnClickListener(this);
        btn_add_svetiln.setOnClickListener(this);
        btn_add_kupit_svetiln.setOnClickListener(this);
        btn_karniz.setOnClickListener(this);
        btn_add_truby.setOnClickListener(this);
        btn_fire.setOnClickListener(this);
        btn_add_vent.setOnClickListener(this);
        btn_add_diff.setOnClickListener(this);
        btn_color_canvases.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_add_cornice.setOnClickListener(this);
        //btn_add_comp.setOnClickListener(this);
        //btn_add_other_work.setOnClickListener(this);
        //btn_cancel.setOnClickListener(this);
        btn_rb_v.setOnClickListener(this);
        btn_calculate.setOnClickListener(this);
        btn_add_profile.setOnClickListener(this);
        btn_wall.setOnClickListener(this);
        btn_fasteners.setOnClickListener(this);
        btn_in_cut.setOnClickListener(this);
        btn_diff_acc.setOnClickListener(this);
        btn_separator.setOnClickListener(this);
        btn_soaring_ceiling.setOnClickListener(this);
        btn_mount_wall.setOnClickListener(this);
        btn_mount_granite.setOnClickListener(this);
        btn_cabling.setOnClickListener(this);
        btn_bond_beam.setOnClickListener(this);

        lustr = (EditText) findViewById(R.id.lustr);
        karniz = (EditText) findViewById(R.id.karniz);
        ed_fire = (EditText) findViewById(R.id.ed_fire);
        name_project = (EditText) findViewById(R.id.name_project);
        ed_wall = (EditText) findViewById(R.id.ed_wall);
        ed_cabling = (EditText) findViewById(R.id.ed_cabling);
        ed_separator = (EditText) findViewById(R.id.ed_separator);
        ed_fasteners = (EditText) findViewById(R.id.ed_fasteners);
        ed_in_cut = (EditText) findViewById(R.id.ed_in_cut);
        mount_wall = (EditText) findViewById(R.id.mount_wall);
        mount_granite = (EditText) findViewById(R.id.mount_granite);
        ed_diff_acc = (EditText) findViewById(R.id.ed_diff_acc);
        ed_discount = (EditText) findViewById(R.id.ed_discount);
        bond_beam = (EditText) findViewById(R.id.bond_beam);
        soaring_ceiling = (EditText) findViewById(R.id.soaring_ceiling);

        text_calculate = (TextView) findViewById(R.id.text_calculate);

        RB_karniz = (CheckBox) findViewById(R.id.RB_karniz);

        RB_karniz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RB_karniz.isChecked()) {
                    rb_k = true;
                    int_rb_k = 1;
                } else {
                    rb_k = false;
                    int_rb_k = 0;
                }
            }
        });

        rb_v_white = (RadioButton) findViewById(R.id.rb_v_white);
        rb_v_color = (RadioButton) findViewById(R.id.rb_v_color);
        rb_v_no = (RadioButton) findViewById(R.id.rb_v_no);

        rb_v_no.setChecked(true);
        rb_vstavka = "0";

        RadioGroup radGrp = (RadioGroup) findViewById(R.id.radios_v);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_v_white:
                        btn_rb_v.setEnabled(false);
                        btn_rb_v.setBackgroundColor(Color.WHITE);
                        rb_vstavka = "1";
                        SharedPreferences SP = getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        SharedPreferences.Editor ed = SP.edit();
                        ed.putString("", "314");
                        ed.commit();
                        break;

                    case R.id.rb_v_color:
                        btn_rb_v.setEnabled(true);
                        btn_rb_v.setBackgroundResource(R.drawable.rounded_button);
                        rb_vstavka = "1";
                        break;

                    case R.id.rb_v_no:
                        btn_rb_v.setEnabled(false);
                        btn_rb_v.setBackgroundColor(Color.WHITE);
                        rb_vstavka = "0";
                        SP = getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", "0");
                        ed.commit();
                        break;
                }
            }
        });

        rb_m_yes = (RadioButton) findViewById(R.id.rb_m_yes);
        rb_m_no = (RadioButton) findViewById(R.id.rb_m_no);

        rb_m_yes.setChecked(true);

        mounting = true;
        RadioGroup radios_mounting = (RadioGroup) findViewById(R.id.radios_mounting);
        radios_mounting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_m_yes:
                        mounting = true;
                        break;

                    case R.id.rb_m_no:
                        mounting = false;
                        break;

                }
            }
        });

        rb_b_no = (RadioButton) findViewById(R.id.rb_b_no);
        rb_b_potol = (RadioButton) findViewById(R.id.rb_b_potol);
        rb_b_all = (RadioButton) findViewById(R.id.rb_b_all);

        rb_baget = "0";
        Log.d("mLog", rb_baget);

        RadioGroup radios_b = (RadioGroup) findViewById(R.id.radios_b);
        radios_b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_b_no:
                        rb_baget = "0";
                        Log.d("mLog", rb_baget);
                        break;

                    case R.id.rb_b_potol:
                        rb_baget = "1";
                        Log.d("mLog", rb_baget);
                        break;

                    case R.id.rb_b_all:
                        rb_baget = "2";
                        Log.d("mLog", rb_baget);
                        break;
                }
            }
        });

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SP4 = PreferenceManager.getDefaultSharedPreferences(this);
        SP5 = PreferenceManager.getDefaultSharedPreferences(this);
        SP9 = PreferenceManager.getDefaultSharedPreferences(this);
        SPI = PreferenceManager.getDefaultSharedPreferences(this);
        SPSO = PreferenceManager.getDefaultSharedPreferences(this);

        image = (ImageView) findViewById(R.id.id_image);

        area = (TextView) findViewById(R.id.area);
        perimetr = (TextView) findViewById(R.id.perimetr);
        corners = (TextView) findViewById(R.id.corners);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SP = getSharedPreferences("id_calc", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", String.valueOf(1));
        ed.commit();

        id_cl = getIntent().getStringExtra("id_cl"); // project
        calc = getIntent().getStringExtra("calc");  // id_calculation

        Log.d("mLog id_cl + calc", id_cl + " " + calc);

        if (calc != null && id_cl != null) {
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            String sqlQuewy = "SELECT calculation_title, n1, n2, n3, n4, n5, n6, n7, n8, n9," +
                    " n10, n11, n12, n16, n17, n18, n19, n20, n21, n24," +
                    " n25, dop_krepezh, calc_image, n27, color, offcut_square, discount, n28, n30, original_sketch "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE _id = ?";

            Cursor k = db.rawQuery(sqlQuewy, new String[]{calc});

            if (k != null) {
                if (k.moveToFirst()) {
                    do {
                        String calculation_title = k.getString(k.getColumnIndex(k.getColumnName(0)));
                        String n1 = k.getString(k.getColumnIndex(k.getColumnName(1)));
                        n2 = k.getString(k.getColumnIndex(k.getColumnName(2)));
                        n3 = k.getString(k.getColumnIndex(k.getColumnName(3)));
                        String n4 = k.getString(k.getColumnIndex(k.getColumnName(4)));
                        String n5 = k.getString(k.getColumnIndex(k.getColumnName(5)));
                        String n6 = k.getString(k.getColumnIndex(k.getColumnName(6)));
                        String n7 = k.getString(k.getColumnIndex(k.getColumnName(7)));
                        String n8 = k.getString(k.getColumnIndex(k.getColumnName(8)));
                        String n9 = k.getString(k.getColumnIndex(k.getColumnName(9)));
                        String n10 = k.getString(k.getColumnIndex(k.getColumnName(10)));
                        String n11 = k.getString(k.getColumnIndex(k.getColumnName(11)));
                        String n12 = k.getString(k.getColumnIndex(k.getColumnName(12)));
                        String n16 = k.getString(k.getColumnIndex(k.getColumnName(13)));
                        String n17 = k.getString(k.getColumnIndex(k.getColumnName(14)));
                        String n18 = k.getString(k.getColumnIndex(k.getColumnName(15)));
                        String n19 = k.getString(k.getColumnIndex(k.getColumnName(16)));
                        String n20 = k.getString(k.getColumnIndex(k.getColumnName(17)));
                        String n21 = k.getString(k.getColumnIndex(k.getColumnName(18)));
                        String n24 = k.getString(k.getColumnIndex(k.getColumnName(19)));
                        String n25 = k.getString(k.getColumnIndex(k.getColumnName(20)));
                        String dop_krepezh = k.getString(k.getColumnIndex(k.getColumnName(21)));
                        imag = k.getString(k.getColumnIndex(k.getColumnName(22)));
                        String n27 = k.getString(k.getColumnIndex(k.getColumnName(23)));
                        String color = k.getString(k.getColumnIndex(k.getColumnName(24)));
                        square_obr = k.getString(k.getColumnIndex(k.getColumnName(25)));
                        String discount = k.getString(k.getColumnIndex(k.getColumnName(26)));
                        String n28 = k.getString(k.getColumnIndex(k.getColumnName(27)));
                        String n30 = k.getString(k.getColumnIndex(k.getColumnName(28)));
                        original_sketch = k.getString(k.getColumnIndex(k.getColumnName(29)));

                        name_project.setText(calculation_title);
                        area.setText(" S = " + n4 + " м2");
                        S = Double.valueOf(n4);
                        perimetr.setText(" P = " + n5 + " м");
                        P = Double.valueOf(n5);
                        // if (n6 == "1"){
                        //     rb_v=true;
                        // }

                        SPSO = getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        ed = SPSO.edit();
                        ed.putString("", n6);
                        ed.commit();

                        mount_wall.setText(n7);
                        mount_granite.setText(n8);
                        corners.setText(" Количество углов =   " + n9);
                        Angle = Double.valueOf(n9);
                        ed_in_cut.setText(n11);
                        lustr.setText(n12);
                        if (n16 == "1") {
                            rb_k = true;
                        }
                        ed_wall.setText(n18);
                        ed_cabling.setText(n19);
                        ed_separator.setText(n20);
                        ed_fire.setText(n21);
                        ed_diff_acc.setText(n24);
                        bond_beam.setText(n17);
                        ed_fasteners.setText(dop_krepezh);
                        StringBuffer sb = new StringBuffer(imag.subSequence(0, imag.length()));
                        sb.delete(0, 22);
                        Log.d("mLog", "imag = " + imag.length());
                        if (imag.length() < 30) {
                        } else fromBase64(sb.toString());
                        karniz.setText(n27);

                        db = dbHelper.getWritableDatabase();
                        sqlQuewy = "select hex "
                                + "FROM rgzbn_gm_ceiling_colors " +
                                "where title = ? ";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{color});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    btn_color_canvases.setEnabled(true);
                                    String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    btn_color_canvases.setBackgroundColor(Color.parseColor("#" + hex));
                                    btn_color_canvases_visible = true;

                                    SP = getSharedPreferences("color_title", MODE_PRIVATE);
                                    ed = SP.edit();
                                    ed.putString("", color);
                                    ed.commit();

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        if (n6.equals("0")) {
                            rb_v_no.setChecked(true);
                        } else if (n6.equals("314")) {
                            rb_v_white.setChecked(true);
                        } else {
                            rb_v_color.setChecked(true);
                            db = dbHelper.getWritableDatabase();
                            sqlQuewy = "select hex "
                                    + "FROM rgzbn_gm_ceiling_colors " +
                                    "where title = ? ";
                            c = db.rawQuery(sqlQuewy, new String[]{n6});
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                        btn_rb_v.setBackgroundColor(Color.parseColor("#" + hex));

                                    } while (c.moveToNext());
                                }
                            }
                            c.close();
                        }

                        if (square_obr.equals("")) {
                            square_obr = "0";
                        }

                        if (n28==null){
                            n28="0";
                        }

                        if (n28.equals("0")) {
                            rb_baget = "0";
                            rb_b_no.setChecked(true);
                        } else if (n28.equals("1")) {
                            rb_baget = "1";
                            rb_b_potol.setChecked(true);
                        } else if (n28.equals("2")) {
                            rb_baget = "2";
                            rb_b_all.setChecked(true);
                        }

                        soaring_ceiling.setText(n30);
                        ed_discount.setText(discount);

                        Log.d("all_calc", "n1 " + n1);
                        Log.d("all_calc", "n2 " + n2);
                        Log.d("all_calc", "n3 " + n3);
                        Log.d("all_calc", "n4 " + n4);
                        Log.d("all_calc", "n5 " + n5);
                        Log.d("all_calc", "n7 " + n7);
                        Log.d("all_calc", "n8 " + n8);
                        Log.d("all_calc", "n9 " + n9);
                        Log.d("all_calc", "n11 " + n11);
                        Log.d("all_calc", "n12 " + n12);
                        Log.d("all_calc", "n16 " + n16);
                        Log.d("all_calc", "n17 " + n17);
                        Log.d("all_calc", "n18 " + n18);
                        Log.d("all_calc", "n19 " + n19);
                        Log.d("all_calc", "n21 " + n20);
                        Log.d("all_calc", "n21 " + n21);
                        Log.d("all_calc", "n24 " + n24);
                        Log.d("all_calc", "n25 " + n25);
                        Log.d("all_calc", "n27 " + n27);
                        Log.d("all_calc", "original " + original_sketch);

                    } while (k.moveToNext());
                }
            }
            k.close();
        } else {
            int max_id_contac = 0;
            try {
                String sqlQuewy = "select MAX(_id) "
                        + "FROM rgzbn_gm_ceiling_calculations " +
                        "where _id>? and _id<?";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            max_id_contac++;
                            calc = String.valueOf(max_id_contac);
                        } while (c.moveToNext());
                    }
                }
            } catch (Exception e) {
                max_id_contac = gager_id_int + 1;
                calc = String.valueOf(max_id_contac);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        canv_text();
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
    protected void onDestroy() {
        super.onDestroy();

        if (calc == null) {

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_OTHER_WORK, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_OTHER_WORK, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_OTHER_COMP, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_OTHER_COMP, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, "calculation_id = ?", new String[]{String.valueOf(calc)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }

        SP = getSharedPreferences("SAVED_SO", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        Log.d("mLog", "destroy");

        SPSO = getSharedPreferences("color_title", MODE_PRIVATE);
        ed = SPSO.edit();
        ed.putString("", "");
        ed.commit();

        SPSO = getSharedPreferences("color_title_vs", MODE_PRIVATE);
        ed = SPSO.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_N4", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_N5", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_N9", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_I", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_LL", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_KP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_WP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_DP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_PT_P", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("draft_diags_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("draft_walls_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("draft_pt_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("draft_auto", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getSharedPreferences("end_draft", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SPSO = getSharedPreferences("color_title", MODE_PRIVATE);
        String id_color = SPSO.getString("", "");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuewy = "select hex "
                + "FROM rgzbn_gm_ceiling_colors " +
                "where title = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_color});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    btn_color_canvases.setBackgroundColor(Color.parseColor("#" + hex));

                } while (c.moveToNext());
            }
        }
        c.close();

        SPSO = getSharedPreferences("color_title_vs", MODE_PRIVATE);
        String id_color_vs = SPSO.getString("", "");

        db = dbHelper.getWritableDatabase();
        sqlQuewy = "select hex "
                + "FROM rgzbn_gm_ceiling_colors " +
                "where title = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{id_color_vs});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    btn_rb_v.setBackgroundColor(Color.parseColor("#" + hex));

                } while (c.moveToNext());
            }
        }
        c.close();

        SPSO = getSharedPreferences("SAVED_SO", MODE_PRIVATE);
        if (SPSO.getString("", "").equals("")) {
            square_obr = "0";
        } else {
            square_obr = SPSO.getString(SAVED_SO, "");
        }

        SP4 = getSharedPreferences("SAVED_N4", MODE_PRIVATE);
        if (SP4.getString(SAVED_N4, "").length() == 0) {
            area.setText(" S =  м2");
        } else {
            area.setText(" S =   " + SP4.getString(SAVED_N4, "") + " м2");
            S = Double.valueOf(SP4.getString(SAVED_N4, ""));
        }

        SP5 = getSharedPreferences("SAVED_N5", MODE_PRIVATE);
        if (SP5.getString(SAVED_N5, "").length() == 0) {
            perimetr.setText(" P =  м");
        } else {
            perimetr.setText(" Р =   " + SP5.getString(SAVED_N5, "") + " м");
            P = Double.valueOf(SP5.getString(SAVED_N5, ""));
        }

        SP9 = getSharedPreferences("SAVED_N9", MODE_PRIVATE);
        if (SP9.getString(SAVED_N9, "").length() == 0) {
            corners.setText(" Количество углов =   ");
        } else {
            corners.setText(" Количество углов =   " + SP9.getString(SAVED_N9, ""));
            Angle = Double.valueOf(SP9.getString(SAVED_N9, ""));
        }

        SPW = getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
        width_final = SPW.getString(SAVED_W, "");

        SPW = getSharedPreferences("SAVED_LL", MODE_PRIVATE);
        lines_length = SPW.getString(SAVED_W, "");

        SPI = getSharedPreferences("SAVED_I", MODE_PRIVATE);
        imag = SPI.getString(SAVED_I, "");

        SPI = getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
        imag_cut = SPI.getString("", "");


        SPI = getSharedPreferences("SAVED_KP", MODE_PRIVATE);
        SAVED_KP = SPI.getString("", "");
        Log.d("mLog", SAVED_KP);

        SPI = getSharedPreferences("SAVED_WP", MODE_PRIVATE);
        SAVED_WP = SPI.getString("", "");
        Log.d("mLog", SAVED_WP);

        SPI = getSharedPreferences("SAVED_DP", MODE_PRIVATE);
        SAVED_DP = SPI.getString("", "");
        Log.d("mLog", SAVED_DP);

        SPI = getSharedPreferences("SAVED_PT_P", MODE_PRIVATE);
        SAVED_PT_P = SPI.getString("", "");
        Log.d("mLog", SAVED_PT_P);

        SPI = getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
        SAVED_CODE = SPI.getString("", "");
        Log.d("mLog", SAVED_CODE);

        SPI = getSharedPreferences("end_draft", MODE_PRIVATE);
        String end_draft = SPI.getString("", "");

        SPI = getSharedPreferences("draft_auto", MODE_PRIVATE);
        String draft_auto = SPI.getString("", "");

        if (end_draft.equals("1") && draft_auto.equals("1")){
            calculat = false;
            calculation();
        }

        SPI = getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
        SAVED_ALFAVIT = SPI.getString("", "");
        Log.d("mLog", SAVED_ALFAVIT);

        StringBuffer sb = new StringBuffer(imag.subSequence(0, imag.length()));
        sb.delete(0, 22);

        Log.d("mLog", "imag = " + imag.length());

        if (imag.length() < 34) {

        } else fromBase64(sb.toString()); // декодируем текст в картинку
    }

    public void fromBase64(String imag) {

        byte[] decodedString = Base64.decode(imag, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Bitmap bmHalf = Bitmap.createScaledBitmap(decodedByte, decodedByte.getWidth(),
                decodedByte.getHeight(), false);

        image.setImageBitmap(bmHalf);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_save:
                calculat = true;
                calculation();
                break;
            case R.id.btn_calculate:

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                calc = getIntent().getStringExtra("calc");  // id_calculation

                String old_n2 = "";
                String old_n3 = "";
                try {
                    String sqlQuewy = "select n2, n3 "
                            + "FROM rgzbn_gm_ceiling_calculations " +
                            "where _id=? ";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{calc});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                old_n2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                old_n3 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                            } while (c.moveToNext());
                        }
                    }
                }catch (Exception e){

                }

                try {
                    String sqlQuewy = "select _id, price, width "
                            + "FROM rgzbn_gm_ceiling_canvases " +
                            "where _id = ? ";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{n3});         // заполняем массивы из таблицы
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                id_n3 = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                }catch (Exception e){
                    String sqlQuewy = "select _id, price, width "
                            + "FROM rgzbn_gm_ceiling_canvases " +
                            "where _id = ? ";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{texture_id});         // заполняем массивы из таблицы
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                id_n3 = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                }

                if (old_n3.equals("") || old_n2.equals("")) {
                    calculat = false;
                    calculation();
                } else {
                    if (old_n3.equals(String.valueOf(id_n3)) && old_n2.equals(String.valueOf(texture_id))) {
                        calculat = false;
                        calculation();
                    } else if (calc == null) {
                        calculat = false;
                        calculation();
                    } else {
                        SP = getSharedPreferences("draft_auto", MODE_PRIVATE);
                        SharedPreferences.Editor ed = SP.edit();
                        ed.putString("", String.valueOf(1));
                        ed.commit();

                        int i = 0;
                        String walls_points = "";
                        String diags_points = "";
                        String pt_points = "";

                        for (String retval : original_sketch.split("\\|\\|")) {
                            if (i == 0) {
                                int j = 0;
                                for (String retval1 : retval.split(";")) {
                                    j++;
                                    if (j % 4 == 1) {
                                        walls_points += "{\"s0_x\":" + retval1 + ",";
                                    } else if (j % 4 == 2) {
                                        walls_points += "\"s0_y\":" + retval1 + ",";
                                    } else if (j % 4 == 3) {
                                        walls_points += "\"s1_x\":" + retval1 + ",";
                                    } else if (j % 4 == 0) {
                                        walls_points += "\"s1_y\":" + retval1 + "},";
                                    }
                                }

                            } else if (i == 1) {
                                int j = 0;
                                for (String retval1 : retval.split(";")) {
                                    j++;
                                    if (j % 4 == 1) {
                                        diags_points += "{\"s0_x\":" + retval1 + ",";
                                    } else if (j % 4 == 2) {
                                        diags_points += "\"s0_y\":" + retval1 + ",";
                                    } else if (j % 4 == 3) {
                                        diags_points += "\"s1_x\":" + retval1 + ",";
                                    } else if (j % 4 == 0) {
                                        diags_points += "\"s1_y\":" + retval1 + "},";
                                    }
                                }

                            } else if (i == 2) {
                                int j = 0;
                                for (String retval1 : retval.split(";")) {
                                    j++;
                                    if (j % 2 == 1) {
                                        pt_points += "{\"x\":" + retval1 + ",";
                                    } else if (j % 2 == 0) {
                                        pt_points += "\"y\":" + retval1 + "},";
                                    }
                                }
                            } else if (i == 3) {

                            } else if (i == 4) {
                            }
                            i++;
                        }

                        walls_points = "[" + walls_points.substring(0, walls_points.length() - 1) + "]";
                        Log.d("mLog", walls_points);
                        diags_points = "[" + diags_points.substring(0, diags_points.length() - 1) + "]";
                        Log.d("mLog", diags_points);
                        pt_points = "[" + pt_points.substring(0, pt_points.length() - 1) + "]";
                        Log.d("mLog", pt_points);


                        SP = getSharedPreferences("draft_diags_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", diags_points);
                        ed.commit();

                        SP = getSharedPreferences("draft_walls_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", walls_points);
                        ed.commit();

                        SP = getSharedPreferences("draft_pt_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", pt_points);
                        ed.commit();

                        intent = new Intent(this, Activity_draft.class);
                        startActivity(intent);

                    }
                }
                break;
            case R.id.chertezh:

                SP = getSharedPreferences("draft_diags_points", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getSharedPreferences("draft_walls_points", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getSharedPreferences("draft_pt_points", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getSharedPreferences("draft_auto", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                intent = new Intent(this, Activity_draft.class);
                startActivity(intent);
                break;
            case R.id.btn_texture:
                s_setMessage = "  Матовый больше похож на побелку \n  Сатин - на, крашенный потолок \n  Глянец - имеет лёгкий отблеск";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_width:
                s_setMessage = "    От ширины материала зависит бесшовный ли будет потолок и его цена, чем шире полотно тем дороже";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_vstavka:
                s_setMessage = "    Между стеной и натяжным потолком после монтажа остаётся технологический засор 5мм, который закрывается декоративной вставкой";
                s_setMessage1 = "";
                s_setdrawable = String.valueOf(R.drawable.vstavka);
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_light:
                s_setMessage = "    Если на потолке будут люстры или светильники укажите их количество и характеристики. Если их не будет просто пропустите этот пункт";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_lustr:
                s_setMessage = "    В паспорте на люстру есть описание системы крепежа и диаметр технологического отверстия";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_add_svetiln:
                intent = new Intent(this, Activity_add_svetiln.class);

                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;
            case R.id.btn_add_kupit_svetiln:
                intent = new Intent(this, Activity_add_kupit_svetiln.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;
            case R.id.btn_karniz:
                s_setMessage = "    Если его не будет или он будет крепиться к стене просто пропустите этот пункт. " +
                        "\nШторный карниз можно крепить на потолок двумя способами:" +
                        "\n 1.Видимый";
                s_setMessage1 = "2.Скрытый (в этом случае надо указать длину стены, на которой окно и ставить галочку напротив надписи скрытый шторный карниз)";
                s_setdrawable = String.valueOf(R.drawable.karniz1);
                s_setdrawable1 = String.valueOf(R.drawable.karniz2);
                fun_builder();
                break;
            case R.id.btn_add_truby:
                intent = new Intent(this, Activity_add_truby.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;
            case R.id.btn_fire:
                s_setMessage = "    Если на основном потолке установлена пожарная сигнализация или планируете сделать её установку на натяжной потолок, укажите количество пожарных сигнализаций" +
                        "в данной комнате для комплектации стоек под них";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_add_vent:
                intent = new Intent(this, Activity_add_vent.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;
            case R.id.btn_add_diff:
                intent = new Intent(this, Activity_add_diffuzor.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;
            case R.id.color_canvases:
                intent = new Intent(this, Activity_color.class);
                Log.d("mLog", texture_id);
                intent.putExtra("texture_id", texture_id);
                startActivity(intent);
                break;
            case R.id.btn_rb_v:
                intent = new Intent(this, Activity_color.class);
                intent.putExtra("component_id", "15");
                startActivity(intent);
                break;
            case R.id.btn_add_cornice:
                intent = new Intent(this, Activity_add_kupit_cornice.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;

            case R.id.btn_add_profile:
                intent = new Intent(this, Activity_add_profile.class);
                intent.putExtra("id_calc", calc);
                startActivity(intent);
                break;

            case R.id.btn_wall:
                s_setMessage = "    На 1м усиления стен используется:  \nБрус 40*50 + 3 * Саморез 3,5 * 51  + 3 * Дюбель полим. 6 * 51 + 3 * Кронштейн 15 * 12,5 см.";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_fasteners:
                s_setMessage = "    На 1м дополнительного крепежа используется:  \n10 * Саморез 3,5 * 51  + Багет (на выбор: ПВХ (2,5 м) , стеновой аллюм, потолочный аллюм)  ";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_in_cut:
                s_setMessage = "    На 1м внутреннего выреза используется:  \n Брус 40*50  + Багет (на выбор: ПВХ (2,5 м) , " +
                        "стеновой аллюм, потолочный аллюм) + 3 * Кронштейн 15 * 12,5 см. + + 22 * Саморез 3,5 * 41 +  16 * Дюбель полим. 6 * 51 + Гарпун ";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_diff_acc:
                s_setMessage = "    Наценка на монтажные работы за труднодоступные места. Считается по метрам.  ";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_separator:
                s_setMessage = "    На 1м разделителя используется:  \nБрус 40*50  + 3 * Саморез ГКД 4,2 * 102 +  3 * Дюбель полим. " +
                        "6 * 51 + 20 * Саморез ГДК 3,5 * 51 +  Вставка в разд 303 гриб + Багет разделительный аллюм (2.5 м.) ";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_soaring_ceiling:
                s_setMessage = "    На 1м парящего потолка используется: \nБагет для парящих пот аллюм + Вставка для парящих потолков";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_mount_wall:
                s_setMessage = "    Считается дополнительная работа монтажникам по креплению багета в плитку";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_mount_granite:
                s_setMessage = "    Считается дополнительная работа монтажникам по креплению багета в керамогранит";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_cabling:
                s_setMessage = "    На 1м провода используется:  \nПровод ПВС 2 х 0,75  + 2 * Дюбель полим. 6 * 51 + 2 * Саморез ГДК 3,5 * 51 ";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_bond_beam:
                s_setMessage = "    На 1м  используется:   \nБрус 40*50  + 3 * Подвес прямой П 60 (0,8) +  6 * Дюбель полим. " +
                        "6 * 51 + 6 * Саморез ГДК 3,5 * 51 + 6 * Саморез ГДК 3,5 * 41";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
        }

    }

    void calculation() {

        //------------------------------------- РАСЧЕТ СТОИМОСТИ КОМПЛЕКТУЮЩИХ -------------------------------------//

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBHelper.TABLE_MOUNTING_DATA, null, null);
        db.delete(DBHelper.TABLE_COMPONENT_ITEM, null, null);

        int cou =0;
        String sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToLast()) {
                cou = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
            }
        }
        c.close();

        component_count.clear();
        for (int i = 0; i < cou+10; i++) {
            component_count.add(Double.valueOf(0));
        }

        canvases_data.clear();
        for (int i = 0; i < 8; i++) {
            canvases_data.add(0);
        }

        mounting_data.clear();
        for (int i = 0; i < 6; i++) {
            mounting_data.add(0);
        }

        results.clear();
        for (int i = 0; i < 43; i++) {
            results.add(0);
        }

        component_item.clear();
        for (int i = 0; i < 12; i++) {
            component_item.add(0);
        }

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_mount " +
                "where user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id_str});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (int j = 1; j < 43; j++) {
                        String res = c.getString(c.getColumnIndex(c.getColumnName(j)));
                        results.set(j - 1, Integer.valueOf(res));
                    }
                } while (c.moveToNext());
            }
        }
        c.close();


        items_9 = 0;                                      // саморез
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%3,5 * 51%') and component_id = 6";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_9 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_5 = 0;                                      // дюбель
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%6 * 51%') and component_id = 5";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_5 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_11 = 0;                                      // Стеновой багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%ПВХ (2,5 м)%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_11 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_236 = 0;                                      // потолоч багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%потолочный аллюм%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_236 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_239 = 0;                                      // стеновой багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%стеновой аллюм%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_239 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_vstavka_bel = 0;                                      // Вставка белая
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%303%') and component_id = 15";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_vstavka_bel = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_vstavka = 0;
        if (colorIndex > 0) {                               // Вставка цветная
            String name1 = "652";
            String color1;
            sqlQuewy = "select _id "
                    + "FROM rgzbn_gm_ceiling_components_option " +
                    "where title = ? and component_id = 15";
            c = db.rawQuery(sqlQuewy, new String[]{name1});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        items_vstavka = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();
            if (items_vstavka == 0)
                items_vstavka = items_vstavka_bel;
        }

        items_559 = 0;                                      // для парящих пот аллюм багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%для парящих пот аллюм%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_559 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_10 = 0;                                      // Саморез
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%п/сф 3,5*9,5%') and component_id = 6";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_10 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_16 = 0;                                      // Платформа под люстру
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%тарелка%') and component_id = 9";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_16 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_556 = 0;                                      // шуруп-полукольцо
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%6*40%') and component_id = 23";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_556 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_4 = 0;                                      // Провод
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%ПВС 2 х 0,75%') and component_id = 4";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_4 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_58 = 0;                                      // Круглое кольцо
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('50') and component_id = 21";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_58 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_3 = 0;                                      // Подвес прямой
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%П 60 (0,8)%') and component_id = 3";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_3 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_2 = 0;                                      // Клеммная колодка
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%2,5 мм%') and component_id = 2";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_2 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_1 = 0;                                      // Брус
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%40*50%') and component_id = 1";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_1 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_8 = 0;                                      // Саморез
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%3,5 * 41%') and component_id = 6";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_8 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_6 = 0;                                      // Саморез
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%4,2 * 102%') and component_id = 6";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_6 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_14 = 0;                                      // Вставка
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%в разд 303 гриб%') and component_id = 15";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_14 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_38 = 0;                                      // для парящих потолков
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%для парящих потолков%') and component_id = 15";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_38 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_430 = 0;                                      // Кронштейн
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%15 * 12,5 см.%') and component_id = 16";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_430 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_35 = 0;                                      // Багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%разделительный аллюм%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_35 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_360 = 0;                                      // гарпун
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = 42";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_360 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_495 = 0;                                      // Платформа для карнизов
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%70*100 мм%') and component_id = 68";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_495 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_233 = 0;                                      // Декскор 2,5 багет
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%Декскор 2,5%') and component_id = 30";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_233 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();
        items_659 = 0;                                      // Переход уровня
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%Переход уровня%') and component_id = 190";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_659 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        items_660 = 0;                                      // Переход уровня с нишей
        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where title LIKE('%Переход уровня с нишей%') and component_id = 191";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_660 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();


        if (n1.equals("28") && P == 0.0) {
        } else {
            component_count.set(items_9, component_count.get(items_9) + P * 10);
            component_count.set(items_5, component_count.get(items_5) + P * 10);
            if (rb_baget.equals("0")) {
                component_count.set(items_11, component_count.get(items_11) + P + 0.5);
            } else if (rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + P + 0.5);
            } else if (rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + P + 0.5);
            }
        }


        Log.d("mLog","items_11 " + String.valueOf(component_count.get(items_11)));

        // внутренний вырез
        if (ed_in_cut.getText().toString().equals("") || ed_in_cut.getText().toString().equals("0") || ed_in_cut.getText().toString().equals("0.0")) {
        } else {
            Double cut = Double.valueOf(ed_in_cut.getText().toString());
            if (n1.equals("29")) {
                component_count.set(items_1, component_count.get(items_1) + cut);
            } else if (n1.equals("28") && rb_baget.equals("0")) {
                component_count.set(items_11, component_count.get(items_11) + P);
            } else if (n1.equals("28") && rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + P);
            } else if (n1.equals("28") && rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + P);
            }
            component_count.set(items_430, component_count.get(items_430) + cut * 3);
            component_count.set(items_8, component_count.get(items_8) + cut * 22);
            component_count.set(items_5, component_count.get(items_5) + cut * 16);
            component_count.set(items_360, component_count.get(items_360) + cut);
        }

        String id_color_vs = "0";
        try {
            SPSO = getSharedPreferences("color_title_vs", MODE_PRIVATE);
            id_color_vs = SPSO.getString("", "");

            double n5_count = Math.ceil(P + 0.5);
            component_count.set(Integer.parseInt(id_color_vs), n5_count + 0.5);
        } catch (Exception e) {
        }

        //if (n1.equals("28") && rb_vstavka=="1") {

        //    String id="";
        //    int i = 0;
        //    sqlQuewy = "select _id "
        //            + "FROM rgzbn_gm_ceiling_components_option " +
        //            "where component_id = ? and title = ? ";
        //    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(15), id_color_vs});
        //    if (c != null) {
        //        if (c.moveToFirst()) {
        //            do {
        //                id = c.getString(c.getColumnIndex(c.getColumnName(0)));
        //            } while (c.moveToNext());
        //        }
        //    }
        //    c.close();
        //}

        //люстры
        int count_lustr = 0;
        if (lustr.getText().toString().equals("") || lustr.getText().toString().equals("0") || lustr.getText().toString().equals("0.0")) {
        } else {
            count_lustr = Integer.parseInt(lustr.getText().toString());
            component_count.set(items_5, component_count.get(items_5) + count_lustr * 3);
            component_count.set(items_9, component_count.get(items_9) + count_lustr * 3);
            component_count.set(items_10, component_count.get(items_10) + count_lustr * 8);
            component_count.set(items_16, component_count.get(items_16) + count_lustr);
            component_count.set(items_556, component_count.get(items_556) + count_lustr);
            component_count.set(items_4, component_count.get(items_4) + count_lustr * 0.5);
            component_count.set(items_58, component_count.get(items_58) + count_lustr);
            component_count.set(items_3, component_count.get(items_3) + count_lustr * 4);
        }


        if (count_lustr > 0) {
            component_count.set(items_2, component_count.get(items_2) + 2);
        }

        // светильники
        int i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_fixtures " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         //сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        i++;
        n13_count = new int[i];
        n13_type = new String[i];
        n13_size_id = new int[i];
        n13_size = new String[i];
        count_svet = 0;

        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_fixtures " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n13_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n13_type[i] = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    n13_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    count_svet += n13_count[i];
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        int j = 0;
        sqlQuewy = "select title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where _id = ?";

        for (j = 0; j < i; j++) {
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n13_size_id[j])});// заполняем массивы из таблицы
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        n13_size[j] = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
        }
        c.close();

        if (i > 0) {
            components_circle(i, 21, 11, n13_count, n13_size);
            components_square(i, 12, 10, n13_count, n13_size);
            component_count.set(items_2, component_count.get(items_2) + 1);
        }

        component_count.set(items_9, component_count.get(items_9) + count_svet * 4);
        component_count.set(items_10, component_count.get(items_10) + count_svet * 4);
        component_count.set(items_5, component_count.get(items_5) + count_svet * 2);
        component_count.set(items_3, component_count.get(items_3) + count_svet * 2);
        component_count.set(items_4, component_count.get(items_4) + count_svet * 0.5);
        component_count.set(items_2, component_count.get(items_2) + count_svet * 1);


        Log.d("mLog","items_11 " + String.valueOf(component_count.get(items_11)));

        //профиль
        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_profil " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         //сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        i++;
        n29_count = new int[i];
        n29_type = new int[i];

        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_profil " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n29_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n29_type[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        for (j=0; i>j; j++){
            if ((n29_type[j] == 12) || (n29_type[j] == 13)){
                component_count.set(items_659, component_count.get(items_659) + n29_count[j]);
            } else if ((n29_type[j] == 15) || (n29_type[j] == 16)){
                component_count.set(items_660, component_count.get(items_660) + n29_count[j]);
            }

        }



        // вентиляция
        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_hoods " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         //сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        i++;
        count_vent = 0;
        count_electr = 0;
        n22_count = new int[i];
        n22_type = new String[i];
        n22_size_id = new int[i];
        n22_size = new String[i];

        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_hoods " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n22_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n22_type[i] = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    n22_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    if (n22_type[i].equals("5") || n22_type[i].equals("6")) {
                        count_vent += n22_count[i];
                    } else if (n22_type[i].equals("7") || n22_type[i].equals("8")) {
                        count_electr += n22_count[i];
                    }
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        j = 0;
        sqlQuewy = "select title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where _id = ?";

        for (j = 0; j < i; j++) {
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n22_size_id[j])});// заполняем массивы из таблицы
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        n22_size[j] = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
        }
        c.close();

        if (i > 0) {
            components_circle(i, 21, 11, n22_count, n22_size);
            components_square(i, 12, 10, n22_count, n22_size);
        }

        component_count.set(items_9, component_count.get(items_9) + count_vent * 4);
        component_count.set(items_10, component_count.get(items_10) + count_vent * 4);
        component_count.set(items_5, component_count.get(items_5) + count_vent * 2);

        component_count.set(items_9, component_count.get(items_9) + count_electr * 4);
        component_count.set(items_10, component_count.get(items_10) + count_electr * 4);
        component_count.set(items_5, component_count.get(items_5) + count_electr * 2);
        component_count.set(items_3, component_count.get(items_3) + count_electr * 2);
        component_count.set(items_4, component_count.get(items_4) + count_electr * 0.5);
        component_count.set(items_2, component_count.get(items_2) + count_electr * 1);



        Log.d("mLog","items_11 " + String.valueOf(component_count.get(items_11)));

        //труба
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_pipes " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // смотрим сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
        i++;

        n14_count = new int[i];
        n14_size_id = new int[i];
        n14_size = new String[i];

        i = 0;
        count_pipes = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_pipes " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n14_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n14_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    count_pipes += n14_count[i];
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        if (i > 0) {
            for (j = 0; j < i; j++) {
                component_count.set(n14_size_id[j], component_count.get(n14_size_id[j]) + n14_count[j]);
                Log.d("mLog", "трубы " + n14_size_id[j] + " " + n14_count[j]);
            }
        }

        //диффузор
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_diffusers " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // смотрим сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
        i++;

        n23_count = new int[i];
        n23_type = new String[i];
        n23_size_id = new int[i];
        n23_size = new String[i];

        i = 0;
        count_diffus = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_diffusers " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n23_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n23_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    count_diffus += n23_count[i];
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        if (i > 0) {
            for (j = 0; j < i; j++) {
                component_count.set(n23_size_id[j], component_count.get(n23_size_id[j]) + n23_count[j]);
            }
        }

        //кaрниз
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_cornice " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // смотрим сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
        i++;

        n15_count = new int[i];
        n15_type = new String[i];
        n15_size_id = new int[i];
        n15_size = new String[i];

        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_cornice " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n15_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n15_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        if (i > 0) {
            for (j = 0; j < i; j++) {

                sqlQuewy = "select * "
                        + "FROM rgzbn_gm_ceiling_components_option " +
                        "where _id = ?";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n15_size_id[j])});         // заполняем массивы из таблицы
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            component_count.set(Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0)))),
                                    component_count.get(Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))))) + n15_count[j]);
                        } while (c.moveToNext());
                    }
                }
                c.close();
            }
        }

        Log.d("mLog", "2 " + component_count.get(42));

        //экола
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_ecola " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // смотрим сколько всего в таблице
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
        i++;

        n26_count = new int[i];
        n26_type_id = new int[i];
        n26_size_id = new int[i];

        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_ecola " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc)});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    n26_count[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    n26_size_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(3))));
                    n26_type_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        if (i > 0) {
            for (j = 0; j < i; j++) {
                component_count.set(n26_size_id[j], component_count.get(n26_size_id[j]) + n26_count[j]);
                component_count.set(n26_type_id[j], component_count.get(n26_type_id[j]) + n26_count[j]);
            }
        }

        // парящий потоолок
        if (soaring_ceiling.getText().toString().equals("") || soaring_ceiling.getText().toString().equals("0")) {
        } else {
            double count_s = Double.parseDouble(soaring_ceiling.getText().toString());

            component_count.set(items_559, component_count.get(items_559) + count_s);
            component_count.set(items_38, component_count.get(items_38) + count_s);
        }

        //карниз
        if (karniz.getText().toString().equals("") || karniz.getText().toString().equals("0")) {
        } else {
            double count_c = Double.parseDouble(karniz.getText().toString());

            component_count.set(items_1, component_count.get(items_1) + count_c);
            component_count.set(items_3, component_count.get(items_3) + count_c * 3);
            component_count.set(items_5, component_count.get(items_5) + count_c * 6);
            component_count.set(items_8, component_count.get(items_8) + count_c * 9);
            component_count.set(items_9, component_count.get(items_9) + count_c * 6);

            // скрытый карниз
            if (rb_k) {
                component_count.set(items_430, component_count.get(items_430) + count_c * 2);
                component_count.set(items_8, component_count.get(items_8) + count_c * 4);
            }

        }

        //закладная брусом
        double count_bond_beam = 0;
        if (bond_beam.getText().toString().equals("") || bond_beam.getText().toString().equals("0")) {
        } else {
            count_bond_beam = Double.parseDouble(bond_beam.getText().toString());
            component_count.set(items_1, component_count.get(items_1) + count_bond_beam);
            component_count.set(items_3, component_count.get(items_3) + count_bond_beam * 3);
            component_count.set(items_5, component_count.get(items_5) + count_bond_beam * 6);
            component_count.set(items_9, component_count.get(items_9) + count_bond_beam * 6);
            component_count.set(items_8, component_count.get(items_8) + count_bond_beam * 6);
        }

        //укрепление стен
        double count_wall = 0;
        if (ed_wall.getText().toString().equals("") || ed_wall.getText().toString().equals("0")) {
        } else {
            count_wall = Double.parseDouble(ed_wall.getText().toString());
            component_count.set(items_1, component_count.get(items_1) + count_wall);
            component_count.set(items_6, component_count.get(items_6) + count_wall * 3);
            component_count.set(items_5, component_count.get(items_5) + count_wall * 3);
            component_count.set(items_430, component_count.get(items_430) + count_wall * 3);
        }

        // провод
        double count_cabling = 0;
        if (ed_cabling.getText().toString().equals("") || ed_cabling.getText().toString().equals("0")) {
        } else {
            count_cabling = Double.parseDouble(ed_cabling.getText().toString());
            component_count.set(items_4, component_count.get(items_4) + count_cabling);
            component_count.set(items_9, component_count.get(items_9) + count_cabling * 2);
            component_count.set(items_5, component_count.get(items_5) + count_cabling * 2);
        }
        Log.d("mLog","items_11 " + String.valueOf(component_count.get(items_11)));
        //разделитель только для ПВХ
        double count_separator = 0;
        if (ed_separator.getText().toString().equals("") || ed_separator.getText().toString().equals("0")) {
        } else {
            if (n1.equals("28")) {
                count_separator = Double.parseDouble(ed_separator.getText().toString());
                component_count.set(items_1, component_count.get(items_1) + count_separator);
                component_count.set(items_6, component_count.get(items_6) + count_separator * 3);
                component_count.set(items_9, component_count.get(items_9) + count_separator * 20);
                component_count.set(items_5, component_count.get(items_5) + count_separator * 3);
                component_count.set(items_14, component_count.get(items_14) + count_separator);

                int n20_count = (int) (count_separator / 2.5);

                if ((count_separator % 2.5) > 0) {
                    n20_count++;
                }

                component_count.set(items_35, component_count.get(items_35) + n20_count * 2.5);
                Log.d("mLog", " раздел 4 " + component_count.get(items_35));
            }
        }

        // дополнительный крепеж
        double count_fasteners = 0;
        if (ed_fasteners.getText().toString().equals("") || ed_fasteners.getText().toString().equals("0")) {
        } else {
            count_fasteners = Double.parseDouble(ed_fasteners.getText().toString());
            component_count.set(items_9, component_count.get(items_9) + count_fasteners * 10);
            if (n1.equals("29")){
                component_count.set(items_233, component_count.get(items_233) + (count_fasteners / 2));
            } else if (n1.equals("28") && rb_baget.equals("0")) {
                component_count.set(items_11, component_count.get(items_11) + (count_fasteners / 2));
            } else if (n1.equals("28") && rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + (count_fasteners / 2));
            } else if (n1.equals("28") && rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + (count_fasteners / 2));
            }
        }

        // пожарная сигнализация
        double count_fire = 0;
        if (ed_fire.getText().toString().equals("") || ed_fire.getText().toString().equals("0")) {
        } else {
            count_fire = Double.parseDouble(ed_fire.getText().toString());
            component_count.set(items_9, component_count.get(items_9) + count_fire * 3);
            component_count.set(items_10, component_count.get(items_10) + count_fire * 6);
            component_count.set(items_495, component_count.get(items_495) + count_fire * 6);
            component_count.set(items_16, component_count.get(items_16) + count_fire);
            component_count.set(items_58, component_count.get(items_58) + count_fire);
            component_count.set(items_3, component_count.get(items_3) + count_fire * 3);
            component_count.set(items_5, component_count.get(items_5) + count_fire * 3);
            component_count.set(items_2, component_count.get(items_2) + count_fire * 2);
        }

        Log.d("mLog","items_11 " + String.valueOf(component_count.get(items_11)));
        //стеновой багет 2.5м считается кусками, которые потребуются выложить весь периметр

        if (rb_baget.equals("0")) {
            rouding(items_11, component_count.get(items_11), 2.5);
        } else if (rb_baget.equals("1")) {
            rouding(items_236, component_count.get(items_236), 2.5);
        } else if (rb_baget.equals("2")) {
            rouding(items_239, component_count.get(items_239), 2.5);
        }

        rouding(items_559, component_count.get(items_559), 2.5);
        rouding(items_233, component_count.get(items_233), 2.5);
        rouding(items_38, component_count.get(items_38), 0.5);
        rouding(items_1, component_count.get(items_1), 0.5);
        rouding(650, component_count.get(650), 2.5);
        rouding(651, component_count.get(651), 2.5);
        rouding(652, component_count.get(652), 2.5);
        rouding(653, component_count.get(653), 2.5);
        rouding(654, component_count.get(654), 2.5);
        rouding(655, component_count.get(655), 2.5);
        rouding(656, component_count.get(656), 2.5);

        component_count.set(items_4, Math.ceil(component_count.get(items_4)));

        //--------------------------------------- ВОЗВРАЩАЕМ СТОИМОСТЬ КОМПЛЕКТУЮЩИХ ----------------------------------------//

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_dealer_info " +
                "where dealer_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id_str});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    gm_can_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    gm_comp_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(5))));
                    gm_mount_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(6))));

                    dealer_can_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    dealer_comp_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(2))));
                    dealer_mount_marg = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(3))));
                } while (c.moveToNext());
            }
        }
        c.close();

        StringBuffer sb = new StringBuffer();
        char[] chars = canvases.toCharArray();
        for (int s = 0; s < canvases.length(); s++) {
            if (chars[s] == ' ') {
                break;
            } else {
                sb.append(chars[s]);
            }
        }

        //Обработка 1 угла
        if (n1.equals("28") && Angle > 4) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Oбработка 1 угла");
            values.put(DBHelper.KEY_QUANTITY, Angle - 4);
            values.put(DBHelper.KEY_STACK, "0");
            values.put(DBHelper.KEY_GM_PRICE, results.get(19));
            values.put(DBHelper.KEY_GM_TOTAL, (Angle - 4) * results.get(19));
            values.put(DBHelper.KEY_DEALER_PRICE, results.get(19));
            values.put(DBHelper.KEY_DEALER_TOTAL, (Angle - 4) * results.get(19));
            db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
        }

        sqlQuewy = "select * " +
                "FROM component_item";
        c = db.rawQuery(sqlQuewy, new String[]{});         // заполняем массивы из табли
        if (c != null) if (c.moveToFirst()) {
            do {

                String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                String self_price = c.getString(c.getColumnIndex(c.getColumnName(10)));
                String quantity = c.getString(c.getColumnIndex(c.getColumnName(4)));

                String full_name = c.getString(c.getColumnIndex(c.getColumnName(1)));

                String self_total = String.valueOf(Math.round((Double.parseDouble(self_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                String gm_price = String.valueOf(margin(Double.parseDouble(self_price), gm_comp_marg));
                String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), gm_comp_marg, dealer_comp_marg));
                String dealer_total = String.valueOf(Math.round((Double.parseDouble(dealer_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_COMP_ID, "0");
                values.put(DBHelper.KEY_SELF_TOTAL, self_total);
                values.put(DBHelper.KEY_GM_PRICE, gm_price);
                values.put(DBHelper.KEY_GM_TOTAL, gm_total);
                values.put(DBHelper.KEY_DEALER_PRICE, dealer_price);
                values.put(DBHelper.KEY_DEALER_TOTAL, dealer_total);
                db.update(DBHelper.TABLE_COMPONENT_ITEM, values, "_id = ?", new String[]{id});

            } while (c.moveToNext());
        }

        double can_sum = 0;
        double price = 0;
        double width = 0;
        id_n3 = 0;

        //Сюда считаем итоговую сумму полотна
        Log.d("mLog asdsssssss", String.valueOf(width_final));
        if (width_final.equals("")) {
            if (n3 == null) { //если новый расчёт
            } else {
                sqlQuewy = "select _id, price, width "
                        + "FROM rgzbn_gm_ceiling_canvases " +
                        "where _id = ? ";
                c = db.rawQuery(sqlQuewy, new String[]{n3});         // заполняем массивы из таблицы
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            id_n3 = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            price = Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                            width = Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(2))));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                //price = double_margin(price, gm_can_marg, dealer_can_marg) / 100 * 40;
                canvases_data.set(0, texture + ", " + canvases + ", " + width);                         // название
                canvases_data.set(1, Double.valueOf(S));                                             // кол-во
                canvases_data.set(2, price);                                                         // цена
                canvases_data.set(3, price * Double.valueOf(S));                                     // Кол-во * Себестоимость
                canvases_data.set(4, margin(price, gm_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
                canvases_data.set(5, Math.rint(100.0 * (margin(price, gm_can_marg)) * S) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                canvases_data.set(6, double_margin(price, gm_can_marg, dealer_can_marg) );            //Стоимость с маржой ГМ и дилера (для клиента)
                canvases_data.set(7, Math.rint(100 * (double_margin(price, gm_can_marg, dealer_can_marg)) * S) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

                can_sum = Double.parseDouble(String.valueOf(canvases_data.get(3)));

                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_COMP_ID, "canv");
                values.put(DBHelper.KEY_TITLE, String.valueOf(canvases_data.get(0)));
                values.put(DBHelper.KEY_QUANTITY, String.valueOf(canvases_data.get(1)));
                values.put(DBHelper.KEY_SELF_PRICE, String.valueOf(canvases_data.get(2)));
                values.put(DBHelper.KEY_SELF_TOTAL, String.valueOf(canvases_data.get(3)));
                values.put(DBHelper.KEY_GM_PRICE, String.valueOf(canvases_data.get(4)));
                values.put(DBHelper.KEY_GM_TOTAL, String.valueOf(canvases_data.get(5)));
                values.put(DBHelper.KEY_DEALER_PRICE, String.valueOf(canvases_data.get(6)));
                values.put(DBHelper.KEY_DEALER_TOTAL, String.valueOf(canvases_data.get(7)));
                db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);

                //Сюда считаем итоговую сумму обрезков
                Log.d("mLog", "square_obr " + square_obr );
                canvases_data.set(0, "Количесво обрезков");                 // название
                canvases_data.set(1, Double.valueOf(square_obr));           // кол-во
                canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                canvases_data.set(3, Math.rint(100 * (Double.valueOf(square_obr) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                canvases_data.set(4, Math.rint(100 * (margin(price, gm_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                canvases_data.set(5, Math.rint(100 * Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, gm_can_marg, dealer_can_marg) / 100 * 40, gm_can_marg, dealer_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                canvases_data.set(7, Math.rint(100 * (Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

                can_sum += Double.parseDouble(String.valueOf(canvases_data.get(3)));


                Log.d("mLog", "0 = " + square_obr + " " + price);
                Log.d("mLog", "0 = " + canvases_data.get(0));
                Log.d("mLog", "1 = " + canvases_data.get(1));
                Log.d("mLog", "2 = " + canvases_data.get(2));
                Log.d("mLog", "3 = " + canvases_data.get(3));
                Log.d("mLog", "4 = " + canvases_data.get(4));
                Log.d("mLog", "5 = " + canvases_data.get(5));
                Log.d("mLog", "6 = " + canvases_data.get(6));
                Log.d("mLog", "7 = " + canvases_data.get(7));

                values = new ContentValues();
                values.put(DBHelper.KEY_COMP_ID, "canv");
                values.put(DBHelper.KEY_TITLE, String.valueOf(canvases_data.get(0)));
                values.put(DBHelper.KEY_QUANTITY, String.valueOf(canvases_data.get(1)));
                values.put(DBHelper.KEY_SELF_PRICE, String.valueOf(canvases_data.get(2)));
                values.put(DBHelper.KEY_SELF_TOTAL, String.valueOf(canvases_data.get(3)));
                values.put(DBHelper.KEY_GM_PRICE, String.valueOf(canvases_data.get(4)));
                values.put(DBHelper.KEY_GM_TOTAL, String.valueOf(canvases_data.get(5)));
                values.put(DBHelper.KEY_DEALER_PRICE, String.valueOf(canvases_data.get(6)));
                values.put(DBHelper.KEY_DEALER_TOTAL, String.valueOf(canvases_data.get(7)));
                db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
            }
        } else {    //если изменён чертёж, то сюда
            double wf = Double.valueOf(width_final) / 100;
            sqlQuewy = "select _id, price "
                    + "FROM rgzbn_gm_ceiling_canvases " +
                    "where texture_id = ? and name LIKE('%" + sb + "%') and width =?";
            c = db.rawQuery(sqlQuewy, new String[]{texture_id, String.valueOf(wf)});         // заполняем массивы из таблицы
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        id_n3 = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        price = Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    } while (c.moveToNext());
                }
            }
            c.close();

            Log.d("mLog", String.valueOf(gm_can_marg));

            canvases_data.set(0, texture + ", " + canvases + ", " + wf);                         // название
            canvases_data.set(1, Double.valueOf(S));                                             // кол-во
            canvases_data.set(2, price);                                                         // цена
            canvases_data.set(3, price * Double.valueOf(S));                                     // Кол-во * Себестоимость
            canvases_data.set(4, margin(price, gm_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
            canvases_data.set(5, Math.rint(100.0 * (margin(price, gm_can_marg)) * S) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
            canvases_data.set(6, double_margin(price, gm_can_marg, dealer_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
            canvases_data.set(7, Math.rint(100 * (double_margin(price, gm_can_marg, dealer_can_marg)) * S) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

            can_sum = Double.parseDouble(String.valueOf(canvases_data.get(3)));


            Log.d("mLog", "0 = " + square_obr + " " + price);
            Log.d("mLog", "0 = " + canvases_data.get(0));
            Log.d("mLog", "1 = " + canvases_data.get(1));
            Log.d("mLog", "2 = " + canvases_data.get(2));
            Log.d("mLog", "3 = " + canvases_data.get(3));
            Log.d("mLog", "4 = " + canvases_data.get(4));
            Log.d("mLog", "5 = " + canvases_data.get(5));
            Log.d("mLog", "6 = " + canvases_data.get(6));
            Log.d("mLog", "7 = " + canvases_data.get(7));

            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_COMP_ID, "canv");
            values.put(DBHelper.KEY_TITLE, String.valueOf(canvases_data.get(0)));
            values.put(DBHelper.KEY_QUANTITY, String.valueOf(canvases_data.get(1)));
            values.put(DBHelper.KEY_SELF_PRICE, String.valueOf(canvases_data.get(2)));
            values.put(DBHelper.KEY_SELF_TOTAL, String.valueOf(canvases_data.get(3)));
            values.put(DBHelper.KEY_GM_PRICE, String.valueOf(canvases_data.get(4)));
            values.put(DBHelper.KEY_GM_TOTAL, String.valueOf(canvases_data.get(5)));
            values.put(DBHelper.KEY_DEALER_PRICE, String.valueOf(canvases_data.get(6)));
            values.put(DBHelper.KEY_DEALER_TOTAL, String.valueOf(canvases_data.get(7)));
            db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
        }

        //Сюда считаем итоговую сумму обрезков
        try {
            Log.d("mLog square_obr ", width_final + " " + square_obr + " " + texture_id);
            if (width_final.equals("") && square_obr.equals("0")) {
            } else {
                double wf = Double.valueOf(width_final) / 100;
               //sqlQuewy = "select price "
               //        + "FROM rgzbn_gm_ceiling_canvases " +
               //        "where texture_id = ?";
               //c = db.rawQuery(sqlQuewy, new String[]{texture_id});         // заполняем массивы из таблицы
               //if (c != null) {
               //    if (c.moveToFirst()) {
               //        do {
               //            price = Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
               //        } while (c.moveToNext());
               //    }
               //}
               //c.close();

                canvases_data.set(0, "Количесво обрезков");                 // название
                canvases_data.set(1, Double.valueOf(square_obr));           // кол-во
                canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                canvases_data.set(3, Math.rint(100 * (Double.valueOf(square_obr) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                canvases_data.set(4, Math.rint(100 * (margin(price, gm_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                canvases_data.set(5, Math.rint(100 * Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, gm_can_marg, dealer_can_marg) / 100 * 40, gm_can_marg, dealer_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                canvases_data.set(7, Math.rint(100 * (Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

                can_sum += Double.parseDouble(String.valueOf(canvases_data.get(3)));

                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_COMP_ID, "canv");
                values.put(DBHelper.KEY_TITLE, String.valueOf(canvases_data.get(0)));
                values.put(DBHelper.KEY_QUANTITY, String.valueOf(canvases_data.get(1)));
                values.put(DBHelper.KEY_SELF_PRICE, String.valueOf(canvases_data.get(2)));
                values.put(DBHelper.KEY_SELF_TOTAL, String.valueOf(canvases_data.get(3)));
                values.put(DBHelper.KEY_GM_PRICE, String.valueOf(canvases_data.get(4)));
                values.put(DBHelper.KEY_GM_TOTAL, String.valueOf(canvases_data.get(5)));
                values.put(DBHelper.KEY_DEALER_PRICE, String.valueOf(canvases_data.get(6)));
                values.put(DBHelper.KEY_DEALER_TOTAL, String.valueOf(canvases_data.get(7)));
                db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
            }
        } catch (Exception e) {
        }

        //Сюда считаем итоговую сумму компонентов
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_components_option";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    if (component_count.get(Integer.parseInt(id)) != 0) {
                        String title = c.getString(c.getColumnIndex(c.getColumnName(2)));
                        String stack = "0";
                        String component_id = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        String self_price = c.getString(c.getColumnIndex(c.getColumnName(3)));

                        sqlQuewy = "select * "
                                + "FROM rgzbn_gm_ceiling_components " +
                                "where _id = ? ";
                        Cursor k = db.rawQuery(sqlQuewy, new String[]{component_id});         // заполняем массивы из таблицы
                        if (k != null) {
                            if (k.moveToFirst()) {
                                do {
                                    String full_name = component_id + " " + k.getString(k.getColumnIndex(k.getColumnName(1))) + " " + title;

                                    String unit = k.getString(k.getColumnIndex(k.getColumnName(2)));
                                    String quantity = "";
                                    if (unit.equals("шт.")) {
                                        quantity = String.valueOf(Math.ceil(component_count.get(Integer.parseInt(id))));
                                    } else {
                                        quantity = String.valueOf(component_count.get(Integer.parseInt(id)));
                                    }

                                    String self_total = String.valueOf(Math.round((Double.parseDouble(self_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                                    String gm_price = String.valueOf(margin(Double.parseDouble(self_price), gm_comp_marg));
                                    String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                                    String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), gm_comp_marg, dealer_comp_marg));
                                    String dealer_total = String.valueOf(Math.round((Double.parseDouble(dealer_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.KEY_TITLE, full_name);
                                    values.put(DBHelper.KEY_UNIT, unit);
                                    values.put(DBHelper.KEY_COMP_ID, id);
                                    values.put(DBHelper.KEY_QUANTITY, quantity);
                                    values.put(DBHelper.KEY_STACK, stack);
                                    values.put(DBHelper.KEY_SELF_PRICE, self_price);
                                    values.put(DBHelper.KEY_SELF_TOTAL, self_total);
                                    values.put(DBHelper.KEY_GM_PRICE, gm_price);
                                    values.put(DBHelper.KEY_GM_TOTAL, gm_total);
                                    values.put(DBHelper.KEY_DEALER_PRICE, dealer_price);
                                    values.put(DBHelper.KEY_DEALER_TOTAL, dealer_total);
                                    db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);

                                } while (k.moveToNext());
                            }
                        }
                    }
                } while (c.moveToNext());
            }
        }

        if (rb_vstavka.equals("0")) {
        } else {
            sqlQuewy = "select * "
                    + "FROM rgzbn_gm_ceiling_components_option " +
                    "where title = ?";
            c = db.rawQuery(sqlQuewy, new String[]{id_color_vs});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        String full_name = "Вставка " + id_color_vs;
                        String stack = "0";

                        String unit = "м.п.";

                        String self_price = c.getString(c.getColumnIndex(c.getColumnName(3)));
                        double quantity = 0;

                        quantity = component_count.get(Integer.parseInt(id_color_vs));

                        String self_total = String.valueOf(Math.round((Double.parseDouble(self_price) * quantity) * 100.0) / 100.0);

                        String gm_price = String.valueOf(margin(Double.parseDouble(self_price), gm_comp_marg));
                        String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * quantity) * 100.0) / 100.0);

                        String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), gm_comp_marg, dealer_comp_marg));
                        String dealer_total = String.valueOf(Math.round((Double.parseDouble(dealer_price) * quantity) * 100.0) / 100.0);

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_TITLE, full_name);
                        values.put(DBHelper.KEY_UNIT, "");
                        values.put(DBHelper.KEY_COMP_ID, id);
                        values.put(DBHelper.KEY_QUANTITY, quantity);
                        values.put(DBHelper.KEY_STACK, stack);
                        values.put(DBHelper.KEY_SELF_PRICE, self_price);
                        values.put(DBHelper.KEY_SELF_TOTAL, self_total);
                        values.put(DBHelper.KEY_GM_PRICE, gm_price);
                        values.put(DBHelper.KEY_GM_TOTAL, gm_total);
                        values.put(DBHelper.KEY_DEALER_PRICE, dealer_price);
                        values.put(DBHelper.KEY_DEALER_TOTAL, dealer_total);
                        db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);

                    } while (c.moveToNext());
                }
            }
        }

        sqlQuewy = "select * "
                + "FROM table_other_work";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String title = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    String stack = "0";
                    String quantity = "1";
                    String dealer_price = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    String dealer_total = String.valueOf(Double.parseDouble(dealer_price) * Integer.valueOf(quantity));
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_TITLE, title);
                    values.put(DBHelper.KEY_QUANTITY, quantity);
                    values.put(DBHelper.KEY_STACK, stack);
                    values.put(DBHelper.KEY_DEALER_PRICE, dealer_price);
                    values.put(DBHelper.KEY_DEALER_TOTAL, dealer_total);
                    db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
                } while (c.moveToNext());
            }
        }

        Cursor cursor = db.query(DBHelper.TABLE_COMPONENT_ITEM, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int title = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int unit = cursor.getColumnIndex(DBHelper.KEY_UNIT);
            int comp_id = cursor.getColumnIndex(DBHelper.KEY_COMP_ID);
            int quantity = cursor.getColumnIndex(DBHelper.KEY_QUANTITY);
            int stack = cursor.getColumnIndex(DBHelper.KEY_STACK);
            int self_price = cursor.getColumnIndex(DBHelper.KEY_SELF_PRICE);
            int self_total = cursor.getColumnIndex(DBHelper.KEY_SELF_TOTAL);
            int gm_price = cursor.getColumnIndex(DBHelper.KEY_GM_PRICE);
            int gm_total = cursor.getColumnIndex(DBHelper.KEY_GM_TOTAL);
            int dealer_price = cursor.getColumnIndex(DBHelper.KEY_DEALER_PRICE);
            int dealer_total = cursor.getColumnIndex(DBHelper.KEY_DEALER_TOTAL);
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", наименование = " + cursor.getString(title) +
                        ", количество = " + cursor.getString(quantity) +
                        ", цена, руб = " + cursor.getString(dealer_price) +
                        ", стоимость, руб = " + cursor.getString(dealer_total));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

        Log.d("mLog", "-------------------------------------------------------------------------------");

        //---------------------------------- РАСЧЕТ СТОИМОСТИ МОНТАЖА --------------------------------------//


        double cut = 0;
        //внутренний вырез ТОЛЬКО ДЛЯ ПВХ
        if (ed_in_cut.getText().toString().equals("")) {
        } else {
            cut = Double.valueOf(ed_in_cut.getText().toString());
            if (n1.equals("28") && (cut > 0)) {
                ContentValues values = new ContentValues();

                values.put(DBHelper.KEY_TITLE, "Внутренний вырез для ПВХ");
                values.put(DBHelper.KEY_QUANTITY, cut);
                values.put(DBHelper.KEY_GM_SALARY, results.get(21));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, cut * results.get(21));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(21));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, cut * results.get(21));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        //периметр только для ПВХ
        if (n1.equals("28") && P > 0 && rb_baget.equals("0")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(0));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(0));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }
        if (n1.equals("28") && P > 0 && rb_baget.equals("1")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(30));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(30));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(30));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(30));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }
        if (n1.equals("28") && P > 0 && rb_baget.equals("2")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(31));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(31));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(31));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(31));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        //вставка
        if (rb_vstavka.equals("1")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Вставка");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(9));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(9));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(9));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(9));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }


        double mount_w = 0;
        if (mount_wall.getText().toString().equals("") || mount_wall.getText().toString().equals("0")) {
        } else {
            mount_w = Double.valueOf(mount_wall.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Крепление в плитку");
            values.put(DBHelper.KEY_QUANTITY, Double.valueOf(mount_w));
            values.put(DBHelper.KEY_GM_SALARY, results.get(12));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, Double.valueOf(mount_w) * results.get(12));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(12));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, Double.valueOf(mount_w) * results.get(12));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        double mount_g = 0;
        if (mount_granite.getText().toString().equals("") || mount_granite.getText().toString().equals("0")) {
        } else {
            mount_g = Double.valueOf(mount_granite.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Крепление в керамогранит");
            values.put(DBHelper.KEY_QUANTITY, Double.valueOf(mount_g));
            values.put(DBHelper.KEY_GM_SALARY, results.get(13));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, Double.valueOf(mount_g) * results.get(13));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(13));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, Double.valueOf(mount_g) * results.get(13));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }


        if (lustr.getText().toString().equals("") || lustr.getText().toString().equals("0")) {
        } else {
            count_lustr = Integer.parseInt(lustr.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка люстр");
            values.put(DBHelper.KEY_QUANTITY, Integer.valueOf(count_lustr));
            values.put(DBHelper.KEY_GM_SALARY, results.get(1));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, Integer.valueOf(count_lustr) * results.get(1));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(1));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, Integer.valueOf(count_lustr) * results.get(1));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (count_svet > 0) {
            Log.d("mLog svet", String.valueOf(count_svet));
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка светильников");
            values.put(DBHelper.KEY_QUANTITY, count_svet);
            values.put(DBHelper.KEY_GM_SALARY, Math.max(results.get(3), results.get(4)));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_svet * Math.max(results.get(3), results.get(4)));
            values.put(DBHelper.KEY_DEALER_SALARY, Math.max(results.get(3), results.get(4)));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_svet * Math.max(results.get(3), results.get(4)));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (count_vent > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка вентиляции");
            values.put(DBHelper.KEY_QUANTITY, count_vent);
            values.put(DBHelper.KEY_GM_SALARY, results.get(11));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_vent * results.get(11));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(11));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_vent * results.get(11));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (count_electr > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка электровытяжки");
            values.put(DBHelper.KEY_QUANTITY, count_electr);
            values.put(DBHelper.KEY_GM_SALARY, results.get(15));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_electr * results.get(15));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(15));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_electr * results.get(15));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (count_diffus > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка диффузора");
            values.put(DBHelper.KEY_QUANTITY, count_diffus);
            values.put(DBHelper.KEY_GM_SALARY, results.get(18));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_diffus * results.get(18));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(18));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_diffus * results.get(18));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (count_pipes > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Обвод трубы");
            values.put(DBHelper.KEY_QUANTITY, count_pipes);
            values.put(DBHelper.KEY_GM_SALARY, results.get(7));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_pipes * results.get(7));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(7));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_pipes * results.get(7));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        double length_karniz = 0;
        if (karniz.getText().toString().equals("")) {
        } else {
            length_karniz = Double.valueOf(karniz.getText().toString());
            if (length_karniz > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Шторный карниз");
                values.put(DBHelper.KEY_QUANTITY, length_karniz);
                values.put(DBHelper.KEY_GM_SALARY, results.get(10));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, length_karniz * results.get(10));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(10));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, length_karniz * results.get(10));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        double countbond_beam = 0;
        if (bond_beam.getText().toString().equals("")) {
        } else {
            countbond_beam = Double.valueOf(bond_beam.getText().toString());
            if (countbond_beam > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Закладная брусом");
                values.put(DBHelper.KEY_QUANTITY, countbond_beam);
                values.put(DBHelper.KEY_GM_SALARY, results.get(10));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, countbond_beam * results.get(10));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(10));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, countbond_beam * results.get(10));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        if (ed_wall.getText().toString().equals("")) {
        } else {
            count_wall = Double.parseDouble(ed_wall.getText().toString());
            if (count_wall > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Укрепление стены");
                values.put(DBHelper.KEY_QUANTITY, count_wall);
                values.put(DBHelper.KEY_GM_SALARY, results.get(14));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_wall * results.get(14));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(14));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_wall * results.get(14));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        if (ed_separator.getText().toString().equals("")) {
        } else {
            count_separator = Double.parseDouble(ed_separator.getText().toString());
            if (count_wall > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Разделитель");
                values.put(DBHelper.KEY_QUANTITY, count_separator);
                values.put(DBHelper.KEY_GM_SALARY, results.get(8));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_separator * results.get(8));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(8));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_separator * results.get(8));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        if (ed_fire.getText().toString().equals("")) {
        } else {
            count_fire = Double.parseDouble(ed_fire.getText().toString());
            if (count_fire > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Пожарная сигнализация");
                values.put(DBHelper.KEY_QUANTITY, count_fire);
                values.put(DBHelper.KEY_GM_SALARY, results.get(5));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_fire * results.get(5));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(5));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_fire * results.get(5));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        double count_s =0;
        if (soaring_ceiling.getText().toString().equals("")) {
        } else {
            count_s = Double.parseDouble(soaring_ceiling.getText().toString());
            if (count_s > 0) {
                Log.d("mLog", String.valueOf(results.get(29)));
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Парящий потолок");
                values.put(DBHelper.KEY_QUANTITY, count_s);
                values.put(DBHelper.KEY_GM_SALARY, results.get(29));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_s * results.get(29));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(29));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_s * results.get(29));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        double count_diff = 0;
        if (ed_diff_acc.getText().toString().equals("")) {
        } else {
            count_diff = Double.parseDouble(ed_diff_acc.getText().toString());
            if (count_wall > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Сложность доступа");
                values.put(DBHelper.KEY_QUANTITY, count_diff);
                values.put(DBHelper.KEY_GM_SALARY, results.get(16));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_diff * results.get(16));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(16));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_diff * results.get(16));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        if (ed_fasteners.getText().toString().equals("")) {
        } else {
            count_fasteners = Double.parseDouble(ed_fasteners.getText().toString());
            if (count_wall > 0) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_TITLE, "Дополнительный крепеж");
                values.put(DBHelper.KEY_QUANTITY, count_fasteners);
                values.put(DBHelper.KEY_GM_SALARY, results.get(17));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_fasteners * results.get(17));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(17));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_fasteners * results.get(17));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
            }
        }

        //щепотка дополнительных копмлектующих
        sqlQuewy = "select * "
                + "FROM table_other_comp";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String title = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    String quantity = "1";
                    String dealer_price = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    String dealer_total = String.valueOf(Double.parseDouble(dealer_price) * Integer.valueOf(quantity));
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_TITLE, title);
                    values.put(DBHelper.KEY_QUANTITY, quantity);
                    values.put(DBHelper.KEY_GM_SALARY, dealer_price);
                    values.put(DBHelper.KEY_GM_SALARY_TOTAL, dealer_total);
                    values.put(DBHelper.KEY_DEALER_SALARY, dealer_price);
                    values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, dealer_total);
                    db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select * " +
                "FROM mounting_data";
        Cursor k = db.rawQuery(sqlQuewy, new String[]{});         // заполняем массивы из табли
        if (k != null)
            if (k.moveToFirst()) {
                do {

                    String id = k.getString(k.getColumnIndex(k.getColumnName(0)));
                    String quantity = k.getString(k.getColumnIndex(k.getColumnName(2)));

                    String gm_salary_total = String.valueOf(Math.round(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))) * 100.0) / 100.0);
                    String dealer_salary_total = String.valueOf(Math.round(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(6)))) * 100.0) / 100.0);

                    String price_with_gm_margin = String.valueOf(margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))), gm_mount_marg));
                    String total_with_gm_margin = String.valueOf(Math.round((Double.parseDouble(quantity) * Double.parseDouble(price_with_gm_margin)) * 100.0) / 100.0);

                    String price_with_gm_dealer_margin = String.valueOf(double_margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))), gm_mount_marg, dealer_mount_marg));
                    String total_with_gm_dealer_margin = String.valueOf(Math.round((Double.parseDouble(quantity) * Double.parseDouble(price_with_gm_dealer_margin)) * 100.0) / 100.0);

                    String price_with_dealer_margin = String.valueOf(margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(5)))), dealer_mount_marg));
                    String total_with_dealer_margin = String.valueOf(Math.round((Double.parseDouble(quantity) * Double.parseDouble(price_with_dealer_margin)) * 100.0) / 100.0);

                    ContentValues values = new ContentValues();
                    values.put(dbHelper.KEY_PRICE_WITH_GM_MARGIN, price_with_gm_margin);
                    values.put(dbHelper.KEY_TOTAL_WITH_GM_MARGIN, total_with_gm_margin);
                    values.put(dbHelper.KEY_PRICE_WITH_GM_DEALER_MARGIN, price_with_gm_dealer_margin);
                    values.put(dbHelper.KEY_TOTAL_WITH_GM_DEALER_MARGIN, total_with_gm_dealer_margin);
                    values.put(dbHelper.KEY_PRICE_WITH_DEALER_MARGIN, price_with_dealer_margin);
                    values.put(dbHelper.KEY_TOTAL_WITH_DEALER_MARGIN, total_with_dealer_margin);

                    db.update(DBHelper.TABLE_MOUNTING_DATA, values, "_id = ?", new String[]{id});

                } while (k.moveToNext());
            }

        cursor = db.query(DBHelper.TABLE_MOUNTING_DATA, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int title = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int quantity = cursor.getColumnIndex(DBHelper.KEY_QUANTITY);
            int dealer_price = cursor.getColumnIndex(DBHelper.KEY_PRICE_WITH_DEALER_MARGIN);
            int dealer_total = cursor.getColumnIndex(DBHelper.KEY_TOTAL_WITH_DEALER_MARGIN);
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", наименование = " + cursor.getString(title) +
                        ", количество = " + cursor.getString(quantity) +
                        ", цена, руб = " + cursor.getString(dealer_price) +
                        ", стоимость, руб = " + cursor.getString(dealer_total));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

        // подсчёт всех комплектующих
        double canvases_sum_total = 0;
        double total_sum = 0;
        double components_sum = 0;
        double gm_components_sum = 0;
        double dealer_components_sum = 0;

        cursor = db.query(DBHelper.TABLE_COMPONENT_ITEM, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int comp_id = cursor.getColumnIndex(DBHelper.KEY_COMP_ID);
            int self_total = cursor.getColumnIndex(DBHelper.KEY_SELF_TOTAL);
            int gm_total = cursor.getColumnIndex(DBHelper.KEY_GM_TOTAL);
            int dealer_total = cursor.getColumnIndex(DBHelper.KEY_DEALER_TOTAL);
            do {
                if (cursor.getString(self_total) == null) {
                } else {
                    if (cursor.getString(comp_id).equals("canv")) {
                        canvases_sum_total += Double.valueOf(cursor.getString(self_total));
                        Log.d("self_total", " canvases_sum_total " + cursor.getString(comp_id));
                    }
                    Log.d("self_total", " 1 " + String.valueOf(canvases_sum_total));
                    components_sum += Double.valueOf(cursor.getString(self_total));
                    Log.d("self_total", " 2 " + String.valueOf(components_sum));
                    components_sum = Math.round(components_sum * 100.0) / 100.0;
                    Log.d("self_total", " 3 " + String.valueOf(components_sum));
                }

                if (cursor.getString(gm_total) == null) {
                } else
                    gm_components_sum += Double.valueOf(cursor.getString(gm_total));

                if (cursor.getString(dealer_total) == null) {
                } else
                    dealer_components_sum += Double.valueOf(cursor.getString(dealer_total));

                //Log.d("mLog suuuuuuuum1", String.valueOf(components_sum));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

        //...и монтаж дилера с помощью ГМ
        double total_gm_mounting = 0;
        double total_dealer_mounting = 0;
        double total_with_gm_margin = 0;
        double total_with_gm_dealer_margin = 0;
        double total_with_dealer_margin = 0;

        cursor = db.query(DBHelper.TABLE_MOUNTING_DATA, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int gm = cursor.getColumnIndex(DBHelper.KEY_GM_SALARY_TOTAL);
            int dealer = cursor.getColumnIndex(DBHelper.KEY_DEALER_SALARY_TOTAL);
            int gm_dealer = cursor.getColumnIndex(DBHelper.KEY_TOTAL_WITH_GM_MARGIN);
            int gm_dealer_margin = cursor.getColumnIndex(DBHelper.KEY_TOTAL_WITH_GM_DEALER_MARGIN);
            int dealer_margin = cursor.getColumnIndex(DBHelper.KEY_TOTAL_WITH_DEALER_MARGIN);
            do {
                if (cursor.getString(gm) == null) {
                } else
                    total_gm_mounting += Double.valueOf(cursor.getString(gm));
                total_gm_mounting = Math.round(total_gm_mounting * 100.0) / 100.0;

                if (cursor.getString(dealer) == null) {
                } else
                    total_dealer_mounting += Double.valueOf(cursor.getString(dealer));

                if (cursor.getString(gm_dealer) == null) {
                } else
                    total_with_gm_margin += Double.valueOf(cursor.getString(gm_dealer));

                if (cursor.getString(gm_dealer_margin) == null) {
                } else
                    total_with_gm_dealer_margin += Double.valueOf(cursor.getString(gm_dealer_margin));

                if (cursor.getString(dealer_margin) == null) {
                } else
                    total_with_dealer_margin += Double.valueOf(cursor.getString(dealer_margin));

                //Log.d("mLog suuuuuuuum2", String.valueOf(gm));
            } while (cursor.moveToNext());
        } else

            Log.d("mLog", "0 rows");
        cursor.close();

        if (!mounting) {
            total_with_dealer_margin = 0;
            total_gm_mounting = 0;
        }

        String ts = "";

        total_sum += Math.round((dealer_components_sum + total_with_dealer_margin) * 100.0) / 100.0;
        ts = String.valueOf(total_sum);

        String dis = ed_discount.getText().toString();
        if (dis.length() > 0) {
            ts = String.valueOf(Double.parseDouble(String.valueOf(Math.round(total_sum) * 100.0 / 100)) + "руб. / \n"
                    + Double.parseDouble(String.valueOf((Math.round(total_sum - (total_sum / 100 * Integer.valueOf(dis))) * 100.0) / 100)));
        } else {
            dis = "0";
        }

        text_calculate.setText(ts + " руб.");

        String ll = "";
        try {
            ll = lines_length.replaceAll("name", "");
            ll = ll.replaceAll("length", "");
            ll = ll.replaceAll("\"", "");
            ll = ll.replaceAll("]", "");
            ll = ll.replaceAll(":", "");
            ll = ll.replaceAll("\\[", "");
            ll = ll.replaceAll("\\{", "");
            ll = ll.replaceAll("\\}", "");
            int ii = 0;
            sb = new StringBuffer();
            for (i = 0; i < ll.length(); ) {
                if (ll.startsWith(",", i) && ii == 0) {
                    sb.append("=");
                    ii++;
                } else if (ll.startsWith(",", i) && ii == 1) {
                    sb.append(";");
                    ii--;
                } else {
                    sb.append(ll.charAt(i));
                }
                i++;
            }

            ll = String.valueOf(sb) + ";";
        } catch (Exception e) {
        }

        if (calculat) {
            int id_color = 0;
            int id_color_vs_int = 0;

            try {
                SPSO = getSharedPreferences("color_title", MODE_PRIVATE);
                id_color = Integer.valueOf(SPSO.getString("", ""));

                SPSO = getSharedPreferences("color_title_vs", MODE_PRIVATE);
                id_color_vs_int = Integer.valueOf(SPSO.getString("", ""));

                Log.d("mLog", "id_color_vs_int1 " + id_color_vs_int);
            } catch (Exception e) {

            }

            if (name_project.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Укажите название потолка ", Toast.LENGTH_SHORT);
                toast.show();
            } else if (imag.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Постройте чертёж ", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (square_obr == null) {
                    square_obr = "0";
                }

                String save ="{"+ SAVED_KP.substring(1, SAVED_KP.length()-1)+"}";
                StringBuffer sbb = new StringBuffer();
                cou=0;

                for (i = 0; i < save.length(); ) {
                    if (save.startsWith("[", i) && save.startsWith("{", i+1)) {
                        cou++;
                        sbb.append("\"kp"+cou+"\"").append(':').append('[');
                    } else {
                        sbb.append(save.charAt(i));
                    }
                    i++;
                }

                save = String.valueOf(sbb);

                Log.d("mLog", save);

                String cut_d = "";

                for (i=1;i<cou+1;i++) {

                    cut_d += "Полотно"+i+": ";

                    try {

                        JSONObject jsonObject = new JSONObject(save);

                        JSONArray kp = jsonObject.getJSONArray("kp"+i);

                        for (int y = 0; y < kp.length(); y++) {

                            org.json.JSONObject kp1 = kp.getJSONObject(y);

                            String name = kp1.getString("name");
                            String koordinats = kp1.getString("koordinats");

                            cut_d += " " + name + koordinats + ",";
                        }
                    } catch (Exception e) {
                    }

                    cut_d = cut_d.substring(0, cut_d.length()-1);
                    cut_d += "; ";
                }

                String original_sk ="";
                try {
                    save = "{wp:"+ SAVED_WP+"}";
                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray wp = jsonObject.getJSONArray("wp");

                    for (int y = 0; y < wp.length(); y++) {

                        org.json.JSONObject wp1 = wp.getJSONObject(y);

                        String s0_x = wp1.getString("s0_x");
                        String s0_y = wp1.getString("s0_y");
                        String s1_x = wp1.getString("s1_x");
                        String s1_y = wp1.getString("s1_y");

                        original_sk+= s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e){
                }

                original_sk +="||";
                try {
                    save = "{dp:"+ SAVED_DP+"}";

                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray dp = jsonObject.getJSONArray("dp");

                    for (int y = 0; y < dp.length(); y++) {

                        org.json.JSONObject dp1 = dp.getJSONObject(y);

                        String s0_x = dp1.getString("s0_x");
                        String s0_y = dp1.getString("s0_y");
                        String s1_x = dp1.getString("s1_x");
                        String s1_y = dp1.getString("s1_y");

                        original_sk+= s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e){
                }

                original_sk +="||";
                try {
                    save = "{pt_p:"+ SAVED_PT_P+"}";

                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray pt_p = jsonObject.getJSONArray("pt_p");

                    for (int y = 0; y < pt_p.length(); y++) {

                        org.json.JSONObject pt_p1 = pt_p.getJSONObject(y);

                        String x = pt_p1.getString("x");
                        String yy = pt_p1.getString("y");

                        original_sk+= x + ";" + yy + ";";
                    }
                } catch (Exception e){
                }

                original_sk += "||" + SAVED_CODE + "||" + SAVED_ALFAVIT;

                int ordering = 0;
                int state = 1;
                int checked_out = 0;
                String checked_out_time = "0000-00-00 00:00:00";
                int created_by = Integer.parseInt(gager_id);
                int modified_by = Integer.parseInt(gager_id);
                String calculation_title = name_project.getText().toString();
                int project_id = Integer.parseInt(id_cl);
                int n1 = 28;
                int n2 = Integer.valueOf(texture_id);
                int n3 = id_n3;
                String n4 = String.valueOf(S);
                String n5 = String.valueOf(P);
                int n6 = id_color_vs_int;
                String n7 = String.valueOf(mount_w);
                String n8 = String.valueOf(mount_g);
                String n9 = String.valueOf(Angle);
                String n10 = "0";
                String n11 = String.valueOf(cut);
                int n12 = Integer.valueOf(count_lustr);
                String n16 = String.valueOf(int_rb_k);
                String n17 = String.valueOf(count_bond_beam);
                String n18 = String.valueOf(count_wall);
                String n19 = String.valueOf(count_cabling);
                String n20 = String.valueOf(count_separator);
                String n21 = String.valueOf(count_fire);
                String n24 = String.valueOf(count_diff);
                int n25 = 0;
                String n27 = String.valueOf(length_karniz);
                String n28 = String.valueOf(rb_baget);
                String n30 = String.valueOf(count_s);
                String components_sum_total = String.valueOf(Math.round((components_sum - canvases_sum_total) * 100.0) / 100.0);
                String canvases_sum = String.valueOf(Math.round(can_sum * 100.0) / 100.0);
                int mounting_sum = (int) Math.ceil(Math.round(total_gm_mounting * 100.0) / 100.0);
                String dop_krepezh = String.valueOf(count_fasteners);
                String extra_components = "";
                String extra_mounting = "";
                int color = id_color;
                String details = "";
                String calc_image = String.valueOf(imag);
                String calc_data = ll;
                String calc_point = "";
                String cut_image = String.valueOf(imag_cut);
                String cut_data = cut_d;
                String offcut_square = square_obr;
                String original_sketch = original_sk;
                String discount = dis;

                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_ORDERING, ordering);
                values.put(DBHelper.KEY_STATE, state);
                values.put(DBHelper.KEY_CHECKED_OUT, checked_out);
                values.put(DBHelper.KEY_CHECKED_OUT_TIME, checked_out_time);
                values.put(DBHelper.KEY_CREATED_BY, created_by);
                values.put(DBHelper.KEY_MODIFIED_BY, modified_by);
                values.put(DBHelper.KEY_CALCULATION_TITLE, calculation_title);
                values.put(DBHelper.KEY_PROJECT_ID, project_id);
                values.put(DBHelper.KEY_N1, n1);
                values.put(DBHelper.KEY_N2, n2);
                values.put(DBHelper.KEY_N3, n3);
                values.put(DBHelper.KEY_N4, n4);
                values.put(DBHelper.KEY_N5, n5);
                values.put(DBHelper.KEY_N6, n6);
                values.put(DBHelper.KEY_N7, n7);
                values.put(DBHelper.KEY_N8, n8);
                values.put(DBHelper.KEY_N9, n9);
                values.put(DBHelper.KEY_N10, n10);
                values.put(DBHelper.KEY_N11, n11);
                values.put(DBHelper.KEY_N12, n12);
                values.put(DBHelper.KEY_N16, n16);
                values.put(DBHelper.KEY_N17, n17);
                values.put(DBHelper.KEY_N18, n18);
                values.put(DBHelper.KEY_N19, n19);
                values.put(DBHelper.KEY_N20, n20);
                values.put(DBHelper.KEY_N21, n21);
                values.put(DBHelper.KEY_N24, n24);
                values.put(DBHelper.KEY_N25, n25);
                values.put(DBHelper.KEY_N27, n27);
                values.put(DBHelper.KEY_N28, n28);
                values.put(DBHelper.KEY_N30, n30);
                values.put(DBHelper.KEY_COMPONENTS_SUM, components_sum_total);
                values.put(DBHelper.KEY_CANVASES_SUM, canvases_sum);
                values.put(DBHelper.KEY_MOUNTING_SUM, mounting_sum);
                values.put(DBHelper.KEY_DOP_KREPEZH, dop_krepezh);
                values.put(DBHelper.KEY_EXTRA_COMPONENTS, extra_components);
                values.put(DBHelper.KEY_EXTRA_MOUNTING, extra_mounting);
                values.put(DBHelper.KEY_COLOR, color);
                values.put(DBHelper.KEY_DETAILS, details);
                values.put(DBHelper.KEY_CALC_IMAGE, calc_image);
                values.put(DBHelper.KEY_CALC_DATA, calc_data);
                values.put(DBHelper.KEY_CALC_POINT, calc_point);
                values.put(DBHelper.KEY_CUT_IMAGE, cut_image);
                values.put(DBHelper.KEY_CUT_DATA, cut_data);
                values.put(DBHelper.KEY_OFFCUT_SQUARE, offcut_square);
                values.put(DBHelper.KEY_ORIGINAL_SKETCH, original_sketch);
                values.put(DBHelper.KEY_DISCOUNT, discount);

                calc = getIntent().getStringExtra("calc");  // id_calculation
                if (calc != null) {
                    db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{calc});

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Расчёт обновлён ", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //values.put(DBHelper.KEY_ID, max_id);
                    db = dbHelper.getReadableDatabase();
                    //values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_calculations " +
                                "where _id>? and _id<?";
                        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(gager_id_int), String.valueOf(gager_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e) {
                        max_id_contac = gager_id_int + 1;
                    }
                    Log.d("mLog", "calc1 = " + max_id_contac);

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Расчёт добавлен ", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (id_cl != null) {
                    SharedPreferences SP = getSharedPreferences("end_activity_inform_proj", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", "1");
                    ed.commit();
                }

                finish();
            }
        }
    }

    void rouding(int id, double coun, double value) {

        double count = Math.floor(coun / value);

        if ((coun / value) > count) {
            count++;
        }

        component_count.set(id, count * value);

    }

    double margin(double val, int mar) {

        return val * 100 / (100 - mar);
    }

    double double_margin(double val, int mar, int mar2) {

        return (val * 100 / (100 - mar)) * 100 / (100 - mar2);
    }

    //кольцо
    void components_circle(int j, int id, int id2, int[] count, String[] sizes) {

        String[] size = new String[sizes.length];
        for (int i = 0; i < sizes.length - 1; i++) {
            size[i] = sizes[i];
        }

        int[] counts = new int[count.length];
        for (int i = 0; i < count.length - 1; i++) {
            counts[i] = count[i];
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i = 0;
        String sqlQuewy = "select title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        i++;

        //  круглое кольцо
        ar1_size = new double[i];
        ar2_size = new double[i];
        int[] ar1_id = new int[i];
        int[] ar2_id = new int[i];
        i = 0;
        sqlQuewy = "select _id, title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    ar1_id[i] = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    try {
                        ar1_size[i] = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(1))).replace(",", "."));
                    } catch (Exception e) {
                        ar1_size[i] = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(1))));
                    }

                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        int y = 0;
        for (int k = 0; k < j; k++) {
            double min = ar1_size[i - 1];
            String tmp = String.valueOf(size[k]);
            if (tmp.length() < 4) {
                for (int l = 0; l < i; l++) {
                    if ((Double.parseDouble(size[k]) <= ar1_size[l]) && (min >= ar1_size[l])) {
                        min = ar1_size[l];
                        y = l;
                    }
                }
                component_count.set(ar1_id[y], component_count.get(ar1_id[y]) + 1 * counts[k]);
            }
        }

        // круглая платформа
        i = 0;
        sqlQuewy = "select _id, title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id2)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    //выводим внешний радиус круглой платформы
                    String tmp = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    String tmp2 = "";
                    int pos = 0;
                    for (int l = 0; l < tmp.length(); l++) {
                        pos = tmp.indexOf("-") + 1;
                    }
                    StringBuffer sb = new StringBuffer();
                    char[] chars = tmp.toCharArray();
                    for (int s = 0; pos < tmp.length(); pos++) {
                        sb.append(chars[pos]);
                    }

                    tmp2 = String.valueOf(sb);
                    ar2_size[i] = Integer.valueOf(tmp2);
                    ar2_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));

                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        for (int k = 0; k < j; k++) {
            double min = 1000;
            String tmp = String.valueOf(size[k]);
            boolean flag = true;
            if (tmp.length() < 4) {
                for (int l = 0; l < i; l++) {
                    if ((Double.parseDouble(size[k]) <= ar2_size[l]) && (min >= ar2_size[l])) {
                        min = ar2_size[l];
                        flag = false;
                        y = l;
                    }
                }
                if (!flag) {
                    component_count.set(ar2_id[y], component_count.get(ar2_id[y]) + 1 * counts[k]);
                }
                if (flag) {
                    component_count.set(items_1, component_count.get(items_1) + 1 * counts[k]);
                }
            }
        }


    }

    //квадрат
    void components_square(int j, int id, int id2, int[] count, String[] sizes) {

        String[] size = new String[sizes.length];
        for (int i = 0; i < sizes.length - 1; i++) {
            size[i] = sizes[i];
        }

        int[] counts = new int[count.length];
        for (int i = 0; i < count.length - 1; i++) {
            counts[i] = count[i];
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int i = 0;
        String sqlQuewy = "select title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        i++;

        //термоквадраты из бд
        ar1_size = new double[i];
        ar2_size = new double[i];
        ar2_size2 = new double[i];
        int[] ar1_id = new int[i];
        int[] ar2_id = new int[i];
        i = 0;
        sqlQuewy = "select _id, title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    if (c.getString(c.getColumnIndex(c.getColumnName(1))).length() > 3) {
                        String tmp = c.getString(c.getColumnIndex(c.getColumnName(1)));

                        String tmp2 = "";
                        int pos = 0;
                        for (int l = 0; l < tmp.length(); l++) {
                            pos = tmp.indexOf("*");
                        }

                        StringBuffer sb = new StringBuffer();
                        char[] chars = tmp.toCharArray();
                        for (int s = 0; s < pos; s++) {
                            sb.append(chars[s]);
                        }

                        tmp2 = String.valueOf(sb);
                        ar1_size[i] = Double.parseDouble(tmp2);
                        ar1_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));

                    }

                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        for (int k = 0; k < j; k++) {
            if (size[k].length() > 3) {
                String tmp = String.valueOf(size[k]);
                String tmp2 = "";
                int pos = 0;
                for (int l = 0; l < tmp.length(); l++) {
                    pos = tmp.indexOf("*");
                }

                StringBuffer sb = new StringBuffer();
                char[] chars = tmp.toCharArray();
                for (int s = 0; s < pos; s++) {
                    sb.append(chars[s]);
                }

                tmp2 = String.valueOf(sb);

                int y = 0;
                double min = 1000;

                for (int l = 0; l < i; l++) {
                    if ((Double.parseDouble(tmp2) <= ar1_size[l]) && (min >= ar1_size[l])) {
                        min = ar1_size[l];
                        y = l;
                    }
                }
                component_count.set(ar1_id[y], component_count.get(ar1_id[y]) + 1 * counts[k]);

            }
        }

        //квадратная платформа
        i = 0;
        sqlQuewy = "select _id, title "
                + "FROM rgzbn_gm_ceiling_components_option " +
                "where component_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id2)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    //выводим внешний радиус квадратной платформы
                    String tmp = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    String tmp2 = "";
                    int pos = 0;
                    for (int l = 0; l < tmp.length(); l++) {
                        pos = tmp.indexOf("-") + 1;
                    }
                    StringBuffer sb = new StringBuffer();
                    char[] chars = tmp.toCharArray();
                    for (int s = 0; pos < tmp.length(); pos++) {
                        sb.append(chars[pos]);
                    }
                    tmp2 = String.valueOf(sb);
                    ar2_size2[i] = Integer.valueOf(tmp2);

                    for (int l = 0; l < tmp.length(); l++) {
                        pos = tmp.indexOf("-");
                    }
                    sb = new StringBuffer();
                    chars = tmp.toCharArray();
                    for (int s = 0; s < pos; s++) {
                        sb.append(chars[s]);
                    }
                    tmp2 = String.valueOf(sb);

                    ar2_size[i] = Integer.valueOf(tmp2);

                    ar2_id[i] = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));

                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();

        for (int k = 0; k < j; k++) {
            double min = 1000;
            String tmp = String.valueOf(size[k]);
            boolean flag = true;
            if (tmp.length() > 3) {

                String tmp2 = "";
                int pos = 0;
                for (int l = 0; l < tmp.length(); l++) {
                    pos = tmp.indexOf("*");
                }
                StringBuffer sb = new StringBuffer();
                char[] chars = tmp.toCharArray();
                for (int s = 0; s < pos; s++) {
                    sb.append(chars[s]);
                }

                tmp2 = String.valueOf(sb);

                int y = 0;
                for (int l = 0; l < i; l++) {
                    if ((Double.parseDouble(tmp2) < ar2_size2[l]) && (Double.parseDouble(tmp2) > ar2_size[l]) && (min > ar2_size[l])) {
                        min = ar2_size[l];
                        flag = false;
                        y = l;
                    }
                }
                if (!flag) {
                    component_count.set(ar2_id[y], component_count.get(ar2_id[y]) + 1 * counts[k]);
                }
                if (flag) {
                    component_count.set(items_1, component_count.get(items_1) + 1 * counts[k]);
                }
            }
        }

    }

    void fun_builder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("")
                .setCancelable(false)
                .setNegativeButton("Хорошо",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.my_dialog_calcul, null);
        TextView txt = (TextView)dialogView.findViewById(R.id.text_dialog);
        txt.setText(s_setMessage);
        TextView txt1 = (TextView)dialogView.findViewById(R.id.text_dialog1);
        txt1.setText(s_setMessage1);
        if (s_setdrawable.equals("")) {
        }else {
                ImageView image = (ImageView) dialogView.findViewById(R.id.view_dialog);
                image.setImageResource(Integer.parseInt(s_setdrawable));
            }
        if (s_setdrawable1.equals("")) {
        }else {
            ImageView image1 = (ImageView) dialogView.findViewById(R.id.view_dialog2);
            image1.setImageResource(Integer.parseInt(s_setdrawable1));
        }

        builder.setView(dialogView);

        AlertDialog alert = builder.create();
        alert.show();
    }

    void canv_text() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String groupBy1 = null;
        String item_content1 = null;

        s_c.clear();
        Cursor c = null;

        String sqlQuewy = "select t1.texture_title as TITLE, t2.count as COUNT, t1._id "
                + "FROM rgzbn_gm_ceiling_textures as t1 " +
                "INNER JOIN rgzbn_gm_ceiling_canvases as t2 " +
                "ON t1._id = t2.texture_id " +
                "where COUNT > 0 " +
                "group by t1._id";

        c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_c.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_canvases);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_c);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        try {
            sqlQuewy = "select texture_title "
                    + "FROM rgzbn_gm_ceiling_textures " +
                    "where _id=?";

            c = db.rawQuery(sqlQuewy, new String[]{n2});

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    } while (c.moveToNext());
                }
            }
            c.close();

            String nn2 = item_content1; // нужная строка
            adapter1 = (ArrayAdapter) spinner1.getAdapter();
            int position = adapter1.getPosition(nn2);

            spinner1.setSelection(position);
        } catch (Exception e) {

        }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                texture = spinner1.getSelectedItem().toString();
                Log.d("mLog", texture);

                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();

                String sqlQuewy;
                Cursor c;

                sqlQuewy = "select texture_colored "
                        + "FROM rgzbn_gm_ceiling_textures " +
                        "where texture_title=?";

                c = db.rawQuery(sqlQuewy, new String[]{texture});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            String c_id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            if (c_id.equals("1")) {
                                btn_color_canvases_visible = true;
                            } else {
                                btn_color_canvases_visible = false;
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();
                if (btn_color_canvases_visible) {
                    c = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, null, "texture_title = ?",
                            new String[]{texture}, null, null, null);
                    texture_id = "";
                    if (c.moveToFirst()) {
                        colorIndex = c.getColumnIndex(DBHelper.KEY_TEXTURE_COLORED);
                        do {
                            if (c.getInt(colorIndex) == 1) {
                                btn_color_canvases.setVisibility(View.VISIBLE);
                                btn_color_canvases.setEnabled(true);                                    // активность кнопки чертить
                                btn_color_canvases.setBackgroundResource(R.drawable.rounded_button);    // активность кнопки чертить

                                SPSO = getSharedPreferences("color_title", MODE_PRIVATE);
                                String id_color = SPSO.getString("", "");

                                Log.d("mLog", id_color);
                                sqlQuewy = "select hex "
                                        + "FROM rgzbn_gm_ceiling_colors " +
                                        "where title = ? ";
                                c = db.rawQuery(sqlQuewy, new String[]{id_color});
                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        do {
                                            String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                            btn_color_canvases.setBackgroundColor(Color.parseColor("#" + hex));

                                        } while (c.moveToNext());
                                    }
                                }
                                c.close();
                            }
                        } while (c.moveToNext());
                    }
                    c.close();
                } else {
                    btn_color_canvases.setVisibility(View.GONE);
                    SP = getSharedPreferences("color_title", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", "");
                    ed.commit();
                }

                int idIndex = 0;

                c = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, null, "texture_title = ?",
                        new String[]{texture}, null, null, null);

                texture_id = "";

                if (c.moveToFirst()) {
                    idIndex = c.getColumnIndex(DBHelper.KEY_ID);
                    do {
                        texture_id = c.getString(idIndex);
                        break;
                    } while (c.moveToNext());
                }
                c.close();

                String item_content2 = null;
                String item_content3 = null;
                s_t.clear();

                String groupBy2 = "name";
                c = db.query("rgzbn_gm_ceiling_canvases", null, "texture_id = ? ",
                        new String[]{texture_id}, groupBy2, null, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                item_content2 = c.getString(c.getColumnIndex(c.getColumnName(3)));
                                item_content3 = c.getString(c.getColumnIndex(c.getColumnName(4)));
                                item_content2 += " " + item_content3;
                            }

                            s_t.add(item_content2);
                        } while (c.moveToNext());
                    }
                }
                c.close();

                final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_texture);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Activity_calcul.this, android.R.layout.simple_spinner_item, s_t);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                try {
                    sqlQuewy = "select name, country "
                            + "FROM rgzbn_gm_ceiling_canvases " +
                            "where _id=?";

                    c = db.rawQuery(sqlQuewy, new String[]{n3});

                    String item_content1 = "";

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0))) + " " + c.getString(c.getColumnIndex(c.getColumnName(1)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    String nn2 = item_content1; // нужная строка
                    adapter2 = (ArrayAdapter) spinner2.getAdapter();
                    int position = adapter2.getPosition(nn2);

                    spinner2.setSelection(position);

                    if (position == -1) {
                        spinner2.setSelection(0);
                    }
                } catch (Exception e) {
                }

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {

                        canvases = spinner2.getSelectedItem().toString();

                        String sqlQuewy = "select * "
                                + "FROM rgzbn_gm_ceiling_canvases " +
                                "where texture_id=? " +
                                " ORDER BY width DESC";

                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{texture_id});

                        String str = "[";

                        if (c.moveToFirst()) {
                            int nameIndex = c.getColumnIndex(DBHelper.KEY_NAME);
                            int countryIndex = c.getColumnIndex(DBHelper.KEY_COUNTRY);
                            int priceIndex = c.getColumnIndex(DBHelper.KEY_PRICE);
                            int widthIndex = c.getColumnIndex(DBHelper.KEY_WIDTH);
                            do {
                                String nc = c.getString(nameIndex) + " " + c.getString(countryIndex);

                                if (nc.equals(canvases)) {

                                    float width = Float.valueOf(c.getString(widthIndex)) * 100;
                                    int price = Integer.valueOf(c.getString(priceIndex));
                                    str += "{\"width\":" + (Math.round(width) + "," + "\"price\":" + price + "},");

                                }
                            } while (c.moveToNext());
                        } else
                            Log.d("mLog", "0 rows");

                        String strr = "";

                        for (int i = 0; i < str.length() - 1; i++)
                            strr += str.charAt(i);

                        strr += " ]";

                        Log.d("mLog canvases", strr);

                        c.close();

                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(SAVED_TEXT, String.valueOf(strr)); // передача выбранных полотен на чертилку
                        ed.commit();

                        SP = getSharedPreferences("textures_draft", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", texture_id);
                        ed.commit();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}