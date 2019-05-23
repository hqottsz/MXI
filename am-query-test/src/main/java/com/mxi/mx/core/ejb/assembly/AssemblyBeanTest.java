package com.mxi.mx.core.ejb.assembly;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.FlightMeasurementRequirement;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.FlightMeasurementRequirementSearchParameters;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.Measurement;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.impl.FlightMeasurementRequirementResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for AssemblyBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class AssemblyBeanTest extends ResourceBeanTest {

   @Inject
   FlightMeasurementRequirementResourceBean flightMeasurementRequirementResourceBean;

   AssemblyBean assemblyBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ID = "DB941D0EA40C11E894C75FEB009DBE2D";
   private static final String ASSEMBLY_CD = "ACFTFL1";
   private static final int ASSEMBLY_DB_ID = 4650;
   private static final int DATA_TYPE_ID = 100007;
   private static final int DATA_TYPE_DB_ID = 4650;
   private static final String DATA_TYPE_CD1 = "TEST1";
   private static final String ENG_UNIT_CD1 = "TEST1";

   FlightMeasurementRequirement flightMeasurementRequirementExpected;


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), AssemblyBeanTest.class );
      initializeSecurityContext();
   }


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );

      assemblyBean = new AssemblyBean();
      assemblyBean.ejbCreate();
      assemblyBean.setSessionContext( iSessionContext );

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
    * This method will test whether eqp_assmbl revision date is updated once the flight measurement
    * is unassign from the assembly
    *
    * @throws AmApiResourceNotFoundException
    * @throws MxException
    * @throws TriggerException
    * @throws SQLException
    * @throws InterruptedException
    */
   @Test
   public void testFlightMeasurementUnassignRevisionDateUpdate()
         throws AmApiResourceNotFoundException, MxException, TriggerException, SQLException,
         InterruptedException {

      DataTypeKey[] dataTypeArray = new DataTypeKey[1];
      DataTypeKey dataTypeKey = new DataTypeKey( DATA_TYPE_DB_ID, DATA_TYPE_ID );
      dataTypeArray[0] = dataTypeKey;

      FlightMeasurementRequirement flightMeasurementRequirementOld = searchFlightMeasurement( ID );

      Thread.sleep( 2000 );

      assemblyBean.unassignMeasurement( new AssemblyKey( ASSEMBLY_DB_ID, ASSEMBLY_CD ),
            dataTypeArray );

      FlightMeasurementRequirement flightMeasurementRequirementNew = searchFlightMeasurement( ID );

      // assert whether the revision date is updated once the flight measurement is unassign
      Assert.assertTrue( "The revision date was not updated in the Assemly",
            flightMeasurementRequirementOld.getLastModifiedDate()
                  .before( flightMeasurementRequirementNew.getLastModifiedDate() ) );

      assertFlightMeasurementRequirementObjects( flightMeasurementRequirementNew );

   }


   public FlightMeasurementRequirement searchFlightMeasurement( String id ) {
      FlightMeasurementRequirementSearchParameters searchParams =
            new FlightMeasurementRequirementSearchParameters( id );
      List<FlightMeasurementRequirement> flightMeasurementRequirements =
            flightMeasurementRequirementResourceBean.search( searchParams );
      return flightMeasurementRequirements.get( 0 );
   }


   private void constructFlightMeasurementRequirementObjects() {

      flightMeasurementRequirementExpected = new FlightMeasurementRequirement();
      flightMeasurementRequirementExpected.setAssemblyId( ID );
      List<Measurement> measurementList = new ArrayList<Measurement>();
      Measurement measurements = new Measurement();
      measurements.setDataTypeCode( DATA_TYPE_CD1 );
      measurements.setEngineeringUnitCode( ENG_UNIT_CD1 );
      measurementList.add( measurements );
      flightMeasurementRequirementExpected.setMeasurements( measurementList );;

   }


   private void assertFlightMeasurementRequirementObjects(
         FlightMeasurementRequirement flightMeasurementRequirementsActual ) {
      for ( Measurement measurementExpected : flightMeasurementRequirementExpected
            .getMeasurements() ) {
         flightMeasurementRequirementsActual.getMeasurements().contains( measurementExpected );
      }
      Assert.assertEquals( flightMeasurementRequirementExpected.getAssemblyId(),
            flightMeasurementRequirementsActual.getAssemblyId() );
   }

}
