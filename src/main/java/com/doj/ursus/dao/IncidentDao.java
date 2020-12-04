package com.doj.ursus.dao;

import com.doj.ursus.model.Incident;
import com.doj.ursus.model.Incidents;

public interface IncidentDao {

    public Incident createIncident(Incident details);

    public Incident getIncidentDetails(int incidentId);

    public Incidents createBulkIncidents(Incidents incidents);

}
