package com.doj.ursus.model;

import java.sql.Date;
import java.util.List;

public class IncidentForceLocation {

    private String forceLocation;
   // private Date changeDate;
    private int forceId;
    private int forceLocationId;

    public int getForceLocationId() {
        return forceLocationId;
    }

    public void setForceLocationId(int forceLocationId) {
        this.forceLocationId = forceLocationId;
    }

    public int getForceId() {
        return forceId;
    }

    public void setForceId(int forceId) {
        this.forceId = forceId;
    }


    public String getForceLocation() {
        return forceLocation;
    }

    public void setForceLocation(String forceLocation) {
        this.forceLocation = forceLocation;
    }

//    public Date getChangeDate() {
//        return changeDate;
//    }
//
//    public void setChangeDate(Date changeDate) {
//        this.changeDate = changeDate;
//    }

    @Override
    public String toString() {
        return "IncidentForceLocation{" +
                "forceLocation='" + forceLocation + '\'' +
                ", forceId=" + forceId +
                ", forceLocationId=" + forceLocationId +
                '}';
    }
}
