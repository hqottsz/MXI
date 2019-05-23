--liquibase formatted sql


--changeSet acor_mpc_openpanel_jic_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_mpc_openpanel_jic_v1
AS
WITH
rvw_task_panel
AS (
   SELECT
   task_task.alt_id       AS task_id,
   panel.assmbl_db_id,
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
)
SELECT
   task_task.alt_id                    AS task_id,
   task_task.task_cd                   AS task_code,
   eqp_assmbl_bom.alt_id               AS bom_id,
   eqp_assmbl_bom.assmbl_cd            AS assembly_code,
   task_task.task_appl_ldesc           AS applicability_description,
   task_task.task_appl_eff_ldesc       AS applicability_range,
   formatApplSQLForDisplay(task_task.task_appl_sql_ldesc) AS applicability_rule,
   org_org.alt_id                      AS organization_id,
   org_org.org_cd                      AS organization_code,
   task_task.revision_ord              AS revision_order
FROM
   task_task
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN org_org ON
      org_org.org_db_id = task_task.org_db_id AND
      org_org.org_id    = task_task.org_id
   INNER JOIN rvw_task_panel panel ON
      task_task.alt_id    = panel.task_id
      AND
      task_task.assmbl_db_id = panel.assmbl_db_id AND
      task_task.assmbl_cd    = panel.assmbl_cd
   -- assmbl
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
WHERE
   ref_task_class.task_class_db_id = 0 AND
   ref_task_class.task_class_cd    = 'OPENPANEL'
;