
package com.mxi.mx.core.services.fnc.transaction;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.eqp.EqpPartRotableAdjustTable;
import com.mxi.mx.core.table.fnc.FncXactionLog;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the {@link AbstractFinancialTransaction} class.
 *
 * @author Libin Cai
 * @created March 11, 2016
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class FinancialTransactionAbstractClassTest {

   /** The part finance info before transaction */
   private static final BigDecimal AUP_BEFORE = BigDecimal.ONE;
   private static final BigDecimal TOTAL_QT_BEFORE = BigDecimal.ONE;
   private static final BigDecimal TOTAL_VALUE_BEFORE = BigDecimal.ONE;

   private static final BigDecimal AUP_AFTER = BigDecimal.TEN;
   private static final BigDecimal TOTAL_QT_AFTER = BigDecimal.TEN;

   private PartNoKey iPartNoKey;
   private MockFinancialTransaction iMockTransaction;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensures the part finance info (aup, total qt and total value) is correctly logged before and
    * after transaction.
    */
   @Test
   public void testPartFinanceInfoBeforeAndAfterTransaction() {

      iMockTransaction = new MockFinancialTransaction( iPartNoKey, AUP_AFTER, TOTAL_QT_AFTER );
      iMockTransaction.execute();

      // assert the transaction log table
      FncXactionLog lFncXactionLog = FncXactionLog.findByPrimaryKey( iMockTransaction.iXactionLog );

      assertThat( lFncXactionLog.getAvgUnitPriceBeforeTx(), is( equalTo( AUP_BEFORE ) ) );
      assertThat( lFncXactionLog.getTotalQtBeforeTx(), is( equalTo( TOTAL_QT_BEFORE ) ) );
      assertThat( lFncXactionLog.getTotalValueBeforeTx(), is( equalTo( TOTAL_VALUE_BEFORE ) ) );

      assertThat( lFncXactionLog.getAvgUnitPriceAfterTx(), is( equalTo( AUP_AFTER ) ) );
      assertThat( lFncXactionLog.getTotalQtAfterTx(), is( equalTo( TOTAL_QT_AFTER ) ) );
      assertThat( lFncXactionLog.getTotalValueAfterTx(),
            is( equalTo( AUP_AFTER.multiply( TOTAL_QT_AFTER ) ) ) );

      // assert the rotable adjust table
      EqpPartRotableAdjustTable lRotablePartAdjust =
            EqpPartRotableAdjustTable.findByPrimaryKey( iMockTransaction.iRotablePartAdjust );
      assertThat( lRotablePartAdjust.getAvgUnitPriceBeforeAdjust(), is( equalTo( AUP_BEFORE ) ) );
      assertThat( lRotablePartAdjust.getTotalQtBeforeAdjust(), is( equalTo( TOTAL_QT_BEFORE ) ) );
      assertThat( lRotablePartAdjust.getTotalValueBeforeAdjust(),
            is( equalTo( TOTAL_VALUE_BEFORE ) ) );

      assertThat( lRotablePartAdjust.getAvgUnitPriceAfterAdjust(), is( equalTo( AUP_AFTER ) ) );
      assertThat( lRotablePartAdjust.getTotalQtAfterAdjust(), is( equalTo( TOTAL_QT_AFTER ) ) );
      assertThat( lRotablePartAdjust.getTotalValueAfterAdjust(),
            is( equalTo( AUP_AFTER.multiply( TOTAL_QT_AFTER ) ) ) );

   }


   /**
    * Ensures no null pointer exception is thrown when the transaction has no part (e.g. created
    * transaction for order misc line).
    */
   @Test
   public void testNoNullPointerErrorForTransactionWithoutPart() {

      iMockTransaction = new MockFinancialTransaction( null, null, null );

      try {
         iMockTransaction.execute();
      } catch ( NullPointerException e ) {
         MxAssert
               .fail( "Null pointer was thrown when the transaction has no part:" + e.toString() );
      }

   }


   /**
    * Ensures the total value is 0 if the total quantity and/or the AUP is 0 after the transaction.
    */
   @Test
   public void testTotalValueIsZeroWhenAUPorTotalQuantityIsZero() {

      // test when the AUP is 0
      BigDecimal lAUP = BigDecimal.ZERO;
      BigDecimal lTotalQty = BigDecimal.ONE;
      setAUPandTotalQuantityAndAssert( lAUP, lTotalQty );

      // test when the part total quantity is 0
      lAUP = BigDecimal.ONE;
      lTotalQty = BigDecimal.ZERO;
      setAUPandTotalQuantityAndAssert( lAUP, lTotalQty );

      // test when both are 0
      lAUP = BigDecimal.ZERO;
      lTotalQty = BigDecimal.ZERO;
      setAUPandTotalQuantityAndAssert( lAUP, lTotalQty );
   }


   /**
    * Set AUP and Total quantity for the transaction then assert the total value after transaction.
    *
    * @param aAUP
    *           the AUP
    * @param aTotalQty
    *           the total quantity
    */
   private void setAUPandTotalQuantityAndAssert( BigDecimal aAUP, BigDecimal aTotalQty ) {

      iMockTransaction = new MockFinancialTransaction( iPartNoKey, aAUP, aTotalQty );
      iMockTransaction.execute();

      assertThat( iMockTransaction.iEqpPartNo.getTotalValue(), is( equalTo( BigDecimal.ZERO ) ) );
   }


   /**
    * Mock transaction class that removes internal dependencies
    */
   static class MockFinancialTransaction extends AbstractFinancialTransaction {

      BigDecimal iAUP;
      BigDecimal iTotalQty;


      /**
       * Creates a new {@linkplain MockFinancialTransaction} object.
       *
       * @param aPartNoKey
       *           The part no
       * @param aAUP
       *           The average unit price
       * @param aTotalQty
       *           The total quantity
       */
      public MockFinancialTransaction(PartNoKey aPartNoKey, BigDecimal aAUP, BigDecimal aTotalQty) {

         if ( aPartNoKey != null ) {
            iEqpPartNo = EqpPartNoTable.findByPrimaryKey( aPartNoKey );
         }

         iAUP = aAUP;
         iTotalQty = aTotalQty;
      }


      /**
       * Processes the financial transaction.
       */
      @Override
      protected void processTransaction() {

         FncXactionLog lFncXactionLog = FncXactionLog.create();
         iXactionLog = lFncXactionLog.insert();

         if ( iEqpPartNo != null ) {

            EqpPartRotableAdjustTable lRotablePartAdjust =
                  EqpPartRotableAdjustTable.create( iEqpPartNo.getPartNoKey() );
            iRotablePartAdjust = lRotablePartAdjust.insert();

            iEqpPartNo.setAvgUnitPrice( iAUP );
            iEqpPartNo.setTotalQt( iTotalQty );

            // When AUP and/or total quantity is 0, we need to test the total value is also changed
            // to 0 after transaction, so we don't change the total value here to keep it as no-zero
            // value.
            if ( iAUP.compareTo( BigDecimal.ZERO ) != 0
                  && iTotalQty.compareTo( BigDecimal.ZERO ) != 0 ) {
               iEqpPartNo.setTotalValue( iAUP.multiply( iTotalQty ) );
            }

            iEqpPartNo.update();
         }
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected boolean conditions() {
         return true;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void savePartFinanceInfoBeforeAndAfterXaction() {

         // Ensure the unit test only test one case. In other words, the
         // unit test is not impacted by the logic other than the one to be tested.
         if ( iXactionLog != null || iRotablePartAdjust != null ) {
            super.savePartFinanceInfoBeforeAndAfterXaction();
         }

      }


      /**
       * {@inheritDoc}
       */
      @Override
      protected void validate() {
         // Make this method empty to ensure the unit test only test one case. In other words, the
         // unit test is not impacted by the logic other than the one to be tested.
      }
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      iPartNoKey = new PartNoBuilder().withTotalQuantity( TOTAL_QT_BEFORE )
            .withAverageUnitPrice( AUP_BEFORE ).withTotalValue( TOTAL_VALUE_BEFORE ).build();

   }
}
