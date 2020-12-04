package com.doj.ursus.impl;

import com.doj.ursus.dao.IncidentDao;
import com.doj.ursus.dao.ScreenerDao;
import com.doj.ursus.model.*;
import com.doj.ursus.util.IncidentSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class IncidentDaoImpl implements IncidentDao {

    private final Logger logger = LoggerFactory.getLogger(IncidentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    IncidentSql incidentSql;

    @Autowired
    ScreenerDao screenerDao;

    @Override
    @Transactional
    public Incident createIncident(Incident details) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(incidentSql.INSERT_INCIDENT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, details.getPrimaryAgency());
                ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                ps.setString(3, details.getIsArrestMade()); // need to change to string Y /N
                ps.setString(4, details.getIsCrimeReportFiled());
                ps.setString(5, details.getIncidentReason());
                ps.setInt(6, details.getOfficersCount());
                ps.setString(7, details.getAgencyORI());
                ps.setInt(8, details.getCiviliansCount());
                ps.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                ps.setString(10, details.getScenario());
                ps.setString(11, details.getCaseNumber());
                ps.setString(12, details.getCustodyEventOptions()); // need to add column in table
                return ps;
            }
        }, holder);

        //int newUserId = holder.getKey().intValue();
        int incidentId = (int) holder.getKeys().get("incident_id");
        logger.info("incident id:" + incidentId);
        details.setIncidentId(incidentId);

        List<Address> addresses = details.getAddresses();
        int[] countOfIncidentLocations = submitIncidentAddress(addresses, incidentId);
        logger.info("total locations inserted in table :" + countOfIncidentLocations.length);

        List<Civilians> civiliansList = details.getCivilians();
        logger.info("civilains list:" + civiliansList);
        int[] countOfCiviliansInIncident = submitCiviliansInvolvedInIncident(civiliansList, incidentId);
        logger.info("total civilians inserted in table :" + countOfCiviliansInIncident.length);

        List<Officers> officersList = details.getOfficers();
        logger.info("officers list:" + officersList);
        List<Civilians> civilianInIncident = getCivilianForIncident(incidentId);
        logger.info("civilianInIncident for given IncidentId :" + civilianInIncident);
        List<Civilians> updtciviliansList = getUpdatedCivilianForIncident(civilianInIncident, civiliansList);
        logger.info("Civilinas list afterinserting civlian table:" + updtciviliansList);
        int[] countOfOfficersInIncident = submitOfficersInvolvedInIncident(officersList, incidentId);
        logger.info("total officers inserted in table:" + countOfOfficersInIncident.length);
        List<Officers> officersInIncident = getOfficrsForIncident(incidentId);
        logger.info("officersInIncident for given IncidentId :" + officersInIncident.toString());

        List<Officers> officers = getUpdatedOfficersForIncident(officersInIncident, officersList);
        logger.info("Officers list afterinserting officer table --- :" + officers);
        insertOfficersForceReason(officers);
        insertFireArmDetails(updtciviliansList);
        List<OfficerCivilians> officerCivilians = getEngagedOfficersForCivilians(officers, updtciviliansList, incidentId);
        logger.info("officercivilians details:" + officerCivilians);
        int[] countOfOfficerCivilianIdsUpdate = submitOfficersCiviliansIds(officerCivilians);
        logger.info("total officerscivilans inserted in to table:" + countOfOfficerCivilianIdsUpdate.length);
        List<Integer> offCivList = getUpdatedForceDetails(officerCivilians);
        logger.info("officer civlians id's involved in incident:" + offCivList);

        int[] forceLocationDetailsInserted = insertForceLocationDetails(officerCivilians, updtciviliansList, officers, offCivList);
        logger.info("total force location Details Inserted in to table:" + forceLocationDetailsInserted.length);
        int[] forceTypeDetailsInserted = insertForceTypeDetails(officerCivilians, updtciviliansList, officers, offCivList);
        logger.info("total force type Details Inserted in to table:" + forceTypeDetailsInserted.length);

        int[] countOfInjuryTypeDetailsInserted = insertInjuryTypeDetails(officerCivilians, updtciviliansList, officers, offCivList);
        logger.info("total Injury type details inserted in to table:" + countOfInjuryTypeDetailsInserted.length);

        int[] countOfPrimaryRaceDetails = insertPrimaryRaceDetails(officerCivilians, updtciviliansList, officers);
        logger.info("total primary race details inserted in to table :"+countOfPrimaryRaceDetails.length);

        int[] countOfAsianRaceDetails = insertAsianRaceDetails(officerCivilians, updtciviliansList, officersList);
        logger.info("total asian race details inserted in to table :"+countOfAsianRaceDetails.length);

        int[] countOfHawaiianRaceDetails = insertHawaiianRaceDetails(officerCivilians, updtciviliansList, officersList);
        logger.info("total hawaiian race details inserted in to table :"+countOfHawaiianRaceDetails.length);

       // perceived weapon type
        List<PerceivedWeaponType> perceivedWeaponTypeList = getPerceivedWeaponTypeList(updtciviliansList);
        if (!perceivedWeaponTypeList.isEmpty()) {
            int[] countOfPerceivedWeaponTypeList = submitperceivedWeaponTypeList(perceivedWeaponTypeList);
            logger.info("total count of perceived weapon inserted in to table:" + countOfPerceivedWeaponTypeList.length);
        }
        // perceived weapon type
        List<ConfimedArmedWeapon> confirmedWeaponTypeList = getConfimedWeaponTypeList(updtciviliansList);
        if (!confirmedWeaponTypeList.isEmpty()) {
            int[] countOfconfirmedWeaponTypeList = submitConfirmedWeaponTypeList(confirmedWeaponTypeList);
            logger.info("total count of confirmed weapontype inserted in to table:" + countOfconfirmedWeaponTypeList.length);
        }
        //resistance type details
        List<ResistanceType> resistanceTypeList = getResistanceTypeList(updtciviliansList);
        if (!resistanceTypeList.isEmpty()) {
            int[] countOfResistanceTypeList = submitResistanceTypeList(resistanceTypeList);
            logger.info("total count of resistance type inserted in to table:" + countOfResistanceTypeList.length);
        }
        return details;
    }

    @Override
    public Incidents createBulkIncidents(Incidents incidents)
    {
        List<Incident> incidentList = incidents.getIncident();
        List<Incident> incidentList1 = new ArrayList<>();

        for(Incident incident : incidentList)
        {
            Incident incident1 = insertBulkIncidents(incident);
            incidentList1.add(incident1);
        }
        Incidents incidents1 = new Incidents();
        incidents1.setIncident(incidentList);
        return incidents1;
    }



    @Transactional
    public Incident insertBulkIncidents(Incident incident)
    {
        Incident incident2 = new Incident();
        try {
            Screener screener = screenerDao.createIncident(incident.getScreener());
            incident2 = createIncident(incident);
            incident2.setScreener(screener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incident2;
    }

    @Override
    public Incident getIncidentDetails(int incidentId) {
        logger.info("given IncidentId :" + incidentId);
        Incident incident = getIncidentBasicDetails(incidentId);

        List<Civilians> civiliansList = getCivilians(incidentId);
        logger.info("civiliansList:"+civiliansList);

        List<Civilians> civiliansWithInjuryDetails = getCiviliansWithInjuryDetails(civiliansList);
        logger.info("civiliansWithInjuryDetails:"+civiliansWithInjuryDetails);

        List<Civilians> civiliansWithPrimaryRaceDetails = getUpdatedCiviliansWithPrimaryRaceDetails(civiliansWithInjuryDetails);
        logger.info("civilians with primary race details :"+civiliansWithPrimaryRaceDetails);

        List<Civilians> civiliansWithAsianRaceDetails = getUpdatedCiviliansWithAsianRaceDetails(civiliansWithPrimaryRaceDetails);
        logger.info("civilians with asian race details :"+civiliansWithAsianRaceDetails);

        List<Civilians> civiliansWithHawaiianDetails = getUpdateCivilainDetailsWithHawaiianRaceDetails(civiliansWithAsianRaceDetails);
        logger.info("civilians with hawaiian race details :"+civiliansWithHawaiianDetails);

        List<Civilians> civiliansWithFireArmDetails = getCiviliansWithFireArmDetails(civiliansWithHawaiianDetails);
        logger.info("civiliansWithFireArmDetails:"+civiliansWithFireArmDetails);

        List<Civilians> civiliansWithForceLocation = getCiviliansWithForceLocationDetails(civiliansWithFireArmDetails);
        logger.info("civilians with force location details :"+civiliansWithForceLocation);

        List<Civilians> civiliansWithForceType = getCiviliansWithForceTypeDetails(civiliansWithForceLocation);
        logger.info("civilians with force type details :"+civiliansWithForceType);

        List<Civilians> civiliansWithConfirmedArmedWeaponDetails = getCiviliansWithConfirmedArmedWeapons(civiliansWithForceType);
        logger.info("civiliansWithConfirmedArmedWeaponDetails:"+civiliansWithConfirmedArmedWeaponDetails);

        List<Civilians> civiliansWithPerceivedWeaponTypeDetails = getCiviliansWithPerceivedWeaponTypeDetails(civiliansWithConfirmedArmedWeaponDetails);
        logger.info("civiliansWithPerceivedWeaponTypeDetails:"+civiliansWithPerceivedWeaponTypeDetails);

        List<Civilians> civiliansWithResistanceTypeDetails = getCiviliansWithResistanceTypeDetails(civiliansWithPerceivedWeaponTypeDetails);
        logger.info("civiliansWithResistanceTypeDetails:"+civiliansWithResistanceTypeDetails);

        List<Officers> officersList = getOfficers(incidentId);
        logger.info("officersList:"+officersList);
        List<Officers> officersWithInjuryDetails = getOfficersWithInjuryDetails(officersList);
        logger.info("officersWithInjuryDetails:"+officersWithInjuryDetails);

        List<Officers> officersWithPrimaryRaceList = getUpdatedOfficersWithPrimaryRaceDetails(officersWithInjuryDetails);
        logger.info("officers with primary race details :"+officersWithPrimaryRaceList);

        List<Officers> officersWithAsianRaceList = getUpdatedOfficersWithAsianRaceDetails(officersWithPrimaryRaceList);
        logger.info("officers with asian race list :"+officersWithAsianRaceList);

        List<Officers> officersWithHawaiianRaceList = getUpdatedOfficersWithHawaiianRaceList(officersWithAsianRaceList);
        logger.info(" officers with hawaiian race list :"+officersWithHawaiianRaceList);

        List<Officers> officersWithForceLocation = getOfficersWithForceLocationDetails(officersWithHawaiianRaceList);
        logger.info("officers with force location details :"+officersWithForceLocation);

        List<Officers> officersWithForceType = getOfficersWithForceTypeDetails(officersWithForceLocation);
        logger.info(" officers with force type details :"+officersWithForceType);

        List<Officers> officersWithForceReasonDetails = getOfficersWithForceReasonDetails(officersWithForceType);
        logger.info("officersWithForceReasonDetails:"+officersWithForceReasonDetails);

        List<Address> addressList1 = getIncidentAddressDetails(incidentId);
        logger.info("address list:"+addressList1);
        incident.setAddresses(addressList1);
        incident.setOfficers(officersWithForceReasonDetails);
        incident.setCivilians(civiliansWithResistanceTypeDetails);
        return incident;
    }

    public List<Officers> getUpdatedOfficersWithHawaiianRaceList(List<Officers> officersWithAsianRaceList) {
        List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaceList = getOfficersHawaiianRaceDetails(officersWithAsianRaceList);
        if (!hawaiianPacificIslanderRaceList.isEmpty()) {
            return getUpdatedOfficersWithHawaiianRaceDetails(officersWithAsianRaceList, hawaiianPacificIslanderRaceList);
        } else {
            return officersWithAsianRaceList;
        }
    }


    public List<Civilians> getUpdateCivilainDetailsWithHawaiianRaceDetails(List<Civilians> civiliansWithAsianRaceDetails) {
        List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaceList = getCiviliansHawaiianRaceDetails(civiliansWithAsianRaceDetails);
        if (!hawaiianPacificIslanderRaceList.isEmpty()) {
            return getUpdateCivilainsWithHawaiianRaceList(civiliansWithAsianRaceDetails, hawaiianPacificIslanderRaceList);
        } else {
            return civiliansWithAsianRaceDetails;
        }
    }

    public List<Officers> getUpdatedOfficersWithAsianRaceDetails(List<Officers> officersWithPrimaryRaceList) {
        List<AsianRace> asianRaceList = getOfficersAsianRaceDetails(officersWithPrimaryRaceList);
        if (!asianRaceList.isEmpty()) {
            return getUpdatedOffWithAsianRaceDetails(officersWithPrimaryRaceList, asianRaceList);
        } else {
            return officersWithPrimaryRaceList;
        }
    }

    public List<Civilians> getUpdatedCiviliansWithAsianRaceDetails(List<Civilians> civiliansWithPrimaryRaceDetails) {
        List<AsianRace> asianRaceList = getCiviliansAsianRaceDetails(civiliansWithPrimaryRaceDetails);
        if (!asianRaceList.isEmpty()) {
            return getUpdateCivilainsWithAsianRaceList(civiliansWithPrimaryRaceDetails, asianRaceList);
        } else {
            return civiliansWithPrimaryRaceDetails;
        }
    }

    public List<Officers> getUpdatedOfficersWithPrimaryRaceDetails(List<Officers> officersWithInjuryDetails) {
        List<PrimaryRace> primaryRaceList = getOfficersPrimaryRaceDetails(officersWithInjuryDetails);
        if (!primaryRaceList.isEmpty()) {
            return getUpdateOfficersWithPrimaryRaceList(officersWithInjuryDetails, primaryRaceList);
        } else {
            return officersWithInjuryDetails;
        }
    }

    public List<Civilians> getUpdatedCiviliansWithPrimaryRaceDetails(List<Civilians> civiliansWithInjuryDetails) {
        List<PrimaryRace> primaryRaceList = getCiviliansPrimaryRaceDetails(civiliansWithInjuryDetails);
        if (!primaryRaceList.isEmpty()) {
            return getUpdateCivilainsWithPrimaryRaceList(civiliansWithInjuryDetails, primaryRaceList);
        } else {
            return civiliansWithInjuryDetails;
        }
    }

    public void insertFireArmDetails(List<Civilians> updtciviliansList) {
        List<FireArm> armList = getFireArmListInIncident(updtciviliansList);
        logger.info("firearm list before inserting:" + armList);
        int[] countOfUpdatedFireArmsInIncident = submitFireArmsUsedInIncident(armList);
        logger.info("total firearms inserted in to table:"+countOfUpdatedFireArmsInIncident.length);
    }

    public void insertOfficersForceReason(List<Officers> officers) {
        List<ForceReason> forceReasonList = getForceReasonForIncident(officers);
        logger.info("force Reason list in incident :" + forceReasonList);
        int[] countOfUpdatedForceReasonInIncident = submitForceReasonInIncident(forceReasonList);
        logger.info("total forceReason inserted in to table:"+countOfUpdatedForceReasonInIncident.length);
    }

    public int[] insertForceLocationDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList, List<Integer> offCivList)
    {
        List<ForceLocation> forceLocationListOfCivilians = getForceLocationDetailsOfCivilians(officerCivilians, civiliansList1);
        logger.info(" civilains forcelocation details :"+forceLocationListOfCivilians);
        List<ForceLocation> forceLocationListOfOfficers = getForceLocationDetailsOfOfficers(officerCivilians, officersList);
        logger.info(" officers forcelocation details :"+forceLocationListOfOfficers);
        forceLocationListOfCivilians.addAll(forceLocationListOfOfficers);
        logger.info(" force location details in the incident :"+forceLocationListOfCivilians);
        int[] countOfIncidentForceLocationSubmitted = submitIncidentForceLocationDetails(forceLocationListOfCivilians);
        logger.info("total force location details inserted in to table:"+countOfIncidentForceLocationSubmitted.length);
        return countOfIncidentForceLocationSubmitted;
    }

    public int[] insertForceTypeDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList, List<Integer> offCivList)
    {
        List<ForceType> forceTypeListOfCivilians = getForceTypeDetailsOfCivilians(officerCivilians, civiliansList1);
        logger.info(" civilains force type details :"+forceTypeListOfCivilians);
        List<ForceType> forceTypeListOfOfficers = getForceTypeDetailsOfOfficers(officerCivilians, officersList);
        logger.info(" officers force type details :"+forceTypeListOfOfficers);
        forceTypeListOfCivilians.addAll(forceTypeListOfOfficers);
        logger.info(" force type details in the incident :"+forceTypeListOfCivilians);
        int[] countOfIncidentForceTypeSubmitted = submittedForceTypeForIncident(forceTypeListOfCivilians);
        logger.info("total force type details inserted in to table:"+countOfIncidentForceTypeSubmitted.length);
        return countOfIncidentForceTypeSubmitted;
    }

    public int[] insertInjuryTypeDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList, List<Integer> offCivList)
    {
        List<InjuryType> civilainsInjuryTypeDetails = getCiviliansInjuryTypeList(officerCivilians, civiliansList1);
        logger.info("injury type details for civilains in incident:" + civilainsInjuryTypeDetails);
        List<InjuryType> officersInjuryTypeDetails = getOfficersInjuryTypeList(officerCivilians, officersList);
        logger.info("injury type details for officers in incident:" + officersInjuryTypeDetails);
        civilainsInjuryTypeDetails.addAll(officersInjuryTypeDetails);
        logger.info("total injuries details:"+civilainsInjuryTypeDetails);
        int[] countOfOffCivliansInjuryDetailsSubmitted = submitInjuryTypeDetailsOfIncident(civilainsInjuryTypeDetails);
        logger.info("total Injuriey types inserted in to table:"+ countOfOffCivliansInjuryDetailsSubmitted.length);
        return countOfOffCivliansInjuryDetailsSubmitted;
    }

    public List<PrimaryRace> getOfficersPrimaryRace(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getPrimaryRaceType() != null)) {
                    for(String prim : officers.getPrimaryRaceType())
                    {
                        PrimaryRace primaryRace = new PrimaryRace();
                        primaryRace.setPrimaryRace(prim);
                        primaryRace.setOfficerId(officers.getOfficerId());
                        primaryRace.setPrimaryRaceOf("O");
                        primaryRace.setCivilianId(offCiv.getCivilianId());
                        primaryRaceList.add(primaryRace);
                    }

                }
            }
        }
        return primaryRaceList.stream().distinct().collect(Collectors.toList());
    }

    public List<PrimaryRace> getCivilianPrimaryRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1)
    {
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civiliansList1) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getPrimaryRaceType() != null)) {
                    for(String primRace : civilians.getPrimaryRaceType())
                    {
                        PrimaryRace primaryRace = new PrimaryRace();
                        primaryRace.setCivilianId(civilians.getCivilianId());
                        primaryRace.setPrimaryRaceOf("C");
                        primaryRace.setOfficerId(offCiv.getOfficerId());
                        primaryRace.setPrimaryRace(primRace);
                        primaryRaceList.add(primaryRace);
                    }

                }
            }
        }
        return primaryRaceList.stream().distinct().collect(Collectors.toList());
    }

    public List<AsianRace> getOfficersAsianRace(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<AsianRace> asianRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getAsianRaceType() != null)) {
                    for(String asian : officers.getAsianRaceType())
                    {
                        AsianRace asianRace = new AsianRace();
                        asianRace.setAsianRace(asian);
                        asianRace.setOfficerId(officers.getOfficerId());
                        asianRace.setAsianRaceOf("O");
                        asianRace.setCivilianId(offCiv.getCivilianId());
                        asianRaceList.add(asianRace);
                    }

                }
            }
        }
        return asianRaceList.stream().distinct().collect(Collectors.toList());
    }

    public List<AsianRace> getCivilianAsianRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1)
    {
        List<AsianRace> asianRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civiliansList1) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getAsianRaceType() != null)) {
                    for(String asRace : civilians.getAsianRaceType())
                    {
                        AsianRace asianRace = new AsianRace();
                        asianRace.setCivilianId(civilians.getCivilianId());
                        asianRace.setAsianRaceOf("C");
                        asianRace.setOfficerId(offCiv.getOfficerId());
                        asianRace.setAsianRace(asRace);
                        asianRaceList.add(asianRace);
                    }

                }
            }
        }
        return asianRaceList.stream().distinct().collect(Collectors.toList());
    }

    public List<HawaiianPacificIslanderRace> getOfficersHawaiianRace(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<HawaiianPacificIslanderRace> hawaiianRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getHawaiianRaceType() != null)) {
                    for(String hawaiian : officers.getHawaiianRaceType())
                    {
                        HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
                        hawaiianPacificIslanderRace.setHawaiianRace(hawaiian);
                        hawaiianPacificIslanderRace.setOfficerId(officers.getOfficerId());
                        hawaiianPacificIslanderRace.setHawaiianRaceOf("O");
                        hawaiianPacificIslanderRace.setCivilianId(offCiv.getCivilianId());
                        hawaiianRaceList.add(hawaiianPacificIslanderRace);
                    }

                }
            }
        }
        return hawaiianRaceList.stream().distinct().collect(Collectors.toList());
    }

    public List<HawaiianPacificIslanderRace> getCivilianHawaiianRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1)
    {
        List<HawaiianPacificIslanderRace> hawaiianRaceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civiliansList1) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getHawaiianRaceType() != null)) {
                    for(String hawaiianRace : civilians.getHawaiianRaceType())
                    {
                        HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
                        hawaiianPacificIslanderRace.setCivilianId(civilians.getCivilianId());
                        hawaiianPacificIslanderRace.setHawaiianRace("C");
                        hawaiianPacificIslanderRace.setOfficerId(offCiv.getOfficerId());
                        hawaiianPacificIslanderRace.setHawaiianRace(hawaiianRace);
                        hawaiianRaceList.add(hawaiianPacificIslanderRace);
                    }

                }
            }
        }
        return hawaiianRaceList.stream().distinct().collect(Collectors.toList());
    }


    public int[] insertPrimaryRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList)
    {
        List<PrimaryRace> civilainsPrimaryRaceList = getCivilianPrimaryRaceDetails(officerCivilians, civiliansList1);
        logger.info(" civilians primary race list :"+civilainsPrimaryRaceList);
        List<PrimaryRace> officersPrimaryRaceList = getOfficersPrimaryRace(officerCivilians, officersList);
        logger.info(" officers primary race list :"+officersPrimaryRaceList);
        civilainsPrimaryRaceList.addAll(officersPrimaryRaceList);
        logger.info(" primary race details of incident :"+civilainsPrimaryRaceList);
        int[] countOfInsertPrimaryRaceDetails = submittPrimaryRaceDetailsOfIncident(civilainsPrimaryRaceList);
        logger.info(" total count of primary race details inserted in to table :"+countOfInsertPrimaryRaceDetails.length);
        return countOfInsertPrimaryRaceDetails;
    }

    public int[] insertAsianRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList)
    {
        List<AsianRace> civilainsAsianRaceList = getCivilianAsianRaceDetails(officerCivilians, civiliansList1);
        logger.info(" civilians asian race list :"+civilainsAsianRaceList);
        List<AsianRace> officersAsianRaceList = getOfficersAsianRace(officerCivilians, officersList);
        logger.info(" officers asian race list :"+officersAsianRaceList);
        civilainsAsianRaceList.addAll(officersAsianRaceList);
        logger.info(" asian race details of incident :"+civilainsAsianRaceList);
        int[] countOfInsertAsianRaceDetails = submittAsianRaceDetailsOfIncident(civilainsAsianRaceList);
        logger.info(" total count of asian race details inserted in to table :"+countOfInsertAsianRaceDetails.length);
        return countOfInsertAsianRaceDetails;
    }

    public int[] insertHawaiianRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList)
    {
        List<HawaiianPacificIslanderRace> civilainsHawaiianPacificRaceList = getCivilianHawaiianRaceDetails(officerCivilians, civiliansList1);
        logger.info(" civilians hawaiian race list :"+civilainsHawaiianPacificRaceList);
        List<HawaiianPacificIslanderRace> officersHawaiianRaceList = getOfficersHawaiianRace(officerCivilians, officersList);
        logger.info(" officers hawaiian race list :"+officersHawaiianRaceList);
        civilainsHawaiianPacificRaceList.addAll(officersHawaiianRaceList);
        logger.info(" hawaiian race details of incident :"+civilainsHawaiianPacificRaceList);
        int[] countOfInsertHawaiianRaceDetails = submittHawaiianRaceDetailsOfIncident(civilainsHawaiianPacificRaceList);
        logger.info(" total count of hawaiian race details inserted in to table :"+countOfInsertHawaiianRaceDetails.length);
        return countOfInsertHawaiianRaceDetails;
    }

    public List<Officers> getOfficersWithCivilians(List<Officers> officersList, List<OfficerCivilians> officerCivilians) {
        List<Officers> officersList1 = new ArrayList<>();
        for (Officers officers : officersList) {
            List<Integer> engagedCivilians = new ArrayList<>();
            for (OfficerCivilians offCiv : officerCivilians) {
                if (officers.getOfficerId() == offCiv.getOfficerId()) {
                    engagedCivilians.add(offCiv.getCivilianId());
                    officers.setEngagedCivilians(engagedCivilians);
                    officersList1.add(officers);
                }
            }
        }
        return officersList1;
    }

    public List<Civilians> getCiviliansWithOfficers(List<Civilians> civiliansList, List<OfficerCivilians> officerCivilians) {
        List<Civilians> civiliansList1 = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            List<Integer> involvedOfficersList = new ArrayList<>();
            for (OfficerCivilians offCiv : officerCivilians) {
                if (civilians.getCivilianId() == offCiv.getCivilianId()) {
                    involvedOfficersList.add(offCiv.getOfficerId());
                    civilians.setEngagedOfficers(involvedOfficersList);
                    civiliansList1.add(civilians);
                }
            }
        }
        return civiliansList1;
    }

    public List<Civilians> getCiviliansWithInjuryDetails(List<Civilians> civiliansList) {
        List<InjuryType> civilianInjuryList = getCiviliansInjuryDetails(civiliansList);
        if (!civilianInjuryList.isEmpty()) {
            return getUpdatedCiviliansWithInjuryTypeForIncident(civiliansList, civilianInjuryList);
        } else {
            return civiliansList;
        }
    }

    public List<Officers> getOfficersWithInjuryDetails(List<Officers> officersList) {
        List<InjuryType> officersInjuryType = getOfficersInjuriesWithInjuryType(officersList);
        if (!officersInjuryType.isEmpty()) {
            return getUpdatedOfficersWithInjuryTypeForIncident(officersList, officersInjuryType);
        } else {
            return officersList;
        }
    }

    public List<ForceReason> getOfficersForceReasonDetails(List<Officers> officersList) {
        List<Integer> officerIds = getOfficerIds(officersList);
        logger.info("OfficersForceReasonDetails for officer_details_officer_id: " + officerIds);
        String inParams = officerIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        List<ForceReason> forceReasonList = jdbcTemplate.query(String.format(incidentSql.GET_OFFICERS_FORCE_REASON, inParams), new ForceReasonRowMapper());
        return forceReasonList;
    }

    public List<Officers> getOfficersWithForceReasonDetails(List<Officers> officersWithForceDetails) {
        List<ForceReason> forceReasonList = getOfficersForceReasonDetails(officersWithForceDetails);
        logger.info("officers forcereason list:" + forceReasonList);
        if (!forceReasonList.isEmpty()) {
            List<Officers> officersWithForceReason = getUpdatedOfficersWithForceReason(forceReasonList, officersWithForceDetails);
            return officersWithForceDetails.stream()
                    .map(officers -> officersWithForceReason.stream()
                            .filter(i -> i.getOfficerId() == (officers.getOfficerId()))
                            .findFirst().orElse(officers))
                    .collect(Collectors.toList());
        }
        return officersWithForceDetails;
    }

    public List<Civilians> getCiviliansWithResistanceTypeDetails(List<Civilians> civiliansWithPerceivedWeaponTypeDetails) {
        List<ResistanceType> resistanceTypeList = getResistanceTypeListForCivilian(civiliansWithPerceivedWeaponTypeDetails);
        if (!resistanceTypeList.isEmpty()) {
            List<Civilians> civiliansWithResistance = getUpdatedCiviliansWithResistance(resistanceTypeList, civiliansWithPerceivedWeaponTypeDetails);
            return civiliansWithPerceivedWeaponTypeDetails.stream()
                    .map(person -> civiliansWithResistance.stream()
                            .filter(i -> i.getCivilianId() == (person.getCivilianId()))
                            .findFirst().orElse(person))
                    .collect(Collectors.toList());
        }
        return civiliansWithPerceivedWeaponTypeDetails;
    }

    public List<Civilians> getCiviliansWithPerceivedWeaponTypeDetails(List<Civilians> civiliansWithConfirmedArmedWeaponDetails) {
        try {
            List<PerceivedWeaponType> perceivedWeaponTypeList = getPerceivedWeaponTypeListForCivilian(civiliansWithConfirmedArmedWeaponDetails);
            logger.info("perceived Weapon Type List:" + perceivedWeaponTypeList);
            List<Civilians> civiliansWithPerceivedWeaponList;
            if (!perceivedWeaponTypeList.isEmpty()) {
                civiliansWithPerceivedWeaponList = getUpdatedPerceivedWeaponsOfCivilians(perceivedWeaponTypeList, civiliansWithConfirmedArmedWeaponDetails);
                logger.info(" updated civilians with perceivedWeapons:" + civiliansWithPerceivedWeaponList);
                List<Civilians> finalCiviliansWithPerceivedWeaponList = civiliansWithPerceivedWeaponList;
                return civiliansWithConfirmedArmedWeaponDetails.stream()
                        .map(civilians -> finalCiviliansWithPerceivedWeaponList.stream()
                                .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                                .findFirst().orElse(civilians))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return civiliansWithConfirmedArmedWeaponDetails;
    }

    public List<Civilians> getCiviliansWithConfirmedArmedWeapons(List<Civilians> civiliansWithForceDetails) {
        List<ConfimedArmedWeapon> confimedArmedWeapons = getConfimedWeaponTypeListForCivilian(civiliansWithForceDetails);
        List<Civilians> civiliansWithConfirmedArmedWeapons;
        if (!confimedArmedWeapons.isEmpty()) {
            civiliansWithConfirmedArmedWeapons = getUpdatedConfirmedArmedWeaponsOfCivilians(confimedArmedWeapons, civiliansWithForceDetails);
            System.out.println(" updated civilians with confirmedArmedWeapons ---- :" + civiliansWithConfirmedArmedWeapons);
            return civiliansWithConfirmedArmedWeapons.stream()
                    .map(civilians -> civiliansWithForceDetails.stream()
                            .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                            .findFirst().orElse(civilians))
                    .collect(Collectors.toList());
        }
        return civiliansWithForceDetails;
    }

    public List<Officers> getOfficersWithForceTypeDetails(List<Officers> officersWithForceLocation) {
        List<ForceType> forceTypeList = getOfficersForceTypeDetails(officersWithForceLocation);
        if (!forceTypeList.isEmpty()) {
            return getUpdateOfficersWithForceType(officersWithForceLocation, forceTypeList);
        } else {
            return officersWithForceLocation;
        }
    }

    public List<Civilians> getCiviliansWithForceTypeDetails(List<Civilians> civiliansWithForceLocation)
    {
        List<ForceType> forceTypeList = getCivilianForceTypeDetails(civiliansWithForceLocation);
        if (!forceTypeList.isEmpty()) {
            return getUpdateCiviliansWithForceType(civiliansWithForceLocation, forceTypeList);
        } else {
            return civiliansWithForceLocation;
        }
    }

    public List<Officers> getOfficersWithForceLocationDetails(List<Officers> officersWithHawaiianRaceList) {
        List<ForceLocation> forceLocationList = getOfficersForceLocationDetails(officersWithHawaiianRaceList);
        if (!forceLocationList.isEmpty()) {
            return getUpdateOfficersWithForceLocation(officersWithHawaiianRaceList, forceLocationList);
        } else {
            return officersWithHawaiianRaceList;
        }
    }

    public List<Civilians> getCiviliansWithForceLocationDetails(List<Civilians> civiliansWithFireArmDetails) {
        List<ForceLocation> forceLocationList = getCivilianForceLocationDetails(civiliansWithFireArmDetails);
        if (!forceLocationList.isEmpty()) {
            return getUpdateCiviliansWithForceLocation(civiliansWithFireArmDetails, forceLocationList);
        } else {
            return civiliansWithFireArmDetails;
        }
    }

    public List<Civilians> getCiviliansWithFireArmDetails(List<Civilians> civiliansWithRaceDetails) {
        List<FireArm> civilainFireArms = getClientFireArmListInIncident(civiliansWithRaceDetails);
        System.out.println(" civilainFireArms for given incident --- :" + civilainFireArms);
        if (!civilainFireArms.isEmpty()) {
            List<Civilians> civiliansWithFireArmList = getUpdateCiviliansWithFireArm(civiliansWithRaceDetails, civilainFireArms);
            return civiliansWithFireArmList.stream()
                    .map(civilians -> civiliansWithRaceDetails.stream()
                            .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                            .findFirst().orElse(civilians))
                    .collect(Collectors.toList());
        }
        return civiliansWithRaceDetails;
    }

    public List<ForceType> getOfficersForceTypeDetails(List<Officers> officersForceDetails) {
        List<ForceType> incidentForceTypeList = null;
        try {
            List<Integer> civilanIds = getOfficerIds(officersForceDetails);
            String inParams = civilanIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            incidentForceTypeList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_OFF_FORCE_TYPE, inParams), new ForceTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return incidentForceTypeList;
    }

    public List<ForceLocation> getOfficersForceLocationDetails(List<Officers> officersWithHawaiianRaceList) {
        List<ForceLocation> forceLocationList = null;
        try {
            List<Integer> officerIds = getOfficerIds(officersWithHawaiianRaceList);
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceLocationList = jdbcTemplate.query(String.format(incidentSql.GET_OFFICERS_FORCE_LOCATION_DETAILS, inParams), new ForceLocationRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceLocationList;
    }

    public List<ResistanceType> getResistanceTypeListForCivilian(List<Civilians> civiliansWithPerceivedWeaponList) {
        List<ResistanceType> resistanceTypeList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(civiliansWithPerceivedWeaponList);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            resistanceTypeList = jdbcTemplate.query(String.format(incidentSql.GET_CIVILIANS_RESISTANCE_TYPE, inParams), new ResistanceTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return resistanceTypeList;
    }

    public List<PerceivedWeaponType> getPerceivedWeaponTypeListForCivilian(List<Civilians> civiliansWithConfirmedArmedWeapons) {
        List<PerceivedWeaponType> perceivedWeaponTypeList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(civiliansWithConfirmedArmedWeapons);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            perceivedWeaponTypeList = jdbcTemplate.query(String.format(incidentSql.GET_CIV_PERCEIVE_WPN_TYPE, inParams), new PerceivedWeaponTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return perceivedWeaponTypeList;
    }

    public List<ConfimedArmedWeapon> getConfimedWeaponTypeListForCivilian(List<Civilians> updatedCivilinsWithForceDetails) {
        List<ConfimedArmedWeapon> confimedArmedWeapons = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(updatedCivilinsWithForceDetails);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            confimedArmedWeapons = jdbcTemplate.query(String.format(incidentSql.GET_CIV_CONF_ARMED_WPN, inParams), new ConfimedArmedWeaponRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return confimedArmedWeapons;
    }

    public List<ForceType> getCivilianForceTypeDetails(List<Civilians> civiliansWithForceLocation)
    {
        List<ForceType> forceTypeList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(civiliansWithForceLocation);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceTypeList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_CIVILIANS_FORCE_TYPE, inParams), new ForceTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceTypeList;
    }


    public List<ForceLocation> getCivilianForceLocationDetails(List<Civilians> civiliansList) {
        List<ForceLocation> forceLocationList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(civiliansList);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceLocationList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_CIVILIANS_FORCE_LOCATION, inParams), new ForceLocationRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceLocationList;
    }

    public List<FireArm> getClientFireArmListInIncident(List<Civilians> updateCivilainsWithPrimaryRace) {
        List<FireArm> fireArmList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(updateCivilainsWithPrimaryRace);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            fireArmList = jdbcTemplate.query(String.format(incidentSql.GET_CIV_FIRE_ARMED_LIST, inParams), new FireArmRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return fireArmList;
    }

    public List<Officers> getUpdatedOfficersWithInjuryTypeForIncident(List<Officers> officersList, List<InjuryType> officersInjuryType)
    {
        List<Officers>  ofcList = new ArrayList<>();
        for (Officers officers : officersList) {
            for (InjuryType injuryType : officersInjuryType) {
                if (officers.getOfficerId() == injuryType.getOfficerId()) {
                    String injType = injuryType.getInjuryType();
                    List<String> injuryTypeList = new ArrayList<>();
                    injuryTypeList.add(injType);
                    officers.setInjuryType(injuryTypeList);
                    ofcList.add(officers);
                }
            }
        }
        return officersList.stream()
                .map(officers -> ofcList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }


    public List<Civilians> getUpdatedCiviliansWithInjuryTypeForIncident(List<Civilians> civiliansList, List<InjuryType> civilianInjuryList)
    {
        List<Civilians> civList = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (InjuryType injuryType : civilianInjuryList) {
                if (civilians.getCivilianId() == injuryType.getCivilianId()) {
                    String injType = injuryType.getInjuryType();
                    List<String> injuryTypeList = new ArrayList<>();
                    injuryTypeList.add(injType);
                    civilians.setInjuryType(injuryTypeList);
                    civList.add(civilians);
                }
            }
        }
        return civiliansList.stream()
                .map(civilians -> civList.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdatedOfficersWithHawaiianRaceDetails(List<Officers> officersWithAsianRaceList, List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaceList)
    {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : officersWithAsianRaceList) {
            for (HawaiianPacificIslanderRace hawaiianPacificIslanderRace : hawaiianPacificIslanderRaceList) {
                List<String> hawaiianList = new ArrayList<>();
                if (officers.getOfficerId() == hawaiianPacificIslanderRace.getOfficerId()) {
                    String hRace = hawaiianPacificIslanderRace.getHawaiianRace();
                    hawaiianList.add(hRace);
                    officers.setHawaiianRaceType(hawaiianList);
                    officersList.add(officers);
                }
            }
        }
        return officersWithAsianRaceList.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());

    }

    public List<Civilians> getUpdateCivilainsWithHawaiianRaceList(List<Civilians> civiliansWithAsianRaceDetails, List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaceList)
    {
        List<Civilians> civiliansList1 = new ArrayList<>();
        for (Civilians civilians : civiliansWithAsianRaceDetails) {
            for (HawaiianPacificIslanderRace hawaiianPacificIslanderRace : hawaiianPacificIslanderRaceList) {
                List<String> hawaiianList = new ArrayList<>();
                if (civilians.getCivilianId() == hawaiianPacificIslanderRace.getCivilianId()) {
                    String hRace = hawaiianPacificIslanderRace.getHawaiianRace();
                    hawaiianList.add(hRace);
                    civilians.setHawaiianRaceType(hawaiianList);
                    civiliansList1.add(civilians);
                }
            }
        }
        return civiliansWithAsianRaceDetails.stream()
                .map(civilians -> civiliansList1.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdatedOffWithAsianRaceDetails(List<Officers> officersWithPrimaryRaceList, List<AsianRace> asianRaceList)
    {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : officersWithPrimaryRaceList) {
            for (AsianRace asianRace : asianRaceList) {
                List<String> asnRaceList = new ArrayList<>();
                if (officers.getOfficerId() == asianRace.getOfficerId()) {
                    String asnRace = asianRace.getAsianRace();
                    asnRaceList.add(asnRace);
                    officers.setAsianRaceType(asnRaceList);
                    officersList.add(officers);
                }
            }
        }
        return officersWithPrimaryRaceList.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCivilainsWithAsianRaceList(List<Civilians> civiliansWithPrimaryRaceDetails, List<AsianRace> asianRaceList)
    {
        List<Civilians> civiliansList1 = new ArrayList<>();
        for (Civilians civilians : civiliansWithPrimaryRaceDetails) {
            for (AsianRace asianRace : asianRaceList) {
                List<String> asnRaceList = new ArrayList<>();
                if (civilians.getCivilianId() == asianRace.getCivilianId()) {
                    String asnRace = asianRace.getAsianRace();
                    asnRaceList.add(asnRace);
                    civilians.setAsianRaceType(asnRaceList);
                    civiliansList1.add(civilians);
                }
            }
        }
        return civiliansWithPrimaryRaceDetails.stream()
                .map(civilians -> civiliansList1.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdateOfficersWithPrimaryRaceList(List<Officers> officersWithInjuryDetails, List<PrimaryRace> primaryRaceList)
    {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : officersWithInjuryDetails) {
            for (PrimaryRace primaryRace : primaryRaceList) {
                List<String> prmRaceList = new ArrayList<>();
                if (officers.getOfficerId() == primaryRace.getOfficerId()) {
                    String primRace = primaryRace.getPrimaryRace();
                    prmRaceList.add(primRace);
                    officers.setPrimaryRaceType(prmRaceList);
                    officersList.add(officers);
                }
            }
        }
        return officersWithInjuryDetails.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }


    public List<Civilians> getUpdateCivilainsWithPrimaryRaceList(List<Civilians> civiliansList, List<PrimaryRace> updatedCiviliansRaceWithCiviliansPrimaryRace) {
        List<Civilians> civiliansList1 = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (PrimaryRace primaryRace : updatedCiviliansRaceWithCiviliansPrimaryRace) {
                List<String> primaryRaceList = new ArrayList<>();
                if (civilians.getCivilianId() == primaryRace.getCivilianId()) {
                    String primRace = primaryRace.getPrimaryRace();
                    primaryRaceList.add(primRace);
                    civilians.setPrimaryRaceType(primaryRaceList);
                    civiliansList1.add(civilians);
                }
            }
        }
        return civiliansList.stream()
                .map(civilians -> civiliansList1.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdatedOfficersWithForceReason(List<ForceReason> forceReasonList, List<Officers> officersWithForceDetails) {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : officersWithForceDetails) {
            List<String> frcReasonList = new ArrayList<>();
            for (ForceReason forceReason : forceReasonList) {
                if (forceReason.getOfficerId() == officers.getOfficerId()) {
                    frcReasonList.add(forceReason.getForceReason());
                    officers.setReasonForOfficerUsedForce(frcReasonList);
                    officersList.add(officers);
                }
            }
        }
        return officersList.stream().distinct().collect(Collectors.toList());
    }

    public List<Civilians> getUpdatedCiviliansWithResistance(List<ResistanceType> resistanceTypeList, List<Civilians> civiliansWithPerceivedWeaponList) {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : civiliansWithPerceivedWeaponList) {
            List<String> resistanceList = new ArrayList<>();
            for (ResistanceType resistanceType : resistanceTypeList) {
                if (resistanceType.getCivilianId() == civilians.getCivilianId()) {
                    resistanceList.add(resistanceType.getResistanceType());
                    civilians.setResistanceType(resistanceList);
                    civiliansList.add(civilians);
                }
            }
        }
        return civiliansList.stream().distinct().collect(Collectors.toList());
    }

    public List<Civilians> getUpdatedPerceivedWeaponsOfCivilians(List<PerceivedWeaponType> perceivedWeaponTypeList, List<Civilians> civiliansWithConfirmedArmedWeapons) {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : civiliansWithConfirmedArmedWeapons) {
            List<String> weaponList = new ArrayList<>();
            for (PerceivedWeaponType perceivedWeaponType : perceivedWeaponTypeList) {
                if (perceivedWeaponType.getCivilianId() == civilians.getCivilianId()) {
                    weaponList.add(perceivedWeaponType.getPerceivedWeapon());
                    civilians.setPerceivedWeaponType(weaponList);
                    civiliansList.add(civilians);
                }
            }
        }
        return civiliansList.stream().distinct().collect(Collectors.toList());
    }

    public List<Civilians> getUpdatedConfirmedArmedWeaponsOfCivilians(List<ConfimedArmedWeapon> confimedArmedWeapons, List<Civilians> updatedCivilinsWithForceDetails) {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : updatedCivilinsWithForceDetails) {
            List<String> armedList = new ArrayList<>();
            for (ConfimedArmedWeapon confimedArmedWeapon : confimedArmedWeapons) {
                if (confimedArmedWeapon.getCivilianId() == civilians.getCivilianId()) {
                    armedList.add(confimedArmedWeapon.getConfirmedWeapon());
                    civilians.setConfirmedArmedWeapon(armedList);
                    civiliansList.add(civilians);
                }
            }
        }
        return updatedCivilinsWithForceDetails.stream()
                .map(civilians -> civiliansList.stream()
                        .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdateOfficersWithForceType(List<Officers> officersWithForceLocation, List<ForceType> forceTypeList)
    {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : officersWithForceLocation) {
            List<String> forceTypeLst = new ArrayList<>();
            for (ForceType forceType : forceTypeList) {
                if (officers.getOfficerId() == forceType.getOfficerId()) {
                    forceTypeLst.add(forceType.getForceType());
                    officers.setForceLocation(forceTypeLst);
                    officersList.add(officers);
                }
            }
        }
        return officersWithForceLocation.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId() == (officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCiviliansWithForceType(List<Civilians> civiliansWithForceLocation, List<ForceType> forceTypeList)
    {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : civiliansWithForceLocation) {
            List<String> forceTypeLst = new ArrayList<>();
            for (ForceType forceType : forceTypeList) {
                if (civilians.getCivilianId() == forceType.getCivilianId()) {
                    forceTypeLst.add(forceType.getForceType());
                    civilians.setForceLocation(forceTypeLst);
                    civiliansList.add(civilians);
                }
            }
        }
        return civiliansWithForceLocation.stream()
                .map(person -> civiliansList.stream()
                        .filter(i -> i.getCivilianId() == (person.getCivilianId()))
                        .findFirst().orElse(person))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdateOfficersWithForceLocation(List<Officers> officersWithHawaiianRaceList, List<ForceLocation> forceLocationList)
    {
        List<Officers> officersList = new ArrayList<>();
        for (Officers  officers : officersWithHawaiianRaceList) {
            List<String> forceLocList = new ArrayList<>();
            for (ForceLocation forceLocation : forceLocationList) {
                if (officers.getOfficerId() == forceLocation.getOfficerId()) {
                    forceLocList.add(forceLocation.getForceLocation());
                    officers.setForceLocation(forceLocList);
                    officersList.add(officers);
                }
            }
        }
        return officersWithHawaiianRaceList.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId() == (officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCiviliansWithForceLocation(List<Civilians> civiliansWithFireArmDetails, List<ForceLocation> forceLocationList)
    {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : civiliansWithFireArmDetails) {
            List<String> forceLocList = new ArrayList<>();
            for (ForceLocation forceLocation : forceLocationList) {
                if (civilians.getCivilianId() == forceLocation.getCivilianId()) {
                    forceLocList.add(forceLocation.getForceLocation());
                    civilians.setForceLocation(forceLocList);
                    civiliansList.add(civilians);
                }
            }
        }
        return civiliansWithFireArmDetails.stream()
                .map(person -> civiliansList.stream()
                        .filter(i -> i.getCivilianId() == (person.getCivilianId()))
                        .findFirst().orElse(person))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCiviliansWithFireArm(List<Civilians> updateCivilainsWithPrimaryRace, List<FireArm> civilainFireArms) {

        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : updateCivilainsWithPrimaryRace) {
            List<String> firArmList = new ArrayList<>();
            for (FireArm fireArm : civilainFireArms) {
                if (civilians.getCivilianId() == fireArm.getCivilianId()) {
                    firArmList.add(fireArm.getFireArmType());
                    civilians.setFireArms(firArmList);
                    civiliansList.add(civilians);
                }
            }
        }
        return updateCivilainsWithPrimaryRace.stream()
                .map(person -> civiliansList.stream()
                        .filter(i -> i.getCivilianId() == (person.getCivilianId()))
                        .findFirst().orElse(person))
                .collect(Collectors.toList());
    }

    public List<HawaiianPacificIslanderRace> getOfficersHawaiianRaceDetails(List<Officers> officersWithAsianRaceList)
    {
        List<Integer> officerIds = getOfficerIds(officersWithAsianRaceList);
        List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaces = new ArrayList<>();
        logger.info("officerids for hawaiian race:" + officerIds);
        hawaiianPacificIslanderRaces = getOffHawaiianRaceDetails(officerIds);
        logger.info("officers hawaiian race list :"+hawaiianPacificIslanderRaces);
        return hawaiianPacificIslanderRaces;
    }

    public List<HawaiianPacificIslanderRace> getCiviliansHawaiianRaceDetails(List<Civilians> civiliansWithAsianRaceDetails)
    {
        List<Integer> civilianIds = getCivilianIds(civiliansWithAsianRaceDetails);
        List<HawaiianPacificIslanderRace> hawaiianPacificIslanderRaces = new ArrayList<>();
        logger.info("civilianIds for hawaiian race:" + civilianIds);
        hawaiianPacificIslanderRaces = getCivHawaiianRaceDetails(civilianIds);
        logger.info("civilians hawaiian race list :"+hawaiianPacificIslanderRaces);
        return hawaiianPacificIslanderRaces;
    }

    public List<AsianRace> getCiviliansAsianRaceDetails(List<Civilians> civiliansList)
    {
        List<Integer> civilianIds = getCivilianIds(civiliansList);
        List<AsianRace> asianRaceList = new ArrayList<>();
        logger.info("civilianIds for asian race:" + civilianIds);
        asianRaceList = getCivAsianRaceDetails(civilianIds);
        logger.info("civilians primary race list :"+asianRaceList);
        return asianRaceList;
    }

    public List<AsianRace> getOfficersAsianRaceDetails(List<Officers> officersWithPrimaryRaceList)
    {
        List<Integer> officerIds = getOfficerIds(officersWithPrimaryRaceList);
        logger.info(" officerIds for asian race :"+officerIds);
        List<AsianRace> asianRaceList = getOffAsianRaceDetails(officerIds);
        logger.info("officers asian race details :"+asianRaceList);
        return asianRaceList;
    }


    public List<PrimaryRace> getOfficersPrimaryRaceDetails(List<Officers> officersWithInjuryDetails)
    {
        List<Integer> officerIds = getOfficerIds(officersWithInjuryDetails);
        logger.info(" officerIds for primaryrace :"+officerIds);
        List<PrimaryRace> primaryRaceList = getOffPrimaryRaceDetails(officerIds);
        logger.info("officers primary race details :"+primaryRaceList);
        return primaryRaceList;
    }


    public List<PrimaryRace> getCiviliansPrimaryRaceDetails(List<Civilians> civiliansList) {
        List<Integer> civilianIds = getCivilianIds(civiliansList);
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        logger.info("civilianIds for Race:" + civilianIds);
        primaryRaceList = getCivPrimaryRaceDetails(civilianIds);
        logger.info("civilians primary race list :"+primaryRaceList);
        return primaryRaceList;
    }

    public List<InjuryType> getCiviliansInjuryDetails(List<Civilians> civilianIdList) {
        List<Integer> civilianIds = getCivilianIds(civilianIdList);
        return getInjuryListForCiviliansInIncident(civilianIds);
    }

    public List<InjuryType> getOfficersInjuriesWithInjuryType(List<Officers> officersList) {
        List<Integer> officerIds = getOfficerIds(officersList);
        return getInjuryListForOfficersInIncident(officerIds);
    }

    public List<Integer> getOfficerIds(List<Officers> officersList) {
        List<Integer> OfficerIds = new ArrayList<>();
        for (Officers officers : officersList) {
            OfficerIds.add(officers.getOfficerId());
        }
        return OfficerIds;
    }

    public List<Integer> getCivilianIds(List<Civilians> civilianIdList) {
        List<Integer> civilianIds = new ArrayList<>();
        for (Civilians civilians : civilianIdList) {
            civilianIds.add(civilians.getCivilianId());
        }
        return civilianIds;
    }

    public List<OfficerCivilians> getEngagedOfficersForCivilians(List<Officers> officersInIncident, List<Civilians> civilianInIncident, int incidentId) {
        List<OfficerCivilians> officerCiviliansLit = new ArrayList<>();
        for (Officers officers : officersInIncident) {
            for (Civilians civilians : civilianInIncident) {
                OfficerCivilians officerCivilians = new OfficerCivilians();
                if(civilians.getEngagedOfficers().contains(officers.getOfficerOrder())) {
                    officerCivilians.setCivilianId(civilians.getCivilianId());
                    officerCivilians.setOfficerId(officers.getOfficerId());
                    officerCiviliansLit.add(officerCivilians);
                }
            }
        }
        return officerCiviliansLit;
    }

    public List<Civilians> getUpdatedCivilianForIncident(List<Civilians> civilianInIncident, List<Civilians> civiliansList) {
        List<Civilians> civilians = new ArrayList<>();
        for (Civilians civilians1 : civiliansList) {
            for (Civilians civilians2 : civilianInIncident) {
                if (civilians1.getCivilianNumber() == civilians2.getCivilianNumber()) {
                    civilians1.setCivilianId(civilians2.getCivilianId());
                    civilians.add(civilians1);
                }
            }
        }
        return civilians;
    }

    public List<Officers> getUpdatedOfficersForIncident(List<Officers> officersInIncident, List<Officers> officersList) {
        List<Officers> officers = new ArrayList<>();
        for (Officers officers1 : officersList) {
            for (Officers officers2 : officersInIncident) {
                if (officers1.getOfficerOrder() == officers2.getOfficerOrder()) {
                    officers1.setOfficerId(officers2.getOfficerId());
                    officers.add(officers1);
                }
            }
        }
        return officers;
    }

    public List<ForceReason> getForceReasonForIncident(List<Officers> officers) {
        List<ForceReason> forceReasonList = new ArrayList<>();
        for (Officers officers1 : officers) {
            if(officers1.getReasonForOfficerUsedForce()!=null) {
                for (String reason : officers1.getReasonForOfficerUsedForce()) {
                    ForceReason forceReason = new ForceReason();
                    forceReason.setForceReason(reason);
                    forceReason.setOfficerId(officers1.getOfficerId());
                    forceReasonList.add(forceReason);
                }
            }
        }
        return forceReasonList;
    }

    public List<ResistanceType> getResistanceTypeList(List<Civilians> civiliansList1) {
        List<ResistanceType> resistanceTypeList = new ArrayList<>();
        for (Civilians civilians : civiliansList1) {
            if (civilians.getResistanceType() != null) {
                for (String resistanceType : civilians.getResistanceType()) {
                    ResistanceType resistanceType1 = new ResistanceType();
                    resistanceType1.setCivilianId(civilians.getCivilianId());
                    resistanceType1.setResistanceType(resistanceType);
                    resistanceTypeList.add(resistanceType1);
                }
            }
        }
        return resistanceTypeList;
    }


    public List<ConfimedArmedWeapon> getConfimedWeaponTypeList(List<Civilians> civiliansList1) {
        List<ConfimedArmedWeapon> confimedArmedWeaponArrayList = new ArrayList<>();
        for (Civilians civilians : civiliansList1) {
            if (civilians.getConfirmedArmedWeapon() != null) {
                for (String weaponType : civilians.getConfirmedArmedWeapon()) {
                    ConfimedArmedWeapon confimedArmedWeapon = new ConfimedArmedWeapon();
                    confimedArmedWeapon.setCivilianId(civilians.getCivilianId());
                    confimedArmedWeapon.setConfirmedWeapon(weaponType);
                    confimedArmedWeaponArrayList.add(confimedArmedWeapon);
                }
            }
        }
        return confimedArmedWeaponArrayList;
    }


    public List<PerceivedWeaponType> getPerceivedWeaponTypeList(List<Civilians> civiliansList1) {
        List<PerceivedWeaponType> perceivedWeaponTypeList = new ArrayList<>();
        for (Civilians civilians : civiliansList1) {
            if (civilians.getResistanceType() != null) {
                for (String weaponType : civilians.getPerceivedWeaponType()) {
                    PerceivedWeaponType perceivedWeaponType = new PerceivedWeaponType();
                    perceivedWeaponType.setCivilianId(civilians.getCivilianId());
                    perceivedWeaponType.setPerceivedWeapon(weaponType);
                    perceivedWeaponTypeList.add(perceivedWeaponType);
                }
            }
        }
        return perceivedWeaponTypeList;
    }

    public List<InjuryType> getOfficersInjuryTypeList(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<InjuryType>  injuryTypeList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getInjuryType() != null)) {
                    for(String injType : officers.getInjuryType())
                    {
                        InjuryType injuryType = new InjuryType();
                        injuryType.setInjuryType(injType);
                        injuryType.setOfficerId(officers.getOfficerId());
                        injuryType.setInjutyTypeOn("O");
                        injuryType.setCivilianId(offCiv.getCivilianId());
                        injuryTypeList.add(injuryType);
                    }

                }
            }
        }
        return injuryTypeList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceType> getForceTypeDetailsOfOfficers(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<ForceType> forceTypeList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getForceLocation() != null)) {
                    for(String forType : officers.getForceLocation())
                    {
                        ForceType forceType = new ForceType();
                        forceType.setForceType(forType);
                        forceType.setOfficerId(officers.getOfficerId());
                        forceType.setForceOn("O");
                        forceType.setCivilianId(offCiv.getCivilianId());
                        forceTypeList.add(forceType);
                    }

                }
            }
        }
        //return forceLocationList;
        return forceTypeList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceLocation> getForceLocationDetailsOfOfficers(List<OfficerCivilians> officerCivilians, List<Officers> officersList)
    {
        List<ForceLocation> forceLocationList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers : officersList) {
                if ((officers.getOfficerId() == offCiv.getOfficerId()) && (officers.getForceLocation() != null)) {
                    for(String forceLoc : officers.getForceLocation())
                    {
                        ForceLocation forceLocation = new ForceLocation();
                        forceLocation.setForceLocation(forceLoc);
                        forceLocation.setOfficerId(officers.getOfficerId());
                        forceLocation.setForceOn("O");
                        forceLocation.setCivilianId(offCiv.getCivilianId());
                        forceLocationList.add(forceLocation);
                    }

                }
            }
        }
        //return forceLocationList;
        return forceLocationList.stream().distinct().collect(Collectors.toList());
    }

    public List<InjuryType>  getCiviliansInjuryTypeList(List<OfficerCivilians> officerCivilians, List<Civilians> civilianInIncident)
    {
        List<InjuryType> injuryTypeList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civilianInIncident) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getInjuryType() != null)) {
                    for(String injType : civilians.getInjuryType())
                    {
                        InjuryType injuryType = new InjuryType();
                        injuryType.setCivilianId(civilians.getCivilianId());
                        injuryType.setInjutyTypeOn("C");
                        injuryType.setOfficerId(offCiv.getOfficerId());
                        injuryType.setInjuryType(injType);
                        injuryTypeList.add(injuryType);
                    }

                }
            }
        }
        return injuryTypeList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceType> getForceTypeDetailsOfCivilians(List<OfficerCivilians> officerCivilians, List<Civilians> civilianInIncident)
    {
        List<ForceType>  forceTypeList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civilianInIncident) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getForceType() != null)) {
                    for(String forType : civilians.getForceType())
                    {
                        ForceType forceType = new ForceType();
                        forceType.setCivilianId(civilians.getCivilianId());
                        forceType.setForceOn("C");
                        forceType.setOfficerId(offCiv.getOfficerId());
                        forceType.setForceType(forType);
                        forceTypeList.add(forceType);
                    }

                }
            }
        }
        //return forceTypeList;
        return forceTypeList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceLocation> getForceLocationDetailsOfCivilians(List<OfficerCivilians> officerCivilians, List<Civilians> civilianInIncident)
    {
        List<ForceLocation> forceLocationList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civilianInIncident) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getForceLocation() != null)) {
                    for(String forceLoc : civilians.getForceLocation())
                    {
                        ForceLocation forceLocation = new ForceLocation();
                        forceLocation.setCivilianId(civilians.getCivilianId());
                        forceLocation.setForceOn("C");
                        forceLocation.setOfficerId(offCiv.getOfficerId());
                        forceLocation.setForceLocation(forceLoc);
                        forceLocationList.add(forceLocation);
                    }

                }
            }
        }
        //return forceLocationList;
       return forceLocationList.stream().distinct().collect(Collectors.toList());
    }

    public List<FireArm> getFireArmListInIncident(List<Civilians> civiliansList1) {
        List<FireArm> fireArmList = new ArrayList<>();
        for (Civilians civilians : civiliansList1) {
            if (civilians.getFireArms() != null) {
                for (String fireArm : civilians.getFireArms()) {
                    FireArm fireArm1 = new FireArm();
                    fireArm1.setFireArmType(fireArm);
                    fireArm1.setCivilianId(civilians.getCivilianId());
                    fireArmList.add(fireArm1);
                }
            }
        }
        return fireArmList;
    }


    public List<Address> getIncidentAddressDetails(int incidentId) {
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = jdbcTemplate.query(
                    incidentSql.GET_INCIDENT_LOCATION, new Object[]{incidentId},
                    new IncidentLocationRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return addressList;
    }

    public Incident getIncidentBasicDetails(int incidentId) {
        Incident incident = new Incident();
        try {
            incident = (Incident) jdbcTemplate.queryForObject(incidentSql.GET_INCIDENT_CORE_DETAILS, new Object[]{incidentId}, new IncidentRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return incident;
    }

    public List<Officers> getOfficrsForIncident(int incidentId) {
        List<Officers> officersList = new ArrayList<>();
        try {
            officersList = jdbcTemplate.query(
                    incidentSql.GET_INCIDENT_OFFICERS, new Object[]{incidentId},
                    new OfficersRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return officersList;
    }

    public List<Officers> getOfficers(int incidentId) {
        List<Officers> officersWithCivilians = new ArrayList<>();
        try {
            List<Officers> officersList = getOfficrsForIncident(incidentId);
            List<Integer> officersIds = getOfficerIds(officersList);
            List<OfficerCivilians> officerCivilians = getOfficersCiviliansIdsForIncident(officersIds);
            officersWithCivilians = getOfficersWithCivilians(officersList, officerCivilians);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return officersWithCivilians;
    }

    public List<Civilians> getCivilians(int incidentId) {
        List<Civilians> civiliansWithOfficers = new ArrayList<>();
        try {
            List<Civilians> civiliansList = getCivilianForIncident(incidentId);
            logger.info("civilianslist for given incidentId :"+civiliansList);
            List<Integer> civilianIds = getCivilianIds(civiliansList);
            logger.info("civilianIds for given IncidentId :"+civilianIds);
            List<OfficerCivilians> officerCivilians = getCiviliansOfficersIdsForIncident(civilianIds);
            logger.info("officercivilians for given incident ID :"+officerCivilians);
            civiliansWithOfficers = getCiviliansWithOfficers(civiliansList, officerCivilians);
            logger.info("civiliansWithOfficers for given incidentId:"+civiliansWithOfficers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return civiliansWithOfficers;
    }


    public List<Civilians> getCivilianForIncident(int incidentId) {
        List<Civilians> civiliansList = new ArrayList<>();
        try {
            civiliansList = jdbcTemplate.query(
                    incidentSql.GET_INCIDENT_CIV, new Object[]{incidentId},
                    new CivilianRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return civiliansList;
    }

    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }


    public List<InjuryType> getInjuryListForOfficersInIncident(List<Integer> officerIds)
    {
        List<InjuryType> injuryTypeDetails = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryTypeDetails = jdbcTemplate.query(String.format(incidentSql.GET_OFF_INJURY_TYPE, inParams), new InjuryTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryTypeDetails;
    }

    public List<InjuryType> getInjuryListForCiviliansInIncident(List<Integer> offCivList) {
        List<InjuryType> injuryTypeDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryTypeDetails = jdbcTemplate.query(String.format(incidentSql.GET_CIV_INJURY_LIST, inParams), new InjuryTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryTypeDetails;
    }

    public List<HawaiianPacificIslanderRace> getOffHawaiianRaceDetails(List<Integer> officerIds)
    {
        List<HawaiianPacificIslanderRace> hawaiianRaceList = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            hawaiianRaceList = jdbcTemplate.query(String.format(incidentSql.GET_OFF_HAWAIIAN_REACE_DETAILS, inParams), new HawaiianRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return hawaiianRaceList;
    }

    public List<HawaiianPacificIslanderRace> getCivHawaiianRaceDetails(List<Integer> civilianRaceIds) {
        List<HawaiianPacificIslanderRace> hawaiianRaceList = new ArrayList<>();
        try {
            String inParams = civilianRaceIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            hawaiianRaceList = jdbcTemplate.query(String.format(incidentSql.GET_HAWAIIAN_REACE_DETAILS, inParams), new HawaiianRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return hawaiianRaceList;
    }

    public List<AsianRace> getCivAsianRaceDetails(List<Integer> civilianRaceIds) {
        List<AsianRace> asianRaceList = new ArrayList<>();
        try {
            String inParams = civilianRaceIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            asianRaceList = jdbcTemplate.query(String.format(incidentSql.GET_ASIAN_RACE_DETAILS, inParams), new AsianRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return asianRaceList;
    }

    public List<AsianRace> getOffAsianRaceDetails(List<Integer> officerIds)
    {
        List<AsianRace> asianRaceList = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            asianRaceList = jdbcTemplate.query(String.format(incidentSql.GET_OFF_ASIAN_RACE_DETAILS, inParams), new AsianRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return asianRaceList;
    }

    public List<PrimaryRace> getOffPrimaryRaceDetails(List<Integer> officerIds)
    {
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            primaryRaceList = jdbcTemplate.query(String.format(incidentSql.GET_OFF_PRIMARY_RACE_DETAILS, inParams), new PrimaryRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return primaryRaceList;
    }

    public List<PrimaryRace> getCivPrimaryRaceDetails(List<Integer> civilianRaceIds) {
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        try {
            String inParams = civilianRaceIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            primaryRaceList = jdbcTemplate.query(String.format(incidentSql.GET_PRIMARY_RACE_DETAILS, inParams), new PrimaryRaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return primaryRaceList;
    }

    public List<InjuryType> getOfficersInjuryType(List<Integer> injuryIds) {
        List<InjuryType> injuryTypeDetails = new ArrayList<>();
        try {
            String inParams = injuryIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryTypeDetails = jdbcTemplate.query(String.format(incidentSql.GET_OFF_INJURY_TYPE, inParams), new InjuryTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryTypeDetails;
    }

    public List<Integer> getUpdatedForceDetails(List<OfficerCivilians> officerCivilians) {
        Set<Integer> offCivSet = new HashSet<>();
        for (OfficerCivilians forceDetailsList : officerCivilians) {
            offCivSet.add(forceDetailsList.getCivilianId());
            offCivSet.add(forceDetailsList.getOfficerId());
        }
        return offCivSet.stream().collect(Collectors.toList());
    }

    public List<OfficerCivilians> getCiviliansOfficersIdsForIncident(List<Integer> civilianIds) {
        List<OfficerCivilians> officerCivilians = new ArrayList<>();
        try {
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            officerCivilians = jdbcTemplate.query(String.format(incidentSql.GET_OFF_CIV_FOR_CIV, inParams), new OfficerCiviliansRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return officerCivilians;
    }

    public List<OfficerCivilians> getOfficersCiviliansIdsForIncident(List<Integer> officerIds) {
        List<OfficerCivilians> officerCivilians = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            officerCivilians  = jdbcTemplate.query(String.format(incidentSql.GET_OFF_CIV_FOR_OFF, inParams), new OfficerCiviliansRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return officerCivilians;
    }


    public int[] submittedForceTypeForIncident(List<ForceType> forceTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_FORCE_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, forceTypeList.get(i).getForceType());
                preparedStatement.setInt(2, forceTypeList.get(i).getCivilianId());
                preparedStatement.setInt(3, forceTypeList.get(i).getOfficerId());
                preparedStatement.setString(4, forceTypeList.get(i).getForceOn());
            }
            @Override
            public int getBatchSize() {
                return forceTypeList.size();
            }
        });
    }

    public int[] submittHawaiianRaceDetailsOfIncident(List<HawaiianPacificIslanderRace> incidentHawaiianRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_HAWAIIAN_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentHawaiianRaceDetails.get(i).getHawaiianRace());
                preparedStatement.setInt(2, incidentHawaiianRaceDetails.get(i).getCivilianId());
                preparedStatement.setInt(3, incidentHawaiianRaceDetails.get(i).getOfficerId());
                preparedStatement.setString(4, incidentHawaiianRaceDetails.get(i).getHawaiianRaceOf());
            }
            @Override
            public int getBatchSize() {
                return incidentHawaiianRaceDetails.size();
            }
        });
    }

    public int[] submitInjuryTypeDetailsOfIncident(List<InjuryType> injuryTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_INJURY_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, injuryTypeList.get(i).getInjuryType());
                preparedStatement.setInt(2, injuryTypeList.get(i).getCivilianId());
                preparedStatement.setInt(3, injuryTypeList.get(i).getOfficerId());
                preparedStatement.setString(4, injuryTypeList.get(i).getInjutyTypeOn());
            }
            @Override
            public int getBatchSize() {
                return injuryTypeList.size();
            }
        });
    }

    public int[] submittAsianRaceDetailsOfIncident(List<AsianRace> incidentAsianRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_ASIAN_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentAsianRaceDetails.get(i).getAsianRace());
                preparedStatement.setInt(2, incidentAsianRaceDetails.get(i).getCivilianId());
                preparedStatement.setInt(3, incidentAsianRaceDetails.get(i).getOfficerId());
                preparedStatement.setString(4, incidentAsianRaceDetails.get(i).getAsianRaceOf());
            }
            @Override
            public int getBatchSize() {
                return incidentAsianRaceDetails.size();
            }
        });
    }

    public int[] submitResistanceTypeList(List<ResistanceType> resistanceTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_RESISTANCE_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, resistanceTypeList.get(i).getResistanceType());
                preparedStatement.setInt(2, resistanceTypeList.get(i).getCivilianId());
            }
            @Override
            public int getBatchSize() {
                return resistanceTypeList.size();
            }
        });
    }

    public int[] submitConfirmedWeaponTypeList(List<ConfimedArmedWeapon> confirmedWeaponTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_CONFIRMED_ARMED_WEAPON_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, confirmedWeaponTypeList.get(i).getConfirmedWeapon());
                preparedStatement.setInt(2, confirmedWeaponTypeList.get(i).getCivilianId());
            }
            @Override
            public int getBatchSize() {
                return confirmedWeaponTypeList.size();
            }
        });
    }


    public int[] submitperceivedWeaponTypeList(List<PerceivedWeaponType> perceivedWeaponTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_PERCEIVED_WEAPON_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, perceivedWeaponTypeList.get(i).getPerceivedWeapon());
                preparedStatement.setInt(2, perceivedWeaponTypeList.get(i).getCivilianId());
            }
            @Override
            public int getBatchSize() {
                return perceivedWeaponTypeList.size();
            }
        });
    }

    public int[] submittPrimaryRaceDetailsOfIncident(List<PrimaryRace> incidentPrimaryRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_PRIMARY_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentPrimaryRaceDetails.get(i).getPrimaryRace());
                preparedStatement.setInt(2, incidentPrimaryRaceDetails.get(i).getCivilianId());
                preparedStatement.setInt(3, incidentPrimaryRaceDetails.get(i).getOfficerId());
                preparedStatement.setString(4, incidentPrimaryRaceDetails.get(i).getPrimaryRaceOf());
            }
            @Override
            public int getBatchSize() {
                return incidentPrimaryRaceDetails.size();
            }
        });
    }

    public int[] submitIncidentForceLocationDetails(List<ForceLocation> forceLocationList)
    {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_FORCE_LOCATION_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, forceLocationList.get(i).getForceLocation());
                preparedStatement.setInt(2, forceLocationList.get(i).getCivilianId());
                preparedStatement.setInt(3, forceLocationList.get(i).getOfficerId());
                preparedStatement.setString(4, forceLocationList.get(i).getForceOn());
            }
            @Override
            public int getBatchSize() {
                return forceLocationList.size();
            }
        });
    }


    public int[] submitOfficersCiviliansIds(List<OfficerCivilians> officerCivilians) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_OFFICER_CIVILIAN_IDS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, officerCivilians.get(i).getCivilianId());
                preparedStatement.setInt(2, officerCivilians.get(i).getOfficerId());
            }
            @Override
            public int getBatchSize() {
                return officerCivilians.size();
            }
        });
    }

    public int[] submitForceReasonInIncident(List<ForceReason> forceReasonList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_FORCE_REASON_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, forceReasonList.get(i).getForceReason());
                preparedStatement.setInt(2, forceReasonList.get(i).getOfficerId());
            }
            @Override
            public int getBatchSize() {
                return forceReasonList.size();
            }
        });
    }

    public int[] submitFireArmsUsedInIncident(List<FireArm> fireArmListInIncident) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_FIRMARM_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, fireArmListInIncident.get(i).getFireArmType());
                preparedStatement.setInt(2, fireArmListInIncident.get(i).getCivilianId());
            }
            @Override
            public int getBatchSize() {
                return fireArmListInIncident.size();
            }
        });
    }

    public int[] submitOfficersInvolvedInIncident(List<Officers> officersList, int incidentId) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_OFFICERS_IN_INCIDENT, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, officersList.get(i).getOfficerOrder());
                ps.setString(2, officersList.get(i).isOfficerUsedForce() ? "Y" : "N");
                ps.setInt(3, officersList.get(i).getAge());
                ps.setString(4, officersList.get(i).getGender().name());
                ps.setString(5, officersList.get(i).isInjured() ? "Y" : "N");
                ps.setString(6, officersList.get(i).isInjuryFromPreExistingCondition() ? "Y" : "N");
                ps.setString(7, officersList.get(i).isOfficerAssaulted() ? "Y" : "N");
                ps.setString(8, officersList.get(i).isOnDuty() ? "Y" : "N");
                ps.setString(9, officersList.get(i).getDress());
                ps.setDate(10, new java.sql.Date(System.currentTimeMillis()));
                ps.setInt(11, incidentId);
                ps.setString(12, officersList.get(i).getAgency());
            }
            public int getBatchSize() {
                return officersList.size();
            }
        });

    }

    public int[] submitCiviliansInvolvedInIncident(List<Civilians> civiliansList, int incidentId) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_CIVILIANS_IN_INCIDENT, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, civiliansList.get(i).getCivilianNumber());
                ps.setInt(2, civiliansList.get(i).getAge());
                ps.setString(3, civiliansList.get(i).getCustodyStatus());
                ps.setString(4, civiliansList.get(i).getHighestCharge()); //need to check with Jay chargetype is Highest charge
                ps.setString(5, civiliansList.get(i).getGender().name());
                ps.setString(6, civiliansList.get(i).getRace()); // this is different
                ps.setString(7, civiliansList.get(i).isInjured() ? "Y" : "N");
                ps.setString(8, civiliansList.get(i).isInjuryFromPreExisting() ? "Y" : "N");
                ps.setString(9, civiliansList.get(i).isAssaultedOfficer() ? "Y" : "N");
                ps.setString(10, civiliansList.get(i).getMentalStatus());
                ps.setString(11, civiliansList.get(i).isResisted() ? "Y" : "N");
                ps.setString(12, civiliansList.get(i).isPerceivedArmed() ? "Y" : "N");
                ps.setString(13, civiliansList.get(i).isConfirmedArmed() ? "Y" : "N");
                ps.setString(14, civiliansList.get(i).isReceivedForce() ? "Y" : "N");
                ps.setDate(15, new java.sql.Date(System.currentTimeMillis()));
                ps.setInt(16, incidentId);
            }
            public int getBatchSize() {
                return civiliansList.size();
            }
        });
    }

    public int[] submitIncidentAddress(List<Address> incidentLocations, int incidentId) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_ADDRESS, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, incidentLocations.get(i).getIncidentAddressSequence());
                ps.setString(2, incidentLocations.get(i).getIsOnK12Campus());
                ps.setString(3, incidentLocations.get(i).getStreetName());
                ps.setString(4, incidentLocations.get(i).getCity());
                ps.setString(5, incidentLocations.get(i).getState());
                ps.setString(6, incidentLocations.get(i).getCounty());
                ps.setString(7, incidentLocations.get(i).getZip());
                ps.setDouble(8, incidentLocations.get(i).getLatitude());
                ps.setDouble(9, incidentLocations.get(i).getLongitude());
                ps.setString(10, incidentLocations.get(i).getLocationDetails());
                ps.setDate(11, new java.sql.Date(System.currentTimeMillis()));
                ps.setInt(12, incidentId);
            }
            public int getBatchSize() {
                return incidentLocations.size();
            }
        });
    }
}
