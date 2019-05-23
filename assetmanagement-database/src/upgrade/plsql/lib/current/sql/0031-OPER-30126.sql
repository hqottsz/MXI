--liquibase formatted sql

--changeSet OPER-30126:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment We may be arriving here from a previous version which contained a parallel implementation of this issue. Start by cleaning that up
BEGIN
   -- the gtt prefixed function is no loner required as the primary function will be replaced in this issue with the gtt logic
   utl_migr_schema_pkg.function_drop('gttBsyncValidateActuals');
   -- the gtt prefixed function is no longer required as the primary function will be replaced in this issue with the gtt logic
   utl_migr_schema_pkg.function_drop('gttHasACTVDependentTask');
     
   -- the global temporary table may exists in a state where it has two additional columns which are not required
   utl_migr_schema_pkg.table_column_drop('GTT_H_BASELINE_TASK','TASK_DEFN_DB_ID');
   utl_migr_schema_pkg.table_column_drop('GTT_H_BASELINE_TASK','TASK_DEFN_ID');
END;
/

--changeSet OPER-30126:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment This view is no longer referenced by core logic
BEGIN
   utl_migr_schema_pkg.view_drop('VW_H_BASELINE_TASK');
END;
/

--changeSet OPER-30126:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment If it does not yet exist, create the global temporary table
BEGIN
   utl_migr_schema_pkg.table_create('
   CREATE GLOBAL TEMPORARY TABLE GTT_H_BASELINE_TASK
   (
     TASK_DB_ID      NUMBER(10,0),
     TASK_ID         NUMBER(10,0)
   )
   ON COMMIT DELETE ROWS
   ');
EXCEPTION WHEN OTHERS THEN
    -- ORA-00955: name is already used by an existing object
    -- Could not be caught because utl_migr throws the same exception for every error
    IF SQLCODE = -20001 THEN
       NULL;
    ELSE
       RAISE;
    END IF;
END;
/

--changeSet OPER-30126:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
   CREATE INDEX "IX_GTTHBASELINETASK_TASK" ON "GTT_H_BASELINE_TASK" ("TASK_DB_ID", "TASK_ID")
   ');
END;
/

--changeSet OPER-30126:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     hasACTVDependentTask
* Arguments:    aTaskDbId, aTaskId 	    - pk for the task definition revision
*               aInvNoDbId, aInvNoId    - pk for the main inventory
* Description:  This function will determine if there is an ACTV dependant not
*               on-condition CRT task on a task definition or the task definition
*               has an ACTV task itself.
*               NOTE: this function relays on data in gtt_h_baseline_task to be present
*               and up to date. See com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView
*               for more information
*
* Returns 1 if there are ACTV dependant tasks, 0 if there are none
*
* Orig.Coder:    Cdaley
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION hasACTVDependentTask
(
   aTaskDbId task_task.task_db_id%TYPE,
   aTaskId   task_task.task_id%TYPE,

   aInvNoDbId inv_inv.inv_no_db_id%TYPE,
   aInvNoId   inv_inv.inv_no_id%TYPE,

   aPrevTaskDbId task_task.task_db_id%TYPE,
   aPrevTaskId   task_task.task_id%TYPE,

   aTable mxKeyTable,

   aBackwardsBool NUMBER := 0

) RETURN mxKeyTable IS

   lTaskDefnHasActvDependentTasks NUMBER;
   lTable mxKeyTable;
   lIsEvaluated NUMBER;
   lIndex NUMBER;

   -- returns previous and next dependent task definitions (CRT and POSTCRT links)
   CURSOR lDeptTask IS
      -- Get previous CRT dependent task definitions
      SELECT  /*+ FIRST_ROWS (100)  */
         task_task_dep.task_db_id AS dep_task_db_id,
         task_task_dep.task_id AS dep_task_id,
         1 AS previous_bool
      FROM
         task_task
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
         -- Get prev dependent tasks
         INNER JOIN task_task_dep ON
            task_task_dep.dep_task_defn_db_id = task_task.task_defn_db_id AND
            task_task_dep.dep_task_defn_id    = task_task.task_defn_id
         -- Get latest approved task revision
         INNER JOIN gtt_h_baseline_task ON
            gtt_h_baseline_task.task_db_id = task_task_dep.task_db_id AND
            gtt_h_baseline_task.task_id    = task_task_dep.task_id
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         -- only unique task definitions
         task_task.unique_bool = 1
         AND
         -- only not on-condition task definitions
         task_task.on_condition_bool = 0
         AND
         -- filter out JICs
         ref_task_class.class_mode_cd != 'JIC'
         AND
         task_task_dep.task_dep_action_db_id = 0 AND
         task_task_dep.task_dep_action_cd    = 'CRT'
         -- exclude the calling task definition since this will be called recursively
         AND NOT
         (
            task_task_dep.task_db_id = aPrevTaskDbId AND
            task_task_dep.task_id    = aPrevTaskId
         )

      UNION ALL

      -- Get previous POSTCRT dependent task definitions
      SELECT
         r_task_task.task_db_id AS dep_task_db_id,
         r_task_task.task_id AS dep_task_id,
         0 AS previous_bool
      FROM
         task_task
         INNER JOIN task_task_dep ON
            task_task_dep.task_db_id = task_task.task_db_id AND
            task_task_dep.task_id    = task_task.task_id
         -- return latest revision of next dependent task definition
         INNER JOIN task_task r_task_task ON
            r_task_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
            r_task_task.task_defn_id    = task_task_dep.dep_task_defn_id
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
         -- Get latest approved task revision
         INNER JOIN gtt_h_baseline_task ON
            gtt_h_baseline_task.task_db_id = r_task_task.task_db_id AND
            gtt_h_baseline_task.task_id    = r_task_task.task_id
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         -- only unique task definitions
         task_task.unique_bool = 1
         AND
         -- only not on-condition task definitions
         task_task.on_condition_bool = 0
         AND
         -- filter out JIC class modes
         ref_task_class.class_mode_cd != 'JIC'
         AND
         -- get next dependent tasks
         task_task_dep.task_dep_action_db_id = 0 AND
         task_task_dep.task_dep_action_cd    = 'POSTCRT'
         -- exclude the calling task definition since this will be called recursively
         AND NOT
         (
            r_task_task.task_db_id = aPrevTaskDbId AND
            r_task_task.task_id    = aPrevTaskId
         )

      UNION ALL

      -- Return itself if it is not JIC mode and is unique
      SELECT
         task_task.task_db_id AS dep_task_db_id,
         task_task.task_id  AS dep_task_id,
         0 AS previous_bool
      FROM
         task_task
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         task_task.unique_bool    = 1
         AND
         -- only not on-condition task definitions
         task_task.on_condition_bool = 0
         AND
         -- filter out JICs
         ref_task_class.class_mode_cd != 'JIC'

      UNION ALL

      -- Get next CRT dependent task definitions
      SELECT
         r_task_task.task_db_id AS dep_task_db_id,
         r_task_task.task_id AS dep_task_id,
         0 AS previous_bool
      FROM
         task_task
         INNER JOIN task_task_dep ON
            task_task_dep.task_db_id = task_task.task_db_id AND
            task_task_dep.task_id    = task_task.task_id
         -- return latest revision of next dependent task definition
         INNER JOIN task_task r_task_task ON
            r_task_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
            r_task_task.task_defn_id    = task_task_dep.dep_task_defn_id
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
         -- Get latest approved task revision
         INNER JOIN gtt_h_baseline_task ON
            gtt_h_baseline_task.task_db_id = r_task_task.task_db_id AND
            gtt_h_baseline_task.task_id    = r_task_task.task_id
      WHERE
         -- only lookup next tasks if not going backwards
         aBackwardsBool = 0
         AND
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         -- only unique task definitions
         task_task.unique_bool = 1
         AND
         -- only not on-condition task definitions
         task_task.on_condition_bool = 0
         AND
         -- filter out JIC class modes
         ref_task_class.class_mode_cd != 'JIC'
         AND
         -- get next dependent tasks
         task_task_dep.task_dep_action_db_id = 0 AND
         task_task_dep.task_dep_action_cd    = 'CRT'
         -- exclude the calling task definition since this will be called recursively
         AND NOT
         (
            r_task_task.task_db_id = aPrevTaskDbId AND
            r_task_task.task_id    = aPrevTaskId
         )

      UNION ALL

      SELECT
         task_task_dep.task_db_id AS dep_task_db_id,
         task_task_dep.task_id AS dep_task_id,
         1 AS previous_bool
      FROM
         task_task
         INNER JOIN ref_task_class ON
            ref_task_class.task_class_db_id = task_task.task_class_db_id AND
            ref_task_class.task_class_cd    = task_task.task_class_cd
         -- Get prev dependent tasks
         INNER JOIN task_task_dep ON
            task_task_dep.dep_task_defn_db_id = task_task.task_defn_db_id AND
            task_task_dep.dep_task_defn_id    = task_task.task_defn_id
         -- Get latest approved task revision
         INNER JOIN gtt_h_baseline_task ON
            gtt_h_baseline_task.task_db_id = task_task_dep.task_db_id AND
            gtt_h_baseline_task.task_id    = task_task_dep.task_id
      WHERE
         -- only lookup next tasks if not going backwards
         aBackwardsBool = 0
         AND
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id    = aTaskId
         AND
         -- only unique task definitions
         task_task.unique_bool = 1
         AND
         -- only not on-condition task definitions
         task_task.on_condition_bool = 0
         AND
         -- filter out JICs
         ref_task_class.class_mode_cd != 'JIC'
         AND
         task_task_dep.task_dep_action_db_id = 0 AND
         task_task_dep.task_dep_action_cd    = 'POSTCRT'
         -- exclude the calling task definition since this will be called recursively
         AND NOT
         (
            task_task_dep.task_db_id = aPrevTaskDbId AND
            task_task_dep.task_id    = aPrevTaskId
         );

   lDeptTaskRec lDeptTask%ROWTYPE;

BEGIN

   lTaskDefnHasActvDependentTasks := 0;
   lIsEvaluated := 0;
   lIndex := 0;

   -- initialize local table variable
   lTable := mxKeyTable(mxKey(-1,-1));

   -- copy aTable into local variable so we can add to it
   IF aTable IS NOT NULL THEN

      FOR i IN aTable.FIRST..aTable.LAST LOOP
         -- add a new row
         lTable.EXTEND;
         -- get index of newly added row
         lIndex := lTable.LAST;
         -- assign value
         lTable(lIndex) := mxKey(aTable(i).db_id, aTable(i).id);
      END LOOP;

   END IF;

   -- loop through all the previous and next dependent tasks to determine if an actv task exists
   FOR lDeptTaskRec IN lDeptTask LOOP

      lTaskDefnHasActvDependentTasks := 0;

      -- Check if this dependent task definition already evaluated before
      -- (added rownum for performance)
      SELECT DECODE( COUNT(*), 0, 0, 1)
      INTO
         lIsEvaluated
      FROM
         TABLE(lTable) evalTable
      WHERE
         evalTable.db_id = lDeptTaskRec.dep_task_db_id AND
         evalTable.id    = lDeptTaskRec.dep_task_id
         AND
         ROWNUM = 1;

      -- If dependent task defn not evaluated, we will proceed
      IF lIsEvaluated = 0 THEN

         -- if there is at least one actv task, we can set lTaskDefnHasActvDependentTasks to 1
         -- (added rownum for performance)
         SELECT
            DECODE (count(*), 0, 0, 1) as actv_task_count
         INTO
            lTaskDefnHasActvDependentTasks
         FROM
            task_task
            -- get all the task definition revisions for the task definition
            INNER JOIN task_task actv_task_task ON
               actv_task_task.task_defn_db_id = task_task.task_defn_db_id AND
               actv_task_task.task_defn_id    = task_task.task_defn_id
            -- Lookup all active tasks with the given definition
            INNER JOIN sched_stask ON
               sched_stask.task_db_id = actv_task_task.task_db_id AND
               sched_stask.task_id    = actv_task_task.task_id
            INNER JOIN evt_event ON
               evt_event.event_db_id = sched_stask.sched_db_id AND
               evt_event.event_id    = sched_stask.sched_id
            -- return main inventory tree
            INNER JOIN (
                  SELECT
                     inv_inv.inv_no_db_id,
                     inv_inv.inv_no_id
                  FROM
                     inv_inv
                  WHERE rstat_cd = 0
                  CONNECT BY
                     PRIOR inv_inv.nh_inv_no_db_id = inv_inv.inv_no_db_id AND
                     PRIOR inv_inv.nh_inv_no_id    = inv_inv.inv_no_id AND
                     PRIOR inv_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
                  START WITH
                     inv_inv.inv_no_db_id = aInvNoDbId AND
                     inv_inv.inv_no_id    = aInvNoId
                  ) inv_tree ON
               inv_tree.inv_no_db_id = sched_stask.main_inv_no_db_id AND
               inv_tree.inv_no_id    = sched_stask.main_inv_no_id
         WHERE
            task_task.task_db_id = lDeptTaskRec.dep_task_db_id AND
            task_task.task_id    = lDeptTaskRec.dep_task_id
            AND
            sched_stask.rstat_cd = 0
            AND
            evt_event.event_status_cd = 'ACTV'
            AND
            (
               -- If the task defn is config slot based then return the entire inv tree,
               -- otherwise, for part based task defn only return the target inv.
               -- (config slot based task defns have their assmbl_bom_id set)
               actv_task_task.assmbl_bom_id IS NOT NULL
               OR
               (
                  inv_tree.inv_no_db_id = aInvNoDbId AND
                  inv_tree.inv_no_id    = aInvNoId
               )
            )
            AND
            ROWNUM = 1;

         -- if active tasks exist, return null table
         IF lTaskDefnHasActvDependentTasks = 1 THEN
            RETURN NULL;

         -- else insert task definition into table
         ELSE

            lTable.EXTEND;
            lIndex := lTable.LAST;
            lTable(lIndex) := mxKey(lDeptTaskRec.dep_task_db_id,lDeptTaskRec.dep_task_id);

            -- call recursively on current task definition only if is not the one being passed into function initially
            -- (for performance reasons)
            IF NOT(aTaskDbId = lDeptTaskRec.dep_task_db_id AND aTaskId = lDeptTaskRec.dep_task_id) THEN

               -- Evaluate dependent tasks of current task definition
               lTable := hasACTVDependentTask(
                     lDeptTaskRec.dep_task_db_id,
                     lDeptTaskRec.dep_task_id,
                     aInvNoDbId,
                     aInvNoId,
                     aTaskDbId,
                     aTaskId,
                     lTable,
                     lDeptTaskRec.previous_bool);

               -- if null is returned, there was an actv tasks
               IF lTable IS NULL THEN
                  RETURN NULL;
               END IF;

            END IF;

         END IF;

      END IF; -- end if for checking lIsEvaluated=0

   END LOOP; -- end loop for dependent tasks

   RETURN lTable;

END hasACTVDependentTask;
/

--changeSet OPER-30126:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet OPER-30126:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id )
  VALUES('VALIDATE_TASK_ACTUALS','com.mxi.mx.core.worker.bsync.ValidateTaskActualsWorker','wm/Maintenix-BaselineSyncWorkManager',1, 0, 500, 0 );
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
  NULL;
