
package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.Map.Entry;

import com.mxi.am.domain.SerializedInventory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.inv.InvInvTable;


public class SerializedInventoryBuilder {

   public static InventoryKey build( SerializedInventory aSer ) {
      InventoryBuilder lInventoryBuilder = new InventoryBuilder();
      lInventoryBuilder.withClass( RefInvClassKey.SER );
      lInventoryBuilder.withPartNo( aSer.getPartNumber() );
      lInventoryBuilder.withSerialNo( aSer.getSerialNumber() );
      lInventoryBuilder.atLocation( aSer.getLocation() );
      lInventoryBuilder.withOwner( aSer.getOwner() );
      lInventoryBuilder.withComplete( aSer.getComplete() );
      lInventoryBuilder.withDescription( aSer.getInvNoSdesc() );

      lInventoryBuilder.withLockedBoolean( aSer.getLockedBoolean() );
      if ( aSer.getOwnershipType() != null ) {
         lInventoryBuilder.withOwnershipType( aSer.getOwnershipType() );
      }

      if ( aSer.getOrderLine() != null ) {
         lInventoryBuilder.withOrderLine( aSer.getOrderLine() );
      }

      if ( aSer.getCondition() != null ) {
         lInventoryBuilder.withCondition( aSer.getCondition() );
      } else {
         lInventoryBuilder.withCondition( RefInvCondKey.INSRV );
      }

      if ( aSer.getFinanceStatusCd() != null ) {
         lInventoryBuilder.withFinanceStatusCd( aSer.getFinanceStatusCd() );
      }

      if ( aSer.getAuthorityKey() != null ) {
         lInventoryBuilder.withRequiredAuthority( aSer.getAuthorityKey() );
      }

      if ( aSer.getBarcode() != null ) {
         lInventoryBuilder.withBarcode( aSer.getBarcode() );
      }

      for ( Entry<DataTypeKey, BigDecimal> lUsageEntry : aSer.getUsage().entrySet() ) {
         lInventoryBuilder.withUsage( lUsageEntry.getKey(), lUsageEntry.getValue().doubleValue() );
      }

      InventoryKey lSerKey = lInventoryBuilder.build();

      // Update any required columns after the inventory is build using InventoryBuilder.
      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lSerKey );

      // Check if a parent is provided or not, in order to set the denormalized related inv keys.
      InventoryKey lParent = aSer.getParent();
      if ( lParent != null ) {
         AttachmentService.attach( lSerKey, lParent );
      } else {
         lInvTable.setNhInvNo( null );
         lInvTable.setHInvNo( lSerKey );
         lInvTable.setAssmblInvNo( null );
      }

      if ( aSer.getReceivedDate() != null ) {
         lInvTable.setReceivedDt( aSer.getReceivedDate() );
      }
      lInvTable.update();

      return lSerKey;
   }

}
