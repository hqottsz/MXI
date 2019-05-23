
package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;


/**
 * This UT test the GetSchedHrsAndManHrsByRootTask query
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSchedHrsAndManHrsByRootTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query set */
   private QuerySet iQs;


   /**
    * The actual task based on the task definition has a labour row with all possible roles TECH,
    * CERT and INSP
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testGetSchedHrAndManHrsByRootTaskWithAllRoles() throws Exception {
      final double lWorkSchedHours = 2.5;
      final double lCertificationHours = 0.7;
      final double lInspectionHours = 0.5;

      // create JIC task definition
      final TaskTaskKey lJIC =
            new TaskRevisionBuilder().withLabourRequirement( RefLabourSkillKey.LBR, 1,
                  lWorkSchedHours, lCertificationHours, lInspectionHours ).build();

      // create root actual task
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" ).build();

      // create child actual task based on JIC task definition
      new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).withLabour( RefLabourSkillKey.LBR, lWorkSchedHours,
                  lCertificationHours, lInspectionHours )
            .build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRootTask, "aRootTaskDbId", "aRootTaskId" );

      iQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that 3 rows are retrieved by the query, each one containing the TECH, CERT and INSP
      // roles
      assertEquals( "Number of retrieved rows", 3, iQs.getRowCount() );
   }


   /**
    * The actual task based on the task definition has any labour role
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testGetSchedHrAndManHrsByRootTaskWithAnyRole() throws Exception {

      // create JIC task definition
      final TaskTaskKey lJIC = new TaskRevisionBuilder().build();

      // create root actual task without labor requirement
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" ).build();

      // create child actual task based on JIC task definition
      new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRootTask, "aRootTaskDbId", "aRootTaskId" );

      iQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that no row is retrieved by the query, since there is no labour row defined for the
      // actual task
      assertEquals( "Number of retrieved rows", 0, iQs.getRowCount() );
   }


   /**
    * The actual task based on the task definition has a labour row with TECH work only, no CERT and
    * no INSP
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testGetSchedHrAndManHrsByRootTaskWithTECHRole() throws Exception {
      final double lSchedHours = 5.0;

      // create JIC task definition
      final TaskTaskKey lJIC = new TaskRevisionBuilder()
            .withLabourRequirement( RefLabourSkillKey.LBR, 1, lSchedHours ).build();

      // create root actual task
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" ).build();

      // create child actual task based on JIC task definition
      new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).withLabour( RefLabourSkillKey.LBR, lSchedHours ).build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRootTask, "aRootTaskDbId", "aRootTaskId" );

      iQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that only one row is retrieved by the query
      assertEquals( "Number of retrieved rows", 1, iQs.getRowCount() );
      iQs.first();

      SchedLabourRoleKey lSchedLabourRoleKey = iQs.getKey( SchedLabourRoleKey.class, "role_key" );
      SchedLabourRoleTable lSchedLabourRoleTable =
            SchedLabourRoleTable.findByPrimaryKey( lSchedLabourRoleKey );

      // Assert labour role is TECH
      assertEquals( lSchedLabourRoleTable.getLabourRoleType(), RefLabourRoleTypeKey.TECH );
   }
}
