
package com.mxi.mx.web.query.po;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.order.OrderServiceFactory;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.unittest.table.po.PoLine;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 * @created September 22, 2016
 */

public final class POInventoryReceiptsTest {

   private static final double BIN_QT_OF_SER_RECEIVED_INV = 1;
   private static final double BIN_QT_OF_BATCH_RECEIVED_INV = 10.0;
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;
   private static final String SER_INV_SN = "TEST123";
   private static final String INVASSET_ACCOUNT = "INVASSET";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private QuerySet iQuerySet;
   private PurchaseOrderKey iPurchaseOrder;
   private PurchaseOrderLineKey iPoLine;
   private LocationKey iVendorLocation;
   private ShipmentKey iInboundShipment;
   private InventoryBuilder iInventoryBuilder;
   private RefInvCondKey iConditionOfReceivedInv;
   private HumanResourceKey iHr;
   private VendorKey iVendor;
   private OwnerKey iLocalOwner;
   private OrgKey iReceiptOrganization;
   private OrderService iOrderService;
   private FncAccountKey iLocalAccount;


   @Before
   public void loadData() throws Exception {

      iPurchaseOrder = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE ).build();

      iInboundShipment =
            createCompletedInboundShipment( iPurchaseOrder, RefShipmentTypeKey.PURCHASE );

      iVendorLocation = Domain.createLocation( aVendorLocation -> {
         aVendorLocation.setCode( "TESTVENLOC" );
         aVendorLocation.setType( RefLocTypeKey.VENDOR );
      } );

      iLocalOwner = Domain.createOwner();

