package com.doj.ursus.impl;

import com.doj.ursus.controller.IncidentController;
import com.doj.ursus.dao.ScreenerDao;
import com.doj.ursus.model.Screener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ScreenerDaoImpl implements ScreenerDao {

    private final Logger logger = LoggerFactory.getLogger(ScreenerDaoImpl.class);

    private final String INSERT_INCIDENT_SCREENER = "INSERT INTO ursus.screener(\n" +
            "\tscreener_multiple_agencies, screener_discharge_firearm, screener_officer_usedforce, scrrener_civilian_injured_killed, screener_civilian_usedforce, screener_officer_injured_killed, is_incident)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Screener createIncident(Screener screener) {
        String isIncident = isIncidentReport(screener);
        logger.info("isIncident Reportable :"+isIncident);
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_INCIDENT_SCREENER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, screener.getIsPrimaryAgency());
                ps.setString(2, screener.getIsDischargeOfFirearm());
                ps.setString(3, screener.getIsOfficerUsedForce());
                ps.setString(4, screener.getIsCivilianSeriouslyInjured());
                ps.setString(5, screener.getIsCivilianUsedForce());
                ps.setString(6, screener.getIsOfficerSeriouslyInjured());
                //ps.setBoolean(7, screener.isIncident());
                ps.setString(7, isIncident);
                return ps;
            }
        }, holder);

        //int newUserId = holder.getKey().intValue();
        int newUserId = (int) holder.getKeys().get("screener_id");
        screener.setScreenerId(newUserId);
        screener.setIsIncident(isIncident);
        logger.info("screener details:"+screener);
        return screener;
    }

    private String isIncidentReport(Screener screener) {
        return "Y";
    }
}
