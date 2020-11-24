package com.doj.ursus.model;

public class SubOrdinateAgency {

    private String secondaryAgency;
    private String secondaryAgencyORI;

    public String getSecondaryAgency() {
        return secondaryAgency;
    }

    public void setSecondaryAgency(String secondaryAgency) {
        this.secondaryAgency = secondaryAgency;
    }

    public String getSecondaryAgencyORI() {
        return secondaryAgencyORI;
    }

    public void setSecondaryAgencyORI(String secondaryAgencyORI) {
        this.secondaryAgencyORI = secondaryAgencyORI;
    }

    @Override
    public String toString() {
        return "SubOrdinateAgency{" +
                "secondaryAgency='" + secondaryAgency + '\'' +
                ", secondaryAgencyORI='" + secondaryAgencyORI + '\'' +
                '}';
    }
}
