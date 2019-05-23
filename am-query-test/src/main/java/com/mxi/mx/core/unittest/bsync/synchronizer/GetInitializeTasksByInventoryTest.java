package com.mxi.mx.core.unittest.bsync.synchronizer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MaintPrgmCarrierMapKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.MaintPrgmTaskKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefMaintPrgmStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.bsync.synchronizer.GetInitializeTasksByInventoryTest.InitializeTestData;


/**
 * This is the test suite for the GetInitializeTasksByInventory query
 *
 * @author cdaley
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInitializeTasksByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * @see InitializeTestData.ScenarioBasic#setupGeneralScenario01()
    */
   @Test
   public void testGeneralScenario01() throws Exception {
      InitializeTestData.ScenarioBasic.setupGeneralScenario01();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_1, null );
   }


   /**
    * @see InitializeTestData.setupGeneralScenario2
    */
   @Test
   public void testGeneralScenario02() throws Exception {
      InitializeTestData.ScenarioBasic.setupGeneralScenario02();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario1
    */
   @Test
   public void testInitializeBLOCK01() throws Exception {

      // create test data
      InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario01();

      // run the query
      execute( InitializeTestData.ASSY_INVENTORY_1 );

      // assert that no rows are returned
      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario2
    */
   @Test
   public void testInitializeBLOCK02() throws Exception {
      InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario02();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      // test that the task definition is returned with parent null
      testRow( InitializeTestData.TASK_TASK_KEY_BLOCK_1, null );
   }


   /**
    * @see InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario3
    */
   @Test
   public void testInitializeBLOCK03() throws Exception {
      InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario03();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario4
    */
   @Test
   public void testInitializeBLOCK04() throws Exception {
      InitializeTestData.ScenarioBLOCK.setupInitializeBLOCKScenario04();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      // test that the maintenance program revision is returned
      testRow( InitializeTestData.TASK_TASK_KEY_BLOCK_2, null );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario1
    */
   @Test
   public void testInitializeJIC01() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario01();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario2
    */
   @Test
   public void testInitializeJIC02() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario02();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario3
    */
   @Test
   public void testInitializeJIC03() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario03();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario4
    */
   @Test
   public void testInitializeJIC04() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario04();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario5
    */
   @Test
   public void testInitializeJIC05() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario05();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario6
    */
   @Test
   public void testInitializeJIC06() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario06();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario7
    */
   @Test
   public void testInitializeJIC07() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario07();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario8
    */
   @Test
   public void testInitializeJIC08() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario08();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_2, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario9
    */
   @Test
   public void testInitializeJIC09() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario09();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario10
    */
   @Test
   public void testInitializeJIC10() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario10();

      MxAssert.assertEquals( true, true );

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_2, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario11
    */
   @Test
   public void testInitializeJIC11() throws Exception {

      // create test data
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario11();

      // run the query
      execute( InitializeTestData.ASSY_INVENTORY_1 );

      // assert that no rows are returned
      MxAssert.assertEquals( "Number of retrieved results", 3, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_2 );
      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_3 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC.setupInitializeJICScenario12
    */
   @Test
   public void testInitializeJIC12() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario12();

      // run the query
      execute( InitializeTestData.ASSY_INVENTORY_1 );

      // assert that 1 row is returned
      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_3 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC#setupInitializeJICScenario13()
    */
   @Test
   public void testInitializeJIC13() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario13();

      execute( InitializeTestData.TRK_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC#setupInitializeJICScenario14()
    */
   @Test
   public void testInitializeJIC14() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario14();

      // expect one row for TRK_INVENTORY_1 inventory
      execute( InitializeTestData.TRK_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );

      // expect one row for TRK_INVENTORY_2 inventory
      execute( InitializeTestData.TRK_INVENTORY_2 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_2 );
   }


   /**
    * @see InitializeTestData.ScenarioJIC#setupInitializeJICScenario15()
    */
   @Test
   public void testInitializeJIC15() throws Exception {
      InitializeTestData.ScenarioJIC.setupInitializeJICScenario15();

      // run the query
      execute( InitializeTestData.ASSY_INVENTORY_1 );

      // assert that 1 row is returned
      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_JIC_1, InitializeTestData.ACTUAL_TASK_KEY_REQ_1 );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario1
    */
   @Test
   public void testInitializeREQ01() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario01();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_1, null );
   }


   /**
    * @see InitializeTestData.setupInitializeREQScenario2
    */
   @Test
   public void testInitializeREQ02() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario02();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_1, null );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario3
    */
   @Test
   public void testInitializeREQ03() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario03();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario4
    */
   @Test
   public void testInitializeREQ04() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario04();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario5
    */
   @Test
   public void testInitializeREQ05() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario05();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_2, null );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario6
    */
   @Test
   public void testInitializeREQ06() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario06();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_2, null );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario7
    */
   @Test
   public void testInitializeREQ07() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario07();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario8
    */
   @Test
   public void testInitializeREQ08() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario08();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario9
    */
   @Test
   public void testInitializeREQ09() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario09();

      execute( InitializeTestData.TRK_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_1, null );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario09
    */
   @Test
   public void testInitializeREQ10() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario10();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_2, null );
   }


   /**
    * @see InitializeTestData.ScenarioREQ.setupInitializeREQScenario12
    */
   @Test
   public void testInitializeREQ12() throws Exception {
      InitializeTestData.ScenarioREQ.setupInitializeREQScenario12();

      execute( InitializeTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved results", 1, iDataSet.getRowCount() );

      testRow( InitializeTestData.TASK_TASK_KEY_REQ_1, null );
   }


   /**
    * Creates the database data
    *
    * @throws Exception
    *            an error occurred
    */
   @Before
   public void loadData() throws Exception {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(),
            GetInitializeTasksByInventoryTest.class, "TestData.xml" );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           The Start Date of the Scheduling Window
    *
    * @throws Exception
    *            if an exception occurs
    */
   private void execute( InventoryKey aInventoryKey ) throws Exception {

      // Copy data from vw_h_baseline_task to the global temporary table
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", lArgs );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.bsync.initialize.GetInitializeTasksByInventory", lArgs );
      iDataSet.setFilter( "dsBoolean(init_task_bool)" );
   }


   /**
    * Tests that the returned row is correct
    *
    * @param aTaskTaskKey
    *           The task key to be initialized
    * @param aParentTaskKey
    *           The parent task
    */
   private void testRow( TaskTaskKey aTaskTaskKey, TaskKey aParentTaskKey ) {
      MxTestUtils.getRow( iDataSet, "parent_task_key", aParentTaskKey );
      MxAssert.assertEquals( aTaskTaskKey, iDataSet.getKey( TaskTaskKey.class, "task_task_key" ) );
      MxAssert.assertEquals( aParentTaskKey, iDataSet.getKey( TaskKey.class, "parent_task_key" ) );
   }


   /**
    * Internal class that houses the test data
    *
    * @author cdaley
    */
   protected static class InitializeTestData {

      /** inventory key 1 */
      private static final InventoryKey ASSY_INVENTORY_1 = new InventoryKey( 1, 1 );

      /** inventory key 2 */
      private static final InventoryKey ASSY_INVENTORY_2 = new InventoryKey( 1, 2 );

      /** inventory key 3 */
      private static final InventoryKey ASSY_INVENTORY_3 = new InventoryKey( 1, 3 );

      /** Inventory key 4 */
      private static final InventoryKey TRK_INVENTORY_1 = new InventoryKey( 1, 4 );

      /** Inventory key 5 */
      private static final InventoryKey TRK_INVENTORY_2 = new InventoryKey( 1, 5 );

      /** bom item position 1 */
      private static final ConfigSlotPositionKey BOM_ITEM_POSITION_1 =
            new ConfigSlotPositionKey( 1, "TESTBOM", 1, 1 );

      /** bom item position 2 */
      private static final ConfigSlotPositionKey BOM_ITEM_POSITION_2 =
            new ConfigSlotPositionKey( 1, "TESTBOM", 1, 2 );

      /** task defn for block */
      private static final TaskDefnKey TASK_DEFN_KEY_BLOCK_1 = new TaskDefnKey( 1000, 1 );

      /** task defn for jic */
      private static final TaskDefnKey TASK_DEFN_KEY_JIC_1 = new TaskDefnKey( 2000, 1 );

      /** task defn for sibling jic */
      private static final TaskDefnKey TASK_DEFN_KEY_SIBLING_JIC_1 = new TaskDefnKey( 2001, 1 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_1 = new TaskDefnKey( 3000, 1 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_2 = new TaskDefnKey( 3000, 2 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_3 = new TaskDefnKey( 3000, 3 );

      /** task task for block 1 */
      private static final TaskTaskKey TASK_TASK_KEY_BLOCK_1 = new TaskTaskKey( 1001, 1 );

      /** task task for block 2 */
      private static final TaskTaskKey TASK_TASK_KEY_BLOCK_2 = new TaskTaskKey( 1001, 2 );

      /** task task for req 1 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_1 = new TaskTaskKey( 2001, 1 );

      /** task task for req 2 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_2 = new TaskTaskKey( 2001, 2 );

      /** task task for req 3 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_3 = new TaskTaskKey( 2001, 3 );

      /** task task for jic 1 */
      private static final TaskTaskKey TASK_TASK_KEY_JIC_1 = new TaskTaskKey( 3001, 1 );

      /** task task for jic 2 */
      private static final TaskTaskKey TASK_TASK_KEY_JIC_2 = new TaskTaskKey( 3001, 2 );

      /** task task for sibling jic 1 */
      private static final TaskTaskKey TASK_TASK_KEY_SIBLING_JIC_1 = new TaskTaskKey( 3002, 1 );

      /** actual task for block 1 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_1 = new TaskKey( 1002, 1 );

      /** actual task for block 2 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_2 = new TaskKey( 1002, 2 );

      /** actual task for req 1 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_1 = new TaskKey( 2002, 1 );

      /** actual task for req 2 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_2 = new TaskKey( 2002, 2 );

      /** actual task for req 3 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_3 = new TaskKey( 2002, 3 );

      /** actual task for jic 1 */
      private static final TaskKey ACTUAL_TASK_KEY_JIC_1 = new TaskKey( 3002, 1 );

      /** actual task for jic 2 */
      private static final TaskKey ACTUAL_TASK_KEY_JIC_2 = new TaskKey( 3002, 2 );

      /** actual task for sibling jic 1 */
      private static final TaskKey ACTUAL_TASK_KEY_SIBLING_JIC_1 = new TaskKey( 3003, 1 );

      private static final MaintPrgmDefnKey MAINT_PRGM_DEFN_KEY_1 = new MaintPrgmDefnKey( 4000, 1 );
      private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 1, "TESTBOM" );

      /** maintenance program key for MP 1 */
      private static final MaintPrgmKey MAINT_PRGM_KEY_1 = new MaintPrgmKey( 5000, 1 );

      /** maintenance program key for MP 2 */
      private static final MaintPrgmKey MAINT_PRGM_KEY_2 = new MaintPrgmKey( 5000, 2 );

      /** maintenance program key for MP 3 */
      private static final MaintPrgmKey MAINT_PRGM_KEY_3 = new MaintPrgmKey( 5000, 3 );
      private static final PartNoKey PART_NO_KEY_1 = new PartNoKey( 7000, 1 );
      private static final PartNoKey PART_NO_KEY_2 = new PartNoKey( 7000, 2 );


      /**
       * DOCUMENT_ME
       *
       * @author jclarkin
       */
      private static class ScenarioBasic {

         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Multiple Inventory Exist</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision on All inventory inventory</li>
          * <li>No actual tasks</li>
          * </ol>
          * <br>
          * Expected Result: Only return 1 row with task definition associated to the inventory<br>
          */
         private static void setupGeneralScenario01() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_2, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_3, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create block task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create block task task with BUILD status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has existing 2 BLOCKS without REQ, 1 REQ of type already exists</li>
          * <li>Task Definition is marked as unique</li>
          * </ol>
          * <br>
          * Expected Result: Return no rows<br>
          */
         private static void setupGeneralScenario02() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // set tasktask to unique
            TaskTaskTable lTaskTask = TaskTaskTable.findByPrimaryKey( TASK_TASK_KEY_REQ_1 );
            lTaskTask.setUniqueBool( true );
            lTaskTask.update();

            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual blocks
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_BLOCK_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_BLOCK_1, RefEventStatusKey.ACTV );

            // create block req mapping
            TestDataInitializer.setupBlockReqMap( TASK_TASK_KEY_BLOCK_1, TASK_DEFN_KEY_REQ_1 );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );
         }
      }

      /**
       * DOCUMENT_ME
       *
       * @author jclarkin
       */
      private static class ScenarioBLOCK {

         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition BLOCK exists</li>
          * <li>Task Definition has no ACTV revision</li>
          * <li>No actual tasks</li>
          * </ol>
          * <br>
          * Expected Result: No rows returned<br>
          */
         private static void setupInitializeBLOCKScenario01() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create block task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create block task task with BUILD status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.SUPRSEDE,
                  RefTaskClassKey.CHECK );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition BLOCK exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual tasks</li>
          * <li>No maintenance program</li>
          * </ol>
          * <br>
          * Expected Result: Return block<br>
          */
         private static void setupInitializeBLOCKScenario02() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create block task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create block task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CHECK );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition BLOCK exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>Actual tasks</li>
          * <li>maintenance program</li>
          * </ol>
          * <br>
          * Expected Result: Return no BLOCK<br>
          */
         private static void setupInitializeBLOCKScenario03() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create block task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create block task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CHECK );

            // create actual task for task task on inventory
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_BLOCK_1, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition BLOCK exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual tasks</li>
          * <li>maintenance program</li>
          * </ol>
          * <br>
          * Expected Result: Return MP revision task_task<br>
          */
         private static void setupInitializeBLOCKScenario04() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create block task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create block task task with ACTV status revision 2
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CHECK );

            // create block task task with SUPERCEDE status revision 1
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.SUPRSEDE,
                  RefTaskClassKey.CHECK );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 2 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_BLOCK_1 ),
                  TASK_TASK_KEY_BLOCK_2 );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_BLOCK_1 ) ).delete();
         }
      }

      /**
       * DOCUMENT_ME
       *
       * @author jclarkin
       */
      private static class ScenarioJIC {

         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>No tasks exist with Assembly</li>
          * </ol>
          * <br>
          * Expected Result: No rows returned
          */
         private static void setupInitializeJICScenario01() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>No tasks exist with class mode code JIC/BLOCK/REQ</li>
          * <li>Tasks exist with other class mode code</li>
          * </ol>
          * <br>
          * Expected Result: No rows returned<br>
          */
         private static void setupInitializeJICScenario02() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task ADHOC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.ADHOC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for JIC</li>
          * <li>Not in a maintenance program</li>
          * <li>Has a jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * </ol>
          * <br>
          * Expected Result: Return JIC and actual req<br>
          */
         private static void setupInitializeJICScenario03() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>Actual Tasks Exist</li>
          * <li>Not in a maintenance program</li>
          * <li>Has a jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return JIC<br>
          */
         private static void setupInitializeJICScenario04() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual JIC
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_JIC_1, ACTUAL_TASK_KEY_REQ_1,
                  ASSY_INVENTORY_1, TASK_TASK_KEY_JIC_1, RefEventStatusKey.ACTV );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has no ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>Not in a maintenance program</li>
          * <li>Has a jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return JIC<br>
          */
         private static void setupInitializeJICScenario05() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.BUILD, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>Not in a maintenance program</li>
          * <li>Has no jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return JIC<br>
          */
         private static void setupInitializeJICScenario06() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>Not in a maintenance program</li>
          * <li>Has jic-req mapping</li>
          * <li>Parent req is CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return JIC<br>
          */
         private static void setupInitializeJICScenario07() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CORR );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>In a maintenance program</li>
          * <li>Has jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return JIC task definition corresponding to maintenance program
          * revision <br>
          */
         private static void setupInitializeJICScenario08() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );

            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_2, TASK_DEFN_KEY_REQ_1 );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_JIC_1 ),
                  TASK_TASK_KEY_JIC_2 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_JIC_1 ) ).delete();
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has no ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>In a maintenance program</li>
          * <li>Has jic-req mapping</li>
          * <li>Parent req is CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: No results<br>
          */
         private static void setupInitializeJICScenario09() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.BUILD, RefTaskClassKey.JIC );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CORR );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );

            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_2, TASK_DEFN_KEY_REQ_1 );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_JIC_1 ),
                  TASK_TASK_KEY_JIC_2 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_JIC_1 ) ).delete();
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition has no ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>In a maintenance program</li>
          * <li>Has jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return JIC task definition corresponding to maintenance program
          * revision <br>
          */
         private static void setupInitializeJICScenario10() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create JIC task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create REQ task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create JIC task task with BUILD status revision 2
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.BUILD, RefTaskClassKey.JIC );

            // create JIC task task with SUPERCEDE status revision 1
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.JIC );

            // create REQ task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_JIC_1 ),
                  TASK_TASK_KEY_JIC_2 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_JIC_1 ) ).delete();

            // create JIC req map to REQ
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_2, TASK_DEFN_KEY_REQ_1 );

            // create actual on REQ
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exist</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No actual tasks</li>
          * <li>Not in maintenance program</li>
          * <li>Has parent not CORR</li>
          * <li>Multiple actuals of the Parent</li>
          * </ol>
          * <br>
          * Expected Result: Return 3 row with task definition associated to acutal REQ<br>
          */
         private static void setupInitializeJICScenario11() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create JIC task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create JIC task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create REQ task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create the req task task with actv status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create link between JIC and REQ
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );

            // create multiple actuals
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_3, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exist</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>actual tasks exist on all but 1 parent req</li>
          * <li>Not in maintenance program</li>
          * <li>Has parent not CORR</li>
          * <li>Multiple actuals of the Parent</li>
          * </ol>
          * <br>
          * Expected Result: Return 1 task definition without actual against the req<br>
          */
         private static void setupInitializeJICScenario12() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create JIC task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create JIC task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create REQ task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create the req task task with actv status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create link between JIC and REQ
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );

            // create multiple actuals
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_3, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_JIC_1, ACTUAL_TASK_KEY_REQ_1,
                  ASSY_INVENTORY_1, TASK_TASK_KEY_JIC_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_JIC_2, ACTUAL_TASK_KEY_REQ_2,
                  ASSY_INVENTORY_1, TASK_TASK_KEY_JIC_1, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition JIC is mapped to Inventory via Part No</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for JIC</li>
          * <li>Not in a maintenance program</li>
          * <li>Has a jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * </ol>
          * <br>
          * Expected Result: Return JIC and actual req<br>
          */
         private static void setupInitializeJICScenario13() {

            // create inventory
            TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION_1, PART_NO_KEY_2,
                  RefInvClassKey.TRK, RefBOMClassKey.TRK );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, PART_NO_KEY_2, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_2, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition JIC exists</li>
          * <li>Task Definition JIC is mapped to Inventory via Part No</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for JIC</li>
          * <li>Not in a maintenance program</li>
          * <li>Has a jic-req mapping</li>
          * <li>Parent req is not CORR</li>
          * <li>Part No is in Part Group attached to config slot with multiple positions</li>
          * </ol>
          * <br>
          * Expected Result: Return JIC and actual req<br>
          */
         private static void setupInitializeJICScenario14() {

            // create inventory
            TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION_1, PART_NO_KEY_2,
                  RefInvClassKey.TRK, RefBOMClassKey.TRK );

            // create inventory at second position
            TestDataInitializer.setupInventory( TRK_INVENTORY_2, BOM_ITEM_POSITION_2, PART_NO_KEY_2,
                  RefInvClassKey.TRK, RefBOMClassKey.TRK );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, PART_NO_KEY_2, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create req task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create req
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_2, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req for TRK_INVENTORY_1
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create actual req for TRK_INVENTORY_2
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, TRK_INVENTORY_2,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            // create req jic map
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          * Simulates adding a new job card to an existing requirement where other job cards already
          * exist
          *
          * <ol>
          * <li>Inventory Exist</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition Sibling JIC has ACTV revision</li>
          * <li>Task Definition JIC has ACTV revision</li>
          * <li>actual task doesn't exist</li>
          * <li>actual sibling jic exists on req</li>
          * <li>Not in maintenance program</li>
          * <li>Has parent not CORR</li>
          * </ol>
          * <br>
          * Expected Result: Return 1 task definition without actual against the req<br>
          */
         private static void setupInitializeJICScenario15() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create JIC task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1 );

            // create task defn for sibling JIC
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_SIBLING_JIC_1 );

            // create JIC task task with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

            // create task task sibling JIC with ACTV status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_SIBLING_JIC_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_SIBLING_JIC_1, RefTaskDefinitionStatusKey.ACTV,
                  RefTaskClassKey.JIC );

            // create REQ task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create the req task task with actv status
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create link between JICs and REQ
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
            TestDataInitializer.setupJICReqMap( TASK_TASK_KEY_SIBLING_JIC_1, TASK_DEFN_KEY_REQ_1 );

            // create actuals
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );

            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_SIBLING_JIC_1,
                  ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1, TASK_TASK_KEY_SIBLING_JIC_1,
                  RefEventStatusKey.ACTV );
         }
      }

      /**
       * DOCUMENT_ME
       *
       * @author jclarkin
       */
      private static class ScenarioREQ {

         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for REQ</li>
          * <li>Not in a maintenance program</li>
          * <li>Has no parent block mapping</li>
          * <li></li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return REQ with null as parent<br>
          */
         private static void setupInitializeREQScenario01() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for REQ</li>
          * <li>Not in a maintenance program</li>
          * <li>Has parent block mapping</li>
          * </ol>
          * <br>
          * Expected Result: Return REQ and Block as parent<br>
          */
         private static void setupInitializeREQScenario02() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create the block defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create the block
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CHECK );

            // create an actual block
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_BLOCK_1, RefEventStatusKey.ACTV );

            // create block req mapping
            TestDataInitializer.setupBlockReqMap( TASK_TASK_KEY_BLOCK_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>Actual Tasks Exist</li>
          * <li>No parent block</li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return REQ<br>
          */
         private static void setupInitializeREQScenario03() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has no ACTV revision</li>
          * <li>Not in a maintenance program</li>
          * <li>No parent block</li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Do not return REQ<br>
          */
         private static void setupInitializeREQScenario04() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.BUILD, RefTaskClassKey.REQ );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>In a maintenance program</li>
          * <li>No parent block</li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return REQ from maintenance program<br>
          */
         private static void setupInitializeREQScenario05() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_REQ_1 ),
                  TASK_TASK_KEY_REQ_2 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) ).delete();
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist</li>
          * <li>In a maintenance program</li>
          * <li>parent block</li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return REQ from maintenance program with parent blocks<br>
          */
         private static void setupInitializeREQScenario06() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 2,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

            // create task task for block
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.CHECK );

            // create actual block
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_BLOCK_1, RefEventStatusKey.ACTV );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_REQ_1 ),
                  TASK_TASK_KEY_REQ_2 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) ).delete();

            // link block to req
            TestDataInitializer.setupBlockReqMap( TASK_TASK_KEY_BLOCK_1, TASK_DEFN_KEY_REQ_1 );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Not a maintenance program</li>
          * <li>Req is one-time not recurring</li>
          * <li>Historic completed instance already exists</li>
          * </ol>
          * <br>
          * Expected Result: No requirements should be returned<br>
          */
         private static void setupInitializeREQScenario07() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create actual block
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_1, RefEventStatusKey.COMPLETE );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ1 exists</li>
          * <li>Task Definition REQ2 exists</li>
          * <li>Task Definition REQ3 exists</li>
          * <li>Not a maintenance program</li>
          * <li>Req is one-time not recurring</li>
          * <li>Req1 has a CRT link to Req2. Req2 has a CRT link to Req3</li>
          * <li>Actual of Req 3 exists</li>
          * </ol>
          * <br>
          * Expected Result: No requirements should be returned<br>
          */
         private static void setupInitializeREQScenario08() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_2 );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_3 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // set tasktask to unique
            TaskTaskTable lTaskTask = TaskTaskTable.findByPrimaryKey( TASK_TASK_KEY_REQ_1 );
            lTaskTask.setUniqueBool( true );
            lTaskTask.update();

            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_2, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // set tasktask to unique
            lTaskTask = TaskTaskTable.findByPrimaryKey( TASK_TASK_KEY_REQ_2 );
            lTaskTask.setUniqueBool( true );
            lTaskTask.update();

            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, BOM_ITEM_POSITION_1, 1,
                  TASK_DEFN_KEY_REQ_3, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // set tasktask to unique
            lTaskTask = TaskTaskTable.findByPrimaryKey( TASK_TASK_KEY_REQ_3 );
            lTaskTask.setUniqueBool( true );
            lTaskTask.update();

            // setup task dependancies
            TestDataInitializer.setupTaskDep( new TaskTaskDepKey( TASK_TASK_KEY_REQ_1, 1 ),
                  TASK_DEFN_KEY_REQ_2, RefTaskDepActionKey.CRT );

            TestDataInitializer.setupTaskDep( new TaskTaskDepKey( TASK_TASK_KEY_REQ_2, 1 ),
                  TASK_DEFN_KEY_REQ_3, RefTaskDepActionKey.CRT );

            // create an actual req
            TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_3, ASSY_INVENTORY_1,
                  TASK_TASK_KEY_REQ_3, RefEventStatusKey.ACTV );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>Inventory Exists</li>
          * <li>Task Definition REQ exists</li>
          * <li>Task Definition has ACTV revision</li>
          * <li>No Actual Tasks Exist for REQ</li>
          * <li>Not in a maintenance program</li>
          * <li>Has no parent block mapping</li>
          * <li>Task Definition REQ is mapped to the Inventory via Part No</li>
          * </ol>
          * <br>
          * Expected Result: Return REQ with null as parent<br>
          */
         private static void setupInitializeREQScenario09() {

            // create inventory
            TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION_1, PART_NO_KEY_1,
                  RefInvClassKey.TRK, RefBOMClassKey.TRK );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task JIC
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
                  TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>ACTV Maintenace Program Exists</li>
          * <li>SUPERSEDED REQ (rev1) assigned to ACTV program</li>
          * <li>SUPERSEDED REQ (rev2) temporarily issued</li>
          * <li>ACTV REQ (rev3) exists</li>
          * <li>Revision 2 of maintenace program exists</li>
          * <li>REQ (rev3) is assigned to MP in revision status</li> *
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Return REQ 2 because REQ 3 has not been temporarily issued<br>
          */
         private static void setupInitializeREQScenario10() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1,
                  BOM_ITEM_POSITION_1.getBomItemKey(), null, 1, TASK_DEFN_KEY_REQ_1,
                  RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP task lined to supercede task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_1, TASK_DEFN_KEY_REQ_1 ),
                  TASK_TASK_KEY_REQ_1 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) ).delete();

            // create task task REQ 2
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2,
                  BOM_ITEM_POSITION_1.getBomItemKey(), null, 2, TASK_DEFN_KEY_REQ_1,
                  RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

            // create MP 2
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_2, MAINT_PRGM_DEFN_KEY_1, 2,
                  RefMaintPrgmStatusKey.REVISION );

            // create MP 2 carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_2, TestDataInitializer.CARRIER_KEY_1 ),
                  false, 2 );

            TestDataInitializer.setupTempIssue( MAINT_PRGM_DEFN_KEY_1,
                  TestDataInitializer.CARRIER_KEY_1, TASK_DEFN_KEY_REQ_1, TASK_TASK_KEY_REQ_2 );

            // create task task REQ 3
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3,
                  BOM_ITEM_POSITION_1.getBomItemKey(), null, 3, TASK_DEFN_KEY_REQ_1,
                  RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create MP 2 task lined to ACTV task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_2, TASK_DEFN_KEY_REQ_1 ),
                  TASK_TASK_KEY_REQ_3 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) ).delete();
         }


         /**
          * Setup test scenario for inventory where:<br>
          *
          * <ol>
          * <li>MP(rev1) is ACTV with no requirements</li>
          * <li>REQ 1 is assigned to MP(rev2) in REVISION status</li>
          * <li>REQ 1 is temporarily issued</li>
          * <li></li>
          * </ol>
          * <br>
          * Expected Result: Temporary revision is returned<br>
          */
         private static void setupInitializeREQScenario12() {

            // create inventory
            TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION_1,
                  RefInvClassKey.ASSY );

            // create MP defn
            TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

            // create MP 1
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_1, MAINT_PRGM_DEFN_KEY_1, 1,
                  RefMaintPrgmStatusKey.ACTV );

            // create MP 1 carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_1, TestDataInitializer.CARRIER_KEY_1 ),
                  true, 1 );

            // create MP 2
            TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY_2, MAINT_PRGM_DEFN_KEY_1, 2,
                  RefMaintPrgmStatusKey.REVISION );

            // create MP 2 carrier map
            TestDataInitializer.setupMaintPrgmCarriermap(
                  new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY_2, TestDataInitializer.CARRIER_KEY_1 ),
                  false, 2 );

            // create task defn
            TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1 );

            // create task task REQ 1
            TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1,
                  BOM_ITEM_POSITION_1.getBomItemKey(), null, 1, TASK_DEFN_KEY_REQ_1,
                  RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

            // create MP 2 task lined to actv task
            TestDataInitializer.setupMaintPrgmTask(
                  new MaintPrgmTaskKey( MAINT_PRGM_KEY_2, TASK_DEFN_KEY_REQ_1 ),
                  TASK_TASK_KEY_REQ_1 );
            TestDataInitializer.setupTempIssue( MAINT_PRGM_DEFN_KEY_1,
                  TestDataInitializer.CARRIER_KEY_1, TASK_DEFN_KEY_REQ_1, TASK_TASK_KEY_REQ_1 );
            TaskFleetApprovalTable
                  .findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) ).delete();
         }
      }
   }
}
