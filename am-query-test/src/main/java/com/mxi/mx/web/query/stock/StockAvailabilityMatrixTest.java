/**
 * Query test for StockAvailabilityMatrix.qrx
 *
 */

package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefConfigSlotStatusKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.shipment.ShipmentService;


@RunWith( BlockJUnit4ClassRunner.class )
public final class StockAvailabilityMatrixTest {

   private static final String AIRPORT = "ATL";
   private static final String AIRPORT_A = "BOS";
   private static final String AIRPORT_B = "YOW";

   private static final String CONFIG_SLOT_CODE = "TRK_CONF_SLOT";
   private static final String PART_GROUP = "PART_GRP_01";
   private static final String PART_NO1 = "PART_001";
   private static final String PART_NO2 = "PART_002";
   private static final String BATCH_PART_NO = "BATCH_PART";
   private static final String STOCK_NO = "TRK_STOCK_001";
   private static final String BATCH_STOCK_NO = "BATCH_STOCK_001";
   private static StockNoKey iStock = null;
   private static StockNoKey iBatchStock = null;
   private static PartNoKey iBatchPartNo = null;

   private static final double QTY = 200.0;
   private static final double QTY_A = 60.0;
   private static final double QTY_B = 80.0;

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /*
    * Test data setup: 5 inventory parts of class TRK and condition RFI. 3 inventories are
    * incomplete. 2 of the inventories are from OEM part PART_001. 3 inventories from PART_002. Both
    * parts PART_001 and PART_002 are assigned with a same stock TRK_STOCK_001
    *
    * one serviceable BATCH inventory and one unserviceable BATCH inventory are created, and
    * shipments to dock A and dock B are done, so as to test batch inventory availability in
    * transit. BATCH parts are assigned to the stock BATCH_STOCK_001
    */

   @Before
   public void setup() throws MxException, TriggerException {
      HumanResourceKey lHr = Domain.createHumanResource();

      // create locations which are not OPS (Transit)
      LocationKey lAirport = Domain.createLocation( loc -> {
         loc.setCode( AIRPORT );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lAirportA = Domain.createLocation( loc -> {
         loc.setCode( AIRPORT_A );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lLocationDockA = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setSupplyLocation( lAirportA );
      } );
      LocationKey lAirportB = Domain.createLocation( loc -> {
         loc.setCode( AIRPORT_B );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lLocationDockB = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setSupplyLocation( lAirportB );
      } );

      // create owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // create stocks and airport stock levels
      iStock =
            new StockBuilder().withStockCode( STOCK_NO ).withInvClass( RefInvClassKey.TRK ).build();
      new StockLevelBuilder( lAirport, iStock, lOwner ).build();

      iBatchStock = new StockBuilder().withStockCode( BATCH_STOCK_NO )
            .withInvClass( RefInvClassKey.BATCH ).build();
      new StockLevelBuilder( lAirport, iBatchStock, lOwner ).build();

      // create part1
      PartNoKey lPartNo1 =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).withOemPartNo( PART_NO1 )
                  .withStatus( RefPartStatusKey.ACTV ).withStock( iStock ).build();
      // create part2
      PartNoKey lPartNo2 =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).withOemPartNo( PART_NO2 )
                  .withStatus( RefPartStatusKey.ACTV ).withStock( iStock ).build();

      // create a tracked config slot
      ConfigSlotKey lTrkConfigSlot =
            new ConfigSlotBuilder( CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
                  .withStatus( RefConfigSlotStatusKey.ACTIVE ).isMandatory().build();
      // create part group and attach it to tracked configuration slot. Then add parts to part group
      PartGroupKey lPartGroup = Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setCode( PART_GROUP );
         aPartGroup.setConfigurationSlot( lTrkConfigSlot );
         aPartGroup.setInventoryClass( RefInvClassKey.TRK );
         aPartGroup.addPart( lPartNo1 );
         aPartGroup.addPart( lPartNo2 );
      } );

      // inventory for part PART_001 with condition RFI and complete true
      createTrackedInventory( lPartGroup, lPartNo1, RefInvCondKey.RFI, true, lAirport, lOwner );

      // inventory for part PART_001 with condition RFI and complete false
      createTrackedInventory( lPartGroup, lPartNo1, RefInvCondKey.RFI, false, lAirport, lOwner );

      // 2 inventory for part PART_002 with condition RFI and complete false
      createTrackedInventory( lPartGroup, lPartNo2, RefInvCondKey.RFI, false, lAirport, lOwner );
      createTrackedInventory( lPartGroup, lPartNo2, RefInvCondKey.RFI, false, lAirport, lOwner );

      // inventory for part PART_002 with condition RFI and complete true
      createTrackedInventory( lPartGroup, lPartNo2, RefInvCondKey.RFI, true, lAirport, lOwner );

      // create batch parts
      iBatchPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( BATCH_PART_NO ).withStatus( RefPartStatusKey.ACTV )
            .withStock( iBatchStock ).build();

      // create serviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lServiceableBatchInventory =
            createBatchInventory( iBatchPartNo, RefInvCondKey.RFI, QTY, lAirport, lOwner );

      createShipmentAndSend( lHr, lAirport, lLocationDockA, lServiceableBatchInventory, QTY_A );
      createShipmentAndSend( lHr, lAirport, lLocationDockB, lServiceableBatchInventory, QTY_B );

      // create unserviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lUnserviceableBatchInventory =
            createBatchInventory( iBatchPartNo, RefInvCondKey.REPREQ, QTY, lAirport, lOwner );

