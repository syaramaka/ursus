package com.doj.ursus.impl;

import com.doj.ursus.model.IncidentForceType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceTypeRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        IncidentForceType incidentForceType = new IncidentForceType();
        incidentForceType.setForceId(resultSet.getInt("force_level_force_id"));
        incidentForceType.setForceType(resultSet.getString("force_type"));
        incidentForceType.setForceTypeId(resultSet.getInt("force_type_id"));
        return incidentForceType;
    }
}
