package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class IncidentForceLocation {

    private String forceLocation;
    private int forceId;
    private int forceLocationId;

    public int getForceLocationId() {
        return forceLocationId;
    }

    public void setForceLocationId(int forceLocationId) {
        this.forceLocationId = forceLocationId;
    }

    public int getForceId() {
        return forceId;
    }

    public void setForceId(int forceId) {
        this.forceId = forceId;
    }


    public String getForceLocation() {
        return forceLocation;
    }

    public void setForceLocation(String forceLocation) {
        this.forceLocation = forceLocation;
    }

    @Override
    public String toString() {
        return "IncidentForceLocation{" +
                "forceLocation='" + forceLocation + '\'' +
                ", forceId=" + forceId +
                ", forceLocationId=" + forceLocationId +
                '}';
    }
}
