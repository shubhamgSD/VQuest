package com.csy.vquest;

/**
 * Created by yash_mishra on 3/16/2018.
 */

public class AnnouncementBean {
    private String astring;
    private String degree;
    private String department;
    private long time;
    private String username;
    private long visibility;
    private String year;

    public AnnouncementBean() {

    }

    public AnnouncementBean(String astring, String degree, String department, long time, String username,long visibility, String year) {
        this.astring = astring;
        this.degree = degree;
        this.department = department;
        this.time = time;
        this.username = username;
        this.visibility = visibility;
        this.year = year;
    }

    public long getVisibility() {
        return visibility;
    }

    public void setVisibility(long visibility) {
        this.visibility = visibility;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAstring() {
        return astring;
    }

    public void setAstring(String astring) {
        this.astring = astring;
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

}
