package com.doj.ursus.model;

/*
screener pojo, map to screener table
 */
public class Screener {
    private int screenerId;
    private String isMultipleAgencies;
    private String isPrimaryAgency;
    /*
    if isPrimaryAgency is NO, then user has to enter
    primary agency name and click Submit to DOJ
    then email has to send to DOJ / Primary agency.
    In the above case remaining fields should disable
     */
    private String isDischargeOfFirearm;
    private String isOfficerUsedForce;
    private String isCivilianSeriouslyInjured;
    private String isCivilianUsedForce;
    private String isOfficerSeriouslyInjured;
    private String isIncident;

    public int getScreenerId() {
        return screenerId;
    }

    public void setScreenerId(int screenerId) {
        this.screenerId = screenerId;
    }

    public String getIsMultipleAgencies() {
        return isMultipleAgencies;
    }

    public void setIsMultipleAgencies(String isMultipleAgencies) {
        this.isMultipleAgencies = isMultipleAgencies;
    }

    public String getIsPrimaryAgency() {
        return isPrimaryAgency;
    }

    public void setIsPrimaryAgency(String isPrimaryAgency) {
        this.isPrimaryAgency = isPrimaryAgency;
    }

    public String getIsDischargeOfFirearm() {
        return isDischargeOfFirearm;
    }

    public void setIsDischargeOfFirearm(String isDischargeOfFirearm) {
        this.isDischargeOfFirearm = isDischargeOfFirearm;
    }

    public String getIsOfficerUsedForce() {
        return isOfficerUsedForce;
    }

    public void setIsOfficerUsedForce(String isOfficerUsedForce) {
        this.isOfficerUsedForce = isOfficerUsedForce;
    }

    public String getIsCivilianSeriouslyInjured() {
        return isCivilianSeriouslyInjured;
    }

    public void setIsCivilianSeriouslyInjured(String isCivilianSeriouslyInjured) {
        this.isCivilianSeriouslyInjured = isCivilianSeriouslyInjured;
    }

    public String getIsCivilianUsedForce() {
        return isCivilianUsedForce;
    }

    public void setIsCivilianUsedForce(String isCivilianUsedForce) {
        this.isCivilianUsedForce = isCivilianUsedForce;
    }

    public String getIsOfficerSeriouslyInjured() {
        return isOfficerSeriouslyInjured;
    }

    public void setIsOfficerSeriouslyInjured(String isOfficerSeriouslyInjured) {
        this.isOfficerSeriouslyInjured = isOfficerSeriouslyInjured;
    }

    public String getIsIncident() {
        return isIncident;
    }

    public void setIsIncident(String isIncident) {
        this.isIncident = isIncident;
    }

    @Override
    public String toString() {
        return "Screener{" +
                "screenerId=" + screenerId +
                ", isMultipleAgencies='" + isMultipleAgencies + '\'' +
                ", isPrimaryAgency='" + isPrimaryAgency + '\'' +
                ", isDischargeOfFirearm='" + isDischargeOfFirearm + '\'' +
                ", isOfficerUsedForce='" + isOfficerUsedForce + '\'' +
                ", isCivilianSeriouslyInjured='" + isCivilianSeriouslyInjured + '\'' +
                ", isCivilianUsedForce='" + isCivilianUsedForce + '\'' +
                ", isOfficerSeriouslyInjured='" + isOfficerSeriouslyInjured + '\'' +
                ", isIncident='" + isIncident + '\'' +
                '}';
    }
}
