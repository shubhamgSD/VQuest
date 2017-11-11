package com.csy.vquest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import java.util.Date;

/**
 * Created by shubhamgupta on 7/11/17.
 */
public class CustomFirebaseListAdapter extends FirebaseListAdapter<QuestionBean> {

    private  Context mContext;
    private int mLayout;

    public CustomFirebaseListAdapter(Context context, Class modelClass,int mLayout,
                                     Query query) {
        super(context, modelClass,mLayout, query);
        mContext = context;
        this.mLayout = mLayout;

    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       // super.getView(position, view, viewGroup);
        if (view == null) {
            Log.d("check inflate >","view= "+view);
            view = LayoutInflater.from(mContext).inflate(this.mLayout, viewGroup, false);
            Log.d("check  after inflate >","view= "+view);
        }


            QuestionBean model = getItem(position);

            TextView qstringView = (TextView) view.findViewById(R.id.textView1);
            TextView categoryView = (TextView) view.findViewById(R.id.textView2);

            qstringView.setText(model.getQstring());
            categoryView.setText(model.getCategory());

        return view;

    }

    @Override
    protected void populateView(View v, QuestionBean model, int position) {

    }

}
