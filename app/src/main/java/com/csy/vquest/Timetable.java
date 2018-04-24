package com.csy.vquest;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


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

       // toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
//        loadingIndicator.setVisibility(View.VISIBLE);

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

        private DatabaseReference rootRef, surveyReference, surveyDoneRef;

        FirebasePagerAdapter(FragmentManager fm) {
            super(fm);

            for(int i=1;i<8;i++)
            {
                timingFragments.add(new Timing());
                       days.add("Day: "+i);
            }
           // loadingIndicator.setVisibility(View.GONE);

//            rootRef = FirebaseDatabase.getInstance().getReference();
//            surveyReference = rootRef.child("survey");
//            surveyDoneRef = rootRef.child("survey_done");
//
//            Query query = surveyReference.orderByChild("visibility").equalTo(1);
//
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    surveyFragments.clear();
//                    surveyTitles.clear();
//
//                    int surveyCount = 0;
//                    for (DataSnapshot surveySnapshot : dataSnapshot.getChildren()) {
//                        surveyCount++;
//                        final String surveyKey = surveySnapshot.getKey();
//
//                        SurveyBean survey = surveySnapshot.getValue(SurveyBean.class);
//                        surveyFragments.add(OneSurveyFragment.newInstance(surveyKey, survey, surveyCount));
//                        surveyTitles.add("Survey " + surveyCount);
//
//                        /*surveyDoneRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                if (dataSnapshot != null){
//                                    if (!dataSnapshot.child(surveyKey).child(firebaseUser).exists()){
//
//
//
//                                    }
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });*/
//                    }
//                    notifyDataSetChanged();
//                    loadingIndicator.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
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
        public CharSequence getPageTitle(int position) {
            return days.get(position);
        }
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
