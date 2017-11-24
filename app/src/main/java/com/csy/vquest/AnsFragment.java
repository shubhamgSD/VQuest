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

    private Button likeBtn;
    private ListView listView;
    private TextView username_view, time_view, qstring, views_view;
    private long currentAnswers;
    ;

    public AnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ans, container, false);

        listView = (ListView) view.findViewById(R.id.ans_lv);
        Button btn = (Button) view.findViewById(R.id.btn_answer);

        qstring = (TextView) view.findViewById(R.id.qstring_view1);
        username_view = (TextView) view.findViewById(R.id.uname_view2);
        time_view = (TextView) view.findViewById(R.id.time_view2);
        views_view = (TextView) view.findViewById(R.id.views_view1);


        if (getArguments() != null) {
            key = getArguments().getString("key");
        }


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference answerRef = rootRef.child("answer").child(key);
        DatabaseReference queRef = rootRef.child("question");

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

                                if(dataSnapshot.child(key).child(databaseReference.getKey()).hasChild(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()))
                                {
                                    likeref.child(key).child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                                else
                                {
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

                TextView astringView = (TextView) view.findViewById(R.id.astring_view);
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
                        if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                           likebtn.setTextColor(Color.parseColor("#ffffff"));
                           likebtn.setBackgroundColor(Color.parseColor("#FF7F92FA"));
                        }
                        else
                        {
                            likebtn.setTextColor(Color.parseColor("#100f10"));
                            likebtn.setBackgroundColor(Color.parseColor("#FFB7B6B6"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                astringView.setText(model.getAstring());

                    likeView.setText(model.getLikes()+" likes");
                    Date date = new Date(model.getTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);
                time.setText(day+"/"+month+"/"+year);



                    if(model.getaanonymity()==1)
                    {
                        username.setText("Anonymous");
                        username.setTextColor(Color.parseColor("#FF4B4A4B"));
                    }
                    else
                    {
                        username.setText(model.getUsername());
                        username.setTextColor(Color.parseColor("#0000EE"));
                    }

                return view;

            }


        };


      queRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                  if(messageSnapshot.getKey().equals(key))
                  {
                      questionBean = messageSnapshot.getValue(QuestionBean.class);
                      qstring.setText(questionBean.getQstring());

                      views_view.setText(questionBean.getViews()+" views");
                      Date date = new Date(questionBean.getTime());

                      Calendar cal = Calendar.getInstance();
                      cal.setTime(date);
                      year = cal.get(Calendar.YEAR);
                      month = cal.get(Calendar.MONTH);
                      day = cal.get(Calendar.DAY_OF_MONTH);
                      hour = cal.get(Calendar.HOUR_OF_DAY);
                      minute = cal.get(Calendar.MINUTE);
                      seconds = cal.get(Calendar.SECOND);
                      time_view.setText(day+"/"+month+"/"+year);


                      if(questionBean.getQanonymity() == 1)
                      {
                          username_view.setText("Anonymous");
                          username_view.setTextColor(Color.parseColor("#FF4B4A4B"));

                      }
                      else
                      {
                          username_view.setText(questionBean.getUsername());
                          username_view.setTextColor(Color.parseColor("#0000EE"));
                      }

              }
              }
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


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                rootRef.child("answer").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentAnswers = dataSnapshot.getChildrenCount();
                        Log.d("current answers", currentAnswers+"");
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

                                rootRef.child("member")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        newAnswerRef.child("username").setValue(dataSnapshot.getValue());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(getActivity(), "Answered succesfully", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                dialogBuilder.create().show();
//               Fragment fragment = new AnsOfQuestion();
//               fragment.setArguments(bundle);
//               getFragmentManager().beginTransaction()
//                       .replace(R.id.fragment, fragment)
//                       .addToBackStack("")
//                       .commit();
            }
        });

        return view;

    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.like_btn:
//            int position = listView.getPositionForView((View) v.getParent());
//            Log.d("Position",position+"");
//            break;
//
//
//        }
//
//    }
}
