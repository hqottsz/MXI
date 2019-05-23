--liquibase formatted sql


--changeSet acor_fnc_trx_wp_ac_info_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fnc_trx_wp_ac_info_v1
AS 
WITH rvw_evt_event
AS (
   SELECT
      event_db_id,
      event_id,
      h_event_db_id,
      h_event_id,
      event_type_cd
   FROM
      evt_event
),
rvw_inv_inv
AS (
   SELECT
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id,
      inv_inv.serial_no_oem AS oem_serial_number,
      eqp_part_no.part_no_oem AS part_number,
      inv_inv.batch_no_oem AS oem_batch_number,
      eqp_part_no.manufact_cd AS manufacturer_code,
      inv_inv.po_db_id,
      inv_inv.po_id,
      inv_inv.po_line_id
   FROM
      inv_inv
      INNER JOIN eqp_part_no ON
         inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
         inv_inv.part_no_id    = eqp_part_no.part_no_id
)
SELECT
   wp.wo_ref_sdesc      AS work_package_number,
   wp.alt_id AS wp_id,
   log.XACTION_TYPE_CD  AS Transaction_type,
   inv_ac_reg.ac_reg_cd AS registration_code,
   inv_inv.alt_id AS aircraft_id,
   log.alt_id AS Transaction_id
FROM
   sched_stask wp
   INNER JOIN rvw_evt_event event ON
      wp.sched_db_id = event.H_EVENT_DB_ID AND
      wp.sched_id    = event.H_EVENT_ID
   INNER JOIN req_part rp ON
      event.EVENT_DB_ID = rp.SCHED_DB_ID AND
      event.EVENT_ID    = rp.SCHED_ID
   INNER JOIN inv_xfer ix ON
      rp.REQ_PART_DB_ID = ix.INIT_EVENT_DB_ID AND
      rp.REQ_PART_ID    = ix.INIT_EVENT_ID
   INNER JOIN rvw_evt_event part_req_evt ON
      part_req_evt.EVENT_DB_ID = ix.INIT_EVENT_DB_ID AND
      part_req_evt.EVENT_ID    = ix.INIT_EVENT_ID AND
      part_req_evt.EVENT_TYPE_CD = 'PR'
   INNER JOIN fnc_xaction_log log ON
      ix.XFER_DB_ID = log.EVENT_DB_ID AND
      ix.XFER_ID    = log.EVENT_ID
   LEFT JOIN inv_ac_reg ON
      rp.req_ac_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      rp.req_ac_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv ON
      inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
WHERE
   log.XACTION_TYPE_CD = 'ISSUE'
UNION
SELECT
   wp.wo_ref_sdesc AS work_package_number,
   wp.alt_id AS wp_id,
   log.XACTION_TYPE_CD AS Transaction_type,
   inv_ac_reg.ac_reg_cd AS registration_code,
   inv_inv.alt_id AS aircraft_id,
   log.alt_id AS Transaction_id
FROM
   fnc_xaction_log log
   INNER JOIN rvw_inv_inv inv ON
      log.INV_NO_DB_ID = inv.inv_no_db_id AND
      log.INV_NO_ID    = inv.inv_no_id
   INNER JOIN req_part rp ON
      inv.po_db_id   = rp.PO_DB_ID AND
      inv.po_id      = rp.PO_ID AND
      inv.po_line_id = rp.PO_LINE_ID
   LEFT JOIN inv_ac_reg ON
      rp.req_ac_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      rp.req_ac_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv ON
      inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN rvw_evt_event event ON
      rp.SCHED_DB_ID = event.event_db_id AND
      rp.SCHED_ID    = event.event_id
   INNER JOIN sched_stask wp ON
      event.H_EVENT_DB_ID = wp.sched_db_id AND
      event.H_EVENT_ID    = wp.sched_id
WHERE
   log.XACTION_TYPE_CD = 'TURN IN';