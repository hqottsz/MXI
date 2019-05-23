
package com.mxi.am.domain.builder;

import java.util.Date;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.RemovalRecord.RemovedInventoryInfo;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


public class RemovalRecordBuilder {

   public static void build( RemovalRecord aRemovalRecord ) {

      Date lRemovalDate = aRemovalRecord.getRemovalDate();
      InventoryKey lMainInv = aRemovalRecord.getInventory();

      // Create an evt_event row.
      EvtEventTable lEventTable = EvtEventTable.create();
      lEventTable.setEventType( RefEventTypeKey.FG );
      lEventTable.setEventStatus( RefEventStatusKey.FGRMVL );
      lEventTable.setHistBool( true );
      lEventTable.setEventDate( lRemovalDate );
      EventKey lEventKey = lEventTable.insert();

      // Create an evt_inv row.
      EventInventoryBuilder lEventInventoryBuilder =
            new EventInventoryBuilder().withEventKey( lEventKey ).withInventoryKey( lMainInv )
                  .isMainInv( true ).withConfigSlotPosKey( aRemovalRecord.getPosition() );
      lEventInventoryBuilder.withAssemblyInvKey( aRemovalRecord.getAssembly() );
      lEventInventoryBuilder.withNHInventoryKey( aRemovalRecord.getParent() );
      lEventInventoryBuilder.withHInventoryKey( aRemovalRecord.getHighest() );
      lEventInventoryBuilder.build();

      // Create an inv_removal row for the main inventory.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_remove_id", new SequentialUuidGenerator().newUuid() );
      lArgs.add( lEventKey, "event_db_id", "event_id" );
      lArgs.add( "event_dt", lRemovalDate );

      lArgs.add( "main_inv_bool", true );
      lArgs.add( lMainInv, "inv_no_db_id", "inv_no_id" );
      lArgs.add( aRemovalRecord.getParent(), "nh_inv_no_db_id", "nh_inv_no_id" );
      lArgs.add( aRemovalRecord.getHighest(), "h_inv_no_db_id", "h_inv_no_id" );
      lArgs.add( aRemovalRecord.getAssembly(), "assmbl_inv_no_db_id", "assmbl_inv_no_id" );

      MxDataAccess.getInstance().executeInsert( "inv_remove", lArgs );

      // Recursively create inv_removal rows for the sub inventory.
      addSubInvToInvRemove( lMainInv, lEventKey, lRemovalDate, aRemovalRecord.getSubInventories() );
   }


   private static void addSubInvToInvRemove( InventoryKey aParentInv, EventKey aRemovalEvent,
         Date aRemovalDate, Set<DomainConfiguration<RemovedInventoryInfo>> aSubInvConfigs ) {

      for ( DomainConfiguration<RemovedInventoryInfo> lSubInvConfig : aSubInvConfigs ) {
         RemovedInventoryInfo lSubInvInfo = new RemovalRecord().new RemovedInventoryInfo();
         lSubInvConfig.configure( lSubInvInfo );

         InventoryKey lSubInvKey = lSubInvInfo.getInventory();

         // Create a row in inv_removal for each of the sub-inventory.
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( "inv_remove_id", new SequentialUuidGenerator().newUuid() );
         lArgs.add( aRemovalEvent, "event_db_id", "event_id" );
         lArgs.add( "event_dt", aRemovalDate );

         lArgs.add( "main_inv_bool", false );
         lArgs.add( lSubInvKey, "inv_no_db_id", "inv_no_id" );
         lArgs.add( aParentInv, "nh_inv_no_db_id", "nh_inv_no_id" );
         lArgs.add( lSubInvInfo.getHighest(), "h_inv_no_db_id", "h_inv_no_id" );
         lArgs.add( lSubInvInfo.getAssembly(), "assmbl_inv_no_db_id", "assmbl_inv_no_id" );

         MxDataAccess.getInstance().executeInsert( "inv_remove", lArgs );

         // Recursively create rows in inv_removal for the sub-inventory's sub-inventory.
         addSubInvToInvRemove( lSubInvKey, aRemovalEvent, aRemovalDate, lSubInvInfo.getSubInvs() );
      }
   }

}
