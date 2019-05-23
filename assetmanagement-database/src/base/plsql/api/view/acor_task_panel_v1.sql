--liquibase formatted sql


--changeSet acor_task_panel_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_task_panel_v1
AS
SELECT
   task_task.alt_id       AS task_id,
   panel.assmbl_cd,
   zone.zone_cd           AS zone_code,
   zone.desc_sdesc        AS zone_name,
   panel.panel_cd         AS panel_code,
   panel.desc_sdesc       AS panel_name
FROM
   task_task
   INNER JOIN task_panel ON
      task_task.task_db_id = task_panel.task_db_id AND
      task_task.task_id    = task_panel.task_id
   -- core task panel view
   INNER JOIN eqp_task_panel panel ON
      task_panel.panel_db_id = panel.panel_db_id AND
      task_panel.panel_id    = panel.panel_id
   -- core task zone view
   INNER JOIN eqp_task_zone zone ON
      panel.zone_db_id = zone.zone_db_id AND
      panel.zone_id    = zone.zone_id
      AND
      panel.assmbl_db_id = zone.assmbl_db_id AND
      panel.assmbl_cd    = zone.assmbl_cd
;