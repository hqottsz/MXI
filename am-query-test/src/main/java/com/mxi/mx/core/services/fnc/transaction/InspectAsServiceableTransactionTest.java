package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.ChargeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
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
import com.mxi.mx.core.services.fnc.transaction.InspectAsServiceableTransactionTest.InspectAsServiceableTransactionStub.TransactionLine;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoHeaderTable;
import com.mxi.mx.core.table.po.PoLineCharge;
import com.mxi.mx.core.table.po.PoLineTax;
import com.mxi.mx.core.table.procurement.ChargeTable;
import com.mxi.mx.core.table.procurement.TaxTable;


/**
 * This class tests Inspect as Serviceable Transaction.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InspectAsServiceableTransactionTest {

   private static final FncAccountKey EXPENSE_ACCOUNT = FncAccountKey.CONSIGN;
   private static final FncAccountKey ASSET_ACCOUNT = FncAccountKey.INVOICE;
   private static final HumanResourceKey CURRENT_HUMAN_RESOURCE = new HumanResourceKey( 4650, 1 );
   private static final RefFinancialClassKey FINANCIAL_CLASS_CONSUMABLE =
         new RefFinancialClassKey( 10, "CONSUMABLE" );
   private static final RefFinancialClassKey FINANCIAL_CLASS_ROTABLE =
         new RefFinancialClassKey( 10, "ROTABLE" );
   private static final RefInvClassKey INV_CLASS_TRK = RefInvClassKey.TRK;
   private static final RefInvClassKey INV_CLASS_BATCH = RefInvClassKey.BATCH;
   private static final String TRK_PART_NO_OEM = "TRK_PART_NO_OEM";
   private static final String PART_NO_OEM = "PART_NO_OEM";
   private static final BigDecimal TAX_1 = new BigDecimal( 0.1 );
   private static final BigDecimal TAX_2 = new BigDecimal( 0.2 );
   private static final BigDecimal CHARGE = new BigDecimal( 35 );
   private static final Double EXPECTED_BIN_QT = new Double( 3.0 );
   private static final BigDecimal OLD_ACCRUED_VALUE = new BigDecimal( 11 );
   private static final BigDecimal PO_LINE_UNIT_PRICE = new BigDecimal( 8 );
   private static final BigDecimal RECEIVED_QT = BigDecimal.ONE;
   private static final BigDecimal EXPECTED_ORDER_QT = new BigDecimal( 10 );
   private static final BigDecimal EXPECTED_SERIALIZED_INV_RECEIVED_QT = BigDecimal.ONE;
   private static final BigDecimal EXPECTED_PART_UNIT_PRICE = new BigDecimal( 7 );
   private static final BigDecimal OLD_PART_TOTAL_VALUE = new BigDecimal( 9 );
   private static final BigDecimal OLD_PART_TOTAL_QT = new BigDecimal( 2 );
   private static final BigDecimal UNDO_UNIT_PRICE =
         new BigDecimal( 25 ).multiply( new BigDecimal( "-1" ) );

   private EventKey iEventKey = new EventKey( 4650, 1 );
   private EventKey iTRKEventKey = new EventKey( 4650, 2 );
   private InventoryKey iInventoryKey = new InventoryKey( 4650, 1 );
   private InventoryKey iTrkRotableInventoryKey = new InventoryKey( 4650, 2 );
   private OwnerKey iOwnerKey = new OwnerKey( 4650, 1 );
   private PartNoKey iPartNoKey = new PartNoKey( 4650, 1 );
   private PartNoKey iTRKPartNoKey = new PartNoKey( 4650, 2 );
   private PurchaseOrderLineKey iPoLine;

   private InvCndChgInvDao iInvCndChgInvDao;
   private InvCndChgEventDao iInvCndChgEventDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Creates a new {@linkplain InspectAsServiceableTransactionTest} object.
    */
   public InspectAsServiceableTransactionTest() {
      super();
   }


   /**
    * Ensures that value of Batch Consumable inventory is properly debited/credited when changing
    * part number
    */
   @Test
   public void testValueOfInventoryDebitedCredited() {

      InspectAsServiceableTransactionStub lTx =
            new InspectAsServiceableTransactionStub( iInventoryKey, false, false, null );
      lTx.execute();

      // transaction unit price and amount
      BigDecimal lTxUnitPrice = PO_LINE_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lTxAmount = lTxUnitPrice.multiply( BigDecimal.valueOf( EXPECTED_BIN_QT ) );

      // check log
      assertThat( lTx.iFinancialTransaction.iUnitPrice, is( equalTo( lTxUnitPrice ) ) );
      assertThat( lTx.iFinancialTransaction.iQuantity,
            is( equalTo( BigDecimal.valueOf( EXPECTED_BIN_QT ) ) ) );

      // check debit and credit trx
      for ( TransactionLine lTransactionLine : lTx.iTransactionLines ) {
         assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmount ) ) );
      }

      // part total value and total quantity after transaction
      BigDecimal lTotalValue = lTxAmount.add( OLD_PART_TOTAL_VALUE );
      BigDecimal lTotalQty = OLD_PART_TOTAL_QT.add( BigDecimal.valueOf( EXPECTED_BIN_QT ) );

      // check AUP
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      assertThat( lPartNoTable.getAvgUnitPrice(),
            is( equalTo( lTotalValue.divide( lTotalQty ) ) ) );

      // check accrued amount
      POLineTable lPoLine = POLineTable.findByPrimaryKey( iPoLine );
      assertThat( lPoLine.getAccruedValue(), is( equalTo( lTxAmount.add( OLD_ACCRUED_VALUE ) ) ) );
   }


   /**
    * Ensures that value of TRK Rotable inventory is properly debited/credited when changing part
    * number
    */
   @Test
   public void testValueOfTrkRotableInventoryDebitedCredited() {

      // set up data for receiving TRK Rotable inventory
      setUpData( iTRKPartNoKey, FINANCIAL_CLASS_ROTABLE, TRK_PART_NO_OEM, EXPECTED_PART_UNIT_PRICE,
            OLD_PART_TOTAL_QT, OLD_PART_TOTAL_VALUE, iTRKEventKey, RECEIVED_QT, EXPECTED_ORDER_QT,
            OLD_ACCRUED_VALUE, PO_LINE_UNIT_PRICE, new Double( 0.0 ), iTrkRotableInventoryKey,
            INV_CLASS_TRK );

      InspectAsServiceableTransactionStub lTx =
            new InspectAsServiceableTransactionStub( iTrkRotableInventoryKey, false, false, null );
      lTx.execute();

      // transaction unit price and amount
      BigDecimal lTxUnitPrice = PO_LINE_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lTxAmount = PO_LINE_UNIT_PRICE.multiply( RECEIVED_QT );

      // check log
      assertThat( lTx.iFinancialTransaction.iUnitPrice, is( equalTo( lTxUnitPrice ) ) );
      assertThat( lTx.iFinancialTransaction.iQuantity,
            is( equalTo( EXPECTED_SERIALIZED_INV_RECEIVED_QT ) ) );

      // check debit and credit trx
      for ( TransactionLine lTransactionLine : lTx.iTransactionLines ) {
         assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmount ) ) );
      }

      // part total value and total quantity after transaction
      BigDecimal lTotalValue = lTxAmount.add( OLD_PART_TOTAL_VALUE );
      BigDecimal lTotalQty = OLD_PART_TOTAL_QT.add( EXPECTED_SERIALIZED_INV_RECEIVED_QT );

      // check AUP
      EqpPartNoTable lTRKPartNoTable = EqpPartNoTable.findByPrimaryKey( iTRKPartNoKey );
      assertThat( lTRKPartNoTable.getAvgUnitPrice(),
            is( equalTo( lTotalValue.divide( lTotalQty, 5, RoundingMode.HALF_UP ) ) ) );

      // check accrued amount
      POLineTable lTRKPoLine = POLineTable.findByPrimaryKey( iPoLine );
      assertThat( lTRKPoLine.getAccruedValue(),
            is( equalTo( lTxAmount.add( OLD_ACCRUED_VALUE ) ) ) );
   }


   /**
    * Ensures that value of inventory is properly debited/credited when undo the transaction
    */
   @Test
   public void testValueOfInventoryDebitedCreditedforUndo() {

      testValueOfInventoryDebitedCredited();

      // unit price and amount and accured value of last transaction
      BigDecimal lTxUnitPriceBefore = PO_LINE_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lTxAmountBefore =
            lTxUnitPriceBefore.multiply( BigDecimal.valueOf( EXPECTED_BIN_QT ) );
      BigDecimal lAccuredValueBefore = lTxAmountBefore.add( OLD_ACCRUED_VALUE );

      InspectAsServiceableTransactionStub lTx =
            new InspectAsServiceableTransactionStub( iInventoryKey, true, false, null );
      lTx.execute();

      // check log
      assertThat( lTx.iFinancialTransaction.iUnitPrice, is( equalTo( UNDO_UNIT_PRICE ) ) );
      assertThat( lTx.iFinancialTransaction.iQuantity,
            is( equalTo( BigDecimal.valueOf( EXPECTED_BIN_QT ) ) ) );

      // transaction amount for undo
      BigDecimal lTxAmount =
            UNDO_UNIT_PRICE.multiply( BigDecimal.valueOf( EXPECTED_BIN_QT ) ).abs();

      // check debit and credit trx
      for ( TransactionLine lTransactionLine : lTx.iTransactionLines ) {
         assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmount ) ) );
      }

      // part total value after transaction
      BigDecimal lTotalValue =
            ( PO_LINE_UNIT_PRICE.multiply( BigDecimal.valueOf( EXPECTED_BIN_QT ) )
                  .add( OLD_PART_TOTAL_VALUE ) ).subtract( lTxAmount );

      // check AUP
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      assertThat( lPartNoTable.getAvgUnitPrice(),
            is( equalTo( lTotalValue.divide( OLD_PART_TOTAL_QT ) ) ) );

      // check accrued amount
      POLineTable lPoLine = POLineTable.findByPrimaryKey( iPoLine );
      assertThat( lPoLine.getAccruedValue(), is( equalTo( lAccuredValueBefore
            .add( UNDO_UNIT_PRICE.multiply( BigDecimal.valueOf( EXPECTED_BIN_QT ) ) ) ) ) );
   }


   /**
    * Ensures that value of inventory is properly debited/credited when changing part number
    */
   @Test
   public void testValueOfTaxChargeTransactions() {

      // set up data for receiving TRK Rotable inventory
      setUpData( iTRKPartNoKey, FINANCIAL_CLASS_ROTABLE, TRK_PART_NO_OEM, EXPECTED_PART_UNIT_PRICE,
            OLD_PART_TOTAL_QT, OLD_PART_TOTAL_VALUE, iTRKEventKey, RECEIVED_QT, EXPECTED_ORDER_QT,
            OLD_ACCRUED_VALUE, PO_LINE_UNIT_PRICE, new Double( 0.0 ), iTrkRotableInventoryKey,
            INV_CLASS_TRK );

      setUpTaxCharge();

      InspectAsServiceableTransactionStub lTx =
            new InspectAsServiceableTransactionStub( iTrkRotableInventoryKey, false, false, null );
      lTx.execute();

      // check log
      assertThat( lTx.iFinancialTransaction.iUnitPrice,
            is( equalTo( PO_LINE_UNIT_PRICE.divide( RECEIVED_QT ) ) ) );
      assertThat( lTx.iFinancialTransaction.iQuantity,
            is( equalTo( EXPECTED_SERIALIZED_INV_RECEIVED_QT ) ) );

      // transaction amount
      BigDecimal lTxAmountWithoutTaxCharge =
            PO_LINE_UNIT_PRICE.multiply( EXPECTED_SERIALIZED_INV_RECEIVED_QT );
      BigDecimal lTxAmountWithTax1 = lTxAmountWithoutTaxCharge.multiply( TAX_1 );
      BigDecimal lTxAmountWithTwoTaxes =
            lTxAmountWithoutTaxCharge.add( lTxAmountWithTax1 ).multiply( TAX_2 );
      // received quantity < ordered quantity
      BigDecimal lTxAmountWithCharge = CHARGE.multiply( RECEIVED_QT ).divide( EXPECTED_ORDER_QT );

      // check debit and credit trx
      for ( int i = 0; i < lTx.iTransactionLines.size(); i++ ) {
         TransactionLine lTransactionLine = lTx.iTransactionLines.get( i );
         switch ( i ) {

            case 0:
               assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmountWithoutTaxCharge ) ) );

               break;

            case 1:
               assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmountWithTax1 ) ) );

               break;

            case 2:
               assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmountWithTwoTaxes ) ) );

               break;

            case 3:
               assertThat( lTransactionLine.iAmount, is( equalTo( lTxAmountWithCharge ) ) );

               break;

            case 4:
               assertThat( lTransactionLine.iAmount,
                     is( equalTo( lTxAmountWithoutTaxCharge.add( lTxAmountWithTax1 )
                           .add( lTxAmountWithTwoTaxes ).add( lTxAmountWithCharge ) ) ) );

               break;

            default:
               fail( "Transaction is invalid." );

               break;
         }
      }
      // check AUP
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      assertThat( lPartNoTable.getAvgUnitPrice(), is( equalTo( EXPECTED_PART_UNIT_PRICE ) ) );

      // check accrued amount
      POLineTable lPoLine = POLineTable.findByPrimaryKey( iPoLine );
      assertThat( lPoLine.getAccruedValue(), is( equalTo(
            OLD_ACCRUED_VALUE.add( lTxAmountWithoutTaxCharge ).add( lTxAmountWithTwoTaxes ) ) ) );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      TriggerFactory.setInstance( new TriggerFactoryStub() );
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( CURRENT_HUMAN_RESOURCE ) );

      InvOwnerTable lOwnerTable = InvOwnerTable.create( iOwnerKey );
      lOwnerTable.setLocalBool( true );
      lOwnerTable.insert();

      iInvCndChgInvDao = InjectorContainer.get().getInstance( InvCndChgInvDao.class );
      iInvCndChgEventDao = InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // set up data for receiving Batch Consumable inventory
      setUpData( iPartNoKey, FINANCIAL_CLASS_CONSUMABLE, PART_NO_OEM, EXPECTED_PART_UNIT_PRICE,
            OLD_PART_TOTAL_QT, OLD_PART_TOTAL_VALUE, iEventKey, RECEIVED_QT, EXPECTED_ORDER_QT,
            OLD_ACCRUED_VALUE, PO_LINE_UNIT_PRICE, EXPECTED_BIN_QT, iInventoryKey,
            INV_CLASS_BATCH );
   }


   /**
    * Set up part no, order, order line, inventory data.
    *
    * @param aPartNoKey
    *           the part no key
    * @param aFinancialClass
    *           the financial class key
    * @param aPartNoOem
    *           the part no oem
    * @param aPartUnitPrice
    *           the part unit price
    * @param aPartTotalQt
    *           the part total quantity
    * @param aPartTotalValue
    *           the part total value
    * @param aEventKey
    *           the event key
    * @param aExchangeQt
    *           the exchange quantity
    * @param aOrderQt
    *           the order quantity
    * @param aAccruedValue
    *           the accrued value
    * @param aLineUnitPrice
    *           the order line unit price
    * @param aBinQt
    *           the bin quantity
    * @param aInventoryKey
    *           the inventory key
    * @param aRefInvClassKey
    *           the RefInvClassKey
    */
   private void setUpData( PartNoKey aPartNoKey, RefFinancialClassKey aFinancialClass,
         String aPartNoOem, BigDecimal aPartUnitPrice, BigDecimal aPartTotalQt,
         BigDecimal aPartTotalValue, EventKey aEventKey, BigDecimal aExchangeQt,
         BigDecimal aOrderQt, BigDecimal aAccruedValue, BigDecimal aLineUnitPrice, Double aBinQt,
         InventoryKey aInventoryKey, RefInvClassKey aRefInvClassKey ) {

      EqpPartNoTable lPartNoTable = EqpPartNoTable.create( aPartNoKey );
      lPartNoTable.setFinancialClass( aFinancialClass );
      lPartNoTable.setPartNoOem( aPartNoOem );
      lPartNoTable.setQtyUnit( RefQtyUnitKey.EA );
      lPartNoTable.setAssetAccount( ASSET_ACCOUNT );
      lPartNoTable.setAvgUnitPrice( aPartUnitPrice );
      lPartNoTable.setTotalQt( aPartTotalQt );
      lPartNoTable.setTotalValue( aPartTotalValue );
      lPartNoTable.insert();

      PoHeaderTable lPoHeaderTable = PoHeaderTable.create( aEventKey );
      lPoHeaderTable.setPoType( RefPoTypeKey.PURCHASE );
      lPoHeaderTable.setExchangeQt( aExchangeQt );
      lPoHeaderTable.insert();

      POLineTable lPoLine = POLineTable.create( lPoHeaderTable.getPk() );
      lPoLine.setPartNo( aPartNoKey );
      lPoLine.setOrderQt( aOrderQt );
      lPoLine.setAccount( ASSET_ACCOUNT );
      lPoLine.setAccruedValue( aAccruedValue );
      lPoLine.setUnitPrice( aLineUnitPrice );
      lPoLine.setQtyUnit( RefQtyUnitKey.EA );
      iPoLine = lPoLine.insert();

      InvInvTable lInventoryTable = InvInvTable.create( aInventoryKey );
      lInventoryTable.setFinanceStatusCd( FinanceStatusCd.NEW );
      lInventoryTable.setIssuedBool( false );
      lInventoryTable.setOwner( iOwnerKey );
      lInventoryTable.setPartNo( aPartNoKey );
      lInventoryTable.setInvClass( aRefInvClassKey );
      lInventoryTable.setBinQt( aBinQt );
      lInventoryTable.setPurchaseOrderLine( iPoLine );
      lInventoryTable.insert();

      EvtInvTable lEvtInvTable = EvtInvTable.create( lPoHeaderTable.getPk() );
      lEvtInvTable.setInventoryKey( aInventoryKey );
      lEvtInvTable.setMainInvBool( true );
      lEvtInvTable.insert();

      // Data setup for post conversion.
      InvCndChgInvTable lInvCndChgInvTable = iInvCndChgInvDao.create();
      lInvCndChgInvTable.setInventory( aInventoryKey );
      lInvCndChgInvTable.setEventKey( new InvCndChgEventKey( lPoHeaderTable.getPk().getDbId(),
            lPoHeaderTable.getPk().getId() ) );
      lInvCndChgInvTable.setMainInvBool( true );
      iInvCndChgInvDao.insert( lInvCndChgInvTable );

      EvtEventTable lEvtEventTable = EvtEventTable.create( lEvtInvTable.getPk().getEventKey() );
      lEvtEventTable.setStatus( RefEventStatusKey.ACRFI );
      lEvtEventTable.setEventType( RefEventTypeKey.AC );
      lEvtEventTable.insert();

      // Data setup for post conversion.
      InvCndChgEventTable lInvCndChgEventTable =
            iInvCndChgEventDao.create( lInvCndChgInvTable.getPk().getInvCndChgEventKey() );
      lInvCndChgEventTable.setEventStatus( RefEventStatusKey.ACRFI );
      iInvCndChgEventDao.insert( lInvCndChgEventTable );

   }


   /**
    * Set up tax and charge data.
    *
    */
   protected void setUpTaxCharge() {
      TaxTable lTax1 = TaxTable.create();
      lTax1.setAccountKey( EXPENSE_ACCOUNT );
      lTax1.setArchive( false );
      lTax1.setRecoverable( false );
      lTax1.setCompound( false );
      lTax1.setExternalId( "External Id" );
      lTax1.setTaxName( "Tax1" );
      lTax1.setTaxCode( "Tax1" );
      lTax1.setTaxRate( TAX_1 );

      TaxKey lTax1Key = lTax1.insert();

      PoLineTax lPoLineTax = PoLineTax.create( iPoLine, lTax1Key );
      lPoLineTax.setTaxRate( TAX_1 );
      lPoLineTax.insert();

      TaxTable lTax2 = TaxTable.create();
      lTax2.setAccountKey( ASSET_ACCOUNT );
      lTax2.setArchive( false );
      lTax2.setRecoverable( false );
      lTax2.setCompound( true );
      lTax2.setExternalId( "External Id" );
      lTax2.setTaxName( "Tax2" );
      lTax2.setTaxCode( "Tax2" );
      lTax2.setTaxRate( TAX_2 );

      TaxKey lTax2Key = lTax2.insert();

      lPoLineTax = PoLineTax.create( iPoLine, lTax2Key );
      lPoLineTax.setTaxRate( TAX_2 );
      lPoLineTax.setCompound( true );
      lPoLineTax.insert();

      ChargeTable lCharge1 = ChargeTable.create();
      lCharge1.setAccountKey( EXPENSE_ACCOUNT );
      lCharge1.setArchive( false );
      lCharge1.setRecoverable( false );
      lCharge1.setExternalId( "External Id" );
      lCharge1.setChargeName( "Charge1" );
      lCharge1.setChargeCode( "Charge1" );

      ChargeKey lCharge1Key = lCharge1.insert();

      PoLineCharge lPoLineCharge = PoLineCharge.create( iPoLine, lCharge1Key );
      lPoLineCharge.setChargeAmount( CHARGE );
      lPoLineCharge.insert();
   }


   /**
    * Stub class that removes internal dependencies
    */
   static class InspectAsServiceableTransactionStub extends InspectInventoryTransaction {

      FinancialTransaction iFinancialTransaction = null;

      List<TransactionLine> iTransactionLines = new ArrayList<TransactionLine>();


      /**
       * Creates a new {@linkplain InspectAsServiceableTransactionStub} object.
       *
       * @param aInventory
       *           The inventory
       * @param aUndoTransaction
       *           The option to undo the transaction
       * @param aConvertToPurchase
       *           The option to convert to purchase
       * @param aUserNote
       *           The user note
       */
      public InspectAsServiceableTransactionStub(InventoryKey aInventory, boolean aUndoTransaction,
            boolean aConvertToPurchase, String aUserNote) {
         super( aInventory, aUndoTransaction, aConvertToPurchase, aUserNote, true );
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
       * {@inheritDoc}
       */
      @Override
      protected BigDecimal getUnitPriceForUndo() {
         return UNDO_UNIT_PRICE;
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
            iAmount = aAmount;
            iDebit = aDebit;
         }
      }
   }
}
