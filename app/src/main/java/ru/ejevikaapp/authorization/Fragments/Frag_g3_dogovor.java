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

import org.jsoup.nodes.Element;

import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_g3_buhgalt_class;
import ru.ejevikaapp.authorization.Class.Frag_g3_dogovor_class;
import ru.ejevikaapp.authorization.R;


public class Frag_g3_dogovor extends Fragment {

    ProgressDialog mProgressDialog;
    ArrayList<Frag_g3_dogovor_class> table_mas = new ArrayList<>();

    public Frag_g3_dogovor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Table().execute();
        return inflater.inflate(R.layout.frag_g3_dogovor, container, false);
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            clients();
            mProgressDialog.dismiss();
        }
    }

    void clients(){

        BindDictionary<Frag_g3_dogovor_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.c_number, new StringExtractor<Frag_g3_dogovor_class>() {
            @Override
            public String getStringValue(Frag_g3_dogovor_class nc, int position) {
                return nc.getNumber();
            }
        });
        dict.addStringField(R.id.c_address, new StringExtractor<Frag_g3_dogovor_class>() {
            @Override
            public String getStringValue(Frag_g3_dogovor_class nc, int position) {
                return nc.getAddress();
            }
        });
        dict.addStringField(R.id.c_price, new StringExtractor<Frag_g3_dogovor_class>() {
            @Override
            public String getStringValue(Frag_g3_dogovor_class nc, int position) {
                return nc.getPrice();
            }
        });
        dict.addStringField(R.id.c_income, new StringExtractor<Frag_g3_dogovor_class>() {
            @Override
            public String getStringValue(Frag_g3_dogovor_class nc, int position) {
                return nc.getIncome();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), table_mas, R.layout.clients_item3,dict);

        ListView News_list = (ListView)getActivity().findViewById(R.id.list_client);
        News_list.setAdapter(adapter);
    }

}
