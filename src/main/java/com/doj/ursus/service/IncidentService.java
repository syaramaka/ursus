package com.doj.ursus.service;

import com.doj.ursus.model.IncidentCoreDetails;

public interface IncidentService {

    public IncidentCoreDetails createIncident(IncidentCoreDetails details);

    public IncidentCoreDetails getIncidentDetails(int incidentId);
}
