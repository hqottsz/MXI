package com.mxi.am.domain.reader;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.EventUtils;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;


/**
 * Reader for retrieving Usage Snapshot information.
 *
 */
public class UsageSnapshotReader {

   /**
    *
    * Retrieve the completion usage given a task, an inventory, and a usage parameter.
    *
    * @param aTask
    * @param aInventory
    * @param aDataType
    * @return
    */
   public static UsageSnapshot readUsageAtCompletion( TaskKey aTask, InventoryKey aInventory,
         DataTypeKey aUsageParameter ) {

      EventKey lTaskEventKey = aTask.getEventKey();
      Integer lEventInvId = EventUtils.getEventInvId( lTaskEventKey, aInventory );
      EventInventoryKey lEventInvKey = new EventInventoryKey( lTaskEventKey, lEventInvId );
      EventInventoryUsageKey lEventInvUsageKey =
            new EventInventoryUsageKey( lEventInvKey, aUsageParameter );
      EvtInvUsageTable lEvtInvUsage = EvtInvUsageTable.findByPrimaryKey( lEventInvUsageKey );

      if ( lEvtInvUsage.exists() ) {
         return new UsageSnapshot( aInventory, aUsageParameter, lEvtInvUsage.getTsoQt(),
               lEvtInvUsage.getTsnQt(), lEvtInvUsage.getTsiQt(), lEvtInvUsage.getAssmblTsoQt(),
               lEvtInvUsage.getAssmblTsnQt() );
      }

      return null;
   }

}
