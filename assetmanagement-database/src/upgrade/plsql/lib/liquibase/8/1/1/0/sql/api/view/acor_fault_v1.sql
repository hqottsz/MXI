--liquibase formatted sql


--changeSet acor_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_v1
AS
SELECT
   sd_fault.alt_id              AS fault_id,
   inv_acft.alt_id              AS aircraft_id,
   sched_stask.alt_id           AS sched_id,
   org_hr.alt_id                AS found_by_user_id,
   inv_ac_reg.ac_reg_cd         AS registration_code,
   event_fault.event_sdesc      AS fault_name,
  REGEXP_REPLACE(REPLACE(event_fault.event_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')   AS fault_description,
   evt_sched_dead.sched_dead_dt AS due_date,
   event_task.sched_start_dt    AS plan_date,
   event_fault.event_status_cd  AS status,
   sd_fault.fail_sev_cd         AS severity_code,
   ref_fail_defer.fail_defer_cd AS deferral_code,
   fail_defer_ref.defer_ref_ldesc   AS deferral_description,
   CASE event_fault.event_status_cd 
     WHEN 'CFDEFER' THEN
         defer_stage.stage_reason_cd      
   END                          AS deferral_reason,  
   sd_fault.defer_ref_sdesc     AS deferral_ref_name,
   sched_stask.barcode_sdesc    AS barcode
FROM
   sd_fault
   INNER JOIN evt_event event_fault ON
      sd_fault.fault_db_id = event_fault.event_db_id AND
      sd_fault.fault_id    = event_fault.event_id
   INNER JOIN org_hr ON
      sd_fault.found_by_hr_db_id = org_hr.hr_db_id AND
      sd_fault.found_by_hr_id    = org_hr.hr_id
   -- corrective task
   INNER JOIN sched_stask ON 
      sd_fault.fault_db_id = sched_stask.fault_db_id AND
      sd_fault.fault_id    = sched_stask.fault_id
   INNER JOIN evt_event event_task ON
      sched_stask.sched_db_id = event_task.event_db_id AND
      sched_stask.sched_id    = event_task.event_id
   -- component
   INNER JOIN inv_inv ON 
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id 
   -- aircraft
   LEFT JOIN inv_ac_reg ON 
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   LEFT JOIN inv_inv inv_acft ON
      inv_ac_reg.inv_no_db_id = inv_acft.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = inv_acft.inv_no_id   
   -- deadline
   LEFT JOIN evt_sched_dead ON
      event_task.event_db_id = evt_sched_dead.event_db_id AND
      event_task.event_id    = evt_sched_dead.event_id
      AND
      evt_sched_dead.sched_driver_bool = 1
   -- deferral
   LEFT JOIN ref_fail_defer ON
      sd_fault.fail_defer_db_id = ref_fail_defer.fail_defer_db_id AND
      sd_fault.fail_defer_cd    = ref_fail_defer.fail_defer_cd    
   -- deferral description 
   LEFT JOIN fail_defer_ref ON 
      sd_fault.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND
      sd_fault.fail_defer_cd    = fail_defer_ref.fail_defer_cd    
      AND -- filter with assembly as well
      fail_defer_ref.assmbl_db_id = inv_inv.assmbl_db_id AND
      fail_defer_ref.Assmbl_cd    = inv_inv.Assmbl_cd                
      AND
      sd_fault.defer_ref_sdesc  = fail_defer_ref.defer_ref_sdesc        
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
      sd_fault.fault_db_id = defer_stage.event_db_id AND
      sd_fault.fault_id    = defer_stage.event_id      
WHERE
   event_fault.event_type_db_id = 0 AND
   event_fault.event_type_cd    = 'CF'
;     