      iReceiptOrganization = new InvOwnerTable( iLocalOwner ).getOrgKey();

      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      iHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUsername( USERNAME_TESTUSER );
         aUser.setUserId( USERID_TESTUSER );
      } ) ) );

      iLocalAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( INVASSET_ACCOUNT ).isDefault().build();

      // For core inventory creation to lookup the user ID
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iOrderService = new OrderService();

   }


   /**
    * Test the over received inventory from inbound shipment. For example: If ordered 10, received
    * 15, there should be two rows in the Inventory Receipts group box in the Receipt and Return tab
    * of order details page. One is received inventory with bin quantity of 10, another one is
    * quarantine inventory with bin quantity of 5.
    */
   @Test
   public void testGivenAnOrderWhenOverReceivedThenShowOverReceivedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.INSPREQ;

      InventoryKey lBatchReceivedInv = prepareDataForBatchInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      final double OVER_RECEIVED_QT = 5.0;

      InventoryKey lOverReceivedInv = iInventoryBuilder.withCondition( RefInvCondKey.QUAR )
            .withBinQt( OVER_RECEIVED_QT ).build();

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertTwoBatchInventoriesInTheResult( lBatchReceivedInv, BIN_QT_OF_BATCH_RECEIVED_INV,
            lOverReceivedInv, RefInvCondKey.QUAR, OVER_RECEIVED_QT );

   }


   /**
    * Test Sending and then Receiving the same SER inventory in an Exchange Order. The inbound
    * shipment should appear on the Receipt and Return tab
    *
    * this test is done mostly using actual business logic to ensure the data is written correctly
    * and the query tested properly
    */
   @Test
   public void testGivenAnOrderWhenSentAndReceivedSameInventoryThenShowInventory()
         throws Exception {

      // an exception is thrown about a missing parameter if this is not defined manually
      UserParametersStub lSecuredResourceUserParms =
            new UserParametersStub( USERID_TESTUSER, "SECURED_RESOURCE" );
      lSecuredResourceUserParms
            .setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE", true );
      UserParameters.setInstance( USERID_TESTUSER, "SECURED_RESOURCE", lSecuredResourceUserParms );

      UserParametersStub lLogicUserParms = new UserParametersStub( USERID_TESTUSER, "LOGIC" );
      lLogicUserParms.setBoolean( "ALLOW_AUTO_COMPLETION", true );
      UserParameters.setInstance( USERID_TESTUSER, "LOGIC", lLogicUserParms );

      LocationKey lSupplyLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.AIRPORT );
         aLocation.setIsSupplyLocation( true );
      } );

      LocationKey lDockLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
         aLocation.setSupplyLocation( lSupplyLocation );
      } );

      // Create Exchange order against the vender and local org
      PurchaseOrderKey iExchangeOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.EXCHANGE );
         aPurchaseOrder.vendor( iVendor );
         aPurchaseOrder.setOrganization( iReceiptOrganization );
         aPurchaseOrder.shippingTo( lDockLocation );

      } );

      // Create serialized part number
      PartNoKey lPartNo = Domain.createPart( aPart -> {

         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );

      } );

      // create the exchange order line with the SER Part number
      PurchaseOrderLineKey lEOLine = Domain.createPurchaseOrderLine( aPoLine -> {

         aPoLine.orderKey( iExchangeOrder );
         aPoLine.lineType( RefPoLineTypeKey.EXCHANGE );
         aPoLine.part( lPartNo );
         aPoLine.unitType( RefQtyUnitKey.EA );
         aPoLine.orderQuantity( new BigDecimal( BIN_QT_OF_SER_RECEIVED_INV ) );
         aPoLine.linePrice( new BigDecimal( 22.0 ) );
         aPoLine.account( iLocalAccount );

      } );

      // create local inventory and assign to the EO
      InventoryKey lReturnInv = Domain.createSerializedInventory( aInventory -> {

         aInventory.setOwner( iLocalOwner );
         aInventory.setPartNumber( lPartNo );
         aInventory.setOwnershipType( RefOwnerTypeKey.LOCAL );
         aInventory.setLocation( lDockLocation );
         aInventory.setSerialNumber( SER_INV_SN );
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setOrderLine( lEOLine );

      } );

      // set the local inventory for outbound return on the exchange order
      OrderServiceFactory.getInstance().getLineService().setReturnInventory( lEOLine, lReturnInv,
            iHr );

      // get the outbound shipment line
      PoLine lPoLine = new PoLine( lEOLine );
      ShipmentKey lXchgShipment = lPoLine.getXchgShipment();

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

      Date lReceivedDate = new Date();

      // Create shipment line transfer object
      ReceiveShipmentLineTO lReceiveShipmentLineTO =
            new ReceiveShipmentLineTO( lInboundShipmentLine );
      lReceiveShipmentLineTO.setPartNo( lPartNo );
      lReceiveShipmentLineTO.setReceivedQty( BIN_QT_OF_SER_RECEIVED_INV );
      lReceiveShipmentLineTO.setReceivedDocs( true );
      lReceiveShipmentLineTO.setManufacturedDate( new Date() );
      lReceiveShipmentLineTO.setRouteCond( RefInvCondKey.RFI, "" );
      lReceiveShipmentLineTO.setSerialNo( SER_INV_SN );

      ReceiveShipmentLineTO[] lReceiveShipmentLineTOArray =
            new ReceiveShipmentLineTO[] { lReceiveShipmentLineTO };

      // Receive the inbound shipment from the vendor
      ShipmentService.receive( lInboundShipment, lReceivedDate, lReceiveShipmentLineTOArray, null,
            iHr );

      InventoryKey[] lInventory = ShipmentService.getInventory( lInboundShipment );

      // inspect the received inventory
      new DefaultConditionService().inspectInventory( lInventory[0], new InspectInventoryTO(),
            iHr );

      // ACTION: Execute the Query
      execute( iExchangeOrder );

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      assertEquals( lInventory[0], iQuerySet.getKey( InventoryKey.class, "inv_key" ) );

   }


   /**
    * Test if an inventory is returned without invoice is marked as to-be-paid, this inventory
    * should not be shown in the query result. For example: If ordered 10, received 10, then return
    * 3 to vendor, there should be one row in the Inventory Receipts group box in the Receipt and
    * Return tab of order details page. The received inventory bin quantity should be 7.
    */
   @Test
   public void
         testGivenABatchReceivedInventoryWhenReturnedWithoutInvoiceMarkedToBePaidThenNotShowReturnedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.INSPREQ;

      InventoryKey lBatchReceivedInv = prepareDataForBatchInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      double lNewBinQt = 7.0;

      // Split received inventory to two
      InventoryKey lSplitInv = splitReceivedInventory( lBatchReceivedInv, lNewBinQt,
            RefInvCondKey.ARCHIVE, BIN_QT_OF_BATCH_RECEIVED_INV - lNewBinQt );

      // Return inventory by creating completed outbound shipment
      createCompletedOutboundShipment( iPurchaseOrder, iPoLine, lSplitInv,
            RefShipmentTypeKey.RTNVEN, null );

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertOneBatchInventoryInTheResult( lBatchReceivedInv, lNewBinQt );
   }


   /**
    * Test if an inventory is returned after the date when invoice is marked as to-be-paid, this
    * inventory should be shown in the query result. For example: If ordered 10, received 10,
    * created an invoice and marked it as to-be-paid, then return 3 to vendor, there should be two
    * rows in the Inventory Receipts group box in the Receipt and Return tab of order details page.
    * One is received inventory with bin quantity of 7, another one is archived inventory with bin
    * quantity of 3.
    */
   @Test
   public void
         testGivenABatchReceivedInventoryWhenReturnedAfterInvoiceMarkedToBePaidThenShowReturnedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.RFI;

      InventoryKey lBatchReceivedInv = prepareDataForBatchInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      double lNewBinQt = 7.0;
      double lSplitBinQt = BIN_QT_OF_BATCH_RECEIVED_INV - lNewBinQt;

      // Split received inventory to two
      RefInvCondKey lConditionOfSplitInv = RefInvCondKey.ARCHIVE;
      InventoryKey lSplitInv = splitReceivedInventory( lBatchReceivedInv, lNewBinQt,
            lConditionOfSplitInv, lSplitBinQt );

      // Create invoice on the order line, mark invoice to be paid, and then return inventory
      Date lMarkedTobePaidDate = new Date();
      returnAfterMarkedToBePaid( lMarkedTobePaidDate, lSplitInv );

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertTwoBatchInventoriesInTheResult( lBatchReceivedInv, lNewBinQt, lSplitInv,
            lConditionOfSplitInv, lSplitBinQt );
   }


   /**
    * Test if an inventory is returned after in another order, the returned inventory should be
    * shown in the query result for this order. For example: If ordered 10, received 10, created
    * work package on 3 and scheduled externally to create a repair order, then sent the split
    * inventory with 3 to vendor, there should be two rows in the Inventory Receipts group box in
    * the Receipt and Return tab of order details page for this order. One is received inventory
    * with bin quantity of 7, another one is INREP inventory with bin quantity of 3.
    */
   @Test
   public void
         testGivenABatchReceivedInventoryWhenReturnedInAnotherOrderThenShowReturnedInventoryInThisOrder() {

      iConditionOfReceivedInv = RefInvCondKey.REPREQ;

      InventoryKey lBatchReceivedInv = prepareDataForBatchInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      double lNewBinQt = 7.0;
      double lSplitBinQt = BIN_QT_OF_BATCH_RECEIVED_INV - lNewBinQt;

      // Split received inventory to two
      RefInvCondKey lConditionOfSplitInv = RefInvCondKey.INREP;
      InventoryKey lSplitInv = splitReceivedInventory( lBatchReceivedInv, lNewBinQt,
            lConditionOfSplitInv, lSplitBinQt );

      // ---------------------------------------------------------------------------------
      // Send inventory by creating completed outbound shipment on another order
      // ---------------------------------------------------------------------------------

      PurchaseOrderKey lAnotherOrder = new OrderBuilder().build();

      ShipmentKey lOutboundShipment = new ShipmentDomainBuilder().withOrder( lAnotherOrder )
            .toLocation( iVendorLocation ).withStatus( RefEventStatusKey.IXCMPLT ).build();

      new ShipmentLineBuilder( lOutboundShipment )
            .forOrderLine( new OrderLineBuilder( lAnotherOrder )
                  .forPart( new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).build() ).build() )
            .forInventory( lSplitInv ).build();

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertTwoBatchInventoriesInTheResult( lBatchReceivedInv, lNewBinQt, lSplitInv,
            lConditionOfSplitInv, lSplitBinQt );

   }


   /**
    * Test if a serialized inventory is returning to vendor, but hasn't been sent yet, this
    * inventory should be shown in the Inventory Receipts group box in the Receipt and Return tab of
    * order details page.
    */
   @Test
   public void
         testGivenASerReceivedInventoryWhenReturnToVendorButNotSendThenShowReturnedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.INSPREQ;

      InventoryKey lSerReceivedInv = prepareDataForSerializedInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      // Return inventory by creating completed outbound shipment
      createOutboundShipment( iPurchaseOrder, iPoLine, lSerReceivedInv, null,
            RefShipmentTypeKey.RTNVEN, RefEventStatusKey.IXPEND );

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertOneBatchInventoryInTheResult( lSerReceivedInv, null );
   }


   /**
    * Test if a serialized inventory is sent to vendor without invoice marked to-be-paid, this
    * inventory should not be shown in the Inventory Receipts group box in the Receipt and Return
    * tab of order details page.
    */
   @Test
   public void
         testGivenASerReceivedInventoryWhenReturnToVendorWithoutInvoiceMarkToBePaidThenNotShowReturnedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.INSPREQ;

      InventoryKey lSerReceivedInv = prepareDataForSerializedInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      // Return inventory by creating completed outbound shipment
      createCompletedOutboundShipment( iPurchaseOrder, iPoLine, lSerReceivedInv,
            RefShipmentTypeKey.RTNVEN, null );

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      // TEST: Confirm the result
      assertEquals( 0, iQuerySet.getRowCount() );
   }


   /**
    * Test if a serialized inventory is sent to vendor after invoice marked to-be-paid, this
    * inventory should be shown in the Inventory Receipts group box in the Receipt and Return tab of
    * order details page.
    */
   @Test
   public void
         testGivenASerReceivedInventoryWhenReturnToVendorAfterInvoiceMarkToBePaidThenShowReturnedInventory() {

      iConditionOfReceivedInv = RefInvCondKey.RFI;

      InventoryKey lSerReceivedInv = prepareDataForSerializedInventory( iPurchaseOrder,
            RefPoLineTypeKey.PURCHASE, iConditionOfReceivedInv, iInboundShipment );

      // Create invoice on the order line, mark invoice to be paid, and then return inventory
      Date lMarkedTobePaidDate = new Date();
      returnAfterMarkedToBePaid( lMarkedTobePaidDate, lSerReceivedInv );

      // ACTION: Execute the Query
      execute( iPurchaseOrder );

      assertOneBatchInventoryInTheResult( lSerReceivedInv, null );
   }


   /**
    * WHEN repair order is created and received completely, THEN there should be one receipt fetched
    * and that receipt belongs to latest inbound shipment. AND the same inventory return to vendor
    * and and received back, THEN one receipt line should be fetched for new inbound shipment, which
    * was created for return to vendor scenario
    *
    */
   @Test
   public void testRepairOrderReturnToVendorGetLatestShipment() {
      PurchaseOrderKey lRepairOrder = Domain.createPurchaseOrder( aPurchaseOrder -> {
         aPurchaseOrder.orderType( RefPoTypeKey.REPAIR ).status( RefEventStatusKey.POISSUED )
               .vendor( iVendor );
      } );
      ShipmentKey lInboundShipment =
            createCompletedInboundShipment( lRepairOrder, RefShipmentTypeKey.REPAIR );

      InventoryKey lRoInventory = prepareDataForSerializedInventory( lRepairOrder,
            RefPoLineTypeKey.REPAIR, RefInvCondKey.REPREQ, lInboundShipment );
      createCompletedOutboundShipment( lRepairOrder, iPoLine, lRoInventory,
            RefShipmentTypeKey.SENDREP, new Date() );

      execute( lRepairOrder );

      // RO is received. assert the receipt count and shipment key
      assertEquals( 1, iQuerySet.getRowCount() );
      iQuerySet.next();
      assertEquals( lInboundShipment, iQuerySet.getKey( ShipmentKey.class, "shipment_key" ) );

      // return inventory to vendor. one inbound and one outbound shipment are created
      ShipmentKey lInboundShipmentForReturnToVenor =
            createCompletedInboundShipment( lRepairOrder, RefShipmentTypeKey.REPAIR );
      new ShipmentLineBuilder( lInboundShipmentForReturnToVenor ).forOrderLine( iPoLine )
            .withReceivedQuantity( BIN_QT_OF_SER_RECEIVED_INV ).forInventory( lRoInventory )
            .withSerialNo( "BN-SN" ).build();

      createCompletedOutboundShipment( lRepairOrder, iPoLine, lRoInventory,
            RefShipmentTypeKey.SENDREP, new Date() );

      execute( lRepairOrder );

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();
      assertEquals( lInboundShipmentForReturnToVenor,
            iQuerySet.getKey( ShipmentKey.class, "shipment_key" ) );

   }


   private InventoryKey prepareDataForBatchInventory( PurchaseOrderKey aPurchaseOrderKey,
         RefPoLineTypeKey aOrderType, RefInvCondKey aConditionOfReceivedInv,
         ShipmentKey aInboundShipment ) {
      return prepareData( aPurchaseOrderKey, aOrderType, RefInvClassKey.BATCH,
            aConditionOfReceivedInv, aInboundShipment, BIN_QT_OF_BATCH_RECEIVED_INV );
   }


   private InventoryKey prepareDataForSerializedInventory( PurchaseOrderKey aPurchaseOrderKey,
         RefPoLineTypeKey aOrderType, RefInvCondKey aConditionOfReceivedInv,
         ShipmentKey aInboundShipment ) {
      return prepareData( aPurchaseOrderKey, aOrderType, RefInvClassKey.SER,
            aConditionOfReceivedInv, aInboundShipment, BIN_QT_OF_SER_RECEIVED_INV );
   }


   private InventoryKey prepareData( PurchaseOrderKey aPurchaseOrderKey,
         RefPoLineTypeKey aOrderType, RefInvClassKey aInventoryClass,
         RefInvCondKey aConditionOfReceivedInv, ShipmentKey aInboundShipment, double aBinQt ) {

      PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( aInventoryClass ).build();

      iInventoryBuilder = new InventoryBuilder().withClass( aInventoryClass ).withPartNo( lPartNo )
            .withSerialNo( "BN-SN" );

      InventoryKey lReceivedInv =
            iInventoryBuilder.withCondition( aConditionOfReceivedInv ).withBinQt( aBinQt ).build();

      iPoLine = new OrderLineBuilder( aPurchaseOrderKey ).withLineType( aOrderType )
            .forPart( lPartNo ).withUnitType( RefQtyUnitKey.EA ).build();

      new ShipmentLineBuilder( aInboundShipment ).forOrderLine( iPoLine )
            .withReceivedQuantity( aBinQt ).forInventory( lReceivedInv ).build();

      return lReceivedInv;
   }


   private InventoryKey splitReceivedInventory( InventoryKey aReceivedInv, double aNewBinQt,
         RefInvCondKey aConditionOfSplitInv, double aSplitBinQt ) {

      InvInvTable lReceivedInvTable = InvInvTable.findByPrimaryKey( aReceivedInv );
      lReceivedInvTable.setBinQt( aNewBinQt );
      lReceivedInvTable.update();

      return iInventoryBuilder.withCondition( aConditionOfSplitInv ).withBinQt( aSplitBinQt )
            .build();
   }


   private void returnAfterMarkedToBePaid( Date aMarkedToBePaidDate, InventoryKey aReturnedInv ) {

      PurchaseInvoiceKey lInvoice = Domain.createInvoice( invoice -> {
         invoice.setStageStatus( RefEventStatusKey.PITOBEPAID );
         invoice.setStageDate( aMarkedToBePaidDate );
      } );

      new InvoiceLineBuilder( lInvoice ).mapToOrderLine( iPoLine ).build();

      // Return inventory by creating completed outbound shipment
      createCompletedOutboundShipment( iPurchaseOrder, iPoLine, aReturnedInv,
            RefShipmentTypeKey.RTNVEN, DateUtils.addDays( aMarkedToBePaidDate, 1 ) );
   }


   private ShipmentKey createCompletedOutboundShipment( PurchaseOrderKey aPurchaseOrder,
         PurchaseOrderLineKey aPoLine, InventoryKey aSentInventory,
         RefShipmentTypeKey aShipmentType, Date aSentDate ) {
      return createOutboundShipment( aPurchaseOrder, aPoLine, aSentInventory, aSentDate,
            aShipmentType, RefEventStatusKey.IXCMPLT );
   }


   private ShipmentKey createCompletedInboundShipment( PurchaseOrderKey aPurchaseOrder,
         RefShipmentTypeKey aShipmentType ) {
      return new ShipmentDomainBuilder().withOrder( aPurchaseOrder ).withType( aShipmentType )
            .withStatus( RefEventStatusKey.IXCMPLT ).build();
   }


   private ShipmentKey createOutboundShipment( PurchaseOrderKey aPurchaseOrder,
         PurchaseOrderLineKey aPoLine, InventoryKey aSentInventory, Date aSentDate,
         RefShipmentTypeKey aShipmentType, RefEventStatusKey aShipmentStatus ) {

      ShipmentKey lReturnShipment = new ShipmentDomainBuilder().withType( aShipmentType )
            .withOrder( iPurchaseOrder ).toLocation( iVendorLocation ).withStatus( aShipmentStatus )
            .withActualShipmentDate( aSentDate ).build();

      new ShipmentLineBuilder( lReturnShipment ).forOrderLine( aPoLine )
            .forInventory( aSentInventory ).build();
      return lReturnShipment;
   }


   private void execute( PurchaseOrderKey aPoKey ) {

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aPoKey, "aPoDbId", "aPoId" );

      iQuerySet = QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   private void assertOneBatchInventoryInTheResult( InventoryKey aExpectedReceivedInv,
         Double aExpectedBinQt ) {

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      assertEquals( aExpectedReceivedInv, iQuerySet.getKey( InventoryKey.class, "inv_key" ) );

      if ( aExpectedBinQt != null ) {
         assertEquals( aExpectedBinQt, iQuerySet.getDouble( "bin_qt" ), 0 );
      }
   }


   private void assertTwoBatchInventoriesInTheResult( InventoryKey aExpectedReceivedInv,
         double aExpectedBinQtOfReceivedInv, InventoryKey aExpectedAnotherInv,
         RefInvCondKey aExpectedConditionOfAnotherInv, double aExpectedBinQtOfAnotherInv ) {

      assertEquals( 2, iQuerySet.getRowCount() );

      boolean lHasReceivedInv = false;
      boolean lHasAnotherInv = false;

      while ( iQuerySet.next() ) {

         InventoryKey lInventory = iQuerySet.getKey( InventoryKey.class, "inv_key" );
         String lInvConditionCd = iQuerySet.getString( "inventory_condition_cd" );
         double lBinQty = iQuerySet.getDouble( "bin_qt" );

         if ( iConditionOfReceivedInv.getCd().equals( lInvConditionCd ) ) {

            lHasReceivedInv = true;
            assertEquals( aExpectedReceivedInv, lInventory );
            assertEquals( aExpectedBinQtOfReceivedInv, lBinQty, 0 );

         } else if ( aExpectedConditionOfAnotherInv.getCd().equals( lInvConditionCd ) ) {

            lHasAnotherInv = true;
            assertEquals( aExpectedAnotherInv, lInventory );
            assertEquals( aExpectedBinQtOfAnotherInv, lBinQty, 0 );
         }
      }

      if ( !lHasReceivedInv ) {
         Assert.fail( "No received inventory is found in the results" );
      }

      if ( !lHasAnotherInv ) {
         Assert.fail( "No another inventory is found in the results" );
      }
   }

}
