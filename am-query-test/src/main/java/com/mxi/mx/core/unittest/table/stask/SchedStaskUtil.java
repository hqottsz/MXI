
package com.mxi.mx.core.unittest.table.stask;

import java.util.Date;
import java.util.UUID;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefReceiveCondKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskOriginatorKey;
import com.mxi.mx.core.key.RefTaskPriorityKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>sched_stask</code> table.
 *
 * @author asmolko
 * @created March 18, 2002
 */
public class SchedStaskUtil {

   private SchedStaskTable iTable;


   /**
    * Initializes the class.
    *
    * @param aTask
    *           TaskKey.
    */
   public SchedStaskUtil(TaskKey aTask) {
      iTable = SchedStaskTable.findByPrimaryKey( aTask );
   }


   /**
    * Initializes the class.
    *
    * @param aTask
    *           TaskKey.
    */
   public SchedStaskUtil(SchedStaskTable aTable) {
      iTable = aTable;
   }


   /**
    * Returns the value of the table property.
    *
    * @return the value of the table property
    */
   public SchedStaskTable getTable() {
      return iTable;
   }


   /**
    * Find task
    *
    * @param aTaskClass
    *           inventory class
    * @param aPreviousTaskKey
    *           previous task
    *
    * @return null if there were no new tasks
    */
   public static TaskKey findLastCreatedTask( String aTaskClass, TaskKey aPreviousTaskKey ) {

      // return columns
      String[] lColumns = new String[] { "max(sched_id) as Max_Id" };

      // where arguments
      DataSetArgument lWhereArgs = new DataSetArgument();

      lWhereArgs.add( "task_class_cd", aTaskClass );
      lWhereArgs.add( "sched_db_id", 4650 );

      // get the inventory row
      DataSet lRows =
            MxDataAccess.getInstance().executeQuery( lColumns, "sched_stask", lWhereArgs );
      if ( lRows.next() ) {

         TaskKey lNewTask = new TaskKey( 4650, lRows.getInt( "Max_Id" ) );

         if ( ( aPreviousTaskKey == null ) || ( lNewTask.getId() > aPreviousTaskKey.getId() ) ) {
            return lNewTask;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }


   /**
    * Find the last created task for a task definition
    *
    * @param aTaskDefn
    *           the task definition key
    *
    * @return null if there were no new tasks
    */
   public static TaskKey findLastCreatedTaskForTaskDefinition( TaskTaskKey aTaskDefn ) {

      // return columns
      String[] lColumns = new String[] { "max(sched_id) as Max_Id" };

      // where arguments
      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aTaskDefn, new String[] { "task_db_id", "task_id" } );

      // get the sched_stask row
      DataSet lRows =
            MxDataAccess.getInstance().executeQuery( lColumns, "sched_stask", lWhereArgs );
      if ( lRows.next() && ( lRows.getInt( "Max_Id" ) != 0 ) ) {
         return new TaskKey( 4650, lRows.getInt( "Max_Id" ) );
      } else {
         return null;
      }
   }


   /**
    * Asserts that the <code>ADHOC_RECUR_BOOL</code> value are equal.
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertAdHocRecurBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isAdhocRecurBool() );
   }


   /**
    * Assert that the alternate key matches the expected value
    *
    * @param aAlternateKey
    *           the alternate key to match
    */
   public void assertAlternateKey( UUID aAlternateKey ) {
      MxAssert.assertEquals( aAlternateKey, iTable.getAlternateKey() );
   }


   /**
    * Asserts that the <code>AUTO_COMPLETE_BOOL</code> value are equal.
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertAutoCompleteBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isAutoCompleteBool() );
   }


   /**
    * Asserts the Barcode value.
    *
    * @param aExpected
    *           The expected barcode value
    */
   public void assertBarcode( String aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getBarcode() );
   }


