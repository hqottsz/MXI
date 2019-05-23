package com.mxi.mx.core.ejb.po;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.servlet.EntityContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.services.order.ConvertOrdersTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.services.order.OrderServiceFactory;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoLineCharge;
import com.mxi.mx.core.table.procurement.ChargeTable;


/**
 * This is a unit test for purchase order bean
 *
 * @author srchlk
 *
 */
public class PurchaseOrderBeanTest {

   private PurchaseOrderKey iPurchaseOrder;
   private EntityContextFake iEntityContextFake = new EntityContextFake();

   /** the purchase price */
   public static final BigDecimal PURCHASE_PRICE = new BigDecimal( "500" );

   /** the borrow charges */
   public static final BigDecimal BORROW_CHARGES = new BigDecimal( "200" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    *
    * GIVEN borrow order with order line for a TRK part, borrow charges and purchase price, WHEN
    * borrow order is converted to purchase order THEN total line price = borrow charge + purchase
    * price
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testTotalLinePriceWhenConvertBorrowToPurchseTRKPart()
         throws MxException, TriggerException {

      assertTotalLinePrice( RefInvClassKey.TRK );

   }


   /**
    *
    * GIVEN borrow order with order line for a ASSY part, borrow charges and purchase price, WHEN
    * borrow order is converted to purchase order THEN total line price = borrow charge + purchase
    * price
    *
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testTotalLinePriceWhenConvertBorrowToPurchseASSYPart()
         throws MxException, TriggerException {

      assertTotalLinePrice( RefInvClassKey.ASSY );

   }


   /**
    *
    * GIVEN borrow order with order line for a SER part, borrow charges and purchase price, WHEN
    * borrow order is converted to purchase order THEN total line price = borrow charge + purchase
    * price
    *
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testTotalLinePriceWhenConvertBorrowToPurchseSERPart()
         throws MxException, TriggerException {

      assertTotalLinePrice( RefInvClassKey.SER );

   }


   /**
    * Builds a part with inv class aInvClass, add order line with borrow charge and purchase price
    * to a borrow order, converts the order to purchase order and checks whether the total line
    * price is updated correctly.
    *
    * @param aInvClass
    *           the inventory class
    * @throws MandatoryArgumentException
    *            if a mandatory argument is missing
    * @throws NegativeValueException
    *            if a negative value is encountered
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   private void assertTotalLinePrice( RefInvClassKey aInvClass )
         throws MandatoryArgumentException, NegativeValueException, MxException, TriggerException {

      // Build a part for a given inv class
      PartNoKey lPartNo =
            new PartNoBuilder().withStatus( RefPartStatusKey.ACTV ).withUnitType( RefQtyUnitKey.EA )
                  .withTotalValue( BigDecimal.ONE ).withInventoryClass( aInvClass ).build();

      // Add a purchase order line for the part with an order quantity of 1.0
      PurchaseOrderLineKey lPurchaseOrderLine = Domain.createPurchaseOrderLine( aLineKey -> {
         aLineKey.orderKey( iPurchaseOrder );
         aLineKey.part( lPartNo );
         aLineKey.orderQuantity( new BigDecimal( 1.0 ) );
      } );

      // Add a borrow charge to the PO line
      PoLineCharge lPoLineCharge =
            PoLineCharge.create( lPurchaseOrderLine, ChargeTable.create().insert() );
      lPoLineCharge.setChargeAmount( BORROW_CHARGES );
      lPoLineCharge.insert();

      // Create a transfer object with a purchase price
      ConvertOrdersTO lConvertOrdersTO = new ConvertOrdersTO( lPurchaseOrderLine );
      lConvertOrdersTO.setPurchasePrice( PURCHASE_PRICE );

      // Convert the borrow order to a purchase order
      OrderServiceFactory.getInstance().getLineConversionService()
            .convertBorrowLineToPurchase( lConvertOrdersTO, null, Domain.createHumanResource() );

      // Update the PO line prices
      OrderService.updateLinePrices( iPurchaseOrder );

      // Assert whether the total line price is equal to the purchase price+borrow charges
      assertEquals( "Line price = purchase price+borrow charges",
            PURCHASE_PRICE.add( BORROW_CHARGES ),
            POLineTable.findByPrimaryKey( lPurchaseOrderLine ).getLinePrice() );
   }


   @Before
   public void setUp() {
      // Create borrow order
      iPurchaseOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.BORROW );
         aOrder.setOrganization( Domain.createOrganization() );
         aOrder.shippingTo( Domain.createLocation() );
         aOrder.priority( RefReqPriorityKey.NORMAL );
      } );

   }


   @After
   public void tearDown() {
      iEntityContextFake.setPrimaryKey( null );
   }

}
