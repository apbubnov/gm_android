package ru.ejevikaapp.gm_android.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.DBHelper;

import static android.content.Context.MODE_PRIVATE;

public class HelperClass {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int countColumns(Context context, String table_name) {

        int count = 0;
        String sql = "";

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();

        String sqlQuewy = "SELECT sql FROM sqlite_master " +
                "WHERE tbl_name = '" + table_name + "' ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    sql = c.getString(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        count = sql.length() - sql.replace(",", "").length() + 1;

        return count;
    }

    static DBHelper dbHelper;

    static ArrayList<Double> component_count = new ArrayList();    // component_option
    static ArrayList canvases_data = new ArrayList();
    static ArrayList mounting_data = new ArrayList();
    static ArrayList<Integer> results = new ArrayList();
    static ArrayList component_item = new ArrayList();

    static int items_9, items_5, items_11, items_vstavka_bel, items_vstavka, items_10, items_16, items_556, items_4, items_58, items_3,
               items_2, items_1, items_8, items_6, items_14, items_430, items_35, items_360, items_236,
               items_239, items_559, items_38, items_495, items_233, items_659, items_660;

    static int[] n13_count;
    static String[] n13_type;
    static int[] n13_size_id;
    static String[] n13_size;

    static int[] n22_count;
    static String[] n22_type;
    static int[] n22_size_id;
    static String[] n22_size;

    static int[] n14_count;
    static int[] n14_size_id;
    static String[] n14_size;

    static int[] n23_count;
    static String[] n23_type;
    static int[] n23_size_id;
    static String[] n23_size;

    static int[] n15_count;
    static String[] n15_type;
    static int[] n15_size_id;
    static String[] n15_size;

    static int[] n26_count;
    static int[] n26_type_id;
    static int[] n26_size_id;

    static int[] n29_count;
    static int[] n29_type;

    static double[] ar1_size;
    static double[] ar2_size;
    static double[] ar2_size2;

    static Integer gm_can_marg, gm_comp_marg, gm_mount_marg, dealer_can_marg, dealer_comp_marg, dealer_mount_marg, count_svet,
            count_vent, count_electr, count_diffus, count_pipes, id_n3 = 0;

    static int profile_12_13, profile_15_16, circle_count = 0, square_count = 0;

    public static JSONObject calculation(Context context, String dealer_id_str, int colorIndex, String id_calculation, String canvases, String texture, String rb_vstavka,
                                     String n1, Integer n2, String n3, double S, double P, int n6, double n7, double n8, double n9,
                                     double n11, double n12, double n16, double n17, double n18, double n19, double n20,
                                     double n21, double n24, double n27, double n28,
                                     double n30, double n31, double n32, double dop_krepezh, double height, String offcut_square, String width_final,
                                     String final_comp, String final_mount) {

        JSONObject result = new JSONObject();
        JSONObject comp_estimate = new JSONObject();

        dbHelper = new DBHelper(context);
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

        cou = HelperClass.countColumns(context, "rgzbn_gm_ceiling_mount");

        results.clear();
        for (int i = 0; i < cou; i++) {
            results.add(0);
        }

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_mount " +
                "where user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id_str});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (int j = 1; j < cou; j++) {
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
                "where component_id = 190";
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
                "where component_id = 191";
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
            if (n28 == 0) {
                component_count.set(items_11, component_count.get(items_11) + P);
                component_count.set(items_9, component_count.get(items_9) + P * 10);
                component_count.set(items_5, component_count.get(items_5) + P * 10);
            } else if (n28 == 1) {
                component_count.set(items_236, component_count.get(items_236) + P);
                component_count.set(items_9, component_count.get(items_9) + P * 10);
                component_count.set(items_5, component_count.get(items_5) + P * 10);
            } else if (n28 == 2) {
                component_count.set(items_239, component_count.get(items_239) + P);
                component_count.set(items_9, component_count.get(items_9) + P * 10);
                component_count.set(items_5, component_count.get(items_5) + P * 10);
            }
        }

        if (n1.equals("29") && P == 0.0) {
            if (n28 == 3) {
            } else {
                component_count.set(items_9, component_count.get(items_9) + P * 10);
                component_count.set(items_5, component_count.get(items_5) + P * 10);
            }
        }

        if (n6 == 0) {
        } else {
            component_count.set(n6, component_count.get(n6) + P);
        }

