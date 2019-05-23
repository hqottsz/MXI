--liquibase formatted sql


--changeSet GetMESchedRuleDeadline:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedue:      GetMESchedRuleDeadline
* Arguments:     an_SchedDbId      - the db id of the actual task.
*                an_SchedId        - the id of the actual task.
*                an_TaskTask_pk    - the task definition to use for rule retrieval
*                an_DataTypeDbId   - the db id of the data type for which the  measurement specific scheduling 
*                                    needs to be verified.
*                an_DataTypeId     - the id of the data type for which the  measurement specific scheduling needs 
*                                    to be verified.
*                an_IntervalQt_me  -  variable to return 'Interval' value.
*                an_NotifyQt_me    -  variable to return 'Notification' value.
*                an_DeviationQt_me -  variable to return 'Deviation' value.
*                an_PrefixedQt_me  -  variable to return 'Prefix' value.
*                an_PostfixedQt_me -  variable to return 'Postfix' value.
*
* Return:        on_Return    (number) - success/failure of the procedure    
*
* Description:   This procedure returns measurements scheduling rule values based on actual task measurements.
*  Following steps are followed in the procedure below:
*  1) First it is verified if the task is baselined and it is a recurring requirement.
*  2) Check if measurement scheduling rules exists
*  3) Check if any of the JICs has exactly one measurement for the measurement scheduling rule.
*  4) Check if the measurement scheduling rule is in 'Increasing' or 'Decreasing' mode.
*  5) Return the measurement scheduling rule values from task defn, by comparing against the measurement from task. 
*
* Note: There will be only one measurement specific scheduling rule for a datatype.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE GetMESchedRuleDeadline
(
    an_SchedDbId        IN  sched_stask.sched_db_id%TYPE,
    an_SchedId          IN  sched_stask.sched_db_id%TYPE,
    an_TaskTaskDbId     IN  task_task.task_db_id%TYPE,
    an_TaskTaskId       IN  task_task.task_id%TYPE,
    an_DataTypeDbId     IN  mim_data_type.data_type_db_id%TYPE,
    an_DataTypeId       IN  mim_data_type.data_type_id%TYPE,
    an_IntervalQt_me    OUT task_sched_rule.def_interval_qt%TYPE,
    an_NotifyQt_me      OUT task_sched_rule.def_notify_qt%TYPE,
    an_DeviationQt_me   OUT task_sched_rule.def_deviation_qt%TYPE,
    an_PrefixedQt_me    OUT task_sched_rule.def_prefixed_qt%TYPE,
    an_PostfixedQt_me   OUT task_sched_rule.def_postfixed_qt%TYPE,
    on_Return           OUT NUMBER
)

IS
    ln_parm_qt        NUMBER;
    ln_incr_bool      NUMBER;
    ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
    ln_PrevSchedId    sched_stask.sched_db_id%TYPE;
    ln_MEDataTypeDbId mim_data_type.data_type_db_id%TYPE;
    ln_MEDataTypeId   mim_data_type.data_type_id%TYPE;

    -- retrieve the measurement scheduling rule
    CURSOR lcur_me_rule ( cb_IncreasingValuesBool task_me_rule.Increasing_Values_Bool%TYPE ) IS
      SELECT
             task_me_rule_interval.interval_qt,
             task_me_rule_interval.notification_qt,
             task_me_rule_interval.deviation_qt,
             task_me_rule_interval.prefix_qt,
             task_me_rule_interval.postfix_qt,
             task_me_rule_interval.me_qt,
             DECODE (cb_IncreasingValuesBool, 1, task_me_rule_interval.me_qt, -1*task_me_rule_interval.me_qt) AS sort_column
      FROM
             task_me_rule_interval
      WHERE
             task_me_rule_interval.task_db_id           = an_TaskTaskDbId     AND
             task_me_rule_interval.task_id              = an_TaskTaskId       AND
             task_me_rule_interval.rule_data_type_db_id = an_DataTypeDbId     AND
             task_me_rule_interval.rule_data_type_id    = an_DataTypeId
      ORDER BY
             sort_column;

    lrec_me_rule  lcur_me_rule%ROWTYPE;


    -- retrieve the measurement information
    CURSOR lcur_jic_me ( cn_SchedDbId     IN sched_stask.sched_db_id%TYPE,
                         cn_SchedId       IN sched_stask.sched_id%TYPE,
                         cn_DataTypeDbId  IN mim_data_type.data_type_db_id%TYPE,
                         cn_DataTypeId    IN mim_data_type.data_type_id%TYPE  )   IS
      SELECT
             inv_parm_data.parm_qt
      FROM
             sched_stask,
             ref_task_class,
             evt_event,
             inv_parm_data
      WHERE
             evt_event.nh_event_db_id = cn_SchedDbId AND
             evt_event.nh_event_id =    cn_SchedId
             AND
             inv_parm_data.event_db_id     = evt_event.event_db_id AND
             inv_parm_data.event_id        = evt_event.event_id    AND
             inv_parm_data.data_type_db_id = cn_DataTypeDbId       AND
             inv_parm_data.data_type_id    = cn_DataTypeId
             AND
             sched_stask.sched_db_id = evt_event.event_db_id  AND
             sched_stask.sched_id    = evt_event.event_id
             AND
             ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
             ref_task_class.task_class_cd    = sched_stask.task_class_cd    AND
             ref_task_class.class_mode_cd    = 'JIC'

      UNION ALL

      -- get the measurement of requirement itself as there will be no child JICs
      SELECT
             inv_parm_data.parm_qt
      FROM
             sched_stask,
             evt_event,
             inv_parm_data,
             task_task
      WHERE
             evt_event.event_db_id = cn_SchedDbId AND
             evt_event.event_id =    cn_SchedId
             AND
             inv_parm_data.event_db_id     = evt_event.event_db_id AND
             inv_parm_data.event_id        = evt_event.event_id    AND
             inv_parm_data.data_type_db_id = cn_DataTypeDbId       AND
             inv_parm_data.data_type_id    = cn_DataTypeId
             AND
             sched_stask.sched_db_id = evt_event.event_db_id  AND
             sched_stask.sched_id    = evt_event.event_id
             AND
             task_task.task_db_id     = sched_stask.task_db_id   AND
             task_task.task_id        = sched_stask.task_id      AND
             task_task.workscope_bool =1 ;

    lrec_jic_me        lcur_jic_me%ROWTYPE;

    -- retrieve the details of the task definition
    CURSOR lcur_Details IS
      SELECT
             evt_event_rel.event_db_id,
             evt_event_rel.event_id,
             task_me_rule.increasing_values_bool,
             task_me_rule.me_data_type_db_id,
             task_me_rule.me_data_type_id
      FROM
             evt_event_rel,
             task_task,
             task_me_rule,
             ref_task_class
     WHERE
             task_task.task_db_id          = an_TaskTaskDbId AND
             task_task.task_id             = an_TaskTaskId   AND
             task_task.recurring_task_bool = 1
             AND
             task_me_rule.task_db_id           = task_task.task_db_id AND
             task_me_rule.task_id              = task_task.task_id    AND
             task_me_rule.rule_data_type_db_id = an_DataTypeDbId      AND
             task_me_rule.rule_data_type_id    = an_DataTypeId
             AND
             ref_task_class.task_class_db_id = task_task.task_class_db_id AND
             ref_task_class.task_class_cd    = task_task.task_class_cd    AND
             ref_task_class.class_mode_cd    = 'REQ'
             AND
             evt_event_rel.rel_event_db_id = an_SchedDbId AND
             evt_event_rel.rel_event_id    = an_SchedId   AND
             evt_event_rel.rel_type_db_id  = 0            AND
             evt_event_rel.rel_type_cd     = 'DEPT';

    lrec_Details lcur_Details%ROWTYPE;

