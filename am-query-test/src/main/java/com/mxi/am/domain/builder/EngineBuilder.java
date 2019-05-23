package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.inv.InvInvTable;


public class EngineBuilder {

   public static InventoryKey build( Engine aEngine ) {
      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withClass( RefInvClassKey.ASSY );
      lInventoryBuilder.withOriginalAssembly( aEngine.getOriginalAssembly() );
      lInventoryBuilder.withSerialNo( aEngine.getSerialNumber() );
      lInventoryBuilder.withDescription( aEngine.getDescription() );
      lInventoryBuilder.withPartNo( aEngine.getPartNumber() );
      lInventoryBuilder.withPartGroup( aEngine.getPartGroup() );
      lInventoryBuilder.withConfigSlotPosition( aEngine.getPosition() );
      lInventoryBuilder.atLocation( aEngine.getLocation() );
      lInventoryBuilder.withOwner( aEngine.getOwner() );
      lInventoryBuilder.withApplicabilityCode( aEngine.getApplicabilityCode() );
      lInventoryBuilder.withComplete( aEngine.isComplete() );
      lInventoryBuilder.withRequiredAuthority( aEngine.getAuthority() );

      if ( aEngine.getCondition() != null ) {
         lInventoryBuilder.withCondition( aEngine.getCondition() );
      }

      if ( ( boolean ) defaultIfNull( aEngine.isAllowingSynchronization(), false ) ) {
         lInventoryBuilder.allowSync();
      }

      for ( Entry<DataTypeKey, BigDecimal> lUsageEntry : aEngine.getUsage().entrySet() ) {
         BigDecimal lUsageValue =
               ( BigDecimal ) defaultIfNull( lUsageEntry.getValue(), BigDecimal.ZERO );
         lInventoryBuilder.withUsage( lUsageEntry.getKey(), lUsageValue.doubleValue() );
      }

      AssemblyKey lAssemblyKey = aEngine.getAssembly();
      if ( lAssemblyKey != null ) {
         // When given an assembly, determine if it has a root config slot configured.
         EqpAssmblBom lRootConfigSlot =
               EqpAssmblBom.findByPrimaryKey( new ConfigSlotKey( lAssemblyKey, 0 ) );
         if ( lRootConfigSlot != null ) {

            // When assembly provided we will use its root config slot and position.
            ConfigSlotKey lRootConfigSlotKey = lRootConfigSlot.getPk();
            ConfigSlotPositionKey lRootConfigSlotPosition = new ConfigSlotPositionKey(
                  lRootConfigSlotKey, EqpAssmblPos.getFirstPosId( lRootConfigSlotKey ) );
            lInventoryBuilder.withConfigSlotPosition( lRootConfigSlotPosition );
         }
      }

      InventoryKey lEngineKey = lInventoryBuilder.build();

      {
         // Set any additional inv_inv columns.
         InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( lEngineKey );

         // Set the original-assembly to equal the assembly if one was not provided.
         if ( lInvInvTable.getOrigAssmbl() == null && lInvInvTable.getAssmbl() != null ) {
            lInvInvTable.setOrigAssmbl( lInvInvTable.getAssmbl().getAssemblyKey() );
         }

         // The deprecated InventoryBuilder does not support setting the barcode, so do it here.
         lInvInvTable.setBarcode( aEngine.getBarcode() );

         lInvInvTable.update();
      }

      InventoryKey lParent = aEngine.getParent();
      if ( lParent != null ) {
         AttachmentService.attach( lEngineKey, lParent );
      } else {
         InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lEngineKey );
         lInvTable.setNhInvNo( null );
         lInvTable.setHInvNo( lEngineKey );
         lInvTable.setAssmblInvNo( lEngineKey );
         lInvTable.update();
      }

      // Attach all the sub-inventory.
      Set<InventoryKey> lAttachedInvs = new HashSet<InventoryKey>();
      lAttachedInvs.addAll( aEngine.getSerializedKeys() );
      lAttachedInvs.addAll( aEngine.getTrackedKeys() );
      lAttachedInvs
            .addAll( TrackedInventoryBuilder.buildAll( aEngine.getTrackedConfigurations() ) );
      lAttachedInvs.addAll(
            SystemBuilder.buildAll( aEngine.getSystemConfigurations(), aEngine.getUsage() ) );

      for ( InventoryKey lAttachedInv : lAttachedInvs ) {
         AttachmentService.attach( lAttachedInv, lEngineKey );
      }

      for ( InstallationRecord lInstallationRecord : aEngine.getInstallationRecords() ) {
         lInstallationRecord.setInventory( lEngineKey );
         InstallationRecordBuilder.build( lInstallationRecord );
      }

      for ( RemovalRecord lRemovalRecord : aEngine.getRemovalRecords() ) {
         lRemovalRecord.setInventory( lEngineKey );
         RemovalRecordBuilder.build( lRemovalRecord );
      }

      return lEngineKey;
   }


   public static Set<InventoryKey>
         buildAll( Set<DomainConfiguration<Engine>> aEngineConfigurations ) {

      Set<InventoryKey> lEngineKeys = new HashSet<InventoryKey>();

      for ( DomainConfiguration<Engine> lEngineConfig : aEngineConfigurations ) {
         Engine lEngine = new Engine();
         lEngineConfig.configure( lEngine );
         lEngineKeys.add( EngineBuilder.build( lEngine ) );
      }

      return lEngineKeys;
   }

}
