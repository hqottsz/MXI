package com.mxi.mx.core.services.purchase;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.ComponentWorkPackage;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.model.order.exception.InvalidRepairOrderLineQuantityException;
import com.mxi.mx.core.services.order.EditOrderLineTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;


/**
 * This UT verifies that the inbound shipment's qty is correctly updated when the RO Line's qty is
 * decreased or increased respectively.
 *
 */

public class ROLineChangeQuantityTest {

   private static final String ACCOUNT_CD = "TESTACCOUNT";
   private static final String ALT_ACCOUNT_CD = "TESTALTACCOUNT";
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;

   private static final double BINQTY_100 = 100;
   private static final double BINQTY_125 = 125;

   private FncAccountKey iAccount;
   private HumanResourceKey iHr;
   private VendorKey iVendor;
   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;

   private PurchaseOrderLineKey iOrderLine;
   private PartNoKey iBatchPart;
   private InventoryKey iBatchInventory;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Pre-condition: The order status is ISSUED. This test verifies the shipment line's expected
    * quantity is increased after the RO's order line quantity has increased.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void singleOrderLineQuantityIncreased() throws Exception {

      PurchaseOrderKey lOrder = createRepairOrderBuilder( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine =
            createOrderLineBuilder( lOrder, iBatchPart, RefPoLineTypeKey.REPAIR,
                  new BigDecimal( 100 ) ).withReceivedQuantity( BigDecimal.ZERO ).build();

      ShipmentKey lShipment = createInboundShipmentBuilder( lOrder, RefEventStatusKey.IXPEND,
            iVendorLocation, iDockLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLineBuilder( lShipment, iBatchPart, lOrderLine, BINQTY_100 ).build();

      updateBatchInventoryQty( BINQTY_125 );

      // increase original quantity
      EditOrderLineTO lEditOrderLineTo = editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, BINQTY_125 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that shipment line expected quantity has increased as well
      assertEquals( new Double( BINQTY_125 ),
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt() );
   }


   /**
    * Pre-condition: The order status is ISSUED. This test verifies an increase to the RO's Lines
    * Quantity does not exceed that of the associated Batch Inventories Bin Qty.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test( expected = InvalidRepairOrderLineQuantityException.class )
   public void singleOrderLineQuantityIncreasedBeyondBinQty() throws Exception {

      PurchaseOrderKey lOrder = createRepairOrderBuilder( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine =
            createOrderLineBuilder( lOrder, iBatchPart, RefPoLineTypeKey.REPAIR,
                  new BigDecimal( 100 ) ).withReceivedQuantity( BigDecimal.ZERO ).build();

      ShipmentKey lShipment = createInboundShipmentBuilder( lOrder, RefEventStatusKey.IXPEND,
            iVendorLocation, iDockLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLineBuilder( lShipment, iBatchPart, lOrderLine, 100 ).build();

      // increase original quantity
      EditOrderLineTO lEditOrderLineTo = editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, 125 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

   }


   /**
    * Change the batch inventories Bin Qty.
    *
    * @param anewBinQty
    *           the new Bin quantity
    *
    */
   private void updateBatchInventoryQty( double aExpectedQuantity ) {
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInventory );
      lInvInv.setBinQt( aExpectedQuantity );
      lInvInv.update();
   }


   /**
    * Prepare the EditOrderLineTO.
    *
    *
    * @param aOrderLine
    *           the order line key
    * @param aAccountCode
    *           the account code
    * @param aQuantity
    *           the received quantity
    *
    * @return EditOrderLineTO the transfer objects
    *
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    */
   private EditOrderLineTO editOrderLineTOSetup( PurchaseOrderLineKey aOrderLine,
         String aAccountCode, double aQuantity )
         throws MandatoryArgumentException, NegativeValueException {

      // update order line - quantity decreased
      POLineTable lPOLineTable = POLineTable.findByPrimaryKey( aOrderLine );

      // build TO
      EditOrderLineTO lEditOrderLineTO = new EditOrderLineTO( aOrderLine );
      lEditOrderLineTO.setUnitPrice( lPOLineTable.getUnitPrice(), "unit price" );
      lEditOrderLineTO.setQuantityUnit( lPOLineTable.getQtyUnit(), "unit type" );
      lEditOrderLineTO.setAccountCode( aAccountCode, "account code" );
      lEditOrderLineTO.setQuantity( aQuantity, "order_quantity" );

      return lEditOrderLineTO;

   }


