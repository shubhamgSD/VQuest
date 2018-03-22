package com.csy.vquest;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searchable_Activity extends AppCompatActivity {

    Map<String, String> map;
    int count=0;
    String[] questions;
    int i=-1;
    ListView lv;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_);
        map= new HashMap<String,String>();
        lv = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,query+" searchable", Toast.LENGTH_LONG).show();

              findQuestions(query);
        }

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                   for(Map.Entry m:map.entrySet()) {
                       if(m.getValue().toString().equals(questions[position]))
                       {
                           key = m.getKey().toString();
                           NavigationDrawerActivity.searchKey = key;
                           onBackPressed();

//                           Bundle bundle  = new Bundle();
//                           bundle.putString("key",key);
//
//                           Fragment fragment = new AnsFragment();
//                           fragment.setArguments(bundle);
//                           getSupportFragmentManager().beginTransaction()
//                                   .replace(R.id.fragment, fragment, "ans_fragment")
//                                   .commit();
                       }

                   }
           }
       });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void findQuestions(final String search)
    {


        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("question");
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i = (int) dataSnapshot.getChildrenCount();
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    QuestionBean questionBean = snapshot.getValue(QuestionBean.class);
                    if(matchSearch(search,questionBean.getQstring())==true) {
                        map.put(snapshot.getKey(), questionBean.getQstring());
                    }
                    i--;

                    count++;
                    if(i==0) {
                       // Toast.makeText(Searchable_Activity.this, count + " count", Toast.LENGTH_SHORT).show();
                        showMap();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showMap()
    {
        questions = new String[map.size()];
        int i=0;
        Toast.makeText(Searchable_Activity.this, map+"", Toast.LENGTH_SHORT).show();
        for(Map.Entry m:map.entrySet()){
            questions[i]=m.getValue().toString();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,questions);
        lv.setAdapter(adapter);

    }


    public boolean matchSearch(String substring,String question)
    {
        String[] wordsOfSub = substring.split("\\s");
        int w_len = wordsOfSub.length;
        String[] wordsOfQue = question.split("\\s");
        int q_len = wordsOfQue.length;
        int wcount=0;
        for(int i=0;i<w_len;i++)
        {
            for(int j=0;j<q_len;j++)
            {
                if(wordsOfSub[i].equalsIgnoreCase(wordsOfQue[j]))
                {
                    wcount++;
                    break;
                }
            }
        }

        if(wcount==w_len)
            return true;
        else
            return false;
    }
}


