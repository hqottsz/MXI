
package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.RefBOMClassKey;


/**
 * An Aircraft Definition
 *
 */
public class AircraftAssembly {

   private String iCode = "ACFT";
   private ConfigurationSlot iRootConfigurationSlot;
   private Set<MaintenanceProgramDefinition> iMaintenanceProgramDefinitions =
         new HashSet<MaintenanceProgramDefinition>();
   private List<DomainConfiguration<Panel>> iPanelConfigurations = new ArrayList<>();
   private List<DomainConfiguration<UsageDefinition>> iUsageDefinitionConfigurations =
         new ArrayList<>();


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public void
         setRootConfigurationSlot( DomainConfiguration<ConfigurationSlot> aDomainConfiguration ) {
      iRootConfigurationSlot = new ConfigurationSlot();
      iRootConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
      aDomainConfiguration.configure( iRootConfigurationSlot );
   }


   public ConfigurationSlot getRootConfigurationSlot() {
      return iRootConfigurationSlot;
   }


   public void addMaintenanceProgramDefinition() {
      iMaintenanceProgramDefinitions.add( new MaintenanceProgramDefinition() );
   }


   public void addMaintenanceProgramDefinition(
         DomainConfiguration<MaintenanceProgramDefinition> aDomainConfiguration ) {
      MaintenanceProgramDefinition lMaintenanceProgramDefinition =
            new MaintenanceProgramDefinition();
      aDomainConfiguration.configure( lMaintenanceProgramDefinition );
      iMaintenanceProgramDefinitions.add( lMaintenanceProgramDefinition );
   }


   public Set<MaintenanceProgramDefinition> getMaintenanceProgramDefinitions() {
      return iMaintenanceProgramDefinitions;
   }


   public List<DomainConfiguration<Panel>> getPanelConfigurations() {
      return iPanelConfigurations;
   }


   public void addPanelConfiguration( DomainConfiguration<Panel> aPanelConfiguration ) {
      iPanelConfigurations.add( aPanelConfiguration );
   }


   public List<DomainConfiguration<UsageDefinition>> getUsageDefinitionConfigurations() {
      return iUsageDefinitionConfigurations;
   }


   public void addUsageDefinitionConfiguration(
         DomainConfiguration<UsageDefinition> aUsageDefinitionConfiguration ) {
      iUsageDefinitionConfigurations.add( aUsageDefinitionConfiguration );
   }

}
