--liquibase formatted sql

--changeSet OPER-19508:1 stripComments:false
--comment add a task dependency of type REPLACES into table ref_task_dep_action
INSERT INTO
   ref_task_dep_action
      (
	    task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
	  )
SELECT	  
   10, 'REPLACES', 'Replaces', 'Replaced by a different task (No logic)', 0, TO_DATE('01-05-2018','DD-MM-YYYY'), TO_DATE('01-05-2018','DD-MM-YYYY'), 100, 'MXI'
FROM
   DUAL
WHERE
   NOT EXISTS
      (
	     SELECT 1 FROM ref_task_dep_action WHERE task_dep_action_db_id = 10 AND task_dep_action_cd = 'REPLACES'
      );