--liquibase formatted sql


--changeSet acor_maint_prgm_task_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_maint_prgm_task_v1
AS 
SELECT
   maint_prgm.alt_id                 AS maint_prgm_id,
   task_task.alt_id                  AS task_id,
   task_task.task_cd                 AS task_code,
   ref_task_class.task_class_cd      AS task_class_code,
   ref_task_class.class_mode_cd      AS class_mode_code
FROM
   maint_prgm
   INNER JOIN (
                SELECT
                    maint_prgm_task.maint_prgm_db_id,
                    maint_prgm_task.maint_prgm_id,
                    maint_prgm_task.task_db_id,
                    maint_prgm_task.task_id
                FROM
                    maint_prgm_task
                    where
                    (maint_prgm_task.maint_prgm_db_id, maint_prgm_task.maint_prgm_id, task_defn_db_id, task_defn_id) NOT IN
                    (
                       SELECT maint_prgm_db_id, maint_prgm_id, task_task.task_defn_db_id, task_task.task_defn_id
                       FROM   task_temp_issue_log
                       INNER JOIN task_task ON
                           task_task.task_db_id = task_temp_issue_log.task_db_id AND
                           task_task.task_id = task_temp_issue_log.task_id
                    )
                UNION
                SELECT
                    maint_prgm_task_rev.maint_prgm_db_id,
                    maint_prgm_task_rev.maint_prgm_id,
                    maint_prgm_task_rev.task_db_id,
                    maint_prgm_task_rev.task_id
                FROM
                   task_temp_issue_log maint_prgm_task_rev
              ) maint_prgm_task ON
      maint_prgm.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
      maint_prgm.maint_prgm_id    = maint_prgm_task.maint_prgm_id
   INNER JOIN task_task ON
      maint_prgm_task.task_db_id      = task_task.task_db_id AND
      maint_prgm_task.task_id         = task_task.task_id
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
;