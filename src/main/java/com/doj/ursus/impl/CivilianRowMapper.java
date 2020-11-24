package com.doj.ursus.impl;


import com.doj.ursus.model.Civilians;
import com.doj.ursus.model.User;
import com.doj.ursus.util.Gender;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CivilianRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        Civilians civilians = new Civilians();
        civilians.setCivilianId(rs.getInt("civilian_id"));
        civilians.setCivilianNumber(rs.getInt("civilian_count"));
        civilians.setAge(rs.getInt("civilian_age"));
        civilians.setCustodyStatus(rs.getString("civilian_status"));
        civilians.setHighestCharge(rs.getString("civilian_charge_type"));
        civilians.setGender(Gender.valueOf(rs.getString("civilian_gender")));
        civilians.setRace(rs.getString("civilian_race"));
        civilians.setInjured(rs.getBoolean("civilian_injured"));
        civilians.setInjuryFromPreExisting(rs.getBoolean("civilian_injury_pre_exist"));
        civilians.setAssaultedOfficer(rs.getBoolean("civilian_assault_officer"));
        civilians.setMentalStatus(rs.getString("civilian_mental_status"));
        civilians.setResisted(rs.getBoolean("civilian_resisted"));
        civilians.setPerceivedArmed(rs.getBoolean("civilian_perceived_armed"));
        civilians.setConfirmedArmed(rs.getBoolean("civilian_confirmed_armed"));
        civilians.setReceivedForce(rs.getBoolean("civilian_forced"));
        civilians.setChangeDate(rs.getDate("change_date"));
        civilians.setIncidentId(rs.getInt("incident_incident_id"));
        return civilians;
    }
    /*
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("record_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("address"));
        user.setEmail(rs.getString("email"));
        return user;
    }
     */
}
