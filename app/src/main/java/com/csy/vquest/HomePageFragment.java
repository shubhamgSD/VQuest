package com.csy.vquest;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class HomePageFragment extends Fragment {

    private FloatingActionButton floatBtn;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Home");

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        setHasOptionsMenu(true);

        ListView listView = (ListView) view.findViewById(R.id.listViewHome);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference questionRef = rootRef.child("question");

        FirebaseListAdapter<QuestionBean> firebaseListAdapter = new FirebaseListAdapter<QuestionBean>(
                getActivity(),
                QuestionBean.class,
                R.layout.question_display_layout,
                questionRef
        ) {
            @Override
            protected void populateView(View v, QuestionBean model, int position) {

                TextView usernameView = (TextView) v.findViewById(R.id.usernameView1);
                TextView timeView = (TextView) v.findViewById(R.id.timeView1);
                TextView editedView = (TextView) v.findViewById(R.id.editedView1);
                TextView qstringView = (TextView) v.findViewById(R.id.qstringView1);
                TextView categoryView = (TextView) v.findViewById(R.id.categoryView1);
                TextView viewsView = (TextView) v.findViewById(R.id.viewsView1);

                if(model.getQanonymity() == 1)
                    usernameView.setText("Anonymous");
                else
                    usernameView.setText(model.getUsername());

                Date date = new Date(model.getTime());
                timeView.setText(date.toString());

                if(model.getQedited() == 1)
                    editedView.setVisibility(View.VISIBLE);
                else
                    editedView.setVisibility(View.INVISIBLE);

                qstringView.setText(model.getQstring());
                categoryView.setText(model.getCategory());
                viewsView.setText(String.valueOf(model.getViews()) + " views");

            }
        };

        listView.setAdapter(firebaseListAdapter);

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

}
