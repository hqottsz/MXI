package com.mxi.am.api.resource.sys.refterm.flightlegstatus;

import java.security.Principal;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.flightlegstatus.impl.FlightLegStatusResourceBean;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for FlightLegStatusBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FlightLegStatusResourceBeanTest extends ResourceBeanTest {

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Inject
   FlightLegStatusResourceBean iFlightLegStatusResourceBean;

   private static final String INVALID_FLIGHT_LEG_STATUS_CODE = "INVALID_FLIGHT_LEG_STATUS_CODE";

   private static final String FLIGHT_LEG_STATUS_CODE1 = "MXPLAN";


   @Before
   public void setUp() throws MxException {
      InjectorContainer.get().injectMembers( this );
      iFlightLegStatusResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    *
    * Test FlightLegStatus object returns for a invalid aFlightLegStatusCode failure path
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testFindFlightLegStatusByInvlidCode() throws AmApiResourceNotFoundException {
      iFlightLegStatusResourceBean.get( INVALID_FLIGHT_LEG_STATUS_CODE );
   }

}
