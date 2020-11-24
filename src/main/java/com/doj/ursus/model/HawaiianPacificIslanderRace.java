package com.doj.ursus.model;

public class HawaiianPacificIslanderRace {

    private int hawainRaceId;
    private int raceId;
    private String hawaiianRace;

    @Override
    public String toString() {
        return "HawaiianPacificIslanderRace{" +
                "hawainRaceId=" + hawainRaceId +
                ", raceId=" + raceId +
                ", hawaiianRace='" + hawaiianRace + '\'' +
                '}';
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

}
