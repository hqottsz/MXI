package com.mxi.am.stepdefn.persona.engineer.data;

import java.util.Arrays;
import java.util.List;


public final class SystemSensitivitiesEditScenarioData {

   public static final int ASSEMBLY_DB_ID = 4650;
   public static final String ASSEMBLY_NAME = "ACFTENG1 (Aircraft ENG 1)";
   public static final String ASSEMBLY_CD = "ACFTENG1";
   public static final String CONFIG_SLOT_1 = "Aircraft System 1";
   public static final String CONFIG_SLOT_2 = "Aircraft System 1-1";
   public static final String CONFIG_SLOT_NAME_1 = "SYS-1 (Aircraft System 1)";
   public static final List<String> ENABLED_SENSITIVITIES = Arrays.asList( "SS7", "SS8" );
   public static final List<String> ENABLED_SENSITIVITIES_ID = Arrays.asList( "aSS7", "aSS8" );
   public static final List<String> EDITED_SENSITIVITY = Arrays.asList( "SS7" );
   public static final List<String> DISABLED_SENSITIVITIES = Arrays.asList( "SS8" );


   private SystemSensitivitiesEditScenarioData() {
   }
}
