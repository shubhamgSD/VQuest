package com.csy.vquest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shubhamgupta on 7/11/17.
 */
public class CustomFirebaseListAdapter extends FirebaseListAdapter<QuestionBean> {

    private Context mContext;
    private int mLayout;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int seconds;


    public CustomFirebaseListAdapter(Context context, Class modelClass, int mLayout,
                                     Query query) {
        super(context, modelClass, mLayout, query);
        mContext = context;
        this.mLayout = mLayout;

    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // super.getView(position, view, viewGroup);
        if (view == null) {
            Log.d("check inflate >", "view= " + view);
            view = LayoutInflater.from(mContext).inflate(this.mLayout, viewGroup, false);
            Log.d("check  after inflate >", "view= " + view);
        }


        QuestionBean model = getItem(position);

        TextView qstringView = (TextView) view.findViewById(R.id.qstring_view);
        TextView categoryView = (TextView) view.findViewById(R.id.category_view);
        TextView unameView = (TextView) view.findViewById(R.id.uname_view2);
        TextView timeView = (TextView) view.findViewById(R.id.time_view2);
        TextView viewsView = (TextView) view.findViewById(R.id.views_view);
        TextView replyView = (TextView) view.findViewById(R.id.reply_view);

        qstringView.setText(model.getQstring());
        categoryView.setText(model.getCategory());

        if(model.getCategory().equals("Events"))
        {

            categoryView.setTextColor(Color.parseColor("#303F9F"));
        }
        else if(model.getCategory().equals("General"))
        {

            categoryView.setTextColor(Color.parseColor("#FF3DB927"));
        }
        else if(model.getCategory().equals("Common Academic"))
        {

            categoryView.setTextColor(Color.parseColor("#FFF4F727"));
        }
        else if(model.getCategory().equals("Academic-CSE"))
        {

            categoryView.setTextColor(Color.parseColor("#FFFF0000"));
        }
        else if(model.getCategory().equals("Academic-ECE"))
        {

            categoryView.setTextColor(Color.parseColor("#FFE433D8"));
        }
        else if(model.getCategory().equals("Academic-ME"))
        {

            categoryView.setTextColor(Color.parseColor("#FFF98F33"));
        }
        else if(model.getCategory().equals("Hostel"))
        {

            categoryView.setTextColor(Color.parseColor("#FFC73FE9"));
        }
        else if(model.getCategory().equals("Mess"))
        {

            categoryView.setTextColor(Color.parseColor("#FF30CEC0"));
        }


        if(model.getQanonymity() == 0) {
            unameView.setText(model.getUsername());
            unameView.setTextColor(Color.parseColor("#0000EE"));

        }
        else {
            unameView.setText("Anonymous");
            unameView.setTextColor(Color.parseColor("#FF4B4A4B"));
        }
        viewsView.setText(String.valueOf(model.getViews()) + " views");

        Date date = new Date(model.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND);
        timeView.setText(day+"/"+month+"/"+year);

        replyView.setText(model.getReplies()+" replies");


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


        return view;

    }

    @Override
    protected void populateView(View v, QuestionBean model, int position) {

    }

}
