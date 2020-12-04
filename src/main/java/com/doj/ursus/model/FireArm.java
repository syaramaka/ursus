package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FireArm {

    private int fireArmTypeId;
    private String fireArmType;
    private int civilianId;

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public int getFireArmTypeId() {
        return fireArmTypeId;
    }

    public void setFireArmTypeId(int fireArmTypeId) {
        this.fireArmTypeId = fireArmTypeId;
    }

    public String getFireArmType() {
        return fireArmType;
    }

    public void setFireArmType(String fireArmType) {
        this.fireArmType = fireArmType;
    }

    @Override
    public String toString() {
        return "FireArm{" +
                "fireArmTypeId=" + fireArmTypeId +
                ", fireArmType='" + fireArmType + '\'' +
                ", civilianId=" + civilianId +
                '}';
    }
}
