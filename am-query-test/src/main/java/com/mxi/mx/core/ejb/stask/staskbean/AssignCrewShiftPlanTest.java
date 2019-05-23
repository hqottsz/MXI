package com.mxi.mx.core.ejb.stask.staskbean;

import static com.mxi.am.domain.Domain.createAdhocTask;
import static com.mxi.am.domain.Domain.createCrew;
import static com.mxi.am.domain.Domain.createCrewShiftDay;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createShift;
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
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.OrgCrewShiftTaskKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtDept;
import com.mxi.mx.core.table.org.OrgCrewShiftTask;


/**
 *
 * Integration unit test for {@linkplain STaskBean#assignCrewShiftPlan}
 *
 */
public class AssignCrewShiftPlanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Hard coded "unassigned" crew-shift-day is defined as OrgCrewShiftPlanKey(-1,-1, -1) in
   // Maintenix.
   private static final OrgCrewShiftPlanKey UNASSIGNED_CREW_SHIFT_DAY =
         new OrgCrewShiftPlanKey( -1, -1, -1 );

   // Class under test.
   private STaskBean sTaskBean;


   @Before
   public void before() {
      sTaskBean = new STaskBean();
      // Set the session context to avoid a NPE if ever the underlying code throws an exception.
      sTaskBean.setSessionContext( new SessionContextFake() );
   }


   /**
    *
    * Verify that unassigning a crew-shift-day from a task that this assigned to a committed work
    * package results in the crew-shift-day no longer being assigned to the task.
    *
    */
   @Test
   public void unassignCrewShiftDayFromTaskInCommittedWorkPackage() throws Exception {

      // Given a crew shift day assigned to the task.
      DepartmentKey crew = createCrew();
      ShiftKey shift = createShift();
      Date shiftDay = DateUtils.floorSecond( new Date() );
      OrgCrewShiftPlanKey crewShiftDay = createCrewShiftDay( csd -> {
         csd.setCrew( crew );
         csd.setShift( shift );
         csd.setDay( shiftDay );
      } );

      // Given a task to which the crew shift day is assigned.
      TaskKey task = createAdhocTask( tsk -> {
         tsk.setCrewShiftDay( crewShiftDay );
      } );

      // Given a committed work package to which the task is assigned.
      createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( task );
      } );

      // When a crew shift day is unassigned from the task.
      TaskKey[] tasks = { task };
      sTaskBean.assignCrewShiftPlan( tasks, null, createHumanResource() );

      // Then the crew shift day is no longer assigned to the task.
      //
      // Note: if the crew is not assigned to the task AND if the crew shift is not assigned to the
      // task then that is enough to indicated that the crew shift day is not assigned to the task.
      assertFalse( "Crew unexpectedly assigned to task.",
            EvtDept.isCrewExists( task.getEventKey(), crew ) );
      assertFalse( "Crew shift unexpectedly assigned to task.",
            OrgCrewShiftTask
                  .findByPrimaryKey(
                        new OrgCrewShiftTaskKey( task, new OrgCrewShiftPlanKey( crew, 1 ) ) )
                  .exists() );
   }


   /**
    *
    * Verify that assigning the "unassigned" crew-shift-day to task, which already has an assigned
    * crew-shift-day and is itself assigned to a committed work package, results in the
    * crew-shift-day no longer being assigned to the task.
    *
    */
   @Test
   public void assignUnassignedCrewShiftDayToTaskInCommittedWorkPackage() throws Exception {

      // Given a crew shift day assigned to the task.
      DepartmentKey crew = createCrew();
      ShiftKey shift = createShift();
      Date shiftDay = DateUtils.floorSecond( new Date() );
      OrgCrewShiftPlanKey crewShiftDay = createCrewShiftDay( csd -> {
         csd.setCrew( crew );
         csd.setShift( shift );
         csd.setDay( shiftDay );
      } );

      // Given a task to which the crew shift day is assigned.
      TaskKey task = createAdhocTask( tsk -> {
         tsk.setCrewShiftDay( crewShiftDay );
      } );

      // Given a committed work package to which the task is assigned.
      createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( task );
      } );

      // When the "unassigned" crew shift day is assigned to the task.
      TaskKey[] tasks = { task };
      sTaskBean.assignCrewShiftPlan( tasks, UNASSIGNED_CREW_SHIFT_DAY, createHumanResource() );

      // Then the crew shift day is no longer assigned to the task.
      //
      // Note: if the crew is not assigned to the task AND if the crew shift is not assigned to the
      // task then that is enough to indicated that the crew shift day is not assigned to the task.
      assertFalse( "Crew unexpectedly assigned to task.",
            EvtDept.isCrewExists( task.getEventKey(), crew ) );
      assertFalse( "Crew shift unexpectedly assigned to task.",
            OrgCrewShiftTask
                  .findByPrimaryKey(
                        new OrgCrewShiftTaskKey( task, new OrgCrewShiftPlanKey( crew, 1 ) ) )
                  .exists() );
   }


   /**
    * Verify that crew-shift-day is assigned to task.
    *
    */
   @Test
   public void assignCrewShiftDayToTaskInCommittedWp() throws Exception {

      // Given a crew shift day
      DepartmentKey crew = createCrew();
      ShiftKey shift = createShift();
      Date shiftDay = DateUtils.floorSecond( new Date() );
      OrgCrewShiftPlanKey crewShiftDay = createCrewShiftDay( csd -> {
         csd.setCrew( crew );
         csd.setShift( shift );
         csd.setDay( shiftDay );
      } );

      // Given a task.
      TaskKey task = createAdhocTask();

      // When a crew shift day is assigned to the task.
      TaskKey[] tasks = { task };
      sTaskBean.assignCrewShiftPlan( tasks, crewShiftDay, createHumanResource() );

      // Then the crew shift day is assigned to the task.
      assertTrue( "Crew unexpectedly assigned to task.",
            EvtDept.isCrewExists( task.getEventKey(), crew ) );
      assertTrue( "Crew shift unexpectedly assigned to task.",
            OrgCrewShiftTask
                  .findByPrimaryKey(
                        new OrgCrewShiftTaskKey( task, new OrgCrewShiftPlanKey( crew, 1 ) ) )
                  .exists() );

   }

}
