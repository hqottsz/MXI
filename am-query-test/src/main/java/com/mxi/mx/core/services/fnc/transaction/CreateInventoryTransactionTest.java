package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
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
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.services.fnc.transaction.CreateInventoryTransactionTest.CreateInventoryTransactionStub.TransactionLine;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.eqp.EqpPartRotableAdjustTable;
import com.mxi.mx.core.table.fnc.FncAccount;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.ref.RefFinancialClass;


/**
 * This class tests the {@link CreateInventoryTransaction} class.
 *
 * @author Libin Cai
 * @created March 11, 2016
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CreateInventoryTransactionTest {

   /** The part finance info before transaction */
   private static final BigDecimal AUP_BEFORE = BigDecimal.ONE;
   private static final BigDecimal TOTAL_QT_BEFORE = BigDecimal.ONE;
   private static final BigDecimal TOTAL_VALUE_BEFORE = BigDecimal.ONE;
   private static final Double BIN_QT = 3.0;

   private RefFinancialClassKey iRotableFinancialClass =
         new RefFinancialClassKey( 4650, "ROTABLE" );
   private RefFinancialClassKey iConsumFinancialClass = new RefFinancialClassKey( 4650, "CONSUM" );
   private static final FncAccountKey EXPENSE_ACCOUNT = FncAccountKey.CONSIGN;
   private FncAccount iAdjqtyAccount;
   private FncAccount iInvassetAccount;

   private InventoryKey iBatchConsumInventoryKey = new InventoryKey( 4650, 1 );
   private InventoryKey iTrkRotableInventoryKey = new InventoryKey( 4650, 2 );
   private PartNoKey iBatchPartNoKey = new PartNoKey( 4650, 1 );
   private PartNoKey iTrkPartNoKey = new PartNoKey( 4650, 2 );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensures the Batch Consumable part finance info (aug, total qt and total value) is correctly
    * logged before and after the create inventory transaction. And ensure the transaction
    * information is correct.
    */
   @Test
   public void testCreateBatchComsumInvTransaction() {

      EqpPartNoTable lBatchPartNoTable = EqpPartNoTable.findByPrimaryKey( iBatchPartNoKey );
      lBatchPartNoTable.setFinancialClass( iConsumFinancialClass );
      lBatchPartNoTable.update();

      CreateInventoryTransactionStub lCreateInventoryTransaction =
            new CreateInventoryTransactionStub( iBatchConsumInventoryKey, iAdjqtyAccount.getPk() );
      lCreateInventoryTransaction.execute();

      assertThat( lCreateInventoryTransaction.iAupBefore, is( equalTo( AUP_BEFORE ) ) );
      assertThat( lCreateInventoryTransaction.iTotalQtBefore, is( equalTo( TOTAL_QT_BEFORE ) ) );
      assertThat( lCreateInventoryTransaction.iTotalValueBefore,
            is( equalTo( TOTAL_VALUE_BEFORE ) ) );

      EqpPartNoTable lPart = EqpPartNoTable
            .findByPrimaryKey( lCreateInventoryTransaction.iEqpPartNo.getPartNoKey() );
      // Verify Unit Price has not changed
      assertThat( lPart.getAvgUnitPrice(), is( equalTo( AUP_BEFORE ) ) );
      // Verify that the Total Quantity has increased by 1
      assertThat( lPart.getTotalQt(),
            is( equalTo( TOTAL_QT_BEFORE.add( new BigDecimal( BIN_QT ) ) ) ) );
      // Verify Total Value is (New Total Quantity x Unit Price)
      assertThat( lPart.getTotalValue(), is( equalTo(
            TOTAL_VALUE_BEFORE.add( new BigDecimal( BIN_QT ).multiply( AUP_BEFORE ) ) ) ) );

      // Verify Transaction Type is CRTINV
      assertThat( lCreateInventoryTransaction.iFinancialTransaction.iXactionType,
            is( equalTo( RefXactionTypeKey.CRTINV ) ) );

      assertThat( lCreateInventoryTransaction.iTransactionLines.size(), is( equalTo( 2 ) ) );
      Boolean lInvassetAccountExist = false;
      Boolean lAdjqtyAccountExist = false;
      // Verify ADJQTY account type Credit with amount = Unit Price x Quantity Created; INVASSET
      // account type Debit with amount = Unit Price x Quantity Created
      for ( TransactionLine lTransactionLine : lCreateInventoryTransaction.iTransactionLines ) {
         if ( iInvassetAccount.getPk().equals( lTransactionLine.iAccount ) ) {

            lInvassetAccountExist = true;

            if ( lTransactionLine.iDebit ) {

               assertThat( lTransactionLine.iAmount, is(
                     equalTo( lPart.getAvgUnitPrice().multiply( new BigDecimal( BIN_QT ) ) ) ) );
               FncAccount lDebitAccount = FncAccount.findByPrimaryKey( lTransactionLine.iAccount );
               // Verify INVASSET Debit account
               assertThat( lDebitAccount.getAccountType(),
                     is( equalTo( RefAccountTypeKey.INVASSET ) ) );
            } else {
               Assert.fail( "Debit is expected" );
            }
         } else if ( iAdjqtyAccount.getPk().equals( lTransactionLine.iAccount ) ) {

            lAdjqtyAccountExist = true;

            if ( !lTransactionLine.iDebit ) {

               assertThat( lTransactionLine.iAmount, is(
                     equalTo( lPart.getAvgUnitPrice().multiply( new BigDecimal( BIN_QT ) ) ) ) );
               FncAccount lCreditAccount = FncAccount.findByPrimaryKey( lTransactionLine.iAccount );
               // Verify ADJQTY Credit account
               assertThat( lCreditAccount.getAccountType(),
                     is( equalTo( RefAccountTypeKey.ADJQTY ) ) );
            } else {
               Assert.fail( "Credit is expected" );
            }
         }
      }
      assertTrue( lInvassetAccountExist );
      assertTrue( lAdjqtyAccountExist );
   }


   /**
    * Ensures the TRK Rotable part finance info (aug, total qt and total value) is correctly logged
    * before and after creating inventory. And ensure that new rotable part adjust transaction is
    * created and there is no financial transaction.
    */
   @Test
   public void testCreateTrkRotableInvTransaction() {

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iTrkPartNoKey );
      lPartNoTable.setFinancialClass( iRotableFinancialClass );
      lPartNoTable.update();

      CreateInventoryTransaction lCreateInventoryTransaction =
            new CreateInventoryTransactionStub( iTrkRotableInventoryKey, EXPENSE_ACCOUNT );
      lCreateInventoryTransaction.execute();

      EqpPartRotableAdjustTable lRotablePartAdjust = EqpPartRotableAdjustTable
            .findByPrimaryKey( lCreateInventoryTransaction.iRotablePartAdjust );

      assertThat( lRotablePartAdjust.getAvgUnitPriceBeforeAdjust(), is( equalTo( AUP_BEFORE ) ) );
      assertThat( lRotablePartAdjust.getTotalQtBeforeAdjust(), is( equalTo( TOTAL_QT_BEFORE ) ) );
      assertThat( lRotablePartAdjust.getTotalValueBeforeAdjust(),
            is( equalTo( TOTAL_VALUE_BEFORE ) ) );

      // Verify Average Price at Receipt has not changed.
      assertThat( lRotablePartAdjust.getAvgUnitPriceAfterAdjust(), is( equalTo( AUP_BEFORE ) ) );
      // Verify that the Total Spares has increased by 1
      assertThat( lRotablePartAdjust.getTotalQtAfterAdjust(),
            is( equalTo( TOTAL_QT_BEFORE.add( BigDecimal.ONE ) ) ) );
      // Verify EQP_PART_NO.TOTAL_VALUE is (New Total SPARES x Average Price at Receipt)
      assertThat( lRotablePartAdjust.getTotalValueAfterAdjust(),
            is( equalTo( TOTAL_QT_BEFORE.add( BigDecimal.ONE ).multiply( AUP_BEFORE ) ) ) );

      // Verify no new transactions were recorded
      assertNull( lCreateInventoryTransaction.iXactionLog );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      iAdjqtyAccount = FncAccount.create();
      iAdjqtyAccount.setAccountType( RefAccountTypeKey.ADJQTY );
      iAdjqtyAccount.insert();

      iInvassetAccount = FncAccount.create();
      iInvassetAccount.setAccountType( RefAccountTypeKey.INVASSET );
      iInvassetAccount.insert();

      EqpPartNoTable lBatchPartNoTable = EqpPartNoTable.create( iBatchPartNoKey );
      lBatchPartNoTable.setTotalQt( TOTAL_QT_BEFORE );
      lBatchPartNoTable.setPartNoOem( "BATCH_PART" );
      lBatchPartNoTable.setAvgUnitPrice( AUP_BEFORE );
      lBatchPartNoTable.setTotalValue( TOTAL_VALUE_BEFORE );
      lBatchPartNoTable.setInvClass( RefInvClassKey.BATCH );
      lBatchPartNoTable.setQtyUnit( RefQtyUnitKey.EA );
      lBatchPartNoTable.setAssetAccount( iInvassetAccount.getPk() );
      lBatchPartNoTable.insert();

      InvInvTable lBatchConsumInvInv = InvInvTable.create( iBatchConsumInventoryKey );
      lBatchConsumInvInv.setPartNo( iBatchPartNoKey );
      lBatchConsumInvInv.setInvClass( RefInvClassKey.BATCH );
      lBatchConsumInvInv.setBinQt( BIN_QT );
      lBatchConsumInvInv.insert();

      EqpPartNoTable lTrkPartNoTable = EqpPartNoTable.create( iTrkPartNoKey );
      lTrkPartNoTable.setTotalQt( TOTAL_QT_BEFORE );
      lTrkPartNoTable.setPartNoOem( "TRK_PART" );
      lTrkPartNoTable.setAvgUnitPrice( AUP_BEFORE );
      lTrkPartNoTable.setTotalValue( TOTAL_VALUE_BEFORE );
      lTrkPartNoTable.setInvClass( RefInvClassKey.TRK );
      lTrkPartNoTable.setAssetAccount( iInvassetAccount.getPk() );
      lTrkPartNoTable.insert();

      InvInvTable lTrkRotableInvInv = InvInvTable.create( iTrkRotableInventoryKey );
      lTrkRotableInvInv.setPartNo( iTrkPartNoKey );
      lTrkRotableInvInv.setInvClass( RefInvClassKey.TRK );
      lTrkRotableInvInv.insert();

      iConsumFinancialClass = createFinancialClass( RefFinanceTypeKey.CONSUM );
      iRotableFinancialClass = createFinancialClass( RefFinanceTypeKey.ROTABLE );

   }


   /**
    * Create financial class.
    *
    * @param aFinanceTypeKey
    *           the finance type key
    */
   private RefFinancialClassKey createFinancialClass( RefFinanceTypeKey aFinanceTypeKey ) {

      RefFinancialClass lRefFinancialClass = RefFinancialClass.create( aFinanceTypeKey.getCd() );
      lRefFinancialClass.setFinancialType( aFinanceTypeKey );

      return lRefFinancialClass.insert();
   }


   /**
    * Stub class that removes internal dependencies
    */
   static class CreateInventoryTransactionStub extends CreateInventoryTransaction {

      FinancialTransaction iFinancialTransaction = null;
      List<TransactionLine> iTransactionLines = new ArrayList<TransactionLine>();


      /**
       * Creates a new {@linkplain CreateInventoryTransactionStub} object.
       *
       * @param aInventory
       *           inventory that was just created
       * @param aCreditAccount
       *           expense account to be credited
       */
      public CreateInventoryTransactionStub(InventoryKey aInventory, FncAccountKey aCreditAccount) {
         super( aInventory, aCreditAccount );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean conditions() {
         return true;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void addTransactionLine( FncAccountKey aAccount, BigDecimal aAmount,
            boolean aDebit ) {
         iTransactionLines.add( new TransactionLine( aAccount, aAmount, aDebit ) );
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
               aEvent, aInvCndChgEvent, aQuantity );
      }


      /**
       * The financial transaction
       */
      static class FinancialTransaction {

         final EventKey iEvent;
         final InventoryKey iInventory;
         final PartNoKey iPartNo;
         final BigDecimal iQuantity;
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
          */
         FinancialTransaction(RefXactionTypeKey aXactionType, InventoryKey aInventory,
               PartNoKey aPartNo, EventKey aEvent, InvCndChgEventKey aInvCndChgEvent,
               BigDecimal aQuantity) {
            iXactionType = aXactionType;
            iInventory = aInventory;
            iPartNo = aPartNo;
            iEvent = aEvent;
            iQuantity = aQuantity;
            iInvCndChgEvent = aInvCndChgEvent;
         }
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void createRotablePartAdjust( RefXactionTypeKey aXactionType, HumanResourceKey aHr,
            Date aDate, double aAdjustQty, String aUserNote, String aSystemNote, EventKey aEvent,
            InvCndChgEventKey aInvCndChgEvent, PurchaseOrderLineKey aOrderLine ) {

         // generate new table
         EqpPartRotableAdjustTable lRotablePartAdjust =
               EqpPartRotableAdjustTable.create( iEqpPartNo.getPartNoKey() );

         iRotablePartAdjust = lRotablePartAdjust.insert();
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
