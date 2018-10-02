package com.csy.vquest;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.csy.vquest.NavigationDrawerActivity.current_uname;

public class MySurveyCreatedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIndicator;
    private FirebasePagerAdapter firebasePagerAdapter;
    private boolean isAlreadyRendered = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Surveys I created");

        View view = inflater.inflate(R.layout.fragment_my_survey_created, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_survey_created);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_survey_created);
        loadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        if (!isAlreadyRendered) {
            loadingIndicator.setVisibility(View.VISIBLE);
            firebasePagerAdapter = new FirebasePagerAdapter(getChildFragmentManager());
            isAlreadyRendered = true;
        }

        return view;
    }

    private class FirebasePagerAdapter extends FragmentStatePagerAdapter{

        private List<Fragment> surveyFragments = new ArrayList<>();
        private List<String> surveyTitles = new ArrayList<>();

        private DatabaseReference surveyReference;

        FirebasePagerAdapter(FragmentManager fm) {
            super(fm);

            surveyReference = FirebaseDatabase.getInstance().getReference().child("survey");

            Query query = surveyReference.orderByChild("username").equalTo(current_uname);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    surveyFragments.clear();
                    surveyTitles.clear();

                    int surveyCount = 0;
                    for(DataSnapshot surveySnapshot : dataSnapshot.getChildren()){
                        surveyCount++;
                        String surveyKey = surveySnapshot.getKey();
                        SurveyBean survey = surveySnapshot.getValue(SurveyBean.class);
                        surveyFragments.add(OneSurveyCreatedFragment.newInstance(surveyKey, survey));
                        surveyTitles.add("Survey " + surveyCount);
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

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(firebasePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
