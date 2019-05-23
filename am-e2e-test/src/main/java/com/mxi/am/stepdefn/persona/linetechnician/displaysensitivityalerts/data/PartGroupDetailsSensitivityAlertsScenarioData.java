package com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data;

import java.util.Arrays;
import java.util.List;


public class PartGroupDetailsSensitivityAlertsScenarioData {

   public static final int ASSMBL_DB_ID = 4650;
   public static final String AIRCRAFT_ASSEMBLY_CODE = "ACFT_LT8";
   public static final String AIRCRAFT_PART_NO = "ACFT_P_LT8";
   public static final String AIRCRAFT_SERIAL_NO = "LT-SSP-3";
   public static final String AIRCRAFT_FAILED_SYSTEM_NAME = "Aircraft System 1";
   public static final String PART_GROUP_CODE = "ACFT-SSP-1-1-TRK";
   public static final String PART_GROUP_NAME = "SSP Part";
   public static final String PART_NO = "SSPSEARCH";
   public static final String FAULT_NAME = "SSP Search Fault";
   public static final List<String> ACTIVE_SENSITIVITIES =
         Arrays.asList( "PGDS1", "PGDS2", "PGDS3" );


   private PartGroupDetailsSensitivityAlertsScenarioData() {
   }
}
