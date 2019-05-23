/**
 * Query test for PartAvailability.qrx
 *
 */
package com.mxi.mx.web.query.part;

import static com.mxi.am.domain.Domain.createLocation;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefConfigSlotStatusKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.inv.InvOwnerTable;


@RunWith( BlockJUnit4ClassRunner.class )
public final class PartAvailabilityTest {

   private static final String AIRPORT = "ATL";
   private static final String AIRPORT_A = "BOS";
   private static final String AIRPORT_B = "YOW";

   private static final String CONFIG_SLOT_CODE = "TRK_CONF_SLOT";
   private static final String PART_GROUP = "PART_GRP_01";
   private static final String PART_NO = "PART_001";
   private static final String BATCH_PART_NO = "BATCH_PART";

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
    * Test data setup: 5 inventory parts of class TRK and condition RFI. Also 2 inventories are
    * incomplete. All 5 inventories are from same part group connected to TRK config slot
    */
   public PartNoKey setupTrackedPartForRfiRfb() throws MxException, TriggerException {

      HumanResourceKey lHr = Domain.createHumanResource();

      // create location which is not OPS (Transit)
      LocationKey lAirport = createLocation( loc -> {
         loc.setCode( AIRPORT );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );

      // create owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // create tracked part
      PartNoKey lTrackedPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withOemPartNo( PART_NO ).withStatus( RefPartStatusKey.ACTV ).build();

      // create a tracked config slot
      ConfigSlotKey lTrkConfigSlot =
            new ConfigSlotBuilder( CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
                  .withStatus( RefConfigSlotStatusKey.ACTIVE ).isMandatory().build();
      // create part group and attach it to tracked configuration slot. Then add part to part group
      PartGroupKey lPartGroup = Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setCode( PART_GROUP );
         aPartGroup.setConfigurationSlot( lTrkConfigSlot );
         aPartGroup.setInventoryClass( RefInvClassKey.TRK );
         aPartGroup.addPart( lTrackedPartNo );
      } );

      // three tracked inventory for part PART_001 with condition RFI and complete true ( aka real
      // RFI )
      createTrackedInventory( lPartGroup, lTrackedPartNo, RefInvCondKey.RFI, true, lAirport,
            lOwner );
      createTrackedInventory( lPartGroup, lTrackedPartNo, RefInvCondKey.RFI, true, lAirport,
            lOwner );
      createTrackedInventory( lPartGroup, lTrackedPartNo, RefInvCondKey.RFI, true, lAirport,
            lOwner );

      // two tracked inventory for part PART_001 with condition RFI and complete false ( aka RFB )
      createTrackedInventory( lPartGroup, lTrackedPartNo, RefInvCondKey.RFI, false, lAirport,
            lOwner );
      createTrackedInventory( lPartGroup, lTrackedPartNo, RefInvCondKey.RFI, false, lAirport,
            lOwner );

      return lTrackedPartNo;
   }


