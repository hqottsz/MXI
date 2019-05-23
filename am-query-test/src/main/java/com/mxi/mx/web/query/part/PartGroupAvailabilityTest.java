/**
 * Query test for PartGroupAvailability.qrx
 *
 */

package com.mxi.mx.web.query.part;

import static com.mxi.am.domain.Domain.createLocation;
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
import com.mxi.mx.core.services.shipment.ShipmentService;


@RunWith( BlockJUnit4ClassRunner.class )
public final class PartGroupAvailabilityTest {

   private static final String MAIN_AIRPORT = "ATL";
   private static final String AIRPORT_A = "BOS";
   private static final String AIRPORT_B = "YOW";

   private static final String CONFIG_SLOT_CODE = "TRK_CONF_SLOT";
   private static final String PART_GROUP = "PART_GROUP";
   private static final String PART_NO_A = "PART_NO_A";
   private static final String PART_NO_B = "PART_NO_B";

   private static final String CONSUM_CONFIG_SLOT_CODE = "CONSUM_CONF_SLOT";
   private static final String BATCH_PART_GROUP = "BATCH_PART_GROUP";
   private static final String BATCH_PART_NO_A = "BATCH_PART_NO_A";
   private static final String BATCH_PART_NO_B = "BATCH_PART_NO_B";

   private static final double QTY = 200.0;
   private static final double QTY_A = 60.0;
   private static final double QTY_B = 80.0;

   private PartGroupKey iPartGroup;
   private static PartNoKey iPartNoA;
   private static PartNoKey iPartNoB;

