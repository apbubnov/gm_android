package ru.ejevikaapp.authorization.Manager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_client_schedule_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_in_production extends Fragment {

    DBHelper dbHelper;
    ListView list_clients;
    ArrayList<Frag_client_schedule_class> client_mas = new ArrayList<>();

    SharedPreferences SP;
    String SAVED_ID="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View View = inflater.inflate(R.layout.frag_g3_zapusch, container, false);

        list_clients = (ListView)View.findViewById(R.id.list_client);

        clients();
        return View;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void clients (){

        dbHelper = new DBHelper(getActivity());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE project_status = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{String.valueOf("5")});

        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    String id = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    Log.d("mLog", id);

                    sqlQuewy = "SELECT * "
                            + "FROM rgzbn_gm_ceiling_clients" +
                            " WHERE _id = ?";

                    Cursor k = db.rawQuery(sqlQuewy, new String[]{id});

                    if (k.moveToFirst()) {
                        int kdIndex = k.getColumnIndex(DBHelper.KEY_ID);
                        int fioIndex = k.getColumnIndex(DBHelper.KEY_CLIENT_NAME);
                        do {
                            String id_c = k.getString(kdIndex);
                            String p_info = "";
                            String phone = "";
                            String id_client="";
                            String fio = "";

                            sqlQuewy = "SELECT project_info, client_id "
                                    + "FROM rgzbn_gm_ceiling_projects";

                            Cursor cursor = db.rawQuery(sqlQuewy, new String[]{});

                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        p_info = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                        id_client = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            sqlQuewy = "SELECT phone "
                                    + "FROM rgzbn_gm_ceiling_clients_contacts" +
                                    " WHERE client_id = ?";

                            cursor = db.rawQuery(sqlQuewy, new String[]{id_client});

                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        phone = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            sqlQuewy = "SELECT client_name "
                                    + "FROM rgzbn_gm_ceiling_clients" +
                                    " WHERE _id = ?";

                            cursor = db.rawQuery(sqlQuewy, new String[]{id_client});

                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        fio = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));

                                    } while (cursor.moveToNext());
                                }
                            }
                            cursor.close();

                            Frag_client_schedule_class fc = new Frag_client_schedule_class(k.getString(kdIndex), fio,
                                    p_info, id_client, phone, null);
                            client_mas.add(fc);

                        } while (k.moveToNext());
                    }
                    k.close();

                } while (c.moveToNext());
            }
            c.close();
        }


        BindDictionary<Frag_client_schedule_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_client_schedule_class>() {
            @Override
            public String getStringValue(Frag_client_schedule_class nc, int position) {
                return nc.getId();
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
                return nc.getStatus();
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
                return nc.getFio();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item3, dict);
        list_clients.setAdapter(adapter);

        list_clients.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Frag_client_schedule_class selectedid = client_mas.get(position);
                final String s_id = selectedid.getId_client();

                SharedPreferences SP = getActivity().getSharedPreferences("id_cl", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString(SAVED_ID, String.valueOf(s_id));
                ed.commit();

                Intent intent = new Intent(getActivity(), Activity_in_production_tab.class);
                startActivity(intent);
            }
        });
    }

}
