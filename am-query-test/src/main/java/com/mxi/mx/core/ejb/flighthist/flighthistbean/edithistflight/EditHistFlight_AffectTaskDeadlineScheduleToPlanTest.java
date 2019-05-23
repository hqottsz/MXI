package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static com.mxi.mx.core.key.RefSchedFromKey.LASTDUE;
import static com.mxi.mx.core.key.RefSchedFromKey.LASTEND;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * The schedule-to-plan feature of recurring task deadlines affects the deadline start value of the
 * next task. The following are the behaviours of that feature:
 *
 * <pre>
 * When a task is completed OUTSIDE its schedule-to-plan window,
 * - the next task's deadline start value is based on LASTEND and
 * - is set to the completed task's COMPLETION usage value.
 * 
 * When a task is completed INSIDE its schedule-to-plan window,
 * - the next task's deadline start value is based on LASTDUE and
 * - is set to the completed task's DUE usage value.
 * </pre>
 *
 * When a flight, which occurred prior to the completed task, has its usage updated then the
 * completion usage of that task will change. That updated completion date may move from inside the
 * schedule-to-plan window to outside the window, or visa-verso. In either of these cases the
 * following task's deadline start values (and thus due value) may change (in accordance to the
 * schedule-to-plan feature behaviours listed above).
 *
 * This test class is intended to ensure these behaviours are observed when the completion date of a
 * task is updated due to a previous flight usage update.
 *
 */
