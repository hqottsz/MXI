package com.mxi.mx.web.query.labour;

import static com.mxi.am.domain.Domain.createAdhocTask;
import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;
import static com.mxi.mx.core.key.RefLabourSkillKey.LBR;
import static com.mxi.mx.core.key.RefLabourSkillKey.PILOT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.tuple.Pair;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.labour.LabourScheduler.qrx.
 */
public class LabourSchedulerTest {

   @ClassRule
   public static DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule dakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   /**
    *
    * Verify that all tasks with labour skills assigned to work package are returned from the query
    * when executed query all task labour skills of the work package are showed correct.
    *
    */
   @Test
   public void returnTasksWithAllLabourSkills() {
      // Given an aircraft for the tasks.
      // This inventory is needed by the query but is not related to this scenario.
      InventoryKey lAircraft = createAircraft();

      // Given a work package with tasks both assigned.
      // And both tasks with labour skills
      TaskKey task1 = createAdhocTask( task -> {
         task.setInventory( lAircraft );
         task.addLabour( labour -> {
            labour.setSkill( ENG );
         } );
         task.addLabour( labour -> {
            labour.setSkill( PILOT );
         } );
         task.addLabour( labour -> {
            labour.setSkill( LBR );
         } );
      } );
      TaskKey task2 = createAdhocTask( task -> {
         task.setInventory( lAircraft );
         task.addLabour( labour -> {
            labour.setSkill( LBR );

         } );
      } );
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addTask( task1 );
         wp.addTask( task2 );
      } );

      // When the query is executed. All labour requirements of the work package are showed.
      DataSetArgument args = new DataSetArgument();
      args.add( workPackage, "aCheckDbId", "aCheckId" );
      QuerySet lQs = QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );

      // Then returned total number of all task labour skills.
      assertThat( "Unexpectd number of labour skills returned.", lQs.getRowCount(), is( 4 ) );

      // Ensure task1 and task2 added into work package
      List<Pair<TaskKey, String>> actualResult = new ArrayList<>();
      while ( lQs.next() ) {
         actualResult.add( new Pair<TaskKey, String>( lQs.getKey( TaskKey.class, "task_key" ),
               lQs.getString( "labour_skill_cd" ) ) );
      }
      Pair<TaskKey, String> task1WithENGSkill = new Pair<TaskKey, String>( task1, ENG.getCd() );
      Pair<TaskKey, String> task1WitPILOTSkill = new Pair<TaskKey, String>( task1, PILOT.getCd() );
      Pair<TaskKey, String> task1WitLBRSkill = new Pair<TaskKey, String>( task1, LBR.getCd() );
      Pair<TaskKey, String> task2WitLBRSkill = new Pair<TaskKey, String>( task2, LBR.getCd() );

      assertThat( actualResult, Matchers.containsInAnyOrder( task1WithENGSkill, task1WitPILOTSkill,
            task1WitLBRSkill, task2WitLBRSkill ) );

   }

}
