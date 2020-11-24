package com.doj.ursus.model;

import java.util.List;

/*
this is for incident address details, map to incident_address table
 */
public class IncidentAddressDetails {

    //private int incidentAddressId;
    //private String pimaryAgency;
    //private List<SubOrdinateAgency> subOrdinateAgencyList;
    private List<IncidentLocation> incidentLocations;

    public List<IncidentLocation> getIncidentLocations() {
        return incidentLocations;
    }

    public void setIncidentLocations(List<IncidentLocation> incidentLocations) {
        this.incidentLocations = incidentLocations;
    }

    @Override
    public String toString() {
        return "IncidentAddressDetails{" +
                "incidentLocations=" + incidentLocations +
                '}';
    }
}
