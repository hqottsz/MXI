
package com.mxi.mx.core.services.fnc.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

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
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncXactionAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.fnc.FncXactionAccount;
import com.mxi.mx.core.table.fnc.FncXactionLog;


/**
 * This class tests the {@link ChangeFinanceTypeTransaction} class.
 *
 * @author ydai
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ChangeFinanceTypeTransactionTest {

   private final BigDecimal UNIT_PRICE = BigDecimal.valueOf( 7.17 );
   private final BigDecimal TOTAL_QTY = BigDecimal.valueOf( 3.23 );
   private final BigDecimal TOTAL_VALUE = BigDecimal.TEN;
   private final BigDecimal NEW_UNIT_PRICE = BigDecimal.valueOf( 8.0 );

   private final int EXPECTED_XACTION_LINE_NUMBER = 2;

   private HumanResourceKey iHr;
   private OwnerKey iLocalOwner;
   private OwnerKey iNonLocalOwner;
   private PartNoKey iConsumPart;
   private PartNoKey iRotablePart;
   private PartNoKey iExpensePart;
   private FncAccountKey iAccount;

   private QueryAccessObject iQao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensures that no financial transaction with type CHGFINTP is created, if the part has
    * non-locally owned inventory or locally non-active inventory.
    */
   @Test
   public void testNoXactionWhenLocalActiveInvNotExists() {

      // Non locally owned inventory
      new InventoryBuilder().withPartNo( iRotablePart ).withOwner( iNonLocalOwner ).build();

      // Locally owned inventory but not active
      CreateRotableLocalInv( RefInvCondKey.ARCHIVE );
      CreateRotableLocalInv( RefInvCondKey.SCRAP );
      CreateRotableLocalInv( RefInvCondKey.INSRV );

      executeAndAssertNoXaction( iRotablePart, RefFinanceTypeKey.CONSUM );

   }


   /**
    * Ensures that no financial transaction with type CHGFINTP is created, if the part finance type
    * was changed between EXPENSE and ROTABLE (from EXPENSE to ROTABLE).
    */
   @Test
   public void testNoXactionWhenChangedFromEXPENSEToROTABLE() {

      // Assume the part is already changed from EXPENSE to ROTABLE
      CreateRotableLocalInv( RefInvCondKey.INREP );

      executeAndAssertNoXaction( iRotablePart, RefFinanceTypeKey.EXPENSE );

   }


   /**
    * Ensures that no financial transaction with type CHGFINTP is created, if the part finance type
    * was changed between EXPENSE and ROTABLE (from ROTABLE to EXPENSE).
    */
   @Test
   public void testNoXactionWhenChangedFromROTABLEToEXPENSE() {

      // Assume the part is already changed from ROTABLE to EXPENSE
      CreateLocalInv( iExpensePart, RefInvCondKey.INREP );

      executeAndAssertNoXaction( iExpensePart, RefFinanceTypeKey.ROTABLE );

   }


   /**
    * Ensures that financial transaction with type CHGFINTP is created, if the part finance type was
    * changed from CONSUM. (there is locally owned and not locked or archived inventory) Make sure
    * the old asset account is credited with expected value and the CHGFINTP account is debited with
    * expected value.
    */
   @Test
   public void testXactionCreatedWhenChangedFromCONSUM() {

      // Assume the part is already changed from CONSUM to ROTABLE
      CreateRotableLocalInv( RefInvCondKey.INREP );

      // Assert: when inventory is serialized, total quantity will be one and the amount of
      // transaction line will be unit price multiply by quantity which is one;
      // Assert: credit to old account when change from CONSUM type.
      executeAndAssertXaction( iRotablePart, RefFinanceTypeKey.CONSUM,
            NEW_UNIT_PRICE.multiply( BigDecimal.ONE ), iAccount, FncAccountKey.CHGFINTP );

   }


   /**
    * Ensures that financial transaction with type CHGFINTP is created, if the part finance type was
    * changed to CONSUM. Make sure the new asset account is debited with expected value and the
    * CHGFINTP account is credited with expected value.
    */
   @Test
   public void testXactionCreatedWhenChangedToCONSUM() {

      // Assume the part is already changed from ROTABLE to CONSUM
      CreateLocalInv( iConsumPart, RefInvCondKey.INREP );

      // Assert: when inventory is serialized, total quantity will be one and the amount of
      // transaction line will be unit price multiply by quantity which is one;
      // Assert: debit to new account when change to CONSUM type.
      executeAndAssertXaction( iConsumPart, RefFinanceTypeKey.ROTABLE,
            NEW_UNIT_PRICE.multiply( BigDecimal.ONE ), FncAccountKey.CHGFINTP, iAccount );

   }


   /**
    * Ensures that financial transaction with type CHGFINTP is created, if the part has more than
    * one local active inventories. (The part finance type was changed from or to CONSUM) Make sure
    * the value of transaction is the total of both inventories.
    */
   @Test
   public void testTotalValueWhenChangedFromCONSUM() {

      // Assume the part is already changed from CONSUM to ROTABLE
      CreateRotableLocalInv( RefInvCondKey.INREP );
      CreateRotableLocalInv( RefInvCondKey.RFI );

      // Assert: when inventory is serialized and there are more than one local active inventories,
      // total quantity will be the total of both inventories' quantity;
      // Assert: credit to old account when change from CONSUM type.
      executeAndAssertXaction( iRotablePart, RefFinanceTypeKey.CONSUM, NEW_UNIT_PRICE
            .multiply( BigDecimal.ONE ).add( NEW_UNIT_PRICE.multiply( BigDecimal.ONE ) ), iAccount,
            FncAccountKey.CHGFINTP );

   }


   /**
    * Ensures that financial transaction with type CHGFINTP is created, if the part has more than
    * one local active inventories. (The part finance type was changed from or to CONSUM) Make sure
    * the value of transaction is the total of both inventories.
    */
   @Test
   public void testTotalValueWhenChangeToCONSUM() {

      // Assume the part is already changed from ROTABLE to CONSUM
      CreateLocalInv( iConsumPart, RefInvCondKey.INREP );
      CreateLocalInv( iConsumPart, RefInvCondKey.RFI );

      // Assert: when inventory is serialized and there are more than one local active inventories,
      // total quantity will be the total of both inventories' quantity;
      // Assert: credit to old account when change from CONSUM type.
      executeAndAssertXaction( iConsumPart, RefFinanceTypeKey.ROTABLE, NEW_UNIT_PRICE
            .multiply( BigDecimal.ONE ).add( NEW_UNIT_PRICE.multiply( BigDecimal.ONE ) ),
            FncAccountKey.CHGFINTP, iAccount );
   }


   /**
    * Ensures that no financial transaction is created if changed from ROTABLE to EXPENSE.But if the
    * part has more than one local active inventories. Make sure the value of the part's total
    * quantity and total value are the total of both inventories.
    */
   @Test
   public void testTotalValueWhenChangeFromROTABLEToEXPENSE() {

      // Assume the part is already changed from ROTABLE to EXPENSE
      CreateLocalInv( iExpensePart, RefInvCondKey.INREP );
      CreateLocalInv( iExpensePart, RefInvCondKey.RFI );

      // Assert: There is no financial transaction created.
      // Assert: when inventory is serialized and there are more than one local active inventories,
      // total quantity will be the total of both inventories' quantity and the total value will be
      // the total of both inventories' value.
      executeAndAssertNoXaction( iExpensePart, RefFinanceTypeKey.ROTABLE );

      EqpPartNoTable lExpensePart = EqpPartNoTable.findByPrimaryKey( iExpensePart );
      assertEquals( BigDecimal.ONE.add( BigDecimal.ONE ), lExpensePart.getTotalQt() );
      assertEquals(
            UNIT_PRICE.multiply( BigDecimal.ONE ).add( UNIT_PRICE.multiply( BigDecimal.ONE ) ),
            lExpensePart.getTotalValue() );

   }


   /**
    * Create locally owned inventory.
    *
    * @param aPartNo
    *           the part no
    * @param aInvCond
    *           the inventory condition code
    */
   private void CreateLocalInv( PartNoKey aPartNo, RefInvCondKey aInvCond ) {
      new InventoryBuilder().withPartNo( aPartNo ).withOwner( iLocalOwner )
            .withCondition( aInvCond ).build();
   }


   /**
    * Create locally owned Rotable inventory.
    *
    * @param aInvCond
    *           the inventory condition code
    */
   private void CreateRotableLocalInv( RefInvCondKey aInvCond ) {
      CreateLocalInv( iRotablePart, aInvCond );
   }


   /**
    * Execute ChangeFinanceTypeTransaction and assert no new transaction created.
    *
    * @param aPartNo
    *           the part no
    * @param aOldFinanceType
    *           the old finance type
    */
   private void executeAndAssertNoXaction( PartNoKey aPartNo, RefFinanceTypeKey aOldFinanceType ) {
      ChangeFinanceTypeTransaction lXaction =
            new ChangeFinanceTypeTransaction( aPartNo, aOldFinanceType, null, null );

      lXaction.execute();

      // Assert no new transaction created
      assertEquals( null, lXaction.iXactionLog );
   }


   /**
    * Execute ChangeFinanceTypeTransaction and assert new transaction.
    *
    * @param lXaction
    *           ChangeFinanceTypeTransaction
    * @param aAmount
    *           amount of the transaction line
    * @param aCreditAccount
    *           credit account for the transaction line
    * @param aDebitAccount
    *           debit account for the transaction line
    *
    */
   private void executeAndAssertXaction( PartNoKey aPartNo, RefFinanceTypeKey aOldFinanceType,
         BigDecimal aAmount, FncAccountKey aCreditAccount, FncAccountKey aDebitAccount ) {
      ChangeFinanceTypeTransaction lXaction =
            new ChangeFinanceTypeTransaction( aPartNo, aOldFinanceType, iAccount, NEW_UNIT_PRICE );

      lXaction.execute();

      // Assert new transaction created
      assertNotNull( lXaction.iXactionLog );

      // Assert that there are two transaction lines created
      DataSetArgument lArgs = lXaction.iXactionLog.getPKWhereArg();
      QuerySet lQs = iQao.executeQueryTable( "fnc_xaction_account", lArgs );
      assertEquals( EXPECTED_XACTION_LINE_NUMBER, lQs.getRowCount() );

      // Assert the transaction amount
      assertEquals( aAmount, lXaction.iCredits );
      assertEquals( aAmount, lXaction.iDebits );
      assertEquals( aAmount, lXaction.iXactionTotal );

      // Assert the credit and debit accounts
      for ( int i = 1; i <= EXPECTED_XACTION_LINE_NUMBER; i++ ) {
         FncXactionAccount lFncXactionAccount = FncXactionAccount
               .findByPrimaryKey( new FncXactionAccountKey( lXaction.iXactionLog, i ) );
         if ( lFncXactionAccount.isCredit() ) {
            assertEquals( aCreditAccount, lFncXactionAccount.getAccount() );
         } else if ( lFncXactionAccount.isDebit() ) {
            assertEquals( aDebitAccount, lFncXactionAccount.getAccount() );
         }
      }

      // Assert transaction log type is CHGFINTP
      FncXactionLog lFncXactionLog = FncXactionLog.findByPrimaryKey( lXaction.iXactionLog );
      assertEquals( RefXactionTypeKey.CHGFINTP, lFncXactionLog.getXactionType() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() {

      GlobalParametersStub lGlobalParams = new GlobalParametersStub( "LOGIC" );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_ACCOUNT_CD", false );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_PART_NO_OEM", false );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_VENDOR_CD", false );
      GlobalParameters.setInstance( lGlobalParams );

      iQao = QuerySetFactory.getInstance();

      // Create a HR
      iHr = new HumanResourceDomainBuilder().build();

      TriggerFactory.setInstance( new TriggerFactoryStub() );
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // Create an account
      iAccount = new AccountBuilder().withCode( "TEST_ACCOUNT" ).build();

      // Create a CONSUM part
      iConsumPart = new PartNoBuilder().withOemPartNo( "TEST_CONSUM_PART" )
            .withTotalQuantity( TOTAL_QTY ).withAverageUnitPrice( UNIT_PRICE )
            .withTotalValue( TOTAL_VALUE ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withAssetAccount( iAccount ).build();

      // Create a ROTABLE part
      iRotablePart = new PartNoBuilder().withOemPartNo( "TEST_ROTABLE_PART" )
            .withTotalQuantity( TOTAL_QTY ).withAverageUnitPrice( UNIT_PRICE )
            .withTotalValue( TOTAL_VALUE ).withFinancialType( RefFinanceTypeKey.ROTABLE )
            .withAssetAccount( iAccount ).build();

      // Create an EXPENSE part
      iExpensePart = new PartNoBuilder().withOemPartNo( "TEST_EXPENSE_PART" )
            .withTotalQuantity( TOTAL_QTY ).withAverageUnitPrice( UNIT_PRICE )
            .withTotalValue( TOTAL_VALUE ).withFinancialType( RefFinanceTypeKey.EXPENSE )
            .withAssetAccount( iAccount ).build();

      // Create a local owner
      iLocalOwner = new OwnerDomainBuilder().build();

      // Create a not local owner
      iNonLocalOwner = new OwnerDomainBuilder().isNonLocal().build();

      // Create a default currency
      new CurrencyBuilder( "TESTCRCY" ).isDefault().build();
   }

}
