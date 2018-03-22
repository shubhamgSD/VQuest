package com.csy.vquest;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.csy.vquest.NavigationDrawerActivity.current_uname;

public class HomePageFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FloatingActionButton floatBtn;
    private ListView listView;
    private ProgressBar loadingIndicator;

    private String fragmentType;
//    Parcelable state;

//    private int indexList = 0;
//    private int toplist = 0;
//    private View vList;

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

        if (item.getItemId() == R.id.survey_menu) {

            Fragment viewSurveyFragment = new ViewSurveyFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, viewSurveyFragment, "view_survey_fragment")
                    .addToBackStack("viewSurveyFragment")
                    .commit();

        } else {

            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("question");

            Query query;

            switch (item.getItemId()) {

                case R.id.fil_all:
                    query = questionRef;
                    break;

                case R.id.fil_unanswered:
                    if (item.isChecked())
                        query = questionRef.orderByChild("replies").equalTo(0);
                    else
                        query = questionRef;
                    break;

                case R.id.fil_answered:
                    if (item.isChecked())
                        query = questionRef.orderByChild("replies").startAt(1);
                    else
                        query = questionRef;
                    break;

                case R.id.fil_general:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("General");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_academic_common:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Common Academic");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_academic_cse:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Academic-CSE");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_academic_ece:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Academic-ECE");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_academic_me:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Academic-ME");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_hostel:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Hostel");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_mess:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Mess");
                    else
                        query = questionRef;
                    break;

                case R.id.fil_events:
                    if (item.isChecked())
                        query = questionRef.orderByChild("category").equalTo("Events");
                    else
                        query = questionRef;
                    break;

                default:
                    query = questionRef;
                    break;

            }
            //  query = query.orderByChild("time");

            firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                    QuestionBean.class, R.layout.card_layout, query, loadingIndicator);

            listView.setAdapter(firebaseListAdapter);

        }

        return true;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentType = getArguments().getString("fragment");

        if (fragmentType == "home") {
            getActivity().setTitle("Home");
        } else {
            getActivity().setTitle("My Questions");
        }
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        if (fragmentType == "home") {
            setHasOptionsMenu(true);
        } else {
            setHasOptionsMenu(false);
        }
        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        listView = (ListView) view.findViewById(R.id.listViewHome);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference questionRef = rootRef.child("question");

        if (fragmentType == "home") {
            firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                    QuestionBean.class, R.layout.card_layout, questionRef, loadingIndicator);
        } else {
            firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                    QuestionBean.class, R.layout.card_layout, questionRef.orderByChild("username").equalTo(current_uname), loadingIndicator);
        }

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

        Bundle bundle = new Bundle();
        bundle.putString("key", databaseReference.getKey());

        Fragment fragment = new AnsFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack("ansFragment")
                .commit();

    }

}
