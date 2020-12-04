package com.doj.ursus.model;

public class ResistanceType {

    private int resistanceId;
    private int civilianId;
    private String resistanceType;

    public int getResistanceId() {
        return resistanceId;
    }

    public void setResistanceId(int resistanceId) {
        this.resistanceId = resistanceId;
    }

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public String getResistanceType() {
        return resistanceType;
    }

    public void setResistanceType(String resistanceType) {
        this.resistanceType = resistanceType;
    }

    @Override
    public String toString() {
        return "ResistanceType{" +
                "resistanceId=" + resistanceId +
                ", civilianId=" + civilianId +
                ", resistanceType='" + resistanceType + '\'' +
                '}';
    }
}
