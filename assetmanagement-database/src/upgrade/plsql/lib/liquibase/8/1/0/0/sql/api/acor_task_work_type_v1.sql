--liquibase formatted sql


--changeSet acor_task_work_type_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_task_work_type_v1
AS 
SELECT
   task_task.alt_id    AS task_id,
   work_type_cd
FROM
   task_task
   INNER JOIN task_work_type ON
      task_task.task_db_id = task_work_type.task_db_id AND
      task_task.task_id    = task_work_type.task_id
;