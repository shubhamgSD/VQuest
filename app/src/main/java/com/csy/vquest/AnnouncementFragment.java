package com.csy.vquest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class AnnouncementFragment extends Fragment{

    private String ann = "";
    private EditText announcement;
    private Button make_announcement;
    private FirebaseDatabase database;
    private long noOfChild = 0;

    public AnnouncementFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("New Announcement");
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);




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

        announcement = (EditText) view.findViewById(R.id.writeannounce);
        make_announcement = (Button) view.findViewById(R.id.buttonannounce);
        make_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ann = announcement.getText().toString();
                noOfChild = noOfChild + 1;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference announcementRef = rootRef.child("announcement");

                final DatabaseReference newAnnouncementRef = announcementRef.child(String.valueOf(noOfChild));
                newAnnouncementRef.child("astring").setValue(ann);
                newAnnouncementRef.child("time").setValue(ServerValue.TIMESTAMP);

                rootRef.child("member")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newAnnouncementRef.child("username").setValue(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getActivity(), "Announced succesfully", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();

            }
        });
        return view;
    }

}
