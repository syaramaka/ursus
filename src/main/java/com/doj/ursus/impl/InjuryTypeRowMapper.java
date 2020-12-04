package com.doj.ursus.impl;

import com.doj.ursus.model.InjuryType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InjuryTypeRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InjuryType injuryType = new InjuryType();
        injuryType.setInjuryType(resultSet.getString("injury_type"));
        injuryType.setInjuryTypeId(resultSet.getInt("injury_type_id"));
        injuryType.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        injuryType.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        injuryType.setInjutyTypeOn(resultSet.getString("injury_type_on"));
        return injuryType;
    }
}
