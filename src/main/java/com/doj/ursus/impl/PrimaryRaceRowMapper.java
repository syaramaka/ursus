package com.doj.ursus.impl;

import com.doj.ursus.model.PrimaryRace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimaryRaceRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        PrimaryRace primaryRace = new PrimaryRace();
        primaryRace.setPrimaryRaceId(resultSet.getInt("primary_race_id"));
        primaryRace.setPrimaryRace(resultSet.getString("primary_race"));
        primaryRace.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        primaryRace.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        primaryRace.setPrimaryRaceOf(resultSet.getString("primary_race_for"));
        return primaryRace;
    }
}
