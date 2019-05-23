--liquibase formatted sql


--changeSet aopr_fl_faults_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_fl_faults_v1
AS 
SELECT
   fault_id,
   logbook_fault.sched_id,
   fault_name,
   config_slot_code,
   fault_description,
   operational_restrictions,
   event_type_code,
   fault_status_code,
   corr_task_status_code,
   found_on_date,
   logbook_reference,
   fault_source,
   fault_severity,
   deferral_class,
   deferral_reference,
   deferral_authorization,
   fault_log_type,
   barcode,
   aircraft_id,
   registration_code,
   panel_list.panel_list,
   zone_list.zone_list,
   leg_id,
   flight_number
FROM
   acor_logbook_fault_v1 logbook_fault
   LEFT JOIN acor_sched_panel_list_v1 panel_list ON
      logbook_fault.sched_id    = panel_list.sched_id
   LEFT JOIN acor_sched_zone_list_v1 zone_list ON
      logbook_fault.sched_id    = zone_list.sched_id
WHERE
   leg_id IS NOT NULL
;