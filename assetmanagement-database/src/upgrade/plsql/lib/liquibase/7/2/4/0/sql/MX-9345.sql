--liquibase formatted sql


--changeSet MX-9345:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Create a package for accessing the CONTEXT
*/
CREATE OR REPLACE PACKAGE context_package AS
   -- define an exception for the package
   null_context_exception exception;
   pragma exception_init(null_context_exception, -20111);
   
   -- set the context
   PROCEDURE set_inv(aInvNoDbId IN NUMBER, aInvNoId IN NUMBER, aReturn OUT NUMBER);

   -- checks if context is set
   FUNCTION is_inv_set RETURN NUMBER;
   
   -- getters for the context
   FUNCTION get_inv_no_db_id RETURN NUMBER;
   FUNCTION get_inv_no_id RETURN NUMBER;
END context_package;
/

--changeSet MX-9345:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY context_package AS
   
   -- set the current inventory key
   PROCEDURE set_inv(aInvNoDbId IN NUMBER, aInvNoId IN NUMBER, aReturn OUT NUMBER) IS
   BEGIN
      dbms_session.set_context(getContextName('current_inv_no'), 'inv_no_db_id', aInvNoDbId);
      dbms_session.set_context(getContextName('current_inv_no'), 'inv_no_id', aInvNoId);
      
      aReturn := 1;
      
      EXCEPTION
         WHEN OTHERS THEN
            aReturn := -1;
      	    application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
   END set_inv;
   
   -- checks if the context is set
   FUNCTION is_inv_set RETURN NUMBER
   IS
      lInvNoDbId NUMBER;
   BEGIN
      -- get the db id
      lInvNoDbId := sys_context(getContextName('current_inv_no'), 'inv_no_db_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoDbId IS NULL OR lInvNoDbId = '') THEN
         RETURN 0;
      ELSE
         RETURN 1;
      END IF;
   END is_inv_set;
   
   -- get the current inv_no_db_id
   FUNCTION get_inv_no_db_id RETURN NUMBER
   IS
      lInvNoDbId NUMBER;
   BEGIN
      -- get the db id
      lInvNoDbId := sys_context(getContextName('current_inv_no'), 'inv_no_db_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoDbId IS NULL OR lInvNoDbId = '') THEN
         raise_application_error(-20111, 'Inventory context value cannot be null');
      END IF;
      RETURN lInvNoDbId; 
      
      EXCEPTION
         WHEN null_context_exception THEN
            RAISE;
   END get_inv_no_db_id;
   
   -- get the current inv_no_db_id
   FUNCTION get_inv_no_id RETURN NUMBER 
   IS
      lInvNoId NUMBER;
   BEGIN
      -- get the id
      lInvNoId := sys_context(getContextName('current_inv_no'), 'inv_no_id');
      
      -- if there is no value, raise an exception
      IF (lInvNoId IS NULL OR lInvNoId = '') THEN
         raise_application_error(-20111, 'Inventory context value cannot be null');
      END IF;
      RETURN lInvNoId; 
      
      EXCEPTION
         WHEN null_context_exception THEN
            RAISE;
            
   END get_inv_no_id;
END context_package;
/

--changeSet MX-9345:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:     isTaskACTVForInv
* Arguments:    aTaskDbId, aTaskId    - pk for the task definition revision
*               aInvNoDbId, aInvNoId  - pk for the inventory
* Description:  This function determines if a task revision is active
*               on an inventory
*********************************************************************************/
CREATE OR REPLACE FUNCTION isTaskACTVForInv
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
         task_task.task_def_status_cd = 'ACTV';
   END IF;
   
   IF lReturn <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isTaskACTVForInv;
/