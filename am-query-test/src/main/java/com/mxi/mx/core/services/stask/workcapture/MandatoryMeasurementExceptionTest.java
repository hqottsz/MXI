package com.mxi.mx.core.services.stask.workcapture;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.RefDataValueKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.services.stask.measurement.MeasurementDetailsTO;


/**
 * Tests for {@link MandatoryMeasurementExceptionTest}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MandatoryMeasurementExceptionTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Test
   public void testMandatoryMeasurementException_TEXT() throws Exception {

      // TEST CASE: Not empty Text - expected no exception
      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();

      try {
         lMeasurementDetailsTO.setParmText( "TEST_TEXT      " );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.TEXT );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

      // TEST CASE: Empty Text - expected the exception
      try {
         lMeasurementDetailsTO.setParmText( "" );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.TEXT );

         fail( "Expected MandatoryMeasurementException but it was not thrown." );

      } catch ( MandatoryMeasurementException e ) {
         ;
      }

      // TEST CASE: Blank spaces - expected the exception
      try {
         lMeasurementDetailsTO.setParmText( "           " );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.TEXT );

         fail( "Expected MandatoryMeasurementException but it was not thrown." );

      } catch ( MandatoryMeasurementException e ) {
         ;
      }

   }


   @Test
   public void testMandatoryMeasurementException_CALENDAR() throws Exception {

      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();

      // TEST CASE: Date is not NULL - expected no exception

      try {
         lMeasurementDetailsTO.setParmDate( new Date() );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.CALENDAR );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

      // TEST CASE: Date is NULL - expected the exception
      try {
         lMeasurementDetailsTO.setParmDate( null );
         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.CALENDAR );

         fail( "Expected MandatoryMeasurementException but it was not thrown." );

      } catch ( MandatoryMeasurementException e ) {
         ;
      }

   }


   @Test
   public void testMandatoryMeasurementException_CHARACTER() throws Exception {

      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();

      // TEST CASE: CHARACTER is NULL - expected the exception

      try {
         lMeasurementDetailsTO.setDataValue( null );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO,
               RefDomainTypeKey.CHARACTER );
         fail( "Expected MandatoryMeasurementException but it was not thrown." );

      } catch ( MandatoryMeasurementException e ) {
         ;
      }

      // TEST CASE: CHARACTER is not NULL - expected no exception

      try {
         lMeasurementDetailsTO.setDataValue( new RefDataValueKey( "4650:BUSY" ) );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO,
               RefDomainTypeKey.CHARACTER );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

   }


   @Test
   public void testMandatoryMeasurementException_NUMERIC() throws Exception {

      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();
      // TEST CASE: value is NULL
      try {
         lMeasurementDetailsTO.setParmQuantity( null );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.NUMERIC );
         fail( "Expected MandatoryMeasurementException but it was not thrown." );

      } catch ( MandatoryMeasurementException e ) {
         ;
      }

      // TEST CASE: numeric value is not NULL

      try {
         lMeasurementDetailsTO.setParmQuantity( 234d );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.NUMERIC );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

   }


   @Test
   public void testMandatoryMeasurementException_NA() throws Exception {

      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();
      // TEST CASE: value is NULL
      lMeasurementDetailsTO.setParmQuantity( null );
      lMeasurementDetailsTO.setNa( true );

      MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.NUMERIC );
      // No exception expected
   }


   @Test
   public void testMandatoryMeasurementException_CHECKBOX() throws Exception {

      MeasurementDetailsTO lMeasurementDetailsTO = new MeasurementDetailsTO();
      // TEST CASE: Checkbox is checked
      try {
         lMeasurementDetailsTO.setParmQuantity( 1d );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.CHK );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

      // TEST CASE: Checkbox is checked

      try {
         lMeasurementDetailsTO.setParmQuantity( 2d );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.CHK );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

      // TEST CASE: Checkbox is unchecked ( NULL default value )

      try {
         lMeasurementDetailsTO.setParmQuantity( null );

         MandatoryMeasurementException.validate( lMeasurementDetailsTO, RefDomainTypeKey.CHK );

      } catch ( MandatoryMeasurementException e ) {
         fail( "Expected no MandatoryMeasurementException but it was thrown." );
      }

   }

}
