package ru.ejevikaapp.authorization.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.ejevikaapp.authorization.Class.Frag_g2_price_poloten_class;
import ru.ejevikaapp.authorization.R;

public class Frag_g2_price_poloten extends Fragment {

    ProgressDialog mProgressDialog;
    ArrayList<Frag_g2_price_poloten_class> price_mas = new ArrayList<>();


    public Frag_g2_price_poloten() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Table().execute();
        return inflater.inflate(R.layout.frag_g2_price_poloten, container, false);
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
                doc = Jsoup.connect("http://test.gm-vrn.ru/index.php?option=com_gm_ceiling&view=canvases").get();
                table = doc.select("table").get(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                Frag_g2_price_poloten_class n = new Frag_g2_price_poloten_class(cols.get(0).text(), cols.get(1).text(),
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

        BindDictionary<Frag_g2_price_poloten_class> dict = new BindDictionary<>();

        dict.addStringField(R.id.id_p, new StringExtractor<Frag_g2_price_poloten_class>() {
            @Override
            public String getStringValue(Frag_g2_price_poloten_class nc, int position) {
                return nc.getId();
            }
        });
        dict.addStringField(R.id.name_p, new StringExtractor<Frag_g2_price_poloten_class>() {
            @Override
            public String getStringValue(Frag_g2_price_poloten_class nc, int position) {
                return nc.getName();
            }
        });
        dict.addStringField(R.id.textur_p, new StringExtractor<Frag_g2_price_poloten_class>() {
            @Override
            public String getStringValue(Frag_g2_price_poloten_class nc, int position) {
                return nc.getTextur();
            }
        });
        dict.addStringField(R.id.price_p, new StringExtractor<Frag_g2_price_poloten_class>() {
            @Override
            public String getStringValue(Frag_g2_price_poloten_class nc, int position) {
                return nc.getPrice();
            }
        });

        FunDapter adapter = new FunDapter(getActivity(), price_mas, R.layout.price_item,dict);

        ListView News_list = (ListView)getActivity().findViewById(R.id.list_price);
        News_list.setAdapter(adapter);
    }
}
