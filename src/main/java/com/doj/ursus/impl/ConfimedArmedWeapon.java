package com.doj.ursus.impl;

public class ConfimedArmedWeapon {

    private int civilianId;
    private int confirmedWeaponId;
    private String confirmedWeapon;

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public int getConfirmedWeaponId() {
        return confirmedWeaponId;
    }

    public void setConfirmedWeaponId(int confirmedWeaponId) {
        this.confirmedWeaponId = confirmedWeaponId;
    }

    public String getConfirmedWeapon() {
        return confirmedWeapon;
    }

    public void setConfirmedWeapon(String confirmedWeapon) {
        this.confirmedWeapon = confirmedWeapon;
    }

    @Override
    public String toString() {
        return "ConfimedArmedWeapon{" +
                "civilianId=" + civilianId +
                ", confirmedWeaponId=" + confirmedWeaponId +
                ", confirmedWeapon='" + confirmedWeapon + '\'' +
                '}';
    }
}
