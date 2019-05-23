
package com.mxi.mx.core.services.taskdefn.exception;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Tests the behaviour of the RevisionAlreadyExistsValidator.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RevisionAlreadyExistsValidatorTest {

   private static final TaskDefnKey TASK_DEFN_KEY = new TaskDefnKey( 1, 1 );

   RevisionAlreadyExistsValidator iValidator;

   private int iTaskDefnRevisionOrd = 1;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException when checking a version of a task
    * definition that does not have a status of REVISION but another version of the task definition
    * does.
    */
   @Test
   public void testTaskDefnWithAnotherVersionHavingAStatusOfRevision() {
      withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.REVISION );

      TaskTaskKey lTaskTaskKey = withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.ACTV );

      try {
         iValidator.check( lTaskTaskKey );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         ;
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking a task
    * definition that has multiple versions but none have a status of REVISION.
    */
   @Test
   public void testTaskDefnWithNoVersionsHavingStatusOfRevision() {
      withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
      withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.SUPRSEDE );

      TaskTaskKey lTaskTaskKey = withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.ACTV );

      try {
         iValidator.check( lTaskTaskKey );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   /**
    * Ensure the validator throws a RevisionAlreadyExistsException when checking a version of a task
    * definition that has a status of REVISION.
    */
   @Test
   public void testTaskDefnWithStatusOfRevision() {
      TaskTaskKey lTaskTaskKey =
            withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.REVISION );

      try {
         iValidator.check( lTaskTaskKey );
         fail( "Expected RevisionAlreadyExistsException but it was not thrown." );
      } catch ( RevisionAlreadyExistsException e ) {
         ;
      }
   }


   /**
    * Ensure the validator does not throw a RevisionAlreadyExistsException when checking a version
    * of a task definition that has a status other than REVISION, and no other versions exist.
    */
   @Test
   public void testTaskDefnWithStatusOtherThanRevision() {
      TaskTaskKey lTaskTaskKey = withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey.ACTV );

      try {
         iValidator.check( lTaskTaskKey );
      } catch ( RevisionAlreadyExistsException e ) {
         fail( "Unexpected RevisionAlreadyExistsException" );
      }
   }


   @Before
   public void setUp() throws Exception {
      iValidator = new RevisionAlreadyExistsValidator();
   }


   /**
    * Create a task definition version in the DB that has the provided status.
    *
    * @param aStatus
    *           status of the task definition version
    *
    * @return the key of the task definition
    */
   private TaskTaskKey withTaskDefnVersionHavingStatus( RefTaskDefinitionStatusKey aStatus ) {
      TaskTaskTable lTaskTask = TaskTaskTable.create();
      lTaskTask.setTaskDefn( TASK_DEFN_KEY );
      lTaskTask.setTaskDefStatus( aStatus );
      lTaskTask.setRevisionOrd( iTaskDefnRevisionOrd++ );

      return lTaskTask.insert( TaskTaskTable.generatePrimaryKey() );
   }

}
