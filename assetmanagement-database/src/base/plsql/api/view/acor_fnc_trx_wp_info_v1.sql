--liquibase formatted sql


--changeSet acor_fnc_trx_wp_info_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_FNC_TRX_WP_INFO_V1 AS
SELECT
   -- NOTE: If column is changed here, make sure you make the same changes in acor_fnc_trx_wp_ac_info_v1
   xaction_issue_xfer.transaction_id,
   xaction_issue_xfer.transaction_type,
   DECODE(xaction_issue_xfer.transaction_type, 'TURN IN', null, req_part.alt_id) AS part_request_id,
   task.alt_id          AS task_id,
   wp.alt_id            AS wp_id,
   wp.wo_ref_sdesc      AS work_package_number,
   acft_inv.alt_id      AS aircraft_id,
   inv_ac_reg.ac_reg_cd AS registration_code
FROM
   -----------------------------------------------------------------------
   -- Get work package, task and part request from financial transaction
   -----------------------------------------------------------------------
   (
      -- get issue xfer from turnin xaction
      SELECT
         fnc_xaction_log.alt_id          AS transaction_id,
         fnc_xaction_log.xaction_type_cd AS transaction_type,
         -- get issue xfer from turnin xfer (adhoc turnin has no
         -- corresponding part installation with issue xfer)
         turnin_xfer.init_event_db_id    AS issue_xfer_db_id,
         turnin_xfer.init_event_id       AS issue_xfer_id
      FROM
         fnc_xaction_log
         -- get turnin xfer from fnc_xaction_log
         INNER JOIN inv_xfer turnin_xfer ON
         (
            fnc_xaction_log.event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.event_id    = turnin_xfer.xfer_id
         ) OR
         (
            fnc_xaction_log.ac_event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.ac_event_id    = turnin_xfer.xfer_id
         )
      WHERE
         fnc_xaction_log.xaction_type_db_id = 0 AND
         fnc_xaction_log.xaction_type_cd = 'TURN IN'
      UNION ALL
      -- get issue xfer from issue xaction
      SELECT
         alt_id          AS transaction_id,
         xaction_type_cd AS transaction_type,
         event_db_id     AS issue_xfer_db_id,
         event_id        AS issue_xfer_id
      FROM
         fnc_xaction_log
      WHERE
         xaction_type_db_id = 0 AND
         xaction_type_cd = 'ISSUE'
   ) xaction_issue_xfer
   INNER JOIN inv_xfer issue_xfer ON
      xaction_issue_xfer.issue_xfer_db_id = issue_xfer.xfer_db_id AND
      xaction_issue_xfer.issue_xfer_id    = issue_xfer.xfer_id
   -- get req_part from issue xfer
   INNER JOIN req_part ON
      issue_xfer.init_event_db_id = req_part.req_part_db_id AND
      issue_xfer.init_event_id    = req_part.req_part_id
   -- get task from req_part
   INNER JOIN sched_stask task ON
      req_part.sched_db_id   = task.sched_db_id AND
      req_part.sched_id      = task.sched_id
   -- get task event from task
   INNER JOIN evt_event task_evt ON
      task.sched_db_id = task_evt.event_db_id AND
      task.sched_id    = task_evt.event_id
   -- get work package from task event
   INNER JOIN sched_stask wp ON
      task_evt.h_event_db_id = wp.sched_db_id AND
      task_evt.h_event_id    = wp.sched_id
   -----------------------------------------------------------------------
   -- Get aircraft inv info from req_part for a aircraft work package task
   -----------------------------------------------------------------------
   LEFT JOIN inv_ac_reg ON
      req_part.req_ac_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      req_part.req_ac_inv_no_id    = inv_ac_reg.inv_no_id
   LEFT JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
      ;