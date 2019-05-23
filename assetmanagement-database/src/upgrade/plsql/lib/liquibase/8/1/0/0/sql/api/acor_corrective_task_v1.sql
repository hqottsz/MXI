--liquibase formatted sql


--changeSet acor_corrective_task_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_CORRECTIVE_TASK_V1 AS
SELECT
   sched_stask.alt_id               AS corrective_task_id,
   sd_fault.alt_id                  AS fault_id
FROM
   --corrective task
   sched_stask
   INNER JOIN sd_fault ON
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id    = sched_stask.fault_id;