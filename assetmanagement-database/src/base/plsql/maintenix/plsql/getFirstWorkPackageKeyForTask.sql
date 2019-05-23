--liquibase formatted sql


--changeSet getFirstWorkPackageKeyForTask:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getFirstWorkPackageKeyForTask
* Arguments:     aLpaDbId, aSchedDbId, aSchedId  - the primary key for the LPA task
*                aStaskMaintOpId - the LPA maintenance opportunity to check
* Description:   This function determines if there is a work package available in the
		 window available for a maintenance opportunity.  If there is, it
		 returns the task key of that opportunity
*
* Orig.Coder:    CDALEY
* Recent Date:   May 22, 2007
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getFirstWorkPackageKeyForTask
(
   aLpaDbId number,
   aSchedDbId number,
   aSchedId number,
   aStaskMaintOpId number
) RETURN STRING
IS
   ls_WP_Task_Key string(16);

BEGIN
   SELECT
      wp_task_key
   INTO
      ls_WP_Task_Key
   FROM
   ( SELECT
      FIRST_VALUE(wp_event.event_db_id || ':' || wp_event.event_id)
         OVER ( ORDER BY wp_event.event_status_cd ) AS wp_task_key,
      RANK()
         OVER (order by wp_event.event_status_cd) as rank_number
      FROM
         lpa_stask_maint_op,
         -- task tables
         sched_stask,
         evt_inv task_inv,
         inv_inv,
         -- wp tables
         sched_stask sched_wp,
         evt_event wp_event,
         evt_inv wp_inv,
         evt_loc wp_loc
      WHERE
         -- find the maintenance opportunity
         lpa_stask_maint_op.lpa_db_id         = aLpaDbId AND
         lpa_stask_maint_op.sched_db_id       = aSchedDbId AND
         lpa_stask_maint_op.sched_id          = aSchedId AND
         lpa_stask_maint_op.stask_maint_op_id = aStaskMaintOpId
         AND
         -- find the task
         sched_stask.sched_db_id = lpa_stask_maint_op.sched_db_id AND
         sched_stask.sched_id    = lpa_stask_maint_op.sched_id	  AND
         sched_stask.rstat_cd	 = 0
         AND
         -- find the inventory for the task
         task_inv.event_db_id = sched_stask.sched_db_id AND
         task_inv.event_id    = sched_stask.sched_id
         AND
         task_inv.main_inv_bool = 1
         AND
         inv_inv.inv_no_db_id = task_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = task_inv.inv_no_id
         AND
         inv_inv.rstat_cd = 0
         AND
         -- find a check
         sched_wp.task_class_db_id = 0 AND
         sched_wp.task_class_cd    = 'CHECK'
         AND
         -- not a heavy check
         sched_wp.heavy_bool = 0	AND
         sched_wp.rstat_cd   = 0
         AND
         -- not historical
         wp_event.event_db_id = sched_wp.sched_db_id AND
         wp_event.event_id    = sched_wp.sched_id
         AND
         wp_event.hist_bool = 0
         AND
         -- root inventory of the task's inventory is the aircraft for the check
         wp_inv.event_db_id = wp_event.event_db_id AND
         wp_inv.event_id    = wp_event.event_id
         AND
         wp_inv.main_inv_bool = 1
         AND
         wp_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
         wp_inv.inv_no_id    = inv_inv.h_inv_no_id
         AND
         -- location is the same as the maintenance opportunity
         wp_loc.event_db_id = wp_event.event_db_id AND
         wp_loc.event_id    = wp_event.event_id
         AND
         wp_loc.loc_db_id = lpa_stask_maint_op.loc_db_id AND
         wp_loc.loc_id    = lpa_stask_maint_op.loc_id
         AND
         -- the status must be plan or in work
         wp_event.event_status_db_id = 0 AND
         (
            (
               wp_event.event_status_cd = 'ACTV'
               AND
               wp_event.sched_start_gdt IS NOT NULL
               AND
               wp_event.sched_end_gdt IS NOT NULL
            )
            OR
            (
               wp_event.event_status_cd = 'IN WORK'
               AND
               -- the task or its parents are not forecasted
               NOT EXISTS
               (
                  SELECT
                     1
                  FROM
                     evt_event
                  WHERE
                     evt_event.event_status_cd = 'FORECAST'
                  START WITH
                     evt_event.event_db_id = aSchedDbId AND
                     evt_event.event_id    = aSchedId
                  CONNECT BY
                     evt_event.event_db_id = PRIOR evt_event.nh_event_db_id AND
                     evt_event.event_id    = PRIOR evt_event.nh_event_id
               )
            )
         )
         AND
         -- it must be scheduled during the mainteniance opportunity's time window
         lpa_stask_maint_op.opp_start_dt <= DECODE(wp_event.actual_start_dt, NULL, wp_event.sched_start_dt, wp_event.actual_start_dt) AND
         lpa_stask_maint_op.opp_end_dt >= DECODE(wp_event.actual_start_dt, NULL, wp_event.sched_start_dt, wp_event.actual_start_dt)
   )
   WHERE
      rownum <= 1;

   RETURN ls_WP_Task_Key;

END getFirstWorkPackageKeyForTask;
/