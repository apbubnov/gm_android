package ru.ejevikaapp.gm_android.Dealer;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.Class.Select_work;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

public class Activity_price_mounting extends AppCompatActivity {

    ArrayList<Select_work> sel_work = new ArrayList<>();
    DBHelper dbHelper;
    SQLiteDatabase db;

    Double str_distance, str_transport, mp, str_canvases_margin, str_components_margin, str_mounting_margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_mounting);

        SharedPreferences SP_end = getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        ListView listView = (ListView) findViewById(R.id.List_mounting);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        double dealer_mounting_margin = 0.0;
        String sqlQuewy = "select dealer_mounting_margin "
                + "FROM rgzbn_gm_ceiling_dealer_info " +
                "where dealer_id = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                dealer_mounting_margin = c.getDouble(c.getColumnIndex(c.getColumnName(0)));
            }
        }
        c.close();

        sqlQuewy = "SELECT mp1, mp2, mp3, mp4, mp5, mp6, mp7, mp8, mp9, mp10, mp11, mp12, mp13, mp14, mp15, mp16, mp17, " +
                "mp18, mp19, mp22, mp23, mp24, mp25, mp26, mp27, mp30, mp31, mp32, mp33, mp34, mp36, mp37, mp38, mp40, mp41, mp42, mp43, transport, distance "
                + "FROM rgzbn_gm_ceiling_mount" +
                " WHERE user_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});         // заполняем массивы из таблицы
        if (c != null) {
            if (c.moveToFirst()) {
                mp = c.getDouble(c.getColumnIndex(c.getColumnName(0)));
                double price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Монтаж", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(1)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Люстра планочная", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(2)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Люстра большая", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(3)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка светильников", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(4)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Светильники квадратные", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(5)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Пожарная сигнализация", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(6)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Обвод трубы D > 100мм", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(7)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Обвод трубы D < 100мм", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(8)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Брус-разделитель, брус-отбойник", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(9)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Вставка", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(10)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Шторный карниз на полотно", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(11)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка электровытяжки", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(12)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Крепление в плитку", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(13)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Крепление в керамогранит", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(14)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Усиление стен", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(15)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка вентиляции", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(16)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Сложность доступа", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(17)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Дополнительный монтаж", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(18)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка диффузора", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(19)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Внутренний вырез для ПВХ", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(20)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Переход уровня по прямой", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(21)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Переход уровня по кривой", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(22)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Переход уровня по прямой с нишей", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(23)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Переход уровня по кривой с нишей", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(24)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Слив воды", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(25)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Парящий потолок", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(26)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Монтаж (потолочный багет)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(27)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Парящий потолок", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(28)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Монтаж (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(29)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка люстры (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(30)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка светильников (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(31)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Светильники квадратные (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(32)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Пожарная сигнализация (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(33)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Обвод трубы D < 100мм (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(34)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Шторный карниз на полотно (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(35)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Установка вентиляции (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(36)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Обработка каждого угла (ткань)", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(37)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Транспортные расходы", String.valueOf(price2)));

                mp = c.getDouble(c.getColumnIndex(c.getColumnName(38)));
                price2 = mp * 100 / (100 - dealer_mounting_margin);
                sel_work.add(new Select_work(null, String.valueOf(mp), "", "Выезд за город", String.valueOf(price2)));

            }
        }
        c.close();

        BindDictionary<Select_work> dict = new BindDictionary<>();
        dict.addStringField(R.id.title, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getName();
            }
        });
        dict.addStringField(R.id.price, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getTime();
            }
        });
        dict.addStringField(R.id.price2, new StringExtractor<Select_work>() {
            @Override
            public String getStringValue(Select_work nc, int position) {
                return nc.getN5();
            }
        });

        final FunDapter adapter = new FunDapter(this, sel_work, R.layout.price_mounting_helper, dict);
        listView.setAdapter(adapter);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}