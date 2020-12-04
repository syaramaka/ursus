package com.doj.ursus.model;

import java.util.List;

public class OfficersList {

    private List<Officers> officersList;

    public List<Officers> getOfficersList() {
        return officersList;
    }

    public void setOfficersList(List<Officers> officersList) {
        this.officersList = officersList;
    }

    @Override
    public String toString() {
        return "OfficersList{" +
                "officersList=" + officersList +
                '}';
    }
}
