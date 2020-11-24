package com.doj.ursus.model;

public class PrimaryRace {

    private int primaryRaceId;
    private String primaryRace;
    private int raceId;

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
                '}';
    }
}
