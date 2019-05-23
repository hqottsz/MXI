package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventTypeKey.LX;

import com.mxi.am.domain.Transfer;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvXferTable;


public class TransferBuilder {

   static EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );


   public static TransferKey build( Transfer transfer ) {

      final EventKey eventKey = evtEventDao.generatePrimaryKey();
      final TransferKey transferKey = new TransferKey( eventKey );
      final InventoryKey inventory = transfer.getInventory();

      EvtEventTable evtEventRow = evtEventDao.create( eventKey );
      evtEventRow.setEventType( LX );
      evtEventRow.setEventStatus( transfer.getStatus() );
      evtEventDao.insert( evtEventRow );

      EvtInvTable evtInvRow = EvtInvTable.create( eventKey );
      evtInvRow.setInventoryKey( inventory );
      evtInvRow.setMainInvBool( true );
      evtInvRow.insert();

      InvXferTable invXferRow = InvXferTable.create( transferKey );
      invXferRow.setInventory( transfer.getInventory() );
      invXferRow.setXferQt( transfer.getQuantity() );
      invXferRow.setTransferType( transfer.getType() );
      if ( inventory != null ) {
         // The trigger TIBR_UOM_INVXFER requires that when an inventory is provided that the
         // inventory have a part number and that part number have an associated quantity unit.
         ensureInventoryHasPartWithQuantityUnit( inventory );
      }

      invXferRow.insert();

      // Note; the order of insert is important (must be "from" then "to").
      EvtLocTable fromLocRow = EvtLocTable.create( eventKey );
      fromLocRow.setLocation( transfer.getFromLocation() );
      fromLocRow.insert();
      EvtLocTable toLocRow = EvtLocTable.create( eventKey );
      toLocRow.setLocation( transfer.getToLocation() );
      toLocRow.insert();

      return transferKey;
   }


   private static void ensureInventoryHasPartWithQuantityUnit( InventoryKey inventory ) {
      InvInvTable invInvRow = InvInvTable.findByPrimaryKey( inventory );
      PartNoKey partNumber = invInvRow.getPartNo();
      if ( partNumber == null ) {
         EqpPartNoTable partNoRow = EqpPartNoTable.create( EqpPartNoTable.generatePartNoPK() );
         partNoRow.setQtyUnit( RefQtyUnitKey.EA );
         partNumber = partNoRow.insert();
         invInvRow.setPartNo( partNumber );
         invInvRow.update();
      } else {
         EqpPartNoTable partNoRow = EqpPartNoTable.findByPrimaryKey( partNumber );
         if ( partNoRow.getQtyUnit() == null ) {
            partNoRow.setQtyUnit( RefQtyUnitKey.EA );
            partNoRow.update();
         }
      }

   }

}
