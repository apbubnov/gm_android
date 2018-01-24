package ru.ejevikaapp.authorization.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_g3_dogovor_class;
import ru.ejevikaapp.authorization.Class.Frag_montazh_class;
import ru.ejevikaapp.authorization.R;

public class Frag_montazh extends Fragment {

    ProgressDialog mProgressDialog;
    View view;
    ArrayList<Frag_montazh_class> table_mas = new ArrayList<>();

    public Frag_montazh() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Table().execute();
        return inflater.inflate(R.layout.fragment_frag_montazh, container, false);
    }

    private class Table extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Идёт загрузка таблицы");
            mProgressDialog.setMessage("Загрузка...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

        //   Frag_montazh_class n = new Frag_montazh_class("666","666",
        //           "г. Воронеж, ул. Туполева, д. 45, кв. 33", "+79521067977", "Лобов Андрей Сергеевич" );
        //   table_mas.add(n);

        //   n = new Frag_montazh_class("777","777",
        //           "г. Воронеж, ул. Проспект Труда, д. 48, кв. 1", "+79531264875", "Ком Ким Чин" );
        //   table_mas.add(n);
           return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            clients();
            mProgressDialog.dismiss();
        }
    }

    void clients(){

        BindDictionary<Frag_montazh_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_montazh_class>() {
            @Override
            public String getStringValue(Frag_montazh_class nc, int position) {
                return nc.getNumber();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_montazh_class>() {
            @Override
            public String getStringValue(Frag_montazh_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_montazh_class>() {
            @Override
            public String getStringValue(Frag_montazh_class nc, int position) {
                return nc.getPhone();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_montazh_class>() {
            @Override
            public String getStringValue(Frag_montazh_class nc, int position) {
                return nc.getClient();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), table_mas, R.layout.clients_item3,dict);

        ListView News_list = (ListView)view.findViewById(R.id.list_client);
        News_list.setAdapter(adapter);
    }
}