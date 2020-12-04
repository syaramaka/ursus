package com.doj.ursus.model;

import com.doj.ursus.util.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@XmlRootElement(name = "civilian")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Civilians {

    private int civilianId;
    private int civilianNumber;

    private boolean isAssaultedOfficer;
    private String custodyStatus;
    private boolean isPerceivedArmed;
    @XmlElementWrapper(name = "perceivedWeaponTypes")
    @XmlElement(name = "perceivedWeaponType")
    private List<String> perceivedWeaponType;
    @XmlElementWrapper(name = "engagedOfficers")
    @XmlElement(name = "engagedOfficers")
    private List<Integer> engagedOfficers;
    private String k12Type;
    private boolean isConfirmedArmed;
    @XmlElementWrapper(name = "confirmedArmedWeapons")
    @XmlElement(name = "confirmedArmedWeapon")
    private List<String> confirmedArmedWeapon;
   @XmlElementWrapper(name = "fireArms")
   @XmlElement(name = "fireArms")
    List<String> fireArms;
    private boolean isResisted;
    @XmlElementWrapper(name = "resistanceTypes")
    @XmlElement(name = "resistanceType")
    private List<String> resistanceType;
    private boolean isReceivedForce;
    private boolean isOrderOfForceSpecified;
    private String mentalStatus; // behaviour
    private boolean isInjured;
    private boolean isInjuryFromPreExisting;
    private int age;
    private Gender gender;
    private String race;
    private String highestCharge; // charge-type in table -- need to confirm with Jay
    private Date changeDate;
    @JsonIgnore
    private int incidentId;
    @XmlElementWrapper(name = "forceLocations")
    @XmlElement(name = "forceLocation")
    private List<String> forceLocation;
    @XmlElementWrapper(name = "forceTypes")
    @XmlElement(name = "forceType")
    private List<String> forceType;
    private int injuryLevel;
    @XmlElementWrapper(name = "injuryTypes")
    @XmlElement(name = "injuryType")
    private List<String> injuryType;
    private String injuryMedicalAid;
    @XmlElementWrapper(name = "primaryRaceTypes")
    @XmlElement(name = "primaryRaceType")
    private List<String> primaryRaceType;
    @XmlElementWrapper(name = "asianRaceTypes")
    @XmlElement(name = "asianRaceType")
    private List<String> asianRaceType;
    @XmlElementWrapper(name = "hawaiianRaceTypes")
    @XmlElement(name = "hawaiianRaceType")
    private List<String> hawaiianRaceType;


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

    public List<Integer> getEngagedOfficers() {
        return engagedOfficers;
    }

    public void setEngagedOfficers(List<Integer> engagedOfficers) {
        this.engagedOfficers = engagedOfficers;
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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }
    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public int getCivilianNumber() {
        return civilianNumber;
    }

    public void setCivilianNumber(int civilianNumber) {
        this.civilianNumber = civilianNumber;
    }

    public boolean isAssaultedOfficer() {
        return isAssaultedOfficer;
    }

    public void setAssaultedOfficer(boolean assaultedOfficer) {
        isAssaultedOfficer = assaultedOfficer;
    }

    public String getCustodyStatus() {
        return custodyStatus;
    }

    public void setCustodyStatus(String custodyStatus) {
        this.custodyStatus = custodyStatus;
    }

    public boolean isPerceivedArmed() {
        return isPerceivedArmed;
    }

    public void setPerceivedArmed(boolean perceivedArmed) {
        isPerceivedArmed = perceivedArmed;
    }

    public List<String> getPerceivedWeaponType() {
        return perceivedWeaponType;
    }

    public void setPerceivedWeaponType(List<String> perceivedWeaponType) {
        this.perceivedWeaponType = perceivedWeaponType;
    }

    public String getK12Type() {
        return k12Type;
    }

    public void setK12Type(String k12Type) {
        this.k12Type = k12Type;
    }

    public boolean isConfirmedArmed() {
        return isConfirmedArmed;
    }

    public void setConfirmedArmed(boolean confirmedArmed) {
        isConfirmedArmed = confirmedArmed;
    }

    public List<String> getConfirmedArmedWeapon() {
        return confirmedArmedWeapon;
    }

    public void setConfirmedArmedWeapon(List<String> confirmedArmedWeapon) {
        this.confirmedArmedWeapon = confirmedArmedWeapon;
    }

    public boolean isResisted() {
        return isResisted;
    }

    public void setResisted(boolean resisted) {
        isResisted = resisted;
    }

    public List<String> getResistanceType() {
        return resistanceType;
    }

    public void setResistanceType(List<String> resistanceType) {
        this.resistanceType = resistanceType;
    }

    public boolean isReceivedForce() {
        return isReceivedForce;
    }

    public void setReceivedForce(boolean receivedForce) {
        isReceivedForce = receivedForce;
    }

    public boolean isOrderOfForceSpecified() {
        return isOrderOfForceSpecified;
    }

    public void setOrderOfForceSpecified(boolean orderOfForceSpecified) {
        isOrderOfForceSpecified = orderOfForceSpecified;
    }

    public String getMentalStatus() {
        return mentalStatus;
    }

    public void setMentalStatus(String mentalStatus) {
        this.mentalStatus = mentalStatus;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    public boolean isInjuryFromPreExisting() {
        return isInjuryFromPreExisting;
    }

    public void setInjuryFromPreExisting(boolean injuryFromPreExisting) {
        isInjuryFromPreExisting = injuryFromPreExisting;
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

    public String getHighestCharge() {
        return highestCharge;
    }

    public void setHighestCharge(String highestCharge) {
        this.highestCharge = highestCharge;
    }

    public List<String> getFireArms() {
        return fireArms;
    }

    public void setFireArms(List<String> fireArms) {
        this.fireArms = fireArms;
    }

    @Override
    public String toString() {
        return "Civilians{" +
                "civilianId=" + civilianId +
                ", civilianNumber=" + civilianNumber +
                ", isAssaultedOfficer=" + isAssaultedOfficer +
                ", custodyStatus='" + custodyStatus + '\'' +
                ", isPerceivedArmed=" + isPerceivedArmed +
                ", perceivedWeaponType=" + perceivedWeaponType +
                ", engagedOfficers=" + engagedOfficers +
                ", k12Type='" + k12Type + '\'' +
                ", isConfirmedArmed=" + isConfirmedArmed +
                ", confirmedArmedWeapon=" + confirmedArmedWeapon +
                ", fireArms=" + fireArms +
                ", isResisted=" + isResisted +
                ", resistanceType=" + resistanceType +
                ", isReceivedForce=" + isReceivedForce +
                ", isOrderOfForceSpecified=" + isOrderOfForceSpecified +
                ", mentalStatus='" + mentalStatus + '\'' +
                ", isInjured=" + isInjured +
                ", isInjuryFromPreExisting=" + isInjuryFromPreExisting +
                ", age=" + age +
                ", gender=" + gender +
                ", race='" + race + '\'' +
                ", highestCharge='" + highestCharge + '\'' +
                ", changeDate=" + changeDate +
                ", incidentId=" + incidentId +
                ", forceLocation=" + forceLocation +
                ", forceType=" + forceType +
                ", injuryLevel=" + injuryLevel +
                ", injuryType=" + injuryType +
                ", injuryMedicalAid='" + injuryMedicalAid + '\'' +
                ", primaryRaceType=" + primaryRaceType +
                ", asianRaceType=" + asianRaceType +
                ", hawaiianRaceType=" + hawaiianRaceType +
                '}';
    }

}