public class EditHistFlight_AffectTaskDeadlineScheduleToPlanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;
   private TaskTaskKey iReqDefn;
   private HumanResourceKey iHrKey;

   private FlightHistBean iFlightHistBean;

   private static final BigDecimal SCHEDULE_TO_PLAN_LOW = new BigDecimal( 5 );
   private static final BigDecimal SCHEDULE_TO_PLAN_HIGH = new BigDecimal( 5 );
   private static final BigDecimal REQ_REPEAT_INTERVAL = new BigDecimal( 50 );

   private static final RefSchedFromKey NA_SCHED_FROM = null;
   private static final BigDecimal NA_START_USAGE = null;
   private static final Object NOT_COMPLETED = null;


   /**
    * Unit test for {@linkplain FlightHistBean#editHistFlight} that involve updating deadlines of
    * tasks and in particular the behaviours of the schedule-to-plan window of those deadlines.
    *
    * Verify that when the flight usage is edited causing the subsequent task's completion usage to
    * move from inside its schedule-to-plan window to outside, that the following active task's
    * deadline start is calculated from the "last-end" and uses the completed task's completion
    * usage (when it originally was calculated from the "last-due" and used the completed task's due
    * usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS     Start:    LASTDUE 100      Start:    LASTDUE 150
    *  Due:      100                  Due:      150              Due:      200
    *  Complete: 100                  Complete: 148 (inside)
    * 
    * Act:
    *  Flight is reduced by 10 HOURS (18 -> 8)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     8 HOURS*     Start:    LASTDUE 100      Start:    LASTEND* 138*
    *  Due:      100                  Due:      150              Due:      188*
    *  Complete: 100                  Complete: 138* (outside*)
    * </pre>
    *
    */
   @Test
   public void
         itAdjustsActiveTaskDeadlineWhenCompletionUsageMovesFromInsideToOutsideScheduleToPlanWindow()
               throws Exception {

      // *** Data for arrangement (referring to values in method comment).

      // Flight (pick any date in the distant past)
      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final BigDecimal lInitialFlightHoursDelta = new BigDecimal( 18 );
      final BigDecimal lInitialFlightHoursTsn = new BigDecimal( 118 );
      BigDecimal lFlightAdjustment = new BigDecimal( -10 );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = new BigDecimal( 100 );
      BigDecimal lReq1_CompletionUsage = new BigDecimal( 100 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = new BigDecimal( 148 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTDUE;
      BigDecimal lReq3_StartUsage = lReq2_DueUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      BigDecimal lReq2_ExpectedStartUsage = lReq2_StartUsage;
      BigDecimal lReq2_ExpectedDueUsage = lReq2_DueUsage;
      BigDecimal lReq2_ExpectedCompletionUsage = lReq2_CompletionUsage.add( lFlightAdjustment );

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTEND;
      BigDecimal lReq3_ExpectedStartUsage = lReq2_ExpectedCompletionUsage;
      BigDecimal lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_ExpectedCompletionUsage = null;

      // *** Data for assertion end.

      //
      // Arrangements
      //

      // Given an aircraft collecting HOURS usage
      // (so that flight usage may be collected for the aircraft).
      iAircraft = createAircraftInventory();

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( HOURS );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed HOURS
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 18 HOURS -> 8 HOURS (-10)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Then Req2 has the following changes (due to the flight usage being edited);
      // - the Completion HOURS is reduced by the change in flight HOURS delta (moved outside
      // window)
      // - other values are unchanged
      assertDeadline( lReq2, lReq2_ExpectedStartFrom, lReq2_ExpectedStartUsage,
            lReq2_ExpectedDueUsage, lReq2_ExpectedCompletionUsage );

      // Then Req3 has the following changes (as Req2's completion HOURS moved outside window);
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start HOURS changes to Req2's completion HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq3, lReq3_ExpectedStartFrom, lReq3_ExpectedStartUsage,
            lReq3_ExpectedDueUsage, lReq3_ExpectedCompletionUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage to
    * move from outside its schedule-to-plan window to outside, that the following active task's
    * deadline start is calculated from the "last-end" and uses the completed task's completion
    * usage (it originally was calculated from the "last-end" and used the completed task's complete
    * usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS     Start:    LASTDUE 100      Start:    LASTEND 143
    *  Due:      100                  Due:      150              Due:      193
    *  Complete: 100                  Complete: 143 (outside)
    * 
    * Act:
    *  Flight is reduced by 10 HOURS (18 -> 8)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     8 HOURS*     Start:    LASTDUE 100      Start:    LASTEND 133*
    *  Due:      100                  Due:      150              Due:      183*
    *  Complete: 100                  Complete: 133* (outside*)
    * </pre>
    *
    */
   @Test
   public void
         itAdjustsActiveTaskDeadlineWhenCompletionUsageMovesFromOutsideToOutsideScheduleToPlanWindow()
               throws Exception {

      // *** Data for arrangement (referring to values in method comment).

      // Flight (pick any date in the distant past)
      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final BigDecimal lInitialFlightHoursDelta = new BigDecimal( 18 );
      final BigDecimal lInitialFlightHoursTsn = new BigDecimal( 118 );
      BigDecimal lFlightAdjustment = new BigDecimal( -10 );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = new BigDecimal( 100 );
      BigDecimal lReq1_CompletionUsage = new BigDecimal( 100 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = new BigDecimal( 143 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      BigDecimal lReq2_ExpectedStartUsage = lReq2_StartUsage;
      BigDecimal lReq2_ExpectedDueUsage = lReq2_DueUsage;
      BigDecimal lReq2_ExpectedCompletionUsage = lReq2_CompletionUsage.add( lFlightAdjustment );

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = lReq3_StartFrom;
      BigDecimal lReq3_ExpectedStartUsage = lReq2_ExpectedCompletionUsage;
      BigDecimal lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_ExpectedCompletionUsage = null;

      // *** Data for assertion end.

      //
      // Arrangements
      //

      // Given an aircraft collecting HOURS usage
      // (so that flight usage may be collected for the aircraft).
      iAircraft = createAircraftInventory();

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( HOURS );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed HOURS
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 18 HOURS -> 8 HOURS (-10)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Then Req2 has the following changes (due to the flight usage being edited);
      // - the Completion HOURS is reduced by the change in flight HOURS delta (moved outside
      // window)
      // - other values are unchanged
      assertDeadline( lReq2, lReq2_ExpectedStartFrom, lReq2_ExpectedStartUsage,
            lReq2_ExpectedDueUsage, lReq2_ExpectedCompletionUsage );

      // Then Req3 has the following changes (as Req2's completion HOURS moved outside window);
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start HOURS changes to Req2's completion HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq3, lReq3_ExpectedStartFrom, lReq3_ExpectedStartUsage,
            lReq3_ExpectedDueUsage, lReq3_ExpectedCompletionUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage to
    * move from outside its schedule-to-plan window to inside, that the following active task's
    * deadline start is calculated from the "last-due" and uses the completed task's due usage (it
    * originally was calculated from the "last-end" and used the completed task's complete usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS     Start:    LASTDUE 100      Start:    LASTEND 143
    *  Due:      100                  Due:      150              Due:      193
    *  Complete: 100                  Complete: 143 (outside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     21 HOURS*    Start:    LASTDUE 100      Start:    LASTDUE* 150*
    *  Due:      100                  Due:      150              Due:      200*
    *  Complete: 100                  Complete: 146* (inside*)
    * </pre>
    *
    */
   @Test
   public void
         itAdjustsActiveTaskDeadlineWhenCompletionUsageMovesFromOutsideToInsideScheduleToPlanWindow()
               throws Exception {

      // *** Data for arrangement (referring to values in method comment).

      // Flight (pick any date in the distant past)
      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final BigDecimal lInitialFlightHoursDelta = new BigDecimal( 18 );
      final BigDecimal lInitialFlightHoursTsn = new BigDecimal( 118 );
      BigDecimal lFlightAdjustment = new BigDecimal( +3 );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = new BigDecimal( 100 );
      BigDecimal lReq1_CompletionUsage = new BigDecimal( 100 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = new BigDecimal( 143 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      BigDecimal lReq2_ExpectedStartUsage = lReq2_StartUsage;
      BigDecimal lReq2_ExpectedDueUsage = lReq2_DueUsage;
      BigDecimal lReq2_ExpectedCompletionUsage = lReq2_CompletionUsage.add( lFlightAdjustment );

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      BigDecimal lReq3_ExpectedStartUsage = lReq2_ExpectedDueUsage;
      BigDecimal lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_ExpectedCompletionUsage = null;

      // *** Data for assertion end.

      //
      // Arrangements
      //

      // Given an aircraft collecting HOURS usage
      // (so that flight usage may be collected for the aircraft).
      iAircraft = createAircraftInventory();

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( HOURS );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed HOURS
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 18 HOURS -> 21 HOURS (+3)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Then Req2 has the following changes (due to the flight usage being edited);
      // - the Completion HOURS is reduced by the change in flight HOURS delta (moved outside
      // window)
      // - other values are unchanged
      assertDeadline( lReq2, lReq2_ExpectedStartFrom, lReq2_ExpectedStartUsage,
            lReq2_ExpectedDueUsage, lReq2_ExpectedCompletionUsage );

      // Then Req3 has the following changes (as Req2's completion HOURS moved outside window);
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start HOURS changes to Req2's completion HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq3, lReq3_ExpectedStartFrom, lReq3_ExpectedStartUsage,
            lReq3_ExpectedDueUsage, lReq3_ExpectedCompletionUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage to
    * move from inside its schedule-to-plan window to inside, that the following active task's
    * deadline start is calculated from the "last-due" and uses the completed task's due usage (it
    * originally was calculated from the "last-due" and used the completed task's due usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS     Start:    LASTDUE 100      Start:    LASTDUE 150
    *  Due:      100                  Due:      150              Due:      200
    *  Complete: 100                  Complete: 148 (inside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight       T2 (complete)              T3 (active)
    *  Start:    n/a     21 HOURS*    Start:    LASTDUE 100      Start:    LASTDUE* 150*
    *  Due:      100                  Due:      150              Due:      200*
    *  Complete: 100                  Complete: 151* (inside*)
    * </pre>
    *
    */
   @Test
   public void
         itDoesNotAdjustActiveTaskDeadlineWhenCompletionUsageMovesFromInsideToInsideScheduleToPlanWindow()
               throws Exception {

      // *** Data for arrangement (referring to values in method comment).

      // Flight (pick any date in the distant past)
      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final BigDecimal lInitialFlightHoursDelta = new BigDecimal( 18 );
      final BigDecimal lInitialFlightHoursTsn = new BigDecimal( 118 );
      BigDecimal lFlightAdjustment = new BigDecimal( +3 );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = new BigDecimal( 100 );
      BigDecimal lReq1_CompletionUsage = new BigDecimal( 100 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = new BigDecimal( 148 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTDUE;
      BigDecimal lReq3_StartUsage = lReq2_DueUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      BigDecimal lReq2_ExpectedStartUsage = lReq2_StartUsage;
      BigDecimal lReq2_ExpectedDueUsage = lReq2_DueUsage;
      BigDecimal lReq2_ExpectedCompletionUsage = lReq2_CompletionUsage.add( lFlightAdjustment );

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      BigDecimal lReq3_ExpectedStartUsage = lReq3_StartUsage;
      BigDecimal lReq3_ExpectedDueUsage = lReq3_DueUsage;
      BigDecimal lReq3_ExpectedCompletionUsage = null;

      // *** Data for assertion end.

      //
      // Arrangements
      //

      // Given an aircraft collecting HOURS usage
      // (so that flight usage may be collected for the aircraft).
      iAircraft = createAircraftInventory();

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( HOURS );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed HOURS
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 18 HOURS -> 21 HOURS (+3)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Then Req2 has the following changes (due to the flight usage being edited);
      // - the Completion HOURS is reduced by the change in flight HOURS delta (moved outside
      // window)
      // - other values are unchanged
      assertDeadline( lReq2, lReq2_ExpectedStartFrom, lReq2_ExpectedStartUsage,
            lReq2_ExpectedDueUsage, lReq2_ExpectedCompletionUsage );

      // Then Req3 has the following changes (as Req2's completion HOURS moved outside window);
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start HOURS changes to Req2's completion HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq3, lReq3_ExpectedStartFrom, lReq3_ExpectedStartUsage,
            lReq3_ExpectedDueUsage, lReq3_ExpectedCompletionUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the next task's completion usage to move
    * from outside its schedule-to-plan window to inside and the subsequent task's completion usage
    * to move from inside to outside its schedule-to-plan window, that the following active task's
    * deadline start is calculated from the "last-end" and uses the completed task's complete usage
    * (it originally was calculated from the "last-due" and used the completed task's due usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight       T2 (complete)              T3 (complete)                  T4 (active)
    *  Start:    n/a     18 HOURS     Start:    LASTDUE 100      Start:    LASTEND 143          Start:    LASTDUE 193
    *  Due:      100                  Due:      150              Due:      193                  Due:      243
    *  Complete: 100                  Complete: 143 (outside)    Complete: 189 (inside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight       T2 (complete)              T3 (complete)                  T4 (active)
    *  Start:    n/a     21 HOURS*    Start:    LASTDUE 100      Start:    LASTDUE* 150*        Start:    LASTEND 192
    *  Due:      100                  Due:      150              Due:      200*                 Due:      242
    *  Complete: 100                  Complete: 146* (inside*)   Complete: 192* (outside*)
    * </pre>
    *
    */
   @Test
   public void
         itAdjustsCompletedTaskDeadlineWhenFirstCompletionMovesFromOutsideToInsideAndSecondMovesFromInsideToOutside()
               throws Exception {

      // *** Data for arrangement (referring to values in method comment).

      // Flight (pick any date in the distant past)
      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final BigDecimal lInitialFlightHoursDelta = new BigDecimal( 18 );
      final BigDecimal lInitialFlightHoursTsn = new BigDecimal( 118 );
      BigDecimal lFlightAdjustment = new BigDecimal( +3 );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = new BigDecimal( 100 );
      BigDecimal lReq1_CompletionUsage = new BigDecimal( 100 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = new BigDecimal( 143 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (completed after Req2)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_CompletionUsage = new BigDecimal( 189 );
      Date lReq3_CompletionDate = DateUtils.addDays( lReq2_CompletionDate, +10 );

      // Req4 (active - as indicated by no completion date)
      RefSchedFromKey lReq4_StartFrom = LASTDUE;
      BigDecimal lReq4_StartUsage = lReq3_DueUsage;
      BigDecimal lReq4_DueUsage = lReq4_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      BigDecimal lReq2_ExpectedStartUsage = lReq2_StartUsage;
      BigDecimal lReq2_ExpectedDueUsage = lReq2_DueUsage;
      BigDecimal lReq2_ExpectedCompletionUsage = lReq2_CompletionUsage.add( lFlightAdjustment );

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      BigDecimal lReq3_ExpectedStartUsage = lReq2_ExpectedDueUsage;
      BigDecimal lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_ExpectedCompletionUsage = lReq3_CompletionUsage.add( lFlightAdjustment );

      // Req4
      RefSchedFromKey lReq4_ExpectedStartFrom = LASTEND;
      BigDecimal lReq4_ExpectedStartUsage = lReq3_ExpectedCompletionUsage;
      BigDecimal lReq4_ExpectedDueUsage = lReq4_ExpectedStartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq4_ExpectedCompletionUsage = null;

      // *** Data for assertion end.

      //
      // Arrangements
      //

      // Given an aircraft collecting HOURS usage
      // (so that flight usage may be collected for the aircraft).
      iAircraft = createAircraftInventory();

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( HOURS );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed HOURS
      // outside the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring REQ completed after to the last completed REQ that has its completed
      // HOURS within the schedule to plan window.
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, lReq3_CompletionUsage, lReq3_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq3;
      TaskKey lReq4 = createRequirement( lPrevReq, lReq4_StartFrom, lReq4_StartUsage,
            lReq4_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 18 HOURS -> 21 HOURS (+3)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Then Req2 has the following changes (due to the flight usage being edited);
      // - the Completion HOURS is increased by the change in flight HOURS delta (moved inside
      // window)
      // - other values are unchanged
      assertDeadline( lReq2, lReq2_ExpectedStartFrom, lReq2_ExpectedStartUsage,
            lReq2_ExpectedDueUsage, lReq2_ExpectedCompletionUsage );

      // Then Req3 has the following changes (as Req2's completion HOURS moved inside window);
      // - the Start From changes from LASTEND to LASTDUE
      // - the Start HOURS changes to Req2's due HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq3, lReq3_ExpectedStartFrom, lReq3_ExpectedStartUsage,
            lReq3_ExpectedDueUsage, lReq3_ExpectedCompletionUsage );

      // Then Req4 has the following changes (as Req3's completion HOURS moved outside window);
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start HOURS changes to Req2's completion HOURS
      // - the Due HOURS changes to the Start HOURS + interval
      assertDeadline( lReq4, lReq4_ExpectedStartFrom, lReq4_ExpectedStartUsage,
            lReq4_ExpectedDueUsage, lReq4_ExpectedCompletionUsage );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( "username" ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private InventoryKey createAircraftInventory() {

      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, new BigDecimal( 1000 ) );
         }
      } );
      return lAircraft;
   }


   private FlightLegId createFlight( final Date aFlightDate, final BigDecimal aFlightHoursDelta,
         final BigDecimal aFlightHoursTsn ) {

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( iAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( aFlightDate );
            aFlight.addUsage( iAircraft, HOURS, aFlightHoursDelta, aFlightHoursTsn );

         }
      } );
      return lFlight;
   }


   private TaskKey createRequirement( final TaskKey aPrevReq, final RefSchedFromKey aSchedFrom,
         final BigDecimal aStartUsage, final BigDecimal aDueUsage,
         final BigDecimal aCompletionUsage, final Date aCompletionDate ) {

      TaskKey lRequirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         // Default to ACTV, unless a completion date is provided.
         RefEventStatusKey lStatus = ( aCompletionDate == null ) ? ACTV : COMPLETE;


         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iReqDefn );
            aReq.setPreviousTask( aPrevReq );
            aReq.setInventory( iAircraft );
            aReq.setStatus( lStatus );
            aReq.setActualEndDate( aCompletionDate );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( HOURS );
                  aDeadline.setScheduledFrom( aSchedFrom );
                  aDeadline.setStartTsn( aStartUsage );
                  aDeadline.setDueValue( aDueUsage );
                  aDeadline.setInterval( REQ_REPEAT_INTERVAL );
                  aDeadline.setScheduleToPlanWindow( SCHEDULE_TO_PLAN_LOW, SCHEDULE_TO_PLAN_HIGH );
               }
            } );

            if ( COMPLETE.equals( lStatus ) ) {
               aReq.addUsage(
                     new UsageSnapshot( iAircraft, HOURS, aCompletionUsage.doubleValue() ) );
            }
         }
      } );

      return lRequirement;
   }


   private void assertDeadline( TaskKey aReq, RefSchedFromKey aExpectedStartFrom,
         BigDecimal aExpectedStartUsage, BigDecimal aExpectedDueUsage,
         BigDecimal aExpectedCompletionUsage ) {

      // Assert deadline.
      EvtSchedDeadTable lDeadline =
            EvtSchedDeadTable.findByPrimaryKey( new EventDeadlineKey( aReq.getEventKey(), HOURS ) );

      Assert.assertEquals( "Unexpected HOURS start-from for Deadline", aExpectedStartFrom,
            lDeadline.getScheduledFrom() );
      Assert.assertEquals( "Unexpected HOURS start value for Deadline.",
            DataTypeUtils.toDouble( aExpectedStartUsage ), lDeadline.getStartQt() );
      Assert.assertEquals( "Unexpected HOURS due value for Deadline.",
            DataTypeUtils.toDouble( aExpectedDueUsage ), lDeadline.getDeadlineQt() );

      // Assert completion.
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( aReq, iAircraft, HOURS );
      BigDecimal lActualCompletionUsage =
            ( lUsage != null ) ? new BigDecimal( lUsage.getTSN() ) : null;

      Assert.assertEquals( "Unexpected completion HOURS for Req2.", aExpectedCompletionUsage,
            lActualCompletionUsage );
   }


   private FlightInformationTO generateFlightInfoTO( String aName, Date aArrivalDate,
         BigDecimal aHours ) {

      LocationKey lDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      LocationKey lArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

      Date lDepartureDate =
            DateUtils.addHours( aArrivalDate, aHours.multiply( new BigDecimal( -1 ) ).intValue() );

      return new FlightInformationTO( aName, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, lDepartureDate, aArrivalDate, null, null,
            false, false );
   }


   private void editHistFlightUsage( FlightLegId aFlight, BigDecimal aUpdatedFlightHours )
         throws Exception {

      FlightInformationTO lUpdatedFlightTO =
            generateFlightInfoTO( "flightname", new Date(), aUpdatedFlightHours );

      CollectedUsageParm lUpdatedFlightUsage = new CollectedUsageParm(
            new UsageParmKey( iAircraft, HOURS ), aUpdatedFlightHours.doubleValue() );

      iFlightHistBean.editHistFlight( aFlight, iHrKey, lUpdatedFlightTO,
            new CollectedUsageParm[] { lUpdatedFlightUsage }, null );
   }

}
