
package com.mxi.mx.core.unittest.table.ship;

import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.ship.ShipShipmentTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>ship_shipment</code> table.
 *
 * @author cjb
 * @created August 5, 2003
 */
public class ShipShipment extends ShipShipmentTable {

   /**
    * Initializes the class. Retrieves all columns in the database table for the given <code>
    * aShipment</code> primary key.
    *
    * @param aShipment
    *           primary key of the table.
    */
   public ShipShipment(ShipmentKey aShipment) {
      super( aShipment );
   }


   /**
    * Returns an array of shipment line keys for the given shipment.
    *
    * @param aShipment
    *           the shipment.
    *
    * @return the shipment line keys.
    */
   public static ShipmentLineKey[] getShipmentLines( ShipmentKey aShipment ) {
      return ShipmentService.getShipmentLines( aShipment );
   }


   /**
    * Asserts that the <code>shipment_type</code> and argument values are equal.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertShipmentType( RefShipmentTypeKey aExpected ) {
      MxAssert.assertEquals( "shipment_type_pk", aExpected, this.getShipmentType() );
   }
}
