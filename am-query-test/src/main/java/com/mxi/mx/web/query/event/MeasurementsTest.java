package com.mxi.mx.web.query.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.Measurement;
import com.mxi.am.domain.MeasurementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class performs unit testing on the query file Measurements.qrx.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class MeasurementsTest {

   private static final String TEST_NA_NOTE_FOR_MEASUREMENT = "Test Na Note for Measurement.";
   private static final String MEASUREMENT_CODE_1 = "MEASUREMENT_CODE_1";
   private static final String MEASUREMENT_CODE_2 = "MEASUREMENT_CODE_2";
   private static final String MEASUREMENT_CODE_3 = "MEASUREMENT_CODE_3";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the query returns an NA note of the measurements for the job card
    */
   @Test
   public void itReturnsNaNoteOfMeasurement() {
      // Given
      final InventoryKey lAircraft = Domain.createAircraft();

      final DataTypeKey lMeasurementDefinition1 =
            Domain.createMeasurementDefinition( new DomainConfiguration<MeasurementDefinition>() {

               @Override
               public void configure( MeasurementDefinition aMeasurementDefinition ) {
                  aMeasurementDefinition.setCode( MEASUREMENT_CODE_1 );
               }
            } );

      final DataTypeKey lMeasurementDefinition2 =
            Domain.createMeasurementDefinition( new DomainConfiguration<MeasurementDefinition>() {

               @Override
               public void configure( MeasurementDefinition aMeasurementDefinition ) {
                  aMeasurementDefinition.setCode( MEASUREMENT_CODE_2 );
               }
            } );

      // add three measurements to job card in the order of ME2, ME1, ME3
      final TaskKey lJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.addMeasurementConfig( new DomainConfiguration<Measurement>() {

               @Override
               public void configure( Measurement aMeasurement ) {
                  aMeasurement.setOrder( 2 );
                  aMeasurement.setInventory( lAircraft );
                  aMeasurement.setDataType( lMeasurementDefinition1 );
                  aMeasurement.setNaNote( TEST_NA_NOTE_FOR_MEASUREMENT );
               }

            } );
            aJobCard.addMeasurementConfig( new DomainConfiguration<Measurement>() {

               @Override
               public void configure( Measurement aMeasurement ) {
                  aMeasurement.setOrder( 1 );
                  aMeasurement.setInventory( lAircraft );
                  aMeasurement.setDataType( lMeasurementDefinition2 );
                  aMeasurement.setNaNote( null );
               }

            } );

         }

      } );

      // When the query is executed.
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aEventDbId", lJobCard.getDbId() );
      lDataSetArgument.add( "aEventId", lJobCard.getId() );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lDataSetArgument );

      // Then note is retrieved.
      Assert.assertEquals( "The query did not return the expected number of rows.", 2,
            lQs.getRowCount() );

      lQs.next();
      Assert.assertEquals( "Measurement2 should be in this position.", lMeasurementDefinition2,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );

      // No NA note expected:
      Assert.assertEquals( "Measurement2 should have an empty note.", null,
            lQs.getString( "na_note" ) );

      lQs.next();
      Assert.assertEquals( "Measurement1 should be in this position.", lMeasurementDefinition1,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
      // Expected NA note:
      Assert.assertEquals( "Measurement1 na_note should not be empty.",
            TEST_NA_NOTE_FOR_MEASUREMENT, lQs.getString( "na_note" ) );

   }


   /**
    * Verify that the query returns the correct measurements order against a job card
    */
   @Test
   public void itReturnsCorrectMeasurmentsOrderOfAJobCard() {
      // Given
      final InventoryKey lAircraft = Domain.createAircraft();

      final DataTypeKey lMeasurementDefinition1 =
            Domain.createMeasurementDefinition( new DomainConfiguration<MeasurementDefinition>() {

               @Override
               public void configure( MeasurementDefinition aMeasurementDefinition ) {
                  aMeasurementDefinition.setCode( MEASUREMENT_CODE_1 );
               }
            } );

      final DataTypeKey lMeasurementDefinition2 =
            Domain.createMeasurementDefinition( new DomainConfiguration<MeasurementDefinition>() {

               @Override
               public void configure( MeasurementDefinition aMeasurementDefinition ) {
                  aMeasurementDefinition.setCode( MEASUREMENT_CODE_2 );
               }
            } );

      final DataTypeKey lMeasurementDefinition3 =
            Domain.createMeasurementDefinition( new DomainConfiguration<MeasurementDefinition>() {

               @Override
               public void configure( MeasurementDefinition aMeasurementDefinition ) {
                  aMeasurementDefinition.setCode( MEASUREMENT_CODE_3 );
               }
            } );

      // add three measurements to job card in the order of ME2, ME1, ME3
      final TaskKey lJobCard = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.addMeasurementConfig( new DomainConfiguration<Measurement>() {

               @Override
               public void configure( Measurement aMeasurement ) {
                  aMeasurement.setOrder( 2 );
                  aMeasurement.setInventory( lAircraft );
                  aMeasurement.setDataType( lMeasurementDefinition1 );
               }

            } );
            aJobCard.addMeasurementConfig( new DomainConfiguration<Measurement>() {

               @Override
               public void configure( Measurement aMeasurement ) {
                  aMeasurement.setOrder( 1 );
                  aMeasurement.setInventory( lAircraft );
                  aMeasurement.setDataType( lMeasurementDefinition2 );
               }

            } );
            aJobCard.addMeasurementConfig( new DomainConfiguration<Measurement>() {

               @Override
               public void configure( Measurement aMeasurement ) {
                  aMeasurement.setOrder( 3 );
                  aMeasurement.setInventory( lAircraft );
                  aMeasurement.setDataType( lMeasurementDefinition3 );
               }

            } );
         }

      } );

      // When the query is executed.
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aEventDbId", lJobCard.getDbId() );
      lDataSetArgument.add( "aEventId", lJobCard.getId() );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lDataSetArgument );

      // Then
      Assert.assertEquals( "The query did not return the expected number of rows.", 3,
            lQs.getRowCount() );

      lQs.next();
      Assert.assertEquals( "Measurement2 should be in this position.", lMeasurementDefinition2,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );

      lQs.next();
      Assert.assertEquals( "Measurement1 should be in this position.", lMeasurementDefinition1,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );

      lQs.next();
      Assert.assertEquals( "Measurement3 should be in this position.", lMeasurementDefinition3,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
   }
}
