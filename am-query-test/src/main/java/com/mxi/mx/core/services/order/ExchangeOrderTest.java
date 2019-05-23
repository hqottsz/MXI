package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.misc.OrderOwnership;
import com.mxi.mx.core.services.inventory.InventoryAPI;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentDirection;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.services.shipment.message.MessageReceivedPart;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.po.PoLine;


/**
 * Ensures that all {@link Exchange Orders} work as expected
 */
public class ExchangeOrderTest {

   private static final String USERNAME_TESTUSER = "testuser";
   private static final LocationKey ARCHIVED_LOC = new LocationKey( 0, 1002 );
   private static final int USERID_TESTUSER = 999;
   private static final String SER_INV_SN = "TEST123";
   private static final String SER_INV_SN2 = "TEST123";
   private static final String SER_INV_DUPLICATE_SN = "TEST123";
   private static final String SER_INV_DUPLICATE2_SN = "TEST234";
   private static final String INVASSET_ACCOUNT = "INVASSET";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iVendorLocation;
   private HumanResourceKey iHr;
   private VendorKey iVendor;
   private OwnerKey iLocalOwner;
   private OwnerKey iVendorOwner;
   private OrgKey iReceiptOrganization;
   private OrderService iOrderService;
   private FncAccountKey iLocalAccount;
   private LocationKey iSupplyLocation;
   private LocationKey iDockLocation;
   private PartNoKey iPartNo;

   private HumanResourceKey iHR = null;


