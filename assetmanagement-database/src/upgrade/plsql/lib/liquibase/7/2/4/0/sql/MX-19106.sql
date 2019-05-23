--liquibase formatted sql


--changeSet MX-19106:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            an_TaskDbId,
            an_TaskId,
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCorrectiveFaultInfo@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetLastCompletionUsageInfo@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindUsageDeadlineVariables@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetOldCompleteDate@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetMainInventoryBirthInfo@@@'||SQLERRM);
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
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCurrentInventoryUsage@@@'||SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlinesTree@@@: '||SQLERRM);
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
        IF (an_RelativeBool=1) THEN
          /** if we should schedule from date provided */
          IF ad_PreviousCompletionDt IS NOT NULL THEN
             ad_StartDt := ad_PreviousCompletionDt;
             as_SchedFromCd := 'CUSTOM';
          /* if there is effective date for the task definition and it is not before the manufactured date */
          ELSIF ad_EffectiveDt IS NOT NULL AND ( ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt)) THEN
            ad_StartDt:=ad_EffectiveDt;
            as_SchedFromCd:='EFFECTIV';
           /* if there is no effective date or the manufactured date is later */
           ELSIF ld_ManufactDt IS NOT NULL THEN
            ad_StartDt:=ld_ManufactDt;
            as_SchedFromCd:='CUSTOM';          
           ELSE
            ad_StartDt:=SYSDATE;
            as_SchedFromCd:='CUSTOM';
           END IF;
        /* if the task should be scheduled from birth*/
        ELSE

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
               evt_inv_usage.event_db_id  = evt_inv.event_db_id    AND
               evt_inv_usage.event_id     = evt_inv.event_id       AND
               evt_inv_usage.event_inv_id = evt_inv.event_inv_id   AND
               evt_inv_usage.data_type_db_id = an_DataTypeDbId     AND
               evt_inv_usage.data_type_id    = an_DataTypeId       AND
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
          ELSIF ad_EffectiveDt IS NOT NULL AND (ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt ) ) THEN
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
          ELSIF ld_ManufactDt IS NOT NULL THEN
               an_StartQt:=0; 
               as_SchedFromCd:='CUSTOM';
              
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
            ln_TaskDbId,
            ln_TaskId,
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
            ln_TaskDbId,
            ln_TaskId,
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
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
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
   lrec_DeadlineInfo  DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);

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
            ls_EngUnitCd, 
            ll_RefMultQt 
         );

      -- Output the deadline information.
      PIPE ROW( lrec_DeadlineInfo );
   END LOOP;

END GetTaskDeadlines;


END PREP_DEADLINE_PKG;
/