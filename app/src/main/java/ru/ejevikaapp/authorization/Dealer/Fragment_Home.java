package ru.ejevikaapp.authorization.Dealer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ru.ejevikaapp.authorization.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment {

    View view;
    ImageButton imageButton;

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
        String avatar_user ="";
        try {
            SharedPreferences SP_end = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
            avatar_user= SP_end.getString("", "");
        }catch (Exception e){
        }

        imageButton = (ImageButton) view.findViewById(R.id.avatar);
        if (avatar_user.length()>10) {
            fromBase64(avatar_user);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        return view;
    }

    public void fromBase64(String imag) {

        byte[] decodedString = Base64.decode(imag, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Bitmap bmHalf = Bitmap.createScaledBitmap(decodedByte, decodedByte.getWidth(),
                decodedByte.getHeight(), false);

        imageButton.setImageBitmap(bmHalf);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageButton imageView = (ImageButton) view.findViewById(R.id.avatar);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);

                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte [] b=baos.toByteArray();
                    String temp=Base64.encodeToString(b, Base64.DEFAULT);

                    SharedPreferences SP = getActivity().getSharedPreferences("avatar_user", MODE_PRIVATE);
                    SharedPreferences.Editor ed = SP.edit();
                    ed.putString("", temp);
                    ed.commit();

                }
        }
    }

}
