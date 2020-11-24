package com.doj.ursus.impl;

import com.doj.ursus.model.IncidentCoreDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        IncidentCoreDetails incidentCoreDetails = new IncidentCoreDetails();
        incidentCoreDetails.setIncidentId(resultSet.getInt("incident_id"));
        incidentCoreDetails.setPrimaryAgency(resultSet.getString("incident_primary_agency"));
        incidentCoreDetails.setIncidentDate(resultSet.getDate("incident_date"));
        incidentCoreDetails.setIsArrestMade(resultSet.getString("incident_arrests"));
        incidentCoreDetails.setIsCrimeReportFiled(resultSet.getString("incident_crime_report"));
        incidentCoreDetails.setIncidentReason(resultSet.getString("incident_reason"));
        incidentCoreDetails.setOfficersCount(resultSet.getInt("incident_officer_count"));
        incidentCoreDetails.setAgencyORI(resultSet.getString("incident_agency_ori"));
        incidentCoreDetails.setCiviliansCount(resultSet.getInt("incident_civilian_count"));
        incidentCoreDetails.setScenario(resultSet.getString("incident_scenario"));
        incidentCoreDetails.setCaseNumber(resultSet.getString("incident_caseNumber"));
        incidentCoreDetails.setCustodyEventOptions(resultSet.getString("incident_custody_event"));
        return incidentCoreDetails;
    }
}
