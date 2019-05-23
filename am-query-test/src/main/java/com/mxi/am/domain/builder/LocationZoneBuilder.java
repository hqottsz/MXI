
package com.mxi.am.domain.builder;

import com.mxi.am.domain.LocationZone;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.dao.location.InvLocZoneDao;
import com.mxi.mx.core.key.InvLocZoneKey;
import com.mxi.mx.core.table.inv.InvLocZoneTable;


/**
 * Builds a <code>inv_loc_zone</code> object
 */
public class LocationZoneBuilder {

   public static InvLocZoneKey build( LocationZone entity ) {

      InvLocZoneDao dao = InjectorContainer.get().getInstance( InvLocZoneDao.class );

      InvLocZoneKey pk = new InvLocZoneKey( entity.getLocation() );

      InvLocZoneTable tableRow = dao.create( pk );
      tableRow.setRouteOrder( entity.getRouteOrder() );
      dao.insert( tableRow );

      return pk;

   }
}
