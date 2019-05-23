
package com.mxi.mx.core.services.shipment.routing;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipSegmentBuilder;
import com.mxi.am.domain.builder.ShipSegmentMapBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.unittest.table.ship.ShipSegment;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;


/**
 * This class tests the shipment routing service.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ShipmentRoutingServiceTest {

   private LocationKey iDockLocation;

   private HumanResourceKey iHr;

   private LocationKey iSupplyLocation;

   private LocationKey iVendorLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test the case where a batch part is received on a ship segment, inventory will not be set at
    * that point.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testReceiveNewBatch() throws Exception {

      // create a new BATCH part number
      PartNoKey lBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( BigDecimal.ONE )
            .withTotalValue( BigDecimal.ONE ).withRepairBool( true ).build();

      // create a shipment
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iVendorLocation )
            .toLocation( iDockLocation ).withType( RefShipmentTypeKey.REPAIR ).build();

      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLine = new ShipmentLineBuilder( lShipment ).forPart( lBatchPart )
            .withExpectedQuantity( 5.0 ).build();

      // create 2 segments
      ShipSegmentKey lShipSegment1 = new ShipSegmentBuilder().fromLocation( iVendorLocation )
            .toLocation( iSupplyLocation ).withStatus( RefShipSegmentStatusKey.PENDING ).build();

      ShipSegmentKey lShipSegment2 = new ShipSegmentBuilder().fromLocation( iSupplyLocation )
            .toLocation( iDockLocation ).withStatus( RefShipSegmentStatusKey.PLAN ).build();

      // create a map to link segments to shipment
      new ShipSegmentMapBuilder().withShipment( lShipment ).withSegment( lShipSegment1 )
            .withOrder( 1 ).build();

      new ShipSegmentMapBuilder().withShipment( lShipment ).withSegment( lShipSegment2 )
            .withOrder( 2 ).build();

      // setup the shipment segment transfer object
      ReceiveShipmentSegmentTO lTO = new ReceiveShipmentSegmentTO();
      lTO.setShipmentKey( lShipment );
      lTO.setFinalDestination( false );
      lTO.setReceivedDate( new Date() );

      // receive the first segment
      ShipmentRoutingService lShipmentRoutingService = new ShipmentRoutingService( lShipment );
      lShipmentRoutingService.receive( lTO, iHr );

      // assert inventory is not set and segment is completed
      new ShipShipmentLine( lShipmentLine ).assertInventory( null );
      new ShipSegment( lShipSegment1 ).assertStatus( RefShipSegmentStatusKey.COMPLETE );
   }


   @Before
   public void loadData() throws Exception {
      // create a human resource
      iHr = new HumanResourceDomainBuilder().build();

      // create a vendor location
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();
   }
}
