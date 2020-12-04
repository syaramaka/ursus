package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HawaiianPacificIslanderRace {

    private int hawainRaceId;
    private int raceId;
    private String hawaiianRace;
    private int civilianId;
    private int officerId;
    private String hawaiianRaceOf;

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

    public String getHawaiianRaceOf() {
        return hawaiianRaceOf;
    }

    public void setHawaiianRaceOf(String hawaiianRaceOf) {
        this.hawaiianRaceOf = hawaiianRaceOf;
    }

    public String getHawaiianRace() {
        return hawaiianRace;
    }

    public void setHawaiianRace(String hawaiianRace) {
        this.hawaiianRace = hawaiianRace;
    }

    public int getHawainRaceId() {
        return hawainRaceId;
    }

    public void setHawainRaceId(int hawainRaceId) {
        this.hawainRaceId = hawainRaceId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    @Override
    public String toString() {
        return "HawaiianPacificIslanderRace{" +
                "hawainRaceId=" + hawainRaceId +
                ", raceId=" + raceId +
                ", hawaiianRace='" + hawaiianRace + '\'' +
                ", civilianId=" + civilianId +
                ", officerId=" + officerId +
                ", hawaiianRaceOf='" + hawaiianRaceOf + '\'' +
                '}';
    }
}
