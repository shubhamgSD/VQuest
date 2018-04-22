package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneSurveyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneSurveyFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SURVEY_KEY = "survey_number";
    private static final String ARG_SURVEY = "survey";
    private static final String ARG_SURVEY_COUNT = "survey_count";
    private int surveyKey, surveyCount;
    private SurveyBean surveyBean;
    private ViewPager viewPager;
    private View view;
    private RadioButton checkedRadioBtn;
    private TextView surveyStringTextView, unameView, timeView;
    private RadioButton optionView1, optionView2, optionView3, optionView4;
    private RadioGroup optionGroup;
    private Button continueBtn;
    private boolean radioChecked = false;
    private DatabaseReference rootRef, surveyVotesRef, surveyDoneRef;
    private String firebaseUser;
    private long votes1, votes2, votes3, votes4;

    private final static String NULL_VALUE_STRING = "__NULL__";

    public OneSurveyFragment() {
    }

    public static OneSurveyFragment newInstance(String surveyKey, SurveyBean survey, int surveyCount) {
        OneSurveyFragment fragment = new OneSurveyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SURVEY_KEY, Integer.parseInt(surveyKey));
        args.putParcelable(ARG_SURVEY, survey);
        args.putInt(ARG_SURVEY_COUNT, surveyCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one_survey, container, false);
        surveyKey = getArguments().getInt(ARG_SURVEY_KEY);
        surveyBean = getArguments().getParcelable(ARG_SURVEY);
        surveyCount = getArguments().getInt(ARG_SURVEY_COUNT);
        surveyStringTextView = (TextView) view.findViewById(R.id.sstring_view);
        unameView = (TextView) view.findViewById(R.id.uname_view2);
        timeView = (TextView) view.findViewById(R.id.time_view2);
        continueBtn = (Button) view.findViewById(R.id.survey_continue_button);

        optionGroup = (RadioGroup) view.findViewById(R.id.survey_radio_group);
        optionView1 = (RadioButton) view.findViewById(R.id.survey_option_1);
        optionView2 = (RadioButton) view.findViewById(R.id.survey_option_2);
        optionView3 = (RadioButton) view.findViewById(R.id.survey_option_3);
        optionView4 = (RadioButton) view.findViewById(R.id.survey_option_4);

        viewPager = (ViewPager) container.getRootView().findViewById(R.id.survey_view_pager);

        surveyStringTextView.setText(surveyBean.getSstring());
        unameView.setText(surveyBean.getUsername());

        Date date = new Date(surveyBean.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        timeView.setText(day + "/" + month + "/" + year);

        final String option1 = surveyBean.getOption1();
        String option2 = surveyBean.getOption2();
        String option3 = surveyBean.getOption3();
        String option4 = surveyBean.getOption4();

        optionView1.setText(option1);
        optionView2.setText(option2);

        if (option3.equals(NULL_VALUE_STRING)) {
            optionView3.setVisibility(View.GONE);
        } else {
            optionView3.setText(option3);
            optionView3.setVisibility(View.VISIBLE);
        }
        if (option4.equals(NULL_VALUE_STRING)) {
            optionView4.setVisibility(View.GONE);
        } else {
            optionView4.setText(option4);
            optionView4.setVisibility(View.VISIBLE);
        }

        optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioChecked = true;

            }
        });

        continueBtn.setOnClickListener(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference surveyViewRef = rootRef.child("survey_views").child(String.valueOf(surveyKey));
        surveyViewRef.child(firebaseUser).setValue(0);

        surveyVotesRef = rootRef.child("survey_votes").child(String.valueOf(surveyKey));
        surveyVotesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                votes1 = (long) dataSnapshot.child("option1").getValue();
                votes2 = (long) dataSnapshot.child("option2").getValue();
                votes3 = (long) dataSnapshot.child("option3").getValue();
                votes4 = (long) dataSnapshot.child("option4").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        surveyDoneRef = rootRef.child("survey_done").child(String.valueOf(surveyKey));
        surveyDoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){
                    if(dataSnapshot.hasChild(firebaseUser)){
                        continueBtn.setEnabled(false);
                        optionView1.setEnabled(false);
                        optionView2.setEnabled(false);
                        optionView3.setEnabled(false);
                        optionView4.setEnabled(false);

                        long option = (long) dataSnapshot.child(firebaseUser).getValue();
                        if (option == 1) {
                            optionView1.setEnabled(true);
                            optionView1.setChecked(true);

                        } else if (option == 2) {
                            optionView2.setEnabled(true);
                            optionView2.setChecked(true);

                        } else if (option == 3) {
                            optionView3.setEnabled(true);
                            optionView3.setChecked(true);

                        } else if (option == 4) {
                            optionView4.setEnabled(true);
                            optionView4.setChecked(true);

                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.survey_continue_button) {

            if (radioChecked) {

                checkedRadioBtn = (RadioButton) view.findViewById(optionGroup.getCheckedRadioButtonId());

                String displayMessage = "Successfully voted for \"" + checkedRadioBtn.getText().toString() + "\"";

                switch (optionGroup.getCheckedRadioButtonId()) {
                    case R.id.survey_option_1:
                        surveyDoneRef.child(firebaseUser).setValue(1);
                        surveyVotesRef.child("option1").setValue(votes1 + 1);
                        break;
                    case R.id.survey_option_2:
                        surveyDoneRef.child(firebaseUser).setValue(2);
                        surveyVotesRef.child("option2").setValue(votes2 + 1);
                        break;
                    case R.id.survey_option_3:
                        surveyDoneRef.child(firebaseUser).setValue(3);
                        surveyVotesRef.child("option3").setValue(votes3 + 1);
                        break;
                    case R.id.survey_option_4:
                        surveyDoneRef.child(firebaseUser).setValue(4);
                        surveyVotesRef.child("option4").setValue(votes4 + 1);
                        break;
                }

                Toast.makeText(getContext(), displayMessage, Toast.LENGTH_SHORT).show();

            }
            Log.d("Log", viewPager.getCurrentItem()+" "+surveyCount);
            viewPager.setCurrentItem(surveyCount, true);

        }

    }
}