   @Before
   public void loadData() throws Exception {

      iVendorLocation = Domain.createLocation( aVendorLocation -> {
         aVendorLocation.setCode( "TESTVENLOC" );
         aVendorLocation.setType( RefLocTypeKey.VENDOR );
      } );

      iLocalOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( "LOCAL OWNER" );
         aOwner.setName( "LOCAL" );
         aOwner.setLocalBool( true );
      } );

      iVendorOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( "TESTVENDOR" );
         aOwner.setName( "TESTVENDORL" );
         aOwner.setLocalBool( false );
      } );

      iReceiptOrganization = new InvOwnerTable( iLocalOwner ).getOrgKey();

      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation )
            .withOwner( iVendorOwner ).withVendorType( RefVendorTypeKey.REPAIR ).build();

      iHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUsername( USERNAME_TESTUSER );
         aUser.setUserId( USERID_TESTUSER );
      } ) ) );

      iLocalAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( INVASSET_ACCOUNT ).isDefault().build();

      // For core inventory creation to lookup the user ID
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // an exception is thrown about a missing parameter if this is not defined manually
      UserParametersStub lSecuredResourceUserParms =
            new UserParametersStub( USERID_TESTUSER, "SECURED_RESOURCE" );
      lSecuredResourceUserParms
            .setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE", true );
      lSecuredResourceUserParms
            .setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY", true );
      UserParameters.setInstance( USERID_TESTUSER, "SECURED_RESOURCE", lSecuredResourceUserParms );

      UserParametersStub lLogicUserParms = new UserParametersStub( USERID_TESTUSER, "LOGIC" );
      lLogicUserParms.setBoolean( "ALLOW_AUTO_COMPLETION", true );
      UserParameters.setInstance( USERID_TESTUSER, "LOGIC", lLogicUserParms );

      iSupplyLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.AIRPORT );
         aLocation.setIsSupplyLocation( true );
      } );

      iDockLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
         aLocation.setSupplyLocation( iSupplyLocation );
      } );

      // Create serialized part number
      iPartNo = Domain.createPart( aPart -> {

         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
         aPart.setPartStatus( RefPartStatusKey.ACTV );
         aPart.setShortDescription( "TEST" );

      } );

      iOrderService = new OrderService();

   }


   /**
    * Test sending and receiving SER inventory on an exchange order, then un-archiving the inventory
    * which was received at the vendor location
    *
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void sendSerInvAndUnarchiveExchangeReturnInv() throws Exception {

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );
         // This is important. The PO line must be vendor owned to match application GUI logic
         aPoLine.owner( iVendorOwner );

      } );

      // Issue the EO to generate the inbound shipment
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( lInboundShipment )[0];

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO },
            new OrderOwnership( RefPoLineTypeKey.EXCHANGE, ShipmentDirection.INBOUND ), iHr );

      InventoryKey lReveivedInventory = ShipmentService.getInventory( lInboundShipment )[0];

      // Assert that the received has is ownership type EXCHRCVD
      InvInv lInvInv = new InvInv( lReveivedInventory );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRCVD.getCd() );
      lInvInv.assertOemSerialNumber( SER_INV_SN );
      lInvInv.assertCondCd( RefInvCondKey.INSPREQ.getCd() );

      // create local inventory to be sent to vendor
      InventoryKey lReturnInv = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_SN2 );
         aInventory.setCondition( RefInvCondKey.INSPREQ );
         aInventory.setFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );
      } );

      // set the local inventory for outbound return on the exchange order
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lEOLine, lReturnInv,
            iHr );

      // inspect the received inventory as servicable
      InspectInventoryTO lInspectInvTO = new InspectInventoryTO();
      lInspectInvTO.setMode( InspectInventoryTO.MODE_INSPECT_AS_SERVICEABLE );
      new DefaultConditionService().inspectInventory( lReveivedInventory, lInspectInvTO, iHr );

      // Assert that the received has a financial status of Inspected
      lInvInv = new InvInv( lReveivedInventory );
      lInvInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.LOCAL.getCd() );
      lInvInv.assertCondCd( RefInvCondKey.RFI.getCd() );

      // Assert that the to-be returned inventory is now Ownership Type EXCHRTRN
      InvInv lReturnedInv = new InvInv( lReturnInv );
      lReturnedInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRTRN.getCd() );

      // get the outbound shipment line
      ShipmentKey lXchgShipment = new PoLine( lEOLine ).getXchgShipment();

      // Send the outbound shipment to the vendor
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Assert that the returned inventory is properly vendor owned
      lInvInv = new InvInv( lReturnInv );
      lInvInv.assertLocation( ARCHIVED_LOC );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRTRN.getCd() );
      lInvInv.assertOwner( iVendorOwner );

      // Un-archive the Exchange Returned inventory and bring it to a local supply location
      // Calling via the API will automatically detect this situation and change ownership to OTHER
      new InventoryAPI( lReturnInv ).reinductExchangeReturnInventory( iDockLocation,
            RefInvCondKey.RFI, null, iHr, null, null );

      // Check that ownership type has changed to OTHER
      InvInv lUnarchivedInv = new InvInv( lReturnInv );
      lUnarchivedInv.assertOwnerTypeCd( RefOwnerTypeKey.OTHER.getCd() );

   }


   /**
    * Test sending and receiving back the same inventory using an exchange order
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void sendSerInvAndReceiveSameInvBackThroughInboundShipment() throws Exception {

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // create local inventory and assign to the EO
      InventoryKey lReturnInv = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_SN );
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setOrderLine( lEOLine );

      } );

      // set the local inventory for outbound return on the exchange order
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lEOLine, lReturnInv,
            iHr );

      // get the outbound shipment line
      ShipmentKey lXchgShipment = new PoLine( lEOLine ).getXchgShipment();

      // Send the outbound shipment to the vendor
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Issue the EO to generate the inbound shipment
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine = null;
      for ( ShipmentLineKey lCurrentLine : ShipmentService.getShipmentLines( lInboundShipment ) ) {
         lInboundShipmentLine = lCurrentLine;
         break;
      }

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      InventoryKey[] lInventory = ShipmentService.getInventory( lInboundShipment );

      // inspect the received inventory
      new DefaultConditionService().inspectInventory( lInventory[0], new InspectInventoryTO(),
            iHr );

      // Asserts the received inventory's property
      InvInv lInvInv = new InvInv( lInventory[0] );
      lInvInv.assertLocation( iDockLocation );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.LOCAL.getCd() );
      lInvInv.assertOwner( iLocalOwner );

   }


   /**
    * Test receiving inventory and then sending the same inventory back to the vendor
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void receiveSerInvAndSendSameInvBackThroughOutboundShipment() throws Exception {

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );
         aPoLine.owner( iLocalOwner );

      } );

      // Issue the EO to generate the inbound shipment
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( lInboundShipment )[0];

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      InventoryKey lReveivedInventory = ShipmentService.getInventory( lInboundShipment )[0];

      // inspect the received inventory as servicable
      new DefaultConditionService().inspectInventory( lReveivedInventory, new InspectInventoryTO(),
            iHr );

      // Assert that the returned has a financial status of Inspected
      InvInv lInvInv = new InvInv( lReveivedInventory );
      lInvInv.assertFinanceStatusCd( InvInvTable.FinanceStatusCd.INSP );

      // set the local inventory for outbound return on the exchange order
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lEOLine,
            lReveivedInventory, iHr );

      // get the outbound shipment line
      ShipmentKey lXchgShipment = new PoLine( lEOLine ).getXchgShipment();

      // Send the outbound shipment to the vendor
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Assert that the returned inventory is properly vendor owned
      lInvInv = new InvInv( lReveivedInventory );
      lInvInv.assertLocation( ARCHIVED_LOC );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRCVD.getCd() );
      lInvInv.assertOwner( iLocalOwner );
   }


   /**
    * Test sending inventory first and then receiving un-related duplicate loose inventory back from
    * the vendor. This should trigger the shipment warning about loose inventory already existing
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void sendSerInvAndReceiveDuplicateLooseInvBackThroughInboundShipment() throws Exception {

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // create local inventory to be sent to vendor
      InventoryKey lReturnInv = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_SN );
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setOrderLine( lEOLine );

      } );

      // create local inventory, not related to this exchange order
      Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_DUPLICATE_SN );
         aInventory.setCondition( RefInvCondKey.RFI );

      } );

      // set the local inventory for outbound return on the exchange order
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lEOLine, lReturnInv,
            iHr );

      // get the outbound shipment line
      ShipmentKey lXchgShipment = new PoLine( lEOLine ).getXchgShipment();

      // Send the outbound shipment to the vendor
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Issue the EO to generate the inbound shipment
      OrderService.overrideAuthorization( iExchangeOrder, "note", iHr );
      ShipmentKey lInboundShipment = iOrderService.issue( iExchangeOrder, "note", iHr );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine = null;
      for ( ShipmentLineKey lCurrentLine : ShipmentService.getShipmentLines( lInboundShipment ) ) {
         lInboundShipmentLine = lCurrentLine;
         break;
      }

      // Create shipment line transfer object where the vendor returns duplicate inventory to you
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_DUPLICATE_SN );

      // Shipment warnings should be seen.
      MessageReceivedPart[] lMessageReceivedPart = ShipmentService.getReceiveWarnings(
            lInboundShipment, new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, iHr );

      // Shipment warnings should be returned in this scenario
      assertEquals( true, ( lMessageReceivedPart != null ) && ( lMessageReceivedPart.length > 0 ) );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      InventoryKey[] lInventory = ShipmentService.getInventory( lInboundShipment );

      // inspect the received inventory
      new DefaultConditionService().inspectInventory( lInventory[0], new InspectInventoryTO(),
            iHr );

      // Asserts the received inventory's property
      InvInv lInvInv = new InvInv( lInventory[0] );
      lInvInv.assertLocation( iDockLocation );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRCVD.getCd() );
      lInvInv.assertOwner( iLocalOwner );

   }


   /**
    * Test sending serial inventory out to a vendor in one exchange order, then receiving that
    * inventory in a second exchange order while the first exchange order is still open
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Ignore
   @Test
   public void sendSerInvAndReceiveDuplicateLooseInvBackThroughSecondEOInboundShipment()
         throws Exception {

      // Create an exchange order 1
      PurchaseOrderKey lExchangeOrderKey1 = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // Exchange order line key 1
      PurchaseOrderLineKey lExchangeOrderLineKey1 = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( lExchangeOrderKey1 );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // Create an exchange order 2
      PurchaseOrderKey lExchangeOrderKey2 = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      PurchaseOrderLineKey lExchangeOrderLineKey2 = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( lExchangeOrderKey2 );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // Authorize the orders
      OrderService.overrideAuthorization( lExchangeOrderKey1, "note", iHr );
      OrderService.overrideAuthorization( lExchangeOrderKey2, "note", iHr );

      // Issue the orders
      iOrderService.issue( lExchangeOrderKey1, "note", iHr );
      ShipmentKey iInboundShipmentKey2 = iOrderService.issue( lExchangeOrderKey2, "note", iHr );

      // Create local SER Inventory
      InventoryKey lSentSerInvKey = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_DUPLICATE_SN );
         aInventory.setCondition( RefInvCondKey.RFI );

      } );

      // Select outgoing broken inventory to return to vendor using exchange order 1
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lExchangeOrderLineKey1,
            lSentSerInvKey, iHr );

      // Select the returned inventory (the received inventory) for the shipment
      ShipmentKey lXchgShipment = new PoLine( lExchangeOrderLineKey1 ).getXchgShipment();

      // Send the outbound shipment to the vendor
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Get the inbound shipment line for this part
      ShipmentLineKey lInboundShipmentLine =
            ShipmentService.getShipmentLines( iInboundShipmentKey2 )[0];

      // Create shipment line transfer object where the vendor returns duplicate inventory to you
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.RFI, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_DUPLICATE_SN );

      // Receive shipment of exchange order 2 (Inventory sent in exchange order 1)
      MessageReceivedPart[] lMessageReceivedPart = ShipmentService.getReceiveWarnings(
            iInboundShipmentKey2, new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, iHr );

      // No Shipment warnings should be returned in this scenario
      assertEquals( true,
            ( lMessageReceivedPart == null ) || ( lMessageReceivedPart.length == 0 ) );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( iInboundShipmentKey2, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      // Get the inventory from the inbound shipment
      InventoryKey lInventory = ShipmentService.getInventory( iInboundShipmentKey2 )[0];

      // Inspect as serviceable
      new DefaultConditionService().inspectInventory( lInventory, new InspectInventoryTO(), iHR );

      // Check that ownership is local, inspected, and the inventory can be used again
      InvInv lInvInv = new InvInv( lInventory );
      lInvInv.assertLocation( iDockLocation );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.LOCAL.getCd() );
      lInvInv.assertFinanceStatusCd( FinanceStatusCd.INSP );
      lInvInv.assertOwner( iLocalOwner );

   }


   /**
    * Test sending serial inventory out to a vendor in one exchange order. Complete the first order,
    * then receiving that inventory in a second exchange order. The ownership of the inventory
    * received should not be Exchange Received
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void sendSerInvAndReceiveInSecondOrderInboundShipment() throws Exception {

      // Create an exchange order 1
      PurchaseOrderKey lExchangeOrderKey1 = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // Exchange order line key 1
      PurchaseOrderLineKey lExchangeOrderLineKey1 = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( lExchangeOrderKey1 );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // Authorize the order 1
      OrderService.overrideAuthorization( lExchangeOrderKey1, "note", iHr );

      // Issue the order 1
      ShipmentKey iInboundShipmentKey1 = iOrderService.issue( lExchangeOrderKey1, "note", iHr );

      // Create local SER Inventory
      InventoryKey lSentSerInvKey = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( iPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( iDockLocation );
         aInventory.setSerialNumber( SER_INV_SN );
         aInventory.setCondition( RefInvCondKey.RFI );

      } );

      // Select outgoing broken inventory to return to vendor using exchange order 1
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lExchangeOrderLineKey1,
            lSentSerInvKey, iHr );

      // Select the returned inventory (the received inventory) for the shipment
      ShipmentKey lXchgShipment = new PoLine( lExchangeOrderLineKey1 ).getXchgShipment();

      // Send shipment (Exchange order 1)
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Receive Inbound shipment (Exchange order 1)
      ReceiveShipmentLineTO lReceiveShipmentLineTO = new ReceiveShipmentLineTO(
            ShipmentService.getShipmentLines( iInboundShipmentKey1 )[0] );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( iInboundShipmentKey1, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      // Get the inventory from the inbound shipment
      InventoryKey lInventory = ShipmentService.getInventory( iInboundShipmentKey1 )[0];

      // Inspect as servicable
      new DefaultConditionService().inspectInventory( lInventory, new InspectInventoryTO(), iHr );

      // Check that ownership is local, inspected, and the inventory can be used again
      InvInv lInvInv = new InvInv( lInventory );
      lInvInv.assertLocation( iDockLocation );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.LOCAL.getCd() );
      lInvInv.assertFinanceStatusCd( FinanceStatusCd.INSP );
      lInvInv.assertOwner( iLocalOwner );

      // Exchange Order 1 is now complete

      // Create an exchange order 2
      PurchaseOrderKey lExchangeOrderKey2 = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( iDockLocation );

      } );

      // Exchange order line key 2
      PurchaseOrderLineKey lExchangeOrderLineKey2 = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( lExchangeOrderKey2 );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( iPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( 1 ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // Authorize the order 2
      OrderService.overrideAuthorization( lExchangeOrderKey2, "note", iHr );

      // Issue the order 2
      ShipmentKey iInboundShipmentKey2 = iOrderService.issue( lExchangeOrderKey2, "note", iHr );

      // Select outgoing broken inventory to return to vendor using exchange order 2 (Same inv from
      // Exchange Order 1)
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lExchangeOrderLineKey2,
            lSentSerInvKey, iHr );

      // Select the returned inventory (the received inventory) for the shipment
      lXchgShipment = new PoLine( lExchangeOrderLineKey2 ).getXchgShipment();

      // Send shipment (Exchange order 1)
      ShipmentService.send( lXchgShipment, iHr, new Date(), new Date(), "", "" );

      // Receive the inbound shipment from the vendor using the SN from the previous EO
      lReceiveShipmentLineTO = new ReceiveShipmentLineTO(
            ShipmentService.getShipmentLines( iInboundShipmentKey2 )[0] );
      lReceiveShipmentLineTO.setPartNo( iPartNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( iInboundShipmentKey2, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, iHr );

      // Get the inventory from the inbound shipment
      lInventory = ShipmentService.getInventory( iInboundShipmentKey2 )[0];

      // Inspect as servicable
      new DefaultConditionService().inspectInventory( lInventory, new InspectInventoryTO(), iHr );

      // Check that ownership is local, inspected, and the inventory can be used again
      lInvInv = new InvInv( lInventory );
      lInvInv.assertLocation( iDockLocation );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.LOCAL.getCd() );
      lInvInv.assertFinanceStatusCd( FinanceStatusCd.INSP );
      lInvInv.assertOwner( iLocalOwner );

   }

}
