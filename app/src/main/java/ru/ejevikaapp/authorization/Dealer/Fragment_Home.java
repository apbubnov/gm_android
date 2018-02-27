package ru.ejevikaapp.authorization.Dealer;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.ShapeImageView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.pixplicity.sharp.Sharp;
import com.pixplicity.sharp.SharpPicture;

import org.w3c.dom.Text;

import java.io.IOException;

import ru.ejevikaapp.authorization.Activity_zamer;
import ru.ejevikaapp.authorization.BuildConfig;
import ru.ejevikaapp.authorization.DBHelper;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment implements View.OnClickListener {

    View view;
    EditText name_org;

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
        String avatar_user = "";
        try {
            SharedPreferences SP_end = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
            avatar_user = SP_end.getString("", "");
        } catch (Exception e) {
        }

        SharedPreferences SP_end = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id = SP_end.getString("", "");

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
                            "where client_id = ? and (project_status = 10 or project_status = 5) or project_status = 4";
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
            ava.setImageResource(R.raw.gm_hd);
        }

        ava.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        Button btn_client = (Button) view.findViewById(R.id.btn_client);
        Button btn_zamer = (Button) view.findViewById(R.id.btn_zamer);
        Button btn_install = (Button) view.findViewById(R.id.btn_install);
        Button btn_add_zamer = (Button) view.findViewById(R.id.btn_add_zamer);
        Button btn_save_name = (Button) view.findViewById(R.id.btn_save_name);

        name_org = (EditText) view.findViewById(R.id.name_org);
        SP_end = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
        String user_name = SP_end.getString("", "");
        name_org.setText(user_name);

        btn_client.setOnClickListener(this);
        btn_zamer.setOnClickListener(this);
        btn_install.setOnClickListener(this);
        btn_add_zamer.setOnClickListener(this);
        btn_save_name.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            case R.id.btn_save_name:

                SharedPreferences SP = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
                final String user_id = SP.getString("", "");

                DBHelper dbHelper = new DBHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //values.put(DBHelper.KEY_MIN_SUM, userInput.getText().toString());  тут будет пароль
                values.put(DBHelper.KEY_NAME, name_org.getText().toString());
                db.update(DBHelper.TABLE_USERS, values, "_id = ?",
                        new String[]{user_id});

                SP = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
                SharedPreferences.Editor ed = SP.edit();
                ed.putString("", String.valueOf(name_org.getText()));
                ed.commit();

                values = new ContentValues();
                values.put(DBHelper.KEY_ID_OLD, user_id);
                values.put(DBHelper.KEY_ID_NEW, "0");
                values.put(DBHelper.KEY_NAME_TABLE, "rgzbn_users");
                values.put(DBHelper.KEY_SYNC, "0");
                values.put(DBHelper.KEY_TYPE, "send");
                values.put(DBHelper.KEY_STATUS, "1");
                db.insert(DBHelper.HISTORY_SEND_TO_SERVER, null, values);

                getActivity().startService(new Intent(getActivity(), Service_Sync.class));

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;

        ImageView ava = (ImageView) view.findViewById(R.id.ava);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ava.setImageBitmap(bitmap);

                     SharedPreferences SP = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
                     SharedPreferences.Editor ed = SP.edit();
                     ed.putString("", String.valueOf(selectedImage));
                     ed.commit();

                }
        }
    }
}