--liquibase formatted sql


--changeSet acor_rbl_pirepmarep_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rbl_pirepmarep_v1 
AS
SELECT
   inv_acft.alt_id            AS aircraft_id,
   inv_inv.alt_id             AS inventory_id,
   eqp_assmbl_bom.alt_id      AS config_slot_id,
   sd_fault.alt_id            AS fault_id,
   sd_fault.leg_id,
   inv_acft.assmbl_cd,
   SUBSTR(eqp_assmbl_bom.assmbl_bom_cd,1,2) config_slot_code,
   NVL(sd_fault.fault_source_cd,'UNKNOWN') fault_source_cd,
   sd_fault.fail_sev_cd,
   fault_event.actual_start_dt,
   TO_NUMBER(TO_CHAR(fault_event.actual_start_dt,'YYYYMM')) AS year_month
FROM
   sd_fault
   -- found on date
   INNER JOIN evt_event fault_event on
      sd_fault.fault_db_id = fault_event.event_db_id AND
      sd_fault.fault_id    = fault_event.event_id
   -- inventory
   INNER JOIN evt_inv ON
      fault_event.event_db_id = evt_inv.event_db_id AND
      fault_event.event_id    = evt_inv.event_id
      AND
      evt_inv.main_inv_bool = 1
   -- aircraft
   INNER JOIN inv_inv ON
      evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
      evt_inv.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv inv_acft ON
      inv_ac_reg.inv_no_db_id = inv_acft.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = inv_acft.inv_no_id
   -- config slot
   INNER JOIN eqp_assmbl_bom ON
      evt_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      evt_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      evt_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   sched_stask
                   INNER JOIN evt_event ON
                      sched_stask.sched_db_id = evt_event.event_db_id AND
                      sched_stask.sched_id    = evt_event.event_id
                WHERE
                   sched_stask.fault_db_id = sd_fault.fault_db_id AND
                   sched_stask.fault_id    = sd_fault.fault_id
                   AND
                   evt_event.event_type_db_id = 0 AND
                   evt_event.event_type_cd    = 'TS'
                   AND
                   evt_event.event_status_db_id = 0 AND
                   evt_event.event_status_cd    = 'ERROR'
               )
;