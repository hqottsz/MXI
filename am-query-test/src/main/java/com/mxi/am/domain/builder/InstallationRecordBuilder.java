
package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefRelationTypeKey.TTFG;

import java.util.Date;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.InstallationRecord.InstalledInventoryInfo;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.DataAccessObject;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.EventUtils;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


public class InstallationRecordBuilder {

   public static void build( InstallationRecord aInstallationRecord ) {

      Date lInstallDate = aInstallationRecord.getInstallationDate();
      InventoryKey lMainInv = aInstallationRecord.getInventory();

      // Create an evt_event row.
      EvtEventTable lEventTable = EvtEventTable.create();
      lEventTable.setEventType( RefEventTypeKey.FG );
      lEventTable.setEventStatus( RefEventStatusKey.FGINST );
      lEventTable.setHistBool( true );
      lEventTable.setEventDate( lInstallDate );
      EventKey lEventKey = lEventTable.insert();

      // Create an evt_inv row.
      EventInventoryBuilder lEventInventoryBuilder = new EventInventoryBuilder()
            .withEventKey( lEventKey ).withInventoryKey( lMainInv ).isMainInv( true );
      lEventInventoryBuilder.withAssemblyInvKey( aInstallationRecord.getAssembly() );
      lEventInventoryBuilder.withNHInventoryKey( aInstallationRecord.getParent() );
      lEventInventoryBuilder.withHInventoryKey( aInstallationRecord.getHighest() );
      lEventInventoryBuilder.build();

      // Create an evt_event_rel row if this installation is related to a task.
      TaskKey lTaskKey = aInstallationRecord.getTask();
      if ( lTaskKey != null ) {
         EventKey lTaskEvent = lTaskKey.getEventKey();
         EvtEventRel.create( lTaskEvent, lEventKey, TTFG );
      }

      // Create an inv_install row for the main inventory.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_install_id", new SequentialUuidGenerator().newUuid() );
      lArgs.add( lEventKey, "event_db_id", "event_id" );
      lArgs.add( "event_dt", lInstallDate );

      lArgs.add( "main_inv_bool", true );
      lArgs.add( lMainInv, "inv_no_db_id", "inv_no_id" );
      lArgs.add( aInstallationRecord.getParent(), "nh_inv_no_db_id", "nh_inv_no_id" );
      lArgs.add( aInstallationRecord.getHighest(), "h_inv_no_db_id", "h_inv_no_id" );
      lArgs.add( aInstallationRecord.getAssembly(), "assmbl_inv_no_db_id", "assmbl_inv_no_id" );

      MxDataAccess.getInstance().executeInsert( "inv_install", lArgs );

      // Recursively create inv_install rows for the sub inventory.
      addSubInvToInvInstall( lMainInv, lEventKey, lInstallDate,
            aInstallationRecord.getSubInventories() );

      addUsageSnapshots( lEventKey, aInstallationRecord.getUsageSnapshots() );

   }


   private static void addSubInvToInvInstall( InventoryKey aParentInv, EventKey aInstallEvent,
         Date aInstallDate, List<DomainConfiguration<InstalledInventoryInfo>> aSubInvConfigs ) {

      for ( DomainConfiguration<InstalledInventoryInfo> lSubInvConfig : aSubInvConfigs ) {
         InstalledInventoryInfo lSubInvInfo = new InstallationRecord().new InstalledInventoryInfo();
         lSubInvConfig.configure( lSubInvInfo );

         InventoryKey lSubInvKey = lSubInvInfo.getInventory();

         // Create a row in inv_install for each of the sub-inventory.
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( "inv_install_id", new SequentialUuidGenerator().newUuid() );
         lArgs.add( aInstallEvent, "event_db_id", "event_id" );
         lArgs.add( "event_dt", aInstallDate );
         lArgs.add( "main_inv_bool", false );
         lArgs.add( lSubInvKey, "inv_no_db_id", "inv_no_id" );
         lArgs.add( aParentInv, "nh_inv_no_db_id", "nh_inv_no_id" );
         lArgs.add( lSubInvInfo.getHighest(), "h_inv_no_db_id", "h_inv_no_id" );
         lArgs.add( lSubInvInfo.getAssembly(), "assmbl_inv_no_db_id", "assmbl_inv_no_id" );

         MxDataAccess.getInstance().executeInsert( "inv_install", lArgs );

         // Recursively create rows in inv_install for the sub-inventory's sub-inventory.
         addSubInvToInvInstall( lSubInvKey, aInstallEvent, aInstallDate, lSubInvInfo.getSubInvs() );
      }
   }


   private static void addUsageSnapshots( EventKey aEvent, List<UsageSnapshot> aUsageSnapshots ) {

      DataAccessObject lDao = MxDataAccess.getInstance();
      DataSetArgument lArg;

      for ( UsageSnapshot lUsage : aUsageSnapshots ) {
         lArg = new DataSetArgument();

         lArg.add( aEvent, "aEventDbId", "aEventId" );
         lArg.add( "aEventInvId", EventUtils.getEventInvId( aEvent, lUsage.getInventory() ) );
         lArg.add( lUsage.getDataType(), "aDataTypeDbId", "aDataTypeId" );

         lArg.add( "aTsnQt", lUsage.getTSN() );
         lArg.add( "aTsoQt", lUsage.getTSO() );
         lArg.add( "aTsiQt", lUsage.getTSI() );
         lArg.add( "aAssmblTsnQt", lUsage.getAssemblyTSN() );
         lArg.add( "aAssmblTsoQt", lUsage.getAssemblyTSO() );
         lArg.add( "aHighestTsnQt", lUsage.getHighestTSN() );
         lArg.add( "aHighestTsoQt", lUsage.getHighestTSO() );
         lArg.add( "aNextHighestTsnQt", lUsage.getNextHighestTSN() );
         lArg.add( "aNextHighestTsoQt", lUsage.getNextHighestTSO() );
         lArg.add( lUsage.getUsageSnapshotSourceType(), "aUsageSourceDbId", "aUsageSourceCd" );

         if ( lDao.executeUpdate( "com.mxi.mx.core.query.table.evt.addUsageSnapshot",
               lArg ) != 1 ) {
            throw new RuntimeException( "Unable to create usage snapshot: " + lArg );
         }
      }

   }

}
