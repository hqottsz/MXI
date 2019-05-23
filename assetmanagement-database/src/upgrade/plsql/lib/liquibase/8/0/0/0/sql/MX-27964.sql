--liquibase formatted sql


--changeSet MX-27964:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('ISTASKACTVFORINV');
END;
/

--changeSet MX-27964:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   lIsInvSet NUMBER;
   lInvNoDbIdContext NUMBER := NULL;
   lInvNoIdContext NUMBER := NULL;
   lReturn NUMBER;
   lSetInvReturn NUMBER;
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
      lIsInvSet := context_package.is_inv_set();
      IF lIsInvSet = 1 THEN
         lInvNoDbIdContext := context_package.get_inv_no_db_id();
         lInvNoIdContext := context_package.get_inv_no_id();
      ELSE
         lInvNoDbIdContext := NULL;
         lInvNoIdContext := NULL;
      END IF;

      -- If context is different than the current context, switch to new context
      IF (lIsInvSet = 0 OR aInvNoDbId != lInvNoDbIdContext OR aInvNoId != lInvNoIdContext) THEN
         context_package.set_inv(aInvNoDbId, aInvNoId, lSetInvReturn);
      END IF;

      SELECT
         count(*) INTO lReturn
      FROM
         vw_baseline_task
      WHERE
         vw_baseline_task.task_db_id = aTaskDbId AND
         vw_baseline_task.task_id = aTaskId;
      
      -- When finished with new context, switch back to old context
      IF (lIsInvSet = 0 OR aInvNoDbId != lInvNoDbIdContext OR aInvNoId != lInvNoIdContext) THEN
         context_package.set_inv(lInvNoDbIdContext, lInvNoIdContext, lSetInvReturn);
      END IF;
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