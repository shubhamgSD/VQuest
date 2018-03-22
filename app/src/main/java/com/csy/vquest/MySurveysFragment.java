package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

public class MySurveysFragment extends Fragment implements View.OnClickListener {

    private TextView surveysCreatedView, surveysVotedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("My Surveys");

        View view = inflater.inflate(R.layout.fragment_my_surveys, container, false);

        surveysCreatedView = (TextView) view.findViewById(R.id.tv_surveys_created);
        surveysVotedView = (TextView) view.findViewById(R.id.tv_surveys_voted);

        surveysCreatedView.setOnClickListener(this);
        surveysVotedView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment;

        switch(v.getId()){
            case R.id.tv_surveys_created:
                fragment = new MySurveyCreatedFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "my_survey_created_fragment")
                        .addToBackStack("mysurveycreatedfragment")
                        .commit();
                break;

            case R.id.tv_surveys_voted:
        }

    }
}
