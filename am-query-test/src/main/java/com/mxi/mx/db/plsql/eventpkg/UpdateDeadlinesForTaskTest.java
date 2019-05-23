
package com.mxi.mx.db.plsql.eventpkg;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createFlight;
import static com.mxi.am.domain.Domain.createForecastModel;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DST_OFFSET;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.MONTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.Statement;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.DomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.report.DataType;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventRelationKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.EventInventoryService;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.unittest.ProcedureStatementFactory;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;


/**
 * Tests for the EVENT_PKG.UpdateDeadlinesForTask PLSQL procedure.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateDeadlinesForTaskTest {

   // We have to use the current date, as the PLSQL makes its calculations based on the
   // current date.
   private static final Calendar CALENDAR_NOW = Calendar.getInstance();
   private static final Date NOW = CALENDAR_NOW.getTime();

   private static final Double DEFAULT_FC_MODEL_RATE = 1.0d;
   private static final BigDecimal DEFAULT_CURRENT_USAGE = BigDecimal.ZERO;

   private static final int FLIGHT_DURATION = 2;

   private static final int HOURS_BETWEEN_FLIGHTS = 1;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that a task's usage remaining is correctly updated.
    */
   @Test
   public void testUsageRemainingIsCorrectlyUpdatedWhenValueIncorrect() {
      // Create an aircraft.
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, DEFAULT_FC_MODEL_RATE );
                     }

                  } ) );
            aAircraft.addUsage( HOURS, DEFAULT_CURRENT_USAGE );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      int lDeadlineInterval = 100;
      createDeadlineForTask( lTaskKey, HOURS, lDeadlineInterval );

      updateDeadlinesForTask( lAircraft, lTaskKey );

      // Ensure the usage remaining is expected for the task.
      EvtSchedDeadTable lEvtSchedDeadTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( lTaskKey.getEventKey(), HOURS ) );
      BigDecimal lExpectedUsageRemaining =
            new BigDecimal( lDeadlineInterval ).subtract( DEFAULT_CURRENT_USAGE );
      assertEquals( lExpectedUsageRemaining,
            new BigDecimal( lEvtSchedDeadTable.getUsageRemaining() ) );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Task Definition with Reschedule From as WPEND</li>
    * <li>Work Package Exists:
    * <ul>
    * <li>Scheduled End is Set</li>
    * <li>Task 1 is in the Work Package</li>
    *
    * </ul>
    * </li>
    * <li>Aircraft has usages against it</li>
    * <li>Usage snapshot exists for the work package and task</li>
    * <li>A historic task is:
    * <ul>
    * <li>Start_qt is set</li>
    * <li>Prefix and Postfix are set</li>
    * <li>completed within planning window</li>
    * <li>rescheduled from WPEND or WPSTART</li>
    * <li>usage based</li>
    * </ul>
    * </li>
    * <li>A Next task exists:
    * <ul>
    * <li>Prefix and Postfix are set</li>
    * <li>rescheduled from WPEND or WPSTART</li>
    * </ul>
    * </li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Run the evt_event_pkg.updatedeadlinesfortask procedure</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>The next task's is scheduled from LASTDUE</li>
    * <li>The next task's start_qt is showing the last tasks last due qt value.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testUsageStartQtyIsPreviousDueQty() throws Exception {

      RefSchedFromKey lSchedFromBirth = RefSchedFromKey.BIRTH;
      RefSchedFromKey lSchedFromWPEND = RefSchedFromKey.WPEND;

      int lInterval = 100;

      double lStartQt = 0d;
      double lPrefixedQt = 100d;
      double lPostfixedQt = 0d;

      Double lFirstDueQty = 100.0;
      Double lSecondDueQty = 125.0;

      double lCurrentUsage = 25d;

      DomainBuilder<TaskTaskKey> lTaskDefnBuilder =
            new TaskRevisionBuilder().isUnique().withRescheduleFrom( RefReschedFromKey.WPEND );
      final Double aCurrentUsage = lCurrentUsage;

      // Create an aircraft.
      final InventoryKey lAircraft = createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft
                  .setForecastModel( createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, DEFAULT_FC_MODEL_RATE );
                     }

                  } ) );
            aAircraft.addUsage( HOURS, new BigDecimal( aCurrentUsage ) );
         }
      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setName( "Test WP" );
            aBuilder.setAircraft( lAircraft );
         }
      } );

      EvtEventTable lWorkPackageEvent =
            EvtEventTable.findByPrimaryKey( lWorkPackage.getEventKey() );

      Calendar lWorkPackageDates = Calendar.getInstance();
      lWorkPackageDates.add( Calendar.DATE, 10 );
      lWorkPackageEvent.setSchedEndDt( lWorkPackageDates.getTime() );
      lWorkPackageEvent.update();

      takeUsageSnapshot( lWorkPackage.getEventKey() );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().withTaskRevision( lTaskDefnBuilder )
            .onInventory( lAircraft ).withParentTask( lWorkPackage ).build();

      takeUsageSnapshot( lTaskKey.getEventKey() );

      // Create a deadline for the task.
      Calendar lInstance = Calendar.getInstance();
      Date aFirstDueDate = lInstance.getTime();

      Date lDeadlineForFirstTask = createDeadlineForTask( lTaskKey, HOURS, lInterval,
            lSchedFromBirth, lFirstDueQty, aFirstDueDate );

      updateDeadlinesForTask( lAircraft, lTaskKey );

      // update the deadline sched rule so it has a prefix and a postfix and a start qt
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( lTaskKey.getEventKey(), HOURS );
      EvtSchedDeadTable lEvtSchedDeadTable = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );

      lEvtSchedDeadTable.setStartQt( lStartQt );
      lEvtSchedDeadTable.setPrefixedQt( lPrefixedQt );
      lEvtSchedDeadTable.setPostfixedQt( lPostfixedQt );
      lEvtSchedDeadTable.update();

      assertEquals( Double.valueOf( lStartQt ), lEvtSchedDeadTable.getStartQt() );
      assertEquals( Double.valueOf( lEvtSchedDeadTable.getStartQt() + lInterval ),
            lEvtSchedDeadTable.getDeadlineQt() );

      // Assert the aircraft usage records
      assertEvtInvUsage( lAircraft, lTaskKey, lCurrentUsage );

      // make sure this is a historic task
      EvtEventTable lEvtEventTable = EvtEventTable.findByPrimaryKey( lTaskKey.getEventKey() );
      lEvtEventTable.setHistBool( true );
      lEvtEventTable.setEventStatus( RefEventStatusKey.COMPLETE );
      lEvtEventTable.update();

      assertEquals( true, lEvtEventTable.getHistBool() );
      assertEquals( RefEventStatusKey.COMPLETE.getCd(), lEvtEventTable.getEventStatusCd() );

      // create a next task which we will attempt to update it's deadlines
      TaskKey lNextWPTask = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setName( "Test WP2" );
            aBuilder.setAircraft( lAircraft );
         }
      } );

      TaskKey lNextTaskKey =
            new TaskBuilder().withTaskRevision( lTaskDefnBuilder ).onInventory( lAircraft )
                  .withParentTask( lNextWPTask ).withPrevTask( lTaskKey ).build();

      // make sure the relationship to the previous task is correctly set up
      EventRelationKey lEventRelationKey =
            new EventRelationKey( lTaskKey.getDbId(), lTaskKey.getId(), 1 );
      EvtEventRel lEventRel = EvtEventRel.findByPrimaryKey( lEventRelationKey );

      assertEquals( RefRelationTypeKey.DEPT.getCd(), lEventRel.getRelType() );
      assertEquals( lNextTaskKey.toString(), lEventRel.getRelEvent().toString() );

      // assert the next task is ACTV
      lEvtEventTable = EvtEventTable.findByPrimaryKey( lNextTaskKey.getEventKey() );

      assertEquals( false, lEvtEventTable.getHistBool() );
      assertEquals( RefEventStatusKey.ACTV.getCd(), lEvtEventTable.getEventStatusCd() );

      // set the due date for the second task about 100 days into the future from the previous task
      // to simulate an interval of 100 hours
      lInstance.add( Calendar.DATE, lInterval );
      Date aNextDueDate = lInstance.getTime();

      // create the deadline
      createDeadlineForTask( lNextTaskKey, HOURS, lInterval, lSchedFromWPEND, lSecondDueQty,
            aNextDueDate );

      EventDeadlineKey lKey = new EventDeadlineKey( lNextTaskKey.getEventKey(), HOURS );
      EvtSchedDeadTable lNextEvtSchedDeadTable = EvtSchedDeadTable.findByPrimaryKey( lKey );
      lNextEvtSchedDeadTable.setStartQt( lStartQt );
      lNextEvtSchedDeadTable.setPrefixedQt( lPrefixedQt );
      lNextEvtSchedDeadTable.setPostfixedQt( lPostfixedQt );
      lNextEvtSchedDeadTable.setStartDate( lDeadlineForFirstTask );
      lNextEvtSchedDeadTable.update();

      updateDeadlinesForTask( lAircraft, lNextTaskKey );

      // assert the deadline has been updated. Make sure it is not pointing to the usage snapshot
      // which it used to do. We want the start_qt to be pointing to the previous tasks due qty.
      assertEquals( RefSchedFromKey.LASTDUE, lNextEvtSchedDeadTable.getScheduledFrom() );
      assertEquals( lFirstDueQty, lNextEvtSchedDeadTable.getStartQt() );

   }


   private void assertEvtInvUsage( InventoryKey lAircraft, TaskKey lTaskKey,
         Double aExpectedUsage ) {
      EventInventoryKey lEventInventory =
            new EventInventoryKey( lTaskKey.getEventKey(), lAircraft.getId() );
      EventInventoryUsageKey lEventInventoryUsageKey =
            new EventInventoryUsageKey( lEventInventory, HOURS );

      EvtInvUsageTable lEvtInvUsage = EvtInvUsageTable.findByPrimaryKey( lEventInventoryUsageKey );

      assertEquals( aExpectedUsage, Double.valueOf( lEvtInvUsage.getTsnQt() ) );
   }


   /**
    * Verify the deadline is properly updated for an HOURS usage based task, against an aircraft
    * with planned flights that combined acrue usage beyond the deadline's due quantity. This
    * exercises the specific flight plan handling logic for HOURS based deadlines.<br>
    * <br>
    * It is expected that the updated deadline due date would be 1 minute prior to the particular
    * flight that causes the acrued usage to equal or exceed the deadline's due quantity.
    *
    * @throws Exception
    */
   @Test
   public void testWithFlightPlanAndOnlyHoursDeadline() throws Exception {

      // Create an aircraft.
      InventoryKey lAircraft = createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft
                  .setForecastModel( createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, DEFAULT_FC_MODEL_RATE );
                     }

                  } ) );
            aAircraft.addUsage( HOURS, DEFAULT_CURRENT_USAGE );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      int lDeadlineInterval = 5;
      Date lDueDate = createDeadlineForTask( lTaskKey, HOURS, lDeadlineInterval );

      // Ensure the current deadline is as expected.
      assertEquals( lDueDate, getTaskDeadlineDate( lTaskKey, HOURS ) );

      // Create a series of flights on the current day,
      // each with a duration of 2 hours and with a space of 1 hour between them.
      Date lDepartureDate;
      Date lArrivalDate;

      // 2 hour flight < 5 hour due quantity, thus not due
      lDepartureDate = NOW;
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // ( 2 X 2 hour flight = 4 ) < 5 hour due quantity, thus not due
      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // ( 3 X 2 hour flight = 6 ) >= 5 hour due quantity, thus
      // the task will be due within the third flight.
      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // Assert that the deadline will be 1 minute prior to the third flight (with no milliseconds).
      Date lExpectedDate = floorMillisecond( addMinutes( lDepartureDate, -1 ) );
      assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, HOURS ) );
   }


   /**
    * Verify the deadline is properly updated for a LANDING usage based task, against an aircraft
    * with planned flights that combined acrue usage beyond the deadline's due quantity. This
    * exercises the specific flight plan handling logic for LANDING based deadlines.<br>
    * <br>
    * It is expected that the updated deadline due date would be 1 minute after the particular
    * flight that causes the acrued usage to equal or exceed the deadline's due quantity.
    *
    * @throws Exception
    */
   @Test
   public void testWithFlightPlanAndOnlyLandingDeadline() throws Exception {

      // Create an aircraft.
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, LANDING, DEFAULT_FC_MODEL_RATE );
                     }

                  } ) );
            aAircraft.addUsage( LANDING, DEFAULT_CURRENT_USAGE );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      createDeadlineForTask( lTaskKey, LANDING, 3 );

      // Create a series of flights on the current day,
      // each with a duration of 2 hours and with a space of 1 hour between them.
      Date lDepartureDate;
      Date lArrivalDate;

      // 1 landing/flight < 3 landing due quantity, thus not due
      lDepartureDate = NOW;
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // ( 2 X 1 landing/flight = 2 ) < 3 landing due quantity, thus not due
      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // ( 3 X 1 landing/flight = 3 ) >= 3 landing due quantity, thus
      // the task will be due after the third flight.
      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // The deadline will be 1 minute after this third flight (with no milliseconds).
      Date lExpectedDate = floorMillisecond( addMinutes( lArrivalDate, 1 ) );
      assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, LANDING ) );
   }


   /**
    * Test that when a usage-based deadline is updated on a loose inventory, the deadline date is
    * set to null. This is done because there is no way to predict the usage of a loose component.
    */
   @Test
   public void testWithLooseInventory() {
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withHoursUsage( DEFAULT_CURRENT_USAGE.doubleValue() ).build();

      TaskKey lTask = new TaskBuilder().onInventory( lInventory ).build();

      EventDeadlineKey lDeadline = new TaskDeadlineBuilder( lTask ).withDataType( HOURS )
            .withInterval( 1.0 ).withDueDate( DateUtils.getEndOfPrevDay( NOW ) )
            .withStartDate( DateUtils.getEndOfPrevDay( NOW ) )
            .scheduledFrom( RefSchedFromKey.EFFECTIV ).build();

      updateDeadlinesForTask( null, lTask );

      new EvtSchedDead( lDeadline ).assertSchedDeadDt( null );
   }


   /**
    * Verify the deadline is properly updated for an HOURS usage based task, against an aircraft
    * with with a forecast model having multiple ranges with varying rates.
    *
    * @throws Exception
    */
   @Test
   public void testWithMultipleForecastRanges() throws Exception {

      // Create an aircraft.
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, BigDecimal.ZERO );
            aAircraft.setForecastModel( createTestForecastModel() );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      createDeadlineForTask( lTaskKey, HOURS, 100, RefSchedFromKey.CUSTOM, 100d, new Date() );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // Determine the expected date using this formula:
      // with a deadline quantity of 100,
      // range 1 -> 15 days @ 2 HOURS/day = 30 HOURS
      // (usage remaining = 100 - 30 = 70 HOURS, after 15 days)
      // range 2 -> 10 days @ 3 HOURS/day = 30 HOURS
      // (usage remaining = 70 - 30 = 40 HOURS, after 15 + 10 = 25 days)
      // Thus, usage remaining @ 3rd range rate is: 40 HOURS @ 5 HOURS/day = 8 days
      // Therefore, the deadline will be 25 + 8 = 33 days.

      Date lActualDate = getTaskDeadlineDate( lTaskKey, HOURS );

      String lActualDateStr =
            DateUtils.toString( lActualDate, "yyyy-MM-dd HH:mm:ss" ).substring( 0, 10 );

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
      String lDateNow = simpleDateFormat.format( new Date() );

      Assert.assertTrue( "The days difference is 33 days",
            DateDiffInDays( lDateNow, lActualDateStr ) == 33 );

      // Date lExpectedDate = calculateExpectedDeadlineDt( NOW, 33 );

      // Verify the task's deadline has not changed.

      // assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, HOURS ) );

   }


   /**
    * Verify the deadline is properly updated for an HOURS usage based task, against an aircraft
    * with with a forecast model having multiple ranges with varying rates AND with a blackout
    * period spaning the ranges.
    *
    * @throws Exception
    */
   @Test
   public void testWithMultipleForecastRangesAndBlackout() throws Exception {
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, BigDecimal.ZERO );

            // Forecast Model
            aAircraft.setForecastModel( createTestForecastModel() );

            // Set the blackout period to start 10 days after the start of the first forecast range.
            GregorianCalendar lBlackoutStart = new GregorianCalendar();
            lBlackoutStart.setTime( NOW );
            lBlackoutStart.add( DATE, 10 );

            // Set the blackout period to end 10 days after the start of the third forecast range.
            GregorianCalendar lBlackoutEnd = new GregorianCalendar();
            lBlackoutEnd.setTime( NOW );
            lBlackoutEnd.add( DATE, 35 );

            aAircraft.addBlackout( lBlackoutStart.getTime(), lBlackoutEnd.getTime() );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      createDeadlineForTask( lTaskKey, HOURS, 100 );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // Determine the expected date using this formula:
      // with a deadline quantity of 100,
      // range 1 -> 10 days @ 2 HOURS/day + 5 days @ 0 HOURS/day (blackout) = 20 HOURS
      // (usage remaining = 100 - 20 = 80 HOURS, after 10 + 5 = 15 days)
      // range 2 -> 10 days @ 0 HOURS/day (blackout) = 0 HOURS
      // (usage remaining = 80 - 0 = 80 HOURS, after 15 + 10 = 25 days)
      // range 3 -> 10 days @ 0 HOURS/day (blackout) = 0 HOURS
      // (usage remaining = 80 - 0 = 80 HOURS, after 25 + 10 = 35 days)
      // Thus, usage remaining @ 3rd range rate is: : 80 HOURS @ 5 HOURS/day = 16 days
      // Therefore, the deadline will be 35 + 16 = 51 days.

      Date lActualDate = getTaskDeadlineDate( lTaskKey, HOURS );

      String lActualDateStr =
            DateUtils.toString( lActualDate, "yyyy-MM-dd HH:mm:ss" ).substring( 0, 10 );

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
      String lDateNow = simpleDateFormat.format( new Date() );

      Assert.assertTrue( "The days difference is 51 days",
            DateDiffInDays( lDateNow, lActualDateStr ) == 51 );

      // Date lExpectedDate = calculateExpectedDeadlineDt( NOW, 51 );
      // assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, HOURS ) );
   }


   /**
    * Verify the deadline is properly updated for an HOURS usage based task, against an aircraft
    * with with a forecast model having multiple ranges with varying rates AND with a flight plan.
    *
    * @throws Exception
    */
   @Test
   public void testWithMultipleForecastRangesAndFlightPlan() throws Exception {

      // Create an aircraft.
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setForecastModel( createTestForecastModel() );
            aAircraft.addUsage( HOURS, DEFAULT_CURRENT_USAGE );
         }
      } );

      // Create a series of flights on the current day,
      // each with a duration of 2 hours and with a space of 1 hour between them.
      Date lDepartureDate;
      Date lArrivalDate;

      lDepartureDate = NOW;
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      lDepartureDate = DateUtils.addHours( lArrivalDate, HOURS_BETWEEN_FLIGHTS );
      lArrivalDate = DateUtils.addHours( lDepartureDate, FLIGHT_DURATION );
      createTestFlight( lAircraft, lDepartureDate, lArrivalDate );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      createDeadlineForTask( lTaskKey, HOURS, 100 );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // The flight plan has all of its flights within the same day (today). Thus, for the
      // first day only the flight plan usage is used = ( 4 flights X 2 HOURS/flight ) = 8 HOURS
      // The remaining will use the forecasting model:
      // with a deadline quantity of 100,
      // range 1 (minus a day for the flights) -> 14 days @ 2 HOURS/day = 28 HOURS
      // (usage remaining = 100 - 8 - 28 = 64 HOURS, after 14 days)
      // range 2 -> 10 days @ 3 HOURS/day = 30 HOURS
      // (usage remaining = 64 - 30 = 34 HOURS, after 14 + 10 = 24 days)
      // Thus, usage remaining @ 3rd range rate is: 34 HOURS @ 5 HOURS/day = 6.8 days ~= 7 days
      // Therefore, the deadline will be 24 + 7 = 31 days.

      Date lActualDate = getTaskDeadlineDate( lTaskKey, HOURS );

      String lActualDateStr =
            DateUtils.toString( lActualDate, "yyyy-MM-dd HH:mm:ss" ).substring( 0, 10 );

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
      String lDateNow = simpleDateFormat.format( new Date() );

      Assert.assertTrue( "The days difference is 31 days",
            DateDiffInDays( lDateNow, lActualDateStr ) == 31 );

      // Verify the task's deadline has not changed.
      // Date lExpectedDate = calculateExpectedDeadlineDt( NOW, 31 );
      // assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, HOURS ) );
   }


   /**
    * Creates a forecast model based on the current time.<br>
    * <br>
    * Because EVENT_PKG.UpdateDeadlinesForTask calculates deadlines base on the current time we must
    * create a forecast model based on the current time (unfortunately, we cannot use a set point in
    * time like usual). But this also means we have to take into account ranges that extend over
    * year end boundary and Feb. 29th on a lead year (which are not possible via the GUI).
    *
    * @throws Exception
    */
   private FcModelKey createTestForecastModel() {
      return createForecastModel( new DomainConfiguration<ForecastModel>() {

         @Override
         public void configure( ForecastModel aForecastModel ) {
            GregorianCalendar lFcRangeStart = new GregorianCalendar();
            int lFcRangeStartMonth;
            int lFcRangeStartDay;
            Double lFcRangeRate;

            // First range starts today, with a rate of 2 HOURS.
            lFcRangeStart.setTime( NOW );
            lFcRangeStartMonth = lFcRangeStart.get( MONTH ) + 1;
            lFcRangeStartDay = lFcRangeStart.get( DATE );
            lFcRangeRate = 2.0d;
            aForecastModel.addRange( lFcRangeStartMonth, lFcRangeStartDay, HOURS, lFcRangeRate );

            if ( lFcRangeStartMonth == 2 && lFcRangeStartDay == 29 ) {
               // Skip this test if the forecast range starts on Feb. 29th of a leap year.
               throw new UnableToGenerateTestForcastModelException();
            }

            // Second range starts 15 days after today, with a rate of 3 HOURS.
            lFcRangeStart.add( DATE, 15 );
            lFcRangeStartMonth = lFcRangeStart.get( MONTH ) + 1;
            lFcRangeStartDay = lFcRangeStart.get( DATE );
            lFcRangeRate = 3.0d;
            aForecastModel.addRange( lFcRangeStartMonth, lFcRangeStartDay, HOURS, lFcRangeRate );

            if ( lFcRangeStartMonth == 2 && lFcRangeStartDay == 29 ) {
               // Skip this test if the forecast range starts on Feb. 29th of a leap year.
               throw new UnableToGenerateTestForcastModelException();
            }

            // Third range starts 10 days after the second range, with a rate of 5 HOURS.
            lFcRangeStart.add( DATE, 10 );
            lFcRangeStartMonth = lFcRangeStart.get( MONTH ) + 1;
            lFcRangeStartDay = lFcRangeStart.get( DATE );
            lFcRangeRate = 5.0d;
            aForecastModel.addRange( lFcRangeStartMonth, lFcRangeStartDay, HOURS, lFcRangeRate );

            if ( lFcRangeStartMonth == 2 && lFcRangeStartDay == 29 ) {
               // Skip this test if the forecast range starts on Feb. 29th of a leap year.
               throw new UnableToGenerateTestForcastModelException();
            }

            if ( CALENDAR_NOW.get( java.util.Calendar.YEAR ) != lFcRangeStart
                  .get( java.util.Calendar.YEAR ) ) {
               // The first range always starts within the current year and all the ranges must fall
               // within the same year.
               // Therefore, skip this test if the final range falls within the next year.
               throw new UnableToGenerateTestForcastModelException();
            }
         }
      } );
   }


   /**
    * Verify the deadline is properly updated for an HOURS usage based task, against an aircraft
    * with with a forecast model having one range.
    *
    * @throws Exception
    */
   @Test
   public void testWithOneForecastRange() throws Exception {

      // Create an aircraft.
      InventoryKey lAircraft = createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft
                  .setForecastModel( createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, 4.0d );
                     }

                  } ) );
            aAircraft.addUsage( HOURS, BigDecimal.ZERO );
         }
      } );

      // Create a task against the aircraft.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lAircraft ).build();

      // Create a deadline for the task.
      createDeadlineForTask( lTaskKey, HOURS, 100 );

      // Execute the updating of the task deadlines.
      updateDeadlinesForTask( lAircraft, lTaskKey );

      // The expected date is the due quantity divided by the forecasted rate:
      // 100 HOURS @ 4 HOURS/day = 25 days
      // (the deadline date will be end of the previous day, with no milliseconds )
      Date lActualDate = getTaskDeadlineDate( lTaskKey, HOURS );
      String lActualDateStr =
            DateUtils.toString( lActualDate, "yyyy-MM-dd HH:mm:ss" ).substring( 0, 10 );

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
      String lDateNow = simpleDateFormat.format( new Date() );

      Assert.assertTrue( "The days difference is 25 days",
            DateDiffInDays( lDateNow, lActualDateStr ) == 25 );

      // Date lExpectedDate = calculateExpectedDeadlineDt( NOW, 25 );
      // assertEquals( lExpectedDate, getTaskDeadlineDate( lTaskKey, HOURS ) );
   }


   /**
    * Calls APP_OBJ_PKG_GETMXIERROR PLSQL procedure. Performs some error checking a
    *
    * @return description of the Returned Value.
    *
    * @exception MxRuntimeException
    *               if the PLSQL error occured.
    */
   protected String getPlsqlErrors() throws MxRuntimeException {

      DataSetArgument lArgs = new DataSetArgument();
      String lErrMsg;

      // Call the PL/SQL procedure
      lArgs = MxDataAccess.getInstance()
            .executeWithReturnParms( "com.mxi.mx.core.query.plsql.AppObjPkgGetMxiError", lArgs );

      // Extract the return arguments
      lErrMsg = lArgs.getString( "aErrMsg" );

      // If no error was returned, make sure to return empty string
      if ( StringUtils.isBlank( lErrMsg ) ) {

         return "";
      }

      // Return error message
      return lErrMsg;
   }


   /**
    * creates a usage record
    *
    * @param aInventory
    *           the inventory
    * @param aTSN
    *           the tsn value for the record
    * @param aURDate
    *           the record's date
    *
    * @throws Exception
    *            should an exception occur
    */
   private void takeUsageSnapshot( EventKey aEventKey ) throws Exception {

      new EventInventoryService().takeCurrentUsageSnapshotByEvent( aEventKey );

   }


   /**
    * Return a new date that is based on the provided original date that has be incremented by the
    * provided number of minutes. Note: this method is not provided in DateUtils.
    *
    * @param aOrigDate
    *           original date
    * @param aNumMinutes
    *           number of minutes to add
    *
    * @return new date
    */
   private Date addMinutes( Date aOrigDate, int aNumMinutes ) {
      Calendar lCal = Calendar.getInstance();
      lCal.setTime( aOrigDate );
      lCal.add( Calendar.MINUTE, aNumMinutes );

      return lCal.getTime();
   }


   /**
    * Calculates the deadline date, given the current date and the number of hours until the
    * deadline. As per the Maintenix deadline calculation logic, the hours are added to the current
    * date, and the deadline is set to 23:59 of the previous day. NOTE: Maintenix logic currently
    * DOES NOT handle the switch from standard time to daylight savings time, and vice-versa. The
    * GregorianCalendar employed in this method does handle the switches, however. As a result, this
    * method adds/subtracts an hour when necessary to keep the test case deadlines in line with the
    * expected Maintenix deadlines.
    *
    * @param aCurrentDt
    *           the current date.
    * @param aNumDaysUntilDeadline
    *           the number of days until the deadline.
    *
    * @return the deadline date
    */
   private Date calculateExpectedDeadlineDt( Date aCurrentDt, int aNumDaysUntilDeadline ) {

      // Create a gregorian calendar for calculations
      final GregorianCalendar lGregCal = new GregorianCalendar();

      // Set the calendar's time to the current date
      lGregCal.setTime( aCurrentDt );

      // Check if the current date falls in daylight savings or standard time
      final boolean lCurrDtInDst = this.isDateInDst( aCurrentDt );

      // Add the number of hours until the deadline to the calendar
      lGregCal.add( DATE, aNumDaysUntilDeadline );

      // Check if the deadline date falls in daylight savings or standard time
      final boolean lDeadlineDtInDst = this.isDateInDst( lGregCal.getTime() );

      // If the current and deadline dates are not in the same time (daylight savings/standard),
      // then
      // we must compensate for the difference
      if ( lCurrDtInDst != lDeadlineDtInDst ) {

         // If the deadline date is in DST, then we must subtract an hour so that it matches the
         // current date
         if ( lDeadlineDtInDst ) {

            // Subtract an hour from the deadline date
            lGregCal.add( HOUR, -1 );
         }
         // Otherwise, the deadline date is in standard time, so we must add an hour so that it
         // matches the current date
         else {

            // Add an hour to the deadline date
            lGregCal.add( HOUR, 1 );
         }
      }

      // Return end of previous day
      Date lDeadlineDate = floorMillisecond( DateUtils.getEndOfPrevDay( lGregCal.getTime() ) );

      return lDeadlineDate;
   }


   /**
    * Create a task deadline using the provided data type and interval.
    *
    * @param aTaskKey
    * @param aDataType
    * @param aDeadlineInterval
    *
    * @return the due date of the task based on this deadline
    */
   private Date createDeadlineForTask( TaskKey aTaskKey, DataTypeKey aDataType,
         int aDeadlineInterval, RefSchedFromKey aSchedFrom, Double aDueQuantity, Date aDueDate ) {
      new TaskDeadlineBuilder( aTaskKey ).scheduledFrom( aSchedFrom ).withDataType( aDataType )
            .withInterval( aDeadlineInterval ).withDueDate( aDueDate )
            .withDueQuantity( aDueQuantity ).build();
      return aDueDate;
   }


   /**
    * Create a task deadline using the provided data type and interval.
    *
    * @param aTaskKey
    * @param aDataType
    * @param aDeadlineInterval
    *
    * @return the due date of the task based on this deadline
    */
   private Date createDeadlineForTask( TaskKey aTaskKey, DataTypeKey aDataType,
         int aDeadlineInterval ) {

      // The deadline due quantity and date values are not relevant to the tests but
      // they must be set.
      Date lDueDate = floorMillisecond( DateUtils.addDays( NOW, aDeadlineInterval ) );
      Double lDueQuantity = new Double( aDeadlineInterval );

      new TaskDeadlineBuilder( aTaskKey ).scheduledFrom( RefSchedFromKey.CUSTOM )
            .withDataType( aDataType ).withInterval( aDeadlineInterval ).withDueDate( lDueDate )
            .withDueQuantity( lDueQuantity ).build();

      return lDueDate;
   }


   /**
    * Create a flight for the provided aircraft
    *
    * @param aAircraft
    * @param aDepartureDate
    * @param aArrivalDate
    */
   private FlightLegId createTestFlight( final InventoryKey aAircraft, final Date aDepartureDate,
         final Date aArrivalDate ) {
      return createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( aAircraft );
            aFlight.setScheduledDepartureDate( aDepartureDate );
            aFlight.setScheduledArrivalDate( aArrivalDate );
         }
      } );
   }


   /**
    * Executes the PLSQL package procedure EVENT_PKG.TestUpdateDeadlinesForTask, which is a testing
    * wrapper for EVENT_PKG.UpdateDeadlinesForTask.
    *
    * <pre>
    *      The reason for the wrapper are
    *      a) UpdateDeadlinesForTask is not exposed in the package spec and
    *      b) UpdateDeadlinesForTask requires custom data types that are not supported by MxUnittestDao
    * </pre>
    *
    * @param aAcftKey
    *           aircraft inventory key
    * @param aTaskKey
    *           task key
    */
   private void updateDeadlinesForTask( InventoryKey aAcftKey, TaskKey aTaskKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      if ( aAcftKey == null ) {
         lArgs.addObject( "an_AcftDbId", DataType.INTEGER, null );
         lArgs.addObject( "an_AcftId", DataType.INTEGER, null );
      } else {
         lArgs.add( aAcftKey, "an_AcftDbId", "an_AcftId" );
      }

      lArgs.add( aTaskKey, "an_SchedDbId", "an_SchedId" );
      lArgs.add( "an_Forecast", 1 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "on_Return", DataTypeUtils.INTEGER );

      // We have to use a special version of UpdateDeadlinesForTask() to avoid passing custom data
      // types that are not supported by our DAO.
      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "EVENT_PKG.UpdateDeadlinesForTask", lArgs, lOutArgs );

      int lReturn = lOutArgs.getInteger( "on_Return" );
      if ( lReturn != 1 ) {
         fail( "Error during EVENT_PKG.UpdateDeadlinesForTask(on_Return=" + lReturn + ": "
               + getPlsqlErrors() );
      }
   }


   /**
    * Return a new date that is based on the provided original date that has had its millisecond
    * value set to zero. Note: this method is not provided in DateUtils.
    *
    * @param aOrigDate
    *           original date
    *
    * @return new date
    */
   private Date floorMillisecond( Date aOrigDate ) {
      Calendar lCal = Calendar.getInstance();
      lCal.setTime( aOrigDate );
      lCal.set( Calendar.MILLISECOND, 000 );

      return lCal.getTime();
   }


   /**
    * Retreives the deadline date for the provided task and data type.
    *
    * @param aTaskKey
    * @param aDataType
    *
    * @return deadline date
    */
   private Date getTaskDeadlineDate( TaskKey aTaskKey, DataTypeKey aDataType ) {
      EvtSchedDeadTable lEvtSchedDeadTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( aTaskKey.getEventKey(), aDataType ) );

      return lEvtSchedDeadTable.getDeadlineDate();
   }


   /**
    * Determines if the given date is in standard time or daylight savings time.
    *
    * @param aDate
    *           the date.
    *
    * @return true if the given date is in DST; false otherwise.
    */
   private boolean isDateInDst( Date aDate ) {

      // Create a gregorian calendar for calculations, and set its time to the given date final
      GregorianCalendar lGregCal = new GregorianCalendar();
      lGregCal.setTime( aDate );

      // Get the DST offset from the calendar
      int lDstOffset = lGregCal.get( DST_OFFSET );

      // If the offset is zero, the date is in standard time; otherwise, it's in daylight savings
      // time
      return lDstOffset != 0;
   }


   /**
    * Updates the forecast model for the given aircraft.
    *
    * @param aAircraft
    *           aircraft inventory key
    * @param aFcModel
    *           forecast model key
    */
   private void updateAircraftForecastModel( InventoryKey aAircraft, FcModelKey aFcModel ) {

      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( aFcModel, "forecast_model_db_id", "forecast_model_id" );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aAircraft, "inv_no_db_id", "inv_no_id" );

      MxDataAccess.getInstance().executeUpdate( "inv_ac_reg", lSetArgs, lWhereArgs );
   }


   /*
    * Exception used to indicate a forecast model cannot be generated due to an invalid date range
    * (based on current date).
    */
   private final class UnableToGenerateTestForcastModelException extends RuntimeException {

      private static final long serialVersionUID = -1097983166766611061L;
   }


   /**
    * This method will calculate the difference in days for current and future dates.
    *
    * @param aCurrentDate
    *           - today's date
    * @param aForecastDate
    *           - generated forecasted date
    * @return Number of days
    */
   protected long DateDiffInDays( String aCurrentDate, String aForecastDate ) {
      long diffDays = 0;

      ResultSet ResultSetRecords;

      String Strquery = "SELECT TO_DATE('" + aForecastDate + "', 'YYYY-MM-DD') - TO_DATE('"
            + aCurrentDate + "', 'YYYY-MM-DD') AS DateDiff FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         diffDays = ResultSetRecords.getLong( "DateDiff" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return diffDays + 1;
   }


   /**
    * check date difference between date1 and date2
    *
    * @return: day(s) of date difference
    */
   public long DateDiffInDays( java.util.Date d1, java.util.Date d2 ) {

      long diffDays = 0;

      ResultSet ResultSetRecords;

      String Strquery = "SELECT TO_DATE('" + d2 + "', 'YYYY-MM-DD') - TO_DATE('" + d1
            + "', 'YYYY-MM-DD') AS DateDiff FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         diffDays = ResultSetRecords.getLong( "DateDiff" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return diffDays;

   }


   /**
    * Runs the provided query on the database
    *
    * @param aQuery
    *           Query to be run
    *
    * @return Returns the string query result
    *
    * @throws SQLException
    */
   protected ResultSet runQuery( String aQuery ) throws SQLException {

      System.out.println( "Executing Query: " + aQuery );

      PreparedStatement lStatement = iDatabaseConnectionRule.getConnection().prepareStatement(
            aQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

      ResultSet lResultSet = lStatement.executeQuery( aQuery );

      return lResultSet;
   }


   @Rule
   public TestRule iIgnoreUnableToGenerateForecast = new TestRule() {

      @Override
      public Statement apply( final Statement aBase, Description aDescription ) {
         return new Statement() {

            @Override
            public void evaluate() throws Throwable {
               try {
                  aBase.evaluate();
               } catch ( UnableToGenerateTestForcastModelException e ) {
                  // catch!
               }
            }
         };
      }
   };
}
