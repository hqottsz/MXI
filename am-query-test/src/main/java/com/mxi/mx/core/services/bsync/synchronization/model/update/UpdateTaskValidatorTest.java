
package com.mxi.mx.core.services.bsync.synchronization.model.update;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.synchronization.model.update.UpdateTask.UpdateTaskBuilder;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Tests {@linkplain UpdateTaskValidator}
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateTaskValidatorTest {

   private static final int REVISION_ONE = 1;
   private static final int REVISION_TWO = 2;

   private InventoryKey iInvKey;
   private TaskRevisionBuilder iTaskRevBuilder;
   private TaskDefnKey iTaskDefnKey;
   private TaskKey iTaskKey;
   private TaskTaskKey iNewTaskRev;
   private UpdateTaskBuilder iUpdateTaskBuilder;

   private Mockery iContext = new Mockery();
   private QueryAccessObject iMockQao = iContext.mock( QueryAccessObject.class );
   private QuerySet iQs = iContext.mock( QuerySet.class );

   private UpdateTaskValidator iUpdateTaskValidator = new UpdateTaskValidator( iMockQao );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that when the task is valid for updating that
    * {@linkplain UpdateTaskValidator#isValid(UpdateTask)} returns true (happy path test).
    */
   @Test
   public void testWhenTaskIsValidForUpdating() {

      withTaskBasedOnTaskDefinition( REVISION_ONE );
      withNewTaskDefinitionRevision( REVISION_TWO );

      // When the query is not empty it returned successful.
      withValidatorQueryIsEmpty( false );

      buildUpdateTask();
      UpdateTask lUpdateTask = iUpdateTaskBuilder.build();

      assertTrue( iUpdateTaskValidator.isValid( lUpdateTask ) );

      iContext.assertIsSatisfied();

   }


   /**
    * Verify that when an altogether new task definition is provided, which is difference than the
    * task definition of the task, that {@linkplain UpdateTaskValidator#isValid(UpdateTask)} returns
    * true.
    */
   @Test
   public void testWhenNewTaskDefinitionDiffersFromDefinitionOfTask() {

      withTaskBasedOnTaskDefinition( REVISION_ONE );

      // Set the new task revision to be an altogether different task definition.
      buildUpdateTaskWithAnotherTaskDefn();

      UpdateTask lUpdateTask = iUpdateTaskBuilder.build();

      assertTrue( iUpdateTaskValidator.isValid( lUpdateTask ) );

   }


   /**
    * Verify that when the revision number of the new task revision is less than the revision number
    * of the task, that {@linkplain UpdateTaskValidator#isValid(UpdateTask)} returns false.
    */
   @Test
   public void testWhenNewRevisionLessThanCurrentRevision() {

      withTaskBasedOnTaskDefinition( REVISION_TWO );

      // Set the new task revision number to be less than the task revision number.
      withNewTaskDefinitionRevision( REVISION_ONE );

      buildUpdateTask();
      UpdateTask lUpdateTask = iUpdateTaskBuilder.build();

      assertFalse( iUpdateTaskValidator.isValid( lUpdateTask ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Verify that when the UpdateTaskValidator query does not return any rows, that
    * {@linkplain UpdateTaskValidator#isValid(UpdateTask)} returns false.
    */
   @Test
   public void testWhenUpdateTaskValidatorQueryReturnsNoRows() {

      withTaskBasedOnTaskDefinition( REVISION_ONE );
      withNewTaskDefinitionRevision( REVISION_TWO );

      // When the query returns no rows it was unsuccessful.
      withValidatorQueryIsEmpty( true );

      buildUpdateTask();
      UpdateTask lUpdateTask = iUpdateTaskBuilder.build();

      assertFalse( iUpdateTaskValidator.isValid( lUpdateTask ) );

      iContext.assertIsSatisfied();
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      iInvKey = new InventoryBuilder().build();
   }


   private void buildUpdateTask() {

      iUpdateTaskBuilder = new UpdateTaskBuilder();
      iUpdateTaskBuilder.hInventory( iInvKey ).task( iTaskKey ).taskDefn( iTaskDefnKey )
            .taskRevision( iNewTaskRev );

   }


   private void buildUpdateTaskWithAnotherTaskDefn() {

      buildUpdateTask();

      TaskTaskKey lAnotherTaskDefnKey = new TaskRevisionBuilder().build();

      iUpdateTaskBuilder.taskRevision( lAnotherTaskDefnKey );
   }


   private void withNewTaskDefinitionRevision( int aRevisionNumber ) {

      iTaskRevBuilder.withRevisionNumber( aRevisionNumber );
      iNewTaskRev = iTaskRevBuilder.build();
   }


   private void withTaskBasedOnTaskDefinition( int aRevisionNumber ) {

      iTaskRevBuilder = new TaskRevisionBuilder();
      iTaskRevBuilder.withRevisionNumber( aRevisionNumber );
      TaskTaskKey lTaskRev = iTaskRevBuilder.build();

      iTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev ).getTaskDefn();
      iTaskKey = new TaskBuilder().withTaskRevision( lTaskRev ).onInventory( iInvKey ).build();
   }


   private void withValidatorQueryIsEmpty( final boolean aIsEmpty ) {

      iContext.checking( new Expectations() {

         {
            one( iQs ).isEmpty();
            will( returnValue( aIsEmpty ) );

            one( iMockQao ).executeQuery(
                  with( equal( "com.mxi.mx.core.query.bsync.update.UpdateTaskValidator" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( iQs ) );

         }
      } );
   }
}
