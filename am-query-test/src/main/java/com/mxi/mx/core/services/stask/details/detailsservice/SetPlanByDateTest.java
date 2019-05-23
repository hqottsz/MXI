package com.mxi.mx.core.services.stask.details.detailsservice;

import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.InvalidPlanByDateException;
import com.mxi.mx.core.services.stask.details.DetailsService;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Integration unit test for {@linkplain DetailsService#setPlanByDate}
 *
 */
public class SetPlanByDateTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedEx = ExpectedException.none();

   private static SchedStaskDao schedStaskDao;

   // Times in the DB have a granularity of seconds.
   private static final Date NOW = new Date();
   private static final Date FIVE_DAYS_FROM_NOW = floorSecond( addDays( NOW, 5 ) );
   private static final Date TEN_DAYS_FROM_NOW = floorSecond( addDays( NOW, 10 ) );
   private static final Date FIVE_DAYS_AGO = floorSecond( addDays( NOW, -5 ) );


   @Before
   public void before() {
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
   }


   @Test
   public void successfulWhenDueInFutureAndPlannedInFutureButBeforeDueDate() throws Exception {

      // Given a requirement with a due date in the future.
      Date dueDate = TEN_DAYS_FROM_NOW;
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( dueDate );
         } );
      } );

      // When the requirement's plan-by-date is set to a date in the future
      // but is still before the due date.
      Date planByDate = FIVE_DAYS_FROM_NOW;
      new DetailsService( requirement ).setPlanByDate( requirement, planByDate,
            createHumanResource() );

      // Then the plan-by-date is persisted.
      assertThat( "Plan by date does not match the date to which it was set.",
            schedStaskDao.findByPrimaryKey( requirement ).getPlanByDate(), is( planByDate ) );

   }


   @Test
   public void successfulWhenDueInFutureAndPlannedIsSameAsDue() throws Exception {

      // Given a requirement with a due date in the future.
      Date dueDate = TEN_DAYS_FROM_NOW;
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( dueDate );
         } );
      } );

      // When the requirement's plan-by-date is set to the due date.
      Date planByDate = dueDate;
      new DetailsService( requirement ).setPlanByDate( requirement, planByDate,
            createHumanResource() );

      // Then the plan-by-date is persisted.
      assertThat( "Plan by date does not match the date to which it was set.",
            schedStaskDao.findByPrimaryKey( requirement ).getPlanByDate(), is( planByDate ) );

   }


   @Test
   public void errorWhenDueInFutureAndPlannedInPast() throws Exception {

      // Given a requirement with a due date in the future.
      Date dueDate = TEN_DAYS_FROM_NOW;
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( dueDate );
         } );
      } );

      // Anticipating an exception to be thrown with error message core.err.31320
      expectedEx.expect( InvalidPlanByDateException.class );
      expectedEx.expectMessage( Matchers.containsString( "31320" ) );

      // When the requirement's plan-by-date is in the past.
      Date planByDate = FIVE_DAYS_AGO;
      new DetailsService( requirement ).setPlanByDate( requirement, planByDate,
            createHumanResource() );
   }


   @Test
   public void errorWhenDueInFutureAndPlannedAfterDue() throws Exception {

      // Given a requirement with a due date in the future.
      Date dueDate = TEN_DAYS_FROM_NOW;
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( dueDate );
         } );
      } );

      // Anticipating an exception to be thrown with error message core.err.31319
      expectedEx.expect( InvalidPlanByDateException.class );
      expectedEx.expectMessage( Matchers.containsString( "31319" ) );

      // When the requirement's plan-by-date is after the due date.
      Date planByDate = addDays( dueDate, 1 );
      new DetailsService( requirement ).setPlanByDate( requirement, planByDate,
            createHumanResource() );
   }

}
