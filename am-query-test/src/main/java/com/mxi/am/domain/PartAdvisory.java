package com.mxi.am.domain;

import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.VendorKey;


/**
 * Domain entity for Part Advisory
 *
 */
public class PartAdvisory {

   private String name;

   private VendorKey vendor;

   private PartNoKey partNo;


   public PartNoKey getPartNo() {
      return partNo;
   }


   public void setPartNo( PartNoKey partNo ) {
      this.partNo = partNo;
   }


   public VendorKey getVendor() {
      return vendor;
   }


   public void setVendor( VendorKey vendor ) {
      this.vendor = vendor;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }
}
