package com.doj.ursus.impl;

import com.doj.ursus.model.OfficerCivilians;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficerCiviliansRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        OfficerCivilians officerCivilians = new OfficerCivilians();
        officerCivilians.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        officerCivilians.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        return officerCivilians;
    }
}
