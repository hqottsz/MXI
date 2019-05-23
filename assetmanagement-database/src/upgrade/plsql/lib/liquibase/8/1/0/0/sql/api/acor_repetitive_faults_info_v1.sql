--liquibase formatted sql


--changeSet acor_repetitive_faults_info_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_repetitive_faults_info_v1
AS 
SELECT
    init_fault.corr_task_barcode AS init_fault_barcode,
    prev_fault.corr_task_barcode AS prev_fault_barcode,
    repeat_fault.corr_task_barcode,
    repeat_fault.fault_id,
    repeat_fault.init_fault_id,
    repeat_fault.corr_task_id,
    wp_stask.alt_id AS wp_task_id,
    fault_info.aircraft_id,
    fault_info.fault_name,
    fault_info.config_slot_code,
    fault_info.fault_description,
    fault_info.operational_restrictions,
    fault_info.fault_status_code,
    fault_info.corr_task_status_code,
    fault_info.found_on_date,
    fault_info.logbook_reference,
    fault_info.fault_source,
    fault_info.fault_severity,
    fault_info.deferral_class,
    fault_info.deferral_reference,
    fault_info.deferral_authorization,
    fault_info.fault_log_type,
    fault_info.registration_code,
    fault_info.found_by_hr
 FROM
     acor_repetitive_faults_v1  repeat_fault
     INNER JOIN
       (
        SELECT
           fault_id,
           aircraft_id,
           fault_name,
           config_slot_code,
           fault_description,
           operational_restrictions,
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
           registration_code,
           found_by_hr
         FROM
           acor_non_routine_fault_v1
         UNION
           SELECT
           fault_id,
           aircraft_id,
           fault_name,
           config_slot_code,
           fault_description,
           operational_restrictions,
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
           registration_code,
           found_by_hr
         FROM
           acor_logbook_fault_v1
       )
        fault_info ON
          repeat_fault.fault_id = fault_info.fault_id
        INNER JOIN acor_repetitive_faults_v1 init_fault ON
          init_fault.fault_id = repeat_fault.init_fault_id
        LEFT JOIN acor_repetitive_faults_v1 prev_fault ON
          prev_fault.fault_id = repeat_fault.prev_fault_id
        INNER JOIN sched_stask corr_stask ON
          corr_stask.alt_id = repeat_fault.corr_task_id
        INNER JOIN evt_event corr_event ON
          corr_event.event_db_id = corr_stask.sched_db_id AND
          corr_event.event_id = corr_stask.sched_id
        LEFT JOIN evt_event wp_event ON
          wp_event.event_db_id = corr_event.h_event_db_id AND
          wp_event.event_id = corr_event.h_event_id
        INNER JOIN sched_stask wp_stask ON
          wp_stask.sched_db_id = wp_event.event_db_id AND
          wp_stask.sched_id = wp_event.event_id
;