package com.doj.ursus.model;

import java.sql.Date;
import java.util.List;

public class Injury {

    private int injuryId;
    private int injuryLevel;
    private List<String> injuryType;
   // private List<InjuryType> typeList;
    private String injuryMedicalAid;
    private int civilianId;
    private int officerId;
    private Date changeDate;
    private String injuryOffCiv;

//    public List<InjuryType> getTypeList() {
//        return typeList;
//    }
//
//    public void setTypeList(List<InjuryType> typeList) {
//        this.typeList = typeList;
//    }

    @Override
    public String toString() {
        return "Injury{" +
                "injuryId=" + injuryId +
                ", injuryLevel=" + injuryLevel +
                ", injuryType=" + injuryType +
                ", injuryMedicalAid='" + injuryMedicalAid + '\'' +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", changeDate=" + changeDate +
                ", injuryOffCiv='" + injuryOffCiv + '\'' +
                '}';
    }

    public String getInjuryOffCiv() {
        return injuryOffCiv;
    }

    public void setInjuryOffCiv(String injuryOffCiv) {
        this.injuryOffCiv = injuryOffCiv;
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

    public int getInjuryLevel() {
        return injuryLevel;
    }

    public void setInjuryLevel(int injuryLevel) {
        this.injuryLevel = injuryLevel;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
    }

    public String getInjuryMedicalAid() {
        return injuryMedicalAid;
    }

    public void setInjuryMedicalAid(String injuryMedicalAid) {
        this.injuryMedicalAid = injuryMedicalAid;
    }


    public List<String> getInjuryType() {
        return injuryType;
    }

    public void setInjuryType(List<String> injuryType) {
        this.injuryType = injuryType;
    }

}
