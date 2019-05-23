--liquibase formatted sql


--changeSet acor_acft_wp_parts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_wp_parts_v1
AS 
WITH
rvw_inv_part
AS
   (
     SELECT
        inv_inv.inv_no_db_id,
        inv_inv.inv_no_id,
        parts.part_no_db_id,
        parts.part_no_id,
        parts.part_no_oem AS part_number,
        inv_inv.serial_no_oem,
        inv_inv.alt_id,
        inv_inv.barcode_sdesc,
        inv_inv.loc_db_id,
        inv_inv.loc_id
     FROM
        inv_inv
        INNER JOIN eqp_part_no parts ON
           inv_inv.part_no_db_id = parts.part_no_db_id AND
           inv_inv.part_no_id    = parts.part_no_id
   )
SELECT
    wp_workscope.wo_sched_id,
    wp_workscope.sched_id,
    eqp_bom_part.bom_part_cd          AS part_group_code,
    rmvd_inv.part_number              AS removal_part_number,
    rmvd_inv.serial_no_oem            AS removal_serial_number,
    rmvd_inv.alt_id                   AS removal_inventory_id,
    rmvd_inv.barcode_sdesc            AS removal_inventory_barcode,
    sched_rmvd_part.rmvd_qt           AS removal_quantity,
    eqp_assmbl_pos.eqp_pos_cd         AS position_code,
    sched_rmvd_part.remove_reason_cd  AS remove_reason_code,
    inst_inv.part_number              AS installation_part_number,
    inst_inv.serial_no_oem            AS installation_serial_number,
    inst_inv.alt_id                   AS installation_inventory_id,
    inst_inv.barcode_sdesc            AS installation_inventory_barcode,
    inst_inv_loc.loc_name             AS installation_location,
    sched_inst_part.inst_qt           AS installation_quantity,
    req_part.alt_id                   AS part_request_id,
    evt_event.event_sdesc             AS part_request_barcode,
    order_event.event_sdesc           AS order_number,
    po_header.alt_id                  AS order_id
FROM
   acor_acft_wp_workscope_v1 wp_workscope
   INNER JOIN sched_stask ON
      wp_workscope.sched_id = sched_stask.alt_id
   INNER JOIN sched_part ON
      sched_stask.sched_db_id = sched_part.sched_db_id AND
      sched_stask.sched_id    = sched_part.sched_id
   -- part group
   INNER JOIN eqp_bom_part ON
      sched_part.sched_bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      sched_part.sched_bom_part_id    = eqp_bom_part.bom_part_id
   -- position
   LEFT JOIN eqp_assmbl_pos ON
      sched_part.assmbl_db_id  = eqp_assmbl_pos.assmbl_db_id  AND
      sched_part.assmbl_cd     = eqp_assmbl_pos.assmbl_cd     AND
      sched_part.assmbl_bom_id = eqp_assmbl_pos.assmbl_bom_id AND
      sched_part.assmbl_pos_id = eqp_assmbl_pos.assmbl_pos_id
   -- sched removal part
   LEFT JOIN sched_rmvd_part ON
      sched_part.sched_db_id = sched_rmvd_part.sched_db_id AND
      sched_part.sched_id    = sched_rmvd_part.sched_id AND
      sched_part.sched_part_id = sched_rmvd_part.sched_part_id
   LEFT JOIN rvw_inv_part rmvd_inv ON
      sched_rmvd_part.inv_no_db_id  = rmvd_inv.inv_no_db_id AND
      sched_rmvd_part.inv_no_id     = rmvd_inv.inv_no_id
      AND
      sched_rmvd_part.part_no_db_id = rmvd_inv.part_no_db_id AND
      sched_rmvd_part.part_no_id    = rmvd_inv.part_no_id
   -- sched installation part
   LEFT JOIN sched_inst_part ON
       sched_part.sched_db_id = sched_inst_part.sched_db_id AND
      sched_part.sched_id    = sched_inst_part.sched_id AND
      sched_part.sched_part_id = sched_inst_part.sched_part_id
   LEFT JOIN rvw_inv_part inst_inv ON
      sched_inst_part.inv_no_db_id  = inst_inv.inv_no_db_id AND
      sched_inst_part.inv_no_id     = inst_inv.inv_no_id
      AND
      sched_inst_part.part_no_db_id = inst_inv.part_no_db_id AND
      sched_inst_part.part_no_id    = inst_inv.part_no_id
   -- installation inventory location
   LEFT JOIN inv_loc inst_inv_loc ON
      inst_inv.loc_db_id = inst_inv_loc.loc_db_id AND
      inst_inv.loc_id    = inst_inv_loc.loc_id
   -- part request
   LEFT JOIN req_part ON
      sched_inst_part.sched_db_id        = req_part.sched_db_id AND
      sched_inst_part.sched_id           = req_part.sched_id    AND
      sched_inst_part.sched_part_id      = req_part.sched_part_id AND
      sched_inst_part.sched_inst_part_id = req_part.sched_inst_part_id
   LEFT JOIN evt_event ON
      evt_event.event_db_id = req_part.req_part_db_id AND
      evt_event.event_id    = req_part.req_part_id
   LEFT JOIN po_header ON
      req_part.po_db_id = po_header.po_db_id AND
      req_part.po_id    = po_header.po_id
   LEFT JOIN evt_event order_event ON
      po_header.po_db_id = order_event.event_db_id AND
      po_header.po_id    = order_event.event_id
;