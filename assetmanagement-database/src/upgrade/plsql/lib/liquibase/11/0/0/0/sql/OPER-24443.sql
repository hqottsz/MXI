--liquibase formatted sql

--changeSet OPER-24443:1 stripComments:false 
INSERT INTO
   ref_task_class
      (
	     task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, assignable_to_block_bool, assignable_to_maint_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
	  )
SELECT	  
   0, 'REPREF', 0, 1, 'Repair Reference', 'A task class used for all types of repairs (EA, SRM, etc.). Can only be selected when resolving a fault.', 0, 1, 1, 'REQ', 0, 0, 0, 0, TO_DATE('27-07-2018','DD-MM-YYYY'), TO_DATE('27-07-2018','DD-MM-YYYY'), 100, 'MXI'
FROM
   DUAL
WHERE
   NOT EXISTS
      (
	     SELECT 1 FROM ref_task_class WHERE task_class_db_id =0 AND task_class_cd ='REPREF'
      );