
package com.mxi.am.stepdefn.persona.maintenancecontroller.pendingdeferralrequests.data;

import java.util.UUID;

import com.mxi.mx.core.key.RefEventStatusKey;


public final class PendingReferenceApprovalsScenarioData {

   public static final String CONFIRMATION_DIALOG_TITLE = "Approve Reference";
   public static final String SUCCESS_DIALOG_TITLE = "Reference Successfully Approved";
   public static final String DEFERRAL_REFERENCE_NAME = "PDR-AUTH-1-DEF";
   public static final String DEFERRAL_NOTES = "The fault should be deferred, please.";
   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAULT_DESC = UUID.randomUUID().toString();
   public static final String FAULT_STATUS_CD = "CFACTV";
   public static final String USERNAME = "mxi";
   public static final String FAILED_SYSTEM_NAME = "SYS-1 - Aircraft System 1";
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFTMOC1";
   public static final String AIRCRAFT_PART_NO = "ACFT_ASSY_MOC1";
   public static final String AIRCRAFT_SERIAL_NO = "PDR-AUTH-1";
   public static final String FAULT_SOURCE = "MECH";
   public static final String WORK_PACKAGE_NAME = "PDR-AUTH-WP";
   public static final String WORK_PACKAGE_STATUS = RefEventStatusKey.IN_WORK.getCd();


   private PendingReferenceApprovalsScenarioData() {
   }
}
