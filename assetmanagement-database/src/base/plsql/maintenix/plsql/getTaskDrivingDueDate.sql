--liquibase formatted sql


--changeSet getTaskDrivingDueDate:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskDrivingDueDate
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return the earliest due date for the
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.09
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskDrivingDueDate
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER,
   aCurrentDate   DATE
) RETURN DATE
IS
   lDueDate DATE;

   CURSOR lCurDeadline IS
      SELECT
         evt_event.event_db_id AS event_db_id,
         evt_event.event_id AS event_id,
         evt_sched_dead.sched_dead_dt AS sched_dead_dt,
         DECODE(
            task_task.last_sched_dead_bool,
            NULL, (evt_sched_dead.sched_dead_dt - aCurrentDate),
            DECODE( task_task.last_sched_dead_bool, 0,
               (evt_sched_dead.sched_dead_dt - aCurrentDate),
               -1 * (evt_sched_dead.sched_dead_dt - aCurrentDate)
           )
         ) AS sort_column
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
         evt_event,
         evt_sched_dead,
         sched_stask,
         task_task
      WHERE
         evt_event.event_db_id = task_tree.event_db_id AND
         evt_event.event_id    = task_tree.event_id
         AND
         evt_event.hist_bool = 0    AND
         evt_event.rstat_cd  = 0
         and
         sched_stask.sched_db_id = evt_event.h_event_db_id and
         sched_stask.sched_id    = evt_event.h_event_id
         and
         task_task.task_db_id (+)= sched_stask.task_db_id and
         task_task.task_id    (+)= sched_stask.task_id
         AND
         evt_sched_dead.event_db_id       = evt_event.event_db_id AND
         evt_sched_dead.event_id          = evt_event.event_id AND
         evt_sched_dead.sched_driver_bool = 1
      ORDER BY
         sort_column ASC;

      lRecDeadline lCurDeadline%ROWTYPE;

BEGIN

   /* Get the driving deadline for the specified task tree, sorted by predicted days remaining */
   OPEN lCurDeadline;
   FETCH lCurDeadline INTO lRecDeadline;

      /* If no driving deadline, return null */
      IF NOT lCurDeadline%FOUND THEN
         CLOSE lCurDeadline;
         RETURN NULL;
      /* Otherwise, if a driving deadline is found, retain it */
      ELSE
        lDueDate := lRecDeadline.sched_dead_dt;
      END IF;

   CLOSE lCurDeadline;

   RETURN lDueDate;

END getTaskDrivingDueDate;
/