--liquibase formatted sql


--changeSet MX-28608:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY EVENT_PKG
IS


/**
* Resets the driving task relationship for the specified task and also updates the
* work order's deadlines if it applies
*/
PROCEDURE SetDRVTASKForRootOrBlock(
   an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
   an_SchedId     IN sched_stask.sched_id%TYPE,
   av_TaskClassCd IN sched_stask.task_class_cd%TYPE,
   an_IsForecast  IN NUMBER,
   an_IsRoot      IN NUMBER,
   on_Return      OUT NUMBER
   )IS

   ln_DrivingEventDbId        evt_event.event_db_id%TYPE;
   ln_DrivingEventId          evt_event.event_id%TYPE;
   ln_EvtEventRelId           NUMBER;
   ln_RelEventOrd             NUMBER;
   ln_DrivingCount            NUMBER;

   CURSOR lcur_Deadline IS
      SELECT
         event_db_id,
         event_id,
         data_type_db_id,
         data_type_id,
         ext_usage_rem_qt,
         extended_dt - SYSDATE AS sort_column
      FROM
        (  SELECT
             subtasks.event_db_id,
             subtasks.event_id,
             evt_sched_dead.data_type_db_id,
             evt_sched_dead.data_type_id,
             CASE WHEN mim_data_type.domain_type_cd = 'CA'
                  THEN evt_sched_dead.usage_rem_qt + (evt_sched_dead.deviation_qt/ref_eng_unit.ref_mult_qt)
                  ELSE evt_sched_dead.usage_rem_qt +  evt_sched_dead.deviation_qt
              END AS ext_usage_rem_qt,
             getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                    evt_sched_dead.sched_dead_dt,
                                    mim_data_type.domain_type_cd,
                                    mim_data_type.data_type_cd,
                                    ref_eng_unit.ref_mult_qt
             ) AS extended_dt
          FROM
             ref_eng_unit,
             mim_data_type,
             ( SELECT evt_event.event_db_id,
                      evt_event.event_id
               FROM   evt_event,
                      sched_stask
               WHERE  an_IsRoot = 1
                      AND
                      -- the task is a root
                      evt_event.h_event_db_id = an_SchedDbId AND
                      evt_event.h_event_id    = an_SchedId   AND
                      evt_event.hist_bool     = 0            AND
                      evt_event.rstat_cd      = 0
                      AND
                      sched_stask.sched_db_id        = evt_event.event_db_id AND
                      sched_stask.sched_id           = evt_event.event_id    AND
                      sched_stask.soft_deadline_bool = 0
               UNION ALL
               SELECT evt_event.event_db_id,
                      evt_event.event_id
               FROM   evt_event,
                      sched_stask
               WHERE  an_IsRoot = 0
                      AND
                      -- the task is a block
                      sched_stask.h_sched_db_id     = an_SchedDbId AND
                      sched_stask.h_sched_id        = an_SchedId   AND
                      sched_stask.soft_deadline_bool = 0
                      AND
                      evt_event.event_db_id = sched_stask.sched_db_id AND
                      evt_event.event_id    = sched_stask.sched_id    AND
                      evt_event.hist_bool   = 0                       AND
                      evt_event.rstat_cd    = 0
             ) subtasks,
             evt_sched_dead
          WHERE
             evt_sched_dead.event_db_id       = subtasks.event_db_id AND
             evt_sched_dead.event_id          = subtasks.event_id AND
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
            SELECT  DECODE (MAX(evt_event_rel.rel_event_ord), NULL, 1, MAX(evt_event_rel.rel_event_ord)+1),
                    DECODE (MAX(evt_event_rel.event_rel_id),  NULL, 1, MAX(evt_event_rel.event_rel_id)+1)
            INTO    ln_RelEventOrd,
                    ln_EvtEventRelId
            FROM    evt_event_rel
            WHERE   evt_event_rel.event_db_id = an_SchedDbId AND
                    evt_event_rel.event_id    = an_SchedId;


            /*insert new driving deadline relationship*/
            INSERT INTO evt_event_rel(event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd, rel_event_ord )
            VALUES(   an_SchedDbId, an_SchedId, ln_EvtEventRelId, ln_DrivingEventDbId, ln_DrivingEventId, 0, 'DRVTASK', ln_RelEventOrd);

        ELSE
            /* if the driving relationship already exists*/
            /* just update it */
            UPDATE evt_event_rel
            SET    evt_event_rel.rel_event_db_id = ln_DrivingEventDbId,
                   evt_event_rel.rel_event_id    = ln_DrivingEventId
            WHERE  evt_event_rel.event_db_id    = an_SchedDbId  AND
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
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetDRVTASKForRootOrBlock@@@' || SQLERRM);
      IF lcur_Deadline%ISOPEN THEN CLOSE lcur_Deadline; END IF;
      RETURN;
END SetDRVTASKForRootOrBlock;
   
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
* Recent Coder:   Julie Bajer
* Recent Date:    December 2010
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

   -- root task information
   ln_RootDbId       evt_event.h_event_db_id%TYPE;
   ln_RootId         evt_event.h_event_id%TYPE;
   lv_RootClassCd    sched_stask.task_class_cd%TYPE;
   lb_RootForecast   NUMBER;
   -- parent task information
   ln_ParentDbId     sched_stask.h_sched_db_id%TYPE;
   ln_ParentId       sched_stask.h_sched_id%TYPE;
   lv_ParentClassCd  sched_stask.task_class_cd%TYPE;
   lv_ParentModeCd   ref_task_class.class_mode_cd%TYPE;
   lb_ParentForecast NUMBER;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;


      SELECT root_stask.sched_db_id,
             root_stask.sched_id,
             root_stask.task_class_cd,
             DECODE ( root_event.event_status_cd, 'FORECAST', 1, 0),
             parent_stask.sched_db_id,
             parent_stask.sched_id,
             parent_stask.task_class_cd,
             ref_task_class.class_mode_cd,
             DECODE ( parent_event.event_status_cd, 'FORECAST', 1, 0)
      INTO   ln_RootDbId,
             ln_RootId,
             lv_RootClassCd,
             lb_RootForecast,
             ln_ParentDbId,
             ln_ParentId,
             lv_ParentClassCd,
             lv_ParentModeCd,
             lb_ParentForecast
      FROM   sched_stask,
             evt_event,
             sched_stask parent_stask,
             evt_event parent_event,
             sched_stask root_stask,
             evt_event root_event,
             ref_task_class
      WHERE  sched_stask.sched_db_id = an_SchedStaskDbId AND
             sched_stask.sched_id    = an_SchedStaskId
             AND
             -- highest parent
             parent_stask.sched_db_id (+)= sched_stask.h_sched_db_id AND
             parent_stask.sched_id    (+)= sched_stask.h_sched_id
             AND
             ref_task_class.task_class_db_id (+)= parent_stask.task_class_db_id AND
             ref_task_class.task_class_cd    (+)= parent_stask.task_class_cd
             AND
             parent_event.event_db_id (+)= parent_stask.sched_db_id AND
             parent_event.event_id    (+)= parent_stask.sched_id
             AND
             evt_event.event_db_id = sched_stask.sched_db_id AND
             evt_event.event_id    = sched_stask.sched_id
             AND
             -- root task
             root_stask.sched_db_id = evt_event.h_event_db_id AND
             root_stask.sched_id    = evt_event.h_event_id
             AND
             root_event.event_db_id = root_stask.sched_db_id AND
             root_event.event_id    = root_stask.sched_id;
     
     -- update the driving task for the root task.
     SetDRVTASKForRootOrBlock(ln_RootDbId, ln_RootId, lv_RootClassCd, lb_RootForecast, 1, on_Return);
     IF on_Return < 0 THEN
        RETURN;
     END IF;
     
     -- update the driving deadline of the parent BLOCK if it is different than the root task.
     IF ln_ParentDbId IS NOT NULL AND lv_ParentModeCd = 'BLOCK' AND NOT( ln_ParentDbId = ln_RootDbId AND ln_ParentId = ln_RootId ) THEN
         SetDRVTASKForRootOrBlock(ln_ParentDbId, ln_ParentId, lv_ParentClassCd, lb_ParentForecast, 0 , on_Return);
         IF on_Return < 0 THEN
            RETURN;
         END IF;
     END IF;
     
     -- Return success
     on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDrivingDeadline@@@' || SQLERRM);
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

   CURSOR lcur_ReplTask IS
       SELECT
          evt_event_rel.event_db_id,
          evt_event_rel.event_id
       FROM
          evt_event_rel
       WHERE
          evt_event_rel.rel_type_db_id  = 0              AND
          evt_event_rel.rel_type_cd     ='WORMVL'        AND
          evt_event_rel.rel_event_db_id = an_WorkPkgDbId AND
          evt_event_rel.rel_event_id    = an_WorkPkgId;
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
    CLOSE lcur_TsnQt;
    UpdateDeadline(lrec_DrvTaskDeadline.event_db_id, lrec_DrvTaskDeadline.event_id, on_Return);
    IF on_Return < 0 THEN
        CLOSE lcur_DrvTaskDeadline;
        RETURN;
    END IF;
 
   ELSE
    OPEN lcur_ReplTask;
    FETCH lcur_ReplTask INTO lrec_ReplTask;
    /* If Replacement Task if found */
    IF lcur_ReplTask%FOUND THEN
       /*Delete any exisitng dealines of the REPL task since there is no driving task for work package */
       DELETE evt_sched_dead
       WHERE  event_db_id = lrec_ReplTask.event_db_id AND
              event_id    = lrec_ReplTask.event_id;

       UpdateDeadline(lrec_ReplTask.event_db_id, lrec_ReplTask.event_id, on_Return);
       IF on_Return < 0 THEN
         CLOSE lcur_ReplTask;
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
      IF lcur_ReplTask%ISOPEN        THEN CLOSE lcur_ReplTask;        END IF;
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
          NVL(fl_leg.actual_departure_dt, fl_leg.sched_departure_dt) AS flight_start_dt
         ,NVL(fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt) AS flight_end_dt
         ,((NVL(fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt) - NVL(fl_leg.actual_departure_dt, fl_leg.sched_departure_dt))*24) AS flight_hrs
     FROM
         fl_leg
    WHERE
      fl_leg.aircraft_db_id = aAircraftDbId AND
      fl_leg.aircraft_id = aAircraftId AND
      fl_leg.hist_bool     = 0
      AND NVL(fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt) > aCurrentDate
      AND NVL(fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt) IS NOT NULL
      AND NVL(fl_leg.actual_departure_dt, fl_leg.sched_departure_dt) IS NOT NULL
    ORDER BY
     NVL(fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt), -- since the end date is used as the filter it must be sorted by this value as well
      NVL(fl_leg.actual_departure_dt, fl_leg.sched_departure_dt);

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
        NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) as flight_start_dt,
        NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) as flight_end_dt,
        ( ( NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) - NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) )*24 ) as flight_hrs
      FROM
        fl_leg
      WHERE
        fl_leg.aircraft_db_id = aAircraftDbId AND
        fl_leg.aircraft_id = aAircraftId AND
        fl_leg.hist_bool     = 0
        AND
        fl_leg.sched_arrival_dt > aCurrentDate
        AND
        NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) is not null AND
        NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) is not null
      ORDER BY
        NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt );

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
          CASE sqlcode
             WHEN -1861 THEN
               -- we went past the last day (ever (in oracle)), just use the last day
               aForecastDt := TO_DATE('9999-12-31 23:59:59', 'YYYY-MM-DD HH24:MI:SS');
               ol_Return := icn_Success;
             ELSE
               ol_Return := -100;
               APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@findForecastedDeadDtFcModel@@@' || SQLERRM);
          END CASE;
          IF lCurBlackouts%ISOPEN THEN CLOSE lCurBlackouts; END IF;
          IF lCurRanges%ISOPEN    THEN CLOSE lCurRanges;    END IF;
END findForecastedDeadDtFcModel;


/********************************************************************************
*
* FUNCTION:       GetEndOfDay
* Arguments:      aDate : date to use
* Returns:        Date holding the passed in dates end-of-day
*
* Description:    By definition, the end of day occurs at 23:59:59 of that date.
*                 (refer to the many places in PREP_DEADLINE_PKG where
*                  end-of-day is 23:59:59)
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetEndOfDay(
            aDate       DATE)
RETURN Date
IS
BEGIN
   return to_date( to_char( aDate, 'DD-MM-YYYY') || ' 23:59:59' ,
                   'DD-MM-YYYY HH24:MI:SS' );
END GetEndOfDay;


/********************************************************************************
*
* FUNCTION:       GetStartOfDay
* Arguments:      aDate : date to use
* Returns:        Date holding the passed in dates start-of-day
*
* Description:    By definition, the end of day occurs at 23:59:59 of that date
*                 (refer to GetEndOfDay).
*                 Therefore, if was want to get the start of day of that same
*                 day, it must be 24 hours prior to the end of day.
*                 Thus, the start of day must be 23:59:59 of the previous day.
*                 (as tempting as it seems to use 00:00:00, if we did,
*                  then our days would be short a second)
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetStartOfDay(
            aDate       DATE)
RETURN Date
IS
BEGIN
   return ( GetEndOfDay(aDate) - 1 );
END GetStartOfDay;


/********************************************************************************
*
* FUNCTION:       GetForecastPeriods
* Arguments:      aAircraftDbId : aircraft inventory key
*                 aAircraftId   :           "
*                 aDataTypeDbId : data type key
*                 aDataTypeId   :       "
*                 aStartDate    : date to start looking for forecast periods
*                 aEndDate      : date to end looking for forecast periods
* Returns:        Table (collection type) of ForecastPeriod records.
*
* Description:    Get the list of forecast periods for the provided aircraft that
*                 overlap the provided start and end dates.
*                 Note for clarity, if the start OR end date fall within a
*                 forecast period, that forecast period is inlcuded.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetForecastPeriods(
            aAircraftDbId  inv_ac_reg.inv_no_db_id%TYPE,
            aAircraftId    inv_ac_reg.inv_no_id%TYPE,
            aDataTypeDbId  evt_sched_dead.data_type_db_id%TYPE,
            aDataTypeId    evt_sched_dead.data_type_id%TYPE,
            aStartDate     DATE,
            aEndDate       DATE)
RETURN ForecastPeriods
IS
   -- Cursor to retrieve forecast ranges for the aircraft and data type.
   CURSOR lFcRangesCursor IS
      SELECT
         fc_range.start_month,
         fc_range.start_day,
         fc_rate.forecast_rate_qt,
         next_range.start_month as next_start_month,
         next_range.start_day   as next_start_day
      FROM
         inv_ac_reg
         INNER JOIN fc_range ON
            fc_range.model_db_id = inv_ac_reg.forecast_model_db_id AND
            fc_range.model_id    = inv_ac_reg.forecast_model_id
         INNER JOIN fc_rate ON
            fc_rate.model_db_id = fc_range.model_db_id AND
            fc_rate.model_id    = fc_range.model_id AND
            fc_rate.range_id    = fc_range.range_id
         LEFT OUTER JOIN fc_range next_range ON
            next_range.model_db_id = inv_ac_reg.forecast_model_db_id AND
            next_range.model_id    = inv_ac_reg.forecast_model_id AND
            next_range.range_id    = fc_rate.range_id + 1
      WHERE
         inv_ac_reg.inv_no_db_id = aAircraftDbId AND
         inv_ac_reg.inv_no_id    = aAircraftId
         AND
         fc_rate.data_type_db_id = aDataTypeDbId AND
         fc_rate.data_type_id    = aDataTypeId
      ORDER BY
         fc_range.start_month,
         fc_range.start_day
      ;

   lYear                 NUMBER;
   lYearCount            NUMBER;
   lIndex                NUMBER;
   lNumRecs              NUMBER;
   lForecastRec          ForecastPeriod;
   lForecastList         ForecastPeriods := ForecastPeriods();
   lReturnList           ForecastPeriods := ForecastPeriods();
   
BEGIN

   lYear := to_char( aStartDate, 'YYYY' );
   
   -- Forecast ranges are relative to a year (they have only start month and start day and
   -- implicitly end at either the the start of the next range or the end of the year)
   --
   -- So build a list of ranges spanning the entire year of the start date.
   -- This is used later to create the ranges for all the years up to the target date.
   
   lIndex := 1;
   
   FOR lFcRange IN lFcRangesCursor LOOP

      -- If there is no range starting on Jan 1st then we will need to create one.
      IF (lIndex = 1) AND NOT (lFcRange.start_month = 1 AND lFcRange.start_day = 1) THEN
      
         -- The newly created range will start on Jan 1 and end on the start of the first range.
         lForecastRec.in_StartDate := GetStartOfDay( trunc( aStartDate, 'YYYY' ) );
         lForecastRec.in_EndDate   := GetStartOfDay( to_date( lFcRange.start_day || '-' ||
                                                              lFcRange.start_month || '-' ||
                                                              lYear,
                                                              'DD-MM-YYYY') );
         lForecastRec.in_Rate := 0;
   
         lForecastList.EXTEND;
         lForecastList(lIndex) := lForecastRec;
         
         lIndex := lIndex + 1;
         
      END IF;
      
      -- Forecast range period is from start-of-day of this range to the start-of-day of the next range.
      lForecastRec.in_StartDate := GetStartOfDay( to_date(lFcRange.start_day   || '-' ||
                                                          lFcRange.start_month || '-' ||
                                                          lYear,
                                                          'DD-MM-YYYY') );
      IF lFcRange.next_start_month IS NULL THEN
         -- No next month so use Jan 1 of the following year.
         lForecastRec.in_EndDate := GetStartOfDay( add_months( to_date('01-01-' || lYear, 'DD-MM-YYYY'), 12 ) );
      ELSE
         lForecastRec.in_EndDate := GetStartOfDay( to_date(lFcRange.next_start_day   || '-' ||
                                                           lFcRange.next_start_month || '-' ||
                                                           lYear,
                                                           'DD-MM-YYYY') );
      END IF;
      lForecastRec.in_Rate := lFcRange.forecast_rate_qt;
      
      lForecastList.EXTEND;
      lForecastList(lIndex) := lForecastRec;
         
      lIndex := lIndex + 1;

   END LOOP;

   lNumRecs := lForecastList.COUNT;
   
   -- If there are no forecast ranges, then do nothing but return the empty list.
   IF lNumRecs = 0 THEN
      RETURN lReturnList;
   END IF;
   
   
   -- Add the same ranges for the following years up till the target date.
   lYearCount := 1;
   LOOP
      FOR i IN 1 .. lNumRecs LOOP
      
         lForecastRec := lForecastList(i);
         
         -- use all the same record values excect increase the start and end dates year
         lForecastRec.in_StartDate := to_date( to_char(lForecastRec.in_StartDate, 'DD-MM-') ||
                                               to_char(to_char(lForecastRec.in_StartDate, 'YYYY') + lYearCount) ||
                                               to_char(lForecastRec.in_StartDate, ' HH24:MI:SS'),
                                               'DD-MM-YYYY HH24:MI:SS');
         lForecastRec.in_EndDate   := to_date( to_char(lForecastRec.in_EndDate, 'DD-MM-') ||
                                               to_char(to_char(lForecastRec.in_EndDate, 'YYYY') + lYearCount) ||
                                               to_char(lForecastRec.in_EndDate, ' HH24:MI:SS'),
                                               'DD-MM-YYYY HH24:MI:SS');
                                             
         EXIT WHEN lForecastRec.in_StartDate > aEndDate;
         
         lForecastList.EXTEND;
         lForecastList(lIndex) := lForecastRec;
         
         lIndex := lIndex + 1;

      END LOOP;
      
      EXIT WHEN lForecastRec.in_StartDate > aEndDate;
      lYearCount := lYearCount + 1;
      
   END LOOP;
   
   -- We needed to populate the forecast periods with the entire start year in order to
   -- generate the subsequent years.
   -- So we will now trim the periods that fall prior to the provided start date.
   lIndex := 1;
   FOR i IN 1 .. lForecastList.COUNT LOOP
   
      CONTINUE WHEN lForecastList(i).in_EndDate < aStartDate;
   
      lReturnList.EXTEND;
      lReturnList(lIndex) := lForecastList(i);
      lIndex := lIndex + 1;
   END LOOP;

   RETURN lReturnList;

END GetForecastPeriods;


/********************************************************************************
*
* FUNCTION:       GetBlackoutPeriods
* Arguments:      aAircraftDbId : aircraft inventory key
*                 aAircraftId   :           "
*                 aStartDate    : date to start looking for blackout periods
*                 aEndDate      : date to end looking for blackout periods
* Returns:        Table (collection type) of BlackoutPeriod records.
*
* Description:    Get the list of blackout periods for the provided aircraft that
*                 overlap the provided start and end dates.
*                 Note for clarity, if the start OR end date fall within a
*                 blackout, that blackout period is inlcuded.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetBlackoutPeriods(
            aAircraftDbId  inv_ac_reg.inv_no_db_id%TYPE,
            aAircraftId    inv_ac_reg.inv_no_id%TYPE,
            aStartDate     DATE,
            aEndDate       DATE)
RETURN BlackoutPeriods
IS
   CURSOR lBlackoutsCursor IS
      SELECT
         evt_event.actual_start_dt as start_date,
         evt_event.event_dt as end_date
      FROM
         evt_inv
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
      WHERE
         evt_inv.inv_no_db_id = aAircraftDbId AND
         evt_inv.inv_no_id    = aAircraftId
         AND
         evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd    = 'BLK'
         AND
         evt_event.actual_start_dt < aEndDate AND
         evt_event.event_dt        > aStartDate
      ORDER BY
         evt_event.actual_start_dt
      ;

   lIndex        NUMBER;
   lBlackoutRec  BlackoutPeriod;
   lBlackoutList BlackoutPeriods := BlackoutPeriods();
   
BEGIN

   lIndex := 1;
   FOR lBlackout IN lBlackoutsCursor LOOP
   
      lBlackoutRec.in_StartDate := lBlackout.start_date;
      lBlackoutRec.in_EndDate   := lBlackout.end_date;
   
      lBlackoutList.EXTEND;
      lBlackoutList(lIndex) := lBlackoutRec;
      lIndex := lIndex + 1;
   
   END LOOP;
   
   RETURN lBlackoutList;
   
END GetBlackoutPeriods;


/********************************************************************************
*
* FUNCTION:       GetAnticipatedUsage
* Arguments:      aForecastPeriods : list of forecast periods
*                 aStartDate       : date to start looking for usage
*                 aEndDate         : date to end looking for usage
*
* Description:    Get the anticipated usage from the provided list of forecast
*                 periods between the provided start and end dates.
*                 The forecast periods each have their own usage rates that are
*                 used to calculate the anticipated usage.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetAnticipatedUsage(
            aForecastPeriods  ForecastPeriods,
            aStartDate  DATE,
            aEndDate    DATE)
RETURN NUMBER
IS
   lUsage        NUMBER;
   lStart        DATE;
   lEnd          DATE;
   lIndex        NUMBER;
   lRec  ForecastPeriod;

BEGIN

   lUsage := 0;
   
   -- Use these start and end dates to walk the forecast periods and
   -- use this index to keep track of where we are.
   lStart := aStartDate;
   lEnd   := aEndDate;
   lIndex := 1;

   -- Find the forecast period which contains the start date,
   -- as the forecast period may have to be adjusted.
   FOR i IN 1 .. aForecastPeriods.COUNT LOOP
   
      lRec := aForecastPeriods(i);
      lIndex := lIndex + 1;
   
      IF (lStart >= lRec.in_StartDate) AND (lStart < lRec.in_EndDate) THEN

         -- If the end is beyond the forecast period
         -- then adjust it to be the forecast period end date.
         IF lEnd > lRec.in_EndDate THEN
            lEnd := lRec.in_EndDate;
         END IF;

         lUsage := lUsage + (lEnd - lStart) * lRec.in_Rate;
         
         -- Move the start to the end of this forecast period and break out of loop.
         lStart := lEnd;
         EXIT;
         
      END IF;
      
   END LOOP;

   -- Calculate the anticipated usage for the rest of the forecast periods.
   IF lIndex < aForecastPeriods.COUNT THEN
      FOR i IN lIndex .. aForecastPeriods.COUNT LOOP
      
         lRec := aForecastPeriods(i);
   
         EXIT WHEN lRec.in_StartDate > aEndDate;
   
         lStart := lRec.in_StartDate;
         lEnd   := lRec.in_EndDate;
         IF aEndDate < lRec.in_EndDate THEN
            lEnd := aEndDate;
         END IF;
   
         lUsage := lUsage + (lEnd - lStart) * lRec.in_Rate;
   
      END LOOP;
   END IF;
   
   RETURN lUsage;
   
END GetAnticipatedUsage;


/********************************************************************************
*
* PROCEDURE:      GetPlannedFlightUsageBetweenDt
* Arguments:      aAircraftDbId           : aircraft primary key
*                 aAircraftId             :         "
*                 aDataTypeDbId           : datatype primary key
*                 aDataTypeId             :         "
*                 aStartDate              : date to start looking for flights
*                 aTargetDate             : date to end looking for flights
*                 oLastFlightArrivalDate  : returned date of the last flight,
*                                           otherwise NULL
*                 oAnticipatedFlightUsage : returned total anticipated usage
*
* Description:    Get the anticipated usage for flights between the provided
*                 start and target dates, for the provided aircraft and data type.
*                 It returns the total anticipated usage, as well as, the
*                 date of the last flight (if exists).
*                 If there are no flights then the usage will be 0 and the date
*                 of the last flight will be null.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetPlannedFlightUsageBetweenDt(aAircraftDbId       IN inv_inv.inv_no_db_id%TYPE,
                                         aAircraftId         IN inv_inv.inv_no_id%TYPE,
                                         aDataTypeDbId       IN evt_sched_dead.data_type_db_id%TYPE,
                                         aDataTypeId         IN evt_sched_dead.data_type_id%TYPE,
                                         aStartDate          IN evt_sched_dead.start_dt%TYPE,
                                         aTargetDate         IN evt_sched_dead.start_dt%TYPE,
                                         oLastFlightArrivalDate  OUT DATE,
                                         oAnticipatedFlightUsage OUT NUMBER
   )
IS

   -- This query fetches all the non-historic flights against the current aircraft
   -- whose arrival date is in the future of the provided start date.
   -- (if the flight happens to have actual departure/arrival dates,
   --  then these will be used instead of the scheduled departure/arrival dates)
   CURSOR lNonHistoricFlightsCursor IS
      SELECT
         NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) as flight_start_dt,
         NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) as flight_end_dt,
         ( ( NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) - NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) )*24 ) as flight_hrs
      FROM
         fl_leg
      WHERE
         fl_leg.aircraft_db_id = aAircraftDbId AND
         fl_leg.aircraft_id    = aAircraftId AND
         fl_leg.hist_bool      = 0
         AND
         -- must have both a departure and arrival date (may be actual or scheduled)
         NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt ) IS NOT NULL AND
         NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) IS NOT NULL
         AND
         NVL( fl_leg.actual_arrival_dt, fl_leg.sched_arrival_dt ) >= aStartDate
      ORDER BY
         NVL( fl_leg.actual_departure_dt, fl_leg.sched_departure_dt );
   
   lNonHistoricFlightRec     lNonHistoricFlightsCursor%ROWTYPE;
   lAnticipatedUsage         NUMBER := 0;
   
BEGIN

   oAnticipatedFlightUsage := 0;
   oLastFlightArrivalDate  := NULL;
      
   -- Check if the data type is for a flight (i.e. HOURS, CYCLES, LANDING).
   IF (aDataTypeDbId = 0) AND ( aDataTypeId = 1 OR aDataTypeId = 10 OR aDataTypeId = 30 ) THEN

      -- Get all non-historic flights (i.e. planned flights) after the start date.
      FOR lNonHistoricFlightRec IN lNonHistoricFlightsCursor LOOP

         -- Keep track to determine the last flight.
         oLastFlightArrivalDate := lNonHistoricFlightRec.flight_end_dt;
         
         -- Exit once we find a planned flight that arrives on or after the target date.
         EXIT WHEN lNonHistoricFlightRec.flight_end_dt >= aTargetDate;
            
         -- Since the flight arrived prior to the target,
         -- get the flights anticipated usage based on the data type.
         
         -- Check if the data type is HOURS.
         IF (aDataTypeDbId = 0) AND (aDataTypeId = 1) THEN
     
            -- The anticipated usage is the flights flying hours.
            lAnticipatedUsage := lNonHistoricFlightRec.flight_hrs;
            
         ELSE
         
            -- For all other flight data types the anticipated usage is one per flight.
            -- (e.g CYCLES, LANDING)
            lAnticipatedUsage :=  1;
            
         END IF;
         
         oAnticipatedFlightUsage := oAnticipatedFlightUsage + lAnticipatedUsage;
            
      END LOOP;
      
   END IF;
      
END GetPlannedFlightUsageBetweenDt;


/********************************************************************************
*
* FUNCTION:       GetForecastUsageBetweenDt
* Arguments:      aForecastPeriods : collection of forecast periods
*                 aBlackoutPeriods : collection of blackout periods
*                 aStartDate       : start date
*                 aEndDate         : end date
* Returns:        Forecasted usage.
*
* Description:    Get the forecasted usage between the provided start and end dates.
*                 The forecasted usage is calculated by deriving the anticipated
*                 usage from the provided forecast periods and subtrcating the
*                 anticipated usage from the provided blackout periods.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetForecastUsageBetweenDt(
            aForecastPeriods    IN ForecastPeriods,
            aBlackoutPeriods    IN BlackoutPeriods,
            aStartDate          IN evt_sched_dead.start_dt%TYPE,
            aEndDate            IN evt_sched_dead.start_dt%TYPE
   )
RETURN NUMBER
IS
   lUsage           NUMBER;
   lBlackoutPeriod  BlackoutPeriod;
   lBlackoutUsage   NUMBER;
   lBlackoutStart   DATE;
   lBlackoutEnd     DATE;
   
BEGIN

   -- Get the total anticipated usage between the start and target dates from the forecast periods.
   lUsage := GetAnticipatedUsage( aForecastPeriods, aStartDate, aEndDate );

   -- Subtract the anticipated usage for each of the blackout periods.
   FOR i IN 1 .. aBlackoutPeriods.COUNT LOOP

      lBlackoutPeriod := aBlackoutPeriods(i);

      -- Adjust the blackout start and/or end to ensure we are not counting the blackout
      -- rates outside the provided start and end dates.
      
      lBlackoutStart := lBlackoutPeriod.in_StartDate;
      IF (aStartDate > lBlackoutPeriod.in_StartDate) AND (aStartDate < lBlackoutPeriod.in_EndDate) THEN
         lBlackoutStart := aStartDate;
      END IF;
      
      lBlackoutEnd := lBlackoutPeriod.in_EndDate;
      IF (aEndDate > lBlackoutPeriod.in_StartDate) AND (aEndDate < lBlackoutPeriod.in_EndDate) THEN
         lBlackoutEnd := aEndDate;
      END IF;
      
      
      lBlackoutUsage := GetAnticipatedUsage(aForecastPeriods,
                                            lBlackoutStart,
                                            lBlackoutEnd );
      lUsage := lUsage - lBlackoutUsage;

   END LOOP;
   
   RETURN lUsage;
   
END GetForecastUsageBetweenDt;


/********************************************************************************
*
* PROCEDURE:      GetActualUsageForDay
* Arguments:      aAircraftDbId : aircraft inventory key
*                 aAircraftId   :           "
*                 aDataTypeDbId : data type key
*                 aDataTypeId   :       "
*                 aDate         : date to get actual usage on
* Returns:        Actual usage.
*
* Description:    Get the actual usage for the provided aircraft and
*                 data type on the day of the provided date.
*                 i.e. from the start of that day till the end.
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetActualUsageForDay(
            aAircraftDbId       IN inv_inv.inv_no_db_id%TYPE,
            aAircraftId         IN inv_inv.inv_no_id%TYPE,
            aDataTypeDbId       IN evt_sched_dead.data_type_db_id%TYPE,
            aDataTypeId         IN evt_sched_dead.data_type_id%TYPE,
            aDate               IN evt_sched_dead.start_dt%TYPE
   )
RETURN NUMBER
IS

   lTotalUsage NUMBER;
   lStartOfDay DATE   := GetStartOfDay(aDate);
   lEndOFDay   DATE   := GetEndOfDay(aDate);
   
BEGIN

   SELECT
      SUM(usg_usage_data.tsn_delta_qt)
   INTO
      lTotalUsage
   FROM
      usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
   WHERE
      usg_usage_data.inv_no_db_id = aAircraftDbId AND
      usg_usage_data.inv_no_id    = aAircraftId
      AND
      usg_usage_data.data_type_db_id = aDataTypeDbId AND
      usg_usage_data.data_type_id    = aDataTypeId
      AND
      usg_usage_record.usage_dt >= lStartOfDay AND
      usg_usage_record.usage_dt <= lEndOFDay
   ;
   
   IF lTotalUsage IS NULL THEN
      lTotalUsage := 0;
   END IF;
   
   RETURN lTotalUsage;
   
END GetActualUsageForDay;


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
*                 flight plan and forecast model.
*
* Orig.Coder:     Andrei Smolko
* Recent Coder:   N/A
* Recent Date:    March 19, 2008
*
*********************************************************************************
*
* Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
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
   
   ln_AnticipatedFlightUsage NUMBER  := 0;
   ln_AnticipatedFcUsage     NUMBER  := 0;
   ln_ActualDaysUsage        NUMBER  := 0;
   ld_LastFlightArrivalDate  DATE    := NULL;
   ld_FcModelStartDate       DATE    := NULL;
   ld_StartOfStartDay        DATE    := NULL;
   ld_EndOfStartDay          DATE    := NULL;
   lb_GetFcModelUsage        BOOLEAN := TRUE;
   lb_PlannedFlightsExist    BOOLEAN := FALSE;
   
   lForecastPeriods  ForecastPeriods := NULL;
   lBlackoutPeriods  BlackoutPeriods := NULL;
        
BEGIN

   on_Return := icn_Success;
   
   -- Initialize the returned predicted usage with the passed in start quantity.
   on_TsnQt := an_StartUsageQt;
   
   -- Initialize the forecast model start date to be the end-of-day of the start date.
   -- (the usage for the day of the start date will be handled later)
   ld_FcModelStartDate := GetEndOfDay( ad_StartDate );
   

   -- Check if the data type is for a flight (i.e. HOURS, CYCLES, LANDING).
   IF (an_DataTypeDbId = 0) AND ( an_DataTypeId = 1 OR an_DataTypeId = 10 OR an_DataTypeId = 30 ) THEN
      
      -- The data type is for a flight so get any anticipated flight usage.
      GetPlannedFlightUsageBetweenDt(aAircraftDbId,
                                     aAircraftId,
                                     an_DataTypeDbId,
                                     an_DataTypeId,
                                     ad_StartDate,
                                     ad_TargetDate,
                                     ld_LastFlightArrivalDate,
                                     ln_AnticipatedFlightUsage);

      on_TsnQt := on_TsnQt + ln_AnticipatedFlightUsage;
      
      IF ld_LastFlightArrivalDate IS NOT NULL THEN
         lb_PlannedFlightsExist := TRUE;
      END IF;
      
      IF lb_PlannedFlightsExist THEN
      
         -- If the last flight is prior to the start-of-day of the target date
         -- then use forecast model for that remaining time.
         -- (we assume that planned flights are planned for the whole day,
         --  thus, if the last planned flight is on the target day then we are done)

         IF ld_LastFlightArrivalDate < GetStartOfDay( ad_TargetDate ) THEN
            ld_FcModelStartDate := GetEndOfDay( ld_LastFlightArrivalDate );
         ELSE
            lb_GetFcModelUsage := FALSE;
         END IF;
         
      END IF;
            
   END IF;
   
   -- Check if we need to get anticipated usage using the forecast model.
   IF lb_GetFcModelUsage THEN
   
      -- Get the forecast periods starting with the start-of-day for the start date.
      -- (as we will be reusing these forecast periods when calculating
      --  the usage for the day of the start date)
      lForecastPeriods := GetForecastPeriods(aAircraftDbId,
                                             aAircraftId,
                                             an_DataTypeDbId,
                                             an_DataTypeId,
                                             GetStartOfDay(ad_StartDate),
                                             ad_TargetDate);
                                             
      -- Get the blackout periods between the start and target dates.
      -- (we won't be reusing these later)
      lBlackoutPeriods := GetBlackoutPeriods(aAircraftDbId,
                                             aAircraftId,
                                             ad_StartDate,
                                             ad_TargetDate);
      
                                
      -- Get the anticipated usage from the forecast model (using blackouts).
      ln_AnticipatedFcUsage := GetForecastUsageBetweenDt(lForecastPeriods,
                                                         lBlackoutPeriods,
                                                         ld_FcModelStartDate,
                                                         ad_TargetDate);

      on_TsnQt := on_TsnQt + ln_AnticipatedFcUsage;
      
      
      -- If there were no planned flights then we also want to forecast the usage
      -- for the day of the start date (as the previous forecast usage was calculated
      -- from the start dates end-of-day).
      IF NOT lb_PlannedFlightsExist THEN
      
         -- Get the actual usage acrued on the day of the start date.
         -- (it is assumed that this actual usage has already been applied
         --  to the provided start usage quantity)
         ln_ActualDaysUsage := GetActualUsageForDay(aAircraftDbId,
                                                    aAircraftId,
                                                    an_DataTypeDbId,
                                                    an_DataTypeId,
                                                    ad_StartDate);
                                                              
         ld_StartOfStartDay := GetStartOfDay( ad_StartDate );
         ld_EndOfStartDay := GetEndOfDay( ad_StartDate );
         
         -- Get the blackout periods for the day of the start date.
         lBlackoutPeriods := GetBlackoutPeriods(aAircraftDbId,
                                                aAircraftId,
                                                ld_StartOfStartDay,
                                                ld_EndOfStartDay);
                                        
         -- Get the anticipated usage from the forecast model (using blackouts).
         ln_AnticipatedFcUsage := GetForecastUsageBetweenDt(lForecastPeriods,
                                                            lBlackoutPeriods,
                                                            ld_StartOfStartDay,
                                                            ld_EndOfStartDay);
         
         -- If there is less actual usage then the anticipated forecast usage
         -- then add the difference to the predicted usage.
         -- (the actual usage is already counted in the current usage, so we
         --  do not want to count it again)

         IF ln_ActualDaysUsage < ln_AnticipatedFcUsage THEN
            on_TsnQt := on_TsnQt + (ln_AnticipatedFcUsage - ln_ActualDaysUsage);
         END IF;
         
      END IF;
      

   END IF;

   EXCEPTION
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
   ln_DataTypeDbId   mim_data_type.data_type_db_id%TYPE;
   ln_DataTypeId     mim_data_type.data_type_id%TYPE;

   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
   CURSOR lcur_LastDue IS
      SELECT
                  evt_sched_dead.data_type_db_id,
                  evt_sched_dead.data_type_id,
                  CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt
                       THEN 0
                       ELSE 1
                  END AS is_positive,
                  getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                         evt_sched_dead.sched_dead_dt,
                                         mim_data_type.domain_type_cd,
                                         mim_data_type.data_type_cd,
                                         ref_eng_unit.ref_mult_qt
                  ) AS extended_dt
           FROM
                  evt_sched_dead,
                  mim_data_type,
                  ref_eng_unit
           WHERE
                  evt_sched_dead.event_db_id   = aEventDbId AND
                  evt_sched_dead.event_id      = aEventId
                  AND
                  mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                  mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                  AND
                  ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
                  ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
           ORDER BY extended_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC;
   lrec_LastDue lcur_LastDue%ROWTYPE;
   
   CURSOR lcur_FirstDue IS
      SELECT
                  evt_sched_dead.data_type_db_id,
                  evt_sched_dead.data_type_id,
                  CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt
                       THEN 0
                       ELSE 1
                  END AS is_positive,
                  getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                         evt_sched_dead.sched_dead_dt,
                                         mim_data_type.domain_type_cd,
                                         mim_data_type.data_type_cd,
                                         ref_eng_unit.ref_mult_qt
                  ) AS extended_dt
           FROM
                  evt_sched_dead,
                  mim_data_type,
                  ref_eng_unit
           WHERE
                  evt_sched_dead.event_db_id   = aEventDbId AND
                  evt_sched_dead.event_id      = aEventId
                  AND
                  mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                  mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                  AND
                  ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
                  ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
           ORDER BY is_positive, extended_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC;
   lrec_FirstDue lcur_FirstDue%ROWTYPE;
   
BEGIN

    on_Return := 0;

    IF aLastSchedBool = 1 THEN
    
      OPEN lcur_LastDue;
      FETCH lcur_LastDue INTO lrec_LastDue;
      IF lcur_LastDue%FOUND THEN
         ln_DataTypeDbId := lrec_LastDue.Data_Type_Db_Id;
         ln_DataTypeId   := lrec_LastDue.Data_Type_Id;
      END IF;
      CLOSE lcur_LastDue;
    ELSE
   
      OPEN lcur_FirstDue;
      FETCH lcur_FirstDue INTO lrec_FirstDue;
      IF lcur_FirstDue%FOUND THEN
         ln_DataTypeDbId := lrec_FirstDue.Data_Type_Db_Id;
         ln_DataTypeId   := lrec_FirstDue.Data_Type_Id;
      END IF;
      CLOSE lcur_FirstDue;
    END IF;
   
    IF ln_DataTypeDbId IS NOT NULL THEN

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

   /* return success */
   on_Return := icn_Success;

