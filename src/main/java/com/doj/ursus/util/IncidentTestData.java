package com.doj.ursus.util;

import com.doj.ursus.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncidentTestData {

    @Autowired
    ScreenTestData screenTestData;

    public Incident getIncidentData(Incident incident)
    {
        Incident incident1 = new Incident();
        incident1.setAgencyORI("CA384000");
        incident1.setCaseNumber("1234567");
        incident1.setCiviliansCount(4);
        incident1.setCustodyEventOptions("Booked-No Charge filed");
        incident1.setIncidentReason("Local Fight");
        incident1.setIsArrestMade("Y");
        incident1.setIsCrimeReportFiled("Y");
        incident1.setOfficersCount(2);
        incident1.setPrimaryAgency("DOJ");
        incident1.setScenario("local fight..two offiers, 4 civilains");
        Screener screener = new Screener();
        incident1.setScreener(screenTestData.getScreenData(screener));
        incident1.setAddresses(getAddresses());
        incident1.setCivilians(getCivilianList());
        incident1.setOfficers(getOfficersInIncident());
        return incident1;
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
        civilians.setEngagedOfficers(officerIds);
        List<String> civilianFireArms = new ArrayList<>();
        civilianFireArms.add("Shotgun");
        civilianFireArms.add("Knife");
        civilians.setFireArms(civilianFireArms);
        List<String> forceLoationList = new ArrayList<>();
        forceLoationList.add("Head");
        forceLoationList.add("Arms/hands");
        civilians.setForceLocation(forceLoationList);
        List<String> forceTypeList = new ArrayList<>();
        forceTypeList.add("Impact projectile");
        forceTypeList.add("Threat of firearm");
        civilians.setForceType(forceTypeList);
        civilians.setGender(Gender.Male);
        civilians.setHighestCharge("TEST Punishment");
        civilians.setRace("American-Indian");
        civilians.setInjured(true);
        List<String> injuryTypeList = new ArrayList<>();
        injuryTypeList.add("Unconscious");
        injuryTypeList.add("Internal injury");
        civilians.setInjuryType(injuryTypeList);
        civilians.setInjuryMedicalAid("2");
        civilians.setInjuryLevel(1);
        civilians.setInjuryFromPreExisting(false);
        civilians.setK12Type("Student");
        civilians.setMentalStatus("Signs of drug impairment ");
        civilians.setOrderOfForceSpecified(true);
        civilians.setPerceivedArmed(true);
        List<String> weaponTypes = new ArrayList<>();
        weaponTypes.add("Other Dangerous weapon");
        weaponTypes.add("Unknown");
        civilians.setPerceivedWeaponType(weaponTypes); //-- BA has to confirm value
        List<String> primaryRaceList = new ArrayList<>();
        primaryRaceList.add("black");
        primaryRaceList.add("american_indian");
        civilians.setPrimaryRaceType(primaryRaceList);
        civilians.setResisted(false);
        List<String> resistanceTypeList = new ArrayList<>();
        resistanceTypeList.add(" Assaultive");
        resistanceTypeList.add("Fleeing");
        civilians.setResistanceType(resistanceTypeList);
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
        List<String> civilianFireArms1 = new ArrayList<>();
        civilianFireArms1.add("Rifle");
        civilianFireArms1.add("Handgun");
        civilians1.setFireArms(civilianFireArms1);
        List<String> forceLoationList1 = new ArrayList<>();
        forceLoationList1.add(" Rear upper torso/back");
        forceLoationList1.add(" Front lower torso/abdomen");
        civilians1.setForceLocation(forceLoationList1);
        List<String> forceTypeList1 = new ArrayList<>();
        forceTypeList1.add(" Blunt / impact weapon");
        forceTypeList1.add(" Impact projectile");
        civilians1.setForceType(forceTypeList1);
        civilians1.setGender(Gender.Male);
        civilians1.setHighestCharge("TEST Punishment");
        civilians1.setInjured(true);
        List<String> injuryType1 = new ArrayList<>();
        injuryType1.add("Abrasion/Laceration");
        injuryType1.add("Bone fracture");
        civilians1.setInjuryType(injuryType1);
        civilians1.setInjuryMedicalAid("2");
        civilians1.setInjuryLevel(2);
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
        List<String> primaryRaceList1 = new ArrayList<>();
        primaryRaceList1.add("Asian");
        primaryRaceList1.add("White");
        civilians1.setPrimaryRaceType(primaryRaceList1);
        List<String> asianRaceType1 = new ArrayList<>();
        asianRaceType1.add("Filipino");
        asianRaceType1.add("cambodian");
        civilians1.setAsianRaceType(asianRaceType1);
        List<String> resistanceTypeList1 = new ArrayList<>();
        resistanceTypeList1.add(" Active resistance");
        resistanceTypeList1.add(" Life-threatening ");
        civilians1.setResistanceType(resistanceTypeList1);
        List<Integer> officerIds1 = new ArrayList<>();
        officerIds1.add(2);
        civilians1.setEngagedOfficers(officerIds1);
        civiliansList.add(civilians);
        civiliansList.add(civilians1);
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
        officers.setForceLocation(forceLocation1);
        List<String> forceType1 = new ArrayList<>();
        forceType1.add("Threat of firearm");
        officers.setForceType(forceType1);
        officers.setAge(35);
        officers.setGender(Gender.Male);
        officers.setInjured(true);
        officers.setInjuryFromPreExistingCondition(true);
        officers.setOfficerAssaulted(true);
        officers.setOnDuty(true);
        officers.setDress("Patrol Uniform");
        officers.setAgency("Police Department of Sacramento");
        List<String> injuryType1 = new ArrayList<>();
        injuryType1.add("Gunshot wound");
        injuryType1.add("Bone fracture");
        officers.setInjuryType(injuryType1);
        officers.setInjuryMedicalAid("1");
        officers.setInjuryLevel(1);
        List<String> primaryRaceList = new ArrayList<>();
        primaryRaceList.add(" American Indian or Alaska Native");
        officers.setPrimaryRaceType(primaryRaceList);
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
        List<String> injuryType2 = new ArrayList<>();
        injuryType2.add("Internal injury");
        officers1.setInjuryType(injuryType2);
        officers1.setInjuryMedicalAid("1");
        officers1.setInjuryLevel(1);
        officers1.setInjuryFromPreExistingCondition(true);
        officers1.setOfficerAssaulted(true);
        officers1.setOnDuty(true);
        officers1.setDress("Patrol Uniform");
        List<String> primaryRaceList1 = new ArrayList<>();
        primaryRaceList1.add("White");
        officers1.setPrimaryRaceType(primaryRaceList1);
        List<String> hawaiianPacificIslanderRaces = new ArrayList<>();
        hawaiianPacificIslanderRaces.add("Hawaiian");
        officers1.setHawaiianRaceType(hawaiianPacificIslanderRaces);
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

    public List<Address> getAddresses()
    {
        Address address = new Address();
        Address address1 = new Address();
        List<Address> addresses = new ArrayList<>();

        address.setCity("Sacramento");
        address.setCounty("Natomas");
        address.setIncidentAddressSequence(1);
        address.setIsOnK12Campus("Y");
        address.setLatitude(38.581573);
        address.setLongitude(-121.494400);
        address.setStreetNo("4500");
        address.setLocationDetails("4500 Truxel Rd");
        address.setState("CA");
        address.setStreetName("Truxel Rd");
        address.setZip("95834");
        addresses.add(address);

        address1.setCity("Sacramento");
        address1.setCounty("Northgate Blvd");
        address1.setIncidentAddressSequence(2);
        address1.setStreetNo("4500");
        address1.setIsOnK12Campus("Y");
        address1.setLatitude(38.581573);
        address1.setLongitude(-121.494400);
        address1.setLocationDetails("466, Sanator Ave");
        address1.setState("CA");
        address1.setStreetName("Sanator Ave");
        address1.setZip("95833");
        addresses.add(address1);

        return addresses;
    }
}
