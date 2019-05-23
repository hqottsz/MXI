
package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class JobCard {

   private InventoryKey iInventory;
   private TaskTaskKey iDefinition;
   private RefEventStatusKey iStatus;
   private Boolean iHistorical;
   private TaskKey iSuppressingJobCard;

   private List<PartRequirement> iPartRequirements = new ArrayList<>();
   private List<DomainConfiguration<Labour>> iLabourConfigs = new ArrayList<>();
   private List<DomainConfiguration<Measurement>> iMeasurementConfigs = new ArrayList<>();


   public void setInventory( InventoryKey aMainInventory ) {
      iInventory = aMainInventory;
   }


   public Optional<InventoryKey> getInventory() {
      return Optional.ofNullable( iInventory );
   }


   public void setDefinition( TaskTaskKey aDefinition ) {
      iDefinition = aDefinition;
   }


   public Optional<TaskTaskKey> getDefinition() {
      return Optional.ofNullable( iDefinition );
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public Optional<RefEventStatusKey> getStatus() {
      return Optional.ofNullable( iStatus );
   }


   public void setHistorical( boolean aHistorical ) {
      iHistorical = aHistorical;
   }


   public Optional<Boolean> isHistorical() {
      return Optional.ofNullable( iHistorical );
   }


   public void setSuppressingJobCard( TaskKey aSuppressingJobCard ) {
      iSuppressingJobCard = aSuppressingJobCard;
   }


   public Optional<TaskKey> getSuppressingJobCard() {
      return Optional.ofNullable( iSuppressingJobCard );
   }


   public void addPartRequirement( PartNoKey aPart, PartGroupKey aPartGroup,
         RefReqActionKey aRequestAction, Double aInstallQuantity ) {
      iPartRequirements
            .add( new PartRequirement( aPart, aPartGroup, aRequestAction, aInstallQuantity ) );
   }


   public List<PartRequirement> getPartRequirements() {
      return iPartRequirements;
   }


   public void addLabour( DomainConfiguration<Labour> aLabourConfiguration ) {
      iLabourConfigs.add( aLabourConfiguration );
   }


   public List<DomainConfiguration<Labour>> getLabourConfigurations() {
      return iLabourConfigs;
   }


   public void addMeasurementConfig( DomainConfiguration<Measurement> aMeasurement ) {
      iMeasurementConfigs.add( aMeasurement );
   }


   public List<DomainConfiguration<Measurement>> getMeasurementConfigs() {
      return iMeasurementConfigs;
   }


   public class PartRequirement {

      private final PartNoKey iPart;
      private final PartGroupKey iPartGroup;
      private final RefReqActionKey iRequestAction;
      private final Double iInstallQuantity;


      public PartRequirement(PartNoKey aPart, PartGroupKey aPartGroup,
            RefReqActionKey aRequestAction, Double aInstallQuantity) {
         iPart = aPart;
         iPartGroup = aPartGroup;
         iRequestAction = aRequestAction;
         iInstallQuantity = aInstallQuantity;
      }


      public PartNoKey getPart() {
         return iPart;
      }


      public PartGroupKey getPartGroup() {
         return iPartGroup;
      }


      public RefReqActionKey getRequestAction() {
         return iRequestAction;
      }


      public Double getInstallQuantity() {
         return iInstallQuantity;
      }

   }

}
