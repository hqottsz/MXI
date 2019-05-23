
package com.mxi.am.domain.builder;

import java.util.ArrayList;

import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PartGroup;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.mim.MimPartNumData;


public final class ConfigurationSlotBuilder {

   public static ConfigSlotKey build( ConfigurationSlot aConfigurationSlot ) {

      // Build the config slot.
      ConfigSlotBuilder lConfigSlotBuilder = new ConfigSlotBuilder( aConfigurationSlot.getCode() )
            .withName( aConfigurationSlot.getName() )
            .withRootAssembly( aConfigurationSlot.getRootAssembly() )
            .withParent( aConfigurationSlot.getParentConfigurationSlot() );

      int lNoOfPos = aConfigurationSlot.getPositionCodes().size();
      if ( lNoOfPos < 1 ) {
         // at least 1 position
         lNoOfPos = 1;
      }
      lConfigSlotBuilder.withNumberOfPositions( lNoOfPos );

      if ( aConfigurationSlot.getConfigurationSlotClass() != null )
         lConfigSlotBuilder.withClass( aConfigurationSlot.getConfigurationSlotClass() );
      else
         lConfigSlotBuilder.withClass( RefBOMClassKey.SYS );

      ConfigSlotKey lConfigurationSlot = lConfigSlotBuilder.build();

      // Build any part groups configured for the config slot.
      boolean lIsOnlyOnePartGroup = ( aConfigurationSlot.getPartGroupConfigurations().size() == 1 );
      for ( DomainConfiguration<PartGroup> lPartGroupConfig : aConfigurationSlot
            .getPartGroupConfigurations() ) {
         PartGroup lPartGroup = new PartGroup();
         lPartGroupConfig.configure( lPartGroup );
         lPartGroup.setConfigurationSlot( lConfigurationSlot );

         // Providing some default behaviour...
         // If there is only one part group and it has no code
         // then give it the same code as the config slot.
         if ( lIsOnlyOnePartGroup && lPartGroup.getCode() == null ) {
            lPartGroup.setCode( aConfigurationSlot.getCode() );
         }

         PartGroupBuilder.build( lPartGroup );
      }

      // Build positions
      EqpAssmblBom lBomItem = EqpAssmblBom.findByPrimaryKey( lConfigurationSlot );

      // get the parent
      ConfigSlotKey lParentConfigSlotKey = lBomItem.getParentBomItem();

      // Root config slot has one position, no parent.
      // If there is otherwise no parent config slot, similarly treat as root
      if ( lBomItem.getBomClass() == RefBOMClassKey.ROOT
            || lParentConfigSlotKey.getAssemblyKey().getCd() == null ) {
         new ConfigSlotPositionBuilder().withConfigSlot( lConfigurationSlot )
               .withParentPosition( null ).build();
      } else {
         EqpAssmblBom lParentBomItem = EqpAssmblBom.findByPrimaryKey( lParentConfigSlotKey );

         // iterate over my parent's positions
         for ( int lParentBomItemPos = 1; lParentBomItemPos <= lParentBomItem
               .getPosCt(); lParentBomItemPos++ ) {
            ConfigSlotPositionKey aParentPositionKey =
                  new ConfigSlotPositionKey( lParentConfigSlotKey, lParentBomItemPos );

            ArrayList<String> lPositionCodes =
                  new ArrayList<String>( aConfigurationSlot.getPositionCodes() );
            // iterate over my own positions
            for ( int lBomItemPos = 0; lBomItemPos < lBomItem.getPosCt(); lBomItemPos++ ) {

               if ( lBomItemPos < lPositionCodes.size() ) {
                  new ConfigSlotPositionBuilder().withConfigSlot( lConfigurationSlot )
                        .withParentPosition( aParentPositionKey )
                        .withPositionCode( lPositionCodes.get( lBomItemPos ) ).build();

               } else {
                  new ConfigSlotPositionBuilder().withConfigSlot( lConfigurationSlot )
                        .withParentPosition( aParentPositionKey ).build();
               }
            }
         }
      }

      // Build any usage parameters.
      for ( DataTypeKey lDataType : aConfigurationSlot.getUsageParameters() ) {
         MimPartNumData.create( new MimPartNumDataKey( lConfigurationSlot, lDataType ) );
      }

      // Build any child config slots.
      //
      // Note: be aware that all config slot codes must be unique under an assembly. If not unique
      // then a runtime exception will be thrown indicated IX_EQPASS_ASSCDBOMCD constraint
      // violation.
      //

      for ( DomainConfiguration<ConfigurationSlot> lChildConfigSlotConfig : aConfigurationSlot
            .getConfigurationSlotConfigurations() ) {
         ConfigurationSlot lChildConfigSlot = new ConfigurationSlot();
         lChildConfigSlotConfig.configure( lChildConfigSlot );

         if ( lChildConfigSlot.getParentConfigurationSlot() != null ) {
            throw new RuntimeException( "Cannot set parent config slot on a sub config slot" );
         }
         lChildConfigSlot.setParentConfigurationSlot( lConfigurationSlot );

         if ( lChildConfigSlot.getRootAssembly() == null ) {
            lChildConfigSlot.setRootAssembly( aConfigurationSlot.getRootAssembly() );
         }

         ConfigurationSlotBuilder.build( lChildConfigSlot );

      }

      // Build any calculated usage parameters.
      // Note: needs to be after all the sub-config slots are build, as the calculated usage
      // parameters may reference them.
      for ( DomainConfiguration<CalculatedUsageParameter> lCalcParmConfig : aConfigurationSlot
            .getCalculatedUsageParameterConfigurations() ) {
         CalculatedUsageParameter lCalculatedUsageParameter = new CalculatedUsageParameter();
         lCalcParmConfig.configure( lCalculatedUsageParameter );

         lCalculatedUsageParameter.setConfigurationSlot( lConfigurationSlot );

         CalculatedUsageParameterBuilder.build( lCalculatedUsageParameter );
      }

      return lConfigurationSlot;
   }

}
