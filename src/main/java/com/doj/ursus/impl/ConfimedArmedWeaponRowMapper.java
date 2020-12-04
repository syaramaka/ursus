package com.doj.ursus.impl;

import com.doj.ursus.model.ConfimedArmedWeapon;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfimedArmedWeaponRowMapper implements RowMapper {


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfimedArmedWeapon confimedArmedWeapon = new ConfimedArmedWeapon();
        confimedArmedWeapon.setConfirmedWeapon(resultSet.getString("conf_armed_weapon"));
        confimedArmedWeapon.setCivilianId(resultSet.getInt("civilian_details_civilian_id"));
        confimedArmedWeapon.setConfirmedWeaponId(resultSet.getInt("conf_armed_weapon_id"));
        return confimedArmedWeapon;
    }
}
