package com.doj.ursus.impl;

import com.doj.ursus.controller.IncidentController;
import com.doj.ursus.dao.ScreenerDao;
import com.doj.ursus.model.Mail;
import com.doj.ursus.model.Screener;
import com.doj.ursus.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Repository
public class ScreenerDaoImpl implements ScreenerDao {

   // private Properties ursusProperties;
    private Properties ursusProperties = new Properties();
    private static final String URSUS_AGENCY_MAIL_MAPPING = "ursus.email";


//    public ScreenerDaoImpl(Properties properties) {
//        this.ursusProperties = properties;
//    }

//    public void init() {
//        System.out.println(" --------------init() method-------------");
//        ursusAgencyEmailMap = new HashMap<String, String>();
//        populateAgencyEmailMap();
//    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(ScreenerDaoImpl.class);

    private final String INSERT_INCIDENT_SCREENER = "INSERT INTO ursus.screener(\n" +
            "\tscreener_multiple_agencies, screener_discharge_firearm, screener_officer_usedforce, scrrener_civilian_injured_killed, screener_civilian_usedforce, screener_officer_injured_killed, is_incident)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";

    @Override
    public Screener createIncident(Screener screener) {
        String isIncident = isIncidentReport(screener);
        logger.info("isIncident Reportable :"+isIncident);

        if(screener.getIsMultipleAgencies().equals("Y") && (screener.getIsPrimaryAgency().equals("N")))
        {
            sentMail(screener);
        }
        if(screener.getIsPrimaryAgency().equals("Y")) {
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
            logger.info("screener details:" + screener);
            //return screener;
        }
        return screener;
    }

    private String isIncidentReport(Screener screener) {
        String isIncidentReport=null;
        if(screener.getIsCivilianSeriouslyInjured().equals("Y") || (screener.getIsOfficerSeriouslyInjured().equals("Y"))
                || (screener.getIsDischargeOfFirearm().equals("Y")))
        {
            isIncidentReport="Y";
        }
        else{
            isIncidentReport="N";
        }
        return isIncidentReport;
    }

    private Map<String, String> populateAgencyEmailMap() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            ursusProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(" in populateAgencyEmailMap-------------------");
        String trackingUrl;
        Map<String, String> ursusAgencyEmailMap = new HashMap<>();
        for (String propName : ursusProperties.stringPropertyNames()) {
            System.out.println("propName---:"+propName);
            if (propName.contains(URSUS_AGENCY_MAIL_MAPPING)) {
                System.out.println(" propname matched---:"+propName);
                String[] email = propName.split("email.");
                System.out.println("email array :"+ Arrays.toString(email));
                System.out.println("length of the array----:"+email.length);
                System.out.println("first element:"+email[email.length-1]);
                trackingUrl = email[email.length - 1];
                System.out.println(" trackingurl ----:"+trackingUrl);
                System.out.println(" ursusProperties.getProperty(propName) :"+ursusProperties.getProperty(propName));
                ursusAgencyEmailMap.put(trackingUrl, ursusProperties.getProperty(propName));
            }
        }
        return ursusAgencyEmailMap;
    }

    public void sentMail(Screener screener)
    {
        String agencyName = screener.getPrimaryAgencyName();
        String content = "Request to submit incident";
        Map<String, String> map = populateAgencyEmailMap();
        String emailId = map.get(agencyName);
        Mail mail = new Mail();
        mail.setMailFrom("qualapps@gmail.com");
        mail.setMailTo(emailId);
        mail.setMailSubject("Request to Submit Incident");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", agencyName);
        model.put("lastName", "Admin");
        model.put("content", content);
        model.put("location", "URSUS");
        model.put("signature", "Admin");
        mail.setModel(model);

        try {
            mailService.sendEmail(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done!");
    }
}
