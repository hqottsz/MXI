package com.mxi.mx.core.services.stask.deadline.deadlineservice;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OneTimeSchedulingRuleBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.deadline.DeadlineService;
import com.mxi.mx.core.services.stask.deadline.TaskReschedulingException;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 * Integration tests for {@linkplain DeadlineService#rescheduleTaskBasedOnDefinitionsScheduleFrom}
 *
 * <pre>
 *       Some notes about the Reschedule Task feature:
 *        - the feature is not available for tasks with previous tasks.
 *        - due to the fact that there is no previous tasks,
 *          there can be no measurement scheduling rules
 * </pre>
 */
@RunWith( Enclosed.class )
public final class RescheduleTaskBasedOnDefinitionsScheduleFromTest {

   private static final Double ONE = BigDecimal.ONE.doubleValue();
   private static final Double TEN = BigDecimal.TEN.doubleValue();

   private static final Date MARCH_10_2000;
   private static final Date MARCH_17_2000;
   private static final Date MARCH_20_2000;
   private static final Date MARCH_30_2000;

   private static final Date MARCH_17_2000_EOD;
   private static final Date MARCH_20_2000_EOD;
   private static final Date EPOCH;

   private static final Double USAGE_ON_MARCH_10_2000;
   private static final Double USAGE_ON_MARCH_17_2000;
   private static final Double USAGE_ON_MARCH_20_2000;
   private static final Double USAGE_ON_MARCH_30_2000;
   private static final Double CURRENT_USAGE;
   private static final Double USAGE_ON_EFFECTIVE_DATE;

   static {

      // Pick a date in the past to test with.
      Calendar lCal = Calendar.getInstance();
      lCal.set( 2000, 2, 17, 11, 00, 00 );

      // Used as the effective date.
      MARCH_17_2000 = lCal.getTime();
      MARCH_17_2000_EOD = DateUtils.ceilDay( MARCH_17_2000 );

      // Used as the custom date.
      MARCH_20_2000 = DateUtils.addDays( MARCH_17_2000, 3 );
      MARCH_20_2000_EOD = DateUtils.ceilDay( MARCH_20_2000 );

      // Used for adding usage (before and after the effective/custom dates).
      MARCH_10_2000 = DateUtils.addDays( MARCH_17_2000, -7 );
      MARCH_30_2000 = DateUtils.addDays( MARCH_20_2000, 10 );

      // Maintenance epoch for deadlines (1-JAN-1950 EOD)
      lCal.set( 1950, 0, 1, 23, 59, 59 );
      EPOCH = lCal.getTime();

      // Usage quantities.
      USAGE_ON_MARCH_10_2000 = 10.0d;
      USAGE_ON_MARCH_17_2000 = 17.0d;
      USAGE_ON_MARCH_20_2000 = 20.0d;
      USAGE_ON_MARCH_30_2000 = 30.0d;

      CURRENT_USAGE = USAGE_ON_MARCH_30_2000;
      USAGE_ON_EFFECTIVE_DATE = USAGE_ON_MARCH_17_2000;
   }


   /**
    * Integration tests for {@linkplain DeadlineService#RescheduleTaskTest} on tasks that are not
    * valid for rescheduling.
    */
   @RunWith( BlockJUnit4ClassRunner.class )
   public static final class RescheduleTaskTestForValidation {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();

      @Rule
      public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date that is
       *              based on a task definition that is scheduled from the received date.
       *              And given the task has a previous task.
       *              When the task is rescheduled the validation will fail due to the
       *              task having a previous task and a TaskReschedulingException is thrown.
       *
       *   - task defn
       *      - REQ
       *      - scheduled from an received date
       *      - with a scheduling rule
       *   - task
       *      - based on the task defn
       *      - with a deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *   - previous task exists
       *
       *   Expected results:
       *      - TaskReschedulingException is thrown
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithCustomStartBasedOnDefnSchedFromRecvHasAPreviousTask()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().receivedOn( MARCH_17_2000 ).build();

         // Create the REQ task definition revision scheduled from the received date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromRecievedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule = new OneTimeSchedulingRule();
         lOneTimeSchedulingRule.setUsageParameter( DataTypeKey.CDY );
         // Add a scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a previous task against the task defn rev.
         TaskKey lPrevTask = new TaskBuilder().build();

         // Initialize a task against the task defn rev that follows the previous task.
         TaskKey lTask =
               new TaskBuilder().withTaskRevision( lTaskRev ).withTaskClass( RefTaskClassKey.REQ )
                     .onInventory( lInv ).withPrevTask( lPrevTask ).build();

         // Add a deadline to the task based on the scheduling rule of the task defn
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );

         // Execute the test.
         try {
            new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();
         } catch ( TaskReschedulingException e ) {
            // Note, for some reason the @Test(expected = TaskReschedulingException) is not working.
            return;
         }

         fail( "Expected TaskReschedulingException" );

      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date that is
       *              based on a task definition that is scheduled from the manufactured date.
       *              And given the task has a previous task.
       *              When the task is rescheduled the validation will fail due to the
       *              task having a previous task and a TaskReschedulingException is thrown.
       *
       *   - task defn
       *      - REQ
       *      - scheduled from an manufactured date
       *      - with a scheduling rule
       *   - task
       *      - based on the task defn
       *      - with a deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *   - previous task exists
       *
       *   Expected results:
       *      - TaskReschedulingException is thrown
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithCustomStartBasedOnDefnSchedFromManHasAPreviousTask()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().manufacturedOn( MARCH_17_2000 ).build();

         // Create the REQ task definition revision scheduled from the received date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromRecievedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule = new OneTimeSchedulingRule();
         lOneTimeSchedulingRule.setUsageParameter( DataTypeKey.CDY );
         // Add a scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a previous task against the task defn rev.
         TaskKey lPrevTask = new TaskBuilder().build();

         // Initialize a task against the task defn rev that follows the previous task.
         TaskKey lTask =
               new TaskBuilder().withTaskRevision( lTaskRev ).withTaskClass( RefTaskClassKey.REQ )
                     .onInventory( lInv ).withPrevTask( lPrevTask ).build();

         // Add a deadline to the task based on the scheduling rule of the task defn
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );

         // Execute the test.
         try {
            new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();
         } catch ( TaskReschedulingException e ) {
            // Note, for some reason the @Test(expected = TaskReschedulingException) is not working.
            return;
         }

         fail( "Expected TaskReschedulingException" );

      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date that is
       *              based on a task definition that is scheduled from an effective date.
       *              And given the task has a previous task.
       *              When the task is rescheduled the validation will fail due to the
       *              task having a previous task and a TaskReschedulingException is thrown.
       *
       *   - task defn
       *      - REQ
       *      - scheduled from an effective date
       *      - with a scheduling rule
       *   - task
       *      - based on the task defn
       *      - with a deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *   - previous task exists
       *
       *   Expected results:
       *      - TaskReschedulingException is thrown
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithCustomStartBasedOnDefnSchedFromEffHasAPreviousTask()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REQ task definition revision scheduled from the received date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).isScheduledFromRecievedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule = new OneTimeSchedulingRule();
         lOneTimeSchedulingRule.setUsageParameter( DataTypeKey.CDY );
         // Add a scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a previous task against the task defn rev.
         TaskKey lPrevTask = new TaskBuilder().build();

         // Initialize a task against the task defn rev that follows the previous task.
         TaskKey lTask =
               new TaskBuilder().withTaskRevision( lTaskRev ).withTaskClass( RefTaskClassKey.REQ )
                     .onInventory( lInv ).withPrevTask( lPrevTask ).build();

         // Add a deadline to the task based on the scheduling rule of the task defn
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );

         // Execute the test.
         try {
            new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();
         } catch ( TaskReschedulingException e ) {
            // Note, for some reason the @Test(expected = TaskReschedulingException) is not working.
            return;
         }

         fail( "Expected TaskReschedulingException" );

      }

   }

   /**
    * Integration tests for {@linkplain DeadlineService#RescheduleTaskTest} on tasks that have
    * calendar based deadlines.
    */
   @RunWith( BlockJUnit4ClassRunner.class )
   public static final class RescheduleTaskTestWithCalendarBasedDeadline {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();

      @Rule
      public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


      /**
       * <pre>
       *
       *   Behaviour: Given a corrective task that is scheduled from a custom start date with
       *              a custom interval and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will remain having a scheduled-from code of CUSTOM
       *              but its start date will be changed to the Mx epoch.
       *              The task will continue to use its custom interval.
       *
       *   - task defn
       *      - CORR
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task continues to have a schedule-from code of CUSTOM
       *      - task start date modified to be Mx epoch (Jan 1, 1950)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the Mx epoch + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenCorrWithCaDeadlineHavingCustomStartDateAndInterval() throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REPL task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.CORR )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from a custom date but that
         // custom date is the Mx epoch. Verify the deadline interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( EPOCH );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on epoch plus the custom interval of the task.
         Date lExpectedDueDate = DateUtils.addDays( EPOCH, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task and a forecasted task that are both scheduled from a
       *              custom start date and are based on a recurring task definition that
       *              is scheduled from an effective date.
       *              When the REQ task is rescheduled, the forecast task is modified
       *              to be scheduled from last-due.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - recurring
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *   - forecast task
       *      - dependent on task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *
       *   Expected results:
       *      - forecast task is modified to be schedule from LASTDUE
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenForecastTaskWithCaDeadlineHavingCustomStartDate() throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isRecurring().isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Initialize a forecast task against the task defn rev.
         TaskKey lForecastTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv )
               .withStatus( RefEventStatusKey.FORECAST ).withPrevTask( lTask ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lForecastTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).build();

         // Prior to executing the test, ensure the forecast task is scheduled using the custom
         // deadline info.
         EvtSchedDead lForecastDeadline =
               new EvtSchedDead( lForecastTask.getEventKey(), DataTypeKey.CDY );
         lForecastDeadline.assertExist();
         lForecastDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the forecast task is updated to be scheduled from last due.
         lForecastDeadline = new EvtSchedDead( lForecastTask.getEventKey(), DataTypeKey.CDY );
         lForecastDeadline.assertExist();
         lForecastDeadline.assertSchedFromCd( RefSchedFromKey.LASTDUE.getCd() );
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a replacement task that is scheduled from a custom start date with
       *              a custom interval and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will remain having a scheduled-from code of CUSTOM
       *              but its start date will be changed to the Mx epoch.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REPL
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task continues to have a schedule-from code of CUSTOM
       *      - task start date modified to be Mx epoch (Jan 1, 1950)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the Mx epoch + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReplWithCaDeadlineHavingCustomStartDateAndInterval() throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REPL task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REPL )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from a custom date but that
         // custom date is the Mx epoch. Verify the deadline interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( EPOCH );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on epoch plus the custom interval of the task.
         Date lExpectedDueDate = DateUtils.addDays( EPOCH, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date and is based on a
       *              task definition that is scheduled from an effective date.
       *              When the task is rescheduled it will be reset to use the effective start date
       *              of its task definition.
       *              The task will continue to use the interval matching the task definition.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev (end-of-day)
       *      - task deadline's due date recalculated using the
       *        task defn rev's effective date + the task's interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithCaDeadlineHavingCustomStartDate() throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( TEN );
         lDeadline.assertNotifyQt( TEN );
         lDeadline.assertDeviationQt( TEN );
         lDeadline.assertPrefixedQt( TEN );
         lDeadline.assertPostfixedQt( TEN );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev (adjsuted to the end-of-the-day).
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000_EOD );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( TEN );
         lDeadline.assertNotifyQt( TEN );
         lDeadline.assertDeviationQt( TEN );
         lDeadline.assertPrefixedQt( TEN );
         lDeadline.assertPostfixedQt( TEN );

         // Verify the deadline has a due date based on the effective date of
         // the task defn rev plus the interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, TEN.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a fault's correctional task that is scheduled from a custom start date
       *              with a custom interval and is based on a task definition that is scheduled
       *              from an effective date.
       *              When the task is rescheduled it will remain scheduled from the custom start
       *              date and remain using the custom interval.
       *
       *   Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *         as correctional task definition cannot be "scheduled from" a date. Their tasks are
       *         always scheduled from a CUSTOM date.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - using a custom interval
       *
       *   - fault
       *      - associated to the task (task is correction task of fault)
       *      - found on date different then the task's custom start date
       *
       *
       *   Expected results:
       *      - task remains scheduled from the custom start date
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date remains the custom start date + the custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithCaDeadlineHavingCustomStartDateAndIsCorrTaskOfFault()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Create a fault and associate the task as a corrective task
         // (Mx requires the task to have a custom deadline with a start date).
         FaultKey lFault = new SdFaultBuilder().build();
         new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lTask )
               .withType( RefRelationTypeKey.CORRECT ).build();

         // Ensure the fault has a found on date that differs from the task's custom start date.
         Date lFaultFoundOnDate = MARCH_30_2000;
         EvtEventTable lFaultEvent = EvtEventTable.findByPrimaryKey( lFault );
         lFaultEvent.setActualStartDate( lFaultFoundOnDate );
         lFaultEvent.update();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from the custom date and the deadline
         // interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000_EOD );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on epoch plus the custom interval of the task.
         Date lExpectedDueDate = DateUtils.addDays( MARCH_20_2000_EOD, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date, has a custom
       *              interval, and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will be reset to have the effective start
       *              date of its task definition.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev (end-of-day)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the task defn rev's
       *        effective date + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromEffReqWithCaDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev (adjsuted to the end-of-the-day).
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000_EOD );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date,
       *              has a custom interval, and is based on a task definition that is scheduled
       *              from a manufactured date.
       *              When the task is rescheduled it will be reset to be scheduled from birth,
       *              which is the manufactured date of the task's inventory.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from the manufactured date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from birth and the manufactured date of the
       *        task's inventory (end-of-day)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the
       *        task inventory's manufactured date + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromManReqWithCaDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().manufacturedOn( MARCH_17_2000 ).build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromManufacturedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from birth and the
         // manufactured date of the task's inventory (adjsuted to the end-of-the-day).
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000_EOD );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date,
       *              has a custom interval, and is based on a task definition that is scheduled
       *              from a recieved date.
       *              When the task is rescheduled it will be reset to be scheduled from birth,
       *              which is the received date of the task's inventory.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from the received date
       *      - calendar based scheduling rule
       *         - all scheduling info set
       *   - task
       *      -based on the task defn
       *      -calendar based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from birth and the received date of the
       *        task's inventory (end-of-day)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the
       *        task inventory's received date + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromRcvReqWithCaDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         InventoryKey lInv = new InventoryBuilder().receivedOn( MARCH_17_2000 ).build();

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromRecievedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from birth and the
         // received date of the task's inventory (adjsuted to the end-of-the-day).
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000_EOD );
         lDeadline.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, ONE.intValue() );
         lDeadline.assertSchedDeadDt( lExpectedDueDate );
         lDeadline.assertSchedDeadQt( null ); // no due quantity for calendar based deadlines
      }

   }

   /**
    * Integration tests for {@linkplain DeadlineService#RescheduleTaskTest} on tasks that have usage
    * based deadlines.
    */
   @RunWith( BlockJUnit4ClassRunner.class )
   public static final class RescheduleTaskTestWithUsageBasedDeadline {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();

      @Rule
      public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


      /**
       * <pre>
       *
       *   Behaviour: Given a corrective task that is scheduled from a custom start date with
       *              a custom interval and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will remain having a scheduled-from code of CUSTOM
       *              but its start date will be set to NULL and thus its start value set to 0 (due to it
       *              being a corrective task).
       *              The task will continue to use the custom interval.
       *
       *   Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *         as correctional task definitions cannot be "scheduled from" a date. Their tasks are
       *         always scheduled from a CUSTOM date.
       *
       *   - task defn
       *      - CORR
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task continues to have a schedule-from code of CUSTOM
       *      - task start date modified to be null and the start value set to 0
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date is simply the custom interval (due to 0 start value)
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenCorrWithUsDeadlineHavingCustomStartDateAndInterval() throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REPL task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.CORR )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from CUSTOM but because the task is a
         // correctional task, the start date is not set and the start quantity is set to 0. Verify
         // the deadline interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( null ); // For CORR tasks the start date is not set.
         lDeadline.assertStartQt( 0.0d ); // For CORR tasks the start quantity is set to 0.
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on epoch plus the custom interval of the
         // task.

         // Verify the deadline has a due quantity equal to custom interval (due to the start
         // quantity being 0).
         lDeadline.assertSchedDeadQt( ONE );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task and a forecasted task that are both scheduled from a
       *              custom start date and are based on a recurring task definition that
       *              is scheduled from an effective date.
       *              When the REQ task is rescheduled, the forecast task is modified
       *              to be scheduled from last-due.
       *
       *   Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *         as forecast task definitions cannot be scheduled from a custom date or quantity.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - recurring
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *         -all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *      -scheduled from a custom start date
       *   - forecast task
       *      - dependent on task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *      -scheduled from a custom start date
       *
       *   Expected results:
       *      - forecast task is modified to be schedule from LASTDUE
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenForecastTaskWithUsDeadlineHavingCustomStartDate() throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isRecurring().isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Initialize a forecast task against the task defn rev.
         TaskKey lForecastTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv )
               .withStatus( RefEventStatusKey.FORECAST ).withPrevTask( lTask ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lForecastTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).build();

         // Prior to executing the test, ensure the forecast task is scheduled using the custom
         // deadline info.
         EvtSchedDead lForecastDeadline =
               new EvtSchedDead( lForecastTask.getEventKey(), DataTypeKey.CYCLES );
         lForecastDeadline.assertExist();
         lForecastDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the forecast task is updated to be scheduled from last due.
         lForecastDeadline = new EvtSchedDead( lForecastTask.getEventKey(), DataTypeKey.CYCLES );
         lForecastDeadline.assertExist();
         lForecastDeadline.assertSchedFromCd( RefSchedFromKey.LASTDUE.getCd() );
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a replacement task that is scheduled from a custom start date with
       *              a custom interval and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will remain scheduled from a CUSTOM start date
       *              but that date will be the Mx epoch.
       *              The task will continue to use the custom interval.
       *
       *   Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *         as REPL task definitions cannot have scheduling rules.
       *
       *   - task defn
       *      - REPL
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *         - all scheduling info set
       *  - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task remains scheduled from CUSTOM but the start date is not set, thus the
       *        start quantity is set to 0
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date is simply the custom interval (due to 0 start quantity)
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReplWithUsDeadlineHavingCustomStartDateAndInterval() throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REPL task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REPL )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from CUSTOM but because the task is a
         // correctional task, the start date is not set and the start quantity is set to 0. Verify
         // the deadline interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( null ); // For CORR tasks the start date is not set.
         lDeadline.assertStartQt( 0.0d ); // For CORR tasks the start quantity is set to 0.
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on epoch plus the custom interval of the
         // task.

         // Verify the deadline has a due quantity equal to custom interval (due to the start
         // quantity being 0).
         lDeadline.assertSchedDeadQt( ONE );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date and is based on a
       *              task definition that is scheduled from an effective date.
       *              When the task is rescheduled it will be reset to have the effective start date
       *              of its task definition.
       *              The task will continue to use the interval matching the task definition.
       *
       *   Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *         as tasks with usage deadlines cannot have a custom start date.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *      -all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         -scheduled from a custom start date
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev
       *      - task start quantity set to the usage of the inventory on that effective date
       *      - task deadline's due quantity recalculated
       *        to equal the task's start quantity + the task's interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithUsDeadlineHavingCustomStartDate() throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_10_2000 )
               .withStartQuantity( TEN ).withInterval( TEN ).withNotifyQuantity( TEN )
               .withDeviation( TEN ).withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_10_2000 );
         lDeadline.assertStartQt( TEN );
         lDeadline.assertIntervalQt( TEN );
         lDeadline.assertNotifyQt( TEN );
         lDeadline.assertDeviationQt( TEN );
         lDeadline.assertPrefixedQt( TEN );
         lDeadline.assertPostfixedQt( TEN );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000 );
         lDeadline.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadline.assertIntervalQt( TEN );
         lDeadline.assertNotifyQt( TEN );
         lDeadline.assertDeviationQt( TEN );
         lDeadline.assertPrefixedQt( TEN );
         lDeadline.assertPostfixedQt( TEN );

         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the interval of the task.
         Double lExpectedDueQuantity = USAGE_ON_EFFECTIVE_DATE + TEN;
         lDeadline.assertSchedDeadQt( lExpectedDueQuantity );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *           Behaviour: Given a fault's correctional task that is scheduled from a custom start date
       *               with a custom interval and is based on a task definition that is scheduled
       *               from an effective date.
       *               When the task is rescheduled it will remain scheduled from the custom start
       *               date and remain using the custom interval.
       *
       *           Note: this behaviour is supported by the plsql procedure but is not supported by the GUI,
       *                 as faults may only have trouble shooting tasks that are CORR tasks (not REQs).
       *                 And even if a CORR task were associated to the fault and that task had a custom
       *                 start value added to a usage deadline, the Reschedule From button is not available
       *                 (because the GUI does not allow scheduling rules on CORR task defns).
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - using a custom interval
       *
       *   - fault
       *      - associated to the task (task is correction task of fault)
       *      - found on date different then the task's custom start date
       *
       *
       *   Expected results:
       *              - task remains scheduled from the custom start date and start value
       *              - task deadline's interval remains the custom interval
       *              - task deadline's due date remains the custom start value + the custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenReqWithUsDeadlineHavingCustomStartDateAndIsCorrTaskOfFault()
            throws Exception {

         // Create an inventory with a manufactured date and with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .manufacturedOn( MARCH_17_2000 ).withUsage( DataTypeKey.CYCLES, CURRENT_USAGE )
               .build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Create a fault and associate the task as a corrective task
         // (Mx requires the task to have a custom deadline with a start date).
         FaultKey lFault = new SdFaultBuilder().build();
         new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lTask )
               .withType( RefRelationTypeKey.CORRECT ).build();

         // Ensure the fault has a found on date that differs from the task's custom start date.
         Date lFaultFoundOnDate = MARCH_30_2000;
         EvtEventTable lFaultEvent = EvtEventTable.findByPrimaryKey( lFault );
         lFaultEvent.setActualStartDate( lFaultFoundOnDate );
         lFaultEvent.update();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task remains scheduled from the custom date and the deadline
         // interval remains the custom interval.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertStartQt( USAGE_ON_MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due quantity equal to the usage on the custom date of
         // the task plus the custom interval of the task.
         Double lExpectedDueQuantity = USAGE_ON_MARCH_20_2000 + ONE;
         lDeadline.assertSchedDeadQt( lExpectedDueQuantity );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date, has a custom
       *              interval, and is based on a task definition that is scheduled from an
       *              effective date.
       *              When the task is rescheduled it will be reset to have the effective start
       *              date of its task definition.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start quantity
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev
       *      - task start quantity set to the usage of the inventory on that effective date
       *      - task deadline's due quantity recalculated
       *        to equal the task's start quantity + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromEffReqWithUsDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000 );
         lDeadline.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedDueQuantity = USAGE_ON_EFFECTIVE_DATE + ONE;
         lDeadline.assertSchedDeadQt( lExpectedDueQuantity );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date, has a custom
       *              interval, and is based on a task definition with multiple usage based scheduling rule
       *              that is scheduled from an effective date.
       *              When the task is rescheduled it will be reset to have the effective start
       *              date of its task definition.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule for HOURS and CYCLES
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start quantity
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev
       *      - task start quantity set to the usage of the inventory on that effective date
       *      - task deadline's due quantity recalculated
       *        to equal the task's start quantity + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromEffReqWithUsMultipleDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE )
               .withUsage( DataTypeKey.HOURS, CURRENT_USAGE ).build();

         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );
         withUsage( lInv, MARCH_10_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRuleCycles =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCycles, lTaskRev );

         OneTimeSchedulingRule lOneTimeSchedulingRuleHours =
               new OneTimeSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanHigh( BigDecimal.TEN );
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleHours, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add deadline for hours and cycles to the task, with the same deadline information as the
         // scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.HOURS )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         // check for Cycles' deadline
         EvtSchedDead lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_20_2000 );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // check for Hours' deadline
         EvtSchedDead lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineHours.assertStartDt( MARCH_20_2000 );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.

         // check for Cycles' deadline
         lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_17_2000 );
         lDeadlineCycles.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedCyclesDueQuantity = USAGE_ON_EFFECTIVE_DATE + ONE;
         lDeadlineCycles.assertSchedDeadQt( lExpectedCyclesDueQuantity );
         lDeadlineCycles.assertSchedDeadDt( null ); // no due date for usage based deadlines

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.

         // check for Hours' deadline
         lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineHours.assertStartDt( MARCH_17_2000 );
         lDeadlineHours.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );
         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedHoursDueQuantity = USAGE_ON_EFFECTIVE_DATE + TEN;
         lDeadlineHours.assertSchedDeadQt( lExpectedHoursDueQuantity );
         lDeadlineHours.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date,
       *              has a custom interval, and is based on a task definition with multiple
       *              usage based scheduling rule that is scheduled  from a manufactured date.
       *              When the task is rescheduled it will be reset to be scheduled from birth,
       *              which is the manufactured date of the task's inventory.
       *              The deadline's start quantity will be 0 (due to it being scheduled from a
       *              manufactured date). The deadline will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from the manufactured date
       *      - usage based scheduling rule for HOURS and CYCLES
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from birth and the manufactured date of the
       *        task's inventory
       *      - task start quantity set to 0 (due to it being scheduled from manufactured date).
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using only the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromManReqWithUsMultipleDeadlinesHavingCustomStartDateAndInterval()
            throws Exception {

         // Create an inventory with a manufactured date and with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .manufacturedOn( MARCH_17_2000 ).withUsage( DataTypeKey.CYCLES, CURRENT_USAGE )
               .withUsage( DataTypeKey.HOURS, CURRENT_USAGE ).build();

         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );
         withUsage( lInv, MARCH_10_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an manufactured date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromManufacturedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRuleCycles =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCycles, lTaskRev );

         OneTimeSchedulingRule lOneTimeSchedulingRuleHours =
               new OneTimeSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanHigh( BigDecimal.TEN );
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleHours, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add deadlines for hours and cycles to the task, with the same deadline information as
         // the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadlines info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.HOURS )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadlines
         // info.
         // check for Cycles' deadline
         EvtSchedDead lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_20_2000 );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // check for Hours' deadline
         EvtSchedDead lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineHours.assertStartDt( MARCH_20_2000 );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from manufactured date.
         // However, for usage based deadlines that are scheduled from a manufactued date,
         // the start date is not set and the start quantity is set to zero.
         //
         // Refer to logic within prep_deadline_pkg.FindDeadlineStartQt().

         // check for Cycles' deadline
         lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadlineCycles.assertStartDt( null );
         lDeadlineCycles.assertStartQt( 0.0d );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // Since the start quantity is set to zero for usage based deadlines that are
         // scheduled from a manufactued date, the due quantity is the task's custom interval.
         lDeadlineCycles.assertSchedDeadQt( ONE );
         lDeadlineCycles.assertSchedDeadDt( null ); // no due date for usage based deadlines

         // check for Hours' deadline
         lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadlineHours.assertStartDt( null );
         lDeadlineHours.assertStartQt( 0.0d );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );
         // Since the start quantity is set to zero for usage based deadlines that are
         // scheduled from a manufactued date, the due quantity is the task's custom interval.
         lDeadlineHours.assertSchedDeadQt( TEN );
         lDeadlineHours.assertSchedDeadDt( null ); // no due date for usage based deadlines

      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date,
       *              has a custom interval, and is based on a task definition that is scheduled
       *              from a manufactured date.
       *              When the task is rescheduled it will be reset to be scheduled from birth,
       *              which is the manufactured date of the task's inventory.
       *              The deadline's start quantity will be 0 (due to it being scheduled from a
       *              manufactured date). The deadline will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from the manufactured date
       *      - usage based scheduling rule
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based deadline (matching scheduling rule of task defn)
       *         - scheduled from a custom start date
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from birth and the manufactured date of the
       *        task's inventory
       *      - task start quantity set to 0 (due to it being scheduled from manufactured date).
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using only the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromManReqWithUsDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         // Create an inventory with a manufactured date and with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .manufacturedOn( MARCH_17_2000 ).withUsage( DataTypeKey.CYCLES, CURRENT_USAGE )
               .build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an manufactured date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromManufacturedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from manufactured date.
         // However, for usage based deadlines that are scheduled from a manufactued date,
         // the start date is not set and the start quantity is set to zero.
         //
         // Refer to logic within prep_deadline_pkg.FindDeadlineStartQt().
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadline.assertStartDt( null );
         lDeadline.assertStartQt( 0.0d );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Since the start quantity is set to zero for usage based deadlines that are
         // scheduled from a manufactued date, the due quantity is the task's custom interval.
         lDeadline.assertSchedDeadQt( ONE );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date,
       *              has a custom interval, and is based on a task definition that is scheduled
       *              from a recieved date.
       *              When the task is rescheduled it will be reset to be scheduled from birth,
       *              which is the received date of the task's inventory.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      -REQ (not CORR and not REPL)
       *      -scheduled from the received date
       *      -calendar based scheduling rule
       *                    - all scheduling info set
       *   - task
       *      -based on the task defn
       *      -calendar based deadline (matching scheduling rule of task defn)
       *         -scheduled from a custom start date
       *         -with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from birth and the received date of the
       *        task's inventory (end-of-day)
       *      - task deadline's interval remains the custom interval
       *      - task deadline's due date recalculated using the
       *        task inventory's received date + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void testWhenSchedFromRcvReqWithUsDeadlineHavingCustomStartDateAndInterval()
            throws Exception {

         // Create an inventory with a manufactured date and with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .receivedOn( MARCH_17_2000 ).withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();
         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromRecievedDate().build();

         OneTimeSchedulingRule lOneTimeSchedulingRule =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRule.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRule.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev );
         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add a deadline to the task, with the same deadline information as the scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         EvtSchedDead lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadline.assertStartDt( MARCH_20_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from birth and the
         // received date of the task's inventory (adjsuted to the end-of-the-day).
         lDeadline = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadline.assertExist();
         lDeadline.assertSchedFromCd( RefSchedFromKey.BIRTH.getCd() );
         lDeadline.assertStartDt( MARCH_17_2000 );
         lDeadline.assertStartQt( USAGE_ON_MARCH_17_2000 );
         lDeadline.assertIntervalQt( ONE );
         lDeadline.assertNotifyQt( ONE );
         lDeadline.assertDeviationQt( ONE );
         lDeadline.assertPrefixedQt( ONE );
         lDeadline.assertPostfixedQt( ONE );

         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Double lExpectedDueQuantity = USAGE_ON_MARCH_17_2000 + ONE;
         lDeadline.assertSchedDeadQt( lExpectedDueQuantity );
         lDeadline.assertSchedDeadDt( null ); // no due date for usage based deadlines
      }


      /**
       * Creates a usage record.
       *
       * @param aInvKey
       *           inventory to which the usage is applied
       * @param aUsageDate
       *           date of the usage
       * @param aParameter
       *           usage parameter (data type)
       * @param aUsageValue
       *           usage value
       */
      private void withUsage( InventoryKey aInvKey, Date aUsageDate, DataTypeKey aParameter,
            Double aUsageValue ) {
         UUID lUsageRecId = new SequentialUuidGenerator().newUuid();
         DataSetArgument lUsageRecArgs = new DataSetArgument();
         lUsageRecArgs.add( "usage_record_id", lUsageRecId );
         lUsageRecArgs.add( "usage_dt", aUsageDate );
         lUsageRecArgs.add( "creation_dt", aUsageDate );
         MxDataAccess.getInstance().executeInsert( "usg_usage_record", lUsageRecArgs );

         UUID lUsageDataId = new SequentialUuidGenerator().newUuid();
         DataSetArgument lUsageDataArgs = new DataSetArgument();
         lUsageDataArgs.add( "usage_data_id", lUsageDataId );
         lUsageDataArgs.add( "usage_record_id", lUsageRecId );
         lUsageDataArgs.add( aInvKey, "inv_no_db_id", "inv_no_id" );
         lUsageDataArgs.add( aParameter, "data_type_db_id", "data_type_id" );
         lUsageDataArgs.add( "tsn_qt", aUsageValue );
         lUsageDataArgs.add( "negated_bool", 0 );
         MxDataAccess.getInstance().executeInsert( "usg_usage_data", lUsageDataArgs );
      }

   }

   /**
    * Integration tests for {@linkplain DeadlineService#RescheduleTaskTest} on tasks that have usage
    * based deadlines.
    */
   @RunWith( BlockJUnit4ClassRunner.class )
   public static class RescheduleTaskTestWithUsageAndCalendarBasedDeadline {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();

      @Rule
      public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date, has a custom
       *              interval, and is based on a task definition with one usage based and one calendar based scheduling rule
       *              that is scheduled from an effective date.
       *              When the task is rescheduled it will be reset to have the effective start
       *              date of its task definition.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule for CDY and CYCLES
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based and calendar based deadlines (matching scheduling rule of task defn)
       *         - scheduled from a custom start quantity
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev
       *      - task start quantity set to the usage of the inventory on that effective date
       *      - task deadline's due quantity recalculated
       *        to equal the task's start quantity + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void
            testWhenSchedFromEffReqWithUsSingleUsageAndCalendarDeadlineHavingCustomStartDateAndInterval()
                  throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE ).build();

         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         OneTimeSchedulingRule lOneTimeSchedulingRuleCycles =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCycles, lTaskRev );

         OneTimeSchedulingRule lOneTimeSchedulingRuleCdy =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setSchedToPlanHigh( BigDecimal.TEN );
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCdy, lTaskRev );

         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add deadline for calendar and cycles to the task, with the same deadline information as
         // the
         // scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         // check for Cycles' deadline
         EvtSchedDead lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_20_2000 );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );

         // check for Calendar's deadline
         EvtSchedDead lDeadlineCalendar = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadlineCalendar.assertExist();
         lDeadlineCalendar.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCalendar.assertStartDt( MARCH_20_2000 );
         lDeadlineCalendar.assertIntervalQt( ONE );
         lDeadlineCalendar.assertNotifyQt( ONE );
         lDeadlineCalendar.assertDeviationQt( ONE );
         lDeadlineCalendar.assertPrefixedQt( ONE );
         lDeadlineCalendar.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.

         // check for Cycles' deadline
         lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_17_2000 );
         lDeadlineCycles.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedCyclesDueQuantity = USAGE_ON_EFFECTIVE_DATE + ONE;
         lDeadlineCycles.assertSchedDeadQt( lExpectedCyclesDueQuantity );
         lDeadlineCycles.assertSchedDeadDt( null ); // no due date for usage based deadlines

         // check for Calendar's deadline
         lDeadlineCalendar = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadlineCalendar.assertExist();
         lDeadlineCalendar.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineCalendar.assertStartDt( MARCH_17_2000_EOD );
         lDeadlineCalendar.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadlineCalendar.assertIntervalQt( ONE );
         lDeadlineCalendar.assertNotifyQt( ONE );
         lDeadlineCalendar.assertDeviationQt( ONE );
         lDeadlineCalendar.assertPrefixedQt( ONE );
         lDeadlineCalendar.assertPostfixedQt( ONE );
         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, ONE.intValue() );
         lDeadlineCalendar.assertSchedDeadDt( lExpectedDueDate );
         lDeadlineCalendar.assertSchedDeadQt( null ); // no due quantity for calendar based
                                                      // deadlines
      }


      /**
       * <pre>
       *
       *   Behaviour: Given a REQ task that is scheduled from a custom start date, has a custom
       *              interval, and is based on a task definition with multiple usage based and
       *              one calendar based scheduling rule that is scheduled from an effective date.
       *              When the task is rescheduled it will be reset to have the effective start
       *              date of its task definition.
       *              The task will continue to use the custom interval.
       *
       *   - task defn
       *      - REQ (not CORR and not REPL)
       *      - scheduled from an effective date
       *      - usage based scheduling rule for CDY, HOURS and CYCLES
       *         - all scheduling info set
       *   - task
       *      - based on the task defn
       *      - usage based and calendar based deadlines (matching scheduling rule of task defn)
       *         - scheduled from a custom start quantity
       *         - with a custom interval (and other custom deadline info)
       *
       *   Expected results:
       *      - task reset to be scheduled from the effective date of the task defn rev
       *      - task start quantity set to the usage of the inventory on that effective date
       *      - task deadline's due quantity recalculated
       *        to equal the task's start quantity + the task's custom interval
       *
       * </pre>
       *
       * @throws Exception
       */
      @Test
      public void
            testWhenSchedFromEffReqWithUsMultipleUsageAndCalendarDeadlineHavingCustomStartDateAndInterval()
                  throws Exception {

         // Create an inventory with usage history (non-TRK).
         InventoryKey lInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY )
               .withUsage( DataTypeKey.CYCLES, CURRENT_USAGE )
               .withUsage( DataTypeKey.HOURS, CURRENT_USAGE ).build();

         withUsage( lInv, MARCH_10_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.CYCLES, USAGE_ON_MARCH_30_2000 );
         withUsage( lInv, MARCH_10_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_10_2000 );
         withUsage( lInv, MARCH_17_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_17_2000 );
         withUsage( lInv, MARCH_20_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_20_2000 );
         withUsage( lInv, MARCH_30_2000, DataTypeKey.HOURS, USAGE_ON_MARCH_30_2000 );

         // Create the REQ task definition revision scheduled from an effective date.
         TaskTaskKey lTaskRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
               .isScheduledFromEffectiveDate( MARCH_17_2000 ).build();

         // Add usage(Calendar, hours and cycles) based scheduling rule to the task defn rev.
         OneTimeSchedulingRule lOneTimeSchedulingRuleCycles =
               new OneTimeSchedulingRule( DataTypeKey.CYCLES, BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCycles.setSchedToPlanHigh( BigDecimal.TEN );
         // Add a calendar based scheduling rule to the task defn rev.
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCycles, lTaskRev );

         OneTimeSchedulingRule lOneTimeSchedulingRuleHours =
               new OneTimeSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleHours.setSchedToPlanHigh( BigDecimal.TEN );
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleHours, lTaskRev );

         OneTimeSchedulingRule lOneTimeSchedulingRuleCdy =
               new OneTimeSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setDeviation( BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setSchedToPlanLow( BigDecimal.TEN );
         lOneTimeSchedulingRuleCdy.setSchedToPlanHigh( BigDecimal.TEN );
         new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRuleCdy, lTaskRev );
         // Initialize a task against the task defn rev.
         TaskKey lTask = new TaskBuilder().withTaskRevision( lTaskRev )
               .withTaskClass( RefTaskClassKey.REQ ).onInventory( lInv ).build();

         // Add deadline for Calendar, hours and cycles to the task, with the same deadline
         // information as the
         // scheduling rule
         // BUT having a CUSTOM start date, a custom interval, and custom other deadline info.
         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CYCLES )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.HOURS )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( TEN ).withNotifyQuantity( TEN ).withDeviation( TEN )
               .withPrefixQuantity( TEN ).withPostfixQuantity( TEN ).build();

         new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY )
               .scheduledFrom( RefSchedFromKey.CUSTOM ).withStartDate( MARCH_20_2000 )
               .withInterval( ONE ).withNotifyQuantity( ONE ).withDeviation( ONE )
               .withPrefixQuantity( ONE ).withPostfixQuantity( ONE ).build();

         // Prior to executing the test, ensure the task is scheduled using the custom deadline
         // info.
         // check for Cycles' deadline
         EvtSchedDead lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_20_2000 );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // check for Hours' deadline
         EvtSchedDead lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineHours.assertStartDt( MARCH_20_2000 );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );
         // check for Calendar's deadline
         EvtSchedDead lDeadlineCalendar = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadlineCalendar.assertExist();
         lDeadlineCalendar.assertSchedFromCd( RefSchedFromKey.CUSTOM.getCd() );
         lDeadlineCalendar.assertStartDt( MARCH_20_2000 );
         lDeadlineCalendar.assertIntervalQt( ONE );
         lDeadlineCalendar.assertNotifyQt( ONE );
         lDeadlineCalendar.assertDeviationQt( ONE );
         lDeadlineCalendar.assertPrefixedQt( ONE );
         lDeadlineCalendar.assertPostfixedQt( ONE );

         // Execute the test.
         new DeadlineService( lTask ).rescheduleTaskBasedOnDefinitionsScheduleFrom();

         // Verify the deadline of the task has been reset to be scheduled from the effective date
         // of the task defn rev and has a start quantity equal to the usage on that effective date.

         // check for Cycles' deadline
         lDeadlineCycles = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CYCLES );
         lDeadlineCycles.assertExist();
         lDeadlineCycles.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineCycles.assertStartDt( MARCH_17_2000 );
         lDeadlineCycles.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadlineCycles.assertIntervalQt( ONE );
         lDeadlineCycles.assertNotifyQt( ONE );
         lDeadlineCycles.assertDeviationQt( ONE );
         lDeadlineCycles.assertPrefixedQt( ONE );
         lDeadlineCycles.assertPostfixedQt( ONE );
         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedCyclesDueQuantity = USAGE_ON_EFFECTIVE_DATE + ONE;
         lDeadlineCycles.assertSchedDeadQt( lExpectedCyclesDueQuantity );
         lDeadlineCycles.assertSchedDeadDt( null ); // no due date for usage based deadlines

         // check for Hours' deadline
         lDeadlineHours = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.HOURS );
         lDeadlineHours.assertExist();
         lDeadlineHours.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineHours.assertStartDt( MARCH_17_2000 );
         lDeadlineHours.assertStartQt( USAGE_ON_EFFECTIVE_DATE );
         lDeadlineHours.assertIntervalQt( TEN );
         lDeadlineHours.assertNotifyQt( TEN );
         lDeadlineHours.assertDeviationQt( TEN );
         lDeadlineHours.assertPrefixedQt( TEN );
         lDeadlineHours.assertPostfixedQt( TEN );
         // Verify the deadline has a due quantity equal to the usage on the effective date of
         // the task defn rev plus the custom interval of the task.
         Double lExpectedHoursDueQuantity = USAGE_ON_EFFECTIVE_DATE + TEN;
         lDeadlineHours.assertSchedDeadQt( lExpectedHoursDueQuantity );
         lDeadlineHours.assertSchedDeadDt( null ); // no due date for usage based deadlines

         // check for Calendar's deadline
         lDeadlineCalendar = new EvtSchedDead( lTask.getEventKey(), DataTypeKey.CDY );
         lDeadlineCalendar.assertExist();
         lDeadlineCalendar.assertSchedFromCd( RefSchedFromKey.EFFECTIV.getCd() );
         lDeadlineCalendar.assertStartDt( MARCH_17_2000_EOD );
         lDeadlineCalendar.assertStartQt( null ); // no start quantity for calendar based deadlines
         lDeadlineCalendar.assertIntervalQt( ONE );
         lDeadlineCalendar.assertNotifyQt( ONE );
         lDeadlineCalendar.assertDeviationQt( ONE );
         lDeadlineCalendar.assertPrefixedQt( ONE );
         lDeadlineCalendar.assertPostfixedQt( ONE );
         // Verify the deadline has a due date based on the effective date of the task defn rev plus
         // the custom interval of the task (adjsuted to the end-of-the-day).
         Date lExpectedDueDate = DateUtils.addDays( MARCH_17_2000_EOD, ONE.intValue() );
         lDeadlineCalendar.assertSchedDeadDt( lExpectedDueDate );
         lDeadlineCalendar.assertSchedDeadQt( null ); // no due quantity for calendar based
                                                      // deadlines
      }


      /**
       * Creates a usage record.
       *
       * @param aInvKey
       *           inventory to which the usage is applied
       * @param aUsageDate
       *           date of the usage
       * @param aParameter
       *           usage parameter (data type)
       * @param aUsageValue
       *           usage value
       */
      private void withUsage( InventoryKey aInvKey, Date aUsageDate, DataTypeKey aParameter,
            Double aUsageValue ) {
         UUID lUsageRecId = new SequentialUuidGenerator().newUuid();
         DataSetArgument lUsageRecArgs = new DataSetArgument();
         lUsageRecArgs.add( "usage_record_id", lUsageRecId );
         lUsageRecArgs.add( "usage_dt", aUsageDate );
         lUsageRecArgs.add( "creation_dt", aUsageDate );
         MxDataAccess.getInstance().executeInsert( "usg_usage_record", lUsageRecArgs );

         UUID lUsageDataId = new SequentialUuidGenerator().newUuid();
         DataSetArgument lUsageDataArgs = new DataSetArgument();
         lUsageDataArgs.add( "usage_data_id", lUsageDataId );
         lUsageDataArgs.add( "usage_record_id", lUsageRecId );
         lUsageDataArgs.add( aInvKey, "inv_no_db_id", "inv_no_id" );
         lUsageDataArgs.add( aParameter, "data_type_db_id", "data_type_id" );
         lUsageDataArgs.add( "tsn_qt", aUsageValue );
         lUsageDataArgs.add( "negated_bool", 0 );
         MxDataAccess.getInstance().executeInsert( "usg_usage_data", lUsageDataArgs );
      }

   }


   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

}
