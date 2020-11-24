package com.doj.ursus.impl;

import com.doj.ursus.model.InjuryType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InjuryTypeRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InjuryType injuryType = new InjuryType();
        injuryType.setInjuryId(resultSet.getInt("injury_id"));
        injuryType.setInjuryType(resultSet.getString("injury_type"));
        injuryType.setInjuryTypeId(resultSet.getInt("injury_type_id"));
        return injuryType;
    }
}
