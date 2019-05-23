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
 * Test case for IgnoredTopLocations.qrx
 *
 * @author fzhang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IgnoredTopLocationsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static String LOCATION_KEY = "location_key";
   private static String LOCATION_CD_NAME = "location_cd_name";
   private static String LOC_TYPE_CD = "loc_type_cd";

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
    * Verify that top ignored locations should be retrieved
    *
    */
   @Test
   public void topIgnoredLocationsShouldBeRetrieved() {

      // set up test data
      setUpTopIgnoredLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert row count is 2, and top ignored locations of the supply location are retrieved
      assertTopIgnoredLocationsAreRetrieved();
   }


   /**
    *
    * Verify that non-top ignored locations should NOT be retrieved
    *
    */
   @Test
   public void nonTopIgnoredLocationsShouldNotBeRetrieved() {

      // set up test data
      setUpTopIgnoredLocations();
      setUpNonTopIgnoredLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert row count is 2, and top ignored locations of the supply location are retrieved
      assertTopIgnoredLocationsAreRetrieved();
   }


   /**
    *
    * Verify that top ignored locations for another supply location should NOT be retrieved
    *
    */
   @Test
   public void topIgnoredLocationsForAnotherSupplyLocShouldNotBeRetrieved() {

      // set up test data
      setUpTopIgnoredLocations();
      setUpTopIgnoredLocForAnotherSupplyLocations();

      // execute the query with the supply location
      iDataSet = execute();

      // assert row count is 2, and top ignored locations of the supply location are retrieved
      assertTopIgnoredLocationsAreRetrieved();
   }


   /**
    * Assert Top Ignored Locations Are Retrieved
    *
    */
   private void assertTopIgnoredLocationsAreRetrieved() {
      assertEquals( "item is returned", 2, iDataSet.getRowCount() );

      iDataSet.next();
      assertEquals( iSrvstoreLocation, iDataSet.getKey( LocationKey.class, LOCATION_KEY ) );

      iDataSet.next();
      assertEquals( iDockLocation, iDataSet.getKey( LocationKey.class, LOCATION_KEY ) );
   }


   public void setUpTopIgnoredLocations() {
      iSupplyLocation =
            new LocationDomainBuilder().withName( "SUPPLY_LOCATION" ).isSupplyLocation().build();
      iAnotherSupplyLocation =
            new LocationDomainBuilder().withName( "ANOTHER_SUPPLY_LOCATION" ).isSupplyLocation().build();

      // two ignored locations for the supply location
      iSrvstoreLocation = new LocationDomainBuilder().withName( "SRVSTORE_LOCATION" )
            .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.SRVSTORE )
            .isIgnoredLocation().build();
      iDockLocation =
            new LocationDomainBuilder().withName( "DOCK_LOCATION" ).withSupplyLocation( iSupplyLocation )
                  .withType( RefLocTypeKey.DOCK ).isIgnoredLocation().build();
      iLineLocation = new LocationDomainBuilder().withName( "LINE_LOCATION" )
            .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.LINE ).build();

   }


   public void setUpNonTopIgnoredLocations() {
      // two non-top ignored locations for the supply location
      iPredrawLocation = new LocationDomainBuilder().withName( "PREDRBIN_LOCATION" )
            .withParent( iSrvstoreLocation ).withSupplyLocation( iSupplyLocation )
            .withType( RefLocTypeKey.PREDRBIN ).isIgnoredLocation().build();
      iStoreLocation = new LocationDomainBuilder().withName( "STORE_LOCATION" )
            .withParent( iSrvstoreLocation ).withSupplyLocation( iSupplyLocation )
            .withType( RefLocTypeKey.STORE ).isIgnoredLocation().build();

   }


   public void setUpTopIgnoredLocForAnotherSupplyLocations() {
      // two ignored locations for the supply location
      iAnotherSrvstoreLocation = new LocationDomainBuilder().withName( "ANOTHER_SRVSTORE_LOCATION" )
            .withSupplyLocation( iAnotherSupplyLocation ).withType( RefLocTypeKey.SRVSTORE )
            .isIgnoredLocation().build();
      iAnotheriDockLocation = new LocationDomainBuilder().withName( "ANOTHER_DOCK_LOCATION" )
            .withSupplyLocation( iAnotherSupplyLocation ).withType( RefLocTypeKey.DOCK )
            .isIgnoredLocation().build();

   }


   public DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocDbId", iSupplyLocation.getDbId() );
      lArgs.add( "aLocId", iSupplyLocation.getId() );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
