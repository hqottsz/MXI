--liquibase formatted sql


--changeSet acor_repetitive_faults_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_repetitive_faults_v1
AS 
WITH
 rvw_fault
 (
   fault_name,
   corr_task_barcode,
   corr_task_id,
   fault_id,
   prev_fault_id,
   init_fault_id,
   found_by_hr_id
  )
 AS
  (
  --- select all initial faults
   SELECT
      fault_event.event_sdesc AS fault_name,
      sched_stask.barcode_sdesc AS corr_task_barcode,
      sched_stask.alt_id AS corr_task_id,
      sd_fault.alt_id AS fault_id,
      NULL AS prev_fault_id,
      sd_fault.alt_id AS init_fault_id,
      org_hr.alt_id AS found_by_hr_id
   FROM
       evt_event fault_event
      INNER JOIN sd_fault ON
         sd_fault.fault_db_id = fault_event.event_db_id AND
         sd_fault.fault_id = fault_event.event_id
       INNER JOIN org_hr ON
          org_hr.hr_db_id = sd_fault.found_by_hr_db_id AND
          org_hr.hr_id = sd_fault.Found_By_Hr_Id
       INNER JOIN sched_stask ON
          sched_stask.fault_db_id = sd_fault.fault_db_id AND
          sched_stask.fault_id = sd_fault.fault_id
       INNER JOIN evt_event_rel ON
          fault_event.event_db_id = evt_event_rel.rel_event_db_id AND
          fault_event.event_id = evt_event_rel.rel_event_id
       WHERE
           evt_event_rel.rel_type_cd='RECUR'
         AND NOT EXISTS
         (
           SELECT 1
           FROM evt_event_rel
           WHERE
               fault_event.event_db_id = evt_event_rel.event_db_id AND
               fault_event.event_id = evt_event_rel.event_id AND
               evt_event_rel.rel_type_cd='RECUR' )
   UNION ALL
    ---select all recurring faults that originate from the initial faults
    SELECT
      recur_fault.fault_name,
      recur_fault.corr_task_barcode,
      recur_fault.corr_task_id,
      recur_fault.fault_id,
      recur_fault.prev_fault_id,
      tree_fault.init_fault_id,
      recur_fault.found_by_hr_id
    FROM
     (
       SELECT
         fault_event.event_sdesc AS fault_name,
         sd_fault.alt_id AS fault_id,
         corr_sched_stask.barcode_sdesc AS corr_task_barcode,
         corr_sched_stask.alt_id AS corr_task_id,
         prev_sd_fault.alt_id AS prev_fault_id,
         org_hr.alt_id AS found_by_hr_id
       FROM
         evt_event fault_event
        INNER JOIN evt_event_rel ON
           fault_event.event_db_id = evt_event_rel.event_db_id AND
           fault_event.event_id = evt_event_rel.event_id
        INNER JOIN sd_fault ON
           sd_fault.fault_db_id = fault_event.event_db_id AND
           sd_fault.fault_id = fault_event.event_id
        INNER JOIN org_hr ON
           org_hr.hr_db_id = sd_fault.found_by_hr_db_id AND
           org_hr.hr_id = sd_fault.Found_By_Hr_Id
        INNER JOIN sched_stask corr_sched_stask ON
           corr_sched_stask.fault_db_id = sd_fault.fault_db_id AND
           corr_sched_stask.fault_id = sd_fault.fault_id
        INNER JOIN evt_event_rel prev_fault_event_rel ON
           fault_event.event_db_id = prev_fault_event_rel.event_db_id AND
           fault_event.event_id = prev_fault_event_rel.event_id
        INNER JOIN sd_fault prev_sd_fault ON
           prev_sd_fault.fault_db_id = prev_fault_event_rel.rel_event_db_id AND
           prev_sd_fault.fault_id = prev_fault_event_rel.rel_event_id
       WHERE
          evt_event_rel.rel_type_cd='RECUR'
        )
         recur_fault
       INNER JOIN rvw_fault tree_fault ON
        recur_fault.prev_fault_id = tree_fault.fault_id
 )
 SELECT
   fault_name,
   corr_task_barcode,
   corr_task_id,
   fault_id,
   prev_fault_id,
   init_fault_id,
   found_by_hr_id
 FROM
   rvw_fault
;