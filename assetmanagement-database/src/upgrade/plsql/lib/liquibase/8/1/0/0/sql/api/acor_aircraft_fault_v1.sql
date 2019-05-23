--liquibase formatted sql


--changeSet acor_aircraft_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_aircraft_fault_v1
AS 
SELECT
   sd_fault.alt_id                  AS fault_id,
   evt_event.event_sdesc            AS fault_name,
   eqp_assmbl_bom.assmbl_bom_cd     AS chapter_code,
   REGEXP_REPLACE(REPLACE(evt_event.event_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')   AS fault_description,
   evt_event.actual_start_dt        AS found_on_date,
   ref_fail_sev.sev_type_cd         AS fault_severity,
   sd_fault.fail_sev_cd             AS deferral_class,
   acft_inv.alt_id                  AS aircraft_id,
   acft_inv.serial_no_oem           AS serial_number
FROM
   sd_fault
   -- found date
   INNER JOIN evt_event on
      sd_fault.fault_db_id = evt_event.event_db_id AND
      sd_fault.fault_id    = evt_event.event_id
   -- inventory
   INNER JOIN evt_inv ON
      evt_inv.event_db_id   = evt_event.event_db_id AND
      evt_inv.event_id      = evt_event.event_id    AND
      evt_inv.main_inv_bool = 1
   -- aircraft
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id   = evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id      = evt_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN ref_fail_sev ON
      ref_fail_sev.fail_sev_db_id = sd_fault.fail_sev_db_id AND
      ref_fail_sev.fail_sev_cd    = sd_fault.fail_sev_cd
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   -- config slot
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
;