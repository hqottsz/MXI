package com.mxi.am.api.resource.sys.refterm.flightreason;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.flightreason.impl.FlightReasonResourceBean;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for FlightReasonResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FlightReasonResourceBeanTest extends ResourceBeanTest {

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Inject
   FlightReasonResourceBean flightReasonResourceBean;

   private static final String INVALID_FLIGHT_REASON_CODE = "INVALID_FLIGHT_REASON_CODE";

   private static final String FLIGHT_REASON_CODE_1 = "FLTEST1";
   private static final String DISPLAY_CODE_1 = "TEST1";
   private static final String DISPLAY_NAME_1 = "Test 1 FL event reason";

   private static final String FLIGHT_REASON_CODE_2 = "FLTEST2";
   private static final String DISPLAY_CODE_2 = "TEST2";
   private static final String DISPLAY_NAME_2 = "Test 2 FL event reason";

   private static final String DISPLAY_DESCRIPTION = "Flight reason testing";

   private FlightReason flightReason1;
   private FlightReason flightReason2;


   @Before
   public void setUp() throws MxException {
      InjectorContainer.get().injectMembers( this );
      flightReasonResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      buildFlightReasonObjects();
   }


   /**
    *
    * Test GET FlightReason
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testGetFlightReasonByCode() throws AmApiResourceNotFoundException {

      FlightReason actualFlightReason =
            flightReasonResourceBean.get( flightReason1.getReasonCode() );

      Assert.assertEquals( actualFlightReason, flightReason1 );
   }


   /**
    *
    * Test FlightReason object returns for a invalid flightReasonCode failure path
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetFlightReasonInvalidCode() throws AmApiResourceNotFoundException {
      flightReasonResourceBean.get( INVALID_FLIGHT_REASON_CODE );
   }


   /**
    *
    * Test Search FlightReason
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchFlightReason() throws AmApiResourceNotFoundException {

      List<FlightReason> actualFlightReasons = flightReasonResourceBean.search();

      Assert.assertTrue(
            "Flight reason with code " + flightReason1.getReasonCode() + " was not in the list",
            actualFlightReasons.contains( flightReason1 ) );
      Assert.assertTrue(
            "Flight reason with code " + flightReason2.getReasonCode() + " was not in the list",
            actualFlightReasons.contains( flightReason2 ) );
      Assert.assertEquals( "Flight reason list size was not as expected", 2,
            actualFlightReasons.size() );
   }


   private void buildFlightReasonObjects() {
      flightReason1 = new FlightReason();
      flightReason1.setReasonCode( FLIGHT_REASON_CODE_1 );
      flightReason1.setDisplayCode( DISPLAY_CODE_1 );
      flightReason1.setDisplayName( DISPLAY_NAME_1 );
      flightReason1.setDisplayDescription( DISPLAY_DESCRIPTION );

      flightReason2 = new FlightReason();
      flightReason2.setReasonCode( FLIGHT_REASON_CODE_2 );
      flightReason2.setDisplayCode( DISPLAY_CODE_2 );
      flightReason2.setDisplayName( DISPLAY_NAME_2 );
      flightReason2.setDisplayDescription( DISPLAY_DESCRIPTION );
   }
}
