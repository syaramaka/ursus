package com.doj.ursus.model;

import java.util.List;

public class Race {

    private int raceId;
    private List<String> primaryRaceType;
    private List<String> asianRaceType;
    private List<String> hawaiianRaceType;
    private String raceOf;
    private int officerId;
    private int civilainsId;

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

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

    public String getRaceOf() {
        return raceOf;
    }

    public void setRaceOf(String raceOf) {
        this.raceOf = raceOf;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public int getCivilainsId() {
        return civilainsId;
    }

    public void setCivilainsId(int civilainsId) {
        this.civilainsId = civilainsId;
    }

    @Override
    public String toString() {
        return "Race{" +
                "raceId=" + raceId +
                ", primaryRaceType=" + primaryRaceType +
                ", asianRaceType=" + asianRaceType +
                ", hawaiianRaceType=" + hawaiianRaceType +
                ", raceOf='" + raceOf + '\'' +
                ", officerId=" + officerId +
                ", civilainsId=" + civilainsId +
                '}';
    }
}
