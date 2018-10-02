package com.csy.vquest;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

/**
 * Created by shubham on 26/4/18.
 */

class TimingBean implements Parcelable {

    private String subone;
    private String subtwo;
    private String subthree;
    private String subfour;
    private String subfive;
    private String subsix;
    private String subseven;
    private String subeight;
    private String teaone;
    private String teatwo;
    private String teathree;
    private String teafour;
    private String teafive;
    private String teasix;
    private String teaseven;
    private String teaeight;
    private String location;

    public TimingBean(){

    }

    public TimingBean(String location, String subeight, String subfive,
                      String subfour, String subone, String subseven,
                      String subsix, String subthree, String subtwo,
                      String teaeight, String teafive, String teafour,
                      String teaone, String teaseven, String teasix,
                      String teathree, String teatwo) {

        this.subone = subone;
        this.subtwo = subtwo;
        this.subthree = subthree;
        this.subfour = subfour;
        this.subfive = subfive;
        this.subsix = subsix;
        this.subseven = subseven;
        this.subeight = subeight;
        this.teaone = teaone;
        this.teatwo = teatwo;
        this.teathree = teathree;
        this.teafour = teafour;
        this.teafive = teafive;
        this.teasix = teasix;
        this.teaseven = teaseven;
        this.teaeight = teaeight;
        this.location = location;
    }

    public String getSubone() {
        return subone;
    }

    public void setSubone(String subone) {
        this.subone = subone;
    }

    public String getSubtwo() {
        return subtwo;
    }

    public void setSubtwo(String subtwo) {
        this.subtwo = subtwo;
    }

    public String getSubthree() {
        return subthree;
    }

    public void setSubthree(String subthree) {
        this.subthree = subthree;
    }

    public String getSubfour() {
        return subfour;
    }

    public void setSubfour(String subfour) {
        this.subfour = subfour;
    }

    public String getSubfive() {
        return subfive;
    }

    public void setSubfive(String subfive) {
        this.subfive = subfive;
    }

    public String getSubsix() {
        return subsix;
    }

    public void setSubsix(String subsix) {
        this.subsix = subsix;
    }

    public String getSubseven() {
        return subseven;
    }

    public void setSubseven(String subseven) {
        this.subseven = subseven;
    }

    public String getSubeight() {
        return subeight;
    }

    public void setSubeight(String subeight) {
        this.subeight = subeight;
    }

    public String getTeaone() {
        return teaone;
    }

    public void setTeaone(String teaone) {
        this.teaone = teaone;
    }

    public String getTeatwo() {
        return teatwo;
    }

    public void setTeatwo(String teatwo) {
        this.teatwo = teatwo;
    }

    public String getTeathree() {
        return teathree;
    }

    public void setTeathree(String teathree) {
        this.teathree = teathree;
    }

    public String getTeafour() {
        return teafour;
    }

    public void setTeafour(String teafour) {
        this.teafour = teafour;
    }

    public String getTeafive() {
        return teafive;
    }

    public void setTeafive(String teafive) {
        this.teafive = teafive;
    }

    public String getTeasix() {
        return teasix;
    }

    public void setTeasix(String teasix) {
        this.teasix = teasix;
    }

    public String getTeaseven() {
        return teaseven;
    }

    public void setTeaseven(String teaseven) {
        this.teaseven = teaseven;
    }

    public String getTeaeight() {
        return teaeight;
    }

    public void setTeaeight(String teaeight) {
        this.teaeight = teaeight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
