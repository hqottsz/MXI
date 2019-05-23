package com.mxi.am.domain.builder;

import com.mxi.am.domain.ShipmentLine;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;


/**
 *
 * Domain builder for shipment line entities. Performs inserts on SHIP_SHIPMENT_LINE and INV_INV.
 *
 */
public class ShipmentLineDomainBuilder {

   /**
    * @param aShipmentLine
    *           domain entity for insertion
    * @return The shipment line key from the new order.
    */
   public static ShipmentLineKey build( ShipmentLine aShipmentLine ) {

      ShipShipmentLineTable lShipmentLine = ShipShipmentLineTable.create();
      lShipmentLine.setShipment( aShipmentLine.getShipmentKey() );
      lShipmentLine.setPartNo( aShipmentLine.getPart() );
      lShipmentLine.setExpectQt( aShipmentLine.getExpectedQuantity() );
      lShipmentLine.setPoLine( aShipmentLine.getOrderLine() );
      lShipmentLine.setInventory( aShipmentLine.getInventory() );
      lShipmentLine.setSerialNoOem( aShipmentLine.getSerialNo() );

      if ( aShipmentLine.getReceivedQuantity() != null ) {
         lShipmentLine.setReceiveQt( aShipmentLine.getReceivedQuantity() );
      }

      if ( aShipmentLine.getPart() == null ) {
         // The trigger TIUBR_UOM_SHIPLINE requires there be a part if the quantity unit is null.
         // So instead of creating a default part number we will set a default quantity unit.
         lShipmentLine.setQtyUnit( RefQtyUnitKey.EA );
      }

      if ( aShipmentLine.getInventory() != null ) {
         InvInvTable lInventory = InvInvTable.create( aShipmentLine.getInventory() );
         try {
            lInventory.insert();
         } catch ( Exception e ) {
            // Record probably exists and does not need a new insert.
         }

      }

      return lShipmentLine.insert();
   }

}
