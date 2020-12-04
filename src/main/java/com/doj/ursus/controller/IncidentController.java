package com.doj.ursus.controller;

import com.doj.ursus.impl.ScreenerDaoImpl;
import com.doj.ursus.model.Incident;
import com.doj.ursus.model.Incidents;
import com.doj.ursus.model.Screener;
import com.doj.ursus.service.IncidentService;
import com.doj.ursus.util.IncidentTestData;
import com.doj.ursus.util.ScreenTestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class IncidentController {

    @Autowired
    ScreenerDaoImpl screenerDao;

    @Autowired
    IncidentTestData testData;

    @Autowired
    ScreenTestData screenTestData;

    @Autowired
    IncidentService incidentService;

    private final Logger logger = LoggerFactory.getLogger(IncidentController.class);

    private static final String INCIDENTS_XML = "C:\\Users\\sunee\\OneDrive\\Desktop\\Qualapps Work\\incidents.xml";

    @GetMapping(value = "/incident/{incidentId}")
    public Incident getIncident(@PathVariable("incidentId") int incidentId)
    {
        logger.info("get Incident details for given incident Id:"+incidentId);
        return incidentService.getIncidentDetails(incidentId);
    }

    @GetMapping(value = "/create/incident")
    public Incident createIncident(Incident details) {
        logger.debug("Getting all files details from the database.");
        Incident incident = new Incident();
        incident = testData.getIncidentData(incident);
        return incidentService.createIncident(incident);
    }

    @GetMapping(value = "/upload/bulk/incident")
    public Incidents createJson(Incidents incidents)
    {
        Incidents incidents1 = new Incidents();
        Incident incident = new Incident();
        List<Incident> incidentList = new ArrayList<>();
        Screener screener = new Screener();
        screener = screenTestData.getScreenData(screener);
        incident = testData.getIncidentData(incident);
        incident.setScreener(screener);
        incidentList.add(incident);
        incidents1.setIncident(incidentList);
        //
        try {
            convertObjectToXML(incidents1);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return incidents1;
    }

    public void convertObjectToXML(Incidents incidents) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Incidents.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(incidents, System.out);
        m.marshal(incidents, new File(INCIDENTS_XML));
    }

}
