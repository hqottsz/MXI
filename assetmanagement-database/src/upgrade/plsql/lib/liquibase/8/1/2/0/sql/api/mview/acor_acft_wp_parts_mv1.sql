--liquibase formatted sql


--changeSet acor_acft_wp_parts_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('ACOR_ACFT_WP_PARTS_MV1');
END;
/

--changeSet acor_acft_wp_parts_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW acor_acft_wp_parts_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH
rvw_inv 
AS 
   (
     SELECT
        alt_id,
        inv_no_db_id,
        inv_no_id,
        h_inv_no_db_id,
        h_inv_no_id,
        loc_db_id,
        loc_id,
        barcode_sdesc
     FROM 
        inv_inv 
   ),
rvw_part
AS 
   (
     SELECT
        part_no_db_id,
        part_no_id,
        part_no_oem
     FROM 
        eqp_part_no
   )   
SELECT
    wp_stask.alt_id                   AS wo_sched_id,
    sched_stask.alt_id                AS sched_id,
    inv_acft.alt_id                   AS aircraft_id,
    inv_ac_reg.ac_reg_cd              AS registration_code,
    eqp_bom_part.bom_part_cd          AS part_group_code,
    rmvd_part.part_no_oem             AS removal_part_number,
    sched_rmvd_part.serial_no_oem     AS removal_serial_number,
    rmvd_inv.alt_id                   AS removal_inventory_id,
    rmvd_inv.barcode_sdesc            AS removal_inventory_barcode,
    sched_rmvd_part.rmvd_qt           AS removal_quantity,
    eqp_assmbl_pos.eqp_pos_cd         AS position_code,
    sched_rmvd_part.remove_reason_cd  AS remove_reason_code,
    inst_part.part_no_oem             AS installation_part_number,
    sched_inst_part .serial_no_oem    AS installation_serial_number,
    inst_inv.alt_id                   AS installation_inventory_id,
    inst_inv.barcode_sdesc            AS installation_inventory_barcode,
    inst_inv_loc.loc_cd               AS installation_location,
    sched_inst_part.inst_qt           AS installation_quantity,
    req_part.alt_id                   AS part_request_id,
    po_event.event_sdesc              AS part_request_barcode,
    order_event.event_sdesc           AS order_number,
    po_header.alt_id                  AS order_id
FROM
   sched_stask
   INNER JOIN sched_part ON
      sched_stask.sched_db_id = sched_part.sched_db_id AND
      sched_stask.sched_id    = sched_part.sched_id
   -- workpackage
   INNER JOIN evt_event event_task ON 
      sched_stask.sched_db_id = event_task.event_db_id AND
      sched_stask.sched_id    = event_task.event_id 
      AND -- 
      event_task.event_status_cd = 'COMPLETE'
   INNER JOIN sched_stask wp_stask ON
      event_task.h_event_db_id = wp_stask.sched_db_id AND
      event_task.h_event_id    = wp_stask.sched_id
   -- aircraft
   INNER JOIN rvw_inv inv ON 
      wp_stask.main_inv_no_db_id = inv.inv_no_db_id AND
      wp_stask.main_inv_no_id    = inv.inv_no_id      
   INNER JOIN inv_ac_reg ON 
      inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv.h_inv_no_id    = inv_ac_reg.inv_no_id 
   INNER JOIN rvw_inv inv_acft ON 
      inv_ac_reg.inv_no_db_id = inv_acft.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = inv_acft.inv_no_id
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
      sched_part.sched_db_id   = sched_rmvd_part.sched_db_id AND
      sched_part.sched_id      = sched_rmvd_part.sched_id AND
      sched_part.sched_part_id = sched_rmvd_part.sched_part_id
   LEFT JOIN rvw_inv rmvd_inv ON
      sched_rmvd_part.inv_no_db_id  = rmvd_inv.inv_no_db_id AND
      sched_rmvd_part.inv_no_id     = rmvd_inv.inv_no_id
   LEFT JOIN rvw_part rmvd_part ON
      sched_rmvd_part.part_no_db_id = rmvd_part.part_no_db_id AND
      sched_rmvd_part.part_no_id    = rmvd_part.part_no_id
   -- sched installation part
   LEFT JOIN sched_inst_part ON
      sched_rmvd_part.sched_db_id   = sched_inst_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_inst_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_inst_part.sched_part_id
   LEFT JOIN rvw_inv inst_inv ON
      sched_inst_part.inv_no_db_id  = inst_inv.inv_no_db_id AND
      sched_inst_part.inv_no_id     = inst_inv.inv_no_id
   LEFT JOIN rvw_part inst_part ON
      sched_inst_part.part_no_db_id = inst_part.part_no_db_id AND
      sched_inst_part.part_no_id    = inst_part.part_no_id
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
   LEFT JOIN evt_event po_event ON
      po_event.event_db_id = req_part.req_part_db_id AND
      po_event.event_id    = req_part.req_part_id
   LEFT JOIN po_header ON
      req_part.po_db_id = po_header.po_db_id AND
      req_part.po_id    = po_header.po_id
   LEFT JOIN evt_event order_event ON
      po_header.po_db_id = order_event.event_db_id AND
      po_header.po_id    = order_event.event_id;
