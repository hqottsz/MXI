
package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;


public class KitInventory {

   private PartNoKey iPartNo;

   private String iSerialNo;

   private LocationKey iLocation;

   private OwnerKey iOwner;

   private List<InventoryKey> iKitContentInventory = new ArrayList<InventoryKey>();


   public String getSerialNo() {
      return iSerialNo;
   }


   public void setSerialNo( String aSerialNo ) {
      iSerialNo = aSerialNo;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public void addKitContentInventory( InventoryKey aKitContentInv ) {
      iKitContentInventory.add( aKitContentInv );
   }


   public List<InventoryKey> getKitContents() {
      return iKitContentInventory;
   }


   public PartNoKey getPartNo() {
      return iPartNo;
   }


   public void setPartNo( PartNoKey aPartNo ) {
      iPartNo = aPartNo;
   }

}
