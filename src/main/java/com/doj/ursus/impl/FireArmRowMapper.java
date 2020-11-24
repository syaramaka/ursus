package com.doj.ursus.impl;

import com.doj.ursus.model.FireArm;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FireArmRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        FireArm fireArm = new FireArm();
        fireArm.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        fireArm.setFireArmType(resultSet.getString("firearm_type"));
        fireArm.setFireArmTypeId(resultSet.getInt("firearm_type_id"));
        return fireArm;
    }
}
