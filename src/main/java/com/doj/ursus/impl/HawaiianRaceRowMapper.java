package com.doj.ursus.impl;

import com.doj.ursus.model.HawaiianPacificIslanderRace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HawaiianRaceRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
        hawaiianPacificIslanderRace.setHawaiianRace(resultSet.getString("hawaiian_race"));
        hawaiianPacificIslanderRace.setHawainRaceId(resultSet.getInt("hawaiian_race_id"));
        hawaiianPacificIslanderRace.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        hawaiianPacificIslanderRace.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        hawaiianPacificIslanderRace.setHawaiianRaceOf(resultSet.getString("hawaiian_race_for"));
        return hawaiianPacificIslanderRace;
    }
}
