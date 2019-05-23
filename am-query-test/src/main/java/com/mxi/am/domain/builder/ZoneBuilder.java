
package com.mxi.am.domain.builder;

import java.util.concurrent.atomic.AtomicInteger;

import com.mxi.am.domain.Zone;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.table.eqp.EqpTaskZone;


/**
 * Builds a <code>eqp_task_zone</code> object
 */
public class ZoneBuilder {

   private static AtomicInteger zoneId = new AtomicInteger( 1 );

   private static final String DEFAULT_ASSEMBLY_CODE = "TEST";


   public static ZoneKey build( Zone zone ) {

      AssemblyKey assemblyKey = zone.getParentAssembly();
      if ( assemblyKey == null ) {
         assemblyKey = new AssemblyBuilder( DEFAULT_ASSEMBLY_CODE ).build();
      }

      ZoneKey zoneKey = new ZoneKey( Table.Util.getDatabaseId(), zoneId.getAndIncrement() );
      MxDataAccess.getInstance().executeInsert( "eqp_task_zone", zoneKey.getPKWhereArg() );

      EqpTaskZone eqpTaskZone = EqpTaskZone.findByPrimaryKey( zoneKey );
      eqpTaskZone.setAssembly( assemblyKey );
      eqpTaskZone.setZoneCd( zone.getZoneCode() );
      eqpTaskZone.update();
      return zoneKey;

   }
}
