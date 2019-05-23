--liquibase formatted sql


--changeSet aopr_acft_eo_compl_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_eo_compl_v1
AS
WITH
rvw_act_req
AS
   (
     SELECT
        sched_id,
        aircraft_id inventory_id,
        task_id,
        event_status_code
     FROM
        acor_acft_ts_req_v1
     UNION ALL
     SELECT
        sched_id,
        inventory_id,
        task_id,
        event_status_code
     FROM
        acor_comp_ts_req_v1
   ),
rvw_bsl_req_ata
AS
   (
      SELECT
         task_id,
         bom_id,
         assembly_code,
         config_slot_code
      FROM
         acor_req_ata_v1
      UNION ALL
      SELECT
         task_id,
         bom_id,
         assembly_code,
         config_slot_code
      FROM
         acor_req_exec_ata_v1
   ),
rvw_req_last_done
AS
   (
      SELECT
         barcode,
         sched_id,
         task_id,
         data_type_id,
         data_type_code,
         event_date,
         time_since_new_quantity
      FROM
         aopr_acft_hist_ts_req_ldone_v1
      UNION ALL
      SELECT
         barcode_sdesc,
         sched_id,
         task_id,
         data_type_id,
         data_type_code,
         event_date,
         time_since_new_quantity
      FROM
         aopr_comp_hist_ts_req_ldone_v1
   ),
rvw_req_next_due
AS
   (
      SELECT
         barcode,
         sched_id,
         task_id,
         data_type_id,
         data_type_code,
         schedule_deadline_date,
         schedule_deadline_quantity,
         interval_quantity
      FROM
          aopr_comp_actv_ts_req_v1
      UNION
      SELECT
         barcode,
         sched_id,
         task_id,
         --data_type_db_id,
         data_type_id,
         data_type_code,
         schedule_deadline_date,
         schedule_deadline_quantity,
         interval_quantity
      FROM
         aopr_acft_actv_ts_req_v1
   ),
rvw_inventory
AS
   (
     SELECT
        inventory_id,
        h_inventory_id
     FROM
       acor_inv_serial_cntrl_asset_v1
     UNION
     SELECT
        aircraft_id inventory_id,
        aircraft_id h_inventory_id
     FROM
        acor_inv_aircraft_v1
   )
SELECT
   rvw_act_req.sched_id,
   req_task.task_id,
   req_task.task_code,
   inv_aircraft.aircraft_id,
   inv_aircraft.registration_code,
   rvw_inventory.inventory_id,
   task_class_code,
   task_subclass_code,
   rvw_act_req.event_status_code,
   originator_code,
   reference_description,
   req_task.applicability_range,
   applicability_rule,
 --  task_sched_from_db_id,
   schedule_from_code,
   effectivity_date,
   manual_scheduling_flag,
   use_manufact_date_flag,
   --resched_from_db_id,
   reschedule_from_code,
   recurring_flag,
   forecast_range,
   on_condition_flag,
   soft_deadline_flag,
   req_task.etops_flag,
 --  task_priority_db_id,
   priority_code,
   work_type_list,
   review_on_receipt_flag,
   min_plan_yield,
   req_task.account_id,
   task_description,
   engineering_description,
   instruction,
   --rev_hr_db_id,
   revision_contact,
 --  task_rev_reason_db_id,
   revision_reason_code,
   revision_note,
   organization_code,
   external_key_description,
   -- ata
   rvw_bsl_req_ata.assembly_code,
   rvw_bsl_req_ata.bom_id,
   rvw_bsl_req_ata.config_slot_code,
   req_task.revision_order,
   -- next due
   rvw_req_next_due.barcode as barcode_next_due,
   rvw_req_next_due.data_type_code,
   rvw_req_next_due.schedule_deadline_date,
   rvw_req_next_due.schedule_deadline_quantity,
   rvw_req_next_due.interval_quantity,
   -- last done
   rvw_req_last_done.barcode as barcode_last_done,
   rvw_req_last_done.event_date,
   rvw_req_last_done.time_since_new_quantity
FROM
   rvw_act_req
   -- baseline requirement
   INNER JOIN acor_req_defn_v1 req_task ON
      rvw_act_req.task_id    = req_task.task_id
   INNER JOIN rvw_bsl_req_ata ON
      rvw_act_req.task_id    = rvw_bsl_req_ata.task_id
   -- baseline scheduling rules
   INNER JOIN acor_req_sched_rules_v1 req_sched_rules ON
      rvw_act_req.task_id    = req_sched_rules.task_id
   -- next due
   LEFT JOIN rvw_req_next_due ON
      rvw_act_req.sched_id = rvw_req_next_due.sched_id
      AND
--      req_sched_rules.data_type_db_id = rvw_req_next_due.data_type_db_id AND
      req_sched_rules.data_type_id    = rvw_req_next_due.data_type_id
   -- last done
   LEFT JOIN rvw_req_last_done ON
      rvw_act_req.sched_id    = rvw_req_last_done.sched_id
      AND
--      req_sched_rules.data_type_db_id = rvw_req_last_done.data_type_db_id AND
      req_sched_rules.data_type_id    = rvw_req_last_done.data_type_id
   -- inventory
   INNER JOIN rvw_inventory ON
      rvw_act_req.inventory_id    = rvw_inventory.inventory_id
   LEFT JOIN acor_inv_aircraft_v1 inv_aircraft ON
      rvw_inventory.h_inventory_id    = inv_aircraft.aircraft_id
;