package com.mxi.mx.core.ejb.stask.staskbean;

import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.warning.MxWarning;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.WarningSchedule;
import com.mxi.mx.core.services.stask.WarningSchedule.WarningChildDueDateEarlierThanCheckStartDate;


/**
 * Integration test for validating the scheduled start date of a work package.
 *
 * Exercising: {@linkplain STaskBean#validateSchedule( TaskKey aCheck, Date aScheduledStartDate )}
 *
 */
public class ValidateScheduleTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private STaskBean staskBean;


   @Before
   public void before() {
      // Create a bean to test against.
      //
      // And set its session context to a fake so that errors will not masked by a NPE due to the
      // session context not being set.
      staskBean = new STaskBean();
      staskBean.setSessionContext( new SessionContextFake() );
   }


   /**
    * Verify that a warning is generated when validating the start date of a work package that is
    * after the due date of a task within the work package.
    */
   @Test
   public void scheduleWorkPackageAfterTaskDueDate() throws Exception {

      // Given a task with a due date.
      // Note, dates in the DB have a granularity of seconds.
      Date taskDueDate = floorSecond( new Date() );
      TaskKey task = Domain.createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( taskDueDate );
         } );
      } );

      // Given an active, unscheduled work package to which the task is assigned.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.setStatus( ACTV );
         wp.setScheduledStartDate( null );
         wp.setScheduledEndDate( null );
         wp.addTask( task );
      } );

      // Given a start date that is after the due date of the task.
      Date startDate = addDays( taskDueDate, 1 );

      // When the start date is validated as the scheduled start date of the work package.
      WarningSchedule warningSchedule = staskBean.validateSchedule( workPackage, startDate );

      // Then a WarningChildDueDateEarlierThanCheckStartDate warning is generated.
      List<MxWarning> warnings = warningSchedule.getWarning();
      assertThat( "Unexpected number of warnings.", warnings.size(), is( 1 ) );

      MxWarning warning = warnings.get( 0 );
      assertThat( "Unexpected warning class.", warning,
            instanceOf( WarningChildDueDateEarlierThanCheckStartDate.class ) );

      WarningChildDueDateEarlierThanCheckStartDate specificWarning =
            ( WarningChildDueDateEarlierThanCheckStartDate ) warning;
      assertThat( "Unexpected task in warning.", specificWarning.iChild, is( task ) );
      assertThat( "Unexpected task due date in warning.", specificWarning.iChildDueDate,
            is( taskDueDate ) );
   }


   /**
    * Verify that no warning is generated when validating the start date of a work package that is
    * before the due date of a task within the work package.
    */
   @Test
   public void scheduleWorkPackageBeforeTaskDueDate() throws Exception {

      // Given a task with a due date.
      // Note, dates in the DB have a granularity of seconds.
      Date taskDueDate = floorSecond( new Date() );
      TaskKey task = Domain.createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( taskDueDate );
         } );
      } );

      // Given an active, unscheduled work package to which the task is assigned.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.setStatus( ACTV );
         wp.setScheduledStartDate( null );
         wp.setScheduledEndDate( null );
         wp.addTask( task );
      } );

      // Given a start date that is before the due date of the task.
      Date startDate = addDays( taskDueDate, -1 );

      // When the start date is validated as the scheduled start date of the work package.
      WarningSchedule warningSchedule = staskBean.validateSchedule( workPackage, startDate );

      // Then no warning is generated.
      List<MxWarning> warnings = warningSchedule.getWarning();
      assertThat( "Unexpected number of warnings.", warnings.size(), is( 0 ) );
   }

}
