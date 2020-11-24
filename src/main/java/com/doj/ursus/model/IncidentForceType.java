package com.doj.ursus.model;

import java.sql.Date;
import java.util.List;

public class IncidentForceType {

private String forceType;
//private Date chnageDate;
private int forceId;
private int forceTypeId;

    public int getForceTypeId() {
        return forceTypeId;
    }

    public void setForceTypeId(int forceTypeId) {
        this.forceTypeId = forceTypeId;
    }

    public int getForceId() {
        return forceId;
    }

    public void setForceId(int forceId) {
        this.forceId = forceId;
    }

    public String getForceType() {
        return forceType;
    }

    public void setForceType(String forceType) {
        this.forceType = forceType;
    }

//    public Date getChnageDate() {
//        return chnageDate;
//    }
//
//    public void setChnageDate(Date chnageDate) {
//        this.chnageDate = chnageDate;
//    }

    @Override
    public String toString() {
        return "IncidentForceType{" +
                "forceType='" + forceType + '\'' +
                ", forceId=" + forceId +
                ", forceTypeId=" + forceTypeId +
                '}';
    }
}
