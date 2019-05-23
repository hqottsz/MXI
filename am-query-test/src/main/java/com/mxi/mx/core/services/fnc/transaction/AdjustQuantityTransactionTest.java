package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static com.mxi.mx.testing.matchers.MxMatchers.within;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
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
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.RotablePartAdjustKey;
import com.mxi.mx.core.services.fnc.transaction.AdjustQuantityTransactionTest.AdjustQuantityTransactionStub.TransactionLine;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.eqp.EqpPartRotableAdjustTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the {@link AdjustQuantityTransaction} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AdjustQuantityTransactionTest {

   private static final RefFinancialClassKey BASE_FINANCIAL_CLASS =
         new RefFinancialClassKey( 4650, "BASE" );
   private static final RefFinancialClassKey ROTABLE_FINANCIAL_CLASS =
         new RefFinancialClassKey( 4650, "ROTABLE" );
   private static final RefFinancialClassKey EXPENSE_FINANCIAL_CLASS =
         new RefFinancialClassKey( 4650, "EXPENSE" );
   private static final FncAccountKey EXPENSE_ACCOUNT = FncAccountKey.CONSIGN;
   private static final FncAccountKey ASSET_ACCOUNT = FncAccountKey.INVOICE;
   private static final FinanceStatusCd BASE_FINANCE_STATUS = FinanceStatusCd.INSP;
   private static final FinanceStatusCd NEW_FINANCE_STATUS = FinanceStatusCd.NEW;

   private static final HumanResourceKey CURRENT_HUMAN_RESOURCE = new HumanResourceKey( 4650, 1 );

   private EventKey iEventKey = new EventKey( 4650, 1 );

   private InventoryKey iInventoryKey = new InventoryKey( 4650, 1 );
   private OwnerKey iOwnerKey = new OwnerKey( 4650, 1 );
   private PartNoKey iPartNoKey = new PartNoKey( 4650, 1 );
   private String iReasonCd = null;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Creates a new {@linkplain AdjustQuantityTransactionTest} object.
    */
   public AdjustQuantityTransactionTest() {
      super();
   }


   /**
    * Ensures that we do not adjust the inventory total count for issued inventory.
    */
   @Test
   public void testAdjustForIssuedRotableInventory() {
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( iInventoryKey );
      lInventoryTable.setIssuedBool( true );
      lInventoryTable.update();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lPartNoTable.setFinancialClass( ROTABLE_FINANCIAL_CLASS );
      lPartNoTable.update();

      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      lPartNoTable.refresh();
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ONE ) ) );
   }


   /**
    * Ensures that the credit account is creditted when the inventory quantity adjustment is
    * positive, AUP of the new part is calculated based on the old part AUP.
    */
   @Test
   public void testAUPAndTotalQuantity() {

      // SET UP data:
      EqpPartNoTable lNewPartNoTable = updateNewPartNumber();

      PartNoKey lOldPartNoKey = createOldPartNumber();

      // Run AdjustQuantityTransaction:
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, new BigDecimal( 5.0 ),
                  EXPENSE_ACCOUNT, lOldPartNoKey, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      // Expected result: AUP = 73.3333 , and TQTY = 15
      lNewPartNoTable.refresh();

      // AUP = (NewPart TV + OldPart AUP * OldPart_Transfer_Qty) / NewPart_TQty after qty
      // adjustmented: (1000 + 20*5 )/15 = 73.3333
      assertThat( lNewPartNoTable.getTotalQt(), is( equalTo( BigDecimal.valueOf( 15.0 ) ) ) );

      // need rounding of the value to compare:
      BigDecimal lExpectedValue = BigDecimal.valueOf( 73.33333 );

      assertThat( lNewPartNoTable.getAvgUnitPrice(), is( equalTo( lExpectedValue ) ) );
      assertThat( lNewPartNoTable.getTotalValue(), is( equalTo( BigDecimal.valueOf( 1100.0 ) ) ) );
   }


   /**
    * Ensures that the debit account is creditted when the inventory quantity adjustment is
    * negative.
    */
   @Test
   public void testCreditAssetAccountOnNegativeAdjustment() {
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE.negate(),
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      boolean lDebitAccountIsCreditted = false;
      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         if ( !ASSET_ACCOUNT.equals( lTransactionLine.iAccount ) ) {
            continue;
         }

         if ( !lTransactionLine.iDebit ) {
            lDebitAccountIsCreditted = true;
         }
      }

      assertTrue( lDebitAccountIsCreditted );
   }


   /**
    * Ensures that the credit account is creditted when the inventory quantity adjustment is
    * positive.
    */
   @Test
   public void testCreditExpenseAccountOnPositiveAdjustment() {
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      boolean lCreditAccountIsCreditted = false;
      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         if ( !EXPENSE_ACCOUNT.equals( lTransactionLine.iAccount ) ) {
            continue;
         }

         if ( !lTransactionLine.iDebit ) {
            lCreditAccountIsCreditted = true;
         }
      }

      assertTrue( lCreditAccountIsCreditted );
   }


   /**
    * Ensures that the debit account is debitted when the inventory quantity adjustment is positive.
    */
   @Test
   public void testDebitAssetAccountOnPositiveAdjustment() {
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      boolean lDebitAccountIsDebitted = false;
      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         if ( !ASSET_ACCOUNT.equals( lTransactionLine.iAccount ) ) {
            continue;
         }

         if ( lTransactionLine.iDebit ) {
            lDebitAccountIsDebitted = true;
         }
      }

      assertTrue( lDebitAccountIsDebitted );
   }


   /**
    * Ensures that the credit account is debitted when the inventory quantity adjustment is
    * negative.
    */
   @Test
   public void testDebitExpenseAccountOnNegativeAdjustment() {
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE.negate(),
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      boolean lCreditAccountIsDebitted = false;
      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         if ( !EXPENSE_ACCOUNT.equals( lTransactionLine.iAccount ) ) {
            continue;
         }

         if ( lTransactionLine.iDebit ) {
            lCreditAccountIsDebitted = true;
         }
      }

      assertTrue( lCreditAccountIsDebitted );
   }


   /**
    * Ensure that we can decrease the inventory total count for non-serialized inventory.
    */
   @Test
   public void testDecreaseQuantityOnNonSerializedInventory() {
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( iInventoryKey );
      lInventoryTable.setInvClass( RefInvClassKey.BATCH );
      lInventoryTable.update();

      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.TEN, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.TEN ) ) );
   }


   /**
    * Ensure that we can decrease the inventory total count.
    */
   @Test
   public void testDecreaseQuantityOnSerializedInventory() {
      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE.negate(),
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ONE.negate() ) ) );
   }


   /**
    * Ensure that the average price unit of the new part does not get updated if the total quantity
    * is 0 or less
    */
   @Test
   public void testDontAdjustAvgUnitPriceForPartNoWithZeroTotalQty() {
      final BigDecimal NEW_PART_AVG_UNIT_PRICE = BigDecimal.valueOf( 90.0 );
      final BigDecimal NEW_PART_TOTAL_QTY = BigDecimal.valueOf( -1.0 );

      // DATA SETUP: Set the total quantity for the new part numbers to -1
      EqpPartNoTable lNewPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lNewPartNoTable.setTotalQt( NEW_PART_TOTAL_QTY );
      lNewPartNoTable.setAvgUnitPrice( NEW_PART_AVG_UNIT_PRICE );
      lNewPartNoTable.setTotalValue( NEW_PART_TOTAL_QTY.multiply( NEW_PART_AVG_UNIT_PRICE ) );
      lNewPartNoTable.update();

      // DATA SETUP: Create an old part
      PartNoKey lOldPartNoKey = createOldPartNumber();

      // Run AdjustQuantityTransaction:
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  lOldPartNoKey, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      // Asserts the new part
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      assertThat( lPartNoTable.getAvgUnitPrice(), is( equalTo( NEW_PART_AVG_UNIT_PRICE ) ) );
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ZERO ) ) );
      assertThat( lPartNoTable.getTotalValue(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * Ensures that we do not adjust the inventory total count for issued inventory.
    */
   @Test
   public void testDontAdjustForIssuedNonRotableInventory() {
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( iInventoryKey );
      lInventoryTable.setIssuedBool( true );
      lInventoryTable.update();

      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * Ensure that we do not adjust the inventory total count for inventory in new financial class.
    */
   @Test
   public void testDontAdjustForNewInventory() {
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( iInventoryKey );
      lInventoryTable.setFinanceStatusCd( NEW_FINANCE_STATUS );
      lInventoryTable.update();

      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * Ensure that we do not adjust the inventory total count if the inventory owner is not local.
    */
   @Test
   public void testDontAdjustForRemoteOwner() {
      InvOwnerTable lOwnerTable = InvOwnerTable.findByPrimaryKey( iOwnerKey );
      lOwnerTable.setLocalBool( false );
      lOwnerTable.update();

      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * Ensure we do not create transaction records for the expense debit class.
    */
   @Test
   public void testFinancialTransactionNotRecordedForExpenseAccount() {
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lPartNoTable.setFinancialClass( EXPENSE_FINANCIAL_CLASS );
      lPartNoTable.update();

      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( iEventKey, iInventoryKey, BigDecimal.ONE,
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      assertThat( lQuantityTransaction.iFinancialTransaction, is( nullValue() ) );

      assertThat( lQuantityTransaction.iTransactionLines.size(), is( equalTo( 0 ) ) );
   }


   /**
    * Ensure we create financial transactions
    */
   @Test
   public void testFinancialTransactionRecorded() {
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( iEventKey, iInventoryKey, BigDecimal.ONE,
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      assertThat( lQuantityTransaction.iFinancialTransaction.iXactionType,
            is( equalTo( RefXactionTypeKey.QTYADJ ) ) );
      assertThat( lQuantityTransaction.iFinancialTransaction.iEvent, is( equalTo( iEventKey ) ) );
      assertThat( lQuantityTransaction.iFinancialTransaction.iInventory,
            is( equalTo( iInventoryKey ) ) );
      assertThat( lQuantityTransaction.iFinancialTransaction.iPartNo, is( equalTo( iPartNoKey ) ) );
      assertThat( lQuantityTransaction.iFinancialTransaction.iQuantity,
            is( equalTo( BigDecimal.ONE ) ) );
   }


   /**
    * Ensure that we can increase the inventory total count on non-serialized inventory
    */
   @SuppressWarnings( "deprecation" )
   @Test
   public void testIncreaseQuantityOnNonSerializedInventory() {

      BigDecimal lPreviousTotalQt = new BigDecimal( 5 );
      BigDecimal lPreviousAUP = BigDecimal.ONE;
      BigDecimal lPreviousTotalValue = lPreviousAUP.multiply( lPreviousTotalQt );
      BigDecimal lNewBinQt = BigDecimal.TEN;

      // Assume previous bin qty is 1.
      BigDecimal lAdjustmentQt = lNewBinQt.subtract( BigDecimal.ONE );

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lPartNoTable.setTotalQt( lPreviousTotalQt );
      lPartNoTable.setAvgUnitPrice( lPreviousAUP );
      lPartNoTable.setTotalValue( lPreviousTotalValue );
      lPartNoTable.update();

      // Assume the quantity is already changed to the following values
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( iInventoryKey );
      lInventoryTable.setInvClass( RefInvClassKey.BATCH );
      lInventoryTable.setBinQt( lNewBinQt.doubleValue() );
      lInventoryTable.update();

      // Change quantity from 2 to 10.
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, lAdjustmentQt, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      lPartNoTable.refresh();;

      BigDecimal lAdjustmentValue = lAdjustmentQt.multiply( lPreviousAUP );
      BigDecimal lNewTotalQt = lPreviousTotalQt.add( lAdjustmentQt );

      // assert part properties
      assertThat( lPartNoTable.getTotalQt(), is( equalTo( lNewTotalQt ) ) );
      assertThat( lPartNoTable.getAvgUnitPrice(), is( equalTo( lPreviousAUP ) ) );
      assertThat( lPartNoTable.getTotalValue(),
            is( equalTo( lNewTotalQt.multiply( lPreviousAUP ) ) ) );

      // assert transaction properties
      assertThat( lQuantityTransaction.iFinancialTransaction.iXactionType,
            is( equalTo( RefXactionTypeKey.QTYADJ ) ) );

      assertThat( lQuantityTransaction.iTransactionLines.size(), is( equalTo( 2 ) ) );

      for ( int i = 0; i < lQuantityTransaction.iTransactionLines.size(); i++ ) {
         TransactionLine lTransactionLine = lQuantityTransaction.iTransactionLines.get( i );
         switch ( i ) {

            case 0:
               // charge to account Credit
               assertThat( lTransactionLine.iAccount, is( EXPENSE_ACCOUNT ) );
               assertThat( lTransactionLine.iAmount, is( equalTo( lAdjustmentValue ) ) );
               assertThat( lTransactionLine.iDebit, is( false ) );
               break;

            case 1:
               // INVASSET debit (because the amount is not positive, so it is actually credit)
               assertThat( lTransactionLine.iAccount, is( lPartNoTable.getAssetAccount() ) );
               assertThat( lTransactionLine.iDebit, is( true ) );
               assertThat( lTransactionLine.iAmount, is( equalTo( lAdjustmentValue ) ) );

               break;

            default:
               MxAssert.fail( "Transactio line is invalid." );

               break;
         }
      }
   }


   /**
    * Ensure that we can increase the inventory total count.
    */
   @Test
   public void testIncreaseQuantityOnSerializedInventory() {
      AdjustQuantityTransaction lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );

      assertThat( lPartNoTable.getTotalQt(), is( equalTo( BigDecimal.ONE ) ) );
   }


   /**
    * Ensures that the credit account is creditted when the inventory quantity adjustment is
    * positive.
    */
   @Test
   public void testRotableAdjustmentLogged() {
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey );
      lPartNoTable.setFinancialClass( ROTABLE_FINANCIAL_CLASS );
      lPartNoTable.update();

      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, BigDecimal.ONE, EXPENSE_ACCOUNT,
                  null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      // Ensure rotable part adjust log is kept up to date
      EqpPartRotableAdjustTable lPartRotableAdjustTable =
            EqpPartRotableAdjustTable.findByPrimaryKey( new RotablePartAdjustKey( iPartNoKey, 1 ) );
      assertTrue( lPartRotableAdjustTable.exists() );

      // Ensure rotable adjustment log is properly logged
      assertThat( lPartRotableAdjustTable.getInventory(), is( equalTo( iInventoryKey ) ) );
      assertThat( lPartRotableAdjustTable.getXactionType(),
            is( equalTo( RefXactionTypeKey.QTYADJ ) ) );
      assertThat( lPartRotableAdjustTable.getHr(), is( equalTo( CURRENT_HUMAN_RESOURCE ) ) );
      assertThat( lPartRotableAdjustTable.getAdjustDate(), is( within( 5, Calendar.SECOND ) ) );
      assertThat( lPartRotableAdjustTable.getAdjustQty(),
            is( equalTo( BigDecimal.ONE.doubleValue() ) ) );
      assertThat( lPartRotableAdjustTable.getSystemNote(), containsString(
            i18n.get( "core.msg.ADJUST_PART_QUANTITY_TRANSACTION_FOR_SER", "PART_NO_OEM" ) ) );
   }


   /**
    * Ensures that value of inventory is properly debited/credited when part number is unchanged
    */
   @Test
   public void testValueOfInventoryDebitedCredited() {

      // SET UP data:
      updateNewPartNumber();

      // No part changed:
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, new BigDecimal( 5.0 ),
                  EXPENSE_ACCOUNT, null, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      // Expected result: amount is 100 * 5 = 500

      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         assertThat( lTransactionLine.iAmount, is( equalTo( BigDecimal.valueOf( 500 ) ) ) );
      }
   }


   /**
    * Ensures that value of inventory is properly debited/credited when changing part number
    */
   @Test
   public void testValueOfInventoryDebitedCredited_PartChange() {

      // SET UP data:
      updateNewPartNumber();

      PartNoKey lOldPartNoKey = createOldPartNumber();

      // Part changed
      AdjustQuantityTransactionStub lQuantityTransaction =
            new AdjustQuantityTransactionStub( null, iInventoryKey, new BigDecimal( 5.0 ),
                  EXPENSE_ACCOUNT, lOldPartNoKey, iReasonCd, CURRENT_HUMAN_RESOURCE );
      lQuantityTransaction.execute();

      // Expected result: amount is 20 * 5 = 100

      for ( TransactionLine lTransactionLine : lQuantityTransaction.iTransactionLines ) {
         assertThat( lTransactionLine.iAmount, is( equalTo( BigDecimal.valueOf( 100 ) ) ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      InvOwnerTable lOwnerTable = InvOwnerTable.create( iOwnerKey );
      lOwnerTable.setLocalBool( true );
      lOwnerTable.insert();

      EqpPartNoTable lPartNoTable = EqpPartNoTable.create( iPartNoKey );
      lPartNoTable.setFinancialClass( BASE_FINANCIAL_CLASS );
      lPartNoTable.setTotalQt( BigDecimal.ZERO );
      lPartNoTable.setPartNoOem( "PART_NO_OEM" );
      lPartNoTable.setQtyUnit( RefQtyUnitKey.EA );
      lPartNoTable.setAssetAccount( ASSET_ACCOUNT );
      lPartNoTable.setAvgUnitPrice( BigDecimal.ONE );
      lPartNoTable.insert();

      InvInvTable lInventoryTable = InvInvTable.create( iInventoryKey );
      lInventoryTable.setFinanceStatusCd( BASE_FINANCE_STATUS );
      lInventoryTable.setIssuedBool( false );
      lInventoryTable.setOwner( iOwnerKey );
      lInventoryTable.setBinQt( null );
      lInventoryTable.setPartNo( iPartNoKey );
      lInventoryTable.setInvClass( RefInvClassKey.SER );
      lInventoryTable.insert();
   }


   /**
    * Creates the old part number
    *
    * @return the old part number key
    */
   private PartNoKey createOldPartNumber() {
      PartNoKey lOldPartNoKey = new PartNoKey( 4650, 2 );
      EqpPartNoTable lOldPartNoTable = EqpPartNoTable.create( lOldPartNoKey );
      lOldPartNoTable.setTotalQt( BigDecimal.valueOf( 5.0 ) );
      lOldPartNoTable.setAvgUnitPrice( BigDecimal.valueOf( 20.0 ) );
      lOldPartNoTable.setTotalValue( BigDecimal.valueOf( 100.0 ) );
      lOldPartNoTable.insert();

      return lOldPartNoKey;
   }


   /**
    * Updates the new part number
    *
    * @return the new part number table
    */
   private EqpPartNoTable updateNewPartNumber() {
      EqpPartNoTable lNewPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartNoKey ); // new part
      lNewPartNoTable.setTotalQt( BigDecimal.valueOf( 10.0 ) );
      lNewPartNoTable.setAvgUnitPrice( BigDecimal.valueOf( 100.0 ) );
      lNewPartNoTable.setTotalValue( BigDecimal.valueOf( 1000.0 ) );
      lNewPartNoTable.update();

      return lNewPartNoTable;
   }


   /**
    * Stub class that removes internal dependencies
    */
   static class AdjustQuantityTransactionStub extends AdjustQuantityTransaction {

      FinancialTransaction iFinancialTransaction = null;

      List<TransactionLine> iTransactionLines = new ArrayList<TransactionLine>();


      /**
       * Creates a new {@linkplain AdjustQuantityTransactionStub} object.
       *
       * @param aAdjustQtyEvent
       *           The event
       * @param aInventory
       *           The inventory
       * @param aAdjustmentQuantity
       *           The previous total quantity
       * @param aChargeToAccount
       *           The credit account
       * @param aOldPartNoKey
       *           The old part number ( optional )
       * @param aReasonCd
       *           The reason code
       * @param aAuthorizingHr
       *           the authorizing human resource
       */
      public AdjustQuantityTransactionStub(EventKey aAdjustQtyEvent, InventoryKey aInventory,
            BigDecimal aAdjustmentQuantity, FncAccountKey aChargeToAccount, PartNoKey aOldPartNoKey,
            String aReasonCd, HumanResourceKey aAuthorizingHr) {
         super( aAdjustQtyEvent, aInventory, aAdjustmentQuantity, aChargeToAccount, aOldPartNoKey,
               aReasonCd, aAuthorizingHr );
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
