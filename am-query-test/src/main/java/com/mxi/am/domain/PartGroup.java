package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;


public class PartGroup {

   private String iApplicabilityRange;
   private String iCode;
   private String iName;
   private RefInvClassKey iInventoryClass;
   private ConfigSlotKey iConfigurationSlot;

   private List<PartNoKey> iParts = new ArrayList<PartNoKey>();
   private List<DomainConfiguration<Part>> iPartConfigs =
         new ArrayList<DomainConfiguration<Part>>();
   private Boolean iLineReplaceableUnit;


   public String getApplicabilityRange() {
      return iApplicabilityRange;
   }


   public void setApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aPartGroupName ) {
      iName = aPartGroupName;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aPartGroupCode ) {
      iCode = aPartGroupCode;
   }


   public RefInvClassKey getInventoryClass() {
      return iInventoryClass;
   }


   public void setInventoryClass( RefInvClassKey aInventoryClass ) {
      iInventoryClass = aInventoryClass;
   }


   public List<PartNoKey> getParts() {
      return iParts;
   }


   public void addPart( PartNoKey aPart ) {
      iParts.add( aPart );
   }


   public List<DomainConfiguration<Part>> getPartConfigurations() {
      return iPartConfigs;
   }


   public void addPart( DomainConfiguration<Part> aPartConfiguration ) {
      iPartConfigs.add( aPartConfiguration );
   }


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigurationSlot;
   }


   public void setConfigurationSlot( ConfigSlotKey aConfigurationSlot ) {
      iConfigurationSlot = aConfigurationSlot;
   }


   public Optional<Boolean> isLineReplaceableUnit() {
      return Optional.ofNullable( iLineReplaceableUnit );
   }


   public void setLineReplaceableUnit( Boolean aLineReplaceableUnit ) {
      this.iLineReplaceableUnit = aLineReplaceableUnit;
   }

}
