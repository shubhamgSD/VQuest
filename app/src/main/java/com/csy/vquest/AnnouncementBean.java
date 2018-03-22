package com.csy.vquest;

/**
 * Created by yash_mishra on 3/16/2018.
 */

public class AnnouncementBean {
    private String astring;
    private long time;
    private String username;

    public AnnouncementBean(int aanonymity, String astring, long time, String username) {
        this.astring = astring;
        this.time = time;
        this.username = username;
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

    public AnnouncementBean(){

    }
}
