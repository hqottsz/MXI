package com.mxi.am.api.resource.materials.asset.aircraft.engines;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.asset.aircraft.engines.impl.EnginesResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database-level test class for the Aircraft Engines API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class EnginesResourceBeanTest extends ResourceBeanTest {

   @Inject
   EnginesResourceBean enginesResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException exception = ExpectedException.none();

   private static final String NONEXISTENT_AC_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private AircraftEngines acEngines1 = new AircraftEngines();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      enginesResourceBean.setEJBContext( ejbContext );
      constructExpectedResults();
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), EnginesResourceBeanTest.class );
      initializeSecurityContext();
   }


   @Test
   @CSIContractTest( Project.SWA_AC_CAPABILITIES )
   public void get_success() throws AmApiResourceNotFoundException {
      AircraftEngines engines = enginesResourceBean.get( acEngines1.getAircraftId() );

      assertEquals( "Returned aircraft id of the engines is incorrect: ",
            acEngines1.getAircraftId(), engines.getAircraftId() );

      assertAircraftEngineList( acEngines1.getEngines(), engines.getEngines() );
   }


   @Test
   public void get_failure_noAircraftFound() throws AmApiResourceNotFoundException {
      exception.expect( AmApiResourceNotFoundException.class );
      exception.expectMessage( String.format( "AIRCRAFT %s not found", NONEXISTENT_AC_ID ) );

      enginesResourceBean.get( NONEXISTENT_AC_ID );
   }


   private void assertAircraftEngineList( List<AircraftEngine> expectedEngineList,
         List<AircraftEngine> actualEngineList ) {
      assertEquals( "Number of engines returned is incorrect: ", expectedEngineList.size(),
            actualEngineList.size() );

      AircraftEngine[] expectedEngines =
            expectedEngineList.toArray( new AircraftEngine[expectedEngineList.size()] );
      AircraftEngine[] actualEngines =
            actualEngineList.toArray( new AircraftEngine[actualEngineList.size()] );

      Arrays.sort( expectedEngines, Comparator.comparing( AircraftEngine::getInventoryId ) );
      Arrays.sort( actualEngines, Comparator.comparing( AircraftEngine::getInventoryId ) );

      for ( int i = 0; i < expectedEngines.length; i++ ) {
         assertAircraftEngine( expectedEngines[i], actualEngines[i] );
      }

   }


   private void assertAircraftEngine( AircraftEngine expected, AircraftEngine actual ) {
      assertEquals( "Returned engine inventory id is incorrect: ", expected.getInventoryId(),
            actual.getInventoryId() );
      assertEquals( "Returned engine serial number is incorrect: ", expected.getOEMSerialNumber(),
            actual.getOEMSerialNumber() );
      assertEquals( "Returned engine part number is incorrect: ", expected.getPartNumber(),
            actual.getPartNumber() );
      assertEquals( "Returned engine assembly position is incorrect: ",
            expected.getAssemblyPosition(), actual.getAssemblyPosition() );
   }


   private void constructExpectedResults() {
      List<AircraftEngine> acEngineList1 = new ArrayList<>();
      AircraftEngine acEngine1 = new AircraftEngine();
      acEngine1.setAssemblyPosition( "1" );
      acEngine1.setInventoryId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA101" );
      acEngine1.setOEMSerialNumber( "ENG11S" );
      acEngine1.setPartNumber( "ENG11PN" );
      acEngines1.setAircraftId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA001" );
      acEngineList1.add( acEngine1 );
      AircraftEngine acEngine2 = new AircraftEngine();
      acEngine2.setAssemblyPosition( "2" );
      acEngine2.setInventoryId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA102" );
      acEngine2.setOEMSerialNumber( "ENG12S" );
      acEngine2.setPartNumber( "ENG12PN" );
      acEngineList1.add( acEngine2 );

      acEngines1.setAircraftId( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA100" );
      acEngines1.setEngines( acEngineList1 );

   }

}
