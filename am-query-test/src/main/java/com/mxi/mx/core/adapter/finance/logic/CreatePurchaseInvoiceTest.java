
package com.mxi.mx.core.adapter.finance.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.adapter.finance.model.Account;
import com.mxi.mx.core.adapter.finance.model.Invoice;
import com.mxi.mx.core.adapter.finance.model.InvoiceLine;
import com.mxi.mx.core.adapter.finance.model.PurchaseOrder;
import com.mxi.mx.core.adapter.finance.model.Vendor;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.po.POLineTable;


/**
 * This class tests the {@link MxFinanceAdapter.createPurchaseInvoice} method.
 */

public final class CreatePurchaseInvoiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private final String VENDOR_CODE = "VENDOR_CD";

   private final String PART_DESCRIPTION = "Part Description";

   private final String PART_CODE = "PART_CD";

   private final String ACCOUNT_CODE = "ACCOUNT_CD";

   private final String INV_SERIAL_NO = "123";

   private final String PO_NUMBER = "Po Number";

   private final BigDecimal INVOICE_PRICE = new BigDecimal( 600 );

   private final BigDecimal ORDER_PRICE = new BigDecimal( 100 );

   private final Integer TOLERANCE_PERCENTAGE_500 = 500;

   private final Integer TOLERANCE_FIXED_1000 = 1000;

   private final Integer ZERO_TOLERANCE_PERCENTAGE = 0;

   private final Integer ZERO_TOLERANCE_FIXED = 0;

   private final boolean VALIDATE_WITH_ORDER = true;

   private final boolean DO_NOT_VALIDATE_WITH_ORDER = false;

   private RefCurrencyKey defaultCurrency;

   private HumanResourceKey hr;


   /**
    * Test creating a purchase invoice where the invoice pricing is within the fixed tolerance but
    * outside the percentage tolerance
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testCreatePurchaseInvoice_ValidateWithOrder_OutsideTolerancePercentage()
         throws Exception {

      // DATA SETUP: Create a Purchase Order
      PurchaseOrderKey purchaseOrder = createPurchaseOrder();

      // DATA SETUP: Update ADMIN config parm
      updateAdminConfigParm( TOLERANCE_FIXED_1000, ZERO_TOLERANCE_PERCENTAGE );

      // Create an invoice with the invoice price
      PurchaseInvoiceKey purchaseInvoice = createPOInvoice( VALIDATE_WITH_ORDER );

      // Asserts that an invoice is created
      assertNotNull( purchaseInvoice );

      // Asserts that the invoice status is OPEN
      RefEventStatusKey invoiceStatus = new JdbcEvtEventDao()
            .findByPrimaryKey( purchaseInvoice.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PIOPEN, invoiceStatus );

      // Asserts that the purchase order status is RECEIVED
      RefEventStatusKey orderStatus =
            new JdbcEvtEventDao().findByPrimaryKey( purchaseOrder.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PORECEIVED, orderStatus );
   }


   /**
    * Test creating a purchase invoice where the invoice pricing is within the percentage tolerance
    * but outside the fixed tolerance
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testCreatePurchaseInvoice_ValidateWithOrder_OutsideToleranceFixed()
         throws Exception {

      // DATA SETUP: Create a Purchase Order
      PurchaseOrderKey purchaseOrder = createPurchaseOrder();

      // DATA SETUP: Update ADMIN config parm
      updateAdminConfigParm( ZERO_TOLERANCE_FIXED, TOLERANCE_PERCENTAGE_500 );

      // Create an invoice
      PurchaseInvoiceKey purchaseInvoice = createPOInvoice( VALIDATE_WITH_ORDER );

      // Asserts that an invoice is created
      assertNotNull( purchaseInvoice );

      // Asserts that the invoice status is OPEN
      RefEventStatusKey invoiceStatus = new JdbcEvtEventDao()
            .findByPrimaryKey( purchaseInvoice.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PIOPEN, invoiceStatus );

      // Asserts that the purchase order status is RECEIVED
      RefEventStatusKey orderStatus =
            new JdbcEvtEventDao().findByPrimaryKey( purchaseOrder.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PORECEIVED, orderStatus );
   }


   /**
    * Test creating a purchase invoice where the invoice pricing does not match the order pricing
    * but it is within the tolerance
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testCreatePurchaseInvoice_ValidateWithOrder_WithinTolerance() throws Exception {

      // DATA SETUP: Create a Purchase Order
      PurchaseOrderKey purchaseOrder = createPurchaseOrder();

      // DATA SETUP: Update ADMIN config parm
      updateAdminConfigParm( TOLERANCE_FIXED_1000, TOLERANCE_PERCENTAGE_500 );

      // Create an invoice
      PurchaseInvoiceKey purchaseInvoice = createPOInvoice( VALIDATE_WITH_ORDER );

      // Asserts that an invoice is created
      assertNotNull( purchaseInvoice );

      // Asserts that the invoice status is OPEN
      RefEventStatusKey invoiceStatus = new JdbcEvtEventDao()
            .findByPrimaryKey( purchaseInvoice.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PITOBEPAID, invoiceStatus );

      // Asserts that the purchase order status is CLOSED
      RefEventStatusKey orderStatus =
            new JdbcEvtEventDao().findByPrimaryKey( purchaseOrder.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.POCLOSED, orderStatus );
   }


   /**
    * Test creating a purchase invoice with no validation on order
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testCreatePurchaseInvoice_NoValidation() throws Exception {

      // DATA SETUP: Create a Purchase Order
      PurchaseOrderKey purchaseOrder = createPurchaseOrder();

      // DATA SETUP: Update ADMIN config parm
      updateAdminConfigParm( ZERO_TOLERANCE_FIXED, ZERO_TOLERANCE_PERCENTAGE );

      // Create an invoice
      PurchaseInvoiceKey purchaseInvoice = createPOInvoice( DO_NOT_VALIDATE_WITH_ORDER );

      // Asserts that an invoice is created
      assertNotNull( purchaseInvoice );

      // Asserts that the invoice status is OPEN
      RefEventStatusKey invoiceStatus = new JdbcEvtEventDao()
            .findByPrimaryKey( purchaseInvoice.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.PITOBEPAID, invoiceStatus );

      // Asserts that the purchase order status is CLOSED
      RefEventStatusKey orderStatus =
            new JdbcEvtEventDao().findByPrimaryKey( purchaseOrder.getEventKey() ).getEventStatus();

      assertEquals( RefEventStatusKey.POCLOSED, orderStatus );
   }


   private PurchaseInvoiceKey createPOInvoice( boolean validateWithOrder ) throws Exception {

      Invoice invoice = createInvoiceObject( validateWithOrder );

      PurchaseInvoiceKey purchaseInvoice = new MxFinanceAdapter().createPurchaseInvoice( invoice );

      return purchaseInvoice;
   }


   private Invoice createInvoiceObject( boolean validateWithOrder ) {

      Account account = new Account();
      account.setCode( ACCOUNT_CODE );

      PurchaseOrder purchaseOrder = new PurchaseOrder();
      purchaseOrder.setPONumber( PO_NUMBER );
      purchaseOrder.setPOLineNumber( 1 );

      InvoiceLine invoiceLine = new InvoiceLine();
      invoiceLine.setUnitPrice( INVOICE_PRICE );
      invoiceLine.setInvoiceQty( BigDecimal.ONE );
      invoiceLine.setDescription( PART_DESCRIPTION );
      invoiceLine.setAccount( account );
      invoiceLine.addPurchaseOrder( purchaseOrder );
      invoiceLine.setQtyUnit( RefQtyUnitKey.EA );
      invoiceLine.setPartNumber( PART_CODE );

      Vendor vendor = new Vendor();
      vendor.setCode( VENDOR_CODE );

      Invoice invoice = new Invoice();
      invoice.setInvoiceNumber( "INVOICE123" );
      invoice.setValidateWithOrderBool( validateWithOrder );
      invoice.addLine( invoiceLine );
      invoice.setVendor( vendor );
      invoice.setCurrency( defaultCurrency );
      invoice.setExchangeRate( BigDecimal.ONE );

      return invoice;
   }


   private PurchaseOrderKey createPurchaseOrder() throws Exception {

      // create currency
      defaultCurrency =
            new CurrencyBuilder( "USD" ).withExchangeQt( BigDecimal.ONE ).isDefault().build();

      // Create a financial account
      FncAccountKey account = new AccountBuilder().withCode( ACCOUNT_CODE )
            .withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // Create a part
      PartNoKey partNo = Domain.createPart( part -> {
         part.setCode( PART_CODE );
         part.setShortDescription( PART_DESCRIPTION );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setFinancialAccount( account );
         part.setPartStatus( RefPartStatusKey.ACTV );
         part.setInventoryClass( RefInvClassKey.SER );
      } );

      // Create location
      LocationKey vendorLoc = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.VENDOR ) );

      LocationKey shippingToLoc = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.DOCK ) );

      LocationKey invLoc = Domain.createLocation( loc -> loc.setType( RefLocTypeKey.DOCK ) );

      // Create a vendor
      VendorKey vendor =
            new VendorBuilder().atLocation( vendorLoc ).withCode( VENDOR_CODE ).build();

      // Create a purchase order
      PurchaseOrderKey purchaseOrder = Domain.createPurchaseOrder( order -> {
         order.orderType( RefPoTypeKey.PURCHASE );
         order.status( RefEventStatusKey.POAUTH );
         order.vendor( vendor );
         order.setOrderNumber( PO_NUMBER );
         order.usingCurrency( defaultCurrency );
         order.shippingTo( shippingToLoc );
      } );

      // Create an owner
      OwnerKey owner = Domain.createOwner( ownerBuilder -> ownerBuilder.setLocalBool( true ) );

      // Create a purchase order line
      PurchaseOrderLineKey orderLine = Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( purchaseOrder );
         poLine.lineType( RefPoLineTypeKey.PURCHASE );
         poLine.part( partNo );
         poLine.unitType( RefQtyUnitKey.EA );
         poLine.orderQuantity( BigDecimal.ONE );
         poLine.unitPrice( ORDER_PRICE );
         poLine.account( account );
         poLine.owner( owner );
         poLine.accruedValue( BigDecimal.ZERO );
      } );

      // Issue the purchase order
      ShipmentKey inboundShipment = new OrderService().issue( purchaseOrder, null, hr );

      // Create an inventory
      InventoryKey receivedInv = Domain.createSerializedInventory( inv -> {
         inv.setPartNumber( partNo );
         inv.setSerialNumber( INV_SERIAL_NO );
         inv.setCondition( RefInvCondKey.RFI );
         inv.setLocation( invLoc );
      } );

      // Receive Inventory
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( ShipmentService.getShipmentLines( inboundShipment )[0] );
      lReceiveShipmentLineTO.setPartNo( partNo );
      lReceiveShipmentLineTO.setReceivedQty( 1d );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "" );
      lReceiveShipmentLineTO.setSerialNo( INV_SERIAL_NO );

      ShipmentService.receive( inboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO }, null, hr );

      POLineTable.findByPrimaryKey( orderLine ).getReceivedQt();

      // Inspect the inventory as servicable
      new DefaultConditionService().inspectInventory( receivedInv, new InspectInventoryTO(), hr );

      return purchaseOrder;
   }


   private void updateAdminConfigParm( Integer fixedValue, Integer percentageValue ) {

      int userAdmin = UserKey.ADMIN.getId();

      UserParametersStub userParametersStub = new UserParametersStub( userAdmin, "LOGIC" );
      userParametersStub.setInteger( "INVOICE_TOLERANCE_PCT", percentageValue );
      userParametersStub.setInteger( "INVOICE_TOLERANCE_FIXED", fixedValue );
      userParametersStub.setBoolean( "INVOICE_ALLOW_OVERINV", true );
      UserParameters.setInstance( userAdmin, "LOGIC", userParametersStub );
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
