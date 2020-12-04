package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PrimaryRace {

    private int primaryRaceId;
    private String primaryRace;
    private int raceId;
    private int civilianId;
    private int officerId;
    private String primaryRaceOf;

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


    public String getPrimaryRaceOf() {
        return primaryRaceOf;
    }

    public void setPrimaryRaceOf(String primaryRaceOf) {
        this.primaryRaceOf = primaryRaceOf;
    }

    public int getPrimaryRaceId() {
        return primaryRaceId;
    }

    public void setPrimaryRaceId(int primaryRaceId) {
        this.primaryRaceId = primaryRaceId;
    }

    public String getPrimaryRace() {
        return primaryRace;
    }

    public void setPrimaryRace(String primaryRace) {
        this.primaryRace = primaryRace;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    @Override
    public String toString() {
        return "PrimaryRace{" +
                "primaryRaceId=" + primaryRaceId +
                ", primaryRace='" + primaryRace + '\'' +
                ", raceId=" + raceId +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", primaryRaceOf='" + primaryRaceOf + '\'' +
                '}';
    }
}
