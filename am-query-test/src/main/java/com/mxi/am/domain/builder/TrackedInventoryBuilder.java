package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.inv.InvAssociationDao;
import com.mxi.mx.core.table.inv.InvAssociationTableRow;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.JdbcInvAssociationDao;


public class TrackedInventoryBuilder {

   public static InventoryKey build( TrackedInventory aTrk ) {
      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withClass( RefInvClassKey.TRK );
      lInventoryBuilder.withPartNo( aTrk.getPartNumber() );
      lInventoryBuilder.withPartGroup( aTrk.getPartGroup() );
      lInventoryBuilder.withSerialNo( aTrk.getSerialNumber() );
      lInventoryBuilder.atLocation( aTrk.getLocation() );
      lInventoryBuilder.withOwner( aTrk.getOwner() );
      lInventoryBuilder.withDescription( aTrk.getDescription() );
      lInventoryBuilder.manufacturedOn( aTrk.getManufactureDate() );
      lInventoryBuilder.withOriginalAssembly( aTrk.getOriginalAssembly() );
      lInventoryBuilder.withComplete( aTrk.isComplete() );

      if ( ( boolean ) defaultIfNull( aTrk.isLocked(), false ) ) {
         lInventoryBuilder.isLocked();
      }

      if ( aTrk.isLocked() ) {
         lInventoryBuilder.isLocked();
      }

      if ( aTrk.getCondition() != null ) {
         lInventoryBuilder.withCondition( aTrk.getCondition() );
      } else {
         lInventoryBuilder.withCondition( RefInvCondKey.INSRV );
      }

      if ( aTrk.isIssued() ) {
         lInventoryBuilder.isIssued();
      }

      if ( ( boolean ) defaultIfNull( aTrk.isAllowingSynchronization(), false ) ) {
         lInventoryBuilder.allowSync();
      }

      for ( Entry<DataTypeKey, BigDecimal> lUsageEntry : aTrk.getUsage().entrySet() ) {
         lInventoryBuilder.withUsage( lUsageEntry.getKey(), lUsageEntry.getValue().doubleValue() );
      }

      // Set position if provided.
      ConfigSlotPositionKey lPositionKey = aTrk.getPosition();

      if ( lPositionKey == null ) {
         // Otherwise attempt to determine the position using the assembly, config slot, and
         // position codes.
         if ( !StringUtils.isBlank( aTrk.getAssemblyCode() )
               && !StringUtils.isBlank( aTrk.getConfigSlotCode() )
               && !StringUtils.isBlank( aTrk.getPositionCode() ) ) {

            ConfigSlotKey lConfigSlotKey =
                  EqpAssmblBom.getBomItemKey( aTrk.getAssemblyCode(), aTrk.getConfigSlotCode() );

            Integer lPosId = EqpAssmblPos.getAssmblPosId( lConfigSlotKey, aTrk.getPositionCode() );
            lPositionKey = new ConfigSlotPositionKey( lConfigSlotKey, lPosId );
         }
      }

      AssemblyKey lAssemblyKey = aTrk.getAssembly();

      if ( lPositionKey != null && lAssemblyKey != null ) {
         throw new RuntimeException( "Can not provide both an assembly and a position." );
      }

      // Set the position to the assembly's root config slot position, if provided.
      if ( lAssemblyKey != null ) {
         EqpAssmblBom lRootConfigSlot =
               EqpAssmblBom.findByPrimaryKey( new ConfigSlotKey( lAssemblyKey, 0 ) );

         if ( lRootConfigSlot != null ) {
            ConfigSlotKey lRootConfigSlotKey = lRootConfigSlot.getPk();
            lPositionKey = new ConfigSlotPositionKey( lRootConfigSlotKey,
                  EqpAssmblPos.getFirstPosId( lRootConfigSlotKey ) );
         }
      }

      lInventoryBuilder.withConfigSlotPosition( lPositionKey );

      // Build the tracked inventory.
      InventoryKey lTrkKey = lInventoryBuilder.build();

      // Update any required columns after the inventory is build using InventoryBuilder.
      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lTrkKey );

      // The deprecated InventoryBuilder does not support setting a barcode, so do it here.
      if ( aTrk.getBarcode() != null ) {
         lInvTable.setBarcode( aTrk.getBarcode() );
      }

      // Check if a parent is provided or not, in order to set the denormalized related inv keys.
      InventoryKey lParent = aTrk.getParent();
      if ( lParent != null ) {
         AttachmentService.attach( lTrkKey, lParent );
      } else {
         lInvTable.setNhInvNo( null );
         lInvTable.setHInvNo( lTrkKey );
         lInvTable.setAssmblInvNo( null );
      }

      lInvTable.update();

      Set<InventoryKey> lAttachedInvs = new HashSet<InventoryKey>();

      // Build any serialized or tracked inventories if configurations provided (and add to list of
      // attached inv).
      lAttachedInvs.addAll( aTrk.getSerializedKeys() );
      lAttachedInvs.addAll( aTrk.getTrackedKeys() );
      lAttachedInvs.addAll( buildAll( aTrk.getTrackedConfigurations() ) );

      for ( InventoryKey lAttachedInv : lAttachedInvs ) {
         AttachmentService.attach( lAttachedInv, lTrkKey );
      }

      for ( InstallationRecord lInstallationRecord : aTrk.getInstallationRecords() ) {
         lInstallationRecord.setInventory( lTrkKey );
         InstallationRecordBuilder.build( lInstallationRecord );
      }

      for ( RemovalRecord lRemoval : aTrk.getRemovalRecords() ) {
         lRemoval.setInventory( lTrkKey );
         RemovalRecordBuilder.build( lRemoval );
      }

      // Add inventory association.
      aTrk.getAssociation().ifPresent( aAssociation -> {
         InvAssociationDao lDao = new JdbcInvAssociationDao();
         InvAssociationTableRow lInvAssociationTableRow =
               lDao.create( lTrkKey ).setAssociationId( aAssociation );
         lDao.insert( lInvAssociationTableRow );
      } );

      return lTrkKey;
   }


   public static Set<InventoryKey>
         buildAll( Set<DomainConfiguration<TrackedInventory>> aConfigurations ) {

      Set<InventoryKey> lTrackedKeys = new HashSet<InventoryKey>();

      for ( DomainConfiguration<TrackedInventory> lConfig : aConfigurations ) {
         TrackedInventory lTrk = new TrackedInventory();
         lConfig.configure( lTrk );
         lTrackedKeys.add( build( lTrk ) );

      }

      return lTrackedKeys;
   }

}
