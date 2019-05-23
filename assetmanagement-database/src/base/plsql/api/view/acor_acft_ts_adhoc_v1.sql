--liquibase formatted sql


--changeSet acor_acft_ts_adhoc_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_ts_adhoc_v1
AS
SELECT
   sched_stask.alt_id               AS sched_id,
   task_task.alt_id                 AS task_id,
   eqp_assmbl_bom.alt_id            AS bom_id,
   sched_stask.barcode_sdesc        AS barcode,
   evt_event.event_sdesc            AS event_name,
   evt_event.event_ldesc            AS event_description,
   acft_inv.alt_id                  AS aircraft_id,
   inv_ac_reg.ac_reg_cd             AS registration_code,
   eqp_assmbl_bom.assmbl_cd         AS assembly_code,
   eqp_assmbl_bom.assmbl_bom_cd     AS config_slot_code,
   evt_event.sched_priority_cd      AS schedule_priority_code,
   sched_stask.etops_bool           AS etops_flag,
   --
   evt_event.event_dt               AS event_date,
   evt_event.event_type_cd          AS event_type_code,
   evt_event.event_status_cd        AS event_status_code,
   evt_event.hist_bool              AS historical_flag
FROM
   sched_stask
   LEFT JOIN task_task ON
      sched_stask.task_db_id = task_task.task_db_id AND
      sched_stask.task_id    = task_task.task_id
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_Event.event_id
   -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id = inv_inv.inv_no_id
   INNER JOIN inv_ac_reg ON
      inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   INNER JOIN eqp_assmbl_bom ON
      inv_inv.Assmbl_Db_Id  = eqp_assmbl_bom.assmbl_db_id AND
      inv_inv.Assmbl_Cd     = eqp_assmbl_bom.Assmbl_Cd    AND
      inv_inv.Assmbl_Bom_Id = eqp_assmbl_bom.Assmbl_Bom_Id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd IN ('ACFT','SYS','ASSY')
   AND
   sched_stask.task_class_db_id = 0 AND
   sched_stask.task_class_cd    = 'ADHOC'
;