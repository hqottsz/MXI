package com.mxi.mx.core.services.purchase.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.purchase.invoice.InvoiceOrderStateValidator.InvoiceOrderHasInvalidStateException;


/**
 * Tests the {@link InvoiceOrderStateValidator}.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class InvoiceOrderStateValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Check an exception is thrown when the order is invalid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test( expected = InvoiceOrderHasInvalidStateException.class )
   public void testExceptionThrownWhenOrderIsInvalid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createUnissuedPurchaseOrder( RefEventStatusKey.POOPEN );

      // when
      new InvoiceOrderStateValidator( lPurchaseOrder ).validate();

      // then -- exception thrown
   }


   /**
    * An open order is invalid.
    *
    */
   @Test
   public void testOpenOrderIsInvalid() {

      // given
      PurchaseOrderKey lPurchaseOrder = createUnissuedPurchaseOrder( RefEventStatusKey.POOPEN );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( false, lOrderIsValid );
   }


   /**
    * An open order that was issued before is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testOpenOrderThatWasIssuedIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POOPEN );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * An authenticated order is invalid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testAuthOrderIsInvalid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createUnissuedPurchaseOrder( RefEventStatusKey.POAUTH );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( false, lOrderIsValid );
   }


   /**
    * An authenticated order that was issued before is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testAuthOrderThatWasIssuedIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POAUTH );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * An issued order is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testIssuedOrderIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POISSUED );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * An acknowledged order is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testAcknowledgedOrderIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder =
            createIssuedPurchaseOrder( RefEventStatusKey.POACKNOWLEDGED );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * A partial order is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testPartialOrderIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POPARTIAL );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * A received order is valid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testReceivedOrderIsValid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.PORECEIVED );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( true, lOrderIsValid );
   }


   /**
    * A cancelled order is invalid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testCanceledOrderIsInvalid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POCANCEL );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( false, lOrderIsValid );
   }


   /**
    * A closed order is invalid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testClosedOrderIsInvalid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder( RefEventStatusKey.POCLOSED );

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lPurchaseOrder ).isValid();

      // then
      assertEquals( false, lOrderIsValid );
   }


   /**
    * A consignment-exchange order is invalid.
    *
    * @throws InvoiceOrderHasInvalidStateException
    */
   @Test
   public void testConsignmentExchangeOrderIsInvalid() throws InvoiceOrderHasInvalidStateException {

      // given
      PurchaseOrderKey lConsignmentExchangeOrder = createConsignmentExchangeOrder();

      // when
      boolean lOrderIsValid = new InvoiceOrderStateValidator( lConsignmentExchangeOrder ).isValid();

      // then
      assertEquals( false, lOrderIsValid );
   }


   /**
    * Create a purchase order with the given status and issued state.
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createOrder( RefPoTypeKey aRefPoType, RefEventStatusKey aStatus,
         boolean aIsIssued ) {

      VendorKey lVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();
      RefCurrencyKey lDefaultCurrency = new CurrencyBuilder( "USD" ).isDefault().build();

      return new OrderBuilder().withOrderType( aRefPoType ).withVendor( lVendor )
            .usingCurrency( lDefaultCurrency ).withExchangeRate( BigDecimal.ONE )
            .withStatus( aStatus ).withRevisionNumber( aIsIssued ? 1 : 0 ).build();
   }


   /**
    * Create a purchase order with the given status that has been issued before.
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createIssuedPurchaseOrder( RefEventStatusKey aStatus ) {
      return createOrder( RefPoTypeKey.PURCHASE, aStatus, true );
   }


   /**
    * Create a purchase order with the given status that has never been issued.
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createUnissuedPurchaseOrder( RefEventStatusKey aStatus ) {
      return createOrder( RefPoTypeKey.PURCHASE, aStatus, false );
   }


   /**
    * Create a purchase order with the given status that has never been issued.
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createConsignmentExchangeOrder() {
      return createOrder( RefPoTypeKey.CSGNXCHG, RefEventStatusKey.POISSUED, true );
   }
}
