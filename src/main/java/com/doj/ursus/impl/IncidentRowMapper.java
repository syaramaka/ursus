package com.doj.ursus.impl;

import com.doj.ursus.model.Incident;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Incident incident = new Incident();
        incident.setIncidentId(resultSet.getInt("incident_id"));
        incident.setPrimaryAgency(resultSet.getString("incident_primary_agency"));
        incident.setIncidentDate(resultSet.getDate("incident_date"));
        incident.setIsArrestMade(resultSet.getString("incident_arrests"));
        incident.setIsCrimeReportFiled(resultSet.getString("incident_crime_report"));
        incident.setIncidentReason(resultSet.getString("incident_reason"));
        incident.setOfficersCount(resultSet.getInt("incident_officer_count"));
        incident.setAgencyORI(resultSet.getString("incident_agency_ori"));
        incident.setCiviliansCount(resultSet.getInt("incident_civilian_count"));
        incident.setScenario(resultSet.getString("incident_scenario"));
        incident.setCaseNumber(resultSet.getString("incident_caseNumber"));
        incident.setCustodyEventOptions(resultSet.getString("incident_custody_event"));
        return incident;
    }
}
