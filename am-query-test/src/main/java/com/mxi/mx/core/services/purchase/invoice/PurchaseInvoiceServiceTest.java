package com.mxi.mx.core.services.purchase.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.facade.order.OrderManagerFacade;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.misc.OrderOwnership;
import com.mxi.mx.core.model.order.OrderModifier;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.config.DefaultChangePartNoService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.order.OrderDetailsTO;
import com.mxi.mx.core.services.order.OrderLineTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentDirection;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoInvoiceLine;
import com.mxi.mx.core.table.ref.RefCurrency;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This class tests the {@link PurchaseInvoiceService}.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PurchaseInvoiceServiceTest {

   private static final int USERID_TESTUSER = 999;
   private static final String USERNAME_TESTUSER = "testuser";

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartNoKey iAltPart;

   private FncAccountKey iAssetAccount;

   private RefCurrencyKey iBitCoinCurrency;

   private HumanResourceKey iHr;

   private PartNoKey iPart;

   private PartGroupKey iPartGroup;

   private VendorKey iVendor;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Given a received PO for a part and an inventory that has been updated to be an alternate part,
    * when the invoice is created, the part number is that of the alternate part.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testCreateInvoiceAfterPartChange() throws Exception {
      OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
      lPODetailsTO.setChargeToAcc( iAssetAccount );
      lPODetailsTO.setCurrency( iBitCoinCurrency );
      lPODetailsTO.setExchangeRate( new BigDecimal( 1.31172 ) );
      lPODetailsTO.setOrganization( OrgKey.ADMIN );
      lPODetailsTO.setPartNo( iPart );
      lPODetailsTO.setPoLineType( RefPoLineTypeKey.PURCHASE );
      lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );
      lPODetailsTO.setQtyUnit( RefQtyUnitKey.EA );
      lPODetailsTO.setQuantity( 1.0 );
      lPODetailsTO.setShipToLocation( "DOCK" );
      lPODetailsTO.setUnitPrice( new BigDecimal( 696.81 ) );
      lPODetailsTO.setVendor( iVendor );
      lPODetailsTO.setInspectionRequired( true );

      PurchaseOrderKey lOrderKey = OrderService.create( lPODetailsTO, ( String ) null, iHr );

      OrderLineTO lOrderLineTO = new OrderLineTO();
      lOrderLineTO.setBorrowRate( lPODetailsTO.getBorrowRate() );
      lOrderLineTO.setChargeToAcc( lPODetailsTO.getChargeToAcc() );
      lOrderLineTO.setOwner( lPODetailsTO.getOwner() );
      lOrderLineTO.setPoLineType( lPODetailsTO.getPoLineType() );
      lOrderLineTO.setPoType( lPODetailsTO.getPoType() );
      lOrderLineTO.setPromisedByDate( lPODetailsTO.getPromisedByDate() );
      lOrderLineTO.setQtyUnit( lPODetailsTO.getQtyUnit() );
      lOrderLineTO.setQuantity( 1.0, "Qty" );
      lOrderLineTO.setReturnByDate( lPODetailsTO.getReturnByDate() );
      lOrderLineTO.setUnitPrice( lPODetailsTO.getUnitPrice(), "Unit Price" );
      lOrderLineTO.setBaseUnitPrice( lPODetailsTO.getBaseUnitPrice() );
      lOrderLineTO.setMaintenanceReceipt( lPODetailsTO.isMaintenanceReceipt() );
      lOrderLineTO.setInvCsgnXchgKeys( lPODetailsTO.getInvCsgnXchgKey() );
      lOrderLineTO.setReturnInv( lPODetailsTO.getReturnInv() );
      lOrderLineTO.setExchangePrice( lPODetailsTO.getExchangePrice() );
      if ( lPODetailsTO.getPartNo() != null ) {
         lOrderLineTO.setPartNo( lPODetailsTO.getPartNo(), "Part No" );
      }

      OrderManagerFacade.manageOrder( lOrderKey, new OrderModifier( lOrderKey, lOrderLineTO ),
            iHr );

      PurchaseOrderLineKey[] lPOLines = OrderService.getPOLines( lOrderKey );
      assertEquals( 1, lPOLines.length );

      // update the po line price and unit price, as if through the UI
      POLineTable lPOLine = POLineTable.findByPrimaryKey( lPOLines[0] );
      lPOLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPOLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPOLine.update();

      OrderService.authorize( lOrderKey, null, iHr );

      ShipmentKey lShipmentKey = new OrderService().issue( lOrderKey, null, iHr );

      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );
      assertEquals( 1, lShipmentLines.length );

      ReceiveShipmentLineTO lReceiveTO = new ReceiveShipmentLineTO( lShipmentLines[0] );
      lReceiveTO.setReceivedQty( 1.0 );
      lReceiveTO.setSerialNo( "TESTSERNO" );
      lReceiveTO.setPartNo( iPart );

      ShipmentService.receive( lShipmentKey, new Date(), new ReceiveShipmentLineTO[] { lReceiveTO },
            new OrderOwnership( RefPoLineTypeKey.PURCHASE, ShipmentDirection.INBOUND ), iHr );

      ShipShipmentLineTable lShipmentLine =
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLines[0] );

      InventoryKey lInventoryKey = lShipmentLine.getInventory();

      new DefaultChangePartNoService().setPartNo( lInventoryKey, iAltPart, true, iHr, null );

      new InvInv( lInventoryKey ).assertPartNo( iAltPart );

      InspectInventoryTO lInspectAsServiceableTO = new InspectInventoryTO();
      new DefaultConditionService().inspectInventory( lInventoryKey, lInspectAsServiceableTO, iHr );

      new InvInv( lInventoryKey ).assertPartNo( iAltPart );

      RefCurrency lRefCurrency = RefCurrency.findByPrimaryKey( iBitCoinCurrency );
      lRefCurrency.setExchgQt( new BigDecimal( 1.35172 ) );
      lRefCurrency.update();

      POInvoiceDetailsTO lPOInvoiceDetailsTO = new POInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setVendor( "TESTVENDOR" );
      lPOInvoiceDetailsTO.setCurrency( iBitCoinCurrency );
      lPOInvoiceDetailsTO.setExchangeRate( new BigDecimal( 1.35172 ) );
      lPOInvoiceDetailsTO.setPOInvoiceNumber( "123" );

      PurchaseInvoiceKey lInvoiceKey =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lOrderKey, iHr );

      PurchaseInvoiceLineKey[] lInvoiceLines =
            PurchaseInvoiceService.getInvoiceLines( lInvoiceKey );
      assertEquals( 1, lInvoiceLines.length );

      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( lInvoiceLines[0] );
      assertEquals( iAltPart, lPoInvoiceLine.getPartNo() );
      assertEquals( "ALT_PART (Alternate Part)", lPoInvoiceLine.getLineLdesc() );

      // update the line/unit price the easy way
      lPoInvoiceLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.update();

      PurchaseInvoiceService.validateForPayment( lInvoiceKey, iHr, null );

      // we are simply expecting no exceptions to occur
   }


   /**
    * Given an invoice created for a recieved PO for a part, when the received inventory is changed
    * to an alternate part, the invoice is updated to the alternate part as well.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testCreateInvoiceBeforePartChange() throws Exception {
      // setup order details
      OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
      lPODetailsTO.setChargeToAcc( iAssetAccount );
      lPODetailsTO.setCurrency( iBitCoinCurrency );
      lPODetailsTO.setExchangeRate( new BigDecimal( 1.31172 ) );
      lPODetailsTO.setOrganization( OrgKey.ADMIN );
      lPODetailsTO.setPartNo( iPart );
      lPODetailsTO.setPoLineType( RefPoLineTypeKey.PURCHASE );
      lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );
      lPODetailsTO.setQtyUnit( RefQtyUnitKey.EA );
      lPODetailsTO.setQuantity( 1.0 );
      lPODetailsTO.setShipToLocation( "DOCK" );
      lPODetailsTO.setUnitPrice( new BigDecimal( 696.81 ) );
      lPODetailsTO.setVendor( iVendor );
      lPODetailsTO.setInspectionRequired( true );

      // create the order
      PurchaseOrderKey lOrderKey = OrderService.create( lPODetailsTO, ( String ) null, iHr );

      // setup the order line
      OrderLineTO lOrderLineTO = new OrderLineTO();
      lOrderLineTO.setBorrowRate( lPODetailsTO.getBorrowRate() );
      lOrderLineTO.setChargeToAcc( lPODetailsTO.getChargeToAcc() );
      lOrderLineTO.setOwner( lPODetailsTO.getOwner() );
      lOrderLineTO.setPoLineType( lPODetailsTO.getPoLineType() );
      lOrderLineTO.setPoType( lPODetailsTO.getPoType() );
      lOrderLineTO.setPromisedByDate( lPODetailsTO.getPromisedByDate() );
      lOrderLineTO.setQtyUnit( lPODetailsTO.getQtyUnit() );
      lOrderLineTO.setQuantity( 1.0, "Qty" );
      lOrderLineTO.setReturnByDate( lPODetailsTO.getReturnByDate() );
      lOrderLineTO.setUnitPrice( lPODetailsTO.getUnitPrice(), "Unit Price" );
      lOrderLineTO.setBaseUnitPrice( lPODetailsTO.getBaseUnitPrice() );
      lOrderLineTO.setMaintenanceReceipt( lPODetailsTO.isMaintenanceReceipt() );
      lOrderLineTO.setInvCsgnXchgKeys( lPODetailsTO.getInvCsgnXchgKey() );
      lOrderLineTO.setReturnInv( lPODetailsTO.getReturnInv() );
      lOrderLineTO.setExchangePrice( lPODetailsTO.getExchangePrice() );
      if ( lPODetailsTO.getPartNo() != null ) {
         lOrderLineTO.setPartNo( lPODetailsTO.getPartNo(), "Part No" );
      }

      // add the order line
      OrderManagerFacade.manageOrder( lOrderKey, new OrderModifier( lOrderKey, lOrderLineTO ),
            iHr );

      PurchaseOrderLineKey[] lPOLines = OrderService.getPOLines( lOrderKey );
      assertEquals( 1, lPOLines.length );

      // update the po line price and unit price, as if through the UI
      POLineTable lPOLine = POLineTable.findByPrimaryKey( lPOLines[0] );
      lPOLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPOLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPOLine.update();

      // authorize the order
      OrderService.authorize( lOrderKey, null, iHr );

      // issue the order
      ShipmentKey lShipmentKey = new OrderService().issue( lOrderKey, null, iHr );

      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );
      assertEquals( 1, lShipmentLines.length );

      // receive the shipment
      ReceiveShipmentLineTO lReceiveTO = new ReceiveShipmentLineTO( lShipmentLines[0] );
      lReceiveTO.setReceivedQty( 1.0 );
      lReceiveTO.setSerialNo( "TESTSERNO" );
      lReceiveTO.setPartNo( iPart );

      ShipmentService.receive( lShipmentKey, new Date(), new ReceiveShipmentLineTO[] { lReceiveTO },
            new OrderOwnership( RefPoLineTypeKey.PURCHASE, ShipmentDirection.INBOUND ), iHr );

      // update the exchange rate
      RefCurrency lRefCurrency = RefCurrency.findByPrimaryKey( iBitCoinCurrency );
      lRefCurrency.setExchgQt( new BigDecimal( 1.35172 ) );
      lRefCurrency.update();

      // setup the invoice details
      POInvoiceDetailsTO lPOInvoiceDetailsTO = new POInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setVendor( "TESTVENDOR" );
      lPOInvoiceDetailsTO.setCurrency( iBitCoinCurrency );
      lPOInvoiceDetailsTO.setExchangeRate( new BigDecimal( 1.35172 ) );
      lPOInvoiceDetailsTO.setPOInvoiceNumber( "123" );

      // create the invoice
      PurchaseInvoiceKey lInvoiceKey =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lOrderKey, iHr );

      ShipShipmentLineTable lShipmentLine =
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLines[0] );

      InventoryKey lInventoryKey = lShipmentLine.getInventory();

      // change the part number for the inventory
      new DefaultChangePartNoService().setPartNo( lInventoryKey, iAltPart, true, iHr, null );

      // inspect the inventory
      InspectInventoryTO lInspectAsServiceableTO = new InspectInventoryTO();
      new DefaultConditionService().inspectInventory( lInventoryKey, lInspectAsServiceableTO, iHr );

      PurchaseInvoiceLineKey[] lInvoiceLines =
            PurchaseInvoiceService.getInvoiceLines( lInvoiceKey );
      assertEquals( 1, lInvoiceLines.length );

      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( lInvoiceLines[0] );
      assertEquals( iAltPart, lPoInvoiceLine.getPartNo() );
      assertEquals( "ALT_PART (Alternate Part)", lPoInvoiceLine.getLineLdesc() );

      // update the line/unit price the easy way
      lPoInvoiceLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.update();

      // validate the invoice for payment
      PurchaseInvoiceService.validateForPayment( lInvoiceKey, iHr, null );

      // we are simply expecting no exceptions to occur
   }


   /**
    * Given an invoice created for a recieved PO for a part, when the received inventory is changed
    * to an alternate part via the shipment, the invoice is updated to the alternate part as well.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testCreateInvoiceBeforePartChangeForReceivedInv() throws Exception {
      // setup order details
      OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
      lPODetailsTO.setChargeToAcc( iAssetAccount );
      lPODetailsTO.setCurrency( iBitCoinCurrency );
      lPODetailsTO.setExchangeRate( new BigDecimal( 1.31172 ) );
      lPODetailsTO.setOrganization( OrgKey.ADMIN );
      lPODetailsTO.setPartNo( iPart );
      lPODetailsTO.setPoLineType( RefPoLineTypeKey.PURCHASE );
      lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );
      lPODetailsTO.setQtyUnit( RefQtyUnitKey.EA );
      lPODetailsTO.setQuantity( 1.0 );
      lPODetailsTO.setShipToLocation( "DOCK" );
      lPODetailsTO.setUnitPrice( new BigDecimal( 696.81 ) );
      lPODetailsTO.setVendor( iVendor );
      lPODetailsTO.setInspectionRequired( true );

      // create the order
      PurchaseOrderKey lOrderKey = OrderService.create( lPODetailsTO, ( String ) null, iHr );

      // setup the order line
      OrderLineTO lOrderLineTO = new OrderLineTO();
      lOrderLineTO.setBorrowRate( lPODetailsTO.getBorrowRate() );
      lOrderLineTO.setChargeToAcc( lPODetailsTO.getChargeToAcc() );
      lOrderLineTO.setOwner( lPODetailsTO.getOwner() );
      lOrderLineTO.setPoLineType( lPODetailsTO.getPoLineType() );
      lOrderLineTO.setPoType( lPODetailsTO.getPoType() );
      lOrderLineTO.setPromisedByDate( lPODetailsTO.getPromisedByDate() );
      lOrderLineTO.setQtyUnit( lPODetailsTO.getQtyUnit() );
      lOrderLineTO.setQuantity( 1.0, "Qty" );
      lOrderLineTO.setReturnByDate( lPODetailsTO.getReturnByDate() );
      lOrderLineTO.setUnitPrice( lPODetailsTO.getUnitPrice(), "Unit Price" );
      lOrderLineTO.setBaseUnitPrice( lPODetailsTO.getBaseUnitPrice() );
      lOrderLineTO.setMaintenanceReceipt( lPODetailsTO.isMaintenanceReceipt() );
      lOrderLineTO.setInvCsgnXchgKeys( lPODetailsTO.getInvCsgnXchgKey() );
      lOrderLineTO.setReturnInv( lPODetailsTO.getReturnInv() );
      lOrderLineTO.setExchangePrice( lPODetailsTO.getExchangePrice() );
      if ( lPODetailsTO.getPartNo() != null ) {
         lOrderLineTO.setPartNo( lPODetailsTO.getPartNo(), "Part No" );
      }

      // add the order line
      OrderManagerFacade.manageOrder( lOrderKey, new OrderModifier( lOrderKey, lOrderLineTO ),
            iHr );

      PurchaseOrderLineKey[] lPOLines = OrderService.getPOLines( lOrderKey );
      assertEquals( 1, lPOLines.length );

      // update the po line price and unit price, as if through the UI
      POLineTable lPOLine = POLineTable.findByPrimaryKey( lPOLines[0] );
      lPOLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPOLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPOLine.update();

      // authorize the order
      OrderService.authorize( lOrderKey, null, iHr );

      // issue the order
      ShipmentKey lShipmentKey = new OrderService().issue( lOrderKey, null, iHr );

      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );
      assertEquals( 1, lShipmentLines.length );

      // receive the shipment
      ReceiveShipmentLineTO lReceiveTO = new ReceiveShipmentLineTO( lShipmentLines[0] );
      lReceiveTO.setReceivedQty( 1.0 );
      lReceiveTO.setSerialNo( "TESTSERNO" );
      lReceiveTO.setPartNo( iAltPart );

      ShipmentService.receive( lShipmentKey, new Date(), new ReceiveShipmentLineTO[] { lReceiveTO },
            new OrderOwnership( RefPoLineTypeKey.PURCHASE, ShipmentDirection.INBOUND ), iHr );

      // update the exchange rate
      RefCurrency lRefCurrency = RefCurrency.findByPrimaryKey( iBitCoinCurrency );
      lRefCurrency.setExchgQt( new BigDecimal( 1.35172 ) );
      lRefCurrency.update();

      // setup the invoice details
      POInvoiceDetailsTO lPOInvoiceDetailsTO = new POInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setVendor( "TESTVENDOR" );
      lPOInvoiceDetailsTO.setCurrency( iBitCoinCurrency );
      lPOInvoiceDetailsTO.setExchangeRate( new BigDecimal( 1.35172 ) );
      lPOInvoiceDetailsTO.setPOInvoiceNumber( "123" );

      // create the invoice
      PurchaseInvoiceKey lInvoiceKey =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lOrderKey, iHr );

      ShipShipmentLineTable lShipmentLine =
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLines[0] );

      InventoryKey lInventoryKey = lShipmentLine.getInventory();

      // assert the inventory is received as the alternate part
      new InvInv( lInventoryKey ).assertPartNo( iAltPart );

      // inspect the inventory
      InspectInventoryTO lInspectAsServiceableTO = new InspectInventoryTO();
      new DefaultConditionService().inspectInventory( lInventoryKey, lInspectAsServiceableTO, iHr );

      PurchaseInvoiceLineKey[] lInvoiceLines =
            PurchaseInvoiceService.getInvoiceLines( lInvoiceKey );
      assertEquals( 1, lInvoiceLines.length );

      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( lInvoiceLines[0] );

      assertEquals( iAltPart, lPoInvoiceLine.getPartNo() );
      assertEquals( "ALT_PART (Alternate Part)", lPoInvoiceLine.getLineLdesc() );

      // update the line/unit price the easy way
      lPoInvoiceLine.setLinePrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.setUnitPrice( new BigDecimal( 696.81 ) );
      lPoInvoiceLine.update();

      // validate the invoice for payment
      PurchaseInvoiceService.validateForPayment( lInvoiceKey, iHr, null );

      // we are simply expecting no exceptions to occur
   }


   @Before
   public void loadData() throws Exception {

      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create the default currency as USD
      new CurrencyBuilder( "USD" ).isDefault().build();

      // create another currency
      iBitCoinCurrency = new CurrencyBuilder( "BITCOIN" ).build();

      // create an asset account
      iAssetAccount =
            new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // create a quantity adjustment account
      new AccountBuilder().withType( RefAccountTypeKey.ADJQTY ).isDefault().build();

      // create a part group
      iPartGroup = new PartGroupDomainBuilder( "TESTGROUP" ).build();

      // create a part
      iPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "STD_PART" )
            .withShortDescription( "Standard Part" ).withAssetAccount( iAssetAccount )
            .withTotalValue( new BigDecimal( 0 ) ).withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 510.37 ) ).isAlternateIn( iPartGroup ).build();

      // create a part that is an alternate to the first part
      iAltPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "ALT_PART" )
            .withShortDescription( "Alternate Part" ).withAssetAccount( iAssetAccount )
            .withTotalValue( new BigDecimal( 2100.67571 ) )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 4 ) )
            .withAverageUnitPrice( new BigDecimal( 525.16893 ) ).isAlternateIn( iPartGroup )
            .build();

      // create a default local owner
      new OwnerDomainBuilder().isDefault().build();

      // create a vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create a dock location
      new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withCode( "DOCK" ).build();
   }

}
