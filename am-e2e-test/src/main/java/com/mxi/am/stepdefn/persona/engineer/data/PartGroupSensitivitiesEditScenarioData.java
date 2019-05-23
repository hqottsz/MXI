package com.mxi.am.stepdefn.persona.engineer.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class PartGroupSensitivitiesEditScenarioData {

   public static final String PART_GROUP_NAME = "SSP Part Group";
   public static final String ASSEMBLY_CODE = "ACFTENG3";
   public static final int DB_ID = 4650;
   public static final String PART_GROUP_CODE = "ACFT-SYS-1-SSPPG";
   public static final List<String> PART_GROUP_INITIAL_SENSITIVITIES_CODES =
         Arrays.asList( "PGSENS1", "PGSENS2" );
   public static final List<String> PART_GROUP_SENSITIVITY_TO_DISABLE = Arrays.asList( "PGSENS2" );
   public static final Map<String, Boolean> PART_GROUP_INITIAL_SENSITIVITIES =
         new HashMap<String, Boolean>() {

            {
               put( "PGSENS1", true );
               put( "PGSENS2", true );
            }
         };

   public static final Map<String, Boolean> PART_GROUP_EDITED_SENSITIVITIES =
         new HashMap<String, Boolean>() {

            {
               put( "PGSENS1", true );
               put( "PGSENS2", false );
            }
         };


   private PartGroupSensitivitiesEditScenarioData() {
   }
}
