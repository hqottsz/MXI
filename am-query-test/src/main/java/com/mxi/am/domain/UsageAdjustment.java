package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageType;


public class UsageAdjustment {

   private Date iUsageDate;
   private boolean iIsNegated = false;
   private boolean iHasBeenApplied = false;
   private InventoryKey iMainInventory;
   private Map<InventoryKey, Map<DataTypeKey, Usage>> iUsages =
         new HashMap<InventoryKey, Map<DataTypeKey, Usage>>();
   private UsageType iUsageType;


   public void setUsageDate( Date aUsageDate ) {
      iUsageDate = aUsageDate;
   }


   public void setIsNegated( boolean aIsNegated ) {
      iIsNegated = aIsNegated;
   }


   public void setUsageType( UsageType aUsageType ) {
      iUsageType = aUsageType;
   }


   public void setHasBeenApplied( boolean aHasBeenApplied ) {
      iHasBeenApplied = aHasBeenApplied;
   }


   public void setMainInventory( InventoryKey aInventory ) {
      iMainInventory = aInventory;
   }


   public void addUsage( InventoryKey aInventory, DataTypeKey aType, BigDecimal aValue,
         BigDecimal aDelta ) {

      if ( !iUsages.containsKey( aInventory ) ) {
         iUsages.put( aInventory, new HashMap<DataTypeKey, Usage>() );
      }

      iUsages.get( aInventory ).put( aType, new Usage( aType, aValue, aDelta ) );
   }


   public Date getUsageDate() {
      return iUsageDate;
   }


   public UsageType getUsageType() {
      return iUsageType;
   }


   public boolean isNegated() {
      return iIsNegated;
   }


   public boolean hasBeenApplied() {
      return iHasBeenApplied;
   }


   public InventoryKey getMainInventory() {
      return iMainInventory;
   }


   public Set<InventoryKey> getAllInventory() {
      return iUsages.keySet();
   }


   public Set<DataTypeKey> getDataTypes( InventoryKey aInventory ) {
      if ( !iUsages.containsKey( aInventory ) ) {
         throw new RuntimeException( "Unknown inventory." );
      }
      return iUsages.get( aInventory ).keySet();
   }


   public BigDecimal getUsageValue( InventoryKey aInventory, DataTypeKey aType ) {
      if ( !iUsages.containsKey( aInventory ) ) {
         throw new RuntimeException( "Unknown inventory." );
      }
      return iUsages.get( aInventory ).get( aType ).iTsnValue;
   }


   public BigDecimal getUsageDelta( InventoryKey aInventory, DataTypeKey aType ) {
      if ( !iUsages.containsKey( aInventory ) ) {
         throw new RuntimeException( "Unknown inventory." );
      }
      return iUsages.get( aInventory ).get( aType ).iTsnDelta;
   }


   private class Usage {

      final DataTypeKey iType;
      final BigDecimal iTsnValue;
      final BigDecimal iTsnDelta;
      final BigDecimal iTsoValue;
      final BigDecimal iTsoDelta;
      final BigDecimal iTsiValue;
      final BigDecimal iTsiDelta;


      public Usage(DataTypeKey aType, BigDecimal aValue, BigDecimal aDelta) {
         iType = aType;
         iTsnValue = aValue;
         iTsoValue = aValue;
         iTsiValue = aValue;
         iTsnDelta = aDelta;
         iTsoDelta = aDelta;
         iTsiDelta = aDelta;
      }

   }

}
