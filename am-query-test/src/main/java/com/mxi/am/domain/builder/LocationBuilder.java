package com.mxi.am.domain.builder;

import com.mxi.am.domain.Location;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.dao.location.PreferredInventoryLocationDao;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InvLocPrefMapKey;
import com.mxi.mx.core.key.LocationCapabilityKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.table.inv.InvLocCapabilityDao;
import com.mxi.mx.core.table.inv.InvLocCapabilityTableRow;
import com.mxi.mx.core.table.inv.InvLocPrefMapTable;
import com.mxi.mx.core.table.inv.InvLocTable;


public class LocationBuilder {

   public static LocationKey build( Location locationEntity ) {
      LocationKey locationKey = generatePrimaryKey();
      InvLocTable invLoc = InvLocTable.create( locationKey );

      invLoc.setLocType( locationEntity.getType() );
      invLoc.setLocCd( locationEntity.getCode() );
      invLoc.setLocName( locationEntity.getName() );
      invLoc.setOvernightShift( locationEntity.getOvernightShift() );
      invLoc.setTimeZoneCd( locationEntity.getTimeZone() );
      invLoc.setHubLocation( locationEntity.getHubLocation() );
      invLoc.setNHLocation( locationEntity.getParent() );

      // setting default to 0 if not specified to avoid null pointer exceptions
      invLoc.setInboundFlightsQt( locationEntity.getInboundFlightsQt().orElse( 0.0 ) );

      invLoc.setNoAutoRsrvBool( locationEntity.getIsPreventingAutoReservation().orElse( false ) );
      invLoc.setDefaultDock( locationEntity.getIsDefaultDock().orElse( false ) );

      boolean isSupplyLocation = locationEntity.getIsSupplyLocation().orElse( false );
      invLoc.setSupplyBool( isSupplyLocation );
      invLoc.setAutoIssueBool( locationEntity.getAutoIssueBool().orElse( false ) );

      // Helper logic - if no supply location is provided but this location is flagged as a supply
      // location then set this location's supply location to itself.
      LocationKey supplyLocation = locationEntity.getSupplyLocation();
      if ( isSupplyLocation && supplyLocation == null ) {
         supplyLocation = locationKey;
      }
      invLoc.setSupplyLoc( supplyLocation );

      invLoc.insert();

      // Build preferred locations for this location, if any.
      if ( !locationEntity.getPreferredLocations().isEmpty() ) {
         PreferredInventoryLocationDao perfInvLocDao =
               InjectorContainer.get().getInstance( PreferredInventoryLocationDao.class );

         int orderNumber = 1;
         for ( LocationKey prefLoc : locationEntity.getPreferredLocations() ) {
            InvLocPrefMapTable row =
                  perfInvLocDao.create( new InvLocPrefMapKey( locationKey, prefLoc ) );
            row.setPriorityOrder( orderNumber++ );
            perfInvLocDao.insert( row );
         }
      }

      if ( !locationEntity.getCapabilities().isEmpty() ) {
         InvLocCapabilityDao dao = InjectorContainer.get().getInstance( InvLocCapabilityDao.class );
         locationEntity.getCapabilities().forEach( capability -> {
            AssemblyKey assembly = capability.getComponent1();
            RefWorkTypeKey workType = capability.getComponent2();
            InvLocCapabilityTableRow row =
                  dao.create( new LocationCapabilityKey( locationKey, assembly, workType ) );
            dao.insert( row );
         } );
      }

      return locationKey;
   }


   private static LocationKey generatePrimaryKey() {
      // Get the next id from the sequence
      int locationDbId = Table.Util.getDatabaseId();
      int nextLocationId =
            EjbFactory.getInstance().createSequenceGenerator().nextValue( "INV_LOC_ID" );

      // Instantiate the table primary key as a proper key
      return new LocationKey( locationDbId, nextLocationId );
   }

}
