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

import ru.ejevikaapp.authorization.Class.Frag_g2_price_montazh_class;
import ru.ejevikaapp.authorization.Class.Frag_g2_price_poloten_class;
import ru.ejevikaapp.authorization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_g2_price_montazh extends Fragment {


    ProgressDialog mProgressDialog;
    ArrayList<Frag_g2_price_montazh_class> price_mas = new ArrayList<>();


    public Frag_g2_price_montazh() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Frag_g2_price_montazh.Table().execute();
        return inflater.inflate(R.layout.fragment_frag_g2_price_montazh, container, false);
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

            Document doc = null;
            try {
                doc = Jsoup.connect("http://test.gm-vrn.ru/index.php?option=com_gm_ceiling&view=mount").get();
                table = doc.select("table").get(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                Frag_g2_price_montazh_class n = new Frag_g2_price_montazh_class(cols.get(0).text(), cols.get(1).text(),
                        cols.get(2).text(), cols.get(3).text());
                price_mas.add(n);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            cat();
            mProgressDialog.dismiss();
        }
    }
    void cat(){

        BindDictionary<Frag_g2_price_montazh_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.id_p, new StringExtractor<Frag_g2_price_montazh_class>() {
            @Override
            public String getStringValue(Frag_g2_price_montazh_class nc, int position) {
                return nc.getId();
            }
        });
        dict.addStringField(R.id.name_p, new StringExtractor<Frag_g2_price_montazh_class>() {
            @Override
            public String getStringValue(Frag_g2_price_montazh_class nc, int position) {
                return nc.getName();
            }
        });
        dict.addStringField(R.id.textur_p, new StringExtractor<Frag_g2_price_montazh_class>() {
            @Override
            public String getStringValue(Frag_g2_price_montazh_class nc, int position) {
                return nc.getPrice1();
            }
        });
        dict.addStringField(R.id.price_p, new StringExtractor<Frag_g2_price_montazh_class>() {
            @Override
            public String getStringValue(Frag_g2_price_montazh_class nc, int position) {
                return nc.getPrice2();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), price_mas, R.layout.price_item,dict);

        ListView News_list = (ListView)getActivity().findViewById(R.id.list_price);
        News_list.setAdapter(adapter);
    }
}
