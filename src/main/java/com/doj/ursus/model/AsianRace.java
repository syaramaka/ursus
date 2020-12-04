package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AsianRace {

    private int asianRaceId;
    private int raceId;
    private String asianRace;
    private int civilianId;
    private int officerId;
    private String asianRaceOf;

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

    public String getAsianRaceOf() {
        return asianRaceOf;
    }

    public void setAsianRaceOf(String asianRaceOf) {
        this.asianRaceOf = asianRaceOf;
    }

    public String getAsianRace() {
        return asianRace;
    }

    public void setAsianRace(String asianRace) {
        this.asianRace = asianRace;
    }

    public int getAsianRaceId() {
        return asianRaceId;
    }

    public void setAsianRaceId(int asianRaceId) {
        this.asianRaceId = asianRaceId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    @Override
    public String toString() {
        return "AsianRace{" +
                "asianRaceId=" + asianRaceId +
                ", raceId=" + raceId +
                ", asianRace='" + asianRace + '\'' +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", asianRaceOf='" + asianRaceOf + '\'' +
                '}';
    }
}
