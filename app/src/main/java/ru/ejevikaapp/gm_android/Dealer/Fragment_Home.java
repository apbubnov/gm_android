package ru.ejevikaapp.gm_android.Dealer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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

import java.io.IOException;

import ru.ejevikaapp.gm_android.ActivityPrice;
import ru.ejevikaapp.gm_android.Activity_zamer;
import ru.ejevikaapp.gm_android.DBHelper;
import ru.ejevikaapp.gm_android.R;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment implements View.OnClickListener {

    View view;
    public TextView name_org;

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

        Log.d("Fragment 1", "onResume");

        String avatar_user = "";
        try {
            SharedPreferences SP_end = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
            avatar_user = SP_end.getString("", "");
        } catch (Exception e) {
        }

        SharedPreferences SP_end = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

        Log.d("mLog", user_id);

        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        TextView txt_count_zamer = (TextView) view.findViewById(R.id.count_zamer);
        TextView txt_count_mount = (TextView) view.findViewById(R.id.count_mount);

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

                    sqlQuewy = "SELECT project_info, project_status "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where client_id = ? and project_status = 1";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                count_zamer ++;
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_zamer>0){
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

                    sqlQuewy = "SELECT project_info, project_status "
                            + "FROM rgzbn_gm_ceiling_projects " +
                            "where client_id = ? and (project_status = 10 or project_status = 5 or project_status = 4)";
                    Cursor cc = db.rawQuery(sqlQuewy, new String[]{id_client});
                    if (cc != null) {
                        if (cc.moveToFirst()) {
                            do {
                                count_mount ++;
                            } while (cc.moveToNext());
                        }
                    }
                    cc.close();
                } while (c.moveToNext());
            }
        }
        c.close();

        if (count_mount>0){
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

        Button btn_client = (Button) view.findViewById(R.id.btn_client);
        Button btn_zamer = (Button) view.findViewById(R.id.btn_zamer);
        Button btn_install = (Button) view.findViewById(R.id.btn_install);
        Button btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
        Button price = (Button) view.findViewById(R.id.price);

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
        }
    }

}