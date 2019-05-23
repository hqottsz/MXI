--liquibase formatted sql

--changeSet OPER-19339:1 stripComments:false
--comment add a task of type FOLLOW into table ref_task_class
INSERT INTO
   ref_task_class
      (
	     task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
	  )
SELECT	  
   0, 'FOLLOW', 0, 1, 'Follow-on Task', 'A follow-on repair task for allowable damage tracking', 0, 0, 0, 'REQ', 0, 0, TO_DATE('29-03-2018','DD-MM-YYYY'), TO_DATE('29-03-2018','DD-MM-YYYY'), 100, 'MXI'
FROM
   DUAL
WHERE
   NOT EXISTS
      (
	     SELECT 1 FROM ref_task_class WHERE task_class_db_id =0 AND task_class_cd ='FOLLOW'
      );