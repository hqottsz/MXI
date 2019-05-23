package com.mxi.mx.core.ejb.stask.staskbean;

import static com.mxi.am.domain.Domain.createAdhocTask;
import static com.mxi.am.domain.Domain.createCrew;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtDept;


/**
 * Integration unit test for
 * {@link STaskBean#assignCrews(TaskKey, DepartmentKey[], HumanResourceKey)}
 *
 */
public class AssignCrewsTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   // Hard coded department key indicating an "unassigned crew" within Maintenix.
   private static final DepartmentKey UNASSIGNED_CREW = new DepartmentKey( -1, -1 );

   private STaskBean sTaskBean;


   @Before
   public void before() {
      sTaskBean = new STaskBean();
      // Set the session context to avoid a NPE if ever the underlying code throws an exception.
      sTaskBean.setSessionContext( new SessionContextFake() );
   }


   /**
    * Verify that a crew is no longer assigned to a task, which is assigned to a scheduled work
    * package, after the "unassigned crew" is assigned to the task.
    *
    * The "unassigned crew" is hard coded in Maintenix to be the DepartmentKey(-1,-1).
    */
   @Test
   public void assignTheUnassignedCrewToTaskInScheduleWp() throws Exception {

      // Given a crew.
      DepartmentKey crew = createCrew();

      // Given a task with the crew assigned.
      TaskKey adhocTask = createAdhocTask( task -> {
         task.setCrew( crew );
      } );

      // Given a scheduled work package with the task assigned.
      createWorkPackage( wp -> {
         wp.setScheduledStartDate( new Date() );
         wp.setScheduledEndDate( new Date() );
         wp.addTask( adhocTask );
      } );

      // When the "unassigned crew" is assigned to the task.
      DepartmentKey[] crews = new DepartmentKey[] { UNASSIGNED_CREW };
      sTaskBean.assignCrews( adhocTask, crews, createHumanResource() );

      // Then the crew is unassigned from the task
      assertFalse( "Crew still is unexpectedly assigned.",
            EvtDept.isCrewExists( adhocTask.getEventKey(), crew ) );
   }


   /**
    * Verify that a crew is no longer assigned to a task, which is assigned to a scheduled work
    * package, after unassigning the crew from the task.
    *
    * The act of un-assigning a crew from a task is accomplished by assigning no crew to the task.
    *
    */
   @Test
   public void unassignCrewFromTaskInScheduleWp() throws Exception {

      // Given a crew.
      DepartmentKey crew = createCrew();

      // Given a task with the crew assigned.
      TaskKey adhocTask = createAdhocTask( task -> {
         task.setCrew( crew );
      } );

      // Given a scheduled work package with the task assigned.
      createWorkPackage( wp -> {
         wp.setScheduledStartDate( new Date() );
         wp.setScheduledEndDate( new Date() );
         wp.addTask( adhocTask );
      } );

      // When no crew (null) is assigned to the task.
      sTaskBean.assignCrews( adhocTask, null, createHumanResource() );

      // Then the crew is unassigned from the task
      assertFalse( "Crew still is unexpectedly assigned.",
            EvtDept.isCrewExists( adhocTask.getEventKey(), crew ) );
   }


   /**
    * Verify that crews are no longer assigned to a work package after unassigning the crews from
    * the work package.
    *
    */
   @Test
   public void unassignCrewsFromWp() throws Exception {

      // Given crews.
      DepartmentKey crew1 = createCrew();
      DepartmentKey crew2 = createCrew();

      // Given a work package with the crews assigned.
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.addCrew( crew1 );
         wp.addCrew( crew2 );
      } );

      // When no crew (null) is assigned to the work package.
      sTaskBean.assignCrews( workpackage, null, createHumanResource() );

      // Then the crews are unassigned from the work package.
      assertFalse( "Unexpectedly, crew1 is still assigned to the work package.",
            EvtDept.isCrewExists( workpackage.getEventKey(), crew1 ) );
      assertFalse( "Unexpectedly, crew2 is still assigned to the work package.",
            EvtDept.isCrewExists( workpackage.getEventKey(), crew2 ) );
   }


   /**
    * Verify that crews are assigned to a committed work package
    *
    */
   @Test
   public void assignCrewsToCommitWp() throws Exception {

      // Given crews.
      DepartmentKey crew1 = createCrew();
      DepartmentKey crew2 = createCrew();

      // Given a committed work package.
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
      } );

      // When crews are assigned to the work package.
      DepartmentKey[] crews = new DepartmentKey[] { crew1, crew2 };
      sTaskBean.assignCrews( workpackage, crews, createHumanResource() );

      // Then the crews are assigned to the work package.
      assertTrue( "Unexpectedly, crew1 is not assigned to the work package.",
            EvtDept.isCrewExists( workpackage.getEventKey(), crew1 ) );
      assertTrue( "Unexpectedly, crew2 is not assigned to the work package.",
            EvtDept.isCrewExists( workpackage.getEventKey(), crew2 ) );
   }


   /**
    * Verify that crew are assigned to task
    *
    */
   @Test
   public void assignCrewToTask() throws Exception {

      // Given a crew.
      DepartmentKey crew = createCrew();

      // Given a adhoc task.
      TaskKey task = createAdhocTask();

      // When crew is assigned.
      sTaskBean.assignCrews( task, new DepartmentKey[] { crew }, createHumanResource() );

      // Then the crew is assigned to task.
      assertTrue( "Unexpectedly, crew is not assigned to task.",
            EvtDept.isCrewExists( task.getEventKey(), crew ) );
   }

}
