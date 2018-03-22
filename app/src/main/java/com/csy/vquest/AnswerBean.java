package com.csy.vquest;

/**
 * Created by shubhamgupta on 7/11/17.
 */

public class AnswerBean {

    private int aanonymity;
    private int aedited;
    private String astring;
    private int likes;
    private int r_no;
    private long time;
    private String username;
    private int visibility;

    public AnswerBean() {
    }

    public AnswerBean(int aanonymity, int aedited, String astring, int likes, int r_no, long time, String username, int visibility) {
        this.aanonymity = aanonymity;
        this.aedited = aedited;
        this.astring = astring;
        this.likes = likes;
        this.r_no = r_no;
        this.time = time;
        this.username = username;
        this.visibility = visibility;
    }

    public int getaanonymity() {
        return aanonymity;
    }

    public void setaanonymity(int aanonymity) {
        this.aanonymity = aanonymity;
    }

    public int getAedited() {
        return aedited;
    }

    public void setAedited(int aedited) {
        this.aedited = aedited;
    }

    public String getAstring() {
        return astring;
    }

    public void setAstring(String astring) {
        this.astring = astring;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