        // внутренний вырез(на месте)
        if (n11 > 0) {
            component_count.set(items_1, component_count.get(items_1) + n11);
            if (n1.equals("29")) {
                component_count.set(items_233, component_count.get(items_233) + n11);
            } else if (n1.equals("28") && n28 == 0) {
                component_count.set(items_11, component_count.get(items_11) + n11);
            } else if (n1.equals("28") && n28 == 1) {
                component_count.set(items_236, component_count.get(items_236) + n11);
            } else if (n1.equals("28") && n28 == 2) {
                component_count.set(items_239, component_count.get(items_239) + n11);
            }

            component_count.set(items_430, component_count.get(items_430) + n11 * 3);
            component_count.set(items_8, component_count.get(items_8) + n11 * 22);
            component_count.set(items_5, component_count.get(items_5) + n11 * 16);
            component_count.set(items_360, component_count.get(items_360) + n11);

            component_count.set(n6, component_count.get(n6) + n11);
        }

        // внутренний вырез(в цеху)
        if (n31 > 0) {
            double n31_count = Math.ceil(n31);
            try {
                if (n6 == 0) {
                    //ничего не делаем
                } else if (n6 == 314) {
                    component_count.set(n6, component_count.get(n6) + n31_count);
                } else {
                    component_count.set(n6, component_count.get(n6) + n31_count);
                }
            } catch (Exception e) {
            }

            if (n1.equals("28") && n28 == 0) {
                component_count.set(items_11, component_count.get(items_11) + n31);
            } else if (n1.equals("28") && n28 == 1) {
                component_count.set(items_236, component_count.get(items_236) + n31);
            } else if (n1.equals("28") && n28 == 1) {
                component_count.set(items_239, component_count.get(items_239) + n31);
            }
            component_count.set(items_9, component_count.get(items_9) + n31 * 10);
            component_count.set(items_5, component_count.get(items_5) + n31 * 10);

        }

        //люстры

