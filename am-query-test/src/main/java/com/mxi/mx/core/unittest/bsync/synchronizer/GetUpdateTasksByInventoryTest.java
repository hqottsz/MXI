
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
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.bsync.synchronizer.GetInitializeTasksByInventoryTest.InitializeTestData;


/**
 * This is the test suite for the GetUpdateTasksByInventory query
 *
 * @author cdaley
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetUpdateTasksByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * @see InitializeTestData.setupUpdateScenario01
    */
   @Test
   public void testUpdateScenario01() {
      UpdateTestData.setupUpdateScenario01();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( UpdateTestData.ACTUAL_TASK_KEY_REQ_1, UpdateTestData.ACTUAL_REQ_BARCODE_1,
            UpdateTestData.TASK_TASK_KEY_REQ_1, UpdateTestData.TASK_TASK_KEY_REQ_2 );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario02
    */
   @Test
   public void testUpdateScenario02() {
      UpdateTestData.setupUpdateScenario02();

      execute( UpdateTestData.ASSY_INVENTORY_2 );

      MxAssert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario03
    */
   @Test
   public void testUpdateScenario03() {
      UpdateTestData.setupUpdateScenario03();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see {@link UpdateTestData#setupUpdateScenario04()}
    */
   @Test
   public void testUpdateScenario04() {
      UpdateTestData.setupUpdateScenario04();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 3, iDataSet.getRowCount() );

      testRow( UpdateTestData.ACTUAL_TASK_KEY_REQ_1, UpdateTestData.ACTUAL_REQ_BARCODE_1,
            UpdateTestData.TASK_TASK_KEY_REQ_1, UpdateTestData.TASK_TASK_KEY_REQ_2 );

      testRow( UpdateTestData.ACTUAL_TASK_KEY_REQ_2, UpdateTestData.ACTUAL_REQ_BARCODE_2,
            UpdateTestData.TASK_TASK_KEY_REQ_1, UpdateTestData.TASK_TASK_KEY_REQ_2 );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario05
    */
   @Test
   public void testUpdateScenario05() {
      UpdateTestData.setupUpdateScenario05();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( UpdateTestData.ACTUAL_TASK_KEY_REQ_1, UpdateTestData.ACTUAL_REQ_BARCODE_1,
            UpdateTestData.TASK_TASK_KEY_REQ_1, UpdateTestData.TASK_TASK_KEY_REQ_2 );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario06
    */
   @Test
   public void testUpdateScenario06() {
      UpdateTestData.setupUpdateScenario06();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( UpdateTestData.ACTUAL_TASK_KEY_REQ_1, UpdateTestData.ACTUAL_REQ_BARCODE_1,
            UpdateTestData.TASK_TASK_KEY_REQ_1, UpdateTestData.TASK_TASK_KEY_REQ_2 );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario07
    */
   @Test
   public void testUpdateScenario07() {
      UpdateTestData.setupUpdateScenario07();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see InitializeTestData.setupUpdateScenario08
    */
   @Test
   public void testUpdateScenario08() {
      UpdateTestData.setupUpdateScenario08();

      execute( UpdateTestData.ASSY_INVENTORY_1 );

      MxAssert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * Creates the database data
    *
    * @throws Exception
    *            an error occurred
    */
   @Before
   public void loadData() throws Exception {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(), "TestData.xml" );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           The Start Date of the Scheduling Window
    */
   private void execute( InventoryKey aInventoryKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInvNoDbId", aInventoryKey.getDbId() );
      lArgs.add( "aInvNoId", aInventoryKey.getId() );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.bsync.GetUpdateTasksByInventory", lArgs );
   }


   /**
    * Tests that the returned row is correct
    *
    * @param aActualTaskKey
    *           The actual task
    * @param aBarcodeSdesc
    *           The actual task barcode
    * @param aTaskTaskKey
    *           The task definition of the cancel task
    * @param aTaskRevisionKey
    *           The current revision of the cancel task
    */
   private void testRow( TaskKey aActualTaskKey, String aBarcodeSdesc, TaskTaskKey aTaskTaskKey,
         TaskTaskKey aTaskRevisionKey ) {
      MxTestUtils.getRow( iDataSet, "sched_task_key", aActualTaskKey );
      MxAssert.assertEquals( aActualTaskKey, iDataSet.getKey( TaskKey.class, "sched_task_key" ) );
      MxAssert.assertEquals( aBarcodeSdesc, iDataSet.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( aTaskTaskKey, iDataSet.getKey( TaskTaskKey.class, "task_task_key" ) );
      MxAssert.assertEquals( aTaskRevisionKey,
            iDataSet.getKey( TaskTaskKey.class, "new_task_task_key" ) );
   }


   /**
    * Internal class that houses the test data
    *
    * @author cdaley
    */
   private static class UpdateTestData {

      /** inventory key 1 */
      private static final InventoryKey ASSY_INVENTORY_1 = new InventoryKey( 1, 1 );

      /** inventory key 2 */
      private static final InventoryKey ASSY_INVENTORY_2 = new InventoryKey( 1, 2 );

      /** bom item position */
      private static final ConfigSlotPositionKey BOM_ITEM_POSITION =
            new ConfigSlotPositionKey( 1, "TESTBOM", 1, 1 );

      /** task defn for block */
      private static final TaskDefnKey TASK_DEFN_KEY_BLOCK_1 = new TaskDefnKey( 1000, 1 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_1 = new TaskDefnKey( 3000, 1 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_2 = new TaskDefnKey( 3000, 2 );

      /** task task for block 1 */
      private static final TaskTaskKey TASK_TASK_KEY_BLOCK_1 = new TaskTaskKey( 1001, 1 );

      /** task task for req 1 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_1 = new TaskTaskKey( 2001, 1 );

      /** task task for req 2 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_2 = new TaskTaskKey( 2001, 2 );

      /** task task for req 3 */
      private static final TaskTaskKey TASK_TASK_KEY_REQ_3 = new TaskTaskKey( 2001, 3 );

      /** actual task for block 1 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_1 = new TaskKey( 1002, 1 );

      /** actual task for block 2 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_2 = new TaskKey( 1002, 2 );

      /** actual task for block 3 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_3 = new TaskKey( 1002, 3 );

      /** actual task for block 4 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_4 = new TaskKey( 1002, 4 );

      /** actual task for block 5 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_5 = new TaskKey( 1002, 5 );

      /** actual task for block 6 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_6 = new TaskKey( 1002, 6 );

      /** actual task for block 7 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_7 = new TaskKey( 1002, 7 );

      /** actual task for req 1 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_1 = new TaskKey( 2002, 1 );

      /** actual task for req 2 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_2 = new TaskKey( 2002, 2 );

      /** actual task for req 3 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_3 = new TaskKey( 2002, 3 );

      /** actual task for req 4 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_4 = new TaskKey( 2002, 4 );

      /** actual task for req 5 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_5 = new TaskKey( 2002, 5 );

      /** actual task for req 6 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_6 = new TaskKey( 2002, 6 );

      /** actual task for req 7 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_7 = new TaskKey( 2002, 7 );

      /** maintenance program defn */
      private static final MaintPrgmDefnKey MAINT_PRGM_DEFN_KEY_1 = new MaintPrgmDefnKey( 4000, 1 );

      /** assembly key */
      private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 1, "TESTBOM" );

      /** maintenance program key */
      private static final MaintPrgmKey MAINT_PRGM_KEY = new MaintPrgmKey( 5000, 1 );

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_1 = "ACTUALREQ1";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_2 = "ACTUALREQ2";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_3 = "ACTUALREQ3";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_4 = "ACTUALREQ4";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_5 = "ACTUALREQ5";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_6 = "ACTUALREQ6";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_7 = "ACTUALREQ7";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_1 = "ACTUALBLOCK1";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_2 = "ACTUALBLOCK2";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_3 = "ACTUALBLOCK3";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_4 = "ACTUALBLOCK4";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_5 = "ACTUALBLOCK5";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_6 = "ACTUALBLOCK6";

      /** barcode sdesc for actual req */
      public static final String ACTUAL_BLOCK_BARCODE_7 = "ACTUALBLOCK7";
      private static final PartNoKey PART_NO_KEY_1 = new PartNoKey( 7000, 1 );


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>2 Actual Tasks exist</li>
       * <li>1 Actual has work committed work package</li>
       * </ol>
       * <br>
       * Expected Result: Return the actual task for the ACTUALREQ1<br>
       */
      public static void setupUpdateScenario01() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ACTUAL_TASK_KEY_BLOCK_1,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1,
               RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ACTUAL_TASK_KEY_BLOCK_2,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_2,
               RefEventStatusKey.ACTV );

         // create actual blocks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_2, RefEventStatusKey.COMMIT );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>2 Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>no committed work package</li>
       * </ol>
       * <br>
       * Expected Result: Return return no actuals when using inventory 2<br>
       */
      public static void setupUpdateScenario02() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         TestDataInitializer.setupInventory( ASSY_INVENTORY_2, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ACTUAL_TASK_KEY_BLOCK_1,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1,
               RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ACTUAL_TASK_KEY_BLOCK_2,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_2,
               RefEventStatusKey.ACTV );

         // create actual blocks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_2, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>no committed work package</li>
       * <li>events are historic</li>
       * </ol>
       * <br>
       * Expected Result: Return return no actuals<br>
       */
      public static void setupUpdateScenario03() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ACTUAL_TASK_KEY_BLOCK_1,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1,
               RefEventStatusKey.ACTV );

         EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( ACTUAL_TASK_KEY_REQ_1 );
         lEvtEvent.setHistBool( true );
         lEvtEvent.update();

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ACTUAL_TASK_KEY_BLOCK_2,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_2,
               RefEventStatusKey.ACTV );

         lEvtEvent = EvtEventTable.findByPrimaryKey( ACTUAL_TASK_KEY_REQ_2 );
         lEvtEvent.setHistBool( true );
         lEvtEvent.update();

         // create actual blocks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_2, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>Work packages for every status exist against tasks</li>
       * </ol>
       * <br>
       * Expected Result: Returns the 3 actual tasks which have uncommited work packages<br>
       */
      public static void setupUpdateScenario04() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         TestDataInitializer.setupInventory( ASSY_INVENTORY_2, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ACTUAL_TASK_KEY_BLOCK_1,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1,
               RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ACTUAL_TASK_KEY_BLOCK_2,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_2,
               RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_3, ACTUAL_TASK_KEY_BLOCK_3,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_3,
               RefEventStatusKey.CANCEL );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_4, ACTUAL_TASK_KEY_BLOCK_4,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_4,
               RefEventStatusKey.COMMIT );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_5, ACTUAL_TASK_KEY_BLOCK_5,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_5,
               RefEventStatusKey.COMPLETE );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_6, ACTUAL_TASK_KEY_BLOCK_6,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_6,
               RefEventStatusKey.IN_WORK );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_7, ACTUAL_TASK_KEY_BLOCK_7,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_7,
               RefEventStatusKey.FORECAST );

         // create actual blocks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_2, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_3, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_3, RefEventStatusKey.CANCEL );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_4, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_4, RefEventStatusKey.COMMIT );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_5, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_5, RefEventStatusKey.COMPLETE );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_6, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_6, RefEventStatusKey.IN_WORK );

         // This is an orphaned forecasted task and should not be pick up by query.
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_7, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_7, RefEventStatusKey.FORECAST );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>Actv task exists</li>
       * <li>Maintenance program exists</li>
       * <li>no work package</li>
       * </ol>
       * <br>
       * Expected Result: Returns maintenance program revision<br>
       */
      public static void setupUpdateScenario05() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, BOM_ITEM_POSITION, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         // create MP defn
         TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

         // create MP
         TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY, MAINT_PRGM_DEFN_KEY_1, 1,
               RefMaintPrgmStatusKey.ACTV );

         // create MP carrier map
         TestDataInitializer.setupMaintPrgmCarriermap(
               new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY, TestDataInitializer.CARRIER_KEY_1 ),
               true, 1 );

         // create MP task lined to supercede task
         TestDataInitializer.setupMaintPrgmTask(
               new MaintPrgmTaskKey( MAINT_PRGM_KEY, TASK_DEFN_KEY_REQ_1 ), TASK_TASK_KEY_REQ_2 );
         TaskFleetApprovalTable.findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) )
               .delete();
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>no Actv task exists</li>
       * <li>Maintenance program exists</li>
       * <li>no work package</li>
       * </ol>
       * <br>
       * Expected Result: Returns maintenance program revision<br>
       */
      public static void setupUpdateScenario06() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         // create MP defn
         TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

         // create MP
         TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY, MAINT_PRGM_DEFN_KEY_1, 1,
               RefMaintPrgmStatusKey.ACTV );

         // create MP carrier map
         TestDataInitializer.setupMaintPrgmCarriermap(
               new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY, TestDataInitializer.CARRIER_KEY_1 ),
               true, 1 );

         // create MP task lined to supercede task
         TestDataInitializer.setupMaintPrgmTask(
               new MaintPrgmTaskKey( MAINT_PRGM_KEY, TASK_DEFN_KEY_REQ_1 ), TASK_TASK_KEY_REQ_2 );
         TaskFleetApprovalTable.findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) )
               .delete();
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>no Actv task exists</li>
       * <li>no Maintenance program exists</li>
       * <li>no work package</li>
       * </ol>
       * <br>
       * Expected Result: Returns no results<br>
       */
      public static void setupUpdateScenario07() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks exists</li>
       * <li>Actv task exists on correct revision</li>
       * <li>Maintenance program exists</li>
       * <li>no committed work package</li>
       * </ol>
       * <br>
       * Expected Result: Returns no results<br>
       */
      public static void setupUpdateScenario08() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_2, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_2, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_3, ACTUAL_REQ_BARCODE_2, RefEventStatusKey.ACTV );

         // create MP defn
         TestDataInitializer.setupMaintPrgmDefn( MAINT_PRGM_DEFN_KEY_1, ASSEMBLY_KEY, 1 );

         // create MP
         TestDataInitializer.setupMaintPrgm( MAINT_PRGM_KEY, MAINT_PRGM_DEFN_KEY_1, 1,
               RefMaintPrgmStatusKey.ACTV );

         // create MP carrier map
         TestDataInitializer.setupMaintPrgmCarriermap(
               new MaintPrgmCarrierMapKey( MAINT_PRGM_KEY, TestDataInitializer.CARRIER_KEY_1 ),
               true, 1 );

         // create MP task lined to supercede task
         TestDataInitializer.setupMaintPrgmTask(
               new MaintPrgmTaskKey( MAINT_PRGM_KEY, TASK_DEFN_KEY_REQ_1 ), TASK_TASK_KEY_REQ_1 );
         TaskFleetApprovalTable.findByPrimaryKey( new TaskFleetApprovalKey( TASK_DEFN_KEY_REQ_1 ) )
               .delete();
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>2 Actual Tasks exist for Part No. Based Task Definitions</li>
       * <li>1 Actual has work committed work package</li>
       * </ol>
       * <br>
       * Expected Result: Return the actual task for the ACTUALREQ1<br>
       */
      public static void setupUpdateScenario09() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.ASSY, RefBOMClassKey.ROOT );

         // create task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         // create task tasks
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // create actual reqs
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ACTUAL_TASK_KEY_BLOCK_1,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1,
               RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_2, ACTUAL_TASK_KEY_BLOCK_2,
               ASSY_INVENTORY_1, TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_2,
               RefEventStatusKey.ACTV );

         // create actual blocks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_2, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_2, RefEventStatusKey.COMMIT );
      }
   }
}
