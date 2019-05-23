
package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.table.ref.RefCurrency;


/**
 * Builds a <code>ref_currency</code> object
 */
public class CurrencyBuilder implements DomainBuilder<RefCurrencyKey> {

   private final String iCode;

   private boolean iDefault = false;
   private BigDecimal iExchangeQt;


   /**
    * Creates a new {@linkplain CurrencyBuilder} object.
    *
    * @param aCode
    *           The currency code
    */
   public CurrencyBuilder(String aCode) {
      iCode = aCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public RefCurrencyKey build() {
      int lDbId = ( int ) PrimaryKeyService.getInstance().getDatabaseId();
      RefCurrencyKey lPrimaryKey = new RefCurrencyKey( lDbId, iCode );

      DataSetArgument lArgs = lPrimaryKey.getPKWhereArg();
      lArgs.add( RefCurrency.ColumnName.DEFAULT_BOOL.name(), iDefault );

      if ( iExchangeQt != null ) {
         lArgs.add( RefCurrency.ColumnName.EXCHG_QT.name(), iExchangeQt );
      }

      MxDataAccess.getInstance().executeInsert( "ref_currency", lArgs );

      return lPrimaryKey;
   }


   /**
    * Sets the exchange rate
    *
    * @param aExchangeQt
    * @return
    */
   public CurrencyBuilder withExchangeQt( BigDecimal aExchangeQt ) {
      iExchangeQt = aExchangeQt;

      return this;
   }


   public CurrencyBuilder isDefault() {
      iDefault = true;

      return this;
   }
}