        if (n12 > 0) {
            component_count.set(items_5, component_count.get(items_5) + n12 * 3);
            component_count.set(items_9, component_count.get(items_9) + n12 * 3);
            component_count.set(items_10, component_count.get(items_10) + n12 * 8);
            component_count.set(items_16, component_count.get(items_16) + n12);
            component_count.set(items_556, component_count.get(items_556) + n12);
            component_count.set(items_4, component_count.get(items_4) + n12 * 0.5);
            component_count.set(items_58, component_count.get(items_58) + n12);
            component_count.set(items_3, component_count.get(items_3) + n12 * 4);
        }
        if (n12 > 0) {
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
        component_count.set(items_2, component_count.get(items_2) + count_svet * 1);

        //профиль
        i = 0;
        profile_12_13 = 0;
        profile_15_16 = 0;

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
                profile_12_13 += n29_count[j];
            } else if ((n29_type[j] == 15) || (n29_type[j] == 16)) {
                component_count.set(items_660, component_count.get(items_660) + n29_count[j]);
                profile_15_16 += n29_count[j];
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
        if (n30 > 0) {
            if (n28 == 0) {
                if (component_count.get(items_11) > n30) {
                    component_count.set(items_11, component_count.get(items_11) - n30);
                } else {
                    component_count.set(items_11, 0.0);
                }
            } else if (n28 == 1) {
                if (component_count.get(items_236) > n30) {
                    component_count.set(items_236, component_count.get(items_236) + n30);
                } else {
                    component_count.set(items_236, 0.0);
                }
            } else if (n28 == 2) {
                if (component_count.get(items_239) > n30) {
                    component_count.set(items_239, component_count.get(items_239) - n30);
                } else {
                    component_count.set(items_11, 0.0);
                }
            }

            component_count.set(items_559, component_count.get(items_559) + n30);
            component_count.set(items_38, component_count.get(items_38) + n30);

        }

        //rouding(items_11, component_count.get(items_11), 2.5);
        //rouding(items_236, component_count.get(items_236), 2.5);
        //rouding(items_239, component_count.get(items_239), 2.5);

        //карниз
        if (n27 > 0) {

            component_count.set(items_1, component_count.get(items_1) + n27);
            component_count.set(items_3, component_count.get(items_3) + n27 * 3);
            component_count.set(items_5, component_count.get(items_5) + n27 * 6);
            component_count.set(items_8, component_count.get(items_8) + n27 * 9);
            component_count.set(items_9, component_count.get(items_9) + n27 * 6);

            // скрытый карниз
            if (n16 == 1) {
                component_count.set(items_430, component_count.get(items_430) + n27 * 2);
                component_count.set(items_8, component_count.get(items_8) + n27 * 4);
            }

        }

        //закладная брусом
        if (n17 > 0) {
            component_count.set(items_1, component_count.get(items_1) + n17);
            component_count.set(items_3, component_count.get(items_3) + n17 * 3);
            component_count.set(items_5, component_count.get(items_5) + n17 * 6);
            component_count.set(items_9, component_count.get(items_9) + n17 * 6);
            component_count.set(items_8, component_count.get(items_8) + n17 * 6);
        }

        //укрепление стен
        if (n18 > 0) {
            component_count.set(items_1, component_count.get(items_1) + n18);
            component_count.set(items_6, component_count.get(items_6) + n18 * 3);
            component_count.set(items_5, component_count.get(items_5) + n18 * 3);
            component_count.set(items_430, component_count.get(items_430) + n18 * 3);
        }

        // провод
        if (n19 > 0) {
            component_count.set(items_4, component_count.get(items_4) + n19);
            component_count.set(items_9, component_count.get(items_9) + n19 * 2);
            component_count.set(items_5, component_count.get(items_5) + n19 * 2);
        }

        //разделитель только для ПВХ
        if (n20 > 0) {
            component_count.set(items_1, component_count.get(items_1) + n20);
            component_count.set(items_6, component_count.get(items_6) + n20 * 3);
            component_count.set(items_9, component_count.get(items_9) + n20 * 20);
            component_count.set(items_5, component_count.get(items_5) + n20 * 3);
            component_count.set(items_14, component_count.get(items_14) + n20);

            int n20_count = (int) (n20 / 2.5);
            if ((n20 % 2.5) > 0) {
                n20_count++;
            }
            component_count.set(items_35, component_count.get(items_35) + n20_count * 2.5);
        }

        // дополнительный крепеж
        if (dop_krepezh > 0) {
            component_count.set(items_9, component_count.get(items_9) + dop_krepezh * 10);
            if (n1.equals("29")) {
                component_count.set(items_233, component_count.get(items_233) + (dop_krepezh / 2));
            } else if (n1.equals("28") && n28 == 0) {
                component_count.set(items_11, component_count.get(items_11) + (dop_krepezh / 2));
            } else if (n1.equals("28") && n28 == 1) {
                component_count.set(items_236, component_count.get(items_236) + (dop_krepezh / 2));
            } else if (n1.equals("28") && n28 == 2) {
                component_count.set(items_239, component_count.get(items_239) + (dop_krepezh / 2));
            }
        }

        // пожарная сигнализация
        Log.d("mLog", String.valueOf(n21));
        if (n21 > 0) {
            component_count.set(items_9, component_count.get(items_9) + n21 * 3);
            component_count.set(items_10, component_count.get(items_10) + n21 * 6);
            component_count.set(items_495, component_count.get(items_495) + n21);
            component_count.set(items_58, component_count.get(items_58) + n21);
            component_count.set(items_3, component_count.get(items_3) + n21 * 3);
            component_count.set(items_5, component_count.get(items_5) + n21 * 3);
            component_count.set(items_2, component_count.get(items_2) + n21 * 2);
        }

        //стеновой багет 2.5м считается кусками, которые потребуются выложить весь периметр
        if (n28 == 0) {
            rouding(items_11, component_count.get(items_11), 2.5);
        } else if (n28 == 1) {
            rouding(items_236, component_count.get(items_236), 2.5);
        } else if (n28 == 2) {
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

        if (n31 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Внутренний вырез(в цеху)");
            values.put(DBHelper.KEY_QUANTITY, n31);
            values.put(DBHelper.KEY_STACK, "0");
            values.put(DBHelper.KEY_GM_PRICE, results.get(21));
            values.put(DBHelper.KEY_GM_TOTAL, n31 * results.get(21));
            values.put(DBHelper.KEY_DEALER_PRICE, results.get(21));
            values.put(DBHelper.KEY_DEALER_TOTAL, n31 * results.get(21));
            db.insert(DBHelper.TABLE_COMPONENT_ITEM, null, values);
        }

        // другие комп
        if (final_comp.equals("{}")) {
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

                String gm_price = String.valueOf(margin(Double.parseDouble(self_price), dealer_comp_marg));
                String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), dealer_comp_marg, gm_comp_marg));
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


