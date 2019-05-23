--liquibase formatted sql


--changeSet getTaskMaxPriority:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskMaxPriority
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return the maximum priority for a
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.05.03
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskMaxPriority
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER
) RETURN VARCHAR
IS
   lPriority VARCHAR(20);

   CURSOR lCurPriority IS
      SELECT
         MAX( NVL(ref_task_priority.task_priority_ord, 0 ) ) max_priority,
         ref_task_priority.task_priority_db_id || ':' || ref_task_priority.task_priority_cd AS priority_key
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
         sched_stask.rstat_cd	= 0
         AND
         ref_task_priority.task_priority_db_id = sched_stask.task_priority_db_id AND
         ref_task_priority.task_priority_cd    = sched_stask.task_priority_cd
      GROUP BY
         ref_task_priority.task_priority_db_id,
         ref_task_priority.task_priority_cd
      ORDER BY
         max_priority DESC;

   lRecPriority lCurPriority%ROWTYPE;

BEGIN

   /* Get the Max Priority */
   OPEN lCurPriority;
   FETCH lCurPriority INTO lRecPriority;

      /* If no Priority found, return null */
      IF NOT lCurPriority%FOUND THEN
         CLOSE lCurPriority;
         RETURN NULL;
      /* Otherwise, retain it */
      ELSE
        lPriority := lRecPriority.priority_key;
      END IF;

   CLOSE lCurPriority;

   RETURN lPriority;

END getTaskMaxPriority;
/