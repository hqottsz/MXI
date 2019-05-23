--liquibase formatted sql


--changeSet acor_block_defn_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_block_defn_v1
AS
SELECT
   task_task.alt_id                    AS task_id,
   task_task.task_cd                   AS task_code,
   task_task.task_name                 AS task_name,
   task_task.revision_ord              AS revision_order,
   task_task.task_def_status_cd        AS status_code,
   task_task.block_chain_sdesc         AS block_chain_description,
   task_task.est_duration_qt           AS estimated_duration_quantity,
   eqp_assmbl_bom.assmbl_cd            AS assembly_code,
   eqp_assmbl_bom.alt_id               AS bom_id,
   eqp_assmbl_bom.assmbl_bom_cd        AS config_slot_code,
   task_task.task_class_cd             AS task_class_code,
   task_task.task_subclass_cd          AS task_subclass_code,
   task_task.task_originator_cd        AS originator_code,
   task_task.task_ref_sdesc            AS reference_description,
   task_task.task_appl_ldesc           AS applicability_description,
   task_task.task_appl_eff_ldesc       AS applicability_range,
   formatApplSQLForDisplay(task_task.task_appl_sql_ldesc) AS applicability_rule,
   task_task.task_sched_from_cd        AS scheduled_from_code,
   task_task.effective_dt              AS effectivity_date,
   task_task.manual_scheduling_bool    AS manual_scheduling_flag,
   task_task.sched_from_latest_bool    AS use_manufact_date_flag,
   task_task.resched_from_cd           AS reschedule_from_code,
   task_task.recurring_task_bool       AS recurring_task_flag,
   task_task.forecast_range_qt         AS forecast_range_quantity,
   task_task.on_condition_bool         AS on_condition_flag,
   task_task.soft_deadline_bool        AS soft_deadline_flag,
   task_task.task_priority_cd          AS priority_code,
   CONCAT_DATA(CURSOR(SELECT
                         work_type_cd
                      FROM
                         acor_task_work_type_v1
                      WHERE
                         acor_task_work_type_v1.task_id    = task_task.alt_id
                      )
               )                       AS work_type_list,
   task_task_flags.review_receipt_bool AS review_on_receipt_flag,
   task_task.min_plan_yield_pct        AS minimum_plan_yield_percentage,
   fnc_account.alt_id                  AS account_id,
   task_task.task_ldesc                AS task_description,
   task_task.engineering_ldesc         AS engineering_description,
   CASE
     WHEN task_task.rev_hr_id IS NOT NULL THEN
      utl_user.last_name ||', ' || utl_user.first_name
   END                               AS revision_contact,
   task_task.task_rev_reason_cd      AS revision_reason_code,
   task_task.rev_note                AS revision_note,
   org_org.org_cd,
   task_task.ext_key_sdesc           AS external_key_sdesc
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN task_task_flags ON
      task_task.task_db_id = task_task_flags.task_db_id AND
      task_task.task_id    = task_task_flags.task_id
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
   INNER JOIN org_org ON
      org_org.org_db_id = task_task.org_db_id AND
      org_org.org_id    = task_task.org_id
   LEFT JOIN org_hr ON
      task_task.rev_hr_db_id = org_hr.hr_db_id AND
      task_task.rev_hr_id    = org_hr.hr_id
   LEFT JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
   LEFT JOIN fnc_account ON
      task_task.issue_account_db_id = fnc_account.account_db_id AND
      task_task.issue_account_id    = fnc_account.account_id
WHERE
   class_mode_cd = 'BLOCK'
;