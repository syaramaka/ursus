package com.doj.ursus.dao;

import com.doj.ursus.model.IncidentCoreDetails;

public interface IncidentDao {

    public IncidentCoreDetails createIncident(IncidentCoreDetails details);

    public IncidentCoreDetails getIncidentDetails(int incidentId);

}
