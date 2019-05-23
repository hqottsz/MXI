--liquibase formatted sql


--changeSet acor_sched_zone_list_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_sched_zone_list_v1
AS 
SELECT
   sched_stask.alt_id     AS sched_id,
   LISTAGG(zone_cd,',') WITHIN GROUP (ORDER BY sched_stask.sched_db_id, sched_stask.sched_id) zone_list
FROM
   sched_stask
   INNER JOIN sched_zone ON
      sched_stask.sched_db_id = sched_zone.sched_db_id AND
      sched_stask.sched_id    = sched_zone.sched_id
   INNER JOIN eqp_task_zone ON
      sched_zone.zone_db_id = eqp_task_zone.zone_db_id AND
      sched_zone.zone_id    = eqp_task_zone.zone_id
GROUP BY
   sched_stask.alt_id;