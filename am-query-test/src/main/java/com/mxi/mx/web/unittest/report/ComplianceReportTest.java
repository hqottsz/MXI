
package com.mxi.mx.web.unittest.report;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.ejb.EjBFactorySystemStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefDispositionKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskRefDocKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.TaskDefnUtils;
import com.mxi.mx.core.services.taskdefn.transferobject.SetSchedulingRuleTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.ref.RefTaskClass;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskRefDocTable;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.web.report.compliance.ComplianceReport;
import com.mxi.mx.web.report.compliance.InvComplianceReport;
import com.mxi.mx.web.report.compliance.RefDocComplianceReport;


/**
 * This is the test suite for the ComplianceReport
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ComplianceReportTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      RefTaskClass lRefTaskClass = RefTaskClass.create();
      lRefTaskClass.setClassModeCd( "REF" );
      lRefTaskClass.insert( new RefTaskClassKey( "10:REF" ) );
   }


   /**
    * Tests the inventory scenario
    */
   @Test
   public void testInventoryScenario() {

      // set am ejb factory stub to ensure the security utils class is not instantiated for
      // validation downstream.
      EjbFactory.setSingleton( new EjBFactorySystemStub() );

      TestData.setupScenario();

      DataSet lDataSet = execute( TestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 15, lDataSet.getRowCount() );

      assertStandardLayout( lDataSet );
   }


   /**
    * Tests the reference doc scenario
    */
   @Test
   public void testRefDocScenario() {

      TestData.setupScenario();

      DataSet lDataSet = execute( TestData.TASK_TASK_KEY_REF_1, TestData.TASK_DEFN_KEY_REF_1 );

      Assert.assertEquals( "Number of retrieved rows", 15, lDataSet.getRowCount() );

      assertStandardLayout( lDataSet );
   }


   /**
    * Tests that the returned row is correct
    *
    * @param aDataSet
    *           TODO
    * @param aAssembly
    *           Root Assembly / Aircraft
    * @param aTaskDefnRev
    *           Ref / Req Revision
    * @param aConfigSlot
    *           Config Slot
    * @param aStatus
    *           Status
    * @param aLastDone
    *           Last Done Task
    * @param aNextDue
    *           Next Due Task
    * @param aLastUsageQtString
    *           Last usage quantity string
    */
   private void assertNextRow( DataSet aDataSet, InventoryKey aAssembly, TaskTaskKey aTaskDefnRev,
         ConfigSlotPositionKey aConfigSlot, String aStatus, TaskKey aLastDone, TaskKey aNextDue,
         String aLastUsageQtString ) {
      Assert.assertTrue( aDataSet.next() );
      Assert.assertEquals( aAssembly, aDataSet.getKey( InventoryKey.class, "inventory_key" ) );
      Assert.assertEquals( aTaskDefnRev, aDataSet.getKey( TaskTaskKey.class, "task_key" ) );
      Assert.assertEquals( aConfigSlot.getBomItemKey(),
            aDataSet.getKey( ConfigSlotKey.class, "config_slot_key" ) );
      Assert.assertEquals( aStatus, aDataSet.getString( "status" ) );
      Assert.assertEquals( aLastDone, aDataSet.getKey( TaskKey.class, "last_task_key" ) );
      Assert.assertEquals( aNextDue, aDataSet.getKey( TaskKey.class, "next_task_key" ) );
      Assert.assertEquals( "Last Usage Quantity", aLastUsageQtString,
            aDataSet.getString( "last_usage_qt" ) );
   }


   /**
    * This tests the standard layout for the scenario, and ensures that the results are identical
    * for both the Inventory and Ref Doc version of the Compliance Report
    */
   private void assertStandardLayout( DataSet aDataSet ) {

      // Verify that the dataset layout matches the intended data setup
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_1,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_2,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_1,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null,
            TestData.ACTUAL_TASK_KEY_REQ_1, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_3,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_2,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(),
            TestData.ACTUAL_TASK_KEY_REQ_3, TestData.ACTUAL_TASK_KEY_REQ_2,
            TestData.USAGE_HOURS_STRING );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_2,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(),
            TestData.ACTUAL_TASK_KEY_REQ_3, TestData.ACTUAL_TASK_KEY_REQ_2, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_4,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.COMPLETE.toString(), null, null,
            "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_3,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.COMPLETE.toString(),
            TestData.ACTUAL_TASK_KEY_REQ_4, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_5,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_6,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_7,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.N_A.toString(), null, null, "" );

      // Note: REF 8 should not appear due to N/A parent

      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REF_9,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_5,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OPEN.toString(), null, null, "" );

      // There exists a completed task for obsoleted REQ
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_6,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.COMPLETE.toString(),
            TestData.ACTUAL_TASK_KEY_REQ_5, null, "" );

      // no completed task for obsoleted REQ
      assertNextRow( aDataSet, TestData.ASSY_INVENTORY_1, TestData.TASK_TASK_KEY_REQ_7,
            TestData.BOM_ITEM_POSITION, ComplianceReport.Status.OBSOLETE.toString(), null, null,
            "" );
   }


   /**
    * Execute the Compliance Report.
    *
    * @param aAssembly
    *           The Ref Document to use as Root
    * @return
    */
   private DataSet execute( InventoryKey aAssembly ) {
      ComplianceReport lReport = new InvComplianceReport( aAssembly, null );
      lReport.generate();
      return lReport.getDataSet();
   }


   /**
    * Execute the query.
    *
    * @param aRefDocRev
    *           The Ref Document to use as Root
    * @param aRefDoc
    *           The Ref Document to use as Root
    * @return
    */
   private DataSet execute( TaskTaskKey aRefDocRev, TaskDefnKey aRefDoc ) {
      ComplianceReport lReport = new RefDocComplianceReport( aRefDocRev, aRefDoc, null );
      lReport.generate();
      return lReport.getDataSet();
   }


   /**
    * Internal class that houses the test data
    *
    * @author cdaley
    */
   private static class TestData {

      /** inventory key 1 */
      private static final InventoryKey ASSY_INVENTORY_1 = new InventoryKey( 1, 1 );

      /** Config Slot & Position */
      private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 1, "TESTBOM" );
      private static final ConfigSlotPositionKey BOM_ITEM_POSITION =
            new ConfigSlotPositionKey( new ConfigSlotKey( ASSEMBLY_KEY, 0 ), 1 );

      /** task defns for REF DOCs */
      private static final TaskDefnKey TASK_DEFN_KEY_REF_1 = new TaskDefnKey( 1000, 1 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_2 = new TaskDefnKey( 1000, 2 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_3 = new TaskDefnKey( 1000, 3 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_4 = new TaskDefnKey( 1000, 4 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_5 = new TaskDefnKey( 1000, 5 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_6 = new TaskDefnKey( 1000, 6 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_7 = new TaskDefnKey( 1000, 7 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_8 = new TaskDefnKey( 1000, 8 );
      private static final TaskDefnKey TASK_DEFN_KEY_REF_9 = new TaskDefnKey( 1000, 9 );

      private static final TaskTaskKey TASK_TASK_KEY_REF_1 = new TaskTaskKey( 1001, 1 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_2 = new TaskTaskKey( 1001, 2 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_3 = new TaskTaskKey( 1001, 3 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_4 = new TaskTaskKey( 1001, 4 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_5 = new TaskTaskKey( 1001, 5 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_6 = new TaskTaskKey( 1001, 6 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_7 = new TaskTaskKey( 1001, 7 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_8 = new TaskTaskKey( 1001, 8 );
      private static final TaskTaskKey TASK_TASK_KEY_REF_9 = new TaskTaskKey( 1001, 9 );

      /** task defn for REQs */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_1 = new TaskDefnKey( 3000, 1 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_2 = new TaskDefnKey( 3000, 2 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_3 = new TaskDefnKey( 3000, 3 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_4 = new TaskDefnKey( 3000, 4 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_5 = new TaskDefnKey( 3000, 5 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_6 = new TaskDefnKey( 3000, 6 );
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_7 = new TaskDefnKey( 3000, 7 );

      private static final TaskTaskKey TASK_TASK_KEY_REQ_1 = new TaskTaskKey( 3001, 1 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_2 = new TaskTaskKey( 3001, 2 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_3 = new TaskTaskKey( 3001, 3 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_4 = new TaskTaskKey( 3001, 4 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_5 = new TaskTaskKey( 3001, 5 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_6 = new TaskTaskKey( 3001, 6 );
      private static final TaskTaskKey TASK_TASK_KEY_REQ_7 = new TaskTaskKey( 3001, 7 );

      private static final TaskKey ACTUAL_TASK_KEY_REQ_1 = new TaskKey( 3002, 1 );
      private static final TaskKey ACTUAL_TASK_KEY_REQ_2 = new TaskKey( 3002, 2 );
      private static final TaskKey ACTUAL_TASK_KEY_REQ_3 = new TaskKey( 3002, 3 );
      private static final TaskKey ACTUAL_TASK_KEY_REQ_4 = new TaskKey( 3002, 4 );
      private static final TaskKey ACTUAL_TASK_KEY_REQ_5 = new TaskKey( 3002, 5 );

      /** carrier key */
      private static final CarrierKey CARRIER_KEY_1 = new CarrierKey( 6000, 1 );

      /** 10 level REF task class */
      private static final RefTaskClassKey REF_TASK_CLASS = new RefTaskClassKey( "10:REF" );

      /** usage data */
      private static final double USAGE_HOURS = 5.25;
      private static final String USAGE_HOURS_STRING = "5.25 HOUR";


      /**
       * Setup test scenario for a tree as follows:<br>
       * <br>
       *
       * <UL>
       * <LI>REF 1</LI>
       * <UL>
       * <LI>REF 2</LI>
       * <UL>
       * <LI>REQ 1<BR>
       * Actuals: Active Task</LI>
       * </UL>
       * <LI>REF 3</LI>
       * <UL>
       * <LI>REQ 2<BR>
       * Actuals: Historic 'COMPLETE' Task, Active Task</LI>
       * </UL>
       * <LI>REF 4</LI>
       * <UL>
       * <LI>REQ 3<BR>
       * Actuals: Historic 'COMPLETE' Task</LI>
       * <UL>
       * <LI>REQ 4<BR>
       * Notes: Should not appear due to being under another REQ</LI>
       * </UL>
       * </UL>
       * <LI>REF 5</LI>
       * <UL>
       * <LI>REF 6<BR>
       * Note: Tests case for no REQs under a REF DOC</LI>
       * </UL>
       * <LI>REF 7<BR>
       * Note: Non Applicable</LI>
       * <UL>
       * <LI>REF 8<BR>
       * Note: Should not appear due to N/A parent</LI>
       * </UL>
       * <LI>REF 9</LI>
       * <UL>
       * <LI>REQ 5<BR>
       * Actuals: None</LI>
       * <LI>REQ 6 (OBSOLETE)<BR>
       * Actuals: Historic 'COMPLETE' Task</LI>
       * <LI>REQ 7 (OBSOLETE)<BR>
       * Actuals: None</LI>
       * </UL>
       * </UL>
       * </UL>
       */
      public static void setupScenario() {
         setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION, RefInvClassKey.ASSY );

         // create task defns
         setupTaskDefn( TASK_DEFN_KEY_REF_1, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_2, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_3, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_4, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_5, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_6, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_7, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_8, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REF_9, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_2, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_3, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_4, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_5, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_6, 1 );
         setupTaskDefn( TASK_DEFN_KEY_REQ_7, 1 );

         // create task tasks
         setupTaskTask( TASK_TASK_KEY_REF_1, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_1,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_2, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_2,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_3, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_3,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_4, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_4,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_5, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_5,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_6, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_6,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_7, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_7,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_8, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_8,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REF_9, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REF_9,
               RefTaskDefinitionStatusKey.ACTV, REF_TASK_CLASS );
         setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_1,
               RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_2,
               RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_3, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_3,
               RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_4, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_4,
               RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_5, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_5,
               RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_6, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_6,
               RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );
         setupTaskTask( TASK_TASK_KEY_REQ_7, BOM_ITEM_POSITION, 1, TASK_DEFN_KEY_REQ_7,
               RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         // create actual reqs
         setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1,
               RefEventStatusKey.ACTV );
         setupActualTask( ACTUAL_TASK_KEY_REQ_2, ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2,
               RefEventStatusKey.ACTV );
         setupActualTask( ACTUAL_TASK_KEY_REQ_3, ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2,
               RefEventStatusKey.COMPLETE );
         setupActualTask( ACTUAL_TASK_KEY_REQ_4, ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_3,
               RefEventStatusKey.COMPLETE );
         setupActualTask( ACTUAL_TASK_KEY_REQ_5, ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_6,
               RefEventStatusKey.COMPLETE );

         // create usage data
         setupUsage( ACTUAL_TASK_KEY_REQ_3, DataTypeKey.HOURS, USAGE_HOURS );

         // Link the Tasks into a Tree: Level 1
         setupLink( TASK_TASK_KEY_REF_2, 1, TASK_DEFN_KEY_REF_1 );
         setupLink( TASK_TASK_KEY_REF_3, 1, TASK_DEFN_KEY_REF_1 );
         setupLink( TASK_TASK_KEY_REF_4, 1, TASK_DEFN_KEY_REF_1 );
         setupLink( TASK_TASK_KEY_REF_5, 1, TASK_DEFN_KEY_REF_1 );
         setupLink( TASK_TASK_KEY_REF_7, 1, TASK_DEFN_KEY_REF_1 );
         setupLink( TASK_TASK_KEY_REF_9, 1, TASK_DEFN_KEY_REF_1 );

         // Link the Tasks into a Tree: Level 2
         setupLink( TASK_TASK_KEY_REQ_1, 1, TASK_DEFN_KEY_REF_2 );
         setupLink( TASK_TASK_KEY_REQ_2, 1, TASK_DEFN_KEY_REF_3 );
         setupLink( TASK_TASK_KEY_REQ_3, 1, TASK_DEFN_KEY_REF_4 );
         setupLink( TASK_TASK_KEY_REF_6, 1, TASK_DEFN_KEY_REF_5 );
         setupLink( TASK_TASK_KEY_REF_8, 1, TASK_DEFN_KEY_REF_7 );
         setupLink( TASK_TASK_KEY_REQ_5, 1, TASK_DEFN_KEY_REF_9 );
         setupLink( TASK_TASK_KEY_REQ_6, 2, TASK_DEFN_KEY_REF_9 );
         setupLink( TASK_TASK_KEY_REQ_7, 3, TASK_DEFN_KEY_REF_9 );

         // Link the Tasks into a Tree: Level 3
         setupLink( TASK_TASK_KEY_REQ_4, 1, TASK_DEFN_KEY_REQ_3 );

         // Make Ref 7 Non Applicable to the Inventory
         try {
            TaskTaskTable lTable = TaskTaskTable.findByPrimaryKey( TASK_TASK_KEY_REF_7 );
            lTable.setTaskDefStatus( RefTaskDefinitionStatusKey.BUILD );
            lTable.setTaskApplSqlLdesc(
                  TaskDefnUtils.convertApplicabilityToRawSql( "[Aircraft Serial No] = 'TEST'" ) );
            lTable.update();

            SetSchedulingRuleTO lTO = new SetSchedulingRuleTO();
            lTO.setDataType( new DataTypeKey( 100, 7 ), "" );
            lTO.setDeviation( 0.0, "" );
            lTO.setInitialInterval( 400.0, "" );
            lTO.setNotification( 50.0, "" );
            lTO.setRepeatInterval( 300.0, "" );
            lTO.setSchedToPlanHigh( 0.0, "" );
            lTO.setSchedToPlanLow( 0.0, "" );
            TaskDefnService.setPartSpecificSchedulingRule( TASK_TASK_KEY_REF_7, lTO,
                  new PartNoKey( 0, 0 ) );

            lTable.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
            lTable.update();
         } catch ( MxException e ) {
            Assert.fail( "Failed to setup a Non-Applicable Ref Doc" );
         }
      }


      /**
       * Create an actual task with a parent task
       *
       * @param aTaskKey
       *           The actual task key
       * @param aInventory
       *           the inventory
       * @param aTaskTask
       *           the task definition
       * @param aRefEventStatus
       *           the task status
       */
      private static void setupActualTask( TaskKey aTaskKey, InventoryKey aInventory,
            TaskTaskKey aTaskTask, RefEventStatusKey aRefEventStatus ) {
         SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
         lSchedStask.setTaskTaskKey( aTaskTask );
         lSchedStask.setMainInventory( aInventory );
         lSchedStask.setBarcode( aTaskKey.toString() );
         lSchedStask.setMainInventory( aInventory );
         lSchedStask.insert();

         boolean lHistoric = RefEventStatusKey.CANCEL.equals( aRefEventStatus )
               || RefEventStatusKey.COMPLETE.equals( aRefEventStatus )
               || RefEventStatusKey.TERMINATE.equals( aRefEventStatus );

         EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
         lEvent.setEventStatus( aRefEventStatus );
         lEvent.setHEvent( aTaskKey.getEventKey() );
         lEvent.setHistBool( lHistoric );
         lEvent.insert();

         EvtInvTable lEvtInv = EvtInvTable.create( aTaskKey );
         lEvtInv.setInventoryKey( aInventory );
         lEvtInv.setMainInvBool( true );
         lEvtInv.insert();
      }


      /**
       * Create an inventory
       *
       * @param aInventoryKey
       *           inventory key to create
       * @param aBomItemPosition
       *           inventory bom item position
       * @param aInvClass
       *           the inventory class
       */
      private static void setupInventory( InventoryKey aInventoryKey,
            ConfigSlotPositionKey aBomItemPosition, RefInvClassKey aInvClass ) {

         // Create a single Inventory at the location
         InvOwnerTable lOwner = InvOwnerTable.create( new OwnerKey( 0, 0 ) );
         InvInvTable lInventory = InvInvTable.create( aInventoryKey );
         lInventory.setAssmblInvNo( aInventoryKey );
         lInventory.setInvCond( RefInvCondKey.RFI );
         lInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         lInventory.setOwner( lOwner.insert() );
         lInventory.setInvClass( aInvClass );
         lInventory.setBomItemPosition( aBomItemPosition );
         lInventory.setApplEffCd( null );
         lInventory.setCarrier( CARRIER_KEY_1 );
         lInventory.setApplEffCd( null );
         lInventory.setNhInvNo( null );
         lInventory.setHInvNo( aInventoryKey );
         lInventory.setInvNoSdesc( aInventoryKey.toString() );
         lInventory.setOrigAssmbl( aBomItemPosition.getAssemblyKey() );
         lInventory.insert();

         EqpBomPart lEqpBomPart = EqpBomPart.create();
         lEqpBomPart.setApplEffLdesc( null );
         lEqpBomPart.setBomItem( aBomItemPosition.getBomItemKey() );
         lEqpBomPart.setBomPartCd( aBomItemPosition.getCd() + "_" + aInventoryKey.getDbId() + "_"
               + aInventoryKey.getId() );
         lEqpBomPart.insert( new PartGroupKey( aInventoryKey.getDbId(), aInventoryKey.getId() ) );

         try {
            EqpAssmblBom lEqpAssmblBom = EqpAssmblBom.create( aBomItemPosition.getBomItemKey() );
            lEqpAssmblBom.insert();

            EqpAssmblPos lEqpAssmblPos = EqpAssmblPos.create( aBomItemPosition );
            lEqpAssmblPos.insert();
         } catch ( Exception e ) {
            // if the assembly already exists then continue
         }
      }


      /**
       * DOCUMENT_ME
       *
       * @param aTaskTask
       *           DOCUMENT_ME
       * @param aDepId
       *           DOCUMENT_ME
       * @param aDepTaskDefn
       *           DOCUMENT_ME
       */
      private static void setupLink( TaskTaskKey aTaskTask, int aDepId, TaskDefnKey aDepTaskDefn ) {

         TaskTaskDepTable lDependency =
               TaskTaskDepTable.create( new TaskTaskDepKey( aTaskTask, aDepId ) );
         lDependency.setDepTaskDefn( aDepTaskDefn );
         lDependency.setTaskDepAction( RefTaskDepActionKey.COMPLIES );
         lDependency.insert();
      }


      /**
       * Create a task defn
       *
       * @param aTaskDefn
       *           the task defn to create
       * @param aLastRevisionOrd
       *           DOCUMENT ME!
       */
      private static void setupTaskDefn( TaskDefnKey aTaskDefn, Integer aLastRevisionOrd ) {
         TaskDefnTable lTaskDefn = TaskDefnTable.create( aTaskDefn );
         lTaskDefn.setLastRevisionOrd( aLastRevisionOrd );
         lTaskDefn.insert();
      }


      /**
       * Create a block
       *
       * @param aTaskTaskKey
       *           block to create
       * @param aConfigSlotPositionKey
       *           bom item position
       * @param aRevision
       *           revision
       * @param aTaskDefn
       *           the task defn
       * @param aStatus
       *           the status of the task_task
       * @param aTaskClassKey
       *           the task class
       */
      private static void setupTaskTask( TaskTaskKey aTaskTaskKey,
            ConfigSlotPositionKey aConfigSlotPositionKey, int aRevision, TaskDefnKey aTaskDefn,
            RefTaskDefinitionStatusKey aStatus, RefTaskClassKey aTaskClassKey ) {
         TaskTaskTable lTaskTask = TaskTaskTable.create();
         lTaskTask.setBomItem( aConfigSlotPositionKey.getBomItemKey() );
         lTaskTask.setRevisionOrd( aRevision );
         lTaskTask.setTaskClass( aTaskClassKey );
         lTaskTask.setTaskCd( aTaskClassKey.getCd() + " " + aTaskTaskKey );
         lTaskTask.setTaskDefn( aTaskDefn );
         lTaskTask.setTaskDefStatus( aStatus );
         lTaskTask.setTaskApplEffLdesc( null );
         lTaskTask.setTaskApplSqlLdesc( null );
         lTaskTask.setOnConditionBool( false );
         lTaskTask.insert( aTaskTaskKey );

         if ( RefTaskClassKey.REF_CLASS_MODE_CD.equals( aTaskClassKey.getCd() ) ) {

            TaskRefDocTable lRefDocTask =
                  TaskRefDocTable.create( new TaskRefDocKey( aTaskTaskKey ) );
            lRefDocTask.setAmendRefSdesc( "amend" );
            lRefDocTask.setRefTaskDefDispositionKey( new RefTaskDefDispositionKey( 10, "DISPO" ) );
            lRefDocTask.insert();
         }
      }


      /**
       * Create usage data for the given task using the provided data type and quantity.
       *
       * @param aTaskKey
       *           The actual task key
       * @param aDataTypeKey
       *           The usage data type
       * @param aQuantity
       *           The usage quantity
       */
      private static void setupUsage( TaskKey aTaskKey, DataTypeKey aDataTypeKey,
            double aQuantity ) {
         EventInventoryKey lEventInventoryKey = new EventInventoryKey( aTaskKey.getEventKey(), 1 );
         EventInventoryUsageKey lEventInventoryUsageKey =
               new EventInventoryUsageKey( lEventInventoryKey, aDataTypeKey );

         // create minimal records for usage using the provided task key, data type, and quantity
         EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( aTaskKey, aDataTypeKey );
         lEvtSchedDead.setDriver( true );
         lEvtSchedDead.setDeviationQt( 0.0 );
         lEvtSchedDead.setNotifyQt( 0.0 );
         lEvtSchedDead.setIntervalQt( 0.0 );
         lEvtSchedDead.setPrefixedQt( 0.0 );
         lEvtSchedDead.setPostfixedQt( 0.0 );
         lEvtSchedDead.insert();
         EvtInvUsage.create( lEventInventoryUsageKey, aQuantity, aQuantity, aQuantity,
               RefUsgSnapshotSrcTypeKey.MAINTENIX );
      }
   }
}
