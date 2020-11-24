package com.doj.ursus.model;

import java.util.List;

public class IncidentDemographics {

    private List<Officers> officersList;
    private List<Civilians> civiliansList;

    public List<Officers> getOfficersList() {
        return officersList;
    }

    public void setOfficersList(List<Officers> officersList) {
        this.officersList = officersList;
    }

    public List<Civilians> getCiviliansList() {
        return civiliansList;
    }

    public void setCiviliansList(List<Civilians> civiliansList) {
        this.civiliansList = civiliansList;
    }

    @Override
    public String toString() {
        return "IncidentDemographics{" +
                "officersList=" + officersList +
                ", civiliansList=" + civiliansList +
                '}';
    }
}
