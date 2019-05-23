--liquibase formatted sql


--changeSet acor_mpc_steps_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_mpc_steps_v1
AS 
SELECT
   task_task.alt_id             AS task_id,
   task_step.step_ord           AS step_order,
   task_step.step_ldesc         AS step_description
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   -- steps
   INNER JOIN task_step ON
      task_task.task_db_id = task_step.task_db_id AND
      task_task.task_id = task_step.task_id
WHERE
   class_mode_cd = 'MPC';