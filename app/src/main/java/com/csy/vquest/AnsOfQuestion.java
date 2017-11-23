package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnsOfQuestion extends Fragment {

    private String key;
    private long noOfChild=0;
    private Button btn;
    private FirebaseDatabase database;
    private int anonymity = 1;

    public AnsOfQuestion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ans_of_question, container, false);

       final  EditText editText = (EditText) view.findViewById(R.id.editText);
        btn = (Button) view.findViewById(R.id.button6);



        if(getArguments()!=null)
        {
            key = getArguments().getString("key");
        }

        database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference questionRef = rootRef.child("answer").child(key);

        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfChild = dataSnapshot.getChildrenCount();
                Log.d("Value","onDataChannge"+noOfChild);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(getActivity(),key, Toast.LENGTH_SHORT).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noOfChild=noOfChild+1;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference questionRef = rootRef.child("answer").child(key);
                final DatabaseReference newAnswerRef = questionRef.child(noOfChild+"");
                newAnswerRef.child("aanonymity").setValue(1);
                newAnswerRef.child("aedited").setValue(0);
                newAnswerRef.child("astring").setValue(editText.getText().toString());
                newAnswerRef.child("likes").setValue(0);
                newAnswerRef.child("r_no").setValue(-1);
                newAnswerRef.child("time").setValue(ServerValue.TIMESTAMP);


                

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
                
                newAnswerRef.child("visibility").setValue(1);
             //   Log.d("Value In Button click",i+"");

            }
        });

    return view;
    }

}
