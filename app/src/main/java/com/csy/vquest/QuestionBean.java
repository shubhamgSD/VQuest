package com.csy.vquest;

/**
 * Created by Chirag on 04-11-2017.
 */

public class QuestionBean {

    private String category;
    private int qanonymity;
    private int qedited;
    private String qstring;
    private int r_no;
    private long time;
    private String username;
    private int views;
    private int visibility;
    private int replies;

    public QuestionBean() {
    }

    public QuestionBean(String category, int qanonymity, int qedited, String qstring, int r_no, long time, String username, int views, int visibility, int replies) {
        this.category = category;
        this.qanonymity = qanonymity;
        this.qedited = qedited;
        this.qstring = qstring;
        this.r_no = r_no;
        this.time = time;
        this.username = username;
        this.views = views;
        this.visibility = visibility;
        this.replies = replies;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getQanonymity() {
        return qanonymity;
    }

    public void setQanonymity(int qanonymity) {
        this.qanonymity = qanonymity;
    }

    public int getQedited() {
        return qedited;
    }

    public void setQedited(int qedited) {
        this.qedited = qedited;
    }

    public int getR_no() {
        return r_no;
    }

    public void setR_no(int r_no) {
        this.r_no = r_no;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getQstring() {
        return qstring;
    }

    public void setQstring(String qstring) {
        this.qstring = qstring;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
