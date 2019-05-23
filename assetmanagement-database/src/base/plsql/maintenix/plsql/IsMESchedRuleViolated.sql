--liquibase formatted sql


--changeSet IsMESchedRuleViolated:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedue:      IsMESchedRuleViolated
* Arguments:     an_SchedDbId    - the db id of the actual task.
*                an_SchedId      - the id of the actual task.
*                an_DataTypeDbId - the db id of the data type for which the  measurement specific scheduling
*                                  needs to be verified.
*                an_DataTypeId   - the id of the data type for which the  measurement specific scheduling needs
*                                  to be verified.
*                an_IsCompleteTask - 0 if the this is not a task completion action   
*
* Return:        on_Return    (number) - success/failure of the procedure
*
* Description:   This procedure returns 1 if the measurements scheduling were violated.
*  Following steps are followed in the procedure below:
*  1) First it is verified if the task is baselined and it is a recurring requirement.
*  2) Check if measurement scheduling rules exists
*  3) Check if any of the JICs has exactly one measurement for the measurement scheduling rule.
*  4) Check if the measurement scheduling rule is in 'Increasing' or 'Decreasing' mode.
*  5) Check if the measurement scheduling rule is violated by comparing the measurement from task with task defn.
*
* Note: There will be only one measurement specific scheduling rule for a datatype.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE IsMESchedRuleViolated
(
    an_SchedDbId IN sched_stask.sched_db_id%TYPE,
    an_SchedId   IN sched_stask.sched_db_id%TYPE,
    an_DataTypeDbId     IN mim_data_type.data_type_db_id%TYPE,
    an_DataTypeId       IN mim_data_type.data_type_id%TYPE,
    an_IsCompleteTask   IN  NUMBER,
    on_Return           OUT NUMBER
)

