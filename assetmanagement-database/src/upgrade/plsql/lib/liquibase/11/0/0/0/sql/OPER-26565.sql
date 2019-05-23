--liquibase formatted sql


--changeSet OPER-26565:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- Remove the old trigger
   upg_migr_schema_v1_pkg.trigger_drop('TIUAR_EVT_EVENT_HIST_BOOL');

END;
/

--changeSet OPER-26565:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create a new update only trigger
-- Triggers TIBR_SCHED_STASK_HIST_BOOL and TIBR_EVT_SCHED_DEAD_HIST_BOOL will handle insert
CREATE OR REPLACE TRIGGER "TUAR_EVT_EVENT_HIST_BOOL" AFTER UPDATE OF "HIST_BOOL"
   ON "EVT_EVENT" FOR EACH ROW
BEGIN
   -- run trigger logic for tasks only
   IF :new.event_type_db_id = 0 AND :new.event_type_cd = 'TS' THEN
      UPDATE
         sched_stask
      SET
         sched_stask.hist_bool_ro = :new.hist_bool
      WHERE
         sched_stask.sched_db_id = :new.event_db_id AND
         sched_stask.sched_id    = :new.event_id;

      UPDATE
         evt_sched_dead
      SET
         evt_sched_dead.hist_bool_ro = :new.hist_bool
      WHERE
         evt_sched_dead.event_db_id = :new.event_db_id AND
         evt_sched_dead.event_id    = :new.event_id;
   END IF;
END;
/