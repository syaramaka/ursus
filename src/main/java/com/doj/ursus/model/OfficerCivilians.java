package com.doj.ursus.model;

import java.util.List;

public class OfficerCivilians {

    private int officerId;
    private int civilianId;
    private String offCivType;

    public String getOffCivType() {
        return offCivType;
    }

    public void setOffCivType(String offCivType) {
        this.offCivType = offCivType;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    @Override
    public String toString() {
        return "OfficerCivilians{" +
                ", officerId=" + officerId +
                ", civilianId=" + civilianId +
                ", offCivType='" + offCivType + '\'' +
                '}';
    }
}