BEGIN

    on_Return := 0;

    OPEN lcur_Details;
    FETCH lcur_Details INTO lrec_Details;
    IF NOT lcur_Details%FOUND THEN
       CLOSE lcur_Details;
       -- either the defn is null or is not recurring,
       -- or the class is not mode REQ,
       -- or there are no measurement rules
       -- or any previous tasks.
       RETURN;
    END IF;

    ln_MEDataTypeDbId := lrec_Details.me_data_type_db_id;
    ln_MEDataTypeId   := lrec_Details.me_data_type_id;
    ln_PrevSchedDbId  := lrec_Details.event_db_id;
    ln_PrevSchedId    := lrec_Details.event_id;
    ln_incr_bool      := lrec_Details.increasing_values_bool;
    CLOSE lcur_Details;

    /*  If JIC has a single measurement than proceed else return */
    OPEN lcur_jic_me(ln_PrevSchedDbId, ln_PrevSchedId, ln_MEDataTypeDbId, ln_MEDataTypeId);
    FETCH lcur_jic_me INTO lrec_jic_me;
    IF NOT (lcur_jic_me%FOUND AND lcur_jic_me%ROWCOUNT = 1) THEN
       CLOSE lcur_jic_me;
       RETURN;
    END IF;

     /* get the measurement*/
    ln_parm_qt := lrec_jic_me.parm_qt;
    CLOSE lcur_jic_me;

    /* retrive the interval value*/
    FOR lrec_me_rule IN lcur_me_rule(ln_incr_bool) LOOP
      IF ln_incr_bool = 1 THEN

          IF  ln_parm_qt <= lrec_me_rule.me_qt THEN
              /* break the loop after first success */
               an_IntervalQt_me   := lrec_me_rule.interval_qt;
               an_NotifyQt_me     := lrec_me_rule.notification_qt ;
               an_DeviationQt_me  := lrec_me_rule.deviation_qt;
               an_PrefixedQt_me   := lrec_me_rule.prefix_qt;
               an_PostfixedQt_me  := lrec_me_rule.postfix_qt;
               on_Return := 1;
               RETURN;
          END IF;

      ELSE
          IF  ln_parm_qt >= lrec_me_rule.me_qt THEN
              /* break the loop after first success */
               an_IntervalQt_me   := lrec_me_rule.interval_qt;
               an_NotifyQt_me     := lrec_me_rule.notification_qt ;
               an_DeviationQt_me  := lrec_me_rule.deviation_qt;
               an_PrefixedQt_me   := lrec_me_rule.prefix_qt;
               an_PostfixedQt_me  := lrec_me_rule.postfix_qt;
               on_Return := 1;
               RETURN;
          END IF;
      END IF;
    END LOOP;


    EXCEPTION
        WHEN OTHERS THEN
            -- Unexpected error
            on_Return := -1;
             IF lcur_Details%ISOPEN THEN CLOSE lcur_Details; END IF;
             IF lcur_jic_me%ISOPEN  THEN CLOSE lcur_jic_me;  END IF;
             IF lcur_me_rule%ISOPEN THEN CLOSE lcur_me_rule; END IF;
            APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
            RETURN;

END GetMESchedRuleDeadline;
/