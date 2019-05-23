--liquibase formatted sql


--changeSet getTaskMaxPriorityOrd:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskMaxPriorityOrd
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return the maximum priority ord for a
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.05.03
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskMaxPriorityOrd
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER
) RETURN ref_task_priority.task_priority_ord%TYPE
IS
   lPriorityOrd ref_task_priority.task_priority_ord%TYPE;


BEGIN
   SELECT
      MAX( NVL(ref_task_priority.task_priority_ord, 0 ) ) max_priority
   INTO
      lPriorityOrd
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
      sched_stask,
      ref_task_priority
   WHERE
      sched_stask.sched_db_id = task_tree.event_db_id AND
      sched_stask.sched_id    = task_tree.event_id
      AND
      sched_stask.rstat_cd    = 0
      AND
      ref_task_priority.task_priority_db_id (+)= sched_stask.task_priority_db_id AND
      ref_task_priority.task_priority_cd    (+)= sched_stask.task_priority_cd;

   RETURN lPriorityOrd;

END getTaskMaxPriorityOrd;
/