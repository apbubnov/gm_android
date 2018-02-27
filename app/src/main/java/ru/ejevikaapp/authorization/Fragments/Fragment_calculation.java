package ru.ejevikaapp.authorization.Fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.pixplicity.sharp.Sharp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Activity_add_diffuzor;
import ru.ejevikaapp.authorization.Activity_color;
import ru.ejevikaapp.authorization.Activity_draft;
import ru.ejevikaapp.authorization.Activity_zamer;
import ru.ejevikaapp.authorization.Class.Diffuzor_class;
import ru.ejevikaapp.authorization.Class.Extra_class;
import ru.ejevikaapp.authorization.Class.Kupit_Svetlin_class;
import ru.ejevikaapp.authorization.Class.Kupit_cornice;
import ru.ejevikaapp.authorization.Class.Profile_class;
import ru.ejevikaapp.authorization.Class.Select_work;
import ru.ejevikaapp.authorization.Class.Svetiln_class;
import ru.ejevikaapp.authorization.Class.Truby_class;
import ru.ejevikaapp.authorization.Class.Ventil_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_calculation extends Fragment implements View.OnClickListener{

    Button chertezh, btn_texture, btn_width, btn_vstavka, btn_light, btn_lustr, btn_add_svetiln,
            btn_add_kupit_svetiln, btn_karniz, btn_rb_v;

    Button btn_add_truby, btn_fire, btn_add_vent, btn_add_diff, btn_color_canvases, btn_save, btn_add_cornice,
            btn_add_other_work, btn_calculate, btn_add_profile,
            btn_wall, btn_fasteners, btn_in_cut, btn_diff_acc, btn_separator, btn_soaring_ceiling, btn_mount_wall, btn_mount_granite, btn_cabling, btn_bond_beam,
            btn_in_cut_shop, btn_drain_the_water, btn_add_other_comp;

    TextView area, perimetr, corners, text_calculate;

    ArrayAdapter<String> adapter;

    public int colorIndex = 0;

    DBHelper dbHelper;

    ArrayList s_c = new ArrayList();
    ArrayList s_t = new ArrayList();

    String texture, canvases, texture_id, s_setMessage, s_setMessage1, s_setdrawable, s_setdrawable1, square_obr, s_sp5 = "", s_spa = "", id_project, id_calculation;
    String width_final = "", imag_cut = "", imag = "", rb_vstavka = "0", n2, n3, lines_length, user_id = "", dealer_id_str = "", final_comp = "", final_mount = "",
            rb_baget = "", original_sketch = "", rb_h ="0";

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
            count_vent, count_electr, count_diffus, count_pipes, user_id_int, id_n3;

    Double S = 0.0, P = 0.0, Angle = 0.0;

    ImageView image;

    CheckBox RB_karniz;

    EditText lustr, ed_fire, karniz, name_project, ed_wall, ed_cabling, ed_separator, ed_fasteners, ed_in_cut, mount_wall,
            mount_granite, ed_diff_acc, ed_discount, bond_beam, soaring_ceiling, ed_in_cut_shop, ed_drain_the_water,
            count_comp, price_comp;

    boolean rb_k = false, calculat = false, btn_color_canvases_visible = false, mounting = true, delete_comp = true;

    ArrayList<Double> component_count = new ArrayList();    // component_option
    ArrayList canvases_data = new ArrayList();
    ArrayList mounting_data = new ArrayList();
    ArrayList<Integer> results = new ArrayList();
    ArrayList component_item = new ArrayList();

    int items_9, items_5, items_11, items_vstavka_bel, items_vstavka, items_10, items_16, items_556, items_4, items_58, items_3,
            items_2, items_1, items_8, items_6, items_14, items_430, items_35, items_360, int_rb_k, items_236,
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

    RadioButton rb_v_white, rb_v_color, rb_v_no, rb_m_yes, rb_m_no, rb_b_no, rb_b_potol, rb_b_all, rb_h_no, rb_h_yes;

    // fixtures
    Button btn_add_svetilnik;
    ListView list_svetilnik;

    View view;

    ScrollView scroll_calc;

    boolean bool_resume = false;

    public Fragment_calculation() {
    }

    public static Fragment_calculation newInstance() {
        return new Fragment_calculation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calculation, container, false);

        SharedPreferences SP = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP.getString("", "");
        user_id_int = Integer.parseInt(user_id) * 1000000;

        SP = getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id_str = SP.getString("", "");

        scroll_calc = (ScrollView) view.findViewById(R.id.scroll_calc);

        chertezh = (Button) view.findViewById(R.id.chertezh);
        btn_texture = (Button) view.findViewById(R.id.btn_texture);
        btn_width = (Button) view.findViewById(R.id.btn_width);
        btn_vstavka = (Button) view.findViewById(R.id.btn_vstavka);
        btn_light = (Button) view.findViewById(R.id.btn_light);
        btn_lustr = (Button) view.findViewById(R.id.btn_lustr);
        btn_add_svetiln = (Button) view.findViewById(R.id.btn_add_svetiln);
        btn_add_kupit_svetiln = (Button) view.findViewById(R.id.btn_add_kupit_svetiln);
        btn_karniz = (Button) view.findViewById(R.id.btn_karniz);
        btn_add_truby = (Button) view.findViewById(R.id.btn_add_truby);
        btn_fire = (Button) view.findViewById(R.id.btn_fire);
        btn_add_vent = (Button) view.findViewById(R.id.btn_add_vent);
        btn_add_diff = (Button) view.findViewById(R.id.btn_add_diff);
        btn_color_canvases = (Button) view.findViewById(R.id.color_canvases);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_add_cornice = (Button) view.findViewById(R.id.btn_add_cornice);
        //btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_rb_v = (Button) view.findViewById(R.id.btn_rb_v);
        btn_calculate = (Button) view.findViewById(R.id.btn_calculate);
        btn_add_profile = (Button) view.findViewById(R.id.btn_add_profile);
        btn_wall = (Button) view.findViewById(R.id.btn_wall);
        btn_fasteners = (Button) view.findViewById(R.id.btn_fasteners);
        btn_in_cut = (Button) view.findViewById(R.id.btn_in_cut);
        btn_diff_acc = (Button) view.findViewById(R.id.btn_diff_acc);
        btn_separator = (Button) view.findViewById(R.id.btn_separator);
        btn_soaring_ceiling = (Button) view.findViewById(R.id.btn_soaring_ceiling);
        btn_mount_wall = (Button) view.findViewById(R.id.btn_mount_wall);
        btn_mount_granite = (Button) view.findViewById(R.id.btn_mount_granite);
        btn_cabling = (Button) view.findViewById(R.id.btn_cabling);
        btn_bond_beam = (Button) view.findViewById(R.id.btn_bond_beam);
        btn_in_cut_shop = (Button) view.findViewById(R.id.btn_in_cut_shop);
        btn_drain_the_water = (Button) view.findViewById(R.id.btn_drain_the_water);

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
        btn_in_cut_shop.setOnClickListener(this);
        btn_drain_the_water.setOnClickListener(this);

        lustr = (EditText) view.findViewById(R.id.lustr);
        karniz = (EditText) view.findViewById(R.id.karniz);
        ed_fire = (EditText) view.findViewById(R.id.ed_fire);
        name_project = (EditText) view.findViewById(R.id.name_project);
        ed_wall = (EditText) view.findViewById(R.id.ed_wall);
        ed_cabling = (EditText) view.findViewById(R.id.ed_cabling);
        ed_separator = (EditText) view.findViewById(R.id.ed_separator);
        ed_fasteners = (EditText) view.findViewById(R.id.ed_fasteners);
        ed_in_cut = (EditText) view.findViewById(R.id.ed_in_cut);
        mount_wall = (EditText) view.findViewById(R.id.mount_wall);
        mount_granite = (EditText) view.findViewById(R.id.mount_granite);
        ed_diff_acc = (EditText) view.findViewById(R.id.ed_diff_acc);
        ed_discount = (EditText) view.findViewById(R.id.ed_discount);
        bond_beam = (EditText) view.findViewById(R.id.bond_beam);
        soaring_ceiling = (EditText) view.findViewById(R.id.soaring_ceiling);
        ed_in_cut_shop = (EditText) view.findViewById(R.id.ed_in_cut_shop);
        ed_drain_the_water = (EditText) view.findViewById(R.id.ed_drain_the_water);

        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        String dealer_calc = SP.getString("", "");

        if (dealer_calc.equals("true")){
            btn_save.setVisibility(View.GONE);
            name_project.setVisibility(View.GONE);
        }

        text_calculate = (TextView) view.findViewById(R.id.text_calculate);

        RB_karniz = (CheckBox) view.findViewById(R.id.RB_karniz);

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

        // вставка
        rb_v_white = (RadioButton) view.findViewById(R.id.rb_v_white);
        rb_v_color = (RadioButton) view.findViewById(R.id.rb_v_color);
        rb_v_no = (RadioButton) view.findViewById(R.id.rb_v_no);

        rb_v_no.setChecked(true);
        rb_vstavka = "0";

        RadioGroup radGrp = (RadioGroup) view.findViewById(R.id.radios_v);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_v_white:
                        btn_rb_v.setEnabled(false);
                        btn_rb_v.setBackgroundColor(Color.WHITE);
                        rb_vstavka = "1";
                        SharedPreferences SP = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
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
                        SP = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", "0");
                        ed.commit();
                        break;
                }
            }
        });

        // высота
        rb_h_no = (RadioButton) view.findViewById(R.id.rb_h_no);
        rb_h_yes = (RadioButton) view.findViewById(R.id.rb_h_yes);

        rb_h_no.setChecked(true);
        rb_h = "0";

        RadioGroup radios_height = (RadioGroup) view.findViewById(R.id.radios_height);
        radios_height.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_h_no:
                        rb_h = "0";
                        break;
                    case R.id.rb_h_yes:
                        rb_h = "1";
                        break;
                }
            }
        });

        extra_comp("{}");
        extra_mount("{}");

        rb_m_yes = (RadioButton)view.findViewById(R.id.rb_m_yes);
        rb_m_no = (RadioButton) view.findViewById(R.id.rb_m_no);

        rb_m_yes.setChecked(true);

        mounting = true;
        RadioGroup radios_mounting = (RadioGroup) view.findViewById(R.id.radios_mounting);
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

        rb_b_no = (RadioButton) view.findViewById(R.id.rb_b_no);
        rb_b_potol = (RadioButton) view.findViewById(R.id.rb_b_potol);
        rb_b_all = (RadioButton) view.findViewById(R.id.rb_b_all);

        rb_baget = "0";

        RadioGroup radios_b = (RadioGroup) view.findViewById(R.id.radios_b);
        radios_b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_b_no:
                        rb_baget = "0";
                        break;

                    case R.id.rb_b_potol:
                        rb_baget = "1";
                        break;

                    case R.id.rb_b_all:
                        rb_baget = "2";
                        break;
                }
            }
        });

        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SP4 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SP5 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SP9 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SPI = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SPSO = PreferenceManager.getDefaultSharedPreferences(getActivity());

        image = (ImageView) view.findViewById(R.id.id_image);

        area = (TextView) view.findViewById(R.id.area);
        perimetr = (TextView) view.findViewById(R.id.perimetr);
        corners = (TextView) view.findViewById(R.id.corners);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SP = getActivity().getSharedPreferences("id_calc", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", String.valueOf(1));
        ed.commit();

        id_project = getActivity().getIntent().getStringExtra("id_project"); // project
        id_calculation = getActivity().getIntent().getStringExtra("id_calculation");  // id_calculation

        Log.d("mLog id_project + calc", id_project + " " + id_calculation);

        if (id_calculation != null && id_project != null) {
            dbHelper = new DBHelper(getActivity());
            db = dbHelper.getReadableDatabase();
            String sqlQuewy = "SELECT calculation_title, n1, n2, n3, n4, n5, n6, n7, n8, n9," +
                    " n10, n11, n12, n16, n17, n18, n19, n20, n21, n24," +
                    " n25, dop_krepezh, calc_image, n27, color, offcut_square, discount, n28, n30, original_sketch, n31, n32, height, " +
                    " extra_components, extra_mounting "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE _id = ?";

            Cursor k = db.rawQuery(sqlQuewy, new String[]{id_calculation});

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
                        String n31 = k.getString(k.getColumnIndex(k.getColumnName(30)));
                        String n32 = k.getString(k.getColumnIndex(k.getColumnName(31)));
                        String height = k.getString(k.getColumnIndex(k.getColumnName(32)));
                        String extra_components = k.getString(k.getColumnIndex(k.getColumnName(33)));
                        String extra_mounting = k.getString(k.getColumnIndex(k.getColumnName(34)));

                        extra_comp(extra_components);
                        extra_mount(extra_mounting);

                        name_project.setText(calculation_title);
                        area.setText(" S = " + n4 + " м2");
                        S = Double.valueOf(n4);
                        perimetr.setText(" P = " + n5 + " м");
                        P = Double.valueOf(n5);

                        SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        ed = SPSO.edit();
                        ed.putString("", n6);
                        ed.commit();

                        mount_wall.setText(n7);
                        mount_granite.setText(n8);
                        corners.setText(" Количество углов =   " + n9);
                        Angle = Double.valueOf(n9);
                        ed_in_cut.setText(n11);
                        ed_in_cut_shop.setText(n31);
                        ed_drain_the_water.setText(n32);
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

                        try {
                            Sharp.loadString(imag).into(image);
                        }catch (Exception e){
                        }

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

                                    SP = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
                                    ed = SP.edit();
                                    ed.putString("", color);
                                    ed.commit();

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        if (height.equals("0")){
                            rb_h_no.setChecked(true);
                        } else if (height.equals("1")){
                            rb_h_yes.setChecked(true);
                        }

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
                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            max_id_contac++;
                            id_calculation = String.valueOf(max_id_contac);
                        } while (c.moveToNext());
                    }
                }
            } catch (Exception e) {
                max_id_contac = user_id_int + 1;
                id_calculation = String.valueOf(max_id_contac);
            }
        }

        fixtures();
        ecola();
        cornice();
        pipes();
        profile();
        hoods();
        diffuzor();

        Log.d("mLog", "id_calc = " + id_calculation);

        canv_text();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bool_resume) {
            Log.d("mLog", "RESUME");
            Log.d("mLog", "id_calc = " + id_calculation);

            SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
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

            SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
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

            SPSO = getActivity().getSharedPreferences("SAVED_SO", MODE_PRIVATE);
            if (SPSO.getString("", "").equals("")) {
                square_obr = "0";
            } else {
                square_obr = SPSO.getString(SAVED_SO, "");
            }

            SP4 = getActivity().getSharedPreferences("SAVED_N4", MODE_PRIVATE);
            if (SP4.getString(SAVED_N4, "").length() == 0) {
                area.setText(" S =  м2");
            } else {
                area.setText(" S =   " + SP4.getString(SAVED_N4, "") + " м2");
                S = Double.valueOf(SP4.getString(SAVED_N4, ""));
            }

            SP5 = getActivity().getSharedPreferences("SAVED_N5", MODE_PRIVATE);
            if (SP5.getString(SAVED_N5, "").length() == 0) {
                perimetr.setText(" P =  м");
            } else {
                perimetr.setText(" Р =   " + SP5.getString(SAVED_N5, "") + " м");
                P = Double.valueOf(SP5.getString(SAVED_N5, ""));
            }

            SP9 = getActivity().getSharedPreferences("SAVED_N9", MODE_PRIVATE);
            if (SP9.getString(SAVED_N9, "").length() == 0) {
                corners.setText(" Количество углов =   ");
            } else {
                corners.setText(" Количество углов =   " + SP9.getString(SAVED_N9, ""));
                Angle = Double.valueOf(SP9.getString(SAVED_N9, ""));
            }

            SPW = getActivity().getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
            width_final = SPW.getString(SAVED_W, "");

            SPW = getActivity().getSharedPreferences("SAVED_LL", MODE_PRIVATE);
            lines_length = SPW.getString(SAVED_W, "");

            SPI = getActivity().getSharedPreferences("SAVED_I", MODE_PRIVATE);
            imag = SPI.getString(SAVED_I, "");

            SPI = getActivity().getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
            imag_cut = SPI.getString("", "");

            SPI = getActivity().getSharedPreferences("SAVED_KP", MODE_PRIVATE);
            SAVED_KP = SPI.getString("", "");
            Log.d("mLog", "saved " + SAVED_KP);

            SPI = getActivity().getSharedPreferences("SAVED_WP", MODE_PRIVATE);
            SAVED_WP = SPI.getString("", "");
            Log.d("mLog", SAVED_WP);

            SPI = getActivity().getSharedPreferences("SAVED_DP", MODE_PRIVATE);
            SAVED_DP = SPI.getString("", "");
            Log.d("mLog", SAVED_DP);

            SPI = getActivity().getSharedPreferences("SAVED_PT_P", MODE_PRIVATE);
            SAVED_PT_P = SPI.getString("", "");
            Log.d("mLog", SAVED_PT_P);

            SPI = getActivity().getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
            SAVED_CODE = SPI.getString("", "");
            Log.d("mLog", SAVED_CODE);

            SPI = getActivity().getSharedPreferences("end_draft", MODE_PRIVATE);
            String end_draft = SPI.getString("", "");

            SPI = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
            String draft_auto = SPI.getString("", "");

            if (end_draft.equals("1") && draft_auto.equals("1")) {
                calculat = false;
                calculation();
            }

            SPI = getActivity().getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
            SAVED_ALFAVIT = SPI.getString("", "");
            Log.d("mLog", SAVED_ALFAVIT);

            try {
                Sharp.loadString(imag).into(image);
            }catch (Exception e){
            }

        } else bool_resume = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("mLog", "destroy");

        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        String dealer_calc = SP.getString("", "");

        if (id_calculation == null || dealer_calc.equals("true") || id_project == null || delete_comp) {

            dbHelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_OTHER_WORK, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_OTHER_WORK, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_OTHER_COMP, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_OTHER_COMP, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();

            cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, "calculation_id = ?", new String[]{String.valueOf(id_calculation)});
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }

        SP = getActivity().getSharedPreferences("SAVED_SO", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", "");
        ed.commit();


        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
        ed = SPSO.edit();
        ed.putString("", "");
        ed.commit();

        SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
        ed = SPSO.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_N4", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_N5", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_N9", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_I", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_LL", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_KP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_WP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_DP", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_PT_P", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("draft_diags_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("draft_walls_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("draft_pt_points", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();

        SP = getActivity().getSharedPreferences("end_draft", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();
    }

    void extra_comp(String extra_components) {

        final ArrayList<Extra_class> extra_mas = new ArrayList<>();
        final int[] i = {0};

        Log.d("extra_components", String.valueOf(extra_components));
        if (extra_components.equals("{}")){

        } else {
            try {
                org.json.JSONObject dat = new org.json.JSONObject(extra_components);
                do {
                    try {
                        JSONObject id_array = dat.getJSONObject(String.valueOf(i[0]));
                        String title = id_array.getString("title");
                        String value = id_array.getString("value");

                        Log.d("extra_comp", String.valueOf(i[0]) + " " + title + " " + value);
                        Extra_class fix_class = new Extra_class(String.valueOf(i[0]), title, value);
                        extra_mas.add(fix_class);

                    } catch (Exception e) {
                        Log.d("extra_comp", String.valueOf(e));
                    }

                    i[0]++;
                } while (dat.length() != i[0]);
            } catch (Exception e) {
                Log.d("extra_comp", String.valueOf(e));
            }
        }

        BindDictionary<Extra_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_count, new StringExtractor<Extra_class>() {
            @Override
            public String getStringValue(Extra_class nc, int position) {
                return nc.getTitle();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Extra_class>() {
            @Override
            public String getStringValue(Extra_class nc, int position) {
                return nc.getValue();
            }
        });

        FunDapter Fun_adapter = new FunDapter(getActivity(), extra_mas, R.layout.list_2column, dict);

        ListView list_comp = (ListView) view.findViewById(R.id.list_comp);
        list_comp.setAdapter(Fun_adapter);
        setListViewHeightBasedOnChildren(list_comp);

        final int[] j = {0};
        final String final_extra_comp = extra_components;
        list_comp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Extra_class selectedid = extra_mas.get(position);
                final int s_id = Integer.valueOf(selectedid.getId());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        org.json.simple.JSONObject json_extra = new org.json.simple.JSONObject();
                                        String extra = "{";

                                        int key=0;
                                        Log.d("extra_comp1", String.valueOf(s_id));

                                        try {
                                            JSONObject dat = new JSONObject(final_extra_comp);
                                            do {
                                                if (s_id == j[0]){
                                                } else {
                                                    JSONObject id_array = dat.getJSONObject(String.valueOf(j[0]));
                                                    String title = id_array.getString("title");
                                                    String value = id_array.getString("value");

                                                    extra += "\""+key+"\":{\"title\":\""+title+"\",\"value\":\""+value+"\"},";

                                                    key++;
                                                    Log.d("extra_comp2", String.valueOf(extra));
                                                }
                                                j[0]++;
                                            } while (dat.length() != j[0]);
                                            extra = extra.substring(0,extra.length()-1)+"}";

                                        } catch (Exception e) {
                                        }

                                        Log.d("extra_comp3", String.valueOf(extra));
                                        extra_comp(extra);

                                        Toast toast = Toast.makeText(getActivity(),
                                                "Удалён ", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button btn_add_comp = (Button) view.findViewById(R.id.btn_add_comp);
        final EditText count_comp = (EditText) view.findViewById(R.id.count_comp);
        final EditText price_comp = (EditText) view.findViewById(R.id.price_comp);

        // создаем обработчик нажатия
        final int finalI = i[0];
        final String[] finalExtra_comp = {extra_components};
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_comp.getText().toString().trim();
                String price = price_comp.getText().toString().trim();

                if (kol_vo.equals("") && price.equals("")) {

                    toast = Toast.makeText(getActivity(),
                            "Введите что-то", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    org.json.simple.JSONObject json_extra = new org.json.simple.JSONObject();
                    String add_extra;

                    json_extra.put("value", price);
                    json_extra.put("title", kol_vo);
                    add_extra = String.valueOf(json_extra);

                    if (finalI == 0){
                        finalExtra_comp[0] = finalExtra_comp[0].substring(0, finalExtra_comp[0].length() - 1)
                                + "\"" + finalI + "\":" + json_extra + "}";
                    } else {
                        finalExtra_comp[0] = finalExtra_comp[0].substring(0, finalExtra_comp[0].length() - 1)
                                + ",\"" + finalI + "\":" + json_extra + "}";
                    }

                    Log.d("extra_comp", String.valueOf(finalExtra_comp[0]));

                    extra_comp(finalExtra_comp[0]);
                    count_comp.setText("");
                    price_comp.setText("");

                }
            }
        };

        btn_add_comp.setOnClickListener(on_click);

        if (extra_components.equals("null")){
            extra_components = "{}";
        }
        final_comp = extra_components;
    }

    void extra_mount(String extra_mounting) {

        final ArrayList<Extra_class> extra_mas = new ArrayList<>();
        final int[] i = {0};

        if (extra_mounting.equals("{}")){

        } else {
            try {
                org.json.JSONObject dat = new org.json.JSONObject(extra_mounting);
                Log.d("extra_mount", String.valueOf(dat));
                do {
                    JSONObject id_array = dat.getJSONObject(String.valueOf(i[0]));
                    String title = id_array.getString("title");
                    String value = id_array.getString("value");

                    Extra_class fix_class = new Extra_class(String.valueOf(i[0]), title, value);
                    extra_mas.add(fix_class);

                    i[0]++;
                } while (dat.length() != i[0]);
            } catch (Exception e) {
                Log.d("extra_mount", String.valueOf(e));
            }
        }

        BindDictionary<Extra_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.tv_count, new StringExtractor<Extra_class>() {
            @Override
            public String getStringValue(Extra_class nc, int position) {
                return nc.getTitle();
            }
        });
        dict.addStringField(R.id.tv_diam, new StringExtractor<Extra_class>() {
            @Override
            public String getStringValue(Extra_class nc, int position) {
                return nc.getValue();
            }
        });

        FunDapter Fun_adapter = new FunDapter(getActivity(), extra_mas, R.layout.list_2column, dict);

        ListView list_mount = (ListView) view.findViewById(R.id.list_mount);
        list_mount.setAdapter(Fun_adapter);
        setListViewHeightBasedOnChildren(list_mount);

        final int[] j = {0};
        final String final_extra_comp = extra_mounting;
        list_mount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Extra_class selectedid = extra_mas.get(position);
                final int s_id = Integer.valueOf(selectedid.getId());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Удалить выбранный элемент?")
                        .setMessage(null)
                        .setIcon(null)
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        org.json.simple.JSONObject json_extra = new org.json.simple.JSONObject();
                                        String extra = "{";

                                        int key=0;
                                        Log.d("extra_mount1", String.valueOf(s_id));

                                        try {
                                            JSONObject dat = new JSONObject(final_extra_comp);
                                            do {
                                                if (s_id == j[0]){
                                                } else {
                                                    JSONObject id_array = dat.getJSONObject(String.valueOf(j[0]));
                                                    String title = id_array.getString("title");
                                                    String value = id_array.getString("value");

                                                    extra += "\""+key+"\":{\"title\":\""+title+"\",\"value\":\""+value+"\"},";

                                                    key++;
                                                    Log.d("extra_mount2", String.valueOf(extra));
                                                }
                                                j[0]++;
                                            } while (dat.length() != j[0]);
                                            extra = extra.substring(0,extra.length()-1)+"}";

                                        } catch (Exception e) {
                                        }

                                        Log.d("extra_mount3", String.valueOf(extra));
                                        extra_mount(extra);

                                        Toast toast = Toast.makeText(getActivity(),
                                                "Удалён ", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button btn_add_mount = (Button) view.findViewById(R.id.btn_add_mount);
        final EditText count_mount = (EditText) view.findViewById(R.id.count_mount);
        final EditText price_mount = (EditText) view.findViewById(R.id.price_mount);

        // создаем обработчик нажатия
        final int finalI = i[0];
        final String[] finalExtra_comp = {extra_mounting};
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_mount.getText().toString().trim();
                String price = price_mount.getText().toString().trim();

                if (kol_vo.equals("") && price.equals("")) {

                    toast = Toast.makeText(getActivity(),
                            "Введите что-то", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    org.json.simple.JSONObject json_extra = new org.json.simple.JSONObject();
                    String add_extra;

                    json_extra.put("value", price);
                    json_extra.put("title", kol_vo);
                    add_extra = String.valueOf(json_extra);

                    if (finalI == 0){
                        finalExtra_comp[0] =finalExtra_comp[0].substring(0, finalExtra_comp[0].length() - 1)
                                + "\"" + finalI + "\":" + json_extra + "}";
                    } else {
                        finalExtra_comp[0] = finalExtra_comp[0].substring(0, finalExtra_comp[0].length() - 1)
                                + ",\"" + finalI + "\":" + json_extra + "}";
                    }

                    Log.d("extra_mount", String.valueOf(finalExtra_comp[0]));

                    extra_mount(finalExtra_comp[0]);
                    count_mount.setText("");
                    price_mount.setText("");

                }
            }
        };

        btn_add_mount.setOnClickListener(on_click);

        if (extra_mounting.equals("null")) {
            extra_mounting = "{}";
        }
        final_mount = extra_mounting;
    }

    void fixtures(){

        final DBHelper[] dbHelper = new DBHelper[1];
        final EditText count_fixtures;

        final String[] select_vid = new String[1];
        final String[] select_diam = new String[1];
        final String[] c_id = { "" };

        final Integer[] type_id = new Integer[1];
        final Integer[] comp_opt = new Integer[1];

        ArrayList s_v = new ArrayList();
        final ArrayList s_d = new ArrayList();

        final ArrayList<Svetiln_class> svet_mas = new ArrayList<>();

        Log.d("fixtures", "fix");
        dbHelper[0] = new DBHelper(getActivity());

        String item_content1 = null;

        final SQLiteDatabase db = dbHelper[0].getWritableDatabase();

        s_v.clear();

        final Cursor[] fix_c  = {db.query("rgzbn_gm_ceiling_type", null, "_id between 2 and 3",
                null, null, null, null)};
        if (fix_c[0] != null) {
            if (fix_c[0].moveToFirst()) {
                do {
                    for (String cn : fix_c[0].getColumnNames()) {
                        item_content1 = fix_c[0].getString(fix_c[0].getColumnIndex(fix_c[0].getColumnName(2)));
                    }
                    s_v.add(item_content1);
                } while (fix_c[0].moveToNext());
            }
        }
        fix_c[0].close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_vid_fixtures);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_v);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_vid[0] = spinner1.getSelectedItem().toString();

                switch (spinner1.getSelectedItemPosition()){
                    case (0): c_id[0] = " 21";
                        type_id[0] = 2;
                        break;
                    case (1): c_id[0] = " 12";
                        type_id[0] = 3;
                        break;
                }

                s_d.clear();
                String item_content2 = null;
                SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                String sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE component_id = ?";

                fix_c[0] = db.rawQuery(sqlQuewy, new String[]{c_id[0]});

                if (fix_c[0] != null) {
                    if (fix_c[0].moveToFirst()) {
                        do {
                            for (String cn : fix_c[0].getColumnNames()) {
                                item_content2 = fix_c[0].getString(fix_c[0].getColumnIndex(fix_c[0].getColumnName(0)));
                            }

                            s_d.add(item_content2);
                        } while (fix_c[0].moveToNext());
                    }
                }
                fix_c[0].close();

                final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_diametr_fixtures);
                SpinnerAdapter adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_d);
                ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {

                        select_diam[0] = spinner2.getSelectedItem().toString();

                        SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                        String sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_components_option" +
                                " WHERE component_id = ? and title = ?";

                        fix_c[0] = db.rawQuery(sqlQuewy, new String[]{c_id[0], select_diam[0]});

                        if (fix_c[0] != null) {
                            if (fix_c[0].moveToFirst()) {
                                do {
                                    comp_opt[0] = Integer.valueOf(fix_c[0].getString(fix_c[0].getColumnIndex(fix_c[0].getColumnName(0))));

                                } while (fix_c[0].moveToNext());
                            }
                        }
                        fix_c[0].close();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_fixtures = (EditText)view.findViewById(R.id.count_fixtures);

        if (id_calculation == null) {

        } else {

            String sqlQuewy = "SELECT * "
                    + "FROM rgzbn_gm_ceiling_fixtures" +
                    " WHERE calculation_id = ?";
            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{id_calculation});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int kdIndex = cursor.getColumnIndex(cursor.getColumnName(0));
                    int kidIndex = cursor.getColumnIndex(cursor.getColumnName(1));
                    int kol_voIndex = cursor.getColumnIndex(cursor.getColumnName(2));
                    int vidIndex = cursor.getColumnIndex(cursor.getColumnName(3));
                    int diametrIndex = cursor.getColumnIndex(cursor.getColumnName(4));

                    do {

                        Log.d("fixtures", cursor.getString(kdIndex));

                        String vid_c = cursor.getString(vidIndex);
                        String diam_c = cursor.getString(diametrIndex);
                        String vid = "";
                        String diam = "";

                        sqlQuewy = "SELECT * "
                                + "FROM rgzbn_gm_ceiling_type" +
                                " WHERE _id = ?";

                        fix_c[0] = db.rawQuery(sqlQuewy, new String[]{vid_c});

                        if (fix_c[0] != null) {
                            if (fix_c[0].moveToFirst()) {
                                do {
                                    vid = fix_c[0].getString(fix_c[0].getColumnIndex(fix_c[0].getColumnName(2)));

                                } while (fix_c[0].moveToNext());
                            }
                        }
                        fix_c[0].close();

                        sqlQuewy = "SELECT title "
                                + "FROM rgzbn_gm_ceiling_components_option" +
                                " WHERE _id = ?";

                        fix_c[0] = db.rawQuery(sqlQuewy, new String[]{diam_c});

                        if (fix_c[0] != null) {
                            if (fix_c[0].moveToFirst()) {
                                do {
                                    diam = fix_c[0].getString(fix_c[0].getColumnIndex(fix_c[0].getColumnName(0)));

                                } while (fix_c[0].moveToNext());
                            }
                        }
                        fix_c[0].close();

                        Svetiln_class fix_class = new Svetiln_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                                cursor.getString(kol_voIndex), vid, diam);
                        svet_mas.add(fix_class);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

            BindDictionary<Svetiln_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Svetiln_class>() {
                @Override
                public String getStringValue(Svetiln_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_vid, new StringExtractor<Svetiln_class>() {
                @Override
                public String getStringValue(Svetiln_class nc, int position) {
                    return nc.getVid();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Svetiln_class>() {
                @Override
                public String getStringValue(Svetiln_class nc, int position) {
                    return nc.getDiametr();
                }
            });

            FunDapter Fun_adapter = new FunDapter(getActivity(), svet_mas, R.layout.svet_list, dict);

            list_svetilnik = (ListView) view.findViewById(R.id.list_svetilnik);
            list_svetilnik.setAdapter(Fun_adapter);
            setListViewHeightBasedOnChildren(list_svetilnik);

            list_svetilnik.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {        // УДАЛЕНИЕ

                    Svetiln_class selectedid = svet_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            SQLiteDatabase db = dbHelper[0].getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_fixtures"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }
                                            cursor.close();
                                            fixtures();
                                            count_fixtures.setText("0");

                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                                    "Светильник удалён ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        btn_add_svetilnik = (Button) view.findViewById(R.id.btn_add_svetilnik);

        // создаем обработчик нажатия
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast;
                String kol_vo = count_fixtures.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    ContentValues values = new ContentValues();
                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_fixtures " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N13_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N13_TYPE, type_id[0]);
                    values.put(DBHelper.KEY_N13_SIZE, comp_opt[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_fixtures");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Светильник добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    count_fixtures.setText("");

                    fixtures();

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество светильников", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_svetilnik.setOnClickListener(on_click);

    }

    void ecola() {

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = "";

        final String[] select_color = new String[1];
        final String[] select_lampa = new String[1];
        String id_calc = null;

        final ArrayList<Kupit_Svetlin_class> svet_mas = new ArrayList<>();

        ArrayList s_c = new ArrayList();
        ArrayList s_l = new ArrayList();

        final Integer[] illumin = new Integer[1];
        final Integer[] lamp = new Integer[1];

        s_c.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 19' and count > ' 0'";

        final Cursor[] c = {db.rawQuery(sqlQuewy, new String[]{})};

        if (c[0] != null) {
            if (c[0].moveToFirst()) {
                do {
                    for (String cn : c[0].getColumnNames()) {
                        item_content1 = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));
                    }
                    s_c.add(item_content1);
                } while (c[0].moveToNext());
            }
        }
        c[0].close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_vid_ecola);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_c);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_color[0] = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c[0] = db.rawQuery(sqlQuewy, new String[]{select_color[0]});

                if (c[0] != null) {
                    if (c[0].moveToFirst()) {
                        do {
                            for (String cn : c[0].getColumnNames()) {
                                illumin[0] = Integer.valueOf(c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0))));
                            }
                        } while (c[0].moveToNext());
                    }
                }
                c[0].close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s_l.clear();
        String item_content2 = null;
        db = dbHelper.getWritableDatabase();

        sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 20' and count > ' 0'";

        c[0] = db.rawQuery(sqlQuewy, new String[]{});

        if (c[0] != null) {
            if (c[0].moveToFirst()) {
                do {
                    for (String cn : c[0].getColumnNames()) {
                        item_content2 = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));
                        Log.d("title",item_content2);
                    }

                    s_l.add(item_content2);
                } while (c[0].moveToNext());
            }
        }
        c[0].close();

        final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_diametr_ecola);
        SpinnerAdapter adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_l);
        ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_lampa[0] = spinner2.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c[0] = db.rawQuery(sqlQuewy, new String[]{select_lampa[0]});

                if (c[0] != null) {
                    if (c[0].moveToFirst()) {
                        do {
                            for (String cn : c[0].getColumnNames()) {
                                lamp[0] = Integer.valueOf(c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0))));
                            }
                        } while (c[0].moveToNext());
                    }
                }
                c[0].close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final EditText count_ecola = (EditText)view.findViewById(R.id.count_ecola);

        if (id_calculation == null){
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, "calculation_id =?", new String[]{id_calculation},
                    null, null, null);

            if (cursor.moveToFirst()) {

                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N26_COUNT);
                int colorIndex = cursor.getColumnIndex(DBHelper.KEY_N26_ILLUMINATOR);
                int lampaIndex = cursor.getColumnIndex(DBHelper.KEY_N26_LAMP);

                do {
                    String illum_c = cursor.getString(colorIndex);
                    String lamp_c = cursor.getString(lampaIndex);
                    String illum = "";
                    String lamp_ecola = "";

                    Log.d("diam = ", String.valueOf(colorIndex) + " " + cursor.getString(lampaIndex));

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{illum_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                illum = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{lamp_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                lamp_ecola = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    Kupit_Svetlin_class c_ecola = new Kupit_Svetlin_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), illum, lamp_ecola);
                    svet_mas.add(c_ecola);

                } while (cursor.moveToNext());
            }
            cursor.close();

            BindDictionary<Kupit_Svetlin_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Kupit_Svetlin_class>() {
                @Override
                public String getStringValue(Kupit_Svetlin_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_vid, new StringExtractor<Kupit_Svetlin_class>() {
                @Override
                public String getStringValue(Kupit_Svetlin_class nc, int position) {
                    return nc.getColor();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Kupit_Svetlin_class>() {
                @Override
                public String getStringValue(Kupit_Svetlin_class nc, int position) {
                    return nc.getLampa();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), svet_mas, R.layout.svet_list, dict);

            ListView list_ecola = (ListView) view.findViewById(R.id.list_ecola);
            list_ecola.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_ecola);

            list_ecola.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Kupit_Svetlin_class selectedid = svet_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, null, null, null, null, null);
//
                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_ecola"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            ecola();

                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Светильник удалён ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        Button btn_add_ecola = (Button) view.findViewById(R.id.btn_add_ecola);

        // создаем обработчик нажатия
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_ecola.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count>0) {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_ecola " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N26_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N26_ILLUMINATOR,illumin[0]);
                    values.put(DBHelper.KEY_N26_LAMP, lamp[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_ecola");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    ecola();

                    count_ecola.setText("");

                    toast = Toast.makeText(getActivity(),
                            "Светильник добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                } else {toast = Toast.makeText(getActivity(),
                        "Введите нужное количество светильников", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_ecola.setOnClickListener(on_click);

    }

    void cornice(){

        final EditText count_cornice;
        Button btn_add_cornice;
        ListView list_cornice;

        final String[] select_color = new String[1];
        final String[] select_lampa = new String[1];
        String id_calc;

        final ArrayList<Kupit_cornice> svet_mas = new ArrayList<>();

        final Integer[] type = new Integer[1];
        final Integer[] length = new Integer[1];

        ArrayList s_c = new ArrayList();
        ArrayList s_l = new ArrayList();

        final String[] item_content1 = {null};

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        s_c.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_type" +
                " WHERE _id = ' 10'";

        final Cursor[] c = {db.rawQuery(sqlQuewy, new String[]{})};

        if (c[0] != null) {
            if (c[0].moveToFirst()) {
                do {
                    for (String cn : c[0].getColumnNames()) {
                        item_content1[0] = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));
                    }
                    s_c.add(item_content1[0]);
                } while (c[0].moveToNext());
            }
        }
        c[0].close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_vid_cornice);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_c);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_color[0] = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_type" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c[0] = db.rawQuery(sqlQuewy, new String[]{select_color[0]});

                if (c[0] != null) {
                    if (c[0].moveToFirst()) {
                        do {
                            for (String cn : c[0].getColumnNames()) {
                                type[0] = Integer.valueOf(c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0))));
                            }
                        } while (c[0].moveToNext());
                    }
                }
                c[0].close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s_l.clear();
        String item_content2 = null;
        db = dbHelper.getWritableDatabase();

        sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 51'";

        c[0] = db.rawQuery(sqlQuewy, new String[]{});

        if (c[0] != null) {
            if (c[0].moveToFirst()) {
                do {
                    for (String cn : c[0].getColumnNames()) {
                        item_content2 = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));
                        Log.d("title",item_content2);
                    }

                    s_l.add(item_content2);
                } while (c[0].moveToNext());
            }
        }
        c[0].close();

        final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_diametr_cornice);
        SpinnerAdapter adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_l);
        ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_lampa[0] = spinner2.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                c[0] = db.rawQuery(sqlQuewy, new String[]{select_lampa[0]});

                if (c[0] != null) {
                    if (c[0].moveToFirst()) {
                        do {
                            for (String cn : c[0].getColumnNames()) {
                                length[0] = Integer.valueOf(c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0))));
                            }
                        } while (c[0].moveToNext());
                    }
                }
                c[0].close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (id_calculation == null) {
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null,
                    "calculation_id =?", new String[]{id_calculation}, null, null, null);

            if (cursor.moveToFirst()) {

                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N15_COUNT);
                int typeIndex = cursor.getColumnIndex(DBHelper.KEY_N15_TYPE);
                int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_N15_SIZE);

                do {

                    Log.d("cornice", String.valueOf(cursor.getString(kdIndex)));

                    String type_c = cursor.getString(typeIndex);
                    String length_c = cursor.getString(sizeIndex);
                    String type_str = "";
                    String length_str = "";

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_type" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{type_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                type_str = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{length_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                length_str = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    Kupit_cornice kc = new Kupit_cornice(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), type_str, length_str);
                    svet_mas.add(kc);

                } while (cursor.moveToNext());
            }
            cursor.close();

            BindDictionary<Kupit_cornice> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Kupit_cornice>() {
                @Override
                public String getStringValue(Kupit_cornice nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_vid, new StringExtractor<Kupit_cornice>() {
                @Override
                public String getStringValue(Kupit_cornice nc, int position) {
                    return nc.getType();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Kupit_cornice>() {
                @Override
                public String getStringValue(Kupit_cornice nc, int position) {
                    return nc.getLength();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), svet_mas, R.layout.svet_list, dict);

            list_cornice = (ListView) view.findViewById(R.id.list_cornice);
            list_cornice.setAdapter(adapter);

            setListViewHeightBasedOnChildren(list_cornice);

            list_cornice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Kupit_cornice selectedid = svet_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, null, null, null, null, null);
//
                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_cornice"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            cornice();

                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Карниз удалён ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        btn_add_cornice = (Button) view.findViewById(R.id.btn_add_cornices);

        count_cornice = (EditText) view.findViewById(R.id.count_cornice);

        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_cornice.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count>0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_cornice " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N15_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N15_TYPE, type[0]);
                    values.put(DBHelper.KEY_N15_SIZE, length[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_cornice");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Карниз добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    cornice();

                    count_cornice.setText("");

                } else {toast = Toast.makeText(getActivity(),
                        "Введите нужное количество карнизов", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_cornice.setOnClickListener(on_click);

    }

    void pipes() {

        final EditText count_pipes;
        Spinner spinner_diametr_pipes;
        Button btn_add_pipes;
        ListView list_pipes;

        ArrayList s_t = new ArrayList();

        final String[] select_truby = new String[1];
        String id_calc;

        final Integer[] count = new Integer[1];
        final Integer[] size = new Integer[1];

        final ArrayList<Truby_class> truby_mas = new ArrayList<>();

        String diametr;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String item_content1 = null;

        s_t.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 24' and count > ' 0'";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_t.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_diametr_pipes);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_t);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_truby[0] = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ? and component_id =' 24'";

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor c = db.rawQuery(sqlQuewy, new String[]{select_truby[0]});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                size[0] = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_pipes = (EditText) view.findViewById(R.id.count_pipes);

        if (id_calculation == null){
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, "calculation_id =?", new String[]{id_calculation},
                    null, null, null);

            if (cursor.moveToFirst()) {

                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N14_COUNT);
                int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_N14_SIZE);

                do {
                    String pipes_c = cursor.getString(sizeIndex);
                    String pipes = "";

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ? ";

                    Log.d("pipes = ", pipes + " | " + pipes_c);

                    c = db.rawQuery(sqlQuewy, new String[]{pipes_c});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    Truby_class t = new Truby_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), pipes);
                    truby_mas.add(t);
                } while (cursor.moveToNext());
            }

            cursor.close();

            BindDictionary<Truby_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_count, new StringExtractor<Truby_class>() {
                @Override
                public String getStringValue(Truby_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Truby_class>() {
                @Override
                public String getStringValue(Truby_class nc, int position) {
                    return nc.getDiametr();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), truby_mas, R.layout.list_2column, dict);

            list_pipes = (ListView) view.findViewById(R.id.list_pipes);
            list_pipes.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_pipes);

            list_pipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Truby_class selectedid = truby_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_pipes"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            pipes();

                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Трубы удалены ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        btn_add_pipes = (Button) view.findViewById(R.id.btn_add_pipes);
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_pipes.getText().toString().trim();
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();

                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_pipes " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N14_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N14_SIZE, String.valueOf(size[0]));
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_pipes");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Трубы добавлены ", Toast.LENGTH_SHORT);
                    toast.show();

                    pipes();

                    count_pipes.setText("");

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество труб", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        btn_add_pipes.setOnClickListener(on_click);
    }

    void profile(){

        final DBHelper[] dbHelper = new DBHelper[1];
        final EditText count_profile;
        Spinner spinner_profiles;
        ListView list_profile;

        ArrayList s_t = new ArrayList();

        final String[] select_truby = new String[1];

        final Integer[] size = new Integer[1];

        final ArrayList<Profile_class> truby_mas = new ArrayList<>();

        dbHelper[0] = new DBHelper(getActivity());
        final SQLiteDatabase db = dbHelper[0].getWritableDatabase();

        String item_content1 = null;

        s_t.clear();

        Cursor c = db.query("rgzbn_gm_ceiling_type", null, "_id between 12 and 16", null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    }
                    s_t.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        Log.d("profile", String.valueOf(s_t));

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_vid_profile);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_t);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_truby[0] = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_type" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                Cursor c = db.rawQuery(sqlQuewy, new String[]{select_truby[0]});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                size[0] = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_profile = (EditText) view.findViewById(R.id.count_profile);

        if (id_calculation == null) {
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, "calculation_id =?", new String[]{id_calculation},
                    null, null, null);

            if (cursor.moveToFirst()) {

                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N29_COUNT);
                int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_N29_TYPE);

                do {
                    String pipes_c = cursor.getString(sizeIndex);
                    String pipes = "";


                    String sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_type" +
                            " WHERE _id = ? ";

                    c = db.rawQuery(sqlQuewy, new String[]{pipes_c});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            } while (c.moveToNext());
                        }
                    }
                    c.close();

                    Profile_class t = new Profile_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), pipes);
                    truby_mas.add(t);
                } while (cursor.moveToNext());
            }

            cursor.close();

            BindDictionary<Profile_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_count, new StringExtractor<Profile_class>() {
                @Override
                public String getStringValue(Profile_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Profile_class>() {
                @Override
                public String getStringValue(Profile_class nc, int position) {
                    return nc.getDiametr();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), truby_mas, R.layout.list_2column, dict);

            list_profile = (ListView) view.findViewById(R.id.list_profile);
            list_profile.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_profile);

            list_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Profile_class selectedid = truby_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper[0] = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper[0].getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, null, null, null, null, null);

                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_profil"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            profile();
                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Профиль удалён ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        Button btn_add_profiles = (Button) view.findViewById(R.id.btn_add_profiles);

        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_profile.getText().toString().trim();
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    ContentValues values = new ContentValues();

                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_profil " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N29_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N29_TYPE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_profil");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Профиль добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    count_profile.setText("");
                    profile();

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество профиля", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_profiles.setOnClickListener(on_click);

    }

    void hoods(){

        final DBHelper[] dbHelper = new DBHelper[1];
        final EditText count_hoods;

        Button btn_add_hoods;
        ListView list_hoods;

        ArrayList s_t = new ArrayList();
        final ArrayList s_r = new ArrayList();

        final String[] select_type = new String[1];
        final String[] select_razmer = new String[1];
        String id_calc;

        final Integer[] type = new Integer[1];
        final Integer[] size = new Integer[1];

        final ArrayList<Ventil_class> vent_mas = new ArrayList<>();

        dbHelper[0] = new DBHelper(getActivity());
        final SQLiteDatabase db = dbHelper[0].getWritableDatabase();

        String item_content1 = null;

        s_t.clear();

        final Cursor[] c = {db.query("rgzbn_gm_ceiling_type", null, "_id between 5 and 8", null, null, null, null)};
        if (c[0] != null) {
            if (c[0].moveToFirst()) {
                do {
                    for (String cn : c[0].getColumnNames()) {
                        item_content1 = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(2)));
                    }
                    s_t.add(item_content1);
                } while (c[0].moveToNext());
            }
        }
        c[0].close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_type_hoods);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_t);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String c_id = "";

                select_type[0] = spinner1.getSelectedItem().toString();

                switch (spinner1.getSelectedItemPosition()){
                    case (0): c_id = " 21";
                        type[0] = 5;
                        break;
                    case (2): c_id = " 21";
                        type[0] = 7;
                        break;
                    case (1): c_id = " 12";
                        type[0] = 6;
                        break;
                    case (3): c_id = " 12";
                        type[0] = 8;
                        break;
                }

                s_r.clear();
                String item_content2 = null;
                SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                String sqlQuewy = "SELECT title "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE component_id = ?";

                c[0] = db.rawQuery(sqlQuewy, new String[]{c_id});

                if (c[0] != null) {
                    if (c[0].moveToFirst()) {
                        do {
                            for (String cn : c[0].getColumnNames()) {
                                item_content2 = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));
                                Log.d("title",item_content2);
                            }

                            s_r.add(item_content2);
                        } while (c[0].moveToNext());
                    }
                }
                c[0].close();


                final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_razmer_hoods);
                SpinnerAdapter adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_r);
                ((ArrayAdapter<String>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {

                        select_razmer[0] = spinner2.getSelectedItem().toString();

                        String sqlQuewy = "SELECT _id "
                                + "FROM rgzbn_gm_ceiling_components_option" +
                                " WHERE title = ?";

                        SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                        c[0] = db.rawQuery(sqlQuewy, new String[]{select_razmer[0]});

                        if (c[0] != null) {
                            if (c[0].moveToFirst()) {
                                do {
                                    for (String cn : c[0].getColumnNames()) {
                                        size[0] = Integer.valueOf(c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0))));
                                    }
                                } while (c[0].moveToNext());
                            }
                        }
                        c[0].close();

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_hoods = (EditText) view.findViewById(R.id.count_hoods);

        if (id_calculation == null){
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, "calculation_id =?", new String[]{id_calculation},
                    null, null, null);
