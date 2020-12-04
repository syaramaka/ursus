package com.doj.ursus.controller;

import com.doj.ursus.impl.ScreenerDaoImpl;
import com.doj.ursus.model.Incident;
import com.doj.ursus.model.Incidents;
import com.doj.ursus.service.IncidentService;
import com.doj.ursus.util.IncidentTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;

@RestController
public class IncidentFileUploadController {

    @Autowired
    ScreenerDaoImpl screenerDao;

    @Autowired
    IncidentTestData testData;

    @Autowired
    IncidentService incidentService;

    private final Logger logger = LoggerFactory.getLogger(IncidentController.class);

    @PostMapping(value = "/upload/incident")
    public Incidents uploadIncidents(@RequestParam("file") MultipartFile file) {
        System.out.println(" start bulk crate employee");
        Incidents incidents = new Incidents();


        if (file.getOriginalFilename().contains(".json")) {
            try {
                System.out.println("<!---------------Generating the Java objects from Json Input-------------->");
                incidents = new ObjectMapper().readValue(file.getBytes(), Incidents.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JAXBContext context = null;
            Unmarshaller um = null;
            try {
                context = JAXBContext.newInstance(Incidents.class);
                um = context.createUnmarshaller();
                try {
                    incidents = (Incidents) um.unmarshal(file.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            System.out.println("<!---------------Generating the Java objects from XML Input-------------->");
        }
        List<Incident> incidentList = incidents.getIncident();
        for (Incident details : incidentList) {
            System.out.println("Incident Details : " + details);
        }
        return incidentService.createBulkIncidents(incidents);
    }
}
