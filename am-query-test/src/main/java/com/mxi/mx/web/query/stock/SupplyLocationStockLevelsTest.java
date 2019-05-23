package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Query test for SupplyLocationStockLevels.qrx (the query used to populate the csv containing
 * supply loc stock level data)
 *
 * @author sufelk
 *
 */
public class SupplyLocationStockLevelsTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final String STOCK_CD = "STOCK_CD";
   private static final double STOCK_CD_WEIGHT = 50.0;
   private static final String AIRPORT = "BIA";
   private static final String SRVSTORE = "BIA/SRVSTORE";
   private static final String OWNER = "SOWRY";
   private static final RefStockLowActionKey STOCK_LOW_ACTION = RefStockLowActionKey.POREQ;
   private static final double SAFETY_LEVEL = 5.0;
   private static final double RESTOCK_QT = 10.0;
   private static final double MAX_LEVEL = 100.0;
   private static final double NUM_INBOUND_FLIGHTS = 45.0;
   private static final double STAITION_WEIGHT_FACTOR = 5.0;
   private static final double ALLOCATION_PERCENTAGE = 0.12;
   private static final double REORDER_SHIPPING_QT = 15.0;

   private static StockNoKey iStockNo;
   private static LocationKey iSupplyLocation;
   private static LocationKey iNonSupplyLocation;
   private static OwnerKey iOwner;


   @Before
   public void dataSetup() {
      iStockNo =
            new StockBuilder().withStockCode( STOCK_CD ).withMaxMultQt( STOCK_CD_WEIGHT ).build();

      iSupplyLocation = Domain.createLocation( location -> {
         location.setCode( AIRPORT );
         location.setType( RefLocTypeKey.AIRPORT );
         location.setIsSupplyLocation( true );
         location.setInboundFlightsQt( NUM_INBOUND_FLIGHTS );
      } );

      iNonSupplyLocation = Domain.createLocation( location -> {
         location.setCode( SRVSTORE );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( false );
         location.setInboundFlightsQt( NUM_INBOUND_FLIGHTS );
      } );

      iOwner = Domain.createOwner( owner -> {
         owner.setCode( OWNER );
      } );
   }


   /**
    *
    * GIVEN a stock level for a supply location, WHEN the data is queried against the
    * SupplyLocationStockLevels.qrx file, THEN the stock level details are retrieved as expected.
    *
    */
   @Test
   public void testSupplyLocationStockLevelQrx_MarkedAsSupplyLocations() {

      // create a stock level at a supply location
      buildStockLevel( iSupplyLocation );

      // execute the qrx
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.stock.SupplyLocationStockLevels" );

      // assert that data was retrieved
      assertEquals( "Supply loc stock level", true, lQs.first() );

      // data assertions
      assertEquals( "Stock code", STOCK_CD, lQs.getString( "stock_cd" ) );
      assertEquals( "Stock level location", AIRPORT, lQs.getString( "stock_level_loc" ) );
      assertEquals( "Owner code", OWNER, lQs.getString( "owner_cd" ) );
      assertEquals( "Stock code weight", STOCK_CD_WEIGHT, lQs.getDouble( "stock_cd_weight" ), 0 );
      assertEquals( "Number of inbound flights", NUM_INBOUND_FLIGHTS,
            lQs.getDouble( "num_inbound_flights" ), 0 );
      assertEquals( "Station weight factor", STAITION_WEIGHT_FACTOR,
            lQs.getDouble( "station_weight_factor" ), 0 );
      assertEquals( "Allocation percentage", ALLOCATION_PERCENTAGE * 100,
            lQs.getDouble( "allocation_percentage" ), 0 );
      assertEquals( "Safety level", SAFETY_LEVEL, lQs.getDouble( "safety_level" ), 0 );
      assertEquals( "Restock level", RESTOCK_QT, lQs.getDouble( "restock_level" ), 0 );
      assertEquals( "Max level", MAX_LEVEL, lQs.getDouble( "max_level" ), 0 );
      assertEquals( "Reorder level", REORDER_SHIPPING_QT, lQs.getDouble( "reorder_qt" ), 0 );
      assertEquals( "Stock low action", STOCK_LOW_ACTION, lQs.getString( "stock_low_action" ) );

   }


   /**
    *
    * GIVEN a stock level for a non-supply location, WHEN the data is queried against the
    * SupplyLocationStockLevels.qrx file, THEN the stock level is not retrieved.
    *
    */
   @Test
   public void testSupplyLocationStockLevelQrx_NonSupplyLocation() {

      // create a stock level at a non-supply location
      buildStockLevel( iNonSupplyLocation );

      // execute the qrx
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.stock.SupplyLocationStockLevels" );

      // assert that no data was fetched
      assertEquals( "No stock levels for supply locations", false, lQs.first() );
   }


   /**
    *
    * Builds a stock level at a specified location
    *
    * @param aLocation
    *           the location key
    */
   private void buildStockLevel( LocationKey aLocation ) {
      // create a stock level at a supply location
      new StockLevelBuilder( aLocation, iStockNo, iOwner )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION ).build();
   }
}
