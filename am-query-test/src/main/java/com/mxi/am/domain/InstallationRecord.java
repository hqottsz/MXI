package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


public class InstallationRecord {

   private Date iInstallationDate;
   private InstalledInventoryInfo iMainInv = new InstalledInventoryInfo();
   private TaskKey iTask;
   private List<DomainConfiguration<InstalledInventoryInfo>> iSubInvs =
         new ArrayList<DomainConfiguration<InstalledInventoryInfo>>();
   private List<UsageSnapshot> iUsageSnapshots = new ArrayList<UsageSnapshot>();


   public Date getInstallationDate() {
      return iInstallationDate;
   }


   public InstallationRecord setInstallationDate( Date aInstalledDate ) {
      iInstallationDate = aInstalledDate;
      return this;
   }


   public InventoryKey getInventory() {
      return iMainInv.getInventory();
   }


   public InstallationRecord setInventory( InventoryKey aInventory ) {
      iMainInv.setInventory( aInventory );
      return this;
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


   public TaskKey getTask() {
      return iTask;
   }


   public void setTask( TaskKey aTask ) {
      iTask = aTask;
   }


   public List<DomainConfiguration<InstalledInventoryInfo>> getSubInventories() {
      return iSubInvs;
   }


   public void addSubInventory( DomainConfiguration<InstalledInventoryInfo> aSubInv ) {
      iSubInvs.add( aSubInv );
   }


   public List<UsageSnapshot> getUsageSnapshots() {
      return iUsageSnapshots;
   }


   public void addUsageSnapshot( UsageSnapshot aUsageSnapshot ) {
      iUsageSnapshots.add( aUsageSnapshot );
   }


   public class InstalledInventoryInfo {

      private InventoryKey iInventoryKey;
      private InventoryKey iParentKey;
      private InventoryKey iHighestKey;
      private InventoryKey iAssemblyKey;
      private List<DomainConfiguration<InstalledInventoryInfo>> iSubInvs =
            new ArrayList<DomainConfiguration<InstalledInventoryInfo>>();


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


      public List<DomainConfiguration<InstalledInventoryInfo>> getSubInvs() {
         return iSubInvs;
      }


      public void addSubInv( DomainConfiguration<InstalledInventoryInfo> aSubInv ) {
         iSubInvs.add( aSubInv );
      }
   }

}
