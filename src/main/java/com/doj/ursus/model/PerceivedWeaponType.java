package com.doj.ursus.model;

public class PerceivedWeaponType {

    private int civilianId;
    private int perWeaponId;
    private String perceivedWeapon;

    public int getCivilianId() {
        return civilianId;
    }

    public void setCivilianId(int civilianId) {
        this.civilianId = civilianId;
    }

    public int getPerWeaponId() {
        return perWeaponId;
    }

    public void setPerWeaponId(int perWeaponId) {
        this.perWeaponId = perWeaponId;
    }

    public String getPerceivedWeapon() {
        return perceivedWeapon;
    }

    public void setPerceivedWeapon(String perceivedWeapon) {
        this.perceivedWeapon = perceivedWeapon;
    }

    @Override
    public String toString() {
        return "PerceivedWeaponType{" +
                "civilianId=" + civilianId +
                ", perWeaponId=" + perWeaponId +
                ", perceivedWeapon='" + perceivedWeapon + '\'' +
                '}';
    }
}
