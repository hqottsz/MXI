package com.mxi.am.stepdefn.persona.lineplanner.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public final class ViewSensitivityAlertsScenarioData {

   public static final String WORK_PACKAGE_NAME = "SSP-ALERT-WP";
   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAULT_DESC = UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "PILOT";
   public static final String USERNAME = "mxi";
   public static final String FAULT_STATUS = "CFACTV";
   public static final int AIRCRAFT_ASSEMBLY_DB_ID = 4650;
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFT_LP1";
   public static final String AIRCRAFT_PART_NO = "ACFT_P_LP1";
   public static final String AIRCRAFT_SERIAL_NO = "LP-SSP-1";
   public static final String AIRCRAFT_FAILED_SYSTEM_SDESC = "SYS-1 - Aircraft System 1";
   public static final String AIRCRAFT_FAILED_SYSTEM_NAME = "Aircraft System 1";
   public static final String WORK_PACKAGE_STATUS = "IN WORK";
   public static final List<String> ACTIVE_SENSITIVITIES = Arrays.asList( "VSA1", "VSA2", "VSA3" );


   private ViewSensitivityAlertsScenarioData() {
   }

}
