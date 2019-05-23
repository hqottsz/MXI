
package com.mxi.am.domain.builder;

import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;


public class EngineAssemblyBuilder {

   public static AssemblyKey build( EngineAssembly aEngineAssembly ) {

      String lEngAssmCode = aEngineAssembly.getCode();
      if ( StringUtils.isBlank( lEngAssmCode ) ) {
         lEngAssmCode = RefAssmblClassKey.ENG.getCd();
      }
      AssemblyKey lAssemblyKey =
            new AssemblyBuilder( lEngAssmCode ).withClass( RefAssmblClassKey.ENG ).build();

      ConfigurationSlot lRootConfigSlot = aEngineAssembly.getRootConfigurationSlot();
      if ( lRootConfigSlot == null ) {
         // Create a default root config slot.
         lRootConfigSlot = new ConfigurationSlot();
         lRootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
      }
      lRootConfigSlot.setRootAssembly( lAssemblyKey );

      if ( lRootConfigSlot.getPartGroupConfigurations().isEmpty() ) {
         // Create a default root part group.
         lRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

            @Override
            public void configure( PartGroup aPartGroup ) {
               // no configuration
            }
         } );
      }

      ConfigurationSlotBuilder.build( lRootConfigSlot );

      for ( DomainConfiguration<UsageDefinition> lUsageDefinitionConfiguration : aEngineAssembly
            .getUsageDefinitionConfigurations() ) {
         UsageDefinition lUsageDefinition = new UsageDefinition();
         lUsageDefinitionConfiguration.configure( lUsageDefinition );
         if ( lUsageDefinition.getAssembly() != null ) {
            throw new RuntimeException(
                  "Usage Definition Assembly can only be set from the within the Engine Assembly Builder" );
         }
         lUsageDefinition.setAssembly( lAssemblyKey );
         UsageDefinitionBuilder.build( lUsageDefinition );

      }

      return lAssemblyKey;
   }

}
