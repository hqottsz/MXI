--liquibase formatted sql


--changeSet getFormatedRepeatInterval:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
      evt_event_rel.rel_event_db_id,
      evt_event_rel.rel_event_id,
      sched_stask.task_db_id,
      sched_stask.task_id
   FROM
      sched_stask,
      evt_event_rel
   WHERE
      sched_stask.sched_db_id = cn_SchedDbId AND
      sched_stask.sched_id    = cn_SchedId   AND
      sched_stask.rstat_cd    = 0
      AND
      evt_event_rel.event_db_id (+)= sched_stask.sched_db_id AND
      evt_event_rel.event_id    (+)= sched_stask.sched_id    AND
      evt_event_rel.rel_type_cd (+)= 'DEPT';
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

BEGIN

   lRepeatInterval  := '';


   FOR lRecSchedStask IN lCurSchedStask (aSchedDbId, aSchedId ) LOOP

      -- Check if Task is recurring
      IF (
            (
            lRecSchedStask.task_db_id IS NULL
            AND
            lRecSchedStask.adhoc_recur_bool = 1
            )
         OR
            (
            lRecSchedStask.task_db_id IS NOT NULL
            AND
            isTaskDefnRecurring(lRecSchedStask.task_db_id, lRecSchedStask.task_id) =  1
            )
      ) THEN

         -- Check whether Next Task Exists
         IF (lRecSchedStask.rel_event_db_id IS NOT NULL) THEN

            FOR  lRecEvtSchedDead IN lCurEvtSchedDead (lRecSchedStask.rel_event_db_id, lRecSchedStask.rel_event_id ) LOOP
               IF ( ( lRecEvtSchedDead.interval_qt IS NOT NULL ) AND ( lRecEvtSchedDead.interval_qt != 0 ) ) THEN
                  IF (lRecEvtSchedDead.eng_unit_cd = aEngUnitCd ) THEN
                     lFirstParm := ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                  ELSE
                     lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                  END IF;
               END IF;
            END LOOP;

         -- Check evt_sched_dead
         ELSE
             FOR  lRecEvtSchedDead IN lCurEvtSchedDead (aSchedDbId, aSchedId ) LOOP
                  IF ( ( lRecEvtSchedDead.interval_qt IS NOT NULL ) AND ( lRecEvtSchedDead.interval_qt != 0 ) ) THEN
                     IF (lRecEvtSchedDead.eng_unit_cd = aEngUnitCd ) THEN
                        lFirstParm := ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                     ELSE
                        lRepeatInterval := lRepeatInterval ||  ', ' || TO_CHAR(ROUND(lRecEvtSchedDead.interval_qt,lRecEvtSchedDead.entry_prec_qt)) || ' ' || lRecEvtSchedDead.eng_unit_cd;
                     END IF;
                  END IF;
             END LOOP;
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