-- exception handling
EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@SetTaskDrivingDeadline@@@' || SQLERRM);
      IF lcur_FirstDue%ISOPEN THEN CLOSE lcur_FirstDue; END IF;
      IF lcur_LastDue%ISOPEN  THEN CLOSE lcur_LastDue;  END IF;
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
          cl_EventDbId typn_Id,
        cl_EventId typn_Id
    ) IS
     SELECT drv_sched_dead.sched_dead_qt,
            getExtendedDeadlineDt(
               drv_sched_dead.deviation_qt,
               drv_sched_dead.sched_dead_dt,
               mim_data_type.domain_type_cd,
               mim_data_type.data_type_cd,
               ref_eng_unit.ref_mult_qt
             ) sched_dead_dt
     FROM evt_event,
          evt_event drv_event,
          evt_sched_dead drv_sched_dead,
          evt_event_rel drv_event_rel,
          mim_data_type,
          ref_eng_unit
     WHERE ( evt_event.event_db_id, evt_event.event_id ) IN
           (SELECT event_db_id,
                   event_id
              FROM evt_event
              START WITH event_db_id      = cl_eventdbid AND
                         event_id         = cl_eventid
              CONNECT BY nh_event_db_id   = PRIOR event_db_id AND
                         nh_event_id      = PRIOR event_id
            )
           AND
         drv_event_rel.event_db_id        = evt_event.event_db_id AND
         drv_event_rel.event_id           = evt_event.event_id AND
           drv_event_rel.rel_type_cd        = 'DRVTASK'
           AND
         drv_event.event_db_id            = drv_event_rel.rel_event_db_id AND
           drv_event.event_id               = drv_event_rel.rel_event_id
           AND
         drv_sched_dead.event_db_id       = drv_event.event_db_id AND
           drv_sched_dead.event_id          = drv_event.event_id AND
           drv_sched_dead.sched_driver_bool = 1
           AND
         mim_data_type.data_type_db_id    = drv_sched_dead.data_type_db_id AND
           mim_data_type.data_type_id       = drv_sched_dead.data_type_id
           AND
         ref_eng_unit.eng_unit_db_id      = mim_data_type.eng_unit_db_id AND
           ref_eng_unit.eng_unit_cd         = mim_data_type.eng_unit_cd;
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
   CURSOR lcur_CADeadlines (
         cn_EventDbId evt_event.event_db_id%TYPE,
         cn_EventId   evt_event.event_id%TYPE
      )
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
         mim_data_type.data_type_cd,
         evt_sched_dead.revision_dt AS sched_dead_revision_dt,
         evt_sched_dead.postfixed_qt
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
   CURSOR lcur_USDeadlines (
         cn_EventDbId evt_event.event_db_id%TYPE,
         cn_EventId   evt_event.event_id%TYPE
      )
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
         evt_sched_dead.sched_from_cd,
         NVL( evt_sched_dead.interval_qt, 0 ) AS interval_qt,
         inv_inv.inv_class_db_id,
         inv_inv.inv_class_cd,
         inv_curr_usage.tsn_qt,
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         evt_sched_dead.revision_dt AS sched_dead_revision_dt,
         inv_curr_usage.revision_dt AS curr_usage_revision_dt
      FROM
         evt_sched_dead,
         mim_data_type,
         inv_inv,
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
         inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
         inv_inv.inv_no_id    = sched_stask.main_inv_no_id
         AND
         inv_curr_usage.inv_no_db_id    = inv_inv.inv_no_db_id              AND
         inv_curr_usage.inv_no_id       = inv_inv.inv_no_id                 AND
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


   -- for WPEND/WPSTART scheduling window
   CURSOR lcur_SchedulingWindow (
         cn_EventDbId    evt_sched_dead.event_db_id%TYPE,
         cn_EventId      evt_sched_dead.event_id%TYPE,
         cn_DataTypeDbId evt_sched_dead.data_type_db_id%TYPE,
         cn_DataTypeId   evt_sched_dead.data_type_id%TYPE,
         cn_StartQt      evt_sched_dead.sched_dead_qt%TYPE )
   IS
      SELECT
         resched_info.sched_dead_qt,
         resched_info.resched_from_cd,
         resched_info.is_within
      FROM
         (SELECT
            evt_sched_dead.sched_dead_qt,
            root_event.hist_bool,
            root_event.sched_start_gdt,
            root_event.actual_start_gdt,
            root_event.sched_end_gdt,
            root_event.event_gdt,
            task_task.resched_from_cd,
            -- the usage at the date is within the scheduling window of the previous task
            CASE WHEN cn_StartQt >= (evt_sched_dead.sched_dead_qt - evt_sched_dead.prefixed_qt) AND
                  cn_StartQt <= (evt_sched_dead.sched_dead_qt + evt_sched_dead.postfixed_qt) THEN
               1
            ELSE
               0
            END AS is_within
         FROM
            evt_event_rel,
            evt_sched_dead,
            evt_event,
            evt_event root_event,
            sched_stask,
            sched_stask root_stask,
            task_task
         WHERE
            evt_event_rel.rel_event_db_id = cn_EventDbId AND
            evt_event_rel.rel_event_id    = cn_EventId
            AND
            evt_sched_dead.event_db_id    = evt_event_rel.event_db_id AND
            evt_sched_dead.event_id       = evt_event_rel.event_id    AND
            evt_sched_dead.data_type_db_id = cn_DataTypeDbId          AND
            evt_sched_dead.data_type_id    = cn_DataTypeId
            AND
            evt_sched_dead.sched_dead_qt IS NOT NULL AND
            evt_sched_dead.postfixed_qt  IS NOT NULL AND
            evt_sched_dead.prefixed_qt   IS NOT NULL
            AND
            evt_event.event_db_id  = evt_sched_dead.event_db_id AND
            evt_event.event_id     = evt_sched_dead.event_id    AND
            evt_event.hist_bool    = 1
            AND
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
            AND
            task_task.task_db_id         = sched_stask.task_db_id AND
            task_task.task_id            = sched_stask.task_id    AND
            task_task.resched_from_db_id = 0                      AND
            task_task.resched_from_cd    IN ('WPSTART', 'WPEND')
            AND
            -- and is part of a work package
            root_event.event_db_id = evt_event.h_event_db_id AND
            root_event.event_id    = evt_event.h_event_id
            AND
            root_stask.sched_db_id      = root_event.event_db_id AND
            root_stask.sched_id         = root_event.event_id    AND
            root_stask.task_class_db_id = 0                      AND
            root_stask.task_class_cd    IN ('CHECK', 'RO')
         ) resched_info
      WHERE
         (
            resched_info.resched_from_cd = 'WPSTART' AND
            resched_info.hist_bool       = 1         AND
            resched_info.actual_start_gdt  IS NOT NULL
         )
         OR
         (
            resched_info.resched_from_cd = 'WPSTART' AND
            resched_info.hist_bool       = 0         AND
            resched_info.sched_start_gdt   IS NOT NULL
         )
         OR
         (
            resched_info.resched_from_cd = 'WPEND' AND
            resched_info.hist_bool       = 1       AND
            resched_info.event_gdt         IS NOT NULL
         )
         OR
         (
            resched_info.resched_from_cd = 'WPEND' AND
            resched_info.hist_bool       = 0       AND
            resched_info.sched_end_gdt     IS NOT NULL
         );
   lrec_SchedulingWindow lcur_SchedulingWindow%ROWTYPE;

   ld_BeginDate        DATE;
   lf_BeginQt          FLOAT;
   lf_StartQt          FLOAT;
   lf_UsageRemExtended FLOAT;
   lf_UsageRem         FLOAT;
   lf_SchedDeadQt      FLOAT;
   ld_SchedDeadDt      DATE;
   ld_StartDt          DATE;
   lv_SchedFromCd      ref_sched_from.sched_from_cd%TYPE;

   -- stored the due values of the previous task
   lt_LastCADue        hash_date_type;
   lt_LastBeginDt      hash_date_type;
   lt_LastBeginQt      hash_float_type;
   lt_LastUSDue        hash_float_type;
   lt_LastUSOverlap    hash_float_type;
   ls_HashKey          VARCHAR(100);
   lf_RootLevel        NUMBER := 0;
   lf_PrevLevel        NUMBER;
   ls_PrevHashKey      VARCHAR(100);
   lb_HistoricalTask   evt_event.hist_bool%TYPE;

   lf_USOverlap        NUMBER;

   -- variables used to implement retry mechanism
   ln_PreExecutionCount NUMBER;
   ln_MaxAttempts NUMBER := 10;
   ln_AttemptCount NUMBER := 0;

   -- maximum retries exception
   max_retries_exception EXCEPTION;
   first_task_not_actv_exception EXCEPTION;

BEGIN
   <<retry>>
   -- increment the attempt counter
   ln_AttemptCount:=  ln_AttemptCount + 1;

   --clean up cursors and cache if this is a retry
   IF (ln_AttemptCount > 1) THEN
      --close cursors if open in case we got to this point via a retry
      IF lcur_CADeadlines%ISOPEN THEN CLOSE lcur_CADeadlines; END IF;
      IF lcur_USDeadlines%ISOPEN THEN CLOSE lcur_USDeadlines; END IF;
      IF lcur_DepTasks%ISOPEN THEN CLOSE lcur_DepTasks; END IF;
      IF lcur_SchedulingWindow%ISOPEN THEN CLOSE lcur_SchedulingWindow; END IF;

      --clear hash tables (associative arrays used to cache values)
      lt_LastCADue.DELETE;
      lt_LastBeginDt.DELETE;
      lt_LastBeginQt.DELETE;
      lt_LastUSDue.DELETE;
      lt_LastUSOverlap.DELETE;
   END IF;

   -- try up to 10 times if a conflict is detected, i.e. another process is updating usage
   IF ln_AttemptCount > ln_MaxAttempts THEN
      RAISE max_retries_exception;
   END IF;

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

      /*check to see how many rows will be updated for calendar deadlines*/
      SELECT
         COUNT(*) INTO ln_PreExecutionCount
      FROM
         evt_sched_dead
      WHERE
         evt_sched_dead.event_db_id     = lrec_CADeadlines.Event_Db_Id     AND
         evt_sched_dead.event_id        = lrec_CADeadlines.Event_Id        AND
         evt_sched_dead.data_type_db_id = lrec_CADeadlines.Data_Type_Db_Id AND
         evt_sched_dead.data_type_id    = lrec_CADeadlines.Data_Type_Id
         AND
         (
            NOT (
               evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
               evt_sched_dead.usage_rem_qt  = lf_UsageRem
            )
            OR
            evt_sched_dead.sched_dead_dt IS NULL
            OR
            evt_sched_dead.usage_rem_qt  IS NULL
         );

      IF ln_PreExecutionCount = 1 THEN
         /* update the evt_sched_dead */
         /* the where clause in this case includes a check of revision date */
         /* if the number of rows updated does not match the pre-execution count, it means another process revised the record */
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
            evt_sched_dead.revision_dt = lrec_CADeadlines.sched_dead_revision_dt;


         /* check to see if a row was updated when using the revision_dt in the where clause */
         IF (SQL%ROWCOUNT = 0) THEN
            GOTO retry;
         END IF;
      END IF;

      ls_HashKey := lf_RootLevel ||':'|| lrec_CADeadlines.Data_Type_Db_Id || ':' || lrec_CADeadlines.Data_Type_Id;
      
      -- schedule the next task from the extended deadline date
      -- however, if the schedule to plan high value is greater than or equal to the deviation value
      -- then the next task is scheduled from the due date (instead of the extended deadline date)
      IF lrec_CADeadlines.postfixed_qt >= lrec_CADeadlines.deviation_qt THEN
         lt_LastCADue(ls_HashKey) := ld_SchedDeadDt;
      ELSE
        lt_LastCADue(ls_HashKey) :=
              getExtendedDeadlineDt(
                    lrec_CADeadlines.deviation_qt,
                    ld_SchedDeadDt,
                    'CA',
                    lrec_CADeadlines.data_type_cd,
                    lrec_CADeadlines.ref_mult_qt
                 );
      END IF;
      
   END IF;
   CLOSE lcur_CADeadlines;

   -- process the ACTV task's US deadlines.
   FOR lrec_USDeadlines IN lcur_USDeadlines (an_SchedDbId, an_SchedId) LOOP

      lf_SchedDeadQt  := lrec_USDeadlines.Sched_Dead_Qt;
      lf_StartQt      := lrec_USDeadlines.Start_Qt;
      ld_BeginDate    := SYSDATE;
      lf_USOverlap    := 0;
      ld_SchedDeadDt  := NULL;
      lv_SchedFromCd  := lrec_USDeadlines.Sched_From_Cd;

      -- If based on a start date we need to re-calculate the start_qt
      -- Exclude CUSTOM deadlines.  CUSTOM deadlines should have no start_dt, but do this because not sure
      -- if all of the code clears the start_dt when changing to CUSTOM
      IF lrec_USDeadlines.Sched_From_Cd != 'CUSTOM' AND lrec_USDeadlines.Start_Dt IS NOT NULL THEN

         IF lrec_USDeadlines.Start_Dt > SYSDATE THEN
            IF an_AcftDbId IS NOT NULL THEN
               ls_HashKey := lf_RootLevel ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                                TO_CHAR(lrec_USDeadlines.Start_Dt, 'DD-MON-YYYY HH24:MI:SS') || ':' || lrec_USDeadlines.Tsn_Qt;

               IF at_GetQt.EXISTS(ls_HashKey) THEN
                  lf_StartQt := at_GetQt(ls_HashKey);
               ELSE
                  -- in the future so predict the quantity
                  PredictUsageBetweenDt(
                        an_AcftDbId,
                        an_AcftId,
                        lrec_USDeadlines.data_type_db_id,
                        lrec_USDeadlines.data_type_id,
                        SYSDATE,
                        lrec_USDeadlines.Start_Dt,
                        lrec_USDeadlines.Tsn_Qt,
                        lf_StartQt,
                        on_Return
                     );
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
            -- in the past, so go see what was the value back then

            IF ( lrec_USDeadlines.inv_class_db_id = 0 AND lrec_USDeadlines.inv_class_cd = 'TRK' ) THEN
               -- For TRK components there is no stored historic usage so
               -- use the last calculated start quantity (or 0 if doesn't exist).
               prep_deadline_pkg.GetTasksUsageStartQt(
                  an_SchedDbId,
                  an_SchedId,
                  lrec_USDeadlines.data_type_db_id,
                  lrec_USDeadlines.data_type_id,
                  lf_StartQt,
                  on_Return);
            ELSE
               prep_deadline_pkg.GetHistoricUsageAtDt(
                     lrec_USDeadlines.Start_Dt,
                     lrec_USDeadlines.data_type_db_id,
                     lrec_USDeadlines.data_type_id,
                     lrec_USDeadlines.Inv_No_Db_Id,
                     lrec_USDeadlines.Inv_No_Id,
                     lf_StartQt,
                     on_Return
                  );
            END IF;
            
            IF on_Return < 0 THEN
               RETURN;
            END IF;

            IF lrec_USDeadlines.Sched_From_Cd = 'WPSTART' OR
               lrec_USDeadlines.Sched_From_Cd = 'WPEND'   OR
               lrec_USDeadlines.Sched_From_Cd = 'LASTDUE'   THEN

               /* (now consider the scheduling window)
                * if the completion fell within the window, use scheduled completion information
                */
               OPEN lcur_SchedulingWindow(
                     lrec_USDeadlines.Event_Db_Id,
                     lrec_USDeadlines.Event_Id,
                     lrec_USDeadlines.data_type_db_id,
                     lrec_USDeadlines.data_type_id,
                     lf_StartQt
                  );
               FETCH lcur_SchedulingWindow INTO lrec_SchedulingWindow ;
               IF lcur_SchedulingWindow%FOUND THEN
                  -- the task is using either WPSTART/WPEND, and it is within the window
                  IF lrec_SchedulingWindow.is_within = 1 THEN
                     lf_StartQt     := lrec_SchedulingWindow.sched_dead_qt;
                     lv_SchedFromCd := 'LASTDUE';
                  ELSE
                     -- the value is not within the window, so reset to from LASTDUE to either WPEND/WPSTART
                     lv_SchedFromCd := lrec_SchedulingWindow.Resched_From_Cd;
                  END IF;
               END IF;
               CLOSE lcur_SchedulingWindow;
            END IF;
         END IF;

         -- the start value might have changed, recalculate the new deadline in this case
         lf_SchedDeadQt := lf_StartQt + lrec_USDeadlines.Interval_Qt;
      END IF;

      lf_UsageRem := lf_SchedDeadQt - lrec_USDeadlines.Tsn_Qt;
       
      -- the usage remaining until the extended deadline (use this for sched_dead_dt)
      lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;

      -- see if we have already calculated this value before
      ls_HashKey := lf_RootLevel ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                       lf_UsageRemExtended;

      IF an_AcftDbId IS NULL THEN
         ld_SchedDeadDt := NULL;

      ELSIF at_GetDt.EXISTS(ls_HashKey) THEN
         ld_SchedDeadDt := at_GetDt(ls_HashKey);

      ELSE
         -- find the deadline date
         findForecastedDeadDt_new(
               an_AcftDbId,
               an_AcftId,
               lrec_USDeadlines.data_type_db_id,
               lrec_USDeadlines.data_type_id,
               lf_UsageRemExtended,
               ld_BeginDate,
               ld_SchedDeadDt,
               lf_USOverlap,
               on_Return
            );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         
         -- save the value so we don't have to recalculate it again later
         at_GetDt(ls_HashKey) := ld_SchedDeadDt;
         IF lf_UsageRemExtended > 0 THEN
            ls_HashKey := lf_RootLevel ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
            
            -- schedule the next task from the extended deadline quantity
            lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt + lrec_USDeadlines.deviation_qt;
            lt_LastBeginDt(ls_HashKey)   := ld_BeginDate;
            lt_LastBeginQt(ls_HashKey)   := lf_UsageRemExtended;
            lt_LastUSOverlap(ls_HashKey) := lf_USOverlap;
         END IF;

      END IF;

      -- check to see how many rows will be updated for usage deadlines
      SELECT
         COUNT(*) INTO ln_PreExecutionCount
      FROM
         evt_sched_dead
      WHERE
         evt_sched_dead.event_db_id     = lrec_USDeadlines.Event_Db_Id     AND
         evt_sched_dead.event_id        = lrec_USDeadlines.Event_Id        AND
         evt_sched_dead.data_type_db_id = lrec_USDeadlines.Data_Type_Db_Id AND
         evt_sched_dead.data_type_id    = lrec_USDeadlines.Data_Type_Id
         AND
         (
            NOT (
               evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
               evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
               evt_sched_dead.sched_dead_qt = lf_SchedDeadQt AND
               evt_sched_dead.start_qt      = lf_StartQt     AND
               evt_sched_dead.sched_from_cd = lv_SchedFromCd
            )
            OR
            (
               evt_sched_dead.sched_dead_dt IS NULL AND
               ld_SchedDeadDt IS NOT NULL
            )
            OR
            (
               evt_sched_dead.sched_dead_dt IS NOT NULL AND
               ld_SchedDeadDt IS NULL
            )
            OR
            evt_sched_dead.usage_rem_qt  IS NULL
            OR
            evt_sched_dead.sched_dead_qt IS NULL
            OR
            evt_sched_dead.start_qt      IS NULL
         );

      IF ln_PreExecutionCount = 1 THEN
         /* update the evt_sched_dead */
         UPDATE
            evt_sched_dead
         SET
            evt_sched_dead.sched_dead_dt              = ld_SchedDeadDt,
            evt_sched_dead.usage_rem_qt               = lf_UsageRem,
            evt_sched_dead.sched_dead_dt_last_updated = SYSDATE,
            evt_sched_dead.sched_dead_qt              = lf_SchedDeadQt,
            evt_sched_dead.start_qt                   = lf_StartQt,
            evt_sched_dead.sched_from_cd              = lv_SchedFromCd
         WHERE
            evt_sched_dead.event_db_id     = lrec_USDeadlines.Event_Db_Id     AND
            evt_sched_dead.event_id        = lrec_USDeadlines.Event_Id        AND
            evt_sched_dead.data_type_db_id = lrec_USDeadlines.Data_Type_Db_Id AND
            evt_sched_dead.data_type_id    = lrec_USDeadlines.Data_Type_Id
            AND
            evt_sched_dead.revision_dt     = lrec_USDeadlines.sched_dead_revision_dt;

         /* check to see if a row was updated when using the revision_dt in the where clause */
         IF (SQL%ROWCOUNT = 0) THEN
            GOTO retry;
         END IF;
      END IF;

   END LOOP;

   -- process the forecast tasks
   FOR lrec_DepTasks IN lcur_DepTasks LOOP
      /**
       * The task dependencies are updated using a depth-first algorithm as demonstrated below:
       *
       * Task A (first processed, level 1)
       *   Task B (second processed, level 2)
       *     Task C (third processed, level 3)
       *   Task D (fourth processed, level 2)
       *     Task E (fifth processed, level 3)
       *
       * Since Task D's values depend on Task A's values, we cannot store the values in a hash table using the hash key "data_type_key" as
       * it would retrieve Task C's values. Given that the algorithm uses a depth-first algorithm, we can use the hash key "level:data_type_key". When
       * Task D is processed, it will look up the Task A's value and override the hash value stored by Task B. All tasks dependent on Task B is already
       * processed. This algorithm allows us to obtain the previous dependent tasks without causing performance problems.
       */

       -- process the CA deadine if there is one
      OPEN lcur_CADeadlines(lrec_DepTasks.Rel_Event_Db_Id, lrec_DepTasks.Rel_Event_Id);
      FETCH lcur_CADeadlines INTO lrec_CADeadlines ;
      IF lcur_CADeadlines%FOUND THEN
         ls_HashKey := lrec_DepTasks.level ||':'|| lrec_CADeadlines.Data_Type_Db_Id || ':' || lrec_CADeadlines.Data_Type_Id;
         ld_StartDt := NULL;
         FOR lf_PrevLevel IN REVERSE lf_RootLevel..(lrec_DepTasks.level-1) LOOP
            ls_PrevHashKey := lf_PrevLevel ||':'|| lrec_CADeadlines.Data_Type_Db_Id || ':' || lrec_CADeadlines.Data_Type_Id;

            -- if the forecast task has the same CA deadline as the previous task, start at the due date of the previous.
            IF lt_LastCADue.EXISTS(ls_PrevHashKey) THEN
               ld_StartDt := lt_LastCADue(ls_PrevHashKey);
            END IF;
            EXIT WHEN ld_StartDt IS NOT NULL;
         END LOOP;

         IF ld_StartDt IS NULL THEN
            ld_StartDt := lrec_CADeadlines.Start_Dt;
         END IF;

         /* Only round non Calendar Hour based dates */
         IF (NOT UPPER(lrec_CADeadlines.Data_Type_Cd) = 'CHR') THEN
            ld_StartDt := TO_DATE(TO_CHAR(ld_StartDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
         END IF;

         /* use the function that adds the interval to the start date */
         ld_SchedDeadDt := getDeadlineDt(
               lrec_CADeadlines.Interval_Qt,
               ld_StartDt,
               lrec_CADeadlines.Data_Type_Cd,
               lrec_CADeadlines.Ref_Mult_Qt
            );

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
           
         /*check to see how many rows will be updated for calendar deadlines*/
         SELECT
            COUNT(*) INTO ln_PreExecutionCount
         FROM
            evt_sched_dead
         WHERE
            evt_sched_dead.event_db_id     = lrec_CADeadlines.Event_Db_Id     AND
            evt_sched_dead.event_id        = lrec_CADeadlines.Event_Id        AND
            evt_sched_dead.data_type_db_id = lrec_CADeadlines.Data_Type_Db_Id AND
            evt_sched_dead.data_type_id    = lrec_CADeadlines.Data_Type_Id
            AND
            (
               NOT (
                  evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                  evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
                  evt_sched_dead.start_dt      = ld_StartDt
               )
               OR
               evt_sched_dead.sched_dead_dt IS NULL
               OR
               evt_sched_dead.usage_rem_qt  IS NULL
               OR
               evt_sched_dead.Start_Dt      IS NULL
            );

         IF(ln_PreExecutionCount = 1)THEN
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
               evt_sched_dead.revision_dt = lrec_CADeadlines.sched_dead_revision_dt;

            /* check to see if a row was updated when using the revision_dt in the where clause */
            IF (SQL%ROWCOUNT = 0) THEN
               GOTO retry;
            ELSE
               -- schedule the next task from the extended deadline date
               -- however, if the schedule to plan high value is greater than or equal to the deviation value
               -- then the next task is scheduled from the due date (instead of the extended deadline date)
               IF lrec_CADeadlines.postfixed_qt >= lrec_CADeadlines.deviation_qt THEN
                  lt_LastCADue(ls_HashKey) := ld_SchedDeadDt;
               ELSE
                  lt_LastCADue(ls_HashKey) :=
                        getExtendedDeadlineDt(
                              lrec_CADeadlines.deviation_qt,
                              ld_SchedDeadDt,
                              'CA',
                              lrec_CADeadlines.data_type_cd,
                              lrec_CADeadlines.ref_mult_qt
                           );
               END IF;
            END IF;
         END IF;
         
      END IF;
      CLOSE lcur_CADeadlines;

      -- process the US deadlines of the forecast task
      FOR lrec_USDeadlines IN lcur_USDeadlines (lrec_DepTasks.Rel_Event_Db_Id, lrec_DepTasks.Rel_Event_Id) LOOP
         ls_HashKey := lrec_DepTasks.level ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
         -- if the forecast task has the same US deadline as the previous task, start at the due qt of the previous.
         lf_StartQt := NULL;
         ld_BeginDate := NULL;
         FOR lf_PrevLevel IN REVERSE lf_RootLevel..(lrec_DepTasks.level-1) LOOP
            ls_PrevHashKey := lf_PrevLevel ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
            IF lt_LastUSDue.EXISTS(ls_PrevHashKey) THEN
               lf_StartQt   := lt_LastUSDue(ls_PrevHashKey);
            END IF;

            IF lt_LastBeginDt.EXISTS(ls_PrevHashKey) THEN
               ld_BeginDate := lt_LastBeginDt(ls_PrevHashKey);
               lf_USOverlap := lt_LastUSOverlap(ls_PrevHashKey);
               lf_BeginQt   := lt_LastBeginQt(ls_PrevHashKey);
            END IF;
            
            EXIT WHEN lf_StartQt IS NOT NULL;
         END LOOP;
         
         IF lf_StartQt IS NULL THEN
            lf_StartQt := lrec_USDeadlines.Start_Qt;
         END IF;
         
         IF ld_BeginDate IS NULL THEN
            ld_BeginDate := SYSDATE;
            lf_BeginQt   := 0;
            lf_USOverlap := 0;
         END IF;

         lf_SchedDeadQt := lf_StartQt + lrec_USDeadlines.Interval_Qt;

         lf_UsageRem := lf_SchedDeadQt - lrec_USDeadlines.Tsn_Qt;

         -- the usage remaining until the extended deadline (use this for sched_dead_dt)
         lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;

         -- see if we have already calculated this value before
         ls_HashKey := lrec_DepTasks.level ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id || ':' ||
                       lf_UsageRemExtended;

         IF an_AcftDbId IS NULL THEN
            -- no aircraft then no sched_dead_dt
            ld_SchedDeadDt := NULL;
         ELSIF lf_UsageRemExtended <= 0 THEN
            -- overdue usage based rules are always due yesterday
            ld_SchedDeadDt := TO_DATE(TO_CHAR(SYSDATE-1, 'DD-MON-YYYY ')||'23:59:59','DD-MON-YYYY HH24:MI:SS');
         ELSIF an_Forecast = 0 THEN
            -- flagged not to update the estimated date
            ld_SchedDeadDt := lrec_USDeadlines.sched_dead_dt;
         ELSIF at_GetDt.EXISTS(ls_HashKey) THEN
            ld_SchedDeadDt := at_GetDt(ls_HashKey);

         ELSE
            -- the difference between the deadline date of the forecast task and the previous deadline date
            lf_UsageRemExtended := lf_UsageRemExtended + lf_USOverlap - lf_BeginQt;

            findForecastedDeadDt_new(
                  an_AcftDbId,
                  an_AcftId,
                  lrec_USDeadlines.data_type_db_id,
                  lrec_USDeadlines.data_type_id,
                  lf_UsageRemExtended,
                  ld_BeginDate,
                  ld_SchedDeadDt,
                  lf_USOverlap,
                  on_Return
               );
            IF on_Return < 0 THEN
               RETURN;
            END IF;

            lf_UsageRemExtended := lf_UsageRem + lrec_USDeadlines.deviation_qt ;
            at_GetDt(ls_HashKey) := ld_SchedDeadDt;
            ls_HashKey := lrec_DepTasks.level ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
            lt_LastBeginDt(ls_HashKey)   := ld_BeginDate;
            lt_LastBeginQt(ls_HashKey)   := lf_UsageRemExtended;
            lt_LastUSOverlap(ls_HashKey) := lf_USOverlap;
         END IF;

         ls_HashKey := lrec_DepTasks.level ||':'|| lrec_USDeadlines.Data_Type_Db_Id || ':' || lrec_USDeadlines.Data_Type_Id;
         -- schedule the next task from the extended deadline quantity
         lt_LastUSDue(ls_HashKey)     := lf_SchedDeadQt + lrec_USDeadlines.deviation_qt;

         SELECT
            COUNT(*) INTO ln_PreExecutionCount
         FROM
            evt_sched_dead
         WHERE
            evt_sched_dead.event_db_id     = lrec_USDeadlines.Event_Db_Id     AND
            evt_sched_dead.event_id        = lrec_USDeadlines.Event_Id        AND
            evt_sched_dead.data_type_db_id = lrec_USDeadlines.Data_Type_Db_Id AND
            evt_sched_dead.data_type_id    = lrec_USDeadlines.Data_Type_Id
            AND
            (
               NOT (
                  evt_sched_dead.sched_dead_dt = ld_SchedDeadDt AND
                  evt_sched_dead.usage_rem_qt  = lf_UsageRem    AND
                  evt_sched_dead.sched_dead_qt = lf_SchedDeadQt AND
                  evt_sched_dead.start_qt      = lf_StartQt
               )
               OR
               (
                  evt_sched_dead.sched_dead_dt IS NULL AND
                  ld_SchedDeadDt IS NOT NULL
               )
               OR
               (
                  evt_sched_dead.sched_dead_dt IS NOT NULL AND
                  ld_SchedDeadDt IS NULL
               )
               OR
               evt_sched_dead.usage_rem_qt  IS NULL
               OR
               evt_sched_dead.sched_dead_qt IS NULL
               OR
               evt_sched_dead.start_qt      IS NULL
            );

         IF ln_PreExecutionCount = 1 THEN
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
               evt_sched_dead.revision_dt = lrec_USDeadlines.sched_dead_revision_dt;
                        
            /* check to see if a row was updated when using the revision_dt in the where clause */
            IF (SQL%ROWCOUNT = 0) THEN
               GOTO retry;
            END IF;
         END IF;
      END LOOP;
   END LOOP;

   /* return success */
   on_Return := icn_Success;
  
   SELECT
      evt_event.hist_bool
   INTO
      lb_HistoricalTask
   FROM
      evt_event
   WHERE
      evt_event.event_db_id = an_SchedDbId AND
      evt_event.event_id    = an_SchedId
      ;

   IF lb_HistoricalTask = 1 THEN
      RAISE first_task_not_actv_exception;
   END IF;

EXCEPTION
   WHEN max_retries_exception THEN
      on_Return := -200;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForTask@@@' || 'Another process is trying to update deadlines.  Maximum retries exceeded.');
      IF lcur_CADeadlines%ISOPEN THEN CLOSE lcur_CADeadlines; END IF;
      IF lcur_USDeadlines%ISOPEN THEN CLOSE lcur_USDeadlines; END IF;
      IF lcur_DepTasks%ISOPEN THEN CLOSE lcur_DepTasks; END IF;
      IF lcur_SchedulingWindow%ISOPEN THEN CLOSE lcur_SchedulingWindow; END IF;
      RETURN;
   WHEN first_task_not_actv_exception THEN
      on_Return := -200;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForTask@@@' || 'Another process is trying to update deadlines.');
      IF lcur_CADeadlines%ISOPEN THEN CLOSE lcur_CADeadlines; END IF;
      IF lcur_USDeadlines%ISOPEN THEN CLOSE lcur_USDeadlines; END IF;
      IF lcur_DepTasks%ISOPEN THEN CLOSE lcur_DepTasks; END IF;
      IF lcur_SchedulingWindow%ISOPEN THEN CLOSE lcur_SchedulingWindow; END IF;
      RETURN;
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@UpdateDeadlinesForTask@@@' || SQLERRM);
      IF lcur_CADeadlines%ISOPEN THEN CLOSE lcur_CADeadlines; END IF;
      IF lcur_USDeadlines%ISOPEN THEN CLOSE lcur_USDeadlines; END IF;
      IF lcur_DepTasks%ISOPEN THEN CLOSE lcur_DepTasks; END IF;
      IF lcur_SchedulingWindow%ISOPEN THEN CLOSE lcur_SchedulingWindow; END IF;
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

   ln_DataTypeDbId   mim_data_type.data_type_db_id%TYPE;
   ln_DataTypeId     mim_data_type.data_type_id%TYPE;
   lf_UsageRemQt     evt_sched_dead.usage_rem_qt%TYPE;
   ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
   ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
   ln_TempPriority   NUMBER;

 
   /* deadlines for this task, ordered by the deadline date
      ordering is done based on the last sched boolean flag */
   CURSOR lcur_LastDue IS
      SELECT
                  evt_sched_dead.data_type_db_id,
                  evt_sched_dead.data_type_id,
                  evt_sched_dead.usage_rem_qt,
                  evt_sched_dead.notify_qt,
                  evt_sched_dead.deviation_qt,
                  CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt
                       THEN 0
                       ELSE 1
                  END AS is_positive,
                  getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                         evt_sched_dead.sched_dead_dt,
                                         mim_data_type.domain_type_cd,
                                         mim_data_type.data_type_cd,
                                         ref_eng_unit.ref_mult_qt
                  ) AS extended_dt
           FROM
                  evt_sched_dead,
                  mim_data_type,
                  ref_eng_unit
           WHERE
                  evt_sched_dead.event_db_id   = an_SchedDbId AND
                  evt_sched_dead.event_id      = an_SchedId
                  AND
                  mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                  mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                  AND
                  ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
                  ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
           ORDER BY extended_dt DESC NULLS LAST, is_positive DESC, evt_sched_dead.sched_driver_bool DESC;
   lrec_LastDue lcur_LastDue%ROWTYPE;
   
   CURSOR lcur_FirstDue IS
      SELECT
                  evt_sched_dead.data_type_db_id,
                  evt_sched_dead.data_type_id,
                  evt_sched_dead.usage_rem_qt,
                  evt_sched_dead.notify_qt,
                  evt_sched_dead.deviation_qt,
                  CASE WHEN (-1)*evt_sched_dead.usage_rem_qt >= evt_sched_dead.deviation_qt
                       THEN 0
                       ELSE 1
                  END AS is_positive,
                  getExtendedDeadlineDt( evt_sched_dead.deviation_qt,
                                         evt_sched_dead.sched_dead_dt,
                                         mim_data_type.domain_type_cd,
                                         mim_data_type.data_type_cd,
                                         ref_eng_unit.ref_mult_qt
                  ) AS extended_dt
           FROM
                  evt_sched_dead,
                  mim_data_type,
                  ref_eng_unit
           WHERE
                  evt_sched_dead.event_db_id   = an_SchedDbId AND
                  evt_sched_dead.event_id      = an_SchedId
                  AND
                  mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                  mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                  AND
                  ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
                  ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
           ORDER BY is_positive, extended_dt NULLS LAST, evt_sched_dead.sched_driver_bool DESC;
   lrec_FirstDue lcur_FirstDue%ROWTYPE;
BEGIN

    on_Return := 0;

    ln_TempPriority := 1;

    IF ab_LastSchedBool = 1 THEN
    
      OPEN lcur_LastDue;
      FETCH lcur_LastDue INTO lrec_LastDue;
      IF lcur_LastDue%FOUND THEN
         ln_DataTypeDbId := lrec_LastDue.Data_Type_Db_Id;
         ln_DataTypeId   := lrec_LastDue.Data_Type_Id;
         ln_DeviationQt  := lrec_LastDue.Deviation_Qt;
         ln_NotifyQt     := lrec_LastDue.Notify_Qt;
         lf_UsageRemQt   := lrec_LastDue.Usage_Rem_Qt;
      END IF;
      CLOSE lcur_LastDue;
    ELSE
   
      OPEN lcur_FirstDue;
      FETCH lcur_FirstDue INTO lrec_FirstDue;
      IF lcur_FirstDue%FOUND THEN
         ln_DataTypeDbId := lrec_FirstDue.Data_Type_Db_Id;
         ln_DataTypeId   := lrec_FirstDue.Data_Type_Id;
         ln_DeviationQt  := lrec_FirstDue.Deviation_Qt;
         ln_NotifyQt     := lrec_FirstDue.Notify_Qt;
         lf_UsageRemQt   := lrec_FirstDue.Usage_Rem_Qt;
      END IF;
      CLOSE lcur_FirstDue;
    END IF;
   
    IF ln_DataTypeDbId IS NOT NULL THEN

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
      IF lcur_FirstDue%ISOPEN THEN CLOSE lcur_FirstDue; END IF;
      IF lcur_LastDue%ISOPEN  THEN CLOSE lcur_LastDue; END IF;
      RETURN;
END SetDriverPriorityForTask;


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
                   ref_task_class.class_mode_cd,
                   sched_stask.soft_deadline_bool,
                   sched_stask.main_inv_no_db_id,
                   sched_stask.main_inv_no_id,
                   evt_sched_dead.data_type_db_id,
                   DECODE (evt_event.event_status_cd, 'FORECAST', 1, 0) AS is_forecast
            FROM
                   ref_task_class,
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
                   AND
                   ref_task_class.task_class_db_id (+)= task_task.task_class_db_id AND
                   ref_task_class.task_class_cd    (+)= task_task.task_class_cd
          )
          SELECT
                   acft_tasks.nh_event_db_id,
                   acft_tasks.event_db_id,
                   acft_tasks.event_id,
                   acft_tasks.task_class_cd,
                   acft_tasks.last_sched_dead_bool,
                   acft_tasks.class_mode_cd,
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

       IF lrec_Tasks.Nh_Event_Db_Id IS NOT NULL AND lrec_Tasks.class_mode_cd = 'BLOCK' THEN
          -- 4. set the driving task for the BLOCK task
          SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 0, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
             RETURN;
          END IF;
       END IF;
       
       IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN
          -- 4. set the driving task for the root task
          SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 1, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
             RETURN;
          END IF;

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
                   ref_task_class.class_mode_cd,
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
                   ref_task_class,
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
                   AND
                   ref_task_class.task_class_db_id (+)= task_task.task_class_db_id AND
                   ref_task_class.task_class_cd    (+)= task_task.task_class_cd
            GROUP BY
                   evt_event.nh_event_db_id,
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0),
                   ref_task_class.class_mode_cd,
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
                   loose_component_tasks.class_mode_cd,
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

      IF lrec_Tasks.Nh_Event_Db_Id IS NOT NULL AND lrec_Tasks.class_mode_cd = 'BLOCK' THEN
         -- 3. set the driving task for the BLOCK task
         SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 0, on_Return);
         IF on_Return<0 THEN
            Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
            RETURN;
         END IF;
      END IF;
      
      -- the following logic applies only to root tasks
      IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN

         -- 3. set the driving task for the root task
         SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 1, on_Return);
         IF on_Return<0 THEN
            Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
            RETURN;
         END IF;

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
                   ref_task_class.class_mode_cd,
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
                   ref_task_class,
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
                   AND
                   ref_task_class.task_class_db_id (+)= task_task.task_class_db_id AND
                   ref_task_class.task_class_cd    (+)= task_task.task_class_cd
            GROUP BY
                   evt_event.nh_event_db_id,
                   evt_event.nh_event_id,
                   evt_event.event_db_id,
                   evt_event.event_id,
                   sched_stask.task_class_cd,
                   NVL(task_task.last_sched_dead_bool, 0),
                   ref_task_class.class_mode_cd,
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
                   attchd_component_tasks.class_mode_cd,
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

       IF lrec_Tasks.Nh_Event_Db_Id IS NOT NULL AND lrec_Tasks.class_mode_cd = 'BLOCK' THEN
          -- 3. set the driving task for the BLOCK task
          SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 0, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
             RETURN;
          END IF;
       END IF;
       
       -- the following logic applies only to root tasks
       IF lrec_Tasks.Nh_Event_Db_Id IS NULL THEN

          -- 3. set the driving task for the root task
          SetDRVTASKForRootOrBlock(lrec_Tasks.Event_Db_Id, lrec_Tasks.Event_Id, lrec_Tasks.Task_Class_Cd, lrec_Tasks.is_forecast, 1, on_Return);
          IF on_Return<0 THEN
             Application_Object_Pkg.appendmxierror('for task ' || ln_EventDbId || ':' || ln_EventId);
             RETURN;
          END IF;

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

   IF on_Return < 0 THEN
      RETURN;
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

