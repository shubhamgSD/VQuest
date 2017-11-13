package com.csy.vquest;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                        .replace(R.id.fragment, fragment)
                        .addToBackStack("questionfragment")
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DatabaseReference databaseReference = firebaseListAdapter.getRef(position);

      Bundle bundle  = new Bundle();
      bundle.putString("key",databaseReference.getKey());

        Fragment fragment = new AnsFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack("questionfragment")
                .commit();
    }
}
