package ru.ejevikaapp.gm_android.Crew;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ejevikaapp.gm_android.R;

public class Fragment_mounting_info extends Fragment {


    public Fragment_mounting_info() {
        // Required empty public constructor
    }

    public static Fragment_mounting_info newInstance(int page) {
        Fragment_mounting_info fragment = new Fragment_mounting_info();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_mounting_info, container, false);
    }

}
