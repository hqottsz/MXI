
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.inv.InvLocStockTable;


/**
 * This is a data builder that builds stock levels
 *
 */
public class StockLevelBuilder implements DomainBuilder<InvLocStockKey> {

   private InvLocStockKey iInvLocStockKey;
   private Double iMinReorderLevel;
   private Double iReorderQt;
   private Double iMaxLevel;
   private Double iBatchSize;
   private RefStockLowActionKey iStockLowAction;
   private boolean iIgnoreOwnerBool = false;
   private Double iWeightFactor;
   private Double iAllocationPct;


   /**
    * Creates a new {@linkplain StockLevelBuilder} object.
    *
    * @param aOrderKey
    *           The order this line belongs to.
    */
   public StockLevelBuilder(LocationKey aLocationKey, StockNoKey aStockNoKey, OwnerKey aOwnerKey) {
      iInvLocStockKey = new InvLocStockKey( aLocationKey, aStockNoKey, aOwnerKey );
   }


   /**
    * Builds the order line.
    *
    * @return The order line key
    */
   @Override
   public InvLocStockKey build() {
      InvLocStockTable lInvLocStockTable = InvLocStockTable.create( iInvLocStockKey );

      lInvLocStockTable.setMinReorderQt( iMinReorderLevel );
      lInvLocStockTable.setReorderQt( iReorderQt );
      lInvLocStockTable.setMaxQt( iMaxLevel );
      lInvLocStockTable.setBatchSize( iBatchSize );
      lInvLocStockTable.setStockLowAction( iStockLowAction );
      lInvLocStockTable.setIgnoreOwnerBool( iIgnoreOwnerBool );
      lInvLocStockTable.setWeightFactorQt( iWeightFactor );
      lInvLocStockTable.setAllocPct( iAllocationPct );

      return lInvLocStockTable.insert();
   }


   public StockLevelBuilder withBatchSize( Double aQty ) {
      iBatchSize = aQty;

      return this;
   }


   /**
    * Sets the max level
    *
    * @param aMaxQt
    *           max level
    * @return StockLevelBuilder
    */
   public StockLevelBuilder withMaxLevel( Double aMaxLevel ) {

      if ( aMaxLevel != null ) {
         iMaxLevel = aMaxLevel;
      }

      return this;
   }


   /**
    * Sets the stock low action
    *
    * @param aStockLowAction
    *           stock low action
    * @return StockLevelBuilder
    */
   public StockLevelBuilder withStockLowAction( RefStockLowActionKey aStockLowAction ) {
      iStockLowAction = aStockLowAction;

      return this;
   }


   /**
    * Sets the min reorder level
    *
    * @param aMinReorderQt
    *           min reorder level
    * @return StockLevelBuilder
    */
   public StockLevelBuilder withMinReorderLevel( Double aMinReorderLevel ) {
      iMinReorderLevel = aMinReorderLevel;

      return this;
   }


   public StockLevelBuilder withReorderQt( Double aReorderQt ) {
      iReorderQt = aReorderQt;

      return this;
   }


   public StockLevelBuilder withIgnoreOwnerBool( boolean aIgnoreOwnerBool ) {
      iIgnoreOwnerBool = aIgnoreOwnerBool;

      return this;
   }


   public StockLevelBuilder withWeightFactorQt( Double aWeightFactor ) {
      iWeightFactor = aWeightFactor;

      return this;
   }


   public StockLevelBuilder withAllocationPercentage( Double aAllocPct ) {
      iAllocationPct = aAllocPct;

      return this;
   }
}
