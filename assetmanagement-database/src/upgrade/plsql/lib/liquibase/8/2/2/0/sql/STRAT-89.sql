--liquibase formatted sql


--changeSet STRAT-89:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Updated PREP_DEADLINE_PKG.GetRescheduleFromDtValues such that when it is comparing the ln_PostfixedQt and ln_Deviation to determine the start date, it treats them as having a value of zero if they are set to NULL.
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
   ScheduleFromCd       VARCHAR2(16),
   ReschedFromCd        VARCHAR2(8),
   SchedFromLatestBool  NUMBER,
   RecurringBool        NUMBER,
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTwoLastRevisions@@@'||SQLERRM);
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
            an_TaskDbId            IN task_task.task_db_id%TYPE,
            an_TaskId              IN task_task.task_id%TYPE,
            ab_RefreshMe           IN BOOLEAN,
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
   IF ab_RefreshMe THEN
        /* If this task satisfies the ME sched rules condition then use the baseline values*/
        GetMESchedRuleDeadline(
                               an_SchedDbId,
                               an_SchedId,
                               an_TaskDbId,
                               an_TaskId,
                               an_DataTypeDbId,
                               an_DataTypeId,
                               ln_IntervalQt_me,
                               ln_NotifyQt_me,
                               ln_DeviationQt_me,
                               ln_PrefixedQt_me,
                               ln_PostfixedQt_me,
                               ln_Return_me
        );

        IF ln_Return_me > 0
           AND  -- only if the measurement deadline has changed due to JIC measurement changing. We do not want to reset the deviation
                -- unless we have to
           NOT ( an_IntervalQt  = ln_IntervalQt_me AND
                 an_NotifyQt    = ln_NotifyQt_me   AND
                 an_PrefixedQt  = ln_PrefixedQt_me AND
                 an_PostfixedQt = ln_PostfixedQt_me    ) THEN

           an_IntervalQt   := ln_IntervalQt_me;
           an_NotifyQt     := ln_NotifyQt_me;
           an_DeviationQt  := ln_DeviationQt_me;
           an_PrefixedQt   := ln_PrefixedQt_me;
           an_PostfixedQt  := ln_PostfixedQt_me;
        END IF;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetActualDeadline@@@'||SQLERRM);
      RETURN;

END GetActualDeadline;


/********************************************************************************
*
* Procedure:    UpdateOverdueTaskByNowForInv
* Arguments:
*           an_MainInvNoDbId    (long) - The primary key of main inventory
*           an_MainInvNoId      (long) --//--
* Return:
*           on_Return       (long) - Success/Failure
*
* Description:  This procedure is used to update deadline status for all
*               overdue tasks with calendar based deadlines with to date
*               for given inventory.
*
* Orig.Coder:     Yuriy Vakulenko
* Recent Date:    October 2016
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateOverdueTaskByNowForInv (
      an_MainInvNoDbId            IN  sched_stask.main_inv_no_db_id%TYPE,
      an_MainInvNoId              IN  sched_stask.main_inv_no_id%TYPE,
      on_Return               OUT typn_RetCode
   ) IS

      -- recursivelly fetch all inventories starting from the given root
      CURSOR lcur_InventoryTree IS
         SELECT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id
         FROM
            inv_inv
         START WITH
            inv_inv.inv_no_db_id = an_MainInvNoDbId AND
            inv_inv.inv_no_id = an_MainInvNoId
         CONNECT BY
            inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
            inv_inv.nh_inv_no_id = PRIOR inv_inv.inv_no_id;

      -- retrieves deadlines
      CURSOR lcur_OverdueTasks(
            an_InvNoDbId            IN  sched_stask.main_inv_no_db_id%TYPE,
            an_InvNoId              IN  sched_stask.main_inv_no_id%TYPE
      )  IS
         SELECT
            sched_stask.sched_db_id,
            sched_stask.sched_id
         FROM
            evt_sched_dead
            JOIN evt_event ON evt_event.event_db_id             = evt_sched_dead.event_db_id AND
                              evt_event.event_id                = evt_sched_dead.event_id
            JOIN sched_stask ON sched_stask.sched_db_id         = evt_event.event_db_id AND
                             sched_stask.sched_id               = evt_event.event_id
            JOIN evt_inv ON evt_inv.event_db_id                 = evt_event.event_db_id AND
                            evt_inv.event_id                    = evt_event.event_id
            JOIN inv_inv ON inv_inv.inv_no_db_id                = evt_inv.inv_no_db_id AND
                            inv_inv.inv_no_id                   = evt_inv.inv_no_id
            JOIN mim_data_type ON mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                                  mim_data_type.data_type_id    = evt_sched_dead.data_type_id
            JOIN ref_eng_unit ON ref_eng_unit.eng_unit_db_id    = mim_data_type.eng_unit_db_id AND
                                 ref_eng_unit.eng_unit_cd       = mim_data_type.eng_unit_cd
         WHERE
            -- Only check tasks that are not historical
            evt_sched_dead.hist_bool_ro = 0
            AND
            -- Only check tasks with hard deadlines
            sched_stask.soft_deadline_bool = 0
            AND
            -- Only check tasks that are not currently overdue
            evt_event.sched_priority_cd <> 'O/D'
            AND
            -- Only check tasks where main inventory for the task is still active
            evt_inv.main_inv_bool = 1
            AND
            inv_inv.locked_bool = 0
            AND
            -- Only check the driving deadline
            evt_sched_dead.sched_driver_bool = 1
            AND
            -- Only perform this operation for calendar-based deadlines, as usage based deadlines will be recalculated after each change in usage
            mim_data_type.domain_type_db_id = 0 AND
            mim_data_type.domain_type_cd = 'CA'
            AND
            -- Narrow down tasks defined against given inventory
            sched_stask.main_inv_no_db_id = an_InvNoDbId AND
            sched_stask.main_inv_no_id    = an_InvNoId
            AND
            -- Finally, compare the extended deadline against the current sysdate
            getExtendedDeadlineDt(
               evt_sched_dead.deviation_qt,
               evt_sched_dead.sched_dead_dt,
               mim_data_type.domain_type_cd,
               mim_data_type.data_type_id,
               ref_eng_unit.ref_mult_qt) < SYSDATE;

BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;
   /* loop through the inventory tree starting from given inventory as the root */
   FOR lrec_InventoryTree IN lcur_InventoryTree
   LOOP
      /* loop through all overdue tasks */
      FOR lrec_OverdueTasks IN lcur_OverdueTasks(lrec_InventoryTree.inv_no_db_id, lrec_InventoryTree.inv_no_id)
      LOOP
         /* Update the Deadlines for each task */
         EVENT_PKG.UpdateDeadline( lrec_OverdueTasks.sched_db_id, lrec_OverdueTasks.sched_id, on_Return );
      END LOOP;
   END LOOP;

   IF on_Return < 1 THEN
      RETURN;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareSchedDeadlines@@@: '||SQLERRM);
      RETURN;
END UpdateOverdueTaskByNowForInv;


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
                          an_TaskDbId,
                          an_TaskId,
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetBaselineDeadline@@@'||SQLERRM);
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
                 task_task.sched_from_latest_bool,
                 task_task.last_sched_dead_bool,
                 task_task.effective_gdt,
                 task_task.task_sched_from_cd,
                 task_task.resched_from_cd,
                 task_task.manual_scheduling_bool
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
            (lrec_OrigTask.recurring_task_bool           != lrec_LatestTask.recurring_task_bool)          OR
            (lrec_OrigTask.manual_scheduling_bool        != lrec_LatestTask.manual_scheduling_bool)       OR
            (lrec_OrigTask.sched_from_latest_bool        != lrec_LatestTask.sched_from_latest_bool)
            OR
            NOT(lrec_OrigTask.effective_gdt          = lrec_LatestTask.effective_gdt)                     OR
            (lrec_OrigTask.effective_gdt IS NULL     AND lrec_LatestTask.effective_gdt IS NOT NULL)       OR
            (lrec_OrigTask.effective_gdt IS NOT NULL AND lrec_LatestTask.effective_gdt IS NULL)
            OR
            NOT(lrec_OrigTask.task_sched_from_cd          = lrec_LatestTask.task_sched_from_cd )                OR
            (lrec_OrigTask.task_sched_from_cd IS NULL     AND lrec_LatestTask.task_sched_from_cd IS NOT NULL)   OR
            (lrec_OrigTask.task_sched_from_cd IS NOT NULL AND lrec_LatestTask.task_sched_from_cd IS NULL)
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@BaselineDeadlinesChanged@@@'||SQLERRM);
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

      ld_BaselineEffectiveDt     task_task.effective_dt%TYPE;

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
          (
             -- for config slot based task definition
             (
                task_task.assmbl_db_id  = prev_task.assmbl_db_id AND
                task_task.assmbl_cd     = prev_task.assmbl_cd    AND
                task_task.assmbl_bom_id = prev_task.assmbl_bom_id
             )
             -- for part number based task definition
             OR
             (
                task_task.assmbl_db_id IS NULL AND
                EXISTS
                (
                   SELECT 1
                   FROM
                      task_part_map,
                      task_part_map prev_task_part_map
                   WHERE
                      task_part_map.task_db_id = task_task.task_db_id AND
                      task_part_map.task_id    = task_task.task_id
                      AND
                      prev_task_part_map.task_db_id = prev_task.task_db_id AND
                      prev_task_part_map.task_id    = prev_task.task_id
                      AND
                      task_part_map.part_no_db_id = prev_task_part_map.part_no_db_id AND
                      task_part_map.part_no_id    = prev_task_part_map.part_no_id
                )
             )
          )
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
            an_SchedDbId,
            an_SchedId,
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

      /* get existing deadline , do not refresh measurement rules */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            an_TaskDbId,
            an_TaskId,
            FALSE,
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

   -- Check if the baseline deadline has changed between revisions.
   IF ln_BaselineDeadlineChanged = 1 THEN
      -- The baseline deadline has changed between revisions, so update the actual.
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
   ELSE
      -- The baseline deadline is the same between revisions.

      -- Get the baseline effective date.
      SELECT
         effective_dt
      INTO
         ld_BaselineEffectiveDt
      FROM
         task_task
      WHERE
         task_db_id = an_TaskDbId AND
         task_id    = an_TaskId;

      -- Determine if there is a difference between the task deadline and the baseline.
      IF ( an_BaselineIntervalQt <> ln_ActualIntervalQt ) OR
         ( an_BaselineNotifyQt <> ln_ActualNotifyQt ) OR
         ( ( ls_ActualSchedFromCd = 'EFFECTIV' ) AND (ld_BaselineEffectiveDt <> ld_ActualStartDt) ) THEN

         -- The baseline deadline differs from the actual deadline, so update the actual.
         an_UpdateActualDeadline := 1;
         as_sched_from_cd        := ls_ActualSchedFromCd;
         ad_start_dt             := ld_ActualStartDt;
         an_start_qt             := ln_ActualStartQt;
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
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@DeleteDeadline@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetUsageParmInfo@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@InsertDeadlineRow@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDeadlineRow@@@'||SQLERRM);
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
            evt_event.rstat_cd   = 0;
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCorrectiveFaultInfo@@@'||SQLERRM);
      IF lcur_custom_deadline%ISOPEN THEN CLOSE lcur_custom_deadline; END IF;
      RETURN;

