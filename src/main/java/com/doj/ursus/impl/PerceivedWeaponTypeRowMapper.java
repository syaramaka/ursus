package com.doj.ursus.impl;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PerceivedWeaponTypeRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        PerceivedWeaponType perceivedWeaponType = new PerceivedWeaponType();
        perceivedWeaponType.setPerceivedWeapon(resultSet.getString("perceived_weapon"));
        perceivedWeaponType.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        perceivedWeaponType.setPerWeaponId(resultSet.getInt("perceived_weapon_id"));
        return perceivedWeaponType;
    }
}