//
            if (cursor.moveToFirst()) {
                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N22_COUNT);
                int typeIndex = cursor.getColumnIndex(DBHelper.KEY_N22_TYPE);
                int razmerIndex = cursor.getColumnIndex(DBHelper.KEY_N22_SIZE);
                do {
                    String type_c = cursor.getString(typeIndex);
                    String size_c = cursor.getString(razmerIndex);
                    String type_str = "";
                    String size_str = "";
                    String sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_type" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{type_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                type_str = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    c[0] = db.rawQuery(sqlQuewy, new String[]{size_c});

                    if (c[0] != null) {
                        if (c[0].moveToFirst()) {
                            do {
                                size_str = c[0].getString(c[0].getColumnIndex(c[0].getColumnName(0)));

                            } while (c[0].moveToNext());
                        }
                    }
                    c[0].close();

                    Ventil_class vent = new Ventil_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex),
                            type_str, size_str);
                    vent_mas.add(vent);
                } while (cursor.moveToNext());
            }
            cursor.close();

            BindDictionary<Ventil_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_kol_vo, new StringExtractor<Ventil_class>() {
                @Override
                public String getStringValue(Ventil_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_vid, new StringExtractor<Ventil_class>() {
                @Override
                public String getStringValue(Ventil_class nc, int position) {
                    return nc.getType();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Ventil_class>() {
                @Override
                public String getStringValue(Ventil_class nc, int position) {
                    return nc.getRazmer();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), vent_mas, R.layout.svet_list, dict);

            list_hoods = (ListView) view.findViewById(R.id.list_hoods);
            list_hoods.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_hoods);

            list_hoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Ventil_class selectedid = vent_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper[0] = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper[0].getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, null, null,
                                                    null, null, null);
//
                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);
                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_hoods"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            hoods();

                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Вентиляция удалена ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        btn_add_hoods= (Button) view.findViewById(R.id.btn_add_hoods);

        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_hoods.getText().toString().trim();
                int count=0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() >0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count>0) {
                    ContentValues values = new ContentValues();
                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_hoods " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N22_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N22_TYPE, type[0]);
                    values.put(DBHelper.KEY_N22_SIZE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_hoods");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Вентиляция добавленa ", Toast.LENGTH_SHORT);
                    toast.show();

                    hoods();
                    count_hoods.setText("");

                } else {toast = Toast.makeText(getActivity(),
                        "Введите нужное количество вентиляции", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_hoods.setOnClickListener(on_click);

    }

    void diffuzor(){

        final DBHelper[] dbHelper = new DBHelper[1];
        final EditText count_diffuzor;
        Spinner spinner_diffuzor;
        Button btn_add_diffuzor;
        ListView list_diffuzor;

        ArrayList s_d = new ArrayList();

        final String[] select_diffuzor = new String[1];

        final Integer[] size = new Integer[1];

        final ArrayList<Diffuzor_class> diff_mas = new ArrayList<>();

        dbHelper[0] = new DBHelper(getActivity());
        final SQLiteDatabase db = dbHelper[0].getWritableDatabase();

        String item_content1 = null;

        s_d.clear();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ' 22' and count > ' 0'";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }
                    s_d.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_diffuzor);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_d);
        ((ArrayAdapter<String>) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setSelection(0);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                select_diffuzor[0] = spinner1.getSelectedItem().toString();

                String sqlQuewy = "SELECT _id "
                        + "FROM rgzbn_gm_ceiling_components_option" +
                        " WHERE title = ?";

                SQLiteDatabase db = dbHelper[0].getWritableDatabase();

                Cursor c = db.rawQuery(sqlQuewy, new String[]{select_diffuzor[0]});

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                size[0] = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_diffuzor = (EditText) view.findViewById(R.id.count_diffuzor);

        if (id_calculation == null){
        } else {
            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, "calculation_id =?", new String[]{id_calculation},
                    null, null, null);
//
            if (cursor.moveToFirst()) {
                int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int kidIndex = cursor.getColumnIndex(DBHelper.KEY_CALCULATION_ID);
                int kol_voIndex = cursor.getColumnIndex(DBHelper.KEY_N23_COUNT);
                int razmerIndex = cursor.getColumnIndex(DBHelper.KEY_N23_SIZE);
                do {

                    String diff_c = cursor.getString(razmerIndex);
                    String diff = "";

                    sqlQuewy = "SELECT title "
                            + "FROM rgzbn_gm_ceiling_components_option" +
                            " WHERE _id = ?";

                    Log.d("pipes = ", diff + " " + diff_c);

                    c = db.rawQuery(sqlQuewy, new String[]{diff_c});

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                diff = c.getString(c.getColumnIndex(c.getColumnName(0)));

                            } while (c.moveToNext());
                        }
                    }
                    c.close();
                    Diffuzor_class d = new Diffuzor_class(cursor.getString(kdIndex), cursor.getString(kidIndex),
                            cursor.getString(kol_voIndex), diff);
                    diff_mas.add(d);
                } while (cursor.moveToNext());
            }
            cursor.close();

            BindDictionary<Diffuzor_class> dict = new BindDictionary<>();

            dict.addStringField(R.id.tv_count, new StringExtractor<Diffuzor_class>() {
                @Override
                public String getStringValue(Diffuzor_class nc, int position) {
                    return nc.getKol_vo();
                }
            });
            dict.addStringField(R.id.tv_diam, new StringExtractor<Diffuzor_class>() {
                @Override
                public String getStringValue(Diffuzor_class nc, int position) {
                    return nc.getRazmer();
                }
            });

            FunDapter adapter = new FunDapter(getActivity(), diff_mas, R.layout.list_2column, dict);

            list_diffuzor = (ListView) view.findViewById(R.id.list_diffuzor);
            list_diffuzor.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_diffuzor);

            list_diffuzor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Diffuzor_class selectedid = diff_mas.get(position);
                    final String s_id = selectedid.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Удалить выбранный элемент?")
                            .setMessage(null)
                            .setIcon(null)
                            .setCancelable(false)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dbHelper[0] = new DBHelper(getActivity());
                                            SQLiteDatabase db = dbHelper[0].getReadableDatabase();
                                            Cursor cursor = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, null,
                                                    null, null, null, null);
