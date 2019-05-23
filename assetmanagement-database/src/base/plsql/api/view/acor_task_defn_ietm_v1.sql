--liquibase formatted sql


--changeSet acor_task_defn_ietm_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_task_defn_ietm_v1
AS
SELECT
   task_task.alt_id           AS task_id,
   ietm_ietm.ietm_cd          AS technical_manual_code,
   ietm_topic.topic_sdesc     AS topic_description,
   cmdline_parm_ldesc         AS commandline_parm_description,
   ietm_type_cd               AS ietm_type_code,
   topic_note                 AS topic_note
FROM
   task_task
   INNER JOIN task_task_ietm ON
      task_task.task_db_id = task_task_ietm.task_db_id AND
      task_task.task_id    = task_task_ietm.task_id
   INNER JOIN ietm_topic ON
      task_task_ietm.ietm_db_id    = ietm_topic.ietm_db_id AND
      task_task_ietm.ietm_id       = ietm_topic.ietm_id    AND
      task_task_ietm.ietm_topic_id = ietm_topic.ietm_topic_id
   INNER JOIN ietm_ietm ON
      ietm_topic.ietm_db_id = ietm_ietm.ietm_db_id AND
      ietm_topic.ietm_id    = ietm_ietm.ietm_id
;