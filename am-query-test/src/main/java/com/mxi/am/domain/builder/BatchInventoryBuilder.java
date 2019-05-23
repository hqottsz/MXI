
package com.mxi.am.domain.builder;

import com.mxi.am.domain.BatchInventory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.inv.InvInvTable;


public class BatchInventoryBuilder {

   public static InventoryKey build( BatchInventory aBatch ) {
      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withClass( RefInvClassKey.BATCH );
      lInventoryBuilder.withPartNo( aBatch.getPartNumber() );
      lInventoryBuilder.withBatchNo( aBatch.getBatchNumber() );
      lInventoryBuilder.atLocation( aBatch.getLocation() );
      lInventoryBuilder.withOwner( aBatch.getOwner() );
      lInventoryBuilder.withComplete( aBatch.getComplete() );
      lInventoryBuilder.withFinanceStatusCd( aBatch.getFinanceStatusCd() );
      lInventoryBuilder.withOwnershipType( aBatch.getOwnershipType() );
      lInventoryBuilder.withSerialNo( aBatch.getBatchNumber() );
      lInventoryBuilder.withCondition( aBatch.getCondition().orElse( RefInvCondKey.INSRV ) );

      aBatch.getBinQt().ifPresent( qty -> {
         lInventoryBuilder.withBinQt( qty.doubleValue() );
      } );

      aBatch.getOrderLine().ifPresent( line -> {
         lInventoryBuilder.withOrderLine( line );
      } );

      InventoryKey lBatchKey = lInventoryBuilder.build();

      // Update any required columns after the inventory is build using InventoryBuilder.
      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lBatchKey );

      // Check if a parent is provided or not, in order to set the denormalized related inv keys.
      InventoryKey lParent = aBatch.getParent();
      if ( lParent != null ) {
         AttachmentService.attach( lBatchKey, lParent );
      } else {
         lInvTable.setNhInvNo( null );
         lInvTable.setHInvNo( lBatchKey );
         lInvTable.setAssmblInvNo( null );
      }

      lInvTable.setIssuedBool( aBatch.isIssued() );

      lInvTable.update();

      return lBatchKey;
   }

}
