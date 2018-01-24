package ru.ejevikaapp.authorization.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_inform_zapysch extends Fragment {

    String SAVED_ID="", phone, fio, pro_info, calc_date, dealer_id, imag, id_calc;

    View view;

    private int pageNumber;

    ImageView image, id_polotna;

    DBHelper dbHelper;

    LinearLayout.LayoutParams textViewParams, titleViewParams, imageViewParams;
    LinearLayout mainL;

    TextView id_cl, id_pagenumber;

    public static Fragment_inform_zapysch newInstance(int page) {
        Fragment_inform_zapysch fragment = new Fragment_inform_zapysch();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inform_zapysch, container, false);

        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;

        mainL = (LinearLayout) view.findViewById(R.id.linear_main);

        textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        titleViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleViewParams.setMargins(0,15,0,0);

        id_cl = (TextView)view.findViewById(R.id.id_cl);
        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_cl", MODE_PRIVATE);
        id_calc = SPI.getString(SAVED_ID,"");
        id_cl.setText(id_calc);

        id_pagenumber = (TextView)view.findViewById(R.id.id_pagenumber);
        id_pagenumber.setText(String.valueOf(pageNumber));

        image = (ImageView)view.findViewById(R.id.id_image);
        id_polotna = (ImageView)view.findViewById(R.id.id_polotna);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT calc_image "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    imag = c.getString(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        StringBuffer sb = new StringBuffer(imag.subSequence(0, imag.length()));
        sb.delete(0, 22);

        if (imag.length() < 30) {
        } else fromBase64(sb.toString()); // декодируем текст в картинку

        sqlQuewy = "SELECT n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n16, n17, n18, n19, n20, n21, n24, n25, dop_krepezh, " +
                "n27, color, n28, n30 "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String n1 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    String n2 = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    String n3 = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    sqlQuewy = "SELECT texture_title "
                            + "FROM rgzbn_gm_ceiling_textures" +
                            " WHERE _id = ?";

                    Cursor k = db.rawQuery(sqlQuewy, new String[]{n1});

                    if (k != null) {
                        if (k.moveToFirst()) {
                            do {
                                String title = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                TextView mat_1 = (TextView)view.findViewById(R.id.mat_1);
                                mat_1.setText(mat_1.getText()+ "  " + title);
                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT  texture_title "
                            + "FROM rgzbn_gm_ceiling_textures" +
                            " WHERE _id = ?";

                    k = db.rawQuery(sqlQuewy, new String[]{n2});

                    if (k != null) {
                        if (k.moveToFirst()) {
                            do {
                                String title = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                TextView mat_2 = (TextView)view.findViewById(R.id.mat_2);
                                mat_2.setText(mat_2.getText()+ "  " + title);
                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT name, country, width "
                            + "FROM rgzbn_gm_ceiling_canvases" +
                            " WHERE _id = ?";

                    k = db.rawQuery(sqlQuewy, new String[]{n3});

                    if (k != null) {
                        if (k.moveToFirst()) {
                            do {
                                String title = k.getString(k.getColumnIndex(k.getColumnName(0))) + " " +
                                        k.getString(k.getColumnIndex(k.getColumnName(1))) + " " +k.getString(k.getColumnIndex(k.getColumnName(2)));

                                TextView mat_3 = (TextView)view.findViewById(R.id.mat_3);
                                mat_3.setText(mat_3.getText()+ "  " + title);
                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    String color = c.getString(c.getColumnIndex(c.getColumnName(22)));
                    sqlQuewy = "SELECT hex "
                            + "FROM rgzbn_gm_ceiling_colors" +
                            " WHERE _id = ?";

                    k = db.rawQuery(sqlQuewy, new String[]{color});

                    if (k != null) {
                        if (k.moveToFirst()) {
                            do {
                                LinearLayout color_lin = (LinearLayout)view.findViewById(R.id.color_lin);
                                color_lin.setVisibility(View.VISIBLE);
                                String hex = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                id_polotna.setBackgroundColor(Color.parseColor("#"+hex));

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    String n4 = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    TextView raz_1 = (TextView)view.findViewById(R.id.raz_1);
                    raz_1.setText(raz_1.getText()+ "  " + n4);

                    String n5 = c.getString(c.getColumnIndex(c.getColumnName(4)));
                    TextView raz_2 = (TextView)view.findViewById(R.id.raz_2);
                    raz_2.setText(raz_2.getText()+ "  " + n5);

                    String n28 = c.getString(c.getColumnIndex(c.getColumnName(23)));
                    if (n28 == null){
                        n28="0";
                    }

                    if (n28.equals("0")){
                        textt("Багет");
                        textv("Обычный багет");
                    } else
                    if (n28.equals("1")){
                        textt("Багет");
                        textv("Потолочный багет");
                    } else
                    if (n28.equals("2")){
                        textt("Багет");
                        textv("Аллюминиевый багет");
                    }

                    String n6 = c.getString(c.getColumnIndex(c.getColumnName(5)));
                    try {
                        if (n6.equals("314")) {
                            textt("Вставка");
                            textv("Белая вставка");
                        } else
                            try {
                                if (Integer.parseInt(n6) != 314 && Integer.parseInt(n6) != 0) {
                                    textt("Вставка");
                                    String hex = "";
                                    sqlQuewy = "SELECT title, hex "
                                            + "FROM rgzbn_gm_ceiling_colors" +
                                            " WHERE title = ? ";
                                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{n6});
                                    if (cc != null) {
                                        if (cc.moveToFirst()) {
                                            Log.d("mLog color2", n6);
                                            String title = cc.getString(cc.getColumnIndex(cc.getColumnName(0)));
                                            hex = cc.getString(cc.getColumnIndex(cc.getColumnName(1)));
                                            textv("Цветная " + title);
                                        }
                                    }
                                    cc.close();
                                    ImageView im = new ImageView(getActivity());
                                    imageViewParams.height = 200;
                                    imageViewParams.width = 300;
                                    im.setLayoutParams(imageViewParams);
                                    im.setBackgroundColor(Color.parseColor("#"+hex));
                                    mainL.addView(im);
                                }
                            } catch (Exception e) {

                            }
                    }catch (Exception e){

                    }

                    String n12 = c.getString(c.getColumnIndex(c.getColumnName(11)));
                    if (Integer.valueOf(n12) > 0){
                        textt("Установка люстры");
                        n12 = n12 + " шт.";
                        textv(n12);
                    }

                    sqlQuewy = "SELECT n13_count, n13_type, n13_size "
                            + "FROM rgzbn_gm_ceiling_fixtures" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Установка светильников");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт.  - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_type" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Тип: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) + "  - ";
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                str = k.getString(c.getColumnIndex(c.getColumnName(2)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Размер: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0)));
                                        } while (k1.moveToNext());
                                    }
                                }

                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT n14_count, n14_size "
                            + "FROM rgzbn_gm_ceiling_pipes" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Обвод трубы");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт. - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Тип: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) ;
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    String n27 = c.getString(c.getColumnIndex(c.getColumnName(21)));
                    if (Double.valueOf(n27) > 0){
                        textt("Шторный карниз");
                        n27 = n27 + " м.";
                        textv(n27);
                    }

                    sqlQuewy = "SELECT n15_count, n15_type, n15_size "
                            + "FROM rgzbn_gm_ceiling_cornice" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Шторный карниз Гильдии мастеров");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт.  - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));
                                Log.d("mLog", str);

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_type" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Тип: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) + "  - ";
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                str = k.getString(c.getColumnIndex(c.getColumnName(2)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Длина: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0)));
                                        } while (k1.moveToNext());
                                    }
                                }

                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT n26_count, n26_illuminator, n26_lamp "
                            + "FROM rgzbn_gm_ceiling_ecola" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Светильники Эcola");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт.  - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Тип: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) + "  - ";
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                str = k.getString(c.getColumnIndex(c.getColumnName(2)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Лампа: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0)));
                                        } while (k1.moveToNext());
                                    }
                                }

                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT n22_count, n22_type, n22_size "
                            + "FROM rgzbn_gm_ceiling_hoods" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Вентиляция");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт.  - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Тип: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) + "  - ";
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                str = k.getString(c.getColumnIndex(c.getColumnName(2)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_type" +
                                        " WHERE _id = ?";
                                k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Размер: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0)));
                                        } while (k1.moveToNext());
                                    }
                                }

                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    sqlQuewy = "SELECT n23_count, n23_size "
                            + "FROM rgzbn_gm_ceiling_diffusers" +
                            " WHERE calculation_id = ?";
                    k = db.rawQuery(sqlQuewy, new String[]{String.valueOf(pageNumber)});
                    if (k != null) {

                        if (k.moveToFirst()) {
                            textt("Обвод трубы");
                            do {

                                String str = k.getString(k.getColumnIndex(k.getColumnName(0)));
                                String title = "Количество: " + str + " шт. - ";

                                str = k.getString(k.getColumnIndex(k.getColumnName(1)));

                                sqlQuewy = "SELECT title "
                                        + "FROM rgzbn_gm_ceiling_components_option" +
                                        " WHERE _id = ?";
                                Cursor k1 = db.rawQuery(sqlQuewy, new String[]{str});
                                if (k1 != null) {
                                    if (k1.moveToFirst()) {
                                        do {
                                            title += " Размер: " + k1.getString(k1.getColumnIndex(k1.getColumnName(0))) ;
                                        } while (k1.moveToNext());
                                    }
                                }
                                k1.close();

                                textv(title);

                            } while (k.moveToNext());
                        }
                    }
                    k.close();

                    if ((Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(6)))) > 0) || (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(7)))) > 0) ||
                            (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(8)))) > 0) ||(Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(9)))) > 0) ||
                            (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(10)))) > 0) ||(Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(15)))) > 0) ||
                            (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(16)))) > 0) ||(Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(17)))) > 0) ||
                            (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(18)))) > 0) ||(Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(20)))) > 0) ||
                    (Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(23)))) > 0))
                    {
                        textt("Прочее");

                        String n7 = c.getString(c.getColumnIndex(c.getColumnName(6)));
                        if (Double.valueOf(n7) > 0){
                            n7 = "Крепление в плитку, м: " + n7;
                            textv(n7);
                        }
                        String n8 = c.getString(c.getColumnIndex(c.getColumnName(7)));
                        if (Double.valueOf(n8) > 0){
                            n8 = "Крепление в керамогранит, м: " + n8;
                            textv(n8);
                        }
                        String n9 = c.getString(c.getColumnIndex(c.getColumnName(8)));
                        if (Double.valueOf(n9) > 0){
                            n9 = "Углы, м: " + n9;
                            textv(n9);
                        }
                        String n10 = c.getString(c.getColumnIndex(c.getColumnName(9)));
                        if (Double.valueOf(n10) > 0){
                            n10 = "Криволинейный вырез, м: " + n10;
                            textv(n10);
                        }
                        String n19 = c.getString(c.getColumnIndex(c.getColumnName(15)));
                        if (Double.valueOf(n19) > 0){
                            n19 = "Провод, м: " + n19;
                            textv(n19);
                        }

                        String n30 = c.getString(c.getColumnIndex(c.getColumnName(24)));
                        if (n30 == null ){
                            n30 = "0";
                        }

                        if (Double.valueOf(n30) > 0){
                            n30  = "Парящий потолок, м: " + n30 ;
                            textv(n30 );
                        }

                        String n20 = c.getString(c.getColumnIndex(c.getColumnName(16)));
                        if (Double.valueOf(n20 ) > 0){
                            n20  = "Разделитель, м: " + n20 ;
                            textv(n20 );
                        }
                        String n21 = c.getString(c.getColumnIndex(c.getColumnName(17)));
                        if (Double.valueOf(n21 ) > 0){
                            n21  = "Пожарная сигнализация, м: " + n21 ;
                            textv(n21 );
                        }
                        String n24 = c.getString(c.getColumnIndex(c.getColumnName(18)));
                        if (Double.valueOf(n24) > 0){
                            n24 = "Сложность доступа к месту монтажа, м: " + n24;
                            textv(n24);
                        }
                        String dop_krep = c.getString(c.getColumnIndex(c.getColumnName(20)));
                        if (Double.valueOf(dop_krep) > 0){
                            dop_krep = "Дополнительный крепеж: " + dop_krep;
                            textv(dop_krep);
                        }

                    }

                } while (c.moveToNext());
            }
        }
        c.close();

        return view;
    }

    void textt(String text){

        TextView txtV = new TextView(getActivity());
        txtV.setText(text);
        txtV.setLayoutParams(titleViewParams);
        txtV.setTextColor(Color.parseColor("#414099"));
        txtV.setTypeface(null, Typeface.BOLD);
        mainL.addView(txtV);
    }

    void textv(String text){

        TextView txtV = new TextView(getActivity());
        txtV.setText(text);
        txtV.setLayoutParams(textViewParams);
        txtV.setTextColor(Color.parseColor("#414099"));
        mainL.addView(txtV);
    }

    public void fromBase64(String imag) {

        byte[] decodedString = Base64.decode(imag, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Bitmap bmHalf = Bitmap.createScaledBitmap(decodedByte, decodedByte.getWidth(),
                decodedByte.getHeight(), false);

        image.setImageBitmap(bmHalf);
    }

}