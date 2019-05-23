--liquibase formatted sql


--changeSet getTaskMaxSchedPriority:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskMaxSchedPriority
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return the maximum sched priority for a
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.05.03
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskMaxSchedPriority
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER
) RETURN VARCHAR
IS
   lPriority VARCHAR(20);

   CURSOR lCurPriority IS
      SELECT
         MAX( NVL(ref_sched_priority.priority_ord, 0 ) ) max_priority,
         ref_sched_priority.sched_priority_db_id || ':' || ref_sched_priority.sched_priority_cd AS sched_priority_key
      FROM
         (  SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id    = PRIOR evt_event.event_id
         ) task_tree,
         evt_event,
         ref_sched_priority
      WHERE
         evt_event.event_db_id = task_tree.event_db_id AND
         evt_event.event_id    = task_tree.event_id
         AND
         ref_sched_priority.sched_priority_db_id = evt_event.sched_priority_db_id AND
         ref_sched_priority.sched_priority_cd    = evt_event.sched_priority_cd
      GROUP BY
         ref_sched_priority.sched_priority_db_id,
         ref_sched_priority.sched_priority_cd
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
        lPriority := lRecPriority.sched_priority_key;
      END IF;

   CLOSE lCurPriority;

   RETURN lPriority;

END getTaskMaxSchedPriority;
/