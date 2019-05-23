/******************************************************************************
* Script Filename  : task_taskACTV.sql
*
* Script Description: Activates task definitions, except "AL_BUILD" task. Injects tasks into task_fleet_list table (assumes they're not part of a maintenance program)
*                     
*                     
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2016 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/


-- Spool processing log
-- SPOOL log\task_taskACTV.log

   BEGIN
           UPDATE task_task SET task_def_status_cd = 'ACTV' WHERE task_cd not in ( 'AL_BUILD', 'ONE-TIME-REQ', 'BAM-JICSTEPLEVELSIGNOFF-JIC1' );
	   	   
	      INSERT 
	      INTO 
	         task_fleet_approval (
	   	 task_defn_db_id,
	   	 task_defn_id,
	   	 task_db_id,
	   	 task_id
	         )
	      SELECT 
	         task_defn_db_id, 
	         task_defn_id, 
	         task_db_id, 
	         task_id 
	      FROM 
	         task_task
	      WHERE
	         task_def_status_db_id = 0 AND
	         task_def_status_cd    = 'ACTV' AND
	         recurring_task_bool   = 0 AND
      task_cd not in ('AL_BUILD', 'ONE-TIME-REQ', 'BAM-JICSTEPLEVELSIGNOFF-JIC1');
      
  END;
  /
   
-- SPOOL OFF; 