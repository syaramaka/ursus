package com.doj.ursus.impl;

import com.doj.ursus.model.IncidentForceLocation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceLocationRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        IncidentForceLocation incidentForceLocation = new IncidentForceLocation();
        incidentForceLocation.setForceId(resultSet.getInt("force_level_force_id"));
        incidentForceLocation.setForceLocationId(resultSet.getInt("force_location_id"));
        incidentForceLocation.setForceLocation(resultSet.getString("force_location"));
        return incidentForceLocation;
    }
}
