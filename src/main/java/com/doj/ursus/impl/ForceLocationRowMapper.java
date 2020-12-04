package com.doj.ursus.impl;

import com.doj.ursus.model.ForceLocation;
import com.doj.ursus.model.IncidentForceLocation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceLocationRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ForceLocation forceLocation = new ForceLocation();
        forceLocation.setForceLocationId(resultSet.getInt("force_location_id"));
        forceLocation.setForceLocation(resultSet.getString("force_location"));
        forceLocation.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        forceLocation.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        forceLocation.setForceOn(resultSet.getString("force_level_on"));
        return forceLocation;
    }
}
