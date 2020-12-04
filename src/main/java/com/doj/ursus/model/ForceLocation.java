package com.doj.ursus.model;

public class ForceLocation {

    private int forceLocationId;
    private String forceLocation;
    private int civilianId;
    private int officerId;
    private String forceOn;

    public String getForceOn() {
        return forceOn;
    }

    public void setForceOn(String forceOn) {
        this.forceOn = forceOn;
    }

    public int getForceLocationId() {
        return forceLocationId;
    }

    public void setForceLocationId(int forceLocationId) {
        this.forceLocationId = forceLocationId;
    }

    public String getForceLocation() {
        return forceLocation;
    }

    public void setForceLocation(String forceLocation) {
        this.forceLocation = forceLocation;
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

    @Override
    public String toString() {
        return "ForceLocation{" +
                "forceLocationId=" + forceLocationId +
                ", forceLocation='" + forceLocation + '\'' +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", forceOn='" + forceOn + '\'' +
                '}';
    }
}
