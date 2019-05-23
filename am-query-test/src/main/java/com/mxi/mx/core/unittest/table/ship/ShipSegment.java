
package com.mxi.mx.core.unittest.table.ship;

import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>ship_segment</code> table.
 *
 * @author cdaley
 */
public class ShipSegment extends ShipSegmentTable {

   /**
    * Creates a new ShipSegment object.
    *
    * @param aShipSegmentKey
    *           the ship segment key
    */
   public ShipSegment(ShipSegmentKey aShipSegmentKey) {
      super( aShipSegmentKey );
   }


   /**
    * Asserts that the <code>segment_status_db_id:segment_status_cd</code> and argument values are
    * equal.
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertStatus( RefShipSegmentStatusKey aExpected ) {
      MxAssert.assertEquals( "ship_status", aExpected, this.getStatus() );
   }

}
