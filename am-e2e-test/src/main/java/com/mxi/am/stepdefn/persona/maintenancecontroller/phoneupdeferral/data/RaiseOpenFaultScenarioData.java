
package com.mxi.am.stepdefn.persona.maintenancecontroller.phoneupdeferral.data;

import java.util.UUID;

import com.mxi.am.driver.web.lmoc.phoneupdeferral.model.PhoneUpDeferralFormInput;


public final class RaiseOpenFaultScenarioData {

   public static final String AC_NO = "PUD-OPEN-1";
   public static final String AC_REG_CD = "Aircraft Part MOC 1 - PUD-OPEN-1";
   public static final String ASSEMBLY_CODE = "ACFTMOC1";
   public static final String DEFERRAL_REFERENCE_NAME = "PUD-OPEN-1-DEF";
   public static final String FAILED_SYSTEM_NAME = "SYS-1 - Aircraft System 1";
   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAULT_SEVERITY = "AOG (Aircraft is on Ground)";
   public static final String FAULT_SOURCE = "Mechanic";
   public static final String FAULT_STATUS = "OPEN (Active)";
   public static final String FOUND_BY_USER = "MCTEST";
   public static final String LOGBOOK_DESCRIPTION = FAULT_NAME + "\n Logbook fault description";
   public static final String LOGBOOK_REFERENCE = UUID.randomUUID().toString();
   public static final String OPERATIONAL_RESTRICTIONS_DESC = "An operational restriction.";
   public static final String ORIGINATING_FLIGHT = "Planned Flight PUD-OPEN-1";
   public static final String SUCCESS_DIALOG_TITLE = "Fault Successfully Raised";

   public static final PhoneUpDeferralFormInput FORM_DATA =
         new PhoneUpDeferralFormInput( AC_NO, DEFERRAL_REFERENCE_NAME, LOGBOOK_DESCRIPTION,
               LOGBOOK_REFERENCE, FOUND_BY_USER, FAULT_SOURCE, ORIGINATING_FLIGHT );


   private RaiseOpenFaultScenarioData() {
   }

}