//
                                            if (cursor.moveToFirst()) {
                                                int kd_Index = cursor.getColumnIndex(DBHelper.KEY_ID);

                                                do {
                                                    if (s_id.equals(cursor.getString(kd_Index))) {
                                                        db.delete(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, "_id = ?", new String[]{String.valueOf(s_id)});
                                                        db.delete(DBHelper.HISTORY_SEND_TO_SERVER, "id_old = ? and name_table=?",
                                                                new String[]{String.valueOf(s_id), "rgzbn_gm_ceiling_diffusers"});
                                                    }
                                                }
                                                while (cursor.moveToNext());
                                            }

                                            diffuzor();

                                            cursor.close();

                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Диффузор удалён ", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        btn_add_diffuzor = (Button) view.findViewById(R.id.btn_add_diffuzor);
        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_diffuzor.getText().toString().trim();
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    ContentValues values = new ContentValues();

                    int max_id_contac=0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_diffusers " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id_contac ++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e){
                        max_id_contac = user_id_int + 1;
                    }

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N23_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N23_SIZE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id_contac);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_diffusers");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "0");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Диффузор добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    count_diffuzor.setText("");
                    diffuzor();

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество диффузоров", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_diffuzor.setOnClickListener(on_click);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0); totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

                //id_calculation = getActivity().getIntent().getStringExtra("id_calculation");  // id_calculation

                String old_n2 = "";
                String old_n3 = "";
                String old_n4 = "";
                String old_n5 = "";
                try {
                    String sqlQuewy = "select n2, n3, n4, n5 "
                            + "FROM rgzbn_gm_ceiling_calculations " +
                            "where _id=? ";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{id_calculation});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                old_n2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                old_n3 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                old_n4 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                old_n5 = c.getString(c.getColumnIndex(c.getColumnName(3)));
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

                if (old_n3.equals("") || old_n2.equals("") || old_n4.equals("0.0") || old_n5.equals("0.0") || old_n4.equals("0") || old_n5.equals("0")) {
                    calculat = false;
                    calculation();
                } else {
                    if (old_n3.equals(String.valueOf(id_n3)) && old_n2.equals(String.valueOf(texture_id))) {
                        calculat = false;
                        calculation();
                    } else if (id_calculation == null) {
                        calculat = false;
                        calculation();
                    } else {
                        SP = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
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


                        SP = getActivity().getSharedPreferences("draft_diags_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", diags_points);
                        ed.commit();

                        SP = getActivity().getSharedPreferences("draft_walls_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", walls_points);
                        ed.commit();

                        SP = getActivity().getSharedPreferences("draft_pt_points", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", pt_points);
                        ed.commit();

                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Осуществляется перерасчёт ", Toast.LENGTH_SHORT);
                        toast.show();

                        intent = new Intent(getActivity(), Activity_draft.class);
                        startActivity(intent);

                    }
                }
                break;
            case R.id.chertezh:

                SP = getActivity().getSharedPreferences("draft_diags_points", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getActivity().getSharedPreferences("draft_walls_points", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getActivity().getSharedPreferences("draft_pt_points", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                SP = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
                ed = SP.edit();
                ed.putString("", "");
                ed.commit();

                intent = new Intent(getActivity(), Activity_draft.class);
                startActivity(intent);
                break;
            case R.id.btn_texture:
                s_setMessage = " Выберите фактуру для Вашего будущего потолка \n  Матовый больше похож на побелку \n  Сатин - на, крашенный потолок \n  Глянец - имеет лёгкий отблеск";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_width:
                s_setMessage = " От производителя материала зависит качество потолка и его цена";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_vstavka:
                s_setMessage = "    Между стеной и натяжным потолком после монтажа остаётся технологический засор 5мм, который закрывается декоративной вставкой";
                s_setMessage1 = "";
                s_setdrawable = String.valueOf(R.raw.vstavka);
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
                s_setMessage = "    На упаковке светильника указан диаметр технологического отверстия";
                s_setMessage1 = "(Чтобы удалить ненужный светильник, нажмите на него в таблице)";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_add_kupit_svetiln:
                s_setMessage = "(Чтобы удалить ненужный светильник, нажмите на него в таблице)";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_karniz:
                s_setMessage = "    Если его не будет или он будет крепиться к стене просто пропустите этот пункт. " +
                        "\nШторный карниз можно крепить на потолок двумя способами:" +
                        "\n 1.Видимый";
                s_setMessage1 = "2.Скрытый (в этом случае надо указать длину стены, на которой окно и ставить галочку напротив надписи скрытый шторный карниз)";
                s_setdrawable = String.valueOf(R.raw.karniz1);
                s_setdrawable1 = String.valueOf(R.raw.karniz2);
                fun_builder();
                break;
            case R.id.btn_add_cornice:
                s_setMessage = "    (Чтобы удалить ненужный корниз, нажмите на него в таблице)";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_add_truby:
                s_setMessage = "    На картинке изображены 3 трубы разного диаметра. Выбираем отдельно одну трубу 55, а две других 32";
                s_setMessage1 = "(Чтобы удалить ненужный светильник, нажмите на него в таблице)";
                s_setdrawable = String.valueOf(R.raw.obvod);
                s_setdrawable1 = "";
                fun_builder();
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
                s_setMessage = "    (Чтобы удалить ненужную вентиляцию, нажмите на неё в таблице)";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.btn_add_diff:
                s_setMessage = "    Вентиляционная решётка для отвода воздуха, который попадает за потолочное пространство\n    (Чтобы удалить ненужный диффузор, нажмите на него в таблице)";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.color_canvases:
                intent = new Intent(getActivity(), Activity_color.class);
                Log.d("mLog", texture_id);
                intent.putExtra("texture_id", texture_id);
                startActivity(intent);
                break;
            case R.id.btn_rb_v:
                intent = new Intent(getActivity(), Activity_color.class);
                intent.putExtra("component_id", "15");
                startActivity(intent);
                break;

            case R.id.btn_add_profile:
                s_setMessage = "    (Чтобы удалить ненужный переход, нажмите на него в таблице)";
                s_setMessage1 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
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
            case R.id.btn_drain_the_water:
                s_setMessage = "    В работу входит слив воды. Укажите количество комнат";
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

        int cou = 0;
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
        for (int i = 0; i < cou + 10; i++) {
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
                        if (res.equals("") || res == null) {
                            res = "0";
                        }
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
                "where title LIKE('%303 белая%') and component_id = 15";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items_vstavka_bel = c.getInt(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        Log.d("mLog", "items_vstavka_bel " + String.valueOf(items_vstavka_bel));

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
                component_count.set(items_11, component_count.get(items_11) + P);
            } else if (rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + P);
            } else if (rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + P);
            }
        }

        // внутренний вырез(на месте)
        if (ed_in_cut.getText().toString().equals("") || ed_in_cut.getText().toString().equals("0") || ed_in_cut.getText().toString().equals("0.0")) {
        } else {
            double cut = Double.valueOf(ed_in_cut.getText().toString());
            component_count.set(items_1, component_count.get(items_1) + cut);
            if (n1.equals("29")) {
                component_count.set(items_233, component_count.get(items_233) + cut);
            } else if (n1.equals("28") && rb_baget.equals("0")) {
                component_count.set(items_11, component_count.get(items_11) + cut);
            } else if (n1.equals("28") && rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + cut);
            } else if (n1.equals("28") && rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + cut);
            }
            component_count.set(items_430, component_count.get(items_430) + cut * 3);
            component_count.set(items_8, component_count.get(items_8) + cut * 22);
            component_count.set(items_5, component_count.get(items_5) + cut * 16);
            component_count.set(items_360, component_count.get(items_360) + cut);
        }

        // внутренний вырез(в цеху)
        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0") || ed_in_cut_shop.getText().toString().equals("0.0")) {
        } else {
            Double cut = Double.valueOf(ed_in_cut_shop.getText().toString());
            double n31_count = Math.ceil(cut);

            try {
                String id_color_vs = "0";
                SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                id_color_vs = SPSO.getString("", "");
                if (id_color_vs.equals("0")) {
                    component_count.set(items_vstavka_bel, n31_count);
                } else {
                    component_count.set(Integer.parseInt(id_color_vs), n31_count);
                }
            } catch (Exception e) {
            }

            if (n1.equals("28") && rb_baget.equals("0")) {
                component_count.set(items_11, component_count.get(items_11) + cut);
            } else if (n1.equals("28") && rb_baget.equals("1")) {
                component_count.set(items_236, component_count.get(items_236) + cut);
            } else if (n1.equals("28") && rb_baget.equals("2")) {
                component_count.set(items_239, component_count.get(items_239) + cut);
            }
            component_count.set(items_9, component_count.get(items_9) + cut * 10);
            component_count.set(items_5, component_count.get(items_5) + cut * 10);
        }

        String id_color_vs = "0";
        try {
            SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
            id_color_vs = SPSO.getString("", "");

            Log.d("mLog", "id_color_vs " + id_color_vs);
            double n5_count = Math.ceil(P + 0.5);
            component_count.set(Integer.parseInt(id_color_vs), n5_count + 0.5);
        } catch (Exception e) {
        }

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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         //сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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

        //профиль
        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_profil " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         //сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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

        for (j = 0; i > j; j++) {
            if ((n29_type[j] == 12) || (n29_type[j] == 13)) {
                component_count.set(items_659, component_count.get(items_659) + n29_count[j]);
            } else if ((n29_type[j] == 15) || (n29_type[j] == 16)) {
                component_count.set(items_660, component_count.get(items_660) + n29_count[j]);
            }

        }

        // вентиляция
        i = 0;
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_hoods " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         //сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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

        //труба
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_pipes " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // смотрим сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // смотрим сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // смотрим сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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

        //экола
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_ecola " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // смотрим сколько всего в таблице
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
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_calculation)});         // заполняем массивы из таблицы
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
            }
        }

        // дополнительный крепеж
        double count_fasteners = 0;
        if (ed_fasteners.getText().toString().equals("") || ed_fasteners.getText().toString().equals("0")) {
        } else {
            count_fasteners = Double.parseDouble(ed_fasteners.getText().toString());
            component_count.set(items_9, component_count.get(items_9) + count_fasteners * 10);
            if (n1.equals("29")) {
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
            component_count.set(items_495, component_count.get(items_495) + count_fire);
            component_count.set(items_58, component_count.get(items_58) + count_fire);
            component_count.set(items_3, component_count.get(items_3) + count_fire * 3);
            component_count.set(items_5, component_count.get(items_5) + count_fire * 3);
            component_count.set(items_2, component_count.get(items_2) + count_fire * 2);
        }

        //стеновой багет 2.5м считается кусками, которые потребуются выложить весь периметр
        if (rb_baget.equals("0")) {
            rouding(items_11, component_count.get(items_11), 2.5);
        } else if (rb_baget.equals("1")) {
            rouding(items_236, component_count.get(items_236), 2.5);
        } else if (rb_baget.equals("2")) {
            rouding(items_239, component_count.get(items_239), 2.5);
        }

        Log.d("mLog", "items11 4 " + component_count.get(items_11));

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

        double count_ed_in_cut_shop = 0;
        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0")) {
        } else {
            count_ed_in_cut_shop = Double.valueOf(ed_in_cut_shop.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Внутренний вырез(в цеху)");
            values.put(DBHelper.KEY_QUANTITY, count_ed_in_cut_shop);
            values.put(DBHelper.KEY_STACK, "0");
            values.put(DBHelper.KEY_GM_PRICE, results.get(21));
            values.put(DBHelper.KEY_GM_TOTAL, count_ed_in_cut_shop * results.get(21));
            values.put(DBHelper.KEY_DEALER_PRICE, results.get(21));
            values.put(DBHelper.KEY_DEALER_TOTAL, count_ed_in_cut_shop * results.get(21));
            db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
        }

        // другие комп
        if (final_comp.equals("{}")){
        } else {
            int comp = 0;
            try {
            org.json.JSONObject dat = new org.json.JSONObject(final_comp);
            do {
                try {
                    JSONObject id_array = dat.getJSONObject(String.valueOf(comp));
                    String title = id_array.getString("title");
                    String value = id_array.getString("value");

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.KEY_TITLE, title);
                    values.put(DBHelper.KEY_QUANTITY, "1");
                    values.put(DBHelper.KEY_STACK, "0");
                    values.put(DBHelper.KEY_GM_PRICE, value);
                    values.put(DBHelper.KEY_GM_TOTAL, value);
                    values.put(DBHelper.KEY_DEALER_PRICE, value);
                    values.put(DBHelper.KEY_DEALER_TOTAL, value);
                    db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
                } catch (Exception e) {
                }
                comp++;
            } while (dat.length() != comp);
        } catch (Exception e) {
            Log.d("extra_comp", String.valueOf(e));
        }
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
                canvases_data.set(6, double_margin(price, gm_can_marg, dealer_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
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

                try {
                    //Сюда считаем итоговую сумму обрезков
                    canvases_data.set(0, "Количесво обрезков");                 // название
                    canvases_data.set(1, Double.valueOf(square_obr));           // кол-во
                    canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                    canvases_data.set(3, Math.rint(100 * (Double.valueOf(square_obr) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                    canvases_data.set(4, Math.rint(100 * (margin(price, gm_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(5, Math.rint(100 * Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, gm_can_marg, dealer_can_marg) / 100 * 40, gm_can_marg, dealer_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                    canvases_data.set(7, Math.rint(100 * (Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

                } catch (Exception e){
                }

                can_sum += Double.parseDouble(String.valueOf(canvases_data.get(3)));

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

            canvases_data.set(0, texture + ", " + canvases + ", " + wf);                         // название
            canvases_data.set(1, Double.valueOf(S));                                             // кол-во
            canvases_data.set(2, price);                                                         // цена
            canvases_data.set(3, price * Double.valueOf(S));                                     // Кол-во * Себестоимость
            canvases_data.set(4, margin(price, gm_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
            canvases_data.set(5, Math.rint(100.0 * (margin(price, gm_can_marg)) * S) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
            canvases_data.set(6, double_margin(price, gm_can_marg, dealer_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
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

                try {
                    canvases_data.set(0, "Количесво обрезков");                 // название
                    canvases_data.set(1, Double.valueOf(square_obr));           // кол-во
                    canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                    canvases_data.set(3, Math.rint(100 * (Double.valueOf(square_obr) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                    canvases_data.set(4, Math.rint(100 * (margin(price, gm_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(5, Math.rint(100 * Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, gm_can_marg, dealer_can_marg) / 100 * 40, gm_can_marg, dealer_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                    canvases_data.set(7, Math.rint(100 * (Double.parseDouble(square_obr) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)
                } catch (Exception e){
                }

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

        int cost = 0;
        if (rb_h.equals("1")){
            cost = 10;
        }

        //периметр только для ПВХ
        if (n1.equals("28") && P > 0 && rb_baget.equals("0")) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(0) + cost);
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * (results.get(0) + cost));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(0) + cost);
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * (results.get(0) + cost));
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

        count_ed_in_cut_shop = 0;
        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0") || ed_in_cut_shop.getText().toString().equals("0.0")) {
        } else {
            count_ed_in_cut_shop = Double.valueOf(ed_in_cut_shop.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр (внутренний вырез)");
            values.put(DBHelper.KEY_QUANTITY, count_ed_in_cut_shop);
            values.put(DBHelper.KEY_GM_SALARY, results.get(0));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_ed_in_cut_shop * results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_ed_in_cut_shop * results.get(0));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        //if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0")) {
        //} else {
        //    count_ed_in_cut_shop = Double.valueOf(ed_in_cut_shop.getText().toString());
        //    ContentValues values = new ContentValues();
        //    values.put(DBHelper.KEY_TITLE, "Периметр (внутренний вырез)");
        //    values.put(DBHelper.KEY_QUANTITY, count_ed_in_cut_shop);
        //    values.put(DBHelper.KEY_GM_SALARY, results.get(21));
        //    values.put(DBHelper.KEY_GM_SALARY_TOTAL, count_ed_in_cut_shop * results.get(21));
        //    values.put(DBHelper.KEY_DEALER_SALARY, results.get(21));
        //    values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, count_ed_in_cut_shop * results.get(21));
        //    db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        //}

        SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
        Log.d("mLog", "vstav = " + SPSO.getString("", ""));

        //вставка
        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0") || ed_in_cut_shop.getText().toString().equals("0.0")) {
        } else {
            try {
                String id_color = "0";
                ContentValues values = new ContentValues();

                SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                id_color = SPSO.getString("", "");
                if (id_color.equals("0")) {
                    values.put(DBHelper.KEY_TITLE, "Вставка(внутренний вырез)");
                } else {
                    values.put(DBHelper.KEY_TITLE, "Вставка(внутренний вырез), цвет: " + id_color);
                }
                double cut_shop = Double.parseDouble(ed_in_cut_shop.getText().toString());
                values.put(DBHelper.KEY_QUANTITY, cut_shop);
                values.put(DBHelper.KEY_GM_SALARY, results.get(9));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, cut_shop * results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, cut_shop * results.get(9));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);

            } catch (Exception e) {
            }
        }

        //Слив воды
        int drain = 0;
        if (ed_drain_the_water.getText().toString().equals("") || ed_drain_the_water.getText().toString().equals("0.0") ||
                ed_drain_the_water.getText().toString().equals("0")) {
        } else {
            drain = Integer.valueOf(ed_drain_the_water.getText().toString());
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Слив воды");
            values.put(DBHelper.KEY_QUANTITY, drain);
            values.put(DBHelper.KEY_GM_SALARY, results.get(26));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, drain * results.get(26));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(26));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, drain * results.get(26));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        double mount_w = 0;
        if (mount_wall.getText().toString().equals("") || mount_wall.getText().toString().equals("0") || mount_wall.getText().toString().equals("0.0")) {
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
        if (mount_granite.getText().toString().equals("") || mount_granite.getText().toString().equals("0") || mount_granite.getText().toString().equals("0.0")) {
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

        double count_s = 0;
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

        // другие работы
        if (final_mount.equals("{}")){
        } else {
            int comp = 0;
            try {
                org.json.JSONObject dat = new org.json.JSONObject(final_mount);
                do {
                    try {
                        JSONObject id_array = dat.getJSONObject(String.valueOf(comp));
                        String title = id_array.getString("title");
                        String value = id_array.getString("value");

                        ContentValues values = new ContentValues();
                        values.put(DBHelper.KEY_TITLE, title);
                        values.put(DBHelper.KEY_QUANTITY, "1");
                        values.put(DBHelper.KEY_GM_SALARY, value);
                        values.put(DBHelper.KEY_GM_SALARY_TOTAL, value);
                        values.put(DBHelper.KEY_DEALER_SALARY, value);
                        values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, value);
                        db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
                    } catch (Exception e) {
                    }

                    comp++;

                } while (dat.length() != comp);
            } catch (Exception e) {
            }
        }

        sqlQuewy = "select * " +
                "FROM mounting_data";
        Cursor k = db.rawQuery(sqlQuewy, new String[]{});         // заполняем массивы из табли
        if (k != null)
            if (k.moveToFirst()) {
                do {

                    String id = k.getString(k.getColumnIndex(k.getColumnName(0)));
                    String quantity = String.valueOf(k.getInt(k.getColumnIndex(k.getColumnName(2))));

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

        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        String dealer_calc = SP.getString("", "");
        if (dealer_calc.equals("true")) {
            btn_save.setVisibility(View.VISIBLE);
            name_project.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    scroll_calc.scrollTo(0, 30000);

                }
            }, 1);

            int max_id_proj = 0;
            try {
                sqlQuewy = "select MAX(_id) "
                        + "FROM rgzbn_gm_ceiling_projects " +
                        "where _id>? and _id<?";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 99999)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            max_id_proj = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            max_id_proj++;
                        } while (c.moveToNext());
                    }
                }
            } catch (Exception e) {
                max_id_proj = user_id_int + 1;
            }
            id_project = String.valueOf(max_id_proj);
        }

        if (calculat) {
            int id_color = 0;
            int id_color_vs_int = 0;
            try {
                SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
                id_color = Integer.valueOf(SPSO.getString("", ""));

                SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                id_color_vs_int = Integer.valueOf(SPSO.getString("", ""));

                Log.d("mLog", "id_color_vs_int1 " + id_color_vs_int);
            } catch (Exception e) {

            }
            if (name_project.getText().toString().equals("")) {

                int count_calc = 0;
                sqlQuewy = "select * "
                        + "FROM rgzbn_gm_ceiling_calculations " +
                        "where project_id=?";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id_project)});
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            count_calc++;
                        } while (c.moveToNext());
                    }
                }

                count_calc++;
                name_project.setText("Потолок " + count_calc);
            }
            if (square_obr == null) {
                square_obr = "0";
            }

            String original_sk = "";
            String cut_d = "";

            if (SAVED_KP.equals("")) {
                if (id_calculation != null) {
                    original_sk = original_sketch;
                }
            } else {
                String save = "{" + SAVED_KP.substring(1, SAVED_KP.length() - 1) + "}";
                StringBuffer sbb = new StringBuffer();
                cou = 0;

                for (i = 0; i < save.length(); ) {
                    if (save.startsWith("[", i) && save.startsWith("{", i + 1)) {
                        cou++;
                        sbb.append("\"kp" + cou + "\"").append(':').append('[');
                    } else {
                        sbb.append(save.charAt(i));
                    }
                    i++;
                }

                save = String.valueOf(sbb);

                Log.d("mLog", save);

                for (i = 1; i < cou + 1; i++) {

                    cut_d += "Полотно" + i + ": ";

                    try {

                        JSONObject jsonObject = new JSONObject(save);

                        JSONArray kp = jsonObject.getJSONArray("kp" + i);

                        for (int y = 0; y < kp.length(); y++) {

                            org.json.JSONObject kp1 = kp.getJSONObject(y);

                            String name = kp1.getString("name");
                            String koordinats = kp1.getString("koordinats");

                            cut_d += " " + name + koordinats + ",";
                        }
                    } catch (Exception e) {
                    }

                    cut_d = cut_d.substring(0, cut_d.length() - 1);
                    cut_d += "; ";
                }

                try {
                    save = "{wp:" + SAVED_WP + "}";
                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray wp = jsonObject.getJSONArray("wp");

                    for (int y = 0; y < wp.length(); y++) {

                        org.json.JSONObject wp1 = wp.getJSONObject(y);

                        String s0_x = wp1.getString("s0_x");
                        String s0_y = wp1.getString("s0_y");
                        String s1_x = wp1.getString("s1_x");
                        String s1_y = wp1.getString("s1_y");

                        original_sk += s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e) {
                }

                original_sk += "||";
                try {
                    save = "{dp:" + SAVED_DP + "}";

                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray dp = jsonObject.getJSONArray("dp");

                    for (int y = 0; y < dp.length(); y++) {

                        org.json.JSONObject dp1 = dp.getJSONObject(y);

                        String s0_x = dp1.getString("s0_x");
                        String s0_y = dp1.getString("s0_y");
                        String s1_x = dp1.getString("s1_x");
                        String s1_y = dp1.getString("s1_y");

                        original_sk += s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e) {
                }

                original_sk += "||";
                try {
                    save = "{pt_p:" + SAVED_PT_P + "}";

                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray pt_p = jsonObject.getJSONArray("pt_p");

                    for (int y = 0; y < pt_p.length(); y++) {

                        org.json.JSONObject pt_p1 = pt_p.getJSONObject(y);

                        String x = pt_p1.getString("x");
                        String yy = pt_p1.getString("y");

                        original_sk += x + ";" + yy + ";";
                    }
                } catch (Exception e) {
                }

                original_sk += "||" + SAVED_CODE + "||" + SAVED_ALFAVIT;
            }

            int ordering = 0;
            int state = 1;
            int checked_out = 0;
            String checked_out_time = "0000-00-00 00:00:00";
            int created_by = Integer.parseInt(user_id);
            int modified_by = Integer.parseInt(user_id);
            String calculation_title = name_project.getText().toString();
            int project_id = Integer.parseInt(id_project);
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
            String n31 = String.valueOf(count_ed_in_cut_shop);
            String n32 = String.valueOf(drain);
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
            String height = rb_h;
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
            values.put(DBHelper.KEY_N31, n31);
            values.put(DBHelper.KEY_N32, n32);
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
            values.put(DBHelper.KEY_HEIGHT, height);
            values.put(DBHelper.KEY_EXTRA_COMPONENTS, final_comp);
            values.put(DBHelper.KEY_EXTRA_MOUNTING, final_mount);

            int max_id_contac = 0;
            id_calculation = getActivity().getIntent().getStringExtra("id_calculation");  // id_calculation
            if (id_calculation != null) {
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{id_calculation});

                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Расчёт обновлён ", Toast.LENGTH_SHORT);
                toast.show();

                sync(Integer.parseInt(id_calculation));
            } else {
                //values.put(DBHelper.KEY_ID, max_id);
                db = dbHelper.getReadableDatabase();
                //values = new ContentValues();
                try {
                    sqlQuewy = "select MAX(_id) "
                            + "FROM rgzbn_gm_ceiling_calculations " +
                            "where _id>? and _id<?";
                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                max_id_contac = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                max_id_contac++;
                            } while (c.moveToNext());
                        }
                    }
                } catch (Exception e) {
                    max_id_contac = user_id_int + 1;
                }
                Log.d("mLog", "calc1 = " + max_id_contac);

                values.put(DBHelper.KEY_ID, max_id_contac);
                db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);

                sync(max_id_contac);

                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Расчёт добавлен ", Toast.LENGTH_SHORT);
                toast.show();

            }

            if (id_project != null) {
                SharedPreferences SP = getActivity().getSharedPreferences("end_activity_inform_proj", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", "1");
                ed.commit();
            }

            delete_comp = false;
            if (dealer_calc.equals("true")) {
                Intent intent = new Intent(getActivity(), Activity_zamer.class);
                startActivity(intent);
            }

            getActivity().finish();
        }
    }

    void sync(Integer id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ID_OLD, id);
        values.put(DBHelper.KEY_ID_NEW, "0");
        values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_calculations");
        values.put(DBHelper.KEY_SYNC, "0");
        values.put(DBHelper.KEY_TYPE, "send");
        values.put(DBHelper.KEY_STATUS, "1");
        db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

        String sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_cornice " +
                "where calculation_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String cornice = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{cornice, "rgzbn_gm_ceiling_cornice"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_diffusers " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String dif = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{dif, "rgzbn_gm_ceiling_diffusers"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_profil " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String prof = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{prof, "rgzbn_gm_ceiling_profil"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_ecola " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String ecola = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{ecola, "rgzbn_gm_ceiling_ecola"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_fixtures " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String fixtures = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{fixtures, "rgzbn_gm_ceiling_fixtures"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_hoods " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String hoods = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{hoods, "rgzbn_gm_ceiling_hoods"});
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_pipes " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.update(DBHelper.HISTORY_SEND_TO_SERVER, values, "id_old = ? and name_table=?",
                            new String[]{pipes, "rgzbn_gm_ceiling_pipes"});
                } while (c.moveToNext());
            }
        }

        getActivity().startService(new Intent(getActivity(), Service_Sync.class));
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

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

        android.app.AlertDialog alert = builder.create();
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

        final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_canvases);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_c);
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

                                SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
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
                    SP = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
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

                final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_texture);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, s_t);
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

                        SP = getActivity().getSharedPreferences("textures_draft", MODE_PRIVATE);
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