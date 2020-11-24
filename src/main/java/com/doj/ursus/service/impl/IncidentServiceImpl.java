package com.doj.ursus.service.impl;

import com.doj.ursus.dao.IncidentDao;
import com.doj.ursus.dao.ScreenerDao;
import com.doj.ursus.model.IncidentCoreDetails;
import com.doj.ursus.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    IncidentDao incidentDao;

    @Override
    public IncidentCoreDetails createIncident(IncidentCoreDetails details) {
        return incidentDao.createIncident(details);
    }

    @Override
    public IncidentCoreDetails getIncidentDetails(int incidentId) {
        return incidentDao.getIncidentDetails(incidentId);
    }
}
