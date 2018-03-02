package ru.ejevikaapp.gm_android.Manager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

import static android.content.Context.MODE_PRIVATE;

public class Frag_general_in_production extends Fragment implements View.OnClickListener {

    TextView id_proj, dealer_cl;
    Calendar dateAndTime = Calendar.getInstance();
    TextView name_cl;
    TextView contact_cl;
    TextView address_cl;
    TextView notes_cl;
    TextView final_amount;

    String SAVED_ID="", id_cl, phone, fio, pro_info, calc_date, dealer_id, date, item, S, P;

    Button btn_start;

    DBHelper dbHelper;
    View view;

    TextView DateTime, S_and_P;

    LinearLayout.LayoutParams textViewParams;
    LinearLayout mainL, mainL2, mainL3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frag_general_in_production, container, false);

        mainL = (LinearLayout) view.findViewById(R.id.layout_info);
        mainL2 = (LinearLayout) view.findViewById(R.id.layout_info2);
        mainL3 = (LinearLayout) view.findViewById(R.id.layout_info3);

        textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        SharedPreferences SPI = this.getActivity().getSharedPreferences("id_cl", MODE_PRIVATE);
        id_cl = SPI.getString(SAVED_ID,"");

        id_proj = (TextView)view.findViewById(R.id.id_proj);
        id_proj.setText( "Информация по проекту № " + id_cl);

        name_cl = (TextView) view.findViewById(R.id.name_cl);
        contact_cl = (TextView)view.findViewById(R.id.contact_cl);
        address_cl = (TextView)view.findViewById(R.id.address_cl);
        DateTime = (TextView) view.findViewById(R.id.data_cl);
        dealer_cl = (TextView)view.findViewById(R.id.dealer_cl);
        notes_cl = (TextView)view.findViewById(R.id.notes_cl);
        S_and_P = (TextView)view.findViewById(R.id.S_and_P);
        final_amount = (TextView)view.findViewById(R.id.final_amount);

        btn_start  = (Button)view.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuewy = "SELECT client_name "
                + "FROM rgzbn_gm_ceiling_clients" +
                " WHERE _id = ?";

        Cursor c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    fio = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    name_cl.setText(fio);
                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT phone "
                + "FROM rgzbn_gm_ceiling_clients_contacts" +
                " WHERE client_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    phone = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    contact_cl.setText(phone);

                } while (c.moveToNext());
            }
        }
        c.close();

        double sum =0;
        int discount =0;

        sqlQuewy = "SELECT project_info, project_calculation_date, dealer_id, project_note, project_discount, project_sum "
                + "FROM rgzbn_gm_ceiling_projects" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    pro_info = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    address_cl.setText(pro_info);

                    calc_date = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    DateTime.setText(calc_date);

                    dealer_id = c.getString(c.getColumnIndex(c.getColumnName(2)));

                    String note = c.getString(c.getColumnIndex(c.getColumnName(3)));
                    notes_cl.setText(note);

                    try {
                        discount = Integer.valueOf(c.getString(c.getColumnIndex(c.getColumnName(4))));
                    } catch (Exception e){
                        discount = 0;
                    }

                    try {
                        sum = Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(5))));
                    } catch (Exception e){
                        sum = 0;
                    }


                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT name "
                + "FROM rgzbn_users" +
                " WHERE _id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{dealer_id});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String dealer_name = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    dealer_cl.setText(dealer_name);

                } while (c.moveToNext());
            }
        }
        c.close();

        sqlQuewy = "SELECT n4, n5 "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";

        c = db.rawQuery(sqlQuewy, new String[]{id_cl});

        double s = 0.0;
        double p = 0.0;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    S = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    P = c.getString(c.getColumnIndex(c.getColumnName(1)));
                    s += Double.parseDouble(S);
                    p += Double.parseDouble(P);
                } while (c.moveToNext());
            }
        }
        c.close();


       // if (discount > 0){
       //     final_amount_disc.setText("Итого/ \n" + discount + "%");
       //     final_amount.setText(String.valueOf(sum) + "/ \n" + String.valueOf((Math.round(sum - (sum / 100 * discount))* 100.0)/100));
       // } else {
       //     final_amount.setText(final_amount.getText().toString() + " " + String.valueOf(sum));
       // }

       // S_and_P.setText(Math.round(s * 100.0) / 100.0 + " / \n" + Math.round(p * 100.0) / 100.0);

       // Toast toast = Toast.makeText(getActivity().getApplicationContext(),
       //         id_cl, Toast.LENGTH_SHORT);
       // toast.show();

        sqlQuewy = "SELECT  calculation_title, canvases_sum "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_cl});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String title = c.getString(c.getColumnIndex(c.getColumnName(0))) + " " + Math.round(Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(1))))* 100.0) / 100.0;
                    textv(title);
                } while (c.moveToNext());
            }
        }
        c.close();

        double title = 0.0;
        sqlQuewy = "SELECT  components_sum "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_cl});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    title += Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
                } while (c.moveToNext());
            }
        }
        c.close();

        textv3(String.valueOf(title));

        sqlQuewy = "SELECT  calculation_title, mounting_sum "
                + "FROM rgzbn_gm_ceiling_calculations" +
                " WHERE project_id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{id_cl});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String title1 = c.getString(c.getColumnIndex(c.getColumnName(0))) + " " + c.getString(c.getColumnIndex(c.getColumnName(1)));
                    textv2(title1);
                } while (c.moveToNext());
            }
        }
        c.close();

        return view;
    }

    void textv(String text){

        TextView txtV = new TextView(getActivity());
        txtV.setText(text + " руб.");
        txtV.setLayoutParams(textViewParams);
        mainL.addView(txtV);
    }

    void textv2(String text){

        TextView txtV = new TextView(getActivity());
        txtV.setText(text + " руб.");
        txtV.setLayoutParams(textViewParams);
        mainL2.addView(txtV);
    }

    void textv3(String text){

        TextView txtV = new TextView(getActivity());
        txtV.setText(text + " руб." );
        txtV.setLayoutParams(textViewParams);
        mainL3.addView(txtV);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_PROJECT_STATUS, "10");
                db.update(DBHelper.TABLE_RGZBN_GM_CEILING_PROJECTS, values, "_id = ?", new String[]{id_cl});
                getActivity().finish();

                break;
        }
    }
}