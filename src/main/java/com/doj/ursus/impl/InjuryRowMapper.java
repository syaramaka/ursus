package com.doj.ursus.impl;

import com.doj.ursus.model.Injury;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InjuryRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Injury injury = new Injury();
        injury.setInjuryLevel(resultSet.getInt("injury_level"));
        injury.setInjuryMedicalAid(resultSet.getString("injury_medical_aid"));
        injury.setInjuryId(resultSet.getInt("injury_id"));
        injury.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        injury.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        injury.setInjuryOffCiv(resultSet.getString("injury_civ_off"));
        return injury;
    }
}
