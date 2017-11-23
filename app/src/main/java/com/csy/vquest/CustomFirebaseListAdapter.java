package com.csy.vquest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

/**
 * Created by shubhamgupta on 7/11/17.
 */
public class CustomFirebaseListAdapter extends FirebaseListAdapter<QuestionBean> {

    private Context mContext;
    private int mLayout;

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

        if(model.getQanonymity() == 0)
            unameView.setText(model.getUsername());
        else
            unameView.setText("Anonymous");

        viewsView.setText(String.valueOf(model.getViews()) + " views");

        Date date = new Date(model.getTime());
        timeView.setText(date.toString());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


        return view;

    }

    @Override
    protected void populateView(View v, QuestionBean model, int position) {

    }

}
