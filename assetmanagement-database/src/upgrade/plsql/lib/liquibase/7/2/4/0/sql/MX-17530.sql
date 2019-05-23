--liquibase formatted sql


--changeSet MX-17530:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     hasACTVDependentTask
* Arguments:    aTaskDbId, aTaskId 	- pk for the task definition revision
*               aInvNoDbId, aInvNoId    - pk for the main inventory
* Description:  This function will determine if there is an ACTV dependant CRT task
*               on a task definition or the task definition has an ACTV task itself.
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

    aTable mxKeyTable

)   RETURN mxKeyTable IS

    lTaskDefnHasActvDependentTasks NUMBER;
    lTable mxKeyTable;
    lIsEvaluated NUMBER;
    lIndex NUMBER;

   -- returns previous and next dependent task definitions (CRT and POSTCRT links)
   CURSOR lDeptTask IS
            SELECT
                  task_task_dep.task_db_id AS dep_task_db_id,
                  task_task_dep.task_id AS dep_task_id
            FROM
                task_task,
                task_task_dep,
                ref_task_class
            WHERE
                 task_task.task_db_id = aTaskDbId AND
                 task_task.task_id    = aTaskId
                 AND
                 -- only unique task definitions
                 task_task.unique_bool = 1
                 AND
                 -- filter out JICs
                 ref_task_class.task_class_db_id = task_task.task_class_db_id AND
                 ref_task_class.task_class_cd    = task_task.task_class_cd
                 AND
                 ref_task_class.class_mode_cd != 'JIC'	AND
         	 ref_task_class.rstat_cd       = 0
                 AND
                 -- Get prev dependent tasks
                 task_task_dep.dep_task_defn_db_id = task_task.task_defn_db_id AND
                 task_task_dep.dep_task_defn_id    = task_task.task_defn_id
                 AND
                 task_task_dep.task_dep_action_cd IN ('CRT', 'POSTCRT')
                 -- exclude the calling task definition since this will be called recursively
                 AND NOT
                 (
                     task_task_dep.task_db_id = aPrevTaskDbId AND
                     task_task_dep.task_id    = aPrevTaskId
                 )

          UNION ALL

            -- Return itself if it is not JIC mode and is unique
            SELECT
  	           task_task.task_db_id AS dep_task_db_id,
  	           task_task.task_id  AS dep_task_id
  	        FROM
  	            task_task,
                ref_task_class
  	        WHERE
  	            task_task.task_db_id = aTaskDbId AND
                task_task.task_id    = aTaskId
                AND
              	task_task.unique_bool    = 1
                AND
  	            -- filter out JICs
                ref_task_class.task_class_db_id = task_task.task_class_db_id AND
                ref_task_class.task_class_cd    = task_task.task_class_cd
                AND
                ref_task_class.class_mode_cd != 'JIC'	AND
         	ref_task_class.rstat_cd       = 0	

          UNION ALL

            -- Get next dependent task definitions
            SELECT
                  r_task_task.task_db_id AS dep_task_db_id,
                  r_task_task.task_id AS dep_task_id
            FROM
                task_task,
                task_task_dep,
                task_task r_task_task,
                ref_task_class
            WHERE
                 task_task.task_db_id = aTaskDbId AND
                 task_task.task_id    = aTaskId
                 AND
                 -- only unique task definitions
                 task_task.unique_bool = 1
                 AND
                 -- filter out JIC class modes
                 ref_task_class.task_class_db_id = task_task.task_class_db_id AND
                 ref_task_class.task_class_cd    = task_task.task_class_cd
                 AND
                 ref_task_class.class_mode_cd != 'JIC'	AND
         	 ref_task_class.rstat_cd       = 0
                 AND
                 -- get next dependent tasks
                 task_task_dep.task_db_id = task_task.task_db_id AND
                 task_task_dep.task_id    = task_task.task_id
                 AND
                 task_task_dep.task_dep_action_cd IN ('CRT', 'POSTCRT')
                 AND
                 -- return latest revision of next dependent task definition
                 r_task_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
                 r_task_task.task_defn_id    = task_task_dep.dep_task_defn_id
                 -- exclude the calling task definition since this will be called recursively
                 AND NOT
                 (
                     r_task_task.task_db_id = aPrevTaskDbId AND
                     r_task_task.task_id    = aPrevTaskId
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
              rownum = 1;

         -- If dependent task defn not evaluated, we will proceed
         IF lIsEvaluated = 0 THEN

             -- if there is at least one actv task, we can set lTaskDefnHasActvDependentTasks to 1
             -- (added rownum for performance)
             SELECT
                 DECODE (count(*), 0, 0, 1) as actv_task_count
             INTO
                 lTaskDefnHasActvDependentTasks
             FROM
                 task_task,
                 task_task actv_task_task,
                 sched_stask,
                 evt_event,
                 evt_inv,
                 (
                    -- return main inventory tree
                    SELECT
                       inv_inv.inv_no_db_id,
                       inv_inv.inv_no_id
                    FROM
                       inv_inv
                    WHERE rstat_cd = 0
                    CONNECT BY
                       PRIOR inv_inv.nh_inv_no_db_id = inv_inv.inv_no_db_id AND
                       PRIOR inv_inv.nh_inv_no_id    = inv_inv.inv_no_id AND
                       inv_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
                    START WITH
                       inv_inv.inv_no_db_id = aInvNoDbId AND
                       inv_inv.inv_no_id    = aInvNoId

                    UNION ALL

                    -- return assembly of main inventory (if applicable)
                    SELECT
                       ass_inv.inv_no_db_id,
                       ass_inv.inv_no_id
                    FROM
                       inv_inv,
                       inv_inv ass_inv
                    WHERE
                       inv_inv.inv_no_db_id = aInvNoDbId AND
                       inv_inv.inv_no_id    = aInvNoId
                       AND
         	       inv_inv.rstat_cd	= 0
                       AND
                       ass_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                       ass_inv.inv_no_id    = inv_inv.assmbl_inv_no_id

               )inv_tree

             WHERE
                  -- get all the task definition revisions for the task definition
                  task_task.task_db_id = lDeptTaskRec.dep_task_db_id AND
                  task_task.task_id    = lDeptTaskRec.dep_task_id
                  AND
                  actv_task_task.task_defn_db_id = task_task.task_defn_db_id AND
                  actv_task_task.task_defn_id    = task_task.task_defn_id
                  AND
                  -- Lookup all active tasks with the given definition
                  sched_stask.task_db_id = actv_task_task.task_db_id AND
                  sched_stask.task_id    = actv_task_task.task_id
                  AND
         	  sched_stask.rstat_cd	= 0
                  AND
                  evt_event.event_db_id = sched_stask.sched_db_id AND
                  evt_event.event_id    = sched_stask.sched_id AND
                  evt_event.event_status_cd = 'ACTV'
                  AND
                  -- Link up the task events and inventory
                  evt_inv.event_db_id  = evt_event.event_db_id AND
                  evt_inv.event_id     = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1
                  AND
                  evt_inv.inv_no_db_id = inv_tree.inv_no_db_id AND
                  evt_inv.inv_no_id    = inv_tree.inv_no_id
                  AND
                  rownum = 1;

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
                        lTable := hasactvdependenttask(lDeptTaskRec.dep_task_db_id,lDeptTaskRec.dep_task_id,aInvNoDbId,aInvNoId,aTaskDbId,aTaskId,lTable);

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