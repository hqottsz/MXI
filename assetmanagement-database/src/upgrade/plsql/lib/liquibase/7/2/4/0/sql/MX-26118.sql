--liquibase formatted sql


--changeSet MX-26118:1 stripComments:false
-- set task_task_flags.nsv_bool to 0 if the soft_deadline_bool is FALSE 
-- OR task_must_remove_pk is not OFFWING or OFFPARENT
UPDATE 
   task_task_flags
SET
   task_task_flags.nsv_bool = 0
WHERE
   task_task_flags.nsv_bool = 1
   AND
   EXISTS
   (
	SELECT 1
	FROM
	    task_task                    
	WHERE
	    task_task.task_db_id = task_task_flags.task_db_id AND
	    task_task.task_id    = task_task_flags.task_id
	    AND
	    (
		task_task.soft_deadline_bool = 0
		OR
		task_task.task_must_remove_cd NOT IN ( 'OFFWING', 'OFFPARENT' )		
	    )
	    AND
	    task_task.task_def_status_cd IN ( 'ACTV', 'BUILD', 'REVISION' )
   );