--liquibase formatted sql


--changeSet bsyncValidateActuals:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      bsyncValidateActuals
* Arguments:     aInvNoDbId, aInvNoId - the primary key for the inventory
*                aTaskDbId, aTaskId   - the primary key for the task definition
*                aSchedDbId, aSchedId - the parent task
* Description:   This function determines if the task definition should be
*                initialized on the inventory.
*                NOTE: this function relays on data in gtt_h_baseline_task to be present
*                and up to date. See com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView
*                for more information
*
* Returns:       0 if the task definition should be initialized
*                1 if the task definition should not be initialized
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION bsyncValidateActuals
(
   aInvNoDbId   inv_inv.inv_no_db_id%TYPE,
   aInvNoId     inv_inv.inv_no_id%TYPE,
   aTaskDbId    task_task.task_db_id%TYPE,
   aTaskId      task_task.task_id%TYPE,
   aClassModeCd ref_task_class.class_mode_cd%TYPE,
   aSchedDbId   sched_stask.sched_db_id%TYPE,
   aSchedId     sched_stask.sched_id%TYPE
) RETURN NUMBER
IS 
   
   -- This cursor retrieves the actual tasks based on the task definition on the inventory and under the parent task
   CURSOR lActuals (
         aInvNoDbId inv_inv.inv_no_db_id%TYPE,
         aInvNoId   inv_inv.inv_no_id%TYPE,
         aTaskDbId  task_task.task_db_id%TYPE,
         aTaskId    task_task.task_id%TYPE,
         aSchedDbId sched_stask.sched_db_id%TYPE,
         aSchedId   sched_stask.sched_id%TYPE
      )
   IS
      SELECT
         evt_event.hist_bool,
         evt_event.event_status_cd,
         isTaskDefnRecurring( task_task.task_db_id, task_task.task_id ) AS recurring_bool,
         task_task.unique_bool
      FROM
         task_task
         -- Get all revisions of the task definition
         INNER JOIN task_task rev_task_task ON
            rev_task_task.task_defn_db_id = task_task.task_defn_db_id AND
            rev_task_task.task_defn_id    = task_task.task_defn_id
         -- Get all actuals based on any revision of the task definition on the same inventory
         INNER JOIN sched_stask ON
            sched_stask.task_db_id = rev_task_task.task_db_id AND
            sched_stask.task_id    = rev_task_task.task_id
            AND
            sched_stask.main_inv_no_db_id = aInvNoDbId AND
            sched_stask.main_inv_no_id    = aInvNoId
            AND
            sched_stask.orphan_frct_bool = 0
         INNER JOIN evt_event ON
            evt_event.event_db_id = sched_stask.sched_db_id AND
            evt_event.event_id    = sched_stask.sched_id
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         -- If the parent task is not provided, return the actual
         -- Otherwise, only return the actual if it's under the same parent
         (
            aSchedDbId IS NULL
            OR
            (
               evt_event.nh_event_db_id = aSchedDbId AND
               evt_event.nh_event_id    = aSchedId
            )
         )
         AND
         (
            -- where the status does not make sense for task initialization
            ( evt_event.event_status_db_id = 0 AND
              evt_event.event_status_cd IN ('ACTV','BLKOUT','COMMIT','FORECAST','IN WORK','N/A','TERMINATE','PAUSE','INSPREQ')
            )
            OR -- a non-recurring task which has already been completed, check this second because the function call is expensive
            ( evt_event.event_status_db_id = 0 AND
              evt_event.event_status_cd = 'COMPLETE' AND
              isTaskDefnRecurring( task_task.task_db_id, task_task.task_id ) = 0
            )
        )
        -- Any rows returned indicates task actuals existing
        AND ROWNUM <=1;

   -- This cursor retrieves the number of dependencies for the task definition and inventory combination
   CURSOR
      lDependencies
      (
         aInvNoDbId inv_inv.inv_no_db_id%TYPE,
         aInvNoId   inv_inv.inv_no_id%TYPE,
         aTaskDbId  task_task.task_db_id%TYPE,
         aTaskId    task_task.task_id%TYPE
      )
   IS
      SELECT
         COUNT(*) AS row_count
      FROM
         inv_inv,
         task_task
      WHERE
         inv_inv.inv_no_db_id = aInvNoDbId AND
         inv_inv.inv_no_id    = aInvNoId   AND
         inv_inv.rstat_cd     = 0
         AND
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         (
            -- Check if task definition is a dependent CRT link
            EXISTS
            (
               SELECT
                  1
               FROM
                  task_task_dep
               INNER JOIN task_task pre_task_task ON
                     pre_task_task.task_db_id = task_task_dep.task_db_id AND
                     pre_task_task.task_id    = task_task_dep.task_id
               INNER JOIN gtt_h_baseline_task ON
                     gtt_h_baseline_task.task_db_id = pre_task_task.task_db_id AND
                     gtt_h_baseline_task.task_id    = pre_task_task.task_id
               WHERE
                  task_task_dep.dep_task_defn_db_id = task_task.task_defn_db_id AND
                  task_task_dep.dep_task_defn_id    = task_task.task_defn_id
                  AND
                  task_task_dep.task_dep_action_db_id = 0 AND
                  task_task_dep.task_dep_action_cd    = 'CRT'
                  AND
                  -- Do not filter for self-recurring task definitions
                  NOT
                  (
                     pre_task_task.task_defn_db_id = task_task.task_defn_db_id AND
                     pre_task_task.task_defn_id    = task_task.task_defn_id
                  )
                  -- Do not filter for recurring block chain links
                  AND
                  (
                     pre_task_task.block_chain_sdesc IS NULL
                     OR
                     pre_task_task.block_chain_sdesc != task_task.block_chain_sdesc
                  )
            )

            OR
            -- Check if task definition is a source POSTCRT link
            EXISTS
            (
               SELECT
                  1
               FROM
                  task_task_dep
               WHERE
                  task_task_dep.task_db_id = task_task.task_db_id AND
                  task_task_dep.task_id    = task_task.task_id
                  AND
                  task_task_dep.task_dep_action_db_id = 0 AND
                  task_task_dep.task_dep_action_cd    = 'POSTCRT'
            )
            OR
            hasACTVOrNoComplianceRefDoc(
               aTaskDbId, aTaskId,
               aInvNoDbId, aInvNoId
            ) = 0
         );

   -- other local variables
   lEventStatusCd ref_event_status.event_status_cd%TYPE;
   lDuplicateTask NUMBER;
   lDependenciesCount lDependencies%ROWTYPE;
