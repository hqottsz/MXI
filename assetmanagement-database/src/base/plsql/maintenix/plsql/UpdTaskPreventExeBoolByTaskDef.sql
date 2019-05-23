--liquibase formatted sql


--changeSet UpdTaskPreventExeBoolByTaskDef:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE UpdTaskPreventExeBoolByTaskDef(aTaskDbId  NUMBER,
                                                         aTaskId    NUMBER,
                                                         aPreventExeBool NUMBER,
                                                         aReviewDt DATE) IS

BEGIN

    UPDATE
         sched_stask_flags
      SET
         prevent_exe_bool 			= aPreventExeBool,
         prevent_exe_review_dt 	= aReviewDt
      WHERE
         -- make sure that already prevented actual task does not get re-prevented or
         -- already allowed actual task does not get re-allowed.
         prevent_exe_bool != aPreventExeBool
         AND
         ( sched_db_id, sched_id )
         IN
         (
            SELECT
               sched_stask.sched_db_id,
               sched_stask.sched_id
            FROM
               sched_stask
               INNER JOIN evt_event ON
                  evt_event.event_db_id 	= sched_stask.sched_db_id AND
                  evt_event.event_id		= sched_stask.sched_id
               INNER JOIN evt_event check_event ON
                  check_event.event_db_id	= evt_event.h_event_db_id AND
                  check_event.event_id		= evt_event.h_event_id
            WHERE
               sched_stask.task_db_id = aTaskDbId AND
               sched_stask.task_id		= aTaskId
               AND
               evt_event.event_status_db_id = 0 AND
               evt_event.event_status_cd  IN ( 'ACTV','CANCEL','TERMINATE', 'FORECAST' )
               AND
               (
                  -- when prevent a task from execution, make sure to exclude tasks that are currently assigned to commit or in work work package
                  (
                     aPreventExeBool = 1
                     AND
                     check_event.event_status_cd NOT IN ( 'COMMIT', 'IN WORK' )
                  )
                  OR
                  aPreventExeBool = 0
               )
         );


END;
/