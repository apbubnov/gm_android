package ru.ejevikaapp.authorization.Dealer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ejevikaapp.authorization.Fragments.Activity_calcul;
import ru.ejevikaapp.authorization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_for_calc extends Fragment {


    public Fragment_for_calc() {
        // Required empty public constructor
    }

    public static Fragment_for_calc newInstance() {
        return new Fragment_for_calc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Intent intent = new Intent(getActivity(), Activity_calcul.class);
        startActivity(intent);

        return inflater.inflate(R.layout.fragment_for_calc, container, false);
    }

}
