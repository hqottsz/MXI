package com.mxi.am.stepdefn.persona.engineer.data;

import java.util.Arrays;
import java.util.List;


public final class SystemSensitivitiesViewScenarioData {

   public static final int ASSEMBLY_DB_ID = 4650;
   public static final String ASSEMBLY_CD = "ACFTMOC4";
   public static final String ASSEMBLY_BOM_NAME = "Aircraft System 1";
   public static final String ASSEMBLY_NAME = "ACFTMOC4 (Aircraft MOC 4)";
   public static final String CONFIG_SLOT = "SYS-1 (Aircraft System 1)";
   public static final String CONFIG_SLOT_CLASS = "SYS (System)";
   public static final List<String> ACTIVE_SENSITIVITIES = Arrays.asList( "SS1", "SS2", "SS3" );
   public static final List<String> ENABLED_SENSITIVITIES = Arrays.asList( "SS1", "SS3" );
   public static final List<String> ENABLED_SENSITIVITIES_BOOLEAN =
         Arrays.asList( "true", "false", "true" );


   private SystemSensitivitiesViewScenarioData() {
   }

}
