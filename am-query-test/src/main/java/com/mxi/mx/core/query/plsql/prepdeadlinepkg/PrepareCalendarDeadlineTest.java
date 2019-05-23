package com.mxi.mx.core.query.plsql.prepdeadlinepkg;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefSchedFromKey.EFFECTIV;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.SUPRSEDE;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OneTimeSchedulingRuleBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedureDelegate;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;


/**
 * Test case for the plsql package procedure prep_deadline_pkg.PrepareCalendarDeadline
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PrepareCalendarDeadlineTest {

   private static final boolean SYNC_WITH_BASELINE = true;
   private static final boolean DO_NOT_SYNC_WITH_BASELINE = false;

   private DeadlineProcedureDelegate iProc;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    *
    *        Given the following scenario:
    *        - 1st superseded revision of a task defn
    *        --- scheduled from an effective date
    *        --- with calendar based scheduling rule
    *        - 2nd active revision of the task defn
    *        --- with modified effective date
    *        --- same calendar based scheduling rule
    *        - task initialized against the 2nd active revision of the defn, BUT...
    *        --- with effective date of the 1st revision of the task defn (*)
    *        --- with prevent-deadline-sync enabled (allow manual scheduling)
    *        -> test prepareCalendarDeadline() to update the task's deadline
    *
    *        (*) This simulates the scenario where the task had manual scheduling enabled prior to it
    *            being sync'd to the second defn revision. Thus, the effective date was not updated.
    *            After which the task had manual scheduling disabled (reset to deadline baseline).
    *            Then the call to  prepareCalendarDeadline() is called.
    *
    *        Test calling prepareCalendarDeadline() with "sync with baseline" set to true, expecting the
    *        deadline's start date to be updated to the effective date of the revised task defn.
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskDefnEffDateRevisedAndTaskPreviouslyManuallyScheduled() throws Exception {

      // Create test effective dates (calendar based scheduling uses EOD)
      Calendar lCal = new GregorianCalendar();
      lCal.set( 2015, 2, 17 );

      Date lEffectiveDate = DateUtils.ceilDay( lCal.getTime() );
      Date lNewEffectiveDate = DateUtils.addDays( lEffectiveDate, 2 );

      // Create an task revision that is scheduled from an effective date.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CDY, BigDecimal.TEN );

      // Add a calendar based scheduling rule to the task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition with a new effective date.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lNewEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add the same calendar based scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the second task revision (emulating a task that was revised from
      // the first to the second task revision).
      // Note: by default "prevent baseline sync" is disabled, meaning it will allow baseline sync
      // to modify it.
      InventoryKey lInvKey = new InventoryBuilder().build();
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev2Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      // With a start date that equals the effective date of the task defn's first revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CDY )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( EFFECTIV )
            .withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );

      // Call prepareCalendarDeadline(), indicating the task is to be synced with the baseline.
      // As well, pass in the second task defn revision simulated the task has already been
      // sync'd to that second revision (just not its scheduling info - see method comment).
      iProc.prepareCalendarDeadline( lTaskKey, lTaskRev2Key, CDY, SYNC_WITH_BASELINE );

      // Verify the scheduled deadline was modified with the new task revision's effective date.
      lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lNewEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );
   }


   /**
    * <pre>
    *
    *      Given the following scenario:
    *      - task defn revision
    *      -- scheduled from an effective date
    *      -- with calendar based scheduling rule
    *      - task initialized against task defn revision with deadline
    *      - new task defn revision with modified effective date
    *      - task updated to new task defn revision
    *      -> test prepareCalendarDeadline() to update the task's deadline
    *
    *      Test calling prepareCalendarDeadline() with "sync with baseline" set to true, expecting the
    *      deadline's start date to be updated to the effective date of the revised task defn.
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskDefnEffectiveDateRevisedAndRequestedToSyncWithBaseline()
         throws Exception {

      // Create test effective dates (calendar based scheduling uses EOD)
      Calendar lCal = new GregorianCalendar();
      lCal.set( 2015, 2, 17 );

      Date lEffectiveDate = DateUtils.ceilDay( lCal.getTime() );
      Date lNewEffectiveDate = DateUtils.addDays( lEffectiveDate, 2 );

      // Create an task revision that is schedulded from an effective date.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CDY, BigDecimal.TEN );
      // Add a scheduling rule to the first task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition with a revised effective date.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lNewEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add the same scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the second task revision (emulating a task that was revised from
      // the first to the second task revision).
      InventoryKey lInvKey = new InventoryBuilder().build();
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev2Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CDY )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( EFFECTIV )
            .withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );

      // Call prepareCalendarDeadline(), indicateding the task is to be synced with the baseline.
      iProc.prepareCalendarDeadline( lTaskKey, lTaskRev1Key, CDY, SYNC_WITH_BASELINE );

      // Verify the scheduled deadline was modified with the new task revision's effective date.
      lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lNewEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );
   }


   /**
    * <pre>
    *
    *      Given the following scenario:
    *      - task defn revision
    *      -- scheduled from an effective date
    *      -- with calendar based scheduling rule
    *      - task initialized against task defn revision with deadline
    *      - new task defn revision with modified effective date
    *      - task updated to new task defn revision
    *      -> test prepareCalendarDeadline() to update the task's deadline
    *
    *      Test calling prepareCalendarDeadline() with "sync with baseline" set to false, expecting the
    *      deadline's start date to not be updated. Instead the start date will remain being the
    *      effective date of the task's original task defn revision.
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskDefnEffectiveDateRevisedButRequestedNotToSyncWithBaseline()
         throws Exception {

      // Create test effective dates (calendar based scheduling uses EOD)
      Calendar lCal = new GregorianCalendar();
      lCal.set( 2015, 2, 17 );

      Date lEffectiveDate = DateUtils.ceilDay( lCal.getTime() );
      Date lNewEffectiveDate = DateUtils.addDays( lEffectiveDate, 2 );

      // Create an task revision that is schedulded from an effective date.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CDY, BigDecimal.TEN );
      // Add a scheduling rule to the first task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition with a revised effective date.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lNewEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add the same scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the second task revision (emulating a task that was revised from
      // the first to the second task revision).
      InventoryKey lInvKey = new InventoryBuilder().build();
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev2Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CDY )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( EFFECTIV )
            .withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );

      // Call prepareCalendarDeadline(), indicateding the task is NOT to be synced with the
      // baseline.
      iProc.prepareCalendarDeadline( lTaskKey, lTaskRev1Key, CDY, DO_NOT_SYNC_WITH_BASELINE );

      // Verify the scheduled deadline was NOT modified.
      lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );
   }


   /**
    *
    * Test Case 1: Calculate the deadline start date and deadline date for calendar data type when
    * scheduled from effective date and manufacturer date is before effective date.
    *
    * Preconditions:
    *
    * None, there is no data setup needed
    *
    * Action:
    *
    * Call the prepareCalendarDeadline procedure
    *
    * Expectation:
    *
    * The effective date is stored as start date and deadline date is also recalculated in the table
    * evt_sched_dead.
    *
    * @throws Exception
    */
   @Test
   public void testStartDateWhenScheduledFromEffectiveDateAndManufacturerDateBeforeEffectiveDate()
         throws Exception {

      Calendar lEffectiveDateCalendar = Calendar.getInstance();
      lEffectiveDateCalendar.set( 2016, 0, 1 );
      Date lEffectiveDate = new Date( lEffectiveDateCalendar.getTimeInMillis() );

      Calendar lManufacturerDateCalendar = Calendar.getInstance();
      lManufacturerDateCalendar.set( 2015, 11, 23 );
      Date lManufacturerDate = new Date( lManufacturerDateCalendar.getTimeInMillis() );

      Calendar lReceivedDateCalendar = Calendar.getInstance();
      lReceivedDateCalendar.set( 2016, 0, 6 );
      Date lReceivedDate = new Date( lReceivedDateCalendar.getTimeInMillis() );

      // prepare the expected date
      Calendar lExpectedStartDateCalendar = Calendar.getInstance();
      lExpectedStartDateCalendar.set( 2016, 0, 1, 23, 59, 59 );
      Date lExpectedStartDate = new Date( lExpectedStartDateCalendar.getTimeInMillis() );

      // prepare the expected deadline date
      Calendar lExpectedNewDeadlineDateCalendar = Calendar.getInstance();
      lExpectedNewDeadlineDateCalendar.set( 2016, 3, 10, 23, 59, 59 );
      Date lExpectedNewDeadlineDate =
            new Date( lExpectedNewDeadlineDateCalendar.getTimeInMillis() );

      // Create a requirement that is scheduled from an effective date.
      TaskRevisionBuilder lReqBuilder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 1 );
      TaskTaskKey lReqKey = lReqBuilder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CDY, new BigDecimal( 100 ) );
      // Add a scheduling rule to the requirement.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lReqKey );

      InventoryKey lInvKey = new InventoryBuilder().manufacturedOn( lManufacturerDate )
            .receivedOn( lReceivedDate ).build();

      // Create a actual task against the requirement
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lReqKey )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CDY ).withInterval( 100 )
            .scheduledFrom( EFFECTIV ).withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );

      // Call the PLSQL procedure to execute
      iProc.prepareCalendarDeadline( lTaskKey, lReqKey, CDY, DO_NOT_SYNC_WITH_BASELINE );

      lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );

      // Verify the value of the scheduled deadline's scheduled from code.
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );

      // Verify the value of the scheduled deadline's start date.
      lEvtSchedDead.assertStartDt( lExpectedStartDate );

      // Verify the value of the scheduled deadline date.
      lEvtSchedDead.assertSchedDeadDt( lExpectedNewDeadlineDate );

   }


   /**
    *
    * Test Case 2: Calculate the deadline start date and deadline date for calendar data type when
    * scheduled from effective date and manufacturer date is after effective date.
    *
    * Preconditions:
    *
    * None, there is no data setup needed
    *
    * Action:
    *
    * Call the prepareCalendarDeadline procedure
    *
    * Expectation:
    *
    * The manufacturer date is stored as start date and deadline date is also recalculated in the
    * table evt_sched_dead.
    *
    * @throws Exception
    */
   @Test
   public void testStartDateWhenScheduledFromEffectiveDateAndManufacturerDateAfterEffectiveDate()
         throws Exception {

      Calendar lEffectiveDateCalendar = Calendar.getInstance();
      lEffectiveDateCalendar.set( 2016, 0, 1 );
      Date lEffectiveDate = new Date( lEffectiveDateCalendar.getTimeInMillis() );

      Calendar lManufacturerDateCalendar = Calendar.getInstance();
      lManufacturerDateCalendar.set( 2016, 0, 4 );
      Date lManufacturerDate = new Date( lManufacturerDateCalendar.getTimeInMillis() );

      Calendar lReceivedDateCalendar = Calendar.getInstance();
      lReceivedDateCalendar.set( 2016, 0, 6 );
      Date lReceivedDate = new Date( lReceivedDateCalendar.getTimeInMillis() );

      // prepare the expected start date
      Calendar lExpectedStartDateCalendar = Calendar.getInstance();
      lExpectedStartDateCalendar.set( 2016, 0, 4, 23, 59, 59 );
      Date lExpectedStartDate = new Date( lExpectedStartDateCalendar.getTimeInMillis() );

      // prepare the expected deadline date
      Calendar lExpectedNewDeadlineDateCalendar = Calendar.getInstance();
      lExpectedNewDeadlineDateCalendar.set( 2016, 3, 13, 23, 59, 59 );
      Date lExpectedNewDeadlineDate =
            new Date( lExpectedNewDeadlineDateCalendar.getTimeInMillis() );

      // Create a requirement that is scheduled from an effective date.
      TaskRevisionBuilder lReqBuilder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 1 );
      TaskTaskKey lReqKey = lReqBuilder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CDY, new BigDecimal( 100 ) );
      // Add a scheduling rule to the requirement.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lReqKey );

      InventoryKey lInvKey = new InventoryBuilder().manufacturedOn( lManufacturerDate )
            .receivedOn( lReceivedDate ).build();
      // Create a actual task against the requirement
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lReqKey )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the requirement.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CDY ).withInterval( 100 )
            .scheduledFrom( EFFECTIV ).withStartDate( lManufacturerDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );
      lEvtSchedDead.assertStartDt( lManufacturerDate );

      // Call the PLSQL procedure to execute
      iProc.prepareCalendarDeadline( lTaskKey, lReqKey, CDY, DO_NOT_SYNC_WITH_BASELINE );

      lEvtSchedDead = new EvtSchedDead( lTaskKey, CDY );

      // Verify the value of the scheduled deadline's start date.
      lEvtSchedDead.assertStartDt( lExpectedStartDate );

      // Verify the value of the scheduled deadline date.
      lEvtSchedDead.assertSchedDeadDt( lExpectedNewDeadlineDate );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iProc = new DeadlineProcedureDelegate();
   }
}
