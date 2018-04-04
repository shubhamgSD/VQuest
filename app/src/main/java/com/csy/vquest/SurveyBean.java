package com.csy.vquest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chirag on 07-03-2018.
 */

public class SurveyBean implements Parcelable {

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int r_no;
    private int sanonymity;
    private String sstring;
    private long time;
    private String username;
    private int views;
    private int visibility;

    public SurveyBean() {
    }

    public SurveyBean(String option1, String option2, String option3, String option4, int r_no, int sanonymity, String sstring, long time, String username, int views, int visibility) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.r_no = r_no;
        this.sanonymity = sanonymity;
        this.sstring = sstring;
        this.time = time;
        this.username = username;
        this.views = views;
        this.visibility = visibility;
    }

    protected SurveyBean(Parcel in) {
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        r_no = in.readInt();
        sanonymity = in.readInt();
        sstring = in.readString();
        time = in.readLong();
        username = in.readString();
        views = in.readInt();
        visibility = in.readInt();
    }

    public static final Creator<SurveyBean> CREATOR = new Creator<SurveyBean>() {
        @Override
        public SurveyBean createFromParcel(Parcel in) {
            return new SurveyBean(in);
        }

        @Override
        public SurveyBean[] newArray(int size) {
            return new SurveyBean[size];
        }
    };

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getR_no() {
        return r_no;
    }

    public void setR_no(int r_no) {
        this.r_no = r_no;
    }

    public int getSanonymity() {
        return sanonymity;
    }

    public void setSanonymity(int sanonymity) {
        this.sanonymity = sanonymity;
    }

    public String getSstring() {
        return sstring;
    }

    public void setSstring(String sstring) {
        this.sstring = sstring;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(r_no);
        dest.writeInt(sanonymity);
        dest.writeString(sstring);
        dest.writeLong(time);
        dest.writeString(username);
        dest.writeInt(views);
        dest.writeInt(visibility);
    }
}