   /**
    * Asserts that the <code>collection_required_bool</code> value are equal
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertCollectionRequiredBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isCollectionRequiredBool() );
   }


   /**
    * Asserts that the <code>CORR_FIX_BOOL</code> value are equal.
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertCorrFixBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isCorrFixBool() );
   }


   /**
    * Asserts that the row doesn't exist.
    */
   public void assertDoesNotExist() {
      if ( iTable.exists() ) {
         Assert.fail( "The sched_stask table does have the row" );
      }
   }


   /**
    * Asserts that the <code>dup_jic_sched_db_id:dup_jic_sched_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertDupJicSchedPk( TaskKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getDupJicSchedKey() );
   }


   /**
    * Asserts the EST_DURATION_QT column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertEstDurationQt( Double aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getEstimatedDuration() );
   }


   /**
    * Asserts that the row with <code>sched_db_id:sched_id
    */
   public void assertExist() {
      if ( !iTable.exists() ) {
         Assert.fail( "The sched_stask table does not have the row" );
      }
   }


   /**
    * Assert the fault key
    *
    * @param aExpected
    *           should an exception occur
    */
   public void assertFaultKey( FaultKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getFault() );
   }


   /**
    * Asserts that the <code>heavy_bool</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertHeavyBool( Boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isHeavy() );
   }


   /**
    * Assert the highest task key
    *
    * @param aExpected
    *           should an exception occur
    */
   public void assertHTaskKey( TaskKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getHTaskKey() );
   }


   /**
    * Asserts that the <code>instruction_ldesc</code> value are equal.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertInstructionLdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getInstructionLdesc() );
   }


   /**
    * Asserts that the <code>issued_dt</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertIssuedDt( Date aExpected ) {
      MxAssert.assertEquals( "", aExpected, iTable.getIssuedDt(), 1 );
   }


   /**
    * Asserts that the <code>issued_gdt</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertIssuedGdt( Date aExpected ) {
      MxAssert.assertEquals( "", aExpected, iTable.getIssuedGdt(), 1 );
   }


   /**
    * Assert the issue account is the same
    *
    * @param aExpected
    *           the expected value
    */
   public void assertIssueToAccount( FncAccountKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getIssueAccount() );
   }


   /**
    * Asserts that the <code>LRP_BOOL</code> value are equal.
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertLrpBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isLRPBool() );
   }


   /**
    * Asserts the main_inv_no_pk field
    *
    * @param aExpected
    *           the expected main inventory.
    */
   public void assertMainInventory( InventoryKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getMainInventory() );
   }


   /**
    * Asserts that the <code>ADJUSTED_BILLING_BOOL</code> and the expected value are equal.
    *
    * @param aExpected
    *           the expected <code>ADJUSTED_BILLING_BOOL</code> value
    */
   public void assertMarkAsAdjustedForBillingBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isMarkAsAdjustedForBillingBool() );
   }


   /**
    * Asserts the MIN_PLAN_YIELD_PCT column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertMinPlanningYield( Double aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getMinimumPlanningYield() );
   }


   /**
    * Asserts that the <code>orig_part_no_db_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertOrigPartNo( PartNoKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getOrigPartNo() );
   }


   /**
    * Asserts that the <code>parts_ready_bool</code> value is as expected.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertPartsReadyBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isPartsReady() );
   }


   /**
    * Asserts that the <code>plan_by_dt</code> value is equal.
    *
    * @param aExpected
    *           expected Date.
    */
   public void assertPlanByDate( Date aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getPlanByDate() );
   }


   /**
    * Asserts that the prevent_deadline_sync is as expected.
    *
    * @param aExpected
    *           The expected value
    */
   public void assertPreventDeadlineSync( boolean aExpected ) {
      MxAssert.assertEquals( "prevent_deadline_sync", aExpected, iTable.isPreventDeadlineSync() );
   }


   /**
    * Asserts that the <code>PREVENT_LPA_BOOL</code> value are equal.
    *
    * @param aExpected
    *           The expected database value
    */
   public void assertPreventLPABool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isPreventLinePlanningAutomation() );
   }


   /**
    * Asserts that the <code>RECEIVED_COND_CD</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertReceivedCond( RefReceiveCondKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getReceiveCond() );
   }


   /**
    * Asserts that the <code>repair_qt</code> value are equal.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertRepairQt( Double aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getRepairQt() );
   }


   /**
    * Asserts that the <code>resource_sum_bool</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertResourceSummary( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isResourceSumBool() );
   }


   /**
    * Asserts that the <code>ro_ref_sdesc</code> value are equal.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertRoRefSdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getRoRefSdesc() );
   }


   /**
    * Asserts that the <code>routine_bool</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertRoutineBool( Boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isRoutineBool() );
   }


   /**
    * Asserts that the <code>ro_vendor_db_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertRoVendor( VendorKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getRoVendor() );
   }


   /**
    * Asserts the SCHED_HR_MULT_QT column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertSchedHrMultQtQt( Double aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getSchedHrMultQt() );
   }


   /**
    * Asserts that the <code>soft_deadline_bool</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertSoftDeadlineBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isSoftDeadlineBool() );
   }


   /**
    * Asserts that the <code>task_class_pk</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskClass( RefTaskClassKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskClass() );
   }


   /**
    * Asserts that the <code>TASK_LOCK_BOOL</code> and the expected value are equal.
    *
    * @param aExpected
    *           the expected <code>TASK_LOCK_BOOL</code> value
    */
   public void assertTaskLockBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isTaskLockBool() );
   }


   /**
    * Asserts that the <code>task_originator_cd</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskOriginator( RefTaskOriginatorKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskOriginator() );
   }


   /**
    * Asserts that the <code>task_db_id:task_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskPk( TaskTaskKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskTaskKey() );
   }


   /**
    * Asserts that the <code>task_priority_cd</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskPriority( RefTaskPriorityKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskPriority() );
   }


   /**
    * Asserts that the <code>task_ref_sdesc</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskRefSdesc( String aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskRefSdesc() );
   }


   /**
    * Asserts that the <code>task_subclass_db_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskSubclass( RefTaskSubclassKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskSubclass() );
   }


   /**
    * Asserts that the <code>task_db_id:task_id</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertTaskTaskPk( TaskTaskKey aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getTaskTaskKey() );
   }


   /**
    * Asserts that the <code>tools_ready_bool</code> value is as expected.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertToolsReadyBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isToolsReady() );
   }


   /**
    * Asserts that the <code>vendor_wo_ref_sdesc</code> value is as expected.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertVendorWoRefSdesc( String aExpected ) {

      MxAssert.assertEquals( aExpected, iTable.getVendorWoRefSdesc() );
   }


   /**
    * Asserts that the <code>warranty_note</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertWarrantyNote( String aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getWarrantyNote() );
   }


   /**
    * Asserts that the <code>watch_bool</code> value are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertWatchBool( boolean aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.isWatch() );
   }


   /**
    * Asserts that the actual table column value and the given expected value are equal.
    *
    * @param aExpected
    *           the expected column value.
    */
   public void assertWoCommitLineOrd( Integer aExpected ) {
      MxAssert.assertEquals( aExpected, iTable.getWoCommitLineOrd() );
   }


   /**
    * Asserts that the <code>wo_ref_sdesc</code> value are equal.
    *
    * @param aExpected
    *           expected value (or start with value)
    * @param aStartsWith
    *           perform only 'start with' comparison
    */
   public void assertWoRefSdesc( String aExpected, boolean aStartsWith ) {
      if ( aStartsWith ) {
         MxAssert.assertNotNull( iTable.getWoRefSdesc() );
         MxAssert.assertTrue( iTable.getWoRefSdesc().startsWith( aExpected ) );
      } else {
         MxAssert.assertEquals( aExpected, iTable.getWoRefSdesc() );
      }
   }


   /**
    * Assert that the Wo the Task was Completed On is the same as the provided WO.
    *
    * @param aWoKey
    */
   public void assertWoTaskCompletedOn( EventKey aWoKey ) {
      MxAssert.assertEquals( aWoKey.toString(), iTable.getWoTaskCompletedOn().toString() );
   }

}
