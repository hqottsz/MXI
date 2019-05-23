--liquibase formatted sql


--changeSet acor_assy_wp_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assy_wp_v1
AS 
SELECT
    sched_stask.alt_id            AS sched_id,
    sched_stask.wo_ref_sdesc      AS work_package_number,
    sched_stask.barcode_sdesc     AS barcode,
    evt_event.sched_start_dt      AS schedule_start_date,
    evt_event.sched_end_dt        AS schedule_end_date,
    evt_event.actual_start_dt     AS actual_start_date,
    evt_event.event_dt            AS actual_end_date,
    evt_event.event_sdesc         AS work_package_name,
    evt_event.doc_ref_sdesc       AS doc_reference_name,    
    fnc_account.alt_id            AS account_id,
    fnc_account.account_cd        AS issue_to_account,
    sched_stask.heavy_bool        AS maintenance_flag,
    inv_loc.alt_id                AS location_id,
    inv_loc.loc_cd                AS location_code,
    -- work types
    CONCAT_DATA(CURSOR(SELECT
                          work_type_cd
                       FROM
                          sched_work_type
                       WHERE
                          sched_work_type.sched_db_id = sched_stask.sched_db_id AND
                          sched_work_type.sched_id    = sched_stask.sched_id
                      )
                 )                AS work_type_list,
    inv_inv.alt_id                AS assembly_id,
    inv_inv.serial_no_oem         AS assembly_serial_number,
    eqp_part_no.part_no_oem       AS assembly_part_number,
    eqp_part_no.manufact_cd       AS assembly_manufacturer,
    evt_event.event_status_cd     AS status,
    po_header.alt_id              AS repair_order_id,
    --
    hr_usg.tsn hours_tsn_completion,
    hr_usg.tso hours_ts0_completion,
    cyc_usg.tsn cycles_tsn_completion,
    cyc_usg.tso cycles_ts0_completion
FROM
   sched_stask
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   -- issue to account
   LEFT JOIN fnc_account ON
      sched_stask.issue_account_db_id = fnc_account.account_db_id AND
      sched_stask.issue_account_id    = fnc_account.account_id
   -- assebly
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN eqp_part_no ON
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
   LEFT JOIN evt_loc ON
      evt_event.event_db_id = evt_loc.event_db_id AND
      evt_event.event_id    = evt_loc.event_id
   LEFT JOIN inv_loc ON
      evt_loc.loc_db_id = inv_loc.loc_db_id AND
      evt_loc.loc_id    = inv_loc.loc_id
   LEFT JOIN po_line ON
      sched_stask.sched_db_id = po_line.sched_db_id AND
      sched_stask.sched_id    = po_line.sched_id
   LEFT JOIN po_header ON
      po_line.po_db_id = po_header.po_db_id AND
      po_line.po_id    = po_header.po_id
   --
   -- completion usage hours
   LEFT JOIN acor_acft_usg_wp_completion_v1 hr_usg ON
      sched_stask.alt_id = hr_usg.sched_id AND
      hr_usg.data_type_code = 'HOURS'
   -- completion usage cycles
   LEFT JOIN acor_acft_usg_wp_completion_v1 cyc_usg ON
      sched_stask.alt_id = cyc_usg.sched_id AND
      cyc_usg.data_type_code = 'CYCLES'
WHERE
   sched_stask.wo_ref_sdesc IS NOT NULL
   AND
   sched_stask.task_class_cd IN ('CHECK','RO')
   AND
   inv_inv.inv_class_cd = 'ASSY'
;