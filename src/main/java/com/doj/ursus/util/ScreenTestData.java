package com.doj.ursus.util;

import com.doj.ursus.model.Screener;
import org.springframework.stereotype.Service;

@Service
public class ScreenTestData {

    public Screener getScreenData(Screener screener)
    {
        screener.setIsMultipleAgencies("Y");
        screener.setIsDischargeOfFirearm("Y");
        screener.setIsOfficerUsedForce("Y");
        screener.setIsCivilianSeriouslyInjured("Y");
        screener.setIsCivilianUsedForce("Y");
        screener.setIsOfficerSeriouslyInjured("Y");
        screener.setIsPrimaryAgency("N");
        screener.setPrimaryAgencyName("SAPD");
        return screener;
    }
}
