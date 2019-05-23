package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.System;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.inv.InvInvTable;


public class SystemBuilder {

   public static InventoryKey build( System aSys ) {
      InventoryBuilder lInventoryBuilder = new InventoryBuilder();

      // The name (a.k.a. description) uniquely identifies a system among its peers under a single
      // parent.
      lInventoryBuilder.withDescription( aSys.getName() );

      lInventoryBuilder.withClass( RefInvClassKey.SYS );
      lInventoryBuilder.withSerialNo( aSys.getSerialNumber() );
      lInventoryBuilder.withPartNo( aSys.getPartNumber() );
      lInventoryBuilder.atLocation( aSys.getLocation() );
      lInventoryBuilder.withOwner( aSys.getOwner() );
      lInventoryBuilder.withConfigSlotPosition( aSys.getPosition() );
      lInventoryBuilder.withCondition( aSys.getCondition() );

      if ( aSys.isLocked().orElse( false ) ) {
         lInventoryBuilder.isLocked();
      }

      for ( Entry<DataTypeKey, BigDecimal> lUsageEntry : aSys.getUsage().entrySet() ) {
         BigDecimal lValue = lUsageEntry.getValue();
         double lCurrentUsage = ( lValue != null ) ? lValue.doubleValue() : 0.0d;
         lInventoryBuilder.withUsage( lUsageEntry.getKey(), lCurrentUsage );
      }

      InventoryKey lSysKey = lInventoryBuilder.build();

      // Set other attributes that are not supported by the InventoryBuilder.
      if ( aSys.getBarcode().isPresent() ) {
         InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( lSysKey );
         lInvInvTable.setBarcode( aSys.getBarcode().get() );
         lInvInvTable.update();
      }

      // Attach all the sub-inventory.
      Set<InventoryKey> lAttachedInvs = new HashSet<InventoryKey>();
      lAttachedInvs.addAll( aSys.getSerializedKeys() );
      lAttachedInvs.addAll( aSys.getTrackedKeys() );
      lAttachedInvs.addAll( aSys.getEngineKeys() );
      lAttachedInvs.addAll( TrackedInventoryBuilder.buildAll( aSys.getTrackedConfigurations() ) );
      lAttachedInvs.addAll( EngineBuilder.buildAll( aSys.getEngineConfigurations() ) );
      lAttachedInvs.addAll( buildAll( aSys.getSubSystemConfigurations(), aSys.getUsage() ) );

      for ( InventoryKey lAttachedInv : lAttachedInvs ) {
         AttachmentService.attach( lAttachedInv, lSysKey );
      }

      return lSysKey;
   }


   public static Set<InventoryKey> buildAll( Set<DomainConfiguration<System>> aSystemConfigs,
         Map<DataTypeKey, BigDecimal> aUsage ) {

      Set<InventoryKey> lSystemKeys = new HashSet<InventoryKey>();
      for ( DomainConfiguration<System> lSysConfig : aSystemConfigs ) {
         System lSystem = new System();
         lSysConfig.configure( lSystem );
         lSystem.setUsage( aUsage );

         lSystemKeys.add( SystemBuilder.build( lSystem ) );
      }
      return lSystemKeys;
   }

}
