package com.mxi.mx.core.ejb.stask.staskbean;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createLocation;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.RefLocTypeKey.AIRPORT;
import static com.mxi.mx.core.key.RefLocTypeKey.DOCK;
import static com.mxi.mx.core.key.RefLocTypeKey.LINE;
import static com.mxi.mx.core.key.RefLocTypeKey.SRVSTG;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.internationalization.Localizable;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.exception.StartDateAfterEndDateException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.status.InvalidScheduledLocationException;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;
import com.mxi.mx.core.services.transfer.LocationNotExistsException;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;


/**
 * Integration unit test for scheduling a work package to an internal location.
 *
 * Exercising:
 * {@linkplain STaskBean#schedule( TaskKey aCheck, ScheduleInternalTO aTO, HumanResourceKey aAuthorizingHr )}
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class ScheduleInternallyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public final ExpectedException expectedException = ExpectedException.none();

   private static final String LOCATION_CODE = "LOCATION_CODE";
   private static final RefLocTypeKey LOCATION_TYPE_KEY = RefLocTypeKey.LINE;

   // Scheduling times are stored with a granularity of seconds.
   private static final Date VALID_SCHEDULED_START_DATE = floorSecond( addDays( new Date(), 1 ) );
   private static final Date VALID_SCHEDULED_END_DATE =
         floorSecond( addDays( VALID_SCHEDULED_START_DATE, 1 ) );

   private static final String ERROR_MESSAGE_CODE_FOR_INVALID_LOCATION = "30181";

   // Refer to StartDateAfterEndDateException for the hard coded error message code.
   private static final String ERROR_MESSAGE_CODE_FOR_END_TIME_BEFORE_START_TIME = "30241";

   // Refer to LocationNotExistsException for the hard coded error message code.
   private static final String ERROR_MESSAGE_CODE_FOR_NON_EXISTING_LOCATION = "30078";

   // Constants for attributes that are required but not applicable to the tests.
   private static final Date NA_SCHEDULED_START_DATE = new Date();
   private static final Date NA_SCHEDULED_END_DATE = new Date();
   private static final String NA_WORK_ORDER_NO = "";

   private STaskBean staskBean;
   private HumanResourceKey schedulingUser;


   @Before
   public void before() {
      // Create a bean to test against.
      //
      // And set its session context to a fake so that errors will not masked by a NPE due to the
      // session context not being set.
      staskBean = new STaskBean();
      staskBean.setSessionContext( new SessionContextFake() );

      // Create a user to test with.
      schedulingUser = Domain.createHumanResource();
      StringBundles.setSingleton( mock( Localizable.class ) );
   }


   /**
    * Verify that a work package can be scheduled to an internal location when that location allows
    * work packages to be scheduled to it (1) and all other mandatory, valid information is
    * provided.
    *
    * Note (1): a location is deemed to "allow work packages to be scheduled to it" when that
    * location has its supply location set to a location whose location type matches one of the
    * types listed in the value of the SCHEDULE_LOCATIONS_CHECK global configuration parameter.
    *
    */
   @Test
   public void schedulesWpToValidLocation() throws Exception {

      // Given a location to which a work package may be scheduled.
      LocationKey location = createWpSchedulableLocation( LOCATION_CODE );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled to the location.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work location of the work package is set to the location.
      assertThat( "Unexpected work package location.", getLocationOfWorkPackage( workPackage ),
            is( location ) );
   }


   /**
    * Verify that a work package can be scheduled to an internal location, using a case-insensitive
    * location code, when that location allows work packages to be scheduled to it (1) and all other
    * mandatory, valid information is provided.
    *
    * Note (1): a location is deemed to "allow work packages to be scheduled to it" when that
    * location has its supply location set to a location whose location type matches one of the
    * types listed in the value of the SCHEDULE_LOCATIONS_CHECK global configuration parameter.
    *
    */
   @Test
   public void schedulesWpToValidLocationUsingCaseInsensitiveCode() throws Exception {

      // Given a location to which a work package may be scheduled.
      String locationCode = "LOCATION_CODE";
      LocationKey location = createWpSchedulableLocation( locationCode );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled to the location using a code that does not match the
      // case of the location's code.
      // (other mandatory info needs to be provided and valid)
      String caseInsensitiveCode = "LoCaTiOn_CoDe";
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( caseInsensitiveCode );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work location of the work package is set to the location.
      assertThat( "Unexpected work package location.", getLocationOfWorkPackage( workPackage ),
            is( location ) );
   }


   /**
    * Verify that a work package cannot be scheduled to an internal location when the system is
    * configured to not have any locations types allow scheduling.
    *
    * Location types listed in the value of the SCHEDULE_LOCATIONS_CHECK global configuration
    * parameter allow scheduling.
    *
    */
   @Test
   public void doesNotSchedulesWpToLocationWhenSystemConfiguredWithNoValidLocationTypes()
         throws Exception {

      // Given Mx has no location types configured to allow scheduling.
      GlobalParameters globalParmsFake = new GlobalParametersFake( "LOGIC" );
      globalParmsFake.setString( "SCHEDULE_LOCATIONS_CHECK", "" );
      GlobalParameters.setInstance( globalParmsFake );

      // Parent Airport Location that is set up as a supply location
      LocationKey airportLocation = createLocation( airportLoc -> {
         airportLoc.setType( AIRPORT );
         airportLoc.setIsSupplyLocation( true );
      } );

      // Given a location that would normally be valid if its location type allowed scheduling.
      createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.setType( LOCATION_TYPE_KEY );
         loc.setParent( airportLocation );
         loc.setSupplyLocation( airportLocation );
      } );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // Set up the expected exception.
      expectedException.expect( InvalidScheduledLocationException.class );
      expectedException.expectMessage( containsString( ERROR_MESSAGE_CODE_FOR_INVALID_LOCATION ) );

      // When the work package is scheduled to the location.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work package is prevented from being scheduled
      // and a meaningful message is presented to the user.
      //
      // See "expected exception"
   }


   /**
    * Verify that a work package cannot be scheduled to a location using an invalid location code
    * (the location does not exist).
    *
    */
   @Test
   public void doesNotSchedulesWpToLocationWhenNoLocationsExistForLocationCode() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // Set up the expected exception.
      expectedException.expect( LocationNotExistsException.class );
      expectedException
            .expectMessage( containsString( ERROR_MESSAGE_CODE_FOR_NON_EXISTING_LOCATION ) );

      // When the work package is scheduled to the location using a location code for a location
      // that does not exist.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work package is prevented from being scheduled
      // and a meaningful message is presented to the user.
      //
      // See "expected exception"
   }


   /**
    * Verify that a work package cannot be scheduled to a location that does not have a supply
    * location.
    *
    */
   @Test
   public void doesNotSchedulesWpToLocationWithoutASupplyLocation() throws Exception {

      // Given a location type is configured to allow scheduling.
      GlobalParameters globalParmsFake = new GlobalParametersFake( "LOGIC" );
      globalParmsFake.setString( "SCHEDULE_LOCATIONS_CHECK", LOCATION_TYPE_KEY.getCd() );
      GlobalParameters.setInstance( globalParmsFake );

      // Given a location that would normally be valid but has no supply location.
      createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.setType( LOCATION_TYPE_KEY );
         loc.setSupplyLocation( null );
      } );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // Set up the expected exception.
      expectedException.expect( InvalidScheduledLocationException.class );
      expectedException.expectMessage( containsString( ERROR_MESSAGE_CODE_FOR_INVALID_LOCATION ) );

      // When the work package is scheduled to the location.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work package is prevented from being scheduled
      // and a meaningful message is presented to the user.
   }


   /**
    * Verify that a work package cannot be scheduled to a location whose location type is not
    * configured to allow scheduling and whose supply location has no other locations that allow
    * scheduling (also the supply location does not allow scheduling).
    */
   @Test
   public void doesNotScheduleWpToInvalidLocationWhoseSupplyLocationHasNoOtherValidLocations()
         throws Exception {

      // Given Mx has a location type configured to allow scheduling.
      GlobalParameters globalParmsFake = new GlobalParametersFake( "LOGIC" );
      globalParmsFake.setString( "SCHEDULE_LOCATIONS_CHECK", LINE.getCd() );
      GlobalParameters.setInstance( globalParmsFake );

      // Given a supply location that has a non-scheduling location type.
      LocationKey supplyLocation = createLocation( supplyLoc -> {
         supplyLoc.setIsSupplyLocation( true );
         supplyLoc.setType( AIRPORT );
      } );

      // Give a location that has a non-scheduling location type and uses the supply location.
      createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.setType( DOCK );
         loc.setSupplyLocation( supplyLocation );
      } );

      // Give another location that also has a non-scheduling location type and uses the supply
      // location.
      createLocation( anotherLoc -> {
         anotherLoc.setType( DOCK );
         anotherLoc.setSupplyLocation( supplyLocation );
      } );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // Set up the expected exception.
      expectedException.expect( InvalidScheduledLocationException.class );
      expectedException.expectMessage( containsString( ERROR_MESSAGE_CODE_FOR_INVALID_LOCATION ) );

      // When the work package is scheduled to the location.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work package is prevented from being scheduled
      // and a meaningful message is presented to the user.
      //
      // See "expected exception"
   }


   /**
    * Verify that a work package can be scheduled to a location whose location type is not
    * configured to allow scheduling but whose supply location has another location that allows
    * scheduling. HOWEVER, the location will be changed to the valid other location.
    *
    */
   @Test
   public void schedulesWpToInvalidLocationWhoseSupplyLocationHasAnotherValidLocation()
         throws Exception {

      // Given Mx has a location type configured to allow scheduling.
      GlobalParameters globalParmsFake = new GlobalParametersFake( "LOGIC" );
      globalParmsFake.setString( "SCHEDULE_LOCATIONS_CHECK", LINE.getCd() );
      GlobalParameters.setInstance( globalParmsFake );

      // Given a supply airport location that has a non-scheduling location type.
      LocationKey airportLocation = createLocation( supplyLoc -> {
         supplyLoc.setIsSupplyLocation( true );
         supplyLoc.setType( AIRPORT );
      } );

      // Serviceable Staging SRVSTG Location (Parent to Dock location below)
      LocationKey serviceableStagingLocation = createLocation( loc -> {
         loc.setCode( "SRVSTG" );
         loc.setType( SRVSTG );
         loc.setParent( airportLocation );
      } );

      // Give a location that has a non-scheduling location type and uses the supply location.
      createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.setType( DOCK );
         loc.setParent( serviceableStagingLocation );
         loc.setSupplyLocation( airportLocation );
      } );

      // Give another location that HAS a scheduling location type and uses the supply location.
      LocationKey anotherLocation = createLocation( anotherLoc -> {
         anotherLoc.setType( LINE );
         anotherLoc.setSupplyLocation( airportLocation );
      } );

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled to the location.
      // (other mandatory info needs to be provided and valid)
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setWorkLocationCd( LOCATION_CODE );
      transferObject.setSchedDates( NA_SCHEDULED_START_DATE, NA_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work location of the work package is set to the other valid location.
      assertThat( "Unexpected work package location.", getLocationOfWorkPackage( workPackage ),
            is( anotherLocation ) );
   }


   /**
    * Verify that a work package can be scheduled when no location is provided, as long as both a
    * valid start and end time are provided.
    *
    */
   @Test
   public void schedulesWpWithValidStartAndEndTimesButNoLocation() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled with a valid start and end time
      // and no location is provided.
      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setSchedDates( VALID_SCHEDULED_START_DATE, VALID_SCHEDULED_END_DATE );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the scheduled start and scheduled end times of the work package are set to those
      // times, respectively.
      assertThat( "Unexpected scheduled start date of work package.",
            getScheduledStartOfWorkPackage( workPackage ), is( VALID_SCHEDULED_START_DATE ) );
      assertThat( "Unexpected scheduled end date of work package.",
            getScheduledEndOfWorkPackage( workPackage ), is( VALID_SCHEDULED_END_DATE ) );
   }


   /**
    * Verify that a work package can be scheduled with both the scheduled start and end times in the
    * past (and start prior to end).
    *
    */
   @Test
   public void schedulesWpWithBothStartAndEndTimesInThePast() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled with both a start and end time in the past (start is
      // prior to end).
      // Note, scheduling times are stored with a granularity of seconds.
      Date endDate = floorSecond( addDays( new Date(), -1 ) );
      Date startDate = floorSecond( addDays( endDate, -1 ) );

      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setSchedDates( startDate, endDate );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the scheduled start and scheduled end times of the work package are set to those
      // times, respectively.
      assertThat( "Unexpected scheduled start date of work package.",
            getScheduledStartOfWorkPackage( workPackage ), is( startDate ) );
      assertThat( "Unexpected scheduled end date of work package.",
            getScheduledEndOfWorkPackage( workPackage ), is( endDate ) );
   }


   /**
    * Verify that a work package can be scheduled with both the scheduled start and end times in the
    * future (and start prior to end).
    *
    */
   @Test
   public void schedulesWpWithBothStartAndEndTimesInTheFuture() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled with both a start and end time in the future (start is
      // prior to end).
      // Note, scheduling times are stored with a granularity of seconds.
      Date startDate = floorSecond( addDays( new Date(), 1 ) );
      Date endDate = floorSecond( addDays( startDate, 1 ) );

      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setSchedDates( startDate, endDate );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the scheduled start and scheduled end times of the work package are set to those
      // times, respectively.
      assertThat( "Unexpected scheduled start date of work package.",
            getScheduledStartOfWorkPackage( workPackage ), is( startDate ) );
      assertThat( "Unexpected scheduled end date of work package.",
            getScheduledEndOfWorkPackage( workPackage ), is( endDate ) );
   }


   /**
    * Verify that a work package can be scheduled with the scheduled start time in the past and end
    * time in the future.
    *
    */
   @Test
   public void schedulesWpWithStartTimeInThePastAndEndTimeInTheFuture() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // When the work package is scheduled with a start time in the past and an end time in the
      // future.
      // Note, scheduling times are stored with a granularity of seconds.
      Date startDate = floorSecond( addDays( new Date(), -1 ) );
      Date endDate = floorSecond( addDays( new Date(), 1 ) );

      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setSchedDates( startDate, endDate );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the scheduled start and scheduled end times of the work package are set to those
      // times, respectively.
      assertThat( "Unexpected scheduled start date of work package.",
            getScheduledStartOfWorkPackage( workPackage ), is( startDate ) );
      assertThat( "Unexpected scheduled end date of work package.",
            getScheduledEndOfWorkPackage( workPackage ), is( endDate ) );
   }


   /**
    * Verify that a work package cannot be scheduled with the scheduled end time is before the
    * scheduled start time.
    *
    */
   @Test
   public void doesNotscheduleWpWithEndTimeBeforeTheStartTime() throws Exception {

      // Given an unscheduled work package against an aircraft.
      TaskKey workPackage = createUnscheduledWorkPackage();

      // Set up the expected exception.
      expectedException.expect( StartDateAfterEndDateException.class );
      expectedException
            .expectMessage( containsString( ERROR_MESSAGE_CODE_FOR_END_TIME_BEFORE_START_TIME ) );

      // When the work package is scheduled with a end time before the start time.
      // Note, scheduling times are stored with a granularity of seconds.
      Date startDate = floorSecond( new Date() );
      Date endDate = floorSecond( addDays( startDate, -1 ) );

      ScheduleInternalTO transferObject = new ScheduleInternalTO();
      transferObject.setSchedDates( startDate, endDate );
      transferObject.setWorkOrderNo( NA_WORK_ORDER_NO );

      staskBean.schedule( workPackage, transferObject, schedulingUser );

      // Then the work package is prevented from being scheduled
      // and a meaningful message is presented to the user.
      //
      // See "expected exception"
   }


   private TaskKey createUnscheduledWorkPackage() {
      return createWorkPackage( wp -> {
         wp.setAircraft( createAircraft() );
         wp.setScheduledStartDate( null );
         wp.setScheduledEndDate( null );
      } );
   }


   private LocationKey createWpSchedulableLocation( String locationCode ) {
      // In order for a work package to be scheduled to a location, that location must have a supply
      // location and have a location type code is listed in the global configuration parameter
      // SCHEDULE_LOCATIONS_CHECK.
      // There are other ways to have a valid location but this is the simplest.
      //
      // Refer to InvalidScheduledLocationExceptionForCheck.qrx

      GlobalParameters globalParmsFake = new GlobalParametersFake( "LOGIC" );
      globalParmsFake.setString( "SCHEDULE_LOCATIONS_CHECK", LOCATION_TYPE_KEY.getCd() );
      GlobalParameters.setInstance( globalParmsFake );

      LocationKey airportLocation = createLocation( supplyLoc -> {
         supplyLoc.setType( AIRPORT );
         supplyLoc.setIsSupplyLocation( true );
      } );

      return createLocation( loc -> {
         loc.setCode( locationCode );
         loc.setType( LOCATION_TYPE_KEY );
         loc.setParent( airportLocation );
         loc.setSupplyLocation( airportLocation );
      } );
   }


   private LocationKey getLocationOfWorkPackage( TaskKey workPackage ) {
      DataSetArgument args = new DataSetArgument();
      args.add( workPackage.getEventKey(), "event_db_id", "event_id" );

      QuerySet querySet = QuerySetFactory.getInstance().executeQueryTable( "evt_loc", args );
      assertThat( "Unexpected number of rows in evt_loc for the work package.",
            querySet.getRowCount(), is( 1 ) );
      querySet.next();
      return querySet.getKey( LocationKey.class, "loc_db_id", "loc_id" );
   }


   private EvtEventTable getEvtEventRowOfWorkPackage( TaskKey workPackage ) {
      EvtEventDao dao = InjectorContainer.get().getInstance( EvtEventDao.class );
      return dao.findByPrimaryKey( workPackage.getEventKey() );
   }


   private Date getScheduledStartOfWorkPackage( TaskKey workPackage ) {
      return getEvtEventRowOfWorkPackage( workPackage ).getSchedStartDt();
   }


   private Date getScheduledEndOfWorkPackage( TaskKey workPackage ) {
      return getEvtEventRowOfWorkPackage( workPackage ).getSchedEndDt();
   }

}
