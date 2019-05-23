--liquibase formatted sql


--changeSet acor_repetitive_faults_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_repetitive_faults_v1
AS 
WITH
rvw_rep_fault
   (
     fault_db_id,
     fault_id,
     prev_fault_db_id ,
     prev_fault_id,
     init_fault_db_id,
     init_fault_id
   )
AS
   (
      SELECT
        rel_event_db_id fault_db_id,
        rel_event_id    fault_id,
        rel_event_db_id prev_fault_db_id ,
        rel_event_id    prev_fault_id,
        rel_event_db_id init_fault_db_id,
        rel_event_id    init_fault_id
      FROM
         evt_event_rel
      WHERE
         evt_event_rel.rel_type_cd = 'RECUR'
         AND
         NOT EXISTS
         (
           SELECT
              1
           FROM
              evt_event_rel inner_rel
           WHERE
              evt_event_rel.rel_event_db_id = inner_rel.event_db_id AND
              evt_event_rel.rel_event_id    = inner_rel.event_id AND
              inner_rel.rel_type_cd = 'RECUR'
         )
      UNION ALL
      SELECT
        c_fault.event_db_id,
        c_fault.event_id,
        c_fault.rel_event_db_id,
        c_fault.rel_event_id,
        rvw_rep_fault.init_fault_db_id,
        rvw_rep_fault.init_fault_id
      FROM
         evt_event_rel c_fault
         INNER JOIN rvw_rep_fault ON
            c_fault.rel_event_db_id = rvw_rep_fault.fault_db_id AND
            c_fault.rel_event_id    = rvw_rep_fault.fault_id
      WHERE
         c_fault.rel_type_cd = 'RECUR'
   ),
rvw_rep_fault_hierarchy
AS
   (
      SELECT
        fault_db_id,
        fault_id,
        CASE
           WHEN prev_fault_id <> fault_id THEN
              prev_fault_db_id
        END prev_fault_db_id,
        CASE
           WHEN prev_fault_id <> fault_id THEN
              prev_fault_id
        END prev_fault_id,
        init_fault_db_id,
        init_fault_id
      FROM
         rvw_rep_fault
   ),
rvw_sd_fault
AS
   (
     SELECT
        alt_id,
        fault_db_id,
        fault_id,
        found_by_hr_db_id,
        found_by_hr_id,
        fail_defer_db_id,
        fail_defer_cd,
        defer_ref_sdesc  
     FROM
       sd_fault
   )
SELECT
   evt_event.event_sdesc         AS fault_name,
   fault_corr_task.barcode_sdesc AS corr_task_barcode,
   fault_corr_task.alt_id        AS corr_task_id,
   fault.alt_id                  AS fault_id,
   prev_fault.alt_id             AS prev_fault_id,
   init_fault.alt_id             AS init_fault_id,
   org_hr.alt_id                 AS found_by_hr_id,
   fail_defer_ref.defer_ref_ldesc   AS deferral_description,
   CASE evt_event.event_status_cd 
     WHEN 'CFDEFER' THEN
         defer_stage.stage_reason_cd      
   END                             AS deferral_reason     
FROM
  rvw_rep_fault_hierarchy fault_hierarchy
  INNER JOIN rvw_sd_fault fault ON
     fault_hierarchy.fault_db_id = fault.fault_db_id AND
     fault_hierarchy.fault_id    = fault.fault_id
  INNER JOIN evt_event ON
     fault_hierarchy.fault_db_id = evt_event.event_db_id AND
     fault_hierarchy.fault_id    = evt_event.event_id
  -- found by hr
  INNER JOIN org_hr ON
     fault.found_by_hr_db_id = org_hr.hr_db_id AND
     fault.found_by_hr_id    = org_hr.hr_id
  -- fault corrective task
  INNER JOIN sched_stask fault_corr_task ON
     fault_hierarchy.fault_db_id = fault_corr_task.fault_db_id AND
     fault_hierarchy.fault_id    = fault_corr_task.fault_id
  INNER JOIN inv_inv ON 
     fault_corr_task.main_inv_no_db_id = inv_inv.inv_no_db_id AND
     fault_corr_task.main_inv_no_id    = inv_inv.inv_no_id
  -- initial fault
  INNER JOIN rvw_sd_fault init_fault ON
     fault_hierarchy.init_fault_db_id = init_fault.fault_db_id AND
     fault_hierarchy.init_fault_id    = init_fault.fault_id
  -- previous fault
  LEFT JOIN rvw_sd_fault prev_fault ON
     fault_hierarchy.prev_fault_db_id = prev_fault.fault_db_id AND
     fault_hierarchy.prev_fault_id    = prev_fault.fault_id
  -- deferral description 
   LEFT JOIN fail_defer_ref ON 
      init_fault.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND
      init_fault.fail_defer_cd    = fail_defer_ref.fail_defer_cd  
      AND -- filter with assembly as well
      fail_defer_ref.assmbl_db_id = inv_inv.assmbl_db_id AND
      fail_defer_ref.Assmbl_cd    = inv_inv.Assmbl_cd           
      AND
      init_fault.defer_ref_sdesc  = fail_defer_ref.defer_ref_sdesc           
   -- defferal reason
   LEFT JOIN (
               SELECT
                  evt_stage.event_db_id,
                  evt_stage.event_id,
                  stage_reason_cd
               FROM
                  evt_stage
                  INNER JOIN (
                              SELECT
                                 event_db_id,
                                 event_id,
                                 MAX(stage_id) stage_id
                              FROM
                                 evt_stage
                              WHERE
                                 event_status_db_id = 0 AND
                                 event_status_cd = 'CFDEFER'
                              GROUP BY
                                 event_db_id,
                                 event_id
                             ) max_stage ON
                     evt_stage.event_db_id = max_stage.event_db_id AND
                     evt_stage.event_id    = max_stage.event_id    AND
                     evt_stage.stage_id    = max_stage.stage_id
             ) defer_stage ON 
      init_fault.fault_db_id = defer_stage.event_db_id AND
      init_fault.fault_id    = defer_stage.event_id        
;