package com.doj.ursus.impl;

import com.doj.ursus.dao.IncidentDao;
import com.doj.ursus.model.*;
import com.doj.ursus.util.IncidentSql;
import org.glassfish.jersey.internal.guava.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class IncidentDaoImpl implements IncidentDao {

    private final Logger logger = LoggerFactory.getLogger(IncidentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    IncidentSql incidentSql;



    @Override
    @Transactional
    public IncidentCoreDetails createIncident(IncidentCoreDetails details) {
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
                //ps.setTime(12, new java.sql.Time(new java.util.Date().getTime()));
                return ps;
            }
        }, holder);

        //int newUserId = holder.getKey().intValue();
        int incidentId = (int) holder.getKeys().get("incident_id");
        logger.info("incident id:" + incidentId);
        details.setIncidentId(incidentId);
        List<IncidentLocation> incidentLocations = details.getIncidentAddressDetails().getIncidentLocations();
        int[] countOfIncidentLocations = submitIncidentAddress(incidentLocations, incidentId);
        logger.info("total locations inserted in table :" + countOfIncidentLocations.length);
        IncidentDemographics incidentDemographics = details.getIncidentDemographics();
        List<Civilians> civiliansList = incidentDemographics.getCiviliansList();
        List<Officers> officersList = incidentDemographics.getOfficersList();
        logger.info("officers list:" + officersList);
        logger.info("civilains list:" + civiliansList);
        int[] countOfCiviliansInIncident = submitCiviliansInvolvedInIncident(civiliansList, incidentId);
        logger.info("total civilians inserted in table :" + countOfCiviliansInIncident.length);
        List<Civilians> civilianInIncident = getCivilianForIncident(incidentId);
        logger.info("civilianInIncident for given IncidentId :" + civilianInIncident);
        List<Civilians> updtciviliansList = getUpdatedCivilianForIncident(civilianInIncident, civiliansList);
        logger.info("Civilinas list afterinserting civlian table:" + updtciviliansList);
        int[] countOfOfficersInIncident = submitOfficersInvolvedInIncident(officersList, incidentId);
        logger.info("total officers inserted in table:" + countOfOfficersInIncident.length);
        List<Officers> officersInIncident = getOfficrsForIncident(incidentId);
        logger.info("officersInIncident for given IncidentId :" + officersInIncident.toString());
        List<Integer> officersId = getOfficersIdForIncident(incidentId);
        logger.info("officersId involved in Incident :" + officersId);
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
        int[] forceDetailsInserted = insertForceDetails(officerCivilians, updtciviliansList, officers, offCivList);
        logger.info("total forceDetails Inserted in to table:" + forceDetailsInserted.length);
        int[] countOfOffCivliansInjuryDetailsInserted = insertInjuryDetails(officerCivilians, updtciviliansList, officers, offCivList);
        logger.info("total Injury details inserted in to table:" + countOfOffCivliansInjuryDetailsInserted);
        int[] countOfOffCivliansRaceDetailsInserted = insertIncidentRaceDetails(officerCivilians, updtciviliansList, officers);
        logger.info("total race details inserted in to table:" + countOfOffCivliansRaceDetailsInserted.length);
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
    public IncidentCoreDetails getIncidentDetails(int incidentId) {
        logger.info("given IncidentId :" + incidentId);
        IncidentCoreDetails incidentCoreDetails = getIncidentCoreDetails(incidentId);
        IncidentDemographics incidentDemographics = new IncidentDemographics();
        IncidentAddressDetails incidentAddressDetails = new IncidentAddressDetails();

        List<Civilians> civiliansList = getCivilians(incidentId);
        logger.info("civiliansList:"+civiliansList);
        List<Civilians> civiliansWithInjuryDetails = getCiviliansWithInjuryDetails(civiliansList);
        logger.info("civiliansWithInjuryDetails:"+civiliansWithInjuryDetails);
        List<Civilians> civiliansWithRaceDetails = getUpdateCivilainDetailsWithCivilianRaceDetails(civiliansWithInjuryDetails);
        logger.info("civiliansWithRaceDetails"+civiliansWithRaceDetails);
        List<Civilians> civiliansWithFireArmDetails = getCiviliansWithFireArmDetails(civiliansWithRaceDetails);
        logger.info("civiliansWithFireArmDetails:"+civiliansWithFireArmDetails);
        List<Civilians> civiliansWithForceDetails = getCiviliansWithForceDetails(civiliansWithFireArmDetails);
        logger.info("civiliansWithForceDetails:"+civiliansWithForceDetails);
        List<Civilians> civiliansWithConfirmedArmedWeaponDetails = getCiviliansWithConfirmedArmedWeapons(civiliansWithForceDetails);
        logger.info("civiliansWithConfirmedArmedWeaponDetails:"+civiliansWithConfirmedArmedWeaponDetails);
        List<Civilians> civiliansWithPerceivedWeaponTypeDetails = getCiviliansWithPerceivedWeaponTypeDetails(civiliansWithConfirmedArmedWeaponDetails);
        logger.info("civiliansWithPerceivedWeaponTypeDetails:"+civiliansWithPerceivedWeaponTypeDetails);
        List<Civilians> civiliansWithResistanceTypeDetails = getCiviliansWithResistanceTypeDetails(civiliansWithPerceivedWeaponTypeDetails);
        logger.info("civiliansWithResistanceTypeDetails:"+civiliansWithResistanceTypeDetails);

        List<Officers> officersList = getOfficers(incidentId);
        logger.info("officersList:"+officersList);
        List<Officers> officersWithInjuryDetails = getOfficersWithInjuryDetails(officersList);
        logger.info("officersWithInjuryDetails:"+officersWithInjuryDetails);
        List<Officers> officersWithRaceDetails = getUpdateOfficersDetailsWithOfficerRaceDetails(officersWithInjuryDetails);
        logger.info("officersWithRaceDetails:"+officersWithRaceDetails);
        List<Officers> officersWithForceDetails = getOfficersWithForceDetails(officersWithRaceDetails);
        logger.info("officersWithForceDetails"+officersWithForceDetails);
        List<Officers> officersWithForceReasonDetails = getOfficersWithForceReasonDetails(officersWithForceDetails);
        logger.info("officersWithForceReasonDetails:"+officersWithForceReasonDetails);

        /*
        List<Civilians> updatedCivilians = Stream.concat(civiliansList.stream(), civiliansWithPerceivedWeaponList.stream())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(" updated civilains ------------"+updatedCivilians);
        */
        /*
        List<Civilians> newList = civiliansWithResistance.stream()
                .map(person -> civiliansWithPerceivedWeaponList.stream()                                       // map Person to
                        .filter(i -> i.getCivilianId()==(person.getCivilianId()))  // .. the found Id
                        .findFirst().orElse(person))                    // .. or else to self
                .collect(Collectors.toList());
        System.out.println(" newList------------"+newList);
        */
        List<IncidentLocation> incidentLocations = getIncidentLocationListDetails(incidentId);
        logger.info("incidentLocationDetails:"+incidentLocations);
        incidentAddressDetails.setIncidentLocations(incidentLocations);
        incidentDemographics.setOfficersList(officersWithForceReasonDetails);
        incidentDemographics.setCiviliansList(civiliansWithResistanceTypeDetails);
        incidentCoreDetails.setIncidentAddressDetails(incidentAddressDetails);
        incidentCoreDetails.setIncidentDemographics(incidentDemographics);
        return incidentCoreDetails;
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

    public int[] insertForceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList, List<Integer> offCivList) {
        List<ForceDetails> forceDetailsOfCiviliansList = getForceDetailsOfCiviliansInIncident(officerCivilians, civiliansList1);
        logger.info("forcedetails of civilains:" + forceDetailsOfCiviliansList);
        List<ForceDetails> forceDetailsOfOfficersList = getForceDetailsOfOfficersInIncident(officerCivilians, officersList);
        logger.info(" forcedetails of officers:" + forceDetailsOfOfficersList);
        forceDetailsOfCiviliansList.addAll(forceDetailsOfOfficersList);
        int[] countOfIncidentForceSubmitted = submitIncidentForceDetails(forceDetailsOfCiviliansList);
        logger.info("total forcedetails inserted in to table:"+countOfIncidentForceSubmitted.length);
        List<ForceDetails> forceDetailsList = getUpdatedForceDetailsWithForceId(offCivList);
        logger.info("forcedetailslist with forceid's:" + forceDetailsList);
        List<ForceDetails> civilainForceDetails = getUpdatedCivilianForceDetails(forceDetailsList, forceDetailsOfCiviliansList);
        logger.info("updated civilainForceDetails:" + civilainForceDetails);

        List<IncidentForceLocation> civiliansForceLocationDetails = getIncidentCiviliansForceDetails(civilainForceDetails);
        logger.info("civiliansForceLocationDetails---:"+civiliansForceLocationDetails);
        List<IncidentForceLocation> officersForceLocationDetails = getIncidentOfficersForceDetails(civilainForceDetails);
        logger.info("officersForceLocationDetails---:"+officersForceLocationDetails);
        civiliansForceLocationDetails.addAll(officersForceLocationDetails);
        logger.info("all incident forlocation details :"+civiliansForceLocationDetails);
        int[] countOfIncidentForceLocationsSubmitted = submittedForceLocationsForIncident(civiliansForceLocationDetails);
        logger.info("total forcelocations inserted in to table:" + countOfIncidentForceLocationsSubmitted.length);

        //List<IncidentForceLocation> incidentForceLocationList = getIncidentForceLocationList(civilainForceDetails);
        //System.out.println(" all incident forlocation details --- :" + incidentForceLocationList);
        //int[] countOfIncidentForceLocationsSubmitted = submittedForceLocationsForIncident(incidentForceLocationList);

        List<IncidentForceType> civiliansForceTypeList = getIncidentCiviliansForceTypeDetails(civilainForceDetails);
        logger.info("civiliansForceTypeList:"+civiliansForceTypeList);
        List<IncidentForceType> officersForceTypeList = getIncidentOfficersForceTypeDetails(civilainForceDetails);
        logger.info("officersForceTypeList:"+officersForceTypeList);
        civiliansForceTypeList.addAll(officersForceTypeList);
        logger.info("all incident forcetype details:"+civiliansForceTypeList);
        int[] countOfIncidentForceTypeSubmitted = submittedForceTypeForIncident(civiliansForceTypeList);
        logger.info(" total forcetype inserted in to table:" + countOfIncidentForceTypeSubmitted.length);

        //List<IncidentForceType> incidentForceTypeList = getIncidentForceTypeList(civilainForceDetails);
        //System.out.println(" all incident forcetype details --- :" + incidentForceTypeList);
        //int[] countOfIncidentForceTypeSubmitted = submittedForceTypeForIncident(incidentForceTypeList);

        return countOfIncidentForceSubmitted;
    }

    public int[] insertInjuryDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList, List<Integer> offCivList) {
        List<Injury> civilainsInjuryDetails = getCiviliansInjuryList(officerCivilians, civiliansList1);
        logger.info("injurydetails for civilains in incident:" + civilainsInjuryDetails);
        List<Injury> officersInjuryDetails = getOfficersInjuryList(officerCivilians, officersList);
        logger.info("injurydetails for officers in incident:" + officersInjuryDetails);
        civilainsInjuryDetails.addAll(officersInjuryDetails);
        logger.info("total injuries details:"+civilainsInjuryDetails);
        int[] countOfOffCivliansInjuryDetailsSubmitted = submitIncidentInjuryDetails(civilainsInjuryDetails);
        logger.info("total Injuries inserted in to table:"+ countOfOffCivliansInjuryDetailsSubmitted.length);
        List<Injury> injuryList = getInjuryListWithInjuryId(offCivList);
        logger.info("injurydetails with injuryId's:" + injuryList);
        List<Injury> updatedInjuryDetails = getUpdatedInjuryDetails(injuryList, civilainsInjuryDetails);
        logger.info("updated injurydetails:" + updatedInjuryDetails);
        //injury type details
        List<InjuryType> incidentInjuryTypeDetails = getIncidentInjuryTypeList(updatedInjuryDetails);
        logger.info("all incident injury type details:" + incidentInjuryTypeDetails);
        int[] countOfInjuryTypeDetailsSubmitted = submittInjuryTypeDetailsOfIncident(incidentInjuryTypeDetails);
        logger.info("total injurytype details inserted in table:" + countOfInjuryTypeDetailsSubmitted.length);
        return countOfOffCivliansInjuryDetailsSubmitted;
    }

    public int[] insertIncidentRaceDetails(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1, List<Officers> officersList) {
        List<Race> civilainsRaceDetails = getCiviliansRaceList(officerCivilians, civiliansList1);
        logger.info("raceDetails of civilains in incident:" + civilainsRaceDetails);
        List<Race> officersRaceDetails = getOfficersRaceDetailsOfIncident(officerCivilians, officersList);
        logger.info("racedetails of Officers in incident:" + officersRaceDetails);
        civilainsRaceDetails.addAll(officersRaceDetails);
        logger.info("total race details in incident:" + civilainsRaceDetails);
        int[] countOfOffCivliansRaceDetailsSubmitted = submitIncidentRaceDetails(civilainsRaceDetails);
        logger.info(" total race details inserted in table:" +countOfOffCivliansRaceDetailsSubmitted.length);
        List<Integer> offCivList = getUpdatedForceDetails(officerCivilians);
        logger.info("officer civlians id's involved in incident:" + offCivList);
        List<Race> raceList = getUpdatedRaceDetailsWithRaceId(offCivList);
        logger.info("racedetails with raceid's:" + raceList);
        List<Race> updatedRaceDetails = getUpdatedRaceDetails(raceList, civilainsRaceDetails);
        logger.info("updated racedetails:" + updatedRaceDetails);
        //primary race details
        List<PrimaryRace> incidentPrimaryRaceDetails = getIncidentPrimayRaceList(updatedRaceDetails);
        logger.info("all incident primaryRace details:" + incidentPrimaryRaceDetails);
        int[] countOfPrimaryRaceDetailsSubmitted = submittPrimaryRaceDetailsOfIncident(incidentPrimaryRaceDetails);
        logger.info("total primaryRaceDetails inserted in table:" + countOfPrimaryRaceDetailsSubmitted);
        //asian race details
        List<AsianRace> incidentAsianRaceDetails = getIncidentAsianRaceList(updatedRaceDetails);
        if (!incidentAsianRaceDetails.isEmpty()) {
            logger.info("all incident asianRace details:" + incidentAsianRaceDetails);
            int[] countOfAsianRaceDetailsSubmitted = submittAsianRaceDetailsOfIncident(incidentAsianRaceDetails);
            logger.info("total asianRaceDetails inserted in table:" + countOfAsianRaceDetailsSubmitted.length);
        }
        //hawaiian race details
        List<HawaiianPacificIslanderRace> incidentHawaiianRaceDetails = getIncidentHawaiianRaceList(updatedRaceDetails);
        if (!incidentAsianRaceDetails.isEmpty()) {
            logger.info("all incident hawaiian details:" + incidentHawaiianRaceDetails);
            int[] countOfHawaiianRaceDetailsSubmitted = submittHawaiianRaceDetailsOfIncident(incidentHawaiianRaceDetails);
            logger.info("total hawaiianRaceDetails inserted in table:" + countOfHawaiianRaceDetailsSubmitted.length);
        }
        return countOfOffCivliansRaceDetailsSubmitted;
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
        List<Injury> civilianInjuryList = getCiviliansInjuryDetails(civiliansList);
        logger.info("civilians Injury List:" + civilianInjuryList);
        List<InjuryType> civiliansInjuryType = getCiviliansInjuriesWithInjuryType(civilianInjuryList);
        logger.info("civilians Injury Type List:" + civiliansInjuryType);
        List<Injury> updatedCiviliansInjuryList = getUpdatedCiviliansInjuryDetails(civiliansInjuryType, civilianInjuryList);
        logger.info("updated Civilians Injury List:" + updatedCiviliansInjuryList);
        List<Civilians> civiliansDetails = getUpdatedCiviliansWithInjuryTypeForIncident(civiliansList, updatedCiviliansInjuryList);
        return civiliansDetails;
    }

    public List<Officers> getOfficersWithInjuryDetails(List<Officers> officersList) {
        List<Injury> officersInjuryList = getOfficeresInjuryDetails(officersList);
        logger.info("officers Injury List:" + officersInjuryList);
        List<InjuryType> officersInjuryType = getOfficersInjuriesWithInjuryType(officersInjuryList);
        logger.info("officers Injury Type List:" + officersInjuryType);
        List<Injury> updatedOfficersInjuryList = getUpdatedOfficersInjuryDetails(officersInjuryType, officersInjuryList);
        logger.info("updated Officers Injury List:" + updatedOfficersInjuryList);
        List<Officers> updatedOfficersWithInjuryDetails = getUpdatedOfficersWithInjuryTypeForIncident(officersList, updatedOfficersInjuryList);
        return updatedOfficersWithInjuryDetails;
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

    public List<Officers> getOfficersWithForceDetails(List<Officers> officersWithRaceDetails) {
        List<ForceDetails> officersForceDetails = getOfficersForceDetails(officersWithRaceDetails);
        logger.info("officersForceDetails for given IncidentId:" + officersForceDetails);
        List<IncidentForceLocation> officersForceLocationDetails = getOfficersForceLocationDetails(officersForceDetails);
        logger.info("officersForceLocationDetails gor given incident:" + officersForceLocationDetails);
        List<IncidentForceType> officersForceTypeDetails = getOfficersForceTypeDetails(officersForceDetails);
        logger.info("officersForceTypeDetails for given incident:" + officersForceTypeDetails);
        List<ForceDetails> updatedOfficersForceWithForceLocation = getOfficersForceWithForceLocation(officersForceDetails, officersForceLocationDetails);
        logger.info("updatedOfficersForceWithForceLocation for given incident:" + updatedOfficersForceWithForceLocation);
        List<ForceDetails> updatedOfficersForceWithLocationTypeDetails = getUpdatedOfficersForceWithLocationTypeDetails(updatedOfficersForceWithForceLocation, officersForceTypeDetails);
        logger.info("updatedOfficersForceWithLocationTypeDetails for given incident:" + updatedOfficersForceWithLocationTypeDetails);
        List<Officers> updatedOfficersWithForceDetails = getUpdatedOfficersWithOfficersForceDetails(officersWithRaceDetails, updatedOfficersForceWithLocationTypeDetails);
        return updatedOfficersWithForceDetails;
    }

    public List<Officers> getUpdateOfficersDetailsWithOfficerRaceDetails(List<Officers> officersWithInjuryDetails) {
        List<Race> officersRaceList = getOfficeresRaceDetails(officersWithInjuryDetails);
        logger.info("officersRaceList:" + officersRaceList);
        List<PrimaryRace> officersPrimaryRaceDetails = getOfficersPrimaryRaceDetails(officersRaceList);
        logger.info("officers Primary Race Details:" + officersPrimaryRaceDetails);
        List<AsianRace> officersAsianRaceDetails = getOfficersAsianRaceDetails(officersRaceList);
        logger.info("officers Asian Race Details:" + officersAsianRaceDetails);
        List<HawaiianPacificIslanderRace> officersHawaiianRaceDetails = getOfficersHawaiianRaceDetails(officersRaceList);
        logger.info("officers Hawaiian Race Details:" + officersHawaiianRaceDetails);
        List<Race> updateOfficersRaceWithOfficersPrimaryRace = getUpdatedOfficersRaceWithPrimaryRaceDetails(officersPrimaryRaceDetails, officersRaceList);
        logger.info("update Officers Race With Officers Primary Race:" + updateOfficersRaceWithOfficersPrimaryRace);
        List<Race> updateOfficersRaceWithOfficersAsianRace = getUpdatedOfficersRaceWithAsianRaceDetails(officersAsianRaceDetails, officersRaceList);
        logger.info("update Officers Race With Officers Asian Race:" + updateOfficersRaceWithOfficersAsianRace);
        List<Race> updateOfficersRaceWithOfficersHawaiianRace = getUpdatedOfficersRaceWithHawaiianRaceDetails(officersHawaiianRaceDetails, officersRaceList);
        logger.info("update Officers Race With Officers Hawaiian Race:" + updateOfficersRaceWithOfficersHawaiianRace);
        List<Officers> updateOfficersWithPrimaryRace = getUpdateOfficersWithPrimaryRace(officersWithInjuryDetails, updateOfficersRaceWithOfficersPrimaryRace);
        logger.info("officers with primaryRace:" +updateOfficersWithPrimaryRace);

        List<Officers> updateOfficersWithAsianRace;
        if (!updateOfficersRaceWithOfficersAsianRace.isEmpty()) {
            updateOfficersWithAsianRace = getUpdateOfficersWithAsianRace(updateOfficersWithPrimaryRace, updateOfficersRaceWithOfficersAsianRace);
            updateOfficersWithPrimaryRace = updateOfficersWithAsianRace;
            logger.info("officers with asianRace :" + updateOfficersWithPrimaryRace);
        }

        List<Officers> officersWithHawaiianRace;
        if (!updateOfficersRaceWithOfficersHawaiianRace.isEmpty()) {
            officersWithHawaiianRace = getUpdateOfficersWithHawaiianRace(updateOfficersWithPrimaryRace, updateOfficersRaceWithOfficersHawaiianRace);
            updateOfficersWithPrimaryRace = officersWithHawaiianRace;
            logger.info("officers with hawaiian race :" + updateOfficersWithPrimaryRace);
        }
        return updateOfficersWithPrimaryRace;
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

//        return civiliansWithResistance.stream()
//                .map(person -> civiliansWithPerceivedWeaponList.stream()                                       // map Person to
//                        .filter(i -> i.getCivilianId()==(person.getCivilianId()))  // .. the found Id
//                        .findFirst().orElse(person))                    // .. or else to self
//                .collect(Collectors.toList());
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
        //incident_confirmed_armed_weapon
        System.out.println("civiliansWithForceDetails @getCiviliansWithConfirmedArmedWeapons ---:"+civiliansWithForceDetails);
        List<ConfimedArmedWeapon> confimedArmedWeapons = getConfimedWeaponTypeListForCivilian(civiliansWithForceDetails);
        System.out.println(" confirmedArmedWeapons for given incident ----:" + confimedArmedWeapons);
        List<Civilians> civiliansWithConfirmedArmedWeapons = new ArrayList<>();
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

    public List<Civilians> getCiviliansWithForceDetails(List<Civilians> civiliansWithFireArmDetails) {
        //forcedetails -- civilians
        List<ForceDetails> civilianForceDetails = getCivilianForceDetails(civiliansWithFireArmDetails);
        System.out.println(" civilianForceDetails for given IncidentId ---- :" + civilianForceDetails);
        List<IncidentForceLocation> civilianForceLocationDetails = getCivilianForceLocationDetails(civilianForceDetails);
        System.out.println(" civilianForceLocationDetails gor given incident ----:" + civilianForceLocationDetails);
        List<IncidentForceType> civilianForceTypeDetails = getCivilianForceTypeDetails(civilianForceDetails);
        System.out.println(" civilianForceTypeDetails for given incident ---- :" + civilianForceTypeDetails);
        List<ForceDetails> updatedCivilianForceWithForceLocation = getCivilianForceWithForceLocation(civilianForceDetails, civilianForceLocationDetails);
        System.out.println("updatedCivilianForceWithForceLocation for given incident ---:" + updatedCivilianForceWithForceLocation);
        List<ForceDetails> updatedCivilianForceWithLocationTypeDetails = getUpdatedCivilianForceWithLocationTypeDetails(updatedCivilianForceWithForceLocation, civilianForceTypeDetails);
        System.out.println("updatedCivilianForceWithLocationTypeDetails for given incident --- :" + updatedCivilianForceWithLocationTypeDetails);
        List<Civilians> updatedCivilinsWithForceDetails = getUpdatedCiviliansWithCivilianForceDetails(civiliansWithFireArmDetails, updatedCivilianForceWithLocationTypeDetails);
        System.out.println(" updatedCivilinsWithForceDetails -------------------:" + updatedCivilinsWithForceDetails);
        return updatedCivilinsWithForceDetails.stream()
                .map(civilians -> civiliansWithFireArmDetails.stream()
                        .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
        //return updatedCivilinsWithForceDetails;
    }

    public List<Civilians> getCiviliansWithFireArmDetails(List<Civilians> civiliansWithRaceDetails) {
        // get civilain fireArm details
        System.out.println(" civiliansWithRaceDetails @getCiviliansWithFireArmDetails----:"+civiliansWithRaceDetails);
        List<FireArm> civilainFireArms = getClientFireArmListInIncident(civiliansWithRaceDetails);
        System.out.println(" civilainFireArms for given incident --- :" + civilainFireArms);
        if (!civilainFireArms.isEmpty()) {
            List<Civilians> civiliansWithFireArmList = getUpdateCiviliansWithFireArm(civiliansWithRaceDetails, civilainFireArms);
            System.out.println(" civiliansWithFireArmList -------------------:" + civiliansWithFireArmList);
            return civiliansWithFireArmList.stream()
                    .map(civilians -> civiliansWithRaceDetails.stream()
                            .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                            .findFirst().orElse(civilians))
                    .collect(Collectors.toList());
        }
        return civiliansWithRaceDetails;
    }

    public List<Civilians> getUpdateCivilainDetailsWithCivilianRaceDetails(List<Civilians> CiviliansDetails) {
        //get race details
        List<Race> civilianRaceList = getCiviliansRaceDetails(CiviliansDetails);
        System.out.println("civilianRaceList------------:" + civilianRaceList);
        //get PrimaryRace details
        List<PrimaryRace> civiliansPrimaryRaceDetails = getCiviliansPrimaryRaceDetails(civilianRaceList);
        System.out.println("civiliansPrimaryRaceDetails--------:" + civiliansPrimaryRaceDetails);
        //get asianRaceType details
        List<AsianRace> civiliansAsianRaceDetails = getCiviliansAsianRaceDetails(civilianRaceList);
        System.out.println("civiliansAsianRaceDetails--------:" + civiliansAsianRaceDetails);
        //get hawaiisn racetype details
        List<HawaiianPacificIslanderRace> civiliansHawaiianRaceDetails = getCiviliansHawaiianRaceDetails(civilianRaceList);
        System.out.println("civiliansHawaiianRaceDetails--------:" + civiliansHawaiianRaceDetails);

        List<Race> updatedCiviliansRaceWithCiviliansPrimaryRace = getUpdatedCiviliansRaceWithPrimaryRaceDetails(civiliansPrimaryRaceDetails, civilianRaceList);
        System.out.println("updatedCiviliansRaceWithCiviliansPrimaryRace-----------:" + updatedCiviliansRaceWithCiviliansPrimaryRace);

        List<Race> updatedCiviliansRaceWithCiviliansAsianRace = getUpdatedCiviliansRaceWithAsianRaceDetails(civiliansAsianRaceDetails, civilianRaceList);
        System.out.println("updatedCiviliansRaceWithCiviliansAsianRace-----------:" + updatedCiviliansRaceWithCiviliansAsianRace);

        List<Race> updatedCiviliansRaceWithCiviliansHawaiianRace = getUpdatedCiviliansRaceWithHawaiianRaceDetails(civiliansHawaiianRaceDetails, civilianRaceList);
        System.out.println("updatedCiviliansRaceWithCiviliansHawaiianRace-----------:" + updatedCiviliansRaceWithCiviliansHawaiianRace);

        List<Civilians> updateCivilainsWithPrimaryRace = getUpdateCivilainsWithPrimaryRace(CiviliansDetails, updatedCiviliansRaceWithCiviliansPrimaryRace);
        System.out.println("civilians with primaryRace :" + updateCivilainsWithPrimaryRace);

        List<Civilians> updateCivilainsWithAsianRace = new ArrayList<>();
        if (!updatedCiviliansRaceWithCiviliansAsianRace.isEmpty()) {
            updateCivilainsWithAsianRace = getUpdateCivilainsWithAsianRace(updateCivilainsWithPrimaryRace, updatedCiviliansRaceWithCiviliansAsianRace);
            updateCivilainsWithPrimaryRace = updateCivilainsWithAsianRace;
            System.out.println("civilians with asianRace :" + updateCivilainsWithPrimaryRace);
        }

        if (!updatedCiviliansRaceWithCiviliansHawaiianRace.isEmpty()) {
            updateCivilainsWithPrimaryRace = getUpdateCiviliansWithHawaiianRace(updateCivilainsWithPrimaryRace, updatedCiviliansRaceWithCiviliansHawaiianRace);
            System.out.println("civlians with hawaiian race :" + updateCivilainsWithPrimaryRace);

        }
        return updateCivilainsWithPrimaryRace.stream()
                .map(civilians -> CiviliansDetails.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<IncidentForceType> getOfficersForceTypeDetails(List<ForceDetails> officersForceDetails) {
        List<IncidentForceType> incidentForceTypeList = null;
        try {
            List<Integer> forceIds = getCivilianForceIds(officersForceDetails);
            logger.info("forceIds of officers for given incident:"+forceIds);
            String inParams = forceIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            incidentForceTypeList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_FORCE_TYPE, inParams), new ForceTypeRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return incidentForceTypeList;
    }

    public List<IncidentForceType> getCivilianForceTypeDetails(List<ForceDetails> civilianForceDetails) {
        List<Integer> forceIds = getCivilianForceIds(civilianForceDetails);
        System.out.println("forceIds for getCivilianForceTypeDetails --- :" + forceIds);
        System.out.println(" getCivilianForceTypeDetails for force_level_force_id --- : " + forceIds);
        String inParams = forceIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        System.out.println("inParams for get getCivilianForceTypeDetails --- :" + inParams);
        List<IncidentForceType> incidentForceTypeList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_CIVILIANS_FORCE_TYPE, inParams), new ForceTypeRowMapper());
        return incidentForceTypeList;
    }

    public List<IncidentForceLocation> getOfficersForceLocationDetails(List<ForceDetails> officersForceDetails) {
        List<IncidentForceLocation> incidentForceLocationList = null;
        try {
            List<Integer> forceIds = getCivilianForceIds(officersForceDetails);
            String inParams = forceIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            incidentForceLocationList = jdbcTemplate.query(String.format(incidentSql.GET_OFFICERS_FORCE_LOCATION_DETAILS, inParams), new ForceLocationRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return incidentForceLocationList;
    }


    public List<IncidentForceLocation> getCivilianForceLocationDetails(List<ForceDetails> civilianForceDetails) {
        List<Integer> forceIds = getCivilianForceIds(civilianForceDetails);
        String inParams = forceIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        System.out.println("inParams for get getCivilianForceDetails --- :" + inParams);
        List<IncidentForceLocation> incidentForceLocationList = jdbcTemplate.query(String.format(incidentSql.GET_INCIDENT_CIVILIANS_FORCE_LOCATION, inParams), new ForceLocationRowMapper());
        return incidentForceLocationList;
    }

    public List<ForceDetails> getOfficersForceDetails(List<Officers> updateOfficersWithPrimaryRace) {
        List<ForceDetails> forceDetailsList=new ArrayList<>();
        try {
            List<Integer> officerIds = getOfficerIds(updateOfficersWithPrimaryRace);
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceDetailsList = jdbcTemplate.query(String.format(incidentSql.GET_OFFICERS_FORCE_DETAILS, inParams), new ForceDetailsRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceDetailsList;
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

    public List<ForceDetails> getCivilianForceDetails(List<Civilians> civiliansWithFireArmList) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        try {
            List<Integer> civilianIds = getCivilianIds(civiliansWithFireArmList);
            String inParams = civilianIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceDetailsList = jdbcTemplate.query(String.format(incidentSql.GET_CIV_FORCE_DETAILS, inParams), new ForceDetailsRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceDetailsList;
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

    public List<Civilians> getUpdatedCiviliansWithInjuryTypeForIncident(List<Civilians> civiliansList, List<Injury> updatedCiviliansInjuryList) {
        List<Civilians> civList = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (Injury injury : updatedCiviliansInjuryList) {
                if (civilians.getCivilianId() == injury.getCivilianId()) {
                    civilians.setInjuryDetails(injury);
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

    public List<Officers> getUpdateOfficersWithHawaiianRace(List<Officers> updateOfficersWithPrimaryRace, List<Race> updateOfficersRaceWithOfficersHawaiianRace) {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : updateOfficersWithPrimaryRace) {
            for (Race hawaiianRace : updateOfficersRaceWithOfficersHawaiianRace) {
                Race race = new Race();
                if (officers.getOfficerId() == hawaiianRace.getOfficerId()) {
                    race.setPrimaryRaceType(hawaiianRace.getPrimaryRaceType());
                    race.setAsianRaceType(hawaiianRace.getAsianRaceType());
                    race.setHawaiianRaceType(hawaiianRace.getHawaiianRaceType());
                    officers.setRaceDetails(race);
                    officersList.add(officers);
                }
            }
        }
        return updateOfficersWithPrimaryRace.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }


    public List<Civilians> getUpdateCiviliansWithHawaiianRace(List<Civilians> updateCivilainsWithAsianRace, List<Race> updatedCiviliansRaceWithCiviliansHawaiianRace) {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : updateCivilainsWithAsianRace) {
            for (Race hawaiianRace : updatedCiviliansRaceWithCiviliansHawaiianRace) {
                Race race = new Race();
                if (civilians.getCivilianId() == hawaiianRace.getCivilainsId()) {
                    race.setPrimaryRaceType(hawaiianRace.getPrimaryRaceType());
                    race.setAsianRaceType(hawaiianRace.getAsianRaceType());
                    race.setHawaiianRaceType(hawaiianRace.getHawaiianRaceType());
                    civilians.setRaceDetails(race);
                    civiliansList.add(civilians);
                }
            }
        }
        return updateCivilainsWithAsianRace.stream()
                .map(civilians -> civiliansList.stream()
                        .filter(i -> i.getCivilianId()==(civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
                .collect(Collectors.toList());
    }

    public List<Officers> getUpdateOfficersWithAsianRace(List<Officers> updateOfficersWithPrimaryRace, List<Race> updateOfficersRaceWithOfficersAsianRace) {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : updateOfficersWithPrimaryRace) {
            for (Race asianRace : updateOfficersRaceWithOfficersAsianRace) {
                Race race = new Race();
                if (officers.getOfficerId() == asianRace.getOfficerId()) {
                    race.setPrimaryRaceType(asianRace.getPrimaryRaceType());
                    race.setAsianRaceType(asianRace.getAsianRaceType());
                    officers.setRaceDetails(race);
                    officersList.add(officers);
                }
            }
        }
        return updateOfficersWithPrimaryRace.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());

    }

    public List<ForceDetails> getUpdatedOfficersForceWithLocationTypeDetails(List<ForceDetails> updatedOfficersForceWithForceLocation, List<IncidentForceType> officersForceTypeDetails) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        for (ForceDetails forceDetails : updatedOfficersForceWithForceLocation) {
            List<String> forceType = new ArrayList<>();
            for (IncidentForceType incidentForceType : officersForceTypeDetails) {
                if (forceDetails.getForceId() == incidentForceType.getForceId()) {
                    forceType.add(incidentForceType.getForceType());
                    forceDetails.setForceType(forceType);
                    forceDetailsList.add(forceDetails);
                }
            }
        }
        return forceDetailsList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceDetails> getUpdatedCivilianForceWithLocationTypeDetails(List<ForceDetails> updatedCivilianForceWithForceLocation, List<IncidentForceType> civilianForceTypeDetails) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        for (ForceDetails forceDetails : updatedCivilianForceWithForceLocation) {
            List<String> forceType = new ArrayList<>();
            for (IncidentForceType incidentForceType : civilianForceTypeDetails) {
                if (forceDetails.getForceId() == incidentForceType.getForceId()) {
                    forceType.add(incidentForceType.getForceType());
                    forceDetails.setForceType(forceType);
                    forceDetailsList.add(forceDetails);
                }
            }
        }
        return forceDetailsList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceDetails> getOfficersForceWithForceLocation(List<ForceDetails> officersForceDetails, List<IncidentForceLocation> officersForceLocationDetails) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        for (ForceDetails forceDetails : officersForceDetails) {
            List<String> forceLocation = new ArrayList<>();
            for (IncidentForceLocation incidentForceLocation : officersForceLocationDetails) {
                if (forceDetails.getForceId() == incidentForceLocation.getForceId()) {
                    forceLocation.add(incidentForceLocation.getForceLocation());
                    forceDetails.setForceLocation(forceLocation);
                    forceDetailsList.add(forceDetails);
                }
            }
        }
        return forceDetailsList.stream().distinct().collect(Collectors.toList());
    }

    public List<ForceDetails> getCivilianForceWithForceLocation(List<ForceDetails> civilianForceDetails, List<IncidentForceLocation> civilianForceLocationDetails) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        for (ForceDetails forceDetails : civilianForceDetails) {
            List<String> forceLocation = new ArrayList<>();
            for (IncidentForceLocation incidentForceLocation : civilianForceLocationDetails) {
                if (forceDetails.getForceId() == incidentForceLocation.getForceId()) {
                    forceLocation.add(incidentForceLocation.getForceLocation());
                    forceDetails.setForceLocation(forceLocation);
                    forceDetailsList.add(forceDetails);
                }
            }
        }
        return forceDetailsList.stream().distinct().collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCivilainsWithAsianRace(List<Civilians> civiliansList, List<Race> updatedCiviliansRaceWithCiviliansAsianRace) {
        List<Civilians> civList = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
                for (Race asianRace : updatedCiviliansRaceWithCiviliansAsianRace) {
                    Race race = new Race();
                    if (civilians.getCivilianId() == asianRace.getCivilainsId()) {
                        race.setPrimaryRaceType(asianRace.getPrimaryRaceType());
                        race.setAsianRaceType(asianRace.getAsianRaceType());
                        civilians.setRaceDetails(race);
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

    public List<Officers> getUpdateOfficersWithPrimaryRace(List<Officers> updatedOfficersWithInjuryDetails, List<Race> updateOfficersRaceWithOfficersPrimaryRace) {
        List<Officers> officersList1 = new ArrayList<>();
        for (Officers officers : updatedOfficersWithInjuryDetails) {
            for (Race primaryRace : updateOfficersRaceWithOfficersPrimaryRace) {
                Race race = new Race();
                if (officers.getOfficerId() == primaryRace.getOfficerId()) {
                    race.setPrimaryRaceType(primaryRace.getPrimaryRaceType());
                    officers.setRaceDetails(race);
                    officersList1.add(officers);
                }
            }
        }
        return updatedOfficersWithInjuryDetails.stream()
                .map(officers -> officersList1.stream()
                        .filter(i -> i.getOfficerId()==(officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdateCivilainsWithPrimaryRace(List<Civilians> civiliansList, List<Race> updatedCiviliansRaceWithCiviliansPrimaryRace) {
        List<Civilians> civiliansList1 = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (Race primaryRace : updatedCiviliansRaceWithCiviliansPrimaryRace) {
                Race race = new Race();
                if (civilians.getCivilianId() == primaryRace.getCivilainsId()) {
                    race.setPrimaryRaceType(primaryRace.getPrimaryRaceType());
                    civilians.setRaceDetails(race);
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


    public List<String> getUpdatedCiviliansRaceWithCiviliansPrimaryRaceList(List<Civilians> civiliansList, List<Race> updatedCiviliansRaceWithCiviliansPrimaryRace) {
        List<String> civiliansRaceWithPrimaryRaceList = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (Race primaryRace : updatedCiviliansRaceWithCiviliansPrimaryRace) {
                if (civilians.getCivilianId() == primaryRace.getCivilainsId()) {
                    civiliansRaceWithPrimaryRaceList.addAll(primaryRace.getPrimaryRaceType());
                }
            }
        }
        return civiliansRaceWithPrimaryRaceList;
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


    public List<Officers> getUpdatedOfficersWithOfficersForceDetails(List<Officers> updateOfficersWithPrimaryRace, List<ForceDetails> updatedOfficersForceWithLocationTypeDetails) {
        List<Officers> officersList = new ArrayList<>();
        for (Officers officers : updateOfficersWithPrimaryRace) {
            for (ForceDetails forceDetails : updatedOfficersForceWithLocationTypeDetails) {
                if (forceDetails.getOfficerId() == officers.getOfficerId()) {
                    officers.setForceDetails(forceDetails);
                    officersList.add(officers);
                }
            }
        }
        return updateOfficersWithPrimaryRace.stream()
                .map(officers -> officersList.stream()
                        .filter(i -> i.getOfficerId() == (officers.getOfficerId()))
                        .findFirst().orElse(officers))
                .collect(Collectors.toList());
    }

    public List<Civilians> getUpdatedCiviliansWithCivilianForceDetails(List<Civilians> civiliansWithFireArmList, List<ForceDetails> updatedCivilianForceWithLocationTypeDetails) {
        List<Civilians> civiliansList = new ArrayList<>();
        for (Civilians civilians : civiliansWithFireArmList) {
            for (ForceDetails forceDetails : updatedCivilianForceWithLocationTypeDetails) {
                if (forceDetails.getCivilianId() == civilians.getCivilianId()) {
                    civilians.setForceDetails(forceDetails);
                    civiliansList.add(civilians);
                }
            }
        }
        return civiliansWithFireArmList.stream()
                .map(civilians -> civiliansList.stream()
                        .filter(i -> i.getCivilianId() == (civilians.getCivilianId()))
                        .findFirst().orElse(civilians))
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

    public List<Officers> getUpdatedOfficersWithInjuryTypeForIncident(List<Officers> officersList, List<Injury> updatedOfficersInjuryList) {
        List<Officers> officers = new ArrayList<>();
        for (Officers officers1 : officersList) {
            for (Injury injury : updatedOfficersInjuryList) {
                if (officers1.getOfficerId() == injury.getOfficerId()) {
                    officers1.setInjuryDetails(injury);
                    officers.add(officers1);
                }
            }
        }
        return officersList.stream()
                .map(officers1 -> officers.stream()
                        .filter(i -> i.getOfficerId() == (officers1.getOfficerId()))
                        .findFirst().orElse(officers1))
                .collect(Collectors.toList());
    }

    public List<Injury> getUpdatedCiviliansInjuryDetails(List<InjuryType> civiliansInjuryType, List<Injury> civilianInjuryList) {
        List<Injury> injuryList = new ArrayList<>();
        for (Injury injury : civilianInjuryList) // injurylist
        {
            List<String> injType = new ArrayList<>();
            InjuryType injTyp = new InjuryType();
            for (InjuryType injuryType : civiliansInjuryType) // injury type list
            {
                if (injury.getInjuryId() == injuryType.getInjuryId()) {
                    injTyp.setInjuryType(injuryType.getInjuryType());
                    injType.add(injuryType.getInjuryType());
                    injury.setInjuryType(injType);
                    injuryList.add(injury);
                    continue;
                }
            }
        }
        return injuryList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedOfficersRaceWithPrimaryRaceDetails(List<PrimaryRace> officersPrimaryRaceDetails, List<Race> officersRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : officersRaceList) {
            List<String> primaryRaceList = new ArrayList<>();
            PrimaryRace primaryRace = new PrimaryRace();
            for (PrimaryRace primaryRace1 : officersPrimaryRaceDetails) {
                if (race.getRaceId() == primaryRace1.getRaceId()) {
                    primaryRace.setPrimaryRace(primaryRace1.getPrimaryRace());
                    primaryRaceList.add(primaryRace1.getPrimaryRace());
                    race.setPrimaryRaceType(primaryRaceList);
                    raceList.add(race);
                    continue;
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedOfficersRaceWithHawaiianRaceDetails(List<HawaiianPacificIslanderRace> officersHawaiianRaceDetails, List<Race> officersRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : officersRaceList) {
            List<String> hawaiianPacificIslanderRaceList = new ArrayList<>();
            HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
            for (HawaiianPacificIslanderRace hawaiianPacificIslanderRace1 : officersHawaiianRaceDetails) {
                if (race.getRaceId() == hawaiianPacificIslanderRace1.getRaceId()) {
                    hawaiianPacificIslanderRace.setHawaiianRace(hawaiianPacificIslanderRace1.getHawaiianRace());
                    hawaiianPacificIslanderRaceList.add(hawaiianPacificIslanderRace1.getHawaiianRace());
                    race.setHawaiianRaceType(hawaiianPacificIslanderRaceList);
                    raceList.add(race);
                    continue;
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedCiviliansRaceWithHawaiianRaceDetails(List<HawaiianPacificIslanderRace> civiliansHawaiianRaceDetails, List<Race> civilianRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : civilianRaceList) {
            if(race.getHawaiianRaceType()!=null) {
                List<String> hawaiianPacificIslanderRaceList = new ArrayList<>();
                HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
                for (HawaiianPacificIslanderRace hawaiianPacificIslanderRace1 : civiliansHawaiianRaceDetails) {
                    if (race.getRaceId() == hawaiianPacificIslanderRace1.getRaceId()) {
                        hawaiianPacificIslanderRace.setHawaiianRace(hawaiianPacificIslanderRace1.getHawaiianRace());
                        hawaiianPacificIslanderRaceList.add(hawaiianPacificIslanderRace1.getHawaiianRace());
                        race.setHawaiianRaceType(hawaiianPacificIslanderRaceList);
                        raceList.add(race);
                        continue;
                    }
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedOfficersRaceWithAsianRaceDetails(List<AsianRace> officersAsianRaceDetails, List<Race> officersRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : officersRaceList) {
            List<String> asianRaceList = new ArrayList<>();
            AsianRace asianRace = new AsianRace();
            for (AsianRace asianRace1 : officersAsianRaceDetails) {
                if (race.getRaceId() == asianRace1.getRaceId()) {
                    asianRace.setAsianRace(asianRace1.getAsianRace());
                    asianRaceList.add(asianRace1.getAsianRace());
                    race.setAsianRaceType(asianRaceList);
                    raceList.add(race);
                    continue;
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedCiviliansRaceWithAsianRaceDetails(List<AsianRace> civiliansAsianRaceDetails, List<Race> civilianRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : civilianRaceList) {
            List<String> asianRaceList = new ArrayList<>();
            AsianRace asianRace = new AsianRace();
            for (AsianRace asianRace1 : civiliansAsianRaceDetails) {
                if (race.getRaceId() == asianRace1.getRaceId()) {
                    asianRace.setAsianRace(asianRace1.getAsianRace());
                    asianRaceList.add(asianRace1.getAsianRace());
                    race.setAsianRaceType(asianRaceList);
                    raceList.add(race);
                    continue;
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Race> getUpdatedCiviliansRaceWithPrimaryRaceDetails(List<PrimaryRace> civiliansPrimaryRaceDetails, List<Race> civilianRaceList) {
        List<Race> raceList = new ArrayList<>();
        for (Race race : civilianRaceList) {
            List<String> primaryRaceList = new ArrayList<>();
            PrimaryRace primaryRace = new PrimaryRace();
            for (PrimaryRace primaryRace1 : civiliansPrimaryRaceDetails) {
                if (race.getRaceId() == primaryRace1.getRaceId()) {
                    primaryRace.setPrimaryRace(primaryRace1.getPrimaryRace());
                    primaryRaceList.add(primaryRace1.getPrimaryRace());
                    race.setPrimaryRaceType(primaryRaceList);
                    raceList.add(race);
                    continue;
                }
            }
        }
        return raceList.stream().distinct().collect(Collectors.toList());
    }

    public List<Injury> getUpdatedOfficersInjuryDetails(List<InjuryType> officersInjuryType, List<Injury> officersInjuryList) {
        List<Injury> injuryList = new ArrayList<>();
        for (Injury injury : officersInjuryList) // injurylist
        {
            List<String> injType = new ArrayList<>();
            InjuryType injTyp = new InjuryType();
            for (InjuryType injuryType : officersInjuryType) // injury type list
            {
                if (injury.getInjuryId() == injuryType.getInjuryId()) {
                    injTyp.setInjuryType(injuryType.getInjuryType());
                    injType.add(injuryType.getInjuryType());
                    injury.setInjuryType(injType);
                    injuryList.add(injury);
                    continue;
                }
            }
        }
        return injuryList.stream().distinct().collect(Collectors.toList());
    }

    public List<PrimaryRace> getOfficersPrimaryRaceDetails(List<Race> officersRaceList) {
        List<Integer> officerRaceIds = getOfficerRaceIds(officersRaceList);
        logger.info("raceIds for officersRace:" + officerRaceIds);
        return getCivPrimaryRaceDetails(officerRaceIds);
    }

    public List<AsianRace> getOfficersAsianRaceDetails(List<Race> officersRaceList) {
        List<Integer> officersRaceIds = getCivilianRaceIds(officersRaceList);
        logger.info("raceIds for OfficersAsianRaceDetails:" + officersRaceIds);
        return getCivAsianRaceDetails(officersRaceIds);
    }

    public List<HawaiianPacificIslanderRace> getOfficersHawaiianRaceDetails(List<Race> officersRaceList) {
        List<Integer> officersRaceIds = getCivilianRaceIds(officersRaceList);
        logger.info("raceIds for OfficersHawaiianRaceDetails:" + officersRaceIds);
        return getCivHawaiianRaceDetails(officersRaceIds);
    }

    public List<HawaiianPacificIslanderRace> getCiviliansHawaiianRaceDetails(List<Race> civilianRaceList) {
        List<Integer> civilianRaceIds = getCivilianRaceIds(civilianRaceList);
        logger.info("raceIds for CiviliansHawaiianRaceDetails:" + civilianRaceIds);
        return getCivHawaiianRaceDetails(civilianRaceIds);
    }

    public List<AsianRace> getCiviliansAsianRaceDetails(List<Race> civilianRaceList) {
        List<Integer> civilianRaceIds = getCivilianRaceIds(civilianRaceList);
        logger.info("raceIds for getCiviliansAsianRaceDetails:" + civilianRaceIds);
        return getCivAsianRaceDetails(civilianRaceIds);
    }

    public List<PrimaryRace> getCiviliansPrimaryRaceDetails(List<Race> civilianRaceList) {
        List<Integer> civilianRaceIds = getCivilianRaceIds(civilianRaceList);
        logger.info("raceIds for civiliansRace:" + civilianRaceIds);
        return getCivPrimaryRaceDetails(civilianRaceIds);
    }

    public List<InjuryType> getCiviliansInjuriesWithInjuryType(List<Injury> civilianInjuryList) {
        List<Integer> injuryIds = getInjuryIds(civilianInjuryList);
        logger.info("injuryIds for civilains injury :" + injuryIds);
        return getOfficersInjuryType(injuryIds);
    }

    public List<InjuryType> getOfficersInjuriesWithInjuryType(List<Injury> officersInjuryList) {
        List<Integer> injuryIds = getInjuryIds(officersInjuryList);
        logger.info(" injuryIds for officers injury :" + injuryIds);
        return getOfficersInjuryType(injuryIds);
    }

    public List<Injury> getOfficeresInjuryDetails(List<Officers> officersList) {
        List<Integer> officerIds = getOfficerIds(officersList);
        return getInjuryListForOfficersInIncident(officerIds);
    }

    public List<Race> getOfficeresRaceDetails(List<Officers> officersList) {
        List<Integer> officerIds = getOfficerIds(officersList);
        logger.info("officerIds of Race:" + officerIds);
        return getUpdatedOfficersRaceDetailsWithRaceId(officerIds);
    }

    public List<Race> getCiviliansRaceDetails(List<Civilians> civiliansList) {
        List<Integer> civilianIds = getCivilianIds(civiliansList);
        logger.info("civilianIds for Race:" + civilianIds);
        return getUpdatedCivilianRaceDetailsWithRaceId(civilianIds);
    }

    public List<Injury> getCiviliansInjuryDetails(List<Civilians> civilianIdList) {
        List<Integer> civilianIds = getCivilianIds(civilianIdList);
        return getInjuryListForCiviliansInIncident(civilianIds);
    }

    public List<Integer> getOfficerRaceIds(List<Race> officersRaceList) {
        List<Integer> raceIds = new ArrayList<>();
        for (Race race : officersRaceList) {
            raceIds.add(race.getRaceId());
        }
        return raceIds;
    }

    public List<Integer> getCivilianRaceIds(List<Race> civilianRaceList) {
        logger.info("civilianRaceList for getCivilianRaceIds:" + civilianRaceList);
        List<Integer> raceIds = new ArrayList<>();
        for (Race race : civilianRaceList) {
            raceIds.add(race.getRaceId());
        }
        return raceIds;
    }

    public List<Integer> getInjuryIds(List<Injury> officersInjuryList) {
        logger.info("officersInjuryList for getInjuryIds:" + officersInjuryList);
        List<Integer> injuryIds = new ArrayList<>();
        for (Injury injury : officersInjuryList) {
            injuryIds.add(injury.getInjuryId());
        }
        return injuryIds;
    }

    public List<Integer> getOfficerIds(List<Officers> officersList) {
        List<Integer> OfficerIds = new ArrayList<>();
        for (Officers officers : officersList) {
            OfficerIds.add(officers.getOfficerId());
        }
        return OfficerIds;
    }

    public List<Integer> getCivilianForceIds(List<ForceDetails> civilianForceDetails) {
        List<Integer> civilianForceIds = new ArrayList<>();
        for (ForceDetails forceDetails : civilianForceDetails) {
            civilianForceIds.add(forceDetails.getForceId());
        }
        return civilianForceIds;
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

    public List<Civilians> getUpdatedCiviliansWithInjuryForIncident(List<Civilians> civilianInIncident, List<Civilians> civiliansList) {
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

    public List<Officers> getUpdatedOfficersWithInjuryForIncident(List<Officers> officersInIncident, List<Officers> officersList) {
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

    public List<Race> getOfficersRaceDetailsOfIncident(List<OfficerCivilians> officerCivilians, List<Officers> officers) {
        List<Race> raceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers1 : officers) {
                if ((officers1.getOfficerId() == offCiv.getOfficerId()) && (officers1.getRaceDetails() != null)) {
                    Race race = new Race();
                    race.setOfficerId(officers1.getOfficerId());
                    race.setCivilainsId(offCiv.getCivilianId());
                    race.setRaceOf("O");
                    race.setPrimaryRaceType(officers1.getRaceDetails().getPrimaryRaceType());
                    race.setAsianRaceType(officers1.getRaceDetails().getAsianRaceType());
                    race.setHawaiianRaceType(officers1.getRaceDetails().getHawaiianRaceType());
                    raceList.add(race);
                }
            }
        }
        return raceList;
    }

    public List<ForceDetails> getForceDetailsOfOfficersInIncident(List<OfficerCivilians> officerCivilians, List<Officers> officers) {
        List<ForceDetails> forceDetails = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers1 : officers) {
                if ((officers1.getOfficerId() == offCiv.getOfficerId()) && (officers1.getForceDetails() != null)) {
                    ForceDetails details = new ForceDetails();
                    details.setForceLocation(officers1.getForceDetails().getForceLocation());
                    details.setForceType(officers1.getForceDetails().getForceType());
                    details.setOfficerId(officers1.getOfficerId());
                    details.setCivilianId(offCiv.getCivilianId());
                    details.setForceOn("O");
                    forceDetails.add(details);
                }
            }
        }
        return forceDetails.stream().distinct().collect(Collectors.toList());

    }

    public List<Injury> getOfficersInjuryList(List<OfficerCivilians> officerCivilians, List<Officers> officers) {
        List<Injury> injuryList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Officers officers1 : officers) {
                if ((officers1.getOfficerId() == offCiv.getOfficerId()) && (officers1.getInjuryDetails() != null)) {
                    Injury injury = new Injury();
                    injury.setInjuryMedicalAid(officers1.getInjuryDetails().getInjuryMedicalAid());
                    injury.setInjuryLevel(officers1.getInjuryDetails().getInjuryLevel());
                    injury.setInjuryType(officers1.getInjuryDetails().getInjuryType());
                    injury.setOfficerId(officers1.getOfficerId());
                    injury.setCivilianId(offCiv.getCivilianId());
                    injury.setInjuryOffCiv("O");
                    injuryList.add(injury);
                }
            }
        }
        return injuryList;
    }

    public List<Injury> getCiviliansInjuryList(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1) {
        List<Injury> injuryList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civiliansList1) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getRaceDetails() != null)) {
                    Injury injury = new Injury();
                    injury.setInjuryMedicalAid(civilians.getInjuryDetails().getInjuryMedicalAid());
                    injury.setInjuryLevel(civilians.getInjuryDetails().getInjuryLevel());
                    injury.setInjuryType(civilians.getInjuryDetails().getInjuryType());
                    injury.setCivilianId(civilians.getCivilianId());
                    injury.setOfficerId(offCiv.getOfficerId());
                    injury.setInjuryOffCiv("C");
                    injuryList.add(injury);
                }
            }
        }
        return injuryList;
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

    public List<Race> getCiviliansRaceList(List<OfficerCivilians> officerCivilians, List<Civilians> civiliansList1) {
        List<Race> raceList = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civiliansList1) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getRaceDetails() != null)) {
                    Race race = new Race();
                    race.setCivilainsId(civilians.getCivilianId());
                    race.setOfficerId(offCiv.getOfficerId());
                    race.setAsianRaceType(civilians.getRaceDetails().getAsianRaceType());
                    race.setPrimaryRaceType(civilians.getRaceDetails().getPrimaryRaceType());
                    race.setHawaiianRaceType(civilians.getRaceDetails().getHawaiianRaceType());
                    race.setRaceOf("C");
                    raceList.add(race);
                }
            }
        }
        return raceList;
    }

    public List<ForceDetails> getForceDetailsOfCiviliansInIncident(List<OfficerCivilians> officerCivilians, List<Civilians> civilianInIncident) {
        List<ForceDetails> forceDetails = new ArrayList<>();
        for (OfficerCivilians offCiv : officerCivilians) {
            for (Civilians civilians : civilianInIncident) {
                if ((civilians.getCivilianId() == offCiv.getCivilianId()) && (civilians.getForceDetails() != null)) {
                    ForceDetails details = new ForceDetails();
                    details.setForceLocation(civilians.getForceDetails().getForceLocation());
                    details.setForceType(civilians.getForceDetails().getForceType());
                    details.setCivilianId(civilians.getCivilianId());
                    details.setOfficerId(offCiv.getOfficerId());
                    details.setForceOn("C");
                    forceDetails.add(details);
                }
            }
        }
        return forceDetails.stream().distinct().collect(Collectors.toList());
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


    public List<IncidentLocation> getIncidentLocationListDetails(int incidentId) {
        List<IncidentLocation> locationList = new ArrayList<>();
        try {
            locationList = jdbcTemplate.query(
                    incidentSql.GET_INCIDENT_LOCATION, new Object[]{incidentId},
                    new IncidentLocationRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return locationList;
    }

    public IncidentCoreDetails getIncidentCoreDetails(int incidentId) {
        IncidentCoreDetails incidentCoreDetails = new IncidentCoreDetails();
        try {
            incidentCoreDetails = (IncidentCoreDetails) jdbcTemplate.queryForObject(incidentSql.GET_INCIDENT_CORE_DETAILS, new Object[]{incidentId}, new IncidentRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return incidentCoreDetails;
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

    public List<Integer> getCivilianIdForIncident(int incidentId) {
        List<Integer> civilianId = new ArrayList<>();
        civilianId = jdbcTemplate.queryForList(incidentSql.GET_CIV_IDS, new Object[]{incidentId}, Integer.class);
        return civilianId;
    }

    public List<Integer> getOfficersIdForIncident(int incidentId) {
        List<Integer> officersId = new ArrayList<>();
        try {
            officersId = jdbcTemplate.queryForList(incidentSql.GET_OFF_IDS, new Object[]{incidentId}, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return officersId;
    }

    public List<Civilians> submitFireArmsUsedInIncident(List<Civilians> civiliansList, List<Integer> civilianId) {
        int civId = 0;
        List<Civilians> civList = new ArrayList<>();
        for (Civilians civilians : civiliansList) {
            for (int i = 0; i < civilianId.size(); i++) {
                civId = civilianId.get(i);
                Civilians civilian = new Civilians();
                civilian.setCivilianId(civId);
                civilian.setFireArms(civilians.getFireArms());
                civList.add(civilian);
                break;
            }
        }
        return civList;
    }

     public List<Injury> getInjuryDetailsOfOfficerInIncident(List<OfficerCivilians> officerCiviliansList, List<Officers> officersUpdateWithInuryList) {
        List<Injury> injuryList = new ArrayList<>();
        for (OfficerCivilians officerCivilians : officerCiviliansList) {
            for (Officers officers : officersUpdateWithInuryList) {
                Injury injury = new Injury();
                if ((officers.getOfficerId() == officerCivilians.getOfficerId()) && (officers.getInjuryDetails() != null)) {
                    injury.setInjuryLevel(officers.getInjuryDetails().getInjuryLevel());
                    injury.setInjuryMedicalAid(officers.getInjuryDetails().getInjuryMedicalAid());
                    injury.setInjuryType(officers.getInjuryDetails().getInjuryType());
                    injury.setOfficerId(officers.getOfficerId());
                    injury.setCivilianId(officerCivilians.getCivilianId());
                    injuryList.add(injury);
                }
            }
        }
        return injuryList;
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

    public List<Injury> getInjuryDetailsOfCivlianInIncident(List<OfficerCivilians> officerCiviliansList, List<Civilians> civiliansUpdateWithInuryList) {
        List<Injury> injuryList = new ArrayList<>();
        for (OfficerCivilians officerCivilians : officerCiviliansList) {
            for (Civilians civilians : civiliansUpdateWithInuryList) {
                Injury injury = new Injury();
                if ((civilians.getCivilianId() == officerCivilians.getCivilianId()) && (civilians.getInjuryDetails() != null)) {
                    injury.setInjuryLevel(civilians.getInjuryDetails().getInjuryLevel());
                    injury.setInjuryMedicalAid(civilians.getInjuryDetails().getInjuryMedicalAid());
                    injury.setInjuryType(civilians.getInjuryDetails().getInjuryType());
                    injury.setCivilianId(civilians.getCivilianId());
                    injury.setOfficerId(officerCivilians.getOfficerId());
                    injuryList.add(injury);
                }
            }
        }
        return injuryList;
    }

    public List<IncidentForceType> getIncidentForceTypeList(List<ForceDetails> civilainForceDetails) {
        List<IncidentForceType> incidentForceTypeList = new ArrayList<>();
        for (ForceDetails details : civilainForceDetails) {
            for (String type : details.getForceType()) {
                IncidentForceType incidentForceType = new IncidentForceType();
                incidentForceType.setForceId(details.getForceId());
                incidentForceType.setForceType(type);
                incidentForceTypeList.add(incidentForceType);
            }
        }
        return incidentForceTypeList;
    }

    public List<HawaiianPacificIslanderRace> getIncidentHawaiianRaceList(List<Race> updatedRaceDetails) {
        List<HawaiianPacificIslanderRace> hawaiianRaceList = new ArrayList<>();
        for (Race race : updatedRaceDetails) {
            if (race.getHawaiianRaceType() != null) {
                for (String hawaiianRace : race.getHawaiianRaceType()) {
                    HawaiianPacificIslanderRace hawaiianPacificIslanderRace = new HawaiianPacificIslanderRace();
                    hawaiianPacificIslanderRace.setHawaiianRace(hawaiianRace);
                    hawaiianPacificIslanderRace.setRaceId(race.getRaceId());
                    hawaiianRaceList.add(hawaiianPacificIslanderRace);
                }
            }
        }
        return hawaiianRaceList;
    }

    public List<AsianRace> getIncidentAsianRaceList(List<Race> updatedRaceDetails) {
        List<AsianRace> asianRaceList = new ArrayList<>();
        for (Race race : updatedRaceDetails) {
            if (race.getAsianRaceType() != null) {
                for (String asianRace : race.getAsianRaceType()) {
                    AsianRace asianRace1 = new AsianRace();
                    asianRace1.setAsianRace(asianRace);
                    asianRace1.setRaceId(race.getRaceId());
                    asianRaceList.add(asianRace1);
                }
            }
        }
        return asianRaceList;
    }

    public List<InjuryType> getIncidentInjuryTypeList(List<Injury> updatedInjuryDetails) {
        List<InjuryType> injuryTypeList = new ArrayList<>();
        for (Injury injury : updatedInjuryDetails) {
            if (injury.getInjuryType() != null) {
                for (String injuryType : injury.getInjuryType()) {
                    InjuryType type = new InjuryType();
                    type.setInjuryType(injuryType);
                    type.setInjuryId(injury.getInjuryId());
                    injuryTypeList.add(type);
                }
            }
        }
        return injuryTypeList;
    }

    public List<PrimaryRace> getIncidentPrimayRaceList(List<Race> updatedRaceDetails) {
        List<PrimaryRace> primaryRaceList = new ArrayList<>();
        for (Race race : updatedRaceDetails) {
            for (String primaryRaceType : race.getPrimaryRaceType()) {
                PrimaryRace primaryRace = new PrimaryRace();
                primaryRace.setPrimaryRace(primaryRaceType);
                primaryRace.setRaceId(race.getRaceId());
                primaryRaceList.add(primaryRace);
            }
        }
        return primaryRaceList;
    }

    public List<IncidentForceLocation> getIncidentOfficersForceDetails(List<ForceDetails> officersForceDetails)
    {
        List<ForceDetails> distinctOfficersForceDetails = new ArrayList<>();
        List<IncidentForceLocation> incidentForceLocationList = new ArrayList<>();
        try {
            distinctOfficersForceDetails = officersForceDetails.stream()
                    .filter(p -> p.getForceOn().equals("O"))
                    .filter( distinctByKeys(p -> p.getOfficerId()) )
                    .collect( Collectors.toList() );
            for (ForceDetails details : distinctOfficersForceDetails) {
                    for (String location : details.getForceLocation()) {
                        IncidentForceLocation incidentForceLocation = new IncidentForceLocation();
                        incidentForceLocation.setForceId(details.getForceId());
                        incidentForceLocation.setForceLocation(location);
                        incidentForceLocationList.add(incidentForceLocation);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidentForceLocationList;
    }

    public List<IncidentForceType> getIncidentOfficersForceTypeDetails(List<ForceDetails> civilainForceDetails)
    {
        List<ForceDetails> distinctCivilianForceDetails;
        List<IncidentForceType> incidentForceTypeList = new ArrayList<>();
        try {
            distinctCivilianForceDetails = civilainForceDetails.stream()
                    .filter(p -> p.getForceOn().equals("O"))
                    .filter( distinctByKeys(p -> p.getOfficerId()) )
                    .collect( Collectors.toList() );
            for (ForceDetails details : distinctCivilianForceDetails) {
                for (String type : details.getForceType()) {
                    IncidentForceType incidentForceType = new IncidentForceType();
                    incidentForceType.setForceId(details.getForceId());
                    incidentForceType.setForceType(type);
                    incidentForceTypeList.add(incidentForceType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidentForceTypeList;
    }

    public List<IncidentForceType> getIncidentCiviliansForceTypeDetails(List<ForceDetails> civilainForceDetails)
    {
        List<IncidentForceType> incidentForceTypeList = new ArrayList<>();
        try {
            List<ForceDetails> distinctCivilianForceDetails = civilainForceDetails.stream()
                    .filter(p -> p.getForceOn().equals("C"))
                    .filter( distinctByKeys(p -> p.getCivilianId()) )
                    .collect( Collectors.toList() );
            for (ForceDetails details : distinctCivilianForceDetails) {
                for (String type : details.getForceType()) {
                    IncidentForceType incidentForceType = new IncidentForceType();
                    incidentForceType.setForceId(details.getForceId());
                    incidentForceType.setForceType(type);
                    incidentForceTypeList.add(incidentForceType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidentForceTypeList;
    }

    public List<IncidentForceLocation> getIncidentCiviliansForceDetails(List<ForceDetails> civilainForceDetails)
    {
        List<IncidentForceLocation> incidentForceLocationList = new ArrayList<>();
        try {
            List<ForceDetails> distinctCivilianForceDetails = civilainForceDetails.stream()
                    .filter(p -> p.getForceOn().equals("C"))
                    .filter( distinctByKeys(p -> p.getCivilianId()) )
                    .collect( Collectors.toList() );
            for (ForceDetails details : distinctCivilianForceDetails) {
                    for (String location : details.getForceLocation()) {
                        IncidentForceLocation incidentForceLocation = new IncidentForceLocation();
                        incidentForceLocation.setForceId(details.getForceId());
                        incidentForceLocation.setForceLocation(location);
                        incidentForceLocationList.add(incidentForceLocation);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidentForceLocationList;
    }


    public List<IncidentForceLocation> getIncidentForceLocationList(List<ForceDetails> civilainForceDetails) {
        List<IncidentForceLocation> incidentForceLocationList = new ArrayList<>();
        for (ForceDetails details : civilainForceDetails) {
            for (String location : details.getForceLocation()) {
                IncidentForceLocation incidentForceLocation = new IncidentForceLocation();
                incidentForceLocation.setForceId(details.getForceId());
                incidentForceLocation.setForceLocation(location);
                incidentForceLocationList.add(incidentForceLocation);
            }
        }
        return incidentForceLocationList;
    }

    public List<Injury> getUpdatedInjuryDetails(List<Injury> injuryList, List<Injury> civilainsInjuryDetails) {
        List<Injury> updatedInjuryDetails = new ArrayList<>();
        for (Injury injury : civilainsInjuryDetails) {
            for (Injury injury1 : injuryList) {
                if ((injury.getCivilianId() == injury1.getCivilianId()) && (injury.getOfficerId() == injury1.getOfficerId()) && ((injury.getInjuryOffCiv().equals(injury1.getInjuryOffCiv())))) {
                    injury.setInjuryId(injury1.getInjuryId());
                    injury.setInjuryOffCiv(injury1.getInjuryOffCiv());
                    updatedInjuryDetails.add(injury);
                }
            }
        }
        return updatedInjuryDetails;
    }

    public List<Race> getUpdatedRaceDetails(List<Race> raceList, List<Race> civilainsRaceDetails) {
        List<Race> updatedRaceDetails = new ArrayList<>();
        for (Race race : civilainsRaceDetails) {
            for (Race race1 : raceList) {
                if ((race.getCivilainsId() == race1.getCivilainsId()) && (race.getOfficerId() == race1.getOfficerId()) && ((race.getRaceOf().equals(race1.getRaceOf())))) {
                    race.setRaceId(race1.getRaceId());
                    race.setRaceOf(race1.getRaceOf());
                    updatedRaceDetails.add(race);
                }
            }
        }
        return updatedRaceDetails;
    }

    public List<ForceDetails> getUpdatedCivilianForceDetails(List<ForceDetails> forceDetailsWithForceIdList, List<ForceDetails> forceDetailsOfCiviliansList) {
        List<ForceDetails> updatedCivilianForceDetails = new ArrayList<>();
        for (ForceDetails details : forceDetailsWithForceIdList) {
            for (ForceDetails details1 : forceDetailsOfCiviliansList) {
                if ((details.getCivilianId() == details1.getCivilianId()) && (details.getOfficerId() == details1.getOfficerId()) && ((details.getForceOn().equals(details1.getForceOn())))) {
                    details1.setForceId(details.getForceId());
                    details1.setForceOn(details.getForceOn());
                    updatedCivilianForceDetails.add(details1);
                }
            }
        }
        return updatedCivilianForceDetails;
    }

    public List<ForceDetails> getUpdatedOfficersForceDetails(List<ForceDetails> forceDetailsWithForceIdList, List<ForceDetails> forceDetailsOfCiviliansList) {
        List<ForceDetails> updatedOfficersForceDetails = new ArrayList<>();
        for (ForceDetails details : forceDetailsWithForceIdList) {
            for (ForceDetails details1 : forceDetailsOfCiviliansList) {
                if ((details.getOfficerId() == details1.getOfficerId()) && (details.getCivilianId() == details1.getCivilianId()) && (details.getForceOn().equals(details1.getForceOn()))) {
                    details1.setForceId(details.getForceId());
                    details1.setForceOn(details.getForceOn());
                    updatedOfficersForceDetails.add(details1);
                }
            }
        }
        return updatedOfficersForceDetails;
    }

    public List<Injury> getInjuryListForCiviliansInIncident(List<Integer> offCivList) {
        List<Injury> injuryDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryDetails = jdbcTemplate.query(String.format(incidentSql.GET_CIV_INJURY_LIST, inParams), new InjuryRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryDetails;
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

    public List<Injury> getInjuryListForOfficersInIncident(List<Integer> offCivList) {
        List<Injury> injuryDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryDetails = jdbcTemplate.query(String.format(incidentSql.GET_OFF_INJURY_DETAILS, inParams), new InjuryRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryDetails;
    }

    public List<Injury> getInjuryListWithInjuryId(List<Integer> offCivList) {
        List<Injury> injuryDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            injuryDetails = jdbcTemplate.query(String.format(incidentSql.GET_CIV_INJURY_DETAILS, inParams), new InjuryRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return injuryDetails;
    }

    public List<Race> getUpdatedOfficersRaceDetailsWithRaceId(List<Integer> officerIds) {
        List<Race> raceDetails = new ArrayList<>();
        try {
            String inParams = officerIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            raceDetails = jdbcTemplate.query(String.format(incidentSql.GET_OFF_RACE_DETAILS, inParams), new RaceRowMapper());
            return raceDetails;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return raceDetails;
    }

    public List<Race> getUpdatedCivilianRaceDetailsWithRaceId(List<Integer> offCivList) {
        List<Race> raceDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            raceDetails = jdbcTemplate.query(String.format(incidentSql.GET_CIV_RACE_DETAILS, inParams), new RaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return raceDetails;
    }

    public List<Race> getUpdatedRaceDetailsWithRaceId(List<Integer> offCivList) {
        List<Race> raceDetails = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            raceDetails = jdbcTemplate.query(String.format(incidentSql.GET_RACE_DETAILS, inParams), new RaceRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return raceDetails;
    }

    public List<ForceDetails> getUpdatedForceDetailsWithForceId(List<Integer> offCivList) {
        List<ForceDetails> forceDetailsList = new ArrayList<>();
        try {
            String inParams = offCivList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            forceDetailsList = jdbcTemplate.query(String.format(incidentSql.GET_FORCE_DETAILS, inParams), new ForceDetailsRowMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return forceDetailsList;
    }

    public List<Integer> getCivIds(List<OfficerCivilians> officerCivilians) {
        Set<Integer> offCivSet = new HashSet<>();
        for (OfficerCivilians forceDetailsList : officerCivilians) {
            offCivSet.add(forceDetailsList.getCivilianId());
        }
        return offCivSet.stream().collect(Collectors.toList());
    }

    public List<Integer> getOffIds(List<OfficerCivilians> officerCivilians) {
        Set<Integer> offCivSet = new HashSet<>();
        for (OfficerCivilians forceDetailsList : officerCivilians)
        {
            offCivSet.add(forceDetailsList.getOfficerId());
        }
        return offCivSet.stream().collect(Collectors.toList());
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


    public List<Integer> getOfficerIdsForIncident(List<Officers> officersUpdateWithInuryList, List<Civilians> civiliansUpdateWithInuryList) {
        List<Integer> officerIds = new ArrayList<>();
        for (Officers officers : officersUpdateWithInuryList) {
            officerIds.add(officers.getOfficerId());
        }
        return officerIds;
    }

    public int[] submittedForceTypeForIncident(List<IncidentForceType> incidentForceTypeList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_FORCE_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, incidentForceTypeList.get(i).getForceId());
                preparedStatement.setString(2, incidentForceTypeList.get(i).getForceType());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return incidentForceTypeList.size();
            }
        });
    }

    public int[] submittHawaiianRaceDetailsOfIncident(List<HawaiianPacificIslanderRace> incidentHawaiianRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_HAWAIIAN_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentHawaiianRaceDetails.get(i).getHawaiianRace());
                preparedStatement.setInt(2, incidentHawaiianRaceDetails.get(i).getRaceId());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
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
                preparedStatement.setInt(2, injuryTypeList.get(i).getInjuryId());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
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
                preparedStatement.setInt(2, incidentAsianRaceDetails.get(i).getRaceId());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
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
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
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
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
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
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return perceivedWeaponTypeList.size();
            }
        });
    }


    public int[] submittInjuryTypeDetailsOfIncident(List<InjuryType> incidentInjuryTypeDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_INJURY_TYPE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentInjuryTypeDetails.get(i).getInjuryType());
                preparedStatement.setInt(2, incidentInjuryTypeDetails.get(i).getInjuryId());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return incidentInjuryTypeDetails.size();
            }
        });
    }

    public int[] submittPrimaryRaceDetailsOfIncident(List<PrimaryRace> incidentPrimaryRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_PRIMARY_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, incidentPrimaryRaceDetails.get(i).getPrimaryRace());
                preparedStatement.setInt(2, incidentPrimaryRaceDetails.get(i).getRaceId());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return incidentPrimaryRaceDetails.size();
            }
        });
    }

    public int[] submittedForceLocationsForIncident(List<IncidentForceLocation> incidentForceLocationList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_FORCE_LOCATION_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, incidentForceLocationList.get(i).getForceId());
                preparedStatement.setString(2, incidentForceLocationList.get(i).getForceLocation());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return incidentForceLocationList.size();
            }
        });
    }

    public int[] submitIncidentInjuryDetails(List<Injury> civilainsInjuryDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_INJURIES, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, civilainsInjuryDetails.get(i).getInjuryLevel());
                preparedStatement.setString(2, civilainsInjuryDetails.get(i).getInjuryMedicalAid());
                preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.setInt(4, civilainsInjuryDetails.get(i).getCivilianId());
                preparedStatement.setInt(5, civilainsInjuryDetails.get(i).getOfficerId());
                preparedStatement.setString(6, civilainsInjuryDetails.get(i).getInjuryOffCiv());
            }
            @Override
            public int getBatchSize() {
                return civilainsInjuryDetails.size();
            }
        });
    }


    public int[] submitIncidentRaceDetails(List<Race> civilainsRaceDetails) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_RACE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, civilainsRaceDetails.get(i).getOfficerId());
                preparedStatement.setInt(2, civilainsRaceDetails.get(i).getCivilainsId());
                preparedStatement.setString(3, civilainsRaceDetails.get(i).getRaceOf());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return civilainsRaceDetails.size();
            }
        });
    }

    public int[] submitIncidentForceDetails(List<ForceDetails> forceDetailsOfCiviliansList) {
        return this.jdbcTemplate.batchUpdate(incidentSql.INSERT_INCIDENT_FORCE_DETAILS, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, forceDetailsOfCiviliansList.get(i).getCivilianId());
                preparedStatement.setInt(2, forceDetailsOfCiviliansList.get(i).getOfficerId());
                preparedStatement.setString(3, forceDetailsOfCiviliansList.get(i).getForceOn());
                //preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }
            @Override
            public int getBatchSize() {
                return forceDetailsOfCiviliansList.size();
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
                //ps.setString(3, officersList.get(i).getReasonForOfficerUsedForce());
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

    public int[] submitIncidentAddress(List<IncidentLocation> incidentLocations, int incidentId) {
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
