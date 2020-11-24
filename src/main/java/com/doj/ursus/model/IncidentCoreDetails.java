package com.doj.ursus.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class IncidentCoreDetails {

    private int incidentId;
    private String primaryAgency;
    /*
    if it is multiple agencies and incident should submitted by primary agency
    then, in incident details page subordinate agency field should enable.
    user can add multiple subordinate agencies by clicking "Add Subordinate Agency" button
     */
    private List<SubOrdinateAgency> subOrdinateAgencyList;
    private Date incidentDate;
    private Timestamp incidentTime;
    private String isArrestMade;
    private String isCrimeReportFiled;
    private String incidentReason;
    private String agencyORI;
    private int officersCount;
    private int civiliansCount;
    private String scenario;
    private String caseNumber;
    private String custodyEventOptions;

    private IncidentAddressDetails incidentAddressDetails;
    private IncidentDemographics incidentDemographics;

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public String getPrimaryAgency() {
        return primaryAgency;
    }

    public void setPrimaryAgency(String primaryAgency) {
        this.primaryAgency = primaryAgency;
    }

    public List<SubOrdinateAgency> getSubOrdinateAgencyList() {
        return subOrdinateAgencyList;
    }

    public void setSubOrdinateAgencyList(List<SubOrdinateAgency> subOrdinateAgencyList) {
        this.subOrdinateAgencyList = subOrdinateAgencyList;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public Timestamp getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(Timestamp incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getIsArrestMade() {
        return isArrestMade;
    }

    public void setIsArrestMade(String isArrestMade) {
        this.isArrestMade = isArrestMade;
    }

    public String getIsCrimeReportFiled() {
        return isCrimeReportFiled;
    }

    public void setIsCrimeReportFiled(String isCrimeReportFiled) {
        this.isCrimeReportFiled = isCrimeReportFiled;
    }

    public String getIncidentReason() {
        return incidentReason;
    }

    public void setIncidentReason(String incidentReason) {
        this.incidentReason = incidentReason;
    }

    public String getAgencyORI() {
        return agencyORI;
    }

    public void setAgencyORI(String agencyORI) {
        this.agencyORI = agencyORI;
    }

    public int getOfficersCount() {
        return officersCount;
    }

    public void setOfficersCount(int officersCount) {
        this.officersCount = officersCount;
    }

    public int getCiviliansCount() {
        return civiliansCount;
    }

    public void setCiviliansCount(int civiliansCount) {
        this.civiliansCount = civiliansCount;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCustodyEventOptions() {
        return custodyEventOptions;
    }

    public void setCustodyEventOptions(String custodyEventOptions) {
        this.custodyEventOptions = custodyEventOptions;
    }

    public IncidentAddressDetails getIncidentAddressDetails() {
        return incidentAddressDetails;
    }

    public void setIncidentAddressDetails(IncidentAddressDetails incidentAddressDetails) {
        this.incidentAddressDetails = incidentAddressDetails;
    }

    public IncidentDemographics getIncidentDemographics() {
        return incidentDemographics;
    }

    public void setIncidentDemographics(IncidentDemographics incidentDemographics) {
        this.incidentDemographics = incidentDemographics;
    }

    @Override
    public String toString() {
        return "IncidentCoreDetails{" +
                "incidentId='" + incidentId + '\'' +
                ", primaryAgency='" + primaryAgency + '\'' +
                ", subOrdinateAgencyList=" + subOrdinateAgencyList +
                ", incidentDate=" + incidentDate +
                ", incidentTime=" + incidentTime +
                ", isArrestMade='" + isArrestMade + '\'' +
                ", isCrimeReportFiled='" + isCrimeReportFiled + '\'' +
                ", incidentReason='" + incidentReason + '\'' +
                ", agencyORI='" + agencyORI + '\'' +
                ", officersCount=" + officersCount +
                ", civiliansCount=" + civiliansCount +
                ", scenario='" + scenario + '\'' +
                ", caseNumber='" + caseNumber + '\'' +
                ", custodyEventOptions='" + custodyEventOptions + '\'' +
                ", incidentAddressDetails=" + incidentAddressDetails +
                ", incidentDemographics=" + incidentDemographics +
                '}';
    }
}
