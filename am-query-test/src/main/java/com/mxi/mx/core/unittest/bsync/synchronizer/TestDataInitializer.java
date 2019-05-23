
package com.mxi.mx.core.unittest.bsync.synchronizer;

import org.hamcrest.Matchers;

import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MaintPrgmCarrierMapKey;
import com.mxi.mx.core.key.MaintPrgmCarrierTempTaskKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.MaintPrgmTaskKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefMaintPrgmStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskBlockReqMapKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskFleetApprovalKey;
import com.mxi.mx.core.key.TaskJicReqMapKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartMapKey;
import com.mxi.mx.core.key.TaskTaskDepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInv;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.maint.MaintPrgmCarrierMapTable;
import com.mxi.mx.core.table.maint.MaintPrgmCarrierTempTaskTable;
import com.mxi.mx.core.table.maint.MaintPrgmDefnTable;
import com.mxi.mx.core.table.maint.MaintPrgmTable;
import com.mxi.mx.core.table.maint.MaintPrgmTaskTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskBlockReqMapTable;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskFleetApprovalTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskPartMapTable;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Provides shared data setup methods for this packages' test cases
 *
 * @author jclarkin
 */
public class TestDataInitializer {

   protected static final CarrierKey CARRIER_KEY_1 = new CarrierKey( 6000, 1 );


   /**
    * Binds a task revision to the temp issued task
    *
    * @param aMaintPrgmDefn
    *           the maintenance program definition
    * @param aCarrier
    *           the carrier
    * @param aTaskDefn
    *           the task definition
    * @param aTask
    *           the task
    */
   public static void setupTempIssue( MaintPrgmDefnKey aMaintPrgmDefn, CarrierKey aCarrier,
         TaskDefnKey aTaskDefn, TaskTaskKey aTask ) {
      MaintPrgmCarrierTempTaskTable lTempTask = MaintPrgmCarrierTempTaskTable
            .create( new MaintPrgmCarrierTempTaskKey( aMaintPrgmDefn, aCarrier, aTaskDefn ) );
      lTempTask.setTask( aTask );
      lTempTask.insert();
   }


