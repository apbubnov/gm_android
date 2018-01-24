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

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Activity_add_brigade;
import ru.ejevikaapp.authorization.Activity_upd_brigade;
import ru.ejevikaapp.authorization.Class.Frag_add_brigade_class;
import ru.ejevikaapp.authorization.Class.Frag_brigade_class;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;

public class Frag_brigade extends Fragment implements View.OnClickListener{

    ArrayList<Frag_brigade_class> client_mas = new ArrayList<>();
    Button add_brigade;
    View view;
    ListView list_client;
    DBHelper dbHelper;

    public Frag_brigade() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        add_brigade = (Button) view.findViewById(R.id.add_brigade);
        add_brigade.setOnClickListener(this);
        client_mas = new ArrayList<>();

        dbHelper = new DBHelper(getActivity());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

    //    Cursor cursor = db.query(DBHelper.TABLE_NAME7, null, null, null, null, null, null);
//
    //    if (cursor.moveToFirst()) {
    //        int kdIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
    //        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
    //        int fioIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
    //        int phoneIndex = cursor.getColumnIndex(DBHelper.KEY_PHONE);
    //        do {
    //            Frag_brigade_class c = new Frag_brigade_class(cursor.getString(kdIndex), cursor.getString(nameIndex),
    //                    null, null,null);
    //            client_mas.add(c);
//
    //        } while (cursor.moveToNext());
    //    }
    //    cursor.close();

        BindDictionary<Frag_brigade_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_brigade_class>() {
            @Override
            public String getStringValue(Frag_brigade_class nc, int position) {
                return nc.getName();
            }
        });

        dict.addStringField(R.id.c_address, new StringExtractor<Frag_brigade_class>() {
            @Override
            public String getStringValue(Frag_brigade_class nc, int position) {
                return "0";
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_brigade_class>() {
            @Override
            public String getStringValue(Frag_brigade_class nc, int position) {
                return "123/1232";
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_brigade_class>() {
            @Override
            public String getStringValue(Frag_brigade_class nc, int position) {
                return "9";
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), client_mas, R.layout.clients_item44, dict);

        list_client = (ListView)view.findViewById(R.id.list_client);
        list_client.setAdapter(adapter);

            list_client.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Frag_brigade_class pos = client_mas.get(position);

                    Intent intent = new Intent(getActivity(), Activity_upd_brigade.class);
                    intent.putExtra("ID_Client",pos.getId());
                    startActivity(intent);
                }
            });
        getChildFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.frag_brigade, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_brigade:
                Intent intent = new Intent(getActivity(), Activity_add_brigade.class);
                startActivity(intent);
                break;
        }
    }

}
