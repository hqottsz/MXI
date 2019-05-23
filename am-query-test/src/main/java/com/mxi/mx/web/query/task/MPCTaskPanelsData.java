
package com.mxi.mx.web.query.task;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.SchedPanelKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskLabourListKey;
import com.mxi.mx.core.key.TaskPanelKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedPanel;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskLabourList;
import com.mxi.mx.core.table.task.TaskPanelTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * MPC Task panels data
 */
public class MPCTaskPanelsData {

   /**
    * Inserts into sched_stask and evt_event tables
    *
    * @param aTask
    *           The task key
    * @param aTaskDefn
    *           The task definition
    * @param aTaskSubclass
    *           The task subclass key
    * @param aTaskClass
    *           The task class key
    * @param aEventDescription
    *           The event description
    * @param aEventStatus
    *           The event status key
    * @param aRootTask
    *           The root task for the event
    */
   public static void createActualTask( TaskKey aTask, TaskTaskKey aTaskDefn,
         RefTaskSubclassKey aTaskSubclass, RefTaskClassKey aTaskClass, String aEventDescription,
         RefEventStatusKey aEventStatus, TaskKey aRootTask ) {
      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( aTask );
      lSchedStaskTable.setTaskTaskKey( aTaskDefn );
      lSchedStaskTable.setTaskSubclass( aTaskSubclass );
      lSchedStaskTable.setTaskClass( aTaskClass );
      lSchedStaskTable.insert();

      EvtEventTable lEvtEventTable = EvtEventTable.create( aTask.getEventKey() );
      lEvtEventTable.setEventSdesc( aEventDescription );
      lEvtEventTable.setEventStatus( aEventStatus );
      lEvtEventTable.setHEvent( aRootTask.getEventKey() );
      lEvtEventTable.insert();
   }


   /**
    * Inserts into eqp_task_panel table
    *
    * @param aPanel
    *           The panel key
    * @param aPanelCd
    *           The panel code
    * @param aPanelDescription
    *           The panel description
    */
   public static void createPanel( PanelKey aPanel, String aPanelCd, String aPanelDescription ) {
      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( aPanel, "panel_db_id", "panel_id" );
      lInsertArgs.add( "panel_cd", aPanelCd );
      lInsertArgs.add( "desc_sdesc", aPanelDescription );

      MxDataAccess.getInstance().executeInsert( "eqp_task_panel", lInsertArgs );
   }


   /**
    * Inserts into sched_panel table
    *
    * @param aTask
    *           The task key
    * @param aPanel
    *           The panel key
    * @param aOpenMPCTask
    *           The mpc open task key
    * @param aCloseMPCTask
    *           The mpc close task key
    */
   public static void createSchedPanel( TaskKey aTask, PanelKey aPanel, TaskKey aOpenMPCTask,
         TaskKey aCloseMPCTask ) {
      SchedPanel lSchedPanel = SchedPanel.create( new SchedPanelKey( aTask, 1 ) );

      if ( aPanel != null ) {
         lSchedPanel.setPanel( aPanel );
      }

      if ( aOpenMPCTask != null ) {
         lSchedPanel.setOpnMpcSched( new SchedPanelKey( aOpenMPCTask, 1 ) );
      }

      if ( aCloseMPCTask != null ) {
         lSchedPanel.setClsMpcSched( new SchedPanelKey( aCloseMPCTask, 1 ) );
      }

      lSchedPanel.insert();
   }


   /**
    * Inserts into task_task
    *
    * @param aTaskTask
    *           The task definition key
    * @param aTaskClass
    *           The task class key
    */
   public static void createTaskDefinition( TaskTaskKey aTaskTask, RefTaskClassKey aTaskClass ) {
      TaskTaskTable lTaskTaskTable = TaskTaskTable.create( aTaskTask );
      lTaskTaskTable.setTaskDefStatus( RefTaskDefinitionStatusKey.ACTV );
      lTaskTaskTable.setTaskClass( aTaskClass );
      lTaskTaskTable.insert();
   }


   /**
    * Inserts into task_labour_list table
    *
    * @param aTaskTask
    *           The task definition key
    * @param aLabourSkill
    *           The labour skill key
    */
   public static void createTaskLabourList( TaskTaskKey aTaskTask,
         RefLabourSkillKey aLabourSkill ) {
      TaskLabourListKey lTaskLabourList = new TaskLabourListKey( aTaskTask, aLabourSkill );

      TaskLabourList lTaskLabourListTable = TaskLabourList.create( lTaskLabourList );
      lTaskLabourListTable.insert();
   }


   /**
    * Inserts into task_panel table
    *
    * @param aTaskTask
    *           The task definition key
    * @param aPanel
    *           The panel key
    */
   public static void createTaskPanel( TaskTaskKey aTaskTask, PanelKey aPanel ) {
      TaskPanelTable lTaskPanelTable = TaskPanelTable.create( new TaskPanelKey( aTaskTask, 1 ) );
      lTaskPanelTable.setPanel( aPanel );
      lTaskPanelTable.insert();
   }
}
