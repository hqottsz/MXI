
package com.mxi.mx.core.unittest.table.ship;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>ship_shipment_line</code> table.
 *
 * @author cjb
 * @created August 7, 2003
 */
public class ShipShipmentLine extends ShipShipmentLineTable {

   /**
    * Initializes the class. Retrieves all columns in the database table for the given <code>
    * aShipmentLine</code> primary key.
    *
    * @param aShipmentLine
    *           primary key of the table.
    */
   public ShipShipmentLine(ShipmentLineKey aShipmentLine) {
      super( aShipmentLine );
   }


   /**
    * Asserts that the <code>inv_no_db_id:inv_no_id</code> and argument values are equal.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertInventory( InventoryKey aExpected ) {
      MxAssert.assertEquals( "inventory_pk", aExpected, getInventory() );
   }


   /**
    * Asserts that the <code>expect_qt</code> and argument values are equal.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertExpectQt( Double aExpected ) {
      MxAssert.assertEquals( "expect_qt", aExpected, getExpectQt() );
   }

}
