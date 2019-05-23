--liquibase formatted sql


--changeSet isTaskActvOrObsoleteForInv:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:     isTaskActvOrObsoleteForInv
* Arguments:    aTaskDbId, aTaskId    - pk for the task definition revision
*               aInvNoDbId, aInvNoId  - pk for the inventory
* Description:  This function determines if a task revision is active
*               on an inventory
*********************************************************************************/
CREATE OR REPLACE FUNCTION isTaskActvOrObsoleteForInv
(
    aTaskDbId task_task.task_db_id%TYPE,
    aTaskId task_task.task_id%TYPE,
    aInvNoDbId inv_inv.inv_no_db_id%TYPE,
    aInvNoId inv_inv.inv_no_id%TYPE
) RETURN NUMBER
IS
   lIsOnMaintenanceProgram NUMBER;
   lReturn NUMBER;
BEGIN
   -- If the task is on a maintenance program, use vw_baseline_task
   SELECT
      count(*) INTO lIsOnMaintenanceProgram
   FROM
      task_task
      INNER JOIN maint_prgm_task ON
         maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id = task_task.task_defn_id
   WHERE
      task_task.task_db_id = aTaskDbId AND
      task_task.task_id = aTaskId;

   IF lIsOnMaintenanceProgram <> 0 THEN

      -- Get Current Inventory Context if it exists
      SELECT
         count(*) INTO lReturn
      FROM
         vw_baseline_task
      WHERE
         vw_baseline_task.task_db_id = aTaskDbId AND
         vw_baseline_task.task_id = aTaskId AND
         vw_baseline_task.filter_inv_no_db_id = aInvNoDbId AND
         vw_baseline_task.filter_inv_no_id = aInvNoId
         ;
   ELSE
      SELECT
         count(*) INTO lReturn
      FROM
         task_task
      WHERE
         task_task.task_db_id = aTaskDbId AND
         task_task.task_id = aTaskId
         AND
         task_task.task_def_status_db_id = 0 AND
         task_task.task_def_status_cd IN ( 'ACTV', 'OBSOLETE' );
   END IF;

   IF lReturn <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isTaskActvOrObsoleteForInv;
/