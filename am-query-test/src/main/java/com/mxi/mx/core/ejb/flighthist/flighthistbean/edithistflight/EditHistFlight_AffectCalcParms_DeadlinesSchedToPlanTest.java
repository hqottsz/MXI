package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.am.domain.Domain.readUsageParameter;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static com.mxi.mx.core.key.RefSchedFromKey.LASTDUE;
import static com.mxi.mx.core.key.RefSchedFromKey.LASTEND;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
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
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
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
 * Unit test for {@linkplain FlightHistBean#editHistFlight} that involve updating calculated
 * parameter deadlines of tasks and in particular the behaviours of the schedule-to-plan window of
 * those deadlines.
 *
 * NOTE: this test class was originally based on
 * {@linkplain EditHistFlight_AffectTaskDeadlineScheduleToPlanTest} with the addition and use of a
 * calculated parameter.
 *
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
public class EditHistFlight_AffectCalcParms_DeadlinesSchedToPlanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;
   private TaskTaskKey iReqDefn;

   private FlightHistBean iFlightHistBean;

   private static final BigDecimal SCHEDULE_TO_PLAN_LOW = BigDecimal.valueOf( 5.0 );
   private static final BigDecimal SCHEDULE_TO_PLAN_HIGH = BigDecimal.valueOf( 5.0 );
   private static final BigDecimal REQ_REPEAT_INTERVAL = BigDecimal.valueOf( 50.0 );

   private static final RefSchedFromKey NA_SCHED_FROM = null;
   private static final BigDecimal NA_START_USAGE = null;
   private static final Object NOT_COMPLETED = null;

   // Calculated parameter data type, function, and constant.
   // The function is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String MULTIPLIER_FUNCTION_NAME = "MULTIPLIER_FUNCTION_NAME";
   private static final String MULTIPLIER_CONSTANT_NAME = "MULTIPLIER_CONSTANT_NAME";
   private static final BigDecimal MULTIPLIER_CONSTANT = BigDecimal.valueOf( 2.0 );
   private static final String CALC_PARM_CODE = "CALC_PARM_CODE";


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage of
    * a calculated parameter to move from inside its schedule-to-plan window to outside, that the
    * following active task's deadline start is calculated from the "last-end" and uses the
    * completed task's completion usage (when it originally was calculated from the "last-due" and
    * used the completed task's due usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 CALC_PARM -5/+5 (window)
    * 
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     9 HOURS        Start:    LASTDUE 100      Start:    LASTDUE 150
    *  Due:      100     (18 CALC_PARM)  Due:      150              Due:      200
    *  Complete: 100                     Complete: 148 (inside)
    * 
    * Act:
    *  Flight is reduced by 10 HOURS (9 -> 4)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     4 HOURS*        Start:    LASTDUE 100      Start:    LASTEND* 132*
    *  Due:      100     (8 CALC_PARM*)  Due:      150              Due:      188*
    *  Complete: 100                     Complete: 138* (outside*)
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
      final BigDecimal lInitialFlightHoursDelta = BigDecimal.valueOf( 9.0 );
      final BigDecimal lInitialFlightHoursTsn = BigDecimal.valueOf( 59.0 );
      BigDecimal lFlightAdjustment = BigDecimal.valueOf( -5 );
      BigDecimal lCalcParmAdjustment = lFlightAdjustment.multiply( MULTIPLIER_CONSTANT );

      // REQ usage values are for the CALC_PARM (not HOURS)

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = BigDecimal.valueOf( 100.0 );
      BigDecimal lReq1_CalcCompletionUsage = BigDecimal.valueOf( 100.0 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CalcCompletionUsage = BigDecimal.valueOf( 148.0 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTDUE;
      BigDecimal lReq3_StartUsage = lReq2_DueUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      Double lReq2_ExpectedStartUsage = lReq2_StartUsage.doubleValue();
      Double lReq2_ExpectedDueUsage = lReq2_DueUsage.doubleValue();
      Double lReq2_ExpectedCompletionUsage =
            lReq2_CalcCompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTEND;
      Double lReq3_ExpectedStartUsage = lReq2_ExpectedCompletionUsage;
      Double lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage + REQ_REPEAT_INTERVAL.doubleValue();

      // *** Data for assertion end.

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addCalcParmFunctionToDatabase();
      }

      //
      // Arrangements
      //

      //
      // Given an aircraft assembly tracking both HOURS and a calculated parameter based on HOURS.
      //
      AssemblyKey lAcftAssy = createAcftAssyTrackingParmsAndCalcParms();

      // Get the data type key for the calculated parameter.
      final DataTypeKey lCalcParm =
            readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given an aircraft based on that assembly collecting HOURS and CALC_PARM usage
      iAircraft = createAircraftInventory( lAcftAssy, HOURS, lCalcParm );

      // Given a recurring requirement definition with a scheduling rule for CALC_PARM that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( lCalcParm );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lCalcParm, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CalcCompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed CALC_PARM
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lCalcParm, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CalcCompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lCalcParm, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, ( BigDecimal ) NOT_COMPLETED, ( Date ) NOT_COMPLETED );

      //
      // Act
      //

      // When the flight is edited and the usage is modified; 9 HOURS -> 4 HOURS (-5)
      BigDecimal lNewFlightHours = lInitialFlightHoursDelta.add( lFlightAdjustment );
      editHistFlightUsage( lFlight, lNewFlightHours );

      //
      // Assertions
      //

      // Get Req2's deadline and usage at completion for CALC_PARM.
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lReq2.getEventKey(), lCalcParm );
      EvtSchedDeadTable lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( lReq2, iAircraft, lCalcParm );

      RefSchedFromKey lReq2_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq2_ActualStartUsage = lDeadline.getStartQt();
      Double lReq2_ActualDueUsage = lDeadline.getDeadlineQt();
      Double lReq2_ActualCompletionUsage = lUsage.getTSN();

      // Get Req3's deadline for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq3.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      RefSchedFromKey lReq3_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq3_ActualStartUsage = lDeadline.getStartQt();
      Double lReq3_ActualDueUsage = lDeadline.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropCalcParmFunctionToDatabase();
      }

      // Then for Req2 (completed REQ after flight) the sched-from, start value, and due value are
      // unchanged.
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req2.", lReq2_ExpectedStartFrom,
            lReq2_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req2.",
            lReq2_ExpectedStartUsage, lReq2_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req2.", lReq2_ExpectedDueUsage,
            lReq2_ActualDueUsage );

      // Then for Req2 (completed REQ after flight) the completion value changes due to it's
      // modified completion value moving from INSIDE to OUTSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req2.",
            lReq2_ExpectedCompletionUsage, lReq2_ActualCompletionUsage );

      // Then for Req3 (active REQ after Req2) the sched-from, start value, and due value are
      // changed due to Req2's completion CALC_PARM moving from INSIDE to OUTSIDE the window;
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start CALC_PARM changes to Req2's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req3.", lReq3_ExpectedStartFrom,
            lReq3_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req3.",
            lReq3_ExpectedStartUsage, lReq3_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req3.", lReq3_ExpectedDueUsage,
            lReq3_ActualDueUsage );

   }


   /**
    * Verify that when the flight usage is edited which causes the subsequent task's completion
    * usage of a calculated parameter to move from outside its schedule-to-plan window to outside,
    * that the following active task's deadline start is calculated from the "last-end" and uses the
    * completed task's completion usage (it originally was calculated from the "last-end" and used
    * the completed task's complete usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 CALC_PARM -5/+5 (window)
    * 
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS        Start:    LASTDUE 100      Start:    LASTEND 143
    *  Due:      100     (36 CALC_PARM)  Due:      150              Due:      193
    *  Complete: 100                     Complete: 143 (outside)
    * 
    * Act:
    *  Flight is reduced by 10 HOURS (18 -> 8)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     8 HOURS*        Start:    LASTDUE 100      Start:    LASTEND 127*
    *  Due:      100     (16 CALC_PARM*) Due:      150              Due:      177*
    *  Complete: 100                     Complete: 127* (outside*)
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
      final BigDecimal lInitialFlightHoursDelta = BigDecimal.valueOf( 18.0 );
      final BigDecimal lInitialFlightHoursTsn = BigDecimal.valueOf( 59.0 );
      BigDecimal lFlightAdjustment = BigDecimal.valueOf( -10.0 );
      BigDecimal lCalcParmAdjustment = lFlightAdjustment.multiply( MULTIPLIER_CONSTANT );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = BigDecimal.valueOf( 100.0 );
      BigDecimal lReq1_CompletionUsage = BigDecimal.valueOf( 100.0 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = BigDecimal.valueOf( 143.0 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      Double lReq2_ExpectedStartUsage = lReq2_StartUsage.doubleValue();
      Double lReq2_ExpectedDueUsage = lReq2_DueUsage.doubleValue();
      Double lReq2_ExpectedCompletionUsage =
            lReq2_CompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = lReq3_StartFrom;
      Double lReq3_ExpectedStartUsage = lReq2_ExpectedCompletionUsage;
      Double lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage + REQ_REPEAT_INTERVAL.doubleValue();

      // *** Data for assertion end.

      //
      // Arrangements
      //

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addCalcParmFunctionToDatabase();
      }

      //
      // Given an aircraft assembly tracking both HOURS and a calculated parameter based on HOURS.
      //
      AssemblyKey lAcftAssy = createAcftAssyTrackingParmsAndCalcParms();

      // Get the data type key for the calculated parameter.
      final DataTypeKey lCalcParm =
            readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given an aircraft based on that assembly collecting HOURS and CALC_PARM usage
      iAircraft = createAircraftInventory( lAcftAssy, HOURS, lCalcParm );

      // Given a recurring requirement definition with a scheduling rule for CALC_PARM that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( lCalcParm );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lCalcParm, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed CALC_PARM
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lCalcParm, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lCalcParm, lReq3_StartFrom, lReq3_StartUsage,
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

      // Get Req2's deadline and usage at completion for CALC_PARM.
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lReq2.getEventKey(), lCalcParm );
      EvtSchedDeadTable lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( lReq2, iAircraft, lCalcParm );

      RefSchedFromKey lReq2_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq2_ActualStartUsage = lDeadline.getStartQt().doubleValue();
      Double lReq2_ActualDueUsage = lDeadline.getDeadlineQt().doubleValue();
      Double lReq2_ActualCompletionUsage = lUsage.getTSN();

      // Get Req3's deadline for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq3.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      RefSchedFromKey lReq3_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq3_ActualStartUsage = lDeadline.getStartQt().doubleValue();
      Double lReq3_ActualDueUsage = lDeadline.getDeadlineQt().doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropCalcParmFunctionToDatabase();
      }

      // Then for Req2 (completed REQ after flight) the sched-from, start value, and due value are
      // unchanged.
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req2.", lReq2_ExpectedStartFrom,
            lReq2_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req2.",
            lReq2_ExpectedStartUsage, lReq2_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req2.", lReq2_ExpectedDueUsage,
            lReq2_ActualDueUsage );

      // Then for Req2 (completed REQ after flight) the completion value changes due to it's
      // modified completion value moving from INSIDE to OUTSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req2.",
            lReq2_ExpectedCompletionUsage, lReq2_ActualCompletionUsage );

      // Then for Req3 (active REQ after Req2) the sched-from, start value, and due value are
      // changed due to Req2's completion CALC_PARM moving from INSIDE to OUTSIDE the window;
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start CALC_PARM changes to Req2's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req3.", lReq3_ExpectedStartFrom,
            lReq3_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req3.",
            lReq3_ExpectedStartUsage, lReq3_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req3.", lReq3_ExpectedDueUsage,
            lReq3_ActualDueUsage );

   }


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage of
    * a calculated parameter to move from outside its schedule-to-plan window to inside, that the
    * following active task's deadline start is calculated from the "last-due" and uses the
    * completed task's due usage (it originally was calculated from the "last-end" and used the
    * completed task's complete usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS        Start:    LASTDUE 100      Start:    LASTEND 143
    *  Due:      100     (36 CALC_PARM)  Due:      150              Due:      193
    *  Complete: 100                     Complete: 143 (outside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     21 HOURS*       Start:    LASTDUE 100      Start:    LASTDUE* 150*
    *  Due:      100     (42 CALC_PARM)  Due:      150              Due:      200*
    *  Complete: 100                     Complete: 149* (inside*)
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
      final BigDecimal lInitialFlightHoursDelta = BigDecimal.valueOf( 18.0 );
      final BigDecimal lInitialFlightHoursTsn = BigDecimal.valueOf( 59.0 );
      BigDecimal lFlightAdjustment = BigDecimal.valueOf( +3 );
      BigDecimal lCalcParmAdjustment = lFlightAdjustment.multiply( MULTIPLIER_CONSTANT );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = BigDecimal.valueOf( 100.0 );
      BigDecimal lReq1_CompletionUsage = BigDecimal.valueOf( 100.0 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = BigDecimal.valueOf( 143.0 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      Double lReq2_ExpectedStartUsage = lReq2_StartUsage.doubleValue();
      Double lReq2_ExpectedDueUsage = lReq2_DueUsage.doubleValue();
      Double lReq2_ExpectedCompletionUsage =
            lReq2_CompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      Double lReq3_ExpectedStartUsage = lReq2_ExpectedDueUsage;
      Double lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage + REQ_REPEAT_INTERVAL.doubleValue();

      // *** Data for assertion end.

      //
      // Arrangements
      //

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addCalcParmFunctionToDatabase();
      }

      //
      // Given an aircraft assembly tracking both HOURS and a calculated parameter based on HOURS.
      //
      AssemblyKey lAcftAssy = createAcftAssyTrackingParmsAndCalcParms();

      // Get the data type key for the calculated parameter.
      final DataTypeKey lCalcParm =
            readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given an aircraft based on that assembly collecting HOURS and CALC_PARM usage
      iAircraft = createAircraftInventory( lAcftAssy, HOURS, lCalcParm );

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( lCalcParm );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lCalcParm, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed CALC_PARM
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lCalcParm, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lCalcParm, lReq3_StartFrom, lReq3_StartUsage,
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

      // Get Req2's deadline and usage at completion for CALC_PARM.
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lReq2.getEventKey(), lCalcParm );
      EvtSchedDeadTable lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( lReq2, iAircraft, lCalcParm );

      RefSchedFromKey lReq2_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq2_ActualStartUsage = lDeadline.getStartQt();
      Double lReq2_ActualDueUsage = lDeadline.getDeadlineQt();
      Double lReq2_ActualCompletionUsage = lUsage.getTSN();

      // Get Req3's deadline for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq3.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      RefSchedFromKey lReq3_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq3_ActualStartUsage = lDeadline.getStartQt();
      Double lReq3_ActualDueUsage = lDeadline.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropCalcParmFunctionToDatabase();
      }

      // Then for Req2 (completed REQ after flight) the sched-from, start value, and due value are
      // unchanged.
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req2.", lReq2_ExpectedStartFrom,
            lReq2_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req2.",
            lReq2_ExpectedStartUsage, lReq2_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req2.", lReq2_ExpectedDueUsage,
            lReq2_ActualDueUsage );

      // Then for Req2 (completed REQ after flight) the completion value changes due to it's
      // modified completion value moving from OUTSIDE to INSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req2.",
            lReq2_ExpectedCompletionUsage, lReq2_ActualCompletionUsage );

      // Then for Req3 (active REQ after Req2) the sched-from, start value, and due value are
      // changed due to Req2's completion CALC_PARM moving from OUTSIDE to INSIDE the window;
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start CALC_PARM changes to Req2's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req3.", lReq3_ExpectedStartFrom,
            lReq3_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req3.",
            lReq3_ExpectedStartUsage, lReq3_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req3.", lReq3_ExpectedDueUsage,
            lReq3_ActualDueUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the subsequent task's completion usage of
    * a calculated parameter to move from inside its schedule-to-plan window to inside, that the
    * following active task's deadline start is calculated from the "last-due" and uses the
    * completed task's due usage (it originally was calculated from the "last-due" and used the
    * completed task's due usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     18 HOURS        Start:    LASTDUE 100      Start:    LASTDUE 150
    *  Due:      100     (36 CALC_PARM)  Due:      150              Due:      200
    *  Complete: 100                     Complete: 148 (inside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight          T2 (complete)              T3 (active)
    *  Start:    n/a     21 HOURS*       Start:    LASTDUE 100      Start:    LASTDUE* 150*
    *  Due:      100     (42 CALC_PARM)  Due:      150              Due:      200*
    *  Complete: 100                     Complete: 154* (inside*)
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
      final BigDecimal lInitialFlightHoursDelta = BigDecimal.valueOf( 18.0 );
      final BigDecimal lInitialFlightHoursTsn = BigDecimal.valueOf( 59.0 );
      BigDecimal lFlightAdjustment = BigDecimal.valueOf( +3 );
      BigDecimal lCalcParmAdjustment = lFlightAdjustment.multiply( MULTIPLIER_CONSTANT );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = BigDecimal.valueOf( 100.0 );
      BigDecimal lReq1_CompletionUsage = BigDecimal.valueOf( 100.0 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = BigDecimal.valueOf( 148.0 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (active - as indicated by no completion date)
      RefSchedFromKey lReq3_StartFrom = LASTDUE;
      BigDecimal lReq3_StartUsage = lReq2_DueUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      Double lReq2_ExpectedStartUsage = lReq2_StartUsage.doubleValue();
      Double lReq2_ExpectedDueUsage = lReq2_DueUsage.doubleValue();
      Double lReq2_ExpectedCompletionUsage =
            lReq2_CompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      Double lReq3_ExpectedStartUsage = lReq3_StartUsage.doubleValue();
      Double lReq3_ExpectedDueUsage = lReq3_DueUsage.doubleValue();

      // *** Data for assertion end.

      //
      // Arrangements
      //

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addCalcParmFunctionToDatabase();
      }

      //
      // Given an aircraft assembly tracking both HOURS and a calculated parameter based on HOURS.
      //
      AssemblyKey lAcftAssy = createAcftAssyTrackingParmsAndCalcParms();

      // Get the data type key for the calculated parameter.
      final DataTypeKey lCalcParm =
            readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given an aircraft based on that assembly collecting HOURS and CALC_PARM usage
      iAircraft = createAircraftInventory( lAcftAssy, HOURS, lCalcParm );

      // Given a recurring requirement definition with a scheduling rule for CALC_PARM that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( lCalcParm );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lCalcParm, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed CALC_PARM
      // within the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lCalcParm, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lCalcParm, lReq3_StartFrom, lReq3_StartUsage,
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

      // Get Req2's deadline and usage at completion for CALC_PARM.
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lReq2.getEventKey(), lCalcParm );
      EvtSchedDeadTable lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( lReq2, iAircraft, lCalcParm );

      RefSchedFromKey lReq2_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq2_ActualStartUsage = lDeadline.getStartQt();
      Double lReq2_ActualDueUsage = lDeadline.getDeadlineQt();
      Double lReq2_ActualCompletionUsage = lUsage.getTSN();

      // Get Req3's deadline for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq3.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      RefSchedFromKey lReq3_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq3_ActualStartUsage = lDeadline.getStartQt();
      Double lReq3_ActualDueUsage = lDeadline.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropCalcParmFunctionToDatabase();
      }

      // Then for Req2 (completed REQ after flight) the sched-from, start value, and due value are
      // unchanged.
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req2.", lReq2_ExpectedStartFrom,
            lReq2_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req2.",
            lReq2_ExpectedStartUsage, lReq2_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req2.", lReq2_ExpectedDueUsage,
            lReq2_ActualDueUsage );

      // Then for Req2 (completed REQ after flight) the completion value changes due to it's
      // modified completion value moving from INSIDE to OUTSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req2.",
            lReq2_ExpectedCompletionUsage, lReq2_ActualCompletionUsage );

      // Then for Req3 (active REQ after Req2) the sched-from, start value, and due value are
      // changed due to Req2's completion CALC_PARM moving from INSIDE to OUTSIDE the window;
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start CALC_PARM changes to Req2's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req3.", lReq3_ExpectedStartFrom,
            lReq3_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req3.",
            lReq3_ExpectedStartUsage, lReq3_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req3.", lReq3_ExpectedDueUsage,
            lReq3_ActualDueUsage );
   }


   /**
    * Verify that when the flight usage is edited causing the next task's completion usage of a
    * calculated parameter to move from outside its schedule-to-plan window to inside and the
    * subsequent task's completion usage to move from inside to outside its schedule-to-plan window,
    * that the following active task's deadline start is calculated from the "last-end" and uses the
    * completed task's complete usage (it originally was calculated from the "last-due" and used the
    * completed task's due usage).
    *
    * <pre>
    * Arrangement:
    *  REQ interval 50 HOURS -5/+5 (window)
    * 
    *  T1 (complete)     Flight          T2 (complete)              T3 (complete)                  T4 (active)
    *  Start:    n/a     18 HOURS        Start:    LASTDUE 100      Start:    LASTEND 143          Start:    LASTDUE 193
    *  Due:      100     (36 CALC_PARM)  Due:      150              Due:      193                  Due:      243
    *  Complete: 100                     Complete: 143 (outside)    Complete: 189 (inside)
    * 
    * Act:
    *  Flight is increased by 3 HOURS (18 -> 21)
    * 
    * Assert (* = updated):
    *  T1 (complete)     Flight          T2 (complete)              T3 (complete)                  T4 (active)
    *  Start:    n/a     20 HOURS*       Start:    LASTDUE 100      Start:    LASTDUE* 150*        Start:    LASTEND 193
    *  Due:      100     (40 CALC_PARM)  Due:      150              Due:      200*                 Due:      243
    *  Complete: 100                     Complete: 147* (inside*)   Complete: 193* (outside*)
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
      final BigDecimal lInitialFlightHoursDelta = BigDecimal.valueOf( 18.0 );
      final BigDecimal lInitialFlightHoursTsn = BigDecimal.valueOf( 59.0 );
      BigDecimal lFlightAdjustment = BigDecimal.valueOf( +2 );
      BigDecimal lCalcParmAdjustment = lFlightAdjustment.multiply( MULTIPLIER_CONSTANT );

      // Req1 (completed before flight)
      RefSchedFromKey lReq1_StartFrom = NA_SCHED_FROM;
      BigDecimal lReq1_StartUsage = NA_START_USAGE;
      BigDecimal lReq1_DueUsage = BigDecimal.valueOf( 100.0 );
      BigDecimal lReq1_CompletionUsage = BigDecimal.valueOf( 100.0 );
      Date lReq1_CompletionDate = addDays( lFlightDate, -10 );

      // Req2 (completed after flight)
      RefSchedFromKey lReq2_StartFrom = LASTDUE;
      BigDecimal lReq2_StartUsage = lReq1_DueUsage;
      BigDecimal lReq2_DueUsage = lReq2_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq2_CompletionUsage = BigDecimal.valueOf( 143.0 );
      Date lReq2_CompletionDate = DateUtils.addDays( lFlightDate, +10 );

      // Req3 (completed after Req2)
      RefSchedFromKey lReq3_StartFrom = LASTEND;
      BigDecimal lReq3_StartUsage = lReq2_CompletionUsage;
      BigDecimal lReq3_DueUsage = lReq3_StartUsage.add( REQ_REPEAT_INTERVAL );
      BigDecimal lReq3_CompletionUsage = BigDecimal.valueOf( 189.0 );
      Date lReq3_CompletionDate = DateUtils.addDays( lReq2_CompletionDate, +10 );

      // Req4 (active - as indicated by no completion date)
      RefSchedFromKey lReq4_StartFrom = LASTDUE;
      BigDecimal lReq4_StartUsage = lReq3_DueUsage;
      BigDecimal lReq4_DueUsage = lReq4_StartUsage.add( REQ_REPEAT_INTERVAL );

      // *** Data for arrangement end.

      // *** Data for assertion (referring to values in method comment).

      // Req2
      RefSchedFromKey lReq2_ExpectedStartFrom = lReq2_StartFrom;
      Double lReq2_ExpectedStartUsage = lReq2_StartUsage.doubleValue();
      Double lReq2_ExpectedDueUsage = lReq2_DueUsage.doubleValue();
      Double lReq2_ExpectedCompletionUsage =
            lReq2_CompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req3
      RefSchedFromKey lReq3_ExpectedStartFrom = LASTDUE;
      Double lReq3_ExpectedStartUsage = lReq2_ExpectedDueUsage;
      Double lReq3_ExpectedDueUsage = lReq3_ExpectedStartUsage + REQ_REPEAT_INTERVAL.doubleValue();
      Double lReq3_ExpectedCompletionUsage =
            lReq3_CompletionUsage.add( lCalcParmAdjustment ).doubleValue();

      // Req4
      RefSchedFromKey lReq4_ExpectedStartFrom = LASTEND;
      Double lReq4_ExpectedStartUsage = lReq3_ExpectedCompletionUsage;
      Double lReq4_ExpectedDueUsage = lReq4_ExpectedStartUsage + REQ_REPEAT_INTERVAL.doubleValue();

      // *** Data for assertion end.

      //
      // Arrangements
      //

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addCalcParmFunctionToDatabase();
      }

      //
      // Given an aircraft assembly tracking both HOURS and a calculated parameter based on HOURS.
      //
      AssemblyKey lAcftAssy = createAcftAssyTrackingParmsAndCalcParms();

      // Get the data type key for the calculated parameter.
      final DataTypeKey lCalcParm =
            readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given an aircraft based on that assembly collecting HOURS and CALC_PARM usage
      iAircraft = createAircraftInventory( lAcftAssy, HOURS, lCalcParm );

      // Given a recurring requirement definition with a scheduling rule for HOURS that has a
      // schedule to plan window.
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {

                  OneTimeSchedulingRule lSchedRule = new OneTimeSchedulingRule();
                  lSchedRule.setUsageParameter( lCalcParm );
                  lSchedRule.setThreshold( REQ_REPEAT_INTERVAL );
                  lSchedRule.setSchedToPlanLow( SCHEDULE_TO_PLAN_LOW );
                  lSchedRule.setSchedToPlanHigh( SCHEDULE_TO_PLAN_HIGH );

                  aReqDefn.addSchedulingRule( lSchedRule );
               }
            } );

      // Given a recurring REQ completed prior to the target flight that will establish the Start
      // From, Start Value, and Due value of the completed task after the flight (see below).
      TaskKey lPrevReq = null;
      TaskKey lReq1 = createRequirement( lPrevReq, lCalcParm, lReq1_StartFrom, lReq1_StartUsage,
            lReq1_DueUsage, lReq1_CompletionUsage, lReq1_CompletionDate );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight =
            createFlight( lFlightDate, lInitialFlightHoursDelta, lInitialFlightHoursTsn );

      // Given a recurring REQ completed after to the target flight that has its completed CALC_PARM
      // outside the schedule to plan window.
      lPrevReq = lReq1;
      TaskKey lReq2 = createRequirement( lPrevReq, lCalcParm, lReq2_StartFrom, lReq2_StartUsage,
            lReq2_DueUsage, lReq2_CompletionUsage, lReq2_CompletionDate );

      // Given a recurring REQ completed after to the last completed REQ that has its completed
      // CALC_PARM within the schedule to plan window.
      lPrevReq = lReq2;
      TaskKey lReq3 = createRequirement( lPrevReq, lCalcParm, lReq3_StartFrom, lReq3_StartUsage,
            lReq3_DueUsage, lReq3_CompletionUsage, lReq3_CompletionDate );

      // Given a recurring, active REQ (incomplete) following the last completed REQ.
      // (completion usage and date are NOT set, i.e. null)
      lPrevReq = lReq3;
      TaskKey lReq4 = createRequirement( lPrevReq, lCalcParm, lReq4_StartFrom, lReq4_StartUsage,
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

      // Get Req2's deadline and usage at completion for CALC_PARM.
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lReq2.getEventKey(), lCalcParm );
      EvtSchedDeadTable lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      UsageSnapshot lUsage = Domain.readUsageAtCompletion( lReq2, iAircraft, lCalcParm );

      RefSchedFromKey lReq2_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq2_ActualStartUsage = lDeadline.getStartQt();
      Double lReq2_ActualDueUsage = lDeadline.getDeadlineQt();
      Double lReq2_ActualCompletionUsage = lUsage.getTSN();

      // Get Req3's deadline and usage at completion for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq3.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      lUsage = Domain.readUsageAtCompletion( lReq3, iAircraft, lCalcParm );

      RefSchedFromKey lReq3_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq3_ActualStartUsage = lDeadline.getStartQt();
      Double lReq3_ActualDueUsage = lDeadline.getDeadlineQt();
      Double lReq3_ActualCompletionUsage = lUsage.getTSN();

      // Get Req4's deadline for CALC_PARM.
      lDeadlineKey = new EventDeadlineKey( lReq4.getEventKey(), lCalcParm );
      lDeadline = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      RefSchedFromKey lReq4_ActualStartFrom = lDeadline.getScheduledFrom();
      Double lReq4_ActualStartUsage = lDeadline.getStartQt();
      Double lReq4_ActualDueUsage = lDeadline.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropCalcParmFunctionToDatabase();
      }

      // Then for Req2 (completed REQ after flight) the sched-from, start value, and due value are
      // unchanged.
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req2.", lReq2_ExpectedStartFrom,
            lReq2_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req2.",
            lReq2_ExpectedStartUsage, lReq2_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req2.", lReq2_ExpectedDueUsage,
            lReq2_ActualDueUsage );

      // Then for Req2 (completed REQ after flight) the completion value changes due to it's
      // modified completion value moving from OUTSIDE to INSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req3.",
            lReq2_ExpectedCompletionUsage, lReq2_ActualCompletionUsage );

      // Then for Req3 (active REQ after Req2) the sched-from, start value, and due value are
      // changed due to Req2's completion CALC_PARM moving from OUTSIDE to INSIDE the window;
      // - the Start From changes from LASTEND to LASTDUE
      // - the Start CALC_PARM changes to Req2's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req3.", lReq3_ExpectedStartFrom,
            lReq3_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req3.",
            lReq3_ExpectedStartUsage, lReq3_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req3.", lReq3_ExpectedDueUsage,
            lReq3_ActualDueUsage );

      // Then for Req3 (completed after Req2) the completion value changes due to it's
      // modified completion value moving from INSIDE to OUTSIDE the schedule-to-plan window.
      assertEquals( "Unexpected CALC_PARM caompletion value for Req3.",
            lReq3_ExpectedCompletionUsage, lReq3_ActualCompletionUsage );

      // Then for Req4 (active REQ after Req3) the sched-from, start value, and due value are
      // changed due to Req3's completion CALC_PARM moving from INSIDE to OUTSIDE the window;
      // - the Start From changes from LASTDUE to LASTEND
      // - the Start CALC_PARM changes to Req3's completion CALC_PARM
      // - the Due CALC_PARM changes to the Start CALC_PARM + interval
      assertEquals( "Unexpected CALC_PARM deadline's sched-from for Req4.", lReq4_ExpectedStartFrom,
            lReq4_ActualStartFrom );
      assertEquals( "Unexpected CALC_PARM deadline's start value for Req4.",
            lReq4_ExpectedStartUsage, lReq4_ActualStartUsage );
      assertEquals( "Unexpected CALC_PARM deadline's due value for Req4.", lReq4_ExpectedDueUsage,
            lReq4_ActualDueUsage );

   }


   @Before
   public void setup() {

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      iFlightHistBean.setSessionContext( null );
   }


   private void addCalcParmFunctionToDatabase() throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + MULTIPLIER_FUNCTION_NAME
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN NUMBER" + " " + "IS "
            + "result NUMBER; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + MULTIPLIER_FUNCTION_NAME + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   private void dropCalcParmFunctionToDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit rollback before function drop ensuring no data gets committed
      // accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            MULTIPLIER_FUNCTION_NAME );
   }


   private AssemblyKey createAcftAssyTrackingParmsAndCalcParms() {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {

            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootCs ) {

                  aRootCs.addUsageParameter( HOURS );

                  // Calculate parameter based on HOURS.
                  aRootCs.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( CALC_PARM_CODE );
                              aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                              aBuilder.setPrecisionQt( 2 );
                              aBuilder.addParameter( HOURS );
                              aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME, MULTIPLIER_CONSTANT );
                           }

                        } );
               }
            } );
         }
      } );
   }


   private InventoryKey createAircraftInventory( final AssemblyKey aAssy,
         final DataTypeKey... aUsageParms ) {

      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         // Current usage is not relevant to the test.
         BigDecimal lNotApplicableCurrUsage = null;


         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( aAssy );
            for ( DataTypeKey lUsageParm : aUsageParms ) {
               aAircraft.addUsage( lUsageParm, lNotApplicableCurrUsage );
            }
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


   private TaskKey createRequirement( final TaskKey aPrevReq, final DataTypeKey aCalcUsageParm,
         final RefSchedFromKey aSchedFrom, final BigDecimal aStartUsage, final BigDecimal aDueUsage,
         final BigDecimal aCalcCompletionUsage, final Date aCompletionDate ) {

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
                  aDeadline.setUsageType( aCalcUsageParm );
                  aDeadline.setScheduledFrom( aSchedFrom );
                  aDeadline.setStartTsn( aStartUsage );
                  aDeadline.setDueValue( aDueUsage );
                  aDeadline.setInterval( REQ_REPEAT_INTERVAL );
                  aDeadline.setScheduleToPlanWindow( SCHEDULE_TO_PLAN_LOW, SCHEDULE_TO_PLAN_HIGH );
               }
            } );

            if ( COMPLETE.equals( lStatus ) ) {
               aReq.addUsage( new UsageSnapshot( iAircraft, aCalcUsageParm,
                     aCalcCompletionUsage.doubleValue() ) );
               aReq.addUsage( new UsageSnapshot( iAircraft, HOURS,
                     aCalcCompletionUsage.divide( MULTIPLIER_CONSTANT ).doubleValue() ) );
            }
         }
      } );

      return lRequirement;
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

      Date lDepartureDate = DateUtils.addHours( aArrivalDate,
            aHours.multiply( BigDecimal.valueOf( -1.0 ) ).intValue() );

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

      // Create HR that is editing the flight.
      HumanResourceKey lHrKey = new HumanResourceDomainBuilder().withUsername( "username" ).build();
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iFlightHistBean.editHistFlight( aFlight, lHrKey, lUpdatedFlightTO,
            new CollectedUsageParm[] { lUpdatedFlightUsage }, null );

      // Reset the user parameters singleton.
      UserParameters.setInstance( lUserId, "LOGIC", null );

   }

}
