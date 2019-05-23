--liquibase formatted sql


--changeSet getTaskDrivingDuration:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskDrivingDuration
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return the longest estimated duration for the
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.09
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskDrivingDuration
(
   aSchedDbId number,
   aSchedId number
) RETURN FLOAT
IS
   lDuration FLOAT;
BEGIN

   SELECT
      MAX( sched_stask.est_duration_qt ) max_duration
   INTO
      lDuration
   FROM
      (  SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE rstat_cd = 0
         START WITH
            evt_event.event_db_id = aSchedDbId AND
            evt_event.event_id    = aSchedId
         CONNECT BY
            evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
            evt_event.nh_event_id    = PRIOR evt_event.event_id
      ) task_tree,
      sched_stask
   WHERE
      sched_stask.sched_db_id = task_tree.event_db_id AND
      sched_stask.sched_id    = task_tree.event_id
      AND
      sched_stask.rstat_cd    = 0;

   RETURN NVL( lDuration, 0 );

END getTaskDrivingDuration;
/