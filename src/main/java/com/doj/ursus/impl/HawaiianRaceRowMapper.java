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
        hawaiianPacificIslanderRace.setRaceId(resultSet.getInt("race_id"));
        return hawaiianPacificIslanderRace;
    }
}
