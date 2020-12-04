package com.doj.ursus.model;

public class ForceType {

    private int forceTypeId;
    private String forceType;
    private int civilianId;
    private int officerId;
    private String forceOn;

    public String getForceOn() {
        return forceOn;
    }

    public void setForceOn(String forceOn) {
        this.forceOn = forceOn;
    }

    public int getForceTypeId() {
        return forceTypeId;
    }

    public void setForceTypeId(int forceTypeId) {
        this.forceTypeId = forceTypeId;
    }

    public String getForceType() {
        return forceType;
    }

    public void setForceType(String forceType) {
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

    @Override
    public String toString() {
        return "ForceType{" +
                "forceTypeId=" + forceTypeId +
                ", forceType='" + forceType + '\'' +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", forceOn='" + forceOn + '\'' +
                '}';
    }
}
