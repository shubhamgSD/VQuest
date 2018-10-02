package com.csy.vquest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import static com.csy.vquest.NavigationDrawerActivity.current_uname;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class AnnouncementFragment extends Fragment{

    private String ann = "";
    private EditText announcement;
    private Button make_announcement;
    private FirebaseDatabase database;
    private long noOfChild = 0;

    private String dept, deg, year;
    private Spinner deptSpinner, degSpinner, yearSpinner;

    public AnnouncementFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("New Announcement");
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        deptSpinner = (Spinner) view.findViewById(R.id.spinner_dept);
        degSpinner = (Spinner) view.findViewById(R.id.spinner_deg);
        yearSpinner = (Spinner) view.findViewById(R.id.spinner_year);

        database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference announcementRef = rootRef.child("announcement");

        announcementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfChild = dataSnapshot.getChildrenCount();
                Log.d("Value", "onDataChange" + noOfChild);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        announcement = (EditText) view.findViewById(R.id.writeannounce);
        make_announcement = (Button) view.findViewById(R.id.buttonannounce);
        make_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                ann = announcement.getText().toString().trim();
                if (ann.equals("")){
                    announcement.setError("Required");
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference announcementRef = rootRef.child("announcement");

                noOfChild = noOfChild + 1;
                final DatabaseReference newAnnouncementRef = announcementRef.child(String.valueOf(noOfChild));
                newAnnouncementRef.child("degree").setValue(deg);
                newAnnouncementRef.child("department").setValue(dept);
                newAnnouncementRef.child("year").setValue(year);
                newAnnouncementRef.child("astring").setValue(ann);
                newAnnouncementRef.child("time").setValue(ServerValue.TIMESTAMP);
                newAnnouncementRef.child("username").setValue(current_uname);
                newAnnouncementRef.child("visibility").setValue(1);

                Toast.makeText(getActivity(), "Announced succesfully", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();

            }
        });
        return view;
    }

}
