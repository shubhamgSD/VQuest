package com.csy.vquest;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.Date;
import java.util.Objects;

import static com.csy.vquest.NavigationDrawerActivity.current_uname;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnsFragment extends Fragment {

    QuestionBean questionBean;
    String key;
    FirebaseListAdapter<AnswerBean> firebaseListAdapter;
    int flag = 0;


    private Button likeBtn, qVar_btn, aVar_btn;
    private ListView listView;
    private TextView username_view, time_view, qstring, views_view;
    private long currentAnswers;

    FirebaseAuth firebaseAuth;

    public AnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ans, container, false);

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
                        DatabaseReference likeref = rootref.child("answer_like");
                        likeref.child(key).child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
                        likeref.addListenerForSingleValueEvent(new ValueEventListener() {
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

                astringView.setText(model.getAstring());

                likeView.setText(model.getLikes() + " likes");
                Date date = new Date(model.getTime());
                time.setText(date.toString());

                if (model.getaanonymity() == 1) {
                    username.setText("Anonymous");
                } else {
                    username.setText(model.getUsername());
                }

                aVar_btn = (Button) view.findViewById(R.id.btn_report1);
                if(model.getUsername().equalsIgnoreCase(current_uname))
                    aVar_btn.setText("Edit");
                else
                    aVar_btn.setText("Report");

                aVar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(aVar_btn.getText().toString().equalsIgnoreCase("Report")) {

                            Toast.makeText(getContext(), aVar_btn.getText().toString(), Toast.LENGTH_SHORT).show();


                        }
                        else {

                            Toast.makeText(getContext(), aVar_btn.getText().toString(), Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder aEditDialog = new AlertDialog.Builder(getActivity());
                            final View aEditView = LayoutInflater.from(getContext()).inflate(R.layout.edit_answer_dialog, null);
                            final EditText aEditText = (EditText) aEditView.findViewById(R.id.input_edited_answer);
                            aEditText.setText(astringView.getText().toString());
                            aEditDialog.setView(aEditView);
                            aEditDialog.setTitle("Edit answer");
                            aEditDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(aEditText.getText() == null)
                                        aEditText.setError("Required");
                                    else{
                                        aEditText.setError(null);
                                        DatabaseReference ansRef = rootRef.child("answer").child(key).child(firebaseListAdapter.getRef(tempPos).getKey());
                                        ansRef.child("astring").setValue(aEditText.getText().toString());
                                        ansRef.child("aedited").setValue(1);
                                        ansRef.child("time").setValue(ServerValue.TIMESTAMP);
                                        Toast.makeText(getContext(), "Answer Edited successfully", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                            aEditDialog.create().show();

                        }
                    }
                });



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
                        time_view.setText(date.toString());

                        if (questionBean.getQanonymity() == 1) {
                            username_view.setText("Anonymous");
                        } else {
                            username_view.setText(questionBean.getUsername());
                        }

                        if(questionBean.getUsername().equalsIgnoreCase(current_uname))
                            qVar_btn.setText("Edit");
                        else
                            qVar_btn.setText("Report");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                if(qVar_btn.getText().toString() == "Report"){



                }
                else {

                    AlertDialog.Builder qEditDialog = new AlertDialog.Builder(getActivity());
                    final View qEditView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_question_dialog, null);
                    final EditText qEditText = (EditText) qEditView.findViewById(R.id.input_edited_question);
                    qEditText.setText(qstring.getText().toString());
                    qEditDialog.setView(qEditView);
                    qEditDialog.setTitle("Edit question");
                    qEditDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(qEditText.getText() == null)
                                qEditText.setError("Required");
                            else{
                                qEditText.setError(null);
                                queRef.child(key).child("qstring").setValue(qEditText.getText().toString());
                                queRef.child(key).child("qedited").setValue(1);
                                queRef.child(key).child("time").setValue(ServerValue.TIMESTAMP);
                                Toast.makeText(getContext(), "Question Edited successfully", Toast.LENGTH_LONG).show();
                            }

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

                                currentAnswers++;
                                rootRef.child("question").child(key).child("replies").setValue(currentAnswers);
                                final DatabaseReference newAnswerRef = rootRef.child("answer").child(key).child(String.valueOf(currentAnswers));
                                if (checkAnonym.isChecked()) {
                                    newAnswerRef.child("aanonymity").setValue(1);
                                } else {
                                    newAnswerRef.child("aanonymity").setValue(0);
                                }
                                newAnswerRef.child("aedited").setValue(0);
                                newAnswerRef.child("astring").setValue(answerText.getText().toString());
                                newAnswerRef.child("likes").setValue(0);
                                newAnswerRef.child("r_no").setValue(-1);
                                newAnswerRef.child("time").setValue(ServerValue.TIMESTAMP);
                                newAnswerRef.child("visibility").setValue(1);
                                newAnswerRef.child("username").setValue(current_uname);

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
