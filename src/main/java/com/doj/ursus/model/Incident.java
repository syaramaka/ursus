package com.doj.ursus.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@XmlRootElement(name = "incident")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Incident {

    private Screener screener;
    private int incidentId;
    private String primaryAgency;
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

    @XmlElementWrapper(name = "addresses")
    @XmlElement(name="address")
    private List<Address> addresses;

    @XmlElementWrapper(name="civilians")
    @XmlElement(name="civilian")
    private List<Civilians> civilians;

    @XmlElementWrapper(name="officers")
    @XmlElement(name="officer")
    private List<Officers> officers;


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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Civilians> getCivilians() {
        return civilians;
    }

    public void setCivilians(List<Civilians> civilians) {
        this.civilians = civilians;
    }

    public List<Officers> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officers> officers) {
        this.officers = officers;
    }

    public Screener getScreener() {
        return screener;
    }

    public void setScreener(Screener screener) {
        this.screener = screener;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "screener=" + screener +
                ", incidentId=" + incidentId +
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
                ", addresses=" + addresses +
                ", civilians=" + civilians +
                ", officers=" + officers +
                '}';
    }
}
