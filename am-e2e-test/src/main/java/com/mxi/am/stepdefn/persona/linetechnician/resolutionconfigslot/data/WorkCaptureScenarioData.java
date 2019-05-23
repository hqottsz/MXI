package com.mxi.am.stepdefn.persona.linetechnician.resolutionconfigslot.data;

import java.util.UUID;

import com.mxi.mx.core.key.RefEventStatusKey;


public final class WorkCaptureScenarioData {

   // Aircraft Data
   public static final int AIRCRAFT_ASSEMBLY_DB_ID = 4650;
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFT_LT5";
   public static final String AIRCRAFT_PART_NO = "ACFT_P_LT5";
   public static final String AIRCRAFT_SERIAL_NO = "LT-RCS-2";

   // Fault Data
   public static final String FAILED_SYSTEM_SDESC = "SYS-LT5-1 - Aircraft System 1";
   public static final String FAULT_NAME_EDIT_WORK_CAPTURE =
         "Edit Work Capture-" + UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "PILOT";
   public static final String FAULT_STATUS = "CFACTV";
   public static final String FOUND_BY_USER = "linetech";

   // Work Package Data
   public static final String WORK_PACKAGE_NAME = "RCS-WORK CAPTURE";
   public static final String WORK_PACKAGE_STATUS = RefEventStatusKey.IN_WORK.getCd();

   // Edit Work Capture Data
   public static final String RESOLUTION_CONFIG_SLOT_ATA_CODE = "SYS-LT5-1";
   public static final String RESOLUTION_CONFIG_SLOT_NAME = "Aircraft System 1";
   public static final String RESOLUTION_CONFIG_SLOT_LABEL =
         RESOLUTION_CONFIG_SLOT_ATA_CODE + " - " + RESOLUTION_CONFIG_SLOT_NAME;
   public static final String CORRECTIVE_ACTION = "Test description Corrective Action";


   private WorkCaptureScenarioData() {
   }
}
