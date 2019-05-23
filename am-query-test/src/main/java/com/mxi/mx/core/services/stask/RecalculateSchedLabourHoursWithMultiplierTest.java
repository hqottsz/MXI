
package com.mxi.mx.core.services.stask;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.details.DetailsService;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test the DetailsService.recalculateSchedHours() method
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RecalculateSchedLabourHoursWithMultiplierTest {

   /** Global Logic Parm stub */
   private GlobalParametersStub iGlobalParams;

   /** The DetailsService instance */
   private DetailsService iService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test the DetailsService.recalculateSchedHours() method. The root task does not have schedule
    * hours multiplier, then the schedule labour hours is not updated for the labour type
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testRecalculateSchedHoursNoMultiplier() throws Exception {
      final double lSchedHours = 5.0;

      // create JIC task definition
      final TaskTaskKey lJIC = new TaskRevisionBuilder()
            .withLabourRequirement( RefLabourSkillKey.LBR, 1, lSchedHours ).build();

      // create root actual task
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" ).build();

      // create child actual task based on JIC task definition
      final TaskKey lChildTask = new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).withLabour( RefLabourSkillKey.LBR, lSchedHours ).build();

      // Build query arguments and execute query to get the SchedLabourKey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lChildTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.LBR, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "sched_labour", lArgs );
      lQuerySet.first();

      SchedLabourKey lSchedLabourKey =
            lQuerySet.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      // Assert that sched hour for TECH role prior to recalculate
      MxAssert.assertEquals( lSchedHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );

      // recalculate scheduled hours based on the sched hours multiplier
      iService = new DetailsService( lChildTask );
      iService.recalculateSchedHrs( lRootTask );

      // Assert that sched hour for TECH role after to recalculate didnt change since there is no
      // multiplier
      MxAssert.assertEquals( lSchedHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );
   }


   /**
    * Test the DetailsService.recalculateSchedHours() method. The root task has schedule hours
    * multiplier, then the schedule labour row hours are updated for each labour type. The labour
    * requirement for the task needs certification and inspection.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testRecalculateSchedHoursWithCertificationAndInspection() throws Exception {
      final double lMultiplier = 2.0;
      final double lWorkSchedHours = 2.5;
      final double lCertificationHours = 0.7;
      final double lInspectionHours = 0.5;

      // create JIC task definition
      final TaskTaskKey lJIC =
            new TaskRevisionBuilder().withLabourRequirement( RefLabourSkillKey.LBR, 1,
                  lWorkSchedHours, lCertificationHours, lInspectionHours ).build();

      // create root actual task
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" )
            .withSchedHoursMultiplier( lMultiplier ).build();

      // create child actual task based on JIC task definition
      final TaskKey lChildTask = new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).withLabour( RefLabourSkillKey.LBR, lWorkSchedHours,
                  lCertificationHours, lInspectionHours )
            .build();

      // Build query arguments and execute query to get the SchedLabourKey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lChildTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.LBR, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "sched_labour", lArgs );
      lQuerySet.first();

      SchedLabourKey lSchedLabourKey =
            lQuerySet.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      // Assert that sched hour for TECH role prior to recalculate
      MxAssert.assertEquals( lWorkSchedHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );

      // Assert that sched hour for CERT role after recalculating with multiplier
      MxAssert.assertEquals( lCertificationHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.CERT ).getSchedHours() );

      // Assert that sched hour for INSP role after recalculating with multiplier
      MxAssert.assertEquals( lInspectionHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.INSP ).getSchedHours() );

      // recalculate scheduled hours based on the sched hours multiplier
      iService = new DetailsService( lChildTask );
      iService.recalculateSchedHrs( lRootTask );

      // Assert that sched hour for TECH role after recalculating with multiplier
      MxAssert.assertEquals( lWorkSchedHours * lMultiplier, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );

      // Assert that sched hour for CERT role after recalculating with multiplier
      MxAssert.assertEquals( lCertificationHours * lMultiplier, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.CERT ).getSchedHours() );

      // Assert that sched hour for INSP role after recalculating with multiplier
      MxAssert.assertEquals( lInspectionHours * lMultiplier, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.INSP ).getSchedHours() );
   }


   /**
    * Test the DetailsService.recalculateSchedHours() method. The root task has schedule hours
    * multiplier, then the schedule labour row hours is updated for the labour type.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testRecalculateSchedHoursWithMultiplier() throws Exception {
      final double lMultiplier = 3.5;
      final double lSchedHours = 2.0;

      // create JIC task definition
      final TaskTaskKey lJIC = new TaskRevisionBuilder()
            .withLabourRequirement( RefLabourSkillKey.LBR, 1, lSchedHours ).build();

      // create root actual task
      final TaskKey lRootTask = new TaskBuilder().withName( "Root_Task" )
            .withSchedHoursMultiplier( lMultiplier ).build();

      // create child actual task based on JIC task definition
      final TaskKey lChildTask = new TaskBuilder().withName( "Child_Task" ).withTaskRevision( lJIC )
            .withParentTask( lRootTask ).withLabour( RefLabourSkillKey.LBR, lSchedHours ).build();

      // Build query arguments and execute query to get the SchedLabourKey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lChildTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.LBR, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "sched_labour", lArgs );
      lQuerySet.first();

      SchedLabourKey lSchedLabourKey =
            lQuerySet.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      // Assert that sched hour for TECH role prior to recalculate
      MxAssert.assertEquals( lSchedHours, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );

      // recalculate scheduled hours based on the sched hours multiplier
      iService = new DetailsService( lChildTask );
      iService.recalculateSchedHrs( lRootTask );

      // Assert that sched hour for TECH role after recalculating with multiplier
      MxAssert.assertEquals( lSchedHours * lMultiplier, SchedLabourRoleTable
            .findByForeignKey( lSchedLabourKey, RefLabourRoleTypeKey.TECH ).getSchedHours() );
   }

}
