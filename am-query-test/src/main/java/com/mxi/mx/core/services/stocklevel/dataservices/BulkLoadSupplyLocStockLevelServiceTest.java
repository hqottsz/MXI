package com.mxi.mx.core.services.stocklevel.dataservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * Tests the BulkLoadSupplyLocStockLevelService class.
 *
 * @author sufelk
 *
 */
public class BulkLoadSupplyLocStockLevelServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final String STOCK_CD = "STCK001";
   private static final String SUPPLY_LOC = "BIA";
   private static final String OWNER = "SOWRY";
   private static final String WEIGHTING_FACTOR = "10";
   private static final String NUM_INBOUND_FLIGHTS = "2";
   private static final String STATION_WEIGHT_FACTOR = "100";
   private static final String ALLOCATION_PCT = "40";
   private static final String SAFETY_LVL = "5.4999";
   private static final String RESTOCK_LVL = "8.5000";
   private static final String MAX_LVL = "50.7890";
   private static final double SAFETY_LVL_ROUNDED = 5;
   private static final double RESTOCK_LVL_ROUNDED = 9;
   private static final double MAX_LVL_ROUNDED = 51;
   private static final String REORDER_QT = "7";
   private static final String STOCK_LOW_ACTN = RefStockLowActionKey.MANUAL.getCd();

   private BulkLoadSupplyLocStockLevelService iSupplyLocStockLevelService;
   private BulkLoadElementTO iBulkLoadTO;
   private StockNoKey iStockNo;
   private LocationKey iSupplyLoc;
   private OwnerKey iOwner;


   @Before
   public void loadData() {
      // initialization
      iSupplyLocStockLevelService = new BulkLoadSupplyLocStockLevelService();
      iBulkLoadTO = new BulkLoadElementTO();

      // create a stock no
      iStockNo = new StockBuilder().withStockCode( STOCK_CD ).withMaxMultQt( 25.0 ).build();

      // create a supply location
      iSupplyLoc = Domain.createLocation( aLocation -> {
         aLocation.setCode( SUPPLY_LOC );
         aLocation.setIsSupplyLocation( true );
      } );

      // create an owner
      iOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER );
      } );

      // setup the Bulk Load Element transfer object
      iBulkLoadTO.setC0( STOCK_CD );
      iBulkLoadTO.setC1( SUPPLY_LOC );
      iBulkLoadTO.setC2( OWNER );
      iBulkLoadTO.setC3( WEIGHTING_FACTOR );
      iBulkLoadTO.setC4( NUM_INBOUND_FLIGHTS );
      iBulkLoadTO.setC5( STATION_WEIGHT_FACTOR );
      iBulkLoadTO.setC6( ALLOCATION_PCT );
      iBulkLoadTO.setC7( SAFETY_LVL );
      iBulkLoadTO.setC8( RESTOCK_LVL );
      iBulkLoadTO.setC9( MAX_LVL );
      iBulkLoadTO.setC10( REORDER_QT );
      iBulkLoadTO.setC11( STOCK_LOW_ACTN );

   }


   /**
    *
    * GIVEN a valid data row in the form of a BulkLoadElementTO transfer object, for a supply
    * location stock level that does not exist in the Maintenix db, WHEN the TO is passed to the
    * {@link BulkLoadSupplyLocStockLevelService.processRow()} method, THEN a new stock level is
    * created AND all fields are set as expected.
    *
    */
   @Test
   public void testProcessRow_NewStockLevelCreated() {

      // initialize and pass the TO to the processRow method
      iSupplyLocStockLevelService.init( iBulkLoadTO );
      iSupplyLocStockLevelService.processRow( iBulkLoadTO );

      // check whether the stock level has been added
      InvLocStockKey lInvLocStockKey = new InvLocStockKey( iSupplyLoc, iStockNo, iOwner );
      InvLocStockTable lInvLocStockTable = InvLocStockTable.findByPrimaryKey( lInvLocStockKey );
      assertTrue( "Stock level added", lInvLocStockTable.exists() );

      // assert whether the values have been set
      assertEquals( "Weighting Factor", Double.parseDouble( WEIGHTING_FACTOR ),
            EqpStockNoTable.findByPrimaryKey( iStockNo ).getMaxMultQt(), 0 );
      assertEquals( "Number of Inbound Flights", Double.parseDouble( NUM_INBOUND_FLIGHTS ),
            InvLocTable.findByPrimaryKey( iSupplyLoc ).getInboundFlightsQt(), 0 );
      assertEquals( "Station Weight Factor", Double.parseDouble( STATION_WEIGHT_FACTOR ),
            lInvLocStockTable.getWeightFactorQt(), 0 );
      assertEquals( "Allocation Percentage", Double.parseDouble( ALLOCATION_PCT ) / 100,
            lInvLocStockTable.getAllocPct(), 0 );
      assertEquals( "Safety Level", SAFETY_LVL_ROUNDED, lInvLocStockTable.getMinReorderQt(), 0 );
      assertEquals( "Restock Level", RESTOCK_LVL_ROUNDED, lInvLocStockTable.getReorderQt(), 0 );
      assertEquals( "Max Level", MAX_LVL_ROUNDED, lInvLocStockTable.getMaxQt(), 0 );
      assertEquals( "Reorder Quantity", Double.parseDouble( REORDER_QT ),
            lInvLocStockTable.getBatchSize(), 0 );
      assertEquals( "Stock Low Action", STOCK_LOW_ACTN,
            lInvLocStockTable.getStockLowAction().getCd() );

   }


   /**
    *
    * GIVEN a stock level for a supply location, WHEN the attributes of the stock level are changed
    * through a data row in a csv processed through the
    * {@link BulkLoadSupplyLocStockLevelService.processRow()} method, THEN the values are updated in
    * the database
    *
    */
   @Test
   public void testProcessRow_ExistingStockLevelUpdated() {
      // create a stock level with a few fields set
      InvLocStockKey lExistingStockLvl =
            new StockLevelBuilder( iSupplyLoc, iStockNo, iOwner ).withWeightFactorQt( 100.0 )
                  .withMaxLevel( 120.0 ).withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // initialize and pass the TO to the processRow method
      iSupplyLocStockLevelService.init( iBulkLoadTO );
      iSupplyLocStockLevelService.processRow( iBulkLoadTO );

      // assert whether the values have been updated to the values in the TO
      InvLocStockTable lInvLocStockTable = InvLocStockTable.findByPrimaryKey( lExistingStockLvl );
      assertEquals( "Station Weight Factor", Double.parseDouble( STATION_WEIGHT_FACTOR ),
            lInvLocStockTable.getWeightFactorQt(), 0 );
      assertEquals( "Max Level", MAX_LVL_ROUNDED, lInvLocStockTable.getMaxQt(), 0 );
      assertEquals( "Stock Low Action", STOCK_LOW_ACTN,
            lInvLocStockTable.getStockLowAction().getCd() );
   }
}