   /**
    * Clean up all the data in the table used
    */
   protected static void deleteTestData() {
      try {
         MxDataAccess.getInstance().executeDelete( InventoryKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( EventKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( "evt_inv", null );
         MxDataAccess.getInstance().executeDelete( MaintPrgmCarrierTempTaskKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( "TASK_FLEET_APPROVAL", null );
         MxDataAccess.getInstance().executeDelete( TaskTaskKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( TaskDefnKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( TaskKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( MaintPrgmCarrierMapKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( MaintPrgmKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( MaintPrgmDefnKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( MaintPrgmTaskKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( TaskBlockReqMapKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( TaskJicReqMapKey.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( EqpBomPart.TABLE_NAME, null );
         MxDataAccess.getInstance().executeDelete( "TASK_TASK_DEP", null );
         MxDataAccess.getInstance().executeDelete( "TASK_PART_MAP", null );
         MxDataAccess.getInstance().executeDelete( EqpAssmblBom.TABLE_NAME, null );
      } catch ( Exception e ) {
         MxAssert.fail( "Error clearing data." );
      }
   }


   /**
    * Create an acutal task in sched_stask, the event, and the evt inv row assuming that boo
    *
    * @param aTaskKey
    *           DOCUMENT_ME
    * @param aInventory
    *           DOCUMENT_ME
    * @param aTaskTask
    *           DOCUMENT_ME
    * @param aRefEventStatus
    *           DOCUMENT_ME
    */
   protected static void setupActualTask( TaskKey aTaskKey, InventoryKey aInventory,
         TaskTaskKey aTaskTask, RefEventStatusKey aRefEventStatus ) {
      setupActualTask( aTaskKey, aInventory, aTaskTask, aRefEventStatus, false );
   }


   /**
    * Create an actual task in sched_stask, the event event, and an evt inv row
    *
    * @param aTaskKey
    *           the task
    * @param aInventory
    *           the inventory
    * @param aTaskTask
    *           the task definition
    * @param aRefEventStatus
    *           the event status
    * @param aHistBool
    *           DOCUMENT_ME
    */
   protected static void setupActualTask( TaskKey aTaskKey, InventoryKey aInventory,
         TaskTaskKey aTaskTask, RefEventStatusKey aRefEventStatus, boolean aHistBool ) {
      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskTask );
      lSchedStask.setMainInventory( aInventory );
      lSchedStask.insert();

      EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvent.setEventStatus( aRefEventStatus );
      lEvent.setHistBool( aHistBool );
      lEvent.setHEvent( aTaskKey.getEventKey() );
      lEvent.insert();

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( aInventory );

      EvtInv lEvtInv = EvtInv.create( aTaskKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
      lEvtInv.setAssmbl( lInvInv.getBOMItemPosition() );
   }


   /**
    * Create an actual task with a parent task
    *
    * @param aTaskKey
    *           The actual task key
    * @param aParentTaskKey
    *           the actual parent task key
    * @param aInventory
    *           the inventory
    * @param aTaskTask
    *           the task definition
    * @param aRefEventStatus
    *           the task status
    */
   protected static void setupActualTask( TaskKey aTaskKey, TaskKey aParentTaskKey,
         InventoryKey aInventory, TaskTaskKey aTaskTask, RefEventStatusKey aRefEventStatus ) {
      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskTask );
      lSchedStask.setMainInventory( aInventory );
      lSchedStask.insert();

      boolean lHistoric = RefEventStatusKey.CANCEL.equals( aRefEventStatus )
            || RefEventStatusKey.COMPLETE.equals( aRefEventStatus )
            || RefEventStatusKey.TERMINATE.equals( aRefEventStatus );

      EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvent.setEventStatus( aRefEventStatus );
      lEvent.setNhEvent( aParentTaskKey.getEventKey() );
      lEvent.setHistBool( lHistoric );
      lEvent.insert();

      EvtInv lEvtInv = EvtInv.create( aTaskKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
   }


   /**
    * Create an actual task in sched_stask, the event event, and an evt inv row
    *
    * @param aTaskKey
    *           the task
    * @param aInventory
    *           the inventory
    * @param aTaskTask
    *           the task definition
    * @param aBarcodeSdesc
    *           the description of the task
    * @param aRefEventStatus
    *           the event status
    */
   protected static void setupActualTask( TaskKey aTaskKey, InventoryKey aInventory,
         TaskTaskKey aTaskTask, String aBarcodeSdesc, RefEventStatusKey aRefEventStatus ) {
      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskTask );
      lSchedStask.setBarcode( aBarcodeSdesc );
      lSchedStask.setMainInventory( aInventory );
      lSchedStask.insert();

      boolean lHistoric = RefEventStatusKey.CANCEL.equals( aRefEventStatus )
            || RefEventStatusKey.COMPLETE.equals( aRefEventStatus )
            || RefEventStatusKey.TERMINATE.equals( aRefEventStatus );

      EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvent.setEventStatus( aRefEventStatus );
      lEvent.setHEvent( aTaskKey.getEventKey() );
      lEvent.setHistBool( lHistoric );
      lEvent.insert();

      EvtInv lEvtInv = EvtInv.create( aTaskKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
   }


   /**
    * Create an actual task in sched_stask, the event event, and an evt inv row
    *
    * @param aTaskKey
    *           the task
    * @param aInventory
    *           the inventory
    * @param aOrigPartNo
    *           the original part no
    * @param aTaskTask
    *           the task definition
    * @param aBarcodeSdesc
    *           the description of the task
    * @param aRefEventStatus
    *           the event status
    */
   protected static void setupActualTask( TaskKey aTaskKey, InventoryKey aInventory,
         PartNoKey aOrigPartNo, TaskTaskKey aTaskTask, String aBarcodeSdesc,
         RefEventStatusKey aRefEventStatus ) {
      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskTask );
      lSchedStask.setBarcode( aBarcodeSdesc );
      lSchedStask.setOrigPartNo( aOrigPartNo );
      lSchedStask.setMainInventory( aInventory );
      lSchedStask.insert();

      boolean lHistoric = RefEventStatusKey.CANCEL.equals( aRefEventStatus )
            || RefEventStatusKey.COMPLETE.equals( aRefEventStatus )
            || RefEventStatusKey.TERMINATE.equals( aRefEventStatus );

      EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvent.setEventStatus( aRefEventStatus );
      lEvent.setHEvent( aTaskKey.getEventKey() );
      lEvent.setHistBool( lHistoric );
      lEvent.insert();

      EvtInv lEvtInv = EvtInv.create( aTaskKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
   }


   /**
    * Create an actual task with a parent task
    *
    * @param aTaskKey
    *           The actual task key
    * @param aParentTaskKey
    *           the actual parent task key
    * @param aInventory
    *           the inventory
    * @param aTaskTask
    *           the task definition
    * @param aBarcodeSdesc
    *           the description of the task
    * @param aRefEventStatus
    *           the task status
    */
   protected static void setupActualTask( TaskKey aTaskKey, TaskKey aParentTaskKey,
         InventoryKey aInventory, TaskTaskKey aTaskTask, String aBarcodeSdesc,
         RefEventStatusKey aRefEventStatus ) {
      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskTask );
      lSchedStask.setBarcode( aBarcodeSdesc );
      lSchedStask.setMainInventory( aInventory );
      lSchedStask.insert();

      boolean lHistoric = RefEventStatusKey.CANCEL.equals( aRefEventStatus )
            || RefEventStatusKey.COMPLETE.equals( aRefEventStatus )
            || RefEventStatusKey.TERMINATE.equals( aRefEventStatus );

      EvtEventTable lEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvent.setEventStatus( aRefEventStatus );
      lEvent.setHEvent( aParentTaskKey.getEventKey() );
      lEvent.setNhEvent( aParentTaskKey.getEventKey() );
      lEvent.setHistBool( lHistoric );
      lEvent.insert();

      EvtInv lEvtInv = EvtInv.create( aTaskKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
   }


   /**
    * Set up a block req mapping
    *
    * @param aBlock
    *           the block
    * @param aReq
    *           the req
    */
   protected static void setupBlockReqMap( TaskTaskKey aBlock, TaskDefnKey aReq ) {
      TaskBlockReqMapTable lBlockReqMap = TaskBlockReqMapTable.create( aBlock, aReq );
      lBlockReqMap.setIntervalQt( 1 );
      lBlockReqMap.setStartBlockOrd( 1 );
      lBlockReqMap.insert();
   }


   /**
    * Set up a check
    *
    * @param aCheckKey
    *           the check key
    * @param aTaskKey
    *           the actual task key
    * @param aInventory
    *           the inventory
    * @param aCheckStatus
    *           the check status
    */
   protected static void setupCheckTask( TaskKey aCheckKey, TaskKey aTaskKey,
         InventoryKey aInventory, RefEventStatusKey aCheckStatus ) {

      EvtEventTable lEvent = EvtEventTable.create( aCheckKey.getEventKey() );
      lEvent.setEventStatus( aCheckStatus );
      lEvent.setHEvent( aCheckKey.getEventKey() );
      lEvent.setNhEvent( aCheckKey.getEventKey() );
      lEvent.setHistBool( false );

      EventKey lCheckEventKey = lEvent.insert();

      EvtEventTable lTaskEvent = EvtEventTable.findByPrimaryKey( aTaskKey.getEventKey() );
      lTaskEvent.setHEvent( lCheckEventKey );
      lTaskEvent.update();

      EvtInv lEvtInv = EvtInv.create( aCheckKey.getEventKey(), aInventory );
      lEvtInv.setMainInvBool( true );
   }


   /**
    * Create an inventory
    *
    * @param aInventoryKey
    *           inventory key to create
    * @param aBomItemPosition
    *           inventory bom item position
    * @param aInvClass
    *           the inventory class
    */
   protected static void setupInventory( InventoryKey aInventoryKey,
         ConfigSlotPositionKey aBomItemPosition, RefInvClassKey aInvClass ) {
      setupInventory( aInventoryKey, aBomItemPosition, null, aInvClass, RefBOMClassKey.ROOT );
   }


   /**
    * Create an inventory
    *
    * @param aInventoryKey
    *           inventory key to create
    * @param aBomItemPosition
    *           inventory bom item position
    * @param aPartNoKey
    *           inventory part number
    * @param aInvClass
    *           the inventory class
    * @param aConfigClass
    *           the config slot class
    */
   protected static void setupInventory( InventoryKey aInventoryKey,
         ConfigSlotPositionKey aBomItemPosition, PartNoKey aPartNoKey, RefInvClassKey aInvClass,
         RefBOMClassKey aConfigClass ) {

      // Create a single Inventory at the location
      InvInvTable lInventory = InvInvTable.create( aInventoryKey );
      lInventory.setInvCond( RefInvCondKey.RFI );
      lInventory.setInvClass( aInvClass );
      lInventory.setBomItemPosition( aBomItemPosition );
      lInventory.setApplEffCd( null );
      lInventory.setCarrier( CARRIER_KEY_1 );
      lInventory.setApplEffCd( null );
      lInventory.setNhInvNo( null );
      lInventory.setHInvNo( aInventoryKey );
      lInventory.setPartNo( aPartNoKey );
      if ( Matchers.isOneOf( RefInvClassKey.ACFT, RefInvClassKey.ASSY ).matches( aInvClass ) ) {
         lInventory.setAssmblInvNo( aInventoryKey );
      }

      lInventory.setOrigAssmbl( aBomItemPosition.getAssemblyKey() );
      lInventory.insert();

      EqpBomPart lEqpBomPart = EqpBomPart.create();
      lEqpBomPart.setApplEffLdesc( null );
      lEqpBomPart.setBomItem( aBomItemPosition.getBomItemKey() );
      lEqpBomPart.setBomPartCd( aBomItemPosition.getCd() + "_" + aInventoryKey.getDbId() + "_"
            + aInventoryKey.getId() );
      lEqpBomPart.insert( new PartGroupKey( aInventoryKey.getDbId(), aInventoryKey.getId() ) );

      if ( !EqpAssmblBom.findByPrimaryKey( aBomItemPosition.getBomItemKey() ).exists() ) {
         EqpAssmblBom lEqpAssmblBom = EqpAssmblBom.create( aBomItemPosition.getBomItemKey() );
         lEqpAssmblBom.setConfigClass( aConfigClass );
         lEqpAssmblBom.insert();
      }
   }


   /**
    * Set up a JIC Req map
    *
    * @param aJic
    *           the jic
    * @param aReq
    *           the req
    */
   protected static void setupJicReqMap( TaskTaskKey aJic, TaskDefnKey aReq ) {
      TaskJicReqMapTable lJicReqMap = TaskJicReqMapTable.create( aJic, aReq );
      lJicReqMap.insert();
   }


   /**
    * Set up a JIC Req map
    *
    * @param aJIC
    *           the jic
    * @param aReq
    *           the req
    */
   protected static void setupJICReqMap( TaskTaskKey aJIC, TaskDefnKey aReq ) {
      TaskJicReqMapTable lJICReqMap = TaskJicReqMapTable.create( aJIC, aReq );
      lJICReqMap.insert();
   }


   /**
    * Creates a maintenance program
    *
    * @param aMaintPrgmKey
    *           the primary key
    * @param aMaintPrgmDefnKey
    *           the maint defintition
    * @param aRevisionOrd
    *           the revision
    * @param aRefMaintPrgmStatusKey
    *           the status
    */
   protected static void setupMaintPrgm( MaintPrgmKey aMaintPrgmKey,
         MaintPrgmDefnKey aMaintPrgmDefnKey, int aRevisionOrd,
         RefMaintPrgmStatusKey aRefMaintPrgmStatusKey ) {

      MaintPrgmTable lMaintPrgm = MaintPrgmTable.create( aMaintPrgmKey );
      lMaintPrgm.setMaintPrgmDefn( aMaintPrgmDefnKey );
      lMaintPrgm.setRevisionOrd( aRevisionOrd );
      lMaintPrgm.setMaintPrgmStatus( aRefMaintPrgmStatusKey );
      lMaintPrgm.insert();
   }


   /**
    * Create a MP carrier map
    *
    * @param aMaintPrgmCarrierMapKey
    *           primary key
    * @param aLatestRevision
    *           lastest revision bool
    * @param aCarrierRevision
    *           the carrier revision
    */
   protected static void setupMaintPrgmCarriermap( MaintPrgmCarrierMapKey aMaintPrgmCarrierMapKey,
         Boolean aLatestRevision, int aCarrierRevision ) {
      MaintPrgmCarrierMapTable lMaintCarrier =
            MaintPrgmCarrierMapTable.create( aMaintPrgmCarrierMapKey );
      lMaintCarrier.setLatestRevisionBool( aLatestRevision );
      lMaintCarrier.setCarrierRevisionOrd( aCarrierRevision );
      lMaintCarrier.insert();
   }


   /**
    * Creates a maintenance program definition
    *
    * @param aMaintPrgmDefnKey
    *           mp defn key
    * @param aAssembly
    *           the assembly of the mp defn
    * @param aLastRevisionOrd
    *           the last revision ord
    */
   protected static void setupMaintPrgmDefn( MaintPrgmDefnKey aMaintPrgmDefnKey,
         AssemblyKey aAssembly, int aLastRevisionOrd ) {
      MaintPrgmDefnTable lMaintPrgmDefn = MaintPrgmDefnTable.create( aMaintPrgmDefnKey );
      lMaintPrgmDefn.setAssembly( aAssembly );
      lMaintPrgmDefn.setLastRevisionOrd( aLastRevisionOrd );
      lMaintPrgmDefn.insert();
   }


   /**
    * Create a maintenance program task
    *
    * @param aMaintPrgmTaskKey
    *           the primary key
    * @param aTaskTaskKey
    *           the task definition
    */
   protected static void setupMaintPrgmTask( MaintPrgmTaskKey aMaintPrgmTaskKey,
         TaskTaskKey aTaskTaskKey ) {
      MaintPrgmTaskTable lMaintTask = MaintPrgmTaskTable.create( aMaintPrgmTaskKey );
      lMaintTask.setTask( aTaskTaskKey );
      lMaintTask.insert();
   }


   /**
    * Create a maintenance program task
    *
    * @param aMaintPrgmTaskKey
    *           the primary key
    * @param aTaskTaskKey
    *           the task definition
    * @param aUnassign
    *           the unassign bool
    */
   protected static void setupMaintPrgmTask( MaintPrgmTaskKey aMaintPrgmTaskKey,
         TaskTaskKey aTaskTaskKey, boolean aUnassign ) {
      MaintPrgmTaskTable lMaintTask = MaintPrgmTaskTable.create( aMaintPrgmTaskKey );
      lMaintTask.setTask( aTaskTaskKey );
      lMaintTask.setUnassignBool( aUnassign );
      lMaintTask.insert();
   }


   /**
    * Create a task defn
    *
    * @param aTaskDefn
    *           the task defn to create
    */
   protected static void setupTaskDefn( TaskDefnKey aTaskDefn ) {
      setupTaskDefn( aTaskDefn, 1 );
   }


   /**
    * Create a task defn
    *
    * @param aTaskDefn
    *           the task defn to create
    * @param aLastRevisionOrd
    *           the most recent revision
    */
   protected static void setupTaskDefn( TaskDefnKey aTaskDefn, Integer aLastRevisionOrd ) {
      TaskDefnTable lTaskDefn = TaskDefnTable.create( aTaskDefn );
      lTaskDefn.setLastRevisionOrd( aLastRevisionOrd );
      lTaskDefn.insert();
   }


   /**
    * Inserts a task defintition dependency
    *
    * @param aTaskTaskDepKey
    *           The TaskTaskDep key
    * @param aDepTaskDefnKey
    *           the dependent task definition
    * @param aTaskDepActionKey
    *           the dependent action
    */
   protected static void setupTaskDep( TaskTaskDepKey aTaskTaskDepKey, TaskDefnKey aDepTaskDefnKey,
         RefTaskDepActionKey aTaskDepActionKey ) {
      TaskTaskDepTable lTaskTaskDepTable = TaskTaskDepTable.create( aTaskTaskDepKey );
      lTaskTaskDepTable.setDepTaskDefn( aDepTaskDefnKey );
      lTaskTaskDepTable.setTaskDepAction( aTaskDepActionKey );
      lTaskTaskDepTable.insert();
   }


   /**
    * Create a task definition revision
    *
    * @param aTaskTaskKey
    *           block to create
    * @param aPartNoKey
    *           bom item position
    * @param aRevision
    *           revision
    * @param aTaskDefn
    *           the task defn
    * @param aStatus
    *           the status of the task_task
    * @param aTaskClassKey
    *           the task class
    */
   protected static void setupTaskTask( TaskTaskKey aTaskTaskKey, PartNoKey aPartNoKey,
         int aRevision, TaskDefnKey aTaskDefn, RefTaskDefinitionStatusKey aStatus,
         RefTaskClassKey aTaskClassKey ) {
      setupTaskTask( aTaskTaskKey, null, aPartNoKey, aRevision, aTaskDefn, aStatus, aTaskClassKey );
   }


   /**
    * Create a task definition revision
    *
    * @param aTaskTaskKey
    *           block to create
    * @param aConfigSlotPositionKey
    *           bom item position
    * @param aRevision
    *           revision
    * @param aTaskDefn
    *           the task defn
    * @param aStatus
    *           the status of the task_task
    * @param aTaskClassKey
    *           the task class
    */
   protected static void setupTaskTask( TaskTaskKey aTaskTaskKey,
         ConfigSlotPositionKey aConfigSlotPositionKey, int aRevision, TaskDefnKey aTaskDefn,
         RefTaskDefinitionStatusKey aStatus, RefTaskClassKey aTaskClassKey ) {
      setupTaskTask( aTaskTaskKey, aConfigSlotPositionKey.getBomItemKey(), null, aRevision,
            aTaskDefn, aStatus, aTaskClassKey );
   }


   /**
    * Create a task definition revision
    *
    * @param aTaskTaskKey
    *           block to create
    * @param aConfigSlotKey
    *           bom item position
    * @param aPartNoKey
    *           part no key
    * @param aRevision
    *           revision
    * @param aTaskDefn
    *           the task defn
    * @param aStatus
    *           the status of the task_task
    * @param aTaskClassKey
    *           the task class
    */
   protected static void setupTaskTask( TaskTaskKey aTaskTaskKey, ConfigSlotKey aConfigSlotKey,
         PartNoKey aPartNoKey, int aRevision, TaskDefnKey aTaskDefn,
         RefTaskDefinitionStatusKey aStatus, RefTaskClassKey aTaskClassKey ) {
      TaskTaskTable lTaskTask = TaskTaskTable.create();
      lTaskTask.setBomItem( aConfigSlotKey );
      lTaskTask.setRevisionOrd( aRevision );
      lTaskTask.setTaskClass( aTaskClassKey );
      lTaskTask.setTaskDefn( aTaskDefn );
      lTaskTask.setTaskDefStatus( aStatus );
      lTaskTask.setTaskApplEffLdesc( null );
      lTaskTask.setTaskApplSqlLdesc( null );
      lTaskTask.setOnConditionBool( false );
      lTaskTask.setUniqueBool( aTaskClassKey.equals( RefTaskClassKey.REQ ) );
      lTaskTask.insert( aTaskTaskKey );

      if ( aPartNoKey != null ) {
         TaskPartMapKey lMapKey = new TaskPartMapKey( aTaskTaskKey, aPartNoKey );
         TaskPartMapTable lTaskPartMap = TaskPartMapTable.create( lMapKey );
         lTaskPartMap.insert();
      }

      if ( RefTaskDefinitionStatusKey.ACTV.equals( aStatus )
            || RefTaskDefinitionStatusKey.OBSOLETE.equals( aStatus ) ) {
         TaskFleetApprovalKey lApprovalKey = new TaskFleetApprovalKey( aTaskDefn );
         TaskFleetApprovalTable lApprovalTable = TaskFleetApprovalTable.create( lApprovalKey );
         lApprovalTable.setTaskRevision( aTaskTaskKey );
         lApprovalTable.insert();
      }
   }
}
