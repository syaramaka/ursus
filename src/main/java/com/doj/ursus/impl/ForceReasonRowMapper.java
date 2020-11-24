package com.doj.ursus.impl;

import com.doj.ursus.model.ForceReason;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForceReasonRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ForceReason forceReason = new ForceReason();
        forceReason.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        forceReason.setForceReason(resultSet.getString("force_reason"));
        forceReason.setForceReasonId(resultSet.getInt("force_reason_id"));
        return forceReason;
    }
}
