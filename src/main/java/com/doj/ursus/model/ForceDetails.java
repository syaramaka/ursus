package com.doj.ursus.model;

import java.sql.Date;
import java.util.List;

public class ForceDetails {

    private int forceId;

    private int civilianId;
    private int officerId;
    private Date changeDate;
    private List<String> forceLocation;
    private List<String> forceType;
    private String forceOn;

    public String getForceOn() {
        return forceOn;
    }

    public void setForceOn(String forceOn) {
        this.forceOn = forceOn;
    }

    public List<String> getForceLocation() {
        return forceLocation;
    }

    public void setForceLocation(List<String> forceLocation) {
        this.forceLocation = forceLocation;
    }

    public List<String> getForceType() {
        return forceType;
    }

    public void setForceType(List<String> forceType) {
        this.forceType = forceType;
    }

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public int getForceId() {
        return forceId;
    }

    public void setForceId(int forceId) {
        this.forceId = forceId;
    }

    @Override
    public String toString() {
        return "ForceDetails{" +
                "forceId=" + forceId +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", changeDate=" + changeDate +
                ", forceLocation=" + forceLocation +
                ", forceType=" + forceType +
                ", forceOn='" + forceOn + '\'' +
                '}';
    }
}
