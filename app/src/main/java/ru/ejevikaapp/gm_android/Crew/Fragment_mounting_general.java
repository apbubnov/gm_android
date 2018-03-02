package ru.ejevikaapp.gm_android.Crew;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Class.Frag_client_schedule_class;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_mounting_general extends Fragment {

    ListView list_general;
    String id_project, dealer_id;

    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    View view;

    double transport, distance, distance_col, dop_krepezh, mounting_sum; // из бд
    double count_transport, count_dop_krepezh, count_mounting_sum; // сумма

    ArrayList calc_array = new ArrayList();
    ArrayList<Double> n = new ArrayList();
    ArrayList<Integer> results = new ArrayList();
    ArrayList<Integer> price = new ArrayList();
    ArrayList<Double> count_price = new ArrayList();

    String[] mp_title = {
            "Периметр",
            "Люстра планочная",
            "Люстра большая",
            "Светильники",
            "Светильники квадратные",
            "Пожарная сигнализация",
            "Обвод трубы D > 120мм",
            "Обвод трубы D < 120мм",
            "брус\\разделит; брус\\отбойник",
            "Вставка",
            "Шторный карниз на полотно",
            "Установка вытяжки",
            "Крепление в плитку",
            "Крепление в керамогранит",
            "Усиление стен",
            "Установка вентиляции",
            "Сложность доступа",
            "Дополнительный крепеж",
            "Установка диффузора",
            "Обработка 1 угла",
            "Криволинейный участок для ПВХ",
            "Внутренний вырез для ПВХ",
            "Переход уровня по прямой",
            "Переход уровня по кривой",
            "Переход уровня по прямой с нишей",
            "Переход уровня по кривой с нишей",
            "Слив воды",
            "",
            "",
            "Парящий потолок",
            "Периметр, если выбран потолочный багет",
            "Периметр, если выбран алюминиевый багет",
            "Периметр (ткань)",
            "Люстра планочная (ткань)",
            "Люстра большая (ткань)",
            "Светильники (ткань)",
            "Светильники квадратные (ткань)",
            "Пожарная сигнализация (ткань)",
            "Обвод трубы D > 120мм (ткань)",
            "Обвод трубы D < 120мм (ткань)",
            "Шторный карниз на полотно (ткань)",
            "Установка вентиляции (ткань)",
            "Обработка каждого угла (ткань)"
    };

    public Fragment_mounting_general() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment_mounting_info, container, false);

        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_project_spisok", MODE_PRIVATE);
        id_project = SPI.getString("", "");

        SPI = this.getActivity().getSharedPreferences("dealer_id", MODE_PRIVATE);
        dealer_id = SPI.getString("", "");

        list_general = (ListView) view.findViewById(R.id.list_general);

        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_project});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    calc_array.add(id);
                } while (c.moveToNext());
            }
        }
        c.close();

        for (int i = 0; i < 43; i++) { // mp
            results.add(0);
        }

        for (int i = 0; i < 32; i++) {
            n.add(0.0);
        }

        for (int i = 0; i < 43; i++) { // стоимость
            price.add(0);
        }

        for (int i = 0; i < 43; i++) { // стоимость * кол-во
            count_price.add(0.0);
        }

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_mount " +
                "where user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id});         // заполняем массивы из таблицы
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

        sqlQuewy = "select transport, distance, distance_col "
                + "FROM rgzbn_gm_ceiling_projects " +
                "where _id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_project});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                transport = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
                distance = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(1))));
                distance_col = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(2))));
            }
        }
        c.close();

        for (int i = 0; i < calc_array.size(); i++) {

            sqlQuewy = "SELECT n1, n5, n6, n7, n8, n9, n11, n12, n17, n18, n20, n21, n24, " +
                    "n27, n28, n30, mounting_sum, dop_krepezh "
                    + "FROM rgzbn_gm_ceiling_calculations" +
                    " WHERE _id = ?";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        n.set(0, c.getDouble(c.getColumnIndex(c.getColumnName(0))));
                        n.set(4, c.getDouble(c.getColumnIndex(c.getColumnName(1))));
                        n.set(5, c.getDouble(c.getColumnIndex(c.getColumnName(2))));
                        n.set(6, c.getDouble(c.getColumnIndex(c.getColumnName(3))));
                        n.set(7, c.getDouble(c.getColumnIndex(c.getColumnName(4))));
                        n.set(8, c.getDouble(c.getColumnIndex(c.getColumnName(5))));
                        n.set(10, c.getDouble(c.getColumnIndex(c.getColumnName(6))));
                        n.set(11, c.getDouble(c.getColumnIndex(c.getColumnName(7))));
                        n.set(16, c.getDouble(c.getColumnIndex(c.getColumnName(8))));
                        n.set(17, c.getDouble(c.getColumnIndex(c.getColumnName(9))));
                        n.set(19, c.getDouble(c.getColumnIndex(c.getColumnName(10))));
                        n.set(20, c.getDouble(c.getColumnIndex(c.getColumnName(11))));
                        n.set(23, c.getDouble(c.getColumnIndex(c.getColumnName(12))));
                        n.set(26, c.getDouble(c.getColumnIndex(c.getColumnName(13))));
                        n.set(27, c.getDouble(c.getColumnIndex(c.getColumnName(14))));
                        n.set(29, c.getDouble(c.getColumnIndex(c.getColumnName(15))));
                        mounting_sum = c.getDouble(c.getColumnIndex(c.getColumnName(16)));
                        dop_krepezh = c.getDouble(c.getColumnIndex(c.getColumnName(17)));

                    } while (c.moveToNext());
                }
            }
            c.close();

            if (n.get(0) == 28) {
                if (n.get(27) == 0) {
                    price.set(4, results.get(0));
                }
                if (n.get(27) == 1) {
                    price.set(4, results.get(30));
                }
                if (n.get(27) == 2) {
                    price.set(4, results.get(31));
                }

                count_price.set(0, price.get(4) * n.get(4));

                if (n.get(5) > 0) {
                    count_price.set(20, n.get(4) * results.get(9));
                } else {
                    count_price.set(20, 0.0);
                }

                count_price.set(21, n.get(10) * results.get(21));
                count_price.set(1, n.get(11) * price.get(1));
                count_price.set(22, n.get(16) * price.get(22));
                count_price.set(8, n.get(19) * price.get(8));
                count_price.set(9, n.get(20) * price.get(9));
                count_price.set(10, n.get(26) * price.get(10));
                count_price.set(29, n.get(29) * price.get(29));
            } else if (n.get(0) == 29) {
                count_price.set(32, n.get(4) * results.get(32));
                count_price.set(42, n.get(9) * price.get(42));
                count_price.set(32, n.get(10) * price.get(32));
                count_price.set(33, n.get(11) * price.get(33));
                count_price.set(40, n.get(16) * price.get(40));
                count_price.set(37, n.get(20) * price.get(37));
                count_price.set(40, n.get(26) * price.get(40));
            }

            count_price.set(12, n.get(6) * results.get(12));
            count_price.set(13, n.get(7) * results.get(13));
            count_price.set(14, n.get(17) * results.get(14));
            count_price.set(16, n.get(23) * results.get(16));
            count_dop_krepezh = dop_krepezh * results.get(17);

            //int n13_count =0;
            //sqlQuewy = "SELECT n13_count, n13_type, n13_size "
            //        + "FROM rgzbn_gm_ceiling_fixtures" +
            //        " WHERE calculation_id = ?";
            //c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            //if (c != null) {

            //    if (c.moveToFirst()) {
            //        do {
            //            n13_count += Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
            //        } while (c.moveToNext());
            //    }
            //}
            //c.close();

            //if (n.get(0)==28) {
            //    int price = Math.max(results.get(3), results.get(4));
            //    count_price.set(12, Double.valueOf(price * n13_count));
            //} else if (n.get(0)==29) {
            //    int price = Math.max(results.get(35), results.get(36));
            //    count_price.set(12, Double.valueOf(price * n13_count));
            //}

            //int n14_count =0;
            //sqlQuewy = "SELECT n14_count, n14_size "
            //        + "FROM rgzbn_gm_ceiling_pipes" +
            //        " WHERE calculation_id = ?";
            //c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            //if (c != null) {
            //    if (c.moveToFirst()) {
            //        do {
            //            n14_count = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
            //            int n14_size = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));

            //            if (n.get(0) == 28) {
            //                if (n14_size > 120) {
            //                    int price = results.get(6);
            //                    count_price.set(13, Double.valueOf(price * n14_count));
            //                } else if (n14_size < 120) {
            //                    int price = results.get(7);
            //                    count_price.set(13, Double.valueOf(price * n14_count));
            //                }
            //            } else if (n.get(0) == 29) {
            //                if (n14_size > 120) {
            //                    int price = results.get(38);
            //                    count_price.set(13, Double.valueOf(price * n14_count));
            //                } else if (n14_size < 120) {
            //                    int price = results.get(39);
            //                    count_price.set(13, Double.valueOf(price * n14_count));
            //                }
            //            }
            //        } while (c.moveToNext());
            //    }
            //}
            //c.close();

            //int n22_count =0;                                  // вентиляция
            //sqlQuewy = "SELECT n22_count, n22_type "
            //        + "FROM rgzbn_gm_ceiling_hoods" +
            //        " WHERE calculation_id = ?";
            //c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            //if (c != null) {
            //    if (c.moveToFirst()) {
            //        do {
            //            n22_count = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
            //            int n22_type = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));

            //            int price_56;
            //            if (n.get(0) == 28) {
            //                price_56 = results.get(11);
            //            } else if (n.get(0) == 29) {
            //                price_56 = results.get(41);
            //            }
            //            if (n22_type == 5 || n22_type == 6){

            //            } else if (n22_type == 7 || n22_type == 8){

            //            }

            //        } while (c.moveToNext());
            //    }
            //}
            //c.close();

            //int n23_count = 0;                                                   // диффузор
            //sqlQuewy = "SELECT n23_count "
            //        + "FROM rgzbn_gm_ceiling_diffusers" +
            //        " WHERE calculation_id = ?";
            //c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            //if (c != null) {
            //    if (c.moveToFirst()) {
            //        do {
            //            n22_count += Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
            //        } while (c.moveToNext());
            //    }
            //}
            //c.close();

            //int price = results.get(18);
            //count_price.set(22, Double.valueOf(price * n22_count));

            //if (transport == 1){
            //    count_transport = distance_col * distance;
            //} else if (transport == 2){
            //    count_transport = distance_col * distance;
            //}

            //int n29_count =0;
            //sqlQuewy = "SELECT n29_count, n29_type "
            //        + "FROM rgzbn_gm_ceiling_profil" +
            //        " WHERE calculation_id = ?";
            //c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(calc_array.get(i))});
            //if (c != null) {
            //    if (c.moveToFirst()) {
            //        do {
            //            n29_count = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
            //            int n29_type = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));

            //            if (n.get(0) == 28) {
            //                if (n29_type == 13){

            //                } else if(n29_type == 16){

            //                }
            //            }

            //        } while (c.moveToNext());
            //    }
            //}
            //c.close();

            // $value["n11"] == n
            // $price_n11 == price
            // $calc_n11 == count_price
        }

        for (int j = 0; j < count_price.size(); j++) {
            if (count_price.get(j) > 0) {
                Log.d("mLog","LOG " + j + " " + mp_title[j] + " " + results.get(j)+ " " + n.get(j)+ " " + count_price.get(j));

                Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                        mp_title[j], String.valueOf(results.get(j)), String.valueOf(n.get(j)), String.valueOf(count_price.get(j)), null);

                client_mas.add(fc);
            }
        }

        if (dop_krepezh>0){
            Log.d("mLog","LOG " + mp_title[17] + " " + results.get(17)+ " " + dop_krepezh + " " + count_dop_krepezh);
            Frag_client_schedule_class fc = new Frag_client_schedule_class(null,
                    mp_title[17], String.valueOf(results.get(17)), String.valueOf(dop_krepezh), String.valueOf(count_dop_krepezh), null);

            client_mas.add(fc);
        }

        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId_client();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getStatus();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.adapter_list_mounting, dict);
        list_general.setAdapter(adapter);

        return view;
    }
}