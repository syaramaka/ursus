package com.doj.ursus.impl;

import com.doj.ursus.model.Race;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RaceRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        Race race = new Race();
        race.setRaceId(resultSet.getInt("race_id"));
        race.setCivilainsId(resultSet.getInt("civilian_details_civilian_id"));
        race.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        race.setRaceOf(resultSet.getString("race_of"));
        return race;
    }
}
