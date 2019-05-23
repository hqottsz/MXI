--liquibase formatted sql


--changeSet MX-19795:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
         event_db_id,
         event_id,
         data_type_db_id,
         data_type_id,
         ext_usage_rem_qt,
         DECODE ( NVL(last_sched_dead_bool, 0),
                  0,
                 (extended_dt - SYSDATE ),
                  -1 * (extended_dt - SYSDATE)
            ) AS sort_column
      FROM
        (  SELECT
             evt_event.event_db_id,
             evt_event.event_id,
             evt_sched_dead.data_type_db_id,
             evt_sched_dead.data_type_id,
             CASE WHEN mim_data_type.domain_type_cd = 'CA'
                  THEN evt_sched_dead.usage_rem_qt + (evt_sched_dead.deviation_qt/ref_eng_unit.ref_mult_qt)
                  ELSE evt_sched_dead.usage_rem_qt +  evt_sched_dead.deviation_qt
              END AS ext_usage_rem_qt,
             task_task.last_sched_dead_bool,
             getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                    evt_sched_dead.sched_dead_dt,
                                    mim_data_type.domain_type_cd,
                                    ref_eng_unit.ref_mult_qt
             ) AS extended_dt
           FROM
             ref_eng_unit,
             mim_data_type,
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
             AND
             mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
             mim_data_type.data_type_id    = evt_sched_dead.data_type_id
             AND
             ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
             ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
         )
      ORDER BY
         sort_column ASC,
         data_type_db_id,
         data_type_id,
         ext_usage_rem_qt;

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
   ld_ExtendedDt     evt_sched_dead.sched_dead_dt%TYPE;

