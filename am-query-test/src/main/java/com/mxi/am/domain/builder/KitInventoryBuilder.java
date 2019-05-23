
package com.mxi.am.domain.builder;

import com.mxi.am.domain.KitInventory;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.inv.InvKitMapTable;


public class KitInventoryBuilder {

   public static InventoryKey build( KitInventory aKitInventory ) {

      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withClass( RefInvClassKey.KIT );
      lInventoryBuilder.withSerialNo( aKitInventory.getSerialNo() );
      lInventoryBuilder.withPartNo( aKitInventory.getPartNo() );
      lInventoryBuilder.atLocation( aKitInventory.getLocation() );
      lInventoryBuilder.withOwner( aKitInventory.getOwner() );

      InventoryKey lKitKey = lInventoryBuilder.build();

      for ( InventoryKey lSubKitInv : aKitInventory.getKitContents() ) {

         InvKitMapKey lInvKitMapKey = new InvKitMapKey( lKitKey, lSubKitInv );

         InvKitMapTable lInvKitMapTable = InvKitMapTable.create( lInvKitMapKey );
         lInvKitMapTable.insert();
      }

      return lKitKey;
   }
}
