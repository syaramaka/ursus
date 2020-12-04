package com.doj.ursus.model;

import com.doj.ursus.util.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@XmlRootElement(name = "officer")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Officers {

    private int officerId;
    private String agency;
    private int officerOrder; // number
    private List<Integer> engagedCivilians;
    private boolean isOfficerUsedForce;
    @XmlElementWrapper(name = "reasonForOfficerUsedForce")
    @XmlElement(name = "reasonForOfficerUsedForce")
    private List<String> reasonForOfficerUsedForce;
    private int age;
    private Gender gender;
    private boolean isInjured;
    private boolean isInjuryFromPreExistingCondition;
    private boolean isReceivedForce;
    private boolean isOfficerAssaulted;
    private boolean isOnDuty;
    private String dress;
    private Date changeDate;
    @JsonIgnore
    private int incidentId;

    @XmlElementWrapper(name = "primaryRaceTypes")
    @XmlElement(name = "primaryRaceType")
    private List<String> primaryRaceType;
    @XmlElementWrapper(name = "asianRaceTypes")
    @XmlElement(name = "asianRaceType")
    private List<String> asianRaceType;
    @XmlElementWrapper(name = "hawaiianRaceTypes")
    @XmlElement(name = "hawaiianRaceType")
    private List<String> hawaiianRaceType;
    private int injuryLevel;
    @XmlElementWrapper(name = "injuryTypes")
    @XmlElement(name = "injuryType")
    private List<String> injuryType;
    private String injuryMedicalAid;
    @XmlElementWrapper(name = "forceLocations")
    @XmlElement(name = "forceLocation")
    private List<String> forceLocation;
    @XmlElementWrapper(name = "forceTypes")
    @XmlElement(name = "forceType")
    private List<String> forceType;

    public List<String> getPrimaryRaceType() {
        return primaryRaceType;
    }

    public void setPrimaryRaceType(List<String> primaryRaceType) {
        this.primaryRaceType = primaryRaceType;
    }

    public List<String> getAsianRaceType() {
        return asianRaceType;
    }

    public void setAsianRaceType(List<String> asianRaceType) {
        this.asianRaceType = asianRaceType;
    }

    public List<String> getHawaiianRaceType() {
        return hawaiianRaceType;
    }

    public void setHawaiianRaceType(List<String> hawaiianRaceType) {
        this.hawaiianRaceType = hawaiianRaceType;
    }

    public int getInjuryLevel() {
        return injuryLevel;
    }

    public void setInjuryLevel(int injuryLevel) {
        this.injuryLevel = injuryLevel;
    }

    public List<String> getInjuryType() {
        return injuryType;
    }

    public void setInjuryType(List<String> injuryType) {
        this.injuryType = injuryType;
    }

    public String getInjuryMedicalAid() {
        return injuryMedicalAid;
    }

    public void setInjuryMedicalAid(String injuryMedicalAid) {
        this.injuryMedicalAid = injuryMedicalAid;
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

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
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
                ", isInjured=" + isInjured +
                ", isInjuryFromPreExistingCondition=" + isInjuryFromPreExistingCondition +
                ", isReceivedForce=" + isReceivedForce +
                ", isOfficerAssaulted=" + isOfficerAssaulted +
                ", isOnDuty=" + isOnDuty +
                ", dress='" + dress + '\'' +
                ", changeDate=" + changeDate +
                ", incidentId=" + incidentId +
                ", primaryRaceType=" + primaryRaceType +
                ", asianRaceType=" + asianRaceType +
                ", hawaiianRaceType=" + hawaiianRaceType +
                ", injuryLevel=" + injuryLevel +
                ", injuryType=" + injuryType +
                ", injuryMedicalAid='" + injuryMedicalAid + '\'' +
                ", forceLocation=" + forceLocation +
                ", forceType=" + forceType +
                '}';
    }
}
