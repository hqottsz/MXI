--liquibase formatted sql


--changeSet acor_jic_cond_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_cond_v1
AS 
SELECT
   task_task.alt_id            AS task_id,
   ref_ac_cond.ac_cond_cd      AS condition_code,
   ref_ac_cond.desc_sdesc      AS condition_name,
   ref_cond_set.cond_set_cd    AS setting_code,
   ref_cond_set.desc_sdesc     AS setting_name
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- conditions
   INNER JOIN task_cond ON
      task_task.task_db_id = task_cond.task_db_id AND
      task_task.task_id    = task_cond.task_id
   INNER JOIN ref_ac_cond ON
      task_cond.ac_cond_db_id = ref_ac_cond.ac_cond_db_id AND
      task_cond.ac_cond_cd    = ref_ac_cond.ac_cond_cd
   INNER JOIN ref_cond_set ON
      task_cond.cond_set_db_id = ref_cond_set.cond_set_db_id AND
      task_cond.cond_set_cd    = ref_cond_set.cond_set_cd
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       );