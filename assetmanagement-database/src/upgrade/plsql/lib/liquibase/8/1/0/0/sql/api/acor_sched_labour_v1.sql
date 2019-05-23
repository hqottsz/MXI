--liquibase formatted sql


--changeSet acor_sched_labour_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_sched_labour_v1
AS 
SELECT
      sched_stask.alt_id AS sched_stask_id,
      sched_labour.labour_stage_cd,
      sched_labour.labour_skill_cd,
      sched_labour.work_perf_bool,
      sched_labour.cert_bool,
      sched_labour.insp_bool
FROM
     sched_labour
     INNER JOIN sched_stask ON
      sched_stask.sched_db_id = sched_labour.sched_db_id AND
      sched_stask.sched_id    = sched_labour.sched_id
;