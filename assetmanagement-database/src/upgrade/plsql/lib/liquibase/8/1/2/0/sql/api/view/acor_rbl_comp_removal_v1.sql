--liquibase formatted sql


--changeSet acor_rbl_comp_removal_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_RBL_COMP_REMOVAL_V1 AS
SELECT
   inv_acft.alt_id                  AS aircraft_id,
   inv_inv.alt_id                   AS inventory_id,
   sched_stask.alt_id               AS sched_id,
   eqp_assmbl_bom.alt_id            AS config_slot_id,
   sched_rmvd_part.sched_part_id,
   sched_rmvd_part.sched_rmvd_part_id,
   inv_acft.assmbl_cd               AS fleet_type,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   inv_acft.serial_no_oem           AS acft_serial_number,
   eqp_assmbl_bom.bom_class_cd,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot,
   -- derived:
   NVL(acor_rbl_acft_part_qty_v1.acft_part_qty,0) AS quantity_per_acft,
   --
   eqp_part_no.manufact_cd          AS manufacturer_code,
   eqp_part_no.part_no_oem          AS part_number,
   eqp_part_no.part_no_sdesc        AS part_name,
   sched_rmvd_part.serial_no_oem    AS removed_serial_number,
   eqp_part_no.inv_class_cd         AS inventory_class,
   sched_rmvd_part.remove_reason_cd AS removal_reason,
   evt_event.event_dt               AS removal_date,
   TO_NUMBER(TO_CHAR(evt_event.event_dt,'YYYYMM')) AS year_month,
   CASE -- based on spec2000 standard
      WHEN ref_remove_reason.spec2k_remove_reason_cd = 'U' THEN
         'U' -- unscheduled
      ELSE
         'S' -- scheduled
   END removal_type,
   CASE
      WHEN ref_remove_reason.spec2k_remove_reason_cd = 'U' AND fault_event.event_status_cd = 'CFCERT' THEN
         1
      ELSE
         0
   END justified_failure,
   evt_event.event_type_cd,
   evt_event.event_status_cd
FROM
   sched_rmvd_part
   -- remove component
   INNER JOIN inv_inv ON
      sched_rmvd_part.inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_rmvd_part.inv_no_id    = inv_inv.inv_no_id
   -- removal reasion
   INNER JOIN ref_remove_reason ON
      sched_rmvd_part.remove_reason_db_id = ref_remove_reason.remove_reason_db_id AND
      sched_rmvd_part.remove_reason_cd    = ref_remove_reason.remove_reason_cd
   -- part
   INNER JOIN eqp_part_no ON
      sched_rmvd_part.part_no_db_id = eqp_part_no.part_no_db_id AND
      sched_rmvd_part.part_no_id    = eqp_part_no.part_no_id
   -- components needed
   INNER JOIN sched_part ON
      sched_part.sched_db_id   = sched_rmvd_part.sched_db_id   AND
      sched_part.sched_id      = sched_rmvd_part.sched_id      AND
      sched_part.sched_part_id = sched_rmvd_part.sched_part_id
   -- task
   INNER JOIN evt_event ON
      sched_rmvd_part.sched_db_id = evt_event.event_db_id AND
      sched_rmvd_part.sched_id    = evt_event.event_id
   -- event highest inventory
   INNER JOIN evt_inv ON
      evt_event.event_db_id  = evt_inv.event_db_id AND
      evt_event.event_id     = evt_inv.event_id
      AND
      evt_inv.main_inv_bool = 1
   -- aircraft using the highest event inventory
   INNER JOIN inv_inv inv_acft ON
      evt_inv.h_inv_no_db_id = inv_acft.inv_no_db_id AND
      evt_inv.h_inv_no_id    = inv_acft.inv_no_id
      AND
      inv_acft.inv_class_cd = 'ACFT'
   INNER JOIN inv_ac_reg ON
      inv_acft.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_acft.inv_no_id    = inv_ac_reg.inv_no_id
   -- config slot
   INNER JOIN eqp_assmbl_bom ON
      sched_part.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      sched_part.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
      sched_part.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
   -- task
   INNER JOIN sched_stask ON
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id    = sched_stask.sched_id
   -- join to fault - check if it is a justified failure
   LEFT JOIN evt_event fault_event ON
      sched_stask.fault_db_id = fault_event.event_db_id AND
      sched_stask.fault_id    = fault_event.event_id
      AND
      fault_event.event_type_db_id = 0 AND
      fault_event.event_type_cd    = 'CF'
   -- part quantity
   LEFT JOIN ( -- done as a workaround for part that
               -- have multiple part group
               SELECT
                  assmbl_cd,
                  manufact_cd,
                  part_no_oem,
                  acft_part_qty
               FROM
                 acor_rbl_acft_part_qty_v1
               GROUP BY
                  assmbl_cd,
                  manufact_cd,
                  part_no_oem,
                  acft_part_qty
             ) acor_rbl_acft_part_qty_v1 ON
     acor_rbl_acft_part_qty_v1.assmbl_cd   = inv_acft.assmbl_cd AND
     acor_rbl_acft_part_qty_v1.manufact_cd = eqp_part_no.manufact_cd AND
     acor_rbl_acft_part_qty_v1.part_no_oem = eqp_part_no.part_no_oem
WHERE
   evt_event.event_type_db_id = 0 AND
   evt_event.event_type_cd    = 'TS'
   AND
   evt_event.event_status_db_id = 0 AND
   evt_event.event_status_cd    = 'COMPLETE';