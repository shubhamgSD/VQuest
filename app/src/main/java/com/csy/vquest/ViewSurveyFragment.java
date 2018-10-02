package com.csy.vquest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.csy.vquest.NavigationDrawerActivity.current_deg;
import static com.csy.vquest.NavigationDrawerActivity.current_dept;
import static com.csy.vquest.NavigationDrawerActivity.current_year;
import static com.google.firebase.database.ServerValue.TIMESTAMP;

public class ViewSurveyFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIndicator;

    private String firebaseUser;
    private final static String ALL = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Surveys");
        View view = inflater.inflate(R.layout.fragment_view_survey, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.survey_tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.survey_view_pager);
        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadingIndicator.setVisibility(View.VISIBLE);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        FirebasePagerAdapter firebasePagerAdapter = new FirebasePagerAdapter(getFragmentManager());
        viewPager.setAdapter(firebasePagerAdapter);

    }

    private class FirebasePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> surveyFragments = new ArrayList<>();
        private List<String> surveyTitles = new ArrayList<>();

        private DatabaseReference rootRef, surveyReference, surveyDoneRef;

        FirebasePagerAdapter(FragmentManager fm) {
            super(fm);
            rootRef = FirebaseDatabase.getInstance().getReference();
            surveyReference = rootRef.child("survey");
            surveyDoneRef = rootRef.child("survey_done");

            Query query = surveyReference.orderByChild("visibility").equalTo(1);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    surveyFragments.clear();
                    surveyTitles.clear();

                    int surveyCount = 0;
                    for (DataSnapshot surveySnapshot : dataSnapshot.getChildren()) {
                        SurveyBean survey = surveySnapshot.getValue(SurveyBean.class);

                        if (survey != null
                                && (survey.getDegree().equals(ALL) || survey.getDegree().equals(current_deg))
                                && (survey.getDepartment().equals(ALL) || survey.getDepartment().equals(current_dept))
                                && (survey.getYear().equals(ALL) || survey.getYear().equals(current_year))) {

                            long timeDiff = System.currentTimeMillis() - survey.getTime();

                            if (timeDiff < 86400000) {
                                surveyCount++;
                                final String surveyKey = surveySnapshot.getKey();

                                surveyFragments.add(OneSurveyFragment.newInstance(surveyKey, survey, surveyCount));
                                surveyTitles.add("Survey " + surveyCount);
                            } else {
                                surveySnapshot.child("visibility").getRef().setValue(0);
                            }

                        }

                    }
                    notifyDataSetChanged();
                    loadingIndicator.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        @Override
        public Fragment getItem(int position) {
            return surveyFragments.get(position);
        }

        @Override
        public int getCount() {
            return surveyFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return surveyTitles.get(position);
        }
    }

}
