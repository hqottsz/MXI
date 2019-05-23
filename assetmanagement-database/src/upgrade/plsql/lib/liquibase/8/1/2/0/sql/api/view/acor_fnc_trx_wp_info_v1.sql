--liquibase formatted sql


--changeSet acor_fnc_trx_wp_info_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_FNC_TRX_WP_INFO_V1 AS
SELECT
   fnc_xaction_log.alt_id          AS transaction_id,
   fnc_xaction_log.xaction_type_cd AS transaction_type,
   wp.alt_id                       AS wp_id,
   wp.wo_ref_sdesc                 AS work_package_number,
   task.alt_id                     AS task_id,
   req_part.alt_id                 AS part_request_id,
   acft_inv.alt_id                 AS aircraft_id,
   inv_ac_reg.ac_reg_cd            AS registration_code
FROM
   -----------------------------------------------------------------------
   -- Get work package, task and part request from financial transaction
   -----------------------------------------------------------------------
   fnc_xaction_log
   INNER JOIN
      -- get installed and removed part info including inv and sched part
      (
         SELECT 
           inv_no_db_id,
           inv_no_id,
           sched_db_id,
           sched_id,
           sched_part_id
         FROM
            sched_inst_part  
         UNION ALL  
         SELECT 
           inv_no_db_id,
           inv_no_id,         
           sched_db_id,
           sched_id,
           sched_part_id
         FROM
            sched_rmvd_part             
      ) sched_part_info ON
      sched_part_info.inv_no_db_id = fnc_xaction_log.inv_no_db_id AND
      sched_part_info.inv_no_id    = fnc_xaction_log.inv_no_id
   -- get part request from sched_part_info
   INNER JOIN req_part ON 
      req_part.sched_db_id   = sched_part_info.sched_db_id AND
      req_part.sched_id      = sched_part_info.sched_id AND
      req_part.sched_part_id = sched_part_info.sched_part_id         
   -- get task from sched_part_info
   INNER JOIN sched_stask task ON
      task.sched_db_id = sched_part_info.sched_db_id AND
      task.sched_id    = sched_part_info.sched_id
   -- get task event from task
   INNER JOIN evt_event task_evt ON
      task_evt.event_db_id = task.sched_db_id AND
      task_evt.event_id    = task.sched_id
   -- get work package from task event
   INNER JOIN sched_stask wp ON
      wp.sched_db_id = task_evt.h_event_db_id AND
      wp.sched_id    = task_evt.h_event_id
   -----------------------------------------------------------------------
   -- Get aircraft inv info from req_part for a aircraft work package task
   -----------------------------------------------------------------------
   LEFT JOIN inv_ac_reg ON
      inv_ac_reg.inv_no_db_id = req_part.req_ac_inv_no_db_id AND
      inv_ac_reg.inv_no_id    = req_part.req_ac_inv_no_id 
   LEFT JOIN inv_inv acft_inv ON
      acft_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      acft_inv.inv_no_id    = inv_ac_reg.inv_no_id   
WHERE
   fnc_xaction_log.xaction_type_db_id = 0 AND
   fnc_xaction_log.xaction_type_cd IN ('ISSUE', 'TURN IN');