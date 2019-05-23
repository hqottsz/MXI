package com.mxi.am.stepdefn.persona.maintenancecontroller.phoneupdeferral.data;

import java.util.UUID;

import com.mxi.am.driver.web.lmoc.phoneupdeferral.model.PhoneUpDeferralFormInput;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.model.DueTableRow;


public final class PhoneUpDeferralScenarioData {

   public static final int CDY_INTERVAL = 2;
   public static final int CYCLES_INTERVAL = 200;
   public static final double HOURS_INTERVAL = 100.00;

   public static final String AC_NO = "PUD-AUTH-1";
   public static final String[] CAPABILITY_REMINDERS = new String[] {
         "Extended Operations capability level to NO_ETOPS.", "WIFI capability level to NO." };
   public static final String DEFERRAL_REFERENCE_NAME = "PUD-AUTH-1-DEF";
   public static final String DRIVING_DEADLINE = String.valueOf( CDY_INTERVAL ).concat( " Days" );
   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "Pilot";
   public static final String FOUND_BY_USER = "MCTEST";
   public static final String LOGBOOK_DESCRIPTION = FAULT_NAME + "\n Logbook fault description";
   public static final String LOGBOOK_REFERENCE = UUID.randomUUID().toString();
   public static final String OPERATIONAL_RESTRICTIONS_DESC = "An operational restriction.";
   public static final String ORIGINATING_FLIGHT = "Planned Flight PUD-AUTH-1";
   public static final String SUCCESS_DIALOG_TITLE = "Deferral Successfully Authorized";

   public static final DueTableRow CDY_ROW =
         new DueTableRow( true, "CDY", String.valueOf( CDY_INTERVAL ) );
   public static final DueTableRow CYCLES_ROW =
         new DueTableRow( false, "CYCLES", String.valueOf( CYCLES_INTERVAL ) );
   public static final DueTableRow HOURS_ROW =
         new DueTableRow( false, "HOURS", String.format( "%.2f", HOURS_INTERVAL ) );

   public static final String TEAR_DOWN_QUERY =
         "UPDATE evt_event SET evt_event.event_status_cd = 'CANCEL', evt_event.hist_bool = 1 WHERE evt_event.alt_id "
               + "IN ( SELECT evt_event.alt_id FROM evt_event INNER JOIN ( SELECT task_event.event_db_id, task_event.event_id "
               + "FROM evt_event task_event INNER JOIN evt_event_rel ON evt_event_rel.event_db_id = task_event.event_db_id "
               + "AND evt_event_rel.event_id = task_event.event_id INNER JOIN evt_event fault_event ON "
               + "fault_event.event_db_id = evt_event_rel.rel_event_db_id AND fault_event.event_id = evt_event_rel.rel_event_id "
               + "WHERE fault_event.event_sdesc = ? AND task_event.event_status_cd = 'ACTV' AND evt_event_rel.rel_type_db_id = 0 "
               + "AND evt_event_rel.rel_type_cd = 'RECSRC' ) parent_event ON parent_event.event_db_id = evt_event.h_event_db_id "
               + "AND parent_event.event_id = evt_event.h_event_id )";

   public static final PhoneUpDeferralFormInput FORM_DATA =
         new PhoneUpDeferralFormInput( AC_NO, DEFERRAL_REFERENCE_NAME, LOGBOOK_DESCRIPTION,
               LOGBOOK_REFERENCE, FOUND_BY_USER, FAULT_SOURCE, ORIGINATING_FLIGHT );


   private PhoneUpDeferralScenarioData() {
   }

}
