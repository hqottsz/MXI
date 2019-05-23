
package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.FncXactionAccountKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.table.fnc.FncXactionAccount;


/**
 * Builds a <code>fnc_xaction_account</code> object
 */
public class FinancialTransactionDetailBuilder implements DomainBuilder<FncXactionAccountKey> {

   private final FncXactionLogKey iFncXactionLogKey;
   private final int iFncXactionAccountId;
   private FncAccountKey iFncAccountKey;
   private BigDecimal iCreditAmount = BigDecimal.ZERO;
   private BigDecimal iDebitAmount = BigDecimal.ZERO;


   /**
    * Creates a new {@linkplain FinancialTransactionDetailBuilder} object.
    */
   public FinancialTransactionDetailBuilder(FncXactionLogKey aFncXactionLogKey,
         int aFncXactionAccountId) {
      iFncXactionLogKey = aFncXactionLogKey;
      iFncXactionAccountId = aFncXactionAccountId;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public FncXactionAccountKey build() {

      FncXactionAccount lFncXactionAccount =
            FncXactionAccount.create( iFncXactionLogKey, iFncXactionAccountId );

      lFncXactionAccount.setAccount( iFncAccountKey );
      lFncXactionAccount.setCreditCost( iCreditAmount );
      lFncXactionAccount.setDebitCost( iDebitAmount );

      return lFncXactionAccount.insert();
   }


   /*
    * Sets the account
    * 
    * @param aFncAccountKey The account
    * 
    * @return The builder
    */
   public FinancialTransactionDetailBuilder withAccount( FncAccountKey aFncAccountKey ) {
      iFncAccountKey = aFncAccountKey;

      return this;
   }


   /*
    * Sets the credit amount
    * 
    * @param aFncAccountKey The credit amount
    * 
    * @return The builder
    */
   public FinancialTransactionDetailBuilder withCredit( BigDecimal aCreditAmount ) {
      iCreditAmount = aCreditAmount;

      return this;
   }


   /*
    * Sets the debit amount
    * 
    * @param aFncAccountKey The debit amount
    * 
    * @return The builder
    */
   public FinancialTransactionDetailBuilder withDebit( BigDecimal aDebitAmount ) {
      iDebitAmount = aDebitAmount;

      return this;
   }

}
