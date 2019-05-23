
package com.mxi.am.stepdefn.persona.linetechnician.resolutionconfigslot.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public final class LogAndCloseFaultScenarioData {

   public static final String AIRCRAFT_REG_CD = "LT-RCS-1";
   public static final String FAULT_NAME = "Log and Close - " + UUID.randomUUID().toString();
   public static final String FAILED_SYSTEM = "SYS-LT5-2 - Aircraft System 2";
   public static final String RESOLUTION_CONFIG_SLOT = "SYS-LT5-1 - Aircraft System 1";
   public static final List<String> EXPECTED_RESOLUTION_CONFIG_SLOT_SUGGESTIONS =
         Arrays.asList( "SYS-LT5-1 - Aircraft System 1", "SYS-LT5-2 - Aircraft System 2" );
   public static final String RESOLUTION_CONFIG_SLOT_PARTIAL_STRING = "SYS";
   public static final String CORRECTIVE_ACTION = "Test description Corrective Action";


   private LogAndCloseFaultScenarioData() {
   }

}
