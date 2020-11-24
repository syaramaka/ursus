package com.doj.ursus.model;

import com.doj.ursus.util.Gender;

import java.sql.Date;
import java.util.List;

public class Civilians {

    private int civilianId;
    private int civilianNumber;

    private boolean isAssaultedOfficer;
    private String custodyStatus;
    private boolean isPerceivedArmed;
    private List<String> perceivedWeaponType;
    private List<Integer> engagedOfficers;
    //private List<Integer> involvedOfficerIds;
    private String k12Type;
    private boolean isConfirmedArmed;
    private List<String> confirmedArmedWeapon;
   // private List<FireArm> fireArms;
    List<String> fireArms;
    private boolean isResisted;
    private List<String> resistanceType;
    private boolean isReceivedForce;
    private ForceDetails forceDetails;
    private boolean isOrderOfForceSpecified;
    //orderOfForceString
    private String mentalStatus; // behaviour
    private boolean isInjured;
    private Injury injuryDetails;
    private boolean isInjuryFromPreExisting;
    private int age;
    private Gender gender;
    private String race;
    private Race raceDetails;
    private String highestCharge; // charge-type in table -- need to confirm with Jay
    private Date changeDate;
    private int incidentId;

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


    public ForceDetails getForceDetails() {
        return forceDetails;
    }

    public void setForceDetails(ForceDetails forceDetails) {
        this.forceDetails = forceDetails;
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

    public Injury getInjuryDetails() {
        return injuryDetails;
    }

    public void setInjuryDetails(Injury injuryDetails) {
        this.injuryDetails = injuryDetails;
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

    public Race getRaceDetails() {
        return raceDetails;
    }

    public void setRaceDetails(Race raceDetails) {
        this.raceDetails = raceDetails;
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
                ", forceDetails=" + forceDetails +
                ", isOrderOfForceSpecified=" + isOrderOfForceSpecified +
                ", mentalStatus='" + mentalStatus + '\'' +
                ", isInjured=" + isInjured +
                ", injuryDetails=" + injuryDetails +
                ", isInjuryFromPreExisting=" + isInjuryFromPreExisting +
                ", age=" + age +
                ", gender=" + gender +
                ", race='" + race + '\'' +
                ", raceDetails=" + raceDetails +
                ", highestCharge='" + highestCharge + '\'' +
                ", changeDate=" + changeDate +
                ", incidentId=" + incidentId +
                '}';
    }
}
