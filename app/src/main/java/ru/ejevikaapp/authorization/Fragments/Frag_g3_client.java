package ru.ejevikaapp.authorization.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Activity_add_client;
import ru.ejevikaapp.authorization.Activity_upd_client;
import ru.ejevikaapp.authorization.Class.Frag_g3_client_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Frag_g3_client extends Fragment implements View.OnClickListener{

    Button add_client;
    View view;
    DBHelper dbHelper;
    ListView list_client;

    ArrayList<Frag_g3_client_class> client_mas = new ArrayList<>();

    public Frag_g3_client() {
    }

    @Override
    public void onResume() {
        super.onResume();

        add_client = (Button)view.findViewById(R.id.add_client);
        add_client.setOnClickListener(this);
        client_mas = new ArrayList<>();

        dbHelper = new DBHelper(getActivity());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

    //    Cursor cursor = db.query(DBHelper.TABLE_NAME6, null, null, null, null, null, null);
//
    //    if (cursor.moveToFirst()) {
    //        int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
    //        int fioIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
    //        int numberIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
    //        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
    //        do {
    //            Frag_g3_client_class c = new Frag_g3_client_class(cursor.getString(kdIndex), cursor.getString(fioIndex),
    //                    cursor.getString(numberIndex), cursor.getString(dateIndex));
    //            client_mas.add(c);
//
    //        } while (cursor.moveToNext());
    //    }
    //    cursor.close();

        BindDictionary<Frag_g3_client_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_date, new StringExtractor<Frag_g3_client_class>() {
            @Override
            public String getStringValue(Frag_g3_client_class nc, int position) {
                return nc.getDate();
            }
        });

        dict.addStringField(R.id.c_fio, new StringExtractor<Frag_g3_client_class>() {
            @Override
            public String getStringValue(Frag_g3_client_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_phone, new StringExtractor<Frag_g3_client_class>() {
            @Override
            public String getStringValue(Frag_g3_client_class nc, int position) {
                return nc.getNumber();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item4, dict);

        list_client = (ListView)view.findViewById(R.id.list_client);
        list_client.setAdapter(adapter);

        list_client.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Frag_g3_client_class pos = client_mas.get(position);

                Intent intent = new Intent(getActivity(), Activity_upd_client.class);
                intent.putExtra("ID_Client",pos.getId());
                startActivity(intent);
            }
        });
        getChildFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_g3_client, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_client:
                Intent intent = new Intent(getActivity(), Activity_add_client.class);
                startActivity(intent);
                break;
        }
    }
}
