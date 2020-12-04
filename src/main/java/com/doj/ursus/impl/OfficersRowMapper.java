package com.doj.ursus.impl;

import com.doj.ursus.model.Officers;
import com.doj.ursus.util.Gender;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficersRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Officers officers = new Officers();
        officers.setOfficerId(resultSet.getInt("officer_id"));
        officers.setOfficerOrder(resultSet.getInt("officer_sequence"));
        officers.setOfficerUsedForce(resultSet.getBoolean("officer_force_used"));
        officers.setAge(resultSet.getInt("officer_age"));
        officers.setGender(Gender.valueOf(resultSet.getString("officer_gender")));
        officers.setInjured(resultSet.getBoolean("officer_injured"));
        officers.setInjuryFromPreExistingCondition(resultSet.getBoolean("officer_injury_pre_exist"));
        officers.setOfficerAssaulted(resultSet.getBoolean("officer_assaulted"));
        officers.setOnDuty(resultSet.getBoolean("officer_on_duty"));
        officers.setDress(resultSet.getString("officer_dress"));
        officers.setChangeDate(resultSet.getDate("change_date"));
        officers.setIncidentId(resultSet.getInt("incident_incident_id"));
        officers.setAgency(resultSet.getString("officer_agency"));
        return officers;
    }
}
