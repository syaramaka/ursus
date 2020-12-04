package com.doj.ursus.service.impl;

import com.doj.ursus.dao.IncidentDao;
import com.doj.ursus.model.Incident;
import com.doj.ursus.model.Incidents;
import com.doj.ursus.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    IncidentDao incidentDao;

    @Override
    public Incident createIncident(Incident details) {
        return incidentDao.createIncident(details);
    }

    @Override
    public Incident getIncidentDetails(int incidentId) {
        return incidentDao.getIncidentDetails(incidentId);
    }

    @Override
    public Incidents createBulkIncidents(Incidents incidents) {
        return incidentDao.createBulkIncidents(incidents);
    }
}
