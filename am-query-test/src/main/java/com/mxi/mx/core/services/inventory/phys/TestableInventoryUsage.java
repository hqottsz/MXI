
package com.mxi.mx.core.services.inventory.phys;

import com.mxi.mx.core.key.UsageParmKey;


/**
 * This clase makes InventoryUsage testeable
 *
 * @author gpichetto
 */
public class TestableInventoryUsage extends InventoryUsage {

   /** Use serialVersionUID for interoperability. */
   private static final long serialVersionUID = 5583979931256166298L;


   /**
    * Creates a new {@linkplain TestableInventoryUsage} object.
    *
    * @param aUsageParmKey
    *           usage parameter key
    */
   public TestableInventoryUsage(UsageParmKey aUsageParmKey) {
      super( aUsageParmKey );
   }


   @Override
   public UsageParmKey getUsageParmKey() {

      return iUsageParmKey;
   }
}
