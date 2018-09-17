package ru.ejevikaapp.gm_android.Dealer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ru.ejevikaapp.gm_android.ActivityPrice;
import ru.ejevikaapp.gm_android.Activity_zamer;
import ru.ejevikaapp.gm_android.Class.HelperClass;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment implements View.OnClickListener {

    View view;
    public TextView name_org;
    Button sum_users;

    DBHelper dbHelper;
    SQLiteDatabase db;

    String user_id;

    public Fragment_Home() {
        // Required empty public constructor
    }

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onResume() {
        super.onResume();

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();

        String avatar_user = "";
        try {
            SharedPreferences SP_end = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
            avatar_user = SP_end.getString("", "");
        } catch (Exception e) {
        }

        SharedPreferences SP_end = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = SP_end.getString("", "");

        TextView txt_count_zamer = (TextView) view.findViewById(R.id.count_zamer);
        TextView txt_count_mount = (TextView) view.findViewById(R.id.count_mount);

        sum_users = (Button) view.findViewById(R.id.sum_users);
        sum_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySumUsers.class);
                startActivity(intent);
            }
        });

        int count_zamer = 0;
        int count_mount = 0;

        String sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_clients " +
                "where dealer_id = ? ";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    if (HelperClass.associated_client(getActivity(), user_id, id_client)) {

                    } else {

                        sqlQuewy = "SELECT project_info, project_status "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where client_id = ? and project_status = 1";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    count_zamer++;
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();
                    }
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_zamer > 0) {
            txt_count_zamer.setVisibility(View.VISIBLE);
            txt_count_zamer.setText(String.valueOf(count_zamer));
        }

        sqlQuewy = "SELECT _id "
                + "FROM rgzbn_gm_ceiling_clients " +
                "where dealer_id = ? ";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id_client = c.getString(c.getColumnIndex(c.getColumnName(0)));
                    if (HelperClass.associated_client(getActivity(), user_id, id_client)) {

                    } else {
                        sqlQuewy = "SELECT project_info, project_status "
                                + "FROM rgzbn_gm_ceiling_projects " +
                                "where client_id = ? and (project_status = 10 or project_status = 5 or project_status = 4)";
                        Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                        if (cc != null) {
                            if (cc.moveToFirst()) {
                                do {
                                    count_mount++;
                                } while (cc.moveToNext());
                            }
                        }
                        cc.close();
                    }
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_mount > 0) {
            txt_count_mount.setVisibility(View.VISIBLE);
            txt_count_mount.setText(String.valueOf(count_mount));
        }

        ImageView ava = (ImageView) view.findViewById(R.id.ava);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(avatar_user));
            ava.setImageBitmap(bitmap);
        } catch (IOException e) {
            // ava.setBackgroundResource(R.drawable.it_c);
        }

        Typeface fontAwesomeFont = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        Button btn_client = (Button) view.findViewById(R.id.btn_client);
        btn_client.setTypeface(fontAwesomeFont);
        Button btn_zamer = (Button) view.findViewById(R.id.btn_zamer);
        btn_zamer.setTypeface(fontAwesomeFont);
        Button btn_install = (Button) view.findViewById(R.id.btn_install);
        btn_install.setTypeface(fontAwesomeFont);
        Button btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
        btn_add_zamer.setTypeface(fontAwesomeFont);
        Button price = (Button) view.findViewById(R.id.price);
        price.setTypeface(fontAwesomeFont);
        Button analytics = (Button) view.findViewById(R.id.analytics);
        analytics.setTypeface(fontAwesomeFont);
        Button btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_call.setTypeface(fontAwesomeFont);

        String user_name = "";

        sqlQuewy = "SELECT name "
                + "FROM rgzbn_users " +
                "where _id = ?";
        c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    user_name = c.getString(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();

        name_org = (TextView) view.findViewById(R.id.name_org);
        name_org.setText(user_name);

        btn_client.setOnClickListener(this);
        btn_zamer.setOnClickListener(this);
        btn_install.setOnClickListener(this);
        btn_add_zamer.setOnClickListener(this);
        price.setOnClickListener(this);
        analytics.setOnClickListener(this);
        btn_call.setOnClickListener(this);

        sum_us();
    }

    void sum_us(){

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        float sum = 0;
        String sqlQuewy = "SELECT sum "
                + "FROM rgzbn_gm_ceiling_recoil_map_project " +
                "where recoil_id = ?";
        Cursor c = db.rawQuery(sqlQuewy, new String[]{user_id});
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    sum += c.getFloat(c.getColumnIndex(c.getColumnName(0)));
                } while (c.moveToNext());
            }
        }
        c.close();
        sum_users.setText(sum+ " руб.");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_zamer:
                Intent intent = new Intent(getActivity(), Activity_zamer.class);
                startActivity(intent);
                break;
            case R.id.btn_client:
                intent = new Intent(getActivity(), Activity_client.class);
                startActivity(intent);
                break;
            case R.id.btn_zamer:
                intent = new Intent(getActivity(), Activity_empty.class);
                startActivity(intent);
                break;
            case R.id.btn_install:
                intent = new Intent(getActivity(), Activity_mounting.class);
                startActivity(intent);
                break;
            case R.id.price:
                intent = new Intent(getActivity(), ActivityPrice.class);
                startActivity(intent);
                break;
            case R.id.btn_call:
                intent = new Intent(getActivity(), CallBack.class);
                startActivity(intent);
                break;
            case R.id.analytics:
                if (HelperClass.isOnline(getActivity())) {
                    intent = new Intent(getActivity(), Activity_analytics.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Проверьте подключение к интернету, или возможны работы на сервере", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

}