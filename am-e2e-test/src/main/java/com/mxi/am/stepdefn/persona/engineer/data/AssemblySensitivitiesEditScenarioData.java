package com.mxi.am.stepdefn.persona.engineer.data;

import java.util.Arrays;
import java.util.List;


public final class AssemblySensitivitiesEditScenarioData {

   public static final int ASSEMBLY_DB_ID = 4650;
   public static final String ASSEMBLY_NAME = "ACFTENG2 (Aircraft ENG 2)";
   public static final String ASSEMBLY_CD = "ACFTENG2";
   public static final List<String> ACTIVE_SENSITIVITIES =
         Arrays.asList( "ASSM1", "ASSM2", "ASSM3" );
   public static final List<String> ASSEMBLY_ASSIGNED_SENSITIVITIES_INITIAL =
         Arrays.asList( "ASSM3", "ASSM2" );
   public static final String SENSITIVITY_TO_ASSIGN = "ASSM1";
   public static final String SENSITIVITY_TO_UNASSIGN = "ASSM2";
   public static final List<String> ASSEMBLY_ASSIGNED_SENSITIVITIES_FINAL =
         Arrays.asList( "ASSM3", "ASSM1" );


   private AssemblySensitivitiesEditScenarioData() {
   }
}
