package com.mxi.am.domain.builder;

import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.dao.shipment.ShipShipmentLineMpDao;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.ShipmentLineMpKey;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineMpTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;


/**
 * This is a data builder that builds shipment lines.
 *
 * @author dsewell
 */
public class ShipmentLineBuilder implements DomainBuilder<ShipmentLineKey> {

   private Double iExpectedQty;

   private Double iReceivedQty;

   private String iMpKeySdesc;

   private PurchaseOrderLineKey iOrderLine;

   private PartNoKey iPart;

   private InventoryKey iInventory;

   private String iSerialNo = null;

   private final ShipmentKey iShipmentKey;


   /**
    * Creates a new {@linkplain ShipmentLineBuilder} object.
    *
    * @param aShipmentKey
    *           The shipment this line belongs to.
    */
   public ShipmentLineBuilder(ShipmentKey aShipmentKey) {
      iShipmentKey = aShipmentKey;
   }


   /**
    * Builds the order line.
    *
    * @return The order line key
    */
   @Override
   public ShipmentLineKey build() {
      ShipShipmentLineTable lShipmentLine = ShipShipmentLineTable.create();

      lShipmentLine.setShipment( iShipmentKey );
      lShipmentLine.setPartNo( iPart );
      lShipmentLine.setExpectQt( iExpectedQty );
      lShipmentLine.setPoLine( iOrderLine );
      lShipmentLine.setInventory( iInventory );
      lShipmentLine.setReceiveQt( iReceivedQty );
      lShipmentLine.setSerialNoOem( iSerialNo );

      ShipmentLineKey lShipmentLineKey = lShipmentLine.insert();

      if ( iMpKeySdesc != null ) {
         ShipShipmentLineMpTable lShipmentMpLine =
               InjectorContainer.get().getInstance( ShipShipmentLineMpDao.class )
                     .create( new ShipmentLineMpKey( lShipmentLineKey ) );
         lShipmentMpLine.setMpKeySdesc( iMpKeySdesc );
         lShipmentMpLine.insert();
      }

      return lShipmentLineKey;
   }


   /**
    * Sts the order line
    *
    * @param aOrderLine
    *           The order line
    *
    * @return The builder
    */
   public ShipmentLineBuilder forOrderLine( PurchaseOrderLineKey aOrderLine ) {
      iOrderLine = aOrderLine;

      PartNoKey lPart = POLineTable.findByPrimaryKey( aOrderLine ).getPartNo();
      if ( lPart != null ) {
         iPart = lPart;
      }

      return this;
   }


   /**
    * Sets the part to be shipped
    *
    * @param aPart
    *           The part
    *
    * @return The builder
    */
   public ShipmentLineBuilder forPart( PartNoKey aPart ) {
      iPart = aPart;

      return this;
   }


   /**
    * Sets the inventory to be shipped
    *
    * @param aInventory
    *           The inventory
    *
    * @return The builder
    */
   public ShipmentLineBuilder forInventory( InventoryKey aInventory ) {
      iInventory = aInventory;

      return this;
   }


   /**
    * Sets the expected quantity.
    *
    * @param aQuantity
    *           The quantity
    *
    * @return The builder
    */
   public ShipmentLineBuilder withExpectedQuantity( Double aQuantity ) {
      iExpectedQty = aQuantity;

      return this;
   }


   /**
    * Sets the serial no.
    *
    * @param aSerialNo
    *           The serial no
    *
    * @return The builder
    */
   public ShipmentLineBuilder withSerialNo( String aSerialNo ) {
      iSerialNo = aSerialNo;

      return this;
   }


   /**
    * Sets the received quantity.
    *
    * @param aReceiveQty
    *           The quantity
    *
    * @return The builder
    */
   public ShipmentLineBuilder withReceivedQuantity( Double aReceiveQty ) {
      iReceivedQty = aReceiveQty;

      return this;
   }


   /**
    *
    * Sets the MP key
    *
    * @param aMpKeySdesc
    * @return
    */
   public ShipmentLineBuilder withMpKeySdesc( String aMpKeySdesc ) {
      iMpKeySdesc = aMpKeySdesc;
      return this;
   }
}
