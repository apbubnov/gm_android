package ru.ejevikaapp.gm_android.Fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.pixplicity.sharp.Sharp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.ejevikaapp.gm_android.Activity_color;
import ru.ejevikaapp.gm_android.Activity_draft;
import ru.ejevikaapp.gm_android.Activity_inform_proj;
import ru.ejevikaapp.gm_android.Activity_zamer;
import ru.ejevikaapp.gm_android.Class.Diffuzor_class;
import ru.ejevikaapp.gm_android.Class.Extra_class;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Class.Kupit_Svetlin_class;
import ru.ejevikaapp.gm_android.Class.Kupit_cornice;
import ru.ejevikaapp.gm_android.Class.Profile_class;
import ru.ejevikaapp.gm_android.Class.Svetiln_class;
import ru.ejevikaapp.gm_android.Class.Truby_class;
import ru.ejevikaapp.gm_android.Class.Ventil_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;
import ru.ejevikaapp.gm_android.Service_Sync;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_calculation extends Fragment implements View.OnClickListener {

    Button chertezh, texture_help, canvas_help, btn_light, btn_add_kupit_svetiln, btn_rb_v;

    Button btn_color_canvases, btn_save, btn_add_cornice, btn_calculate;

    Button buguette_hide, vstavka_hide, lustr_hide, svetiln_hide, cabling_hide, karniz_hide, pipes_hide, bond_beam_hide, separator_hide,
            mount_wall_hide, mount_granite_hide, wall_hide, fasteners_hide, fire_hide, add_vent_hide, diff_acc_hide, soaring_ceiling_hide,
            add_profile_hide, in_cut_hide, in_cut_shop_hide, drain_the_water_hide, height_hide, add_other_comp_hide, add_other_mount_hide,
            add_diff_hide, mounting_hide, general_hide;

    Button buguette_help, vstavka_help, lustr_help, svetiln_help, cabling_help, karniz_help, pipes_help, bond_beam_help, separator_help,
            mount_wall_help, mount_granite_help, wall_help, fasteners_help, fire_help, add_vent_help, diff_acc_help, soaring_ceiling_help,
            add_profile_help, in_cut_help, in_cut_shop_help, drain_the_water_help, height_help, add_other_comp_help, add_other_mount_help,
            add_diff_help, mounting_help;

    LinearLayout buguette_layout, vstavka_layout, lustr_layout, svetiln_layout, karniz_layout, pipes_layout, add_vent_layout,
            add_other_comp_layout, add_other_mount_layout, add_profile_layout, add_diff_layout, general_layout, second_layout;

    EditText ed_cabling, bond_beam, ed_separator, mount_wall, mount_granite, ed_wall, ed_fasteners, ed_fire, soaring_ceiling, ed_in_cut,
            ed_in_cut_shop, ed_drain_the_water, ed_diff_acc;

    RadioGroup radios_height, radios_mounting;

    int profile_12_13, profile_15_16, circle_count = 0, square_count = 0;

    TextView area, perimetr, corners, text_calculate;

    ArrayAdapter<String> adapter;

    public int colorIndex = 0;

    DBHelper dbHelper;

    ArrayList s_c = new ArrayList();
    ArrayList s_t = new ArrayList();

    String texture = "", canvases = "", s_setMessage, s_setMessage1, s_setMessage2, s_setdrawable, s_setdrawable1, offcut_square, s_sp5 = "", s_spa = "",
            id_project, id_calculation, canvases_id;
    String width_final = "", cut_image = "", calc_image = "", rb_vstavka = "0", save_n3 = "", n3, lines_length, user_id = "", dealer_id_str = "", final_comp = "", final_mount = "",
            original_sketch = "", cut_data = "", calc_data = "";

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
            count_vent, count_electr, count_diffus, count_pipes, user_id_int, id_n3 = 0;

    Double S = 0.0, P = 0.0;

    ImageView image;

    CheckBox RB_karniz;

    SQLiteDatabase db;

    EditText lustr, karniz, name_project, ed_discount, count_comp, price_comp;

    boolean rb_k = false, calculat = false, btn_color_canvases_visible = false, mounting = true, delete_comp = true, chertezh_bool = false;

    ArrayList<Double> component_count = new ArrayList();    // component_option
    ArrayList canvases_data = new ArrayList();
    ArrayList mounting_data = new ArrayList();
    ArrayList<Integer> results = new ArrayList();
    ArrayList component_item = new ArrayList();

    int items_9, items_5, items_11, items_vstavka_bel, items_vstavka, items_10, items_16, items_556, items_4, items_58, items_3,
            items_2, items_1, items_8, items_6, items_14, items_430, items_35, items_360, items_236,
            items_239, items_559, items_38, items_495, items_233, items_659, items_660;

    double n7 = 0, n8 = 0, n9 = 0, n10 = 0, n11 = 0, n12 = 0, n16 = 0, n17 = 0, n18 = 0, n19 = 0, n20 = 0, n21 = 0, n24 = 0, n25 = 0,
            n27 = 0, n28 = 0, n30 = 0, n31 = 0, n32 = 0, height = 0, dop_krepezh = 0, discount = 0;
    int n6 = 0, color = 0, save_n2 = 0;
    Integer n2 = 0;

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

    RadioButton rb_v_white, rb_v_color, rb_v_no, rb_m_yes, rb_m_no, rb_b_no, rb_b_norm, rb_b_potol, rb_b_all, rb_h_no, rb_h_yes;

    // fixtures
    Button btn_add_svetilnik;
    ListView list_svetilnik;

    View view;

    ScrollView scroll_calc;

    boolean bool_resume = false;

    LinearLayout linear_image;

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
        linear_image = (LinearLayout) view.findViewById(R.id.linear_image);

        chertezh = (Button) view.findViewById(R.id.chertezh);
        btn_color_canvases = (Button) view.findViewById(R.id.color_canvases);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_add_cornice = (Button) view.findViewById(R.id.btn_add_cornice);
        btn_rb_v = (Button) view.findViewById(R.id.btn_rb_v);
        btn_calculate = (Button) view.findViewById(R.id.btn_calculate);

        chertezh.setOnClickListener(this);
        btn_color_canvases.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_add_cornice.setOnClickListener(this);
        btn_rb_v.setOnClickListener(this);
        btn_calculate.setOnClickListener(this);

        buguette_hide = (Button) view.findViewById(R.id.buguette_hide);
        vstavka_hide = (Button) view.findViewById(R.id.vstavka_hide);
        lustr_hide = (Button) view.findViewById(R.id.lustr_hide);
        svetiln_hide = (Button) view.findViewById(R.id.svetiln_hide);
        cabling_hide = (Button) view.findViewById(R.id.cabling_hide);
        karniz_hide = (Button) view.findViewById(R.id.karniz_hide);
        pipes_hide = (Button) view.findViewById(R.id.pipes_hide);
        bond_beam_hide = (Button) view.findViewById(R.id.bond_beam_hide);
        separator_hide = (Button) view.findViewById(R.id.separator_hide);
        mount_wall_hide = (Button) view.findViewById(R.id.mount_wall_hide);
        mount_granite_hide = (Button) view.findViewById(R.id.mount_granite_hide);
        wall_hide = (Button) view.findViewById(R.id.wall_hide);
        fasteners_hide = (Button) view.findViewById(R.id.fasteners_hide);
        fire_hide = (Button) view.findViewById(R.id.fire_hide);
        add_vent_hide = (Button) view.findViewById(R.id.add_vent_hide);
        diff_acc_hide = (Button) view.findViewById(R.id.diff_acc_hide);
        soaring_ceiling_hide = (Button) view.findViewById(R.id.soaring_ceiling_hide);
        add_profile_hide = (Button) view.findViewById(R.id.add_profile_hide);
        in_cut_hide = (Button) view.findViewById(R.id.in_cut_hide);
        in_cut_shop_hide = (Button) view.findViewById(R.id.in_cut_shop_hide);
        height_hide = (Button) view.findViewById(R.id.height_hide);
        add_other_comp_hide = (Button) view.findViewById(R.id.add_other_comp_hide);
        add_other_mount_hide = (Button) view.findViewById(R.id.add_other_mount_hide);
        add_diff_hide = (Button) view.findViewById(R.id.add_diff_hide);
        drain_the_water_hide = (Button) view.findViewById(R.id.drain_the_water_hide);
        mounting_hide = (Button) view.findViewById(R.id.mounting_hide);
        general_hide = (Button) view.findViewById(R.id.general_hide);

        buguette_hide.setOnClickListener(this);
        vstavka_hide.setOnClickListener(this);
        lustr_hide.setOnClickListener(this);
        svetiln_hide.setOnClickListener(this);
        cabling_hide.setOnClickListener(this);
        karniz_hide.setOnClickListener(this);
        pipes_hide.setOnClickListener(this);
        bond_beam_hide.setOnClickListener(this);
        separator_hide.setOnClickListener(this);
        mount_wall_hide.setOnClickListener(this);
        mount_granite_hide.setOnClickListener(this);
        wall_hide.setOnClickListener(this);
        fasteners_hide.setOnClickListener(this);
        fire_hide.setOnClickListener(this);
        add_vent_hide.setOnClickListener(this);
        diff_acc_hide.setOnClickListener(this);
        soaring_ceiling_hide.setOnClickListener(this);
        add_profile_hide.setOnClickListener(this);
        in_cut_hide.setOnClickListener(this);
        in_cut_shop_hide.setOnClickListener(this);
        height_hide.setOnClickListener(this);
        add_other_comp_hide.setOnClickListener(this);
        add_other_mount_hide.setOnClickListener(this);
        add_diff_hide.setOnClickListener(this);
        drain_the_water_hide.setOnClickListener(this);
        mounting_hide.setOnClickListener(this);
        general_hide.setOnClickListener(this);

        buguette_help = (Button) view.findViewById(R.id.buguette_help);
        texture_help = (Button) view.findViewById(R.id.texture_help);
        canvas_help = (Button) view.findViewById(R.id.canvas_help);
        vstavka_help = (Button) view.findViewById(R.id.vstavka_help);
        lustr_help = (Button) view.findViewById(R.id.lustr_help);
        svetiln_help = (Button) view.findViewById(R.id.svetiln_help);
        karniz_help = (Button) view.findViewById(R.id.karniz_help);
        pipes_help = (Button) view.findViewById(R.id.pipes_help);
        fire_help = (Button) view.findViewById(R.id.fire_help);
        add_vent_help = (Button) view.findViewById(R.id.add_vent_help);
        add_diff_help = (Button) view.findViewById(R.id.add_diff_help);
        wall_help = (Button) view.findViewById(R.id.wall_help);
        add_profile_help = (Button) view.findViewById(R.id.add_profile_help);
        fasteners_help = (Button) view.findViewById(R.id.fasteners_help);
        in_cut_help = (Button) view.findViewById(R.id.in_cut_help);
        diff_acc_help = (Button) view.findViewById(R.id.diff_acc_help);
        separator_help = (Button) view.findViewById(R.id.separator_help);
        soaring_ceiling_help = (Button) view.findViewById(R.id.soaring_ceiling_help);
        mount_wall_help = (Button) view.findViewById(R.id.mount_wall_help);
        mount_granite_help = (Button) view.findViewById(R.id.mount_granite_help);
        cabling_help = (Button) view.findViewById(R.id.cabling_help);
        bond_beam_help = (Button) view.findViewById(R.id.bond_beam_help);
        in_cut_shop_help = (Button) view.findViewById(R.id.in_cut_shop_help);
        drain_the_water_help = (Button) view.findViewById(R.id.drain_the_water_help);
        height_help = (Button) view.findViewById(R.id.height_help);
        add_other_comp_help = (Button) view.findViewById(R.id.add_other_comp_help);
        add_other_mount_help = (Button) view.findViewById(R.id.add_other_mount_help);
        mounting_help = (Button) view.findViewById(R.id.mounting_help);

        mounting_help.setOnClickListener(this);
        add_other_comp_help.setOnClickListener(this);
        add_other_mount_help.setOnClickListener(this);
        height_help.setOnClickListener(this);
        buguette_help.setOnClickListener(this);
        texture_help.setOnClickListener(this);
        canvas_help.setOnClickListener(this);
        vstavka_help.setOnClickListener(this);
        lustr_help.setOnClickListener(this);
        svetiln_help.setOnClickListener(this);
        karniz_help.setOnClickListener(this);
        pipes_help.setOnClickListener(this);
        fire_help.setOnClickListener(this);
        add_vent_help.setOnClickListener(this);
        add_diff_help.setOnClickListener(this);
        add_profile_help.setOnClickListener(this);
        wall_help.setOnClickListener(this);
        fasteners_help.setOnClickListener(this);
        in_cut_help.setOnClickListener(this);
        diff_acc_help.setOnClickListener(this);
        separator_help.setOnClickListener(this);
        soaring_ceiling_help.setOnClickListener(this);
        mount_wall_help.setOnClickListener(this);
        mount_granite_help.setOnClickListener(this);
        cabling_help.setOnClickListener(this);
        bond_beam_help.setOnClickListener(this);
        in_cut_shop_help.setOnClickListener(this);
        drain_the_water_help.setOnClickListener(this);

        buguette_layout = (LinearLayout) view.findViewById(R.id.buguette_layout);
        vstavka_layout = (LinearLayout) view.findViewById(R.id.vstavka_layout);
        lustr_layout = (LinearLayout) view.findViewById(R.id.lustr_layout);
        svetiln_layout = (LinearLayout) view.findViewById(R.id.svetiln_layout);
        karniz_layout = (LinearLayout) view.findViewById(R.id.karniz_layout);
        pipes_layout = (LinearLayout) view.findViewById(R.id.pipes_layout);
        add_other_comp_layout = (LinearLayout) view.findViewById(R.id.add_other_comp_layout);
        add_other_mount_layout = (LinearLayout) view.findViewById(R.id.add_other_mount_layout);
        add_profile_layout = (LinearLayout) view.findViewById(R.id.add_profile_layout);
        add_vent_layout = (LinearLayout) view.findViewById(R.id.add_vent_layout);
        add_diff_layout = (LinearLayout) view.findViewById(R.id.add_diff_layout);
        general_layout = (LinearLayout) view.findViewById(R.id.general_layout);
        second_layout = (LinearLayout) view.findViewById(R.id.second_layout);

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

        text_calculate = (TextView) view.findViewById(R.id.text_calculate);

        RB_karniz = (CheckBox) view.findViewById(R.id.RB_karniz);

        RB_karniz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RB_karniz.isChecked()) {
                    n16 = 1;
                } else {
                    n16 = 0;
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

        radios_height = (RadioGroup) view.findViewById(R.id.radios_height);
        radios_height.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_h_no:
                        height = 0;
                        break;
                    case R.id.rb_h_yes:
                        height = 1;
                        break;
                }
            }
        });

        extra_comp("{}");
        extra_mount("{}");

        rb_m_yes = (RadioButton) view.findViewById(R.id.rb_m_yes);
        rb_m_no = (RadioButton) view.findViewById(R.id.rb_m_no);

        rb_m_yes.setChecked(true);

        mounting = true;
        radios_mounting = (RadioGroup) view.findViewById(R.id.radios_mounting);
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
        rb_b_norm = (RadioButton) view.findViewById(R.id.rb_b_norm);
        rb_b_potol = (RadioButton) view.findViewById(R.id.rb_b_potol);
        rb_b_all = (RadioButton) view.findViewById(R.id.rb_b_all);

        n28 = 3;

        RadioGroup radios_b = (RadioGroup) view.findViewById(R.id.radios_b);
        radios_b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.rb_b_no:
                        n28 = 3;
                        break;

                    case R.id.rb_b_norm:
                        n28 = 0;
                        break;

                    case R.id.rb_b_potol:
                        n28 = 1;
                        break;

                    case R.id.rb_b_all:
                        n28 = 2;
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
        db = dbHelper.getReadableDatabase();

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
                        String n1_2 = k.getString(k.getColumnIndex(k.getColumnName(1)));
                        n2 = Integer.parseInt(k.getString(k.getColumnIndex(k.getColumnName(2))));
                        n3 = k.getString(k.getColumnIndex(k.getColumnName(3)));
                        String n4_2 = k.getString(k.getColumnIndex(k.getColumnName(4)));
                        String n5_2 = k.getString(k.getColumnIndex(k.getColumnName(5)));
                        String n6_2 = k.getString(k.getColumnIndex(k.getColumnName(6)));
                        String n7_2 = k.getString(k.getColumnIndex(k.getColumnName(7)));
                        String n8_2 = k.getString(k.getColumnIndex(k.getColumnName(8)));
                        String n9_2 = k.getString(k.getColumnIndex(k.getColumnName(9)));
                        String n10_2 = k.getString(k.getColumnIndex(k.getColumnName(10)));
                        String n11_2 = k.getString(k.getColumnIndex(k.getColumnName(11)));
                        String n12_2 = k.getString(k.getColumnIndex(k.getColumnName(12)));
                        String n16_2 = k.getString(k.getColumnIndex(k.getColumnName(13)));
                        String n17_2 = k.getString(k.getColumnIndex(k.getColumnName(14)));
                        String n18_2 = k.getString(k.getColumnIndex(k.getColumnName(15)));
                        String n19_2 = k.getString(k.getColumnIndex(k.getColumnName(16)));
                        String n20_2 = k.getString(k.getColumnIndex(k.getColumnName(17)));
                        String n21_2 = k.getString(k.getColumnIndex(k.getColumnName(18)));
                        String n24_2 = k.getString(k.getColumnIndex(k.getColumnName(19)));
                        String n25_2 = k.getString(k.getColumnIndex(k.getColumnName(20)));
                        String dop_krepezh_2 = k.getString(k.getColumnIndex(k.getColumnName(21)));
                        calc_image = k.getString(k.getColumnIndex(k.getColumnName(22)));
                        String n27_2 = k.getString(k.getColumnIndex(k.getColumnName(23)));
                        String color_2 = k.getString(k.getColumnIndex(k.getColumnName(24)));
                        offcut_square = k.getString(k.getColumnIndex(k.getColumnName(25)));
                        String discount_2 = k.getString(k.getColumnIndex(k.getColumnName(26)));
                        String n28_2 = k.getString(k.getColumnIndex(k.getColumnName(27)));
                        String n30_2 = k.getString(k.getColumnIndex(k.getColumnName(28)));
                        original_sketch = k.getString(k.getColumnIndex(k.getColumnName(29)));
                        String n31_2 = k.getString(k.getColumnIndex(k.getColumnName(30)));
                        String n32_2 = k.getString(k.getColumnIndex(k.getColumnName(31)));
                        String height_2 = k.getString(k.getColumnIndex(k.getColumnName(32)));
                        String extra_components_2 = k.getString(k.getColumnIndex(k.getColumnName(33)));
                        String extra_mounting_2 = k.getString(k.getColumnIndex(k.getColumnName(34)));

                        extra_comp(extra_components_2);
                        extra_mount(extra_mounting_2);

                        name_project.setText(calculation_title);
                        area.setText(" S = " + n4_2 + " м2");
                        S = Double.valueOf(n4_2);
                        perimetr.setText(" P = " + n5_2 + " м");
                        P = Double.valueOf(n5_2);

                        SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                        ed = SPSO.edit();
                        ed.putString("", n6_2);
                        ed.commit();

                        mount_wall.setText(n7_2);
                        mount_granite.setText(n8_2);
                        corners.setText(" Количество углов =   " + n9_2);
                        n9 = Double.valueOf(n9_2);
                        ed_in_cut.setText(n11_2);
                        ed_in_cut_shop.setText(n31_2);
                        ed_drain_the_water.setText(n32_2);
                        lustr.setText(n12_2);
                        if (n16_2 == "1") {
                            n16 = 1;
                        }
                        ed_wall.setText(n18_2);
                        ed_cabling.setText(n19_2);
                        ed_separator.setText(n20_2);
                        ed_fire.setText(n21_2);
                        ed_diff_acc.setText(n24_2);
                        bond_beam.setText(n17_2);
                        ed_fasteners.setText(dop_krepezh_2);
                        StringBuffer sb = new StringBuffer(calc_image.subSequence(0, calc_image.length()));
                        sb.delete(0, 22);

                        try {
                            Sharp.loadString(calc_image).into(image);
                            linear_image.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                        }

                        karniz.setText(n27_2);

                        db = dbHelper.getWritableDatabase();
                        sqlQuewy = "select hex "
                                + "FROM rgzbn_gm_ceiling_colors " +
                                "where title = ? ";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{color_2});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    btn_color_canvases.setEnabled(true);
                                    String hex = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                    btn_color_canvases.setBackgroundColor(Color.parseColor("#" + hex));
                                    btn_color_canvases_visible = true;

                                    SP = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
                                    ed = SP.edit();
                                    ed.putString("", color_2);
                                    ed.commit();

                                } while (c.moveToNext());
                            }
                        }
                        c.close();

                        if (height_2.equals("0")) {
                            rb_h_no.setChecked(true);
                        } else if (height_2.equals("1")) {
                            rb_h_yes.setChecked(true);
                        }

                        if (n6_2.equals("0")) {
                            rb_v_no.setChecked(true);
                        } else if (n6_2.equals("314")) {
                            rb_v_white.setChecked(true);
                        } else {
                            rb_v_color.setChecked(true);
                            db = dbHelper.getWritableDatabase();
                            sqlQuewy = "select hex "
                                    + "FROM rgzbn_gm_ceiling_colors " +
                                    "where title = ? ";
                            c = db.rawQuery(sqlQuewy, new String[]{n6_2});
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

                        if (offcut_square.equals("")) {
                            offcut_square = "0";
                        }

                        if (n28_2 == null) {
                            n28_2 = "0";
                        }

                        if (n28_2.equals("0")) {
                            n28 = 0;
                            rb_b_norm.setChecked(true);
                        } else if (n28_2.equals("1")) {
                            n28 = 1;
                            rb_b_potol.setChecked(true);
                        } else if (n28_2.equals("2")) {
                            n28 = 2;
                            rb_b_all.setChecked(true);
                        } else if (n28_2.equals("3")) {
                            n28 = 3;
                            rb_b_no.setChecked(true);
                        }

                        soaring_ceiling.setText(n30_2);
                        ed_discount.setText(discount_2);

                    } while (k.moveToNext());
                }
            }
            k.close();
            canv_text();
        } else {
            canv_text();
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuewy;
        Cursor c;
        if (bool_resume) {
            Log.d("mLog", "RESUME");
            Log.d("mLog", "id_calc = " + id_calculation);

            SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
            if (SPSO.getString("", "").equals("")) {
                color = 0;
            } else {
                color = Integer.parseInt(SPSO.getString("", ""));
            }

            if (color == 0) {
            } else {

                sqlQuewy = "select hex "
                        + "FROM rgzbn_gm_ceiling_colors " +
                        "where title = ? ";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(color)});
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

            SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
            String id_color_vs = SPSO.getString("", "");

            if (id_color_vs.equals("")) {
            } else {
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
            }

            SPSO = getActivity().getSharedPreferences("SAVED_SO", MODE_PRIVATE);
            if (SPSO.getString("", "").equals("")) {
            } else if (SPSO.getString("", "").equals("")) {
            } else {
                offcut_square = SPSO.getString("", "");
            }

            SP4 = getActivity().getSharedPreferences("SAVED_N4", MODE_PRIVATE);
            if (SP4.getString("", "").length() == 0) {
                area.setText(" S =  м2");
                if (S.equals("")) {
                } else {
                    area.setText(" S = " + S + "  м2");
                }
            } else {
                S = Double.valueOf(SP4.getString("", ""));
                area.setText(" S = " + S + "  м2");
            }

            SP5 = getActivity().getSharedPreferences("SAVED_N5", MODE_PRIVATE);
            if (SP5.getString("", "").length() == 0) {
                perimetr.setText(" P =  м");
                if (P.equals("")) {
                } else {
                    perimetr.setText(" P = " + P + "  м");
                }
            } else {
                P = Double.valueOf(SP5.getString("", ""));
                perimetr.setText(" P = " + P + "  м");
            }

            SP9 = getActivity().getSharedPreferences("SAVED_N9", MODE_PRIVATE);
            if (SP9.getString("", "").length() == 0) {
                corners.setText(" Количество углов =   ");
                if (SP9.getString("", "").equals("")) {
                } else {
                    corners.setText(" Количество углов = " + n9);
                }
            } else {
                n9 = Double.valueOf(SP9.getString("", ""));
                corners.setText(" Количество углов = " + n9);
            }

            SPW = getActivity().getSharedPreferences("SAVED_WIDTH", MODE_PRIVATE);
            if (SPW.getString("", "").equals("")) {
            } else {
                width_final = SPW.getString("", "");
            }

            SPW = getActivity().getSharedPreferences("SAVED_LL", MODE_PRIVATE);
            if (SPW.getString("", "").equals("")) {
            } else {
                lines_length = SPW.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_I", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                calc_image = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_I_CUT", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                cut_image = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_KP", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_KP = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_WP", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_WP = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_DP", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_DP = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_PT_P", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_PT_P = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("SAVED_CODE", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_CODE = SPI.getString("", "");
            }

            SPI = getActivity().getSharedPreferences("end_draft", MODE_PRIVATE);
            String end_draft = SPI.getString("", "");

            SPI = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
            String draft_auto = SPI.getString("", "");

            if (end_draft.equals("1") && draft_auto.equals("1")) {
                calculat = false;
                calculation();
            }

            SPI = getActivity().getSharedPreferences("SAVED_ALFAVIT", MODE_PRIVATE);
            if (SPI.getString("", "").equals("")) {
            } else {
                SAVED_ALFAVIT = SPI.getString("", "");
            }

            try {
                Sharp.loadString(calc_image).into(image);
                linear_image.setVisibility(View.VISIBLE);
            } catch (Exception e) {
            }

        } else bool_resume = true;


        if (id_project == null) {
        } else {
            if (chertezh_bool) {
                calculat = true;
                calculation();

            }
        }

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

        SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
        ed = SPSO.edit();
        ed.putString("", "0");
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

        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        ed = SP.edit();
        ed.putString("", "");
        ed.commit();
    }

    void extra_comp(String extra_components) {

        final ArrayList<Extra_class> extra_mas = new ArrayList<>();
        final int[] i = {0};

        Log.d("extra_components", String.valueOf(extra_components));
        if (extra_components.equals("{}")) {

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

                                        int key = 0;
                                        Log.d("extra_comp1", String.valueOf(s_id));

                                        try {
                                            JSONObject dat = new JSONObject(final_extra_comp);
                                            do {
                                                if (s_id == j[0]) {
                                                } else {
                                                    JSONObject id_array = dat.getJSONObject(String.valueOf(j[0]));
                                                    String title = id_array.getString("title");
                                                    String value = id_array.getString("value");

                                                    extra += "\"" + key + "\":{\"title\":\"" + title + "\",\"value\":\"" + value + "\"},";

                                                    key++;
                                                    Log.d("extra_comp2", String.valueOf(extra));
                                                }
                                                j[0]++;
                                            } while (dat.length() != j[0]);
                                            extra = extra.substring(0, extra.length() - 1) + "}";

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

                    if (finalI == 0) {
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

        if (extra_components.equals("null")) {
            extra_components = "{}";
        }
        final_comp = extra_components;
    }

    void extra_mount(String extra_mounting) {

        final ArrayList<Extra_class> extra_mas = new ArrayList<>();
        final int[] i = {0};

        if (extra_mounting.equals("{}")) {

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

                                        int key = 0;
                                        Log.d("extra_mount1", String.valueOf(s_id));

                                        try {
                                            JSONObject dat = new JSONObject(final_extra_comp);
                                            do {
                                                if (s_id == j[0]) {
                                                } else {
                                                    JSONObject id_array = dat.getJSONObject(String.valueOf(j[0]));
                                                    String title = id_array.getString("title");
                                                    String value = id_array.getString("value");

                                                    extra += "\"" + key + "\":{\"title\":\"" + title + "\",\"value\":\"" + value + "\"},";

                                                    key++;
                                                    Log.d("extra_mount2", String.valueOf(extra));
                                                }
                                                j[0]++;
                                            } while (dat.length() != j[0]);
                                            extra = extra.substring(0, extra.length() - 1) + "}";

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

                    if (finalI == 0) {
                        finalExtra_comp[0] = finalExtra_comp[0].substring(0, finalExtra_comp[0].length() - 1)
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

    void fixtures() {

        final DBHelper[] dbHelper = new DBHelper[1];
        final EditText count_fixtures;

        final String[] select_vid = new String[1];
        final String[] select_diam = new String[1];
        final String[] c_id = {""};

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

        final Cursor[] fix_c = {db.query("rgzbn_gm_ceiling_type", null, "_id between 2 and 3",
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

                switch (spinner1.getSelectedItemPosition()) {
                    case (0):
                        c_id[0] = " 21";
                        type_id[0] = 2;
                        break;
                    case (1):
                        c_id[0] = " 12";
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

        count_fixtures = (EditText) view.findViewById(R.id.count_fixtures);

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

                        String vid_c = cursor.getString(vidIndex);
                        String diam_c = cursor.getString(diametrIndex);
                        String vid = "";
                        String diam = "";

                        if (vid_c.equals("2")) {
                            circle_count++;
                        } else if (vid_c.equals("3")) {
                            square_count++;
                        }
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
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }
                if (count > 0) {

                    ContentValues values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_fixtures " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N13_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N13_TYPE, type_id[0]);
                    values.put(DBHelper.KEY_N13_SIZE, comp_opt[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_FIXTURES, null, values);

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
                        Log.d("title", item_content2);
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

        final EditText count_ecola = (EditText) view.findViewById(R.id.count_ecola);

        if (id_calculation == null) {
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
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count > 0) {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_ecola " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N26_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N26_ILLUMINATOR, illumin[0]);
                    values.put(DBHelper.KEY_N26_LAMP, lamp[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_ECOLA, null, values);

                    ecola();

                    count_ecola.setText("");

                    toast = Toast.makeText(getActivity(),
                            "Светильник добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество светильников", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_ecola.setOnClickListener(on_click);

    }

    void cornice() {

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
                        Log.d("title", item_content2);
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
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count > 0) {

                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_cornice " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N15_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N15_TYPE, type[0]);
                    values.put(DBHelper.KEY_N15_SIZE, length[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CORNICE, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Карниз добавлен ", Toast.LENGTH_SHORT);
                    toast.show();

                    cornice();

                    count_cornice.setText("");

                } else {
                    toast = Toast.makeText(getActivity(),
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

        if (id_calculation == null) {
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

                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_pipes " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N14_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N14_SIZE, String.valueOf(size[0]));
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PIPES, null, values);

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

    void profile() {

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

                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_profil " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N29_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N29_TYPE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROFIL, null, values);

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

    void hoods() {

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

                switch (spinner1.getSelectedItemPosition()) {
                    case (0):
                        c_id = " 21";
                        type[0] = 5;
                        break;
                    case (2):
                        c_id = " 21";
                        type[0] = 7;
                        break;
                    case (1):
                        c_id = " 12";
                        type[0] = 6;
                        break;
                    case (3):
                        c_id = " 12";
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
                                Log.d("title", item_content2);
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

        if (id_calculation == null) {
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

        btn_add_hoods = (Button) view.findViewById(R.id.btn_add_hoods);

        View.OnClickListener on_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                String kol_vo = count_hoods.getText().toString().trim();
                int count = 0;
                if (kol_vo.length() == 0) {
                    count = 0;
                } else if (kol_vo.length() > 0) {
                    count = Integer.parseInt(kol_vo);
                }

                if (count > 0) {
                    ContentValues values = new ContentValues();
                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_hoods " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N22_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N22_TYPE, type[0]);
                    values.put(DBHelper.KEY_N22_SIZE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_HOODS, null, values);

                    toast = Toast.makeText(getActivity(),
                            "Вентиляция добавленa ", Toast.LENGTH_SHORT);
                    toast.show();

                    hoods();
                    count_hoods.setText("");

                } else {
                    toast = Toast.makeText(getActivity(),
                            "Введите нужное количество вентиляции", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        btn_add_hoods.setOnClickListener(on_click);

    }

    void diffuzor() {

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

        if (id_calculation == null) {
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

                    int max_id_contac = 0;
                    try {
                        String sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_diffusers " +
                                "where _id>? and _id<?";
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    values.put(DBHelper.KEY_CALCULATION_ID, id_calculation);
                    values.put(DBHelper.KEY_N23_COUNT, kol_vo);
                    values.put(DBHelper.KEY_N23_SIZE, size[0]);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_DIFFUSERS, null, values);

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
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
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
                second_layout.setVisibility(View.VISIBLE);

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                int old_n2 = 0;
                int old_n3 = 0;
                int old_n4 = 0;
                int old_n5 = 0;
                try {
                    String sqlQuewy = "select n2, n3, n4, n5 "
                            + "FROM rgzbn_gm_ceiling_calculations " +
                            "where _id=? ";
                    Cursor c = db.rawQuery(sqlQuewy, new String[]{id_calculation});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                try {
                                    old_n2 = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                } catch (Exception e) {
                                    old_n2 = 0;
                                }
                                try {
                                    old_n3 = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(1))));
                                } catch (Exception e) {
                                    old_n3 = 0;
                                }
                                try {
                                    old_n4 = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(2))));
                                } catch (Exception e) {
                                    old_n4 = 0;
                                }
                                try {
                                    old_n5 = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(3))));
                                } catch (Exception e) {
                                    old_n5 = 0;
                                }
                            } while (c.moveToNext());
                        }
                    }
                } catch (Exception e) {
                }

                String nc = "_";
                String sqlQuewy = "select name, country "
                        + "FROM rgzbn_gm_ceiling_canvases " +
                        "where _id = ? ";
                Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(old_n3)});         // заполняем массивы из таблицы
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            String name = c.getString(c.getColumnIndex(c.getColumnName(0)));
                            String country = c.getString(c.getColumnIndex(c.getColumnName(1)));
                            nc = name + " " + country;
                            if (nc.equals(canvases) && (n2 == old_n2)) {
                                id_n3 = Integer.valueOf(old_n3);
                            } else {
                                id_n3 = 0;
                            }
                        } while (c.moveToNext());
                    }
                }
                c.close();

                Log.d("perera", old_n3 + " " + old_n2);
                Log.d("perera", old_n3 + " " + id_n3);
                Log.d("perera", old_n2 + " " + n2);
                Log.d("perera", save_n2 + " " + n2);
                Log.d("perera", save_n3 + " " + id_n3);
                Log.d("perera", save_n3 + " " + canvases);

                if ((((old_n3 == 0 && old_n2 == 0) || (old_n3 == id_n3 && old_n2 == n2))
                        && (save_n2 == n2 || save_n2 == 0)
                        && (save_n3.equals(id_n3) || save_n3.equals(""))
                        || (save_n3.equals(canvases) || save_n3.equals(""))) || calc_image.length()<10) {
                    Log.d("perera","then");

                    if (old_n3 == 0 || old_n2 == 0 || old_n4 == 0 || old_n5 == 0 || old_n4 == 0 || old_n5 == 0) {
                        calculat = false;
                        calculation();
                        save_n2 = n2;
                        save_n3 = String.valueOf(canvases);
                    } else {
                        if (old_n3 == id_n3 && old_n2 == n2) {
                            calculat = false;
                            calculation();
                            save_n2 = n2;
                            save_n3 = String.valueOf(canvases);
                        } else if (id_calculation == null) {
                            calculat = false;
                            calculation();
                            save_n2 = n2;
                            save_n3 = String.valueOf(canvases);
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
                            diags_points = "[" + diags_points.substring(0, diags_points.length() - 1) + "]";
                            pt_points = "[" + pt_points.substring(0, pt_points.length() - 1) + "]";

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

                } else {
                    Log.d("perera","else");

                    save_n2 = n2;
                    save_n3 = String.valueOf(canvases);

                    sqlQuewy = "select * " +
                            " FROM rgzbn_gm_ceiling_canvases " +
                            " where texture_id = ? " +
                            " ORDER BY width DESC";

                    db = dbHelper.getWritableDatabase();
                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n2)});

                    String str = "[";

                    if (c.moveToFirst()) {
                        canvases_id = c.getString(c.getColumnIndex(DBHelper.KEY_ID));
                        int nameIndex = c.getColumnIndex(DBHelper.KEY_NAME);
                        int countryIndex = c.getColumnIndex(DBHelper.KEY_COUNTRY);
                        int priceIndex = c.getColumnIndex(DBHelper.KEY_PRICE);
                        int widthIndex = c.getColumnIndex(DBHelper.KEY_WIDTH);
                        do {
                            nc = c.getString(nameIndex) + " " + c.getString(countryIndex);

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

                    c.close();

                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(SAVED_TEXT, String.valueOf(strr)); // передача выбранных полотен на чертилку
                    ed.commit();

                    SP = getActivity().getSharedPreferences("textures_draft", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", String.valueOf(n2));
                    ed.commit();

                    SP = getActivity().getSharedPreferences("draft_auto", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", String.valueOf(1));
                    ed.commit();


                    SP = getActivity().getSharedPreferences("draft_diags_points", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", SAVED_DP);
                    ed.commit();

                    SP = getActivity().getSharedPreferences("draft_walls_points", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", SAVED_WP);
                    ed.commit();

                    SP = getActivity().getSharedPreferences("draft_pt_points", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", SAVED_PT_P);
                    ed.commit();

                    if (SAVED_DP.equals("") || SAVED_WP.equals("") || SAVED_PT_P.equals("")) {
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
                        diags_points = "[" + diags_points.substring(0, diags_points.length() - 1) + "]";
                        pt_points = "[" + pt_points.substring(0, pt_points.length() - 1) + "]";

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
                    }

                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Осуществляется перерасчёт ", Toast.LENGTH_SHORT);
                    toast.show();

                    intent = new Intent(getActivity(), Activity_draft.class);
                    startActivity(intent);

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


                SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
                String dealer_calc = SP.getString("", "");

                if (id_project == null || dealer_calc.equals("true")) {
                } else {
                    chertezh_bool = true;
                }

                break;
            case R.id.canvas_help:
                s_setMessage = "    Выберите фактуру для Вашего будущего потолка \n  " +
                        "Матовый больше похож на побелку \n  " +
                        "Сатин - на, крашенный потолок \n  " +
                        "Глянец - имеет лёгкий отблеск";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.texture_help:
                s_setMessage = "    От производителя материала зависит качество потолка и его цена!";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.buguette_help:
                s_setMessage = "    В расчет входит багет (2,5 м) \n" +
                        "А также на 1 м багета:\n" +
                        "10 саморезов (ГДК 3,5*51)\n" +
                        "10 дюбелей (красн. 6*51)\n" +
                        "+ монтажная работа по обагечиванию";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.vstavka_help:
                s_setMessage = "    Между стеной и натяжным потолком после монтажа остаётся технологический засор 5мм, который закрывается декоративной вставкой";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = String.valueOf(R.raw.vstavka);
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.lustr_help:
                s_setMessage = "    В расчет входит:\n" +
                        "3 самореза (ГДК 3,5*51)\n" +
                        "3 дюбеля (красн. 6*51)\n" +
                        "8 саморезов (п/сф 305*9,5 цинк)\n" +
                        "1 шуруп кольцо (6*40)\n" +
                        "2 клеммные пары\n" +
                        "1 круглое кольцо (50)\n" +
                        "1 платформа под люстру (тарелка)\n" +
                        "4 подвеса прямых (П 60 (0,8))\n" +
                        "0,5м провода (ПВС 2*0,75)\n" +
                        "+ монтажная работа по установке люстр";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.svetiln_help:
                s_setMessage = "    В расчет входит:\n" +
                        "4 самореза (ГДК 3,5*51)\n" +
                        "2 дюбеля (красн. 6*51)\n" +
                        "4 саморезов (п/сф 305*9,5 цинк)\n" +
                        "термоквадрат или круглое кольцо\n" +
                        "1 клеммная пара\n" +
                        "1 платформа под светильник (квадратная или круглая)\n" +
                        "2 подвеса прямых (П 60 (0,8))\n" +
                        "+ монтажная работа по установке светильников";
                s_setMessage1 = "(Чтобы удалить ненужный светильник, нажмите на него в таблице)";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.karniz_help:
                s_setMessage = "    Шторный карниз можно крепить на потолок двумя способами.\n" +
                        "                       Видимый:";
                s_setMessage1 = "   В расчет на 1м карниза входит:\n" +
                        "1м бруса (40*50)\n" +
                        "6 саморезов (ГДК 3,5*51)\n" +
                        "6 дюбелей (красн. 6*51)\n" +
                        "9 саморезов (ГДК 3,5*41)\n" +
                        "3 подвеса прямых (П 60 (0,8))\n\n " +
                        "                       Скрытый:";
                s_setMessage2 = "   В расчет на 1м скрытого карниза входит:\n" +
                        "1м бруса (40*50)\n" +
                        "6 саморезов (ГДК 3,5*51)\n" +
                        "6 дюбелей (красн. 6*51)\n" +
                        "13 саморезов (ГДК 3,5*41)\n" +
                        "3 подвеса прямых (П 60 (0,8))\n" +
                        "2 белых кронштейна (15*12,5)\n" +
                        "+ монтажная работа по установке шторного карниза";
                s_setdrawable = String.valueOf(R.raw.karniz1);
                s_setdrawable1 = String.valueOf(R.raw.karniz2);
                fun_builder();
                break;
            case R.id.btn_add_cornice:
                s_setMessage = "    (Чтобы удалить ненужный корниз, нажмите на него в таблице)";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.pipes_help:
                s_setMessage = "    В расчет на 1 трубу входит 1 пластина\n" +
                        "+ монтажная работа по обводу трубы";
                s_setMessage1 = "(Чтобы удалить ненужный светильник, нажмите на него в таблице)";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.fire_help:
                s_setMessage = "    В расчет на 1 пожарную сигнализацию входит:\n" +
                        "3 дюбеля (красн. 6*51)\n" +
                        "1 клеммная пара\n" +
                        "1 круглое кольцо (50)\n" +
                        "1 платформа для карнизов (70*100)\n" +
                        "3 подвеса прямых (П 60 (0,8))\n" +
                        "3 самореза (ГКД 3,5*51)\n" +
                        "6 саморезов (п/сф 3,5*9,5 цинк)\n" +
                        "+ монтжаная работа по установке пожарной сигнализации";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.add_vent_help:
                s_setMessage = "    В расчет на 1 вентиляцию входит:\n" +
                        "2 дюбеля (красн. 6*51)\n" +
                        "1 квадратная или круглая платформа\n" +
                        "4 самореза (ГКД 3,5*51)\n" +
                        "4 самореза (п/сф 3,5*9,5 цинк)\n" +
                        "1 термоквадрат или круглое кольцо\n" +
                        "В расчет на 1 электровытяжку входит:\n" +
                        "2 дюбеля (красн. 6*51)\n" +
                        "1 клеммная пара\n" +
                        "1 круглая или квадратная платформа\n" +
                        "1 круглое кольцо или термоквадрат\n" +
                        "2 подвеса прямых (П 60 (0,8))\n" +
                        "0,5м провода (ПВС 2*0,75)\n" +
                        "4 самореза (ГКД 3,5*51)\n" +
                        "4 самореза (п/сф 3,5*9,5 цинк)\n" +
                        "+ монтжаная работа по установке вытяжки";
                s_setMessage1 = "(Чтобы удалить ненужную вентиляцию, нажмите на неё в таблице)";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.add_diff_help:
                s_setMessage = "    В расчет входит 1 диффузор + монтажная работа по установке диффузора";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = String.valueOf(R.raw.diffuser);
                fun_builder();
                break;
            case R.id.color_canvases:
                intent = new Intent(getActivity(), Activity_color.class);
                intent.putExtra("texture_id", n2);
                startActivity(intent);
                break;
            case R.id.btn_rb_v:
                intent = new Intent(getActivity(), Activity_color.class);
                intent.putExtra("component_id", "15");
                startActivity(intent);
                break;

            case R.id.add_profile_help:
                s_setMessage = "    Для перехода без нишей в расчет входит 343 р. + маржа на комплектующие\n" +
                        "Для перехода с нишей в расчет входит 532 о. + маржа на комплектующие\n" +
                        "+ монтажная работа \"переход уровня с нишей или без\"";
                s_setMessage1 = "    (Чтобы удалить ненужный переход, нажмите на него в таблице)";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;

            case R.id.wall_help:
                s_setMessage = "    В расчет на 1м усиления входит:\n" +
                        "1м бруса (40*50)\n" +
                        "3 дюбеля (красн. 6*51)\n" +
                        "3 белых кронштейна (15*12,5)\n" +
                        "3 самореза (ГКД 4,2*102 окс)\n" +
                        "+ монтажная работа по усилению стен";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.fasteners_help:
                s_setMessage = "    В расчет на 1м дополнительного крепежа входит:\n" +
                        "1м багета ПВХ (2,5)\n" +
                        "10 саморезов (ГДК 3,5*51)\n" +
                        "+ монтажная работа \"дополнительный крепеж\"";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.in_cut_help:
                s_setMessage = "    В расчет на 1м внутреннего выреза на месте входит:\n" +
                        "1м багета ПВХ (2,5)\n" +
                        "1м белой вставки\n" +
                        "1м бруса (40*50)\n" +
                        "16 дюбелей(красн. 6*51)\n" +
                        "22 самореза (ГКД 3,5*41)\n" +
                        "3 белых кронштейна (15*12,5)\n" +
                        "1м гарпуна\n" +
                        "+ монтажная работа \"внутренний вырез\"";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.diff_acc_help:
                s_setMessage = "    В расчет входит монтажная работа \"сложность доступа\". Считается по метрам.";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.separator_help:
                s_setMessage = "    В расчет на 1м разделителя входит разделительный багет алюминиевый (2,5)\n" +
                        "А также:\n" +
                        "1м бруса (40*50)\n" +
                        "1м белой вставки в разделитель (гриб)\n" +
                        "3 дюбеля (красн. 6*51)\n" +
                        "20 саморезов (ГДК 3,5*41)\n" +
                        "3 самореза (ГКД 4,2*102 окс)\n" +
                        "+ монтажная работа по установке разделителя";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.soaring_ceiling_help:
                s_setMessage = "    В расчет на 1м парящего потолка входит:\n" +
                        "1м багета для парящих потолков (а из периметра вычитается 1м стенового багета)\n" +
                        "1м светопропускающей вставки для парящих потолков\n" +
                        "+ монтажная работа по установке профиля для парящих потолков";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.mount_wall_help:
                s_setMessage = "    В расчет считается добавочная стоимость на сложность крепления в плитку";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.mount_granite_help:
                s_setMessage = "    В расчет считается добавочная стоимость на сложность крепления в керамогранит";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.cabling_help:
                s_setMessage = "    В расчет входит провод (ПВС 2*0,75)\n" +
                        "А также на 1м провода:\n" +
                        "2 самореза (ГДК 3,5*51)\n" +
                        "2 дюбеля (красн. 6*51)";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.bond_beam_help:
                s_setMessage = "    В расчет на 1м бруса входит:\n" +
                        "1м бруса (40*50)\n" +
                        "6 саморезов (ГДК 3,5*51)\n" +
                        "6 дюбелей (красн. 6*51)\n" +
                        "6 саморезов (ГДК 3,5*41)\n" +
                        "3 подвеса прямых (П 60 (0,8))\n" +
                        "+ монтажная работа по установке закладной брусом";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.drain_the_water_help:
                s_setMessage = "    В расчет входит монтажная работа по сливу воды\n" +
                        "Укажите количество комнат";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.in_cut_shop_help:
                s_setMessage = "    В расчет на 1м внутреннего выреза в цеху входит:\n" +
                        "1м багета ПВХ (2,5)\n" +
                        "1м белой вставки\n" +
                        "10 дюбелей (красн. 6*51)\n" +
                        "10 саморезов (ГКД 3,5*51)\n" +
                        "4 самореза (п/сф 3,5*9,5 цинк)\n" +
                        "1 внутренний вырез";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.height_help:
                s_setMessage = "    В расчет входит добавочная стоимость на высоту помещения выше 3х метров";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.add_other_comp_help:
                s_setMessage = "    Это поле предназначено для введения непредусмотренных программной комплектующих. Вы можете произвольно написать названия " +
                        "комплектующих и их себестоимость. Программа сама сделает наценку, как и на все остальные комплектующие и выдаст введенное Вами название " +
                        "в прайсе для клиента.";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.add_other_mount_help:
                s_setMessage = "    Это поле предназначено для введения непредусмотренных программной монтажных работ. Вы можете произвольно написать названия " +
                        "монтажных работ и их себестоимость. Программа сама сделает наценку, как и на все остальные монтажных работ и выдаст введенное Вами " +
                        "название в прайсе для клиента.";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;
            case R.id.mounting_help:
                s_setMessage = "    Данная кнопка может отменить все монтажные работы";
                s_setMessage1 = "";
                s_setMessage2 = "";
                s_setdrawable = "";
                s_setdrawable1 = "";
                fun_builder();
                break;

            // показать / убрать списки
            case R.id.general_hide:
                if (general_layout.getVisibility() == View.GONE) {
                    general_layout.setVisibility(View.VISIBLE);
                } else {
                    general_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.buguette_hide:
                if (buguette_layout.getVisibility() == View.GONE) {
                    buguette_layout.setVisibility(View.VISIBLE);
                    buguette_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    buguette_layout.setVisibility(View.GONE);
                    buguette_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.vstavka_hide:
                if (vstavka_layout.getVisibility() == View.GONE) {
                    vstavka_layout.setVisibility(View.VISIBLE);
                    vstavka_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    vstavka_layout.setVisibility(View.GONE);
                    vstavka_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.lustr_hide:
                if (lustr_layout.getVisibility() == View.GONE) {
                    lustr_layout.setVisibility(View.VISIBLE);
                    lustr_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    lustr_layout.setVisibility(View.GONE);
                    lustr_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.svetiln_hide:
                if (svetiln_layout.getVisibility() == View.GONE) {
                    svetiln_layout.setVisibility(View.VISIBLE);
                    svetiln_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    svetiln_layout.setVisibility(View.GONE);
                    svetiln_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.cabling_hide:
                if (ed_cabling.getVisibility() == View.GONE) {
                    ed_cabling.setVisibility(View.VISIBLE);
                    cabling_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_cabling.setVisibility(View.GONE);
                    cabling_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.karniz_hide:
                if (karniz_layout.getVisibility() == View.GONE) {
                    karniz_layout.setVisibility(View.VISIBLE);
                    karniz_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    karniz_layout.setVisibility(View.GONE);
                    karniz_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.pipes_hide:
                if (pipes_layout.getVisibility() == View.GONE) {
                    pipes_layout.setVisibility(View.VISIBLE);
                    pipes_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    pipes_layout.setVisibility(View.GONE);
                    pipes_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.bond_beam_hide:
                if (bond_beam.getVisibility() == View.GONE) {
                    bond_beam.setVisibility(View.VISIBLE);
                    bond_beam_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    bond_beam.setVisibility(View.GONE);
                    bond_beam_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.separator_hide:
                if (ed_separator.getVisibility() == View.GONE) {
                    ed_separator.setVisibility(View.VISIBLE);
                    separator_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_separator.setVisibility(View.GONE);
                    separator_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.mount_wall_hide:
                if (mount_wall.getVisibility() == View.GONE) {
                    mount_wall.setVisibility(View.VISIBLE);
                    mount_wall_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    mount_wall.setVisibility(View.GONE);
                    mount_wall_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.mount_granite_hide:
                if (mount_granite.getVisibility() == View.GONE) {
                    mount_granite.setVisibility(View.VISIBLE);
                    mount_granite_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    mount_granite.setVisibility(View.GONE);
                    mount_granite_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.wall_hide:
                if (ed_wall.getVisibility() == View.GONE) {
                    ed_wall.setVisibility(View.VISIBLE);
                    wall_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_wall.setVisibility(View.GONE);
                    wall_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.fasteners_hide:
                if (ed_fasteners.getVisibility() == View.GONE) {
                    ed_fasteners.setVisibility(View.VISIBLE);
                    fasteners_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_fasteners.setVisibility(View.GONE);
                    fasteners_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.fire_hide:
                if (ed_fire.getVisibility() == View.GONE) {
                    ed_fire.setVisibility(View.VISIBLE);
                    fire_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_fire.setVisibility(View.GONE);
                    fire_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.add_vent_hide:
                if (add_vent_layout.getVisibility() == View.GONE) {
                    add_vent_layout.setVisibility(View.VISIBLE);
                    add_vent_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    add_vent_layout.setVisibility(View.GONE);
                    add_vent_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.diff_acc_hide:
                if (ed_diff_acc.getVisibility() == View.GONE) {
                    ed_diff_acc.setVisibility(View.VISIBLE);
                    diff_acc_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_diff_acc.setVisibility(View.GONE);
                    diff_acc_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.soaring_ceiling_hide:
                if (soaring_ceiling.getVisibility() == View.GONE) {
                    soaring_ceiling.setVisibility(View.VISIBLE);
                    soaring_ceiling_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    soaring_ceiling.setVisibility(View.GONE);
                    soaring_ceiling_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.add_profile_hide:
                if (add_profile_layout.getVisibility() == View.GONE) {
                    add_profile_layout.setVisibility(View.VISIBLE);
                    add_profile_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    add_profile_layout.setVisibility(View.GONE);
                    add_profile_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.in_cut_shop_hide:
                if (ed_in_cut_shop.getVisibility() == View.GONE) {
                    ed_in_cut_shop.setVisibility(View.VISIBLE);
                    in_cut_shop_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_in_cut_shop.setVisibility(View.GONE);
                    in_cut_shop_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.in_cut_hide:
                if (ed_in_cut.getVisibility() == View.GONE) {
                    ed_in_cut.setVisibility(View.VISIBLE);
                    in_cut_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_in_cut.setVisibility(View.GONE);
                    in_cut_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.drain_the_water_hide:
                if (ed_drain_the_water.getVisibility() == View.GONE) {
                    ed_drain_the_water.setVisibility(View.VISIBLE);
                    drain_the_water_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    ed_drain_the_water.setVisibility(View.GONE);
                    drain_the_water_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.height_hide:
                if (radios_height.getVisibility() == View.GONE) {
                    radios_height.setVisibility(View.VISIBLE);
                    height_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    radios_height.setVisibility(View.GONE);
                    height_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.add_other_comp_hide:
                if (add_other_comp_layout.getVisibility() == View.GONE) {
                    add_other_comp_layout.setVisibility(View.VISIBLE);
                    add_other_comp_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    add_other_comp_layout.setVisibility(View.GONE);
                    add_other_comp_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.add_other_mount_hide:
                if (add_other_mount_layout.getVisibility() == View.GONE) {
                    add_other_mount_layout.setVisibility(View.VISIBLE);
                    add_other_mount_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    add_other_mount_layout.setVisibility(View.GONE);
                    add_other_mount_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.add_diff_hide:
                if (add_diff_layout.getVisibility() == View.GONE) {
                    add_diff_layout.setVisibility(View.VISIBLE);
                    add_diff_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    add_diff_layout.setVisibility(View.GONE);
                    add_diff_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
            case R.id.mounting_hide:
                if (radios_mounting.getVisibility() == View.GONE) {
                    radios_mounting.setVisibility(View.VISIBLE);
                    mounting_hide.setBackgroundResource(R.drawable.rounded_button_question_2);
                } else {
                    radios_mounting.setVisibility(View.GONE);
                    mounting_hide.setBackgroundResource(R.drawable.rounded_button_question);
                }
                break;
        }
    }

    void calculation() {

        //------------------------------------- РАСЧЕТ СТОИМОСТИ КОМПЛЕКТУЮЩИХ -------------------------------------//

        SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
        if (SPSO.getString("", "").equals("")) {
            n6 = 0;
        } else {
            n6 = Integer.valueOf(SPSO.getString("", ""));
        }

        // внутренний вырез(на месте)
        if (ed_in_cut.getText().toString().equals("") || ed_in_cut.getText().toString().equals("0") || ed_in_cut.getText().toString().equals("0.0")) {
            n11 = 0;
        } else {
            n11 = Double.valueOf(ed_in_cut.getText().toString());
        }

        // внутренний вырез(в цеху)
        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0") || ed_in_cut_shop.getText().toString().equals("0.0")) {
            n31 = 0;
        } else {
            n31 = Double.valueOf(ed_in_cut_shop.getText().toString());
        }

        //люстры

        if (lustr.getText().toString().equals("") || lustr.getText().toString().equals("0") ||
                lustr.getText().toString().equals("0.0")) {
            n12 = 0;
        } else {
            n12 = Integer.parseInt(lustr.getText().toString());

        }

        // парящий потоолок
        if (soaring_ceiling.getText().toString().equals("") || soaring_ceiling.getText().toString().equals("0")) {
            n30=0;
        } else {
            n30 = Double.valueOf(soaring_ceiling.getText().toString());
        }

        //карниз
        if (karniz.getText().toString().equals("") || karniz.getText().toString().equals("0")) {
            n27=0;
        } else {
            n27 = Double.parseDouble(karniz.getText().toString());

        }

        //закладная брусом
        if (bond_beam.getText().toString().equals("") || bond_beam.getText().toString().equals("0")) {
            n17=0;
        } else {
            n17 = Double.parseDouble(bond_beam.getText().toString());
        }

        //укрепление стен
        if (ed_wall.getText().toString().equals("") || ed_wall.getText().toString().equals("0")) {
            n18=0;
        } else {
            n18 = Double.parseDouble(ed_wall.getText().toString());
        }

        // провод
        if (ed_cabling.getText().toString().equals("") || ed_cabling.getText().toString().equals("0")) {
            n19=0;
        } else {
            n19 = Double.parseDouble(ed_cabling.getText().toString());
        }

        //разделитель только для ПВХ
        if (ed_separator.getText().toString().equals("") || ed_separator.getText().toString().equals("0")) {
            n20=0;
        } else {
            if (n1.equals("28")) {
                n20 = Double.parseDouble(ed_separator.getText().toString());
            }
        }

        // дополнительный крепеж
        if (ed_fasteners.getText().toString().equals("") || ed_fasteners.getText().toString().equals("0")) {
            dop_krepezh=0;
        } else {
            dop_krepezh = Double.parseDouble(ed_fasteners.getText().toString());
        }

        // пожарная сигнализация
        if (ed_fire.getText().toString().equals("") || ed_fire.getText().toString().equals("0")) {
            n21=0;
        } else {
            n21 = Double.parseDouble(ed_fire.getText().toString());
        }

        //--------------------------------------- ВОЗВРАЩАЕМ СТОИМОСТЬ КОМПЛЕКТУЮЩИХ ----------------------------------------//

        if (ed_in_cut_shop.getText().toString().equals("") || ed_in_cut_shop.getText().toString().equals("0")) {
            n31=0;
        } else {
            n31 = Double.valueOf(ed_in_cut_shop.getText().toString());
        }

        //Слив воды
        if (ed_drain_the_water.getText().toString().equals("") || ed_drain_the_water.getText().toString().equals("0.0") ||
                ed_drain_the_water.getText().toString().equals("0")) {
            n32=0;
        } else {
            n32 = Math.ceil(Double.parseDouble(ed_drain_the_water.getText().toString()));
        }

        if (mount_wall.getText().toString().equals("") || mount_wall.getText().toString().equals("0") || mount_wall.getText().toString().equals("0.0")) {
            n7=0;
        } else {
            n7 = Double.valueOf(mount_wall.getText().toString());
        }

        if (mount_granite.getText().toString().equals("") || mount_granite.getText().toString().equals("0") || mount_granite.getText().toString().equals("0.0")) {
            n8=0;
        } else {
            n8 = Double.valueOf(mount_granite.getText().toString());
        }

        if (karniz.getText().toString().equals("")) {
            n27=0;
        } else {
            n27 = Double.valueOf(karniz.getText().toString());

        }

        if (soaring_ceiling.getText().toString().equals("")) {
            n30=0;
        } else {
            n30 = Double.parseDouble(soaring_ceiling.getText().toString());

        }

        if (ed_diff_acc.getText().toString().equals("")) {
            n24=0;
        } else {
            n24 = Double.parseDouble(ed_diff_acc.getText().toString());

        }

        Log.d("mLog",canvases + " " + texture);

        JSONObject result = HelperClass.calculation(getActivity(), dealer_id_str, colorIndex, id_calculation, canvases, texture, rb_vstavka,
                n1, n2, n3, S, P, n6, n7, n8, n9,
                n11, n12, n16, n17, n18, n19,
                n20, n21, n24, n27, n28,
                n30, n31, n32, dop_krepezh, height, offcut_square, width_final,
                final_comp, final_mount);

        String sqlQuewy;
        Cursor c;

        double canvases_sum_total = 0;
        double total_sum = 0;
        double components_sum = 0;
        double dealer_components_sum = 0;

        double total_gm_mounting = 0;
        double total_with_dealer_margin = 0;

        Log.d("mLog", String.valueOf(result));


        try {
            JSONObject project = result.getJSONObject("project");
            total_with_dealer_margin = Double.parseDouble(project.getString("total_with_dealer_margin"));
            total_gm_mounting = Double.parseDouble(project.getString("total_gm_mounting"));
            total_sum = Double.parseDouble(project.getString("total_sum"));
            dealer_components_sum = Double.parseDouble(project.getString("dealer_components_sum"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!mounting) {
            total_with_dealer_margin = 0;
            total_gm_mounting = 0;
        }

        String ts = "";

        total_sum += Math.round((dealer_components_sum + total_with_dealer_margin) * 100.0) / 100.0;
        ts = String.valueOf(total_sum);

        if (ed_discount.getText().toString().equals("")) {
            discount = 0;
        } else {
            discount = Double.parseDouble(ed_discount.getText().toString());
        }
        if (discount > 0) {
            ts = String.valueOf(Double.parseDouble(String.valueOf(Math.round(total_sum) * 100.0 / 100)) + "руб. / \n"
                    + Double.parseDouble(String.valueOf((Math.round(total_sum - (total_sum / 100 * discount)) * 100.0) / 100)));
        } else {
            discount = 0;
        }

        text_calculate.setText(ts + " руб.");


        StringBuffer sb = new StringBuffer();
        char[] chars = canvases.toCharArray();
        for (int s = 0; s < canvases.length(); s++) {
            if (chars[s] == ' ') {
                break;
            } else {
                sb.append(chars[s]);
            }
        }

        try {
            calc_data = lines_length.replaceAll("name", "");
            calc_data = calc_data.replaceAll("length", "");
            calc_data = calc_data.replaceAll("\"", "");
            calc_data = calc_data.replaceAll("]", "");
            calc_data = calc_data.replaceAll(":", "");
            calc_data = calc_data.replaceAll("\\[", "");
            calc_data = calc_data.replaceAll("\\{", "");
            calc_data = calc_data.replaceAll("\\}", "");
            int ii = 0;
            sb = new StringBuffer();
            for (int i = 0; i < calc_data.length(); ) {
                if (calc_data.startsWith(",", i) && ii == 0) {
                    sb.append("=");
                    ii++;
                } else if (calc_data.startsWith(",", i) && ii == 1) {
                    sb.append(";");
                    ii--;
                } else {
                    sb.append(calc_data.charAt(i));
                }
                i++;
            }

            calc_data = String.valueOf(sb) + ";";
        } catch (Exception e) {
        }

        SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
        String dealer_calc = SP.getString("", "");

        Log.d("mLog", "dealer_calc  _ " + dealer_calc + " _ " + user_id_int);

        if (dealer_calc.equals("true")) {
            btn_save.setVisibility(View.VISIBLE);
            name_project.setVisibility(View.VISIBLE);

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

            Log.d("mLog", "max_id_proj " + max_id_proj);

            id_project = String.valueOf(max_id_proj);
        }

        double double_dealer_components_sum = 0.0;
        double double_dealer_canvases_sum = 0.0;

        sqlQuewy = "select dealer_total, id "
                + "FROM component_item ";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    if (String.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1)))).equals("canv")) {
                        double_dealer_canvases_sum += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    } else {
                        double_dealer_components_sum += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
                    }
                } while (c.moveToNext());
            }
        }

        if (calculat) {
            try {
                SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
                color = Integer.valueOf(SPSO.getString("", ""));
            } catch (Exception e) {

            }
            try {
                SPSO = getActivity().getSharedPreferences("color_title_vs", MODE_PRIVATE);
                n6 = Integer.valueOf(SPSO.getString("", ""));
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

                Log.d("mLog", "Потолок " + count_calc);
                count_calc++;
                name_project.setText("Потолок " + count_calc);
            }
            if (offcut_square == null) {
                offcut_square = "0";
            }

            if (SAVED_KP.equals("")) {
                if (id_calculation != null) {

                }
            } else {
                original_sketch = "";
                String save = "{" + SAVED_KP.substring(1, SAVED_KP.length() - 1) + "}";
                StringBuffer sbb = new StringBuffer();
                int cou = 0;

                for (int i = 0; i < save.length(); ) {
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

                for (int i = 1; i < cou + 1; i++) {

                    cut_data += "Полотно" + i + ": ";

                    try {

                        JSONObject jsonObject = new JSONObject(save);

                        JSONArray kp = jsonObject.getJSONArray("kp" + i);

                        for (int y = 0; y < kp.length(); y++) {

                            org.json.JSONObject kp1 = kp.getJSONObject(y);

                            String name = kp1.getString("name");
                            String koordinats = kp1.getString("koordinats");

                            cut_data += " " + name + koordinats + ",";
                        }
                    } catch (Exception e) {
                    }

                    cut_data = cut_data.substring(0, cut_data.length() - 1);
                    cut_data += "; ";
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

                        original_sketch += s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e) {
                }

                original_sketch += "||";
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

                        original_sketch += s0_x + ";" + s0_y + ";" + s1_x + ";" + s1_y + ";";
                    }
                } catch (Exception e) {
                }

                original_sketch += "||";
                try {
                    save = "{pt_p:" + SAVED_PT_P + "}";

                    JSONObject jsonObject = new JSONObject(save);

                    JSONArray pt_p = jsonObject.getJSONArray("pt_p");

                    for (int y = 0; y < pt_p.length(); y++) {

                        org.json.JSONObject pt_p1 = pt_p.getJSONObject(y);

                        String x = pt_p1.getString("x");
                        String yy = pt_p1.getString("y");

                        original_sketch += x + ";" + yy + ";";
                    }
                } catch (Exception e) {
                }

                original_sketch += "||" + SAVED_CODE + "||" + SAVED_ALFAVIT;
            }

            if (calc_image.equals("")) {
                id_n3 = null;
                n2 = null;
            }

            Integer n3 = id_n3;

            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_ORDERING, 0);
            values.put(DBHelper.KEY_STATE, 1);
            values.put(DBHelper.KEY_CHECKED_OUT, 0);
            values.put(DBHelper.KEY_CHECKED_OUT_TIME, "0000-00-00 00:00:00");
            values.put(DBHelper.KEY_CREATED_BY, Integer.parseInt(user_id));
            values.put(DBHelper.KEY_MODIFIED_BY, Integer.parseInt(user_id));
            values.put(DBHelper.KEY_CALCULATION_TITLE, name_project.getText().toString());
            values.put(DBHelper.KEY_PROJECT_ID, Integer.parseInt(id_project));
            values.put(DBHelper.KEY_N3, n3);
            values.put(DBHelper.KEY_N4, S);
            values.put(DBHelper.KEY_N5, P);
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
            values.put(DBHelper.KEY_COMPONENTS_SUM, String.valueOf(Math.round((components_sum - canvases_sum_total) * 100.0) / 100.0));
            values.put(DBHelper.KEY_CANVASES_SUM, String.valueOf(Math.round(canvases_sum_total * 100.0) / 100.0));
            values.put(DBHelper.KEY_MOUNTING_SUM, (int) Math.ceil(Math.round(total_gm_mounting * 100.0) / 100.0));
            values.put(DBHelper.KEY_DEALER_CANVASES_SUM, double_dealer_canvases_sum);
            values.put(DBHelper.KEY_DEALER_COMPONENTS_SUM, double_dealer_components_sum);
            values.put(DBHelper.KEY_EXTRA_MOUNTING, final_mount);
            values.put(DBHelper.KEY_DOP_KREPEZH, dop_krepezh);
            values.put(DBHelper.KEY_COLOR, color);
            values.put(DBHelper.KEY_DETAILS, "");
            values.put(DBHelper.KEY_CALC_IMAGE, calc_image);
            values.put(DBHelper.KEY_CALC_DATA, calc_data);
            values.put(DBHelper.KEY_CALC_POINT, "");
            values.put(DBHelper.KEY_CUT_IMAGE, cut_image);
            values.put(DBHelper.KEY_CUT_DATA, cut_data);
            values.put(DBHelper.KEY_OFFCUT_SQUARE, offcut_square);
            values.put(DBHelper.KEY_ORIGINAL_SKETCH, original_sketch);
            values.put(DBHelper.KEY_DISCOUNT, discount);
            values.put(DBHelper.KEY_HEIGHT, height);
            values.put(DBHelper.KEY_EXTRA_COMPONENTS, final_comp);
            values.put(DBHelper.KEY_EXTRA_MOUNTING, final_mount);

            if (chertezh_bool) {
                int max_id_contac = 0;
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
                values.put(DBHelper.KEY_ID, max_id_contac);
                db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);
                id_calculation = String.valueOf(max_id_contac);
                chertezh_bool = false;

            } else {
                int max_id_contac = 0;
                //id_calculation = getActivity().getIntent().getStringExtra("id_calculation");  // id_calculation
                if (id_calculation != null) {

                    if (dealer_calc.equals("true")) {

                        values.put(DBHelper.KEY_ID, id_calculation);
                        db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);

                    } else {
                        db.update(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, values, "_id = ?", new String[]{id_calculation});
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Расчёт обновлён ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
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

                    values.put(DBHelper.KEY_ID, max_id_contac);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CALCULATIONS, null, values);

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

                Log.d("mLog", "dealer_calc " + dealer_calc);
                delete_comp = false;
                if (dealer_calc.equals("true")) {

                    int max_id = 0;
                    try {
                        sqlQuewy = "select MAX(_id) "
                                + "FROM rgzbn_gm_ceiling_clients " +
                                "where _id>? and _id<?";
                        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id_int), String.valueOf(user_id_int + 999999)});
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    max_id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
                                    max_id++;
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Exception e) {
                        max_id = user_id_int + 1;
                    }

                    Calendar date_cr = new GregorianCalendar();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
                    String date = df.format(date_cr.getTime());

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID, max_id);
                    values.put(DBHelper.KEY_CLIENT_NAME, max_id);
                    values.put(DBHelper.KEY_CLIENT_DATA_ID, "");
                    values.put(DBHelper.KEY_TYPE_ID, "1");
                    values.put(DBHelper.KEY_DEALER_ID, user_id);
                    values.put(DBHelper.KEY_MANAGER_ID, "");
                    values.put(DBHelper.KEY_CREATED, date);
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_CLIENTS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, max_id);
                    values.put(DBHelper.KEY_ID_NEW, 0);
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_clients");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    String dealer_canvases_margin = "";
                    String dealer_components_margin = "";
                    String dealer_mounting_margin = "";
                    String gm_canvases_margin = "";
                    String gm_components_margin = "";
                    String gm_mounting_margin = "";

                    sqlQuewy = "select dealer_canvases_margin, dealer_components_margin, dealer_mounting_margin, gm_canvases_margin, " +
                            "gm_components_margin, gm_mounting_margin "
                            + "FROM rgzbn_gm_ceiling_dealer_info " +
                            "where dealer_id = ?";
                    c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(user_id)});
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                dealer_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(0)));
                                dealer_components_margin = c.getString(c.getColumnIndex(c.getColumnName(1)));
                                dealer_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(2)));
                                gm_canvases_margin = c.getString(c.getColumnIndex(c.getColumnName(3)));
                                gm_components_margin = c.getString(c.getColumnIndex(c.getColumnName(4)));
                                gm_mounting_margin = c.getString(c.getColumnIndex(c.getColumnName(5)));
                            } while (c.moveToNext());
                        }
                    }

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID, id_project);
                    values.put(DBHelper.KEY_STATE, "1");
                    values.put(DBHelper.KEY_CLIENT_ID, max_id);
                    values.put(DBHelper.KEY_PROJECT_INFO, "-");
                    values.put(DBHelper.KEY_PROJECT_CALCULATION_DATE, date);
                    values.put(DBHelper.KEY_PROJECT_CALCULATOR, user_id);
                    values.put(DBHelper.KEY_PROJECT_MOUNTING_DATE, "0000-00-00 00:00:00");
                    values.put(DBHelper.KEY_PROJECT_MOUNTING_START, "0000-00-00 00:00:00");
                    values.put(DBHelper.KEY_PROJECT_MOUNTING_END, "0000-00-00 00:00:00");
                    values.put(DBHelper.KEY_PROJECT_MOUNTER, "null");
                    values.put(DBHelper.KEY_CREATED, date);
                    values.put(DBHelper.KEY_CREATED_BY, user_id);
                    values.put(DBHelper.KEY_MODIFIED_BY, user_id);
                    values.put(DBHelper.KEY_PROJECT_STATUS, "1");
                    values.put(DBHelper.KEY_WHO_CALCULATE, user_id);
                    values.put(DBHelper.KEY_GM_CANVASES_MARGIN, gm_canvases_margin);
                    values.put(DBHelper.KEY_GM_COMPONENTS_MARGIN, gm_components_margin);
                    values.put(DBHelper.KEY_GM_MOUNTING_MARGIN, gm_mounting_margin);
                    values.put(DBHelper.KEY_DEALER_CANVASES_MARGIN, dealer_canvases_margin);
                    values.put(DBHelper.KEY_DEALER_COMPONENTS_MARGIN, dealer_components_margin);
                    values.put(DBHelper.KEY_DEALER_MOUNTING_MARGIN, dealer_mounting_margin);
                    values.put(DBHelper.KEY_PROJECT_NOTE, "");
                    values.put(DBHelper.KEY_TRANSPORT, "1");
                    values.put(DBHelper.KEY_DISTANCE, "0");
                    values.put(DBHelper.KEY_DISTANCE_COL, "1");
                    db.insert(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, null, values);

                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, id_project);
                    values.put(DBHelper.KEY_ID_NEW, 0);
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_projects");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                    getActivity().startService(new Intent(getActivity(), Service_Sync.class));
                    sync(max_id_contac);

                    SP = getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", String.valueOf(id_project));
                    ed.commit();

                    Intent intent = new Intent(getActivity(), Activity_inform_proj.class);
                    startActivity(intent);

                    SP = getActivity().getSharedPreferences("dealer_calc", MODE_PRIVATE);
                    ed = SP.edit();
                    ed.putString("", "");
                    ed.commit();

                } else {
                    sync(max_id_contac);
                    getActivity().finish();
                }
            }
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
                    values.put(DBHelper.KEY_ID_OLD, dif);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_diffusers");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
                    values.put(DBHelper.KEY_ID_OLD, prof);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_profil");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
                    values.put(DBHelper.KEY_ID_OLD, ecola);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_ecola");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
                    values.put(DBHelper.KEY_ID_OLD, fixtures);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_fixtures");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
                    values.put(DBHelper.KEY_ID_OLD, hoods);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_hoods");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
                    values.put(DBHelper.KEY_ID_OLD, pipes);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_pipes");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
                } while (c.moveToNext());
            }
        }

        sqlQuewy = "select _id "
                + "FROM rgzbn_gm_ceiling_cornice " +
                "where calculation_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String pipes = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    values = new ContentValues();
                    values.put(DBHelper.KEY_ID_OLD, pipes);
                    values.put(DBHelper.KEY_ID_NEW, "0");
                    values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_gm_ceiling_cornice");
                    values.put(DBHelper.KEY_SYNC, "0");
                    values.put(DBHelper.KEY_TYPE, "send");
                    values.put(DBHelper.KEY_STATUS, "1");
                    db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);
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
        TextView txt = (TextView) dialogView.findViewById(R.id.text_dialog);
        txt.setText(s_setMessage);
        TextView txt1 = (TextView) dialogView.findViewById(R.id.text_dialog1);
        txt1.setText(s_setMessage1);
        TextView txt2 = (TextView) dialogView.findViewById(R.id.text_dialog2);
        txt2.setText(s_setMessage2);
        if (s_setMessage1.equals("")) {
            txt1.setVisibility(View.GONE);
        }
        if (s_setMessage2.equals("")) {
            txt2.setVisibility(View.GONE);
        }
        if (s_setdrawable.equals("")) {
        } else {
            ImageView image = (ImageView) dialogView.findViewById(R.id.view_dialog);
            image.setImageResource(Integer.parseInt(s_setdrawable));
        }
        if (s_setdrawable1.equals("")) {
        } else {
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

        if (id_calculation == null || id_project == null) {
            spinner1.setSelection(0);
        } else {
            try {
                sqlQuewy = "select texture_title "
                        + "FROM rgzbn_gm_ceiling_textures " +
                        "where _id=?";

                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n2)});

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
                    if (c.moveToFirst()) {
                        colorIndex = c.getColumnIndex(DBHelper.KEY_TEXTURE_COLORED);
                        do {
                            if (c.getInt(colorIndex) == 1) {
                                btn_color_canvases.setVisibility(View.VISIBLE);
                                btn_color_canvases.setEnabled(true);                                    // активность кнопки чертить
                                btn_color_canvases.setBackgroundResource(R.drawable.rounded_button);    // активность кнопки чертить

                                SPSO = getActivity().getSharedPreferences("color_title", MODE_PRIVATE);
                                color = Integer.parseInt(SPSO.getString("", ""));

                                sqlQuewy = "select hex "
                                        + "FROM rgzbn_gm_ceiling_colors " +
                                        "where title = ? ";
                                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(color)});
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
                    ed.putString("", "0");
                    ed.commit();
                }

                int idIndex = 0;

                c = db.query(DBHelper.TABLE_RGZBN_GM_CEILING_TEXTURES, null, "texture_title = ?",
                        new String[]{texture}, null, null, null);

                if (c.moveToFirst()) {
                    idIndex = c.getColumnIndex(DBHelper.KEY_ID);
                    do {
                        n2 = c.getInt(idIndex);
                        break;
                    } while (c.moveToNext());
                }
                c.close();

                String item_content2 = null;
                String item_content3 = null;
                s_t.clear();

                String groupBy2 = "manufacturer_id";
                c = db.query("rgzbn_gm_ceiling_canvases", null, "texture_id = ? ",
                        new String[]{String.valueOf(n2)}, groupBy2, null, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            for (String cn : c.getColumnNames()) {
                                int id = c.getInt(c.getColumnIndex(c.getColumnName(3)));
                                Log.d("mLog", String.valueOf(id));
                                //item_content3 = c.getString(c.getColumnIndex(c.getColumnName(4)));
                                //item_content2 += " " + item_content3;

                                sqlQuewy = "select name, country "
                                        + "FROM rgzbn_gm_ceiling_canvases_manufacturers " +
                                        "where _id = ?";
                                Cursor k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(id)});
                                if (k != null) {
                                    if (k.moveToFirst()) {
                                        do {
                                            Log.d("mLog", String.valueOf(item_content2));
                                            item_content2 = c.getString(c.getColumnIndex(c.getColumnName(1))) + " "
                                                    + c.getInt(c.getColumnIndex(c.getColumnName(2)));

                                        } while (k.moveToNext());
                                    }
                                }
                                k.close();
                                s_t.add(item_content2);
                            }

                            //s_t.add(item_content2);
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
                            "where _id = ?";

                    c = db.rawQuery(sqlQuewy, new String[]{n3});

                    String item_content1 = "";

                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                item_content1 = c.getString(c.getColumnIndex(c.getColumnName(0))) + " "
                                        + c.getString(c.getColumnIndex(c.getColumnName(1)));
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

                        String sqlQuewy = "select * " +
                                " FROM rgzbn_gm_ceiling_canvases " +
                                " where texture_id = ? " +
                                " ORDER BY width DESC";

                        SQLiteDatabase db;
                        db = dbHelper.getWritableDatabase();
                        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n2)});

                        String str = "[";

                        if (c.moveToFirst()) {
                            int countryIndex = c.getColumnIndex(DBHelper.KEY_MANUFACTURER_ID);
                            int priceIndex = c.getColumnIndex(DBHelper.KEY_PRICE);
                            int widthIndex = c.getColumnIndex(DBHelper.KEY_WIDTH);
                            do {
                                String nc = c.getString(countryIndex);

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

                        c.close();

                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(SAVED_TEXT, String.valueOf(strr)); // передача выбранных полотен на чертилку
                        ed.commit();

                        SP = getActivity().getSharedPreferences("textures_draft", MODE_PRIVATE);
                        ed = SP.edit();
                        ed.putString("", String.valueOf(n2));
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