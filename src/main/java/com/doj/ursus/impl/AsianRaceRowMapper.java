package com.doj.ursus.impl;

import com.doj.ursus.model.AsianRace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsianRaceRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AsianRace asianRace = new AsianRace();
        asianRace.setAsianRace(resultSet.getString("asian_race"));
        asianRace.setAsianRaceId(resultSet.getInt("asian_race_id"));
        asianRace.setAsianRaceOf(resultSet.getString("asian_race_for"));
        asianRace.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        asianRace.setOfficerId(resultSet.getInt("officer_details_officer_id"));
        return asianRace;
    }
}
