package com.mxi.am.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;


public class RemovalRecord {

   private Date iRemovalDate;
   private RemovedInventoryInfo iMainInv = new RemovedInventoryInfo();
   private Set<DomainConfiguration<RemovedInventoryInfo>> iSubInvs =
         new HashSet<DomainConfiguration<RemovedInventoryInfo>>();


   public Date getRemovalDate() {
      return iRemovalDate;
   }


   public void setRemovalDate( Date aDate ) {
      iRemovalDate = aDate;
   }


   public InventoryKey getInventory() {
      return iMainInv.getInventory();
   }


   public void setInventory( InventoryKey aInventory ) {
      iMainInv.setInventory( aInventory );
   }


   public InventoryKey getParent() {
      return iMainInv.getParent();
   }


   public void setParent( InventoryKey aParent ) {
      iMainInv.setParent( aParent );
   }


   public InventoryKey getHighest() {
      return iMainInv.getHighest();
   }


   public void setHighest( InventoryKey aHighest ) {
      iMainInv.setHighest( aHighest );
   }


   public InventoryKey getAssembly() {
      return iMainInv.getAssembly();
   }


   public void setAssembly( InventoryKey aAssembly ) {
      iMainInv.setAssembly( aAssembly );
   }


   public ConfigSlotPositionKey getPosition() {
      return iMainInv.getPosition();
   }


   public void setPosition( ConfigSlotPositionKey aBomItemPosition ) {
      iMainInv.setPosition( aBomItemPosition );
   }


   public Set<DomainConfiguration<RemovedInventoryInfo>> getSubInventories() {
      return iSubInvs;
   }


   public void addSubInventory( DomainConfiguration<RemovedInventoryInfo> aSubInv ) {
      iSubInvs.add( aSubInv );
   }


   public class RemovedInventoryInfo {

      private InventoryKey iInventoryKey;
      private InventoryKey iParentKey;
      private InventoryKey iHighestKey;
      private InventoryKey iAssemblyKey;
      private ConfigSlotPositionKey iPositionKey;
      private Set<DomainConfiguration<RemovedInventoryInfo>> iSubInvs =
            new HashSet<DomainConfiguration<RemovedInventoryInfo>>();


      public InventoryKey getInventory() {
         return iInventoryKey;
      }


      public void setInventory( InventoryKey aInventory ) {
         iInventoryKey = aInventory;
      }


      public InventoryKey getParent() {
         return iParentKey;
      }


      public void setParent( InventoryKey aParent ) {
         iParentKey = aParent;
      }


      public InventoryKey getHighest() {
         return iHighestKey;
      }


      public void setHighest( InventoryKey aHighest ) {
         iHighestKey = aHighest;
      }


      public InventoryKey getAssembly() {
         return iAssemblyKey;
      }


      public void setAssembly( InventoryKey aAssembly ) {
         iAssemblyKey = aAssembly;
      }


      public ConfigSlotPositionKey getPosition() {
         return iPositionKey;
      }


      public void setPosition( ConfigSlotPositionKey aConfigSlotPositionKey ) {
         iPositionKey = aConfigSlotPositionKey;
      }


      public Set<DomainConfiguration<RemovedInventoryInfo>> getSubInvs() {
         return iSubInvs;
      }


      public void addSubInv( DomainConfiguration<RemovedInventoryInfo> aSubInv ) {
         iSubInvs.add( aSubInv );
      }
   }

}
