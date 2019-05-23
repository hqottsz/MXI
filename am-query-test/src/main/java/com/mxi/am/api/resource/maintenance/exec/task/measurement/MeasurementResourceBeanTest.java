package com.mxi.am.api.resource.maintenance.exec.task.measurement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.resource.maintenance.exec.task.measurement.impl.MeasurementResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.DataTypeDao;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.JdbcDataTypeEntityDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database-level test class for the Task Measurement API.
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class MeasurementResourceBeanTest extends ResourceBeanTest {

   Measurement measurement1 = new Measurement();
   Measurement measurement2 = new Measurement();
   Measurement measurement3 = new Measurement();
   Measurement measurement4 = new Measurement();
   Measurement measurement5 = new Measurement();
   Measurement measurement6 = new Measurement();

   LocalDate updatedDate = LocalDate.of( 2018, 1, 15 );

   private static final String INVENTORY_ID = "20000000000000000000000000000000";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( Security.class ).to( CoreSecurity.class );
               bind( DataTypeDao.class ).to( JdbcDataTypeEntityDao.class );
            }
         } );

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Inject
   private MeasurementResourceBean measurementResourceBean;


   @Before
   public void init() throws MxException {
      InjectorContainer.get().injectMembers( this );
      measurementResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();
      buildExpectedMeasurements();
   }


   /**
    * Test retrieving a single result from a barcode search.
    *
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void search_success_byBarcodeSingleResult() {
      MeasurementSearchParameters params =
            new MeasurementSearchParameters().withTaskBarcode( "TASK2BC" );
      List<Measurement> measurements = measurementResourceBean.search( params );
      assertMeasurements( Arrays.asList( measurement6 ), measurements );
   }


   /**
    * Test getting multiple results from an id search. Returns one of each type of data type
    *
    */
   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_byTaskIdMultipleResults() {
      MeasurementSearchParameters params =
            new MeasurementSearchParameters().withTaskId( "10000000000000000000000000000000" );
      List<Measurement> measurements = measurementResourceBean.search( params );
      assertEquals( "Incorrect number of rows returned: ", 5, measurements.size() );
      assertMeasurements(
            Arrays.asList( measurement1, measurement2, measurement3, measurement4, measurement5 ),
            measurements );
   }


   /**
    * Tests that no errors occur when a search with no results is made.
    *
    */
   @Test
   public void search_success_byTaskBarcodeNoResults() {
      MeasurementSearchParameters params =
            new MeasurementSearchParameters().withTaskBarcode( "NOTVALID" );

      List<Measurement> measurements = measurementResourceBean.search( params );
      assertEquals( "Incorrect number of rows returned: ", 0, measurements.size() );
   }


   /**
    * Tests that it's possible to search with multiple parameters. No particular reason this should
    * be done. Returns one result.
    *
    */
   @Test
   public void search_success_multipleParameters() {
      MeasurementSearchParameters params = new MeasurementSearchParameters()
            .withTaskBarcode( "TASK2BC" ).withTaskId( "10100000000000000000000000000000" );
      List<Measurement> measurements = measurementResourceBean.search( params );
      assertEquals( "Incorrect number of rows returned: ", 1, measurements.size() );
      assertMeasurements( Arrays.asList( measurement6 ), measurements );
   }


   /**
    * Asserts that two lists of measurements are equal
    *
    * @param asList
    * @param measurements
    */
   private void assertMeasurements( List<Measurement> expectedMeasurements,
         List<Measurement> actualMeasurements ) {

      // create a hashmap - the parameter code is unique. If not, it indicates bad test data.
      HashMap<String, Measurement> actualHash = new HashMap<>();
      for ( Measurement actual : actualMeasurements ) {
         actualHash.put( actual.getParameterCode(), actual );
      }

      assertEquals( "Incorrect number of rows returned: ", expectedMeasurements.size(),
            actualMeasurements.size() );
      assertEquals( "Parameter codes of measurements are not unique: ", actualMeasurements.size(),
            actualHash.size() );

      for ( Measurement expected : expectedMeasurements ) {
         assertTrue(
               "Expected a parameter with code " + expected.getParameterCode()
                     + " to be returned, but it was not.",
               actualHash.containsKey( expected.getParameterCode() ) );

         Measurement actual = actualHash.get( expected.getParameterCode() );

         assertEquals(
               "Retrieved inventory id for " + expected.getParameterCode() + " is incorrect: ",
               expected.getInventoryId(), actual.getInventoryId() );
         assertEquals(
               "Retrieved parameter description for " + expected.getParameterCode()
                     + " is incorrect: ",
               expected.getParameterDescription(), actual.getParameterDescription() );
         assertEquals(
               "Retrieved unit of measure for " + expected.getParameterCode() + " is incorrect: ",
               expected.getUnitOfMeasure(), actual.getUnitOfMeasure() );
         assertEquals(
               "Retrieved parameter value for " + expected.getParameterCode() + " is incorrect: ",
               expected.getValue(), actual.getValue() );
      }

   }


   private void buildExpectedMeasurements() {
      measurement1.setInventoryId( INVENTORY_ID );
      measurement1.setParameterCode( "TESTTYPE1" );
      measurement1.setParameterDescription( "Test Data Type 1 - TEXT" );
      measurement1.setUnitOfMeasure( "EA" );
      measurement1.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 0 ) ) );
      measurement1.setValue( "Test measurement 1" );

      measurement2.setInventoryId( INVENTORY_ID );
      measurement2.setParameterCode( "TESTTYPE2" );
      measurement2.setParameterDescription( "Test Data Type 2 - CHK" );
      measurement2.setUnitOfMeasure( "EA" );
      measurement2.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 1 ) ) );
      measurement2.setValue( "10" );

      measurement3.setInventoryId( INVENTORY_ID );
      measurement3.setParameterCode( "TESTTYPE3" );
      measurement3.setParameterDescription( "Test Data Type 3 - ME" );
      measurement3.setUnitOfMeasure( "EA" );
      measurement3.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 2 ) ) );
      measurement3.setValue( "15" );

      measurement4.setInventoryId( INVENTORY_ID );
      measurement4.setParameterCode( "TESTTYPE4" );
      measurement4.setParameterDescription( "Test Data Type 4 - CME" );
      measurement4.setUnitOfMeasure( "EA" );
      measurement4.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 3 ) ) );
      measurement4.setValue( "30-JAN-18" );

      measurement5.setInventoryId( INVENTORY_ID );
      measurement5.setParameterCode( "TESTTYPE5" );
      measurement5.setParameterDescription( "Test Data Type 5 - CH" );
      measurement5.setUnitOfMeasure( "EA" );
      measurement5.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 4 ) ) );
      measurement5.setValue( "DATAVAL1" );

      measurement6.setInventoryId( INVENTORY_ID );
      measurement6.setParameterCode( "TESTTYPE1" );
      measurement6.setParameterDescription( "Test Data Type 1 - TEXT" );
      measurement6.setUnitOfMeasure( "EA" );
      measurement6.setUpdatedDate( Date.valueOf( updatedDate.plusDays( 5 ) ) );
      measurement6.setValue( "Test measurement 2" );

   }
}
