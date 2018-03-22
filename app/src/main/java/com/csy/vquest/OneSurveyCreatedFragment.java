package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class OneSurveyCreatedFragment extends Fragment {

    private static final String ARG_SURVEY_KEY = "survey_number";
    private static final String ARG_SURVEY = "survey";

    private int surveyKey;
    private SurveyBean surveyBean;
    private TextView surveyStringTextView;
    private TextView unameView;
    private TextView timeView;
    private TableRow optionRow3, optionRow4;
    private TextView votesView1, votesView2, votesView3, votesView4;
    private TextView totalVotesView, totalViewsView;

    private long votes1, votes2, votes3, votes4;
    private long totalVotes, totalViews;

    public OneSurveyCreatedFragment() {
    }

    public static OneSurveyCreatedFragment newInstance(String surveyKey, SurveyBean survey) {

        Bundle args = new Bundle();
        args.putInt(ARG_SURVEY_KEY, Integer.parseInt(surveyKey));
        args.putParcelable(ARG_SURVEY, survey);

        OneSurveyCreatedFragment fragment = new OneSurveyCreatedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_survey_created, container, false);

        surveyKey = getArguments().getInt(ARG_SURVEY_KEY);
        surveyBean = getArguments().getParcelable(ARG_SURVEY);

        surveyStringTextView = (TextView) view.findViewById(R.id.sstring_view);
        unameView = (TextView) view.findViewById(R.id.uname_view2);
        timeView = (TextView) view.findViewById(R.id.time_view2);
        optionRow3 = (TableRow) view.findViewById(R.id.row_option3);
        optionRow4 = (TableRow) view.findViewById(R.id.row_option4);
        votesView1 = (TextView) view.findViewById(R.id.tv_survey_votes1);
        votesView2 = (TextView) view.findViewById(R.id.tv_survey_votes2);
        votesView3 = (TextView) view.findViewById(R.id.tv_survey_votes3);
        votesView4 = (TextView) view.findViewById(R.id.tv_survey_votes4);
        totalVotesView = (TextView) view.findViewById(R.id.tv_survey_total_votes);
        totalViewsView = (TextView) view.findViewById(R.id.tv_survey_total_views);

        surveyStringTextView.setText(surveyBean.getSstring());
        unameView.setText(surveyBean.getUsername());

        Date date = new Date(surveyBean.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        timeView.setText(day + "/" + month + "/" + year);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference surveyVotesRef = rootRef.child("survey_votes").child(String.valueOf(surveyKey));
        surveyVotesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalVotes = 0;

                votes1 = (long) dataSnapshot.child("option1").getValue();
                votes2 = (long) dataSnapshot.child("option2").getValue();
                votes3 = (long) dataSnapshot.child("option3").getValue();
                votes4 = (long) dataSnapshot.child("option4").getValue();

                votesView1.setText(String.valueOf(votes1));
                votesView2.setText(String.valueOf(votes2));

                totalVotes += votes1 + votes2;

                if(votes3 > -1){
                    totalVotes += votes3;
                    optionRow3.setVisibility(View.VISIBLE);
                    votesView3.setText(String.valueOf(votes3));
                }
                if(votes4 > -1){
                    totalVotes += votes4;
                    optionRow4.setVisibility(View.VISIBLE);
                    votesView4.setText(String.valueOf(votes4));
                }

                totalVotesView.setText(String.valueOf(totalVotes));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference surveyViewsRef = rootRef.child("survey_views").child(String.valueOf(surveyKey));
        surveyViewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalViews = 0;
                if(dataSnapshot != null){
                    if(dataSnapshot.hasChildren()){
                        totalViews = dataSnapshot.getChildrenCount();
                    }
                }

                totalViewsView.setText(String.valueOf(totalViews));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
