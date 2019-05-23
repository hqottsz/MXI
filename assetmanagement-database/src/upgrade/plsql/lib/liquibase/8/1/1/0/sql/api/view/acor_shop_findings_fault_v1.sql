--liquibase formatted sql


--changeSet acor_shop_findings_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_shop_findings_fault_v1
AS 
SELECT
   sd_fault.alt_id                  AS fault_id,
   sched_stask.alt_id               AS corr_sched_id,
   evt_event.event_sdesc            AS fault_name,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot_code,
   REGEXP_REPLACE(REPLACE(evt_event.event_ldesc,'<br>',CHR(10)),'(\<|\/|u|b|\>)')   AS fault_description,
   evt_event.event_status_cd        AS fault_status_code,
   corr_task_event.event_status_cd  AS corr_task_status_code,
   evt_event.actual_start_dt        AS found_on_date,
   sched_stask.barcode_sdesc        AS barcode
FROM
   sd_fault
   -- found date
   INNER JOIN evt_event on
      sd_fault.fault_db_id = evt_event.event_db_id AND
      sd_fault.fault_id    = evt_event.event_id
   INNER JOIN sched_stask ON
      evt_event.event_db_id = sched_stask.fault_db_id AND
      evt_event.event_id    = sched_stask.fault_id
   -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   -- config slot
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
   -- corrective task
   INNER JOIN evt_event corr_task_event ON
      sched_stask.sched_db_id = corr_task_event.event_db_id AND
      sched_stask.sched_id    = corr_task_event.event_id
WHERE
    -- shop findings will be always linked to TRK/SER/ASSY part
    inv_inv.inv_class_db_id = 0 AND
    inv_inv.inv_class_cd    IN ('TRK','SER','ASSY')
;