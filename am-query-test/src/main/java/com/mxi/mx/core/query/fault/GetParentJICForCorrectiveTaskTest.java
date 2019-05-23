
package com.mxi.mx.core.query.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Test the GetParentJICForCorrectiveTask query using builder for data set up
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetParentJICForCorrectiveTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;
   private ConfigSlotKey iConfigSlot_ACFT1;
   private TaskTaskKey iTaskRevisionReq;


   /**
    * This ensure that the query return a row, since the fault is build for an actual task based on
    * a task definition which has a class mode code = JIC
    *
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testReturnsARow() throws Exception {

      // Create REQ definition
      iTaskRevisionReq = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( iConfigSlot_ACFT1 ).withTaskCode( "REQ_1" )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).build();

      // Create OPENPANEL task definition, which has task class mode code = JIC
      TaskTaskKey lTaskRevisionJIC = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.OPENPANEL ).withConfigSlot( iConfigSlot_ACFT1 )
            .withTaskCode( "JIC_1" ).withStatus( RefTaskDefinitionStatusKey.ACTV ).build();

      // Asociate JIC with REQ
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( iTaskRevisionReq );
      TaskJicReqMapTable lTable =
            TaskJicReqMapTable.create( lTaskRevisionJIC, lTaskTaskTable.getTaskDefn() );
      lTable.insert();

      // Create WP
      // iAircraft = new InventoryBuilder().build();

      TaskKey lWPKey = new TaskBuilder().onInventory( iAircraft ).withName( "WP_1" )
            .withTaskClass( RefTaskClassKey.CHECK ).withStatus( RefEventStatusKey.IN_WORK ).build();

      // Create REQ task based on task definition and assign to WP
      TaskKey lReqTaskKey = new TaskBuilder().withName( "REQ_TASK_1" )
            .withTaskRevision( iTaskRevisionReq ).withParentTask( lWPKey )
            .withStatus( RefEventStatusKey.ACTV ).withTaskClass( RefTaskClassKey.REQ ).build();

      // Create JIC task based on task definition
      TaskKey lJICTaskKey = new TaskBuilder().withName( "JIC_TASK_1" )
            .withTaskRevision( lTaskRevisionJIC ).withParentTask( lReqTaskKey )
            .withStatus( RefEventStatusKey.ACTV ).withTaskClass( RefTaskClassKey.JIC ).build();

      // Raise fault for previous task
      FaultKey lFault = new SdFaultBuilder().withName( "FAULT_1" ).build();
      TaskKey lCorrectiveTask = new TaskBuilder().withName( "CORRECTIVE_TASK_1" ).build();

      // create corrective task for fault
      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      // relate fault with actual JIC based on task definition
      new EventRelationshipBuilder().fromEvent( lJICTaskKey ).toEvent( lFault )
            .withType( RefRelationTypeKey.DISCF ).build();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lCorrectiveTask, new String[] { "aCorrDbId", "aCorrId" } );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert that one record is returned by the query since we created a fault
      // against a scheduled task based on a JIC task definiton
      assertTrue( lQs.first() );
      assertEquals( lQs.getInteger( "task_db_id" ), ( Integer ) lTaskRevisionJIC.getDbId() );
      assertEquals( lQs.getInteger( "task_id" ), ( Integer ) lTaskRevisionJIC.getId() );
   }


   /**
    * This ensure that the query does not return a row, since the fault is build for an actual task
    * based on a task definition which has a class mode code different than JIC
    *
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testReturnsNoRows() throws Exception {

      // Create WP
      TaskKey lWPKey = new TaskBuilder().onInventory( iAircraft ).withName( "WP_2" )
            .withTaskClass( RefTaskClassKey.CHECK ).withStatus( RefEventStatusKey.IN_WORK ).build();

      // Create REQ task based on task definition and assign to WP
      TaskKey lReqTaskKey = new TaskBuilder().withName( "REQ_TASK_2" )
            .withTaskRevision( iTaskRevisionReq ).withParentTask( lWPKey )
            .withStatus( RefEventStatusKey.ACTV ).withTaskClass( RefTaskClassKey.REQ ).build();

      // Raise fault for previous task
      FaultKey lFault = new SdFaultBuilder().withName( "FAULT_2" ).build();
      TaskKey lCorrectiveTask = new TaskBuilder().withName( "CORRECTIVE_TASK_1" ).build();

      // create corrective task for fault
      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      // relate fault with actual JIC based on task definition
      new EventRelationshipBuilder().fromEvent( lReqTaskKey ).toEvent( lFault )
            .withType( RefRelationTypeKey.DISCF ).build();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lCorrectiveTask, new String[] { "aCorrDbId", "aCorrId" } );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // assert that no record is returned by the query
      assertTrue( !lQs.next() );
   }


   /**
    * Sets up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {
      iAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).build();
   }
}
