--liquibase formatted sql


--changeSet MX-18509:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
  
   ln_temp varchar2(2000);

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
         evt_event.hist_bool = 0        AND
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
           evt_event.event_id = an_SchedStaskId      AND
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

    ll_SoftDeadline  sched_stask.soft_deadline_bool%TYPE;
    ln_TaskPriorityDbId           sched_stask.task_priority_db_id% TYPE;
    ln_TaskPriorityCd             sched_stask.task_priority_cd%TYPE;    

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
                 DrvTaskInv.event_db_id as driving_task_event_db_id,
                 DrvTaskInv.event_id as driving_task_event_id,
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
                                  
        /* Copy the priority of driving task to REPL task */
        
        /* lrec_DrvTaskDeadline.event_db_id,lrec_DrvTaskDeadline.event_id REPL Task event id. */
        /* Driving_task_event_db_id, driving_task_event_id DRIVING TASK event id. */

          SELECT
            sched_stask.task_priority_db_id,
            sched_stask.task_priority_cd
          INTO
            ln_TaskPriorityDbId,
            ln_TaskPriorityCd
          FROM
            sched_stask
          WHERE
            sched_stask.sched_db_id = lrec_DrvTaskDeadline.driving_task_event_db_id  AND
            sched_stask.sched_id = lrec_DrvTaskDeadline.driving_task_event_id;
            
          IF ln_TaskPriorityDbId IS NOT NULL AND ln_TaskPriorityCd IS NOT NULL THEN
          
             UPDATE
               sched_stask
             SET
               sched_stask.task_priority_db_id = ln_TaskPriorityDbId,
               sched_stask.task_priority_cd = ln_TaskPriorityCd
             WHERE
               sched_stask.sched_db_id = lrec_DrvTaskDeadline.event_db_id AND
               sched_stask.sched_id = lrec_DrvTaskDeadline.event_id;
          
          END IF;
        
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
         evt_event.hist_bool     = 0    AND
         evt_event.rstat_cd  = 0
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
         evt_event.event_type_cd    = 'BLK' AND
         evt_event.rstat_cd = 0
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
   ln_IsOrphan             NUMBER;    
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
                 0 ),
         sched_stask.orphan_frct_bool
     INTO
         ln_IsHistoric, ln_IsSuspend, ln_IsOrphan
     FROM
        evt_event, sched_stask
     WHERE
         evt_event.event_db_id = al_EventDbId ANd
         evt_event.event_id    = al_EventId   AND
         evt_event.rstat_cd = 0
         AND
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id;         

   /* Return if task is historic */
   IF (ln_IsHistoric = 1 )  THEN
      ol_Return := icn_Success;
      RETURN;
   END IF;

   /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
   IF ln_IsOrphan = 1 THEN
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
           inv_inv.rstat_cd = 0;

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
         sched_stask.sched_id    = al_EventId   AND
         sched_stask.rstat_cd    = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         task_task.last_sched_dead_bool = 1;

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
         evt_event.rstat_cd = 0
         AND
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
         AND
         evt_inv.main_inv_bool = 1
         AND
         highest_inv.inv_no_db_id = evt_inv.h_inv_no_db_id AND
         highest_inv.inv_no_id    = evt_inv.h_inv_no_id    AND
         highest_inv.rstat_cd   = 0;


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
     inv_inv.rstat_cd     = 0
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
          e.rstat_cd    = 0
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
          sched_stask.rstat_cd    = 0;

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
            IF lMainInvDbId IS NOT NULL         AND 
                lMainInvPartUseCd = 'TOOLS'     AND
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
             evt_event.event_id    = cl_EventId   AND
             evt_event.rstat_cd    = 0;
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
           evt_event.event_id    = al_EventId   AND
           evt_event.rstat_cd    = 0
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
             req_part.pr_sched_id = cl_SchedId      AND
             req_part.rstat_cd    = 0
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
   an_AcftDbId  IN inv_ac_reg.inv_no_db_id%TYPE,
   an_AcftId    IN inv_ac_reg.inv_no_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE,
   an_Forecast  IN NUMBER,
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
         evt_sched_dead.deviation_qt,
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
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.deviation_qt,
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
      lf_UsageRem := round( ( (ld_SchedDeadDt - ROUND(SYSDATE,'HH')) / lrec_CADeadlines.ref_mult_qt), 6) ;

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
       -- schedule the next task from the extended deadline date
       lt_LastCADue(ls_HashKey) := ld_SchedDeadDt + ( lrec_CADeadlines.deviation_qt * lrec_CADeadlines.ref_mult_qt );
   END IF;
   CLOSE lcur_CADeadlines;
   
   -- process the ACTV task's US deadlines.
   FOR lrec_USDeadlines IN lcur_USDeadlines (an_SchedDbId, an_SchedId) LOOP

          lf_SchedDeadQt  := lrec_USDeadlines.Sched_Dead_Qt;
          lf_StartQt      := lrec_USDeadlines.Start_Qt;
          ld_BeginDate    := SYSDATE;
          lf_USOverlap    := 0;
          ld_SchedDeadDt  := NULL; 
          
          -- If based on a start date we need to re-calculate the start_qt
          IF lrec_USDeadlines.Start_Dt IS NOT NULL THEN
             -- see if we have already calculated this value before
             ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                             TO_CHAR(lrec_USDeadlines.Start_Dt, 'DD-MON-YYYY HH24:MI:SS');
             IF at_GetQt.EXISTS(ls_HashKey) THEN
                lf_StartQt := at_GetQt(ls_HashKey);
             ELSE
                IF lrec_USDeadlines.Start_Dt > SYSDATE THEN
                      IF an_AcftDbId IS NOT NULL THEN
                          -- in the future so predict the quantity                 
                          PredictUsageBetweenDt(an_AcftDbId,
                                                an_AcftId,
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
                          prep_deadline_pkg.GetCurrentInventoryUsage(an_SchedDbId,
                                                                     an_SchedId,
                                                                     lrec_USDeadlines.Data_Type_Db_Id,
                                                                     lrec_USDeadlines.Data_Type_Id,
                                                                     lf_StartQt,
                                                                     on_Return);
                          IF on_Return < 0 THEN
                              RETURN;
                          END IF;
                                                     
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

          IF an_AcftDbId IS NULL THEN
             ld_SchedDeadDt := NULL;
             
          ELSIF at_GetDt.EXISTS(ls_HashKey) THEN
             ld_SchedDeadDt := at_GetDt(ls_HashKey);

          ELSE
              -- find the deadline date
              findForecastedDeadDt_new( an_AcftDbId,
                                    an_AcftId,
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
                -- schedule the next task from the extended deadline quantity
                lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt + lrec_USDeadlines.deviation_qt;
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
                 OR    ( evt_sched_dead.sched_dead_dt IS NULL     AND ld_SchedDeadDt IS NOT NULL)
                 OR    ( evt_sched_dead.sched_dead_dt IS NOT NULL AND ld_SchedDeadDt IS NULL)                 
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
           -- schedule the next task from the extended deadline date
           lt_LastCADue(ls_HashKey) := ld_SchedDeadDt + ( lrec_CADeadlines.deviation_qt * lrec_CADeadlines.ref_mult_qt );
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
              ELSIF an_AcftDbId IS NULL THEN
                 ld_SchedDeadDt := NULL;                 
              ELSIF an_Forecast = 0 THEN
                 -- flagged not to update the estimated date
                 ld_SchedDeadDt := lrec_USDeadlines.sched_dead_dt;                  
              ELSIF at_GetDt.EXISTS(ls_HashKey) THEN
                 ld_SchedDeadDt := at_GetDt(ls_HashKey);
    
              ELSE
                 -- the difference between the deadline date of the forecast task and the previous deadline date
                 lf_UsageRemExtended := lf_UsageRemExtended + lf_USOverlap - lf_BeginQt;
    
                 findForecastedDeadDt_new( an_AcftDbId,
                                           an_AcftId,
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
              -- schedule the next task from the extended deadline quantity
              lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt + lrec_USDeadlines.deviation_qt;          

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
                     OR   (evt_sched_dead.sched_dead_dt IS NULL     AND ld_SchedDeadDt IS NOT NULL)
                     OR   (evt_sched_dead.sched_dead_dt IS NOT NULL AND ld_SchedDeadDt IS NULL)
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
         sched_stask child_task,
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
         -- soft deadline tasks cannot drive the root
         child_task.sched_db_id        = evt_event.event_db_id AND
         child_task.sched_id           = evt_event.event_id    AND
         child_task.soft_deadline_bool = 0  
         AND       
         task_task.task_db_id (+)= sched_stask.task_db_id AND 
         task_task.task_id    (+)= sched_stask.task_id
         AND
         evt_sched_dead.event_db_id       = evt_event.event_db_id AND
         evt_sched_dead.event_id          = evt_event.event_id    AND
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

     /* Otherwise, if a driving deadline is found, retain its event */
     ELSE
         ln_DrivingEventDbId := lrec_Deadline.event_db_id;
         ln_DrivingEventId   := lrec_Deadline.event_id;
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
 *  Recalculates all deadlines on the given aircraft, including FORECAST tasks
 *  
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
                   NVL( task_task.last_sched_dead_bool, 0) AS last_sched_dead_bool,
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
                   sched_stask.main_inv_no_id    = inv_inv.inv_no_id    AND
                   sched_stask.orphan_frct_bool  = 0
                   AND
                   inv_inv.h_inv_no_db_id = an_AircraftDbId AND
                   inv_inv.h_inv_no_id    = an_AircraftId   AND
                   inv_inv.locked_bool    = 0
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

   FOR lrec_Tasks IN lcur_Tasks  LOOP
       -- these values are important in case an error occurs.
       ln_EventDbId := lrec_Tasks.Event_Db_Id;
       ln_EventId   := lrec_Tasks.Event_Id;  
       -- only fire this logic on the non-forecast tasks since it will update the forecast deadlines in the process
       IF lrec_Tasks.data_type_db_id IS NOT NULL AND lrec_Tasks.is_forecast = 0 THEN
          -- 1. Update all CA and US deadlines for the task with deadlines
          UpdateDeadlinesForTask(an_AircraftDbId, an_AircraftId, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, 1, at_QtAsDate, at_DateAsQt, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
             RETURN;
          END IF;
       END IF;

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
                   NVL( task_task.last_sched_dead_bool,0) AS last_sched_dead_bool,
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
                   ref_eng_unit.ref_mult_qt
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
                            sched_stask.main_inv_no_id    = inv_inv.inv_no_id    AND
                            sched_stask.orphan_frct_bool  = 0
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
                   sched_stask.sched_db_id      = evt_event.event_db_id AND
                   sched_stask.sched_id         = evt_event.event_id    AND
                   sched_stask.orphan_frct_bool = 0
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
                  LEVEL DESC;
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
   END LOOP;
   
   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateCADeadlinesLooseInv@@@' || SQLERRM);
      RETURN;
END UpdateCADeadlinesLooseInv;



/**
 *  Recalculates the non-FORECAST deadlines on the given loose component
 */
PROCEDURE UpdateDeadlinesForLooseInv(
          an_AircraftDbId  IN inv_ac_reg.inv_no_db_id%TYPE,
          an_AircraftId    IN inv_ac_reg.inv_no_id%TYPE,
          an_ComponentDbId IN inv_inv.inv_no_db_id%TYPE,
          an_ComponentId   IN inv_inv.inv_no_id%TYPE,
          an_Forecast      IN NUMBER,          
          on_Return        OUT NUMBER ) IS

   CURSOR lcur_Tasks IS
          -- all tasks for this component, ordered leafs first
          WITH loose_component_tasks AS
          (
            SELECT
                   evt_event.nh_event_db_id,
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0) AS last_sched_dead_bool,
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0) AS is_forecast,
                   MAX ( DECODE (recalc_tasks.event_id, evt_event.event_id, DECODE (recalc_tasks.event_db_id, evt_event.event_db_id, 1, 0), 0) ) AS recalc_task
            FROM   -- all actv tasks with deadlines on the component
                   ( SELECT evt_event.h_event_db_id,
                            evt_event.h_event_id,
                            evt_event.event_db_id,
                            evt_event.event_id
                     FROM   evt_event,
                            evt_sched_dead,
                            sched_stask,
                            inv_inv
                     WHERE  inv_inv.h_inv_no_db_id = an_ComponentDbId AND
                            inv_inv.h_inv_no_id    = an_ComponentId   AND
                            inv_inv.locked_bool    = 0
                            AND
                            sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
                            sched_stask.main_inv_no_id    = inv_inv.inv_no_id    AND
                            sched_stask.orphan_frct_bool  = 0
                            AND
                            evt_event.event_db_id     = sched_stask.sched_db_id AND
                            evt_event.event_id        = sched_stask.sched_id    AND
                            evt_event.hist_bool       = 0                       AND
                            evt_event.rstat_cd        = 0                       AND
                            ( ( an_Forecast = 1 )
                              OR
                              ( an_Forecast = 0 AND evt_event.event_status_cd <> 'FORECAST' )
                            )
                            AND
                            evt_sched_dead.event_db_id = evt_event.event_db_id AND
                            evt_sched_dead.event_id    = evt_event.event_id    AND
                            evt_sched_dead.sched_driver_bool = 1   
                   ) recalc_tasks,            
                   sched_stask,
                   evt_event,
                   task_task
            WHERE
                   evt_event.h_event_db_id = recalc_tasks.h_event_db_id AND
                   evt_event.h_event_id    = recalc_tasks.h_event_id    AND
                   evt_event.hist_bool     = 0                          AND
                   evt_event.rstat_cd      = 0
                   AND
                   sched_stask.sched_db_id      = evt_event.event_db_id AND
                   sched_stask.sched_id         = evt_event.event_id    AND
                   sched_stask.orphan_frct_bool = 0
                   AND
                   task_task.task_db_id (+)= sched_stask.task_db_id AND
                   task_task.task_id    (+)= sched_stask.task_id
            GROUP BY
                   evt_event.nh_event_db_id,
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0),
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0)
          )
          SELECT
                   loose_component_tasks.nh_event_db_id,
                   loose_component_tasks.event_db_id,
                   loose_component_tasks.event_id,
                   loose_component_tasks.task_class_cd,
                   loose_component_tasks.last_sched_dead_bool,
                   loose_component_tasks.soft_deadline_bool,
                   loose_component_tasks.main_inv_no_db_id,
                   loose_component_tasks.main_inv_no_id,   
                   loose_component_tasks.is_forecast,
                   loose_component_tasks.recalc_task,
                   LEVEL
          FROM
                   loose_component_tasks

          CONNECT BY
                  PRIOR loose_component_tasks.event_db_id = loose_component_tasks.nh_event_db_id AND
                  PRIOR loose_component_tasks.event_id    = loose_component_tasks.nh_event_id

          START WITH
                  loose_component_tasks.nh_event_db_id IS NULL AND
                  loose_component_tasks.nh_event_id    IS NULL
          ORDER BY
                  loose_component_tasks.is_forecast, LEVEL DESC;
   lrec_Tasks lcur_Tasks%ROWTYPE;

   -- save the date for the usage/datatype
   at_QtAsDate      hash_date_type;
   at_DateAsQt      hash_float_type;
   ln_EventDbId     sched_stask.sched_db_id%TYPE;
   ln_EventId       sched_stask.sched_id%TYPE;
   
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;
   
   FOR lrec_Tasks IN lcur_Tasks  LOOP
       -- these values are important in case an error occurs.
       ln_EventDbId := lrec_Tasks.Event_Db_Id;
       ln_EventId   := lrec_Tasks.Event_Id;

       -- only fire this logic on the non-forecast tasks since it will update the forecast deadlines in the process
       IF lrec_Tasks.recalc_task = 1 AND lrec_Tasks.is_forecast = 0 THEN  

          -- 1. Update all CA and US deadlines for the task with deadlines
          UpdateDeadlinesForTask(an_AircraftDbId, an_AircraftId, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, an_Forecast, at_QtAsDate, at_DateAsQt, on_Return);
          IF on_Return<0 THEN 
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
             RETURN; 
          END IF;
       END IF;

      -- 2. set the driver_bool and the task priorities
      SetDriverPriorityForTask(lrec_Tasks.main_inv_no_db_id, lrec_Tasks.main_inv_no_id,  lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Soft_Deadline_Bool, lrec_Tasks.last_sched_dead_bool, on_Return);
      IF on_Return<0 THEN
         Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);          
         RETURN;
      END IF;

      -- the following logic applies only to root tasks
      IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN

         -- 3. set the driving task for the root task
         SetDRVTASKForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, on_Return);
         IF on_Return<0 THEN
            Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);             
            RETURN;
         END IF;

         -- 4. update the part requests
         SetPRRequiredByDtForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, on_Return);
         IF on_Return<0 THEN
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
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForLooseInv@@@' || SQLERRM);
      RETURN;
END UpdateDeadlinesForLooseInv;



/**
 *  Recalculates the deadlines of non-FORECAST tasks on the given attached component.
 */
PROCEDURE UpdateDeadlinesForAttachedInv(
          an_AircraftDbId  IN inv_ac_reg.inv_no_db_id%TYPE,
          an_AircraftId    IN inv_ac_reg.inv_no_id%TYPE,
          an_ComponentDbId IN inv_inv.inv_no_db_id%TYPE,
          an_ComponentId   IN inv_inv.inv_no_id%TYPE,
          an_Forecast      IN NUMBER,          
          on_Return        OUT NUMBER ) IS

 CURSOR lcur_Tasks IS
          WITH attchd_component_tasks AS
          ( 
            SELECT evt_event.nh_event_db_id, 
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0) AS last_sched_dead_bool,
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,        
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0) AS is_forecast,                  
                   MAX ( DECODE (recalc_tasks.event_id, evt_event.event_id, DECODE (recalc_tasks.event_db_id, evt_event.event_db_id, 1, 0), 0) ) AS recalc_task
            FROM
                   ( SELECT evt_event.h_event_db_id,
                            evt_event.h_event_id,
                            evt_event.event_db_id,
                            evt_event.event_id
                     FROM   evt_event,
                            evt_sched_dead,
                            sched_stask,
                            (SELECT inv_inv.inv_no_db_id,
                                    inv_inv.inv_no_id
                             FROM   inv_inv
                             START WITH
                                    inv_inv.inv_no_db_id = an_ComponentDbId AND
                                    inv_inv.inv_no_id    = an_ComponentId   AND
                                    inv_inv.locked_bool  = 0
                             CONNECT BY
                                    inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
                                    inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
                            ) inv_inv
                     WHERE  sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
                            sched_stask.main_inv_no_id    = inv_inv.inv_no_id    AND
                            sched_stask.orphan_frct_bool  = 0
                            AND
                            evt_event.event_db_id     = sched_stask.sched_db_id AND
                            evt_event.event_id        = sched_stask.sched_id    AND
                            evt_event.hist_bool       = 0                       AND
                            evt_event.rstat_cd        = 0                       AND
                            ( ( an_Forecast = 1 )
                              OR
                              ( an_Forecast = 0 AND evt_event.event_status_cd <> 'FORECAST' )
                            )                            
                            AND
                            evt_sched_dead.event_db_id = evt_event.event_db_id AND
                            evt_sched_dead.event_id    = evt_event.event_id    AND
                            evt_sched_dead.sched_driver_bool = 1   
                   ) recalc_tasks,
                   sched_stask,
                   task_task,
                   evt_event
            WHERE  
                   evt_event.h_event_db_id = recalc_tasks.h_event_db_id AND
                   evt_event.h_event_id    = recalc_tasks.h_event_id    AND
                   evt_event.hist_bool     = 0                          AND
                   evt_event.rstat_cd      = 0
                   AND
                   sched_stask.sched_db_id      = evt_event.event_db_id AND
                   sched_stask.sched_id         = evt_event.event_id    AND
                   sched_stask.orphan_frct_bool = 0
                   AND
                   task_task.task_db_id (+)= sched_stask.task_db_id AND
                   task_task.task_id    (+)= sched_stask.task_id
            GROUP BY
                   evt_event.nh_event_db_id, 
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0),
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,        
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0)
          )  
          SELECT  
                   attchd_component_tasks.event_db_id,
                   attchd_component_tasks.event_id,
                   attchd_component_tasks.nh_event_db_id,   
                   attchd_component_tasks.task_class_cd,
                   attchd_component_tasks.last_sched_dead_bool,
                   attchd_component_tasks.soft_deadline_bool,
                   attchd_component_tasks.main_inv_no_db_id,
                   attchd_component_tasks.main_inv_no_id,
                   attchd_component_tasks.is_forecast,
                   attchd_component_tasks.recalc_task,
                   LEVEL          
               
           FROM    attchd_component_tasks  
          CONNECT BY 
                  PRIOR attchd_component_tasks.event_db_id = attchd_component_tasks.nh_event_db_id AND 
                  PRIOR attchd_component_tasks.event_id    = attchd_component_tasks.nh_event_id
          START WITH 
                  attchd_component_tasks.nh_event_db_id IS NULL AND 
                  attchd_component_tasks.nh_event_id    IS NULL
          ORDER BY 
                  attchd_component_tasks.is_forecast, LEVEL DESC;   
   lrec_Tasks lcur_Tasks%ROWTYPE;

   -- save the date for the usage/datatype
   at_QtAsDate      hash_date_type;
   at_DateAsQt      hash_float_type;
   ln_EventDbId     sched_stask.sched_db_id%TYPE;
   ln_EventId       sched_stask.sched_id%TYPE;

