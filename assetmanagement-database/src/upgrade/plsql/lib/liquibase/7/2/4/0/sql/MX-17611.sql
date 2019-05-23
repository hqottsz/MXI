--liquibase formatted sql


--changeSet MX-17611:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_WORK_ITEM_TYPE" (
	"NAME" Varchar2 (40) NOT NULL DEFERRABLE ,
	"WORKER_CLASS" Varchar2 (100),
	"WORK_MANAGER" Varchar2 (80),
	"ENABLED" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ENABLED IN (0, 1) ) DEFERRABLE ,
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
 Constraint "PK_UTL_WORK_ITEM_TYPE" primary key ("NAME") 
) 
');
END;
/

--changeSet MX-17611:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_WORK_ITEM" (
	"ID" Number(10,0) NOT NULL DEFERRABLE  Check (ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TYPE" Varchar2 (40) NOT NULL DEFERRABLE ,
	"KEY" Varchar2 (40),
	"DATA" Varchar2 (2000),
	"SERVER_ID" Varchar2 (20),
	"SCHEDULED_DATE" Timestamp(6),
	"START_DATE" Timestamp(6),
	"END_DATE" Timestamp(6),
	"ERROR_MSG" Clob,
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
 Constraint "PK_UTL_WORK_ITEM" primary key ("ID") 
) 
');
END;
/

--changeSet MX-17611:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_WORK_ITEM_TYPE" add Constraint "FK_MIMDB_UTLWORKITEMTYPE" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-17611:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_WORK_ITEM" add Constraint "FK_MIMDB_UTLWORKITEM" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-17611:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_WORK_ITEM" add Constraint "FK_UTLWORKITEMTYPE_UTLWORKITEM" foreign key ("TYPE") references "UTL_WORK_ITEM_TYPE" ("NAME")  DEFERRABLE
');
END;
/

--changeSet MX-17611:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
     PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'COMPLETED_WORK_ITEM_TTL', '1', 'WORK_MANAGER', 0, 'This parameter is used to set the time-to-live in hours for completed Work Items in the UTL_WORK_ITEM table.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 1 , 1, 'Work Manager Parameter', '6.8.12', 0  
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'COMPLETED_WORK_ITEM_TTL' ); 

--changeSet MX-17611:7 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'UTL_WORK_ITEM_ID', 1, 'UTL_WORK_ITEM', 'ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'UTL_WORK_ITEM_ID');   

--changeSet MX-17611:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('UTL_WORK_ITEM_ID', 1);   
END;
/   

--changeSet MX-17611:9 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT 'MX_COMMON_EXPIRED_WORK_ITEM_CLEANER', 'Removes expired entries from the UTL_WORK_ITEM table, as defined by the COMPLETED_WORK_ITEM_TTL config parm.', null, 30, 3600, 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_job WHERE utl_job.job_cd = 'MX_COMMON_EXPIRED_WORK_ITEM_CLEANER');

--changeSet MX-17611:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_AC_REG', 'RECALC_PRED_DEAD_DT_BOOL');
END;
/

--changeSet MX-17611:11 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
     PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'REALTIME_ACFTDEADLINE_WORKITEM_DELAY', '10', 'WORK_MANAGER', 0, 'The minutes the work item will wait before execution. Used to eliminate excessive processing for the same aircrafts.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 10, 1, 'Work Manager Parameter', '6.8.12', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'REALTIME_ACFTDEADLINE_WORKITEM_DELAY' );       

--changeSet MX-17611:12 stripComments:false
-- Delete the types assigned to the roles
/** 
	Migration script for removed 0-level alerts
*/
DELETE FROM utl_alert_type_role t WHERE t.alert_type_id IN (41, 42);

--changeSet MX-17611:13 stripComments:false
-- Delete the alerts assigned to the users
DELETE FROM utl_user_alert t WHERE t.alert_id IN (SELECT utl_alert.alert_id FROM utl_alert WHERE utl_alert.alert_type_id IN (41, 42));

--changeSet MX-17611:14 stripComments:false
-- Delete the alert parms
DELETE FROM utl_alert_parm t WHERE t.alert_id IN (SELECT utl_alert.alert_id FROM utl_alert WHERE utl_alert.alert_type_id IN (41, 42));

--changeSet MX-17611:15 stripComments:false
-- Delete the alert log
DELETE FROM utl_alert_log t WHERE t.alert_id IN (SELECT utl_alert.alert_id FROM utl_alert WHERE utl_alert.alert_type_id IN (41, 42));

--changeSet MX-17611:16 stripComments:false
-- Delete the alert status log
DELETE FROM utl_alert_status_log t WHERE t.alert_id IN (SELECT utl_alert.alert_id FROM utl_alert WHERE utl_alert.alert_type_id IN (41, 42));

--changeSet MX-17611:17 stripComments:false
-- Delete the alerts
DELETE FROM utl_alert t WHERE t.alert_type_id IN (41, 42);

--changeSet MX-17611:18 stripComments:false
-- Delete the types
DELETE FROM utl_alert_type t WHERE t.alert_type_id IN (41, 42);      

--changeSet MX-17611:19 stripComments:false
DELETE FROM utl_job WHERE job_cd = 'MX_CORE_UPDATE_PREDICTED_DEADLINE_DATE';

--changeSet MX-17611:20 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'Job_Aircraft_Deadline_Update', 'com.mxi.mx.core.worker.updatedeadline.AircraftDeadlineWorker',  'wm/Maintenix-AircraftDeadlinesJobWorkManager',  1, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Job_Aircraft_Deadline_Update' ); 

--changeSet MX-17611:21 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'Real_Time_Aircraft_Deadline_Update',    'com.mxi.mx.core.worker.updatedeadline.AircraftDeadlineWorker',  'wm/Maintenix-AircraftDeadlinesRealTimeWorkManager',     1, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Real_Time_Aircraft_Deadline_Update' );       

--changeSet MX-17611:22 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'Job_Inventory_Deadline_Update', 'com.mxi.mx.core.worker.updatedeadline.InventoryDeadlineWorker', 'wm/Maintenix-InventoryDeadlinesJobWorkManager', 1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Job_Inventory_Deadline_Update' );       

--changeSet MX-17611:23 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'Real_Time_Inventory_Deadline_Update',    'com.mxi.mx.core.worker.updatedeadline.InventoryDeadlineWorker', 'wm/Maintenix-InventoryDeadlinesRealTimeWorkManager',    1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Real_Time_Inventory_Deadline_Update' );       

--changeSet MX-17611:24 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'INVENTORY_SYNC',    'com.mxi.mx.core.worker.bsync.TaskSynchronizationWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'INVENTORY_SYNC' );       

--changeSet MX-17611:25 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'UPDATE_TASK',    'com.mxi.mx.core.worker.bsync.UpdateTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'UPDATE_TASK' );       

--changeSet MX-17611:26 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'CANCEL_TASK',    'com.mxi.mx.core.worker.bsync.CancelTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'CANCEL_TASK' );       

--changeSet MX-17611:27 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'INITIALIZE_TASK',    'com.mxi.mx.core.worker.bsync.InitializeTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'INITIALIZE_TASK' );       

--changeSet MX-17611:28 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'ZIP_QUEUE',    'com.mxi.mx.core.worker.bsync.ZipQueueWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'ZIP_QUEUE' );             

--changeSet MX-17611:29 stripComments:false
INSERT INTO
   utl_work_item_type
   (
     name, worker_class, work_manager, enabled, utl_id
   )
   SELECT 'ZIP_TASK',    'com.mxi.mx.core.worker.bsync.ZipTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',    1, 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'ZIP_TASK' ); 

--changeSet MX-17611:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY EVENT_PKG
IS


/********************************************************************************
*
* Procedure:      UpdateDrivingDeadline
* Arguments:      an_SchedStaskDbId (IN NUMBER): Event in a tree that will have
*                                 it's driving deadline udpated
*                          an_SchedStaskId(IN NUMBER): ""
*                          ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   This procedure is used to update the driving deadline
*                for a check or work order.  The task within the check that has
*                the nearest upcoming date is considered to the the driving
*                deadline.  The driving deadline relationship will be captured in the
*                EVT_EVENT_REL table.
*
* Orig.Coder:     slr
* Recent Coder:   Michal Bajer
* Recent Date:    August 24, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDrivingDeadline(
          an_SchedStaskDbId          IN  typn_Id,
          an_SchedStaskId            IN  typn_Id,
          on_Return                  OUT NUMBER
   )IS

   /* local variables */
   ln_RootEventDbId              evt_event.event_db_id%TYPE;
   ln_RootEventId                evt_event.event_id%TYPE;
   ln_DrivingEventDbId           evt_event.event_db_id%TYPE;
   ln_DrivingEventId             evt_event.event_id%TYPE;
   ln_EvtEventRelId           NUMBER;
   ln_RelEventOrd             NUMBER;
   ln_DrivingCount            NUMBER;
   ln_CountVar                NUMBER;

   /* cursor declarations */

   /*
      If the parent task is based on a task definition with last_sched_dead_bool set to true
      we need to sort in opposite order.
   */
   CURSOR lcur_Deadline (
         cl_RootEventDbId   typn_Id,
         cl_RootEventId     typn_Id
      ) IS
      SELECT
         evt_event.event_db_id as event_db_id,
         evt_event.event_id as event_id,
         evt_sched_dead.sched_dead_dt as sched_dead_dt,
         decode(
            task_task.last_sched_dead_bool,
            null, (evt_sched_dead.sched_dead_dt - sysdate),
            decode(
               task_task.last_sched_dead_bool,
               0, (evt_sched_dead.sched_dead_dt - sysdate),
               -1 * (evt_sched_dead.sched_dead_dt - sysdate)
            )
         ) as sort_column
      FROM
         evt_event,
         evt_sched_dead,
         sched_stask,
         sched_stask child_task,
         task_task
      WHERE
         evt_event.h_event_db_id = cl_RootEventDbId AND
         evt_event.h_event_id    = cl_RootEventId
         AND
         evt_event.hist_bool = 0	AND
         evt_event.rstat_cd  = 0
         and
         sched_stask.sched_db_id = evt_event.h_event_db_id and
         sched_stask.sched_id    = evt_event.h_event_id
         AND
         child_task.sched_db_id        = evt_event.event_db_id AND
         child_task.sched_id           = evt_event.event_id    AND
         child_task.soft_deadline_bool = 0 
         and
         task_task.task_db_id (+)= sched_stask.task_db_id and
         task_task.task_id    (+)= sched_stask.task_id
         AND
         evt_sched_dead.event_db_id       = evt_event.event_db_id AND
         evt_sched_dead.event_id          = evt_event.event_id AND
         evt_sched_dead.sched_driver_bool = 1
      ORDER BY
         sort_column ASC;

      lrec_Deadline lcur_Deadline%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

     /*Get the root stask for the event*/
     SELECT evt_event.h_event_db_id,
            evt_event.h_event_id
     INTO ln_RootEventDbId,
          ln_RootEventId
     FROM evt_event
     WHERE
           evt_event.event_db_id = an_SchedStaskDbId AND
           evt_event.event_id = an_SchedStaskId	     AND
           evt_event.rstat_cd = 0;


     /* Get the driving deadline for the task, sorted by predicted days remaining */
     OPEN lcur_Deadline (ln_RootEventDbId,
                              ln_RootEventId);
     LOOP                              
     FETCH lcur_Deadline INTO lrec_Deadline;

     /* If no driving deadline is found, remove any that may exist from moved tasks */
     IF NOT lcur_Deadline%FOUND THEN

        /* Delete all driving deadlines on the root task */
        DELETE
          FROM evt_event_rel
         WHERE event_db_id = ln_RootEventDbId AND
               event_id    = ln_RootEventId
           AND rel_type_db_id = 0 AND
               rel_type_cd    = 'DRVTASK';
      UpdateWormvlDeadline(ln_RootEventDbId, ln_RootEventId, on_Return);

      CLOSE lcur_Deadline;

        /* Return */
        on_Return := icn_Success;
        RETURN;

     /* Otherwise, if a driving deadline is found, retain its event */
     ELSE
        ln_DrivingEventDbId := lrec_Deadline.event_db_id;
        ln_DrivingEventId := lrec_Deadline.event_id;
        
        /* check if the driving deadline task is a 'Next Shop Visit' and 'OFFWING' Task*/
        IF ln_DrivingEventId IS NOT NULL THEN

         SELECT COUNT(*) INTO ln_CountVar
         FROM
           sched_stask,
           task_task,
           task_task_flags
         WHERE
           sched_stask.sched_db_id = ln_DrivingEventDbId  AND
           sched_stask.sched_id    = ln_DrivingEventId
           AND
           task_task.task_db_id  = sched_stask.task_db_id AND
           task_task.task_id     = sched_stask.task_id
           AND
           task_task_flags.task_db_id = task_task.task_db_id AND
           task_task_flags.task_id    = task_task.task_id
           AND
           task_task_flags.nsv_bool = 1
           AND
           task_task.task_must_remove_cd = 'OFFWING';

         /* exit if the task with driving deadline is not a NSV with OFFWING value*/
         IF ln_CountVar < 1 THEN
           EXIT;
         END IF;
        END IF;
        
     END IF;
     END LOOP;
     CLOSE lcur_Deadline;
     
     


     /*check if the root task have driving relationship set*/
     SELECT
          count(*)
     INTO ln_DrivingCount
     FROM evt_event_rel
     WHERE
          evt_event_rel.event_db_id =  ln_RootEventDbId AND
          evt_event_rel.event_id = ln_RootEventId AND
          rel_type_db_id = 0 AND
          rel_type_cd = 'DRVTASK';

    /*if driving relationship is not set, we will have to create a new one*/
    IF ln_DrivingCount=0 THEN

     /*get the next allowed event_ord and rel_id for this task */
     SELECT
          decode (max(rel_event_ord), null, 1, max(rel_event_ord)+1),
          decode (max(event_rel_id), null, 1,  max(event_rel_id)+1)
     INTO ln_RelEventOrd,
          ln_EvtEventRelId
     FROM evt_event_rel
     WHERE
          evt_event_rel.event_db_id =  ln_RootEventDbId AND
          evt_event_rel.event_id = ln_RootEventId;


     /*insert new driving deadline relationship*/
     INSERT INTO evt_event_rel(event_db_id,
                               event_id,
                               event_rel_id,
                               rel_event_db_id,
                               rel_event_id,
                               rel_type_db_id,
                               rel_type_cd,
                               rel_event_ord )

     VALUES(      ln_RootEventDbId,
                  ln_RootEventId,
                  ln_EvtEventRelId,
                  ln_DrivingEventDbId,
                  ln_DrivingEventId,
                  0,
                  'DRVTASK',
                  ln_RelEventOrd);



     ELSE
     /* if the driving relationship already exists*/

     /* just update it */
     UPDATE evt_event_rel SET rel_event_db_id=ln_DrivingEventDbId,
                              rel_event_id=ln_DrivingEventId
     WHERE
          evt_event_rel.event_db_id =  ln_RootEventDbId AND
          evt_event_rel.event_id = ln_RootEventId AND
          rel_type_db_id = 0 AND
          rel_type_cd = 'DRVTASK' AND
          NOT (
            rel_event_db_id =ln_DrivingEventDbId AND
            rel_event_id    =ln_DrivingEventId
          );
     END IF;


     UpdateWormvlDeadline(ln_RootEventDbId, ln_RootEventId, on_Return);
     IF on_Return < 0 THEN
        RETURN;
     END IF;

     -- Return success
     on_Return := icn_Success;


EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDrivingDeadline@@@' || SQLERRM);
      IF lcur_Deadline%ISOPEN THEN CLOSE lcur_Deadline; END IF;      
      RETURN;
   END UpdateDrivingDeadline;





/********************************************************************************
*
* Procedure:      UpdateWormvlDeadline
* Arguments:      an_WorkPkgDbId (IN NUMBER): work order primary key
*                 an_WorkPkgId(IN NUMBER):    ---//---
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   Update deadline of a work order replacement task which is shown
*                to have a relationship WORMVL. The deadline will be copied wrom
*                the work order driving deadline task.

*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    July 3, 2008
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWormvlDeadline(
          an_WorkPkgDbId          IN  typn_Id,
          an_WorkPkgId            IN  typn_Id,
          on_Return                  OUT NUMBER
   )IS

	ll_SoftDeadline          sched_stask.soft_deadline_bool%TYPE;

     CURSOR lcur_TsnQt (
          cl_DataTypeDbId  typn_Id,
          cl_DataTypeId    typn_Id,
          cl_ReplInvNoDbId typn_Id,
          cl_ReplInvNoId   typn_id
     ) IS
          SELECT inv_curr_usage.tsn_qt
          FROM
                 inv_curr_usage
          WHERE
                 inv_curr_usage.data_type_db_id = cl_DataTypeDbId  AND
                 inv_curr_usage.data_type_id    = cl_DataTypeId    AND
                 inv_curr_usage.inv_no_db_id    = cl_ReplInvNoDbId AND
                 inv_curr_usage.inv_no_id       = cl_ReplInvNoId;
   
	lrec_TsnQt lcur_TsnQt%ROWTYPE;

		CURSOR lcur_DrvTaskDeadline (
			cl_EventDbId   typn_Id,
			cl_EventId     typn_Id
      ) IS
          SELECT ReplTaskInv.inv_no_db_id repl_inv_no_db_id,
                 ReplTaskInv.inv_no_id repl_inv_no_id,
                 ReplTaskInv.event_db_id,
                 ReplTaskInv.event_id,
                 DrvTaskInv.inv_no_db_id drv_inv_no_db_id,
                 DrvTaskInv.inv_no_id drv_inv_no_id,
                 drvtask_dead.data_type_db_id,
                 drvtask_dead.data_type_id,
                 drvtask_dead.sched_dead_qt,
                 drvtask_dead.sched_dead_dt,
                 drvtask_dead.usage_rem_qt,
                 drvtask_dead.start_dt,
                 drvtask_dead.start_qt,
                 drvtask_dead.notify_qt,
                 drvtask_dead.deviation_qt,
                 drvtask_dead.interval_qt,
                 drvtask_dead.prefixed_qt,
                 drvtask_dead.postfixed_qt,
                 mim_data_type.domain_type_cd
          FROM   evt_event_rel wormvl_rel,
                 evt_event_rel drvtask_rel,
                 evt_sched_dead drvtask_dead,
                 evt_inv ReplTaskInv,
                 evt_inv DrvTaskInv,
                 mim_data_type
          WHERE  wormvl_rel.rel_type_cd     ='WORMVL'      AND
                 wormvl_rel.rel_event_db_id = cl_EventDbId AND
                 wormvl_rel.rel_event_id    = cl_EventId
                 AND
                 ReplTaskInv.event_db_id = wormvl_rel.event_db_id AND
                 ReplTaskInv.event_id    = wormvl_rel.event_id
                 AND
                 drvtask_rel.rel_type_cd = 'DRVTASK'                  AND
                 drvtask_rel.event_db_id = wormvl_rel.rel_event_db_id AND
                 drvtask_rel.event_id    = wormvl_rel.rel_event_id
                 AND
                 drvtask_dead.event_db_id = drvtask_rel.rel_event_db_id AND
                 drvtask_dead.event_id    = drvtask_rel.rel_event_id    AND
                 drvtask_dead.sched_driver_bool = 1
                 AND
                 DrvTaskInv.event_db_id = drvtask_dead.event_db_id AND
                 DrvTaskInv.event_id    = drvtask_dead.event_id
                 AND
                 mim_data_type.data_type_db_id = drvtask_dead.data_type_db_id AND
                 mim_data_type.data_type_id    = drvtask_dead.data_type_id;
                 lrec_DrvTaskDeadline lcur_DrvTaskDeadline%ROWTYPE;
	
	   CURSOR lcur_Deadline (
         cl_RootEventDbId   typn_Id,
         cl_RootEventId     typn_Id
      ) IS
		
   SELECT
      evt_event.event_db_id as event_db_id,
      evt_event.event_id as event_id,
      decode(
         task_task.last_sched_dead_bool,
         null, (evt_sched_dead.sched_dead_dt - sysdate),
         decode(
            task_task.last_sched_dead_bool,
            0, (evt_sched_dead.sched_dead_dt - sysdate),
            -1 * (evt_sched_dead.sched_dead_dt - sysdate)
         )
      ) as sort_column
   FROM
      evt_event,
      evt_sched_dead,
      sched_stask,
      sched_stask child_task,
      task_task
   WHERE
      evt_event.h_event_db_id = cl_RootEventDbId AND
      evt_event.h_event_id    = cl_RootEventId
      AND
      evt_event.hist_bool = 0
      AND
      sched_stask.sched_db_id = evt_event.h_event_db_id AND
      sched_stask.sched_id    = evt_event.h_event_id
      AND
      child_task.sched_db_id        = evt_event.event_db_id AND
      child_task.sched_id           = evt_event.event_id    AND
      child_task.soft_deadline_bool = 0 
      AND
      task_task.task_db_id (+)= sched_stask.task_db_id AND
      task_task.task_id    (+)= sched_stask.task_id
      AND
      evt_sched_dead.event_db_id       = evt_event.event_db_id AND
      evt_sched_dead.event_id          = evt_event.event_id AND
      evt_sched_dead.sched_driver_bool = 1
   ORDER BY
      sort_column ASC;

   lrec_Deadline lcur_Deadline%ROWTYPE;

   CURSOR lcur_ReplTask (
       cl_EventDbId   typn_Id,
       cl_EventId     typn_Id
   ) IS
       SELECT 
          evt_event_rel.event_db_id,
          evt_event_rel.event_id
       FROM   
          evt_event_rel
       WHERE  
          evt_event_rel.rel_type_cd     ='WORMVL'      AND
          evt_event_rel.rel_event_db_id = cl_EventDbId AND
          evt_event_rel.rel_event_id    = cl_EventId;

   lrec_ReplTask lcur_ReplTask%ROWTYPE;
   ld_StartDt DATE;
   lf_StartQt FLOAT;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   OPEN lcur_DrvTaskDeadline (an_WorkPkgDbId, an_WorkPkgId);
   FETCH lcur_DrvTaskDeadline INTO lrec_DrvTaskDeadline;

   /*If the WorkOrder has a driving task then*/
   IF lcur_DrvTaskDeadline%FOUND THEN

     /* get the replacement tasks inventory tsn_qt */
     OPEN lcur_TsnQt(lrec_DrvTaskDeadline.data_type_db_id,
                     lrec_DrvTaskDeadline.data_type_id,
                     lrec_DrvTaskDeadline.repl_inv_no_db_id,
                     lrec_DrvTaskDeadline.repl_inv_no_id);
     FETCH lcur_TsnQt INTO lrec_TsnQt;

     /* if TSN for this data type exists on the inventory*/
     IF lcur_TsnQt%FOUND OR lrec_DrvTaskDeadline.domain_type_cd = 'CA' THEN

       /*Delete any exisitng dealines of the REPL task*/
       DELETE evt_sched_dead 
       WHERE  event_db_id = lrec_DrvTaskDeadline.event_db_id AND
              event_id    = lrec_DrvTaskDeadline.event_id;
       
       IF lrec_DrvTaskDeadline.Domain_Type_Cd = 'CA' THEN
          ld_StartDt := lrec_DrvTaskDeadline.start_dt;
          lf_StartQt := NULL;
       ELSE
          lf_StartQt := lrec_DrvTaskDeadline.start_qt;       
          ld_StartDt := NULL;
       END IF;
       /*Simply copy the driving deadline of the driving task to the REPL task */
       INSERT INTO evt_sched_dead (event_db_id,
                                   event_id,
                                   data_type_db_id,
                                   data_type_id,
                                   sched_from_db_id,
                                   sched_from_cd,
                                   start_dt,
                                   start_qt,
                                   sched_dead_qt,
                                   sched_dead_dt,
                                   usage_rem_qt,
                                   notify_qt,
                                   sched_driver_bool,
                                   deviation_qt,
                                   interval_qt,
                                   prefixed_qt,
                                   postfixed_qt)
                           VALUES (lrec_DrvTaskDeadline.event_db_id,
                                   lrec_DrvTaskDeadline.event_id,
                                   lrec_DrvTaskDeadline.data_type_db_id,
                                   lrec_DrvTaskDeadline.data_type_id,
                                   0,
                                   'CUSTOM',
                                   ld_StartDt,
                                   lf_StartQt,
                                   lrec_DrvTaskDeadline.sched_dead_qt,
                                   lrec_DrvTaskDeadline.sched_dead_dt,
                                   lrec_DrvTaskDeadline.usage_rem_qt,
                                   lrec_DrvTaskDeadline.notify_qt,
                                   1,
                                   lrec_DrvTaskDeadline.deviation_qt,
                                   lrec_DrvTaskDeadline.interval_qt,
                                   lrec_DrvTaskDeadline.prefixed_qt,
                                   lrec_DrvTaskDeadline.postfixed_qt
                                  );
       /* if the WorkOrder's driving task is on a different piece of inventory and usage based deadline */
       IF (NOT (lrec_DrvTaskDeadline.repl_inv_no_db_id = lrec_DrvTaskDeadline.drv_inv_no_db_id) OR
           NOT (lrec_DrvTaskDeadline.repl_inv_no_id  = lrec_DrvTaskDeadline.drv_inv_no_id)
          ) AND lrec_DrvTaskDeadline.domain_type_cd = 'US' THEN

            /* recalculate the deadline_qt */
            UPDATE evt_sched_dead
            SET    sched_dead_qt= lrec_TsnQt.Tsn_Qt+lrec_DrvTaskDeadline.usage_rem_qt
            WHERE  event_db_id = lrec_DrvTaskDeadline.event_db_id AND
                   event_id    = lrec_DrvTaskDeadline.event_id  AND
                   data_type_db_id = lrec_DrvTaskDeadline.data_type_db_id AND
                   data_type_id    = lrec_DrvTaskDeadline.data_type_id AND
                   (
                    sched_dead_qt<> lrec_TsnQt.Tsn_Qt+lrec_DrvTaskDeadline.usage_rem_qt
                    OR
                    sched_dead_qt IS NULL
                    );
       END IF;
     END IF;

		/* Get the driving deadline for the component work package */
		OPEN lcur_Deadline (an_WorkPkgDbId, an_WorkPkgId);
	
		FETCH lcur_Deadline INTO lrec_Deadline;

		/* If driving deadline is found */
		IF lcur_Deadline%FOUND THEN

			/* Get the soft deadline boolean value */
			SELECT
				sched_stask.soft_deadline_bool
			INTO 
				ll_SoftDeadline
			FROM
				sched_stask
			WHERE
				sched_stask.sched_db_id =  lrec_Deadline.event_db_id AND
				sched_stask.sched_id    = lrec_Deadline.event_id;

			/* Sync it to the new task */
			UPDATE 
				sched_stask
			SET    
				sched_stask.soft_deadline_bool = ll_SoftDeadline
			WHERE
				sched_stask.sched_db_id = lrec_DrvTaskDeadline.event_db_id AND
				sched_stask.sched_id    = lrec_DrvTaskDeadline.event_id;

		END IF;

		CLOSE lcur_Deadline;
    CLOSE lcur_TsnQt;		
		UpdateDeadline(lrec_DrvTaskDeadline.event_db_id, lrec_DrvTaskDeadline.event_id, on_Return);
		IF on_Return < 0 THEN
			RETURN;
		END IF;
   ELSE   
    OPEN lcur_ReplTask (an_WorkPkgDbId, an_WorkPkgId);
    FETCH lcur_ReplTask INTO lrec_ReplTask;

    /* If Replacement Task if found */
    IF lcur_ReplTask%FOUND THEN
    
       /*Delete any exisitng dealines of the REPL task since there is no driving task for work package */
       DELETE evt_sched_dead
       WHERE  event_db_id = lrec_ReplTask.event_db_id AND
              event_id    = lrec_ReplTask.event_id;
    
       UpdateDeadline(lrec_ReplTask.event_db_id, lrec_ReplTask.event_id, on_Return);
       IF on_Return < 0 THEN
         RETURN;
       END IF;

    END IF;
    CLOSE lcur_ReplTask;            

   END IF;
   CLOSE lcur_DrvTaskDeadline;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateWormvlDeadline@@@' || SQLERRM);
      IF lcur_DrvTaskDeadline%ISOPEN THEN CLOSE lcur_DrvTaskDeadline; END IF;
      IF lcur_TsnQt%ISOPEN           THEN CLOSE lcur_TsnQt;           END IF;            
      RETURN;
END UpdateWormvlDeadline;


/*
* Reworked version of findForecastedDeadDtFlightPlan
*/
PROCEDURE findForecastDeadFlightPlan_new(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%TYPE,
      aAircraftId IN inv_ac_reg.inv_no_id%TYPE,
      aDataTypeDbId IN mim_data_type.data_type_db_id%TYPE,
      aDataTypeId IN mim_data_type.data_type_id%TYPE,
      aCurrentDate IN OUT DATE,
      aRemainingUsageQt IN OUT evt_sched_dead.usage_rem_qt%TYPE,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%TYPE,
      aOverlapUsageQt OUT evt_sched_dead.usage_rem_qt%type,
      ol_Return OUT NUMBER
)
IS
   -- This query fetches all the non-historic flights against the current aircraft
   CURSOR lCurFlightPlanInfo
   IS
   SELECT DISTINCT -- This is necessary to eliminate duplicate flight as they will cause a loop
          NVL(evt_event.actual_start_dt,evt_event.sched_start_dt) AS flight_start_dt
         ,NVL(evt_event.event_dt,evt_event.sched_end_dt) AS flight_end_dt
         ,((NVL(evt_event.event_dt,evt_event.sched_end_dt) - NVL(evt_event.actual_start_dt,evt_event.sched_start_dt))*24) AS flight_hrs
     FROM evt_event
         ,evt_inv
    WHERE evt_event.event_type_cd = 'FL'
      AND evt_event.hist_bool     = 0
      AND evt_inv.event_db_id = evt_event.event_db_id
      AND evt_inv.event_id    = evt_event.event_id
      AND evt_inv.main_inv_bool = 1
      AND evt_inv.inv_no_db_id  = aAircraftDbId
      AND evt_inv.inv_no_id     = aAircraftId
      AND NVL(evt_event.event_dt,evt_event.sched_end_dt) > aCurrentDate
      AND NVL(evt_event.event_dt,evt_event.sched_end_dt) IS NOT NULL
      AND NVL(evt_event.actual_start_dt,evt_event.sched_start_dt) IS NOT NULL
    ORDER
       BY NVL(evt_event.event_dt,evt_event.sched_end_dt) -- since the end date is used as the filter it must be sorted by this value as well
         ,NVL(evt_event.actual_start_dt,evt_event.sched_start_dt);

   -- cursor iterator
   lFlightPlanInfoRec lCurFlightPlanInfo%ROWTYPE;
   lExitLoop NUMBER;
BEGIN

     -- dealing with CYCLES or LANDINGS
     IF ( aDataTypeDbId = 0 AND ( aDataTypeId = 10 OR aDataTypeId = 30 ) ) THEN

       -- open the flight plan cursor
       OPEN lCurFlightPlanInfo;

       -- loop through our list of flights
       lExitLoop := 0;
       WHILE lExitLoop = 0 LOOP
           -- get the next flight plan row
           FETCH lCurFlightPlanInfo INTO lFlightPlanInfoRec;

           -- if there is a flight
           IF lCurFlightPlanInfo%FOUND THEN

              -- if there is no remaining usage, then the deadline is due right after this flight
              IF aRemainingUsageQt-1 <= 0 THEN
                  -- Update the overlap and remaining usage
                  aOverlapUsageQt   := 0;
                  aRemainingUsageQt := aRemainingUsageQt-1;
                  -- record the value before altering
                  aCurrentDate      := lFlightPlanInfoRec.flight_end_dt;
                  -- set the date to one minute after the flight and return
                  aForecastDt := lFlightPlanInfoRec.flight_end_dt + 1/(1440);
                  -- Processed successfully.
                  CLOSE lCurFlightPlanInfo;
                  ol_Return := icn_Success;
                  RETURN;

              END IF;
              -- Update the remaining usage and current date
              aRemainingUsageQt := aRemainingUsageQt-1;
              aCurrentDate := lFlightPlanInfoRec.flight_end_dt;
           ELSE
              -- if the remaining usage
              lExitLoop := 1;
           END IF;
       END LOOP;

       -- the flight plan didnt go far enough to find the forecasted date.
       -- our current date has been updated to be the end of the last flight in the flight plan
       -- and our remaining usage has been adjusted for the flight plan.
       CLOSE lCurFlightPlanInfo;
       aOverlapUsageQt := 0;
       aForecastDt := NULL;
       ol_Return := icn_Success;
       RETURN;

     -- dealing with flying hours.
     ELSIF ( aDataTypeDbId = 0 AND aDataTypeId = 1 ) THEN

       -- open the flight plan cursor
       OPEN lCurFlightPlanInfo;

       lExitLoop := 0;
       WHILE lExitLoop = 0 LOOP
           -- get the next flight plan row
           FETCH lCurFlightPlanInfo INTO lFlightPlanInfoRec;

           -- If there is a flight
           IF lCurFlightPlanInfo%FOUND THEN
              -- has more flight time been accrued that usage that is remaining:3
              IF aRemainingUsageQt - lFlightPlanInfoRec.flight_hrs < 0 THEN
                  -- Update the overlap and remaining usage
                  aOverlapUsageQt   := aRemainingUsageQt;
                  aRemainingUsageQt := aRemainingUsageQt - lFlightPlanInfoRec.flight_hrs;
                  -- record the value before altering
                  aCurrentDate      := lFlightPlanInfoRec.flight_start_dt;
                  -- set the deadline to one minute before this flight
                  aForecastDt := lFlightPlanInfoRec.flight_start_dt - (1/1440);
                  -- Processed successfully.
                  CLOSE lCurFlightPlanInfo;
                  ol_Return := icn_Success;
                  RETURN;
              END IF;
              -- update our current date and remaining usages for the flight
              aCurrentDate := lFlightPlanInfoRec.flight_end_dt;
              aRemainingUsageQt := aRemainingUsageQt - lFlightPlanInfoRec.flight_hrs;
           ELSE
              -- if the remaining usage
              lExitLoop := 1;
           END IF;
       END LOOP;

       -- the flight plan didnt go far enough to find the forecasted date.
       -- our current date has been updated to be the end of the last flight in the flight plan
       -- and our remaining usage has been adjusted for the flight plan.
       CLOSE lCurFlightPlanInfo;
       aOverlapUsageQt := 0;
       aForecastDt := NULL;
       ol_Return := icn_Success;
       RETURN;

     -- invalid data type
     ELSE
     IF lCurFlightPlanInfo%ISOPEN THEN CLOSE lCurFlightPlanInfo; END IF;
        aOverlapUsageQt := 0;
        aForecastDt := NULL;
        ol_Return := icn_InvalidDataType;
        RETURN;
     END IF;

    -- exception handling
    EXCEPTION
       WHEN OTHERS THEN
          ol_Return := -100;
          APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastDeadFlightPlan_new@@@' || SQLERRM);
          IF lCurFlightPlanInfo%ISOPEN THEN CLOSE lCurFlightPlanInfo; END IF;
END findForecastDeadFlightPlan_new;

/********************************************************************************
*
* PROCEDURE:      findForecastedDeadDtFlightPlan
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aCurrentDate (IN OUT DATE): The starting date to use when calculating the forecasted date.
*                                             If the forecasted date extends past the flight plan, then this
*                                             date will be set to the scheduled end date of the last flight in
*                                             the flight plan upon exit.
*                 aRemainingUsageQt (IN NUMBER): amount of remaining usage before the deadline is due.
*                                             If the forecasted date extends past the flight plan, then this
*                                             value will be set to the remaining usage as of the scheduled end
*                                             of the last flight in the flight plan.
*                 aForecastDt (OUT DATE): the date that the deadline will become due. NULL if the forecasted date
*                                         cannot be calculated.
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Use the aircrafts flight plan to determine the forecasted deadline date given the current
*                 date/usage combination. The flight plan is only used to calculate forecasted deadline dates
*                 for usage types of CYCLES, LANDINGS, and flying HOURS.
*
* Orig.Coder:     Neil Ouellette
* Recent Coder:   N/A
* Recent Date:    Dec 19, 2006
*
*********************************************************************************
*
* Copyright 1998-2006 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE findForecastedDeadDtFlightPlan(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%TYPE,
      aAircraftId IN inv_ac_reg.inv_no_id%TYPE,
      aDataTypeDbId IN mim_data_type.data_type_db_id%TYPE,
      aDataTypeId IN mim_data_type.data_type_id%TYPE,
      aCurrentDate IN OUT DATE,
      aRemainingUsageQt IN OUT evt_sched_dead.usage_rem_qt%TYPE,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%TYPE,
      ol_Return OUT NUMBER
)
IS
   -- This query fetches all the non-historic flights against the current aircraft
   CURSOR lCurFlightPlanInfo IS
      SELECT
        NVL( evt_event.actual_start_dt, evt_event.sched_start_dt ) as flight_start_dt,
        NVL( evt_event.event_dt, evt_event.sched_end_dt ) as flight_end_dt,
         ( ( NVL( evt_event.event_dt, evt_event.sched_end_dt ) - NVL( evt_event.actual_start_dt, evt_event.sched_start_dt ) )*24 ) as flight_hrs
      FROM
         evt_event,
         evt_inv
      WHERE
         evt_event.event_type_cd = 'FL' AND
         evt_event.hist_bool     = 0	AND
         evt_event.rstat_cd	 = 0
         AND
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
         and
         evt_inv.main_inv_bool = 1 AND
         evt_inv.inv_no_db_id  = aAircraftDbId  AND
         evt_inv.inv_no_id     = aAircraftId
        AND
        evt_event.sched_end_dt > aCurrentDate
        and
        NVL( evt_event.actual_start_dt, evt_event.sched_start_dt ) is not null and
        NVL( evt_event.event_dt, evt_event.sched_end_dt ) is not null
      ORDER BY
        NVL( evt_event.actual_start_dt, evt_event.sched_start_dt );

   -- cursor iterator
   lFlightPlanInfoRec lCurFlightPlanInfo%ROWTYPE;
   lExitLoop NUMBER;
BEGIN
     -- dealing with CYCLES or LANDINGS
     IF ( aDataTypeDbId = 0 AND ( aDataTypeId = 10 OR aDataTypeId = 30 ) ) THEN

       -- open the flight plan cursor
       OPEN lCurFlightPlanInfo;

       -- loop through our list of flights
       lExitLoop := 0;
       WHILE lExitLoop = 0 LOOP
           -- get the next flight plan row
           FETCH lCurFlightPlanInfo INTO lFlightPlanInfoRec;

           -- is there a flight?
           IF lCurFlightPlanInfo%FOUND THEN
              -- update our current date and remaining usages for the flight
              aCurrentDate := lFlightPlanInfoRec.flight_end_dt;
              aRemainingUsageQt := aRemainingUsageQt-1;

              -- if there is no remaining usage, then the deadline is due right after this flight
              IF aRemainingUsageQt <= 0 THEN
                  -- set the date to one minute after the flight and return
                  aForecastDt := lFlightPlanInfoRec.flight_end_dt + 1/(1440);

                  -- Processed successfully.
                  CLOSE lCurFlightPlanInfo;
                  ol_Return := icn_Success;
                  RETURN;
              END IF;
           ELSE
              -- if the remaining usage
              lExitLoop := 1;
           END IF;
       END LOOP;

       -- the flight plan didnt go far enough to find the forecasted date.
       -- our current date has been updated to be the end of the last flight in the flight plan
       -- and our remaining usage has been adjusted for the flight plan.
       CLOSE lCurFlightPlanInfo;
       aForecastDt := NULL;
       ol_Return := icn_Success;
       RETURN;

     -- dealing with flying hours.
     ELSIF ( aDataTypeDbId = 0 AND aDataTypeId = 1 ) THEN

       -- open the flight plan cursor
       OPEN lCurFlightPlanInfo;

       lExitLoop := 0;
       WHILE lExitLoop = 0 LOOP
           -- get the next flight plan row
           FETCH lCurFlightPlanInfo INTO lFlightPlanInfoRec;

           -- is there a flight?
           IF lCurFlightPlanInfo%FOUND THEN
              -- update our current date and remaining usages for the flight
              aCurrentDate := lFlightPlanInfoRec.flight_end_dt;
              aRemainingUsageQt := aRemainingUsageQt - lFlightPlanInfoRec.flight_hrs;

              -- has more flight time been accrued that usage that is remaining?
              IF aRemainingUsageQt < 0 THEN
                 -- set the deadline to one minute before this flight
                 aForecastDt := lFlightPlanInfoRec.flight_start_dt - (1/1440);

                 -- Processed successfully.
                 CLOSE lCurFlightPlanInfo;
                 ol_Return := icn_Success;
                 RETURN;
              END IF;

           ELSE
              -- if the remaining usage
              lExitLoop := 1;
           END IF;
       END LOOP;

       -- the flight plan didnt go far enough to find the forecasted date.
       -- our current date has been updated to be the end of the last flight in the flight plan
       -- and our remaining usage has been adjusted for the flight plan.
       CLOSE lCurFlightPlanInfo;
       aForecastDt := NULL;
       ol_Return := icn_Success;
       RETURN;

     -- invalid data type
     ELSE
	IF lCurFlightPlanInfo%ISOPEN THEN CLOSE lCurFlightPlanInfo; END IF;	
        ol_Return := icn_InvalidDataType;
        RETURN;
     END IF;

    -- exception handling
    EXCEPTION
       WHEN OTHERS THEN
          ol_Return := -100;
          APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastedDeadDtFlightPlan@@@' || SQLERRM);
          IF lCurFlightPlanInfo%ISOPEN THEN CLOSE lCurFlightPlanInfo; END IF;          
END findForecastedDeadDtFlightPlan;


/********************************************************************************
*
* PROCEDURE:      findForecastedDeadDtFcModel
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aStartDate (IN DATE): The starting date to use when calculating the forecasted date.
*                 aRemainingUsageQt (IN NUMBER): amount of remaining usage before the deadline is due.
*                 aForecastDt (IN DATE): the date that the deadline will become due.
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Use the aircrafts forecast model to determine the forecasted deadline date given the current
*                 date/usage combination. Blackouts are also taken into consideration when determining the
*                 deadline date and are treated as zero rate usage accrual ranges.
*
* Orig.Coder:     Neil Ouellette
* Recent Coder:   N/A
* Recent Date:    Dec 19, 2006
*
*********************************************************************************
*
* Copyright 1998-2006 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE findForecastedDeadDtFcModel(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aStartDate IN date,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      ol_Return OUT NUMBER
)
IS
   -- list of fc_range entries for the forecast model assigned to the given aircraft.
   -- the list of ranges are for the given year only.
   CURSOR lCurRanges ( aYear NUMBER ) is
      SELECT
         to_date( aYear || '-' || fc_range.start_month || '-' || fc_range.start_day, 'YYYY-MM-DD' ) as start_date,
         decode( next_row.start_month, null, to_date( aYear+1 || '-' || first_row.start_month || '-' || first_row.start_day, 'YYYY-MM-DD' ), to_date( aYear || '-' || next_row.start_month || '-' || next_row.start_day, 'YYYY-MM-DD' ) ) as end_date,
         decode( fc_rate.forecast_rate_qt, null, 1, fc_rate.forecast_rate_qt) as forecast_rate
      FROM
          fc_range,
          fc_range first_row,
          fc_range next_row,
          fc_rate,
          inv_ac_reg
      WHERE
          inv_ac_reg.inv_no_db_id = aAircraftDbId AND
          inv_ac_reg.inv_no_id    = aAircraftId
          AND
          fc_range.model_db_id = inv_ac_reg.forecast_model_db_id AND
          fc_range.model_id    = inv_ac_reg.forecast_model_id
          AND
          first_row.model_db_id (+)= fc_range.model_db_id AND
          first_row.model_id    (+)= fc_range.model_id AND
          first_row.range_id    (+)= 1
          AND
          next_row.model_db_id (+)= fc_range.model_db_id AND
          next_row.model_id    (+)= fc_range.model_id AND
          next_row.range_id    (+)= fc_range.range_id+1
          AND
          fc_rate.model_db_id     (+)= fc_range.model_db_id AND
          fc_rate.model_id        (+)= fc_range.model_id AND
          fc_rate.range_id        (+)= fc_range.range_id AND
          fc_rate.data_type_db_id (+)= aDataTypeDbId AND
          fc_rate.data_type_id    (+)= aDataTypeId
          AND
          (
             decode( next_row.start_month, null, to_date( aYear+1 || '-' || first_row.start_month || '-' || first_row.start_day, 'YYYY-MM-DD' ), to_date( aYear || '-' || next_row.start_month || '-' || next_row.start_day, 'YYYY-MM-DD' ) ) > aStartDate
             or
             to_date( aYear || '-' || fc_range.start_month || '-' || fc_range.start_day, 'YYYY-MM-DD' ) > aStartDate
          )
      ORDER BY
          start_date;

   -- list of blackouts listed against the given aircraft. Only blackouts that span past the given
   -- aFromDate argument date are listed
   CURSOR lCurBlackouts ( aFromDate DATE ) IS
      SELECT
         evt_event.actual_start_dt as start_date,
         evt_event.event_dt as end_date,
         0 as forecast_rate
      FROM
         evt_event,
         evt_inv
      WHERE
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id    = evt_inv.event_id
         AND
         evt_inv.inv_no_db_id = aAircraftDbId AND
         evt_inv.inv_no_id    = aAircraftId
         AND
         evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd    = 'BLK'	AND
         evt_event.rstat_cd	= 0
         AND
         evt_event.event_dt > aFromDate
      ORDER BY
         evt_event.actual_start_dt;

   -- cursor records
   lRecBlackout lCurBlackouts%ROWTYPE;
   lRecRange lCurRanges%ROWTYPE;

   -- placeholders
   lRemainingUsage inv_curr_usage.tsn_qt%TYPE;
   lCurrentDate DATE;

   -- iterator varialbes
   lCurrentYear NUMBER;
   lExitLoop NUMBER;
BEGIN
    -- initialize the current date and remaining usage quantities
    lCurrentDate := aStartDate;
    lRemainingUsage := aRemainingUsageQt;

    -- update our year value
    lCurrentYear := to_char( lCurrentDate, 'YYYY' );

    -- make sure we a remaining usage is actuall specified
    IF lRemainingUsage <= 0 THEN
       -- overdue - use the current specified date as the due date
       aForecastDt := lCurrentDate;
       ol_Return := icn_Success;
       RETURN;
    END IF;

    -- OPEN our blackouts CURSOR AND fetch the first row
    OPEN lCurBlackouts( lCurrentDate );
    FETCH lCurBlackouts INTO lRecBlackout;

    -- OPEN our ranges CURSOR AND fetch the first row
    OPEN lCurRanges( lCurrentYear );
    FETCH lCurRanges INTO lRecRange;

    WHILE( lRemainingUsage > 0 ) LOOP

       -- does the current date span a blackout?
       IF lCurBlackouts%FOUND AND lRecBlackout.start_date <= lCurrentDate AND lRecBlackout.end_date >= lCurrentDate THEN
          -- IF so, move our current date pointer to the END of the blackout
          lCurrentDate := lRecBlackout.end_date;
          lCurrentYear := to_char( lCurrentDate, 'YYYY' );

          -- increment our blackouts CURSOR
          FETCH lCurBlackouts INTO lRecBlackout;

          -- increment our ranges CURSOR such that the END date of the current range occurs after the current date
          lExitLoop := 0;
          WHILE lExitLoop = 0 LOOP
             -- IF there is no range object, OPEN the next years set of ranges
             IF lCurRanges%NOTFOUND THEN
                lCurrentYear := lCurrentYear+1;

                CLOSE lCurRanges;
                OPEN lCurRanges(lCurrentYear);
                FETCH lCurRanges INTO lRecRange;

                -- IF there is no ranges then something is wrong
                IF lCurRanges%NOTFOUND THEN
                   -- CLOSE our cursors
                   CLOSE lCurRanges;
                   CLOSE lCurBlackouts;

                   -- flag the error and return
                   ol_Return := icn_InvalidForecastModel;
                   RETURN;
                END IF;
             -- range END date occurs before our current date
             ELSIF lCurRanges%FOUND AND lRecRange.end_date <= lCurrentDate THEN
                -- move ahead
                FETCH lCurRanges INTO lRecRange;
             -- found a range with an end date that occurs after the current date
             ELSE
                lExitLoop := 1;
             END IF;
          END LOOP;

       -- process the forecast model range
       ELSE
          -- IF there is no range object, OPEN the next years set of ranges
          IF lCurRanges%NOTFOUND THEN
             lCurrentYear := lCurrentYear+1;

             CLOSE lCurRanges;
             OPEN lCurRanges(lCurrentYear);
             FETCH lCurRanges INTO lRecRange;

             -- IF there is no ranges then something is wrong
             IF lCurRanges%NOTFOUND THEN
                 -- CLOSE our cursors
                 CLOSE lCurRanges;
                 CLOSE lCurBlackouts;

                 -- flag the error and return
                 ol_Return := icn_InvalidForecastModel;
                 RETURN;
             END IF;
          END IF;

           -- does a blackout start before this range ends?
          IF lCurBlackouts%FOUND AND lRecRange.end_date > lRecBlackout.start_date THEN

             -- will the remaining usage run out before the blackout starts?
             IF ( lRemainingUsage - lRecRange.forecast_rate * (  lRecBlackout.start_date - lCurrentDate ) ) <= 0 THEN
                -- get the actual date that it will run out at
                aForecastDt := lCurrentDate + lRemainingUsage / lRecRange.forecast_rate;

                -- CLOSE our cursors
                CLOSE lCurRanges;
                CLOSE lCurBlackouts;

                -- now that we have our deadline date, return
                ol_Return := icn_Success;
                RETURN;
             ELSE
                 -- we still have remaning usage at the END of this time span.

                 -- update our running usage total
                 lRemainingUsage := lRemainingUsage - lRecRange.forecast_rate * ( lRecBlackout.start_date - lCurrentDate );

                 -- update our current date to the END of the blackout date
                 lCurrentDate := lRecBlackout.end_date;

                 -- increment our blackouts CURSOR
                 FETCH lCurBlackouts INTO lRecBlackout;

                 -- IF the blackout runs into a new range, increment our 'range' CURSOR
                  lExitLoop := 0;
                  WHILE lExitLoop = 0 LOOP
                     -- IF there is no range object, OPEN the next years set of ranges
                     IF lCurRanges%NOTFOUND THEN
                        lCurrentYear := lCurrentYear+1;

                        CLOSE lCurRanges;
                        OPEN lCurRanges(lCurrentYear);
                        FETCH lCurRanges INTO lRecRange;

                        -- IF there is no ranges then something is wrong
                        IF lCurRanges%NOTFOUND THEN
                           -- CLOSE our cursors AND return
                           CLOSE lCurRanges;
                           CLOSE lCurBlackouts;

                           -- flag the error and return
                           ol_Return := icn_InvalidForecastModel;
                           RETURN;
                        END IF;
                     -- range END date occurs before our current date
                     ELSIF lCurRanges%FOUND AND lRecRange.end_date <= lCurrentDate THEN
                        -- move ahead
                        FETCH lCurRanges INTO lRecRange;
                     -- found a range with an end date that occurs after the current date
                     ELSE
                        lExitLoop := 1;
                     END IF;
                  END LOOP;
              END IF;
          ELSE
              -- there is no overlapping blackout.

              -- will the remaining usage run out during this timespan?
              IF ( lRemainingUsage - lRecRange.forecast_rate * ( lRecRange.end_date - lCurrentDate ) ) <= 0 THEN
                 -- get the actual date that it will run out at
                 aForecastDt := lCurrentDate + lRemainingUsage / lRecRange.forecast_rate;

                 -- CLOSE our cursors
                 CLOSE lCurRanges;
                 CLOSE lCurBlackouts;

                 -- now that we have our deadline date, return
                 ol_Return := icn_Success;
                 RETURN;
              ELSE
                 -- we still have remaning usage at the END of this time span.

                 -- update our running usage total
                 lRemainingUsage := lRemainingUsage - lRecRange.forecast_rate * ( lRecRange.end_date - lCurrentDate );

                 -- update our current date
                 lCurrentDate := lRecRange.end_date;

                 -- increment our range CURSOR
                 FETCH lCurRanges INTO lRecRange;
              END IF;
          END IF;
       END IF;

    END LOOP;

    -- exception handling
    EXCEPTION
       WHEN OTHERS THEN
          ol_Return := -100;
          APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastedDeadDtFcModel@@@' || SQLERRM);
          IF lCurBlackouts%ISOPEN THEN CLOSE lCurBlackouts; END IF;
          IF lCurRanges%ISOPEN    THEN CLOSE lCurRanges;    END IF;                    
END findForecastedDeadDtFcModel;

/********************************************************************************
*
* PROCEDURE:      PredictUsageBetweenDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aStartDate (IN DATE): the relative date to perform our calculations from. Normally call with SYSDATE
*                 ad_TargetDate (IN DATE): The date we want the deadline to be due
*                 an_StartUsageQt (IN NUMBER): the usages at the Start Date
*                 on_TsnQt (OUT NUMBER): The predicted TSN value at the ad_TargetDate
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted usage quantity at the specified date for a deadline with the specified datatype on
*                 the given inventory. The forecasted deadline value(quantity) is calculated using the aircrafts
*                 flight plan and forecast model. It uses binary search to re-use existing function to calculate due date based on usage value
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   N/A
* Recent Date:    March 19, 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PredictUsageBetweenDt(
            aAircraftDbId    IN  inv_inv.inv_no_db_id%TYPE,
            aAircraftId      IN  inv_inv.inv_no_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDate    IN  evt_sched_dead.start_dt%TYPE,
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_StartUsageQt IN  evt_sched_dead.start_qt%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS
  lForecastDate date;
  ln_RemainingUsageMinQt number;
  ln_RemainingUsageMaxQt number;
  ln_RemainingUsageQt number;
  lStartDate date;
BEGIN
     ln_RemainingUsageMaxQt:=1;
     ln_RemainingUsageMinQt:=1;

     -- Set calculated forecast date to a target date
     lForecastDate:=ad_TargetDate;
     lStartDate := ad_StartDate;

     -- Calculate the predicted date with minimal number before going into the binary search
     findForecastedDeadDt(
             aAircraftDbId,
             aAircraftId,
             an_DataTypeDbId,
             an_DataTypeId,
             1,
             lStartDate,
             lForecastDate,
             on_Return
            );
          
     -- if a forecast date is Today or in future then we will skip a the Binary loop 
     -- and have to set  ln_RemainingUsageMinQt and  ln_RemainingUsageMaxQt to Zero               
     IF lForecastDate>=ad_TargetDate THEN
            ln_RemainingUsageMinQt:=0;
            ln_RemainingUsageMaxQt:=0;
     END IF;
     
     -- Find the first increment that will put us out of range
     WHILE (lForecastDate<ad_TargetDate) LOOP
           lStartDate := ad_StartDate;
           ln_RemainingUsageMinQt := ln_RemainingUsageMaxQt;
           ln_RemainingUsageMaxQt := ln_RemainingUsageMaxQt*2;
           findForecastedDeadDt(aAircraftDbId, aAircraftId, an_DataTypeDbId, an_DataTypeId, ln_RemainingUsageMaxQt, lStartDate, lForecastDate, on_Return);
           IF on_Return < 0 THEN
              RETURN;
           END IF;

     END LOOP;

     -- Using binary search pinpoint the time when the calculated forecasted date is the same as the target/effective date.
     ln_RemainingUsageQt:=ln_RemainingUsageMaxQt;      
     WHILE ((ln_RemainingUsageMaxQt-ln_RemainingUsageMinQt)>1 AND (TRUNC(lForecastDate,'DD')<>TRUNC(ad_TargetDate,'DD'))) LOOP
           lStartDate := ad_StartDate;
           IF (lForecastDate<ad_TargetDate) THEN
              ln_RemainingUsageMinQt:=ln_RemainingUsageQt;
           ELSE
              ln_RemainingUsageMaxQt:=ln_RemainingUsageQt;
           END IF;
          ln_RemainingUsageQt:=ln_RemainingUsageMaxQt-(ln_RemainingUsageMaxQt-ln_RemainingUsageMinQt)/2;
          findForecastedDeadDt(aAircraftDbId, aAircraftId, an_DataTypeDbId, an_DataTypeId, ln_RemainingUsageQt, lStartDate, lForecastDate, on_Return);
           IF on_Return < 0 THEN
              RETURN;
           END IF;

     END LOOP;
     on_TsnQt:=an_StartUsageQt+ln_RemainingUsageQt;
     on_Return := icn_Success;

  EXCEPTION
   WHEN NO_DATA_FOUND THEN
      on_TsnQt := 0;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','Event_pkg@@@PredictUsageBetweenDt@@@'||SQLERRM);
   RETURN;

END PredictUsageBetweenDt;


/*
* Reworked version of findForecastedDeadDt
*/
PROCEDURE findForecastedDeadDt_new(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aStartDt IN OUT date,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      aOverlapUsageQt OUT evt_sched_dead.usage_rem_qt%type,
      ol_Return OUT NUMBER
)
IS
   lCurrentDate date;
   lRemainingUsageQt evt_sched_dead.usage_rem_qt%type;
BEGIN
   -- Initialize the return value
   ol_Return := icn_NoProc;
   aOverlapUsageQt := 0;

   -- initialize our 'current date' to the sysdate and our remaining usage counter to that specified in the args
   lCurrentDate := aStartDt;
   lRemainingUsageQt := aRemainingUsageQt;

   -- if the remaining usage is negative then this task is overdue.
   -- set the forecast date to the end of the previous day and return.
   IF lRemainingUsageQt <= 0 THEN
      aForecastDt := TO_DATE(TO_CHAR(aStartDt-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');
      ol_Return := icn_Success;
      RETURN;
   END IF;

   -- if we are dealing with a datatype that requires the flight plan to compute the forecasted
   -- date, then delegate to the flight plan calculation procedure.
   -- we use the flight plan calcuations for CYCLES, LANDINGS, or HOURS
   IF( aDataTypeDbId = 0 AND ( aDataTypeId = 1 OR aDataTypeId = 10 OR aDataTypeId = 30 ) ) THEN
      EVENT_PKG.findForecastDeadFlightPlan_new(
            aAircraftDbId,
            aAircraftId,
            aDataTypeDbId,
            aDataTypeId,
            lCurrentDate,
            lRemainingUsageQt,
            aForecastDt,
            aOverlapUsageQt,
            ol_Return );

       -- process successfully?
       IF ol_Return < 0 THEN
          RETURN;
       END IF;

       -- if the forecast date was found to be within the flight plan, return it
       IF aForecastDt IS NOT NULL THEN
          -- set our pointer to the found date
          aStartDt := lCurrentDate;

          -- return
          ol_Return := icn_Success;
          RETURN;
       END IF;

   END IF;

   -- if we got here, either the forecasted date was not found within the flight plan or the
   -- datatype was not one that required flight plan calculations.
   -- Delegate to the the forecast model calculation procedure.
   EVENT_PKG.findForecastedDeadDtFcModel(
         aAircraftDbId,
         aAircraftId,
         aDataTypeDbId,
         aDataTypeId,
         lCurrentDate,
         lRemainingUsageQt,
         aForecastDt,
         ol_Return );

    -- process successfully?
    IF ol_Return < 0 THEN
       RETURN;
    END IF;

    -- store our exact deadline date for return
    aStartDt := aForecastDt;

    /* round the deadlines to the end of the previous */
    aForecastDt := TO_DATE(TO_CHAR(aForecastDt-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');

    -- flag our procecure as beign processed successfully
    ol_Return := icn_Success;

    -- exception handling
    EXCEPTION
       WHEN OTHERS THEN
          ol_Return := -100;
          APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastedDeadDt_new@@@' || SQLERRM);
          RETURN;
END findForecastedDeadDt_new;

/********************************************************************************
*
* PROCEDURE:      findForecastedDeadDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aRemainingUsageQt (IN NUMBER): amount of remaining usage before the deadline is due
*                 aStartDate (IN OUT DATE): the relative date to perform our calculations from.
*                    Also used to return the exact deadline date.
*                 aForecastDt (OUT DATE): the date that the deadline will become due.
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted deadline date for a deadline with the specified datatype on
*                 the given aircraft. The forecasted deadline date is calculated using the aircrafts
*                 flight plan and forecast model.
*
* Orig.Coder:     Neil Ouellette
* Recent Coder:   N/A
* Recent Date:    Dec 19, 2006
*
*********************************************************************************
*
* Copyright 1998-2006 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE findForecastedDeadDt(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aStartDt IN OUT date,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      ol_Return OUT NUMBER
)
IS
   lCurrentDate date;
   lRemainingUsageQt evt_sched_dead.usage_rem_qt%type;
BEGIN
   -- Initialize the return value
   ol_Return := icn_NoProc;

   -- initialize our 'current date' to the sysdate and our remaining usage counter to that specified in the args
   lCurrentDate := aStartDt;
   lRemainingUsageQt := aRemainingUsageQt;

   -- if the remaining usage is negative then this task is overdue.
   -- set the forecast date to the end of the previous day and return.
   IF lRemainingUsageQt <= 0 THEN
      aForecastDt := TO_DATE(TO_CHAR(aStartDt-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');
      ol_Return := icn_Success;
      RETURN;
   END IF;

   -- if we are dealing with a datatype that requires the flight plan to compute the forecasted
   -- date, then delegate to the flight plan calculation procedure.
   -- we use the flight plan calcuations for CYCLES, LANDINGS, or HOURS
   IF( aDataTypeDbId = 0 AND ( aDataTypeId = 1 OR aDataTypeId = 10 OR aDataTypeId = 30 ) ) THEN
      EVENT_PKG.findForecastedDeadDtFlightPlan(
            aAircraftDbId,
            aAircraftId,
            aDataTypeDbId,
            aDataTypeId,
            lCurrentDate,
            lRemainingUsageQt,
            aForecastDt,
            ol_Return );

       -- process successfully?
       IF ol_Return < 0 THEN
          RETURN;
       END IF;

       -- if the forecast date was found to be within the flight plan, return it
       IF aForecastDt IS NOT NULL THEN
          -- set our pointer to the found date
          aStartDt := aForecastDt;

          -- return
          ol_Return := icn_Success;
          RETURN;
       END IF;

   END IF;

   -- if we got here, either the forecasted date was not found within the flight plan or the
   -- datatype was not one that required flight plan calculations.
   -- Delegate to the the forecast model calculation procedure.
   EVENT_PKG.findForecastedDeadDtFcModel(
         aAircraftDbId,
         aAircraftId,
         aDataTypeDbId,
         aDataTypeId,
         lCurrentDate,
         lRemainingUsageQt,
         aForecastDt,
         ol_Return );

    -- process successfully?
    IF ol_Return < 0 THEN
       RETURN;
    END IF;

    -- store our exact deadline date for return
    aStartDt := aForecastDt;

    /* round the deadlines to the end of the previous */
    aForecastDt := TO_DATE(TO_CHAR(aForecastDt-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');

    -- flag our procecure as beign processed successfully
    ol_Return := icn_Success;

    -- exception handling
    EXCEPTION
       WHEN OTHERS THEN
          ol_Return := -100;
          APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastedDeadDt@@@' || SQLERRM);
          RETURN;
END findForecastedDeadDt;


/********************************************************************************
*
* Procedure:      SetTaskDrivingDeadline
* Arguments:      aEventDbId (IN NUMBER): pk of the task having it's deadline updated
*                 aEventId (IN NUMBER): pk of the task having it's deadline updated
*                 aLastSchedBool (IN NUMBER): whether the task has last_sched_bool set to true.
*                 on_Return (out NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to set flag the driving deadline on a given task
*
* Orig.Coder:     nso
* Recent Coder:
* Recent Date:    Jan 04, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
procedure SetTaskDrivingDeadline(
          aEventDbId IN typn_Id,
          aEventId IN typn_Id,
          aLastSchedBool IN NUMBER,
          on_Return OUT NUMBER )
IS
   ls_Query          VARCHAR2(32767);
   lc_Cursor         SYS_REFCURSOR;
   ln_DataTypeDbId   mim_data_type.data_type_db_id%TYPE;
   ln_DataTypeId     mim_data_type.data_type_id%TYPE;
   lb_IsPositive     NUMBER;

BEGIN

    on_Return := 0;

     ls_Query :=
     'SELECT                                                         ' ||
     '       evt_sched_dead.data_type_db_id,                         ' ||
     '       evt_sched_dead.data_type_id,                            ' ||
     '       CASE WHEN evt_sched_dead.usage_rem_qt < 0               ' ||
     '            THEN 0                                             ' ||
     '            ELSE 1                                             ' ||
     '       END AS is_positive                                      ' ||  
     'FROM                                                           ' ||
     '       evt_sched_dead                                          ' ||
     'WHERE                                                          ' ||
     '       evt_sched_dead.event_db_id   = ' ||  aEventDbId || ' AND' ||
     '       evt_sched_dead.event_id      = ' ||  aEventId ;
     
   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
     IF aLastSchedBool = 1 THEN
        ls_Query := ls_Query || ' ORDER BY evt_sched_dead.sched_dead_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC';
     ELSE
        ls_Query := ls_Query || ' ORDER BY is_positive, evt_sched_dead.sched_dead_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC';     
     END IF;
      
     OPEN lc_Cursor FOR ls_Query ;
     FETCH lc_Cursor INTO ln_DataTypeDbId ,
                          ln_DataTypeId   ,
                          lb_IsPositive   ;
     IF lc_Cursor%FOUND THEN

       /* clear sched_driver_bool for the non-driving deadline that used to be the driver */
       UPDATE evt_sched_dead
       SET    sched_driver_bool = 0
       WHERE  event_db_id = aEventDbId AND
              event_id    = aEventId
              AND
              sched_driver_bool <> 0 AND
              NOT ( data_type_db_id = ln_DataTypeDbId AND
                    data_type_id    = ln_DataTypeId
                  );
    
       /* set sched_driver_bool in the new driving deadline */
       UPDATE evt_sched_dead
       SET    sched_driver_bool = 1
       WHERE  event_db_id = aEventDbId AND 
              event_id    = aEventId
              AND
              sched_driver_bool <> 1
              AND
              data_type_db_id = ln_DataTypeDbId AND
              data_type_id    = ln_DataTypeId;

     END IF;
     CLOSE lc_Cursor;

   /* return success */
   on_Return := icn_Success;

-- exception handling
EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetTaskDrivingDeadline@@@' || SQLERRM);
      IF lc_Cursor%ISOPEN THEN CLOSE lc_Cursor; END IF;      
      RETURN;
END SetTaskDrivingDeadline;

/********************************************************************************
*
* Procedure:      UpdateDeadline
* Arguments:      al_EventDbId (IN NUMBER): The event to be updated
*                 al_EventId (IN NUMBER):   ""
*                 aHighestInvDbId: Event highest inventory
*                 aHighestInvId:   Event highest inventory
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to update an event's priorities and
*                 deadlines. Similar to the other UpdateDeadline method except
*                 this method takes the highest inventory as an argument.
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Ling Soh
* Recent Date:    Dec 20, 2006
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadline(
        al_EventDbId    IN typn_Id,
        al_EventId      IN typn_Id,
        aHighestInvDbId   IN inv_inv.inv_no_db_id%type,
        aHighestInvId     IN inv_inv.inv_no_id%type,
        ol_Return       OUT NUMBER
   ) IS

   /* local variables */
   ln_IsHistoric           NUMBER;
   ldt_ScheduledDeadlineDt typdt_Dt;
   lf_ScheduledDeadlineQt  typf_Qt;
   lf_UsageRemQt           typf_Qt;
   lf_PredDaysRemQt        typf_Qt;
   ln_IsSuspend            NUMBER;
   ln_FromLast             NUMBER;
   ln_RemainingUsage       NUMBER;
   ln_IsLocked             NUMBER;
   ln_IsAircraft           NUMBER;

   /* cursor declarations */
   /* used to get task initial task deadline info */
   CURSOR lcur_EventDead (
         cl_EventDbId   typn_Id,
         cl_EventId     typn_Id
      ) IS
      SELECT evt_sched_dead.data_type_db_id,
             evt_sched_dead.data_type_id,
             evt_sched_dead.sched_dead_qt,
             evt_sched_dead.sched_dead_dt,
             evt_sched_dead.usage_rem_qt,
             evt_sched_dead.deviation_qt,
             evt_sched_dead.notify_qt,
             mim_data_type.domain_type_cd,
             ref_eng_unit.ref_mult_qt,
             mim_data_type.eng_unit_cd,
             mim_data_type.data_type_cd
        FROM evt_sched_dead,
             mim_data_type,
             ref_eng_unit
       WHERE evt_sched_dead.event_db_id = cl_EventDbId AND
             evt_sched_dead.event_id    = cl_EventId
             AND
             mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
             mim_data_type.data_type_id    = evt_sched_dead.data_type_id
             AND
             ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
             ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd;

   lrec_EventDead lcur_EventDead%ROWTYPE;

   /* get tsn_qt and forecast rate for the current inventory */
   CURSOR lcur_CurrUsage (
         cl_EventDbId      typn_Id,
         cl_EventId        typn_Id,
         cl_DataTypeDbId   typn_Id,
         cl_DataTypeId     typn_Id
      ) IS
      SELECT inv_curr_usage.tsn_qt
        FROM inv_curr_usage,
             evt_inv
       WHERE evt_inv.event_db_id   = cl_EventDbId AND
             evt_inv.event_id      = cl_EventId AND
             evt_inv.main_inv_bool = 1
             AND
             inv_curr_usage.inv_no_db_id    = evt_inv.inv_no_db_id AND
             inv_curr_usage.inv_no_id       = evt_inv.inv_no_id AND
             inv_curr_usage.data_type_db_id = cl_DataTypeDbId AND
             inv_curr_usage.data_type_id    = cl_DataTypeId;

   lrec_CurrUsage lcur_CurrUsage%ROWTYPE;

   INV_CURR_USAGE_NOT_FOUND EXCEPTION;

   lStartDt date;
BEGIN

   -- Initialize the return value
   ol_Return := icn_NoProc;

   /* Get task historic and suspend status */
   SELECT evt_event.hist_bool,
          DECODE(evt_event.event_status_db_id, 1,
                 DECODE(evt_event.event_status_cd,'SUSPEND', 1, 0),
                 0 )
     INTO
         ln_IsHistoric, ln_IsSuspend
     FROM
        evt_event
     WHERE
         evt_event.event_db_id = al_EventDbId ANd
         evt_event.event_id    = al_EventId   AND
         evt_event.rstat_cd	= 0;

   /* Return if task is historic */
   IF (ln_IsHistoric = 1 )  THEN
      ol_Return := icn_Success;
      RETURN;
   END IF;


   /* get highest inventory lock and class status */
   SELECT
          inv_inv.locked_bool,
          DECODE(inv_inv.inv_class_db_id, 0,
                 DECODE(inv_inv.inv_class_cd,'ACFT', 1, 0),
                 0 )
     INTO
          ln_IsLocked, ln_IsAircraft
     FROM
          inv_inv
     WHERE
           inv_inv.inv_no_db_id = aHighestInvDbId AND
           inv_inv.inv_no_id    = aHighestInvId   AND
           inv_inv.rstat_cd	= 0;

   /* if highest inventory is locked, return */
   IF (ln_IsLocked = 1) THEN
      ol_Return := icn_Success;
      RETURN;
   END IF;

    /* Get last_sched_dead_bool of task */
   SELECT count(*)
     INTO ln_FromLast
     FROM
         task_task,
         sched_stask
     WHERE
         sched_stask.sched_db_id = al_EventDbId AND
         sched_stask.sched_id    = al_EventId	AND
         sched_stask.rstat_cd	 = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         task_task.last_sched_dead_bool = 1;


   /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
   IF IsOrphanedForecastedTask(al_EventDbId, al_EventId) = 1 THEN
      ol_Return := icn_Success;
      RETURN;
   END IF;

   /* loop for every deadline defined for this task */
   FOR lrec_EventDead IN lcur_EventDead(al_EventDbId, al_EventId) LOOP

      /* *** CALENDAR-BASED PARAMETER *** */
      IF lrec_EventDead.domain_type_cd = 'CA' THEN

         /* if the task is suspended, the update the scheduled deadline date */
         IF ln_IsSuspend = 1 THEN

            /* Calculate predicted days remaining */
            lf_PredDaysRemQt := lRec_EventDead.Usage_Rem_Qt * lrec_EventDead.ref_mult_qt;

            /* "push out" the scheduled deadline date */
            ldt_ScheduledDeadlineDt := SYSDATE + lf_PredDaysRemQt;

            /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
            IF NOT UPPER(lrec_EventDead.data_type_cd) = 'CHR' THEN
               ldt_ScheduledDeadlineDt := TO_DATE(TO_CHAR(ldt_ScheduledDeadlineDt, 'DD-MON-YYYY ')||'23:59:59',
               'DD-MON-YYYY HH24:MI:SS');
            END IF;

            /* do not change any of the other columns */
            lf_ScheduledDeadlineQt := lrec_EventDead.sched_dead_qt;
            lf_UsageRemQt          := lrec_EventDead.usage_rem_qt;

         /* if the task is not suspended, then update the "usage remaining" columns */
         ELSE

            /* do not change the existing scheduled deadlines */
            ldt_ScheduledDeadlineDt := lrec_EventDead.sched_dead_dt;
            lf_ScheduledDeadlineQt  := lrec_EventDead.sched_dead_qt;

           /* if data type CLMON, than scheduled deadline should be at the end of the month */
            IF UPPER(lrec_EventDead.data_type_cd) = 'CLMON' THEN
               -- set the deadline to the last day of the month
               ldt_ScheduledDeadlineDt := LAST_DAY(ldt_ScheduledDeadlineDt);
            END IF;

            /* get the various usage remaining values */
            lf_UsageRemQt := (ldt_ScheduledDeadlineDt - SYSDATE) / lrec_EventDead.ref_mult_qt;

            /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
            IF NOT UPPER(lrec_EventDead.data_type_cd) = 'CHR' THEN
               ldt_ScheduledDeadlineDt := TO_DATE(TO_CHAR(ldt_ScheduledDeadlineDt, 'DD-MON-YYYY ')||'23:59:59',
               'DD-MON-YYYY HH24:MI:SS');
            END IF;

            /* calculate the predicted number of days remaining */
            lf_PredDaysRemQt := ldt_ScheduledDeadlineDt + ( lrec_EventDead.deviation_qt * lrec_EventDead.ref_mult_qt )  - SYSDATE;

         END IF;


      /* *** USAGE-BASED PARAMETER *** */
      /* Only update usage based deadline if highest is an aircraft */
      ELSIF (lrec_EventDead.domain_type_cd = 'US') THEN

         /* get the current usage of the given usage parameter */
         OPEN lcur_CurrUsage(al_EventDbId,
                             al_EventId,
                             lrec_EventDead.data_type_db_id,
                             lrec_EventDead.data_type_id);

         -- get the current usage
         FETCH lcur_CurrUsage INTO lrec_CurrUsage;

         -- we must have a current usage value to do any sort of calculations
         IF lcur_CurrUsage%NOTFOUND THEN
            CLOSE lcur_CurrUsage;         
            RAISE INV_CURR_USAGE_NOT_FOUND;
         END IF;

         -- close our cursor
         CLOSE lcur_CurrUsage;

         /* if the task is suspended, then update the scheduled deadline usage */
         IF ln_IsSuspend = 1 THEN

            /* "push out" the scheduled deadline usage */
            lf_ScheduledDeadlineQt := lrec_CurrUsage.tsn_qt + lrec_EventDead.usage_rem_qt;

            /* do not change any of the other columns */
            ldt_ScheduledDeadlineDt := lrec_EventDead.sched_dead_dt;
            lf_UsageRemQt          := lrec_EventDead.usage_rem_qt;
            lf_PredDaysRemQt := (ldt_ScheduledDeadlineDt - SYSDATE);

         /* if the task is not suspended, then update the "usage remaining" columns */
         ELSE

            /* do not change the existing scheduled deadlines */
            lf_ScheduledDeadlineQt := lrec_EventDead.sched_dead_qt;

            /* clear the predicted deadline date */
            ldt_ScheduledDeadlineDt := NULL;
            lf_PredDaysRemQt := NULL;

            /* get the various usage remaining values */
            lf_UsageRemQt := lrec_EventDead.sched_dead_qt - lrec_CurrUsage.tsn_qt;

         END IF;

         ln_RemainingUsage := lrec_EventDead.sched_dead_qt + lrec_EventDead.deviation_qt - lrec_CurrUsage.tsn_qt;

         IF (ln_IsAircraft = 1) THEN
             -- Reset the start day to now, so if you have multiple deadlines 
             -- the start date does not get increased between them
             lStartDt := sysdate;
             /* calculate the predicted deadline date */
             findForecastedDeadDt( aHighestInvDbId,
                                   aHighestInvId,
                                   lrec_EventDead.data_type_db_id,
                                   lrec_EventDead.data_type_id,
                                   ln_RemainingUsage,
                                   lStartDt,
                                   ldt_ScheduledDeadlineDt,
                                   ol_Return);
            IF ol_Return < 0 THEN
                  RETURN;
            END IF;

            /* update predicted number of days remaining */
            lf_PredDaysRemQt := (ldt_ScheduledDeadlineDt - SYSDATE);
         END IF;

      END IF;

      /* update the evt_sched_dead */
      UPDATE evt_sched_dead
         SET
             sched_dead_dt    = ldt_ScheduledDeadlineDt,
             sched_dead_dt_last_updated = SYSDATE,
             sched_dead_qt    = lf_ScheduledDeadlineQt,
             usage_rem_qt     = lf_UsageRemQt
       WHERE
             event_db_id = al_EventDbId
         AND event_id    = al_EventId
         AND data_type_db_id = lrec_EventDead.data_type_db_id
         AND data_type_id    = lrec_EventDead.data_type_id
         AND (
             -- dont update if all the values are the same
             NOT (
                NVL( sched_dead_dt, SYSDATE ) = NVL( ldt_ScheduledDeadlineDt, SYSDATE )
                AND sched_dead_qt    = lf_ScheduledDeadlineQt
                AND usage_rem_qt     = lf_UsageRemQt
             )
             -- update if any of the current values are null
             OR sched_dead_dt IS NULL
             OR sched_dead_qt IS NULL
             OR usage_rem_qt  IS NULL
         );


   END LOOP;

   SetTaskDrivingDeadline(al_EventDbId, al_EventId, ln_FromLast, ol_Return);
   IF ol_Return < 0 THEN
      RETURN;
   END IF;

   /* Update the priority of the event */
   SetPriority(al_EventDbId, al_EventId, ol_Return);
   IF ol_Return < 0 THEN
      RETURN;
   END IF;

   /*Update the driving deadline for the event tree that the event is a part of*/
   EVENT_PKG.UPDATEDRIVINGDEADLINE( al_EventDbId, al_EventId, ol_Return );
   IF ol_Return < 0 THEN
      RETURN;
   END IF;

   EVENT_PKG.SyncPRRequiredByDt( al_EventDbId, al_EventId, ol_Return);

    IF ol_Return < 0 THEN
      RETURN;
    END IF;

   /* return success */
   ol_Return := icn_Success;

EXCEPTION
   WHEN INV_CURR_USAGE_NOT_FOUND THEN
      ol_Return := icn_InvCurrUsageNotFound;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadline@@@' || 'INV_CURR_USAGE not found.' );
      IF lcur_CurrUsage%ISOPEN THEN CLOSE lcur_CurrUsage; END IF;      
      RETURN;
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadline@@@' || SQLERRM);
      IF lcur_CurrUsage%ISOPEN THEN CLOSE lcur_CurrUsage; END IF;            
      RETURN;
END UpdateDeadline;


/********************************************************************************
*
* Procedure:      UpdateDeadline
* Arguments:      al_EventDbId (IN NUMBER): The event to be updated
*                 al_EventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to update an event's priorities and
*                 deadlines
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Ling Soh
* Recent Date:    Dec 20, 2006
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadline(
        al_EventDbId    IN typn_Id,
        al_EventId      IN typn_Id,
        ol_Return       OUT NUMBER
   ) IS

   /* local variables */
   ln_AircraftDbId         inv_ac_reg.inv_no_db_id%type;
   ln_AircraftId           inv_ac_reg.inv_no_id%type;

BEGIN

   -- Initialize the return value
   ol_Return := icn_NoProc;

   /* Get highest inventory */
   SELECT
        highest_inv.inv_no_db_id, highest_inv.inv_no_id
     INTO
        ln_AircraftDbId, ln_AircraftId
     FROM
        evt_event,
        evt_inv,
        inv_inv highest_inv
     WHERE
         evt_event.event_db_id = al_EventDbId AND
         evt_event.event_id    = al_EventId   AND
         evt_event.rstat_cd	= 0
         AND
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
         AND
         evt_inv.main_inv_bool = 1
         AND
         highest_inv.inv_no_db_id = evt_inv.h_inv_no_db_id AND
         highest_inv.inv_no_id    = evt_inv.h_inv_no_id	   AND
         highest_inv.rstat_cd	= 0;


   /*Update the deadline given aircraft key*/
   UpdateDeadline( al_EventDbId,
                   al_EventId,
                   ln_AircraftDbId,
                   ln_AircraftId,
                   ol_Return );
   IF ol_Return < 0 THEN
      RETURN;
   END IF;

   /* return success */
   ol_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadline@@@' || SQLERRM);
      RETURN;
END UpdateDeadline;


/********************************************************************************
*
* Procedure:      IsInstalledOnAircraft
* Arguments:      aInvNoDbId (IN NUMBER): pk of the inventory to check
*                 aInvNoId (IN NUMBER): ""
* Description:    Tests whether the given inventory is installed on an aircraft
*
* Orig.Coder:     nso
* Recent Coder:   nso
* Recent Date:    Jan 5, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsInstalledOnAircraft( aInvNoDbId IN typn_Id, aInvNoId   IN typn_Id ) RETURN NUMBER
IS
   lClassDbId inv_inv.inv_class_db_id%type;
   lClassCd   inv_inv.inv_class_cd%type;
BEGIN
   SELECT
      h_inv_inv.inv_class_db_id,
      h_inv_inv.inv_class_cd
   INTO
      lClassDbId,
      lClassCd
   FROM
      inv_inv,
      inv_inv h_inv_inv
   WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId	AND
      inv_inv.rstat_cd	   = 0
      AND
      h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id    AND
      h_inv_inv.rstat_cd     = 0;

   /* Must be an aircraft */
   IF (lClassDbId = 0) AND (lClassCd = 'ACFT') THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;
END IsInstalledOnAircraft;



/********************************************************************************
*
* Procedure:      IsLocked
* Arguments:      aInvNoDbId (IN NUMBER): pk of the inventory to check
*                 aInvNoId (IN NUMBER): ""
* Description:    Tests whether the given inventory is locked
*
* Orig.Coder:     nso
* Recent Coder:   nso
* Recent Date:    Jan 5, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsLocked( aInvNoDbId IN typn_Id, aInvNoId IN typn_Id ) RETURN NUMBER
IS
   lLocked number;
BEGIN
   SELECT
      inv_inv.locked_bool
   INTO
      lLocked
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId	AND
      inv_inv.rstat_cd	   = 0;

   -- return the locked bool
   RETURN lLocked;
END IsLocked;

/********************************************************************************
*
* Procedure:      IsInstalledOnAircraft
* Arguments:      aInvNoDbId (IN NUMBER): pk of the inventory to check
*                 aInvNoId (IN NUMBER): ""
*                 aAircraftDbId (OUT NUMBER): if the inventory is installed on aircraft
*                 aAircraftId (OUT NUMBER): this is aicraft's PK.
* Description:    Tests whether the given inventory is installed on an aircraft, and returns the aircraft PK
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   
* Recent Date:    Mar 27, 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsInstalledOnAircraft(aInvNoDbId IN typn_Id, aInvNoId IN typn_Id, aAircraftDbId OUT typn_Id, aAircraftId OUT typn_Id) RETURN NUMBER
IS
     lHighestInvNoDbId typn_Id;
     lHighestInvNoId typn_Id;
     lIsInstalledOnAircraft number;
BEGIN
   SELECT
     h_inv_inv.inv_no_db_id,
     h_inv_inv.inv_no_id,
     DECODE( h_inv_inv.inv_class_db_id, 0, DECODE( h_inv_inv.inv_class_cd, 'ACFT', 1, 0 ), 0 ) as is_highest_aircraft
   INTO
     lHighestInvNoDbId,
     lHighestInvNoId,
     lIsInstalledOnAircraft
   FROM
     inv_inv,
     inv_inv h_inv_inv
   WHERE
     inv_inv.inv_no_db_id = aInvNoDbId AND
     inv_inv.inv_no_id    = aInvNoId   AND
     inv_inv.rstat_cd	  = 0
     and
     h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
     h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id    AND
     h_inv_inv.rstat_cd     = 0;
  IF lIsInstalledOnAircraft=1 THEN
     aAircraftDbId := lHighestInvNoDbId;
     aAircraftId := lHighestInvNoId;
  END IF;
  RETURN lIsInstalledOnAircraft;
END IsInstalledOnAircraft;

/********************************************************************************
*
* Procedure:      UpdateUSDeadlinesForInvTree
* Arguments:      aInventoryDbId (IN NUMBER): pk of the inventory having it's deadlines updated
*                 aInventoryId (IN NUMBER): pk of the inventory having it's deadlines updated
*                 on_Return (out NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to recalculate the usage based deadlines on
*                 any tasks against the given inventory or sub-inventory.
*
* Orig.Coder:     nso
* Recent Coder:
* Recent Date:    Jan 04, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateUSDeadlinesForInvTree(
          aInventoryDbId IN inv_inv.inv_no_db_id%type,
          aInventoryId   IN inv_inv.inv_no_id%type,
          on_Return      OUT NUMBER
)
IS
   -- all usage based deadlines against the inventory and sub-inventory
   CURSOR lCurDeadlines (
         aInvNoDbId    typn_Id,
         aInvNoId      typn_Id,
         aDataTypeDbId typn_Id,
         aDataTypeId   typn_Id
      )
   IS
      SELECT
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_status_cd,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.sched_dead_dt_last_updated,
         evt_sched_dead.sched_dead_qt,
         evt_sched_dead.usage_rem_qt,
         evt_sched_dead.deviation_qt,
         inv_curr_usage.tsn_qt,
         evt_sched_dead.sched_from_cd,
         evt_sched_dead.start_dt,
         evt_sched_dead.interval_qt
      FROM
         evt_inv,
         evt_inv main_evt_inv,
         evt_event,
         evt_sched_dead,
         mim_data_type,
         inv_curr_usage
      WHERE
         ( evt_inv.inv_no_db_id, evt_inv.inv_no_id ) IN
         ( SELECT
              inv_no_db_id,
              inv_no_id
           FROM
              inv_inv   WHERE rstat_cd = 0 
           START WITH
              ( inv_no_db_id = aInvNoDbId AND inv_no_id = aInvNoId )
           CONNECT BY
              nh_inv_no_db_id = PRIOR inv_no_db_id AND
              nh_inv_no_id    = PRIOR inv_no_id
         )
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id    = evt_inv.event_id AND
         evt_event.hist_bool   = 0
         and
         evt_event.event_status_cd <> 'FORECAST'	AND
         evt_event.rstat_cd    = 0
         and
         main_evt_inv.event_db_id = evt_event.event_db_id and
         main_evt_inv.event_id    = evt_event.event_id and
         main_evt_inv.main_inv_bool = 1
         AND
         evt_sched_dead.event_db_id = evt_event.event_db_id AND
         evt_sched_dead.event_id    = evt_event.event_id
         and
         mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id and
         mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         and
         mim_data_type.domain_type_cd = 'US'
         and
         mim_data_type.data_type_db_id = aDataTypeDbId and
         mim_data_type.data_type_id    = aDataTypeId
         and
         inv_curr_usage.inv_no_db_id = main_evt_inv.inv_no_db_id and
         inv_curr_usage.inv_no_id    = main_evt_inv.inv_no_id and
         inv_curr_usage.data_type_db_id = mim_data_type.data_type_db_id and
         inv_curr_usage.data_type_id    = mim_data_type.data_type_id
      ORDER BY
         evt_sched_dead.usage_rem_qt + evt_sched_dead.deviation_qt;

   lRecDeadline lCurDeadlines%ROWTYPE;

   -- usage based data types
   CURSOR lCurDataTypes
   IS
      select
          mim_data_type.data_type_db_id,
          mim_data_type.data_type_id
      from
         mim_data_type
      where
         mim_data_type.domain_type_cd = 'US';
   lRecDataType lCurDataTypes%ROWTYPE;

   lCurrentDate DATE;
   lTsnQt NUMBER;
   lUsageRemExtended NUMBER;
   lUsageRem NUMBER;
   lSchedDeadQt NUMBER;
   lSchedDeadDt DATE;

   lHighestInvNoDbId NUMBER;
   lHighestInvNoId NUMBER;
   lIsInstalledOnAircraft NUMBER;
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   -- we need to know if this inventory is installed on an aircraft, and if so, the aircraft pk
   lIsInstalledOnAircraft := IsInstalledOnAircraft(aInventoryDbId, aInventoryId, lHighestInvNoDbId, lHighestInvNoId);

   -- loop through each data type
   FOR lRecDataType IN lCurDataTypes LOOP

      -- loop through each deadline
      FOR lRecDeadline IN lCurDeadlines( aInventoryDbId,
                                         aInventoryId,
                                         lRecDataType.data_type_db_id,
                                         lRecDataType.data_type_id ) LOOP
         -- if the task is suspended just push the deadline forward
         IF lRecDeadline.event_status_cd = 'SUSPEND' THEN
            lSchedDeadQt := lRecDeadline.tsn_qt + lRecDeadline.usage_rem_qt;
            lUsageRem := lRecDeadline.usage_rem_qt;
         -- the task is not suspended
         ELSE
            lSchedDeadQt := lRecDeadline.sched_dead_qt;
            lUsageRem := lRecDeadline.sched_dead_qt - lRecDeadline.tsn_qt;
         END IF;


         -- the usage remaining until the extended deadline (use this for sched_dead_dt)
         lUsageRemExtended := lRecDeadline.sched_dead_qt + lRecDeadline.deviation_qt - lRecDeadline.tsn_qt;

          -- If based on effective date and the effective date in future we need to calculate the start_qt
          IF lRecDeadline.Sched_From_Cd='EFFECTIV' THEN
             IF lRecDeadline.Start_Dt>SYSDATE THEN
                IF lIsInstalledOnAircraft = 1 THEN
                    PredictUsageBetweenDt(lHighestInvNoDbId,
                                          lHighestInvNoId,
                                          lRecDataType.data_type_db_id,
                                          lRecDataType.data_type_id,
                                          SYSDATE,
                                          lRecDeadline.Start_Dt,
                                          lRecDeadline.Tsn_Qt,
                                          lTsnQt,
                                          on_Return);
                    IF on_Return < 0 THEN
                        RETURN;
                    END IF;
                ELSE
                    prep_deadline_pkg.GetCurrentInventoryUsage( 
                                          lRecDeadline.Event_Db_Id,
                                          lRecDeadline.Event_Id,
                                          lRecDataType.Data_Type_Db_Id,
                                          lRecDataType.Data_Type_Id,
                                          lTsnQt,
                                          on_Return);
                    IF on_Return < 0 THEN
                        RETURN;
                    END IF;
                END IF;
             ELSE
                 prep_deadline_pkg.GetHistoricUsageAtDt(
                    lRecDeadline.Start_Dt,
                    lRecDataType.data_type_db_id,
                    lRecDataType.data_type_id,
                    aInventoryDbId,
                    aInventoryId,
                    lTsnQt,
                    on_Return);
                IF on_Return < 0 THEN
                   RETURN;
                END IF;
  
             END IF;
  
             lSchedDeadQt := lTsnQt+lRecDeadline.Interval_Qt;
             lUsageRem := lSchedDeadQt-lRecDeadline.Tsn_Qt;
             lUsageRemExtended := lUsageRem + lRecDeadline.deviation_qt;
  
             UPDATE
                evt_sched_dead
             SET
                evt_sched_dead.start_qt = lTsnQt
             WHERE
                event_db_id = lRecDeadline.event_db_id AND
                event_id    = lRecDeadline.event_id
                AND
                data_type_db_id = lRecDeadline.data_type_db_id AND
                data_type_id    = lRecDeadline.data_type_id
                AND NOT ( start_qt = lTsnQt );
          END IF;
         -- can we calculate the new predicted deadline date using a forecast model?
         IF lIsInstalledOnAircraft = 1 THEN
            -- reset start date
            lCurrentDate := SYSDATE;
            findForecastedDeadDt( lHighestInvNoDbId,
                                  lHighestInvNoId,
                                  lRecDataType.data_type_db_id,
                                  lRecDataType.data_type_id,
                                  lUsageRemExtended,
                                  lCurrentDate,
                                  lSchedDeadDt,
                                  on_Return);
            IF on_Return < 0 THEN
               RETURN;
            END IF;
         -- no forecast model - cant calculate the predicted deadline date -> clear it
         ELSE
            lSchedDeadDt := NULL;
         END IF;

        /* update the evt_sched_dead */
        UPDATE
           evt_sched_dead
        SET
           sched_dead_dt              = lSchedDeadDt,
           sched_dead_dt_last_updated = SYSDATE,
           sched_dead_qt              = lSchedDeadQt,
           usage_rem_qt               = lUsageRem
        WHERE
           event_db_id = lRecDeadline.event_db_id and
           event_id    = lRecDeadline.event_id
           and
           data_type_db_id = lRecDeadline.data_type_db_id and
           data_type_id    = lRecDeadline.data_type_id
           AND (
               NOT(
                  ( sched_dead_dt IS NOT NULL AND sched_dead_dt = lSchedDeadDt AND lSchedDeadDt IS NOT NULL )
                   AND sched_dead_qt = lSchedDeadQt
                   AND usage_rem_qt  = lUsageRem
                   )
               OR sched_dead_dt IS NULL
               OR sched_dead_qt IS NULL
               OR usage_rem_qt  IS NULL
           )
         ;


        -- update the deadlines on the dependant tasks (FORECAST)
      prep_deadline_pkg.UpdateDependentDeadlines( lRecDeadline.event_db_id,
                                                  lRecDeadline.event_id,
                                                  on_Return);
      IF on_Return < 0 THEN RETURN; END IF;

      END LOOP; -- deadline

   END LOOP; -- data type

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateUSDeadlinesForInvTree@@@' || SQLERRM);
      RETURN;
end UpdateUSDeadlinesForInvTree;

/********************************************************************************
*
* Procedure:      UpdateCADeadlinesForInvTree
* Arguments:      aInventoryDbId (IN NUMBER): pk of the inventory having it's deadlines updated
*                 aInventoryId (IN NUMBER): pk of the inventory having it's deadlines updated
*                 on_Return (out NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to recalculate the calendar based deadlines on
*                 any tasks against the given inventory or sub-inventory.
*
* Orig.Coder:     nso
* Recent Coder:
* Recent Date:    Jan 04, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateCADeadlinesForInvTree (
   aInventoryDbId IN typn_Id,
   aInventoryId   IN typn_Id,
   on_Return      OUT NUMBER
) IS

   -- all calendar based deadlines
   CURSOR lCurDeadlines (
         aInvNoDbId    typn_Id,
         aInvNoId      typn_Id
      ) IS
      SELECT
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_status_cd,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.sched_dead_qt,
         evt_sched_dead.usage_rem_qt,
         ref_eng_unit.ref_mult_qt,
         mim_data_type.data_type_cd
      FROM
         evt_inv,
         evt_event,
         evt_sched_dead,
         mim_data_type,
         ref_eng_unit
      WHERE
         ( evt_inv.inv_no_db_id, evt_inv.inv_no_id ) IN
         ( SELECT
              inv_no_db_id,
              inv_no_id
           FROM
              inv_inv WHERE rstat_cd = 0
           START WITH
              ( inv_no_db_id = aInvNoDbId AND inv_no_id = aInvNoId )
           CONNECT BY
              nh_inv_no_db_id = PRIOR inv_no_db_id AND
              nh_inv_no_id    = PRIOR inv_no_id
         )
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id    = evt_inv.event_id AND
         evt_event.hist_bool   = 0
         and
         evt_event.event_status_cd <> 'FORECAST'	AND
         evt_event.rstat_cd	= 0
         AND
         evt_sched_dead.event_db_id = evt_event.event_db_id AND
         evt_sched_dead.event_id    = evt_event.event_id
         and
         mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id and
         mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         and
         mim_data_type.domain_type_cd = 'CA'
         and
         ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id and
         ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd;

   lRecDeadline lCurDeadlines%ROWTYPE;

   lSchedDeadDt date;
   lSchedDeadQt float;
   lUsageRem float;
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   -- loop through the deadlines
   FOR lRecDeadline IN lCurDeadlines( aInventoryDbId, aInventoryId ) LOOP

      -- suspended task?
      IF lRecDeadline.event_status_cd = 'SUSPEND' THEN

         -- push out the sched deadline date
         lSchedDeadDt := sysdate + lRecDeadline.usage_rem_qt * lRecDeadline.ref_mult_qt;

         -- keep the deadlien qt and usage rem qt the same
         lSchedDeadQt := lRecDeadline.sched_dead_qt;
         lUsageRem := lRecDeadline.usage_rem_qt;

         /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
         IF NOT UPPER(lRecDeadline.data_type_cd) = 'CHR' THEN
            lSchedDeadDt := TO_DATE(TO_CHAR(lSchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
         END IF;

      -- regular task
      ELSE

         -- keep the same scheduled deadline
         lSchedDeadDt := lRecDeadline.sched_dead_dt;
         lSchedDeadQt := lRecDeadline.sched_dead_qt;

         /* if data type CLMON, than scheduled deadline should be at the end of the month */
         IF UPPER(lRecDeadline.data_type_cd) = 'CLMON' THEN
            -- set the deadline to the last day of the month
            lSchedDeadDt := LAST_DAY(lSchedDeadDt);
         END IF;

         -- get the number of days between now and deadline
         lUsageRem := (lSchedDeadDt - SYSDATE) / lRecDeadline.ref_mult_qt;

         /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
         IF NOT UPPER(lRecDeadline.data_type_cd) = 'CHR' THEN
            lSchedDeadDt := TO_DATE(TO_CHAR(lSchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
         END IF;

      END IF;

      /* update the evt_sched_dead */
      UPDATE
         evt_sched_dead
      SET
         sched_dead_dt = lSchedDeadDt,
         sched_dead_dt_last_updated = SYSDATE,
         sched_dead_qt = lSchedDeadQt,
         usage_rem_qt  = lUsageRem
      WHERE
         event_db_id = lRecDeadline.event_db_id and
         event_id    = lRecDeadline.event_id
         and
         data_type_db_id = lRecDeadline.data_type_db_id and
         data_type_id    = lRecDeadline.data_type_id
         AND (
             NOT (
                sched_dead_dt     = lSchedDeadDt
                AND sched_dead_qt = lSchedDeadQt
                AND usage_rem_qt  = lUsageRem
             )
             OR sched_dead_dt IS NULL
             OR sched_dead_qt IS NULL
             OR usage_rem_qt  IS NULL
         );
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateCADeadlinesForInvTree@@@' || SQLERRM);
      RETURN;
END UpdateCADeadlinesForInvTree;

/********************************************************************************
*
* Procedure:      UpdateDeadlineForInvTree
* Arguments:      al_InvNoDbId (IN NUMBER): The top inventory in the tree
*                 al_InvNoId (IN NUMBER): ""
* Description:    Sometimes you need to update all of the deadlines against
*                 inventory in a given tree. If you pass in the top inventory
*                 item, this function will evaluate the deadlines for all
*                 subcomponents.
*
* Orig.Coder:     nso
* Recent Coder:   nso
* Recent Date:    Jan 4, 2007
*
*********************************************************************************
*
* Copyright 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineForInvTree (
   an_InvNoDbId IN typn_Id,
   an_InvNoId   IN typn_Id,
   on_Return    OUT NUMBER
) IS

   -- all non-historic tasks under the given inventory that have deadlines
   CURSOR lCurTasks (
         aInvNoDbId    typn_Id,
         aInvNoId      typn_Id
      ) IS
      SELECT
         evt_event.event_db_id,
         evt_event.event_id,
         decode( task_task.task_db_id, null, 0, task_task.last_sched_dead_bool ) as last_sched_dead_bool
      FROM
         evt_inv,
         evt_event,
         sched_stask,
         task_task
      WHERE
         ( evt_inv.inv_no_db_id, evt_inv.inv_no_id ) IN
         ( SELECT
              inv_no_db_id,
              inv_no_id
           FROM
              inv_inv  WHERE rstat_cd = 0
           START WITH
              ( inv_no_db_id = an_InvNoDbId AND inv_no_id = an_InvNoId )
           CONNECT BY
              nh_inv_no_db_id = PRIOR inv_no_db_id AND
              nh_inv_no_id    = PRIOR inv_no_id
         )
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id    = evt_inv.event_id AND
         evt_event.hist_bool   = 0
         AND
         evt_event.event_status_cd <> 'FORECAST'  AND
         evt_event.rstat_cd	= 0
         AND
         sched_stask.sched_db_id = evt_event.event_db_id and
         sched_stask.sched_id    = evt_event.event_id
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id and
         task_task.task_id    (+)= sched_stask.task_id
         AND EXISTS
           ( SELECT
                1
             FROM
                evt_sched_dead
             WHERE
                evt_sched_dead.event_db_id = evt_event.event_db_id AND
                evt_sched_dead.event_id    = evt_event.event_id )
       ORDER BY
         evt_event.event_db_id, evt_event.event_id;

   lRecTask lCurTasks%ROWTYPE;
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   -- dont update deadlines if the inventory is locked
   IF IsLocked( an_InvNoDbId, an_InvNoId ) = 1 THEN
      /* return success */
      on_Return := icn_Success;
      RETURN;
   END IF;

   -- update the calendar deadlines
   UpdateCADeadlinesForInvTree( an_InvNoDbId, an_InvNoId, on_Return );
   IF on_Return < 0 THEN RETURN; END IF;

   -- update the usage based deadlines
   UpdateUSDeadlinesForInvTree( an_InvNoDbId, an_InvNoId, on_Return );
   IF on_Return < 0 THEN RETURN; END IF;

   -- now loop through the events
   FOR lRecTask IN lCurTasks( an_InvNoDbId, an_InvNoId ) LOOP

      -- flag the proper deadline on this task as the driver
      SetTaskDrivingDeadline(  lRecTask.event_db_id, lRecTask.event_id, lRecTask.last_sched_dead_bool, on_Return );
      IF on_Return < 0 THEN RETURN; END IF;

      /* Update the priority of the event */
      SetPriority( lRecTask.event_db_id, lRecTask.event_id, on_Return );
      IF on_Return < 0 THEN RETURN; END IF;

      /*Update the driving deadline for the event tree that the event is a part of*/
      UpdateDrivingDeadline( lRecTask.event_db_id, lRecTask.event_id, on_Return );
      IF on_Return < 0 THEN RETURN; END IF;

      -- sync the part requests with with task
      SyncPRRequiredByDt( lRecTask.event_db_id, lRecTask.event_id, on_Return );
      IF on_Return < 0 THEN RETURN; END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlineForInvTree@@@' || SQLERRM);
      RETURN;
END UpdateDeadlineForInvTree;

/********************************************************************************
*
* Procedure:      UpdateDeadlineForInv
* Arguments:      al_InvNoDbId (IN NUMBER): The inventory to update
*                 al_InvNoId (IN NUMBER): ""
* Description:    Sometimes you need to update all of the deadlines against
*                 a given inventory item.
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Andrew Hircock
* Recent Date:    Aug 20,1999
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineForInv(
      an_InvNoDbId      typn_Id,
      an_InvNoId        typn_Id,
      on_Return         OUT NUMBER
   ) IS

   ln_HighestInvDbId   typn_Id;
   ln_HighestInvId     typn_Id;

   /* cursor declarations */
   /* return all non-historic events in the inventory tree that have
      scheduled deadlines */
   CURSOR lcur_DeadlineTask (
         cn_InvNoDbId   typn_Id,
         cn_InvNoId     typn_Id
      ) IS
      SELECT evt_event.event_db_id,
             evt_event.event_id
        FROM evt_inv,
             evt_event
       WHERE evt_inv.inv_no_db_id = cn_InvNoDbId AND
             evt_inv.inv_no_id    = cn_InvNoId
             AND
             evt_event.event_db_id = evt_inv.event_db_id AND
             evt_event.event_id    = evt_inv.event_id AND
             evt_event.hist_bool   = 0		      AND
             evt_event.rstat_cd	   = 0
             AND
             EXISTS
             ( SELECT 1
                 FROM evt_sched_dead
                WHERE evt_sched_dead.event_db_id = evt_event.event_db_id AND
                      evt_sched_dead.event_id    = evt_event.event_id );
   lrec_DeadlineTask lcur_DeadlineTask%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* Get highest inventory from current inventory */
   SELECT
          highest_inv.inv_no_db_id, highest_inv.inv_no_id
     INTO
         ln_HighestInvDbId, ln_HighestInvId
     FROM
        inv_inv,
        inv_inv highest_inv
     WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId   AND
         inv_inv.rstat_cd     = 0
         AND
         highest_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
         highest_inv.inv_no_id    = inv_inv.h_inv_no_id	   AND
         highest_inv.rstat_cd	  = 0;


   /* loop through all of the tasks which need updating */
   FOR lrec_DeadlineTask IN lcur_DeadlineTask(an_InvNoDbId, an_InvNoId) LOOP

      /* update the deadlines on the given task */
      UpdateDeadline( lrec_DeadlineTask.event_db_id,
                      lrec_DeadlineTask.event_id,
                      ln_HighestInvDbId,
                      ln_HighestInvId,
                      on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlineForInv@@@' || SQLERRM);
      RETURN;
END UpdateDeadlineForInv;


/********************************************************************************
*
* PROCEDURE:      SetPriority
* Arguments:      al_EventDbId (IN NUMBER): The event whose priority will be determined
*                 al_EventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Procedure to take a supplied event and update it's priority
*                 Evaluates to the highest of:
*                     - Priority as determined by the event's deadline
*                     - the maximum priority of it's children events
*                 This procedure will not update a priority that has been set to
*                 a non-system prioirity (i.e. manually set by the user).
*
* Orig.Coder:     Rob Vandenberg
* Recent Coder:   Andrei Smolko
* Recent Date:    October 19, 2004
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SetPriority(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ol_Return           OUT NUMBER
   ) IS

   /* local variables */
   ll_NHEventDbId       evt_event.nh_event_db_id%TYPE;
   ll_NHEventId         evt_event.nh_event_id%TYPE;
   ll_HistBool          evt_event.hist_bool%TYPE;
   lb_SoftDeadlineBool  sched_stask.soft_deadline_bool%TYPE;
   lv_OrigPriority      evt_event.sched_priority_cd%TYPE;
   ll_OrigPriorityNo    NUMBER;
   ll_MaxPriority       NUMBER;
   ll_TempPriority      NUMBER;
   lv_Priority          evt_event.sched_priority_cd%TYPE;  
   lHInventoryDbId      inv_inv.inv_no_db_id%TYPE;
   lHInventoryId        inv_inv.inv_no_id%TYPE;
   lHInvCondCd          inv_inv.inv_cond_cd%TYPE;
   lAircraftDbId        inv_ac_reg.inv_no_db_id%TYPE;
   lAircraftId          inv_ac_reg.inv_no_id%TYPE;
   lMainInvDbId         inv_inv.inv_no_db_id%TYPE;
   lMainInvId           inv_inv.inv_no_id%TYPE;  
   lMainInvPartUseCd    eqp_part_no.part_use_cd%TYPE;
   lMainInvCondCd       inv_inv.inv_cond_cd%TYPE;   

   /* exceptions */
   xc_UnknownSQLError   EXCEPTION;

   /* cursor declarations */
   /* used to get the maximum sched_priority on child tasks */
   CURSOR lcur_MaxChildPriority (
         al_NHEventDbId   evt_event.event_db_id%TYPE,
         al_NHEventId     evt_event.event_id%TYPE
      ) IS
      SELECT max (nvl(p.priority_ord, 1)) priority_no
        FROM evt_event e, ref_sched_priority p
       WHERE e.nh_event_db_id = al_NHEventDbId
         AND e.nh_event_id    = al_NHEventId
         AND e.hist_bool = 0
         AND e.sched_priority_db_id = p.sched_priority_db_id (+)
         AND e.sched_priority_cd    = p.sched_priority_cd (+);
   lrec_MaxChildPriority lcur_MaxChildPriority%ROWTYPE;

   /* used to get task initial task deadline info */
   CURSOR lcur_EventDead (
         cl_EventDbId   typn_Id,
         cl_EventId     typn_Id
      ) IS
      SELECT evt_sched_dead.sched_dead_qt,
             evt_sched_dead.sched_dead_dt,
             evt_sched_dead.usage_rem_qt,
             evt_sched_dead.deviation_qt,
             evt_sched_dead.notify_qt
        FROM evt_sched_dead
       WHERE evt_sched_dead.sched_driver_bool=1 AND
             evt_sched_dead.event_db_id = cl_EventDbId AND
             evt_sched_dead.event_id    = cl_EventId;

BEGIN

   -- Initialize the return value
   ol_Return := icn_NoProc;

   /* Retrieve Event information */
   SELECT e.nh_event_db_id,
          e.nh_event_id,
          e.hist_bool,
          e.sched_priority_cd,
          nvl(p.priority_ord, 1) priority_no,          
          main_inv.inv_no_db_id, 
          main_inv.inv_no_id,
          main_inv.inv_cond_cd,
          eqp_part_no.part_use_cd,          
          h_inv.inv_no_db_id,
          h_inv.inv_no_id,
          h_inv.inv_cond_cd,          
          inv_ac_reg.inv_no_db_id,
          inv_ac_reg.inv_no_id          
     INTO ll_NHEventDbId,
          ll_NHEventId,
          ll_HistBool,
          lv_OrigPriority,
          ll_OrigPriorityNo,          
          lMainInvDbId,
          lMainInvId,
          lMainInvCondCd,
          lMainInvPartUseCd,          
          lHInventoryDbId,
          lHInventoryId,
          lHInvCondCd,          
          lAircraftDbId,
          lAircraftId                   
     FROM evt_event e,
          ref_sched_priority p,
          evt_inv main_evt_inv,
          inv_inv main_inv,
          inv_ac_reg,
          inv_inv h_inv,
          eqp_part_no
    WHERE
          e.event_db_id = al_EventDbId AND
          e.event_id    = al_EventId   AND
          e.rstat_cd	= 0
          AND
          p.sched_priority_db_id (+)= e.sched_priority_db_id AND
          p.sched_priority_cd    (+)= e.sched_priority_cd
          AND
          main_evt_inv.event_db_id   (+)= e.event_db_id AND
          main_evt_inv.event_id      (+)= e.event_id AND
          main_evt_inv.main_inv_bool (+)= 1
          AND
          main_inv.inv_no_db_id (+)= main_evt_inv.inv_no_db_id AND
          main_inv.inv_no_id    (+)= main_evt_inv.inv_no_id
          AND
          inv_ac_reg.inv_no_db_id (+)= main_inv.h_inv_no_db_id AND
          inv_ac_reg.inv_no_id    (+)= main_inv.h_inv_no_id
          AND
          h_inv.inv_no_db_id (+)= main_inv.h_inv_no_db_id AND
          h_inv.inv_no_id    (+)= main_inv.h_inv_no_id
          AND
          eqp_part_no.part_no_db_id (+)= main_inv.part_no_db_id AND
          eqp_part_no.part_no_id    (+)= main_inv.part_no_id;

   /* If the event is historic, then do nothing */
   IF ll_HistBool = 1 THEN
      ol_Return := icn_Success;
      RETURN;
   END IF;


   /* Determine if the event is a task, with a soft deadline. */
   SELECT sched_stask.soft_deadline_bool
     INTO lb_SoftDeadlineBool
     FROM sched_stask
    WHERE sched_stask.sched_db_id = al_EventDbId AND
          sched_stask.sched_id    = al_EventId   AND
          sched_stask.rstat_cd	  = 0;

   /* initialize the priority to NONE */
   ll_MaxPriority := 1;

   /* Part 1: Determine priority based on the event's own Deadlines */
   FOR lrec_EventDead IN lcur_EventDead (al_EventDbId, al_EventId) LOOP

      /* By default, initialize the priority to NONE */
      ll_TempPriority := 1;

      /* If current usage is before the notify period and notify_period is
         not negative (NEG notify_period means priority should be HIGH),
         then priority is LOW */
      IF lrec_EventDead.usage_rem_qt > lrec_EventDead.notify_qt AND
         lRec_EventDead.notify_qt >= 0 THEN
         ll_TempPriority := 2;

      /* If past deadline and deviation period, priority is OVERDUE */
      ELSIF (-1)*lrec_EventDead.usage_rem_qt >= lrec_EventDead.deviation_qt AND
            lb_SoftDeadlineBool = 0 THEN
         ll_TempPriority := 4;

      /* If current usage is within notify period before deadline or
         past deadline but within deviation period, priority is HIGH */
      ELSE
         ll_TempPriority := 3;
      END IF;

      /* if this is the highest priority, then record it */
      IF ll_TempPriority > ll_MaxPriority THEN
         ll_MaxPriority := ll_TempPriority;
      END IF;
   END LOOP;

   /* Part 2: Retrieve the maximum Child priority */
   OPEN lcur_MaxChildPriority (al_EventDbId, al_EventId);
   FETCH lcur_MaxChildPriority INTO lrec_MaxChildPriority;

   /* If a Priority is found on child events*/
   IF lcur_MaxChildPriority%FOUND THEN
      ll_TempPriority := lrec_MaxChildPriority.priority_no;
   ELSE
      ll_TempPriority := 1;
   END IF;
   CLOSE lcur_MaxChildPriority;

   /* Determine the maximum priority between the current event's priority
      and the child event's priority */
   IF ll_TempPriority > ll_MaxPriority THEN
      ll_MaxPriority := ll_TempPriority;
   END IF;

   /* Convert the priority number to a code */
   IF ll_MaxPriority = 2 THEN
      lv_Priority := 'LOW';
   ELSIF ll_MaxPriority = 3 THEN
      lv_Priority := 'HIGH';
   ELSIF ll_MaxPriority = 4 THEN
      lv_Priority := 'O/D';
   ELSE
      lv_Priority := 'NONE';
   END IF;

   /* Update the event's priority */
   UPDATE evt_event
      SET sched_priority_db_id = 0,
          sched_priority_cd    = lv_Priority
    WHERE event_db_id   = al_EventDbId
      AND event_id      = al_EventId
      AND (
          NOT(
              sched_priority_db_id = 0 AND
              sched_priority_cd    = lv_Priority
              )
           OR sched_priority_db_id IS NULL
      OR sched_priority_cd    IS NULL
      );

   /* Use recursion to update parent's priority */
   IF ll_NHEventDbId IS NOT NULL AND ll_NHEventId IS NOT NULL THEN
      SetPriority (ll_NHEventDbId, ll_NHEventId, ol_Return);
      IF ol_Return < 0 THEN
         RAISE xc_UnknownSQLError;
      END IF;
   END IF;

   /* if the task priority is is O/D, for aircraft this will be calculated in Java */
   IF lv_Priority = 'O/D' AND lAircraftDbId IS NULL THEN
         
         /* set the highest inventory condition to REPREQ */
			IF lHInventoryId IS NOT NULL  AND
				lHInvCondCd   <> 'QUAR'    AND 
				lHInvCondCd   <> 'CONDEMN' THEN
				
				UPDATE
					  inv_inv
				SET
					  inv_cond_db_id = 0,
					  inv_cond_cd    = 'REPREQ'
				WHERE
					  inv_no_db_id = lHInventoryDbId AND
					  inv_no_id    = lHInventoryId AND
					  NOT(
					  inv_cond_db_id = 0 AND
					  inv_cond_cd    = 'REPREQ'
					  );
			END IF;

			/* if a tool then update the inventory's condition to REPREQ  */
			IF lMainInvDbId IS NOT NULL     	AND 
				lMainInvPartUseCd = 'TOOLS'   	AND
				lHInvCondCd       <> 'QUAR'    AND 
				lHInvCondCd       <> 'CONDEMN' THEN
				
				UPDATE
					  inv_inv
				SET
					  inv_cond_db_id = 0,
					  inv_cond_cd    = 'REPREQ'
				WHERE
					  inv_no_db_id = lMainInvDbId AND
					  inv_no_id    = lMainInvId   AND
					  NOT(
					  inv_cond_db_id = 0 AND
              inv_cond_cd    = 'REPREQ'
              );              
      END IF;  
   END IF;

   /* return sucess */
   ol_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@rolluppriority@@@' || SQLERRM);
      IF lcur_MaxChildPriority%ISOPEN THEN CLOSE lcur_MaxChildPriority; END IF;      
      RETURN;
END SetPriority;


/********************************************************************************
*
* PROCEDURE:      SetPRRequiredByDate
* Arguments:      al_EventDbId (IN NUMBER): part request primary key
*                 al_EventId (IN NUMBER):   ----//-----
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:  This method sets required by date of a part request.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    October 1, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SetPRRequiredByDate(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ad_NeededByDate     IN date,
      ol_Return           OUT NUMBER
   ) IS


   /* exceptions */
   xc_UnknownSQLError   EXCEPTION;

BEGIN
   -- Initialize the return value
   ol_Return := icn_NoProc;

   /* Update the part requests required by date */
   UPDATE req_part
      SET req_by_dt = ad_NeededByDate
    WHERE req_part_db_id   = al_EventDbId
      AND req_part_id      = al_EventId
      AND
       (
        req_by_dt <> ad_NeededByDate
        OR
        req_by_dt IS NULL
       );

   /* return sucess */
   ol_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetPRRequiredByDate@@@' || SQLERRM);
      RETURN;
END SetPRRequiredByDate;



/********************************************************************************
*
* PROCEDURE:      FindTaskDueDate
* Arguments:      al_EventDbId (IN NUMBER): task primary key
*                 al_EventId (IN NUMBER):   ---//---
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Looks up task due date, uses task driving deadline to determine due date.
*                 If task does not have a deadline we follow the tree up to find it. If not
*                 found scheduled start date, or actual start date of a check or ro is used.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Ling Soh
* Recent Date:    Nov 29, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindTaskDueDate(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ad_NeededByDate     OUT date,
      ol_Return           OUT NUMBER
   ) IS


   /* exceptions */
   xc_UnknownSQLError   EXCEPTION;

   /* declare local variables */
   ls_RootEventStatus   evt_event.event_status_cd%TYPE;
   ld_RootSchedStartDt  Date;
   ld_RootActualStartDt Date;

   /* driving deadline if one exists we assume that both US
      and CA deadlines have sched_dead_dt properly updated (update deadline logic) . */

   CURSOR lcur_EventDead (
         cl_EventDbId   typn_Id,
         cl_EventId     typn_Id
      ) IS
      SELECT evt_sched_dead.sched_dead_qt,
             evt_sched_dead.sched_dead_dt
        FROM evt_sched_dead
       WHERE evt_sched_dead.sched_driver_bool=1 AND
             evt_sched_dead.event_db_id = cl_EventDbId AND
             evt_sched_dead.event_id    = cl_EventId;
             lrec_EventDead lcur_EventDead%ROWTYPE;

   /* get parent task, if one exists */
   CURSOR lcur_ParentTask (
         cl_EventDbId   typn_Id,
         cl_EventId     typn_Id

      ) IS
      SELECT evt_event.nh_event_db_id,
             evt_event.nh_event_id,
             evt_event.actual_start_dt,
             evt_event.sched_start_dt,
             evt_event.event_status_cd
        FROM evt_event
       WHERE evt_event.event_db_id = cl_EventDbId AND
             evt_event.event_id    = cl_EventId	  AND
             evt_event.rstat_cd	   = 0;
             lrec_ParentTask lcur_ParentTask%ROWTYPE;

BEGIN
   -- Initialize the return value
   ol_Return := icn_NoProc;

   -- Get root task's status and scheduled start date and actual start date
     SELECT
            root_event.event_status_cd,
            root_event.sched_start_gdt,
            root_event.actual_start_gdt
     INTO
          ls_RootEventStatus,
          ld_RootSchedStartDt,
          ld_RootActualStartDt
     FROM
          evt_event,
          evt_event root_event
     WHERE
           evt_event.event_db_id = al_EventDbId AND
           evt_event.event_id    = al_EventId	AND
           evt_event.rstat_cd	 = 0
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id;

   -- get the deadline
   OPEN lcur_EventDead (al_EventDbId, al_EventId);
   FETCH lcur_EventDead INTO lrec_EventDead;


   -- if deadline found, use the deadlines date to set the required by date, return.
   IF lcur_EventDead%FOUND THEN

      -- If root event is 'COMMIT' and scheduled start date is before the
      -- deadline date, use scheduled start date as the due date
      IF ls_RootEventStatus = 'COMMIT' AND
         ld_RootSchedStartDt<lrec_EventDead.sched_dead_dt THEN

         ad_NeededByDate:= ld_RootSchedStartDt;
         CLOSE lcur_EventDead;         
         ol_Return  := icn_Success;
         RETURN;

      -- else if root event is 'IN WORK' and actual start date is before the
      -- deadline date, use actual start date as the due date
      ELSIF ls_RootEventStatus = 'IN WORK' AND
            ld_RootActualStartDt<lrec_EventDead.sched_dead_dt THEN

         ad_NeededByDate:= ld_RootActualStartDt;
         CLOSE lcur_EventDead;         
         ol_Return  := icn_Success;
         RETURN;

      -- use the deadlines date to set the required by date, return.
      ELSE
         ad_NeededByDate:=lrec_EventDead.sched_dead_dt;
         CLOSE lcur_EventDead;         
         ol_Return  := icn_Success;
         RETURN;
      END IF;
   -- if deadline not found, go to the parent task
   ELSE

        -- fetch parent task
        OPEN lcur_ParentTask (al_EventDbId, al_EventId);
        FETCH lcur_ParentTask INTO lrec_ParentTask;

         -- if parent task exists, look for the due date
         IF (lrec_ParentTask.nh_event_db_id is not null) THEN
         -- find task due date
         findTaskDueDate(lrec_ParentTask.nh_event_db_id,
                         lrec_ParentTask.nh_event_id,
                         ad_NeededByDate,
                         ol_Return);
             IF ol_Return < 0 THEN
                CLOSE lcur_EventDead;
                CLOSE lcur_ParentTask;             
                RETURN;
             END IF;
         -- if parent task does not exist, then task is a root
         ELSE

           -- use the tasks actual start date if one found (estimated date, or actual)
           IF NOT (lrec_ParentTask.actual_start_dt is null) THEN
              ad_NeededByDate:=lrec_ParentTask.actual_start_dt;
              ol_Return  := icn_Success;
           ELSE
              -- use the tasks scheduled start date
              ad_NeededByDate:=lrec_ParentTask.sched_start_dt;
              ol_Return  := icn_Success;
           END IF;

         END IF;
         CLOSE lcur_ParentTask;
   END IF;

   CLOSE lcur_EventDead;

   /* return sucess */
   ol_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@FindTaskDueDate@@@' || SQLERRM);
      IF lcur_ParentTask%ISOPEN THEN CLOSE lcur_ParentTask; END IF;
      IF lcur_EventDead%ISOPEN  THEN CLOSE lcur_EventDead;  END IF;      
      RETURN;
END FindTaskDueDate;

/********************************************************************************
*
* PROCEDURE:      SyncPRRequiredByDt
* Arguments:      al_EventDbId (IN NUMBER): task primary key
*                 al_EventId (IN NUMBER):   ---//---
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Updates task's children part request needed by date based on the known due date.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    October 1, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SyncPRRequiredByDt(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ol_Return           OUT NUMBER
   ) IS


   /* exceptions */
   xc_UnknownSQLError   EXCEPTION;

   /* due date */
   ld_DueDate Date;


  -- lists all active part requests associated with task
  CURSOR lcur_GetPartRequestsForTask (
         cl_SchedDbId   typn_Id,
         cl_SchedId     typn_Id
      ) IS
      SELECT req_part.req_part_db_id,
             req_part.req_part_id
        FROM req_part,
             evt_event
       WHERE
             req_part.pr_sched_db_id = cl_SchedDbId AND
             req_part.pr_sched_id = cl_SchedId	    AND
             req_part.rstat_cd	  = 0
             AND
             evt_event.event_db_id=req_part.req_part_db_id AND
             evt_event.event_id=req_part.req_part_id AND
             evt_event.hist_bool=0 AND
             evt_event.event_type_cd = 'PR';
             lrec_GetPartRequestsForTask lcur_GetPartRequestsForTask%ROWTYPE;

 -- get all task under given task including the task itself
 CURSOR lcur_GetTaskTree (
         cl_SchedDbId   typn_Id,
         cl_SchedId     typn_Id
      ) IS
             SELECT
                    event_db_id ,
                    event_id
             FROM
                    evt_event WHERE rstat_cd = 0
             START WITH
                    event_db_id = cl_SchedDbId AND
                    event_id    = cl_SchedId

             CONNECT BY
                    nh_event_db_id  = PRIOR event_db_id AND
                    nh_event_id   =   PRIOR event_id;
             lrec_GetPartRequestsForTask lcur_GetPartRequestsForTask%ROWTYPE;


BEGIN
   -- Initialize the return value
     ol_Return := icn_NoProc;
     FOR lrec_GetTaskTree IN lcur_GetTaskTree(al_EventDbId, al_EventId) LOOP

       -- find due date of the task
       FindTaskDueDate (lrec_GetTaskTree.event_db_id, lrec_GetTaskTree.event_id, ld_DueDate, ol_Return);
       IF ol_Return < 0 THEN
        RETURN;
       END IF;
       -- for every active part request
       FOR lrec_GetPartRequestsForTask IN lcur_GetPartRequestsForTask(lrec_GetTaskTree.event_db_id, lrec_GetTaskTree.event_id) LOOP

       -- update required by date of the part request to due date of the task
       SetPRRequiredByDate (lrec_GetPartRequestsForTask.req_part_db_id,
                                   lrec_GetPartRequestsForTask.req_part_id,
                                   ld_DueDate,
                                   ol_Return);
       IF ol_Return < 0 THEN
           RETURN;
       END IF;
       END LOOP;
     END LOOP;


   /* return sucess */
   ol_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SyncPRRequiredByDt@@@' || SQLERRM);
      RETURN;
END SyncPRRequiredByDt;

/********************************************************************************
*
* Procedure: GetMainInventory
* Arguments:
*            an_EventDbId      (long) - event priamry key
*            an_EventId        (long) --//--
* Return:
*             an_InvNoDbId (long) -- main inventory primary key
*             an_InvNoId   (long)
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns main inventory for a given event.
*
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventory(
            an_EventDbId    IN  evt_event.event_db_id%TYPE,
            an_EventId      IN  evt_event.event_id%TYPE,
            an_InvNoDbId   OUT inv_inv.inv_no_db_id%TYPE,
            an_InvNoId     OUT inv_inv.inv_no_id%TYPE,
            on_Return      OUT typn_RetCode
   ) IS
     xc_MainInvNotFound  EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_OracleError;

     SELECT inv_no_db_id,
            inv_no_id
         INTO
            an_InvNoDbId,
            an_InvNoId
         FROM evt_inv
     WHERE evt_inv.event_db_id = an_EventDbId AND
           evt_inv.event_id    = an_EventId   AND
           evt_inv.main_inv_bool = 1;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
               RAISE xc_MainInvNotFound;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_OracleError;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','event_pkg@@@GetMainInventory@@@'||SQLERRM);
      RETURN;

END GetMainInventory;



/**
*  Recalculates both US and CA deadlines for the given task and for all of its FORECAST tasks as well
*/
PROCEDURE UpdateDeadlinesForTask (
   an_InvNoDbId IN inv_ac_reg.inv_no_db_id%TYPE,
   an_InvNoId   IN inv_ac_reg.inv_no_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE,
   at_GetDt     IN OUT hash_date_type,
   at_GetQt     IN OUT hash_float_type,   
   on_Return    OUT NUMBER
) IS
  -- CA deadlines
   CURSOR lcur_CADeadlines ( cn_EventDbId evt_event.event_db_id%TYPE,      
                             cn_EventId   evt_event.event_id%TYPE )
    IS
      SELECT
         evt_sched_dead.event_db_id,
         evt_sched_dead.event_id,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.start_dt,
         NVL( evt_sched_dead.interval_qt, 0 ) AS interval_qt,
         ref_eng_unit.ref_mult_qt,
         mim_data_type.data_type_cd
      FROM
         evt_sched_dead,
         mim_data_type,
         ref_eng_unit
      WHERE
         evt_sched_dead.event_db_id = cn_EventDbId AND
         evt_sched_dead.event_id    = cn_EventId
         AND
         mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
         mim_data_type.data_type_id    = evt_sched_dead.data_type_id    AND
         mim_data_type.domain_type_db_id = 0                            AND
         mim_data_type.domain_type_cd    = 'CA'
         AND
         ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
         ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd;
   lrec_CADeadlines lcur_CADeadlines%ROWTYPE;
         
   -- US deadlines
   CURSOR lcur_USDeadlines ( cn_EventDbId evt_event.event_db_id%TYPE,      
                             cn_EventId   evt_event.event_id%TYPE )
 IS
      SELECT
         evt_sched_dead.event_db_id,
         evt_sched_dead.event_id,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_dead_qt,
         evt_sched_dead.deviation_qt,
         evt_sched_dead.sched_from_cd,
         NVL( evt_sched_dead.interval_qt, 0 ) AS interval_qt,   
         inv_curr_usage.tsn_qt,
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id
      FROM
         evt_sched_dead,
         mim_data_type,
         inv_curr_usage,
         ref_eng_unit,
         sched_stask
      WHERE
         evt_sched_dead.event_db_id = cn_EventDbId AND
         evt_sched_dead.event_id    = cn_EventId
         AND
         sched_stask.sched_db_id = evt_sched_dead.event_db_id AND
         sched_stask.sched_id    = evt_sched_dead.event_id
         AND
         mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
         mim_data_type.data_type_id    = evt_sched_dead.data_type_id    AND
         mim_data_type.domain_type_db_id = 0                            AND
         mim_data_type.domain_type_cd    = 'US'
         AND
         ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
         ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd   
         AND
         inv_curr_usage.inv_no_db_id    = sched_stask.main_inv_no_db_id     AND
         inv_curr_usage.inv_no_id       = sched_stask.main_inv_no_id        AND
         inv_curr_usage.data_type_db_id = evt_sched_dead.data_type_db_id    AND
         inv_curr_usage.data_type_id    = evt_sched_dead.data_type_id;
   lrec_USDeadlines lcur_USDeadlines%ROWTYPE;
   
   -- forecast tasks
   CURSOR lcur_DepTasks  IS
      SELECT
         LEVEL,
         rel_event_db_id,
         rel_event_id 
      FROM
         evt_event_rel
      START WITH
         evt_event_rel.event_db_id = an_SchedDbId AND
         evt_event_rel.event_id    = an_SchedId   AND
         evt_event_rel.rel_type_cd = 'DEPT'
      CONNECT BY
         evt_event_rel.event_db_id = PRIOR evt_event_rel.rel_event_db_id AND
         evt_event_rel.event_id    = PRIOR evt_event_rel.rel_event_id    AND
         evt_event_rel.rel_type_cd = 'DEPT'
      ORDER BY 1;   
   lrec_DepTasks lcur_DepTasks%ROWTYPE;      
      
   ld_BeginDate        DATE;
   lf_BeginQt          FLOAT;
   lf_StartQt          FLOAT;
   lf_UsageRemExtended FLOAT;
   lf_UsageRem         FLOAT;
   lf_SchedDeadQt      FLOAT;
   ld_SchedDeadDt      DATE;
   ld_StartDt          DATE;
   
   -- stored the due values of the previous task
   lt_LastCADue        hash_date_type;
   lt_LastBeginDt      hash_date_type;   
   lt_LastBeginQt      hash_float_type;
   lt_LastUSDue        hash_float_type;
   lt_LastUSOverlap    hash_float_type;   
   ls_HashKey         VARCHAR(100);
   lf_USOverlap       NUMBER;
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;
   
   -- process the CA deadine if there is one
   OPEN lcur_CADeadlines(an_SchedDbId, an_SchedId);
   FETCH lcur_CADeadlines INTO lrec_CADeadlines ;
   IF lcur_CADeadlines%FOUND THEN
      ld_SchedDeadDt := lrec_CADeadlines.sched_dead_dt;

      /* if data type CLMON, than scheduled deadline should be at the end of the month */
      IF UPPER(lrec_CADeadlines.data_type_cd) = 'CLMON' THEN
         -- set the deadline to the last day of the month
         ld_SchedDeadDt := LAST_DAY(ld_SchedDeadDt);
      END IF;

      -- get the number of days between now (rounded to the nearest hour) and the deadline
      lf_UsageRem :=  (ld_SchedDeadDt - ROUND(SYSDATE,'HH')) / lrec_CADeadlines.ref_mult_qt ;

      /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
      IF NOT UPPER(lrec_CADeadlines.data_type_cd) = 'CHR' THEN
         ld_SchedDeadDt := TO_DATE(TO_CHAR(ld_SchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;

      /* update the evt_sched_dead */
      UPDATE
            evt_sched_dead
      SET
            evt_sched_dead.sched_dead_dt              = ld_SchedDeadDt,
            evt_sched_dead.usage_rem_qt               = lf_UsageRem,
            evt_sched_dead.sched_dead_dt_last_updated = SYSDATE
      WHERE
            evt_sched_dead.event_db_id     = lrec_CADeadlines.Event_Db_Id     AND
            evt_sched_dead.event_id        = lrec_CADeadlines.Event_Id        AND
            evt_sched_dead.data_type_db_id = lrec_CADeadlines.Data_Type_Db_Id AND
            evt_sched_dead.data_type_id    = lrec_CADeadlines.Data_Type_Id
            AND
            ( NOT ( evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                    evt_sched_dead.usage_rem_qt  = lf_UsageRem
                   )
              OR    evt_sched_dead.sched_dead_dt IS NULL
              OR    evt_sched_dead.usage_rem_qt  IS NULL
            );
       
       ls_HashKey := lrec_CADeadlines.Data_Type_Db_Id || ':' || lrec_CADeadlines.Data_Type_Id;
       lt_LastCADue(ls_HashKey) := ld_SchedDeadDt;
   END IF;    
   CLOSE lcur_CADeadlines;
   
   -- process the ACTV task's US deadlines.
   FOR lrec_USDeadlines IN lcur_USDeadlines (an_SchedDbId, an_SchedId) LOOP

          lf_SchedDeadQt  := lrec_USDeadlines.Sched_Dead_Qt;
          lf_StartQt      := lrec_USDeadlines.Start_Qt;
          ld_BeginDate    := SYSDATE;
          lf_USOverlap    := 0;

          -- If based on effective date and the effective date in future we need to calculate the start_qt
          IF lrec_USDeadlines.Sched_From_Cd='EFFECTIV' THEN
             -- see if we have already calculated this value before
             ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                             TO_CHAR(lrec_USDeadlines.Start_Dt, 'DD-MON-YYYY HH24:MI:SS');   
             IF at_GetQt.EXISTS(ls_HashKey) THEN
                lf_StartQt := at_GetQt(ls_HashKey);
             ELSE          
                IF lrec_USDeadlines.Start_Dt > SYSDATE THEN
                      -- in the future so predict the quantity                 
                      PredictUsageBetweenDt(an_InvNoDbId,
                                            an_InvNoId,
                                            lrec_USDeadlines.data_type_db_id,
                                            lrec_USDeadlines.data_type_id,
                                            SYSDATE,
                                            lrec_USDeadlines.Start_Dt,
                                            lrec_USDeadlines.Tsn_Qt,
                                            lf_StartQt,
                                            on_Return);
                      IF on_Return < 0 THEN
                          RETURN;
                      END IF;
                 
                ELSE
                      -- in the past, so go see what's the value back then
                      prep_deadline_pkg.GetHistoricUsageAtDt( lrec_USDeadlines.Start_Dt,
                                                              lrec_USDeadlines.data_type_db_id,
                                                              lrec_USDeadlines.data_type_id,
                                                              lrec_USDeadlines.Inv_No_Db_Id,
                                                              lrec_USDeadlines.Inv_No_Id,
                                                              lf_StartQt,
                                                              on_Return);
                      IF on_Return < 0 THEN
                         RETURN;
                      END IF;                        
                END IF;
                
                -- save it so we don't have to recalculate it again later
                at_GetQt(ls_HashKey) := lf_StartQt;
             
             END IF; 
             -- the start value might have changed, recalculate the new deadline in this case              
             lf_SchedDeadQt := lf_StartQt + lrec_USDeadlines.Interval_Qt;                
          END IF;

          lf_UsageRem := lf_SchedDeadQt - lrec_USDeadlines.Tsn_Qt;
          -- the usage remaining until the extended deadline (use this for sched_dead_dt)
          lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;

          -- see if we have already calculated this value before
          ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                          lf_UsageRemExtended;

          IF at_GetDt.EXISTS(ls_HashKey) THEN
             ld_SchedDeadDt := at_GetDt(ls_HashKey);

          ELSE
              -- find the deadline date 
              findForecastedDeadDt_new( an_InvNoDbId,
                                    an_InvNoId,
                                    lrec_USDeadlines.data_type_db_id,
                                    lrec_USDeadlines.data_type_id,
                                    lf_UsageRemExtended,
                                    ld_BeginDate,
                                    ld_SchedDeadDt,
                                    lf_USOverlap,
                                    on_Return);
             IF on_Return < 0 THEN
                RETURN;
             END IF;
             -- save the value so we don't have to recalculate it again later
             at_GetDt(ls_HashKey) := ld_SchedDeadDt;
             IF lf_UsageRemExtended > 0 THEN
                ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
                lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt;
                lt_LastBeginDt(ls_HashKey)   := ld_BeginDate;
                lt_LastBeginQt(ls_HashKey)   := lf_UsageRemExtended;
                lt_LastUSOverlap(ls_HashKey) := lf_USOverlap;       
             END IF;             

          END IF;

         /* update the evt_sched_dead */
         UPDATE
               evt_sched_dead
         SET
               evt_sched_dead.sched_dead_dt              = ld_SchedDeadDt,
               evt_sched_dead.usage_rem_qt               = lf_UsageRem,
               evt_sched_dead.sched_dead_dt_last_updated = SYSDATE,
               evt_sched_dead.sched_dead_qt              = lf_SchedDeadQt,
               evt_sched_dead.start_qt                   = lf_StartQt
         WHERE
               evt_sched_dead.event_db_id     = lrec_USDeadlines.Event_Db_Id     AND
               evt_sched_dead.event_id        = lrec_USDeadlines.Event_Id        AND
               evt_sched_dead.data_type_db_id = lrec_USDeadlines.Data_Type_Db_Id AND
               evt_sched_dead.data_type_id    = lrec_USDeadlines.Data_Type_Id
               AND
               ( NOT ( evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                       evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
                       evt_sched_dead.sched_dead_qt = lf_SchedDeadQt AND
                       evt_sched_dead.start_qt      = lf_StartQt
                      )
                 OR    evt_sched_dead.sched_dead_dt IS NULL
                 OR    evt_sched_dead.usage_rem_qt  IS NULL
                 OR    evt_sched_dead.sched_dead_qt IS NULL
                 OR    evt_sched_dead.start_qt      IS NULL
               );             

   END LOOP;
   
   -- process the forecast tasks
   FOR lrec_DepTasks IN lcur_DepTasks LOOP

       -- process the CA deadine if there is one
      OPEN lcur_CADeadlines(lrec_DepTasks.Rel_Event_Db_Id, lrec_DepTasks.Rel_Event_Id);
      FETCH lcur_CADeadlines INTO lrec_CADeadlines ;
      IF lcur_CADeadlines%FOUND THEN
           ls_HashKey := lrec_CADeadlines.Data_Type_Db_Id || ':' || lrec_CADeadlines.Data_Type_Id;
           -- if the forecast task has the same CA deadline as the previous task, start at the due date of the previous.
           IF  lt_LastCADue.EXISTS(ls_HashKey) THEN
               ld_StartDt := lt_LastCADue(ls_HashKey);
           ELSE
               ld_StartDt := lrec_CADeadlines.Start_Dt;
           END IF;
           
           /* Only round non Calendar Hour based dates */
           IF (NOT UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CHR') THEN
                ld_StartDt := TO_DATE(TO_CHAR(ld_StartDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
           END IF;
           
           /* if the data type is month then use months to calculate new deadline (not days) */
           IF UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CMON' THEN
              ld_SchedDeadDt := ADD_MONTHS( ld_StartDt, lrec_CADeadlines.Interval_Qt ) + (lrec_CADeadlines.Interval_Qt - TRUNC(lrec_CADeadlines.Interval_Qt)) * lrec_CADeadlines.Ref_Mult_Qt;

           /* if the data type is month then use months to calculate new deadline (not days) */
           ELSIF UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CLMON' THEN
              ld_StartDt:= LAST_DAY(ld_StartDt);
              ld_SchedDeadDt :=  ADD_MONTHS( ld_StartDt, lrec_CADeadlines.Interval_Qt );

           /* if the data type is year then use years to calculate new deadline (not days) */
           ELSIF UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CYR' THEN
              ld_SchedDeadDt := ADD_MONTHS( ld_StartDt, lrec_CADeadlines.Interval_Qt*12 );

           /* If it is a Calendar Hour, do not truncate */
           ELSIF UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CHR' THEN
              ld_SchedDeadDt := ld_StartDt + (lrec_CADeadlines.Interval_Qt * lrec_CADeadlines.Ref_Mult_Qt);

           /* add the correct # of days to the start date */
           ELSE
              ld_SchedDeadDt := ld_StartDt + TRUNC(lrec_CADeadlines.Interval_Qt * lrec_CADeadlines.Ref_Mult_Qt);
           END IF;
           
           -- get the number of days between now (rounded to the nearest hour) and the deadline
           lf_UsageRem :=  (ld_SchedDeadDt - ROUND(SYSDATE,'HH')) / lrec_CADeadlines.ref_mult_qt ;

           /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
           IF NOT UPPER(lrec_CADeadlines.data_type_cd) = 'CHR' THEN
              ld_SchedDeadDt := TO_DATE(TO_CHAR(ld_SchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
           END IF;

           /* update the evt_sched_dead of the forecast task*/
           UPDATE
                  evt_sched_dead
           SET
                  evt_sched_dead.sched_dead_dt              = ld_SchedDeadDt,
                  evt_sched_dead.Start_Dt                   = ld_StartDt,
                  evt_sched_dead.usage_rem_qt               = lf_UsageRem,
                  evt_sched_dead.sched_dead_dt_last_updated = SYSDATE
           WHERE
                  evt_sched_dead.event_db_id     = lrec_CADeadlines.Event_Db_Id     AND
                  evt_sched_dead.event_id        = lrec_CADeadlines.Event_Id        AND
                  evt_sched_dead.data_type_db_id = lrec_CADeadlines.Data_Type_Db_Id AND
                  evt_sched_dead.data_type_id    = lrec_CADeadlines.Data_Type_Id
                  AND
                  ( NOT ( evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                          evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
                          evt_sched_dead.start_dt      = ld_StartDt
                     )
                     OR    evt_sched_dead.sched_dead_dt IS NULL
                     OR    evt_sched_dead.usage_rem_qt  IS NULL
                     OR    evt_sched_dead.Start_Dt      IS NULL
                  );
         
           lt_LastCADue(ls_HashKey) := ld_SchedDeadDt;       
      END IF;    
      CLOSE lcur_CADeadlines;
   
      -- process the US deadlines of the forecast task
      FOR lrec_USDeadlines IN lcur_USDeadlines (lrec_DepTasks.Rel_Event_Db_Id, lrec_DepTasks.Rel_Event_Id) LOOP
          ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
           -- if the forecast task has the same US deadline as the previous task, start at the due qt of the previous.
          IF lt_LastUSDue.EXISTS(ls_HashKey) THEN
             lf_StartQt   := lt_LastUSDue(ls_HashKey);
          ELSE
             lf_StartQt   := lrec_USDeadlines.Start_Qt;
          END IF;
          
          IF lt_LastBeginDt.EXISTS(ls_HashKey) THEN
             ld_BeginDate := lt_LastBeginDt(ls_HashKey);
             lf_USOverlap := lt_LastUSOverlap(ls_HashKey);
             lf_BeginQt   := lt_LastBeginQt(ls_HashKey);
          ELSE
             ld_BeginDate := SYSDATE;
             lf_BeginQt   := 0;
             lf_USOverlap := 0; 
          END IF;
          
          lf_SchedDeadQt := lf_StartQt + lrec_USDeadlines.Interval_Qt;

          lf_UsageRem := lf_SchedDeadQt - lrec_USDeadlines.Tsn_Qt;

          -- the usage remaining until the extended deadline (use this for sched_dead_dt)
          lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;

          -- see if we have already calculated this value before
          ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                        lf_UsageRemExtended;
                        
          IF lf_UsageRemExtended <= 0 THEN
             ld_SchedDeadDt := TO_DATE(TO_CHAR(SYSDATE-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');
          ELSIF at_GetDt.EXISTS(ls_HashKey) THEN
             ld_SchedDeadDt := at_GetDt(ls_HashKey);

          ELSE
             -- the difference between the deadline date of the forecast task and the previous deadline date
             lf_UsageRemExtended := lf_UsageRemExtended + lf_USOverlap - lf_BeginQt;

             findForecastedDeadDt_new( an_InvNoDbId,
                                    an_InvNoId,
                                    lrec_USDeadlines.data_type_db_id,
                                    lrec_USDeadlines.data_type_id,
                                    lf_UsageRemExtended,
                                    ld_BeginDate,
                                    ld_SchedDeadDt,
                                    lf_USOverlap,
                                    on_Return);
             IF on_Return < 0 THEN
                RETURN;
             END IF;
             
             lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;
             at_GetDt(ls_HashKey) := ld_SchedDeadDt;
             ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
             lt_LastBeginDt(ls_HashKey)   := ld_BeginDate;
             lt_LastBeginQt(ls_HashKey)   := lf_UsageRemExtended;
             lt_LastUSOverlap(ls_HashKey) := lf_USOverlap;              
          END IF;
          
          ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
          lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt;
         
          /* update the evt_sched_dead */
          UPDATE
               evt_sched_dead
          SET
               evt_sched_dead.sched_dead_dt              = ld_SchedDeadDt,
               evt_sched_dead.usage_rem_qt               = lf_UsageRem,
               evt_sched_dead.sched_dead_dt_last_updated = SYSDATE,
               evt_sched_dead.sched_dead_qt              = lf_SchedDeadQt,
               evt_sched_dead.start_qt                   = lf_StartQt
          WHERE
               evt_sched_dead.event_db_id     = lrec_USDeadlines.Event_Db_Id     AND
               evt_sched_dead.event_id        = lrec_USDeadlines.Event_Id        AND
               evt_sched_dead.data_type_db_id = lrec_USDeadlines.Data_Type_Db_Id AND
               evt_sched_dead.data_type_id    = lrec_USDeadlines.Data_Type_Id
               AND
               ( NOT ( evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                       evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
                       evt_sched_dead.sched_dead_qt = lf_SchedDeadQt AND
                       evt_sched_dead.start_qt      = lf_StartQt
                      )
                 OR    evt_sched_dead.sched_dead_dt IS NULL
                 OR    evt_sched_dead.usage_rem_qt  IS NULL
                 OR    evt_sched_dead.sched_dead_qt IS NULL
                 OR    evt_sched_dead.start_qt      IS NULL
               );
      END LOOP;             
   END LOOP;
   
   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForTask@@@' || SQLERRM);
      RETURN;
END UpdateDeadlinesForTask;


/**
* Using the specified values, the function returns the priority number for the task
*/
PROCEDURE GetPriorityForTask(
   af_UsageRemQt    IN evt_sched_dead.usage_rem_qt%TYPE,
   af_NotifyQt      IN evt_sched_dead.notify_qt%TYPE,
   af_DeviationQt   IN evt_sched_dead.deviation_qt%TYPE,
   ab_SoftDeadBool  IN sched_stask.soft_deadline_bool%TYPE,
   an_Priority      OUT NUMBER,   
   on_Return        OUT NUMBER
   ) IS

BEGIN

   an_Priority := 1;

   IF af_UsageRemQt IS NOT NULL THEN
     
     /* If current usage is before the notify period and notify_period is
        not negative (NEG notify_period means priority should be HIGH),
        then priority is LOW */
     IF af_UsageRemQt > af_NotifyQt AND af_NotifyQt >= 0 THEN
        an_Priority := 2;
  
     /* If past deadline and deviation period, priority is OVERDUE */
     ELSIF (-1)*af_UsageRemQt >= af_DeviationQt AND ab_SoftDeadBool = 0 THEN
        an_Priority := 4;
  
     /* If current usage is within notify period before deadline or
        past deadline but within deviation period, priority is HIGH */
     ELSE
        an_Priority := 3;
     END IF;
   END IF;

   /* return success */
   on_Return := icn_Success;

-- exception handling
EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@GetPriorityForTask@@@' || SQLERRM);
      RETURN;
END GetPriorityForTask;

/**
* The method sets the priority to the highest of it's priority or that of its children
*/
PROCEDURE SetPriorityForTask(
   an_MainInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
   an_MainInvNoId   IN inv_inv.inv_no_id%TYPE,
   an_SchedDbId     IN sched_stask.sched_db_id%TYPE,
   an_SchedId       IN sched_stask.sched_id%TYPE,
   ln_Priority      IN NUMBER,
   on_Return        OUT NUMBER
   ) IS

   -- the largest child's priority
   CURSOR lcur_MaxChildPriority ( cn_EventDbId evt_event.event_db_id%TYPE,
                                  cn_EventId   evt_event.event_id%TYPE
       ) IS
       SELECT
              MAX (NVL(ref_sched_priority.priority_ord, 1)) AS priority_no
       FROM
              evt_event,
              ref_sched_priority
       WHERE
              evt_event.nh_event_db_id = cn_EventDbId AND
              evt_event.nh_event_id    = cn_EventId   AND
              evt_event.hist_bool      = 0            AND
              evt_event.rstat_cd       = 0 
              AND
              ref_sched_priority.sched_priority_db_id = evt_event.sched_priority_db_id AND
              ref_sched_priority.sched_priority_cd    = evt_event.sched_priority_cd;
   lrec_MaxChildPriority lcur_MaxChildPriority%ROWTYPE;


   ln_TempPriority NUMBER;
   lv_Priority     evt_event.sched_priority_cd%TYPE;

BEGIN

       ln_TempPriority := ln_Priority;

       OPEN lcur_MaxChildPriority(an_SchedDbId, an_SchedId);
       FETCH lcur_MaxChildPriority INTO lrec_MaxChildPriority;
       IF lcur_MaxChildPriority%FOUND THEN
          IF lrec_MaxChildPriority.Priority_No > ln_TempPriority THEN
             ln_TempPriority := lrec_MaxChildPriority.Priority_No;
          END IF;
       END IF;
       CLOSE lcur_MaxChildPriority;

       /* Convert the priority number to a code */
       IF ln_TempPriority = 2    THEN
             lv_Priority := 'LOW';
       ELSIF ln_TempPriority = 3 THEN
             lv_Priority := 'HIGH';
       ELSIF ln_TempPriority = 4 THEN
             lv_Priority := 'O/D';
       ELSE
             lv_Priority := 'NONE';
       END IF;

       /* Update the event's priority */
       UPDATE evt_event
       SET    evt_event.sched_priority_db_id = 0,
              evt_event.sched_priority_cd    = lv_Priority
       WHERE  evt_event.event_db_id   = an_SchedDbId AND
              evt_event.event_id      = an_SchedId
              AND (
                  NOT( evt_event.sched_priority_db_id = 0 AND
                       evt_event.sched_priority_cd    = lv_Priority
                  )
                  OR evt_event.sched_priority_db_id IS NULL
                  OR evt_event.sched_priority_cd    IS NULL
               );

   /* if the task priority is is O/D, we might need to set the condition to REPREQ */
   IF lv_Priority = 'O/D'  THEN

     /* set the highest inventory condition to REPREQ, if it is not already REPREQ, QUAR or CONDEMN */
      UPDATE inv_inv
      SET    inv_inv.inv_cond_db_id = 0,
             inv_inv.inv_cond_cd    = 'REPREQ'
      WHERE  (inv_inv.inv_no_db_id, inv_inv.inv_no_id) IN
             ( SELECT h.inv_no_db_id, h.inv_no_id
               FROM   inv_inv i,
                      inv_inv h
               WHERE  i.inv_no_db_id = an_MainInvNoDbId AND
                      i.inv_no_id    = an_MainInvNoId
                      AND
                      h.inv_no_db_id = i.h_inv_no_db_id AND
                      h.inv_no_id    = i.h_inv_no_id    AND
                      -- aircrafts are udpated in java
                      h.inv_class_cd <> 'ACFT'          AND
                      h.inv_cond_cd  NOT IN ('REPREQ', 'QUAR', 'CONDEMN')
              );

			/* if a tool then update the inventory's condition to REPREQ  */
      UPDATE inv_inv
      SET    inv_inv.inv_cond_db_id = 0,
             inv_inv.inv_cond_cd    = 'REPREQ'
      WHERE  (inv_inv.inv_no_db_id, inv_inv.inv_no_id) IN
             ( SELECT i.inv_no_db_id, i.inv_no_id
               FROM   inv_inv i,
                      eqp_part_no e
               WHERE  i.inv_no_db_id  = an_MainInvNoDbId AND
                      i.inv_no_id     = an_MainInvNoId   AND
                      i.inv_cond_cd   NOT IN ('REPREQ', 'QUAR', 'CONDEMN')
                      AND
                      e.part_no_db_id = i.part_no_db_id  AND
                      e.part_no_id    = i.part_no_id     AND
                      e.part_use_db_id = 0               AND
                      e.part_use_cd    = 'TOOLS'
              );  
   END IF;
   
   /* return success */
   on_Return := icn_Success;

-- exception handling
EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetPriorityForTask@@@' || SQLERRM);
      IF lcur_MaxChildPriority%ISOPEN THEN CLOSE lcur_MaxChildPriority; END IF;
      RETURN;
END SetPriorityForTask;

/**
* Resets the driving deadline for the given task
*/
PROCEDURE SetDriverPriorityForTask(
   an_MainInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
   an_MainInvNoId   IN inv_inv.inv_no_id%TYPE, 
   an_SchedDbId     IN sched_stask.sched_db_id%TYPE,
   an_SchedId       IN sched_stask.sched_id%TYPE,
   ab_SoftDeadBool  IN sched_stask.soft_deadline_bool%TYPE,
   ab_LastSchedBool IN task_task.last_sched_dead_bool%TYPE,   
   on_Return    OUT NUMBER
   ) IS

   ls_Query          VARCHAR2(32767);
   lc_Cursor         SYS_REFCURSOR;
   ln_DataTypeDbId   mim_data_type.data_type_db_id%TYPE;
   ln_DataTypeId     mim_data_type.data_type_id%TYPE;
   lf_UsageRemQt     evt_sched_dead.usage_rem_qt%TYPE;
   ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
   ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
   lb_IsPositive     NUMBER;
   ln_TempPriority   NUMBER;


BEGIN

     on_Return := 0;

     ls_Query :=
     'SELECT                                                         ' ||
     '       evt_sched_dead.data_type_db_id,                         ' ||
     '       evt_sched_dead.data_type_id,                            ' ||
     '       evt_sched_dead.usage_rem_qt,                            ' ||
     '       evt_sched_dead.notify_qt,                               ' ||
     '       evt_sched_dead.deviation_qt,                            ' ||          
     '       CASE WHEN evt_sched_dead.usage_rem_qt < 0               ' ||
     '            THEN 0                                             ' ||
     '            ELSE 1                                             ' ||
     '       END AS is_positive                                      ' ||  
     'FROM                                                           ' ||
     '       evt_sched_dead                                          ' ||
     'WHERE                                                          ' ||
     '       evt_sched_dead.event_db_id   = ' ||  an_SchedDbId || ' AND' ||
     '       evt_sched_dead.event_id      = ' ||  an_SchedId ;
     
   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
     IF ab_LastSchedBool = 1 THEN
        ls_Query := ls_Query || ' ORDER BY evt_sched_dead.sched_dead_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC';
     ELSE
        ls_Query := ls_Query || ' ORDER BY is_positive, evt_sched_dead.sched_dead_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC';     
     END IF;

   ln_TempPriority := 1;

   -- set the driving task
     OPEN lc_Cursor FOR ls_Query ;
     FETCH lc_Cursor INTO ln_DataTypeDbId ,
                          ln_DataTypeId   ,
                          lf_UsageRemQt   ,
                          ln_NotifyQt     ,
                          ln_DeviationQt  ,
                          lb_IsPositive   ;
     IF lc_Cursor%FOUND THEN

         -- clear the previous driver
         UPDATE evt_sched_dead
         SET    evt_sched_dead.sched_driver_bool = 0
         WHERE  evt_sched_dead.event_db_id       = an_SchedDbId AND
                evt_sched_dead.event_id          = an_SchedId   AND
                evt_sched_dead.sched_driver_bool <> 0
                AND
                NOT ( evt_sched_dead.data_type_db_id = ln_DataTypeDbId AND
                      evt_sched_dead.data_type_id    = ln_DataTypeId
                    );
  
         -- set the new driver
         UPDATE evt_sched_dead
         SET    evt_sched_dead.sched_driver_bool = 1
         WHERE  evt_sched_dead.event_db_id     = an_SchedDbId   AND
                evt_sched_dead.event_id        = an_SchedId     AND
                evt_sched_dead.sched_driver_bool <> 1
                AND
                evt_sched_dead.data_type_db_id = ln_DataTypeDbId AND
                evt_sched_dead.data_type_id    = ln_DataTypeId;

         GetPriorityForTask( lf_UsageRemQt, 
                             ln_NotifyQt, 
                             ln_DeviationQt, 
                             ab_SoftDeadBool, 
                             ln_TempPriority,
                             on_Return );
        IF on_Return < 0 THEN
            RETURN;
        END IF;                           
    END IF;
    CLOSE lc_Cursor;

    SetPriorityForTask(an_MainInvNoDbId, an_MainInvNoId, an_SchedDbId, an_SchedId, ln_TempPriority, on_Return);
    IF on_Return < 0 THEN
        RETURN;
    END IF;

   /* return success */
   on_Return := icn_Success;

-- exception handling
EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetDriverPriorityForTask@@@' || SQLERRM);
      IF lc_Cursor%ISOPEN THEN CLOSE lc_Cursor; END IF;
      RETURN;
END SetDriverPriorityForTask;

/**
* Resets the driving task relationship for the specified root task and also updates the 
* work order's deadlines if it applies
*/
PROCEDURE SetDRVTASKForRootTask(
   an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
   an_SchedId     IN sched_stask.sched_id%TYPE,
   av_TaskClassCd IN sched_stask.task_class_cd%TYPE,
   an_IsForecast  IN NUMBER,
   on_Return      OUT NUMBER
   )IS

   ln_DrivingEventDbId        evt_event.event_db_id%TYPE;
   ln_DrivingEventId          evt_event.event_id%TYPE;
   ln_EvtEventRelId           NUMBER;
   ln_RelEventOrd             NUMBER;
   ln_DrivingCount            NUMBER;

   /*
      If the parent task is based on a task definition with last_sched_dead_bool set to true
      we need to sort in opposite order.
   */
   CURSOR lcur_Deadline IS
      SELECT
         evt_event.event_db_id,
         evt_event.event_id,
         evt_sched_dead.sched_dead_dt,
         DECODE ( task_task.last_sched_dead_bool,
                  NULL,
                  (evt_sched_dead.sched_dead_dt - SYSDATE),
                  DECODE ( task_task.last_sched_dead_bool,
                           0,
                           (evt_sched_dead.sched_dead_dt - SYSDATE),
                           -1 * (evt_sched_dead.sched_dead_dt - SYSDATE)
                         )
         ) AS sort_column
      FROM
         evt_event,
         evt_sched_dead,
         sched_stask,
         task_task
      WHERE
         evt_event.h_event_db_id = an_SchedDbId AND
         evt_event.h_event_id    = an_SchedId   AND
         evt_event.hist_bool     = 0            AND
         evt_event.rstat_cd      = 0
         AND
         sched_stask.sched_db_id = evt_event.h_event_db_id and
         sched_stask.sched_id    = evt_event.h_event_id
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id and
         task_task.task_id    (+)= sched_stask.task_id
         AND
         evt_sched_dead.event_db_id       = evt_event.event_db_id AND
         evt_sched_dead.event_id          = evt_event.event_id AND
         evt_sched_dead.sched_driver_bool = 1
      ORDER BY
         sort_column ASC;

      lrec_Deadline lcur_Deadline%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

     OPEN lcur_Deadline ;
     FETCH lcur_Deadline INTO lrec_Deadline;
     /* If no driving deadline is found, remove any that may exist from moved tasks */
     IF NOT lcur_Deadline%FOUND THEN
        /* Delete all driving deadlines on the root task */
         DELETE
         FROM  evt_event_rel
         WHERE evt_event_rel.event_db_id    = an_SchedDbId AND
               evt_event_rel.event_id       = an_SchedId   AND
               evt_event_rel.rel_type_db_id = 0            AND
               evt_event_rel.rel_type_cd    = 'DRVTASK';

        CLOSE lcur_Deadline;
        /* Return */
        on_Return := icn_Success;
        RETURN;

     /* Otherwise, if a driving deadline is found, retain its event */
     ELSE
        ln_DrivingEventDbId := lrec_Deadline.event_db_id;
        ln_DrivingEventId   := lrec_Deadline.event_id;
     END IF;
     CLOSE lcur_Deadline;


     /*check if the root task have driving relationship set*/
     SELECT COUNT(*)
     INTO   ln_DrivingCount
     FROM   evt_event_rel
     WHERE  evt_event_rel.event_db_id    = an_SchedDbId AND
            evt_event_rel.event_id       = an_SchedId   AND
            evt_event_rel.rel_type_db_id = 0            AND
            evt_event_rel.rel_type_cd    = 'DRVTASK';

    /*if driving relationship is not set, we will have to create a new one*/
    IF ln_DrivingCount=0 THEN

       /*get the next allowed event_ord and rel_id for this task */
        SELECT
             DECODE (MAX(evt_event_rel.rel_event_ord), NULL, 1, MAX(evt_event_rel.rel_event_ord)+1),
             DECODE (MAX(evt_event_rel.event_rel_id),  NULL, 1, MAX(evt_event_rel.event_rel_id)+1)
        INTO
             ln_RelEventOrd,
             ln_EvtEventRelId
        FROM
             evt_event_rel
        WHERE
             evt_event_rel.event_db_id = an_SchedDbId AND
             evt_event_rel.event_id    = an_SchedId;


        /*insert new driving deadline relationship*/
        INSERT INTO evt_event_rel(event_db_id,
                                  event_id,
                                  event_rel_id,
                                  rel_event_db_id,
                                  rel_event_id,
                                  rel_type_db_id,
                                  rel_type_cd,
                                  rel_event_ord )

        VALUES(   an_SchedDbId,
                  an_SchedId,
                  ln_EvtEventRelId,
                  ln_DrivingEventDbId,
                  ln_DrivingEventId,
                  0,
                  'DRVTASK',
                  ln_RelEventOrd);


     ELSE
        /* if the driving relationship already exists*/
        /* just update it */
        UPDATE
               evt_event_rel
        SET
               evt_event_rel.rel_event_db_id = ln_DrivingEventDbId,
               evt_event_rel.rel_event_id    = ln_DrivingEventId
        WHERE
               evt_event_rel.event_db_id    = an_SchedDbId  AND
               evt_event_rel.event_id       = an_SchedId    AND
               evt_event_rel.rel_type_db_id = 0             AND
               evt_event_rel.rel_type_cd    = 'DRVTASK'     AND
            NOT (
                  evt_event_rel.rel_event_db_id = ln_DrivingEventDbId AND
                  evt_event_rel.rel_event_id    = ln_DrivingEventId
             );
     END IF;

     -- only non-forecast work packages have WORMVL relationships set
     IF an_IsForecast = 0 AND av_TaskClassCd IN ('RO','CHECK') THEN  
        UpdateWormvlDeadline(an_SchedDbId, an_SchedId, on_Return);
        IF on_Return < 0 THEN
            RETURN;
        END IF;
     END IF;

     -- Return success
     on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetDRVTASKForRootTask@@@' || SQLERRM);
      IF lcur_Deadline%ISOPEN THEN CLOSE lcur_Deadline; END IF;
      RETURN;
   END SetDRVTASKForRootTask;


/**
* Resets the needed by date of the part requests for the given root task
*/
PROCEDURE SetPRRequiredByDtForRootTask(
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE,
   on_Return    OUT NUMBER
   ) IS

  -- lists all active part requests associated with task
  CURSOR lcur_PartRequests IS
      SELECT evt_event.event_db_id,
             evt_event.event_id,
             req_part.req_part_db_id,
             req_part.req_part_id
      FROM
             req_part,
             evt_event req_event,
             evt_event
      WHERE
             evt_event.h_event_db_id = an_SchedDbId AND
             evt_event.h_event_id    = an_SchedId   AND
             evt_event.hist_bool     = 0
             AND
             req_part.pr_sched_db_id = evt_event.event_db_id AND
             req_part.pr_sched_id    = evt_event.event_id    AND
             req_part.rstat_cd       = 0
             AND
             req_event.event_db_id   = req_part.req_part_db_id AND
             req_event.event_id      = req_part.req_part_id    AND
             req_event.hist_bool     = 0
       ORDER BY
             evt_event.event_db_id,
             evt_event.event_id;
      lrec_PartRequests lcur_PartRequests%ROWTYPE;

      ln_SchedDbId      sched_stask.sched_db_id%TYPE;
      ln_SchedId        sched_stask.sched_id%TYPE;
      ld_DueDate        DATE;
      ln_ExitLoop       NUMBER;

BEGIN
     on_Return := icn_NoProc;

     OPEN lcur_PartRequests;
     FETCH lcur_PartRequests INTO lrec_PartRequests;
     LOOP
         EXIT WHEN lcur_PartRequests%NOTFOUND;

         ln_SchedDbId := lrec_PartRequests.event_db_id;
         ln_SchedId   := lrec_PartRequests.event_id;

         -- find due date of the task
         FindTaskDueDate (ln_SchedDbId, ln_SchedId, ld_DueDate, on_Return);
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         ln_ExitLoop := 0;

         WHILE ln_ExitLoop = 0 LOOP
            -- update required by date of the part request to due date of the task
             UPDATE req_part
             SET    req_part.req_by_dt   = ld_DueDate
             WHERE  req_part_db_id       = lrec_PartRequests.Req_Part_Db_Id AND
                    req_part.req_part_id = lrec_PartRequests.Req_Part_Id ;

             FETCH lcur_PartRequests INTO lrec_PartRequests;
             IF  lcur_PartRequests%NOTFOUND THEN
                 ln_ExitLoop := 1;
             ELSIF NOT (ln_SchedDbId = lrec_PartRequests.Event_Db_Id AND ln_SchedId = lrec_PartRequests.Event_Id) THEN
                 ln_ExitLoop := 1;
             END IF;
         END LOOP;
     END LOOP;

   /* return sucess */
   on_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetPRRequiredByDtForRootTask@@@' || SQLERRM);
      RETURN;
END SetPRRequiredByDtForRootTask;

/**
 *  Recalculates the deadlines on the given aircraft
 */
PROCEDURE UpdateDeadlinesForAircraft(
          an_AircraftDbId IN inv_ac_reg.inv_no_db_id%type,
          an_AircraftId   IN inv_ac_reg.inv_no_id%type,
          on_Return OUT NUMBER ) IS

   CURSOR lcur_Tasks IS
          -- all tasks for this aircraft, ordered leafs first
          WITH acft_tasks AS
          ( 
            SELECT 
                   evt_event.nh_event_db_id, 
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   DECODE( task_task.last_sched_dead_bool, NULL,0, task_task.last_sched_dead_bool) AS last_sched_dead_bool,
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,                      
                   evt_sched_dead.data_type_db_id,
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0) AS is_forecast
            FROM
                   sched_stask,
                   inv_inv,
                   evt_event,
                   evt_sched_dead,
                   task_task
            WHERE
                   sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
                   sched_stask.main_inv_no_id    = inv_inv.inv_no_id
                   AND
                   inv_inv.h_inv_no_db_id = an_AircraftDbId AND
                   inv_inv.h_inv_no_id    = an_AircraftId
                   AND
                   evt_event.event_db_id = sched_stask.sched_db_id AND
                   evt_event.event_id    = sched_stask.sched_id    AND
                   evt_event.hist_bool   = 0                       AND
                   evt_event.rstat_cd    = 0                  
                   AND
                   evt_sched_dead.event_db_id       (+)= evt_event.event_db_id AND
                   evt_sched_dead.event_id          (+)= evt_event.event_id    AND
                   evt_sched_dead.sched_driver_bool (+)= 1
                   AND
                   task_task.task_db_id (+)= sched_stask.task_db_id AND
                   task_task.task_id    (+)= sched_stask.task_id               
          )
          SELECT  
                   acft_tasks.nh_event_db_id,
                   acft_tasks.event_db_id,
                   acft_tasks.event_id,
                   acft_tasks.task_class_cd,
                   acft_tasks.last_sched_dead_bool,
                   acft_tasks.soft_deadline_bool,
                   acft_tasks.main_inv_no_db_id,
                   acft_tasks.main_inv_no_id,                    
                   acft_tasks.data_type_db_id,
                   acft_tasks.is_forecast,
                   LEVEL
          FROM     
                   acft_tasks
          
          CONNECT BY 
                  PRIOR acft_tasks.event_db_id = acft_tasks.nh_event_db_id AND 
                  PRIOR acft_tasks.event_id    = acft_tasks.nh_event_id
          
          START WITH 
                  acft_tasks.nh_event_db_id IS NULL AND 
                  acft_tasks.nh_event_id    IS NULL
          ORDER BY 
                  acft_tasks.is_forecast, LEVEL DESC;
   lrec_Tasks lcur_Tasks%ROWTYPE;

   -- save the date for the usage/datatype
   at_QtAsDate hash_date_type;
   at_DateAsQt hash_float_type;   
   ln_EventDbId     sched_stask.sched_db_id%TYPE;
   ln_EventId       sched_stask.sched_id%TYPE; 
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   -- dont update deadlines if the inventory is locked
   IF IsLocked( an_AircraftDbId, an_AircraftId ) = 1 THEN
      RETURN;
   END IF;


   FOR lrec_Tasks IN lcur_Tasks  LOOP
       -- these values are important in case an error occurs.
       ln_EventDbId := lrec_Tasks.Event_Db_Id;
       ln_EventId   := lrec_Tasks.Event_Id;  
       -- only fire this logic on the non-forecast tasks since it will update the forecast deadlines in the process
       IF lrec_Tasks.data_type_db_id IS NOT NULL AND lrec_Tasks.is_forecast = 0 THEN
          -- 1. Update all CA and US deadlines for the task with deadlines
          UpdateDeadlinesForTask(an_AircraftDbId, an_AircraftId, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, at_QtAsDate, at_DateAsQt, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
             RETURN;
          END IF;
       END IF;
      /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
      IF lrec_Tasks.is_forecast = 0 OR ( lrec_Tasks.is_forecast = 1 AND
                                         NOT IsOrphanedForecastedTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id) = 1 ) THEN

          -- 3. set the driver_bool and the task priorities
          SetDriverPriorityForTask(lrec_Tasks.main_inv_no_db_id, lrec_Tasks.main_inv_no_id, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Soft_Deadline_Bool, lrec_Tasks.last_sched_dead_bool, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
             RETURN;
          END IF;

          IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN
             -- 4. set the driving task for the root task
             SetDRVTASKForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, on_Return);
             IF on_Return<0 THEN
                Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
                RETURN;
             END IF;
             -- 5. update the part requests
             SetPRRequiredByDtForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, on_Return);
             IF on_Return<0 THEN
                Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
                RETURN;
             END IF;
          END IF;
       END IF;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForAircraft@@@' || SQLERRM);
      RETURN;
END UpdateDeadlinesForAircraft;

/**
 *  Recalculates the CA deadlines on all loose inventory
 */
PROCEDURE UpdateCADeadlinesLooseInv(
          an_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
          an_InvNoId   IN inv_inv.inv_no_id%TYPE,          
          on_Return OUT NUMBER ) IS

   CURSOR lcur_Tasks IS
          WITH inv_tasks AS
          ( 
            SELECT evt_event.nh_event_db_id, 
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   DECODE( task_task.last_sched_dead_bool, NULL,0, task_task.last_sched_dead_bool) AS last_sched_dead_bool,
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,                         
                   evt_sched_dead.data_type_db_id,
                   evt_sched_dead.data_type_id,
                   evt_sched_dead.sched_dead_dt,
                   evt_sched_dead.notify_qt,
                   evt_sched_dead.deviation_qt,
                   evt_sched_dead.usage_rem_qt,
                   mim_data_type.domain_type_cd,
                   ref_eng_unit.ref_mult_qt,
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0) AS is_forecast
            FROM
                   ( SELECT DISTINCT
                            evt_event.h_event_db_id,
                            evt_event.h_event_id
                     FROM   evt_event,
                            evt_sched_dead,
                            mim_data_type,
                            sched_stask,
                            inv_inv
                     WHERE  inv_inv.h_inv_no_db_id = an_InvNoDbId AND
                            inv_inv.h_inv_no_id    = an_InvNoId
                            AND
                            sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
                            sched_stask.main_inv_no_id    = inv_inv.inv_no_id
                            AND
                            evt_event.event_db_id = sched_stask.sched_db_id AND
                            evt_event.event_id    = sched_stask.sched_id    AND
                            evt_event.hist_bool   = 0                       AND
                            evt_event.rstat_cd    = 0 
                            AND
                            evt_sched_dead.event_db_id = evt_event.event_db_id AND
                            evt_sched_dead.event_id    = evt_event.event_id    
                            AND
                            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                            mim_data_type.data_type_id    = evt_sched_dead.data_type_id    AND
                            mim_data_type.domain_type_cd  = 'CA'
                   ) roots,
                   sched_stask,
                   evt_event,
                   evt_sched_dead,
                   mim_data_type,
                   ref_eng_unit,
                   task_task
            WHERE
                   evt_event.h_event_db_id = roots.h_event_db_id AND
                   evt_event.h_event_id    = roots.h_event_id    AND
                   evt_event.hist_bool     = 0                   AND
                   evt_event.rstat_cd      = 0       
                   AND
                   evt_sched_dead.event_db_id       (+)= evt_event.event_db_id AND
                   evt_sched_dead.event_id          (+)= evt_event.event_id    AND
                   evt_sched_dead.sched_driver_bool (+)= 1
                   AND
                   mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
                   mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id    
                   AND
                   ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
                   ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
                   AND
                   sched_stask.sched_db_id = evt_event.event_db_id AND
                   sched_stask.sched_id    = evt_event.event_id
                   AND
                   task_task.task_db_id (+)= sched_stask.task_db_id AND
                   task_task.task_id    (+)= sched_stask.task_id               
          )
          SELECT   inv_tasks.nh_event_db_id,
                   inv_tasks.event_db_id,
                   inv_tasks.event_id,
                   inv_tasks.data_type_db_id,
                   inv_tasks.data_type_id,      
                   inv_tasks.sched_dead_dt,
                   inv_tasks.ref_mult_qt,          
                   inv_tasks.last_sched_dead_bool,
                   inv_tasks.soft_deadline_bool,
                   inv_tasks.main_inv_no_db_id,
                   inv_tasks.main_inv_no_id,                        
                   inv_tasks.domain_type_cd,
                   inv_tasks.notify_qt,
                   inv_tasks.deviation_qt,
                   inv_tasks.usage_rem_qt,
                   inv_tasks.is_forecast,
                   LEVEL
          FROM     
                   inv_tasks
          
          CONNECT BY 
                  PRIOR inv_tasks.event_db_id = inv_tasks.nh_event_db_id AND 
                  PRIOR inv_tasks.event_id    = inv_tasks.nh_event_id
          
          START WITH 
                  inv_tasks.nh_event_db_id IS NULL AND 
                  inv_tasks.nh_event_id    IS NULL
          ORDER BY 
                  inv_tasks.is_forecast, LEVEL DESC;
   lrec_Tasks       lcur_Tasks%ROWTYPE;
   ln_EventDbId     sched_stask.sched_db_id%TYPE;
   ln_EventId       sched_stask.sched_id%TYPE;
   lf_UsageRem      FLOAT;
   ln_TempPriority  NUMBER;   
   
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   FOR lrec_Tasks IN lcur_Tasks  LOOP
       -- these values are important in case an error occurs.
       ln_EventDbId := lrec_Tasks.Event_Db_Id;
       ln_EventId   := lrec_Tasks.Event_Id;
       
       lf_UsageRem := lrec_Tasks.usage_rem_qt;
       -- 1. refresh the usage remaining on the CA deadline
       IF lrec_Tasks.domain_type_cd IS NOT NULL AND lrec_Tasks.domain_type_cd = 'CA' THEN
          -- get the number of days between now (rounded to the nearest hour) and the deadline
          lf_UsageRem :=  (lrec_Tasks.sched_dead_dt - ROUND(SYSDATE,'HH')) / lrec_Tasks.ref_mult_qt ;
    
          /* update the evt_sched_dead */
          UPDATE
                evt_sched_dead
          SET
                evt_sched_dead.usage_rem_qt               = lf_UsageRem,
                evt_sched_dead.sched_dead_dt_last_updated = SYSDATE
          WHERE
                evt_sched_dead.event_db_id     = lrec_Tasks.Event_Db_Id     AND
                evt_sched_dead.event_id        = lrec_Tasks.Event_Id        AND
                evt_sched_dead.data_type_db_id = lrec_Tasks.Data_Type_Db_Id AND
                evt_sched_dead.data_type_id    = lrec_Tasks.Data_Type_Id
                AND
                ( NOT ( evt_sched_dead.usage_rem_qt  = lf_UsageRem )
                  OR    evt_sched_dead.usage_rem_qt  IS NULL
                );  
      END IF;

      -- 2. recalculate the priorities
      IF lrec_Tasks.is_forecast = 0 OR ( lrec_Tasks.is_forecast = 1 AND 
                                           NOT IsOrphanedForecastedTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id) = 1 ) THEN
          GetPriorityForTask( lf_UsageRem, 
                              lrec_Tasks.notify_qt, 
                              lrec_Tasks.deviation_qt, 
                              lrec_Tasks.soft_deadline_bool, 
                              ln_TempPriority, 
                              on_Return ); 
          IF on_Return < 0 THEN
              Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
              RETURN;
          END IF;    

          SetPriorityForTask(lrec_Tasks.main_inv_no_db_id, lrec_Tasks.main_inv_no_id, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, ln_TempPriority, on_Return);
          IF on_Return < 0 THEN
              Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
              RETURN;
          END IF; 
      END IF;
   END LOOP;
   
   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateCADeadlinesLooseInv@@@' || SQLERRM);
      RETURN;
END UpdateCADeadlinesLooseInv;
END event_pkg;
/

--changeSet MX-17611:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FlightInterface IS

/*-------------------- Private procedures --------------------------------------*/
PROCEDURE generateFlightPlanForAircraft(aInvNoDbId IN evt_inv.inv_no_db_id%TYPE,
                                          aInvNoId IN evt_inv.inv_no_id%TYPE) IS

CURSOR lcur_GetActiveFlights(aInvNoDbId inv_inv.inv_no_db_id%TYPE, aInvNoId inv_inv.inv_no_id%TYPE) IS
     SELECT
         jl_flight.flight_db_id,
         jl_flight.flight_id,
         evt_event.event_status_cd,
         arr_evt_loc.loc_db_id arr_loc_db_id,
         arr_evt_loc.loc_id arr_loc_id,
         dep_evt_loc.loc_db_id dep_loc_db_id,
         dep_evt_loc.loc_id dep_loc_id
      FROM
         evt_inv,
         evt_event,
         jl_flight,
         evt_loc arr_evt_loc,
         evt_loc dep_evt_loc
      WHERE
         evt_inv.inv_no_db_id = aInvNoDbId AND
         evt_inv.inv_no_id = aInvNoId
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id = evt_inv.event_id AND
         hist_bool = 0
         AND
         jl_flight.flight_db_id = evt_event.event_db_id AND
         jl_flight.flight_id = evt_event.event_id
         AND
         dep_evt_loc.event_db_id = evt_event.event_db_id AND
         dep_evt_loc.event_id = evt_event.event_id AND
         dep_evt_loc.event_loc_id = 1
         AND
         arr_evt_loc.event_db_id = evt_event.event_db_id AND
         arr_evt_loc.event_id = evt_event.event_id AND
         arr_evt_loc.event_loc_id = 2
      ORDER BY
         evt_event.sched_end_gdt ASC;

CURSOR lcur_GetLastCompletedFlight(aInvNoDbId evt_inv.inv_no_db_id%TYPE, aInvNoId evt_inv.inv_no_id%TYPE) IS
     SELECT * FROM
         (SELECT
            jl_flight.flight_db_id,
            jl_flight.flight_id
         FROM
            evt_inv,
            evt_event,
            jl_flight
         WHERE
            evt_inv.inv_no_db_id = aInvNoDbId AND
            evt_inv.inv_no_id    = aInvNoId
            AND
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id AND
            hist_bool = 1
            AND
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd    = 'FLCMPLT'
            AND
            jl_flight.flight_db_id = evt_event.event_db_id AND
            jl_flight.flight_id    = evt_event.event_id
         ORDER BY
            evt_event.event_gdt DESC) hist_flight
      WHERE
         ROWNUM = 1;

--type definitions
TYPE flight IS RECORD(aFlightDbId      jl_flight.flight_db_id%TYPE, 
                        aFlightId         jl_flight.flight_id%TYPE, 
                     aEventStatus   evt_event.event_status_cd%TYPE,
                      aArrLocDbId           evt_loc.loc_db_id%TYPE,
                      aArrLocId                evt_loc.loc_id%TYPE,
                      aDepLocDbId           evt_loc.loc_db_id%TYPE,
                      aDepLocId                evt_loc.loc_id%TYPE
                      );
TYPE activeFlightsArray IS TABLE OF flight  INDEX BY BINARY_INTEGER;

--Local variables
lFlight flight;
lActiveFlightsArray   activeFlightsArray;
lrec_GetLastCompletedFlight lcur_GetLastCompletedFlight%ROWTYPE;
lCurrentLocDbId inv_inv.loc_db_id%TYPE;
lCurrentLocId inv_inv.loc_id%TYPE;
lFlightPlanOrd NUMBER;

BEGIN
     --Delete all of the rows from the flight plan for this aircraft
     lFlightPlanOrd := 1;
     DELETE FROM inv_ac_flight_plan 
           WHERE inv_ac_flight_plan.inv_no_db_id = aInvNoDbId AND 
                    inv_ac_flight_plan.inv_no_id = aInvNoId;

     --Get all the active flights for the aircraft
     OPEN lcur_GetActiveFlights(aInvNoDbId,aInvNoId);
     FETCH lcur_GetActiveFlights BULK COLLECT INTO lActiveFlightsArray;

     FOR i IN 1 .. lcur_GetActiveFlights%ROWCOUNT LOOP

         lFlight := lActiveFlightsArray(i);
         IF lcur_GetActiveFlights%ROWCOUNT = 1 THEN
            IF  lFlight.aEventStatus = 'FLPLAN' OR 
                lFlight.aEventStatus = 'FLOUT'  OR
                lFlight.aEventStatus = 'RETURN' OR 
                lFlight.aEventStatus = 'FLDELAY' THEN

               --Get most recent historic flight
               OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
               FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
               CLOSE lcur_GetLastCompletedFlight;

               --Create new flight plan record
               INSERT INTO inv_ac_flight_plan(   inv_no_db_id,
                                                    inv_no_id,
                                                    loc_db_id,
                                                       loc_id,
                                             arr_flight_db_id,
                                                arr_flight_id,
                                              flight_plan_ord
                                              )
                                        VALUES(    aInvNoDbId,
                                                     aInvNoId,
                                          lFlight.aDepLocDbId,
                                            lFlight.aDepLocId,
                     lrec_GetLastCompletedFlight.Flight_Db_Id,
                        lrec_GetLastCompletedFlight.Flight_Id,
                                               lFlightPlanOrd
                                                );

               lFlightPlanOrd := lFlightPlanOrd + 1;

            END IF;
         END IF;

         --Update the departure flight of the previous airport
         UPDATE inv_ac_flight_plan
            SET  dep_flight_db_id = lFlight.aFlightDbId,
                    dep_flight_id = lFlight.aFlightId 
          WHERE      inv_no_db_id = aInvNoDbId AND 
                        inv_no_id = aInvNoId AND 
                  flight_plan_ord = lFlightPlanOrd-1;

         --Create new flight plan record
         INSERT INTO inv_ac_flight_plan( inv_no_db_id,inv_no_id,
                                               loc_db_id,loc_id,
                                               arr_flight_db_id,
                                                  arr_flight_id,
                                                flight_plan_ord
                                        )
                                  VALUES(            aInvNoDbId,
                                                       aInvNoId,
                                            lFlight.aArrLocDbId,
                                              lFlight.aArrLocId,
                       lrec_GetLastCompletedFlight.Flight_Db_Id,
                          lrec_GetLastCompletedFlight.Flight_Id,
                                                 lFlightPlanOrd
                                         );

         lFlightPlanOrd := lFlightPlanOrd + 1;

     END LOOP;

     IF lcur_GetActiveFlights%NOTFOUND THEN

        --Get the current location of the aircraft
        SELECT inv_inv.loc_db_id,
                  inv_inv.loc_id 
          INTO   lCurrentLocDbId, 
                   lCurrentLocId 
         FROM inv_inv 
         WHERE inv_inv.inv_no_db_id = aInvNoDbId AND
                  inv_inv.inv_no_id = aInvNoId;

         --get most recent historic flight
        OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
        FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
        CLOSE lcur_GetLastCompletedFlight;

        --Insert a new row for the arrival flight of the next airport
        INSERT INTO inv_ac_flight_plan(   inv_no_db_id,
                                             inv_no_id,
                                             loc_db_id,
                                                loc_id,
                                      arr_flight_db_id,
                                         arr_flight_id,
                                       flight_plan_ord
                                    )
                              VALUES(
                                            aInvNoDbId,
                                              aInvNoId,
                                       lCurrentLocDbId,
                                         lCurrentLocId,
              lrec_GetLastCompletedFlight.Flight_Db_Id,
                 lrec_GetLastCompletedFlight.Flight_Id,
                                        lFlightPlanOrd
                                    );

        lFlightPlanOrd := lFlightPlanOrd + 1;

     END IF;
END generateFlightPlanForAircraft;

/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aEventDbId (number):
*               aEventId (number):
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure, 
*               procedure 'generateFlightPlanForAircraft' will be invoked, and also 
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree'.
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE aircraftSwap(aEventDbId IN evt_event.event_db_id%TYPE, 
                            aEventId IN evt_event.event_id%TYPE, 
                        aInvNoDbId IN inv_inv.inv_no_db_id%TYPE, 
                             aInvNoId IN inv_inv.inv_no_id%TYPE,
                    aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                         aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                         aReturn OUT NUMBER
                      ) IS

CURSOR lcur_GetInvNumData(aEventDbId evt_event.event_db_id%TYPE,
                               aEventId evt_event.event_id%TYPE
                          ) IS
      SELECT i.data_type_db_id,
             i.data_type_id,
             i.tsn_delta_qt,
             i.tso_delta_qt,
             i.tsi_delta_qt,
             e.inv_no_db_id,
             e.inv_no_id
        FROM evt_inv e,
             inv_num_data i
       WHERE i.event_db_id = aEventDbId     AND
             i.event_id    = aEventId       AND
             e.event_db_id  = i.event_db_id AND
             e.event_id     = i.event_id    AND
             e.event_inv_id = i.event_inv_id;

CURSOR lcur_GetEvtInv(aEventDbId evt_event.event_db_id%TYPE,
                           aEventId evt_event.event_id%TYPE
                     ) IS
      SELECT *
        FROM evt_Inv
       WHERE Evt_Inv.event_db_id = aEventDbId AND
             evt_Inv.event_id = aEventId;

CURSOR lcur_GetSubAssemblies(aInvNoDbId inv_inv.inv_no_db_id%TYPE,
                                  aInvNoId inv_inv.inv_no_id%TYPE
                            ) IS
     SELECT   INV_INV.INV_NO_DB_ID,
               INV_INV.INV_NO_ID
        FROM   INV_INV
       WHERE   ORIG_ASSMBL_DB_ID IS NOT NULL
               AND
         NOT   ( INV_INV.INV_NO_DB_Id = aInvNoDbId     AND
                 INV_INV.INV_NO_ID    = aInvNoId )
      START WITH
               ( INV_INV.INV_NO_DB_ID = aInvNoDbId   )  AND
               ( INV_INV.INV_NO_ID    = aInvNoId     )
      CONNECT BY
               ( INV_INV.NH_INV_NO_DB_ID = PRIOR INV_INV.INV_NO_DB_ID   )  AND
               ( INV_INV.NH_INV_NO_ID    = PRIOR INV_INV.INV_NO_ID      );

--type definitions
TYPE inventory IS RECORD(aInvNoDbId inv_inv.inv_no_db_id%TYPE, 
                              aInvNoId inv_inv.inv_no_id%TYPE
                        );
TYPE inventoryArray IS TABLE OF inventory INDEX BY BINARY_INTEGER;

--local variables
lInventoryArray inventoryArray;
lInventory inventory;
lOriginalInvNoDbId evt_inv.inv_no_db_id%TYPE ;
lOriginalInvNoId  evt_inv.inv_no_id%TYPE;
lHist_bool evt_event.hist_bool%TYPE;
lMain_inv NUMBER;
lMaxId NUMBER;
lNum NUMBER;
lReturn NUMBER;

--exceptions
xc_CreateEvtInvEntry        EXCEPTION;
xc_UsageCalculations        EXCEPTION;
xc_UpdateDeadlineForInvTree EXCEPTION;

BEGIN
     -- Initialize the return
     aReturn := iNoProc;

     SELECT evt_inv.inv_no_db_id,
               evt_inv.inv_no_id 
       INTO   lOriginalInvNoDbId, 
                LOriginalInvNoId 
       FROM  evt_inv 
       WHERE evt_inv.event_db_id = aEventDbId AND
                evt_inv.event_id = aEventId AND
           evt_inv.main_inv_bool = 1;

     SELECT evt_event.hist_bool 
       INTO lHist_bool 
       FROM evt_event 
       WHERE evt_event.event_db_id = aEventDbId AND
                evt_event.event_id = aEventId;

     IF lHist_bool = 1 THEN
     
         FOR lrec_InvNumData IN lcur_GetInvNumData(aEventDbId, aEventId) LOOP

             UPDATE inv_curr_usage
             SET tsn_qt = tsn_qt - lrec_InvNumData.Tsn_Delta_Qt,
                 tso_qt = tso_qt - lrec_InvNumData.Tso_Delta_Qt,
                 tsi_qt = tsi_qt - lrec_InvNumData.Tsi_Delta_Qt
             WHERE data_type_db_id = lrec_InvNumData.Data_Type_Db_Id AND
                   data_type_id    = lrec_InvNumData.Data_Type_Id
             AND ( inv_no_db_id, inv_no_id )
                 IN
                 ( SELECT inv_no_db_id, inv_no_id
                     FROM inv_inv
                    WHERE ( assmbl_inv_no_db_id = lrec_InvNumData.Inv_No_Db_Id and
                            assmbl_inv_no_id    = lrec_InvNumData.Inv_No_Id   and
                            orig_assmbl_db_id is null
                          )
                      OR ( inv_no_db_id = lrec_InvNumData.Inv_No_Db_Id AND
                           inv_no_id    = lrec_InvNumData.Inv_No_Id )
                 );

       END LOOP;

       FOR lrec_EvtInv IN lcur_GetEvtInv(aEventDbId, aEventId) LOOP

           --Execute usage calculations on the inventory
           USAGE_PKG.UsageCalculations(lrec_EvtInv.Inv_No_Db_Id, lrec_EvtInv.Inv_No_Id, lReturn);
           IF lReturn < 0 THEN
              RAISE xc_UsageCalculations;
           END IF;

           IF lrec_EvtInv.Main_Inv_Bool = 1 THEN

              EVENT_PKG.UpdateDeadlineForInvTree(lrec_EvtInv.Inv_No_Db_Id, lrec_EvtInv.Inv_No_Id, lReturn);
              IF lReturn < 0 THEN
                 RAISE xc_UpdateDeadlineForInvTree;
              END IF;
           END IF;

       END LOOP;

     END IF;
     
      --Remove rows from relevant five tables for current event
      DELETE FROM inv_num_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM inv_chr_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM inv_parm_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM evt_inv_usage
      WHERE event_db_id = aEventDbId
      AND event_id = aEventId;

      DELETE FROM evt_inv
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      OPEN lcur_GetSubAssemblies(aInvNoDbId,aInvNoId);
      FETCH lcur_GetSubAssemblies BULK COLLECT INTO lInventoryArray;
      CLOSE lcur_GetSubAssemblies;

      SELECT COUNT(*) 
        INTO lMain_inv 
        FROM evt_inv 
        WHERE evt_inv.inv_no_db_id = aInvNoDbId AND
                 evt_inv.inv_no_id = aInvNoId   AND
             evt_inv.main_inv_bool = 1;

      IF lMain_inv > 0 THEN
         --Reset its MAIN_INV_BOOL flag to false
         UPDATE evt_inv 
            SET evt_inv.main_inv_bool = 0 
          WHERE evt_inv.inv_no_db_id = aInvNoDbId AND
                   evt_inv.inv_no_id = aInvNoId   AND 
               evt_inv.main_inv_bool = 1;
      END IF;

      --Create a new row in the EVT_INV table
      SELECT MAX(evt_inv.event_inv_id) 
        INTO lMaxId 
        FROM evt_inv 
       WHERE evt_inv.event_db_id = aEventDbId AND 
                evt_inv.event_id = aEventId;

      IF lMaxId IS NULL THEN
         --Default
         lMaxId := 1;
      ELSE
         lMaxId := lMaxId + 1;
      END IF;

      INSERT INTO evt_inv( event_db_id,
                              event_id,
                          event_inv_id,
                          inv_no_db_id,
                             inv_no_id,
                         main_inv_bool
                         )
                   values(   aEventDbId,
                               aEventId,
                                 lMaxId,
                             aInvNoDbId,
                               aInvNoId,
                                      0
                          );

      SELECT COUNT(*) 
        INTO lNum 
        FROM evt_inv 
       WHERE     evt_inv.event_db_id = aEventDbId AND
                    evt_inv.event_id = aEventId   AND
                evt_inv.inv_no_db_id = aInvNoDbId AND
                   evt_inv.inv_no_id = aInvNoId   AND
                evt_inv.event_inv_id = lMaxId;

      IF lNum <> 1 THEN
         --Exception
         RAISE xc_CreateEvtInvEntry;         
      END IF;

      UPDATE evt_inv 
         SET evt_inv.main_inv_bool = 1 
       WHERE evt_inv.inv_no_db_id = aInvNoDbId AND
                evt_inv.inv_no_id = aInvNoId   AND
             evt_inv.event_inv_id = lMaxId;

      FOR i IN 1 .. lInventoryArray.LAST LOOP

         lInventory := lInventoryArray(i);

         --Create a new row in the EVT_INV table
         SELECT MAX(evt_inv.event_inv_id) 
           INTO lMaxId 
           FROM evt_inv 
           WHERE evt_inv.event_db_id = aEventDbId AND
                    evt_inv.event_id = aEventId;

         IF SQL%NOTFOUND THEN
            --Default
            lMaxId := 1;
         ELSE
            lMaxId := lMaxId + 1;
         END IF;

         INSERT INTO evt_inv(  event_db_id,
                                  event_id,
                              event_inv_id,
                              inv_no_db_id,
                                 inv_no_id,
                             main_inv_bool
                            )
                      VALUES(   aEventDbId,
                                  aEventId,
                                    lMaxId,
                     lInventory.aInvNoDbId,
                       lInventory.aInvNoId,
                                         0
                             );

         SELECT COUNT(*) 
           INTO lNum 
           FROM evt_inv 
          WHERE    evt_inv.event_db_id = aEventDbId AND
                      evt_inv.event_id = aEventId   AND 
                  evt_inv.inv_no_db_id = lInventory.aInvNoDbId AND
                     evt_inv.inv_no_id = lInventory.aInvNoId   AND
                  evt_inv.event_inv_id = lMaxId;

         IF lNum <> 1 THEN
            --exception
            RAISE xc_CreateEvtInvEntry;           
         END IF;

      END LOOP;

      --Take configuration snapshot of the event inventories
      UPDATE evt_inv
         SET (    nh_inv_no_db_id, 
                     nh_inv_no_id,
              assmbl_inv_no_db_id, 
                 assmbl_inv_no_id,
                   h_inv_no_db_id, 
                      h_inv_no_id,
                     assmbl_db_id, 
                        assmbl_cd, 
                    assmbl_bom_id, 
                    assmbl_pos_id,
                    part_no_db_id, 
                       part_no_id,
                   bom_part_db_id, 
                      bom_part_id
                ) = (SELECT  nh_inv_no_db_id, 
                                nh_inv_no_id,
                         assmbl_inv_no_db_id, 
                            assmbl_inv_no_id,
                              h_inv_no_db_id, 
                                 h_inv_no_id,
                                assmbl_db_id, 
                                   assmbl_cd, 
                               assmbl_bom_id,
                               assmbl_pos_id,
                               part_no_db_id,
                                  part_no_id,
                              bom_part_db_id, 
                                 bom_part_id
                        FROM inv_inv
                       WHERE inv_inv.inv_no_db_id  = evt_inv.inv_no_db_id AND
                             inv_inv.inv_no_id     = evt_inv.inv_no_id
                   )
      WHERE  evt_inv.event_db_id = aEventDbId AND
                evt_inv.event_id = aEventId;

      -- Assign the new aircraft type if it is provided
      IF aAssmblDbId > 0 AND aAssmblCd IS NOT NULL THEN
         UPDATE jl_flight
            SET plan_assmbl_db_id = aAssmblDbId, 
                   plan_assmbl_cd = aAssmblCd
         WHERE jl_flight.flight_db_id = aEventDbId AND
               jl_flight.flight_id = aEventId;
      END IF;

      --Regenerate the original aircraft's flight plan
      generateFlightPlanForAircraft(lOriginalInvNoDbId, lOriginalInvNoId);

      --Regenerate the new aircraft's flight plan
      generateFlightPlanForAircraft(aInvNoDbId, aInvNoId);

      --return success
      aReturn := iSuccess;

      EXCEPTION
          WHEN xc_CreateEvtInvEntry THEN
               aReturn := iCreateEvtInvEntryError;
          WHEN xc_UsageCalculations THEN
               aReturn := iUsageCalculationsError;
          WHEN xc_UpdateDeadlineForInvTree THEN
               aReturn := iUpdateDeadlineForInvTreeError;
          WHEN OTHERS THEN
               aReturn := iOracleError;

END aircraftSwap;

END FlightInterface;
/

--changeSet MX-17611:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE EVENT_PKG
IS

/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

/* subtype declarations */
SUBTYPE typn_RetCode IS NUMBER;
SUBTYPE typn_Id      IS inv_inv.inv_no_id%TYPE;
SUBTYPE typv_Cd      IS ref_data_source.data_source_cd%TYPE;
SUBTYPE typf_Qt      IS inv_curr_usage.tsn_qt%TYPE;
SUBTYPE typn_Bool    IS evt_event.hist_bool%TYPE;
SUBTYPE typdt_Dt     IS evt_event.event_dt%TYPE;
SUBTYPE typv_Sdesc   IS inv_inv.inv_no_sdesc%TYPE;
SUBTYPE typv_Oem     IS inv_inv.serial_no_oem%TYPE;
SUBTYPE typv_LCd     IS eqp_assmbl_bom.assmbl_bom_cd%TYPE;

/* constant declarations (error codes) */
icn_Success                CONSTANT NUMBER       := 1;
icn_NoProc                 CONSTANT typn_RetCode := 0;  -- No processing done
icn_DiffLengthArrays       CONSTANT NUMBER       := -1;     /* used by RecordUsage */
icn_EvtInvNotFound         CONSTANT NUMBER       := -2;
icn_InvCurrUsageNotFound   CONSTANT NUMBER       := -3;
icn_InvalidDataType        CONSTANT NUMBER       := -30;
icn_InvalidForecastModel   CONSTANT NUMBER       := -31;
icn_SequenceError          CONSTANT NUMBER       := -99;
icn_OracleError            CONSTANT NUMBER       := -100;

/* declare TABLE types (used like arrays) */
TYPE typtabn_Id    IS TABLE OF typn_Id   NOT NULL INDEX BY BINARY_INTEGER;
TYPE typtabf_Qt    IS TABLE OF FLOAT     NOT NULL INDEX BY BINARY_INTEGER;
TYPE typtabn_Bool  IS TABLE OF typn_Bool NOT NULL INDEX BY BINARY_INTEGER;
TYPE hash_date_type  IS TABLE OF DATE             INDEX BY VARCHAR2(100);
TYPE hash_float_type IS TABLE OF FLOAT            INDEX BY VARCHAR2(100);


/********************************************************************************
*
* Procedure:      UpdateDeadline
* Arguments:      al_EventDbId (IN NUMBER): The event to be updated
*                 al_EventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:    This procedure used to update an event's priorities and
*                 deadlines
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Ling Soh
* Recent Date:    Dec 20, 2006
*
*********************************************************************************
*
* Copyright 2006 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadline(
        al_EventDbId    IN typn_Id,
        al_EventId      IN typn_Id,
        ol_Return       OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      PredictUsageBetweenDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aStartDate (IN DATE): the relative date to perform our calculations from. Normally call with SYSDATE
*                 ad_TargetDate (IN DATE): The date we want the deadline to be due
*                 an_StartUsageQt (IN NUMBER): the usages at the Start Date
*                 on_TsnQt (OUT NUMBER): The predicted TSN value at the ad_TargetDate
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted usage quantity at the stpecified date for a deadline with the specified datatype on
*                 the given inventory. The forecasted deadline value(quantity) is calculated using the aircrafts
*                 flight plan and forecast model. It uses binary search to re-use existing function to calculate due date based on usage value
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   N/A
* Recent Date:    March 19, 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PredictUsageBetweenDt(
            aAircraftDbId    IN  inv_inv.inv_no_db_id%TYPE,
            aAircraftId      IN  inv_inv.inv_no_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDate    IN  evt_sched_dead.start_dt%TYPE,
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_StartUsageQt IN  evt_sched_dead.start_qt%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   );

/********************************************************************************
*
* Procedure:      IsInstalledOnAircraft
* Arguments:      aInvNoDbId (IN NUMBER): pk of the inventory to check
*                 aInvNoId (IN NUMBER): ""
*                 aAircraftDbId (OUT NUMBER): if the inventory is installed on aircraft
*                 aAircraftId (OUT NUMBER): this is aicraft's PK.
* Description:    Tests whether the given inventory is installed on an aircraft, and returns the aircraft PK
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   
* Recent Date:    Mar 27, 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsInstalledOnAircraft(
                           aInvNoDbId IN typn_Id, 
                           aInvNoId IN typn_Id, 
                           aAircraftDbId OUT typn_Id, 
                           aAircraftId OUT typn_Id) RETURN NUMBER;

/********************************************************************************
*
* PROCEDURE:      FindTaskDueDate
* Arguments:      al_EventDbId (IN NUMBER): task primary key
*                 al_EventId (IN NUMBER):   ---//---
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Looks up task due date, uses task driving deadline to determine due date.
*                 If task does not have a deadline we follow the tree up to find it. If not
*                 found scheduled start date, or actual start date of a check or ro is used.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    October 1, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindTaskDueDate(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ad_NeededByDate     OUT date,
      ol_Return           OUT NUMBER
   );
/********************************************************************************
*
* Procedure:      UpdateWormvlDeadline
* Arguments:      an_WorkPkgDbId (IN NUMBER): work order primary key
*                 an_WorkPkgId(IN NUMBER):    ---//---
*                 ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   Update deadline of a work order replacement task which is shown
*                to have a relationship WORMVL. The deadline will be copied wrom
*                the work order driving deadline task.

*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    September 9, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWormvlDeadline(
          an_WorkPkgDbId          IN  typn_Id,
          an_WorkPkgId            IN  typn_Id,
          on_Return                  OUT NUMBER);
/********************************************************************************
*
* Procedure:      UpdateRootDrivingDeadline
* Arguments:      an_RootEventDbId (IN NUMBER): Event in a tree that will have
*                                 it's driving deadline udpated
*                          al_RootEventId(IN NUMBER): ""
*                          ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Description:   This procedure is used to update the driving deadline
*                for a check or work order.  The task within the check that has
*                the nearest upcoming date is considered to the the driving
*                deadline.  The driving deadline relationship will be captured in the
*                EVT_EVENT_REL table.
*
* Orig.Coder:     slr
* Recent Coder:
* Recent Date:    March 27, 2003
*
*********************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDrivingDeadline(
          an_SchedStaskDbId     IN  typn_Id,
          an_SchedStaskId       IN  typn_Id,
          on_Return             OUT NUMBER
          );

/********************************************************************************
*
* Procedure:      UpdateDeadlineForInvTree
* Arguments:      al_InvNoDbId (IN NUMBER): The top inventory in the tree
*                 al_InvNoId (IN NUMBER): ""
* Description:    Sometimes you need to update all of the deadlines against
*                 inventory in a given tree. If you pass in the top inventory
*                 item, this function will evaluate the deadlines for all
*                 subcomponents.
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Andrew Hircock
* Recent Date:    Aug 12,1999
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineForInvTree(
      an_InvNoDbId      typn_Id,
      an_InvNoId        typn_Id,
      on_Return         OUT NUMBER
   );

/********************************************************************************
*
* Procedure:      UpdateDeadlineForInv
* Arguments:      al_InvNoDbId (IN NUMBER): The inventory to update
*                 al_InvNoId (IN NUMBER): ""
* Description:    Sometimes you need to update all of the deadlines against
*                 a given inventory item.
*
* Orig.Coder:     Andrew Hircock
* Recent Coder:   Andrew Hircock
* Recent Date:    Aug 20,1999
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineForInv(
      an_InvNoDbId      typn_Id,
      an_InvNoId        typn_Id,
      on_Return         OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      SetPriority
* Arguments:      al_NewEventDbId (IN NUMBER): The event with the priority to rollup
*                 al_NewEventId (IN NUMBER):   ""
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Procedure to take a supplied event and update it's priority
*                 Evaluates to the highest of:
*                     - Priority as determined by the event's deadline
*                     - the maximum priority of it's children events
*                 This procedure will not update a priority that has been set to
*                 a non-system prioirity (i.e. manually set by the user).
*
* Orig.Coder:     Rob Vandenberg
* Recent Coder:   Rob Vandenberg
* Recent Date:    December 10, 2001
*
*********************************************************************************
*
* Copyright 1998-2001 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SetPriority(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ol_Return           OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      SyncPRRequiredByDt
* Arguments:      al_EventDbId (IN NUMBER): task primary key
*                 al_EventId (IN NUMBER):   ---//---
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Updates task's children part request needed by date based on the known due date.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   N/A
* Recent Date:    October 1, 2005
*
*********************************************************************************
*
* Copyright 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
   PROCEDURE SyncPRRequiredByDt(
      al_EventDbId        IN typn_Id,
      al_EventId          IN typn_Id,
      ol_Return           OUT NUMBER
   );

/********************************************************************************
*
* PROCEDURE:      findForecastedDeadDt
* Arguments:      aAircraftDbId (IN NUMBER): aircraft primary key
*                 aAircraftId (IN NUMBER): aircraft primary key
*                 aDataTypeDbId (IN NUMBER): datatype primary key
*                 aDataTypeId (IN NUMBER): datatype primary key
*                 aRemainingUsageQt (IN NUMBER): amount of remaining usage before the deadline is due
*                 aStartDate (IN OUT DATE): the relative date to perform our calculations from.
*                    Also used to return the exact deadline date.
*                 aForecastDt (OUT DATE): the date that the deadline will become due.
*                 ol_Return (OUT NUMBER): Return 1 if success, <0 if failure
*
* Description:    Determine the forecasted deadline date for a deadline with the specified datatype on
*                 the given aircraft. The forecasted deadline date is calculated using the aircrafts
*                 flight plan and forecast model.
*
* Orig.Coder:     Neil Ouellette
* Recent Coder:   N/A
* Recent Date:    Dec 19, 2006
*
*********************************************************************************
*
* Copyright 1998-2006 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE findForecastedDeadDt(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aStartDt IN OUT date,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      ol_Return OUT NUMBER
);

/********************************************************************************
*
* Procedure: GetMainInventory
* Arguments:
*            an_EventDbId      (long) - event priamry key
*            an_EventId        (long) --//--
* Return:
*             an_InvNoDbId (long) -- main inventory primary key
*             an_InvNoId   (long)
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns main inventory for a given event.
*
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventory(
            an_EventDbId    IN  evt_event.event_db_id%TYPE,
            an_EventId      IN  evt_event.event_id%TYPE,
            an_InvNoDbId   OUT inv_inv.inv_no_db_id%TYPE,
            an_InvNoId     OUT inv_inv.inv_no_id%TYPE,
            on_Return      OUT typn_RetCode
   );


/**
 *  Recalculates the deadlines on the given aircraft
 */
PROCEDURE UpdateDeadlinesForAircraft(
          an_AircraftDbId IN inv_ac_reg.inv_no_db_id%type,
          an_AircraftId   IN inv_ac_reg.inv_no_id%type,
          on_Return OUT NUMBER )   ;

/**
 *  Recalculates the CA deadlines on all loose inventory
 */
PROCEDURE UpdateCADeadlinesLooseInv(
          an_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
          an_InvNoId   IN inv_inv.inv_no_id%TYPE,
          on_Return OUT NUMBER );        

END EVENT_PKG;
/

--changeSet MX-17611:33 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PROCEDURE_DROP('EVENT_PKG_UPDATEPREDDEADLINEDT');
END;
/

--changeSet MX-17611:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for adding baseline sync work items to the trunk
-- the script must accomplish the following tasks:
-- 1.  Create the inv_sync_queue table [zxcvbn]
-- Modify the following packages to no longer reference the inv_inv.synch_reqd_bool:
-- 		2. inv_create_body [asdfgh]
-- 		3. UpdateSyncAllInvByTaskDefnRev [qwerty]
-- 		4. UpdateSyncInvByMaintPrgm [uiopas]
-- 5.  Migrate the inv_inv table to have inventory requiring synchronization have an entry in the inv_sync_queue [dfghjk]
-- 6.  Remove the inv_inv.synch_reqd_bool [lzxcvb]
-- 7.  Add the following package: utl_lock [nmqwer]
-- 8.  Replace the baseline_sync_pkg [tyuiop]
-- 9.  Add new menu item for work item admin console [qazwsx]
/*
1.	Create the inv_sync_queue table [zxcvbn]
*/
	BEGIN
		utl_migr_schema_pkg.table_create('
			Create table INV_SYNC_QUEUE (
			INV_NO_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE,
			INV_NO_ID Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE,
			QUEUE_DATE Date NOT NULL DEFERRABLE ,
			Constraint PK_INV_SYNC_QUEUE primary key (INV_NO_DB_ID,INV_NO_ID))
		');
	END;
/

--changeSet MX-17611:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
2.	Modify the inv_create_body package to no longer use the inv_inv.inv_synch_reqd_bool [asdfgh]
*/
	CREATE OR REPLACE PACKAGE BODY INV_CREATE_PKG IS
	/*------------------- Private Procedure Declarations -----------------------*/

	-- Procedure to create a new inventory record.
	PROCEDURE AddInventoryRecord(
						   an_NewInvDbId IN typn_Id,
						   an_NewInvId IN typn_Id,
						   an_AssmblDbId IN typn_DbId,
						   as_AssmblCd IN typs_AssmblCd,
						   an_AssmblBomId IN typn_AssmblBomId,
						   an_AssmblPosId IN typn_AssmblPosId,
						   as_InvClass IN inv_inv.inv_class_cd%TYPE,
						   an_PartNoDbId IN typn_DbId,
						   an_PartNoId IN typn_PartId,
						   an_ParentInvNoDbId IN typn_DbId,
						   an_ParentInvNoId IN typn_Id,
						   an_NewInvBomPartDbId IN inv_inv.bom_part_db_id%TYPE,
						   an_NewInvBomPartId IN inv_inv.bom_part_id%TYPE,
						   on_Return OUT typn_RetCode);

	-- Procedure to get the standard part for a bom slot.
	PROCEDURE GetStandardPart(an_AssmblDbId IN typn_DbId,
							as_AssmblCd IN typs_AssmblCd,
							an_AssmblBomId IN typn_AssmblBomId,
							as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
							on_PartNoDbId OUT typn_DbId,
							on_PartNoId OUT typn_PartId,
							on_Return OUT typn_RetCode);

	-- Procedure to drill down through the full or partial bill of materials
	-- and create inventory records.
	PROCEDURE RecurseBOM( as_Mode IN VARCHAR2,
						  an_AssmblDbId IN typn_DbId,
						  as_AssmblCd IN typs_AssmblCd,
						  an_AssmblBomId IN typn_AssmblBomId,
						  an_AssmblPosId IN typn_AssmblPosId,
						  an_ParentDbId IN typn_DbId,
						  an_ParentId IN typn_Id,
						  as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
						  on_Return OUT typn_RetCode );

	-- Procedure to initialize the instance variables.
	PROCEDURE SetGlobalInfo( an_ParentItemDbId IN typn_DbId,
							 an_ParentItemId IN typn_Id,
							 on_Return OUT typn_RetCode );
							 
	/*---------------------- Package Variable declarations ------------------------*/

	-- Declare instance variables
	il_LocationDbId      inv_loc.loc_db_id%TYPE;
	il_LocationId        inv_loc.loc_id%TYPE;
	il_RootDbId          typn_DbId;
	il_RootId            typn_Id;
	il_OwnerDbId         inv_owner.owner_db_id%TYPE;
	il_OwnerId           inv_owner.owner_id%TYPE;
	il_InvCondDbId       ref_inv_cond.inv_cond_db_id%TYPE := 0;
	is_InvCondCd         ref_inv_cond.inv_cond_cd%TYPE := 'INSRV';
	idt_InstallDt        inv_inv.install_dt%TYPE;
	idt_InstallGDt       inv_inv.install_gdt%TYPE;
	idt_ReceivedDt       inv_inv.received_dt%TYPE;
	idt_ManufactDt       inv_inv.manufact_dt%TYPE;
	ib_LockedBool        inv_inv.locked_bool%TYPE;
	il_AssmblInvNoDbId   typn_DbId;
	il_AssmblInvNoId     typn_Id;
	il_PreventSynchBool  inv_inv.prevent_synch_bool%TYPE;

	/*----------------------------- Public Modules --------------------------------*/

	/********************************************************************************
	*
	*  Procedure:    CreateInventory
	*  Arguments:    an_ParentItemDbId  - parent item database identifier
	*                an_ParentItemId    - parent item identifier
	*                as_CreateType      - 'FULL' or 'PARTIAL' mode of creation
	*  Return:       on_Return (integer) - > 0 to indicate success
	*  Description:  This is the main procedure for the bulk instantiation of
	*                inventory items.
	*
	*  Orig. Coder:  Laura Cline
	*  Recent Coder: cjb
	*  Recent Date:  February 27, 2005
	*
	*********************************************************************************
	*
	*  Copyright ? 1998 MxI Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the MxI source code by any other party than
	*  MxI Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE CreateInventory( an_ParentItemDbId IN typn_DbId,
							   an_ParentItemId IN typn_Id,
							   as_CreateType IN VARCHAR2,
							   on_Return OUT typn_RetCode )
	IS
	   -- Declare local variables
	   ln_AssmblDbId     typn_DbId;
	   ln_AssmblBomId    typn_AssmblBomId;
	   ln_AssmblPosId    typn_AssmblPosId;
	   ls_AssmblCd       typs_AssmblCd;
	   ls_ApplicabilityCd inv_inv.appl_eff_cd%TYPE;

	   -- Declare a local variable to hold the invendory description return
	   -- This variable is never really used; it is only a placeholder
	   ls_Sdesc    inv_inv.inv_no_sdesc%TYPE;

	   CURSOR lcur_InvHier (cn_ParentItemDbId IN typn_DbId,
							cn_ParentItemId   IN typn_Id) IS
	   SELECT inv_no_db_id, inv_no_id
		 FROM inv_inv
		 WHERE rstat_cd = 0
		START WITH
			  inv_no_db_id = cn_ParentItemDbId  AND
			  inv_no_id    = cn_ParentItemId
		CONNECT BY
			  nh_inv_no_db_id = PRIOR inv_no_db_id AND
			  nh_inv_no_id    = PRIOR inv_no_id;


	BEGIN

	   -- Initialize the return codes
	   on_Return    := icn_NoProc;

	   -- Initialize the instance variables.
	   SetGlobalInfo( an_ParentItemDbId,
					  an_ParentItemId,
					  on_Return );
	   IF on_Return < 0 THEN
		  RETURN;
	   END IF;

	   -- Update the root inventory's description
	   INV_DESC_PKG.InvUpdInvDesc( an_ParentItemDbId,
								   an_ParentItemId,
								   ls_Sdesc,
								   on_Return );
	   IF on_Return < 0 THEN
		  RETURN;
	   END IF;

	   -- Determine the assembly id of the root item.
	   SELECT assmbl_db_id,
			  assmbl_cd,
			  assmbl_bom_id,
			  assmbl_pos_id
		 INTO ln_AssmblDbId,
			  ls_AssmblCd,
			  ln_AssmblBomId,
			  ln_AssmblPosId
		 FROM inv_inv
		WHERE inv_no_db_id = an_ParentItemDbId
		  AND inv_no_id    = an_ParentItemId 
		  AND rstat_cd = 0;

	   -- If the root item has an assembly, recurse and evaluate completeness
	   IF ( ln_AssmblDbId IS NOT NULL ) THEN

		  -- get the applicability code
		  select
				inv_inv.appl_eff_cd
		  into
				ls_ApplicabilityCd
		  from
				inv_inv
		  where
			 inv_inv.inv_no_db_id =  an_ParentItemDbId AND
			 inv_inv.inv_no_id    =  an_ParentItemId
			 AND
			 inv_inv.rstat_cd	= 0;
	   
		  -- Recurse through the bill of materials to create inventory records.
		  RecurseBOM( as_CreateType,
					  ln_AssmblDbId,
					  ls_AssmblCd,
					  ln_AssmblBomId,
					  ln_AssmblPosId,
					  an_ParentItemDbId,
					  an_ParentItemId,
					  ls_ApplicabilityCd,
					  on_Return );
		  IF on_Return < 0 THEN
			 RETURN;
		  END IF;

		  -- Evaluate the completeness of the new assembly
		  INV_COMPLETE_PKG.EvaluateAssemblyCompleteness(
								  an_ParentItemId,
								  an_ParentItemDbId,
								  on_Return );
		  IF on_Return < 0 THEN
			 RETURN;
		  END IF;

		  -- Evaluate the completeness of the parent inventory tree
		  INV_COMPLETE_PKG.UpdateCompleteFlagOnInstall(
								  an_ParentItemId,
								  an_ParentItemDbId,
								  on_Return );
		  IF on_Return < 0 THEN
			 RETURN;
		  END IF;
	   END IF;

	   -- Go through the new inventory hierarchy and initialize usage

	   FOR lrec_Inv IN lcur_InvHier(an_ParentItemDbId, an_ParentItemId) LOOP

		  InitializeCurrentUsage(lrec_Inv.inv_no_db_id, lrec_Inv.inv_no_id, on_Return);
		  IF on_Return < 0 THEN
			 RETURN;
		  END IF;
	   END LOOP;

	   -- If everything executed successfully, set the return to indicate success.
	   on_Return := icn_Success;

	/*-------------------------- Exception Handling ----------------------------*/
	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@CreateInventory@@@' || SQLERRM);
		  RETURN;
	END CreateInventory;


	/********************************************************************************
	*
	*  Procedure:    InvCreateSys
	*  Arguments:    an_AssmblDbId (long)  - new config slot
	*                an_AssmblCd (string)  - new config slot
	*                an_AssmblBomId (long) - new config slot
	*  Return:       on_Return long  - > 0 to indicate success
	*  Description:  This procedure creates new SYS inventory items when the user creates
	*                new SYS config slots in the Baseliner.
	*
	*  Orig. Coder:  jprimeau
	*  Recent Coder: 
	*  Recent Date:  2007-03-28
	*
	*********************************************************************************
	*
	*  Copyright ? 2006 Mxi Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the MxI source code by any other party than
	*  MxI Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE InvCreateSys (
		  an_AssmblDbId   IN eqp_assmbl_bom.assmbl_db_id%TYPE,
		  an_AssmblCd     IN eqp_assmbl_bom.assmbl_cd%TYPE,
		  an_AssmblBomId  IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
		  on_Return       OUT NUMBER
	   ) IS

		  -- find all inventory that are missing the new SYS config slot.
		  CURSOR lcur_InventoryRecords (
			 cn_AssmblDbId   IN eqp_assmbl_pos.assmbl_db_id%TYPE,
			 cn_AssmblCd     IN eqp_assmbl_pos.assmbl_cd%TYPE,
			 cn_AssmblBomId  IN eqp_assmbl_pos.assmbl_bom_id%TYPE
		  ) IS
			 SELECT inv_inv.inv_no_db_id,
					inv_inv.inv_no_id,
					inv_inv.inv_class_cd,
					eqp_assmbl_pos.assmbl_pos_id
			   FROM inv_inv,
					eqp_assmbl_pos,
					eqp_assmbl_bom parent_bom
			  WHERE eqp_assmbl_pos.assmbl_db_id  = cn_AssmblDbId  AND 
					eqp_assmbl_pos.assmbl_cd     = cn_AssmblCd    AND 
					eqp_assmbl_pos.assmbl_bom_id = cn_AssmblBomId
					AND
					parent_bom.assmbl_db_id = eqp_assmbl_pos.nh_assmbl_db_id AND
					parent_bom.assmbl_cd    = eqp_assmbl_pos.nh_assmbl_cd    AND
					parent_bom.assmbl_bom_id = eqp_assmbl_pos.nh_assmbl_bom_id
					AND
					(
					 (
					  parent_bom.bom_class_cd = 'ROOT' 
					  AND
					  inv_inv.orig_assmbl_db_id   = eqp_assmbl_pos.nh_assmbl_db_id   AND
					  inv_inv.orig_assmbl_cd      = eqp_assmbl_pos.nh_assmbl_cd
					  AND
				  inv_inv.rstat_cd	= 0
					  )
					OR
					  (
					   parent_bom.bom_class_cd <> 'ROOT' 
					   AND
					   inv_inv.assmbl_db_id   = eqp_assmbl_pos.nh_assmbl_db_id   AND
					   inv_inv.assmbl_cd      = eqp_assmbl_pos.nh_assmbl_cd      AND
					   inv_inv.assmbl_bom_id  = eqp_assmbl_pos.nh_assmbl_bom_id
					   AND
				   inv_inv.rstat_cd	= 0
					  )
					);
		  lrec_InventoryRecords   lcur_InventoryRecords%ROWTYPE;

	 
	   -- Declare local variables
	   ln_NewInvDbId         typn_DbId;
	   ln_NewInvId           typn_Id;
	   ln_True               inv_inv.complete_bool%TYPE := 1;
		-- Declare exceptions
		xc_SetCompleteFlagFail    EXCEPTION;         

	   BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;
		  
	   -- Call the creation procedure for each inventory found
	   FOR lrec_InventoryRecords IN lcur_InventoryRecords(an_AssmblDbId, an_AssmblCd, an_AssmblBomId)
	   LOOP
		  -- Initialize the instance variables.
		  SetGlobalInfo( lrec_InventoryRecords.inv_no_db_id,
						 lrec_InventoryRecords.inv_no_id,
						 on_Return );
		  IF on_Return < 0 THEN
			 RETURN;
		  END IF;      
		  
		  -- reset the assembly inventory key to the parent if the parent is an assembly
		  IF lrec_InventoryRecords.inv_class_cd = 'ASSY' THEN
			 il_AssmblInvNoDbId := lrec_InventoryRecords.inv_no_db_id;
			   il_AssmblInvNoId   := lrec_InventoryRecords.inv_no_id;
			END IF;   
		  
	   -- Get the next inventory DB_ID
	   SELECT db_id
			INTO ln_NewInvDbId
	   FROM mim_local_db;
		 
	   -- Get the next inventory id from the sequence.
	   SELECT inv_no_id_seq.NEXTVAL
	   INTO ln_NewInvId
	   FROM dual;
		
	   -- Add a row to the inventory table for the new item.
	   AddInventoryRecord( ln_NewInvDbId,
						   ln_NewInvId,
						   an_AssmblDbId,
						   an_AssmblCd,
						   an_AssmblBomId,
						   lrec_InventoryRecords.assmbl_pos_id,
						   'SYS',
						   NULL,
						   NULL,
					   lrec_InventoryRecords.inv_no_db_id,
					  lrec_InventoryRecords.inv_no_id,
						   NULL,
						   NULL,
								on_Return);
	   
			 IF on_Return < 0 THEN
			   RETURN;
			 END IF;

		  -- Set the complete flag for the inventory item to TRUE.
		  INV_COMPLETE_PKG.SetCompleteFlag( ln_NewInvId, 
						   ln_NewInvDbId,
						   ln_True,
						   on_Return );
		  IF on_Return <= 0 THEN
			  RAISE xc_SetCompleteFlagFail;
		  END IF;
		END LOOP;

	   -- If everything executed successfully, set the return to indicate success.
	   on_Return := icn_Success;

	/*-------------------------- Exception Handling ----------------------------*/
	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@InvCreateSys@@@' || SQLERRM);
		  RETURN;
	END InvCreateSys;


	/********************************************************************************
	*
	*  Procedure:   InitializeCurrentUsage
	*  Arguments:   an_ItemDbId   - inventory item identifier
	*               an_ItemId     - inventory item identifier
	*  Return:       (number) - 1 to indicate success, -1 to indicate failure
	*  Description:  This procedure initializes the current usage informaton
	*                for the specified inventory item.
	*                Basically, it takes the inventory's part no and then
	*                determines which bom items this part no can be assigned to.
	*                Once all of the bom items have been determined, a superset of
	*                the usage parms from these bom items is generated.
	*
	*  Orig. Coder:  Laura Cline
	*  Recent Coder: cjb
	*  Recent Date:  February 27, 2005
	*
	*********************************************************************************
	*
	*  Copyright ? 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the MxI source code by any other party than
	*  MxI Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE InitializeCurrentUsage( an_ItemDbId IN  typn_DbId,
									  an_ItemId   IN  typn_Id,
									  on_Return   OUT typn_RetCode )
	IS

	   -- Usage definition cursor
	   /* get usage parms from the all inventory assembly position */
	   CURSOR lcur_UsageDefn (
			 cn_InvNoDbId IN typn_DbId,
			 cn_InvNoId   IN typn_Id
		  ) IS
	   /* Since we have no idea what the input inventory is, we'll just search for all data types */
	   SELECT DISTINCT
				 mim_part_numdata.data_type_db_id,
				 mim_part_numdata.data_type_id
			FROM inv_inv,
				 mim_part_numdata,
				 ref_inv_class
		   WHERE inv_inv.inv_no_db_id =  cn_InvNoDbId AND
				 inv_inv.inv_no_id    =  cn_InvNoId
				 AND
				 inv_inv.rstat_cd	= 0
				 AND
				 ref_inv_class.inv_class_db_id = inv_inv.inv_class_db_id AND
				 ref_inv_class.inv_class_cd = inv_inv.inv_class_cd
				 AND
				 ( ref_inv_class.serial_bool = 1 OR inv_inv.inv_class_cd = 'SYS' )
				 AND
				 mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id  AND
				 mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd     AND
				 mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
				 AND
				 NOT EXISTS
					 ( SELECT 1
						 FROM inv_curr_usage
						WHERE inv_no_db_id = inv_inv.inv_no_db_id AND
							  inv_no_id    = inv_inv.inv_no_id
							  AND
							  data_type_db_id = mim_part_numdata.data_type_db_id AND
							  data_type_id    = mim_part_numdata.data_type_id );
	   lrec_UsageDefn lcur_UsageDefn%ROWTYPE;

	   -- Some variables
	   ls_InvClassCd  inv_inv.inv_no_sdesc%TYPE;
	   ln_tsn_qt      inv_curr_usage.tsn_qt%TYPE;
	   ln_tso_qt      inv_curr_usage.tso_qt%TYPE;
	   ln_tsi_qt      inv_curr_usage.tsi_qt%TYPE;

	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;

	   -- Get inventory class
	   SELECT   inv_class_cd
	   INTO     ls_InvClassCd
	   FROM     inv_inv
	   WHERE    inv_inv.inv_no_db_id = an_ItemDbId  AND
				inv_inv.inv_no_id    = an_ItemId AND
				inv_inv.rstat_cd = 0;

	   -- For each of the data types defined for the item's part no, set the current usage values
	   IF ls_InvClassCd = 'SYS' THEN
		  -- For SYSTEM, initialize the current usage to the ASSMBL_INV_NO_PK's current usage

		  FOR lrec_UsageDefn IN lcur_UsageDefn( an_ItemDbId, an_ItemId )
		  LOOP
			 -- Get tsn/tso/tsi values from the assembl inventory
			 SELECT
				decode( inv_curr_usage.tsn_qt, null, 0, inv_curr_usage.tsn_qt ),
				decode( inv_curr_usage.tso_qt, null, 0, inv_curr_usage.tso_qt ),
				decode( inv_curr_usage.tsi_qt, null, 0, inv_curr_usage.tsi_qt )
			 INTO
				ln_tsn_qt,
				ln_tso_qt,
				ln_tsi_qt
			 FROM
				inv_curr_usage,
				inv_inv
			 WHERE
				inv_inv.inv_no_db_id = an_ItemDbId  AND
				inv_inv.inv_no_id    = an_ItemId
				AND
				inv_inv.rstat_cd	= 0
				AND
				-- Outer join in case the inventory does not have this data type yet.
				inv_curr_usage.inv_no_db_id   (+)= inv_inv.assmbl_inv_no_db_id AND
				inv_curr_usage.inv_no_id      (+)= inv_inv.assmbl_inv_no_id
				AND
				inv_curr_usage.data_type_db_id   (+)= lrec_UsageDefn.data_type_db_id AND
				inv_curr_usage.data_type_id      (+)= lrec_UsageDefn.data_type_id;

			 -- Insert a new row into the current usage table.
			 INSERT INTO inv_curr_usage
					( data_type_db_id,
					  data_type_id,
					  inv_no_db_id,
					  inv_no_id,
					  tsn_qt,
					  tso_qt,
					  tsi_qt )
			 VALUES ( lrec_UsageDefn.data_type_db_id,
					  lrec_UsageDefn.data_type_id,
					  an_ItemDbId,
					  an_ItemId,
					  ln_tsn_qt,
					  ln_tso_qt,
					  ln_tsi_qt );
		  END LOOP;
	   ELSE
		  -- For non-SYSTEM, initialize the current usage to 0
		  FOR lrec_UsageDefn IN lcur_UsageDefn( an_ItemDbId, an_ItemId )
		  LOOP

			 -- Insert a new row into the current usage table.
			 INSERT INTO inv_curr_usage
					( data_type_db_id,
					  data_type_id,
					  inv_no_db_id,
					  inv_no_id,
					  tsn_qt,
					  tso_qt,
					  tsi_qt )
			 VALUES ( lrec_UsageDefn.data_type_db_id,
					  lrec_UsageDefn.data_type_id,
					  an_ItemDbId,
					  an_ItemId,
					  0,
					  0,
					  0 );
		  END LOOP;
	   END IF;

	   -- Return success
	   on_Return := icn_Success;

	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError( 'DEV-99999', 'INV_CREATE_PKG@@@InitializeCurrentUsage@@@' || SQLERRM );
		  RETURN;
	END InitializeCurrentUsage;


	/*--------------------------- Private Modules ------------------------------*/

	/********************************************************************************
	*
	*  Procedure:   RecurseBom
	*  Arguments:   as_Mode        - 'FULL' or 'PARTIAL' mode of recursion
	*               an_AssmblDbId  - assembly database identifier
	*               as_AssmblCd    - assembly code
	*               an_AssmblBomId - assembly bill of materials identifier
	*               an_AssmblPosId - assembly position identifier
	*               an_ParentDbId - parent inventory item
	*               an_ParentId   - ""
	*  Return:      (integer) - > 0 to indicate success
	*  Description: This procedure recurses through the full or partial bill of
	*               materials, creating inventory records.
	*
	*  Orig.Coder:   Laura Cline
	*  Recent Coder: Daniel Baxter
	*  Date:         December 18, 2008
	*
	*********************************************************************************
	*
	*  Copyright ? 1998-2008 MxI Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the MxI source code by any other party than
	*  MxI Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE RecurseBOM( as_Mode IN VARCHAR2,
						  an_AssmblDbId IN typn_DbId,
						  as_AssmblCd IN typs_AssmblCd,
						  an_AssmblBomId IN typn_AssmblBomId,
						  an_AssmblPosId IN typn_AssmblPosId,
						  an_ParentDbId IN typn_DbId,
						  an_ParentId IN typn_Id,
						  as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
						  on_Return OUT typn_RetCode )
	IS
	   -- Declare local variables
	   ln_PartNoDbId         typn_DbId;
	   ln_PartNoId           typn_PartId;
	   ln_NewInvDbId         typn_DbId;
	   ln_NewInvId           typn_Id;
	   ls_InvClass           inv_inv.inv_class_cd%TYPE;
	   ln_NewInvBomPartDbId  inv_inv.bom_part_db_id%TYPE;
	   ln_NewInvBomPartId    inv_inv.bom_part_id%TYPE;

	   -- Declare a bill of materials cursor based on the mode of recursion
	   -- specified; full to create all inventory, partial to create only
	   -- systems.
	   CURSOR lcur_Bom(
			 cs_Mode IN VARCHAR2,
			 cn_AssmblDbId IN typn_DbId,
			 cn_AssmblBomId IN typn_AssmblBomId,
			 cn_AssmblPosId IN typn_AssmblPosId,
			 cs_AssmblCd IN typs_AssmblCd
		  ) IS
		  SELECT BOM.assmbl_db_id,
				 BOM.assmbl_cd,
				 BOM.assmbl_bom_id,
				 BOM.bom_class_cd,
				 POS.assmbl_pos_id
			FROM eqp_assmbl_bom BOM,
				 eqp_assmbl_pos POS
		   WHERE cs_Mode = 'FULL'
			 AND POS.nh_assmbl_db_id  = cn_AssmblDbId
			 AND POS.nh_assmbl_cd     = cs_AssmblCd
			 AND POS.nh_assmbl_bom_id = cn_AssmblBomId
			 AND POS.nh_assmbl_pos_id = cn_AssmblPosId
			 AND BOM.assmbl_db_id  = POS.assmbl_db_id
			 AND BOM.assmbl_cd     = POS.assmbl_cd
			 AND BOM.assmbl_bom_id = POS.assmbl_bom_id
			 AND BOM.bom_class_cd    != 'SUBASSY'
			 AND BOM.mandatory_bool   = 1
			 AND BOM.rstat_cd        <> 2
			 AND POS.rstat_cd     <> 2
		   UNION
		  SELECT BOM.assmbl_db_id,
				 BOM.assmbl_cd,
				 BOM.assmbl_bom_id,
				 BOM.bom_class_cd,
				 POS.assmbl_pos_id
			FROM eqp_assmbl_bom BOM,
				 eqp_assmbl_pos POS
		   WHERE cs_Mode != 'FULL'
			 AND POS.nh_assmbl_db_id  = cn_AssmblDbId
			 AND POS.nh_assmbl_cd     = cs_AssmblCd
			 AND POS.nh_assmbl_bom_id = cn_AssmblBomId
			 AND POS.nh_assmbl_pos_id = cn_AssmblPosId
			 AND BOM.assmbl_db_id  = POS.assmbl_db_id
			 AND BOM.assmbl_cd     = POS.assmbl_cd
			 AND BOM.assmbl_bom_id = POS.assmbl_bom_id
			 AND BOM.bom_class_cd     = 'SYS'
			 AND BOM.mandatory_bool   = 1
			 AND BOM.rstat_cd        <> 2
			 AND POS.rstat_cd     <> 2;
	   lrec_Bom lcur_Bom%ROWTYPE;

	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;

	   -- Find all of the child bom items underneath the given bom item
	   FOR lrec_Bom IN lcur_Bom(as_Mode,
								an_AssmblDbId,
								an_AssmblBomId,
								an_AssmblPosId,
								as_AssmblCd
								)
	   LOOP
		  IF (INV_COMPLETE_PKG.isApplicableBOMItem(lrec_Bom.assmbl_db_id,
								 lrec_Bom.assmbl_cd,
								 lrec_Bom.assmbl_bom_id,
								 lrec_Bom.assmbl_pos_id,
								 as_ApplicabilityCd,
								 on_Return
								 )) THEN
	   
			-- Get the standard or default part information.
			GetStandardPart(lrec_Bom.assmbl_db_id,
							lrec_Bom.assmbl_cd,
							lrec_Bom.assmbl_bom_id,
							as_ApplicabilityCd,
							ln_PartNoDbId,
							ln_PartNoId,
							on_Return);
			IF on_Return < 0 THEN
			   RETURN;
			END IF;
	  
			-- Check the BOM class of the retrieved assembly; assign an inventory class
			-- based on this BOM class
			IF lrec_Bom.bom_class_cd = 'SYS' THEN
			   ls_InvClass := 'SYS';
			ELSIF lrec_Bom.bom_class_cd = 'SUBASSY' THEN
				ls_InvClass := 'ASSY';
			ELSIF lrec_Bom.bom_class_cd = 'TRK' THEN
				ls_InvClass := 'TRK';
			END IF;
	  
			-- Get the BOM Part for the new inventory
			IF ( ls_InvClass = 'SYS' ) THEN
	  
			   -- SYS inventory's BOM Part key must be null
			   ln_NewInvBomPartDbId := NULL;
			   ln_NewInvBomPartId   := NULL;
			ELSE
	  
				-- Lookup BOM Part key for all other inventory types
				SELECT eqp_bom_part.bom_part_db_id,
					   eqp_bom_part.bom_part_id
				  INTO ln_NewInvBomPartDbId,
					   ln_NewInvBomPartId
				  FROM eqp_bom_part,
					   ref_inv_class
				 WHERE eqp_bom_part.assmbl_db_id  = lrec_Bom.assmbl_db_id
				   AND eqp_bom_part.assmbl_cd     = lrec_Bom.assmbl_cd
				   AND eqp_bom_part.assmbl_bom_id = lrec_Bom.assmbl_bom_id
				   AND ref_inv_class.inv_class_db_id = eqp_bom_part.inv_class_db_id
				   AND ref_inv_class.inv_class_cd    = eqp_bom_part.inv_class_cd
				   AND ref_inv_class.tracked_bool  = 1
				   AND ref_inv_class.rstat_cd    = 0;
			END IF;
	  
			-- Test for errors before continuing.  If no standard part was
			-- found, then the bom slot specified will be left empty in the
			-- inventory hierarchy.  This is not an error condition that we
			-- should stop processing on.
			IF ( ( lrec_Bom.bom_class_cd = 'SYS' AND on_Return >= 0 ) OR ( lrec_Bom.bom_class_cd <> 'SYS' AND on_Return > 0 ) ) THEN
	  
			   -- Get the next inventory DB_ID
			   SELECT db_id
				 INTO ln_NewInvDbId
				 FROM mim_local_db;
	  
			   -- Get the next inventory id from the sequence.
			   SELECT inv_no_id_seq.NEXTVAL
				 INTO ln_NewInvId
				 FROM dual;
	  
			   -- Add a row to the inventory table for the new item.
			   AddInventoryRecord( ln_NewInvDbId,
								   ln_NewInvId,
								   lrec_Bom.assmbl_db_id,
								   lrec_Bom.assmbl_cd,
								   lrec_Bom.assmbl_bom_id,
								   lrec_Bom.assmbl_pos_id,
								   ls_InvClass,
								   ln_PartNoDbId,
								   ln_PartNoId,
								   an_ParentDbId,
								   an_ParentId,
								   ln_NewInvBomPartDbId,
								   ln_NewInvBomPartId,
								   on_Return);
			   IF on_Return < 0 THEN
				  RETURN;
			   END IF;
	  
			   -- Recurse through the children of the new item.
			   RecurseBOM( as_Mode,
						   lrec_Bom.assmbl_db_id,
						   lrec_Bom.assmbl_cd,
						   lrec_Bom.assmbl_bom_id,
						   lrec_Bom.assmbl_pos_id,
						   ln_NewInvDbId,
						   ln_NewInvId,
						   as_ApplicabilityCd,
						   on_Return );
			   IF on_Return < 0 THEN
				  RETURN;
			   END IF;
			END IF;
		  END IF;
	   END LOOP;

	   -- If all of the loops executed successfully, set the return code
	   -- to indicate success.
	   on_Return := icn_Success;

	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@RecurseBOM@@@' || SQLERRM);
		  RETURN;
	END RecurseBOM;


	/********************************************************************************
	*
	*  Procedure: GetStandardPart
	*  Arguments: an_AssmblDbId  - assembly database identifier
	*             as_AssmblCd  - assembly code
	*             an_AssmblBomId  - assembly bill of materials identifier
	*             as_ApplicabilityCd - applicability code of the assembly/aircraft being constructed
	*  Return:    on_PartNoDbId   - part number database identifier
	*             on_PartNoId  - part number identifier
	*             on_Return    - return code
	*  Description: This procedure selects the standard part for the specified
	*              assembly slot.  If no standard is found or it is not applicable, 
	*            the first part found is selected.  If no part is found, an 
	*              error is returned.
	*
	*  Orig. Coder:  Laura Cline
	*  Recent Coder: Daniel Baxter
	*  Recent Date:  December 18, 2008
	*
	*********************************************************************************
	*
	*  Copyright ? 1998-2008 MxI Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the MxI source code by any other party than
	*  MxI Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE GetStandardPart(an_AssmblDbId IN typn_DbId,
						as_AssmblCd IN typs_AssmblCd,
						an_AssmblBomId IN typn_AssmblBomId,
						as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
						on_PartNoDbId OUT typn_DbId,
						on_PartNoId OUT typn_PartId,
						on_Return OUT typn_RetCode)
	IS
	BEGIN

	   -- Initialize the return code to indicate no processing.
	   on_Return := icn_NoProc;

	   -- Select the part to be installed for the specified assembly item. It defaults to the standard part unless the 
	   -- standard part is not applicable then it grabs the first applicable part as sorted by part number
	   SELECT 
		  applicable_parts.part_no_db_id,
		  applicable_parts.part_no_id
	   INTO 
		  on_PartNoDbId, 
		  on_PartNoId
	   FROM
		  (
		  SELECT 
			 eqp_part_no.part_no_db_id,
			 eqp_part_no.part_no_id
		  FROM   
			 eqp_part_baseline,
			 eqp_bom_part,
			 ref_inv_class,
			 eqp_part_no
		  WHERE 
			 eqp_bom_part.assmbl_db_id  = an_AssmblDbId AND 
			 eqp_bom_part.assmbl_cd     = as_AssmblCd AND 
			 eqp_bom_part.assmbl_bom_id = an_AssmblBomId
			 AND 
			 ref_inv_class.inv_class_db_id = eqp_bom_part.inv_class_db_id AND 
			 ref_inv_class.inv_class_cd    = eqp_bom_part.inv_class_cd AND 
			 ref_inv_class.tracked_bool    = 1 	  AND
			 ref_inv_class.rstat_cd        = 0
			 AND
			 eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND 
			 eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id AND 
			 eqp_part_baseline.rstat_cd       <> 2
			 AND
			 eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
			 eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id
			 AND
			 isApplicable(eqp_part_baseline.appl_eff_ldesc, as_ApplicabilityCd ) = 1
		  ORDER BY 
			 eqp_part_baseline.standard_bool DESC, 
			 eqp_part_no.part_no_oem
		  ) applicable_parts
	   WHERE
		  ROWNUM = 1;

	   -- Set the return code to indicate success
	   on_Return := icn_Success;

	EXCEPTION
	   WHEN NO_DATA_FOUND THEN
		  -- Set the return code to indicate no processing, and return.
		  on_Return := icn_NoProc;
		  RETURN;
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@GetStandadrPart@@@' || SQLERRM);
		  RETURN;
	END GetStandardPart;

	/********************************************************************************
	*
	*  Procedure:    AddInventoryRecord
	*  Arguments:    an_NewInvDbId        - the ID of the new inventory row to add
	*                an_NewInvId          - ""
	*                an_AssmblDbId        - assembly database identifier
	*                as_AssmblCd          - assembly code
	*                an_AssmblBomId       - assembly bill of materials id
	*                an_AssmblPosId       - assembly position identifier
	*                as_InvClass          - inventory class code
	*                an_PartNoDbId        - part number database identifier
	*                an_PartNoId          - part number identifier
	*                an_ParentInvNoDbId   - parent's inv no
	*                an_ParentInvNoId     - ""
	*                an_NewInvBomPartDbId - the Bom Part Db Id of the new inventory
	*                an_NewInvBomPartId   - the Bom Part Id of the new inventory
	*  Return:       (number) - 1 to indicate success, -1 to indicate failure
	*  Description:  This procedure adds a new row to the inventory table.
	*
	*  Orig. Coder:  Laura Cline
	*  Recent Coder: cjb
	*  Recent Date:  February 27, 2005
	*
	*********************************************************************************
	*
	*  Copyright ? 1998-2002 Mxi Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the Mxi source code by any other party than
	*  Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE AddInventoryRecord(
						   an_NewInvDbId IN typn_Id,
						   an_NewInvId IN typn_Id,
						   an_AssmblDbId IN typn_DbId,
						   as_AssmblCd IN typs_AssmblCd,
						   an_AssmblBomId IN typn_AssmblBomId,
						   an_AssmblPosId IN typn_AssmblPosId,
						   as_InvClass IN inv_inv.inv_class_cd%TYPE,
						   an_PartNoDbId IN typn_DbId,
						   an_PartNoId IN typn_PartId,
						   an_ParentInvNoDbId IN typn_DbId,
						   an_ParentInvNoId IN typn_Id,
						   an_NewInvBomPartDbId IN inv_inv.bom_part_db_id%TYPE,
						   an_NewInvBomPartId IN inv_inv.bom_part_id%TYPE,
						   on_Return OUT typn_RetCode)
	IS

	   -- Declare local variables
	   ls_Sdesc    inv_inv.inv_no_sdesc%TYPE;
	  
	   CURSOR getParentInvCursor(cl_InvDbId NUMBER, cl_InvId NUMBER) IS
		  SELECT inv.inv_no_db_id, inv.inv_no_id, inv.po_db_id, inv.po_id, owner.local_bool 
				 FROM INV_INV inv, INV_OWNER owner 
				 WHERE inv_no_db_id=cl_InvdBiD AND inv_no_id=cl_InvId
					   AND
					   inv.owner_db_id=owner.owner_db_id AND 
					   inv.owner_id=owner.owner_id
					   AND
				   inv.rstat_cd	= 0;
	   
	   CURSOR mostRecentShipmentCursor(cl_PoDbId NUMBER, cl_PoId NUMBER) IS
		  SELECT po.po_type_cd, line.return_inv_no_db_id, line.return_inv_no_id 
				 FROM PO_HEADER po, PO_LINE line 
				 WHERE po.po_db_id   = cl_PoDbId AND 
					   po.po_id      = cl_PoId 
					   AND
					   line.po_db_id = cl_PoDbId AND
					   line.po_id    = cl_PoId
					   AND
				   po.rstat_cd	= 0;
				  
	   lTypeStr VARCHAR2(8);

	   lParentInvRec getParentInvCursor%ROWTYPE;         
	   lMostRecentShipmentRec mostRecentShipmentCursor%ROWTYPE;
	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;


	   OPEN getParentInvCursor(an_NewInvDbId, an_NewInvId);
		  FETCH getParentInvCursor INTO lParentInvRec;
		  -- Initially assume all further cases fails, so set ownership type to OTHER
		  lTypeStr:='OTHER';
		  IF (lParentInvRec.local_bool=1) THEN
			 -- If inventory owner is INV_OWNER.LOCAL_BOOL = 1 owner, set ownership type to LOCAL
			 lTypeStr:='LOCAL';
		  ELSE
			 IF ((lParentInvRec.po_db_id IS NOT NULL) AND (lParentInvRec.po_id IS NOT NULL)) THEN
				OPEN mostRecentShipmentCursor(lParentInvRec.po_db_id, lParentInvRec.po_id);
				FETCH mostRecentShipmentCursor INTO lMostRecentShipmentRec;
				IF (mostRecentShipmentCursor%FOUND) THEN
				   -- If most recent shipment was incoming shipment received on exchange PO 
				   -- && no return inventory has been selected. Set ownership type to EXCHRCVD
				   IF (lMostRecentShipmentRec.po_type_cd = 'EXCHANGE' AND 
					   lMostRecentShipmentRec.return_inv_no_db_id IS NULL AND
					   lMostRecentShipmentRec.return_inv_no_id IS NULL) THEN
					  lTypeStr:='EXCHRCVD';
				   -- If most recent shipment was outgoing shipment on an exchange PO
				   -- && is return inventory. Set ownership type to EXCHRTRN
				   ELSIF (lMostRecentShipmentRec.po_type_cd = 'EXCHANGE' AND 
					   lMostRecentShipmentRec.return_inv_no_db_id = lParentInvRec.inv_no_db_id AND
					   lMostRecentShipmentRec.return_inv_no_id = lParentInvRec.inv_no_id) THEN
					  lTypeStr:='EXCHRTRN';
				   -- If most recent incoming shipment is on an borrow PO
				   -- Set ownership type to BORROW
				   ELSIF (lMostRecentShipmentRec.po_type_cd = 'BORROW') THEN
					  lTypeStr:='BORROW';
				   -- If most recent shipment was outgoing shipment on an consignment PO
				   -- && no return inventory. Set ownership type to CSGNRCVD
				   ELSIF (lMostRecentShipmentRec.po_type_cd = 'CONSIGN' AND 
					   lMostRecentShipmentRec.return_inv_no_db_id IS NULL AND
					   lMostRecentShipmentRec.return_inv_no_id IS NULL) THEN
					  lTypeStr:='CSGNRCVD';
				   -- If most recent shipment was outgoing shipment on an consignment PO
				   -- && is return inventory. Set ownership type to CSGNRTRN
				   ELSIF (lMostRecentShipmentRec.po_type_cd = 'CONSIGN' AND 
					   lMostRecentShipmentRec.return_inv_no_db_id = lParentInvRec.inv_no_db_id AND
					   lMostRecentShipmentRec.return_inv_no_id = lParentInvRec.inv_no_id) THEN
					  lTypeStr:='CSGNRTRN';
				   END IF;
				END IF;
				CLOSE mostRecentShipmentCursor;
			 END IF;
		  END IF;
	   CLOSE getParentInvCursor;
	  
	   -- Insert a new row into the inventory table.
	   INSERT INTO inv_inv (
			  inv_no_db_id,
			  inv_no_id,
			  inv_class_db_id,
			  inv_class_cd,
			  bom_part_db_id,
			  bom_part_id,
			  loc_db_id,
			  loc_id,
			  part_no_db_id,
			  part_no_id,
			  h_inv_no_db_id,
			  h_inv_no_id,
			  nh_inv_no_db_id,
			  nh_inv_no_id,
			  assmbl_db_id,
			  assmbl_cd,
			  assmbl_bom_id,
			  assmbl_pos_id,
			  owner_db_id,
			  owner_id,
			  owner_type_db_id,
			  owner_type_cd,
			  complete_bool,
			  install_dt,
			  install_gdt,
			  received_dt,
			  manufact_dt,
			  used_bool,
			  serial_no_oem,
			  inv_cond_db_id,
			  inv_cond_cd,
			  assmbl_inv_no_db_id,
			  assmbl_inv_no_id,
			  reserved_bool,
			  barcode_sdesc,
			  locked_bool,
			  issued_bool,
			  prevent_synch_bool)
	   VALUES (
			  an_NewInvDbId,
			  an_NewInvId,
			  0,
			  as_InvClass,
			  an_NewInvBomPartDbId,
			  an_NewInvBomPartId,
			  il_LocationDbId,
			  il_LocationId,
			  an_PartNoDbId,
			  an_PartNoId,
			  il_RootDbId,
			  il_RootId,
			  an_ParentInvNoDbId,
			  an_ParentInvNoId,
			  an_AssmblDbId,
			  as_AssmblCd,
			  an_AssmblBomId,
			  an_AssmblPosId,
			  il_OwnerDbId,
			  il_OwnerId,
			  0,
			  lTypeStr,
			  0,
			  NULL,
			  NULL,
			  idt_ReceivedDt,
			  idt_ManufactDt,
			  0,
			  'XXX',
			  il_InvCondDbId,
			  is_InvCondCd,
			  il_AssmblInvNoDbId,
			  il_AssmblInvNoId,
			  0,
			  GENERATE_INV_BARCODE(),
			  ib_LockedBool,
			  1,
			  il_PreventSynchBool);

	   -- Set the inventory short description for the new item.
	   INV_DESC_PKG.InvUpdInvDesc( an_NewInvDbId,
								   an_NewInvId,
								   ls_Sdesc,
								   on_Return );

	   -- Set the inventory config position description for the new created inventory                               
	   IF ( as_InvClass = 'ASSY' OR as_InvClass = 'SYS' OR as_InvClass = 'TRK' ) THEN
		   INV_DESC_PKG.UpdateInvConfigPosDesc( an_NewInvDbId,
												 an_NewInvId,
												 on_Return );
	   END IF;
						
	   IF on_Return < 0 THEN
		  RETURN;
	   END IF;

	   -- Return success
	   on_Return := icn_Success;

	/*------------------------------- Exception Handling --------------------------*/
	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@AddInventoryRecord@@@' || SQLERRM);
		  IF mostRecentShipmentCursor%ISOPEN THEN CLOSE mostRecentShipmentCursor; END IF;
		  IF getParentInvCursor%ISOPEN       THEN CLOSE getParentInvCursor;       END IF;
		  RETURN;
	END AddInventoryRecord;


	/********************************************************************************
	*
	*  Procedure:    SetGlobalInfo
	*  Arguments:    an_ParentItemDbId  - parent inventory database identifier
	*                an_ParentItemId - parent inventory identifier
	*  Return:       (number) - > 0 to indicate success
	*  Description:  This procedure initializes the instance variables.
	*
	*  Orig. Coder:  Laura Cline
	*  Recent Coder: cjb
	*  Recent Date:  February 27, 2005
	*
	*********************************************************************************
	*
	*  Copyright ? 1998, 1999, 2000 Mxi Technologies Ltd.  All Rights Reserved.
	*  Any distribution of the Mxi source code by any other party than
	*  Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE SetGlobalInfo( an_ParentItemDbId IN typn_DbId,
							 an_ParentItemId IN typn_Id,
							 on_Return OUT typn_RetCode )
	IS
	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;

	   -- Determine the owner, the location, the installation and received
	   -- dates, the inventory condition and the root identifier.
	   SELECT loc_db_id,
			  loc_id,
			  owner_db_id,
			  owner_id,
			  install_dt,
			  install_gdt,
			  received_dt,
			  manufact_dt,
			  h_inv_no_db_id,
			  h_inv_no_id,
			  assmbl_inv_no_db_id,
			  assmbl_inv_no_id,
			  locked_bool,
			  prevent_synch_bool
		 INTO il_LocationDbId,
			  il_LocationId,
			  il_OwnerDbId,
			  il_OwnerId,
			  idt_InstallDt,
			  idt_InstallGDt,
			  idt_ReceivedDt,
			  idt_ManufactDt,
			  il_RootDbId,
			  il_RootId,
			  il_AssmblInvNoDbId,
			  il_AssmblInvNoId,
			  ib_LockedBool,
			  il_PreventSynchBool
		 FROM inv_inv
		WHERE inv_no_db_id = an_ParentItemDbId
		  AND inv_no_id    = an_ParentItemId
		  AND rstat_cd = 0;

	   -- Return success
	   on_Return := icn_Success;

	EXCEPTION
	   WHEN OTHERS THEN
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@SetGlobalInfo@@@' || SQLERRM);
		  RETURN;
	END SetGlobalInfo;

	/*----------------------- End of Package -----------------------------------*/
	END INV_CREATE_PKG;
/

--changeSet MX-17611:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
3.  Modify the UpdateSyncAllInvByTaskDefnRev procedure[qwerty]
*/
	CREATE OR REPLACE PROCEDURE UpdateSyncAllInvByTaskDefnRev(p_aTaskDbId IN NUMBER,
															  p_aTaskId   IN NUMBER) IS

	BEGIN

		MERGE INTO inv_sync_queue
		  USING
		  (
			 SELECT DISTINCT
				inv_inv.inv_no_db_id,
				inv_inv.inv_no_id
			 FROM
				inv_inv,
				task_task,
				task_part_map,
				eqp_assmbl_bom
			 WHERE
				task_task.task_db_id = p_aTaskDbId AND
				task_task.task_id    = p_aTaskId
				AND
				task_part_map.task_db_id (+)= task_task.task_db_id AND
				task_part_map.task_id    (+)= task_task.task_id
				AND
				(
				  -- get all the possible inventory for the task definition against the assembly
				  (
					eqp_assmbl_bom.assmbl_db_id  = inv_inv.assmbl_db_id AND
					eqp_assmbl_bom.assmbl_cd     = inv_inv.assmbl_cd AND
					eqp_assmbl_bom.assmbl_bom_id = inv_inv.assmbl_bom_id
					AND
					(
					   (
						  task_task.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
						  task_task.assmbl_cd = eqp_assmbl_bom.assmbl_cd AND
						  task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
					   )
					   OR
					   (
						  task_task.repl_assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
						  task_task.repl_assmbl_cd = eqp_assmbl_bom.assmbl_cd AND
						  task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
					   )
					)
				   )
				  -- get all the possible inventory for the task definition against the original assembly
				  OR
				  (
					(
					  eqp_assmbl_bom.assmbl_db_id  = inv_inv.orig_assmbl_db_id AND
					  eqp_assmbl_bom.assmbl_cd     = inv_inv.orig_assmbl_cd AND
					  eqp_assmbl_bom.assmbl_bom_id = 0
					  AND
					  (
						 (
							task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
						 OR
						 (
							task_task.repl_assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.repl_assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
					  )
					)
				  )
				  -- get all the possible inventory for the task definition against the task definition parts
				  OR
				  (
					 task_part_map.part_no_db_id = inv_inv.part_no_db_id AND
					 task_part_map.part_no_id    = inv_inv.part_no_id
				  )
				)
				AND
				inv_inv.rstat_cd = 0
			 ) inv_tasks
		  ON
		  (
			 inv_sync_queue.inv_no_db_id = inv_tasks.inv_no_db_id AND
			 inv_sync_queue.inv_no_id = inv_tasks.inv_no_id
		  )
		  WHEN NOT MATCHED THEN
			 INSERT(inv_no_db_id, inv_no_id, queue_date) VALUES (inv_tasks.inv_no_db_id, inv_tasks.inv_no_id, SYSDATE);

	END;
/

--changeSet MX-17611:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
4.  Modify the UpdateSyncInvByMaintPrgrm procedure [uiopas]
*/
	CREATE OR REPLACE PROCEDURE UpdateSyncInvByMaintPrgm(p_aMaintPrgmDbId IN NUMBER,
														 p_aMaintPrgmId   IN NUMBER)

	 IS
	BEGIN
		  MERGE INTO inv_sync_queue
		  USING
		  (
			 SELECT DISTINCT
				inv_inv.inv_no_db_id,
				inv_inv.inv_no_id
			 FROM
				(
				   SELECT
					  maint_prgm_task.task_db_id,
					  maint_prgm_task.task_id
				   FROM
					  maint_prgm,
					  maint_prgm_carrier_map,
					  maint_prgm_task
				   WHERE
					  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
					  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
					  AND
					  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
					  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
					  AND
					  maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
					  maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
					  AND
					  maint_prgm_task.unassign_bool = 0
					  AND NOT EXISTS
					  (
						 SELECT
							1
						 FROM
							maint_prgm_task prev_maint_prgm_task,
							maint_prgm prev_maint_prgm,
							maint_prgm_carrier_map prev_maint_prgm_carrier
						 WHERE
							prev_maint_prgm_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
							prev_maint_prgm_task.task_defn_id    = maint_prgm_task.task_defn_id
							AND
							prev_maint_prgm_task.unassign_bool = 0
							AND
							prev_maint_prgm.maint_prgm_db_id = prev_maint_prgm_task.maint_prgm_db_id AND
							prev_maint_prgm.maint_prgm_id    = prev_maint_prgm_task.maint_prgm_id
							AND
							prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
							prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
							AND
							prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
							prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
							AND
							prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
							prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
							AND
							prev_maint_prgm_carrier.latest_revision_bool = 1
					  )
				   UNION ALL
				   SELECT
					  prev_maint_prgm_task.task_db_id,
					  prev_maint_prgm_task.task_id
				   FROM
					  maint_prgm,
					  maint_prgm_carrier_map,
					  maint_prgm prev_maint_prgm,
					  maint_prgm_carrier_map prev_maint_prgm_carrier,
					  maint_prgm_task prev_maint_prgm_task
				   WHERE
					  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
					  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
					  AND
					  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
					  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
					  AND
					  prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
					  prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
					  AND
					  prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
					  prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
					  AND
					  prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
					  prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
					  AND
					  prev_maint_prgm_carrier.latest_revision_bool = 1
					  AND
					  prev_maint_prgm_task.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
					  prev_maint_prgm_task.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
					  AND
					  prev_maint_prgm_task.unassign_bool = 0
					  AND NOT EXISTS
					  (
						 SELECT
							1
						 FROM
							maint_prgm_task
						 WHERE
							maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
							maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
							AND
							maint_prgm_task.task_defn_db_id = prev_maint_prgm_task.task_defn_db_id AND
							maint_prgm_task.task_defn_id    = prev_maint_prgm_task.task_defn_id
							AND
							maint_prgm_task.unassign_bool = 0
					  )
				   UNION ALL
				   SELECT
					  maint_prgm_task.task_db_id,
					  maint_prgm_task.task_id
				   FROM
					  maint_prgm,
					  maint_prgm_carrier_map,
					  maint_prgm_task,
					  maint_prgm_task prev_maint_prgm_task,
					  maint_prgm prev_maint_prgm,
					  maint_prgm_carrier_map prev_maint_prgm_carrier
				   WHERE
					  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
					  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
					  AND
					  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
					  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
					  AND
					  maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
					  maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
					  AND
					  maint_prgm_task.unassign_bool = 0
					  AND
					  prev_maint_prgm_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
					  prev_maint_prgm_task.task_defn_id    = maint_prgm_task.task_defn_id
					  AND
					  prev_maint_prgm_task.unassign_bool = 0
					  AND
					  prev_maint_prgm.maint_prgm_db_id = prev_maint_prgm_task.maint_prgm_db_id AND
					  prev_maint_prgm.maint_prgm_id    = prev_maint_prgm_task.maint_prgm_id
					  AND
					  prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
					  prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
					  AND
					  prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
					  prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
					  AND
					  prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
					  prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
					  AND
					  prev_maint_prgm_carrier.latest_revision_bool = 1
					  AND NOT
					  (
						 maint_prgm_task.task_db_id = prev_maint_prgm_task.task_db_id AND
						 maint_prgm_task.task_id    = prev_maint_prgm_task.task_id
					  )
				) maint_prgm_task,
				maint_prgm_carrier_map,
				inv_inv,
				inv_inv assmbl_inv,
				eqp_assmbl_bom,
				task_task
			 WHERE
				maint_prgm_carrier_map.maint_prgm_db_id = p_aMaintPrgmDbId AND
				maint_prgm_carrier_map.maint_prgm_id    = p_aMaintPrgmId
				AND
				assmbl_inv.inv_no_db_id (+)= inv_inv.assmbl_inv_no_db_id AND
				assmbl_inv.inv_no_id    (+)= inv_inv.assmbl_inv_no_id
				AND
				assmbl_inv.rstat_cd(+) = 0
				AND
				maint_prgm_carrier_map.carrier_db_id =
				  CASE WHEN inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
					 THEN inv_inv.carrier_db_id
					 ELSE assmbl_inv.carrier_db_id
				  END
				AND
				maint_prgm_carrier_map.carrier_id =
				  CASE WHEN inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
					 THEN inv_inv.carrier_id
					 ELSE assmbl_inv.carrier_id
				  END
				AND
				(
				   (
					  eqp_assmbl_bom.assmbl_db_id  = inv_inv.assmbl_db_id AND
					  eqp_assmbl_bom.assmbl_cd     = inv_inv.assmbl_cd AND
					  eqp_assmbl_bom.assmbl_bom_id = inv_inv.assmbl_bom_id
					  AND
					  (
						 (
							task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
						 OR
						 (
							task_task.repl_assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.repl_assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
					  )
				   )
				   OR
				   (
					  eqp_assmbl_bom.assmbl_db_id  = inv_inv.orig_assmbl_db_id AND
					  eqp_assmbl_bom.assmbl_cd     = inv_inv.orig_assmbl_cd AND
					  eqp_assmbl_bom.assmbl_bom_id = 0
					  AND
					  (
						 (
							task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
						 OR
						 (
							task_task.repl_assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
							task_task.repl_assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
							task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
						 )
					  )
				   )
				)
				AND
				maint_prgm_task.task_db_id = task_task.task_db_id AND
				maint_prgm_task.task_id    = task_task.task_id
				AND
				inv_inv.rstat_cd = 0
	   ) maint_inv
	   ON
	   (
		  inv_sync_queue.inv_no_db_id = maint_inv.inv_no_db_id AND
		  inv_sync_queue.inv_no_id = maint_inv.inv_no_id
	   )
	   WHEN NOT MATCHED THEN
		  INSERT(inv_no_db_id, inv_no_id, queue_date) VALUES (maint_inv.inv_no_db_id, maint_inv.inv_no_id, SYSDATE);

	END;
/

--changeSet MX-17611:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
5.  Migrate the inv_inv table to have inventory requiring synchronization have an entry in the inv_sync_queue [dfghjk]
*/
DECLARE
   v_exists  NUMBER;
   v_sql     VARCHAR2(100);
BEGIN
   SELECT COUNT(*)
     INTO v_exists
     FROM user_tab_columns
    WHERE table_name = 'INV_INV'
      AND column_name = 'SYNCH_REQD_BOOL';
   
   IF v_exists > 0
   THEN
      v_sql := 'INSERT INTO ' || 
               '   INV_SYNC_QUEUE( ' || 
               '      inv_no_db_id,  ' || 
               '      inv_no_id,  ' || 
               '      queue_date)  ' || 
               '   SELECT  ' || 
               '      inv_no_db_id,  ' || 
               '      inv_no_id,  ' || 
               '      SYSDATE  ' || 
               '   FROM  ' || 
               '      inv_inv  ' || 
               '   WHERE  ' || 
               '      inv_inv.synch_reqd_bool = 1 ';
         
       EXECUTE IMMEDIATE(v_sql);

   END IF;
   
END;
/    

--changeSet MX-17611:39 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
6.  Remove the inv_inv.synch_reqd_bool [lzxcvb]
*/
BEGIN
	   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_INV', 'SYNCH_REQD_BOOL');
END;
/

--changeSet MX-17611:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
7.  Add the following package: utl_lock [nmqwer]
*/
	/*
		Package:       UtlLockPkg
		Description:   This package is used to manipulate the dbms_lock functions

		Author:        Chris Daley
		Created Date:  May 11th, 2010
	*/
	CREATE OR REPLACE PACKAGE Utl_Lock_Pkg IS

		/**
			Procedure:	RequestLock
		  
			Arguments:	aLockName   		The name for the lock
						aResult     		The lock handle
						 
			Description:	Determine the lock handle for the given lock name
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
		*/
		PROCEDURE RequestLockHandle(
			aLockName		IN		VARCHAR2,
			aResult			OUT		VARCHAR2
		);

		/**
		  Procedure:     RequestLock
		  
		  Arguments:     aLockHandle 		The unique lock handle
						 aLockMode   		The lock mode
						 aLockTimeout		The amount of time for the request to wait for timeout
						 aReleaseOnCommit	If the lock should release automagically when the transaction commits
						 aResult     		The result of the request
						 
		  Description:   Request a lock for the given lock handle with specified arguments
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE RequestLock(
		  aLockHandle   	IN    	VARCHAR2,
		  aLockMode   		IN    	NUMBER,
		  aLockTimeout		IN		NUMBER,
		  aReleaseOnCommit	IN		BOOLEAN,
		  aResult     		OUT   	NUMBER);
		  
	   /**
		  Procedure:     RequestLock
		  
		  Arguments:     aLockHandle The unique lock handle
						 aLockMode   The lock mode
						 aResult     The result of the request
						 
		  Description:   Request a lock for the given lock handle
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE RequestLock(
		  aLockHandle   IN    VARCHAR2,
		  aLockMode   	IN    NUMBER,
		  aResult     	OUT   NUMBER);
	   
	   /**
		  Procedure:     ReleaseLock
		  
		  Arguments:     aLockHandle    The lock handle
						 aResult        The result of the release
		  
		  Description:   Release the specified lock
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE ReleaseLock(
		  aLockHandle   IN  VARCHAR2,
		  aResult       OUT NUMBER );

	END Utl_Lock_Pkg;
/

--changeSet MX-17611:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
	/*
	   Package:       UtlLockPkg
	   Description:   This package is used to manipulate the dbms_lock functions

	   Author:        Chris Daley
	   Created Date:  May 11th, 2010
	*/
	CREATE OR REPLACE PACKAGE BODY Utl_Lock_Pkg IS

		/**
			Procedure:	RequestLock
		  
			Arguments:	aLockName   		The name for the lock
						aResult     		The lock handle
						 
			Description:	Determine the lock handle for the given lock name
		  
		  Author:  Cdaley
		  Create:  May 12th, 2010
		*/
		PROCEDURE RequestLockHandle(
			aLockName		IN		VARCHAR2,
			aResult			OUT		VARCHAR2
		)
		AS
		BEGIN
			dbms_lock.allocate_unique(aLockName,aResult);	  
		END RequestLockHandle;
		
	   /**
		  Procedure:     RequestLock
		  
		  Arguments:     aLockHandle 		The unique lock handle
						 aLockMode   		The lock mode
						 aLockTimeout		The amount of time for the request to wait for timeout
						 aReleaseOnCommit	If the lock should release automagically when the transaction commits
						 aResult     		The result of the request
						 
		  Description:   Request a lock for the given lock name in the specified mode
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE RequestLock(
		  aLockHandle   	IN    	VARCHAR2,
		  aLockMode   		IN    	NUMBER,
		  aLockTimeout		IN		NUMBER,
		  aReleaseOnCommit	IN		BOOLEAN,
		  aResult     		OUT   	NUMBER)
	   AS
	   BEGIN
		  aResult := dbms_lock.request(aLockHandle, aLockMode, aLockTimeout, aReleaseOnCommit);
	   END RequestLock;
	   
	   /**
		  Procedure:     RequestLock
		  
		  Arguments:     aLockHandle The unique lock handle
						 aLockMode   The lock mode
						 aResult     The result of the request
						 
		  Description:   Request a lock for the given lock handle
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE RequestLock(
		  aLockHandle   	IN    	VARCHAR2,
		  aLockMode   		IN    	NUMBER,
		  aResult     		OUT   	NUMBER)
	   AS
	   BEGIN
		  aResult := dbms_lock.request(aLockHandle, aLockMode, 0, FALSE);
		  
	   END RequestLock;
	  
	   
	   /**
		  Procedure:     ReleaseLock
		  
		  Arguments:     aLockHandle    The lock handle
						 aResult        The result of the release
		  
		  Description:   Release the specified lock
		  
		  Author:  Cdaley
		  Create:  May 11th, 2010
	   */
	   PROCEDURE ReleaseLock(
		  aLockHandle   IN  VARCHAR2,
		  aResult       OUT NUMBER)
	   AS
	   BEGIN
		  aResult := dbms_lock.release(aLockHandle);
		  
	   END ReleaseLock;

	END Utl_Lock_Pkg;
/

--changeSet MX-17611:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
	8.  Replace the baseline_sync_pkg [tyuiop]
*/
	/*
	   Package:       BaselineSyncPkg
	   Description:   This package is used to perform stored procedures related to baseline synchronizations

	   Author:        Chris Daley
	   Created Date:  Apr. 7th, 2009
	*/
	CREATE OR REPLACE PACKAGE "BASELINESYNCPKG" IS

	-- Basic error handling codes
		icn_Success CONSTANT NUMBER := 1;       -- Success
		icn_NoProc  CONSTANT NUMBER := 0;       -- No processing done
		icn_Error   CONSTANT NUMBER := -1;      -- Error
		
		
	/*
		Procedure:	createZipQueueWorkItem
		Arguments:	aZipId	NUMBER	the zip id to create the work item for

		Description:	This procedure creates a work item for the zip queue given.  This needs to be done since requesting zip can occur at the database layer.
		Author:	Chris Daley
		Created Date:	March 03, 2009
	*/
	PROCEDURE createZipQueueWorkItem(
		aZipId	IN	NUMBER,
		aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
		aInvNoId   IN inv_inv.inv_no_id%TYPE
	);
	/*
	   Procedure:     updateBlockZiplist

	   Arguments:     aTaskDbId   NUMBER   the block db id
					  aTaskId     NUMBER   the block id
					  aZipId      NUMBER   The zipqueue that the tasks are to be associated with
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
	   Author:        Chris Daley
	   Created Date:  Apr. 7th, 2009
	*/
	PROCEDURE updateBlockZiplist(
	   aZipId      IN    NUMBER,
	   aTaskDbId   IN    task_task.task_db_id%TYPE,
	   aTaskId     IN    task_task.task_id%TYPE,
	   aResult     OUT   NUMBER
	);

	/*
	   Procedure:     updateBlockZiplist

	   Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
					  aTaskDbId   NUMBER   the block definition revision that is being updated
					  aTaskId     NUMBER   --
					  aHInvNoDbId NUMBER   the highest inventory in the tree for the inventory that is being synchronized
					  aHInvNoId   NUMBER   --
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.  This will find blocks/reqs across all revisions.
	   Author:        Chris Daley
	   Created Date:  June. 18th, 2010
	*/
	PROCEDURE updateBlockZiplist(
	   aZipId      IN    NUMBER,
	   aTaskDbId   IN    task_task.task_db_id%TYPE,
	   aTaskId     IN    task_task.task_id%TYPE,
	   aHInvNoDbId IN    inv_inv.inv_no_db_id%TYPE,
	   aHInvNoId   IN    inv_inv.inv_no_id%TYPE,
	   aResult     OUT   NUMBER
	);

	/*
	   Procedure:     updateZipFullBlockOnTree

	   Arguments:     aBlockDbId  NUMBER   the block task db id
					  aBlockId       NUMBER   the block task id
					  aInvNoDbId     NUMBER   the inventory db id
					  aInvNoId NUMBER    the inventory id
					  aZipId   NUMBER   The zipqueue that the tasks are to be associated with
					  aResult        NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision and invenotry.
	   Author:        Yungjae Cho
	   Created Date:  Aug. 10th, 2009
	*/
	PROCEDURE updateZipFullBlockOnTree(
	   aZipId      IN    NUMBER,
	   aBlockDbId  IN    sched_stask.sched_db_id%TYPE,
	   aBlockId    IN    sched_stask.sched_id%TYPE,
	   aInvNoDbId  IN    inv_inv.inv_no_db_id%TYPE,
	   aInvNoId    IN    inv_inv.inv_no_id%TYPE,
	   aResult     OUT   NUMBER
	);

	/********************************************************************************
	*
	* Procedure:    RequestZipForNewReq
	* Arguments:
	*        an_InvNoDbId             The inventory on which the task is created
	*        an_InvNoId               "  "
	*        an_BlockDbId             The Task requesting Zipping
	*        an_BlockId               "  "
	*
	* Return:
	*        on_Return        (long) - Success/Failure of requesting Zip
	*
	* Description:  This procedure is used to queue a new requirement for
	*                 Baseline Sync zipping
	*
	* Orig.Coder:     Jonathan Clarkin
	* Recent Coder:   Jonathan Clarkin
	*
	*********************************************************************************
	*
	* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
	* Any distribution of the Mxi source code by any other party than
	* Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE RequestZipForNewReq(
		  an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
		  an_InvNoId            IN inv_inv.inv_no_id%TYPE,
		  an_ReqDbId            IN sched_stask.sched_db_id%TYPE,
		  an_ReqId              IN sched_stask.sched_id%TYPE,
		  on_Return             OUT NUMBER
	   );

	/********************************************************************************
	*
	* Procedure:    RequestZipForNewBlock
	* Arguments:
	*        an_InvNoDbId             The inventory on which the task is created
	*        an_InvNoId               "  "
	*        an_BlockDbId             The Task requesting Zipping
	*        an_BlockId               "  "
	*
	* Return:
	*        on_Return        (long) - Success/Failure of requesting Zip
	*
	* Description:  This procedure is used to queue a new block for Baseline Sync zipping
	*
	* Orig.Coder:     Jonathan Clarkin
	* Recent Coder:   Jonathan Clarkin
	*
	*********************************************************************************
	*
	* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
	* Any distribution of the Mxi source code by any other party than
	* Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE RequestZipForNewBlock(
		  an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
		  an_InvNoId            IN inv_inv.inv_no_id%TYPE,
		  an_BlockDbId          IN sched_stask.sched_db_id%TYPE,
		  an_BlockId            IN sched_stask.sched_id%TYPE,
		  on_Return             OUT NUMBER
	   );

	/*
	   Procedure:     removeDuplicateQueues

	   Arguments:     aZipId      NUMBER   The zipqueue that is used to search for pre-existing subsets
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure looks and removes previous Queues that are proper subsets of the passed in Queue
	   Author:        Jonathan Clarkin
	   Created Date:  ANovember 17th, 2009
	*/
	PROCEDURE removeDuplicateQueues(
	   aZipId      IN    NUMBER,
	   aResult     OUT   NUMBER
	);

	/*
	   Procedure:     UpdateActivateReq

	   Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
					  aReqDbId   NUMBER   the block db id
					  aReqId     NUMBER   the block id
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
					  This is moved from UpdateActivateReq.qrx
	   Author:        Bo Wang
	   Created Date:  Dec. 11, 2009
	*/
	PROCEDURE UpdateActivateReq(
	   aZipId      IN    NUMBER,
	   aReqDbId   IN    task_task.task_db_id%TYPE,
	   aReqId     IN    task_task.task_id%TYPE,
	   aResult     OUT   NUMBER
	);

	END BaselineSyncPkg;
/

--changeSet MX-17611:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
	CREATE OR REPLACE PACKAGE BODY "BASELINESYNCPKG" IS
	/*
	   Package:       BaselineSyncPkg
	   Description:   This package is used to perform stored procedures related to baseline synchronizations

	   Author:        Chris Daley
	   Created Date:  Apr. 7th, 2009
	*/

	/*
		Procedure:	createZipQueueWorkItem
		Arguments:	aZipId	NUMBER	the zip id to create the work item for

		Description:	This procedure creates a work item for the zip queue given.  This needs to be done since requesting zip can occur at the database layer.
		Author:	Chris Daley
		Created Date:	March 03, 2009
	*/
	PROCEDURE createZipQueueWorkItem(
		aZipId	   IN	NUMBER,
		aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
		aInvNoId   IN inv_inv.inv_no_id%TYPE
	)
	AS
		workId NUMBER;
		mimDb NUMBER;
	BEGIN
		SELECT
			db_id,
			utl_work_item_id.nextval
		INTO
			mimDb,
			workId
		FROM
			mim_local_db;

		INSERT INTO utl_work_item(id, type, key, data, utl_id) VALUES (workId, 'ZIP_QUEUE', aInvNoDbId||':'||aInvNoId, aZipId, mimDb);

	END createZipQueueWorkItem;

	/*
	   Procedure:     updateBlockZiplist

	   Arguments:     aTaskDbId   NUMBER   the block db id
					  aTaskId     NUMBER   the block id
					  aZipId      NUMBER   The zipqueue that the tasks are to be associated with
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
	   Author:        Chris Daley
	   Created Date:  Apr. 7th, 2009
	*/
	PROCEDURE updateBlockZiplist(
	   aZipId      IN    NUMBER,
	   aTaskDbId   IN    task_task.task_db_id%TYPE,
	   aTaskId     IN    task_task.task_id%TYPE,
	   aResult     OUT   NUMBER
	)
	AS
		  -- tables of predefined keys
		  TYPE zipId IS TABLE OF zip_task.zip_id%TYPE;
		  TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
		  TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
		  TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
		  TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
		  TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;

		  -- variables for the task definitions
		  lBlockList mxkeytable;
		  lPrevBlockDef mxkeytable;

		  -- variables for the ziplist
		  lZipId zipId;
		  lSchedDbId schedDbId;
		  lSchedId schedId;
		  lClassModeCd classModeCd;
		  lAssmblInvNoDbId assmblInvNoDbId;
		  lAssmblInvNoId assmblInvNoId;
	BEGIN
	   aResult := -1;

	   -- create a table to store the BLOCK objects
	   lBlockList := mxkeytable();

	   -- create a table to store the previous BLOCK Def objects
	   lPrevBlockDef := mxkeytable();

	   -- create tables for the ziplist()
	   lZipId := zipId();
	   lSchedDbId := schedDbId();
	   lSchedId := schedId();
	   lClassModeCd := classModeCd();
	   lAssmblInvNoDbId := assmblInvNoDbId();
	   lAssmblInvNoId := assmblInvNoId();

	   -- get the list of BLOCK task definition revisions
	   SELECT
		  mxkey(task_db_id, task_id)
	   BULK COLLECT INTO lBlockList
	   FROM
	   (
		  -- get the single blocks (no chain)
		  SELECT
			 task_task.task_db_id,
			 task_task.task_id
		  FROM
			 task_task
		  WHERE
			 task_task.task_db_id = aTaskDbId AND
			 task_task.task_id = aTaskId
			 AND
			 task_task.block_chain_sdesc IS NULL
		  UNION
		  -- get the block chains
		  SELECT
			 block_chain.task_db_id,
			 block_chain.task_id
		  FROM
			 task_task INNER JOIN task_task block_chain
				ON block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
				   block_chain.assmbl_db_id = task_task.assmbl_db_id AND
				   block_chain.assmbl_cd = task_task.assmbl_cd AND
				   block_chain.assmbl_bom_id = task_task.assmbl_bom_id
		  WHERE
			 task_task.task_db_id = aTaskDbId AND
			 task_task.task_id = aTaskId
	   );

	   -- get the previous block definition revision
	   SELECT
		  mxkey(task_defn_db_id, task_defn_id)
	   BULK COLLECT INTO lPrevBlockDef
	   FROM
	   (
		  SELECT DISTINCT
			 single_block.task_defn_db_id,
			 single_block.task_defn_id
		  FROM
			 task_task single_block
		  WHERE
			 single_block.task_db_id = aTaskDbId AND
			 single_block.task_id = aTaskId
			 AND
			 single_block.block_chain_sdesc IS NULL
		  UNION ALL
		  SELECT
			 chain_block.task_defn_db_id,
			 chain_block.task_defn_id
		  FROM
			 task_task single_block INNER JOIN task_task chain_block
				ON chain_block.block_chain_sdesc = single_block.block_chain_sdesc AND
				   chain_block.assmbl_db_id = single_block.assmbl_db_id AND
				   chain_block.assmbl_cd = single_block.assmbl_cd AND
				   chain_block.assmbl_bom_id = single_block.assmbl_bom_id AND
				   chain_block.revision_ord = single_block.revision_ord
		  WHERE
			 single_block.task_db_id = aTaskDbId AND
			 single_block.task_id = aTaskId
	   );

	   -- get all the actual BLOCKs
	   SELECT DISTINCT
		  aZipId,
		  sched_stask.sched_db_id,
		  sched_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  TABLE(lBlockList) blocks INNER JOIN task_task ON
			 blocks.db_id = task_task.task_db_id AND blocks.ID = task_task.task_id
		  INNER JOIN task_task prev_revisions ON
			 task_task.task_defn_db_id = prev_revisions.task_defn_db_id AND task_task.task_defn_id = prev_revisions.task_defn_id
		  INNER JOIN sched_stask
			 ON prev_revisions.task_db_id = sched_stask.task_db_id AND prev_revisions.task_id = sched_stask.task_id
		  INNER JOIN ref_task_class
			 ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN  evt_event
			 ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
		  INNER JOIN  evt_inv
			 ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
		  INNER JOIN  inv_inv
			 ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
		  LEFT OUTER JOIN inv_inv assmbl_inv
			 ON inv_inv.assmbl_inv_no_db_id = assmbl_inv.inv_no_db_id AND inv_inv.assmbl_inv_no_id = assmbl_inv.inv_no_id
	   WHERE
		  evt_event.hist_bool = 0
		  AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0
		  AND
		  inv_inv.locked_bool = 0;

	   -- insert the actual blocks
	   FORALL i in lZipId.first .. lZipId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   lZipId(i),
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   -- get the actual REQs created on previous revision
	   SELECT DISTINCT
		  aZipId,
		  req_stask.sched_db_id,
		  req_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  -- get the block defn
		  TABLE(lPrevBlockDef) blockDefs INNER JOIN task_task
			 ON blockDefs.db_id = task_task.task_defn_db_id AND blockDefs.id = task_task.task_defn_id
		  -- get the actual task block
		  INNER JOIN sched_stask
			 ON task_task.task_db_id = sched_stask.task_db_id AND task_task.task_id = sched_stask.task_id
		  -- get the actual req (sub_task) created for this particular block task
		  INNER JOIN evt_event req_evt
			 ON req_evt.nh_event_db_id = sched_stask.sched_db_id AND req_evt.nh_event_id = sched_stask.sched_id
		  INNER JOIN sched_stask req_stask
			 ON req_stask.sched_db_id = req_evt.event_db_id AND req_stask.sched_id = req_evt.event_id

		  INNER JOIN ref_task_class
			 ON req_stask.task_class_db_id = ref_task_class.task_class_db_id AND req_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN  evt_event
			 ON req_stask.sched_db_id = evt_event.event_db_id AND req_stask.sched_id = evt_event.event_id
		  INNER JOIN  evt_inv
			 ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
		  INNER JOIN  inv_inv
			 ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
	   WHERE
		  evt_event.hist_bool = 0
		  AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0
		  AND
		  inv_inv.locked_bool = 0;

	   -- insert the actual reqs
	   FORALL i in lZipId.first .. lZipId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   lZipId(i),
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   removeDuplicateQueues(aZipId, aResult);

	   -- set the result to 1 indicating successful operation
	   aResult := 1;

	-- if any exception occurs, return -1
	EXCEPTION
		  WHEN OTHERS THEN
			 aResult := -1;
			 APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
	END updateBlockZipList;

	PROCEDURE updateBlockZiplist(
	   aZipId      IN    NUMBER,
	   aTaskDbId   IN    task_task.task_db_id%TYPE,
	   aTaskId     IN    task_task.task_id%TYPE,
	   aHInvNoDbId  IN    inv_inv.inv_no_db_id%TYPE,
	   aHInvNoId    IN    inv_inv.inv_no_id%TYPE,
	   aResult     OUT   NUMBER
	)
	AS
		  -- tables of predefined keys
		  TYPE zipId IS TABLE OF zip_task.zip_id%TYPE;
		  TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
		  TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
		  TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
		  TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
		  TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;

		  -- variables for the task definitions
		  lBlockList mxkeytable;
		  lPrevBlockDef mxkeytable;

		  -- variables for the ziplist
		  lZipId zipId;
		  lSchedDbId schedDbId;
		  lSchedId schedId;
		  lClassModeCd classModeCd;
		  lAssmblInvNoDbId assmblInvNoDbId;
		  lAssmblInvNoId assmblInvNoId;
	BEGIN
	   aResult := -1;

	   -- create a table to store the BLOCK objects
	   lBlockList := mxkeytable();

	   -- create a table to store the previous BLOCK Def objects
	   lPrevBlockDef := mxkeytable();

	   -- create tables for the ziplist()
	   lZipId := zipId();
	   lSchedDbId := schedDbId();
	   lSchedId := schedId();
	   lClassModeCd := classModeCd();
	   lAssmblInvNoDbId := assmblInvNoDbId();
	   lAssmblInvNoId := assmblInvNoId();

	   -- get the list of BLOCK task definition revisions
	   SELECT
		  mxkey(task_db_id, task_id)
	   BULK COLLECT INTO lBlockList
	   FROM
	   (
		  -- get the single blocks (no chain)
		  SELECT
			 task_task.task_db_id,
			 task_task.task_id
		  FROM
			 task_task
		  WHERE
			 task_task.task_db_id = aTaskDbId AND
			 task_task.task_id = aTaskId
			 AND
			 task_task.block_chain_sdesc IS NULL
		  UNION
		  -- get the block chains
		  SELECT
			 block_chain.task_db_id,
			 block_chain.task_id
		  FROM
			 task_task INNER JOIN task_task block_chain
				ON block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
				   block_chain.assmbl_db_id = task_task.assmbl_db_id AND
				   block_chain.assmbl_cd = task_task.assmbl_cd AND
				   block_chain.assmbl_bom_id = task_task.assmbl_bom_id
		  WHERE
			 task_task.task_db_id = aTaskDbId AND
			 task_task.task_id = aTaskId
	   );

	   -- get the previous block definition revision
	   SELECT
		  mxkey(task_defn_db_id, task_defn_id)
	   BULK COLLECT INTO lPrevBlockDef
	   FROM
	   (
		  SELECT DISTINCT
			 single_block.task_defn_db_id,
			 single_block.task_defn_id
		  FROM
			 task_task single_block
		  WHERE
			 single_block.task_db_id = aTaskDbId AND
			 single_block.task_id = aTaskId
			 AND
			 single_block.block_chain_sdesc IS NULL
		  UNION ALL
		  SELECT
			 chain_block.task_defn_db_id,
			 chain_block.task_defn_id
		  FROM
			 task_task single_block INNER JOIN task_task chain_block
				ON chain_block.block_chain_sdesc = single_block.block_chain_sdesc AND
				   chain_block.assmbl_db_id = single_block.assmbl_db_id AND
				   chain_block.assmbl_cd = single_block.assmbl_cd AND
				   chain_block.assmbl_bom_id = single_block.assmbl_bom_id AND
				   chain_block.revision_ord = single_block.revision_ord
		  WHERE
			 single_block.task_db_id = aTaskDbId AND
			 single_block.task_id = aTaskId
	   );

	   -- get all the actual BLOCKs
	   SELECT DISTINCT
		  aZipId,
		  sched_stask.sched_db_id,
		  sched_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  TABLE(lBlockList) blocks INNER JOIN task_task ON
			 blocks.db_id = task_task.task_db_id AND blocks.ID = task_task.task_id
		  INNER JOIN task_task prev_revisions ON
			 task_task.task_defn_db_id = prev_revisions.task_defn_db_id AND task_task.task_defn_id = prev_revisions.task_defn_id
		  INNER JOIN sched_stask
			 ON prev_revisions.task_db_id = sched_stask.task_db_id AND prev_revisions.task_id = sched_stask.task_id
		  INNER JOIN ref_task_class
			 ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN  evt_event
			 ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
		  INNER JOIN  evt_inv
			 ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
		  INNER JOIN  inv_inv
			 ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
		  LEFT OUTER JOIN inv_inv assmbl_inv
			 ON inv_inv.assmbl_inv_no_db_id = assmbl_inv.inv_no_db_id AND inv_inv.assmbl_inv_no_id = assmbl_inv.inv_no_id
	   WHERE
		  evt_event.hist_bool = 0
		  AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0
		  AND
		  inv_inv.h_inv_no_db_id = aHInvNoDbId AND
		  inv_inv.h_inv_no_id = aHInvNoId
		  AND
		  inv_inv.locked_bool = 0;

	   -- insert the actual blocks
	   FORALL i in lZipId.first .. lZipId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   lZipId(i),
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   -- get the non-historic actual reqs created against all versions of the task definition
	   -- previous revisions must be included or they will not be unzipped properly
	   SELECT
		  aZipId,
		  sched_stask.sched_db_id,
		  sched_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  (
			 SELECT DISTINCT
				req_task_task.task_db_id,
				req_task_task.task_id
			 FROM
				TABLE(lPrevBlockDef) blockDefs INNER JOIN task_task ON 
				   blockDefs.db_id = task_task.task_defn_db_id AND 
				   blockDefs.id = task_task.task_defn_id
				INNER JOIN task_block_req_map ON
				   task_task.task_db_id = task_block_req_map.block_task_db_id AND
				   task_task.task_id = task_block_req_map.block_task_id
				INNER JOIN task_task req_task_task ON
				   req_task_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
				   req_task_task.task_defn_id = task_block_req_map.req_task_defn_id
		  ) req_task_task
		  INNER JOIN sched_stask ON
			 sched_stask.task_db_id = req_task_task.task_db_id AND
			 sched_stask.task_id = req_task_task.task_id
		  INNER JOIN ref_task_class ON
			 sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND
			 sched_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN evt_event ON
			 sched_stask.sched_db_id = evt_event.event_db_id AND
			 sched_stask.sched_id = evt_event.event_id
		  INNER JOIN evt_inv ON
			 evt_event.event_db_id = evt_inv.event_db_id AND
			 evt_event.event_id = evt_inv.event_id
		  INNER JOIN inv_inv ON
			 evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
			 evt_inv.inv_no_id = inv_inv.inv_no_id
	   WHERE
		  evt_event.hist_bool = 0
		  AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0
		  AND
		  inv_inv.h_inv_no_db_id = aHInvNoDbId AND
		  inv_inv.h_inv_no_id = aHInvNoId
		  AND
		  inv_inv.locked_bool = 0;

	   -- insert the actual reqs
	   FORALL i in lZipId.first .. lZipId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   lZipId(i),
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   removeDuplicateQueues(aZipId, aResult);

	   -- set the result to 1 indicating successful operation
	   aResult := 1;

	-- if any exception occurs, return -1
	EXCEPTION
		  WHEN OTHERS THEN
			 aResult := -1;
			 APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
	END updateBlockZipList;

	/*
	   Procedure:     updateZipFullBlockOnTree

	   Arguments:     aBlockDbId  NUMBER   the block task db id
					  aBlockId       NUMBER   the block task id
			aInvNoDbId   NUMBER   the inventory db id
			aInvNoId  NUMBER    the inventory id
					  aZipId         NUMBER   The zipqueue that the tasks are to be associated with
					  aResult        NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision and invenotry.
	   Author:        Yungjae Cho
	   Created Date:  Aug. 10th, 2009
	*/
	PROCEDURE updateZipFullBlockOnTree(
	   aZipId      IN    NUMBER,
	   aBlockDbId  IN    sched_stask.sched_db_id%TYPE,
	   aBlockId    IN    sched_stask.sched_id%TYPE,
	   aInvNoDbId  IN    inv_inv.inv_no_db_id%TYPE,
	   aInvNoId    IN    inv_inv.inv_no_id%TYPE,
	   aResult     OUT   NUMBER
	)
	AS
		  -- tables of predefined keys
		  TYPE zipId IS TABLE OF zip_task.zip_id%TYPE;
		  TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
		  TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
		  TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
		  TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
		  TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;

		  -- variables for the table keys
		  lBlockList mxkeytable;
		  lSchedTaskList mxkeytable;
		  lInventoryList mxkeytable;

		  -- variables for the ziplist
		  lZipId zipId;
		  lSchedDbId schedDbId;
		  lSchedId schedId;
		  lClassModeCd classModeCd;
		  lAssmblInvNoDbId assmblInvNoDbId;
		  lAssmblInvNoId assmblInvNoId;
	BEGIN
	   aResult := -1;

	   -- create a table to store the BLOCK objects
	   lBlockList := mxkeytable();

	   -- create a table to store the actual task objects
	   lSchedTaskList := mxkeytable();

	   -- create a table to store the assembly inventory tree
	   lInventoryList := mxkeytable();

	   -- create tables for the ziplist()
	   lZipId := zipId();
	   lSchedDbId := schedDbId();
	   lSchedId := schedId();
	   lClassModeCd := classModeCd();
	   lAssmblInvNoDbId := assmblInvNoDbId();
	   lAssmblInvNoId := assmblInvNoId();

	   -- get the list of BLOCK task definition revisions
	   SELECT
		  mxkey(task_db_id, task_id)
	   BULK COLLECT INTO lBlockList
	   FROM
	   (
		  -- get the single blocks (no chain)
		  SELECT
			 task_task.task_db_id,
			 task_task.task_id
		  FROM
		sched_stask block_stask INNER JOIN task_task
		   ON    task_task.task_db_id = block_stask.task_db_id AND
			   task_task.task_id    = block_stask.task_id
		  WHERE
			 block_stask.sched_db_id = aBlockDbId AND
			 block_stask.sched_id    = aBlockId
			 AND
			 task_task.block_chain_sdesc IS NULL
		  UNION
		  -- get the block chains
		  SELECT
			 block_chain.task_db_id,
			 block_chain.task_id
		  FROM
		sched_stask block_stask INNER JOIN task_task
		   ON    task_task.task_db_id = block_stask.task_db_id AND
			   task_task.task_id    = block_stask.task_id
		INNER JOIN task_task block_chain
		   ON    block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
			   block_chain.assmbl_db_id = task_task.assmbl_db_id AND
			   block_chain.assmbl_cd = task_task.assmbl_cd AND
			   block_chain.assmbl_bom_id = task_task.assmbl_bom_id
		  WHERE
			 block_stask.sched_db_id = aBlockDbId AND
			 block_stask.sched_id    = aBlockId
	   );

		-- Get the Baseline link to all Actual REQs and BLOCKS
	   SELECT
		  mxkey(sched_db_id, sched_id)
	   BULK COLLECT INTO lSchedTaskList
	   FROM
	   (
		  -- Get actual REQs
		  SELECT /*+ cardinality (blocks 10) */
			 sched_stask.sched_db_id,
		sched_stask.sched_id
		  FROM
			 TABLE(lBlockList) blocks INNER JOIN task_block_req_map
		   ON   task_block_req_map.block_task_db_id = blocks.db_Id AND
			  task_block_req_map.block_task_id    = blocks.id
		INNER JOIN task_task req_task
		   ON   req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
				req_task.task_defn_id    = task_block_req_map.req_task_defn_id
		INNER JOIN sched_stask
		   ON   sched_stask.task_db_id = req_task.task_db_id AND
				sched_stask.task_id  = req_task.task_id
		  UNION
		  -- Get actual BLOCKS
		  SELECT /*+ cardinality (blocks 10) */
			 sched_stask.sched_db_id,
		sched_stask.sched_id
		  FROM
			 TABLE(lBlockList) blocks INNER JOIN sched_stask
		   ON   sched_stask.task_db_id = blocks.db_id AND
			  sched_stask.task_id    = blocks.id
	   );

		  -- Lookup of the Assembly inv tree based on having the root assembly key
		  -- Include the Root Assembly and any inv that belong to that root Assembly
		  SELECT
			 mxkey(inv_no_db_id, inv_no_id)
		BULK COLLECT INTO lInventoryList
		  FROM
		  (
			 SELECT
				inv_inv.inv_no_db_id,
				inv_inv.inv_no_id
			 FROM inv_inv
			 WHERE
				-- Get the base inventory
				inv_inv.inv_no_db_id = aInvNoDbId AND
				inv_inv.inv_no_id    = aInvNoId
				AND
				-- Limit to ASSY or ACFT cases
				( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
			 UNION ALL
			 SELECT
				assmbl_inv.inv_no_db_id,
				assmbl_inv.inv_no_id
			 FROM
				inv_inv assmbl_inv INNER JOIN inv_inv
				   ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.inv_no_db_id AND
					   assmbl_inv.assmbl_inv_no_id    = inv_inv.inv_no_id
			 WHERE
				-- Get the base inventory
				inv_inv.inv_no_db_id = aInvNoDbId AND
				inv_inv.inv_no_id    = aInvNoId
				AND
				-- Limit to ASSY or ACFT cases
				( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
			 UNION ALL
			 -- Lookup of the Assembly inv tree based on not having the root assembly key
			 -- Include all those sharing the Root Assembly and the Root Assembly inv as well
			 SELECT
				assmbl_inv.inv_no_db_id,
				assmbl_inv.inv_no_id
			 FROM
				inv_inv assmbl_inv INNER JOIN inv_inv
				   ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
						assmbl_inv.assmbl_inv_no_id    = inv_inv.assmbl_inv_no_id
			 WHERE
				-- Get the base inventory
				inv_inv.inv_no_db_id = aInvNoDbId AND
				inv_inv.inv_no_id    = aInvNoId
				AND
				-- Limit to sub node cases
				( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
			 UNION ALL
			 SELECT
				inv_inv.assmbl_inv_no_db_id,
				inv_inv.assmbl_inv_no_id
			 FROM
				inv_inv assmbl_inv INNER JOIN inv_inv
				   ON   assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
						  assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
			 WHERE
				-- Get the base inventory
				inv_inv.inv_no_db_id = aInvNoDbId AND
				inv_inv.inv_no_id    = aInvNoId
				AND
				-- Limit to sub node cases
				( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
	   );


	   -- get all the actual tasks
	   SELECT DISTINCT
		  aZipId,
		  sched_stask.sched_db_id,
		  sched_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  TABLE(lSchedTaskList) tasks INNER JOIN sched_stask
			 ON tasks.db_id = sched_stask.sched_db_id AND tasks.id = sched_stask.sched_id
		  INNER JOIN ref_task_class
			 ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN  evt_event
			 ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
		  INNER JOIN  evt_inv
			 ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
		  INNER JOIN inv_inv
			 ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
		  INNER JOIN  TABLE(lInventoryList) inventory
			 ON inv_inv.inv_no_db_id = inventory.db_id AND inv_inv.inv_no_id = inventory.id
	   WHERE
		  evt_event.hist_bool = 0 AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0 AND
		  inv_inv.locked_bool = 0;

	   -- insert the actual tasks
	   FORALL i in lZipId.first .. lZipId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   lZipId(i),
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   removeDuplicateQueues(aZipId, aResult);


	   -- set the result to 1 indicating successful operation
	   aResult := 1;

	-- if any exception occurs, return -1
	EXCEPTION
		  WHEN OTHERS THEN
			 aResult := -1;
			 APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
	END updateZipFullBlockOnTree;

	/********************************************************************************
	*
	* Procedure:    RequestZipForNewReq
	* Arguments:
	*        an_InvNoDbId             The inventory on which the task is created
	*        an_InvNoId               "  "
	*        an_BlockDbId             The Task requesting Zipping
	*        an_BlockId               "  "
	*
	* Return:
	*        on_Return        (long) - Success/Failure of requesting Zip
	*
	* Description:  This procedure is used to queue a new requirement for
	*                 Baseline Sync zipping
	*
	* Orig.Coder:     Jonathan Clarkin
	* Recent Coder:   Jonathan Clarkin
	*
	*********************************************************************************
	*
	* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
	* Any distribution of the Mxi source code by any other party than
	* Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE RequestZipForNewReq(
		  an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
		  an_InvNoId            IN inv_inv.inv_no_id%TYPE,
		  an_ReqDbId            IN sched_stask.sched_db_id%TYPE,
		  an_ReqId              IN sched_stask.sched_id%TYPE,
		  on_Return             OUT NUMBER
	   ) IS

	   /* *** DECLARE LOCAL VARIABLES *** */
	   ln_ZipId          zip_queue.zip_id%TYPE;
	   ln_IdGenerated    NUMBER;
	   ln_InvNoDbId      inv_inv.inv_no_db_id%TYPE;
	   ln_InvNoId        inv_inv.inv_no_id%TYPE;

	   /* *** DECLARE CURSORS *** */
	   CURSOR lcur_ZipList (
		  an_InvNoDbId inv_inv.inv_no_db_id%TYPE,
		  an_InvNoId   inv_inv.inv_no_id%TYPE,
		  an_ReqDbId sched_stask.sched_db_id%TYPE,
		  an_ReqId   sched_stask.sched_id%TYPE
	   ) IS
		  SELECT DISTINCT
			 sched_stask.sched_db_id,
			 sched_stask.sched_id,
			 ref_task_class.class_mode_cd,
			 inv_inv.assmbl_inv_db_id,
			 inv_inv.assmbl_inv_id
		  FROM
			 sched_stask req_stask,
			 task_block_req_map,
			 task_task req_task,
			 task_task gen_task,
			 sched_stask,
			 evt_event,
			 evt_inv,
			 (
				-- Get the assembly tree for the current assembly only
				SELECT
				   assmbl_inv.inv_no_db_id,
				   assmbl_inv.inv_no_id,
				   -- This column should always be the current assembly
				   -- If inv_inv is an assembly, always use the current inv pk because the
				   -- assmbly pk will be that of the parent assembly
				   -- Otherwise, always use the assembly pk
				   nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id ) AS assmbl_inv_db_id,
				   nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id ) AS assmbl_inv_id
				FROM
				   inv_inv,
				   inv_inv assmbl_inv
				WHERE
				   inv_inv.inv_no_db_id = an_InvNoDbId AND
				   inv_inv.inv_no_id    = an_InvNoId
				   AND
				   (
					  (  -- Get all inventory on the current assembly, if this is an assembly, connect using inv pk
						 assmbl_inv.assmbl_inv_no_db_id = nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id ) AND
						 assmbl_inv.assmbl_inv_no_id    = nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id )
					  )
					  OR
					  (  -- Get the current inv as well in cases where the argument is the pk of an assembly
						 assmbl_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
						 assmbl_inv.inv_no_id    = inv_inv.inv_no_id
					  )
					  OR
					  (  -- If this is a sub inv on an ASSY, we have to also get the main assmbl inv
						 -- We only want to do this if this is not an assembly, otherwise we will end up
						 -- getting the assembly above this one
						 inv_inv.orig_assmbl_db_id IS NULL AND
						 assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
						 assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
					  )
				   )
				   AND
				   -- Exclude inventory that is flagged as Prevent Sync or Locked
				   assmbl_inv.prevent_synch_bool = 0
				   AND
				   assmbl_inv.locked_bool = 0
			 ) inv_inv,
			 task_task,
			 ref_task_class
		  WHERE
			 -- Get the Actual Requirement
			 req_stask.sched_db_id = an_ReqDbId AND
			 req_stask.sched_id    = an_ReqId
			 AND
			 -- Get the Baseline link to all Blocks
			 req_task.task_db_id = req_stask.task_db_id AND
			 req_task.task_id    = req_stask.task_id
			 AND
			 task_block_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
			 task_block_req_map.req_task_defn_id    = req_task.task_defn_id
			 AND
			 -- Link to all revisions of the REQs and BLOCKS
			 (  (  gen_task.task_db_id = req_task.task_db_id AND
				   gen_task.task_id    = req_task.task_id
				)
				OR
				(  gen_task.task_db_id = task_block_req_map.block_task_db_id AND
				   gen_task.task_id    = task_block_req_map.block_task_id
				)
			 )
			 AND
			 task_task.task_defn_db_id = gen_task.task_defn_db_id AND
			 task_task.task_defn_id    = gen_task.task_defn_id
			 AND
			 -- Link to all Actual REQs and BLOCKS
			 sched_stask.task_db_id = task_task.task_db_id AND
			 sched_stask.task_id    = task_task.task_id
			 AND
			 -- Limit to those on the same assembly inv tree
			 evt_event.event_db_id = sched_stask.sched_db_id AND
			 evt_event.event_id    = sched_stask.sched_id
			 AND
			 evt_event.hist_bool = 0
			 AND
			 evt_inv.event_db_id   = evt_event.event_db_id AND
			 evt_inv.event_id      = evt_event.event_id    AND
			 evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id  AND
			 evt_inv.inv_no_id     = inv_inv.inv_no_id     AND
			 evt_inv.main_inv_bool = 1
			 AND
			 -- Get the Task Class
			 ref_task_class.task_class_db_id = task_task.task_class_db_id AND
			 ref_task_class.task_class_cd    = task_task.task_class_cd;
	   lrec_ZipList  lcur_ZipList%ROWTYPE;

	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;
	   ln_IdGenerated := 0;

	   -- Add all the Zip Tasks
	   FOR lrec_ZipList IN lcur_ZipList( an_InvNoDbId, an_InvNoId, an_ReqDbId, an_ReqId )
	   LOOP

		  IF( ln_IdGenerated = 0 ) THEN
			 -- get the next zip id
			 SELECT ZIP_QUEUE_SEQ.nextval INTO ln_ZipId FROM dual;

			 -- Insert the new Zip ID
			 INSERT INTO zip_queue ( zip_id, actv_bool )
			 VALUES ( ln_ZipId, 0 );

			 ln_IdGenerated := 1;

		  END IF;

		  INSERT INTO zip_task (
			 zip_id,
			 sched_db_id,
			 sched_id,
			 class_mode_cd,
			 assmbl_inv_no_db_id,
			 assmbl_inv_no_id
		  )
		  VALUES (
			 ln_ZipId,
			 lrec_ZipList.sched_db_id,
			 lrec_ZipList.sched_id,
			 lrec_ZipList.class_mode_cd,
			 lrec_ZipList.assmbl_inv_db_id,
			 lrec_ZipList.assmbl_inv_id
		  );
	   END LOOP;

	   -- Activate the Zip ID
	   IF( ln_IdGenerated = 1 ) THEN
		  UPDATE zip_queue
		  SET    actv_bool = 1
		  WHERE  zip_id = ln_ZipId;

		 SELECT inv_inv.h_inv_no_db_id,
				inv_inv.h_inv_no_id
		 INTO   ln_InvNoDbId,
				ln_InvNoId
		 FROM   inv_inv
		 WHERE  inv_inv.inv_no_db_id = an_InvNoDbId AND
				inv_inv.inv_no_id    = an_InvNoId;

		   createZipQueueWorkItem(ln_ZipId, ln_InvNoDbId, ln_InvNoId);
	   END IF;

	   on_Return    := icn_Success;

	EXCEPTION
	   WHEN OTHERS THEN
		  -- Unexpected error
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@RequestZipForNewReq@@@'||SQLERRM);
		  RETURN;
	END RequestZipForNewReq;

	/********************************************************************************
	*
	* Procedure:    RequestZipForNewBlock
	* Arguments:
	*        an_InvNoDbId             The inventory on which the task is created
	*        an_InvNoId               "  "
	*        an_BlockDbId             The Task requesting Zipping
	*        an_BlockId               "  "
	*
	* Return:
	*        on_Return        (long) - Success/Failure of requesting Zip
	*
	* Description:  This procedure is used to queue a new block for Baseline Sync zipping
	*
	* Orig.Coder:     Jonathan Clarkin
	* Recent Coder:   Jonathan Clarkin
	*
	*********************************************************************************
	*
	* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
	* Any distribution of the Mxi source code by any other party than
	* Mxi Technologies Ltd is prohibited.
	*
	*********************************************************************************/
	PROCEDURE RequestZipForNewBlock(
		  an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
		  an_InvNoId            IN inv_inv.inv_no_id%TYPE,
		  an_BlockDbId          IN sched_stask.sched_db_id%TYPE,
		  an_BlockId            IN sched_stask.sched_id%TYPE,
		  on_Return             OUT NUMBER
	   ) IS

	   /* *** DECLARE LOCAL VARIABLES *** */
	   ln_ZipId          zip_queue.zip_id%TYPE;
	   ln_IdGenerated    NUMBER;
	   ln_InvNoDbId      inv_inv.inv_no_db_id%TYPE;
	   ln_InvNoId        inv_inv.inv_no_id%TYPE;


		-- tables of predefined keys
		TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
		TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
		TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
		TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
		TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;

		-- variables for the table keys
		lBlockList mxkeytable;
		lSchedTaskList mxkeytable;
		lInventoryList mxkeytable;

		-- variables for the ziplist
		lSchedDbId schedDbId;
		lSchedId schedId;
		lClassModeCd classModeCd;
		lAssmblInvNoDbId assmblInvNoDbId;
		lAssmblInvNoId assmblInvNoId;

	BEGIN

	   -- Initialize the return value
	   on_Return := icn_NoProc;
	   ln_IdGenerated := 0;

	   -- create a table to store the BLOCK objects
	   lBlockList := mxkeytable();

	   -- create a table to store the actual task objects
	   lSchedTaskList := mxkeytable();

	   -- create a table to store the assembly inventory tree
	   lInventoryList := mxkeytable();

	   -- create tables for the ziplist()
	   lSchedDbId := schedDbId();
	   lSchedId := schedId();
	   lClassModeCd := classModeCd();
	   lAssmblInvNoDbId := assmblInvNoDbId();
	   lAssmblInvNoId := assmblInvNoId();

	   -- get the list of BLOCK task definition revisions
	   SELECT
		  mxkey(task_db_id, task_id)
	   BULK COLLECT INTO lBlockList
	   FROM
	   (
		  -- get the single blocks (no chain)
		  SELECT
			 task_task.task_db_id,
			 task_task.task_id
		  FROM
		 sched_stask block_stask INNER JOIN task_task
			ON    task_task.task_db_id = block_stask.task_db_id AND
				  task_task.task_id    = block_stask.task_id
		  WHERE
			 block_stask.sched_db_id = an_BlockDbId AND
			 block_stask.sched_id    = an_BlockId
			 AND
			 task_task.block_chain_sdesc IS NULL
		  UNION
		  -- get the block chains
		  SELECT
			 block_chain.task_db_id,
			 block_chain.task_id
		  FROM
		 sched_stask block_stask INNER JOIN task_task
			ON    task_task.task_db_id = block_stask.task_db_id AND
				  task_task.task_id    = block_stask.task_id
		 INNER JOIN task_task block_chain
			ON    block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
				  block_chain.revision_ord = task_task.revision_ord AND
				  block_chain.assmbl_db_id = task_task.assmbl_db_id AND
				  block_chain.assmbl_cd = task_task.assmbl_cd AND
				  block_chain.assmbl_bom_id = task_task.assmbl_bom_id
		  WHERE
			 block_stask.sched_db_id = an_BlockDbId AND
			 block_stask.sched_id    = an_BlockId
	   );

		 -- Get the Baseline link to all Actual REQs and BLOCKS
	   SELECT
		  mxkey(sched_db_id, sched_id)
	   BULK COLLECT INTO lSchedTaskList
	   FROM
	   (
		  -- Get actual REQs
		  SELECT
			 sched_stask.sched_db_id,
			   sched_stask.sched_id
		  FROM
			 TABLE(lBlockList) blocks INNER JOIN task_block_req_map
				ON   task_block_req_map.block_task_db_id = blocks.db_Id AND
					 task_block_req_map.block_task_id    = blocks.id
			 INNER JOIN task_task req_task
				ON   req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
					 req_task.task_defn_id    = task_block_req_map.req_task_defn_id
			 INNER JOIN sched_stask
				ON   sched_stask.task_db_id = req_task.task_db_id AND
					 sched_stask.task_id	= req_task.task_id
		  UNION
		  -- Get actual BLOCKS
		  SELECT
			 sched_stask.sched_db_id,
			   sched_stask.sched_id
		  FROM
			 TABLE(lBlockList) blocks INNER JOIN sched_stask
				ON   sched_stask.task_db_id = blocks.db_id AND
					 sched_stask.task_id 	= blocks.id
	   );

		-- Lookup of the Assembly inv tree based on having the root assembly key
		-- Include the Root Assembly and any inv that belong to that root Assembly
		SELECT
		   mxkey(inv_no_db_id, inv_no_id)
		   BULK COLLECT INTO lInventoryList
		FROM
		(
		   SELECT
			  inv_inv.inv_no_db_id,
			  inv_inv.inv_no_id
		   FROM inv_inv
		   WHERE
			  -- Get the base inventory
			  inv_inv.inv_no_db_id = an_InvNoDbId AND
			  inv_inv.inv_no_id    = an_InvNoId
			  AND
			  -- Limit to ASSY or ACFT cases
			  ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
		   UNION ALL
		   SELECT
			  assmbl_inv.inv_no_db_id,
			  assmbl_inv.inv_no_id
		   FROM
			  inv_inv assmbl_inv INNER JOIN inv_inv
				 ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.inv_no_db_id AND
					  assmbl_inv.assmbl_inv_no_id    = inv_inv.inv_no_id
		   WHERE
			  -- Get the base inventory
			  inv_inv.inv_no_db_id = an_InvNoDbId AND
			  inv_inv.inv_no_id    = an_InvNoId
			  AND
			  -- Limit to ASSY or ACFT cases
			  ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
		   UNION ALL
		   -- Lookup of the Assembly inv tree based on not having the root assembly key
		   -- Include all those sharing the Root Assembly and the Root Assembly inv as well
		   SELECT
			  assmbl_inv.inv_no_db_id,
			  assmbl_inv.inv_no_id
		   FROM
			  inv_inv assmbl_inv INNER JOIN inv_inv
				 ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
					  assmbl_inv.assmbl_inv_no_id    = inv_inv.assmbl_inv_no_id
		   WHERE
			  -- Get the base inventory
			  inv_inv.inv_no_db_id = an_InvNoDbId AND
			  inv_inv.inv_no_id    = an_InvNoId
			  AND
			  -- Limit to sub node cases
			  ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
		   UNION ALL
		   SELECT
			  inv_inv.assmbl_inv_no_db_id,
			  inv_inv.assmbl_inv_no_id
		   FROM
			  inv_inv assmbl_inv INNER JOIN inv_inv
				 ON   assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
					  assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
		   WHERE
			  -- Get the base inventory
			  inv_inv.inv_no_db_id = an_InvNoDbId AND
			  inv_inv.inv_no_id    = an_InvNoId
			  AND
			  -- Limit to sub node cases
			  ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
	   );


	   -- get all the actual tasks
	   SELECT DISTINCT
		  sched_stask.sched_db_id,
		  sched_stask.sched_id,
		  ref_task_class.class_mode_cd,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
			 ELSE inv_inv.assmbl_inv_no_db_id
		  END AS assmbl_inv_db_id,
		  CASE
			 WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
			 WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
			 ELSE inv_inv.assmbl_inv_no_id
		  END AS assmbl_inv_id
	   BULK COLLECT INTO lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
	   FROM
		  TABLE(lSchedTaskList) tasks INNER JOIN sched_stask
			 ON tasks.db_id = sched_stask.sched_db_id AND tasks.id = sched_stask.sched_id
		  INNER JOIN ref_task_class
			 ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
		  INNER JOIN  evt_event
			 ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
		  INNER JOIN  evt_inv
			 ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
		  INNER JOIN inv_inv
			 ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
		  INNER JOIN  TABLE(lInventoryList) inventory
			 ON inv_inv.inv_no_db_id = inventory.db_id AND inv_inv.inv_no_id = inventory.id
	   WHERE
		  evt_event.hist_bool = 0 AND
		  evt_inv.main_inv_bool = 1
		  AND
		  inv_inv.prevent_synch_bool = 0 AND
		  inv_inv.locked_bool = 0;

		IF( ln_IdGenerated = 0 ) THEN
		   -- get the next zip id
		   SELECT ZIP_QUEUE_SEQ.nextval INTO ln_ZipId FROM dual;

		   -- Insert the new Zip ID
		   INSERT INTO zip_queue ( zip_id, actv_bool )
		   VALUES ( ln_ZipId, 0 );

		   ln_IdGenerated := 1;

		END IF;

	   -- insert the actual tasks
	   FORALL i in lSchedDbId.first .. lSchedDbId.last
		  INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
		  VALUES (   ln_ZipId,
				   lSchedDbId(i),
				   lSchedId(i),
				   lClassModeCd(i),
				   lAssmblInvNoDbId(i),
				   lAssmblInvNoId(i)
			   );

	   -- Activate the Zip ID
	   IF( ln_IdGenerated = 1 ) THEN
		  UPDATE zip_queue
		  SET    actv_bool = 1
		  WHERE  zip_id = ln_ZipId;

		 SELECT inv_inv.h_inv_no_db_id,
				inv_inv.h_inv_no_id
		 INTO   ln_InvNoDbId,
				ln_InvNoId
		 FROM   inv_inv
		 WHERE  inv_inv.inv_no_db_id = an_InvNoDbId AND
				inv_inv.inv_no_id    = an_InvNoId;

		 createZipQueueWorkItem(ln_ZipId, ln_InvNoDbId, ln_InvNoId);
	   END IF;

	   on_Return    := icn_Success;

	EXCEPTION
	   WHEN OTHERS THEN
		  -- Unexpected error
		  on_Return := icn_Error;
		  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@RequestZipForNewBlock@@@'||SQLERRM);
		  RETURN;
	END RequestZipForNewBlock;


	/*
	   Procedure:     removeDuplicateQueues

	   Arguments:     aZipId      NUMBER   The zipqueue that is used to search for pre-existing subsets
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure looks and removes previous Queues that are proper subsets of the passed in Queue
	   Author:        Jonathan Clarkin
	   Created Date:  ANovember 17th, 2009
	*/
	PROCEDURE removeDuplicateQueues(
	   aZipId      IN    NUMBER,
	   aResult     OUT   NUMBER
	)
	AS
	   CURSOR lcur_AllZipId( an_ZipId NUMBER )
	   IS
		  SELECT zip_id
		  FROM zip_queue
		  WHERE
			 zip_id <> an_ZipId
			 AND
			 NOT EXISTS (
				SELECT sched_db_id,sched_id
				FROM zip_task
				WHERE zip_task.zip_id = zip_queue.zip_id
			   MINUS
				SELECT sched_db_id,sched_id
				FROM zip_task
				WHERE zip_task.zip_id = an_ZipId
			 );
	   lrec_AllZipId  lcur_AllZipId%ROWTYPE;


	BEGIN
	   aResult := -1;

	   -- Delete any existing Queues that are proper-subsets of this Queue
	   FOR lrec_AllZipId IN lcur_AllZipId(aZipId) LOOP
		  DELETE
		  FROM zip_task
		  WHERE zip_id = lrec_AllZipId.zip_id;
		  DELETE
		  FROM zip_queue
		  WHERE zip_id = lrec_AllZipId.zip_id;
	   END LOOP;

	   -- set the result to 1 indicating successful operation
	   aResult := 1;

	-- if any exception occurs, return -1
	EXCEPTION
		  WHEN OTHERS THEN
			 aResult := -1;
			 APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
	END removeDuplicateQueues;


	/*
	   Procedure:     UpdateActivateReq

	   Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
					  aReqDbId   NUMBER   the block db id
					  aReqId     NUMBER   the block id
					  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

	   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
					  This is moved from UpdateActivateReq.qrx - Adds all Reqs in Maintenix to a Zip List for Baseline Sync
	   Author:        Bo Wang
	   Created Date:  Dec. 11, 2009
	*/
	PROCEDURE UpdateActivateReq(
	   aZipId      IN    NUMBER,
	   aReqDbId   IN    task_task.task_db_id%TYPE,
	   aReqId     IN    task_task.task_id%TYPE,
	   aResult     OUT   NUMBER
	) IS

	BEGIN
	   aResult := -1;
	   INSERT INTO ZIP_TASK (
			 zip_id,
			 sched_db_id,
			 sched_id,
			 class_mode_cd,
			 assmbl_inv_no_db_id,
			 assmbl_inv_no_id
		  )
		  (
			 SELECT DISTINCT
				aZipId,
				sched_stask.sched_db_id,
				sched_stask.sched_id,
				ref_task_class.class_mode_cd,
				CASE
				   WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
				   WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
				   ELSE inv_inv.assmbl_inv_no_db_id
				END AS assmbl_inv_db_id,
				CASE
				   WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
				   WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
				   ELSE inv_inv.assmbl_inv_no_id
				END AS assmbl_inv_id
			 FROM
				task_task req_task,
				task_block_req_map,
				task_task gen_task,
				sched_stask,
				evt_event,
				evt_inv,
				inv_inv,
				inv_inv assmbl_inv,
				task_task,
				ref_task_class
			 WHERE
				-- Get the Baselined Requirement
				req_task.task_db_id = aReqDbId AND
				req_task.task_id    = aReqId
				AND
				-- Get all the parent Block Definitions
				task_block_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
				task_block_req_map.req_task_defn_id    = req_task.task_defn_id
				AND
				-- Link to all Actual REQs and BLOCKS
				(  (  gen_task.task_db_id = req_task.task_db_id AND
					  gen_task.task_id    = req_task.task_id
				   )
				   OR
				   (  gen_task.task_db_id = task_block_req_map.block_task_db_id AND
					  gen_task.task_id    = task_block_req_map.block_task_id
				   )
				)
				AND
				task_task.task_defn_db_id = gen_task.task_defn_db_id AND
				task_task.task_defn_id    = gen_task.task_defn_id
				AND
				sched_stask.task_db_id = task_task.task_db_id AND
				sched_stask.task_id    = task_task.task_id
				AND
				sched_stask.rstat_cd = 0
				AND
				-- Filter out Historic tasks
				evt_event.event_db_id = sched_stask.sched_db_id AND
				evt_event.event_id    = sched_stask.sched_id
				AND
				evt_event.hist_bool = 0
				AND
				evt_event.rstat_cd = 0
				AND
				-- Get the inventory and assembly inventory for the tasks
				evt_inv.event_db_id   = evt_event.event_db_id AND
				evt_inv.event_id      = evt_event.event_id    AND
				evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id  AND
				evt_inv.inv_no_id     = inv_inv.inv_no_id     AND
				evt_inv.main_inv_bool = 1
				AND
				assmbl_inv.inv_no_db_id (+)= inv_inv.assmbl_inv_no_db_id AND
				assmbl_inv.inv_no_id    (+)= inv_inv.assmbl_inv_no_id
				AND
				assmbl_inv.rstat_cd(+) = 0
				AND
				-- Exclude Inventory that is flagged as Prevent Sync or Locked
				inv_inv.prevent_synch_bool = 0
				AND
				inv_inv.locked_bool = 0
				AND
				inv_inv.rstat_cd = 0
				AND
				-- Get the Task Class
				ref_task_class.task_class_db_id = task_task.task_class_db_id AND
				ref_task_class.task_class_cd    = task_task.task_class_cd
				AND
				ref_task_class.rstat_cd = 0
			 );

	   -- set the result to 1 indicating successful operation
	   aResult := 1;

	-- if any exception occurs, return -1
	EXCEPTION
		  WHEN OTHERS THEN
			 aResult := -1;
			 APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
	END;

	END BaselineSyncPkg;
/

--changeSet MX-17611:44 stripComments:false
/*
9.  Add new menu item for work item admin console [qazwsx]
*/
	INSERT INTO 
		UTL_MENU_ITEM
		(
			MENU_ID, 
			TODO_LIST_ID, 
			MENU_NAME, 
			MENU_LINK_URL, 
			NEW_WINDOW_BOOL, 
			UTL_ID
		)
	SELECT
		20025,
		NULL,
		'web.menuitem.WORK_ITEM_ADMIN_CONSOLE',
		'/maintenix/common/work/WorkItemAdminConsole.jsp',
		0,
		0
	FROM
		dual
	WHERE NOT EXISTS
	(
		SELECT 
			1 
		FROM 
			utl_menu_item 
		WHERE 
			menu_name = 'web.menuitem.WORK_ITEM_ADMIN_CONSOLE'
	);

--changeSet MX-17611:45 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UTL_WORK_ITEM_KEY');
END;
/

--changeSet MX-17611:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UTL_WORK_ITEM_SCHEDULED_DATE');
END;
/

--changeSet MX-17611:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UTL_WORK_ITEM_SERVER_ID');
END;
/