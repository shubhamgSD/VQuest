package com.csy.vquest;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.csy.vquest.NavigationDrawerActivity.current_deg;
import static com.csy.vquest.NavigationDrawerActivity.current_dept;
import static com.csy.vquest.NavigationDrawerActivity.current_year;


public class Timetable extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIndicator;


    public Timetable() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        getActivity().setTitle("Timetable");
       // toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
//        loadingIndicator.setVisibility(View.VISIBLE);

        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        loadingIndicator.setVisibility(View.VISIBLE);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        return view;

    }

    private void setupViewPager(ViewPager viewPager) {

        FirebasePagerAdapter firebasePagerAdapter = new FirebasePagerAdapter(getFragmentManager());
        viewPager.setAdapter(firebasePagerAdapter);

    }

    private class FirebasePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> timingFragments = new ArrayList<>();
        private List<String> days = new ArrayList<>();
        private int loaded = 0;

        private DatabaseReference rootRef, surveyReference, surveyDoneRef;

        FirebasePagerAdapter(FragmentManager fm) {
            super(fm);
            final Timing tm = new Timing();
            DatabaseReference timetablerefs = FirebaseDatabase.getInstance().getReference().child("timetable");
            DatabaseReference timingrefs = timetablerefs.child(current_dept).child(current_deg).child(current_year);
            for(int i=0;i<7;i++)
            {
                DatabaseReference dayrefs = timingrefs.child(tm.findDay(i));

                dayrefs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (loaded <= 6) {
                            Log.d("if", "done");
                            TimingBean timingBean= dataSnapshot.getValue(TimingBean.class);
                            timingFragments.add(Timing.newInstance(timingBean));

                            notifyDataSetChanged();
                            loadingIndicator.setVisibility(View.GONE);
                            loaded++;
                        } else {
                            Log.d("else", "done");
                            TimingBean timingBean= dataSnapshot.getValue(TimingBean.class);
                            String key = dataSnapshot.getKey();
                            int p = tm.findPosition(key);
                            timingFragments.remove(p);
                            timingFragments.add(p, Timing.newInstance(timingBean));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                days.add(tm.findDay(i));


            }

        }



        @Override
        public Fragment getItem(int position) {
            return timingFragments.get(position);
        }

        @Override
        public int getCount() {
            return timingFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {return days.get(position); }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
