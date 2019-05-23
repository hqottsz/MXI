
package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;


public class EngineAssembly {

   private String iCode;
   private ConfigurationSlot iRootConfigurationSlot;
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
      aDomainConfiguration.configure( iRootConfigurationSlot );
   }


   public ConfigurationSlot getRootConfigurationSlot() {
      return iRootConfigurationSlot;
   }


   public List<DomainConfiguration<UsageDefinition>> getUsageDefinitionConfigurations() {
      return iUsageDefinitionConfigurations;
   }


   public void addUsageDefinitionConfiguration(
         DomainConfiguration<UsageDefinition> aUsageDefinitionConfiguration ) {
      iUsageDefinitionConfigurations.add( aUsageDefinitionConfiguration );
   }

}