BEGIN
   -- Initialize the return value
   on_Return        := icn_NoProc;
   
   FOR lrec_Tasks IN lcur_Tasks  LOOP
       -- these values are important in case an error occurs.
       ln_EventDbId := lrec_Tasks.Event_Db_Id;
       ln_EventId   := lrec_Tasks.Event_Id;

       -- only fire this logic on the non-forecast tasks since it will update the forecast deadlines in the process
       IF lrec_Tasks.recalc_task = 1 AND lrec_Tasks.is_forecast = 0 THEN  

          -- 1. Update all CA and US deadlines for the task with deadlines
          UpdateDeadlinesForTask(an_AircraftDbId, an_AircraftId, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, an_Forecast, at_QtAsDate, at_DateAsQt, on_Return);
          IF on_Return<0 THEN 
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId); 
             RETURN; 
          END IF;
       END IF;

        -- 2. set the driver_bool and the task priorities
       SetDriverPriorityForTask(lrec_Tasks.main_inv_no_db_id, lrec_Tasks.main_inv_no_id, lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Soft_Deadline_Bool, lrec_Tasks.last_sched_dead_bool, on_Return);
       IF on_Return<0 THEN
          Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);          
          RETURN;
       END IF;
  
       -- the following logic applies only to root tasks
       IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN
  
          -- 3. set the driving task for the root task
          SetDRVTASKForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);             
             RETURN;
          END IF;
  
          -- 4. update the part requests
          SetPRRequiredByDtForRootTask(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, on_Return);
          IF on_Return<0 THEN
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
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForAttachedInv@@@' || SQLERRM);
      RETURN;
END UpdateDeadlinesForAttachedInv;

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
* Orig.Coder:
* Recent Coder:   jbajer
* Recent Date:
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

   ln_AircraftDbId inv_ac_reg.inv_no_db_id%TYPE;
   ln_AircraftId   inv_ac_reg.inv_no_id%TYPE;   
   lv_InvClassCd   inv_inv.inv_class_cd%TYPE;
   ln_IsLoose      NUMBER;
   ln_IsLocked     NUMBER;
   ln_Forecast     NUMBER;
      
BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;
   ln_Forecast := 0;
   
   SELECT h_inv_inv.inv_no_db_id,
          h_inv_inv.inv_no_id,
          h_inv_inv.inv_class_cd,
          DECODE(inv_inv.nh_inv_no_id, NULL,  1, 0),
          inv_inv.locked_bool
   INTO   ln_AircraftDbId,
          ln_AircraftId,
          lv_InvClassCd,
          ln_IsLoose,
          ln_IsLocked
   FROM   inv_inv, 
          inv_inv h_inv_inv
   WHERE  inv_inv.inv_no_db_id = an_InvNoDbId AND
          inv_inv.inv_no_id    = an_InvNoId   
          AND
          h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
          h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id;

   IF lv_InvClassCd <> 'ACFT' THEN
      ln_AircraftDbId := NULL;
      ln_AircraftId   := NULL;
      -- if not attached to an aircraft, update the forecast tasks.
      ln_Forecast     := 1;  
   END IF;
   
   IF ln_IsLocked = 1   THEN
      on_Return := icn_Success; -- do not update deadlines if the inventory is locked
   ELSIF ln_IsLoose = 1 THEN
      UpdateDeadlinesForLooseInv(ln_AircraftDbId, ln_AircraftId, an_InvNoDbId, an_InvNoId, ln_Forecast, on_Return);
   ELSE
      UpdateDeadlinesForAttachedInv(ln_AircraftDbId, ln_AircraftId, an_InvNoDbId, an_InvNoId, ln_Forecast, on_Return);
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlineForInvTree@@@' || SQLERRM);
      RETURN;
END UpdateDeadlineForInvTree;

END event_pkg;
/

--changeSet MX-18509:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PREP_DEADLINE_PKG IS

/* Subtypes internal to the package. */
TYPE typrec_SchedulingRule IS RECORD (

   DataTypeDbId      NUMBER,
   DataTypeId        NUMBER,
   IntervalQt        FLOAT(22),
   InitialQt         FLOAT(22),
   DeviationQt       FLOAT(22),
   PrefixedQt        FLOAT(22),
   PostfixedQt       FLOAT(22)
);

TYPE typtabrec_SchedulingRuleTable IS TABLE OF typrec_SchedulingRule INDEX BY binary_integer;


TYPE typrec_ScheduleDetails IS RECORD (

   -- Stask details
   STaskDbId NUMBER,
   STaskId   NUMBER,
   FirstInstanceBool BOOLEAN,
   PreviousSTaskDbId NUMBER,
   PreviousSTaskId   NUMBER,
   HInvNoDbId        NUMBER,
   HInvNoId          NUMBER,
   PartNoDbId        NUMBER,
   PartNoId          NUMBER,

   -- Task definition details
   ActiveTaskTaskDbId   NUMBER,
   ActiveTaskTaskId     NUMBER,
   RevisionTaskTaskDbId NUMBER,
   RevisionTaskTaskId   NUMBER,
   EffectiveDt          DATE,
   RelativeBool         NUMBER,
   RecurringBool        NUMBER,
   SchedFromReceivedDtBool NUMBER,
   TaskClassCd          VARCHAR2(8),
   ScheduleToLast       NUMBER
);


/********************************************************************************
*
* Procedure: GetTwoLastRevisions
* Arguments:
*            an_TaskDbId task definition pk
*            an_TaskId   -- // --
*
* Return:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_PrevTaskDbId   - previous task definition
*            an_PrevTaskId     -- // --
*            on_Return         - 1 is success
*
* Description: This procedure returns two latest revisions for a task definition.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE GetTwoLastRevisions(
            an_TaskDbId IN task_task.task_db_id%TYPE,
            an_TaskId IN task_task.task_id%TYPE,
            an_LatestTaskDbId OUT task_task.task_db_id%TYPE,
            an_LatestTaskId OUT task_task.task_id%TYPE,
            an_PrevTaskDbId OUT task_task.task_db_id%TYPE,
            an_PrevTaskId OUT task_task.task_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


/* get the deadline */
CURSOR lcur_TwoLastRevisions  IS
 SELECT
       prev_task_def.task_db_id as prev_task_db_id,
       prev_task_def.task_id as prev_task_id,
       new_task_def.task_db_id as new_task_db_id,
       new_task_def.task_id as new_task_id,
       task_defn.task_defn_db_id,
       task_defn.task_defn_id

 FROM task_defn,
       task_task new_task_def,
       task_task prev_task_def,
       task_task start_task_def
 WHERE
      -- get main task definition
      start_task_def.task_db_id=an_TaskDbId AND
      start_task_def.task_id=an_TaskId
      AND
      task_defn.task_defn_db_id=start_task_def.task_defn_db_id AND
      task_defn.task_defn_id=start_task_def.task_defn_id
      AND
      -- get task with previous task definition
      prev_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      prev_task_def.task_defn_id=task_defn.task_defn_id AND
      prev_task_def.revision_ord = task_defn.last_revision_ord-1
      AND
      new_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      new_task_def.task_defn_id=task_defn.task_defn_id AND
      new_task_def.revision_ord = task_defn.last_revision_ord;
      lrec_TwoLastRevisions  lcur_TwoLastRevisions%ROWTYPE;
BEGIN


   on_Return := icn_NoProc;

   OPEN  lcur_TwoLastRevisions();
   FETCH lcur_TwoLastRevisions INTO lrec_TwoLastRevisions;
   CLOSE lcur_TwoLastRevisions;


   an_LatestTaskDbId:= lrec_TwoLastRevisions.new_task_db_id;
   an_LatestTaskId  := lrec_TwoLastRevisions.new_task_id ;
   an_PrevTaskDbId  := lrec_TwoLastRevisions.prev_task_db_id;
   an_PrevTaskId  := lrec_TwoLastRevisions.prev_task_id;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetTwoLastRevisions@@@'||SQLERRM);
      RETURN;

END GetTwoLastRevisions;

/********************************************************************************
*
* Procedure:    GetActualDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
* Return:
*           as_Sched_FromCd (char) - scheduled from refterm
*           an_IntervalQt   (float) - deadline interval qt
*           an_NotifyQt     (float) - deadline notify qt
*           an_DeviationQt  (float) - deadline deviation qt
*           an_PrefixedQt   (float) - deadline prefixed qt
*           an_PostfixedQt  (float) - deadline postfixed qt
*           ad_StartDate    (date)  - start date
*           an_StartQt      (float) - start qt
*           an_InitialIntervalBool (boolean) -true if using def_inital_interval
*           on_Return       (long) - 1 Success/ Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetActualDeadline(
            an_DataTypeDbId        IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId          IN task_sched_rule.data_type_id%TYPE,
            an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_SchedId             IN sched_stask.sched_id%TYPE,
            as_Sched_FromCd        OUT evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt          OUT task_sched_rule.def_interval_qt%TYPE,
            an_NotifyQt            OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt         OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt          OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt         OUT task_sched_rule.def_postfixed_qt%TYPE,
            ad_StartDt             OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt             OUT evt_sched_dead.start_qt%TYPE,
            an_DeadlineExists      OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return              OUT typn_RetCode
   ) IS

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;


/* get the deadline */
CURSOR lcur_ActualsDeadlines  IS
      SELECT
           evt_sched_dead.interval_qt,
           evt_sched_dead.notify_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_sched_dead.sched_from_cd,
           evt_sched_dead.start_dt,
           evt_sched_dead.start_qt
      FROM
          evt_sched_dead
      WHERE
           evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
           evt_sched_dead.data_type_id = an_DataTypeId
           AND
           evt_sched_dead.event_db_id =  an_SchedDbId AND
           evt_sched_dead.event_id = an_SchedId;
           lrec_ActualsDeadlines  lcur_ActualsDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* get actual deadline of a task */
   OPEN  lcur_ActualsDeadlines();
   FETCH lcur_ActualsDeadlines INTO lrec_ActualsDeadlines;
   IF NOT lcur_ActualsDeadlines%FOUND THEN
      -- no actual deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_ActualsDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_ActualsDeadlines;


   an_IntervalQt   := lrec_ActualsDeadlines.interval_qt;
   an_NotifyQt     := lrec_ActualsDeadlines.notify_qt ;
   an_DeviationQt  := lrec_ActualsDeadlines.deviation_qt;
   an_PrefixedQt   := lrec_ActualsDeadlines.prefixed_qt;
   an_PostfixedQt  := lrec_ActualsDeadlines.postfixed_qt;
   as_Sched_FromCd :=lrec_ActualsDeadlines.sched_from_cd;
   ad_StartDt      :=lrec_ActualsDeadlines.start_dt;
   an_StartQt      :=lrec_ActualsDeadlines.start_qt;

   /* Measurement scheduling rules*/
      /* If this task satisfies the ME sched rules condition then use the baseline values*/
      GetMESchedRuleDeadline(
                             an_SchedDbId,
                             an_SchedId,
                             an_DataTypeDbId,
                             an_DataTypeId,
                             ln_IntervalQt_me,
                             ln_NotifyQt_me,
                             ln_DeviationQt_me,
                             ln_PrefixedQt_me,
                             ln_PostfixedQt_me,
                             ln_Return_me
      );

      IF ln_Return_me > 0 THEN
         an_IntervalQt   := ln_IntervalQt_me;
         an_NotifyQt     := ln_NotifyQt_me;
         an_DeviationQt  := ln_DeviationQt_me;
         an_PrefixedQt   := ln_PrefixedQt_me;
         an_PostfixedQt  := ln_PostfixedQt_me;
      END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetActualDeadline@@@'||SQLERRM);
      RETURN;

END GetActualDeadline;

/********************************************************************************
*
* Procedure:    GetBaselineDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
*           an_TaskDbId     (long) - The primary key of the task's definition key
*           an_TaskId       (long) --//--
*           an_PartNoDbId   (long) - The primary key of the main inventory part no
*           an_PartNoId     (long) --//--
* Return:
*           an_IntervalQt   (float) - deadline interval qt
*           an_InitialQt    (float) - deadline initial qt
*           an_NotifyQt     (float) - dedline notify qt
*           an_DeviationQt  (float) - dedline deviation qt
*           an_PrefixedQt   (float) - dedline prefixed qt
*           an_PostfixedQt  (float) - dedline postfixed qt
*           an_DeadlineExists  (long) - 1 if baseline deadline exists
*           on_Return       (long) - Success/Failure
*
* Description:  This procedure is used to get deadline from task_sched_rule table.
*               Baselined deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetBaselineDeadline(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            an_TaskDbId     IN task_interval.task_db_id%TYPE,
            an_TaskId       IN task_interval.task_id%TYPE,
            an_SchedDbId    IN sched_stask.task_db_id%TYPE,
            an_SchedId      IN sched_stask.task_id%TYPE,
            an_PartNoDbId   IN task_interval.part_no_db_id%TYPE,
            an_PartNoId     IN task_interval.part_no_id%TYPE,
            an_HInvNoDbId   IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId     IN task_ac_rule.inv_no_id%TYPE,
            an_IntervalQt      OUT task_sched_rule.def_interval_qt%TYPE,
            an_InitialQt       OUT task_sched_rule.def_initial_qt%TYPE,
            an_NotifyQt        OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt     OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt      OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt     OUT task_sched_rule.def_postfixed_qt%TYPE,
            an_DeadlineExists  OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return          OUT typn_RetCode
   ) IS

    /*
   || Cursor used to get BASELINED scheduling rules for the task.  This
   || will only return deadlines for tasks with task definitions, since adhoc
   || task cannot have baselines deadlines.
   || It  will bring back the part-specific interval_qt if it exists.
   ||
   || When determine the deadline date for an actual task, the scheduling rules should be taken in this order:
   ||    Measurement Specifc
   ||    Part Sepcific
   ||    Tail Specific
   ||    Standard
   */
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id%TYPE,
            cn_TaskId      task_interval.task_id%TYPE,
            cn_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            cn_DataTypeId IN task_sched_rule.data_type_id%TYPE,
            cn_PartNoDbId  task_interval.part_no_db_id%TYPE,
            cn_PartNoId    task_interval.part_no_id%TYPE
         ) IS
         -- Standard, if no Part or Tail rules
         SELECT
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_notify_qt      notify_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId       AND
            task_sched_rule.data_type_db_id = cn_DataTypeDbId AND
            task_sched_rule.data_type_id    = cn_DataTypeId

            -- No Part Specific exists
            AND NOT EXISTS
             ( SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId )

            -- No Tail Specific exists
            AND NOT EXISTS
               (  SELECT 1
                  FROM
                     task_ac_rule
                  WHERE
                     task_ac_rule.task_db_id      = cn_TaskDbId     AND
                     task_ac_rule.task_id         = cn_TaskId       AND
                     task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
                     task_ac_rule.data_type_id    = cn_DataTypeId   AND
                     task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
                     task_ac_rule.inv_no_id       = an_HInvNoId )

        UNION ALL

         -- Tail Specific rules
         SELECT
            task_ac_rule.interval_qt,
            task_ac_rule.initial_qt,
            task_ac_rule.notify_qt,
            task_ac_rule.deviation_qt,
            task_ac_rule.prefixed_qt,
            task_ac_rule.postfixed_qt
         FROM
            task_ac_rule
         WHERE
            task_ac_rule.task_db_id      = cn_TaskDbId     AND
            task_ac_rule.task_id         = cn_TaskId       AND
            task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
            task_ac_rule.data_type_id    = cn_DataTypeId   AND
            task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
            task_ac_rule.inv_no_id       = an_HInvNoId
            -- No Part Specific exists
            AND NOT EXISTS
            (
               SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId
            )
        UNION ALL

         -- Part Specific Rules
         SELECT
            task_interval.interval_qt,
            task_interval.initial_qt,
            task_interval.notify_qt,
            task_interval.deviation_qt,
            task_interval.prefixed_qt,
            task_interval.postfixed_qt
         FROM
            task_interval
         WHERE
            task_interval.task_db_id      = cn_TaskDbId     AND
            task_interval.task_id         = cn_TaskId       AND
            task_interval.data_type_db_id = cn_DataTypeDbId AND
            task_interval.data_type_id    = cn_DataTypeId   AND
            task_interval.part_no_db_id   = cn_PartNoDbId   AND
            task_interval.part_no_id      = cn_PartNoId;

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;

            lrec_BaselineDeadlines  lcur_BaselineDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* If Measurement scheduling rules exist, then use them*/
   GetMESchedRuleDeadline(
                          an_SchedDbId,
                          an_SchedId,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_IntervalQt_me,
                          ln_NotifyQt_me,
                          ln_DeviationQt_me,
                          ln_PrefixedQt_me,
                          ln_PostfixedQt_me,
                          ln_Return_me
      );   

   IF ln_Return_me > 0 THEN
      an_IntervalQt   := ln_IntervalQt_me;
      an_NotifyQt     := ln_NotifyQt_me;
      an_DeviationQt  := ln_DeviationQt_me;
      an_PrefixedQt   := ln_PrefixedQt_me;
      an_PostfixedQt  := ln_PostfixedQt_me;
      an_InitialQt    := 0;

      an_DeadlineExists :=1;
      on_Return         := icn_Success;
      RETURN;
   END IF;

    /* get the baseline deadline for this task definition.*/
   OPEN  lcur_BaselineDeadlines(
            an_TaskDbId,
            an_TaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            an_PartNoDbId,
            an_PartNoId );
   FETCH lcur_BaselineDeadlines INTO lrec_BaselineDeadlines;
   IF NOT lcur_BaselineDeadlines%FOUND THEN
      -- no baseline deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_BaselineDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_BaselineDeadlines;

   /* initialize the out variables */
   an_IntervalQt      := lrec_BaselineDeadlines.interval_qt;
   an_InitialQt       := lrec_BaselineDeadlines.initial_qt;
   an_NotifyQt        := lrec_BaselineDeadlines.notify_qt ;
   an_DeviationQt     := lrec_BaselineDeadlines.deviation_qt;
   an_PrefixedQt      := lrec_BaselineDeadlines.prefixed_qt;
   an_PostfixedQt     := lrec_BaselineDeadlines.postfixed_qt;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetBaselineDeadline@@@'||SQLERRM);
      RETURN;

END GetBaselineDeadline;

