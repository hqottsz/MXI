
package com.mxi.mx.core.unittest.po;

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
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.model.order.exception.InvalidRepairOrderLineQuantityException;
import com.mxi.mx.core.services.order.EditOrderLineTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.po.PoLine;


/**
 * This class tests the PurchaseOrderBean.setPOLines() method. It is different version of
 * POSetPOLinesTest. This test class extends DatabaseTestCase and all the test data are created with
 * data builder, so it is dependent to the database.
 *
 * @author Libin Cai
 * @created April 30, 2015
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class POSetPOLinesDbTest {

   /** the order line unit price */
   private static final BigDecimal LINE_UNIT_PRICE = new BigDecimal( 10 );

   /** the order line quantity unit */
   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;

   /** the order line quantity */
   private static final BigDecimal LINE_QTY = new BigDecimal( 20 );

   /** the finance account code */
   private static final String ACCOUNT_CD = "TESTACCOUNT";

   private PurchaseOrderKey iRO;
   private EditOrderLineTO iLineTo;
   private HumanResourceKey iHr;
   private LocationKey iSupplyLocation;
   private LocationKey iDockLocation;
   private LocationKey iVendorLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the InvalidRepairOrderLineQuantityException is thrown when update the quantity for
    * the order line quantity different than the inventory bin quantity.
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testInvalidRepairOrderLineQuantityExceptionWhenChangeQuantity() throws Exception {

      iLineTo.setQuantity( 30.0, "quantity" );

      setOrderLineAndAssert();

   }


   /**
    * Test the price can be changed when the line quantity is different than the inventory bin
    * quantity.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testChangePrice() throws Exception {

      BigDecimal lNewUnitPrice = new BigDecimal( 300.0 );

      iLineTo.setUnitPrice( lNewUnitPrice, "unit price" );

      OrderService.setPOLines( iRO, new EditOrderLineTO[] { iLineTo }, iHr );

      new PoLine( new PurchaseOrderLineKey( iRO, 1 ) ).assertUnitPrice( lNewUnitPrice );

   }


   /**
    * Set the po line and assert the exception is thrown.
    *
    * @throws MxException
    *            if a mx error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   private void setOrderLineAndAssert() throws MxException, TriggerException {

      try {
         OrderService.setPOLines( iRO, new EditOrderLineTO[] { iLineTo }, iHr );

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( "Expected InvalidRepairOrderLineQuantityException" );

      } catch ( InvalidRepairOrderLineQuantityException e ) {
         ; // do nothing
      }
   }


   /**
    * Tests that the InvalidRepairOrderLineQuantityException is thrown when update the quantity unit
    * for the order line quantity different than the inventory bin quantity.
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testInvalidRepairOrderLineQuantityExceptionWhenChangeQuantityUnit()
         throws Exception {

      iLineTo.setQuantityUnit( new RefQtyUnitKey( 10, "BAG" ), "Quantity Unit" );;

      try {
         OrderService.setPOLines( iRO, new EditOrderLineTO[] { iLineTo },
               new HumanResourceDomainBuilder().build() );

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( "Expected InvalidRepairOrderLineQuantityException" );

      } catch ( InvalidRepairOrderLineQuantityException e ) {
         ; // do nothing
      }

   }


   /**
    * Tests that increase in purchase order line quantity updates the pending shipment line quantity
    * with the updated value
    *
    * @throws Exception
    */
   @Test
   public void testIncreaseOrderLineQty() throws Exception {

      PartNoKey lBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).build();

      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .withStatus( RefEventStatusKey.POISSUED ).withIssueDate( new Date() )
            .withVendor( new VendorBuilder().withCode( "TESTVENDOR" ).build() ).build();

      // make the line quantity different than the inventory bin quantity
      PurchaseOrderLineKey lOrderLineKey = new OrderLineBuilder( lPO ).forPart( lBatchPart )
            .withOrderQuantity( LINE_QTY ).withUnitType( QTY_UNIT ).withUnitPrice( LINE_UNIT_PRICE )
            .withAccount( new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
                  .withCode( ACCOUNT_CD ).isDefault().build() )
            .build();

      // build shipment for oder
      final ShipmentKey lShipment =
            new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
                  .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();

      // build shipment line
      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLineKey = new ShipmentLineBuilder( lShipment ).forPart( lBatchPart )
            .forOrderLine( lOrderLineKey ).withExpectedQuantity( LINE_QTY.doubleValue() ).build();

      // now edit the PO line qty
      EditOrderLineTO lEditOrderLineTO = new EditOrderLineTO( new PurchaseOrderLineKey( lPO, 1 ) );
      Double lNewLineQty = new Double( 25 );
      lEditOrderLineTO.setQuantity( lNewLineQty, "quantity" );
      lEditOrderLineTO.setQuantityUnit( QTY_UNIT, "quantity unit" );
      lEditOrderLineTO.setUnitPrice( LINE_UNIT_PRICE, "unit price" );
      lEditOrderLineTO.setAccountCode( ACCOUNT_CD, "account code" );

      OrderService.setPOLines( lPO, new EditOrderLineTO[] { lEditOrderLineTO }, iHr );

      // assert the PO line qty is updated
      assertEquals( POLineTable.findByPrimaryKey( lOrderLineKey ).getOrderQt().doubleValue(),
            lNewLineQty.doubleValue(), 0f );

      // assert that the order status is changed back to POAUTH
      assertEquals( EvtEventTable.findByPrimaryKey( lPO.getEventKey() ).getEventStatusCd(),
            RefEventStatusKey.POAUTH.getCd() );

      // assert that the shipment line quantity is updated to new PO line qty
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLineKey ).getExpectQt(),
            lNewLineQty, 0f );

      // increase the quantity again on the PO line, and test that the qty gets updated on the
      // shipment, testing this scenario because once the order status changes to POAUTH different
      // class handles the increaseBatch logic
      lEditOrderLineTO = new EditOrderLineTO( new PurchaseOrderLineKey( lPO, 1 ) );
      Double lNewLineQty2 = new Double( 30 );
      lEditOrderLineTO.setQuantity( lNewLineQty2, "quantity" );
      lEditOrderLineTO.setQuantityUnit( QTY_UNIT, "quantity unit" );
      lEditOrderLineTO.setUnitPrice( LINE_UNIT_PRICE, "unit price" );
      lEditOrderLineTO.setAccountCode( ACCOUNT_CD, "account code" );

      OrderService.setPOLines( lPO, new EditOrderLineTO[] { lEditOrderLineTO }, iHr );

      // assert the PO line qty is updated
      assertEquals( POLineTable.findByPrimaryKey( lOrderLineKey ).getOrderQt().doubleValue(),
            lNewLineQty2.doubleValue(), 0f );

      // assert that the order status is changes to POAUTH
      assertEquals( EvtEventTable.findByPrimaryKey( lPO.getEventKey() ).getEventStatusCd(),
            RefEventStatusKey.POAUTH.getCd() );

      // assert that the shipment line quantity is updated to new PO line qty
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLineKey ).getExpectQt(),
            lNewLineQty2 );
   }


   /**
    * Setup any instance variables.
    *
    * @exception Exception
    *               If an error occurs.
    */
   @Before
   public void setUp() throws Exception {
      PartNoKey lBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).build();

      iRO = new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
            .withVendor( new VendorBuilder().withCode( "TESTVENDOR" ).build() ).build();

      // make the line quantity different than the inventory bin quantity
      new OrderLineBuilder( iRO ).forPart( lBatchPart ).withOrderQuantity( LINE_QTY )
            .withUnitType( QTY_UNIT ).withUnitPrice( LINE_UNIT_PRICE )
            .withTask( new TaskBuilder().onInventory( new InventoryBuilder()
                  .withClass( RefInvClassKey.BATCH ).withCondition( RefInvCondKey.REPREQ )
                  .withPartNo( lBatchPart ).withBinQt( LINE_QTY.doubleValue() - 10 ).build() )
                  .build() )
            .build();

      iLineTo = new EditOrderLineTO( new PurchaseOrderLineKey( iRO, 1 ) );
      iLineTo.setQuantity( LINE_QTY.doubleValue(), "quantity" );
      iLineTo.setQuantityUnit( QTY_UNIT, "quantity unit" );

      // create a vendor location
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      iHr = new HumanResourceDomainBuilder().build();

   }

}
