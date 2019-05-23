
package com.mxi.mx.core.unittest.shipment;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
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
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
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
import com.mxi.mx.core.model.order.OrderModifier;
import com.mxi.mx.core.services.order.OrderDetailsTO;
import com.mxi.mx.core.services.order.OrderLineTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.shipment.SendShipmentTO;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.services.shipment.routing.ReceiveShipmentSegmentTO;
import com.mxi.mx.core.services.shipment.routing.ShipmentRoutingService;
import com.mxi.mx.core.table.po.POLineTable;


/**
 * Tests the ShipmentBean.updateAndSend() method.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SPUpdateWaybillSendTest {

   private static final int USERID_TESTUSER = 999;

   private static final String USERNAME_TESTUSER = "testuser";

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   private FncAccountKey iAssetAccount;

   private RefCurrencyKey iCoinCurrency;

   private HumanResourceKey iHr;

   private PartNoKey iPart;

   private PartGroupKey iPartGroup;

   private VendorKey iVendor;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests sending a shipment that is part of a waybill group and PO. Make sure that the promised
    * by date of the PO is updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSendUpdatedShipmentWithWaybillGroup() throws Exception {
      // setup order details
      OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
      lPODetailsTO.setChargeToAcc( iAssetAccount );
      lPODetailsTO.setCurrency( iCoinCurrency );
      lPODetailsTO.setExchangeRate( new BigDecimal( 1.31172 ) );
      lPODetailsTO.setOrganization( OrgKey.ADMIN );
      lPODetailsTO.setPartNo( iPart );
      lPODetailsTO.setPoLineType( RefPoLineTypeKey.PURCHASE );
      lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );
      lPODetailsTO.setQtyUnit( RefQtyUnitKey.EA );
      lPODetailsTO.setQuantity( 1.0 );
      lPODetailsTO.setShipToLocation( "BOS/DOCK" );
      lPODetailsTO.setReExpediteToLocation( "JFK/DOCK", "" );
      lPODetailsTO.setUnitPrice( new BigDecimal( 696.81 ) );
      lPODetailsTO.setVendor( iVendor );
      lPODetailsTO.setInspectionRequired( true );
      Calendar lPromisedByDate = Calendar.getInstance();
      lPromisedByDate.add( Calendar.DAY_OF_MONTH, 10 );
      lPODetailsTO.setPromisedByDate( lPromisedByDate.getTime() );

      // create the order
      PurchaseOrderKey lOrderKey = OrderService.create( lPODetailsTO, "Create the PO", iHr );

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
      OrderService.authorize( lOrderKey, "Authorizing the PO", iHr );

      // issue the order
      ShipmentKey lShipmentKey = new OrderService().issue( lOrderKey, "Issuing the PO", iHr );

      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );
      assertEquals( 1, lShipmentLines.length );

      // receive the shipment
      ReceiveShipmentSegmentTO lTO = new ReceiveShipmentSegmentTO();
      lTO.setShipmentKey( lShipmentKey );
      lTO.setFinalDestination( false );
      lTO.setReceivedDate( new Date() );

      // receive the first segment
      ShipmentRoutingService lShipmentRoutingService = new ShipmentRoutingService( lShipmentKey );
      lShipmentRoutingService.receive( lTO, iHr );

      // update the shipment to set the waybill number
      ShipmentService.assignToWaybillGroup( "TEST1234", lShipmentKey );

      lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );

      // send the shipment to the final destination
      SendShipmentTO lSendShipmentTO = new SendShipmentTO();
      lSendShipmentTO.setWaybillNumber( "TEST1234", "" );
      lSendShipmentTO.setShipmentDate( new Date() );
      Calendar lEstimatedReceivedByDate = Calendar.getInstance();
      lEstimatedReceivedByDate.add( Calendar.DAY_OF_MONTH, 5 );
      Date lEstimatedReceivedByDt = new Date( lEstimatedReceivedByDate.getTimeInMillis() );
      lSendShipmentTO.setEstReceiveDate( lEstimatedReceivedByDt );
      lSendShipmentTO.setNotes( "Test Send Shipment", "" );

      ShipmentService.setSendShipment( lShipmentKey, lSendShipmentTO );

      // check that the promised by date was changed.
      lPOLines = OrderService.getPOLines( lOrderKey );
      assertEquals( 1, lPOLines.length );

      // update the po line price and unit price, as if through the UI
      lPOLine = POLineTable.findByPrimaryKey( lPOLines[0] );

      Date lUpdatedPromisedByDate = new Date( lPOLine.getPromisedBy().getTime() );
      assertEquals( lUpdatedPromisedByDate.toString(), lEstimatedReceivedByDt.toString() );

   }


   /**
    * Tests sending a shipment that is not part of a waybill group and PO. Make sure that the
    * promised by date of the PO is not updated.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSendUpdatedShipmentWithoutWaybillGroup() throws Exception {
      // setup order details
      OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
      lPODetailsTO.setChargeToAcc( iAssetAccount );
      lPODetailsTO.setCurrency( iCoinCurrency );
      lPODetailsTO.setExchangeRate( new BigDecimal( 1.31172 ) );
      lPODetailsTO.setOrganization( OrgKey.ADMIN );
      lPODetailsTO.setPartNo( iPart );
      lPODetailsTO.setPoLineType( RefPoLineTypeKey.PURCHASE );
      lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );
      lPODetailsTO.setQtyUnit( RefQtyUnitKey.EA );
      lPODetailsTO.setQuantity( 1.0 );
      lPODetailsTO.setShipToLocation( "BOS/DOCK" );
      lPODetailsTO.setReExpediteToLocation( "JFK/DOCK", "" );
      lPODetailsTO.setUnitPrice( new BigDecimal( 696.81 ) );
      lPODetailsTO.setVendor( iVendor );
      lPODetailsTO.setInspectionRequired( true );
      Calendar lPromisedByDate = Calendar.getInstance();
      lPromisedByDate.add( Calendar.DAY_OF_MONTH, 10 );
      lPODetailsTO.setPromisedByDate( lPromisedByDate.getTime() );

      // create the order
      PurchaseOrderKey lOrderKey = OrderService.create( lPODetailsTO, "Create the PO", iHr );

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
      OrderService.authorize( lOrderKey, "Authorizing the PO", iHr );

      // issue the order
      ShipmentKey lShipmentKey = new OrderService().issue( lOrderKey, "Issuing the PO", iHr );

      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );
      assertEquals( 1, lShipmentLines.length );

      // receive the shipment
      ReceiveShipmentSegmentTO lTO = new ReceiveShipmentSegmentTO();
      lTO.setShipmentKey( lShipmentKey );
      lTO.setFinalDestination( false );
      lTO.setReceivedDate( new Date() );

      // receive the first segment
      ShipmentRoutingService lShipmentRoutingService = new ShipmentRoutingService( lShipmentKey );
      lShipmentRoutingService.receive( lTO, iHr );
      lShipmentLines = ShipmentService.getShipmentLines( lShipmentKey );

      // send the shipment to the final destination
      Calendar lEstimatedReceivedByDate = Calendar.getInstance();
      lEstimatedReceivedByDate.add( Calendar.DAY_OF_MONTH, 5 );
      Date lEstimatedReceivedByDt = new Date( lEstimatedReceivedByDate.getTimeInMillis() );
      ShipmentService.send( lShipmentKey, iHr, new Date(), lEstimatedReceivedByDt,
            "Test Send Shipment", "Test Send Shipment" );

      // check that the promised by date was changed.
      lPOLines = OrderService.getPOLines( lOrderKey );
      assertEquals( 1, lPOLines.length );

      // update the po line price and unit price, as if through the UI
      lPOLine = POLineTable.findByPrimaryKey( lPOLines[0] );

      Date lUpdatedPromisedByDate = new Date( lPOLine.getPromisedBy().getTime() );
      assertEquals( lUpdatedPromisedByDate.toString(), lPromisedByDate.getTime().toString() );

   }


   /**
    * Creates a new SPUpdateAndSendTest object.
    *
    * @throws Exception
    *            if an exception occurs
    */
   @Before
   public void loadData() throws Exception {
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create the default currency as USD
      iCoinCurrency = new CurrencyBuilder( "USD" ).isDefault().build();

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

      // create a default local owner
      new OwnerDomainBuilder().isDefault().build();

      // create a vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create a dock locations for test
      new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withCode( "BOS/DOCK" ).build();
      new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withCode( "JFK/DOCK" ).build();
   }
}