--changeSet MX-28608:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY SCHED_STASK_PKG IS

/* *** ARRAY TYPES *** */
TYPE typtabn_DbId       IS TABLE OF inv_inv.inv_no_db_id%TYPE     INDEX BY BINARY_INTEGER;
TYPE typtabn_Id         IS TABLE OF inv_inv.inv_no_id%TYPE        INDEX BY BINARY_INTEGER;

/* *** Private procedure declarations *** */

/* procedure used to check for non-historical MOD tasks */
PROCEDURE CheckForNonHistMOD(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      on_TaskExists          OUT typn_RetCode,
      on_Return              OUT typn_RetCode );

/* Check that the inventory's part number is valid when
   you are creating a modification task */
PROCEDURE CheckModPartNum(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_MatchingPartNo      OUT typn_RetCode,
      on_Return              OUT typn_RetCode );


/* Check that the inventory and the task definition have matching boms */
PROCEDURE CheckTaskPartNo(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_TaskApplies         OUT typn_RetCode,
      on_Return              OUT typn_RetCode );

/* create all auto-apply children for a new task */
PROCEDURE AutoCreateChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode );

/* create all on condition children for a new mandatory block */
PROCEDURE AutoCreateOnCondChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode );

/* This procedure will create all of the rows in SCHED_PART that come
   from the baseline task definition */
PROCEDURE GenSchedParts (
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return      OUT typn_RetCode );

/* *** Procedure Bodies *** */


