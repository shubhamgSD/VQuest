package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnsFragment extends Fragment {

    QuestionBean questionBean;
    String key;
    FirebaseListAdapter<AnswerBean> firebaseListAdapter;
    int flag=0;


    private Button likeBtn;
    private ListView listView;
    private TextView username_view,time_view,qstring,views_view;

    public AnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_ans, container, false);

        listView = (ListView) view.findViewById(R.id.ans_lv);
        Button btn = (Button) view.findViewById(R.id.btn_answer);

         qstring = (TextView) view.findViewById(R.id.qstring_view1);
         username_view = (TextView) view.findViewById(R.id.uname_view2);
         time_view = (TextView) view.findViewById(R.id.time_view2);
         views_view = (TextView) view.findViewById(R.id.views_view1);


        if(getArguments()!=null)
        {
            key = getArguments().getString("key");
        }


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference answerRef = rootRef.child("answer").child(key);
        DatabaseReference queRef = rootRef.child("question");

        firebaseListAdapter = new FirebaseListAdapter<AnswerBean>(getActivity(),
                AnswerBean.class,R.layout.custom_answer_layout,answerRef) {
            @Override
            protected void populateView(View v, AnswerBean model, int position) {

            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                // super.getView(position, view, viewGroup);
                if (view == null) {
                   // Log.d("check inflate >","view= "+view);
                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_answer_layout, viewGroup, false);
                   // Log.d("check  after inflate >","view= "+view);
                }



                    AnswerBean model = getItem(position);
                    likeBtn = (Button) view.findViewById(R.id.btn_like);
                    likeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = listView.getPositionForView((View) v.getParent());
                            Log.d("Position",position+"");
                            final DatabaseReference databaseReference = firebaseListAdapter.getRef(position);
                            DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference likeref = rootref.child("answer_like");
                            likeref.child(key).child(databaseReference.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(0);
                            likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    databaseReference.child("likes").setValue(dataSnapshot.child(key).child(databaseReference.getKey()).getChildrenCount());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                    TextView astringView = (TextView) view.findViewById(R.id.astring_view);
                TextView username = (TextView) view.findViewById(R.id.uname_view2);
                TextView time = (TextView) view.findViewById(R.id.time_view2);
                TextView likeView = (TextView) view.findViewById(R.id.likes_view);

                    astringView.setText(model.getAstring());
                    username.setText(model.getUsername());
                    likeView.setText(model.getLikes()+" likes");
                    Date date = new Date(model.getTime());
                    time.setText(date.toString());
                return view;

            }


        };

      queRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                  if(messageSnapshot.getKey().equals(key))
                  {
                      questionBean = messageSnapshot.getValue(QuestionBean.class);
                      qstring.setText(questionBean.getQstring());
                      username_view.setText(questionBean.getUsername());
                      views_view.setText(questionBean.getViews()+" views");
                      Date date = new Date(questionBean.getTime());
                      time_view.setText(date.toString());

              }
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });



     listView.setAdapter(firebaseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "On click", Toast.LENGTH_SHORT).show();
            }
        });


       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Bundle bundle  = new Bundle();
               bundle.putString("key",key);
               Fragment fragment = new AnsOfQuestion();
               fragment.setArguments(bundle);
               getFragmentManager().beginTransaction()
                       .replace(R.id.fragment, fragment)
                       .addToBackStack("questionfragment")
                       .commit();
           }
       });

        return view;

    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.like_btn:
//            int position = listView.getPositionForView((View) v.getParent());
//            Log.d("Position",position+"");
//            break;
//
//
//        }
//
//    }
}
