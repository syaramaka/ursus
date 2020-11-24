package com.doj.ursus.service.impl;

import com.doj.ursus.dao.ScreenerDao;
import com.doj.ursus.model.Screener;
import com.doj.ursus.service.ScreenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScreenerServiceImpl implements ScreenerService {

    @Autowired
    ScreenerDao screenerDao;

    @Override
    public Screener createIncident(Screener screener) {
        return screenerDao.createIncident(screener);
    }
}
