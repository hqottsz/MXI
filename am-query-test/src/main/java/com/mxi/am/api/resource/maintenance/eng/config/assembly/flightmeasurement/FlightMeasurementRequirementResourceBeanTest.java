package com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.ibm.icu.text.SimpleDateFormat;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiPreconditionFailException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.impl.FlightMeasurementRequirementResourceBean;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for FlightMeasurementRequirementResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class FlightMeasurementRequirementResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Inject
   FlightMeasurementRequirementResourceBean flightMeasurementRequirementResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   private static final String ID = "DB941D0EA40C11E894C75FEB009DBE2D";
   private static final String INVALID_ASSEMBLY_ID = "DB941D0EA40C11E894C75FEB009DBE9D";
   private static final String INVALID_UUID = "DB941D0EA40C11E894C75FEB009DBE9D0000000";
   private static final String NULL_ID = "";
   private static final String EMPTY_ASSEMBLY_ID = "";

   private static final String DATA_TYPE_CD1 = "TEST1";
   private static final String DATA_TYPE_CD2 = "TEST2";
   private static final String DATA_TYPE_CD3 = "TEST3";
   private static final String ENG_UNIT_CD1 = "UNITCD1";
   private static final String ENG_UNIT_CD2 = "UNITCD2";

   private static final String INVALID_DATA_TYPE_CD = "TEST5";

   private static final String REVISION_DT_VALUE = "2016-12-19 08:41:00";
   SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   FlightMeasurementRequirement flightMeasurementRequirementExpected;

   @ClassRule
   public static final DatabaseConnectionRule databaseConnectionRule =
         new DatabaseConnectionRule( null, () -> {
            enableTriggers();

         } );


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( databaseConnectionRule.getConnection(),
            FlightMeasurementRequirementResourceBeanTest.class );
      initializeSecurityContext();
   }


   static void enableTriggers() {

      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_EQP_FLIGHT_SPEC" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_EQP_ASSMBL" );

   }


   @BeforeClass
   public static void init() {
      // Disable triggers before loading data
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_EQP_FLIGHT_SPEC" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_EQP_ASSMBL" );
   }


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      flightMeasurementRequirementResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
      constructFlightMeasurementRequirementObjects();

   }


   /**
    *
    * Test correct FlightMeasurementRequirement object returns for a valid assembly id success path-
    * Response-
    *
    * @throws AmApiResourceNotFoundException
    *
    * @throws Exception
    */
   @Test
   public void testSearchFlightMeasurementRequirementById() throws AmApiResourceNotFoundException {
      FlightMeasurementRequirementSearchParameters searchParams =
            new FlightMeasurementRequirementSearchParameters( ID );
      List<FlightMeasurementRequirement> flightMeasurementRequirements =
            flightMeasurementRequirementResourceBean.search( searchParams );
      Assert.assertEquals( "More than One results found", 1, flightMeasurementRequirements.size() );
      assertFlightMeasurementRequirementObjectsForSearch( flightMeasurementRequirementExpected,
            flightMeasurementRequirements.get( 0 ) );

   }


   /**
    * Test null returns for invalid assembly id Response
    *
    *
    * @throws Exception
    */
   @Test
   public void testSearchNoFlightMeasurementRequirementFoundInvalidId() {
      FlightMeasurementRequirementSearchParameters searchParams =
            new FlightMeasurementRequirementSearchParameters( INVALID_ASSEMBLY_ID );
      List<FlightMeasurementRequirement> flightMeasurementRequirements =
            flightMeasurementRequirementResourceBean.search( searchParams );
      Assert.assertEquals( 0, flightMeasurementRequirements.size() );

   }


   /**
    * Test exception returns for null assembly id Response
    *
    *
    * @throws Exception
    */
   @Test
   public void testSearchNoFlightMeasurementRequirementFoundNullId() {
      FlightMeasurementRequirementSearchParameters searchParams =
            new FlightMeasurementRequirementSearchParameters( NULL_ID );
      try {
         flightMeasurementRequirementResourceBean.search( searchParams );
         Assert.fail();
      } catch ( AmApiPreconditionFailException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage().contains( "Assembly Id cannot be empty" ) );
      }

   }


   /**
    * Test exception returns for invalid uuid type assembly id Response
    *
    *
    * @throws Exception
    */
   @Test
   public void testSearchNoFlightMeasurementRequirementFoundInvalidUuId() {
      FlightMeasurementRequirementSearchParameters searchParams =
            new FlightMeasurementRequirementSearchParameters( INVALID_UUID );
      try {
         flightMeasurementRequirementResourceBean.search( searchParams );
         Assert.fail();
      } catch ( AmApiPreconditionFailException e ) {
         // Test successful if exception is thrown
         Assert.assertTrue( e.getMessage()
               .contains( "Invalid number of characters for a UUID: " + INVALID_UUID.length() ) );
      }
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testAssignFlightMeasurementFaliureInvalidId() throws AmApiBusinessException {
      flightMeasurementRequirementExpected.setAssemblyId( INVALID_ASSEMBLY_ID );
      flightMeasurementRequirementResourceBean.create( flightMeasurementRequirementExpected );

   }


   @Test( expected = AmApiBadRequestException.class )
   public void testAssignFlightMeasurementFaliureEmptyId() throws AmApiBusinessException {
      flightMeasurementRequirementExpected.setAssemblyId( EMPTY_ASSEMBLY_ID );
      flightMeasurementRequirementResourceBean.create( flightMeasurementRequirementExpected );

   }


   @Test( expected = AmApiBadRequestException.class )
   public void testAssignFlightMeasurementFaliureInvalidDataTypeCode()
         throws AmApiBadRequestException, AmApiBusinessException {
      Measurement measurermentsObject = new Measurement();
      measurermentsObject.setDataTypeCode( INVALID_DATA_TYPE_CD );
      flightMeasurementRequirementExpected.getMeasurements().add( measurermentsObject );
      flightMeasurementRequirementResourceBean.create( flightMeasurementRequirementExpected );

   }


   private void constructFlightMeasurementRequirementObjects() throws ParseException {

      flightMeasurementRequirementExpected = new FlightMeasurementRequirement();
      flightMeasurementRequirementExpected.setAssemblyId( ID );

      List<Measurement> measurementList = new ArrayList<Measurement>();
      Measurement measurermentsObjectOne = new Measurement();
      measurermentsObjectOne.setDataTypeCode( DATA_TYPE_CD1 );
      measurermentsObjectOne.setEngineeringUnitCode( ENG_UNIT_CD1 );

      Measurement measurermentsObjectTwo = new Measurement();
      measurermentsObjectTwo.setDataTypeCode( DATA_TYPE_CD2 );
      measurermentsObjectTwo.setEngineeringUnitCode( ENG_UNIT_CD2 );

      measurementList.add( measurermentsObjectOne );
      measurementList.add( measurermentsObjectTwo );

      flightMeasurementRequirementExpected.setMeasurements( measurementList );;

      SimpleDateFormat iFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date revisionDate = iFormatter.parse( REVISION_DT_VALUE );
      flightMeasurementRequirementExpected.setLastModifiedDate( revisionDate );

   }


   private void assertFlightMeasurementRequirementObjectsForSearch(
         FlightMeasurementRequirement flightMeasurementRequirementExpected,
         FlightMeasurementRequirement flightMeasurementRequirementsActual ) {
      for ( Measurement measurementExpected : flightMeasurementRequirementExpected
            .getMeasurements() ) {
         Assert.assertTrue( flightMeasurementRequirementsActual.getMeasurements()
               .contains( measurementExpected ) );
      }
      Assert.assertEquals( DATA_TYPE_CD1,
            flightMeasurementRequirementsActual.getMeasurements().get( 0 ).getDataTypeCode() );
      Assert.assertEquals( DATA_TYPE_CD2,
            flightMeasurementRequirementsActual.getMeasurements().get( 1 ).getDataTypeCode() );
      Assert.assertEquals( DATA_TYPE_CD3,
            flightMeasurementRequirementsActual.getMeasurements().get( 2 ).getDataTypeCode() );
      Assert.assertEquals( flightMeasurementRequirementExpected.getAssemblyId(),
            flightMeasurementRequirementsActual.getAssemblyId() );
      Assert.assertEquals( flightMeasurementRequirementExpected.getLastModifiedDate(),
            flightMeasurementRequirementsActual.getLastModifiedDate() );
   }

}
