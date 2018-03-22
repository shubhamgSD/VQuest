package com.csy.vquest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chirag on 07-03-2018.
 */

public class CustomSurveyFirebaseAdapter extends FirebaseListAdapter<SurveyBean> {

    private Context mContext;
    private int mLayout;

    public CustomSurveyFirebaseAdapter(Context context, Class<SurveyBean> modelClass, int modelLayout, Query query) {
        super(context, modelClass, modelLayout, query);
        this.mContext = context;
        this.mLayout = modelLayout;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            Log.d("check inflate >", "view= " + view);
            view = LayoutInflater.from(mContext).inflate(this.mLayout, viewGroup, false);
            Log.d("check  after inflate >", "view= " + view);
        }

        SurveyBean surveyBeanModel = getItem(position);

        TextView unameView = (TextView) view.findViewById(R.id.uname_view2);
        TextView timeView = (TextView) view.findViewById(R.id.time_view2);
        TextView sstringView = (TextView) view.findViewById(R.id.sstring_view);

        if (surveyBeanModel.getSanonymity() == 0) {
            unameView.setText(surveyBeanModel.getUsername());
            unameView.setTextColor(Color.parseColor("#0000EE"));
        } else {
            unameView.setText("Anonymous");
            unameView.setTextColor(Color.parseColor("#FF4B4A4B"));
        }

        Date date = new Date(surveyBeanModel.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        timeView.setText(day + "/" + month + "/" + year);
        sstringView.setText(surveyBeanModel.getSstring());

        return view;
    }

    @Override
    protected void populateView(View v, SurveyBean model, int position) {

    }
}
