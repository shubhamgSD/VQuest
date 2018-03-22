package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Map;

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

                /*rootRef.child("member")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newQuestionRef.child("username").setValue(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                Toast.makeText(getActivity(), "Question raised succesfully", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();

            }
        });

        editText = (EditText) view.findViewById(R.id.editText3);

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
