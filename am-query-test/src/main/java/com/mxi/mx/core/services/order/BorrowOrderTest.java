package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.shipment.PickShipmentTO;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.po.POLineTable;


/**
 * Ensures that all {@link Borrow Orders} work as expected
 */
public class BorrowOrderTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private HumanResourceKey hr;

   private PartNoKey partNo;

   private InventoryKey borrowedInv;

   private PurchaseOrderKey borrowOrder;

   private PurchaseOrderLineKey borrowOrderLine;

   private static final String ACCOUNT_CD = "ACCOUNT";

   private static final String INV_SERIAL_NO = "INV SERIAL NO";


   /**
    * Test re-issuing a borrow order that has already been received should not create a new inbound
    * shipment
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testReissueReceivedOrder() throws Exception {

      // DATA SETUP: Create manufacture
      ManufacturerKey manufacturer =
            Domain.createManufacturer( manufact -> manufact.setCode( "MANUFACT" ) );

      // DATA SETUP: Create a part
      partNo = Domain.createPart( part -> {
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setCode( "Part No OEM" );
         part.setShortDescription( "Part Description" );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setPartStatus( RefPartStatusKey.ACTV );
         part.setManufacturer( manufacturer );
      } );

      // DATA SETUP: Create a location
      LocationKey shippingToLoc = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
      } );

      LocationKey vendorLoc = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.VENDOR );
      } );

      LocationKey inventoryLoc = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
      } );

      // DATA SETUP: Create a financial account
      FncAccountKey account = new AccountBuilder().withCode( ACCOUNT_CD )
            .withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // DATA SETUP: Create a vendor
      VendorKey vendor = new VendorBuilder().atLocation( vendorLoc ).build();

      // DATA SETUP: Create an owner
      OwnerKey owner = Domain.createOwner( ownerBuilder -> {
         ownerBuilder.setLocalBool( false );
      } );

      // DATA SETUP: Create an inventory
      borrowedInv = Domain.createSerializedInventory( inv -> {
         inv.setPartNumber( partNo );
         inv.setSerialNumber( INV_SERIAL_NO );
         inv.setCondition( RefInvCondKey.RFI );
         inv.setLocation( inventoryLoc );
      } );

      // DATA SETUP: Create a borrow order
      borrowOrder = Domain.createPurchaseOrder( purchaseOrder -> {
         purchaseOrder.orderType( RefPoTypeKey.BORROW );
         purchaseOrder.status( RefEventStatusKey.POAUTH );
         purchaseOrder.vendor( vendor );
         purchaseOrder.shippingTo( shippingToLoc );
      } );

      // DATA SETUP: Create a borrow order line
      borrowOrderLine = Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( borrowOrder );
         poLine.lineType( RefPoLineTypeKey.BORROW );
         poLine.part( partNo );
         poLine.unitType( RefQtyUnitKey.EA );
         poLine.orderQuantity( new BigDecimal( 1 ) );
         poLine.account( account );
         poLine.owner( owner );
         poLine.unitPrice( new BigDecimal( 100 ) );
      } );

      // DATA SETUP: Issue the borrow order
      ShipmentKey inboundShipment = new OrderService().issue( borrowOrder, null, hr );

      // Asserts an inbound shipment was created.
      assertNotNull( inboundShipment );

      // DATA SETUP: Receive the inbound shipment
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( ShipmentService.getShipmentLines( inboundShipment )[0] );
      lReceiveShipmentLineTO.setPartNo( partNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( INV_SERIAL_NO );

      ShipmentService.receive( inboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, hr );

      // DATA SETUP: Inspect the inventory as servicable
      new DefaultConditionService().inspectInventory( borrowedInv, new InspectInventoryTO(), hr );

      // Asserts an outbound shipment was created
      ShipmentKey outboundShipment =
            POLineTable.findByPrimaryKey( borrowOrderLine ).getXchgShipment();

      assertNotNull( outboundShipment );

      // DATA SETUP: Send the outbound shipment
      PickShipmentTO pickShipmentTO = new PickShipmentTO( outboundShipment );
      pickShipmentTO.addPickedItem( new PickedItem( borrowedInv, 1, null ) );

      ShipmentService.pickShipment( pickShipmentTO, hr );

      ShipmentService.send( outboundShipment, hr, new Date(), new Date(), null, null );

      // Asserts that the inventory's condition is ARCHIVE and locked
      InvInvTable invInvTable = InvInvTable.findByPrimaryKey( borrowedInv );
      assertTrue( invInvTable.isLocked() );
      assertEquals( RefInvCondKey.ARCHIVE, invInvTable.getInvCond() );

      // Asserts the po status is RECEIVED
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable evtEventTable = evtEventDao.findByPrimaryKey( borrowOrder.getEventKey() );

      assertEquals( RefEventStatusKey.PORECEIVED, evtEventTable.getEventStatus() );

      // Update base unit price to trigger re-issuing process
      EditOrderLineTO editOrderLineTO = new EditOrderLineTO( borrowOrderLine );
      editOrderLineTO.setBaseUnitPrice( new BigDecimal( 500 ) );
      editOrderLineTO.setQuantity( new Double( 1 ), "quantity" );
      editOrderLineTO.setAccountCode( ACCOUNT_CD, "account" );
      editOrderLineTO.setQuantityUnit( RefQtyUnitKey.EA, "quantity unit" );

      OrderService.setPOLines( borrowOrder, new EditOrderLineTO[] { editOrderLineTO }, hr );

      // Asserts the borrow order status is now AUTH
      evtEventDao.refresh( evtEventTable );
      assertEquals( RefEventStatusKey.POAUTH, evtEventTable.getEventStatus() );

      // Re-issue the borrow order
      ShipmentKey newInboundShipment = new OrderService().issue( borrowOrder, null, hr );

      // Asserts that there is no new inbound shipment
      assertNull( newInboundShipment );
   }


   @Before
   public void setup() {

      int currentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      // DATA SETUP: Create a human resource
      hr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUserId( currentUserId );
      } ) ) );

      UserParametersStub userParametersStub = new UserParametersStub( currentUserId, "LOGIC" );
      userParametersStub.setBoolean( "ALLOW_AUTO_COMPLETION", true );
      UserParameters.setInstance( currentUserId, "LOGIC", userParametersStub );
   }
}
