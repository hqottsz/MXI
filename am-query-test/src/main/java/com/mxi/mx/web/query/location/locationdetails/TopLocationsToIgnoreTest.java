package com.mxi.mx.web.query.location.locationdetails;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * Test case for TopLocationsToIgnore.qrx
 *
 * @author fzhang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TopLocationsToIgnoreTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static String LOCATION_KEY = "location_key";

   private LocationKey iSupplyLocation;
   private LocationKey iAnotherSupplyLocation;

   private LocationKey iSrvstoreLocation;
   private LocationKey iDockLocation;
   private LocationKey iLineLocation;
   private LocationKey iPredrawLocation;
   private LocationKey iStoreLocation;

   private LocationKey iAnotherSrvstoreLocation;
   private LocationKey iAnotheriDockLocation;

   private DataSet iDataSet;


   /**
    *
    * Verify that top to-be-ignored locations should be retrieved. Such locations are not marked as
    * ignored yet.
    *
    */
   @Test
   public void topToBeIgnoredLocationsShouldBeRetrieved() {

      // set up test data
      setUptopToBeIgnoredLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert that row count is 2, and top to-be-ignored locations of the supply location are
      // retrieved
      assertTopToBeIgnoredLocationsAreRetrieved();
   }


   /**
    *
    * Verify that non-top to-be-ignored locations should NOT be retrieved. Such locations are not
    * marked as ignored yet.
    *
    */
   @Test
   public void nonTopToBeIgnoredLocationsShouldNotBeRetrieved() {

      // set up test data
      setUptopToBeIgnoredLocations();
      setUpNonTopToBeIgnoredLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert that row count is 2, and top to-be-ignored locations of the supply location are
      // retrieved while non-top ones are not
      assertTopToBeIgnoredLocationsAreRetrieved();
   }


   /**
    *
    * Verify that top to-be-ignored locations for another supply location should NOT be retrieved.
    * Such locations are not marked as ignored yet.
    *
    */
   @Test
   public void topToBeIgnoredLocationsForAnotherSupplyLocShouldNotBeRetrieved() {

      // set up test data
      setUptopToBeIgnoredLocations();
      setUpTopLocForAnotherSupplyLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert row count is 2, and top to-be-ignored locations of the supply location are retrieved
      // while top ones for another supply location are not
      assertTopToBeIgnoredLocationsAreRetrieved();
   }


   /**
    * Assert Top to-be-ignored locations Are Retrieved
    *
    */
   private void assertTopToBeIgnoredLocationsAreRetrieved() {
      assertEquals( "Row count ", 2, iDataSet.getRowCount() );

      iDataSet.next();
      assertEquals( iSrvstoreLocation, iDataSet.getKey( LocationKey.class, LOCATION_KEY ) );

      iDataSet.next();
      assertEquals( iDockLocation, iDataSet.getKey( LocationKey.class, LOCATION_KEY ) );
   }


   public void setUptopToBeIgnoredLocations() {
      iSupplyLocation =
            new LocationDomainBuilder().withName( "SUPPLY_LOCATION" ).isSupplyLocation().build();
      iAnotherSupplyLocation =
            new LocationDomainBuilder().withName( "ANOTHER_SUPPLY_LOCATION" ).isSupplyLocation().build();

      // two to-be-ignored locations for the supply location
      iSrvstoreLocation = new LocationDomainBuilder().withName( "SRVSTORE_LOCATION" )
            .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.SRVSTORE ).build();
      iDockLocation = new LocationDomainBuilder().withName( "DOCK_LOCATION" )
            .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.DOCK ).build();
      iLineLocation =
            new LocationDomainBuilder().withName( "LINE_LOCATION" ).withSupplyLocation( iSupplyLocation )
                  .withType( RefLocTypeKey.LINE ).isIgnoredLocation().build();

   }


   public void setUpNonTopToBeIgnoredLocations() {
      // two non-top to-be-ignored locations for the supply location
      iPredrawLocation =
            new LocationDomainBuilder().withName( "PREDRBIN_LOCATION" ).withParent( iSrvstoreLocation )
                  .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.PREDRBIN ).build();
      iStoreLocation =
            new LocationDomainBuilder().withName( "STORE_LOCATION" ).withParent( iSrvstoreLocation )
                  .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.STORE ).build();

   }


   public void setUpTopLocForAnotherSupplyLocations() {
      // two to-be-ignored locations for another supply location
      iAnotherSrvstoreLocation = new LocationDomainBuilder().withName( "ANOTHER_SRVSTORE_LOCATION" )
            .withSupplyLocation( iAnotherSupplyLocation ).withType( RefLocTypeKey.SRVSTORE )
            .build();
      iAnotheriDockLocation = new LocationDomainBuilder().withName( "ANOTHER_DOCK_LOCATION" )
            .withSupplyLocation( iAnotherSupplyLocation ).withType( RefLocTypeKey.DOCK ).build();

   }


   public DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocDbId", iSupplyLocation.getDbId() );
      lArgs.add( "aLocId", iSupplyLocation.getId() );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
