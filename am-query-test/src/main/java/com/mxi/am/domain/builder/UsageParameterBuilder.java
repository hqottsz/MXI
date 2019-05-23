
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.mim.MimPartNumData;


/**
 * Builds an <code>inv_curr_usage</code> object
 */
public class UsageParameterBuilder implements DomainBuilder<UsageParmKey> {

   private DataTypeKey iDataType;
   private InventoryKey iInventory;
   private double iTsiQuantity;
   private double iTsnQuantity;
   private double iTsoQuantity;


   /**
    * Builds a current usage record in inv_curr_usage.
    *
    * @return usage parameter key (pk of inv_curr_usage)
    */
   @Override
   public UsageParmKey build() {

      // Assign the data type to the inventory's config slot.
      ConfigSlotKey lConfigSlot = InvInvTable.findByPrimaryKey( iInventory ).getBOMItem();

      // Create the MimPartNumDataKey if it does not already exist.
      MimPartNumDataKey lMimPartNumDataKey = new MimPartNumDataKey( lConfigSlot, iDataType );
      if ( !lMimPartNumDataKey.isValid() ) {
         MimPartNumData.create( lMimPartNumDataKey );
      }

      InvCurrUsage lInvCurrUsage =
            InvCurrUsage.create( iInventory, iDataType, iTsnQuantity, iTsoQuantity );
      lInvCurrUsage.setTsiQt( iTsiQuantity );

      return ( UsageParmKey ) lInvCurrUsage.getPk();
   }


   /**
    * Set the data type.
    *
    * @param aDataType
    *           type key
    *
    * @return This builder.
    */
   public UsageParameterBuilder forDataType( DataTypeKey aDataType ) {
      iDataType = aDataType;

      return this;
   }


   /**
    * Set the inventory.
    *
    * @param aInventory
    *           key
    *
    * @return This builder.
    */
   public UsageParameterBuilder forInventory( InventoryKey aInventory ) {
      iInventory = aInventory;

      return this;
   }


   /**
    * Set the TSI quantity.
    *
    * @param aQuantity
    *
    * @return This builder.
    */
   public UsageParameterBuilder withTsi( double aQuantity ) {
      iTsiQuantity = aQuantity;

      return this;
   }


   /**
    * Set the TSN quantity.
    *
    * @param aQuantity
    *
    * @return This builder.
    */
   public UsageParameterBuilder withTsn( double aQuantity ) {
      iTsnQuantity = aQuantity;

      return this;
   }


   /**
    * Set the TSO quantity.
    *
    * @param aQuantity
    *
    * @return This builder.
    */
   public UsageParameterBuilder withTso( double aQuantity ) {
      iTsoQuantity = aQuantity;

      return this;
   }
}
