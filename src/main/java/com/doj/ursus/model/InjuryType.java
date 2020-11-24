package com.doj.ursus.model;

public class InjuryType {

    private String injuryType;
    private int injuryId;
    private int injuryTypeId;

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
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
                ", injuryId=" + injuryId +
                ", injuryTypeId=" + injuryTypeId +
                '}';
    }
}
