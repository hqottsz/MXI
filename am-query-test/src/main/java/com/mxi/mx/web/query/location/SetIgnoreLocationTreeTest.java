package com.mxi.mx.web.query.location;

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
import com.mxi.mx.core.services.location.LocationService;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * Test case for SetIgnoreLocationTree.qrx
 *
 * @author fzhang
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SetIgnoreLocationTreeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iSupplyLocation;
   private LocationKey iSrvstoreLocation;
   private LocationKey iPredrawLocation;
   private LocationKey iStoreLocation;


   /**
    *
    * Verify that the location tree ignore flag will be set to TRUE
    *
    */
   @Test
   public void locationTreeIgnoreFlagShouldBeSetToTrue() {

      // set up test data
      setupLocationTree();

      // set ignore location tree to TRUE
      LocationService.setIgnoreLocationTree( iSrvstoreLocation, true );

      // assert that row count is 2, and the location tree ignore flag is set to TRUE
      assertLocationTreeIgnoreFlagIsSetTo( true );
   }


   /**
    *
    * Verify that the location tree ignore flag will be reset to FALSE
    *
    */
   @Test
   public void locationTreeIgnoreFlagShouldBeSetToFalse() {

      // set up test data
      setupLocationTree();

      // set ignore location tree to FALSE
      LocationService.setIgnoreLocationTree( iSrvstoreLocation, false );

      // assert that row count is 2, and the location tree ignore flag is set to FALSE
      assertLocationTreeIgnoreFlagIsSetTo( false );
   }


   /**
    * Assert location tree ignore flag is set to the given flag
    *
    */
   private void assertLocationTreeIgnoreFlagIsSetTo( boolean aIgnoreFlag ) {
      // assert parent location ignore flag is set to the given flag
      assertLocationIgnoreFlagIsSet( iSrvstoreLocation, aIgnoreFlag );

      // assert all the sub-locations' ignore flags are set to the given flag
      assertLocationIgnoreFlagIsSet( iPredrawLocation, aIgnoreFlag );
      assertLocationIgnoreFlagIsSet( iStoreLocation, aIgnoreFlag );
   }


   private void assertLocationIgnoreFlagIsSet( LocationKey aLocation, boolean aIgnoreFlag ) {

      InvLocTable lInvLocTable = InvLocTable.findByPrimaryKey( aLocation );

      assertEquals( lInvLocTable.getLocName(), lInvLocTable.getNoAutoRsrvBool(), aIgnoreFlag );
   }


   public void setupLocationTree() {
      // setup parent location
      iSrvstoreLocation = new LocationDomainBuilder().withName( "SRVSTORE_LOCATION" )
            .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.SRVSTORE ).build();

      // setup sub-locations for the parent location
      iPredrawLocation =
            new LocationDomainBuilder().withName( "PREDRBIN_LOCATION" ).withParent( iSrvstoreLocation )
                  .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.PREDRBIN ).build();
      iStoreLocation =
            new LocationDomainBuilder().withName( "STORE_LOCATION" ).withParent( iSrvstoreLocation )
                  .withSupplyLocation( iSupplyLocation ).withType( RefLocTypeKey.STORE ).build();

   }


   public DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocDbId", iSupplyLocation.getDbId() );
      lArgs.add( "aLocId", iSupplyLocation.getId() );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
