package com.mxi.am.domain;

import com.mxi.mx.core.key.LocationKey;


/**
 * Domain class for inv_loc_zone table
 *
 */
public class LocationZone {

   private LocationKey location;
   private Integer routeOrder;


   public LocationKey getLocation() {
      return location;
   }


   public void setLocation( LocationKey location ) {
      this.location = location;
   }


   public Integer getRouteOrder() {
      return routeOrder;
   }


   public void setRouteOrder( Integer routerOrder ) {
      this.routeOrder = routerOrder;
   }

}
