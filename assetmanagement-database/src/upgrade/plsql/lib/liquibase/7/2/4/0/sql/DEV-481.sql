--liquibase formatted sql


--changeSet DEV-481:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getFormatedRepeatInterval
(
   aSchedDbId    sched_stask.sched_db_id%TYPE,
   aSchedId      sched_stask.sched_id%TYPE,
   aEngUnitCd    mim_data_type.eng_unit_cd%TYPE
) RETURN VARCHAR2

IS

lRepeatInterval      VARCHAR2(400);
lFirstParm          VARCHAR2(400);
lUsageRemDev 	    NUMBER;
lRecurring          NUMBER; 


CURSOR lCurEvtSchedDead(
   cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
   cn_SchedId      IN sched_stask.sched_id%TYPE
   ) IS
     SELECT
        mim_data_type.domain_type_cd,
        evt_sched_dead.usage_rem_qt,
        mim_data_type.eng_unit_cd,
        mim_data_type.entry_prec_qt,
        ref_eng_unit.ref_mult_qt,
        evt_sched_dead.sched_dead_dt,
        evt_sched_dead.interval_qt
     FROM
        evt_sched_dead,
        mim_data_type,
        ref_eng_unit
     WHERE
        evt_sched_dead.event_db_id = cn_SchedDbId AND
        evt_sched_dead.event_id    = cn_SchedId
        AND
        mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
        mim_data_type.data_type_id    = evt_sched_dead.data_type_id
        AND
        ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
        ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
      ORDER BY
         mim_data_type.eng_unit_cd;
    lRecEvtSchedDead lCurEvtSchedDead%ROWTYPE;

BEGIN

lRepeatInterval  := '';

SELECT
   COUNT(*) INTO lRecurring
FROM
   sched_stask,
   task_task
WHERE
   sched_stask.sched_db_id = aSchedDbId AND
   sched_stask.sched_id    = aSchedId
   AND
   task_task.task_db_id          = sched_stask.task_db_id  AND
   task_task.task_id             = sched_stask.task_id     AND
   task_task.recurring_task_bool =  1;
		
IF lRecurring = 0 THEN

SELECT
   COUNT(*) INTO lRecurring
FROM
   sched_stask
WHERE
    sched_stask.sched_db_id      = aSchedDbId AND
    sched_stask.sched_id         = aSchedId   AND
    sched_stask.adhoc_recur_bool =  1;
		
   IF lRecurring = 0 THEN
      RETURN NULL;  
   END IF;
END IF;


FOR  lRecEvtSchedDead IN lCurEvtSchedDead (aSchedDbId, aSchedId ) LOOP

   IF ( ( lRecEvtSchedDead.interval_qt IS NOT NULL ) AND ( lRecEvtSchedDead.interval_qt != 0 ) ) THEN

     lUsageRemDev := lRecEvtSchedDead.usage_rem_qt + lRecEvtSchedDead.interval_qt;
        
      IF(lRecEvtSchedDead.eng_unit_cd = aEngUnitCd ) THEN
         lFirstParm := ', ' || TO_CHAR(ROUND(lUsageRemDev,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
      ELSE
         lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lUsageRemDev,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
      END IF;  
   END IF;
END LOOP;

   lRepeatInterval := lFirstParm || lRepeatInterval; 

IF lRepeatInterval IS NULL THEN
   RETURN NULL;
END IF;

RETURN substr(lRepeatInterval, 3);

END getFormatedRepeatInterval;
/