        //Обработка 1 угла
        if (n1.equals("28") && n9 > 6) {

            canvases_data.set(0, "Oбработка 1 угла");                         // название
            canvases_data.set(1, n9 - 6);                                             // кол-во
            canvases_data.set(2, results.get(19));                                                         // цена
            canvases_data.set(3, (n9 - 6) * results.get(19));                                     // Кол-во * Себестоимость
            canvases_data.set(4, margin(results.get(19), dealer_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
            canvases_data.set(5, Math.rint(100.0 * (margin(results.get(19), dealer_can_marg)) * (n9 - 6)) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
            canvases_data.set(6, double_margin(results.get(19), dealer_can_marg, gm_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
            canvases_data.set(7, Math.rint(100 * (double_margin(results.get(19), dealer_can_marg, gm_can_marg)) * (n9 - 6)) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

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

        Log.d("mLog", "canvases = " + canvases + n3);

        int count_space = 0;
        char[] chars = canvases.toCharArray();
        for (int s = 0; s < canvases.length(); s++) {
            if (chars[s] == ' ') {
                count_space ++;
            }
        }

        StringBuffer sb = new StringBuffer();
        chars = canvases.toCharArray();
        int count = 0;
        for (int s = 0; s < canvases.length(); s++) {

            if (chars[s] == ' ') {
                sb.append(chars[s]);
                count++;
            } else {
                sb.append(chars[s]);
            }

            if (chars[s] == ' ' && count_space == count) {
                break;
            }
        }

        String str_sb = String.valueOf(sb);
        try {
            str_sb = str_sb.substring(0, str_sb.length() - 1);
        } catch (Exception e){
        }

        boolean canvases_price = false;
        boolean canvases_price_dealer = false;

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_canvases_dealer_price " +
                "where user_id = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id_str});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                canvases_price = true;
            }
        }
        c.close();

        if (!canvases_price) {
            sqlQuewy = "select * "
                    + "FROM rgzbn_gm_ceiling_canvases_dealer_price " +
                    "where user_id = ? ";
            c = db.rawQuery(sqlQuewy, new String[]{"1"});         // заполняем массивы из таблицы
            if (c != null) {
                if (c.moveToFirst()) {
                    canvases_price_dealer = true;
                }
            }
            c.close();
        }

        //Сюда считаем итоговую сумму полотна
        if (P == 0.0) {
        } else {
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

                    if (canvases_price) {
                        price = new_price("canvases", dealer_id_str, id_n3, price);
                    }

                    canvases_data.set(0, texture + ", " + canvases + ", " + width);                         // название
                    canvases_data.set(1, Double.valueOf(S));                                             // кол-во
                    canvases_data.set(2, price);                                                         // цена
                    canvases_data.set(3, price * Double.valueOf(S));                                     // Кол-во * Себестоимость
                    canvases_data.set(4, margin(price, dealer_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(5, Math.rint(100.0 * (margin(price, dealer_can_marg)) * S) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                    canvases_data.set(6, double_margin(price, dealer_can_marg, gm_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
                    canvases_data.set(7, Math.rint(100 * (double_margin(price, dealer_can_marg, gm_can_marg)) * S) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

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
                        canvases_data.set(1, Double.valueOf(offcut_square));           // кол-во
                        canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                        canvases_data.set(3, Math.rint(100 * (Double.valueOf(offcut_square) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                        canvases_data.set(4, Math.rint(100 * (margin(price, dealer_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                        canvases_data.set(5, Math.rint(100 * Double.parseDouble(offcut_square) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                        canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, dealer_can_marg, gm_can_marg) / 100 * 40, dealer_can_marg, gm_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                        canvases_data.set(7, Math.rint(100 * (Double.parseDouble(offcut_square) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

                    } catch (Exception e) {
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

                int id = 0;
                sqlQuewy = "select _id "
                        + "FROM rgzbn_gm_ceiling_canvases_manufacturers " +
                        "where name LIKE('%"+str_sb+"%')";
                c = db.rawQuery(sqlQuewy, null);         // заполняем массивы из таблицы
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            id = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                sqlQuewy = "select _id, price "
                        + "FROM rgzbn_gm_ceiling_canvases " +
                        "where texture_id = ? and manufacturer_id = ? and width =?";
                c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n2), String.valueOf(id), String.valueOf(wf)});         // заполняем массивы из таблицы
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            id_n3 = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
                            price = Double.valueOf(c.getString(c.getColumnIndex(c.getColumnName(1))));
                        } while (c.moveToNext());
                    }
                }
                c.close();

                if (canvases_price) {
                    price = new_price("canvases", dealer_id_str, id_n3, price);
                } else {
                    price = new_price("canvases", "1", id_n3, price);
                }

                canvases_data.set(0, texture + ", " + canvases + ", " + wf);                         // название
                canvases_data.set(1, Double.valueOf(S));                                             // кол-во
                canvases_data.set(2, price);                                                         // цена
                canvases_data.set(3, price * Double.valueOf(S));                                     // Кол-во * Себестоимость
                canvases_data.set(4, margin(price, dealer_can_marg));                                    //Стоимость с маржой ГМ (для дилера)
                canvases_data.set(5, Math.rint(100.0 * (margin(price, dealer_can_marg)) * S) / 100.0);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                canvases_data.set(6, double_margin(price, dealer_can_marg, gm_can_marg));            //Стоимость с маржой ГМ и дилера (для клиента)
                canvases_data.set(7, Math.rint(100 * (double_margin(price, dealer_can_marg, gm_can_marg)) * S) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)

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
                if (width_final.equals("") && (offcut_square.equals("0") || offcut_square.equals("null"))) {
                } else {
                    double wf = Double.valueOf(width_final) / 100;
                    try {
                        canvases_data.set(0, "Количесво обрезков");                 // название
                        canvases_data.set(1, Double.valueOf(offcut_square));           // кол-во
                        canvases_data.set(2, Math.rint(100 * (price / 2)) / 100.0);                                // цена
                        canvases_data.set(3, Math.rint(100 * (Double.valueOf(offcut_square) * Double.valueOf(String.valueOf(canvases_data.get(2))))) / 100);       // Кол-во * Себестоимость
                        canvases_data.set(4, Math.rint(100 * (margin(price, dealer_can_marg)) / 2) / 100);                                    //Стоимость с маржой ГМ (для дилера)
                        canvases_data.set(5, Math.rint(100 * Double.parseDouble(offcut_square) * Double.parseDouble(String.valueOf(canvases_data.get(4)))) / 100);   //Кол-во * Стоимость с маржой ГМ (для дилера)
                        canvases_data.set(6, Math.rint(100 * (double_margin(double_margin(price, dealer_can_marg, gm_can_marg) / 100 * 40, dealer_can_marg, gm_can_marg)) / 2) / 100);            //Стоимость с маржой ГМ и дилера (для клиента)
                        canvases_data.set(7, Math.rint(100 * (Double.parseDouble(offcut_square) * Double.parseDouble(String.valueOf(canvases_data.get(6))))) / 100);  //Кол-во * Стоимость с маржой ГМ и дилера (для клиента)
                    } catch (Exception e) {
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
        }

        boolean components_price = false;
        boolean components_price_dealer = false;

        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_components_dealer_price " +
                "where user_id = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{dealer_id_str});
        if (c != null) {
            if (c.moveToFirst()) {
                components_price = true;
            }
        }
        c.close();

        if (!components_price) {
            sqlQuewy = "select * "
                    + "FROM rgzbn_gm_ceiling_components_dealer_price " +
                    "where user_id = ? ";
            c = db.rawQuery(sqlQuewy, new String[]{"1"});
            if (c != null) {
                if (c.moveToFirst()) {
                    components_price_dealer = true;
                }
            }
            c.close();
        }

        //Сюда считаем итоговую сумму компонентов
        sqlQuewy = "select * "
                + "FROM rgzbn_gm_ceiling_components_option";
        c = db.rawQuery(sqlQuewy, new String[]{});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Integer id = c.getInt(c.getColumnIndex(c.getColumnName(0)));

                    if (component_count.get(id) != 0) {
                        String title = c.getString(c.getColumnIndex(c.getColumnName(2)));
                        String stack = "0";
                        String component_id = c.getString(c.getColumnIndex(c.getColumnName(1)));
                        Double self_price = c.getDouble(c.getColumnIndex(c.getColumnName(3)));

                        if (components_price){
                            self_price = new_price("components", dealer_id_str, id, self_price);
                        } else {
                            self_price = new_price("components", "1", id, self_price);
                        }

                        sqlQuewy = "select * "
                                + "FROM rgzbn_gm_ceiling_components " +
                                "where _id = ? ";
                        Cursor k = db.rawQuery(sqlQuewy, new String[]{component_id});         // заполняем массивы из таблицы
                        if (k != null) {
                            if (k.moveToFirst()) {
                                do {
                                    String full_name = k.getString(k.getColumnIndex(k.getColumnName(1))) + " " + title;

                                    String unit = k.getString(k.getColumnIndex(k.getColumnName(2)));
                                    String quantity = "";
                                    if (unit.equals("шт.")) {
                                        quantity = String.valueOf(Math.ceil(component_count.get(id)));
                                    } else {
                                        quantity = String.valueOf(component_count.get(id));
                                    }

                                    String self_total = String.valueOf(Math.round((self_price * Double.parseDouble(quantity)) * 100.0) / 100.0);

                                    String gm_price = String.valueOf(margin(self_price, dealer_comp_marg));
                                    String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * Double.parseDouble(quantity)) * 100.0) / 100.0);

                                    String dealer_price = String.valueOf(double_margin(self_price, dealer_comp_marg, gm_comp_marg));
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
            if (n11 > 0 || n31 > 0) {
                sqlQuewy = "select * "
                        + "FROM rgzbn_gm_ceiling_components_option " +
                        "where title = ? and component_id = 15";
                c = db.rawQuery(sqlQuewy, new String[]{"303 белая"});
                if (c != null) {
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                        String full_name = "Вставка 303 белая" ;
                        String stack = "0";

                        String self_price = c.getString(c.getColumnIndex(c.getColumnName(3)));

                        double quantity = n11 + n31;

                        String self_total = String.valueOf(Math.round((Double.parseDouble(self_price) * quantity) * 100.0) / 100.0);

                        String gm_price = String.valueOf(margin(Double.parseDouble(self_price), dealer_comp_marg));
                        String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * quantity) * 100.0) / 100.0);

                        String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), dealer_comp_marg, gm_comp_marg));
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
                    }
                }
            }
        } else if (rb_vstavka.equals("1")) {

            sqlQuewy = "select * "
                    + "FROM rgzbn_gm_ceiling_components_option " +
                    "where title = ? and component_id = 15";
            c = db.rawQuery(sqlQuewy, new String[]{String.valueOf(n6)});
            if (c != null) {
                if (c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));

                    String full_name = "Вставка " + n6;
                    String stack = "0";

                    String unit = "м.п.";

                    String self_price = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    double quantity = 0;

                    quantity += component_count.get(n6);

                    String self_total = String.valueOf(Math.round((Double.parseDouble(self_price) * quantity) * 100.0) / 100.0);

                    String gm_price = String.valueOf(margin(Double.parseDouble(self_price), dealer_comp_marg));
                    String gm_total = String.valueOf(Math.round((Double.parseDouble(gm_price) * quantity) * 100.0) / 100.0);

                    String dealer_price = String.valueOf(double_margin(Double.parseDouble(self_price), dealer_comp_marg, gm_comp_marg));
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

                }
            }
        }

        String components = "";
        Cursor cursor = db.query(DBHelper.TABLE_COMPONENT_ITEM, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int title = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int quantity = cursor.getColumnIndex(DBHelper.KEY_QUANTITY);
            int dealer_price = cursor.getColumnIndex(DBHelper.KEY_DEALER_PRICE);
            int dealer_total = cursor.getColumnIndex(DBHelper.KEY_DEALER_TOTAL);
            do {
                Log.d("mLog", "Hаименование = " + cursor.getString(title) +
                        ", количество = " + cursor.getString(quantity) +
                        ", цена, руб = " + cursor.getString(dealer_price) +
                        ", стоимость, руб = " + cursor.getString(dealer_total));

                components += "Hаименование = " + cursor.getString(title) +
                        "; количество = " + cursor.getString(quantity) +
                        "; цена, руб = " + cursor.getString(dealer_price) +
                        "; стоимость, руб = " + cursor.getString(dealer_total)+" | ";

            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

        try {
            comp_estimate.put("components", components);
        } catch (JSONException e) {
        }

        //---------------------------------- РАСЧЕТ СТОИМОСТИ МОНТАЖА --------------------------------------//

        //внутренний вырез ТОЛЬКО ДЛЯ ПВХ
        if (n1.equals("28") && (n11 > 0)) {
            ContentValues values = new ContentValues();

            values.put(DBHelper.KEY_TITLE, "Внутренний вырез для ПВХ");
            values.put(DBHelper.KEY_QUANTITY, n11);
            values.put(DBHelper.KEY_GM_SALARY, results.get(21));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n11 * results.get(21));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(21));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n11 * results.get(21));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        int cost = 0;
        if (height == 1) {
            cost = 10;
        }

        //периметр только для ПВХ
        if (n1.equals("28") && P > 0 && n28 == 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(0) + cost);
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * (results.get(0) + cost));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(0) + cost);
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * (results.get(0) + cost));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        } else if (n1.equals("28") && P > 0 && n28 == 1) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(30));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(30));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(30));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(30));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        } else if (n1.equals("28") && P > 0 && n28 == 2) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр");
            values.put(DBHelper.KEY_QUANTITY, P);
            values.put(DBHelper.KEY_GM_SALARY, results.get(31));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(31));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(31));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(31));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n31 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Периметр (внутренний вырез)");
            values.put(DBHelper.KEY_QUANTITY, n31);
            values.put(DBHelper.KEY_GM_SALARY, results.get(0));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n31 * results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(0));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n31 * results.get(0));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        //вставка внутренний вырез
        if (n31 > 0) {
            try {
                ContentValues values = new ContentValues();
                if (n6 == 0) {
                    values.put(DBHelper.KEY_TITLE, "Вставка(внутренний вырез)");
                } else {
                    values.put(DBHelper.KEY_TITLE, "Вставка(внутренний вырез), цвет: " + n6);
                }
                values.put(DBHelper.KEY_QUANTITY, n31);
                values.put(DBHelper.KEY_GM_SALARY, results.get(9));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, n31 * results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n31 * results.get(9));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);

            } catch (Exception e) {
            }
        }

        if (rb_vstavka.equals("1")) {
            try {
                ContentValues values = new ContentValues();

                if (n6 == 0) {
                    values.put(DBHelper.KEY_TITLE, "Вставка");
                } else {
                    values.put(DBHelper.KEY_TITLE, "Вставка, цвет: " + n6);
                }

                values.put(DBHelper.KEY_QUANTITY, P);
                values.put(DBHelper.KEY_GM_SALARY, results.get(9));
                values.put(DBHelper.KEY_GM_SALARY_TOTAL, P * results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY, results.get(9));
                values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, P * results.get(9));
                db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);

            } catch (Exception e) {
            }
        }

        //Слив воды
        if (n32 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Слив воды");
            values.put(DBHelper.KEY_QUANTITY, n32);
            values.put(DBHelper.KEY_GM_SALARY, results.get(26));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n32 * results.get(26));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(26));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n32 * results.get(26));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n7 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Крепление в плитку");
            values.put(DBHelper.KEY_QUANTITY, Double.valueOf(n7));
            values.put(DBHelper.KEY_GM_SALARY, results.get(12));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, Double.valueOf(n7) * results.get(12));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(12));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, Double.valueOf(n7) * results.get(12));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n8 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Крепление в керамогранит");
            values.put(DBHelper.KEY_QUANTITY, Double.valueOf(n8));
            values.put(DBHelper.KEY_GM_SALARY, results.get(13));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, Double.valueOf(n8) * results.get(13));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(13));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, Double.valueOf(n8) * results.get(13));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }


        if (n12 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка люстр");
            values.put(DBHelper.KEY_QUANTITY, n12);
            values.put(DBHelper.KEY_GM_SALARY, results.get(1));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n12 * results.get(1));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(1));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n12 * results.get(1));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (circle_count > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка круглых светильников");
            values.put(DBHelper.KEY_QUANTITY, circle_count);
            values.put(DBHelper.KEY_GM_SALARY, results.get(3));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, circle_count * results.get(3));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(3));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, circle_count * results.get(3));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }
        if (square_count > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Установка квадратных светильников");
            values.put(DBHelper.KEY_QUANTITY, square_count);
            values.put(DBHelper.KEY_GM_SALARY, results.get(4));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, square_count * results.get(4));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(4));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, square_count * results.get(4));
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

        if (profile_12_13 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Переход уровня по прямой");
            values.put(DBHelper.KEY_QUANTITY, profile_12_13);
            values.put(DBHelper.KEY_GM_SALARY, results.get(22));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, profile_12_13 * results.get(22));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(22));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, profile_12_13 * results.get(22));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (profile_15_16 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Переход уровня по прямой с нишей");
            values.put(DBHelper.KEY_QUANTITY, profile_15_16);
            values.put(DBHelper.KEY_GM_SALARY, results.get(24));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, profile_15_16 * results.get(24));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(24));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, profile_15_16 * results.get(24));
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

        if (n27 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Шторный карниз");
            values.put(DBHelper.KEY_QUANTITY, n27);
            values.put(DBHelper.KEY_GM_SALARY, results.get(10));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n27 * results.get(10));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(10));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n27 * results.get(10));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n17 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Закладная брусом");
            values.put(DBHelper.KEY_QUANTITY, n17);
            values.put(DBHelper.KEY_GM_SALARY, results.get(10));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n17 * results.get(10));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(10));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n17 * results.get(10));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n18 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Укрепление стены");
            values.put(DBHelper.KEY_QUANTITY, n18);
            values.put(DBHelper.KEY_GM_SALARY, results.get(14));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n18 * results.get(14));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(14));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n18 * results.get(14));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n20 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Разделитель");
            values.put(DBHelper.KEY_QUANTITY, n20);
            values.put(DBHelper.KEY_GM_SALARY, results.get(8));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n20 * results.get(8));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(8));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n20 * results.get(8));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n21 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Пожарная сигнализация");
            values.put(DBHelper.KEY_QUANTITY, n21);
            values.put(DBHelper.KEY_GM_SALARY, results.get(5));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n21 * results.get(5));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(5));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n21 * results.get(5));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n30 > 0) {
            Log.d("mLog", String.valueOf(results.get(29)));
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Парящий потолок");
            values.put(DBHelper.KEY_QUANTITY, n30);
            values.put(DBHelper.KEY_GM_SALARY, results.get(29));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n30 * results.get(29));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(29));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n30 * results.get(29));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (n24 > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Сложность доступа");
            values.put(DBHelper.KEY_QUANTITY, n24);
            values.put(DBHelper.KEY_GM_SALARY, results.get(16));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, n24 * results.get(16));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(16));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, n24 * results.get(16));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
        }

        if (dop_krepezh > 0) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_TITLE, "Дополнительный крепеж");
            values.put(DBHelper.KEY_QUANTITY, dop_krepezh);
            values.put(DBHelper.KEY_GM_SALARY, results.get(17));
            values.put(DBHelper.KEY_GM_SALARY_TOTAL, dop_krepezh * results.get(17));
            values.put(DBHelper.KEY_DEALER_SALARY, results.get(17));
            values.put(DBHelper.KEY_DEALER_SALARY_TOTAL, dop_krepezh * results.get(17));
            db.insert(DBHelper.TABLE_MOUNTING_DATA, null, values);
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
        if (final_mount.equals("{}")) {
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
                    String quantity = String.valueOf(k.getDouble(k.getColumnIndex(k.getColumnName(2))));

                    //String gm_salary_total = String.valueOf(Math.round(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))) * 100.0) / 100.0);
                    //String dealer_salary_total = String.valueOf(Math.round(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(6)))) * 100.0) / 100.0);

                    String price_with_gm_margin = String.valueOf(margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))), gm_mount_marg));
                    String total_with_gm_margin = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(price_with_gm_margin));

                    Log.d("mlog", "mount1 " + total_with_gm_margin);

                    String price_with_gm_dealer_margin = String.valueOf(double_margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(3)))), dealer_mount_marg, gm_mount_marg));
                    String total_with_gm_dealer_margin = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(price_with_gm_dealer_margin));

                    Log.d("mlog", "mount2 " + price_with_gm_dealer_margin + " " + total_with_gm_dealer_margin);

                    String price_with_dealer_margin = String.valueOf(margin(Double.parseDouble(k.getString(k.getColumnIndex(k.getColumnName(5)))), dealer_mount_marg));
                    String total_with_dealer_margin = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(price_with_dealer_margin));

                    Log.d("mlog", "mount3 " + total_with_dealer_margin);

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

            String mounting = "";
        cursor = db.query(DBHelper.TABLE_MOUNTING_DATA, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int title = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int quantity = cursor.getColumnIndex(DBHelper.KEY_QUANTITY);
            int dealer_price = cursor.getColumnIndex(DBHelper.KEY_PRICE_WITH_DEALER_MARGIN);
            int dealer_total = cursor.getColumnIndex(DBHelper.KEY_TOTAL_WITH_DEALER_MARGIN);
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        "; наименование = " + cursor.getString(title) +
                        "; количество = " + cursor.getString(quantity) +
                        "; цена, руб = " + cursor.getString(dealer_price) +
                        "; стоимость, руб = " + cursor.getString(dealer_total));

                mounting += "Hаименование = " + cursor.getString(title) +
                        "; количество = " + cursor.getString(quantity) +
                        "; цена, руб = " + cursor.getString(dealer_price) +
                        "; стоимость, руб = " + cursor.getString(dealer_total) + " | ";

            } while (cursor.moveToNext());
        }
        cursor.close();

        try {
            comp_estimate.put("mounting", mounting);
        } catch (JSONException e) {
        }

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

            } while (cursor.moveToNext());
        } else

            Log.d("mLog", "0 rows");
        cursor.close();


        JSONObject array = new JSONObject();
        try {
            array.put("canvases_sum_total", canvases_sum_total);
            array.put("components_sum", components_sum);
            array.put("total_with_dealer_margin", total_with_dealer_margin);
            array.put("total_gm_mounting", total_gm_mounting);
            array.put("total_sum", total_sum);
            array.put("dealer_components_sum", dealer_components_sum);
            result.put("project",array);

            result.put("estimate",comp_estimate);

        } catch (JSONException e) {
        }

        return result;
    }

    static Double new_price(String table, String user_id, Integer id, Double old_price){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Integer type = 0;
        Double price = 0.0, value = 0.0;
        Double newPrice = 0.0;
        String sqlQuewy;

        if(table.equals("canvases")) {
            sqlQuewy = "select price, value, type "
                    + "FROM rgzbn_gm_ceiling_canvases_dealer_price " +
                    "where user_id = ? and canvas_id = ? ";
        } else {
            sqlQuewy = "select price, value, type "
                    + "FROM rgzbn_gm_ceiling_components_dealer_price " +
                    "where user_id = ? and component_id = ? ";
        }

        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id, String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    price = c.getDouble(c.getColumnIndex(c.getColumnName(0)));
                    value = c.getDouble(c.getColumnIndex(c.getColumnName(1)));
                    type = c.getInt(c.getColumnIndex(c.getColumnName(2)));

                } while (c.moveToNext());
            }
        }

        switch (type){
            case 0:
                newPrice = old_price;
                break;
            case 1:
                newPrice = price;
                break;
            case 2:
                newPrice = old_price + value;
                break;
            case 3:
                newPrice = old_price + (old_price * (value/100));
                break;
            case 4:
                newPrice = price + value;
                break;
            case 5:
                newPrice = price + (price * (value/100));
                break;
        }

        return newPrice;
    }

    static void rouding(int id, double coun, double value) {

        double count = Math.floor(coun / value);

        if ((coun / value) > count) {
            count++;
        }

        component_count.set(id, count * value);

    }

    static double margin(double val, int mar) {

        return val * 100 / (100 - mar);
    }

    static double double_margin(double val, int mar, int mar2) {

        return (val * 100 / (100 - mar)) * 100 / (100 - mar2);

    }

    //кольцо
    static void components_circle(int j, int id, int id2, int[] count, String[] sizes) {

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
    static void components_square(int j, int id, int id2, int[] count, String[] sizes) {

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

}