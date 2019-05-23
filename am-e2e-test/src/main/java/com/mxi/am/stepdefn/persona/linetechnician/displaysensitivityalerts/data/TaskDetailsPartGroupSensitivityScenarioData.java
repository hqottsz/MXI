package com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data;

import java.util.Arrays;
import java.util.List;


public final class TaskDetailsPartGroupSensitivityScenarioData {

   public static final int ASSMBL_DB_ID = 4650;
   public static final String AIRCRAFT_ASSEMBLY_CODE = "ACFT_LT9";
   public static final String AIRCRAFT_PART_NO = "ACFT_P_LT9";
   public static final String AIRCRAFT_SERIAL_NO = "LT-SSP-4";
   public static final String AIRCRAFT_FAILED_SYSTEM_NAME = "Aircraft System 1";
   public static final String PART_GROUP_CODE = "ACFT-SSP-1-2-TRK";
   public static final String PART_GROUP_NAME = "SSP Part 2";
   public static final String PART_NO = "SSPCHIPS";
   public static final String FAULT_NAME = "SSP Part Group Fault";
   public static final List<String> ACTIVE_SENSITIVITIES =
         Arrays.asList( "TDPGS1", "TDPGS2", "TDPGS3" );


   private TaskDetailsPartGroupSensitivityScenarioData() {

   }
}
