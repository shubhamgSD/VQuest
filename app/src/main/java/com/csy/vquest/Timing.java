package com.csy.vquest;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.csy.vquest.NavigationDrawerActivity.current_deg;
import static com.csy.vquest.NavigationDrawerActivity.current_dept;
import static com.csy.vquest.NavigationDrawerActivity.current_year;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timing extends Fragment {

    private static final String ARG_TIMING = "timing";

    private Button editbtn;
    private ViewPager viewPager;
    private EditText subOne;
    private EditText subTwo;
    private EditText subThree;
    private EditText subFour;
    private EditText subFive;
    private EditText subSix;
    private EditText subSeven;
    private EditText subEight;
    private EditText teaOne;
    private EditText teaTwo;
    private EditText teaThree;
    private EditText teaFour;
    private EditText teaFive;
    private EditText teaSix;
    private EditText teaSeven;
    private EditText teaEight;
    private EditText loc;

    private TimingBean timingBean;


    int count=0;

    public Timing() {
        // Required empty public constructor
    }

    public static Timing newInstance(TimingBean timingBean) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_TIMING, timingBean);

        Timing fragment = new Timing();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timing, container, false);
        editbtn = (Button) view.findViewById(R.id.edit_timetable);
        viewPager = (ViewPager) container.getRootView().findViewById(R.id.viewpager);
        subOne = (EditText) view.findViewById(R.id.sub_one);
        subTwo = (EditText) view.findViewById(R.id.sub_two);
        subThree = (EditText) view.findViewById(R.id.sub_three);
        subFour = (EditText) view.findViewById(R.id.sub_four);
        subFive = (EditText) view.findViewById(R.id.sub_five);
        subSix = (EditText) view.findViewById(R.id.sub_six);
        subSeven = (EditText) view.findViewById(R.id.sub_seven);
        subEight = (EditText) view.findViewById(R.id.sub_eight);
        teaOne  = (EditText) view.findViewById(R.id.teacher_one);
        teaTwo  = (EditText) view.findViewById(R.id.teacher_two);
        teaThree  = (EditText) view.findViewById(R.id.teacher_three);
        teaFour  = (EditText) view.findViewById(R.id.teacher_four);
        teaFive = (EditText) view.findViewById(R.id.teacher_five);
        teaSix = (EditText) view.findViewById(R.id.teacher_six);
        teaSeven  = (EditText) view.findViewById(R.id.teacher_seven);
        teaEight  = (EditText) view.findViewById(R.id.teacher_eight);
        loc  = (EditText) view.findViewById(R.id.location);

        timingBean = getArguments().getParcelable(ARG_TIMING);
        subOne.setText(timingBean.getSubone());
        subTwo.setText(timingBean.getSubtwo());
        subThree.setText(timingBean.getSubthree());
        subFour.setText(timingBean.getSubfour());
        subFive.setText(timingBean.getSubfive());
        subSix.setText(timingBean.getSubsix());
        subSeven.setText(timingBean.getSubseven());
        subEight.setText(timingBean.getSubeight());

        teaOne.setText(timingBean.getTeaone());
        teaTwo.setText(timingBean.getTeatwo());
        teaThree.setText(timingBean.getTeathree());
        teaFour.setText(timingBean.getTeafour());
        teaFive.setText(timingBean.getTeafive());
        teaSix.setText(timingBean.getTeasix());
        teaSeven.setText(timingBean.getTeaseven());
        teaEight.setText(timingBean.getTeaeight());

        loc.setText(timingBean.getLocation());


        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(), viewPager.getCurrentItem()+"", Toast.LENGTH_SHORT).show();

                if(count==0) {
                    Toast.makeText(getContext(), "Edit Timetable", Toast.LENGTH_SHORT).show();
                    makeEditable();
                    editbtn.setText("Save");
                    editbtn.setTextColor(Color.parseColor("#208f5d"));
                    teaOne.setHint("Teacher");teaTwo.setHint("Teacher");teaThree.setHint("Teacher");
                    teaFour.setHint("Teacher");teaFive.setHint("Teacher");teaSix.setHint("Teacher");
                    teaSeven.setHint("Teacher");teaEight.setHint("Teacher");
                    subOne.setHint("Enter Subject");subTwo.setHint("Enter Subject");subThree.setHint("Enter Subject");
                    subFour.setHint("Enter Subject");subFive.setHint("Enter Subject");subSix.setHint("Enter Subject");
                    subSeven.setHint("Enter Subject");subEight.setHint("Enter Subject");
                    count=1;
                }
                else
                {
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    DatabaseReference timetableref = FirebaseDatabase.getInstance().getReference().child("timetable");
                    DatabaseReference timingref = timetableref.child(current_dept).child(current_deg).child(current_year);
                    DatabaseReference dayref = timingref.child(findDay(viewPager.getCurrentItem()));

                   dayref.child("subone").setValue(subOne.getText().toString());
                    dayref.child("subtwo").setValue(subTwo.getText().toString());
                    dayref.child("subthree").setValue(subThree.getText().toString());
                    dayref.child("subfour").setValue(subFour.getText().toString());
                    dayref.child("subfive").setValue(subFive.getText().toString());
                    dayref.child("subsix").setValue(subSix.getText().toString());
                    dayref.child("subseven").setValue(subSeven.getText().toString());
                    dayref.child("subeight").setValue(subEight.getText().toString());

                    dayref.child("teaone").setValue(teaOne.getText().toString());
                    dayref.child("teatwo").setValue(teaTwo.getText().toString());
                    dayref.child("teathree").setValue(teaThree.getText().toString());
                    dayref.child("teafour").setValue(teaFour.getText().toString());
                    dayref.child("teafive").setValue(teaFive.getText().toString());
                    dayref.child("teasix").setValue(teaSix.getText().toString());
                    dayref.child("teaseven").setValue(teaSeven.getText().toString());
                    dayref.child("teaeight").setValue(teaEight.getText().toString());

                    dayref.child("location").setValue(loc.getText().toString());
                    teaOne.setHint("");teaTwo.setHint("");teaThree.setHint("");
                    teaFour.setHint("");teaFive.setHint("");teaSix.setHint("");
                    teaSeven.setHint("");teaEight.setHint("");
                    subOne.setHint("");subTwo.setHint("");subThree.setHint("");
                    subFour.setHint("");subFive.setHint("");subSix.setHint("");
                    subSeven.setHint("");subEight.setHint("");


                    makeNonEditable();

                    editbtn.setText("Edit");
                    editbtn.setTextColor(Color.parseColor("#003366"));
                    count=0;
                }





            }
        });

        return view;

    }

    public int findPosition(String key){

        switch(key){
            case "Mon":
                return 0;
            case "Tue":
                return 1;
            case "Wed":
                return 2;
            case "Thu":
                return 3;
            case "Fri":
                return 4;
            case "Sat":
                return 5;
            case "Sun":
                return 6;
            default:
                return 0;
        }

    }

    public String findDay(int num)
    {
        if(num==0)
        {
            return "Mon";
        }
        else if(num==1)
        {
            return "Tue";
        }
        else if(num==2)
        {
            return "Wed";
        }
        else if(num==3)
        {
            return "Thu";
        }
        else if(num==4)
        {
            return "Fri";
        }
        else if(num==5)
        {
            return "Sat";
        }
        else if(num==6)
        {
            return "Sun";
        }

        return null;
    }

    public void makeEditable()
    {
        subOne.setClickable(true);
        subOne.setCursorVisible(true);
        subOne.setFocusable(true);
        subOne.setFocusableInTouchMode(true);

        subTwo.setClickable(true);
        subTwo.setCursorVisible(true);
        subTwo.setFocusable(true);
        subTwo.setFocusableInTouchMode(true);

        subThree.setClickable(true);
        subThree.setCursorVisible(true);
        subThree.setFocusable(true);
        subThree.setFocusableInTouchMode(true);

        subFour.setClickable(true);
        subFour.setCursorVisible(true);
        subFour.setFocusable(true);
        subFour.setFocusableInTouchMode(true);

        subFive.setClickable(true);
        subFive.setCursorVisible(true);
        subFive.setFocusable(true);
        subFive.setFocusableInTouchMode(true);

        subSix.setClickable(true);
        subSix.setCursorVisible(true);
        subSix.setFocusable(true);
        subSix.setFocusableInTouchMode(true);

        subSeven.setClickable(true);
        subSeven.setCursorVisible(true);
        subSeven.setFocusable(true);
        subSeven.setFocusableInTouchMode(true);

        subEight.setClickable(true);
        subEight.setCursorVisible(true);
        subEight.setFocusable(true);
        subEight.setFocusableInTouchMode(true);

        teaOne.setClickable(true);
        teaOne.setCursorVisible(true);
        teaOne.setFocusable(true);
        teaOne.setFocusableInTouchMode(true);

        teaTwo.setClickable(true);
        teaTwo.setCursorVisible(true);
        teaTwo.setFocusable(true);
        teaTwo.setFocusableInTouchMode(true);

        teaThree.setClickable(true);
        teaThree.setCursorVisible(true);
        teaThree.setFocusable(true);
        teaThree.setFocusableInTouchMode(true);

        teaFour.setClickable(true);
        teaFour.setCursorVisible(true);
        teaFour.setFocusable(true);
        teaFour.setFocusableInTouchMode(true);

        teaFive.setClickable(true);
        teaFive.setCursorVisible(true);
        teaFive.setFocusable(true);
        teaFive.setFocusableInTouchMode(true);

        teaSix.setClickable(true);
        teaSix.setCursorVisible(true);
        teaSix.setFocusable(true);
        teaSix.setFocusableInTouchMode(true);

        teaSeven.setClickable(true);
        teaSeven.setCursorVisible(true);
        teaSeven.setFocusable(true);
        teaSeven.setFocusableInTouchMode(true);

        teaEight.setClickable(true);
        teaEight.setCursorVisible(true);
        teaEight.setFocusable(true);
        teaEight.setFocusableInTouchMode(true);

        loc.setClickable(true);
        loc.setCursorVisible(true);
        loc.setFocusable(true);
        loc.setFocusableInTouchMode(true);


    }

    public void makeNonEditable()
    {
        subOne.setClickable(false);
        subOne.setCursorVisible(false);
        subOne.setFocusable(false);
        subOne.setFocusableInTouchMode(false);

        subTwo.setClickable(false);
        subTwo.setCursorVisible(false);
        subTwo.setFocusable(false);
        subTwo.setFocusableInTouchMode(false);

        subThree.setClickable(false);
        subThree.setCursorVisible(false);
        subThree.setFocusable(false);
        subThree.setFocusableInTouchMode(false);

        subFour.setClickable(false);
        subFour.setCursorVisible(false);
        subFour.setFocusable(false);
        subFour.setFocusableInTouchMode(false);

        subFive.setClickable(false);
        subFive.setCursorVisible(false);
        subFive.setFocusable(false);
        subFive.setFocusableInTouchMode(false);

        subSix.setClickable(false);
        subSix.setCursorVisible(false);
        subSix.setFocusable(false);
        subSix.setFocusableInTouchMode(false);

        subSeven.setClickable(false);
        subSeven.setCursorVisible(false);
        subSeven.setFocusable(false);
        subSeven.setFocusableInTouchMode(false);

        subEight.setClickable(false);
        subEight.setCursorVisible(false);
        subEight.setFocusable(false);
        subEight.setFocusableInTouchMode(false);

        teaOne.setClickable(false);
        teaOne.setCursorVisible(false);
        teaOne.setFocusable(false);
        teaOne.setFocusableInTouchMode(false);

        teaTwo.setClickable(false);
        teaTwo.setCursorVisible(false);
        teaTwo.setFocusable(false);
        teaTwo.setFocusableInTouchMode(false);

        teaThree.setClickable(false);
        teaThree.setCursorVisible(false);
        teaThree.setFocusable(false);
        teaThree.setFocusableInTouchMode(false);

        teaFour.setClickable(false);
        teaFour.setCursorVisible(false);
        teaFour.setFocusable(false);
        teaFour.setFocusableInTouchMode(false);

        teaFive.setClickable(false);
        teaFive.setCursorVisible(false);
        teaFive.setFocusable(false);
        teaFive.setFocusableInTouchMode(false);

        teaSix.setClickable(false);
        teaSix.setCursorVisible(false);
        teaSix.setFocusable(false);
        teaSix.setFocusableInTouchMode(false);

        teaSeven.setClickable(false);
        teaSeven.setCursorVisible(false);
        teaSeven.setFocusable(false);
        teaSeven.setFocusableInTouchMode(false);

        teaEight.setClickable(false);
        teaEight.setCursorVisible(false);
        teaEight.setFocusable(false);
        teaEight.setFocusableInTouchMode(false);


        loc.setClickable(false);
        loc.setCursorVisible(false);
        loc.setFocusable(false);
        loc.setFocusableInTouchMode(false);

    }



}