END;
/

--changeSet OPER-30126:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE utl_work_item_type ADD (
           BATCH_EXECUTION_TIME_LIMIT Number(4,0) DEFAULT 0 NOT NULL
      )
   ');
END;
/

--changeSet OPER-30126:9 stripComments:false
COMMENT ON COLUMN UTL_WORK_ITEM_TYPE.BATCH_EXECUTION_TIME_LIMIT IS 'Enables batch execution optimization if set to a value greater than zero. Defines the maximum scheduled time block to fit in related work items in a batch scheduling operation' ;

--changeSet OPER-30126:10 stripCommends:false
--comment Enable batch optimization for work items known to be sensitive to this sort of scheduling
UPDATE UTL_WORK_ITEM_TYPE
SET BATCH_EXECUTION_TIME_LIMIT = 300
WHERE NAME IN 
('CALCULATE_AIRCRAFT_OPERATING_STATUS','CANCEL_TASK','INITIALIZE_TASK','UPDATE_TASK','ZIP_TASK');

--changeSet OPER-30126:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'WORK_ITEM_BSYNCVALIDATEACTUALS_THRESHOLD',
      'WORK_MANAGER',
      'Defines the number of baseline task dependencies that will trigger baseline sync validate task actuals work to be done in a separate work item.',
      'GLOBAL',
      'Non-negative integer.',
      '8',
      1,
      'BSync - Validate Task Actuals',
      '8.2-SP3',
      0
   );
END;
/
