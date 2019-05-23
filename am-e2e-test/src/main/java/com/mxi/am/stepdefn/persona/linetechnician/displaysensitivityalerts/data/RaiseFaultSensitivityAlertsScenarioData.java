package com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data;

import java.util.Arrays;
import java.util.List;


public final class RaiseFaultSensitivityAlertsScenarioData {

   public static final int ASSMBL_DB_ID = 4650;
   public static final String AIRCRAFT_ASSY_CD = "ACFT_LT3";
   public static final String AIRCRAFT_PART_NO = "ACFT_P_LT3";
   public static final String AIRCRAFT_SERIAL_NO = "LT-SSP-1";
   public static final String AIRCRAFT_FAILED_SYSTEM_NAME = "Aircraft System 1";
   public static final List<String> ACTIVE_SENSITIVITIES =
         Arrays.asList( "RFSA1", "RFSA2", "RFSA3" );


   private RaiseFaultSensitivityAlertsScenarioData() {
   }

}
