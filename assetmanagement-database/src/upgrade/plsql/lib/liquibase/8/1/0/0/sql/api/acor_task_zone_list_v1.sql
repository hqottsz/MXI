--liquibase formatted sql


--changeSet acor_task_zone_list_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_task_zone_list_v1
AS 
SELECT
   task_task.alt_id     AS task_id,
   zone.assmbl_cd,
   LISTAGG(zone_cd,',') WITHIN GROUP (ORDER BY task_task.task_db_id, task_task.task_id) zone_list
FROM
   task_task
   INNER JOIN task_zone ON
      task_task.task_db_id = task_zone.task_db_id AND
      task_task.task_id    = task_zone.task_id
   -- task zone view
   INNER JOIN eqp_task_zone zone ON
      task_zone.zone_db_id = zone.zone_db_id AND
      task_zone.zone_id    = zone.zone_id
GROUP BY
   task_task.alt_id ,
   zone.assmbl_db_id,
   zone.assmbl_cd
;