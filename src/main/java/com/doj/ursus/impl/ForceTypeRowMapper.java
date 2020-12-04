package com.doj.ursus.impl;

import com.doj.ursus.model.ForceType;
import com.doj.ursus.model.IncidentForceType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceTypeRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ForceType forceType = new ForceType();
        forceType.setForceType(resultSet.getString("force_type"));
        forceType.setForceTypeId(resultSet.getInt("force_type_id"));
        forceType.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        forceType.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        forceType.setForceOn(resultSet.getString("force_type_on"));
        return forceType;
    }
}
