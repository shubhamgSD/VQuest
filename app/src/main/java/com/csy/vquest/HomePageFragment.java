package com.csy.vquest;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class HomePageFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FloatingActionButton floatBtn;
    private ImageButton imageButton;
    private ListView listView;

    CustomFirebaseListAdapter firebaseListAdapter;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);

        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("question");

        Query query;

        switch(item.getItemId()) {

            case R.id.fil_all:
                query = questionRef;
                break;

            case R.id.fil_unanswered:
                if(item.isChecked())
                    query = questionRef.orderByChild("replies").equalTo(0);
                else
                    query = questionRef;
                break;

            case R.id.fil_answered:
                if(item.isChecked())
                    query = questionRef.orderByChild("replies").startAt(1);
                else
                    query = questionRef;
                break;

            case R.id.fil_general:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("General");
                else
                    query = questionRef;
                break;

            case R.id.fil_academic_common:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Common Academic");
                else
                    query = questionRef;
                break;

            case R.id.fil_academic_cse:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Academic-CSE");
                else
                    query = questionRef;
                break;

            case R.id.fil_academic_ece:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Academic-ECE");
                else
                    query = questionRef;
                break;

            case R.id.fil_academic_me:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Academic-ME");
                else
                    query = questionRef;
                break;

            case R.id.fil_hostel:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Hostel");
                else
                    query = questionRef;
                break;

            case R.id.fil_mess:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Mess");
                else
                    query = questionRef;
                break;

            case R.id.fil_events:
                if(item.isChecked())
                    query = questionRef.orderByChild("category").equalTo("Events");
                else
                    query = questionRef;
                break;

            default:
                query = questionRef;
                break;

        }

        query = query.orderByChild("time");

        listView.setAdapter(new CustomFirebaseListAdapter(
                getActivity(),
                QuestionBean.class,
                R.layout.card_layout,
                query
        ));

        return true;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.listViewHome);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference questionRef = rootRef.child("question");

        firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                QuestionBean.class,R.layout.card_layout,questionRef);

        listView.setAdapter(firebaseListAdapter);
        listView.setOnItemClickListener(this);


        floatBtn = (FloatingActionButton) view.findViewById(R.id.fab);
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new QuestionFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "question_fragment")
                        .addToBackStack("questionfragment")
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final DatabaseReference databaseReference = firebaseListAdapter.getRef(position);

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference viewRef = rootRef.child("question_views");
        viewRef.child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
        viewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.child("views").setValue(dataSnapshot.child(databaseReference.getKey()).getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      Bundle bundle  = new Bundle();
      bundle.putString("key",databaseReference.getKey());

        Fragment fragment = new AnsFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack("ansFragment")
                .commit();
    }
}
