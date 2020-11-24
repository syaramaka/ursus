package com.doj.ursus.model;

public class IncidentLocation {

    private int incidentAddressSequence;
    private String isOnK12Campus;
    private String streetNo;
    private String streetName;
    private String city;
    private String state;
    private String county;
    private String zip;
    private Double longitude;
    private Double latitude;
    private String location;
    private String locationDetails;
    private int incidentId;
    private int locationId;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public int getIncidentAddressSequence() {
        return incidentAddressSequence;
    }

    public void setIncidentAddressSequence(int incidentAddressSequence) {
        this.incidentAddressSequence = incidentAddressSequence;
    }

    public String getIsOnK12Campus() {
        return isOnK12Campus;
    }

    public void setIsOnK12Campus(String isOnK12Campus) {
        this.isOnK12Campus = isOnK12Campus;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    @Override
    public String toString() {
        return "IncidentLocation{" +
                "incidentAddressSequence=" + incidentAddressSequence +
                ", isOnK12Campus='" + isOnK12Campus + '\'' +
                ", streetNo='" + streetNo + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", county='" + county + '\'' +
                ", zip='" + zip + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", location='" + location + '\'' +
                ", locationDetails='" + locationDetails + '\'' +
                ", incidentId=" + incidentId +
                ", locationId=" + locationId +
                '}';
    }
}
