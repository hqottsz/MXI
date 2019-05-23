
package com.mxi.mx.core.unittest.bsync.synchronizer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
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
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefMaintPrgmStatusKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This is the test suite for the GetCancelTasksByInventory query file
 *
 * @author cdaley
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCancelTasksByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition, Revision 1</li>
    * <li>Part No of Inventory and Revision 1 are the same</li>
    * <li>Revision 1, 2 are SUPRSEDE, Task Def is in Revision 3 , ACTV, exists and with the same
    * Part No as inventory Part No</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * DO NOT cancel a task as it is applicable based on part number
    */
   @Test
   public void testCancel_PartNo() {
      CancelTestData.setupCancel_PartNo();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: no Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition in ACTV check</li>
    * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
    * it</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * Cancel a task as it is not applicable anymore and in ACTV check
    */
   @Test
   public void testCancel_PartNo_ACTV_Check() {
      CancelTestData.setupCancel_PartNo_ACTV_Check();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: 1 Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_2, CancelTestData.TASK_DEFN_KEY_REQ_1, null );
   }


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition in COMMIT check</li>
    * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
    * it</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * DO NOT cancel a task as it is in IN COMMIT check
    */
   @Test
   public void testCancel_PartNo_COMMIT_Check() {
      CancelTestData.setupCancel_PartNo_COMMIT_Check();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: no Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition in IN WORK check</li>
    * <li>Task Def is in Rev 3, ACTV, exists but not applicable anymore as part No removed from
    * it</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * DO NOT cancel a task as it is in IN WORK check
    */
   @Test
   public void testCancel_PartNo_IN_WORK_Check() {
      CancelTestData.setupCancel_PartNo_IN_WORK_Check();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: no Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition</li>
    * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
    * it</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * Cancel a task as it is not applicable anymore
    *
    * @throws SQLException
    */
   @Test
   public void testCancel_PartNo_Removed() throws SQLException {
      CancelTestData.setupCancel_PartNo_Removed();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: 1 Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_2, CancelTestData.TASK_DEFN_KEY_REQ_1, null // CancelTestData.TASK_TASK_KEY_REQ_2
      );
   }


   /**
    * <ol>
    * <li>Inventory exists</li>
    * <li>Actual Task exists from Part No Based Task Definition</li>
    * <li>Task Def is in Rev 3 , Obsolete, and part No removed from it</li>
    * <li>No maintenance program exists</li>
    * </ol>
    * <br>
    * Cancel a task as it is not applicable anymore
    */
   @Test
   public void testCancel_PartNo_Removed_Obsolete() {
      CancelTestData.setupCancel_PartNo_Removed_Obsolete();

      execute( CancelTestData.TRK_INVENTORY_1 );

      // Expected Result: 1 Actual task in the return list of tasks to be canceled
      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_2, CancelTestData.TASK_DEFN_KEY_REQ_1, null // CancelTestData.TASK_TASK_KEY_REQ_2
      );
   }


   /**
    * @see CancelTestData.setupCancelScenario01
    */
   @Test
   public void testCancelScenario01() {
      CancelTestData.setupCancelScenario01();

      execute( CancelTestData.ASSY_INVENTORY_2 );

      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see CancelTestData.setupCancelScenario03
    */
   @Test
   public void testCancelScenario03() {
      CancelTestData.setupCancelScenario03();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see CancelTestData.setupCancelScenario04
    */
   @Test
   public void testCancelScenario04() {
      CancelTestData.setupCancelScenario04();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 3, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_JIC_1, CancelTestData.ACTUAL_JIC_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_JIC_1, CancelTestData.TASK_DEFN_KEY_JIC_1, null );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_1, CancelTestData.TASK_DEFN_KEY_REQ_1, null );

      testRow( CancelTestData.ACTUAL_TASK_KEY_BLOCK_1, CancelTestData.ACTUAL_BLOCK_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_BLOCK_1, CancelTestData.TASK_DEFN_KEY_BLOCK_1, null );
   }


   /**
    * @see CancelTestData.setupCancelScenario05
    */
   @Test
   public void testCancelScenario05() {
      CancelTestData.setupCancelScenario05();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see CancelTestData.setupCancelScenario06
    */
   @Test
   public void testCancelScenario06() {
      CancelTestData.setupCancelScenario06();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see CancelTestData.setupCancelScenario07
    */
   @Test
   public void testCancelScenario07() {
      CancelTestData.setupCancelScenario07();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_1, CancelTestData.TASK_DEFN_KEY_REQ_1, null );
   }


   /**
    * @see CancelTestData.setupCancelScenario08
    */
   @Test
   public void testCancelScenario08() {
      CancelTestData.setupCancelScenario08();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_JIC_1, CancelTestData.ACTUAL_JIC_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_JIC_1, CancelTestData.TASK_DEFN_KEY_JIC_1,
            CancelTestData.TASK_TASK_KEY_JIC_1 );
   }


   /**
    * @see CancelTestData.setupCancelScenario09
    */
   @Test
   public void testCancelScenario09() {
      CancelTestData.setupCancelScenario09();

      execute( CancelTestData.ASSY_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * @see CancelTestData.setupCancelScenario11
    */
   @Test
   public void testCancelScenario11() {
      CancelTestData.setupCancelScenario11();

      execute( CancelTestData.TRK_INVENTORY_1 );

      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_1, CancelTestData.TASK_DEFN_KEY_REQ_1, null );
   }


   /**
    * @see CancelTestData.setupCancelScenario12
    */
   @Test
   public void testCancelScenario12() {
      CancelTestData.setupCancelScenario12();

      execute( CancelTestData.TRK_TO_SER_INVENTORY );

      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      testRow( CancelTestData.ACTUAL_TASK_KEY_REQ_1, CancelTestData.ACTUAL_REQ_BARCODE_1,
            CancelTestData.TASK_TASK_KEY_REQ_1, CancelTestData.TASK_DEFN_KEY_REQ_1, null );
   }


   /**
    * This scenario consists of an actual task that is candidate to be cancelled therefore, it will
    * be gathered by the execution of GetCancelTasksByInventory query.
    *
    * <ul>
    * <li>Two SER parts exists in the same part group</li>
    * <li>There is a task definition based on one of the parts</li>
    * <li>An inventory is created for one of the parts</li>
    * <li>An actual task exists and is initialized from the task definition</li>
    * <li>The inventory is being updated to point to the other part of the part group</li>
    * </ul>
    */
   @Test
   public void testCancelTaskWhenNoLongerApplicable() {

      // create a part group
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "TESTGROUP" ).build();

      // create a SER part
      PartNoKey lPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "STD_PART" ).withShortDescription( "Standard Part" )
            .withInventoryClass( RefInvClassKey.SER ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withStatus( RefPartStatusKey.ACTV ).isAlternateIn( lPartGroup ).build();

      // create a SER part that is an alternate to the first part
      PartNoKey lAltPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "ALT_PART" ).withShortDescription( "Alternate Part" )
            .withInventoryClass( RefInvClassKey.SER ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withStatus( RefPartStatusKey.ACTV ).isAlternateIn( lPartGroup ).build();

      // create inventory for the first part
      InventoryKey lInventory = new InventoryBuilder().withPartGroup( lPartGroup )
            .withPartNo( lPart ).withClass( RefInvClassKey.SER ).build();

      // create REQ task definition based on the first part
      List<PartNoKey> lPartKeyList = new ArrayList<PartNoKey>();
      lPartKeyList.add( lPart );

      TaskTaskKey lTaskDef = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( "REQ_1" ).withPart( lPartKeyList )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).withRevisionNumber( 1 ).build();

      // initialized Task
      String lBarcode = "BARCODE";

      TaskKey lActualTask = new TaskBuilder().onInventory( lInventory ).withTaskRevision( lTaskDef )
            .withStatus( RefEventStatusKey.ACTV ).withBarcode( lBarcode ).build();

      // change inventory to point to second part
      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lInventory );
      lInvTable.setPartNo( lAltPart );
      lInvTable.update();

      // execute GetCancelTasksByInventory query
      execute( lInventory );

      // assert that only one task is candidate to be cancelled
      Assert.assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );
      iDataSet.first();

      // assert that the actual task is being brough for the query execution
      Assert.assertEquals( "actual_task_key", lActualTask,
            iDataSet.getKey( TaskKey.class, "actual_task_key" ) );

      Assert.assertEquals( "barcode_sdesc", lBarcode, iDataSet.getString( "barcode_sdesc" ) );
   }


   /**
    * Creates the database data
    *
    * @throws Exception
    *            an error occurred
    */
   @Before
   public void setUp() throws Exception {
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
      lArgs.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.bsync.GetCancelTasksByInventory", lArgs );
   }


   /**
    * Tests that the returned row is correct
    *
    * @param aActualTaskKey
    *           The parent task
    * @param aBarcodeSdesc
    *           the task barcode
    * @param aTaskTaskKey
    *           The task key to be initialized
    * @param aTaskDefnKey
    *           the task defn
    * @param aRevisionTaskKey
    *           the revision task task
    */
   private void testRow( TaskKey aActualTaskKey, String aBarcodeSdesc, TaskTaskKey aTaskTaskKey,
         TaskDefnKey aTaskDefnKey, TaskTaskKey aRevisionTaskKey ) {
      MxTestUtils.getRow( iDataSet, "actual_task_key", aActualTaskKey );
      Assert.assertEquals( "actual_task_key", aActualTaskKey,
            iDataSet.getKey( TaskKey.class, "actual_task_key" ) );
      Assert.assertEquals( "barcode_sdesc", aBarcodeSdesc, iDataSet.getString( "barcode_sdesc" ) );
      Assert.assertEquals( "task_task_key", aTaskTaskKey,
            iDataSet.getKey( TaskTaskKey.class, "task_task_key" ) );
      Assert.assertEquals( "task_defn_key", aTaskDefnKey,
            iDataSet.getKey( TaskDefnKey.class, "task_defn_key" ) );
      Assert.assertEquals( "revision_task_key", aRevisionTaskKey,
            iDataSet.getKey( TaskTaskKey.class, "revision_task_key" ) );
   }


   /**
    * Internal class that houses the test data
    *
    * @author cdaley
    */
   private static class CancelTestData {

      /** inventory key 1 */
      private static final InventoryKey ASSY_INVENTORY_1 = new InventoryKey( 1, 1 );

      /** inventory key 2 */
      private static final InventoryKey ASSY_INVENTORY_2 = new InventoryKey( 1, 2 );

      private static final InventoryKey TRK_TO_SER_INVENTORY = new InventoryKey( 1, 3 );

      private static final InventoryKey TRK_INVENTORY_1 = new InventoryKey( 1, 3 );

      /** bom item position */
      private static final ConfigSlotPositionKey BOM_ITEM_POSITION =
            new ConfigSlotPositionKey( 1, "TESTBOM", 1, 1 );

      /** task defn for block */
      private static final TaskDefnKey TASK_DEFN_KEY_BLOCK_1 = new TaskDefnKey( 1000, 1 );

      /** task defn for jic */
      private static final TaskDefnKey TASK_DEFN_KEY_JIC_1 = new TaskDefnKey( 2000, 1 );

      /** task defn for req */
      private static final TaskDefnKey TASK_DEFN_KEY_REQ_1 = new TaskDefnKey( 3000, 1 );

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

      /** actual task for block 1 */
      private static final TaskKey ACTUAL_TASK_KEY_BLOCK_1 = new TaskKey( 1002, 1 );

      /** actual task for req 1 */
      private static final TaskKey ACTUAL_TASK_KEY_REQ_1 = new TaskKey( 2002, 1 );

      /** Check 1 */
      private static final TaskKey CHECK_1 = new TaskKey( 7999, 1 );

      /** actual task for jic 1 */
      private static final TaskKey ACTUAL_TASK_KEY_JIC_1 = new TaskKey( 3002, 1 );

      /** actual task for adhoc 1 */
      private static final TaskKey ACTUAL_TASK_KEY_ADHOC_1 = new TaskKey( 7002, 1 );

      /** maintenance program defn */
      private static final MaintPrgmDefnKey MAINT_PRGM_DEFN_KEY_1 = new MaintPrgmDefnKey( 4000, 1 );

      /** assembly key */
      private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 1, "TESTBOM" );

      /** maintenance program key */
      private static final MaintPrgmKey MAINT_PRGM_KEY = new MaintPrgmKey( 5000, 1 );

      /** barcode sdesc for actual req */
      public static final String ACTUAL_REQ_BARCODE_1 = "ACTUALREQ1";

      /** barcode sdesc for actual block */
      public static final String ACTUAL_BLOCK_BARCODE_1 = "ACTUALBLOCK1";

      /** barcode sdesc for actual JIC */
      public static final String ACTUAL_JIC_BARCODE_1 = "ACTUALJIC1";

      /** barcode sdesc for actual adhoc */
      public static final String ACTUAL_ADHOC_BARCODE_1 = "ACTUALADHOC1";
      private static final PartNoKey PART_NO_KEY_1 = new PartNoKey( 7000, 1 );

      private static final PartNoKey PART_NO_KEY_2 = new PartNoKey( 7000, 2 );


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition, Revision 1</li>
       * <li>Part No of Inventory and Revision 1 are the same</li>
       * <li>Revision 1, 2 are SUPRSEDE, Task Def is in Revision 3 , ACTV, exists and with the same
       * Part No as inventory Part No</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: no Actual task in the list of tasks to be canceled<br>
       */
      public static void setupCancel_PartNo() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - ACTV
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_1, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual task:
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition in ACTV check</li>
       * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
       * it</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: Actual task is to be canceled.<br>
       */
      public static void setupCancel_PartNo_ACTV_Check() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - ACTV
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_2, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         // setup COMMIT
         TestDataInitializer.setupCheckTask( CHECK_1, ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
               RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition in COMMIT check</li>
       * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
       * it</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: No actual task to be canceled.<br>
       */
      public static void setupCancel_PartNo_COMMIT_Check() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - ACTV
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_2, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         // setup COMMIT
         TestDataInitializer.setupCheckTask( CHECK_1, ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
               RefEventStatusKey.COMMIT );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition in IN WORK check</li>
       * <li>Task Def is in Rev 3, ACTV, exists but not applicable anymore as part No removed from
       * it</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: No actual task to be canceled.<br>
       */
      public static void setupCancel_PartNo_IN_WORK_Check() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - ACTV
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_2, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         // setup COMMIT
         TestDataInitializer.setupCheckTask( CHECK_1, ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
               RefEventStatusKey.IN_WORK );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition</li>
       * <li>Task Def is in Rev 3 , ACTV, exists but not applicable anymore as part No removed from
       * it</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: Return actual task details<br>
       */
      public static void setupCancel_PartNo_Removed() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - ACTV
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // part No is changed:
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_2, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition</li>
       * <li>Task Def is in Rev 3 , Obsolete, and part No removed from it</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: Actual task is to be canceled<br>
       */
      public static void setupCancel_PartNo_Removed_Obsolete() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defn:
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definition revisions: SUPRSEDE - SUPRSEDE - OBSOLETE
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // part No is changed:
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_3, PART_NO_KEY_2, 3,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1, PART_NO_KEY_1,
               TASK_TASK_KEY_REQ_2, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Not Exists</li>
       * </ol>
       * <br>
       * Expected Result: Return no rows<br>
       */
      public static void setupCancelScenario01() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Event historic</li>
       * </ol>
       * <br>
       * Expected Result: Return no rows<br>
       */
      public static void setupCancelScenario03() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( ACTUAL_TASK_KEY_REQ_1 );
         lEventTable.setHistBool( true );
         lEventTable.update();
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Tasks Exist for block, req, jic, adhoc class mode code</li>
       * <li>No ACTV revisions, or maint programs exist</li>
       * </ol>
       * <br>
       * Expected Result: Return rows for block, req, jic class mode code tasks sorted in JIC, REQ,
       * BLOCK order<br>
       */
      public static void setupCancelScenario04() {

         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_BLOCK_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1, 1 );

         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.CHECK );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_BLOCK_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_BLOCK_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.CHECK );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.JIC );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.JIC );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_BLOCK_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_BLOCK_1, ACTUAL_BLOCK_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_JIC_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_JIC_1, ACTUAL_JIC_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_ADHOC_1, ASSY_INVENTORY_1, null,
               ACTUAL_ADHOC_BARCODE_1, RefEventStatusKey.ACTV );

         SchedStaskTable lSchedStaskTable =
               SchedStaskTable.findByPrimaryKey( ACTUAL_TASK_KEY_ADHOC_1 );
         lSchedStaskTable.setTaskClass( RefTaskClassKey.ADHOC );
         lSchedStaskTable.update();

         // setup jic req map
         TestDataInitializer.setupJicReqMap( TASK_TASK_KEY_JIC_1, TASK_DEFN_KEY_REQ_1 );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory Exists</li>
       * <li>Actual Task exists</li>
       * <li>No ACTV revision</li>
       * <li>Maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: No row returned<br>
       */
      public static void setupCancelScenario05() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // setup actual tasks
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
               new MaintPrgmTaskKey( MAINT_PRGM_KEY, TASK_DEFN_KEY_REQ_1 ), TASK_TASK_KEY_REQ_1 );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists</li>
       * <li>ACTV task exists</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: No row returned<br>
       */
      public static void setupCancelScenario06() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists</li>
       * <li>No ACTV task exists</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: Return actual task details<br>
       */
      public static void setupCancelScenario07() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists for JIC</li>
       * <li>ACTV task exists</li>
       * <li>No maintenance program exists</li>
       * <li>No JIC req mapping</li>
       * </ol>
       * <br>
       * Expected Result: Return actual task details<br>
       */
      public static void setupCancelScenario08() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_JIC_1, 1 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_JIC_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_JIC_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.JIC );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_JIC_1, ASSY_INVENTORY_1,
               TASK_TASK_KEY_JIC_1, ACTUAL_JIC_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists for REQ</li>
       * <li>ACTV task exists</li>
       * <li>maintenance program exists</li>
       * <li>Req unassigned from maintenance program</li>
       * </ol>
       * <br>
       * Expected Result: No results returned<br>
       */
      public static void setupCancelScenario09() {
         TestDataInitializer.setupInventory( ASSY_INVENTORY_1, BOM_ITEM_POSITION,
               RefInvClassKey.ASSY );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 2 );

         // setup task definitions

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.ACTV, RefTaskClassKey.REQ );

         // setup actual tasks
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
               new MaintPrgmTaskKey( MAINT_PRGM_KEY, TASK_DEFN_KEY_REQ_1 ), TASK_TASK_KEY_REQ_1,
               true );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Inventory exists</li>
       * <li>Actual Task exists from Part No Based Task Definition</li>
       * <li>No ACTV task exists</li>
       * <li>No maintenance program exists</li>
       * </ol>
       * <br>
       * Expected Result: Return actual task details<br>
       */
      public static void setupCancelScenario11() {
         TestDataInitializer.setupInventory( TRK_INVENTORY_1, BOM_ITEM_POSITION, PART_NO_KEY_1,
               RefInvClassKey.TRK, RefBOMClassKey.TRK );

         // setup task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 1 );

         // setup task definitions
         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, PART_NO_KEY_1, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, PART_NO_KEY_1, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_INVENTORY_1,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );
      }


      /**
       * Setup test scenario for inventory where:<br>
       *
       * <ol>
       * <li>Requirement is obsolete</li>
       * <li>Config Slot is changed from TRK to SER</li>
       * </ol>
       * <br>
       * Expected Result: Return 1 row<br>
       */
      public static void setupCancelScenario12() {
         TestDataInitializer.setupInventory( TRK_TO_SER_INVENTORY, BOM_ITEM_POSITION,
               RefInvClassKey.TRK );

         // set up task defns
         TestDataInitializer.setupTaskDefn( TASK_DEFN_KEY_REQ_1, 2 );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_1, BOM_ITEM_POSITION, 1,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.SUPRSEDE, RefTaskClassKey.REQ );

         // setup actual tasks
         TestDataInitializer.setupActualTask( ACTUAL_TASK_KEY_REQ_1, TRK_TO_SER_INVENTORY,
               TASK_TASK_KEY_REQ_1, ACTUAL_REQ_BARCODE_1, RefEventStatusKey.ACTV );

         TestDataInitializer.setupTaskTask( TASK_TASK_KEY_REQ_2, BOM_ITEM_POSITION, 2,
               TASK_DEFN_KEY_REQ_1, RefTaskDefinitionStatusKey.OBSOLETE, RefTaskClassKey.REQ );

         // convert the inventory from TRK to SER
         InvInv lInvInv = new InvInv( TRK_TO_SER_INVENTORY );
         lInvInv.setInvClass( RefInvClassKey.SER );
         lInvInv.setPartGroup( null );
         lInvInv.setBomItemPosition( null );
         lInvInv.setOrigAssmbl( null );
         lInvInv.update();
      }
   }
}
