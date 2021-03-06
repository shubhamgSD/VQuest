package com.csy.vquest;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.csy.vquest.NavigationDrawerActivity.current_deg;
import static com.csy.vquest.NavigationDrawerActivity.current_dept;
import static com.csy.vquest.NavigationDrawerActivity.current_uname;
import static com.csy.vquest.NavigationDrawerActivity.current_year;

public class HomePageFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ProgressBar loadingIndicator;
    private BottomNavigationView bottomNavigationView;

    private String fragmentType;

    private Query announcementRef;
    private RecyclerView list;
    private LinearLayoutManager mLayoutManager;
    private static int year;
    private static int month;
    private static int day;

    private final static String ALL = "All";

    int i=-1;

    CustomFirebaseListAdapter firebaseListAdapter;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_filter_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.survey_menu) {

            Fragment viewSurveyFragment = new ViewSurveyFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, viewSurveyFragment, "view_survey_fragment")
                    .addToBackStack(null)
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentType = getArguments().getString("fragment");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        list = (RecyclerView) view.findViewById(R.id.list);
        if (fragmentType == "home") {
            getActivity().setTitle("Home");
            setHasOptionsMenu(true);
            list.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, true);
            mLayoutManager.setStackFromEnd(true);
            list.setLayoutManager(mLayoutManager);
        } else {
            getActivity().setTitle("My Questions");
            setHasOptionsMenu(false);
            list.setVisibility(View.GONE);
        }
        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        listView = (ListView) view.findViewById(R.id.listViewHome);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;

                switch (item.getItemId()){

                    case R.id.action_survey:

                        fragment = new CreateSurveyFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, fragment, "create_survey_fragment")
                                .addToBackStack(null)
                                .commit();

                        break;

                    case R.id.action_question:

                        fragment = new QuestionFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, fragment, "question_fragment")
                                .addToBackStack(null)
                                .commit();

                        break;

                    case R.id.action_announcement:

                        fragment = new AnnouncementFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, fragment, "announcement_fragment")
                                .addToBackStack(null)
                                .commit();

                        break;

                }

                return true;
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference questionRef = rootRef.child("question");
        announcementRef = rootRef.child("announcement").orderByChild("visibility").equalTo(1);
        announcementRef.keepSynced(true);

        if (fragmentType == "home") {
            firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                    QuestionBean.class, R.layout.card_layout, questionRef, loadingIndicator);
        } else {
            firebaseListAdapter = new CustomFirebaseListAdapter(getActivity(),
                    QuestionBean.class, R.layout.card_layout, questionRef.orderByChild("username").equalTo(current_uname), loadingIndicator);
        }

        listView.setAdapter(firebaseListAdapter);
        listView.setOnItemClickListener(this);

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
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (fragmentType.equals("home")) {
            FirebaseRecyclerAdapter<AnnouncementBean,AnnouncementViewHolder>firebaseRecyclerAdapter
                    = new FirebaseRecyclerAdapter<AnnouncementBean, AnnouncementViewHolder>
                    (AnnouncementBean.class, R.layout.announcement_item,AnnouncementViewHolder.class,announcementRef) {
                @Override
                protected void populateViewHolder(AnnouncementViewHolder viewHolder, AnnouncementBean model, int position) {
                    if ((model.getDegree().equals(ALL) || model.getDegree().equals(current_deg))
                            && (model.getDepartment().equals(ALL) || model.getDepartment().equals(current_dept))
                            && (model.getYear().equals(ALL) || model.getYear().equals(current_year))) {

                        viewHolder.setannouncement(model.getAstring());
                        viewHolder.setusername(model.getUsername());
                        viewHolder.settime(model.getTime());

                    }
                }
            };
            list.setAdapter(firebaseRecyclerAdapter);
        }

    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public AnnouncementViewHolder(View itemview){
            super(itemview);
            mview = itemview;
        }
        public void setusername(String username){
            TextView announcement_username = (TextView)mview.findViewById(R.id.user_name);
            announcement_username.setText(username);
        }
        public void setannouncement(String announcement){
            TextView announcement_string = (TextView)mview.findViewById(R.id.user_announcement);
            announcement_string.setText(announcement);
        }
        public void settime(long time){
            TextView announcement_time = (TextView) mview.findViewById(R.id.user_time);
            Date date = new Date(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);
            announcement_time.setText(day+"/"+month+"/"+year);

        }

    }

}
