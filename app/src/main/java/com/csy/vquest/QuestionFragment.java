package com.csy.vquest;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.vquest.model.SentimentInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.csy.vquest.NavigationDrawerActivity.current_uname;


public class QuestionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

//    private long qno = 4;

    private String category = "";
    private String question = "";
    private int anonymity = 0;

    private Timestamp timestamp = null;
    private int views = 0;
    private String username = "";
    private int edited = 0;
    private int r_no = -1;
    private int visibility = 1;

    private Button btn1;
    private CheckBox checkBox;
    private EditText editText;
    private Spinner spinner;
    private FirebaseDatabase database;
    private long noOfChild = 0;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("New Question");

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        editText = (EditText) view.findViewById(R.id.editText3);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    anonymity = 1;
                } else {
                    anonymity = 0;
                }
            }
        });


        database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference questionRef = rootRef.child("question");

        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfChild = dataSnapshot.getChildrenCount();
                Log.d("Value", "onDataChannge" + noOfChild);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn1 = (Button) view.findViewById(R.id.button4);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String queString = editText.getText().toString().trim();
                if(queString.equals("")){
                    editText.setError("Required");
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                ((NavigationDrawerActivity) getActivity()).startAnalyze(queString);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (NavigationDrawerActivity.score != null) {
                                if (Double.parseDouble(NavigationDrawerActivity.score) >= 0) {
//                Toast.makeText(getContext(), NavigationDrawerActivity.score+" if",
                                    //  Toast.LENGTH_SHORT).show();
                                    NavigationDrawerActivity.score = null;
                                    question = queString;
                                    noOfChild = noOfChild + 1;
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference rootRef = database.getReference();
                                    DatabaseReference questionRef = rootRef.child("question");

                                    final DatabaseReference newQuestionRef = questionRef.child(String.valueOf(noOfChild));
                                    newQuestionRef.child("category").setValue(category);
                                    newQuestionRef.child("qanonymity").setValue(anonymity);
                                    newQuestionRef.child("qedited").setValue(edited);
                                    newQuestionRef.child("qstring").setValue(question);
                                    newQuestionRef.child("r_no").setValue(r_no);
                                    newQuestionRef.child("time").setValue(ServerValue.TIMESTAMP);
                                    newQuestionRef.child("views").setValue(views);
                                    newQuestionRef.child("visibility").setValue(visibility);
                                    newQuestionRef.child("replies").setValue(0);
                                    newQuestionRef.child("username").setValue(current_uname);

                                    // Toast.makeText(getActivity(), "Question raised succesfully", Toast.LENGTH_LONG).show();

                                    //getActivity().onBackPressed();

                                    final Activity activity = getActivity();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            progressDialog.dismiss();

                                            Toast.makeText(getContext(), "Question raised succesfully",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("If", "Score");
                                            getActivity().onBackPressed();

                                        }
                                    });

                                    break;
                                } else {
                                    final Activity activity = getActivity();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            progressDialog.dismiss();

                                            Toast.makeText(getContext(), "Score: " + NavigationDrawerActivity.score + " , " + "Magnitude: " + NavigationDrawerActivity.magnitude,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("Else Part", "Score");
                                            NavigationDrawerActivity.score = null;
                                            AlertDialog alertDialog = new AlertDialog.Builder(
                                                    getContext()).create();
                                            alertDialog.setTitle("Warning");
                                            alertDialog.setMessage("Looks like what you are writing is not appropriate and may get deleted " +
                                                    "in future. Do you still want to continue?");
                                            alertDialog.setIcon(R.drawable.alert);
                                            alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    question = editText.getText().toString();
                                                    noOfChild = noOfChild + 1;
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference rootRef = database.getReference();
                                                    DatabaseReference questionRef = rootRef.child("question");

                                                    final DatabaseReference newQuestionRef = questionRef.child(String.valueOf(noOfChild));
                                                    newQuestionRef.child("category").setValue(category);
                                                    newQuestionRef.child("qanonymity").setValue(anonymity);
                                                    newQuestionRef.child("qedited").setValue(edited);
                                                    newQuestionRef.child("qstring").setValue(question);
                                                    newQuestionRef.child("r_no").setValue(r_no);
                                                    newQuestionRef.child("time").setValue(ServerValue.TIMESTAMP);
                                                    newQuestionRef.child("views").setValue(views);
                                                    newQuestionRef.child("visibility").setValue(visibility);
                                                    newQuestionRef.child("replies").setValue(0);
                                                    newQuestionRef.child("username").setValue(current_uname);
                                                    Toast.makeText(getContext(), "Question raised succesfully",
                                                            Toast.LENGTH_SHORT).show();
                                                    getActivity().onBackPressed();

                                                        }
                                            });
                                            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();


                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                });


                thread.start();
            }
        });



        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = parent.getItemAtPosition(0).toString();
    }

}
