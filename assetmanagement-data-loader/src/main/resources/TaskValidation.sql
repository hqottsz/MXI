SELECT
   'C_RI_TASK' AS "TABLE",
   al_proc_tasks.task_cd,
   al_proc_tasks.serial_no_oem,
   al_proc_tasks.part_no_oem,
   al_proc_tasks.manufact_cd,
   'N/A' AS "PARAMETER_CD",
   al_proc_tasks_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_tasks
   INNER JOIN al_proc_tasks_error ON
      al_proc_tasks.record_id = al_proc_tasks_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_tasks_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_TASK' AS "TABLE",
   al_proc_hist_task.task_cd,
   al_proc_hist_task.serial_no_oem,
   al_proc_hist_task.part_no_oem,
   al_proc_hist_task.manufact_cd,
   'N/A' AS "PARAMETER_CD",
   al_proc_tasks_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_hist_task
   INNER JOIN al_proc_tasks_error ON
      al_proc_hist_task.record_id = al_proc_tasks_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_tasks_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_TASK_SCHED' AS "TABLE",
   al_proc_tasks_parameter.task_cd,
   al_proc_tasks_parameter.serial_no_oem,
   'N/A' AS "PART_NO_OEM",
   'N/A' AS "MANUFACT_CD",
   al_proc_tasks_parameter.parameter_code AS "PARAMETER_CD",
   al_proc_tasks_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_tasks_parameter
   INNER JOIN al_proc_tasks_error ON
      al_proc_tasks_parameter.record_id = al_proc_tasks_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_tasks_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_TASK_SCHED' AS "TABLE",
   al_proc_hist_task_parameter.task_cd,
   al_proc_hist_task_parameter.serial_no_oem,
   al_proc_hist_task_parameter.part_no_oem,
   al_proc_hist_task_parameter.manufact_cd,
   al_proc_hist_task_parameter.scheduling_parameter AS "PARAMETER_CD",
   al_proc_tasks_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_hist_task_parameter
   INNER JOIN al_proc_tasks_error ON
      al_proc_hist_task_parameter.record_id = al_proc_tasks_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_tasks_error.error_cd
WHERE
    dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'GLOBAL' AS "TABLE",
   'N/A' AS task_cd,
   'N/A' AS serial_no_oem,
   'N/A' AS part_no_oem,
   'N/A' AS manufact_cd,
   'N/A' AS "PARAMETER_CD",
   al_proc_tasks_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_tasks_error
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_tasks_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')