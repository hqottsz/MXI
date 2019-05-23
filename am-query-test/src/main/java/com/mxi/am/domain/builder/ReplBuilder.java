
package com.mxi.am.domain.builder;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Repl;
import com.mxi.am.domain.Repl.UsageDeadline;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Builds a REPL task
 */
public final class ReplBuilder {

   public static TaskKey build( Repl aRepl ) {
      TaskBuilder lTaskBuilder = new TaskBuilder();

      InventoryKey lMainInventory = aRepl.getMainInventory();
      if ( lMainInventory == null ) {
         aRepl.setMainInventory( Domain.createAircraft() );
      }
      lTaskBuilder.onInventory( aRepl.getMainInventory() );

      lTaskBuilder.withTaskClass( RefTaskClassKey.REPL );
      lTaskBuilder.withComponentWorkPackage( aRepl.getComponentWorkPackage() );
      for ( UsageDeadline lUsage : aRepl.getUsageDeadline() ) {
         lTaskBuilder.withUsageDeadline( lUsage.getType(), lUsage.getDueQuantity(),
               lUsage.getDueDate() );
      }
      return lTaskBuilder.build();
   }

}
