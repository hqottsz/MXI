
package com.mxi.mx.core.services.forecast;

import static com.mxi.am.domain.Domain.createForecastModel;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.FcRangeKey;
import com.mxi.mx.core.table.fc.FcModelTable;
import com.mxi.mx.core.table.fc.FcRangeTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests for {@linkplain ForecastService}.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ForecastServiceTest {

   private static final String FC_MODEL_NAME = "FC_MODEL_NAME";
   private static final String NEW_FC_MODEL_NAME = "FC_MODEL_NEW_NAME";

   private static final AuthorityKey AUTHORITY_KEY = new AuthorityKey( 1, 1 );
   private static final AuthorityKey NEW_AUTHORITY_KEY = new AuthorityKey( 1, 2 );

   private static final Double FC_HOURS_RATE = 5.0d;

   private static final FcModelKey FC_MODEL_KEY = new FcModelKey( 1, 1 );

   // range months from 1-12
   private static final int RANGE_START_MONTH = 3;
   private static final int RANGE_START_DAY = 17;
   private static final int RANGE_END_MONTH = 3;
   private static final int RANGE_END_DAY = 18;

   private static final int NEW_RANGE_START_MONTH = 1;
   private static final int NEW_RANGE_START_DAY = 1;
   private static final int NEW_RANGE_END_MONTH = 2;
   private static final int NEW_RANGE_END_DAY = 2;

   private static final int ANOTHER_NEW_RANGE_START_MONTH = 10;
   private static final int ANOTHER_NEW_RANGE_START_DAY = 10;
   private static final int ANOTHER_NEW_RANGE_END_MONTH = 20;
   private static final int ANOTHER_NEW_RANGE_END_DAY = 20;

   private Date iAnotherNewRangeEndDate;
   private Date iAnotherNewRangeStartDate;
   private Date iNewRangeEndDate;
   private Date iNewRangeStartDate;
   private Date iRangeEndDate;
   private Date iRangeStartDate;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Verifies that {@linkplain ForecastService#set} deletes the existing ranges of the forecast
    * model when no longer provided in the transfer object.
    *
    * @throws Exception
    */
   @Test
   public void testSetDeletesExistingFcModelRanges() throws Exception {

      // Create a forecast model with a know forecast range.
      FcModelKey lFcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel aForecastModel ) {
                  aForecastModel.setAuthority( AUTHORITY_KEY );
                  aForecastModel.setName( FC_MODEL_NAME );
                  aForecastModel.addRange( RANGE_START_MONTH, RANGE_START_DAY, HOURS,
                        FC_HOURS_RATE );
               }
            } );

      // Ensure the forecast range and rate are as expected prior to the test.
      FcRangeKey lFcRangeKey = new FcRangeKey( lFcModelKey, 1 );
      FcRangeTable lFcRangeTable = FcRangeTable.findByPrimaryKey( lFcRangeKey );
      assertTrue( lFcRangeTable.exists() );
      assertTrue( lFcRangeTable.getStartMonth() == RANGE_START_MONTH );
      assertTrue( lFcRangeTable.getStartDay() == RANGE_START_DAY );

      // Ensure there is only one range.
      assertFalse( FcRangeTable.findByPrimaryKey( new FcRangeKey( lFcModelKey, 2 ) ).exists() );

      // Setup the transfer object for the test.
      // The forecast range is mandatory, so ensure it is different then the origianl.
      ForecastModelTO lForecastModelTO = generateFcModelTO();
      lForecastModelTO.setFcModelKey( lFcModelKey );
      lForecastModelTO.setForecastRanges(
            Arrays.asList( generateFcRangeTO( iNewRangeStartDate, iNewRangeEndDate ) ), "" );

      // Call method under test.
      ForecastService.set( lForecastModelTO );

      // Verify the existing forecast range has been deleted, but verifying there is only one range
      // and the start/end dates of that range do not match the original dates.
      lFcRangeKey = new FcRangeKey( lFcModelKey, 1 );
      lFcRangeTable = FcRangeTable.findByPrimaryKey( lFcRangeKey );
      assertTrue( lFcRangeTable.exists() );
      assertTrue( lFcRangeTable.getStartMonth() != RANGE_START_MONTH );
      assertTrue( lFcRangeTable.getStartDay() != RANGE_START_DAY );

      assertFalse( FcRangeTable.findByPrimaryKey( new FcRangeKey( lFcModelKey, 2 ) ).exists() );
   }


   /**
    * Verifies that {@linkplain ForecastService#set} detects a range that contains an invalid rate
    * value.
    *
    * @throws Exception
    */
   @Test
   public void testSetDetectsInvalidRate() throws Exception {

      // Create a forecast model with a valid rate within it range.
      FcModelKey lFcModelKey = createForecastModel( new DomainConfiguration<ForecastModel>() {

         @Override
         public void configure( ForecastModel aForecastModel ) {
            aForecastModel.setName( FC_MODEL_NAME );
            aForecastModel.addRange( RANGE_START_MONTH, RANGE_START_DAY, HOURS, FC_HOURS_RATE );
         }
      } );

      double lInvalidRate = 0.0d;

      // Setup the transfer object for the test.
      ForecastModelTO lForecastModelTO = generateFcModelTO();
      lForecastModelTO.setFcModelKey( lFcModelKey );
      lForecastModelTO.setForecastRanges(
            Arrays.asList( generateFcRangeTO( iRangeStartDate, iRangeEndDate ) ), "" );

      // Update the TO to have an invalid forecast rate.
      lForecastModelTO.getFirstForecastRange().getForecastRates().get( 0 )
            .setForecastRate( lInvalidRate, "" );

      // Call method under test and verify the exception is thrown.
      try {
         ForecastService.set( lForecastModelTO );
         MxAssert.fail( "Expected InvalidFcRateException" );
      } catch ( InvalidFcRateException e ) {
         // Expected exception.
      }
   }


   /**
    * Verifies that {@linkplain ForecastService#set} sets all ranges for the forecast model that are
    * provided within the transfer object.
    *
    * @throws Exception
    */
   @Test
   public void testSetUpdatesFcModelRanges() throws Exception {

      // Create a forecast model with a know forecast range.
      FcModelKey lFcModelKey = createForecastModel( new DomainConfiguration<ForecastModel>() {

         @Override
         public void configure( ForecastModel aBuilder ) {
            aBuilder.setAuthority( AUTHORITY_KEY );
            aBuilder.setName( FC_MODEL_NAME );
            aBuilder.addRange( RANGE_START_MONTH, RANGE_START_DAY, HOURS, FC_HOURS_RATE );
         }
      } );

      // Ensure the forecast range and rate are as expected prior to the test.
      FcRangeKey lFcRangeKey = new FcRangeKey( lFcModelKey, 1 );
      FcRangeTable lFcRangeTable = FcRangeTable.findByPrimaryKey( lFcRangeKey );
      assertTrue( lFcRangeTable.exists() );
      assertTrue( lFcRangeTable.getStartMonth() == RANGE_START_MONTH );
      assertTrue( lFcRangeTable.getStartDay() == RANGE_START_DAY );

      // Ensure there is only one range.
      assertFalse( FcRangeTable.findByPrimaryKey( new FcRangeKey( lFcModelKey, 2 ) ).exists() );

      // Setup the transfer object for the test with two new forecast ranges.
      ForecastModelTO lForecastModelTO = generateFcModelTO();
      lForecastModelTO.setFcModelKey( lFcModelKey );
      lForecastModelTO
            .setForecastRanges(
                  Arrays.asList( generateFcRangeTO( iNewRangeStartDate, iNewRangeEndDate ),
                        generateFcRangeTO( iAnotherNewRangeStartDate, iAnotherNewRangeEndDate ) ),
                  "" );

      // Call method under test.
      ForecastService.set( lForecastModelTO );

      // Verify the that only the two new ranges exist.
      for ( int lRangeId : Arrays.asList( 1, 2 ) ) {

         FcRangeKey lA = new FcRangeKey( lFcModelKey, lRangeId );
         lFcRangeTable = FcRangeTable.findByPrimaryKey( lA );

         assertTrue( lFcRangeTable.exists() );

         if ( lFcRangeTable.getStartMonth() == NEW_RANGE_START_MONTH ) {
            assertTrue( lFcRangeTable.getStartDay() == NEW_RANGE_START_DAY );
         } else if ( lFcRangeTable.getStartMonth() == ANOTHER_NEW_RANGE_START_MONTH ) {
            assertTrue( lFcRangeTable.getStartDay() == ANOTHER_NEW_RANGE_START_DAY );
         } else {
            fail( "Unexpected range found." );
         }
      }

      assertFalse( FcRangeTable.findByPrimaryKey( new FcRangeKey( lFcModelKey, 3 ) ).exists() );
   }


   /**
    * Verifies that {@linkplain ForecastService#set} updates the name and authority of the forecast
    * model.
    *
    * @throws Exception
    */
   @Test
   public void testSetUpdatesNameAndAuthorityOfFcModel() throws Exception {

      // Create a forecast model with a know name and authority key.
      FcModelKey lFcModelKey = createForecastModel( new DomainConfiguration<ForecastModel>() {

         @Override
         public void configure( ForecastModel aForecastModel ) {
            aForecastModel.setAuthority( AUTHORITY_KEY );
            aForecastModel.setName( FC_MODEL_NAME );
         }
      } );

      // Ensure the original name and authority are as expected prior to the test.
      FcModelTable lFcModelTable = FcModelTable.findByPrimaryKey( lFcModelKey );
      assertEquals( FC_MODEL_NAME, lFcModelTable.getSDesc() );
      assertEquals( AUTHORITY_KEY, lFcModelTable.getAuthority() );

      // Setup the transfer object for the test.
      ForecastModelTO lForecastModelTO = generateFcModelTO();
      lForecastModelTO.setFcModelKey( lFcModelKey );
      lForecastModelTO.setName( NEW_FC_MODEL_NAME, "" );
      lForecastModelTO.setAuthority( NEW_AUTHORITY_KEY );

      // Call method under test.
      ForecastService.set( lForecastModelTO );

      // Verify the forecast model was updated.
      lFcModelTable = FcModelTable.findByPrimaryKey( lFcModelKey );
      assertEquals( NEW_FC_MODEL_NAME, lFcModelTable.getSDesc() );
      assertEquals( NEW_AUTHORITY_KEY, lFcModelTable.getAuthority() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      Calendar lCal = Calendar.getInstance();

      // Note: the ranges months are from 1-12, thus subtract one for Calendar.

      lCal.set( 2014, RANGE_START_MONTH - 1, RANGE_START_DAY, 0, 0, 0 );
      iRangeStartDate = lCal.getTime();
      lCal.set( 2014, RANGE_END_MONTH - 1, RANGE_END_DAY, 0, 0, 0 );
      iRangeEndDate = lCal.getTime();

      lCal.set( 2014, NEW_RANGE_START_MONTH - 1, NEW_RANGE_START_DAY, 0, 0, 0 );
      iNewRangeStartDate = lCal.getTime();
      lCal.set( 2014, NEW_RANGE_END_MONTH - 1, NEW_RANGE_END_DAY, 0, 0, 0 );
      iNewRangeEndDate = lCal.getTime();

      lCal.set( 2014, ANOTHER_NEW_RANGE_START_MONTH - 1, ANOTHER_NEW_RANGE_START_DAY, 0, 0, 0 );
      iAnotherNewRangeStartDate = lCal.getTime();
      lCal.set( 2014, ANOTHER_NEW_RANGE_END_MONTH - 1, ANOTHER_NEW_RANGE_END_DAY, 0, 0, 0 );
      iAnotherNewRangeEndDate = lCal.getTime();
   }


   /**
    * Generate a populated forecast model transfer object with minimal content.
    *
    * @return populated forecast model transfer object
    *
    * @throws Exception
    */
   private ForecastModelTO generateFcModelTO() throws Exception {

      List<ForecastRangeTO> lFcRanges = new ArrayList<ForecastRangeTO>( 1 );
      lFcRanges.add( generateFcRangeTO( iRangeStartDate, iRangeEndDate ) );

      ForecastModelTO lTO = new ForecastModelTO();
      lTO.setName( FC_MODEL_NAME, "" );
      lTO.setFcModelKey( FC_MODEL_KEY );
      lTO.setForecastRanges( lFcRanges, "" );

      return lTO;
   }


   /**
    * Generate a populated forecast range transfer object with minimal content but using the
    * provided start and end dates.
    *
    * @param aStartDate
    *           range start date
    * @param aEndDate
    *           range end date
    *
    * @return populated forecast range transfer object
    *
    * @throws Exception
    */
   private ForecastRangeTO generateFcRangeTO( Date aStartDate, Date aEndDate ) throws Exception {
      List<ForecastRateTO> lFcRates = new ArrayList<ForecastRateTO>( 1 );
      lFcRates.add( generateFcRateTO() );

      ForecastRangeTO lFcRangeTO = new ForecastRangeTO();
      lFcRangeTO.setStartDate( aStartDate );
      lFcRangeTO.setEndDate( aEndDate );
      lFcRangeTO.setForecastRates( lFcRates, "" );

      return lFcRangeTO;
   }


   /**
    * Generate a populated forecast rate transfer object with minimal content.
    *
    * @return populated forecast rate transfer object
    *
    * @throws Exception
    */
   private ForecastRateTO generateFcRateTO() throws Exception {
      ForecastRateTO lForecastRateTO = new ForecastRateTO();
      lForecastRateTO.setDataType( DataTypeKey.HOURS );
      lForecastRateTO.setForecastRate( FC_HOURS_RATE, "" );

      return lForecastRateTO;
   }

}
