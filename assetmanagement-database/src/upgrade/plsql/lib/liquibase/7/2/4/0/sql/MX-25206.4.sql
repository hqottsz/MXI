--liquibase formatted sql


--changeSet MX-25206.4:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      bsyncValidateActuals
* Arguments:     aInvNoDbId, aInvNoId - the primary key for the inventory
*                aTaskDbId, aTaskId  - the primary key for the task definition
*                aSchedDbId, aSchedId - the parent task
* Description:   This function determines if the task definition should be
*                initialized on the inventory.
* Returns:       0 if the task definition should be initialized
*                1 if the task definition should not be initialized
*
* Orig.Coder:    jcimino
* Recent Date:   November 3, 2008
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
   CURSOR
      lActuals
      (
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
         (
            SELECT
               *
            FROM
               inv_inv 
            WHERE
               inv_inv.inv_no_db_id = aInvNoDbId AND
               inv_inv.inv_no_id    = aInvNoId	AND
               inv_inv.rstat_cd	    = 0
         ) inv_inv,
         (
            SELECT
               *
            FROM
               task_task
            WHERE
               task_task.task_db_id = aTaskDbId AND
               task_task.task_id    = aTaskId
         ) task_task,
         task_task rev_task_task,
         sched_stask,
         evt_event,
         evt_inv,
         inv_inv stask_inv
      WHERE
         -- Get all revisions of the task definition
         rev_task_task.task_defn_db_id = task_task.task_defn_db_id AND
         rev_task_task.task_defn_id    = task_task.task_defn_id   
         AND
         -- Get all actuals based on any revision of the task definition
         sched_stask.task_db_id = rev_task_task.task_db_id AND
         sched_stask.task_id    = rev_task_task.task_id
         AND
         -- Get the inventory to which the task belongs
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
         AND
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
         AND
         evt_event.rstat_cd = 0 AND
         evt_inv.main_inv_bool = 1
         AND
         stask_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
         stask_inv.inv_no_id    = evt_inv.inv_no_id
         AND
         -- Limit the tasks to the same inventory and inventory tree
         inv_inv.h_inv_no_db_id = stask_inv.h_inv_no_db_id AND
         inv_inv.h_inv_no_id    = stask_inv.h_inv_no_id
         AND
         inv_inv.inv_no_db_id = stask_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = stask_inv.inv_no_id
         -- If the parent task is not provided, return the actual
         -- Otherwise, only return the actual if it's under the same parent
         AND
         (
            aSchedDbId IS NULL
            OR
            (
               evt_event.nh_event_db_id = aSchedDbId AND
               evt_event.nh_event_id    = aSchedId
            )
         );

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
                  INNER JOIN vw_h_baseline_task ON
                     vw_h_baseline_task.task_db_id = pre_task_task.task_db_id AND
                     vw_h_baseline_task.task_id    = pre_task_task.task_id
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
            -- Check if the task definition has a COMPLIES link to a historic actual within the inventory tree
            EXISTS
            (
               SELECT
                  1
               FROM
                  task_task_dep,
                  task_task   dep_task_task,
                  sched_stask dep_sched_stask,
                  evt_event   dep_evt_event,
                  evt_inv     dep_evt_inv,
                  task_defn   dep_defn,
                  task_task   dep_latest_task_task
               WHERE
                  -- Get the complies dependents that are not obsolete
                  task_task_dep.task_db_id            = task_task.task_db_id AND
                  task_task_dep.task_id               = task_task.task_id    AND
                  task_task_dep.task_dep_action_db_id = 0                    AND
                  task_task_dep.task_dep_action_cd    = 'COMPLIES'
                  AND
                  dep_task_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
                  dep_task_task.task_defn_id    = task_task_dep.dep_task_defn_id
                  AND
                  dep_defn.task_defn_db_id = dep_task_task.task_defn_db_id AND
                  dep_defn.task_defn_id    = dep_task_task.task_defn_id    
                  AND
                  dep_latest_task_task.task_defn_db_id    = dep_defn.task_defn_db_id   AND
                  dep_latest_task_task.task_defn_id       = dep_defn.task_defn_id      AND
                  dep_latest_task_task.revision_ord       = dep_defn.last_revision_ord AND
                  dep_latest_task_task.task_def_status_cd <> 'OBSOLETE'
                  AND
                  -- Get all actuals of the dependent
                  dep_sched_stask.task_db_id = dep_task_task.task_db_id AND
                  dep_sched_stask.task_id    = dep_task_task.task_id	AND
         	        dep_sched_stask.rstat_cd	 = 0
                  AND
                  dep_evt_event.event_db_id = dep_sched_stask.sched_db_id AND
                  dep_evt_event.event_id    = dep_sched_stask.sched_id    AND
                  dep_evt_event.event_status_cd <> 'ERROR'
                  AND
                  -- Limit to historic actuals
                  dep_evt_event.hist_bool = 1
                  AND
                  -- Limit to actuals on the same inventory tree
                  dep_evt_inv.event_db_id = dep_evt_event.event_db_id AND
                  dep_evt_inv.event_id    = dep_evt_event.event_id
                  AND
                  dep_evt_inv.main_inv_bool = 1
                  AND
                  dep_evt_inv.h_inv_no_db_id = inv_inv.h_inv_no_db_id AND
                  dep_evt_inv.h_inv_no_id    = inv_inv.h_inv_no_id
            )
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

   -- Make sure it's not a duplicate task
   SELECT
      DECODE(count(*), 0, 1, 0)
   INTO
      lDuplicateTask
   FROM
      TABLE(hasACTVDependentTask(aTaskDbId, aTaskId, aInvNoDbId, aInvNoId, -1, -1, NULL))
   WHERE                                
      ROWNUM = 1; -- added for performance

   IF lDuplicateTask = 1 THEN
      RETURN 1;
   END IF;

   -- Loop through the actuals
   FOR lNextActual IN lActuals ( aInvNoDbId, aInvNoId, aTaskDbId, aTaskId, aSchedDbId, aSchedId ) LOOP

      -- If an actual has been terminated, do not initialize the task
      IF lNextActual.event_status_cd = 'TERMINATE' THEN
         RETURN 1;
      END IF;

      -- If there is a non-historic actual (not forecast tasks), do not initialize the task
      IF lNextActual.hist_bool = 0 AND lNextActual.event_status_cd <> 'FORECAST' THEN
         RETURN 1;
      END IF;

      -- If there is a COMPLETE actual and the task definition is not recurring, do not initialize the task
      IF lNextActual.event_status_cd = 'COMPLETE' AND lNextActual.recurring_bool = 0 THEN
         RETURN 1;
      END IF;

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

   -- If you got this far, return 0 which means the task should be initialized
   RETURN 0;
END bsyncValidateActuals;
/