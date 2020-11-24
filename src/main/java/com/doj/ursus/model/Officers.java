package com.doj.ursus.model;

import com.doj.ursus.util.Gender;

import java.sql.Date;
import java.util.List;

public class Officers {

    private int officerId;
    private String agency;
    private int officerOrder; // number
    private List<Integer> engagedCivilians;
    //private List<Integer> involvedCivilianIds;
    private boolean isOfficerUsedForce;
    private List<String> reasonForOfficerUsedForce;
    private int age;
    private Gender gender;
    private Race raceDetails;
    private boolean isInjured;
    private Injury injuryDetails;
    private boolean isInjuryFromPreExistingCondition;
    private boolean isReceivedForce;
    private ForceDetails forceDetails;
    private boolean isOfficerAssaulted;
    private boolean isOnDuty;
    private String dress;
    private Date changeDate;
    private int incidentId;

    public List<Integer> getEngagedCivilians() {
        return engagedCivilians;
    }

    public void setEngagedCivilians(List<Integer> engagedCivilians) {
        this.engagedCivilians = engagedCivilians;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getOfficerOrder() {
        return officerOrder;
    }

    public void setOfficerOrder(int officerOrder) {
        this.officerOrder = officerOrder;
    }

    public boolean isOfficerUsedForce() {
        return isOfficerUsedForce;
    }

    public void setOfficerUsedForce(boolean officerUsedForce) {
        isOfficerUsedForce = officerUsedForce;
    }

    public List<String> getReasonForOfficerUsedForce() {
        return reasonForOfficerUsedForce;
    }

    public void setReasonForOfficerUsedForce(List<String> reasonForOfficerUsedForce) {
        this.reasonForOfficerUsedForce = reasonForOfficerUsedForce;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Race getRaceDetails() {
        return raceDetails;
    }

    public void setRaceDetails(Race raceDetails) {
        this.raceDetails = raceDetails;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    public Injury getInjuryDetails() {
        return injuryDetails;
    }

    public void setInjuryDetails(Injury injuryDetails) {
        this.injuryDetails = injuryDetails;
    }

    public boolean isInjuryFromPreExistingCondition() {
        return isInjuryFromPreExistingCondition;
    }

    public void setInjuryFromPreExistingCondition(boolean injuryFromPreExistingCondition) {
        isInjuryFromPreExistingCondition = injuryFromPreExistingCondition;
    }

    public boolean isReceivedForce() {
        return isReceivedForce;
    }

    public void setReceivedForce(boolean receivedForce) {
        isReceivedForce = receivedForce;
    }

    public ForceDetails getForceDetails() {
        return forceDetails;
    }

    public void setForceDetails(ForceDetails forceDetails) {
        this.forceDetails = forceDetails;
    }

    public boolean isOfficerAssaulted() {
        return isOfficerAssaulted;
    }

    public void setOfficerAssaulted(boolean officerAssaulted) {
        isOfficerAssaulted = officerAssaulted;
    }

    public boolean isOnDuty() {
        return isOnDuty;
    }

    public void setOnDuty(boolean onDuty) {
        isOnDuty = onDuty;
    }

    public String getDress() {
        return dress;
    }

    public void setDress(String dress) {
        this.dress = dress;
    }

    @Override
    public String toString() {
        return "Officers{" +
                "officerId=" + officerId +
                ", agency='" + agency + '\'' +
                ", officerOrder=" + officerOrder +
                ", engagedCivilians=" + engagedCivilians +
                ", isOfficerUsedForce=" + isOfficerUsedForce +
                ", reasonForOfficerUsedForce=" + reasonForOfficerUsedForce +
                ", age=" + age +
                ", gender=" + gender +
                ", raceDetails=" + raceDetails +
                ", isInjured=" + isInjured +
                ", injuryDetails=" + injuryDetails +
                ", isInjuryFromPreExistingCondition=" + isInjuryFromPreExistingCondition +
                ", isReceivedForce=" + isReceivedForce +
                ", forceDetails=" + forceDetails +
                ", isOfficerAssaulted=" + isOfficerAssaulted +
                ", isOnDuty=" + isOnDuty +
                ", dress='" + dress + '\'' +
                ", changeDate=" + changeDate +
                ", incidentId=" + incidentId +
                '}';
    }
}
