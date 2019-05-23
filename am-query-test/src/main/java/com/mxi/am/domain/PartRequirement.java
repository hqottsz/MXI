package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefControlMethodKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Part requirement domain entity
 *
 */
public class PartRequirement {

   private PartGroupKey iPartGroup;
   private ConfigSlotPositionKey iConfigSlotPosition;
   private ConfigSlotPositionKey iAssemblyConfigSlotPosition;
   private TaskKey iTaskKey;
   private List<PartInstallRequest> iPartInstallRequests = new ArrayList<>();
   private List<PartRemovalRequest> iPartRemovalRequests = new ArrayList<>();
   private Double iQuantity = 1.0;
   private RefControlMethodKey controlMethodKey;


   public Double getQuantity() {
      return iQuantity;
   }


   public void setQuantity( Double aQuantity ) {
      iQuantity = aQuantity;
   }


   public PartGroupKey getPartGroup() {
      return iPartGroup;
   }


   public void setPartGroup( PartGroupKey aPartGroup ) {
      iPartGroup = aPartGroup;
   }


   public ConfigSlotPositionKey getConfigSlotPosition() {
      return iConfigSlotPosition;
   }


   public void setConfigSlotPosition( ConfigSlotPositionKey aConfigSlotPosition ) {
      iConfigSlotPosition = aConfigSlotPosition;
   }


   public ConfigSlotPositionKey getAssemblyConfigSlotPosition() {
      return iAssemblyConfigSlotPosition;
   }


   public void setAssemblyConfigSlotPosition( ConfigSlotPositionKey aAssemblyConfigSlotPosition ) {
      iAssemblyConfigSlotPosition = aAssemblyConfigSlotPosition;
   }


   public TaskKey getTaskKey() {
      return iTaskKey;
   }


   public void setTaskKey( TaskKey aTaskKey ) {
      iTaskKey = aTaskKey;
   }


   public List<PartInstallRequest> getPartInstallRequests() {
      return iPartInstallRequests;
   }


   public void setPartInstallRequest( PartInstallRequest aPartInstallRequest ) {
      iPartInstallRequests.add( aPartInstallRequest );
   }


   public List<PartRemovalRequest> getPartRemovalRequests() {
      return iPartRemovalRequests;
   }


   public void setPartRemovalRequest( PartRemovalRequest aPartRemovalRequest ) {
      iPartRemovalRequests.add( aPartRemovalRequest );
   }


   public RefControlMethodKey getControlMethodKey() {
      return controlMethodKey;
   }


   public void setControlMethodKey( RefControlMethodKey controlMethodKey ) {
      this.controlMethodKey = controlMethodKey;
   }


   public class PartInstallRequest {

      PartNoKey iPartNoKey;
      InventoryKey iInventory;
      Double iQuantity;


      public PartInstallRequest() {
         iQuantity = PartRequirement.this.iQuantity;
      }


      public Double getQuantity() {
         return iQuantity;
      }


      public void setQuantity( Double aQuantity ) {
         iQuantity = aQuantity;
      }


      public PartNoKey getPartNoKey() {
         return iPartNoKey;
      }


      public PartInstallRequest withPartNo( PartNoKey aPartNoKey ) {
         iPartNoKey = aPartNoKey;
         return this;
      }


      public InventoryKey getInventory() {
         return iInventory;
      }


      public PartInstallRequest withInventory( InventoryKey aInventory ) {
         iInventory = aInventory;
         return this;
      }

   }

   public class PartRemovalRequest {

      PartNoKey iPartNoKey;
      InventoryKey iInventory;
      Double iQuantity;
      private RefRemoveReasonKey iRemovalReason;


      public PartRemovalRequest() {
         iQuantity = PartRequirement.this.iQuantity;
      }


      public Double getQuantity() {
         return iQuantity;
      }


      public void setQuantity( Double aQuantity ) {
         iQuantity = aQuantity;
      }


      public PartNoKey getPartNoKey() {
         return iPartNoKey;
      }


      public void setPartNoKey( PartNoKey aPart ) {
         iPartNoKey = aPart;
      }


      public InventoryKey getInventory() {
         return iInventory;
      }


      public PartRemovalRequest withInventory( InventoryKey aInventory ) {
         iInventory = aInventory;
         return this;
      }


      public PartRemovalRequest withPartNo( PartNoKey aPartNoKey ) {
         iPartNoKey = aPartNoKey;
         return this;
      }


      public RefRemoveReasonKey getRemovalReason() {
         return iRemovalReason;
      }


      public PartRemovalRequest hasRemovalReason( RefRemoveReasonKey aRemovalReason ) {
         iRemovalReason = aRemovalReason;
         return this;
      }

   }
}