/********************************************************************************
*
* Procedure:    GenSchedTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
* Description:  This procedure is used to generate a new Task record. The task
*               will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask. If you do, then the
*               deadlines for this task will be initialized based on the scheduled
*               deadlines for the previous task.
*
* Orig.Coder:     Siku Adam
* Recent Coder:   Natasa Subotic
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenSchedTask(
        an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
   ) IS

   /* *** DECLARE LOCAL VARIABLES *** */
   ls_ExitCd               VARCHAR2(8);
   lb_TempPartReady        sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady        sched_stask.tools_ready_bool%TYPE;


   /* cursor used to get task_defn class class code and class mode code */
   CURSOR lcur_TaskDefinitionInfo (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT
         task_task.task_class_cd,
         task_task.on_condition_bool,
         ref_task_class.class_mode_cd
      FROM
         task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd      = task_task.task_class_cd
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinitionInfo  lcur_TaskDefinitionInfo%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get the task definition class info */
   OPEN  lcur_TaskDefinitionInfo( an_TaskDbId, an_TaskId );
   FETCH lcur_TaskDefinitionInfo INTO lrec_TaskDefinitionInfo;
   CLOSE lcur_TaskDefinitionInfo;

   /* Create one scheduled task. If the previous task IS NOT provided new task will have status ACTV,
      otherwise the status will be FORECAST */
   GenOneSchedTask (
         an_EvtEventDbId,
         an_EvtEventId,
         an_InvNoDbId,
         an_InvNoId,
         an_TaskDbId,
         an_TaskId,
         an_PreviousTaskDbId,
         an_PreviousTaskId,
         ad_CompletionDate,
         an_ReasonDbId,
         an_ReasonCd,
         as_UserNote,
         an_HrDbId,
         an_HrId,
         ab_CalledExternally,
         ab_Historic,
         ab_CreateNATask,
         on_SchedDbId,
         on_SchedId,
         on_Return );

   IF on_Return < 1 THEN
      RETURN;
   END IF;

      IF ( on_SchedDbId IS NOT NULL AND on_SchedId IS NOT NULL ) THEN

      /* If a previous task was given, then add a row to the evt_event_rel table */
      IF ( an_PreviousTaskDbId <> -1 ) THEN
         CreateTaskDependencyLink(an_PreviousTaskDbId, an_PreviousTaskId, on_SchedDbId, on_SchedId, on_Return);
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;


      /* For Non-Historic Tasks: */
      IF ab_historic = false THEN

         /* Create Task Deadlines */
         PREP_DEADLINE_PKG.PrepareSchedDeadlines( on_SchedDbId, on_SchedId, NULL, NULL, true, ad_PreviousCompletionDt, on_Return );
         IF on_Return < 1 THEN
            RETURN;
         END IF;

         /*
          * Create all child tasks, but only for REQs
          * Should not auto-create a job card under a CORR task
          */
         IF ( lrec_TaskDefinitionInfo.class_mode_cd = 'REQ' AND lrec_TaskDefinitionInfo.task_class_cd <> 'CORR') THEN
            AutoCreateChildTasks( on_SchedDbId, on_SchedId, on_Return );
            IF on_Return < 1 THEN
               RETURN;
            END IF;

            -- Evaluate part/tool readiness for this task
            UpdatePartsAndToolsReadyBool(
                  on_SchedDbId,
                  on_SchedId,
                  lb_TempPartReady,
                  lb_TempToolReady,
                  on_Return
               );
            IF on_Return < 1 THEN
               RETURN;
            END IF;

            -- if we are creating a replacement
            IF lrec_TaskDefinitionInfo.task_class_cd = 'REPL' THEN

               --update the replacement part requirement for it and its children
               UpdateReplSchedPart(
                     on_SchedDbId,
                     on_SchedId,
                     on_Return
                  );
               IF on_Return < 1 THEN
                  RETURN;
               END IF;
            END IF;
         /* else if it is a mandatory block */
         ELSIF lrec_TaskDefinitionInfo.class_mode_cd = 'BLOCK' AND lrec_TaskDefinitionInfo.on_condition_bool = 0 THEN
            AutoCreateOnCondChildTasks( on_SchedDbId, on_SchedId, on_Return );
            IF on_Return < 1 THEN
               RETURN;
            END IF;
         END IF;


           /* Update the references to the root task (non-check) */
           UpdateHSched( on_SchedDbId, on_SchedId, on_Return );
           IF on_Return < 1 THEN
              RETURN;
           END IF;

            /* Generate all of the dependendent Forecasted Tasks */
           GenForecastedTasks( on_SchedDbId, on_SchedId, ls_ExitCd, on_Return );
           IF on_Return < 1 THEN
              RETURN;
           END IF;

         END IF;

      END IF;

   /* record the event_id as this will ultimately return the highest event_id (original request) */
   on_Return    := icn_Success;

END GenSchedTask;


/********************************************************************************
*
* Procedure:    GenServiceTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
*
* Description:  This procedure is used to generate new scheduled tasks for a service
*                check.
*               Tasks will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask and an_NextTask. If you do,
*               only one new task will be created with status FORECAST and it will be
*               inserted in between an_PreviousTask and an_NextTask. If previous and next
*               task are not provided, the whole chain with ACTV and FORECAST tasks will be created.
*
* Orig.Coder:   Natasa Subotic
*
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenServiceTask(
      an_EvtEventDbId         IN evt_event.event_db_id%TYPE,
      an_EvtEventId           IN evt_event.event_id%TYPE,
      an_InvNoDbId            IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId              IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId             IN sched_stask.task_db_id%TYPE ,
      an_TaskId               IN sched_stask.task_id%TYPE,
      an_PreviousTaskDbId     IN evt_event.event_db_id%TYPE,
      an_PreviousTaskId       IN evt_event.event_id%TYPE,
      an_NextTaskDbId         IN evt_event.event_db_id%TYPE,
      an_NextTaskId           IN evt_event.event_id%TYPE,
      ad_CompletionDate       IN evt_event.event_dt%TYPE,
      an_ReasonDbId           IN evt_stage.stage_reason_db_id%TYPE,
      an_ReasonCd             IN evt_stage.stage_reason_cd%TYPE,
      as_UserNote             IN evt_stage.user_stage_note%TYPE,
      an_HrDbId               IN org_hr.hr_db_id%TYPE,
      an_HrId                 IN org_hr.hr_id%TYPE,
      ab_CalledExternally     IN BOOLEAN,
      ab_Historic             IN BOOLEAN,
      ab_CreateNATask         IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_SchedDbId            OUT evt_event.event_db_id%TYPE,
      on_SchedId              OUT evt_event.event_id%TYPE,
      on_Return               OUT typn_RetCode
   ) IS

   lb_TempPartReady        sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady        sched_stask.tools_ready_bool%TYPE;

   /* cursor used to get task_defn class class code and class mode code */
   CURSOR lcur_TaskDefinitionInfo (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT
         task_task.task_class_cd,
         task_task.on_condition_bool,
         ref_task_class.class_mode_cd
      FROM
         task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd      = task_task.task_class_cd
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinitionInfo  lcur_TaskDefinitionInfo%ROWTYPE;

BEGIN

   /* get the task definition class info */
   OPEN  lcur_TaskDefinitionInfo( an_TaskDbId, an_TaskId );
   FETCH lcur_TaskDefinitionInfo INTO lrec_TaskDefinitionInfo;
   CLOSE lcur_TaskDefinitionInfo;


   /* if previous and next tasks are provided create only one FORECAST task */
   IF ( an_PreviousTaskDbId <> -1 AND an_PreviousTaskId <> -1 AND an_NextTaskDbId <> -1 AND an_NextTaskId <> -1 ) THEN
      GenOneSchedTask(
            an_EvtEventDbId,
            an_EvtEventId,
            an_InvNoDbId,
            an_InvNoId,
            an_TaskDbId,
            an_TaskId,
            an_PreviousTaskDbId,
            an_PreviousTaskId,
            ad_CompletionDate,
            an_ReasonDbId,
            an_ReasonCd,
            as_UserNote,
            an_HrDbId,
            an_HrId,
            ab_CalledExternally,
            ab_Historic,
            ab_CreateNATask,
            on_SchedDbId,
            on_SchedId,
            on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;

      /* insert task in between previous and next task */
      InsertTask(
            on_SchedDbId,
            on_SchedId,
            an_PreviousTaskDbId,
            an_PreviousTaskId,
            an_NextTaskDbId,
            an_NextTaskId,
            on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;


     /* For Non-Historic Tasks: */
     IF ab_historic = false THEN

        /* Create Task Deadlines */
        PREP_DEADLINE_PKG.PrepareSchedDeadlines( on_SchedDbId, on_SchedId, NULL, NULL, true, ad_PreviousCompletionDt, on_Return );
        IF on_Return < 1 THEN
          RETURN;
        END IF;

        /*
         * Create all on condition child tasks, but only for mandatory blocks
         */
        IF lrec_TaskDefinitionInfo.class_mode_cd = 'BLOCK' AND lrec_TaskDefinitionInfo.on_condition_bool = 0 THEN
            AutoCreateOnCondChildTasks( on_SchedDbId, on_SchedId, on_Return );
            IF on_Return < 1 THEN
               RETURN;
            END IF;
         END IF;


        /* Update the references to the root task (non-check) */
        UpdateHSched( on_SchedDbId, on_SchedId, on_Return );
        IF on_Return < 1 THEN
           RETURN;
        END IF;
     END IF;

    /* if previous and next tasks are not provided create chain with ACTV and FORECAST tasks */
   ELSE
      GenSchedTask(
          an_EvtEventDbId,
          an_EvtEventId,
          an_InvNoDbId,
          an_InvNoId,
          an_TaskDbId,
          an_TaskId,
          an_PreviousTaskDbId,
          an_PreviousTaskId,
          ad_CompletionDate,
          an_ReasonDbId,
          an_ReasonCd,
          as_UserNote,
          an_HrDbId,
          an_HrId,
          ab_CalledExternally,
          ab_Historic,
          ab_CreateNATask,
          ad_PreviousCompletionDt,
          on_SchedDbId,
          on_SchedId,
          on_Return
       );

       IF on_Return < 1 THEN
           RETURN;
       END IF;

   END IF;

   -- Return success
   on_Return := icn_Success;

END GenServiceTask;



/********************************************************************************
*
* Procedure:    InsertTask
* Arguments:
*        an_TaskDbId             (long) - The task that will be inserted in the existing chain of tasks
*        an_TaskId               (long) - ""
*        an_PreviousTaskDbId     (long) - The task that the new task will be inserted after
*        an_PreviousTaskId       (long) - ""
*        an_NextTaskDbId         (long) - The task taht the new task will be inserted before
*        an_NextTaskId           (long) - ""
* Return:
*        on_Return                (long) - Success/Failure of inserting the task
*
* Description:  This procedure is used to insert a new task in between two tasks in the existing chain.
*
* Orig.Coder:   Natasa Subotic
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InsertTask(
        an_TaskDbId           IN evt_event.event_db_id%TYPE,
        an_TaskId             IN evt_event.event_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        an_NextTaskDbId       IN evt_event.event_db_id%TYPE,
        an_NextTaskId         IN evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
   ) IS

 BEGIN

  -- Initialize the return value
   on_Return := icn_NoProc;

   /* Before inserting new task in between two tasks, delete relationship link between them */
   DELETE evt_event_rel
   WHERE
   event_db_id = an_PreviousTaskDbId AND
   event_id    = an_PreviousTaskId
   AND
   rel_event_db_id = an_NextTaskDbId AND
   rel_event_id    = an_NextTaskId
   AND
   evt_event_rel.rel_type_cd='DEPT';


   /* Create relationship between the previoius and the new task*/
   IF (  an_PreviousTaskDbId <> -1 ) THEN
      CreateTaskDependencyLink(an_PreviousTaskDbId,  an_PreviousTaskId,  an_TaskDbId, an_TaskId, on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

  /* Create relationship between the new and the next task */
   IF (  an_PreviousTaskDbId <> -1 ) THEN
      CreateTaskDependencyLink(an_TaskDbId,  an_TaskId,  an_NextTaskDbId, an_NextTaskId, on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','CreateTaskDependencyLink: '||SQLERRM);
     on_Return:= icn_Error;

END InsertTask;


/********************************************************************************
*
* Procedure:    GenOneSchedTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
* Description:  This procedure is used to generate a new Task record. The task
*               will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask. If you do, then the
*               deadlines for this task will be initialized based on the scheduled
*               deadlines for the previous task.
*
* Orig.Coder:     Natasa Subotic
*
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenOneSchedTask (
        an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
    )IS



    /* *** DECLARE LOCAL VARIABLES *** */

   ln_EventDbId            evt_event.event_db_id%TYPE;              /* event database id */
   ln_EventId              evt_event.event_id%TYPE;                 /* event id */
   ln_LabourId             sched_labour.labour_id%TYPE;             /* labour id of assigned labour */
   ln_ToolId               evt_tool.tool_id%TYPE;                   /* tool id of assigned tool */
   ln_LabourRoleId         sched_labour_role.labour_role_id%TYPE;   /* labour id of assigned labour */
   ln_LabourRoleStatusId   sched_labour_role_status.status_id%TYPE; /* labour role id of assigned labour */
   ln_StepId               sched_step.step_id%TYPE;                 /* step id of task step */
   ln_ParentTaskCount      NUMBER;                                  /* one if this task has a parent, 0 if it does not. */
   ls_TaskClassMode        ref_task_class.class_mode_cd%TYPE;       /* task class mode identifier */
   ln_TaskApplies          typn_RetCode;                            /* task applies return value. */
   ln_SubEventOrd          evt_event.sub_event_ord%TYPE;            /* child ordering number */
   ln_InitalTaskStatus     ref_event_status.event_status_cd%TYPE;   /* the initial event status */
   ln_PreviousTaskHistBool evt_event.hist_bool%TYPE;                /* historic value of the previous event */
   ls_UserStageNote        evt_stage.user_stage_note%TYPE;          /* user stage note */
   ls_TaskType             evt_stage.user_stage_note%TYPE;          /* the task type check, work order, task */
   ln_ExistsCount          NUMBER;
   ln_histBool             NUMBER;
   ln_woCommitLine         NUMBER;
   ln_PreviousTaskDbId     NUMBER DEFAULT -1;
   ln_PreviousTaskId       NUMBER DEFAULT -1;
   ln_StdPartNoDbId        NUMBER;
   ln_StdPartNoId          NUMBER;
   ln_count                NUMBER;
   ln_TaskClassRstatCd     NUMBER;
   ln_StageId              evt_stage.stage_id%TYPE;
   ln_PreventExeReqBool    sched_stask_flags.prevent_exe_bool%TYPE;
   ld_PreventExeReviewDt   sched_stask_flags.prevent_exe_review_dt%TYPE;
   ln_EngUnitDbId          mim_data_type.eng_unit_db_id%TYPE;
   ls_EngUnitCD            mim_data_type.eng_unit_cd%TYPE;

   /* *** DECLARE EXCEPTIONS *** */
   xc_UnknownSQLError         EXCEPTION;
   xc_InvHasIncorrectPartNo   EXCEPTION;
   xc_TaskDefnNotActive       EXCEPTION;
   xc_InvIsLocked             EXCEPTION;
   xc_TaskClassSoftDeleted    EXCEPTION;
   xc_TaskAlreadyExist        EXCEPTION;

   /* *** DECLARE CURSORS *** */
   /* cursor used to get task_defn info */
   CURSOR lcur_TaskDefinition (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT
         task_task.task_class_db_id,
         task_task.task_class_cd,
         task_task.task_subclass_db_id,
         task_task.task_subclass_cd,
         task_task.task_originator_db_id,
         task_task.task_originator_cd,
         task_task.bitmap_db_id,
         task_task.bitmap_tag,
         task_task.task_cd,
         task_task.task_name,
         task_task.task_priority_db_id,
         task_task.task_priority_cd,
         task_task.task_def_status_db_id,
         task_task.task_def_status_cd,
         task_task.task_ref_sdesc,
         task_task.recurring_task_bool,
         task_task.task_ldesc,
         task_task.routine_bool,
         task_task.effective_dt,
         task_task.resource_sum_bool,
         task_task.instruction_ldesc,
         task_task.unique_bool,
         task_task.auto_complete_bool,
         task_task.task_appl_eff_ldesc,
         task_task.est_duration_qt,
         task_task.min_plan_yield_pct,
         task_task.soft_deadline_bool,
         task_task.issue_account_db_id,
         task_task.issue_account_id,
         task_task.etops_bool,
         task_task.workscope_bool
      FROM
         task_task
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinition  lcur_TaskDefinition%ROWTYPE;

   /* cursor used to get inventory details */
   CURSOR lcur_Inventory (
         cn_InvNoDbId   inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId     inv_inv.inv_no_id%TYPE
      ) IS
      SELECT
         inv_inv.nh_inv_no_db_id,
         inv_inv.nh_inv_no_id,
         inv_inv.h_inv_no_db_id,
         inv_inv.h_inv_no_id,
         inv_inv.assmbl_inv_no_db_id,
         inv_inv.assmbl_inv_no_id,
         inv_inv.assmbl_db_id,
         inv_inv.assmbl_cd,
         inv_inv.assmbl_bom_id,
         inv_inv.assmbl_pos_id,
         inv_inv.part_no_db_id,
         inv_inv.part_no_id,
         inv_inv.bom_part_db_id,
         inv_inv.bom_part_id,
         inv_inv.locked_bool,
         ass_inv_inv.appl_eff_cd

      FROM
         inv_inv,
         inv_inv ass_inv_inv
      WHERE
         inv_inv.inv_no_db_id = cn_InvNoDbId AND
         inv_inv.inv_no_id    = cn_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         ass_inv_inv.inv_no_db_id  (+)= decode( inv_inv.inv_class_cd, 'ASSY',
                                           inv_inv.inv_no_db_id,
                                           inv_inv.assmbl_inv_no_db_id
                                        )
         AND
         ass_inv_inv.inv_no_id     (+)= decode (inv_inv.inv_class_cd, 'ASSY',
                                           inv_inv.inv_no_id,
                                           inv_inv.assmbl_inv_no_id
                                        );
   lrec_Inventory lcur_Inventory%ROWTYPE;

   /* used to get labour list for a task defn */
   CURSOR lcur_TaskLabour (
         an_TaskDbId    task_labour_list.task_db_id%TYPE,
         an_TaskId      task_labour_list.task_id%TYPE
      ) IS
      SELECT
         labour_skill_db_id,
         labour_skill_cd,
         man_pwr_ct,
         work_perf_hr,
         work_perf_bool,
         cert_hr,
         cert_bool,
         insp_hr,
         insp_bool
      FROM task_labour_list
      WHERE
         task_db_id = an_TaskDbId AND
         task_id    = an_TaskId;

   /* used to get tool list for a task defn */
   CURSOR lcur_Tool (
         an_TaskDbId task_tool_list.task_db_id%TYPE,
         an_TaskId   task_tool_list.task_id%TYPE
      ) IS
      SELECT
         task_tool_list.bom_part_db_id,
         task_tool_list.bom_part_id,
         task_tool_list.task_tool_id,
         task_tool_list.sched_hr
      FROM task_tool_list
      WHERE
         task_tool_list.task_db_id = an_TaskDbId AND
         task_tool_list.task_id    = an_TaskId;

   /* used to get steps for a task defn */
   CURSOR lcur_Step (
         an_TaskDbId task_step.task_db_id%TYPE,
         an_TaskId   task_step.task_id%TYPE
      ) IS
      SELECT
         task_step.step_ord,
         task_step.step_ldesc
      FROM task_step
      WHERE
         task_step.task_db_id = an_TaskDbId AND
         task_step.task_id    = an_TaskId;

   /* used to get zone list for a task defn */
   CURSOR lcur_Zone (
         an_TaskDbId task_zone.task_db_id%TYPE,
         an_TaskId   task_zone.task_id%TYPE
      ) IS
      SELECT
         task_zone.zone_db_id,
         task_zone.zone_id
      FROM task_zone
      WHERE
         task_zone.task_db_id = an_TaskDbId AND
         task_zone.task_id    = an_TaskId;

   /* used to get panel list for a task defn */
   CURSOR lcur_Panel (
         an_TaskDbId task_panel.task_db_id%TYPE,
         an_TaskId   task_panel.task_id%TYPE
      ) IS
      SELECT
         task_panel.panel_db_id,
         task_panel.panel_id
      FROM task_panel
      WHERE
         task_panel.task_db_id = an_TaskDbId AND
         task_panel.task_id    = an_TaskId;

   /* used to get ietm list for a task defn */
   CURSOR lcur_TaskIetm (
         an_TaskDbId task_task_ietm.task_db_id%TYPE,
         an_TaskId   task_task_ietm.task_id%TYPE
      ) IS
      SELECT
         task_task_ietm.task_ietm_id,
         task_task_ietm.ietm_db_id,
         task_task_ietm.ietm_id,
         task_task_ietm.ietm_topic_id,
         task_task_ietm.ietm_ord,
         ietm_topic.attach_blob,
         ietm_topic.print_bool
      FROM
         task_task_ietm
         INNER JOIN ietm_topic ON
            ietm_topic.ietm_db_id    = task_task_ietm.ietm_db_id AND
            ietm_topic.ietm_id       = task_task_ietm.ietm_id AND
            ietm_topic.ietm_topic_id = task_task_ietm.ietm_topic_id,
         inv_inv
         INNER JOIN inv_inv h_inv_inv ON
            h_inv_inv.inv_no_db_id    = inv_inv.h_inv_no_db_id AND
            h_inv_inv.inv_no_id       = inv_inv.h_inv_no_id    AND
            h_inv_inv.rstat_cd        =  0
         LEFT OUTER JOIN inv_inv assmbl_inv_inv ON
           assmbl_inv_inv.inv_no_db_id    = inv_inv.assmbl_inv_no_db_id AND
           assmbl_inv_inv.inv_no_id       = inv_inv.assmbl_inv_no_id    AND
           assmbl_inv_inv.rstat_cd        = 0
      WHERE
         task_task_ietm.task_db_id = an_TaskDbId AND
         task_task_ietm.task_id    = an_TaskId
         AND
         inv_inv.inv_no_db_id = an_InvNoDbId    AND
         inv_inv.inv_no_id    = an_InvNoId
         AND
         isIETMApplicable(inv_inv.inv_class_cd,inv_inv.appl_eff_cd,h_inv_inv.appl_eff_cd,assmbl_inv_inv.inv_no_db_id,assmbl_inv_inv.appl_eff_cd,ietm_topic.appl_eff_ldesc ) = 1
         AND
         (
            h_inv_inv.carrier_db_id IS NULL
            OR
            EXISTS
            ( SELECT
                 1
              FROM
                 ietm_topic_carrier
              WHERE
                 ietm_topic_carrier.carrier_db_id =  h_inv_inv.carrier_db_id          AND
                 ietm_topic_carrier.carrier_id    =  h_inv_inv.carrier_id             AND
                 ietm_topic_carrier.ietm_db_id    =  ietm_topic.ietm_db_id            AND
                 ietm_topic_carrier.ietm_id       =  ietm_topic.ietm_id               AND
                 ietm_topic_carrier.ietm_topic_id =  ietm_topic.ietm_topic_id
            )
         );

   /* used to get measurements for a task  based on a task defn */
   /* Ignore if measurement is an assembly measurement */
   CURSOR lcur_Measurement (
         an_TaskDbId task_parm_data.task_db_id%TYPE,
         an_TaskId   task_parm_data.task_id%TYPE
      ) IS
      SELECT
         task_parm_data.data_type_db_id,
         task_parm_data.data_type_id,
         task_parm_data.data_ord
      FROM
         task_parm_data
      WHERE
         task_parm_data.task_db_id = an_TaskDbId
         AND
         task_parm_data.task_id    = an_TaskId
         AND
         task_parm_data.data_type_db_id || ':' || task_parm_data.data_type_id NOT IN
      (SELECT
           ref_data_type_assmbl_class.data_type_db_id || ':' || ref_data_type_assmbl_class.data_type_id
       FROM
           task_parm_data,
           ref_data_type_assmbl_class
       WHERE
           task_parm_data.task_db_id = an_TaskDbId
           AND
           task_parm_data.task_id    = an_TaskId
           AND
           ref_data_type_assmbl_class.data_type_db_id = task_parm_data.data_Type_db_id
           AND
           ref_data_type_assmbl_class.data_type_id = task_parm_data.data_type_id
      );

  /* Cursor for getting assembly measurements associated to the task definition */
  CURSOR lcur_Assembly_Measurement(
   an_TaskDbId task_parm_data.task_db_id%TYPE,
        an_TaskId   task_parm_data.task_id%TYPE
      ) IS
   SELECT
      task_parm_data.data_type_db_id,
      task_parm_data.data_type_id,
      task_parm_data.data_ord,
      ref_data_type_assmbl_class.assmbl_class_db_id,
      ref_data_type_assmbl_class.assmbl_class_cd

   FROM

      task_parm_data

      INNER JOIN ref_data_type_assmbl_class ON
         task_parm_data.data_type_db_id                 = ref_data_type_assmbl_class.data_type_db_id AND
         task_parm_data.data_type_id                    = ref_data_type_assmbl_class.data_type_id

      INNER JOIN ref_assmbl_class ON
         ref_data_type_assmbl_class.assmbl_class_db_id  = ref_assmbl_class.assmbl_class_db_id AND
         ref_data_type_assmbl_class.assmbl_class_cd     = ref_assmbl_class.assmbl_class_cd

   WHERE
      task_parm_data.task_db_id = an_TaskDbId AND
      task_parm_data.task_id    = an_TaskId;

   /* Cursor for getting the assemblies and sub assemblies from an inventory */
   CURSOR lcur_Assembly_Inventories(
      an_InventoryDbId inv_inv.inv_no_db_id%TYPE,
      an_InventoryId inv_inv.inv_no_id%TYPE
   )  IS

   SELECT
     inv_inv.orig_assmbl_db_id,
     inv_inv.orig_assmbl_cd,
     eqp_assmbl.assmbl_class_db_id,
     eqp_assmbl.assmbl_class_cd,
     inv_inv.inv_no_db_id,
     inv_inv.inv_no_id

   FROM
     eqp_assmbl,
     inv_inv

   WHERE
      inv_inv.orig_assmbl_db_id       = eqp_assmbl.assmbl_db_Id AND
      inv_inv.orig_assmbl_cd          = eqp_assmbl.assmbl_cd AND
      inv_inv.inv_no_id
      IN (
         SELECT
            inv_inv.inv_no_id
         FROM
            inv_inv
         WHERE
            inv_inv.inv_class_cd = 'ASSY'
            START WITH
               inv_inv.inv_no_db_id = an_InventoryDbId AND
               inv_inv.inv_no_id    = an_InventoryId
            CONNECT BY
               inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
               inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
      );

   /* used to get work types for a task defn */
   CURSOR lcur_WorkType (
         an_TaskDbId task_work_type.task_db_id%TYPE,
         an_TaskId   task_work_type.task_id%TYPE
      ) IS
      SELECT
         task_work_type.work_type_db_id,
         task_work_type.work_type_cd
      FROM
         task_work_type
      WHERE
         task_work_type.task_db_id = an_TaskDbId AND
         task_work_type.task_id    = an_TaskId;

 BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;


   /* get the task definition info */
   OPEN  lcur_TaskDefinition( an_TaskDbId, an_TaskId );
   FETCH lcur_TaskDefinition INTO lrec_TaskDefinition;
   CLOSE lcur_TaskDefinition;

   /* get the inventory info */
   OPEN  lcur_Inventory(an_InvNoDbId, an_InvNoId);
   FETCH lcur_Inventory INTO lrec_Inventory;
   CLOSE lcur_Inventory;

   /* if task will be historic */
   IF (ab_Historic) THEN
      -- initial status is COMPLETE and is Historic
      ln_InitalTaskStatus:='COMPLETE';
      ln_histBool:=1;
   ELSE
      -- initial status is ACTV and is Non-Historic
      ln_InitalTaskStatus:='ACTV';
      ln_histBool:=0;
   END IF;


   /* if previous task exists then use it */
   IF ( an_PreviousTaskDbId <> -1 AND an_PreviousTaskId <> -1) THEN
      ln_PreviousTaskDbId := an_PreviousTaskDbId;
      ln_PreviousTaskId   := an_PreviousTaskId;
   /* if the task is active and it has a parent task defined in the baseline */
   ELSIF ((NOT ab_Historic) AND CountBaselineParents(an_TaskDbId, an_TaskId) > 0) THEN
      FindLatestTaskInstance(
            an_TaskDbId,
            an_TaskId,
            an_InvNoDbId,
            an_InvNoId,
            ln_PreviousTaskDbId,
            ln_PreviousTaskId,
            on_Return
         );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   ls_TaskType:='task';

    /* if previous task exists */
   IF (ln_PreviousTaskDbId <> -1 ) THEN

      /* get the next rel_id for the previous task */
      SELECT hist_bool
      INTO ln_PreviousTaskHistBool
      FROM evt_event
      WHERE
         event_db_id = ln_PreviousTaskDbId AND
         event_id    = ln_PreviousTaskId   AND
         evt_event.rstat_cd = 0;

      /* if the previous task exists and not historic then this task is a forecasted task */
      IF ln_PreviousTaskHistBool <> 1 THEN
         ln_InitalTaskStatus := 'FORECAST';
      END IF;

   END IF;

   /* if task is unique, and there is and active instance of that task on the aircraft */
   IF (
         lrec_TaskDefinition.unique_bool = 1 AND
         ln_InitalTaskStatus <> 'FORECAST' AND
         ln_InitalTaskStatus <> 'COMPLETE' AND
         CountTaskInstances(an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId) > 0
         ) THEN
         IF ab_CalledExternally THEN
            RAISE xc_TaskAlreadyExist;
         ELSE
            on_Return:=icn_success;
            RETURN;
         END IF;
   END IF;

   /************************************************
   *  PERFORM VALIDATION BEFORE CREATING THE TASK
   ************************************************/

   /* check to make sure that the inventory is not locked */
   IF lrec_Inventory.locked_bool = 1 THEN
      RAISE xc_InvIsLocked;
   END IF;

   /* check to make sure that the task definition is active */
   IF lrec_TaskDefinition.task_def_status_cd NOT IN ('ACTV', 'SUPRSEDE') THEN
      RAISE xc_TaskDefnNotActive;
   END IF;

   /* check to make sure that the given inventory has the correct part no */
   CheckTaskPartNo(
         an_InvNoDbId,
         an_InvNoId,
         an_TaskDbId,
         an_TaskId,
         ln_TaskApplies,
         on_Return
      );
   IF on_Return < 1 THEN
      RETURN;
   END IF;

   IF ln_TaskApplies = icn_False THEN
      RAISE xc_InvHasIncorrectPartNo;
   END IF;

   /* check to make sure that the given piece of inventory falls within
      the task definition's applicability rule */
   ln_TaskApplies :=
         IsTaskApplicable(
               an_InvNoDbId,
               an_InvNoId,
               an_TaskDbId,
               an_TaskId
            );

   IF ln_TaskApplies = 1 THEN
      /* if the rule applicability passes, check to make sure that
         range applicability passes as well */
      ln_TaskApplies := isApplicable( lrec_TaskDefinition.task_appl_eff_ldesc, lrec_Inventory.appl_eff_cd );
   END IF;

   /* do not create a task branch (return) if the task definition is not applicable to the given piece of
   inventory and this procedure is called inernally, ie. task dependency. Applicability of task definition
   has to be checked in the calling code */
   IF ln_TaskApplies = 0 AND NOT ab_CalledExternally THEN
      RETURN;
   END IF;


   /************************************************
   *  CREATE THE NEW TASK, AND UPDATE THE DATABASE
   *********************************************** */

   /* if ln_EventDbId or ln_EventId was not provided, derive the new values for  the new task*/
   IF an_EvtEventDbId IS  null OR  an_EvtEventId  IS  null THEN
      -- get the db_id value
      ln_EventDbId := APPLICATION_OBJECT_PKG.getdbid;
      -- get the id of the new task
      SELECT event_id_seq.NEXTVAL INTO ln_EventId FROM dual;
   ELSE
      -- set local vars to input values
      ln_EventDbId :=  an_EvtEventDbId;
      ln_EventId   :=  an_EvtEventId;
   END IF;

   /*
    * check to make sure that the current task definition is a root
    * if ln_ParentTaskCount=0 then root if ln_ParentTaskCount=1 then subtask
    */
   SELECT
      ref_task_class.class_mode_cd,
      ref_task_class.rstat_cd
   INTO
      ls_TaskClassMode,
      ln_TaskClassRstatCd
   FROM
      task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd    = task_task.task_class_cd
   WHERE
      task_task.task_db_id = an_TaskDbId  AND
      task_task.task_id    = an_TaskId;

   /* throw clear exception informing task class is soft-deleted */
   IF ( ln_TaskClassRstatCd <> 0 ) THEN
      RAISE xc_TaskClassSoftDeleted;
   END IF;

   IF ( ls_TaskClassMode = 'BLOCK' ) THEN
      ln_SubEventOrd := 1;
   ELSE
      IF ( ls_TaskClassMode = 'REQ' ) THEN
         SELECT
            count(*)
         INTO
            ln_ParentTaskCount
         FROM
            task_task
            INNER JOIN task_block_req_map ON
               task_block_req_map.req_task_defn_db_id = task_task.task_defn_db_id   AND
               task_block_req_map.req_task_defn_id    = task_task.task_defn_id
         WHERE
            task_task.task_db_id = an_TaskDbId  AND
            task_task.task_id    = an_TaskId;

         IF ( ln_ParentTaskCount = 0 ) THEN
            ln_SubEventOrd := 1;
         END IF;
      END IF;
   END IF;

   /* Create the Evt_Event row */
   INSERT INTO evt_event (
      event_db_id,
      event_id,
      event_type_db_id,
      event_type_cd,
      event_status_db_id,
      event_status_cd,
      actual_start_dt,
      actual_start_gdt,
      event_dt,
      event_gdt,
      sched_priority_db_id,
      sched_priority_cd,
      bitmap_db_id,
      bitmap_tag,
      event_sdesc,
      event_ldesc,
      hist_bool,
      seq_err_bool,
      h_event_db_id,
      h_event_id,
      sub_event_ord,
      doc_ref_sdesc )
   VALUES (
      ln_EventDbId,
      ln_EventId,
      0,
      'TS',
      0,
      ln_InitalTaskStatus,
      ad_CompletionDate,
      ad_CompletionDate,
      ad_CompletionDate,
      ad_CompletionDate,
      0,
      'NONE',
      lrec_TaskDefinition.bitmap_db_id,
      lrec_TaskDefinition.bitmap_tag,
      lrec_TaskDefinition.task_cd || ' (' || lrec_TaskDefinition.task_name || ')',
      lrec_TaskDefinition.task_ldesc,
      ln_histBool,
      0,
      ln_EventDbId,
      ln_EventId,
      ln_SubEventOrd,
      lrec_TaskDefinition.task_ref_sdesc
   );

   /* Create the Evt_Inv row */
   INSERT INTO evt_inv (
         event_db_id,
         event_id,
         event_inv_id,
         inv_no_db_id,
         inv_no_id,
         nh_inv_no_db_id,
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
         bom_part_id,
         main_inv_bool)
      VALUES (
         ln_EventDbId,
         ln_EventId,
         1,
         an_InvNoDbId,
         an_InvNoId,
         lrec_Inventory.nh_inv_no_db_id,
         lrec_Inventory.nh_inv_no_id,
         lrec_Inventory.assmbl_inv_no_db_id,
         lrec_Inventory.assmbl_inv_no_id,
         lrec_Inventory.h_inv_no_db_id,
         lrec_Inventory.h_inv_no_id,
         lrec_Inventory.assmbl_db_id,
         lrec_inventory.assmbl_cd,
         lrec_Inventory.assmbl_bom_id,
         lrec_Inventory.assmbl_pos_id,
         lrec_Inventory.part_no_db_id,
         lrec_Inventory.part_no_id,
         lrec_Inventory.bom_part_db_id,
         lrec_Inventory.bom_part_id,
         1 );


   IF lrec_TaskDefinition.task_class_cd = 'CHECK' THEN
      ls_TaskType := 'check';
      ln_woCommitLine := 0;
   ELSIF lrec_TaskDefinition.task_class_cd = 'RO' THEN
      ls_TaskType := 'work order';
      ln_woCommitLine := 0;
   END IF;

   /* Create the Sched_Stask row */
   INSERT INTO sched_stask (
         sched_db_id,
         sched_id,
         task_db_id,
         task_id,
         task_class_db_id,
         task_class_cd,
         task_subclass_db_id,
         task_subclass_cd,
         task_originator_db_id,
         task_originator_cd,
         task_priority_db_id,
         task_priority_cd,
         task_ref_sdesc,
         routine_bool,
         resource_sum_bool,
         instruction_ldesc,
         issued_dt,
         issued_gdt,
         orig_part_no_db_id,
         orig_part_no_id,
         wo_commit_line_ord,
         adhoc_recur_bool,
         corr_fix_bool,
         auto_complete_bool,
         barcode_sdesc,
         est_duration_qt,
         min_plan_yield_pct,
         prevent_lpa_bool,
         soft_deadline_bool,
         issue_account_db_id,
         issue_account_id,
         main_inv_no_db_id,
         main_inv_no_id,
         etops_bool)
      VALUES (
         ln_EventDbId,
         ln_EventId,
         an_TaskDbId,
         an_TaskId,
         lrec_TaskDefinition.task_class_db_id,
         lrec_TaskDefinition.task_class_cd,
         lrec_TaskDefinition.task_subclass_db_id,
         lrec_TaskDefinition.task_subclass_cd,
         lrec_TaskDefinition.task_originator_db_id,
         lrec_TaskDefinition.task_originator_cd,
         lrec_TaskDefinition.task_priority_db_id,
         lrec_TaskDefinition.task_priority_cd,
         lrec_TaskDefinition.task_ref_sdesc,
         lrec_TaskDefinition.routine_bool,
         lrec_TaskDefinition.resource_sum_bool,
         lrec_TaskDefinition.instruction_ldesc,
         SYSDATE,
         SYSDATE,
         lrec_Inventory.part_no_db_id,
         lrec_Inventory.part_no_id,
         decode(ln_histBool, 1, ln_woCommitLine, null),
         0,
         1,
         NVL(lrec_TaskDefinition.auto_complete_bool, 0 ),
         GENERATE_TASK_BARCODE(),
         lrec_TaskDefinition.est_duration_qt,
         lrec_TaskDefinition.min_plan_yield_pct,
         decode(lrec_TaskDefinition.task_class_cd, 'RO', 1, decode(lrec_TaskDefinition.task_class_cd, 'CHECK', 1, 0)),
         lrec_TaskDefinition.soft_deadline_bool,
         lrec_TaskDefinition.issue_account_db_id,
         lrec_taskDefinition.issue_account_id,
         an_InvNoDbId,
         an_InvNoId,
         lrec_TaskDefinition.etops_bool);


   /* The note changes based on whether or not the task being created is a dependent task */
   IF ( an_PreviousTaskDbId = -1 ) THEN
      ls_UserStageNote := 'The '|| ls_TaskType || ' has been created.';
   ELSE
      ls_UserStageNote := 'The ' || ls_TaskType || ' has been created as a result of task dependency.';
   END IF;

   IF ( NOT ab_CreateNATask ) THEN
      /* Get the next Stage ID */
      SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

      /* insert a record into the status history */
      INSERT INTO evt_stage (
         event_db_id,
         event_id,
         stage_id,
         event_status_db_id,
         event_status_cd,
         hr_db_id,
         hr_id,
         stage_reason_db_id,
         stage_reason_cd,
         stage_dt,
         stage_gdt,
         user_stage_note,
         system_bool
      )
      VALUES (
         ln_EventDbId,
         ln_EventId,
         ln_StageId,
         0,
         ln_InitalTaskStatus,
         an_HrDbId,
         an_HrId,
         an_ReasonDbId,
         an_ReasonCd,
         SYSDATE,
         SYSDATE,
         NVL( as_UserNote, ls_UserStageNote ),
         DECODE( as_UserNote, null, 1, 0 )
      );
   END IF;

   /* If task class mode is REQ, then check to see if the requirement is prevented from being executed */
   IF ( ls_TaskClassMode = 'REQ' ) THEN
      SELECT
         prevent_exe_bool,
         prevent_exe_review_dt
      INTO
         ln_PreventExeReqBool,
         ld_PreventExeReviewDt
      FROM
         task_task_flags
      WHERE
         task_db_id = an_TaskDbId AND
         task_id    = an_TaskId;

      /* If the requirement is prevented from being executed, then make sure the actual task is prevented as well */
      IF ( ln_PreventExeReqBool = 1 ) THEN

         UPDATE
            sched_stask_flags
         SET
            prevent_exe_bool = 1,
            prevent_exe_review_dt = ld_PreventExeReviewDt
         WHERE
            sched_db_id = ln_EventDbId AND
            sched_id    = ln_EventId;

         /* Get the next stage id */
         SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

         /* Add a history note */
         INSERT INTO evt_stage (
            event_db_id,
            event_id,
            stage_id,
            event_status_db_id,
            event_status_cd,
            hr_db_id,
            hr_id,
            stage_reason_db_id,
            stage_reason_cd,
            stage_dt,
            stage_gdt,
            user_stage_note,
            system_bool
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            ln_StageId,
            0,
            ln_InitalTaskStatus,
            an_HrDbId,
            an_HrId,
            NULL,
            NULL,
            SYSDATE,
            SYSDATE,
            'The task has been prevented from being executed.',
            1
         );
      END IF;
   END IF;


   /* Add all of the Work Types */
   FOR lrec_WorkType IN lcur_WorkType( an_TaskDbId, an_TaskId )
   LOOP
      INSERT INTO sched_work_type (
         sched_db_id,
         sched_id,
         work_type_db_id,
         work_type_cd
      )
      VALUES (
         ln_EventDbId,
         ln_EventId,
         lrec_WorkType.work_type_db_id,
         lrec_WorkType.work_type_cd
      );
   END LOOP;

   /* Add all of the Baseline Attachments and Technical References*/
   FOR lrec_TaskIetm IN lcur_TaskIetm( an_TaskDbId, an_TaskId )
   LOOP

      IF lrec_TaskIetm.attach_blob IS NULL THEN
         INSERT INTO evt_ietm (
            event_db_id,
            event_id,
            event_ietm_id,
            ietm_db_id,
            ietm_id,
            ietm_topic_id,
            ietm_ord
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            lrec_TaskIetm.task_ietm_id,
            lrec_TaskIetm.ietm_db_id,
            lrec_TaskIetm.ietm_id,
            lrec_TaskIetm.ietm_topic_id,
            lrec_TaskIetm.ietm_ord
         );
      ELSE
         INSERT INTO evt_attach (
            event_db_id,
            event_id,
            event_attach_id,
            ietm_db_id,
            ietm_id,
            ietm_topic_id,
            print_bool
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            lrec_TaskIetm.task_ietm_id,
            lrec_TaskIetm.ietm_db_id,
            lrec_TaskIetm.ietm_id,
            lrec_TaskIetm.ietm_topic_id,
            lrec_TaskIetm.print_bool
         );
      END IF;
   END LOOP;

   /* Add zones and panels but only if the task definition is a REQ, REF or JIC */
   IF ( ls_TaskClassMode = 'REQ' OR ls_TaskClassMode = 'REF' OR ls_TaskClassMode = 'JIC' )
   THEN

      /* Add all of the Baseline Zones */
      FOR lrec_Zone IN lcur_Zone( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO sched_zone (
              sched_db_id,
              sched_id,
              sched_zone_id,
              zone_db_id,
              zone_id
               )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              SCHED_ZONE_ID.nextval,
              lrec_Zone.zone_db_id,
              lrec_Zone.zone_id);
      END LOOP;

      /* Add all of the Baseline Panels */
      FOR lrec_Panel IN lcur_Panel( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO sched_panel (
              sched_db_id,
              sched_id,
              sched_panel_id,
              panel_db_id,
              panel_id
               )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              SCHED_PANEL_ID.nextval,
              lrec_Panel.panel_db_id,
              lrec_Panel.panel_id);
      END LOOP;
   END IF;

   /* If the task definition is a JIC or a executable requirement */
   IF ( ls_TaskClassMode = 'JIC' OR (ls_TaskClassMode = 'REQ' AND lrec_TaskDefinition.workscope_bool = 1) )
   THEN

      /* Add all of the Baseline Labours for Tasks allowing Resource Summaries */
      IF ( lrec_TaskDefinition.resource_sum_bool = 0 )
      THEN
         FOR lrec_TaskLabour IN lcur_TaskLabour( an_TaskDbId, an_TaskId )
         LOOP
            FOR ln_ManPwrLoop IN 1 .. lrec_TaskLabour.man_pwr_ct
            LOOP
               -- Generate the new Labour PK and insert the new labour
               SELECT SCHED_LABOUR_SEQ.nextval INTO ln_LabourId FROM dual;
               INSERT INTO sched_labour (
                  labour_db_id, labour_id,
                  sched_db_id, sched_id,
                  labour_stage_db_id, labour_stage_cd,
                  labour_skill_db_id, labour_skill_cd,
                  work_perf_bool,
                  cert_bool,
                  insp_bool,
                  current_status_ord
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  ln_EventDbId, ln_EventId,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV'),
                  lrec_TaskLabour.labour_skill_db_id, lrec_TaskLabour.labour_skill_cd,
                  lrec_TaskLabour.work_perf_bool,
                  lrec_TaskLabour.cert_bool,
                  lrec_TaskLabour.insp_bool,
                  1
               );

               -- Add Role and Status for Tech
               SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
               INSERT INTO sched_labour_role (
                  labour_role_db_id, labour_role_id,
                  labour_db_id, labour_id,
                  labour_role_type_db_id, labour_role_type_cd,
                  sched_hr
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  0, 'TECH',
                  lrec_TaskLabour.work_perf_hr
               );

               SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
               INSERT INTO sched_labour_role_status (
                  status_db_id, status_id,
                  labour_role_db_id, labour_role_id,
                  status_ord,
                  labour_role_status_db_id, labour_role_status_cd
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  1,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV')
               );

               -- If Certification required, Add Role and Status for Cert
               IF( lrec_TaskLabour.cert_bool = 1 ) THEN
                  SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                  INSERT INTO sched_labour_role (
                     labour_role_db_id, labour_role_id,
                     labour_db_id, labour_id,
                     labour_role_type_db_id, labour_role_type_cd,
                     sched_hr
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                     0, 'CERT',
                     lrec_TaskLabour.cert_hr
                  );
                  SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                  INSERT INTO sched_labour_role_status (
                     status_db_id, status_id,
                     labour_role_db_id, labour_role_id,
                     status_ord,
                     labour_role_status_db_id, labour_role_status_cd
                  )
                  VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    1,
                    0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING')
                  );

               END IF;

               -- If Inspection required, Add Role and Status for Insp
               IF ( lrec_TaskLabour.insp_bool = 1 ) THEN
                  SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                  INSERT INTO sched_labour_role (
                     labour_role_db_id, labour_role_id,
                     labour_db_id, labour_id,
                     labour_role_type_db_id, labour_role_type_cd,
                     sched_hr
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                     0, 'INSP',
                     lrec_TaskLabour.insp_hr
                  );

                  SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                  INSERT INTO sched_labour_role_status (
                     status_db_id, status_id,
                     labour_role_db_id, labour_role_id,
                     status_ord,
                     labour_role_status_db_id, labour_role_status_cd
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     1,
                     0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING')
                  );
               END IF;

            END LOOP;
         END LOOP;

      END IF;

      /* Add all of the Baseline Tools */
      ln_ToolId := 1;
      FOR lrec_Tool IN lcur_Tool( an_TaskDbId, an_TaskId )
      LOOP
         -- Get the Part Baseline key for the insert
         SELECT
            eqp_part_baseline.part_no_db_id,
            eqp_part_baseline.part_no_id
         INTO
            ln_StdPartNoDbId,
            ln_StdPartNoId
         FROM
            eqp_part_baseline
         WHERE
            eqp_part_baseline.bom_part_db_id = lrec_Tool.bom_part_db_id  AND
            eqp_part_baseline.bom_part_id    = lrec_Tool.bom_part_id
            AND
            eqp_part_baseline.standard_bool  = 1;

         INSERT INTO evt_tool (
               event_db_id,
               event_id,
               tool_id,
               bom_part_db_id,
               bom_part_id,
               part_no_db_id,
               part_no_id,
               task_db_id,
               task_id,
               task_tool_id,
               sched_hr )
         VALUES (
               ln_EventDbId,
               ln_EventId,
               ln_ToolId,
               lrec_Tool.bom_part_db_id,
               lrec_Tool.bom_part_id,
               ln_StdPartNoDbId,
               ln_StdPartNoId,
               an_TaskDbId,
               an_TaskId,
               lrec_Tool.task_tool_id,
               lrec_Tool.sched_hr );

         ln_ToolId := ln_ToolId + 1; -- increment the tool_id
      END LOOP;

      /* Add all of the Baseline Measurements */
      FOR lrec_Measurement IN lcur_Measurement( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO inv_parm_data (
               event_db_id,
               event_id,
               event_inv_id,
               data_type_db_id,
               data_type_id,
               data_ord,
               inv_no_db_id,
               inv_no_id)
            VALUES (
               ln_EventDbId,
               ln_EventId,
               1,
               lrec_Measurement.data_type_db_id,
               lrec_Measurement.data_type_id,
               lrec_Measurement.data_ord,
               an_InvNoDbId,
               an_InvNoId);
     END LOOP;

/* Add all the OC and Non OC Assembly Measurements.  */

     FOR lrec_Assembly_Inventory IN lcur_Assembly_Inventories(an_InvNoDbId,an_InvNoId)
     LOOP

   FOR lrec_Assembly_Measurement IN lcur_Assembly_Measurement( an_TaskDbId, an_TaskId )
   LOOP
        /* Check for APU or ENG Measurement */
        SELECT
       count(*) INTO ln_count
        FROM
           ref_data_type_assmbl_class
        WHERE
      ref_data_type_assmbl_class.assmbl_class_db_id = lrec_Assembly_Inventory.assmbl_class_db_id
      AND
      ref_data_type_assmbl_class.assmbl_class_cd = lrec_Assembly_Inventory.assmbl_class_cd
      AND
      ref_data_type_assmbl_class.data_type_db_id = lrec_Assembly_Measurement.data_type_db_id
      AND
      ref_data_type_assmbl_class.data_type_id = lrec_Assembly_Measurement.data_type_id;

        IF ln_count > 0 THEN
           SELECT
               COUNT(*) INTO ln_ExistsCount
           FROM
               inv_parm_data
           WHERE
               inv_parm_data.event_db_id = an_TaskDbId
               AND
               inv_parm_data.event_id = an_TaskId
               AND
               inv_parm_data.event_inv_id = 1
               AND
               inv_parm_data.data_type_db_id = lrec_Assembly_Measurement.data_type_db_id
               AND
               inv_parm_data.data_type_id = lrec_Assembly_Measurement.data_type_id
               AND
               inv_parm_data.inv_no_db_id = lrec_Assembly_Inventory.inv_no_db_id
               AND
               inv_parm_data.inv_no_id  =  lrec_Assembly_Inventory.inv_no_id;

              IF (ln_ExistsCount = 0) THEN

         SELECT
             mim_data_type.eng_unit_db_id, mim_data_type.eng_unit_cd
         INTO ln_EngUnitDbId, ls_EngUnitCD
         FROM
             mim_data_type
         WHERE
             data_type_db_id=lrec_Assembly_Measurement.data_type_db_id
             AND
             data_type_id = lrec_Assembly_Measurement.data_type_id;

         INSERT
         INTO inv_parm_data (
             event_db_id,
             event_id,
             event_inv_id,
             data_type_db_id,
             data_type_id,
             data_ord,
             inv_no_db_id,
             inv_no_id,
             rec_eng_unit_db_id,
             rec_eng_unit_cd)
         VALUES (
             ln_EventDbId,
             ln_EventId,
             1,
             lrec_Assembly_Measurement.data_type_db_id,
             lrec_Assembly_Measurement.data_type_id,
             lrec_Assembly_Measurement.data_ord,
             lrec_Assembly_Inventory.inv_no_db_id,
             lrec_Assembly_Inventory.inv_no_id,
             ln_EngUnitDbId,
             ls_EngUnitCD);
       END IF;
        END IF;
        END LOOP;
     END LOOP;


      /* Add all of the Baseline Part Requirements */
      GenSchedParts( ln_EventDbId, ln_EventId, on_Return );
      IF on_Return < 1 THEN
        RETURN;
      END IF;

      /* Add all of the Baseline Steps */
      FOR lrec_Step IN lcur_Step( an_TaskDbId, an_TaskId )
    LOOP
       -- get the next step id
       SELECT SCHED_STEP_ID.nextval INTO ln_StepId FROM dual;

       INSERT INTO sched_step (
        sched_db_id,
        sched_id,
        step_id,
        step_ord,
        step_ldesc,
        step_status_cd)
          VALUES (
        ln_EventDbId,
        ln_EventId,
        ln_StepId,
        lrec_Step.step_ord,
        lrec_Step.step_ldesc,
        'MXPENDING');
      END LOOP;

   END IF; /* End JIC/CORR task Specific logic */


   /* Queue up the Task for Zipping as part of Baseline Sync */
   IF ( ls_TaskClassMode = 'BLOCK' ) THEN
      BaselineSyncPkg.RequestZipForNewBlock( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
   ELSIF ( ls_TaskClassMode = 'REQ' ) THEN
      BaselineSyncPkg.RequestZipForNewReq( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
   END IF;

   on_SchedDbId     := ln_EventDbId;
   on_SchedId       := ln_EventId;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN xc_InvHasIncorrectPartNo THEN
      on_Return := icn_InvHasIncorrectPartNo;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00045','');
      RETURN;
   WHEN xc_TaskDefnNotActive THEN
      on_Return := icn_TaskDefnNotActive;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00047','');
      RETURN;
   WHEN xc_InvIsLocked THEN
      on_Return := icn_InvIsLocked;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00464','');
      RETURN;
   WHEN xc_TaskClassSoftDeleted THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('MX-32440','sched_stask_pkg@@@GenSchedTask@@@The task class has been soft-deleted. Cannot initialize task.');
      RETURN;
   WHEN xc_TaskAlreadyExist THEN
      on_Return := icn_TaskAlreadyExist;
      APPLICATION_OBJECT_PKG.SetMxiError('MX-30342','');
      RETURN;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenSchedTask@@@'||SQLERRM);
      RETURN;

END GenOneSchedTask;


/********************************************************************************
*
* Procedure:    AddAssemblyMeasurements
* Arguments:
*        an_TaskDbId                   The Task for which measurement is added
*        an_TaskId                     " "
*        an_DataTypeDbId               The Measurement Being added to the Task
*        an_DataTypeId                 " "
*
* Return:
*        on_Return        (long) - Success/Failure of Adding Assembly Measurements
*
* Description:
*            To Add Assembly Measurement to a Given Task
*
* Orig.Coder:     Balaji Rajasekaran
* Recent Coder:   Balaji Rajasekaran
*
*********************************************************************************
*
* Copyright 1997-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE AddAssemblyMeasurements(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           an_InventoryDbId      IN inv_inv.inv_no_db_id%TYPE,
           an_InventoryId        IN inv_inv.inv_no_id%TYPE,
           an_DataTypeDbId       IN mim_data_type.data_type_db_id%TYPE,
           an_DataTypeId         IN mim_data_type.data_type_id%TYPE,
           an_RecParmQty         IN inv_parm_data.rec_parm_qt% TYPE,
           on_Return             OUT typn_RetCode)
   IS
   /* *** DECLARE LOCAL VARIABLES *** */

   ln_EngUnitDbId                 mim_data_type.eng_unit_db_id%TYPE;
   ls_EngUnitCD                   mim_data_type.eng_unit_cd%TYPE;
   ln_ExistsCount                 NUMBER;
   ln_OrdCount                    NUMBER;

BEGIN
     on_Return := icn_NoProc;

     /**Get the Eng Unit details from MIM_DATA_TYPE Table*/
     SELECT
         mim_data_type.eng_unit_db_id, mim_data_type.eng_unit_cd
     INTO
         ln_EngUnitDbId, ls_EngUnitCD
     FROM
         mim_data_type
     WHERE
         data_type_db_id=an_DataTypeDbId AND
         data_type_id = an_DataTypeId;

     /**Compute the Data Ordering*/
     SELECT
         count(*)+1   INTO ln_OrdCount
     FROM
         inv_parm_data
     WHERE
         inv_parm_data.event_db_id = an_TaskDbId AND
         inv_parm_data.event_id = an_TaskId;

              SELECT
                  COUNT(*) INTO ln_ExistsCount
              FROM
                  inv_parm_data
              WHERE
                inv_parm_data.event_db_id = an_TaskDbId AND
                inv_parm_data.event_id = an_TaskId AND
                inv_parm_data.event_inv_id = 1 AND
                inv_parm_data.data_type_db_id = an_DataTypeDbId AND
                inv_parm_data.data_type_id = an_DataTypeId AND
                inv_parm_data.inv_no_db_id = an_InventoryDbId AND
                inv_parm_data.inv_no_id  =  an_InventoryId;
    
              IF (ln_ExistsCount = 0) THEN
                INSERT
                INTO inv_parm_data (
                    event_db_id,
                    event_id,
                    event_inv_id,
                    data_type_db_id,
                    data_type_id,
                    data_ord,
                    inv_no_db_id,
                    inv_no_id,
                    rec_eng_unit_db_id,
                    rec_eng_unit_cd,
                    rec_parm_qt,
                    parm_qt)
                 VALUES (
                    an_TaskDbId,
                    an_TaskId,
                    1,
                    an_DataTypeDbId,
                    an_DataTypeId,
                    ln_OrdCount,
                    an_InventoryDbId,
                    an_InventoryId,
                    ln_EngUnitDbId,
                    ls_EngUnitCD,
                    an_RecParmQty,
                    an_RecParmQty);
              END IF;
EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AddAssemblyMeasurements@@@'||SQLERRM);
      RETURN;

END AddAssemblyMeasurements;

/********************************************************************************
*
* Procedure:    CheckForNonHistMOD
* Arguments:    an_InvNoDbId  (long) - inventory database id
*               an_InvNoId    (long) - inventory id
* Return:       on_TaskExists (long) - task exists
*               on_Return     (long) - succss/failure of procedure
*
* Description:  Check that a non-historical MOD task does not exist for
*               this inventory serial no.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: Andrew Hircock
* Recent Date:  Nov 18, 1999
*
*********************************************************************************
*
* Copyright ? 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckForNonHistMOD(
      an_InvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId     IN inv_inv.inv_no_id%TYPE,
      on_TaskExists  OUT typn_RetCode,
      on_Return      OUT typn_RetCode
   ) IS

   /*declare local variable */
   ln_Count      NUMBER;

BEGIN
   /* default to task does exist */
   on_TaskExists := icn_True;

   /* check for non historical rmvl tasks */
   SELECT COUNT(*)
      INTO ln_Count
      FROM
         evt_inv
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
         INNER JOIN sched_stask ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
      WHERE
         evt_inv.inv_no_db_id  = an_InvNoDbId AND
         evt_inv.inv_no_id     = an_InvNoId AND
         evt_inv.main_inv_bool = 1
         AND
         evt_event.hist_bool   = 0  AND
         evt_event.rstat_cd   = 0
         AND
         sched_stask.task_class_cd = 'MOD';

   /* no rows - task does not exists   */
   /* rows    - task exists            */
   IF ln_Count = 0 THEN
      on_TaskExists := icn_False;
   ELSE
      on_TaskExists := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckForNonHistMOD@@@'||SQLERRM);
     RETURN;
END CheckForNonHistMOD;


/********************************************************************************
*
* Procedure:    CheckModPartNum
* Arguments:    an_InvNoDbId           (long) - inventory database id
*               an_InvNoId             (long) - inventory id
*               an_TaskDbId            (long) - task definition database id
*               an_TaskId              (long) - task definition id
* Return:       on_MatchingPartNo      (long) - part existance
*               on_Return              (long) - succss/failure of procedure
*
* Description:  Check that the inventory's part number is correct
*
*
* Orig.Coder:  H. Strutt
* Recent Coder: H. Strutt
* Recent Date:  Aug.17,2002
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckModPartNum(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_MatchingPartNo      OUT typn_RetCode,
      on_Return              OUT typn_RetCode
   ) IS

   /* declare local variable */
   ln_Count   NUMBER;     /* part transformation count */

BEGIN

   /* default to part does not exist */
   on_MatchingPartNo := ICN_FALSE;

   /* check that inventory part no contains a row in the */
   /* task_part_transformation table */
   SELECT COUNT(*)
     INTO ln_Count
     FROM task_part_transform,
          inv_inv
    WHERE inv_inv.inv_no_db_id = an_InvNoDbId
      AND inv_inv.inv_no_id    = an_InvNoId
      AND inv_inv.rstat_cd = 0
      AND task_part_transform.old_part_no_db_id = inv_inv.part_no_db_id
      AND task_part_transform.old_part_no_id    = inv_inv.part_no_id
      AND task_part_transform.task_db_id = an_TaskDbId
      AND task_part_transform.task_id    = an_TaskId;

   /* no rows - transformation does not exists   */
   /* rows    - transformation exists            */
   IF ln_Count = 0 THEN
      on_MatchingPartNo := icn_False;
   ELSE
      on_MatchingPartNo := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckModPartNum@@@'||SQLERRM);
     RETURN;
END CheckModPartNum;


/********************************************************************************
*
* Procedure:    CheckTaskPartNo
* Arguments:    an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:       on_TaskApplies (long) - task applies to the given BOM
*               on_Return      (long) - succss/failure of procedure
*
* Description:  Check that the given task definition applies to the inventory.
                Inventory match-ups are found by determining if the inventory's
                part no can fit into the task's bom item.
*
* Orig.Coder:  H. Strutt
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckTaskPartNo(
      an_InvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId     IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId    IN task_task.task_db_id%TYPE,
      an_TaskId      IN task_task.task_id%TYPE,
      on_TaskApplies OUT typn_RetCode,
      on_Return      OUT typn_RetCode
   ) IS

   /* declare local variables */
   ln_Count   NUMBER;  /* task existance count */
   ln_Count_Part NUMBER ;/* Count of Tasks for Part-Type*/

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* default to part does not exist */
   on_TaskApplies := ICN_FALSE;

   /* if the inventory is a system, then this check always succeeds */
   SELECT COUNT(*)
      INTO ln_Count
      FROM inv_inv
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId AND
         inv_inv.inv_class_cd = 'SYS'  AND
         inv_inv.rstat_cd  = 0;
   IF ln_Count = 1 THEN
      on_TaskApplies := icn_True;
      on_Return      := icn_Success;
      RETURN;
   END IF;

   /* check that the task applies, based on the inventory's part no */
   SELECT COUNT(*)
      INTO ln_Count
      FROM
         inv_inv,
         eqp_part_baseline,
         eqp_bom_part,
         ref_inv_class,
         task_task
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         eqp_part_baseline.part_no_db_id = inv_inv.part_no_db_id AND
         eqp_part_baseline.part_no_id    = inv_inv.part_no_id
         AND
         eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
         eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
         AND
         ref_inv_class.inv_class_db_id=eqp_bom_part.inv_class_db_id AND
         ref_inv_class.inv_class_cd=eqp_bom_part.inv_class_cd AND
         ref_inv_class.tracked_bool = 1
         AND
         task_task.task_db_id    = an_TaskDbId AND
         task_task.task_id       = an_TaskId
         AND
    (
       (
          task_task.assmbl_db_id  = eqp_bom_part.assmbl_db_id AND
          task_task.assmbl_cd     = eqp_bom_part.assmbl_cd AND
          task_task.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
       )
       OR
       (
          task_task.repl_assmbl_db_id  = eqp_bom_part.assmbl_db_id AND
          task_task.repl_assmbl_cd     = eqp_bom_part.assmbl_cd AND
          task_task.repl_assmbl_bom_id = eqp_bom_part.assmbl_bom_id
           )
         );

   /* check that inventory is based on one of the part numbers that is assigned to the part number task definition */
   SELECT COUNT(*)
      INTO ln_Count_Part
      FROM
         inv_inv,
         eqp_part_no,
         task_task,
         task_part_map
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
         eqp_part_no.part_no_id    = inv_inv.part_no_id
         AND
         task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
         task_part_map.part_no_id       = eqp_part_no.part_no_id
         AND
         task_task.task_db_id = task_part_map.task_db_id AND
         task_task.task_id    = task_part_map.task_id
         AND
         task_task.task_db_id    = an_TaskDbId AND
         task_task.task_id       = an_TaskId;

   /* return success/fail based on whether the query returned any rows */
   IF ln_Count = 0 AND ln_Count_Part = 0 THEN
      on_TaskApplies := icn_False;
   ELSE
      on_TaskApplies := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckTaskPartNo@@@'||SQLERRM);
     RETURN;
END CheckTaskPartNo;


/********************************************************************************
*
* Procedure:    IsTaskApplicable
* Arguments:    an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:       ln_TaskApplies (long) - task applies to the given inventory
*
*
* Description:  Check the "task applicability" rules and determine if the given
*               task definition applies to the given inventory.
                This procedure will not validate the inputs (for performance
                reasons). Ensure that the given inv_id and task_id exist in
                the database
*
* Orig.Coder:   A. Hircock
* Recent Coder: Randy Abson
* Recent Date:  March 7, 2005
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsTaskApplicable (
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER IS
   ls_TaskApplSql    task_task.task_appl_sql_ldesc%TYPE; /* applicability clause */
   ln_TaskApplies    typn_RetCode;
   BEGIN

   /* if there is an applicability clause, then run the logic to determine
      if the given inventory is applicable */
   SELECT task_appl_sql_ldesc
     INTO ls_TaskApplSql
     FROM task_task
    WHERE task_db_id = an_TaskDbId
      AND task_id    = an_TaskId;
   IF ls_TaskApplSql IS NOT NULL THEN

      /* Attempt to execute a dynamic SQL statement. The dynamic SQL
         statement will use the applicability clause. It will then set
         on_TaskApplies to 1 if the task applies to the inventory, and 0
         otherwise. If an error occurs, then the general error exception
         will be called.  */

      /* Test the task applicability */
      SELECT
         getTaskApplicability(an_InvNoDbId, an_InvNoId, ls_TaskApplSql)
      INTO
         ln_TaskApplies
      FROM
         dual;


   /* if there is no applicability clause, then the task definition
      applies by default */
   ELSE
      RETURN icn_True;
   END IF;

   /* set return success */
   RETURN ln_TaskApplies;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@IsTaskApplicable@@@'||SQLERRM);
     RETURN icn_Error;
   END IsTaskApplicable;


/********************************************************************************
*
* Procedure:    VerifyApplicabilityRule
* Arguments:    as_TaskApplSql    (string) - the applicability rule
* Return:       icn_True          (long)   - if the applicability rule is valid
*               icn_False         (long)   - if the applicability rule is invalid
*
*
* Description:  Check if the "task applicability" rules is valid.
*
* Orig.Coder:   jcimino
* Recent Coder: ycho
* Recent Date:  2011-03-02
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION VerifyApplicabilityRule (
      as_TaskApplSql IN task_task.task_appl_sql_ldesc%TYPE
   ) RETURN NUMBER IS
   ln_CursorHandle   NUMBER;  /* handle to the dynamic SQL cursor */
   li_DbmsReturn     INTEGER; /* needed to run the DBMS functions */
   ls_SelectClause   VARCHAR2(32767); /* SQL clause */
   BEGIN

   /* if there is an applicability clause, then run the query to determine
      if the rule is valid applicable */
   IF as_TaskApplSql IS NOT NULL THEN

      /* Attempt to execute a dynamic SQL statement. The dynamic SQL
         statement will use the applicability clause. If the SQL runs without
         error, then the applicability rule is valid. If there is an error,
         then the applicability rule is not valid.  */

      /* Create the SQL select statement */
      ls_SelectClause :=
   ' SELECT' ||
   '   1 ' ||
   ' FROM' ||
   '   dual ' ||
   ' WHERE EXISTS ' ||
   ' (' ||
   '   SELECT' ||
   '      *' ||
   '   FROM' ||
   '      inv_inv,' ||
   '      inv_inv assembly,' ||
   '      eqp_part_no,' ||
   '      eqp_manufact,' ||
   '      inv_owner,' ||
   '      inv_owner ass_owner,' ||
   '      inv_ac_reg,' ||
   '      inv_inv ac_inv,' ||
   '      eqp_part_no rootpart,' ||
   '      eqp_part_no asspart,' ||
   '      org_carrier,' ||
   '      org_carrier ass_carrier' ||
   '   WHERE' ||
   '      inv_inv.inv_no_db_id IS NULL AND ' ||
   '      inv_inv.inv_no_id    IS NULL ' ||
   '      AND ' ||
   '      (' || as_TaskApplSql || ')' ||
   '      AND' ||
   '      ac_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND' ||
   '      ac_inv.inv_no_id    = inv_inv.h_inv_no_id    AND' ||
   '      ac_inv.rstat_cd  = 0' ||
   '      AND' ||
   '      org_carrier.carrier_db_id (+)= ac_inv.carrier_db_id AND' ||
   '      org_carrier.carrier_id    (+)= ac_inv.carrier_id' ||
   '      AND' ||
   '      eqp_part_no.part_no_db_id (+)= inv_inv.part_no_db_id AND' ||
   '      eqp_part_no.part_no_id    (+)= inv_inv.part_no_id' ||
   '      AND' ||
   '      eqp_manufact.manufact_db_id (+)= eqp_part_no.manufact_db_id AND' ||
   '      eqp_manufact.manufact_cd    (+)= eqp_part_no.manufact_cd' ||
   '      AND' ||
   '      inv_owner.owner_db_id (+)= inv_inv.owner_db_id AND' ||
   '      inv_owner.owner_id    (+)= inv_inv.owner_id' ||
   '      AND' ||
   '      inv_ac_reg.inv_no_db_id (+)= ac_inv.inv_no_db_id AND' ||
   '      inv_ac_reg.inv_no_id    (+)= ac_inv.inv_no_id' ||
   '      AND' ||
   '      rootpart.part_no_db_id (+)= ac_inv.part_no_db_id AND' ||
   '      rootpart.part_no_id    (+)= ac_inv.part_no_id' ||
   '      AND' ||
   '      assembly.inv_no_db_id =' ||
   '      DECODE (inv_inv.assmbl_inv_no_db_id, NULL, inv_inv.inv_no_db_id, DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id))' ||
   '      AND' ||
   '      assembly.inv_no_id =' ||
   '      DECODE (inv_inv.assmbl_inv_no_id, NULL, inv_inv.inv_no_id, DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id))' ||
   '      AND' ||
   '      ass_carrier.carrier_db_id (+)= assembly.carrier_db_id AND' ||
   '      ass_carrier.carrier_id    (+)= assembly.carrier_id' ||
   '      AND' ||
   '      ass_owner.owner_db_id (+)= assembly.owner_db_id AND' ||
   '      ass_owner.owner_id    (+)= assembly.owner_id' ||
   '      AND' ||
   '      asspart.part_no_db_id (+)= assembly.part_no_db_id AND' ||
   '      asspart.part_no_id    (+)= assembly.part_no_id' ||
   ' )';

      /* open dynamic cursor */
      ln_CursorHandle := DBMS_SQL.OPEN_CURSOR;

      /* parse and define variables for the SQL statement */
      DBMS_SQL.PARSE(ln_CursorHandle, ls_SelectClause, DBMS_SQL.V7);

      /* excute and fetch the dynamic SQL */
      li_DbmsReturn := DBMS_SQL.EXECUTE_AND_FETCH(ln_CursorHandle);

      /* close dynamic cursor */
      DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle);

   ELSE
      RETURN icn_True;
   END IF;

   /* set return success */
   RETURN icn_True;

EXCEPTION
   WHEN OTHERS THEN
    IF  DBMS_SQL.IS_OPEN(ln_CursorHandle) THEN DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle); END IF;
    APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@VerifyApplicabilityRule@@@'||SQLERRM);
    RETURN icn_False;
   END VerifyApplicabilityRule;



/********************************************************************************
*
* Procedure:    CountBaselineParents
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:
*               ln_Count       (long) - number of parent tasks
*
* Description:  Returns number of active parents for a baseline task.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Yui Sotozaki
* Recent Date:  December 28, 2007
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION CountBaselineParents (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER IS
      ln_BlockCount NUMBER;
      ln_ReqCount NUMBER;

   BEGIN

      /* count number of baseline block parents */
      SELECT
         COUNT(*)
      INTO
         ln_BlockCount
      FROM
         task_task,
         task_block_req_map,
         task_task block_task_task
      WHERE
         task_task.task_db_id = an_TaskDbId  AND
         task_task.task_id    = an_TaskId
         AND
         task_block_req_map.req_task_defn_db_id = task_task.task_defn_db_id   AND
         task_block_req_map.req_task_defn_id    = task_task.task_defn_id
         AND
         block_task_task.task_db_id          = task_block_req_map.block_task_db_id  AND
         block_task_task.task_id             = task_block_req_map.block_task_id     AND
         block_task_task.task_def_status_cd  = 'ACTV';

      /* count number of baseline requirement parents */
      SELECT
         COUNT(*)
      INTO
         ln_ReqCount
      FROM
         task_task,
         task_jic_req_map,
         task_task req_task_task
      WHERE
         task_task.task_db_id = an_TaskDbId  AND
         task_task.task_id    = an_TaskId
         AND
         task_jic_req_map.jic_task_db_id  = task_task.task_db_id  AND
         task_jic_req_map.jic_task_id     = task_task.task_id
         AND
         req_task_task.task_defn_db_id    = task_jic_req_map.req_task_defn_db_id AND
         req_task_task.task_defn_id       = task_jic_req_map.req_task_defn_id    AND
         req_task_task.task_def_status_cd = 'ACTV';

      RETURN ( ln_BlockCount + ln_ReqCount );
   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CountBaselineParents@@@'||SQLERRM);
     RETURN icn_Error;
   END CountBaselineParents;


/********************************************************************************
*
* Procedure:   CountTaskInstances
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
*               an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
* Return:
*
*
* Description:  Count number of task active (non Forecast) instances on a specific inventory and
*               with specific task baseline dbid/id.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: N/A
* Recent Date:  July 8, 2004
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION CountTaskInstances (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE

   ) RETURN NUMBER IS
   ln_Count NUMBER;

   BEGIN

   /*count number of active task instances*/
   SELECT COUNT(*)
   INTO ln_Count
   FROM  sched_stask,
         evt_event,
         evt_inv
   WHERE
       sched_stask.task_db_id=an_TaskDbId AND
       sched_stask.task_id=an_TaskId   AND
       sched_stask.rstat_cd         = 0
       AND
       evt_event.event_db_id=sched_stask.sched_db_id AND
       evt_event.event_id=sched_stask.sched_id AND
       evt_event.hist_bool=0 AND
       NOT evt_event.event_status_cd='FORECAST'
       AND
       evt_inv.event_db_id=evt_event.event_db_id AND
       evt_inv.event_id=evt_event.event_id AND
       evt_inv.inv_no_db_id=an_InvNoDbId AND
       evt_inv.inv_no_id=an_InvNoId;

       RETURN ln_Count;
   EXCEPTION
   WHEN OTHERS THEN
   -- Unexpected error
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CountTaskInstances@@@'||SQLERRM);
   RETURN icn_Error;
   END CountTaskInstances;



/********************************************************************************
*
* Procedure:    FindLatestTaskInstance
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
*               an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*
* Return:       an_TaskInstanceDbId (long) - task instance database id
*               an_TaskInstanceId (long) - task instance id
*               on_return (long) - 1 if error not found
*
*
* Description:  Find last instance of a task. Looks up latest task
*               based on a pecific task definition and associated with inventory.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindLatestTaskInstance (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskInstanceDbId    OUT sched_stask.sched_db_id%TYPE,
      an_TaskInstanceId      OUT sched_stask.sched_db_id%TYPE,
      on_Return              OUT typn_RetCode

   ) IS
  /* *** DECLARE CURSORS *** */
   /* cursor used to find latest active task */
   CURSOR lcur_FindPreviousTaskInstance (
         an_taskdbid  task_task.task_db_id%type,
         an_taskid    task_task.task_id%type,
         an_invnodbid inv_inv.inv_no_db_id%type,
         an_invnoid   inv_inv.inv_no_id%type
      ) IS
     SELECT evt_sched_dead.event_db_id,
            evt_sched_dead.event_id
     FROM sched_stask,
          evt_inv,
          evt_sched_dead,
          evt_event
     WHERE
          sched_stask.task_db_id       = an_taskdbid AND
          sched_stask.task_id          = an_taskid   AND
          sched_stask.orphan_frct_bool = 0           AND
          sched_stask.rstat_cd         = 0
          AND
          evt_event.event_db_id=sched_stask.sched_db_id AND
          evt_event.event_id=sched_stask.sched_id AND
         (evt_event.event_status_cd='ACTV' OR
          evt_event.event_status_cd='FORECAST' OR
          evt_event.event_status_cd='COMPLETE'
          )
          AND
          evt_inv.event_db_id=sched_stask.sched_db_id AND
          evt_inv.event_id=sched_stask.sched_id AND
          evt_inv.inv_no_db_id=an_invnodbid AND
          evt_inv.inv_no_id=an_invnoid AND
          evt_inv.main_inv_bool = 1
          AND
          evt_sched_dead.event_db_id=sched_stask.sched_db_id AND
          evt_sched_dead.event_id = sched_stask.sched_id AND
          evt_sched_dead.sched_driver_bool=1
          AND
          (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
          NOT IN(
          SELECT evt_event_rel.event_db_id,
                 evt_event_rel.event_id
          FROM evt_event_rel
          WHERE
          evt_event_rel.rel_type_cd = 'DEPT' )
          ORDER BY decode(evt_event.hist_bool, 1, evt_event.event_dt, evt_sched_dead.sched_dead_dt) desc;
          lrec_FindPreviousTaskInstance  lcur_FindPreviousTaskInstance%rowtype;

/* cursor used to find latest active, forecast or complete task based on older task revision. The task must be in INWORK or COMMIT check */
   CURSOR lcur_FindOlderPreviousTask(
         an_taskdbid  task_task.task_db_id%type,
         an_taskid    task_task.task_id%type,
         an_invnodbid inv_inv.inv_no_db_id%type,
         an_invnoid   inv_inv.inv_no_id%type
      ) IS
     SELECT task_event.event_db_id,
            task_event.event_id
     FROM
         task_task curent_task_task,
         task_task,
         inv_inv,
         sched_stask,
         evt_inv,
         evt_event task_event
     WHERE
         inv_inv.inv_no_db_id = an_invnodbid AND
         inv_inv.inv_no_id    = an_invnoid   AND
         inv_inv.rstat_cd  = 0
         AND
         evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id AND
         evt_inv.inv_no_id     = inv_inv.inv_no_id    AND
         evt_inv.main_inv_bool = 1
         AND
         task_event.event_db_id = evt_inv.event_db_id AND
         task_event.event_id    = evt_inv.event_id    AND
         task_event.event_status_cd IN ('ACTV','FORECAST','COMPLETE')
         AND
         sched_stask.sched_db_id = task_event.event_db_id AND
         sched_stask.sched_id    = task_event.event_id
         AND
         sched_stask.orphan_frct_bool = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id  AND
         task_task.task_id    = sched_stask.task_id
         AND
         curent_task_task.task_defn_db_id = task_task.task_defn_db_id AND
         curent_task_task.task_defn_id    = task_task.task_defn_id
         AND
         curent_task_task.task_db_id = an_taskdbid AND
         curent_task_task.task_id    = an_taskid  AND
         curent_task_task.recurring_task_bool = 1
         AND
         (sched_stask.sched_db_id, sched_stask.sched_id)
           NOT IN(
               SELECT evt_event_rel.event_db_id,
                 evt_event_rel.event_id
               FROM   evt_event_rel
               WHERE  evt_event_rel.rel_type_cd = 'DEPT' );
         lrec_FindOlderPreviousTask  lcur_FindOlderPreviousTask%rowtype;


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get the task definition info */
   OPEN  lcur_FindPreviousTaskInstance( an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId);
   FETCH lcur_FindPreviousTaskInstance INTO lrec_FindPreviousTaskInstance;
   CLOSE lcur_FindPreviousTaskInstance;

   /* if previous task not found, set the return values to -1*/
   IF (lrec_FindPreviousTaskInstance.event_db_id is null) THEN
      /* try to find an existing active task based on an older revision */
      OPEN  lcur_FindOlderPreviousTask( an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId);
      FETCH lcur_FindOlderPreviousTask INTO lrec_FindOlderPreviousTask;
      CLOSE lcur_FindOlderPreviousTask;

      IF (lrec_FindOlderPreviousTask.event_db_id is null) THEN
         an_TaskInstanceDbId:=-1;
         an_TaskInstanceId:=-1;
      ELSE
         an_TaskInstanceDbId := lrec_FindOlderPreviousTask.event_db_id;
         an_TaskInstanceId   := lrec_FindOlderPreviousTask.event_id;
      END IF;
   ELSE
      /* if previous values found return them*/
      an_TaskInstanceDbId := lrec_FindPreviousTaskInstance.event_db_id;
      an_TaskInstanceId   := lrec_FindPreviousTaskInstance.event_id;
   END IF;

   /*if this line is reached, return success*/
   on_Return := icn_Success;

EXCEPTION
WHEN OTHERS THEN
   -- Unexpected error
  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindLatestTaskInstance@@@'||SQLERRM);
  on_Return:= icn_Error;
END FindLatestTaskInstance;

/********************************************************************************
*
* Procedure:    CreateTaskDependencyLink
* Arguments:
*               an_PreviousSchedDbId    (long) - previous task database id
*               an_PreviousSchedId      (long) - previous task id
*               an_SchedDbId    (long) - stask database id
*               an_SchedId      (long) - stask definition id
* Return:       on_Return       (long) - 1 if no error found
*
*
* Description:  Creates DEPT link between two task instances.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CreateTaskDependencyLink (
       an_PreviousSchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_PreviousSchedId           IN sched_stask.sched_id%TYPE,
       an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
       an_SchedId              IN sched_stask.sched_id%TYPE,
       on_Return               OUT typn_RetCode

   ) IS

   ln_NextEventRelId NUMBER;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get the next rel_id for the previous task */
   SELECT
         max( event_rel_id ) + 1
     INTO
         ln_NextEventRelId
     FROM
         evt_event_rel
    WHERE
         event_db_id = an_PreviousSchedDbId AND
         event_id    = an_PreviousSchedId;
   IF ln_NextEventRelId IS NULL THEN
      ln_NextEventRelId := 1;
   END IF;

   /* insert the relationship */
   INSERT INTO evt_event_rel (
         event_db_id,
         event_id,
         event_rel_id,
         rel_event_db_id,
         rel_event_id,
         rel_type_db_id,
         rel_type_cd,
         rel_event_ord )
   VALUES (
         an_PreviousSchedDbId,
         an_PreviousSchedId,
         ln_NextEventRelId,
         an_SchedDbId,
         an_SchedId,
         0,
         'DEPT',
         1 );

   /* link the deadlines if any */
   UPDATE evt_sched_dead t
   SET    t.sched_from_cd = 'LASTDUE'
   WHERE  t.event_db_id = an_SchedDbId AND
          t.event_id    = an_SchedId   AND
          t.sched_from_cd <> 'LASTDUE'
          -- where the two tasks share the same datatype, and the previous task is ACTV
          AND EXISTS
          (SELECT 1
           FROM   evt_sched_dead prev_sched_dead,
                  evt_event      prev_event
           WHERE  prev_event.event_db_id   = an_PreviousSchedDbId AND
                  prev_event.event_id      = an_PreviousSchedId   AND
                  prev_event.hist_bool     = 0
                  AND
                  prev_sched_dead.event_db_id     = prev_event.event_db_id AND
                  prev_sched_dead.event_id        = prev_event.event_id    AND
                  prev_sched_dead.data_type_db_id = t.data_type_db_id      AND
                  prev_sched_dead.data_type_id    = t.data_type_id);

   prep_deadline_pkg.UpdateDependentDeadlines( an_PreviousSchedDbId,
                                               an_PreviousSchedId,
                                               on_Return);

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CreateTaskDependencyLink@@@'||SQLERRM);
     on_Return:= icn_Error;

   END CreateTaskDependencyLink;
/********************************************************************************
*
* Procedure:    FindRemovedInventory
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. It should only be used to
                search for TRK/ASSY inventories
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindRemovedInventory (
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      an_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
      an_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** LOCAL VARIABLES *** */
   ln_OnAssmbl NUMBER;

   /* *** CURSOR DECLARATIONS *** */
   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindOnAssmbl (
         cn_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
         cn_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
         cn_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv start_inv,
         inv_inv find_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId   AND
         start_inv.rstat_cd   = 0
         AND
         (
            ( find_inv.assmbl_inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
              find_inv.assmbl_inv_no_id    = start_inv.assmbl_inv_no_id )
            OR
            ( find_inv.assmbl_inv_no_db_id = start_inv.inv_no_db_id AND
              find_inv.assmbl_inv_no_id    = start_inv.inv_no_id )
            OR
            ( find_inv.inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
              find_inv.inv_no_id    = start_inv.assmbl_inv_no_id )
         )
         AND
         (
            ( find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
              find_inv.assmbl_cd     = cs_FindAssmblCd AND
              find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
              find_inv.assmbl_bom_id = cn_FindAssmblBomId AND
              find_inv.bom_part_db_id = cn_FindBomPartDbId AND
              find_inv.bom_part_id    = cn_FindBomPartId    AND
              find_inv.rstat_cd  = 0)
            OR
            ( cn_FindAssmblBomId = 0 AND
              find_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
              find_inv.orig_assmbl_cd    = cs_FindAssmblCd    AND
              find_inv.rstat_cd  = 0 )
         );
   lrec_FindOnAssmbl lcur_FindOnAssmbl%ROWTYPE;

   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindLoose (
         cn_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
         cn_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
         cn_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv start_inv,
         inv_inv find_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId    AND
         start_inv.rstat_cd   = 0
         AND
         find_inv.bom_part_db_id = cn_FindBomPartDbId AND
         find_inv.bom_part_id    = cn_FindBomPartId    AND
         find_inv.rstat_cd = 0
         AND
         find_inv.h_inv_no_db_id = start_inv.h_inv_no_db_id AND
         find_inv.h_inv_no_id    = start_inv.h_inv_no_id
         AND
         find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
         find_inv.assmbl_cd     = cs_FindAssmblCd AND
         find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
         find_inv.assmbl_bom_id = cn_FindAssmblBomId;

   lrec_FindLoose lcur_FindLoose%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* determine if the starting inventory is on an assembly */
   SELECT DECODE( assmbl_inv_no_db_id, NULL, 0, 1 )
      INTO ln_OnAssmbl
      FROM inv_inv
      WHERE
         inv_no_db_id = an_StartInvNoDbId AND
         inv_no_id    = an_StartInvNoId     AND
         inv_inv.rstat_cd  = 0;

   /* if the start_inv is on an assembly, then look for the matching component
      in that assembly */
   IF ln_OnAssmbl = 1 THEN
      OPEN lcur_FindOnAssmbl(
               an_StartInvNoDbId,
               an_StartInvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId,
               an_FindBomPartDbId,
               an_FindBomPartId );
      FETCH lcur_FindOnAssmbl INTO lrec_FindOnAssmbl;
      IF lcur_FindOnAssmbl%FOUND THEN
         on_InvNoDbId := lrec_FindOnAssmbl.inv_no_db_id;
         on_InvNoId   := lrec_FindOnAssmbl.inv_no_id;
      ELSE
         on_InvNoDbId := null;
         on_InvNoId   := null;
      END IF;
      CLOSE lcur_FindOnAssmbl;
   /* if the start_inv is on a loose component, then look for the matching
      component in the entire inventory tree. */
   ELSE
      OPEN lcur_FindLoose(
               an_StartInvNoDbId,
               an_StartInvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId,
               an_FindBomPartDbId,
               an_FindBomPartId );
      FETCH lcur_FindLoose INTO lrec_FindLoose;
      IF lcur_FindLoose%FOUND THEN
         on_InvNoDbId := lrec_FindLoose.inv_no_db_id;
         on_InvNoId   := lrec_FindLoose.inv_no_id;
      ELSE
         on_InvNoDbId := null;
         on_InvNoId   := null;
      END IF;
      CLOSE lcur_FindLoose;
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindRemovedInventory@@@'||SQLERRM);
     RETURN;
END FindRemovedInventory;



/********************************************************************************
*
* Procedure:    FindRemovedInventory
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. It should only be used to
                search for TRK/ASSY inventories
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindStartInventory (
      an_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindOnAssmbl (
         cn_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv highest_inv,
         inv_inv find_inv
      WHERE
         highest_inv.inv_no_db_id=cn_InvNoDbId AND
         highest_inv.inv_no_id=cn_InvNoId      AND
         highest_inv.rstat_cd = 0
         AND
         find_inv.h_inv_no_db_id = highest_inv.h_inv_no_db_id AND
         find_inv.h_inv_no_id    = highest_inv.h_inv_no_id    AND
         find_inv.rstat_cd = 0
         AND
          find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
          find_inv.assmbl_cd     = cs_FindAssmblCd AND
          find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
          find_inv.assmbl_bom_id = cn_FindAssmblBomId;
   lrec_FindOnAssmbl lcur_FindOnAssmbl%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

      OPEN lcur_FindOnAssmbl(
               an_InvNoDbId,
               an_InvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId);
      FETCH lcur_FindOnAssmbl INTO lrec_FindOnAssmbl;
      IF lcur_FindOnAssmbl%FOUND THEN
         on_InvNoDbId := lrec_FindOnAssmbl.inv_no_db_id;
         on_InvNoId   := lrec_FindOnAssmbl.inv_no_id;
      ELSE
         on_InvNoDbId := an_InvNoDbId;
         on_InvNoId   := an_InvNoId;
      END IF;
      CLOSE lcur_FindOnAssmbl;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindStartInventory@@@'||SQLERRM);
     RETURN;
END FindStartInventory;

/********************************************************************************
*
* Procedure:    AddRmvdPartRequirement
* Arguments:
*
*      an_SchedDbId          (long) -- scheduled part requirement db id
*      an_SchedId            (long) -- scheduled part requirement id
*      an_SchedPartId        (long) -- scheduled part requirement part id
*      an_RemovedReasonDbid  (long) -- removed reason database id
*      as_RemovedReasonCd    (char) -- removed reasin code
*
* Return:      on_Return     (long) - 1 if no error found
*
*
* Description: This procedure generates rows in sched_rmvd_part table based on values
*              in sched_part table.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Jonathan Clarkin
* Recent Date:  September 30, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AddRmvdPartRequirement (
       an_InvDbId            IN inv_inv.inv_no_db_id%TYPE,
       an_InvId              IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId          IN sched_stask.sched_db_id%TYPE,
       an_SchedId            IN sched_stask.sched_id%TYPE,
       an_SchedPartId        IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       an_SchedRmvdPartId    OUT sched_rmvd_part.sched_rmvd_part_id%TYPE,
       on_Return             OUT typn_RetCode

   ) IS

   /* quantity number */
   ln_QtNumber NUMBER;

   /* removed part id (index) */
   ln_SchedRmvdPartId NUMBER;

   /* removed inventory if one exists in a hole */
   ln_RmvdInvNoDbId  sched_rmvd_part.inv_no_db_id%TYPE;
   ln_RmvdInvNoId    sched_rmvd_part.inv_no_id%TYPE;

  /* removed inventory if one exists in a hole */
   ln_StartInvNoDbId  inv_inv.inv_no_db_id%TYPE;
   ln_StartInvNoId    inv_inv.inv_no_id%TYPE;

   /* removed part no */
   ln_RmvdPartNoDbId sched_rmvd_part.part_no_db_id%TYPE;
   ln_RmvdPartNoId   sched_rmvd_part.part_no_id%TYPE;

   /* removed serial no */
   ls_RmvdSerialNo   sched_rmvd_part.serial_no_oem%TYPE;

   /* removed reason */
   ln_RemovedReasonDbid task_part_list.remove_reason_db_id%TYPE;
   ls_RemovedReasonCd   task_part_list.remove_reason_cd%TYPE;

   /* used to get part requirements details */
   CURSOR lcur_SchedPart (
         cn_SchedDbId   sched_part.sched_db_id%TYPE,
         cn_SchedId     sched_part.sched_id%TYPE,
         cn_SchedPartId sched_part.sched_part_id%TYPE
      ) IS
      SELECT
         sched_part.sched_bom_part_db_id,
         sched_part.sched_bom_part_id,
         sched_part.assmbl_db_id,
         sched_part.assmbl_cd,
         sched_part.assmbl_bom_id,
         sched_part.assmbl_pos_id,
         sched_part.nh_assmbl_db_id,
         sched_part.nh_assmbl_cd,
         sched_part.nh_assmbl_bom_id,
         sched_part.nh_assmbl_pos_id,
         sched_part.spec_part_no_db_id,
         sched_part.spec_part_no_id,
         sched_part.sched_qt,
         eqp_bom_part.inv_class_cd
      FROM
         sched_part,
         eqp_bom_part
      WHERE
         sched_part.sched_db_id   = cn_SchedDbId AND
         sched_part.sched_id      = cn_SchedId AND
         sched_part.sched_part_id = cn_SchedPartId AND
         sched_part.rstat_cd  = 0
         AND
         eqp_bom_part.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_bom_part.bom_part_id    = sched_part.sched_bom_part_id;

         lrec_SchedPart lcur_SchedPart%ROWTYPE;

   /* get standard part for bom part */
   CURSOR lcur_StandardPart (
         cn_BomPartDbId sched_part.sched_bom_part_db_id %TYPE,
         cn_BomPartId   sched_part.sched_bom_part_id%TYPE
      ) IS
       SELECT
         eqp_part_baseline.part_no_db_id,
         eqp_part_baseline.part_no_id
       FROM
         eqp_part_baseline
       WHERE
         eqp_part_baseline.bom_part_db_id=cn_BomPartDbId AND
         eqp_part_baseline.bom_part_id=cn_BomPartId AND
         eqp_part_baseline.standard_bool=1;

         lrec_StandardPart lcur_StandardPart%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*  get list of part requirements */
   OPEN  lcur_SchedPart( an_SchedDbId, an_SchedId, an_SchedPartId );
   FETCH lcur_SchedPart INTO lrec_SchedPart;
   CLOSE lcur_SchedPart;

   /*  get standard part */
   OPEN  lcur_StandardPart( lrec_SchedPart.sched_bom_part_db_id, lrec_SchedPart.sched_bom_part_id ) ;
   FETCH lcur_StandardPart INTO lrec_StandardPart;
   CLOSE lcur_StandardPart;

   /* get next index for the removed part requirement */
   SELECT decode( max( sched_rmvd_part_id ), null, 0, max( sched_rmvd_part_id ) )
   INTO ln_SchedRmvdPartId
   FROM sched_rmvd_part
   WHERE sched_db_id   = an_SchedDbId and
         sched_id      = an_SchedId and
         sched_part_id = an_SchedPartId   AND
         rstat_cd = 0;

   /* default the serial number to XXX*/
   ls_RmvdSerialNo := 'XXX';
   /* default the qt to 1 */
   ln_QtNumber := 1;

   ln_RemovedReasonDbid := an_RemovedReasonDbid;
   ls_RemovedReasonCd   := as_RemovedReasonCd;

   /* default the part no to the sched part part no */
   ln_RmvdPartNoDbId := lrec_SchedPart.spec_part_no_db_id;
   ln_RmvdPartNoId   := lrec_SchedPart.spec_part_no_id;

   /* if the Part to be Removed is null, set it to the standard part no */
   IF( ln_RmvdPartNoDbId IS NULL ) THEN
      ln_RmvdPartNoDbId := lrec_StandardPart.part_no_db_id;
      ln_RmvdPartNoId   := lrec_StandardPart.part_no_id;
   END IF;

   /* if the bom part is BATCH */
   IF( lrec_SchedPart.inv_class_cd='BATCH' ) THEN
      /* qt is the sched part qt */
      ln_QtNumber := lrec_SchedPart.sched_qt; -- initialize default quantity to the part requirement qt

      /* serial number is null */
      ls_RmvdSerialNo := null;
   END IF;


   /* if bom part is ASSY or TRK */
   IF
      lrec_SchedPart.inv_class_cd = 'ASSY' OR
      lrec_SchedPart.inv_class_cd = 'TRK' THEN

      ln_RmvdPartNoDbId := NULL;
      ln_RmvdPartNoId   := NULL;
      ls_RmvdSerialNo   := '';

     FindStartInventory (
            an_InvDbId,
            an_InvId,
            lrec_SchedPart.nh_assmbl_db_id,
            lrec_SchedPart.nh_assmbl_cd,
            lrec_SchedPart.nh_assmbl_bom_id,
            lrec_SchedPart.nh_assmbl_pos_id,
            ln_StartInvNoDbId,
            ln_StartInvNoId,
            on_Return
      );



      /*look up an inventory for the part requirement, at given position and with given bom part*/
      FindRemovedInventory (
            ln_StartInvNoDbId,
            ln_StartInvNoId,
            lrec_SchedPart.assmbl_db_id,
            lrec_SchedPart.assmbl_cd,
            lrec_SchedPart.assmbl_bom_id,
            lrec_SchedPart.assmbl_pos_id,
            lrec_SchedPart.sched_bom_part_db_id,
            lrec_SchedPart.sched_bom_part_id,
            ln_RmvdInvNoDbId,
            ln_RmvdInvNoId,
            on_Return
      );

      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* if matching inventory was found, then initialize the other inventory details */
   IF ln_RmvdInvNoDbId IS NOT NULL THEN

      /* fill in the PART_NO and SERIAL_NO */
      SELECT
         inv_inv.part_no_db_id,
         inv_inv.part_no_id,
         inv_inv.serial_no_oem
      INTO
         ln_RmvdPartNoDbId,
         ln_RmvdPartNoId,
         ls_RmvdSerialNo
      FROM
         inv_inv
      WHERE
         inv_inv.inv_no_db_id = ln_RmvdInvNoDbId AND
         inv_inv.inv_no_id    = ln_RmvdInvNoId  AND
         inv_inv.rstat_cd  = 0;
   END IF;


   /* insert new part requirement */
   INSERT INTO sched_rmvd_part (
      sched_db_id,
      sched_id,
      sched_part_id,
      sched_rmvd_part_id,
      part_no_db_id,
      part_no_id,
      serial_no_oem,
      inv_no_db_id,
      inv_no_id,
      rmvd_qt,
      remove_reason_db_id,
      remove_reason_cd
   )
   VALUES (
      an_SchedDbId,
      an_SchedId,
      an_SchedPartId,
      ln_SchedRmvdPartId+1,
      ln_RmvdPartNoDbId,
      ln_RmvdPartNoId,
      ls_RmvdSerialNo,
      ln_RmvdInvNoDbId,
      ln_RmvdInvNoId,
      ln_QtNumber,
      ln_RemovedReasonDbid,
      ls_RemovedReasonCd
   );

   an_SchedRmvdPartId := ln_SchedRmvdPartId+1;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
     on_Return := icn_Error;
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AddRmvdPartRequirement@@@'||SQLERRM);

END AddRmvdPartRequirement;

/********************************************************************************
*
* Procedure:    GenerateRemoveInstallPartReq
* Arguments:
*
*      an_InvDbId            (long) -- main inventory database id
*      an_InvId              (long) -- main inventory dbid
*      an_SchedDbId          (long) -- scheduled part requirement dbid
*      an_SchedId            (long) -- scheduled part requirement id
*      an_SchedPartId        (long) -- shceduled part requirement part id
*      an_RemovedReasonDbid  (long) -- removed reason database id
*      as_RemovedReasonCd    (char) -- removed reasin code
*
* Return:      on_Return     (long) - 1 if no error found
*
*
* Description: This procedure generates rows in sched_inst_part (install_bool=true),
*              sched_rmvd_part (remove_bool=true) tables based on values in sched_part table.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Jonathan Clarkin
* Recent Date:  September 30, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenerateRemoveInstallPartReq (
       an_InvDbId             IN inv_inv.inv_no_db_id%TYPE,
       an_InvId               IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
       an_SchedId             IN sched_stask.sched_id%TYPE,
       an_SchedPartId         IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid   IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd     IN task_part_list.remove_reason_cd%TYPE,
       ab_Remove_bool         IN task_part_list.remove_bool%TYPE,
       ab_Install_bool        IN task_part_list.install_bool%TYPE,
       on_Return              OUT typn_RetCode

   ) IS

   /* local variables */
   ln_RmvdIndex NUMBER;    -- index for the removed inventory
   ln_InstIndex NUMBER;    -- index for the installed inventory
   ln_NumOfInserts NUMBER; -- number of expected inserts to sched_inst_part/sched_rmvd_part table
   ln_QtNumber NUMBER;     -- quantity number
   ln_count NUMBER;        -- count

   /* used to get part requirements details */
   CURSOR lcur_SchedPart (
         cn_SchedDbId   sched_part.sched_db_id%TYPE,
         cn_SchedId     sched_part.sched_id%TYPE,
         cn_SchedPartId sched_part.sched_part_id%TYPE
      ) IS
      SELECT
         sched_part.sched_bom_part_db_id,
         sched_part.sched_bom_part_id,
         sched_part.sched_qt,
         sched_part.spec_part_no_db_id,
         sched_part.spec_part_no_id,
         eqp_bom_part.inv_class_cd
      FROM
         sched_part,
         eqp_bom_part
      WHERE
         sched_part.sched_db_id   = cn_SchedDbId AND
         sched_part.sched_id      = cn_SchedId AND
         sched_part.sched_part_id = cn_SchedPartId AND
         sched_part.rstat_cd  = 0
         AND
         eqp_bom_part.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_bom_part.bom_part_id    = sched_part.sched_bom_part_id;

         lrec_SchedPart lcur_SchedPart%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*  get list of part requirements */
   OPEN  lcur_SchedPart( an_SchedDbId, an_SchedId, an_SchedPartId );
   FETCH lcur_SchedPart INTO lrec_SchedPart;
   CLOSE lcur_SchedPart;


   /* initialize variables */
   ln_NumOfInserts := 1; -- default number of inserts
   ln_InstIndex    := 1; -- starting inst part id
   ln_RmvdIndex    := 1; -- starting rmvd part id

   ln_QtNumber := lrec_SchedPart.sched_qt; -- initialize default quantity to the part requirement qt

   /*if bom part is of class SER, we will insert sched_qt number of rows, each of them contaning one qt*/
   IF ( lrec_SchedPart.inv_class_cd = 'SER' ) THEN
     ln_NumOfInserts := lrec_SchedPart.sched_qt;
     ln_QtNumber     := 1;
   END IF;


   ln_count := 1;

   /* insert rows into the tables */
   FOR ln_count IN 1..ln_NumOfInserts
   LOOP

     /*if remove part requirement */
     IF ( ab_Remove_bool = 1 ) THEN

         /* add new remove part requirement, this will set serial no, part no,
          * removed reason to default value if necessary, or to the actual removed inventory value */
         AddRmvdPartRequirement (
               an_InvDbId ,
               an_InvId,
               an_SchedDbId,
               an_SchedId,
               an_SchedPartId,
               an_RemovedReasonDbid,
               as_RemovedReasonCd,
               ln_RmvdIndex,
               on_Return
         );

         IF on_Return < 0 THEN
            RETURN;
         END IF;

      END IF;

      /*if install part requirement */
      IF ( ab_Install_bool = 1 ) THEN

         INSERT INTO sched_inst_part (
            sched_db_id,
            sched_id,
            sched_part_id,
            sched_inst_part_id,
            part_no_db_id,
            part_no_id,
            serial_no_oem,
            inst_qt
         )
         VALUES (
            an_SchedDbId,
            an_SchedId,
            an_SchedPartId,
            ln_InstIndex,
            lrec_SchedPart.spec_part_no_db_id,
            lrec_SchedPart.spec_part_no_id,
            null,
            ln_QtNumber
         );

         ln_InstIndex := ln_InstIndex+1;

      END IF;

   END LOOP;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
     on_Return := icn_Error;
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenerateRemoveInstallPartReq@@@'||SQLERRM);

END GenerateRemoveInstallPartReq;

/********************************************************************************
*
* Procedure:    FindBomInventoryInTree
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. You will search your direct
                children and parents first. If no children or parents have this
                bom item, then you will start to search siblings.
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindBomInventoryInTree(
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_StartAssmblDbId    IN eqp_assmbl.assmbl_db_id%TYPE,
      as_StartAssmblCd      IN eqp_assmbl.assmbl_cd%TYPE,
      an_StartAssmblBomId   IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      otabn_InvNoDbId      OUT typtabn_DbId,
      otabn_InvNoId        OUT typtabn_Id,
      on_Return            OUT typn_RetCode
   ) IS

   /* find all parents and children of this inv that have the given BOM */
   CURSOR lcur_ParentOrChildInv (
         cn_StartInvNoDbId    inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    eqp_assmbl.assmbl_db_id%TYPE,
         cn_FindAssmblCd      eqp_assmbl.assmbl_cd%TYPE,
         cn_FindAssmblBomId   eqp_assmbl_bom.assmbl_bom_id%TYPE
      ) IS
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      FROM
         inv_inv
      WHERE
         ( inv_inv.inv_no_db_id, inv_inv.inv_no_id ) IN
         ( SELECT
               inv_no_db_id,
               inv_no_id
            FROM
               inv_inv
            WHERE rstat_cd = 0
            START WITH
               inv_no_db_id = cn_StartInvNoDbId AND
               inv_no_id    = cn_StartInvNoId
            CONNECT BY
               nh_inv_no_db_id = PRIOR inv_no_db_id AND
               nh_inv_no_id    = PRIOR inv_no_id )
         AND
         (
           (
             inv_inv.assmbl_db_id  = cn_FindAssmblDbId AND
             inv_inv.assmbl_cd     = cn_FindAssmblCd AND
             inv_inv.assmbl_bom_id = cn_FindAssmblBomId AND
             inv_inv.inv_class_cd <> 'SER'              AND
             inv_inv.rstat_cd = 0
           )
           OR
           (
             inv_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
             inv_inv.orig_assmbl_cd    = cn_FindAssmblCd AND
             cn_FindAssmblBomId = 0              AND
             inv_inv.rstat_cd = 0
           )
         )
      UNION
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      FROM
         inv_inv
      WHERE
         ( inv_inv.inv_no_db_id, inv_inv.inv_no_id ) IN
         ( SELECT
               inv_no_db_id,
               inv_no_id
            FROM
               inv_inv
            WHERE rstat_cd = 0
            START WITH
               inv_no_db_id = cn_StartInvNoDbId AND
               inv_no_id    = cn_StartInvNoId
            CONNECT BY
               inv_no_db_id = PRIOR nh_inv_no_db_id AND
               inv_no_id    = PRIOR nh_inv_no_id )
         AND
         (
           (
             inv_inv.assmbl_db_id  = cn_FindAssmblDbId AND
             inv_inv.assmbl_cd     = cn_FindAssmblCd AND
             inv_inv.assmbl_bom_id = cn_FindAssmblBomId AND
             inv_inv.inv_class_cd <> 'SER'              AND
             inv_inv.rstat_cd = 0
           )
           OR
           (
             inv_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
             inv_inv.orig_assmbl_cd    = cn_FindAssmblCd AND
             cn_FindAssmblBomId = 0              AND
             inv_inv.rstat_cd = 0
           )
         );

   /* find all siblings of this inv that have a particular BOM */
   CURSOR lcur_SiblingInv (
         cn_StartInvNoDbId    inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    eqp_assmbl.assmbl_db_id%TYPE,
         cn_FindAssmblCd      eqp_assmbl.assmbl_cd%TYPE,
         cn_FindAssmblBomId   eqp_assmbl_bom.assmbl_bom_id%TYPE
      ) IS
      SELECT
         target_inv.inv_no_db_id,
         target_inv.inv_no_id,
         target_inv.inv_no_sdesc
      FROM
         inv_inv start_inv,
         inv_inv target_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId              AND
         start_inv.rstat_cd   = 0
         AND
         target_inv.assmbl_db_id  = cn_FindAssmblDbId AND
         target_inv.assmbl_cd     = cn_FindAssmblCd AND
         target_inv.assmbl_bom_id = cn_FindAssmblBomId AND
         target_inv.inv_class_cd <> 'SER'              AND
         target_inv.rstat_cd    = 0
         AND
         (
           (
             target_inv.assmbl_inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
             target_inv.assmbl_inv_no_id    = start_inv.assmbl_inv_no_id
           )
           OR
           (
             target_inv.assmbl_inv_no_db_id = start_inv.inv_no_db_id AND
             target_inv.assmbl_inv_no_id    = start_inv.inv_no_id
           )
         );

   /* local variables */
   ln_Index NUMBER;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* find the list of inventory that child tasks should be created on */
   otabn_InvNoDbId.DELETE;
   otabn_InvNoId.DELETE;

   /* if you are looking for the same bom item that you started with,
      then simply use the starting inventory */
   IF an_StartAssmblDbId  = an_FindAssmblDbId AND
      as_StartAssmblCd    = as_FindAssmblCd AND
      an_StartAssmblBomId = an_FindAssmblBomId THEN

      /* simply return the given inventory */
      otabn_InvNoDbId(1) := an_StartInvNoDbId;
      otabn_InvNoId(1)   := an_StartInvNoId;

      /* return success */
      on_Return := icn_Success;
      RETURN;
   END IF;

   /* find any other inventory that are directly above, or directly below
      the starting inventory */
   ln_Index := 0;
   FOR lrec_ParentOrChildInv IN lcur_ParentOrChildInv (
            an_StartInvNoDbId,
            an_StartInvNoId,
            an_FindAssmblDbId,
            as_FindAssmblCd,
            an_FindAssmblBomId )
   LOOP
      ln_Index := ln_Index + 1;
      otabn_InvNoDbId(ln_Index) := lrec_ParentOrChildInv.inv_no_db_id;
      otabn_InvNoId(ln_Index)   := lrec_ParentOrChildInv.inv_no_id;
   END LOOP;

   /* if there are no inventory directly above or below that start_inv for
      this BOM, then look for siblings */
   IF ln_Index = 0 THEN

      FOR lrec_SiblingInv IN lcur_SiblingInv (
            an_StartInvNoDbId,
            an_StartInvNoId,
            an_FindAssmblDbId,
            as_FindAssmblCd,
            an_FindAssmblBomId )
      LOOP
         ln_Index := ln_Index + 1;
         otabn_InvNoDbId(ln_Index) := lrec_SiblingInv.inv_no_db_id;
         otabn_InvNoId(ln_Index)   := lrec_SiblingInv.inv_no_id;
      END LOOP;

   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindBomInventoryInTree@@@'||SQLERRM);
     RETURN;
END FindBomInventoryInTree;


/********************************************************************************
*
* Procedure:    GenForecastedTasks
* Arguments:    an_SchedDbId (long) - the task whose dependencies should be created
                an_SchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will generate all task dependencies
                that fall within the next forecast_range_qt number of days.
*
* Orig.Coder:   A. Hircock
* Recent Coder: Jonathan R. Clarkin
* Recent Date:  February 24, 2009
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenForecastedTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      os_ExitCd    OUT VARCHAR2,
      on_Return    OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* this cursor will return details about the task and its definition */
   CURSOR lcur_TaskDetails (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT
         DECODE(
            evt_sched_dead.sched_dead_dt,
            NULL,
            SYSDATE,
            evt_sched_dead.sched_dead_dt) sched_dead_gdt,
         DECODE( task_task.forecast_range_qt, NULL, 0, task_task.forecast_range_qt ) forecast_range_qt,
         DECODE( h_inv_inv.inv_class_cd, 'ACFT', 1, 0 ) on_wing_bool,
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_event.hist_bool,
         evt_event.event_status_cd,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         task_task.task_db_id,
         task_task.task_id,
         mim_data_type.domain_type_cd,
         sched_stask.orphan_frct_bool
      FROM
         sched_stask,
         task_task,
         evt_event,
         evt_inv,
         inv_inv h_inv_inv,
         evt_sched_dead,
         mim_data_type
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         evt_event.event_db_id = cn_SchedDbId AND
         evt_event.event_id    = cn_SchedId     AND
         evt_event.rstat_cd   = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         evt_inv.event_db_id = sched_stask.sched_db_id AND
         evt_inv.event_id    = sched_stask.sched_id
         AND
         h_inv_inv.inv_no_db_id = evt_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id    = evt_inv.h_inv_no_id
         AND
         evt_sched_dead.event_db_id       (+)= sched_stask.sched_db_id AND
         evt_sched_dead.event_id          (+)= sched_stask.sched_id AND
         evt_sched_dead.sched_driver_bool (+)= 1
         AND
         mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
         mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

/* in the forecast chain, find the ACTV head task and record the deadline */
 CURSOR lcur_ActiveTaskInfo (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT evt_sched_dead.sched_dead_dt,
             evt_event.sched_priority_cd,
             DECODE ( evt_event_rel.rel_event_db_id, NULL, 0, 1) AS has_forecast
      FROM   evt_event,
             evt_event_rel,
             evt_sched_dead,
             (SELECT     evt_event_rel.event_db_id,
                         evt_event_rel.event_id
              FROM       evt_event_rel
              START WITH rel_event_db_id      = cn_SchedDbId AND
                         rel_event_id         = cn_SchedId   AND
                         rel_type_cd      = 'DEPT'
              CONNECT BY rel_event_db_id  = PRIOR event_db_id AND
                         rel_event_id     = PRIOR event_id  AND
                         rel_type_cd      = 'DEPT'
              UNION ALL
              SELECT     cn_SchedDbId AS event_db_id,
                         cn_SchedId   AS event_id
              FROM       dual
              ) chain
      WHERE   evt_event.event_db_id     = chain.event_db_id AND
              evt_event.event_id        = chain.event_id    AND
              evt_event.event_status_cd = 'ACTV' AND
              evt_event.rstat_cd = 0
              AND
              evt_sched_dead.event_db_id       = evt_event.event_db_id AND
              evt_sched_dead.event_id          = evt_event.event_id    AND
              evt_sched_dead.sched_driver_bool = 1
              AND
              -- get the next task if it exists
              evt_event_rel.event_db_id (+)= evt_event.event_db_id AND
              evt_event_rel.event_id    (+)= evt_event.event_id    AND
              evt_event_rel.rel_type_cd (+)= 'DEPT';
   lrec_ActiveTaskInfo lcur_ActiveTaskInfo%ROWTYPE;


   /* this SQL will retrieve a list of all task definitions that are dependencies
      to this sched_task, but have not been instantiated yet */
   CURSOR lcur_DependentTask (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         DECODE( task_part_map.task_id, NULL, 0, 1) AS part_based
      FROM
         sched_stask init_stask,
         task_task_dep,
         evt_inv,
         inv_inv,
         task_task,
         task_part_map
      WHERE
         init_stask.sched_db_id = cn_SchedDbId AND
         init_stask.sched_id    = cn_SchedId   AND
         init_stask.rstat_cd  = 0
         AND
         task_task_dep.task_db_id = init_stask.task_db_id AND
         task_task_dep.task_id    = init_stask.task_id
         AND
         task_task_dep.task_dep_action_cd = 'CRT' AND
         task_task.task_defn_db_id        = task_task_dep.dep_task_defn_db_id AND
         task_task.task_defn_id           = task_task_dep.dep_task_defn_id
         AND
         evt_inv.event_db_id = init_stask.sched_db_id AND
         evt_inv.event_id    = init_stask.sched_id
         AND
         inv_inv.part_no_db_id (+)= evt_inv.part_no_db_id   AND
         inv_inv.part_no_id    (+)= evt_inv.part_no_id
         AND
         task_part_map.task_db_id (+)= init_stask.task_db_id AND
         task_part_map.task_id    (+)= init_stask.task_id
         AND
         -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         (task_task.assmbl_db_id IS NOT NULL
          OR
          (
            inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
            inv_inv.part_no_id    = task_part_map.part_no_id
          )
         )
         AND
         NOT EXISTS
         (
           SELECT
              1
           FROM
              sched_stask dep_stask,
              evt_event_rel,
              task_task dep_task
           WHERE
              evt_event_rel.event_db_id = init_stask.sched_db_id AND
              evt_event_rel.event_id    = init_stask.sched_id AND
              evt_event_rel.rel_type_cd = 'DEPT'
              AND
              dep_stask.sched_db_id = evt_event_rel.rel_event_db_id AND
              dep_stask.sched_id    = evt_event_rel.rel_event_id
              AND
              dep_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
              dep_task.task_defn_id    = task_task_dep.dep_task_defn_id
              AND
              dep_stask.task_db_id = dep_task.task_db_id AND
              dep_stask.task_id    = dep_task.task_id
         )
    UNION

      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         DECODE( task_part_map.task_id, NULL, 0, 1) AS part_based
      FROM
         sched_stask init_stask,
         task_task_dep,
         task_task,
         task_task orig_task_task,
         evt_inv,
         inv_inv,
         task_part_map
      WHERE
         init_stask.sched_db_id = cn_SchedDbId AND
         init_stask.sched_id    = cn_SchedId AND
         init_stask.rstat_cd  = 0
         AND
         -- Get current task definition
         orig_task_task.task_db_id = init_stask.task_db_id AND
         orig_task_task.task_id    = init_stask.task_id
         AND
         -- Get POSTCRT relation only
         task_task_dep.task_dep_action_cd = 'POSTCRT'
         AND
         -- Join current task definition to the right columns in task_task_dep
         task_task_dep.dep_task_defn_db_id = orig_task_task.task_defn_db_id AND
         task_task_dep.dep_task_defn_id    = orig_task_task.task_defn_id
         AND
         -- Ensure the dependent task definition is active
         task_task.task_db_id = task_task_dep.task_db_id AND
         task_task.task_id    = task_task_dep.task_id
         AND
         task_task.task_def_status_cd = 'ACTV'
         AND
         evt_inv.event_db_id = init_stask.sched_db_id AND
         evt_inv.event_id    = init_stask.sched_id
         AND
         inv_inv.part_no_db_id (+)= evt_inv.part_no_db_id   AND
         inv_inv.part_no_id    (+)= evt_inv.part_no_id
         AND
         task_part_map.task_db_id (+)= init_stask.task_db_id AND
         task_part_map.task_id    (+)= init_stask.task_id
         AND
          -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         ( task_task.assmbl_db_id IS NOT NULL
           OR
           (
             inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
             inv_inv.part_no_id    = task_part_map.part_no_id
           )
         )

         AND
            NOT EXISTS
            (
               SELECT
                  1
               FROM
                  sched_stask dep_stask,
                  evt_event_rel,
                  task_task dep_task
               WHERE
                  evt_event_rel.event_db_id = init_stask.sched_db_id AND
                  evt_event_rel.event_id    = init_stask.sched_id AND
                  evt_event_rel.rel_type_cd = 'DEPT'
                  AND
                  dep_stask.sched_db_id = evt_event_rel.rel_event_db_id AND
                  dep_stask.sched_id    = evt_event_rel.rel_event_id
                  AND
                  dep_task.task_db_id = task_task_dep.task_db_id AND
                  dep_task.task_id   = task_task_dep.task_id
                  AND
                  dep_stask.task_db_id = dep_task.task_db_id AND
                  dep_stask.task_id    = dep_task.task_id
             );
             lrec_DependentTask lcur_DependentTask%ROWTYPE;

   /* local variables */
   ltabn_DepTaskInvNoDbId typtabn_DbId;
   ltabn_DepTaskInvNoId   typtabn_Id;
   ln_NewSchedDbId        sched_stask.sched_db_id%TYPE;
   ln_NewSchedId          sched_stask.sched_id%TYPE;
   ln_TaskDbId            task_defn.task_defn_db_id%TYPE;
   ln_TaskId              task_defn.task_defn_id%TYPE;
   ln_ForecastRangeQt     task_task.forecast_range_qt%TYPE;
   ln_ActiveDeadlineDt    evt_sched_dead.sched_dead_dt%TYPE;
   lb_GenerateForecast    BOOLEAN;
   ln_Count   NUMBER;     /* number of scheduled rules */

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;
   os_ExitCd := 'NORM';
   lb_GenerateForecast := TRUE;

   /* get details about the task and its definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
   IF lrec_TaskDetails.Orphan_Frct_Bool = 1 THEN
      on_Return := icn_Success;
      os_ExitCd := 'ORPHAN';
      RETURN;
   END IF;

   /* we should not generate forcasted tasks for canceled tasks */
   IF ( lrec_TaskDetails.hist_bool = 1 AND NOT ( lrec_TaskDetails.event_status_cd = 'COMPLETE' ) ) THEN
      on_Return := icn_Success;
      os_ExitCd := 'CANCEL';
      RETURN;
   END IF;

   /* do not forecast tasks with forecast range set to 0 */
   IF lrec_TaskDetails.forecast_range_qt = 0 AND lrec_TaskDetails.hist_bool = 0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'ZERO_RNG';
      RETURN;
   END IF;

   /* do not forecast more than the very next task for usage-based intervals for off-wing components */
   IF ( lrec_TaskDetails.on_wing_bool = 0 AND lrec_TaskDetails.domain_type_cd <> 'CA' ) AND lrec_TaskDetails.hist_bool = 0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'OFF_WING';
      RETURN;
   END IF;

   /* check if the current task has scheduling rule defined */
   SELECT
      COUNT(*)
   INTO
      ln_Count
   FROM
      task_sched_rule
   WHERE
      task_sched_rule.task_db_id = lrec_TaskDetails.task_db_id AND
      task_sched_rule.task_id    = lrec_TaskDetails.task_id;

   /* if the current task is active, and scheduled rule was not defined do not
      generate more tasks. When current task is completed and does not have
      schedule rules defined we will attempt to generate one more task.*/
   IF ln_Count = 0 AND lrec_TaskDetails.hist_bool=0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'NO_RULE';
      RETURN;
   END IF;

   /* retrieve the ACTV task deadline date in order to add the forcaste range. */
   OPEN lcur_ActiveTaskInfo(an_SchedDbId, an_SchedId);
   FETCH lcur_ActiveTaskInfo INTO lrec_ActiveTaskInfo;
   IF lcur_ActiveTaskInfo%FOUND THEN
      ln_ActiveDeadlineDt := lrec_ActiveTaskInfo.sched_dead_dt;
      IF  lrec_ActiveTaskInfo.sched_priority_cd = 'O/D' AND lrec_ActiveTaskInfo.has_forecast = 1 THEN
          lb_GenerateForecast := FALSE;
      END IF;
   ELSE
      ln_ActiveDeadlineDt := SYSDATE;
   END IF;
   CLOSE lcur_ActiveTaskInfo;

    /* only generate dependent tasks if the current task's deadline falls before
      the forecast_range_qt number of days and the ACTV is not O/D with a FORECAST task already */
   IF lrec_TaskDetails.hist_bool=0 AND
      ( lb_GenerateForecast = FALSE OR
       lrec_TaskDetails.sched_dead_gdt > (ln_ActiveDeadlineDt + lrec_TaskDetails.forecast_range_qt ))    THEN
      on_Return := icn_Success;
      os_ExitCd := 'PAST_RNG';
      RETURN;
   END IF;

   /* There is a limit on number of open cursors, in order to keep the number of open cursors
      not to exceed its maximum limit we have specified number of maximum forecasted active tasks.
      We get the number of already forecasted tasks.*/

   SELECT
      COUNT(*)
   INTO
      ln_Count
   FROM
      evt_event
   WHERE
      hist_bool = 0  AND
      rstat_cd = 0
      AND
      (event_db_id, event_id) IN
      (  SELECT
            event_db_id,
            event_id
         FROM
            evt_event_rel
         START WITH
            rel_event_db_id = an_SchedDbId AND
            rel_event_id    = an_SchedId AND
            rel_type_cd  = 'DEPT'
         CONNECT BY
            rel_event_db_id  = PRIOR event_db_id AND
            rel_event_id     = PRIOR event_id  AND
            rel_type_cd  = 'DEPT'
      );

   /* the maximum number of non historic forecasted tasks is set to 200.
      Do not forecast any more tasks, if 200 tasks were forecasted.*/
   IF (ln_Count > 200) THEN
      on_Return := icn_Success;
      os_ExitCd := 'MAX';
      RETURN;
   END IF;

   /* find a list of all dependent tasks */
   FOR lrec_DependentTask IN lcur_DependentTask( an_SchedDbId, an_SchedId )
   LOOP
      IF lrec_DependentTask.part_based > 0 THEN

        /* Determine whether the given task/inventory combination are legal */
         SELECT
            COUNT(*)
         INTO
            ln_Count
         FROM
            inv_inv
            INNER JOIN task_part_map ON
                       task_part_map.part_no_db_id = inv_inv.part_no_db_id AND
                       task_part_map.part_no_id    = inv_inv.part_no_id
            INNER JOIN task_task ON
                       task_task.task_db_id = task_part_map.task_db_id AND
                       task_task.task_id    = task_part_map.task_id
         WHERE
            task_task.task_defn_db_id = lrec_DependentTask.task_defn_db_id AND
            task_task.task_defn_id    = lrec_DependentTask.task_defn_id
            AND
            inv_inv.inv_no_db_id  = lrec_TaskDetails.inv_no_db_id AND
            inv_inv.inv_no_id     = lrec_TaskDetails.inv_no_id   AND
            inv_inv.rstat_cd  = 0;

         IF ln_Count > 0 THEN
            ltabn_DepTaskInvNoDbId.DELETE();
            ltabn_DepTaskInvNoId.DELETE();

            ltabn_DepTaskInvNoDbId(1) := lrec_TaskDetails.inv_no_db_id;
            ltabn_DepTaskInvNoId(1)   := lrec_TaskDetails.inv_no_id;
         END IF;

      ELSE
         /* find the inventory that the dependent task should be created on */
         FindBomInventoryInTree(
             lrec_TaskDetails.inv_no_db_id,
             lrec_TaskDetails.inv_no_id,
             lrec_TaskDetails.assmbl_db_id,
             lrec_TaskDetails.assmbl_cd,
             lrec_TaskDetails.assmbl_bom_id,
             lrec_DependentTask.assmbl_db_id,
             lrec_DependentTask.assmbl_cd,
             lrec_DependentTask.assmbl_bom_id,
             ltabn_DepTaskInvNoDbId,
             ltabn_DepTaskInvNoId,
             on_Return );
         IF on_Return < 1 THEN
            os_ExitCd := 'FAIL';
            RETURN;
         END IF;
      END IF;

      /* loop for every inventory that this task could be on */
      FOR i IN 1..ltabn_DepTaskInvNoDbId.COUNT
      LOOP

         ln_TaskDbId := 0;
         ln_TaskId := 0;

         /* determine the correct revision to generate */
         BEGIN

            /* set the context for the view */
            context_package.set_inv(ltabn_DepTaskInvNoDbId(i), ltabn_DepTaskInvNoId(i), on_Return);

            IF on_Return < 1 THEN
               os_ExitCd := 'FAIL';
               RETURN;
            END IF;

            SELECT
               vw_baseline_task.task_db_id,
               vw_baseline_task.task_id
            INTO
               ln_TaskDbId,
               ln_TaskId
            FROM
               vw_baseline_task
            WHERE
               vw_baseline_task.task_defn_db_id = lrec_DependentTask.Task_Defn_db_id AND
               vw_baseline_task.task_defn_id    = lrec_DependentTask.Task_Defn_id;

         /* If no rows found then we are not generating forecasted tasks */
         EXCEPTION WHEN NO_DATA_FOUND THEN
            on_Return := icn_Success;
            os_ExitCd := 'NO_DATA';
            RETURN;
         END;

         /* we are not generating forecasted tasks, if the selected task is OBSOLETE */
         IF (ln_TaskDbId IS NULL) THEN
            on_Return := icn_Success;
            os_ExitCd := 'OBSLT';
            RETURN;
         END IF;

         /* Obtain forecast range for the task definition revision */
         SELECT
            task_task.forecast_range_qt
         INTO
            ln_ForecastRangeQt
         FROM
            task_task
         WHERE
            task_task.task_db_id = ln_TaskDbId AND
            task_task.task_id    = ln_TaskId;

         /* generate dependent tasks only, if the initial task is historic, and the forcasted range of the dependent task is not 0 and the status of the dependent task is ACTV.  Otherwise deal with the next dependent task */
         IF NOT(ln_TaskDbId IS NULL AND ln_TaskId IS NULL) AND NOT (lrec_TaskDetails.hist_bool=0 AND ln_ForecastRangeQt=0) THEN

            /* create the dependent task */
            GenSchedTask(
                     null,
                     null,
                     ltabn_DepTaskInvNoDbId(i),
                     ltabn_DepTaskInvNoid(i),
                     ln_TaskDbId,
                     ln_TaskId,
                     an_SchedDbId,
                     an_SchedId,
                     null,
                     null,
                     null,
                     null,
                     null,
                     null,
                     false, -- Indicates that the procedure is being called internally
                     false, -- Indicated thet create labour, part, tool, forecast tasks, auto create children will be run
                     false,
                     NULL,
                     ln_NewSchedDbId,
                     ln_NewSchedId,
                     on_Return );
            IF on_Return < 1 THEN
               os_ExitCd := 'FAIL';
               RETURN;
            END IF;
         END IF;
      END LOOP;  -- Loop for creation of actual tasks

   END LOOP; -- Loop for lookup of potential tasks

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      CASE sqlcode
         WHEN -1841 THEN
            -- we went past the last day (ever (in oracle)), just say we are past the forecast range
            on_Return := icn_Success;
            os_ExitCd := 'PAST_RNG';
         ELSE
            -- Unexpected error
            on_Return := icn_Error;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenForecastedTasks@@@'||SQLERRM);
      END CASE;
      
      IF lcur_TaskDetails%ISOPEN THEN CLOSE lcur_TaskDetails; END IF;
      IF lcur_ActiveTaskInfo%ISOPEN THEN CLOSE lcur_ActiveTaskInfo; END IF;
      IF lcur_DependentTask%ISOPEN THEN CLOSE lcur_DependentTask; END IF;
      RETURN;
END GenForecastedTasks;



/********************************************************************************
*
* Procedure:    AutoCreateChildTasks
* Arguments:    an_SchedNoDbId   (long) - task's database id
*               an_SchedNoId     (long) - task's id
* Return:       on_Return        (long) - success/failure of procedure
*
* Description:  This procedure will create all child tasks that were defined as
*               "not on condition". It should be called when you are creating a
*               new task based on a task definition.
*
* Orig.Coder:   A. Hircock
* Recent Coder: Yui Sotozaki
* Recent Date:  January 09, 2008
*
*********************************************************************************
*
* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AutoCreateChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* This cursor will return some details about the task and its definition */
   CURSOR lcur_TaskDetails (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT
         sched_stask.main_inv_no_db_id AS inv_no_db_id,
         sched_stask.main_inv_no_id AS inv_no_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id
      FROM
         sched_stask
         INNER JOIN task_task ON
            task_task.task_db_id = sched_stask.task_db_id AND
            task_task.task_id    = sched_stask.task_id
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId
         AND
         sched_stask.rstat_cd = 0;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

   /* This cursor will provide a list of child task definitions for a
      particular task */
   CURSOR lcur_ChildTask (
         cn_SchedDbId task_task.task_db_id%TYPE,
         cn_SchedId   task_task.task_id%TYPE
      ) IS
      --    List of child tasks to create.
      SELECT
         jic_task_task.task_db_id,
         jic_task_task.task_id,
         jic_task_task.task_class_cd,
         jic_task_task.assmbl_db_id,
         jic_task_task.assmbl_cd,
         jic_task_task.assmbl_bom_id,
         task_part_map.task_id AS part_based,
         inv_inv.inv_no_db_id AS inv_no_db_id,
         inv_inv.inv_no_id    AS inv_no_id
      FROM
         sched_stask,
         task_task req_task_task,
         task_jic_req_map,
         task_task jic_task_task,
         task_part_map,
         inv_inv
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         req_task_task.task_db_id   = sched_stask.task_db_id   AND
         req_task_task.task_id      = sched_stask.task_id
         AND
         task_jic_req_map.req_task_defn_db_id   = req_task_task.task_defn_db_id  AND
         task_jic_req_map.req_task_defn_id      = req_task_task.task_defn_id
         AND
         jic_task_task.task_db_id   = task_jic_req_map.jic_task_db_id   AND
         jic_task_task.task_id      = task_jic_req_map.jic_task_id
         AND
         jic_task_task.task_def_status_cd = 'ACTV'
         AND
         inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
         inv_inv.inv_no_id    = sched_stask.main_inv_no_id
         AND
         task_part_map.task_db_id (+)= jic_task_task.task_db_id AND
         task_part_map.task_id    (+)= jic_task_task.task_id
         AND
         ( task_part_map.task_id IS NULL
           OR
           EXISTS
            ( SELECT
                 1
              FROM
                 eqp_part_no
              WHERE
                 eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
                 eqp_part_no.part_no_id    = inv_inv.part_no_id
                 AND
                 eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                 eqp_part_no.part_no_id    = task_part_map.part_no_id
            )
         );


   /* LOCAL VARIABLES */
   ltabn_InvNoDbId      typtabn_DbId;
   ltabn_InvNoId        typtabn_Id;
   ln_NewSchedDbId      sched_stask.sched_db_id%TYPE;
   ln_NewSchedId        sched_stask.sched_id%TYPE;
   ln_ChildrenCreated   NUMBER;

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get details about the task and its definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   ln_ChildrenCreated := 0;

   /* find all auto-create child tasks */
   FOR lrec_ChildTask IN lcur_ChildTask( an_SchedDbId, an_SchedId ) LOOP

   IF lrec_ChildTask.Part_Based IS NOT NULL THEN

   ltabn_InvNoDbId(1) := lrec_ChildTask.Inv_No_Db_Id;
   ltabn_InvNoId(1)   := lrec_ChildTask.Inv_No_Id;

   ELSE
       /* if the last know status of the child task is not CANCEL*/
        /* find  the target inventory in the current inventory tree */
        FindBomInventoryInTree(
                       lrec_TaskDetails.inv_no_db_id,
                       lrec_TaskDetails.inv_no_id,
                       lrec_TaskDetails.assmbl_db_id,
                       lrec_TaskDetails.assmbl_cd,
                       lrec_TaskDetails.assmbl_bom_id,
                       lrec_ChildTask.assmbl_db_id,
                       lrec_ChildTask.assmbl_cd,
                       lrec_ChildTask.assmbl_bom_id,
                       ltabn_InvNoDbId,
                       ltabn_InvNoId,
                       on_Return );
        IF on_Return < 1 THEN
           RETURN;
         END IF;
   END IF;
     /* create a child task on every inventory item */
     FOR i IN 1..ltabn_InvNoDbId.COUNT LOOP

         /* create the child task */
         GenSchedTask(
              null,
              null,
              ltabn_InvNoDbId(i),
              ltabn_InvNoId(i),
              lrec_ChildTask.task_db_id,
              lrec_ChildTask.task_id,
              -1,
              -1,
              null,
              null,
              null,
              null,
              null,
              null,
              false, -- Indicates that the procedure is being called internally
              false, -- Indicated thet create labour, part, tool, forecast tasks, auto create children will be run
              false,
              NULL,
              ln_NewSchedDbId,
              ln_NewSchedId,
              on_Return );
         IF on_Return < 1 THEN
            RETURN;
         END IF;

         /* increment the number of children that were created */
         ln_ChildrenCreated := ln_ChildrenCreated + 1;

         /* record the parent and order of this new child task */
         UPDATE
            evt_event
         SET
            nh_event_db_id = an_SchedDbId,
            nh_event_id    = an_SchedId,
            sub_event_ord  = ln_ChildrenCreated
         WHERE
            event_db_id = ln_NewSchedDbId AND
            event_id    = ln_NewSchedId;

     END LOOP;

   END LOOP;

   /* if any children were created, then record the highest task for this
      new child task */
   IF ln_ChildrenCreated > 0 THEN
      UPDATE
            evt_event
         SET
            h_event_db_id = an_SchedDbId,
            h_event_id    = an_SchedId
         WHERE
            ( event_db_id, event_id ) IN
            ( SELECT
                  event_db_id,
                  event_id
               FROM
                  evt_event
               WHERE rstat_cd = 0
               START WITH
                  event_db_id = an_SchedDbId AND
                  event_id    = an_SchedId
               CONNECT BY
                  nh_event_db_id = PRIOR event_db_id AND
                  nh_event_id    = PRIOR event_id );
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AutoCreateChildTasks@@@'||SQLERRM);
     RETURN;
END AutoCreateChildTasks;


/********************************************************************************
*
* Procedure:    AutoCreateOnCondChildTasks
* Arguments:    an_SchedNoDbId   (long) - task's database id
*               an_SchedNoId     (long) - task's id
* Return:       on_Return        (long) - success/failure of procedure
*
* Description:  This procedure will create all child tasks that were defined as
*               "on condition". It should be called when you are creating a
*               new mandatory block based on a task definition.
*
*********************************************************************************
*
* Copyright 1997-2013 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AutoCreateOnCondChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* This cursor will return some details about the task and its definition */
   CURSOR lcur_TaskDetails (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT
         sched_stask.main_inv_no_db_id AS inv_no_db_id,
         sched_stask.main_inv_no_id AS inv_no_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         evt_event.event_status_db_id,
         evt_event.event_status_cd
      FROM
         sched_stask
         INNER JOIN task_task ON
            task_task.task_db_id = sched_stask.task_db_id AND
            task_task.task_id    = sched_stask.task_id
         INNER JOIN evt_event ON
            evt_event.event_db_id = sched_stask.sched_db_id AND
            evt_event.event_id    = sched_stask.sched_id
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId
         AND
         sched_stask.rstat_cd = 0;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

   /* This cursor will provide a list of child task definitions for a
      particular task */
   CURSOR lcur_ChildTask (
         cn_SchedDbId task_task.task_db_id%TYPE,
         cn_SchedId   task_task.task_id%TYPE
      ) IS
      --    List of child tasks to create
      SELECT
         req_task_task.task_db_id,
         req_task_task.task_id,
         req_task_task.task_class_cd,
         req_task_task.assmbl_db_id,
         req_task_task.assmbl_cd,
         req_task_task.assmbl_bom_id,
         DECODE(task_part_map.task_id, NULL, 0, 1) AS part_based_bool,
         inv_inv.inv_no_db_id AS inv_no_db_id,
         inv_inv.inv_no_id    AS inv_no_id
      FROM
         sched_stask
         INNER JOIN inv_inv ON
            inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
            inv_inv.inv_no_id    = sched_stask.main_inv_no_id
         INNER JOIN task_task block_task_task ON
            block_task_task.task_db_id = sched_stask.task_db_id AND
            block_task_task.task_id    = sched_stask.task_id
         INNER JOIN task_block_req_map ON
            task_block_req_map.block_task_db_id = block_task_task.task_db_id AND
            task_block_req_map.block_task_id    = block_task_task.task_id
         INNER JOIN task_task req_task_task ON
            req_task_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
            req_task_task.task_defn_id    = task_block_req_map.req_task_defn_id
         LEFT OUTER JOIN task_part_map ON
            task_part_map.task_db_id = req_task_task.task_db_id AND
            task_part_map.task_id    = req_task_task.task_id
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         -- block must be mandatory, non-mandatory logic is elsewhere
         block_task_task.on_condition_bool = 0
         AND
         -- req must be on condition
         req_task_task.on_condition_bool = 1
         AND
         req_task_task.task_def_status_db_id = 0 AND
         req_task_task.task_def_status_cd    = 'ACTV'
         AND
         ( 
            task_part_map.task_id IS NULL
            OR
            inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
            inv_inv.part_no_id    = task_part_map.part_no_id
         );


   /* LOCAL VARIABLES */
   ltabn_InvNoDbId      typtabn_DbId;
   ltabn_InvNoId        typtabn_Id;
   ln_NewSchedDbId      sched_stask.sched_db_id%TYPE;
   ln_NewSchedId        sched_stask.sched_id%TYPE;
   ln_ChildrenCreated   NUMBER;

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get details about the task and its definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   ln_ChildrenCreated := 0;

   /* find all on condition child tasks */
   FOR lrec_ChildTask IN lcur_ChildTask( an_SchedDbId, an_SchedId ) LOOP

      IF lrec_ChildTask.part_based_bool = 1 THEN

         ltabn_InvNoDbId(1) := lrec_ChildTask.Inv_No_Db_Id;
         ltabn_InvNoId(1)   := lrec_ChildTask.Inv_No_Id;

      ELSE
         /* find  the target inventory in the current inventory tree */
         FindBomInventoryInTree(
               lrec_TaskDetails.inv_no_db_id,
               lrec_TaskDetails.inv_no_id,
               lrec_TaskDetails.assmbl_db_id,
               lrec_TaskDetails.assmbl_cd,
               lrec_TaskDetails.assmbl_bom_id,
               lrec_ChildTask.assmbl_db_id,
               lrec_ChildTask.assmbl_cd,
               lrec_ChildTask.assmbl_bom_id,
               ltabn_InvNoDbId,
               ltabn_InvNoId,
               on_Return );
         IF on_Return < 1 THEN
            RETURN;
         END IF;
      END IF;
      
      /* create a child task on every inventory item */
      FOR i IN 1..ltabn_InvNoDbId.COUNT LOOP

         /* create the child task */
         GenSchedTask(
              null,
              null,
              ltabn_InvNoDbId(i),
              ltabn_InvNoId(i),
              lrec_ChildTask.task_db_id,
              lrec_ChildTask.task_id,
              -1,
              -1,
              null,
              null,
              null,
              null,
              null,
              null,
              false, -- Indicates that the procedure is being called internally
              false, -- Indicated thet create labour, part, tool, forecast tasks, auto create children will be run
              false,
              NULL,
              ln_NewSchedDbId,
              ln_NewSchedId,
              on_Return );
         IF on_Return < 1 THEN
            RETURN;
         END IF;

         /* increment the number of children that were created */
         ln_ChildrenCreated := ln_ChildrenCreated + 1;

         /* record the parent, order and status of this new child task */
         UPDATE
            evt_event
         SET
            nh_event_db_id     = an_SchedDbId,
            nh_event_id        = an_SchedId,
            sub_event_ord      = ln_ChildrenCreated,
            event_status_db_id = lrec_TaskDetails.event_status_db_id,
            event_status_cd    = lrec_TaskDetails.event_status_cd
         WHERE
            event_db_id = ln_NewSchedDbId AND
            event_id    = ln_NewSchedId;

      END LOOP;

   END LOOP;

   /* if any children were created, then record the highest task for this
      new child task */
   IF ln_ChildrenCreated > 0 THEN
      UPDATE
         evt_event
      SET
         h_event_db_id = an_SchedDbId,
         h_event_id    = an_SchedId
      WHERE
         ( event_db_id, event_id ) IN
         ( SELECT
            event_db_id,
            event_id
         FROM
            evt_event
         WHERE
            rstat_cd = 0
         START WITH
            event_db_id = an_SchedDbId AND
            event_id    = an_SchedId
         CONNECT BY
            nh_event_db_id = PRIOR event_db_id AND
            nh_event_id    = PRIOR event_id );
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AutoCreateOnCondChildTasks@@@'||SQLERRM);
     RETURN;
END AutoCreateOnCondChildTasks;


/*******************************************************************************
*
* Procedure:    UpdateReplSchedPart
* Arguments:    an_SchedDbId   (long) - the task that should be updated
*               an_SchedId     (long) - ""
* Return:       on_Return      (long) - succss/failure of procedure
*
* Description:  Updated the replacement part requirement for the task and its
*               children. This will also work if the task is a child of a REPL.
*
********************************************************************************
*
* Copyright 1997-2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UpdateReplSchedPart(
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   ) IS

   /*
    * This cursor will lookup the replacement part requirement given a task.
    */
   CURSOR lcur_ReplSchedPart(
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
      SELECT
         sched_part.sched_db_id,
         sched_part.sched_id,
         sched_part.sched_part_id
      FROM
         (SELECT
            start_stask.sched_db_id,
            start_stask.sched_id,
            NVL(parent_stask.sched_db_id, start_stask.sched_db_id) AS repl_sched_db_id,
            NVL(parent_stask.sched_id, start_stask.sched_id) AS repl_sched_id
         FROM
            sched_stask start_stask
            INNER JOIN evt_event ON
               evt_event.event_db_id = start_stask.sched_db_id AND
               evt_event.event_id    = start_stask.sched_id
            LEFT OUTER JOIN sched_stask parent_stask ON
               parent_stask.sched_db_id = evt_event.nh_event_db_id AND
               parent_stask.sched_id    = evt_event.nh_event_id
         WHERE
            start_stask.task_class_cd = 'REPL'
            OR
            parent_stask.task_class_cd = 'REPL'
         ) repl_stask_tree
         INNER JOIN sched_stask repl_stask ON
            repl_stask.sched_db_id = repl_stask_tree.repl_sched_db_id AND
            repl_stask.sched_id    = repl_stask_tree.repl_sched_id
         INNER JOIN task_task ON
            task_task.task_db_id = repl_stask.task_db_id AND
            task_task.task_id    = repl_stask.task_id
         INNER JOIN sched_part ON
            sched_part.sched_db_id = repl_stask_tree.sched_db_id AND
            sched_part.sched_id    = repl_stask_tree.sched_id
            AND
            sched_part.assmbl_db_id  = task_task.repl_assmbl_db_id AND
            sched_part.assmbl_cd     = task_task.repl_assmbl_cd AND
            sched_part.assmbl_bom_id = task_task.repl_assmbl_bom_id
         INNER JOIN evt_event root_event ON
            (
               root_event.event_db_id = repl_stask_tree.repl_sched_db_id AND
               root_event.event_id    = repl_stask_tree.repl_sched_id
            )
            OR
            (
               root_event.event_db_id = repl_stask_tree.sched_db_id AND
               root_event.event_id    = repl_stask_tree.sched_id
            )
            OR
            (
               root_event.nh_event_db_id = repl_stask_tree.repl_sched_db_id AND
               root_event.nh_event_id    = repl_stask_tree.repl_sched_id
            )
      WHERE
         root_event.event_db_id = cn_SchedDbId AND
         root_event.event_id    = cn_SchedId
         AND
         EXISTS (
            SELECT
               1
            FROM
               sched_rmvd_part
            WHERE
               sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
               sched_rmvd_part.sched_id      = sched_part.sched_id AND
               sched_rmvd_part.sched_part_id = sched_part.sched_part_id
         );
   lrec_ReplSchedPart lcur_ReplSchedPart%ROWTYPE;

   /**
    * Find the tasks in the tree under the replacement including the replacement
    */
   CURSOR lcur_ReplTree(
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      evt_event.event_db_id,
      evt_event.event_id
   FROM
      sched_stask repl_stask
      INNER JOIN evt_event ON
         evt_event.nh_event_db_id = repl_stask.sched_db_id AND
         evt_event.nh_event_id    = repl_stask.sched_id
         OR
         evt_event.event_db_id = repl_stask.sched_db_id AND
         evt_event.event_id    = repl_stask.sched_id
   WHERE
      repl_stask.sched_db_id = cn_SchedDbId AND
      repl_stask.sched_id    = cn_SchedId
      AND
      repl_stask.task_class_cd = 'REPL'
   UNION ALL
   SELECT
      evt_event.event_db_id,
      evt_event.event_id
   FROM
      evt_event start_event
      INNER JOIN sched_stask repl_stask ON
         repl_stask.sched_db_id = start_event.nh_event_db_id AND
         repl_stask.sched_id    = start_event.nh_event_id
      INNER JOIN evt_event ON
         evt_event.nh_event_db_id = repl_stask.sched_db_id AND
         evt_event.nh_event_id    = repl_stask.sched_id
         OR
         evt_event.event_db_id = repl_stask.sched_db_id AND
         evt_event.event_id    = repl_stask.sched_id
   WHERE
      start_event.event_db_id = cn_SchedDbId AND
      start_event.event_id    = cn_SchedId
      AND
      repl_stask.task_class_cd = 'REPL';
   lrec_ReplTree lcur_ReplTree%ROWTYPE;

   -- stores the part requirement that contains the replacement
   ln_SchedDbId  sched_part.sched_db_id%TYPE;
   ln_SchedId     sched_part.sched_id%TYPE;
   ln_SchedPartId sched_part.sched_part_id%TYPE;

   lb_ExecutedSchedPartLookup NUMBER;

BEGIN

   lb_ExecutedSchedPartLookup := 0;

   -- if this task is in a REPL tree
   OPEN lcur_ReplTree( an_SchedDbId, an_SchedId );
   LOOP
      FETCH lcur_ReplTree INTO lrec_ReplTree;
      EXIT WHEN lcur_ReplTree%NOTFOUND;

      IF lb_ExecutedSchedPartLookup = 0 THEN

         OPEN lcur_ReplSchedPart( an_SchedDbId, an_SchedId );
         FETCH lcur_ReplSchedPart INTO lrec_ReplSchedPart;
         IF lcur_ReplSchedPart%FOUND THEN
            ln_SchedDbId   := lrec_ReplSchedPart.sched_db_id;
            ln_SchedId     := lrec_ReplSchedPart.sched_id;
            ln_SchedPartId := lrec_ReplSchedPart.sched_part_id;
         END IF;
         CLOSE lcur_ReplSchedPart;

         lb_ExecutedSchedPartLookup := 1;
      END IF;

      -- update the task with the replacement part requirement
      UPDATE
         sched_stask
      SET
         repl_sched_db_id   = ln_SchedDbId,
         repl_sched_id      = ln_SchedId,
         repl_sched_part_id = ln_SchedPartId
      WHERE
         sched_db_id = lrec_ReplTree.event_db_id AND
         sched_id    = lrec_ReplTree.event_id;

   END LOOP;
   CLOSE lcur_ReplTree;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateReplSchedPart@@@'||SQLERRM);
     RETURN;

END UpdateReplSchedPart;

/********************************************************************************
*
* Procedure:    GenSchedParts
* Arguments:    an_SchedDbId (long) - the task that was just created
*               an_SchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will generate all of the rows in SCHED_PART that
*               come from the baseline task definition. This is a private method
*               that is only ever called from the GenSchedTask method
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenSchedParts(
      an_SchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_SchedId    IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   ) IS

   /* *** DECLARE LOCAL VARIABLES *** */
   ln_SchedPartId       sched_part.sched_part_id%TYPE;
   ln_IsJIC             NUMBER;
   /* *** DECLARE EXCEPTIONS *** */
   xc_UnknownSQLError              EXCEPTION;

   /* *** DECLARE CURSORS *** */
   /* get some details on the retrieved task */
   CURSOR lcur_TaskDetails (
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      task_task.task_class_cd,
      task_task.task_db_id,
      task_task.task_id,
      task_task.assmbl_db_id,
      task_task.assmbl_cd,
      task_task.assmbl_bom_id,
      evt_inv.assmbl_db_id  inv_assmbl_db_id,
      evt_inv.assmbl_cd     inv_assmbl_cd,
      evt_inv.assmbl_bom_id inv_assmbl_bom_id,
      evt_inv.assmbl_pos_id,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      evt_inv.bom_part_db_id,
      evt_inv.bom_part_id,
      assy.assmbl_db_id as root_assmbl_db_id,
      assy.assmbl_cd as root_assmbl_cd,
      assy.assmbl_bom_id as root_assmbl_bom_id,
      assy.assmbl_pos_id as root_assmbl_pos_id,
      comp_inv_inv.assmbl_db_id as comp_assmbl_db_id,
      comp_inv_inv.assmbl_cd as comp_assmbl_cd,
      comp_inv_inv.assmbl_bom_id as comp_assmbl_bom_id,
      comp_inv_inv.assmbl_pos_id as comp_assmbl_pos_id
   FROM
      task_task,
      evt_inv,
      sched_stask,
      inv_inv assy,
      inv_inv,
      evt_event,
      evt_event_rel,
      evt_inv comp_evt_inv,
      inv_inv comp_inv_inv
   WHERE
      sched_stask.sched_db_id = cn_SchedDbId AND
      sched_stask.sched_id    = cn_SchedId   AND
      sched_stask.rstat_cd = 0
      AND
      evt_inv.event_db_id = sched_stask.sched_db_id AND
      evt_inv.event_id    = sched_stask.sched_id AND
      evt_inv.main_inv_bool=1
      AND
      inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id = evt_inv.inv_no_id
      AND
      (
         (
            inv_inv.assmbl_inv_no_db_id IS NOT NULL AND
            assy.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
            assy.inv_no_id    = inv_inv.assmbl_inv_no_id
         )
         OR
         (
            inv_inv.assmbl_inv_no_db_id IS NULL AND
            assy.inv_no_db_id = inv_inv.h_inv_no_db_id AND
            assy.inv_no_id    = inv_inv.h_inv_no_id
         )
      )
      AND
      task_task.task_db_id = sched_stask.task_db_id AND
      task_task.task_id = sched_stask.task_id
      AND
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id = sched_stask.sched_id
      AND
      evt_event_rel.event_db_id (+)= evt_event.nh_event_db_id AND
      evt_event_rel.event_id (+)= evt_event.nh_event_id AND
      evt_event_rel.rel_type_cd (+)= 'WORMVL'
      AND
      comp_evt_inv.event_db_id (+)= evt_event_rel.rel_event_db_id AND
      comp_evt_inv.event_id (+)= evt_event_rel.rel_event_id AND
      comp_evt_inv.main_inv_bool(+) = 1
      AND
      comp_inv_inv.inv_no_db_id (+)= comp_evt_inv.inv_no_db_id AND
      comp_inv_inv.inv_no_id (+)= comp_evt_inv.inv_no_id;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

   /* used to get part list for a task defn */
   CURSOR lcur_Part (
      cn_TaskDbId sched_stask.sched_db_id%TYPE,
      cn_TaskId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      task_part_list.bom_part_db_id,
      task_part_list.bom_part_id,
      task_part_list.install_bool,
      task_part_list.remove_bool,
      task_part_list.req_qt,
      task_part_list.remove_reason_db_id,
      task_part_list.remove_reason_cd,
      task_part_list.assmbl_db_id,
      task_part_list.assmbl_cd,
      task_part_list.assmbl_bom_id,
      task_part_list.assmbl_pos_id,
      task_part_list.spec_part_no_db_id,
      task_part_list.spec_part_no_id,
      eqp_bom_part.inv_class_cd,
      task_part_list.req_action_db_id,
      task_part_list.req_action_cd
   FROM
      task_part_list,
      eqp_bom_part
   WHERE
      task_part_list.task_db_id = cn_TaskDbId AND
      task_part_list.task_id = cn_TaskId
      AND
      eqp_bom_part.bom_part_db_id=task_part_list.bom_part_db_id AND
      eqp_bom_part.bom_part_id=task_part_list.bom_part_id
   ORDER BY task_part_list.task_part_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get details about the task definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   /* check if the task defn. is JIC */
   ln_IsJIC := 0;

   SELECT
      COUNT(*) INTO ln_IsJIC
   FROM
      ref_task_class
   WHERE
      class_mode_cd = 'JIC'
      AND
      task_class_cd = lrec_TaskDetails.task_class_cd;

   /* create a row in sched_part for every row in task_part_list */
   ln_SchedPartId := 1;
   FOR lrec_Part IN lcur_Part( lrec_TaskDetails.task_db_id, lrec_TaskDetails.task_id )
   LOOP


  /* if the inventory it TRK or ASSY has the same bom part as the task use the task bom item position */
   IF (lrec_Part.inv_class_cd='TRK' OR lrec_Part.inv_class_cd='ASSY') AND
      lrec_TaskDetails.bom_part_db_id  = lrec_Part.bom_part_db_id AND
      lrec_TaskDetails.bom_part_id   = lrec_Part.bom_part_id  AND
      ln_IsJIC != 1 THEN

      lrec_Part.assmbl_db_id:=lrec_TaskDetails.inv_assmbl_db_id;
      lrec_Part.assmbl_cd:= lrec_TaskDetails.inv_assmbl_cd;
      lrec_Part.assmbl_bom_id:=lrec_TaskDetails.inv_assmbl_bom_id;
      lrec_Part.assmbl_pos_id:= lrec_TaskDetails.assmbl_pos_id;
   END IF;

   /* If this is a replacement job card use the position of part to be removed */
   IF (lrec_TaskDetails.comp_assmbl_db_id IS NOT NULL) THEN
      lrec_Part.assmbl_db_id:=lrec_TaskDetails.comp_assmbl_db_id;
      lrec_Part.assmbl_cd:= lrec_TaskDetails.comp_assmbl_cd;
      lrec_Part.assmbl_bom_id:=lrec_TaskDetails.comp_assmbl_bom_id;
      lrec_Part.assmbl_pos_id:= lrec_TaskDetails.comp_assmbl_pos_id;
   END IF;

         INSERT INTO sched_part (
               sched_db_id,
               sched_id,
               sched_part_id,
               sched_bom_part_db_id,
               sched_bom_part_id,
               sched_qt,
               nh_assmbl_db_id,
               nh_assmbl_cd,
               nh_assmbl_bom_id,
               nh_assmbl_pos_id,
               assmbl_db_id,
               assmbl_cd,
               assmbl_bom_id,
               assmbl_pos_id,
               sched_part_status_db_id,
               sched_part_status_cd,
               spec_part_no_db_id,
               spec_part_no_id,
               req_action_db_id,
               req_action_cd)
            VALUES (
               an_SchedDbId,
               an_SchedId,
               ln_SchedPartId,
               lrec_Part.bom_part_db_id,
               lrec_Part.bom_part_id,
               lrec_Part.req_qt,
               lrec_TaskDetails.root_assmbl_db_id,
               lrec_TaskDetails.root_assmbl_cd,
               lrec_TaskDetails.root_assmbl_bom_id,
               lrec_TaskDetails.root_assmbl_pos_id,
               lrec_Part.assmbl_db_id,
               lrec_Part.assmbl_cd,
               lrec_Part.assmbl_bom_id,
               lrec_Part.assmbl_pos_id,
               0,
               'ACTV',
               lrec_Part.spec_part_no_db_id,
               lrec_Part.spec_part_no_id,
               lrec_Part.req_action_db_id,
               lrec_Part.req_action_cd
               );

         GenerateRemoveInstallPartReq(
              lrec_TaskDetails.inv_no_db_id,
              lrec_TaskDetails.inv_no_id,
              an_SchedDbId,
              an_SchedId,
              ln_SchedPartId,
              lrec_Part.remove_reason_db_id,
              lrec_Part.remove_reason_cd,
              lrec_Part.remove_bool,
              lrec_Part.install_bool,
              on_Return);
         IF on_Return < 0 THEN
            RETURN;
         END IF;


         /* increment the sched_part_id counter in the PK*/
         ln_SchedPartId := ln_SchedPartId + 1;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenSchedParts@@@'||SQLERRM);
     RETURN;
END GenSchedParts;



/********************************************************************************
*
* Procedure:    UpdateHSched
* Arguments:    an_SchedNoDbId   (long) - task's database id
*               an_SchedNoId     (long) - task's id
* Return:       on_Return        (long) - succss/failure of procedure
*
* Description:  This procedure will update the reference between a task and its
*               root, non-check, task.
*
* Orig.Coder:   Randy A
* Recent Coder: N/A
* Recent Date:  Dec 5, 2006
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateHSched (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode
   ) IS

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* Run a single update that will traverse the event tree of the given task
      and set the h_sched_db_id:h_sched_id values accordingly. */

   UPDATE
      ( SELECT sched_stask.h_sched_db_id,
               sched_stask.h_sched_id,
               sched_stask.sched_db_id,
               sched_stask.sched_id
        FROM   sched_stask
        WHERE  /* Don't set checks */
               sched_stask.task_class_cd NOT IN ('RO', 'CHECK')   AND
               sched_stask.rstat_cd = 0
               AND
               ( sched_stask.sched_db_id, sched_stask.sched_id ) IN
               ( /* Use connect by prior.  This may not be a root task that's passed in. */
                 SELECT evt_event.event_db_id,
                        evt_event.event_id
                 FROM   evt_event
                 START WITH
                        evt_event.event_db_id = an_SchedDbId AND
                        evt_event.event_id    = an_SchedId
                 CONNECT BY
                        evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
                        evt_event.nh_event_id    = PRIOR evt_event.event_id
               )        
        ORDER BY sched_stask.sched_db_id, sched_stask.sched_id                       
      ) sched_stask
   SET
      ( sched_stask.h_sched_db_id, sched_stask.h_sched_id ) =
      (
         /* Find the higest non-check task looking upwards
            Order by level descending to ensure you get the highest in the hierarchy */
         SELECT DISTINCT
            FIRST_VALUE( evt_event.event_db_id ) OVER ( ORDER BY LEVEL DESC ),
            FIRST_VALUE( evt_event.event_id ) OVER ( ORDER BY LEVEL DESC )
         FROM
            evt_event
         WHERE rstat_cd = 0 AND
            /* Filter out checks */
            NOT EXISTS (
               SELECT
                  1
               FROM
                  sched_stask check_stask
               WHERE
                  check_stask.sched_db_id = evt_event.event_db_id AND
                  check_stask.sched_id    = evt_event.event_id
                  AND
                  check_stask.task_class_cd IN ('RO', 'CHECK')
            )
         START WITH
            evt_event.event_db_id =  sched_stask.sched_db_id AND
            evt_event.event_id    =  sched_stask.sched_id
         CONNECT BY
            evt_event.event_db_id = PRIOR evt_event.nh_event_db_id AND
            evt_event.event_id    = PRIOR evt_event.nh_event_id
      );

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateHSched@@@'||SQLERRM);

     RETURN;
END UpdateHSched;



/********************************************************************************
*
* Procedure:    UpdateTaskLabourSummary
* Arguments:    an_Months        (long) - task's database id
* Return:       on_Return        (long) - succss/failure of procedure
*
* Description:  This procedure will update dwt_task_labour_summary table as follows:
*               For each work scope task definition, find baselined labour requirement;
*               get its most recent update date in dwt_task_labour_summary table, if
*               not exists, use SYSDATE - aMonth; then for each completed task event
*               under this task definition, update ot insert data to dwt_task_labour_summary
*               table.
*
* Orig.Coder:   John Tang
* Recent Coder: N/A
* Recent Date:  Oct 15, 2007
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateTaskLabourSummary (
      an_Months            IN  NUMBER,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** LOCAL VARIABLES *** */
   ld_RecentUpdate     dwt_task_labour_summary.revision_dt%TYPE;
   ln_TaskDnId         task_task.task_db_id%TYPE;
   ln_SchedManPwCt     task_labour_list.man_pwr_ct%TYPE;
   ln_SchedManHr       task_labour_list.work_perf_hr%TYPE;
   ln_ActualManPwrCt   dwt_task_labour_summary.actual_man_pwr_ct%TYPE;
   ln_ActualTotalManHr dwt_task_labour_summary.actual_total_man_hr%TYPE;
   ln_LabourSkillDbId  dwt_task_labour_summary.labour_skill_db_id%TYPE;
   ls_LabourSkillCd    dwt_task_labour_summary.labour_skill_cd%TYPE;


   /* *** CURSOR DECLARATIONS *** */
   /* find the Config Slot based Work Scope task definitions that are AVCT, SUPRSEDE OR OBSOLETE */
   CURSOR lcur_FindWorkScopeTaskDefn IS
      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.task_db_id,
         task_task.task_id,
         task_task.revision_ord,
         task_task.assmbl_db_id,
         task_task.assmbl_cd
      FROM
         task_task
      WHERE
        task_task.workscope_bool = 1 AND
        task_task.task_def_status_cd IN ('ACTV', 'SUPRSEDE', 'OBSOLETE')
        AND
        task_task.assmbl_db_id IS NOT NULL;

   lrec_FindWorkScopeTaskDefn lcur_FindWorkScopeTaskDefn%ROWTYPE;

   /* find all completed actual task events since passed in date and exclude part no. based task defn */
   CURSOR lcur_GetCompletedTaskLabours (
          cd_EventDt   evt_event.event_dt %TYPE,
          cn_TaskDbId  task_task.task_db_id %TYPE,
          cn_TaskId    task_task.task_id %TYPE
      ) IS
      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id,
         sched_stask.barcode_sdesc,
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_dt,
         evt_inv.assmbl_inv_no_db_id,
         evt_inv.assmbl_inv_no_id,
         inv_inv.inv_no_sdesc
      FROM
         task_task,
         sched_stask,
         evt_event,
         evt_inv,
         inv_inv
      WHERE
        task_task.task_db_id = cn_TaskDbId AND
        task_task.task_id    = cn_TaskId
        AND
        task_task.assmbl_db_id IS NOT NULL
        AND
        sched_stask.task_db_id = task_task.task_db_id AND
        sched_stask.task_id    = task_task.task_id AND
        sched_stask.rstat_cd   = 0
        AND
        evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id AND
        evt_event.event_status_cd = 'COMPLETE' AND
        evt_event.event_dt > cd_EventDt
        AND
        evt_inv.event_db_id = evt_event.event_db_id AND
        evt_inv.event_id    = evt_event.event_id AND
        evt_inv.assmbl_inv_no_db_id IS NOT NULL AND
        evt_inv.main_inv_bool = 1
        AND
        inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
        inv_inv.inv_no_id    = evt_inv.inv_no_id

      UNION

      /** Also include all actual tasks that are not recorded for a task_defn in dwt_task_labour_summary
          during past X months since SYS date */
      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id,
         sched_stask.barcode_sdesc,
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_dt,
         evt_inv.assmbl_inv_no_db_id,
         evt_inv.assmbl_inv_no_id,
         inv_inv.inv_no_sdesc
      FROM
         task_task,
         sched_stask,
         evt_event,
         evt_inv,
         inv_inv
      WHERE
        task_task.task_db_id = cn_TaskDbId AND
        task_task.task_id    = cn_TaskId
        AND
        task_task.assmbl_db_id IS NOT NULL
        AND
        sched_stask.task_db_id = task_task.task_db_id AND
        sched_stask.task_id    = task_task.task_id    AND
        sched_stask.rstat_cd  = 0
        AND
        evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id AND
        evt_event.event_status_cd = 'COMPLETE' AND
        evt_event.event_dt > add_months(SYSDATE, -an_Months)
        AND
        evt_inv.event_db_id = evt_event.event_db_id AND
        evt_inv.event_id    = evt_event.event_id AND
        evt_inv.assmbl_inv_no_db_id IS NOT NULL AND
        evt_inv.main_inv_bool = 1
        AND
        inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
        inv_inv.inv_no_id    = evt_inv.inv_no_id
        AND
        NOT EXISTS (
        /** only need actual tasks that have not been recorded */
            SELECT
               1
            FROM
             dwt_task_labour_summary
            WHERE
             dwt_task_labour_summary.task_defn_db_id = cn_TaskDbId AND
             dwt_task_labour_summary.task_defn_id    = cn_TaskId
             AND
             dwt_task_labour_summary.sched_db_id = sched_stask.sched_db_id AND
             dwt_task_labour_summary.sched_id    = sched_stask.sched_id
        );

   lrec_GetCompletedTaskLabours lcur_GetCompletedTaskLabours%ROWTYPE;

   /* create a fully outer join between task_labour_list and sched_labour as
      planned skills v.s. acual completed skills
    */
   CURSOR lcur_GetTaskDefnEvtLabours (
          cd_SchedDbId   sched_stask.sched_db_id %TYPE,
          cn_SchedId     sched_stask.sched_id %TYPE
      ) IS
      /* get scheduled labour skills and actual labour skills */
        SELECT DISTINCT
        task_defn_labour_sched.task_db_id,
        task_defn_labour_sched.task_id,
        task_defn_labour_sched.labour_skill_db_id AS schedlbr_skill_db_id,
        task_defn_labour_sched.labour_skill_cd AS schedlbr_skill_cd,
        sched_labour.sched_db_id AS actual_task_db_id,
        sched_labour.sched_id AS actual_task_id,
        sched_labour.labour_skill_db_id AS actuallbr_db_id,
        sched_labour.labour_skill_cd AS actuallbr_cd
      FROM
         ( /** create a join of task_labour_list and sched_stask */
           SELECT
           task_labour_list.task_db_id,
           task_labour_list.task_id,
           task_labour_list.labour_skill_db_id,
           task_labour_list.labour_skill_cd,
           sched_stask.sched_db_id,
           sched_stask.sched_id
           FROM
              task_labour_list,
              sched_stask
           WHERE
              sched_stask.sched_db_id     = cd_SchedDbId AND
              sched_stask.sched_id        = cn_SchedId AND
              sched_stask.rstat_cd  = 0
              AND
              task_labour_list.task_db_id = sched_stask.task_db_id AND
              task_labour_list.task_id    = sched_stask.task_id
         )task_defn_labour_sched,
         sched_labour
      where
          sched_labour.sched_db_id (+)= task_defn_labour_sched.sched_db_id AND
          sched_labour.sched_id    (+)= task_defn_labour_sched.sched_id AND
          sched_labour.labour_skill_db_id (+)= task_defn_labour_sched.labour_skill_db_id AND
          sched_labour.labour_skill_cd    (+)= task_defn_labour_sched.labour_skill_cd AND
          sched_labour.labour_stage_cd (+)= 'COMPLETE'

      UNION

      SELECT DISTINCT
        task_defn_labour_sched.task_db_id,
        task_defn_labour_sched.task_id,
        task_defn_labour_sched.labour_skill_db_id AS schedlbr_skill_db_id,
        task_defn_labour_sched.labour_skill_cd AS schedlbr_skill_cd,
        sched_labour.sched_db_id AS actual_task_db_id,
        sched_labour.sched_id AS actual_task_id,
        sched_labour.labour_skill_db_id AS actuallbr_db_id,
        sched_labour.labour_skill_cd AS actuallbr_cd
      FROM
         ( /** create a join of task_labour_list and sched_stask */
           SELECT
           task_labour_list.task_db_id,
           task_labour_list.task_id,
           task_labour_list.labour_skill_db_id,
           task_labour_list.labour_skill_cd,
           sched_stask.sched_db_id,
           sched_stask.sched_id
           FROM
              task_labour_list,
              sched_stask
           WHERE
              sched_stask.sched_db_id     = cd_SchedDbId AND
              sched_stask.sched_id        = cn_SchedId AND
              task_labour_list.task_db_id = sched_stask.task_db_id AND
              task_labour_list.task_id    = sched_stask.task_id
         )task_defn_labour_sched,
         sched_labour
      WHERE
           sched_labour.sched_db_id = cd_SchedDbId AND
           sched_labour.sched_id    = cn_SchedId AND
           sched_labour.labour_stage_cd = 'COMPLETE' AND
           task_defn_labour_sched.sched_db_id        (+)= sched_labour.sched_db_id AND
           task_defn_labour_sched.sched_id           (+)= sched_labour.sched_id AND
           task_defn_labour_sched.labour_skill_db_id (+)= sched_labour.labour_skill_db_id AND
           task_defn_labour_sched.labour_skill_cd    (+)= sched_labour.labour_skill_cd;

   lrec_GetTaskDefnEvtLabours lcur_GetTaskDefnEvtLabours%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /** Get each work scope ACTV, SUPRSEDE or OBSOLETE task definition */
   FOR lrec_FindWorkScopeTaskDefn IN lcur_FindWorkScopeTaskDefn ()
   LOOP

      /** Get the most recent update */
      SELECT
         MAX(revision_dt)
      INTO
         ld_RecentUpdate
      FROM
         dwt_task_labour_summary
      WHERE
         dwt_task_labour_summary.task_db_id = lrec_FindWorkScopeTaskDefn.task_db_id AND
         dwt_task_labour_summary.task_id   = lrec_FindWorkScopeTaskDefn.task_id;

      /** If most recent update is null, get the the date past X months from now */
      IF ld_RecentUpdate IS NULL THEN
        ld_RecentUpdate:=add_months(SYSDATE, -an_Months);
      END IF;

      /** get all completed actual tasks after most recent update or
          the date past X months from now */
        FOR lrec_GetCompletedTaskLabours IN lcur_GetCompletedTaskLabours(
            ld_RecentUpdate,
            lrec_FindWorkScopeTaskDefn.task_db_id,
            lrec_FindWorkScopeTaskDefn.task_id)
        LOOP

            FOR lrec_GetTaskDefnEvtLabours IN lcur_GetTaskDefnEvtLabours(
              lrec_GetCompletedTaskLabours.event_db_id,
              lrec_GetCompletedTaskLabours.event_id)
            LOOP

                /** initalize the varlable to negative for later checking if it is updated */
                ln_ActualManPwrCt := -1;
                /** skill is not planned */
                IF lrec_GetTaskDefnEvtLabours.task_db_id IS NULL THEN
                   ln_SchedManPwCt := 0;
                   ln_SchedManHr := 0;
                ELSE
                    /** Get the scheduled man hours and scheduled numbers of men  for a skill*/
                    SELECT
                      task_labour_list.labour_skill_db_id,
                      task_labour_list.labour_skill_cd,
                      task_labour_list.man_pwr_ct,
                      (task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr) AS man_hr
                    INTO
                      ln_LabourSkillDbId,
                      ls_LabourSkillCd,
                      ln_SchedManPwCt,
                      ln_SchedManHr
                    FROM
                      task_labour_list
                    WHERE
                      task_labour_list.task_db_id         = lrec_FindWorkScopeTaskDefn.task_db_id AND
                      task_labour_list.task_id            = lrec_FindWorkScopeTaskDefn.task_id AND
                      task_labour_list.labour_skill_db_id = lrec_GetTaskDefnEvtLabours.schedlbr_skill_db_id AND
                      task_labour_list.labour_skill_cd    = lrec_GetTaskDefnEvtLabours.schedlbr_skill_cd;

                    /** planned skill is not actually used */
                    IF lrec_GetTaskDefnEvtLabours.actual_task_db_id IS NULL THEN
                       ln_ActualManPwrCt := 0;
                       ln_ActualTotalManHr := 0;
                    END IF;
                END IF;

                /** so far ln_ActualManPwrCt is still -1, which means planed skill is actually used
                then acquire the actual number of workers and hours of a skill  */
                IF ln_ActualManPwrCt = -1 THEN
                    SELECT
                       sched_labour.labour_skill_db_id,
                       sched_labour.labour_skill_cd,
                       COUNT(*),
                       NVL(SUM(tech_role.actual_hr),0)
                       + NVL(SUM(cert_role.actual_hr),0)
                       + NVL(SUM(insp_role.actual_hr),0)
                    INTO
                       ln_LabourSkillDbId,
                       ls_LabourSkillCd,
                       ln_ActualManPwrCt,
                       ln_ActualTotalManHr
                    FROM
                       sched_labour
                       INNER JOIN sched_labour_role tech_role ON
                          tech_role.labour_db_id = sched_labour.labour_db_id AND
                          tech_role.labour_id    = sched_labour.labour_id
                          AND
                          tech_role.labour_role_type_cd = 'TECH'
                       LEFT OUTER JOIN sched_labour_role cert_role ON
                          cert_role.labour_db_id = sched_labour.labour_db_id AND
                          cert_role.labour_id    = sched_labour.labour_id
                          AND
                          cert_role.labour_role_type_cd = 'CERT'
                       LEFT OUTER JOIN sched_labour_role insp_role ON
                          insp_role.labour_db_id = sched_labour.labour_db_id AND
                          insp_role.labour_id    = sched_labour.labour_id
                          AND
                          insp_role.labour_role_type_cd = 'INSP'
                    WHERE
                        sched_labour.sched_db_id = lrec_GetTaskDefnEvtLabours.actual_task_db_id AND
                        sched_labour.sched_id    = lrec_GetTaskDefnEvtLabours.actual_task_id AND
                        sched_labour.labour_skill_db_id = lrec_GetTaskDefnEvtLabours.actuallbr_db_id AND
                        sched_labour.labour_skill_cd    = lrec_GetTaskDefnEvtLabours.actuallbr_cd
                    GROUP BY labour_skill_db_id, labour_skill_cd;
                END IF;

               /** initalize this local variable for later determing if we should insert or update
                  strange behavior may happen when not initilized to null*/
               ln_TaskDnId :=NULL;

                /** Check if there exists a row with this pk in dwt_task_labour_summary */
                SELECT
                   count(*)
                INTO
                   ln_TaskDnId
                FROM
                   dwt_task_labour_summary
                WHERE
                   dwt_task_labour_summary.task_defn_db_id    = lrec_FindWorkScopeTaskDefn.task_defn_db_id AND
                   dwt_task_labour_summary.task_defn_id       = lrec_FindWorkScopeTaskDefn.task_defn_id AND
                   dwt_task_labour_summary.sched_db_id        = lrec_GetCompletedTaskLabours.sched_db_id AND
                   dwt_task_labour_summary.sched_id           = lrec_GetCompletedTaskLabours.sched_id AND
                   dwt_task_labour_summary.labour_skill_db_id = ln_LabourSkillDbId AND
                   dwt_task_labour_summary.labour_skill_cd    = ls_LabourSkillCd AND
                   dwt_task_labour_summary.task_db_id         = lrec_FindWorkScopeTaskDefn.task_db_id AND
                   dwt_task_labour_summary.task_id            = lrec_FindWorkScopeTaskDefn.task_id;

                /** no such row in dwt_task_labour_summary */
                IF ln_TaskDnId =0 THEN

                     /* insert a record into the status history */
                     INSERT INTO dwt_task_labour_summary (
                           task_defn_db_id,
                           task_defn_id,
                           sched_db_id,
                           sched_id,
                           labour_skill_db_id,
                           labour_skill_cd,
                           task_db_id,
                           task_id,
                           revision_ord,
                           barcode_sdesc,
                           complete_dt,
                           assmbl_db_id,
                           assmbl_cd,
                           assmbl_inv_no_db_id,
                           assmbl_inv_no_id,
                           assmbl_inv_no_sdesc,
                           sched_man_pwr_ct,
                           sched_man_hr,
                           actual_man_pwr_ct,
                           actual_total_man_hr)
                        VALUES (
                           lrec_FindWorkScopeTaskDefn.task_defn_db_id,
                           lrec_FindWorkScopeTaskDefn.task_defn_id,
                           lrec_GetCompletedTaskLabours.sched_db_id,
                           lrec_GetCompletedTaskLabours.sched_id,
                           ln_LabourSkillDbId,
                           ls_LabourSkillCd,
                           lrec_FindWorkScopeTaskDefn.task_db_id,
                           lrec_FindWorkScopeTaskDefn.task_id,
                           lrec_FindWorkScopeTaskDefn.revision_ord,
                           lrec_GetCompletedTaskLabours.barcode_sdesc,
                           lrec_GetCompletedTaskLabours.event_dt,
                           lrec_FindWorkScopeTaskDefn.assmbl_db_id,
                           lrec_FindWorkScopeTaskDefn.assmbl_cd,
                           lrec_GetCompletedTaskLabours.assmbl_inv_no_db_id,
                           lrec_GetCompletedTaskLabours.assmbl_inv_no_id,
                           lrec_GetCompletedTaskLabours.inv_no_sdesc,
                           ln_SchedManPwCt,
                           ln_SchedManHr,
                           ln_ActualManPwrCt,
                           ln_ActualTotalManHr );

                ELSE

                   /** UPDATE */
                    UPDATE dwt_task_labour_summary
                       SET
                           revision_ord    = lrec_FindWorkScopeTaskDefn.revision_ord,
                           barcode_sdesc   = lrec_GetCompletedTaskLabours.barcode_sdesc,
                           complete_dt     = lrec_GetCompletedTaskLabours.event_dt,
                           assmbl_db_id    = lrec_FindWorkScopeTaskDefn.assmbl_db_id,
                           assmbl_cd       = lrec_FindWorkScopeTaskDefn.assmbl_cd,
                           assmbl_inv_no_db_id = lrec_GetCompletedTaskLabours.assmbl_inv_no_db_id,
                           assmbl_inv_no_id    = lrec_GetCompletedTaskLabours.assmbl_inv_no_id,
                           assmbl_inv_no_sdesc = lrec_GetCompletedTaskLabours.inv_no_sdesc,
                           sched_man_pwr_ct    = ln_SchedManPwCt,
                           sched_man_hr        = ln_SchedManHr,
                           actual_man_pwr_ct   = ln_ActualManPwrCt,
                           actual_total_man_hr = ln_ActualTotalManHr
                     WHERE
                       dwt_task_labour_summary.task_defn_db_id     = lrec_FindWorkScopeTaskDefn.task_defn_db_id AND
                       dwt_task_labour_summary.task_defn_id        = lrec_FindWorkScopeTaskDefn.task_defn_id AND
                       dwt_task_labour_summary.sched_db_id         = lrec_GetCompletedTaskLabours.sched_db_id AND
                       dwt_task_labour_summary.sched_id            = lrec_GetCompletedTaskLabours.sched_id AND
                       dwt_task_labour_summary.labour_skill_db_id  = ln_LabourSkillDbId AND
                       dwt_task_labour_summary.labour_skill_cd     = ls_LabourSkillCd AND
                       dwt_task_labour_summary.task_db_id          = lrec_FindWorkScopeTaskDefn.task_db_id AND
                       dwt_task_labour_summary.task_id             = lrec_FindWorkScopeTaskDefn.task_id ;
                END IF;

           END LOOP;
        END LOOP;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateTaskLabourSummary@@@'||SQLERRM);
     RETURN;
END UpdateTaskLabourSummary;

/********************************************************************************
*
* Procedure:    UpdatePartsAndToolsReadyBool
* Arguments:    an_TaskDbId    (long) - task database id
*               an_TaskId      (long) - task id
* Return:       ab_PartReady   (integer) - indicates part ready
*               ab_ToolReady   (integer) - indicates tool ready
*               on_Return      (long) - succss/failure of procedure
*
* Description: Updates the part and tool ready booleans on a task and its subtasks
*
* Orig.Coder:  L. Soh
* Recent Coder:
* Recent Date:  February 15, 2008
*
*********************************************************************************
*
* Copyright ? 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdatePartsAndToolsReadyBool(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           ab_PartReady          OUT INTEGER,
           ab_ToolReady          OUT INTEGER,
           on_Return             OUT typn_RetCode
   ) IS

   -- local variables
   lb_ChildPartReady   sched_stask.parts_ready_bool%TYPE;
   lb_ChildToolReady   sched_stask.tools_ready_bool%TYPE;
   lb_TempPartReady   sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady   sched_stask.tools_ready_bool%TYPE;
   ln_TempHasChildTasks NUMBER;

   -- Cursor: return all child tasks of given task
   CURSOR lcur_GetChildTasks (
         cn_SchedDbId sched_stask.task_db_id%TYPE,
         cn_SchedId   sched_stask.task_id%TYPE
      ) IS

      SELECT
         event_db_id, event_id
      FROM
         evt_event
      WHERE rstat_cd = 0
      START WITH
            evt_event.nh_event_db_id = cn_SchedDbId AND
            evt_event.nh_event_id    = cn_SchedId
      CONNECT BY
              evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
              evt_event.nh_event_id    = PRIOR evt_Event.event_id
      ORDER BY  
              event_db_id, event_id;

      lrec_GetChildTasks  lcur_GetChildTasks%rowtype;

   -- Cursor: return any install part requirement
   CURSOR lcur_SchedInstPart (
         cn_SchedDbId   sched_inst_part.sched_db_id%TYPE,
         cn_SchedId     sched_inst_part.sched_id%TYPE
      ) IS
      SELECT
         sched_inst_part.sched_db_id,
         sched_inst_part.sched_id
      FROM
         sched_inst_part
      WHERE
         sched_inst_part.sched_db_id   = cn_SchedDbId AND
         sched_inst_part.sched_id      = cn_SchedId;

         lrec_SchedInstPart lcur_SchedInstPart%ROWTYPE;

   -- Cursor: return any tool part requirement
   CURSOR lcur_EvtTool (
         cn_EventDbId   evt_tool.event_db_id%TYPE,
         cn_EventId     evt_tool.event_id%TYPE
      ) IS
      SELECT
         evt_tool.event_db_id,
         evt_tool.event_id
      FROM
         evt_tool
      WHERE
         evt_tool.event_db_id = cn_EventDbId AND
         evt_tool.event_id    = cn_EventId;

         lrec_EvtTool lcur_EvtTool%ROWTYPE;

BEGIN
    -- Initialize the return variable.
    on_Return := icn_NoProc;

    -- initialize local variables
    lb_ChildPartReady := 1;
    lb_ChildToolReady := 1;

    lb_TempPartReady := 1;
    lb_TempToolReady := 1;

    ln_TempHasChildTasks := 0;

    -- If task has sub tasks, we evaluate each sub tasks
    FOR lrec_GetChildTasks IN lcur_GetChildTasks( an_TaskDbId, an_TaskId )
    LOOP

        -- Set flag that we have child tasks
        ln_TempHasChildTasks := 1;

        -- Evaluate part/tool readiness for this sub task
        UpdatePartsAndToolsReadyBool(lrec_GetChildTasks.event_db_id,
                                    lrec_GetChildTasks.event_id,
                                    lb_TempPartReady,
                                    lb_TempToolReady,
                                    on_Return);

         -- Check if procedure is successful
         IF on_Return < 1 THEN
            RETURN;
         END IF;

        -- As long as one sub task has a part not ready, set flag
        IF lb_TempPartReady = 0 THEN
           lb_ChildPartReady := 0;
        END IF;

        -- As long as one sub task has a tool not ready, set flag
        IF lb_TempToolReady = 0 THEN
           lb_ChildToolReady := 0;
        END IF;

    END LOOP;

    -- Get install part requirement for this task
    OPEN  lcur_SchedInstPart( an_TaskDbId, an_TaskId );
    FETCH lcur_SchedInstPart INTO lrec_SchedInstPart;
    CLOSE lcur_SchedInstPart;

    -- Get tool requirement for this task
    OPEN  lcur_EvtTool( an_TaskDbId, an_TaskId );
    FETCH lcur_EvtTool INTO lrec_EvtTool;
    CLOSE lcur_EvtTool;

    -- If we have child tasks, have to take child task readiness into consideration
    IF ln_TempHasChildTasks = 1 THEN

       -- Set part ready to 1 if sub tasks are all part ready and
       -- current task has no install part requirement
       IF lb_ChildPartReady = 1 AND lrec_SchedInstPart.sched_db_id IS NULL THEN
          ab_PartReady := 1;
       ELSE
           ab_PartReady := 0;
       END IF;

       -- Set tool ready to 1 if sub tasks are all tool ready and
       -- current task has no tool requirement
       IF lb_ChildToolReady = 1 AND lrec_EvtTool.event_db_id IS NULL THEN
          ab_ToolReady := 1;
       ELSE
          ab_ToolReady := 0;
       END IF;

    -- If task has no child task
    ELSE

        -- if there is at least an install part requirement, part_ready is false
        IF lrec_SchedInstPart.sched_db_id IS NULL THEN
            ab_PartReady := 1;
        ELSE
            ab_PartReady := 0;
        END IF;

        -- if there is at least a tool part requirement, tool_ready is false
        IF lrec_EvtTool.event_db_id IS NULL THEN
            ab_ToolReady := 1;
        ELSE
            ab_ToolReady := 0;
        END IF;

    END IF;

    -- Update the task
    UPDATE sched_stask
    SET
        sched_stask.parts_ready_bool = ab_PartReady,
        sched_stask.tools_ready_bool = ab_ToolReady
    WHERE
          sched_stask.sched_db_id = an_TaskDbId AND
          sched_stask.sched_id    = an_TaskId   AND
          sched_stask.rstat_cd   = 0;

    -- Return success
    on_Return := icn_Success;

EXCEPTION
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        on_Return := icn_Error;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdatePartsAndToolsReadyBool@@@'||SQLERRM);
        RETURN;
END UpdatePartsAndToolsReadyBool;

END SCHED_STASK_PKG;
/