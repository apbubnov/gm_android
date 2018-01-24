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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_g2_price_poloten_class;
import ru.ejevikaapp.authorization.Class.Frag_g3_buhgalt_class;
import ru.ejevikaapp.authorization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_g3_buhgalt extends Fragment {

    ProgressDialog mProgressDialog;
    ArrayList<Frag_g3_buhgalt_class> table_mas = new ArrayList<>();


    public Frag_g3_buhgalt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Table().execute();
        return inflater.inflate(R.layout.frag_g3_buhgalt, container, false);
    }

    private class Table extends AsyncTask<Void, Void, Void> {
        Element table;

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

           //    Frag_g3_buhgalt_class n = new Frag_g3_buhgalt_class("666","666",
           //            "Ким Чен Ын", "121%");
           //    table_mas.add(n);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            clients();
            mProgressDialog.dismiss();
        }
    }

    void clients(){

        BindDictionary<Frag_g3_buhgalt_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_g3_buhgalt_class>() {
            @Override
            public String getStringValue(Frag_g3_buhgalt_class nc, int position) {
                return nc.getNumber();
            }
        });
        dict.addStringField(R.id.c_fio, new StringExtractor<Frag_g3_buhgalt_class>() {
            @Override
            public String getStringValue(Frag_g3_buhgalt_class nc, int position) {
                return nc.getFio();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_g3_buhgalt_class>() {
            @Override
            public String getStringValue(Frag_g3_buhgalt_class nc, int position) {
                return nc.getIncome();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), table_mas, R.layout.clients_item,dict);

        ListView News_list = (ListView)getActivity().findViewById(R.id.list_client);
        News_list.setAdapter(adapter);
    }

}
