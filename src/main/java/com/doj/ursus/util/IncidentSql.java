package com.doj.ursus.util;

import org.springframework.stereotype.Component;

@Component
public class IncidentSql {

    public final String GET_INCIDENT_CIVILIANS_FORCE_TYPE = "SELECT * FROM ursus.incident_force_type WHERE civilian_details_civilian_id IN (%s) and force_type_on='C'";
    public final String GET_INCIDENT_OFF_FORCE_TYPE = "SELECT * FROM ursus.incident_force_type WHERE officer_details_officer_id IN (%s) and force_type_on='O'";
    public final String GET_OFFICERS_FORCE_LOCATION_DETAILS = "SELECT * FROM ursus.incident_force_level WHERE officer_details_officer_id IN (%s) and force_level_on='O'";
    public final String GET_INCIDENT_CIVILIANS_FORCE_LOCATION = "SELECT * FROM ursus.incident_force_level WHERE civilian_details_civilian_id IN (%s) and force_level_on='C'";
    public final String GET_CIVILIANS_RESISTANCE_TYPE = "SELECT * FROM ursus.incident_resistance_type WHERE civilian_details_civilian_id IN (%s)";
    public final String GET_CIV_PERCEIVE_WPN_TYPE = "SELECT * FROM ursus.incident_perceived_weapon_type WHERE civilian_details_civilian_id IN (%s)";
    public final String GET_CIV_CONF_ARMED_WPN = "SELECT * FROM ursus.incident_confirmed_armed_weapon WHERE civilian_details_civilian_id IN (%s)";
    public final String GET_CIV_FIRE_ARMED_LIST = "SELECT * FROM ursus.firearm_type WHERE civilian_details_civilian_id IN (%s)";
    public final String GET_INCIDENT_LOCATION = "select * from ursus.incident_addresses where incident_incident_id=?";
    public final String GET_INCIDENT_CORE_DETAILS = "select * from ursus.incident where incident_id=?";
    public final String GET_INCIDENT_OFFICERS = "SELECT * FROM ursus.officer_details where incident_incident_id=?";
    public final String GET_INCIDENT_CIV = "SELECT * FROM ursus.civilian_details where incident_incident_id=?";
    public final String GET_CIV_INJURY_LIST = "SELECT * FROM ursus.incident_injury WHERE civilian_details_civilian_id IN (%s) and injury_civ_off='C'";
    public final String GET_HAWAIIAN_REACE_DETAILS = "SELECT * FROM ursus.incident_race_hawaiian_race WHERE civilian_details_civilian_id IN (%s) and hawaiian_race_for='C'";
    public final String GET_OFF_HAWAIIAN_REACE_DETAILS = "SELECT * FROM ursus.incident_race_hawaiian_race WHERE officer_details_officer_id IN (%s) and hawaiian_race_for='O'";
    public final String GET_ASIAN_RACE_DETAILS = "SELECT * FROM ursus.incident_race_asian_race WHERE civilian_details_civilian_id IN (%s) and asian_race_for='C'";
    public final String GET_OFF_ASIAN_RACE_DETAILS = "SELECT * FROM ursus.incident_race_asian_race WHERE officer_details_officer_id IN (%s) and asian_race_for='O'";
    public final String GET_PRIMARY_RACE_DETAILS = "SELECT * FROM ursus.incident_race_primary_race WHERE civilian_details_civilian_id IN (%s) and primary_race_for='C'";
    public final String GET_OFF_PRIMARY_RACE_DETAILS = "SELECT * FROM ursus.incident_race_primary_race WHERE officer_details_officer_id IN (%s) and primary_race_for='O'";
    public final String GET_OFF_INJURY_TYPE = "SELECT * FROM ursus.incident_injury_type WHERE officer_details_officer_id IN (%s) and injury_type_on='O'";
    public final String GET_OFF_CIV_FOR_CIV = "SELECT * FROM ursus.officer_civilian WHERE civilian_details_civilian_id IN (%s)";
    public final String GET_OFF_CIV_FOR_OFF = "SELECT * FROM ursus.officer_civilian WHERE officer_details_officer_id IN (%s)";

    public final String INSERT_INCIDENT_SCREENER = "INSERT INTO ursus.screener(\n" +
            "\tscreener_multiple_agencies, screener_discharge_firearm, screener_officer_usedforce, scrrener_civilian_injured_killed, screener_civilian_usedforce, screener_officer_injured_killed, is_incident)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";

