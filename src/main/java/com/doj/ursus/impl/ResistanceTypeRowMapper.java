package com.doj.ursus.impl;

import com.doj.ursus.model.ResistanceType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResistanceTypeRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ResistanceType resistanceType = new ResistanceType();
        resistanceType.setResistanceType(resultSet.getString("resistance_type"));
        resistanceType.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        resistanceType.setResistanceId(resultSet.getInt("resistance_id"));
        return resistanceType;
    }
}
