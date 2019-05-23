--liquibase formatted sql


--changeSet acor_sched_panel_list_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_sched_panel_list_v1
AS 
SELECT
   sched_stask.alt_id   AS sched_id,
   LISTAGG(panel_cd,',') WITHIN GROUP (ORDER BY sched_stask.sched_db_id, sched_stask.sched_id) panel_list
FROM
   sched_stask
   INNER JOIN sched_panel ON
      sched_stask.sched_db_id = sched_panel.sched_db_id AND
      sched_stask.sched_id    = sched_panel.sched_id
   INNER JOIN eqp_task_panel ON
      sched_panel.panel_db_id = eqp_task_panel.panel_db_id AND
      sched_panel.panel_id    = eqp_task_panel.panel_id
GROUP BY
   sched_stask.alt_id;