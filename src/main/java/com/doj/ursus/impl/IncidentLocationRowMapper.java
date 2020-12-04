package com.doj.ursus.impl;

import com.doj.ursus.model.IncidentLocation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentLocationRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        IncidentLocation incidentLocation = new IncidentLocation();
        incidentLocation.setZip(resultSet.getString("address_zip"));
        incidentLocation.setStreetName(resultSet.getString("address_street"));
        incidentLocation.setState(resultSet.getString("address_state"));
        incidentLocation.setLongitude(resultSet.getDouble("address_long"));
        incidentLocation.setLatitude(resultSet.getDouble("address_lat"));
        incidentLocation.setIsOnK12Campus(resultSet.getString("address_k12"));
        incidentLocation.setIncidentAddressSequence(resultSet.getInt("address_sequence"));
        incidentLocation.setCounty(resultSet.getString("address_county"));
        incidentLocation.setCity(resultSet.getString("address_city"));
        incidentLocation.setLocationDetails(resultSet.getString("address_location_detail"));
        incidentLocation.setIncidentId(resultSet.getInt("incident_incident_id"));
        incidentLocation.setLocationId(resultSet.getInt("address_id"));
        return incidentLocation;
    }
}
