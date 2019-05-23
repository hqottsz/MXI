
package com.mxi.am.domain.builder;

import org.apache.commons.lang.ObjectUtils;

import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.MaintenanceProgramDefinition;
import com.mxi.am.domain.Panel;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.table.maint.MaintPrgmDefnTable;


public class AircraftAssemblyBuilder {

   public static AssemblyKey build( AircraftAssembly aAssembly ) {

      AssemblyBuilder lAssemblyBuilder =
            new AssemblyBuilder( aAssembly.getCode() ).withClass( RefAssmblClassKey.ACFT );
      AssemblyKey lAssemblyKey = lAssemblyBuilder.build();

      ConfigurationSlot lRootConfigSlot = aAssembly.getRootConfigurationSlot();
      if ( lRootConfigSlot == null ) {
         // Create a default root config slot.
         lRootConfigSlot = new ConfigurationSlot();
         lRootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
      }
      lRootConfigSlot.setRootAssembly( lAssemblyKey );

      // If no part group then create a default one.
      if ( lRootConfigSlot.getPartGroupConfigurations().isEmpty() ) {
         lRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

            @Override
            public void configure( PartGroup aPartGroup ) {
               // no configuration
            }
         } );
      }

      // Build the slot hierarchy recursively starting at the root
      ConfigurationSlotBuilder.build( lRootConfigSlot );

      for ( MaintenanceProgramDefinition lMaintenanceProgramDefinition : aAssembly
            .getMaintenanceProgramDefinitions() ) {
         buildMaintenanceProgramDefinition( lMaintenanceProgramDefinition, lAssemblyKey );
      }

      for ( DomainConfiguration<Panel> lPanelConfiguration : aAssembly.getPanelConfigurations() ) {

         Panel lPanel = new Panel();
         lPanelConfiguration.configure( lPanel );
         if ( lPanel.getParentAssembly() != null ) {
            throw new RuntimeException(
                  "Parent Assembly for the panel can only be set from the within the Assembly Builder" );
         }
         lPanel.setParentAssembly( lAssemblyKey );
         PanelBuilder.build( lPanel );
      }

      for ( DomainConfiguration<UsageDefinition> lUsageDefinitionConfiguration : aAssembly
            .getUsageDefinitionConfigurations() ) {
         UsageDefinition lUsageDefinition = new UsageDefinition();
         lUsageDefinitionConfiguration.configure( lUsageDefinition );
         if ( lUsageDefinition.getAssembly() != null ) {
            throw new RuntimeException(
                  "Usage Definition Assembly can only be set from the within the Assembly Builder" );
         }
         lUsageDefinition.setAssembly( lAssemblyKey );
         UsageDefinitionBuilder.build( lUsageDefinition );

      }

      return lAssemblyKey;

   }


   private static void buildMaintenanceProgramDefinition(
         MaintenanceProgramDefinition aMaintenanceProgramDefinition, AssemblyKey aAssemblyKey ) {

      Integer lLastRevisionNumber = ( Integer ) ObjectUtils
            .defaultIfNull( aMaintenanceProgramDefinition.getLastRevisionNumber(), 1 );

      MaintPrgmDefnTable lMaintPrgmDefnTable = MaintPrgmDefnTable.create();
      lMaintPrgmDefnTable.setAssembly( aAssemblyKey );
      lMaintPrgmDefnTable.setLastRevisionOrd( lLastRevisionNumber );
      lMaintPrgmDefnTable.insert();

   }

}
