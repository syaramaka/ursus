package com.doj.ursus.model;

import java.util.List;

public class CiviliansList {

    private List<Civilians> civiliansList;

    public List<Civilians> getCiviliansList() {
        return civiliansList;
    }

    public void setCiviliansList(List<Civilians> civiliansList) {
        this.civiliansList = civiliansList;
    }

    @Override
    public String toString() {
        return "CiviliansList{" +
                "civiliansList=" + civiliansList +
                '}';
    }
}
