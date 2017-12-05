package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyAnswersFragment extends Fragment {

    private ListView listView;

    private FirebaseListAdapter<QuestionBean> questionBeanFirebaseListAdapter;
    private FirebaseListAdapter<AnswerBean> answerBeanFirebaseListAdapter;

    public MyAnswersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_answers, container, false);

        getActivity().setTitle("My Answers");

        listView = (ListView) view.findViewById(R.id.listViewMyAnswers);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference questionRef = rootRef.child("question");
        DatabaseReference answerRef = rootRef.child("answer");

        questionBeanFirebaseListAdapter = new FirebaseListAdapter<QuestionBean>(getActivity(),
                QuestionBean.class, R.layout.myanswer_item_card, questionRef) {
            @Override
            protected void populateView(View v, QuestionBean model, int position) {



            }
        };

        return view;

    }

}