    public final String INSERT_INCIDENT = "INSERT INTO ursus.incident(\n" +
            "    incident_primary_agency, incident_date, incident_arrests, incident_crime_report, incident_reason,\n" +
            "    incident_officer_count, incident_agency_ori, incident_civilian_count, change_date,\n" +
            "    incident_scenario, incident_casenumber, incident_custody_event)\n" +
            "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public final String INSERT_INCIDENT_ADDRESS = "INSERT INTO ursus.incident_addresses(\n" +
            "\t address_sequence, address_k12, address_street, address_city, address_state, address_county," +
            " address_zip, address_lat, address_long, address_location_detail, change_date, incident_incident_id)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public final String INSERT_CIVILIANS_IN_INCIDENT = "INSERT INTO ursus.civilian_details(\n" +
            "\tcivilian_count, civilian_age, civilian_status, civilian_charge_type, civilian_gender," +
            " civilian_race, civilian_injured, civilian_injury_pre_exist, civilian_assault_officer, civilian_mental_status," +
            " civilian_resisted, civilian_perceived_armed, civilian_confirmed_armed, civilian_forced, change_date," +
            " incident_incident_id)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public final String INSERT_OFFICERS_IN_INCIDENT = "INSERT INTO ursus.officer_details(\n" +
            "\t officer_sequence, officer_force_used, officer_age," +
            " officer_gender, officer_injured, officer_injury_pre_exist, officer_assaulted, officer_on_duty," +
            " officer_dress, change_date, incident_incident_id, officer_agency)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public final String INSERT_FIRMARM_DETAILS = "INSERT INTO ursus.firearm_type(\n" +
            "\t firearm_type, civilian_details_civilian_id)\n" +
            "\tVALUES (?, ?);";

    public final String INSERT_FORCE_REASON_DETAILS = "INSERT INTO ursus.force_reason(\n" +
            "\t force_reason, officer_details_officer_id)\n" +
            "\tVALUES (?, ?)";

    public final String INSERT_OFFICER_CIVILIAN_IDS = "INSERT INTO ursus.officer_civilian(\n" +
            "\t civilian_details_civilian_id, officer_details_officer_id)\n" +
            "\tVALUES (?, ?)";

    public final String INSERT_INCIDENT_INJURIES = "INSERT INTO ursus.incident_injury(\n" +
            "\t injury_level, injury_medical_aid, change_date, civilian_details_civilian_id, officer_details_officer_id, injury_civ_off)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?)";

    public final String INSERT_INCIDENT_FORCE_DETAILS = "INSERT INTO ursus.incident_force(\n" +
            "\t civilian_details_civilian_id, officer_details_officer_id, force_on)\n" +
            "\tVALUES (?, ?, ?)";

    public final String INSERT_INCIDENT_FORCE_LOCATION_DETAILS = "INSERT INTO ursus.incident_force_level(\n" +
            "\t force_location, civilian_details_civilian_id, officer_details_officer_id, force_level_on)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_INCIDENT_FORCE_TYPE_DETAILS = "INSERT INTO ursus.incident_force_type(\n" +
            "\t force_type, civilian_details_civilian_id, officer_details_officer_id, force_type_on)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_INCIDENT_RACE_DETAILS = "INSERT INTO ursus.incident_race(\n" +
            "\t officer_details_officer_id, civilian_details_civilian_id, race_of)\n" +
            "\tVALUES (?, ?, ?)";

    public final String INSERT_INCIDENT_PRIMARY_RACE_DETAILS = "INSERT INTO ursus.incident_race_primary_race(\n" +
            "\t primary_race, civilian_details_civilian_id, officer_details_officer_id, primary_race_for)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_INCIDENT_ASIAN_RACE_DETAILS = "INSERT INTO ursus.incident_race_asian_race(\n" +
            "\t asian_race, civilian_details_civilian_id, officer_details_officer_id, asian_race_for)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_INCIDENT_HAWAIIAN_RACE_DETAILS = "INSERT INTO ursus.incident_race_hawaiian_race(\n" +
            "\t hawaiian_race, civilian_details_civilian_id, officer_details_officer_id, hawaiian_race_for)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_INCIDENT_INJURY_TYPE_DETAILS = "INSERT INTO ursus.incident_injury_type(\n" +
            "\t injury_type, civilian_details_civilian_id, officer_details_officer_id, injury_type_on)\n" +
            "\tVALUES (?, ?, ?, ?)";

    public final String INSERT_PERCEIVED_WEAPON_TYPE_DETAILS = "INSERT INTO ursus.incident_perceived_weapon_type(\n" +
            "\t perceived_weapon, civilian_details_civilian_id)\n" +
            "\tVALUES (?, ?)";

    public final String INSERT_CONFIRMED_ARMED_WEAPON_DETAILS = "INSERT INTO ursus.incident_confirmed_armed_weapon(\n" +
            "\t conf_armed_weapon, civilian_details_civilian_id)\n" +
            "\tVALUES (?, ?)";

    public final String INSERT_INCIDENT_RESISTANCE_TYPE_DETAILS = "INSERT INTO ursus.incident_resistance_type(\n" +
            "\t resistance_type, civilian_details_civilian_id)\n" +
            "\tVALUES (?, ?)";

    public final String GET_OFFICERS_FORCE_REASON = "SELECT * FROM ursus.force_reason WHERE officer_details_officer_id IN (%s)";









}
