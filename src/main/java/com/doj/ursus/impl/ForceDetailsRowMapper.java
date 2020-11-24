package com.doj.ursus.impl;

import com.doj.ursus.model.ForceDetails;
import com.doj.ursus.model.OfficerCivilians;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceDetailsRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        ForceDetails forceDetails = new ForceDetails();
        forceDetails.setForceId(resultSet.getInt("force_id"));
        forceDetails.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        forceDetails.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        forceDetails.setForceOn(resultSet.getString("force_on"));
        return forceDetails;

    }
}
