--liquibase formatted sql


--changeSet acor_jic_labor_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_labor_v1
AS 
SELECT
   task_task.alt_id                      AS task_id,
   task_labour_list.labour_skill_cd      AS labour_skill_code,
   task_labour_list.man_pwr_ct           AS manpower_count,
   task_labour_list.work_perf_hr         AS work_perform_duration,
   task_labour_list.cert_bool            AS certify_flag,
   task_labour_list.cert_hr              AS certification_schedule_hour,
   task_labour_list.insp_bool            AS inspection_flag,
   task_labour_list.insp_hr              AS inspection_schedule_hour
FROM
   task_task
   INNER JOIN task_labour_list ON
      task_task.task_db_id = task_labour_list.task_db_id AND
      task_task.task_id    = task_labour_list.task_id
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       )
;