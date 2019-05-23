package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.services.fnc.transaction.InspectAsUnserviceableTransactionTest.InspectAsUnserviceableTransactionStub.TransactionLine;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.InvCndChgInvTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.po.PoLine;


/**
 * This class tests Inspect as Unserviceable Transaction.
 *
 * @author ydai
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InspectAsUnserviceableTransactionTest {

   private InventoryKey iConsumLocalInventory;
   private InventoryKey iConsumNonLocalInventory;
   private InventoryKey iExpenseLocalInventory;
   private PartNoKey iConsumPart;
   private PartNoKey iExpensePart;
   private LocationKey iDockLocation;
   private OwnerKey iLocalOwner;
   private OwnerKey iNonLocalOwner;
   private RefCurrencyKey iCurrency;
   private FncAccountKey iInvassetAccount;
   private static final Integer USER_ID = 999;
   private static final String USER_NAME = "TEST_USER_NAME";
   private static final String CURRENCY_CD = "CAD";
   private static final BigDecimal OLD_PART_TOTAL_QT = new BigDecimal( 2 );
   private static final BigDecimal OLD_PART_TOTAL_VALUE = new BigDecimal( 23 );
   private static final BigDecimal OLD_ACCRUED_VALUE = new BigDecimal( 11 );
   private static final BigDecimal PO_LINE_UNIT_PRICE = new BigDecimal( 2.5 );
   private static final BigDecimal RECEIVED_QT = BigDecimal.ONE;
   private static final BigDecimal PO_LINE_ORDER_QT = new BigDecimal( 2 );
   private static final BigDecimal UNDO_UNIT_PRICE =
         new BigDecimal( 25 ).multiply( new BigDecimal( "-1" ) );
   private static final String INSP_SER_INV_UNSERVICEABLE_TRANSACTION_DESCRIPTION =
         "core.msg.INSP_SER_INV_UNSERVICEABLE_TRANSACTION";
   private static final String UNDO_INSP_SER_INV_TRANSACTION_DESCRIPTION =
         "core.msg.UNDO_INSP_SER_INV_TRANSACTION";

   private InvCndChgInvDao iInvCndChgInvDao;
   private InvCndChgEventDao iInvCndChgEventDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that the Inspect Consumable(Rotable is the same) inventory as Unserviceable transaction
    * is as expected:
    * <ul>
    * Assert:
    * <li>The Total Spares has increased by Received Quantity;
    * <li>The Average Price at Receipt is (New Total Value / New Total Spares);
    * <li>The EQP_PART_NO.TOTAL_VALUE has increased by (Received Quantity x PO Line Unit Price);
    * <li>The transaction type is INSP;
    * <li>There are two transaction lines (INVOICE CREDIT and INVASSET DEBIT) exist with the amount
    * of (Received Quantity x PO Line Unit Price).
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testTransactionWithFinancialClassOfCONSUM() throws Exception {

      // inspect the inventory as unserviceable
      InspectAsUnserviceableTransactionStub lTx =
            new InspectAsUnserviceableTransactionStub( iConsumLocalInventory, false, false, null );
      lTx.execute();

      // transaction unit price and amount
      BigDecimal lExpectedTxUnitPrice = PO_LINE_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lExpectedTxAmount = PO_LINE_UNIT_PRICE.multiply( RECEIVED_QT );

      // part total value and total quantity after transaction
      BigDecimal lExpectedNewTotalValue = OLD_PART_TOTAL_VALUE.add( lExpectedTxAmount );
      BigDecimal lExpectedNewTotalQt = OLD_PART_TOTAL_QT.add( RECEIVED_QT );

      assertTransaction( lTx, RefXactionTypeKey.INSP, lExpectedTxUnitPrice, lExpectedTxAmount,
            i18n.get( INSP_SER_INV_UNSERVICEABLE_TRANSACTION_DESCRIPTION, null, null ),
            iInvassetAccount, FncAccountKey.INVOICE, lExpectedNewTotalValue, lExpectedNewTotalQt,
            lExpectedTxAmount );
   }


   /**
    * Test that Inspect As Unserviceable not local inventory transaction is as expected:
    * <ul>
    * Assert:
    * <li>There is no transaction;
    * <li>The total quantity and total value of part have no change.
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testNoTransactionWithNonLocalInventory() throws Exception {

      // inspect the inventory as unserviceable
      InspectAsUnserviceableTransactionStub lTx = new InspectAsUnserviceableTransactionStub(
            iConsumNonLocalInventory, false, false, null );
      lTx.execute();

      assertNull( lTx.iFinancialTransaction );

      // validate data for part no
      EqpPartNo lPart = new EqpPartNo( iConsumPart );
      lPart.assertTotalQt( OLD_PART_TOTAL_QT );
      lPart.assertTotalValue( OLD_PART_TOTAL_VALUE );
   }


   /**
    * Test that Inspect As Unserviceable Expendable(Kit is the same) local inventory transaction is
    * as expected:
    * <ul>
    * Assert:
    * <li>There is no transaction;
    * <li>The total quantity of part increases one;
    * <li>The total value of part increases by po line price.
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testNoTransactionWithFinancialClassOfEXPENSE() throws Exception {

      // inspect the inventory as unserviceable
      InspectAsUnserviceableTransactionStub lTx =
            new InspectAsUnserviceableTransactionStub( iExpenseLocalInventory, false, false, null );
      lTx.execute();

      assertNull( lTx.iFinancialTransaction );

      // validate data for part no: For Expendable local inventory, the quantity of the part will
      // increase one (Seralized), and the total value of the part will increase by po line price.
      EqpPartNo lPart = new EqpPartNo( iExpensePart );
      lPart.assertTotalQt( OLD_PART_TOTAL_QT.add( BigDecimal.ONE ) );
      lPart.assertTotalValue(
            OLD_PART_TOTAL_VALUE.add( PO_LINE_UNIT_PRICE.multiply( BigDecimal.ONE ) ) );
   }


   /**
    * Test that there is a Consumable inventory which has already been inspected as unserviceable
    * and then convert the linked PO to EO.
    * <P>
    * Assert:
    * <ul>
    * <li>The Total Spares has decreased by Received Quantity;
    * <li>The Average Price at Receipt is (New Total Value / New Total Spares);
    * <li>The EQP_PART_NO.TOTAL_VALUE has increased by (Received Quantity x Convert Unit Price);
    * <li>The transaction type is UNDOINSP;
    * <li>There are two transaction lines (INVOICE CREDIT and INVASSET DEBIT) exist with the amount
    * of (Received Quantity x Convert Unit Price).
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testUndoTransactionWhenPoConvertToEo() throws Exception {

      // convert PO to EO
      InspectAsUnserviceableTransactionStub lConvertToEoTx =
            new InspectAsUnserviceableTransactionStub( iConsumLocalInventory, true, false, null );
      lConvertToEoTx.execute();

      // transaction unit price and amount
      BigDecimal lExpectedConvertToEoTxUnitPrice = UNDO_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lExpectedConvertToEoTxAmount = UNDO_UNIT_PRICE.multiply( RECEIVED_QT ).abs();

      // part total value and total quantity after UNDO transaction
      BigDecimal lExpectedConvertToEoNewTotalValue =
            OLD_PART_TOTAL_VALUE.add( UNDO_UNIT_PRICE.multiply( RECEIVED_QT ) );
      BigDecimal lExpectedConvertToEoNewTotalQt = OLD_PART_TOTAL_QT.subtract( RECEIVED_QT );

      assertTransaction( lConvertToEoTx, RefXactionTypeKey.UNDOINSP,
            lExpectedConvertToEoTxUnitPrice, lExpectedConvertToEoTxAmount,
            StringBundles.getInstance().get( UNDO_INSP_SER_INV_TRANSACTION_DESCRIPTION, null,
                  null ),
            FncAccountKey.INVOICE, iInvassetAccount, lExpectedConvertToEoNewTotalValue,
            lExpectedConvertToEoNewTotalQt, UNDO_UNIT_PRICE.multiply( RECEIVED_QT ) );
   }


   /**
    * Test that there is a Consumable inventory which has already been inspected as unserviceable
    * and then convert the linked PO to EO. Now convert this EO back to PO again.
    * <ul>
    * Assert:
    * <li>The Total Spares has increased by Received Quantity;
    * <li>The Average Price at Receipt is (New Total Value / New Total Spares);
    * <li>The EQP_PART_NO.TOTAL_VALUE has increased by (Received Quantity x PO Line Unit Price);
    * <li>The transaction type is INSP;
    * <li>There are two transaction lines (INVOICE CREDIT and INVASSET DEBIT) exist with the amount
    * of (Received Quantity x PO Line Unit Price).
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testTransactionWhenEoConvertToPo() throws Exception {

      // convert from EO to PO
      InspectAsUnserviceableTransactionStub lConvertToPoTx =
            new InspectAsUnserviceableTransactionStub( iConsumLocalInventory, false, true, null );
      lConvertToPoTx.execute();

      // transaction unit price and amount
      BigDecimal lExpectedConvertToPoTxUnitPrice = PO_LINE_UNIT_PRICE.divide( RECEIVED_QT );
      BigDecimal lExpectedConvertToPoTxAmount = PO_LINE_UNIT_PRICE.multiply( RECEIVED_QT );

      // part total value and total quantity after converting
      BigDecimal lExpectedConvertToPoNewTotalValue =
            OLD_PART_TOTAL_VALUE.add( lExpectedConvertToPoTxAmount );
      BigDecimal lExpectedConvertToPoNewTotalQt = OLD_PART_TOTAL_QT.add( RECEIVED_QT );

      assertTransaction( lConvertToPoTx, RefXactionTypeKey.INSP, lExpectedConvertToPoTxUnitPrice,
            lExpectedConvertToPoTxAmount,
            i18n.get( INSP_SER_INV_UNSERVICEABLE_TRANSACTION_DESCRIPTION, null, null ),
            iInvassetAccount, FncAccountKey.INVOICE, lExpectedConvertToPoNewTotalValue,
            lExpectedConvertToPoNewTotalQt, lExpectedConvertToPoTxAmount );
   }


   /**
    * Assert transaction information.
    *
    * @param aTx
    *           the transaction
    * @param aExpextedTxType
    *           the expected transaction type
    * @param aExpectedTxUnitPrice
    *           the expected transaction unit price
    * @param aExpectedTxAmount
    *           the expected transaction amount
    * @param aExpectedTxDescription
    *           the expected transaction description
    * @param aDebitAccount
    *           the transaction debit account
    * @param aCreditAccount
    *           the transaction credit account
    * @param aExpectedNewTotalValue
    *           the expected part total value after transaction
    * @param aExpectedNewTotalQt
    *           the expected part total quantity after transaction
    * @param aIncreasedAccuredValue
    *           the increased accured value after transaction
    */
   private void assertTransaction( InspectAsUnserviceableTransactionStub aTx,
         RefXactionTypeKey aExpextedTxType, BigDecimal aExpectedTxUnitPrice,
         BigDecimal aExpectedTxAmount, String aExpectedTxDescription, FncAccountKey aDebitAccount,
         FncAccountKey aCreditAccount, BigDecimal aExpectedNewTotalValue,
         BigDecimal aExpectedNewTotalQt, BigDecimal aIncreasedAccuredValue ) {

      assertNotNull( aTx.iFinancialTransaction );

      // check transaction information
      assertThat( aTx.iFinancialTransaction.iXactionType, is( equalTo( aExpextedTxType ) ) );
      assertThat( aTx.iFinancialTransaction.iUnitPrice, is( equalTo( aExpectedTxUnitPrice ) ) );
      assertThat( aTx.iFinancialTransaction.iQuantity, is( equalTo( RECEIVED_QT ) ) );
      assertThat( aTx.iFinancialTransaction.iXactionLdesc,
            is( equalTo( aExpectedTxDescription ) ) );

      // check debit and credit trx
      assertThat( aTx.iTransactionLines.size(), is( equalTo( 2 ) ) );

      Boolean lInvassetAccountExist = false;
      Boolean lInvoiceAccountExist = false;

      for ( TransactionLine lTransactionLine : aTx.iTransactionLines ) {
         if ( aDebitAccount.equals( lTransactionLine.iAccount ) ) {

            lInvassetAccountExist = true;

            if ( lTransactionLine.iDebit ) {
               assertThat( lTransactionLine.iAmount, is( equalTo( aExpectedTxAmount ) ) );
            } else {
               Assert.fail( "Debit is expected" );
            }
         } else if ( aCreditAccount.equals( lTransactionLine.iAccount ) ) {

            lInvoiceAccountExist = true;

            if ( !lTransactionLine.iDebit ) {
               assertThat( lTransactionLine.iAmount, is( equalTo( aExpectedTxAmount ) ) );
            } else {
               Assert.fail( "Credit is expected" );
            }
         }
      }
      assertTrue( lInvassetAccountExist );
      assertTrue( lInvoiceAccountExist );

      // check AUP
      assertThat( new EqpPartNo( iConsumPart ).getAvgUnitPrice(), is( equalTo(
            aExpectedNewTotalValue.divide( aExpectedNewTotalQt, 5, RoundingMode.HALF_UP ) ) ) );

      // check accrued amount
      PoLine lOrderLine = new PoLine( new InvInv( iConsumLocalInventory ).getPurchaseOrderLine() );
      assertThat( lOrderLine.getAccruedValue(),
            is( equalTo( OLD_ACCRUED_VALUE.add( aIncreasedAccuredValue ) ) ) );
   }


   @Before
   public void loadData() throws Exception {

      // create a user
      HumanResourceKey lHr = new HumanResourceDomainBuilder().withUserId( USER_ID )
            .withUsername( USER_NAME ).build();

      TriggerFactory.setInstance( new TriggerFactoryStub() );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      iInvCndChgInvDao = InjectorContainer.get().getInstance( InvCndChgInvDao.class );
      iInvCndChgEventDao = InjectorContainer.get().getInstance( InvCndChgEventDao.class );

      // create a currency
      iCurrency = new CurrencyBuilder( CURRENCY_CD ).isDefault().build();

      // create a local owner
      iLocalOwner = new OwnerDomainBuilder().build();

      // create a non local owner
      iNonLocalOwner = new OwnerDomainBuilder().isNonLocal().build();

      // create an Invasset account
      iInvassetAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).build();

      // create a Consumable part
      iConsumPart = new PartNoBuilder().withFinancialType( RefFinanceTypeKey.CONSUM )
            .withTotalQuantity( OLD_PART_TOTAL_QT ).withUnitType( RefQtyUnitKey.EA )
            .withTotalValue( OLD_PART_TOTAL_VALUE ).withAssetAccount( iInvassetAccount ).build();

      // create a Expendable part
      iExpensePart = new PartNoBuilder().withFinancialType( RefFinanceTypeKey.EXPENSE )
            .withTotalQuantity( OLD_PART_TOTAL_QT ).withUnitType( RefQtyUnitKey.EA )
            .withTotalValue( OLD_PART_TOTAL_VALUE ).withAssetAccount( iInvassetAccount ).build();

      // create a Consumable, local and in Inspection Required status inventory
      iConsumLocalInventory = prepareInventoryAndOrder( iConsumPart, iLocalOwner );

      // create a Consumable, non-local and in Inspection Required status inventory
      iConsumNonLocalInventory = prepareInventoryAndOrder( iConsumPart, iNonLocalOwner );

      // create a Expendable, local and in Inspection Required status inventory
      iExpenseLocalInventory = prepareInventoryAndOrder( iExpensePart, iLocalOwner );

   }


   /**
    * Prepare inventory and order data.
    *
    * @param aPart
    *           the part
    * @param aOwner
    *           the inventory owner
    * @return the inventory
    */
   private InventoryKey prepareInventoryAndOrder( PartNoKey aPart, OwnerKey aOwner ) {

      // create an order
      PurchaseOrderKey lOrder = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .usingCurrency( iCurrency ).build();

      // create order line
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitPrice( PO_LINE_UNIT_PRICE ).withOrderQuantity( PO_LINE_ORDER_QT )
            .withUnitType( RefQtyUnitKey.EA ).withAccruedValue( OLD_ACCRUED_VALUE ).build();

      // create a TRK Rotable inventory which is in Inspection Required status and hasn't been
      // inspected finance status
      InventoryKey lInventory = new InventoryBuilder().withPartNo( aPart )
            .atLocation( iDockLocation ).withOrderLine( lOrderLine ).withOwner( aOwner )
            .withCondition( RefInvCondKey.INSPREQ ).withFinanceStatusCd( FinanceStatusCd.NEW )
            .build();

      // manually change the event status to Change Inventory Status to RFI because this is the
      // pre-check of Inspect as Unserviceable transaction
      EvtInvTable lEvtInvTable = EvtInvTable.create( lOrder );
      lEvtInvTable.setInventoryKey( lInventory );
      lEvtInvTable.setMainInvBool( true );
      lEvtInvTable.insert();

      // Data setup for post conversion.
      InvCndChgInvTable lInvCndChgInvTable = iInvCndChgInvDao.create();
      lInvCndChgInvTable.setInventory( lInventory );
      lInvCndChgInvTable.setEventKey( new InvCndChgEventKey( lOrder.getDbId(), lOrder.getId() ) );
      lInvCndChgInvTable.setMainInvBool( true );
      iInvCndChgInvDao.insert( lInvCndChgInvTable );

      EvtEventTable lEvtEventTable =
            EvtEventTable.findByPrimaryKey( lEvtInvTable.getPk().getEventKey() );
      lEvtEventTable.setStatus( RefEventStatusKey.ACRFI );
      lEvtEventTable.setEventType( RefEventTypeKey.AC );
      lEvtEventTable.update();

      // Data setup for post conversion.
      InvCndChgEventTable lInvCndChgEventTable = iInvCndChgEventDao
            .findByPrimaryKey( lInvCndChgInvTable.getPk().getInvCndChgEventKey() );
      lInvCndChgEventTable.setEventStatus( RefEventStatusKey.ACRFI );
      iInvCndChgEventDao.insert( lInvCndChgEventTable );

      return lInventory;
   }


   /**
    * Stub class that removes internal dependencies
    */
   static class InspectAsUnserviceableTransactionStub extends InspectInventoryTransaction {

      FinancialTransaction iFinancialTransaction = null;

      List<TransactionLine> iTransactionLines = new ArrayList<TransactionLine>();


      /**
       * Creates a new {@linkplain InspectAsUnserviceableTransactionStub} object.
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
      public InspectAsUnserviceableTransactionStub(InventoryKey aInventory,
            boolean aUndoTransaction, boolean aConvertToPurchase, String aUserNote) {
         super( aInventory, aUndoTransaction, aConvertToPurchase, aUserNote, false );
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
         iFinancialTransaction = new FinancialTransaction( aXactionType, aXactionLdesc, aInventory,
               aPartNo, aEvent, aInvCndChgEvent, aQuantity, aUnitPrice );
      }


      /**
       * {@inheritDoc}
       */
      // @Override
      @Override
      protected BigDecimal getUnitPriceForUndo() {
         return UNDO_UNIT_PRICE;
      }


      /**
       * The financial transaction
       */
      static class FinancialTransaction {

         final EventKey iEvent;
         final String iXactionLdesc;
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
         FinancialTransaction(RefXactionTypeKey aXactionType, String aXactionLdesc,
               InventoryKey aInventory, PartNoKey aPartNo, EventKey aEvent,
               InvCndChgEventKey aInvCndChgEvent, BigDecimal aQuantity, BigDecimal aUnitPrice) {
            iXactionType = aXactionType;
            iXactionLdesc = aXactionLdesc;
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
