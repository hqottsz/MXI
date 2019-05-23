package com.mxi.am.api.resource.maintenance.plan.flight;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiPreconditionFailException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.provider.serialization.Iso8601DateTime;
import com.mxi.am.api.resource.maintenance.plan.flight.impl.FlightResourceBean;
import com.mxi.am.api.resource.maintenance.plan.flight.model.CapabilityRequirement;
import com.mxi.am.api.resource.maintenance.plan.flight.model.Flight;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Test suite for {@link FlightResourceBean} class.
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FlightResourceBeanTest extends ResourceBeanTest {

   private static final String WRONG_FLIGHT_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String EXCEPTION_TYPE = "FLIGHT";
   private static final String EXCEPTION_MESSAGE_WRONG_ID =
         "FLIGHT FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF not found";
   private static final String EXCEPTION_MESSAGE_NULL = "FLIGHT null not found";
   private static final String EXCEPTION_MESSAGE_INVALID = "FLIGHT Invalid not found";

   private static final String DEPARTURE_ID = "0000B70E120A43FABAA55D7A0B47D919";
   private static final String SCHED_DEPARTURE_START_DATE = "08-21-2018 12:00:00";
   private static final String SCHED_DEPARTURE_END_DATE = "08-23-2018 23:00:00";
   private static final String INVALID_ID = "Invalid";
   private static final String PRECOND_FAIL_MESSAGE = "INVALID_AIRCRAFT_ID";
   private static final String PRECOND_FAIL_PARAM_MESSAGE =
         "Cannot search without at least one parameter";

   private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "MM-dd-yyyy HH:mm:ss" );

   Flight flight1;
   Flight flight2;

   List<Flight> allFlights;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( FlightResource.class ).to( FlightResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   FlightResourceBean iFlightResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @Before
   public void setUp() throws NamingException, MxException, SQLException, ParseException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      iFlightResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      constructFlights();
      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   // Define DataConnectionRule with triggers that need to be altered before/after the execution of
   // tests.
   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            disableTriggers();
         }, () -> {
            enableTriggers();
         } );


   private static void enableTriggers() {
      // Enable trigger after loading data
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_FL_LEG" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_FL_REQUIREMENT" );
   }


   private static void disableTriggers() {
      // Disable trigger before loading data
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_FL_LEG" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_FL_REQUIREMENT" );
   }


   @Test
   @CSIContractTest( Project.SWA_FAULT_STATUS )
   public void get_success() throws AmApiResourceNotFoundException, ParseException {
      Flight lFlight = iFlightResourceBean.get( flight1.getId() );
      assertFlight( flight1, lFlight );
   }


   @Test
   public void get_failure_flightNotFound() throws AmApiResourceNotFoundException, ParseException {
      try {
         iFlightResourceBean.get( WRONG_FLIGHT_ID );
         Assert.fail(
               "Did not throw any exception when searching for a flight that is not in the database" );
      } catch ( AmApiResourceNotFoundException aEx ) {
         Assert.assertEquals( WRONG_FLIGHT_ID, aEx.getId() );
         Assert.assertEquals( EXCEPTION_TYPE, aEx.getIdType() );
         Assert.assertEquals( EXCEPTION_MESSAGE_WRONG_ID, aEx.getMessage() );
      } catch ( Exception aEx ) {
         Assert.fail( "Did not throw AmApiResourceNotFoundException, instead threw: "
               + aEx.getClass().getName() );
      }
   }


   @Test
   public void test_failure_nullId() {
      try {
         iFlightResourceBean.get( null );
         Assert.fail( "Did not throw any exception when searching for a flight with null id" );
      } catch ( AmApiResourceNotFoundException aEx ) {
         Assert.assertEquals( null, aEx.getId() );
         Assert.assertEquals( EXCEPTION_TYPE, aEx.getIdType() );
         Assert.assertEquals( EXCEPTION_MESSAGE_NULL, aEx.getMessage() );
      } catch ( Exception aEx ) {
         Assert.fail( "Did not throw AmApiResourceNotFoundException, instead threw: "
               + aEx.getClass().getName() );
      }
   }


   @Test
   public void test_failure_invalidId() {
      try {
         iFlightResourceBean.get( INVALID_ID );
         Assert.fail( "Did not throw any exception when searching for a flight with invalid id" );
      } catch ( AmApiResourceNotFoundException aEx ) {
         Assert.assertEquals( EXCEPTION_MESSAGE_INVALID, aEx.getMessage() );
      } catch ( Exception aEx ) {
         Assert.fail( "Did not throw AmApiResourceNotFoundException, instead threw: "
               + aEx.getClass().getName() );
      }
   }


   @Test
   public void search_success_byAircraftId() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setAircraftId( flight1.getAircraftId() );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 2, lFlights.size() );
      assertFlights( allFlights, lFlights );
   }


   @Test
   public void search_failure_invalidAircraftId() {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setAircraftId( INVALID_ID );

      try {
         iFlightResourceBean.search( lParam );
         Assert.fail( "Did not throw an exception when searching with an invalid aircraft id" );
      } catch ( AmApiPreconditionFailException aEx ) {
         Assert.assertEquals( PRECOND_FAIL_MESSAGE, aEx.getMessage() );
      } catch ( Exception aEx ) {
         Assert.fail( aEx.getClass().getName() );
      }
   }


   @Test
   public void search_success_byDepartureLocationId() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setDepartLocation( DEPARTURE_ID );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byFlightNumber() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setFlightName( flight1.getFlightNo() );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byActualDepartureDate() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setActualDepartDate( new Iso8601DateTime( flight1.getActualDepartureDate() ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byScheduledDepartureDate() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSchedDepartDate( new Iso8601DateTime( flight1.getSchedDepartureDate() ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byStatus() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setFlightStatus( Arrays.asList( flight1.getStatus() ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byActualArrivalDate() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setActualArrivalDate( new Iso8601DateTime( flight1.getActualArrivalDate() ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byExternalKey() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setExternalKey( flight1.getExtRefKey() );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
      assertFlight( flight1, lFlights.get( 0 ) );
   }


   @Test
   public void search_success_byScheduledDepartureStartDate() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSchedDepartStartDate(
            new Iso8601DateTime( DATE_FORMAT.parse( SCHED_DEPARTURE_START_DATE ) ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 2, lFlights.size() );
      assertFlights( allFlights, lFlights );
   }


   @Test
   public void search_success_byScheduledDepartureEndDate() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSchedDepartEndDate(
            new Iso8601DateTime( DATE_FORMAT.parse( SCHED_DEPARTURE_END_DATE ) ) );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 2, lFlights.size() );
      assertFlights( allFlights, lFlights );
   }


   /**
    *
    * If only the Page is specified, the size is set too 9999 and the page is set to 1
    *
    * @throws ParseException
    */
   @Test
   public void search_success_testPageAllFlights() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setPage( 1 );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 2, lFlights.size() );
      assertFlights( allFlights, lFlights );
   }


   /**
    *
    * If only Size is specified, the size is set too 9999 and the page is set to 1
    *
    * @throws ParseException
    */
   @Test
   public void search_success_testSizeAllFlights() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSize( 10000 );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 2, lFlights.size() );
      assertFlights( allFlights, lFlights );
   }


   @Test
   public void search_success_bySizeAndSpecificPage() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSize( 10000 );
      lParam.setPage( 2 );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 0, lFlights.size() );
   }


   @Test
   public void search_success_bySizeAndPageWithSizeLimit() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setSize( 1 );
      lParam.setPage( 1 );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 1, lFlights.size() );
   }


   @Test
   public void search_success_allNullParams() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();

      try {
         iFlightResourceBean.search( lParam );
         Assert.fail( "Did not throw an Exception when all parameters are null" );
      } catch ( AmApiPreconditionFailException aEx ) {
         Assert.assertEquals( PRECOND_FAIL_PARAM_MESSAGE, aEx.getMessage() );
      } catch ( Exception aEx ) {
         Assert.fail( "Did not throw an AmApiPreconditionFailException, instead threw: "
               + aEx.getClass().getName() );
      }
   }


   @Test
   public void search_success_byAircraftIdNoFlightsFound() throws ParseException {
      FlightSearchParameters lParam = new FlightSearchParameters();
      lParam.setAircraftId( WRONG_FLIGHT_ID );

      List<Flight> lFlights = iFlightResourceBean.search( lParam );
      Assert.assertEquals( 0, lFlights.size() );
   }


   @Test
   @CSIContractTest( Project.SWA_FAULT_STATUS )
   public void search_success_completedFlightsByAircraftAndActualArrivalDate()
         throws ParseException {
      FlightSearchParameters param = new FlightSearchParameters();
      param.setAircraftId( flight1.getAircraftId() );
      param.setActualArrivalDate( new Iso8601DateTime( flight1.getActualArrivalDate() ) );
      param.setFlightStatus( Arrays.asList( "MXCMPLT" ) );

      List<Flight> flights = iFlightResourceBean.search( param );

      assertEquals( "Unexpected number of flights returned: ", 1, flights.size() );

      assertFlight( flight1, flights.get( 0 ) );
   }


   private void assertFlights( List<Flight> expectedFlightList, List<Flight> actualFlightList )
         throws ParseException {
      assertEquals( "Incorrect number of flights returned: ", expectedFlightList.size(),
            actualFlightList.size() );

      Flight[] expectedFlights =
            expectedFlightList.toArray( new Flight[expectedFlightList.size()] );
      Flight[] actualFlights = actualFlightList.toArray( new Flight[actualFlightList.size()] );

      Arrays.sort( expectedFlights, Comparator.comparing( Flight::getId ) );
      Arrays.sort( actualFlights, Comparator.comparing( Flight::getId ) );

      for ( int i = 0; i < expectedFlights.length; i++ ) {
         assertFlight( expectedFlights[i], actualFlights[i] );
      }
   }


   private void assertFlight( Flight expectedFlight, Flight actualFlight ) throws ParseException {
      Assert.assertEquals( "Incorrect id of returned flight: ", expectedFlight.getId(),
            actualFlight.getId() );

      Assert.assertEquals( "Incorrect aircraft id in returned flight: ",
            expectedFlight.getAircraftId(), actualFlight.getAircraftId() );
      Assert.assertEquals( "Incorrect actual arrival date in returned flight: ",
            expectedFlight.getActualArrivalDate(), actualFlight.getActualArrivalDate() );
      Assert.assertEquals( "Incorrect actual departure date in returned flight: ",
            expectedFlight.getActualDepartureDate(), actualFlight.getActualDepartureDate() );
      Assert.assertEquals( "Incorrect arrival airport code in returned flight: ",
            expectedFlight.getArrivalAirportCode(), actualFlight.getArrivalAirportCode() );
      Assert.assertEquals( "Incorrect arrival airport name in returned flight: ",
            expectedFlight.getArrivalAirportName(), actualFlight.getArrivalAirportName() );
      Assert.assertEquals( "Incorrect arrival gate in returned flight: ",
            expectedFlight.getArrivalGate(), actualFlight.getArrivalGate() );
      Assert.assertEquals( "Incorrect departure airport code in returned flight: ",
            expectedFlight.getDepartureAirportCode(), actualFlight.getDepartureAirportCode() );
      Assert.assertEquals( "Incorrect departure airport name in returned flight: ",
            expectedFlight.getDepartureAirportName(), actualFlight.getDepartureAirportName() );
      Assert.assertEquals( "Incorrect departure gate in returned flight: ",
            expectedFlight.getDepartureGate(), actualFlight.getDepartureGate() );
      Assert.assertEquals( "Incorrect external reference key in returned flight: ",
            expectedFlight.getExtRefKey(), actualFlight.getExtRefKey() );
      Assert.assertEquals( "Incorrect flight description in returned flight: ",
            expectedFlight.getFlightDescription(), actualFlight.getFlightDescription() );
      Assert.assertEquals( "Incorrect flight number in returned flight: ",
            expectedFlight.getFlightNo(), actualFlight.getFlightNo() );
      Assert.assertEquals( "Incorrect master flight number in returned flight: ",
            expectedFlight.getMasterFlightNo(), actualFlight.getMasterFlightNo() );
      Assert.assertEquals( "Incorrect off date in returned flight: ", expectedFlight.getOffDate(),
            actualFlight.getOffDate() );
      Assert.assertEquals( "Incorrect on date in returned flight: ", expectedFlight.getOnDate(),
            actualFlight.getOnDate() );
      Assert.assertEquals( "Incorrect scheduled arrival date in returned flight: ",
            expectedFlight.getSchedArrivalDate(), actualFlight.getSchedArrivalDate() );
      Assert.assertEquals( "Incorrect scheduled departure date in returned flight: ",
            expectedFlight.getSchedDepartureDate(), actualFlight.getSchedDepartureDate() );
      Assert.assertEquals( "Incorrect status in returned flight: ", expectedFlight.getStatus(),
            actualFlight.getStatus() );
      Assert.assertEquals( "Incorrect type in returned flight: ", expectedFlight.getType(),
            actualFlight.getType() );
      Assert.assertEquals( "Incorrect last modified date in returned flight: ",
            expectedFlight.getLastModifiedDate(), actualFlight.getLastModifiedDate() );
      Assert.assertEquals( "Incorrect reason code in returned flight: ",
            expectedFlight.getReasonCode(), actualFlight.getReasonCode() );

      // assert capability requirements
      Assert.assertEquals( "Incorrect number of capability requirements in flight: ",
            expectedFlight.getCapabilityRequirements().size(),
            actualFlight.getCapabilityRequirements().size() );

      CapabilityRequirement[] expectedCapReqs = expectedFlight.getCapabilityRequirements().toArray(
            new CapabilityRequirement[expectedFlight.getCapabilityRequirements().size()] );
      CapabilityRequirement[] actualCapReqs = actualFlight.getCapabilityRequirements()
            .toArray( new CapabilityRequirement[actualFlight.getCapabilityRequirements().size()] );

      Arrays.sort( expectedCapReqs, Comparator.comparing( CapabilityRequirement::getCode ) );
      Arrays.sort( actualCapReqs, Comparator.comparing( CapabilityRequirement::getCode ) );

      for ( int i = 0; i < expectedCapReqs.length; i++ ) {
         assertCapabilityRequirement( i, expectedFlight.getId(), expectedCapReqs[i],
               actualCapReqs[i] );
      }

   }


   private void assertCapabilityRequirement( int index, String flightId,
         CapabilityRequirement expected, CapabilityRequirement actual ) {
      assertEquals( String.format( "Incorrect code in capability requirement #%d of flight [%s]",
            index, flightId ), expected.getCode(), actual.getCode() );
      assertEquals(
            String.format( "Incorrect description in capability requirement #%d of flight [%s]",
                  index, flightId ),
            expected.getDescription(), actual.getDescription() );
      assertEquals(
            String.format( "Incorrect level code in capability requirement #%d of flight [%s]",
                  index, flightId ),
            expected.getLevelCode(), actual.getLevelCode() );
      assertEquals( String.format(
            "Incorrect level description in capability requirement #%d of flight [%s]", index,
            flightId ), expected.getLevelDescription(), actual.getLevelDescription() );
   }


   private void constructFlights() throws ParseException {
      flight1 = new Flight();
      flight1.setId( "9CF19AFAFAF64993BB342C812C4E5EFC" );
      flight1.setAircraftId( "914BFA565BCF4FBFBD9364DB30D88BB3" );
      flight1.setActualArrivalDate( DATE_FORMAT.parse( "08-22-2018 22:00:00" ) );
      flight1.setActualDepartureDate( DATE_FORMAT.parse( "08-22-2018 17:30:00" ) );
      flight1.setArrivalAirportCode( "YVR" );
      flight1.setArrivalAirportName( "Vancouver" );
      flight1.setArrivalGate( "A42" );
      flight1.setDepartureAirportCode( "YOW" );
      flight1.setDepartureAirportName( "Ottawa" );
      flight1.setDepartureGate( "C19" );
      flight1.setExtRefKey( "0923MCOYUL20000923" );
      flight1.setFlightDescription( "Test Flight 1" );
      flight1.setFlightNo( "0923" );
      flight1.setMasterFlightNo( "0923" );
      flight1.setOffDate( DATE_FORMAT.parse( "08-22-2018 16:45:00" ) );
      flight1.setOnDate( DATE_FORMAT.parse( "08-22-2018 22:15:00" ) );
      flight1.setSchedArrivalDate( DATE_FORMAT.parse( "08-22-2018 21:30:00" ) );
      flight1.setSchedDepartureDate( DATE_FORMAT.parse( "08-22-2018 16:00:00" ) );
      flight1.setStatus( "MXCMPLT" );
      flight1.setType( "Test" );
      flight1.setReasonCode( "FLVALID" );
      flight1.setLastModifiedDate( DATE_FORMAT.parse( "08-28-2018 21:02:44" ) );
      CapabilityRequirement capReq1 = new CapabilityRequirement();
      capReq1.setCode( "TEST" );
      capReq1.setDescription( "test cap" );
      capReq1.setLevelCode( "test" );
      capReq1.setLevelDescription( "test level" );
      List<CapabilityRequirement> capReqs1 = new ArrayList<>();
      capReqs1.add( capReq1 );
      flight1.setCapabilityRequirements( capReqs1 );

      flight2 = new Flight();
      flight2.setId( "9CF19AFAFAF64993BB342C834C4E5999" );
      flight2.setAircraftId( "914BFA565BCF4FBFBD9364DB30D88BB3" );
      flight2.setActualArrivalDate( DATE_FORMAT.parse( "08-23-2018 22:00:00" ) );
      flight2.setActualDepartureDate( DATE_FORMAT.parse( "08-23-2018 17:30:00" ) );
      flight2.setArrivalAirportCode( "YOW" );
      flight2.setArrivalAirportName( "Ottawa" );
      flight2.setArrivalGate( "G67" );
      flight2.setDepartureAirportCode( "YVR" );
      flight2.setDepartureAirportName( "Vancouver" );
      flight2.setDepartureGate( "A34" );
      flight2.setExtRefKey( "0923MCOYUL20009999" );
      flight2.setFlightDescription( "Test Flight 2" );
      flight2.setFlightNo( "0999" );
      flight2.setMasterFlightNo( "0999" );
      flight2.setOffDate( DATE_FORMAT.parse( "08-23-2018 16:45:00" ) );
      flight2.setOnDate( DATE_FORMAT.parse( "08-23-2018 22:15:00" ) );
      flight2.setSchedArrivalDate( DATE_FORMAT.parse( "08-23-2018 21:30:00" ) );
      flight2.setSchedDepartureDate( DATE_FORMAT.parse( "08-23-2018 16:00:00" ) );
      flight2.setStatus( "ACTV" );
      flight2.setType( "Test2" );
      flight2.setLastModifiedDate( DATE_FORMAT.parse( "09-05-2018 21:02:44" ) );
      flight2.setCapabilityRequirements( new ArrayList<CapabilityRequirement>() );

      allFlights = new ArrayList<>();
      allFlights.add( flight1 );
      allFlights.add( flight2 );

   }
}
