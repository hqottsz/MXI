
package com.mxi.mx.core.query.bsync.update;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static com.mxi.mx.core.key.RefEventStatusKey.PAUSE;
import static com.mxi.mx.core.key.RefTaskClassKey.JIC;
import static com.mxi.mx.core.key.RefTaskClassKey.MPC;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Unit test for the UpdateTaskValidator query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateTaskValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   private InventoryKey iInventory;
   private ConfigSlotKey iConfigSlot;


   /**
    * Verify that when a task is valid for updating that the query returns a row. (happy path test)
    *
    * @throws SQLException
    *
    */
   @Test
   public void testWhenTaskIsValidForUpdating() throws SQLException {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertFalse( "Expected the query to return a row.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is marked as historical that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsHistorical() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).asHistoric().build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is neither a JIC, REQ, not BLOCK the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskClassIsNotUpdateable() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( RefTaskClassKey.ADHOC )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE ).withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( RefTaskClassKey.ADHOC ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task revision matches that of the active task definition revision that the
    * query returns no rows.
    *
    */
   @Test
   public void testWhenTaskRevisionMatchesTheActiveRevision() {

      withInventoryContext();

      TaskTaskKey lTaskRev1 =
            new TaskRevisionBuilder().withConfigSlot( iConfigSlot ).withTaskClass( REQ )
                  .withStatus( RefTaskDefinitionStatusKey.ACTV ).withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      withFleetApproval( lTaskDefn, lTaskRev1 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task definition has an obsoleted revision that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskHasDefinitionThatHasBeenObsoleted() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.OBSOLETE )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is not a BLOCK, REQ, or JIC that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsNotBlockReqJic() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( MPC ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( MPC ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is a JIC and is in work, that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsInWorkJic() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( IN_WORK ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is a JIC and is paused, that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsPausedJic() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( PAUSE ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is a JIC and is not in work nor paused, that the query returns a row.
    *
    */
   @Test
   public void testWhenTaskIsNeitherInWorkNorPausedJic() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( ACTV ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().withConfigSlot( iConfigSlot )
            .withTaskClass( JIC ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertFalse( "Expected the query to return a row.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is an executable REQ and is in work, that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsInWorkExecutableReq() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( IN_WORK ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is an executable REQ and is paused, that the query returns no rows.
    *
    */
   @Test
   public void testWhenTaskIsPausedExecutableReq() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( PAUSE ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertTrue( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Verify that when a task is an executable REQ and is not in work nor paused, that the query
    * returns a row.
    *
    */
   @Test
   public void testWhenTaskIsNeitherInWorkNorPausedExecutableReq() {

      withInventoryContext();

      // Create a superseded task definition revision.
      TaskTaskKey lTaskRev1 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.SUPRSEDE )
            .withRevisionNumber( 1 ).build();

      // Initialize a task using the task definition revision.
      TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev1 ).withStatus( ACTV ).build();

      // Get the task definition key.
      TaskDefnKey lTaskDefn = TaskTaskTable.findByPrimaryKey( lTaskRev1 ).getTaskDefn();

      // Create another revision for that task definition and make it active.
      TaskTaskKey lTaskRev2 = new TaskRevisionBuilder().isExecutableRequirement()
            .withConfigSlot( iConfigSlot ).withStatus( RefTaskDefinitionStatusKey.ACTV )
            .withTaskDefn( lTaskDefn ).withRevisionNumber( 2 ).build();

      withFleetApproval( lTaskDefn, lTaskRev2 );

      DataSet lDs = executeQuery( lTask );

      assertFalse( "Expected the query to return no rows.", lDs.isEmpty() );
   }


   /**
    * Sets up fleet approval for the provided task definition and revision.
    *
    * @param aTaskDefnKey
    *
    * @param aTaskTaskKey
    */
   private void withFleetApproval( TaskDefnKey aTaskDefnKey, TaskTaskKey aTaskTaskKey ) {

      // Create a fleet approval for the task definition (i.e. indicating it is not part of a MP).
      TaskFleetApprovalKey lApproval = new TaskFleetApprovalKey( aTaskDefnKey );
      TaskFleetApprovalTable lApprovalTable = TaskFleetApprovalTable.create( lApproval );
      lApprovalTable.setTaskRevision( aTaskTaskKey );
      lApprovalTable.insert();
   }


   /**
    * Sets up the inventory for testing.
    *
    */
   private void withInventoryContext() {

      ConfigSlotPositionKey lConfigSlotPos = new ConfigSlotPositionBuilder().build();

      iInventory = new InventoryBuilder().withConfigSlotPosition( lConfigSlotPos ).build();

      iConfigSlot = lConfigSlotPos.getBomItemKey();
   }


   private DataSet executeQuery( TaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aSchedDbId", "aSchedId" );
      lArgs.add( iInventory, "aInvDbId", "aInvId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
