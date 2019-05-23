package com.mxi.mx.web.query.stockdistribution;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.stocklevel.StockDistLocationsNotTheSameException;
import com.mxi.mx.core.services.stocklevel.StockDistLocationsNotTheSameValidator;


/**
 * This class performs tests for StockDistLocationsNotTheSameValidator
 *
 */
public class StockDistLocationsNotTheSameValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iMainWarehouse;
   private LocationKey iLineStore;
   private LocationKey iHangerStore;
   private StockNoKey iStockNo;
   private StockDistReqKey iStockDistReqA;
   private StockDistReqKey iStockDistReqB;
   private StockDistReqKey iStockDistReqC;
   private StockDistReqKey iStockDistReqD;

   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final String STOCK_CODE = "SKCD";
   private static final String STOCK_NAME = "SKNAME";
   private static final Float NEEDED_QTY = new Float( 5.0 );


   @Before
   public void setup() throws Exception {
      // create main warehouse
      iMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "mainwarehouse" );
         location.setName( "mainwarehouse" );
         location.setIsSupplyLocation( true );
      } );

      // create line store, hanger store having main warehouse as their supplier
      iLineStore = CreateSrvStoreLocation( "linestore", iMainWarehouse );
      iHangerStore = CreateSrvStoreLocation( "hangerstore", iMainWarehouse );

      // create stock
      iStockNo = new StockBuilder().withStockCode( STOCK_CODE ).withStockName( STOCK_NAME )
            .withInvClass( RefInvClassKey.BATCH ).withQtyUnitKey( QTY_UNIT ).build();

      // create stock dist requests: A and B have the same locations while A and C have different TO
      // location and A and D have different FROM locations
      iStockDistReqA = createStockDistReqWithLocations( iMainWarehouse, iLineStore );
      iStockDistReqB = createStockDistReqWithLocations( iMainWarehouse, iLineStore );
      iStockDistReqC = createStockDistReqWithLocations( iMainWarehouse, iHangerStore );
      iStockDistReqD = createStockDistReqWithLocations( iHangerStore, iLineStore );
   }


   /**
    * Create serviceable store location
    *
    * @param aStoreName
    * @return
    */
   private LocationKey CreateSrvStoreLocation( String aStoreName, LocationKey aMainWarehouse ) {
      return Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( aStoreName );
         location.setName( aStoreName );
         location.setHubLocation( aMainWarehouse );
      } );
   }


   /**
    * Create Stock Dist Request
    *
    * @param aFromLocation
    * @param aToLocation
    * @return
    */
   private StockDistReqKey createStockDistReqWithLocations( LocationKey aFromLocation,
         LocationKey aToLocation ) {
      return Domain.createStockDistReq( stockDistReq -> {
         stockDistReq.setStockNo( iStockNo );
         stockDistReq.setNeededLocation( aToLocation );
         stockDistReq.setSupplierLocation( aFromLocation );
         stockDistReq.setNeededQuantity( NEEDED_QTY );
      } );
   }


   /**
    *
    * Test that stock distribution requests with same FROM and TO locations will pass the validation
    *
    * @throws StockDistLocationsNotTheSameException
    */
   @Test
   public void testStockDistReqWithSameLocations() throws StockDistLocationsNotTheSameException {

      StockDistReqKey[] lStockDistReqKeys = new StockDistReqKey[2];
      lStockDistReqKeys[0] = iStockDistReqA;
      lStockDistReqKeys[1] = iStockDistReqB;

      StockDistLocationsNotTheSameValidator.validate( lStockDistReqKeys );
   }


   /**
    *
    * Test that stock distribution requests with different TO locations will throw an exception
    *
    * @throws StockDistLocationsNotTheSameException
    */
   @Test( expected = StockDistLocationsNotTheSameException.class )
   public void testStockDistReqWithDiffToLocations() throws StockDistLocationsNotTheSameException {

      StockDistReqKey[] lStockDistReqKeys = new StockDistReqKey[2];
      lStockDistReqKeys[0] = iStockDistReqA;
      lStockDistReqKeys[1] = iStockDistReqC;

      StockDistLocationsNotTheSameValidator.validate( lStockDistReqKeys );
   }


   /**
    *
    * Test that stock distribution requests with different FROM locations will throw an exception
    *
    * @throws StockDistLocationsNotTheSameException
    */
   @Test( expected = StockDistLocationsNotTheSameException.class )
   public void testStockDistReqWithDiffFromLocations()
         throws StockDistLocationsNotTheSameException {

      StockDistReqKey[] lStockDistReqKeys = new StockDistReqKey[2];
      lStockDistReqKeys[0] = iStockDistReqA;
      lStockDistReqKeys[1] = iStockDistReqD;

      StockDistLocationsNotTheSameValidator.validate( lStockDistReqKeys );
   }

}
