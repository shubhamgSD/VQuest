package com.csy.vquest;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Arrays;

import static com.csy.vquest.NavigationDrawerActivity.current_uname;


public class CreateSurveyFragment extends Fragment implements View.OnClickListener {

    private int options = 2;
    private long nSurveys;
    private String dept, deg, year;

    private EditText surveyQuestionText, optionText1, optionText2, optionText3, optionText4;
    private CardView optionCard3, optionCard4;
    private Button publishButton;
    private Spinner deptSpinner, degSpinner, yearSpinner;

    private DatabaseReference rootRef;

    public CreateSurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_survey, container, false);

        getActivity().setTitle("Create Survey");

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.survey_option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                options = Integer.parseInt(parent.getItemAtPosition(position).toString());

                if (options == 2) {
                    optionCard3.setVisibility(View.GONE);
                    optionCard4.setVisibility(View.GONE);
                    Log.d("2", String.valueOf(optionText3.getVisibility()) + " " + optionText3.isShown());
                } else if (options == 3) {
                    optionCard3.setVisibility(View.VISIBLE);
                    optionCard4.setVisibility(View.GONE);
                    Log.d("3", String.valueOf(optionText3.getVisibility()) + " " + optionText3.isShown());
                } else if (options == 4) {
                    optionCard3.setVisibility(View.VISIBLE);
                    optionCard4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                options = 2;
            }
        });

        surveyQuestionText = (EditText) view.findViewById(R.id.input_survey_question);
        optionText1 = (EditText) view.findViewById(R.id.input_option_1);
        optionText2 = (EditText) view.findViewById(R.id.input_option_2);
        optionText3 = (EditText) view.findViewById(R.id.input_option_3);
        optionText4 = (EditText) view.findViewById(R.id.input_option_4);

        optionCard3 = (CardView) view.findViewById(R.id.option_card_3);
        optionCard4 = (CardView) view.findViewById(R.id.option_card_4);

        deptSpinner = (Spinner) view.findViewById(R.id.spinner_dept);
        degSpinner = (Spinner) view.findViewById(R.id.spinner_deg);
        yearSpinner = (Spinner) view.findViewById(R.id.spinner_year);

        publishButton = (Button) view.findViewById(R.id.publish_btn);
        publishButton.setOnClickListener(this);

        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.dept_all_array,
                android.R.layout.simple_spinner_item);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(deptAdapter);

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dept = parent.getItemAtPosition(0).toString();
            }
        });

        ArrayAdapter<CharSequence> degAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.deg_all_array,
                android.R.layout.simple_spinner_item);
        degAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degSpinner.setAdapter(degAdapter);

        degSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deg = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deg = parent.getItemAtPosition(0).toString();
            }
        });

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.year_all_array,
                android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = parent.getItemAtPosition(0).toString();
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("survey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nSurveys = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        options = Integer.parseInt(parent.getItemAtPosition(position).toString());

        if (options == 2) {
            optionCard3.setVisibility(View.GONE);
            optionCard4.setVisibility(View.GONE);
            Log.d("2", String.valueOf(optionText3.getVisibility()) + " " + optionText3.isShown());
        } else if (options == 3) {
            optionCard3.setVisibility(View.VISIBLE);
            optionCard4.setVisibility(View.GONE);
            Log.d("3", String.valueOf(optionText3.getVisibility()) + " " + optionText3.isShown());
        } else if (options == 4) {
            optionCard3.setVisibility(View.VISIBLE);
            optionCard4.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        options = 2;
    }*/

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.publish_btn) {

            String surveyQuestion = surveyQuestionText.getText().toString();
            if (surveyQuestion == null || surveyQuestion.equals("")) {
                surveyQuestionText.setError("Required");
                return;
            }

            String option1 = optionText1.getText().toString();
            if (option1 == null || option1.equals("")) {
                optionText1.setError("Required");
                return;
            }
            String option2 = optionText2.getText().toString();
            if (option2 == null || option2.equals("")) {
                optionText2.setError("Required");
                return;
            }
            if (options == 2) {
                new SurveyTask().execute(surveyQuestion, String.valueOf(2), String.valueOf(nSurveys), option1, option2);
            } else if (options == 3) {
                String option3 = optionText3.getText().toString();
                if (option3 == null || option3.equals("")) {
                    optionText3.setError("Required");
                    return;
                }
                new SurveyTask().execute(surveyQuestion, String.valueOf(3), String.valueOf(nSurveys), option1, option2, option3);

            } else if (options == 4) {
                String option3 = optionText3.getText().toString();
                if (option3 == null || option3.equals("")) {
                    optionText3.setError("Required");
                    return;
                }
                String option4 = optionText4.getText().toString();
                if (option4 == null || option4.equals("")) {
                    optionText4.setError("Required");
                    return;
                }
                new SurveyTask().execute(surveyQuestion, String.valueOf(4), String.valueOf(nSurveys), option1, option2, option3, option4);

            }

        }

    }

    private class SurveyTask extends AsyncTask<String, Void, String> {

        long nSurveys = 0;
        DatabaseReference rootRef, surveyRef;
        int views = 0;
        int anonymity = 0;
        int r_no = -1;
        int visibility = 1;
        int votes = 0;

        final static String NULL_VALUE_STRING = "__NULL__";

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String surveyQuestion = params[0];
            int options = Integer.parseInt(params[1]);
            nSurveys = Long.parseLong(params[2]);
            String option1 = params[3];
            String option2 = params[4];
            String option3 = null;
            String option4 = null;
            if (options == 3)
                option3 = params[5];
            else if (options == 4) {
                option3 = params[5];
                option4 = params[6];
            }

            /*FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference rootRef = database.getReference();
            DatabaseReference surveyRef = rootRef.child("survey");

            surveyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nSurveys = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

            nSurveys++;
            rootRef = FirebaseDatabase.getInstance().getReference();
            surveyRef = rootRef.child("survey");
            DatabaseReference newSurveyRef = surveyRef.child(String.valueOf(nSurveys));
            DatabaseReference newSurveyVotesRef = rootRef.child("survey_votes").child(String.valueOf(nSurveys));

            newSurveyRef.child("degree").setValue(deg);
            newSurveyRef.child("department").setValue(dept);
            newSurveyRef.child("year").setValue(year);
            newSurveyRef.child("time").setValue(ServerValue.TIMESTAMP);
            newSurveyRef.child("sanonymity").setValue(anonymity);
            newSurveyRef.child("sstring").setValue(surveyQuestion);
            newSurveyRef.child("views").setValue(views);
            newSurveyRef.child("r_no").setValue(r_no);
            newSurveyRef.child("visibility").setValue(visibility);
            newSurveyRef.child("username").setValue(current_uname);

            newSurveyRef.child("option1").setValue(option1);
            newSurveyRef.child("option2").setValue(option2);
            newSurveyRef.child("option3").setValue(NULL_VALUE_STRING);
            newSurveyRef.child("option4").setValue(NULL_VALUE_STRING);



            newSurveyVotesRef.child("option1").setValue(0);
            newSurveyVotesRef.child("option2").setValue(0);
            newSurveyVotesRef.child("option3").setValue(-1);
            newSurveyVotesRef.child("option4").setValue(-1);

            if (options == 3) {
                newSurveyRef.child("option3").setValue(option3);
                newSurveyVotesRef.child("option3").setValue(0);
            } else if (options == 4) {
                newSurveyRef.child("option3").setValue(option3);
                newSurveyRef.child("option4").setValue(option4);
                newSurveyVotesRef.child("option3").setValue(0);
                newSurveyVotesRef.child("option4").setValue(0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Survey published succesfully", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();

        }
    }

}