IS
   ln_TempNum    NUMBER;
   ln_parm_qt    NUMBER;
   ln_incr_bool  NUMBER;
   ln_limit_value  NUMBER;
   ln_task_class sched_stask.task_class_cd%TYPE;
   ln_task_db_id task_task.task_db_id%TYPE;
   ln_task_id    task_task.task_id%TYPE;
   ln_MEDataTypeDbId   mim_data_type.data_type_db_id%TYPE;
   ln_MEDataTypeId     mim_data_type.data_type_id%TYPE;

   /* Cursor to get the Previous JIC or Previous executable requirement measurements for a specific Data Type */
   CURSOR lcur_jic_me_pre (
      cn_SchedDbId IN sched_stask.sched_db_id%TYPE,
      cn_SchedId   IN sched_stask.sched_db_id%TYPE,
      cn_DataTypeDbId     IN mim_data_type.data_type_db_id%TYPE,
      cn_DataTypeId       IN mim_data_type.data_type_id%TYPE
   ) IS
      SELECT inv_parm_data.*
      FROM
         evt_event_rel,
         sched_stask,
         evt_event,
         inv_parm_data
      WHERE
         -- Get the Sub JICs of the Previous REQ
         evt_event_rel.rel_event_db_id = cn_SchedDbId AND
         evt_event_rel.rel_event_id    = cn_SchedId
         AND
         evt_event_rel.rel_type_cd = 'DEPT'
         AND
         evt_event.nh_event_db_id = evt_event_rel.event_db_id AND
         evt_event.nh_event_id =    evt_event_rel.event_id
         AND
         sched_stask.sched_db_id = evt_event.event_db_id  AND
         sched_stask.sched_id    = evt_event.event_id
         AND
         sched_stask.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd = 'JIC')
         AND
         -- Get the Measurements of the JIC
         evt_event.event_db_id = inv_parm_data.event_db_id  AND
         evt_event.event_id    = inv_parm_data.event_id
         AND
         inv_parm_data.data_type_db_id  =  cn_DataTypeDbId  AND
         inv_parm_data.data_type_id     =  cn_DataTypeId

      UNION

      -- get the measurement of requirement itself as there will be no child JICs
      SELECT inv_parm_data.*
      FROM
        evt_event_rel,
        sched_stask,
        evt_event,
        inv_parm_data,
        task_task
      WHERE
        -- Get the Previous REQ
        evt_event_rel.rel_event_db_id = cn_SchedDbId AND
        evt_event_rel.rel_event_id    = cn_SchedId
        AND
        evt_event_rel.rel_type_cd = 'DEPT'
        AND
        evt_event.event_db_id = evt_event_rel.event_db_id AND
        evt_event.event_id =    evt_event_rel.event_id
        AND
        sched_stask.sched_db_id = evt_event.event_db_id  AND
        sched_stask.sched_id    = evt_event.event_id
        AND
        sched_stask.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd = 'REQ')
        AND
        -- Get the Measurements
        evt_event.event_db_id = inv_parm_data.event_db_id  AND
        evt_event.event_id    = inv_parm_data.event_id
        AND
        inv_parm_data.data_type_db_id  =  cn_DataTypeDbId  AND
        inv_parm_data.data_type_id     =  cn_DataTypeId
        AND
        task_task.task_db_id = sched_stask.task_db_id   AND
        task_task.task_id    = sched_stask.task_id
        AND
        task_task.workscope_bool = 1 ;

    lrec_jic_me_pre   lcur_jic_me_pre%ROWTYPE;



   /* Cursor to get the JIC or executable requirement measurements for a specific Data Type */
   CURSOR lcur_jic_me (
      cn_SchedDbId IN sched_stask.sched_db_id%TYPE,
      cn_SchedId   IN sched_stask.sched_db_id%TYPE,
      cn_DataTypeDbId     IN mim_data_type.data_type_db_id%TYPE,
      cn_DataTypeId       IN mim_data_type.data_type_id%TYPE
   ) IS
      SELECT inv_parm_data.*
      FROM
         sched_stask,
         evt_event,
         inv_parm_data
      WHERE
         -- Get the Sub JICs of the REQ
         evt_event.nh_event_db_id = cn_SchedDbId AND
         evt_event.nh_event_id =    cn_SchedId
         AND
         sched_stask.sched_db_id = evt_event.event_db_id  AND
         sched_stask.sched_id    = evt_event.event_id
         AND
         sched_stask.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd = 'JIC')
         AND
         -- Get the Measurements of the JIC
         evt_event.event_db_id = inv_parm_data.event_db_id  AND
         evt_event.event_id    = inv_parm_data.event_id
         AND
         inv_parm_data.data_type_db_id  =  cn_DataTypeDbId  AND
         inv_parm_data.data_type_id     =  cn_DataTypeId

      UNION

      -- get the measurement of requirement itself as there will be no child JICs
      SELECT inv_parm_data.*
      FROM
        sched_stask,
        evt_event,
        inv_parm_data,
        task_task
      WHERE
        -- Get the REQ
        evt_event.event_db_id = cn_SchedDbId AND
        evt_event.event_id    = cn_SchedId
        AND
        sched_stask.sched_db_id = evt_event.event_db_id  AND
        sched_stask.sched_id    = evt_event.event_id
        AND
        sched_stask.task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd = 'REQ')
        AND
        -- Get the Measurements of the JIC
        evt_event.event_db_id = inv_parm_data.event_db_id  AND
        evt_event.event_id    = inv_parm_data.event_id
        AND
        inv_parm_data.data_type_db_id  =  cn_DataTypeDbId  AND
        inv_parm_data.data_type_id     =  cn_DataTypeId
        AND
        task_task.task_db_id = sched_stask.task_db_id   AND
        task_task.task_id    = sched_stask.task_id
        AND
        task_task.workscope_bool = 1;

    lrec_jic_me   lcur_jic_me%ROWTYPE;




