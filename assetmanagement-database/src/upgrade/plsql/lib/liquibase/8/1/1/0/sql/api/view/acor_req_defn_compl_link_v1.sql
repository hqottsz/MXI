--liquibase formatted sql


--changeSet acor_req_defn_compl_link_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_defn_compl_link_v1
AS 
SELECT
   task_task.alt_id                     AS task_id,
   task_task.task_cd                    AS task_code,
   task_defn.alt_id                     AS dep_task_id,
   task_defn.task_cd                    AS dep_task_code,
   task_task_dep.task_dep_action_cd     AS dep_action_code
FROM
   task_task
   INNER JOIN ref_task_class ON
     task_task.task_class_db_id = ref_task_class.task_class_db_id AND
     task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN task_task_dep ON
      task_task.task_db_id = task_task_dep.task_db_id AND
      task_task.task_id    = task_task_dep.task_id
   INNER JOIN task_task task_defn ON
      task_task_dep.dep_task_defn_db_id = task_defn.task_defn_db_id AND
      task_task_dep.dep_task_defn_id    = task_defn.task_defn_id
WHERE
   ref_task_class.class_mode_cd = 'REQ'
   AND
   task_task_dep.task_dep_action_db_id = 0 AND
   task_task_dep.task_dep_action_cd    <> 'COMPLIES'
;