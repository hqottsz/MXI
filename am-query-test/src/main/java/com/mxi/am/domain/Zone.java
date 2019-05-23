package com.mxi.am.domain;

import com.mxi.mx.core.key.AssemblyKey;


/**
 * Domain class for Zones
 *
 */
public class Zone {

   private String iZoneCode;
   private AssemblyKey iParentAssembly;


   public Zone() {
      // Nothing to see here. Move along.
   }


   public Zone(String zoneCode) {
      iZoneCode = zoneCode;
   }


   public String getZoneCode() {
      return iZoneCode;
   }


   public void setZoneCode( String zoneCode ) {
      iZoneCode = zoneCode;
   }


   public AssemblyKey getParentAssembly() {
      return iParentAssembly;
   }


   public void setParentAssembly( AssemblyKey aParentAssembly ) {
      iParentAssembly = aParentAssembly;
   }

}
