package com.doj.ursus.service;

import com.doj.ursus.model.Incident;
import com.doj.ursus.model.Incidents;

public interface IncidentService {

    public Incident createIncident(Incident details);

    public Incident getIncidentDetails(int incidentId);

    public Incidents createBulkIncidents(Incidents incidents);
}
