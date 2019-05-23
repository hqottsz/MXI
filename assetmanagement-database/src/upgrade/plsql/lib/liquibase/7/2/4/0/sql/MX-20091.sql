--liquibase formatted sql


--changeSet MX-20091:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getFormatedRepeatInterval
(
   aSchedDbId    sched_stask.sched_db_id%TYPE,
   aSchedId      sched_stask.sched_id%TYPE,
   aEngUnitCd    mim_data_type.eng_unit_cd%TYPE
) RETURN VARCHAR2

IS

lRepeatInterval      VARCHAR2(400);
lFirstParm           VARCHAR2(400);

CURSOR lCurSchedStask(
   cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
   cn_SchedId      IN sched_stask.sched_id%TYPE
   ) IS
     SELECT
        sched_stask.adhoc_recur_bool,
        next_sched_stask.sched_db_id,
        next_sched_stask.sched_id,
        task_task.task_db_id,
        task_task.task_id
     FROM
        sched_stask,
        task_task,
        evt_event_rel next_sched_rel,
        evt_event next_sched,
        sched_stask next_sched_stask
     WHERE
        sched_stask.sched_db_id = cn_SchedDbId AND
        sched_stask.sched_id    = cn_SchedId   AND
        sched_stask.rstat_cd    = 0
        AND
        task_task.task_db_id (+)= sched_stask.task_db_id AND
        task_task.task_id    (+)= sched_stask.task_id
        AND
        next_sched_rel.event_db_id (+)= sched_stask.sched_db_id AND
        next_sched_rel.event_id    (+)= sched_stask.sched_id    AND
        next_sched_rel.rel_type_cd (+)= 'DEPT'
        AND
        next_sched.event_db_id (+)= next_sched_rel.rel_event_db_id AND
        next_sched.event_id    (+)= next_sched_rel.rel_event_id    AND
        next_sched.rstat_cd    (+)= 0
        AND
        next_sched_stask.sched_db_id (+)= next_sched.event_db_id AND
        next_sched_stask.sched_id    (+)= next_sched.event_id;
    lRecSchedStask lCurSchedStask%ROWTYPE;


CURSOR lCurEvtSchedDead(
   cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
   cn_SchedId      IN sched_stask.sched_id%TYPE
   ) IS
     SELECT
        evt_sched_dead.interval_qt,
        mim_data_type.eng_unit_cd,
        mim_data_type.entry_prec_qt
     FROM
        sched_stask,
        evt_sched_dead,
        mim_data_type
     WHERE
        sched_stask.sched_db_id = cn_SchedDbId AND
        sched_stask.sched_id    = cn_SchedId   AND
        sched_stask.rstat_cd    = 0
        AND
        evt_sched_dead.event_db_id = sched_stask.sched_db_id AND
        evt_sched_dead.event_id    = sched_stask.sched_id    AND
        evt_sched_dead.rstat_cd    = 0
        AND
        mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
        mim_data_type.data_type_id    = evt_sched_dead.data_type_id
     -- Need to show in Sorted Order
     ORDER BY
         mim_data_type.eng_unit_cd;
    lRecEvtSchedDead lCurEvtSchedDead%ROWTYPE;


CURSOR lCurTaskSchedRule(
   cn_TaskDbId    IN task_task.task_db_id%TYPE,
   cn_TaskId      IN task_task.task_id%TYPE
   ) IS
     SELECT
        task_sched_rule.def_interval_qt AS interval_qt, 
        mim_data_type.eng_unit_cd,
        mim_data_type.entry_prec_qt
     FROM
        task_sched_rule,
        mim_data_type
     WHERE
        task_sched_rule.task_db_id = cn_TaskDbId AND
        task_sched_rule.task_id    = cn_TaskId
        AND
        mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
        mim_data_type.data_type_id    = task_sched_rule.data_type_id
     -- Need to show in Sorted Order
     ORDER BY
         mim_data_type.eng_unit_cd;
    lRecTaskSchedRule lCurTaskSchedRule%ROWTYPE;


BEGIN

lRepeatInterval  := '';


FOR  lRecSchedStask IN lCurSchedStask (aSchedDbId, aSchedId ) LOOP

   -- Check if Task is recurring 
   IF((lRecSchedStask.adhoc_recur_bool = 1) 
        OR 
       (lRecSchedStask.task_db_Id IS NOT NULL AND isTaskDefnRecurring(lRecSchedStask.task_db_id, lRecSchedStask.task_id) =  1) 
    ) THEN

     -- Check whether Next Task Exists
      IF(lRecSchedStask.sched_db_id IS NOT NULL ) THEN 

         FOR  lRecEvtSchedDead IN lCurEvtSchedDead (lRecSchedStask.sched_db_id, lRecSchedStask.sched_id ) LOOP
            IF ( ( lRecEvtSchedDead.interval_qt IS NOT NULL ) AND ( lRecEvtSchedDead.interval_qt != 0 ) ) THEN
               IF(lRecEvtSchedDead.eng_unit_cd = aEngUnitCd ) THEN
                  lFirstParm := ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
               ELSE
                  lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
               END IF;
            END IF;
         END LOOP;

      -- Check whether Task Defn Exists
      ELSE IF lRecSchedStask.task_db_id IS NOT NULL THEN
         FOR  lRecTaskSchedRule IN lCurTaskSchedRule (lRecSchedStask.task_db_id, lRecSchedStask.task_id ) LOOP
            IF ( ( lRecTaskSchedRule.interval_qt IS NOT NULL ) AND ( lRecTaskSchedRule.interval_qt != 0 ) ) THEN
               IF(lRecTaskSchedRule.eng_unit_cd = aEngUnitCd ) THEN
                  lFirstParm := ', ' || TO_CHAR(ROUND(lRecTaskSchedRule.interval_qt,lRecTaskSchedRule.entry_prec_qt)) || ' ' || lRecTaskSchedRule.eng_unit_cd;
               ELSE
                  lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lRecTaskSchedRule.interval_qt,lRecTaskSchedRule.entry_prec_qt)) || ' ' || lRecTaskSchedRule.eng_unit_cd;
               END IF;
            END IF;
         END LOOP;

       ELSE 
          FOR  lRecEvtSchedDead IN lCurEvtSchedDead (aSchedDbId, aSchedId ) LOOP
             IF ( ( lRecEvtSchedDead.interval_qt IS NOT NULL ) AND ( lRecEvtSchedDead.interval_qt != 0 ) ) THEN
                IF(lRecEvtSchedDead.eng_unit_cd = aEngUnitCd ) THEN
                   lFirstParm := ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                ELSE
                   lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                END IF;
             END IF;
          END LOOP;
       END IF;
       END IF;
    ELSE 
       RETURN NULL;   
    END IF;
END LOOP;

   lRepeatInterval := lFirstParm || lRepeatInterval;

IF lRepeatInterval IS NULL THEN
   RETURN ' ';
END IF;

RETURN substr(lRepeatInterval, 3);

END getFormatedRepeatInterval;
/