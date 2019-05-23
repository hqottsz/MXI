package com.mxi.am.stepdefn.persona.engineer.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public final class SystemSensitivitiesCreateScenarioData {

   public static final int ASSEMBLY_DB_ID = 4650;
   public static final String ASSEMBLY_CD = "ACFTMOC3";
   public static final String ASSEMBLY_NAME = "ACFTMOC3 (Aircraft MOC 3)";
   public static final String SUB_CONFIG_SLOT_XPATH =
         "//tr[@id='4650:ACFTMOC3:1']//*[@type='RADIO']";
   public static final List<String> ENABLED_SENSITIVITIES = Arrays.asList( "SS4", "SS5", "SS6" );
   public static final String CONFIG_SLOT_NAME = UUID.randomUUID().toString();
   public static final String CONFIG_SLOT_ATA = UUID.randomUUID().toString();
   public static final String CONFIG_SLOT_CLASS = "SYS (System)";
   public static final String POSITIONS_PER_PARENT = "2";


   private SystemSensitivitiesCreateScenarioData() {
   }
}
