
package com.mxi.mx.core.query.bsync.initialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.BlockReqMapBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This class tests the GetInitializeOnConditionSubTasksByInventory query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetInitializeOnConditionSubTasksByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private TaskTaskKey iBlock;

   private TaskKey iBlockActual;

   private InventoryKey iInventory;

   private TaskDefnKey iReqDefinition;

   private TaskTaskKey iRequirement;


   /**
    * Test that requirements that are defined as create on aircraft install do not get included in
    * the results.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatCreateOnAcftInstallReturnsNoRows() throws Exception {
      TaskTaskTable lReqTaskTask = TaskTaskTable.findByPrimaryKey( iRequirement );
      lReqTaskTask.setCreateOnAcInstBool( true );
      lReqTaskTask.update();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Test that requirements that are defined as create on install do not get included in the
    * results.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatCreateOnInstallReturnsNoRows() throws Exception {
      TaskTaskTable lReqTaskTask = TaskTaskTable.findByPrimaryKey( iRequirement );
      lReqTaskTask.setCreateOnAnyInstBool( true );
      lReqTaskTask.update();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Tests that when there is an existing actual requirement on a superceded revision, no rows are
    * returned since there is no need to initialize a new one.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatExistingActualReqOnOldRevisionReturnsNoRows() throws Exception {

      // setup() creates tasks for iInventory, thus we will use a different inventory for this test.
      InventoryKey lInventoryKey = new InventoryBuilder().build();

      // create a superseded requirement task revision
      TaskTaskKey lOldRequirementRevision =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
                  .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE ).build();

      // build an actual requirement, no need to initialize a new one
      new TaskBuilder().onInventory( lInventoryKey ).withTaskRevision( lOldRequirementRevision )
            .withTaskClass( RefTaskClassKey.REQ ).withParentTask( iBlockActual ).build();

      DataSet lResult = executeQuery( lInventoryKey );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Tests that when there is an existing actual requirement, no rows are returned since there is
    * no need to initialize a new one.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatExistingActualReqReturnsNoRows() throws Exception {

      // build an actual requirement, no need to initialize a new one
      new TaskBuilder().onInventory( iInventory ).withTaskRevision( iRequirement )
            .withTaskClass( RefTaskClassKey.REQ ).withParentTask( iBlockActual ).build();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Tests that when there is an existing actual mandtory requirement, the row is still returned.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatExistingMandatoryReqReturnsRow() throws Exception {
      TaskDefnKey lMandatoryReqDefn = new TaskDefnKey( 4650, 3 );
      TaskTaskKey lMandatoryRequirement =
            new TaskRevisionBuilder().withTaskDefn( lMandatoryReqDefn )
                  .withTaskClass( RefTaskClassKey.REQ ).isMandatory().build();
      new BlockReqMapBuilder().from( iBlock ).to( lMandatoryReqDefn ).build();

      new TaskBuilder().onInventory( iInventory ).withTaskRevision( lMandatoryRequirement )
            .withTaskClass( RefTaskClassKey.REQ ).withParentTask( iBlockActual ).build();

      DataSet lResult = executeQuery( iInventory );

      assertEquals( 1, lResult.getRowCount() );

      lResult.first();

      assertEquals( iRequirement, lResult.getKey( TaskTaskKey.class, "task_task_key" ) );
      assertEquals( iBlockActual, lResult.getKey( TaskKey.class, "parent_task_key" ) );
      assertTrue( lResult.getBoolean( "init_task_bool" ) );
   }


   /**
    * Test that when the block is historic, no row is returned.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatHistoricBlockReturnsNoRows() throws Exception {
      EvtEventTable lBlockEvent = EvtEventTable.findByPrimaryKey( iBlockActual );
      lBlockEvent.setHistBool( true );
      lBlockEvent.update();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Test that when the requirement is mandatory, no row is returned.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatMandatoryReqReturnsNoRows() throws Exception {
      TaskTaskTable lReqTask = TaskTaskTable.findByPrimaryKey( iRequirement );
      lReqTask.setOnConditionBool( false );
      lReqTask.update();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Test that when the block is not mandatory, no row is returned.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatOnConditionBlockReturnsNoRows() throws Exception {
      TaskTaskTable lBlockTask = TaskTaskTable.findByPrimaryKey( iBlock );
      lBlockTask.setOnConditionBool( true );
      lBlockTask.update();

      DataSet lResult = executeQuery( iInventory );

      assertTrue( lResult.isEmpty() );
   }


   /**
    * Tests the basic flow. An on condition requirement is mapped to a mandtory block but no actual
    * requirement exists.
    *
    * @throws Exception
    *            If an error occurs.
    */

   @Test
   public void testThatOnConditionSubTaskReturnsRow() throws Exception {
      DataSet lResult = executeQuery( iInventory );

      assertEquals( 1, lResult.getRowCount() );

      lResult.first();

      assertEquals( iRequirement, lResult.getKey( TaskTaskKey.class, "task_task_key" ) );
      assertEquals( iBlockActual, lResult.getKey( TaskKey.class, "parent_task_key" ) );
      assertTrue( lResult.getBoolean( "init_task_bool" ) );
   }


   /**
    * Sets up the test case. Creates the test data.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {
      iInventory = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).build();

      iReqDefinition = new TaskDefnKey( 4650, 2 );

      iRequirement = new TaskRevisionBuilder().withTaskDefn( iReqDefinition )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .build();
      iBlock = new TaskRevisionBuilder().withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK )
            .mapToRequirement( iReqDefinition ).isMandatory().build();

      iBlockActual = new TaskBuilder().withTaskRevision( iBlock )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iInventory )
            .build();
   }


   /**
    * Executes the query with the given inventory.
    *
    * @param aInventory
    *           The inventory
    *
    * @return The results of the query
    */
   private DataSet executeQuery( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
