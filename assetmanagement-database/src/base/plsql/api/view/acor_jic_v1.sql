--liquibase formatted sql


--changeSet acor_jic_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_v1
AS
SELECT
   task_task.alt_id                      AS task_id,
   task_task.task_cd                     AS task_code,
   task_task.task_name,
   task_task.task_class_cd               AS task_class_code,
   task_task.task_subclass_cd            AS task_subclass_code,
   task_task.task_originator_cd          AS originator_code,
   task_task.task_ref_sdesc              AS reference_description,
   task_task.task_appl_eff_ldesc         AS applicability_range,
   formatApplSQLForDisplay(task_task.task_appl_sql_ldesc) AS applicability_rule,
   task_task.task_sched_from_cd          AS schedule_from_code,
   task_task.effective_dt                AS effectivity_date,
   task_task.manual_scheduling_bool      AS manual_scheduling_flag,
   CASE  -- when effective
      WHEN task_task.task_sched_from_cd  = 'EFFECTIVE_DT' AND
           task_task.rev_hr_id IS NOT NULL THEN
     1
   ELSE
     0
   END                                   AS use_manufact_date_flag,
   task_task.resched_from_cd             AS rescheduling_from_code,
   task_task.recurring_task_bool         AS recurring_flag,
   task_task.forecast_range_qt           AS forecast_range,
   task_task.on_condition_bool           AS on_condition_flag,
   task_task.soft_deadline_bool          AS soft_deadline_flag,
   task_task.etops_bool                  AS etops_flag,
   task_task.task_priority_cd           AS priority_code,
   CONCAT_DATA(CURSOR(SELECT
                         work_type_cd
                      FROM
                         acor_task_work_type_v1
                      WHERE
                         acor_task_work_type_v1.task_id    = task_task.alt_id
                      )
               ) work_type_list,
   task_task.min_plan_yield_pct         AS minimum_plan_yield,
   fnc_account.alt_id                   AS account_id,
   task_task.task_ldesc                 AS description,
   task_task.engineering_ldesc          AS engineering_description,
   task_task.instruction_ldesc          AS instruction,
   CASE
     WHEN task_task.rev_hr_id IS NOT NULL THEN
      utl_user.last_name ||', ' || utl_user.first_name
   END                                  AS revision_contact,
   task_task.task_rev_reason_cd         AS revision_reason_code,
   task_task.rev_note                   AS revision_note,
   org_org.org_cd                       AS organization_code,
   task_task.ext_key_sdesc              AS external_key_description
FROM
   task_task
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
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
      task_task.issue_account_id    = fnc_account.account_id
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       )
;