BEGIN
    on_Return := 0;

    /* Only verify for REQs */
   SELECT
      task_class_cd, task_db_id, task_id
   INTO
      ln_task_class, ln_task_db_id, ln_task_id
   FROM
      sched_stask
   WHERE
      task_class_cd IN (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd = 'REQ')
      AND
      sched_db_id = an_SchedDbId AND
      sched_id =    an_SchedId;
   IF (ln_task_class IS NULL) OR (ln_task_db_id IS NULL) THEN
      RETURN;
   END IF;

   /* Only verify if Recurring */
   SELECT task_task.recurring_task_bool
   INTO ln_TempNum
   FROM
      task_task
   WHERE
      task_task.task_db_id  = ln_task_db_id AND
      task_task.task_id     = ln_task_id ;
   IF ln_TempNum = 0 THEN
      RETURN;
   END IF;

   /* Only verify if ME Rules exist */
   SELECT COUNT(*)
   INTO ln_TempNum
   FROM task_me_rule
   WHERE
      task_db_id            = ln_task_db_id   AND
      task_id               = ln_task_id      AND
      rule_data_type_db_id  = an_DataTypeDbId AND
      rule_data_type_id     = an_DataTypeId;
   IF ln_TempNum = 0 THEN
      RETURN;
   END IF;

   /* Get the ME Rule Data Type */
   SELECT
      DECODE( task_me_rule.me_data_type_db_id, NULL, -1, task_me_rule.me_data_type_db_id ),
      DECODE( task_me_rule.me_data_type_id,    NULL, -1, task_me_rule.me_data_type_id )
   INTO
      ln_MEDataTypeDbId,
      ln_MEDataTypeId
   FROM
      task_me_rule
   WHERE
      task_db_id            = ln_task_db_id   AND
      task_id               = ln_task_id      AND
      rule_data_type_db_id  = an_DataTypeDbId AND
      rule_data_type_id     = an_DataTypeId;

   IF ln_MEDataTypeDbId = -1 THEN
      RETURN;
   END IF;



   /* If this is a task complete action then look for measurements in the current task(or it's children) itself,
   else in previous task(or it's children) */
   /*  Confirm that the JIC has exactly one measurement for the Data Type */
   ln_parm_qt := -1;

   IF an_IsCompleteTask > 0 THEN


       FOR lrec_jic_me IN lcur_jic_me(an_SchedDbId, an_SchedId, ln_MEDataTypeDbId, ln_MEDataTypeId)
       LOOP
          IF lcur_jic_me%ROWCOUNT > 1 THEN
             RETURN;
          END IF;

          /* get the measurement*/
          ln_parm_qt := lrec_jic_me.parm_qt;
       END LOOP;
       IF ln_parm_qt = -1 THEN
          RETURN;
       END IF;

   ELSE

       FOR lrec_jic_me_pre IN lcur_jic_me_pre(an_SchedDbId, an_SchedId, ln_MEDataTypeDbId, ln_MEDataTypeId)
       LOOP
          IF lcur_jic_me_pre%ROWCOUNT > 1 THEN
             RETURN;
          END IF;

          /* get the measurement*/
          ln_parm_qt := lrec_jic_me_pre.parm_qt;
       END LOOP;
       IF ln_parm_qt = -1 THEN
          RETURN;
       END IF;
   END IF;



    /* Determine if the ME Rule is for Increasing or Decreasing */

   SELECT increasing_values_bool
   INTO ln_incr_bool
   FROM task_me_rule
   WHERE
      task_db_id  = ln_task_db_id AND
      task_id     = ln_task_id
      AND
      rule_data_type_db_id  = an_DataTypeDbId AND
      rule_data_type_id     = an_DataTypeId;

   /* If Increasing then get the Max Rule, otherwise get the Min Rule */
   SELECT
      CASE task_me_rule.increasing_values_bool
         WHEN 1 THEN MAX(task_me_rule_interval.me_qt)
         ELSE        MIN(task_me_rule_interval.me_qt)
      END AS limit_value
   INTO ln_limit_value
   FROM
      task_me_rule,
      task_me_rule_interval
   WHERE
      task_me_rule.task_db_id           = task_me_rule_interval.task_db_id  AND
      task_me_rule.task_id              = task_me_rule_interval.task_id  AND
      task_me_rule.rule_data_type_db_id = task_me_rule_interval.rule_data_type_db_id  AND
      task_me_rule.rule_data_type_id    = task_me_rule_interval.rule_data_type_id  AND
      task_me_rule.me_data_type_db_id   = task_me_rule_interval.me_data_type_db_id  AND
      task_me_rule.me_data_type_id      = task_me_rule_interval.me_data_type_id
      AND
      task_me_rule.task_db_id  = ln_task_db_id AND
      task_me_rule.task_id     = ln_task_id
      AND
      task_me_rule.rule_data_type_db_id  = an_DataTypeDbId AND
      task_me_rule.rule_data_type_id     = an_DataTypeId
   GROUP BY
      task_me_rule.increasing_values_bool;


   /* If Increasing and Previous actual measurement exceeds the limit, return true */
   IF (ln_incr_bool = 1) AND (ln_parm_qt > ln_limit_value) THEN
      on_Return := 1;
   /* If Decreasing and Previous actual measurement is lower than the limit, return true */
   ELSE
      IF (ln_incr_bool = 0) AND (ln_parm_qt < ln_limit_value) THEN
         on_Return := 1;
      END IF;
   END IF;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := -1;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
      RETURN;

END IsMESchedRuleViolated;
/