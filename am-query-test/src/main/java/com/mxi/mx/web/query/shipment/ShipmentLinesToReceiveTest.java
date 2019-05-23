package com.mxi.mx.web.query.shipment;

import static com.mxi.mx.core.key.RefShipSegmentStatusKey.PENDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.PartAdvisory;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.shipment.ShipmentService;


public final class ShipmentLinesToReceiveTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey fromLocation;

   private LocationKey toLocation;

   private HumanResourceKey hr;

   private PartAdvisory partAdvisory;

   private final String SERIAL_NO_OEM_1 = "Serial No 1";

   private final String SERIAL_NO_OEM_2 = "Serial No 2";

   private final String SERIAL_NO_OEM_3 = "Serial No 3";

   private final String PART_NO_OEM_1 = "PART NO OEM 1";

   private final String PART_NO_OEM_2 = "PART NO OEM 2";

   private final String ADVISORY_NAME = "Advisory Name";


   /**
    * Test the vendor part advisory content when receiving an inbound shipment of a purchase order
    */
   @Test
   public void testNewPartWithVendorAdvisory() throws Exception {

      // DATA SETUP: Create a part
      PartNoKey partNo = Domain.createPart( part -> {
         part.setCode( PART_NO_OEM_1 );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setPartStatus( RefPartStatusKey.ACTV );

      } );

      PartNoKey partNo2 = Domain.createPart( part -> {
         part.setCode( PART_NO_OEM_2 );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setPartStatus( RefPartStatusKey.ACTV );
      } );

      // DATA SETUP: Create a location
      LocationKey vendorLoc = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.VENDOR ) );

      // DATA SETUP: Create vendor
      VendorKey vendor = Domain.createVendor( vendorBuilder -> {
         vendorBuilder.setLocation( vendorLoc );
         vendorBuilder.addVendorPartAdvisory( partNo, partAdvisory );
         vendorBuilder.addVendorPartAdvisory( partNo2, null );
      } );

      // DATA SETUP: Create a purchase order
      PurchaseOrderKey purchaseOrder = Domain.createPurchaseOrder( order -> {
         order.orderType( RefPoTypeKey.PURCHASE );
         order.status( RefEventStatusKey.POAUTH );
         order.vendor( vendor );
         order.setOrganization( OrgKey.ADMIN );
         order.shippingTo( toLocation );
      } );

      // DATA SETUP: Create a financial account
      FncAccountKey account = new AccountBuilder().build();

      // DATA SETUP: Create a borrow order line
      Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( purchaseOrder );
         poLine.lineType( RefPoLineTypeKey.PURCHASE );
         poLine.part( partNo );
         poLine.unitType( RefQtyUnitKey.EA );
         poLine.orderQuantity( BigDecimal.ONE );
         poLine.account( account );
      } );

      Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( purchaseOrder );
         poLine.lineType( RefPoLineTypeKey.PURCHASE );
         poLine.part( partNo );
         poLine.unitType( RefQtyUnitKey.EA );
         poLine.orderQuantity( BigDecimal.ONE );
         poLine.account( account );
      } );

      Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( purchaseOrder );
         poLine.lineType( RefPoLineTypeKey.PURCHASE );
         poLine.part( partNo2 );
         poLine.unitType( RefQtyUnitKey.EA );
         poLine.orderQuantity( BigDecimal.ONE );
         poLine.account( account );
      } );

      // DATA SETUP: Issue the borrow order
      ShipmentKey inboundShipment = new OrderService().issue( purchaseOrder, null, hr );

      QuerySet qs = executeQuery( inboundShipment, RefPoTypeKey.PURCHASE.getCd() );

      assertEquals( 3, qs.getRowCount() );

      while ( qs.next() ) {

         if ( qs.getString( "part_no_oem" ).equals( PART_NO_OEM_1 ) ) {

            assertEquals( ADVISORY_NAME, qs.getString( "advsry_name" ) );

         } else if ( qs.getString( "part_no_oem" ).equals( PART_NO_OEM_2 ) ) {

            assertTrue( qs.isNull( "advsry_name" ) );
         } else {

            fail( "Unexpected shipment line" );
         }
      }
   }


   /**
    * Test the part advisory content when receiving a shipment that already has inventory selected
    */
   @Test
   public void testInventoryExistsWithPartAdvisory() throws Exception {

      // DATA SETUP: Create part
      PartNoKey partNoWithAdvisory = Domain.createPart( part -> {
         part.addAdvisory( partAdvisory );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setManufacturer( Domain.createManufacturer() );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      PartNoKey partNo = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.SER );
         part.setManufacturer( Domain.createManufacturer() );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      // DATA SETUP: Create an inventory
      InventoryKey inventory1 = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( partNoWithAdvisory );
         inventory.setSerialNumber( SERIAL_NO_OEM_1 );
         inventory.setLocation( fromLocation );
      } );

      InventoryKey inventory2 = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( partNoWithAdvisory );
         inventory.setSerialNumber( SERIAL_NO_OEM_2 );
         inventory.setLocation( fromLocation );
      } );

      InventoryKey inventory3 = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( partNo );
         inventory.setSerialNumber( SERIAL_NO_OEM_3 );
         inventory.setLocation( fromLocation );
      } );

      // DATA SETUP: Create a shipment
      ShipmentKey shipment = Domain.createShipment( shipmentBuilder -> {
         shipmentBuilder.addShipmentSegment( segment -> {
            segment.setStatus( PENDING );
            segment.setFromLocation( fromLocation );
            segment.setToLocation( toLocation );
         } );
      } );

      // DATA SETUP: Add part line to shipment
      ShipmentLineKey shipmentLine1 = Domain.createShipmentLine( shipmentLine -> {
         shipmentLine.shipmentKey( shipment );
         shipmentLine.part( partNoWithAdvisory );
         shipmentLine.expectedQuantity( 1.0 );
         shipmentLine.inventory( inventory1 );
         shipmentLine.serialNo( SERIAL_NO_OEM_1 );
      } );

      ShipmentLineKey shipmentLine2 = Domain.createShipmentLine( shipmentLine -> {
         shipmentLine.shipmentKey( shipment );
         shipmentLine.part( partNoWithAdvisory );
         shipmentLine.expectedQuantity( 1.0 );
         shipmentLine.inventory( inventory2 );
         shipmentLine.serialNo( SERIAL_NO_OEM_2 );
      } );

      ShipmentLineKey shipmentLine3 = Domain.createShipmentLine( shipmentLine -> {
         shipmentLine.shipmentKey( shipment );
         shipmentLine.part( partNo );
         shipmentLine.expectedQuantity( 1.0 );
         shipmentLine.inventory( inventory3 );
         shipmentLine.serialNo( SERIAL_NO_OEM_3 );
      } );

      // DATA SETUP: Send shipment
      ShipmentService.send( shipment, hr, new Date(), new Date(), null, null );

      QuerySet qs = executeQuery( shipment, null );

      assertEquals( 3, qs.getRowCount() );

      while ( qs.next() ) {

         ShipmentLineKey currShipmentLine = qs.getKey( ShipmentLineKey.class, "shipment_line_key" );

         if ( shipmentLine1.equals( currShipmentLine ) ) {

            assertEquals( ADVISORY_NAME, qs.getString( "advsry_name" ) );
            assertEquals( SERIAL_NO_OEM_1, qs.getString( "serial_no_oem" ) );
         } else if ( shipmentLine2.equals( currShipmentLine ) ) {

            assertEquals( ADVISORY_NAME, qs.getString( "advsry_name" ) );
            assertEquals( SERIAL_NO_OEM_2, qs.getString( "serial_no_oem" ) );
         } else if ( shipmentLine3.equals( currShipmentLine ) ) {

            assertTrue( qs.isNull( "advsry_name" ) );
            assertEquals( SERIAL_NO_OEM_3, qs.getString( "serial_no_oem" ) );
         } else {

            fail( "Unexpected shipment line" );
         }
      }
   }


   private QuerySet executeQuery( ShipmentKey shipmentKey, String typeCode ) {

      DataSetArgument args = new DataSetArgument();
      args.add( shipmentKey, "aShipmentDbId", "aShipmentId" );
      args.add( "aTypeCode", typeCode );

      QuerySet qs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), args );

      return qs;
   }


   @Before
   public void setup() {

      // DATA SETUP: Create a human resource
      hr = Domain.createHumanResource();

      // DATA SETUP: Create location
      fromLocation = Domain.createLocation();

      toLocation = Domain.createLocation( location -> location.setType( RefLocTypeKey.DOCK ) );

      // DATA SETUP: Create a part with part advisory
      partAdvisory = new PartAdvisory();
      partAdvisory.setName( ADVISORY_NAME );
   }
}