   private PartGroupKey iBatchPartGroup;
   private static PartNoKey iBatchPartNoA;
   private static PartNoKey iBatchPartNoB;

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /*
    * Test data setup: 5 TRK inventory of which three are RFI and two are RFB. All five inventories
    * are from same part group connected to TRK config slot
    *
    * one serviceable BATCH inventory and one unservicealbe BATCH inventory are created which belong
    * to the same part group, and shipments to dock A and dock B are done, so as to test part group
    * availability with batch inventory in transit
    */
   @Before
   public void setup() throws MxException, TriggerException {

      HumanResourceKey lHr = Domain.createHumanResource();

      // create locations
      LocationKey lMainAirport = createLocation( loc -> {
         loc.setCode( MAIN_AIRPORT );
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

      // create a tracked config slot, part group
      ConfigSlotKey lTrkConfigSlot =
            new ConfigSlotBuilder( CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
                  .withStatus( RefConfigSlotStatusKey.ACTIVE ).isMandatory().build();

      iPartGroup = createPartGroup( PART_GROUP, lTrkConfigSlot, RefInvClassKey.TRK );

      // create tracked part in the same part group
      iPartNoA = createPartNo( PART_NO_A, iPartGroup, RefInvClassKey.TRK, RefPartStatusKey.ACTV );
      iPartNoB = createPartNo( PART_NO_B, iPartGroup, RefInvClassKey.TRK, RefPartStatusKey.ACTV );

      // three tracked inventory for part PART_NO_A with condition RFI
      createTrackedInventory( iPartGroup, iPartNoA, RefInvCondKey.RFI, true, lMainAirport, lOwner );
      createTrackedInventory( iPartGroup, iPartNoA, RefInvCondKey.RFI, true, lMainAirport, lOwner );
      createTrackedInventory( iPartGroup, iPartNoA, RefInvCondKey.RFI, true, lMainAirport, lOwner );

      // two tracked inventory for part PART_NO_B with condition RFB
      createTrackedInventory( iPartGroup, iPartNoB, RefInvCondKey.RFI, false, lMainAirport,
            lOwner );
      createTrackedInventory( iPartGroup, iPartNoB, RefInvCondKey.RFI, false, lMainAirport,
            lOwner );

      // create a consum config slot, batch part group
      ConfigSlotKey lConsumConfigSlot =
            new ConfigSlotBuilder( CONSUM_CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
                  .withStatus( RefConfigSlotStatusKey.ACTIVE ).isMandatory().build();

      iBatchPartGroup =
            createPartGroup( BATCH_PART_GROUP, lConsumConfigSlot, RefInvClassKey.BATCH );

      // create batch parts in the same part group
      iBatchPartNoA = createPartNo( BATCH_PART_NO_A, iBatchPartGroup, RefInvClassKey.BATCH,
            RefPartStatusKey.ACTV );
      iBatchPartNoB = createPartNo( BATCH_PART_NO_B, iBatchPartGroup, RefInvClassKey.BATCH,
            RefPartStatusKey.ACTV );

      // create serviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lServiceableBatchInventory = createBatchInventory( iBatchPartGroup,
            iBatchPartNoA, RefInvCondKey.RFI, QTY, lMainAirport, lOwner );

      createShipmentAndSend( lHr, lMainAirport, lLocationDockA, lServiceableBatchInventory, QTY_A );
      createShipmentAndSend( lHr, lMainAirport, lLocationDockB, lServiceableBatchInventory, QTY_B );

      // create unserviceable batch inventory with QTY, and send QTY_A to dock A and QTY_B to dock B
      InventoryKey lUnserviceableBatchInventory = createBatchInventory( iBatchPartGroup,
            iBatchPartNoB, RefInvCondKey.REPREQ, QTY, lMainAirport, lOwner );

      createShipmentAndSend( lHr, lMainAirport, lLocationDockA, lUnserviceableBatchInventory,
            QTY_A );
      createShipmentAndSend( lHr, lMainAirport, lLocationDockB, lUnserviceableBatchInventory,
            QTY_B );

   }


   /**
    * Create part group
    *
    * @param aPartGroupCd
    * @param aTrkConfigSlot
    * @param aRefInvClassKey
    * @param aPartNoKey
    * @return
    */
   private PartGroupKey createPartGroup( String aPartGroupCd, ConfigSlotKey aTrkConfigSlot,
         RefInvClassKey aRefInvClassKey ) {

      return Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setCode( aPartGroupCd );
         aPartGroup.setConfigurationSlot( aTrkConfigSlot );
         aPartGroup.setInventoryClass( aRefInvClassKey );
      } );
   }


   /**
    * Create Part No
    *
    * @param aOemPartNo
    * @param aPartGroup
    * @param aInvClass
    * @param aPartStatus
    * @return
    */
   private PartNoKey createPartNo( String aOemPartNo, PartGroupKey aPartGroup,
         RefInvClassKey aInvClass, RefPartStatusKey aPartStatus ) {
      return new PartNoBuilder().withOemPartNo( aOemPartNo ).isAlternateIn( aPartGroup )
            .withInventoryClass( aInvClass ).withStatus( aPartStatus ).build();
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
    * @param aPartNo
    * @param aInvCond
    * @param aQty
    * @param aLocation
    * @param aOwner
    * @return
    */
   private InventoryKey createBatchInventory( PartGroupKey aPartGroup, PartNoKey aPartNo,
         RefInvCondKey aInvCond, double aQty, LocationKey aLocation, OwnerKey aOwner ) {

      RefInvClassKey lInvClass = RefInvClassKey.BATCH;
      return new InventoryBuilder().withPartGroup( aPartGroup ).withPartNo( aPartNo )
            .withClass( lInvClass ).withCondition( aInvCond ).withBinQt( aQty ).withOwner( aOwner )
            .atLocation( aLocation ).build();
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
      new ShipmentLineBuilder( lShipment ).forPart( iBatchPartNoA ).forInventory( aBatchInventory )
            .withExpectedQuantity( aQty ).build();

      ShipmentService.send( lShipment, aHr, new Date(), new Date(), null, null );
   }


   /*
    * This test will test total availability of parts for following categories *** 1. serviceable
    * available *** *** 2. ready for build ***
    */
   @Test
   public void testPartGroupAvailabilityForTrackedPart() {

      DataSet lDataSet = getPartGroupAvailability( iPartGroup );
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
   public void testPartGroupAvailabilityForBatchPartInTransit() {

      DataSet lDataSet = getPartGroupAvailability( iBatchPartGroup );

      // loop over the first three rows while ignore the last one that is for installed inventories
      for ( int i = 0; i < 3; i++ ) {

         lDataSet.next();
         assertPartGroupAvailabilityAtLocation( lDataSet );
      }
   }


   private DataSet getPartGroupAvailability( PartGroupKey aPartGroupKey ) {
      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartGroupKey, new String[] { "aBomPartDbId", "aBomPartId" } );
      // execute query and return results
      DataSet lIds = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.part.PartGroupAvailability", lArgs );
      return lIds;
   }


   private static final double DELTA = 0.1;


   private void assertPartGroupAvailabilityAtLocation( DataSet lDataSet ) {

      String aLocationCd = lDataSet.getString( "loc_cd" );
      switch ( aLocationCd ) {
         case MAIN_AIRPORT:
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
