package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
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
