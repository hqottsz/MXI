package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class Block {

   private InventoryKey iInventory;
   private TaskTaskKey iDefinition;
   private RefEventStatusKey iStatus;
   private TaskKey iPreviousBlock;
   private List<TaskKey> iRequirementKeys = new ArrayList<>();
   private List<DomainConfiguration<Deadline>> iDeadlineConfigurations = new ArrayList<>();


   Block() {

   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setDefinition( TaskTaskKey aDefinition ) {
      iDefinition = aDefinition;
   }


   public TaskTaskKey getDefinition() {
      return iDefinition;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setPreviousBlock( TaskKey aPreviousBlock ) {
      iPreviousBlock = aPreviousBlock;
   }


   public TaskKey getPreviousBlock() {
      return iPreviousBlock;
   }


   public void addRequirement( TaskKey aRequirementKey ) {
      iRequirementKeys.add( aRequirementKey );
   }


   public List<TaskKey> getRequirementKeys() {
      return iRequirementKeys;
   }


   public void addDeadline( DomainConfiguration<Deadline> aDeadlineConfiguration ) {
      iDeadlineConfigurations.add( aDeadlineConfiguration );
   }


   public List<DomainConfiguration<Deadline>> getDeadlines() {
      return iDeadlineConfigurations;
   }

}
