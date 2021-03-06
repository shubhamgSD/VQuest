package com.csy.vquest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.csy.vquest.NavigationDrawerActivity.current_uname;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnsFragment extends Fragment {

    QuestionBean questionBean;
    String key;
    String answernode;
    FirebaseListAdapter<AnswerBean> firebaseListAdapter;
    int flag = 0;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int seconds;

    private Button likeBtn, qVar_btn, aVar_btn;
    private ListView listView;
    private TextView username_view, time_view, qstring, views_view;
    private TextView noAnswerView;
    private ProgressBar loadingIndicator;
    private long currentAnswers;

    FirebaseAuth firebaseAuth;

    public AnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ans, container, false);

        getActivity().setTitle("Question Insight");

        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        noAnswerView = (TextView) view.findViewById(R.id.tv_no_answer);
        noAnswerView.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        listView = (ListView) view.findViewById(R.id.ans_lv);
        Button answerBtn = (Button) view.findViewById(R.id.btn_answer);
        qVar_btn = (Button) view.findViewById(R.id.btn_report);


        qstring = (TextView) view.findViewById(R.id.qstring_view1);
        username_view = (TextView) view.findViewById(R.id.uname_view2);
        time_view = (TextView) view.findViewById(R.id.time_view2);
        views_view = (TextView) view.findViewById(R.id.views_view1);


        if (getArguments() != null) {
            key = getArguments().getString("key");
        }


        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference answerRef = rootRef.child("answer").child(key);
        final DatabaseReference queRef = rootRef.child("question");

