package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Domain entity for a vendor
 *
 */
public class Vendor {

   private String code;

   private List<PartNoKey> parts = new ArrayList<PartNoKey>();

   private LocationKey location;

   private List<PartAdvisory> iPartAdvisories = new ArrayList<PartAdvisory>();

   private HashMap<PartNoKey, PartAdvisory> iVendorPartAdvisories =
         new HashMap<PartNoKey, PartAdvisory>();


   public HashMap<PartNoKey, PartAdvisory> getVendorPartAdvisories() {
      return iVendorPartAdvisories;
   }


   public void addVendorPartAdvisory( PartNoKey partNoKey, PartAdvisory partAdvisory ) {
      iVendorPartAdvisories.put( partNoKey, partAdvisory );
   }


   public void addAdvisory( PartAdvisory aAdvisory ) {
      iPartAdvisories.add( aAdvisory );
   }


   public List<PartAdvisory> getPartAdvisories() {
      return iPartAdvisories;
   }


   public LocationKey getLocation() {
      return location;
   }


   public void setLocation( LocationKey location ) {
      this.location = location;
   }


   public String getCode() {
      return code;
   }


   public List<PartNoKey> getParts() {
      return parts;
   }


   public void addPart( PartNoKey partNo ) {
      parts.add( partNo );
   }


   public void setCode( String code ) {
      this.code = code;
   }

}
