
package com.mxi.mx.core.unittest.table.eqp;

import com.mxi.mx.core.key.EqpPartVendorPriceKey;
import com.mxi.mx.core.table.eqp.EqpPartVendorPriceTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>eqp_part_vendor_price</code> table.
 *
 * @author $Author: emcafee $
 * @version $Revision: 1.1 $
 */
public class EqpPartVendorPrice extends EqpPartVendorPriceTable {

   /**
    * Creates a new EqpPartVendorPrice object.
    *
    * @param aPartVendorPriceKey
    *           DOCUMENT ME!
    */
   public EqpPartVendorPrice(EqpPartVendorPriceKey aPartVendorPriceKey) {

      super( aPartVendorPriceKey );
   }


   /**
    * Asserts that the hist bool is as specified
    *
    * @param aHistBool
    *           preferred boolean
    */
   public void assertHistBool( boolean aHistBool ) {

      // Check if the expected value matches the actual value
      MxAssert.assertEquals( "hist_bool", aHistBool, getHistBool() );
   }

}