   /*
    * one serviceable BATCH inventory and one unservicealbe BATCH inventory are created, and
    * shipments to dock A and dock B are done, so as to test batch inventory availability in transit
    */
   public PartNoKey setupBatchPartInTransit() throws MxException, TriggerException {

      HumanResourceKey lHr = Domain.createHumanResource();

      // create location which is not OPS (Transit)
      LocationKey lAirport = createLocation( loc -> {
         loc.setCode( AIRPORT );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lAirportA = createLocation( loc -> {
         loc.setCode( AIRPORT_A );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lLocationDockA = createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setSupplyLocation( lAirportA );
      } );
      LocationKey lAirportB = createLocation( loc -> {
         loc.setCode( AIRPORT_B );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );
      LocationKey lLocationDockB = createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setSupplyLocation( lAirportB );
      } );

      // create owner
      OwnerKey lOwner = new OwnerDomainBuilder().build();

      // create batch parts
      PartNoKey lBatchPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( BATCH_PART_NO ).withStatus( RefPartStatusKey.ACTV ).build();

      // create serviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lServiceableBatchInventory =
            createBatchInventory( lBatchPartNo, RefInvCondKey.RFI, QTY, lAirport, lOwner );

      createShipmentAndSend( lHr, lAirport, lLocationDockA, lBatchPartNo,
            lServiceableBatchInventory, QTY_A );
      createShipmentAndSend( lHr, lAirport, lLocationDockB, lBatchPartNo,
            lServiceableBatchInventory, QTY_B );

      // create unserviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lUnserviceableBatchInventory =
            createBatchInventory( lBatchPartNo, RefInvCondKey.REPREQ, QTY, lAirport, lOwner );

      createShipmentAndSend( lHr, lAirport, lLocationDockA, lBatchPartNo,
            lUnserviceableBatchInventory, QTY_A );
      createShipmentAndSend( lHr, lAirport, lLocationDockB, lBatchPartNo,
            lUnserviceableBatchInventory, QTY_B );

      return lBatchPartNo;
   }


   public PartNoKey setupPoShipment() {

      LocationKey lVendorLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.VENDOR );
         aLocation.setCode( "AMS" );
      } );
      LocationKey lSupplyLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.AIRPORT );
         aLocation.setIsSupplyLocation( true );
      } );
      LocationKey lDockLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
         aLocation.setSupplyLocation( lSupplyLocation );
      } );

      OwnerKey lLocalOwner = Domain.createOwner();
      VendorKey lVendor =
            new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( lVendorLocation ).build();
      OrgKey lReceiptOrganization = new InvOwnerTable( lLocalOwner ).getOrgKey();
      FncAccountKey lLocalAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "INVASSET_ACCOUNT" ).isDefault().build();

      PartNoKey lBatchPartNo = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.BATCH );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
         aPart.setPartStatus( RefPartStatusKey.ACTV );
         aPart.setShortDescription( "TEST" );
      } );

      double lOrderQty = 20.0;
      InventoryKey lBatchInventory = Domain.createBatchInventory( aInventory -> {
         aInventory.setPartNumber( lBatchPartNo );
         aInventory.setBatchNumber( "A_Batch_Number" );
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setLocation( lDockLocation );
         aInventory.setOwner( Domain.createOwner() );
         aInventory.setBinQt( lOrderQty );
      } );

      // Create Exchange order against the vender and local org
      PurchaseOrderKey lPurchaseOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.PURCHASE );
         aPurchaseOrder.vendor( lVendor );
         aPurchaseOrder.setOrganization( lReceiptOrganization );
         aPurchaseOrder.shippingTo( lDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lPOLine = Domain.createPurchaseOrderLine( aPoLine -> {
         aPoLine.orderKey( lPurchaseOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( lBatchPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( lOrderQty ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( lLocalAccount );
      } );

      ShipmentKey lInboundShipment = Domain.createShipment( aShipment -> {
         aShipment.setPurchaseOrder( lPurchaseOrder );
         aShipment.setType( RefShipmentTypeKey.PURCHASE );
         aShipment.addShipmentSegment( aSegment -> {
            aSegment.setFromLocation( lVendorLocation );
            aSegment.setToLocation( lDockLocation );
         } );
      } );

      double lReceiveQty = 10;
      Domain.createShipmentLine( aShipLine -> {
         aShipLine.shipmentKey( lInboundShipment );
         aShipLine.orderLine( lPOLine );
         aShipLine.inventory( lBatchInventory );
         aShipLine.expectedQuantity( lOrderQty );
         aShipLine.receivedQuantity( lReceiveQty );
      } );

      return lBatchPartNo;
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
         LocationKey aToLocation, PartNoKey aBatchPartNo, InventoryKey aBatchInventory,
         double aQty ) throws MxException, TriggerException {

      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( aFromLocation )
            .toLocation( aToLocation ).withType( RefShipmentTypeKey.STKTRN )
            .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();
      new ShipmentLineBuilder( lShipment ).forPart( aBatchPartNo ).forInventory( aBatchInventory )
            .withExpectedQuantity( aQty ).build();

      ShipmentService.send( lShipment, aHr, new Date(), new Date(), null, null );
   }


   /*
    * This test will test total availability of parts for following categories
    *
    * 1. serviceable 2. ready for build
    */
   @Test
   public void testPartAvailabilitySumForTrackedPart() throws Exception {

      PartNoKey lTrackedPartNo = setupTrackedPartForRfiRfb();

      DataSet lDataSet = getPartAvailability( lTrackedPartNo );
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

      assertEquals( 2, lDataSet.getInt( "ready_for_build" ) );
   }


   /*
    * This test will test total availability of BATCH parts for serviceable, serviceable in transit,
    * unserviceable, and unserviceable in transit
    */
   @Test
   public void testPartAvailabilityForBatchPartInTransit() throws Exception {

      PartNoKey lBatchPartNo = setupBatchPartInTransit();

      DataSet lDataSet = getPartAvailability( lBatchPartNo );

      // loop over the first three rows while ignore the last one that is for installed inventories
      for ( int i = 0; i < 3; i++ ) {

         lDataSet.next();
         assertAvailabilityAtLocation( lDataSet );
      }
   }


   /*
    * Get part availability by executing query PartAvailability.qrx
    */
   private DataSet getPartAvailability( PartNoKey aPartNoKey ) {

      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNoKey, new String[] { "aPartNoDbId", "aPartNoId" } );
      // execute query and return results
      DataSet lIds = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.part.PartAvailability", lArgs );
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
