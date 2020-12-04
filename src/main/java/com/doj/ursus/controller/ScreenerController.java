package com.doj.ursus.controller;

import com.doj.ursus.impl.ScreenerDaoImpl;
import com.doj.ursus.model.Screener;
import com.doj.ursus.service.ScreenerService;
import com.doj.ursus.util.ScreenTestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
screener class
 */
@RestController
public class ScreenerController {

    @Autowired
    ScreenerDaoImpl screenerDao;

    @Autowired
    ScreenerService screenerService;

    @Autowired
    ScreenTestData screenTestData;

    private final Logger logger = LoggerFactory.getLogger(ScreenerController.class);

    @GetMapping(value = "/incident/screener")
    public Screener createFileIncident(Screener screener) {
        Screener screenerData = new Screener();
        logger.debug("Getting all files details from the database.");
        screenerData = screenTestData.getScreenData(screener);
        return screenerService.createIncident(screenerData);
    }
}