   /**
    * Create a shipment line.
    *
    * @param aShipment
    *           the shipment
    * @param aPart
    *           the part
    * @param aOrderLine
    *           the order line
    * @param aExpectedQuantity
    *           the expected quantity
    *
    * @return ShipmentLineKey the shipment line key
    */
   private ShipmentLineBuilder createShipmentLineBuilder( ShipmentKey aShipment, PartNoKey aPart,
         PurchaseOrderLineKey aOrderLine, double aExpectedQuantity ) {
      return new ShipmentLineBuilder( aShipment ).forPart( aPart ).forOrderLine( aOrderLine )
            .withExpectedQuantity( aExpectedQuantity );
   }


   /**
    * Create an inbound shipment.
    *
    * @param aOrder
    *           the Order
    * @param aOrderStatus
    *           the shipment status
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentDomainBuilder createInboundShipmentBuilder( PurchaseOrderKey aOrder,
         RefEventStatusKey aOrderStatus, LocationKey aShipFromLocation,
         LocationKey aShipToLocation ) {
      return new ShipmentDomainBuilder().withOrder( aOrder ).withStatus( aOrderStatus )
            .fromLocation( aShipFromLocation ).toLocation( aShipToLocation )
            .withType( RefShipmentTypeKey.REPAIR );
   }


   /**
    * Create a order line builder.
    *
    * @param aOrder
    *           the Order
    * @param aPartNo
    *           the Part
    * @param aPoLineType
    *           the Order line type
    * @param aOrderQty
    *           The order quantity
    *
    * @return OrderLineBuilder the order line builder
    */
   private OrderLineBuilder createOrderLineBuilder( PurchaseOrderKey aOrder, PartNoKey aPartNo,
         RefPoLineTypeKey aPoLineType, BigDecimal aOrderQty ) {

      final TaskKey lWorkPackage =
            Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

               @Override
               public void configure( ComponentWorkPackage aBuilder ) {
                  aBuilder.setInventory( iBatchInventory );
               }

            } );

      return new OrderLineBuilder( aOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( aPoLineType )
            .withOrderQuantity( aOrderQty ).promisedBy( new Date() ).forPart( aPartNo )
            .withAccount( iAccount ).withTask( lWorkPackage );
   }


   /**
    * Create a order builder.
    *
    * @param aOrderStatus
    *           The order status
    *
    * @return OrderBuilder the order builder
    */
   private OrderBuilder createRepairOrderBuilder( RefEventStatusKey aOrderStatus ) {
      return new OrderBuilder().withStatus( aOrderStatus ).withVendor( iVendor )
            .shippingTo( iDockLocation ).withIssueDate( new Date() )
            .withOrderType( RefPoTypeKey.REPAIR );
   }


   /**
    * Create a part builder with the given class.
    *
    * @param aInventoryClass
    *           the inventory class
    *
    * @return PartNoBuilder the part no builder
    */
   private PartNoBuilder createPartBuilder( RefInvClassKey aInventoryClass ) {
      return new PartNoBuilder().withInventoryClass( aInventoryClass );
   }


   /**
    * Create a part builder with the given class.
    *
    * @param aInventoryClass
    *           the inventory class
    *
    * @return PartNoBuilder the part no builder
    */
   private InventoryBuilder createInventoryBuilder( PartNoKey aPartNoKey ) {

      return new InventoryBuilder().withPartNo( aPartNoKey ).withClass( RefInvClassKey.BATCH )
            .withBinQt( 100 );
   }


   @Before
   public void loadData() throws Exception {

      // create a HR
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // create a new batch part
      iBatchPart = createPartBuilder( RefInvClassKey.BATCH ).build();

      // create a new inventory item from that batch part
      iBatchInventory = createInventoryBuilder( iBatchPart ).build();

      // create a dock location
      iDockLocation =
            new LocationDomainBuilder().withCode( "LOC1/DOCK" ).withType( RefLocTypeKey.DOCK ).build();

      // create a vendor location
      iVendorLocation =
            new LocationDomainBuilder().withCode( "VENDOR1" ).withType( RefLocTypeKey.VENDOR ).build();

      // build finance account
      iAccount = new AccountBuilder().withCode( ACCOUNT_CD ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

   }

}
