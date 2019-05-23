package com.mxi.mx.core.query.usage;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This is a unit test to test GetAircraftAssemblyConfigurationByDate.qrx.
 *
 * @author Yiyi Dai
 * @created March 17, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetAircraftAssemblyConfigurationByDateTest {

   private static final Date OOS_FLIGHT_ACTUAL_ARRIVAL_DATE = DateUtils.addDays( new Date(), -5 );

   private static final Date OLD_ENGINE_REMOVAL_DATE = DateUtils.addDays( new Date(), -4 );

   private static final Date NEW_ENGINE_INSTALLATION_DATE = DateUtils.addDays( new Date(), -3 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test is to verify the aircraft configuration are taken from the configuration at the
    * given actual arrival date.
    *
    * Scenario:
    *
    * -- On an aircraft, an old Engine was removed<br>
    * -- then a new engine was installed<br>
    * -- When the query is executed<br>
    * -- Then the aircraft and the old engine are returned from the query; while the new engine is
    * not returned.
    */
   @Test
   public void itReturnsTheAssembliesAtTheActualArrivalDate() {

      InventoryKey lAircraftKey = Domain.createAircraft();

      InventoryKey lOldEngine =
            removeOldEngineFromAircraft( lAircraftKey, OLD_ENGINE_REMOVAL_DATE );

      installNewEngineToAircraft( lAircraftKey, NEW_ENGINE_INSTALLATION_DATE );

      // Get the assemblies when inserting a flight between the second and third flight
      QuerySet lQs = getAircraftConfigurationByDate( lAircraftKey, OOS_FLIGHT_ACTUAL_ARRIVAL_DATE );

      boolean lGetAircraftUsages = false;
      boolean lGetEngineUsages = false;

      while ( lQs.next() ) {
         if ( lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" )
               .equals( lAircraftKey ) ) {
            lGetAircraftUsages = true;
         } else if ( lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" )
               .equals( lOldEngine ) ) {
            lGetEngineUsages = true;
         }
      }

      Assert.assertTrue( "Expected Aircraft to be returned", lGetAircraftUsages );
      Assert.assertTrue( "Expected Engine to be returned", lGetEngineUsages );

      // Make sure the new engine or duplicates are not returned
      Assert.assertEquals( "Extra assemblies were returned", 2, lQs.getRowCount() );

   }


   /**
    * Remove an engine from an aircraft at a date
    *
    * @return the Engine
    */
   private InventoryKey removeOldEngineFromAircraft( final InventoryKey aAircraftKey,
         final Date aRemovalDate ) {

      final InventoryKey lOldEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setHighest( aAircraftKey );
                  aRemovalRecord.setParent( aAircraftKey );
                  aRemovalRecord.setAssembly( aAircraftKey );
                  aRemovalRecord.setRemovalDate( aRemovalDate );
               }
            } );
         }
      } );

      return lOldEngine;
   }


   /**
    * Install an engine from an aircraft at a date
    */
   private InventoryKey installNewEngineToAircraft( final InventoryKey aAircraftKey,
         final Date aRemovalDate ) {

      final InventoryKey lNewEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setParent( aAircraftKey );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aBuilder ) {
                  aBuilder.setHighest( aAircraftKey );
                  aBuilder.setParent( aAircraftKey );
                  aBuilder.setAssembly( aAircraftKey );
                  aBuilder.setInstallationDate( aRemovalDate );
               }
            } );
         }
      } );

      return lNewEngine;
   }


   /**
    * Get the usages when inserting a flight between the second and third flight.
    *
    * @return the dataset of usage
    */
   private QuerySet getAircraftConfigurationByDate( InventoryKey aAircraftKey, Date aDate ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aAircraftKey, "aAcftInvNoDbId", "aAcftInvNoId" );
      lArgs.add( "aDate", aDate );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
