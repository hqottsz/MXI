--liquibase formatted sql


--changeSet acor_mpc_defn_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_mpc_defn_v1
AS 
WITH
rvw_task_zone_list
AS (
   SELECT
   task_task.alt_id     AS task_id,
   zone.assmbl_db_id,
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
)
SELECT
   task_task.alt_id                     AS task_id,
   eqp_assmbl_bom.alt_id                AS bom_id,
   eqp_assmbl_bom.assmbl_cd             AS assembly_code,
   task_task.task_originator_cd         AS originator_code,
   task_task.task_def_status_cd         AS status_code,
   org_org.alt_id                       AS organization_id,
   org_org.org_cd                       AS organization_code,
   CONCAT_DATA(CURSOR(SELECT
                         work_type_cd
                      FROM
                         acor_task_work_type_v1
                      WHERE
                         acor_task_work_type_v1.task_id    = task_task.alt_id
                      )
               )                        AS work_type_list,
   task_task.task_priority_cd           AS priority_code,
   task_task.task_ldesc                 AS description,
   task_task.instruction_ldesc          AS instruction,
   (
     SELECT
        zone_list
     FROM
        rvw_task_zone_list zone_list
     WHERE
        zone_list.task_id    = task_task.alt_id
        AND
        zone_list.assmbl_db_id = task_task.assmbl_db_id AND
        zone_list.assmbl_cd    = task_task.Assmbl_cd
   )                                    AS zone_list,
   task_task.task_appl_ldesc            AS applicability_description,
   task_task.task_appl_eff_ldesc        AS applicability_range,
   formatApplSQLForDisplay(task_task.task_appl_sql_ldesc)  AS applicability_rule
FROM
   task_task
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN org_org ON
      org_org.org_db_id = task_task.org_db_id AND
      org_org.org_id    = task_task.org_id
   -- assmbl
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
WHERE
   class_mode_cd = 'MPC'
;