
package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.Arrays;

import com.mxi.am.domain.RepairOrder;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Builds a {@linkplain RepairOrder} object
 */
public final class RepairOrderBuilder {

   public static TaskKey build( RepairOrder aRepairOrder ) {

      InventoryKey lMainInventory = aRepairOrder.getMainInventory();
      if ( lMainInventory == null ) {
         lMainInventory = new InventoryBuilder().build();
      }

      RefEventStatusKey lStatus =
            ( RefEventStatusKey ) defaultIfNull( aRepairOrder.getStatus(), ACTV );

      EvtEventDao iEvtEventDao = new JdbcEvtEventDao();
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();
      TaskKey lTaskKey = new TaskKey( lEventKey.getDbId(), lEventKey.getId() );

      // Insert the sched_stask row prior to the evt_event row in order for
      // TIUAR_EVT_EVENT_HIST_BOOL to be triggered.
      SchedStaskDao iSchedStaskDao = new JdbcSchedStaskDao();
      SchedStaskTable lSchedStaskTable = iSchedStaskDao.create( lTaskKey );
      lSchedStaskTable.setTaskClass( RefTaskClassKey.RO );
      lSchedStaskTable.setMainInventory( lMainInventory );
      iSchedStaskDao.insert( lSchedStaskTable );

      EvtEventTable lEvtEventTable = iEvtEventDao.create( lEventKey );
      lEvtEventTable.setHEvent( lEvtEventTable.getPk() );
      lEvtEventTable.setEventType( RefEventTypeKey.TS );
      lEvtEventTable.setEventStatus( lStatus );
      lEvtEventTable.setHistBool( isHistorical( lStatus ) );
      lEvtEventTable.setActualStartDate( aRepairOrder.getActualStartDate() );
      lEvtEventTable.setEventDate( aRepairOrder.getActualEndDate() );
      iEvtEventDao.insert( lEvtEventTable );

      InvInvTable lInv = InvInvTable.findByPrimaryKey( lMainInventory );

      EvtInvTable lEvtInv = EvtInvTable.create( lEventKey );
      lEvtInv.setInventoryKey( lMainInventory );
      lEvtInv.setMainInvBool( true );
      lEvtInv.setHInventoryKey( lInv.getHInvNo() );
      lEvtInv.setAssmblInventoryKey( lInv.getAssmblInvNo() );
      lEvtInv.setNHInventoryKey( lInv.getNhInvNo() );
      lEvtInv.setAssemblyKey( lInv.getOrigAssmbl() );
      lEvtInv.insert();

      LocationKey lLocation = aRepairOrder.getLocationKey();
      if ( lLocation != null ) {
         EvtLocTable lEvtLoc = EvtLocTable.create( lEventKey );
         lEvtLoc.setLocation( lLocation );
         lEvtLoc.insert();
      }
      return lTaskKey;
   }


   private static final boolean isHistorical( RefEventStatusKey aStatus ) {
      return Arrays.asList( COMPLETE, CANCEL ).contains( aStatus );

   }

}