/********************************************************************************
*
* Procedure: BaselineDeadlinesChanged
* Arguments:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_OrigTaskDbId   - the task definition revision before the synch
*            an_OrigTaskId     -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_HInvNoDbId    -- the highest inventory of the main inventory
*            an_HInvNoId      -- // --
*            an_PartNoDbId    -- the part number of the main inventory
*            an_PartNoId      -- // --
*
* Return:
*            an_BaselineDeadlineChanged - 1 if deadline changed between two baseline revisions
*            on_Return                  - 1 is success
*
* Description: This procedure returns
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  BaselineDeadlinesChanged(
            an_LatestTaskDbId IN task_task.task_db_id%TYPE,
            an_LatestTaskId   IN task_task.task_id%TYPE,
            an_OrigTaskDbId   IN task_task.task_db_id%TYPE,
            an_OrigTaskId     IN task_task.task_id%TYPE,
            an_DataTypeDbId   IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId     IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId     IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     IN task_interval.part_no_db_id%TYPE,
            an_PartNoId       IN task_interval.part_no_id%TYPE,
            an_BaselineDeadlineChanged OUT NUMBER,
            on_Return       OUT typn_RetCode
   ) IS

   ln_orig_exist   NUMBER;
   ln_latest_exist NUMBER;


   /* task rules */
   CURSOR lcur_Rule(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_sched_rule.def_initial_qt,
                        NULL,
                        -999999,
                        task_sched_rule.def_initial_qt
                ) AS def_initial_qt,
                task_sched_rule.def_interval_qt,
                task_sched_rule.def_deviation_qt,
                task_sched_rule.def_prefixed_qt,
                task_sched_rule.def_postfixed_qt
          FROM
                task_sched_rule
          WHERE
                task_sched_rule.task_db_id      = cl_TaskDbId     AND
                task_sched_rule.task_id         = cl_TaskId       AND
                task_sched_rule.data_type_db_id = an_DataTypeDbId AND
                task_sched_rule.data_type_id    = an_DataTypeId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId )
                -- No Tail Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_ac_rule
                  WHERE  task_ac_rule.task_db_id      = cl_TaskDbId     AND
                         task_ac_rule.task_id         = cl_TaskId       AND
                         task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                         task_ac_rule.data_type_id    = an_DataTypeId   AND
                         task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                         task_ac_rule.inv_no_id       = an_HInvNoId);

   /* tail rules*/
   CURSOR lcur_Tail(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_ac_rule.initial_qt,
                        NULL,
                        -999999,
                        task_ac_rule.initial_qt
                 ) AS initial_qt,
                 task_ac_rule.interval_qt,
                 task_ac_rule.deviation_qt,
                 task_ac_rule.prefixed_qt,
                 task_ac_rule.postfixed_qt
          FROM
                 task_ac_rule
          WHERE
                 task_ac_rule.task_db_id      = cl_TaskDbId     AND
                 task_ac_rule.task_id         = cl_TaskId       AND
                 task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                 task_ac_rule.data_type_id    = an_DataTypeId   AND
                 task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                 task_ac_rule.inv_no_id       = an_HInvNoId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId );

   /* part rules*/
   CURSOR lcur_Part(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_interval.initial_qt,
                        NULL,
                        -999999,
                        task_interval.initial_qt
                 ) AS initial_qt,
                 task_interval.interval_qt,
                 task_interval.deviation_qt,
                 task_interval.prefixed_qt,
                 task_interval.postfixed_qt
          FROM
                 task_interval
          WHERE
                 task_interval.task_db_id      = cl_TaskDbId     AND
                 task_interval.task_id         = cl_TaskId       AND
                 task_interval.data_type_db_id = an_DataTypeDbId AND
                 task_interval.data_type_id    = an_DataTypeId   AND
                 task_interval.part_no_db_id   = an_PartNoDbId   AND
                 task_interval.part_no_id      = an_PartNoId;

   /* Measurement scheduling rules*/
   CURSOR lcur_ME(
          cn_PrevTaskDbId      task_task.task_db_id%TYPE,
          cn_PrevTaskId        task_task.task_id%TYPE,
          cn_LatestTaskDbId    task_task.task_db_id%TYPE,
          cn_LatestTaskId      task_task.task_id%TYPE
   )
   IS
   (--post minus pre
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt

     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  )
  UNION
  (--pre minus post
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  );

   /* task_task details */
   CURSOR lcur_Task(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                 task_task.recurring_task_bool,
                 task_task.relative_bool,
                 task_task.sched_from_received_dt_bool,
                 task_task.last_sched_dead_bool,
                 task_task.effective_gdt,
                 task_task.resched_from_cd
          FROM
                 task_task
          WHERE
                 task_task.task_db_id = cl_TaskDbId AND
                 task_task.task_id    = cl_TaskId;

   /* CURSORS */
   lrec_MERule    lcur_ME%ROWTYPE;

   lrec_OrigRule   lcur_Rule%ROWTYPE;
   lrec_LatestRule lcur_Rule%ROWTYPE;

   lrec_OrigPart   lcur_Part%ROWTYPE;
   lrec_LatestPart lcur_Part%ROWTYPE;

   lrec_OrigTail   lcur_Tail%ROWTYPE;
   lrec_LatestTail lcur_Tail%ROWTYPE;

   lrec_OrigTask   lcur_Task%ROWTYPE;
   lrec_LatestTask lcur_Task%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;
   an_BaselineDeadlineChanged:=0;

   /*
   * VERIFY FOR DIFFERENCES IN MEASUREMENT SCHEDULING RULES
   */
   OPEN lcur_ME(an_OrigTaskDbId, an_OrigTaskId, an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_ME INTO lrec_MERule;
   IF lcur_ME%FOUND THEN
      an_BaselineDeadlineChanged:=1;
      CLOSE lcur_ME;
      on_Return := icn_Success;
      RETURN;
   END IF;
   CLOSE lcur_ME;

   /*
   * VERIFY FOR DIFFERENCES IN TASK RULES
   */

   /* get original rule information */
   OPEN lcur_Rule(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Rule INTO lrec_OrigRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   /* get latest rule information */
   OPEN lcur_Rule(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Rule INTO lrec_LatestRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigRule.Def_Interval_Qt  = lrec_LatestRule.Def_Interval_Qt)  OR
           NOT(lrec_OrigRule.Def_Deviation_Qt = lrec_LatestRule.Def_Deviation_Qt) OR
           NOT(lrec_OrigRule.Def_Initial_Qt   = lrec_LatestRule.Def_Initial_Qt)   OR
           NOT(lrec_OrigRule.Def_Prefixed_Qt  = lrec_LatestRule.Def_Prefixed_Qt)  OR
           NOT(lrec_OrigRule.Def_Postfixed_Qt = lrec_LatestRule.Def_Postfixed_Qt)
          ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN PART RULES
   */

   /* get original rule information */
   OPEN lcur_Part(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Part INTO lrec_OrigPart;
   IF NOT lcur_Part%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Part;

   /* get latest rule information */
   OPEN lcur_Part(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Part INTO lrec_LatestPart;
   IF NOT lcur_Part%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Part;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigPart.Interval_Qt  = lrec_LatestPart.Interval_Qt)      OR
           NOT(lrec_OrigPart.Deviation_Qt = lrec_LatestPart.Deviation_Qt)     OR
           NOT(lrec_OrigPart.Initial_Qt   = lrec_LatestPart.Initial_Qt)       OR
           NOT(lrec_OrigPart.Prefixed_Qt  = lrec_LatestPart.Prefixed_Qt)      OR
           NOT(lrec_OrigPart.Postfixed_Qt = lrec_LatestPart.Postfixed_Qt)         
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TAIL RULES
   */

   /* get original rule information */
   OPEN lcur_Tail(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Tail INTO lrec_OrigTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   /* get latest rule information */
   OPEN lcur_Tail(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Tail INTO lrec_LatestTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigTail.Interval_Qt  = lrec_LatestTail.Interval_Qt)      OR
           NOT(lrec_OrigTail.Deviation_Qt = lrec_LatestTail.Deviation_Qt)     OR
           NOT(lrec_OrigTail.Initial_Qt   = lrec_LatestTail.Initial_Qt)       OR
           NOT(lrec_OrigTail.Prefixed_Qt  = lrec_LatestTail.Prefixed_Qt)      OR
           NOT(lrec_OrigTail.Postfixed_Qt = lrec_LatestTail.Postfixed_Qt)         
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TASK SCHEDULING DETAILS
   */

   /* get original task information */
   OPEN lcur_Task(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Task INTO lrec_OrigTask;
   IF NOT lcur_Task%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Task;

   /* get latest task information */
   OPEN lcur_Task(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Task INTO lrec_LatestTask;
   IF NOT lcur_Task%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Task;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both task revisions exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 )
         AND
         (
            (lrec_OrigTask.recurring_task_bool           != lrec_LatestTask.recurring_task_bool)             OR
            (lrec_OrigTask.relative_bool                 != lrec_LatestTask.relative_bool)                   OR
            (lrec_OrigTask.sched_from_received_dt_bool   != lrec_LatestTask.sched_from_received_dt_bool)     
            OR
            NOT(lrec_OrigTask.effective_gdt          = lrec_LatestTask.effective_gdt)                     OR
            (lrec_OrigTask.effective_gdt IS NULL     AND lrec_LatestTask.effective_gdt IS NOT NULL)       OR
            (lrec_OrigTask.effective_gdt IS NOT NULL AND lrec_LatestTask.effective_gdt IS NULL)           
            OR
            NOT(lrec_OrigTask.resched_from_cd          = lrec_LatestTask.resched_from_cd )                OR
            (lrec_OrigTask.resched_from_cd IS NULL     AND lrec_LatestTask.resched_from_cd IS NOT NULL)   OR
            (lrec_OrigTask.resched_from_cd IS NOT NULL AND lrec_LatestTask.resched_from_cd IS NULL)       
         )
      THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@BaselineDeadlinesChanged@@@'||SQLERRM);
      RETURN;

END BaselineDeadlinesChanged;


/********************************************************************************
*
* Procedure: GetValuesAndActionForSynch
* Arguments: an_OrigTaskDbId  -definition revision of the task before synch ran
*            an_OrigTaskId    -- // --
*            an_TaskDbId      -task definition pk
*            an_TaskId        -- // --
*            an_SchedDbId     - the actual task pk
*            an_SchedId       -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_PartNoDbId    - the main inventory part number pk
*            an_PartNoId      -- // --
* Return:
*            an_DeleteActualDealine   - true if actual deadline should be deleted
*            an_UpdateActualDeadline  - true if actual deadline should be updated to match baseline
*            an_InsertActualDeadline  - true if actual deadlin should be created as per baseline
*            as_sched_from_cd         - value of actual sched_from_cd
*            ad_start_dt              - value of actuals start_dt (used for UPDATE action)
*            an_start_qt              - value of actuals start_qt (used for UPDATE action)
*            an_BaselineIntervalQt    - value of baseline interval
*            an_BaselineInitialQt     - value of baseline initial interval
*            an_BaselineNotifyQt      - value of baseline notification quantity
*            an_BaselineDeviationQt   - value of baseline deviation
*            an_BaselinePrefixedQt    - value of baseline prefix
*            an_BaselinePostfixedQt   - value of baseline postfix
*            on_Return                - 1 is success
*
* Description: This procedue will retrieve the action to perform on the actual deadline, either
*              DELETE, CREATE or UPDATE the actual to match the baseline
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    September 29, 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  GetValuesAndActionForSynch(
            an_OrigTaskDbId            IN task_task.task_db_id%TYPE,
            an_OrigTaskId              IN task_task.task_id%TYPE,
            an_TaskDbId                IN task_task.task_db_id%TYPE,
            an_TaskId                  IN task_task.task_id%TYPE,
            an_PrevSchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_PrevSchedId             IN sched_stask.sched_id%TYPE,
            an_SchedDbId               IN sched_stask.task_db_id%TYPE,
            an_SchedId                 IN sched_stask.task_id%TYPE,
            an_DataTypeDbId            IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId              IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId              IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId                IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId              IN task_interval.part_no_db_id%TYPE,
            an_PartNoId                IN task_interval.part_no_id%TYPE,
            an_RecurringBool           IN task_task.recurring_task_bool%TYPE,
            an_EventsOnSameInv         IN NUMBER,

            -- OUT actions
            an_DeleteActualDealine    OUT NUMBER,
            an_UpdateActualDeadline   OUT NUMBER,
            an_InsertActualDeadline   OUT NUMBER,

            -- OUT actual values
            as_sched_from_cd          OUT evt_sched_dead.sched_from_cd%TYPE,
            ad_start_dt               OUT evt_sched_dead.start_dt%TYPE,
            an_start_qt               OUT evt_sched_dead.start_qt%TYPE,

            -- OUT baseline values
            an_BaselineIntervalQt     OUT task_sched_rule.def_interval_qt%TYPE,
            an_BaselineNotifyQt       OUT task_sched_rule.def_notify_qt%TYPE,
            an_BaselineDeviationQt    OUT task_sched_rule.def_deviation_qt%TYPE,
            an_BaselinePrefixedQt     OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_BaselinePostfixedQt    OUT task_sched_rule.def_postfixed_qt%TYPE,

            on_Return                 OUT typn_RetCode
   ) IS

      -- actual information
      ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
      ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
      ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
      ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
      ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
      ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
      ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
      ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
      ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

      -- original baseline information
      ln_OrigIntervalQt          task_sched_rule.def_Interval_Qt%TYPE;
      ln_OrigInitialQt           task_sched_rule.def_initial_qt%TYPE;
      ln_OrigNotifyQt            task_sched_rule.def_notify_qt%TYPE;
      ln_OrigDeviationQt         task_sched_rule.def_deviation_qt%TYPE;
      ln_OrigPrefixedQt          task_sched_rule.def_prefixed_qt%TYPE;
      ln_OrigPostfixedQt         task_sched_rule.def_postfixed_qt%TYPE;
      ln_OrigDeadlineExists      task_sched_rule.def_postfixed_qt%TYPE;

      ln_BaselineInitialQt       task_sched_rule.def_initial_qt%TYPE;
      ln_BaselineDeadlineChanged task_sched_rule.def_postfixed_qt%TYPE;
      ln_BaselineDeadlineExists  task_sched_rule.def_postfixed_qt%TYPE;
      ln_SameTaskDefn            NUMBER;
BEGIN
   -- initialize variables
   on_Return               := icn_NoProc;
   an_DeleteActualDealine  := 0;
   an_UpdateActualDeadline := 0;
   an_InsertActualDeadline := 0;

   /* see if the two tasks are on the same definition */
   SELECT COUNT(*)
   INTO   ln_SameTaskDefn 
   FROM   sched_stask prev_sched,
          task_task   prev_task,
          task_task
   WHERE  prev_sched.sched_db_id = an_PrevSchedDbId AND
          prev_sched.sched_id    = an_PrevSchedId
          AND
          -- task definition of the previous task
          prev_task.task_db_id = prev_sched.task_db_id AND
          prev_task.task_id    = prev_sched.task_id  
          AND
          -- task definition of the current task
          task_task.task_db_id = an_TaskDbId AND
          task_task.task_id    = an_TaskId
          AND
          task_task.assmbl_db_id  = prev_task.assmbl_db_id AND
          task_task.assmbl_cd     = prev_task.assmbl_cd    AND
          task_task.assmbl_bom_id = prev_task.assmbl_bom_id
          AND
          ( --check if they are the same
            (prev_task.task_defn_db_id = task_task.task_defn_db_id AND
            prev_task.task_defn_id    = task_task.task_defn_id)
            OR
            (prev_task.block_chain_sdesc = task_task.block_chain_sdesc)
            OR
            ( EXISTS (SELECT 1 
                      FROM   task_task all_prev,
                             task_task all_post
                      WHERE  all_post.block_chain_sdesc = task_task.block_chain_sdesc
                             AND
                             all_prev.block_chain_sdesc = prev_task.block_chain_sdesc
                             AND
                             all_post.task_defn_db_id = all_post.task_defn_db_id AND
                             all_post.task_defn_id    = all_post.task_defn_id ) )
          );          
          
   /* get original deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_OrigTaskDbId,
            an_OrigTaskId, 
            an_PrevSchedDbId,
            an_PrevSchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            ln_OrigIntervalQt,
            ln_OrigInitialQt,
            ln_OrigNotifyQt,
            ln_OrigDeviationQt,
            ln_OrigPrefixedQt,
            ln_OrigPostfixedQt,
            ln_OrigDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* get current deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_TaskDbId,
            an_TaskId,
            an_SchedDbId,
            an_SchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_BaselineIntervalQt,
            ln_BaselineInitialQt,
            an_BaselineNotifyQt,
            an_BaselineDeviationQt,
            an_BaselinePrefixedQt,
            an_BaselinePostfixedQt,
            ln_BaselineDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
         
   -- if the first task in the chain or first on this inventory or first task of this definition, and the task is recurring, use initial interval
   IF (an_PrevSchedId =-1 OR an_EventsOnSameInv = 0 OR ln_SameTaskDefn = 0 ) AND an_RecurringBool = 1 AND ln_BaselineInitialQt IS NOT NULL THEN
      an_BaselineIntervalQt := ln_BaselineInitialQt;
   END IF;

      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   -- If the baseline doesn't exist and the actual doesn't exists, then do nothing
   IF  ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 0 THEN
      on_Return := icn_Success;
      RETURN;

   -- If the baseline exists and the actual doesn't, then create the actual
   ELSIF ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 0 THEN
      an_InsertActualDeadline := 1;
      on_Return := icn_Success;
      RETURN;

   -- If the baseline doesn't exist and the actual does, then delete the actual
   ELSIF ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 1 THEN
      an_DeleteActualDealine := 1;
      on_Return := icn_Success;
      RETURN;

   -- if both deadlines exist, update to match the baseline
   END IF;


   --Now we know that both exist: ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 1
  BaselineDeadlinesChanged(
            an_TaskDbId,
            an_TaskId,
            an_OrigTaskDbId,
            an_OrigTaskId ,
            an_DataTypeDbId,
            an_DataTypeId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_PartNoDbId,
            an_PartNoId,
            ln_BaselineDeadlineChanged,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   IF ln_BaselineDeadlineChanged = 1 THEN
      an_UpdateActualDeadline := 1;
      as_sched_from_cd        := ls_ActualSchedFromCd;
      ad_start_dt             := ld_ActualStartDt;
      an_start_qt             := ln_ActualStartQt;

      --if the user modified the actual, keep their changes:
      IF ln_OrigDeadlineExists = 1 THEN
         -- if the user extended the actual deadline, keep the extension
         IF ln_ActualDeviationQt <> ln_OrigDeviationQt THEN
            an_BaselineDeviationQt := ln_ActualDeviationQt;
         END IF;
      END IF;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetValuesAndActionForSynch@@@'||SQLERRM || '--' || an_TaskDbId ||':' ||  an_TaskId || '--' || an_SchedDbId || ':' || an_SchedId || '--' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;

END GetValuesAndActionForSynch;
/********************************************************************************
*
* Procedure: DeleteDeadline
* Arguments:
*
*            an_SchedDbId - the actual task pk
*            an_SchedTaskId   -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*
* Return:
*
*            on_Return         - 1 is success
*
* Description: This procedue deletes a deadline form actual task
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2006
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE DeleteDeadline(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS
   BEGIN
      /* delete the deadline */
      DELETE
      FROM evt_sched_dead
      WHERE  event_db_id= an_SchedDbId AND
          event_id= an_SchedId AND
          evt_sched_dead.data_type_db_id=an_DataTypeDbId AND
          evt_sched_dead.data_type_id=an_DataTypeId;
   EXCEPTION

   WHEN OTHERS THEN
   -- Unexpected error
   on_Return := icn_Error;
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@DeleteDeadline@@@'||SQLERRM);
   RETURN;

END DeleteDeadline;

/********************************************************************************
*
* Procedure:    GetUsageParmInfo
* Arguments:
*           an_DataTypeDbId (long) - The primary key of mim data type
*           an_DataTypeId   (long) --//--
* Return:
*           as_DomainTypeCd (char) - domain type of the data type
*           al_RefMultQt    (float)- multiplier for the data type
*           as_EngUnitCd    (char) - eng unit for the data type
*           as_DataTypeCd   (char) - data type code
*           on_Return       (long) - 1 Success/Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetUsageParmInfo(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            as_DomainTypeCd   OUT mim_data_type.domain_type_cd%TYPE,
            al_RefMultQt     OUT ref_eng_unit.ref_mult_qt%TYPE,
            as_EngUnitCd  OUT mim_data_type.eng_unit_cd%TYPE,
            as_DataTypeCd   OUT mim_data_type.data_type_cd%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* select info about the mim datatype */
    SELECT mim_data_type.domain_type_cd,
             ref_eng_unit.ref_mult_qt,
             mim_data_type.eng_unit_cd,
             mim_data_type.data_type_cd
        INTO as_DomainTypeCd,
             al_RefMultQt,
             as_EngUnitCd,
             as_DataTypeCd
        FROM mim_data_type,
             ref_eng_unit
       WHERE ( mim_data_type.data_type_db_id = an_DataTypeDbId ) AND
             ( mim_data_type.data_type_id    = an_DataTypeId )
             AND
             ( ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id ) AND
             ( ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetUsageParmInfo@@@'||SQLERRM);
      RETURN;

END GetUsageParmInfo;


/********************************************************************************
*
* Procedure:    InsertDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*           an_InitialIntervalBool (boolean) -true if using task defn initial interval
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InsertDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt   IN task_sched_rule.def_interval_qt%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_NotifyQt     IN task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt  IN task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /*  insert row into the table */
    INSERT INTO evt_sched_dead(
             event_db_id,
             event_id,
             data_type_db_id,
             data_type_id,
             start_qt,
             start_dt,
             sched_from_db_id,
             sched_from_cd,
             sched_dead_qt,
             sched_dead_dt,
             interval_qt,
             notify_qt,
             deviation_qt,
             prefixed_qt,
             postfixed_qt)
      VALUES(
             an_SchedDbId,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             ad_StartQt,
             ad_StartDt,
             0,
             as_SchedFromCd,
             al_NewDeadlineQt,
             ad_NewDeadlineDt,
             an_IntervalQt,
             an_NotifyQt,
             an_DeviationQt,
             an_PrefixedQt,
             an_PostfixedQt
        );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@InsertDeadlineRow@@@'||SQLERRM);
      RETURN;

END InsertDeadlineRow;

/********************************************************************************
*
* Procedure:    UpdateDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN evt_sched_dead.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_IntervalQt   IN evt_sched_dead.interval_qt%TYPE,
            an_NotifyQt     IN evt_sched_dead.notify_qt%TYPE,
            an_DeviationQt  IN evt_sched_dead.deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* update the deadline */
    UPDATE evt_sched_dead
         SET
            sched_dead_qt              = al_NewDeadlineQt,
            sched_dead_dt              = ad_NewDeadlineDt,
            sched_dead_dt_last_updated = SYSDATE,
            start_qt                   = ad_StartQt,
            start_dt                   = ad_StartDt,
            sched_from_db_id           = 0,
            sched_from_cd              = as_SchedFromCd,
            prefixed_qt                = an_PrefixedQt,
            postfixed_qt               = an_PostfixedQt,
            interval_qt                = an_IntervalQt,
            sched_driver_bool          = 0,
            deviation_qt               = an_DeviationQt,
            notify_qt                  = an_NotifyQt
         WHERE
            event_db_id     = an_SchedDbId AND
            event_id        = an_SchedId AND
            data_type_db_id = an_DataTypeDbId AND
            data_type_id    = an_DataTypeId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateDeadlineRow@@@'||SQLERRM);
      RETURN;

END UpdateDeadlineRow;

/********************************************************************************
*
* Procedure:    GetCorrectiveFaultInfo
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*
*
* Return:
*           an_FaultDbId   (long) - fault primary key
*           an_FaultId     (long) --//-
*           ad_FoundOnDate (Date) - fault raised date
*           on_Return      (long) -  1 Success/Failure
*
* Description:  This procedure returns found on date and fault associated with this task.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCorrectiveFaultInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_FaultDbId    OUT  sd_fault.fault_db_id%TYPE,
            an_FaultId      OUT  sd_fault.fault_id%TYPE,
            ad_FoundOnDate       OUT  evt_event.actual_start_dt%TYPE,
            ad_StartDt           OUT  evt_sched_dead.start_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

     /* fault info */
     CURSOR lcur_corrective_fault IS
     SELECT evt_event_rel.event_db_id,
            evt_event_rel.event_id,
            evt_event.actual_start_dt
     FROM   evt_event_rel,
            evt_event
     WHERE  evt_event_rel.rel_event_db_id = an_SchedDbId AND
            evt_event_rel.rel_event_id    = an_SchedId   AND
            evt_event_rel.rel_type_cd     = 'CORRECT'
            AND
            evt_event.event_db_id = evt_event_rel.event_db_id AND
            evt_event.event_id    = evt_event_rel.event_id 
			AND
            evt_event.rstat_cd	= 0;
      lrec_corrective_fault lcur_corrective_fault%ROWTYPE;

     CURSOR lcur_custom_deadline IS
     SELECT evt_sched_dead.start_dt
     FROM   evt_sched_dead
     WHERE  evt_sched_dead.event_db_id   = an_SchedDbId AND
            evt_sched_dead.event_id      = an_SchedId   AND
            evt_sched_dead.sched_from_cd = 'CUSTOM'     AND
            evt_sched_dead.start_dt      IS NOT NULL;
      lrec_custom_deadline lcur_custom_deadline%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get fault info */
   OPEN  lcur_corrective_fault();
   FETCH lcur_corrective_fault INTO lrec_corrective_fault;
   CLOSE lcur_corrective_fault;

   an_FaultDbId:= lrec_corrective_fault.event_db_id;
   an_FaultId  := lrec_corrective_fault.event_id ;
   ad_FoundOnDate:=lrec_corrective_fault.actual_start_dt;

   OPEN  lcur_custom_deadline();
   FETCH lcur_custom_deadline INTO lrec_custom_deadline;
   IF    lcur_custom_deadline%FOUND THEN
         -- if the found on date is not the same as the custom start date
         IF TRUNC(lrec_custom_deadline.start_dt) <> TRUNC(lrec_corrective_fault.actual_start_dt) THEN
            ad_StartDt:=lrec_custom_deadline.start_dt;
         END IF;
   END IF;
   CLOSE lcur_custom_deadline;
   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetCorrectiveFaultInfo@@@'||SQLERRM);
      IF lcur_custom_deadline%ISOPEN THEN CLOSE lcur_custom_deadline; END IF;      
      RETURN;

END GetCorrectiveFaultInfo;


/********************************************************************************
*
* Procedure:    GetLastCompletionUsageInfo
* Arguments:
*            an_EventDbId    (long)  - event primary key
*            an_EventId      (long)  --//--
*            an_DataTypeDbId (long)  - data type primary key
*            an_DataTypeId   (long)  --//--
*
* Return:
*            an_DeadlineTSN  (float) - deadline TSN value
*            an_EvtUsageTSN  (float) - usage snapshot TSN value
*            ab_HistBool     (long)  - 1 if event historic, 0 if not historic
*            on_Return       (long)  - 1 if success
*
* Description:  This procedure gets the latest usage snapshot TSN info, and deadline due TSN
*               for the event.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetLastCompletionUsageInfo(
            an_EventDbId       IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId         IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId    IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId      IN  evt_sched_dead.data_type_id%TYPE,
            an_DeadlineTSN     OUT evt_sched_dead.sched_dead_qt%TYPE,
            an_ExtDeadlineTSN  OUT evt_sched_dead.sched_dead_qt%TYPE,            
            an_EvtUsageTSN     OUT evt_inv_usage.tsn_qt%TYPE,
            ab_HistBool        OUT evt_event.hist_bool%TYPE,
            on_Return          OUT typn_RetCode
   ) IS

 /* usage snapshot for the event, and due deadline tsn*/
 CURSOR lcur_old_complete_usage (
         cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
         cn_EventId        evt_sched_dead.event_id%TYPE,
         cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
         cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
      ) IS
      SELECT
         evt_sched_dead.sched_dead_qt,
         evt_inv_usage.tsn_qt,
         evt_sched_dead.deviation_qt,         
         evt_event.hist_bool
      FROM
         evt_inv_usage,
         evt_sched_dead,
         evt_event
      WHERE
         evt_event.event_db_id = cn_EventDbId AND
         evt_event.event_id    = cn_EventId
         AND
         evt_event.rstat_cd	= 0
         AND
         evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
         evt_sched_dead.event_id        (+)= evt_event.event_id AND
         evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId AND
         evt_sched_dead.data_type_id    (+)= cn_DataTypeId
         AND
         evt_inv_usage.event_db_id     (+)= evt_event.event_db_id AND
         evt_inv_usage.event_id        (+)= evt_event.event_id AND
         evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId AND
         evt_inv_usage.data_type_id    (+)= cn_DataTypeId;
   lrec_old_complete_usage lcur_old_complete_usage%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* find the usage snapshot and deadline tsn value*/
   OPEN  lcur_old_complete_usage(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_old_complete_usage INTO lrec_old_complete_usage;
   CLOSE lcur_old_complete_usage;

   /* intialize the variables */
   an_DeadlineTSN    := lrec_old_complete_usage.sched_dead_qt;
   an_ExtDeadlineTSN := lrec_old_complete_usage.sched_dead_qt + lrec_old_complete_usage.deviation_qt;
   an_EvtUsageTSN    := lrec_old_complete_usage.tsn_qt ;
   ab_HistBool       := lrec_old_complete_usage.hist_bool;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetLastCompletionUsageInfo@@@'||SQLERRM);
      RETURN;

END GetLastCompletionUsageInfo;


/********************************************************************************
*
* Procedure:    FindCalendarDeadlineVariables
* Arguments:
*            an_DataTypeDbId  (long)  - data type primary key
*            an_DataTypeId    (long)  --//--
*            ad_StartDt       (date)  - deadline start date
*            an_Interval      (float) - deadline interval
*            ad_NewDeadlineDt (date)  - deadline date
*
* Return:
*            ad_StartDt       (date)  - new deadline start date
*            an_Interval      (float) - new deadline interval
*            ad_NewDeadlineDt (date)  - mew deadline date
*            on_Return        (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartDt, an_Interval, ad_NewDeadlineDt
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Jonathan Clarkin
* Recent Date:    May 5, 2006
*
*********************************************************************************
*
* Copyright 2000-2006 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindCalendarDeadlineVariables(
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDt       IN OUT evt_sched_dead.start_dt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_dt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /*local variables */
   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   lb_EndOfDay                BOOLEAN;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get deadline parm info*/
   GetUsageParmInfo(
         an_DataTypeDbId,
         an_DataTypeId,
         ls_DomainTypeCd,
         ll_RefMultQt,
         ls_EngUnitCd,
         ls_DataTypeCd,
         on_Return   );
   IF on_Return < 0 THEN
     RETURN;
   END IF;

   /* Only round non Calendar Hour based dates */
   IF (NOT UPPER(ls_DataTypeCd) = 'CHR') THEN

      IF (ad_StartDt IS NOT NULL) THEN
         ad_StartDt := TO_DATE(TO_CHAR(ad_StartDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;


      /* round the all calendar deadlines to the end of the day */
      IF (ad_NewDeadlineDt IS NOT NULL)  AND lb_EndOfDay THEN
         ad_NewDeadlineDt := TO_DATE(TO_CHAR(ad_NewDeadlineDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;

   END IF;

   /* -- Calculate Deadline Date -- */
   IF (ad_NewDeadlineDt is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         ad_NewDeadlineDt := ADD_MONTHS( ad_StartDt, an_Interval ) + (an_Interval - TRUNC(an_Interval)) * ll_RefMultQt;

      /* if the data type is month then use months to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_StartDt:= LAST_DAY(ad_StartDt);
         ad_NewDeadlineDt :=  ADD_MONTHS( ad_StartDt, an_Interval );

      /* if the data type is year then use years to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
     ad_NewDeadlineDt := ADD_MONTHS( ad_StartDt, an_Interval*12 );

      /* If it is a Calendar Hour, do not truncate */
      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         ad_NewDeadlineDt := ad_StartDt + (an_Interval * ll_RefMultQt);

      /* add the correct # of days to the start date */
      ELSE
         ad_NewDeadlineDt := ad_StartDt + TRUNC(an_Interval * ll_RefMultQt);
      END IF;

   /* -- Calculate Start Date -- */
   ELSIF (ad_StartDt is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval ) - (an_Interval - TRUNC(an_Interval)) * ll_RefMultQt;

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval );

      /* if the data type is year then use years to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
          ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval*12 );

      /* add the correct # of days to the start date */
      ELSE
         ad_StartDt := ad_NewDeadlineDt  - (an_Interval * ll_RefMultQt);
      END IF;


   /* -- Calculate Interval -- */
   ELSIF (an_Interval is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' OR UPPER(ls_DataTypeCd) = 'CLMON' THEN
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= LAST_DAY(ad_StartDt);
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
         an_Interval := TRUNC( MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt )/12);

      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         an_Interval :=  (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt;

      /* add the correct # of days to the start date */
      ELSE
         an_Interval :=  TRUNC( (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt );
      END IF;

   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindCalendarDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindCalendarDeadlineVariables;


/********************************************************************************
*
* Procedure: FindUsageDeadlineVariables
* Arguments:
*            ad_StartQt        (float)  - deadline start qt
*            an_Interval       (float)  - deadline interval
*            ad_NewDeadlineTSN (float)  - deadline TSN due value
*
* Return:
             ad_StartDt      (date)  - new deadline start date
*            an_Interval     (float) - new deadline interval
*            ad_NewDeadlineDt (date) - mew deadline TSN due value
*            on_Return       (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartQt, an_Interval, ad_NewDeadlineTSN
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindUsageDeadlineVariables(
            ad_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineTSN IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

       /* recalculate deadline TSN */
       IF (ad_NewDeadlineTSN is NULL) THEN
            ad_NewDeadlineTSN := NVL( ad_StartQt, 0 ) + NVL( an_Interval, 0 );
       END IF;
       IF (ad_StartQt is NULL) THEN
            ad_StartQt := ad_NewDeadlineTSN  - NVL( an_Interval, 0 );
       END IF;
       IF (an_Interval is NULL) THEN
            an_Interval :=  ad_NewDeadlineTSN - ad_StartQt;
       END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindUsageDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindUsageDeadlineVariables;

/********************************************************************************
*
* Procedure: GetOldCompleteDate
* Arguments:
*           an_EventDbId    (long)  - event primary key
*           an_EventId      (long)  --//--
*           an_DataTypeDbId (long)  - deadline data type primary key
*           an_DataTypeId   (long)  --//--
*
* Return:
*           an_DueDate (date) - due date
*           an_EndDate (date) - end date
*           ab_HistBool (long) - 1 if task is historic
*           on_Return       - 1 is success
*
* Description: This procedure returnes due and completion dates.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetOldCompleteDate(
            an_EventDbId    IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId      IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_DueDate      OUT evt_sched_dead.sched_dead_dt%TYPE,
            an_ExtDueDate   OUT evt_sched_dead.sched_dead_dt%TYPE,
            an_EndDate      OUT evt_event.event_dt%TYPE,
            ab_HistBool     OUT evt_event.hist_bool%TYPE,
            on_Return       OUT typn_RetCode
   ) IS

   /* complete date */
   CURSOR lcur_old_complete_date (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_sched_dead.sched_dead_dt,
           evt_sched_dead.deviation_qt,
           ref_eng_unit.ref_mult_qt,           
           DECODE( evt_event.hist_bool, 1, evt_event.event_dt, evt_event.sched_end_dt ) AS end_dt,
           evt_event.hist_bool
        FROM
           evt_sched_dead,
           evt_event,
           mim_data_type,
           ref_eng_unit
        WHERE
           evt_event.event_db_id (+)=  cn_EventDbId AND
           evt_event.event_id    (+)=  cn_EventId AND
           evt_event.rstat_cd	(+)= 0 AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
           mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
           AND
           ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
           ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd;
   lrec_old_complete_date lcur_old_complete_date%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* get the deadline, task completion info info */
   OPEN  lcur_old_complete_date(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_old_complete_date INTO lrec_old_complete_date;
   CLOSE lcur_old_complete_date;

   an_DueDate    := lrec_old_complete_date.sched_dead_dt;
   an_ExtDueDate := lrec_old_complete_date.sched_dead_dt + (lrec_old_complete_date.deviation_qt * lrec_old_complete_date.Ref_Mult_Qt);
   an_EndDate    := lrec_old_complete_date.end_dt ;
   ab_HistBool   :=lrec_old_complete_date.hist_bool;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetOldCompleteDate@@@'||SQLERRM);
      RETURN;

END GetOldCompleteDate;

/********************************************************************************
*
* Procedure: GetOldEvtInvUsage
* Arguments:
*           an_EventDbId    (long)  - event primary key
*           an_EventId      (long)  --//--
*           an_DataTypeDbId (long)  - deadline data type primary key
*           an_DataTypeId   (long)  --//--
*
* Return:
*           an_SnapshotTSN (date) - TSN snapshot
*           on_Return       - 1 is success
*
* Description: This procedure returnes due and completion dates.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetOldEvtInvUsage(
            an_EventDbId    IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId      IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_SnapshotTSN OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

  CURSOR lcur_old_evt_inv_usage (
         cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
         cn_EventId        evt_sched_dead.event_id%TYPE,
         cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
         cn_DataTypeId     evt_sched_dead.data_type_id%TYPE

      ) IS
         SELECT
         evt_inv_usage.tsn_qt,
         evt_inv_usage.event_inv_id
      FROM
         evt_inv_usage
      WHERE
         evt_inv_usage.event_db_id     (+)= cn_EventDbId AND
         evt_inv_usage.event_id        (+)= cn_EventId AND
         evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId AND
         evt_inv_usage.data_type_id    (+)= cn_DataTypeId
         ORDER BY  evt_inv_usage.event_inv_id DESC;
         lrec_old_evt_inv_usage lcur_old_evt_inv_usage%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*get snapshot for the event*/
   OPEN  lcur_old_evt_inv_usage(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_old_evt_inv_usage INTO lrec_old_evt_inv_usage;
   CLOSE lcur_old_evt_inv_usage;


   an_SnapshotTSN:= lrec_old_evt_inv_usage.tsn_qt;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetOldEvtInvUsage@@@'||SQLERRM);
      RETURN;

END GetOldEvtInvUsage;

/********************************************************************************
*
* Procedure: GetMainInventoryBirthInfo
* Arguments:
*             an_SchedDbId (long) - task primary key
*             an_SchedId   (long) --//--
*
* Return:
*             ad_ManufactDt   (date) --  manufacturing date of the inventory
*             ad_ReceivedDt   (date) --  received date of the inventory
*             on_Return       - 1 is success
*
* Description: This procedure returnes manufacturing and received dates of the inventory.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventoryBirthInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            ad_ManufactDt   OUT  inv_inv.received_dt%TYPE,
            ad_ReceivedDt OUT inv_inv.received_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

  SELECT inv_inv.received_dt,
                 inv_inv.manufact_dt
          INTO ad_ReceivedDt,
               ad_ManufactDt
          FROM inv_inv,
               evt_inv
          WHERE inv_inv.inv_no_db_id (+) = evt_inv.inv_no_db_id
            AND inv_inv.inv_no_id    (+) = evt_inv.inv_no_id
            AND inv_inv.rstat_cd	 (+) = 0
            AND evt_inv.main_inv_bool    = 1
            AND evt_inv.event_db_id      = an_SchedDbId
            AND evt_inv.event_id         = an_SchedId;

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetMainInventoryBirthInfo@@@'||SQLERRM);
      RETURN;

END GetMainInventoryBirthInfo;

/********************************************************************************
*
* Procedure: AreTasksOnTheSameInventory
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_PrevSchedDbId  (long) - previous task primary key
*            an_PrevSchedId    (long) --//--
* Return:
*             ll_EventsOnSameInv  (long) -- 1 if tasks are on the same inventory, 0 if not.
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returnes 1 if tasks are on the same inventory 0 if they are not.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AreTasksOnTheSameInventory(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_PrevSchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_PrevSchedId      IN  sched_stask.sched_id%TYPE,
            ll_EventsOnSameInv   OUT  NUMBER,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT COUNT(*)
              INTO ll_EventsOnSameInv
              FROM evt_inv    new_evt_inv,
                   evt_inv    previous_evt_inv
             WHERE ( new_evt_inv.event_db_id = an_SchedDbId ) AND
                   ( new_evt_inv.event_id    = an_SchedId ) AND
                   ( new_evt_inv.main_inv_bool = 1 )
                   AND
                   ( previous_evt_inv.event_db_id = an_PrevSchedDbId ) AND
                   ( previous_evt_inv.event_id    = an_PrevSchedId ) AND
                   ( previous_evt_inv.main_inv_bool = 1 )
                   AND
                   ( new_evt_inv.inv_no_db_id = previous_evt_inv.inv_no_db_id ) AND
                   ( new_evt_inv.inv_no_id    = previous_evt_inv.inv_no_id );

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AreTasksOnTheSameInventory@@@'||SQLERRM);
      RETURN;

END AreTasksOnTheSameInventory;


/********************************************************************************
*
* Procedure: GetCurrentInventoryUsage
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_DataTypeDbId   (long) data type primary key
             an_DataTypeId      (long) --//--
* Return:
*             an_TsnQt (float) -- TSN value from the current inventory
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returnes current inventory usage TSN value.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCurrentInventoryUsage(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_TsnQt OUT inv_curr_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS
     xc_DataTypeNotOnInv  EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

     SELECT inv_curr_usage.tsn_qt
                INTO an_TsnQt
                FROM evt_inv,
                     inv_curr_usage
                WHERE ( evt_inv.event_db_id = an_SchedDbId ) AND
                      ( evt_inv.event_id    = an_SchedId )   AND
                      ( evt_inv.main_inv_bool = 1 )
                       AND
                      ( inv_curr_usage.data_type_db_id = an_DataTypeDbId )AND
                      ( inv_curr_usage.data_type_id = an_DataTypeId )    AND
                      ( inv_curr_usage.inv_no_db_id = evt_inv.inv_no_db_id )AND
                      ( inv_curr_usage.inv_no_id    = evt_inv.inv_no_id );

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
               RAISE xc_DataTypeNotOnInv;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetCurrentInventoryUsage@@@'||SQLERRM);
      RETURN;

END GetCurrentInventoryUsage;



/********************************************************************************
*
* Procedure: GetTaskDetails
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
* Return:
*           an_PrevSchedDbId  (long) - previous task primary key
*           an_PrevSchedId    (long) --//--
*           an_TaskDbId       (long) - this task task definition primary key
*           an_TaskId         (long) --//--
*           an_PartNoDbId     (long) - this tasks main inventory part primary key
*           an_PartNoId       (long) --//--
*           an_RelativeBool   (long) - 1 if this task is relative
*           ad_EffectiveDt    (date) - effective date of the task definition
*           on_Return       -   (long) 1 is success
*
* Description: This procedure returnes task details
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetTaskDetails(
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE,
            an_PrevSchedDbId  OUT sched_stask.sched_db_id%TYPE,
            an_PrevSchedId    OUT sched_stask.sched_id%TYPE,
            an_TaskDbId       OUT task_interval.task_db_id%TYPE,
            an_TaskId         OUT task_interval.task_id%TYPE,
            an_HInvNoDbId      OUT task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId         OUT task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     OUT task_interval.part_no_db_id%TYPE,
            an_PartNoId       OUT task_interval.part_no_id%TYPE,
            an_RelativeBool   OUT task_task.relative_bool%TYPE,
            ad_EffectiveDt    OUT task_task.effective_dt%TYPE,
            an_RecurringBool  OUT task_task.recurring_task_bool%TYPE,
            an_SchedFromReceivedDtBool OUT task_task.sched_from_received_dt_bool%TYPE,
            as_TaskClassCd    OUT task_task.task_class_cd%TYPE,
            on_Return         OUT typn_RetCode

   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
         sched_stask.task_db_id,
         sched_stask.task_id,
         DECODE( evt_event_rel.event_db_id, NULL, -1, evt_event_rel.event_db_id ) AS prev_sched_db_id,
         DECODE( evt_event_rel.event_id,    NULL, -1, evt_event_rel.event_id )    AS prev_sched_id,
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         evt_inv.part_no_db_id,
         evt_inv.part_no_id,
         task_task.relative_bool,
         task_task.effective_dt,
         istaskdefnrecurring(sched_stask.task_db_id, sched_stask.task_id) AS recurring_task_bool,
         task_task.sched_from_received_dt_bool,
         task_task.task_class_cd
      INTO
         an_TaskDbId,
         an_TaskId,
         an_PrevSchedDbId,
         an_PrevSchedId,
         an_HInvNoDbId,
         an_HInvNoId,
         an_PartNoDbId,
         an_PartNoId,
         an_RelativeBool,
         ad_EffectiveDt,
         an_RecurringBool,
         an_SchedFromReceivedDtBool,
         as_TaskClassCd
      FROM
         sched_stask,
         evt_event_rel,
         evt_inv,
         evt_event,
         task_task
      WHERE
         sched_stask.sched_db_id = an_SchedDbId AND
         sched_stask.sched_id    = an_SchedId
         AND
         sched_stask.rstat_cd	= 0
         AND
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
         AND
         evt_event_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
         evt_event_rel.rel_event_id    (+)= sched_stask.sched_id    AND
         evt_event_rel.rel_type_cd     (+)= 'DEPT'
         AND
         evt_inv.event_db_id   = sched_stask.sched_db_id AND
         evt_inv.event_id      = sched_stask.sched_id    AND
         evt_inv.main_inv_bool = 1
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id AND
         task_task.task_id    (+)= sched_stask.task_id;

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GetTaskDetails@@@'||SQLERRM);
      RETURN;

END GetTaskDetails;



/********************************************************************************
*
* Procedure:    UpdateDependentDeadlinesTree
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the given task and its children plus forecasted tasks that follow them.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlinesTree(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   /* SQL to retrieve all children of the task */
   CURSOR lcur_TaskTree (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         event_db_id sched_db_id,
         event_id    sched_id
      FROM
         evt_event
      WHERE rstat_cd = 0
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId
      CONNECT BY
         nh_event_db_id = PRIOR event_db_id AND
         nh_event_id    = PRIOR event_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the children of the task*/
   FOR lrec_TasksTree IN lcur_TaskTree( an_StartSchedDbId, an_StartSchedId )
   LOOP

      UpdateDependentDeadlines( lrec_TasksTree.sched_db_id, lrec_TasksTree.sched_id, on_Return );
      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','UpdateDependentDeadlinesTree: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlinesTree;






/********************************************************************************
*
* Procedure:    FindDeadlineStartDate
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
*                an_PrefixedQt   (long) - deadline prefixed qt
*                an_PostfixedQt  (long) - deadline postfix qt
*                ad_StartDate    (date) - the actual start date
*                as_PrevSchedFromCd (string)  - the sched from refterm
*                abSyncWithBaseline (boolean)  - baseline sync mode value
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
* Return:
*                ad_StartDt      (long) - new deadline start dt
*                as_SchedFromCd  (long) - new deadline sched from refterm
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure looks up the start date for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartDate (
      an_TaskDbId         IN task_task.task_db_id%TYPE,
      an_TaskId           IN task_task.task_id%TYPE,
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId    IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId      IN sched_stask.sched_id%TYPE,
      an_RelativeBool     IN task_task.relative_bool%TYPE,
      ad_EffectiveDt      IN task_task.effective_dt%TYPE,
      an_FromRcvdDt       IN task_task.sched_from_received_dt_bool%TYPE,
      an_PrefixedQt       IN task_sched_rule.def_prefixed_qt%TYPE,
      an_PostfixedQt      IN task_sched_rule.def_postfixed_qt%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      as_TaskClassCd      IN task_task.task_class_cd%TYPE,
      ad_StartDt          IN OUT evt_sched_dead.start_dt%TYPE,
      as_SchedFromCd      IN OUT evt_sched_dead.sched_from_cd%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

   /* local variables */

   ln_LastHistoricBool        evt_event.hist_bool%TYPE;

   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;

   ld_BeginWindowDt           evt_sched_dead.sched_dead_dt%TYPE;
   ld_EndWindowDt             evt_sched_dead.sched_dead_dt%TYPE;
   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;

   ln_DueDate    evt_sched_dead.sched_dead_dt%TYPE;
   ln_ExtDueDate evt_sched_dead.sched_dead_dt%TYPE;   
   ln_EndDate    evt_event.event_dt%TYPE;

   ln_FaultDbId   sd_fault.fault_db_id%TYPE;
   ln_FaultId sd_fault.fault_id%TYPE;
   ld_FoundOnDate evt_event.actual_start_dt%TYPE;
   ld_StartDt     evt_sched_dead.start_dt%TYPE;

   lScheduleFrom task_task.resched_from_cd%TYPE;
   lWPStartDate evt_event.actual_start_dt%TYPE;
   lWPEndDate evt_event.event_gdt%TYPE;

BEGIN

   lScheduleFrom := NULL;

   /* if this task is not a first task */
   IF NOT an_PrevSchedDbId =-1 THEN

      /* get previous task actual or expected completion values */
      GetOldCompleteDate(
            an_PrevSchedDbId  ,
            an_PrevSchedId ,
            an_DataTypeDbId,
            an_DataTypeId ,
            ln_DueDate,
            ln_ExtDueDate,            
            ln_EndDate,
            ln_LastHistoricBool,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      /* if last task is historic */
      IF ln_LastHistoricBool=1 THEN

         /** determine if the actual task is scheduled from WPEND or WPSTART */
         IF ( an_TaskDbId is NOT NULL ) THEN

               /** Retrieve the reschedule from */
              SELECT
                  task_task.resched_from_cd
              INTO
                  lScheduleFrom
              FROM
                  task_task
              WHERE
                  task_task.task_db_id = an_TaskDbId AND
                  task_task.task_id    = an_TaskId;


               /** Get the event actual start date and actual end date of the work package of the previous task */
              SELECT
                  wp_event.actual_start_dt,
                  wp_event.event_dt
              INTO
                  lWPStartDate,
                  lWPEndDate
              FROM
                  evt_event,
                  evt_event wp_event
              WHERE
                  evt_event.event_db_id   = an_PrevSchedDbId AND
                  evt_event.event_id     = an_PrevSchedId
                  AND
                  wp_event.event_db_id = evt_event.h_event_db_id AND
                  wp_event.event_id    = evt_event.h_event_id;

          END IF;

          IF (lScheduleFrom = 'WPSTART' ) THEN
             ad_StartDt := lWPStartDate;
             as_SchedFromCd := 'WPSTART';
          ELSIF ( lScheduleFrom = 'WPEND') THEN
             ad_StartDt := lWPEndDate;
            as_SchedFromCd := 'WPEND';
         /* if last task does not have a deadline due date */
         ELSIF (ln_DueDate is NULL) THEN
            /* start date is the previous task completion date */
            ad_StartDt:=ln_EndDate;
            as_SchedFromCd:='LASTEND';
         /* if last task does have a deadline due date */
         ELSE

            /* get information about the data type */
            GetUsageParmInfo( an_DataTypeDbId,
                               an_DataTypeId,
                               ls_DomainTypeCd,
                               ll_RefMultQt,
                               ls_EngUnitCd,
                               ls_DataTypeCd,
                               on_Return );
            IF on_Return < 0 THEN
               RETURN;
            END IF;

              /* calculate the fixed-scheduling completion "window" */
             ld_BeginWindowDt := ln_DueDate -
                                        (an_PrefixedQt * ll_RefMultQt);
             ld_EndWindowDt := ln_DueDate +
                                        (an_PostfixedQt * ll_RefMultQt);

             /* if the actual completion date fell within the scheduled window,
                then use the SCHEDULED completion date */
             IF ( ln_EndDate > ld_BeginWindowDt ) AND
                ( ln_EndDate < ld_EndWindowDt ) THEN
                ad_StartDt := ln_DueDate;
                as_SchedFromCd:='LASTDUE';

             /* if the actual completion date fell outside the scheduled window,
                then use the previous event's actual completion date */
             ELSE
                 ad_StartDt:=ln_EndDate;
                 as_SchedFromCd:='LASTEND';
             END IF;
        END IF;
      /* if the previous task is not historic */
      ELSE
         /* if the the previous task does not have deadline due date */
         IF (ln_DueDate is NULL) THEN
             ad_StartDt:=SYSDATE;
             as_SchedFromCd:='CUSTOM';
         /*if the the previous task has a deadline due date*/
         ELSE
             ad_StartDt := ln_ExtDueDate;
             as_SchedFromCd:='LASTDUE';
         END IF;
      END IF;

   /* if there is no previous task */
   ELSE

      /* check if this task has a corrective fault */
      GetCorrectiveFaultInfo(
            an_SchedDbId,
            an_SchedId,
            ln_FaultDbId,
            ln_FaultId,
            ld_FoundOnDate,
            ld_StartDt,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      /* if there is a corrective fault */
      IF (ln_FaultId IS NOT NULL) THEN
            IF ld_StartDt IS NOT NULL THEN
                ad_StartDt:=ld_StartDt;
            ELSE
                ad_StartDt:=ld_FoundOnDate;
            END IF;
            as_SchedFromCd:='CUSTOM';
      /* if this task is adhoc or REPL or CORR */
      ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
            (as_TaskClassCd = 'CORR' OR as_TaskClassCd = 'REPL') THEN
            ad_StartDt:=SYSDATE;
            as_SchedFromCd:='CUSTOM';
      /* if this task is based on the task definition */
      ELSE
        /* if this task should not be scheduled from birth */
        IF (an_RelativeBool=1) THEN
          /** if we should schedule from date provided */
          IF ad_PreviousCompletionDt IS NOT NULL THEN
             ad_StartDt := ad_PreviousCompletionDt;
             as_SchedFromCd := 'CUSTOM';
          /* if there is effective date for the task definition */
          ELSIF (ad_EffectiveDt IS NOT NULL) THEN
            ad_StartDt:=ad_EffectiveDt;
            as_SchedFromCd:='EFFECTIV';
           /* if there is no effective date for the task definition */
           ELSE
            ad_StartDt:=SYSDATE;
            as_SchedFromCd:='CUSTOM';
           END IF;
        /* if the task should be scheduled from birth*/
        ELSE
           /* get main inventory manufacturing and received date */
          GetMainInventoryBirthInfo(
            an_SchedDbId,
            an_SchedId,
            ld_ManufactDt,
            ld_ReceivedDt,
            on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;
          /* if the task is a create_on_install task, and a completion date of the
             installation task was provided, then use that date */
            IF ad_PreviousCompletionDt IS NOT NULL THEN
               ad_StartDt := ad_PreviousCompletionDt;
               as_SchedFromCd := 'CUSTOM';
            ELSIF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL THEN
               /* schedule from received date */
               ad_StartDt:=ld_ReceivedDt;
               as_SchedFromCd:='BIRTH';
            ELSIF an_FromRcvdDt=0 AND ld_ManufactDt IS NOT NULL THEN
               /* schedule from manufacturer date */
               ad_StartDt:=ld_ManufactDt;
               as_SchedFromCd:='BIRTH';
            ELSE
               ad_StartDt:=SYSDATE;
               as_SchedFromCd:='CUSTOM';
            END IF;
        END IF;
      END IF;
     END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','FindDeadlineStartDate '||SQLERRM);
      RETURN;
END FindDeadlineStartDate;

/********************************************************************************
*
* Procedure: GetHistoricUsageAtDt
* Arguments:
*            ad_TargetDate     (Date) - usage at this date
*            an_DataTypeDbId   (long) data type primary key
*            an_DataTypeId     (long) --//--
*            an_InvNoDbId      (long) inventory primary key
*            an_InvNoId        (long) --//--
* Return:
*             an_TsnQt         (float) - TSN value at the target date
*             on_Return        (long) 1 is success
*
* Description: This procedure returns the TSN value of the specified inventory
*              at the specified target date.
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetHistoricUsageAtDt(
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
          tsn_qt
   INTO
          on_TsnQt
   FROM
       (SELECT
               evt_inv_usage.tsn_qt
        FROM
               evt_inv_usage,
               inv_inv,
               inv_inv component_inv,
               evt_inv,
               evt_event
        WHERE
               inv_inv.inv_no_db_id = an_InvNoDbId AND
               inv_inv.inv_no_id    = an_InvNoId
               AND
               inv_inv.rstat_cd	= 0
               AND
               (
                    -- for SYS or TRK, use assembly to get usage
        (
           inv_inv.inv_class_cd IN ('SYS','TRK' ) AND
           component_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
           component_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
        )
        OR
        -- for ACFT or ASSY, use itself
        (
           inv_inv.inv_class_cd IN ('ACFT', 'ASSY') AND
           component_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
           component_inv.inv_no_id    = inv_inv.inv_no_id
        )
          )
               AND
               evt_inv.inv_no_db_id = component_inv.inv_no_db_id AND
               evt_inv.inv_no_id    = component_inv.inv_no_id
               AND
               evt_inv_usage.event_db_id = evt_inv.event_db_id AND
               evt_inv_usage.event_id    = evt_inv.event_id    AND
               evt_inv_usage.data_type_db_id = an_DataTypeDbId AND
               evt_inv_usage.data_type_id    = an_DataTypeId   AND
               evt_inv_usage.negated_bool    = 0
               AND
               evt_event.event_db_id = evt_inv.event_db_id AND
               evt_event.event_id    = evt_inv.event_id    AND
               evt_event.hist_bool   = 1                   AND
               evt_event.event_dt <= ad_TargetDate         AND
               evt_event.event_type_cd IN ('FL', 'UC', 'UR', 'FG' )

        ORDER BY
               evt_event.event_dt DESC,
               evt_event.creation_dt DESC
       )
   WHERE
        rownum = 1;

   -- Return success
   on_Return := icn_Success;

  EXCEPTION
   WHEN NO_DATA_FOUND THEN
      on_TsnQt := 0;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetHistoricUsageAtDt@@@'||SQLERRM);
   RETURN;

END GetHistoricUsageAtDt;


/********************************************************************************
*
* Procedure:    FindDeadlineStartQt
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
*                an_PrefixedQt   (long) - deadline prefixed qt
*                an_PostfixedQt  (long) - deadline postfix qt
*                an_StartQuantity (float) - the actual start quantity
*                as_PrevSchedFromCd (varchar) - sched from refterm
*                abSyncWithBaseline (boolean) - baseline sync mode value
* Return:
*                ad_StartQt      (long) - new deadline start qt
*                as_SchedFromCd  (long) - new deadline sched from refterm
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure looks up the start qt for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartQt (
      an_TaskDbId      IN task_task.task_db_id%TYPE,
      an_TaskId        IN task_task.task_id%TYPE,
      an_SchedDbId     IN sched_stask.sched_db_id%TYPE,
      an_SchedId       IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId  IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId    IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId   IN sched_stask.sched_id%TYPE,
      an_RelativeBool  IN task_task.relative_bool%TYPE,
      ad_EffectiveDt   IN task_task.effective_dt%TYPE,
      an_FromRcvdDt       IN task_task.sched_from_received_dt_bool%TYPE,
      an_PrefixedQt    IN task_sched_rule.def_prefixed_qt%TYPE,
      an_PostfixedQt   IN task_sched_rule.def_postfixed_qt%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      an_EventsOnSameInv  IN NUMBER,
      as_TaskClassCd      IN task_task.task_class_cd%TYPE,
      an_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
      as_SchedFromCd   IN OUT evt_sched_dead.sched_from_cd%TYPE,
      ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
      on_Return        OUT typn_RetCode
   ) IS


   /* local variables */
   ln_LastHistoricBool        evt_event.hist_bool%TYPE;
   ln_DeadlineTSN             evt_sched_dead.sched_dead_qt%TYPE;
   ln_ExtDeadlineTSN          evt_sched_dead.sched_dead_qt%TYPE;   
   ln_EvtUsageTSN             evt_inv_usage.tsn_qt%TYPE;
   ll_BeginWindowQt           evt_sched_dead.sched_dead_qt%TYPE;
   ll_EndWindowQt             evt_sched_dead.sched_dead_qt%TYPE;
   ln_InvNoDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId                 inv_inv.inv_no_id%TYPE;
   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ln_FaultDbId   sd_fault.fault_db_id%TYPE;
   ln_FaultId sd_fault.fault_id%TYPE;
   ld_FoundOnDate evt_event.actual_start_dt%TYPE;
   ld_StartDt     evt_sched_dead.start_dt%TYPE;
   lScheduleFrom task_task.resched_from_cd%TYPE;

   lIsInstalledOnAircraft number;
   lAircraftDbId number;
   lAircraftId number;
BEGIN
     lScheduleFrom := NULL;

   /* if this task is a first task being create */
   IF NOT an_PrevSchedId =-1 THEN
       /*if this task is not on the same inventory */
       IF an_EventsOnSameInv = 0 THEN

         /* get the current inventory usage */
         GetCurrentInventoryUsage(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              an_StartQt,
              on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         as_SchedFromCd:='CUSTOM';

       /* if the task is on the same inventory as the previous task */
       ELSE

          /*  get the previous task completion usage info */
          GetLastCompletionUsageInfo(
                an_PrevSchedDbId,
                an_PrevSchedId,
                an_DataTypeDbId,
                an_DataTypeId,
                ln_DeadlineTSN,
                ln_ExtDeadlineTSN,                
                ln_EvtUsageTSN,
                ln_LastHistoricBool,
                on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;

          /* if the last task is historic */
          IF (ln_LastHistoricBool=1) THEN

             /** determine if the actual task is scheduled from WPEND or WPSTART */
             IF ( an_TaskDbId is NOT NULL ) THEN

                   /** Retrieve the reschedule from */
                  SELECT
                      task_task.resched_from_cd
                  INTO
                      lScheduleFrom
                  FROM
                      task_task
                  WHERE
                      task_task.task_db_id = an_TaskDbId AND
                      task_task.task_id    = an_TaskId;
             END IF;

             IF (ln_DeadlineTSN IS NOT NULL) THEN
                /* find out the scheduled completion "window" */
                   ll_BeginWindowQt := ln_DeadlineTSN - an_PrefixedQt;
                   ll_EndWindowQt   := ln_DeadlineTSN + an_PostfixedQt;

                   /* if the actual completion usage fell within the scheduled window,
                      then use the scheduled completion quantity rather than the actual
                      completion quantity */
                   IF ( ln_EvtUsageTSN > ll_BeginWindowQt ) AND
                      ( ln_EvtUsageTSN < ll_EndWindowQt ) THEN

                      an_StartQt := ln_DeadlineTSN;

                      IF (lScheduleFrom = 'WPSTART' ) THEN
                         as_SchedFromCd:='WPSTART';
                      ELSIF ( lScheduleFrom = 'WPEND') THEN
                         as_SchedFromCd:='WPEND';   
                      ELSE
                         as_SchedFromCd:='LASTDUE';
                      END IF;
                   /* if it falls outside the window */
                   ELSE
                     an_StartQt := ln_EvtUsageTSN;
                     
                     IF (lScheduleFrom = 'WPSTART' ) THEN
                         as_SchedFromCd:='WPSTART';
                     ELSIF ( lScheduleFrom = 'WPEND') THEN
                         as_SchedFromCd:='WPEND';   
                     ELSE
                         as_SchedFromCd:='LASTEND';
                     END IF;
                   END IF;
             /*if the task does not have deadline TSN*/
             ELSE
                 /* if there is usage snapshot taken*/
                 IF (ln_EvtUsageTSN IS NOT NULL) THEN
                     an_StartQt := ln_EvtUsageTSN;
                     
                     IF (lScheduleFrom = 'WPSTART' ) THEN
                         as_SchedFromCd:='WPSTART';
                     ELSIF ( lScheduleFrom = 'WPEND') THEN
                         as_SchedFromCd:='WPEND';   
                     ELSE
                         as_SchedFromCd:='LASTEND';
                     END IF;
                 ELSE
                   /* if there is no usage snapshot, schedule from CUSTOM */
                   GetCurrentInventoryUsage(
                         an_SchedDbId,
                         an_SchedId,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         an_StartQt,
                         on_Return );
                   IF on_Return < 0 THEN
                      RETURN;
                   END IF;

                 as_SchedFromCd:='CUSTOM';
               END IF;
             END IF;
          /* if the previoud task is not historic */
          ELSE

            /* if the previous tasks has deadline TSN */
            IF (ln_DeadlineTSN IS NOT NULL) THEN
              an_StartQt := ln_ExtDeadlineTSN;
              as_SchedFromCd:='LASTDUE';

            /* if the previous task does not have deadline TSN */
            ELSE
              GetCurrentInventoryUsage(
                    an_SchedDbId,
                    an_SchedId,
                    an_DataTypeDbId,
                    an_DataTypeId,
                    an_StartQt,
                    on_Return );
              IF on_Return < 0 THEN
                 RETURN;
              END IF;

              as_SchedFromCd:='CUSTOM';
            END IF;
         END IF;

       END IF;

    /*if this task is a first task being created */
    ELSE
        /* set ln_InvNoPk for later use */
        EVENT_PKG.GetMainInventory(
              an_SchedDbId,
              an_SchedId,
              ln_InvNoDbId,
              ln_InvNoId,
              on_Return);
        IF on_Return < 0 THEN
              RETURN;
        END IF;

        /* find fault if this task is associated with one */
        GetCorrectiveFaultInfo(
                an_SchedDbId,
                an_SchedId,
                ln_FaultDbId,
                ln_FaultId,
                ld_FoundOnDate,
                ld_StartDt,
                on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        /* if fault is found */
        IF (ln_FaultId IS NOT NULL) THEN
           /* schedule from custom start date if exists a CUSTOM deadline already */
           IF ld_StartDt IS NOT NULL THEN
              ad_StartDt:=ld_StartDt;
              as_SchedFromCd:='CUSTOM';
              /* if the start date is in the past, get the usage at that time */
              IF ld_StartDt< TRUNC(SYSDATE+1) THEN
                   GetHistoricUsageAtDt(
                         ld_StartDt,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         ln_InvNoDbId,
                         ln_InvNoId,
                         an_StartQt,
                         on_Return);
              ELSE
              /* the date is in the future, use the current usage or predict it if installed on aircraft*/
                   GetCurrentInventoryUsage(
                         an_SchedDbId,
                         an_SchedId,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         an_StartQt,
                         on_Return );

                   lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft( ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                   IF lIsInstalledOnAircraft=1 THEN
                           EVENT_PKG.PredictUsageBetweenDt( lAircraftDbId,
                                                            lAircraftId,
                                                            an_DataTypeDbId,
                                                            an_DataTypeId,
                                                            SYSDATE,
                                                            ld_StartDt,
                                                            an_StartQt,
                                                            an_StartQt,
                                                            on_Return);
                   END IF;
              END IF;
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           ELSE
              ad_StartDt:=ld_FoundOnDate;
              as_SchedFromCd:='CUSTOM';

              /* get usage snapshot of this fault */
              GetOldEvtInvUsage( ln_FaultDbId,
                                 ln_FaultId,
                                 an_DataTypeDbId,
                                 an_DataTypeId,
                                 an_StartQt,
                                 on_Return );
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           END IF;

        /* if task is adhoc or REPL or CORR */
        ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
              (as_TaskClassCd = 'CORR' OR as_TaskClassCd = 'REPL') THEN
               GetCurrentInventoryUsage(
                 an_SchedDbId,
                 an_SchedId,
                 an_DataTypeDbId,
                 an_DataTypeId,
                 an_StartQt,
                 on_Return );
               IF on_Return < 0 THEN
                  RETURN;
               END IF;

                as_SchedFromCd:='CUSTOM';
        /* if task is based on task definition */
        ELSE
         /* if the task is scheduled from install date */
          IF ( ad_PreviousCompletionDt IS NOT NULL) THEN
               ad_StartDt:=ad_PreviousCompletionDt;
               as_SchedFromCd:='CUSTOM';
               /* get the usage at the installation snapshot */
               GetHistoricUsageAtDt(
                      ad_PreviousCompletionDt,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      ln_InvNoDbId,
                      ln_InvNoId,
                      an_StartQt,
                      on_Return);

          /* if the task should be scheduled from birth */
          ELSIF (an_RelativeBool = 0 ) THEN

               /* get main inventory manufacturing and received date */
              GetMainInventoryBirthInfo(
                an_SchedDbId,
                an_SchedId,
                ld_ManufactDt,
                ld_ReceivedDt,
                on_Return );
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
              
              IF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL AND ld_ReceivedDt < SYSDATE THEN
                   ad_StartDt:=ld_ReceivedDt;
                   /* get the usage at received date */
                   GetHistoricUsageAtDt(
                          ld_ReceivedDt,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_InvNoDbId,
                          ln_InvNoId,
                          an_StartQt,
                          on_Return);
               ELSE
                   an_StartQt:=0;               
               END IF;
               as_SchedFromCd:='BIRTH';     

          /* if the task should not be scheduled from birth */
          ELSIF (ad_EffectiveDt IS NOT NULL) THEN
                 ad_StartDt:=ad_EffectiveDt;
                 as_SchedFromCd:='EFFECTIV';

                 /* That's where we need to define the TSN value at the specified date */
                 IF ad_EffectiveDt<SYSDATE THEN
                   GetHistoricUsageAtDt(
                      ad_EffectiveDt,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      ln_InvNoDbId,
                      ln_InvNoId,
                      an_StartQt,
                      on_Return);
                 ELSE
                   GetCurrentInventoryUsage(
                      an_SchedDbId,
                      an_SchedId,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      an_StartQt,
                      on_Return );
                    lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                    IF lIsInstalledOnAircraft=1 THEN
                      EVENT_PKG.PredictUsageBetweenDt(
                                                      lAircraftDbId,
                                                      lAircraftId,
                                                      an_DataTypeDbId,
                                                      an_DataTypeId,
                                                      SYSDATE ,
                                                      ad_EffectiveDt,
                                                      an_StartQt,
                                                      an_StartQt,
                                                      on_Return);
                     ELSE
                       GetCurrentInventoryUsage(
                            an_SchedDbId,
                            an_SchedId,
                            an_DataTypeDbId,
                            an_DataTypeId,
                            an_StartQt,
                            on_Return );
                       IF on_Return < 0 THEN
                          RETURN;
                       END IF;
                     END IF;
                  END IF;
                  IF on_Return < 0 THEN
                     RETURN;
                  END IF;
                /* if there is no effective date for the task definition */
          ELSE
               GetCurrentInventoryUsage(
                    an_SchedDbId,
                    an_SchedId,
                    an_DataTypeDbId,
                    an_DataTypeId,
                    an_StartQt,
                    on_Return );
               IF on_Return < 0 THEN
                  RETURN;
               END IF;
               ad_StartDt:=SYSDATE;
               as_SchedFromCd:='CUSTOM';
          END IF;

        END IF;
    END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','FindDeadlineStartQt: '||SQLERRM);
      RETURN;
END FindDeadlineStartQt;


/********************************************************************************
*
* Procedure:    PrepareCalendarDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                           a component that fired the create_on_install
*                                           logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares calednar deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareCalendarDeadline (
      an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
      an_SchedId           IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId  IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId    IN task_task.task_id%TYPE,
      an_DataTypeDbId      IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId        IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      ad_PreviousCompletionDt  IN evt_event.sched_end_gdt%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      ln_HInvNoDbId      task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId        task_ac_rule.inv_no_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;

      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ld_NewDeadlineDt         evt_sched_dead.sched_dead_dt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ld_NewDeadlineDt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ld_EffectiveDt,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the original task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE
      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );

      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;


   /* find the start date of the deadline */
   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      FindDeadlineStartDate(
         ln_TaskDbId,
         ln_TaskId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_RelativeBool,
         ld_EffectiveDt,
         ln_SchedFromReceivedDtBool,
         ln_PrefixedQt,
         ln_PostfixedQt,
         ad_PreviousCompletionDt,
         ls_TaskClassCd,
         ld_StartDt,
         ls_SchedFromCd,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find the new deadline date */
   FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_NewDeadlineDt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
               an_SchedDbId,
               an_SchedId,
               an_DataTypeDbId,
               an_DataTypeId,
               NULL,
               ld_StartDt,
               ls_SchedFromCd,
               NULL,
               ld_NewDeadlineDt,
               ln_IntervalQt,
               ln_NotifyQt,
               ln_DeviationQt,
               ln_PrefixedQt,
               ln_PostfixedQt,
               on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
                 an_SchedDbId,
                 an_SchedId,
                 an_DataTypeDbId,
                 an_DataTypeId,
                 NULL,
                 ld_StartDt,
                 ls_SchedFromCd,
                 ln_IntervalQt,
                 NULL,
                 ld_NewDeadlineDt,
                 ln_NotifyQt,
                 ln_DeviationQt,
                 ln_PrefixedQt,
                 ln_PostfixedQt,
                 on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

       -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
       UPDATE evt_sched_dead 
       SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
       WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
              evt_sched_dead.data_type_id    = an_DataTypeId   AND
              evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
              (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
              IN
              (
               SELECT evt_event_rel.rel_event_db_id,
                      evt_event_rel.rel_event_id
               FROM   evt_event,
                      evt_event_rel
               WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                      evt_event_rel.event_id    = an_SchedId   AND
                      evt_event_rel.rel_type_cd = 'DEPT'
                      AND
                      evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                      evt_event.event_id        = evt_event_rel.rel_event_id    AND
                      evt_event.event_status_cd = 'FORECAST' );       

    END IF;

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareCalendarDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareCalendarDeadline;


/********************************************************************************
*
* Procedure:    PrepareUsageDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares usage deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2005 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareUsageDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ld_EffectiveDt task_task.effective_dt%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ln_NewDeadlineQt         evt_sched_dead.sched_dead_qt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ln_NewDeadlineQt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ld_EffectiveDt,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the previous task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE

      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;

      /* find the start date of the deadline */
   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      /* find deadline start qt */
      FindDeadlineStartQt (
          ln_TaskDbId,
          ln_TaskId,
          an_SchedDbId,
          an_SchedId,
          an_DataTypeDbId,
          an_DataTypeId,
          ln_PrevSchedDbId,
          ln_PrevSchedId,
          ln_RelativeBool,
          ld_EffectiveDt,
          ln_SchedFromReceivedDtBool,
          ln_PrefixedQt,
          ln_PostfixedQt,
          ad_PreviousCompletionDt,
          ln_EventsOnSameInv,
          ls_TaskClassCd,
          ln_StartQt,
          ls_SchedFromCd,
          ld_StartDt,
          on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find deadline qt valur */
   FindUsageDeadlineVariables(
            ln_StartQt,
            ln_IntervalQt,
            ln_NewDeadlineQt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_NewDeadlineQt,
              NULL,
              ln_IntervalQt,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_IntervalQt,
              ln_NewDeadlineQt,
              NULL,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

       -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
       UPDATE evt_sched_dead 
       SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
       WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
              evt_sched_dead.data_type_id    = an_DataTypeId   AND
              evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
              (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
              IN
              (
               SELECT evt_event_rel.rel_event_db_id,
                      evt_event_rel.rel_event_id
               FROM   evt_event,
                      evt_event_rel
               WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                      evt_event_rel.event_id    = an_SchedId   AND
                      evt_event_rel.rel_type_cd = 'DEPT'
                      AND
                      evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                      evt_event.event_id        = evt_event_rel.rel_event_id    AND
                      evt_event.event_status_cd = 'FORECAST' );       

     END IF;

     -- Return success
     on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareUsageDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareUsageDeadline;



/********************************************************************************
*
* Procedure:    PrepareSchedDeadlines
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares all deadlines for a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareSchedDeadlines (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      -- retrieves deadlines
      CURSOR lcur_ActualsDeadlines  IS
      (  SELECT
            evt_sched_dead.data_type_db_id as data_type_db_id,
            evt_sched_dead.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            evt_sched_dead,
            mim_data_type
         WHERE
            evt_sched_dead.event_db_id = an_SchedDbId AND
            evt_sched_dead.event_id    = an_SchedId
            AND
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
      UNION
         SELECT
            task_sched_rule.data_type_db_id as data_type_db_id,
            task_sched_rule.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            sched_stask,
            task_sched_rule,
            mim_data_type
         WHERE
            sched_stask.sched_db_id = an_SchedDbId AND
            sched_stask.sched_id    = an_SchedId
            AND
            sched_stask.rstat_cd	= 0
            AND
            task_sched_rule.task_db_id = sched_stask.task_db_id AND
            task_sched_rule.task_id    = sched_stask.task_id
            AND
            mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
            mim_data_type.data_type_id    = task_sched_rule.data_type_id
      );

BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the baseline, and actual deadlines */
   FOR lrec_ActualsDeadlines IN lcur_ActualsDeadlines
   LOOP

      /* Calendar Deadline */
      IF (lrec_ActualsDeadlines.domain_type_cd='CA') THEN

         -- prepare calendar deadline
         PrepareCalendarDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
            ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

      /* Usage Deadline */
      ELSE
         PrepareUsageDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
             ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;

   END LOOP;

   /* Update the Deadlines for this Task */
   EVENT_PKG.UpdateDeadline( an_SchedDbId, an_SchedId, on_Return );
   IF on_Return < 1 THEN
      RETURN;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','PrepareSchedDeadlines: '||SQLERRM);
      RETURN;
END PrepareSchedDeadlines;

/********************************************************************************
*
* Procedure:    UpdateDependentDeadlines
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the tasks that have been forecasted to follow it.
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlines(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   ln_TaskTaskDbId  task_task.task_db_id%TYPE;
   ln_TaskTaskId    task_task.task_id%TYPE;

   /* SQL to retrieve all of the forecasted tasks starting with this one */
   CURSOR lcur_DepTasks (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         LEVEL,
         rel_event_db_id sched_db_id,
         rel_event_id    sched_id
      FROM
         evt_event_rel
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId AND
         rel_type_cd = 'DEPT'
      CONNECT BY
         event_db_id = PRIOR rel_event_db_id AND
         event_id    = PRIOR rel_event_id AND
         rel_type_cd = 'DEPT'
      ORDER BY 1;

   /* EXCEPTIONS */
   xc_UnkCUSTOMnSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    SELECT
        sched_stask.task_db_id,
        sched_stask.task_id
    INTO
        ln_TaskTaskDbId,
        ln_TaskTaskId
    FROM
        sched_stask
    WHERE
         sched_stask.sched_db_id = an_StartSchedDbId AND
         sched_stask.sched_id    = an_StartSchedId
         AND
         sched_stask.rstat_cd	= 0;

   /* loop dependant tasks, and prepare their deadlines*/
   FOR lrec_DepTasks IN lcur_DepTasks( an_StartSchedDbId, an_StartSchedId )
   LOOP

      PrepareSchedDeadlines( lrec_DepTasks.sched_db_id,
                             lrec_DepTasks.sched_id,
                             ln_TaskTaskDbId,
                             ln_TaskTaskId,
                             false,
                             NULL,
                             on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','UpdateDependentDeadlines: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlines;



/********************************************************************************
*
* Procedure:      PrepareDeadlineForInv
* Arguments:      al_InvNoDbId (IN NUMBER): The inventory to update
*                 al_InvNoId (IN NUMBER): ""
* Description:    Finds all the tasks with dedlines and runs
*                 prepare deadlines on them.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareDeadlineForInv(
      an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
      as_SchedFrom    IN evt_sched_dead.sched_from_cd%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      on_Return        OUT NUMBER
   ) IS

  /* cursor declarations */
   /* return all non-historic events in the inventory tree that have
      scheduled deadlines */
   CURSOR lcur_DeadlineTask (cn_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      			     cn_InvNoId      IN  inv_inv.inv_no_id%TYPE)  
   IS
      SELECT 
      	     evt_event.event_db_id,
             evt_event.event_id
        FROM
             (
             SELECT     inv_inv.inv_no_db_id, inv_inv.inv_no_id
             FROM       inv_inv
             START WITH inv_inv.inv_no_db_id 	= cn_InvNoDbId AND
                        inv_inv.inv_no_id    	= cn_InvNoId
             CONNECT BY	inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
                        inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
             )inv_tree,
             evt_inv,
             evt_event
       WHERE
             evt_inv.inv_no_db_id = inv_tree.inv_no_db_id AND
             evt_inv.inv_no_id    = inv_tree.inv_no_id    AND
             evt_inv.main_inv_bool=1
             AND
             evt_event.event_db_id     = evt_inv.event_db_id AND
             evt_event.event_id        = evt_inv.event_id    AND
             evt_event.event_type_cd   = 'TS'                AND
             evt_event.hist_bool       = 0                   AND
             evt_event.event_status_cd = 'ACTV'              AND
             evt_event.rstat_cd   = 0
             AND EXISTS
             (  SELECT 1
                FROM   evt_sched_dead
                WHERE  evt_sched_dead.event_db_id   = evt_event.event_db_id AND
                       evt_sched_dead.event_id      = evt_event.event_id    AND
                       evt_sched_dead.sched_from_cd = as_SchedFrom);
   lrec_DeadlineTask lcur_DeadlineTask%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all of the tasks which need updating */
   FOR lrec_DeadlineTask IN lcur_DeadlineTask(an_InvNoDbId, an_InvNoId) LOOP

      PrepareSchedDeadlines( lrec_DeadlineTask.event_db_id,
                             lrec_DeadlineTask.event_id,
                             NULL,
                             NULL,
                             abSyncWithBaseline,
                             NULL,
                             on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      /* prepare deadlines forecasted tasks*/
      UpdateDependentDeadlines( lrec_DeadlineTask.event_db_id,
                                lrec_DeadlineTask.event_id,
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
END PrepareDeadlineForInv;


FUNCTION GetScheduleDetails(
            an_TaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_TaskTaskId     IN task_task.task_id%TYPE,
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE
         ) RETURN typrec_ScheduleDetails
IS
   -- Local stask and task definition variables
   ln_PrevSchedDbId       sched_stask.sched_db_id%TYPE;
   ln_PrevSchedId         sched_stask.sched_id%TYPE;
   ln_ActiveTaskTaskDbId  task_interval.task_db_id%TYPE;
   ln_ActiveTaskTaskId    task_interval.task_id%TYPE;
   ln_HInvNoDbId          task_ac_rule.inv_no_db_id%TYPE;
   ln_HInvNoId            task_ac_rule.inv_no_id%TYPE;
   ln_PartNoDbId          task_interval.part_no_db_id%TYPE;
   ln_PartNoId            task_interval.part_no_id%TYPE;
   ln_RelativeBool        task_task.relative_bool%TYPE;
   ld_EffectiveDt         task_task.effective_dt%TYPE;
   ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
   ln_RecurringBool       task_task.recurring_task_bool%TYPE;
   ls_TaskClassCd         task_task.task_class_cd%TYPE;
   ln_Return              typn_RetCode;

   orec_ScheduleDetails  typrec_ScheduleDetails;

BEGIN

   GetTaskDetails(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_ActiveTaskTaskDbId,
         ln_ActiveTaskTaskId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RelativeBool,
         ld_EffectiveDt,
         ln_RecurringBool,
         ln_SchedFromReceivedDtBool,
         ls_TaskClassCd,
         ln_Return );

   IF ln_Return < 0 THEN
      RETURN NULL;
   END IF;

   -- Populate task details
   orec_ScheduleDetails.STaskDbId         := an_SchedDbId;
   orec_ScheduleDetails.STaskId           := an_SchedId;
   orec_ScheduleDetails.FirstInstanceBool := ( ln_PrevSchedDbId = -1 );
   orec_ScheduleDetails.PreviousSTaskDbId := ln_PrevSchedDbId;
   orec_ScheduleDetails.PreviousSTaskID   := ln_PrevSchedId;
   orec_ScheduleDetails.HInvNoDbId        := ln_HInvNoDbId;
   orec_ScheduleDetails.HInvNoId          := ln_HInvNoId;
   orec_ScheduleDetails.PartNoDbId        := ln_PartNoDbId;
   orec_ScheduleDetails.PartNoId          := ln_PartNoId;

   -- Populate task definition details
   -- Because this function is related to forecasting deadlines, the revised task definition
   -- details are used instead of the stask's actual task definition details.
   orec_ScheduleDetails.RevisionTaskTaskDbId  := an_TaskTaskDbId;
   orec_ScheduleDetails.RevisionTaskTaskID    := an_TaskTaskId;
   orec_ScheduleDetails.ActiveTaskTaskDbId    := ln_ActiveTaskTaskDbId;
   orec_ScheduleDetails.ActiveTaskTaskId      := ln_ActiveTaskTaskId;

   SELECT
      task_task.relative_bool,
      task_task.effective_dt,
      istaskdefnrecurring(an_TaskTaskDbId, an_TaskTaskID ),
      task_task.sched_from_received_dt_bool,
      task_task.last_sched_dead_bool,
      task_task.task_class_cd
   INTO
      orec_ScheduleDetails.RelativeBool,
      orec_ScheduleDetails.EffectiveDt,
      orec_ScheduleDetails.RecurringBool,
      orec_ScheduleDetails.SchedFromReceivedDtBool,
      orec_ScheduleDetails.ScheduleToLast,
      orec_ScheduleDetails.TaskClassCd
   FROM
      task_task
   WHERE
      task_task.task_db_id = an_TaskTaskDbId AND
      task_task.task_id    = an_TaskTaskId;


   RETURN orec_ScheduleDetails;

END GetScheduleDetails;



/********************************************************************************
*
* Function:     GetTaskRuleStartDate
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type 
*               an_DataTypeId            -- // -- 
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*               
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartDate(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE,
            an_PrefixedQt            IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt           IN task_sched_rule.def_postfixed_qt%TYPE

         ) RETURN evt_sched_dead.start_dt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   ls_SchedFromCd          evt_sched_dead.sched_from_cd%TYPE;
   ln_Return               typn_RetCode;
   -- Return value
   ld_StartDt              DATE;

BEGIN

   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF (
         lcur_CustomDeadline%FOUND AND
         lrec_CustomDeadline.sched_from_cd = 'CUSTOM' AND
         lrec_CustomDeadline.start_dt IS NOT NULL )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_dt;
      END IF;

      CLOSE lcur_CustomDeadline;
      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartDate (
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.SchedFromReceivedDtBool,
         an_PrefixedQt,
         an_PostfixedQt,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ld_StartDt,                                 -- This sets the startDt
         ls_SchedFromCd,
         ln_Return
      );

   RETURN ld_StartDt;

END GetTaskRuleStartDate;



/********************************************************************************
*
* Function:     GetTaskRuleStartUsage
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type 
*               an_DataTypeId            -- // -- 
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*               
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartUsage(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE,
            an_PrefixedQt            IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt           IN task_sched_rule.def_postfixed_qt%TYPE

         ) RETURN evt_sched_dead.start_qt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   -- For the moment, assume the event inventory matches.
   ln_EventsOnSameInv      NUMBER := 1;

   ls_SchedFromCd          ref_sched_from.sched_from_cd%TYPE;
   ld_StartDt              evt_sched_dead.start_dt%TYPE;
   ln_StartQt              evt_sched_dead.start_qt%TYPE;
   ln_Return               typn_RetCode;


BEGIN
   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF ( lcur_CustomDeadline%FOUND AND
           lrec_CustomDeadline.start_qt IS NOT NULL AND
           lrec_CustomDeadline.start_qt != 0 )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_qt;

      END IF;

      CLOSE lcur_CustomDeadline;

      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartQt(
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.SchedFromReceivedDtBool,
         an_PrefixedQt,
         an_PostfixedQt,
         ld_PreviousCompletionDt,
         ln_EventsOnSameInv,
         arec_Schedule.TaskClassCd,
         ln_StartQt,
         ls_SchedFromCd,
         ld_StartDt,
         ln_Return
      );

   RETURN ln_StartQt;

END GetTaskRuleStartUsage;



/********************************************************************************
*
* Function:      getCustomDeviation
* Arguments:
*                ar_ScheduleDetails   task definition and stask information.
*
*
* Description:   Obtains custom deadline information for a task definition revision, or scheduled task.
*                If a task_deadline_ext has been defined, its deviation is returned.
*                If the scheduled task has an extension, its deviation is returned.
*                Otherwise NULL is returned.
*
* Orig.Coder:    Alexander Nazarian
* Recent Coder:  Alexander Nazarian
* Recent Date:   July 2009
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getCustomDeviation(
      arec_ScheduleDetails    IN   typrec_ScheduleDetails,
      an_DataTypeDbId         IN   mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId           IN   mim_data_type.data_type_id   %TYPE
   )  RETURN task_deadline_ext.deviation_qt%TYPE

IS
   ln_Deviation                NUMBER;
   lb_RecFound                 BOOLEAN;

   --First check if the user has setup an extension using the Extend Deadline page from the View Impact Report page.
   --If there is an extension, use it.
   CURSOR lCur_TaskDeadlineExt
   (
      cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
      cn_SchedId      IN sched_stask.sched_id%TYPE,
      cn_DataTypeDbId IN mim_data_type.data_type_db_id%TYPE,
      cn_DataTypeId   IN mim_data_type.data_type_id%TYPE
   )
   IS
      SELECT
         task_deadline_ext.deviation_qt
      FROM
         task_deadline_ext
      WHERE
         task_deadline_ext.sched_db_id        = cn_SchedDbId AND
         task_deadline_ext.sched_id           = cn_SchedId   AND
         task_deadline_ext.data_type_db_id    = cn_DataTypeDbId AND
         task_deadline_ext.data_type_id       = cn_DataTypeId;

   --Cursor variable for the task deadline extension
   lRec_TaskDeadlineExt lCur_TaskDeadlineExt%ROWTYPE;

   -- TODO: Not sure where to get this from
   ln_EventsOnSameInv         NUMBER;

   ln_DeleteActualDeadline   NUMBER;
   ln_UpdateActualDeadline   NUMBER;
   ln_InsertActualDeadline   NUMBER;

   ls_sched_from_cd          evt_sched_dead.sched_from_cd%TYPE;
   ld_start_dt               evt_sched_dead.start_dt%TYPE;
   ln_start_qt               evt_sched_dead.start_qt%TYPE;

   -- OUT baseline values
   ln_ActiveIntervalQt       task_sched_rule.def_interval_qt%TYPE;
   ln_ActiveNotifyQt         task_sched_rule.def_notify_qt%TYPE;
   ln_ActiveInitialQt        task_sched_rule.def_deviation_qt%TYPE;
   ln_ActiveDeviationQt      task_sched_rule.def_deviation_qt%TYPE;
   ln_ActivePrefixedQt       task_sched_rule.def_prefixed_qt%TYPE;
   ln_ActivePostfixedQt      task_sched_rule.def_postfixed_qt%TYPE;
   ln_ActiveDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

   ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
   ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
   ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
   ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
   ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
   ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
   ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
   ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
   ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

   ln_BaselineIntervalQt     task_sched_rule.def_interval_qt%TYPE;
   ln_BaselineNotifyQt       task_sched_rule.def_notify_qt%TYPE;
   ln_BaselineDeviationQt    task_sched_rule.def_deviation_qt%TYPE;
   ln_BaselinePrefixedQt     task_sched_rule.def_prefixed_qt%TYPE;
   ln_BaselinePostfixedQt    task_sched_rule.def_postfixed_qt%TYPE;

   ln_Return                 typn_RetCode;

BEGIN
   -- First, check for an extension set up by the user using the extend deadline page.
   OPEN lCur_TaskDeadlineExt
   (
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId
   ) ;

   FETCH lCur_TaskDeadlineExt INTO lRec_TaskDeadlineExt;

   lb_RecFound := lCur_TaskDeadlineExt%FOUND;
   CLOSE lCur_TaskDeadlineExt;

   IF ( lb_RecFound AND 
        lrec_TaskDeadlineExt.deviation_qt IS NOT NULL AND 
        lrec_taskDeadlineExt.deviation_qt > 0 )
   THEN
      ln_Deviation := lRec_TaskDeadlineExt.deviation_qt;
      RETURN ln_Deviation;
   END IF;

   /* 
    * No impact report extension has been defined, so check for an extension on the actual stask.
    *
    * Note that this code repeats queries that will be performed by GetValuesAndActionForSynch in the following block of code.
    * This is because GetValuesAndActionForSynch does not return the stask's custom deviation if scheduling rules have not changed.
    * Because of this, we cannot rely on the deviation returned by it, and have to explicitly check the actual deadline first.
    * Ideally, GetValuesAndActionForSynch's behaviour will be updated when we can be sure it will not break baseline synch.
    */

   -- Get the deviation for the active task def.
   GetBaselineDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            arec_ScheduleDetails.PartNoDbId,
            arec_ScheduleDetails.PartNoId,
            arec_ScheduleDetails.HInvNoDbId,
            arec_ScheduleDetails.HInvNoId,
            ln_ActiveIntervalQt,
            ln_ActiveInitialQt,
            ln_ActiveNotifyQt,
            ln_ActiveDeviationQt,
            ln_ActivePrefixedQt,
            ln_ActivePostfixedQt,
            ln_ActiveDeadlineExists,
            ln_Return );

   -- If available, get the actual stask deviation for this data type.
   GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            ln_Return );

   -- If both exist, and the active baseline deviation != stask deviation then a custom deviation has been defined.
   IF ln_ActualDeadlineExists = 1 AND ln_ActiveDeadlineExists = 1 THEN
      IF ln_ActualDeviationQt <> ln_ActiveDeviationQt THEN
         -- Return the stask's custom deviation.
         RETURN ln_ActualDeviationQt;
      END IF;
   END IF;

   /*
    * No custom deviations exist from the impact report, or the actual stask.  Figure out the new baseline's deviation for this data type.
    */
   GetValuesAndActionForSynch(
      arec_ScheduleDetails.ActiveTaskTaskDbId,
      arec_ScheduleDetails.ActiveTaskTaskId,
      arec_ScheduleDetails.RevisionTaskTaskDbId,
      arec_ScheduleDetails.RevisionTaskTaskId,
      arec_ScheduleDetails.PreviousSTaskDbId,
      arec_ScheduleDetails.PreviousSTaskId,
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId,
      arec_ScheduleDetails.HInvNoDbId,
      arec_ScheduleDetails.HInvNoId,
      arec_ScheduleDetails.PartNoDbId,
      arec_ScheduleDetails.PartNoId,
      arec_ScheduleDetails.RecurringBool,
      ln_EventsOnSameInv,
      ln_DeleteActualDeadline,
      ln_UpdateActualDeadline,
      ln_InsertActualDeadline,
      ls_sched_from_cd,
      ld_start_dt,
      ln_start_qt,
      ln_BaselineIntervalQt,
      ln_BaselineNotifyQt,
      ln_BaselineDeviationQt,
      ln_BaselinePrefixedQt,
      ln_BaselinePostfixedQt,

      ln_Return
   );

   IF ( ln_InsertActualDeadline = 1 OR ln_UpdateActualDeadline = 1 ) THEN
      RETURN ln_BaselineDeviationQt;
   ELSE

      -- The revision has not updated scheduling rules, or the rule has been deleted.  Either way, no custom deviation exists.
      RETURN NULL;
   END IF;

END getCustomDeviation;



/********************************************************************************
*
* Function:     getTaskRuleDeadline
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type 
*               an_DataTypeId            -- // -- 
*               as_DomainTypeCd          Code of the data type's domain.
*               as_EngUnitCd             Code of the data type's engineering unit.
*               an_MultiplierQt          Data type's multiplier.
*
* Return:       Deadline information for the task definition's scheduling rule.
*
* Description:  Calculates deadline information for calendar and usage data types.
*               
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getTaskRuleDeadline(
      arec_Schedule    typrec_ScheduleDetails,
      an_DataTypeDbId  mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId    mim_data_type.data_type_id   %TYPE,
      as_DomainTypeCd  mim_data_type.domain_type_cd %TYPE,
      as_EngUnitCd     mim_data_type.eng_unit_cd    %TYPE,
      an_MultiplierQt  ref_eng_unit.ref_mult_qt     %TYPE
   ) RETURN DeadlineRecord
IS

   ln_IntervalQt        task_sched_rule.def_interval_qt%TYPE;
   ln_InitialQt         task_sched_rule.def_initial_qt%TYPE;
   ln_NotifyQt          task_sched_rule.def_notify_qt%TYPE;
   ln_DeviationQt       task_sched_rule.def_deviation_qt%TYPE;
   ln_PrefixedQt        task_sched_rule.def_prefixed_qt%TYPE;
   ln_PostfixedQt       task_sched_rule.def_postfixed_qt%TYPE;
   ln_DeadlineExists    task_sched_rule.def_postfixed_qt%TYPE;   
   ln_Return            typn_RetCode;

   ln_CustomDeviationQt task_sched_rule.def_deviation_qt%TYPE;

   ln_CurrentUsage      inv_curr_usage.tsn_qt%TYPE;
   ln_UsageRemaining    inv_curr_usage.tsn_qt%TYPE;
   ld_StartDt           evt_sched_dead.start_dt%TYPE;
   ln_StartQt           evt_sched_dead.start_qt%TYPE;
   ld_DeadlineDt        evt_sched_dead.start_dt%TYPE;

   lrec_Deadline        DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);
   
BEGIN
   lrec_Deadline.DeadlineDt := SYSDATE;
   
   -- Get the most relevant scheduling rule and interval information for the task definition revision + data type.

   GetBaselineDeadline(
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.RevisionTaskTaskDbId,
         arec_Schedule.RevisionTaskTaskId,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         arec_Schedule.PartNoDbId,
         arec_Schedule.PartNoId,
         arec_Schedule.HInvNoDbId,
         arec_Schedule.HInvNoId,
         ln_IntervalQt,
         ln_InitialQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         ln_DeadlineExists,
         ln_Return
      );

   IF ( ln_Return < 0 ) THEN
      -- No deadline information found.  This should never happen.
      RETURN lrec_Deadline;
   END IF;

   ln_CustomDeviationQt := getCustomDeviation( arec_Schedule, an_DataTypeDbId, an_DataTypeId );
   
   IF ( ln_CustomDeviationQt IS NOT NULL AND ln_customDeviationQt > 0 ) THEN
      -- If a custom extension has been defined, override the scheduling rule deviation.
      ln_DeviationQt := ln_CustomDeviationQt;
   END IF;


   IF ( arec_Schedule.FirstInstanceBool AND ln_InitialQt IS NOT NULL ) THEN
      -- Use the rule's initial interval for its first stask instance.
      ln_IntervalQt := ln_InitialQt;
   END IF;

   -- Get the deadline start date or usage
   IF ( as_DomainTypeCd = 'CA' ) THEN -- Calendar
      
      ld_StartDt := GetTaskRuleStartDate(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_PrefixedQt,
            ln_PostfixedQt
         );

      -- Calculate the new deadline date.
      FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_DeadlineDt,
            ln_Return
         );

      -- Assign default value for usage remaining - clients should ignore this for calendar deadlines.
      lrec_Deadline.UsageRemainingQt := 0;

   ELSE -- Usage

      -- Get the start usage for this scheduling rule.  
      -- This obtains custom start usage defined for the task def revision, or an actual extension assigned to the stask.
      -- If no extensions are found, the usage is calculated using FindDeadlineStartQt.
      ln_StartQt := GetTaskRuleStartUsage(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_PrefixedQt,
            ln_PostfixedQt
         );

      ld_StartDt := SYSDATE;

      -- Get the main inventory's current usage.
      GetCurrentInventoryUsage(
            arec_Schedule.STaskDbId,
            arec_Schedule.STaskId,
            an_DataTypeDbId, 
            an_DataTypeId,
            ln_CurrentUsage, 
            ln_Return
         );

      ln_UsageRemaining := (ln_StartQt + ln_IntervalQt ) - ln_CurrentUsage;
      
      -- Calculate the usage remaining.
      EVENT_PKG.findForecastedDeadDt(
            arec_Schedule.HInvNoDbId,
            arec_Schedule.HInvNoId,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_UsageRemaining + ln_DeviationQt,
            ld_StartDt,
            ld_DeadlineDt, -- assigned a value by this procedure call.
            ln_Return
         );
      IF ( ln_UsageRemaining IS NULL ) THEN
         ln_UsageRemaining := 0;
      END IF;


      lrec_Deadline.StartUsageQt     := ln_StartQt;
      lrec_Deadline.CurrentUsageQt   := ln_CurrentUsage;
      lrec_Deadline.UsageRemainingQt := ln_UsageRemaining;
   END IF;

   lrec_Deadline.DataTypeDbId := an_DataTypeDbId;
   lrec_Deadline.DataTypeId   := an_DataTypeId;
   lrec_Deadline.DomainTypeCd := as_DomainTypeCd;
   lrec_Deadline.EngUnitCd    := as_EngUnitCd;
   lrec_Deadline.DeviationQt  := ln_DeviationQt;
   lrec_Deadline.DeadlineDt   := ld_DeadlineDt;
   lrec_Deadline.MultiplierQt := an_MultiplierQt;
   lrec_Deadline.IntervalQt   := ln_IntervalQt;
   
   -- determine the extended deadline date
   lrec_Deadline.ExtendedDeadlineDt :=
      getExtendedDeadlineDt(
         lrec_Deadline.DeviationQt,
         lrec_Deadline.DeadlineDt,
         lrec_Deadline.DomainTypeCd,
         lrec_Deadline.MultiplierQt
      );

   RETURN lrec_Deadline;

END getTaskRuleDeadline;



/********************************************************************************
*
* Function:     getForecastedDrivingDueDate
* Arguments:
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // -- 
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // -- 
*               an_AircraftInvNoDbId      IN inv_inv.inv_no_db_id%TYPE,
*               an_AircraftInvNoId        IN inv_inv.inv_no_id%TYPE
*
* Return:       A string with * delimited deadline information.
*
* Description:  Obtains the driving deadline due date for a revised task definition.
*               All scheduling rules for the new revision have their deadline/extended deadline calculated.
*               The deadline returned will be the earliest, or latest depending on the 
*               revised requirement's last_sched_dead_bool value.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetForecastedDrivingDueDate(
            an_STaskDbId              IN sched_stask.sched_db_id%TYPE,
            an_STaskId                IN sched_stask.sched_id%TYPE,
            an_ActiveTaskTaskDbId     IN task_task.task_db_id%TYPE,
            an_ActiveTaskTaskId       IN task_task.task_id%TYPE,
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE

   ) RETURN VARCHAR2
IS

   lrec_Schedule    typrec_ScheduleDetails;
   lrec_CurrentDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);
   lrec_DrivingDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);
   
   
   -- Get the new baseline interval and deviation for the REV requirement
   CURSOR lcur_NewBaselines (
      crec_Schedule typrec_ScheduleDetails
   ) IS
      SELECT
         task_sched_rule.def_interval_qt,
         task_sched_rule.def_deviation_qt,
         mim_data_type.data_type_db_id,
         mim_data_type.data_type_id,
         mim_data_type.data_type_cd,
         mim_data_type.domain_type_cd,
         mim_data_type.eng_unit_cd
      FROM
         task_sched_rule,
         mim_data_type
      WHERE
         task_sched_rule.task_db_id    = an_RevisionTaskTaskDbId AND
         task_sched_rule.task_id       = an_RevisionTaskTaskId
         AND
         mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
         mim_data_type.data_type_id    = task_sched_rule.data_type_id;

   lrec_NewBaseline           lcur_NewBaselines%ROWTYPE;
   ld_DrivingExtDeadlineDate  DATE;
   ln_Return                  typn_RetCode;
   
   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;
   
BEGIN

   lrec_Schedule := GetScheduleDetails( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId, an_STaskDbId, an_STaskId );
   lrec_DrivingDeadline.DomainTypeCd := NULL;

   FOR lrec_NewBaseline IN lcur_NewBaselines( lrec_Schedule ) LOOP

      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return   );
      IF ln_Return < 0 THEN
        RETURN NULL;
      END IF;

      lrec_CurrentDeadline := getTaskRuleDeadline(
            lrec_Schedule,
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      IF ( lrec_DrivingDeadline.DomainTypeCd IS NULL OR
         ( lrec_Schedule.ScheduleToLast = 1 AND lrec_CurrentDeadline.ExtendedDeadlineDt > lrec_DrivingDeadline.ExtendedDeadlineDt ) OR
         ( lrec_Schedule.ScheduleToLast = 0 AND lrec_CurrentDeadline.ExtendedDeadlineDt < lrec_DrivingDeadline.ExtendedDeadlineDt ) 
         )
      THEN
         lrec_DrivingDeadline := lrec_CurrentDeadline;
      END IF;
   END LOOP;

   IF ( lrec_DrivingDeadline.DataTypeDbId = -1 ) THEN
      -- No deadlines found.  Return empty data.
      RETURN '******';
   END IF;
   
   RETURN lrec_DrivingDeadline.DomainTypeCd || '*' || lrec_DrivingDeadline.UsageRemainingQt || '*' || lrec_DrivingDeadline.EngUnitCd || '*' ||
          lrec_DrivingDeadline.MultiplierQt || '*' || TO_CHAR( lrec_DrivingDeadline.DeadlineDt, 'DD-MON-YYYY' ) || ' 23:59 *' || lrec_DrivingDeadline.DeviationQt || '*' ||
          lrec_DrivingDeadline.ExtendedDeadlineDt || ' 23:59';

END GetForecastedDrivingDueDate;



/********************************************************************************
*
* Function:     GetTaskDefnRules
* Arguments:
*               an_TaskTaskDbId    Task definition primary key
*               an_TaskTaskId      -- // --
*               an_STaskDbId       Active stask primary key
*               an_STaskId         -- // -- 
*
* Return:       A table of scheduling rule information for the task definition.
*               Note that *only* "basic" rule information is returned - i.e. 
*               tail, part, and measurement specific information is not included.
*
* Description:  Obtains all basic scheduling rule information for a task definition.
*               Each data type has exactly one row of rule information returned.  
*               Because this is a helper function, part, tail, and measurement 
*               information is not returned.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDefnRules(
            an_TaskTaskDbId     IN task_task.task_db_id   %TYPE,
            an_TaskTaskId       IN task_task.task_id      %TYPE,
            an_STaskDbId        IN sched_stask.sched_db_id%TYPE,
            an_STaskId          IN sched_stask.sched_id   %TYPE
         )
         RETURN typtabrec_SchedulingRuleTable 
IS

   -- Cursor that gets basic scheduling rules for the task.  
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id   %TYPE,
            cn_TaskId      task_interval.task_id      %TYPE
         ) 
      IS
         SELECT
            task_sched_rule.data_type_db_id    data_type_db_id,
            task_sched_rule.data_type_id       data_type_id,
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId       
      ;

   lrec_BaselineDeadline   lcur_BaselineDeadlines%ROWTYPE;
      
   ltab_Rules              typtabrec_SchedulingRuleTable;
   
   ln_Index                NUMBER := 0;
   
BEGIN
   FOR lrec_BaselineDeadline IN lcur_BaselineDeadlines(
         an_TaskTaskDbId,
         an_TaskTaskId
      )
   LOOP
      ltab_Rules( ln_Index ).DataTypeDbId := lrec_BaselineDeadline.data_type_db_id;
      ltab_Rules( ln_Index ).DataTypeId   := lrec_BaselineDeadline.data_type_id;
      ltab_Rules( ln_index ).IntervalQt  := lrec_BaselineDeadline.interval_qt;
      ltab_Rules( ln_index ).InitialQt   := lrec_BaselineDeadline.initial_qt;
      ltab_Rules( ln_index ).DeviationQt := lrec_BaselineDeadline.deviation_qt;
      ltab_Rules( ln_index ).PrefixedQt  := lrec_BaselineDeadline.prefixed_qt;
      ltab_Rules( ln_index ).PostfixedQt := lrec_BaselineDeadline.postfixed_qt;

      ln_Index := ln_Index + 1;
   END LOOP;
   
   RETURN ltab_Rules;
   
END GetTaskDefnRules;


/********************************************************************************
*
* Function:     isTaskDefnSchedulingChanged
* Arguments:
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // -- 
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // -- 
*
* Return:       0 if task definition information and scheduling rules are identical.
*               1 if the task definition has changed (recurring, schedule from, schedule to, etc.)
                1 if any scheduling rules have been added/removed/updated.
*
* Description:  Checks for differences between two revisions of a task definition.
*               Examines the basic task def information (recurring, schedule from, etc) 
*               and checks all scheduling rules for differences.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isTaskDefnSchedulingChanged(
            an_ActiveTaskTaskDbId    task_task.task_db_id   %TYPE,
            an_ActiveTaskTaskId      task_task.task_id      %TYPE,
            an_RevisionTaskTaskDbId  task_task.task_db_id   %TYPE,
            an_RevisionTaskTaskId    task_task.task_id      %TYPE,
            an_STaskDbId             sched_stask.sched_db_id%TYPE,
            an_STaskId               sched_stask.sched_id   %TYPE            
   ) RETURN NUMBER 
IS

   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task defs.
   ltabrec_Rules         typtabrec_SchedulingRuleTable;
   ltabrec_RevisedRules  typtabrec_SchedulingRuleTable;
   
   -- Loop variable.
   lrec_Rule          typrec_SchedulingRule;

   ln_BaselineDeadlineChanged  INT;
   ln_Return                   INT;
   
BEGIN

   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_ActiveTaskTaskDbId, an_ActiveTaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules        := GetTaskDefnRules( an_ActiveTaskTaskDbId,   an_ActiveTaskTaskId,   an_STaskDbId, an_STaskId );
   ltabrec_RevisedRules := GetTaskDefnRules( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId, an_STaskDbId, an_STaskId );
   
   IF ( ltabrec_Rules.COUNT != ltabrec_RevisedRules.COUNT ) THEN
      -- There's a mismatch in rule counts.  Something must have changed.
      RETURN 1;
   END IF;
   
   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );
    
      BaselineDeadlinesChanged(
            an_RevisionTaskTaskDbId,
            an_RevisionTaskTaskId,
            an_ActiveTaskTaskDbId,
            an_ActiveTaskTaskId,
            lrec_Rule.DataTypeDbId,
            lrec_Rule.DataTypeId,
            lrec_Schedule.HInvNoDbId,
            lrec_Schedule.HInvNoId,
            lrec_Schedule.PartNoDbId,
            lrec_Schedule.PartNoId,
            ln_BaselineDeadlineChanged,
            ln_Return
         );
         
      IF ( ln_BaselineDeadlineChanged > 0 ) THEN
         -- A rule has changed.  We can stop checking.
         RETURN 1;
      END IF;
   END LOOP;
   
   -- No differences found.
   RETURN 0;
        
END isTaskDefnSchedulingChanged;


/********************************************************************************
*
* Function:     GetTaskDeadlines
* Arguments:
*               an_TaskTaskDbId   Task definition primary key
*               an_TaskTaskId     -- // --
*               an_STaskDbId      Stask primary key
*               an_STaskId        -- // -- 
*
* Return:       Deadline information for every scheduling rule associated with the task definition.
*
* Description:  Returns one row for each scheduling rule in a task.
*               Part, tail, and measurement information will overwrite basic rule values.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDeadlines(
            an_TaskTaskDbId   IN task_task.task_db_id   %TYPE,
            an_TaskTaskId     IN task_task.task_id      %TYPE,
            an_STaskDbId      IN sched_stask.sched_db_id%TYPE,
            an_STaskId        IN sched_stask.sched_id   %TYPE

   ) RETURN DeadlineTable PIPELINED

IS 
   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task def.
   ltabrec_Rules      typtabrec_SchedulingRuleTable;
   
   -- Used in the loop to store a scheduling rule's data type information.
   ls_DomainTypeCd    mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt       ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd       mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd      mim_data_type.data_type_cd%TYPE;

   -- Used in the loop to temporarily store scheduling rule and deadline data.
   lrec_Rule          typrec_SchedulingRule;
   lrec_DeadlineInfo  DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);

   ln_Return          typn_RetCode;

BEGIN
   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_TaskTaskDbId, an_TaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules := GetTaskDefnRules( an_TaskTaskDbId, an_TaskTaskId, an_STaskDbId, an_STaskId );
   
   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );
      
      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return   
         );

      -- Get the deadline information for the current data type.
      lrec_DeadlineInfo := getTaskRuleDeadline( 
            lrec_Schedule, 
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId, 
            ls_DomainTypeCd, 
            ls_EngUnitCd, 
            ll_RefMultQt 
         );

      -- Output the deadline information.
      PIPE ROW( lrec_DeadlineInfo );
   END LOOP;

END GetTaskDeadlines;


END PREP_DEADLINE_PKG;
/

--changeSet MX-18509:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PREP_DEADLINE_PKG

IS

/********************************************************************************
*
* Package:     PREP_DEADLINE_PKG
* Description: This package is used to prepare scheduling info for a task, calendar and usage deadlines. 
*
* Orig.Coder:   Michal Baje
* Recent Coder: N/A
* Recent Date:  August 24, 2004
*
*********************************************************************************
*
* Copyright 1998-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

/*------------------------------------ SUBTYPES ----------------------------*/
-- Define a subtype for return codes
SUBTYPE typn_RetCode IS NUMBER;

/*---------------------------------- Constants -----------------------------*/

-- Basic error handling codes
icn_Success CONSTANT typn_RetCode := 1;       -- Success
icn_NoProc  CONSTANT typn_RetCode := 0;       -- No processing done
icn_Error   CONSTANT typn_RetCode := -1;      -- Error

-- return codes for the GenSchedTask procedure
icn_ApplicabilityInvalid   CONSTANT typn_RetCode := -11;
icn_InvHasIncorrectPartNo  CONSTANT typn_RetCode := -12;
icn_TaskDefnNotActive      CONSTANT typn_RetCode := -13;
icn_InvIsLocked            CONSTANT typn_RetCode := -15;

-- Sub procedure validation TRUE, FALSE
icn_True    CONSTANT typn_RetCode := 1;  -- True
icn_False   CONSTANT typn_RetCode := 0;  -- False

/*---------------------------------- Procedures -----------------------------*/



PROCEDURE PrepareUsageDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,      
      on_Return           OUT typn_RetCode
   );


PROCEDURE   FindUsageDeadlineVariables(
            ad_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineTSN IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT typn_RetCode
   );

PROCEDURE FindCalendarDeadlineVariables(
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDt       IN OUT evt_sched_dead.start_dt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_dt%TYPE,
            on_Return        OUT typn_RetCode
   );

PROCEDURE GetCurrentInventoryUsage(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_TsnQt OUT inv_curr_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   );


PROCEDURE UpdateDependentDeadlines(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   );

PROCEDURE GetHistoricUsageAtDt(
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   );
   
PROCEDURE PrepareDeadlineForInv(
      an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
      as_SchedFrom    IN evt_sched_dead.sched_from_cd%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      on_Return         OUT NUMBER
   );

PROCEDURE PrepareSchedDeadlines (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,            
      on_Return           OUT typn_RetCode
   );   
PROCEDURE PrepareCalendarDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,      
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,            
      on_Return           OUT typn_RetCode
   ); 


PROCEDURE UpdateDependentDeadlinesTree(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode );


FUNCTION GetForecastedDrivingDueDate(
            an_STaskDbId            IN sched_stask.sched_db_id%TYPE,
            an_STaskId              IN sched_stask.sched_id%TYPE,
            an_ActiveTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_ActiveTaskTaskId     IN task_task.task_id%TYPE,
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE
                        
   ) RETURN VARCHAR2;


FUNCTION isTaskDefnSchedulingChanged(
            an_ActiveTaskTaskDbId    task_task.task_db_id   %TYPE,
            an_ActiveTaskTaskId      task_task.task_id      %TYPE,
            an_RevisionTaskTaskDbId  task_task.task_db_id   %TYPE,
            an_RevisionTaskTaskId    task_task.task_id      %TYPE,
            an_STaskDbId             sched_stask.sched_db_id%TYPE,
            an_STaskId               sched_stask.sched_id   %TYPE            
   ) RETURN NUMBER ;


FUNCTION GetTaskDeadlines(
            an_TaskTaskDbId   IN task_task.task_db_id   %TYPE,
            an_TaskTaskId     IN task_task.task_id      %TYPE,
            an_STaskDbId      IN sched_stask.sched_db_id%TYPE,
            an_STaskId        IN sched_stask.sched_id   %TYPE

   ) RETURN DeadlineTable PIPELINED;

END PREP_DEADLINE_PKG;
/

--changeSet MX-18509:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE

   CURSOR lcur_Inv IS
          -- all inventory that are loose and not aircrafts with only US deadlines.
          SELECT DISTINCT
                 h_inv_inv.inv_no_db_id,
                 h_inv_inv.inv_no_id
          FROM   sched_stask,
                 inv_inv,
                 inv_inv h_inv_inv,
                 evt_event,
                 evt_sched_dead,
                 ref_inv_class
          WHERE  evt_sched_dead.event_db_id       = evt_event.event_db_id AND
                 evt_sched_dead.event_id          = evt_event.event_id    AND
                 evt_sched_dead.sched_driver_bool = 1
                 AND
                 evt_event.hist_bool       = 0          AND
                 evt_event.rstat_cd        = 0          AND
                 NOT ( evt_event.event_status_db_id = 0 AND
                       evt_event.event_status_cd    = 'FORECAST' )
                 AND
                 sched_stask.sched_db_id = evt_event.event_db_id AND
                 sched_stask.sched_id    = evt_event.event_id
                 AND
                 inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
                 inv_inv.inv_no_id    = sched_stask.main_inv_no_id    AND
                 inv_inv.rstat_cd     = 0
                 AND
                 -- where the highest inventory is not locked and is a loose component
                 h_inv_inv.inv_no_db_id   = inv_inv.h_inv_no_db_id AND
                 h_inv_inv.inv_no_id      = inv_inv.h_inv_no_id    AND
                 h_inv_inv.locked_bool    = 0                      AND
                 h_inv_inv.not_found_bool = 0                      AND
                 NOT ( h_inv_inv.inv_class_db_id = ref_inv_class.inv_class_db_id AND
                       h_inv_inv.inv_class_cd    = ref_inv_class.inv_class_cd )
                 AND
                 ref_inv_class.inv_class_db_id = 0 AND
                 ref_inv_class.inv_class_cd    = 'ACFT'          
          MINUS
          -- remove all inventory that have CA deadline on them
          SELECT h_inv_inv.inv_no_db_id,
                 h_inv_inv.inv_no_id
          FROM   sched_stask,
                 inv_inv,
                 inv_inv h_inv_inv,
                 evt_sched_dead,
                 evt_event,
                 ref_inv_class,
                 mim_data_type
          WHERE
                 evt_sched_dead.event_db_id     = evt_event.event_db_id AND
                 evt_sched_dead.event_id        = evt_event.event_id    AND
                 evt_sched_dead.data_type_db_id = mim_data_type.data_type_db_id AND
                 evt_sched_dead.data_type_id    = mim_data_type.data_type_id
                 AND
                 -- calendar deadlines
                 mim_data_type.domain_type_db_id = 0 AND
                 mim_data_type.domain_type_cd    = 'CA'
                 AND
                 evt_event.event_db_id     = evt_sched_dead.event_db_id AND
                 evt_event.event_id        = evt_sched_dead.event_id    AND
                 evt_event.hist_bool       = 0                          AND
                 evt_event.rstat_cd        = 0                          AND
                 NOT ( evt_event.event_status_db_id = 0 AND
                       evt_event.event_status_cd    = 'FORECAST' )
                 AND
                 sched_stask.sched_db_id = evt_event.event_db_id AND
                 sched_stask.sched_id    = evt_event.event_id
                 AND
                 inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
                 inv_inv.inv_no_id    = sched_stask.main_inv_no_id    AND
                 inv_inv.rstat_cd     = 0
                 AND
                 -- where the highest inventory is not locked and is a loose component
                 h_inv_inv.inv_no_db_id   = inv_inv.h_inv_no_db_id AND
                 h_inv_inv.inv_no_id      = inv_inv.h_inv_no_id    AND
                 h_inv_inv.locked_bool    = 0                      AND
                 h_inv_inv.not_found_bool = 0                      AND
                 NOT ( h_inv_inv.inv_class_db_id = ref_inv_class.inv_class_db_id AND
                       h_inv_inv.inv_class_cd    = ref_inv_class.inv_class_cd )
                 AND
                 ref_inv_class.inv_class_db_id = 0 AND
                 ref_inv_class.inv_class_cd    = 'ACFT';   
    lrec_Inv lcur_Inv%ROWTYPE;
    on_Return NUMBER;
BEGIN
     on_Return := 0;
     FOR lrec_Inv IN lcur_Inv LOOP
         event_pkg.UpdateDeadlineForInvTree( lrec_Inv.Inv_No_Db_Id, lrec_Inv.Inv_No_Id, on_Return);
         -- if an error occurs, output and move on.
         IF on_Return <> event_pkg.icn_Success  THEN
            dbms_output.put_line ('MX-18509 migration error for inventory '|| lrec_Inv.Inv_No_Db_Id || ':' || lrec_Inv.Inv_No_Id || '; MSG:' || application_object_pkg.GetMxiError());
         END IF;
     END LOOP;   
END;
/