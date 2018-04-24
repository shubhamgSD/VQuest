package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timing extends Fragment {

    private EditText subOne;

    public Timing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timing, container, false);

        subOne = (EditText) view.findViewById(R.id.sub_one);
        subOne.setEnabled(false);
        return view;

    }

}
