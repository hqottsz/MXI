package com.mxi.mx.core.query.bsync;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.common.dataset.SQLStatement;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.db.view.baselinetask.BaselineTestCase;


/**
 * This class ensures that the bsyncValidateActuals function works properly
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class BSyncValidateActualsTest extends BaselineTestCase {

   private MaintenanceProgram iMaintPrgm = new MaintenanceProgram();

   private TaskDefinition iReq1 = new TaskDefinition( COMPONENT_CONFIG_SLOT );
   private TaskDefinition iReq2 = new TaskDefinition( COMPONENT_CONFIG_SLOT );

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensure that tasks that used to (and not longer) have a dependency are not skipped
    *
    * @throws Exception
    */
   @Test
   public void testDoNotSkipTasksWithSeveredDependencies() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();
      iReq1.revise();
      removeDepedentOn( iReq1, iReq2 );
      iReq1.activate();

      assertThat( actualsFor( iReq2 ), is( false ) );
   }


   /**
    * Ensure that tasks that do not have dependencies are not skipped
    *
    * @throws Exception
    */
   @Test
   public void testNoDependencies() throws Exception {
      iReq1.revise();
      iReq1.activate();
      iReq2.revise();
      iReq2.activate();

      assertThat( actualsFor( iReq2 ), is( false ) );
   }


   /**
    * Ensure that tasks that do not have dependencies because of a maintenance program are not
    * skipped
    *
    * @throws Exception
    */
   @Test
   public void testNoDependencies_UsingMaintenanceProgram() throws Exception {

      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      iReq1.activate();
      iMaintPrgm.create();
      iMaintPrgm.assign( iReq1 );
      iMaintPrgm.activate();
      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      assertThat( actualsFor( iReq2 ), is( false ) );
   }


   /**
    * Ensure that tasks that have a dependency are skipped
    *
    * @throws Exception
    */
   @Test
   public void testSkipTasksWithDependencies() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      assertThat( actualsFor( iReq2 ), is( true ) );
   }


   /**
    * Ensure that tasks that have a dependency because of a maintenance program are skipped
    *
    * @throws Exception
    */
   @Test
   public void testSkipTasksWithDependencies_UsingMaintenanceProgram() throws Exception {
      iReq2.revise();
      iReq2.activate();

      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      iMaintPrgm.create();
      iMaintPrgm.assign( iReq1 );
      iMaintPrgm.activate();

      iReq1.revise();
      removeDepedentOn( iReq1, iReq2 );
      iReq1.activate();

      assertThat( actualsFor( iReq2 ), is( true ) );
   }


   /**
    * Ensure that tasks with new dependencies are skipped
    *
    * @throws Exception
    */
   @Test
   public void testSkipTasksWithNewDependencies() throws Exception {
      iReq2.revise();
      iReq2.activate();
      iReq1.revise();
      iReq1.activate();
      iReq1.revise();
      makeDependentOn( iReq1, iReq2 );
      iReq1.activate();

      assertThat( actualsFor( iReq2 ), is( true ) );
   }


   /**
    * Checks if the provided task has actuals
    *
    * @param aTaskDefinition
    *           the task
    *
    * @return TRUE if specified task has actuals
    *
    * @throws Exception
    */
   private boolean actualsFor( TaskDefinition aTaskDefinition ) throws Exception {

      // Copy data from vw_h_baseline_task to the global temporary table
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( INV_ACFT_CARRIER, "aInvNoDbId", "aInvNoId" );
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", lArgs );

      TaskTaskKey lTaskRevision = aTaskDefinition.getLatestTaskRevision();

      SQLStatement lStatement = new SQLStatement( String.format(
            "SELECT bsyncValidateActuals(%d,%d, %d,%d, 'REQ', NULL, NULL)"
                  + " AS has_actuals FROM dual",
            INV_ACFT_CARRIER.getDbId(), INV_ACFT_CARRIER.getId(), lTaskRevision.getDbId(),
            lTaskRevision.getId() ) );
      lStatement.prepare( iDatabaseConnectionRule.getConnection() );

      QuerySet lQs = lStatement.executeQuery( iDatabaseConnectionRule.getConnection() );
      if ( lQs.first() ) {
         return lQs.getBoolean( "has_actuals" );
      }

      Assert.fail( "cannot get dataset" );

      return false;
   }


   /**
    * Creates a dependency on maintenance program
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the task definition that this depends on
    */
   private void makeDependentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition ) {
      TaskTaskDepTable lTaskDepTable = TaskTaskDepTable
            .create( new TaskTaskDepKey( aTaskDefinition.getLatestTaskRevision(), 1 ) );
      lTaskDepTable.setDepTaskDefn( aDependentOnTaskDefinition.getTaskDefn() );
      lTaskDepTable.setTaskDepAction( RefTaskDepActionKey.CRT );
      lTaskDepTable.insert();
   }


   /**
    * Removes task dependency
    *
    * @param aTaskDefinition
    *           the task definition
    * @param aDependentOnTaskDefinition
    *           the dependent task definition
    *
    * @throws Exception
    */
   private void removeDepedentOn( TaskDefinition aTaskDefinition,
         TaskDefinition aDependentOnTaskDefinition ) throws Exception {
      TaskDefnService
            .removeLinkedTask( new TaskTaskDepKey( aTaskDefinition.getLatestTaskRevision(), 1 ) );
   }
}
