
package com.mxi.mx.web.query.task;

import java.text.ParseException;
import java.util.Date;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefTaskOriginatorKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Assert methods for data
 */
public class OpportunisticTaskUtil {

   /**
    * assert task #1 - Initialized
    *
    * @param aTasksDs
    *           dataset to test
    *
    * @throws ParseException
    *            if parsing fails
    */
   public static void assertInitialized( DataSet aTasksDs ) throws ParseException {

      testRow( aTasksDs, 4650, 238539, false, "01", "01", "T00017R7", "O1 (O1)",
            new InventoryKey( 4650, 390844 ), "MYACFT11 - MYACFT11",
            new InventoryKey( 4650, 390844 ), "MYACFT11 - MYACFT11", null,
            new RefWorkTypeKey( "4650:TEST" ), "TEST", true, true,
            new RefTaskOriginatorKey( 4650, "TEST" ), "TEST", "REQ", "TEST",
            new TaskKey( 4650, 133730 ), "O1 (O1)", new TaskKey( 4650, 133730 ), null, 0.0, 0.0,
            null, null, null, null, 0.0, null, new LocationKey( 4650, 1 ), "T00017R7" );
   }


   /**
    * assert task #2 - Uninitialized
    *
    * @param aTasksDs
    *           dataset to test
    */
   public static void assertUninitialized( DataSet aTasksDs ) {

      testRow( aTasksDs, 4650, 238540, false, "02", "02", null, null,
            new InventoryKey( 4650, 390846 ), "SLOT2PART2 (PN: SLOT2PART2, SN: XXX)", null, null,
            null, null, null, false, false, new RefTaskOriginatorKey( 4650, "TEST" ), "TEST", "REQ",
            "TEST", null, null, new TaskKey( 4650, 3 ), null, 0.0, 0.0, null, null, null, null, 0.0,
            null, new LocationKey( 4650, 1 ), null );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset
    * @param aTaskDbId
    *           The event key
    * @param aTaskId
    *           The inventory key
    * @param aSoftDeadline
    *           Whether a soft-deadline is allowed
    * @param aTaskName
    *           The task name
    * @param aTaskCd
    *           The user status
    * @param aBarcodeSdesc
    *           The barcode
    * @param aEventSdesc
    *           The WO number
    * @param aLinkedInv
    *           The Repair Order Number
    * @param aInvSDesc
    *           The Vendor WO Number
    * @param aParentAssemblyInventory
    *           The check name
    * @param aParentAssemblyInvSDesc
    *           The check barcode
    * @param aTaskPriorityCd
    *           The Check Key
    * @param aWorkType
    *           The driving event key
    * @param aWorkTypeCd
    *           The work type
    * @param aPartsReady
    *           The driving event desc
    * @param aToolsReady
    *           The driving event barcode
    * @param aOriginator
    *           The scheduled deadline for the event
    * @param aTaskOriginatorCd
    *           The date that the event actually ended.
    * @param aTaskClassCd
    *           The parts ready boolean
    * @param aUserSubclassCd
    *           The tools ready boolean
    * @param aWp
    *           The Work Package associated
    * @param aWpSdesc
    *           String to identify the Work-Package
    * @param aLinkedTask
    *           PK to the Opportunistic baseline
    * @param aSchedPriorityCd
    *           The Reference Code
    * @param aDeviationQt
    *           The amount by which the deadline can "slip" past its due date before being
    *           considered overdue
    * @param aUsageRemQt
    *           The difference between the current usuage count and SCHED_DEAD_QT
    * @param aSchedDeadDt
    *           The scheduled deadline for the event
    * @param aDomainTypeCd
    *           The Domain type
    * @param aEngUnitCd
    *           The Engineering Unit Reference
    * @param aDataTypeCd
    *           The Data Type
    * @param aEngUnitMultQt
    *           The multiplier
    * @param aExtSchedDeadDt
    *           Extended Scheduled Deadline Date
    * @param aWorkLoc
    *           The work-area where the task is to be executed
    * @param aWpBarcode
    *           Barcode of the wrapper work-package
    */
   private static void testRow( DataSet aDs, int aTaskDbId, int aTaskId, boolean aSoftDeadline,
         String aTaskName, String aTaskCd, String aBarcodeSdesc, String aEventSdesc,
         InventoryKey aLinkedInv, String aInvSDesc, InventoryKey aParentAssemblyInventory,
         String aParentAssemblyInvSDesc, String aTaskPriorityCd, RefWorkTypeKey aWorkType,
         String aWorkTypeCd, boolean aPartsReady, boolean aToolsReady,
         RefTaskOriginatorKey aOriginator, String aTaskOriginatorCd, String aTaskClassCd,
         String aUserSubclassCd, TaskKey aWp, String aWpSdesc, TaskKey aLinkedTask,
         String aSchedPriorityCd, double aDeviationQt, double aUsageRemQt, Date aSchedDeadDt,
         String aDomainTypeCd, String aEngUnitCd, String aDataTypeCd, double aEngUnitMultQt,
         Date aExtSchedDeadDt, LocationKey aWorkLoc, String aWpBarcode ) {

      MxAssert.assertEquals( "task_db_id", aTaskDbId, aDs.getInt( "task_db_id" ) );
      MxAssert.assertEquals( "task_id", aTaskId, aDs.getInt( "task_id" ) );

      MxAssert.assertEquals( "soft_deadline", aSoftDeadline, aDs.getBoolean( "soft_deadline" ) );

      MxAssert.assertEquals( "task_name", aTaskName, aDs.getString( "task_name" ) );
      MxAssert.assertEquals( "task_cd", aTaskCd, aDs.getString( "task_cd" ) );
      MxAssert.assertEquals( "barcode_sdesc", aBarcodeSdesc, aDs.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( "event_sdesc", aEventSdesc, aDs.getString( "event_Sdesc" ) );

      MxAssert.assertEquals( "linked_inv", aLinkedInv,
            new InventoryKey( aDs.getString( "linked_inv" ) ) );

      MxAssert.assertEquals( "inv_no_sdesc", aInvSDesc, aDs.getString( "inv_no_sdesc" ) );
      if ( !aDs.isNull( "parent_assembly_inv_key" ) ) {
         MxAssert.assertEquals( "parent_assembly_inv_key", aParentAssemblyInventory,
               new InventoryKey( aDs.getString( "parent_assembly_inv_key" ) ) );
      }

      MxAssert.assertEquals( "parent_assembly_inv_sdesc", aParentAssemblyInvSDesc,
            aDs.getString( "parent_assembly_inv_sdesc" ) );

      MxAssert.assertEquals( "task_priority_cd", aTaskPriorityCd,
            aDs.getString( "task_priority_cd" ) );

      if ( !aDs.isNull( "work_type_cd" ) ) {
         MxAssert.assertEquals( "work_type_cd", aWorkType,
               new RefWorkTypeKey( aDs.getString( "work_type_cd" ) ) );
      }

      MxAssert.assertEquals( "parts_ready_bool", aPartsReady,
            aDs.getBoolean( "parts_ready_bool" ) );

      MxAssert.assertEquals( "tools_ready_bool", aPartsReady,
            aDs.getBoolean( "tools_ready_bool" ) );

      if ( !aDs.isNull( "task_originator_cd" ) ) {
         MxAssert.assertEquals( "originator_key", aOriginator.getCd(),
               aDs.getString( "task_originator_cd" ) );
      }

      MxAssert.assertEquals( "task_class_cd", aTaskClassCd, aDs.getString( "task_class_cd" ) );

      MxAssert.assertEquals( "user_subclass_cd", aUserSubclassCd,
            aDs.getString( "user_subclass_cd" ) );

      if ( !aDs.isNull( "wp_key" ) ) {
         MxAssert.assertEquals( "wp_key", aWp, new TaskKey( aDs.getString( "wp_key" ) ) );
      }

      MxAssert.assertEquals( "wp_sdesc", aWpSdesc, aDs.getString( "wp_sdesc" ) );

      if ( aDs.isNull( "linked_task" ) ) {
         MxAssert.assertEquals( "linked_task", aLinkedTask,
               new TaskKey( aDs.getString( "linked_task" ) ) );
      }

      MxAssert.assertEquals( "sched_priority_cd", aSchedPriorityCd,
            aDs.getString( "sched_priority_cd" ) );

      MxAssert.assertEquals( "deviation_qt", aDeviationQt, aDs.getDouble( "deviation_qt" ) );

      MxAssert.assertEquals( "usage_rem_qt", aUsageRemQt, aDs.getDouble( "usage_rem_qt" ) );

      MxAssert.assertEquals( "sched_dead_dt", aSchedDeadDt, aDs.getDate( "sched_dead_dt" ) );

      MxAssert.assertEquals( "domain_type_cd", aDomainTypeCd, aDs.getString( "domain_type_cd" ) );

      MxAssert.assertEquals( "eng_unit_cd", aEngUnitCd, aDs.getString( "eng_unit_cd" ) );

      MxAssert.assertEquals( "data_type_cd", aDataTypeCd, aDs.getString( "data_type_cd" ) );

      MxAssert.assertEquals( "eng_unit_mult_qt", aEngUnitMultQt,
            aDs.getDouble( "eng_unit_mult_qt" ) );

      MxAssert.assertEquals( "ext_sched_dead_dt", aExtSchedDeadDt,
            aDs.getDate( "ext_sched_dead_dt" ) );

      if ( !aDs.isNull( "work_loc_key" ) ) {
         MxAssert.assertEquals( "work_loc_key", aWorkLoc,
               new LocationKey( aDs.getString( "work_loc_key" ) ) );
      }

      MxAssert.assertEquals( "wp_barcode", aWpBarcode, aDs.getString( "wp_barcode" ) );
   }
}