      createShipmentAndSend( lHr, lAirport, lLocationDockA, lUnserviceableBatchInventory, QTY_A );
      createShipmentAndSend( lHr, lAirport, lLocationDockB, lUnserviceableBatchInventory, QTY_B );
   }


   /**
    * Create tracked inventory
    *
    * @param aPartGroup
    * @param aPartNo
    * @param aRefInvCondKey
    * @param aIsComplete
    * @param aAirport
    * @param aOwner
    */
   private void createTrackedInventory( PartGroupKey aPartGroup, PartNoKey aPartNo,
         RefInvCondKey aRefInvCondKey, boolean aIsComplete, LocationKey aAirport,
         OwnerKey aOwner ) {

      Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartGroup( aPartGroup );
         aTrackedInventory.setLocation( aAirport );
         aTrackedInventory.setPartNumber( aPartNo );
         aTrackedInventory.setComplete( aIsComplete );
         aTrackedInventory.setOwner( aOwner );
         aTrackedInventory.setCondition( aRefInvCondKey );
      } );
   }


   /**
    * Create batch inventory
    *
    * @param aBatchPartNo
    * @param aRefInvCondKey
    * @param aQty
    * @param aAirport
    * @param aOwner
    * @return
    */
   private InventoryKey createBatchInventory( PartNoKey aBatchPartNo, RefInvCondKey aRefInvCondKey,
         double aQty, LocationKey aAirport, OwnerKey aOwner ) {

      return new InventoryBuilder().withPartNo( aBatchPartNo ).withClass( RefInvClassKey.BATCH )
            .withCondition( aRefInvCondKey ).withBinQt( aQty ).withOwner( aOwner )
            .atLocation( aAirport ).build();
   }


   /**
    * Create Shipment for batch inventory from location to another location
    *
    * @param aHr
    * @param aFromLocation
    * @param aToLocation
    * @param aBatchInventory
    * @param aQty
    *
    * @throws MxException
    * @throws TriggerException
    */
   private void createShipmentAndSend( HumanResourceKey aHr, LocationKey aFromLocation,
         LocationKey aToLocation, InventoryKey aBatchInventory, double aQty )
         throws MxException, TriggerException {

      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( aFromLocation )
            .toLocation( aToLocation ).withType( RefShipmentTypeKey.STKTRN )
            .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();
      new ShipmentLineBuilder( lShipment ).forPart( iBatchPartNo ).forInventory( aBatchInventory )
            .withExpectedQuantity( aQty ).build();

      ShipmentService.send( lShipment, aHr, new Date(), new Date(), null, null );
   }


   /*
    * This test will test total availability of parts for following categories *** 1. serviceable
    * available *** *** 2. ready for build ***
    */
   @Test
   public void testStockAvailabilitySum() {
      DataSet lDataSet = getStockAvailability( iStock );
      lDataSet.next();
      /*
       * 1. serviceable available: This will include all serviceable inventories including ready for
       * build inventories
       */
      assertEquals( 5, lDataSet.getInt( "serviceable_available" ) );

      /*
       * 2. ready for build (RFB): This will include all ready for build inventories.Those are in
       * inventory class ASSY or TRK and has inventory condition RFI, but are incomplete. RFB
       * calculated even if configuration parameter (ENABLE_READY_FOR_BUILD) is set to false. But
       * the value calculated on this query will only be shown on UI if configuration parameter
       * ENABLE_READY_FOR_BUILD is true
       */

      assertEquals( 3, lDataSet.getInt( "ready_for_build" ) );
   }


   /*
    * This test will test total availability of BATCH parts for serviceable, serviceable in transit,
    * unserviceable, and unserviceable in transit
    */
   @Test
   public void testPartAvailabilityForBatchPartInTransit() {
      DataSet lDataSet = getStockAvailability( iBatchStock );

      // loop over the first three rows while ignore the last one that is for installed inventories
      for ( int i = 0; i < 3; i++ ) {
         lDataSet.next();
         assertAvailabilityAtLocation( lDataSet );
      }

   }


   private DataSet getStockAvailability( StockNoKey aStockNoKey ) {
      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aStockNoKey, new String[] { "aStockNoDbId", "aStockNoId" } );
      // execute query and return results
      DataSet lIds = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.stock.StockAvailabilityMatrix", lArgs );
      return lIds;
   }


   /*
    * Inventory Availability at different locations location serviceable in-transit unserviceable
    * in-transit airport QTY-QTY_A-QTY_B QTY-QTY_A-QTY_B dock A QTY_A QTY_A dock B QTY_B QTY_B
    */
   private static final double DELTA = 0.1;


   private void assertAvailabilityAtLocation( DataSet lDataSet ) {

      String aLocationCd = lDataSet.getString( "loc_cd" );
      switch ( aLocationCd ) {
         case AIRPORT:
            assertEquals( QTY - QTY_A - QTY_B, lDataSet.getInt( "serviceable_available" ), DELTA );
            assertEquals( QTY - QTY_A - QTY_B, lDataSet.getInt( "unserviceable_us" ), DELTA );
            break;
         case AIRPORT_A:
            assertEquals( QTY_A, lDataSet.getInt( "incoming_xfer_srv" ), DELTA );
            assertEquals( QTY_A, lDataSet.getInt( "incoming_xfer_us" ), DELTA );
            break;

         case AIRPORT_B:
            assertEquals( QTY_B, lDataSet.getInt( "incoming_xfer_srv" ), DELTA );
            assertEquals( QTY_B, lDataSet.getInt( "incoming_xfer_us" ), DELTA );
            break;
      }

   }

}
