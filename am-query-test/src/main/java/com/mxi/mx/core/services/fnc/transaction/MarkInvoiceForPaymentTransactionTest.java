package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ChargeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.TaxKey;
import com.mxi.mx.core.services.fnc.transaction.AdjustQuantityTransactionTest.AdjustQuantityTransactionStub;
import com.mxi.mx.core.services.fnc.transaction.MarkInvoiceForPaymentTransactionTest.MarkInvoiceForPaymentTransactionStub.TransactionLine;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoHeaderTable;
import com.mxi.mx.core.table.po.PoInvoice;
import com.mxi.mx.core.table.po.PoInvoiceLine;
import com.mxi.mx.core.table.po.PoInvoiceLineMap;
import com.mxi.mx.core.table.po.PoLineCharge;
import com.mxi.mx.core.table.po.PoLineTax;
import com.mxi.mx.core.table.procurement.ChargeTable;
import com.mxi.mx.core.table.procurement.TaxTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the {@link AdjustQuantityTransaction} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MarkInvoiceForPaymentTransactionTest {

   /** The invoice price */
   private static final BigDecimal INVOICE_PRICE = new BigDecimal( 20 );

   /** The received quantity */
   private static final BigDecimal RECEIVED_QT = new BigDecimal( 3 );

   /** The invoice line price */
   private static final BigDecimal INVOICE_LINE_PRICE = INVOICE_PRICE.multiply( RECEIVED_QT );

   /** The po line unit price */
   private static final BigDecimal PO_LINE_UNIT_PRICE = new BigDecimal( 25 );

   /** The accrued value when purchasing */
   private static final BigDecimal PO_LINE_ACCRUED_VALUE =
         PO_LINE_UNIT_PRICE.multiply( RECEIVED_QT );

   /** The order quantity */
   private static final BigDecimal ORDER_QT = new BigDecimal( 30 );

   /**
    * The part total quantity. Assume there are 7 before receive the shipment and inspect as
    * serviceable.
    */
   private static final BigDecimal PART_TOTAL_QT = new BigDecimal( 7 ).add( RECEIVED_QT );

   /**
    * The old total value. Assume the value is 15 before receive the shipment and inspect as
    * serviceable.
    */
   private static final BigDecimal OLD_TOTAL_VALUE =
         new BigDecimal( 15 ).add( PO_LINE_UNIT_PRICE.multiply( RECEIVED_QT ) );

   private static final FncAccountKey EXPENSE_ACCOUNT1 = FncAccountKey.CONSIGN;
   private static final FncAccountKey ASSET_ACCOUNT = FncAccountKey.INVOICE;
   private static final FncAccountKey AP_ACCOUNT = FncAccountKey.AP;

   private EventKey iEventKey = new EventKey( 4650, 1 );

   private InventoryKey iInventoryKey = new InventoryKey( 4650, 1 );

   private OwnerKey iOwnerKey = new OwnerKey( 4650, 1 );
   private PartNoKey iPartNoKey = new PartNoKey( 4650, 1 );

   private PurchaseInvoiceKey iPoInvoiceKey;
   private PurchaseInvoiceLineKey iPoInvoiceLineKey;

   private PurchaseOrderLineKey iPoLineKey;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensures that value of inventory is properly debited/credited when changing part number
    */
   @SuppressWarnings( "deprecation" )
   @Test
   public void testValueOfInvoiceTransaction() {

      MarkInvoiceForPaymentTransactionStub lTx =
            new MarkInvoiceForPaymentTransactionStub( iPoInvoiceLineKey );
      lTx.execute();

      // check log
      assertThat( lTx.iFinancialTransaction.iUnitPrice, is( equalTo( INVOICE_PRICE ) ) );
      assertThat( lTx.iFinancialTransaction.iQuantity, is( equalTo( RECEIVED_QT ) ) );

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      // If the invoice price is less than the purchase price, we need to change the total value by
      // subtracting the difference.
      BigDecimal lNewTotalValue =
            OLD_TOTAL_VALUE.subtract( PO_LINE_ACCRUED_VALUE.subtract( INVOICE_LINE_PRICE ) );

      // Part total quanity is not changed after mark for payment. It only changes when the part is
      // received and inspected as servicealbe.

      // Verify the Total Quantity has not changed.
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( PART_TOTAL_QT ) ) );

      // Verify Total Value has decreased by [(Original Purchase Price - Invoiced Price) x Invoiced
      // Quantity].
      assertThat( lPartNoTable.getTotalValue(), is( equalTo( lNewTotalValue ) ) );

      // Verify Unit Price is (New Total Value / Total Quantity).
      assertThat( lPartNoTable.getAvgUnitPrice(),
            is( equalTo( lNewTotalValue.divide( PART_TOTAL_QT ) ) ) );

      // Verify Transaction Type is PAYINVC
      assertThat( lTx.iFinancialTransaction.iXactionType,
            is( equalTo( RefXactionTypeKey.PAYINVC ) ) );

      assertThat( lTx.iTransactionLines.size(), is( equalTo( 3 ) ) );

      // check debit and credit trx
      for ( int i = 0; i < lTx.iTransactionLines.size(); i++ ) {
         TransactionLine lTransactionLine = lTx.iTransactionLines.get( i );
         switch ( i ) {

            case 0:
               // AP Credit
               assertThat( lTransactionLine.iAccount, is( FncAccountKey.AP ) );
               assertThat( lTransactionLine.iAmount, is( equalTo( INVOICE_LINE_PRICE ) ) );
               assertThat( lTransactionLine.iDebit, is( false ) );
               break;

            case 1:
               // INVOICE debit
               assertThat( lTransactionLine.iAccount, is( FncAccountKey.INVOICE ) );
               assertThat( lTransactionLine.iAmount, is( equalTo( PO_LINE_ACCRUED_VALUE ) ) );
               assertThat( lTransactionLine.iDebit, is( true ) );
               break;

            case 2:
               // INVASSET credit
               assertThat( lTransactionLine.iAccount, is( lPartNoTable.getAssetAccount() ) );
               assertThat( lTransactionLine.iDebit, is( false ) );
               assertThat( lTransactionLine.iAmount,
                     is( equalTo( INVOICE_LINE_PRICE.subtract( PO_LINE_ACCRUED_VALUE ).abs() ) ) );

               break;

            default:
               MxAssert.fail( "Transaction is invalid." );

               break;
         }
      }

      // check accrued amount
      POLineTable lPoLine = POLineTable.findByPrimaryKey( iPoLineKey );
      assertThat( lPoLine.getAccruedValue(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      InvOwnerTable lOwnerTable = InvOwnerTable.create( iOwnerKey );
      lOwnerTable.setLocalBool( true );
      lOwnerTable.insert();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.create( iPartNoKey );
      lPartNoTable.setFinancialClass( new RefFinancialClassKey( 10, "CONSUMABLE" ) );
      lPartNoTable.setPartNoOem( "PART_NO_OEM" );
      lPartNoTable.setQtyUnit( RefQtyUnitKey.EA );
      lPartNoTable.setAssetAccount( ASSET_ACCOUNT );
      lPartNoTable.setTotalQt( PART_TOTAL_QT );
      lPartNoTable.setTotalValue( OLD_TOTAL_VALUE );
      lPartNoTable.setAvgUnitPrice( OLD_TOTAL_VALUE.divide( PART_TOTAL_QT ) );
      lPartNoTable.insert();

      PoHeaderTable lPoHeaderTable = PoHeaderTable.create( iEventKey );
      lPoHeaderTable.setPoType( RefPoTypeKey.PURCHASE );
      lPoHeaderTable.setExchangeQt( new BigDecimal( 1 ) );
      lPoHeaderTable.insert();

      POLineTable lPoLine = POLineTable.create( lPoHeaderTable.getPk() );
      lPoLine.setPartNo( iPartNoKey );
      lPoLine.setOrderQt( ORDER_QT );
      lPoLine.setReceivedQt( RECEIVED_QT.doubleValue() );
      lPoLine.setInvoiceQt( 0 );
      lPoLine.setAccount( ASSET_ACCOUNT );
      lPoLine.setUnitPrice( PO_LINE_UNIT_PRICE );
      lPoLine.setLinePrice( PO_LINE_UNIT_PRICE.multiply( ORDER_QT ) );
      lPoLine.setAccruedValue( PO_LINE_ACCRUED_VALUE );
      lPoLine.setQtyUnit( RefQtyUnitKey.EA );
      iPoLineKey = lPoLine.insert();

      InvInvTable lInventoryTable = InvInvTable.create( iInventoryKey );
      lInventoryTable.setFinanceStatusCd( FinanceStatusCd.NEW );
      lInventoryTable.setIssuedBool( false );
      lInventoryTable.setOwner( iOwnerKey );
      lInventoryTable.setBinQt( RECEIVED_QT.doubleValue() );
      lInventoryTable.setPartNo( iPartNoKey );
      lInventoryTable.setInvClass( RefInvClassKey.BATCH );
      lInventoryTable.setPurchaseOrderLine( iPoLineKey );
      lInventoryTable.insert();

      EvtInvTable lEvtInvTable = EvtInvTable.create( lPoHeaderTable.getPk() );
      lEvtInvTable.setInventoryKey( iInventoryKey );
      lEvtInvTable.setMainInvBool( true );
      lEvtInvTable.insert();

      EvtEventTable lEvtEventTable = EvtEventTable.create( lEvtInvTable.getPk().getEventKey() );
      lEvtEventTable.setStatus( RefEventStatusKey.ACRFI );
      lEvtEventTable.setEventType( RefEventTypeKey.AC );
      lEvtEventTable.insert();

      PoInvoice lPoInvoice = PoInvoice.create( iEventKey );
      lPoInvoice.setExchangeQt( BigDecimal.ONE );
      iPoInvoiceKey = lPoInvoice.insert();

      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.create( iPoInvoiceKey );
      lPoInvoiceLine.setPartNo( iPartNoKey );
      lPoInvoiceLine.setInvoiceQt( RECEIVED_QT );
      lPoInvoiceLine.setQtyUnit( RefQtyUnitKey.EA );
      lPoInvoiceLine.setUnitPrice( INVOICE_PRICE );
      lPoInvoiceLine.setLinePrice( INVOICE_LINE_PRICE );
      lPoInvoiceLine.setAccount( AP_ACCOUNT );
      iPoInvoiceLineKey = lPoInvoiceLine.insert();

      PoInvoiceLineMap lMap = PoInvoiceLineMap.create( iPoInvoiceLineKey, iPoLineKey );
      lMap.insert();
   }


   protected void setUpTaxCharge() {
      TaxTable lTax1 = TaxTable.create();
      lTax1.setAccountKey( EXPENSE_ACCOUNT1 );
      lTax1.setArchive( false );
      lTax1.setRecoverable( false );
      lTax1.setCompound( false );
      lTax1.setExternalId( "External Id" );
      lTax1.setTaxName( "Tax1" );
      lTax1.setTaxCode( "Tax1" );
      lTax1.setTaxRate( new BigDecimal( 0.1 ) );

      TaxKey lTax1Key = lTax1.insert();

      PoLineTax lPoLineTax = PoLineTax.create( iPoLineKey, lTax1Key );
      lPoLineTax.setTaxRate( new BigDecimal( 0.1 ) );
      lPoLineTax.insert();

      TaxTable lTax2 = TaxTable.create();
      lTax2.setAccountKey( ASSET_ACCOUNT );
      lTax2.setArchive( false );
      lTax2.setRecoverable( false );
      lTax2.setCompound( true );
      lTax2.setExternalId( "External Id" );
      lTax2.setTaxName( "Tax2" );
      lTax2.setTaxCode( "Tax2" );
      lTax2.setTaxRate( new BigDecimal( 0.2 ) );

      TaxKey lTax2Key = lTax2.insert();

      lPoLineTax = PoLineTax.create( iPoLineKey, lTax2Key );
      lPoLineTax.setTaxRate( new BigDecimal( 0.2 ) );
      lPoLineTax.setCompound( true );
      lPoLineTax.insert();

      ChargeTable lCharge1 = ChargeTable.create();
      lCharge1.setAccountKey( EXPENSE_ACCOUNT1 );
      lCharge1.setArchive( false );
      lCharge1.setRecoverable( false );
      lCharge1.setExternalId( "External Id" );
      lCharge1.setChargeName( "Charge1" );
      lCharge1.setChargeCode( "Charge1" );

      ChargeKey lCharge1Key = lCharge1.insert();

      PoLineCharge lPoLineCharge = PoLineCharge.create( iPoLineKey, lCharge1Key );
      lPoLineCharge.setChargeAmount( new BigDecimal( 35 ) );
      lPoLineCharge.insert();
   }


   /**
    * Stub class that removes internal dependencies
    */
   static class MarkInvoiceForPaymentTransactionStub extends MarkInvoiceForPaymentTransaction {

      FinancialTransaction iFinancialTransaction = null;

      List<TransactionLine> iTransactionLines = new ArrayList<TransactionLine>();


      /**
       * Creates a new {@linkplain AdjustQuantityTransactionStub} object.
       *
       * @param aInvoiceLine
       *           The inventory
       */
      public MarkInvoiceForPaymentTransactionStub(PurchaseInvoiceLineKey aInvoiceLine) {
         super( aInvoiceLine );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void addTransactionLine( FncAccountKey aAccount, BigDecimal aAmount,
            boolean aDebit ) {
         iTransactionLines.add( new TransactionLine( aAccount, aAmount, aDebit ) );

         // Add either a debit or credit
         if ( aDebit ) {
            iDebits = iDebits.add( aAmount, MathContext.DECIMAL64 );
         } else {
            iCredits = iCredits.add( aAmount, MathContext.DECIMAL64 );
         }
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void createFinancialTransaction( RefXactionTypeKey aXactionType, Date aXactionDt,
            String aXactionLdesc, InventoryKey aInventory, PartNoKey aPartNo, EventKey aEvent,
            InvCndChgEventKey aInvCndChgEvent, PurchaseOrderLineKey aPOLine,
            PurchaseInvoiceLineKey aPOInvoiceLine, BigDecimal aQuantity, BigDecimal aUnitPrice,
            RefCurrencyKey aTransactionCurrency, BigDecimal aExchangeRate ) {

         iFinancialTransaction = new FinancialTransaction( aXactionType, aInventory, aPartNo,
               aEvent, aInvCndChgEvent, aQuantity, aUnitPrice );
      }


      /**
       * The financial transaction
       */
      static class FinancialTransaction {

         final EventKey iEvent;
         final InventoryKey iInventory;
         final PartNoKey iPartNo;
         final BigDecimal iQuantity;
         final BigDecimal iUnitPrice;
         final RefXactionTypeKey iXactionType;
         final InvCndChgEventKey iInvCndChgEvent;


         /**
          * Creates a new {@linkplain FinancialTransaction} object.
          *
          * @param aXactionType
          *           The transaction type
          * @param aInventory
          *           The inventory
          * @param aPartNo
          *           The part
          * @param aEvent
          *           The event
          * @param aInvCndChgEvent
          *           The AC event
          * @param aQuantity
          *           The quantity
          * @param aUnitPrice
          *           DOCUMENT_ME
          */
         FinancialTransaction(RefXactionTypeKey aXactionType, InventoryKey aInventory,
               PartNoKey aPartNo, EventKey aEvent, InvCndChgEventKey aInvCndChgEvent,
               BigDecimal aQuantity, BigDecimal aUnitPrice) {
            iXactionType = aXactionType;
            iInventory = aInventory;
            iPartNo = aPartNo;
            iEvent = aEvent;
            iQuantity = aQuantity;
            iUnitPrice = aUnitPrice;
            iInvCndChgEvent = aInvCndChgEvent;
         }
      }

      /**
       * Mock Transaction Line
       */
      static class TransactionLine {

         final FncAccountKey iAccount;
         final BigDecimal iAmount;
         final boolean iDebit;


         /**
          * Creates a new {@linkplain TransactionLine} object.
          *
          * @param aAccount
          *           The account
          * @param aAmount
          *           The amount
          * @param aDebit
          *           TRUE if debit
          */
         TransactionLine(FncAccountKey aAccount, BigDecimal aAmount, boolean aDebit) {
            iAccount = aAccount;
            iAmount = aAmount.abs();
            iDebit = ( aAmount.compareTo( BigDecimal.ZERO ) == -1 ) ? ( aDebit ? false : true )
                  : aDebit;
         }
      }
   }
}