//        rootRef.child("member").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                current_uname = dataSnapshot.child("username").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        firebaseListAdapter = new FirebaseListAdapter<AnswerBean>(getActivity(),
                AnswerBean.class, R.layout.custom_answer_layout, answerRef) {
            @Override
            protected void populateView(View v, AnswerBean model, int position) {

            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {


                // super.getView(position, view, viewGroup);
                if (view == null) {
                    // Log.d("check inflate >","view= "+view);
                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_answer_layout, viewGroup, false);
                    // Log.d("check  after inflate >","view= "+view);
                }

                final int tempPos = position;

                AnswerBean model = getItem(position);
                likeBtn = (Button) view.findViewById(R.id.btn_like);
                likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = listView.getPositionForView((View) v.getParent());
                        Log.d("Position", position + "");
                        final DatabaseReference databaseReference = firebaseListAdapter.getRef(position);
                        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference likeref = rootref.child("answer_like");


                        likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(key).child(databaseReference.getKey()).hasChild(FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid())) {
                                    likeref.child(key).child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                } else {
                                    likeref.child(key).child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        likeref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                databaseReference.child("likes").setValue(dataSnapshot.child(key).child(databaseReference.getKey()).getChildrenCount());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

                final TextView astringView = (TextView) view.findViewById(R.id.astring_view);
                TextView username = (TextView) view.findViewById(R.id.uname_view2);
                TextView time = (TextView) view.findViewById(R.id.time_view2);
                TextView likeView = (TextView) view.findViewById(R.id.likes_view);
                final Button likebtn = (Button) view.findViewById(R.id.btn_like);

                final DatabaseReference anslikeref = getRef(position);
                DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference likeref = rootref.child("answer_like");
                likeref.child(key).child(anslikeref.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            likebtn.setTextColor(Color.parseColor("#ffffff"));
                            likebtn.setBackgroundColor(Color.parseColor("#FF7F92FA"));
                        } else {
                            likebtn.setTextColor(Color.parseColor("#100f10"));
                            likebtn.setBackgroundColor(Color.parseColor("#FFD4D1D1"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                aVar_btn = (Button) view.findViewById(R.id.btn_report1);
                if (model.getUsername().equalsIgnoreCase(current_uname))
                    aVar_btn.setText("Edit");
                else
                    aVar_btn.setText("Report");

                aVar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Button aBtn = (Button) v.findViewById(R.id.btn_report1);

                        if (aBtn.getText().toString().equalsIgnoreCase("Report")) {

                            Toast.makeText(getActivity(), "Reported successfully", Toast.LENGTH_LONG).show();

                        } else {


                            AlertDialog.Builder aEditDialog = new AlertDialog.Builder(getActivity());
                            final View aEditView = LayoutInflater.from(getContext()).inflate(R.layout.edit_answer_dialog, null);
                            final EditText aEditText = (EditText) aEditView.findViewById(R.id.input_edited_answer);
                            aEditText.setText(astringView.getText().toString());
                            aEditDialog.setView(aEditView);
                            aEditDialog.setTitle("Edit answer");
                            aEditDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String ansString = aEditText.getText().toString().trim();

                                    if (ansString == null || ansString.equals(""))
                                        aEditText.setError("Required");
                                    else {
                                        aEditText.setError(null);
                                        DatabaseReference ansRef = rootRef.child("answer").child(key).child(firebaseListAdapter.getRef(tempPos).getKey());
                                        ansRef.child("astring").setValue(ansString);
                                        ansRef.child("time").setValue(ServerValue.TIMESTAMP);
                                        ansRef.child("aedited").setValue(1);
                                        Toast.makeText(getContext(), "Answer Edited successfully", Toast.LENGTH_LONG).show();
                                    }

                                }
                            })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            aEditDialog.create().show();

                        }
                    }
                });


                astringView.setText(model.getAstring());

                likeView.setText(model.getLikes() + " likes");
                Date date = new Date(model.getTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);
                time.setText(day + "/" + month + "/" + year);


                if (model.getaanonymity() == 1) {
                    username.setText("Anonymous");
                    username.setTextColor(Color.parseColor("#FF4B4A4B"));
                } else {
                    username.setText(model.getUsername());
                    username.setTextColor(Color.parseColor("#0000EE"));
                }

                loadingIndicator.setVisibility(View.GONE);
                noAnswerView.setVisibility(View.GONE);

                return view;

            }

        };


        queRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                questionBean = dataSnapshot.getValue(QuestionBean.class);
                qstring.setText(questionBean.getQstring());


                views_view.setText(questionBean.getViews() + " views");
                Date date = new Date(questionBean.getTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);
                time_view.setText(day + "/" + month + "/" + year);


                if (questionBean.getQanonymity() == 1) {
                    username_view.setText("Anonymous");
                    username_view.setTextColor(Color.parseColor("#FF4B4A4B"));

                } else {
                    username_view.setText(questionBean.getUsername());
                    username_view.setTextColor(Color.parseColor("#0000EE"));
                }

                if (questionBean.getUsername().equalsIgnoreCase(current_uname))
                    qVar_btn.setText("Edit");
                else
                    qVar_btn.setText("Report");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (firebaseListAdapter.isEmpty()) {
            loadingIndicator.setVisibility(View.GONE);
            noAnswerView.setVisibility(View.VISIBLE);
        }


        listView.setAdapter(firebaseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "On click", Toast.LENGTH_SHORT).show();
            }
        });

        qVar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qVar_btn.getText().toString() == "Report") {

                    Toast.makeText(getActivity(), "Reported successfully", Toast.LENGTH_LONG).show();

                } else {

                    AlertDialog.Builder qEditDialog = new AlertDialog.Builder(getActivity());
                    final View qEditView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_question_dialog, null);
                    final EditText qEditText = (EditText) qEditView.findViewById(R.id.input_edited_question);
                    qEditText.setText(qstring.getText().toString());
                    qEditDialog.setView(qEditView);
                    qEditDialog.setTitle("Edit question");
                    qEditDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String queString = qEditText.getText().toString().trim();

                            if (queString == null || queString.equals(""))
                                qEditText.setError("Required");
                            else {
                                qEditText.setError(null);
                                queRef.child(key).child("qstring").setValue(queString);
                                queRef.child(key).child("qedited").setValue(1);
                                queRef.child(key).child("time").setValue(ServerValue.TIMESTAMP);
                                Toast.makeText(getContext(), "Question Edited successfully", Toast.LENGTH_LONG).show();
                            }

                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    qEditDialog.create().show();

                }
            }
        });

        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                rootRef.child("answer").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentAnswers = dataSnapshot.getChildrenCount();

                        Log.d("current answers", currentAnswers + "");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("key", key);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.answer_dialog, null);
                dialogBuilder.setView(view1);
                dialogBuilder.setTitle("New Answer")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!

                                EditText answerText = (EditText) view1.findViewById(R.id.input_answer);
                                CheckBox checkAnonym = (CheckBox) view1.findViewById(R.id.check_aanonym);

                                String ansString = answerText.getText().toString().trim();
                                if(ansString == null || ansString.equals("")){
                                    answerText.setError("Required");
                                    return;
                                }

                                currentAnswers++;
                                rootRef.child("question").child(key).child("replies").setValue(currentAnswers);
                                final DatabaseReference newAnswerRef = rootRef.child("answer").child(key).child(String.valueOf(currentAnswers));
                                newAnswerRef.child("username").setValue(current_uname);
                                newAnswerRef.child("astring").setValue(ansString);
                                newAnswerRef.child("likes").setValue(0);
                                newAnswerRef.child("time").setValue(ServerValue.TIMESTAMP);
                                if (checkAnonym.isChecked()) {
                                    newAnswerRef.child("aanonymity").setValue(1);
                                } else {
                                    newAnswerRef.child("aanonymity").setValue(0);
                                }
                                newAnswerRef.child("aedited").setValue(0);
                                newAnswerRef.child("r_no").setValue(-1);
                                newAnswerRef.child("visibility").setValue(1);

                                Toast.makeText(getActivity(), "Answered succesfully", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                dialogBuilder.create().show();
            }
        });

        return view;

    }

}
