package com.doj.ursus.controller;

import com.doj.ursus.impl.ScreenerDaoImpl;
import com.doj.ursus.model.*;
import com.doj.ursus.service.IncidentService;
import com.doj.ursus.service.ScreenerService;
import com.doj.ursus.util.Gender;
import com.doj.ursus.util.IncidentTestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncidentController {

    @Autowired
    ScreenerDaoImpl screenerDao;

    @Autowired
    IncidentTestData testData;

    @Autowired
    IncidentService incidentService;

    private final Logger logger = LoggerFactory.getLogger(IncidentController.class);

    @GetMapping(value = "/incident/{incidentId}")
    public IncidentCoreDetails getIncident(@PathVariable("incidentId") int incidentId)
    {
        logger.info("get Incident details for given incident Id:"+incidentId);
        IncidentCoreDetails incidentCoreDetails = new IncidentCoreDetails();
        return incidentService.getIncidentDetails(incidentId);
    }

    @GetMapping(value = "/create/incident")
    public IncidentCoreDetails createIncident(IncidentCoreDetails details) {
        logger.debug("Getting all files details from the database.");
        IncidentCoreDetails coreDetails = new IncidentCoreDetails();
        coreDetails = testData.getIncident(details);
        return incidentService.createIncident(coreDetails);
    }

}
