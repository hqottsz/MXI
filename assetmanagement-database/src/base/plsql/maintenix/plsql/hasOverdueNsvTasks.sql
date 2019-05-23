--liquibase formatted sql


--changeSet hasOverdueNsvTasks:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     hasOverdueNsvTasks
* Arguments:    aInvNoDbId, aInvNoId - pk for the root inventory
* Description:  This function will search for overdue next shop visit tasks 
*               against the provided inventory's tree.
* Returns 1 if there are overdue next shop visit tasks, 0 if there are none
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION hasOverdueNsvTasks
(
   aInvNoDbId inv_inv.h_inv_no_db_id%TYPE,
   aInvNoId   inv_inv.h_inv_no_id%TYPE
) RETURN NUMBER

IS
   lHasOverdueNsvTasks NUMBER;

BEGIN

   lHasOverdueNsvTasks := 0;

   BEGIN
      SELECT
         1
      INTO
         lHasOverdueNsvTasks
      FROM
         inv_inv
         INNER JOIN sched_stask ON
            sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
            sched_stask.main_inv_no_id    = inv_inv.inv_no_id
         INNER JOIN task_task_flags ON
            task_task_flags.task_db_id = sched_stask.task_db_id AND
            task_task_flags.task_id    = sched_stask.task_id
         INNER JOIN vw_drv_deadline ON
            vw_drv_deadline.event_db_id = sched_stask.sched_db_id AND
            vw_drv_deadline.event_id    = sched_stask.sched_id
      WHERE
         inv_inv.h_inv_no_db_id = aInvNoDbId AND
         inv_inv.h_inv_no_id    = aInvNoId
         AND
         sched_stask.orphan_frct_bool = 0 AND
         sched_stask.hist_bool_ro = 0
         AND
         task_task_flags.nsv_bool = 1
         AND
         (
            -- check if overdue
            -- (note; cannot check evt_event.sched_priority_cd = 'O/D'
            --        as overdue tasks with soft deadlines are marked HIGH and not O/D)
            (
               -- calendar based tasks that are overdue
               -- (using deadline date)
               vw_drv_deadline.domain_type_db_id = 0 AND
               vw_drv_deadline.domain_type_cd    = 'CA'
               AND
               getExtendedDeadlineDt(
                  vw_drv_deadline.deviation_qt,
                  vw_drv_deadline.sched_dead_dt,
                  vw_drv_deadline.domain_type_cd,
                  vw_drv_deadline.data_type_cd,
                  vw_drv_deadline.ref_mult_qt
               ) <= SYSDATE
            )
            OR
            (
               -- usage based tasks that are overdue
               -- (using usage remaining because inv may not be installed,
               --  thus may not have deadline date)
               vw_drv_deadline.domain_type_db_id = 0 AND
               vw_drv_deadline.domain_type_cd    = 'US'
               AND
               (-1)*vw_drv_deadline.usage_rem_qt >= vw_drv_deadline.deviation_qt
            )
         )
         AND
         ROWNUM = 1
      ;
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         -- Not really an exception; no tasks are overdue.
         lHasOverdueNsvTasks := 0;
   END;

   RETURN lHasOverdueNsvTasks;

END hasOverdueNsvTasks;
/