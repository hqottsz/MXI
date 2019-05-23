package com.mxi.mx.web.query.location;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Location;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * Query test for GetBinSubLocationsForSrvstore.qrx
 *
 * @author sufelk
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetBinInformationForSrvstoreTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private final String SRVSTORE_LOC_CODE = "AIRPORT/DOCK/SRVSTG/SRVSTORE";
   private final String BIN1_LOC_CODE = "AIRPORT/DOCK/SRVSTG/SRVSTORE/BIN1";
   private final String STORE_LOC_CODE = "AIRPORT/DOCK/SRVSTG/SRVSTORE/STORE";
   private final String BIN2_LOC_CODE = "AIRPORT/DOCK/SRVSTG/SRVSTORE/STORE/BIN2";


   /**
    *
    * GIVEN two bin locations in a location hierachy : {SRVSTORE-->STORE-->BIN1} and
    * {SRVSTORE-->BIN2}, WHEN the query GetBinInformationForSrvstore.qrx is executed for the
    * SRVSTORE location, THEN the location code, parent location code and route order of the two bin
    * locations should be retrieved.
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testAllBinLocationsAndRouteOrdersReturned() throws MxException, TriggerException {

      // create a serviceable store
      LocationKey lSrvStore = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.SRVSTORE );
            aLocation.setCode( SRVSTORE_LOC_CODE );
            aLocation.setParent( Domain.createLocation( new DomainConfiguration<Location>() {

               @Override
               public void configure( Location aLocation ) {
                  aLocation.setType( RefLocTypeKey.SRVSTG );
               }
            } ) );
         }
      } );

      // create a bin that is within a store belonging to the serviceable store
      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.BIN );
            aLocation.setCode( BIN1_LOC_CODE );
            aLocation.setParent( Domain.createLocation( new DomainConfiguration<Location>() {

               @Override
               public void configure( Location aLocation ) {
                  aLocation.setType( RefLocTypeKey.STORE );
                  aLocation.setCode( STORE_LOC_CODE );
                  aLocation.setParent( lSrvStore );
               }
            } ) );
         }
      } );

      // create a bin that is directly under the serviceable store in the hierarchy
      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.BIN );
            aLocation.setCode( BIN2_LOC_CODE );
            aLocation.setParent( lSrvStore );
         }
      } );

      // execute the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lSrvStore, "aLocationDbId", "aLocationId" );
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.location.GetBinInformationForSrvstore", lArgs );

      // assert whether two rows were returned (only the bin locations should be returned)
      assertEquals( "Number of bin locations", 2, lQs.getRowCount(), 0 );

      // assert that the parent location code and route order were fetched for the first bin. Route
      // order should be -1 by default.
      lQs.first();
      assertEquals( "Parent location code", STORE_LOC_CODE, lQs.getString( "parent_loc_cd" ) );
      assertEquals( "Route order", -1, lQs.getInt( "route_order" ), 0 );

      // assert that location code was fetched for the second bin
      lQs.next();
      assertEquals( "Location code", BIN2_LOC_CODE, lQs.getString( "loc_code" ) );

   }

}