END GetCorrectiveFaultInfo;


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

      /* use the function that adds the interval to the deadline date */
      ad_NewDeadlineDt := getDeadlineDt(an_Interval, ad_StartDt, ls_DataTypeCd, ll_RefMultQt);

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
          ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval*12 ) - TRUNC((an_Interval - TRUNC(an_Interval * 12)/12) * ll_RefMultQt);

      /* add the correct # of days to the start date */
      ELSE
         ad_StartDt := ad_NewDeadlineDt  - (an_Interval * ll_RefMultQt);
      END IF;


   /* -- Calculate Interval -- */
   ELSIF (an_Interval is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt := LAST_DAY(ad_StartDt);
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt ) / 12;

      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         an_Interval :=  (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt;

      /* add the correct # of days to the start date */
      ELSE
         an_Interval :=  (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt;
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindUsageDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindUsageDeadlineVariables;

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
* Description: This procedure returns due and completion dates.
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetOldEvtInvUsage@@@'||SQLERRM);
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
* Description: This procedure returns manufacturing and received dates of the inventory.
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
            AND inv_inv.rstat_cd     (+) = 0
            AND evt_inv.main_inv_bool    = 1
            AND evt_inv.event_db_id      = an_SchedDbId
            AND evt_inv.event_id         = an_SchedId;

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetMainInventoryBirthInfo@@@'||SQLERRM);
      RETURN;

END GetMainInventoryBirthInfo;

/********************************************************************************
*
* Procedure: GetMPActivationDate
* Arguments:
*             an_TaskDbId (long) - task definition primary key
*             an_TaskId   (long) --//--
*             an_InvNoDbId (long) inventory primary key
*             an_InvNoId   (long) --//--
*
* Return:
*             ad_MPActvDt (date) --  maintenance program activation date
*             on_Return       - 1 is success
*
* Description: This procedure returns the maintenance program activation date of maintenance program to which this task definition was first assigned.
*
*
* Orig.Coder:     sdevi
* Recent Coder:
* Recent Date:    August 12, 2013
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMPActivationDate(
            an_TaskDbId  IN task_task.task_db_id%TYPE,
            an_TaskId    IN task_task.task_id%TYPE,
            an_InvNoDbId IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId   IN  inv_inv.inv_no_id%TYPE,
            ad_MPActvDt  OUT maint_prgm.actv_dt%TYPE,
            on_Return    OUT typn_RetCode
   ) IS

     ln_CarrierDbId   inv_inv.carrier_db_id%TYPE;
     ln_CarrierId     inv_inv.carrier_id%TYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;
          -- get the inventory carrier
          SELECT
             highest_inv.carrier_db_id,
             highest_inv.carrier_id
          INTO
             ln_CarrierDbId,
             ln_CarrierId
          FROM
             inv_inv
             INNER JOIN inv_inv highest_inv ON
                highest_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
                highest_inv.inv_no_id    = inv_inv.h_inv_no_id
          WHERE
             inv_inv.inv_no_db_id = an_InvNoDbId AND
             inv_inv.inv_no_id    = an_InvNoId;

          -- get MP activation date based on task defn and carrier
          SELECT DISTINCT
             FIRST_VALUE(actv_dt) OVER (
                PARTITION BY maint_prgm_defn_db_id, maint_prgm_defn_id, carrier_db_id, carrier_id
                ORDER BY revision_ord ASC
             ) AS actv_dt
          INTO
             ad_MPActvDt
          FROM
             (
             SELECT
                maint_prgm.maint_prgm_defn_db_id,
                maint_prgm.maint_prgm_defn_id,
                maint_prgm_carrier_map.carrier_db_id,
                maint_prgm_carrier_map.carrier_id,
                maint_prgm.revision_ord,
                maint_prgm.actv_dt
             FROM
                task_task
                INNER JOIN maint_prgm_task ON
                   maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
                   maint_prgm_task.task_defn_id    = task_task.task_defn_id
                INNER JOIN maint_prgm ON
                   maint_prgm.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
                   maint_prgm.maint_prgm_id    = maint_prgm_task.maint_prgm_id
                INNER JOIN maint_prgm_carrier_map ON
                   maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                   maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
             WHERE
                task_task.task_db_id = an_TaskDbId AND
                task_task.task_id    = an_TaskId
                AND
                maint_prgm_carrier_map.carrier_db_id = ln_CarrierDbId AND
                maint_prgm_carrier_map.carrier_id    = ln_CarrierId

             UNION ALL

             SELECT
                maint_prgm.maint_prgm_defn_db_id,
                maint_prgm.maint_prgm_defn_id,
                maint_prgm_carrier_map.carrier_db_id,
                maint_prgm_carrier_map.carrier_id,
                maint_prgm.revision_ord,
                maint_prgm.actv_dt
             FROM
                task_task
                INNER JOIN maint_prgm_carrier_temp_task ON
                   maint_prgm_carrier_temp_task.task_defn_db_id = task_task.task_defn_db_id AND
                   maint_prgm_carrier_temp_task.task_defn_id    = task_task.task_defn_id
                INNER JOIN maint_prgm ON
                   maint_prgm.maint_prgm_defn_db_id = maint_prgm_carrier_temp_task.maint_prgm_defn_db_id AND
                   maint_prgm.maint_prgm_defn_id    = maint_prgm_carrier_temp_task.maint_prgm_defn_id
                INNER JOIN maint_prgm_carrier_map ON
                   maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                   maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id AND
                   maint_prgm_carrier_map.carrier_db_id    = maint_prgm_carrier_temp_task.carrier_db_id AND
                   maint_prgm_carrier_map.carrier_id       = maint_prgm_carrier_temp_task.carrier_id AND
                   maint_prgm_carrier_map.latest_revision_bool = 1
             WHERE
                task_task.task_db_id = an_TaskDbId AND
                task_task.task_id    = an_TaskId
                AND
                maint_prgm_carrier_temp_task.carrier_db_id = ln_CarrierDbId AND
                maint_prgm_carrier_temp_task.carrier_id    = ln_CarrierId
             );

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN NO_DATA_FOUND THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00465','');
      RETURN;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetMPActivationDate@@@'||SQLERRM);
      RETURN;

END GetMPActivationDate;

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
* Description: This procedure returns 1 if tasks are on the same inventory 0 if they are not.
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@AreTasksOnTheSameInventory@@@'||SQLERRM);
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
* Description: This procedure returns current inventory usage TSN value.
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCurrentInventoryUsage@@@'||SQLERRM);
      RETURN;

END GetCurrentInventoryUsage;



/********************************************************************************
*
* Procedure: GetTasksUsageStartQt
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_DataTypeDbId   (long) data type primary key
             an_DataTypeId     (long) --//--
* Return:
*            an_StartQt        (float) - start quantity of usage parameter for task,
*                                        otherwise 0
*            on_Return         (long)  - 1 is success
*
* Description: This procedure returns start quantity value
*              for the provided task's usage parameter.
*              If no start quantity is found then 0 is returned.
*
**********************************************************************************
*
* Copyright 2000-2013 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetTasksUsageStartQt(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_StartQt      OUT evt_sched_dead.start_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
      evt_sched_dead.start_qt
   INTO
      an_StartQt
   FROM
      evt_sched_dead
   WHERE
      evt_sched_dead.event_db_id     = an_SchedDbId AND
      evt_sched_dead.event_id        = an_SchedId AND
      evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
      evt_sched_dead.data_type_id    = an_DataTypeId
   ;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN NO_DATA_FOUND THEN
      an_StartQt := 0;

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTasksUsageStartQt@@@'||SQLERRM);
      RETURN;

END GetTasksUsageStartQt;



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
*           ab_SchedFromLatest   (int) - 1 if this task is scheduled from latest
*           ad_EffectiveDt    (date) - effective date of the task definition
*           av_ScheduleFromCd (string) - schedule from option
*           an_RecurringBool  (long) - 1 if this task is recurring
*           an_OnConditionBool (long) - 1 if the task is on condition
*           as_TaskClassCd    (string) - the task's class code
*           on_Return       - (long) 1 is success
*
* Description: This procedure returns task details
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Yungjae Cho
* Recent Date:    2011-03-15
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
            an_HInvNoDbId     OUT task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       OUT task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     OUT task_interval.part_no_db_id%TYPE,
            an_PartNoId       OUT task_interval.part_no_id%TYPE,
            ab_SchedFromLatest OUT task_task.sched_from_latest_bool%TYPE,
            ad_EffectiveDt    OUT task_task.effective_dt%TYPE,
            av_ScheduleFromCd OUT task_task.task_sched_from_cd%TYPE,
            av_ReschedFromCd  OUT task_task.resched_from_cd%TYPE,
            an_RecurringBool  OUT task_task.recurring_task_bool%TYPE,
            an_OnConditionBool OUT task_task.on_condition_bool%TYPE,
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
         task_task.sched_from_latest_bool,
         task_task.effective_dt,
         task_task.task_sched_from_cd,
         task_task.resched_from_cd,
         istaskdefnrecurring(sched_stask.task_db_id, sched_stask.task_id) AS recurring_task_bool,
         task_task.on_condition_bool,
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
         ab_SchedFromLatest,
         ad_EffectiveDt,
         av_ScheduleFromCd,
         av_ReschedFromCd,
         an_RecurringBool,
         an_OnConditionBool,
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
         sched_stask.rstat_cd = 0
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTaskDetails@@@'||SQLERRM);
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
         INNER JOIN  sched_stask ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
      WHERE evt_event.rstat_cd = 0 AND
            ( (sched_stask.wo_db_id = cn_StartSchedDbId AND sched_stask.wo_id = cn_StartSchedId) OR
            (sched_stask.wo_db_id IS NULL AND sched_stask.wo_id IS NULL))
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId
      CONNECT BY
         nh_event_db_id = PRIOR event_db_id AND
         nh_event_id    = PRIOR event_id

      UNION

      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id
      FROM
         sched_stask
         INNER JOIN sched_stask check_stask ON
            check_stask.sched_db_id = sched_stask.wo_db_id AND
            check_stask.sched_id    = sched_stask.wo_id
         INNER JOIN  evt_event check_evt_event ON
            check_evt_event.event_db_id = check_stask.sched_db_id  AND
            check_evt_event.event_id    = check_stask.sched_id
         WHERE
            sched_stask.wo_db_id    = cn_StartSchedDbId    AND
            sched_stask.wo_id       = cn_StartSchedId      AND
            sched_stask.h_sched_id != cn_StartSchedId
            AND
            check_evt_event.hist_bool = 1
            AND
            check_stask.task_class_cd IN ('CHECK', 'RO');

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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlinesTree@@@: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlinesTree;


/********************************************************************************
*
* Procedure: GetRescheduleFromDtValues
* Arguments:an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*
* Return:
*           ad_StartDt     (date)    - current task's start date
*           av_SchedFromCd (varchar) - actual task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromDtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            an_ServiceCheck IN NUMBER,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_sched_dead.sched_dead_dt AS task_due_dt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           ref_eng_unit.ref_mult_qt,
           mim_data_type.data_type_cd,
           evt_event.event_dt AS task_end_dt,
           evt_event.hist_bool AS task_hist_bool,
           root_event.actual_start_gdt,
           root_event.sched_start_gdt,
           root_event.event_gdt,
           root_event.sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           root_sched_stask.task_class_cd,
           completed_wo.event_gdt as wo_end_gdt,
           completed_wo.sched_end_gdt as wo_sched_end_gdt,
           completed_wo.actual_start_dt as wo_start_gdt,
           completed_wo.sched_start_gdt as wo_sched_start_gdt,
           completed_wo.hist_bool as wo_hist_bool
        FROM
           evt_sched_dead,
           evt_event,
           evt_event root_event,
           sched_stask root_sched_stask,
           sched_stask,
           mim_data_type,
           ref_eng_unit,
           evt_event completed_wo
        WHERE
           evt_event.event_db_id =  cn_EventDbId AND
           evt_event.event_id    =  cn_EventId AND
           evt_event.rstat_cd    = 0
           AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
           mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
           AND
           ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
           ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id
           AND
           root_sched_stask.sched_db_id = root_event.event_db_id AND
           root_sched_stask.sched_id    = root_event.event_id
           AND
           sched_stask.sched_db_id = evt_event.event_db_id AND
           sched_stask.sched_id    = evt_event.event_id
           AND
           completed_wo.event_db_id (+)= sched_stask.wo_db_id AND
           completed_wo.event_id    (+)= sched_stask.wo_id;

   lrec_task lcur_task%ROWTYPE;

   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;

   ld_DueDate          evt_sched_dead.sched_dead_dt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ld_EndDate          evt_event.event_gdt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;
   ll_RefMultQt        ref_eng_unit.ref_mult_qt%TYPE;
   lv_DataTypeCd       mim_data_type.data_type_cd%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   lv_TaskClassCd      sched_stask.task_class_cd%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;

   -- The Work Order under which this task was completed, may be null
   ld_WoEndDt        evt_event.event_gdt%TYPE;
   ld_WoStartDt      evt_event.actual_start_gdt%TYPE;
   ld_WoSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_WoSchedStartDt evt_event.sched_start_gdt%TYPE;
   lb_WoHistBool     evt_event.hist_bool%TYPE;

   ld_BeginWindowDt    evt_sched_dead.sched_dead_dt%TYPE;
   ld_EndWindowDt      evt_sched_dead.sched_dead_dt%TYPE;

BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ld_StartDt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ld_DueDate          := lrec_task.task_due_dt;
      ll_RefMultQt        := lrec_task.ref_mult_qt;
      lv_DataTypeCd       := lrec_task.data_type_cd;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ld_EndDate          := lrec_task.task_end_dt;
      lb_HistBool         := lrec_task.task_hist_bool;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_TaskClassCd      := lrec_task.task_class_cd;
      ld_RootEndDt        := lrec_task.event_gdt;
      ld_RootStartDt      := lrec_task.actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.sched_start_gdt;
      ld_WoEndDt          := lrec_task.wo_end_gdt;
      ld_WoStartDt        := lrec_task.wo_start_gdt;
      ld_WoSchedEndDt     := lrec_task.wo_sched_end_gdt;
      ld_WoSchedStartDt   := lrec_task.wo_sched_start_gdt;
      lb_WoHistBool       := lrec_task.wo_hist_bool;
   CLOSE lcur_task;


   -- if the previous task is not completed but has a due date (e.g, ACTV or FORECAST)
   IF lb_HistBool = 0 AND ld_DueDate IS NOT NULL THEN
      IF an_ServiceCheck = 0 THEN
        lv_ReschedFromCd := 'LASTDUE';

        -- there is an exception to using the extended due date as the start date;
        --   if the Sched to Plan High value is less then or equal to the Deviation
        --   then use the original due date as the start date
        --   (same goes if neither are set, e.g. if both are null)
        --
        -- this exception mimics the behaviour seen when this task is completed
        -- i.e. scheduling the follow on task from the original due date
        --
        IF NVL(ln_PostfixedQt,0) >= NVL(ln_Deviation,0) THEN
           ld_StartDt := ld_DueDate;
        ELSE
           ld_StartDt := getExtendedDeadlineDt( ln_Deviation,
                                                ld_DueDate,
                                                'CA',
                                                lv_DataTypeCd,
                                                ll_RefMultQt );

        END IF;

      ELSE
         IF ( lv_ReschedFromCd = 'WPEND' AND
                   lb_RootHistBool = 0 AND
                   ld_RootSchedEndDt IS NOT NULL ) THEN
            ld_StartDt := ld_RootSchedEndDt;
         ELSIF
             ( lv_ReschedFromCd = 'WPSTART' AND
                lb_RootHistBool = 0 AND
                ld_RootSchedStartDt IS NOT NULL ) THEN
             ld_StartDt := ld_RootSchedStartDt;
          ELSE
             lv_ReschedFromCd := 'LASTDUE';
             ld_StartDt       := getExtendedDeadlineDt( ln_Deviation,
                                                     ld_DueDate,
                                                     'CA',
                                                     lv_DataTypeCd, ll_RefMultQt );
          END IF;
      END IF;

   -- The previous task is not completed but the data type does not exist in that previous task.
   -- (e.g. the task defn was revised and the data type added while the prev task was committed)
   ELSIF lb_HistBool = 0 AND ld_DueDate IS NULL THEN

      -- Set the reschedule from code to LASTDUE (just like it would if the data type had existed)
      -- and set the start date to be the current date.
      lv_ReschedFromCd := 'LASTDUE';
      ld_StartDt       := SYSDATE;

   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF (NOT (lv_TaskClassCd = 'CHECK' OR lv_TaskClassCd = 'RO')) AND lb_WoHistBool IS NULL THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      CASE
       -- If the previous task is resched from "WPEND" or "WPSTART" and is due before the WP starts
        WHEN ld_DueDate IS NOT NULL AND ld_WoStartDt IS NOT NULL AND ld_DueDate < ld_WoStartDt AND (lv_ReschedFromCd = 'WPEND' OR lv_ReschedFromCd = 'WPSTART')THEN
              lv_ReschedFromCd := 'LASTDUE';
              ld_StartDt     := ld_DueDate;
        WHEN lv_ReschedFromCd = 'WPEND' AND lb_WoHistBool = 1 AND ld_WoEndDt IS NOT NULL THEN
           ld_StartDt := ld_WoEndDt;
        WHEN lv_ReschedFromCd = 'WPEND' AND lb_WoHistBool = 0 AND ld_WoSchedEndDt IS NOT NULL THEN
           ld_StartDt := ld_WoSchedEndDt;
        WHEN lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
           ld_StartDt := ld_RootEndDt;
        WHEN lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
           ld_StartDt := ld_RootSchedEndDt;
        WHEN lv_ReschedFromCd = 'WPSTART' AND lb_WoHistBool = 1 AND ld_WoStartDt IS NOT NULL THEN
           ld_StartDt := ld_WoStartDt;
        WHEN lv_ReschedFromCd = 'WPSTART' AND lb_WoHistBool = 0 AND ld_WoSchedStartDt IS NOT NULL THEN
           ld_StartDt := ld_WoSchedStartDt;
        WHEN lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
           ld_StartDt := ld_RootStartDt;
        WHEN lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
           ld_StartDt := ld_RootSchedStartDt;
        WHEN ld_EndDate IS NOT NULL THEN
           ld_StartDt       := ld_EndDate;
           lv_ReschedFromCd := 'LASTEND';
        ELSE
           NULL;
      END CASE;

      /* (now consider the scheduling window)
       * if the baseline task is not resched from "WPEND" or "WPSTART" and the completion fell within the window, use scheduled completion information */
      IF ld_StartDt IS NOT NULL AND ld_DueDate IS NOT NULL AND NOT (lv_ReschedFromCd = 'WPEND' OR lv_ReschedFromCd = 'WPSTART') THEN
         -- build window
         ld_BeginWindowDt := ld_DueDate - (ln_PrefixedQt * ll_RefMultQt);
         ld_EndWindowDt   := ld_DueDate + (ln_PostfixedQt * ll_RefMultQt);
         IF ( ld_StartDt >= ld_BeginWindowDt ) AND
            ( ld_StartDt <= ld_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ld_StartDt     := ld_DueDate;
         END IF;
      END IF;
  END IF;

  /* if we were able to determine the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ld_StartDt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     ad_StartDt     := ld_StartDt;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     ad_StartDt     := SYSDATE;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromDtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromDtValues;

/********************************************************************************
*
* Function:    GetCurrentEffectiveDate
* Arguments:     an_SchedDbId    (long) - Task primary key
*                an_SchedId      (long) --//--
*                an_DataTypeDbId (long) - Date type primary key
*                an_DataTypeId   (long) --//--
* Return:      Refer to Description
*
* Description:  This function returns the current effective date (start date)
*               of the task, or null if task cannot be found.
*
*********************************************************************************
*
* Copyright 2015 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetCurrentEffectiveDate(
      an_SchedDbId    evt_sched_dead.event_db_id%TYPE,
      an_SchedId      evt_sched_dead.event_db_id%TYPE,
      an_DataTypeDbId evt_sched_dead.data_type_db_id%TYPE,
      an_DataTypeId   evt_sched_dead.data_type_id%TYPE
   ) RETURN DATE
IS
   ld_Return DATE;
BEGIN

   BEGIN
      SELECT
        start_dt
      INTO
        ld_Return
      FROM
        evt_sched_dead
      WHERE
        event_db_id = an_SchedDbId AND
        event_id    = an_SchedId
        AND
        data_type_db_id = an_DataTypeDbId AND
        data_type_id    = an_DataTypeId
      ;
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         RETURN NULL;
   END;

   RETURN ld_Return;

END GetCurrentEffectiveDate;


/********************************************************************************
*
* Procedure:    FindDeadlineStartDate
* Arguments:     an_TaskDbId     (long)     - task definition primary key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_SchedFromLatest (int)   - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ScheduleFromCd  (varchar)     - schedule from option
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                av_TaskClassCd  (varchar) - task class code
* Return:
*                ad_StartDt      (long)    - new deadline start dt
*                av_SchedFromCd  (long)    - new deadline sched from refterm
*                on_Return       (long)    - succss/failure of procedure
*
* Description:  This procedure looks up the start date for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Yungjae Cho
* Recent Date:  2011-03-15
*
*********************************************************************************
*
* Copyright ? 2011 MxI Technologies Ltd.  All Rights Reserved.
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
      ab_SchedFromLatest  IN task_task.sched_from_latest_bool%TYPE,
      ad_EffectiveDt      IN task_task.effective_dt%TYPE,
      av_ScheduleFromCd   IN task_task.task_sched_from_cd%TYPE,
      av_ReschedFromCd    IN task_task.resched_from_cd%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      av_TaskClassCd      IN task_task.task_class_cd%TYPE,
      ab_SyncWithBaseline IN BOOLEAN,
      ad_StartDt          IN OUT evt_sched_dead.start_dt%TYPE,
      av_SchedFromCd      IN OUT evt_sched_dead.sched_from_cd%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

   /* local variables */

   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ld_CustomDate              DATE;
   ln_FaultDbId   sd_fault.fault_db_id%TYPE;
   ln_FaultId sd_fault.fault_id%TYPE;
   ld_FoundOnDate evt_event.actual_start_dt%TYPE;
   ld_StartDt     evt_sched_dead.start_dt%TYPE;
   ln_ServiceCheck            NUMBER;
   ls_TaskModeCd              ref_task_class.class_mode_cd%TYPE;
   ld_MPActvDt                maint_prgm.actv_dt%TYPE;
   ln_InvNoDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId                 inv_inv.inv_no_id%TYPE;

BEGIN

  IF ( av_TaskClassCd IS NOT NULL ) THEN
      /* Find the task mode cd for the task class cd */
     SELECT
        ref_task_class.class_mode_cd
     INTO
        ls_TaskModeCd
     FROM
        ref_task_class
     WHERE
        ref_task_class.task_class_cd = av_TaskClassCd;
  END IF;

   /* set the variable to false*/
   ln_ServiceCheck := 0;

   /* If the task is BLOCK, check if it is the service check task*/
  IF ls_TaskModeCd = 'BLOCK' THEN
     ln_ServiceCheck := lpa_pkg.is_task_service_check( an_SchedDbId, an_SchedId );
   /* If the task is REQ, check if the parent task of the req is the service check task*/
   ELSIF ls_TaskModeCd = 'REQ' THEN
      ln_ServiceCheck := lpa_pkg.is_task_parent_service_check( an_SchedDbId, an_SchedId );
  END IF;

   /* if this task is not a first task */
   IF NOT an_PrevSchedDbId =-1 THEN
      /* set reschedule_from */
      GetRescheduleFromDtValues(
            an_PrevSchedDbId  ,
            an_PrevSchedId ,
            an_DataTypeDbId,
            an_DataTypeId ,
            av_ReschedFromCd,
            ln_ServiceCheck,
            ad_StartDt,
            av_SchedFromCd,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* if there is no previous task */
   ELSE
      ld_CustomDate := TO_DATE('1950-01-01 23:59:59', 'YYYY-MM-DD HH24:MI:SS');

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
            av_SchedFromCd:='CUSTOM';
      /* if this task is adhoc or REPL or CORR */
      ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
            (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
            ad_StartDt     := ld_CustomDate;
            av_SchedFromCd := 'CUSTOM';
      /* if this task is based on the task definition */
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

        /* if this task should not be scheduled from birth */
        IF ( av_ScheduleFromCd = 'EFFECTIVE_DT' ) THEN
           /* if we are not syncing with the baseline then use the current effective date */
           IF NOT ab_SyncWithBaseline THEN
              ad_StartDt     := GetCurrentEffectiveDate(an_SchedDbId,an_SchedId,an_DataTypeDbId,an_DataTypeId);
              av_SchedFromCd := 'EFFECTIV';
           /** if we should schedule from date provided */
           ELSIF ad_PreviousCompletionDt IS NOT NULL THEN
              ad_StartDt     := ad_PreviousCompletionDt;
              av_SchedFromCd := 'CUSTOM';
           ELSIF ad_EffectiveDt IS NOT NULL THEN
              /* always use effective date if sched_from_latest_bool is false */
              IF ( ab_SchedFromLatest = 0 ) THEN
                 ad_StartDt     := ad_EffectiveDt;
                 av_SchedFromCd := 'EFFECTIV';
              /* if there is effective date for the task definition and it is not before the manufactured date */
              ELSIF ( ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt)) THEN
                 ad_StartDt     := ad_EffectiveDt;
                 av_SchedFromCd := 'EFFECTIV';
              /* if the effective date is before the manufactured date, use the manufactured date */
              ELSIF ( ld_ManufactDt IS NOT NULL AND ld_ManufactDt > ad_EffectiveDt ) THEN
                 ad_StartDt     := ld_ManufactDt;
                 av_SchedFromCd := 'EFFECTIV';
              ELSE
                 ad_StartDt     := ld_CustomDate;
                 av_SchedFromCd := 'EFFECTIV';
              END IF;
           ELSE
              /* task should be scheduled from effective date but effective date is null */
              ad_StartDt     := ld_CustomDate;
              av_SchedFromCd := 'EFFECTIV';
           END IF;
        /* if the task should be scheduled from maintenance program activation date*/
        ELSIF av_ScheduleFromCd = 'MP_ACTV_DT' THEN

            /* if we should schedule from date provided */
            IF ad_PreviousCompletionDt IS NOT NULL THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
            ELSE
               /* get main inventory */
               EVENT_PKG.GetMainInventory(
                     an_SchedDbId,
                     an_SchedId,
                     ln_InvNoDbId,
                     ln_InvNoId,
                     on_Return);
               IF on_Return < 0 THEN
                  RETURN;
               END IF;

               /*get maintenance program activation date */
               GetMPActivationDate(
                  an_TaskDbId,
                  an_TaskId,
                  ln_InvNoDbId,
                  ln_InvNoId,
                  ld_MPActvDt,
                  on_Return );
                IF on_Return < 0 THEN
                   RETURN;
                END IF;

              ad_StartDt     := ld_MPActvDt;
              av_SchedFromCd := 'EFFECTIV';
           END IF;
        /* if the task should be scheduled from birth*/
        ELSE

          /* if the task is a create_on_install task, and a completion date of the
             installation task was provided, then use that date */
            IF ad_PreviousCompletionDt IS NOT NULL THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
            ELSIF av_ScheduleFromCd = 'RECEIVED_DT' AND ld_ReceivedDt IS NOT NULL THEN
               /* schedule from received date */
               ad_StartDt     := ld_ReceivedDt;
               av_SchedFromCd := 'BIRTH';
            ELSIF av_ScheduleFromCd = 'MANUFACT_DT' AND ld_ManufactDt IS NOT NULL THEN
               /* schedule from manufacturer date */
               ad_StartDt     := ld_ManufactDt;
               av_SchedFromCd := 'BIRTH';
            ELSE
               /* scheduled from birth but appropriate date is null */
               ad_StartDt     := ld_CustomDate;
               av_SchedFromCd := 'BIRTH';
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartDate@@@ '||SQLERRM);
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
      tsn_qt INTO on_TsnQt
    FROM
   (

      SELECT
         tsn_qt
      FROM
      (
         SELECT
            evt_inv_usage.tsn_qt,
            evt_event.event_dt,
            evt_event.creation_dt
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
            -- for TRK there is no historic usage
            inv_inv.inv_class_cd <> 'TRK'
            AND
            inv_inv.rstat_cd     = 0
            AND
            (
                  -- for SYS, use assembly to get usage
               (
                  inv_inv.inv_class_cd = 'SYS' AND
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
            evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
            evt_inv_usage.event_id     = evt_inv.event_id AND
            evt_inv_usage.event_inv_id = evt_inv.event_inv_id AND
            evt_inv_usage.data_type_db_id = an_DataTypeDbId AND
            evt_inv_usage.data_type_id    = an_DataTypeId AND
            evt_inv_usage.negated_bool    = 0
            AND
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id AND
            evt_event.hist_bool   = 1 AND
            evt_event.event_dt <= ad_TargetDate AND
            evt_event.event_type_cd = 'FG'

         UNION

           SELECT
              usg_usage_data.tsn_qt,
               usg_usage_record.usage_dt as event_dt,
               usg_usage_record.creation_dt as creation_dt
           FROM
              inv_inv,
               inv_inv component_inv,
               usg_usage_data,
               usg_usage_record
           WHERE
               inv_inv.inv_no_db_id = an_InvNoDbId AND
               inv_inv.inv_no_id    = an_InvNoId AND
               -- for TRK there is no historic usage
               inv_inv.inv_class_cd <> 'TRK'
               AND
               inv_inv.rstat_cd     = 0
               AND
               (
                   -- for SYS or TRK, use assembly to get usage
                   (
                   inv_inv.inv_class_cd = 'SYS' AND
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
               usg_usage_data.inv_no_db_id = component_inv.inv_no_db_id AND
               usg_usage_data.inv_no_id    = component_inv.inv_no_id
               AND
               usg_usage_data.data_type_db_id = an_DataTypeDbId AND
               usg_usage_data.data_type_id    = an_DataTypeId AND
               usg_usage_data.negated_bool    = 0
               AND
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt <= ad_TargetDate
           )
           ORDER BY event_dt DESC,creation_dt DESC
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
* Procedure: GetRescheduleFromQtValues
* Arguments:
*           an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*           an_InvNoDbId     (long)    - current task's main inventory primary key
*           an_InvNoId       (long)    --//--
* Return:
*           ad_StartDt     (date)    - current task's start date
*           ad_StartQt     (date)    - current task's start quantity
*           av_SchedFromCd (varchar) - current task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for the next task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromQtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            an_InvNoDbId     IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId       IN  inv_inv.inv_no_id%TYPE,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt       OUT evt_sched_dead.start_qt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_inv_usage.tsn_qt,
           evt_sched_dead.sched_dead_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_event.hist_bool AS task_hist_bool,
           mim_data_type.domain_type_db_id,
           mim_data_type.domain_type_cd,
           root_event.actual_start_gdt AS root_actual_start_gdt,
           root_event.sched_start_gdt AS root_sched_start_gdt,
           root_event.event_gdt  AS root_event_gdt,
           root_event.sched_end_gdt AS root_sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           root_sched_stask.task_class_cd AS root_task_class_cd,
           completed_wo.event_gdt as wo_end_gdt,
           completed_wo.sched_end_gdt as wo_sched_end_gdt,
           completed_wo.actual_start_dt as wo_start_gdt,
           completed_wo.sched_start_gdt as wo_sched_start_gdt,
           completed_wo.hist_bool as wo_hist_bool
        FROM
           evt_event
           LEFT OUTER JOIN evt_sched_dead ON
              evt_sched_dead.event_db_id     = evt_event.event_db_id AND
              evt_sched_dead.event_id        = evt_event.event_id    AND
              evt_sched_dead.data_type_db_id = cn_DataTypeDbId       AND
              evt_sched_dead.data_type_id    = cn_DataTypeId
           LEFT OUTER JOIN evt_inv_usage ON
              evt_inv_usage.event_db_id     = evt_event.event_db_id AND
              evt_inv_usage.event_id        = evt_event.event_id    AND
              evt_inv_usage.data_type_db_id = cn_DataTypeDbId       AND
              evt_inv_usage.data_type_id    = cn_DataTypeId
           LEFT OUTER JOIN mim_data_type ON
              mim_data_type.data_type_db_id = evt_inv_usage.data_type_db_id AND
              mim_data_type.data_type_id    = evt_inv_usage.data_type_id
           INNER JOIN evt_event root_event ON
              root_event.event_db_id = evt_event.h_event_db_id AND
              root_event.event_id    = evt_event.h_event_id
           INNER JOIN sched_stask root_sched_stask ON
              root_sched_stask.sched_db_id = root_event.event_db_id AND
              root_sched_stask.sched_id    = root_event.event_id
           INNER JOIN sched_stask ON
              sched_stask.sched_db_id = evt_event.event_db_id AND
              sched_stask.sched_id    = evt_event.event_id
           LEFT OUTER JOIN evt_event completed_wo ON
              completed_wo.event_db_id = sched_stask.wo_db_id AND
              completed_wo.event_id    = sched_stask.wo_id
        WHERE
           evt_event.event_db_id =  cn_EventDbId     AND
           evt_event.event_id    =  cn_EventId       AND
           evt_event.rstat_cd    = 0;

   lrec_task lcur_task%ROWTYPE;


   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;
   ln_StartQt          evt_sched_dead.start_qt%TYPE;

   ln_DueQt            evt_sched_dead.sched_dead_qt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ln_EndQt            evt_inv_usage.tsn_qt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;
   ln_DomainTypeDbId   mim_data_type.domain_type_db_id%TYPE;
   lv_DomainTypeCd     mim_data_type.domain_type_cd%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;
   lv_RootTaskClassCd  sched_stask.task_class_cd%TYPE;

   ld_WoEndDt        evt_event.event_gdt%TYPE;
   ld_WoStartDt      evt_event.actual_start_gdt%TYPE;
   ld_WoSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_WoSchedStartDt evt_event.sched_start_gdt%TYPE;
   lb_WoHistBool     evt_event.hist_bool%TYPE;


   ln_BeginWindowDt    evt_sched_dead.sched_dead_qt%TYPE;
   ln_EndWindowDt      evt_sched_dead.sched_dead_qt%TYPE;
BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ln_StartQt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ln_DueQt            := lrec_task.sched_dead_qt;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ln_EndQt            := lrec_task.tsn_qt;
      lb_HistBool         := lrec_task.task_hist_bool;
      ln_DomainTypeDbId   := lrec_task.domain_type_db_id;
      lv_DomainTypeCd     := lrec_task.domain_type_cd;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_RootTaskClassCd  := lrec_task.root_task_class_cd;
      ld_RootEndDt        := lrec_task.root_event_gdt;
      ld_RootStartDt      := lrec_task.root_actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.root_sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.root_sched_start_gdt;
      ld_WoEndDt          := lrec_task.wo_end_gdt;
      ld_WoStartDt        := lrec_task.wo_start_gdt;
      ld_WoSchedEndDt     := lrec_task.wo_sched_end_gdt;
      ld_WoSchedStartDt   := lrec_task.wo_sched_start_gdt;
      lb_WoHistBool       := lrec_task.wo_hist_bool;
   CLOSE lcur_task;

   -- the previous task is ACTV, so use the extended due date if given
   IF lb_HistBool = 0 AND ln_DueQt IS NOT NULL THEN
      lv_ReschedFromCd := 'LASTDUE';
      ln_StartQt       := ln_DueQt + ln_Deviation;

   -- The previous task is not completed but the data type does not exist in that previous task.
   -- (e.g. the task defn was revised and the data type added while the prev task was committed)
   ELSIF lb_HistBool = 0 AND ln_DueQt IS NULL THEN

      -- Set the reschedule from code to LASTDUE (just like it would if the data type had existed)
      -- and set the start quantity to the current usage quantity.
      lv_ReschedFromCd := 'LASTDUE';
      SELECT
         inv_curr_usage.tsn_qt
      INTO
         ln_StartQt
      FROM
         inv_curr_usage
      WHERE
         inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
         inv_curr_usage.inv_no_id    = an_InvNoId
         AND
         inv_curr_usage.data_type_db_id = an_DataTypeDbId AND
         inv_curr_usage.data_type_id    = an_DataTypeId;

   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF NOT (lv_RootTaskClassCd = 'CHECK' OR lv_RootTaskClassCd = 'RO') THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      IF lv_ReschedFromCd = 'WPEND' AND lb_WoHistBool = 1 AND ld_WoEndDt IS NOT NULL THEN
         ld_StartDt := ld_WoEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_WoHistBool = 0 AND ld_WoSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_WoSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_WoHistBool = 1 AND ld_WoStartDt IS NOT NULL THEN
         ld_StartDt := ld_WoStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_WoHistBool = 0 AND ld_WoSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_WoSchedStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedStartDt;
      ELSIF ln_EndQt IS NOT NULL THEN
         ln_StartQt       := ln_EndQt;
         lv_ReschedFromCd := 'LASTEND';
      END IF;

      -- if the task was scheduled from WPEND or WPSTART and the data type is usage based,
      -- then use the usage of the work package (if exists)
      IF (
            ( lv_ReschedFromCd = 'WPEND' OR lv_ReschedFromCd = 'WPSTART' )
            AND
            ( ln_DomainTypeDbId = 0 AND lv_DomainTypeCd = 'US' )
         ) THEN
         lv_ReschedFromCd := 'LASTEND';
         ln_StartQt := ln_EndQt;
      END IF;

      -- lookup the usage at the date
      IF ln_StartQt IS NULL AND ld_StartDt IS NOT NULL THEN
         GetHistoricUsageAtDt ( ld_StartDt,
                                an_DataTypeDbId,
                                an_DataTypeId,
                                an_InvNoDbId,
                                an_InvNoId,
                                ln_StartQt,
                                on_Return );
      END IF;

       /* (now consider the scheduling window)
       * if the completion fell within the window, use scheduled completion information */
      IF ln_StartQt IS NOT NULL AND ln_DueQt IS NOT NULL THEN
         -- build window
         ln_BeginWindowDt := ln_DueQt - ln_PrefixedQt;
         ln_EndWindowDt   := ln_DueQt + ln_PostfixedQt;
         IF ( ln_StartQt >= ln_BeginWindowDt ) AND
            ( ln_StartQt <= ln_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ln_StartQt       := ln_DueQt;
         END IF;
      END IF;
   END IF;

  /* if we were able to determine the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ln_StartQt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     an_StartQt     := ln_StartQt;
     -- a future start date indicates that we want to use the forecasting engine,
     -- and in this case we do not.
     IF ld_StartDt IS NOT NULL AND ld_StartDt <= SYSDATE THEN
        ad_StartDt     := ld_StartDt;
     END IF;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     GetCurrentInventoryUsage(
           an_EventDbId,
           an_EventId,
           an_DataTypeDbId,
           an_DataTypeId,
           an_StartQt,
           on_Return );
     IF on_Return < 0 THEN
        RETURN;
     END IF;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromQtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromQtValues;

/********************************************************************************
* Procedure:    AreTasksOnSameUsageInformation
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_PrevSchedDbId (long) - previous task primary key
*                an_PrevSchedId   (long) --//--
* Return:
*                on_EventsShareUsageInfo    (long) - 1 if tasks share the same usage information, otherwise 0
*
* Description:  This procedure checks if the tasks share usage information
*********************************************************************************/
PROCEDURE AreTasksOnSameUsageInformation(
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      an_PrevSchedDbId IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId   IN sched_stask.sched_id%TYPE,
      on_EventsShareUsageInfo OUT NUMBER
) IS
   CURSOR lcur_GetInvInfo (
      ln_SchedDbId IN sched_stask.sched_db_id%TYPE,
      ln_SchedId   IN sched_stask.sched_id%TYPE
   ) IS
   SELECT
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id,
      CASE WHEN inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY' THEN
         inv_inv.inv_no_db_id
      ELSE
         inv_inv.assmbl_inv_no_db_id
      END AS assmbl_inv_no_db_id,
      CASE WHEN inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY' THEN
         inv_inv.inv_no_id
      ELSE
         inv_inv.assmbl_inv_no_id
      END AS assmbl_inv_no_id,
      inv_inv.inv_class_db_id,
      inv_inv.inv_class_cd
   FROM
      sched_stask
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
         inv_inv.inv_no_id    = sched_stask.main_inv_no_id
   WHERE
      sched_stask.sched_db_id = ln_SchedDbId AND
      sched_stask.sched_id    = ln_SchedId
   ;

   lrec_CurrInvInfo lcur_GetInvInfo%ROWTYPE;
   lrec_PrevInvInfo lcur_GetInvInfo%ROWTYPE;

BEGIN
   on_EventsShareUsageInfo := 0;

   OPEN lcur_GetInvInfo(an_SchedDbId, an_SchedId);
   FETCH lcur_GetInvInfo INTO lrec_CurrInvInfo;
   CLOSE lcur_GetInvInfo;

   OPEN lcur_GetInvInfo(an_PrevSchedDbId, an_PrevSchedId);
   FETCH lcur_GetInvInfo INTO lrec_PrevInvInfo;
   CLOSE lcur_GetInvInfo;

   -- if both inventory are the same
   IF lrec_CurrInvInfo.inv_no_db_id = lrec_PrevInvInfo.inv_no_db_id AND
      lrec_CurrInvInfo.inv_no_id    = lrec_PrevInvInfo.inv_no_id THEN
      -- then they share the same usage information
      on_EventsShareUsageInfo := 1;
      RETURN;
   END IF;

   -- if both inventory are on the same assembly
   IF lrec_CurrInvInfo.assmbl_inv_no_db_id = lrec_PrevInvInfo.assmbl_inv_no_db_id AND
      lrec_CurrInvInfo.assmbl_inv_no_id    = lrec_PrevInvInfo.assmbl_inv_no_id THEN
      -- and the inventory uses the assembly's usage
      IF lrec_CurrInvInfo.inv_class_db_id = 0 AND
         lrec_CurrInvInfo.inv_class_cd IN ('ACFT', 'ASSY', 'SYS') THEN
         IF lrec_PrevInvInfo.inv_class_db_id = 0 AND
            lrec_PrevInvInfo.inv_class_cd IN ('ACFT', 'ASSY', 'SYS') THEN
            -- then they share the same usage information
            on_EventsShareUsageInfo := 1;
            RETURN;
         END IF;
      END IF;
   END IF;
END AreTasksOnSameUsageInformation;

/********************************************************************************
*
* Procedure:    FindDeadlineStartQt
* Arguments:     an_TaskDbId     (long)     - task definition key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_SchedFromLatest (int)   - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ScheduleFromCd  (varchar) - schedule from option
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                av_TaskClassCd  (varchar)  - task class code
* Return:
*                ad_StartQt      (long)     - new deadline start qt
*                av_SchedFromCd  (long)     - new deadline sched from refterm
*                ad_StartDt      (long)     - new deadline start dt
*                on_Return       (long)     - succss/failure of procedure
*
* Description:  This procedure looks up the start qt and sched_from_cd for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Yungjae Cho
* Recent Date:  2011-03-15
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartQt (
      an_TaskDbId             IN task_task.task_db_id%TYPE,
      an_TaskId               IN task_task.task_id%TYPE,
      an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
      an_SchedId              IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId         IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId           IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId          IN sched_stask.sched_id%TYPE,
      ab_SchedFromLatest      IN task_task.sched_from_latest_bool%TYPE,
      ad_EffectiveDt          IN task_task.effective_dt%TYPE,
      av_ScheduleFromCd       IN task_task.task_sched_from_cd%TYPE,
      av_ReschedFromCd        IN task_task.resched_from_cd%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      av_TaskClassCd          IN task_task.task_class_cd%TYPE,
      ab_SyncWithBaseline     IN BOOLEAN,
      an_StartQt              IN OUT evt_sched_dead.start_qt%TYPE,
      av_SchedFromCd          IN OUT evt_sched_dead.sched_from_cd%TYPE,
      ad_StartDt              OUT evt_sched_dead.start_dt%TYPE,
      on_Return               OUT typn_RetCode
   ) IS


   /* local variables */
   ln_InvNoDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId                 inv_inv.inv_no_id%TYPE;
   ls_InvClassCd              inv_inv.inv_class_cd%TYPE;
   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ln_FaultDbId               sd_fault.fault_db_id%TYPE;
   ln_FaultId                 sd_fault.fault_id%TYPE;
   ld_FoundOnDate             evt_event.actual_start_dt%TYPE;
   ld_StartDt                 evt_sched_dead.start_dt%TYPE;
   ln_CustomQt                NUMBER;
   lIsInstalledOnAircraft     NUMBER;
   lAircraftDbId              NUMBER;
   lAircraftId                NUMBER;
   lSchedFromEffectiveDt      NUMBER;
   ln_EventsShareUsageInfo    NUMBER;
   lb_SyncWithBaseline        NUMBER(1); -- 0/1 only
   ld_MPActvDt                maint_prgm.actv_dt%TYPE;
BEGIN

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

   /* if this task is not a first task being created */
   IF NOT an_PrevSchedId = -1 THEN

      AreTasksOnSameUsageInformation(
            an_SchedDbId,
            an_SchedId,
            an_PrevSchedDbId,
            an_PrevSchedId,
            ln_EventsShareUsageInfo
      );
      IF ln_EventsShareUsageInfo = 0 THEN
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

         av_SchedFromCd:='CUSTOM';

      /* if the task is on the same usage information as the previous task */
      ELSE
         /* set reschedule_from */
         GetRescheduleFromQtValues(
               an_PrevSchedDbId,
               an_PrevSchedId,
               an_DataTypeDbId,
               an_DataTypeId,
               av_ReschedFromCd,
               ln_InvNoDbId,
               ln_InvNoId,
               ad_StartDt,
               an_StartQt,
               av_SchedFromCd,
               on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;

   /*if this task is a first task being created */
   ELSE
      ln_CustomQt := 0;

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
            ad_StartDt     := ld_StartDt;
            av_SchedFromCd := 'CUSTOM';
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
               IF on_Return < 0 THEN
                  RETURN;
               END IF;

               lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft( ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
               IF lIsInstalledOnAircraft=1 THEN
                  EVENT_PKG.PredictUsageBetweenDt(
                        lAircraftDbId,
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
            ad_StartDt     := ld_FoundOnDate;
            av_SchedFromCd := 'CUSTOM';

            /* get usage snapshot of this fault */
            GetOldEvtInvUsage(
                  ln_FaultDbId,
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
            (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
         av_SchedFromCd := 'CUSTOM';
         an_StartQt     := ln_CustomQt;

      /* if task is based on task definition */
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

         /* Retrieve inventory class code */
         SELECT
            inv_class_cd
         INTO
            ls_InvClassCd
         FROM
            inv_inv
         WHERE
            inv_no_db_id = ln_InvNoDbId AND
            inv_no_id    = ln_InvNoId;

         /* if the task is scheduled from install date */
         IF ( ad_PreviousCompletionDt IS NOT NULL) THEN
            ad_StartDt     := ad_PreviousCompletionDt;
            av_SchedFromCd := 'CUSTOM';

            -- if the component is TRK
            IF ( ls_InvClassCd = 'TRK' ) THEN
               -- use the usage at installation (which is the current usage)
               SELECT
                  tsn_qt
               INTO
                  an_StartQt
               FROM
                  inv_curr_usage
               WHERE
                  inv_no_db_id = ln_InvNoDbId AND
                  inv_no_id    = ln_InvNoId
                  AND
                  data_type_db_id = an_DataTypeDbId AND
                  data_type_id    = an_DataTypeId;
            ELSE
               /* get the usage at the installation snapshot */
               GetHistoricUsageAtDt(
                     ad_PreviousCompletionDt,
                     an_DataTypeDbId,
                     an_DataTypeId,
                     ln_InvNoDbId,
                     ln_InvNoId,
                     an_StartQt,
                     on_Return);
               IF on_Return < 0 THEN
                  RETURN;
               END IF;
            END IF;

         /* if the task should be scheduled from birth */
         ELSIF (av_ScheduleFromCd = 'RECEIVED_DT' OR av_ScheduleFromCd = 'MANUFACT_DT' ) THEN
            /* if scheduled from receive date  */
            IF av_ScheduleFromCd = 'RECEIVED_DT' AND ld_ReceivedDt IS NOT NULL THEN
               av_SchedFromCd := 'BIRTH';

               /* for TRK component the start value should be always zero */
               IF ( ls_InvClassCd = 'TRK' ) THEN
                  an_StartQt := 0;
               ELSE
                  ad_StartDt     := ld_ReceivedDt;
                  /* get the usage at received date */
                  IF ld_ReceivedDt<SYSDATE THEN
                     GetHistoricUsageAtDt(
                           ld_ReceivedDt,
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
                     IF on_Return < 0 THEN
                        RETURN;
                     END IF;
                     lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                     IF lIsInstalledOnAircraft=1 THEN
                        EVENT_PKG.PredictUsageBetweenDt(
                              lAircraftDbId,
                              lAircraftId,
                              an_DataTypeDbId,
                              an_DataTypeId,
                              SYSDATE ,
                              ld_ReceivedDt,
                              an_StartQt,
                              an_StartQt,
                              on_Return);
                     END IF;
                  END IF;
                  IF on_Return < 0 THEN
                     RETURN;
                  END IF;
               END IF;

            /* if not scheduled from receive date then must be scheduled from manufactured date */
            ELSIF av_ScheduleFromCd = 'MANUFACT_DT' THEN
               -- we do not enforce a manufactured date in order to schedule from BIRTH for usage rules
               av_SchedFromCd := 'BIRTH';
               an_StartQt     := 0;
            /* scheduled from receive date but received date is null  */
            ELSE
               av_SchedFromCd := 'BIRTH';
               an_StartQt     := 0;
            END IF;

         /* if the task should be scheduled from maintenance program activation date*/
         ELSIF av_ScheduleFromCd = 'MP_ACTV_DT' THEN

            /*get maintenance program activation date */
             GetMPActivationDate(
                an_TaskDbId,
                an_TaskId,
                ln_InvNoDbId,
                ln_InvNoId,
                ld_MPActvDt,
                on_Return );

            IF on_Return < 0 THEN
               RETURN;
            END IF;

            ad_StartDt     := ld_MPActvDt;
            av_SchedFromCd := 'EFFECTIV';

            GetHistoricUsageAtDt(
               ld_MPActvDt,
               an_DataTypeDbId,
               an_DataTypeId,
               ln_InvNoDbId,
               ln_InvNoId,
               an_StartQt,
               on_Return);

           IF on_Return < 0 THEN
              RETURN;
           END IF;

         /* if the task should not be scheduled from birth */
         ELSIF ad_EffectiveDt IS NOT NULL THEN
            -- if sched_from_latest_bool is false then always use effective date
            IF ( ab_SchedFromLatest = 0 ) THEN
               lSchedFromEffectiveDt := 1;
            -- if effective date is later than manufactured date then use effective date
            ELSIF (ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt ) ) THEN
               lSchedFromEffectiveDt := 1;
            -- otherwise use manufactured date
            ELSE
               lschedfromEffectiveDt := 0;
            END IF;

            IF ( lschedfromEffectiveDt = 1 ) THEN

               av_SchedFromCd := 'EFFECTIV';

               -- this converting is only for unit test to GetStartDtWhenSchedFromEffDt(...)
               IF (ab_SyncWithBaseline) THEN
                   lb_SyncWithBaseline := 1;
               ELSE
                   lb_SyncWithBaseline := 0;
               END IF;

               ad_StartDt := GetStartDtWhenSchedFromEffDt(an_SchedDbId,
                                                          an_SchedId,
                                                          an_DataTypeDbId,
                                                          an_DataTypeId,
                                                          ab_SchedFromLatest,
                                                          lb_SyncWithBaseline,
                                                          ad_EffectiveDt,
                                                          ld_ManufactDt);

               /* Start date in the past */
               IF ad_StartDt < SYSDATE THEN

                  IF ( ls_InvClassCd = 'TRK' ) THEN
                     -- TRK components with an effective date in the past
                     -- will use last calculated start quantity (or 0 if doesn't exist).
                     GetTasksUsageStartQt(
                        an_SchedDbId,
                        an_SchedId,
                        an_DataTypeDbId,
                        an_DataTypeId,
                        an_StartQt,
                        on_Return);
                  ELSE
                     GetHistoricUsageAtDt(
                           ad_StartDt,
                           an_DataTypeDbId,
                           an_DataTypeId,
                           ln_InvNoDbId,
                           ln_InvNoId,
                           an_StartQt,
                           on_Return);
                  END IF;

               /* Start date in the future or current. */
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

                  lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);

                  /* Component is installed on an aircraft,
                     so predict the start quantity at the future effective date, based on current usage. */
                  IF lIsInstalledOnAircraft=1 THEN
                     EVENT_PKG.PredictUsageBetweenDt(
                           lAircraftDbId,
                           lAircraftId,
                           an_DataTypeDbId,
                           an_DataTypeId,
                           SYSDATE ,
                           ad_StartDt,
                           an_StartQt,
                           an_StartQt,
                           on_Return);
                  END IF;

               END IF;

               IF on_Return < 0 THEN
                  RETURN;
               END IF;

            /* use the manufactured date, since it is later than the effective date */
            ELSE
               av_SchedFromCd := 'EFFECTIV';
               an_StartQt     := 0;
            END IF;
         /* task should be scheduled from effective date but the effective date is null */
         ELSE
            av_SchedFromCd := 'EFFECTIV';
            an_StartQt     := 0;
         END IF;
      END IF;
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartQt@@@: '||SQLERRM);
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
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_SchedFromLatest   task_task.sched_from_latest_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ScheduleFromCd task_task.task_sched_from_cd%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_OnConditionBool task_task.on_condition_bool%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;
      ld_ManufactDt     inv_inv.manufact_dt%TYPE;
      ld_ReceivedDt     inv_inv.received_dt%TYPE;

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
            ln_SchedFromLatest,
            ld_EffectiveDt,
            lv_ScheduleFromCd,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_OnConditionBool,
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
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
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

   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      /* find the start date of the deadline */
      FindDeadlineStartDate(
         ln_TaskDbId,
         ln_TaskId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_SchedFromLatest,
         ld_EffectiveDt,
         lv_ScheduleFromCd,
         lv_ReschedFromCd,
         ad_PreviousCompletionDt,
         ls_TaskClassCd,
         abSyncWithBaseline,
         ld_StartDt,
         ls_SchedFromCd,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   GetMainInventoryBirthInfo(
               an_SchedDbId,
               an_SchedId,
               ld_ManufactDt,
               ld_ReceivedDt,
               on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   IF ls_SchedFromCd = 'EFFECTIV' AND ln_SchedFromLatest = 1 AND ld_ManufactDt IS NOT NULL THEN
      IF ld_ManufactDt <= ld_EffectiveDt THEN
         ld_StartDt := ld_EffectiveDt;
      ELSE
         ld_StartDt := ld_ManufactDt;
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
* Function:    IsOnCondAndEffectDtIsNull
* Arguments:     an_TaskDbId    (long) - Task definition primary key
*                an_TaskId      (long) --//--
* Return:      Refer to Description
*
* Description:  This function returns true if the task definition has
*               the following criteria:
*                - on-condition
*                - scheduled from an effective date
*                - effective date is not set (null)
*
*
*********************************************************************************
*
* Copyright 2015 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsOnCondAndEffectDtIsNull(
      an_TaskDbId   task_task.task_db_id%TYPE,
      an_TaskId     task_task.task_id%TYPE
   ) RETURN NUMBER
IS
   ln_Return NUMBER := 0;
BEGIN

   BEGIN
   SELECT
      NVL2( task_task.task_db_id, 1, 0 )
   INTO
      ln_Return
   FROM
      task_task
   WHERE
      task_task.task_db_id = an_TaskDbId AND
      task_task.task_id    = an_TaskId
      AND
      task_task.on_condition_bool = 1
      AND
      task_task.task_sched_from_db_id = 0 AND
      task_task.task_sched_from_cd    = 'EFFECTIVE_DT'
      AND
      task_task.effective_dt IS NULL
   ;
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         RETURN 0;
   END;

   RETURN ln_Return;

END IsOnCondAndEffectDtIsNull;


/********************************************************************************
*
* Function:    GetTaskUsageCustomStartDate
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
* Return:      The custom start date of the task's usage scheduling rules, if exists.
*              Otherwise returns null.
*
* Description:  This function checks if the task has a usage based scheduling
*               rule that has a custom start value.
*               If so, then the scheduling rule's custom start date is returned.
*
*               Note: if any of the usage based scheduling rules have a custom
*               start date then all other usage based scheduling rules will have
*               that same custom start date.
*
*********************************************************************************
*
* Copyright 2015 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskUsageCustomStartDate(
      an_SchedDbId   sched_stask.sched_db_id%TYPE,
      an_SchedId     sched_stask.sched_id%TYPE
   ) RETURN DATE
IS
   ld_CustomStartDt evt_sched_dead.start_dt%TYPE;
BEGIN

   BEGIN
      SELECT
         evt_sched_dead.start_dt
      INTO
         ld_CustomStartDt
      FROM
         evt_sched_dead
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
      WHERE
         evt_sched_dead.event_db_id = an_SchedDbId AND
         evt_sched_dead.event_id    = an_SchedId
         AND
         evt_sched_dead.sched_from_db_id = 0 AND
         evt_sched_dead.sched_from_cd    = 'CUSTOM'
         AND
         evt_sched_dead.start_dt IS NOT NULL
         AND
         mim_data_type.domain_type_db_id = 0 AND
         mim_data_type.domain_type_cd    = 'US'
         AND
         ROWNUM = 1
      ;
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         ld_CustomStartDt := NULL;
   END;

   RETURN ld_CustomStartDt;

END GetTaskUsageCustomStartDate;

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
      ln_SchedFromLatest   task_task.sched_from_latest_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ScheduleFromCd task_task.task_sched_from_cd%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;
      ln_OnConditionBool task_task.on_condition_bool%TYPE;
      ld_PreviousCompletionDt  evt_event.sched_end_gdt%TYPE;

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

   -- Local copy of the previous completion date
   -- that may need to be adjusted within this procedure
   ld_PreviousCompletionDt := ad_PreviousCompletionDt;

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
            ln_SchedFromLatest,
            ld_EffectiveDt,
            lv_ScheduleFromCd,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_OnConditionBool,
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
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
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

   -- Specialized code for the scenario where:
   --  - task definition is an on-condition, scheduled from effective date, with no effective date, usage scheduling rule
   --  - task was initalized with an effective date (CUSTOM deadline start quantity based on that effective date)
   --  - task definition is revised and another usage schedule rule added
   -- Result is we want to calculate the deadline start quantity of the newly added schedulign rule using the
   -- effective date provided at the initialization and not the null effective date of the task definition.
   -- Refer to jira: OPER-3928
   IF ld_PreviousCompletionDt IS NULL AND IsOnCondAndEffectDtIsNull( ln_TaskDbId, ln_TaskId ) = 1 THEN
     ld_PreviousCompletionDt := GetTaskUsageCustomStartDate( an_SchedDbId, an_SchedId );
   END IF;

   IF
      ls_SchedFromCd IS NULL
      OR
      ls_SchedFromCd != 'CUSTOM'
      OR
      (
         ln_PrevSchedDbId = -1 AND
         ln_OnConditionBool = 1 AND
         ld_PreviousCompletionDt IS NOT NULL
      )
   THEN
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
         ln_SchedFromLatest,
         ld_EffectiveDt,
         lv_ScheduleFromCd,
         lv_ReschedFromCd,
         ld_PreviousCompletionDt,
         ls_TaskClassCd,
         abSyncWithBaseline,
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
            sched_stask.rstat_cd = 0
            AND
            task_sched_rule.task_db_id = sched_stask.task_db_id AND
            task_sched_rule.task_id    = sched_stask.task_id
            AND
            mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
            mim_data_type.data_type_id    = task_sched_rule.data_type_id
      )
      ORDER BY data_type_db_id, data_type_id;

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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareSchedDeadlines@@@: '||SQLERRM);
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
         sched_stask.rstat_cd = 0;

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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlines@@@: '||SQLERRM);
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
             START WITH inv_inv.inv_no_db_id    = cn_InvNoDbId AND
                        inv_inv.inv_no_id       = cn_InvNoId
             CONNECT BY inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
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
                       evt_sched_dead.sched_from_cd IN ('BIRTH', 'EFFECTIV'));
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
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'prep_deadline_pkg@@@PrepareDeadlineForInv@@@' || SQLERRM);
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
   ln_SchedFromLatest     task_task.sched_from_latest_bool%TYPE;
   ld_EffectiveDt         task_task.effective_dt%TYPE;
   lv_ScheduleFromCd      task_task.task_sched_from_cd%TYPE;
   lv_ReschedFromCd       task_task.resched_from_cd%TYPE;
   ln_RecurringBool       task_task.recurring_task_bool%TYPE;
   ln_OnConditionBool     task_task.on_condition_bool%TYPE;
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
         ln_SchedFromLatest,
         ld_EffectiveDt,
       lv_ScheduleFromCd,
         lv_ReschedFromCd,
         ln_RecurringBool,
         ln_OnConditionBool,
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
      task_task.sched_from_latest_bool,
      task_task.effective_dt,
      task_task.task_sched_from_cd,
      task_task.resched_from_cd,
      istaskdefnrecurring(an_TaskTaskDbId, an_TaskTaskID ),
      task_task.last_sched_dead_bool,
      task_task.task_class_cd
   INTO
      orec_ScheduleDetails.SchedFromLatestBool,
      orec_ScheduleDetails.EffectiveDt,
      orec_ScheduleDetails.ScheduleFromCd,
      orec_ScheduleDetails.ReschedFromCd,
      orec_ScheduleDetails.RecurringBool,
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
* Recent Coder: Yungjae Cho
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
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

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

   -- by default we want to sync with the baseline to find the start date
   ab_SyncWithBaseline BOOLEAN := TRUE;

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
         arec_Schedule.SchedFromLatestBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ScheduleFromCd,
         arec_Schedule.ReschedFromCd,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ab_SyncWithBaseline,
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
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

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

   ls_SchedFromCd          ref_sched_from.sched_from_cd%TYPE;
   ld_StartDt              evt_sched_dead.start_dt%TYPE;
   ln_StartQt              evt_sched_dead.start_qt%TYPE;
   ln_Return               typn_RetCode;

   -- by default we want to sync with the baseline to find the start date
   ab_SyncWithBaseline BOOLEAN := TRUE;

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
         arec_Schedule.SchedFromLatestBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ScheduleFromCd,
         arec_Schedule.ReschedFromCd,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ab_SyncWithBaseline,
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
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            TRUE,
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
      as_DataTypeCd    mim_data_type.data_type_cd   %TYPE,
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

   lrec_Deadline        DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);

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
            an_DataTypeId
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
            an_DataTypeId
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
   lrec_Deadline.DataTypeCd   := as_DataTypeCd;
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
         lrec_Deadline.DataTypeCd,
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
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE

   ) RETURN VARCHAR2
IS

   lrec_Schedule    typrec_ScheduleDetails;
   lrec_CurrentDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);
   lrec_DrivingDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);


   -- Get the new baseline interval and deviation for the REV requirement
   CURSOR lcur_NewBaselines (
      crec_Schedule typrec_ScheduleDetails
   ) IS
      SELECT
         task_sched_rule.data_type_db_id,
         task_sched_rule.data_type_id
      FROM
         task_sched_rule
      WHERE
         task_sched_rule.task_db_id    = an_RevisionTaskTaskDbId AND
         task_sched_rule.task_id       = an_RevisionTaskTaskId;

   lrec_NewBaseline           lcur_NewBaselines%ROWTYPE;
   ln_Return                  typn_RetCode;

   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;
   ls_DeadlineDt              varchar2(17);--contain a date of format 'DD-MON-YYYY hh:mm'
   ls_ExtendedDeadlineDt      varchar2(17);--contain a date of format 'DD-MON-YYYY hh:mm'

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
            ls_DataTypeCd,
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

   IF (lrec_DrivingDeadline.DeadlineDt IS NULL) THEN
      ls_DeadlineDt := '';
   ELSE
      ls_DeadlineDt := TO_CHAR( lrec_DrivingDeadline.DeadlineDt, 'DD-MON-YYYY' ) || ' 23:59';
   END IF;

   IF (lrec_DrivingDeadline.ExtendedDeadlineDt IS NULL) THEN
      ls_ExtendedDeadlineDt := '';
   ELSE
      ls_ExtendedDeadlineDt := TO_CHAR( lrec_DrivingDeadline.ExtendedDeadlineDt, 'DD-MON-YYYY' ) || ' 23:59';
   END IF;

   RETURN lrec_DrivingDeadline.DomainTypeCd || '*' || lrec_DrivingDeadline.UsageRemainingQt || '*' || lrec_DrivingDeadline.EngUnitCd || '*' ||
          lrec_DrivingDeadline.MultiplierQt || '*' || ls_DeadlineDt || '*' || lrec_DrivingDeadline.DeviationQt || '*' || ls_ExtendedDeadlineDt;


END GetForecastedDrivingDueDate;

/********************************************************************************
*
* Function:     GetStartDtWhenSchedFromEffDt
* Arguments:
*            an_SchedDbId        - task primary key
*            an_SchedId            --//--
*            an_DataTypeDbId     - data type primary key
*            an_DataTypeId         --//--
*            ab_SchedFromLatest  - boolean
*            ab_SyncWithBaseline - boolean
*            ad_EffectiveDt      - effective date of the task definition
*            ad_ManufactDt       - manufacter date
*
* Return:       The start date for the task deadline.
*
* Description:  Calculate the deadline start date when scheduled from effective date for the task deadline based on given conditions.
*
*
*********************************************************************************/
FUNCTION GetStartDtWhenSchedFromEffDt(
            an_SchedDbId              IN sched_stask.sched_db_id%TYPE,
            an_SchedId                IN sched_stask.sched_id%TYPE,
            an_DataTypeDbId           IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId             IN task_sched_rule.data_type_id%TYPE,
            ab_SchedFromLatest        IN task_task.sched_from_latest_bool%TYPE,
            ab_SyncWithBaseline       IN INTEGER,--0/1 only
            ad_EffectiveDt            IN task_task.effective_dt%TYPE,
            ad_ManufactDt             IN inv_inv.manufact_dt%TYPE

   ) RETURN DATE
IS

   ld_StartDt       evt_sched_dead.start_dt%TYPE;

BEGIN

   IF ab_SyncWithBaseline = 1 THEN
      -- if syncing with the baseline then use the passed in task definitions effective date
      ld_StartDt     := ad_EffectiveDt;
   ELSE
      -- otherwise use the current effective date of the task
      ld_StartDt := GetCurrentEffectiveDate(an_SchedDbId,an_SchedId,an_DataTypeDbId,an_DataTypeId);
   END IF;

   IF ld_StartDt IS NULL THEN
      IF ab_SchedFromLatest = 1 AND ad_EffectiveDt < ad_ManufactDt THEN
         ld_StartDt := ad_ManufactDt;
      ELSE
         ld_StartDt := ad_EffectiveDt;
      END IF;
   END IF ;

   RETURN ld_StartDt;


END GetStartDtWhenSchedFromEffDt;

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
            an_TaskTaskId       IN task_task.task_id      %TYPE
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
   ltabrec_Rules        := GetTaskDefnRules( an_ActiveTaskTaskDbId,   an_ActiveTaskTaskId );
   ltabrec_RevisedRules := GetTaskDefnRules( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId );

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
   lrec_DeadlineInfo  DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);

   ln_Return          typn_RetCode;

BEGIN
   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_TaskTaskDbId, an_TaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules := GetTaskDefnRules( an_TaskTaskDbId, an_TaskTaskId );

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
            ls_DataTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      -- Output the deadline information.
      PIPE ROW( lrec_DeadlineInfo );
   END LOOP;

END GetTaskDeadlines;


/********************************************************************************
*
* Procedure:    ResetActualSchedFromToBaseline
* Arguments:    an_SchedDbId (long) - the task to be rescheduled
*               an_SchedId   (long) - ""
* Return:       on_Return    (long) - success/failure of procedure
*
* Description:  This procedure will reset the "schedule from" code of the task
*               to that of its definition and reset the start value for each
*               of the task's deadlines (based on that schedule from code).
*               In addition, if the next forecast task has a custom
*               "schedule from" code, it is reset to last-due.
*
*               Note: it is not the intent of this procedure to recalculate
*                     the task's deadline due values. That is performed by
*                     event_pkg.UpdateDeadline().
*
*********************************************************************************
*
* Copyright 2000-2016 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE ResetActualSchedFromToBaseline(
   an_SchedDbId   IN  sched_stask.sched_db_id%TYPE,
   an_SchedId     IN  sched_stask.sched_id%TYPE,
   on_Return      OUT typn_RetCode
) IS

   ln_RuleIntervalQty          task_sched_rule.def_interval_qt%TYPE;
   ln_RuleInitialQty           task_sched_rule.def_initial_qt%TYPE;
   ln_RuleNotifyQty            task_sched_rule.def_notify_qt%TYPE;
   ln_RuleDeviationQty         task_sched_rule.def_deviation_qt%TYPE;
   ln_RulePrefixedQty          task_sched_rule.def_prefixed_qt%TYPE;
   ln_RulePostfixedQty         task_sched_rule.def_postfixed_qt%TYPE;
   ln_SchedulingRuleExists     task_sched_rule.def_postfixed_qt%TYPE;

   ld_StartDate                evt_sched_dead.start_dt%TYPE;
   ln_StartQty                 evt_sched_dead.start_qt%TYPE;
   ls_SchedFromCd              evt_sched_dead.sched_from_cd%TYPE;
   ln_IntervalQty              evt_sched_dead.interval_qt%TYPE;
   ld_DueDate                  evt_sched_dead.sched_dead_dt%TYPE;
   ln_DueQty                   evt_sched_dead.sched_dead_qt%TYPE;

   -- Cursor to retrieve all the task deadline information to be reset to baseline.
   CURSOR lcur_TaskDeadlinesForReset(
             an_SchedDbId sched_stask.sched_db_id%TYPE,
             an_SchedId   sched_stask.sched_id%TYPE
   ) IS (
      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id,
         task_task.task_db_id,
         task_task.task_id,
         task_task.sched_from_latest_bool,
         task_task.effective_dt,
         task_task.task_sched_from_db_id,
         task_task.task_sched_from_cd,
         task_task.resched_from_db_id,
         task_task.resched_from_cd,
         task_task.task_class_db_id,
         task_task.task_class_cd,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.interval_qt,
         evt_sched_dead.notify_qt,
         evt_sched_dead.deviation_qt,
         evt_sched_dead.prefixed_qt,
         evt_sched_dead.postfixed_qt,
         mim_data_type.domain_type_db_id,
         mim_data_type.domain_type_cd,
         evt_inv.part_no_db_id,
         evt_inv.part_no_id,
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         NVL2( evt_event_rel.event_db_id, evt_event_rel.event_db_id, -1 ) AS prev_sched_db_id,
         NVL2( evt_event_rel.event_id,    evt_event_rel.event_id,    -1 ) AS prev_sched_id
      FROM
         sched_stask
         INNER JOIN task_task ON
            task_task.task_db_id = sched_stask.task_db_id AND
            task_task.task_id    = sched_stask.task_id
         INNER JOIN evt_sched_dead ON
            evt_sched_dead.event_db_id = sched_stask.sched_db_id AND
            evt_sched_dead.event_id    = sched_stask.sched_id
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         INNER JOIN evt_inv ON
            evt_inv.event_db_id   = sched_stask.sched_db_id AND
            evt_inv.event_id      = sched_stask.sched_id    AND
            evt_inv.main_inv_bool = 1
         LEFT OUTER JOIN evt_event_rel ON
            evt_event_rel.rel_event_db_id = sched_stask.sched_db_id AND
            evt_event_rel.rel_event_id    = sched_stask.sched_id AND
            evt_event_rel.rel_type_db_id  = 0 AND
            evt_event_rel.rel_type_cd     = 'DEPT'
      WHERE
         sched_stask.sched_db_id = an_SchedDbId AND
         sched_stask.sched_id    = an_SchedId
   );

BEGIN

   on_Return := icn_NoProc;

   FOR lrec_TaskDeadline IN lcur_TaskDeadlinesForReset(an_SchedDbId,an_SchedId) LOOP

      -- Attempt to get the corresponding task definition's scheduling rule for the task's deadline.
      -- (unfortunately the procedure calls it a "baseline deadline" when in fact its a "scheduling rule")
      GetBaselineDeadline(
         lrec_TaskDeadline.data_type_db_id,
         lrec_TaskDeadline.data_type_id,
         lrec_TaskDeadline.task_db_id,
         lrec_TaskDeadline.task_id,
         lrec_TaskDeadline.sched_db_id,
         lrec_TaskDeadline.sched_id,
         lrec_TaskDeadline.part_no_db_id,
         lrec_TaskDeadline.part_no_id,
         lrec_TaskDeadline.h_inv_no_db_id,
         lrec_TaskDeadline.h_inv_no_id,
         ln_RuleIntervalQty,
         ln_RuleInitialQty,
         ln_RuleNotifyQty,
         ln_RuleDeviationQty,
         ln_RulePrefixedQty,
         ln_RulePostfixedQty,
         ln_SchedulingRuleExists,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_SchedulingRuleExists = 0 THEN
         -- This task's deadline does not have a corresponding scheduling rule in the task's definition.
         -- So we will skip this one and leave it as a custom deadline.
         CONTINUE;
      END IF;

      -- Continue to use the deadline interval from the actual task
      -- (which may have been overwritten with custom values).
      ln_IntervalQty  := lrec_TaskDeadline.interval_qt;

      IF lrec_TaskDeadline.domain_type_db_id = 0 AND lrec_TaskDeadline.domain_type_cd = 'CA' THEN

         -- The task deadline is calendar based so find the deadline start date.
         FindDeadlineStartDate(
            lrec_TaskDeadline.task_db_id,
            lrec_TaskDeadline.task_id,
            lrec_TaskDeadline.sched_db_id,
            lrec_TaskDeadline.sched_id,
            lrec_TaskDeadline.data_type_db_id,
            lrec_TaskDeadline.data_type_id,
            lrec_TaskDeadline.prev_sched_db_id,
            lrec_TaskDeadline.prev_sched_id,
            lrec_TaskDeadline.sched_from_latest_bool,
            lrec_TaskDeadline.effective_dt,
            lrec_TaskDeadline.task_sched_from_cd,
            lrec_TaskDeadline.resched_from_cd,
            NULL, -- previous completion date
            lrec_TaskDeadline.task_class_cd,
            TRUE, -- sync with baseline
            ld_StartDate,
            ls_SchedFromCd,
            on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         -- Based on the deadline start date, determine the due date
         -- and possibly modify the start date and inverval.
         -- (Due to rounding and other adjustments based on the data type.)
         ld_DueDate := NULL;

         FindCalendarDeadlineVariables(
            lrec_TaskDeadline.data_type_db_id,
            lrec_TaskDeadline.data_type_id,
            ld_StartDate,
            ln_IntervalQty,
            ld_DueDate,
            on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;

          -- Calendar deadlines have no start quantity nor due quantity.
          ln_StartQty    := null;
          ln_DueQty   := null;

      ELSE

         -- The task deadline is usage based so find the deadline start quantity..
         FindDeadlineStartQt(
            lrec_TaskDeadline.task_db_id,
            lrec_TaskDeadline.task_id,
            lrec_TaskDeadline.sched_db_id,
            lrec_TaskDeadline.sched_id,
            lrec_TaskDeadline.data_type_db_id,
            lrec_TaskDeadline.data_type_id,
            lrec_TaskDeadline.prev_sched_db_id,
            lrec_TaskDeadline.prev_sched_id,
            lrec_TaskDeadline.sched_from_latest_bool,
            lrec_TaskDeadline.effective_Dt,
            lrec_TaskDeadline.task_sched_from_cd,
            lrec_TaskDeadline.resched_from_cd,
            NULL, -- previous completion date
            lrec_TaskDeadline.task_class_cd,
            TRUE, -- sync with baseline
            ln_StartQty,
            ls_SchedFromCd,
            ld_StartDate,
            on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         -- Based on the deadline start quanitity, determine the deadline quantity.
		 ln_DueQty   := null; --reset
         FindUsageDeadlineVariables(
            ln_StartQty,
            ln_IntervalQty,
            ln_DueQty,
            on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         -- Usage deadlines have no due date.
         ld_DueDate := null;

      END IF;

      UpdateDeadlineRow(
         lrec_TaskDeadline.sched_db_id,
         lrec_TaskDeadline.sched_id,
         lrec_TaskDeadline.data_type_db_id,
         lrec_TaskDeadline.data_type_id,
         ln_StartQty,
         ld_StartDate,
         ls_SchedFromCd,
         ln_DueQty,
         ld_DueDate,
         ln_IntervalQty,
         lrec_TaskDeadline.notify_qt,
         lrec_TaskDeadline.deviation_qt,
         lrec_TaskDeadline.prefixed_qt,
         lrec_TaskDeadline.postfixed_qt,
         on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

      -- If the next FORECAST task has the rule scheduled from CUSTOM
      -- then modify it to be scheduled from LASTDUE.
      UPDATE
         evt_sched_dead
      SET
         evt_sched_dead.sched_from_cd = 'LASTDUE'
      WHERE
         evt_sched_dead.data_type_db_id  = lrec_TaskDeadline.data_type_db_id AND
         evt_sched_dead.data_type_id     = lrec_TaskDeadline.data_type_id
         AND
         evt_sched_dead.sched_from_db_id = 0 AND
         evt_sched_dead.sched_from_cd    = 'CUSTOM'
         AND
         (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
         IN
         (
            SELECT
               evt_event_rel.rel_event_db_id,
               evt_event_rel.rel_event_id
            FROM
               evt_event,
               evt_event_rel
            WHERE
               evt_event_rel.event_db_id    = lrec_TaskDeadline.sched_db_id AND
               evt_event_rel.event_id       = lrec_TaskDeadline.sched_id
               AND
               evt_event_rel.rel_type_db_id = 0 AND
               evt_event_rel.rel_type_cd    = 'DEPT'
               AND
               evt_event.event_db_id        = evt_event_rel.rel_event_db_id AND
               evt_event.event_id           = evt_event_rel.rel_event_id
               AND
               evt_event.event_status_db_id = 0 AND
               evt_event.event_status_cd    = 'FORECAST'
         );

   END LOOP;

   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@ResetActualSchedFromToBaseline@@@: '||SQLERRM);
      RETURN;

END ResetActualSchedFromToBaseline;

END PREP_DEADLINE_PKG;
/