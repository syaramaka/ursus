package com.doj.ursus.util;

import com.doj.ursus.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncidentTestData {

    public IncidentCoreDetails getIncident(IncidentCoreDetails details)
    {
        IncidentAddressDetails incidentAddressDetails = new IncidentAddressDetails();
        IncidentDemographics incidentDemographics = new IncidentDemographics();

        details.setAgencyORI("CA384000");
        details.setCaseNumber("1234567");
        details.setCiviliansCount(4);
        details.setCustodyEventOptions("Booked-No Charge filed");
        details.setIncidentReason("Local Fight");
        details.setIsArrestMade("Y");
        details.setIsCrimeReportFiled("Y");
        details.setOfficersCount(2);
        details.setPrimaryAgency("DOJ");
        details.setScenario("local fight..two offiers, 4 civilains");
        incidentAddressDetails.setIncidentLocations(getIncidentLocationList());
        details.setIncidentAddressDetails(incidentAddressDetails);
        incidentDemographics.setCiviliansList(getCivilianList());
        incidentDemographics.setOfficersList(getOfficersInIncident());
        details.setIncidentDemographics(incidentDemographics);
        return details;
    }
    public List<Civilians> getCivilianList()
    {
        List<Civilians> civiliansList = new ArrayList<>();

        Civilians civilians = new Civilians();
        civilians.setAge(36);
        civilians.setAssaultedOfficer(true);
        civilians.setCivilianNumber(1);
        civilians.setConfirmedArmed(false);
        List<String> weapons = new ArrayList<>();
        weapons.add("Firearm");
        weapons.add("Knife Blade Stabbing");
        civilians.setConfirmedArmedWeapon(weapons);
        civilians.setCustodyStatus(" In Custody (WI 5150)");

        List<Integer> officerIds = new ArrayList<>();
        officerIds.add(1);
        //officerIds.add(2);
        civilians.setEngagedOfficers(officerIds);

        List<String> civilianFireArms = new ArrayList<>();
        civilianFireArms.add("Shotgun");
        civilianFireArms.add("Knife");
        civilians.setFireArms(civilianFireArms);
        List<String> forceLocation1 = new ArrayList<>();
        forceLocation1.add("Head");
        forceLocation1.add("Arms/hands");
        List<String> forceType1 = new ArrayList<>();
        forceType1.add("Impact projectile");
        forceType1.add("Threat of firearm");
        ForceDetails details1 = new ForceDetails();
        details1.setForceLocation(forceLocation1);
        details1.setForceType(forceType1);
        civilians.setForceDetails(details1);
        civilians.setGender(Gender.Male);
        civilians.setHighestCharge("TEST Punishment");
        civilians.setRace("American-Indian");
        civilians.setInjured(true);
        Injury injury = new Injury();
        List<String> injuryType = new ArrayList<>();
        injuryType.add("Unconscious");
        injuryType.add("Internal injury");
        injury.setInjuryType(injuryType);
        injury.setInjuryMedicalAid("2");
        injury.setInjuryLevel(1);
        civilians.setInjuryDetails(injury);
        civilians.setInjuryFromPreExisting(false);
        civilians.setK12Type("Student");
        civilians.setMentalStatus("Signs of drug impairment ");
        civilians.setOrderOfForceSpecified(true);
        civilians.setPerceivedArmed(true);
        List<String> weaponTypes = new ArrayList<>();
        weaponTypes.add("Other Dangerous weapon");
        weaponTypes.add("Unknown");
        civilians.setPerceivedWeaponType(weaponTypes); //-- BA has to confirm value
        Race race = new Race();
        List<String> primaryRaceList = new ArrayList<>();
        primaryRaceList.add("black");
        primaryRaceList.add("american_indian");
        race.setPrimaryRaceType(primaryRaceList);
        civilians.setResisted(false);
        List<String> resistanceTypeList = new ArrayList<>();
        resistanceTypeList.add(" Assaultive");
        resistanceTypeList.add("Fleeing");
        civilians.setResistanceType(resistanceTypeList);
        civilians.setRaceDetails(race);

        // second element

        Civilians civilians1 = new Civilians();
        civilians1.setAge(36);
        civilians1.setAssaultedOfficer(true);
        civilians1.setCivilianNumber(2);
        civilians1.setConfirmedArmed(true);

        List<String> weapons1 = new ArrayList<>();
        weapons1.add("Firearm");
        weapons1.add("Other Dangerous weapon");
        civilians1.setConfirmedArmedWeapon(weapons1);

        civilians1.setCustodyStatus(" In Custody (WI 5150)");
        //civilians1.setEngagedOfficer("2");

        List<String> civilianFireArms1 = new ArrayList<>();
        civilianFireArms1.add("Rifle");
        civilianFireArms1.add("Handgun");
        civilians1.setFireArms(civilianFireArms1);

        List<String> forceLocation = new ArrayList<>();
        forceLocation.add(" Rear upper torso/back");
        forceLocation.add(" Front lower torso/abdomen");
        List<String> forceType = new ArrayList<>();
        forceType.add(" Blunt / impact weapon");
        forceType.add(" Impact projectile");
        ForceDetails details = new ForceDetails();
        details.setForceLocation(forceLocation);
        details.setForceType(forceType);
        civilians1.setForceDetails(details);
        civilians1.setGender(Gender.Male);
        civilians1.setHighestCharge("TEST Punishment");
        civilians1.setInjured(true);
        Injury injury1 = new Injury();
        List<String> injuryType1 = new ArrayList<>();
        injuryType1.add("Abrasion/Laceration");
        injuryType1.add("Bone fracture");
        injury1.setInjuryType(injuryType1);
        injury1.setInjuryMedicalAid("2");
        injury1.setInjuryLevel(2);
        civilians1.setInjuryDetails(injury1);
        civilians1.setInjuryFromPreExisting(false);
        civilians1.setK12Type("Student");
        civilians1.setMentalStatus("Signs of drug impairment ");
        civilians1.setOrderOfForceSpecified(true);
        civilians1.setPerceivedArmed(true);
        List<String> weaponTypes1 = new ArrayList<>();
        weaponTypes1.add("Knife Blade Stabbing");
        weaponTypes1.add("Firearm");
        civilians1.setPerceivedWeaponType(weaponTypes1); //-- BA has to confirm value
        civilians1.setRace("American-Indian");
        Race race1 = new Race();
        List<String> primaryRaceList1 = new ArrayList<>();
        primaryRaceList1.add("Asian");
        primaryRaceList1.add("White");
        List<String> asianRaceType1 = new ArrayList<>();
        asianRaceType1.add("Filipino");
        asianRaceType1.add("cambodian");
        race1.setAsianRaceType(asianRaceType1);
        race1.setPrimaryRaceType(primaryRaceList1);
        civilians1.setRaceDetails(race1);
        List<String> resistanceTypeList1 = new ArrayList<>();
        resistanceTypeList1.add(" Active resistance");
        resistanceTypeList1.add(" Life-threatening ");
        civilians1.setResistanceType(resistanceTypeList1);

        List<Integer> officerIds1 = new ArrayList<>();
        officerIds1.add(2);
        civilians1.setEngagedOfficers(officerIds1);
        civiliansList.add(civilians1);

        civiliansList.add(civilians);

        return civiliansList;
    }

    public List<Officers> getOfficersInIncident()
    {
        List<Officers> officersList = new ArrayList<>();
        Officers officers = new Officers();
        officers.setOfficerOrder(1);
        officers.setOfficerUsedForce(true);

        List<String> reasonForOfficerUsedForce = new ArrayList<>();
        reasonForOfficerUsedForce.add("To overcome resistance");
        reasonForOfficerUsedForce.add("To prevent escape");
        officers.setReasonForOfficerUsedForce(reasonForOfficerUsedForce);

        List<String> forceLocation1 = new ArrayList<>();
        forceLocation1.add("Arms/hands");
        List<String> forceType1 = new ArrayList<>();
        forceType1.add("Threat of firearm");
        ForceDetails details1 = new ForceDetails();
        details1.setForceLocation(forceLocation1);
        details1.setForceType(forceType1);
        officers.setForceDetails(details1);

        officers.setAge(35);
        officers.setGender(Gender.Male);
        officers.setInjured(true);
        officers.setInjuryFromPreExistingCondition(true);
        officers.setOfficerAssaulted(true);
        officers.setOnDuty(true);
        officers.setDress("Patrol Uniform");
        officers.setAgency("Police Department of Sacramento");
        // officers.setEngagedCivilian("1");
        Injury injury1 = new Injury();

        List<String> injuryType1 = new ArrayList<>();
        injuryType1.add("Gunshot wound");
        injuryType1.add("Bone fracture");
        injury1.setInjuryType(injuryType1);
        injury1.setInjuryMedicalAid("1");
        injury1.setInjuryLevel(1);
        officers.setInjuryDetails(injury1);

        Race race = new Race();
        List<String> primaryRaceList = new ArrayList<>();
        primaryRaceList.add(" American Indian or Alaska Native");
        race.setPrimaryRaceType(primaryRaceList);
        officers.setRaceDetails(race);

        List<Integer> civilianIds = new ArrayList<>();
        civilianIds.add(1);
        officers.setEngagedCivilians(civilianIds);

        Officers officers1 = new Officers();
        officers1.setOfficerOrder(2);
        officers1.setOfficerUsedForce(true);

        List<String> reasonForOfficerUsedForce1 = new ArrayList<>();
        reasonForOfficerUsedForce1.add("Defense of third party");
        reasonForOfficerUsedForce1.add("To effect arrest/take into custody");
        officers1.setReasonForOfficerUsedForce(reasonForOfficerUsedForce1);

        officers1.setAge(35);
        officers1.setGender(Gender.Male);
        officers1.setInjured(false);

        Injury injury2 = new Injury();
        List<String> injuryType2 = new ArrayList<>();
        injuryType2.add("Internal injury");
        injury2.setInjuryType(injuryType2);
        injury2.setInjuryMedicalAid("1");
        injury2.setInjuryLevel(1);
        officers1.setInjuryDetails(injury2);

        officers1.setInjuryFromPreExistingCondition(true);
        officers1.setOfficerAssaulted(true);
        officers1.setOnDuty(true);
        officers1.setDress("Patrol Uniform");
        //officers1.setEngagedCivilian("2");

        Race race1 = new Race();
        List<String> primaryRaceList1 = new ArrayList<>();
        primaryRaceList1.add("White");
        race1.setPrimaryRaceType(primaryRaceList1);
        List<String> hawaiianPacificIslanderRaces = new ArrayList<>();
        hawaiianPacificIslanderRaces.add("Hawaiian");
        race1.setHawaiianRaceType(hawaiianPacificIslanderRaces);
        officers1.setRaceDetails(race1);

        officers1.setAgency("Police Department of Natomas");

        List<Integer> civilianIds1 = new ArrayList<>();
        civilianIds1.add(2);
        officers1.setEngagedCivilians(civilianIds1);

        officersList.add(officers);
        officersList.add(officers1);
        return officersList;
    }

    public List<IncidentLocation> getIncidentLocationList()
    {
        List<IncidentLocation> incidentLocations = new ArrayList<>();
        IncidentLocation incidentLocation1 = new IncidentLocation();

        incidentLocation1.setCity("Sacramento");
        incidentLocation1.setCounty("Natomas");
        incidentLocation1.setIncidentAddressSequence(1);
        incidentLocation1.setIsOnK12Campus("Y");
        incidentLocation1.setLatitude(38.581573);
        incidentLocation1.setLongitude(-121.494400);
        incidentLocation1.setStreetNo("4500");
        incidentLocation1.setLocationDetails("4500 Truxel Rd");
        incidentLocation1.setState("CA");
        incidentLocation1.setStreetName("Truxel Rd");
        incidentLocation1.setZip("95834");
        incidentLocations.add(incidentLocation1);

        IncidentLocation incidentLocation2 = new IncidentLocation();
        incidentLocation2.setCity("Sacramento");
        incidentLocation2.setCounty("Northgate Blvd");
        incidentLocation2.setIncidentAddressSequence(2);
        incidentLocation2.setStreetNo("4500");
        incidentLocation2.setIsOnK12Campus("Y");
        incidentLocation2.setLatitude(38.581573);
        incidentLocation2.setLongitude(-121.494400);
        incidentLocation2.setLocationDetails("466, Sanator Ave");
        incidentLocation2.setState("CA");
        incidentLocation2.setStreetName("Sanator Ave");
        incidentLocation2.setZip("95833");
        incidentLocations.add(incidentLocation2);

        return incidentLocations;
    }
}
