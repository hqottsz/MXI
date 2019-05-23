
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;


/**
 * Builds a <code>eqp_stock_no</code> object
 */
public class StockBuilder implements DomainBuilder<StockNoKey> {

   private String iStockCode;
   private String iStockName;
   private RefInvClassKey iInvClass;
   private boolean iAutoCreatePo = false;
   private boolean iAutoIssuePo = false;
   private double iGlobalReorderQuantity;
   private double iBatchSize;
   private double iSafetyLevelQty;
   private RefQtyUnitKey iQtyUnitKey;
   private double iMaxMultQt;


   /**
    * {@inheritDoc}
    */
   @Override
   public StockNoKey build() {
      EqpStockNoTable lEqpStockNoTable = EqpStockNoTable.create();

      lEqpStockNoTable.setInvClass( iInvClass );

      if ( iStockCode != null ) {
         lEqpStockNoTable.setStockNoCd( iStockCode );
      }

      if ( iStockName != null ) {
         lEqpStockNoTable.setStockNoName( iStockName );
      }

      lEqpStockNoTable.setAutoCreatePOBool( iAutoCreatePo );
      lEqpStockNoTable.setAutoIssuePOBool( iAutoIssuePo );
      lEqpStockNoTable.setQtyUnit( iQtyUnitKey );
      lEqpStockNoTable.setGlobalReorderQt( iGlobalReorderQuantity );
      lEqpStockNoTable.setBatchSize( iBatchSize );
      lEqpStockNoTable.setSafetyLevelQt( iSafetyLevelQty );
      lEqpStockNoTable.setMaxMultQt( iMaxMultQt );

      return lEqpStockNoTable.insert();
   }


   /**
    * Sets the auto create po flag to true or false
    *
    * @param aValue
    *           the value to set the flag
    *
    * @return The builder
    */
   public StockBuilder isAutoCreatePo( boolean aValue ) {
      iAutoCreatePo = aValue;

      return this;
   }


   /**
    * Sets the auto issue po flag to true or false
    *
    * @param aValue
    *           the value to set the flag
    *
    * @return The builder
    */
   public StockBuilder isAutoIssuePo( boolean aValue ) {
      iAutoIssuePo = aValue;

      return this;
   }


   /**
    * Sets the stock code
    *
    * @param aCode
    *           String representation of stock code
    * @return StockBuilder
    */
   public StockBuilder withStockCode( String aCode ) {
      iStockCode = aCode;

      return this;
   }


   /**
    * Sets the stock name
    *
    * @param aName
    *           String representation of stock name
    * @return StockBuilder
    */
   public StockBuilder withStockName( String aName ) {
      iStockName = aName;

      return this;
   }


   /**
    * Sets the stock inventory class
    *
    * @param aInventoryClass
    *           stock inventory class
    * @return StockBuilder
    */
   public StockBuilder withInvClass( RefInvClassKey aInventoryClass ) {
      iInvClass = aInventoryClass;

      return this;
   }


   /**
    * Sets the quantity unit
    *
    * @param aQuantityUnit
    *
    * @return StockBuilder
    */
   public StockBuilder withQtyUnitKey( RefQtyUnitKey aQuantityUnit ) {
      iQtyUnitKey = aQuantityUnit;

      return this;
   }


   /**
    *
    * Sets the global reorder quantity
    *
    * @param aGlobalReorderQty
    * @return StockBuilder
    */
   public StockBuilder withGlobalReorderQuantity( double aGlobalReorderQty ) {
      iGlobalReorderQuantity = aGlobalReorderQty;

      return this;
   }


   /**
    *
    * Sets the batch size (reorder quantity of the batch)
    *
    * @param aBatchSize
    * @return StockBuilder
    */
   public StockBuilder withBatchSize( double aBatchSize ) {
      iBatchSize = aBatchSize;

      return this;
   }


   /**
    *
    * Sets the safety level quantity
    *
    * @param aSafetyLevelQt
    * @return StockBuilder
    */
   public StockBuilder withSafetyLevelQt( double aSafetyLevelQt ) {
      iSafetyLevelQty = aSafetyLevelQt;

      return this;
   }


   /**
    *
    * Sets the max mult quantity. This appears on UI as the weighting factor on the Stock Levels
    * tab.
    *
    * @param aMaxMultQt
    *           the max mult qt (weighting factor)
    * @return the stock builder
    */
   public StockBuilder withMaxMultQt( double aMaxMultQt ) {
      iMaxMultQt = aMaxMultQt;
      return this;
   }
}
