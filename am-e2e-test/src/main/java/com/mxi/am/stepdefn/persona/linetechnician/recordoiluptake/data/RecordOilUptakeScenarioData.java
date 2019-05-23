package com.mxi.am.stepdefn.persona.linetechnician.recordoiluptake.data;

import java.util.Arrays;
import java.util.List;

import com.mxi.am.driver.web.lmoc.recordoiluptake.model.RecordOilUptakeFormInput;


public final class RecordOilUptakeScenarioData {

   public static final String AIRCRAFT_REG_CODE = "ROU-1";
   public static final String DATE = "2/22/2018";
   public static final String HOURS = "10";
   public static final String MINUTES = "30";
   public static final String NOTES = "This is a Note";
   public static final List<String> OILUPTAKE_VALUES = Arrays.asList( "2.5", "3", "3" );
   public static final String PASSWORD = "password";
   public static final String SUCCESS_MESSAGE = "Successfully recorded oil uptake.";

   public static final RecordOilUptakeFormInput FORM_DATA = new RecordOilUptakeFormInput(
         AIRCRAFT_REG_CODE, DATE, HOURS, MINUTES, NOTES, OILUPTAKE_VALUES, PASSWORD );


   private RecordOilUptakeScenarioData() {
   }

}