BEGIN
  
   
   -- Make sure the parent is not assigned to an IN WORK or COMMIT work package
   IF aSchedDbId IS NOT NULL THEN
      SELECT
         h_evt_event.event_status_cd
      INTO
         lEventStatusCd
      FROM
         evt_event,
         evt_event h_evt_event
      WHERE
         evt_event.event_db_id = aSchedDbId AND
         evt_event.event_id    = aSchedId AND
         evt_event.rstat_cd = 0
         AND
         h_evt_event.event_db_id = evt_event.h_event_db_id AND
         h_evt_event.event_id    = evt_event.h_event_id;

      IF lEventStatusCd = 'COMMIT' OR lEventStatusCd = 'IN WORK' THEN
         RETURN 1;
      END IF;
   END IF;
   
   -- Loop through the actuals
   FOR lResults IN lActuals ( aInvNoDbId, aInvNoId, aTaskDbId, aTaskId, aSchedDbId, aSchedId ) LOOP
         RETURN 1;
   END LOOP;

   -- For non-JIC tasks definitions, check if there are other dependencies that prevent initialization
   IF aClassModeCd <> 'JIC' THEN
      OPEN lDependencies ( aInvNoDbId, aInvNoId, aTaskDbId, aTaskId );
      FETCH lDependencies INTO lDependenciesCount;
      CLOSE lDependencies;
      IF lDependenciesCount.row_count <> 0 THEN
         RETURN 1;
      END IF;
   END IF;

   -- Make sure it's not a duplicate task
   SELECT
      DECODE(count(*), 0, 1, 0)
   INTO
      lDuplicateTask
   FROM
      TABLE(hasACTVDependentTask(aTaskDbId, aTaskId, aInvNoDbId, aInvNoId, -1, -1, NULL,0))
   WHERE
      ROWNUM = 1; -- added for performance

   IF lDuplicateTask = 1 THEN
      RETURN 1;
   END IF;

   -- If you got this far, return 0 which means the task should be initialized
   RETURN 0;
END bsyncValidateActuals;
/