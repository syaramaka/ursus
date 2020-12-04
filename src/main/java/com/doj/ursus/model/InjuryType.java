package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class InjuryType {

    private String injuryType;
    private int injuryTypeId;
    private int civilianId;
    private int officerId;
    private String injutyTypeOn;

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

    public String getInjutyTypeOn() {
        return injutyTypeOn;
    }

    public void setInjutyTypeOn(String injutyTypeOn) {
        this.injutyTypeOn = injutyTypeOn;
    }

    public int getInjuryTypeId() {
        return injuryTypeId;
    }

    public void setInjuryTypeId(int injuryTypeId) {
        this.injuryTypeId = injuryTypeId;
    }

    public String getInjuryType() {
        return injuryType;
    }

    public void setInjuryType(String injuryType) {
        this.injuryType = injuryType;
    }

    @Override
    public String toString() {
        return "InjuryType{" +
                "injuryType='" + injuryType + '\'' +
                ", injuryTypeId=" + injuryTypeId +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", injutyTypeOn='" + injutyTypeOn + '\'' +
                '}';
    }
}
