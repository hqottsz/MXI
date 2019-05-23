package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Aircraft.Blackout;
import com.mxi.am.domain.Aircraft.CapabilityLevel;
import com.mxi.am.domain.Domain;
import com.mxi.mx.core.key.AircraftCapabilityKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.AcftCapLevelsTable;
import com.mxi.mx.core.table.inv.InvAssociationDao;
import com.mxi.mx.core.table.inv.InvAssociationTableRow;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.JdbcInvAssociationDao;


public final class AircraftBuilder {

   public static InventoryKey build( Aircraft aAircraft ) {

      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withForecastModel( aAircraft.getForecastModel() );
      lInventoryBuilder.withPartNo( aAircraft.getPart() );
      lInventoryBuilder.withApplicabilityCode( aAircraft.getApplicabilityCode() );
      lInventoryBuilder.withSerialNo( aAircraft.getSerialNumber() );
      lInventoryBuilder.withOperator( aAircraft.getOperator() );
      lInventoryBuilder.withOriginalAssembly( aAircraft.getOriginalAssembly() );
      lInventoryBuilder.withOwner( aAircraft.getOwner() );
      lInventoryBuilder.withRequiredAuthority( aAircraft.getAuthority() );
      lInventoryBuilder.withReleaseDate( aAircraft.getReleaseDate() );
      lInventoryBuilder.withReleaseNumber( aAircraft.getReleaseNumber() );
      lInventoryBuilder.withReleaseRemarks( aAircraft.getReleaseRemarks() );
      lInventoryBuilder.withComplete( aAircraft.isComplete().orElse( true ) );

      if ( ( boolean ) defaultIfNull( aAircraft.isAllowingSynchronization(), false ) ) {
         lInventoryBuilder.allowSync();
      }

      if ( ( boolean ) defaultIfNull( aAircraft.isLocked(), false ) ) {
         lInventoryBuilder.isLocked();
      }

      if ( aAircraft.getCondition() != null ) {
         lInventoryBuilder.withCondition( aAircraft.getCondition() );
      }

      // if there is no description, add one
      if ( aAircraft.getDescription() == null ) {
         aAircraft.setDescription( "default description" );
      }
      lInventoryBuilder.withDescription( aAircraft.getDescription() );

      if ( aAircraft.getLocation() == null ) {
         aAircraft.setLocation( Domain.createLocation() );
      }
      lInventoryBuilder.atLocation( aAircraft.getLocation() );

      // Set up usage tracking and current usage.
      for ( Entry<DataTypeKey, BigDecimal> lUsageEntry : aAircraft.getUsage().entrySet() ) {
         BigDecimal lValue = lUsageEntry.getValue();
         double lCurrentUsage = ( lValue != null ) ? lValue.doubleValue() : 0.0d;
         lInventoryBuilder.withUsage( lUsageEntry.getKey(), lCurrentUsage );
      }

      AssemblyKey lAssemblyKey = aAircraft.getAssembly();
      if ( lAssemblyKey != null ) {
         // When given an assembly, determine if it has a root config slot configured.
         EqpAssmblBom lRootConfigSlot =
               EqpAssmblBom.findByPrimaryKey( new ConfigSlotKey( lAssemblyKey, 0 ) );
         if ( lRootConfigSlot != null ) {

            // When assembly provided, root config slot and its postion is already created, we shall
            // use it.
            ConfigSlotKey lRootConfigSlotKey = lRootConfigSlot.getPk();
            ConfigSlotPositionKey lRootConfigSlotPosition = new ConfigSlotPositionKey(
                  lRootConfigSlotKey, EqpAssmblPos.getFirstPosId( lRootConfigSlotKey ) );
            lInventoryBuilder.withConfigSlotPosition( lRootConfigSlotPosition );
         }
      }

      lInventoryBuilder.manufacturedOn( aAircraft.getManufacturedDate() );

      lInventoryBuilder.receivedOn( aAircraft.getReceivedDate() );

      InventoryKey lAircraftKey = lInventoryBuilder.build();

      // Set the original-assembly to equal the assembly, if assembly is set.
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( lAircraftKey );
      if ( lInvInvTable.getAssmbl() != null ) {
         lInvInvTable.setOrigAssmbl( lInvInvTable.getAssmbl().getAssemblyKey() );
         lInvInvTable.update();
      }

      if ( lInvInvTable.getBarcodeSdesc() == null && aAircraft.getBarcode() != null ) {
         lInvInvTable.setBarcode( aAircraft.getBarcode() );
         lInvInvTable.update();
      }

      if ( aAircraft.getPositionDescription() != null ) {
         lInvInvTable.setConfigPosSdesc( aAircraft.getPositionDescription() );
         lInvInvTable.update();
      }

      for ( Blackout lBlackout : aAircraft.getBlackouts() ) {
         EventKey lEventKey = EvtEventTable.generatePrimaryKey();

         EvtEventTable lEventTable = EvtEventTable.create( lEventKey );
         lEventTable.setEventType( RefEventTypeKey.BLK );
         lEventTable.setActualStartDate( lBlackout.getStart() );
         lEventTable.setEventDate( lBlackout.getEnd() );
         lEventTable.insert();

         EvtInvTable lEvtInv = EvtInvTable.create( lEventKey );
         lEvtInv.setInventoryKey( lAircraftKey );
         lEvtInv.insert();
      }

      // Attach all the sub-inventory.
      Set<InventoryKey> lAttachedInvs = new HashSet<InventoryKey>();
      lAttachedInvs.addAll( aAircraft.getTrackedKeys() );
      lAttachedInvs.addAll( aAircraft.getSerializedKeys() );
      lAttachedInvs.addAll( aAircraft.getEngineKeys() );
      lAttachedInvs
            .addAll( TrackedInventoryBuilder.buildAll( aAircraft.getTrackedConfigurations() ) );
      lAttachedInvs.addAll( EngineBuilder.buildAll( aAircraft.getEngineConfigurations() ) );
      lAttachedInvs
            .addAll( SystemBuilder.buildAll( aAircraft.getSystems(), aAircraft.getUsage() ) );

      for ( InventoryKey lAttachedInv : lAttachedInvs ) {
         AttachmentService.attach( lAttachedInv, lAircraftKey );
      }

      // Add capabilities to the aircraft
      for ( CapabilityLevel lCapabilityLevel : aAircraft.getCapabilityLevels() ) {
         AircraftCapabilityKey lAircraftCapabilityKey =
               new AircraftCapabilityKey( lAircraftKey.getDbId(), lAircraftKey.getId(),
                     lCapabilityLevel.getCapCd(), lCapabilityLevel.getCapDbId() );

         AcftCapLevelsTable lAcftCapLevelsTable =
               AcftCapLevelsTable.create( lAircraftCapabilityKey );
         lAcftCapLevelsTable.setAcftNoDbId( lAircraftCapabilityKey.getAcftNoDbId() );
         lAcftCapLevelsTable.setAcftNoId( lAircraftCapabilityKey.getAcftNoId() );
         lAcftCapLevelsTable.setCapCd( lAircraftCapabilityKey.getCapCd() );
         lAcftCapLevelsTable.setCapDbId( lAircraftCapabilityKey.getCapDbId() );
         lAcftCapLevelsTable.setConfigLevel( lCapabilityLevel.getConfigLevelCd() );
         lAcftCapLevelsTable.setConfigLevelDbId( lCapabilityLevel.getConfigLevelDbId() );
         lAcftCapLevelsTable.setLevelCd( lCapabilityLevel.getLevelCd() );
         lAcftCapLevelsTable.setCustomLevel( lCapabilityLevel.getCustomLevel() );
         lAcftCapLevelsTable.setLevelDbId( lCapabilityLevel.getLevelDbId() );
         lAcftCapLevelsTable.insert();

      }

      // Add inventory association.
      aAircraft.getAssociation().ifPresent( aAssociation -> {
         InvAssociationDao lDao = new JdbcInvAssociationDao();
         InvAssociationTableRow lRow = lDao.create( lAircraftKey ).setAssociationId( aAssociation );
         lDao.insert( lRow );
      } );

      return lAircraftKey;
   }

}
