--liquibase formatted sql


--changeSet MX-19501:1 stripComments:false
-- unsuppress tasks that are suppressed by a task in a different root
UPDATE sched_stask t 
SET    t.dup_jic_sched_db_id = NULL, 
       t.dup_jic_sched_id    = NULL 
WHERE  t.dup_jic_sched_db_id IS NOT NULL 
       AND
       (t.sched_db_id, t.sched_id )
       IN (   SELECT  s.sched_db_id, 
                      s.sched_id
              FROM    sched_stask s,
                      sched_stask a,
                      evt_event se, 
                      evt_event sa
              WHERE   a.sched_db_id = s.dup_jic_sched_db_id AND
                      a.sched_id    = s.dup_jic_sched_id
                      AND
                      -- neither the active or the suppressed task are historic
                      sa.event_db_id = a.sched_db_id AND
                      sa.event_id    = a.sched_id    AND
                      sa.hist_bool   = 0             AND
                      sa.rstat_cd    = 0
                      AND
                      se.event_db_id = s.sched_db_id AND
                      se.event_id    = s.sched_id    AND
                      se.hist_bool   = 0             AND
                      se.rstat_cd    = 0
                      AND NOT
                      -- and they do not share the same root
                      ( se.h_event_db_id = sa.h_event_db_id AND
                        se.h_event_id    = sa.h_event_id )
            );