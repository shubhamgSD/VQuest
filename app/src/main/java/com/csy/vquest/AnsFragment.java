package com.csy.vquest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnsFragment extends Fragment {

    QuestionBean message;
    String key;
    FirebaseListAdapter<AnswerBean> firebaseListAdapter;
    int flag=0;
    public AnsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_ans, container, false);

        ListView listView = (ListView) view.findViewById(R.id.ans_lv);
        Button btn = (Button) view.findViewById(R.id.button3);
        final TextView tv9 = (TextView) view.findViewById(R.id.textView9);

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

                    TextView astringView = (TextView) view.findViewById(R.id.tv3);
                    astringView.setText(model.getAstring());

                return view;

            }


        };

      queRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                  if(messageSnapshot.getKey().equals(key))
                  {
                      message = messageSnapshot.getValue(QuestionBean.class);
                      tv9.setText(message.getQstring());
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

}
