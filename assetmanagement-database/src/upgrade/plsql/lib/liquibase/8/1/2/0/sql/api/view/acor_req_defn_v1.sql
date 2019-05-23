--liquibase formatted sql


--changeSet acor_req_defn_v1:1 stripComments:false
  CREATE OR REPLACE FORCE VIEW ACOR_REQ_DEFN_V1
  AS
  SELECT
   task_task.alt_id                        AS task_id,
   task_defn.alt_id                        AS defn_id,
   task_task.task_cd                       AS task_code,
   task_task.task_name,
   task_task.task_ldesc                    AS task_description,
   task_task.task_class_cd                 AS task_class_code,
   task_task.task_subclass_cd              AS task_subclass_code,
   task_task.task_def_status_cd            AS status_code,
   ref_task_subclass.user_subclass_cd      AS user_subclass_code,
   task_task.task_originator_cd            AS originator_code,
   task_task.revision_ord                  AS revision_order,
   task_task.task_ref_sdesc                AS reference_description,
   task_task.task_appl_ldesc               AS applicability_description,
   task_task.task_appl_eff_ldesc           AS applicability_range,
   formatApplSQLForDisplay(task_task.task_appl_sql_ldesc) AS applicability_rule,
   task_task.task_sched_from_cd           AS schedule_from_code,
   task_task.effective_dt                 AS effectivity_date,
   task_task.manual_scheduling_bool       AS manual_scheduling_flag,
   CASE  -- when effective
      WHEN task_task.task_sched_from_cd  = 'EFFECTIVE_DT' AND
           task_task.rev_hr_id IS NOT NULL THEN
     1
   ELSE
     0
   END                                    AS use_manufact_date_flag,
   task_task.resched_from_cd              AS reschedule_from_code,
   task_task.recurring_task_bool          AS recurring_flag,
   task_task.forecast_range_qt            AS forecast_range,
   task_task.on_condition_bool            AS on_condition_flag,
   task_task.soft_deadline_bool           AS soft_deadline_flag,
   task_task.etops_bool                   AS etops_flag,
   task_task.task_priority_cd             AS priority_code,
   CONCAT_DATA(CURSOR(SELECT
                         work_type_cd
                      FROM
                         acor_task_work_type_v1
                      WHERE
                         acor_task_work_type_v1.task_id    = task_task.alt_id
                      )
               )                          AS work_type_list,
   CASE  -- when on condition and effective is not null
      WHEN task_task.on_condition_bool = 1 AND
           task_task.effective_dt      IS NOT NULL THEN
     1
   ELSE
     0
   END                                    AS review_on_receipt_flag,
   task_task.min_plan_yield_pct           AS min_plan_yield,
   fnc_account.alt_id                     AS account_id,
   task_task.task_ldesc                   AS description,
   task_task.engineering_ldesc            AS engineering_description,
   task_task.instruction_ldesc            AS instruction,
   CASE
     WHEN task_task.rev_hr_id IS NOT NULL THEN
      utl_user.last_name ||', ' || utl_user.first_name
   END revision_contact,
   task_task.task_rev_reason_cd           AS revision_reason_code,
   task_task.rev_note                     AS revision_note,
   org_org.org_cd                         AS organization_code,
   task_task.ext_key_sdesc                AS external_key_description,
   task_task.workscope_bool               AS workscope_flag
FROM
   task_task
   INNER JOIN task_defn ON
      task_task.task_defn_db_id  = task_defn.task_defn_db_id AND
      task_task.task_defn_id  = task_defn.task_defn_id      
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   LEFT JOIN ref_task_subclass ON
      task_task.task_class_db_id = ref_task_subclass.task_class_db_id AND
      task_task.task_class_cd    = ref_task_subclass.task_class_cd    AND
      task_task.task_subclass_db_id = ref_task_subclass.task_subclass_db_id AND
      task_task.task_subclass_cd    = ref_task_subclass.task_subclass_cd
   INNER JOIN org_org ON
      org_org.org_db_id = task_task.org_db_id AND
      org_org.org_id    = task_task.org_id
   LEFT JOIN org_hr ON
      task_task.rev_hr_db_id = org_hr.hr_db_id AND
      task_task.rev_hr_id    = org_hr.hr_id
   LEFT JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
   -- issue to account
   LEFT JOIN fnc_account ON
      task_task.issue_account_db_id = fnc_account.account_db_id AND
      task_task.Issue_account_id    = fnc_account.account_id
WHERE
   class_mode_cd = 'REQ';