package com.doj.ursus.model;

public class AsianRace {

    private int asianRaceId;
    private int raceId;
    private String asianRace;

    @Override
    public String toString() {
        return "AsianRace{" +
                "asianRaceId=" + asianRaceId +
                ", raceId=" + raceId +
                ", asianRace='" + asianRace + '\'' +
                '}';
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

}