BEGIN

    on_Return := 0;

     ls_Query :=
     'SELECT                                                                           ' ||
     '       evt_sched_dead.data_type_db_id,                                           ' ||
     '       evt_sched_dead.data_type_id,                                              ' ||
     '       CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt ' ||
     '            THEN 0                                                               ' ||
     '            ELSE 1                                                               ' ||
     '       END AS is_positive,                                                       ' ||
     '       getExtendedDeadlineDt( evt_sched_dead.deviation_qt,                       ' ||
     '                              evt_sched_dead.sched_dead_dt,                      ' ||
     '                              mim_data_type.domain_type_cd,                      ' ||
     '                              ref_eng_unit.ref_mult_qt                           ' ||
     '       ) AS extended_dt                                                          ' ||
     'FROM                                                                             ' ||
     '       evt_sched_dead,                                                           ' ||
     '       mim_data_type,                                                            ' ||
     '       ref_eng_unit                                                              ' ||
     'WHERE                                                                            ' ||
     '       evt_sched_dead.event_db_id   = ' ||  aEventDbId || ' AND                  ' ||
     '       evt_sched_dead.event_id      = ' ||  aEventId   || '                      ' ||
     '       AND                                                                       ' ||
     '       mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND        ' ||
     '       mim_data_type.data_type_id    = evt_sched_dead.data_type_id               ' ||
     '       AND                                                                       ' ||
     '       ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND            ' ||
     '       ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd                   ';


   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
     IF aLastSchedBool = 1 THEN
        ls_Query := ls_Query || ' ORDER BY extended_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC';
     ELSE
        ls_Query := ls_Query || ' ORDER BY is_positive, extended_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC';
     END IF;

     OPEN lc_Cursor FOR ls_Query ;
     FETCH lc_Cursor INTO ln_DataTypeDbId ,
                          ln_DataTypeId   ,
                          lb_IsPositive   ,
                          ld_ExtendedDt   ;
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

            /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
            IF NOT UPPER(lrec_EventDead.data_type_cd) = 'CHR' THEN
               ldt_ScheduledDeadlineDt := TO_DATE(TO_CHAR(ldt_ScheduledDeadlineDt, 'DD-MON-YYYY ')||'23:59:59',
               'DD-MON-YYYY HH24:MI:SS');
            END IF;

            IF lrec_EventDead.eng_unit_cd = 'MONTH' THEN
               -- get the number of months between now (rounded to the nearest hour) and the deadline
               lf_UsageRemQt := MONTHS_BETWEEN(ldt_ScheduledDeadlineDt, ROUND(SYSDATE,'HH'));
            ELSE
               /* get the various usage remaining values */
               lf_UsageRemQt := (ldt_ScheduledDeadlineDt - SYSDATE) / lrec_EventDead.ref_mult_qt;
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
                lHInvCondCd   NOT IN ( 'QUAR', 'CONDEMN', 'REPREQ', 'INREP' ) THEN

                UPDATE
                      inv_inv
                SET
                      inv_cond_db_id = 0,
                      inv_cond_cd    = 'REPREQ'
                WHERE
                      inv_no_db_id = lHInventoryDbId AND
                      inv_no_id    = lHInventoryId;
            END IF;

            /* if a tool then update the inventory's condition to REPREQ  */
            IF lMainInvDbId IS NOT NULL         AND
                lMainInvPartUseCd = 'TOOLS'     AND
                lHInvCondCd   NOT IN ( 'QUAR', 'CONDEMN', 'REPREQ', 'INREP' ) THEN

                UPDATE
                      inv_inv
                SET
                      inv_cond_db_id = 0,
                      inv_cond_cd    = 'REPREQ'
                WHERE
                      inv_no_db_id = lMainInvDbId AND
                      inv_no_id    = lMainInvId;
      END IF;
   END IF;

   /* return sucess */
   ol_Return  := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetPriority@@@' || SQLERRM);
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
         mim_data_type.eng_unit_cd,
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

      /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
      IF NOT UPPER(lrec_CADeadlines.data_type_cd) = 'CHR' THEN
         ld_SchedDeadDt := TO_DATE(TO_CHAR(ld_SchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;

      IF lrec_CADeadlines.eng_unit_cd = 'MONTH' THEN
         -- get the number of months between now (rounded to the nearest hour) and the deadline
         lf_UsageRem := round( MONTHS_BETWEEN(ld_SchedDeadDt, ROUND(SYSDATE,'HH')), 6);
      ELSE
         -- get the number of days between now (rounded to the nearest hour) and the deadline
         lf_UsageRem := round(((ld_SchedDeadDt - ROUND(SYSDATE,'HH')) / lrec_CADeadlines.ref_mult_qt), 6) ;
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

            IF lrec_USDeadlines.Start_Dt > SYSDATE THEN
                  IF an_AcftDbId IS NOT NULL THEN
                     ls_HashKey := lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                                   TO_CHAR(lrec_USDeadlines.Start_Dt, 'DD-MON-YYYY HH24:MI:SS') || ':' || lrec_USDeadlines.Tsn_Qt;

                     IF at_GetQt.EXISTS(ls_HashKey) THEN
                          lf_StartQt := at_GetQt(ls_HashKey);
                     ELSE
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
                          -- save it so we don't have to recalculate it again later
                          at_GetQt(ls_HashKey) := lf_StartQt;
                     END IF;
                  ELSE
                      -- Get Current Inventory Usage
                      lf_StartQt := lrec_USDeadlines.Tsn_Qt;
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

           /* round the all calendar deadlines to the end of the day, exception the data type of type hours */
           IF NOT UPPER(lrec_CADeadlines.data_type_cd) = 'CHR' THEN
              ld_SchedDeadDt := TO_DATE(TO_CHAR(ld_SchedDeadDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
           END IF;

           IF lrec_CADeadlines.eng_unit_cd = 'MONTH' THEN
              -- get the number of months between now (rounded to the nearest hour) and the deadline
              lf_UsageRem := MONTHS_BETWEEN(ld_SchedDeadDt, ROUND(SYSDATE,'HH'));
           ELSE
              -- get the number of days between now (rounded to the nearest hour) and the deadline
              lf_UsageRem :=  (ld_SchedDeadDt - ROUND(SYSDATE,'HH')) / lrec_CADeadlines.ref_mult_qt ;
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
                      h.inv_cond_cd  NOT IN ('REPREQ', 'QUAR', 'CONDEMN', 'INREP' )
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
                      i.inv_cond_cd   NOT IN ('REPREQ', 'QUAR', 'CONDEMN', 'INREP' )
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
   ld_ExtendedDt     evt_sched_dead.sched_dead_dt%TYPE;
   ln_TempPriority   NUMBER;


BEGIN

     on_Return := 0;

     ls_Query :=
     'SELECT                                                                           ' ||
     '       evt_sched_dead.data_type_db_id,                                           ' ||
     '       evt_sched_dead.data_type_id,                                              ' ||
     '       evt_sched_dead.usage_rem_qt,                                              ' ||
     '       evt_sched_dead.notify_qt,                                                 ' ||
     '       evt_sched_dead.deviation_qt,                                              ' ||
     '       CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt ' ||
     '            THEN 0                                                               ' ||
     '            ELSE 1                                                               ' ||
     '       END AS is_positive,                                                       ' ||
     '       getExtendedDeadlineDt( evt_sched_dead.deviation_qt,                       ' ||
     '                              evt_sched_dead.sched_dead_dt,                      ' ||
     '                              mim_data_type.domain_type_cd,                      ' ||
     '                              ref_eng_unit.ref_mult_qt                           ' ||
     '       ) AS extended_dt                                                          ' ||
     'FROM                                                                             ' ||
     '       evt_sched_dead,                                                           ' ||
     '       mim_data_type,                                                            ' ||
     '       ref_eng_unit                                                              ' ||
     'WHERE                                                                            ' ||
     '       evt_sched_dead.event_db_id   = ' ||  an_SchedDbId || ' AND                ' ||
     '       evt_sched_dead.event_id      = ' ||  an_SchedId   || '                    ' ||
     '       AND                                                                       ' ||
     '       mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND        ' ||
     '       mim_data_type.data_type_id    = evt_sched_dead.data_type_id               ' ||
     '       AND                                                                       ' ||
     '       ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND            ' ||
     '       ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd                   ';

   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
     IF ab_LastSchedBool = 1 THEN
        ls_Query := ls_Query || ' ORDER BY extended_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC';
     ELSE
        ls_Query := ls_Query || ' ORDER BY is_positive, extended_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC';
     END IF;

   ln_TempPriority := 1;

   -- set the driving task
     OPEN lc_Cursor FOR ls_Query ;
     FETCH lc_Cursor INTO ln_DataTypeDbId ,
                          ln_DataTypeId   ,
                          lf_UsageRemQt   ,
                          ln_NotifyQt     ,
                          ln_DeviationQt  ,
                          lb_IsPositive   ,
                          ld_ExtendedDt   ;
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
         event_db_id,
         event_id,
         data_type_db_id,
         data_type_id,
         ext_usage_rem_qt,
         DECODE ( NVL(last_sched_dead_bool, 0),
                  0,
                 (extended_dt - SYSDATE ),
                  -1 * (extended_dt - SYSDATE)
            ) AS sort_column
      FROM
        (  SELECT
             evt_event.event_db_id,
             evt_event.event_id,
             evt_sched_dead.data_type_db_id,
             evt_sched_dead.data_type_id,
             CASE WHEN mim_data_type.domain_type_cd = 'CA'
                  THEN evt_sched_dead.usage_rem_qt + (evt_sched_dead.deviation_qt/ref_eng_unit.ref_mult_qt)
                  ELSE evt_sched_dead.usage_rem_qt +  evt_sched_dead.deviation_qt
              END AS ext_usage_rem_qt,
             task_task.last_sched_dead_bool,
             getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                    evt_sched_dead.sched_dead_dt,
                                    mim_data_type.domain_type_cd,
                                    ref_eng_unit.ref_mult_qt
             ) AS extended_dt
          FROM
             ref_eng_unit,
             mim_data_type,
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
             AND
             mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
             mim_data_type.data_type_id    = evt_sched_dead.data_type_id
             AND
             ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
             ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
        )
      ORDER BY
         sort_column ASC,
         data_type_db_id,
         data_type_id,
         ext_usage_rem_qt;

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
                   mim_data_type.eng_unit_cd,
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
                   inv_tasks.eng_unit_cd,
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
          IF lrec_Tasks.eng_unit_cd = 'MONTH' THEN
             -- get the number of months between now (rounded to the nearest hour) and the deadline
             lf_UsageRem := MONTHS_BETWEEN(lrec_Tasks.sched_dead_dt, ROUND(SYSDATE,'HH'));
          ELSE
             -- get the number of days between now (rounded to the nearest hour) and the deadline
             lf_UsageRem :=  (lrec_Tasks.sched_dead_dt - ROUND(SYSDATE,'HH')) / lrec_Tasks.ref_mult_qt ;
          END IF;

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

--changeSet MX-19795:2 stripComments:false
-- Get all TOOLS inventory with:
--   - An IN WORK work package
--   - REPREQ condition
-- ... then update its condition to INREP.
UPDATE
   inv_inv
SET
   inv_inv.inv_cond_db_id = 0,
   inv_inv.inv_cond_cd    = 'INREP'
WHERE
   ( inv_inv.inv_no_db_id, inv_inv.inv_no_id ) IN
   (
      SELECT
         inner_inv.inv_no_db_id, inner_inv.inv_no_id
      FROM
         sched_stask
      INNER JOIN evt_event           ON evt_event.event_db_id     = sched_stask.sched_db_id AND
                                        evt_event.event_id        = sched_stask.sched_id
      INNER JOIN inv_inv inner_inv   ON inner_inv.inv_no_db_id    = sched_stask.main_inv_no_db_id AND
                                        inner_inv.inv_no_id       = sched_stask.main_inv_no_id
      INNER JOIN eqp_part_no         ON eqp_part_no.part_no_db_id = inner_inv.part_no_db_id AND
                                        eqp_part_no.part_no_id    = inner_inv.part_no_id
      WHERE
         sched_stask.task_class_db_id = 0 AND
         sched_stask.task_class_cd    = 'RO'
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'IN WORK'
         AND
         eqp_part_no.part_use_db_id   = 0 AND
         eqp_part_no.part_use_cd      = 'TOOLS'
         AND
         inner_inv.inv_cond_db_id     = 0 AND
         inner_inv.inv_cond_cd        = 'REPREQ'
   )
;