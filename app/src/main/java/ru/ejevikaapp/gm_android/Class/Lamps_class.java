package ru.ejevikaapp.gm_android.Class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.DBHelper;

/**
 * Created by Дмитрий on 19.01.2018.
 */

public class Lamps_class {

    DBHelper dbHelper;
    EditText kol_vo_svetiln;
    Button btn_add_svetilnik;
    ListView list_svetilnik;

    String select_vid, select_diam, c_id = "", gager_id="", id_calc;

    Integer type_id, comp_opt;

    ArrayList s_v = new ArrayList();
    ArrayList s_d = new ArrayList();
    ArrayAdapter<String> adapter;

    Cursor c = null;

    final ArrayList<Svetiln_class> svet_mas = new ArrayList<>();

    Integer gager_id_int = 0;

    Context ctx;

    public ArrayList spinner(DBHelper dbHelper){

        String item_content1 = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        s_v.clear();

        Cursor c = db.query("rgzbn_gm_ceiling_type", null, "_id between 2 and 3", null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content1 = c.getString(c.getColumnIndex(c.getColumnName(2)));
                    }
                    s_v.add(item_content1);
                } while (c.moveToNext());
            }
        }
        c.close();

        return s_v;
    }

    public ArrayList spinner2(DBHelper dbHelper, int select_vid){

        switch (select_vid){
            case (0):
                c_id = " 21";
                type_id = 2;
                break;
            case (1):
                c_id = " 12";
                type_id = 3;
                break;
        }

        s_d.clear();
        String item_content2 = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sqlQuewy = "SELECT title "
                + "FROM rgzbn_gm_ceiling_components_option" +
                " WHERE component_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{c_id});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        item_content2 = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    }

                    s_d.add(item_content2);
                } while (c.moveToNext());
            }
        }
        c.close();

        return s_d;
    }
}
