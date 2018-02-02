package ru.ejevikaapp.authorization.Dealer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ru.ejevikaapp.authorization.Activity_inform_proj;
import ru.ejevikaapp.authorization.Fragments.Frag_spisok;
import ru.ejevikaapp.authorization.MainActivity;
import ru.ejevikaapp.authorization.R;
import ru.ejevikaapp.authorization.Service_Sync;
import ru.ejevikaapp.authorization.Service_Sync_Import;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment implements View.OnClickListener {

    View view;
    ImageButton imageButton;
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

        ImageView ava = (ImageView) view.findViewById(R.id.ava);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(avatar_user));
            ava.setImageBitmap(bitmap);
        } catch (IOException e) {
            ava.setImageResource(R.mipmap.ic_app);
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

        name_org = (EditText) view.findViewById(R.id.name_org);
        SharedPreferences SP_end = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
        String user_name = SP_end.getString("", "");
        name_org.setText(user_name);

        btn_client.setOnClickListener(this);
        btn_zamer.setOnClickListener(this);
        btn_install.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences SP = getActivity().getSharedPreferences("name_user", MODE_PRIVATE);
        SharedPreferences.Editor ed = SP.edit();
        ed.putString("", String.valueOf(name_org.getText()));
        ed.commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_client:
                Intent intent = new Intent(getActivity(), Activity_client.class);
                startActivity(intent);
                break;
            case R.id.btn_zamer:
                intent = new Intent(getActivity(), Activity_empty.class);
                startActivity(intent);
                break;
            case R.id.btn_install:
                intent = new Intent(getActivity(), Activity_spisok_brigade.class);
                startActivity(intent);
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